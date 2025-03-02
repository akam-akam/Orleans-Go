
package com.orleansgo.administrateur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueDTO {
    private Long id;
    private String type;
    private String label;
    private Double valeur;
    private LocalDateTime dateCreation;
    private LocalDateTime periode;
    private String metadonnees;
}
