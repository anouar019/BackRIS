package cnam.medical.pacs.domain.dao;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import cnam.medical.pacs.domain.model.Examen;
import cnam.medical.pacs.domain.model.Patient;
import cnam.medical.pacs.domain.model.Protocole;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * <b>ExamentRepo allows us to download the exams from database and to store
 * them using PanacheRepository</b>
 */

@ApplicationScoped
public class ExamenRepo implements PanacheRepository<Examen>  {


    
    /** 
     * @param protocole
     * @return List<Examen>
     */
    public List<Examen> findByProtocole(Protocole protocole){

        return find("protocole", protocole).list();

    }

    
    /** 
     * @param patient
     * @return List<Examen>
     */
    public List<Examen> findByPatient(Patient patient){

        return find("patient", patient).list();

    }

    
    /** 
     * @param medecinPrescripteur
     * @return List<Examen>
     */
    public List<Examen> findByMedecinPrescripteur(String medecinPrescripteur){

        return find("upper(medecinPrescripteur)", medecinPrescripteur.toUpperCase()).list();

    }

    
    /** 
     * @param medecinRadiologue
     * @return List<Examen>
     */
    public List<Examen> findByMedecinRadiologue(String medecinRadiologue){

        return find("upper(medecinRadiologue)", medecinRadiologue.toUpperCase()).list();

    }


    
    /** 
     * @param manipulateur
     * @return List<Examen>
     */
    public List<Examen> findByManipulateur(String manipulateur){

        return find("upper(manipulateur)", manipulateur.toUpperCase()).list();

    }

    
    /** 
     * @param date
     * @return List<Examen>
     */
    public List<Examen> findByDate(LocalDate date){

        return find("date", date).list();

    }

}
