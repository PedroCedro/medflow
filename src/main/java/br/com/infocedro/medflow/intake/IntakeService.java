package br.com.infocedro.medflow.intake;

import br.com.infocedro.medflow.intake.dto.BulkIntakeUpdateItem;
import br.com.infocedro.medflow.intake.dto.IntakeRequest;
import br.com.infocedro.medflow.intake.dto.IntakeResponse;
import br.com.infocedro.medflow.prescription.Prescription;
import br.com.infocedro.medflow.prescription.PrescriptionRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class IntakeService {

    private final IntakeRepository intakeRepository;
    private final PrescriptionRepository prescriptionRepository;

    public IntakeService(IntakeRepository intakeRepository, PrescriptionRepository prescriptionRepository) {
        this.intakeRepository = intakeRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Transactional
    public IntakeResponse create(IntakeRequest request) {
        Prescription prescription = getActivePrescription(request.prescriptionId());

        Intake intake = new Intake(
                prescription,
                request.scheduledAt(),
                request.takenAt(),
                request.status(),
                request.notes()
        );

        return IntakeResponse.fromEntity(intakeRepository.save(intake));
    }

    @Transactional(readOnly = true)
    public List<IntakeResponse> findAllActive() {
        return intakeRepository.findAllByActiveTrueOrderByScheduledAtAsc()
                .stream()
                .map(IntakeResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IntakeResponse> findAllByPrescriptionId(Long prescriptionId) {
        getActivePrescription(prescriptionId);
        return intakeRepository.findAllByPrescriptionIdAndActiveTrueOrderByScheduledAtAsc(prescriptionId)
                .stream()
                .map(IntakeResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public IntakeResponse findById(Long id) {
        return IntakeResponse.fromEntity(getActiveIntake(id));
    }

    @Transactional
    public IntakeResponse update(Long id, IntakeRequest request) {
        Intake intake = getActiveIntake(id);
        Prescription prescription = getActivePrescription(request.prescriptionId());

        intake.update(
                prescription,
                request.scheduledAt(),
                request.takenAt(),
                request.status(),
                request.notes()
        );

        return IntakeResponse.fromEntity(intake);
    }

    @Transactional
    public List<IntakeResponse> bulkUpdate(List<BulkIntakeUpdateItem> items) {
        return items.stream().map(item -> {
            Intake intake = getActiveIntake(item.id());
            intake.updateStatus(item.status(), item.takenAt());
            return IntakeResponse.fromEntity(intake);
        }).toList();
    }

    @Transactional
    public void deactivate(Long id) {
        Intake intake = getActiveIntake(id);
        intake.deactivate();
    }

    private Intake getActiveIntake(Long id) {
        Intake intake = intakeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tomada nao encontrada"));

        if (!intake.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tomada nao encontrada");
        }

        return intake;
    }

    private Prescription getActivePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescricao nao encontrada"));

        if (!prescription.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescricao nao encontrada");
        }

        return prescription;
    }
}
