package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "Prototype1", scope = BeanScope.PROTOTYPE)
public class Prototype1 {

    Integer counter = 0;
    
    @Inject
    private Singleton singleton;

    void doSomething() {
        counter++;
        System.out.println("мяу (prototype1) " + counter);
    }

    @PostConstruct
    void print() {
        System.out.println("I'm prototype1 and it is my post construct method");
    }
}
