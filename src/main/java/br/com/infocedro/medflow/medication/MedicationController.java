package br.com.infocedro.medflow.medication;

import br.com.infocedro.medflow.medication.dto.MedicationRequest;
import br.com.infocedro.medflow.medication.dto.MedicationResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping
    public ResponseEntity<MedicationResponse> create(@Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<MedicationResponse> findAll() {
        return medicationService.findAllActive();
    }

    @GetMapping("/{id}")
    public MedicationResponse findById(@PathVariable Long id) {
        return medicationService.findById(id);
    }

    @PutMapping("/{id}")
    public MedicationResponse update(@PathVariable Long id, @Valid @RequestBody MedicationRequest request) {
        return medicationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        medicationService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
