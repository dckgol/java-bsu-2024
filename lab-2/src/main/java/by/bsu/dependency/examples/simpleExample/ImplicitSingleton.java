package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.annotation.Inject;

public class ImplicitSingleton {
    @Inject
    Prototype2 prototype2;

    int counter = 0;

    void doSomething() {
        System.out.println("I'm not a Singleton ;)");
    }
}
