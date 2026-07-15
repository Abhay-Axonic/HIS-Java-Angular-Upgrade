package com.param.opd.coversheet.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

import com.param.data.dto.AdministrationWorklistTimeDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdvisedDateAppointmentDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.coversheet.dto.AdministrationWorklistDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.MedicationListDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.service.IMedicationService;

@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MedicationController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(MedicationController.class);

	@Autowired
	private IMedicationService iMedicationService;

	@Autowired
	private ISmsUtilityConfigService smsUtilityService;

	@RequestMapping(value = "/saveMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMedication(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			if(medicationDTOs.getIsFromOP()==true && medicationDTOs.getIsFromOP()!=null) {
				List<PrescriptionDetailsDto> statMedList=new ArrayList<PrescriptionDetailsDto>();
				List<PrescriptionDetailsDto> NormalMedList=new ArrayList<PrescriptionDetailsDto>();


				/*
				 * for(PrescriptionDetailsDto prescriptionDetailsDto :
				 * medicationDTOs.getPrescriptionDetailsDtosList()) {
				 * if(prescriptionDetailsDto.getStatflag()==true) {
				 * statMedList.add(prescriptionDetailsDto); } else{
				 * NormalMedList.add(prescriptionDetailsDto); }
				 */

					for(PrescriptionDetailsDto prescriptionDetailsDto : medicationDTOs.getPrescriptionDetailsDtosList()) {
							NormalMedList.add(prescriptionDetailsDto);
					}
					if(statMedList.size() > 0) {
						PrescriptionDto presDto=new PrescriptionDto();
						presDto=medicationDTOs;
						presDto.setStatflag(true);
						presDto.setPrescriptionDetailsDtosList(statMedList);
						iMedicationService.saveMedication(presDto);
					}
					if(NormalMedList.size() > 0) {
						PrescriptionDto presDto2=new PrescriptionDto();
						presDto2=medicationDTOs;
						presDto2.setStatflag(false);
						presDto2.setPrescriptionDetailsDtosList(NormalMedList);
						iMedicationService.saveMedication(presDto2);
					}



					 return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return iMedicationService.saveMedication(medicationDTOs);
			}
		} catch (Exception e) {
			logger.error("saveMedication failed", e);
		}
		if(medicationDTOs.getPrescriptionNo() == null ) {
			 return new Response(ERROR, COMMON_ERROR_CODE, "Please configured prefix for prescription number", null, null);

		}
		return new Response(ERROR, COMMON_ERROR_CODE, "Please Enter all fields", null, null);

	}

	@RequestMapping(value = "/updateMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMedication(@RequestBody MedicationDTO medicationDTO) {
		try {
			return iMedicationService.updateMedication(medicationDTO);
		} catch (Exception e) {
			logger.error("updateMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/deleteMedication", method = RequestMethod.DELETE)
	public @ResponseBody Response deleteMedication(@RequestParam("medicationId") Integer medicationId) {
		try {
			return iMedicationService.deleteMedication(medicationId);
		} catch (Exception e) {
			logger.error("deleteMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicIntructions", method = RequestMethod.GET)
	public @ResponseBody Response getMedicIntructions() {
		try {
			return iMedicationService.getMedicIntructions();
		} catch (Exception e) {
			logger.error("getMedicIntructions failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicFavorites", method = RequestMethod.GET)
	public @ResponseBody Response getMedicFavorites(@RequestParam("doctorId") Integer doctorId) {
		try {
			return iMedicationService.getMedicFavorites(doctorId);
		} catch (Exception e) {
			logger.error("getMedicFavorites failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDrugList", method = RequestMethod.GET)
	public @ResponseBody Response getDrugList(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("searchDrug") String searchDrug) {
		try {
			return iMedicationService.getDrugList(unitId, orgId, searchDrug);
		} catch (Exception e) {
			logger.error("getDrugList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getRoaList", method = RequestMethod.GET)
	public @ResponseBody Response getRoaList(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("formulationTypeId") Integer formulationTypeId,
			@RequestParam("genericId") Integer genericId, @RequestParam("patientId") Integer patientId) {
		try {
			return iMedicationService.getRoaList(unitId, orgId, formulationTypeId, genericId, patientId);
		} catch (Exception e) {
			logger.error("getRoaList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getLastOrder", method = RequestMethod.GET)
	public @ResponseBody Response getLastOrder(@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId) {
		try {
			return iMedicationService.getLastOrder(patientId, encounterId);
		} catch (Exception e) {
			logger.error("getLastOrder failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getActiveMedication", method = RequestMethod.GET)
	public @ResponseBody Response getActiveMedication(@RequestParam("patientId") Integer patientId,
			@RequestParam("date") String currentDate, @RequestParam("unitId") Integer unitId) {
		try {
			return iMedicationService.getActiveMedication(patientId, currentDate, unitId);
		} catch (Exception e) {
			logger.error("getActiveMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@RequestMapping(value = "/getMedicationDetails", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationDetails(@RequestParam("patientId") Integer patientId,
			@RequestParam("date") String currentDate) {
		try {
			return iMedicationService.getMedicationDetails(patientId, currentDate);
		} catch (Exception e) {

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	@RequestMapping(value = "/getMedicationDetails", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unitId") int unitId,
			@RequestHeader(value="organizationId") int organizationId, @RequestParam(value="encounterId",required= false)
			Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try {
			PrescriptionDto prescriptionDto= new PrescriptionDto();
			prescriptionDto.setPatientId(patientId);
			prescriptionDto.setUnitId(unitId);
			prescriptionDto.setOrganizationId(organizationId);
			prescriptionDto.setEncounterId(encounterId);
			prescriptionDto.setAdmissionId(admissionId);
			return iMedicationService.getMedicationDetails(prescriptionDto);
		} catch (Exception e) {
			logger.error("getMedicationDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getFrequencyDetails", method = RequestMethod.GET)
	public @ResponseBody Response getFrequencyDetails() {
		try {
			return iMedicationService.getFrequencyDetails();
		} catch (Exception e) {
			logger.error("getFrequencyDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getRoaMasters", method = RequestMethod.GET)
	public @ResponseBody Response getRoaMasters() {
		try {
			return iMedicationService.getRoaMasters();
		} catch (Exception e) {
			logger.error("getRoaMasters failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getFrequencyList", method = RequestMethod.GET)
	public @ResponseBody Response getFrequencyList(@RequestParam(value = "orgId") Integer orgId,
			@RequestParam(value = "unitId") Integer unitId, @RequestParam(value = "searchPar") String searchPar) {
		try {
			return iMedicationService.getFrequencyList(orgId, unitId, searchPar);
		} catch (Exception e) {
			logger.error("getFrequencyList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getUOMList", method = RequestMethod.GET)
	public @ResponseBody Response getUOMList(@RequestParam(value = "orgId") Integer orgId,
			@RequestParam(value = "unitId") Integer unitId, @RequestParam(value = "itemId") Integer itemId) {
		try {
			return iMedicationService.getUOMList(orgId, unitId, itemId);
		} catch (Exception e) {
			logger.error("getUOMList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getEncounterMedicationHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEncounterMedicationHistory(@RequestBody MedicationDTO medicationDTO) {
		try {
			return iMedicationService.getEncounterMedicationHistory(medicationDTO);
		} catch (Exception e) {
			logger.error("getEncounterMedicationHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationHistoryByEncounterId", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationHistoryByEncounterId(@RequestParam(value="encounterId" , required = false) Integer encounterId,
			@RequestParam(value="admissionId" , required = false) Integer admissionId ,@RequestParam(value="stopDate") String stopDate) {
		try {
			MedicationDTO medicationDTO=new MedicationDTO();
			medicationDTO.setAdmissionId(admissionId);
			medicationDTO.setEncounterId(encounterId);
			medicationDTO.setStopDate(stopDate);
			return iMedicationService.getMedicationHistoryByEncounterId(medicationDTO);
		} catch (Exception e) {
			logger.error("getMedicationHistoryByEncounterId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationByAdmissionId", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationByAdmissionId(@RequestParam(value = "admissionId") Integer admissionId) {
		try {
			return iMedicationService.getMedicationByAdmissionId(admissionId);
		} catch (Exception e) {
			logger.error("getMedicationByAdmissionId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getICPCSYSTEMList", method = RequestMethod.GET)
	public @ResponseBody Response getICPCSYSTEMList() {
		try {
			return iMedicationService.getICPCSYSTEMList();
		} catch (Exception e) {
			logger.error("getICPCSYSTEMList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getICPCMasterList", method = RequestMethod.GET)
	public @ResponseBody Response getICPCMasterList(@RequestParam("icpcSyatemId") Integer icpcSyatemId,
			@RequestParam("searchICPC") String searchICPC) {
		try {
			return iMedicationService.getICPCMasterList(icpcSyatemId, searchICPC);
		} catch (Exception e) {
			logger.error("getICPCMasterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveMedicationHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMedicationHistory(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {

			return iMedicationService.saveMedicationHistory(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("saveMedicationHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateMedicationHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMedicationHistory(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {

			return iMedicationService.updateMedicationHistory(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("updateMedicationHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	 @RequestMapping(value="/deleteMedicationHistory", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody Response deleteMedicationHistory(@RequestBody MedicationHistoryDto medicationHistoryDto) {
         try {
             return iMedicationService.deleteMedicationHistory(medicationHistoryDto);
         }catch (Exception e) {
             logger.error("deleteMedicationHistory failed", e);
         }
         return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
     }

	@RequestMapping(value = "/getMedicationHistoryList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMedicationHistoryList(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getMedicationHistoryList(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getMedicationHistoryList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getAdministrationWorkListList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdministrationWorkListList(
			@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getAdministrationWorkListList(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getAdministrationWorkListList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDetailedListOfMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDetailedListOfMedication(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getDetailedListOfMedication(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getDetailedListOfMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	// get ROA for non formulary item name & generic Name
	@RequestMapping(value = "/getRoaListNonFormulary", method = RequestMethod.GET)
	public @ResponseBody Response getRoaList(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("patientId") Integer patientId) {
		try {
			return iMedicationService.getRoaListNonFormulary(unitId, orgId, patientId);
		} catch (Exception e) {
			logger.error("getRoaListNonFormulary failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMasterListOfICPCSystem/{icpcSystemId}/{searchICPC}", method = RequestMethod.GET)
	public @ResponseBody Response getMasterListOfICPCSystem(@PathVariable("icpcSystemId") Integer icpcSystemId,
			@PathVariable("searchICPC") String searchICPC) {
		try {
			return iMedicationService.getMasterListOfICPCSystem(icpcSystemId, searchICPC);
		} catch (Exception e) {
			logger.error("getMasterListOfICPCSystem failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDateWiseListOfPatientMedication/{patientId}/{organizationId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public @ResponseBody Response getDateWiseListOfPatientMedication(@PathVariable("patientId") Integer patientId,
			@PathVariable("organizationId") Integer organizationId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		try {
			return iMedicationService.getDateWiseListOfPatientMedication(patientId,
					organizationId, fromDate, toDate);
		} catch (Exception e) {
			logger.error("getDateWiseListOfPatientMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationEncountersList/{patientId}/{encounterId}/{unitId}/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationEncountersList(@PathVariable("patientId") int patientId,
			@PathVariable("encounterId") Integer encounterId, @PathVariable("unitId") Integer unitId,
			@PathVariable("organizationId") Integer organizationId) {
		try {
			return iMedicationService.getMedicationEncountersList(patientId, encounterId, unitId, organizationId);
		} catch (Exception e) {
			logger.error("getMedicationEncountersList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationListById/{prescriptionId}/{unitId}/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationListById(@PathVariable("prescriptionId") int prescriptionId,
			@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId) {
		try {
			return iMedicationService.getMedicationListById(prescriptionId, unitId, organizationId);
		} catch (Exception e) {
			logger.error("getMedicationListById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOPDMedicationListById/{prescriptionId}/{prescriptionDetailsid}/{patientId}/{itemId}/{unitId}/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody Response getOPDMedicationListById(@PathVariable("prescriptionId") Integer prescriptionId,@PathVariable("prescriptionDetailsid") Integer prescriptionDetailsid,
			@PathVariable("patientId") Integer patientId, @PathVariable("itemId") Integer itemId,
			@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId) {
		try {
			return iMedicationService.getOPDMedicationListById(prescriptionId, prescriptionDetailsid, patientId, itemId, unitId,
					organizationId);
		} catch (Exception e) {
			logger.error("getOPDMedicationListById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/medicationUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response medicationUpdate(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			return iMedicationService.medicationUpdate(medicationDTOs);
		} catch (Exception e) {
			logger.error("medicationUpdate failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDischargeMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDischargemedication(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			return iMedicationService.getDischargeMedication(medicationDTOs);
		} catch (Exception e) {
			logger.error("getDischargemedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationDetailsByDoctorId", method = RequestMethod.GET)
	/*public @ResponseBody Response getMedicationDetailsByDoctorId(@RequestParam("patientId") Integer patientId,
			@RequestParam("date") String currentDate, @RequestParam("doctorId") Integer doctorId) {*/
	public @ResponseBody Response getMedicationDetailsByDoctorId(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unitId") int unitId,
			@RequestHeader(value="organizationId") int organizationId, @RequestParam(value="doctorId", required = false) Integer doctorId, @RequestParam(value="encounterId",required= false)
			Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try {
			PrescriptionDto prescriptionDto= new PrescriptionDto();
			prescriptionDto.setPatientId(patientId);
			prescriptionDto.setUnitId(unitId);
			prescriptionDto.setDoctorId(doctorId);
			prescriptionDto.setOrganizationId(organizationId);
			prescriptionDto.setEncounterId(encounterId);
			prescriptionDto.setAdmissionId(admissionId);
			return iMedicationService.getMedicationDetailsByDoctorId(prescriptionDto);
		} catch (Exception e) {
			logger.error("getMedicationDetailsByDoctorId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationDetailsForMedicationHistory", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationDetailsForMedicationHistory(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unitId") int unitId,
			@RequestHeader(value="organizationId") int organizationId, @RequestHeader(value="prescriptionDate") String prescriptionDate, @RequestParam(value="encounterId",required= false)
			Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try {
			PrescriptionDto prescriptionDto= new PrescriptionDto();
			prescriptionDto.setPatientId(patientId);
			prescriptionDto.setUnitId(unitId);
			prescriptionDto.setOrganizationId(organizationId);
			prescriptionDto.setPrescriptionDate(prescriptionDate);
//			prescriptionDto.setEncounterId(encounterId);
//			prescriptionDto.setAdmissionId(admissionId);
			return iMedicationService.getMedicationDetailsForMedicationHistory(prescriptionDto);
		} catch (Exception e) {
			logger.error("getMedicationDetailsForMedicationHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getStoreIdForNursing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getStoreIdForNursing(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			return iMedicationService.getStoreIdForNursing(medicationDTOs);
		} catch (Exception e) {
			logger.error("getStoreIdForNursing failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getListOfLastEncounterAndAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfLastEncounterAndAdmission(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.getListOfLastEncounterAndAdmission(medicationDTOs);
	} catch (Exception e) {
		logger.error("getListOfLastEncounterAndAdmission failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@RequestMapping(value = "/saveJustificationForMedicationStop", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveJustificationForMedicationStop(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.saveJustificationForMedicationStop(medicationDTOs);
	} catch (Exception e) {
		logger.error("saveJustificationForMedicationStop failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@RequestMapping(value = "/saveJustificationForMedicationStopForHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveJustificationForMedicationStopForHistory(@RequestBody MedicationHistoryDto medicationHistoryDto) {
	try {
		return iMedicationService.saveJustificationForMedicationStopForHistory(medicationHistoryDto);
	} catch (Exception e) {
		logger.error("saveJustificationForMedicationStopForHistory failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@RequestMapping(value = "/getContinuedMedicationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getContinuedMedicationList(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getContinuedMedicationList(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getContinuedMedicationList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/getLastEncounterAndAdmissionList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getLastEncounterAndAdmissionList(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.getLastEncounterAndAdmissionList(medicationDTOs);
	} catch (Exception e) {
		logger.error("getLastEncounterAndAdmissionList failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@RequestMapping(value = "/getMedicationHistoryListForBookView", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationHistoryListForBookView(@RequestParam(value="patientId") int patientId,
			@RequestHeader(value="organizationId") int organizationId) {
		try {
			MedicationHistoryDto medicationHistoryDto= new MedicationHistoryDto();
			medicationHistoryDto.setPatientId(patientId);
			medicationHistoryDto.setOrganizationId(organizationId);
			return iMedicationService.getMedicationHistoryListForBookView(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getMedicationHistoryListForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/getGenericListForGenericSearch", method = RequestMethod.GET)
	public @ResponseBody Response getGenericListForGenericSearch(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("searchDrug") String searchDrug) {
		try {
			return iMedicationService.getGenericListForGenericSearch(unitId, orgId, searchDrug);
		} catch (Exception e) {
			logger.error("getGenericListForGenericSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getGenericListForBrandNameSearch", method = RequestMethod.GET)
	public @ResponseBody Response getGenericListForBrandNameSearch(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("genericId") Integer genericId) {
		try {
			return iMedicationService.getGenericListForBrandNameSearch(unitId, orgId, genericId);
		} catch (Exception e) {
			logger.error("getGenericListForBrandNameSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@RequestMapping(value = "/getMasterListOfICPCSystemForCoversheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMasterListOfICPCSystemForCoversheet(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getMasterListOfICPCSystemForCoversheet(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getMasterListOfICPCSystemForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/getMasterListOfICPCSystemForCoversheetForCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMasterListOfICPCSystemForCoversheetForCode(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getMasterListOfICPCSystemForCoversheetForCode(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getMasterListOfICPCSystemForCoversheetForCode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDetailedListForAdministrationWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDetailedListForAdministrationWorklist(@RequestBody AdministrationWorklistDto administrationWorklistDto) {
		try {
			return iMedicationService.getDetailedListForAdministrationWorklist(administrationWorklistDto);
		} catch (Exception e) {
			logger.error("getDetailedListForAdministrationWorklist failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getBatchListForAdministrationWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBatchListForAdministrationWorklist(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getBatchListForAdministrationWorklist(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getBatchListForAdministrationWorklist failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getUomListByBatchIdForAdministrationWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUomListByBatchIdForAdministrationWorklist(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getUomListByBatchIdForAdministrationWorklist(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getUomListByBatchIdForAdministrationWorklist failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getBrandListByGenericIdForVaccinationChart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBrandListByGenericIdForVaccinationChart(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getBrandListByGenericIdForVaccinationChart(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getBrandListByGenericIdForVaccinationChart failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getSubstituteListForActiveMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubstituteListForActiveMedication(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getSubstituteListForActiveMedication(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getSubstituteListForActiveMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveJustificationForMedicationDelete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveJustificationForMedicationDelete(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.saveJustificationForMedicationDelete(medicationDTOs);
	} catch (Exception e) {
		logger.error("saveJustificationForMedicationDelete failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}


	@RequestMapping(value = "/saveAdvisedDateAppointment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAdvisedDateAppointment(@RequestBody AdvisedDateAppointmentDto advisedDateAppointmentDto) {
		try {
			Response res= iMedicationService.saveAdvisedDateAppointment(advisedDateAppointmentDto);


			if(res.getStatus().equals(SUCCESS)) {
				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setPatientId(advisedDateAppointmentDto.getPatientId());
				smsInfo.setOrganizationId(advisedDateAppointmentDto.getOrganizationId());
				smsInfo.setOrgId(advisedDateAppointmentDto.getOrganizationId());
				smsInfo.setUnitId(advisedDateAppointmentDto.getUnitId());
				smsInfo.setEncounterId(advisedDateAppointmentDto.getEncounterId());
				smsInfo.setDateTime(advisedDateAppointmentDto.getAdvisedDate());

					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ADVISED_DATE_APPOINTMENT, smsInfo);
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ADVISED_DATE_APPOINTMENT, smsInfo);

					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}

		} catch (Exception e) {
			logger.error("saveAdvisedDateAppointment failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getAdvisedDateAppointmentList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdvisedDateAppointmentList(@RequestBody AdvisedDateAppointmentDto advisedDateAppointmentDto) {
		try {
			return iMedicationService.getAdvisedDateAppointmentList(advisedDateAppointmentDto);

		} catch (Exception e) {
			logger.error("getAdvisedDateAppointmentList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getAdvisedDateAppointmentListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdvisedDateAppointmentListCount(@RequestBody AdvisedDateAppointmentDto advisedDateAppointmentDto) {
		try {
			return iMedicationService.getAdvisedDateAppointmentListCount(advisedDateAppointmentDto);

		} catch (Exception e) {
			logger.error("getAdvisedDateAppointmentListCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getAvailableStokeByItemId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAvailableStokeByItemId(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getAvailableStokeByItemId(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("getAvailableStokeByItemId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getFrequentlyUsedMedicationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFrequentlyUsedMedicationList(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.getFrequentlyUsedMedicationList(medicationDTOs);
	} catch (Exception e) {
		logger.error("getFrequentlyUsedMedicationList failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@RequestMapping(value = "/callAI/{message}", method = RequestMethod.GET)
	public @ResponseBody String callAI(@PathVariable("message") String message) {
		try {
			if (message != null && !message.trim().equals("")) {
				message = message.replace(" ", "%20");
			}
			String url = "https://www.neuronet.ai/raptr/getComplaints?usersays=" + message;
			// String
			// url="http://ec2-54-71-145-56.us-west-2.compute.amazonaws.com:8080/raptr/getComplaints?usersays="+message;
			// final String USER_AGENT = "Mozilla/5.0";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			// con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			//System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			//System.out.println(response.toString());

			return response.toString();
		} catch (Exception e) {
			logger.error("callAI failed", e);
		}
		return "";
	}


	@RequestMapping(value = "/getOPDMedicationListByIdForNonFormulary/{prescriptionId}/{prescriptionDetailsid}/{patientId}/{unitId}/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody Response getOPDMedicationListByIdForNonFormulary(@PathVariable("prescriptionId") Integer prescriptionId,@PathVariable("prescriptionDetailsid") Integer prescriptionDetailsid,
			@PathVariable("patientId") Integer patientId,
			@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId) {
		try {
			return iMedicationService.getOPDMedicationListByIdForNonFormulary(prescriptionId, prescriptionDetailsid, patientId, unitId,
					organizationId);
		} catch (Exception e) {
			logger.error("getOPDMedicationListByIdForNonFormulary failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@SuppressWarnings("unused")
	@RequestMapping(value = "/getRazerPay", method = RequestMethod.GET)
	public @ResponseBody Response getRazerPay(@RequestParam(value = "razorpay_payment_id") String orgId,
			@RequestParam(value = "razorpay_payment_link_id") String unitId, @RequestParam(value = "razorpay_payment_link_reference_id") String searchPar,
			@RequestParam(value = "razorpay_payment_link_status") String razorpay_payment_link_status, @RequestParam(value = "razorpay_signature") String razorpay_signature) {
		try {

			String orgId1 = orgId ;
			String orgId2 = unitId ;
			String orgId13 = searchPar ;
			String orgId14 = razorpay_payment_link_status ;
			String orgId15 = razorpay_signature ;
			return null;
		} catch (Exception e) {
			logger.error("getRazerPay failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveCIMMS", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCIMMS(@RequestBody PrescriptionDto prescriptionDto) {
		try {
			return iMedicationService.saveCIMMS(prescriptionDto);
		} catch (Exception e) {
			logger.error("saveCIMMS failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getMedicationDetailsForNewEMR", method = RequestMethod.GET)
	public @ResponseBody Response getMedicationDetailsForNewEMR(@RequestParam(value="patientId") int patientId,@RequestParam(value="unitId") int unitId,
			@RequestParam(value="organizationId") int organizationId, @RequestParam(value="doctorId", required = false) Integer doctorId, @RequestParam(value="encounterId",required= false)
			Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try {
			PrescriptionDto prescriptionDto= new PrescriptionDto();
			prescriptionDto.setPatientId(patientId);
			prescriptionDto.setUnitId(unitId);
			prescriptionDto.setDoctorId(doctorId);
			prescriptionDto.setOrganizationId(organizationId);
			prescriptionDto.setEncounterId(encounterId);
			prescriptionDto.setAdmissionId(admissionId);
			return iMedicationService.getMedicationDetailsByDoctorId(prescriptionDto);
		} catch (Exception e) {
			logger.error("getMedicationDetailsForNewEMR failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/searchDrugByGenericNameOrItemName", method = RequestMethod.GET)
	public @ResponseBody Response searchDrugByGenericNameOrItemName(@RequestParam("unitId") Integer unitId, @RequestParam("orgId") Integer orgId, @RequestParam("searchDrug") String searchDrug) {
		try {
			return iMedicationService.searchDrugByGenericNameOrItemName(unitId, orgId, searchDrug);
		} catch (Exception e) {
			logger.error("searchDrugByGenericNameOrItemName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveAdministratorWorkList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAdministratorWorkList(@RequestBody List<AdministrationWorklistTimeDetailsDto> dto) {
		try {
			return iMedicationService.saveAdministratorWorkList(dto);
		} catch (Exception e) {
			logger.error("saveAdministratorWorkList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getItemListWithSubstituteForNewEMR", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getItemListWithSubstituteForNewEMR(@RequestBody MedicationListDto dto) {
		try {
			return iMedicationService.getItemListWithSubstituteForNewEMR(dto);
		} catch (Exception e) {
			logger.error("getItemListWithSubstituteForNewEMR failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getSubstituteListForNewEMR", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubstituteListForNewEMR(@RequestBody MedicationListDto dto) {
		try {
			return iMedicationService.getSubstituteListForNewEMR(dto);
		} catch (Exception e) {
			logger.error("getSubstituteListForNewEMR failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@RequestMapping(value = "/getNonFormularyItemForWorklistPharmacy/{prescriptionId}/{unitId}/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody Response getNonFormularyItemForWorklistPharmacy(@PathVariable("prescriptionId") Integer prescriptionId,
			@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId) {
		try {
			return iMedicationService.getNonFormularyItemForWorklistPharmacy(prescriptionId, unitId,
					organizationId);
		} catch (Exception e) {
			logger.error("getNonFormularyItemForWorklistPharmacy failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/deleteMedicationForPharmacy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deleteMedicationForPharmacy(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.deleteMedicationForPharmacy(medicationDTOs);
	} catch (Exception e) {
		logger.error("deleteMedicationForPharmacy failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@RequestMapping(value = "/saveMedicationPrescriptionDrtailsSkipPrescription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMedicationPrescriptionDrtailsSkipPrescription(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			return	iMedicationService.saveMedicationPrescriptionDrtailsSkipPrescription(medicationDTOs);

		} catch (Exception e) {
			logger.error("saveMedicationPrescriptionDrtailsSkipPrescription failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, "Please Enter all fields", null, null);

	}


	@RequestMapping(value = "/saveJustificationForMedicationDeleteMultipleMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveJustificationForMedicationDeleteMultipleMedication(@RequestBody PrescriptionDto medicationDTOs) {
	try {
		return iMedicationService.saveJustificationForMedicationDeleteMultipleMedication(medicationDTOs);
	} catch (Exception e) {
		logger.error("saveJustificationForMedicationDeleteMultipleMedication failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}


	@RequestMapping(value = "/getIpdDoctorCoverSheetUniqueDates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUniqueDates(@RequestBody PrescriptionDto dto) {
		try {

			return iMedicationService.getUniqueDates(dto);


		} catch (Exception e) {
			logger.error("getUniqueDates failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getIpdDoctorCoverSheetDataByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIpdDoctorCoverSheetDataByDate(@RequestBody PrescriptionDto dto) {
		try {
			return iMedicationService.getIpdDoctorCoverSheetDataByDate(dto);

		} catch (Exception e) {
			logger.error("getIpdDoctorCoverSheetDataByDate failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
