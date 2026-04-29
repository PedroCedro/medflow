package br.com.infocedro.medflow.medication;

import br.com.infocedro.medflow.medication.dto.MedicationRequest;
import br.com.infocedro.medflow.medication.dto.MedicationResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Transactional
    public MedicationResponse create(MedicationRequest request) {
        Medication medication = new Medication(
                request.name(),
                request.activeIngredient(),
                request.presentation(),
                request.notes()
        );

        return MedicationResponse.fromEntity(medicationRepository.save(medication));
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> findAllActive() {
        return medicationRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(MedicationResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicationResponse findById(Long id) {
        return MedicationResponse.fromEntity(getActiveMedication(id));
    }

    @Transactional
    public MedicationResponse update(Long id, MedicationRequest request) {
        Medication medication = getActiveMedication(id);
        medication.update(
                request.name(),
                request.activeIngredient(),
                request.presentation(),
                request.notes()
        );

        return MedicationResponse.fromEntity(medication);
    }

    @Transactional
    public void deactivate(Long id) {
        Medication medication = getActiveMedication(id);
        medication.deactivate();
    }

    private Medication getActiveMedication(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento nao encontrado"));

        if (!medication.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento nao encontrado");
        }

        return medication;
    }
}
