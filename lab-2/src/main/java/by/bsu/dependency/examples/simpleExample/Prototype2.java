package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "Prototype2", scope = BeanScope.PROTOTYPE)
public class Prototype2 {
    @Inject
    Prototype1 prototype1;

    void doSomething() {
        System.out.println("prototype2 calling to prototype1 :) ");
        prototype1.doSomething();
    }
}