package br.com.infocedro.medflow.medication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "active_ingredient", length = 120)
    private String activeIngredient;

    @Column(length = 80)
    private String presentation;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private boolean active = true;

    protected Medication() {
    }

    public Medication(String name, String activeIngredient, String presentation, String notes) {
        this.name = name;
        this.activeIngredient = activeIngredient;
        this.presentation = presentation;
        this.notes = notes;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public String getPresentation() {
        return presentation;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public void update(String name, String activeIngredient, String presentation, String notes) {
        this.name = name;
        this.activeIngredient = activeIngredient;
        this.presentation = presentation;
        this.notes = notes;
    }

    public void deactivate() {
        this.active = false;
    }
}
