
package com.orleansgo.support.repository;

import com.orleansgo.support.model.Ticket;
import com.orleansgo.support.model.TicketStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(String userId);
    List<Ticket> findByStatut(TicketStatut statut);
    List<Ticket> findByAssignedTo(String assignedTo);
}
