package by.bsu.dependency.context;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionException;


public class HardCodedSingletonApplicationContext extends AbstractApplicationContext {

    /**
     * ! Класс существует только для базового примера !
     * <br/>
     * Создает контекст, содержащий классы, переданные в параметре. Полагается на отсутсвие зависимостей в бинах,
     * а также на наличие аннотации {@code @Bean} на переданных классах.
     * <br/>
     * ! Контекст данного типа не занимается внедрением зависимостей !
     * <br/>
     * ! Создает только бины со скоупом {@code SINGLETON} !
     *
     * @param beanClasses классы, из которых требуется создать бины
     */
    public HardCodedSingletonApplicationContext(Class<?>... beanClasses) {
        this.beanDefinitions = Arrays.stream(beanClasses).collect(
                Collectors.toMap(
                        beanClass -> beanClass.getAnnotation(Bean.class).name(),
                        Function.identity()
                )
        );
    }

    
    @Override
    public void start() {
        contextStatus = ContextStatus.STARTED;
        beanDefinitions.forEach((beanName, beanClass) -> beansSingletons.put(beanName, instantiateBean(beanClass)));
        beansSingletons.forEach((beanName, beanObject) ->  {
            inject(beansSingletons.get(beanName));
        });
        beansSingletons.forEach((beanName, beanObject) ->  {
            invokePostConstruct(beansSingletons.get(beanName));
        });
    }

    @Override
    public boolean containsBean(String name) {
        checkIfContextIsRunning();
        return beansSingletons.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        // checkIfContextIsRunning();
        checkBeanExists(name);
        return beansSingletons.get(name);
    }

    @Override
    protected void checkBeanExists(String name) {
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
    }

    @Override
    public boolean isPrototype(String name) {
        return !isSingleton(name);
    }

    @Override
    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return true;
    }

}
