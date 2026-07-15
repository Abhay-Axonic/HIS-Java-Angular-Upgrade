package com.param.global.org.dao;

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

import com.param.entity.org.model.GeneralLedgerMaster;
import com.param.entity.org.model.LicenceTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.UserRoleMapper;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.org.dto.LiceneceTypeMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GeneralLedgerMasterServiceDaoImpl extends GenericDao<GeneralLedgerMaster, Integer> implements IGeneralLedgerMasterServiceDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(GeneralLedgerMasterServiceDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;

	public GeneralLedgerMasterServiceDaoImpl() {
		super(GeneralLedgerMaster.class);
	}

	@Override
	public Response getGeneralLedgerMasterByName(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		try {
			List<GeneralLedgerMasterDto> generalLedgerMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENERAL_LEDGER_MASTER_BY_NAME")
					.setParameter("ledgerName", generalLedgerMasterDto.getGeneralLedgerName().toLowerCase())
					.setParameter("orgId", generalLedgerMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GeneralLedgerMasterDto.class)).list();
			return new Response(SUCCESS, null, null, generalLedgerMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getGeneralLedgerMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveGeneralLedgerMaster(GeneralLedgerMasterDto generalLedgerMasterDto) throws ApplicationException {
		try {
			GeneralLedgerMaster generalLedgerMaster = new GeneralLedgerMaster();
			generalLedgerMaster.setGeneralLedgerCode(generalLedgerMasterDto.getGeneralLedgerCode());
			generalLedgerMaster.setGeneralLedgerName(generalLedgerMasterDto.getGeneralLedgerName());
			generalLedgerMaster.setCreatedBy(generalLedgerMasterDto.getCreatedBy());
			generalLedgerMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(generalLedgerMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			generalLedgerMaster.setStatus(generalLedgerMasterDto.getStatus());
			generalLedgerMaster.setUpdatedBy(generalLedgerMasterDto.getUpdatedBy());
			generalLedgerMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(generalLedgerMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			generalLedgerMaster.setOrganizationId(generalLedgerMasterDto.getOrganizationId());
			//generalLedgerMaster.setUnitId(generalLedgerMasterDto.getUnitId());
			save(generalLedgerMaster);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, generalLedgerMaster);
		} catch (HibernateException he) {
		logger.error("saveGeneralLedgerMaster failed", he);
		} catch (Exception e) {
		logger.error("saveGeneralLedgerMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterById(int lederId, int orgId) throws ApplicationException {
		try {
			List<GeneralLedgerMasterDto> generalLedgerMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENERAL_LEDGER_MASTER_BY_ID").setParameter("lederId", lederId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(GeneralLedgerMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, generalLedgerMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getGeneralLedgerMasterById failed", he);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterList(int orgId,int offset, int noOfRecordsPerPage) throws ApplicationException {
		try {
			List<GeneralLedgerMasterDto> generalLedgerMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENERAL_LEDGER_MASTER_LIST").setParameter("orgId", orgId)
                    .setFirstResult(offset)
                    .setMaxResults(noOfRecordsPerPage)
					.setResultTransformer(Transformers.aliasToBean(GeneralLedgerMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, generalLedgerMasterDtosList, null);
		} catch (HibernateException he) {
		logger.error("getGeneralLedgerMasterList failed", he);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getLedgerByNameNotId(GeneralLedgerMasterDto generalLedgerMasterDto) throws ApplicationException {
		try {
			List<GeneralLedgerMasterDto> generalLedgerMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LEDGER_BY_NAME_NOT_ID")
					.setParameter("ledgerId", generalLedgerMasterDto.getGeneralLedgerId())
					.setParameter("ledgerName", generalLedgerMasterDto.getGeneralLedgerName())
					.setResultTransformer(Transformers.aliasToBean(GeneralLedgerMasterDto.class)).list();
			return new Response(SUCCESS, null, null, generalLedgerMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getLedgerByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getLedgerByNameNotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateGeneralLedgerMaster(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		try {
			GeneralLedgerMaster generalLedgerMaster = findById(generalLedgerMasterDto.getGeneralLedgerId());
			generalLedgerMaster.setGeneralLedgerCode(generalLedgerMasterDto.getGeneralLedgerCode());
			generalLedgerMaster.setGeneralLedgerName(generalLedgerMasterDto.getGeneralLedgerName());
			generalLedgerMaster.setUpdatedBy(generalLedgerMasterDto.getUpdatedBy());
			generalLedgerMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(generalLedgerMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(generalLedgerMaster);
			return new Response(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateGeneralLedgerMaster failed", he);
		} catch (Exception e) {
		logger.error("updateGeneralLedgerMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateGeneralLedgerMasterStatus(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		try {
			GeneralLedgerMaster generalLedgerMaster = findById(generalLedgerMasterDto.getGeneralLedgerId());
			
			generalLedgerMaster.setStatus(generalLedgerMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			generalLedgerMaster.setUpdatedBy(generalLedgerMasterDto.getUpdatedBy());
			generalLedgerMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(generalLedgerMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(generalLedgerMaster);
			return new Response<LiceneceTypeMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (Exception e) {
		logger.error("updateGeneralLedgerMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterActiveList(int orgId,String generalLedgerName) throws ApplicationException {
		try {
			List<GeneralLedgerMasterDto> generalLedgerMasterDtosList=null;
			String roughQuery= sessionFactory.getCurrentSession().getNamedQuery("GET_GENERAL_LEDGER_MASTER_ACTIVE_LIST").getQueryString();
			StringBuilder stringBuilderQuery = new StringBuilder(roughQuery);
			if(generalLedgerName!=null && generalLedgerName.length()>0)
			{
				stringBuilderQuery.append(" and LOWER(ledgerMaster.generalLedgerName) LIKE:generalLedgerName");
				generalLedgerMasterDtosList = sessionFactory.getCurrentSession()
						.createQuery(stringBuilderQuery.toString())
						.setParameter("orgId", orgId)
						.setParameter("generalLedgerName","%"+generalLedgerName.toLowerCase()+"%")
						.setResultTransformer(Transformers.aliasToBean(GeneralLedgerMasterDto.class)).list();
			}
			else
			{
			generalLedgerMasterDtosList = sessionFactory.getCurrentSession()
					.createQuery(stringBuilderQuery.toString())
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(GeneralLedgerMasterDto.class)).list();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, generalLedgerMasterDtosList, null);
		} catch (HibernateException he) {
		logger.error("getGeneralLedgerMasterActiveList failed", he);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterCount(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		Session session=sessionFactory.getCurrentSession();
		Criteria c=session.createCriteria(GeneralLedgerMaster.class);
		c.add(Restrictions.eq("organizationId", generalLedgerMasterDto.getOrganizationId()));
		//c.add(Restrictions.eq("unitId", generalLedgerMasterDto.getUnitId()));
		c.setProjection(Projections.rowCount());  
		Long count = (Long)c.uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);		
	}

}
