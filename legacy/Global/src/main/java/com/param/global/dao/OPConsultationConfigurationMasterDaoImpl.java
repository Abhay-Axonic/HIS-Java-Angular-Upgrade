package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CountryMasterDto;
import com.param.global.dto.OPConsultationConfigurationMasterDto;
import com.param.global.model.OPConsultationConfigurationMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class OPConsultationConfigurationMasterDaoImpl extends GenericDao<OPConsultationConfigurationMaster, Integer> implements IOPConsultationConfigurationMasterDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(OPConsultationConfigurationMasterDaoImpl.class);


	public OPConsultationConfigurationMasterDaoImpl() {
		super(OPConsultationConfigurationMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response checkOPConsultationExistOrNot(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			StringBuilder queryBuilder = new StringBuilder("SELECT ")
					.append("    opccm.opVisitRuleId as opVisitRuleId ")
					.append("FROM OPConsultationConfigurationMaster opccm ")
					.append("WHERE opccm.organizationId = :orgId ")
					.append("AND opccm.unitId = :unitId ")
					.append("AND opccm.specialityId = :specialityId ")
					.append("AND opccm.status = 'A' ");
			if (opConsultationConfigurationMasterDto.getDoctorId() != null && opConsultationConfigurationMasterDto.getDoctorId() > 0) {
				queryBuilder.append("AND opccm.doctorId = :doctorId ");
			}
			Query query = sessionFactory.getCurrentSession()
					.createQuery(queryBuilder.toString())
					.setParameter("orgId", opConsultationConfigurationMasterDto.getOrganizationId())
					.setParameter("unitId", opConsultationConfigurationMasterDto.getUnitId())
					.setParameter("specialityId", opConsultationConfigurationMasterDto.getSpecialityId());
			if (opConsultationConfigurationMasterDto.getDoctorId() != null && opConsultationConfigurationMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", opConsultationConfigurationMasterDto.getDoctorId());
			}
			List<OPConsultationConfigurationMasterDto> OPConsultationConfigurationMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(OPConsultationConfigurationMasterDto.class))
					.list();
			return new Response(SUCCESS, null, null, OPConsultationConfigurationMasterDtosList, null);
		} catch (HibernateException he) {
		logger.error("checkOPConsultationExistOrNot failed", he);
		} catch (Exception e) {
		logger.error("checkOPConsultationExistOrNot failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveOPConsultationConfiguration(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) {
		try {
			OPConsultationConfigurationMaster opConsultationConfigurationMaster = mapper.map(
					opConsultationConfigurationMasterDto, OPConsultationConfigurationMaster.class,
					"OPConsultationConfigurationMasterDto_to_OPConsultationConfigurationMaster");
			opConsultationConfigurationMaster.setStatus('A');
			save(opConsultationConfigurationMaster);

			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("saveOPConsultationConfiguration failed", he);
		} catch (Exception e) {
		logger.error("saveOPConsultationConfiguration failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPConsultationConfigurationList(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			List<OPConsultationConfigurationMasterDto> OPConsultationConfigurationMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OP_CONSULTATION_LIST")
					.setParameter("orgId", opConsultationConfigurationMasterDto.getOrganizationId())
					.setParameter("unitId", opConsultationConfigurationMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OPConsultationConfigurationMasterDto.class)).list();
			
			return new Response(SUCCESS, null, null, OPConsultationConfigurationMasterDtosList, null);

		} catch (HibernateException he) {
		logger.error("getOPConsultationConfigurationList failed", he);
		} catch (Exception e) {
		logger.error("getOPConsultationConfigurationList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPConsultationConfigurationListById(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			List<OPConsultationConfigurationMasterDto> OPConsultationConfigurationMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OP_CONSULTATION_CONFIGURATION_LIST_BY_ID")
					.setParameter("opVisitRuleId", opConsultationConfigurationMasterDto.getOpVisitRuleId())
					.setResultTransformer(Transformers.aliasToBean(OPConsultationConfigurationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, OPConsultationConfigurationMasterDtosList, null);

		} catch (HibernateException he) {
		logger.error("getOPConsultationConfigurationListById failed", he);
		} catch (Exception e) {
		logger.error("getOPConsultationConfigurationListById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOPConsultationConfigurationMaster(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			OPConsultationConfigurationMaster opConsultationConfigurationMaster = findById(opConsultationConfigurationMasterDto.getOpVisitRuleId());
				opConsultationConfigurationMaster.setFollowupVisitCount(opConsultationConfigurationMasterDto.getFollowupVisitCount());
				opConsultationConfigurationMaster.setFollowupVisitDays(opConsultationConfigurationMasterDto.getFollowupVisitDays());
				opConsultationConfigurationMaster.setSecondaryVisitDays(opConsultationConfigurationMasterDto.getSecondaryVisitDays());
				opConsultationConfigurationMaster.setSecondaryVisitCount(opConsultationConfigurationMasterDto.getSecondaryVisitCount());
				opConsultationConfigurationMaster.setUpdatedBy(opConsultationConfigurationMasterDto.getUpdatedBy());
				opConsultationConfigurationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(opConsultationConfigurationMasterDto.getUpdatedDate(),"dd-mm-yyyy HH:mm:ss"));
			update(opConsultationConfigurationMaster);
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateOPConsultationConfigurationMaster failed", he);
		} catch (Exception e) {
		logger.error("updateOPConsultationConfigurationMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPConsultationConfigurationBySpecialityId(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			
			String query =" select " 
					 + " 	opccm.op_visit_rule_id as \"opVisitRuleId\", " 
					 + " 	opccm.speciality_id as \"specialityId\", " 
					 + " 	opccm.followup_visit_days as \"followupVisitDays\", " 
					 + " 	opccm.followup_visit_count as \"followupVisitCount\", " 
					 + " 	opccm.secondary_visit_days as \"secondaryVisitDays\", " 
					 + " 	opccm.secondary_visit_count as \"secondaryVisitCount\", " 
					 + " 	'1' as \"type\" " 
					 + " from " 
					 + " 	opd.m_op_consultation_configuration_master opccm " 
					 + " where " 
					 + " 	opccm.organization_id =:orgId " 
					 + " 	and opccm.unit_id =:unitId " 
					 + " 	and opccm.status = 'A' " 
					 + " 	and opccm.speciality_id =:specialityId " 
					 + " 	and opccm.doctor_id is null " 
					 + " union " 
					 + " select " 
					 + " 	opccm.op_visit_rule_id as \"opVisitRuleId\", " 
					 + " 	opccm.speciality_id as \"specialityId\", " 
					 + " 	opccm.followup_visit_days as \"followupVisitDays\", " 
					 + " 	opccm.followup_visit_count as \"followupVisitCount\", " 
					 + " 	opccm.secondary_visit_days as \"secondaryVisitDays\", " 
					 + " 	opccm.secondary_visit_count as \"secondaryVisitCount\", " 
					 + " 	2 as \"type\" " 
					 + " from " 
					 + " 	opd.m_op_consultation_configuration_master opccm " 
					 + " where " 
					 + " 	opccm.organization_id =:orgId " 
					 + " 	and opccm.unit_id =:unitId " 
					 + " 	and opccm.status = 'A' " 
					 + " 	and opccm.speciality_id =:specialityId " 
					 + " 	and opccm.doctor_id =:doctorId " 
					 + " order by " 
					 + " 	type desc " 
					 + " limit 1 ";
			
			
			OPConsultationConfigurationMasterDto OPConsultationConfigurationMasterDtosList = (OPConsultationConfigurationMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", opConsultationConfigurationMasterDto.getOrganizationId())
					.setParameter("unitId", opConsultationConfigurationMasterDto.getUnitId())
					.setParameter("specialityId",opConsultationConfigurationMasterDto.getSpecialityId())
					.setParameter("doctorId",opConsultationConfigurationMasterDto.getDoctorId()!=null ? opConsultationConfigurationMasterDto.getDoctorId() : 0)
					.setResultTransformer(Transformers.aliasToBean(OPConsultationConfigurationMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, OPConsultationConfigurationMasterDtosList);
		} catch (HibernateException he) {
		logger.error("getOPConsultationConfigurationBySpecialityId failed", he);
		} catch (Exception e) {
		logger.error("getOPConsultationConfigurationBySpecialityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOPConsultationConfigurationStatus(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {

			OPConsultationConfigurationMaster opConsultationConfigurationMaster = findById(opConsultationConfigurationMasterDto.getOpVisitRuleId());
			opConsultationConfigurationMaster.setStatus(opConsultationConfigurationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			opConsultationConfigurationMaster.setUpdatedBy(opConsultationConfigurationMasterDto.getUpdatedBy());
			opConsultationConfigurationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(opConsultationConfigurationMasterDto.getUpdatedDate(),"dd-mm-yyyy HH:mm:ss"));
			update(opConsultationConfigurationMaster);
			return new Response<CountryMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateOPConsultationConfigurationStatus failed", he);
			return new Response<CountryMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateOPConsultationConfigurationStatus failed", e);
			return new Response<CountryMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}
	
	
	

}
