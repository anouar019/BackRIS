package cnam.medical.pacs.domain.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Document is my Document model uses to store information about Document
 * to database
 * This class extends PanacheEntity
 */

@Entity
public class Document extends PanacheEntity {

    @NotNull
    public TYPEDOCUMENT typeDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    public Patient patient;

    @NotNull
    @NotBlank
    @NotEmpty
    public String nameFile;

    @NotNull
    @Lob
    public String myfileBase64;

    public LocalDate localedate;

    public enum TYPEDOCUMENT {
        Identity, Insurance, Prescription, Other
    }

}
