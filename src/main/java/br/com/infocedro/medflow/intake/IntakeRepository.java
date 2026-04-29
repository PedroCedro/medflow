package br.com.infocedro.medflow.intake;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeRepository extends JpaRepository<Intake, Long> {

    List<Intake> findAllByActiveTrueOrderByScheduledAtAsc();

    List<Intake> findAllByPrescriptionIdAndActiveTrueOrderByScheduledAtAsc(Long prescriptionId);
}
