
package com.orleansgo.commission.dto;

import com.orleansgo.commission.model.TypeCommission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionDTO {

    private Long id;

    @NotBlank(message = "Le nom de la commission est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le taux de commission est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le taux de commission doit être positif")
    private BigDecimal taux;

    private BigDecimal montantFixe;

    @NotNull(message = "Le type de commission est obligatoire")
    private TypeCommission type;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    private boolean active;
}
