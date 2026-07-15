package com.param.global.service;

import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICompanyMasterDao;
import com.param.global.dto.CompanyContarctGradeMapperDto;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.service.global.model.CompanyContarctGradeMapper;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@Transactional
@SuppressWarnings({"rawtypes","unchecked"})
public class CompanyMasterServiceImpl implements ICompanyMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(CompanyMasterServiceImpl.class);


	@Autowired
	private ICompanyMasterDao iCompanyMasterDao;
	@Autowired
    IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	@Autowired
    DocUploadUtility docUploadUtility;

	private Cache<Integer, List<CompanyMasterDto>> companyMasterCache;

	@PostConstruct
	public void initCache() {
		companyMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	
	public Response getActiveCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> responseCache = companyMasterCache.getIfPresent(companyMasterDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iCompanyMasterDao.getActiveCompanyMasterList(companyMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				companyMasterCache.put(companyMasterDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		}catch (Exception e) {
		logger.error("getActiveCompanyMasterList failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException {
		Response res=iCompanyMasterDao.getCompanyMasterListByName(companyMasterDto);
		try {
			if(res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response saveRes = iCompanyMasterDao.saveCompanyMaster(companyMasterDto);
				if(saveRes.getStatus().equals(SUCCESS)){
					companyMasterCache.invalidate(companyMasterDto.getUnitId());
				}
				return saveRes;
			}
			
		}catch (Exception e) {
		logger.error("saveCompanyMaster failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyMasterDao.getCompanyMasterList(companyMasterDto);
		}catch (Exception e) {
		logger.error("getCompanyMasterList failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCompanyMasterListById(Integer unitId, Integer orgId, Integer companyId)
			throws ApplicationException {
		try {
			return iCompanyMasterDao.getCompanyMasterListById(unitId,orgId,companyId);
		}catch (Exception e) {
		logger.error("getCompanyMasterListById failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response updateCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException {
		Response res2=iCompanyMasterDao.getCompanyMasterListByNameNotById(companyMasterDto);
		try {
			if(res2.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
					Response updateRes= iCompanyMasterDao.updateCompanyMaster(companyMasterDto);
					if(updateRes.getStatus().equals(SUCCESS)){

						companyMasterCache.invalidate(companyMasterDto.getUnitId());
						Response res1=iCompanyMasterDao.updateStatusOfCompanyContarctGradeMapper(companyMasterDto);
						if(res1.getStatus().equals("success")){
							if(companyMasterDto.getListCompanyContarctGradeMapperDto()!=null && companyMasterDto.getListCompanyContarctGradeMapperDto().size()>0){
							ListIterator<CompanyContarctGradeMapperDto> itr=companyMasterDto.getListCompanyContarctGradeMapperDto().listIterator();
								while(itr.hasNext()){
										CompanyContarctGradeMapper companyContarctGradeMapper=new CompanyContarctGradeMapper();
										CompanyContarctGradeMapperDto companyContarctGradeMapperDto=itr.next();
										
										companyContarctGradeMapper.setCompanyId(companyMasterDto.getCompanyId());
										companyContarctGradeMapper.setContractGradeMasterId(companyContarctGradeMapperDto.getContractGradeMasterId());
										companyContarctGradeMapper.setOrgId(companyMasterDto.getOrganizationId());
										companyContarctGradeMapper.setStatus(companyMasterDto.getStatus());
										companyContarctGradeMapper.setOrgUnitId(companyMasterDto.getUnitId());
										companyContarctGradeMapper.setCreatedBy(companyMasterDto.getUpdatedBy());
										companyContarctGradeMapper.setUpdatedBy(companyMasterDto.getUpdatedBy());
										companyContarctGradeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(companyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
										companyContarctGradeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(companyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
										iCompanyMasterDao.saveCompanyContarctGradeMapper(companyContarctGradeMapper);
									}
							   }
							}
						}
			}
			return new Response(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, companyMasterDto.getCompanyId());
			
		}catch (Exception e) {
		logger.error("updateCompanyMaster failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response countOfCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyMasterDao.countOfCompanyMasterList(companyMasterDto);
		}catch (Exception e) {
		logger.error("countOfCompanyMasterList failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateCompanyMasterStatus(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			Response saveRes = iCompanyMasterDao.updateCompanyMasterStatus(companyMasterDto);
			if(saveRes.getStatus().equals(SUCCESS) && saveRes.getObject() != null) {
				Integer filterId = (Integer) saveRes.getObject();
				companyMasterCache.invalidate(companyMasterDto.getUnitId() != null ? companyMasterDto.getUnitId() : filterId);
			}
			return saveRes;
		}catch (Exception e) {
		logger.error("updateCompanyMasterStatus failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCompanyMasterListByName(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyMasterDao.getCompanyMasterListByName(companyMasterDto);
		}catch (Exception e) {
		logger.error("getCompanyMasterListByName failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCompanyMasterListByNameNotById(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyMasterDao.getCompanyMasterListByNameNotById(companyMasterDto);
		}catch (Exception e) {
		logger.error("getCompanyMasterListByNameNotById failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCompanyContractGradeByCompanyId(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyMasterDao.getCompanyContractGradeByCompanyId(companyMasterDto);
		}catch (Exception e) {
		logger.error("getCompanyContractGradeByCompanyId failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getActiveTPA(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyMasterDao.getActiveTPA(companyMasterDto);
		}catch (Exception e) {
		logger.error("getActiveTPA failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveCompanyImage(int orgId, int unitId, int companyId, MultipartFile inputFile)
			throws ApplicationException {
		try{
			if(orgId>0&&unitId>0&&companyId>0&&!inputFile.isEmpty())
		      {
		        Response unitRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(orgId);
		        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
		        String imagePath =  docUploadUtility.uploadCompanyImage(inputFile, organizationMasterDto,unitId,companyId);
		        CompanyMasterDto dto = new CompanyMasterDto();
		        dto.setCompanyId(companyId);
		        dto.setOrganizationId(orgId);
		        dto.setUnitId(unitId);
		        dto.setReportPath(imagePath);
		        return iCompanyMasterDao.updateCompanyImage(dto);
		      }
		    return null;
		}catch(Exception e){
		logger.error("saveCompanyImage failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	

}
