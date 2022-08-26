package cnam.medical.pacs.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import org.jboss.logging.Logger;

import cnam.medical.pacs.domain.dao.PatientRepo;
import cnam.medical.pacs.domain.model.Patient;
import cnam.medical.pacs.domain.model.Patient.Sex;
import cnam.medical.pacs.exception.WrongArg;
import io.quarkus.security.Authenticated;

@Path("/patient")
@Authenticated
public class PatientUI {

    private Logger LOGGER = Logger.getLogger(PatientUI.class);

    @Inject
    PatientRepo patientRepo;

    
    /** 
     * @return List<Patient>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatients() {

        LOGGER.info("Get List of Patients");
        return patientRepo.listAll();

    }

    
    /** 
     * @param id
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getPatient(@PathParam("id") Long id) {

        LOGGER.info("Get Patient by id");
        Patient patient = patientRepo.findById(id);
        return Response.status(Status.OK).entity(patient).build();

    }

    
    /** 
     * @param lastname
     * @return List<Patient>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lastname/{lastname}")
    public List<Patient> getPatientByLastName(@PathParam("lastname") String lastname) {

        LOGGER.info("Get Patients by lastname");
        return patientRepo.findByLastName(lastname);

    }

    
    /** 
     * @param firstname
     * @return List<Patient>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/firstname/{firstname}")
    public List<Patient> getPatientByFirstName(@PathParam("firstname") String firstname) {

        LOGGER.info("Get Patients by firstname");
        return patientRepo.findByFirstName(firstname);

    }

    
    /** 
     * @param sex
     * @return List<Patient>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sex/{sex}")
    public List<Patient> getPatientBySex(@PathParam("sex") Sex sex) {

        LOGGER.info("Get Patients by sex");
        return patientRepo.findBySex(sex);

    }

    
    /** 
     * @param birth
     * @return List<Patient>
     * @throws WrongArg
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/birth/{birth}")
    public List<Patient> getPatientByBirth(@PathParam("birth") String birth) throws WrongArg {

        LOGGER.info("Get Patients by birthdate: " + birth);

        // convert String to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate localDate = null;

        try {
            localDate = LocalDate.parse(birth, formatter);
        } catch (Exception e) {

            LOGGER.info("Exception parse date " + birth);
            throw new WrongArg("Error in birth format: yyyy-MM-dd");

        }

        return patientRepo.findByBirth(localDate);

    }

    
    /** 
     * @param patient
     * @return Response
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed("Secretaire")
    public Response savePatient(Patient patient) {

        LOGGER.info("Post Patient");
        patientRepo.persist(patient);
        return Response.status(Status.CREATED).entity(patient).build();

    }

    
    /** 
     * @param id
     * @param patient
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @RolesAllowed("Secretaire")
    public Response updatePatient(@PathParam("id") Long id, Patient patient){

        LOGGER.info("Update Patient id: "+id);
        Patient tempPatient= patientRepo.findById(id);
        tempPatient.birth=patient.birth;
        tempPatient.firstName=patient.firstName;
        tempPatient.lastName=patient.lastName;
        tempPatient.sex=patient.sex;
        tempPatient.persist();

        return Response.status(Status.OK).entity(tempPatient).build();


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
    public Response deletePatient(@PathParam("id") Long id) {

        LOGGER.info("Delete Patient id: "+id);
        patientRepo.deleteById(id);
        return Response.status(Status.OK).build();

    }

}
