package com.param.adt.discharge.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.model.Discharge;
import com.param.adt.mortuary.dao.IMortuaryDao;
import com.param.adt.mortuary.model.MortuaryRequest;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DischargeDto;
import com.param.global.dto.MortuaryDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.utility.ErrorCorrelationUtil;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DueForDischargeDaoImpl extends GenericDao<Discharge, Integer>
		implements IDueForDischargeDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(DueForDischargeDaoImpl.class);

	public DueForDischargeDaoImpl() {
		super(Discharge.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IMortuaryDao iMortuaryDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Override
	public Response saveDischargeRequest(DischargeDto dischargeDto) throws ApplicationException {
		try {
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(dischargeDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(dischargeDto.getUnitId());
			com.param.global.common.Response configRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if (configRes.getObject() != null) {
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) configRes.getObject();
			}
			
			Discharge discharge = mapper.map(dischargeDto, Discharge.class, "DischargeDto_to_Discharge");
			
			if(unitApplicationConfigurationDto.getSkipReadyForBilling().equals('Y'))
			{
				discharge.setDischargeStatusId(3);
			}
			
			if(dischargeDto.getDischargeTypeId()==3)
				discharge.setIsDeath('Y');
			    
			else
				discharge.setIsDeath('N');
			discharge.setCreatedDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()));
			discharge.setUpdatedDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()));
			save(discharge);

			// <-----Updating is discharge in admission details to P(Pending)
			Session session = sessionFactory.getCurrentSession();
			Query query = session
					.createQuery("update AdmissionDetails adtl set adtl.isDischarge='P' WHERE adtl.status='A' AND adtl.admissionId="
							+ dischargeDto.getAdmissionId());
			query.executeUpdate();
			
			//AdmissionDetails details = (AdmissionDetails) sessionFactory.getCurrentSession().get(AdmissionDetails.class, dischargeDto.getAdmissionId());
			// ----->
			  //update patient if discharge type id is 4 i.e absconding 
			//start
			if(dischargeDto.getDischargeTypeId()!=null && dischargeDto.getDischargeTypeId()==4){
				Query abscondQuery = session.createQuery(
						"update PatientRegistration  set isAbsconded='Y' "					
						+ "where patientId=:patientId");						
				abscondQuery.setParameter("patientId",dischargeDto.getPatientId());
				abscondQuery.executeUpdate();
			}
			if(dischargeDto.getDischargeTypeId()!=null && dischargeDto.getDischargeTypeId()==3 && dischargeDto.getIsSendToMortury() == 'Y'){
				MortuaryDto mortuaryDto = new MortuaryDto();
				mortuaryDto.setOrganizationId(dischargeDto.getOrganizationId());
				mortuaryDto.setUnitId(dischargeDto.getUnitId());
				mortuaryDto.setAdmissionId(dischargeDto.getAdmissionId());
				mortuaryDto.settPatientId(dischargeDto.gettPatientId());
				mortuaryDto.setPatientId(dischargeDto.getPatientId());
				mortuaryDto.setVisitTypeId(dischargeDto.getVisitTypeId());
				mortuaryDto.setStatus('A');
				mortuaryDto.setMortuaryStatusId(1);
				mortuaryDto.setStatus(dischargeDto.getStatus());
				mortuaryDto.setCreatedBy(dischargeDto.getCreatedBy());
				mortuaryDto.setUpdatedBy(dischargeDto.getUpdatedBy());
				mortuaryDto.setCreatedDate(dischargeDto.getCreatedDate());
				mortuaryDto.setUpdatedDate(dischargeDto.getUpdatedDate());
	
			iMortuaryDao.saveMortuaryRequest(mortuaryDto);
			}
			//end
			return new Response(SUCCESS, null, null, null, unitApplicationConfigurationDto.getSkipReadyForBilling()!=null ? unitApplicationConfigurationDto.getSkipReadyForBilling() : 'N');
		} catch (HibernateException he) {
			String correlationId = ErrorCorrelationUtil.newCorrelationId();
			logger.error(ErrorCorrelationUtil.tag(correlationId, "saveDischargeRequest failed"), he);
			return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		} catch (Exception e) {
			String correlationId = ErrorCorrelationUtil.newCorrelationId();
			logger.error(ErrorCorrelationUtil.tag(correlationId, "saveDischargeRequest failed"), e);
			return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		}
	}
	



	@Override
	public Response getDischagePatientList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_DISCHARGE").getQueryString());
			if(admissionDto.getAdmissionId()!=null)
			query.append(" AND admission.admission_id=:admissionId ");
			query.append(" ORDER BY \"pdd\" desc ");
					Query createQuery  = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("pdd", ADTCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "yyyy-MM-dd")+" 24:00:00" , "yyyy-MM-dd HH:mm:ss"))
					.setParameter("doctorId", admissionDto.getDoctorId());

			if(admissionDto.getAdmissionId()!=null)
				createQuery.setParameter("admissionId", admissionDto.getAdmissionId());

			List<AdmissionDto> admissionDtosList =	createQuery.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();


			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response canceDischarge(DischargeDto dischargeDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session
					.createQuery("update Discharge dis set dis.dischargeStatusId= "+dischargeDto.getDischargeStatusId()+",dis.status= 'I' WHERE dis.status='A' AND dis.dischargeId ="+dischargeDto.getDischargeId()
							);
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response updatePDD(DischargeDto dischargeDto) throws ApplicationException {
		try {
			int preAdmissionDetailsId=0;
			
			AdmissionDetails admissionDetails = (AdmissionDetails)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_DETAILS_BY_ADMISSION_ID")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setParameter("admissionId", dischargeDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDetails.class)).uniqueResult();
			
			preAdmissionDetailsId=admissionDetails.getAdmissionDetailsId();
			
			
			Session session = sessionFactory.getCurrentSession();
			
			if(dischargeDto.getIsCancel()!='\0')
			{
				//<-----Updating cancel details to admission details
				Query query = session.createQuery(
						"update AdmissionDetails dis set dis.remark=:remark ,"
						+ "dis.updatedBy=:updatedBy, "
						+ "dis.extendReasonId=:extendReasonId, "
						+ "dis.updatedDate=:updatedDate, "
						+ "dis.isCancel='Y', "
						+ "dis.cancelDate=:updatedDate, "
						+ "dis.cancelBy=:updatedBy "
						+ "where dis.admissionId=:admissionId");
							
				query.setParameter("updatedBy",dischargeDto.getUpdatedBy());
				query.setParameter("admissionId", dischargeDto.getAdmissionId());
				query.setParameter("extendReasonId",dischargeDto.getReasonId());
				query.setParameter("remark", dischargeDto.getRemarkDischarge());
				query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				query.executeUpdate();
				//----->
			}
			else
			{
				//<-----Updating cancel details to admission details
				Query query = session.createQuery(
						"update AdmissionDetails dis set dis.remark=:remark ,"
						+ "dis.updatedBy=:updatedBy, "
						+ "dis.extendReasonId=:extendReasonId, "
						+ "dis.updatedDate=:updatedDate, "
						+ "dis.futureDischargeDate=:pdd "
						+ "where dis.admissionId=:admissionId");
							
				query.setParameter("updatedBy",dischargeDto.getUpdatedBy());
				query.setParameter("admissionId", dischargeDto.getAdmissionId());
				query.setParameter("extendReasonId",dischargeDto.getUpdatedBy());
				query.setParameter("remark", dischargeDto.getRemarkDischarge());
				query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				query.setParameter("pdd", ADTCommonDateUtils.getDate(dischargeDto.getPdd(), "dd-MM-yyyy HH:mm:ss"));
				query.executeUpdate();
				//----->
			}
			
			
					
				admissionDetails.setAdmissionDetailsId(null);
				admissionDetails.setPdd(ADTCommonDateUtils.getDate(dischargeDto.getPdd(), "dd-MM-yyyy HH:mm:ss"));
				admissionDetails.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				admissionDetails.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				admissionDetails.setCreatedBy(dischargeDto.getCreatedBy());
				admissionDetails.setUpdatedBy(dischargeDto.getCreatedBy());
				if(dischargeDto.getDischargeStatusId()!=null && dischargeDto.getDischargeStatusId().intValue()==6)
				{
					admissionDetails.setIsDischarge('N');
				}
				admissionDetails.setStatus('A');
				admissionDetails.setNursingAcceptance('Y');
			sessionFactory.getCurrentSession().save(admissionDetails);
			
			//<-----
			Query query2=session.createQuery("update AdmissionDetails dis set dis.status='I' where dis.admissionDetailsId="+preAdmissionDetailsId);
			query2.executeUpdate();
			//----->
			  //update patient if discharge type id is 4 i.e absconding 
			//start
			if(dischargeDto.getDischargeTypeId()!=null && dischargeDto.getDischargeTypeId()==4){
				Query abscondQuery = session.createQuery(
						"update PatientRegistration  set isAbsconded='Y' "					
						+ "where patientId=:patientId");						
				abscondQuery.setParameter("patientId",dischargeDto.getPatientId());
				abscondQuery.executeUpdate();
			}
			//end
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDischargeOrNot(DischargeDto dischargeDto) throws ApplicationException {
		try {
			DischargeDto dischargeDtosList = (DischargeDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_DISCHARGE_OR_NOT")
					.setParameter("admissionId", dischargeDto.getAdmissionId())
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, dischargeDtosList.getDischCount());

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}return new Response(ERROR, null, null, null, null);
	}




	@Override
	public Response checkDischargeOrNot(DischargeDto dischargeDto) throws ApplicationException {
		try {
			DischargeDto dischargeDtosList = (DischargeDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_PATIENT_DISCHARGE_OR_NOT")
					.setParameter("admissionId", dischargeDto.getAdmissionId())
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, dischargeDtosList.getDischCount());

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}return new Response(ERROR, null, null, null, null);
	}

}
