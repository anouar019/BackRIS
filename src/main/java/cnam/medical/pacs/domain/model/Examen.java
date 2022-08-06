package cnam.medical.pacs.domain.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;

/**
 * Examen is my Examen model uses to store information about exams
 * to database
 * This class extends PanacheEntity
 */

@Entity
public class Examen extends PanacheEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Protocole protocole;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Patient patient;

    public String medecinPrescripteur;

    @NotNull
    public String medecinRadiologue;

    @NotNull
    public String manipulateur;

    @NotNull
    public LocalDate date;
}
