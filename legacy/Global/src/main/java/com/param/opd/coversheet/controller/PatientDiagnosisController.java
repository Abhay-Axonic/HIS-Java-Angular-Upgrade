package com.param.opd.coversheet.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.PatientDiagnosisServiceMapperDto;
import com.param.opd.coversheet.service.IPatientDiagnosisService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/opd")
public class PatientDiagnosisController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(PatientDiagnosisController.class);

	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;

	@RequestMapping(value = "/savePatientDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientDiagnosis(@RequestHeader(value = "unit_id") int unitId,
			@RequestHeader(value = "organization_id") int organizationId,
			@RequestHeader(value = "patient_id") int patientId,
			@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {

			diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.savePatientDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("savePatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getPatientDiagnosisByPatientId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByPatientId(
//			@RequestHeader(value = "unit_id" , required=false) int unitId,
			@RequestHeader(value = "organization_id") int organizationId,
			@RequestHeader(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
//			diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByPatientId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	/**
	 * @author Ganesh Chaudhari
	 * @Aip : get patient Diagnosis by admision or encounter
	 * */
	@RequestMapping(value = "/patientDiagnosis/{patientId}/{visitId}/{visitTypeId}", method = RequestMethod.GET)
	public @ResponseBody Response getPatientDiagnosisForCaseSheet(@PathVariable(value = "patientId") int patientId,
			@PathVariable(value = "visitId") int visitId,
			@PathVariable(value = "visitTypeId") int visitTypeId) {
		try {
			return iPatientDiagnosisService.getPatientDiagnosisForCaseSheet(patientId,visitId,visitTypeId);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisForCaseSheet failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getPatientDiagnosisByPatientIdForMedicalHistory", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByPatientId(@RequestHeader(value = "unit_id") int unitId,
			@RequestHeader(value = "organization_id") int organizationId,@RequestHeader(value = "createdDate") String createdDate,
			@RequestHeader(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			diagnosisPatientAppointmentMapperDto.setCreated_date(createdDate);
			return iPatientDiagnosisService.getPatientDiagnosisByPatientIdForMedicalHistory(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByPatientIdForMedicalHistory failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/savePatientCancelledDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientCancelledDiagnosis(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {

			return iPatientDiagnosisService.savePatientCancelledDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("savePatientCancelledDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getListPatientCancelledDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListPatientCancelledDiagnosis(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {

			return iPatientDiagnosisService.getListPatientCancelledDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getListPatientCancelledDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getPatientDiagnosisByPatientIdForIcon", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByPatientIdForIcon(@RequestHeader(value = "unit_id") int unitId,
			@RequestHeader(value = "organization_id") int organizationId,
			@RequestHeader(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.getPatientDiagnosisByPatientIdForIcon(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByPatientIdForIcon failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getPatientDiagnosisByPatientIdForBookView", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByPatientIdForBookView(@RequestHeader(value = "organization_id") int organizationId,
			@RequestHeader(value="fromDate") String fromDate,@RequestHeader(value="toDate") String toDate,@RequestHeader(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			diagnosisPatientAppointmentMapperDto.setFromDate(fromDate);
			diagnosisPatientAppointmentMapperDto.setToDate(toDate);
			return iPatientDiagnosisService.getPatientDiagnosisByPatientIdForBookView(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByPatientIdForBookView failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updatePatientDiagnosisType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientDiagnosisType(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisService.updatePatientDiagnosisType(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("updatePatientDiagnosisType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/saveResolvedDiagnosisDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResolvedDiagnosisDate(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisService.saveResolvedDiagnosisDate(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("saveResolvedDiagnosisDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getPatientDiagnosisByEncounterId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByEncounterId(
			@RequestHeader(value = "organization_id") int organizationId,
			@RequestHeader(value = "encounter_id") int encounterId,
			@RequestHeader(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setEncounterId(encounterId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.getPatientDiagnosisByEncounterId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByEncounterId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/getPatientDiagnosisByAdmissionId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByAdmissionId(
			@RequestHeader(value = "organization_id") int organizationId,
			@RequestHeader(value = "admission_id") int admissionId,
			@RequestHeader(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setAdmissionId(admissionId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.getPatientDiagnosisByAdmissionId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByAdmissionId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getFrequentlyUsedDiagnosisList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFrequentlyUsedDiagnosisList(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisService.getFrequentlyUsedDiagnosisList(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getFrequentlyUsedDiagnosisList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updatePatientDiagnosisTypeOPD", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientDiagnosisTypeOPD(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisService.updatePatientDiagnosisTypeOPD(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("updatePatientDiagnosisTypeOPD failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/saveFreeTexPatientDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveFreeTexPatientDiagnosis(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisService.saveFreeTexPatientDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("saveFreeTexPatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/serchFreeTexPatientDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response serchFreeTexPatientDiagnosis(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisService.serchFreeTexPatientDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("serchFreeTexPatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/savePatientDiagnosisForNewEMR", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientDiagnosisForNewEMR(@RequestParam(value = "unit_id") int unitId,
			@RequestParam(value = "organization_id") int organizationId,
			@RequestParam(value = "patient_id") int patientId,
			@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {

			diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.savePatientDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("savePatientDiagnosisForNewEMR failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/getPatientDiagnosisByPatientIdForNewEMR", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByPatientIdForNewEMR(
//			@RequestHeader(value = "unit_id" , required=false) int unitId,
			@RequestParam(value = "organization_id") int organizationId,
			@RequestParam(value = "patient_id") int patientId) {
		try {
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto();
//			diagnosisPatientAppointmentMapperDto.setUnitId(unitId);
			diagnosisPatientAppointmentMapperDto.setPatientId(patientId);
			diagnosisPatientAppointmentMapperDto.setOrganizationId(organizationId);
			return iPatientDiagnosisService.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("getPatientDiagnosisByPatientIdForNewEMR failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@RequestMapping(value = "/savePatientDaignosisServiceMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientDaignosisServiceMapper(@RequestBody List<PatientDiagnosisServiceMapperDto> list) {
		try {
			return iPatientDiagnosisService.savePatientDaignosisServiceMapper(list);
		} catch (Exception e) {
			logger.error("savePatientDaignosisServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getPatientDaignosisServiceMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDaignosisServiceMapper(@RequestBody PatientDiagnosisServiceMapperDto dto) {
		try {
			return iPatientDiagnosisService.getPatientDaignosisServiceMapper(dto);
		} catch (Exception e) {
			logger.error("getPatientDaignosisServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
