
package com.orleansgo.trajet.service;

import com.orleansgo.trajet.dto.PositionDTO;
import com.orleansgo.trajet.dto.ReservationDTO;
import com.orleansgo.trajet.dto.TrajetDTO;
import com.orleansgo.trajet.model.Position;
import com.orleansgo.trajet.model.Reservation;
import com.orleansgo.trajet.model.StatutReservation;
import com.orleansgo.trajet.model.StatutTrajet;
import com.orleansgo.trajet.model.Trajet;
import com.orleansgo.trajet.repository.PositionRepository;
import com.orleansgo.trajet.repository.ReservationRepository;
import com.orleansgo.trajet.repository.TrajetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrajetServiceTest {

    @Mock
    private TrajetRepository trajetRepository;
    
    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private TrajetService trajetService;

    private Trajet trajet;
    private TrajetDTO trajetDTO;
    private Reservation reservation;
    private ReservationDTO reservationDTO;
    private Position position;
    private PositionDTO positionDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        trajet = new Trajet();
        trajet.setId(1L);
        trajet.setConducteurId(10L);
        trajet.setVehiculeId(5L);
        trajet.setLieuDepart("Orléans Gare");
        trajet.setLieuArrivee("Paris Gare");
        trajet.setLatitudeDepart(47.9027);
        trajet.setLongitudeDepart(1.9090);
        trajet.setLatitudeArrivee(48.8566);
        trajet.setLongitudeArrivee(2.3522);
        trajet.setDateDepart(now.plusHours(1));
        trajet.setDateArriveeEstimee(now.plusHours(3));
        trajet.setPrix(new BigDecimal("25.00"));
        trajet.setNombrePlaces(4);
        trajet.setNombrePlacesDisponibles(4);
        trajet.setStatut(StatutTrajet.PLANIFIE);
        trajet.setOptionsService("Climatisation, Musique");
        trajet.setDateCreation(now);
        trajet.setDateModification(now);
        
        trajetDTO = new TrajetDTO();
        trajetDTO.setId(1L);
        trajetDTO.setConducteurId(10L);
        trajetDTO.setVehiculeId(5L);
        trajetDTO.setLieuDepart("Orléans Gare");
        trajetDTO.setLieuArrivee("Paris Gare");
        trajetDTO.setLatitudeDepart(47.9027);
        trajetDTO.setLongitudeDepart(1.9090);
        trajetDTO.setLatitudeArrivee(48.8566);
        trajetDTO.setLongitudeArrivee(2.3522);
        trajetDTO.setDateDepart(now.plusHours(1));
        trajetDTO.setDateArriveeEstimee(now.plusHours(3));
        trajetDTO.setPrix(new BigDecimal("25.00"));
        trajetDTO.setNombrePlaces(4);
        trajetDTO.setNombrePlacesDisponibles(4);
        trajetDTO.setStatut(StatutTrajet.PLANIFIE.name());
        trajetDTO.setOptionsService("Climatisation, Musique");
        trajetDTO.setDateCreation(now);
        trajetDTO.setDateModification(now);
        
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setTrajetId(1L);
        reservation.setUtilisateurId(20L);
        reservation.setNombrePlaces(2);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setDateReservation(now);
        reservation.setMontantTotal(new BigDecimal("50.00"));
        reservation.setDateCreation(now);
        reservation.setDateModification(now);
        
        reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        reservationDTO.setTrajetId(1L);
        reservationDTO.setUtilisateurId(20L);
        reservationDTO.setNombrePlaces(2);
        reservationDTO.setStatut(StatutReservation.CONFIRMEE.name());
        reservationDTO.setDateReservation(now);
        reservationDTO.setMontantTotal(new BigDecimal("50.00"));
        reservationDTO.setDateCreation(now);
        reservationDTO.setDateModification(now);
        
        position = new Position();
        position.setId(1L);
        position.setTrajetId(1L);
        position.setLatitude(48.0000);
        position.setLongitude(2.0000);
        position.setDateEnregistrement(now);
        
        positionDTO = new PositionDTO();
        positionDTO.setId(1L);
        positionDTO.setTrajetId(1L);
        positionDTO.setLatitude(48.0000);
        positionDTO.setLongitude(2.0000);
        positionDTO.setDateEnregistrement(now);
    }

    @Test
    void getAllTrajets() {
        when(trajetRepository.findAll()).thenReturn(Arrays.asList(trajet));
        
        List<TrajetDTO> result = trajetService.getAllTrajets();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trajet.getId(), result.get(0).getId());
    }

    @Test
    void getTrajetById() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        
        TrajetDTO result = trajetService.getTrajetById(1L);
        
        assertNotNull(result);
        assertEquals(trajet.getId(), result.getId());
    }
    
    @Test
    void getTrajetsByConducteurId() {
        when(trajetRepository.findByConducteurId(10L)).thenReturn(Arrays.asList(trajet));
        
        List<TrajetDTO> result = trajetService.getTrajetsByConducteurId(10L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trajet.getConducteurId(), result.get(0).getConducteurId());
    }
    
    @Test
    void createTrajet() {
        when(trajetRepository.save(any(Trajet.class))).thenReturn(trajet);
        
        TrajetDTO result = trajetService.createTrajet(trajetDTO);
        
        assertNotNull(result);
        assertEquals(trajet.getId(), result.getId());
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }
    
    @Test
    void updateTrajet() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(trajetRepository.save(any(Trajet.class))).thenReturn(trajet);
        
        TrajetDTO result = trajetService.updateTrajet(1L, trajetDTO);
        
        assertNotNull(result);
        assertEquals(trajet.getId(), result.getId());
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }
    
    @Test
    void updateStatutTrajet() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(trajetRepository.save(any(Trajet.class))).thenReturn(trajet);
        
        trajet.setStatut(StatutTrajet.EN_COURS);
        
        TrajetDTO result = trajetService.updateStatutTrajet(1L, "EN_COURS");
        
        assertNotNull(result);
        assertEquals(StatutTrajet.EN_COURS.name(), result.getStatut());
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }
    
    @Test
    void addReservation() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(trajetRepository.save(any(Trajet.class))).thenReturn(trajet);
        
        ReservationDTO result = trajetService.addReservation(1L, reservationDTO);
        
        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }
    
    @Test
    void getReservationsByTrajetId() {
        when(reservationRepository.findByTrajetId(1L)).thenReturn(Arrays.asList(reservation));
        
        List<ReservationDTO> result = trajetService.getReservationsByTrajetId(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation.getTrajetId(), result.get(0).getTrajetId());
    }
    
    @Test
    void updateReservationStatut() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        
        reservation.setStatut(StatutReservation.ANNULEE);
        
        ReservationDTO result = trajetService.updateReservationStatut(1L, "ANNULEE");
        
        assertNotNull(result);
        assertEquals(StatutReservation.ANNULEE.name(), result.getStatut());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
    
    @Test
    void searchTrajets() {
        when(trajetRepository.findByLieuDepartContainingAndLieuArriveeContaining(
                anyString(), anyString())).thenReturn(Arrays.asList(trajet));
        
        List<TrajetDTO> result = trajetService.searchTrajets("Orléans", "Paris", null, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trajet.getLieuDepart(), result.get(0).getLieuDepart());
        assertEquals(trajet.getLieuArrivee(), result.get(0).getLieuArrivee());
    }
    
    @Test
    void addTrajetPosition() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(positionRepository.save(any(Position.class))).thenReturn(position);
        
        PositionDTO result = trajetService.addTrajetPosition(1L, positionDTO);
        
        assertNotNull(result);
        assertEquals(position.getTrajetId(), result.getTrajetId());
        verify(positionRepository, times(1)).save(any(Position.class));
    }
    
    @Test
    void getTrajetPositions() {
        when(positionRepository.findByTrajetIdOrderByDateEnregistrementAsc(1L))
                .thenReturn(Arrays.asList(position));
        
        List<PositionDTO> result = trajetService.getTrajetPositions(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(position.getTrajetId(), result.get(0).getTrajetId());
    }
}
package com.orleansgo.trajet.service;

import com.orleansgo.trajet.model.Trajet;
import com.orleansgo.trajet.model.StatusTrajet;
import com.orleansgo.trajet.repository.TrajetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrajetServiceTest {

    @Mock
    private TrajetRepository trajetRepository;

    @InjectMocks
    private TrajetService trajetService;

    private Trajet trajet;

    @BeforeEach
    void setUp() {
        trajet = new Trajet();
        trajet.setId(1L);
        trajet.setUtilisateurId(100L);
        trajet.setConducteurId(200L);
        trajet.setOrigineLat(47.9029);
        trajet.setOrigineLon(1.9039);
        trajet.setDestinationLat(47.8414);
        trajet.setDestinationLon(1.8823);
        trajet.setOrigineName("Orléans Centre");
        trajet.setDestinationName("Saint-Jean-de-la-Ruelle");
        trajet.setDistance(5.2);
        trajet.setDureePrevue(15);
        trajet.setPrix(new BigDecimal("12.50"));
        trajet.setStatus(StatusTrajet.PLANIFIE);
        trajet.setDateCreation(LocalDateTime.now());
        trajet.setDateDepart(LocalDateTime.now().plusHours(1));
    }

    @Test
    void shouldSaveTrajet() {
        when(trajetRepository.save(any(Trajet.class))).thenReturn(trajet);

        Trajet savedTrajet = trajetService.saveTrajet(trajet);

        assertThat(savedTrajet).isNotNull();
        assertThat(savedTrajet.getId()).isEqualTo(1L);
        assertThat(savedTrajet.getPrix()).isEqualTo(new BigDecimal("12.50"));
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }

    @Test
    void shouldGetAllTrajets() {
        when(trajetRepository.findAll()).thenReturn(Arrays.asList(trajet));

        List<Trajet> trajets = trajetService.getAllTrajets();

        assertThat(trajets).isNotEmpty();
        assertThat(trajets).hasSize(1);
        assertThat(trajets.get(0).getPrix()).isEqualTo(new BigDecimal("12.50"));
        verify(trajetRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTrajetById() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));

        Optional<Trajet> foundTrajet = trajetService.getTrajetById(1L);

        assertThat(foundTrajet).isPresent();
        assertThat(foundTrajet.get().getId()).isEqualTo(1L);
        verify(trajetRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetTrajetsByUtilisateurId() {
        when(trajetRepository.findByUtilisateurId(100L)).thenReturn(Arrays.asList(trajet));

        List<Trajet> trajets = trajetService.getTrajetsByUtilisateurId(100L);

        assertThat(trajets).isNotEmpty();
        assertThat(trajets).hasSize(1);
        assertThat(trajets.get(0).getUtilisateurId()).isEqualTo(100L);
        verify(trajetRepository, times(1)).findByUtilisateurId(100L);
    }

    @Test
    void shouldGetTrajetsByConducteurId() {
        when(trajetRepository.findByConducteurId(200L)).thenReturn(Arrays.asList(trajet));

        List<Trajet> trajets = trajetService.getTrajetsByConducteurId(200L);

        assertThat(trajets).isNotEmpty();
        assertThat(trajets).hasSize(1);
        assertThat(trajets.get(0).getConducteurId()).isEqualTo(200L);
        verify(trajetRepository, times(1)).findByConducteurId(200L);
    }

    @Test
    void shouldUpdateTrajetStatus() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(trajetRepository.save(any(Trajet.class))).thenReturn(trajet);

        Optional<Trajet> updatedTrajet = trajetService.updateTrajetStatus(1L, StatusTrajet.EN_COURS);

        assertThat(updatedTrajet).isPresent();
        assertThat(updatedTrajet.get().getStatus()).isEqualTo(StatusTrajet.EN_COURS);
        verify(trajetRepository, times(1)).findById(1L);
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }

    @Test
    void shouldUpdateTrajet() {
        Trajet updatedTrajet = new Trajet();
        updatedTrajet.setId(1L);
        updatedTrajet.setUtilisateurId(100L);
        updatedTrajet.setConducteurId(200L);
        updatedTrajet.setPrix(new BigDecimal("15.00"));
        updatedTrajet.setStatus(StatusTrajet.EN_COURS);

        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(trajetRepository.save(any(Trajet.class))).thenReturn(updatedTrajet);

        Optional<Trajet> result = trajetService.updateTrajet(1L, updatedTrajet);

        assertThat(result).isPresent();
        assertThat(result.get().getPrix()).isEqualTo(new BigDecimal("15.00"));
        assertThat(result.get().getStatus()).isEqualTo(StatusTrajet.EN_COURS);
        verify(trajetRepository, times(1)).findById(1L);
        verify(trajetRepository, times(1)).save(any(Trajet.class));
    }

    @Test
    void shouldDeleteTrajet() {
        doNothing().when(trajetRepository).deleteById(1L);

        trajetService.deleteTrajet(1L);

        verify(trajetRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldSearchTrajets() {
        double origineLat = 47.9029;
        double origineLon = 1.9039;
        double destinationLat = 47.8414;
        double destinationLon = 1.8823;
        LocalDateTime dateDepart = LocalDateTime.now().plusHours(1);
        
        when(trajetRepository.findByOrigineCoordsAndDestinationCoordsAndDateDepartAfter(
                anyDouble(), anyDouble(), anyDouble(), 
                anyDouble(), anyDouble(), anyDouble(), 
                any(LocalDateTime.class))).thenReturn(Arrays.asList(trajet));

        List<Trajet> results = trajetService.searchTrajets(
                origineLat, origineLon, 0.5, 
                destinationLat, destinationLon, 0.5, 
                dateDepart);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        verify(trajetRepository, times(1)).findByOrigineCoordsAndDestinationCoordsAndDateDepartAfter(
                anyDouble(), anyDouble(), anyDouble(), 
                anyDouble(), anyDouble(), anyDouble(), 
                any(LocalDateTime.class));
    }
}
