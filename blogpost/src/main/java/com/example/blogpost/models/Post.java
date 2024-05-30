package com.example.blogpost.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post{
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Missing Post Title")
    private String title;
    
    @NotBlank(message = "Missing Post Body")
    @Column(columnDefinition = "TEXT") // USED for longer text inside the db
    private String body;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "account_id",referencedColumnName = "id",nullable = true)
    private Account account;
}