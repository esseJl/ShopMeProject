package com.example.shopme.common.validator.valid.unique_link;


import com.example.shopme.common.annotation.valid.unique_link.ValidUniqueLink;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueLinkConstraintValidator implements ConstraintValidator<ValidUniqueLink, String> {


    @Override
    public boolean isValid(String uniqueLink, ConstraintValidatorContext context) {
        char c[] = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '+', '=', ' ', '/'};
        for (Character c1 : c) {
            if (uniqueLink.contains(c1.toString())) {
                context.buildConstraintViolationWithTemplate("invalid.unique.link")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
