package com.param.bloodBank.controller;

import com.param.global.otims.dto.ProcedureOrderMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.param.bloodBank.dto.BloodComponentOrderMasterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.bloodBank.service.IBloodComponentOrderMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodComponentOrderMasterController  implements ICommonConstants {

	final static Logger logger = Logger.getLogger(BloodComponentOrderMasterController.class);

	@Autowired
	IBloodComponentOrderMasterService ibloodComponentOrderMasterService;

	@RequestMapping(value = "/saveBloodComponentOrderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodComponentOrderMaster(@RequestBody BloodComponentOrderMasterDto bloodComponentOrderMasterDto) {
		try {
			return ibloodComponentOrderMasterService.saveBloodComponentOrderMaster(bloodComponentOrderMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodComponentOrderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodComponentOrderMaster(@RequestBody BloodComponentOrderMasterDto bloodComponentOrderMasterDto) {
		try {
			return ibloodComponentOrderMasterService.updateBloodComponentOrderMaster(bloodComponentOrderMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getBloodComponentOrderMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodComponentOrderMasterList(@RequestBody BloodComponentOrderMasterDto bloodComponentOrderMasterDto) {
		try {
			return ibloodComponentOrderMasterService.getBloodComponentOrderMasterList(bloodComponentOrderMasterDto);
		} catch (Exception e) {
			logger.error("getBloodComponentOrderMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodComponentOrderMasterById/{bloodComponentOrderId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodComponentOrderMasterById(@PathVariable ("bloodComponentOrderId") Integer bloodComponentOrderId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodComponentOrderMasterService.getBloodComponentOrderMasterById(bloodComponentOrderId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodComponentOrderMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodComponentOrderMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodComponentOrderMaster(@PathVariable ("organizationId") Integer organizationId,
			                                      @PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodComponentOrderMasterService.getActiveBloodComponentOrderMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getListOfProcedureOrderByPatientId/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getListOfProcedureOrderByPatientId(@PathVariable ("patientId") Integer patientId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodComponentOrderMasterService.getListOfProcedureOrderByPatientId(patientId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getListOfProcedureOrderByPatientId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchOrderedBloodComponentByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchOrderedBloodComponentByDate(@RequestBody BloodComponentOrderMasterDto bloodComponentOrderMasterDto) {
		try {
			return ibloodComponentOrderMasterService.searchOrderedBloodComponentByDate(bloodComponentOrderMasterDto);
		} catch (Exception e) {
			logger.error("searchOrderedBloodComponentByDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/closePatientRequisition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response closePatientRequisition(@RequestBody PatientRequisitionDetailsDto dto) {
		try {
			return ibloodComponentOrderMasterService.closePatientRequisition(dto);
		} catch (Exception e) {
			logger.error("closePatientRequisition failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateIsExportedStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIsExportedStatus(@RequestBody BloodComponentOrderMasterDto dto) {
		try {
			return ibloodComponentOrderMasterService.updateIsExportedStatus(dto);
		} catch (Exception e) {
			logger.error("updateIsExportedStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getDataByDateFromBloodComponentOrderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataByDateFromBloodComponentOrderMaster(@RequestBody BloodComponentOrderMasterDto dto) {
		try
		{
			return ibloodComponentOrderMasterService.getDataByDateFromBloodComponentOrderMaster(dto);
		}catch(Exception e)
		{
			logger.error("getDataByDateFromBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getBloodComponentListForCoversheet/getUniqueDateFromBloodComponentOrderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateFromBloodComponentOrderMaster(@RequestBody BloodComponentOrderMasterDto dto) {
		try
		{
			return ibloodComponentOrderMasterService.getUniqueDateFromBloodComponentOrderMaster(dto);
		}catch(Exception e)
		{
			logger.error("getUniqueDateFromBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
