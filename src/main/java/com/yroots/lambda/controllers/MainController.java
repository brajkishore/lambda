package com.yroots.lambda.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yroots.lambda.models.APIStatus;
import com.yroots.lambda.models.GenericResponse;
import com.yroots.lambda.models.RequestPayload;
import com.yroots.lambda.services.RequestHandler;

@RestController
public class MainController {

	@Autowired
	@Qualifier("requestHandler")
	private RequestHandler requestHandler;
	
	@PostMapping
	private ResponseEntity<? extends GenericResponse> processNotification(
			@Valid @RequestBody RequestPayload request){
		GenericResponse genericResponse=new GenericResponse(HttpStatus.CREATED.toString(), APIStatus.SUCCESS, "Processing request",null);		
		requestHandler.handle(request);		
		return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
	}
	
}
