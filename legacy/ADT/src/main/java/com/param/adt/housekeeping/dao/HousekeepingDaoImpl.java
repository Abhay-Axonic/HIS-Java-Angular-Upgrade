package com.param.adt.housekeeping.dao;

import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.housekeeping.model.Housekeeping;
import com.param.adt.housekeeping.model.HousekeepingLog;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.global.dto.UserMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class HousekeepingDaoImpl extends GenericDao<Housekeeping, Integer> implements IHousekeepingDao,ICommonConstants{

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	public HousekeepingDaoImpl() {
		super(Housekeeping.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response getBedsForHousekeepingList(BedMasterDto bedMasterDto) {
		try {
			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDS_FOR_HOUSEKEEPING_LIST")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean( BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveUserList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<UserMasterDto> userMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USERS_LIST")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
			return new Response(SUCCESS, null, null, userMasterDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response assignUserForCleaning(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			//<------ Updating housekeepingStatusId=2(Assigned) in Housekeeping	
			Query query = session.createQuery("update Housekeeping hskp set " +
					"hskp.assignedPersonId = :assignedPersonId, " +
					"hskp.housekeepingStatusId = 2, " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId");
			query.setParameter("assignedPersonId", bedMasterDto.getAssignedPersonId());
			query.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query.setParameter("unitId", bedMasterDto.getUnitId());
			query.executeUpdate();
			//----->	
			Query query2 = session.createQuery("update HousekeepingLog hskp set " +
					"hskp.status = 'I', " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId " +
					"AND hskp.status = 'A'");
			query2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query2.setParameter("unitId", bedMasterDto.getUnitId());
			query2.executeUpdate();
			
			HousekeepingLog housekeepingLog=new HousekeepingLog();
				housekeepingLog.setHousekeepingId(bedMasterDto.getHousekeepingId());
				housekeepingLog.setOrganizationId(bedMasterDto.getOrganizationId());
				housekeepingLog.setUnitId(bedMasterDto.getUnitId());
				housekeepingLog.setPriorityId(bedMasterDto.getPriorityId());
				housekeepingLog.setAssignedPersonId(bedMasterDto.getAssignedPersonId());
				housekeepingLog.setHousekeepingStatusId(2);
				housekeepingLog.setCreatedBy(bedMasterDto.getUpdatedBy());
				housekeepingLog.setUpdatedBy(bedMasterDto.getUpdatedBy());
				housekeepingLog.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setStatus('A');
			session.save(housekeepingLog);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response markAsCleaned(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			//<------ Updating housekeepingStatusId=2(Assigned) in Housekeeping	
			Query query = session.createQuery("update Housekeeping hskp set " +
					"hskp.housekeepingStatusId = 3, " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId");
			query.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query.setParameter("unitId", bedMasterDto.getUnitId());
			query.executeUpdate();
			//----->	
			
			//<------ In-activating previous active entry
			Query query2 = session.createQuery("update HousekeepingLog hskp set " +
					"hskp.status = 'I', " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId " +
					"AND hskp.status = 'A'");
			query2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query2.setParameter("unitId", bedMasterDto.getUnitId());
			query2.executeUpdate();
			//----->
			HousekeepingLog housekeepingLog=new HousekeepingLog();
				housekeepingLog.setHousekeepingId(bedMasterDto.getHousekeepingId());
				housekeepingLog.setOrganizationId(bedMasterDto.getOrganizationId());
				housekeepingLog.setUnitId(bedMasterDto.getUnitId());
				housekeepingLog.setPriorityId(bedMasterDto.getPriorityId());
				housekeepingLog.setAssignedPersonId(bedMasterDto.getAssignedPersonId());
				housekeepingLog.setHousekeepingStatusId(3);
				housekeepingLog.setCreatedBy(bedMasterDto.getUpdatedBy());
				housekeepingLog.setUpdatedBy(bedMasterDto.getUpdatedBy());
				housekeepingLog.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setStatus('A');
			session.save(housekeepingLog);
			//<-----Changing status of bed to Booked (6 is the ID of 'Vacant'status)
			Query qry2 = session.createQuery("update BedMaster bed set " +
					"bed.bedStatusId = :bedStatusId, " +
					"bed.updatedBy = :updatedBy, " +
					"bed.updatedDate = :updatedDate " +
					"WHERE bed.bedId = :bedId " +
					"AND bed.organizationId = :organizationId " +
					"AND bed.unitId = :unitId " +
					"AND bed.status = 'A'");
			qry2.setParameter("bedStatusId", 6);  // Assuming 6 is the status code you want to set
			qry2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			qry2.setParameter("bedId", bedMasterDto.getBedId());
			qry2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			qry2.setParameter("unitId", bedMasterDto.getUnitId());
			qry2.executeUpdate();
			// ----->

			// <-----Updating status of current bed to I in BedLogStatus
			Query qry3 = session.createQuery("update BedLogStatus bedStatus set " +
					"bedStatus.status = 'I', " +
					"bedStatus.updatedBy = :updatedBy, " +
					"bedStatus.updatedDate = :updatedDate " +
					"WHERE bedStatus.status = 'A' " +
					"AND bedStatus.bedId = :bedId " +
					"AND bedStatus.organizationId = :organizationId " +
					"AND bedStatus.unitId = :unitId");
			qry3.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			qry3.setParameter("bedId", bedMasterDto.getBedId());
			qry3.setParameter("organizationId", bedMasterDto.getOrganizationId());
			qry3.setParameter("unitId", bedMasterDto.getUnitId());
			qry3.executeUpdate();
			// ----->

			// <-----Adding new entry in log as the status of bed is changing
			BedLogStatus bedLogStatus =new BedLogStatus();
				bedLogStatus.setBedId(bedMasterDto.getBedId());
				bedLogStatus.setBedStatusId(6);
				bedLogStatus.setOrganizationId(bedMasterDto.getOrganizationId());
				bedLogStatus.setUnitId(bedMasterDto.getUnitId());
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(bedMasterDto.getUpdatedBy());
				bedLogStatus.setUpdatedBy(bedMasterDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			session.save(bedLogStatus);
			// ----->
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getListOfVacantBedsForNursing(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Integer count = 0;
			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("LIST_OF_VACANT_BEDS_FOR_NURSING")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset()  : 0)
					.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			
			List<BedMasterDto> bedMasterDtosList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("LIST_OF_VACANT_BEDS_FOR_NURSING")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			if(bedMasterDtosList1 != null && bedMasterDtosList1.size() > 0) {
				 count = bedMasterDtosList1.size() ;
			}
			
			return new Response(SUCCESS, null, null, bedMasterDtosList, count);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response acceptRequestForNursing(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			
			//<------ Updating housekeepingStatusId=4(Accepted By Nursing) in Housekeeping	
			Query query = session.createQuery("update Housekeeping hskp set " +
					"hskp.status = 'I', " +
					"hskp.housekeepingStatusId = :housekeepingStatusId, " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId");
			query.setParameter("housekeepingStatusId", 4);  // Assuming 4 is the status code you want to set
			query.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query.setParameter("unitId", bedMasterDto.getUnitId());
			query.executeUpdate();
			//----->	
			
			//<------ In-activating previous active entry
			Query query2=session.createQuery("update HousekeepingLog hskp set "
					+ "hskp.housekeepingStatusId=:housekeepingStatusId, "
					+ "hskp.status='I', "
					+ "hskp.note=:note, "
					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ " WHERE hskp.housekeepingId=:housekeepingId "
					+ " AND hskp.organizationId=:organizationId "
					+ " AND hskp.unitId=:unitId "
					+ " AND hskp.status='A' ");
			query2.setParameter("housekeepingStatusId", 4);
			query2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query2.setParameter("note", bedMasterDto.getNote());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query2.setParameter("unitId", bedMasterDto.getUnitId());
			query2.executeUpdate();
			//----->
			
			//<-----Changing status of bed to Booked (3 is the ID of 'Vacant and Ready to Receive Patient 'status)
			Query qry2 = session.createQuery("update BedMaster bed set"
					+"bed.bedStatusId=:bedStatusId, "
					+ "bed.updatedBy=:updatedBy, "
					+ "bed.updatedDate=:updatedDate "
					+ "WHERE bed.bedId=:bedId "
					+ "AND bed.organizationId=:organizationId "
					+ "AND bed.unitId=:unitId "
					+ "AND bed.status='A' ");
			qry2.setParameter("bedStatusId", 3);
			qry2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			qry2.setParameter("bedId", bedMasterDto.getBedId());
			qry2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			qry2.setParameter("unitId", bedMasterDto.getUnitId());

			qry2.executeUpdate();
			// ----->

			// <-----Updating status of current bed to I in BedLogStatus
			Query qry3 = session.createQuery("update BedLogStatus bedStatus set " +
					"bedStatus.status = :status, " +
					"bedStatus.updatedBy = :updatedBy, " +
					"bedStatus.updatedDate = :updatedDate " +
					"WHERE bedStatus.status = :currentStatus " +
					"AND bedStatus.bedId = :bedId " +
					"AND bedStatus.organizationId = :organizationId " +
					"AND bedStatus.unitId = :unitId");
			qry3.setParameter("status", 'I');  // Mark the status as 'I' (Inactive)
			qry3.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			qry3.setParameter("currentStatus", 'A');  // The current status is 'A' (Active)
			qry3.setParameter("bedId", bedMasterDto.getBedId());
			qry3.setParameter("organizationId", bedMasterDto.getOrganizationId());
			qry3.setParameter("unitId", bedMasterDto.getUnitId());
			qry3.executeUpdate();
			// ----->

			// <-----Adding new entry in log as the status of bed is changing
			BedLogStatus bedLogStatus =new BedLogStatus();
				bedLogStatus.setBedId(bedMasterDto.getBedId());
				bedLogStatus.setBedStatusId(3);
				bedLogStatus.setOrganizationId(bedMasterDto.getOrganizationId());
				bedLogStatus.setUnitId(bedMasterDto.getUnitId());
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(bedMasterDto.getUpdatedBy());
				bedLogStatus.setUpdatedBy(bedMasterDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			session.save(bedLogStatus);
			// ----->
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response rejectRequestForNursing(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			
			//<------ Updating housekeepingStatusId=5(Rejected By Nursing) in Housekeeping	
			Query query = session.createQuery("update Housekeeping hskp set " +
					"hskp.housekeepingStatusId = :housekeepingStatusId, " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.assignedPersonId = :assignedPersonId, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId");
			query.setParameter("housekeepingStatusId", 5);  // Status 5, as per your original query
			query.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query.setParameter("assignedPersonId", null);  // Assigning null to assignedPersonId
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query.setParameter("unitId", bedMasterDto.getUnitId());
			query.executeUpdate();
			//----->	
			
			//<------ In-activating previous active entry
			Query query2 = session.createQuery("update HousekeepingLog hskp set " +
					"hskp.status = :status, " +
					"hskp.updatedBy = :updatedBy, " +
					"hskp.updatedDate = :updatedDate " +
					"WHERE hskp.housekeepingId = :housekeepingId " +
					"AND hskp.organizationId = :organizationId " +
					"AND hskp.unitId = :unitId " +
					"AND hskp.status = :currentStatus");
			query2.setParameter("status", 'I');  // Set status to 'I'
			query2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("housekeepingId", bedMasterDto.getHousekeepingId());
			query2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			query2.setParameter("unitId", bedMasterDto.getUnitId());
			query2.setParameter("currentStatus", 'A');  // Ensures that the current status is 'A'
			query2.executeUpdate();
			//----->
			
			
			HousekeepingLog housekeepingLog=new HousekeepingLog();
				housekeepingLog.setHousekeepingId(bedMasterDto.getHousekeepingId());
				housekeepingLog.setOrganizationId(bedMasterDto.getOrganizationId());
				housekeepingLog.setUnitId(bedMasterDto.getUnitId());
				housekeepingLog.setPriorityId(bedMasterDto.getPriorityId());
				//housekeepingLog.setAssignedPersonId(bedMasterDto.getAssignedPersonId());
				housekeepingLog.setHousekeepingStatusId(5);
				housekeepingLog.setNote(bedMasterDto.getNote());
				housekeepingLog.setRejectionReasonId(bedMasterDto.getRejectionReasonId());
				housekeepingLog.setCreatedBy(bedMasterDto.getUpdatedBy());
				housekeepingLog.setUpdatedBy(bedMasterDto.getUpdatedBy());
				housekeepingLog.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ?bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setStatus('A');
			session.save(housekeepingLog);

			
			//<-----Changing status of bed to Booked (2 is the ID of 'Housekeeping'status)
			Query qry2 = session.createQuery("update BedMaster bed set " +
					"bed.bedStatusId = :bedStatusId, " +
					"bed.updatedBy = :updatedBy, " +
					"bed.updatedDate = :updatedDate " +
					"WHERE bed.bedId = :bedId " +
					"AND bed.organizationId = :organizationId " +
					"AND bed.unitId = :unitId " +
					"AND bed.status = :status");
			qry2.setParameter("bedStatusId", 2); // Set bed status to 2 (example status)
			qry2.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			qry2.setParameter("bedId", bedMasterDto.getBedId());
			qry2.setParameter("organizationId", bedMasterDto.getOrganizationId());
			qry2.setParameter("unitId", bedMasterDto.getUnitId());
			qry2.setParameter("status", 'A');  // Ensure the status is 'A' (active)


			qry2.executeUpdate();
			// ----->
			
			
			// <-----Updating status of current bed to I in BedLogStatus
			Query qry3 = session.createQuery("update BedLogStatus bedStatus set " +
					"bedStatus.status = :status, " +
					"bedStatus.updatedBy = :updatedBy, " +
					"bedStatus.updatedDate = :updatedDate " +
					"WHERE bedStatus.status = :activeStatus " +
					"AND bedStatus.bedId = :bedId " +
					"AND bedStatus.organizationId = :organizationId " +
					"AND bedStatus.unitId = :unitId");
			qry3.setParameter("status", 'I');  // Set status to 'I' (Inactive)
			qry3.setParameter("updatedBy", bedMasterDto.getUpdatedBy());
			qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			qry3.setParameter("activeStatus", 'A');  // Ensure the status is 'A' (Active)
			qry3.setParameter("bedId", bedMasterDto.getBedId());
			qry3.setParameter("organizationId", bedMasterDto.getOrganizationId());
			qry3.setParameter("unitId", bedMasterDto.getUnitId());
			qry3.executeUpdate();
			// ----->

			// <-----Adding new entry in log as the status of bed is changing
			BedLogStatus bedLogStatus =new BedLogStatus();
				bedLogStatus.setBedId(bedMasterDto.getBedId());
				bedLogStatus.setBedStatusId(2);
				bedLogStatus.setOrganizationId(bedMasterDto.getOrganizationId());
				bedLogStatus.setUnitId(bedMasterDto.getUnitId());
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(bedMasterDto.getUpdatedBy());
				bedLogStatus.setUpdatedBy(bedMasterDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			session.save(bedLogStatus);
			// ----->
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response searchBedByMultipleCriteriaForHousekeeping(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session sesison=sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(sesison.getNamedQuery("GET_BEDS_FOR_HOUSEKEEPING_LIST").getQueryString());
			
			StringBuilder finalStringQuery=generateQueryForHousekeeping(bedMasterDto,query);
			
			Query finalQuery= sesison.createQuery(finalStringQuery.toString());
			
			List<BedMasterDto> bedMasterDtosList = finalQuery
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	private StringBuilder generateQueryForHousekeeping(BedMasterDto bedMasterDto,StringBuilder query)
	{
		
		StringBuilder condition=new StringBuilder();
		
		if(bedMasterDto.getFloorId()>0)
		{
			condition.append(" AND bm.floorId="+bedMasterDto.getFloorId());
		}
		if(bedMasterDto.getWardId()>0)
		{
			condition.append(" AND bm.wardId="+bedMasterDto.getWardId());
		}
		/*
		 * if(bedMasterDto.getNursingStationId()>0) {
		 * condition.append(" AND nurward.nursingStationId="+bedMasterDto.
		 * getNursingStationId()); }
		 */
		if(bedMasterDto.getBedCategoryId()>0)
		{
			condition.append(" AND bm.bedCategoryId="+bedMasterDto.getBedCategoryId());
		}
		if(bedMasterDto.getHousekeepingStatusId()>0)
		{
			condition.append(" AND hskp.housekeepingStatusId="+bedMasterDto.getHousekeepingStatusId());
		}
		return query.append(condition);
	}


	@Override
	public Response searchBedByMultipleCriteriaForNursing(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			
			StringBuilder queryForSearch = new StringBuilder(" select "
					+" distinct (bm.bed_id) as \"bedId\", "
					+" bm.floor_id as \"floorId\", "
					+" floor.floor_name as \"floorName\", "
					+" bm.bed_category_id as \"bedCategoryId\", "
					+" bc.bed_category_desc as \"bedCategoryDesc\", "
					+" bm.bed_number as \"bedNumber\", "
					+" hskp.priority_id as \"priorityId\", "
					+" priority.priority_desc as \"priorityDesc\", "
					+" hskp.housekeeping_id as \"housekeepingId\", "
					+" hskp.housekeeping_status_id as \"housekeepingStatusId\", "
					+" hskp.assigned_person_id as \"assignedPersonId\", "
					+" (coalesce(prefix.prefix_code)|| '.' || ' ' || coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)) as \"userName\", "
					+" hsm.housekeeping_status_desc as \"housekeepingStatusDesc\" "
				+" from "
				+"	adt.m_bed_master bm "
				+" inner join adt.t_bed_log_status bedLog on "
				+"	bm.bed_id = bedLog.bed_id "
				+" left outer join adt.m_floor_master floor on "
				+"	bm.floor_id = floor.floor_id "
				+" left outer join adt.m_ward_master ward on "
				+" 	bm.ward_id = ward.ward_id "
				+" left outer join adt.m_bed_category_master bc on "
				+"	bm.bed_category_id = bc.bed_category_id "
				+" inner join adt.t_housekeeping hskp on "
				+"	bm.bed_id = hskp.bed_id "
				+" inner join adt.t_housekeeping_log hskplg on "
				+"	hskp.housekeeping_id = hskplg.housekeeping_id "
				+" inner join adt.m_housekeeping_status_master hsm on "
				+" 	hskp.housekeeping_status_id = hsm.housekeeping_status_id "
				+" inner join adt.m_priority_master priority on "
				+" 	hskp.priority_id = priority.priority_id "
				+" inner join public.m_user_master um on "
				+"	hskp.assigned_person_id = um.user_id "
				+" inner join public.m_employee_master employee on "
				+"	um.user_id = employee.user_id "
				+" inner join public.m_prefix_master prefix on "
				+"	employee.prefix_id = prefix.prefix_id "
				+" inner join adt.t_nursing_station_ward_mapper ns on "
				+"	bm.ward_id = ns.ward_id "
				+" where "
				+"	bm.bed_status_id = 6 "
				+" 	and bedLog.bed_status_id = 6 "
				+"	and bm.status = 'A' "
				+ " and bedLog.status='A' "
				+"	and bm.organization_id =:organizationId "
				+"	and bm.unit_id =:unitId "
				+"	and hskp.status = 'A' "
				+"	and hskplg.status = 'A'");
			
			
			Session sesison=sessionFactory.getCurrentSession();
//			StringBuilder query=new StringBuilder(sesison.getNamedQuery("LIST_OF_VACANT_BEDS_FOR_NURSING").getQueryString());
			
			
			if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId() > 0){
				queryForSearch.append(" AND bm.floor_id=:floorId ");
			} 
			
			if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId() > 0){
				queryForSearch.append(" AND bm.ward_id=:wardId ");
			}
			
			if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId() > 0){
				queryForSearch.append(" AND bm.bed_category_id=:bedCategoryId ");
			}
			
			if(bedMasterDto.getNursingStationId() != null && bedMasterDto.getNursingStationId() > 0){
				queryForSearch.append(" AND ns.nursing_station_id=:nursingStationId ");
			}


			List<BedMasterDto> bedMasterDtosList= null;
			Query sql = sessionFactory.getCurrentSession().createSQLQuery(queryForSearch.toString())
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset()  : 0)
					.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage()  : 10);

			if (bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId() > 0) {
				sql.setParameter("floorId", bedMasterDto.getFloorId());
			}

			if (bedMasterDto.getWardId() != null && bedMasterDto.getWardId() > 0) {
				sql.setParameter("wardId", bedMasterDto.getWardId());
			}

			if (bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId() > 0) {
				sql.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
			}

			if (bedMasterDto.getNursingStationId() != null && bedMasterDto.getNursingStationId() > 0) {
				sql.setParameter("nursingStationId", bedMasterDto.getNursingStationId());
			}
			 bedMasterDtosList=	sql.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			
			Query sql1 = sessionFactory.getCurrentSession().createSQLQuery(queryForSearch.toString())
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset()  : 0)
					.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage()  : 10);
			if (bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId() > 0) {
				sql1.setParameter("floorId", bedMasterDto.getFloorId());
			}

			if (bedMasterDto.getWardId() != null && bedMasterDto.getWardId() > 0) {
				sql1.setParameter("wardId", bedMasterDto.getWardId());
			}

			if (bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId() > 0) {
				sql1.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
			}

			if (bedMasterDto.getNursingStationId() != null && bedMasterDto.getNursingStationId() > 0) {
				sql1.setParameter("nursingStationId", bedMasterDto.getNursingStationId());
			}
			List<BedMasterDto> bedMasterDtosList1= sql1.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			Integer count=0;
			
			if(bedMasterDtosList1 != null && bedMasterDtosList1.size()>0) {
		    	  count = bedMasterDtosList1.size();
		      }
			return new Response(SUCCESS, null, null, bedMasterDtosList, count);
		} catch (HibernateException he) {
               
		} catch (Exception e) {
		
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	private StringBuilder generateQueryForNursing(BedMasterDto bedMasterDto,StringBuilder query)
	{
		
		StringBuilder condition=new StringBuilder();
		
		if(bedMasterDto.getFloorId()>0)
		{
			condition.append(" AND bm.floorId="+bedMasterDto.getFloorId());
		}
		if(bedMasterDto.getWardId()>0)
		{
			condition.append(" AND bm.wardId="+bedMasterDto.getWardId());
		}
		if(bedMasterDto.getNursingStationId()>0)
		{
			condition.append(" AND nurward.nursingStationId="+bedMasterDto.getNursingStationId());
		}
		if(bedMasterDto.getBedCategoryId()>0)
		{
			condition.append(" AND bm.bedCategoryId="+bedMasterDto.getBedCategoryId());
		}

		return query.append(condition);
	}


	@Override
	public Response getActiveUserListForHouskeeping(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<UserMasterDto> userMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USERS_LIST_HOUSEKEEPING")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
			return new Response(SUCCESS, null, null, userMasterDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
}
