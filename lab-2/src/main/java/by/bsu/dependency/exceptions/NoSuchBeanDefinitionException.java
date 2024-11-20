package by.bsu.dependency.exceptions;

public class NoSuchBeanDefinitionException extends RuntimeException{
    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' is defined");
    }    
}
