
package com.orleansgo.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionTrajetDTO {

    private Long id;

    @NotNull(message = "L'ID du trajet est obligatoire")
    private Long trajetId;

    @NotNull(message = "Le montant total du trajet est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être supérieur à zéro")
    private BigDecimal montantTotalTrajet;

    private BigDecimal montantCommission;

    @NotNull(message = "L'ID de la commission est obligatoire")
    private Long commissionId;
    
    private String nomCommission;
    
    private BigDecimal tauxCommission;

    @NotNull(message = "La date du trajet est obligatoire")
    private LocalDateTime dateTrajet;

    @NotNull(message = "L'ID du chauffeur est obligatoire")
    private Long chauffeurId;

    @NotNull(message = "L'ID du passager est obligatoire")
    private Long passagerId;
}
