package com.orleansgo.utilisateur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public PasswordResetToken(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusHours(1);
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
