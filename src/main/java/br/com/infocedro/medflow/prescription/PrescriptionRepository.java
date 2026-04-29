package br.com.infocedro.medflow.prescription;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findAllByActiveTrueOrderByIdAsc();

    List<Prescription> findAllByPatientIdAndActiveTrueOrderByIdAsc(Long patientId);
}
