package com.yroots.lambda.services;

public class EmailStatusListener implements WorkStatusListener{

	@Override
	public void success(Object success) {
		StaticContextProvider.getLoggerConfig().getEmailAuditLogger()
		.info(success.toString());
	}

	@Override
	public void fail(Object error) {
		StaticContextProvider.getLoggerConfig().getEmailAuditLogger()
		.error(error.toString());
	}

}
