package by.bsu.dependency.examples.simpleExample;

import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.Bean;

@Bean(name = "PrototypeRecursive2", scope = BeanScope.PROTOTYPE)
public class PrototypeRecursive2 {
    @Inject
    PrototypeRecursive1 req;
}
