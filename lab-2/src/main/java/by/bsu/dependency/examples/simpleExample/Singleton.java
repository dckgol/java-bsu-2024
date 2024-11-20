package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "Singleton", scope = BeanScope.SINGLETON)
public class Singleton {

    int counter;

    @Inject
    private Prototype1 prototype1;

    @Inject 
    private ImplicitSingleton implicitSingleton;

    public void doSomething() {
        counter++;
        System.out.println("counter in singleton: " + counter);
    }

    @PostConstruct
    void print() {
        System.out.println("I'm singleton and it is my post construct method");
    }
}
