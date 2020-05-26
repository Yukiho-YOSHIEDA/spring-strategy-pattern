package dev.yukiho.springstrategypattern.strategy;

import dev.yukiho.springstrategypattern.enums.PositionName;
import dev.yukiho.springstrategypattern.model.Book;
import dev.yukiho.springstrategypattern.model.User;

public interface BorrowerStrategy {

    int getMaxBorrowNum();

    User borrowBook(User user, Book book);

    User returnBook(User user, Book book);

    PositionName getPositionName();
}
