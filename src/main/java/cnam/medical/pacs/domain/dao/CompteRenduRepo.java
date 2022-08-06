package cnam.medical.pacs.domain.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import cnam.medical.pacs.domain.model.CompteRendu;
import cnam.medical.pacs.domain.model.Examen;
import cnam.medical.pacs.domain.model.CompteRendu.Etat;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * <b>CompteRenduRepo allows us to download the CR from database and to
 * store them using PanacheRepository</b>
 */

@ApplicationScoped
public class CompteRenduRepo implements PanacheRepository<CompteRendu> {

    
    /** 
     * @param secretaireLogin
     * @return List<CompteRendu>
     */
    public List<CompteRendu> findBySecretaire(String secretaireLogin) {

        return find("secretaireLogin", secretaireLogin).list();
    }

    
    /** 
     * @param medecinLogin
     * @return List<CompteRendu>
     */
    public List<CompteRendu> findByMedecin(String medecinLogin) {

        return find("medecinLogin", medecinLogin).list();
    }

    
    /** 
     * @param etat
     * @return List<CompteRendu>
     */
    public List<CompteRendu> findByEtat(Etat etat) {
       
        return find("etat", etat).list();
    }


    
    /** 
     * @param medecinLogin
     * @param etat
     * @return List<CompteRendu>
     */
    public List<CompteRendu> findByMedecinAndEtat(String medecinLogin, Etat etat) {
       
        return list("medecinLogin = ?1 and etat = ?2", medecinLogin,etat);
    }

    
    /** 
     * @param secretaireLogin
     * @param etat
     * @return List<CompteRendu>
     */
    public List<CompteRendu> findBySecretaireAndEtat(String secretaireLogin, Etat etat) {
       
        return list("secretaireLogin = ?1 and etat = ?2", secretaireLogin,etat);
    }


    public List<CompteRendu> findbyExamen(Examen examen){

        return  find("examen", examen).list();


    }

}
