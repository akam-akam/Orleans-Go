package com.orleansgo.utilisateur.controller; // Package cohérent avec l'architecture

import com.orleansgo.utilisateur.dto.UtilisateurDTO;
import com.orleansgo.utilisateur.exception.ResourceAlreadyExistsException;
import com.orleansgo.utilisateur.exception.ResourceNotFoundException;
import com.orleansgo.utilisateur.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gestion des Utilisateurs") // Nécessite l'import Swagger
@RestController
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Operation(summary = "Créer un nouvel utilisateur")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurDTO creerUtilisateur(
            @Valid @RequestBody UtilisateurDTO utilisateurDTO
    ) throws ResourceAlreadyExistsException {
        return utilisateurService.creerUtilisateur(utilisateurDTO);
    }

    @Operation(summary = "Trouver un utilisateur par email")
    @GetMapping("/{email}")
    public ResponseEntity<UtilisateurDTO> trouverParEmail(@PathVariable String email) {
        return utilisateurService.trouverParEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}
