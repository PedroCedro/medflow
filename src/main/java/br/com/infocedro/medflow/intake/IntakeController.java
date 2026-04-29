package br.com.infocedro.medflow.intake;

import br.com.infocedro.medflow.intake.dto.IntakeRequest;
import br.com.infocedro.medflow.intake.dto.IntakeResponse;
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
@RequestMapping("/api/intakes")
public class IntakeController {

    private final IntakeService intakeService;

    public IntakeController(IntakeService intakeService) {
        this.intakeService = intakeService;
    }

    @PostMapping
    public ResponseEntity<IntakeResponse> create(@Valid @RequestBody IntakeRequest request) {
        IntakeResponse response = intakeService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<IntakeResponse> findAll(@RequestParam(required = false) Long prescriptionId) {
        if (prescriptionId != null) {
            return intakeService.findAllByPrescriptionId(prescriptionId);
        }

        return intakeService.findAllActive();
    }

    @GetMapping("/{id}")
    public IntakeResponse findById(@PathVariable Long id) {
        return intakeService.findById(id);
    }

    @PutMapping("/{id}")
    public IntakeResponse update(@PathVariable Long id, @Valid @RequestBody IntakeRequest request) {
        return intakeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        intakeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
