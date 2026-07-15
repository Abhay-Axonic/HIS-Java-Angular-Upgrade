/**
 * @Author Ganesh Chaudhari
 */

package com.param.hl7;

import com.param.adt.transfer.dto.TransferDto;
import com.param.billing.global.dto.EncounterPaymentDto;
import com.param.global.dto.AdmissionDto;

import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.sync.hl7ConfigListDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.OTTransferRequestDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.utility.UserComponent;
import com.param.hl7.service.Hl7MessageService;
import com.param.ipd.dto.ComplaintAdmissionDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import com.param.global.dto.PatientRegistrationDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@EnableAspectJAutoProxy
@Aspect
@Component
@Conditional(EnableHl7.class)
public class Hl7Listner {


    @Autowired
    private Hl7MessageService service;


    //TransferToICU
   @AfterReturning(value = "execution(* com.param.adt.transfer.controller.PendingTransferRequestController.acceptFinalTransferRequest(..))" +
           " and args(dto)")
   public void afterTransferToICU(JoinPoint joinPoint, TransferDto dto) {
       Integer admissionId = dto.getAdmissionId();
       if (admissionId != null && admissionId > 0) {
           List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();

           boolean isPresent = hl7ConfigList.stream()
                   .anyMatch(config -> "MRRCH".equals(config.getIsPms())); // Fixed syntax

           if (isPresent) { // Fixed variable name
               service.afterTransferToIcu(dto);
           }
       }

   }



    //Transfer to OT
    @AfterReturning(value = "execution(* com.param.global.otims.controller.OTTransferRequestController.updateOTTransferRequest(..))" +
            " and args(dto)")
    public void afterTransferToOt(JoinPoint joinPoint, OTTransferRequestDto dto) {
        Integer admissionId = dto.getAdmissionId();
        if (admissionId != null && admissionId > 0) {
            service.afterTransferToOt(dto);
        }
    }





   /* @AfterReturning(value = "execution(* com.param.global.service.IPatientRegistrationService.updatepatientRegistration(..))" +
            " and args(dto)")
    public void afterPatientUpdate(JoinPoint joinPoint, PatientRegistrationDto dto) {
        service.patientUpdate(dto);
    }*/

    @AfterReturning(value = "execution(* com.param.adt.admission.controller.BedAllocationController.savePatientAdmission(..))" +
            " and args(dto)")
    public void afterAdmission(JoinPoint joinPoint, AdmissionDto dto) {
        if((dto.getPatientId() != null && dto.getPatientId() > 0) && (dto.getAdmissionId() != null && dto.getAdmissionId() > 0)) {
            service.afterAdmission(dto);
        }
    }


    @AfterReturning(value = "execution(* com.param.lis.transaction.controller.BiochemReportReleaseController.saveReportReleaseWorkList(..))" +
            " and args(listLabResultMasterDto ,request)")
    public void afterLabReportRelease(JoinPoint joinPoint, List<LabResultMasterDto> listLabResultMasterDto, HttpServletRequest request) {
        for (LabResultMasterDto dto : listLabResultMasterDto) {
            if (dto.getVisitAdmId() != null && dto.getVisitAdmId() > 0 && dto.getVisitTypeId() != null && dto.getVisitTypeId() != 1) {
                service.afterLabReportRelease(dto);
            }
        }
    }

    @AfterReturning(value = "execution(* com.param.procedure.controller.ProcedureOrderController.saveProcedureList(..))" +
            " and args(procedureList)")
    public void afterProcedureOrder(JoinPoint joinPoint, List<ProcedureOrderMasterDto> procedureList) {

        Integer admissionId = procedureList.get(0).getAdmissionId();
        if (admissionId != null && admissionId > 0) {
            service.afterProcedureOrder(procedureList);
        }
    }

    @SuppressWarnings("null")
	@AfterReturning(value = "execution(* com.param.global.procedure.controller.BillOfMaterialController.updateReservationAgainstAdmissionEncounter(..))" +
            " and args(oTReservationdto)")
    public void afterOTReservation(JoinPoint joinPoint, OTReservationDto oTReservationdto) {
        Integer admissionId = oTReservationdto.getAdmissionId();
        if (admissionId != null && admissionId > 0) {

        	List<ProcedureOrderMasterDto> procedureList =  new LinkedList<>();
        	ProcedureOrderMasterDto dto =  new ProcedureOrderMasterDto();

        	dto.setProReqId(oTReservationdto.getProReqId());
        	dto.setAdmissionId(oTReservationdto.getAdmissionId());
        	dto.setPatientId(oTReservationdto.getPatientId());
        	dto.setOrgId(oTReservationdto.getOrganizationId());
        	dto.setUnitId(oTReservationdto.getUnitId());
        	procedureList.add(dto);
            service.afterProcedureOrder(procedureList);
        }

    }

    @AfterReturning(value = "execution(* com.param.otims.controller.OTReservationController.saveOTReservation(..))" +
            " and args(oTReservationdto,request)")
    public void afterOTReservation(JoinPoint joinPoint, OTReservationDto oTReservationdto, HttpServletRequest request) {
        Integer admissionId = oTReservationdto.getAdmissionId();
        List<ProcedureOrderMasterDto> list = new ArrayList<>();

        if (admissionId != null && admissionId > 0) {
            if (oTReservationdto.getProcedureOrderMasterDto() != null && oTReservationdto.getProcedureOrderMasterDto().size() > 0) {
                service.afterProcedureOrder(oTReservationdto.getProcedureOrderMasterDto());
            } else {
                ProcedureOrderMasterDto dto = new ProcedureOrderMasterDto();
                dto.setAdmissionId(oTReservationdto.getAdmissionId());
                dto.setProReqId(oTReservationdto.getProReqId());
                dto.setPatientId(oTReservationdto.getPatientId());
                dto.setOrganizationId(oTReservationdto.getOrganizationId());
                dto.setUnitId(oTReservationdto.getUnitId());
                list.add(dto);
                service.afterProcedureOrder(list);
            }

        }
    }


    /** For Allergy And Diagnosis*/
    @AfterReturning(value = "execution(* com.param.opd.coversheet.controller.PatientDiagnosisController.savePatientDiagnosis(..)) "+
            " and args(unitId,orgId,patientId,dto)")
    public void afterAdmissionDiagnosisSave(JoinPoint joinPoint, Integer unitId, Integer orgId,Integer patientId, DiagnosisPatientAppointmentMapperDto dto) {
        Integer admissionId = dto.getAdmissionId();
        if (admissionId != null && admissionId > 0) {
            dto.setOrganizationId(orgId);
            dto.setUnitId(unitId);
            dto.setPatientId(patientId);
            service.afterDiagnosisSaveUpdate(dto);
        }
    }

    @AfterReturning(value = "execution(* com.param.opd.coversheet.controller.PatientDiagnosisController.updatePatientDiagnosisType(..))" +
            " and args(dto)")
    public void afterAdmissionDiagnosisUpdate(JoinPoint joinPoint, DiagnosisPatientAppointmentMapperDto dto) {
        Integer admissionId = dto.getAdmissionId();
        if (admissionId != null && admissionId > 0) {
            service.afterDiagnosisSaveUpdate(dto);
        }
    }


    @AfterReturning(value = "execution(* com.param.opd.coversheet.controller.AllergyPatientMapperController.saveAllergyPatientMapper(..))" +
            " and args(unitId,orgId,dto)")
    public void afterAdmissonAllergySave(JoinPoint joinPoint, Integer unitId, Integer orgId, AllergyPatientMapperDto dto) {
        Integer admissionId = dto.getAdmissionId();
        if (admissionId != null && admissionId > 0) {
            dto.setOrganizationId(orgId);
            dto.setUnitId(unitId);
            service.afterAllergySaveUpdate(dto);
        }
    }

    @AfterReturning(value =" execution(* com.param.opd.coversheet.controller.AllergyPatientMapperController.updateAllergyPatientMapper(..))" +
            " and args(unitId,orgId,dto)")
    public void afterAdmissonAllergyUpdate(JoinPoint joinPoint, Integer unitId, Integer orgId, AllergyPatientMapperDto dto) {
        Integer admissionId = dto.getAdmissionId();
        if (admissionId != null && admissionId > 0) {
            dto.setOrganizationId(orgId);
            dto.setUnitId(unitId);
            service.afterAllergySaveUpdate(dto);
        }
    }

    //patient registration
    @AfterReturning(value = "execution(* com.param.global.controller.PatientRegistrationController.savePatientRegistration(..))" +
            " and args(dto)")
    public void afterPatientReg(JoinPoint joinPoint, PatientRegistrationDto dto) {

        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        System.out.println("hl7ConfigList : " + hl7ConfigList);
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getOrgId().equals(dto.getOrganizationId())); // Fixed syntax

        System.out.println(dto.getOrganizationId());
        System.out.println("isPresent : " + isPresent);
        if((isPresent) && (dto.getPatientId() != null && dto.getPatientId() > 0)) {
            service.patientRegistration(dto);
        }
    }

    //patient update
    @AfterReturning(value = "execution(* com.param.global.controller.PatientRegistrationController.updatepatientRegistration(..))" +
            " and args(dto)")
    public void afterUpdatepatientRegistration(JoinPoint joinPoint, PatientRegistrationDto dto) {
        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getOrgId().equals(dto.getOrganizationId())); // Fixed syntax

        if((isPresent) && (dto.getPatientId() != null && dto.getPatientId() > 0)) {
            service.patientUpdatePMS(dto);
        }

    }

   //patient appointment
    @AfterReturning(value = "execution(* com.param.global.opd.controller.AppointmentSchedulingController.scheduleAppointment(..))" +
            " and args(dto)")
    public void afterAppointmentScheduling(JoinPoint joinPoint, SlotMasterDto dto) {
        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getUnitId().equals(dto.getUnitId())); // Fixed syntax

        if((isPresent) && (dto.getAppointmentId() != null && dto.getAppointmentId() > 0)) {
            service.appointmentScheduling(dto,false);
        }

    }

    //patient reScheduling
    @AfterReturning(value = "execution(* com.param.global.opd.controller.AppointmentSchedulingController.rescheduleAppointment(..))" +
            " and args(dto)")
    public void afterAppointmentReScheduling(JoinPoint joinPoint, SlotMasterDto dto) {
        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getUnitId().equals(dto.getUnitId())); // Fixed syntax

        if((isPresent) && (dto.getAppointmentId() != null && dto.getAppointmentId() > 0)) {
            service.appointmentReScheduling(dto);//check
        }

    }


    @AfterReturning(value = "execution(* com.param.billing.global.transaction.controller.BillingMasterController.saveBillFromKioskFutureDate(..))" +
            " and args(dto)")
    public void afterAppointmentWithPaidScheduling(JoinPoint joinPoint, EncounterMasterDto dto) {
        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getUnitId().equals(dto.getUnitId())); // Fixed syntax

        if((isPresent) && (dto.getAppointmentId() != null && dto.getAppointmentId() > 0)) {
            service.afterAppointmentWithPaidScheduling(dto);//No check
        }

    }

    @AfterReturning(value = "execution(* com.param.global.opd.controller.EncounterController.saveEncoutnerMaster(..))" +
            " and args(dto)")
    public void afterPatientVisitAppointmentWithScheduling(JoinPoint joinPoint, EncounterMasterDto dto) {
        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getUnitId().equals(dto.getUnitId())); // Fixed syntax

        if((isPresent) && (dto.getAppointmentId() != null && dto.getAppointmentId() > 0)) {
            service.afterPatientVisitAppointmentWithScheduling(dto);//No check
        }

    }

    @AfterReturning(value = "execution(* com.param.billing.global.transaction.controller.BillingMasterController.collectPaymentFromEncounter(..))" +
            " and args(dto)")
    public void afterPatientVisitAppointmentWithPaidScheduling(JoinPoint joinPoint, EncounterPaymentDto dto) {

        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getUnitId().equals(dto.getEncounterMasterDto().getUnitId())); // Fixed syntax

        if((isPresent) && (dto.getEncounterMasterDto().getAppointmentId() != null && dto.getEncounterMasterDto().getAppointmentId() > 0)) {
            service.afterPatientVisitAppointmentWithPaidScheduling(dto);//No check
        }

    }

    //patient appointmentCancel
    @AfterReturning(value = "execution(* com.param.global.opd.controller.AppointmentSchedulingController.cancelScheduledAppointment(..))" +
            " and args(dto)")
    public void afterAppointmentCancel(JoinPoint joinPoint, SlotMasterDto dto) {

        List<hl7ConfigListDto> hl7ConfigList = UserComponent.gethl7ConfigList();
        boolean isPresent = hl7ConfigList.stream()
                .anyMatch(config -> "PMS".equals(config.getIsPms())&& config.getUnitId().equals(dto.getUnitId())); // Fixed syntax

        if((isPresent) && (dto.getAppointmentId() != null && dto.getAppointmentId() > 0)) {
            service.appointmentCancel(dto);// check
        }

    }
}
