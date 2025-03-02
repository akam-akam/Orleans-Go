
package com.orleansgo.support.repository;

import com.orleansgo.support.model.Faq;
import com.orleansgo.support.model.FaqCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findByActifOrderByOrdre(Boolean actif);
    List<Faq> findByCategorieAndActifOrderByOrdre(FaqCategorie categorie, Boolean actif);
}
