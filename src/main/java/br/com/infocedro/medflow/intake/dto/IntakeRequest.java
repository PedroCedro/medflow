package br.com.infocedro.medflow.intake.dto;

import br.com.infocedro.medflow.intake.IntakeStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record IntakeRequest(
        @NotNull
        Long prescriptionId,

        @NotNull
        LocalDateTime scheduledAt,

        LocalDateTime takenAt,

        @NotNull
        IntakeStatus status,

        @Size(max = 500)
        String notes
) {
}
