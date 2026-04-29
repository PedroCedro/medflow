package br.com.infocedro.medflow.patient.dto;

import br.com.infocedro.medflow.patient.RelationshipType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PatientRequest(
        @NotBlank
        @Size(max = 120)
        String name,

        @PastOrPresent
        LocalDate birthDate,

        RelationshipType relationship,

        @Size(max = 500)
        String notes
) {
}
