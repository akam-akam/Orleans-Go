
package com.orleansgo.support.dto;

import com.orleansgo.support.model.FaqCategorie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDTO {
    private Long id;
    private String question;
    private String reponse;
    private FaqCategorie categorie;
    private Boolean actif;
    private Integer ordre;
}
