package br.com.infocedro.medflow.medication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MedicationRequest(
        @NotBlank
        @Size(max = 120)
        String name,

        @Size(max = 120)
        String activeIngredient,

        @Size(max = 80)
        String presentation,

        @Size(max = 500)
        String notes
) {
}
