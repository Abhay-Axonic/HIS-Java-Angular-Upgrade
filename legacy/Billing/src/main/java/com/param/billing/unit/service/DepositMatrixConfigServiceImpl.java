package com.param.billing.unit.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.unit.dao.IDepositMatrixConfigDao;
import com.param.global.service.VerifiedIdentityService;
import com.param.billing.unit.dto.DepositMatrixConfigMasterDto;
import com.param.billing.unit.dto.DepositMatrixReqDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class DepositMatrixConfigServiceImpl  implements IDepositMatrixConfigService, ICommonConstants 
{
	@Autowired
	private IDepositMatrixConfigDao iDepositMatrixConfigDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	@Transactional
	public Response getDepositMatrixList(int orgId, int unitId, int offset, int recordPerPage) {
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		unitId = verifiedIdentityService.getVerifiedUnitId();
		try {
			return iDepositMatrixConfigDao.getDepositMatrixList(orgId, unitId, offset, recordPerPage);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response saveDepositMatrixConfig(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) {
		depositMatrixConfigMasterDto.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
		try {
			Response response=this.checkDepositMatrixExist(depositMatrixConfigMasterDto);
			Date date = TimeZoneComponent.getDateByzone(depositMatrixConfigMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			long count = (long) response.getObject();
			if(count>0L){
				depositMatrixConfigMasterDto.setCreatedDate(dateTime);
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDepositMatrixConfigDao.saveDepositMatrixConfig(depositMatrixConfigMasterDto);
			}
					
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDepositMatrixConfig(int mDepositMatrixId) throws ApplicationException {
		try {
			return iDepositMatrixConfigDao.getDepositMatrixConfig(mDepositMatrixId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateDepositMatrixConfig(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) {
		depositMatrixConfigMasterDto.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
		try {
			Response response=this.checkDepositMatrixExist(depositMatrixConfigMasterDto);
			Date date = TimeZoneComponent.getDateByzone(depositMatrixConfigMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			long count = (long) response.getObject();
			if(count>0L){
				
				depositMatrixConfigMasterDto.setUpdatedDate(dateTime);
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDepositMatrixConfigDao.updateDepositMatrixConfig(depositMatrixConfigMasterDto);
			}
					
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateDepositMatrixConfigStatus(int mDepositMatrixId, char status) throws ApplicationException {
		try {
			return iDepositMatrixConfigDao.updateDepositMatrixConfigStatus(mDepositMatrixId, status);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDepositMatrixConfigCount(int orgId, int unitId) {
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		unitId = verifiedIdentityService.getVerifiedUnitId();
		try {
			return iDepositMatrixConfigDao.getDepositMatrixConfigCount(orgId,unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkDepositMatrixExist(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) {
		depositMatrixConfigMasterDto.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
		try {
			return iDepositMatrixConfigDao.checkDepositMatrixExist(depositMatrixConfigMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDepositMatrixConfigList(DepositMatrixReqDto depositMatrixReqDto) {
		depositMatrixReqDto.setOrgansationId(verifiedIdentityService.getVerifiedOrganizationId());
		try {
			return iDepositMatrixConfigDao.getDepositMatrixConfigList(depositMatrixReqDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	

}
