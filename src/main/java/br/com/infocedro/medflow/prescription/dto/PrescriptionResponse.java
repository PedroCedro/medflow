package br.com.infocedro.medflow.prescription.dto;

import br.com.infocedro.medflow.prescription.DosageUnit;
import br.com.infocedro.medflow.prescription.Prescription;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record PrescriptionResponse(
        Long id,
        Long patientId,
        String patientName,
        Long medicationId,
        String medicationName,
        BigDecimal dosageAmount,
        DosageUnit dosageUnit,
        String dosageUnitDisplayName,
        String frequency,
        LocalTime scheduledTime,
        LocalDate startDate,
        boolean continuousUse,
        BigDecimal stockQuantity,
        BigDecimal minimumStockAlert,
        String instructions,
        boolean active
) {

    public static PrescriptionResponse fromEntity(Prescription prescription) {
        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getPatient().getName(),
                prescription.getMedication().getId(),
                prescription.getMedication().getName(),
                prescription.getDosageAmount(),
                prescription.getDosageUnit(),
                prescription.getDosageUnit().getDisplayNamePtBr(),
                prescription.getFrequency(),
                prescription.getScheduledTime(),
                prescription.getStartDate(),
                prescription.isContinuousUse(),
                prescription.getStockQuantity(),
                prescription.getMinimumStockAlert(),
                prescription.getInstructions(),
                prescription.isActive()
        );
    }
}
