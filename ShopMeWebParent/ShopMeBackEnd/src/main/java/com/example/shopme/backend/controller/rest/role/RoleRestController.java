package com.example.shopme.backend.controller.rest.role;

import com.example.shopme.backend.service.role.i.IRoleService;
import com.example.shopme.common.model.entity.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/roles")
public class RoleRestController {

    private final IRoleService iRoleService;

    @Autowired
    public RoleRestController(IRoleService iRoleService) {
        this.iRoleService = iRoleService;
    }

    @PostMapping("/check-valid")
    public String checkDuplicatedRole(@RequestParam(name = "roleName", required = true) String roleName) {
        if (roleName == null || roleName.trim().length() <= 2) {
            return "invalid";
        }
        Role role = new Role();
        role.setName(roleName);
        return (iRoleService.isRoleExist(role) ? "role.already.exist" : "valid");
    }
}
