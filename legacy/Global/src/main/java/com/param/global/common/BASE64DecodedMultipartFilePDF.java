package com.param.global.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class BASE64DecodedMultipartFilePDF implements MultipartFile {
	private final byte[] pdfContent;

	public BASE64DecodedMultipartFilePDF(byte[] imgContent) {
		this.pdfContent = imgContent;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getOriginalFilename() {
		return null;
	}

	@Override
	public String getContentType() {
		return "application/pdf";
	}

	@Override
	public boolean isEmpty() {
		return pdfContent != null && pdfContent.length > 0;
	}

	@Override
	public long getSize() {
		return pdfContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return pdfContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(pdfContent);
	}

	@SuppressWarnings("resource")
	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		new FileOutputStream(dest).write(pdfContent);
		
	}


}
