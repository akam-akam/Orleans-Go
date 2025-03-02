
package com.orleansgo.support.controller;

import com.orleansgo.support.dto.FaqDTO;
import com.orleansgo.support.model.FaqCategorie;
import com.orleansgo.support.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<List<FaqDTO>> getAllActiveFaqs() {
        return ResponseEntity.ok(faqService.getAllActiveFaqs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaqDTO> getFaqById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFaqById(id));
    }

    @PostMapping
    public ResponseEntity<FaqDTO> createFaq(@RequestBody FaqDTO faqDTO) {
        return new ResponseEntity<>(faqService.createFaq(faqDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FaqDTO> updateFaq(@PathVariable Long id, @RequestBody FaqDTO faqDTO) {
        return ResponseEntity.ok(faqService.updateFaq(id, faqDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<FaqDTO>> getFaqsByCategorie(@PathVariable FaqCategorie categorie) {
        return ResponseEntity.ok(faqService.getFaqsByCategorie(categorie));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<FaqDTO>> getAllFaqs() {
        return ResponseEntity.ok(faqService.getAllFaqs());
    }
}
