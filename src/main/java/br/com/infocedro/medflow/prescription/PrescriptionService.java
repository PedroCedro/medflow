package br.com.infocedro.medflow.prescription;

import br.com.infocedro.medflow.medication.Medication;
import br.com.infocedro.medflow.medication.MedicationRepository;
import br.com.infocedro.medflow.patient.Patient;
import br.com.infocedro.medflow.patient.PatientRepository;
import br.com.infocedro.medflow.prescription.dto.PrescriptionRequest;
import br.com.infocedro.medflow.prescription.dto.PrescriptionResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository,
            MedicationRepository medicationRepository
    ) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
    }

    @Transactional
    public PrescriptionResponse create(PrescriptionRequest request) {
        Patient patient = getActivePatient(request.patientId());
        Medication medication = getActiveMedication(request.medicationId());

        Prescription prescription = new Prescription(
                patient,
                medication,
                request.dosageAmount(),
                request.dosageUnit(),
                request.frequency(),
                request.scheduledTime(),
                request.startDate(),
                request.continuousUse(),
                request.stockQuantity(),
                request.minimumStockAlert(),
                request.instructions()
        );

        return PrescriptionResponse.fromEntity(prescriptionRepository.save(prescription));
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> findAllActive() {
        return prescriptionRepository.findAllByActiveTrueOrderByIdAsc()
                .stream()
                .map(PrescriptionResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> findAllByPatientId(Long patientId) {
        getActivePatient(patientId);
        return prescriptionRepository.findAllByPatientIdAndActiveTrueOrderByIdAsc(patientId)
                .stream()
                .map(PrescriptionResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse findById(Long id) {
        return PrescriptionResponse.fromEntity(getActivePrescription(id));
    }

    @Transactional
    public PrescriptionResponse update(Long id, PrescriptionRequest request) {
        Prescription prescription = getActivePrescription(id);
        Patient patient = getActivePatient(request.patientId());
        Medication medication = getActiveMedication(request.medicationId());

        prescription.update(
                patient,
                medication,
                request.dosageAmount(),
                request.dosageUnit(),
                request.frequency(),
                request.scheduledTime(),
                request.startDate(),
                request.continuousUse(),
                request.stockQuantity(),
                request.minimumStockAlert(),
                request.instructions()
        );

        return PrescriptionResponse.fromEntity(prescription);
    }

    @Transactional
    public void deactivate(Long id) {
        Prescription prescription = getActivePrescription(id);
        prescription.deactivate();
    }

    private Prescription getActivePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescricao nao encontrada"));

        if (!prescription.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescricao nao encontrada");
        }

        return prescription;
    }

    private Patient getActivePatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente nao encontrado"));

        if (!patient.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente nao encontrado");
        }

        return patient;
    }

    private Medication getActiveMedication(Long medicationId) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento nao encontrado"));

        if (!medication.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento nao encontrado");
        }

        return medication;
    }
}
