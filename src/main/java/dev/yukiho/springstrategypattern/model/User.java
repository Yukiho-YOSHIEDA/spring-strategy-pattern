package dev.yukiho.springstrategypattern.model;

import dev.yukiho.springstrategypattern.enums.PositionName;
import dev.yukiho.springstrategypattern.exception.NullValueException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class User {

    private final PositionName positionName;
    private final String name;
    private final int currentNum;
    private final List<Book> books;

    public User(String name, PositionName positionName, List<Book> books) {
        if (Objects.isNull(name)) {
            throw new NullValueException("Name is null.");
        }
        if (Objects.isNull(positionName)) {
            throw new NullValueException("PositionName is null.");
        }
        if (Objects.isNull(books)) {
            throw new NullValueException("Books is null.");
        }

        this.name = name;
        this.currentNum = books.size();
        this.positionName = positionName;
        this.books = books;
    }

    public User(String name, PositionName positionName) {
        this(name, positionName, new ArrayList<>());
    }
}
