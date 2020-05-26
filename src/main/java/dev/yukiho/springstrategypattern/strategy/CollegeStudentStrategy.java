package dev.yukiho.springstrategypattern.strategy;

import dev.yukiho.springstrategypattern.enums.PositionName;
import dev.yukiho.springstrategypattern.exception.CannotBorrowBookException;
import dev.yukiho.springstrategypattern.exception.CannotReturnBookException;
import dev.yukiho.springstrategypattern.model.Book;
import dev.yukiho.springstrategypattern.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CollegeStudentStrategy implements BorrowerStrategy {
    @Override
    public int getMaxBorrowNum() {
        return 10;
    }

    @Override
    public User borrowBook(User user, Book book) {
        if (user.getCurrentNum() >= getMaxBorrowNum()) {
            throw new CannotBorrowBookException();
        }
        final var books = new ArrayList<>(user.getBooks());
        books.add(book);
        return new User(user.getName(), user.getPositionName(), books);
    }

    @Override
    public User returnBook(User user, Book book) {
        if (user.getCurrentNum() == 0 || !user.getBooks().contains(book)) {
            throw new CannotReturnBookException(book.getName());
        }
        final var books = new ArrayList<>(user.getBooks());
        books.remove(book);
        return new User(user.getName(), user.getPositionName(), books);
    }


    @Override
    public PositionName getPositionName() {
        return PositionName.COLLEGE_STUDENT;
    }
}
