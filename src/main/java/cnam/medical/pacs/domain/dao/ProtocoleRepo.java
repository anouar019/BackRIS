package cnam.medical.pacs.domain.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import cnam.medical.pacs.domain.model.Protocole;
import cnam.medical.pacs.domain.model.Protocole.Injection;
import cnam.medical.pacs.domain.model.Protocole.Membre;
import cnam.medical.pacs.domain.model.Protocole.Technique;
import cnam.medical.pacs.domain.model.Protocole.Type;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * <b>ProtocoleRepo allows us to download the protocols from database and to
 * store them using PanacheRepository</b>
 */

@ApplicationScoped
public class ProtocoleRepo implements PanacheRepository<Protocole> {


    
    /** 
     * @param technique
     * @return List<Protocole>
     */
    public List<Protocole> findByTechnique(Technique technique){

        return find("technique", technique).list();
    }



    
    /** 
     * @param membre
     * @return List<Protocole>
     */
    public List<Protocole> findByMembre(Membre membre){

        return find("membre", membre).list();
    }

    
    
    /** 
     * @param type
     * @return List<Protocole>
     */
    public List<Protocole> findByType(Type type){

        return find("type", type).list();
    }


    
    /** 
     * @param injection
     * @return List<Protocole>
     */
    public List<Protocole> findByInjection(Injection injection){

        return find("injection", injection).list();
    }


    
    /** 
     * @param date
     * @return List<Protocole>
     */
    public List<Protocole> findByDate(Date date){

        return find("date", date).list();
    }



}
