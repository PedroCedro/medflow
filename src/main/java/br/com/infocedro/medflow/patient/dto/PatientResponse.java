package br.com.infocedro.medflow.patient.dto;

import br.com.infocedro.medflow.patient.Patient;
import br.com.infocedro.medflow.patient.RelationshipType;
import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String name,
        LocalDate birthDate,
        RelationshipType relationship,
        String relationshipDisplayName,
        String notes,
        boolean active
) {

    public static PatientResponse fromEntity(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getBirthDate(),
                patient.getRelationship(),
                patient.getRelationship() != null ? patient.getRelationship().getDisplayNamePtBr() : null,
                patient.getNotes(),
                patient.isActive()
        );
    }
}
