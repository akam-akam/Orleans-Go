package com.orleansgo.utilisateur.mapper;

import com.orleansgo.utilisateur.dto.UtilisateurDTO;
import com.orleansgo.utilisateur.model.RoleUtilisateur;
import com.orleansgo.utilisateur.model.Utilisateur;
import org.mapstruct.*;
import java.util.UUID;

@Mapper(componentModel = "spring",
        imports = {UUID.class, RoleUtilisateur.class})
public interface UtilisateurMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeParrainage", expression = "java(genererCodeParrainage())")
    @Mapping(target = "roles", expression = "java(Set.of(RoleUtilisateur.PASSAGER))")
    @Mapping(target = "actif", constant = "true")
    Utilisateur toEntity(UtilisateurDTO dto);

    UtilisateurDTO toDTO(Utilisateur entity);

    default String genererCodeParrainage() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
