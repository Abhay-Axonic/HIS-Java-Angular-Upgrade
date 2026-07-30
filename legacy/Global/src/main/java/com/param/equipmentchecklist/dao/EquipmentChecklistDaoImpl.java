package com.param.equipmentchecklist.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.equipmentchecklist.model.EquipmentChecklist;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistMasterDetailsDto;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.otims.model.ChecklistMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class EquipmentChecklistDaoImpl extends GenericDao<EquipmentChecklist, Integer> implements IEquipmentChecklistDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(EquipmentChecklistDaoImpl.class);


	public EquipmentChecklistDaoImpl() {
		super(EquipmentChecklist.class);
	}

	@Override
	public Response saveEquipmentChecklist(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException {
		try {
			
			EquipmentChecklistDto dto = new EquipmentChecklistDto();
			if(equipmentChecklistDto.getChecklistCode()!=null && equipmentChecklistDto.getChecklistName()!=null 
					&& equipmentChecklistDto.getChecklistEquipmentMasterId()==null ) {//added condition by minesh for update EquipmentChecklist.
				dto = (EquipmentChecklistDto) sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MASTER")
							.setParameter("organizationId", equipmentChecklistDto.getOrganizationId())
							.setParameter("unitId", equipmentChecklistDto.getUnitId())
							.setParameter("checklistCode", equipmentChecklistDto.getChecklistCode().toLowerCase())
							.setParameter("checklistName", equipmentChecklistDto.getChecklistName().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistDto.class)).uniqueResult();
			} else {//added by minesh for update EquipmentChecklist.
				dto = (EquipmentChecklistDto) sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MASTER_NOT_ID")
						.setParameter("organizationId", equipmentChecklistDto.getOrganizationId())
						.setParameter("unitId", equipmentChecklistDto.getUnitId())
						.setParameter("checklistEquipmentMasterId", equipmentChecklistDto.getChecklistEquipmentMasterId())
						.setParameter("checklistCode", equipmentChecklistDto.getChecklistCode().toLowerCase())
						.setParameter("checklistName", equipmentChecklistDto.getChecklistName().toLowerCase())
						.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistDto.class)).uniqueResult();
			}//end update EquipmentChecklist.
			
			
			if(dto!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			}
			
			EquipmentChecklist mstr = new EquipmentChecklist();
			mstr.setChecklistCode(equipmentChecklistDto.getChecklistCode());
			mstr.setChecklistName(equipmentChecklistDto.getChecklistName());
			mstr.setCreatedBy(equipmentChecklistDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(equipmentChecklistDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(equipmentChecklistDto.getOrganizationId());
			mstr.setStatus(equipmentChecklistDto.getStatus());
			mstr.setUnitId(equipmentChecklistDto.getUnitId());
			mstr.setUpdatedBy(equipmentChecklistDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(equipmentChecklistDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			if(equipmentChecklistDto.getChecklistEquipmentMasterId()!=null) {
				// BR-121: an id-only lookup has no ownership check -- the DTO's
				// organizationId/unitId (verified by the service layer before
				// this method is called) is otherwise never actually consulted
				// for this update path. Explicitly verify the row being updated
				// really belongs to the verified tenant before proceeding,
				// rather than silently updating regardless.
				//
				// Uses a row-count Criteria query, not findById(), because
				// findById() loads a second, separately-managed entity with the
				// same identifier as `mstr` below -- confirmed live to throw
				// Hibernate's NonUniqueObjectException ("A different object
				// with the same identifier value was already associated with
				// the session") on an otherwise-legitimate update, once tried.
				Long ownedCount = (Long) sessionFactory.getCurrentSession().createCriteria(EquipmentChecklist.class)
						.add(Restrictions.eq("checklistEquipmentMasterId", equipmentChecklistDto.getChecklistEquipmentMasterId()))
						.add(Restrictions.eq("organizationId", equipmentChecklistDto.getOrganizationId()))
						.add(Restrictions.eq("unitId", equipmentChecklistDto.getUnitId()))
						.setProjection(Projections.rowCount()).uniqueResult();
				if (ownedCount == null || ownedCount == 0) {
					logger.error("saveEquipmentChecklist: refused update of checklistEquipmentMasterId="
							+ equipmentChecklistDto.getChecklistEquipmentMasterId() + " -- not owned by the verified organization/unit");
					return new Response<>(ERROR, ERROR_CODE, "Not found.", null, null);
				}
				mstr.setChecklistEquipmentMasterId(equipmentChecklistDto.getChecklistEquipmentMasterId());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}else {
				save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}
				} catch (HibernateException he) {
				logger.error("saveEquipmentChecklist failed", he);
				} catch (Exception e) {
				logger.error("saveEquipmentChecklist failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllEquipmentChecklistMasterList(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			List<EquipmentChecklistDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_EQUIPMENT_MASTER_LIST")
					.setParameter("organizationId", equipmentChecklistDto.getOrganizationId())
					.setParameter("unitId", equipmentChecklistDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateChecklistEquipmentMasterStatus(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			EquipmentChecklist mstr = findById(equipmentChecklistDto.getChecklistEquipmentMasterId());
			// BR-121: findById(id) alone has no ownership check, and this
			// endpoint doesn't even receive organizationId/unitId from the
			// legacy frontend -- verify the row really belongs to the
			// (service-layer-verified) tenant on the DTO before mutating it.
			if (mstr == null || !mstr.getOrganizationId().equals(equipmentChecklistDto.getOrganizationId())
					|| !mstr.getUnitId().equals(equipmentChecklistDto.getUnitId())) {
				logger.error("updateChecklistEquipmentMasterStatus: refused update of checklistEquipmentMasterId="
						+ equipmentChecklistDto.getChecklistEquipmentMasterId() + " -- not owned by the verified organization/unit");
				return new Response<>(ERROR, ERROR_CODE, "Not found.", null, null);
			}
			mstr.setStatus(equipmentChecklistDto.getStatus());
			mstr.setUpdatedBy(equipmentChecklistDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(equipmentChecklistDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("updateChecklistEquipmentMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEquipmentChecklistNameListById(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			/*List<EquipmentChecklistDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_EQUIPMENT_MASTER_DETAILS_LIST_BY_MASTER_ID")
					.setParameter("organizationId", equipmentChecklistDto.getOrganizationId())
					.setParameter("unitId", equipmentChecklistDto.getUnitId())
					.setParameter("checklistMasterId", equipmentChecklistDto.getChecklistEquipmentMasterId())
					.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistDto.class)).list();*/
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("getEquipmentChecklistNameListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllMealMasterListPaginationList(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			List<EquipmentChecklistDto> EquipmentChecklistDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_EQUIPMENT_MASTER_LIST")
					.setParameter("organizationId",equipmentChecklistDto.getOrganizationId())
					.setParameter("unitId", equipmentChecklistDto.getUnitId())
					.setFirstResult(equipmentChecklistDto.getOffset() != null ? equipmentChecklistDto.getOffset()  : 0)
					.setMaxResults(equipmentChecklistDto.getNoOfRecordsPerPage() != null ? equipmentChecklistDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistDto.class)).list();
			return new Response(SUCCESS, null, null, EquipmentChecklistDtosList, null);

		} catch (Exception e) {
		logger.error("getAllMealMasterListPaginationList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAllMealMasterListPaginationListCount(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(EquipmentChecklist.class);
			crit.add(Restrictions.eq("organizationId", equipmentChecklistDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", equipmentChecklistDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getAllMealMasterListPaginationListCount failed", he);
		} catch (Exception e) {
		logger.error("getAllMealMasterListPaginationListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
