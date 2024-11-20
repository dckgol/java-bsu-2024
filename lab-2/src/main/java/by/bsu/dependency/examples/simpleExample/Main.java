package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.context.*;
import by.bsu.dependency.examples.simpleExample.*;;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SimpleApplicationContext(
                Singleton.class, Prototype1.class, ImplicitSingleton.class,  Prototype2.class
        );
        applicationContext.start();

        Singleton singleton = (Singleton) applicationContext.getBean("Singleton");
        Prototype1 prototype1 = (Prototype1) applicationContext.getBean(Prototype1.class);
        Prototype2 prototype2 = (Prototype2) applicationContext.getBean("Prototype2");
        ImplicitSingleton implicitSingleton = (ImplicitSingleton) applicationContext.getBean(ImplicitSingleton.class);

        singleton.doSomething();
        prototype1.doSomething();
        prototype2.doSomething();
        implicitSingleton.doSomething();
        implicitSingleton.prototype2.doSomething();

        singleton = (Singleton) applicationContext.getBean("Singleton");
        prototype1 = (Prototype1) applicationContext.getBean("Prototype1");
        singleton.doSomething();
        prototype1.doSomething();
    }
}
