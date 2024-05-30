package com.example.blogpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogpost.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
    
}
