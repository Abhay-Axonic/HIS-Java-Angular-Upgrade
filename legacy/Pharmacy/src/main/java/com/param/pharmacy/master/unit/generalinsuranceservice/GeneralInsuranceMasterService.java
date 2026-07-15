package com.param.pharmacy.master.unit.generalinsuranceservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.City;
import com.param.entity.model.master.Country;
import com.param.entity.model.master.GeneralInsurance;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.State;
import com.param.entity.model.master.Vendor;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.vendor.VendorDetailsResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorRequest;
import com.param.pharmacy.master.unit.genralinsurancedao.IGeneralInsuranceMasterDao;
import com.param.pharmacy.master.unit.genralinsurancedto.GeneralInsuranceDetailsResponse;
import com.param.pharmacy.master.unit.genralinsurancedto.GeneralInsuranceRequest;
import com.param.pharmacy.master.unit.genralinsurancedto.GenralInsuranceListResponse;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.EmrService;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;

@Service("GeneralInsuranceMasterService")
public class GeneralInsuranceMasterService implements IGeneralInsuranceMasterService
{
	@Autowired
	IGeneralInsuranceMasterDao genralInsuranDao;
	
	@Autowired
	private SessionService sessionService;
	

	@Override
	@Transactional
	public List<GenralInsuranceListResponse> getAll() {
		List<GenralInsuranceListResponse> list = new ArrayList<GenralInsuranceListResponse>();
	
		for (GeneralInsurance genralInsurance : genralInsuranDao.getAll()) {
			GenralInsuranceListResponse genralInsuranceResponse = new GenralInsuranceListResponse();
			genralInsuranceResponse.setId((Integer) genralInsurance.getId());
			genralInsuranceResponse.setInsuranceCompanyName((String) genralInsurance.getInsuranceCompanyName());
			genralInsuranceResponse.setContactNo((String) genralInsurance.getContactNo());
			genralInsuranceResponse.setEmail((String) genralInsurance.getEmail());
			genralInsuranceResponse.setContactPersonName((String) genralInsurance.getContactPersonName());
			genralInsuranceResponse.setContactPersonNumber((String) genralInsurance.getContactPersonNumber());
			genralInsuranceResponse.setAddress((String) genralInsurance.getAddress());
			genralInsuranceResponse.setStatus((Boolean) genralInsurance.getIsActive());
			genralInsuranceResponse.setAreaId((Integer) genralInsurance.getArea().getAreaId());
			genralInsuranceResponse.setAreaName((String) genralInsurance.getArea().getAreaName());
			genralInsuranceResponse.setCityId((Integer) genralInsurance.getArea().getCity().getCityId());
			genralInsuranceResponse.setCityName((String) genralInsurance.getArea().getCity().getCityName());
			genralInsuranceResponse.setStateId((Integer) genralInsurance.getArea().getCity().getState().getStateId());
			genralInsuranceResponse.setStateName((String) genralInsurance.getArea().getCity().getState().getStateName());
			genralInsuranceResponse.setCountryId((Integer) genralInsurance.getArea().getCity().getState().getCountry().getCountryId());
			genralInsuranceResponse.setCountryName((String) genralInsurance.getArea().getCity().getState().getCountry().getCountryName());
			genralInsuranceResponse.setZipCode((String) genralInsurance.getZipcode());
			
			list.add(genralInsuranceResponse);
		}
		return list;
	}
	
	@Override
	@Transactional
	public GeneralInsuranceDetailsResponse getById(Integer id) {
		GeneralInsuranceDetailsResponse genralInsuranceResponse = new GeneralInsuranceDetailsResponse();

		GeneralInsurance genralInsurance = genralInsuranDao.getById(id);

		if (genralInsuranceResponse != null) {
			genralInsuranceResponse.setId((Integer) genralInsuranceResponse.getId());
			genralInsuranceResponse.setInsuranceCompanyName((String) genralInsurance.getInsuranceCompanyName());
			genralInsuranceResponse.setContactNo((String) genralInsurance.getContactNo());
			genralInsuranceResponse.setEmail((String) genralInsurance.getEmail());
			genralInsuranceResponse.setContactPersonName((String) genralInsurance.getContactPersonName());
			genralInsuranceResponse.setContactPersonNumber((String) genralInsurance.getContactPersonNumber());
			genralInsuranceResponse.setAddress((String) genralInsurance.getAddress());
			genralInsuranceResponse.setStatus((Boolean) genralInsurance.getIsActive());
			genralInsuranceResponse.setAreaId((Integer) genralInsurance.getArea().getAreaId());
			genralInsuranceResponse.setAreaName((String) genralInsurance.getArea().getAreaName());
			genralInsuranceResponse.setCityId((Integer) genralInsurance.getArea().getCity().getCityId());
			genralInsuranceResponse.setCityName((String) genralInsurance.getArea().getCity().getCityName());
			genralInsuranceResponse.setStateId((Integer) genralInsurance.getArea().getCity().getState().getStateId());
			genralInsuranceResponse.setStateName((String) genralInsurance.getArea().getCity().getState().getStateName());
			genralInsuranceResponse.setCountryId((Integer) genralInsurance.getArea().getCity().getState().getCountry().getCountryId());
			genralInsuranceResponse.setCountryName((String) genralInsurance.getArea().getCity().getState().getCountry().getCountryName());
			genralInsuranceResponse.setZipCode((String) genralInsurance.getZipcode());
		
		} else {

		}

		return genralInsuranceResponse;
	}
	
	@Override
	@Transactional
	public CommonResponse save(GeneralInsuranceRequest generalInsuranceRequest) {

		Area area = new Area();
		area.setAreaId(generalInsuranceRequest.getAreaId());
		GeneralInsurance genralInsurance = new GeneralInsurance();
		
		genralInsurance.setInsuranceCompanyName(generalInsuranceRequest.getInsuranceCompanyName());
		genralInsurance.setContactNo(generalInsuranceRequest.getContactNo());
		genralInsurance.setEmail(generalInsuranceRequest.getEmail());
		genralInsurance.setContactPersonName(generalInsuranceRequest.getContactPersonName());
		genralInsurance.setContactPersonNumber(generalInsuranceRequest.getContactPersonNumber());
		genralInsurance.setAddress(generalInsuranceRequest.getAddress());
		genralInsurance.setZipcode(generalInsuranceRequest.getZipCode());
		genralInsurance.setArea(area);
		CommonResponse response = new CommonResponse();
	//	sessionService.setEntityDefaults(genralInsurance);
		genralInsurance.setCreateDefaults(generalInsuranceRequest.getOrganizationId()!=null ? generalInsuranceRequest.getOrganizationId() : sessionService.getOrganizationId(),generalInsuranceRequest.getUnitId() !=null ? generalInsuranceRequest.getUnitId() :sessionService.getUnitId() ,generalInsuranceRequest.getUserId()!=null ? generalInsuranceRequest.getUserId() : sessionService.getUserId());

		response.setId(genralInsuranDao.save(genralInsurance).longValue());
		response.setMessage("General Insurance Saved Successfully");
		genralInsurance.setId(response.getId().intValue());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("save");
		payload.setData(genralInsurance);
		request.setKey(MasterKeyConstants.VendorMasterHISKey.toString());
		request.setPayload(payload);
		IEmrService emrService = new EmrService();
		//EmrService.publish(request);

		return response;
	}
	
	
	@Override
	@Transactional
	public CommonResponse update(GeneralInsuranceRequest generalInsuranceRequest) throws CustomeException {

		GeneralInsurance generalInsurance = genralInsuranDao.getById(generalInsuranceRequest.getId());
		/*
		  "id": 1, "insuranceCompanyName": "BAJAJ",	 "contactNo": "454444",	 "email": "prabhd@ham.con",	"contactPersonName": "Fsdf",
 	 	 "contactPersonNumber": "45465",	"address": "fdsfsdf", "areaId": 1,	 "zipCode":"411016"
		 */
		if (generalInsuranceRequest.getInsuranceCompanyName() != null && generalInsuranceRequest.getInsuranceCompanyName() != "") {
			generalInsurance.setInsuranceCompanyName(generalInsuranceRequest.getInsuranceCompanyName());
		}
		if (generalInsuranceRequest.getContactNo() != null && generalInsuranceRequest.getContactNo()!= "") {
			generalInsurance.setContactNo(generalInsuranceRequest.getContactNo());
		}
		if (generalInsuranceRequest.getEmail() != null && generalInsuranceRequest.getEmail() != "") {
			generalInsurance.setEmail(generalInsuranceRequest.getEmail());
		}
		if (generalInsuranceRequest.getContactPersonName() != null && generalInsuranceRequest.getContactPersonName() != "") {
			generalInsurance.setContactPersonName(generalInsuranceRequest.getContactPersonName());
		}
		if (generalInsuranceRequest.getContactPersonNumber() != null && generalInsuranceRequest.getContactPersonNumber() != "") {
			generalInsurance.setContactPersonNumber(generalInsuranceRequest.getContactPersonNumber());
		}
		if (generalInsuranceRequest.getAddress() != null && generalInsuranceRequest.getAddress() != "") {
			generalInsurance.setAddress(generalInsuranceRequest.getAddress());
		}
		if (generalInsuranceRequest.getAreaId() != null && generalInsuranceRequest.getAreaId() > 0) {
			Area area = genralInsuranDao.getArea(generalInsuranceRequest.getAreaId());
			generalInsurance.setArea(area);
		}
		if (generalInsuranceRequest.getZipCode() != null && generalInsuranceRequest.getZipCode() != "") {
			generalInsurance.setZipcode(generalInsuranceRequest.getZipCode());
		}
		if (generalInsuranceRequest.getStatus() != null) {
			generalInsurance.setIsActive(generalInsuranceRequest.getStatus());
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Insurace Company Updated Successfully");
		Boolean UnableToUpdateStatus = false;
		if (generalInsuranceRequest.getStatus() != null)
		{
			generalInsurance.setIsActive(generalInsuranceRequest.getStatus());
		}
		else
		{
			UnableToUpdateStatus = true;
			if (generalInsuranceRequest.getStatus() != null) {
				generalInsurance.setIsActive(false);
			}
			response.setMessage("Insurance Company is not Active");
		}
		response.setId(genralInsuranDao.update(generalInsurance).longValue());
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					generalInsurance.getIsActive());
			throw customeException;
		}
		return response;
	}



}
