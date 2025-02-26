package com.orleansgo.utilisateur.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "utilisateur", indexes = @Index(columnList = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 60)
    private String motDePasse;

    @Column(length = 50)
    private String prenom;

    @Column(length = 50)
    private String nom;

    @Column(unique = true, length = 15)
    private String numeroTelephone;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Set<RoleUtilisateur> roles;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;

    private String provider;
    private String providerId;

    @ElementCollection
    @Column(name = "backup_codes")
    private Set<String> backupCodes;


    private boolean actif;

    private boolean emailVerifie;

    private boolean telephoneVerifie;


    @Column(unique = true, length = 8)
    private String codeParrainage;

    @Column(precision = 19, scale = 2)
    private BigDecimal soldeBonus = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2)
    private BigDecimal soldeRetirable = BigDecimal.ZERO;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    // Spring Security Methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public String getPassword() { return motDePasse; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return actif; }
}
