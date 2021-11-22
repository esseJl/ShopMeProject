package com.example.shopme.backend.service.user;

import com.example.shopme.backend.repository.user.IUserRepository;
import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.backend.service.storage.i.IStorageService;
import com.example.shopme.backend.service.user.i.IUserService;
import com.example.shopme.common.model.entity.media.image.Image;
import com.example.shopme.common.model.entity.user.User;
import com.example.shopme.common.model.entity.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final IStorageService iStorageService;


    @Autowired
    public UserService(IUserRepository iUserRepository, PasswordEncoder passwordEncoder, IStorageService iStorageService) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.iStorageService = iStorageService;
    }


    @Override
    public Page<User> findAllUser(String keyword, Pageable pageable) {
        if (keyword != null) {
            return iUserRepository.search(keyword, pageable);
        }
        Page<User> users = iUserRepository.findAll(pageable);
        //Page<User> users = iUserRepository.findAllByOrderByIdDesc(pageable);
        return users;
    }


    @Override
    public Set<User> findUsersWithIdList(Set<String> usersId) {
        Set<User> userList = new HashSet<>();
        if (usersId != null) {
            for (String id : usersId) {
                Optional<User> byUuid = iUserRepository.findByUserUUID(id);
                if (byUuid.isPresent()) {
                    userList.add(byUuid.get());
                }
            }
        }
        return userList;
    }

    @Override
    public Set<User> deleteUsersWithIdList(Set<String> usersId) {
        Set<User> usersWithIdList = findUsersWithIdList(usersId);
        iUserRepository.deleteAll(usersWithIdList);
        return usersWithIdList;
    }

    @Override
    public boolean isUserExistWithEmail(String email, String uuid) {

        Optional<User> userWithEmail = findUserWithEmail(email);

        if (userWithEmail.isPresent()) {

            if (uuid.equals(userWithEmail.get().getUserUUID())) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    @Override
    public Optional<User> findUserWithEmail(String email) {
        return iUserRepository.findByEmail(email);
    }

    @Override
    public User saveNewUser(User user) {

        user.setUserUUID(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return iUserRepository.save(user);
    }

    @Override
    public Optional<User> findUserWithUUID(String userId) {
        return iUserRepository.findByUserUUID(userId);
    }

    @Override
    public User updateUser(User user) {
        if (!user.getPassword().equals(findUserWithUUID(user.getUserUUID()).get().getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return iUserRepository.save(user);
    }

    @Override
    public boolean isUserExist(String email) {
        Optional<User> byEmail = iUserRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public void deleteUser(User user) throws UserNotFoundException, StorageMediaFileException {
        Optional<User> byUuid = iUserRepository.findByUserUUID(user.getUserUUID());
        User deleteUser = byUuid.orElseThrow(() -> new UserNotFoundException("can not found user with id :" + user.getUserUUID()));
        if (deleteUser.getImage() != null) {
            Image image = deleteUser.getImage();
            iStorageService.delete(image);
        }
        iUserRepository.delete(deleteUser);
    }


}
