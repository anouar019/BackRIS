package cnam.medical.pacs.domain.dao;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import cnam.medical.pacs.domain.model.Patient;
import cnam.medical.pacs.domain.model.Patient.Sex;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


  /** 
   * <b>PatientRepo allows us to download the patients from database and to store them using PanacheRepository</b>
   */

@ApplicationScoped
public class PatientRepo implements PanacheRepository<Patient> {
    
    
    /** 
     * @param lastName
     * @return List<Patient>
     */
    public List<Patient> findByLastName(String lastName){

        return find("upper(lastName)", lastName.toUpperCase()).list();

    }


    
    /** 
     * @param firstName
     * @return List<Patient>
     */
    public List<Patient> findByFirstName(String firstName){

        return find("upper(firstName)", firstName.toUpperCase()).list();

    }



    
    /** 
     * @param sex
     * @return List<Patient>
     */
    public List<Patient> findBySex(Sex sex){

        return find("sex", sex).list();

    }




    
    /** 
     * @param birth
     * @return List<Patient>
     */
    public List<Patient> findByBirth(LocalDate birth){

        return find("birth", birth).list();

    }


    
}
