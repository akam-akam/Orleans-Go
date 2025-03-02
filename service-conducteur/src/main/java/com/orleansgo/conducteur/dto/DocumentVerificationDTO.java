
package com.orleansgo.conducteur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVerificationDTO {
    private UUID id;
    private UUID conducteurId;
    private boolean permisValide;
    private boolean carteGriseValide;
    private boolean assuranceValide;
    private boolean valideParAdmin;
    private LocalDateTime dateVerification;
}
