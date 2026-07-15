package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
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
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorConsChargesForMyLife;
import com.param.global.dto.DoctorConsultationServiceMapperDto;
import com.param.global.dto.GenderMasterDto;
import com.param.global.model.DoctorConsultationServiceMapper;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class DoctorConsultationServiceMapperDaoImpl extends GenericDao<DoctorConsultationServiceMapper, Integer>
		implements IDoctorConsultationServiceMapperDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DoctorConsultationServiceMapperDaoImpl.class);

	public DoctorConsultationServiceMapperDaoImpl() {
		super(DoctorConsultationServiceMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response saveDoctorConsultationServiceMapperService(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			DoctorConsultationServiceMapper doctorConsultationServiceMapper = mapper.map(
					doctorConsultationServiceMapperDto, DoctorConsultationServiceMapper.class,
					"DoctorConsultationServiceMapperDto_to_DoctorConsultationServiceMapper");
			
			doctorConsultationServiceMapper.setDoctorId(doctorConsultationServiceMapperDto.getDoctorId()!=null ? doctorConsultationServiceMapperDto.getDoctorId() : 0);
			doctorConsultationServiceMapper.setCompanyId(doctorConsultationServiceMapper.getCompanyId()!=null ? doctorConsultationServiceMapper.getCompanyId() : 0);
			doctorConsultationServiceMapper.setAssoCompanyId(doctorConsultationServiceMapper.getAssoCompanyId()!=null ? doctorConsultationServiceMapper.getAssoCompanyId():0);
			doctorConsultationServiceMapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(doctorConsultationServiceMapperDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			doctorConsultationServiceMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(doctorConsultationServiceMapperDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			doctorConsultationServiceMapper.setStatus('A');
			save(doctorConsultationServiceMapper);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, doctorConsultationServiceMapper);
		} catch (HibernateException he) {
			logger.error("Error while saving doctor consultation service mapper", he);
		} catch (Exception e) {
			logger.error("Error while saving doctor consultation service mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR, null, null);
	}

	@Override
	public Response getDoctorConsultationServiceMapperList(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			
			StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_CONSULTATION_SERVICE_MAPPER_LIST").getQueryString());
			
			if(doctorConsultationServiceMapperDto.getTabNumber()==1) {
				query.append(" AND dcsm.doctorId != 0 ");
			}else if(doctorConsultationServiceMapperDto.getTabNumber()==2) {
				query.append(" AND dcsm.doctorId = 0 ");
			}
			
			List<DoctorConsultationServiceMapperDto> doctorConsultationServiceMapperDtosList=sessionFactory.getCurrentSession()
					.createQuery(query.toString())
					.setParameter("orgId", doctorConsultationServiceMapperDto.getOrganizationId())
					.setParameter("unitId", doctorConsultationServiceMapperDto.getUnitId())
					.setFirstResult(doctorConsultationServiceMapperDto.getOffset() != null ? doctorConsultationServiceMapperDto.getOffset() : 0)
					.setMaxResults(doctorConsultationServiceMapperDto.getNoOfRecordsPerPage() != null ? doctorConsultationServiceMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorConsultationServiceMapperDtosList, null);
		}catch (HibernateException he) {
			logger.error("Error while fetching doctor consultation service mapper list", he);
		} catch (Exception e) {
			logger.error("Error while fetching doctor consultation service mapper list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDoctorConsultationServiceMapper(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			
				DoctorConsultationServiceMapper doctorConsultationServiceMapper = findById(doctorConsultationServiceMapperDto.getDoctorConsultationServiceMapperId());
					doctorConsultationServiceMapper.setDoctorId(doctorConsultationServiceMapperDto.getDoctorId());
					doctorConsultationServiceMapper.setNewVisitServiceId(doctorConsultationServiceMapperDto.getNewVisitServiceId());
					doctorConsultationServiceMapper.setFollowupVisitServiceId(doctorConsultationServiceMapperDto.getFollowupVisitServiceId());
					doctorConsultationServiceMapper.setSecondaryVisitServiceId(doctorConsultationServiceMapperDto.getSecondaryVisitServiceId());
					doctorConsultationServiceMapper.setUpdatedBy(doctorConsultationServiceMapperDto.getUpdatedBy());
					doctorConsultationServiceMapper.setNewVisitRate(doctorConsultationServiceMapperDto.getNewVisitRate());
					doctorConsultationServiceMapper.setFollowupVisitRate(doctorConsultationServiceMapperDto.getFollowupVisitRate());
					doctorConsultationServiceMapper.setSecondaryVisitRate(doctorConsultationServiceMapperDto.getSecondaryVisitRate());
					doctorConsultationServiceMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorConsultationServiceMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				
				update(doctorConsultationServiceMapper);
				return new Response<GenderMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, doctorConsultationServiceMapper);

			} catch (HibernateException he) {
				logger.error("Error while updating doctor consultation service mapper", he);
				return new Response<GenderMasterDto, Integer>(ERROR, null, null, null, null);
			} catch (Exception e) {
				logger.error("Error while updating doctor consultation service mapper", e);
			}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Integer checkDoctorConsultationExistOrNot(DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto)
			throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("CHECK_DOCTOR_CONSULTATION_EXIST_OR_NOT").getQueryString());
			if (doctorConsultationServiceMapperDto.getDoctorId() != null && doctorConsultationServiceMapperDto.getDoctorId() > 0) {
				query.append(" AND dcsm.doctorId = :doctorId");
				query.append(" AND dcsm.specialityId = :specialityId");
			} else {
				query.append(" AND dcsm.specialityId = :specialityId");
				query.append(" AND (dcsm.doctorId IS NULL OR dcsm.doctorId = 0)");
			}
			if (doctorConsultationServiceMapperDto.getCompanyId() != null && doctorConsultationServiceMapperDto.getCompanyId() > 0) {
				query.append(" AND dcsm.companyId = :companyId");
			} else {
				query.append(" AND dcsm.companyId = 0");
			}
			if (doctorConsultationServiceMapperDto.getAssoCompanyId() != null && doctorConsultationServiceMapperDto.getAssoCompanyId() > 0) {
				query.append(" AND dcsm.assoCompanyId = :assoCompanyId");
			} else {
				query.append(" AND dcsm.assoCompanyId = 0");
			}

			Query createQuery =sessionFactory.getCurrentSession()
					.createQuery(query.toString());
			createQuery.setParameter("orgId", doctorConsultationServiceMapperDto.getOrganizationId());
			createQuery.setParameter("unitId", doctorConsultationServiceMapperDto.getUnitId());
			createQuery.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId());  // Parameterized query for paymentEntitlementId

			if (doctorConsultationServiceMapperDto.getDoctorId() != null && doctorConsultationServiceMapperDto.getDoctorId() > 0) {
				createQuery	.setParameter("specialityId", doctorConsultationServiceMapperDto.getSpecialityId());
				createQuery.setParameter("doctorId", doctorConsultationServiceMapperDto.getDoctorId());
			} else {
				createQuery	.setParameter("specialityId", doctorConsultationServiceMapperDto.getSpecialityId());
			}

			if (doctorConsultationServiceMapperDto.getCompanyId() != null && doctorConsultationServiceMapperDto.getCompanyId() > 0) {
				createQuery.setParameter("companyId", doctorConsultationServiceMapperDto.getCompanyId());  // Parameterized query for companyId

			}
			if (doctorConsultationServiceMapperDto.getAssoCompanyId() != null && doctorConsultationServiceMapperDto.getAssoCompanyId() > 0) {
				createQuery	.setParameter("assoCompanyId", doctorConsultationServiceMapperDto.getAssoCompanyId());  // Parameterized query for assoCompanyId
			}
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDtoo = (DoctorConsultationServiceMapperDto) createQuery.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class))
					.uniqueResult();
			Integer id=null;
			if(doctorConsultationServiceMapperDtoo!=null)
				id =doctorConsultationServiceMapperDtoo.getDoctorConsultationServiceMapperId();
			
			return id;
		}catch (HibernateException he) {
			logger.error("Error while checking doctor consultation existence", he);
		} catch (Exception e) {
			logger.error("Error while checking doctor consultation existence", e);
		}
		return null;
	}

	@Override
	public Response getDoctorConsultationServiceMapperById(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			List<DoctorConsultationServiceMapperDto> doctorConsultationServiceMapperDtosList= sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CONSULTATION_SERVICE_BY_ID")
					.setParameter("doctorConsultationServiceMapperId", doctorConsultationServiceMapperDto.getDoctorConsultationServiceMapperId())
					.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorConsultationServiceMapperDtosList, null);
		}catch (HibernateException he) {
			logger.error("Error while fetching doctor consultation service mapper by id", he);
		} catch (Exception e) {
			logger.error("Error while fetching doctor consultation service mapper by id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response inactivePreviousEntry(Integer id)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorConsultationServiceMapper dcsm set dcsm.status='I' "
					+ "WHERE dcsm.doctorConsultationServiceMapperId=:doctorConsultationServiceMapperId ")
					.setParameter("doctorConsultationServiceMapperId", id).executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (HibernateException he) {
			logger.error("Error while inactivating previous doctor consultation service mapper entry", he);
		} catch (Exception e) {
			logger.error("Error while inactivating previous doctor consultation service mapper entry", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorConsultationServiceBySpecialityAndDoctorId(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {

			
				String query = " select " 
					 + " 	doctorcons.doctor_consultation_service_mapper_id as \"doctorConsultationServiceMapperId\", " 
					 + " 	doctorcons.new_visit_service_id as \"newVisitServiceId\", " 
					 + " 	doctorcons.new_visit_rate as \"newVisitRate\", " 
					 + " 	doctorcons.followup_visit_service_id as \"followupVisitServiceId\", " 
					 + " 	doctorcons.follow_up_visit_rate as \"followupVisitRate\", " 
					 + " 	doctorcons.secondary_visit_service_id as \"secondaryVisitServiceId\", " 
					 + " 	doctorcons.secondary_visit_rate as \"secondaryVisitRate\", " 
					 + " 	speciality4.speciality_name as \"newVisitSpecialityName\", " 
					 + " 	speciality5.speciality_name as \"followupVisitSpecialityName\", " 
					 + " 	speciality.speciality_name as \"secondarySpecialityName\", " 
					 + " 	servicemas1.service_standard_name as \"newVisitServiceName\", " 
					 + " 	servicemas2.service_standard_name as \"followupVisitServiceName\", " 
					 + " 	servicemas3.service_standard_name as \"secondaryVisitServiceName\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(unitservic7_.is_ref_doctor_share, 'N') " 
					 + " 	from " 
					 + " 		service.t_unit_service_master_details unitservic7_ " 
					 + " 	where " 
					 + " 		unitservic7_.service_master_id = doctorcons.new_visit_service_id " 
					 + " 		and unitservic7_.unit_id = doctorcons.unit_id " 
					 + " 		and unitservic7_.status = 'A') as \"newVisitReferralShare\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(unitservic8_.is_ref_doctor_share, 'N') " 
					 + " 	from " 
					 + " 		service.t_unit_service_master_details unitservic8_ " 
					 + " 	where " 
					 + " 		unitservic8_.service_master_id = doctorcons.followup_visit_service_id " 
					 + " 		and unitservic8_.unit_id = doctorcons.unit_id " 
					 + " 		and unitservic8_.status = 'A') as \"followUpVisitReferralShare\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(unitservic9_.is_ref_doctor_share, 'N') " 
					 + " 	from " 
					 + " 		service.t_unit_service_master_details unitservic9_ " 
					 + " 	where " 
					 + " 		unitservic9_.service_master_id = doctorcons.secondary_visit_service_id " 
					 + " 		and unitservic9_.unit_id = doctorcons.unit_id " 
					 + " 		and unitservic9_.status = 'A') as \"secondaryVisitReferralShare\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(unitservic10_.is_doc_req, 'N') " 
					 + " 	from " 
					 + " 		service.t_unit_service_master_details unitservic10_ " 
					 + " 	where " 
					 + " 		unitservic10_.service_master_id = doctorcons.new_visit_service_id " 
					 + " 		and unitservic10_.unit_id = doctorcons.unit_id " 
					 + " 		and unitservic10_.status = 'A') as \"newVisitDoctorShare\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(unitservic11_.is_doc_req, 'N') " 
					 + " 	from " 
					 + " 		service.t_unit_service_master_details unitservic11_ " 
					 + " 	where " 
					 + " 		unitservic11_.service_master_id = doctorcons.followup_visit_service_id " 
					 + " 		and unitservic11_.unit_id = doctorcons.unit_id " 
					 + " 		and unitservic11_.status = 'A') as \"followUpVisitDoctorShare\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(unitservic12_.is_doc_req, 'N') " 
					 + " 	from " 
					 + " 		service.t_unit_service_master_details unitservic12_ " 
					 + " 	where " 
					 + " 		unitservic12_.service_master_id = doctorcons.secondary_visit_service_id " 
					 + " 		and unitservic12_.unit_id = doctorcons.unit_id " 
					 + " 		and unitservic12_.status = 'A') as \"secondaryVisitDoctorShare\", " 
					 + " 	1 as \"type\"  " 
					 + " from " 
					 + " 	doctor.m_doctor_consultation_service_mapper doctorcons " 
					 + " inner join service.m_service_master servicemas1 on " 
					 + " 	doctorcons.new_visit_service_id = servicemas1.service_master_id " 
					 + " inner join doctor.m_speciality_master speciality4 on " 
					 + " 	servicemas1.speciality_id = speciality4.speciality_id " 
					 + " inner join service.m_service_master servicemas2 on " 
					 + " 	doctorcons.followup_visit_service_id = servicemas2.service_master_id " 
					 + " inner join doctor.m_speciality_master speciality5 on " 
					 + " 	servicemas2.speciality_id = speciality5.speciality_id " 
					 + " inner join service.m_service_master servicemas3 on " 
					 + " 	doctorcons.secondary_visit_service_id = servicemas3.service_master_id " 
					 + " inner join doctor.m_speciality_master speciality on " 
					 + " 	servicemas3.speciality_id = speciality.speciality_id " 
					 + " where " 
					 + " 	doctorcons.organization_id = :orgId " 
					 + " 	and doctorcons.unit_id = :unitId " 
					 + " 	and doctorcons.payment_entitlement_id =:paymentEntitlementId " 
					 + " 	and doctorcons.status = 'A' " 
					 + " 	and doctorcons.speciality_id =:specialityId " 
					 + " 	and doctorcons.doctor_id =:doctorId " 
					 + " 	and doctorcons.company_id = 0 " 
					 + " 	and doctorcons.asso_company_id = 0 " ;
				if((doctorConsultationServiceMapperDto.getCompanyId()!=null && doctorConsultationServiceMapperDto.getCompanyId()>0)
						&& (doctorConsultationServiceMapperDto.getAssoCompanyId()!=null	&& doctorConsultationServiceMapperDto.getAssoCompanyId()>0))
				{
					query = query.concat(" union " 
							 + " select " 
							 + " 	doctorcons.doctor_consultation_service_mapper_id as \"doctorConsultationServiceMapperId\", " 
							 + " 	doctorcons.new_visit_service_id as \"newVisitServiceId\", " 
							 + " 	doctorcons.new_visit_rate as \"newVisitRate\", " 
							 + " 	doctorcons.followup_visit_service_id as \"followupVisitServiceId\", " 
							 + " 	doctorcons.follow_up_visit_rate as \"followupVisitRate\", " 
							 + " 	doctorcons.secondary_visit_service_id as \"secondaryVisitServiceId\", " 
							 + " 	doctorcons.secondary_visit_rate as \"secondaryVisitRate\", " 
							 + " 	speciality4.speciality_name as \"newVisitSpecialityName\", " 
							 + " 	speciality5.speciality_name as \"followupVisitSpecialityName\", " 
							 + " 	speciality.speciality_name as \"secondarySpecialityName\", " 
							 + " 	servicemas1.service_standard_name as \"newVisitServiceName\", " 
							 + " 	servicemas2.service_standard_name as \"followupVisitServiceName\", " 
							 + " 	servicemas3.service_standard_name as \"secondaryVisitServiceName\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic7_.is_ref_doctor_share, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic7_ " 
							 + " 	where " 
							 + " 		unitservic7_.service_master_id = doctorcons.new_visit_service_id " 
							 + " 		and unitservic7_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic7_.status = 'A') as \"newVisitReferralShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic8_.is_ref_doctor_share, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic8_ " 
							 + " 	where " 
							 + " 		unitservic8_.service_master_id = doctorcons.followup_visit_service_id " 
							 + " 		and unitservic8_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic8_.status = 'A') as \"followUpVisitReferralShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic9_.is_ref_doctor_share, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic9_ " 
							 + " 	where " 
							 + " 		unitservic9_.service_master_id = doctorcons.secondary_visit_service_id " 
							 + " 		and unitservic9_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic9_.status = 'A') as \"secondaryVisitReferralShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic10_.is_doc_req, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic10_ " 
							 + " 	where " 
							 + " 		unitservic10_.service_master_id = doctorcons.new_visit_service_id " 
							 + " 		and unitservic10_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic10_.status = 'A') as \"newVisitDoctorShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic11_.is_doc_req, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic11_ " 
							 + " 	where " 
							 + " 		unitservic11_.service_master_id = doctorcons.followup_visit_service_id " 
							 + " 		and unitservic11_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic11_.status = 'A') as \"followUpVisitDoctorShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic12_.is_doc_req, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic12_ " 
							 + " 	where " 
							 + " 		unitservic12_.service_master_id = doctorcons.secondary_visit_service_id " 
							 + " 		and unitservic12_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic12_.status = 'A') as \"secondaryVisitDoctorShare\", " 
							 + " 	2 as \"type\" 	 " 
							 + " from " 
							 + " 	doctor.m_doctor_consultation_service_mapper doctorcons " 
							 + " inner join service.m_service_master servicemas1 on " 
							 + " 	doctorcons.new_visit_service_id = servicemas1.service_master_id " 
							 + " inner join doctor.m_speciality_master speciality4 on " 
							 + " 	servicemas1.speciality_id = speciality4.speciality_id " 
							 + " inner join service.m_service_master servicemas2 on " 
							 + " 	doctorcons.followup_visit_service_id = servicemas2.service_master_id " 
							 + " inner join doctor.m_speciality_master speciality5 on " 
							 + " 	servicemas2.speciality_id = speciality5.speciality_id " 
							 + " inner join service.m_service_master servicemas3 on " 
							 + " 	doctorcons.secondary_visit_service_id = servicemas3.service_master_id " 
							 + " inner join doctor.m_speciality_master speciality on " 
							 + " 	servicemas3.speciality_id = speciality.speciality_id " 
							 + " where " 
							 + " 	doctorcons.organization_id = :orgId " 
							 + " 	and doctorcons.unit_id = :unitId " 
							 + " 	and doctorcons.payment_entitlement_id = 2 " 
							 + " 	and doctorcons.status = 'A' " 
							 + " 	and doctorcons.speciality_id =:specialityId " 
							 + " 	and doctorcons.doctor_id = :doctorId "
							 + " 	and doctorcons.company_id =:companyId "
							 + " 	and doctorcons.asso_company_id =:assoCompanyId ");
				}
				
				if(doctorConsultationServiceMapperDto.getCompanyId()!=null && doctorConsultationServiceMapperDto.getCompanyId() > 0 && doctorConsultationServiceMapperDto.getAssoCompanyId()==null)
				{
					
					query = query.concat(" union " 
							 + " select " 
							 + " 	doctorcons.doctor_consultation_service_mapper_id as \"doctorConsultationServiceMapperId\", " 
							 + " 	doctorcons.new_visit_service_id as \"newVisitServiceId\", " 
							 + " 	doctorcons.new_visit_rate as \"newVisitRate\", " 
							 + " 	doctorcons.followup_visit_service_id as \"followupVisitServiceId\", " 
							 + " 	doctorcons.follow_up_visit_rate as \"followupVisitRate\", " 
							 + " 	doctorcons.secondary_visit_service_id as \"secondaryVisitServiceId\", " 
							 + " 	doctorcons.secondary_visit_rate as \"secondaryVisitRate\", " 
							 + " 	speciality4.speciality_name as \"newVisitSpecialityName\", " 
							 + " 	speciality5.speciality_name as \"followupVisitSpecialityName\", " 
							 + " 	speciality.speciality_name as \"secondarySpecialityName\", " 
							 + " 	servicemas1.service_standard_name as \"newVisitServiceName\", " 
							 + " 	servicemas2.service_standard_name as \"followupVisitServiceName\", " 
							 + " 	servicemas3.service_standard_name as \"secondaryVisitServiceName\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic7_.is_ref_doctor_share, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic7_ " 
							 + " 	where " 
							 + " 		unitservic7_.service_master_id = doctorcons.new_visit_service_id " 
							 + " 		and unitservic7_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic7_.status = 'A') as \"newVisitReferralShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic8_.is_ref_doctor_share, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic8_ " 
							 + " 	where " 
							 + " 		unitservic8_.service_master_id = doctorcons.followup_visit_service_id " 
							 + " 		and unitservic8_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic8_.status = 'A') as \"followUpVisitReferralShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic9_.is_ref_doctor_share, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic9_ " 
							 + " 	where " 
							 + " 		unitservic9_.service_master_id = doctorcons.secondary_visit_service_id " 
							 + " 		and unitservic9_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic9_.status = 'A') as \"secondaryVisitReferralShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic10_.is_doc_req, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic10_ " 
							 + " 	where " 
							 + " 		unitservic10_.service_master_id = doctorcons.new_visit_service_id " 
							 + " 		and unitservic10_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic10_.status = 'A') as \"newVisitDoctorShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic11_.is_doc_req, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic11_ " 
							 + " 	where " 
							 + " 		unitservic11_.service_master_id = doctorcons.followup_visit_service_id " 
							 + " 		and unitservic11_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic11_.status = 'A') as \"followUpVisitDoctorShare\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		coalesce(unitservic12_.is_doc_req, 'N') " 
							 + " 	from " 
							 + " 		service.t_unit_service_master_details unitservic12_ " 
							 + " 	where " 
							 + " 		unitservic12_.service_master_id = doctorcons.secondary_visit_service_id " 
							 + " 		and unitservic12_.unit_id = doctorcons.unit_id " 
							 + " 		and unitservic12_.status = 'A') as \"secondaryVisitDoctorShare\", " 
							 + " 	3 as \"type\"	 " 
							 + " from " 
							 + " 	doctor.m_doctor_consultation_service_mapper doctorcons " 
							 + " inner join service.m_service_master servicemas1 on " 
							 + " 	doctorcons.new_visit_service_id = servicemas1.service_master_id " 
							 + " inner join doctor.m_speciality_master speciality4 on " 
							 + " 	servicemas1.speciality_id = speciality4.speciality_id " 
							 + " inner join service.m_service_master servicemas2 on " 
							 + " 	doctorcons.followup_visit_service_id = servicemas2.service_master_id " 
							 + " inner join doctor.m_speciality_master speciality5 on " 
							 + " 	servicemas2.speciality_id = speciality5.speciality_id " 
							 + " inner join service.m_service_master servicemas3 on " 
							 + " 	doctorcons.secondary_visit_service_id = servicemas3.service_master_id " 
							 + " inner join doctor.m_speciality_master speciality on " 
							 + " 	servicemas3.speciality_id = speciality.speciality_id " 
							 + " where " 
							 + " 	doctorcons.organization_id = :orgId " 
							 + " 	and doctorcons.unit_id = :unitId " 
							 + " 	and doctorcons.payment_entitlement_id = 3 " 
							 + " 	and doctorcons.status = 'A' " 
							 + " 	and doctorcons.speciality_id =:specialityId " 
							 + " 	and doctorcons.doctor_id = :doctorId "
							 + " 	and doctorcons.company_id =:companyId "
							 + " 	and doctorcons.asso_company_id = 0 ");
				}
				
				query = query.concat(" order by \"type\"  " 
						 + " 	desc limit 1 ");


				Query createQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("orgId", doctorConsultationServiceMapperDto.getOrganizationId())
						.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
						.setParameter("doctorId", doctorConsultationServiceMapperDto.getDeptWiseAppointment().equals('Y') ? 0 : doctorConsultationServiceMapperDto.getDoctorId())
						.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
						.setParameter("specialityId", doctorConsultationServiceMapperDto.getSpecialityId());

			if((doctorConsultationServiceMapperDto.getCompanyId()!=null && doctorConsultationServiceMapperDto.getCompanyId()>0)
					&& (doctorConsultationServiceMapperDto.getAssoCompanyId()!=null	&& doctorConsultationServiceMapperDto.getAssoCompanyId()>0))
			{
				createQuery.setParameter("companyId",doctorConsultationServiceMapperDto.getCompanyId());
				createQuery.setParameter("assoCompanyId",doctorConsultationServiceMapperDto.getAssoCompanyId());

			}

			if(doctorConsultationServiceMapperDto.getCompanyId()!=null && doctorConsultationServiceMapperDto.getCompanyId() > 0 && doctorConsultationServiceMapperDto.getAssoCompanyId()==null) {
				createQuery.setParameter("companyId",doctorConsultationServiceMapperDto.getCompanyId());
			}
				List<DoctorConsultationServiceMapperDto> doctorConsultationServiceMapper = createQuery.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).list();

				return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorConsultationServiceMapper, null);

		}catch (HibernateException he) {
			logger.error("Error while fetching doctor consultation service by speciality and doctor id", he);
		} catch (Exception e) {
			logger.error("Error while fetching doctor consultation service by speciality and doctor id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDoctorConsultationServiceMapperStatus(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			DoctorConsultationServiceMapper doctorConsultationServiceMapper = findById(doctorConsultationServiceMapperDto.getDoctorConsultationServiceMapperId());
			doctorConsultationServiceMapper.setStatus(doctorConsultationServiceMapperDto.getStatus());
			doctorConsultationServiceMapper.setUpdatedBy(doctorConsultationServiceMapperDto.getUpdatedBy());
			doctorConsultationServiceMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorConsultationServiceMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		
		update(doctorConsultationServiceMapper);
		return new Response<GenderMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, doctorConsultationServiceMapper);
		}catch (HibernateException he) {
			logger.error("Error while updating doctor consultation service mapper status", he);
		} catch (Exception e) {
			logger.error("Error while updating doctor consultation service mapper status", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public DoctorConsultationServiceMapperDto getDoctorConsultationServiceBySpecialityAndDoctorIdForUpdatePackage(EncounterMasterDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {

			
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapper = null;
			
			
			
			if(doctorConsultationServiceMapperDto.getDeptWiseAppointment().equals('Y'))
			{
				
				StringBuilder strinQ = new StringBuilder(" select " 
						 + " 	doctorcons0_.new_visit_rate as \"newVisitRate\", " 
						 + " 	doctorcons0_.follow_up_visit_rate as \"followupVisitRate\", " 
						 + " 	doctorcons0_.secondary_visit_rate as \"secondaryVisitRate\" " 
						 + " from " 
						 + " 	doctor.m_doctor_consultation_service_mapper doctorcons0_ " 
						 + " where " 
						 + " 	doctorcons0_.unit_id =:unitId " 
						 + " 	and doctorcons0_.payment_entitlement_id =:paymentEntitlementId " 
						 + " 	and doctorcons0_.status = 'A' " 
						 + " 	and doctorcons0_.speciality_id =:specialityId " );
				
				if(doctorConsultationServiceMapperDto.getCompanyId()!= null){
					strinQ.append(" AND doctorcons0_.company_id=:companyId ");
				}
				
				if(doctorConsultationServiceMapperDto.getAssoCompanyId()!= null){
					strinQ.append(" AND doctorcons0_.asso_company_id=:assoCompanyId "+doctorConsultationServiceMapperDto.getAssoCompanyId());
				}
				strinQ.append(" order by  doctorcons0_.doctor_consultation_service_mapper_id desc limit 1 ");

				Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(strinQ.toString())
						.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
						.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
						.setParameter("specialityId", doctorConsultationServiceMapperDto.getDeptId());

				if(doctorConsultationServiceMapperDto.getCompanyId()!= null){
					createQuery.setParameter("companyId",doctorConsultationServiceMapperDto.getCompanyId());
				}
				if(doctorConsultationServiceMapperDto.getAssoCompanyId()!= null){
					createQuery.setParameter("assoCompanyId",doctorConsultationServiceMapperDto.getAssoCompanyId());
				}
				doctorConsultationServiceMapper= (DoctorConsultationServiceMapperDto)createQuery.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).uniqueResult();
				if(doctorConsultationServiceMapper != null){
					
					return doctorConsultationServiceMapper;
					
				}else{
					
					StringBuilder strinQ1 = new StringBuilder(" select " 
							 + " 	doctorcons0_.new_visit_rate as \"newVisitRate\", " 
							 + " 	doctorcons0_.follow_up_visit_rate as \"followupVisitRate\", " 
							 + " 	doctorcons0_.secondary_visit_rate as \"secondaryVisitRate\" " 
							 + " from " 
							 + " 	doctor.m_doctor_consultation_service_mapper doctorcons0_ " 
							 + " where " 
							 + " 	doctorcons0_.unit_id =:unitId " 
							 + " 	and doctorcons0_.payment_entitlement_id =:paymentEntitlementId " 
							 + " 	and doctorcons0_.status = 'A' " 
							 + " 	and doctorcons0_.speciality_id =:specialityId " );
					
					if(doctorConsultationServiceMapperDto.getAssoCompanyId()!= null){
						strinQ1.append("AND doctorcons0_.asso_company_id=:assoCompanyId ");
					}
					
					strinQ1.append(" order by  doctorcons0_.doctor_consultation_service_mapper_id desc limit 1 ");

					Query createSQL  = sessionFactory.getCurrentSession().createSQLQuery(strinQ1.toString())
							.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
							.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
							.setParameter("specialityId", doctorConsultationServiceMapperDto.getDeptId());

					if(doctorConsultationServiceMapperDto.getAssoCompanyId()!= null){
						createSQL.setParameter("assoCompanyId",doctorConsultationServiceMapperDto.getAssoCompanyId());
					}

					doctorConsultationServiceMapper= (DoctorConsultationServiceMapperDto)createSQL.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).uniqueResult();
					
					
					if(doctorConsultationServiceMapper != null){
						
						return doctorConsultationServiceMapper;
						
					}else{
						
						StringBuilder strinQ2 = new StringBuilder(" select " 
								 + " 	doctorcons0_.new_visit_rate as \"newVisitRate\", " 
								 + " 	doctorcons0_.follow_up_visit_rate as \"followupVisitRate\", " 
								 + " 	doctorcons0_.secondary_visit_rate as \"secondaryVisitRate\" " 
								 + " from " 
								 + " 	doctor.m_doctor_consultation_service_mapper doctorcons0_ " 
								 + " where " 
								 + " 	doctorcons0_.unit_id =:unitId " 
								 + " 	and doctorcons0_.payment_entitlement_id =:paymentEntitlementId " 
								 + " 	and doctorcons0_.status = 'A' " 
								 + " 	and doctorcons0_.speciality_id =:specialityId and doctorcons0_.company_id = 0 and doctorcons0_.asso_company_id = 0 " );
						
						strinQ2.append(" order by  doctorcons0_.doctor_consultation_service_mapper_id desc limit 1 ");
						doctorConsultationServiceMapper= (DoctorConsultationServiceMapperDto)sessionFactory.getCurrentSession().createSQLQuery(strinQ2.toString())
								.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
								.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
								.setParameter("specialityId", doctorConsultationServiceMapperDto.getDeptId())
								.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).uniqueResult();
						
						return doctorConsultationServiceMapper;
					}
					
				}
				
				
				
			}else{
				
				StringBuilder strinQ = new StringBuilder(" select " 
						 + " 	doctorcons0_.new_visit_rate as \"newVisitRate\", " 
						 + " 	doctorcons0_.follow_up_visit_rate as \"followupVisitRate\", " 
						 + " 	doctorcons0_.secondary_visit_rate as \"secondaryVisitRate\" " 
						 + " from " 
						 + " 	doctor.m_doctor_consultation_service_mapper doctorcons0_ " 
						 + " where " 
						 + " 	doctorcons0_.unit_id =:unitId " 
						 + " 	and doctorcons0_.payment_entitlement_id =:paymentEntitlementId " 
						 + " 	and doctorcons0_.status = 'A' " 
						 + " 	and doctorcons0_.speciality_id =:specialityId " );
				
				if(doctorConsultationServiceMapperDto.getCompanyId()!= null){
					strinQ.append(" AND doctorcons0_.company_id=:companyId ");
				}

				if(doctorConsultationServiceMapperDto.getAssoCompanyId()!= null){
					strinQ.append(" AND doctorcons0_.asso_company_id=:assoCompanyId ");
				}
				
				strinQ.append(" AND doctorcons0_.doctor_id=:doctorId ");
				strinQ.append(" order by  doctorcons0_.doctor_consultation_service_mapper_id desc limit 1 ");
				
				Query createQ = sessionFactory.getCurrentSession().createSQLQuery(strinQ.toString())
						.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
						.setParameter("doctorId", doctorConsultationServiceMapperDto.getDoctorId())
						.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
						.setParameter("specialityId", doctorConsultationServiceMapperDto.getDeptId());

				if(doctorConsultationServiceMapperDto.getCompanyId()!= null){
					createQ.setParameter("companyId",doctorConsultationServiceMapperDto.getCompanyId());
				}

				if(doctorConsultationServiceMapperDto.getAssoCompanyId()!= null){
					createQ.setParameter("assoCompanyId",doctorConsultationServiceMapperDto.getAssoCompanyId());
				}

				doctorConsultationServiceMapper= (DoctorConsultationServiceMapperDto)createQ.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).uniqueResult();


				if(doctorConsultationServiceMapper != null){
					
					return doctorConsultationServiceMapper;
					
				}else{
					
					
					StringBuilder strinQ1 = new StringBuilder(" select " 
							 + " 	doctorcons0_.new_visit_rate as \"newVisitRate\", " 
							 + " 	doctorcons0_.follow_up_visit_rate as \"followupVisitRate\", " 
							 + " 	doctorcons0_.secondary_visit_rate as \"secondaryVisitRate\" " 
							 + " from " 
							 + " 	doctor.m_doctor_consultation_service_mapper doctorcons0_ " 
							 + " where " 
							 + " 	doctorcons0_.unit_id =:unitId " 
							 + " 	and doctorcons0_.payment_entitlement_id =:paymentEntitlementId " 
							 + " 	and doctorcons0_.status = 'A' " 
							 + " 	and doctorcons0_.speciality_id =:specialityId " );
					
					if(doctorConsultationServiceMapperDto.getCompanyId()!= null){
						strinQ1.append(" AND doctorcons0_.company_id=:companyId ");
					}
					
					
					strinQ1.append(" AND doctorcons0_.doctor_id=:doctorId ");
					strinQ1.append(" order by  doctorcons0_.doctor_consultation_service_mapper_id desc limit 1 ");
					
					Query createQue = sessionFactory.getCurrentSession().createSQLQuery(strinQ1.toString())
							.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
							.setParameter("doctorId", doctorConsultationServiceMapperDto.getDoctorId())
							.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
							.setParameter("specialityId", doctorConsultationServiceMapperDto.getDeptId());

					if(doctorConsultationServiceMapperDto.getCompanyId()!= null){
						createQue.setParameter("companyId",doctorConsultationServiceMapperDto.getCompanyId());
					}

					doctorConsultationServiceMapper= (DoctorConsultationServiceMapperDto)createQue.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).uniqueResult();
					
					if(doctorConsultationServiceMapper != null){
						
						return doctorConsultationServiceMapper;
						
					}else{
						StringBuilder strinQ2 = new StringBuilder(" select " 
								 + " 	doctorcons0_.new_visit_rate as \"newVisitRate\", " 
								 + " 	doctorcons0_.follow_up_visit_rate as \"followupVisitRate\", " 
								 + " 	doctorcons0_.secondary_visit_rate as \"secondaryVisitRate\" " 
								 + " from " 
								 + " 	doctor.m_doctor_consultation_service_mapper doctorcons0_ " 
								 + " where " 
								 + " 	doctorcons0_.unit_id =:unitId " 
								 + " 	and doctorcons0_.payment_entitlement_id =:paymentEntitlementId " 
								 + " 	and doctorcons0_.status = 'A' " 
								 + " 	and doctorcons0_.speciality_id =:specialityId " );
						
						
						strinQ2.append(" AND doctorcons0_.doctor_id=:doctorId and doctorcons0_.company_id = 0 and doctorcons0_.asso_company_id = 0 ");
						strinQ2.append(" order by  doctorcons0_.doctor_consultation_service_mapper_id desc limit 1 ");
						
						doctorConsultationServiceMapper= (DoctorConsultationServiceMapperDto)sessionFactory.getCurrentSession().createSQLQuery(strinQ2.toString())
								.setParameter("unitId",doctorConsultationServiceMapperDto.getUnitId())
								.setParameter("doctorId", doctorConsultationServiceMapperDto.getDoctorId())
								.setParameter("paymentEntitlementId", doctorConsultationServiceMapperDto.getPaymentEntitlementId())
								.setParameter("specialityId", doctorConsultationServiceMapperDto.getDeptId())
								.setResultTransformer(Transformers.aliasToBean(DoctorConsultationServiceMapperDto.class)).uniqueResult();
						
						return doctorConsultationServiceMapper;
					}
					
				}
				
				
				
				
				
			}
			
		}catch (HibernateException he) {
			logger.error("Error while fetching doctor consultation service by speciality and doctor id for update package", he);
		} catch (Exception e) {
			logger.error("Error while fetching doctor consultation service by speciality and doctor id for update package", e);
		}
		return null;
	}
	
	

	@Override
	public Response getDoctorConsultationServiceMapperListCount(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			
			StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_CONSULTATION_SERVICE_MAPPER_LIST_COUNT").getQueryString());
			
			if(doctorConsultationServiceMapperDto.getTabNumber()==1) {
				query.append(" AND dcsm.doctorId is not null");
			}else if(doctorConsultationServiceMapperDto.getTabNumber()==2) {
				query.append(" AND dcsm.doctorId is not null");
			}
			Long count = (Long)sessionFactory.getCurrentSession()
					.createQuery(query.toString())
					.setParameter("orgId", doctorConsultationServiceMapperDto.getOrganizationId())
					.setParameter("unitId", doctorConsultationServiceMapperDto.getUnitId())
					.uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);
		}catch (HibernateException he) {
			logger.error("Error while fetching doctor consultation service mapper list count", he);
		} catch (Exception e) {
			logger.error("Error while fetching doctor consultation service mapper list count", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorCharges(DoctorConsChargesForMyLife doctorConsultationServiceMapperDto)
			throws ApplicationException {
		try
        {
			List<DoctorConsChargesForMyLife> doctochargesList = sessionFactory.getCurrentSession()
					.createSQLQuery(
							"SELECT DISTINCT docMapper.doctor_id AS \"doctorId\", " +
									"spmstr.speciality_name AS \"specialityName\", " +
									"docMapper.new_visit_rate AS \"newVisitRate\", " +
									"docMapper.follow_up_visit_rate AS \"followUpVisitRate\", " +
									"docMapper.speciality_id AS \"specialityId\" " +
									"FROM doctor.m_doctor_consultation_service_mapper docMapper " +
									"INNER JOIN doctor.m_speciality_master spmstr ON docMapper.speciality_id = spmstr.speciality_id " +
									"WHERE docMapper.payment_entitlement_id = :paymentEntitlementId " +
									"AND docMapper.status = :status " +
									"AND docMapper.doctor_id = :doctorId " +
									"AND docMapper.unit_id = :unitId " +
									"AND docMapper.organization_id = :organizationId " +
									"AND docMapper.speciality_id = :specialityId")
					.setParameter("paymentEntitlementId", 1)  // Replace hardcoded value
					.setParameter("status", "A")              // Replace hardcoded value
					.setParameter("doctorId", doctorConsultationServiceMapperDto.getDoctorId())
					.setParameter("unitId", doctorConsultationServiceMapperDto.getUnitId())
					.setParameter("organizationId", doctorConsultationServiceMapperDto.getHospitalId())
					.setParameter("specialityId", doctorConsultationServiceMapperDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(DoctorConsChargesForMyLife.class))
					.list();

				return new Response<>(SUCCESS, SUCCESS_CODE, null, doctochargesList, null);
        } catch (Exception e)
        {
            logger.error("Error while fetching doctor charges", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

}
