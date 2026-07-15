package com.param.global.common;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.param.global.dto.PatientRegistrationDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class HealthCardPdf {
	final static Logger logger = Logger.getLogger(HealthCardPdf.class);

	public void generateHealthCard(PatientRegistrationDto patient, OutputStream outputStream){
		Document document = null;
		try{
			String allergies=null;
			
			if(patient.getAllergies()!=null)
				allergies = patient.getAllergies().toString().replace("[", "").replace("]", "");
			
			Font commonFont = FontFactory.getFont(FontFactory.HELVETICA, 6);
			Font commonBoldFont = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.BOLD);
			PdfPTable table = null;
			PdfPCell cell = null;
			
			float height = 2.16f * 72;
			float width = 3.34f * 72;
			float marginTop = 0.5f * 72;
			float marginBottom = 0.0f * 72;
			float marginLeft = 0.1f * 72;
			float marginRight = 0.1f * 72;
			Rectangle pagesize = new Rectangle(width, height);
			document = new Document(pagesize,marginLeft,marginRight,marginTop,marginBottom);
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			
			//Patient Image
			//String imgpath = "https://s3.ap-south-1.amazonaws.com/his-test/rubyhall1/rubyhallm2/unit-logo/sama.png";
			String imgpath = patient.getImagePath();	
			String emptyframe = "https://s3.ap-south-1.amazonaws.com/his-test/emptyframe.png";
			if(imgpath != null && !imgpath.isEmpty() && !imgpath.equals("[NULL]")){
				Image img = Image.getInstance(imgpath);
				img.setAbsolutePosition(190f, 93f);
				img.scaleAbsolute(40, 40);
				img.setSpacingBefore(5);
				document.add(img); 
			}else{
				Image img = Image.getInstance(emptyframe);
				img.setAbsolutePosition(190f, 93f);
				img.scaleAbsolute(40, 40);
				img.setSpacingBefore(5);
				document.add(img);
			}
			
			
			table = new PdfPTable(1);
			float nameAddWidth[] = {80};
			table.setWidths(nameAddWidth);
			table.setWidthPercentage(100);
			table.getDefaultCell().setBorder(0);
			
			cell = new PdfPCell(new Phrase("Name : " + (patient.getPatientName() != null ? patient.getPatientName() : "-"), commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			document.add(table);
			
			table = new PdfPTable(2);
			float cityPinWidth[] = {50,50};
			table.setWidths(cityPinWidth);
			table.setWidthPercentage(80);
			table.getDefaultCell().setBorder(0);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			cell = new PdfPCell(new Phrase("UHID : " + (patient.getUhidNumber() != null ? patient.getUhidNumber() : "-"), commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			
			/*cell = new PdfPCell(new Phrase("Blood Group : O +ve", commonFont));
			cell.setBorder(0);
			table.addCell(cell);*/
			
			cell = new PdfPCell(new Phrase("Age : "+ (patient.getAgeString() != null ? patient.getAgeString() : "-") , commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("DOB : " + (patient.getDob() != null ? patient.getDob() : "-"), commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Sex : " + (patient.getGenderCode() != null ? patient.getGenderCode() : "-"), commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Mobile : " + (patient.getMobileNumber() != null && !patient.getMobileNumber().isEmpty() ? patient.getMobileNumber() : "-"), commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			document.add(table);
			
			table = new PdfPTable(2);
			float uhidWidth[] = {22,80};
			table.setWidths(uhidWidth);
			table.setWidthPercentage(70);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.getDefaultCell().setBorder(0);
			
			cell = new PdfPCell(new Phrase("Address : ", commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase((patient.getAddress() != null && !patient.getAddress().isEmpty() ? patient.getAddress() : "-"), commonFont));
			cell.setBorder(0);
			table.addCell(cell);
			
			document.add(table);

			Barcode39 barcode39 = new Barcode39();
			//barcode39.setCode(patient.getMrnNumber() != null ? patient.getMrnNumber() : "-");
			barcode39.setCode(patient.getUhidNumber() != null ? patient.getUhidNumber() : "-");
			barcode39.setBarHeight(15);
			barcode39.setAltText("");
			Image barcodeImg = barcode39.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);
			barcodeImg.setAbsolutePosition(130f, 1f);
			barcodeImg.scaleAbsolute(100, 30);
			document.add(barcodeImg); 
			
			document.newPage();
			if(patient.getKinDetailsDto()!=null && allergies !=null){
			Paragraph para = new Paragraph();
			para.setSpacingBefore(55);
			para.add(new Phrase("Emergency Contact Number : " + (patient.getKinDetailsDto().getMobileNo() != null && !patient.getKinDetailsDto().getMobileNo().isEmpty() ? patient.getKinDetailsDto().getMobileNo() : "-")
					+ "\nName : "+(patient.getKinDetailsDto().getKinName() != null && !patient.getKinDetailsDto().getKinName().isEmpty() ? patient.getKinDetailsDto().getKinName() : "-") +"     Relation :  " + (patient.getKinDetailsDto().getRelationDesc() != null && !patient.getKinDetailsDto().getRelationDesc().isEmpty() ? patient.getKinDetailsDto().getRelationDesc() : "-")
					+ "\nAllergies : "+(allergies.length()>0 ? allergies : "-")  , commonFont));
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);
			}
			//System.out.println("Health Card Generated");
			
		}catch(Exception e){
		logger.error("generateHealthCard failed", e);
		}finally{
			try{
				if(document != null && outputStream != null){
					document.close();
					outputStream.close();
				}
			}catch(IOException e){
			logger.error("generateHealthCard failed", e);
			}
		}
	}
}
