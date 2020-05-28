package dev.yukiho.springstrategypattern.factory;

import dev.yukiho.springstrategypattern.enums.PositionName;
import dev.yukiho.springstrategypattern.exception.CannotBorrowBookException;
import dev.yukiho.springstrategypattern.exception.CannotReturnBookException;
import dev.yukiho.springstrategypattern.model.Book;
import dev.yukiho.springstrategypattern.model.User;
import dev.yukiho.springstrategypattern.strategy.CollegeStudentStrategy;
import dev.yukiho.springstrategypattern.strategy.GraduateStudentStrategy;
import dev.yukiho.springstrategypattern.strategy.ProfessorStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BorrowerStrategyFactoryTest {

    static final String PROFESSOR = "Professor";
    static final String GRADUATE_STUDENT = "GraduateStudent";
    static final String COLLEGE_STUDENT = "CollegeStudent";

    @Autowired
    BorrowerStrategyFactory borrowerStrategyFactory;

    @Test
    void canGetAllStrategy() {
        final var professorStrategy = borrowerStrategyFactory.findStrategy(PositionName.PROFESSOR);
        assertThat(professorStrategy).isInstanceOf(ProfessorStrategy.class);
        final var graduateStudentStrategy = borrowerStrategyFactory.findStrategy(PositionName.GRADUATE_STUDENT);
        assertThat(graduateStudentStrategy).isInstanceOf(GraduateStudentStrategy.class);
        final var collegeStudentStrategy = borrowerStrategyFactory.findStrategy(PositionName.COLLEGE_STUDENT);
        assertThat(collegeStudentStrategy).isInstanceOf(CollegeStudentStrategy.class);
    }

    @ParameterizedTest
    @MethodSource
    void successBorrowBook(User user) {
        final var borrowerStrategy = borrowerStrategyFactory.findStrategy(user.getPositionName());
        user = borrowerStrategy.borrowBook(user, new Book("Book1"));
        assertThat(user.getCurrentNum()).isEqualTo(1);
    }

    static Stream<Arguments> successBorrowBook() {
        return Stream.of(
            Arguments.arguments(new User(PROFESSOR, PositionName.PROFESSOR)),
            Arguments.arguments(new User(GRADUATE_STUDENT, PositionName.GRADUATE_STUDENT)),
            Arguments.arguments(new User(COLLEGE_STUDENT, PositionName.COLLEGE_STUDENT))
        );
    }

    @ParameterizedTest
    @MethodSource
    void successBorrowBookOfLimit(User user) {
        final var borrowerStrategy = borrowerStrategyFactory.findStrategy(user.getPositionName());

        final List<Book> books = new ArrayList<>();
        for (int i = 1; i < borrowerStrategy.getMaxBorrowNum(); i++) {
            books.add(new Book("Book" + i));
        }
        user = new User(user.getName(), user.getPositionName(), books);
        user = borrowerStrategy.borrowBook(user, new Book("LimitBook"));
        assertThat(user.getCurrentNum()).isEqualTo(borrowerStrategy.getMaxBorrowNum());
    }

    static Stream<Arguments> successBorrowBookOfLimit() {
        return Stream.of(
            Arguments.arguments(new User(PROFESSOR, PositionName.PROFESSOR)),
            Arguments.arguments(new User(GRADUATE_STUDENT, PositionName.GRADUATE_STUDENT)),
            Arguments.arguments(new User(COLLEGE_STUDENT, PositionName.COLLEGE_STUDENT))
        );
    }

    @ParameterizedTest
    @MethodSource
    void failedBorrowBook(User user) {
        final var borrowerStrategy = borrowerStrategyFactory.findStrategy(user.getPositionName());

        final List<Book> books = new ArrayList<>();
        for (int i = 1; i < borrowerStrategy.getMaxBorrowNum() + 1; i++) {
            books.add(new Book("Book" + i));
        }
        final var fullBookUser = new User(user.getName(), user.getPositionName(), books);
        assertThrows(CannotBorrowBookException.class,
            () -> borrowerStrategy.borrowBook(fullBookUser, new Book("FailedBook")));
    }

    static Stream<Arguments> failedBorrowBook() {
        return Stream.of(
            Arguments.arguments(new User(PROFESSOR, PositionName.PROFESSOR)),
            Arguments.arguments(new User(GRADUATE_STUDENT, PositionName.GRADUATE_STUDENT)),
            Arguments.arguments(new User(COLLEGE_STUDENT, PositionName.COLLEGE_STUDENT))
        );
    }

    @ParameterizedTest
    @MethodSource
    void successReturnBook(User user) {
        final var borrowerStrategy = borrowerStrategyFactory.findStrategy(user.getPositionName());
        final var book = new Book("Book1");
        user = borrowerStrategy.borrowBook(user, book);
        assertThat(user.getCurrentNum()).isEqualTo(1);
        user = borrowerStrategy.returnBook(user, book);
        assertThat(user.getCurrentNum()).isEqualTo(0);
    }

    static Stream<Arguments> successReturnBook() {
        return Stream.of(
            Arguments.arguments(new User(PROFESSOR, PositionName.PROFESSOR)),
            Arguments.arguments(new User(GRADUATE_STUDENT, PositionName.GRADUATE_STUDENT)),
            Arguments.arguments(new User(COLLEGE_STUDENT, PositionName.COLLEGE_STUDENT))
        );
    }

    @ParameterizedTest
    @MethodSource
    void failedReturnBookWhenEmpty(User user) {
        final var borrowerStrategy = borrowerStrategyFactory.findStrategy(user.getPositionName());
        final var book = new Book("Book1");
        assertThrows(CannotReturnBookException.class, () -> borrowerStrategy.returnBook(user, book));
    }

    static Stream<Arguments> failedReturnBookWhenEmpty() {
        return Stream.of(
            Arguments.arguments(new User(PROFESSOR, PositionName.PROFESSOR)),
            Arguments.arguments(new User(GRADUATE_STUDENT, PositionName.GRADUATE_STUDENT)),
            Arguments.arguments(new User(COLLEGE_STUDENT, PositionName.COLLEGE_STUDENT))
        );
    }

    @ParameterizedTest
    @MethodSource
    void failedReturnBookWhenNotFoundBook(User user) {
        final var borrowerStrategy = borrowerStrategyFactory.findStrategy(user.getPositionName());
        final var failedUser = borrowerStrategy.borrowBook(user, new Book("Book1"));
        assertThrows(CannotReturnBookException.class, () -> borrowerStrategy.returnBook(failedUser, new Book("Book2")));
        assertThat(failedUser.getCurrentNum()).isEqualTo(1);
    }

    static Stream<Arguments> failedReturnBookWhenNotFoundBook() {
        return Stream.of(
            Arguments.arguments(new User(PROFESSOR, PositionName.PROFESSOR)),
            Arguments.arguments(new User(GRADUATE_STUDENT, PositionName.GRADUATE_STUDENT)),
            Arguments.arguments(new User(COLLEGE_STUDENT, PositionName.COLLEGE_STUDENT))
        );
    }

}
