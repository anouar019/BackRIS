package cnam.medical.pacs.api;

import java.util.List;

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

import cnam.medical.pacs.domain.dao.ExamenRepo;
import cnam.medical.pacs.domain.model.Examen;
import org.jboss.logging.Logger;

@Path("/examen")
public class ExamenUI {


    @Inject
    ExamenRepo myRepo;

    private Logger LOGGER = Logger.getLogger(ExamenUI.class);


    
    /** 
     * @return List<Examen>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examen> getExamens() {

        LOGGER.info("Get List of Examens");
        return myRepo.listAll();

    }

    
    /** 
     * @param examen
     * @return Response
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveExamen(Examen examen) {

        LOGGER.info("Post Examen");
        myRepo.persist(examen);
        return Response.status(Status.CREATED).entity(examen).build();

    }



    
    /** 
     * @param id
     * @param examen
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response updateExamen(@PathParam("id") Long id, Examen examen){

        LOGGER.info("Update Examen id: "+id);
        Examen tempExamen= myRepo.findById(id);
        tempExamen.date=examen.date;
        tempExamen.manipulateur=examen.manipulateur;

        if(examen.medecinPrescripteur!=null)
        tempExamen.medecinPrescripteur=examen.medecinPrescripteur;

        tempExamen.medecinRadiologue=examen.medecinRadiologue;
        tempExamen.patient=examen.patient;
        tempExamen.protocole=examen.protocole;
        tempExamen.persist();

        return Response.status(Status.OK).entity(tempExamen).build();


    }


    
    /** 
     * @param id
     * @return Response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteExamen(@PathParam("id") Long id) {

        LOGGER.info("Delete Examen id: "+id);
        myRepo.deleteById(id);
        return Response.status(Status.OK).build();

    }
    
}
