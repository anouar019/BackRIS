package cnam.medical.pacs.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * CompteRendu is the model uses to store information about the exam's report
 * Written by the secretary and validated by the radiologist
 * This class extends PanacheEntity
 */
@Entity
public class CompteRendu extends PanacheEntity {

    // In charge of CR
    @NotNull
    @NotBlank
    @NotEmpty
    public String secretaireLogin;
    @NotNull
    @NotBlank
    @NotEmpty
    public String medecinLogin;

    // Contenu du CR
    public String titre;
    public String corp;
    public String signature;

    // Extension possible
    public String extension1;
    public String extension2;
    public String extension3;

    // Etat du CR
    @NotNull
    public Etat etat;

    // // Examen
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    public Examen examen;

    public enum Etat {

        NULL, WRITTEN, VALIDATED

    }

}
