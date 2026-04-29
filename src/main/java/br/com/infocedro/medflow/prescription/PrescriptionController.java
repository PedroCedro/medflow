package br.com.infocedro.medflow.prescription;

import br.com.infocedro.medflow.prescription.dto.PrescriptionRequest;
import br.com.infocedro.medflow.prescription.dto.PrescriptionResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        PrescriptionResponse response = prescriptionService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<PrescriptionResponse> findAll(@RequestParam(required = false) Long patientId) {
        if (patientId != null) {
            return prescriptionService.findAllByPatientId(patientId);
        }

        return prescriptionService.findAllActive();
    }

    @GetMapping("/{id}")
    public PrescriptionResponse findById(@PathVariable Long id) {
        return prescriptionService.findById(id);
    }

    @PutMapping("/{id}")
    public PrescriptionResponse update(@PathVariable Long id, @Valid @RequestBody PrescriptionRequest request) {
        return prescriptionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        prescriptionService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
