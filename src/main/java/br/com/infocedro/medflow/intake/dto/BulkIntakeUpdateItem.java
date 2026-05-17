package br.com.infocedro.medflow.intake.dto;

import br.com.infocedro.medflow.intake.IntakeStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BulkIntakeUpdateItem(
        @NotNull Long id,
        @NotNull IntakeStatus status,
        LocalDateTime takenAt
) {
}
