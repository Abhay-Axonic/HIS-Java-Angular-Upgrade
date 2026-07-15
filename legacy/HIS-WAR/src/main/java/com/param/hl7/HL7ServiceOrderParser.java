package com.param.hl7;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v231.message.ORM_O01;
import ca.uhn.hl7v2.parser.PipeParser;
import com.param.lis.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7ServiceOrderParser implements ICommonConstants {

    private static final Logger logger = LoggerFactory.getLogger(HL7ServiceOrderParser.class);

    public static void main(String[] args) {
        try {
            // Initialize HAPI HL7 Parser
            PipeParser parser = new PipeParser();
            ORM_O01 message = (ORM_O01) parser.parse(ICommonConstants.hl7Message);
            // Extract & Process Data
            String patientId = message.getPIDPD1NTEPV1PV2IN1IN2IN3GT1AL1().getPID().getPatientID().getID().getValue();
            String patientName = message.getPIDPD1NTEPV1PV2IN1IN2IN3GT1AL1().getPID().getPatientName(0).getGivenName().getValue() + " " +
                    message.getPIDPD1NTEPV1PV2IN1IN2IN3GT1AL1().getPID().getPatientName(0).getFamilyLastName().getFamilyName().getValue();
            //String orderId = message.getORDER().getORC().getPlacerOrderNumber().getEntityIdentifier().getValue();
            //String testName = message.getORCOBRRQDRQ1ODSODTRXONTEDG1RXRRXCNTEOBXNTECTIBLG().getOBRRQDRQ1ODSODTRXONTEDG1RXRRXCNTEOBXNTE().getOBR().getObr4_UniversalServiceID().getName();
            String testName =  "CBC(COMPLETE BLOOD COUNT)";
            String visitNumber = "CBC(COMPLETE BLOOD COUNT)";

            System.out.println("Processing Order...");
            System.out.println("Patient ID: " + patientId);
            System.out.println("Patient Name: " + patientName);
           // System.out.println("Order ID: " + orderId);
            System.out.println("Test Ordered: " + testName);
            System.out.println("Visit Number: " + visitNumber);



        } catch (HL7Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }
}
