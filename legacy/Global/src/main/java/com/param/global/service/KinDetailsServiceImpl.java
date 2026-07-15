package com.param.global.service;

import java.util.Iterator;

import javax.transaction.Transactional;

import com.param.global.model.KinDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.MortuaryDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class KinDetailsServiceImpl implements IKinDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(KinDetailsServiceImpl.class);


	@Autowired
	private IKinDetailsDao iKinDetailsDao;
	
	@Transactional
	@Override
	public Response saveKinDetails(AdmissionDto admission) throws ApplicationException {
		try {
			Integer kinId = null; 	//used for death registration
			Iterator<KinDetailsDto> itr = admission.getKinArray().iterator();
			 
			  while (itr.hasNext()) {
				  KinDetailsDto newkinDetailsDto =new KinDetailsDto();
			    
				  KinDetailsDto obj = itr.next();
					  newkinDetailsDto.setOrganizationId(admission.getOrganizationId());
					  newkinDetailsDto.setUnitId(admission.getUnitId());
					  newkinDetailsDto.setAdmissionId(admission.getAdmissionId());
					  newkinDetailsDto.setVisitTypeId(admission.getVisitTypeId());
					  newkinDetailsDto.setPrefixId(obj.getPrefixId());
					  newkinDetailsDto.setKinName(obj.getKinName());
					  newkinDetailsDto.setRelationId(obj.getRelationId());
					  newkinDetailsDto.setMobileNo(obj.getMobileNo());
					  newkinDetailsDto.setPhoneNo(obj.getPhoneNo());
					  newkinDetailsDto.setIdentificationId(obj.getIdentificationId());
					  newkinDetailsDto.setIdentificationNo(obj.getIdentificationNo());
					  newkinDetailsDto.setExpiry(obj.getExpiry());
					  newkinDetailsDto.setIsGuarantor(obj.getIsGuarantor());
					  newkinDetailsDto.setAddress(obj.getAddress());
					  newkinDetailsDto.setUpdatedBy(admission.getCreatedBy());
					  newkinDetailsDto.setCreatedBy(admission.getCreatedBy());
					  newkinDetailsDto.setCreatedDate(admission.getCreatedDate());
					  newkinDetailsDto.setUpdatedDate(admission.getCreatedDate());
					  newkinDetailsDto.setStatus(obj.getStatus());
					  newkinDetailsDto.setCountryId(obj.getCountryId());
					  newkinDetailsDto.setStateId(obj.getStateId());
					  newkinDetailsDto.setDistrictId(obj.getDistrictId());
					  newkinDetailsDto.setCityId(obj.getCityId());
					  newkinDetailsDto.setAreaId(obj.getAreaId());
					  newkinDetailsDto.setPostCode(obj.getPostCode());
					  newkinDetailsDto.setPatientId(admission.getPatientId());
					  newkinDetailsDto.settPatientId(admission.gettPatientId());
					  newkinDetailsDto.setdPatientId(admission.getdPatientId());
					  Response res=iKinDetailsDao.saveKinDetails(newkinDetailsDto);
					  kinId=(Integer)res.getObject();	//need kinId for death registration !!
			     }
			  return new Response(SUCCESS, null, null, null, kinId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response getKinDetailsByPatientId(Integer patientId, Integer unitId, Integer orgId) throws ApplicationException {
		try {
				return iKinDetailsDao.getKinDetailsByPatientId(patientId,unitId, orgId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getKinDetailsById(Integer kinDetailsId) throws ApplicationException {
		try {
			return iKinDetailsDao.getKinDetailsById(kinDetailsId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response changeKinDetailsStatus(Integer kinDetailsId, Character status) throws ApplicationException {
    try {
		return iKinDetailsDao.changeKinDetailsStatus(kinDetailsId, status);	
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getkinDeatilsForMortuaryAllocation(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			return iKinDetailsDao.getkinDeatilsForMortuaryAllocation(mortuaryDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateKinDeatilsForMortuaryAllocation(AdmissionDto admission) throws ApplicationException {
		try {
			Integer kinId = null; 	//used for death registration
			Iterator<KinDetailsDto> itr = admission.getKinArray().iterator();
			KinDetails kinDetails= new KinDetails();
			while (itr.hasNext()) {
				KinDetailsDto newkinDetailsDto =new KinDetailsDto();

				KinDetailsDto obj = itr.next();
				newkinDetailsDto.setOrganizationId(admission.getOrganizationId());
				newkinDetailsDto.setKinDetailsId(obj.getKinDetailsId());
				newkinDetailsDto.setUnitId(admission.getUnitId());
				newkinDetailsDto.setPrefixId(obj.getPrefixId());
				newkinDetailsDto.setKinName(obj.getKinName());
				newkinDetailsDto.setRelationId(obj.getRelationId());
				newkinDetailsDto.setMobileNo(obj.getMobileNo());
				newkinDetailsDto.setPhoneNo(obj.getPhoneNo());
				newkinDetailsDto.setIdentificationId(obj.getIdentificationId());
				newkinDetailsDto.setIdentificationNo(obj.getIdentificationNo());
				newkinDetailsDto.setExpiry(obj.getExpiry());
				newkinDetailsDto.setIsGuarantor(obj.getIsGuarantor());
				newkinDetailsDto.setAddress(obj.getAddress());
				newkinDetailsDto.setUpdatedBy(admission.getCreatedBy());
				newkinDetailsDto.setUpdatedDate(admission.getCreatedDate());
				newkinDetailsDto.setStatus(obj.getStatus());
				newkinDetailsDto.setCountryId(obj.getCountryId());
				newkinDetailsDto.setStateId(obj.getStateId());
				newkinDetailsDto.setDistrictId(obj.getDistrictId());
				newkinDetailsDto.setCityId(obj.getCityId());
				newkinDetailsDto.setAreaId(obj.getAreaId());
				newkinDetailsDto.setPostCode(obj.getPostCode());
				newkinDetailsDto.setPatientId(admission.getPatientId());
				newkinDetailsDto.settPatientId(admission.gettPatientId());
				newkinDetailsDto.setdPatientId(admission.getdPatientId());
				Response res=iKinDetailsDao.updateKinDeatilsForMortuaryAllocation(newkinDetailsDto);
				kinDetails= (KinDetails)res.getObject();
			}
			return new Response(SUCCESS, COMMON_UPDATE, null, null, kinDetails);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
