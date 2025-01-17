package com.itemManagement.controller;

import com.itemManagement.entity.Item;
import com.itemManagement.entity.User;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.ItemService;
import com.itemManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public UserController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                @RequestParam(value = "keyword",required = false) String keyword,
                                @RequestParam(value = "field",required = false, defaultValue = "name") String field,
                                Model model){
        int currentPage = page.orElse(0);
        Page<Item> pageItem = itemService.getItems(currentPage,keyword,field);
        model.addAttribute("pageItem", pageItem);
        return "user/dashboard";
    }

    //profile
    @GetMapping("/profile")
    public String profilePage(Model model){
        return "user/profile";
    }

    @PostMapping("/profile/updateprofileimage")
    public String updateProfileImage(@RequestParam("profileImageUpload") MultipartFile profileImgRequest,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes){
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = userService.updateProfileImage(profileImgRequest, user.getId());
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/updatename")
    public String updateName(@RequestParam("name") String nameRequest, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = userService.updateName(nameRequest, user.getId());
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/updateemail")
    public String updateEmail(@RequestParam("email") String emailRequest, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = userService.updateEmail(emailRequest, user.getId());
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/updateusername")
    public String updateUsername(@RequestParam("username") String usernameRequest, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = userService.updateUsername(usernameRequest, user.getId());
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/updatepassword")
    public String updatePassword(@RequestParam("previousPassword") String previousPasswordRequest,
                                 @RequestParam("newPassword") String newPasswordRequest,
                                 @RequestParam("confirmNewPassword") String confirmNewPasswordRequest,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes){
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = userService.updatePassword(previousPasswordRequest,newPasswordRequest, confirmNewPasswordRequest, user.getId());
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/user/profile";
    }
}
