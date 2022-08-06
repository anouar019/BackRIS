package cnam.medical.pacs.domain.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Patient is my Patient model uses to store information about patients identity
 * to database
 * This class extends PanacheEntity
 */

@Entity
public class Patient extends PanacheEntity {

  @NotNull
  @NotBlank
  @NotEmpty
  public String lastName;

  @NotNull
  @NotBlank
  @NotEmpty
  public String firstName;

  @NotNull
  public LocalDate birth;

  @NotNull
  public Sex sex;

  public enum Sex {
    MALE, FEMALE
  }

}
