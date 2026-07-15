package com.param.hl7.msgbuilder;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.group.ORM_O01_ORDER;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import com.param.hl7.dto.ObrDto;
import com.param.hl7.dto.OrcDto;
import com.param.hl7.dto.PidDto;
import com.param.hl7.dto.Pv1_2Dto;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Orm001MessageBuilder {

    private static ORM_O01 _ormMessage;


    private static String replaceNewlines(String input) {
        return input.replaceAll("\\r", "\n");
    }

    private PidDto pidDto;
    private Pv1_2Dto pv1_2Dto;
    private OrcDto orcDto;
    private ObrDto obrDto;

    public Orm001MessageBuilder(PidDto pidDto, Pv1_2Dto pv1_2Dto) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
    }
    public Orm001MessageBuilder(PidDto pidDto, Pv1_2Dto pv1_2Dto,OrcDto orcDto) {
        this.pidDto = pidDto;
        this.pv1_2Dto = pv1_2Dto;
        this.orcDto = orcDto;
    }

    public Orm001MessageBuilder(OrcDto orcDto) {
        this.orcDto = orcDto;
    }
    public Orm001MessageBuilder() {
    }

    public String getER7EncodedMessage() throws HL7Exception, IOException {
        ORM_O01 orm = this.build();
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(orm);
        return replaceNewlines(encodedMessage);
    }

    public ORM_O01 build() throws HL7Exception, IOException {
        String currentDateTimeString = getCurrentTimeStamp();
        _ormMessage = new ORM_O01();
        _ormMessage.initQuickstart("ORM", "O01", "P");
        createMshSegment(currentDateTimeString);
        createPidSegment();
        createPv1Segment();
        createOrcSegment();
        createOBRSegment();
        return _ormMessage;
    }

    private MSH createMshSegment(String currentDateTimeString) throws DataTypeException {
        MSH mshSegment = _ormMessage.getMSH();
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

    private PID createPidSegment() throws DataTypeException {
        PID pid = _ormMessage.getPATIENT().getPID();
        //patientId
//        pid.getPatientIdentifierList(0).getID().setValue(String.valueOf(pidDto.getPatientId()));
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
        pid.getPid12_CountyCode().setValue(pidDto.getCountryCode());
        pid.getPid13_PhoneNumberHome(0).getPhoneNumber().setValue(pidDto.getMobileNumber());
        pid.getPid12_CountyCode().setValue(pidDto.getCountryCode());
        pid.getPid24_MultipleBirthIndicator().setValue(pidDto.getMultipleBirthIndicator());

        return pid;
    }

    private PV1 createPv1Segment() throws DataTypeException {
        PV1 pv1 = _ormMessage.getPATIENT().getPATIENT_VISIT().getPV1();
        pv1.getPv12_PatientClass().setValue(pv1_2Dto.getPatientType()); // to represent an 'Outpatient'
        pv1.getPv13_AssignedPatientLocation().getRoom().setValue(pv1_2Dto.getRoomNo());
        pv1.getPv13_AssignedPatientLocation().getBed().setValue(pv1_2Dto.getBedNo());
        pv1.getPv14_AdmissionType().setValue(pv1_2Dto.getAdmissionType());
        pv1.getPv110_HospitalService().setValue(pv1_2Dto.getHospitalService());
        pv1.getPv119_VisitNumber().getID().setValue(pv1_2Dto.getVisitNumber());
        pv1.getPv144_AdmitDateTime().getTimeOfAnEvent().setValue(pv1_2Dto.getAdmissionDate());
        return pv1;
    }

    private ORM_O01_ORDER createOrcSegment() throws DataTypeException {
        ORM_O01_ORDER  orderSegment = _ormMessage.getORDER();
        orderSegment.getORC().getOrc1_OrderControl().setValue("NW");
        orderSegment.getORC().getOrc2_PlacerOrderNumber().getNamespaceID().setValue(orcDto.getOrderDetailsId());
        orderSegment.getORC().getOrc3_FillerOrderNumber().getNamespaceID().setValue(orcDto.getOrderDetailsId());
        orderSegment.getORC().getOrc8_ParentOrder().getParentSFillerOrderNumber().getNamespaceID().setValue(orcDto.getProcedureName());
        orderSegment.getORC().getOrc9_DateTimeOfTransaction().getTimeOfAnEvent().setValue(orcDto.getOrderDate());
        orderSegment.getORC().getOrc12_OrderingProvider(0).getIDNumber().setValue(orcDto.getOderProviderIdNumber());
        orderSegment.getORC().getOrc12_OrderingProvider(0).getFamilyName().getSurname().setValue(orcDto.getOderProviderFamilyName());
        return orderSegment;
    }

    private OBR createOBRSegment() throws DataTypeException {
        OBR  obrSegment = _ormMessage.getORDER().getORDER_DETAIL().getOBR();
        obrSegment.getObr1_SetIDOBR().setValue("1");
        obrSegment.getObr2_PlacerOrderNumber().getNamespaceID().setValue(orcDto.getOrderDetailsId());
        obrSegment.getObr3_FillerOrderNumber().getNamespaceID().setValue(orcDto.getOrderDetailsId());
        obrSegment.getObr4_UniversalServiceIdentifier().getIdentifier().setValue("PREMED");
        obrSegment.getObr4_UniversalServiceIdentifier().getNameOfCodingSystem().setValue("Clinivantage");
        obrSegment.getObservationDateTime().getTimeOfAnEvent().setValue(orcDto.getObservationStartTime());
        obrSegment.getObservationEndDateTime().getTimeOfAnEvent().setValue(orcDto.getObservationEndTime());
        return obrSegment;
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
