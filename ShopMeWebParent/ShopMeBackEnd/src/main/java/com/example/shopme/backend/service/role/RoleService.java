package com.example.shopme.backend.service.role;

import com.example.shopme.backend.repository.role.IRoleRepository;
import com.example.shopme.backend.service.role.i.IRoleService;
import com.example.shopme.common.model.entity.role.Role;
import com.example.shopme.common.model.entity.role.exception.exist.RoleExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class RoleService implements IRoleService {

    private final IRoleRepository iRoleRepository;

    @Autowired
    public RoleService(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return iRoleRepository.findAll();
    }

    @Override
    public Role saveNewRole(Role role) {

        if (isRoleExist(role)) {
            new RoleExistException("this " + role.getName() + " already exist.");
        }
        role.setRoleUUID(UUID.randomUUID().toString());
        Role save = iRoleRepository.save(role);
        return save;
    }

    @Override
    public boolean isRoleExist(Role role) {
        Optional<Role> byName = iRoleRepository.findByName(role.getName());
        if (byName.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Role> findRoleByRoleId(String roleId) {
        return iRoleRepository.findByRoleUUID(roleId);
    }


    @Override
    public Role updateRole(Role updatedRole) {
        Role save = iRoleRepository.save(updatedRole);
        return save;
    }

    @Override
    @Transactional
    public void deleteRole(Role role) {
        iRoleRepository.deleteByRoleUUID(role.getRoleUUID());
    }
}
