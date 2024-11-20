package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "PrototypeRecursive1", scope = BeanScope.PROTOTYPE)
public class PrototypeRecursive1 {

    @Inject 
    PrototypeRecursive2 req;
}
