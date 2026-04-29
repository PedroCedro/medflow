package br.com.infocedro.medflow.prescription;

public enum DosageUnit {
    TABLET("Comprimido"),
    CAPSULE("Cápsula"),
    DROP("Gota"),
    ML("mL"),
    MG("mg"),
    G("g"),
    PUFF("Jato"),
    SPOON("Colher");

    private final String displayNamePtBr;

    DosageUnit(String displayNamePtBr) {
        this.displayNamePtBr = displayNamePtBr;
    }

    public String getDisplayNamePtBr() {
        return displayNamePtBr;
    }
}
