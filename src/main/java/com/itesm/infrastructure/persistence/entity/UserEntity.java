package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
<<<<<<< HEAD
=======
@Data
>>>>>>> c1203613c14bc6e2ac8cf48b1e5e1643b7c0f408
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name_1", nullable = false)
    private String lastName1;

    @Column(name = "last_name_2")
    private String lastName2;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Integer age;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "provider_uuid", nullable = false, unique = true)
    private String providerUuid;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role", nullable = false)
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_suburb", nullable = false)
    private SuburbEntity suburb;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
