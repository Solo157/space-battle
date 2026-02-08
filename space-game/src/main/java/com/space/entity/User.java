package com.space.entity;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String username;
    private boolean isAdmin;
    private String password;
    private boolean isPrivilegedUser;

    public User(Integer id, String username, boolean isAdmin, String password, boolean isPrivilegedUser) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
        this.password = password;
        this.isPrivilegedUser = isPrivilegedUser;
    }

}
