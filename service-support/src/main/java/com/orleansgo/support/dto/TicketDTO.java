
package com.orleansgo.support.dto;

import com.orleansgo.support.model.TicketPriorite;
import com.orleansgo.support.model.TicketStatut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private String sujet;
    private String description;
    private String userId;
    private TicketStatut statut;
    private TicketPriorite priorite;
    private String assignedTo;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private LocalDateTime dateCloture;
    private Integer nombreMessages;
}
