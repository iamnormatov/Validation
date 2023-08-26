package com.example.validation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = "password", allowSetters = true)
@Table(name = "users")
@NamedQuery(name = "search", query = "select u from User as u " +
        "where coalesce(:id, u.userId) = u.userId " +
        "or coalesce(:f, u.firstName) = u.firstName " +
        "or coalesce(:l, u.lastName) = u.lastName " +
        "or coalesce(:a, u.age) = u.age " +
        "or coalesce(:e, u.email) = u.email " +
        "and u.deletedAt is null"

)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    private Boolean enabled;
    private Integer age;
    private String email;
    private String password;

    @OneToMany(mappedBy = "username", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Authorities> authorities;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}






