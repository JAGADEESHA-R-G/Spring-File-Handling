package servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RestController
@RequestMapping("/files")
public class HandleFiles {

	public static String upload_directory = "/files";
	
	@RequestMapping(method = RequestMethod.POST)
	public void upload(@RequestParam("file") CommonsMultipartFile file, HttpSession session) throws IOException{
		
		ServletContext context = session.getServletContext();
		String path = context.getRealPath(upload_directory);
		byte[] bytes = file.getBytes();
		
		File f = new File(path + File.separator + file.getOriginalFilename());
		try {
		
		System.out.println(f);	
			
		BufferedOutputStream bufferstream =new BufferedOutputStream(new FileOutputStream(f));
		
		bufferstream.write(bytes);
		bufferstream.flush();
		bufferstream.close();
		System.out.println("File uploaded successfully");
		}
		catch(Exception e)
		{
			
			System.out.println(" Error occured");
		}
		
	}

	@RequestMapping(method = RequestMethod.GET)
	public void download_2(@RequestParam("filename") String fileName,  HttpServletResponse response, HttpSession session) throws IOException{
		
	   
	   ServletContext context = session.getServletContext();
	   String path = context.getRealPath(upload_directory);
	   Path file = Paths.get(path, fileName);
	   System.out.println("filepath = " +file);	   

	   response.setContentType("application/pdf");
	   response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
	   try {
			Files.copy(file, response.getOutputStream());
			response.getOutputStream().flush();		
	   } catch (Exception e) {
			System.out.println("Error occured");
	   }
	
	}
}



























	
