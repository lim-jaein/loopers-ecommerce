package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String birthDate;    // yyyy-MM-dd

    public User(String loginId, String password, String email, String birthDate) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
    }

    public static User create(String loginId, String password, String email, String birthDate) {
        validLoginID(loginId);
        validEmail(email);
        validBirthDate(birthDate);
        return new User(loginId, password, email, birthDate);
    }

    private static void validBirthDate(String birthDate) {
        if(!birthDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 올바르지 않습니다.");
        }
    }

    private static void validEmail(String email) {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
        }
    }

    private static void validLoginID(String loginId) {
        if (!loginId.matches("^[a-zA-Z0-9]{1,10}$")) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID 형식이 올바르지 않습니다.");
        }
    }
}
