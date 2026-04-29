package br.com.infocedro.medflow.prescription;

import br.com.infocedro.medflow.medication.Medication;
import br.com.infocedro.medflow.patient.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(name = "dosage_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal dosageAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_unit", nullable = false, length = 30)
    private DosageUnit dosageUnit;

    @Column(nullable = false, length = 80)
    private String frequency;

    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "continuous_use", nullable = false)
    private boolean continuousUse;

    @Column(name = "stock_quantity", precision = 10, scale = 2)
    private BigDecimal stockQuantity;

    @Column(name = "minimum_stock_alert", precision = 10, scale = 2)
    private BigDecimal minimumStockAlert;

    @Column(length = 500)
    private String instructions;

    @Column(nullable = false)
    private boolean active = true;

    protected Prescription() {
    }

    public Prescription(
            Patient patient,
            Medication medication,
            BigDecimal dosageAmount,
            DosageUnit dosageUnit,
            String frequency,
            LocalTime scheduledTime,
            LocalDate startDate,
            boolean continuousUse,
            BigDecimal stockQuantity,
            BigDecimal minimumStockAlert,
            String instructions
    ) {
        this.patient = patient;
        this.medication = medication;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
        this.frequency = frequency;
        this.scheduledTime = scheduledTime;
        this.startDate = startDate;
        this.continuousUse = continuousUse;
        this.stockQuantity = stockQuantity;
        this.minimumStockAlert = minimumStockAlert;
        this.instructions = instructions;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Medication getMedication() {
        return medication;
    }

    public BigDecimal getDosageAmount() {
        return dosageAmount;
    }

    public DosageUnit getDosageUnit() {
        return dosageUnit;
    }

    public String getFrequency() {
        return frequency;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean isContinuousUse() {
        return continuousUse;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public BigDecimal getMinimumStockAlert() {
        return minimumStockAlert;
    }

    public String getInstructions() {
        return instructions;
    }

    public boolean isActive() {
        return active;
    }

    public void update(
            Patient patient,
            Medication medication,
            BigDecimal dosageAmount,
            DosageUnit dosageUnit,
            String frequency,
            LocalTime scheduledTime,
            LocalDate startDate,
            boolean continuousUse,
            BigDecimal stockQuantity,
            BigDecimal minimumStockAlert,
            String instructions
    ) {
        this.patient = patient;
        this.medication = medication;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
        this.frequency = frequency;
        this.scheduledTime = scheduledTime;
        this.startDate = startDate;
        this.continuousUse = continuousUse;
        this.stockQuantity = stockQuantity;
        this.minimumStockAlert = minimumStockAlert;
        this.instructions = instructions;
    }

    public void deactivate() {
        this.active = false;
    }
}
