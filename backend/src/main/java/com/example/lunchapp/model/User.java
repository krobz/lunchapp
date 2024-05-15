package com.example.lunchapp.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * The User class represents a user in the system with a unique identifier, name, and email address.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AppUser")
public class User {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @NotBlank(message = "Name is mandatory")
    private String name;
    private String email;
}
