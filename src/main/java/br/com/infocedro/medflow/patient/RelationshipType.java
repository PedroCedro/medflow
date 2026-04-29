package br.com.infocedro.medflow.patient;

public enum RelationshipType {
    SELF("Eu"),
    FATHER("Pai"),
    MOTHER("Mãe"),
    SON("Filho"),
    DAUGHTER("Filha"),
    SPOUSE("Cônjuge"),
    GRANDFATHER("Avô"),
    GRANDMOTHER("Avó"),
    OTHER("Outro");

    private final String displayNamePtBr;

    RelationshipType(String displayNamePtBr) {
        this.displayNamePtBr = displayNamePtBr;
    }

    public String getDisplayNamePtBr() {
        return displayNamePtBr;
    }
}
