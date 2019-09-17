package com.yroots.lambda.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.yroots.lambda.configs.AppConstants;
import com.yroots.lambda.configs.FileStorageProperties;
import com.yroots.lambda.exceptions.FileStorageException;

import javassist.NotFoundException;

@Service
public class FileStorageService {
	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

	private final Path fileStorageLocation;
	
	@Value("${dir.invalid.chars}")
	private String invalidChars;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getTmpPath()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public Map<String,String> storeFile(MultipartFile file,String srvName,String catName) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			fileName="T-"+System.nanoTime()+"-"+fileName;			
			//create dir for service/cat
			String srvDir=createDir(this.fileStorageLocation,srvName);
			String catDir=createDir(this.fileStorageLocation.resolve(srvDir),catName);
			fileName=srvDir+File.separator+catDir+File.separator+fileName;
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);			
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			Map<String,String> map=new HashMap<String,String>();	
			map.put(AppConstants.FILENAME_KEY, file.getOriginalFilename());
			map.put(AppConstants.LOCAL_FILEPATH_KEY, targetLocation.toString());
			return map;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	private String createDir(Path parentDir,String path) throws IOException {
		path=path.toLowerCase().trim();
		path = path.replaceAll(invalidChars, "_");
		Path newDir=parentDir.resolve(path);
		Files.createDirectories(newDir);	
		return path;
	}
	public Resource loadFileAsResource(String fileName) throws Exception {
		try {
			if (StringUtils.hasText(fileName)) {

				String actutalFile = fileName;
				Path filePath = this.fileStorageLocation.resolve(actutalFile).normalize();
				Resource resource = new UrlResource(filePath.toUri());

				if (resource.exists()) {
					return resource;
				} else {
					throw new NotFoundException("File not found " + fileName);
				}

			}
		} catch (MalformedURLException ex) {
			throw ex;
		}
		throw new NotFoundException("File not found " + fileName);
	}

	public void deleteFileAsResource(String fileName) {
		try {
			if (StringUtils.hasText(fileName)) {

				String actutalFile = fileStorageLocation + File.separator + fileName;
				File file = new File(actutalFile);

				if (file.delete()) {
					logger.info("File deleted: " + fileName);
				}

			}
		} catch (Exception ex) {
			logger.error("Error:" + ex.getMessage());
		}
	}
}
