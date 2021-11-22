package com.example.shopme.backend.controller.rest.user;

import com.example.shopme.backend.service.user.i.IUserService;
import com.example.shopme.common.model.entity.user.User;
import com.example.shopme.common.model.entity.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UserRestController {

    private final IUserService iUserService;

    @Autowired
    public UserRestController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam("id") String userId,
                                             @Email(message = "invalid") @RequestParam(name = "email", required = true, defaultValue = "") String email) {

        // message to return
        String message = "";

        // is new user or update user
        boolean isCreatingNew = (userId == null);

        //valid param
        if (email != null && email.length() > 9) {

            boolean userExist = iUserService.isUserExist(email);

            if (userExist) {

                if (isCreatingNew) {

                    message = "email.already.exist";
                } else {

                    if (iUserService.findUserWithUUID(userId).equals(iUserService.findUserWithEmail(email))) {
                        message = "valid";
                    } else {
                        message = "email.already.exist";
                    }

                }
                //message = isCreatingNew ? "email.already.exist" : "valid";
            } else {
                message = "valid";
            }

        } else {
            message = "invalid";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/find")
    public User getUser(@RequestParam(name = "id") @Size(min = 36, max = 36) String userId) {
        Optional<User> user = iUserService.findUserWithUUID(userId);
        return user.orElseThrow(() -> new UserNotFoundException("can not find user with id: " + userId));
    }

    @ExceptionHandler
    public ResponseEntity<String> notValid(Exception e) {
        return new ResponseEntity<>("invalid", HttpStatus.OK);
    }


    @ExceptionHandler
    public ResponseEntity<String> notFoundUser(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
