package com.example.shopme.backend.service.user.i;

import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.common.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface IUserService {
    Page<User> findAllUser(String keyword, Pageable pageable);

    Set<User> findUsersWithIdList(Set<String> usersId);

    Set<User> deleteUsersWithIdList(Set<String> usersId);

    boolean isUserExistWithEmail(String email, String uuid);

    Optional<User> findUserWithEmail(String email);

    User saveNewUser(User user);

    Optional<User> findUserWithUUID(String userId);

    User updateUser(User user);

    boolean isUserExist(String email);

    void deleteUser(User user) throws StorageMediaFileException;

}
