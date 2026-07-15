package com.param.hl7.msgbuilder;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.*;
import ca.uhn.hl7v2.parser.Parser;
import com.param.hl7.dto.ObxDto;
import com.param.hl7.dto.OrcDto;
import com.param.hl7.dto.PidDto;
import com.param.hl7.dto.Pv1_2Dto;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OruR01MessageBuilder {

    private static ORU_R01 _oruMessage;


    private static String replaceNewlines(String input) {
        return input.replaceAll("\\r", "\n");
    }

    private PidDto pidDto;
    private Pv1_2Dto pv1_2Dto;
    private OrcDto orcDto;
    private List<ObxDto> obxList;

    public OruR01MessageBuilder(PidDto pidDto, Pv1_2Dto pv1_2Dto, OrcDto orcDto,List<ObxDto> obxList) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
        this.orcDto = orcDto;
        this.obxList = obxList;
    }


    public String getER7EncodedMessage() throws HL7Exception, IOException {
        ORU_R01 oru = this.build();
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(oru);
        return replaceNewlines(encodedMessage);
    }

    public ORU_R01 build() throws HL7Exception, IOException {
        String currentDateTimeString = getCurrentTimeStamp();
        _oruMessage = new ORU_R01();
        _oruMessage.initQuickstart("ORU", "RO1", "P");
        createMshSegment(currentDateTimeString);
        createPidSegment();
        createPv1Segment();
        createOrcSegment();
        createOBRSegment();
        Integer index = 0;
        for (ObxDto obxDto : obxList) {
             createOBXSegment(obxDto,index);
             index++;
        }

        return _oruMessage;
    }

    private MSH createMshSegment(String currentDateTimeString) throws DataTypeException {
        MSH mshSegment = _oruMessage.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getSendingApplication().getNamespaceID().setValue("Clinivantage");
        mshSegment.getSendingFacility().getNamespaceID().setValue("");
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

    private PID createPidSegment() throws DataTypeException {
        PID pid = _oruMessage.getPATIENT_RESULT().getPATIENT().getPID();
        //patientId
//        pid.getPatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
//        pid.getPid2_PatientID().getID().setValue(String.valueOf(pidDto.getPatientId()));
        pid.getPid3_PatientIdentifierList(0).getID().setValue(pidDto.getPatientIdentifire());

//        pid.getPid4_AlternatePatientIDPID(0).getID().setValue(pidDto.getPatientIdentifire());//uhid

        pid.getPid5_PatientName(0).getFamilyName().getSurname().setValue(pidDto.getFamilyName());
        pid.getPid5_PatientName(0).getGivenName().setValue(pidDto.getGivenName());

        pid.getPid7_DateTimeOfBirth().getTimeOfAnEvent().setValue(pidDto.getDob());
        pid.getPid8_AdministrativeSex().setValue(pidDto.getSex());

        pid.getPid11_PatientAddress(0).getOtherDesignation().setValue("114038");
        pid.getPid11_PatientAddress(0).getCity().setValue(pidDto.getCityName());
        pid.getPid11_PatientAddress(0).getZipOrPostalCode().setValue(pidDto.getZipCode());
        pid.getPid11_PatientAddress(0).getCountry().setValue(pidDto.getCountryName());
        pid.getPid12_CountyCode().setValue(pidDto.getCountryCode());
        pid.getPid13_PhoneNumberHome(0).getPhoneNumber().setValue(pidDto.getMobileNumber());
        pid.getPid24_MultipleBirthIndicator().setValue(pidDto.getMultipleBirthIndicator());

        return pid;
    }

    private PV1 createPv1Segment() throws DataTypeException {
        PV1 pv1 = _oruMessage.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
        pv1.getPv12_PatientClass().setValue(pv1_2Dto.getPatientType()); // to represent an 'Outpatient'

        pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue("2A");
        pv1.getPv13_AssignedPatientLocation().getFacility().getNamespaceID().setValue("MRRCH");
//        pv1.getPv13_AssignedPatientLocation().getRoom().setValue(pv1_2Dto.getRoom());
//        pv1.getPv13_AssignedPatientLocation().getBed().setValue(pv1_2Dto.getNurseStation());
//        pv1.getPv14_AdmissionType().setValue(pv1_2Dto.getAdmissionType());
//        pv1.getPv110_HospitalService().setValue(pv1_2Dto.getHospitalService());
        pv1.getPv119_VisitNumber().getID().setValue(pv1_2Dto.getVisitNumber());
     //   pv1.getPv144_AdmitDateTime().getTimeOfAnEvent().setValue(pv1_2Dto.getAdmissionDate());
        return pv1;
    }

    private ORC createOrcSegment() throws DataTypeException {
        ORC orderSegment = _oruMessage.getPATIENT_RESULT().getORDER_OBSERVATION().getORC();
        orderSegment.getFillerOrderNumber().getEntityIdentifier().setValue(orcDto.getOrderDetailsId());
        orderSegment.getOrderStatus().setValue("IP");
        orderSegment.getOrc9_DateTimeOfTransaction().getTimeOfAnEvent().setValue(orcDto.getOrderDate());
        orderSegment.getOrc12_OrderingProvider(0).getFamilyName().getSurname().setValue(pidDto.getFamilyName());
        return orderSegment;
    }

    private OBR createOBRSegment() throws DataTypeException {
        OBR  obrSegment = _oruMessage.getPATIENT_RESULT().getORDER_OBSERVATION().getOBR();
       // obrSegment.getObr1_SetIDOBR().setValue("1");
        obrSegment.getObr3_FillerOrderNumber().getNamespaceID().setValue(orcDto.getOrderDetailsId());
        obrSegment.getObservationDateTime().getTimeOfAnEvent().setValue(orcDto.getOrderDate());
        obrSegment.getSpecimenReceivedDateTime().getTimeOfAnEvent().setValue(orcDto.getOrderDate());
        obrSegment.getResultStatus().setValue(orcDto.getResultStatus());
        return obrSegment;
    }

    private OBX createOBXSegment(ObxDto obxDto,Integer index) throws DataTypeException {
        OBX  obxSegment = _oruMessage.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATION(index).getOBX();

        obxSegment.getObx1_SetIDOBX().setValue(obxDto.getLabResDtlsId());
        obxSegment.getObx2_ValueType().setValue(obxDto.getRefRangeType());

        obxSegment.getObservationIdentifier().getIdentifier().setValue(obxDto.getParamCode());
        obxSegment.getObservationIdentifier().getText().setValue(obxDto.getParamName());

        CE ce = new CE(_oruMessage);
        ce.getIdentifier().setValue(obxDto.getObrValue());
        Varies value = obxSegment.getObservationValue(0);
        value.setData(ce);

        obxSegment.getObx6_Units().getIdentifier().setValue(obxDto.getParamUnit());
        obxSegment.getObx7_ReferencesRange().setValue(obxDto.getRefRange());
        obxSegment.getObx8_AbnormalFlags().setValue(obxDto.getAbnormalFlag());
        obxSegment.getObx11_ObservationResultStatus().setValue(obxDto.getObrResult());
        return obxSegment;
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
