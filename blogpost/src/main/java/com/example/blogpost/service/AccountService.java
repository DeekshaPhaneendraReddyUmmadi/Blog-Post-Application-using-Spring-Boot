package com.example.blogpost.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blogpost.models.Account;
import com.example.blogpost.models.Authority;
import com.example.blogpost.repository.AccountRepository;
import com.example.blogpost.util.constant.Role;

@Service
public class AccountService implements UserDetailsService{
    
    @Value("${spring.mvc.static-path-pattern}")
    private  String photo_prefix;

    // @Value("${spring.mvc.static-path-pattern}")
    // private String photo_prefix = "/resources/static/**";


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @SuppressWarnings("null")
    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if(account.getRole() == null){
            account.setRole(Role.USER.getRole());
            // account.setPhoto(null);
        }
        if(account.getPhoto() == null){
            String path = photo_prefix.replace("**" ,"images/person.jpg");
            account.setPhoto(path);
            // account.setPhoto("/resources/static/images/person.jpg");
        }

        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);
        if(!optionalAccount.isPresent()){
            System.out.println("not working");
            throw new UsernameNotFoundException("Email is not correct");
        }

        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));
        
        for(Authority _auth: account.getAuthorities()){
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
        }

        return new User(account.getEmail(),account.getPassword(),grantedAuthority); 
    }

    public Optional<Account> findOneByEmail(String authUser) {
        return accountRepository.findOneByEmailIgnoreCase(authUser);
    }
    
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }
    
    public Optional<Account> findByToken(String token) {
        return accountRepository.findByToken(token);
        // return accountRepository.findByPassword_reset_token(token);
    }
}
