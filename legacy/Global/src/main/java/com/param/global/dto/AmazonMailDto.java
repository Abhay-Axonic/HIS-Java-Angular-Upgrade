package com.param.global.dto;

import java.util.List;

public class AmazonMailDto {

	 private String to; 
	 
	 private String from;
	 
	 private String subject; 
	 
	 private String data;
	 
	 private String bcc;
	 
	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	private List<AmazonMailAttachement> attachment;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<AmazonMailAttachement> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<AmazonMailAttachement> attachment) {
		this.attachment = attachment;
	}
	 
}
