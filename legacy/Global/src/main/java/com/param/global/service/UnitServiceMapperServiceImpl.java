package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IServiceMasterDao;
import com.param.global.dao.ISpecialityMasterDao;
import com.param.global.dao.IUnitServiceMapperDao;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.model.UnitServiceMapperId;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitServiceMapperServiceImpl implements IUnitServiceMapperService, ICommonConstants{
	final static Logger logger = Logger.getLogger(UnitServiceMapperServiceImpl.class);


	@Autowired
	private IUnitServiceMapperDao iUnitServiceMapperDao;
	
	@Autowired
	private IServiceMasterDao iserviceMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	private ISpecialityMasterDao iSpecialityMasterDao;
	 
	@Autowired
	IMPackageMasterDao iMPackageMasterDao;
	
	@Override
	@Transactional
	public Response saveUnitServiceMapper(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {
		try {
			UnitServiceMapperId unitServiceMapperId = null;
			for (UnitServiceMapperDto mapperDto : unitServiceMapperDto.getListUnitServiceMapperDto()) {
				unitServiceMapperId = new UnitServiceMapperId();
				unitServiceMapperId.setServiceId(mapperDto.getServiceId());
				unitServiceMapperId.setUnitId(unitServiceMapperDto.getUnitId());
				mapperDto.setUnitServiceMapperId(unitServiceMapperId);
				mapperDto.setStatus(unitServiceMapperDto.getStatus());
				mapperDto.setOrgnisationId(unitServiceMapperDto.getOrgnisationId());
				mapperDto.setUnitId(unitServiceMapperDto.getUnitId());
				mapperDto.setCreatedBy(unitServiceMapperDto.getCreatedBy());
				mapperDto.setUpdatedBy(unitServiceMapperDto.getUpdatedBy());
				mapperDto.setCreatedDate(unitServiceMapperDto.getCreatedDate());
				mapperDto.setUpdatedDate(unitServiceMapperDto.getUpdatedDate());
				iUnitServiceMapperDao.saveUnitServiceMapper(mapperDto);
                iUnitServiceMapperDao.saveUnitServiceBasePrice(mapperDto);
				

			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchServiceMasterBySpecialitySubSplecialityUnitAndOrg(String searchKeyword, Integer specialityId, Integer subSpecialityId, Integer unitId, Integer orgId)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchServiceMasterBySpecialitySubSplecialityUnitAndOrg(searchKeyword, specialityId, subSpecialityId, unitId, orgId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.getMappedServicesListByUnit(unitServiceMapperDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateUnitServiceMapperStatus(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.updateUnitServiceMapperStatus(unitServiceMapperDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCountOfMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.getCountOfMappedServicesListByUnit(unitServiceMapperDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchServicesByNameAndCode(serviceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchUnitServiceByMultipleSpecialityAndSubSpeciality(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchUnitServiceByMultipleSpecialityAndSubSpeciality(unitServiceMapperDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchActiveServices(ServiceSearchReqDto serviceSearchReqDto)
			throws ApplicationException {
		try{
			return iUnitServiceMapperDao.searchActiveServices(serviceSearchReqDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchProcedure(int orgId, int unitId,int specialityId, String searchKeyWord) throws ApplicationException {
		try{
			return iUnitServiceMapperDao.searchProcedure(orgId, unitId,specialityId, searchKeyWord);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchUnitServiceMapperWithoutSpecialitySubSpeciality(String searchKeyword, Integer unitId,
			Integer orgId) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchServiceMasterByWithoutSpecialitySubSpeciality(searchKeyword,unitId, orgId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchServiceMasterBySpecialitySubSplecialityOrgUnitAndGender(String searchKeyword,
			Integer specialityId, Integer subSplecialityId, Integer unitId, Integer orgId, Integer gender)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchServiceMasterBySpecialitySubSplecialityOrgUnitAndGender(searchKeyword, specialityId, subSplecialityId, unitId, orgId,gender);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getMappedServicesListByUnitSearch(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.getMappedServicesListByUnitSearch(unitServiceMapperDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkServiceMappedWithTest(ServiceMasterDto dto,Integer labSpecialityId) throws ApplicationException {
		try {
			Response ordRes =iMPackageMasterDao.getPkgOrderDetailsIdByServiceId(dto);
		
			if(labSpecialityId!=null && labSpecialityId>0)
			{
				Response res = iUnitServiceMapperDao.checkServiceMappedWithTest(dto);
				if(res.getObject()!=null)
				{
				   UnitServiceMasterDetailsDto usdDto = (UnitServiceMasterDetailsDto) res.getObject();
				   if(usdDto.getIsPanel()=='Y')
				   {
					   return new Response(SUCCESS, ordRes.getCode(), null, null, 1);
				   }
				   else if(usdDto.getTestId()!=null && usdDto.getTestId()>0)
				   {
					   return new Response(SUCCESS, ordRes.getCode(), null, null, 1);
				   }
				   else {
					   return new Response(SUCCESS, null, null, null, 0);
				   }
				}
			}
			else {
				return new Response(SUCCESS, ordRes.getCode(), null, null, 1);
			}
			
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response searchItemByNameAndCode(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchItemByNameAndCode(serviceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchServiceByNameOrCode(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchServiceByNameOrCode(unitServiceMapperDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response serviceMasterForUnitApplicationService(ServiceMasterDto serviceMasterDto)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.serviceMasterForUnitApplicationService(serviceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response serviceAreaBysearchKeyword(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.serviceAreaBysearchKeyword(areaMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkServiceWithTestParameter(ServiceMasterDto dto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.checkServiceWithTestParameter(dto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchServicesByNameAndCodeForEasyEMR(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			return iUnitServiceMapperDao.searchServicesByNameAndCodeForEasyEMR(serviceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


}
