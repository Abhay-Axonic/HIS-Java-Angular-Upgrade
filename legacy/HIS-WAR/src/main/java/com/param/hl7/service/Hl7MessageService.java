package com.param.hl7.service;


import java.io.IOException;

import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import com.param.billing.global.dto.EncounterPaymentDto;
import com.param.global.dto.SlotMasterDto;
import com.param.hl7.msgbuilder.*;
import com.param.opd.encounter.dto.EncounterMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.transfer.dto.TransferDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.otims.dto.OTTransferRequestDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.hl7.dao.Hl7MessageDaoImpl;
import com.param.hl7.dto.Al1Dto;
import com.param.hl7.dto.Dg1Dto;
import com.param.hl7.dto.Hl7MessageQueueDto;
import com.param.hl7.dto.ObxDto;
import com.param.hl7.dto.OrcDto;
import com.param.hl7.dto.PidDto;
import com.param.hl7.dto.Pv1_2Dto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.global.common.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class Hl7MessageService {

    private static final Logger logger = LoggerFactory.getLogger(Hl7MessageService.class);

    @Autowired
    private Hl7MessageDaoImpl dao;

    public void patientUpdate(PatientRegistrationDto dto) {
        try {
            Integer count = dao.checkPatientIsInICUOrOT(dto.getPatientId());
            if (count > 0) {
                PidDto pidDto = dao.getPID(dto.getPatientId());
                // Pv1_2Dto pv1_2Dto =   dao.getPv1_2(dto.getAdmissionId());
                AdtA08MessageBuilder builder = new AdtA08MessageBuilder(pidDto);
                Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                hl7Dto.setCode("ADT_A08");
                hl7Dto.setMessage(builder.getER7EncodedMessage());
                hl7Dto.setPatientId(dto.getPatientId());
                hl7Dto.setOrgId(dto.getOrganizationId());
                hl7Dto.setUnitId(dto.getUnitId());
                hl7Dto.setMessageType("ADT");
                hl7Dto.setTriggerEvent("A08");
                hl7Dto.setGenerateFor("MRRCH");
                dao.saveMessage(hl7Dto);
            }
        } catch (BillingException | HL7Exception | IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    public void afterDiagnosisSaveUpdate(DiagnosisPatientAppointmentMapperDto dto) {
        try {
            PidDto pidDto = dao.getPID(dto.getPatientId());
            Pv1_2Dto pv1_2Dto = dao.getPv1_2(dto.getAdmissionId());
            List<Dg1Dto> dg1DtoList = dao.getDiagnosisDetails(dto.getAdmissionId());
            AdtA08MessageBuilder builder = new AdtA08MessageBuilder(pidDto, pv1_2Dto, dg1DtoList);
            Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
            hl7Dto.setCode("ADT_A08");
            hl7Dto.setMessage(builder.getER7EncodedMessage());
            hl7Dto.setPatientId(dto.getPatientId());
            hl7Dto.setOrgId(dto.getOrganizationId());
            hl7Dto.setUnitId(dto.getUnitId());
            hl7Dto.setMessageType("ADT");
            hl7Dto.setTriggerEvent("A08");
            hl7Dto.setGenerateFor("MRRCH");
            dao.saveMessage(hl7Dto);
        }
        /*
         * catch (IOException e) {
         *
         * }
         */ catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void afterAllergySaveUpdate(AllergyPatientMapperDto dto) {
        try {
            PidDto pidDto = dao.getPID(dto.getPatientId());
            Pv1_2Dto pv1_2Dto = dao.getPv1_2(dto.getAdmissionId());
            List<Al1Dto> al1DtoList = dao.getAllergyDetails(dto.getAdmissionId());
            AdtA08MessageBuilder builder = new AdtA08MessageBuilder(al1DtoList, pidDto, pv1_2Dto);
            Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
            hl7Dto.setCode("ADT_A08");
            hl7Dto.setMessage(builder.getER7EncodedMessage());
            hl7Dto.setPatientId(dto.getPatientId());
            hl7Dto.setOrgId(dto.getOrganizationId());
            hl7Dto.setUnitId(dto.getUnitId());
            hl7Dto.setMessageType("ADT");
            hl7Dto.setTriggerEvent("A08");
            hl7Dto.setGenerateFor("MRRCH");
            dao.saveMessage(hl7Dto);
        }
        /*
         * catch (HL7Exception e) {
         *
         * }
         */ catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }

    }


    public void afterAdmission(AdmissionDto dto) {

        try {
            Pv1_2Dto pv1_2Dto = dao.getPv1_2(dto.getAdmissionId());
            if (pv1_2Dto != null) {
                PidDto pidDto = dao.getPID(dto.getPatientId());
                AdtA02MessageBuilder builder = new AdtA02MessageBuilder(pidDto, pv1_2Dto);
                Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                hl7Dto.setCode("ADT_A02");
                hl7Dto.setMessage(builder.getER7EncodedMessage());
                hl7Dto.setPatientId(dto.getPatientId());
                hl7Dto.setOrgId(dto.getOrganizationId());
                hl7Dto.setAdmissionId(dto.getAdmissionId());
                hl7Dto.setUnitId(dto.getUnitId());
                hl7Dto.setMessageType("ADT");
                hl7Dto.setTriggerEvent("A02");
                hl7Dto.setGenerateFor("MRRCH");
                dao.saveMessage(hl7Dto);
            }
        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void afterTransferToIcu(TransferDto dto) {
        try {
            Pv1_2Dto pv1_2Dto = dao.getPv1_2(dto.getAdmissionId());
            if (pv1_2Dto != null) {
                PidDto pidDto = dao.getPID(pv1_2Dto.getPatientId());
                AdtA02MessageBuilder builder = new AdtA02MessageBuilder(pidDto, pv1_2Dto);
                Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                hl7Dto.setCode("ADT_A02");
                hl7Dto.setMessage(builder.getER7EncodedMessage());
                hl7Dto.setPatientId(dto.getPatientId());
                hl7Dto.setOrgId(dto.getOrganizationId());
                hl7Dto.setAdmissionId(dto.getAdmissionId());
                hl7Dto.setUnitId(dto.getUnitId());
                hl7Dto.setMessageType("ADT");
                hl7Dto.setTriggerEvent("A02");
                hl7Dto.setGenerateFor("MRRCH");
                dao.saveMessage(hl7Dto);
            }
        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void afterTransferToOt(OTTransferRequestDto dto) {
        try {
            Pv1_2Dto pv1_2Dto = dao.getPv1_2(dto.getAdmissionId());
            if (pv1_2Dto != null) {
                PidDto pidDto = dao.getPID(pv1_2Dto.getPatientId());
                AdtA02MessageBuilder builder = new AdtA02MessageBuilder(pidDto, pv1_2Dto);
                Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                hl7Dto.setCode("ADT_A02");
                hl7Dto.setMessage(builder.getER7EncodedMessage());
                hl7Dto.setPatientId(dto.getPatientId());
                hl7Dto.setOrgId(dto.getOrganizationId());
                hl7Dto.setAdmissionId(dto.getAdmissionId());
                hl7Dto.setUnitId(dto.getUnitId());
                hl7Dto.setMessageType("ADT");
                hl7Dto.setTriggerEvent("A02");
                hl7Dto.setGenerateFor("MRRCH");
                dao.saveMessage(hl7Dto);
            }
        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void afterServiceOrder(OrderMasterDto dto) {
//
//        try {
//            PidDto pidDto =   dao.getPID(dto.getPatientId());
//            Pv1_2Dto pv1_2Dto =   dao.getPv1_2(dto.getAdmissionId());
//            Orm001MessageBuilder builder = new Orm001MessageBuilder(pidDto,pv1_2Dto);
//            Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
//            hl7Dto.setCode("ORM_O01");
//            hl7Dto.setMessage(builder.getER7EncodedMessage());
//            hl7Dto.setPatientId(dto.getPatientId());
//            hl7Dto.setOrgId(dto.getOrgId());
//            hl7Dto.setAdmissionId(dto.getAdmissionId());
//            hl7Dto.setUnitId(dto.getOrgUnitId());
//            hl7Dto.setMessageType("ORM");
//            hl7Dto.setTriggerEvent("001");
//            dao.saveMessage(hl7Dto);
//        } catch (HL7Exception e) {
//            
//        } catch (IOException e) {
//            
//        } catch (BillingException e) {
//            
//        }
//    }

    public void afterLabReportRelease(LabResultMasterDto dto) {
        try {

            Pv1_2Dto pv1_2Dto = dao.getPv1_2(dto.getVisitAdmId());
            if (pv1_2Dto != null) {
                PidDto pidDto = dao.getPID(dto.getPatientId());
                List<OrcDto> orcList = dao.getOrcDetails(dto.getOrderDetailsId());

                for (OrcDto orcDto : orcList) {
                    List<ObxDto> obxList = dao.getObxDetails(orcDto.getOrderDetailsId(), pv1_2Dto.getBedCategoryId());
                    OruR01MessageBuilder builder = new OruR01MessageBuilder(pidDto, pv1_2Dto, orcDto, obxList);
                    Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                    hl7Dto.setCode("ORU_R01");
                    hl7Dto.setMessage(builder.getER7EncodedMessage());
                    hl7Dto.setPatientId(dto.getPatientId());
                    hl7Dto.setOrgId(dto.getOrgId());
                    hl7Dto.setAdmissionId(dto.getVisitAdmId());
                    hl7Dto.setUnitId(dto.getOrgUnitId());
                    hl7Dto.setMessageType("ORU");
                    hl7Dto.setTriggerEvent("R01");
                    hl7Dto.setGenerateFor("MRRCH");
                    dao.saveMessage(hl7Dto);
                }
            }
        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void afterProcedureOrder(List<ProcedureOrderMasterDto> procList) {
        try {


            Integer proReqId = procList.get(0).getProReqId();
            Integer admissionId = procList.get(0).getAdmissionId();
            Integer patientId = procList.get(0).getPatientId();
            Integer orgId = procList.get(0).getOrgId();
            Integer unitId = procList.get(0).getUnitId();
            Pv1_2Dto pv1_2Dto = dao.getPv1_2(admissionId);
            if (pv1_2Dto != null) {
                PidDto pidDto = dao.getPID(patientId);
                List<OrcDto> orcDtoList = dao.getProcedureOrderDetails(proReqId, admissionId);
                for (OrcDto orcDto : orcDtoList) {
                    Orm001MessageBuilder builder = new Orm001MessageBuilder(pidDto, pv1_2Dto, orcDto);
                    Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                    hl7Dto.setCode("ORM_O01");
                    hl7Dto.setMessage(builder.getER7EncodedMessage());
                    hl7Dto.setPatientId(patientId);
                    hl7Dto.setOrgId(orgId);
                    hl7Dto.setAdmissionId(admissionId);
                    hl7Dto.setUnitId(unitId);
                    hl7Dto.setMessageType("ORM");
                    hl7Dto.setTriggerEvent("001");
                    hl7Dto.setGenerateFor("MRRCH");
                    dao.saveMessage(hl7Dto);
                }

            }
        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void patientRegistration(PatientRegistrationDto dto) {
        try {

                PidDto pidDto = dao.getPID(dto.getPatientId());

                if(pidDto != null) {
                    A01MessageBuilder builder = new A01MessageBuilder(pidDto);
                    Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                    hl7Dto.setCode("ADT_A01");
                    hl7Dto.setMessage(builder.buildPatientRegistration());
                    hl7Dto.setPatientId(dto.getPatientId());
                    hl7Dto.setOrgId(dto.getOrganizationId());
                    hl7Dto.setUnitId(dto.getUnitId());
                    hl7Dto.setMessageType("ADT");
                    hl7Dto.setTriggerEvent("A01");
                    hl7Dto.setGenerateFor("PMS");
                    dao.saveMessage(hl7Dto);
                }


        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void patientUpdatePMS(PatientRegistrationDto dto) {

        try {

            PidDto pidDto = dao.getPID(dto.getPatientId());
            A01MessageBuilder builder = new A01MessageBuilder(pidDto);
            Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
            hl7Dto.setCode("ADT_A08");
            hl7Dto.setMessage(builder.buildPatientUpdate());
            hl7Dto.setPatientId(dto.getPatientId());
            hl7Dto.setOrgId(dto.getOrganizationId());
            hl7Dto.setUnitId(dto.getUnitId());
            hl7Dto.setMessageType("ADT");
            hl7Dto.setTriggerEvent("A08");
            hl7Dto.setGenerateFor("PMS");
            dao.saveMessage(hl7Dto);

        } catch (BillingException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void appointmentScheduling(SlotMasterDto dto, boolean flag) {

        try {

            if(dto.getNewPatient() != null && dto.getNewPatient()== true){

                PidDto pidDto = dao.getPID(dto.getPatientId());
                A01MessageBuilder builder = new A01MessageBuilder(pidDto);
                Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                hl7Dto.setCode("ADT_A01");
                hl7Dto.setMessage(builder.buildPatientRegistration());
                hl7Dto.setPatientId(dto.getPatientId());
                hl7Dto.setOrgId(dto.getOrganizationId());
                hl7Dto.setUnitId(dto.getUnitId());
                hl7Dto.setMessageType("ADT");
                hl7Dto.setTriggerEvent("A01");
                hl7Dto.setGenerateFor("PMS");
                dao.saveMessage(hl7Dto);
            }

            if(flag == true){

                SlotMasterDto slotMasterDtolotMasterDto = dao.getAdmissionDetailsForHl7(dto.getAppointmentId(), dto.getOrganizationId(), dto.getUnitId());
                if(slotMasterDtolotMasterDto.getSpecialityName() != null){
                    PidDto pidDto = dao.getPID(slotMasterDtolotMasterDto.getPatientId());
                    pidDto.setDoctorName(slotMasterDtolotMasterDto.getDoctorName());
                    pidDto.setAppointmentId(slotMasterDtolotMasterDto.getAppointmentId());
                    pidDto.setCountryCode(slotMasterDtolotMasterDto.getCountryCallingCode());
                    pidDto.setEmailId(slotMasterDtolotMasterDto.getEmail());
                    pidDto.setSpecialityName(slotMasterDtolotMasterDto.getSpecialityName());
                    pidDto.setMobileNumber(slotMasterDtolotMasterDto.getMobileNumber());
                    pidDto.setAppointmentDate(slotMasterDtolotMasterDto.getAppointmentDate());
                    pidDto.setFromTimeString(slotMasterDtolotMasterDto.getFromTimeString());
                    A01MessageBuilder builder = new A01MessageBuilder(pidDto);
                    Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                    hl7Dto.setCode("SIU_S12");
                    hl7Dto.setMessage(builder.buildAppointmentScheduling());
                    hl7Dto.setPatientId(dto.getPatientId());
                    hl7Dto.setOrgId(dto.getOrganizationId());
                    hl7Dto.setUnitId(dto.getUnitId());
                    hl7Dto.setMessageType("ADT");
                    hl7Dto.setTriggerEvent("S12");
                    hl7Dto.setGenerateFor("PMS");
                    dao.saveMessage(hl7Dto);
                    dao.updateAppointmentFlag(dto.getAppointmentId());
                }


            }

        } catch (BillingException e) {
            throw new RuntimeException(e);
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void appointmentReScheduling(SlotMasterDto dto) {
            try {

                Boolean flag = dao.checkAppointmentFlag(dto.getAppointmentIdOld());

                if(flag){

                    SlotMasterDto slotMasterDtolotMasterDto = dao.getAdmissionDetailsForHl7(dto.getAppointmentId(),dto.getOrganizationId(),dto.getUnitId());
                    PidDto pidDto = dao.getPID(dto.getPatientId());
                    pidDto.setDoctorName(slotMasterDtolotMasterDto.getDoctorName());
                    pidDto.setAppointmentId(slotMasterDtolotMasterDto.getAppointmentId());
                    pidDto.setReasonDesc(slotMasterDtolotMasterDto.getReasonDesc());
                    pidDto.setCountryCode(slotMasterDtolotMasterDto.getCountryCallingCode());
                    pidDto.setAppointmentDate(slotMasterDtolotMasterDto.getAppointmentDate());
                    pidDto.setPatientId(dto.getAppointmentIdOld());
                    pidDto.setFromTimeString(slotMasterDtolotMasterDto.getFromTimeString());
                    A01MessageBuilder builder = new A01MessageBuilder(pidDto);
                    Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                    hl7Dto.setCode("SIU_S13");
                    hl7Dto.setMessage(builder.buildAppointmentRescheduling());
                    hl7Dto.setPatientId(dto.getPatientId());
                    hl7Dto.setOrgId(dto.getOrganizationId());
                    hl7Dto.setUnitId(dto.getUnitId());
                    hl7Dto.setMessageType("ADT");
                    hl7Dto.setTriggerEvent("S13");
                    hl7Dto.setGenerateFor("PMS");
                    dao.saveMessage(hl7Dto);
                    dao.updateAppointmentFlag(dto.getAppointmentId());

                }


            } catch (BillingException e) {
                throw new RuntimeException(e);
            } catch (HL7Exception e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public void afterAppointmentWithPaidScheduling(EncounterMasterDto dto) {

        SlotMasterDto obj =new SlotMasterDto();
        obj.setAppointmentIdOld(dto.getAppointmentIdOld());
        obj.setAppointmentId(dto.getAppointmentId());
        obj.setNewPatient(dto.getNewPatient());
        obj.setOrganizationId(dto.getOrganizationId());
        obj.setPatientId(dto.getPatientId());
        obj.setUnitId(dto.getUnitId());

        Character isReshedule = dto.getIsReshedule() != null ? dto.getIsReshedule() : 'N';

        if(isReshedule.equals('Y'))
        {
           this.appointmentReScheduling(obj);
        }else{
            this.appointmentScheduling(obj, false);
        }


    }

    public void afterPatientVisitAppointmentWithScheduling(EncounterMasterDto dto) {

        SlotMasterDto obj =new SlotMasterDto();
        obj.setAppointmentIdOld(dto.getAppointmentIdOld());
        obj.setAppointmentId(dto.getAppointmentId());
        obj.setNewPatient(dto.getNewPatient());
        obj.setOrganizationId(dto.getOrganizationId());
        obj.setPatientId(dto.getPatientId());
        obj.setUnitId(dto.getUnitId());
        this.appointmentScheduling(obj, true);
    }

    public void afterPatientVisitAppointmentWithPaidScheduling(EncounterPaymentDto dto1) {

        EncounterMasterDto dto = dto1.getEncounterMasterDto();
        SlotMasterDto obj =new SlotMasterDto();
        obj.setAppointmentIdOld(dto.getAppointmentIdOld());
        obj.setAppointmentId(dto.getAppointmentId());
        obj.setNewPatient(dto.getNewPatient());
        obj.setOrganizationId(dto.getOrganizationId());
        obj.setPatientId(dto.getPatientId());
        obj.setUnitId(dto.getUnitId());
        this.appointmentScheduling(obj, true);

    }

    public void appointmentCancel(SlotMasterDto dto) {
        try {

            Boolean flag = dao.checkAppointmentFlag(dto.getAppointmentId());

            if(flag) {

                SlotMasterDto slotMasterDtolotMasterDto = dao.getCancelledForHl7(dto.getAppointmentId(), dto.getOrganizationId(), dto.getUnitId());
                PidDto pidDto = new PidDto();
                pidDto.setPatientId(dto.getPatientId());
                pidDto.setAppointmentId(dto.getAppointmentId());
                pidDto.setUnitId(dto.getUnitId());
                pidDto.setHospitalId(dto.getOrganizationId());
                pidDto.setReasonDesc(slotMasterDtolotMasterDto != null ? slotMasterDtolotMasterDto.getReasonDesc() : "");
                A01MessageBuilder builder = new A01MessageBuilder(pidDto);
                Hl7MessageQueueDto hl7Dto = new Hl7MessageQueueDto();
                hl7Dto.setCode("SIU_S15");
                hl7Dto.setMessage(builder.buildAppointmentCancellation());
                hl7Dto.setPatientId(dto.getPatientId());
                hl7Dto.setOrgId(dto.getOrganizationId());
                hl7Dto.setUnitId(dto.getUnitId());
                hl7Dto.setMessageType("ADT");
                hl7Dto.setTriggerEvent("S15");
                hl7Dto.setGenerateFor("PMS");
                dao.saveMessage(hl7Dto);
            }

        } catch (BillingException e) {
            throw new RuntimeException(e);
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}