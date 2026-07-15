package com.param.lis.unit.service;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dao.ISerologyTestFindingMasterDao;
import com.param.lis.unit.dto.SerologyTestFindingMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SerologyTestMasterFindingServiceImpl implements ISerologyTestFindingMasterService, ICommonConstants, IError {
	@Autowired
	private ISerologyTestFindingMasterDao iSerologyTestFindingMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response saveSerologyTestMaster(SerologyTestFindingMasterDto serologyTestMasterDto) throws ApplicationException {
		try {
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SEROLOGY_BY_CODE")
					.setParameter("code", serologyTestMasterDto.getSerologyCode().trim().toLowerCase())
					.setParameter("orgId", serologyTestMasterDto.getOrgId())
					.setParameter("unitId", serologyTestMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			
			Response  res = iSerologyTestFindingMasterDao.checkForUniqueSerologyTestMasterDesc(serologyTestMasterDto);
			if (departmentMat != null) {
				return new Response(ERROR, ERR_500, SEROLOGY_TEST_CODE_EXISIS, null, null);
			}else if(res.getListObject()!=null && res.getListObject().size()>0) {
				return new Response(ERROR, ERR_500, SEROLOGY_TEST_NAME_EXISIS, null, null);
			}else {
				serologyTestMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(serologyTestMasterDto.getUnitId()).getTime());
				return iSerologyTestFindingMasterDao.saveSerologyTestMaster(serologyTestMasterDto);
			}

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSerologyTestMasterById(Integer orgId, Integer unitId, Integer serologyId)
			throws ApplicationException {
		try {
			return iSerologyTestFindingMasterDao.getSerologyTestMasterById(orgId, unitId, serologyId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateSerologyMaster(SerologyTestFindingMasterDto serologyTestMasterDto) throws ApplicationException {

		try {
			GlobalCommonDto SeroTestMasterDto = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_SEROLOGY_BY_CODE")
					.setParameter("code", serologyTestMasterDto.getSerologyCode().trim().toLowerCase())
					.setParameter("orgId", serologyTestMasterDto.getOrgId())
					.setParameter("unitId", serologyTestMasterDto.getUnitId())
					.setParameter("serologyId", serologyTestMasterDto.getSerologyId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			Response  res = iSerologyTestFindingMasterDao.updateUniqueSerologyTestMasterDesc(serologyTestMasterDto);
			
			if (SeroTestMasterDto != null) {
				return new Response(ERROR, ERR_500, SEROLOGY_TEST_CODE_EXISIS, null, null);
			}else if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response(ERROR, ERR_500, SEROLOGY_TEST_NAME_EXISIS, null, null);
			}else {
				//System.out.println(TimeZoneComponent.getDateByzone(unitId).getTime());
				serologyTestMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(serologyTestMasterDto.getUnitId()).getTime());
				return iSerologyTestFindingMasterDao.updateSerologyMaster(serologyTestMasterDto);
			}

		} catch (Exception e) {
			
			throw e;
		}

	}

	@Override
	@Transactional
	public Response ActivateInactivateSerologyTestMaster(Integer orgId, Integer serologyId, Character serologyStatus)
			throws ApplicationException {
		try {
			return iSerologyTestFindingMasterDao.ActivateInactivateSerologyMaster(orgId, serologyId, serologyStatus);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listSerologyMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iSerologyTestFindingMasterDao.listSerologyMaster(orgId, unitId, offset, recordPerPage);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalSerologyMasterRecord(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iSerologyTestFindingMasterDao.getToTalSerologyMasterRecord(orgId, unitId);
		} catch (Exception e) {
			
			throw e;
		}
	}
}
