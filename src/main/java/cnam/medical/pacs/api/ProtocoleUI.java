package cnam.medical.pacs.api;

import java.util.List;

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

import cnam.medical.pacs.domain.dao.ProtocoleRepo;
import cnam.medical.pacs.domain.model.Protocole;
import cnam.medical.pacs.domain.model.Protocole.Injection;
import cnam.medical.pacs.domain.model.Protocole.Membre;
import cnam.medical.pacs.domain.model.Protocole.Technique;
import cnam.medical.pacs.domain.model.Protocole.Type;
import io.quarkus.security.Authenticated;

import org.jboss.logging.Logger;

@Path("/protocole")
@Authenticated
public class ProtocoleUI {

    private Logger LOGGER = Logger.getLogger(ProtocoleUI.class);


    @Inject
    ProtocoleRepo myRepo;

    
    /** 
     * @return List<Protocole>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Protocole> getProtocoles() {

        LOGGER.info("Get List of Protocoles");
        return myRepo.listAll();

    }


    
    /** 
     * @param protocole
     * @return Response
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed("ManipulateurReferent")
    public Response saveProtocole(Protocole protocole){


        LOGGER.info("Post Protocole");
        myRepo.persist(protocole);
        return Response.status(Status.CREATED).entity(protocole).build();
    }



    
    /** 
     * @param id
     * @param protocole
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ManipulateurReferent")
    public Response updateProtocole(@PathParam("id") Long id, Protocole protocole){

        LOGGER.info("Update Protocole id: "+id);
        Protocole tempProtocole= myRepo.findById(id);
        tempProtocole.date=protocole.date;
        tempProtocole.injection=protocole.injection;
        tempProtocole.membre=protocole.membre;
        tempProtocole.technique=protocole.technique;
        tempProtocole.type=protocole.type;


        tempProtocole.persist();

        return Response.status(Status.OK).entity(tempProtocole).build();


    }



    
    /** 
     * @param id
     * @return Response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ManipulateurReferent")
    public Response deleteProtocole(@PathParam("id") Long id) {



        LOGGER.info("Delete protocole by id: "+id);
        myRepo.deleteById(id);
        return Response.status(Status.OK).build();

    }


    
    /** 
     * @param id
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getProtocoleById(@PathParam("id") Long id) {


        LOGGER.info("Find protocole by id: "+id);
        Protocole protocole = myRepo.findById(id);
        return Response.status(Status.OK).entity(protocole).build();

    }


    
    /** 
     * @param technique
     * @return List<Protocole>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/technique/{technique}")
    public List<Protocole> getProtocoleByTechnique(@PathParam("technique") Technique technique) {


        LOGGER.info("Find protocole by technique: "+technique);
        return myRepo.findByTechnique(technique);

    }


    
    /** 
     * @param membre
     * @return List<Protocole>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/membre/{membre}")
    public List<Protocole> getProtocoleByMembre(@PathParam("membre") Membre membre) {

        LOGGER.info("Find protocole by membre: "+membre);
        return myRepo.findByMembre(membre);

    }

    
    /** 
     * @param type
     * @return List<Protocole>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/{type}")
    public List<Protocole> getProtocoleByType(@PathParam("type") Type type) {

        LOGGER.info("Find protocole by type: "+type);
        return myRepo.findByType(type);

    }


    
    /** 
     * @param injection
     * @return List<Protocole>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/injection/{injection}")
    public List<Protocole> getProtocoleByInjection(@PathParam("injection") Injection injection) {

        LOGGER.info("Find protocole by injection: "+injection);
        return myRepo.findByInjection(injection);

    }


}
