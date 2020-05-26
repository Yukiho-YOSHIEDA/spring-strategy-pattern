package dev.yukiho.springstrategypattern.exception;

public class CannotBorrowBookException extends BaseException {
    public CannotBorrowBookException() {
        super("The maximum number of books can be borrowed.");
    }
}
