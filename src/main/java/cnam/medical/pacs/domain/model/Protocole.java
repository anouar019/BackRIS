package cnam.medical.pacs.domain.model;

import java.util.Date;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.validation.constraints.NotNull;

/**
 * Protocole is the model uses to store information about how is structured the
 * exams
 * This class extends PanacheEntity
 */

@Entity
public class Protocole extends PanacheEntity {

    @NotNull
    public Technique technique;

    @NotNull
    public Membre membre;

    @NotNull
    public Type type;

    @NotNull
    public Injection injection;

    @NotNull
    public Date date = new Date();

    public enum Technique {
        SCANNER, RADIO, MEDECINE_NUCLEAIRE, ECHOGRAPHIE
    }

    public enum Membre {
        CRANE, TRONC, JAMBE, BRAS
    }

    public enum Type {
        MOU, OS
    }

    public enum Injection {
        OUI, NON
    }

}
