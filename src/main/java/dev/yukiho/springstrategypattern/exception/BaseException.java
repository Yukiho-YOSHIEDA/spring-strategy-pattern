package dev.yukiho.springstrategypattern.exception;

public class BaseException extends RuntimeException {
    protected BaseException(String message) {
        super(message);
        System.out.println(message);
    }
}
