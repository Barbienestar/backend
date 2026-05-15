package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
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

    @Column(columnDefinition = "TINYINT", nullable = true)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Users_Hospitals",
            joinColumns = @JoinColumn(name = "id_user", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_hospital", nullable = false))
    private Set<HospitalEntity> hospitals = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
