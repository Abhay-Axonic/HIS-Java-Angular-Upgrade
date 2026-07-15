package com.param.pharmacy.ip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.jsonSchema.types.ArraySchema.Items;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.opd.coversheet.dto.InfusionSolutionDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.service.IInfusionSolutionService;
import com.param.opd.coversheet.service.IMedicationService;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdate;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdateItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.service.pharmacology.IPharmacologyValidationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/pharmacology/validation")
public class PharmacologyValidationController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(PharmacologyValidationController.class);

	@Autowired
	private IPharmacologyValidationService pharmacologyValidationService;

	@Autowired
	private IMedicationService iMedicationService;

	@Autowired
	private IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;
	
	@Autowired
	private IInfusionSolutionService iInfusionSolutionService;

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try {
			return new ResponseEntity<Object>(pharmacologyValidationService.getDropdownList(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get pharmacology validation list ", response = PharmacologyValidationSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pharmacologyValidationSearch(
			@RequestBody PharmacologyValidationSearch pharmacologyValidationSearch) {
		try {
			return new ResponseEntity<Object>(
					pharmacologyValidationService.pharmacologyValidationSearchBP(pharmacologyValidationSearch),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "save pharmacology validation ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pharmacologyValidationSave(@RequestBody PharmacologyUpdate pharmacologyUpdate) {
		try {
			return new ResponseEntity<Object>(
					pharmacologyValidationService.pharmacologyValidationSave(pharmacologyUpdate), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/savePresWithouthPharmaValidation", method =
	 * RequestMethod.PATCH,consumes = MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ApiOperation(value = "save pharmacology validation ", response =
	 * PharmacologyValidationDetails.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<Object> savePresWithouthPharmaValidation(@RequestBody
	 * PrescriptionDto medicationDTOs) { try {
	 * 
	 * iMedicationService.saveMedication(medicationDTOs); //
	 * pharmacologyValidationService.pharmacologyValidationSave(pharmacologyUpdate);
	 * 
	 * } catch(Exception ex) { return new ResponseEntity<Object>(new
	 * ErrorResponse("HIS-1193", "Error while saving pharmacology validation", ex),
	 * HttpStatus.INTERNAL_SERVER_ERROR); } return null; }
	 */

	@RequestMapping(value = "/savePresWithouthPharmaValidation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "save validation ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Response savePresWithouthPharmaValidation(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			Response res = iMedicationService.saveMedication(medicationDTOs);

			boolean validation = true; 

			if (validation == true) {
				if (res.getObject() != null) {
					Integer storeId = 0;
					medicationDTOs = (PrescriptionDto) res.getObject();
					List<PharmacologyUpdateItem> items = new ArrayList<PharmacologyUpdateItem>();

					PharmacologyUpdate pharmacologyUpdate = new PharmacologyUpdate();
					pharmacologyUpdate.setId(medicationDTOs.getPrescriptionId().longValue());
					pharmacologyUpdate.setValidate(true);
					pharmacologyUpdate.setStatusId(2);
					pharmacologyUpdate.setAdmissionId(medicationDTOs.getAdmissionId());
					pharmacologyUpdate.setUnitId(medicationDTOs.getUnitId());
					pharmacologyUpdate.setOrganizationId(medicationDTOs.getOrganizationId());
					pharmacologyUpdate.setUserId(medicationDTOs.getAddedBy());
					for (PrescriptionDetailsDto dto : medicationDTOs.getPrescriptionDetailsDtosList()) {
						if (dto.getItemId() != null) {
							Response checkItem = iMedicationService.getCheckIsNarcoticItem(dto.getItemId());
							PrescriptionDetailsDto dto2 = new PrescriptionDetailsDto();
							dto2 = (PrescriptionDetailsDto) checkItem.getObject();
							PharmacologyUpdateItem presDtlDto = new PharmacologyUpdateItem();
							presDtlDto.setId(dto.getPrescriptionDetailsid().longValue());
							presDtlDto.setIsNarcotic(dto2.getIsNarcotic());
							presDtlDto.setIntervention(false);
							presDtlDto.setItemStatusId(1);
							presDtlDto.setRemark(null);
							presDtlDto.setPrescriptionDetailId(dto.getPrescriptionDetailsid().longValue());
							storeId = dto.getStoreId();
							items.add(presDtlDto);

						}else {

							PharmacologyUpdateItem presDtlDto = new PharmacologyUpdateItem();
							presDtlDto.setId(dto.getPrescriptionDetailsid().longValue());
							presDtlDto.setIsNarcotic(false);
							presDtlDto.setIntervention(false);
							presDtlDto.setItemStatusId(1);
							presDtlDto.setRemark(null);
							presDtlDto.setPrescriptionDetailId(dto.getPrescriptionDetailsid().longValue());
							storeId = dto.getStoreId();
							items.add(presDtlDto);

						
						}
					}

					pharmacologyUpdate.setItems(items);
					pharmacologyUpdate.setStoreId(storeId);
					pharmacologyUpdate.setDoctorId(medicationDTOs.getDoctorId());
					pharmacologyUpdate.setVisitType(medicationDTOs.getVisitTypeId());

					pharmacologyValidationService.pharmacologyValidationSave(pharmacologyUpdate);
				}
			} else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}

//			iMedicationService.saveMedication(medicationDTOs);
			return res;
//			pharmacologyValidationService.pharmacologyValidationSave(pharmacologyUpdate);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get pharmacology validation details ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pharmacologyValidationDetails(@PathVariable("id") Long id,
			@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(pharmacologyValidationService.pharmacologyValidationDetailsBP(id, unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveInfusionPresWithouthPharmaValidation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "save validation ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Response saveInfusionPresWithouthPharmaValidation(@RequestBody InfusionSolutionDto  InfusionSolutionDto) {
		try {
			Response res = iInfusionSolutionService.saveInfusionSolution(InfusionSolutionDto);
			boolean validation = true; 

			if (validation == true) {
				if (res.getObject() != null) {
					Integer storeId = 0;
					InfusionSolutionDto = (InfusionSolutionDto) res.getObject();
					List<PharmacologyUpdateItem> items = new ArrayList<PharmacologyUpdateItem>();

					PharmacologyUpdate pharmacologyUpdate = new PharmacologyUpdate();
					pharmacologyUpdate.setId(InfusionSolutionDto.getPrescriptionId().longValue());
					pharmacologyUpdate.setValidate(true);
					pharmacologyUpdate.setStatusId(2);
					pharmacologyUpdate.setAdmissionId(InfusionSolutionDto.getAdmissionId());
					pharmacologyUpdate.setUnitId(InfusionSolutionDto.getUnitId());
					pharmacologyUpdate.setOrganizationId(InfusionSolutionDto.getOrganizationId());
					pharmacologyUpdate.setUserId(InfusionSolutionDto.getCreatedBy());
                    MedicationDTO medDto=new MedicationDTO();
                    Integer unitId= InfusionSolutionDto.getUnitId();
                    Integer organizationId=InfusionSolutionDto.getOrganizationId();
                    
                    Response presDtlList= iInfusionSolutionService.getPresDtlDataByPresId(InfusionSolutionDto.getPrescriptionId(), organizationId, unitId);
                    Response pres = (Response) presDtlList.getObject();
                    List<PrescriptionDetailsDto> prescriptionList = (List<PrescriptionDetailsDto>) pres.getListObject();
                    for (PrescriptionDetailsDto dto : prescriptionList) {

						if (dto.getItemId() != null) {
							Response checkItem = iMedicationService.getCheckIsNarcoticItem(dto.getItemId());
							PrescriptionDetailsDto dto2 = new PrescriptionDetailsDto();
							dto2 = (PrescriptionDetailsDto) checkItem.getObject();
							PharmacologyUpdateItem presDtlDto = new PharmacologyUpdateItem();
							presDtlDto.setId(dto.getPrescriptionDetailsidNew().longValue());
							presDtlDto.setIsNarcotic(dto2.getIsNarcotic());
							presDtlDto.setIntervention(false);
							presDtlDto.setItemStatusId(1);
							presDtlDto.setRemark(null);
							presDtlDto.setPrescriptionDetailId(dto.getPrescriptionDetailsidNew().longValue());
							storeId = dto.getStoreId();
							items.add(presDtlDto);
							pharmacologyUpdate.setInfusionTotalVolume(dto.getInfusionTotalVolume());
						}
					}

					pharmacologyUpdate.setItems(items);
					pharmacologyUpdate.setStoreId(storeId);
					pharmacologyUpdate.setDoctorId(InfusionSolutionDto.getDoctorId());
					pharmacologyUpdate.setVisitType(InfusionSolutionDto.getVisitTypeId());

					pharmacologyValidationService.pharmacologyValidationSave(pharmacologyUpdate);
				}
			} else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}

			return res;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/savePresWithouthPharmaValidationFromPendingIssue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "save validation s ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Response savePresWithouthPharmaValidationFromPendingIssue(@RequestBody PrescriptionDto medicationDTOs) {
		try {
			Response res = iMedicationService.saveMedicationPrescriptionDrtailsSkipPrescription(medicationDTOs);

			
			boolean validation = true; 
			

			if (validation == true) {
				if (res.getObject() != null) {
					Integer storeId = 0;
					medicationDTOs = (PrescriptionDto) res.getObject();
					List<PharmacologyUpdateItem> items = new ArrayList<PharmacologyUpdateItem>();

					PharmacologyUpdate pharmacologyUpdate = new PharmacologyUpdate();
					pharmacologyUpdate.setId(medicationDTOs.getPrescriptionId().longValue());
					pharmacologyUpdate.setIssueId(medicationDTOs.getIssueId());
					pharmacologyUpdate.setValidate(true);
					pharmacologyUpdate.setStatusId(2);
					pharmacologyUpdate.setAdmissionId(medicationDTOs.getAdmissionId());
					pharmacologyUpdate.setUnitId(medicationDTOs.getUnitId());
					for (PrescriptionDetailsDto dto : medicationDTOs.getPrescriptionDetailsDtosList()) {
						if (dto.getItemId() != null) {
							Response checkItem = iMedicationService.getCheckIsNarcoticItem(dto.getItemId());
							PrescriptionDetailsDto dto2 = new PrescriptionDetailsDto();
							dto2 = (PrescriptionDetailsDto) checkItem.getObject();
							PharmacologyUpdateItem presDtlDto = new PharmacologyUpdateItem();
							presDtlDto.setId(dto.getPrescriptionDetailsid().longValue());
							presDtlDto.setIsNarcotic(dto2.getIsNarcotic());
							presDtlDto.setIntervention(false);
							presDtlDto.setItemStatusId(1);
							presDtlDto.setRemark(null);
							presDtlDto.setPrescriptionDetailId(dto.getPrescriptionDetailsid().longValue());
							storeId = dto.getStoreId();
							items.add(presDtlDto);

						}
					}

					pharmacologyUpdate.setItems(items);
					pharmacologyUpdate.setStoreId(storeId);
					pharmacologyUpdate.setDoctorId(medicationDTOs.getDoctorId());
					pharmacologyUpdate.setVisitType(medicationDTOs.getVisitTypeId());

					 pharmacologyValidationService.pharmacologyValidationSave(pharmacologyUpdate);
					 
					 PrescriptionDto medicationDTOse = (PrescriptionDto) res.getObject();
						for (PrescriptionDetailsDto dto : medicationDTOse.getPrescriptionDetailsDtosList()) {
								Response checkItem = iMedicationService.getIssueDetailsId(dto);
								PrescriptionDto nennd = (PrescriptionDto) checkItem.getObject();
								dto.setIssueDetailsId(nennd.getIssueDetailsId());
							}
						return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, medicationDTOse);
				}
			} else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}

			
			
			//return medicationDTOse;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		return null;
	}
}