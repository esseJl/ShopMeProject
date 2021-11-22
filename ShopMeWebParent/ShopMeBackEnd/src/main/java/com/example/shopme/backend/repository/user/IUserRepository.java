package com.example.shopme.backend.repository.user;

import com.example.shopme.common.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserUUID(String uuid);

    Optional<User> findByEmail(String email);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);

    void deleteByUserUUID(String uuid);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1%")
    Page<User> search(String keyword, Pageable pageable);
}
