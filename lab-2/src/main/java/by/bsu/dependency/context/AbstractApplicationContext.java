package by.bsu.dependency.context;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionExceptionClassType;

public abstract class AbstractApplicationContext implements ApplicationContext {

    Map<String, Class<?>> beanDefinitions;
    Map<String, Object> beansSingletons = new HashMap<>();
    ContextStatus contextStatus = ContextStatus.NOT_STARTED;

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    public void start() {
        beanDefinitions.forEach((beanName, beanClass) -> {
            if (isSingleton(beanName)) {
                    beansSingletons.put(beanName, instantiateBean(beanClass));
            }
        });
        contextStatus = ContextStatus.STARTED;
        beansSingletons.forEach((beanName, beanObject) ->  {
            inject(beansSingletons.get(beanName));
        });
        beansSingletons.forEach((beanName, beanObject) ->  {
            invokePostConstruct(beansSingletons.get(beanName));
        });
    }   


    public boolean isRunning() {
        return contextStatus == ContextStatus.STARTED;
    }

    public boolean containsBean(String name) {
        checkIfContextIsRunning();
        return beanDefinitions.containsKey(name);
    }

    public Object getBean(String name) {
        checkIfContextIsRunning();
        checkBeanExists(name);

        if (isSingleton(name)) {
            return beansSingletons.get(name);
        }
        return createPrototype(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        checkIfContextIsRunning();
        String beanName = findBeanNameByClass(clazz);
        return (T) getBean(beanName);
    }

    String decapitalize(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    public boolean isPrototype(String name) {
        return !isSingleton(name);
    }

    public boolean isSingleton(String name) {
        // checkBeanExists(name);
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return !beanDefinitions.get(name).isAnnotationPresent(Bean.class) ||
                beanDefinitions.get(name).getAnnotation(Bean.class).scope() == BeanScope.SINGLETON;
    }

    protected void checkIfContextIsRunning() {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
    }

    protected void checkBeanExists(String name) {
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
    }


    protected String findBeanNameByClass(Class<?> clazz) {
        return beanDefinitions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(clazz))
                .map(Map.Entry::getKey)
                .findFirst() 
                .orElseThrow(() -> new NoSuchBeanDefinitionExceptionClassType(clazz.getName()));
    }


    public void inject (Object object) {
        Arrays.stream(object.getClass().getDeclaredFields()).forEach(
                field -> {
                    if (field.isAnnotationPresent(Inject.class)) {
                        field.setAccessible(true);
                        Object value = getBean(field.getType());
                        try {
                            field.set(object, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    void invokePostConstruct(Object object) {
        Arrays.stream(object.getClass().getDeclaredMethods()).forEach(
                method -> {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        method.setAccessible(true);
                        try {
                            method.invoke(object);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    public <T> T instantiateBean(Class<T> beanClass) {
        try {
            return beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    Object createPrototype(String name) {
        checkBeanExists(name);
        Object bean = instantiateBean(beanDefinitions.get(name));
        inject(bean);
        invokePostConstruct(bean);
        return bean;
    }

}
