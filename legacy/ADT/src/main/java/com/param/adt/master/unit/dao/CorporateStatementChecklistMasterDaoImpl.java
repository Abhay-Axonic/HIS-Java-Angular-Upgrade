package com.param.adt.master.unit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitTypeMasterDto;
import com.param.adt.master.unit.dto.BillingBedCategoryMasterDto;
import com.param.adt.master.unit.dto.CorporateStatementChecklistMasterDto;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.adt.master.unit.model.BillingBedVisitTypeMapper;
import com.param.adt.master.unit.model.CorporateStatementChecklistMaster;
import com.param.adt.master.unit.model.CorporateStatementChecklistVisitTypeMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CorporateStatementChecklistMasterDaoImpl extends GenericDao<CorporateStatementChecklistMaster, Integer> implements ICorporateStatementChecklistMasterDao,ICommonConstants{

	
	public CorporateStatementChecklistMasterDaoImpl() {
		super(CorporateStatementChecklistMaster.class);
	}
	
	@Override
	public Response getActiveCorporateStatementChecklistMaster(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			List<CorporateStatementChecklistMasterDto> corporateStatementChecklistMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CORPORATE_STATEMENT_CHECKLIST_LIST")
					.setParameter("orgId", corporateStatementChecklistMasterDto.getOrganizationId())
					.setParameter("unitId", corporateStatementChecklistMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CorporateStatementChecklistMasterDto.class)).list();
			return new Response(SUCCESS, null, null, corporateStatementChecklistMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	

	@Override
	public Response getCorporateStatementChecklistByName(CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto)
			throws ApplicationException {
		try {
			List<CorporateStatementChecklistMasterDto> CorporateStatementChecklistMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_CHECKLIST_BY_NAME")
					.setParameter("orgId",corporateStatementChecklistMasterDto.getOrganizationId())
					.setParameter("unitId",corporateStatementChecklistMasterDto.getUnitId())
					.setParameter("desc", corporateStatementChecklistMasterDto.getCheckListDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CorporateStatementChecklistMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, CorporateStatementChecklistMasterDtosList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveCorporateStatementChecklistMaster( CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			CorporateStatementChecklistMaster corporateStatementChecklistMaster=new CorporateStatementChecklistMaster();
				corporateStatementChecklistMaster.setCheckListCode(corporateStatementChecklistMasterDto.getCheckListCode());
				corporateStatementChecklistMaster.setCheckListDesc(corporateStatementChecklistMasterDto.getCheckListDesc());
//				corporateStatementChecklistMaster.setCreatedBy(corporateStatementChecklistMasterDto.getCreatedBy());
//				corporateStatementChecklistMaster.setUpdatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
				corporateStatementChecklistMaster.setCreatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				corporateStatementChecklistMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				corporateStatementChecklistMaster.setStatus(corporateStatementChecklistMasterDto.getStatus());
				corporateStatementChecklistMaster.setOrganizationId(corporateStatementChecklistMasterDto.getOrganizationId());
				corporateStatementChecklistMaster.setUnitId(corporateStatementChecklistMasterDto.getUnitId());
			corporateStatementChecklistMaster=save(corporateStatementChecklistMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, corporateStatementChecklistMaster.getCorporateStatementChecklistId());

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Integer saveCorporateStatementChecklistMasterVisitTypeMapper(CorporateStatementChecklistVisitTypeMapper corporateStatementChecklistVisitTypeMapper)
			throws ApplicationException {
		try {
				Integer i = (Integer) sessionFactory.getCurrentSession().save(corporateStatementChecklistVisitTypeMapper);
			
			return i;

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return 0;
	}

	@Override
	public Response getCorporateStatementChecklistMasterList(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			
			List<CorporateStatementChecklistMasterDto> corporateStatementChecklistMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_CHECKLIST_MASTER_LIST")
					.setParameter("orgId",corporateStatementChecklistMasterDto.getOrganizationId())
					.setParameter("unitId", corporateStatementChecklistMasterDto.getUnitId())
					.setFirstResult(corporateStatementChecklistMasterDto.getOffset() != null ? corporateStatementChecklistMasterDto.getOffset() : 0)
					.setMaxResults(
							corporateStatementChecklistMasterDto.getNoOfRecordsPerPage() != null ? corporateStatementChecklistMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CorporateStatementChecklistMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, corporateStatementChecklistMasterDtoList, null);
			

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public  List<VisitTypeMasterDto> getCorporateStatementChecklistVistList(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			
			List<VisitTypeMasterDto> visitTypeMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_CHECKLIST_VISIT_LIST")
					.setParameter("orgId",corporateStatementChecklistMasterDto.getOrganizationId())
					.setParameter("corporateStatementChecklistId", corporateStatementChecklistMasterDto.getCorporateStatementChecklistId())
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();	
			
			return visitTypeMasterDtosList;

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response getCorporateStatementChecklistMasterCount(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(CorporateStatementChecklistMaster.class);
			c.add(Restrictions.eq("organizationId", corporateStatementChecklistMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", corporateStatementChecklistMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCorporateStatementChecklistMasterById(int corporateStatementChecklistId, int orgId, int unitId)
			throws ApplicationException {
		try {
			List<CorporateStatementChecklistMasterDto> corporateStatementChecklistMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_CHECKLIST_BY_ID")
					.setParameter("orgId",orgId)
					.setParameter("unitId", unitId)
					.setParameter("corporateStatementChecklistId", corporateStatementChecklistId)
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, corporateStatementChecklistMasterDtosList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getCorporateStatementChecklistByNameNotById(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {

			List<CorporateStatementChecklistMasterDto> corporateStatementChecklistMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_CHECKLIST_BY_NAME_NOT_BY_ID")
					.setParameter("corporateStatementChecklistId",corporateStatementChecklistMasterDto.getCorporateStatementChecklistId())
					.setParameter("orgId",corporateStatementChecklistMasterDto.getOrganizationId())
					.setParameter("unitId",corporateStatementChecklistMasterDto.getUnitId())
					.setParameter("desc", corporateStatementChecklistMasterDto.getCheckListDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, corporateStatementChecklistMasterDtosList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCorporateStatementChecklistMaster(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			CorporateStatementChecklistMaster corporateStatementChecklistMaster = findById(corporateStatementChecklistMasterDto.getCorporateStatementChecklistId());
			corporateStatementChecklistMaster.setCheckListDesc(corporateStatementChecklistMasterDto.getCheckListDesc());
			corporateStatementChecklistMaster.setCheckListCode(corporateStatementChecklistMasterDto.getCheckListCode());
//			corporateStatementChecklistMaster.setUpdatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
			corporateStatementChecklistMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
			update(corporateStatementChecklistMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePreviousVisitTypeList(
			CorporateStatementChecklistVisitTypeMapper corporateStatementChecklistVisitTypeMapper)
			throws ApplicationException {
		try {
			
			sessionFactory.getCurrentSession().createQuery("update CorporateStatementChecklistVisitTypeMapper set status='I', "
//					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ "WHERE corporateStatementChecklistId=:corporateStatementChecklistId "
					+ "AND visitTypeId=:visitTypeId ")
//				.setParameter("updatedBy", corporateStatementChecklistVisitTypeMapper.getUpdatedBy())
				.setParameter("corporateStatementChecklistId", corporateStatementChecklistVisitTypeMapper.getCorporateStatementChecklistId())
				.setParameter("visitTypeId", corporateStatementChecklistVisitTypeMapper.getVisitTypeId())
				.setParameter("updatedDate",corporateStatementChecklistVisitTypeMapper.getUpdatedDate())
			.executeUpdate();
			
			return new Response<>(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Integer saveCorporateStatementListBedVisitTypeMapper(
			CorporateStatementChecklistVisitTypeMapper corporateStatementChecklistVisitTypeMapper)
			throws ApplicationException {
		try {
			Integer i = (Integer) sessionFactory.getCurrentSession().save(corporateStatementChecklistVisitTypeMapper);
		
		return i;

	} catch (HibernateException he) {
	} catch (Exception e) {
		
	}
	return 0;
}

	@Override
	public Response updateCorporateStatementChecklistMasterStatus(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			CorporateStatementChecklistMaster corporateStatementChecklistMaster=findById(corporateStatementChecklistMasterDto.getCorporateStatementChecklistId());
			corporateStatementChecklistMaster.setStatus(corporateStatementChecklistMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
//			corporateStatementChecklistMaster.setUpdatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
			corporateStatementChecklistMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(corporateStatementChecklistMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@Override
	public Response getCorporateStatementChecklistMasterListAll(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {

			List<CorporateStatementChecklistMasterDto> corporateStatementChecklistMasterList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_CHECKLIST_MASTER_LIST")
					.setParameter("orgId",corporateStatementChecklistMasterDto.getOrganizationId())
					.setParameter("unitId", corporateStatementChecklistMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CorporateStatementChecklistMasterDto.class)).list();	
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, corporateStatementChecklistMasterList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	
	
}
