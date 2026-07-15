package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistItemMasterDto;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.otims.model.ChecklistMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ChecklistMasterDaoImpl extends GenericDao<ChecklistMaster, Integer> implements IChecklistMasterDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ChecklistMasterDaoImpl.class);

	public ChecklistMasterDaoImpl() {
		super(ChecklistMaster.class);
	}

	@Override
	public Response saveChecklistMaster(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {
			
			ChecklistMasterDto dto = new ChecklistMasterDto();
			if (checklistMasterDto.getCode() != null && checklistMasterDto.getDescription() != null) { // dilip
				if (checklistMasterDto.getChecklistMasterId() == null) {
					dto = (ChecklistMasterDto) sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_MASTER")
							.setParameter("organizationId", checklistMasterDto.getOrganizationId())
							.setParameter("unitId", checklistMasterDto.getUnitId())
							.setParameter("code", checklistMasterDto.getCode().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();
					
					if(dto!=null)
					{
						return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Code Already Exist", null, null);
					}else {
						
						dto = (ChecklistMasterDto) sessionFactory.getCurrentSession()
								.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_MASTER_DESCRIPTION")
								.setParameter("organizationId", checklistMasterDto.getOrganizationId())
								.setParameter("unitId", checklistMasterDto.getUnitId())
								.setParameter("description", checklistMasterDto.getDescription().toLowerCase())
								.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();
						
						if(dto!=null)
						{
							return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Description Already Exist", null, null);
						}
					}
				} else {
					dto = (ChecklistMasterDto) sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_MASTER_BY_NOT_BY_ID")
							.setParameter("organizationId", checklistMasterDto.getOrganizationId())
							.setParameter("unitId", checklistMasterDto.getUnitId())
							.setParameter("checklistMasterId", checklistMasterDto.getChecklistMasterId())
							.setParameter("code", checklistMasterDto.getCode().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();
					if(dto!=null) {
						return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Code Already Exist", null, null);
					}else {
						dto = (ChecklistMasterDto) sessionFactory.getCurrentSession()
								.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_MASTER_DESCRIPTION_BY_NOT_BY_ID")
								.setParameter("organizationId", checklistMasterDto.getOrganizationId())
								.setParameter("unitId", checklistMasterDto.getUnitId())
								.setParameter("checklistMasterId", checklistMasterDto.getChecklistMasterId())
								.setParameter("description", checklistMasterDto.getDescription().toLowerCase())
								.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();
						if(dto!=null) {
							return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Description Already Exist", null, null);
						}
					}
				}
			}
			if(dto!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			}
			
			Integer stage = checklistMasterDto.getStageId();
			ChecklistMaster mstr = new ChecklistMaster();
			mstr.setCode(checklistMasterDto.getCode());
			mstr.setDescription(checklistMasterDto.getDescription());
			mstr.setRoleId(checklistMasterDto.getRoleId());
			mstr.setStageId(stage);
			mstr.setIsWho(checklistMasterDto.getIsWho());
			mstr.setCreatedBy(checklistMasterDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(checklistMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(checklistMasterDto.getOrganizationId());
			mstr.setStatus(checklistMasterDto.getStatus());
			mstr.setUnitId(checklistMasterDto.getUnitId());
			mstr.setUpdatedBy(checklistMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(checklistMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			if(checklistMasterDto.getChecklistMasterId()!=null) {
				mstr.setChecklistMasterId(checklistMasterDto.getChecklistMasterId());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}else {
				save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}
				} catch (HibernateException he) {
					logger.error(he.getMessage(), he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response updateChecklistMasterStatus(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {
			ChecklistMaster mstr = findById(checklistMasterDto.getChecklistMasterId());
			mstr.setStatus(checklistMasterDto.getStatus());
			mstr.setUpdatedBy(checklistMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(checklistMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllChecklistMasterList(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {
			List<ChecklistMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_MASTER_LIST")
					.setParameter("organizationId", checklistMasterDto.getOrganizationId())
					.setParameter("unitId", checklistMasterDto.getUnitId())
                    .setFirstResult(checklistMasterDto.getOffset() != null ? checklistMasterDto.getOffset()  : 0)
                    .setMaxResults(checklistMasterDto.getNoOfRecordsPerPage() != null ? checklistMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getChecklistMasterByStageId(ChecklistMasterDto dto) throws ApplicationException {
		try {
			List<ChecklistMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_MASTER_LIST_BY_STAGE_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("stageId", dto.getStageId())
					.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllChecklistMasterListCount(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {
			List<ChecklistMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_MASTER_LIST")
					.setParameter("organizationId", checklistMasterDto.getOrganizationId())
					.setParameter("unitId", checklistMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, list.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
