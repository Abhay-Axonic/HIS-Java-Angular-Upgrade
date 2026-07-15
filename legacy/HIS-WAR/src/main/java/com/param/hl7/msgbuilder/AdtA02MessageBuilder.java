package com.param.hl7.msgbuilder;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.ADT_A02;
import ca.uhn.hl7v2.model.v24.segment.*;
import ca.uhn.hl7v2.parser.Parser;
import com.param.hl7.dto.PidDto;
import com.param.hl7.dto.Pv1_2Dto;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdtA02MessageBuilder {
    private static ADT_A02 _adtMessage;
    private static String replaceNewlines(String input) {
        return input.replaceAll("\\r", "\n");
    }
    private PidDto pidDto;
    private Pv1_2Dto pv1_2Dto;
    public AdtA02MessageBuilder(PidDto pidDto,Pv1_2Dto pv1_2Dto) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
    }

    public String getER7EncodedMessage() throws HL7Exception, IOException {
        ADT_A02 adt = this.build();
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(adt);
        return replaceNewlines(encodedMessage);
    }

    public ADT_A02 build() throws HL7Exception, IOException {
        String currentDateTimeString = getCurrentTimeStamp();
        _adtMessage = new ADT_A02();
        _adtMessage.initQuickstart("ADT", "A02", "P");
        createMshSegment(currentDateTimeString);
        createEvnSegment(currentDateTimeString);
        createPidSegment();
        createPv1Segment();
        createPv2Segment();

        return _adtMessage;
    }



    private MSH createMshSegment(String currentDateTimeString) throws DataTypeException {
        MSH mshSegment = _adtMessage.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getSendingApplication().getNamespaceID().setValue("HMIS ACE");
        mshSegment.getSendingFacility().getNamespaceID().setValue("Clinivantage");
        mshSegment.getReceivingApplication().getNamespaceID().setValue("Centricity");
        mshSegment.getReceivingFacility().getNamespaceID().setValue("MRRCH");
        mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue(currentDateTimeString);
        mshSegment.getMessageControlID().setValue(getSequenceNumber());
        mshSegment.getVersionID().getVersionID().setValue("2.5");
        mshSegment.getAcceptAcknowledgmentType().setValue("AL");
        mshSegment.getApplicationAcknowledgmentType().setValue("NE");
        mshSegment.getCountryCode().setValue("IND");
        return mshSegment;

    }

    private EVN createEvnSegment(String currentDateTimeString) throws DataTypeException {
        EVN evn = _adtMessage.getEVN();
        evn.getEventTypeCode().setValue("A02");
        evn.getRecordedDateTime().getTimeOfAnEvent().setValue(currentDateTimeString);
        return evn;
    }

    private PID createPidSegment() throws DataTypeException {
        PID pid = _adtMessage.getPID();
        //patientId
        //pid.getPatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid2_PatientID().getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid3_PatientIdentifierList(0).getID().setValue(pidDto.getPatientIdentifire());
        pid.getPid4_AlternatePatientIDPID(0).getID().setValue(pidDto.getPatientIdentifire());//uhid

        pid.getPid5_PatientName(0).getFamilyName().getSurname().setValue(pidDto.getFamilyName());
        pid.getPid5_PatientName(0).getGivenName().setValue(pidDto.getGivenName());

        pid.getPid7_DateTimeOfBirth().getTimeOfAnEvent().setValue(pidDto.getDob());
        pid.getPid8_AdministrativeSex().setValue(pidDto.getSex());

        pid.getPid11_PatientAddress(0).getOtherDesignation().setValue("114038");
        pid.getPid11_PatientAddress(0).getCity().setValue(pidDto.getCityName());
        pid.getPid11_PatientAddress(0).getZipOrPostalCode().setValue(pidDto.getZipCode());
        pid.getPid11_PatientAddress(0).getCountry().setValue(pidDto.getCountryName());
        pid.getPid13_PhoneNumberHome(0).getPhoneNumber().setValue(pidDto.getMobileNumber());
        pid.getPid12_CountyCode().setValue(pidDto.getCountryCode());
        pid.getPid24_MultipleBirthIndicator().setValue(pidDto.getMultipleBirthIndicator());

        return pid;
    }





    private  PV1 createPv1Segment() throws DataTypeException {
        PV1 pv1 = _adtMessage.getPV1();
        pv1.getPv12_PatientClass().setValue(pv1_2Dto.getPatientType()); // to represent an 'Outpatient'
        pv1.getPv13_AssignedPatientLocation().getRoom().setValue(pv1_2Dto.getRoomNo());
        pv1.getPv13_AssignedPatientLocation().getBed().setValue(pv1_2Dto.getBedNo());
        pv1.getPv14_AdmissionType().setValue(pv1_2Dto.getAdmissionType());
        pv1.getPv110_HospitalService().setValue(pv1_2Dto.getHospitalService());
        pv1.getPv119_VisitNumber().getID().setValue(pv1_2Dto.getVisitNumber());
        pv1.getPv144_AdmitDateTime().getTimeOfAnEvent().setValue(pv1_2Dto.getAdmissionDate());
        return pv1;
    }

    private PV2 createPv2Segment() throws DataTypeException {
        PV2 pv2 = _adtMessage.getPV2();
        pv2.getAdmitReason().getIdentifier().setValue(pv1_2Dto.getReasonForAdmission()); // to represent an 'Outpatient'
        pv2.getExpectedDischargeDateTime().getTimeOfAnEvent().setValue(pv1_2Dto.getAdmissionDate());
        return pv2;
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    private static String getDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    private static String getSequenceNumber() {
        String facilityNumberPrefix = "1234"; // some arbitrary prefix for the facility
        return facilityNumberPrefix.concat(getCurrentTimeStamp());
    }


}
