package br.com.infocedro.medflow.medication.dto;

import br.com.infocedro.medflow.medication.Medication;

public record MedicationResponse(
        Long id,
        String name,
        String activeIngredient,
        String presentation,
        String notes,
        boolean active
) {

    public static MedicationResponse fromEntity(Medication medication) {
        return new MedicationResponse(
                medication.getId(),
                medication.getName(),
                medication.getActiveIngredient(),
                medication.getPresentation(),
                medication.getNotes(),
                medication.isActive()
        );
    }
}
