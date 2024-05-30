package com.example.blogpost.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.blogpost.models.Account;
import com.example.blogpost.models.Authority;
import com.example.blogpost.models.Post;
import com.example.blogpost.service.AccountService;
import com.example.blogpost.service.AuthorityService;
import com.example.blogpost.service.PostService;
import com.example.blogpost.util.constant.Privillage;
import com.example.blogpost.util.constant.Role;

@Component
public class SeedDate implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        for(Privillage auth: Privillage.values()){
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getPrivillage());
            authorityService.save(authority);
        }
        
        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        account01.setEmail("nakkanithish27@gmail.com");
        account01.setPassword("1234");
        account01.setFirstname("user");
        account01.setLastname("Lastname");
        account01.setGender("Male");
        account01.setAge(43);
        account01.setDate_of_birth(LocalDate.parse("2000-07-19"));


        // account02.setEmail("email02@email.com"); 
        // account02.setPassword("12345602");
        // account02.setFirstname("user02");
        // account02.setLastname("Lastname");


        account02.setEmail("testofauto@gmail.com");
        account02.setPassword ("1234");
        account02.setFirstname ("admin");
        account02.setLastname ("lastname");
        account02.setGender("Male");
        account02.setAge(43);
        account02.setDate_of_birth(LocalDate.parse("2000-07-19"));
        account02.setRole (Role.ADMIN.getRole());

        account03.setEmail("editor@editor.com");
        account03.setPassword ("1234");
        account03.setFirstname ("editor");
        account03.setLastname ("lastname");
        account03.setGender("Male");
        account03.setAge(43);
        account03.setDate_of_birth(LocalDate.parse("2000-07-19"));
        account03.setRole (Role.EDITOR.getRole());

        account04.setEmail("super_editor@ditor.com");
        account04.setPassword ("1234");
        account04.setFirstname ("super_editor");
        account04.setLastname ("lastname");
        account04.setGender("Male");
        account04.setAge(43);
        account04.setDate_of_birth(LocalDate.parse("2000-07-19"));
        account04.setRole (Role.EDITOR.getRole());
        
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privillage.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        authorityService.findById(Privillage.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);


        List<Post> posts = postService.findAll();
        if(posts.size() == 0){
            Post post01 = new Post();
            post01.setTitle("Post1");
            post01.setBody("Post01 is body ..........");
            post01.setAccount(account01);
            postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("Post02");
            post02.setBody("Post02 is body ..........");
            post02.setAccount(account02);
            postService.save(post02);
            
            Post post03 = new Post();
            post03.setTitle("Post3");
            post03.setAccount(account03);
            post03.setBody("Post03 is body ..........");
            postService.save(post03);

            Post post04 = new Post();
            post04.setTitle("Post04");
            post04.setBody("Post04 is body ..........");
            post04.setAccount(account04);
            postService.save(post04);

            Post post05 = new Post();
            post05.setTitle("Post5");
            post05.setBody("Post05 is body ..........");
            post05.setAccount(account01);
            postService.save(post05);
            
            Post post06 = new Post();
            post06.setTitle("Post6");
            post06.setBody("Post06 is body ..........");
            post06.setAccount(account01);
            postService.save(post06);
            
            Post post07 = new Post();
            post07.setTitle("Post7");
            post07.setBody("Post07 is body ..........");
            post07.setAccount(account02);
            postService.save(post07);
        }
    }
    
}
