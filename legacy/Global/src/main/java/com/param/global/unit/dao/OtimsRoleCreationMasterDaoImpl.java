package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.OtimsRoleMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.OtimsRoleMasterDto;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class OtimsRoleCreationMasterDaoImpl extends GenericDao<OtimsRoleMaster, Integer> implements IOtimsRoleCreationMasterDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(OtimsRoleCreationMasterDaoImpl.class);


	public OtimsRoleCreationMasterDaoImpl() {
		super(OtimsRoleMaster.class);

	}

	@Override
	public Response saveOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			OtimsRoleMaster otimsRoleMstr = new OtimsRoleMaster(); 
			
			if(otimsRoleMasterDto.getRoleName()!=null && otimsRoleMasterDto.getOrgId()!=null && otimsRoleMasterDto.getUnitId()!=null)
			{
				otimsRoleMstr = (OtimsRoleMaster)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_OTIMS_ROLE")
					.setParameter("orgId", otimsRoleMasterDto.getOrgId())
					.setParameter("unitId", otimsRoleMasterDto.getUnitId())
					.setParameter("roleName", otimsRoleMasterDto.getRoleName())
					.setResultTransformer(Transformers.aliasToBean(OtimsRoleMaster.class)).uniqueResult();
			
			}
			
			if(otimsRoleMstr!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			}else {
				OtimsRoleMaster mstr = new OtimsRoleMaster(); 
				mstr.setRoleName(otimsRoleMasterDto.getRoleName());
				mstr.setStatus(otimsRoleMasterDto.getStatus());
				mstr.setOrgId(otimsRoleMasterDto.getOrgId());
				mstr.setUnitId(otimsRoleMasterDto.getUnitId());
				mstr.setCreatedBy(otimsRoleMasterDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(otimsRoleMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				mstr.setUpdatedBy(otimsRoleMasterDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(otimsRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				sessionFactory.getCurrentSession().save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			
			}
		} catch (HibernateException he) {
		logger.error("saveOtimsRoleMaster failed", he);
		} catch (Exception e) {
		logger.error("saveOtimsRoleMaster failed", e);
		}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllOtimsRoleMasterList(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			List<OtimsRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_OTIMS_ROLE_MASTER_LIST")
					.setParameter("unitId", otimsRoleMasterDto.getUnitId())
					.setParameter("organisationId", otimsRoleMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(OtimsRoleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getAllOtimsRoleMasterList failed", he);
		} catch (Exception e) {
		logger.error("getAllOtimsRoleMasterList failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			OtimsRoleMaster otimsRoleMstr = new OtimsRoleMaster(); 
			
			if(otimsRoleMasterDto.getRoleName()!=null && otimsRoleMasterDto.getOrgId()!=null && otimsRoleMasterDto.getUnitId()!=null)
			{
				otimsRoleMstr = (OtimsRoleMaster)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_OTIMS_ROLE")
					.setParameter("orgId", otimsRoleMasterDto.getOrgId())
					.setParameter("unitId", otimsRoleMasterDto.getUnitId())
					.setParameter("roleName", otimsRoleMasterDto.getRoleName())
					.setResultTransformer(Transformers.aliasToBean(OtimsRoleMaster.class)).uniqueResult();
			
			}
			
			if(otimsRoleMstr!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			}else {
			OtimsRoleMaster mstr = new OtimsRoleMaster(); 
				mstr = findById(otimsRoleMasterDto.getRoleId());
				mstr.setRoleName(otimsRoleMasterDto.getRoleName());
				mstr.setUpdatedBy(otimsRoleMasterDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(otimsRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}
		} catch (HibernateException he) {
		logger.error("updateOtimsRoleMaster failed", he);
		} catch (Exception e) {
		logger.error("updateOtimsRoleMaster failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOtimsRoleMasterStatus(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			
			
			OtimsRoleMaster mstr = new OtimsRoleMaster(); 
				mstr = findById(otimsRoleMasterDto.getRoleId());
				mstr.setStatus(otimsRoleMasterDto.getStatus());
				mstr.setUpdatedBy(otimsRoleMasterDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(otimsRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			
		} catch (HibernateException he) {
		logger.error("updateOtimsRoleMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateOtimsRoleMasterStatus failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
}
