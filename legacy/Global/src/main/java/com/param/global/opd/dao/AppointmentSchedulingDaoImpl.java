package com.param.global.opd.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.adt.master.global.model.NationalityMaster;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import com.param.billing.global.smartcard.dto.ItemDetailsResponseDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.PatientSearchBillRes;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IMPackageMasterService;
import com.param.opd.appointment.dto.CountDto;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.appointment.model.CyclicAppointmentMapper;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentSchedulingDaoImpl implements IAppointmentSchedulingDao,ICommonConstants,IAppointmentAndSlotStatusMasterConstants {

	private static final Logger logger = Logger.getLogger(AppointmentSchedulingDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	ISlotMasterGlobalDao iSlotMasterDao;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;

	
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	

	@Override
	@Transactional
	public Response scheduleAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			Random rnd = new Random();
			Integer appointmentUniqueId = rnd.nextInt(99999999);
			
			String appointmentDate  = (slotMasterDto.getAppointmentDate()!=null ? slotMasterDto.getAppointmentDate().substring(0, 10).concat(" 00:00:00") : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "yyyy-MM-dd").concat(" 00:00:00"));
			slotMasterDto.setAppointmentDate(appointmentDate);
			AppointmentMaster appointmentMaster = mapper.map(slotMasterDto, AppointmentMaster.class,"SlotMasterDto_to_AppointmentMaster");
				
				appointmentMaster.setAppointmentDate(GlobalCommonDateUtils.getDate(appointmentDate,"yyyy-MM-dd HH:mm:ss"));
				if(slotMasterDto.getAppointmentDate2()!=null && slotMasterDto.getAppointmentDate2()!= "")
				{
					slotMasterDto.setAppointmentDate2(slotMasterDto.getAppointmentDate2().substring(0, 10).concat(" 00:00:00"));
					appointmentMaster.setAppointmentDate(GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate2().substring(0,10), "dd/M/yyyy"));
				}
				appointmentMaster.setIsFromDischargeSummary(slotMasterDto.getIsFromDischargeSummary()!=null ? slotMasterDto.getIsFromDischargeSummary() : 'N');
				appointmentMaster.setIsDirectEncounter(slotMasterDto.getIsDirectEncounter()!=null ? slotMasterDto.getIsDirectEncounter() : 'N');
				appointmentMaster.setDummyTokenNumber(slotMasterDto.getTokenNumber()!=null ? slotMasterDto.getTokenNumber() : null);
				appointmentMaster.setOrderDetailsId(slotMasterDto.getOrderDetailsId()!=null ? slotMasterDto.getOrderDetailsId() : null);
				appointmentMaster.setCreatedDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
				appointmentMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
				appointmentMaster.setIsAppointmentFromMyLife(slotMasterDto.getIsAppointmentFromMyLife()!=null ? slotMasterDto.getIsAppointmentFromMyLife() : 'N');
				appointmentMaster.setIsTeleconsultationAppointment(slotMasterDto.getIsTeleconsultationAppointment()!=null ? slotMasterDto.getIsTeleconsultationAppointment():0);
				if(appointmentMaster.getSlotId()==null || appointmentMaster.getAppointmentTypeId()==APPOINTMENTTYPEID3)
					appointmentMaster.setAppDateTime(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
				else if(appointmentMaster.getSlotId()!=null && appointmentMaster.getAppointmentTypeId()!=APPOINTMENTTYPEID3)
					appointmentMaster.setAppDateTime(GlobalCommonDateUtils.getDate(iSlotMasterDao.getAppDateTimeBySlotId(appointmentMaster.getSlotId()),"yyyy-MM-dd HH:mm:ss"));
				
				appointmentMaster.setAppointmentUniqueId(appointmentUniqueId);
				
			sessionFactory.getCurrentSession().save(appointmentMaster);
			Integer count=0;
			if(slotMasterDto.getSlotStatusId()==null) {
				slotMasterDto.setSlotStatusId(4);
			}
			if(slotMasterDto.getSlotId()!=null){
				count=	sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
					+ "sm.slotStatusId=:slotStatusId, "
					+ "sm.updatedBy=:updatedBy , "
					+ "sm.updatedDate=:updatedDate "
					+ "where sm.slotId=:slotId ")
				.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
				.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
				.setParameter("slotId",slotMasterDto.getSlotId())
				.setParameter("slotStatusId",slotMasterDto.getSlotStatusId())
				
			.executeUpdate();
			
				if(count>0){
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, appointmentMaster);
				}else{
					return new Response(ERROR, ERROR_CODE, null, null, appointmentMaster);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, appointmentMaster);
		}catch(ConstraintViolationException e){
		throw new ApplicationException(e.getMessage(), e);
	}
		catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}



	@Override
	public Response cancelScheduledAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			//updating slotStatusId to 2(Cancelled) after canceling appointment. 
			sessionFactory.getCurrentSession().createQuery("update AppointmentMaster am set "
						+ "am.appointmentStatusId="+Cancelled+", "
						+ "am.cancelReasonId=:cancelReasonId, "
						+ "am.cancelledBy=:cancelledBy, "
						+ "am.updatedBy=:updatedBy, "
						+ "am.updatedDate=:updatedDate, "
						+ "am.status='I' "
						+ "WHERE am.appointmentId=:appointmentId")
					.setParameter("cancelReasonId", slotMasterDto.getCancelReasonId())
					.setParameter("cancelledBy", slotMasterDto.getCancelledBy())
					.setParameter("updatedBy",slotMasterDto.getUpdatedBy())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("appointmentId", slotMasterDto.getAppointmentId())
			.executeUpdate();
			
			if(slotMasterDto.getSlotId() != null && slotMasterDto.getSlotId() > 0){
			//updating slotStatusId to 1(vacant) after canceling appointment. 
			sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
						+ "sm.slotStatusId="+Vaccant+", " //slot vacant
						+ "sm.updatedBy=:updatedBy, "
						+ "sm.updatedDate=:updatedDate "
						+ "where sm.slotId=:slotId ")
					.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("slotId",slotMasterDto.getSlotId())
			.executeUpdate();
			}
			
			if(slotMasterDto.getEncounterId()!=null) {
				sessionFactory.getCurrentSession().createQuery("update EncounterMaster encounterObj set "
						+ "encounterObj.statusId="+Cancelled+", "
						+ "encounterObj.updatedBy=:updatedBy, "
						+ "encounterObj.updatedDate=:updatedDate, "
						+ "encounterObj.status='I' "
						+ "WHERE encounterObj.encounterId=:encounterId")
					.setParameter("updatedBy",slotMasterDto.getUpdatedBy())
				//	.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
						.setParameter("updatedDate",
								GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss").getTime())

						.setParameter("encounterId", slotMasterDto.getEncounterId())
			.executeUpdate();
			}
			return new Response(SUCCESS, null, "Appointment Cancelled Successfully", null, null);
			
		} catch (HibernateException he) {
			logger.error("cancelScheduledAppointment failed", he);
		} catch (Exception e) {
			logger.error("cancelScheduledAppointment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rescheduleAppointmentForPaidAppointment(EncounterMasterDto encounterMasterDto){
		try{
			// Rescheduling the appointment by deactivating previous appointment by changing the status to I and appointmentStatusId to 3 
				sessionFactory.getCurrentSession().createQuery("update AppointmentMaster am set "
							+ "am.appointmentStatusId="+Rescheduled+", "	//appointmentStatusId=3 ==> For rescheduling 
							+ "am.rescheduleReasonId=:rescheduleReasonId, "
							+ "am.rescheduledBy=:rescheduledBy, "
							+ "am.updatedBy=:updatedBy, "
							+ "am.updatedDate=:updatedDate, "
							+ "am.status='I' "
							+ "WHERE am.appointmentId=:appointmentId")
						.setParameter("rescheduleReasonId", encounterMasterDto.getRescheduleReasonId())
						.setParameter("rescheduledBy", encounterMasterDto.getReschedulledBy())
						.setParameter("updatedBy",encounterMasterDto.getUpdatedBy())
						.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
						.setParameter("appointmentId", encounterMasterDto.getRescheduleAppointmentId())
				.executeUpdate();
						
						
						//updating slotStatusId to 1(vacant) for previous slot after rescheduling appointment. 
				sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
							+ "sm.slotStatusId="+Vaccant+", " //slot vacant
							+ "sm.updatedBy=:updatedBy, "
							+ "sm.updatedDate=:updatedDate "
							+ "where sm.slotId=:slotId")
						.setParameter("updatedBy", encounterMasterDto.getUpdatedBy())
						.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
						.setParameter("slotId",encounterMasterDto.getPreviousSlotId())
				.executeUpdate();
				
				if(encounterMasterDto.getEncounterId()>0)
				{
					sessionFactory.getCurrentSession()
						.createQuery("update EncounterMaster am set " 
								+ " am.statusId = 3, " //Rescheduled 
								+ " am.updatedDate=:updatedDate "
								+ " where am.appointmentId="+encounterMasterDto.getRescheduleAppointmentId())
						.setParameter("updatedDate",TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()))
					.executeUpdate();
				}
			
			
			return new Response(ERROR, null, null, null, null);
		}catch(Exception e){
			logger.error("rescheduleAppointmentForPaidAppointment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	@Override
	public Response rescheduleAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			Session session= sessionFactory.getCurrentSession();
			
			Integer billId = null;
			Integer appointmentId = null;
			
			
			// Rescheduling the appointment by deactivating previous appointment by changing the status to I and appointmentStatusId to 3 
			session.createQuery("update AppointmentMaster am set "
						+ "am.appointmentStatusId="+Rescheduled+", "	//appointmentStatusId=3 ==> For rescheduling 
						+ "am.rescheduleReasonId=:rescheduleReasonId, "
						+ "am.orderDetailsId=NULL, "
						+ "am.rescheduledBy=:rescheduledBy, "
						+ "am.updatedBy=:updatedBy, "
						+ "am.updatedDate=:updatedDate, "
						+ "am.status='I' "
						+ "WHERE am.appointmentId=:appointmentId")
					.setParameter("rescheduleReasonId", slotMasterDto.getRescheduleReasonId())
					.setParameter("rescheduledBy", slotMasterDto.getRescheduledBy())
					.setParameter("updatedBy",slotMasterDto.getUpdatedBy())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("appointmentId", slotMasterDto.getAppointmentId())
			.executeUpdate();
			
			
			//updating slotStatusId to 1(vacant) for previous slot after rescheduling appointment.
			if(slotMasterDto.getPreviousSlotId() != null ) {
				session.createQuery("update SlotMaster sm set "
						+ "sm.slotStatusId="+Vaccant+", " //slot vacant
						+ "sm.updatedBy=:updatedBy, "
						+ "sm.updatedDate=:updatedDate "
						+ "where sm.slotId=:slotId")
					.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("slotId",slotMasterDto.getPreviousSlotId())
			.executeUpdate();	
			}
		
			
			
			//Adding new entry in appointmentMaster with new slot id 
			AppointmentMaster appointmentMaster = mapper.map(slotMasterDto, AppointmentMaster.class,
					"SlotMasterDto_to_AppointmentMaster");
			
				String query = " select " 
						 + " 	billing_master_id as \"billingMasterId\" " 
						 + " from " 
						 + " 	billing.m_billing_master " 
						 + " where " 
						 + " 	encounter_id =( " 
						 + " 	select " 
						 + " 		encounter_id " 
						 + " 	from " 
						 + " 		opd.t_encounter_master " 
						 + " 	where " 
						 + " 		appointment_id =:appointmentId ) "
						 + " 	and bill_status_id !=1 "
				         + " 	and status ='A' ";
				
				BillingMasterDto dto = (BillingMasterDto) sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("appointmentId",slotMasterDto.getAppointmentId())
										.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
				
				
				if(dto!=null && dto.getBillingMasterId()!=null && dto.getBillingMasterId()>0)
				{
					appointmentMaster.setIsPaidAppointment('Y');
					billId = dto.getBillingMasterId();
				}
				
			if(slotMasterDto.getAppointmentDate2()!=null)
			{
				slotMasterDto.setAppointmentDate2(slotMasterDto.getAppointmentDate2().substring(0, 10).concat(" 00:00:00"));
				appointmentMaster.setAppointmentDate(GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate2().substring(0,10), "dd/M/yyyy"));
			}
			Date appDateTime = GlobalCommonDateUtils.getDate(iSlotMasterDao.getAppDateTimeBySlotId(appointmentMaster.getSlotId()),"yyyy-MM-dd HH:mm:ss");
			
			if(appointmentMaster.getSlotId()==null || appointmentMaster.getAppointmentTypeId()==APPOINTMENTTYPEID3)
				appointmentMaster.setAppDateTime(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
			else if(appointmentMaster.getSlotId()!=null && appointmentMaster.getAppointmentTypeId()!=APPOINTMENTTYPEID3)
				appointmentMaster.setAppDateTime(appDateTime);
			
				appointmentMaster.setIsAppointmentFromMyLife('N');
				appointmentMaster.setAppointmentStatusId(Vaccant);
				appointmentMaster.setCreatedDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
				appointmentMaster.setRescheduleReasonId(null);
				appointmentMaster.setCancelReasonId(null);
			appointmentId = (Integer) session.save(appointmentMaster);
			
			//updating slotStatusId to 4(appointment booked) after saving appointment. 
			session.createQuery("update SlotMaster sm set "
						+ "sm.slotStatusId="+Booked+", " //slot booked
						+ "sm.updatedBy=:updatedBy, "
						+ "sm.updatedDate=:updatedDate "
						+ "where sm.slotId=:slotId ")
					.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("slotId",slotMasterDto.getSlotId())
			.executeUpdate();
			
			if(billId != null)
			{
				if (slotMasterDto.getAppointmentId() != null) {
					 sessionFactory.getCurrentSession().createQuery(
									"UPDATE EncounterMaster am " +
											"SET am.statusId = :statusId, " +
											"am.unitTokenNumber = null, " +
											"am.dummyTokenNumber = null, " +
											"am.docQueueNumber = null, " +
											"am.encounterDate = :encounterDate, " +
											"am.encounterTime = :encounterTime, " +
											"am.updatedDate = :updatedDate, " +
											"am.appointmentId = :newAppointmentId " +
											"WHERE am.appointmentId = :existingAppointmentId")
							.setParameter("statusId", 1)
							.setParameter("encounterDate", GlobalCommonDateUtils.getDate(appDateTime, "dd-MM-yyyy HH:mm:ss").getTime())
							.setParameter("encounterTime", GlobalCommonDateUtils.getDate(appDateTime, "HH:mm:ss").toInstant())
							.setParameter("updatedDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss").getTime())
							.setParameter("newAppointmentId", appointmentId)
							.setParameter("existingAppointmentId", slotMasterDto.getAppointmentId())
							.executeUpdate();
				}

			}
			
			return new Response(SUCCESS, null, "Appointment Rescheduled Successfully...!!", null, appointmentMaster);
			
		} catch (HibernateException he) {
		logger.error("rescheduleAppointment failed", he);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response updateAppointmentStatus(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			Date currentDateObj = TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId());
			sessionFactory.getCurrentSession().createQuery("update AppointmentMaster set "
					+ "appointmentStatusId=:appointmentStatusId, "
					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ "WHERE appointmentId=:appointmentId")
				.setParameter("appointmentStatusId", slotMasterDto.getAppointmentStatusId())
				.setParameter("updatedBy",slotMasterDto.getUpdatedBy())
				.setParameter("updatedDate",currentDateObj)
				.setParameter("appointmentId", slotMasterDto.getAppointmentId())
			.executeUpdate();	
			
			if(slotMasterDto.getEncounterId()!=null && slotMasterDto.getEncounterId()>0 && slotMasterDto.getAppointmentStatusId()==APPOINTMENTSTATUSID5)
			{
				sessionFactory.getCurrentSession()
					.createQuery("update EncounterMaster am set " 
							+ "am.statusId = 5, "
							+ "am.updatedDate=:updatedDate "
							+ "where am.encounterId=:encounterId ")
					.setParameter("updatedDate",currentDateObj.getTime())
					.setParameter("encounterId", slotMasterDto.getEncounterId())
				.executeUpdate();
			}
			
			return new Response(SUCCESS, null, null, null, null);
			
		} catch (HibernateException he) {
		logger.error("updateAppointmentStatus failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchAppointmentsByCriteria(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			StringBuilder stringQuery = null;
			if(slotMasterDto.getIsAppointmentRequired() != null && slotMasterDto.getIsAppointmentRequired() == 'N') {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST_FOR_IS_APPOINTMENT_FALSE").getQueryString().toString());
			}else {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString().toString());
			}
			

			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)	
			{
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
					// "AND sm.unit_id=:unitId "
						//+ "AND am.appointment_type_id=:appointmentTypeId "
						+ "AND am.appointment_type_id IN (1,6,9) "
						);
				
				SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());

				if (slotMasterDto.getAppointmentDate() != null) {
					query.setParameter("appointmentDate", slotMasterDto.getAppointmentDate());
				}

				if (slotMasterDto.getPatientId() > 0) {
					query.setParameter("patientId", slotMasterDto.getPatientId());
				}

				if (slotMasterDto.getFromDate() != null) {
					query.setParameter("fromDate", slotMasterDto.getFromDate());
				}

				if (slotMasterDto.getToDate() != null) {
					query.setParameter("toDate", slotMasterDto.getToDate());
				}

				if (slotMasterDto.getParentPackageId() != null && slotMasterDto.getParentPackageId() > 0) {
					query.setParameter("parentPackageId", slotMasterDto.getParentPackageId());
				}

				if (slotMasterDto.getSpecialityId() > 0) {
					query.setParameter("specialityId", slotMasterDto.getSpecialityId());
				}

				if (slotMasterDto.getDoctorId() > 0) {
					query.setParameter("doctorId", slotMasterDto.getDoctorId());
				}

				if (slotMasterDto.getSubSpecialityId() != null && slotMasterDto.getSubSpecialityId() > 0) {
					query.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId());
				}

				if (slotMasterDto.getModalityId() > 0) {
					query.setParameter("modalityId", slotMasterDto.getModalityId());
				}

				if (slotMasterDto.getVisitTypeId() > 0) {
					query.setParameter("visitTypeId", slotMasterDto.getVisitTypeId());
				}

	            if(slotMasterDto.getOffset()!=null && slotMasterDto.getNoOfRecordsPerPage()!=null)
	            {
	            	query.setFirstResult(slotMasterDto.getOffset());
	            	query.setMaxResults(slotMasterDto.getNoOfRecordsPerPage());
	            }

				List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				Integer count=slotMasterDtosList.size();
				List<SlotMasterDto> slotMasterDtosListfinal=  new ArrayList<>();

				List<Integer> encounterIds = slotMasterDtosList.stream()
						.map(SlotMasterDto::getEncounterId)
						.filter(Objects::nonNull) // Removes null entries
						.collect(Collectors.toList());
                List<SlotMasterDto> dtoBillStatusList = null;
                List<BillingMasterDto> billIdList = null;
				if(encounterIds.size()>0) {
						String qryBillStatus =
								"SELECT " +
										"    bill.bill_cancelled AS \"billCancelled\", " +
										"    bill.bill_status_id AS \"billStatusId\", " +
										"    bill.encounter_id AS \"encounterId\" " +
										"FROM " +
										"    billing.m_billing_master bill " +
										"WHERE " +
										"    bill.bill_cancelled = :billCancelled " +
										"    AND bill.is_dr_note = :isDrNote " +
										"    AND bill.encounter_id IN (:encounterIds) " +
										"ORDER BY " +
										"    bill.billing_master_id DESC";

						 dtoBillStatusList = sessionFactory.getCurrentSession()
								.createSQLQuery(qryBillStatus)
								.setParameter("billCancelled", "N")
								.setParameter("isDrNote", "N")
								.setParameterList("encounterIds", encounterIds)
								.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
								.list();



					String qryBillId = " select         " +
						"		bill.billing_master_id  as \"billingMasterId\" ,"
						+ "     bill.encounter_id as \"encounterId\" " + 
						" from         " + 
						"		billing.m_billing_master bill   " + 
						" inner join billing.t_billing_details bhh on " + 
						"		bhh.billing_master_id = bill.billing_master_id         " + 
						" left join billing.t_bill_payee_details billPayee on         " + 
						"		bill.billing_master_id = billPayee.bill_id         " + 
						"	and billPayee.payee_id =(         " + 
						"	select         " + 
						"			uabc.self_payee_id         " + 
						"	from         " + 
						"			public.t_unit_application_billing_configuration uabc         " + 
						"	where         " + 
						"			uabc.status = 'A'         " + 
						"		and uabc.unit_id = bill.unit_id)         " + 
						" inner join public.t_order_details ordDet on         " + 
						"		ordDet.order_details_id = bhh.order_details_id         " + 
						" where         " + 
						"		bill.bill_cancelled = 'N'         " + 
						"	and bill.is_dr_note = 'N'         " +
							"    AND bill.encounter_id IN (:encounterIds) " +
						"	and bill.bill_status_id in (1, 2)         " +
						"	and bill.total_self_payable > 0         " + 
						"	and billPayee.due_amount > 0         " + 
						"	and bill.outstanding > 0         " + 
						"	and coalesce(ordDet.is_consultation_service, 'N' ) = 'Y'         " + 
						" order by         " + 
						"    		bill.billing_master_id desc ";
				StringBuffer sbBillId = new StringBuffer(qryBillId);
				billIdList = sessionFactory.getCurrentSession()
						.createSQLQuery(sbBillId.toString()).setParameterList("encounterIds", encounterIds)
						.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).list();
				
				}
				
                
				for(SlotMasterDto dto : slotMasterDtosList){
					
					if(dto.getEncounterId() != null) {
					
                        
						SlotMasterDto slotMDto = dtoBillStatusList.stream()
                                  .filter(order -> dto.getEncounterId().equals(order.getEncounterId()))
                                  .findAny()
                                  .orElse(null);
						
						BillingMasterDto billDto = billIdList.stream()
                                .filter(order -> dto.getEncounterId().equals(order.getEncounterId()))
                                .findAny()
                                .orElse(null);
						
					
					if( slotMDto != null && slotMDto.getBillStatusId() != null) {
						dto.setBillStatusId(slotMDto.getBillStatusId());
					}
					if(slotMDto != null && slotMDto.getBillCancelled() != null) {
						dto.setBillCancelled(slotMDto.getBillCancelled());
					}
					
					if(billDto != null && billDto.getBillingMasterId() != null) {
						dto.setBillId(billDto.getBillingMasterId());
						}
					
						slotMasterDtosListfinal.add(dto);
					}else {
						slotMasterDtosListfinal.add(dto);
					}
					
				   }
				
				return new Response(SUCCESS, null, null, slotMasterDtosListfinal, count);
				
			}else {
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						+ "AND am.appointment_type_id=:appointmentTypeId ");
			}
		
			SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			if (slotMasterDto.getAppointmentDate() != null) {
				query.setParameter("appointmentDate", slotMasterDto.getAppointmentDate());
			}

			if (slotMasterDto.getPatientId() > 0) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate());
			}

			if (slotMasterDto.getToDate() != null) {
				query.setParameter("toDate", slotMasterDto.getToDate());
			}

			if (slotMasterDto.getParentPackageId() != null && slotMasterDto.getParentPackageId() > 0) {
				query.setParameter("parentPackageId", slotMasterDto.getParentPackageId());
			}

			if (slotMasterDto.getSpecialityId() > 0) {
				query.setParameter("specialityId", slotMasterDto.getSpecialityId());
			}

			if (slotMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", slotMasterDto.getDoctorId());
			}

			if (slotMasterDto.getSubSpecialityId() != null && slotMasterDto.getSubSpecialityId() > 0) {
				query.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId());
			}

			if (slotMasterDto.getModalityId() > 0) {
				query.setParameter("modalityId", slotMasterDto.getModalityId());
			}

			if (slotMasterDto.getVisitTypeId() > 0) {
				query.setParameter("visitTypeId", slotMasterDto.getVisitTypeId());
			}
			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)
			{
			}else{
				query.setParameter("appointmentTypeId",slotMasterDto.getAppointmentTypeId());
			}
			List<SlotMasterDto> slotMasterDtosList = query
				.setParameter("orgId", slotMasterDto.getOrganizationId())
				.setParameter("unitId", slotMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			Integer count=slotMasterDtosList.size();
			
			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
			
		} catch (HibernateException he) {
		logger.error("searchAppointmentsByCriteria failed", he);
		} catch (Exception e) {
        logger.error("searchAppointmentsByCriteria failed", e);
        }
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response scheduledAppointmentList(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<SlotMasterDto> slotMasterDtosList=null;
			List<CountDto> countDtoList=null;
			StringBuilder stringQuery=null;
			SQLQuery query=null;
			
			if(slotMasterDto.getIsAppointmentRequired() != null && slotMasterDto.getIsAppointmentRequired() == 'N') {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST_FOR_IS_APPOINTMENT_FALSE").getQueryString().toString());
			}else {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString().toString());
			}
			
			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)	
			{
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						+ "AND am.appointment_type_id IN (1,6,9) "
						);
			}else if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID2){
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						+ "AND am.admission_id is null "
						+ "AND am.appointment_type_id= "+slotMasterDto.getAppointmentTypeId()
						);
			}else {
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						+ "AND am.appointment_type_id= "+slotMasterDto.getAppointmentTypeId()
						);
			}
			if(slotMasterDto.getParentPackageId()!=null && slotMasterDto.getParentPackageId()>0)
			{
				stringQuery.append(" AND pack.parent_package_id ="+slotMasterDto.getParentPackageId());
			}
			
			//For All Appointments
			if(slotMasterDto.getAppointmentStatusId()==All){
				query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			}
			//For pending appointments
			if(slotMasterDto.getAppointmentStatusId()==Pending){
				stringQuery.append(" AND am.status='A' AND am.appointment_status_id="+Pending+" ");
				query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			}
			//For rescheduled and cancelled appointments
			if(slotMasterDto.getAppointmentStatusId()==Rescheduled){
				stringQuery.append(" AND am.status='I' AND (am.appointment_status_id="+Cancelled+" OR am.appointment_status_id="+Rescheduled+") ");
				query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			}
			//For CheckIn
			if(slotMasterDto.getAppointmentStatusId()==CheckedIn){
				stringQuery.append(" AND am.status='A' AND am.appointment_status_id="+CheckedIn+" ");
				query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			}
			
			//For Admitted Patient List who are having appointments
			if(slotMasterDto.getAppointmentStatusId()==Admitted){
				stringQuery.append(" AND am.status='A' AND am.appointment_status_id="+Admitted+" ");
				query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			}
			
			if(slotMasterDto.getAppointmentStatusId()==Completed){
				stringQuery.append(" AND am.status='A'  AND am.appointment_status_id="+Completed+" ");
				query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			}

			if (slotMasterDto.getAppointmentDate() != null) {
				query.setParameter("appointmentDate", slotMasterDto.getAppointmentDate());
			}

			if (slotMasterDto.getPatientId() > 0) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate());
			}

			if (slotMasterDto.getToDate() != null) {
				query.setParameter("toDate", slotMasterDto.getToDate());
			}

			if (slotMasterDto.getParentPackageId() != null && slotMasterDto.getParentPackageId() > 0) {
				query.setParameter("parentPackageId", slotMasterDto.getParentPackageId());
			}

			if (slotMasterDto.getSpecialityId() > 0) {
				query.setParameter("specialityId", slotMasterDto.getSpecialityId());
			}

			if (slotMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", slotMasterDto.getDoctorId());
			}

			if (slotMasterDto.getSubSpecialityId() != null && slotMasterDto.getSubSpecialityId() > 0) {
				query.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId());
			}

			if (slotMasterDto.getModalityId() > 0) {
				query.setParameter("modalityId", slotMasterDto.getModalityId());
			}

			if (slotMasterDto.getVisitTypeId() > 0) {
				query.setParameter("visitTypeId", slotMasterDto.getVisitTypeId());
			}
			
			slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			List<SlotMasterDto> slotMasterDtosListfinal=  new ArrayList<>();
			for(SlotMasterDto dto : slotMasterDtosList){
				if(dto.getEncounterId() != null) {
				String qry = "select " + 
						"		bill.bill_cancelled as \"billCancelled\", " + 
						"		bill.bill_status_id as \"billStatusId\" " + 
						"	from " + 
						"		billing.m_billing_master bill " + 
						"	where " + 
						"		bill.bill_cancelled = 'N' " + 
						"		and bill.is_dr_note = 'N' " + 
						"		and bill.encounter_id =:encounterId" +
						" order by bill.billing_master_id desc " + 
						"		limit 1";
				StringBuffer sb = new StringBuffer(qry);
				SlotMasterDto dtoss = (SlotMasterDto)sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
			
				
				String qry2 = " select         " + 
						"		bill.billing_master_id  as \"billingMasterId\"  " + 
						" from         " + 
						"		billing.m_billing_master bill   " + 
						" inner join billing.t_billing_details bhh on " + 
						"		bhh.billing_master_id = bill.billing_master_id         " + 
						" left join billing.t_bill_payee_details billPayee on         " + 
						"		bill.billing_master_id = billPayee.bill_id         " + 
						"	and billPayee.payee_id =(         " + 
						"	select         " + 
						"			uabc.self_payee_id         " + 
						"	from         " + 
						"			public.t_unit_application_billing_configuration uabc         " + 
						"	where         " + 
						"			uabc.status = 'A'         " + 
						"		and uabc.unit_id = bill.unit_id)         " + 
						" inner join public.t_order_details ordDet on         " + 
						"		ordDet.order_details_id = bhh.order_details_id         " + 
						" where         " + 
						"		bill.bill_cancelled = 'N'         " + 
						"	and bill.is_dr_note = 'N'         " + 
						"	and bill.encounter_id =:encounterId        " + 
						"	and bill.bill_status_id in (1, 2)         " + 
						"	and bill.total_self_payable > 0         " + 
						"	and billPayee.due_amount > 0         " + 
						"	and bill.outstanding > 0         " + 
						"	and coalesce(ordDet.is_consultation_service, 'N' ) = 'Y'         " + 
						" order by         " + 
						"    		bill.billing_master_id desc ";
				StringBuffer sb2 = new StringBuffer(qry2);
				BillingMasterDto bill = (BillingMasterDto)sessionFactory.getCurrentSession()
						.createSQLQuery(sb2.toString())
						.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(BillingMasterDto.class)).uniqueResult();
				
				if( dtoss != null && dtoss.getBillStatusId() != null) {
					dto.setBillStatusId(dtoss.getBillStatusId());
				}
				if(dtoss != null && dtoss.getBillCancelled() != null) {
				dto.setBillCancelled(dtoss.getBillCancelled());
				}
				
				if(bill != null && bill.getBillingMasterId() != null) {
					dto.setBillId(bill.getBillingMasterId());
					}
				
				
				slotMasterDtosListfinal.add(dto);
				}else {
					slotMasterDtosListfinal.add(dto);
				}
			   }
			
			return new Response(SUCCESS, null, null, slotMasterDtosListfinal, countDtoList);
			
		} catch (HibernateException he) {
		logger.error("scheduledAppointmentList failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@SuppressWarnings("unused")
	private StringBuilder getFinalQueryAfterAppendConditions(StringBuilder namedQuery, SlotMasterDto slotMasterDto, int i) {
		try {


			if (slotMasterDto.getAppointmentDate() != null) {
				namedQuery.append(" AND to_char(am.appointment_date, 'YYYY-MM-DD') = :appointmentDate ");
			}

			if (slotMasterDto.getPatientId() > 0) {
				namedQuery.append(" AND am.patient_id = :patientId ");
			}

			if (slotMasterDto.getFromDate() != null) {
				namedQuery.append(" AND to_char(am.appointment_date, 'YYYY-MM-DD') >= :fromDate ");
			}

			if (slotMasterDto.getToDate() != null) {
				namedQuery.append(" AND to_char(am.appointment_date, 'YYYY-MM-DD') <= :toDate ");
			}

			if (slotMasterDto.getParentPackageId() != null && slotMasterDto.getParentPackageId() > 0) {
				namedQuery.append(" AND ppm.parent_package_id = :parentPackageId ");
			}

			if (slotMasterDto.getSpecialityId() > 0) {
				namedQuery.append(" AND speciaityMaster.speciality_id = :specialityId ");
			}

			if (i == 0) {
				if (slotMasterDto.getDoctorId() > 0) {
					namedQuery.append(" AND doc.doctor_id = :doctorId ");
				}
			} else {
				if (slotMasterDto.getIsAppointmentRequired() != null && slotMasterDto.getIsAppointmentRequired() == 'N') {
					if (slotMasterDto.getDoctorId() > 0) {
						namedQuery.append(" AND doc.doctor_id = :doctorId ");
					}
				} else {
					if (slotMasterDto.getDoctorId() > 0) {
						namedQuery.append(" AND (CASE " +
								" WHEN sm.doctor_id IS NOT NULL THEN doc.doctor_id = :doctorId " +
								" WHEN unitSpeciaity.dept_wise_appointment = 'Y' THEN doc2.doctor_id = :doctorId " +
								" END) ");
					}
				}
			}

			if (slotMasterDto.getSubSpecialityId() != null && slotMasterDto.getSubSpecialityId() > 0) {
				namedQuery.append(" AND sm.sub_speciality_id = :subSpecialityId ");
			}

			if (slotMasterDto.getModalityId() > 0) {
				namedQuery.append(" AND sm.modality_id = :modalityId ");
			}

			if (slotMasterDto.getVisitTypeId() > 0) {
				namedQuery.append(" AND am.visit_type_id = :visitTypeId ");
			}

			namedQuery.append(" ORDER BY sm.appointment_date + sm.from_time DESC ");

			return namedQuery;
		} catch (Exception e) {
		logger.error("getFinalQueryAfterAppendConditions failed", e);
		}

		return namedQuery;
	}
	
	@Override
	public Response blockedScheduleAppointmentList(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			
			//change for ehc packge //Ganesh
			slotMasterDto.setParentPackageId(null);
			
			StringBuilder rawQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOCKED_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString());
			SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(rawQuery,slotMasterDto,0)).toString());

			if (slotMasterDto.getAppointmentDate() != null) {
				query.setParameter("appointmentDate", slotMasterDto.getAppointmentDate());
			}

			if (slotMasterDto.getPatientId() > 0) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate());
			}

			if (slotMasterDto.getToDate() != null) {
				query.setParameter("toDate", slotMasterDto.getToDate());
			}

			if (slotMasterDto.getParentPackageId() != null && slotMasterDto.getParentPackageId() > 0) {
				query.setParameter("parentPackageId", slotMasterDto.getParentPackageId());
			}

			if (slotMasterDto.getSpecialityId() > 0) {
				query.setParameter("specialityId", slotMasterDto.getSpecialityId());
			}

			if (slotMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", slotMasterDto.getDoctorId());
			}

			if (slotMasterDto.getSubSpecialityId() != null && slotMasterDto.getSubSpecialityId() > 0) {
				query.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId());
			}

			if (slotMasterDto.getModalityId() > 0) {
				query.setParameter("modalityId", slotMasterDto.getModalityId());
			}

			if (slotMasterDto.getVisitTypeId() > 0) {
				query.setParameter("visitTypeId", slotMasterDto.getVisitTypeId());
			}

			List<SlotMasterDto> patientRegistrationDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("appointmentTypeId", slotMasterDto.getAppointmentTypeId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("blockedScheduleAppointmentList failed", he);
		} catch (Exception e) {
		logger.error("blockedScheduleAppointmentList failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public boolean isAppointmentValid(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<SlotMasterDto> slotMasterDtosList=null;
			if(slotMasterDto.getSlotId()!=null)
			{
				
				if(slotMasterDto.getSlotStatusId()==6)
				{
					SlotMasterDto dto = (SlotMasterDto)sessionFactory.getCurrentSession().getNamedQuery("IS_OVERBOOKING_ALLOWED_OR_NOT")
							.setParameter("slotId", slotMasterDto.getSlotId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
					
					return dto.isResult();
				}else{
					Query query = sessionFactory.getCurrentSession().createSQLQuery("select appointment_id as \"appointmentId\" from opd.m_appointment_master "
							+ "WHERE slot_id=:slotId "
							+ "AND status='A' ");
					
					slotMasterDtosList = query
							.setParameter("slotId", slotMasterDto.getSlotId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					if(slotMasterDtosList.size()>0)
						return false;
					else 
						return true;
				}
				
			}
			//return new Response(SUCCESS, null, null, slotMasterDtosList, null);
		}catch (HibernateException he) {
		logger.error("isAppointmentValid failed", he);
		} catch (Exception e) {
		logger.error("isAppointmentValid failed", e);
		} 
		return false;
	}



	@Override
	public Response isAppointmentForSameDateTimeExist(SlotMasterDto slotMasterDto) throws ApplicationException {
	try {
			SlotMasterDto slotMasterDtos = new SlotMasterDto();
			List<SlotMasterDto> slotDetailsByAppointmentList = new LinkedList<>();
			//Query query = null;
			if(slotMasterDto.getAppointmentDate()!=null)
			{
				Query query = sessionFactory.getCurrentSession().createQuery("SELECT am.appointmentId as appointmentId, "
						+ "to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate, "
						+ "concat(to_char(sm.fromTime,'HH:MI')) as fromTimeString, "
						+ "concat(to_char(sm.toTime,'HH:MI')) as toTimeString "
						+ "FROM AppointmentMaster am "
						+ "INNER JOIN am.slotMaster sm "
						+ "WHERE am.appointmentStatusId NOT IN (6,2,3,7,9) "
						+ "AND am.patientId=:patientId "
						+ "AND am.organizationId=:orgId "
						//+ "AND am.unitId=:unitId "
						+ "AND am.status='A' "
						+ "AND (sm.appointmentDate=:appointmentDate OR am.appointmentDate=:appointmentDate) ");
				
				slotDetailsByAppointmentList = query
						.setParameter("patientId", slotMasterDto.getPatientId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						//.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-MM-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				
				if(slotMasterDto.getSlotId()!=null){
					Query query2 = sessionFactory.getCurrentSession().createQuery("SELECT sm.slotId as slotId, "
							+ "to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate, "
							+ "concat(to_char(sm.fromTime,'HH:MI')) as fromTimeString, "
							+ "concat(to_char(sm.toTime,'HH:MI')) as toTimeString "
							+ "FROM SlotMaster sm "
							+ "WHERE sm.slotId=:slotId "
							+ "AND sm.appointmentDate=:appointmentDate ");
					
					 slotMasterDtos =(SlotMasterDto) query2
							.setParameter("slotId", slotMasterDto.getSlotId())
							.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-MM-dd"))
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
				}
				else{
					
					Query query3 = sessionFactory.getCurrentSession().createQuery("SELECT am.appointmentId as appointmentId, "
							+ "to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate, "
							+ "concat(to_char(sm.fromTime,'HH:MI')) as fromTimeString, "
							+ "concat(to_char(sm.toTime,'HH:MI')) as toTimeString "
							+ "FROM AppointmentMaster am "
							+ "INNER JOIN am.slotMaster sm "
							+ "WHERE am.appointmentStatusId NOT IN (6,2,3,7,9) "
							+ "AND am.patientId=:patientId "
							+ "AND am.organizationId=:orgId "
							//+ "AND am.unitId=:unitId "
							+ "AND am.status='A' ");
					
					slotDetailsByAppointmentList = query3
							.setParameter("patientId", slotMasterDto.getPatientId())
							.setParameter("orgId", slotMasterDto.getOrganizationId())
							//.setParameter("unitId", slotMasterDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					
					if(slotMasterDto.getSlotId()!=null){
						Query query2 = sessionFactory.getCurrentSession().createQuery("SELECT sm.slotId as slotId, "
								+ "to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate, "
								+ "concat(to_char(sm.fromTime,'HH:MI')) as fromTimeString, "
								+ "concat(to_char(sm.toTime,'HH:MI')) as toTimeString "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.slotId=:slotId ");
						
						 slotMasterDtos =(SlotMasterDto) query2
								.setParameter("slotId", slotMasterDto.getSlotId())
								.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
					}
				}
			}
			
			
			return new Response(SUCCESS, null, null, slotDetailsByAppointmentList,slotMasterDtos);
		}catch (HibernateException he) {
		logger.error("isAppointmentForSameDateTimeExist failed", he);
		} catch (Exception e) {
		logger.error("isAppointmentForSameDateTimeExist failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(SlotMasterDto slotMasterDto)
			throws ApplicationException {
		try {
				SlotMasterDto slotMasterDtos = new SlotMasterDto();	
				List<SlotMasterDto> slotDetailsByAppointmentList = new LinkedList<>();
				Query query = null;
				if(slotMasterDto.getAppointmentDate()!=null){
					
					query = sessionFactory.getCurrentSession().createQuery("SELECT am.appointmentId as appointmentId, "
							+ "sm.doctorId as doctorId, "
							+ "sm.specialityId as specialityId, "
							+ "sm.subSpecialityId as subSpecialityId, "
							+ "sm.modalityId as modalityId "
							+ "FROM AppointmentMaster am "
							+ "INNER JOIN am.slotMaster sm "
							+ "WHERE am.appointmentStatusId NOT IN (6,2,3,7,9) "
							+ "AND am.patientId=:patientId "
							+ "AND am.organizationId=:orgId "
							//+ "AND am.unitId=:unitId "
							+ "AND am.appointmentTypeId=:appointmentTypeId "
							+ "AND sm.appointmentDate=:appointmentDate "
							+ "AND am.status='A' ");
					
					slotDetailsByAppointmentList = query
							.setParameter("patientId", slotMasterDto.getPatientId())
							.setParameter("orgId", slotMasterDto.getOrganizationId())
							//.setParameter("unitId", slotMasterDto.getUnitId())
							.setParameter("appointmentTypeId", slotMasterDto.getAppointmentTypeId())
							.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-MM-dd"))
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					
					if(slotMasterDto.getSlotId()!=null){
					Query query2 = sessionFactory.getCurrentSession().createQuery("SELECT sm.slotId as slotId, "
							+ "sm.doctorId as doctorId, "
							+ "sm.specialityId as specialityId, "
							+ "sm.subSpecialityId as subSpecialityId, "
							+ "sm.modalityId as modalityId "
							+ "FROM SlotMaster sm "
							+ "WHERE sm.slotId=:slotId "
							+ "AND sm.appointmentDate=:appointmentDate ");
					
					 slotMasterDtos =(SlotMasterDto) query2
							.setParameter("slotId", slotMasterDto.getSlotId())
							.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-MM-dd"))
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
				}else{
					
					query = sessionFactory.getCurrentSession().createQuery("SELECT am.appointmentId as appointmentId, "
							+ "sm.doctorId as doctorId, "
							+ "sm.specialityId as specialityId, "
							+ "sm.subSpecialityId as subSpecialityId, "
							+ "sm.modalityId as modalityId "
							+ "FROM AppointmentMaster am "
							+ "INNER JOIN am.slotMaster sm "
							+ "WHERE am.appointmentStatusId NOT IN (6,2,3,7,9) "
							+ "AND am.patientId=:patientId "
							+ "AND am.organizationId=:orgId "
							//+ "AND am.unitId=:unitId "
							+ "AND am.appointmentTypeId=:appointmentTypeId "
							+ "AND am.status='A' ");
					
					slotDetailsByAppointmentList = query
							.setParameter("patientId", slotMasterDto.getPatientId())
							.setParameter("orgId", slotMasterDto.getOrganizationId())
							//.setParameter("unitId", slotMasterDto.getUnitId())
							.setParameter("appointmentTypeId", slotMasterDto.getAppointmentTypeId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					
					if(slotMasterDto.getSlotId()!=null){
					Query query2 = sessionFactory.getCurrentSession().createQuery("SELECT sm.slotId as slotId, "
							+ "sm.doctorId as doctorId, "
							+ "sm.specialityId as specialityId, "
							+ "sm.subSpecialityId as subSpecialityId, "
							+ "sm.modalityId as modalityId "
							+ "FROM SlotMaster sm "
							+ "WHERE sm.slotId=:slotId ");
					
					 slotMasterDtos =(SlotMasterDto) query2
							.setParameter("slotId", slotMasterDto.getSlotId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
					}
				
				}
			}			
			return new Response(SUCCESS, null, null, slotDetailsByAppointmentList, slotMasterDtos);
		} catch (HibernateException he) {
		logger.error("isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment failed", he);
		} catch (Exception e) {
		logger.error("isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response updateOrderDetailsIsAppointableServiceStatus(Integer orderDetailsId, Integer updatedBy)
			throws ApplicationException {
		try {

			sessionFactory.getCurrentSession()
					.createQuery("update OrderDetailsMaster od set " 
							+ "od.isAppointableRadiology='Y', "
							+ "od.updatedBy=:updatedBy , " 
							+ "od.updatedDate=:updatedDate "
							+ "where od.orderDetailsId=:orderDetailsId ")
					.setParameter("updatedBy",updatedBy)
					.setParameter("updatedDate",TimeZoneComponent.getDateByzone(0))
					.setParameter("orderDetailsId", orderDetailsId)

				.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("updateOrderDetailsIsAppointableServiceStatus failed", he);
		} catch (Exception e) {
		logger.error("updateOrderDetailsIsAppointableServiceStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response markArrival(Integer appointmentId,String currentDate,Integer unitId)throws ApplicationException{
		try {
			Integer tokenNumber = getTokenNumber(currentDate, unitId);
			Date currentDateObj = TimeZoneComponent.getDateByzone(unitId);
			sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster am set " +
							"am.arrivalTime = :arrivalTime, " +
							"am.tokenNumber = :tokenNumber, " +
							"am.dummyTokenNumber = :dummyTokenNumber, " +
							"am.updatedDate = :updatedDate " +
							"where am.appointmentId = :appointmentId")
					.setParameter("arrivalTime", currentDateObj)
					.setParameter("tokenNumber", tokenNumber)
					.setParameter("dummyTokenNumber", tokenNumber)
					.setParameter("updatedDate", currentDateObj)
					.setParameter("appointmentId", appointmentId)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, "Arrival Marked !!", null, tokenNumber);
		}
		catch(ConstraintViolationException e){
		logger.error("markArrival failed", e);
			return new Response(ERROR, ALREADY_EXIST_CODE, "Please try again...!!", null, null);
		}
		catch (HibernateException he) {
		logger.error("markArrival failed", he);
		} catch (Exception e) {
		logger.error("markArrival failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response markArrivalForPaidAppointments(EncounterMasterDto encounterMasterDto) {
		try {
			Date currentDateObj = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId());
			encounterMasterDto.setEncounterDate(currentDateObj.getTime());
			Integer tokenNumber = getTokenNumber(encounterMasterDto.getCurrentDate(), encounterMasterDto.getUnitId());

			Query sql=sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster am set " +
							"am.arrivalTime = :arrivalTime, " +
							"am.appointmentStatusId = :appointmentStatusId, " +
							"am.tokenNumber = :tokenNumber, " +
							"am.dummyTokenNumber = :dummyTokenNumber, " +
							"am.updatedDate = :updatedDate " +
							"where am.appointmentId = :appointmentId")
					.setParameter("arrivalTime", currentDateObj)
					.setParameter("appointmentStatusId", 5)
					.setParameter("tokenNumber", tokenNumber)
					.setParameter("dummyTokenNumber", tokenNumber)
					.setParameter("updatedDate", currentDateObj)
					.setParameter("appointmentId", encounterMasterDto.getAppointmentId());
			sql.executeUpdate();

			if (encounterMasterDto.getDeptWiseAppointment() == 'N') {
				Date currentDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId());
				Integer docQueueNumber = iEncounterMasterService.generateDocQueueNumber(encounterMasterDto);
					Query query = sessionFactory.getCurrentSession()
							.createQuery("update EncounterMaster em set " +
									"em.statusId = :statusId, " +
									"em.unitTokenNumber = :unitTokenNumber, " +
									"em.dummyTokenNumber = :dummyTokenNumber, " +
									"em.docQueueNumber = :docQueueNumber, " +
									"em.updatedDate = :updatedDate " +
									"where em.encounterId = :encounterId")
							.setParameter("statusId", 5)
							.setParameter("unitTokenNumber", tokenNumber)
							.setParameter("dummyTokenNumber", tokenNumber)
							.setParameter("docQueueNumber", docQueueNumber)
							.setParameter("updatedDate", currentDate.getTime())
							.setParameter("encounterId", encounterMasterDto.getEncounterId());
					query.executeUpdate();

			}else {
				Date updatedDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId());
				sessionFactory.getCurrentSession()
						.createQuery("update EncounterMaster em set " +
								"em.statusId = :statusId, " +
								"em.unitTokenNumber = :unitTokenNumber, " +
								"em.dummyTokenNumber = :dummyTokenNumber, " +
								"em.specialityQueueNumber = :specialityQueueNumber, " +
								"em.updatedDate = :updatedDate " +
								"where em.encounterId = :encounterId")
						.setParameter("statusId", 5)
						.setParameter("unitTokenNumber", tokenNumber)
						.setParameter("dummyTokenNumber", tokenNumber)
						.setParameter("specialityQueueNumber", iEncounterMasterService.generateSpecialityQueueNumber(encounterMasterDto))
						.setParameter("updatedDate", updatedDate.getTime())
						.setParameter("encounterId", encounterMasterDto.getEncounterId())
						.executeUpdate();
			}

			return new Response(SUCCESS, SUCCESS_CODE, "Arrival Marked !!", null, tokenNumber);
		}catch(ConstraintViolationException e){
		logger.error("markArrivalForPaidAppointments failed", e);
			return new Response(ERROR, ALREADY_EXIST_CODE, "Please try again...!!", null, null);
		}
		catch (HibernateException he) {
		logger.error("markArrivalForPaidAppointments failed", he);
		} catch (Exception e) {
		logger.error("markArrivalForPaidAppointments failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}




	@Override
	public Response updateSlotId(Integer appointmentId, Integer slotId, Integer oldSlotId) throws ApplicationException {
		try {
			
			sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster am set " 
							+ "am.slotId=:slotId, "
							+ "am.updatedDate=:updatedDate "
							+ "where am.appointmentId=:appointmentId ")
					.setParameter("updatedDate",TimeZoneComponent.getDateByzone(0))
					.setParameter("appointmentId", appointmentId)
					.setParameter("slotId", slotId)
				.executeUpdate();
			
			if(oldSlotId != null && oldSlotId > 0)
			{
				sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
						+ "sm.slotStatusId="+Vaccant+", " //slot booked
						+ "sm.updatedDate=:updatedDate "
						+ "where sm.slotId=:slotId ")
				.setParameter("updatedDate",TimeZoneComponent.getDateByzone(0))
				.setParameter("slotId", oldSlotId)
			.executeUpdate();
			}

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("updateSlotId failed", he);
		} catch (Exception e) {
		logger.error("updateSlotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	public Response batchJobToUpdateMultiVisitPackage(List<Integer> unitIds) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			String sqlQuery =
					"UPDATE billing.t_package_consumption_master " +
							"SET applicable_no_of_visit = applicable_no_of_visit - 1 " +
							"WHERE patient_id IN ( " +
							"    SELECT DISTINCT COALESCE(om.patient_id, 0) " +
							"    FROM public.t_order_details od " +
							"    INNER JOIN public.t_order_master om ON om.order_id = od.order_id " +
							"    INNER JOIN public.t_order_details_package_mapper odpckmppr ON " +
							"        od.order_details_id = odpckmppr.order_details_id " +
							"        AND odpckmppr.status = 'A' " +
							"    INNER JOIN service.m_package_master pkgmst ON " +
							"        odpckmppr.package_id = pkgmst.package_master_id " +
							"        AND pkgmst.package_type_id = 5 " +
							"    WHERE TO_CHAR(om.order_date, 'YYYY-MM-DD') < :orderDate " +
							"    AND od.status = 'A' " +
							") " +
							"AND package_id IN ( " +
							"    SELECT DISTINCT COALESCE(odpckmppr.package_id, 0) " +
							"    FROM public.t_order_details od " +
							"    INNER JOIN public.t_order_master om ON om.order_id = od.order_id " +
							"    INNER JOIN public.t_order_details_package_mapper odpckmppr ON " +
							"        od.order_details_id = odpckmppr.order_details_id " +
							"        AND odpckmppr.status = 'A' " +
							"    INNER JOIN service.m_package_master pkgmst ON " +
							"        odpckmppr.package_id = pkgmst.package_master_id " +
							"        AND pkgmst.package_type_id = 5 " +
							"    WHERE TO_CHAR(om.order_date, 'YYYY-MM-DD') < :orderDate " +
							"    AND od.status = 'A' " +
							") " +
							"AND package_type_id = 5 " +
							"AND applicable_no_of_visit > 0 " +
							"AND status = 'A' " +
							"AND unit_id IN (:unitIds)";

			Query query = session.createSQLQuery(sqlQuery);
			query.setParameter("orderDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd"));
			query.setParameterList("unitIds", unitIds); // Assumes `unitIds` is a collection like List or Set
			int result = query.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				throw e;
			}
	}
	

	@Override
	public Response batchJobToUpdateAppointmentStatus() throws ApplicationException {
		try {
			
				Date date = TimeZoneComponent.getDateByzone(0);
			String sqlQuery =
					"UPDATE opd.m_appointment_master " +
							"SET appointment_status_id = 7, " +
							"    updated_date = :updatedDate " +
							"WHERE (appointment_status_id = 1 OR appointment_status_id = 8) " +
							"  AND app_date_time + INTERVAL '24 HOURS' < :appDateTime";
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);
			query.setParameter("updatedDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd HH:mm:ss"));
			query.setParameter("appDateTime", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss"));
			query.executeUpdate();

			String sqlQuery1 =
					"UPDATE opd.m_appointment_master " +
							"SET appointment_status_id = 9, " +
							"    updated_date = :updatedDate " +
							"WHERE (appointment_status_id = 5 OR appointment_status_id = 6) " +
							"  AND app_date_time + INTERVAL '24 HOURS' < :appDateTime";

			Query query1 = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery1);
			query1.setParameter("updatedDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd HH:mm:ss"));
			query1.setParameter("appDateTime", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss"));
			query1.executeUpdate();
			String sqlQuery2 =
					"UPDATE opd.t_encounter_master enc " +
							"SET enc.status_id = 9, " +
							"    enc.updated_date = :updatedDate " +
							"WHERE enc.appointment_id IN ( " +
							"    SELECT mam.appointment_id " +
							"    FROM opd.m_appointment_master mam " +
							"    WHERE (enc.status_id = 5 OR enc.status_id = 6) " +
							"      AND enc.appointment_id = mam.appointment_id " +
							"      AND mam.app_date_time + INTERVAL '24 HOURS' < :appDateTime)";

			Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery2);
			query2.setParameter("updatedDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss"));
			query2.setParameter("appDateTime", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss"));
			query2.executeUpdate();


			String sqlQuery3 =
					"UPDATE opd.t_encounter_master enc " +
							"SET enc.status_id = 7, " +
							"    enc.updated_date = :updatedDate " +
							"WHERE enc.appointment_id IN ( " +
							"    SELECT mam.appointment_id " +
							"    FROM opd.m_appointment_master mam " +
							"    WHERE enc.status_id = 8 " +
							"      AND enc.appointment_id = mam.appointment_id " +
							"      AND mam.app_date_time + INTERVAL '24 HOURS' < :appDateTime)";

			Query query3 = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery3);
			query3.setParameter("updatedDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss"));
			query3.setParameter("appDateTime", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss"));
			query3.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("batchJobToUpdateAppointmentStatus failed", he);
		} catch (Exception e) {
		logger.error("batchJobToUpdateAppointmentStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}



	@Override
	public Response updateAppointmentTypeId(Integer appointmentId) throws ApplicationException {
         try {
			sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster am set " 
							+ "am.appointmentTypeId=5 "
							+ "where am.appointmentId=:appointmentId ")
					.setParameter("appointmentId", appointmentId)
				.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("updateAppointmentTypeId failed", he);
		} catch (Exception e) {
		logger.error("updateAppointmentTypeId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response assignCounterNumber(SlotMasterDto slotMasterDto) throws ApplicationException {
		 try {
				sessionFactory.getCurrentSession()
						.createQuery("update AppointmentMaster am set " 
								+ "am.counterId=:counterId, "
								+ "am.frontDeskArrival=:frontDeskArrival "
								+ "where am.appointmentId=:appointmentId ")
						.setParameter("counterId",slotMasterDto.getCounterId())
						.setParameter("appointmentId", slotMasterDto.getAppointmentId())
						.setParameter("frontDeskArrival", slotMasterDto.getFrontDeskArrival())
					.executeUpdate();
				
				if(slotMasterDto.getRemark()!= null && !slotMasterDto.getRemark().isEmpty())
				{

					String sql = "INSERT INTO opd.t_broadcast_msg (message, type, organization_id, unit_id, status) " +
							"VALUES (:message, :type, :organizationId, :unitId, :status)";
					Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
					query.setParameter("message", slotMasterDto.getRemark());
					query.setParameter("type", 1);
					query.setParameter("organizationId", slotMasterDto.getOrganizationId());
					query.setParameter("unitId", slotMasterDto.getUnitId());
					query.setParameter("status", "A");
					query.executeUpdate();

				}

				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			} catch (HibernateException he) {
			logger.error("assignCounterNumber failed", he);
			} catch (Exception e) {
			logger.error("assignCounterNumber failed", e);
			}
			return new Response(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Integer getTokenNumber(String currentDate,Integer unitId) throws ApplicationException {
		try {

			String query = "SELECT max(token_number) as \"unitTokenNumber\" "
					+ " FROM opd.m_appointment_master "
					+ " WHERE unit_id=:unitId "
					+ " AND to_char(appointment_date,'YYYY-MM-DD')=:date ";

			String tokenScopeDate = (currentDate!=null && currentDate.length()>10) ? currentDate.substring(0, 10) : currentDate;

			EncounterMasterDto encounterMasterDto = (EncounterMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameter("date", tokenScopeDate)
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			if(encounterMasterDto.getUnitTokenNumber()!=null)
				return encounterMasterDto.getUnitTokenNumber() + 1;
			else
				return 1;
			
		} catch (Exception e) {
		logger.error("getTokenNumber failed", e);
		}
		return null;
	}

	@Override
	public Response searchInProcessAppointment(Integer appointmentId) throws ApplicationException {
		try{
			
			List<SlotMasterDto> slotMasterDtosList2 = sessionFactory.getCurrentSession().createSQLQuery(
					"select en.appointment_id from opd.t_encounter_master en where en.appointment_id =:appointmentId ")
					.setParameter("appointmentId",appointmentId).list();
			if(slotMasterDtosList2.size()==0)
			{
				StringBuilder str = new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString().toString());
				str.append(" WHERE am.appointment_id =:appointmentId ");
				str.append(" and am.status ='I'");

				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createSQLQuery(str.toString())
						.setParameter("appointmentId",appointmentId)
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, "Patient already has an appointment which is in process !!",
						slotMasterDtosList, null);
			}
		}catch(HibernateException e){
		logger.error("searchInProcessAppointment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response scheduleAppointmentFromMyLife(SlotMasterDto slotMasterDto) throws ApplicationException {
    try {
    	
    	
    	Random rnd = new Random();
		Integer appointmentUniqueId = rnd.nextInt(99999999);
		
			AppointmentMaster appointmentMaster = mapper.map(slotMasterDto, AppointmentMaster.class,"SlotMasterDto_to_AppointmentMaster");
				appointmentMaster.setIsAppointmentFromMyLife('Y');
				appointmentMaster.setAppDateTime(GlobalCommonDateUtils.getDate(iSlotMasterDao.getAppDateTimeBySlotId(appointmentMaster.getSlotId()),"yyyy-MM-dd HH:mm:ss"));
				appointmentMaster.setIsTeleconsultationAppointment(slotMasterDto.getIsTeleconsultationAppointment());
				appointmentMaster.setCreatedDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
				appointmentMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()));
				appointmentMaster.setAppointmentUniqueId(appointmentUniqueId);
			sessionFactory.getCurrentSession().save(appointmentMaster);
			Integer count=0;
			
			if(slotMasterDto.getSlotId()!=null)
			{
				count=	sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
					+ "sm.slotStatusId=:slotStatusId, "
					+ "sm.updatedDate=:updatedDate "
					+ "where sm.slotId=:slotId ")
				.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
				.setParameter("slotId",slotMasterDto.getSlotId())
				.setParameter("slotStatusId",slotMasterDto.getSlotStatusId())
			    .executeUpdate();
			
				if(count>0){
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, appointmentMaster);
				}else{
					return new Response(ERROR, ERROR_CODE, null, null, appointmentMaster);
				}
			}else{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, appointmentMaster);
			}
        } catch (HibernateException he) {
        logger.error("scheduleAppointmentFromMyLife failed", he);
        } catch (Exception e) {
		logger.error("scheduleAppointmentFromMyLife failed", e);
		}
		return new Response(ERROR, null, null, null, null);
  }
	public Response updateCallCount(Integer appointmentId,Integer callCount) throws ApplicationException {
		try {
				sessionFactory.getCurrentSession()
						.createQuery("update AppointmentMaster am set " 
								+ "am.callCount =:callCount "
								+ "where am.appointmentId=:appointmentId ")
						.setParameter("appointmentId",appointmentId)
						.setParameter("callCount",callCount)
					.executeUpdate();

				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			} catch (HibernateException he) {
			logger.error("updateCallCount failed", he);
			} catch (Exception e) {
			logger.error("updateCallCount failed", e);
			}
			return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateToken(Integer appointmentId, Integer tokenNumber) throws ApplicationException {
		try {
			
			Integer updatedToken = tokenNumber+5;
			sessionFactory.getCurrentSession()
				.createQuery("update AppointmentMaster am set am.callCount=0" 
						+ " ,am.dummyTokenNumber=:updatedToken "
						+ " where am.appointmentId=:appointmentId ")
				.setParameter("appointmentId",appointmentId)
					.setParameter("updatedToken",updatedToken)
			.executeUpdate();

			String hql = "update AppointmentMaster am set " +
					"am.dummyTokenNumber = am.dummyTokenNumber - 1 " +
					"where am.dummyTokenNumber <= :updatedToken " +
					"and :tokenNumber < am.dummyTokenNumber " +
					"and to_char(am.appointmentDate, 'YYYY-MM-DD') = :appointmentDate " +
					"and am.appointmentId != :appointmentId";

			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("updatedToken", updatedToken);
			query.setParameter("tokenNumber", tokenNumber);
			query.setParameter("appointmentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "YYYY-MM-DD"));
			query.setParameter("appointmentId", appointmentId);
			int result = query.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("updateToken failed", he);
		} catch (Exception e) {
		logger.error("updateToken failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response rescheduleAppointmentFromMyMylife(SlotMasterDto slotMasterDto) {
		try {
			Session session= sessionFactory.getCurrentSession();
			session.createQuery("update AppointmentMaster am set "
						+ "am.appointmentStatusId=:statusId, "
						+ "am.updatedDate=:updatedDate,am.isAppointmentFromMyLife='R',"
						+ "am.status='I' "
						+ "WHERE am.appointmentId=:appointmentId")
			        .setParameter("statusId", slotMasterDto.getAppointmentStatusId())
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("appointmentId", slotMasterDto.getAppointmentId())
			.executeUpdate();
			
			
			Integer slotId=(Integer)sessionFactory.getCurrentSession().createSQLQuery("select am.slot_id from opd.m_appointment_master am "
					+ " where am.appointment_id=:appId ")
					.setParameter("appId", slotMasterDto.getAppointmentId()).uniqueResult();
			session.createQuery("update SlotMaster sm set "
						+ "sm.slotStatusId="+Vaccant+", " //slot vacant
						+ "sm.updatedDate=:updatedDate "
						+ "where sm.slotId=:slotId")
					.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"))
					.setParameter("slotId",slotId)
			.executeUpdate();
			
			return new Response(SUCCESS, null, "Appointment Rescheduled Successfully", null, null);
			
		} catch (HibernateException he) {
		logger.error("rescheduleAppointmentFromMyMylife failed", he);
		} catch (Exception e) {
		logger.error("rescheduleAppointmentFromMyMylife failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	public Response assignPatientToAppointmentFromPatientRegistration(Integer appointmentId, Integer patientId)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster am set " 
							+ "am.patientId =:patientId, "
							+ "am.appointmentStatusId = 8 "
							+ "where am.appointmentId=:appointmentId ")
					.setParameter("appointmentId",appointmentId)
					.setParameter("patientId",patientId)
				.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("assignPatientToAppointmentFromPatientRegistration failed", he);
		} catch (Exception e) {
		logger.error("assignPatientToAppointmentFromPatientRegistration failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response updateTokenNursing(Integer encounterId, Integer tokenNumber, Integer orderDetailsId) throws ApplicationException {
		try {
			
			if(tokenNumber != null) {
			Integer updatedToken = tokenNumber+5;
			sessionFactory.getCurrentSession()
				.createQuery("update EncounterMaster em set em.callCount=0" 
						+ " ,em.dummyTokenNumber=:updatedToken "
						+ " where em.encounterId=:encounterId ")
				.setParameter("encounterId",encounterId)
					.setParameter("updatedToken",updatedToken)
			.executeUpdate();

				String hql = "update EncounterMaster em set " +
						"em.dummyTokenNumber = em.dummyTokenNumber - 1 " +
						"where em.dummyTokenNumber <= :updatedToken " +
						"and :tokenNumber < em.dummyTokenNumber " +
						"and to_char(em.encounterDate, 'YYYY-MM-DD') = :encounterDate " +
						"and em.encounterId != :encounterId";

				Query query = sessionFactory.getCurrentSession().createQuery(hql);

				query.setParameter("updatedToken", updatedToken);
				query.setParameter("tokenNumber", tokenNumber);
				query.setParameter("encounterDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd"));
				query.setParameter("encounterId", encounterId);
				int result = query.executeUpdate();

				Session session1=sessionFactory.getCurrentSession();
			session1.createSQLQuery(" update opd.t_encounter_master set nursing_station_id = null where encounter_id =:encounterId ")
					.setParameter("encounterId",encounterId)
			.executeUpdate();
			}else {}

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("updateTokenNursing failed", he);
		} catch (Exception e) {
		logger.error("updateTokenNursing failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response isAppointmentIsForSameSpecialityWithoutCompletionOfPreviousAppintment(SlotMasterDto slotMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Response blockAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			if(slotMasterDto.getSlotId()!=null){
		            sessionFactory.getCurrentSession().createQuery("UPDATE SlotMaster sm "
					+ "set sm.isBlocked=1 "
					+ "WHERE sm.slotId=:slotId ")
					.setParameter("slotId", slotMasterDto.getSlotId())
					.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("blockAppointment failed", he);
		} catch (Exception e) {
		logger.error("blockAppointment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Integer appointmentCheckForKioskPatient(SlotMasterDto slotMasterDto) {
		try {
			String query = "SELECT appointment_id as \"appointmentId\" "
					+ " FROM opd.m_appointment_master "
					+ " WHERE unit_id=:unitId "
					+ "	AND patient_id=:patientId "
					+ " AND to_char(appointment_date,'YYYY-MM-DD')=:date "
					+ " AND slot_id is null "
					+ " AND appointment_status_id=1 "
					+ " AND appointment_type_id=1 "
					+ "	limit 1";
			
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("date", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "yyyy-MM-dd"))
					.setParameter("patientId", slotMasterDto.getPatientId())
			.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			if(encounterMasterDto!=null && encounterMasterDto.getAppointmentId()!=null && encounterMasterDto.getAppointmentId()>0)
				return 1;
			else 
				return 0;
			} catch (HibernateException he) {
			logger.error("appointmentCheckForKioskPatient failed", he);
			} catch (Exception e) {
			logger.error("appointmentCheckForKioskPatient failed", e);
			}
			return 0;
	}

	@Override
	public Response afterConversionPaidAppointment(EncounterMasterDto encounterMasterDto) {
		try {
			encounterMasterDto.setEncounterDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());

			sessionFactory.getCurrentSession()
				.createQuery("update AppointmentMaster am set " 
						+ "am.appointmentStatusId = 5, "
						+ "am.updatedDate=:updatedDate "
						+ "where am.appointmentId=:appointmentId ")
				.setParameter("updatedDate",TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()))
				.setParameter("appointmentId", encounterMasterDto.getAppointmentId())
			.executeUpdate();
			if(encounterMasterDto.getIsKioskApplicable()=='Y')
			{
				if(encounterMasterDto.getDeptWiseAppointment()=='N')
				{
					String hql = "update EncounterMaster am set " +
							"am.statusId = 5, " +
							"am.unitTokenNumber = :unitTokenNumber, " +
							"am.dummyTokenNumber = :unitTokenNumber, " +
							"am.docQueueNumber = :docQueueNumber, " +
							"am.updatedDate = :updatedDate " +
							"where am.encounterId = :encounterId";

					Query query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setParameter("unitTokenNumber", encounterMasterDto.getUnitTokenNumber());
					query.setParameter("docQueueNumber", iEncounterMasterService.generateDocQueueNumber(encounterMasterDto));
					query.setParameter("updatedDate", TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()));
					query.setParameter("encounterId", encounterMasterDto.getEncounterId());
					query.executeUpdate();

				}else{
					String hql = "update EncounterMaster am set " +
							"am.statusId = 5, " +
							"am.unitTokenNumber = :unitTokenNumber, " +
							"am.dummyTokenNumber = :unitTokenNumber, " +
							"am.specialityQueueNumber = :specialityQueueNumber, " +
							"am.updatedDate = :updatedDate " +
							"where am.encounterId = :encounterId";

					Query query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setParameter("unitTokenNumber", encounterMasterDto.getUnitTokenNumber());
					query.setParameter("specialityQueueNumber", iEncounterMasterService.generateSpecialityQueueNumber(encounterMasterDto));
					query.setParameter("updatedDate", TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()));
					query.setParameter("encounterId", encounterMasterDto.getEncounterId());
					query.executeUpdate();
				}
			}else{
				String hql = "update EncounterMaster am set " +
						"am.statusId = 5, " +
						"am.updatedDate = :updatedDate " +
						"where am.encounterId = :encounterId";
				Query query = sessionFactory.getCurrentSession().createQuery(hql);
				query.setParameter("updatedDate", TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()));
				query.setParameter("encounterId", encounterMasterDto.getEncounterId());
				query.executeUpdate();
			}

		return new Response(SUCCESS, SUCCESS_CODE, "Arrival Marked !!", null, null);
	}catch(ConstraintViolationException e){
	logger.error("afterConversionPaidAppointment failed", e);
		return new Response(ERROR, ALREADY_EXIST_CODE, "Please try again...!!", null, null);
	}
	catch (HibernateException he) {
	logger.error("afterConversionPaidAppointment failed", he);
	} catch (Exception e) {
	logger.error("afterConversionPaidAppointment failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response blockSlot(Integer slotId, Integer statusId) {
		try {
				sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
					+ "sm.slotStatusId=:slotStatusId, "
					+ "sm.updatedDate=:updatedDate "
					+ "where sm.slotId=:slotId ")
				.setParameter("updatedDate",GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0),"dd-MM-yyyy HH:mm:ss"))
				.setParameter("slotId",slotId)
				.setParameter("slotStatusId",statusId)
				.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("blockSlot failed", he);
		} catch (Exception e) {
		logger.error("blockSlot failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response searchAppointmentsByPatienId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			StringBuilder stringQuery = null;
			if(slotMasterDto.getIsAppointmentRequired() != null && slotMasterDto.getIsAppointmentRequired() == 'N') {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST_FOR_IS_APPOINTMENT_FALSE").getQueryString().toString());
			}else {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString().toString());
			}
			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)	
			{
				stringQuery.append(" WHERE am.organization_id = :orgId "
						+ "AND am.unit_id = :unitId "
						+ "AND am.appointment_type_id = 1 "
						+ "AND am.status = 'A' "
						+ "AND am.patient_id = :patientId");

				if (slotMasterDto.getSpecialityId() > 0) {
					stringQuery.append(" AND sm.speciality_id = :specialityId");
				}

				if (slotMasterDto.getDoctorId() != null && slotMasterDto.getDoctorId() > 0) {
					stringQuery.append(" AND sm.doctor_id = :doctorId");
				}

				if (slotMasterDto.getIsCyclicAppointment() != null && slotMasterDto.getIsCyclicAppointment().equals('Y')) {
					stringQuery.append(" AND am.is_cyclic_appointment = 'Y' ");
				}

				stringQuery.append(" ORDER BY sm.appointment_date, sm.from_time");

				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				query.setParameter("orgId", slotMasterDto.getOrganizationId());
				query.setParameter("unitId", slotMasterDto.getUnitId());
				query.setParameter("patientId", slotMasterDto.getPatientId());

				if (slotMasterDto.getSpecialityId() > 0) {
					query.setParameter("specialityId", slotMasterDto.getSpecialityId());
				}

				if (slotMasterDto.getDoctorId() != null && slotMasterDto.getDoctorId() > 0) {
					query.setParameter("doctorId", slotMasterDto.getDoctorId());
				}
				List<SlotMasterDto> slotMasterDtosList = query
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
						.list();
				Integer count=slotMasterDtosList.size();
				return new Response(SUCCESS, null, null, slotMasterDtosList, count);
				
			}
		
			SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery((this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto,1)).toString());
			if (slotMasterDto.getAppointmentDate() != null) {
				query.setParameter("appointmentDate", slotMasterDto.getAppointmentDate());
			}

			if (slotMasterDto.getPatientId() > 0) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate());
			}

			if (slotMasterDto.getToDate() != null) {
				query.setParameter("toDate", slotMasterDto.getToDate());
			}

			if (slotMasterDto.getParentPackageId() != null && slotMasterDto.getParentPackageId() > 0) {
				query.setParameter("parentPackageId", slotMasterDto.getParentPackageId());
			}

			if (slotMasterDto.getSpecialityId() > 0) {
				query.setParameter("specialityId", slotMasterDto.getSpecialityId());
			}

			if (slotMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", slotMasterDto.getDoctorId());
			}

			if (slotMasterDto.getSubSpecialityId() != null && slotMasterDto.getSubSpecialityId() > 0) {
				query.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId());
			}

			if (slotMasterDto.getModalityId() > 0) {
				query.setParameter("modalityId", slotMasterDto.getModalityId());
			}

			if (slotMasterDto.getVisitTypeId() > 0) {
				query.setParameter("visitTypeId", slotMasterDto.getVisitTypeId());
			}
			List<SlotMasterDto> slotMasterDtosList = query
				.setParameter("orgId", slotMasterDto.getOrganizationId())
				.setParameter("unitId", slotMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			Integer count=slotMasterDtosList.size();
			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
			
		} catch (HibernateException he) {
		logger.error("searchAppointmentsByPatienId failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Boolean getEHCAppointmentsByPatienId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			String query = "SELECT appointment_id as \"appointmentId\" "
					+ " FROM opd.m_appointment_master "
					+ " WHERE patient_id=:patientId "
					+ " AND to_char(appointment_date,'YYYY-MM-DD')=:date "
					+ " AND appointment_status_id NOT IN  (6,2,3,7,9) "
					+ " AND appointment_type_id=3 ";
			
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("date",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "yyyy-MM-dd"))
					.setParameter("patientId", slotMasterDto.getPatientId())
			.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			if(encounterMasterDto!=null && encounterMasterDto.getAppointmentId()!=null && encounterMasterDto.getAppointmentId()>0)
				return true;
			else 
				return false;
			} catch (HibernateException he) {
			logger.error("getEHCAppointmentsByPatienId failed", he);
			} catch (Exception e) {
			logger.error("getEHCAppointmentsByPatienId failed", e);
			}
			return false;
	}



	@Override
	public Response searchCyclicServiceAppointmentsByPatienId(SlotMasterDto slotMasterDto) throws ApplicationException {

		try {
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_CYCLIC_APPOINTMENTS_SLOTS_LIST_BY_PATIENTID").getQueryString().toString());

				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						+ " AND am.status='A' " //AND am.appointment_type_id=10
						+ "AND am.patient_id=:patientId AND msm.service_master_id=:serviceMasterId "
						+ "	AND ((ordM.visit_type_id = 1 AND  (case when (mbm.bill_status_id!=1 and mbm.bill_status_id is not null) then tbd.status = 'A' else true end) AND appConfig.is_pre_or_post_billing='Y') "
						+ "	OR  (ordM.visit_type_id = 1 AND appConfig.is_pre_or_post_billing='N') OR (ordM.visit_type_id != 1)) "
						+ " AND am.status='A' AND ord.status='A' "
						);
				stringQuery.append(" ORDER BY am.appointment_date ");
				SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
				
				List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("patientId", slotMasterDto.getPatientId())
					.setParameter("serviceMasterId", slotMasterDto.getServiceId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				Integer count=slotMasterDtosList.size();
				return new Response(SUCCESS, null, null, slotMasterDtosList, count);
		} catch (HibernateException he) {
		logger.error("searchCyclicServiceAppointmentsByPatienId failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response createCyclicAppointmentLog(SlotMasterDto slotDto) {
		try {
			LocalTime appTime1 = LocalTime.parse(slotDto.getAppointmentTime());
			LocalDate today = LocalDate.now();
			Instant appTime = appTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			CyclicAppointmentMapper cyclicAppointmentMapper=new CyclicAppointmentMapper();
			cyclicAppointmentMapper.setAppointmentId(slotDto.getAppointmentId());
			if(slotDto.getBillId()!=null)
			cyclicAppointmentMapper.setBillId(slotDto.getBillId());
			cyclicAppointmentMapper.setServiceId(slotDto.getServiceMasterId());
			cyclicAppointmentMapper.setOrderDetailId(slotDto.getOrderDetailsId());
			cyclicAppointmentMapper.setAppointmentTime(appTime);
			cyclicAppointmentMapper.setOrganizationId(slotDto.getOrganizationId());
			cyclicAppointmentMapper.setUnitId(slotDto.getUnitId());
			cyclicAppointmentMapper.setCreatedBy(slotDto.getCreatedBy());
			cyclicAppointmentMapper.setCreatedDate(TimeZoneComponent.getDateByzone(slotDto.getUnitId()));
			cyclicAppointmentMapper.setUpdatedBy(slotDto.getUpdatedBy());
			cyclicAppointmentMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(slotDto.getUnitId()));
			cyclicAppointmentMapper.setAvailabelQuantity(1);
			if(slotDto.getOrderId()!=null && slotDto.getParentPackageId() == null) {
				cyclicAppointmentMapper.setOrderId(slotDto.getOrderId());
			}else {
				OrderDetailsMasterDto orderDetailsMasterDto=(OrderDetailsMasterDto)sessionFactory.getCurrentSession()
						.createQuery("select ord.orderDetailsId as orderDetailsId,ord.orderId as orderId From OrderDetailsMaster ord "
								+ " where ord.orderDetailsId=:orderDetailId")
						.setParameter("orderDetailId", slotDto.getOrderDetailsId())
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
				cyclicAppointmentMapper.setOrderId(orderDetailsMasterDto.getOrderId());
			}
			
			//made changes for sheduled worklist
			//cyclicAppointmentMapper.setStatus('A');
			cyclicAppointmentMapper.setStatus('I');
			sessionFactory.getCurrentSession().save(cyclicAppointmentMapper);
		} catch (HibernateException he) {
		logger.error("createCyclicAppointmentLog failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response rescheduleCyclicServiceAppointment(SlotMasterDto slotMasterDto) {
	try {
		
		Date appDate=(GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "dd-M-yyyy"));
		
		//update appointment master
		StringBuilder appointmentQuery=new StringBuilder("update AppointmentMaster am set " 
				+ "am.updatedDate=:updatedDate,"
				+ " am.appointmentDate=:appointmentDate ");
		
		LocalTime appTime = LocalTime.parse( slotMasterDto.getAppointmentTime());
		
		//update cyclic appointment mapper
		StringBuilder cyclicAppointmentQuery=new StringBuilder("update CyclicAppointmentMapper cam set " 
				+ "cam.updatedDate=:updatedDate,"
				+ " cam.appointmentTime='"+appTime+"' ");
		
		
		CyclicAppointmentMapper appointmentMapper=(CyclicAppointmentMapper)sessionFactory.getCurrentSession()
				.createQuery("select cam.orderDetailId as orderDetailId FROM CyclicAppointmentMapper cam "
						+ " where cam.appointmentId=:appId")
				.setParameter("appId", slotMasterDto.getAppointmentId())
				.setResultTransformer(Transformers.aliasToBean(CyclicAppointmentMapper.class)).uniqueResult();
		
		//get order detail id 
		OrderDetailsMasterDto orderDetailsMasterDto=(OrderDetailsMasterDto)sessionFactory.getCurrentSession()
				.createQuery("select ord.orderDetailsId as orderDetailsId,ord.orderId as orderId From OrderDetailsMaster ord "
						+ " where ord.orderDetailsId=:orderDetailId")
				.setParameter("orderDetailId", appointmentMapper.getOrderDetailId())
				.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
		
		  StringBuilder orderDetailUpdateQuery=new StringBuilder("update OrderDetailsMaster ord set ord.orderDate=:orderDate,"
		  		+ " ord.orderSchDate=:orderDate");
		 
		  if(slotMasterDto.getUpdatedBy()!=null) {
			  orderDetailUpdateQuery.append(",ord.updatedBy="+slotMasterDto.getUpdatedBy());
		  }
		  
		  StringBuilder orderMasterQuery=new StringBuilder("update OrderMaster orm set orm.orderDate=:orderDate ");
		  StringBuilder changeOrderStatusQuery =new StringBuilder("update OrderMaster orm set orm.orderStatusId = 2 where orm.orderStatusId = 3 ");
		  
		  //if update from scheduled worklist
		  if(slotMasterDto.getDoctorId()!=null && slotMasterDto.getSpecialityId()!=null) {
			  orderDetailUpdateQuery.append(",ord.ordDocId="+slotMasterDto.getDoctorId()+",ord.ordDocSplId="+slotMasterDto.getSpecialityId()+""
			  		+ ",ord.status='A',ord.isOrderVisibleDepartment='A' ");
			  cyclicAppointmentQuery.append(",cam.status='A' ");
			  appointmentQuery.append(",am.appointmentStatusId=6 ");
		  }else {
				  orderDetailUpdateQuery.append(",ord.status='A',ord.isOrderVisibleDepartment='A' ");
				  appointmentQuery.append(",am.appointmentStatusId=6 ");
				  if(slotMasterDto.getIsDoctorShareReq()!=null && slotMasterDto.getIsDoctorShareReq().equals('N')) {
					  cyclicAppointmentQuery.append(",cam.status='A' ");
				  }
				 
		  }
		  cyclicAppointmentQuery.append( " where cam.appointmentId=:appointmentId ");
		  //update cyclic appointment mapper
			 sessionFactory.getCurrentSession()
				.createQuery(cyclicAppointmentQuery.toString())
				.setParameter("updatedDate",TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()))
				.setParameter("appointmentId", slotMasterDto.getAppointmentId())
			    .executeUpdate();	
			 
	      //order detail update
	       orderDetailUpdateQuery.append(" where ord.orderDetailsId=:orderDetailId ");
			sessionFactory.getCurrentSession().createQuery(orderDetailUpdateQuery.toString())
	        .setParameter("orderDetailId", orderDetailsMasterDto.getOrderDetailsId())
	        .setParameter("orderDate", appDate).executeUpdate();
	
		//order master update
		orderMasterQuery.append(" where orm.orderId=:orderId ");
		sessionFactory.getCurrentSession().createQuery(orderMasterQuery.toString())
		       .setParameter("orderId", orderDetailsMasterDto.getOrderId())
		       .setParameter("orderDate",appDate).executeUpdate();
		
		//for reopen order
		changeOrderStatusQuery.append(" and orm.orderId=:orderId ");
		sessionFactory.getCurrentSession().createQuery(changeOrderStatusQuery.toString())
	       .setParameter("orderId", orderDetailsMasterDto.getOrderId())
	       .executeUpdate();
		
		//appointment update
		appointmentQuery.append(" where am.appointmentId=:appointmentId "); 
		sessionFactory.getCurrentSession()
		.createQuery(appointmentQuery.toString())
		.setParameter("updatedDate",TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()))
		.setParameter("appointmentDate",appDate)
		.setParameter("appointmentId", slotMasterDto.getAppointmentId())
	    .executeUpdate();
	   return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
	 } catch (HibernateException he) {
	 logger.error("rescheduleCyclicServiceAppointment failed", he);
	 }
	 return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response updateCyclicAppointment(EncounterMasterDto encounterMasterDto) {
		try {
			
			  encounterMasterDto.setUnitTokenNumber(encounterMasterDto.getUnitTokenNumber() != null ? encounterMasterDto.getUnitTokenNumber(): getTokenNumber(
					  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd"),encounterMasterDto.getUnitId()));
					sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster am set " 
							+ "am.updatedDate=:updatedDate,"
							+ "am.tokenNumber=:tokenNo,"
							+ "am.dummyTokenNumber=:tokenNo,"
							+ "am.arrivalTime=:arrivalTime,"
							+ "am.appointmentStatusId=5 "
							+ " where am.appointmentId=:appointmentId ")
					.setParameter("updatedDate",TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()))
					.setParameter("arrivalTime",TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()))
					.setParameter("tokenNo", encounterMasterDto.getUnitTokenNumber())
					.setParameter("appointmentId", encounterMasterDto.getAppointmentId())
					//.setParameter("slotId", encounterMasterDto.getAppointmentObject().getSlotId())
				    .executeUpdate();
			
			/*sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
					+ "sm.slotStatusId=:slotStatusId, "
					+ "sm.updatedBy=:updatedBy , "
					+ "sm.updatedDate=:updatedDate "
					+ "where sm.slotId=:slotId ")
				.setParameter("updatedBy",encounterMasterDto.getAppointmentObject().getUpdatedBy())
				.setParameter("updatedDate",TimeZoneComponent.getDateByzone(unitId))
				.setParameter("slotId", encounterMasterDto.getAppointmentObject().getSlotId())
				.setParameter("slotStatusId", encounterMasterDto.getAppointmentObject().getSlotStatusId())
			    .executeUpdate();*/
			return new Response(SUCCESS, null, null, null, encounterMasterDto);
	 } catch (HibernateException he) {
	 logger.error("updateCyclicAppointment failed", he);
	 }
	 return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response getScheduledServiceAppointmentList(SlotMasterDto slotMasterDto) {
		try {
			StringBuilder stringQuery = new StringBuilder(sessionFactory
					.getCurrentSession()
					.getNamedQuery("GET_SCHEDULED_CYCLIC_APPOINTMENTS_WORKLIST")
					.getQueryString());

			stringQuery.append(" WHERE am.organization_id = :orgId "
					+ " AND am.unit_id = :unitId "
					+ " AND ((ordM.visit_type_id = 1 AND tbd.status = 'A' AND appConfig.is_pre_or_post_billing = 'Y') "
					+ " OR (ordM.visit_type_id = 1 AND appConfig.is_pre_or_post_billing = 'N') "
					+ " OR (ordM.visit_type_id != 1)) "
					+ " AND am.status = 'A' "
					+ " AND cam.status = 'I' "
					+ " AND ord.status = 'A' "
			);

			if (slotMasterDto.getPatientId() != null) {
				stringQuery.append(" AND am.patient_id = :patientId ");
			}

			if (slotMasterDto.getFromDate() != null && slotMasterDto.getToDate() != null) {
				stringQuery.append(" AND to_char(am.appointment_date, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			}

			if (slotMasterDto.getServiceId() != null) {
				stringQuery.append(" AND am.service_id = :serviceId ");
			}

			stringQuery.append(" AND cam.available_quantity > 0 ");
			stringQuery.append(" ORDER BY am.appointment_date ");

			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			query.setParameter("orgId", slotMasterDto.getOrganizationId());
			query.setParameter("unitId", slotMasterDto.getUnitId());

			if (slotMasterDto.getPatientId() != null) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null && slotMasterDto.getToDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate());
				query.setParameter("toDate", slotMasterDto.getToDate());
			}

			if (slotMasterDto.getServiceId() != null) {
				query.setParameter("serviceId", slotMasterDto.getServiceId());
			}
			List<SlotMasterDto> slotMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
					.list();
			Integer count = slotMasterDtosList.size();

			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
		
		} catch (HibernateException he) {
		logger.error("getScheduledServiceAppointmentList failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response updateOrderDetailForServiceAppointment(SlotMasterDto slotDto) {

		try {
			String splitAppDateTime[]=slotDto.getAppointmentDate().split(" ");
			String splitAppDate[]=splitAppDateTime[0].split("-");
			String formatedAppDate=splitAppDate[2]+"-"+splitAppDate[1]+"-"+splitAppDate[0];
			Date appDate=(GlobalCommonDateUtils.getDate(formatedAppDate, "dd-M-yyyy"));
			
			//get order detail id 
			OrderDetailsMasterDto orderDetailsMasterDto=(OrderDetailsMasterDto)sessionFactory.getCurrentSession()
					.createQuery("select ord.orderDetailsId as orderDetailsId,ord.orderId as orderId From OrderDetailsMaster ord "
							+ " where ord.orderDetailsId=:orderDetailId")
					.setParameter("orderDetailId", slotDto.getOrderDetailsId())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).uniqueResult();
			
			  StringBuilder orderDetailUpdateQuery=new StringBuilder("update OrderDetailsMaster ord set ord.orderDate=:orderDate,"
			  		+ " ord.orderSchDate=:orderDate,ord.status='A' ");
			 

			      //order detail update
			       orderDetailUpdateQuery.append(" where ord.orderDetailsId=:orderDetailId ");
					sessionFactory.getCurrentSession().createQuery(orderDetailUpdateQuery.toString())
			        .setParameter("orderDetailId", orderDetailsMasterDto.getOrderDetailsId())
			        .setTimestamp("orderDate", appDate).executeUpdate();
			

			return new Response(SUCCESS, null, null, null, null);
			
		 } catch (HibernateException he) {
		 logger.error("updateOrderDetailForServiceAppointment failed", he);
		 } catch (Exception e) {
		 logger.error("updateOrderDetailForServiceAppointment failed", e);
		 }
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response getDeptWiseServiceListForCyclicOrder(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> cyclicDeptWiseServiceList;
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_DEPT_WISE_SERVICE_LIST_FOR_CYCLIC_ORDER").getQueryString();
			
			StringBuffer subQuery=new StringBuffer();
			
			cyclicDeptWiseServiceList=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("specialityId", slotMasterDto.getSpecialityId())
					.setParameter("organizationId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
			.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, cyclicDeptWiseServiceList, null);
	}catch(HibernateException he) { 
		throw new HibernateException(he.getMessage());
	}catch(Exception e) {
	logger.error("getDeptWiseServiceListForCyclicOrder failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}
	
	
	public Response getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(Integer patientId, Integer oranizationId) {
		try{
			StringBuilder appListQuery=new StringBuilder(new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_DETAILS_BY_ID_FOR_BILLING_SEARCH").getQueryString()));
					appListQuery.append(" AND am.is_cyclic_appointment='Y' ");
					List<PatientSearchBillRes> listPatientSearchBillRes = sessionFactory.getCurrentSession()
							.createSQLQuery(appListQuery.toString()).setParameter("patientId", patientId)
							.setResultTransformer(Transformers.aliasToBean(PatientSearchBillRes.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientSearchBillRes, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}



	@Override
	public Integer getAppointmentId(Integer encounterId) {
		try{
			
			String query = "select "
					+" appointment_id "
					+" from "
					+" opd.m_appointment_master mam "
					+" where "
					+" status = 'A' "
					+" and appointment_id = ( "
					+" select "
					+" 	appointment_id "
					+" from "
					+" 	opd.t_encounter_master "
					+" where "
					+" 	encounter_id = :encounterId)";
			
			Integer appointmentId = (Integer) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("encounterId", encounterId)
					.uniqueResult();
			
			return appointmentId;
		}catch(HibernateException e){
		logger.error("getAppointmentId failed", e);
		}
		return 0;
	}



	@Override
	public Character getNursingAssessmentRequired(Integer encounterId) {
			try{
						
						String query = " select "
								+ " spl.is_nursing_assessment_required "
								+ " from "
								+ " 	opd.t_encounter_master em "
								+ " inner join t_unit_speciality_mapper spl on "
								+ " 	spl.speciality_id = em.dept_id "
								+ " 	and spl.unit_id = em.unit_id "
								+ " where "
								+ " 	em.status = 'A' "
								+ " 	and em.encounter_id = :encounterId";
						
						Character nursingAssessmentRequired = (Character) sessionFactory.getCurrentSession()
								.createSQLQuery(query)
								.setParameter("encounterId", encounterId)
								.uniqueResult();
						
						return nursingAssessmentRequired!= null ? nursingAssessmentRequired : 'N';
					}catch(HibernateException e){
					logger.error("getNursingAssessmentRequired failed", e);
					}
					return 'N';
	}



	@Override
	public Integer getEHCFreeSlot(SlotMasterDto slotMasterDto) {
		try{
			
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
			stringQuery.append(" AND sm.appointment_date=:appointmentDate "
					//+ " AND sm.slot_status_id=1 " 
					+ " ORDER BY sm.slot_id");
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			SlotMasterDto dto = new SlotMasterDto();
			List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("appointmentDate",	GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			
			
			List<SlotMasterDto> masterDtosList = new LinkedList<>();
			ListIterator<SlotMasterDto> iterator = slotMasterDtosList.listIterator(); //main list
			while(iterator.hasNext())
			{
				SlotMasterDto iteratorObj = iterator.next();
				
				if(iteratorObj.getAppointmentId()!=null)
				{
					
				}else{
					masterDtosList.add(iteratorObj);
				}
			}
			
			if(masterDtosList.size()>0)
			{
				 dto = masterDtosList.get(0);
				 return dto.getSlotId();
			}else{
				return 0;
			}
			
		}catch(Exception e){
		logger.error("getEHCFreeSlot failed", e);
		}
		return 0;
	}



	@Override
	public Response getServiceListForCyclicOrder(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> cyclicDeptWiseServiceList;
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICE_LIST_FOR_CYCLIC_ORDER").getQueryString();
			
			StringBuffer subQuery=new StringBuffer();
			
			cyclicDeptWiseServiceList=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
			.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, cyclicDeptWiseServiceList, null);
	}catch(HibernateException he) { 
		throw new HibernateException(he.getMessage());
	}catch(Exception e) {
	logger.error("getServiceListForCyclicOrder failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}



	@Override
	public Response rescheduleAppointmentForMylife(Integer oldAppointmentId,Integer newAppointmentId) {
		try{

			sessionFactory.getCurrentSession()
					.createQuery("update EncounterMaster am set "
							+ " am.statusId = 1, "
							+ " am.unitTokenNumber = null, "
							+ " am.dummyTokenNumber = null, "
							+ " am.docQueueNumber = null, "
							+ " am.updatedDate = :updatedDate, "
							+ " am.appointmentId = :newAppointmentId "
							+ " where am.appointmentId = :oldAppointmentId")
					.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
					.setParameter("newAppointmentId", newAppointmentId)
					.setParameter("oldAppointmentId", oldAppointmentId)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,newAppointmentId);
		}catch(Exception e){
		logger.error("rescheduleAppointmentForMylife failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public boolean isPatientSync(Integer patientId) 
	{
		try{
			boolean result = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select uuid is not null from patient.t_patient_registration tpr where patient_id =:patientId ").setParameter("patientId",patientId).uniqueResult();
			return result;
		}catch(Exception e){
		logger.error("isPatientSync failed", e);
			return true;
		}
	}



	@Override
	public boolean isMyLifeAppointment(Integer appointmentId) {
		boolean result = (boolean)sessionFactory.getCurrentSession().createSQLQuery("select is_appointment_from_my_life='Y' from opd.m_appointment_master mam where appointment_id =:appointmentId ").setParameter("appointmentId",appointmentId).uniqueResult();
		return result;
	}



	@Override
	public Response updateMultiLingCoverPath(String coversheetMlPath, Integer appointmentId) {
		try{
			sessionFactory.getCurrentSession()
				.createQuery("update AppointmentMaster am set " 
						+ " am.coversheetMlFilePath =:filePath "
						+ " where am.appointmentId=:appointmentId ")
				.setParameter("filePath",coversheetMlPath).setParameter("appointmentId",appointmentId)
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,null);
		}catch(Exception e){
		logger.error("updateMultiLingCoverPath failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateAppointmentPaidStatus(Integer appointmentId) {
		try{
			Integer count=sessionFactory.getCurrentSession().createQuery("update AppointmentMaster am set " 
						+ " am.isPaidAppointment ='Y' "
						+ " where am.appointmentId=:appointmentId ")
					.setParameter("appointmentId",appointmentId)
			.executeUpdate();
			if(count>0) {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,null);
			}else {
				return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}
			
		}catch(Exception e){
		logger.error("updateAppointmentPaidStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getAppointMentDetailsByPatientId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			String pattern = "yyyy-MM-dd";
			String formattedDate =new SimpleDateFormat(pattern).format(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"yyyy-MM-dd"));
			
			String query = "select " 
					+ " app.appointment_status_id as  \"appointmentStatusId\","
					+ " app.appointment_id as  \"appointmentId\" "
					+ " from opd.m_appointment_master app "
					+ " where "
					+ "	app.appointment_status_id != 6 "
					+ "	and app.patient_id =  :patientId "
					+ "	and to_char(app.appointment_date, 'yyyy-MM-dd')=:formattedDate "
					+ " order by "
					+ "	app.appointment_id asc "
					+ " limit 1 ";
			SlotMasterDto dto = (SlotMasterDto)sessionFactory
					.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", slotMasterDto.getPatientId())
					.setParameter("formattedDate",formattedDate)
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,dto);
		}catch(Exception e){
		logger.error("getAppointMentDetailsByPatientId failed", e);
			return null;
		}
	}



	@Override
	public Response prnConfiguration(SlotMasterDto slotMasterDto) {
		String query = "select app.lable_name as \"labelName\",app.prn_file as \"prnFormat\"" 
				+ " from public.t_prn_configuration app "
				+ " where "
				+ "	app.type_id= 3 "
				+ "	and app.organization_id =:orgId   "
				+ "	and app.unit_id= :unitId ";
		
		
		List<SlotMasterDto> dto = (List<SlotMasterDto>)sessionFactory
				.getCurrentSession().createSQLQuery(query)
				.setParameter("orgId", slotMasterDto.getOrganizationId())
				.setParameter("unitId", slotMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
		
		String query1 = " select mac.is_prn_required as \"isPrnRequired\" from public.m_application_configuration mac where mac.unit_id="+slotMasterDto.getUnitId()+"";
		
		List<SlotMasterDto> dto1 = sessionFactory
				.getCurrentSession().createSQLQuery(query1).list();
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, dto1,dto);
	}



	@Override
	public Response getTokenNo(Integer appointmentId) throws ApplicationException {
		try{
			String query = "select token_number as \"tokenNumber\" from opd.m_appointment_master mam  where appointment_id  = :appointmentId ";
			AppointmentMaster dto = (AppointmentMaster)sessionFactory
					.getCurrentSession().createSQLQuery(query)
					.setParameter("appointmentId", appointmentId)
					.setResultTransformer(Transformers.aliasToBean(AppointmentMaster.class)).uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,dto);
		}catch(Exception e){
		logger.error("getTokenNo failed", e);
			return null;
		}
	}



	@Override
	public Response getAppointmentUniqueId(SlotMasterDto slotMasterDtoss) throws ApplicationException {
		try{

			String query = "select  am.appointment_unique_id as \"appointmentUniqueId\"  from opd.m_appointment_master  am where am.appointment_id= :appointmentId";
			SlotMasterDto dto = (SlotMasterDto)sessionFactory
					.getCurrentSession().createSQLQuery(query)
					.setParameter("appointmentId", slotMasterDtoss.getAppointmentId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,dto);
		}catch(Exception e){
		logger.error("getAppointmentUniqueId failed", e);
		}
		return null;
	}



	@Override
	public Response searchInProcessAppointmentWeb(Integer appointmentId) throws ApplicationException {
		try{
			
			List<SlotMasterDto> slotMasterDtosList2 = sessionFactory.getCurrentSession().createSQLQuery(
					"select en.appointment_id from opd.t_encounter_master en where en.appointment_id =:appointmentId " ).setParameter("appointmentId",appointmentId)
					.list();
			if(slotMasterDtosList2.size()==0)
			{
				StringBuilder str = new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString().toString());
				str.append(" WHERE am.appointment_id =:appointmentId ");
				str.append(" and am.appointment_status_id != 2 ");

				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createSQLQuery(str.toString())
						.setParameter("appointmentId",appointmentId)
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				if(slotMasterDtosList.size() != 0) {
				return new Response(SUCCESS, null, "Patient already has an appointment which is in process !!",
						slotMasterDtosList, null);
				}
			}
		}catch(HibernateException e){
		logger.error("searchInProcessAppointmentWeb failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}



	@Override
	public Response searchFutureAppointmentsByPatientId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			StringBuilder stringQuery = null;
			if(slotMasterDto.getIsAppointmentRequired() != null && slotMasterDto.getIsAppointmentRequired() == 'N') {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST_FOR_IS_APPOINTMENT_FALSE").getQueryString().toString());
			}else {
				 stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST").getQueryString().toString());
			}
			

			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)	
			{
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						//+ "AND am.appointment_type_id=:appointmentTypeId "
						+ "AND am.appointment_type_id IN (1,6,9) "
						);
				
				if (slotMasterDto.getPatientId() > 0)
					stringQuery.append(" AND am.patient_id =:patientId " );

				if (slotMasterDto.getFromDate() != null)
				{
					stringQuery.append(" AND to_char(am.appointment_date,'YYYY-MM-DD')>=:fromDate ");
				}
				SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery((stringQuery).toString());
				
	            if(slotMasterDto.getOffset()!=null && slotMasterDto.getNoOfRecordsPerPage()!=null)
	            {
	            	query.setFirstResult(slotMasterDto.getOffset());
	            	query.setMaxResults(slotMasterDto.getNoOfRecordsPerPage());
	            }
				if (slotMasterDto.getPatientId() > 0)
					query.setParameter("patientId",slotMasterDto.getPatientId());
				if (slotMasterDto.getFromDate() != null)
				{
					query.setParameter("fromDate",slotMasterDto.getFromDate());
				}


				List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				Integer count=slotMasterDtosList.size();
				return new Response(SUCCESS, null, null, slotMasterDtosList, count);
				
			}else {
				stringQuery.append(" WHERE am.organization_id=:orgId "
						+ "AND am.unit_id=:unitId "
						+ "AND am.appointment_type_id=:appointmentTypeId ");
			}
		
			if (slotMasterDto.getPatientId() > 0)
				stringQuery.append(" AND am.patient_id =:patientId " );

			if (slotMasterDto.getFromDate() != null)
			{
				stringQuery.append(" AND to_char(am.appointment_date,'YYYY-MM-DD')>=:fromDate ");
			}
			SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery((stringQuery).toString());

			if (slotMasterDto.getPatientId() > 0)
				query.setParameter("patientId",slotMasterDto.getPatientId());

			if (slotMasterDto.getFromDate() != null)
			{
				query.setParameter("fromDate",slotMasterDto.getFromDate());
			}

			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)
			{
			}else{
				query.setParameter("appointmentTypeId",slotMasterDto.getAppointmentTypeId());
			}
			List<SlotMasterDto> slotMasterDtosList = query
				.setParameter("orgId", slotMasterDto.getOrganizationId())
				.setParameter("unitId", slotMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			Integer count=slotMasterDtosList.size();
			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
			
		} catch (HibernateException he) {
		logger.error("searchFutureAppointmentsByPatientId failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response updateIsAppointmentFromMyLife(Integer patientId, Integer appointmentId) throws ApplicationException {
		try {
			Integer count=sessionFactory.getCurrentSession().createQuery("update AppointmentMaster am set "
					+ " am.isAppointmentFromMyLife ='Y' "
					+ " where am.appointmentId=:appointmentId "
					+ " and am.patientId=:patientId ")
					.setParameter("appointmentId",appointmentId)
					.setParameter("patientId",patientId)
			.executeUpdate();

			if(count>0) {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,null);
			}else {
				return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}
		}
		catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response addNationalityMaster(ScheduleAppointmentReqDto reqDto) throws ApplicationException {
		try {
			String nationalityCode = reqDto.getNationality().substring(0,2).toUpperCase();
			String sql ="INSERT INTO public.m_nationality_master "
					+ "(nationality_code, nationality_name, created_by, created_date, status, updated_by, updated_date, organization_id, country_id) "
					+ " VALUES('"+nationalityCode+"', '"+reqDto.getNationality()+"', 1, now(), 'A', 1, now(), "+reqDto.getHospitalId()+", "+reqDto.getCountryId()+");";
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.executeUpdate();

			if(count>0) {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,null);
			}else {
				return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}
		}
		catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Integer getNationalityIdByNationalityName(ScheduleAppointmentReqDto reqDto) throws ApplicationException {
		try {
			String nationality = reqDto.getNationality().toLowerCase();
			Integer nationalityId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
							"select nationality_id from m_nationality_master where lower(nationality_name) = '" +nationality+ "' "
									+  " and organization_id =:organizationId "
									+  " and status = 'A' order by nationality_id desc limit 1")
					.setParameter("organizationId",reqDto.getHospitalId())
									.uniqueResult();

			if(nationalityId != null && nationalityId > 0) {
				return nationalityId;
			}else {
				return null;
			}
		}
		catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
