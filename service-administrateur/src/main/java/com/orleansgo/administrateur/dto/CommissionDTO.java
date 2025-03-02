
package com.orleansgo.administrateur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionDTO {
    private String id;
    private BigDecimal tauxCommission;
    private BigDecimal bonusParrain;
    private BigDecimal bonusFilleul;
    private LocalDateTime dateEffective;
    private LocalDateTime dateCreation;
    private String administrateurId;
    private String description;
}
