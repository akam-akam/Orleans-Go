
package com.orleansgo.administrateur.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SauvegardeService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SauvegardeService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public String creerSauvegarde() throws IOException {
        // Créer un répertoire pour les sauvegardes s'il n'existe pas
        String backupDir = "backups";
        Files.createDirectories(Paths.get(backupDir));

        // Créer un nom de fichier basé sur la date et l'heure
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFileName = "backup_" + timestamp + ".json";
        String backupPath = backupDir + File.separator + backupFileName;

        // Liste des tables à sauvegarder
        List<String> tables = List.of(
                "administrateur", 
                "commission", 
                "programme_parrainage", 
                "verification_documents",
                "rapport"
        );

        Map<String, List<Map<String, Object>>> backup = new HashMap<>();

        // Extraire les données de chaque table
        for (String table : tables) {
            String query = "SELECT * FROM " + table;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
            backup.put(table, rows);
        }

        // Écrire le fichier de sauvegarde
        objectMapper.writeValue(new File(backupPath), backup);

        return backupPath;
    }

    @Transactional
    public void restaurerSauvegarde(String cheminFichier) throws IOException {
        Path backupFile = Paths.get(cheminFichier);
        if (!Files.exists(backupFile)) {
            throw new IOException("Le fichier de sauvegarde n'existe pas: " + cheminFichier);
        }

        // Lire le fichier de sauvegarde
        Map<String, List<Map<String, Object>>> backup = objectMapper.readValue(
                backupFile.toFile(),
                objectMapper.getTypeFactory().constructMapType(
                        HashMap.class,
                        objectMapper.getTypeFactory().constructType(String.class),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
                )
        );

        // Pour chaque table, supprimer les données existantes et insérer les données sauvegardées
        for (Map.Entry<String, List<Map<String, Object>>> entry : backup.entrySet()) {
            String table = entry.getKey();
            List<Map<String, Object>> rows = entry.getValue();

            // Supprimer les données existantes
            jdbcTemplate.update("DELETE FROM " + table);

            // Insérer les données sauvegardées
            for (Map<String, Object> row : rows) {
                StringBuilder columns = new StringBuilder();
                StringBuilder values = new StringBuilder();
                StringBuilder params = new StringBuilder();
                Object[] paramValues = new Object[row.size()];
                int i = 0;

                for (Map.Entry<String, Object> field : row.entrySet()) {
                    if (i > 0) {
                        columns.append(", ");
                        values.append(", ");
                        params.append(", ");
                    }
                    columns.append(field.getKey());
                    values.append("?");
                    params.append(field.getKey()).append("=?");
                    paramValues[i] = field.getValue();
                    i++;
                }

                String insertSql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
                jdbcTemplate.update(insertSql, paramValues);
            }
        }
    }
}
