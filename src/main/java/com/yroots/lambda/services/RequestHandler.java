package com.yroots.lambda.services;

import com.yroots.lambda.models.RequestPayload;

public interface RequestHandler {
	public void handle(RequestPayload request);
}
