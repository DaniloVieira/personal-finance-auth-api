package br.com.personalfinance.auth.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pfi_user")
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "ds_name", nullable = false)
    public String name;

    @Column(name = "ds_email", nullable = false, unique = true)
    public String email;

    @Column(name = "ds_password", nullable = false)
    public String password;

    @Column(name = "dt_created_at", nullable = false)
    public LocalDateTime createdAt;

    // Panache style: find methods directly on the entity
    public static User findByEmail(String email) {
        return find("email", email).firstResult();
    }
}