package br.com.infocedro.medflow.intake.dto;

import br.com.infocedro.medflow.intake.Intake;
import br.com.infocedro.medflow.intake.IntakeStatus;
import java.time.LocalDateTime;

public record IntakeResponse(
        Long id,
        Long prescriptionId,
        Long patientId,
        String patientName,
        Long medicationId,
        String medicationName,
        LocalDateTime scheduledAt,
        LocalDateTime takenAt,
        IntakeStatus status,
        String notes,
        boolean active
) {

    public static IntakeResponse fromEntity(Intake intake) {
        return new IntakeResponse(
                intake.getId(),
                intake.getPrescription().getId(),
                intake.getPrescription().getPatient().getId(),
                intake.getPrescription().getPatient().getName(),
                intake.getPrescription().getMedication().getId(),
                intake.getPrescription().getMedication().getName(),
                intake.getScheduledAt(),
                intake.getTakenAt(),
                intake.getStatus(),
                intake.getNotes(),
                intake.isActive()
        );
    }
}
