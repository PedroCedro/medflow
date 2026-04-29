package br.com.infocedro.medflow.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private RelationshipType relationship;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private boolean active = true;

    protected Patient() {
    }

    public Patient(String name, LocalDate birthDate, RelationshipType relationship, String notes) {
        this.name = name;
        this.birthDate = birthDate;
        this.relationship = relationship;
        this.notes = notes;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public RelationshipType getRelationship() {
        return relationship;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public void update(String name, LocalDate birthDate, RelationshipType relationship, String notes) {
        this.name = name;
        this.birthDate = birthDate;
        this.relationship = relationship;
        this.notes = notes;
    }

    public void deactivate() {
        this.active = false;
    }
}
