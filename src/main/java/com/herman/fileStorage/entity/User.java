package com.herman.fileStorage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String githubId;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Folder> folders;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<FileEntity> files;

    public User(UUID uuid, String username, String encodedPassword) {
        this.id = uuid;
        this.username = username;
        this.password = encodedPassword;
    }
}
