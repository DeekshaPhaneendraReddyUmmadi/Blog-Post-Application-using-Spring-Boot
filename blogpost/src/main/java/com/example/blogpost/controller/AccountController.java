package com.example.blogpost.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.blogpost.models.Account;
import com.example.blogpost.service.AccountService;
import com.example.blogpost.service.EmailService;
import com.example.blogpost.util.AppUtil;
import com.example.blogpost.util.email.EmailDetails;

import jakarta.validation.Valid;
import lombok.extern.java.Log;

@Controller
@Log
public class AccountController {

    @Value("${spring.mvc.static-path-pattern}")
    private String photo_prefix;
    
    @Value("${password.token.reset.timeout.minutes}")
    private int password_token_timeout;


    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
        // System.out.println("Not Regestered");
        if (result.hasErrors()) {
            return "account_views/register";
        } 

        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login1";
    }

    

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile (Model model, Principal principal){
        String authUser = "email";
        if(principal != null){
            authUser =  principal.getName();
        }
        // String authUser = SecurityContextHolder.getContext().getAuthentication().getName();
        // System.out.println(authUser);
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
            if(optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                model.addAttribute( "account", account);
                model.addAttribute("photo", account.getPhoto());
                return  "account_views/profile";
        }else{
            return "redirect:/?error";
        }
    }   
    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String post_profile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal,Model model){
        if (result.hasErrors()) {
            return "account_views/profile";
        } 
        String authUser = "email";
        if(principal != null){
            authUser =  principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent()){
            Account account_by_id = accountService.findById(account.getId()).get(); 
            account_by_id.setEmail(account.getEmail());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setLastname(account.getLastname());
            account_by_id.setAge(account.getAge());
            account_by_id.setGender(account.getGender());
            account_by_id.setPassword(account.getPassword());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            // account_by_id.setPhoto(account);

            accountService.save(account_by_id);
            return "redirect:/";
        }
        return "redirect:/?error";
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String update_photo(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,Principal principal){
        if(file.isEmpty()){
            attributes.addFlashAttribute("error","No file uploaded.");
            return "redirect:/profile";
        }else{
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;
                String genratedString = RandomStringUtils.random(length,useLetters,useNumbers);
                String final_photo_name = genratedString + fileName;
                String fileLocation = AppUtil.get_upload_path(final_photo_name);
                
                Path path = Paths.get(fileLocation);
                Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
                
                String authUser = "email";
                if(principal != null){
                    authUser =  principal.getName();
                }
                
                Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                if(optionalAccount.isPresent()){
                    Account account = optionalAccount.get();
                    Account account_by_id = accountService.findById(account.getId()).get();
                    String relative_fileLocation = photo_prefix.replace("**", "uploads/"+final_photo_name);
                    account_by_id.setPhoto(relative_fileLocation);
                    accountService.save(account_by_id);
                }
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
                return "redirect:/profile";
            } catch (Exception e) {
            }
        }
        return "";
    }


    @GetMapping("/forgot_password")
    // @PreAuthorize("isAuthenticated()")
    public String forgotPassword(Model model){
        return "account_views/forgot_password";
    }

    @PostMapping("/reset_password")
    // @PreAuthorize("isAuthenticated()")
    public String reset_password(@RequestParam("email") String _email,RedirectAttributes attributes, Model model){
        Optional<Account> optionalAccount = accountService.findOneByEmail(_email);
        if(optionalAccount.isPresent()){
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String reset_token = UUID.randomUUID().toString();
            account.setToken(reset_token);
            account.setPassword_reset_token_expiry(LocalDateTime.now().plusMinutes(password_token_timeout));
            accountService.save(account);
            // String reset_message = "This is the reset password link: http://www.google.com";
            String reset_message = "This is the reset password link: http://localhost:8080/change_password?token="+reset_token;
            EmailDetails emailDetails = new EmailDetails(account.getEmail(),reset_message,"Reset Password");
            if(emailService.sendSimpleEmail(emailDetails) == false){
                attributes.addFlashAttribute("message" , "Error while sending email");    
                return "redirect:/forgot_password";
            }
            attributes.addFlashAttribute("message" , "Password reset email sent sucessfully!!!");
            return "redirect:/login";
        }else{
            attributes.addFlashAttribute("message" , "No user Found with this email");    
            return "redirect:/forgot_password";
        }
    }

    @GetMapping("/change_password")
    public String change_password(@RequestParam("token") String token,RedirectAttributes attributes,Model model){
        if(token.equals("") || token.equals(null)){
            attributes.addFlashAttribute("error","Invalid Token");
            return "redirect:/forgot_password";
        }
        Optional<Account> optional_account = accountService.findByToken(token);
        System.out.println(token);
        System.out.println(optional_account.get().getToken());
        if(optional_account.isPresent()){
            // System.out.println("inside the change password");
            Account account = accountService.findById(optional_account.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(optional_account.get().getPassword_reset_token_expiry())){
                attributes.addFlashAttribute("error","Token Expired");
                // System.out.println("inside the change password inside of the token expired");
                return "redirect:/forgot_password";
            }
            model.addAttribute("account", account);
            return "account_views/change_password";
        }
        attributes.addFlashAttribute("error","Invalid token");
        return "redirect:/forgot_password";
    }   

    // @PostMapping("/change_password")
    // public String post_change_password(@ModelAttribute Account account ,RedirectAttributes attributes){
    //     // System.out.println(password);
    //     System.out.println(account.getPassword());
    //     Account account_by_id = accountService.findById(account.getId()).get();
    //     account_by_id.setPassword(account.getPassword());
    //     account_by_id.setToken("");
    //     accountService.save(account_by_id);
    //     attributes.addFlashAttribute("message" ,"Password updated");
    //     System.out.println("hello");
    //     return "redirect:/";
    // }

    @PostMapping("/change_password")
    public String post_change_password(@ModelAttribute Account account , RedirectAttributes attributes){

        Account account_by_id = accountService.findById(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setToken(null);
        account_by_id.setPassword_reset_token_expiry(null);
        accountService.save(account_by_id);
        attributes.addFlashAttribute("message" ,"Password Updated");

        // System.out.println(account_by_id.getPassword());
        // System.out.println(account.getPassword());

        // System.out.println("password updated successfully!!");

        log.info("password is successfully updated:::");
        return "redirect:/login";
    }

}
