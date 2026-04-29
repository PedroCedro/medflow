package br.com.infocedro.medflow.intake;

import br.com.infocedro.medflow.prescription.Prescription;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "intakes")
public class Intake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IntakeStatus status;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private boolean active = true;

    protected Intake() {
    }

    public Intake(
            Prescription prescription,
            LocalDateTime scheduledAt,
            LocalDateTime takenAt,
            IntakeStatus status,
            String notes
    ) {
        this.prescription = prescription;
        this.scheduledAt = scheduledAt;
        this.takenAt = takenAt;
        this.status = status;
        this.notes = notes;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public IntakeStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public void update(
            Prescription prescription,
            LocalDateTime scheduledAt,
            LocalDateTime takenAt,
            IntakeStatus status,
            String notes
    ) {
        this.prescription = prescription;
        this.scheduledAt = scheduledAt;
        this.takenAt = takenAt;
        this.status = status;
        this.notes = notes;
    }

    public void deactivate() {
        this.active = false;
    }
}
