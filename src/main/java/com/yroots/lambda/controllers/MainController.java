package com.yroots.lambda.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yroots.lambda.configs.AppConstants;
import com.yroots.lambda.configs.Util;
import com.yroots.lambda.models.APIStatus;
import com.yroots.lambda.models.Attachment;
import com.yroots.lambda.models.GenericResponse;
import com.yroots.lambda.models.RequestPayload;
import com.yroots.lambda.services.FileStorageService;
import com.yroots.lambda.services.RequestHandler;


@RestController
public class MainController {

	@Autowired
	@Qualifier("requestHandler")
	private RequestHandler requestHandler;
	
	@Autowired	
	private FileStorageService fileStorageService;
	
	
	@PostMapping(path="notify",consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<? extends GenericResponse> notify(
			@Valid @RequestBody RequestPayload request){
		GenericResponse genericResponse=new GenericResponse(HttpStatus.CREATED.toString(), APIStatus.SUCCESS, "Processing request",null);		
		requestHandler.handle(request);		
		return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
	}
	
	@PostMapping(path="notify",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<? extends GenericResponse> notifyWithFiles(
			@RequestParam("request") @Valid String requestStr,@RequestParam("files") MultipartFile[] files){
		GenericResponse genericResponse=new GenericResponse(HttpStatus.CREATED.toString(), APIStatus.SUCCESS, "Processing request",null);		
		RequestPayload request=null;
		try {
			request = (new ObjectMapper()).readValue(requestStr, RequestPayload.class);			
			if(request!=null && 
					request.getEmailPayload()!=null &&
					files!=null && 
					files.length>0) {
				List<Attachment> attachments=new ArrayList<>();				
				for(MultipartFile file:files) {					
					Map<String,String> savedInfo=fileStorageService.storeFile(file,request.getServiceName(),request.getCategory());
					attachments.add(Attachment.newInstance(savedInfo.get(AppConstants.FILENAME_KEY),savedInfo.get(AppConstants.LOCAL_FILEPATH_KEY)));					
				}
				if(attachments.size()>0 && 
						request!=null && 
						request.getEmailPayload()!=null) {
						request.getEmailPayload().setAttachments(attachments);
					}						
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(request);
		requestHandler.handle(request);			
		return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
	}
	
}
