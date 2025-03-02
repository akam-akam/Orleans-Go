
package com.orleansgo.utilisateur.repository;

import com.orleansgo.utilisateur.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByUserId(Long userId);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.user.id = :userId")
    Double getAverageRatingForUser(Long userId);
    
    List<Rating> findByRideId(Long rideId);
}
