package fr.lva.framework.pojo;

import fr.lva.framework.annotation.Encrypted;

public class User {

    private Long id;

    @Encrypted(print = true)
    private String name;

    @Encrypted
    private String password;

    public User(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
