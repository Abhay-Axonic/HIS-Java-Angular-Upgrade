package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.DoctorGradeDto;
import com.param.global.billing.service.IDoctorGradeService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping(value="/global/billing")
@SuppressWarnings({"rawtypes"})
public class DoctorGradeController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(DoctorGradeController.class);

	@Autowired
	IDoctorGradeService iDoctorGradeService;

	@RequestMapping(value="/saveDoctorGrade", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	
	public Response saveDoctorGrade(@RequestBody DoctorGradeDto doctorGradeDto){
		try{
			return  iDoctorGradeService.saveDoctorGrade(doctorGradeDto);
		}catch (Exception e) {
			logger.error("saveDoctorGrade failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDoctorGrade", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDoctorGrade(@RequestBody DoctorGradeDto doctorGradeDto){
		try{
				return  iDoctorGradeService.updateDoctorGrade(doctorGradeDto);
			}catch (Exception e) {
				logger.error("updateDoctorGrade failed", e);
				throw new ServiceException(e.getMessage());
			}

	}
	
	@RequestMapping(value = "/updateDoctorGradeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDoctorGradeStatus(@RequestBody DoctorGradeDto doctorGradeDto){
		try{
				return  iDoctorGradeService.updateDoctorGradeStatus(doctorGradeDto);
			}catch (Exception e) {
				logger.error("updateDoctorGradeStatus failed", e);
				throw new ServiceException(e.getMessage());
			}

	}
	
	
	@RequestMapping(value = "/getDoctorGradeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDoctorGradeList(@RequestBody DoctorGradeDto doctorGradeDto){
		try{
				return  iDoctorGradeService.getDoctorGradeList(doctorGradeDto);
			}catch (Exception e) {
				logger.error("getDoctorGradeList failed", e);
				throw new ServiceException(e.getMessage());
			}

	}

	
	@RequestMapping(value = "/getDoctorGradeById/{doctorGradeId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public Response getDoctorGradeById(@PathVariable ("doctorGradeId") Integer doctorGradeId,
										@PathVariable ("orgId") Integer orgId,
										@PathVariable ("orgUnitId") Integer orgUnitId){
		try{
				return  iDoctorGradeService.getDoctorGradeById(doctorGradeId, orgId, orgUnitId);
			}catch (Exception e) {
				logger.error("getDoctorGradeById failed", e);
				throw new ServiceException(e.getMessage());
			}

	}

	
	@RequestMapping(value = "/getDoctorGradeCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDoctorGradeCount(@RequestBody DoctorGradeDto doctorGradeDto){
		try{
				return  iDoctorGradeService.getDoctorGradeCount(doctorGradeDto);
			}catch (Exception e) {
				logger.error("getDoctorGradeCount failed", e);
				throw new ServiceException(e.getMessage());
			}

	}

	
	@RequestMapping(value = "/getActiveDoctorGradeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveDoctorGradeList(@RequestBody DoctorGradeDto doctorGradeDto){
		try{
				return  iDoctorGradeService.getActiveDoctorGradeList(doctorGradeDto);
			}catch (Exception e) {
				logger.error("getActiveDoctorGradeList failed", e);
				throw new ServiceException(e.getMessage());
			}

	}

}

