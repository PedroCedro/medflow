package br.com.infocedro.medflow.medication;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findAllByActiveTrueOrderByNameAsc();
}
