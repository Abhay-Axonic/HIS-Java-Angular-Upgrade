package com.param.billing.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.dto.DoctorSlabDto;
import com.param.billing.unit.service.IDoctorSlabMasterService;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="unit/DoctorSlab")
@SuppressWarnings({"rawtypes"})
public class DoctorSlabMasterController {
	
	@Autowired
	private IDoctorSlabMasterService iDoctorSlabMasterService;
	
	@RequestMapping(value = "/doctorSlabSave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response doctorSlabSave(@RequestBody List<DoctorSlabDto> DoctorSlabDto) {
		try {

			return iDoctorSlabMasterService.doctorSlabSave(DoctorSlabDto);
			
		} catch (Exception e) {
			
		}
		return null;
	}
	
	@RequestMapping(value = "/getdoctorSlabList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getdoctorSlabList(@RequestBody DoctorSlabDto DoctorSlabDto) {
		try {

			return iDoctorSlabMasterService.getdoctorSlabList(DoctorSlabDto);
			
		} catch (Exception e) {
			
		}
		return null;
	}
	
	@RequestMapping(value = "/getdoctorSlabListByDoctorId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getdoctorSlabListByDoctorId(@RequestBody DoctorSlabDto DoctorSlabDto) {
		try {

			return iDoctorSlabMasterService.getdoctorSlabListByDoctorId(DoctorSlabDto);
			
		} catch (Exception e) {
			
		}
		return null;
	}
	
	@RequestMapping(value = "/updateSlabStatusByDoctorId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSlabStatusByDoctorId(@RequestBody DoctorSlabDto DoctorSlabDto) {
		try {

			return iDoctorSlabMasterService.updateSlabStatusByDoctorId(DoctorSlabDto);
			
		} catch (Exception e) {
			
		}
		return null;
	}

}
