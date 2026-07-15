package com.param.global.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pdfcrowd.Pdfcrowd;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@SessionAttributes("sessionObject")
public class GenaratePDFFromHtml {

	private static final Logger logger = Logger.getLogger(GenaratePDFFromHtml.class.getName());

	public ResponseEntity<byte[]> convert(String template, String userId, String password)  {
        try {
            // create the API client instance
            Pdfcrowd.HtmlToPdfClient client =
                new Pdfcrowd.HtmlToPdfClient(userId, password);//"demo", "ce544b6ea52a5621fb9d55f8b542d14d"

            // run the conversion and store the result into the "pdf" variable
            byte[] pdf = client.convertString(template);

            // set HTTP response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Cache-Control", "max-age=0");
            headers.add("Accept-Ranges", "none");
            headers.add("Content-Disposition", "attachment; filename=\"RadiologyReport.pdf\"");

            // send the result in the HTTP response
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        }
        catch(Pdfcrowd.Error why) {
            logger.log(Level.SEVERE, "convert failed", why);
            // send the error in the HTTP response
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/plain");
            String msg = String.format("Pdfcrowd Error: %d - %s",
                                       why.getCode(), why.getMessage());
            return new ResponseEntity<>(msg.getBytes(), headers, HttpStatus.BAD_REQUEST);
        }
    }
}
