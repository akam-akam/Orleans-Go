
package com.orleansgo.administrateur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationSystemeDTO {
    private String cle;
    private String valeur;
    private String description;
    private String type;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String modifiePar;
}
