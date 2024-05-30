package com.example.blogpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogpost.models.Authority;
import com.example.blogpost.util.constant.Privillage;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long>{

    public Authority save(Privillage authority);
    
}
