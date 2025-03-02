
package com.orleansgo.support.service;

import com.orleansgo.support.dto.FaqDTO;
import com.orleansgo.support.model.Faq;
import com.orleansgo.support.model.FaqCategorie;
import com.orleansgo.support.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public List<FaqDTO> getAllActiveFaqs() {
        return faqRepository.findByActifOrderByOrdre(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FaqDTO> getAllFaqs() {
        return faqRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FaqDTO getFaqById(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ non trouvée avec l'ID: " + id));
        return convertToDTO(faq);
    }

    @Transactional
    public FaqDTO createFaq(FaqDTO faqDTO) {
        Faq faq = convertToEntity(faqDTO);
        Faq savedFaq = faqRepository.save(faq);
        return convertToDTO(savedFaq);
    }

    @Transactional
    public FaqDTO updateFaq(Long id, FaqDTO faqDTO) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ non trouvée avec l'ID: " + id));
        
        faq.setQuestion(faqDTO.getQuestion());
        faq.setReponse(faqDTO.getReponse());
        faq.setCategorie(faqDTO.getCategorie());
        faq.setActif(faqDTO.getActif());
        faq.setOrdre(faqDTO.getOrdre());
        
        Faq updatedFaq = faqRepository.save(faq);
        return convertToDTO(updatedFaq);
    }

    @Transactional
    public void deleteFaq(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new EntityNotFoundException("FAQ non trouvée avec l'ID: " + id);
        }
        faqRepository.deleteById(id);
    }

    public List<FaqDTO> getFaqsByCategorie(FaqCategorie categorie) {
        return faqRepository.findByCategorieAndActifOrderByOrdre(categorie, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FaqDTO convertToDTO(Faq faq) {
        return FaqDTO.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .reponse(faq.getReponse())
                .categorie(faq.getCategorie())
                .actif(faq.getActif())
                .ordre(faq.getOrdre())
                .build();
    }

    private Faq convertToEntity(FaqDTO faqDTO) {
        Faq faq = new Faq();
        faq.setQuestion(faqDTO.getQuestion());
        faq.setReponse(faqDTO.getReponse());
        faq.setCategorie(faqDTO.getCategorie());
        faq.setActif(faqDTO.getActif() != null ? faqDTO.getActif() : true);
        faq.setOrdre(faqDTO.getOrdre() != null ? faqDTO.getOrdre() : 0);
        return faq;
    }
}
