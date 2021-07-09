package servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
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

	public static String upload_directory = "/home/local/ZOHOCORP/jagadeesh-11774/Documents/CustomFolder";

	@RequestMapping(method = RequestMethod.POST)
	public void upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam("path") String CustomFolder,
			HttpSession session, HttpServletResponse response) throws IOException {

		byte[] bytes = file.getBytes();
		int pos = CustomFolder.lastIndexOf("/");
		String Folders = CustomFolder.substring(0, pos);
		String FolderPath = upload_directory + Folders;
		String fileName = CustomFolder.substring(pos+1);
		boolean flag = false;

		File f = new File(FolderPath);

		if (!f.exists()) {
			f.mkdirs();
		}

		File f1 = new File(FolderPath + File.separator + fileName);

		String[] fileList = f.list();
		for (String str : fileList) {
			System.out.println(str + " = " +fileName);
			if (fileName.equals(str)) {
				flag = true;
				break;
			}
		}
		System.out.println(flag);
		if (flag) {
			String resp = " File already exist, try with different name! ";
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(resp);
			response.getWriter().flush();
		}
		else {
			try {
				BufferedOutputStream bufferstream = new BufferedOutputStream(new FileOutputStream(f1));
				bufferstream.write(bytes);
				bufferstream.flush();
				bufferstream.close();
				System.out.println("File uploaded successfully");
			} catch (Exception e) {
				System.out.println(" Error occured");
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public void download_2(@RequestParam("path") String path, HttpServletResponse response, HttpSession session)
			throws IOException {

		String Folderpath = upload_directory + path.substring(0, path.lastIndexOf("/"));
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		File f = new File(Folderpath);
		String[] fileList = f.list();
		boolean flag = false;

		for (String str : fileList) {
			if (fileName.equals(str)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			String resp = " File Doesn't exist";
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(resp);
			response.getWriter().flush();

		} else {
			Path file = Paths.get(Folderpath, fileName);
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
}
