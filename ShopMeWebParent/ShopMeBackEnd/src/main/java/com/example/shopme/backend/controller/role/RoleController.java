package com.example.shopme.backend.controller.role;

import com.example.shopme.backend.service.role.i.IRoleService;
import com.example.shopme.backend.service.user.i.IUserService;
import com.example.shopme.common.model.entity.role.Role;
import com.example.shopme.common.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final IRoleService iRoleService;
    private final IUserService iUserService;


    @Autowired
    public RoleController(IRoleService iRoleService, IUserService iUserService) {
        this.iRoleService = iRoleService;
        this.iUserService = iUserService;
    }


    @GetMapping("/list")
    public String showAllRoles(Model model) {
        model.addAttribute("rolesList", iRoleService.getAllRoles());
        return "/roles/list";
    }

    @GetMapping("/new")
    public String newRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/new";
    }

    @PostMapping("/save")
    public String saveNewUser(@Valid @ModelAttribute Role role, BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "roles/new";
        }
        if (iRoleService.isRoleExist(role)) {
            model.addAttribute("roleExistError", "role.Already.Exist");
            return "roles/new";
        }

        Role savedRole = iRoleService.saveNewRole(role);
        redirectAttributes.addFlashAttribute("roleMessageSuccess", savedRole);
        return "redirect:/roles/list";
    }

    @GetMapping("/delete/{roleId}")
    public String deleteRoleById(@PathVariable String roleId, Model model) {
        Optional<Role> roleByRoleId = iRoleService.findRoleByRoleId(roleId);
        model.addAttribute("role", roleByRoleId.get());
        model.addAttribute("usersList", roleByRoleId.get().getUsers());
        return "/roles/delete";
    }

    @PostMapping(path = "/delete")
    public String deleteRole(@Valid @ModelAttribute Role role,
                             @RequestParam(name = "users", required = false, defaultValue = "") List<String> usersId,
                             RedirectAttributes redirectAttributes) {

        iRoleService.deleteRole(role);

        Set<User> userListDeleted = iUserService.deleteUsersWithIdList(new HashSet<>(usersId));
        String userListDeletedMessage = "";
        if (!userListDeleted.isEmpty()) {
            userListDeletedMessage = " these users also deleted -> ";
            for (User user : userListDeleted) {
                userListDeletedMessage += " " + user.getEmail() + ",\n ";
            }
        }

        redirectAttributes.addFlashAttribute("roleMessageSuccess",
                role.getName() + " deleted successfully." + userListDeletedMessage);
        return "redirect:/roles/list";
    }


}
