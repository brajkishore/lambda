package com.yroots.lambda.services;

public class SmsStatusListener implements WorkStatusListener{

	@Override
	public void success(Object success) {
		StaticContextProvider.getLoggerConfig().getSmsAuditLogger()
		.info(success.toString());
	}

	@Override
	public void fail(Object error) {
		StaticContextProvider.getLoggerConfig().getSmsAuditLogger()
		.error(error.toString());
	}

}
