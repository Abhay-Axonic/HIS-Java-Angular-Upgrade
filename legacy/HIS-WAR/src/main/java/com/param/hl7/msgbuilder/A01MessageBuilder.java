package com.param.hl7.msgbuilder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.message.*;
import ca.uhn.hl7v2.model.v231.segment.*;
import ca.uhn.hl7v2.parser.Parser;
import com.param.hl7.dto.PidDto;
import com.param.global.common.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class A01MessageBuilder {

    private static final Logger logger = LoggerFactory.getLogger(A01MessageBuilder.class);

    private static PidDto pidDto;
    public A01MessageBuilder(PidDto pidDto) {
        this.pidDto = pidDto;
    }

    private static String replaceNewlines(String input) {
        return input.replaceAll("\\r", "\n");
    }

    public static String buildPatientRegistration() throws HL7Exception, IOException {
        ADT_A01 adtMessage = new ADT_A01();
        adtMessage.initQuickstart("ADT", "A01", "P");

        createMshSegment(adtMessage.getMSH(), "A01");
        createEvnSegment(adtMessage.getEVN(), "A01");
        createPidSegment(adtMessage.getPID());

        return encodeMessage(adtMessage);
    }

    public static String buildPatientUpdate() throws HL7Exception, IOException {
        ADT_A08 adtMessage = new ADT_A08();
        adtMessage.initQuickstart("ADT", "A08", "P");

        createMshSegment(adtMessage.getMSH(), "A08");
        createEvnSegment(adtMessage.getEVN(), "A08");
        createPidSegment(adtMessage.getPID());

        return encodeMessage(adtMessage);
    }

/*    public static String buildAppointmentScheduling() throws HL7Exception, IOException {
        SIU_S12 siuMessage = new SIU_S12();
        siuMessage.initQuickstart("SIU", "S12", "P");
        createMshSegment(siuMessage.getMSH(), "S12");
        createSchSegment(siuMessage.getSCH());

        return encodeMessage(siuMessage);
    }*/

    public static String buildAppointmentScheduling() throws HL7Exception, IOException {
        SIU_S12 siuMessage = new SIU_S12();
        siuMessage.initQuickstart("SIU", "S12", "P");

        createMshSegment(siuMessage.getMSH(), "S12");
        createSchSegment(siuMessage.getSCH() );
       // createPidSegment(siuMessage.getPIDPD1PV1PV2OBXDG1().getPID());
        createPidSegmentForApointment(siuMessage.getPIDPD1PV1PV2OBXDG1().getPID());
        createPv1Segment(siuMessage.getPIDPD1PV1PV2OBXDG1().getPV1());

        return encodeMessage(siuMessage);
    }

    private static void createPidSegmentForApointment(PID pid) throws DataTypeException {
        //patientId
        pid.getPatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid18_PatientAccountNumber().getID().setValue(pidDto.getOrganizationName());
        pid.getPid19_SSNNumberPatient().setValue(pidDto.getUnitName());

    }


    private static void createPv1Segment(PV1 pv1) throws HL7Exception {
        pv1.getPatientClass().setValue("O"); // Outpatient
        pv1.getAssignedPatientLocation().getPointOfCare().setValue(String.valueOf(2));
        pv1.getAttendingDoctor(0).getIDNumber().setValue(String.valueOf(3));
        pv1.getVisitNumber().getID().setValue(String.valueOf("OP"));
    }



    public static String buildAppointmentRescheduling() throws HL7Exception, IOException {
        SIU_S12 siuMessage = new SIU_S12();
        siuMessage.initQuickstart("SIU", "S13", "P");

        createMshSegment(siuMessage.getMSH(), "S13");
        createSchSegment(siuMessage.getSCH());

        return encodeMessage(siuMessage);
    }

    public static void createMshSegment(MSH msh, String eventType) throws DataTypeException {
        msh.getFieldSeparator().setValue("|");
        msh.getEncodingCharacters().setValue("^~\\&");
        msh.getSendingApplication().getNamespaceID().setValue("HMIS ACE");
        msh.getSendingFacility().getNamespaceID().setValue("Axonic");
        msh.getReceivingApplication().getNamespaceID().setValue("PMS");
        msh.getReceivingFacility().getNamespaceID().setValue("PMS");
        msh.getSendingFacility().getUniversalID().setValue(String.valueOf(pidDto.getHospitalId()));
        msh.getSendingFacility().getUniversalIDType().setValue(String.valueOf(pidDto.getUnitId()));
        // In v2.3.1, the date-time field is "TimeOfMessage" instead of "TimeOfAnEvent"
        msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(getCurrentTimeStamp());

        msh.getMessageControlID().setValue(getSequenceNumber());

        // Set Message Type
        msh.getMessageType().getMessageType().setValue(eventType.startsWith("A") ? "ADT" : "SIU");
        msh.getMessageType().getTriggerEvent().setValue(eventType);

        // In v2.3.1, Version ID is accessed differently
        msh.getVersionID().getVid1_VersionID().setValue("2.3.1");

        msh.getAcceptAcknowledgmentType().setValue("AL");
        msh.getApplicationAcknowledgmentType().setValue("NE");
        msh.getCountryCode().setValue("IND");
    }

    private static void createEvnSegment(EVN evn, String eventType) throws DataTypeException {
        evn.getEventTypeCode().setValue(eventType);
        evn.getRecordedDateTime().getTimeOfAnEvent().setValue(getCurrentTimeStamp());
    }

    private static void createPidSegment(PID pid) throws DataTypeException {
        //patientId
        pid.getPatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid2_PatientID().getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid3_PatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid4_AlternatePatientIDPID(0).getID().setValue(String.valueOf(pidDto.getPatientIdentifire()));//uhid
        pid.getPid5_PatientName(0).getFamilyLastName().getFamilyName().setValue(pidDto.getFamilyName());
        pid.getPid5_PatientName(0).getGivenName().setValue(pidDto.getGivenName());
        pid.getPid5_PatientName(0).getPrefixEgDR().setValue(pidDto.getPrefixDesc());
        pid.getPid7_DateTimeOfBirth().getTimeOfAnEvent().setValue(pidDto.getDob());
        pid.getPid8_Sex().setValue(pidDto.getSex());
        pid.getPid11_PatientAddress(0).getOtherDesignation().setValue(pidDto.getAddress());
        pid.getPid11_PatientAddress(0).getCity().setValue(pidDto.getCityName());
        pid.getPid11_PatientAddress(0).getStateOrProvince().setValue(pidDto.getStateName());
        pid.getPid11_PatientAddress(0).getZipOrPostalCode().setValue(pidDto.getZipCode());
        pid.getPid11_PatientAddress(0).getCountry().setValue(pidDto.getCountryName());
        pid.getPid12_CountyCode().setValue(pidDto.getCountryCode());
        pid.getPid13_PhoneNumberHome(0).getPhoneNumber().setValue(pidDto.getMobileNumber());
        pid.getPid24_MultipleBirthIndicator().setValue(pidDto.getMultipleBirthIndicator());
        pid.getPid13_PhoneNumberHome(0).getEmailAddress().setValue(pidDto.getEmailId());
        pid.getPid18_PatientAccountNumber().getID().setValue(pidDto.getOrganizationName());
        pid.getPid19_SSNNumberPatient().setValue(pidDto.getUnitName());
    }

    public static String buildAppointmentCancellation() throws HL7Exception, IOException {
        SIU_S12 siuMessage = new SIU_S12();
        siuMessage.initQuickstart("SIU", "S15", "P");

        createMshSegment(siuMessage.getMSH(), "S15");
        createSchSegmentForCan(siuMessage.getSCH());

        return encodeMessage(siuMessage);
    }

    private static void createSchSegmentForCan(SCH sch) throws DataTypeException {
        sch.getPlacerAppointmentID().getEntityIdentifier().setValue(String.valueOf(pidDto.getAppointmentId()));
        sch.getParentFillerAppointmentID().getEntityIdentifier().setValue(String.valueOf(pidDto.getPatientId()));
        sch.getAppointmentReason().getText().setValue(pidDto.getReasonDesc());
    }


    private static void createSchSegment(SCH sch) throws DataTypeException {
        sch.getPlacerAppointmentID().getEntityIdentifier().setValue(String.valueOf(pidDto.getAppointmentId()));
        sch.getFillerAppointmentID().getEntityIdentifier().setValue(String.valueOf(pidDto.getAppointmentId()));
        sch.getParentFillerAppointmentID().getEntityIdentifier().setValue(String.valueOf(pidDto.getPatientId()));
        sch.getAppointmentReason().getText().setValue(pidDto.getReasonDesc());
        sch.getEventReason().getText().setValue(pidDto.getSpecialityName());
        sch.getSch8_AppointmentType();
        sch.getPlacerContactPhoneNumber().getPhoneNumber().setValue(pidDto.getMobileNumber());
        sch.getPlacerContactPhoneNumber().getEmailAddress().setValue(pidDto.getEmailId());
        sch.getFillerContactAddress(0).getXad1_StreetAddress().setValue(pidDto.getGivenName() +""+pidDto.getFamilyName());
        sch.getAppointmentDuration().setValue("10");


        String inputDate = pidDto.getAppointmentDate()+ " "+pidDto.getFromTimeString();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        DateTimeFormatter hl7Formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
        String hl7FormattedDate = dateTime.format(hl7Formatter);

        sch.getAppointmentTimingQuantity(0).getStartDateTime().getTimeOfAnEvent().setValue(hl7FormattedDate);
        sch.getPlacerContactPerson(0).getGivenName().setValue(pidDto.getDoctorName());
        sch.getEnteredByPerson(0).getGivenName().setValue(pidDto.getUserName());
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private static String getSequenceNumber() {
        return "1234" + getCurrentTimeStamp();
    }

    private static String encodeMessage(Object hl7Message) throws HL7Exception, IOException {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        return replaceNewlines(parser.encode((Message) hl7Message));
    }

    public static void main(String[] args) {
        try {
            System.out.println("Patient Registration (ADT A01):\n" + buildPatientRegistration());
            System.out.println("\nPatient Update (ADT A08):\n" + buildPatientUpdate());
            System.out.println("\nAppointment Scheduling (SIU S12):\n" + buildAppointmentScheduling());
            System.out.println("\nAppointment Rescheduling (SIU S13):\n" + buildAppointmentRescheduling());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }
}