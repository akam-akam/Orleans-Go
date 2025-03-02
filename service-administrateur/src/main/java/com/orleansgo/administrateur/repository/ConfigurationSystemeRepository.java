
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.ConfigurationSysteme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationSystemeRepository extends JpaRepository<ConfigurationSysteme, String> {
    List<ConfigurationSysteme> findByType(ConfigurationSysteme.TypeConfiguration type);
}
