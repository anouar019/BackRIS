package cnam.medical.pacs.api;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import io.smallrye.mutiny.Uni;

@Path("/patient/file")
public class downloadAccessPoint {

    private final String UPLOADED_FILE_PATH = "/home/lenovo/Téléchargements/down/";

    
    /** 
     * @param id
     * @param fileName
     * @return Uni<Response>
     */
    @GET
    @Path("/download/{id}/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> getFile1(@PathParam("id") Long id, @PathParam("fileName") String fileName) {

        String myPath = UPLOADED_FILE_PATH + "/" + id + "/" + fileName;

        File myFile = new File(myPath);
        System.out.println("myFile.exists()" + myFile.exists());

        if (myFile.exists()) {
            ResponseBuilder response = Response.ok((Object) myFile);
            response.header("Content-Disposition", "attachment;filename=" + myFile);
            Uni<Response> re = Uni.createFrom().item(response.build());
            return re;
        }

        return Uni.createFrom().item(Response.status(404).build());
    }

}
