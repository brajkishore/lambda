package com.yroots.lambda.models;

public class Attachment {
	
	private String filePath;
	private String fileName;
	private boolean retainCopy;

	private Attachment(String fileName,String filePath) {		
		this.fileName=fileName;
		this.filePath=filePath;
		this.retainCopy=true;
	}
	
	public static Attachment newInstance(String fileName,String filePath) {
		return new Attachment(fileName,filePath);
	}


	public String getFilePath() {
		return filePath;
	}

	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isRetainCopy() {
		return retainCopy;
	}

	public void setRetainCopy(boolean retainCopy) {
		this.retainCopy = retainCopy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "Attachment [filePath=" + filePath + ", fileName=" + fileName + ", retainCopy=" + retainCopy + "]";
	}
	
	
	
}
