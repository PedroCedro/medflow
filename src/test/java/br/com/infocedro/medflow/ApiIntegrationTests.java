package br.com.infocedro.medflow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.infocedro.medflow.intake.IntakeStatus;
import br.com.infocedro.medflow.intake.dto.BulkIntakeUpdateItem;
import br.com.infocedro.medflow.intake.dto.IntakeRequest;
import br.com.infocedro.medflow.medication.dto.MedicationRequest;
import br.com.infocedro.medflow.patient.RelationshipType;
import br.com.infocedro.medflow.patient.dto.PatientRequest;
import br.com.infocedro.medflow.prescription.DosageUnit;
import br.com.infocedro.medflow.prescription.dto.PrescriptionRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndListCoreMvpFlow() throws Exception {
        Long patientId = createPatient();
        Long medicationId = createMedication();
        Long prescriptionId = createPrescription(patientId, medicationId);
        createIntake(prescriptionId);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Maria Silva"))
                .andExpect(jsonPath("$[0].relationship").value("MOTHER"));

        mockMvc.perform(get("/api/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Losartana"))
                .andExpect(jsonPath("$[0].activeIngredient").value("Losartana Potassica"));

        mockMvc.perform(get("/api/prescriptions").param("patientId", patientId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(patientId))
                .andExpect(jsonPath("$[0].medicationId").value(medicationId))
                .andExpect(jsonPath("$[0].dosageUnit").value("TABLET"));

        mockMvc.perform(get("/api/intakes").param("prescriptionId", prescriptionId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prescriptionId").value(prescriptionId))
                .andExpect(jsonPath("$[0].status").value("TAKEN"));
    }

    @Test
    void shouldReturnValidationDetailsForInvalidPatientPayload() throws Exception {
        PatientRequest invalidRequest = new PatientRequest("", LocalDate.now().plusDays(1), null, "teste");

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Dados da requisicao invalidos"))
                .andExpect(jsonPath("$.path").value("/api/patients"))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.birthDate").exists());
    }

    @Test
    void shouldReturnNotFoundForMissingPatient() throws Exception {
        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Paciente não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/patients/999"));
    }

    @Test
    void shouldHideInactiveMedicationFromReadsAfterDelete() throws Exception {
        Long medicationId = createMedication();

        mockMvc.perform(delete("/api/medications/{id}", medicationId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/medications/{id}", medicationId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Medicamento nao encontrado"));
    }

    @Test
    void shouldBulkUpdateIntakeStatuses() throws Exception {
        Long patientId = createPatient();
        Long medicationId = createMedication();
        Long prescriptionId = createPrescription(patientId, medicationId);
        Long firstIntakeId = createPendingIntake(prescriptionId, LocalDateTime.of(2026, 5, 4, 8, 0));
        Long secondIntakeId = createPendingIntake(prescriptionId, LocalDateTime.of(2026, 5, 4, 20, 0));
        LocalDateTime takenAt = LocalDateTime.of(2026, 5, 4, 8, 10);

        List<BulkIntakeUpdateItem> request = List.of(
                new BulkIntakeUpdateItem(firstIntakeId, IntakeStatus.TAKEN, takenAt),
                new BulkIntakeUpdateItem(secondIntakeId, IntakeStatus.SKIPPED, null)
        );

        mockMvc.perform(post("/api/intakes/bulk-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(firstIntakeId))
                .andExpect(jsonPath("$[0].status").value("TAKEN"))
                .andExpect(jsonPath("$[0].takenAt").exists())
                .andExpect(jsonPath("$[1].id").value(secondIntakeId))
                .andExpect(jsonPath("$[1].status").value("SKIPPED"))
                .andExpect(jsonPath("$[1].takenAt").doesNotExist());
    }

    private Long createPatient() throws Exception {
        PatientRequest request = new PatientRequest(
                "Maria Silva",
                LocalDate.of(1958, 4, 12),
                RelationshipType.MOTHER,
                "Paciente de teste"
        );

        return createResourceAndExtractId("/api/patients", request, "/api/patients/");
    }

    private Long createMedication() throws Exception {
        MedicationRequest request = new MedicationRequest(
                "Losartana",
                "Losartana Potassica",
                "50 mg comprimido",
                "Uso continuo"
        );

        return createResourceAndExtractId("/api/medications", request, "/api/medications/");
    }

    private Long createPrescription(Long patientId, Long medicationId) throws Exception {
        PrescriptionRequest request = new PrescriptionRequest(
                patientId,
                medicationId,
                new BigDecimal("1.00"),
                DosageUnit.TABLET,
                "1x ao dia",
                LocalTime.of(8, 0),
                LocalDate.of(2026, 5, 4),
                true,
                new BigDecimal("30.00"),
                new BigDecimal("5.00"),
                "Tomar pela manha"
        );

        return createResourceAndExtractId("/api/prescriptions", request, "/api/prescriptions/");
    }

    private Long createIntake(Long prescriptionId) throws Exception {
        IntakeRequest request = new IntakeRequest(
                prescriptionId,
                LocalDateTime.of(2026, 5, 4, 8, 0),
                LocalDateTime.of(2026, 5, 4, 8, 5),
                IntakeStatus.TAKEN,
                "Tomada registrada manualmente"
        );

        return createResourceAndExtractId("/api/intakes", request, "/api/intakes/");
    }

    private Long createPendingIntake(Long prescriptionId, LocalDateTime scheduledAt) throws Exception {
        IntakeRequest request = new IntakeRequest(
                prescriptionId,
                scheduledAt,
                null,
                IntakeStatus.PENDING,
                null
        );

        return createResourceAndExtractId("/api/intakes", request, "/api/intakes/");
    }

    private Long createResourceAndExtractId(String path, Object request, String locationSuffix) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        Long id = body.get("id").asLong();

        org.hamcrest.MatcherAssert.assertThat(
                result.getResponse().getHeader("Location"),
                org.hamcrest.Matchers.containsString(locationSuffix + id)
        );

        return id;
    }
}
