package com.example.lunchapp.model;

import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Restaurant {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;


    private String name;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
}
