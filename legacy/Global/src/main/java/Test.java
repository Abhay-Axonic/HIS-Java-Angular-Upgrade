/*
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;

public class Test {
	public static final String HTML = "<h1>Hello</h1>" + "<p>This was created using iText</p>"
			+ "<a href='hmkcode.com'>hmkcode.com</a>";

	public static void main(String[] args) throws Exception {
		
		 * Document document = new Document(PageSize.A4); PdfWriter writer =
		 * PdfWriter.getInstance(document, new
		 * FileOutputStream("D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/report.pdf"
		 * )); document.open(); XMLWorkerHelper.getInstance().parseXHtml(writer,
		 * document, new
		 * FileInputStream("D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/"
		 * + "report.html")); document.close();
		 

		HtmlConverter.convertToPdf(HTML, new FileOutputStream(
				"D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/report.pdf"));

		PdfPTable header = new PdfPTable(2); 
            // set defaults
            header.setWidths(new int[]{2, 24});
            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(40);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
		
            Image logo = Image.getInstance("https://i.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");
            header.addCell(logo);

            
            
		ConverterProperties properties = new ConverterProperties();
		MediaDeviceDescription description = MediaDeviceDescription.createDefault();
		description.setHeight(1024);
		description.setWidth(1024);
		 
		properties.setMediaDeviceDescription(description);
	
		HtmlConverter.convertToPdf(new FileInputStream("D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/"+ "report.html"), 
	            new FileOutputStream("D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/report.pdf"));
		System.out.println("PDF Created!");
	}

}
*/