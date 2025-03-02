
package com.orleansgo.utilisateur.service;

import com.orleansgo.utilisateur.dto.UserRegistrationRequest;
import com.orleansgo.utilisateur.exception.EmailAlreadyExistsException;
import com.orleansgo.utilisateur.exception.PhoneNumberAlreadyExistsException;
import com.orleansgo.utilisateur.exception.ResourceNotFoundException;
import com.orleansgo.utilisateur.model.User;
import com.orleansgo.utilisateur.model.UserRole;
import com.orleansgo.utilisateur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    }

    @Transactional
    public User registerUser(UserRegistrationRequest registrationRequest) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }

        // Vérifier si le numéro de téléphone existe déjà
        if (userRepository.existsByPhoneNumber(registrationRequest.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Ce numéro de téléphone est déjà utilisé");
        }

        // Créer l'utilisateur
        Set<UserRole> roles = new HashSet<>();
        roles.add(registrationRequest.getRole());

        User user = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .emailVerified(false)
                .phoneVerified(false)
                .twoFactorEnabled(false)
                .roles(roles)
                .creationDate(LocalDateTime.now())
                .build();

        // Enregistrer l'utilisateur
        User savedUser = userRepository.save(user);
        log.info("Utilisateur enregistré avec succès: {}", savedUser.getEmail());

        // Envoyer les emails de vérification (à implémenter avec le service de notification)
        
        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        
        if (userDetails.getPhoneNumber() != null &&
                !userDetails.getPhoneNumber().equals(user.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userDetails.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Ce numéro de téléphone est déjà utilisé");
        }
        
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setProfilePicture(userDetails.getProfilePicture());
        
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        
        userRepository.delete(user);
        log.info("Utilisateur supprimé avec succès: {}", user.getEmail());
    }

    @Transactional
    public User updateLastLogin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User enableTwoFactor(Long userId, String secret) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
        
        user.setTwoFactorEnabled(true);
        user.setTwoFactorSecret(secret);
        return userRepository.save(user);
    }

    @Transactional
    public User disableTwoFactor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
        
        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);
        return userRepository.save(user);
    }

    @Transactional
    public User verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        
        user.setEmailVerified(true);
        return userRepository.save(user);
    }

    @Transactional
    public User verifyPhone(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le numéro: " + phoneNumber));
        
        user.setPhoneVerified(true);
        return userRepository.save(user);
    }
}
