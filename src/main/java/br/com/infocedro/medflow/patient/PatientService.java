package br.com.infocedro.medflow.patient;

import br.com.infocedro.medflow.patient.dto.PatientRequest;
import br.com.infocedro.medflow.patient.dto.PatientResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional
    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient(
                request.name(),
                request.birthDate(),
                request.relationship(),
                request.notes()
        );

        return PatientResponse.fromEntity(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> findAllActive() {
        return patientRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(PatientResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(Long id) {
        return PatientResponse.fromEntity(getActivePatient(id));
    }

    @Transactional
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = getActivePatient(id);
        patient.update(
                request.name(),
                request.birthDate(),
                request.relationship(),
                request.notes()
        );

        return PatientResponse.fromEntity(patient);
    }

    @Transactional
    public void deactivate(Long id) {
        Patient patient = getActivePatient(id);
        patient.deactivate();
    }

    private Patient getActivePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

        if (!patient.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }

        return patient;
    }
}
