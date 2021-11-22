package com.example.shopme.backend.repository.role;

import com.example.shopme.common.model.entity.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    Optional<Role> findByRoleUUID(String uuid);

    void deleteByRoleUUID(String uuid);
}
