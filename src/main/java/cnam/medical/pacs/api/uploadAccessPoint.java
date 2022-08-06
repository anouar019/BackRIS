package cnam.medical.pacs.api;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/patient/file")
public class uploadAccessPoint {

	private final String UPLOADED_FILE_PATH = "/home/lenovo/Téléchargements/down/";
	
	
	/** 
	 * @param id
	 * @param input
	 * @return Response
	 */
	@POST
	@Path("/upload/{id}")
	@Consumes("multipart/form-data")
	public Response uploadFile(@PathParam("id") Long id, MultipartFormDataInput input) {


		String fileName = "";
		
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");

		for (InputPart inputPart : inputParts) {

		 try {

			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = getFileName(header);

			//convert the uploaded file to inputstream
			InputStream inputStream = inputPart.getBody(InputStream.class,null);

			byte [] bytes = IOUtils.toByteArray(inputStream);
				
			//constructs upload file path
			fileName = UPLOADED_FILE_PATH+id+"/"+fileName;
			writeFile(bytes,fileName);
				
			System.out.println("Done");

		  } catch (IOException e) {
			e.printStackTrace();
		  }

		}

		return Response.status(200)
		    .entity("uploadFile is called, Uploaded file name : " + fileName).build();

	}

	/**
	 * header sample
	 * {
	 * 	Content-Type=[image/png], 
	 * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
	 * }
	 **/
	//get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	//save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		System.out.println(filename);
		System.out.println("if (file.exists()) { +"+file.exists());
		System.out.println(file.getParentFile().mkdirs());
		//System.out.println(new File("/home/lenovo/Téléchargements/down/5/5").mkdirs());

		if (!file.exists()) {
			file.createNewFile();
		}

		System.out.println(" get parettn "+filename+" "+file.getParentFile().getAbsolutePath());
		System.out.println(file.getParentFile().mkdir());
		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
