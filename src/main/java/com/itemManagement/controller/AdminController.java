package com.itemManagement.controller;

import com.itemManagement.entity.Item;
import com.itemManagement.entity.Mailing;
import com.itemManagement.entity.User;
import com.itemManagement.payload.RegisterRequest;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.payload.UserRequest;
import com.itemManagement.service.EmployeeService;
import com.itemManagement.service.MailingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final MailingService mailingService;

    @Autowired
    public AdminController(EmployeeService employeeService, MailingService mailingService) {
        this.employeeService = employeeService;
        this.mailingService = mailingService;
    }

    @GetMapping("/management/employees")
    public String employeesManagementPage(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                          Model model){

        int currentPage = page.orElse(0);
        Page<User> pageEmployee = employeeService.getEmployees(currentPage);
        model.addAttribute("pageEmployee", pageEmployee);

        return "user/admin/employees_management";
    }

    @GetMapping("/management/employee/new")
    public String employeeFormPage(Model model){
        model.addAttribute("userRequest",new UserRequest());
        return "user/admin/employee_form";
    }

    @GetMapping("/management/employee/edit/{id}")
    public String employeeFormPage(@PathVariable("id") Long id,Model model){
        ResponseMessage responseMessage = employeeService.getEmployee(id);
        User user = (User) responseMessage.getBody();
        model.addAttribute("userRequest",user);
        return "user/admin/employee_form";
    }

    @PostMapping("/management/employee/save")
    public String saveEmployee(@Valid @ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes){
        if(userRequest.getId() == null){
            ResponseMessage responseMessage = employeeService.newEmployee(userRequest);
            redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
            return "redirect:/admin/management/employee/new";
        }else{
            //update
            ResponseMessage responseMessage = employeeService.updateEmployee(userRequest);
            redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
            return "redirect:/admin/management/employee/edit/" + userRequest.getId();
        }
    }

    @PostMapping("/management/employee/disable/{id}")
    public String disableEmployee(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = employeeService.disableEmployee(id);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/management/employees";
    }


    @GetMapping("/settings/mailing")
    public String mailingSettingsPage(Model model){
        ResponseMessage responseMessage = mailingService.getMailing();
        Mailing mailing = (Mailing) responseMessage.getBody();
        model.addAttribute("mailing",mailing);
        return "user/admin/mailing_settings";
    }

    @PostMapping("/settings/mailing/updatehost")
    public String updateHost(@RequestParam("host") String hostRequest,
                             RedirectAttributes redirectAttributes){
        System.out.println(hostRequest);
        ResponseMessage responseMessage = mailingService.updateHost(hostRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updateport")
    public String updatePort(@RequestParam("port") String portRequest,
                             RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updatePort(portRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updateusername")
    public String updateUsername(@RequestParam("username") String usernameRequest,
                                 RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updateUsername(usernameRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updatepassword")
    public String updatePassword(@RequestParam("previousPassword") String previousPasswordRequest,
                                 @RequestParam("newPassword") String newPasswordRequest,
                                 @RequestParam("confirmNewPassword") String confirmNewPasswordRequest,
                                 RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updatePassword(previousPasswordRequest,newPasswordRequest, confirmNewPasswordRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updateprotocol")
    public String updateProtocol(@RequestParam("protocol") String protocolRequest,
                                 RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updateProtocol(protocolRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updateauth")
    public String updateAuth(@RequestParam("auth") boolean authRequest, RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updateAuth(authRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updateenabletls")
    public String updateEnableTls(@RequestParam("enableTls") boolean enableTlsRequest, RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updateEnableTls(enableTlsRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updaterequiretls")
    public String updateRequireTls(@RequestParam("requireTls") boolean requireTlsRequest, RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updateRequireTls(requireTlsRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

    @PostMapping("/settings/mailing/updateenabledebug")
    public String updateEnableDebug(@RequestParam("enableDebug") boolean enableDebugRequest, RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = mailingService.updateEnableDebug(enableDebugRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/admin/settings/mailing";
    }

}
