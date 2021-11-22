package com.example.shopme.backend.controller.user;

import com.example.shopme.backend.service.export.user.excel.UserExcelExporter;
import com.example.shopme.backend.service.image.i.IImageService;
import com.example.shopme.backend.service.role.i.IRoleService;
import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.backend.service.user.i.IUserService;
import com.example.shopme.common.model.entity.media.image.Image;
import com.example.shopme.common.model.entity.role.Role;
import com.example.shopme.common.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/users")
@SessionAttributes({"user", "image"})
public class UserController {

    private final IUserService iUserService;
    private final IRoleService iRoleService;
    private final IImageService iImageService;
    private final UserExcelExporter userExcelExporter;

    @Autowired
    public UserController(IUserService iUserService,
                          IRoleService iRoleService,
                          IImageService iImageService,
                          UserExcelExporter userExcelExporter) {
        this.iUserService = iUserService;
        this.iRoleService = iRoleService;
        this.iImageService = iImageService;
        this.userExcelExporter = userExcelExporter;
    }

    // @ init models
    @ModelAttribute("rolesList")
    public List<Role> allRolesModel() {
        return iRoleService.getAllRoles();
    }

    @ModelAttribute("user")
    public User userModel() {
        return new User();
    }

    @ModelAttribute("image")
    public Image image() {
        return new Image();
    }


    @ModelAttribute("title")
    public String title() {
        if (userModel().getUserUUID() == null)
            return "new";
        else
            return "update";
    }


    @GetMapping("/list")
    public String listUsers(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                            @RequestParam(name = "sort", required = false, defaultValue = "lastName") String sortField,
                            @RequestParam(name = "order", required = false, defaultValue = "asc") String order,
                            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                            Model model) {


        if (page < 0) {
            page = 0;
        }

        Sort sort = Sort.by(sortField);
        sort = (order.equals("asc")) ? (sort.ascending()) : (sort.descending());

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = iUserService.findAllUser(keyword, pageable);

        model.addAttribute("usersList", users);
        model.addAttribute("totalPage", users.getTotalPages());
        model.addAttribute("totalElement", users.getTotalElements());
        model.addAttribute("size", users.getSize());
        model.addAttribute("number", users.getNumber());
        model.addAttribute("sort", sortField);
        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);

        return "/users/list";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {

        model.addAttribute("title", "new");
        model.addAttribute("user", new User());

        return "/users/form";
    }

    @PostMapping("/save")
    public String saveNewUser(@Valid @ModelAttribute("user") final User user,
                              final BindingResult bindingResult,
                              @RequestParam(name = "userImage", required = false) MultipartFile multipartFile,
                              final RedirectAttributes redirectAttributes,
                              final Model model,
                              SessionStatus sessionStatus) {


        boolean checkEmail = false;
        boolean matchPassword = false;
        boolean validation = false;


        if (iUserService.isUserExistWithEmail(user.getEmail(), user.getUserUUID())) {
            //redirectAttributes.addFlashAttribute("emailAlreadyExist", "email.already.exist");
            model.addAttribute("emailAlreadyExist", "email.already.exist");
            checkEmail = true;
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            model.addAttribute("notMatchPassword", "not.match.password");
            //redirectAttributes.addFlashAttribute("notMatchPassword", "not.match.password");
            matchPassword = true;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            model.addAttribute("user", user);
            validation = true;
        }


        if (checkEmail || matchPassword || validation) {
            return "/users/form";
        }

        if (multipartFile == null || multipartFile.isEmpty()) {
            user.setImage(null);
        } else {

            String imageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            Image image = new Image(UUID.randomUUID().toString(), imageName, multipartFile);

            try {

                iImageService.store(image);
                user.setImage(image);

            } catch (Exception | StorageMediaFileException e) {
                //System.out.println(e.getMessage());
                model.addAttribute("imageError", e.getMessage());
                return "/users/form";
            }

        }

        User savedUser = iUserService.saveNewUser(user);
        redirectAttributes.addFlashAttribute("savedUser", savedUser);
        sessionStatus.setComplete();
        return "redirect:/users/list";
    }


    @GetMapping("/update/{userId}")
    public String updateUserForm(@PathVariable String userId,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        model.addAttribute("title", "update");


        Optional<User> userWithUUID = iUserService.findUserWithUUID(userId);
        if (!userWithUUID.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "user.not.found");
            return "redirect:/users/list";
        }
        model.addAttribute("user", userWithUUID.get());
        return "/users/form";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(name = "userImage", required = false) MultipartFile multipartFile,
                             final Model model) {

        model.addAttribute("title", "update");

        Optional<User> userWithUUID = iUserService.findUserWithUUID(user.getUserUUID());
        if (!userWithUUID.isPresent()) {
            return "redirect:/users/update/" + user.getUserUUID();
        }

        User oldUser = userWithUUID.get();


        if (user.getPassword() == null || user.getPassword().equals("") || user.getPassword().equals(oldUser.getPassword())) {
            //password does not change
            user.setPassword(oldUser.getPassword());
            user.setMatchingPassword(oldUser.getPassword());
        }

        if (user.getEmail() == null || user.getEmail().equals("") || user.getEmail().equals(oldUser.getEmail())) {
            //email does not change
            user.setEmail(oldUser.getEmail());
        }


        user.setId(oldUser.getId());
        user.setImage(oldUser.getImage());

        if (!(multipartFile == null || multipartFile.isEmpty())) {

            String imageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            Image image = new Image(UUID.randomUUID().toString(), imageName, multipartFile);
            model.addAttribute("image", image);
        }


        model.addAttribute("user", user);
        return "forward:/users/update/valid";
    }


    @PostMapping("/update/valid")
    public String updateUserStep2(@Valid @ModelAttribute User user,
                                  BindingResult bindingResult,
                                  @ModelAttribute("image") Image image,
                                  Model model,
                                  SessionStatus sessionStatus) {

        model.addAttribute("title", "update");

        boolean checkEmail = false;
        boolean matchPassword = false;
        boolean validation = false;

        if (iUserService.isUserExistWithEmail(user.getEmail(), user.getUserUUID())) {
            model.addAttribute("emailAlreadyExist", "email.already.exist");
            checkEmail = true;
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            model.addAttribute("notMatchPassword", "not.match.password");
            matchPassword = true;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            model.addAttribute("user", user);
            validation = true;
        }
        if (checkEmail || matchPassword || validation) {
            return "/users/form";
        }

        if (image != null && image.getMultipartFile() != null) {

            try {
                Image deletedImage = null;

                if (user.getImage() != null) {
                    deletedImage = user.getImage();
                    deletedImage.setUser(null);
                }

                Image save = iImageService.store(image);

                if (save != null) {
                    user.setImage(save);
                }


                if (deletedImage != null) {
                    iImageService.delete(deletedImage);
                }


            } catch (Exception | StorageMediaFileException e) {
                sessionStatus.setComplete();
                model.addAttribute("imageError", e.getMessage());
                return "/users/form";
            }
        }


        User updatedUser = iUserService.updateUser(user);
        //@TODO imp redirect updated
        sessionStatus.setComplete();
        return "redirect:/users/list";
    }


    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user,
                             SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
        try {
            iUserService.deleteUser(user);
            redirectAttributes.addFlashAttribute("message", "user successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        } catch (StorageMediaFileException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        sessionStatus.setComplete();
        return "redirect:/users/list";
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) {
        try {
            userExcelExporter.export(iUserService.findAllUser("", null).getContent(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
