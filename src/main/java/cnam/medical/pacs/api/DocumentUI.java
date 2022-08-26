package cnam.medical.pacs.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import cnam.medical.pacs.domain.dao.DocumentRepo;
import cnam.medical.pacs.domain.model.Document;
import cnam.medical.pacs.domain.model.Patient;
import cnam.medical.pacs.domain.model.Document.TYPEDOCUMENT;
import cnam.medical.pacs.exception.WrongArg;
import io.quarkus.security.Authenticated;

@Path("document")
@Authenticated
public class DocumentUI {

    private Logger LOGGER = Logger.getLogger(DocumentUI.class);

    @Inject
    private DocumentRepo myRepo;

    
    /** 
     * @return List<Document>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Document> getDocuments() {

        LOGGER.info("List all Documents");
        return myRepo.listAll();
    }

    
    /** 
     * @param document
     * @return Response
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed("Secretaire")
    public Response saveDocument(Document document) {

        LOGGER.info("Post Document " + document.id);
        myRepo.persist(document);
        return Response.status(Status.CREATED).entity(document).build();

    }

    
    /** 
     * @param id
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getDocumentById(@PathParam("id") Long id) {

        LOGGER.info("Get document by id: " + id);
        Document document = myRepo.findById(id);
        return Response.status(Status.OK).entity(document).build();

    }

    
    /** 
     * @param id
     * @return List<Document>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/patient/{id}")
    public List<Document> getDocumentByPatientID(@PathParam("id") Long id) {

        LOGGER.info("Get Documents by patient id: " + id);

        Patient patient = Patient.findById(id);
        return myRepo.findByPatient(patient);// .findByLastName(lastname);

    }

    
    /** 
     * @param typeDocument
     * @return List<Document>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/{typeDocument}")
    public List<Document> getDocumentByType(@PathParam("typeDocument") TYPEDOCUMENT typeDocument) {

        LOGGER.info("Get Document by type: " + typeDocument);
        return myRepo.findByTypeDocument(typeDocument);
    }

    
    /** 
     * @param localedate
     * @return List<Document>
     * @throws WrongArg
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/date/{localedate}")
    public List<Document> getDocumentByDate(@PathParam("localedate") String localedate) throws WrongArg {

        LOGGER.info("Get Documents by date: " + localedate);

        // convert String to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate localDate = null;

        try {
            localDate = LocalDate.parse(localedate, formatter);
        } catch (Exception e) {

            // LOGGER.info("Exception parse date " + birth);
            throw new WrongArg("Error in birth format: yyyy-MM-dd");

        }

        return myRepo.findByDate(localDate);

    }

    
    /** 
     * @param id
     * @param document
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @RolesAllowed("Secretaire")
    public Response updateDocument(@PathParam("id") Long id, Document document) {

        LOGGER.info("Update Docuemetn id: " + id);
        Document tempDocument = myRepo.findById(id);
        tempDocument.localedate = document.localedate;
        tempDocument.myfileBase64 = document.myfileBase64;
        tempDocument.nameFile = document.nameFile;
        tempDocument.patient = document.patient;
        tempDocument.typeDocument = document.typeDocument;
        tempDocument.persist();

        return Response.status(Status.OK).entity(tempDocument).build();

    }

    
    /** 
     * @param id
     * @return Response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @RolesAllowed("Secretaire")
    public Response deleteDocument(@PathParam("id") Long id) {

        LOGGER.info("Delete Docuemnt id: " + id);
        myRepo.deleteById(id);
        return Response.status(Status.OK).build();

    }

}
