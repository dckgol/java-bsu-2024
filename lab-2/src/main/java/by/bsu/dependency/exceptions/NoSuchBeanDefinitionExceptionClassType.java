package by.bsu.dependency.exceptions;

public class NoSuchBeanDefinitionExceptionClassType extends RuntimeException{
    public NoSuchBeanDefinitionExceptionClassType(String name) {
        super("No bean with class '" + name + "' is defined");
    }    
}
