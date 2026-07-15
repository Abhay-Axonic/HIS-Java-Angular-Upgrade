package com.param.global.otims.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dao.ISubSequentDetailsDao;
import com.param.global.otims.dao.ISubSequentDetailsInfoDao;
import com.param.global.otims.dao.ISubSequentProcedureConfigDao;
import com.param.global.otims.dto.SubSequentDetailsDto;
import com.param.global.otims.dto.SubSequentDetailsInfoDto;
import com.param.global.otims.dto.SubSequentProcedureConfigDto;
import com.param.otims.model.SubSequentDetails;
import com.param.otims.model.SubSequentProcedureConfig;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SubSequentProcedureConfigServiceImpl implements ISubSequentProcedureConfigService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(SubSequentProcedureConfigServiceImpl.class);

	@Autowired
	ISubSequentProcedureConfigDao iSubSequentProcedureConfigDao;
	
	@Autowired
	ISubSequentDetailsDao iSubSequentDetailsDao;
	
	@Autowired
	ISubSequentDetailsInfoDao iSubSequentDetailsInfoDao;
	
	@Override
	public Response saveSubSequentProcedureConfig(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException{
		try{
			Response response1 =null;
			Response response=iSubSequentProcedureConfigDao.getSubSequentProcedureConfigByIdNotName(subSequentProcedureConfigDto);
					
			if (response.getListObject().size()>0 && response.getListObject()!=null)
				{
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
					
				}else{
					
					 response1 =  iSubSequentProcedureConfigDao.saveSubSequentProcedureConfig(subSequentProcedureConfigDto);
					 
					 if(response1.getObject()!=null) {
						 SubSequentProcedureConfig master = (SubSequentProcedureConfig) response1.getObject();
						 
						 for (SubSequentDetailsDto dto : subSequentProcedureConfigDto.getSubSequentDetailsList()) {
							 dto.setCreatedBy(subSequentProcedureConfigDto.getCreatedBy());
							 dto.setCreatedDate(subSequentProcedureConfigDto.getCreatedDate());
							 dto.setOrgId(subSequentProcedureConfigDto.getOrgId());
							 dto.setSubSequentProcConfigId(master.getSubSequentProcConfigId());
							 dto.setUnitId(subSequentProcedureConfigDto.getUnitId());
							 dto.setUpdatedBy(subSequentProcedureConfigDto.getUpdatedBy());
							 dto.setUpdatedDate(subSequentProcedureConfigDto.getUpdatedDate());
							 
							 Response res = iSubSequentDetailsDao.saveSubSequentDetails(dto);
							 if(res.getObject()!=null) {
								 SubSequentDetails mster=(SubSequentDetails) res.getObject();
								 
								 if(dto.getSubSequentDetailsInfoList() !=null && dto.getSubSequentDetailsInfoList().size()>0) {
									 
									 for (SubSequentDetailsInfoDto info : dto.getSubSequentDetailsInfoList()) {
										 info.setSubSequentDetailsId(mster.getSubSequentDetailsId());
										 info.setCreatedBy(subSequentProcedureConfigDto.getCreatedBy());
										 info.setCreatedDate(subSequentProcedureConfigDto.getCreatedDate());
										 info.setOrgId(subSequentProcedureConfigDto.getOrgId());
										 info.setUnitId(subSequentProcedureConfigDto.getUnitId());
										 info.setUpdatedBy(subSequentProcedureConfigDto.getUpdatedBy());
										 info.setUpdatedDate(subSequentProcedureConfigDto.getUpdatedDate());
										 
										 iSubSequentDetailsInfoDao.saveSubSequentDetailsInfo(info); 
									}
								 }
							 }
							 
							 
						}
					 }
				}
			if(subSequentProcedureConfigDto.getSubSequentProcConfigId()!=null) {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);	
			}else {
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);	
			}

		}catch (Exception e) {
				logger.error("saveSubSequentProcedureConfig failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateSubSequentProcedureConfig(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException{
		try{
			Response response=iSubSequentProcedureConfigDao.getSubSequentProcedureConfigByIdNotName(subSequentProcedureConfigDto);
					if (response.getListObject().size()>0 && response.getListObject()!=null)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
					}else{
						return iSubSequentProcedureConfigDao.updateSubSequentProcedureConfig(subSequentProcedureConfigDto);
					}
		}catch (Exception e) {
				logger.error("updateSubSequentProcedureConfig failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateSubSequentProcedureConfigStatus(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException{
		try{
			return iSubSequentProcedureConfigDao.updateSubSequentProcedureConfigStatus(subSequentProcedureConfigDto);

		}catch (Exception e) {
				logger.error("updateSubSequentProcedureConfigStatus failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getSubSequentProcedureConfigList(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException{
		try{
			return iSubSequentProcedureConfigDao.getSubSequentProcedureConfigList(subSequentProcedureConfigDto);

		}catch (Exception e) {
				logger.error("getSubSequentProcedureConfigList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getSubSequentProcedureConfigById(Integer subSequentProcConfigId, Integer orgId, Integer unitId) throws ApplicationException{
		try{
			return iSubSequentProcedureConfigDao.getSubSequentProcedureConfigById(subSequentProcConfigId, orgId, unitId);

		}catch (Exception e) {
				logger.error("getSubSequentProcedureConfigById failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getActiveSubSequentProcedureConfigList(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException{
		try{
			return iSubSequentProcedureConfigDao.getActiveSubSequentProcedureConfigList(subSequentProcedureConfigDto);

		}catch (Exception e) {
				logger.error("getActiveSubSequentProcedureConfigList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getSubSequentProcedureConfigCount(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException{
		try{
			return iSubSequentProcedureConfigDao.getSubSequentProcedureConfigCount(subSequentProcedureConfigDto);

		}catch (Exception e) {
				logger.error("getSubSequentProcedureConfigCount failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getSubSequentProcedureConfigByIdNotName(SubSequentProcedureConfigDto subSequentProcedureConfigDto)
			throws ApplicationException {
		try{
			return iSubSequentProcedureConfigDao.getSubSequentProcedureConfigByIdNotName(subSequentProcedureConfigDto);

		}catch (Exception e) {
				logger.error("getSubSequentProcedureConfigByIdNotName failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	@Transactional
	public Response getSubSequentDetailsListById(SubSequentProcedureConfigDto subSequentProcedureConfigDto)
			throws ApplicationException {
		try{
			List<SubSequentDetailsDto> list= new ArrayList<SubSequentDetailsDto>();
			Response res = iSubSequentProcedureConfigDao.getSubSequentDetailsListById(subSequentProcedureConfigDto);
			if(res.getListObject() != null && res.getListObject().size() >0) {
				list.addAll(res.getListObject());
				
				for (SubSequentDetailsDto subSequentDetailsDto : list) {
					
					List<SubSequentDetailsInfoDto> infoList= new ArrayList<>();
					Response resInfo =  iSubSequentDetailsInfoDao.getSubSequentDetailsInfo(subSequentDetailsDto);
					
					if(resInfo.getListObject() != null && resInfo.getListObject().size()>0) {
						infoList.addAll(resInfo.getListObject());
						
						subSequentDetailsDto.setSubSequentDetailsInfoList(infoList);
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
			
	}


	@Override
	@Transactional
	public Response getSubSequentDetailsListBySpecialityId(SubSequentProcedureConfigDto subSequentProcedureConfigDto)
			throws ApplicationException {
		try{
			return iSubSequentProcedureConfigDao.getSubSequentDetailsListBySpecialityId(subSequentProcedureConfigDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
			
	}


	@Override
	public Response getSubSequentDetailsAndInfoListBySpecialityId(
			SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
		try{
			List<SubSequentDetailsDto> subSequentList = new ArrayList<>();
			Response subSequentRes =  iSubSequentProcedureConfigDao.getSubSequentDetailsAndInfoListBySpecialityId(subSequentProcedureConfigDto);
			if(subSequentRes!=null && subSequentRes.getListObject()!=null && subSequentRes.getListObject().size()>0) {
				 subSequentList = subSequentRes.getListObject();
				for (SubSequentDetailsDto subSequentDetailsDto : subSequentList) {
					if(subSequentDetailsDto.getDiscPercentage()== null || subSequentDetailsDto.getDiscPercentage() == 0) {
						subSequentDetailsDto.setSkipZeroDiscount(true);
						Response subInfoListRes = iSubSequentDetailsInfoDao.getSubSequentDetailsInfo(subSequentDetailsDto);
						if(subInfoListRes!=null && subInfoListRes.getListObject()!=null && subInfoListRes.getListObject().size()>0) {
							List<SubSequentDetailsInfoDto>  subInfoList = subInfoListRes.getListObject();
							subSequentDetailsDto.setSubSequentDetailsInfoList(subInfoList);
						}
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, subSequentList, null);
		}catch (Exception e) {
				logger.error("getSubSequentDetailsAndInfoListBySpecialityId failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
