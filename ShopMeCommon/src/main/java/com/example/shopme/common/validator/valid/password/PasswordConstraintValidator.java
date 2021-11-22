package com.example.shopme.common.validator.valid.password;

import com.example.shopme.common.annotation.valid.password.ValidPassword;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    MessageResolver resolver;

    @Override
    public void initialize(ValidPassword validPassword) {
        URL resource = this.getClass().getClassLoader().getResource("messages.properties");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(resource.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        resolver = new PropertiesMessageResolver(props);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        // disable existing violation message
        //context.disableDefaultConstraintViolation();
        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(
                // at least 8 characters
                new LengthRule(8, 60),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule()

        ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);

        String messageTemplate = messages.stream()
                .collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
