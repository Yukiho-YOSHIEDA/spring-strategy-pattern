package dev.yukiho.springstrategypattern.model;

import dev.yukiho.springstrategypattern.exception.NullValueException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@ToString
public class Book {
    private final String name;

    public Book(String name) {
        if (Objects.isNull(name)) {
            throw new NullValueException("Name is null");
        }
        this.name = name;
    }
}
