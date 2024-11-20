package by.bsu.dependency.context;

import by.bsu.dependency.examples.simpleExample.*;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionExceptionClassType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

class SimpleApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new SimpleApplicationContext(
                Singleton.class, Prototype1.class, ImplicitSingleton.class,  Prototype2.class
        );
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.containsBean("Singleton")
        );
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("Singleton")).isTrue();
        assertThat(applicationContext.containsBean("Prototype1")).isTrue();
        assertThat(applicationContext.containsBean("implicitSingleton")).isTrue();
        assertThat(applicationContext.containsBean("rand")).isFalse();
    }

    @Test
    void testContextGetBeanNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("Prototype1")
        );
    }

    @Test
    void testGetBeanReturns() {
        applicationContext.start();

        assertThat(applicationContext.getBean("Prototype1")).isNotNull().isInstanceOf(Prototype1.class);
        assertThat(applicationContext.getBean("implicitSingleton")).isNotNull().isInstanceOf(ImplicitSingleton.class);
        assertThat(applicationContext.getBean(Singleton.class)).isNotNull().isInstanceOf(Singleton.class);
        assertThat(applicationContext.getBean(Prototype2.class)).isNotNull().isInstanceOf(Prototype2.class);
    }

    @Test
    void testGetBeanThrows() {
        applicationContext.start();

        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean("randomName")
        );
        assertThrows(
                NoSuchBeanDefinitionExceptionClassType.class,
                () -> applicationContext.getBean(Integer.class)
        );
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("implicitSingleton")).isTrue();
        assertThat(applicationContext.isSingleton("Singleton")).isTrue();
        assertThat(applicationContext.isSingleton("Prototype2")).isFalse();
        assertThat(applicationContext.isSingleton("Prototype2")).isFalse();
    }

    @Test
    void testIsSingletonThrows() {
        assertThrows(
                // TODO: уточнить класс исключения (NoSuchBeanDefinitionException)
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("implicitSingleton")).isFalse();
        assertThat(applicationContext.isPrototype("Singleton")).isFalse();
        assertThat(applicationContext.isPrototype("Prototype2")).isTrue();
        assertThat(applicationContext.isPrototype("Prototype2")).isTrue();
    }

    @Test
    void testIsPrototypeThrows() {
        assertThrows(
                // TODO: уточнить класс исключения (NoSuchBeanDefinitionException)
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testSameElementsReturns() {
        applicationContext.start();

        assertEquals(applicationContext.getBean("Singleton"), applicationContext.getBean("Singleton"));
    }

    @Test
    void testDifferentElementsReturns() {
        applicationContext.start();

        assertNotEquals(applicationContext.getBean("Prototype1"),applicationContext.getBean("Prototype1"));
    }

    @Test
    void testInject() throws NoSuchFieldException, IllegalAccessException {
        applicationContext.start();
        Prototype1 prototypeBean = (Prototype1) applicationContext.getBean("Prototype1");
        Field singletonBeanField = prototypeBean.getClass().getDeclaredField("singleton");
        singletonBeanField.setAccessible(true);
        Object injectedSingletonBean = singletonBeanField.get(prototypeBean);
        assertThat(injectedSingletonBean).isNotNull();
        assertThat(injectedSingletonBean).isEqualTo(applicationContext.getBean(Singleton.class));
    }

    @Test
    void TestRecursiveInject() {
        applicationContext = new SimpleApplicationContext(PrototypeRecursive1.class, PrototypeRecursive2.class);
        applicationContext.start();
        assertThrows(
                StackOverflowError.class,
                () -> applicationContext.getBean("PrototypeRecursive1")
        );
    }
}
