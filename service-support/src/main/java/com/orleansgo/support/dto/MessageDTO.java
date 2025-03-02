
package com.orleansgo.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String contenu;
    private String auteurId;
    private String auteurNom;
    private Boolean estAgent;
    private Long ticketId;
    private LocalDateTime dateCreation;
}
