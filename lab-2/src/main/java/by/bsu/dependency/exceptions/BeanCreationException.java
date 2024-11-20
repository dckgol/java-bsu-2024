package by.bsu.dependency.exceptions;

public class BeanCreationException extends RuntimeException{
    public BeanCreationException(String s, ReflectiveOperationException e) {
        super(s, e);
    }
}
