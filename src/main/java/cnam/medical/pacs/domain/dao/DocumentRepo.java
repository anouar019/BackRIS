package cnam.medical.pacs.domain.dao;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import cnam.medical.pacs.domain.model.Document;
import cnam.medical.pacs.domain.model.Patient;
import cnam.medical.pacs.domain.model.Document.TYPEDOCUMENT;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


 /** 
   * <b>DocumentRepo allows us to download the documents from database and to store them using PanacheRepository</b>
   */

@ApplicationScoped
public class DocumentRepo implements PanacheRepository<Document> {

    
    /** 
     * @param patient
     * @return List<Document>
     */
    public List<Document> findByPatient(Patient patient){

        return find("patient", patient).list();

    }


    
    /** 
     * @param typeDocument
     * @return List<Document>
     */
    public List<Document> findByTypeDocument(TYPEDOCUMENT typeDocument){

        return find("typeDocument", typeDocument).list();

    }

    
    /** 
     * @param localedate
     * @return List<Document>
     */
    public List<Document> findByDate(LocalDate localedate){

        return find("localedate", localedate).list();

    }

    
}
