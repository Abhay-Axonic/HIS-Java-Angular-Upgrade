/*
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.PageSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.poi.ss.usermodel.Footer;

public class pdfHTMLHeaderAndFooterExample {

    public static final String sourceFolder = "src/test/resources/pdfHTML/";
    public static final String destinationFolder = "target/output/pdfHTML/";
    public static final String LICENSE = "src/test/resources/itextkey_trial.xml";

    public static final String[] files = {"ipsum"};

    public static void main(String[] args) throws IOException, InterruptedException {
       // LicenseKey.loadLicenseFile(LICENSE);
        for (String name : files) {
            String htmlSource = "D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/"+ "report.html";
            String resourceFolder = sourceFolder + name + "/";
            String pdfDest ="D:/HIS Sharad1/HIS_WS Sharad/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/webapps/SharadDocument/CHT/RISReport/report.pdf";
            File file = new File(pdfDest);

            System.out.println("Parsing: " + htmlSource);
            file.getParentFile().mkdirs();
            new pdfHTMLHeaderAndFooterExample().parseWithHeaderAndFooter(htmlSource, pdfDest, resourceFolder);

        }
    }

    public void parseWithHeaderAndFooter(String htmlSource, String pdfDest, String resoureLoc) throws IOException, InterruptedException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();

        //Create Document
        PdfWriter writer= new PdfWriter(pdfDest);
        PdfDocument pdfDocument = new PdfDocument(writer);
        //Create event-handlers
        String header = "";
        String footer = "Clinivantage footer ";
        Header headerHandler = new Header(header);
        PageXofY footerHandler = new PageXofY(pdfDocument);
        
        //Assign event-handlers
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,headerHandler);
        
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);

        //Convert
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);
        HtmlConverter.convertToDocument(new FileInputStream(htmlSource), pdfDocument, converterProperties);
        //Write the total number of pages to the placeholder
        footerHandler.writeTotal(pdfDocument);
        
        pdfDocument.close();
        System.out.println("completed");


    }

    //Header event handler
    protected class Header implements IEventHandler {
        String header;
        public Header(String header) {
            this.header = header;
        }
        @Override
        public void handleEvent(Event event) {
            //Retrieve document and
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize).setStrokeWidth(20);
            canvas.setFontSize(18f);
            String imageFile = "C:/header.png"; 
            ImageData data = null;
			try {
				data = ImageDataFactory.create(imageFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				
			}

            // Creating an Image object 
            Image img = new Image(data);
        //    img.setFixedPosition(100, 250);
            img.setWidth(595);
            img.setHeight(50);
            
            canvas.add(img);
            
             
            //Write text at position
            canvas.showTextAligned(header,
                    pageSize.getWidth() / 2,
                    pageSize.getTop() - 30, TextAlignment.CENTER);
        }
    }

    //page X of Y 
    protected class PageXofY implements IEventHandler {
    	 String footer;
         public PageXofY(String footer) {
             this.footer = footer;
         }
        protected PdfFormXObject placeholder;
        protected float side = 20;
        protected float x = 300;
        protected float y = 20;
        protected float space = 4.5f;
        protected float descent = 10;
        public PageXofY(PdfDocument pdf) {
        	 Rectangle rectangle = new Rectangle(30, 30, 550, 800);
            placeholder =
                    new PdfFormXObject(new Rectangle(0, 0, 200, 800));
        }
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdf.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
            Paragraph p = new Paragraph().add("Clinivantage footer ");
            
            String imageFile = "C:/header.png"; 
            ImageData data = null;
			try {
				data = ImageDataFactory.create(imageFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				
			}

            // Creating an Image object 
            Image img = new Image(data);
            img.setWidth(595);
            img.setHeight(50);
            img.setFixedPosition(0, 0);
            canvas.add(img);
             
            //Write text at position
            canvas.setUnderline();
            canvas.showTextAligned("",
                    pageSize.getWidth() / 2,
                    pageSize.getBottom(), TextAlignment.CENTER);
          //  canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
            //canvas.setFontSize(10f);
            
           // pdfCanvas.addXObject(placeholder, x + space, y - descent);
           // pdfCanvas.release();
        }
        public void writeTotal(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
          //  String imageFile = "https://previews.123rf.com/images/krisdog/krisdog1601/krisdog160100054/50483319-easter-scene-footer-design-basket-full-of-decorated-chocolate-easter-eggs-and-flowers-in-a-field.jpg"; 
            String imageFile = "C:/header.png"; 
            ImageData data = null;
			try {
				data = ImageDataFactory.create(imageFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				
			}

            // Creating an Image object 
            Image img = new Image(data); 
            //img.setFixedPosition(0, 0); 
           // img.seta
            img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
           // canvas.add(img);
            canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()),1000, descent, TextAlignment.CENTER);
        }
    }



}
*/