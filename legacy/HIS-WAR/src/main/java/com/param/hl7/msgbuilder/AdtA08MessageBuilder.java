package com.param.hl7.msgbuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v231.datatype.PL;
import ca.uhn.hl7v2.model.v231.datatype.XAD;
import ca.uhn.hl7v2.model.v231.datatype.XCN;
import ca.uhn.hl7v2.model.v231.datatype.XPN;
import ca.uhn.hl7v2.model.v231.message.ADT_A01;
import ca.uhn.hl7v2.model.v231.message.ADT_A08;
import ca.uhn.hl7v2.model.v231.message.ADT_A02;
import ca.uhn.hl7v2.model.v231.segment.*;
import ca.uhn.hl7v2.parser.Parser;
import com.param.hl7.dto.*;

public class AdtA08MessageBuilder {

    private static ADT_A08 _adtMessage;
    private static String replaceNewlines(String input) {
        return input.replaceAll("\\r", "\n");
    }
    private PidDto pidDto;
    private Pv1_2Dto pv1_2Dto;
    private List<Al1Dto> al1DtoList;
    private List<Dg1Dto> dg1DtoList;
    private List<Pr1Dto> pr1DtoList;
    private List<ObxDto> obxDtoList;

    public AdtA08MessageBuilder(PidDto pidDto,Pv1_2Dto pv1_2Dto) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
    }

    public AdtA08MessageBuilder(PidDto pidDto,Pv1_2Dto pv1_2Dto,List<Dg1Dto> dg1DtoList) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
        this.dg1DtoList = dg1DtoList;
    }
    public AdtA08MessageBuilder(List<Al1Dto> al1DtoList,PidDto pidDto,Pv1_2Dto pv1_2Dto) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
        this.al1DtoList = al1DtoList;
    }

    public AdtA08MessageBuilder(PidDto pidDto) {
        this.pidDto = pidDto;
    }

    public String getER7EncodedMessage() throws HL7Exception, IOException {
        ADT_A08 adt = this.build();
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(adt);
        return replaceNewlines(encodedMessage);
    }

    public ADT_A08 build() throws HL7Exception, IOException {
        String currentDateTimeString = getCurrentTimeStamp();
        _adtMessage = new ADT_A08();
        _adtMessage.initQuickstart("ADT", "A08", "P");
        createMshSegment(currentDateTimeString);
        createEvnSegment(currentDateTimeString);
        createPidSegment();

        if(pv1_2Dto!=null){
            createPv1Segment();
            createPv2Segment();
        }



        if(obxDtoList!=null && obxDtoList.size()>0){
            Integer index = 0;
            for (ObxDto obxDto : obxDtoList){
                createOBXSegment(obxDto,index);
                index++;
            }
        }

        if(al1DtoList!=null && al1DtoList.size()>0){
            Integer index = 0;
            for (Al1Dto al1Dto : al1DtoList){
                createAL1Segment(al1Dto,index);
                index++;
            }
        }
        if(dg1DtoList!=null && dg1DtoList.size()>0){
            Integer index = 0;
            for (Dg1Dto dg1Dto : dg1DtoList) {
                createDG1Segment(dg1Dto,index);
                index++;
            }
        }

        if(pr1DtoList!=null && pr1DtoList.size()>0){
            Integer index = 0;
            for (Pr1Dto pr1Dto : pr1DtoList) {
                createPR1Segment(pr1Dto,index);
                index++;
            }
        }

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
        evn.getEventTypeCode().setValue("A08");
        evn.getRecordedDateTime().getTimeOfAnEvent().setValue(currentDateTimeString);
        return evn;
    }

    private PID createPidSegment() throws DataTypeException {
        PID pid = _adtMessage.getPID();
        //patientId
        pid.getPatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid2_PatientID().getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid3_PatientIdentifierList(0).getID().setValue(pidDto.getPatientIdentifire());
        pid.getPid4_AlternatePatientIDPID(0).getID().setValue(pidDto.getPatientIdentifire());//uhid

        pid.getPid5_PatientName(0).getFamilyLastName().getFamilyName().setValue(pidDto.getFamilyName());
        pid.getPid5_PatientName(0).getGivenName().setValue(pidDto.getGivenName());

        pid.getPid7_DateTimeOfBirth().getTimeOfAnEvent().setValue(pidDto.getDob());
        pid.getPid8_Sex().setValue(pidDto.getSex());

        pid.getPid11_PatientAddress(0).getOtherDesignation().setValue("114038");
        pid.getPid11_PatientAddress(0).getCity().setValue(pidDto.getCityName());
        pid.getPid11_PatientAddress(0).getZipOrPostalCode().setValue(pidDto.getZipCode());
        pid.getPid11_PatientAddress(0).getCountry().setValue(pidDto.getCountryName());
        pid.getPid12_CountyCode().setValue(pidDto.getCountryCode());
        pid.getPid13_PhoneNumberHome(0).getPhoneNumber().setValue(pidDto.getMobileNumber());
        pid.getPid24_MultipleBirthIndicator().setValue(pidDto.getMultipleBirthIndicator());

        return pid;
    }


    private  PV1 createPv1Segment() throws DataTypeException {
        PV1 pv1 = _adtMessage.getPV1();
        pv1.getPv11_SetIDPV1().setValue(pv1_2Dto.getAdmissionId());
        pv1.getPv12_PatientClass().setValue(pv1_2Dto.getPatientType());
        pv1.getPv13_AssignedPatientLocation().getRoom().setValue(pv1_2Dto.getRoomNo());
        pv1.getPv13_AssignedPatientLocation().getBed().setValue(pv1_2Dto.getBedNo());
        pv1.getPv13_AssignedPatientLocation().getFacility().getNamespaceID().setValue("MRRCH");
        pv1.getPv14_AdmissionType().setValue(pv1_2Dto.getAdmissionType());
        pv1.getPv15_PreadmitNumber().getID().setValue(pv1_2Dto.getVisitNumber());
        pv1.getPatientType().setValue(pv1_2Dto.getPatientType());
        pv1.getFinancialClass(0).getFinancialClass().setValue("K");//payment Entitlement
       // pv1.getChargePriceIndicator().setValue("K");
        //pv1.getBadDebtTransferAmount().setValue("NICKEL");
       // pv1.getServicingFacility().setValue("965190");
       // pv1.getPv110_HospitalService().setValue(pv1_2Dto.getHospitalService());
        pv1.getPv119_VisitNumber().getID().setValue(pv1_2Dto.getVisitNumber());
        pv1.getPv144_AdmitDateTime().getTimeOfAnEvent().setValue(pv1_2Dto.getAdmissionDate());
        pv1.getTotalPayments().setValue("1");
        return pv1;
    }

    private PV2 createPv2Segment() throws DataTypeException {
        PV2 pv2 = _adtMessage.getPV2();
        pv2.getAdmitReason().getIdentifier().setValue(pv1_2Dto.getReasonForAdmission()); // to represent an 'Outpatient'
        pv2.getExpectedDischargeDateTime().getTimeOfAnEvent().setValue(pv1_2Dto.getAdmissionDate());
        return pv2;
    }


    private OBX createOBXSegment(ObxDto obxDto,Integer index) throws DataTypeException {
        OBX obx = _adtMessage.getOBX(index);
        obx.getObservationIdentifier().getIdentifier().setValue(obxDto.getTestCode());
        return obx;
    }

    private AL1 createAL1Segment(Al1Dto al1Dto,Integer index) throws DataTypeException {
        AL1 al1 = _adtMessage.getAL1(index);
        al1.getAl13_AllergyCodeMnemonicDescription().getIdentifier().setValue(al1Dto.getAllergyCode());
        return al1;
    }

    private DG1 createDG1Segment(Dg1Dto dg1Dto,Integer index) throws DataTypeException {
        DG1 dg1 = _adtMessage.getDG1(index);
        dg1.getDg11_SetIDDG1().setValue(dg1Dto.getDiagnosisId());
        dg1.getDg12_DiagnosisCodingMethod().setValue(dg1Dto.getIcd10Code());
        dg1.getDg13_DiagnosisCodeDG1().getText().setValue(dg1Dto.getDiagnosisCode());
        dg1.getDg14_DiagnosisDescription().setValue(dg1Dto.getDiagnosisDescription());
        dg1.getDg15_DiagnosisDateTime().getTimeOfAnEvent().setValue(dg1Dto.getDiagnosisDate());
        dg1.getDg16_DiagnosisType().setValue(dg1Dto.getDiagnosisType());
        return dg1;
    }

    private PR1 createPR1Segment(Pr1Dto pr1Dto,Integer index) throws DataTypeException {
        PR1 pr1 = _adtMessage.getPR1ROL(index).getPR1();
        pr1.getPr11_SetIDPR1().setValue(pr1Dto.getProcedureOrderId());
        pr1.getPr12_ProcedureCodingMethod().setValue(pr1Dto.getCodingMethod());
        pr1.getProcedureCode().getText().setValue(pr1Dto.getProcedureCode());
        pr1.getPr14_ProcedureDescription().setValue(pr1Dto.getProcedureDescription());
        pr1.getPr15_ProcedureDateTime().getTimeOfAnEvent().setValue(pr1Dto.getProcedureDate());
        pr1.getPr16_ProcedureFunctionalType().setValue(pr1Dto.getProcedureType());
        return pr1;
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
