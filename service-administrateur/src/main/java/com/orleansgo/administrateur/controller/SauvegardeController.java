
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.service.SauvegardeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sauvegardes")
public class SauvegardeController {

    private final SauvegardeService sauvegardeService;

    @Autowired
    public SauvegardeController(SauvegardeService sauvegardeService) {
        this.sauvegardeService = sauvegardeService;
    }

    @PostMapping("/creer")
    public ResponseEntity<Map<String, String>> creerSauvegarde() {
        try {
            String cheminFichier = sauvegardeService.creerSauvegarde();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sauvegarde créée avec succès");
            response.put("cheminFichier", cheminFichier);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("erreur", "Erreur lors de la création de la sauvegarde: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/restaurer")
    public ResponseEntity<Map<String, String>> restaurerSauvegarde(@RequestParam("fichier") MultipartFile fichier) {
        try {
            // Créer un répertoire temporaire pour stocker le fichier uploadé
            String tempDir = "temp_backups";
            Files.createDirectories(Paths.get(tempDir));
            
            // Sauvegarder le fichier uploadé
            String cheminFichier = tempDir + File.separator + fichier.getOriginalFilename();
            Path path = Paths.get(cheminFichier);
            Files.write(path, fichier.getBytes());
            
            // Restaurer la sauvegarde
            sauvegardeService.restaurerSauvegarde(cheminFichier);
            
            // Supprimer le fichier temporaire
            Files.delete(path);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Restauration effectuée avec succès");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("erreur", "Erreur lors de la restauration: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
