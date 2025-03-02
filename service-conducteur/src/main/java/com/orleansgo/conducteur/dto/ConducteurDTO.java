
package com.orleansgo.conducteur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConducteurDTO {
    private UUID id;
    private UUID utilisateurId;
    private boolean disponible;
    private double note;
    private int totalCourses;
    private BigDecimal gains;
    private boolean documentsValides;
}
