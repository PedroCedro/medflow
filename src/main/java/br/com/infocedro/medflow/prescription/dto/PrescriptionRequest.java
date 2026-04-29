package br.com.infocedro.medflow.prescription.dto;

import br.com.infocedro.medflow.prescription.DosageUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record PrescriptionRequest(
        @NotNull
        Long patientId,

        @NotNull
        Long medicationId,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal dosageAmount,

        @NotNull
        DosageUnit dosageUnit,

        @NotBlank
        @Size(max = 80)
        String frequency,

        LocalTime scheduledTime,

        @NotNull
        LocalDate startDate,

        boolean continuousUse,

        @DecimalMin(value = "0.0")
        BigDecimal stockQuantity,

        @DecimalMin(value = "0.0")
        BigDecimal minimumStockAlert,

        @Size(max = 500)
        String instructions
) {
}
