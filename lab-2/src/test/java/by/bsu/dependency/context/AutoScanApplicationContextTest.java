package by.bsu.dependency.context;


import by.bsu.dependency.examples.autoScanExample.*;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionException;
import by.bsu.dependency.exceptions.NoSuchBeanDefinitionExceptionClassType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AutoScanApplicationContextTest {

    private AutoScanApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new AutoScanApplicationContext("by.bsu.dependency.examples.autoScanExample");
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
        assertThat(applicationContext.containsBean("Prototype")).isTrue();
        assertThat(applicationContext.containsBean("WithoutBean")).isFalse();
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

        assertThat(applicationContext.getBean("Prototype")).isNotNull().isInstanceOf(Prototype.class);
        assertThat(applicationContext.getBean("Singleton")).isNotNull().isInstanceOf(Singleton.class);
        assertThat(applicationContext.getBean(Singleton.class)).isNotNull().isInstanceOf(Singleton.class);
        assertThat(applicationContext.getBean(Prototype.class)).isNotNull().isInstanceOf(Prototype.class);
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
        assertThat(applicationContext.isSingleton("Singleton")).isTrue();
        assertThat(applicationContext.isSingleton("Prototype")).isFalse();
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
        assertThat(applicationContext.isPrototype("Singleton")).isFalse();
        assertThat(applicationContext.isPrototype("Prototype")).isTrue();
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

        assertNotEquals(applicationContext.getBean("Prototype"),applicationContext.getBean("Prototype"));
    }

    @Test
    void testInject() throws NoSuchFieldException, IllegalAccessException {
        applicationContext.start();
        Prototype prototypeBean = (Prototype) applicationContext.getBean("Prototype");
        Field singletonBeanField = prototypeBean.getClass().getDeclaredField("singleton");
        singletonBeanField.setAccessible(true);
        Object injectedSingletonBean = singletonBeanField.get(prototypeBean);
        assertThat(injectedSingletonBean).isNotNull();
        assertThat(injectedSingletonBean).isEqualTo(applicationContext.getBean(Singleton.class));
    }

}