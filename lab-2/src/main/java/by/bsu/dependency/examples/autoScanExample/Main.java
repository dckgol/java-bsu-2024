package by.bsu.dependency.examples.autoScanExample;

import by.bsu.dependency.context.*;
import by.bsu.dependency.examples.autoScanExample.*;;

public class Main {

    public static void main(String[] args) {
        
        ApplicationContext  applicationContext = new AutoScanApplicationContext("by.bsu.dependency.examples.autoScanExample");

        applicationContext.start();

        Singleton singleton = (Singleton) applicationContext.getBean("Singleton");
        Prototype prototype = (Prototype) applicationContext.getBean(Prototype.class);
        
        singleton.doSomething();
        prototype.doSomething();
       
        new WithoutBean().doSomething();
    }
}
