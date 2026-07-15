package com.param.otims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.otims.dao.IOTChecklistDetailsDao;
import com.param.global.otims.dao.IOtCompletionTimeSheetDao;
import com.param.global.otims.dao.IProcedureOrderEquipmentDao;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.IntraOperativeImageDto;
import com.param.global.otims.dto.OTChecklistDetailsDto;
import com.param.global.otims.dto.OtCompletionTimeSheetDto;
import com.param.global.otims.dto.WhoChecklistDto;
import com.param.otims.dao.IIntraOperativeDetailsDao;
import com.param.otims.dao.IIntraOperativeImageDao;
import com.param.otims.dao.IWhoChecklistDao;
import com.param.otims.model.IntraOperativeDetails;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes"})
public class IntraOperativeDetailsServiceImpl  implements IIntraOperativeDetailsService, ICommonConstants ,IBillingCommonConstants{

	@Autowired 
	IIntraOperativeDetailsDao iIntraOperativeDetailsDao;
	
	@Autowired 
	IWhoChecklistDao oWhoChecklistDao;
	
	@Autowired
	IOtCompletionTimeSheetDao iOtCompletionTimeSheetDao;
	
	@Autowired
	IProcedureOrderEquipmentDao iProcedureOrderEquipmentDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Autowired
	IIntraOperativeImageDao iIntraOperativeImageDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	IOTChecklistDetailsDao iOTChecklistDetailsDao;
	
	@Autowired
	IWhoChecklistDao iWhoChecklistDao;

	
	@Override
	public Response saveIntraOperativeDetails(IntraOperativeDetailsDto intraOperativeDetailsDto) {
		try {
			Response res =  iIntraOperativeDetailsDao.savePreOperativeDetails(intraOperativeDetailsDto);
			if(res.getObject() != null ) {
				
				IntraOperativeDetails mstrDto = (IntraOperativeDetails) res.getObject();
				
				for (WhoChecklistDto dto : intraOperativeDetailsDto.getWhoChecklistDtoList()) {
					dto.setIntraOperative(mstrDto.getIntraOperativeId());
					oWhoChecklistDao.saveWhoChecklist(dto);
				}
				iOtCompletionTimeSheetDao.saveOrUpdateOtCompletionTimeSheet(intraOperativeDetailsDto.getOtCompletionTimeSheetDto());
				iProcedureOrderEquipmentDao.saveProcedureOrderEquipment(intraOperativeDetailsDto.getProcedureOrderEquipmentList(),intraOperativeDetailsDto.getProReqId(),intraOperativeDetailsDto.getProcedureOrderId());
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, res.getObject());
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response saveIntraImage(IntraOperativeImageDto dto) throws ApplicationException {
		try {
			
			if (dto.getUnitId() > 0) 
			{
				Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dto.getOrganizationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
				
					docPath = docUploadUtility.uploadIntraOperativeImage(dto.getFile(),
							organizationMasterDto.getOrganizationFolder(), dto);
				
					dto.setImageUrl(docPath);
					iIntraOperativeImageDao.saveIntraImage(dto);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getIntraOperativeByProReqId(IntraOperativeDetailsDto dto) throws ApplicationException {
		try {
			IntraOperativeDetailsDto intraOperativeDetailsDto = new IntraOperativeDetailsDto();
			
			Response res =  iIntraOperativeDetailsDao.getIntraOperativeByProReqId(dto);
			
			if(res.getObject() != null ) {
				
				intraOperativeDetailsDto = (IntraOperativeDetailsDto)res.getObject();
				Response resCheck =  iWhoChecklistDao.getWhoChecklistByIntraOperativeId(intraOperativeDetailsDto);
				
				OtCompletionTimeSheetDto otTimeSheet1 = new OtCompletionTimeSheetDto();
				Response timeSheetRes = iOtCompletionTimeSheetDao.getOtTimeSheetByProReq(dto.getProReqId());
				otTimeSheet1 = (OtCompletionTimeSheetDto) timeSheetRes.getObject();
				intraOperativeDetailsDto.setOtCompletionTimeSheetDto(otTimeSheet1);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, resCheck.getListObject(), intraOperativeDetailsDto);
				
			}else {
						OTChecklistDetailsDto  oTChecklistDetailsDto = new OTChecklistDetailsDto();
						oTChecklistDetailsDto.setProReqId(dto.getProReqId());
						return iOTChecklistDetailsDao.getIntraOperativeChecklistByProReqId(oTChecklistDetailsDto); 
			}
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response saveIntraVideo(IntraOperativeImageDto dto) throws ApplicationException {
		try {
			
			if (dto.getUnitId() > 0) 
			{
				Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dto.getOrganizationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
				
					docPath = docUploadUtility.uploadIntraOperativeImage(dto.getFile(),
							organizationMasterDto.getOrganizationFolder(), dto);
				
					dto.setImageUrl(docPath);
					iIntraOperativeDetailsDao.saveIntraVideo(dto);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

}
