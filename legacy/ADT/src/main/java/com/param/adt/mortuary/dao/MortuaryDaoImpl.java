package com.param.adt.mortuary.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.master.unit.model.MortuaryBedLogStatus;
import com.param.adt.mortuary.model.MortuaryRequest;
import com.param.global.common.Response;
import com.param.global.dto.MortuaryDto;
import com.param.global.model.DeathRegistration;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MortuaryDaoImpl extends GenericDao<DeathRegistration, Integer> implements IMortuaryDao, ICommonConstants {

	public MortuaryDaoImpl() {
		super(DeathRegistration.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveDeathRegistation(MortuaryDto mortuaryDto) {
		try {
			DeathRegistration dethRegistration = mapper.map(mortuaryDto, DeathRegistration.class,
					"MortuaryDto_to_DeathRegistration");
			dethRegistration.setCreatedDate(TimeZoneComponent.getDateByzone(mortuaryDto.getUnitId() != null ? mortuaryDto.getUnitId() :0));
			dethRegistration.setUpdatedDate(TimeZoneComponent.getDateByzone(mortuaryDto.getUnitId() != null ? mortuaryDto.getUnitId() :0));
			dethRegistration.setIsMedicoLegal(dethRegistration.getIsMedicoLegal()=='\u0000'?'N':dethRegistration.getIsMedicoLegal());
			dethRegistration = save(dethRegistration);

			mortuaryDto.setDethRegistrationId(dethRegistration.getDethRegistrationId());
			mortuaryDto.setdPatientId(dethRegistration.getDethRegistrationId());
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, mortuaryDto);
		} catch (Exception e) {
			
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	
	@Override
	public Response checkAlreadyExistReg(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			
			List<MortuaryDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_ALREADY_EXIST_REG")
					.setParameter("firstName", mortuaryDto.getFirstName())
					.setParameter("lastName", mortuaryDto.getLastName())
					.setParameter("genderId", mortuaryDto.getGenderId())
					.setParameter("age", mortuaryDto.getAge())
					.setParameter("organizationId", mortuaryDto.getOrganizationId())
					.setParameter("unitId", mortuaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MortuaryDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response saveMortuaryRequest(MortuaryDto mortuaryDto) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tr=session.beginTransaction();
		/*	String query = "select mor.patient_id as \"patientId\" from adt.t_mortuary_request mor\r\n" + 
					"where mor.patient_id = :patientId";
			
			List<Integer> ids = sessionFactory.getCurrentSession().createSQLQuery(query)
					            .setParameter("patientId", mortuaryDto.getdPatientId())
								.list();
				if(ids!=null && ids.size()>0) {
					return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, 1);
				}else {*/
				
			
				MortuaryRequest mortuaryRequest = new MortuaryRequest();
				mortuaryRequest.setOrganizationId(mortuaryDto.getOrganizationId());
				mortuaryRequest.setUnitId(mortuaryDto.getUnitId());
				mortuaryRequest.setAdmissionId(mortuaryDto.getAdmissionId());
				mortuaryRequest.settPatientId(mortuaryDto.gettPatientId());
				mortuaryRequest.setdPatientId(mortuaryDto.getdPatientId());
				mortuaryRequest.setPatientId(mortuaryDto.getPatientId());
				mortuaryRequest.setVisitTypeId(mortuaryDto.getVisitTypeId());
				mortuaryRequest.setDurationFormat(mortuaryDto.getDurationFormat());
				mortuaryRequest.setDurationValue(mortuaryDto.getDurationValue());
				mortuaryRequest.setMortuaryStatusId(mortuaryDto.getMortuaryStatusId());
				if(mortuaryDto.getExpectedArrivalDate()!=null)
					mortuaryRequest.setExpectedArrivalDate(ADTCommonDateUtils.getDate(mortuaryDto.getExpectedArrivalDate(),"dd-M-yyyy HH:mm:ss"));
				mortuaryRequest.setExpectedArrivalTime(mortuaryDto.getExpectedArrivalTime());
				mortuaryRequest.setStatus(mortuaryDto.getStatus());
				mortuaryRequest.setCreatedBy(mortuaryDto.getCreatedBy());
				mortuaryRequest.setUpdatedBy(mortuaryDto.getUpdatedBy());
				mortuaryRequest.setCreatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
				mortuaryRequest.setUpdatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			session.save(mortuaryRequest);
			tr.commit();
			session.close();
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		//}
		} catch (Exception e) {
			
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getPendingMortuaryRequestList(MortuaryDto mortuaryDto) throws ApplicationException {
		try{
			List<MortuaryDto> pendingMortuaryRequestList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_MORTUARY_REQUEST_LIST")
					.setParameter("orgId", mortuaryDto.getOrganizationId())
					.setParameter("unitId", mortuaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MortuaryDto.class)).list();
			return new Response(SUCCESS, null, null, pendingMortuaryRequestList, null);
			} catch (HibernateException he) {

			} catch (Exception e) {
				
			} 
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response acceptRejectMortuaryRequestFromPendingList(MortuaryDto mortuaryDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Query query = session.createQuery("UPDATE MortuaryRequest mor " +
					"SET mor.mortuaryStatusId = :mortuaryStatusId, " +
					"mor.updatedBy = :updatedBy, " +
					"mor.updatedDate = :updatedDate " +
					"WHERE mor.organizationId = :organizationId " +
					"AND mor.unitId = :unitId " +
					"AND mor.mortuaryRequestId = :mortuaryRequestId " +
					"AND mor.status = :status");
			query.setParameter("mortuaryStatusId", mortuaryDto.getMortuaryStatusId());
			query.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("organizationId", mortuaryDto.getOrganizationId());
			query.setParameter("unitId", mortuaryDto.getUnitId());
			query.setParameter("mortuaryRequestId", mortuaryDto.getMortuaryRequestId());
			query.setParameter("status", 'A');
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			} 
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReservedMortuaryRequestList(MortuaryDto mortuaryDto) throws ApplicationException {
		try{
			List<MortuaryDto> pendingMortuaryRequestList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESERVED_MORTUARY_REQUEST_LIST")
					.setParameter("orgId", mortuaryDto.getOrganizationId())
					.setParameter("unitId", mortuaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MortuaryDto.class)).list();
			return new Response(SUCCESS, null, null, pendingMortuaryRequestList, null);
			} catch (HibernateException he) {

			} catch (Exception e) {
				
			} 
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMorturyBedList(MortuaryDto mortuaryDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response saveBedAllocationMortuary(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			Session session=sessionFactory.openSession();
			//<-----updating mortuary bed request table
			Query query = session.createQuery("UPDATE MortuaryRequest mbm SET " +
					"mbm.updatedBy = :updatedBy, " +
					"mbm.durationFormat = :durationFormat, " +
					"mbm.durationValue = :durationValue, " +
					"mbm.mortuaryStatusId = :mortuaryStatusId, " +
					"mbm.mortuaryBedId = :mortuaryBedId, " +
					"mbm.toeBandId = :toeBandId, " +
					"mbm.admissionKinId = :admissionKinId, " +
					"mbm.updatedDate = :updatedDate " +
					"WHERE mbm.mortuaryRequestId = :mortuaryRequestId " +
					"AND mbm.organizationId = :organizationId " +
					"AND mbm.unitId = :unitId");
			query.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
			query.setParameter("durationFormat", mortuaryDto.getDurationFormat());
			query.setParameter("durationValue", mortuaryDto.getDurationValue());
			query.setParameter("mortuaryStatusId", mortuaryDto.getMortuaryStatusId());
			query.setParameter("mortuaryBedId", mortuaryDto.getMortuaryBedId());
			query.setParameter("toeBandId", mortuaryDto.getToeBandId());
			query.setParameter("admissionKinId", mortuaryDto.getAdmissionKinId());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("mortuaryRequestId", mortuaryDto.getMortuaryRequestId());
			query.setParameter("organizationId", mortuaryDto.getOrganizationId());
			query.setParameter("unitId", mortuaryDto.getUnitId());
			query.executeUpdate();
			//----->
			
			//<----- Adding new entry in MortuaryBedLogStatus be deactivating previous record
			Query query2 = session.createQuery("UPDATE MortuaryBedLogStatus mbls SET " +
					"mbls.status = :status, " +
					"mbls.updatedBy = :updatedBy, " +
					"mbls.updatedDate = :updatedDate " +
					"WHERE mbls.mortuaryBedId = :mortuaryBedId " +
					"AND mbls.organizationId = :organizationId " +
					"AND mbls.unitId = :unitId " +
					"AND mbls.status = :activeStatus");
			query2.setParameter("status", 'I');
			query2.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query2.setParameter("mortuaryBedId", mortuaryDto.getMortuaryBedId());
			query2.setParameter("organizationId", mortuaryDto.getOrganizationId());
			query2.setParameter("unitId", mortuaryDto.getUnitId());
			query2.setParameter("activeStatus", 'A');
			query2.executeUpdate();
			
			Transaction tr=session.beginTransaction();
			MortuaryBedLogStatus mortuaryBedLogStatus=new MortuaryBedLogStatus();
				mortuaryBedLogStatus.setMortuaryBedId(mortuaryDto.getMortuaryBedId());
				mortuaryBedLogStatus.setOrganizationId(mortuaryDto.getOrganizationId());
				mortuaryBedLogStatus.setUnitId(mortuaryDto.getUnitId());
				mortuaryBedLogStatus.setBedStatusId(mortuaryDto.getBedStatusId());
				mortuaryBedLogStatus.setCreatedBy(mortuaryDto.getUpdatedBy());
				mortuaryBedLogStatus.setUpdatedBy(mortuaryDto.getUpdatedBy());
				mortuaryBedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
				mortuaryBedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
				mortuaryBedLogStatus.setStatus('A');
			session.save(mortuaryBedLogStatus);
			tr.commit();
			session.close();
			//----->
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedMortuaryList(MortuaryDto mortuaryDto) throws ApplicationException {
		try{
			List<MortuaryDto> pendingMortuaryRequestList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_MORTUARY_LIST")
					.setParameter("orgId", mortuaryDto.getOrganizationId())
					.setParameter("unitId", mortuaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MortuaryDto.class)).list();
			return new Response(SUCCESS, null, null, pendingMortuaryRequestList, null);
			} catch (HibernateException he) {

			} catch (Exception e) {
				
			} 
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response releaseMorturyBed(MortuaryDto mortuaryDto) throws ApplicationException 
	{
		try{
		Session session=sessionFactory.openSession();
		//<-----updating Mortuary bed request table
			Query query = session.createQuery("UPDATE MortuaryRequest mbm SET " +
					"mbm.status = :status, " +
					"mbm.releaseNote = :releaseNote, " +
					"mbm.outTime = :outTime, " +
					"mbm.releaseKinId = :releaseKinId, " +
					"mbm.updatedBy = :updatedBy, " +
					"mbm.mortuaryStatusId = :mortuaryStatusId, " +
					"mbm.updatedDate = :updatedDate " +
					"WHERE mbm.mortuaryRequestId = :mortuaryRequestId " +
					"AND mbm.organizationId = :organizationId " +
					"AND mbm.unitId = :unitId " +
					"AND mbm.status = :activeStatus");
			query.setParameter("status", 'I');
			query.setParameter("releaseNote", mortuaryDto.getReleaseNote());
			query.setParameter("outTime", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("releaseKinId", mortuaryDto.getReleaseKinId());
			query.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
			query.setParameter("mortuaryStatusId", mortuaryDto.getMortuaryStatusId());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("mortuaryRequestId", mortuaryDto.getMortuaryRequestId());
			query.setParameter("organizationId", mortuaryDto.getOrganizationId());
			query.setParameter("unitId", mortuaryDto.getUnitId());
			query.setParameter("activeStatus", 'A');
		query.executeUpdate();
		//----->
		
		//<----- Adding new entry in MortuaryBedLogStatus by deactivating previous record
			Query query2 = session.createQuery("UPDATE MortuaryBedLogStatus mbls SET " +
					"mbls.status = :status, " +
					"mbls.updatedBy = :updatedBy, " +
					"mbls.updatedDate = :updatedDate " +
					"WHERE mbls.mortuaryBedId = :mortuaryBedId " +
					"AND mbls.organizationId = :organizationId " +
					"AND mbls.unitId = :unitId " +
					"AND mbls.status = :activeStatus");
			query2.setParameter("status", 'I');
			query2.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query2.setParameter("mortuaryBedId", mortuaryDto.getMortuaryBedId());
			query2.setParameter("organizationId", mortuaryDto.getOrganizationId());
			query2.setParameter("unitId", mortuaryDto.getUnitId());
			query2.setParameter("activeStatus", 'A');
			query2.executeUpdate();
		
		Transaction tr=session.beginTransaction();
		MortuaryBedLogStatus mortuaryBedLogStatus=new MortuaryBedLogStatus();
			mortuaryBedLogStatus.setMortuaryBedId(mortuaryDto.getMortuaryBedId());
			mortuaryBedLogStatus.setOrganizationId(mortuaryDto.getOrganizationId());
			mortuaryBedLogStatus.setUnitId(mortuaryDto.getUnitId());
			mortuaryBedLogStatus.setBedStatusId(6);
			mortuaryBedLogStatus.setCreatedBy(mortuaryDto.getUpdatedBy());
			mortuaryBedLogStatus.setUpdatedBy(mortuaryDto.getUpdatedBy());
			mortuaryBedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			mortuaryBedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			mortuaryBedLogStatus.setStatus('A');
		session.save(mortuaryBedLogStatus);
		tr.commit();
		session.close();
		//----->
		return new Response(SUCCESS, null, null, null, null);
	} catch (HibernateException he) {
	} catch (Exception e) {
		
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDataForMorturyMapOfBed(MortuaryDto mortuaryDto) throws ApplicationException {
		try{
			List<MortuaryDto> dataForMorturyMapOfBedsList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DATA_FOR_MORTURY_MAP_OF_BED")
					.setParameter("orgId", mortuaryDto.getOrganizationId())
					.setParameter("unitId", mortuaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MortuaryDto.class)).list();
			return new Response(SUCCESS, null, null, dataForMorturyMapOfBedsList, null);
			} catch (HibernateException he) {

			} catch (Exception e) {
				
			} 
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelMorgueRequest(MortuaryDto mortuaryDto) throws ApplicationException {
		try {
			Session session=sessionFactory.openSession();
			//<-----updating Mortuary bed request table
			Query query = session.createQuery("UPDATE MortuaryRequest mbm SET " +
					"mbm.status = :status, " +
					"mbm.rejectReasonId = :rejectReasonId, " +
					"mbm.rejectionNote = :rejectionNote, " +
					"mbm.updatedBy = :updatedBy, " +
					"mbm.mortuaryStatusId = :mortuaryStatusId, " +
					"mbm.updatedDate = :updatedDate " +
					"WHERE mbm.mortuaryRequestId = :mortuaryRequestId " +
					"AND mbm.organizationId = :organizationId " +
					"AND mbm.unitId = :unitId");
			query.setParameter("status", 'I');
			query.setParameter("rejectReasonId", mortuaryDto.getRejectReasonId());
			query.setParameter("rejectionNote", mortuaryDto.getRejectionNote());
			query.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
			query.setParameter("mortuaryStatusId", 5);  // As hardcoded value '5'
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("mortuaryRequestId", mortuaryDto.getMortuaryRequestId());
			query.setParameter("organizationId", mortuaryDto.getOrganizationId());
			query.setParameter("unitId", mortuaryDto.getUnitId());
			query.executeUpdate();
			//----->
			if(mortuaryDto.getMortuaryBedId()!=null)
			{
			//<----- Adding new entry in MortuaryBedLogStatus be deactivating previous record
			Query query2=session.createQuery("update MortuaryBedLogStatus mbls set "
					+ "mbls.status=:status, "
					+ "mbls.updatedBy=:updatedBy, "
					+ "mbls.updatedDate=:updatedDate "
					+ " WHERE mbls.mortuaryBedId=:mortuaryBedId "
					+ " AND mbls.organizationId=:organizationId "
					+ " AND mbls.unitId=:unitId "
					+ " AND mbls.status=:activeStatus ");
				query2.setParameter("status", 'I');
				query2.setParameter("updatedBy", mortuaryDto.getUpdatedBy());
				query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				query2.setParameter("mortuaryBedId", mortuaryDto.getMortuaryBedId());
				query2.setParameter("organizationId", mortuaryDto.getOrganizationId());
				query2.setParameter("unitId", mortuaryDto.getUnitId());
				query2.setParameter("activeStatus", 'A');
			query2.executeUpdate();
			
			Transaction tr=session.beginTransaction();
			MortuaryBedLogStatus mortuaryBedLogStatus=new MortuaryBedLogStatus();
				mortuaryBedLogStatus.setMortuaryBedId(mortuaryDto.getMortuaryBedId());
				mortuaryBedLogStatus.setOrganizationId(mortuaryDto.getOrganizationId());
				mortuaryBedLogStatus.setUnitId(mortuaryDto.getUnitId());
				mortuaryBedLogStatus.setBedStatusId(6);
				mortuaryBedLogStatus.setCreatedBy(mortuaryDto.getUpdatedBy());
				mortuaryBedLogStatus.setUpdatedBy(mortuaryDto.getUpdatedBy());
				mortuaryBedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
				mortuaryBedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(mortuaryDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
				mortuaryBedLogStatus.setStatus('A');
			session.save(mortuaryBedLogStatus);
			tr.commit();
			session.close();
			//----->
			}
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	

}