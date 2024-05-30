package com.example.blogpost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogpost.models.Account;




@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{

    Optional<Account> findOneByEmailIgnoreCase(String email);


    // Optional<Account> findByAge(int age);
    // Optional<Account> findByPassword_reset_token(String password_reset_token);

    Optional<Account> findByToken(String token);

    // String findByToken(String token);

    // Optional<Account>
    // String findByPassword_reset_token(String token);

    // public Optional<Account> findOneByEmailIgnoreCase(String email);
    // Optional<Account> findByToken(String token);
    // Optional<Account> findByPassword_reset_token(String token);
    
}