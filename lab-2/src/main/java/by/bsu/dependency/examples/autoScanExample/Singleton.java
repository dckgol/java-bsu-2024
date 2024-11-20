package by.bsu.dependency.examples.autoScanExample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "Singleton", scope = BeanScope.SINGLETON)
public class Singleton {
    @Inject
    Prototype prototype;

    void doSomething() {
        System.out.println("Meow (singl)");
    }

    @PostConstruct
    void print() {
        System.out.println("I'm allive (singl)");
    }
}
