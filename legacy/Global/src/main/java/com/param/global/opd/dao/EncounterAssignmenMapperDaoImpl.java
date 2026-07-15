package com.param.global.opd.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.opd.EncounterDoctorConsultantsMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.unit.dto.DoctorCabinMapperDto;
import com.param.opd.coversheet.model.EncounterDetails;
import com.param.opd.encounter.dto.EncounterAssignmenMapperDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EncounterAssignmenMapperDaoImpl extends GenericDao<EncounterDetails, Integer> implements IEncounterAssignmenMapperDao,ICommonConstants {
	final static Logger logger = Logger.getLogger(EncounterAssignmenMapperDaoImpl.class);


	
	public EncounterAssignmenMapperDaoImpl() {
		super(EncounterDetails.class);
		// TODO Auto-generated constructor stub
	}

	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Override
	public Response saveEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException {
		try
		{
			Session s= sessionFactory.getCurrentSession();
			
			if(encounterAssignmenMapperDto.getDoctorId()!=null && encounterAssignmenMapperDto.getDoctorId()>0)
			{
				if(encounterAssignmenMapperDto.getEncounterId()!=null)
				{
					sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
							+ "doctorId=:doctId, "
							+ "preferredDocId=:doctId "
							+ "WHERE encounterId=:encounterId ")
						.setParameter("doctId", encounterAssignmenMapperDto.getDoctorId())
						.setParameter("encounterId",encounterAssignmenMapperDto.getEncounterId())
					.executeUpdate();
				
					iOrderDetailsMasterService.updateOrderDetailsForDeptWiseAppointment(encounterAssignmenMapperDto.getOrderDetailsId(),encounterAssignmenMapperDto.getDoctorId(),null);
				}
				if(encounterAssignmenMapperDto.getAdmissionId()!=null && encounterAssignmenMapperDto.getConsultDoctorId()!=null)
				{
					sessionFactory.getCurrentSession().createSQLQuery("update opd.t_consult_doctor set referred_status='Completed'"
						+ " where consult_doctor_id=:consultDoctorId ")
							.setParameter("consultDoctorId",encounterAssignmenMapperDto.getConsultDoctorId())
					.executeUpdate();
					if(encounterAssignmenMapperDto.getOrderDetailsId()!=null && encounterAssignmenMapperDto.getDoctorId()!=null) 
					{
						iOrderDetailsMasterService.updateOrderDetailsForDeptWiseAppointment(encounterAssignmenMapperDto.getOrderDetailsId(),encounterAssignmenMapperDto.getDoctorId(),null);
					}
				}
				
				
				
				
			}
			
			
			if(encounterAssignmenMapperDto.getOrderDetailsId()!=null) {
				sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster orderDetObj set orderDetObj.serviceRendered=1"
						+ " WHERE orderDetObj.orderDetailsId=:orderDetailId and orderDetObj.serviceRendered = 0")
					.setParameter("orderDetailId", encounterAssignmenMapperDto.getOrderDetailsId())
				.executeUpdate();
				}
			
			EncounterDetails encounterDetailsMapp =null;
			if(encounterAssignmenMapperDto.getEncounterId()!=null)
			{
				encounterDetailsMapp = (EncounterDetails) s.getNamedQuery("GET_ENCOUNTER_LIST_BY_ENCOUNTER_ID")
				.setParameter("encounterId", encounterAssignmenMapperDto.getEncounterId())
				.setResultTransformer(Transformers.aliasToBean(EncounterDetails.class)).uniqueResult();
			}
			
			if(encounterAssignmenMapperDto.getAllowOtherDoctorsToConsult()!=null && encounterAssignmenMapperDto.getAllowOtherDoctorsToConsult()=='Y')
			{
				Criteria c=sessionFactory.getCurrentSession().createCriteria(EncounterDoctorConsultantsMapper.class); 
					c.add(Restrictions.eq("encounterId", encounterAssignmenMapperDto.getEncounterId()));
					c.add(Restrictions.eq("doctorId", encounterAssignmenMapperDto.getOtherDoctorId()));
				List<EncounterDoctorConsultantsMapper> countList = c.list();
				
				if(countList.size()==0)
				{
					EncounterDoctorConsultantsMapper encounterDoctorConsultantsMapper = new EncounterDoctorConsultantsMapper();
						encounterDoctorConsultantsMapper.setEncounterId(encounterAssignmenMapperDto.getEncounterId());
						encounterDoctorConsultantsMapper.setDoctorId(encounterAssignmenMapperDto.getOtherDoctorId());
						encounterDoctorConsultantsMapper.setCreatedDate(TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					sessionFactory.getCurrentSession().save(encounterDoctorConsultantsMapper);	
				}
			}
			
			if(encounterDetailsMapp != null && encounterAssignmenMapperDto.getEncounterId().equals(encounterDetailsMapp.getEncounterId()) )
			{
				EncounterDetails encounter= findById(encounterDetailsMapp.getEncounterDetailsMapperId());
					encounter.setIsAssignmentCompStatus(encounterAssignmenMapperDto.getIsAssignmentCompStatus());
					encounter.setAssignmentStartBy(encounterAssignmenMapperDto.getAssignmentStartBy() == null ? encounter.getAssignmentStartBy():encounterAssignmenMapperDto.getAssignmentStartBy());
					encounter.setAssignmentStartRoleId(encounterAssignmenMapperDto.getAssignmentStartRoleId() == null ? encounter.getAssignmentStartRoleId() : encounterAssignmenMapperDto.getAssignmentStartRoleId());
					encounter.setAssignmentStartTime(encounterAssignmenMapperDto.getAssignmentStartTime() == null ? encounter.getAssignmentStartTime() : TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setAssignmentStopBy(encounterAssignmenMapperDto.getAssignmentStopBy() == null ? encounter.getAssignmentStopBy() :	encounterAssignmenMapperDto.getAssignmentStopBy());
					encounter.setAssignmentStopRoleId(encounterAssignmenMapperDto.getAssignmentStopRoleId() == null ? encounter.getAssignmentStopRoleId() : encounterAssignmenMapperDto.getAssignmentStopRoleId() );
					encounter.setAssignmentStopTime(encounterAssignmenMapperDto.getAssignmentStopTime() == null ? encounter.getAssignmentStopTime() : TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setConsultationStartBy(encounterAssignmenMapperDto.getConsultationStartBy() == null ? encounter.getConsultationStartBy() : encounterAssignmenMapperDto.getConsultationStartBy() );
					encounter.setConsultationStartRoleId(encounterAssignmenMapperDto.getConsultationStartRoleId() == null  ? encounter.getConsultationStartRoleId() : encounterAssignmenMapperDto.getConsultationStartRoleId());
					encounter.setConsultationStartTime(encounterAssignmenMapperDto.getConsultationStartTime() == null ? encounter.getConsultationStartTime() : TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setConsultationStopBy(encounterAssignmenMapperDto.getConsultationStopBy() == null ? encounter.getConsultationStopBy() : encounterAssignmenMapperDto.getConsultationStopBy());
					encounter.setConsultationStopRoleId(encounterAssignmenMapperDto.getConsultationStopRoleId() == null ? encounter.getConsultationStopRoleId() : encounterAssignmenMapperDto.getConsultationStopRoleId());
					encounter.setConsultationStopTime(encounterAssignmenMapperDto.getConsultationStopTime() == null ? encounter.getConsultationStopTime() :	TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setEncounterId(encounterAssignmenMapperDto.getEncounterId());
					encounter.setDoctorId(encounterAssignmenMapperDto.getDoctorId());
					//encounter.setAdmissionId(encounterAssignmenMapperDto.getAdmissionId());
					encounter.setIsPriority(encounterAssignmenMapperDto.getIsPriority());
					encounter.setPatientId(encounterAssignmenMapperDto.getPatientId());
					encounter.setUnitId(encounterAssignmenMapperDto.getUnitId());
					encounter.setOrganizationId(encounterAssignmenMapperDto.getOrganizationId());
					encounter.setIsNursingAssessmentSkipped(encounterAssignmenMapperDto.getIsNursingAssessmentSkipped()!=null ? encounterAssignmenMapperDto.getIsNursingAssessmentSkipped() : 'N');
					encounter.setIsConsultationStatus(encounterAssignmenMapperDto.getIsConsultationStatus());
				encounter = save(encounter);
				
			}else{
			
				EncounterDetails encounter = new EncounterDetails();
					encounter.setIsAssignmentCompStatus(encounterAssignmenMapperDto.getIsAssignmentCompStatus());
					encounter.setAssignmentStartBy(encounterAssignmenMapperDto.getAssignmentStartBy() == null ? null:encounterAssignmenMapperDto.getAssignmentStartBy());
					encounter.setAssignmentStartRoleId(encounterAssignmenMapperDto.getAssignmentStartRoleId() == null ? null : encounterAssignmenMapperDto.getAssignmentStartRoleId());
					encounter.setAssignmentStartTime(encounterAssignmenMapperDto.getAssignmentStartTime() == null ? null :TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setAssignmentStopBy(encounterAssignmenMapperDto.getAssignmentStopBy() == null ? null : encounterAssignmenMapperDto.getAssignmentStopBy());
					encounter.setAssignmentStopRoleId(encounterAssignmenMapperDto.getAssignmentStopRoleId() == null ? null : encounterAssignmenMapperDto.getAssignmentStopRoleId() );
					encounter.setAssignmentStopTime(encounterAssignmenMapperDto.getAssignmentStopTime() == null ? null : TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setConsultationStartBy(encounterAssignmenMapperDto.getConsultationStartBy() == null ? null : encounterAssignmenMapperDto.getConsultationStartBy() );
					encounter.setConsultationStartRoleId(encounterAssignmenMapperDto.getConsultationStartRoleId() == null  ? null : encounterAssignmenMapperDto.getConsultationStartRoleId());
					encounter.setConsultationStartTime(encounterAssignmenMapperDto.getConsultationStartTime() == null ? null : TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setConsultationStopBy(encounterAssignmenMapperDto.getConsultationStopBy() == null ? null : encounterAssignmenMapperDto.getConsultationStopBy());
					encounter.setConsultationStopRoleId(encounterAssignmenMapperDto.getConsultationStopRoleId() == null ? null : encounterAssignmenMapperDto.getConsultationStopRoleId());
					encounter.setConsultationStopTime(encounterAssignmenMapperDto.getConsultationStopTime() == null ? null : TimeZoneComponent.getDateByzone(encounterAssignmenMapperDto.getUnitId()));
					encounter.setEncounterId(encounterAssignmenMapperDto.getEncounterId());
					encounter.setAdmissionId(encounterAssignmenMapperDto.getAdmissionId());
					encounter.setDoctorId(encounterAssignmenMapperDto.getDoctorId());
					encounter.setPatientId(encounterAssignmenMapperDto.getPatientId());
					encounter.setUnitId(encounterAssignmenMapperDto.getUnitId());
					encounter.setOrganizationId(encounterAssignmenMapperDto.getOrganizationId());
					encounter.setIsPriority(encounterAssignmenMapperDto.getIsPriority());
					encounter.setIsConsultationStatus(encounterAssignmenMapperDto.getIsConsultationStatus());
					encounter.setIsNursingAssessmentSkipped(encounterAssignmenMapperDto.getIsNursingAssessmentSkipped()!=null ? encounterAssignmenMapperDto.getIsNursingAssessmentSkipped() : 'N');
				encounter = save(encounter);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("saveEncounterAssignment failed", he);
		} catch (Exception e) {
		logger.error("saveEncounterAssignment failed", e);
		}
		
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response updateEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException {
		try
		{
			
			if(encounterAssignmenMapperDto.getIsFromCoverSheet()!=null && encounterAssignmenMapperDto.getIsFromCoverSheet().equals('Y'))
			{
				Date consultionstopTime=GlobalCommonDateUtils.getDate(encounterAssignmenMapperDto.getConsultationStopTime(), "dd-MM-yyyy HH:mm:ss");
				sessionFactory.getCurrentSession().createQuery("UPDATE EncounterDetails set "
						+ "isConsultationStatus=:isConsultationStatus, "
						+ "consultationStopTime=:consultionstopTime , "
						+ "consultationStopBy=:consultationStopBy, "
						+ "consultationStopRoleId=:consultationStopRoleId "
						+ "WHERE encounterId=:encounterId ")
				.setParameter("isConsultationStatus", encounterAssignmenMapperDto.getIsConsultationStatus())
				.setParameter("consultationStopBy", encounterAssignmenMapperDto.getConsultationStopBy())
				.setParameter("consultationStopRoleId", encounterAssignmenMapperDto.getConsultationStopRoleId())
				.setParameter("encounterId",encounterAssignmenMapperDto.getEncounterId())
				.setParameter("consultionstopTime",consultionstopTime)
				.executeUpdate();
				//System.out.println(encounterAssignmenMapperDto.getConsultationStopTime());
			}else
			{

				Integer count = sessionFactory.getCurrentSession()
						.createSQLQuery(
								"update opd.t_encounter_details set "
										+ "is_assignment_comp_status = :isAssignmentCompStatus, "
										+ "assignment_stop_time = TO_TIMESTAMP(:assignmentStopTime, 'DD-MM-YYYY HH24:MI:SS'), "
										+ "assignment_stop_by = :assignmentStopBy, "
										+ "assignment_stop_role_id = :assignmentStopRoleId "
										+ "where encounter_id = :encounterId")
						.setParameter("isAssignmentCompStatus", encounterAssignmenMapperDto.getIsAssignmentCompStatus())
						.setParameter("assignmentStopTime", encounterAssignmenMapperDto.getAssignmentStopTime())
						.setParameter("assignmentStopBy", encounterAssignmenMapperDto.getAssignmentStopBy())
						.setParameter("assignmentStopRoleId", encounterAssignmenMapperDto.getAssignmentStopRoleId())
						.setParameter("encounterId", encounterAssignmenMapperDto.getEncounterId())
						.executeUpdate();


			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateEncounterAssignment failed", he);
		} catch (Exception e) {
		logger.error("updateEncounterAssignment failed", e);
		}
		
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response getOldListEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)
			throws ApplicationException {
			try
			{
					Session statlessSession = sessionFactory.getCurrentSession();
					List<EncounterAssignmenMapperDto> listEncounterAssignmenMapperDto = statlessSession.getNamedQuery("GET_OLD_ENCOUNTER_LIST")
							.setParameter("unitId", encounterAssignmenMapperDto.getUnitId())
							.setParameter("organizationId", encounterAssignmenMapperDto.getOrganizationId())
							.setParameter("patientId", encounterAssignmenMapperDto.getPatientId())
							.setResultTransformer(Transformers.aliasToBean(EncounterAssignmenMapperDto.class)).list();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, listEncounterAssignmenMapperDto, null);
			}catch(HibernateException he) { 
				throw new HibernateException(he.getMessage());
			}catch(Exception e) {
			logger.error("getOldListEncounterAssignment failed", e);
			}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getOldConsulataions(Integer admissionId) throws ApplicationException {
		try{
			List<EncounterAssignmenMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_OLD_CONSULATAIONS_AGAINST_ADMISSION")
				.setParameter("admissionId", admissionId)
				.setResultTransformer(Transformers.aliasToBean(EncounterAssignmenMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getOldConsulataions failed", he);
	} catch (Exception e) {
	logger.error("getOldConsulataions failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);


	}

	@Override
	public Response getCabinListByDoctorId(Integer doctorId,Integer unitId) throws ApplicationException {
		try{
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_CABIN_BY_DOC_ID")
				.setParameter("doctorId", doctorId)
     			.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getCabinListByDoctorId failed", he);
	} catch (Exception e) {
	logger.error("getCabinListByDoctorId failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinListByRMOMappedDoctorList(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_CABIN_BY_DOC_ID").getQueryString();
			String finalQuery = query.replace("= :doctorId ", " IN (:doctorList)");
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession().createQuery(finalQuery.toString())
				.setParameterList("doctorList", encounterMasterDto.getDoctorList())
				.setParameter("unitId", encounterMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getCabinListByRMOMappedDoctorList failed", he);
	} catch (Exception e) {
	logger.error("getCabinListByRMOMappedDoctorList failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
