package com.example.shopme.backend.service.role.i;

import com.example.shopme.common.model.entity.role.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<Role> getAllRoles();

    Role saveNewRole(Role role);

    boolean isRoleExist(Role role);

    Optional<Role> findRoleByRoleId(String roleId);

    Role updateRole(Role updatedRole);

    void deleteRole(Role role);
}
