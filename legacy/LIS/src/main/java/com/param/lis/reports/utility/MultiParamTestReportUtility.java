package com.param.lis.reports.utility;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.MultyParamTestHeaderDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.utility.TimeZoneComponent;

public class MultiParamTestReportUtility implements  ICommonConstants, IError,ITransactionConstants {
	

	final static Logger logger = Logger.getLogger(MultiParamTestReportUtility.class);

	static InputStream input;
	static SimpleDateFormat format;
	static Date date;
	
	
	
	static String formatDate (Date date)
	{
		if(date!=null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
			return	simpleDateFormat.format(date);
		}else{
			return "";
		}
		
	}
	
	 static String formatDecimal(String value) {
       try {
           NumberFormat formatter = new DecimalFormat("#0.00");
           return formatter.format(new Double(value));
       } catch (Exception e) {
           logger.error("Exception", e);
       }
       return "";
   }

	static String getDate(Long timestamp) {
		try {
			format = new SimpleDateFormat("dd-M-yyyy");
			date = new Date(timestamp);
			return format.format(date);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "";
	}

	static String formatDecimal(double value) {
		try {
			NumberFormat formatter = new DecimalFormat("#0.00");
			return formatter.format(new Double(value));
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "";
	}
	  

	public static void generateMultiParamReport(LabResultMasterDto labResultMasterDto,
	    SingParamTestResDto singParamTestResDto,HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		Document document = null;
		PdfWriter writer = null;
		FileOutputStream out = null;
		Paragraph para = null;
		PdfPTable table = null;
		Font font = null;
		PdfPCell cell = null;

		try {
		//	ObjectMapper mapper = new ObjectMapper();
		//	JsonNode jsonNode = mapper.readTree(input);

			float height = 11.02f  *72;
			float width = 8.54f  *72;
			float marginTop = 2.18f * 72;
			float marginBottom = 1.02f*  72;
			float marginLeft = 0.39f * 72;
			float marginRight = 0.39f * 72;
			Rectangle pagesize = new Rectangle(width, height);
			document = new Document(pagesize, marginLeft, marginRight, marginTop, marginBottom);
			writer = PdfWriter.getInstance(document, servletResponse.getOutputStream());
			document.open();
			
		    // String imgpath = "https://s3.ap-south-1.amazonaws.com/his-test/rubyhall1/rubyhallm2/unit-logo/sama.png";
			String imgpath = singParamTestResDto.getUnitLogo();
			if(imgpath!=null) {
		      Image img1 = Image.getInstance(imgpath);
		         img1.setAbsolutePosition(10, 630);
		          img1.scaleAbsolute(150, 130);
		          img1.setSpacingBefore(5);
		          document.add(img1);
			}
			/*
			  document = new Document(PageSize.A4); out = new
			  FileOutputStream("g:\\lisTemp.pdf"); document.setMargins(15, 15,
			  20, 20); writer = PdfWriter.getInstance(document, out);
			  document.open();
			 */
			Barcode128 barcode = new Barcode128();
			barcode.setCodeType(Barcode.CODE128);
			barcode.setCode( labResultMasterDto.getUhid());// UHID Number
			Image img = barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);
			// Set Image size //img.scaleAbsolute(80, 50);
			img.scaleAbsolute(90, 35);
			// Set barcode image position //img.setAbsolutePosition(spacing from
			// left, spacing from bottom);
			img.setAbsolutePosition(470, 590);
			document.add(img);

			LineSeparator lineSeparator = new LineSeparator();
			lineSeparator.setLineColor(BaseColor.BLACK);
			lineSeparator.setLineWidth(1);
			document.add(lineSeparator);

			table = new PdfPTable(4);
			table.setSpacingBefore(5);
			table.getDefaultCell().setBorder(0);
			table.setWidthPercentage(100);

			float colWidth2[] = { 10, 40, 10, 40 };
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidths(colWidth2);

			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Visit Date : ", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("" + formatDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId())), font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("UHID :", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("" + labResultMasterDto.getUhid(), font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Name : ", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("" + labResultMasterDto.getPatientDetails(), font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Sex / Age :", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase(""+labResultMasterDto.getPatientGender()+"/"+labResultMasterDto.getPatientAgeInYears().intValue()+"  ", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Ref.By. : ", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("" + labResultMasterDto.getDoctorDetails()==null?"-":labResultMasterDto.getDoctorDetails(), font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);

			document.add(table);

			table = new PdfPTable(2);
			table.setSpacingBefore(2);
			table.getDefaultCell().setBorder(0);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 45, 55 });

			para = new Paragraph();
			para.setFont(font);
			para.setSpacingAfter(5);
			para.setAlignment(Element.PTABLE);
			para.add(table);

			document.add(para);

			// document.add(Chunk.NEWLINE);

			lineSeparator = new LineSeparator();
			lineSeparator.setLineColor(BaseColor.BLACK);
			lineSeparator.setLineWidth(1);
			document.add(lineSeparator);

			font = FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK);
			para = new Paragraph();
			para.setFont(font);
			para.setAlignment(Element.ALIGN_CENTER);
			para.add(labResultMasterDto.getDeptName().toUpperCase());
			para.setSpacingAfter(10);
			document.add(para);

			// document.add(Chunk.NEWLINE);

			lineSeparator = new LineSeparator();
			lineSeparator.setLineColor(BaseColor.BLACK);
			lineSeparator.setLineWidth(1);
			document.add(lineSeparator);

			 table = new PdfPTable(7);
			 table.getDefaultCell().setBorder(0);
			 table.setWidthPercentage(100);
			 table.setWidths(new float[]{25,15,10,7,8,20,15});
				
			String headers[] = {"Investigation","Parameters","Result","Flag","Units","Reference Range","Technique Name"};
			font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
			for (String header : headers) {
				cell = new PdfPCell(new Phrase(header, font));
				cell.setBorder(Rectangle.BOTTOM);
				cell.setFixedHeight(20f);
				table.addCell(cell);
			}

			font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
			
			/*
			  Test
			 */

			cell = new PdfPCell(new Phrase(
					"" + singParamTestResDto.getTestDesc() + "(" + singParamTestResDto.getTestCode() + ")",
					font));
			cell.setBorder(0);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("", font));
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(
					"" + singParamTestResDto.getTechniqueName() ,
					font));
			cell.setBorder(0);
			table.addCell(cell);

			for (MultyParamTestHeaderDto parent : singParamTestResDto.getListMultyParamTestHeaderDto()) {

				
				 /* Header*/

				cell = new PdfPCell(new Phrase("" + parent.getHeaderDesc(), font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				for (LabResultDetailsViewDto child : parent.getListLabResultDetailsViewDto()) {
					/*
					  Parameters
					 */
					
					if(child.getResultTypeFlag().equals('H') ||child.getResultTypeFlag().equals('L')
							|| child.getResultTypeFlag().equals('C')	){
						
				    font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);		
					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase("" + child.getParamName(), font));
					cell.setBorder(0);
					table.addCell(cell);

					font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.RED);
					cell = new PdfPCell(new Phrase("" + child.getFinalResult(), font));
					cell.setBorder(0);
					table.addCell(cell);
					
					if(child.getResultTypeFlag().charValue()=='C')
					{
						font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
						cell = new PdfPCell(new Phrase("**" , font));
						cell.setBorder(0);
						table.addCell(cell);
					}
					else{
						font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
						cell = new PdfPCell(new Phrase("" +child.getResultTypeFlag(), font));
						cell.setBorder(0);
						table.addCell(cell);
					}
					

					cell = new PdfPCell(new Phrase("" + child.getParamUnit(), font));
					cell.setBorder(0);
					table.addCell(cell);

					if(child.getRefrangeTypeId()==REFERENCE_VALUE)
					{
					  cell = new PdfPCell(
                          new Phrase("" + child.getParameterMin() + "  -  " + child.getParameterMax(), font));
					}
					else if(child.getRefrangeTypeId()==TEXTUAL_RANGE)
					{
					  cell = new PdfPCell(
                          new Phrase("" + child.getTextualRangeName(), font));
					}
					else if(child.getRefrangeTypeId()==MULTIVALUED_RANGE)
					{
					  cell = new PdfPCell(
                          new Phrase("" + child.getMultitextaulRange(), font));
					}
				
					
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);
					}else{
						font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
						cell = new PdfPCell(new Phrase("", font));
						cell.setBorder(0);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase("" + child.getParamName(), font));
						cell.setBorder(0);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("" + formatDecimal(child.getFinalResult()), font));
						cell.setBorder(0);
						table.addCell(cell);
						// if con
						cell = new PdfPCell(new Phrase("" , font));
						cell.setBorder(0);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("" + child.getParamUnit(), font));
						cell.setBorder(0);
						table.addCell(cell);

						
						if(child.getRefrangeTypeId()==REFERENCE_VALUE)
	                    {
	                      cell = new PdfPCell(
	                          new Phrase("" + child.getParameterMin() + "  -  " + child.getParameterMax(), font));
	                    }
	                    else if(child.getRefrangeTypeId()==TEXTUAL_RANGE)
	                    {
	                      cell = new PdfPCell(
	                          new Phrase("" + child.getTextualRangeName(), font));
	                    }
	                    else if(child.getRefrangeTypeId()==MULTIVALUED_RANGE)
	                    {
	                      cell = new PdfPCell(
	                          new Phrase("" + child.getMultitextaulRange(), font));
	                    }
						
						cell.setBorder(0);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase("", font));
						cell.setBorder(0);
						table.addCell(cell);
						
					}
				}
			}

			document.add(table);

			// document.add(Chunk.NEWLINE);
			para = new Paragraph();
			para.setSpacingBefore(35);
			document.add(para);

			table = new PdfPTable(2);
			table.getDefaultCell().setBorder(0);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 17, 83 });

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Sample Collection :", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase(" "+formatDate(singParamTestResDto.getSampleCollectionTime()), font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Test Time :", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase(" "+formatDate(singParamTestResDto.getSampleTestTime()), font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Test Release :", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase(" "+formatDate(singParamTestResDto.getTestReleaseTime()), font));
			cell.setBorder(0);
			table.addCell(cell);

			document.add(table);

			para = new Paragraph();
			para.setSpacingBefore(5);
			document.add(para);

			lineSeparator = new LineSeparator();
			lineSeparator.setLineColor(BaseColor.BLACK);
			lineSeparator.setLineWidth(1);
			document.add(lineSeparator);

			table = new PdfPTable(2);
			table.getDefaultCell().setBorder(0);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 10, 90 });

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase("Flag Key:", font));
			cell.setBorder(0);
			table.addCell(cell);

			font = FontFactory.getFont("Arial", 10, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase(
					"L= Abnormal Low,  H= Abnormal High,  **= critical value",
					font));
			cell.setBorder(0);
			table.addCell(cell);

			document.add(table);

			document.close();
			servletResponse.getOutputStream().close();
			logger.info("Report Created");
			
		} catch (Exception e) {
			logger.error("IOException", e);
		} 
	}
	public static ByteArrayOutputStream generateMultiParamReportToMail(LabResultMasterDto labResultMasterDto,
		    SingParamTestResDto singParamTestResDto) {
			Document document = null;
			PdfWriter writer = null;
			FileOutputStream out = null;
			Paragraph para = null;
			PdfPTable table = null;
			Font font = null;
			PdfPCell cell = null;

			
			ByteArrayOutputStream byateArrayout = new ByteArrayOutputStream();
			try {
			//	ObjectMapper mapper = new ObjectMapper();
			//	JsonNode jsonNode = mapper.readTree(input);
				//File file = new File(contextPath + "/reports");
				//file.mkdirs();

				float height = 11.02f  *72;
				float width = 8.54f  *72;
				float marginTop = 2.18f * 72;
				float marginBottom = 1.02f*  72;
				float marginLeft = 0.39f * 72;
				float marginRight = 0.39f * 72;
				Rectangle pagesize = new Rectangle(width, height);
				document = new Document(pagesize, marginLeft, marginRight, marginTop, marginBottom);
				//OutputStream outputStream = new FileOutputStream(contextPath + "/reports/"+TimeZoneComponent.getDateByzone(unitId).getTime());
				writer = PdfWriter.getInstance(document, byateArrayout);
				document.open();
				
			     //String imgpath = "https://s3.ap-south-1.amazonaws.com/his-test/rubyhall1/rubyhallm2/unit-logo/sama.png";
				String imgpath = singParamTestResDto.getUnitLogo();
				if(imgpath!=null) {
			      Image img1 = Image.getInstance(imgpath);
			         img1.setAbsolutePosition(10, 630);
			          img1.scaleAbsolute(150, 130);
			          img1.setSpacingBefore(5);
			          document.add(img1);
				}
				/*
				  document = new Document(PageSize.A4); out = new
				  FileOutputStream("g:\\lisTemp.pdf"); document.setMargins(15, 15,
				  20, 20); writer = PdfWriter.getInstance(document, out);
				  document.open();
				 */
				Barcode128 barcode = new Barcode128();
				barcode.setCodeType(Barcode.CODE128);
				barcode.setCode( labResultMasterDto.getUhid());// UHID Number
				Image img = barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);
				// Set Image size //img.scaleAbsolute(80, 50);
				img.scaleAbsolute(90, 35);
				// Set barcode image position //img.setAbsolutePosition(spacing from
				// left, spacing from bottom);
				img.setAbsolutePosition(470, 590);
				document.add(img);

				LineSeparator lineSeparator = new LineSeparator();
				lineSeparator.setLineColor(BaseColor.BLACK);
				lineSeparator.setLineWidth(1);
				document.add(lineSeparator);

				table = new PdfPTable(4);
				table.setSpacingBefore(5);
				table.getDefaultCell().setBorder(0);
				table.setWidthPercentage(100);

				float colWidth2[] = { 10, 40, 10, 40 };
				table.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.setWidths(colWidth2);

				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Visit Date : ", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("" + formatDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId())), font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("UHID :", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("" + labResultMasterDto.getUhid(), font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Name : ", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("" + labResultMasterDto.getPatientDetails(), font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Sex / Age :", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase(""+labResultMasterDto.getPatientGender()+"/"+labResultMasterDto.getPatientAgeInYears().intValue()+"  ", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Ref.By. : ", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("" + labResultMasterDto.getDoctorDetails()==null?"-":labResultMasterDto.getDoctorDetails(), font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				document.add(table);

				table = new PdfPTable(2);
				table.setSpacingBefore(2);
				table.getDefaultCell().setBorder(0);
				table.setWidthPercentage(100);
				table.setWidths(new float[] { 45, 55 });

				para = new Paragraph();
				para.setFont(font);
				para.setSpacingAfter(5);
				para.setAlignment(Element.PTABLE);
				para.add(table);

				document.add(para);

				// document.add(Chunk.NEWLINE);

				lineSeparator = new LineSeparator();
				lineSeparator.setLineColor(BaseColor.BLACK);
				lineSeparator.setLineWidth(1);
				document.add(lineSeparator);

				font = FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK);
				para = new Paragraph();
				para.setFont(font);
				para.setAlignment(Element.ALIGN_CENTER);
				para.add(labResultMasterDto.getDeptName().toUpperCase());
				para.setSpacingAfter(10);
				document.add(para);

				// document.add(Chunk.NEWLINE);

				lineSeparator = new LineSeparator();
				lineSeparator.setLineColor(BaseColor.BLACK);
				lineSeparator.setLineWidth(1);
				document.add(lineSeparator);

				 table = new PdfPTable(7);
				 table.getDefaultCell().setBorder(0);
				 table.setWidthPercentage(100);
				 table.setWidths(new float[]{25,15,10,7,8,20,15});
					
				String headers[] = {"Investigation","Parameters","Result","Flag","Units","Reference Range","Technique Name"};
				font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
				for (String header : headers) {
					cell = new PdfPCell(new Phrase(header, font));
					cell.setBorder(Rectangle.BOTTOM);
					cell.setFixedHeight(20f);
					table.addCell(cell);
				}

				font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
				
				/*
				  Test
				 */

				cell = new PdfPCell(new Phrase(
						"" + singParamTestResDto.getTestDesc() + "(" + singParamTestResDto.getTestCode() + ")",
						font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("", font));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(
						"" + singParamTestResDto.getTechniqueName() ,
						font));
				cell.setBorder(0);
				table.addCell(cell);

				for (MultyParamTestHeaderDto parent : singParamTestResDto.getListMultyParamTestHeaderDto()) {

					
					 /* Header*/

					cell = new PdfPCell(new Phrase("" + parent.getHeaderDesc(), font));
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase("", font));
					cell.setBorder(0);
					table.addCell(cell);

					for (LabResultDetailsViewDto child : parent.getListLabResultDetailsViewDto()) {
						/*
						  Parameters
						 */
						
						if(child.getResultTypeFlag().equals('H') ||child.getResultTypeFlag().equals('L')
								|| child.getResultTypeFlag().equals('C')	){
							
					    font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);		
						cell = new PdfPCell(new Phrase("", font));
						cell.setBorder(0);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase("" + child.getParamName(), font));
						cell.setBorder(0);
						table.addCell(cell);

						font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.RED);
						cell = new PdfPCell(new Phrase("" + child.getFinalResult(), font));
						cell.setBorder(0);
						table.addCell(cell);
						
						if(child.getResultTypeFlag().charValue()=='C')
						{
							font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
							cell = new PdfPCell(new Phrase("**" , font));
							cell.setBorder(0);
							table.addCell(cell);
						}
						else{
							font = FontFactory.getFont("Arial", 10, Font.BOLD, BaseColor.BLACK);
							cell = new PdfPCell(new Phrase("" +child.getResultTypeFlag(), font));
							cell.setBorder(0);
							table.addCell(cell);
						}
						

						cell = new PdfPCell(new Phrase("" + child.getParamUnit(), font));
						cell.setBorder(0);
						table.addCell(cell);

						if(child.getRefrangeTypeId()==REFERENCE_VALUE)
						{
						  cell = new PdfPCell(
	                          new Phrase("" + child.getParameterMin() + "  -  " + child.getParameterMax(), font));
						}
						else if(child.getRefrangeTypeId()==TEXTUAL_RANGE)
						{
						  cell = new PdfPCell(
	                          new Phrase("" + child.getTextualRangeName(), font));
						}
						else if(child.getRefrangeTypeId()==MULTIVALUED_RANGE)
						{
						  cell = new PdfPCell(
	                          new Phrase("" + child.getMultitextaulRange(), font));
						}
					
						
						cell.setBorder(0);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase("", font));
						cell.setBorder(0);
						table.addCell(cell);
						}else{
							font = FontFactory.getFont("Arial", 10, Font.NORMAL, BaseColor.BLACK);
							cell = new PdfPCell(new Phrase("", font));
							cell.setBorder(0);
							table.addCell(cell);
							
							cell = new PdfPCell(new Phrase("" + child.getParamName(), font));
							cell.setBorder(0);
							table.addCell(cell);

							cell = new PdfPCell(new Phrase("" + formatDecimal(child.getFinalResult()), font));
							cell.setBorder(0);
							table.addCell(cell);
							// if con
							cell = new PdfPCell(new Phrase("" , font));
							cell.setBorder(0);
							table.addCell(cell);

							cell = new PdfPCell(new Phrase("" + child.getParamUnit(), font));
							cell.setBorder(0);
							table.addCell(cell);

							
							if(child.getRefrangeTypeId()==REFERENCE_VALUE)
		                    {
		                      cell = new PdfPCell(
		                          new Phrase("" + child.getParameterMin() + "  -  " + child.getParameterMax(), font));
		                    }
		                    else if(child.getRefrangeTypeId()==TEXTUAL_RANGE)
		                    {
		                      cell = new PdfPCell(
		                          new Phrase("" + child.getTextualRangeName(), font));
		                    }
		                    else if(child.getRefrangeTypeId()==MULTIVALUED_RANGE)
		                    {
		                      cell = new PdfPCell(
		                          new Phrase("" + child.getMultitextaulRange(), font));
		                    }
							
							cell.setBorder(0);
							table.addCell(cell);
							
							cell = new PdfPCell(new Phrase("", font));
							cell.setBorder(0);
							table.addCell(cell);
							
						}
					}
				}

				document.add(table);

				// document.add(Chunk.NEWLINE);
				para = new Paragraph();
				para.setSpacingBefore(35);
				document.add(para);

				table = new PdfPTable(2);
				table.getDefaultCell().setBorder(0);
				table.setWidthPercentage(100);
				table.setWidths(new float[] { 17, 83 });

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Sample Collection :", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase(" "+formatDate(singParamTestResDto.getSampleCollectionTime()), font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Test Time :", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase(" "+formatDate(singParamTestResDto.getSampleTestTime()), font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Test Release :", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase(" "+formatDate(singParamTestResDto.getTestReleaseTime()), font));
				cell.setBorder(0);
				table.addCell(cell);

				document.add(table);

				para = new Paragraph();
				para.setSpacingBefore(5);
				document.add(para);

				lineSeparator = new LineSeparator();
				lineSeparator.setLineColor(BaseColor.BLACK);
				lineSeparator.setLineWidth(1);
				document.add(lineSeparator);

				table = new PdfPTable(2);
				table.getDefaultCell().setBorder(0);
				table.setWidthPercentage(100);
				table.setWidths(new float[] { 10, 90 });

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("Flag Key:", font));
				cell.setBorder(0);
				table.addCell(cell);

				font = FontFactory.getFont("Arial", 10, Font.NORMAL,
						BaseColor.BLACK);
				cell = new PdfPCell(new Phrase(
						"L= Abnormal Low,  H= Abnormal High,  **= critical value",
						font));
				cell.setBorder(0);
				table.addCell(cell);

				document.add(table);

				document.close();
				logger.info("Report Created");

				return byateArrayout;
				
			} catch (Exception e) {
				logger.error("IOException", e);
				return null;
			} 
		}
	

}
