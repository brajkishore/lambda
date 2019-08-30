package com.yroots.lambda.models;

public class GenericResponse {

	private String code;
	private APIStatus status;
	private String message;
	private Object data;
	
	public GenericResponse(String code,APIStatus status,String message,Object data) {
		this.code=code;
		this.status=status;
		this.message=message;
		this.data=data;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public APIStatus getStatus() {
		return status;
	}
	public void setStatus(APIStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GenericResponse [code=" + code + ", status=" + status + ", message=" + message + ", data=" + data + "]";
	}	
}
