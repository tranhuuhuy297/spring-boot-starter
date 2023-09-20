package com.example.user_service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@Getter
@Setter
public class User {
    @Id
    private String id;

    @TextIndexed
    private String name;
    private String gmail;
    private String password;
    private boolean isActivate;

    public User(String name, String gmail, String password, boolean isActivate) {
        this.name = name;
        this.gmail = gmail;
        this.password = password;
        this.isActivate = isActivate;
    }

    @Override
    public String toString() {
        return this.name + this.gmail + this.password + this.isActivate;
    }
}
