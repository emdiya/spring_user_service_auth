package com.kd.spring_user_service.common.validation;

import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class EmailValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public boolean validateEmail(UserModel reqEmail) {
        if (reqEmail == null || reqEmail.getEmail() == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(reqEmail.getEmail());
        return matcher.matches();
    }
}
