package by.bsu.dependency.examples.autoScanExample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "Prototype", scope = BeanScope.PROTOTYPE)
public class Prototype {
    @Inject
    Singleton singleton;

    void doSomething() {
        System.out.println("Woof (prot)");
    }

    @PostConstruct
    void print() {
        System.out.println("I'm allive (prot)");
    }
}
