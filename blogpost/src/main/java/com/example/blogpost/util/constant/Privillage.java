package com.example.blogpost.util.constant;

public enum Privillage {
    RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l,"ACCESS_ADMIN_PANEL");

    private Long id;
    private String privillage;
    
    private Privillage(Long id, String privillage) {
        this.id = id;
        this.privillage = privillage;
    }

    public Long getId() {
        return id;
    }

    public String getPrivillage() {
        return privillage;
    }
}
