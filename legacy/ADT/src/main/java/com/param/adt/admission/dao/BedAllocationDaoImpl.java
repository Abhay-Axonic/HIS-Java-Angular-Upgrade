package com.param.adt.admission.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.BedAllocationDto;
import com.param.adt.admission.dto.NursingPatientAcceptanceDto;
import com.param.adt.admission.model.Admission;
import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.admission.model.AdmissionPatientMapper;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedCategoryMasterDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.global.dto.BedStatusMasterDto;
import com.param.adt.master.global.model.BedCategoryWaitingList;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.Transfer;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.PatientSponsorDetails;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BedAllocationDaoImpl extends GenericDao<AdmissionDetails, Integer>
		implements IBedAllocationDao, ICommonConstants {

	public BedAllocationDaoImpl() {
		super(AdmissionDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getActiveBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			/*List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BED_LIST").setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId())

					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();*/
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_BED_LIST").getQueryString());
			query.append(bedMasterDto.getBillingBedCategoryId() != null ? " and bed.billingBedCategoryId ="+bedMasterDto.getBillingBedCategoryId() : " ");
			query.append(bedMasterDto.getBedCategoryId() != null ? " and bed.bedCategoryId ="+bedMasterDto.getBedCategoryId() : " ");
			query.append(bedMasterDto.getIsDayCare() != null ? " and ward.isDayCare ='"+bedMasterDto.getIsDayCare()+"' " : " ");
			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession().createQuery(query.toString())
			.setParameter("unitId",bedMasterDto.getUnitId())
			.setParameter("organizationId", bedMasterDto.getOrganizationId())
			.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
		
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveFloorMasterList(BedAllocationDto bedAllocationDto) throws ApplicationException {
		try {
			List<BedAllocationDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_FLOOR_LIST")
					.setParameter("organizationId", bedAllocationDto.getOrganizationId())
					.setParameter("unitId", bedAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardListByFloorId(BedAllocationDto bedAllocationDto) throws ApplicationException {
		try {
			List<BedAllocationDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_LIST_BY_FLOOR_ID")
					.setParameter("organizationId", bedAllocationDto.getOrganizationId())
					.setParameter("unitId", bedAllocationDto.getUnitId())
					.setParameter("floorId", bedAllocationDto.getFloorId())
					.setParameter("genderId", bedAllocationDto.getGenderId())
					.setResultTransformer(Transformers.aliasToBean(BedAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationListByWard(BedAllocationDto bedAllocationDto) throws ApplicationException {
		try {
			List<BedAllocationDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_BY_WARD")
					.setParameter("organizationId", bedAllocationDto.getOrganizationId())
					.setParameter("unitId", bedAllocationDto.getUnitId())
					.setParameter("wardId", bedAllocationDto.getWardId())
					.setResultTransformer(Transformers.aliasToBean(BedAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedCategoryList(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST_BY_ORG_UNIT")
					.setParameter("organizationId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			
			//get billing bed category by visitType
			       List<Integer> bedCategoryList = sessionFactory.getCurrentSession()
					.createQuery("SELECT billingBedCategoryId  From BillingBedVisitTypeMapper bbvt where bbvt.visitTypeId=3 ").list();
			       bedCategoryMasterDto.setBedCategoryList(bedCategoryList);
			return new Response(SUCCESS, null, null, bedAllocationDtosList, bedCategoryMasterDto);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedStatusList() throws ApplicationException {
		try {
			List<BedStatusMasterDto> bedStatusMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_STATUS_LIST")
					.setResultTransformer(Transformers.aliasToBean(BedStatusMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedStatusMasterDtos, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getMealPreference(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException {
		try {
			List<BedAllocationDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEAL_PREFERENCE")
					.setParameter("organizationId", mealPreferenceMasterDto.getOrganizationId())
					.setParameter("unitId", mealPreferenceMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MealPreferenceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientAdmission(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Admission admission = mapper.map(admissionDto, Admission.class, "AdmissionDto_to_Admission");
				admission.setIsFinancialCreance('N');
				admission.setStatus('A');
				admission.setIsFinalBill('N');
				admission.setIsAdmissionCancelled('N');
				admission.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
				admission.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
				admission.setDeliveryFlag(0);
			Integer admissionId = (Integer) sessionFactory.getCurrentSession().save(admission);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, admissionId);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Override
	public Response savePatientDetails(AdmissionDto admissionDto) throws ApplicationException {

		try {

			// <-----Adding data in patient details
	/*		AdmissionDetails admissionDetails = mapper.map(admissionDto, AdmissionDetails.class,"AdmissionDto_to_AdmissionDetails");
				admissionDetails.setStatus('A');
				admissionDetails.setIsShortLeave('N');
				admissionDetails.setIsDischarge('N');
				admissionDetails.setIsCancel('N');
				admissionDetails.setIsClose('N');
				admissionDetails.setIsHighRisk('N');
				admissionDetails.setIsCritical('N');
				admissionDetails.setIsMedicoLegal('N');
				admissionDetails.setIsTransferOfCare('N');
				admissionDetails.setNursingAcceptance(admissionDto.getSkipAdmissionAcceptance() == 'N'?'P':'Y');
				admissionDetails.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
				admissionDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
			save(admissionDetails);*/
			// ----->

			AdmissionDetails admissionDetails= new AdmissionDetails();
			admissionDetails.setAdmissionId(admissionDto.getAdmissionId());
			admissionDetails.setAdmissionTypeId(admissionDto.getAdmissionTypeId());
			admissionDetails.setFloorId(admissionDto.getFloorId());
			admissionDetails.setWardId(admissionDto.getWardId());
			admissionDetails.setRoomId(admissionDto.getRoomId());
			admissionDetails.setBedId(admissionDto.getBedId());
			admissionDetails.setCancelBy(admissionDto.getCancelBy());
			//admissionDetails.setCancelDate(GlobalCommonDateUtils.getDate(admissionDto.getCancelDate(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setRemark(admissionDto.getRemark());
			admissionDetails.setDoa(GlobalCommonDateUtils.getDate(admissionDto.getDoa(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setPdd(GlobalCommonDateUtils.getDate(admissionDto.getPdd(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setMealPrefId(admissionDto.getMealPrefId());
			admissionDetails.setAttendentMealPrefId(admissionDto.getAttendentMealPrefId());
			admissionDetails.setBedCategoryId(admissionDto.getBedCategoryId());
			admissionDetails.setBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
			admissionDetails.setTriageCategoryId(admissionDto.getTriageCategoryId());
			admissionDetails.setErBedTypeAllocation(admissionDto.getErBedTypeAllocation());
		//	admissionDetails.setFutureDischargeDate(GlobalCommonDateUtils.getDate(admissionDto.getFutureDischargeDate(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setExtendReasonId(admissionDto.getExtendReasonId());
			admissionDetails.setIsMedicoLegal(admissionDto.getIsMedicoLegal());
			admissionDetails.setIsTransferOfCare(admissionDto.getIsTransferOfCare());
			admissionDetails.setIsShortLeave('N');
			admissionDetails.setIsDischarge('N');
			//admissionDetails.setNursingAcceptanceDateTime(GlobalCommonDateUtils.getDate(admissionDto., "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setNursingRejectionNote(admissionDto.getNursingRejectionNote());
		//	admissionDetails.setNursingRejectionReasonId(admissionDto.getnurs);
			admissionDetails.setIsCancel('N');
			admissionDetails.setIsClose('N');
			admissionDetails.setIsHighRisk('N');
			admissionDetails.setIsCritical('N');
			admissionDetails.setIsMedicoLegal('N');
			admissionDetails.setIsTransferOfCare('N');
			admissionDetails.setNursingAcceptance(admissionDto.getSkipAdmissionAcceptance() == 'N'?'P':'Y');
			admissionDetails.setOrganizationId(admissionDto.getOrganizationId());
			admissionDetails.setUnitId(admissionDto.getUnitId());
			admissionDetails.setStatus('A');
			admissionDetails.setCreatedBy(admissionDto.getCreatedBy());
			admissionDetails.setCreatedDate(GlobalCommonDateUtils.getDate(admissionDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setUpdatedBy(admissionDto.getUpdatedBy());
			admissionDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(admissionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			admissionDetails.setAdmissionDateTime(GlobalCommonDateUtils.getDate(admissionDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

			save(admissionDetails);
			
			if(admissionDto.getAdmissionNoteId()!=null)
			{
				// <-----Updating admission note's admission status to B(Booked)
				// from P(Pending) or R(Reserved)
				Query qry = sessionFactory.getCurrentSession().createQuery(
						"update AdmissionNote note set " +
								"note.admissionStatus = :admissionStatus, " +
								"note.admissionId = :admissionId, " +
								"note.updatedBy = :updatedBy, " +
								"note.updatedDate = :updatedDate " +
								"where note.admissionNoteId = :admissionNoteId"
				);
				qry.setParameter("admissionStatus", 'B');
				qry.setParameter("admissionId", admissionDto.getAdmissionId());
				qry.setParameter("updatedBy", admissionDto.getUpdatedBy());
				qry.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				qry.setParameter("admissionNoteId", admissionDto.getAdmissionNoteId());
				qry.executeUpdate();


				// ----->
	
				// <-----Updating status of last admission patient mapper record to
				// I(inactive)
				Query qry1 = sessionFactory.getCurrentSession().createQuery(
						"update AdmissionPatientMapper m set " +
								"m.status = :status, " +
								"m.updatedBy = :updatedBy, " +
								"m.updatedDate = :updatedDate " +
								"where m.admissionNoteId = :admissionNoteId " +
								"and m.status = :currentStatus"
				);
				qry1.setParameter("status", 'I');
				qry1.setParameter("updatedBy", admissionDto.getUpdatedBy());
				qry1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				qry1.setParameter("admissionNoteId", admissionDto.getAdmissionNoteId());
				qry1.setParameter("currentStatus", 'A');
				qry1.executeUpdate();

				// ----->
	
				// <-----Adding new entry in admission patient mapper where active
				// status will be B(Booked) from P(Pending)
				AdmissionPatientMapper admissionPatientMapper = mapper.map(admissionDto, AdmissionPatientMapper.class,
						"AdmissionDto_to_AdmissionPatientMapper");
				admissionPatientMapper.setActiveStatus('B');
				admissionPatientMapper.setStatus('A');
				admissionPatientMapper.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
				admissionPatientMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
				sessionFactory.getCurrentSession().save(admissionPatientMapper);
				// ----->
			}
			// <-----Changing status of bed to Booked (4 is the ID of
			// 'reserved'status)
			Query qry2 = sessionFactory.getCurrentSession().createQuery(
					"update BedMaster bed set " +
							"bed.bedStatusId = :bedStatusId, " +
							"bed.updatedBy = :updatedBy, " +
							"bed.updatedDate = :updatedDate " +
							"where bed.bedId = :bedId " +
							"and bed.organizationId = :organizationId " +
							"and bed.unitId = :unitId " +
							"and bed.status = :status"
			);
			qry2.setParameter("bedStatusId", admissionDto.getBedStatusId());
			qry2.setParameter("updatedBy", admissionDto.getUpdatedBy());
			qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			qry2.setParameter("bedId", admissionDto.getBedId());
			qry2.setParameter("organizationId", admissionDto.getOrganizationId());
			qry2.setParameter("unitId", admissionDto.getUnitId());
			qry2.setParameter("status", 'A');
			qry2.executeUpdate();


			// ----->

			// <-----Updating status of current bed to I in BedLogStatus
			Query qry3 = sessionFactory.getCurrentSession().createQuery(
					"update BedLogStatus bedStatus set " +
							"bedStatus.status = :status, " +
							"bedStatus.updatedBy = :updatedBy, " +
							"bedStatus.updatedDate = :updatedDate " +
							"where bedStatus.status = :currentStatus " +
							"and bedStatus.bedId = :bedId " +
							"and bedStatus.organizationId = :organizationId " +
							"and bedStatus.unitId = :unitId"
			);
			qry3.setParameter("status", 'I');
			qry3.setParameter("updatedBy", admissionDto.getUpdatedBy());
			qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			qry3.setParameter("currentStatus", 'A');
			qry3.setParameter("bedId", admissionDto.getBedId());
			qry3.setParameter("organizationId", admissionDto.getOrganizationId());
			qry3.setParameter("unitId", admissionDto.getUnitId());
			qry3.executeUpdate();

			// ----->

			// <-----Adding new entry in log as the status of bed is changing
			BedLogStatus bedLogStatus = mapper.map(admissionDto, BedLogStatus.class, "AdmissionDto_to_BedLogStatus");
			bedLogStatus.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() : 0));
			bedLogStatus.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() : 0));
				bedLogStatus.setStatus('A');
			sessionFactory.getCurrentSession().save(bedLogStatus);
			// ----->

			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		//	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		} catch (Exception e) {
			throw e;
			//return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


	}


	@Override
	public Response getBedListByMultipleCriteria(BedMasterDto bedMasterDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchBed(bedMasterDto);
			Query actualQuery = session.createQuery(resQuery.toString());

			Query sql = actualQuery
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setParameter("genderId", bedMasterDto.getGenderId())
					.setParameter("isVirtual", bedMasterDto.getIsVirtual());
			if (bedMasterDto.getFloorId() > 0){
			sql.setParameter("floorId", bedMasterDto.getFloorId());
			}
			if (bedMasterDto.getIsDayCare()!=null) {
				sql.setParameter("isDayCare", bedMasterDto.getIsDayCare());
			}
			if (bedMasterDto.getWardId() > 0) {
				sql.setParameter("wardId", bedMasterDto.getWardId());
			}
			if (bedMasterDto.getNursingStationId() > 0) {
				sql.setParameter("nursingStationId", bedMasterDto.getNursingStationId());
			}
			if (bedMasterDto.getBedCategoryId() > 0) {
				sql.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
			}
			if (bedMasterDto.getBedStatusId() > 0) {
				sql.setParameter("bedStatusId", bedMasterDto.getBedStatusId());
			}
			List<BedMasterDto> bedMasterDtosList= sql.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
			throw he ;

		} catch (Exception e) {
			throw e ;
		}
		//return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	private StringBuilder searchBed(BedMasterDto bedMasterDto) {

		StringBuilder strQuery = new StringBuilder("SELECT bed.bedId as bedId, "
				+ "bed.bedNumber as bedNumber, " 
				+ "bed.floorId as floorId, " 
				+ "bed.roomId as roomId, "
				+ "room.roomDesc as roomDesc, "
				+ "flr.floorName as floorName, "
				+ "bed.wardId as wardId, " 
				+ "ward.wardName as wardName, "
				+ "bed.bedCategoryId as bedCategoryId, "
				+ "bed.billingBedCategoryId as billingBedCategoryId, "
				+ "bbc.billingBedDesc as billingBedDesc, " 
				+ "bc.bedCategoryDesc as bedCategoryDesc, "
				+ "bedlog.bedStatusId as bedStatusId, "
				+ "bedStatus.bedStatusDesc as bedStatusDesc " 
				+ "FROM BedMaster bed "
				+ "INNER JOIN bed.listBedLogStatus bedlog "
				+ "INNER JOIN bedlog.bedStatusMaster bedStatus " 
				+ "INNER JOIN bed.floorMaster flr "
				+ "INNER JOIN bed.wardMaster ward " 
				+ "INNER JOIN bed.bedCategoryMaster bc "
				+ "INNER JOIN bed.billingBedCategoryMaster bbc " 
				+ "INNER JOIN bed.roomMaster room " 
				+ "WHERE bed.unitId=:unitId "
				+ "AND bed.organizationId=:organizationId " 
				+ "AND bedlog.status='A' " 
				+ "AND bed.status='A' "
				+ "AND bed.bedStatusId=3 "
				+ "AND bedlog.bedStatusId=3 "//BedStatusId 3---> Ready For Use
				+ "AND ward.isEr ='N' "
				+ "AND ward.islaborWard ='N' "
				+ "AND ward.ishemodialysisward ='N' "
			//	+ "AND ward.isIcu='N' "HIS-24001
				+ "AND bed.isVirtual=:isVirtual "
				+ "AND bed.wardId IN (select wardId FROM WardGenderMapper where genderId=:genderId and status='A')  " );
		
		if (bedMasterDto.getFloorId() > 0)
			strQuery.append(" AND bed.floorId=:floorId ");
		
		if (bedMasterDto.getIsDayCare()!=null)
			strQuery.append(" AND ward.isDayCare=:isDayCare ");

		if (bedMasterDto.getWardId() > 0)
			strQuery.append(" AND bed.wardId=:wardId ");

		if (bedMasterDto.getNursingStationId() > 0)
			strQuery.append(" AND nursing.nursingStationId=:nursingStationId ");

		if (bedMasterDto.getBedCategoryId() > 0)
			strQuery.append(" AND bed.bedCategoryId=:bedCategoryId ");

		if (bedMasterDto.getBedStatusId() > 0)
			strQuery.append(" AND bed.bedStatusId=:bedStatusId ");

		return strQuery;
	}

	@Override
	public Response saveTransfer(TransferDto transferDto) throws ApplicationException {
		try {
			Transfer transfer = mapper.map(transferDto, Transfer.class, "TransferDto_to_Transfer");
			transfer.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() :0));
			transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() :0));
			transfer.setIsPrimary('Y');
			transfer.setStatus('A');
			sessionFactory.getCurrentSession().save(transfer);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			throw new ApplicationException(he.getMessage(), he);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Override
	public Response getWatingListNumbers(AdmissionDto admissionDto) throws ApplicationException {
		try {
				List<AdmissionDto> updateWatingListNumbersList = sessionFactory.getCurrentSession()
						.getNamedQuery(
								"GET_WAITING_LIST_DATA_WHICH_ARE_GREATER_THAN_WAITING_LIST_NUMBER_OF_CURRENT_ADMISSION")
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("doa", ADTCommonDateUtils.getDate(admissionDto.getDoa(), "dd-M-yyyy HH:mm:ss"))
						.setParameter("bedCategoryId", admissionDto.getBedCategoryId())
						.setParameter("bedCategoryWaitingListId", admissionDto.getBedCategoryWaitingListId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				
				Session session=sessionFactory.getCurrentSession();
				
			Query qry = session.createQuery(
					"update BedCategoryWaitingList bcwl set " +
							"bcwl.status = :status, " +
							"bcwl.updatedBy = :updatedBy, " +
							"bcwl.updatedDate = :updatedDate " +
							"where bcwl.bedCategoryWaitingListId = :bedCategoryWaitingListId " +
							"and bcwl.status = :currentStatus"
			);
			qry.setParameter("status", 'I');
			qry.setParameter("updatedBy", admissionDto.getUpdatedBy());
			qry.setParameter("updatedDate", ADTCommonDateUtils.getDate(admissionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qry.setParameter("bedCategoryWaitingListId", admissionDto.getBedCategoryWaitingListId());
			qry.setParameter("currentStatus", 'A');
			qry.executeUpdate();



			return new Response(SUCCESS, null, null, updateWatingListNumbersList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response updateWatingListNumber(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Session session = sessionFactory.openSession();
			Query qry = session.createQuery(
					"update BedCategoryWaitingList bcwl set " +
							"bcwl.status = :status, " +
							"bcwl.updatedBy = :updatedBy, " +
							"bcwl.updatedDate = :updatedDate " +
							"where bcwl.admissionNoteId = :admissionNoteId " +
							"and bcwl.status = :currentStatus"
			);
			qry.setParameter("status", 'I');
			qry.setParameter("updatedBy", admissionDto.getUpdatedBy());
			qry.setParameter("updatedDate", ADTCommonDateUtils.getDate(admissionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			qry.setParameter("admissionNoteId", admissionDto.getAdmissionNoteId());
			qry.setParameter("currentStatus", 'A');
			qry.executeUpdate();



			Transaction tr = session.beginTransaction();
			BedCategoryWaitingList bedCategoryWaitingList = mapper.map(admissionDto, BedCategoryWaitingList.class,
					"AdmissionDto_to_BedCategoryWaitingList");
			bedCategoryWaitingList.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId():0));
			bedCategoryWaitingList.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId():0));
			bedCategoryWaitingList.setStatus('A');
			session.save(bedCategoryWaitingList);
			tr.commit();
			session.close();
			return new Response<AdmissionDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			return new Response<AdmissionDto, Integer>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		} catch (Exception e) {
			
			return new Response<AdmissionDto, Integer>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Boolean isPatientAlreadyAdmitted(int patientId, int orgId, int unitId) throws ApplicationException {
		try {
			List<BedAllocationDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.createQuery("SELECT patientId as patientId FROM Admission "
							+ "WHERE patientId=:patientId "
							+ "AND organizationId=:orgId "
							+ "AND unitId=:unitId "
							+ "AND status='A' " )
					.setParameter("patientId", patientId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BedAllocationDto.class)).list();
			
			if(bedAllocationDtosList.size()>0)
				return true;
			else
				return false;
			
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return false;
	}

	@Override
	public Response savePatientSponsorDetails(PatientSponsorDetailsDto patientSponsorDetailsDto)
			throws ApplicationException {
		try {
			PatientSponsorDetails patientSponsorDetails = mapper.map(patientSponsorDetailsDto,
					PatientSponsorDetails.class, "PatientSponsorDetailsDto_to_PatientSponsorDetails");
			patientSponsorDetails.setCreatedDate(TimeZoneComponent.getDateByzone(patientSponsorDetailsDto.getUnitId()));
			patientSponsorDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(patientSponsorDetailsDto.getUnitId()));

			sessionFactory.getCurrentSession().save(patientSponsorDetails);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientAcceptanceListForNursing(NursingPatientAcceptanceDto nursingPatientAcceptanceDto) throws ApplicationException {
		try {
			 String query=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_ACCEPTANCE_LIST_FOR_NURSING" ).getQueryString();
			 
			 String finalQuery;
			 List<AdmissionDto> bedAllocationDtosList=null;
			 //added for pagination on patient acceptance screen
			 if(nursingPatientAcceptanceDto.getIsFromNursingAcceptancePage()!=null && nursingPatientAcceptanceDto.getIsFromNursingAcceptancePage()=='Y') {
				 
				 query= query.concat(" AND admission.visit_type_id in (2,3) "
				 				+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
				 
				 if(nursingPatientAcceptanceDto.getIsShowPatientByBed().equals('N'))	{	
					 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
							 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
								.setParameterList("wardList", nursingPatientAcceptanceDto.getWardList())								
								.setFirstResult(nursingPatientAcceptanceDto.getOffset() != null ? nursingPatientAcceptanceDto.getOffset() : 0)
								.setMaxResults(nursingPatientAcceptanceDto.getNoOfRecordsPerPage() != null ? nursingPatientAcceptanceDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
				 }
				 else{
					 finalQuery=query.replace("adl.ward_id IN (:wardList)", "adl.bed_id in (:bedList)");
					 finalQuery= finalQuery.concat(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
					 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
							 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
							 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
								.setParameterList("bedList", nursingPatientAcceptanceDto.getBedList())
								.setFirstResult(nursingPatientAcceptanceDto.getOffset() != null ? nursingPatientAcceptanceDto.getOffset() : 0)
								.setMaxResults(nursingPatientAcceptanceDto.getNoOfRecordsPerPage() != null ? nursingPatientAcceptanceDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
				 }
				 
			 }else {
				 
				 if(nursingPatientAcceptanceDto.getIsShowPatientByBed().equals('N'))	
				 {	
					 query= query.concat(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
					 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
							 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
								.setParameterList("wardList", nursingPatientAcceptanceDto.getWardList())
								.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
				 }
				 else{
					 finalQuery=query.replace("adl.ward_id IN (:wardList)", "adl.bed_id in (:bedList)");
					 finalQuery= finalQuery.concat(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
					 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
							 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
							 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
								.setParameterList("bedList", nursingPatientAcceptanceDto.getBedList())
								.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
				 }
			 }
			
				
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptNursingAcceptanceRequet(AdmissionDto admissionDto) throws ApplicationException {
		try{
				
			int preAdmissionDetailsId=0;
			
			AdmissionDetails admissionDetails = (AdmissionDetails)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_DETAILS_BY_ADMISSION_ID")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDetails.class)).uniqueResult();
			
			
			
			if(admissionDetails!=null && admissionDetails.getNursingAcceptance()!=null && admissionDetails.getNursingAcceptance()=='Y')
				return new Response(ERROR, null, "Admission request is already accepted..!!", null, null);
			else if(admissionDetails==null)
				return new Response(ERROR, null, "Admission request is already rejected..!!", null, null);
			
			preAdmissionDetailsId=admissionDetails.getAdmissionDetailsId();
				admissionDetails.setNursingAcceptance('Y');
				admissionDetails.setStatus('A');
				admissionDetails.setIsCancel('N');
				admissionDetails.setNursingAcceptanceDateTime(ADTCommonDateUtils.getDate(admissionDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				admissionDetails.setAdmissionDetailsId(null);
				admissionDetails.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				admissionDetails.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				admissionDetails.setCreatedBy(admissionDto.getUpdatedBy());
				admissionDetails.setUpdatedBy(admissionDto.getUpdatedBy());
			sessionFactory.getCurrentSession().save(admissionDetails);
			
			//<-----
			Query query2=sessionFactory.getCurrentSession().createQuery("update AdmissionDetails dis set dis.status='I' where dis.admissionDetailsId="+preAdmissionDetailsId);
			query2.executeUpdate();
			//----->
			
			
			admissionDto.setBedStatusId(5);
			// <-----Changing status of bed to Booked (5 is the ID of
					// 'Booked'status)
			Query qry2 = sessionFactory.getCurrentSession().createQuery(
					"update BedMaster bed set " +
							"bed.bedStatusId = :bedStatusId, " +
							"bed.updatedBy = :updatedBy, " +
							"bed.updatedDate = :updatedDate " +
							"where bed.bedId = :bedId " +
							"and bed.organizationId = :organizationId " +
							"and bed.unitId = :unitId " +
							"and bed.status = :status"
			);
			qry2.setParameter("bedStatusId", admissionDto.getBedStatusId());
			qry2.setParameter("updatedBy", admissionDto.getUpdatedBy());
			qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			qry2.setParameter("bedId", admissionDetails.getBedId());
			qry2.setParameter("organizationId", admissionDto.getOrganizationId());
			qry2.setParameter("unitId", admissionDto.getUnitId());
			qry2.setParameter("status", 'A');
			qry2.executeUpdate();


			// ----->

					// <-----Updating status of current bed to I in BedLogStatus
					Query qry3 = sessionFactory.getCurrentSession().createQuery("update BedLogStatus bedStatus set bedStatus.status='I', "
							+ " bedStatus.updatedBy=:updatedBy ,"
							+ " bedStatus.updatedDate=:updatedDate "
							+ " WHERE bedStatus.status=:status "
							+ " AND bedStatus.bedId=:bedId "
							+ " AND bedStatus.organizationId=:organizationId "
							+ " AND bedStatus.unitId=:unitId ");
					qry3.setParameter("updatedBy", admissionDto.getUpdatedBy());
					qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					qry3.setParameter("bedId", admissionDetails.getBedId());
					qry3.setParameter("organizationId", admissionDto.getOrganizationId());
					qry3.setParameter("unitId", admissionDto.getUnitId());
					qry3.setParameter("status", 'A');
					qry3.executeUpdate();
					// ----->

					// <-----Adding new entry in log as the status of bed is changing
					BedLogStatus bedLogStatus =new BedLogStatus();
						bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
						bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
						bedLogStatus.setCreatedBy(admissionDto.getUpdatedBy());
						bedLogStatus.setUpdatedBy(admissionDto.getUpdatedBy());
						bedLogStatus.setStatus('A');
						bedLogStatus.setBedId(admissionDetails.getBedId());
						bedLogStatus.setBedStatusId(5);
						bedLogStatus.setOrganizationId(admissionDto.getOrganizationId());
						bedLogStatus.setUnitId(admissionDto.getUnitId());
					sessionFactory.getCurrentSession().save(bedLogStatus);
					// ----->
					
			return new Response(SUCCESS, null, null, null, null);
		}catch(Exception e){
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectNursingAcceptanceRequet(AdmissionDto admission) throws ApplicationException {
		try{
			
			AdmissionDetails admissionDetails = (AdmissionDetails)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_DETAILS_BY_ADMISSION_ID")
					.setParameter("organizationId", admission.getOrganizationId())
					.setParameter("unitId", admission.getUnitId())
					.setParameter("admissionId", admission.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDetails.class)).uniqueResult();
			
			if(admissionDetails!=null && admissionDetails.getNursingAcceptance()!=null && admissionDetails.getNursingAcceptance()=='Y')
				return new Response(ERROR, null, "Admission request is already accepted..!!", null, null);
			else if(admissionDetails==null)
				return new Response(ERROR, null, "Admission request is already rejected..!!", null, null);
			
			// <-----
			sessionFactory.getCurrentSession()
					.createQuery("update AdmissionDetails dis set "
							+ "dis.status = :status, "
							+ "dis.nursingAcceptance = :nursingAcceptance, "
							+ "dis.updatedBy = :updatedBy, "
							+ "dis.updatedDate = :updatedDate, "
							+ "dis.nursingRejectionReasonId = :nursingRejectionReasonId, "
							+ "dis.nursingRejectionNote = :nursingRejectionNote "
							+ "WHERE dis.admissionDetailsId = :admissionDetailsId")
					.setParameter("status", 'I')
					.setParameter("nursingAcceptance", 'R')
					.setParameter("updatedBy", admission.getUpdatedBy())
					.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-M-yyyy HH:mm:ss"))
					.setParameter("nursingRejectionReasonId", admission.getReasonId())
					.setParameter("nursingRejectionNote", admission.getNursingRejectionNote())
					.setParameter("admissionDetailsId", admission.getAdmissionDetailsId())
					.executeUpdate();


			sessionFactory.getCurrentSession()
				.createQuery("update Admission dis set "
						+ " dis.status =:status, "
						+ " dis.updatedBy=:updatedBy, "
						+ " dis.updatedDate=:updatedDate "
						+ " WHERE dis.admissionId=:admissionId ")
					.setParameter("status", 'I')
				.setParameter("admissionId", admission.getAdmissionId())
				.setParameter("updatedBy", admission.getUpdatedBy())
				.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			// ----->
			
			// <-----Changing status of bed to Booked (5 is the ID of
				// 'Booked'status)
			Query qry2 = sessionFactory.getCurrentSession().createQuery(
					"update BedMaster bed set " +
							"bed.bedStatusId = :bedStatusId, " +
							"bed.updatedBy = :updatedBy, " +
							"bed.updatedDate = :updatedDate " +
							"where bed.bedId = :bedId " +
							"and bed.organizationId = :organizationId " +
							"and bed.unitId = :unitId " +
							"and bed.status = :status"
			);
			qry2.setParameter("bedStatusId", 3);
			qry2.setParameter("updatedBy", admission.getUpdatedBy());
			qry2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			qry2.setParameter("bedId", admission.getBedId());
			qry2.setParameter("organizationId", admission.getOrganizationId());
			qry2.setParameter("unitId", admission.getUnitId());
			qry2.setParameter("status", 'A');
			qry2.executeUpdate();

			// ----->

				// <-----Updating status of current bed to I in BedLogStatus
			Query qry3 = sessionFactory.getCurrentSession().createQuery(
					"update BedLogStatus bedStatus set " +
							"bedStatus.status = :status, " +
							"bedStatus.updatedBy = :updatedBy, " +
							"bedStatus.updatedDate = :updatedDate " +
							"WHERE bedStatus.status = :currentStatus " +
							"AND bedStatus.bedId = :bedId " +
							"AND bedStatus.organizationId = :organizationId " +
							"AND bedStatus.unitId = :unitId"
			);
			qry3.setParameter("status", 'I');
			qry3.setParameter("updatedBy", admission.getUpdatedBy());
			qry3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			qry3.setParameter("currentStatus", 'A');
			qry3.setParameter("bedId", admission.getBedId());
			qry3.setParameter("organizationId", admission.getOrganizationId());
			qry3.setParameter("unitId", admission.getUnitId());
			qry3.executeUpdate();

			// ----->

				// <-----Adding new entry in log as the status of bed is changing
				BedLogStatus bedLogStatus = new BedLogStatus();
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					bedLogStatus.setCreatedBy(admission.getUpdatedBy());
					bedLogStatus.setUpdatedBy(admission.getUpdatedBy());
					bedLogStatus.setStatus('A');
					bedLogStatus.setBedId(admission.getBedId());
					bedLogStatus.setBedStatusId(3);
					bedLogStatus.setOrganizationId(admission.getOrganizationId());
					bedLogStatus.setUnitId(admission.getUnitId());
				sessionFactory.getCurrentSession().save(bedLogStatus);
				// ----->
				
				Criteria c=sessionFactory.getCurrentSession().createCriteria(AdmissionNote.class); 
					c.add(Restrictions.eq("admissionId", admission.getAdmissionId()));
					c.add(Restrictions.eq("admissionStatus", 'B'));
					c.add(Restrictions.eq("status", 'A'));
				AdmissionNote admissionNote = (AdmissionNote)c.uniqueResult();
				
				if(admissionNote!=null && admissionNote.getAdmissionNoteId()>0)
				{
					// <-----Updating admission note's admission status to B(Booked)
					// from P(Pending)
					admissionNote.setAdmissionStatus('P');
					sessionFactory.getCurrentSession().update(admissionNote);
					// ----->
		
					// <-----Updating status of last admission patient mapper record to
					// I(inactive)
					Query qry1 = sessionFactory.getCurrentSession().createQuery(
							"update AdmissionPatientMapper m set " +
									"m.activeStatus = :activeStatus, " +
									"m.updatedBy = :updatedBy, " +
									"m.updatedDate = :updatedDate " +
									"where m.admissionNoteId = :admissionNoteId " +
									"and m.status = :status"
					);
					qry1.setParameter("activeStatus", 'P');
					qry1.setParameter("updatedBy", admission.getUpdatedBy());
					qry1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					qry1.setParameter("admissionNoteId", admissionNote.getAdmissionNoteId());
					qry1.setParameter("status", 'A');
					qry1.executeUpdate();

					// ----->
		
				}
				
				return new Response(SUCCESS, null, "Request rejected !!", null, null);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveLaborBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_LABOR_BED_LIST")
			.setParameter("unitId",bedMasterDto.getUnitId())
			.setParameter("organizationId", bedMasterDto.getOrganizationId())
			.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
		
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveHemodialysisBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_HEMODIALYSIS_BED_LIST")
			.setParameter("unitId",bedMasterDto.getUnitId())
			.setParameter("organizationId", bedMasterDto.getOrganizationId())
			.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
		
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	//getting count for pagination in nursing worklist
	@Override
	public Response getPatientAcceptanceListForNursingCount(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)
			throws ApplicationException {
		try {
			 String query=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PATIENT_ACCEPTANCE_LIST_FOR_NURSING" ).getQueryString();
				 
				 String finalQuery;
				 List<AdmissionDto> bedAllocationDtosList=null;
				 if(nursingPatientAcceptanceDto.getIsFromNursingAcceptancePage()!=null && nursingPatientAcceptanceDto.getIsFromNursingAcceptancePage()=='Y') {
							 query= query.concat(" AND admission.visit_type_id = 2 ");
						 if(nursingPatientAcceptanceDto.getIsShowPatientByBed()!=null && nursingPatientAcceptanceDto.getIsShowPatientByBed().equals('N'))	{	
							 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(query.toString())
									 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
									 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
										.setParameterList("wardList", nursingPatientAcceptanceDto.getWardList())
										.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
						 }
						 else if(nursingPatientAcceptanceDto.getIsShowPatientByBed()!=null && nursingPatientAcceptanceDto.getIsShowPatientByBed().equals('Y')){
							 finalQuery=query.replace("adl.ward_id IN (:wardList)", "adl.bed_id in (:bedList)");
							 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
									 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
									 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
										.setParameterList("bedList", nursingPatientAcceptanceDto.getBedList())
										.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
						 }
			 
				 }
			return new Response(SUCCESS, null, null, null, bedAllocationDtosList.size());

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getcheckBedAllocation(AdmissionDto admissionDto) throws ApplicationException {
		try {
      StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_BED_RESERVATION_LIST_DETAILS").getQueryString());
			query.append(" and admissionNote.patientId=:patientId ");
			query.append(" and to_char(mapper.doa,'dd/MM/yyyy')=:createdDate ");
			List<AdmissionNoteDto> admissionNoteDtosList = sessionFactory.getCurrentSession()
					.createQuery(query.toString())
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("patientId", admissionDto.getPatientId())
					.setParameter("unitId", admissionDto.getUnitId())
			        .setParameter("createdDate", admissionDto.getCreatedDate().substring(0, 10))
					.setResultTransformer(Transformers.aliasToBean(AdmissionNoteDto.class)).list();
			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response universalSearchForNursingAcceptance(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)
			throws ApplicationException {
		try {
			 String query=sessionFactory.getCurrentSession()
					.getNamedQuery("UNIVERSAL_SEARCH_FOR_PATIENT_ACCEPTANCE_LIST_FOR_NURSING" ).getQueryString();
			 
			 String finalQuery;
			 List<AdmissionDto> bedAllocationDtosList=null;
			 //added for pagination on patient acceptance screen
			 if(nursingPatientAcceptanceDto.getIsFromNursingAcceptancePage()!=null && nursingPatientAcceptanceDto.getIsFromNursingAcceptancePage()=='Y') {
				 
				 query= query.concat(" AND admission.visit_type_id in (2,3) "
				 				+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
				 
				 if(nursingPatientAcceptanceDto.getIsShowPatientByBed().equals('N'))	{	
					 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
							 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
								.setParameterList("wardList", nursingPatientAcceptanceDto.getWardList())	
								.setParameter("patientName", "%"+nursingPatientAcceptanceDto.getPatientName().trim().toLowerCase()+"%")
								.setFirstResult(nursingPatientAcceptanceDto.getOffset() != null ? nursingPatientAcceptanceDto.getOffset() : 0)
								.setMaxResults(nursingPatientAcceptanceDto.getNoOfRecordsPerPage() != null ? nursingPatientAcceptanceDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
				 }
				 else{
					 finalQuery=query.replace("adl.ward_id IN (:wardList)", "adl.bed_id in (:bedList)");
					 finalQuery= finalQuery.concat(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
					 bedAllocationDtosList=sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
							 .setParameter("orgId", nursingPatientAcceptanceDto.getOrganizationId())
							 .setParameter("unitId", nursingPatientAcceptanceDto.getUnitId())
								.setParameterList("bedList", nursingPatientAcceptanceDto.getBedList())
								.setParameter("patientName", "%"+nursingPatientAcceptanceDto.getPatientName().trim().toLowerCase()+"%")
								.setFirstResult(nursingPatientAcceptanceDto.getOffset() != null ? nursingPatientAcceptanceDto.getOffset() : 0)
								.setMaxResults(nursingPatientAcceptanceDto.getNoOfRecordsPerPage() != null ? nursingPatientAcceptanceDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(NursingPatientAcceptanceDto.class)).list();
				 }
				 
			 }
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAdmisssoinTAT(AdmissionDto admissionDto) throws ApplicationException {/*
		try {
			List<AdmissionDto> list = (List<AdmissionDto>) sessionFactory.getCurrentSession();
			Query query = sessionFactory.getCurrentSession().createSQLQuery(" select "
					+ " to_char(nursing_acceptance_date_time,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Nursing Acceptance' as \"transaction\" "
					+ " from "
					+ " adt.t_admission_details tad "
					+ " where "
					+ " tad.admission_id =:admissionId "
					+ " and nursing_acceptance = 'Y' "
					+ " and nursing_acceptance_date_time is not null "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Dishcarge Summery Generated' as \"transaction\" "
					+ " from "
					+ " adt.m_discharge_summary "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and is_report_release = 'Y' "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Compalins Added' as \"transaction\" "
					+ " from "
					+ " adt.t_complaint_admission "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Discharge Cancelled' as \"transaction\" "
					+ " from "
					+ " adt.t_discharge "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and discharge_status_id = 3 "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Discharged' as \"transaction\" "
					+ " from "
					+ " adt.t_discharge "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and discharge_status_id = 2 "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'MEWS' as \"transaction\" "
					+ " from "
					+ " adt.t_mews_mapper "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Short Leave Request Accepted' as \"transaction\" "
					+ " from "
					+ " adt.t_short_leave "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and short_leave_status_id = 4 "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Patient retained from short leave' as \"transaction\" "
					+ " from "
					+ " adt.t_short_leave "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and short_leave_status_id = 5 "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Short leave request rejected' as \"transaction\" "
					+ " from "
					+ " adt.t_short_leave "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and short_leave_status_id in (7,8,9) "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Tranfer of care' as \"transaction\" "
					+ " from "
					+ " adt.t_transfer_of_care_request "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and transfer_status_id =11 "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Tranfer of bed' as \"transaction\" "
					+ " from "
					+ " adt.t_transfer_request "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and transfer_status_id =9 "
					+ " and transfer_type_id =1 "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Visitor pass assigned' as \"transaction\" "
					+ " from "
					+ " adt.t_visitor_against_patient "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Deposit Collected' as \"transaction\" "
					+ " from "
					+ " billing.m_deposit_master "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Pre Auth Approval' as \"transaction\" "
					+ " from "
					+ " billing.m_preauth_request_master "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and auth_status = 1 "
					+ " and is_final_submit ='Y' "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Bill Generated' as \"transaction\" "
					+ " from "
					+ " billing.m_billing_master "
					+ " where "
					+ " admission_id =:admissionId "
					+ " and bill_cancelled ='N' as \"transaction\" "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Vital captured' as \"transaction\" "
					+ " from "
					+ " patient.t_appointment_vital_mapper "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Services ordered' as \"transaction\" "
					+ " from "
					+ " t_order_master tom "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(added_date, 'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Medications prescribed' as \"transaction\" "
					+ " from "
					+ " pharmacy.t_prescription "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(added_date, 'YYYY-MM-DD HH:MI') "
					+ " union "
					+ " select "
					+ " to_char(created_date, 'YYYY-MM-DD HH:MI') as \"createdDate\", "
					+ " 'Document Uploaded' as \"transaction\" "
					+ " from "
					+ " opd.t_encounter_doc_details "
					+ " where "
					+ " admission_id =:admissionId "
					+ " group by "
					+ " to_char(created_date, 'YYYY-MM-DD HH:MI') "
					+ " order by \"createdDate\" "
			);
			
			 list =sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("admissionId", admissionDto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			 return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
	} return null;
	
*/

		try {
			List<AdmissionDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("ADMISSION_TAT")
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	
	
	}

	@Override
	public Response updateNotRequiredServices(AdmissionDto admission) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			String orderDetailsIds = admission.getOrderDetailsIds().stream()
					.map(String::valueOf)
					.collect(Collectors.joining(","));

			String hql = "update t_order_details set status = 'I', " +
					"ord_cancelled = 'Y', " +
					"ord_cancel_datetime = :ordCancelDatetime, " +
					"ord_cancel_by = :updatedBy " +
					"where order_details_id in (:orderDetailsIds)";

			session.createSQLQuery(hql)
					.setParameter("ordCancelDatetime", GlobalCommonDateUtils.getDate(admission.getOrdCancelDatetime(), "yyyy-M-dd HH:mm:ss"))
					.setParameter("updatedBy", admission.getUpdatedBy())
					.setParameter("orderDetailsIds", orderDetailsIds)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRemarkAdmissionDetailsByAdissionId(AdmissionDto admission) throws ApplicationException {
		try {
			
			
					Session session=sessionFactory.getCurrentSession();
					session.createSQLQuery(" update adt.t_admission_details set" +
											" remark =:remark "+
											" where admission_id=:admissionId ")
					.setParameter("remark", admission.getRemark())
					.setParameter("admissionId", admission.getAdmissionId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Admission getAdmissionByAdissionId(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Admission  admissionObj = (Admission) sessionFactory.getCurrentSession().get(Admission.class,admissionDto.getAdmissionId());
			return admissionObj;
			
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return null;
	}
	
	
	@Override
	public Admission updateAdmissionObject(Admission admission) throws ApplicationException {
		try {
			 sessionFactory.getCurrentSession().update(admission);
			return admission;
			
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return null;
	}

	@Override
	public Response updateOtStatusAgainstAdmission(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update adt.t_admission set" +
									" ot_status =:otStatus "+
									" where admission_id=:admissionId ")
					.setParameter("otStatus", admissionDto.getOtStatus())
					.setParameter("admissionId", admissionDto.getAdmissionId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		
	    }
	return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedCategoryListByVisitId(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			List<BedCategoryMasterDto> bedAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_LIST_BY_VISIT_TYPE_ID")
					.setParameter("organizationId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedCategoryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedAllocationDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	

}
