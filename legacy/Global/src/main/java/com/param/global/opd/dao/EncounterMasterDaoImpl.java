package com.param.global.opd.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.opd.encounter.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.FetchingScrollableResultsImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.BedLogStatus;
import com.param.adt.transfer.model.Transfer;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.BillListResponse;
import com.param.global.billing.dto.ItemDetailsResponse;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.global.GlobalDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.EncounterSyncDto;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.PACSDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PhysiotherapyOrdersListDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.ReferalDetailsDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.dto.items.UomResponse;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IReferalDetailsService;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.utility.Util;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;
import com.param.opd.coversheet.model.EncounterDocDetails;
import com.param.opd.coversheet.model.EncounterMaster;
import com.param.opd.model.ConsultDoctor;
import com.param.opd.model.PacsResultModel;
import com.param.radiology.model.RadiologyMapper;
import com.param.service.global.model.OrderDetailsPackageMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import scala.math.Integral;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.net.HttpURLConnection;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EncounterMasterDaoImpl extends GenericDao<EncounterMaster, Integer> implements IEncounterMasterDao, ICommonConstants,IError{

	public EncounterMasterDaoImpl() {
		super(EncounterMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IKinDetailsDao iKinDetailsDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IReferalDetailsService iReferalDetailsService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	GlobalDao globalDao;
	
	

	
	
	
	final static Logger logger = Logger.getLogger(EncounterMasterDaoImpl.class);
	
	@Override
	public Response saveEncounterMaster(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			EncounterMaster encounterMaster = mapper.map(encounterMasterDto, EncounterMaster.class, "opd_EncounterMasterDto_to_EncounterMaster");
				encounterMaster.setEncounterTime(encounterMasterDto.getEncounterTime());
				encounterMaster.setDummyTokenNumber(encounterMasterDto.getUnitTokenNumber()!=null ? encounterMasterDto.getUnitTokenNumber() : null);
//				encounterMaster.setEncounterDate(encounterMasterDto.getEncounterDate()!=null ? encounterMasterDto.getEncounterDate() : TimeZoneComponent.getDateByzone(encounterMaster.getUnitId()).getTime());
                encounterMaster.setEncounterDate(TimeZoneComponent.getDateByzone(encounterMaster.getUnitId()).getTime());

				if(encounterMasterDto.getIsPaidAppointment()!=null && encounterMasterDto.getIsPaidAppointment()=='Y')
					encounterMaster.setStatusId(1);
				else
					encounterMaster.setStatusId(encounterMasterDto.getStatusId()); // EncounterStatusId
				encounterMaster.setCreatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
				encounterMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
			encounterMaster = save(encounterMaster);
			
			encounterMasterDto.setEncounterId(encounterMaster.getEncounterId());
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, encounterMasterDto);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("saveEncounterMaster failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getEncounterDetailsById(Integer encounterId) throws ApplicationException {
		try {
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto) sessionFactory.getCurrentSession().getNamedQuery("GET_ENCOUNTER_DETAILS_BY_ENCOUNTER_ID")
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, encounterMasterDto);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getEncounterDetailsById failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Long getNextValEncounterMasterId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('opd.encounter_master_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('opd.encounter_master_seq', :id)");
			query2.setParameter("id", id - 1);
			query2.uniqueResult();

			return id;
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getNextValEncounterMasterId failed", e);
		}
		return null;
	}

	
	@Override
	public List<EncounterMasterDto> getEncounterDetailsByPatientDoctorVisitTypeId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ENCOUNTER_DETAILS_FOR_VISIT_TYPE_SERVICE_BY_DOCTOR_AND_PATIENT_ID").getQueryString());

			if (encounterMasterDto.getSpecialityId() != null && encounterMasterDto.getSpecialityId() > 0) {
				query.append(" AND encounter.deptId = :specialityId");
			}

			if (encounterMasterDto.getDoctorId() != null && encounterMasterDto.getDoctorId() > 0) {
				query.append(" AND encounter.doctorId = :doctorId");
			}

			query.append(" ORDER BY encounter.encounterDate DESC, encounter.encounterId DESC");

		 Query encounterMasterDtosList = sessionFactory.getCurrentSession()
					.createQuery(query.toString())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("patientId", encounterMasterDto.getPatientId());

			if (encounterMasterDto.getSpecialityId() != null && encounterMasterDto.getSpecialityId() > 0) {
				encounterMasterDtosList.setParameter("specialityId", encounterMasterDto.getSpecialityId());
			}

			if (encounterMasterDto.getDoctorId() != null && encounterMasterDto.getDoctorId() > 0) {
				encounterMasterDtosList.setParameter("doctorId", encounterMasterDto.getDoctorId());
			}

			encounterMasterDtosList.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class));

			List<EncounterMasterDto> result = encounterMasterDtosList.list();

			return  result;
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getEncounterDetailsByPatientDoctorVisitTypeId failed", e);
		}
		return null;
	}

	@Override
	public Response getEncounterDetails(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			List<EncounterMasterDto> encounterMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OLD_ENCOUNTER_DETAILS")
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, encounterMasterDtosList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
			throw e;
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfEncounterMasterDao(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<SlotMasterDto> encounterMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_ENCOUNTER_MASTER")
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, encounterMasterDtosList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getListOfEncounterMasterDao failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSearchedListEncounterMaster(SlotMasterDto slotMasterDto)throws ApplicationException {
		try {
            Integer count = 0;
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST").getQueryString());

			if (slotMasterDto.getOffset() != null && slotMasterDto.getNoOfRecordsPerPage() != null) {
				stringQuery.append(" AND enMst.dept_id IN (:deptList) ");
				stringQuery.append(" AND (am.appointment_type_id != 6 and enMst.against_enc_id IS NULL) ");
				stringQuery.append(" AND (tod.service_rendered IS NOT NULL AND (am.appointment_type_id = 1 OR am.appointment_type_id = 9)) ");
			}

			StringBuilder finalQuery = this.getFinalQueryAfterAppendConditions(stringQuery, slotMasterDto);

//			if (slotMasterDto.getOffset() != null && slotMasterDto.getNoOfRecordsPerPage() != null) {
//				finalQuery.append(" LIMIT :limit OFFSET :offset ");
//			}

			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString());

			if (slotMasterDto.getPatientId() != null && slotMasterDto.getPatientId() > 0) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null && slotMasterDto.getToDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate().substring(0, 10));
				query.setParameter("toDate", slotMasterDto.getToDate().substring(0, 10));
			}

			if (slotMasterDto.getSpecialityId() != null && slotMasterDto.getSpecialityId() > 0) {
				query.setParameter("specialityId", slotMasterDto.getSpecialityId());
			}

			if (slotMasterDto.getDoctorId() != null && slotMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", slotMasterDto.getDoctorId());
			}

			if (slotMasterDto.getNursingStationId() != null && slotMasterDto.getNursingStationId() > 0) {
				query.setParameter("nursingStationId", slotMasterDto.getNursingStationId());
			}

			if (slotMasterDto.getVisitTypeId() != null && slotMasterDto.getVisitTypeId() > 0) {
				query.setParameter("clinicId", slotMasterDto.getClinicId());
			}

			query.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameterList("deptList", slotMasterDto.getSpecialityIdList());

//			if (slotMasterDto.getOffset() != null && slotMasterDto.getNoOfRecordsPerPage() != null) {
//				query.setParameter("limit", slotMasterDto.getNoOfRecordsPerPage());
//				query.setParameter("offset", slotMasterDto.getOffset());
//			}

			List<SlotMasterDto> slotMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
					.list();

//			if (slotMasterDto.getOffset() != null && slotMasterDto.getNoOfRecordsPerPage() != null) {
//				query.setParameter("limit", slotMasterDto.getNoOfRecordsPerPage());
//				query.setParameter("offset", slotMasterDto.getOffset());
//			}

			count=slotMasterDtosList.size();

				if (slotMasterDto.getOffset() != null && slotMasterDto.getNoOfRecordsPerPage() != null) {
					List<SlotMasterDto> paginatedList = slotMasterDtosList.stream()
							.skip((long) (slotMasterDto.getOffset()))
							.limit(slotMasterDto.getNoOfRecordsPerPage())
							.collect(Collectors.toList());
					slotMasterDtosList = paginatedList;

				}

//	            if(slotMasterDto.getOffset()!=null && slotMasterDto.getNoOfRecordsPerPage()!=null)
//	            {
//	                StringBuilder stringQuery1=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST").getQueryString().toString());
//	                stringQuery1.append(" AND enMst.dept_id IN (:deptList) "    );
//	                stringQuery1.append(" AND (am.appointment_type_id != 6 and enMst.against_enc_id IS NULL) "    );
//	                stringQuery1.append(" AND (tod.service_rendered IS NOT NULL AND (am.appointment_type_id =1 OR am.appointment_type_id = 9 ) )"    );
//	                StringBuilder finalQueryForCount  = this.getFinalQueryAfterAppendConditions(stringQuery1,slotMasterDto);
//	                SQLQuery query1=sessionFactory.getCurrentSession().createSQLQuery(finalQueryForCount.toString());
//					if (slotMasterDto.getPatientId() != null && slotMasterDto.getPatientId() > 0) {
//						query1.setParameter("patientId", slotMasterDto.getPatientId());
//					}
//
//					if (slotMasterDto.getFromDate() != null && slotMasterDto.getToDate() != null) {
//						query1.setParameter("fromDate", slotMasterDto.getFromDate().substring(0, 10));
//						query1.setParameter("toDate", slotMasterDto.getToDate().substring(0, 10));
//					}
//
//					if (slotMasterDto.getSpecialityId() != null && slotMasterDto.getSpecialityId() > 0) {
//						query1.setParameter("specialityId", slotMasterDto.getSpecialityId());
//					}
//
//					if (slotMasterDto.getDoctorId() != null && slotMasterDto.getDoctorId() > 0) {
//						query1.setParameter("doctorId", slotMasterDto.getDoctorId());
//					}
//
//					if (slotMasterDto.getNursingStationId() != null && slotMasterDto.getNursingStationId() > 0) {
//						query1.setParameter("nursingStationId", slotMasterDto.getNursingStationId());
//					}
//
//					if (slotMasterDto.getVisitTypeId() != null && slotMasterDto.getVisitTypeId() > 0) {
//						query1.setParameter("clinicId", slotMasterDto.getClinicId());
//					}
//
//					List<SlotMasterDto> slotMasterDtosListForCount = query1
//	                    .setParameter("orgId", slotMasterDto.getOrganizationId())
//	                    .setParameter("unitId", slotMasterDto.getUnitId())
//	                    .setParameterList("deptList", slotMasterDto.getSpecialityIdList())
//	                    .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
//	                 count=slotMasterDtosListForCount.size();
//	            }
			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
			
		} catch (HibernateException he) {
		logger.error("getSearchedListEncounterMaster failed", he);
		}
		return new Response(ERROR, null, null, null, null);
	}


	private StringBuilder getFinalQueryAfterAppendConditions(StringBuilder namedQuery, SlotMasterDto slotMasterDto) {
		try {

			if (slotMasterDto.getPatientId() != null && slotMasterDto.getPatientId() > 0) {
				namedQuery.append(" AND enMst.patient_id = :patientId ");
			}

			if (slotMasterDto.getFromDate() != null && slotMasterDto.getToDate() != null) {
				namedQuery.append(" AND to_char(enMst.encounter_date, 'yyyy-mm-dd') BETWEEN :fromDate AND :toDate ");
			}

			if (slotMasterDto.getSpecialityId() != null && slotMasterDto.getSpecialityId() > 0) {
				namedQuery.append(" AND sm.speciality_id = :specialityId ");
			}

			if (slotMasterDto.getDoctorId() != null && slotMasterDto.getDoctorId() > 0) {
				namedQuery.append(" AND enMst.doctor_id = :doctorId ");
			}

			if (slotMasterDto.getNursingStationId() != null && slotMasterDto.getNursingStationId() > 0) {
				namedQuery.append(" AND nurStatClinMapp.nursing_station_id = :nursingStationId ");
			}

			if (slotMasterDto.getVisitTypeId() != null && slotMasterDto.getVisitTypeId() > 0) {
				namedQuery.append(" AND enMst.clinic_id = :clinicId ");
			}

			if (slotMasterDto.getStatusId() > 0) {
				if (slotMasterDto.getStatusId() == 1) {
					namedQuery.append(" AND enDet.assignment_start_time IS NULL ");
				} else if (slotMasterDto.getStatusId() == 2) {
					namedQuery.append(" AND enDet.assignment_start_time IS NOT NULL AND enDet.assignment_stop_time IS NULL ");
				} else if (slotMasterDto.getStatusId() == 3) {
					namedQuery.append(" AND enDet.assignment_start_time IS NOT NULL AND enDet.assignment_stop_time IS NOT NULL ");
				}
			}

			namedQuery.append(" ORDER BY enMst.encounter_date ASC, enDet.is_priority = 'Y', enMst.is_critical = 'Y', EXTRACT(year FROM AGE(CURRENT_TIMESTAMP, patient.dob)) > 60 DESC, enMst.dummy_token_number ");

			return namedQuery;
		} catch (Exception e) {
		logger.error("getFinalQueryAfterAppendConditions failed", e);
		}
		return namedQuery;
	}

	@Override
	public Response getListOfAppointmentStatus(AppointmentStatusMasterDto appointmentStatusMasterDto)
			throws ApplicationException {
		try {
			List<AppointmentStatusMasterDto> appointmentStatusMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_APPOINTMENT_STATUS_MASTER")
					.setResultTransformer(Transformers.aliasToBean(AppointmentStatusMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, appointmentStatusMasterDtoList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getListOfAppointmentStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEncounterByIdForSync(int encounterId)throws ApplicationException {
		try{
			EncounterSyncDto encounterSyncDto = (EncounterSyncDto) sessionFactory.getCurrentSession().getNamedQuery("GET_ENCOUNTER_BY_ID_FOR_GLOBAL_SCHEDULING_SYNC").setParameter("encounterId", encounterId).setResultTransformer(Transformers.aliasToBean(EncounterSyncDto.class)).uniqueResult();
			return new Response<>(SUCCESS, null, null, null, encounterSyncDto);
			
		}catch(HibernateException he){
		logger.error("getEncounterByIdForSync failed", he);
		}catch(Exception e){
		logger.error("getEncounterByIdForSync failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfEncountersByPatientId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			List<AppointmentStatusMasterDto> appointmentStatusMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_ENCOUNTERS_BY_PATIENT_ID")
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, appointmentStatusMasterDtoList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getListOfEncountersByPatientId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAutoRenderedServicesByPatient(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_AUTO_RENDERED_SERVICES_BY_PATIENT").getQueryString());

			query.append(" AND orderm.encounter_id is null AND orderm.admission_id is null ");
		    
			
			List<ServiceMasterDto> listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (Exception e) {
		logger.error("getAutoRenderedServicesByPatient failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	

	@Override
	public List<UnitServiceTariffMasterDto> getBasePriceByServiceTariffMaster(
			UnitServiceTariffMasterDto unitServiceTariffMasterDto) throws ApplicationException {
	try {
				StringBuffer mainQuery = new StringBuffer("select "
						+ " 	unitservic0_.unit_service_tariff_id as \"unitServiceTriffId\", "
						+ " 	coalesce(unitservic0_.rate, 0) as \"rate\", "
						+ " 	coalesce((coalesce(unitservic0_.rate, 0)+((coalesce(tax.tax_percentage, 0)* 0.01)* coalesce(unitservic0_.rate, 0))),0) as \"finalRate\", "
						+ " 	cast(unitservic3_.rate_editble_min_value as float8) as \"minRateEditable\", "
						+ " 	cast(unitservic3_.rate_editable_max_value as float8) as \"maxRateEditable\", "
						+ " 	coalesce(unitservic3_.is_rate_editable, cast('N' as char(1))) as \"isRateEditable\", "
						+ " 	coalesce(unitservic3_.is_allow_multiple_quantity, cast('N' as char(1))) as \"isQuantityEditable\", "
						+ "    coalesce(unitservic3_.is_doc_req, cast('N' as char)) as \"isDocReq\", "
						+ "    coalesce(unitservic3_.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", "
						+ " 	servicemas1_.service_standard_name as \"serviceDesc\", "
						+ " 	servicemas1_.speciality_id as \"specialityId\", "
						+ " 	servicemas1_.sub_speciality_id as \"subSpecialityId\", "
						+ " 	speciality2_.speciality_name as \"specialityDesc\", "
						+ "	coalesce(tax.tax_percentage, 0) as \"taxPercentage\", "
						+ "	tax.id as \"taxId\", "
						+ " appConfig.self_payee_id as  \"selfPayeeId\", "
						+ " 	to_char(unitservic0_.effective_date, 'yyyy-mm-dd') as \"effectiveDate\", "
						+ " 		coalesce(unitservic3_.is_discount,'N') as \"isDiscount\", "
						+ " 		coalesce(appConfig.is_scheme_discount,'N') as \"isSchemeDiscount\", "
						+ "	unitservic3_.is_auto_render as \"isAutoRender\", "
						+ "    appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" "
						+ " from "
						+ " 	service.m_unit_service_tariff_master unitservic0_ "
						+ " inner join service.m_service_master servicemas1_ on "
						+ " 	unitservic0_.service_id = servicemas1_.service_master_id "
						+ " inner join doctor.m_speciality_master speciality2_ on "
						+ " 	servicemas1_.speciality_id = speciality2_.speciality_id "
						+ " inner join service.t_unit_service_master_details unitservic3_ on "
						+ " 	servicemas1_.service_master_id = unitservic3_.service_master_id "
						+ " 	and unitservic3_.status = 'A' "
						+ " left join m_tax tax on "
						+ " 	unitservic3_.gst_type_id = tax.id "
						+ " inner join t_unit_application_billing_configuration appConfig on "
						+ " 	appConfig.unit_id = unitservic3_.unit_id "
						+ " where "
						+ " 	unitservic0_.service_id =:serviceId "
						+ " 	and unitservic0_.tariff_id =:tariffId "
						+ " 	and unitservic0_.payment_entitlement_id =:paymentEntitlementId "
						+ " 	and unitservic0_.visit_type_id =:visitTypeId "
						+ " 	and unitservic0_.patient_type_id =:patientTypeId "
						+ " 	and unitservic0_.billing_bed_category_id =:billingBedCategoryId "
						+ " 	and unitservic0_.organization_id =:organizationId "
						+ " 	and unitservic0_.unit_id =:unitId "
						+ " 	and unitservic3_.unit_id =:unitId "
						+ " 	and to_char(unitservic0_.effective_date, 'YYYY-MM-DD') <=:effectiveDate ");


						StringBuffer subQuery = new StringBuffer(mainQuery.toString());
						subQuery.append(" and unitservic0_.status = 'A' "
								+ " order by unitservic0_.unit_service_tariff_id desc");


				Query query = sessionFactory.getCurrentSession().createSQLQuery(subQuery.toString());
				
				List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtos = query
						.setParameter("serviceId", unitServiceTariffMasterDto.getServiceId())
						.setParameter("tariffId", unitServiceTariffMasterDto.getTariffId())
						.setParameter("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementId())
						.setParameter("visitTypeId", unitServiceTariffMasterDto.getVisitTypeId())
						.setParameter("patientTypeId", unitServiceTariffMasterDto.getPatientTypeId())
						.setParameter("billingBedCategoryId", unitServiceTariffMasterDto.getBillingBedCategoryId())
						.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
						.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
						.setParameter("effectiveDate", unitServiceTariffMasterDto.getOrderDate().substring(0,10))
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();

				if (unitServiceTariffMasterDtos == null || unitServiceTariffMasterDtos.isEmpty()) {

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDate orderDate = LocalDateTime.parse(unitServiceTariffMasterDto.getOrderDate(), formatter).toLocalDate();
					LocalDate today = LocalDate.now();

					if(orderDate.isBefore(today)) {
						mainQuery.append(" order by unitservic0_.unit_service_tariff_id desc "
								+ " limit 1");

						Query updatedQuery = sessionFactory.getCurrentSession().createSQLQuery(mainQuery.toString());
						unitServiceTariffMasterDtos = updatedQuery
								.setParameter("serviceId", unitServiceTariffMasterDto.getServiceId())
								.setParameter("tariffId", unitServiceTariffMasterDto.getTariffId())
								.setParameter("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementId())
								.setParameter("visitTypeId", unitServiceTariffMasterDto.getVisitTypeId())
								.setParameter("patientTypeId", unitServiceTariffMasterDto.getPatientTypeId())
								.setParameter("billingBedCategoryId", unitServiceTariffMasterDto.getBillingBedCategoryId())
								.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
								.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
								.setParameter("effectiveDate", unitServiceTariffMasterDto.getOrderDate().substring(0, 10))
								.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
					}
				}
				return unitServiceTariffMasterDtos;
	
			} catch (HibernateException he) {
			logger.error("getBasePriceByServiceTariffMaster failed", he);
			} catch (Exception e) {
			logger.error("getBasePriceByServiceTariffMaster failed", e);
			} return null;

	}

	@Override
	public Response updateAutorenderServices(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try {
			
			if(orderDetailsMasterDto.getVisitTypeId()==OP){

				StringBuilder query = new StringBuilder("UPDATE OrderMaster SET encounterId = :encounterId, visitTypeId = :visitTypeId");

				if (orderDetailsMasterDto.getOrdDocId() != null && orderDetailsMasterDto.getOrdDocId() > 0) {
					query.append(", doctorId = :doctorId");
				}

				if (orderDetailsMasterDto.getOrdDocSplId() != null && orderDetailsMasterDto.getOrdDocSplId() > 0) {
					query.append(", ordDocSplId = :ordDocSplId");
				}

				query.append(" WHERE orderId = :orderId");

				Query updateQuery = sessionFactory.getCurrentSession().createQuery(query.toString());

				updateQuery.setParameter("encounterId", orderDetailsMasterDto.getEncounterId());
				updateQuery.setParameter("visitTypeId", orderDetailsMasterDto.getVisitTypeId());

				if (orderDetailsMasterDto.getOrdDocId() != null && orderDetailsMasterDto.getOrdDocId() > 0) {
					updateQuery.setParameter("doctorId", orderDetailsMasterDto.getOrdDocId());
				}

				if (orderDetailsMasterDto.getOrdDocSplId() != null && orderDetailsMasterDto.getOrdDocSplId() > 0) {
					updateQuery.setParameter("ordDocSplId", orderDetailsMasterDto.getOrdDocSplId());
				}

				updateQuery.setParameter("orderId", orderDetailsMasterDto.getOrderId());

				updateQuery.executeUpdate();


			}if(orderDetailsMasterDto.getVisitTypeId()!=OP){

				StringBuilder query = new StringBuilder("UPDATE OrderMaster SET admissionId = :admissionId, visitTypeId = :visitTypeId");
				if (orderDetailsMasterDto.getOrdDocId() != null && orderDetailsMasterDto.getOrdDocId() > 0) {
					query.append(", doctorId = :doctorId");
				}
				query.append(" WHERE orderId = :orderId");
				Query updateQuery = sessionFactory.getCurrentSession().createQuery(query.toString());
				updateQuery.setParameter("admissionId", orderDetailsMasterDto.getAdmissionId());
				updateQuery.setParameter("visitTypeId", orderDetailsMasterDto.getVisitTypeId());
				if (orderDetailsMasterDto.getOrdDocId() != null && orderDetailsMasterDto.getOrdDocId() > 0) {
					updateQuery.setParameter("doctorId", orderDetailsMasterDto.getOrdDocId());
				}
				updateQuery.setParameter("orderId", orderDetailsMasterDto.getOrderId());
				updateQuery.executeUpdate();
			}

			OrderDetailsMaster orderDetailsMaster = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsMasterDto.getOrderDetailsId());
				orderDetailsMaster.setServiceAmt( orderDetailsMasterDto.getServiceAmt());
				orderDetailsMaster.setDummyServiceAmt(orderDetailsMasterDto.getDummyServiceAmt());
				orderDetailsMaster.setOrdConcession(orderDetailsMasterDto.getOrdConcession());
				orderDetailsMaster.setOrdDiscount(orderDetailsMasterDto.getOrdDiscount());
				orderDetailsMaster.setTariffId(orderDetailsMasterDto.getTariffId());
				orderDetailsMaster.setServiceRendered(orderDetailsMasterDto.getServiceRendered());
				orderDetailsMaster.setServiceIsBilled(orderDetailsMasterDto.getServiceIsBilled());
				orderDetailsMaster.setTaxId(orderDetailsMasterDto.getTaxId());
				orderDetailsMaster.setTaxPer(orderDetailsMasterDto.getTaxPer());
				orderDetailsMaster.setOrdTotalAmt(orderDetailsMasterDto.getOrdTotalAmt());
				orderDetailsMaster.setTaxAmount(orderDetailsMasterDto.getTaxAmount());
				orderDetailsMaster.setNetAmount(orderDetailsMasterDto.getNetAmount());
				orderDetailsMaster.setSelfPayable(orderDetailsMasterDto.getSelfPayable());
				orderDetailsMaster.setCreditPayable(orderDetailsMasterDto.getCreditPayable());
				orderDetailsMaster.setCoPayPer(orderDetailsMasterDto.getCoPayPer());
				orderDetailsMaster.setPatientClassPercentage(orderDetailsMasterDto.getPatientClassPercentage());
				orderDetailsMaster.setHospitalShare(orderDetailsMasterDto.getHospitalShare());
				orderDetailsMaster.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer());
				orderDetailsMaster.setDocSharePer(orderDetailsMasterDto.getDocSharePer());
				//For package
				if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0)
				{
					for(OrderDetailsPackageMapperDto dto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList())
					{
						OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
						detailsPackageMapper.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
						detailsPackageMapper.setPayeeId(dto.getPayeeId());
						detailsPackageMapper.setPackageId(dto.getPackageId());
						detailsPackageMapper.setIsPackageService(dto.getIsPackageService());
						detailsPackageMapper.setIsCapService(dto.getIsCapService());
						detailsPackageMapper.setPackageAmount(dto.getPackageAmount());
						detailsPackageMapper.setServiceId(dto.getServiceId());
						detailsPackageMapper.setPackageQty(dto.getPackageQty());
						detailsPackageMapper.setServiceAmount(dto.getServiceAmount());
						detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
						detailsPackageMapper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
						detailsPackageMapper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
						detailsPackageMapper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						detailsPackageMapper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
						detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						detailsPackageMapper.setStatus('A');
						sessionFactory.getCurrentSession().save(detailsPackageMapper);
					}
				}
				else
				{
					OrderDetailsPackageMapper detailsPackageMapper = new OrderDetailsPackageMapper();
					detailsPackageMapper.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
					detailsPackageMapper.setIsPackageService('N');
					detailsPackageMapper.setIsCapService('N');
					detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
					detailsPackageMapper.setServiceId(orderDetailsMasterDto.getServiceId());
					detailsPackageMapper.setServiceAmount(orderDetailsMasterDto.getServiceAmt());
					detailsPackageMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
					detailsPackageMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
					detailsPackageMapper.setStatus('A');
					sessionFactory.getCurrentSession().save(detailsPackageMapper);
				}
				
				iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMaster,orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList(),orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList());
				
		} catch (HibernateException he) {
		logger.error("updateAutorenderServices failed", he);
		} catch (Exception e) {
		logger.error("updateAutorenderServices failed", e);
		} return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	
	}

	@Override
	public Tax getTaxPercentageByServiceId(Integer serviceId,Integer unitId) {
		try {
			
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("SELECT tax.id as \"id\"," 
					+	" tax.tax_percentage as \"taxPercentage\" "
					+	" FROM public.m_tax tax "
					+	" INNER JOIN service.t_unit_service_master_details usmd on usmd.gst_type_id = tax.id "
					+	" WHERE usmd.service_master_id =:serviceId "
					+ 	" AND usmd.unit_id =:unitId "
					+   " and usmd.status ='A' "
	
			);
			
			Tax tax = (Tax)query
				.setResultTransformer(Transformers.aliasToBean(Tax.class))
					.setParameter("serviceId",serviceId)
					.setParameter("unitId",unitId)
					.uniqueResult();
			
			return tax;

		} catch (HibernateException he) {
		logger.error("getTaxPercentageByServiceId failed", he);
		} catch (Exception e) {
		logger.error("getTaxPercentageByServiceId failed", e);
		} return null;
	}

	@Override
	public Response updateEncounterStatus(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
					+ "statusId=:statusId, "
					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ "WHERE encounterId=:encounterId")
				.setParameter("statusId", encounterMasterDto.getStatusId())
				.setParameter("updatedBy",encounterMasterDto.getUpdatedBy())
				.setParameter("updatedDate",TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), StandardBasicTypes.TIMESTAMP)
				.setParameter("encounterId", encounterMasterDto.getEncounterId())
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("updateEncounterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateEncounterStatus failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveDocumentAgainstEncounter(EncounterDocDetailsDto encounterDocDetailsDto)
			throws ApplicationException {
		try {
			EncounterDocDetails master=new EncounterDocDetails();
				master.setEncounterId(encounterDocDetailsDto.getEncounterId());
				master.setDocumentTypeId(encounterDocDetailsDto.getDocumentTypeId());
				master.setPatientId(encounterDocDetailsDto.getPatientId());
				master.setDocumentPath(encounterDocDetailsDto.getDocumentPath());
				master.setStatus('A');
				master.setOrganisationId(encounterDocDetailsDto.getOrganisationId());
				master.setUnitId(encounterDocDetailsDto.getUnitId());
				master.setCreatedBy(encounterDocDetailsDto.getCreatedBy());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(encounterDocDetailsDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
				master.setUpdatedBy(encounterDocDetailsDto.getUpdatedBy());
				master.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId()));
				master.setVisitTypeId(encounterDocDetailsDto.getVisitTypeId());
				master.setAdmissionId(encounterDocDetailsDto.getAdmissionId());
				master.setExpenseId(encounterDocDetailsDto.getExpenseId());
				master.setGrnId(encounterDocDetailsDto.getGrnId());
				master.setIncidentReportingMapperId(encounterDocDetailsDto.getIncidentReportingMapperId());
				master.setPreauthRequestId(encounterDocDetailsDto.getPreauthRequestId());
				master.setLabSampleDtlsId(encounterDocDetailsDto.getLabSampleDtlsId());
				master.setCompanyExpenseId(encounterDocDetailsDto.getCompanyExpenseId());
                master.setRemark(encounterDocDetailsDto.getRemark()!=null ? encounterDocDetailsDto.getRemark():null);
                master.setPoId(encounterDocDetailsDto.getPoId()!=null ? encounterDocDetailsDto.getPoId():null);
                master.setDeathPatientId(encounterDocDetailsDto.getDeathPatientId() != null ? encounterDocDetailsDto.getDeathPatientId():null);
                master.setOpbId(encounterDocDetailsDto.getOpbId());
                master.setVendorId(encounterDocDetailsDto.getVendorId());
				if(encounterDocDetailsDto.getUploadDoccumentDate() != null) {
					master.setUploadDoccumentDate(GlobalCommonDateUtils.getDate(encounterDocDetailsDto.getUploadDoccumentDate(), "dd-MM-yyyy HH:mm:ss"));
				}
				master.setFileName(encounterDocDetailsDto.getFileName());
		    Integer id = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster set "
					+ "serviceRendered=1, "
					+ "ordRenderDatetime=now() "
					+ "WHERE orderDetailsId=:orderDetailsId ")
				.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
			.executeUpdate();
			
			
			sessionFactory.getCurrentSession().createQuery("update CyclicAppointmentMapper cam set "
					+ "cam.availabelQuantity=0, "
					+ "cam.status='I'"
					+ "WHERE cam.orderDetailId=:orderDetailsId")
				.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
			.executeUpdate();
			return new Response(SUCCESS, null, "Request accepted successfully...!!", null, null);

		} catch (HibernateException he) {
		logger.error("acceptRadiologyRequest failed", he);
		} catch (Exception e) {
		logger.error("acceptRadiologyRequest failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response retestRadiologyResultEntry(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			Integer count = 0;
			if(radiologyOrdersListDto.getRedoCount()==null){
				count = 1;
			}else{
				count = radiologyOrdersListDto.getRedoCount()+1;
			}

			sessionFactory.getCurrentSession().createQuery("UPDATE OrderDetailsMaster " +
							"SET serviceRendered = 0, " +
							"redoCount = :count, " +
							"redoReason = :reason " +
							"WHERE orderDetailsId = :orderDetailsId")
					.setParameter("count", count)
					.setParameter("reason", radiologyOrdersListDto.getRedoReason())
					.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
					.executeUpdate();

			Integer mapperId= (Integer) sessionFactory.getCurrentSession().createSQLQuery("select mapper_id from public.t_radiology_mapper " +
					"tod  where order_details_id ="+radiologyOrdersListDto.getOrderDetailsId()+" ").uniqueResult();;

			String hql = "DELETE FROM public.t_radiology_mapper WHERE mapper_id = :mapperId";
			Integer deletedEntities = sessionFactory.getCurrentSession().createSQLQuery(hql)
					.setParameter("mapperId", mapperId)
					.executeUpdate();


			sessionFactory.getCurrentSession().createQuery("update CyclicAppointmentMapper cam set "
							+ "cam.availabelQuantity=1, "
							+ "cam.status='A'"
							+ "WHERE cam.orderDetailId=:orderDetailsId")
					.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
					.executeUpdate();
			return new Response(SUCCESS, null, "Retest successfully...!!", null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getEHCPatientsList(EncounterMasterDto encounterMasterDto,Character isPreOrPostBilling)
	{
			try {
				
				String query = null;
				List<SlotMasterDto> slotMasterDtosList=null;
				if(isPreOrPostBilling=='N')
				{
					query =  " SELECT  "
							+ "	ec.consultation_master_id as \"consultationMasterId\", "
							+ "	enMst.encounter_id as \"encounterId\", "
							+ "(case when enMst.encounter_type_id=6 then"  
							+ "		enMst.review_against_enc_id " 
							+"	else " 
							+"		enMst.encounter_id "  
							+"	end) as \"openCoversheetWithEncounterId\", "
							+ "enMst.encounter_number as \"encounterNumber\", "
							+ " enMst.encounter_date as \"encounterDate\", "
							+ " enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" ,"
							+ " enMst.status as \"status\", "
							+ " enMst.status_id as \"statusId\", "
							+ " enMst.is_called as \"isCalled\", "
							+ " enMst.call_count as \"callCount\", "
							+ " enMst.cabin_id as \"cabinId\", "
							+ " enMst.doctor_desk_arrival as \"doctorDeskArrival\", "
							+ " patient.is_vip as \"isVip\", "
							+ " (case when patient.is_vip='Y' then "
							+ "  coalesce(patient.alise_name,patient.first_name) "
							+ "  else "
							+ "  concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) "
							+ "  end	) as \"patientName\", "
							+ "	to_char(sm.appointment_date,'dd/MM/yyyy') as \"appointmentDate\", "
							+ "am.appointment_id as \"appointmentId\", "
							+ "sm.slot_id as \"slotId\", "
							+ "concat(to_char(sm.from_time,'HH24:MI')) as \"fromTimeString\", "
							+ "concat(to_char(sm.to_time,'HH24:MI')) as \"toTimeString\", "
							+ "am.remark as \"remark\", "
							+ "enMst.doctor_id as \"doctorId\", "
							+ "enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", "
							+ "am.appointment_type_id as \"appointmentTypeId\", "
							+ "enMst.encounter_type_id as \"encounterTypeId\", "
							+ "am.appointment_status_id as \"appointmentStatusId\", "
							+ "patient.patient_category_id as \"patientCategoryId\", "
							+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
							+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
							+ "enMst.dept_id as \"specialityId\", "
							+ "speciaityMaster.speciality_name as \"specialityName\", "
							+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\", "
							+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob))) as \"ageYear\", "
							+ "patient.mobile_number as \"mobileNumber\", "
							+ "patient.email as \"email\", "
							+ "patient.patient_id as \"patientId\", "
							+ "patient.uhid_number as \"uhidNumber\", "
							+ "patient.prefix_id as \"prefixId\", "
							+ "prefix.prefix_code as \"prefixCode\", " 
							+ "patient.gender_id as \"genderId\", "
							+ "patient.image_path as \"imagePath\", "
							+ "patient.mpshah_patient_id as \"mpshahPatientId\", "
							+ "gender.gender_code as \"genderCode\", "
							+ "gender.gender_name as \"genderDec\", "
							+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", "
							+ "sm.modality_id as \"modalityId\", "
							+ "modality.modality_desc as \"modalityDesc\", "
							+ " enDet.is_assignment_comp_status as \"isAssignmentCompStatus\","
							+ " enDet.is_consultation_status as \"isConsultationStatus\", "
							+ " to_char(enDet.consultation_start_time,'HH24:MI:SS') as \"consultationStartTime\", "
							+ " to_char(enDet.consultation_stop_time,'HH24:MI:SS') as \"consultationStopTime\", "
							+ " to_char(enDet.consultation_start_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", "
							+ " to_char(enDet.consultation_stop_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", "
							+ " refrlTypeMst.referral_type_name as \"referralTypeName\", "
							+ " reflMst.referral_name as \"referralName\", "
							+ " refrDtls.referral_remark as \"referralRemark\", "
							+ "adm.admission_id as \"admissionId\", "
							+ " to_char(enDet.assignment_start_time,'HH:MM:SS') as \"assignmentStartTime\", "
							+ " to_char(enDet.assignment_start_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", "
							+ " to_char(enDet.assignment_stop_time,'HH:MM:SS') as \"assignmentStopTime\", "
							+ " to_char(enDet.assignment_stop_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", "
							+ " enDet.assignment_start_by as \"assignmentStartBy\", "
							+ " enDet.assignment_stop_by as \"assignmentStopBy\", "
							+ " enDet.assignment_start_role_id as \"assignmentStartRoleId\", "
							+ " enDet.assignment_stop_role_id as \"assignmentStopRoleId\", "
							+ " enDet.consultation_start_by as \"consultationStartBy\", "
							+ " enDet.consultation_start_role_id as \"consultationStartRoleId\", "
							+ " enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", "
							+ " spd.contract_id as \"contractId\", "
							+ " spd.payee_id as \"payeeId\", "
							+ " enMst.doc_queue_number as \"queueNumber\", "
							+ " enMst.unit_token_number as \"tokenNumber\", "
							+ " enDet.is_priority as \"isPriority\", "
							+ " enMst.is_critical as \"isCritical\", "
							+ " doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", "
							+ " enMst.speciality_queue_number as \"specialityQueueNumber\",  "
							+ " speciaityMapper.dept_wise_appointment as \"deptWiseAppointment\", "
							+ "cast((select count(billing_master_id) from billing.m_billing_master where encounter_id=enMst.encounter_id and bill_cancelled = 'N') as integer) as \"billId\", "
							+ "enMst.old_doc_id as \"oldDocId\", "
							+ "(case "
							+ "  when enMst.is_critical = 'Y' then 1 "
							+ "  when enMst.is_critical = 'N' then 2 "
							+ " end) as \"isCriticalOrder\", "
							+ " (case "
							+ "  when enDet.is_priority = 'Y' then 1 "
							+ "  when enDet.is_priority is null then 2 "
							+ " end) as \"isPriorityOrder\", "
							+ " coalesce((select nursing_acceptance from adt.t_admission_details where status='A' and admission_id=( "
							+ "	select admission_id from adt.t_admission_note where encounter_id=enMst.encounter_id)),'N') as \"nursingAccepatance\" "
							+ " FROM "
							+ "	opd.t_encounter_ehc_consultation_master ec "
							+ " inner join  billing.t_package_consumption_master pkgcnsumtxnnmstr on pkgcnsumtxnnmstr.order_detail_id=ec.order_details_id and pkgcnsumtxnnmstr.status='A' " 
							+ " inner join opd.t_encounter_master enMst on 	ec.encounter_id = enMst.encounter_id "
							+ " LEFT JOIN opd.t_encounter_details enDet on enDet.encounter_id= enMst.encounter_id "
							+ " LEFT JOIN opd.m_appointment_master am on am.appointment_id=enMst.appointment_id "
							+ " LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
							+ " LEFT JOIN public.t_referal_details refrDtls on refrDtls.encounter_id=enMst.encounter_id "
							+"  LEFT JOIN adt.m_referral_type_master refrlTypeMst on refrlTypeMst.referral_type_id=refrDtls.referral_type_id "
							+"  LEFT JOIN adt.m_referral_master reflMst on reflMst.referral_id=refrDtls.referral_id "
							+ " LEFT JOIN opd.t_patient_sponsor_details spd on spd.encounter_id=enMst.encounter_id AND spd.priority_id=1 and spd.status='A' "
							+ " LEFT JOIN public.m_clinic_master clinMst on  clinMst.clinic_master_id=enMst.clinic_id "
							+ " LEFT JOIN opd.t_nursing_station_clinic_mapper nurStatClinMapp on nurStatClinMapp.clinic_id=clinMst.clinic_master_id "
							+ " LEFT OUTER JOIN doctor.m_speciality_master speciaityMaster on enMst.dept_id=speciaityMaster.speciality_id "
							+ " left outer join t_unit_speciality_mapper speciaityMapper on " 
							+ " 	enMst.dept_id = speciaityMapper.speciality_id " 
							+ " 	and speciaityMapper.unit_id = enMst.unit_id " 
							+ " LEFT OUTER JOIN doctor.m_doctor_master doc on enMst.doctor_id=doc.doctor_id "
							+ " LEFT OUTER JOIN public.m_modality_master modality on sm.modality_id=modality.modality_id "
							+ " LEFT JOIN patient.t_patient_registration patient on enMst.patient_id=patient.patient_id "
							+ " LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
							+ " LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
							+ " LEFT JOIN opd.m_appointment_status_master asm on asm.appointment_status_id=enMst.status_id "
							+ " LEFT OUTER JOIN adt.t_admission adm on adm.admission_id = am.admission_id  AND adm.status='A' "
							+ " WHERE enMst.organization_id=:orgId "
							+ " AND enMst.unit_id=:unitId "+ 
							 "  AND enMst.status_id in (5,6,8) "
							+ " AND patient.registration_type_id IN (1,3,4,6) "
//							+ " AND am.appointment_type_id IN (3)"
							+ " ";
							
			          slotMasterDtosList = sessionFactory.getCurrentSession()
						    .createSQLQuery(query)
							.setParameter("orgId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				}else {

					query =  " SELECT  "
							+ "	ec.consultation_master_id as \"consultationMasterId\", "
							+ "	enMst.encounter_id as \"encounterId\", "
							+ "(case when enMst.encounter_type_id=6 then"  
							+ "		enMst.review_against_enc_id " 
							+"	else " 
							+"		enMst.encounter_id "  
							+"	end) as \"openCoversheetWithEncounterId\", "
							+ "enMst.encounter_number as \"encounterNumber\", "
							+ " enMst.encounter_date as \"encounterDate\", "
							+ " enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" ,"
							+ " enMst.status as \"status\", "
							+ " enMst.status_id as \"statusId\", "
							+ " enMst.is_called as \"isCalled\", "
							+ " enMst.call_count as \"callCount\", "
							+ " enMst.cabin_id as \"cabinId\", "
							+ " enMst.doctor_desk_arrival as \"doctorDeskArrival\", "
							+ " patient.is_vip as \"isVip\", "
							+ " (case when patient.is_vip='Y' then "
							+ "  coalesce(patient.alise_name,patient.first_name) "
							+ "  else "
							+ "  concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) "
							+ "  end	) as \"patientName\", "
							+ "	to_char(sm.appointment_date,'dd/MM/yyyy') as \"appointmentDate\", "
							+ "am.appointment_id as \"appointmentId\", "
							+ "sm.slot_id as \"slotId\", "
							+ "concat(to_char(sm.from_time,'HH24:MI')) as \"fromTimeString\", "
							+ "concat(to_char(sm.to_time,'HH24:MI')) as \"toTimeString\", "
							+ "am.remark as \"remark\", "
							+ "enMst.doctor_id as \"doctorId\", "
							+ "enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", "
							+ "am.appointment_type_id as \"appointmentTypeId\", "
							+ "enMst.encounter_type_id as \"encounterTypeId\", "
							+ "am.appointment_status_id as \"appointmentStatusId\", "
							+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
							+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
							+ "enMst.dept_id as \"specialityId\", "
							+ "speciaityMaster.speciality_name as \"specialityName\", "
							+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\", "
							+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob))) as \"ageYear\", "
							+ "patient.mobile_number as \"mobileNumber\", "
							+ "patient.email as \"email\", "
							+ "patient.patient_id as \"patientId\", "
							+ "patient.uhid_number as \"uhidNumber\", "
							+ "patient.prefix_id as \"prefixId\", "
							+ "prefix.prefix_code as \"prefixCode\", " 
							+ "patient.gender_id as \"genderId\", "
							+ "patient.image_path as \"imagePath\", "
							+ "patient.patient_category_id as \"patientCategoryId\", "
							+ "patient.mpshah_patient_id as \"mpshahPatientId\", "
							+ "gender.gender_code as \"genderCode\", "
							+ "gender.gender_name as \"genderDec\", "
							+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", "
							+ "sm.modality_id as \"modalityId\", "
							+ "modality.modality_desc as \"modalityDesc\", "
							+ " enDet.is_assignment_comp_status as \"isAssignmentCompStatus\","
							+ " enDet.is_consultation_status as \"isConsultationStatus\", "
							+ " to_char(enDet.consultation_start_time,'HH24:MI:SS') as \"consultationStartTime\", "
							+ " to_char(enDet.consultation_stop_time,'HH24:MI:SS') as \"consultationStopTime\", "
							+ " to_char(enDet.consultation_start_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", "
							+ " to_char(enDet.consultation_stop_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", "
							+ " refrlTypeMst.referral_type_name as \"referralTypeName\", "
							+ " reflMst.referral_name as \"referralName\", "
							+ " refrDtls.referral_remark as \"referralRemark\", "
							+ "adm.admission_id as \"admissionId\", "
							+ " to_char(enDet.assignment_start_time,'HH:MM:SS') as \"assignmentStartTime\", "
							+ " to_char(enDet.assignment_start_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", "
							+ " to_char(enDet.assignment_stop_time,'HH:MM:SS') as \"assignmentStopTime\", "
							+ " to_char(enDet.assignment_stop_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", "
							+ " enDet.assignment_start_by as \"assignmentStartBy\", "
							+ " enDet.assignment_stop_by as \"assignmentStopBy\", "
							+ " enDet.assignment_start_role_id as \"assignmentStartRoleId\", "
							+ " enDet.assignment_stop_role_id as \"assignmentStopRoleId\", "
							+ " enDet.consultation_start_by as \"consultationStartBy\", "
							+ " enDet.consultation_start_role_id as \"consultationStartRoleId\", "
							+ " enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", "
							+ " spd.contract_id as \"contractId\", "
							+ " spd.payee_id as \"payeeId\", "
							+ " enMst.doc_queue_number as \"queueNumber\", "
							+ " enMst.unit_token_number as \"tokenNumber\", "
							+ " enDet.is_priority as \"isPriority\", "
							+ " enMst.is_critical as \"isCritical\", "
							+ " doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", "
							+ " enMst.speciality_queue_number as \"specialityQueueNumber\",  "
							+ " speciaityMapper.dept_wise_appointment as \"deptWiseAppointment\", "
							+ "cast((select count(billing_master_id) from billing.m_billing_master where encounter_id=enMst.encounter_id and bill_cancelled = 'N') as integer) as \"billId\", "
							+ "enMst.old_doc_id as \"oldDocId\", "
							+ "(case "
							+ "  when enMst.is_critical = 'Y' then 1 "
							+ "  when enMst.is_critical = 'N' then 2 "
							+ " end) as \"isCriticalOrder\", "
							+ " (case "
							+ "  when enDet.is_priority = 'Y' then 1 "
							+ "  when enDet.is_priority is null then 2 "
							+ " end) as \"isPriorityOrder\", "
							+ " coalesce((select nursing_acceptance from adt.t_admission_details where status='A' and admission_id=( "
							+ "	select admission_id from adt.t_admission_note where encounter_id=enMst.encounter_id)),'N') as \"nursingAccepatance\" "
							+ " FROM "
							+ "	opd.t_encounter_ehc_consultation_master ec"
							+ " inner join public.t_order_details odtls on odtls.order_details_id=ec.order_details_id "
							+ " inner join  billing.t_package_consumption_master pkgcnsumtxnnmstr on pkgcnsumtxnnmstr.order_detail_id=ec.order_details_id and pkgcnsumtxnnmstr.status='A' " 
							+"  inner join billing.t_billing_details bdtls on bdtls.order_details_id=odtls.order_details_id " 
							+"  inner join billing.m_billing_master bm on bm.billing_master_id=bdtls.billing_master_id and bm.bill_cancelled ='N' "
							+ " inner join opd.t_encounter_master enMst on 	ec.encounter_id = enMst.encounter_id "
							+ " LEFT JOIN opd.t_encounter_details enDet on enDet.encounter_id= enMst.encounter_id "
							+ " LEFT JOIN opd.m_appointment_master am on am.appointment_id=enMst.appointment_id "
							+ " LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
							+ " LEFT JOIN public.t_referal_details refrDtls on refrDtls.encounter_id=enMst.encounter_id "
							+"  LEFT JOIN adt.m_referral_type_master refrlTypeMst on refrlTypeMst.referral_type_id=refrDtls.referral_type_id "
							+"  LEFT JOIN adt.m_referral_master reflMst on reflMst.referral_id=refrDtls.referral_id "
							+ " LEFT JOIN opd.t_patient_sponsor_details spd on spd.encounter_id=enMst.encounter_id AND spd.priority_id=1 and spd.status='A' "
							+ " LEFT JOIN public.m_clinic_master clinMst on  clinMst.clinic_master_id=enMst.clinic_id "
							+ " LEFT JOIN opd.t_nursing_station_clinic_mapper nurStatClinMapp on nurStatClinMapp.clinic_id=clinMst.clinic_master_id "
							+ " LEFT OUTER JOIN doctor.m_speciality_master speciaityMaster on enMst.dept_id=speciaityMaster.speciality_id "
							+ " left outer join t_unit_speciality_mapper speciaityMapper on " 
							+ " 	enMst.dept_id = speciaityMapper.speciality_id " 
							+ " 	and speciaityMapper.unit_id = enMst.unit_id " 
							+ " LEFT OUTER JOIN doctor.m_doctor_master doc on enMst.doctor_id=doc.doctor_id "
							+ " LEFT OUTER JOIN public.m_modality_master modality on sm.modality_id=modality.modality_id "
							+ " LEFT JOIN patient.t_patient_registration patient on enMst.patient_id=patient.patient_id "
							+ " LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
							+ " LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
							+ " LEFT JOIN opd.m_appointment_status_master asm on asm.appointment_status_id=enMst.status_id "
							+ " LEFT OUTER JOIN adt.t_admission adm on adm.admission_id = am.admission_id  AND adm.status='A' "
							+ " WHERE enMst.organization_id=:orgId "
							+ " AND enMst.unit_id=:unitId "+ 
							 "  AND enMst.status_id in (5,6) "
							+ " AND patient.registration_type_id IN (1,3,4,6) "
//							+ " AND am.appointment_type_id IN (3) "
							+ " AND bm.bill_status_id <> 1 ";
							
			                 slotMasterDtosList = sessionFactory.getCurrentSession()
						    .createSQLQuery(query)
							.setParameter("orgId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, slotMasterDtosList, null);
			}catch (HibernateException he) {
				logger.error("Exection", he);
			} catch (Exception e) {
			logger.error("getEHCPatientsList failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public ContractConsumptionDto getServiceRateByCompanyId(ContractConsumptionDto consumptionDto)throws ApplicationException {
		try {
				 consumptionDto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTARCT_CONSUMPTION_SERVICE_DETAILS_BY_CONTARCTID_SERVICEID")
					.setParameter("contractId",consumptionDto.getContractId())
					.setParameter("serviceId", consumptionDto.getServiceId())
					.setParameter("billingBedCategoryid", consumptionDto.getBillingBedCategoryId())
					.setParameter("visitTypeId", consumptionDto.getVisitTypeId())
					.setParameter("organizationId", consumptionDto.getOrganizationId())
					.setParameter("unitId", consumptionDto.getUnitId())
					.setParameter("patientTypeId", consumptionDto.getPatientTypeId())
					.setParameter("currentDate", GlobalCommonDateUtils.getDate(consumptionDto.getCurrentDate(), "yyyy-M-dd"))
					.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
				.uniqueResult();
			return consumptionDto;
		}catch(HibernateException e){
		logger.error("getServiceRateByCompanyId failed", e);
		}
		return null;
	}
	
	
	@Override
	public Response getRadiologyListForSampleCollection(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try{
		
		List<RadiologyOrdersListDto> listServiceMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_REDIOLOGY_LIST_FOR_SAMPLE_COLLECTION")
				.setParameter("unitId", radiologyOrdersListDto.getUnitId())
				.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
				.setParameter("radiologySpecialityId", radiologyOrdersListDto.getRadiologySpecialityId())
				.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
	}catch (Exception e) {
	logger.error("getRadiologyListForSampleCollection failed", e);
	}
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getPatientDocDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ENCOUNTER_DOC_DETAILS").getQueryString();

			StringBuilder sb = new StringBuilder(query);

			if (patientDocReqDto.getVisitTypeId() != null) {
				if (patientDocReqDto.getVisitTypeId().equals(1)) {
					sb.append(" AND (doc_dtls.encounter_id = :encounterId OR doc_dtls.patient_id = :patientId)");
				} else if (patientDocReqDto.getVisitTypeId().equals(2)) {
					sb.append(" AND (doc_dtls.admission_id = :admissionId OR doc_dtls.patient_id = :patientId)");
				}
			}

			if (patientDocReqDto.getLabSampleDtlsId() != null && patientDocReqDto.getLabSampleDtlsId() > 0) {
				sb.append(" AND doc_dtls.lab_sample_dtls_id = :labSampleDtlsId");
			}

			if (patientDocReqDto.getExpenseId() != null && patientDocReqDto.getExpenseId() > 0) {
				sb.append(" AND doc_dtls.expense_id = :expenseId");
			}

			if (patientDocReqDto.getCompanyReceiptsExpenseId() != null && patientDocReqDto.getCompanyReceiptsExpenseId() > 0) {
				sb.append(" AND doc_dtls.company_expense_id = :companyReceiptsExpenseId");
			}

			if (patientDocReqDto.getGrnId() != null && patientDocReqDto.getGrnId() > 0) {
				sb.append(" AND doc_dtls.grn_id = :grnId");
			}

			if (patientDocReqDto.getPoId() != null && patientDocReqDto.getPoId() > 0) {
				sb.append(" AND doc_dtls.po_id = :poId");
			}

			if (patientDocReqDto.getIncidentReportingMapperId() != null && patientDocReqDto.getIncidentReportingMapperId() > 0) {
				sb.append(" AND doc_dtls.grn_id = :incidentReportingMapperId");
			}

			if (patientDocReqDto.getDeathPatientId() != null && patientDocReqDto.getDeathPatientId() > 0) {
				sb.append(" AND doc_dtls.death_patient_id = :deathPatientId");
			}

			if (patientDocReqDto.getOpbId() != null && patientDocReqDto.getOpbId() > 0) {
				sb.append(" AND doc_dtls.opb_id = :opbId");
			}

			if (patientDocReqDto.getVendorId() != null && patientDocReqDto.getVendorId() > 0) {
				sb.append(" AND doc_dtls.vendor_id = :vendorId");
			}

			if (patientDocReqDto.getPatientId() != null && patientDocReqDto.getPatientId() > 0) {
				sb.append(" AND doc_dtls.patient_id = :patientId");
			}

			sb.append(" ORDER BY doc_dtls.created_date DESC");

			Query querya = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("unitId", patientDocReqDto.getUnitId())
					.setParameter("orgId", patientDocReqDto.getOrganisationId());

			if (patientDocReqDto.getVisitTypeId() != null) {
				if (patientDocReqDto.getVisitTypeId().equals(1)) {
					querya.setParameter("encounterId", patientDocReqDto.getEncounterId());
				} else if (patientDocReqDto.getVisitTypeId().equals(2)) {
					querya.setParameter("admissionId", patientDocReqDto.getAdmissionId());
				}
			}

			if (patientDocReqDto.getLabSampleDtlsId() != null && patientDocReqDto.getLabSampleDtlsId() > 0) {
				querya.setParameter("labSampleDtlsId", patientDocReqDto.getLabSampleDtlsId());
			}

			if (patientDocReqDto.getExpenseId() != null && patientDocReqDto.getExpenseId() > 0) {
				querya.setParameter("expenseId", patientDocReqDto.getExpenseId());
			}

			if (patientDocReqDto.getCompanyReceiptsExpenseId() != null && patientDocReqDto.getCompanyReceiptsExpenseId() > 0) {
				querya.setParameter("companyReceiptsExpenseId", patientDocReqDto.getCompanyReceiptsExpenseId());
			}

			if (patientDocReqDto.getGrnId() != null && patientDocReqDto.getGrnId() > 0) {
				querya.setParameter("grnId", patientDocReqDto.getGrnId());
			}

			if (patientDocReqDto.getPoId() != null && patientDocReqDto.getPoId() > 0) {
				querya.setParameter("poId", patientDocReqDto.getPoId());
			}

			if (patientDocReqDto.getIncidentReportingMapperId() != null && patientDocReqDto.getIncidentReportingMapperId() > 0) {
				querya.setParameter("incidentReportingMapperId", patientDocReqDto.getIncidentReportingMapperId());
			}

			if (patientDocReqDto.getDeathPatientId() != null && patientDocReqDto.getDeathPatientId() > 0) {
				querya.setParameter("deathPatientId", patientDocReqDto.getDeathPatientId());
			}

			if (patientDocReqDto.getOpbId() != null && patientDocReqDto.getOpbId() > 0) {
				querya.setParameter("opbId", patientDocReqDto.getOpbId());
			}

			if (patientDocReqDto.getVendorId() != null && patientDocReqDto.getVendorId() > 0) {
				querya.setParameter("vendorId", patientDocReqDto.getVendorId());
			}

			if (patientDocReqDto.getPatientId() != null && patientDocReqDto.getPatientId() > 0) {
				querya.setParameter("patientId", patientDocReqDto.getPatientId());
			}

			List<PatientDocDto> listPatientDocDto = querya.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();

			if (listPatientDocDto != null && listPatientDocDto.size() > 0) {

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();

				for(PatientDocDto dto : listPatientDocDto) {
					if (dto.getVideolink() != null && !dto.getVideolink().isEmpty()) {
						if (dto.getAnalyzedDocDate() != null) {
							String dateStr = dto.getAnalyzedDocDate().toString();
							String trimmedDateStr = dateStr.split("\\.")[0];
							LocalDateTime fetchedDate = LocalDateTime.parse(trimmedDateStr, dtf);
							LocalDateTime sevenDaysAgo = now.minusDays(7);
							if (fetchedDate.isBefore(sevenDaysAgo)) {
								dto.setVideolink(null);
								dto.setAnalyzedDocDate(null);
							}
						}
					}
				}
			}

			if(patientDocReqDto.getPatientId()!=null) {
				StringBuilder querytest = new StringBuilder();
				querytest.append("select lsdtl.lab_report_url as \"documentPath\", "
						+ " 'Pathology Report' AS \"fileName\", "
						+ " 'Pathology' AS \"documentName\", "
						+ " to_char(lsdtl.created_date,'DD-MM-YYYY') as \"createdDate\" "
						+ " from lab.t_lab_sample_master lsm "
						+ " inner join lab.t_lab_sample_dtls  lsdtl on "
						+ " lsdtl.lab_sample_id = lsm.lab_sample_id  "
						+ " where lsm.patient_id  =:patientId "
						+ " and lsdtl.lab_report_url is not null");
				List<PatientDocDto> listPatientDocDtoNew = sessionFactory.getCurrentSession()
						.createSQLQuery(querytest.toString())
						.setParameter("patientId", patientDocReqDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();
				if (listPatientDocDtoNew != null && listPatientDocDtoNew.size() > 0) {
					listPatientDocDto.addAll(listPatientDocDtoNew);
				}

				StringBuilder queryRadiology = new StringBuilder();
				queryRadiology.append("select ttm.radiology_report_link as \"documentPath\", 'Radiology Report' as \"fileName\", 'Radiology' AS \"documentName\", to_char(ttm.created_date,'DD-MM-YYYY') as \"createdDate\" from public.t_template_transaction ttm where ttm.patient_id =:patientId ");
				List<PatientDocDto> listPatientDocDtoRadiaology = sessionFactory.getCurrentSession()
						.createSQLQuery(queryRadiology.toString())
						.setParameter("patientId", patientDocReqDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();

				if (listPatientDocDtoRadiaology != null && listPatientDocDtoRadiaology.size() > 0) {
					listPatientDocDto.addAll(listPatientDocDtoRadiaology);
				}
			}
				   
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientDocDto, null);
		} catch (Exception e) {
			throw e;
		}
	//	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getInsuranceDocByPatientIDDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException {
			
			try{
				   StringBuilder query = new StringBuilder();
				   query.append("select "
							+ " doc_type_mst.document_type_name as \"documentName\", "
							+ " ubc.bucket_flag as \"bucketFlag\", "
							+ " doc_dtls.document_path as \"documentPath\", "		
                            + " doc_dtls.document_type_id as \"docTypeId\" "	
							+ "from "
							+ " opd.t_encounter_doc_details doc_dtls "
							+ "inner join public.m_document_type_master doc_type_mst on "
							+ " doc_dtls.document_type_id = doc_type_mst.document_type_id "
							+ "inner join public.t_unit_application_billing_configuration ubc on "
							+ " doc_dtls.unit_id = ubc.unit_id "
							+ "where "
							+ " doc_dtls.patient_id = :patientId "
							+ " and doc_dtls.organisation_id = :orgId "
							//+ " and doc_dtls.document_type_id = :docTypeId "
							+ " and doc_dtls.unit_id = :unitId ");
				if (patientDocReqDto.getPreauthRequestId() != null) {
					query.append(" and doc_dtls.preauth_request_id = :preauthRequestId ");
				}
				   query.append(" order by doc_dtls.created_date desc");
				   Query  createQuery  = sessionFactory.getCurrentSession()
						   .createSQLQuery(query.toString())
						   .setParameter("orgId", patientDocReqDto.getOrganisationId())
						   .setParameter("unitId", patientDocReqDto.getUnitId())
						   .setParameter("patientId", patientDocReqDto.getPatientId());
				if (patientDocReqDto.getPreauthRequestId() != null) {
					createQuery.setParameter("preauthRequestId", patientDocReqDto.getPreauthRequestId());
				}

				List<PatientDocDto> listPatientDocDto =createQuery.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();
				   
				   return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientDocDto, null);
			  
			  }catch (Exception e) {
			  logger.error("getInsuranceDocByPatientIDDetails failed", e);
			  }
			  return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getOpPatientListByDocId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			List<Integer> deptIds = new LinkedList();
			String query = " select "
					+ " 	spl.speciality_id as \"specialityId\" "
					+ " from "
					+ " 	doctor.t_doctor_speciality_mapper dsm "
					+ " inner join t_unit_speciality_mapper spl on "
					+ " 	spl.speciality_id = dsm.speciality_id "
					+ " where "
					+ " 	 dsm.status = 'A' "
					+ "	and spl.unit_id =:unitId "
					+ " 	and spl.dept_wise_appointment='Y' ";

			if(encounterMasterDto.getIsRMO()!=null) {
				query=query.concat(" and dsm.doctor_id in (:doctorList) ");
			}else {
				query=query.concat(" and dsm.doctor_id =:doctorId ");
			}

			List<DoctorSpecialityMapperDto> list=new ArrayList<DoctorSpecialityMapperDto>();

			if(encounterMasterDto.getIsRMO()!=null) {
				list = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("doctorList", encounterMasterDto.getDoctorList())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			}else {
				Query SQL= sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", encounterMasterDto.getUnitId());
				SQL.setParameter("doctorId",encounterMasterDto.getDoctorId());
				list =SQL.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			}

			if(list.size()>0)
			{
				for(DoctorSpecialityMapperDto dto :list)
				{
					deptIds.add(dto.getSpecialityId());
				}
			}

			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST").getQueryString().toString());


			if(encounterMasterDto.getIsRMO()!=null){
				if(!list.isEmpty()) {
					stringQuery.append("AND (((enMst.dept_id IN(:deptIds) and enMst.preferred_doc_id is null and enMst.doctor_id is null ) "
							+ " OR enMst.preferred_doc_id in(:doctorList))  OR enMst.doctor_id IN (:doctorList)) ");
				}else {
					stringQuery.append(" AND enMst.doctor_id IN (:doctorList) ");
				}
			}else if(list.size()>0){
				stringQuery.append("AND (((enMst.dept_id IN (:deptIds) and enMst.preferred_doc_id is null and enMst.doctor_id is null ) OR enMst.preferred_doc_id =:doctorId )");
				stringQuery.append(" OR enMst.doctor_id=:doctorId) ");
			}else{
				stringQuery.append(" AND enMst.doctor_id=:doctorId ");
			}

			if(encounterMasterDto.getStatusId()!=null && encounterMasterDto.getStatusId()>0)
			{
				if(encounterMasterDto.getStatusId() == 1)
					stringQuery.append(" AND enDet.consultation_start_time is null");
				if(encounterMasterDto.getStatusId() == 2)
					stringQuery.append(" AND enDet.consultation_start_time is not null AND enDet.consultation_stop_time is null ");
				if(encounterMasterDto.getStatusId() == 3)
					stringQuery.append(" AND enDet.consultation_start_time is not null AND enDet.consultation_stop_time is not null ");
			}

			stringQuery.append(" AND (enDet.assignment_start_time is not null or (am.is_teleconsultation_apppointment = 1 "
					+ " 	and mbm.bill_status_id = 3 and enMst.status_id in (5,6,9))) ");
			stringQuery.append(" AND enMst.encounter_date >= CAST(:fromDate AS DATE) ");
			stringQuery.append(" AND enMst.encounter_date < CAST(:toDate AS DATE) + INTERVAL '1 day' ");
			//stringQuery.append((encounterMasterDto.getStatusId() > 0 ) ? " AND enMst.status_id =" + encounterMasterDto.getStatusId() : " ");
			//stringQuery.append(" order by enMst.encounter_date desc, enDet.is_priority = 'Y',enMst.is_critical = 'Y',EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob))>60 desc,\"specialityQueueNumber\" ");
			stringQuery.append(" order by Firstentmst.encounter_date  asc ");
			//stringQuery.append(" order by Firstentmst.encounter_date  asc,enMst.encounter_date asc, enDet.is_priority desc,enMst.is_critical desc,EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) > 60 desc,\"specialityQueueNumber\" ");
			Integer count=null;
			List<SlotMasterDto> slotMasterDtosList=new ArrayList<>();
			List<SlotMasterDto> slotMasterDtosList1 =new ArrayList<>();

			if(encounterMasterDto.getIsRMO()!=null){
				if(!list.isEmpty()) {
					slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
							.setParameter("orgId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
							.setParameterList("deptIds", deptIds)
							.setParameterList("doctorList", encounterMasterDto.getDoctorList())
							.setParameter("doctorId", encounterMasterDto.getDoctorId())
							.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
							.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
							.setFirstResult(encounterMasterDto.getOffset() != null ? encounterMasterDto.getOffset()  : 0)
							.setMaxResults(encounterMasterDto.getNoOfRecordsPerPage() != null ? encounterMasterDto.getNoOfRecordsPerPage()  : 10)
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

					slotMasterDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
							.setParameter("orgId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
							.setParameterList("deptIds", deptIds)
							.setParameter("doctorId", encounterMasterDto.getDoctorId())
							.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
							.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
							.setParameterList("doctorList", encounterMasterDto.getDoctorList())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				}else {
					slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
							.setParameter("orgId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
					//		.setParameter("doctorId", encounterMasterDto.getDoctorId())
							.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
							.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
							.setParameterList("doctorList", encounterMasterDto.getDoctorList())
							.setFirstResult(encounterMasterDto.getOffset() != null ? encounterMasterDto.getOffset()  : 0)
							.setMaxResults(encounterMasterDto.getNoOfRecordsPerPage() != null ? encounterMasterDto.getNoOfRecordsPerPage()  : 10)
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

					slotMasterDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
							.setParameter("orgId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
					//		.setParameter("doctorId", encounterMasterDto.getDoctorId())
							.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
							.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
							.setParameterList("doctorList", encounterMasterDto.getDoctorList())
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				}
				count= slotMasterDtosList1.size();
			}else if(list.size()>0){
				slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameterList("deptIds", deptIds)
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
						.setFirstResult(encounterMasterDto.getOffset() != null ? encounterMasterDto.getOffset()  : 0)
						.setMaxResults(encounterMasterDto.getNoOfRecordsPerPage() != null ? encounterMasterDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				slotMasterDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameterList("deptIds", deptIds)
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				count=slotMasterDtosList1.size();
			}else{
				slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10))
						.setFirstResult(encounterMasterDto.getOffset() != null ? encounterMasterDto.getOffset() : 0)
						.setMaxResults(encounterMasterDto.getNoOfRecordsPerPage() != null ? encounterMasterDto.getNoOfRecordsPerPage() : 10)
						.setFirstResult(encounterMasterDto.getOffset() != null ? encounterMasterDto.getOffset()  : 0)
						.setMaxResults(encounterMasterDto.getNoOfRecordsPerPage() != null ? encounterMasterDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				slotMasterDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				count = slotMasterDtosList1.size();
			}




//			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
			return new Response(SUCCESS,SUCCESS_CODE , SUCCESS_MESSAGE,
					slotMasterDtosList, null,count,slotMasterDtosList1) ;
		}catch (Exception e) {
		logger.error("getOpPatientListByDocId failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Integer getPreviousDocQueueNumber(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			
			String query = "SELECT MAX(doc_queue_number) as \"docQueueNumber\" "
					+ " FROM opd.t_encounter_master "
					+ " WHERE unit_id=:unitId "
					+ " AND doctor_id=:doctorId "
					+ " AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
					+ " AND organization_id=:orgId ";
			
			encounterMasterDto = (EncounterMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("encounterDate", GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterDate(), "yyyy-MM-dd"))
					//.setParameter("encounterDate",GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterDate(), "yyyy-M-dd"))
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return encounterMasterDto.getDocQueueNumber();
		} catch (Exception e) {
		logger.error("getPreviousDocQueueNumber failed", e);
		}
		return null;
	}

	@Override
	public Integer getPreviousUnitTokenNumber(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			
			String query = "SELECT max(unit_token_number) as \"unitTokenNumber\" "
					+ " FROM opd.t_encounter_master "
					+ " WHERE unit_id=:unitId "
					+ " AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
					+ " AND organization_id=:orgId ";
			
			encounterMasterDto = (EncounterMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("encounterDate", GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterDate(), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return encounterMasterDto.getUnitTokenNumber();
		} catch (Exception e) {
		logger.error("getPreviousUnitTokenNumber failed", e);
		}
		return null;

	}

	@Override
	public Response getTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try{
			
			List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOKEN_LIST")
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("encounterDate", encounterMasterDto.getCurrentDate().substring(0,10))
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getTokenList failed", he);
		} catch (Exception e) {
		logger.error("getTokenList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getNursingTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try{
			
			List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOKEN_LIST_NURSING")
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("encounterDate", encounterMasterDto.getCurrentDate().substring(0,10))
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getNursingTokenList failed", he);
		} catch (Exception e) {
		logger.error("getNursingTokenList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Integer getPreviousSpecialityQueueNumber(EncounterMasterDto encounterMasterDto) throws ApplicationException {
			try {
			
			String query = "SELECT MAX(speciality_queue_number) as \"specialityQueueNumber\" "
					+ " FROM opd.t_encounter_master "
					+ " WHERE unit_id=:unitId "
					+ " AND dept_id=:specialityId "
					+ " AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
					+ " AND organization_id=:orgId ";
			
			encounterMasterDto = (EncounterMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("specialityId", encounterMasterDto.getSpecialityId())
					.setParameter("encounterDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return encounterMasterDto.getSpecialityQueueNumber();
		} catch (Exception e) {
		logger.error("getPreviousSpecialityQueueNumber failed", e);
		}
		return null;
	}

	@Override
	public Response updatePatientCritical(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
					+ "isCritical=:isCritical "
					+ "WHERE encounterId=:encounterId")
				.setCharacter("isCritical", encounterMasterDto.getIsCritical())
				.setParameter("encounterId", encounterMasterDto.getEncounterId())
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("updatePatientCritical failed", he);
		} catch (Exception e) {
		logger.error("updatePatientCritical failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response opPatientListByDocIdForCasualty(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST_FOR_CASUALTY").getQueryString().toString());

			stringQuery.append(" AND enDet.assignment_start_time is not null ");
			stringQuery.append(" AND to_char(enMst.encounter_date,'yyyy-mm-dd') Between :fromDate and :toDate ");
			stringQuery.append(" order by \"specialityQueueNumber\" ");
			List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
				.setParameter("orgId", encounterMasterDto.getOrganizationId())
				.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0,10))
					.setParameter("toDate", encounterMasterDto.getToDate().substring(0,10))

				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			Integer count=slotMasterDtosList.size();
			return new Response(SUCCESS, null, null, slotMasterDtosList, count);
			}catch (Exception e) {
			logger.error("opPatientListByDocIdForCasualty failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorIdForEncouner(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			if(encounterMasterDto.getCheckInFromDoctorApp()!=null && encounterMasterDto.getCheckInFromDoctorApp().equals('Y')) {
				sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
						+ "doctorId=:doctorId, "
						+ "checkInFromDoctorApp=:checkInFromDoctorApp, "
						+ "doctorDeskArrival=:doctorDeskArrival "
						+ "WHERE encounterId=:encounterId")
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setCharacter("checkInFromDoctorApp", 'Y')
					.setCharacter("doctorDeskArrival", 'Y')
				.executeUpdate();
			}else {
				sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
						+ "doctorId=:doctorId, "
						+ "cabinId=:cabinId, "
						+ "doctorDeskArrival=:doctorDeskArrival "
						+ "WHERE encounterId=:encounterId")
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("cabinId", encounterMasterDto.getCabinId())
					.setCharacter("doctorDeskArrival", encounterMasterDto.getDoctorDeskArrival())
				.executeUpdate();
			}
			
			
			if(encounterMasterDto.getOrderDetailId()!=null) {
				sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster orderDetObj set orderDetObj.serviceRendered=1"
						+ " WHERE orderDetObj.orderDetailsId=:orderDetailId")
					.setParameter("orderDetailId", encounterMasterDto.getOrderDetailId())
				.executeUpdate();
				}
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateDoctorIdForEncouner failed", he);
		} catch (Exception e) {
		logger.error("updateDoctorIdForEncouner failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response patientCalled(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
					+ "isCalled=:isCalled, "
					+ "callCount=0 "
					+ "WHERE encounterId=:encounterId")
				.setCharacter("isCalled", encounterMasterDto.getIsCalled())
				.setParameter("encounterId", encounterMasterDto.getEncounterId())
			.executeUpdate();
			
			if(encounterMasterDto.getOrderDetailId()!=null) {
			sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster orderDetObj set orderDetObj.serviceRendered=1"
					+ " WHERE orderDetObj.orderDetailsId=:orderDetailId")
				.setParameter("orderDetailId", encounterMasterDto.getOrderDetailId())
			.executeUpdate();
			
			 //for render package
		    Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId( encounterMasterDto.getOrderDetailId());
		    List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
		    
		    AdmissionsForBatchProcessDto vistDetailsObj=new AdmissionsForBatchProcessDto();
		     vistDetailsObj.setEncounterId(encounterMasterDto.getEncounterId());
		     
            for (OrderDetailsPackageMapperDto mpperDto : mpprList) {
            	OrderDetailsMasterDto  orderDetailsMasterDto=new OrderDetailsMasterDto();
	              if(mpperDto.getIsPackageService()=='Y')
	              {
	                    orderDetailsMasterDto.setServiceRendered(1);
	                    orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
	                    iMPackageMasterService.renderPackageServiceFrPostBillig(null,vistDetailsObj,mpperDto.getPackageId());
	              }
            	}
			}
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("patientCalled failed", he);
		} catch (Exception e) {
		logger.error("patientCalled failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try{
			
			if(encounterMasterDto.getDoctorId() == null ) {
				List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DOCTOR_TOKEN_LIST")
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameter("encounterDate", encounterMasterDto.getCurrentDate().substring(0,10))
						.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}else {
				List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DOCTOR_TOKEN_LIST_DOCTOR_ID")
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("encounterDate", encounterMasterDto.getCurrentDate().substring(0,10))
						.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}
				
		} catch (HibernateException he) {
		logger.error("getDoctorTokenList failed", he);
		} catch (Exception e) {
		logger.error("getDoctorTokenList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getCheckedInPatientByDoctorId(EncounterMasterDto encounterMasterDto) throws ApplicationException{
		try{
			Date date = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId());  
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		    String strDate= formatter.format(date);  
		    //System.out.println(strDate);  
			List<Integer> roleId=sessionFactory.getCurrentSession()
					.createSQLQuery("select user_id from adt.t_user_nursing_station_mapper  "
							+ " where user_id=:doctorId ").setParameter("doctorId",encounterMasterDto.getDoctorId()).list();
			if(roleId.size()>0) {
				List<EncounterVitalDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PATIENT_CHECKED_IN_LIST_OF_NURSE")
						.setParameter("orgId", encounterMasterDto.getOrganizationId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameter("userId", encounterMasterDto.getDoctorId())
						//.setParameter("updateDate", GlobalCommonDateUtils.getDate(encounterMasterDto.getEncounterDateString2(), "yyyy-MM-dd"))
						.setParameter("updateDate",strDate)
						.setResultTransformer(Transformers.aliasToBean(EncounterVitalDto.class)).list();
				for(EncounterVitalDto l:list) {
					l.setRoleId("3");
					}
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}
			else {
			    
			List<EncounterVitalDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_CHECKED_IN_LIST_BYDOCTORID")
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("userId", encounterMasterDto.getDoctorId())
					//.setParameter("updateDate", GlobalCommonDateUtils.getDate(encounterMasterDto.getEncounterDateString2(), "yyyy-MM-dd"))
					.setParameter("updateDate",strDate)
					.setResultTransformer(Transformers.aliasToBean(EncounterVitalDto.class)).list();
			for(EncounterVitalDto l:list) {
			l.setRoleId("2");
			}
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}
			
		} catch (HibernateException he) {
		logger.error("getCheckedInPatientByDoctorId failed", he);
		} catch (Exception e) {
		logger.error("getCheckedInPatientByDoctorId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCallCount(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
					+ "callCount=:callCount "
					+ "WHERE encounterId=:encounterId")
				.setParameter("callCount", encounterMasterDto.getCallCount())
				.setParameter("encounterId", encounterMasterDto.getEncounterId())
			.executeUpdate();
			
			
			if(encounterMasterDto.getNursingStationId()!= null && encounterMasterDto.getNursingStationId()!= 0) {
				
				sessionFactory.getCurrentSession().createSQLQuery("update  opd.t_encounter_master  set nursing_station_id =:nursingStationId "
						+ " where  encounter_id =:encounterId")
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("nursingStationId", encounterMasterDto.getNursingStationId())
				.executeUpdate();
			}
			
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("updateCallCount failed", he);
		} catch (Exception e) {
		logger.error("updateCallCount failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getListOfRadiologyTests(Integer admissionId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try{
			
			List<RadiologyOrdersListDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_REDIOLOGY_TESTS")
					.setParameter("admissionId", admissionId)
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
					.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch (Exception e) {
		logger.error("getListOfRadiologyTests failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	@Override
	public Response updateDoctorFromNursingWorkList(Integer encounterId,Integer oldDocId, Integer docId, Integer reasonId)
			throws ApplicationException {
		try {
			
			if(reasonId>0){
			sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
					+ "preferredDocId=:doctId, "
					+ "reasonIdForDocChange=:reasonId, "
					+ "oldDocId=:oldDocId "
					+ "WHERE encounterId=:encounterId ")
				.setParameter("doctId", docId)
				.setParameter("encounterId",encounterId)
				.setParameter("reasonId", reasonId)
				.setParameter("oldDocId", oldDocId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
			}
			else{
			sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
					+ "preferredDocId=:doctId, "
					+ "oldDocId=:oldDocId "
					+ "WHERE encounterId=:encounterId ")
				.setParameter("doctId", docId)
				.setParameter("encounterId",encounterId)
				.setParameter("oldDocId", oldDocId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
			}
		} catch (HibernateException he) {
		logger.error("updateDoctorFromNursingWorkList failed", he);
		} catch (Exception e) {
		logger.error("updateDoctorFromNursingWorkList failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getTokenMsgList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
          try{
			
			List<BroadcastMessageDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOKEN_MESSAGE_LIST")
					.setParameter("type", encounterMasterDto.getAppointmentTypeId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BroadcastMessageDto.class)).list();
			
			if(list.size() > 0)
			{
				sessionFactory.getCurrentSession().createSQLQuery("update opd.t_broadcast_msg set "
						+ "status='I'"
						+ "WHERE id=:id ")
					.setParameter("id", list.get(0).getId())
				.executeUpdate();
			}
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getTokenMsgList failed", he);
		} catch (Exception e) {
		logger.error("getTokenMsgList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEnounterDataForEMR(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_ENCOUNTER_DATA_FOR_EMR").getQueryString());
			
			if(encounterMasterDto.getFromDate()!=null && encounterMasterDto.getToDate()!=null)
				query.append("AND to_char(enMst.encounter_date,'yyyy-mm-dd') between :fromDate AND :toDate");
			
			if(encounterMasterDto.getPatientName()!=null)
			{
				query.append("AND ( lower(concat(coalesce(patient.first_name,''),coalesce(patient.last_name,''))) LIKE :patientName ");
				query.append("OR lower(patient.uhid_number) LIKE :patientName");
			}
			
			if(encounterMasterDto.getClinicId()!=null)
				query.append("AND and clinMst.clinic_id=:clinicId ");
			
			query.append(" order by enMst.encounter_id desc ");
			
			Query sqlQuery  = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getUnitId())
					.setParameter("docId", encounterMasterDto.getDoctorId());
			if (encounterMasterDto.getFromDate() != null && encounterMasterDto.getToDate() != null) {
				sqlQuery.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10));
			}

			if (encounterMasterDto.getPatientName() != null) {
				String patientNameParam = encounterMasterDto.getPatientName().toLowerCase() + "%";
				sqlQuery.setParameter("patientName", patientNameParam)
						.setParameter("patientUhid", "%" + encounterMasterDto.getPatientName().toLowerCase() + "%");
			}

			if (encounterMasterDto.getClinicId() != null) {
				sqlQuery.setParameter("clinicId", encounterMasterDto.getClinicId());
			}
			List<SlotMasterDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch (Exception e) {
		logger.error("getEnounterDataForEMR failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	@Override
	public Response savePACSDetails(PACSDto dto) throws ApplicationException {
		try{
			
			//System.out.println("dto detials="+dto.getData());
			
			   JSONObject jObject = null;
		       jObject = new JSONObject(dto.getData());
		      // JSONObject geoObjectHeader= jObject.getJSONObject("Header");
		       JSONObject geoObjectObservation = jObject.getJSONObject("Observation");
		       JSONObject geoObjectObservationResult = jObject.getJSONObject("Observationresult");
		       JSONObject geoObjectOrderDetails = jObject.getJSONObject("OrderDetails");
		   
		       
		       
		       PacsResultModel model=new PacsResultModel();
		        	model.setSetId(geoObjectObservationResult.getString("obx_1_setid"));
		        	model.setValueType(geoObjectObservationResult.getString("obx_2_valuetype"));
		        	model.setObservationIdentifier(geoObjectObservationResult.getString("obx_3_observationidentifier"));
		        	model.setObservationValue(geoObjectObservationResult.getString("obx_5_observationvalue"));
		        	model.setUnits(geoObjectObservationResult.getString("obx_6_units"));
		        	model.setObservationResultStatus(geoObjectObservationResult.getString("obx_11_observationresultstatus"));
		        	model.setEffectivedateLastObservationNormalValues(geoObjectObservationResult.getString("obx_12_effectivedatelastobservationnormalvalues"));
		        	model.setDatetimeofObservation(geoObjectObservationResult.getString("obx_14_datetimeofobservation"));
		        	model.setProducerid(geoObjectObservationResult.getString("obx_15_producerid"));
		        	model.setResponsibleObserver(geoObjectObservationResult.getString("obx_16_responsibleobserver"));
		        													
		       		model.setAccessNumber(geoObjectObservation.getString("obr_4_universalserviceidentifier"));
		       		model.setOrderNo(geoObjectOrderDetails.getString("orc_2_placerordernumber"));
//		       		model.setAbnormalFlags(geoObjectObservationResult.getString("obx_8_abnormalflags"));
//		       		model.setNatureOfAbnormalityTest(geoObjectObservationResult.getString("obx_10_natureofabnormalitytest"));
//		       		model.setObservationSubId(geoObjectObservationResult.getString("obx_4_observationsubid"));
//		       		model.setProbability(geoObjectObservationResult.getString("obx_9_probability"));
//		       		model.setReferencerange(geoObjectObservationResult.getString("obx_7_referencerange"));
//		       		model.setUserdefinedAccessChecks(geoObjectObservationResult.getString("obx_13_userdefinedaccesschecks"));
		       		
		       		/*model.setOrganizationId((orgUnitArray[0]!=null && orgUnitArray[0]!="") ? Integer.parseInt(orgUnitArray[0]) :1 ); 
		       		model.setUnitId((orgUnitArray[0]!=null && orgUnitArray[0]!="") ? Integer.parseInt(orgUnitArray[0]) :1 );*/
		       		sessionFactory.getCurrentSession().save(model);
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("savePACSDetails failed", he);
		} catch (Exception e) {
		logger.error("savePACSDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPendingServicesOfEncounter(Integer encounterId) throws ApplicationException {
		try {
			List<OrderDetailsByEncounterIdDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_SERVICES_BY_ENCOUNTER_ID")
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
		logger.error("getPendingServicesOfEncounter failed", he);
		} catch (Exception e) {
		logger.error("getPendingServicesOfEncounter failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	public Response getNephrologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
	try {
			//for op pre billing
			StringBuilder OPquery = new StringBuilder(" select " 
					 + " 	distinct (ordDet.order_details_id) as \"orderDetailsId\", " 
					 + " 	sm.slot_id as \"slotId\", " 
					 + " 	adm.admission_id as \"admissionId\", " 
					 + " 	ordDet.ord_doc_id as \"ordDoctorId\", " 
					 + " 	ordDet.status as \"status\", " 
					 + " 	ordDet.service_rendered as \"serviceRendered\", " 
					 + " 	patReg.patient_id as \"patientId\", " 
					 + " 	patReg.uhid_number as \"uhidNumber\", " 
					 + " 	patReg.gender_id as \"genderId\", " 
					 + " 	genMas.gender_code as \"genderCode\", " 
					 + " 	preMas.prefix_code as \"prefixCode\", " 
					 + " 	to_char(patReg.dob, 'DD-MM-YYYY HH:mm:ss') as \"dob\", " 
					 + " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, patReg.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, patReg.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, patReg.dob)), 'D ') as \"ageString\", " 
					 + " 	encMas.encounter_id as \"encounterId\", " 
					 + " 	encMas.doctor_id as \"doctorId\", " 
					 + " 	('Dr. ' || coalesce(docMas.first_name)|| ' ' || coalesce(docMas.middle_name)|| ' ' || coalesce(docMas.last_name)) as \"doctorName\", " 
					 + " 	encMas.dept_id as \"specialityId\", " 
					 + " 	specMas.speciality_name as \"specialityName\", " 
					 + " 	servMast.service_standard_name as \"serviceName\", " 
					 + " 	servMast.service_master_id as \"serviceId\", " 
					 + " 	servMast.service_standard_code as \"serviceCode\", " 
					 + " 	ordDet.priority_id as \"priorityId\", " 
					 + " 	ordMas.order_no as \"orderNo\", " 
					 + " 	to_char(ordDet.order_date, 'dd/mm/yyyy') as \"orderDate\", " 
					 + " 	(preMas.prefix_code || ' ' || coalesce(patReg.first_name)|| ' ' || coalesce(patReg.middle_name)|| ' ' || coalesce(patReg.last_name)) as \"patientName\", " 
					 + " 	'-' as \"bedNumber\" , " 
					 + " 	0 as \"fromBedId\", " 
					 + " 	ordDet.accession_number as \"accessionNumber\" , " 
					 + " 	ordMas.visit_type_id as \"visitTypeId\", " 
					 + " 	patReg.identification_number as \"identificationNumber\", " 
					 + " 	vm.visit_type_name as \"visitName\", " 
					 + " 	pcm.patient_category as \"patientCategory\" , " 
					 + " 	encMas.encounter_number as \"visitNumber\", "
					 + "   billMast.billing_master_id as \"billingMasterId\"," 
					 + " 	cm.country_code as \"countryCode\" , " 
					 + " 	0 as \"toBedId\", " 
					 + " 	'-' as \"toBedNumber\", " 
					 + " 	to_char(sm.from_time, 'HH:MI') as \"appointmentTime\", " 
					 + " 	0 as \"toWardId\", " 
					 + " 	0 as \"toRoomId\", " 
					 + " 	0 as \"fromWardId\", " 
					 + " 	0 as \"fromRoomId\", " 
					 + " 	0 as \"fromBillingBedCategoryId\", " 
					 + " 	0 as \"fromBedCategoryId\" , " 
					 + " 	to_char(appMstr.appointment_date, 'DD/MM/YYYY') as \"appointmentDate\", " 
					 + " 	'' as \"wardName\", " 
					 + " 	hemoDialysisMapper.dialysis_app_mapper_id as \"dialysisAppMapperId\", " 
					 + " 	sm.bed_id as \"chairId\", " 
					 + " 	appMstr.appointment_id as \"appointmentId\", " 
					 + " 	hemoDialysisMapper.doctor_id as \"dialysisDoctorId\", " 
					 + " 	hemoDialysisMapper.workbench_acceptance as \"hemodialysisWorkbenchAcceptance\" , " 
					 + " 	bedChairMapp.bed_number as \"chairName\" " 
					 + " from " 
					 + " 	public.t_order_details ordDet " 
					 + " inner join public.t_order_master ordMas on " 
					 + " 	ordDet.order_id = ordMas.order_id " 
					 + " inner join patient.t_patient_registration patReg on " 
					 + " 	ordMas.patient_id = patReg.patient_id " 
					 + " inner join public.m_prefix_master preMas on " 
					 + " 	patReg.prefix_id = preMas.prefix_id " 
					 + " inner join public.m_gender_master genMas on " 
					 + " 	patReg.gender_id = genMas.gender_id " 
					 + " inner join patient.t_patient_details patdtl on " 
					 + " 	patdtl.patient_id = patReg.patient_id " 
					 + " inner join public.m_visit_type_master vm on " 
					 + " 	vm.visit_type_id = ordMas.visit_type_id " 
					 + " 	and vm.status = 'A' " 
					 + " left join public.m_patient_category_master pcm on " 
					 + " 	pcm.patient_category_id = patReg.patient_category_id " 
					 + " 	and pcm.status = 'A' " 
					 + " left join public.m_country_master cm on " 
					 + " 	cm.country_id = patdtl.country_id " 
					 + " inner join opd.t_encounter_master encMas on " 
					 + " 	ordMas.encounter_id = encMas.encounter_id " 
					 + " left outer join adt.t_admission adm on " 
					 + " 	ordMas.admission_id = adm.admission_id " 
					 + " left join billing.t_billing_details billDet on " 
					 + " 	ordDet.order_details_id = billDet.order_details_id " 
					 + " left join billing.m_billing_master billMast on " 
					 + " 	ordDet.service_bill_id = billMast.billing_master_id " 
					 + " 	and billMast.bill_cancelled = 'N' " 
					 + " left outer join doctor.m_doctor_master docMas on " 
					 + " 	ordDet.ord_doc_id = docMas.doctor_id " 
					 + " left join doctor.m_speciality_master specMas on " 
					 + " 	ordDet.ord_doc_spl_id = specMas.speciality_id " 
					 + " left join opd.t_cyclic_appointment_mapper cam on " 
					 + " 	cam.order_detail_id = ordDet.order_details_id " 
					 + " left join opd.t_hemodialysis_appointment_mapper hemoDialysisMapper on " 
					 + " 	hemoDialysisMapper.order_details_id = ordDet.order_details_id " 
					 + " left join opd.m_appointment_master appMstr on " 
					 + " 	appMstr.appointment_id = " 
					 + " 	(case " 
					 + " 		when ordDet.order_details_id = cam.order_detail_id then cam.appointment_id " 
					 + " 		when hemoDialysisMapper.order_details_id = ordDet.order_details_id then hemoDialysisMapper.appointment_id " 
					 + " 		else encMas.appointment_id " 
					 + " 	end) " 
					 + " 	and appMstr.appointment_type_id in(8, 10) " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	sm.slot_id = appMstr.slot_id " 
					 + " inner join service.m_service_master servMast on " 
					 + " 	ordDet.service_id = servMast.service_master_id " 
					 + " left join adt.m_priority_master pm on " 
					 + " 	pm.priority_id = ordDet.priority_id " 
					 + " left join adt.m_bed_master bedChairMapp on " 
					 + " 	bedChairMapp.bed_id = sm.bed_id " 
					 + " left join t_unit_application_billing_configuration appConfig on " 
					 + " 	appConfig.unit_id = ordDet.org_unit_id " 
					 + "  left join billing.t_credit_note_detail tcnd on  " 
					 + "  	tcnd.order_detail_id = ordDet.order_details_id  " 
					 + "  	and tcnd.status = 'A'  " 
					 + "  	and tcnd.cancel_or_discount = 1 	 " 
					 + " where " 
					 + " 	servMast.speciality_id =:hemodialysisSpecialityId " 
					 + " 	and ordDet.service_rendered = 0 " 
					 + " 	and ordMas.org_id =:orgId " 
					 + " 	and ordMas.org_unit_id =:unitId " 
					 + " 	and ordMas.visit_type_id = 1 " 
					 + " 	and (ordDet.status = 'A' and tcnd.credit_note_detail_id is null) " 
					 + " 	and (appConfig.is_pre_or_post_billing = 'N' " 
					 + " 		or (appConfig.is_pre_or_post_billing = 'Y' " 
					 + " 			and billMast.bill_status_id <> 1 " 
					 + " 			and billDet.status = 'A' )) " 
					 + " 	and ordDet.is_order_visible_dept = 'A' ");

			
			StringBuilder IPQuery = new StringBuilder("  select " 
					 + " 	orderDetail.order_details_id as \"orderDetailsId\", " 
					 + " 	sm.slot_id as \"slotId\", " 
					 + " 	admission.admission_id as \"admissionId\", " 
					 + " 	orderDetail.ord_doc_id as \"ordDoctorId\", " 
					 + " 	orderDetail.status as \"status\", " 
					 + " 	orderDetail.service_rendered as \"serviceRendered\", " 
					 + " 	patientReg.patient_id as \"patientId\", " 
					 + " 	patientReg.uhid_number as \"uhidNumber\", " 
					 + " 	patientReg.gender_id as \"genderId\", " 
					 + " 	genderMaster.gender_code as \"genderCode\", " 
					 + " 	prefixMas.prefix_code as \"prefixCode\", " 
					 + " 	to_char(patientReg.dob, 'DD-MM-YYYY HH:mm:ss') as \"dob\", " 
					 + " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, patientReg.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, patientReg.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, patientReg.dob)), 'D ') as \"ageString\", " 
					 + " 	0 as \"encounterId\", " 
					 + " 	admission.doctor_id as \"doctorId\", " 
					 + " 	('Dr. ' || coalesce(doctorMaster.first_name)|| ' ' || coalesce(doctorMaster.middle_name)|| ' ' || coalesce(doctorMaster.last_name)) as \"doctorName\", " 
					 + " 	admission.speciality_id as \"specialityId\", " 
					 + " 	specialityMaster.speciality_name as \"specialityName\", " 
					 + " 	serviceMaster.service_standard_name as \"serviceName\", " 
					 + " 	serviceMaster.service_master_id as \"serviceId\", " 
					 + " 	serviceMaster.service_standard_code as \"serviceCode\", " 
					 + " 	orderDetail.priority_id as \"priorityId\", " 
					 + " 	orderMaster.order_no as \"orderNo\", " 
					 + " 	to_char(orderDetail.order_date, 'DD/MM/YYYY') as \"orderDate\", " 
					 + " 	(prefixMas.prefix_code || ' ' || coalesce(patientReg.first_name)|| ' ' || coalesce(patientReg.middle_name)|| ' ' || coalesce(patientReg.last_name)) as \"patientName\", " 
					 + " 	bedMstr.bed_number as \"bedNumber\" , " 
					 + " 	bedMstr.bed_id as \"fromBedId\", " 
					 + " 	orderDetail.accession_number as \"accessionNumber\" , " 
					 + " 	orderMaster.visit_type_id as \"visitTypeId\" , " 
					 + " 	patientReg.identification_number as \"identificationNumber\", " 
					 + " 	vm.visit_type_name as \"visitName\" , " 
					 + " 	pcm.patient_category as \"patientCategory\", " 
					 + " 	admission.admission_number as \"visitNumber\", " 
					 + "   billMaster.billing_master_id as \"billingMasterId\"," 
					 + " 	cm.country_code as \"countryCode\", " 
					 + " 	sm.bed_id as \"toBedId\", " 
					 + " 	bm.bed_number as \"toBedNumber\", " 
					 + " 	to_char(sm.from_time, 'HH:MI') as \"appointmentTime\", " 
					 + " 	bm.ward_id as \"toWardId\", " 
					 + " 	bm.room_id as \"toRoomId\", " 
					 + " 	bedMstr.ward_id as \"fromWardId\", " 
					 + " 	bedMstr.room_id as \"fromRoomId\", " 
					 + " 	bedMstr.billing_bed_category_id as \"fromBillingBedCategoryId\", " 
					 + " 	bedMstr.bed_category_id as \"fromBedCategoryId\" , " 
					 + " 	to_char(sm.appointment_date, 'DD-MM-YYYY') as \"appointmentDate\", " 
					 + " 	wm.ward_name as \"wardName\", " 
					 + " 	hemoDialysisMapper.dialysis_app_mapper_id as \"dialysisAppMapperId\", " 
					 + " 	sm.bed_id as \"chairId\", " 
					 + " 	appMstr.appointment_id as \"appointmentId\", " 
					 + " 	hemoDialysisMapper.doctor_id as \"dialysisDoctorId\", " 
					 + " 	hemoDialysisMapper.workbench_acceptance as \"hemodialysisWorkbenchAcceptance\", " 
					 + " 	bm.bed_number as \"chairName\" " 
					 + " from " 
					 + " 	public.t_order_details orderDetail " 
					 + " inner join public.t_order_master orderMaster on " 
					 + " 	orderDetail.order_id = orderMaster.order_id " 
					 + " inner join patient.t_patient_registration patientReg on " 
					 + " 	orderMaster.patient_id = patientReg.patient_id " 
					 + " inner join public.m_prefix_master prefixMas on " 
					 + " 	patientReg.prefix_id = prefixMas.prefix_id " 
					 + " inner join public.m_gender_master genderMaster on " 
					 + " 	patientReg.gender_id = genderMaster.gender_id " 
					 + " inner join patient.t_patient_details patdtl on " 
					 + " 	patdtl.patient_id = patientReg.patient_id " 
					 + " inner join adt.t_admission admission on " 
					 + " 	admission.admission_id = orderMaster.admission_id " 
					 + " inner join public.m_visit_type_master vm on " 
					 + " 	vm.visit_type_id = orderMaster.visit_type_id " 
					 + " 	and vm.status = 'A' " 
					 + " left join public.m_patient_category_master pcm on " 
					 + " 	pcm.patient_category_id = patientReg.patient_category_id " 
					 + " 	and pcm.status = 'A' " 
					 + " left join public.m_country_master cm on " 
					 + " 	cm.country_id = patdtl.country_id " 
					 + " left join adt.t_admission_details dtl on " 
					 + " 	dtl.admission_id = admission.admission_id " 
					 + " 	and admission.status = 'A' " 
					 + " 	and dtl.status = 'A' " 
					 + " left join adt.m_bed_master bedMstr on " 
					 + " 	dtl.bed_id = bedMstr.bed_id " 
					 + " left join adt.m_ward_master wm on " 
					 + " 	wm.ward_id = bedMstr.ward_id " 
					 + " left outer join billing.t_billing_details billingdet7_ on " 
					 + " 	orderDetail.order_details_id = billingdet7_.order_details_id " 
					 + " left outer join billing.m_billing_master billMaster on " 
					 + " 	billingdet7_.billing_master_id = billMaster.billing_master_id " 
					 + " 	and billMaster.bill_cancelled = 'N' " 
					 + " left outer join doctor.m_doctor_master doctorMaster on " 
					 + " 	orderDetail.ord_doc_id = doctorMaster.doctor_id " 
					 + " left outer join doctor.m_speciality_master specialityMaster on " 
					 + " 	orderDetail.ord_doc_spl_id = specialityMaster.speciality_id " 
					 + " left outer join public.t_template_transaction templatetr12_ on " 
					 + " 	orderDetail.order_details_id = templatetr12_.order_details_id " 
					 + " 	and (templatetr12_.status = 'A') " 
					 + " left outer join public.t_template_master templatema13_ on " 
					 + " 	templatetr12_.template_id = templatema13_.template_id " 
					 + " 	and (templatema13_.template_type_id = 13) " 
					 + " left join opd.m_appointment_master appMstr on " 
					 + " 	appMstr.order_details_id = orderDetail.order_details_id " 
					 + " 	and appMstr.appointment_type_id = 8 " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	sm.slot_id = appMstr.slot_id " 
					 + " left join adt.m_bed_master bm on " 
					 + " 	bm.bed_id = sm.bed_id " 
					 + " inner join service.m_service_master serviceMaster on " 
					 + " 	orderDetail.service_id = serviceMaster.service_master_id " 
					 + " left join opd.t_hemodialysis_appointment_mapper hemoDialysisMapper on " 
					 + " 	hemoDialysisMapper.appointment_id = appMstr.appointment_id " 
					 + "  left join billing.t_credit_note_detail tcnd on  " 
					 + "  	tcnd.order_detail_id = orderDetail.order_details_id  " 
					 + "  	and tcnd.status = 'A'  " 
					 + "  	and tcnd.cancel_or_discount = 1 	 " 
					 + " where " 
					 + " 	serviceMaster.speciality_id =:hemodialysisSpecialityId " 
					 + " 	and orderDetail.service_rendered = 0 " 
					 + " 	and orderMaster.org_id =:orgId " 
					 + " 	and orderMaster.org_unit_id =:unitId " 
					 + " 	and orderMaster.visit_type_id in (2, 3, 4) " 
					 + " 	and (orderDetail.status = 'A' and tcnd.credit_note_detail_id is null) " 
					 + " 	and orderDetail.is_order_visible_dept = 'A' ");
			
			
			if (radiologyOrdersListDto.getFromDate() != null){
				IPQuery.append(" AND to_char(orderDetail.order_date,'YYYY-MM-DD') between :fromDate and  :toDate ");
				OPquery.append(" AND to_char(ordDet.order_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
				
			if(radiologyOrdersListDto.getPatientId() > 0){
				IPQuery.append(" and orderMaster.patient_id=:patientId ");
				OPquery.append(" and ordMas.patient_id=:patientId ");
			}
			
			if(radiologyOrdersListDto.getServiceId() > 0){
				IPQuery.append(" and orderDetail.service_id=:serviceId ");
				OPquery.append(" and ordDet.service_id=:serviceId ");
			}
			
			if(radiologyOrdersListDto.getPatientName()!=null) {
				IPQuery.append(" AND (LOWER(concat(coalesce(patientReg.first_name, ''), ' ', coalesce(patientReg.middle_name, ''), ' ', coalesce(patientReg.last_name, ''), ' ', coalesce(patientReg.uhid_number, ''))) ")
						.append("LIKE :patientName)");

				OPquery.append(" AND (LOWER(concat(coalesce(patReg.first_name, ''), ' ', coalesce(patReg.middle_name, ''), ' ', coalesce(patReg.last_name, ''), ' ', coalesce(patReg.uhid_number, ''))) ")
						.append("LIKE :patientName)");

			}
			
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0){
				IPQuery.append(" and orderMaster.visit_type_id IN :visitTypeList ");
				OPquery.append(" and ordMas.visit_type_id IN :visitTypeList ");
			}
			
			if(radiologyOrdersListDto.getDialysisChairList()!=null && radiologyOrdersListDto.getDialysisChairList().size() > 0){
				IPQuery.append(" and hemoDialysisMapper.chair_id IN :dialysisChairList ");
				OPquery.append(" and hemoDialysisMapper.chair_id IN :dialysisChairList ");
			}
			
			String finalQuery = OPquery + " union "+IPQuery;
			Query query2 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery);
			
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size()>0) {
				query2.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			
			if(radiologyOrdersListDto.getDialysisChairList()!=null && radiologyOrdersListDto.getDialysisChairList().size() > 0){
				query2.setParameterList("dialysisChairList", radiologyOrdersListDto.getDialysisChairList());
			}

		if (radiologyOrdersListDto.getFromDate() != null){
			query2.setParameter("fromDate", radiologyOrdersListDto.getFromDate().substring(0, 10));
			query2.setParameter("toDate", radiologyOrdersListDto.getToDate().substring(0, 10));
		}

		if(radiologyOrdersListDto.getPatientId() > 0){
			query2.setParameter("patientId", radiologyOrdersListDto.getPatientId());
		}

		if(radiologyOrdersListDto.getServiceId() > 0){
			query2.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
		}

		if(radiologyOrdersListDto.getPatientName()!=null) {
			query2.setParameter("patientName", "%" + radiologyOrdersListDto.getPatientName().toLowerCase() + "%");
		}

			List<HemodialysisAppointmentMapperDto> listForTotalCount = query2
					.setParameter("unitId", radiologyOrdersListDto.getUnitId())
					.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
					.setParameter("hemodialysisSpecialityId", radiologyOrdersListDto.getHemodialysisSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(HemodialysisAppointmentMapperDto.class)).list();
			
			List<HemodialysisAppointmentMapperDto> listServiceMasterDto = query2
					.setParameter("unitId", radiologyOrdersListDto.getUnitId())
					.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
					.setParameter("hemodialysisSpecialityId", radiologyOrdersListDto.getHemodialysisSpecialityId())
					.setFirstResult(radiologyOrdersListDto.getOffset() != null ? radiologyOrdersListDto.getOffset()  : 0)
					.setMaxResults(radiologyOrdersListDto.getNoOfRecordsPerPage() != null ? radiologyOrdersListDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(HemodialysisAppointmentMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, listForTotalCount.size());
		}catch (Exception e) {
		logger.error("getNephrologyWorkbenchWorklist failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response acceptHemodialysisRequest(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		//System.out.println(radiologyOrdersListDto.getDoctorId());
		try {
			RadiologyOrdersListDto dto=(RadiologyOrdersListDto)sessionFactory.getCurrentSession()
					.createQuery(" select bedId as toBedId from BedMaster bed where bed.bedId=:bedId and bed.bedStatusId=5 ")
					.setParameter("bedId", radiologyOrdersListDto.getToBedId())
					.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();
			
			if(dto!=null){
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			Session session = sessionFactory.getCurrentSession();
			//<-------Booking requested bed so that it should not get allocated to any other 
			Query query=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
			query.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
			query.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
			query.setParameter("unitId", radiologyOrdersListDto.getUnitId());
			query.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
			query.setParameter("bedId", radiologyOrdersListDto.getToBedId());
			query.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query1=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
			query1.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
			query1.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
			query1.setParameter("unitId", radiologyOrdersListDto.getUnitId());
			query1.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
			query1.setParameter("bedId", radiologyOrdersListDto.getToBedId());
			query1.executeUpdate();
			
			
			BedLogStatus bedLogStatusObj=new BedLogStatus();
			bedLogStatusObj.setBedId(radiologyOrdersListDto.getToBedId());
			bedLogStatusObj.setBedStatusId(5);
			bedLogStatusObj.setStatus('A');
			bedLogStatusObj.setCreatedBy(radiologyOrdersListDto.getUpdatedBy());
			bedLogStatusObj.setCreatedDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
			bedLogStatusObj.setUpdatedBy(radiologyOrdersListDto.getUpdatedBy());
			bedLogStatusObj.setUpdatedDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
			bedLogStatusObj.setOrganizationId(radiologyOrdersListDto.getOrganizationId());
			bedLogStatusObj.setUnitId(radiologyOrdersListDto.getUnitId());
			sessionFactory.getCurrentSession().save(bedLogStatusObj);
			
			Query transferQuery = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, "
					+ "  transfer.toWardId=:toWardId, transfer.toRoomId=:toRoomId, transfer.toBedId=:toBedId,"
					+ " transfer.transferStatusId=9,transfer.status='I' "
					+ "  WHERE transfer.admissionId=:admissionId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId AND transfer.status='A' ");
			transferQuery.setParameter("toWardId", radiologyOrdersListDto.getToWardId());
			transferQuery.setParameter("toRoomId", radiologyOrdersListDto.getToRoomId());
			transferQuery.setParameter("toBedId", radiologyOrdersListDto.getToBedId());
			transferQuery.setParameter("admissionId", radiologyOrdersListDto.getAdmissionId());
			transferQuery.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
			transferQuery.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
			transferQuery.setParameter("unitId", radiologyOrdersListDto.getUnitId());
			transferQuery.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
			transferQuery.executeUpdate();
		
			
			Query query3=session.createQuery("update AdmissionDetails admt set  admt.updatedBy=:updatedBy,admt.updatedDate=:updatedDate,"
					+ " admt.wardId=:wardId,admt.bedId=:bedId,roomId=:roomId WHERE admt.admissionId=:admissionId AND admt.unitId=:unitId AND admt.organizationId=:orgId AND admt.status='A' ");
				query3.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
				query3.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				query3.setParameter("unitId", radiologyOrdersListDto.getUnitId());
				query3.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
				query3.setParameter("admissionId", radiologyOrdersListDto.getAdmissionId());
				query3.setParameter("wardId", radiologyOrdersListDto.getToWardId());
				query3.setParameter("roomId",radiologyOrdersListDto.getToRoomId());
				query3.setParameter("bedId", radiologyOrdersListDto.getToBedId());
			
			query3.executeUpdate();
			
			//<-------Releasing previous bed of a patient 
			Query query4=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=7 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
				query4.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
				query4.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				query4.setParameter("unitId", radiologyOrdersListDto.getUnitId());
				query4.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
				query4.setParameter("bedId", radiologyOrdersListDto.getFromBedId());
			query4.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query5=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
				query5.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
				query5.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				query5.setParameter("unitId", radiologyOrdersListDto.getUnitId());
				query5.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
				query5.setParameter("bedId", radiologyOrdersListDto.getFromBedId());
			query5.executeUpdate();
			
			BedLogStatus bedLogStatus=new BedLogStatus();
				bedLogStatus.setBedId(radiologyOrdersListDto.getFromBedId());
				bedLogStatus.setBedStatusId(7);
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(radiologyOrdersListDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				bedLogStatus.setUpdatedBy(radiologyOrdersListDto.getUpdatedBy());
				bedLogStatus.setUpdatedDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				bedLogStatus.setOrganizationId(radiologyOrdersListDto.getOrganizationId());
				bedLogStatus.setUnitId(radiologyOrdersListDto.getUnitId());
			session.save(bedLogStatus);
			//------>	
			
		Transfer transfer=new Transfer();
				transfer.setOrganizationId(radiologyOrdersListDto.getOrganizationId());
				transfer.setUnitId(radiologyOrdersListDto.getUnitId());
				transfer.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
				transfer.setFromBedCategoryId(radiologyOrdersListDto.getFromBedCategoryId());
				transfer.setFromBillingBedCategoryId(radiologyOrdersListDto.getFromBillingBedCategoryId());
				transfer.setFromWardId(radiologyOrdersListDto.getToWardId());
				transfer.setFromRoomId(radiologyOrdersListDto.getToRoomId());
				transfer.setFromBedId(radiologyOrdersListDto.getToBedId());
				transfer.setFromDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				transfer.setFromTime(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				transfer.setCreatedDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
				transfer.setTransferStatusId(1);
				transfer.setIsPrimary('N');
				transfer.setStatus('A');
				transfer.setCreatedBy(radiologyOrdersListDto.getUpdatedBy());
				transfer.setUpdatedBy(radiologyOrdersListDto.getUpdatedBy());
			session.save(transfer);
			
			
			sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster set "
					+ "serviceRendered=1, "
     				/*+ "ordDocId=:doctorId, "*/
     				+ "ordDocSplId=:specialityId, "
					+ "ordRenderDatetime=now(), "
					+ "updatedBy=:updatedBy "
					+ "WHERE orderDetailsId=:orderDetailsId")
				.setParameter("updatedBy",radiologyOrdersListDto.getUpdatedBy())
  			    /*.setParameter("doctorId",radiologyOrdersListDto.getDoctorId())*/
  			    .setParameter("specialityId",radiologyOrdersListDto.getSpecialityId())
				.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery("update Admission set "
     				+ "doctorId=:doctorId, "
     				+ "specialityId=:specialityId, "
					+ "updatedBy=:updatedBy "
					+ "WHERE admissionId=:admissionId")
				.setParameter("updatedBy",radiologyOrdersListDto.getUpdatedBy())
  			    .setParameter("doctorId",radiologyOrdersListDto.getDoctorId())
  			    .setParameter("specialityId",radiologyOrdersListDto.getSpecialityId())
				.setParameter("admissionId", radiologyOrdersListDto.getAdmissionId())
			.executeUpdate();
			
			Query updateAppointQuery=sessionFactory.getCurrentSession().createQuery("update AppointmentMaster appMaster set appMaster.appointmentStatusId=8,appMaster.updatedBy=:updatedBy,appMaster.updatedDate=:updatedDate"
					+ " WHERE appMaster.admissionId=:addId AND appMaster.unitId=:unitId AND appMaster.organizationId=:orgId");
			updateAppointQuery.setParameter("updatedBy", radiologyOrdersListDto.getUpdatedBy());
			updateAppointQuery.setParameter("updatedDate", TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()));
			updateAppointQuery.setParameter("unitId", radiologyOrdersListDto.getUnitId());
			updateAppointQuery.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
			updateAppointQuery.setParameter("addId", radiologyOrdersListDto.getAdmissionId());
			updateAppointQuery.executeUpdate();
			
			return new Response(SUCCESS, null, "Patient transferred to Hemodialysis ward successfully...!!", null, null);

		} catch (Exception e) {
		logger.error("acceptHemodialysisRequest failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getPatientServicesForFutureDate(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			List<ServiceMasterDto> appointmentStatusMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SERVICES_FOR_FUTURE_DATE")
					.setParameter("patientId", patientDocReqDto.getPatientId())
					.setParameter("organizationId", patientDocReqDto.getOrganisationId())
					.setParameter("unitId", patientDocReqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, appointmentStatusMasterDtoList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getPatientServicesForFutureDate failed", e);
		}

		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response rejectHemodialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster set "
					+ "status='I', ordCancelled='Y',ordCancelDatetime=:cancelDate, "
					+ "updatedBy=:updatedBy "
					+ "WHERE orderDetailsId=:orderDetailsId")
				.setParameter("updatedBy",hemodialysisAppointmentMapperDto.getUpdatedBy())
				.setParameter("orderDetailsId", hemodialysisAppointmentMapperDto.getOrderDetailsId())
				.setParameter("cancelDate", TimeZoneComponent.getDateByzone(hemodialysisAppointmentMapperDto.getUnitId()))
			.executeUpdate();
		
			if(hemodialysisAppointmentMapperDto.getSlotId()!=null) {
			sessionFactory.getCurrentSession().createQuery("update SlotMaster set "
					+ "slotStatusId=1,"
					+ "updatedBy=:updatedBy,"
					+ "updatedDate=:updateDate "
					+ "WHERE slotId=:id ")
				.setParameter("updatedBy",hemodialysisAppointmentMapperDto.getUpdatedBy())
				.setParameter("id", hemodialysisAppointmentMapperDto.getSlotId())
				.setParameter("updateDate", TimeZoneComponent.getDateByzone(hemodialysisAppointmentMapperDto.getUnitId()))
			.executeUpdate();
			}
		
			if(hemodialysisAppointmentMapperDto.getAppointmentId()!=null) {
				StringBuilder appointmeQuery=new StringBuilder("update AppointmentMaster appMaster set appMaster.appointmentStatusId=2,appMaster.updatedBy=:updatedBy,appMaster.updatedDate=:updatedDate"
						+ " WHERE   appMaster.unitId=:unitId AND appMaster.organizationId=:orgId  and appMaster.appointmentId=:appId ");
				
				if(hemodialysisAppointmentMapperDto.getAdmissionId()!=null)
				appointmeQuery.append(" and appMaster.appointmentId="+hemodialysisAppointmentMapperDto.getAdmissionId());
				
				if(hemodialysisAppointmentMapperDto.getEncounterId()!=null) {
					appointmeQuery.append(" and appMaster.appointmentId="+hemodialysisAppointmentMapperDto.getOrderDetailsId());
				}
				
			    sessionFactory.getCurrentSession().createQuery(appointmeQuery.toString())
				.setParameter("updatedBy", hemodialysisAppointmentMapperDto.getUpdatedBy())
			    .setTimestamp("updatedDate", TimeZoneComponent.getDateByzone(hemodialysisAppointmentMapperDto.getUnitId()))
				.setParameter("unitId", hemodialysisAppointmentMapperDto.getUnitId())
				.setParameter("orgId", hemodialysisAppointmentMapperDto.getOrganizationId())
				.setParameter("appId", hemodialysisAppointmentMapperDto.getAppointmentId())
				.executeUpdate();
			}
			
			return new Response(SUCCESS, null, "Dialysis Request rejected", null, null);

		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("rejectHemodialysisRequest failed", e);
		}

		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReviewApplicableServicesByPatient(Integer patientId, Integer orgId, Integer unitId,Date date)
			throws ApplicationException {
		try {
			
			String query ="select " 
					 + " 	em.encounter_id as \"encounterId\", " 
					 + " 	sm.service_master_id as \"serviceId\", " 
					 + " 	sm.service_standard_name as \"serviceStandardName\", " 
					 + " 	spem.speciality_name as \"specialityName\", " 
					 + " 	subspe.sub_speciality_name as \"subSpecialityName\", " 
					 + " 	od.order_details_id as \"orderDetailsId\", " 
					 + " 	em.encounter_number as \"encounterNumber\", "
					 + "	em.dept_id as \"reviewAgainstSplId\", "
					 + "	em.doctor_id as \"reviewAgainstDocId\" " 
					 + " from " 
					 + " 	public.t_order_details od " 
					 + " inner join public.t_order_master om on " 
					 + " 	om.order_id = od.order_id " 
					 + " inner join opd.t_encounter_master em on " 
					 + " 	om.encounter_id = em.encounter_id " 
					 + " inner join service.t_unit_service_master_details usmd on " 
					 + " 	od.service_id = usmd.service_master_id " 
					 + " 	and od.status = 'A' " 
					 + " 	and om.org_unit_id = usmd.unit_id " 
					 + " inner join service.m_service_master sm on " 
					 + " 	od.service_id = sm.service_master_id " 
					 + " inner join doctor.m_speciality_master spem on " 
					 + " 	sm.speciality_id = spem.speciality_id " 
					 + " inner join doctor.m_sub_speciality_master subspe on " 
					 + " 	sm.sub_speciality_id = subspe.sub_speciality_id " 
					 + " where " 
					 + " 	em.patient_id = :patientId " 
					 + " 	and usmd.is_review_applicable = 'Y' " 
					 + " 	and od.is_service_reviewed = false " 
					 + " 	and od.status = 'A' " 
					 + " 	and em.encounter_type_id = 1 " 
					 + " 	and to_char(od.review_applicable_date,'YYYY-MM-DD HH24:MI:SS') >= :reviewApplicableDate";
			
			
			List<ReviewConsultationResDto> list  = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("patientId", patientId)
					//.setParameter("date", TimeZoneComponent.getDateByzone(unitId))
					.setParameter("reviewApplicableDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd HH:mm:ss"))
					.setResultTransformer(Transformers.aliasToBean(ReviewConsultationResDto.class)).list();
			if(list!=null && list.size()>0)
			{
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
			}
			else {
				return new Response(ERROR, ERROR_CODE, "No review appointment applicable.", list, null);
			}
			
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getReviewApplicableServicesByPatient failed", e);
		}

		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkReferalAndUpdate(EncounterMasterDto encounterMasterDto) 
	{
		try {
			
			if(encounterMasterDto.getDoctorId()!=null && encounterMasterDto.getDoctorId()>0)
			{
				 String query = "select consult_doctor_id as \"consultDoctorId\"  from opd.t_consult_doctor where patient_id =:patientId and ref_to_doctor_id=:doctorId order by 1 desc limit 1";
				 ConsultDoctor consultDoctor  = (ConsultDoctor)sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setResultTransformer(Transformers.aliasToBean(ConsultDoctor.class)).uniqueResult();
				
				 if(consultDoctor!=null && consultDoctor.getConsultDoctorId()>0)
				 {
					 String updateQuery = "UPDATE opd.t_consult_doctor SET  referred_status=:status"+
							 "WHERE consult_doctor_id=:consultDoctorId";
					 sessionFactory.getCurrentSession().createSQLQuery(updateQuery)
							 .setParameter("status", "Appointment Scheduled")
							 .setParameter("consultDoctorId", consultDoctor.getConsultDoctorId())
							 .executeUpdate();
				 }
			}
			 return new Response(SUCCESS, null, null, null, null);
			
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("checkReferalAndUpdate failed", e);
		}

		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEHCConsultation(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			List<EncounterEhcConsultationMpprDto> list  = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EHC_CONSULTATION_BY_ENCOUNTER_ID_FOR_APPOINTMENT")
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("consultationMasterId", encounterMasterDto.getConsultationMasterId())
					.setResultTransformer(Transformers.aliasToBean(EncounterEhcConsultationMpprDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getEHCConsultation failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response saveRadiologyMapper(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			RadiologyMapper mapper =new RadiologyMapper();
			mapper.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
			mapper.setEncounterId(radiologyOrdersListDto.getEncounterId());
			mapper.setOrderDetailsId(radiologyOrdersListDto.getOrderDetailsId());
			mapper.setCreatedBy(radiologyOrdersListDto.getUserId());
			mapper.setUpdatedBy(radiologyOrdersListDto.getUserId());
			mapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mapper.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mapper.setReasonId(radiologyOrdersListDto.getReasonId());
			mapper.setStatus('A');
			mapper.setIsCancel(radiologyOrdersListDto.getIsCritical()); // is Critical flag used here as Iscancel
			mapper.setUnitId(radiologyOrdersListDto.getUnitId());
			mapper.setOrganizationId(radiologyOrdersListDto.getOrganizationId());
			sessionFactory.getCurrentSession().save(mapper);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		 }catch(HibernateException he) {
		 logger.error("saveRadiologyMapper failed", he);
		 }catch(Exception e) {
		logger.error("saveRadiologyMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
  }

	@Override
	public Response updateEncounterServiceMpperStatus(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			if(encounterMasterDto.getStatusId()!=null && encounterMasterDto.getStatusId().intValue()==5) {
				
				
				if (encounterMasterDto.getAgainstEncId() != null && encounterMasterDto.getAgainstEncId() > 0) 
				{
					if(encounterMasterDto.getDeptWiseAppointment()!=null && encounterMasterDto.getDeptWiseAppointment()=='Y')
					{
						String updateQuery = "update opd.t_encounter_ehc_consultation_mppr set "
								+ "	doctor_id =:doctorId "
								+ "	,consultation_speciality_id = :specialityId, "
								+ "	slot_id = :slotId ,"
								+ "	appointment_status_id = :appointmentStatusId "
								+ " where "
								+ "	service_id = :serviceId "
								+ "	and encounter_id = :encounterId and consultation_mppr_id =:consultationMpprId ";
						sessionFactory.getCurrentSession().createSQLQuery(updateQuery)
//						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("specialityId", encounterMasterDto.getSpecialityId())
						.setParameter("slotId", encounterMasterDto.getSlotId())
						.setParameter("appointmentStatusId", encounterMasterDto.getStatusId())
						.setParameter("consultationMpprId", encounterMasterDto.getConsultationMpprId())
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("encounterId", encounterMasterDto.getAgainstEncId())
						.setParameter("serviceId", encounterMasterDto.getServiceId()).executeUpdate();
					}else {
						String updateQuery = "update opd.t_encounter_ehc_consultation_mppr set "
								+ "	doctor_id = :doctorId, "
								+ "	consultation_speciality_id = :specialityId, "
								+ "	slot_id = :slotId ,"
								+ "	appointment_status_id = :appointmentStatusId "
								+ " where "
								+ "	service_id = :serviceId "
								+ "	and encounter_id = :encounterId and consultation_mppr_id =:consultationMpprId ";
						sessionFactory.getCurrentSession().createSQLQuery(updateQuery)
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("specialityId", encounterMasterDto.getSpecialityId())
						.setParameter("slotId", encounterMasterDto.getSlotId())
						.setParameter("appointmentStatusId", encounterMasterDto.getStatusId())
						.setParameter("consultationMpprId", encounterMasterDto.getConsultationMpprId())
						.setParameter("encounterId", encounterMasterDto.getAgainstEncId())
						.setParameter("serviceId", encounterMasterDto.getServiceId()).executeUpdate();
					}
					
				}
			
			}else{
				// this code used for completing appointment set status id=6
				// update child enounter ehc consultation mapper
				String updateQuery = "update opd.t_encounter_ehc_consultation_mppr set "
						+ "	appointment_status_id = :appointmentStatusId "
						+ " where "
						+ "	 child_encounter_id =:encounterId";
				    sessionFactory.getCurrentSession().createSQLQuery(updateQuery)
				.setParameter("appointmentStatusId", encounterMasterDto.getStatusId())
				.setParameter("encounterId", encounterMasterDto.getChildEncounterId()).executeUpdate();
				    
				    // update child encounter master 
				    sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
							+ "statusId=:statusId "
							+ "WHERE encounterId=:encounterId")
						.setParameter("statusId", encounterMasterDto.getStatusId())
						.setParameter("encounterId", encounterMasterDto.getChildEncounterId()).executeUpdate();
			}
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("updateEncounterServiceMpperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEncounterDetailsForCoversheet(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			List<EncounterMasterDto> encounterMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OLD_ENCOUNTER_DETAILS_FOR_COVERSHEET")
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					//.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
//					.setParameter("createdDate",encounterMasterDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, encounterMasterDtosList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getEncounterDetailsForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response renderOtherServices(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
				sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster set "
						+ "serviceRendered=1, "
						+ "ordRenderDatetime=now(), "
						+ "updatedBy=:updatedBy "
						+ "WHERE orderDetailsId=:orderDetailsId")
					.setParameter("updatedBy",radiologyOrdersListDto.getUpdatedBy())
					.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
				.executeUpdate();
				
				sessionFactory.getCurrentSession().createQuery("update CyclicAppointmentMapper cam set "
						+ "cam.availabelQuantity=0, "
						+ "cam.updatedBy=:updatedBy "
						+ "WHERE cam.orderDetailId=:orderDetailsId")
					.setParameter("updatedBy",radiologyOrdersListDto.getUpdatedBy())
					.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
				.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("renderOtherServices failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getOtherServicesForRendering(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			
			StringBuilder query = new StringBuilder( " select " 
					 + " 	distinct(orderdetails.order_details_id) as \"orderDetailsId\", " 
					 + " 	orderdetails.status as \"status\", " 
					 + " 	orderdetails.service_rendered as \"serviceRendered\", " 
					 + " 	patientreg.patient_id as \"patientId\", " 
					 + " 	patientreg.uhid_number as \"uhidNumber\", " 
					 + " 	patientreg.gender_id as \"genderId\", " 
					 + " 	gendermstr.gender_code as \"genderCode\", " 
					 + " 	to_char( patientreg.dob, 'DD-MM-YYYY HH:mm:ss' ) as \"dob\", " 
					 + " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, patientreg.dob)) , 'Y ' , extract(month from AGE(CURRENT_TIMESTAMP, patientreg.dob)), 'M ', extract(day from AGE(CURRENT_TIMESTAMP, patientreg.dob)), 'D ') as \"ageString\", " 
					 + " 	encountermstr.encounter_id as \"encounterId\", " 
					 + " 	encountermstr.doctor_id as \"doctorId\", " 
					 + " 	( 'Dr. ' || coalesce(doctormstr.first_name)|| ' ' || coalesce(doctormstr.middle_name)|| ' ' || coalesce(doctormstr.last_name) ) as \"doctorName\", " 
					 + " 	servicemstr.speciality_id as \"specialityId\", " 
					 + " 	specialitymstr.speciality_name as \"specialityName\", " 
					 + " 	servicemstr.service_standard_name as \"serviceName\", " 
					 + " 	servicemstr.service_master_id as \"serviceId\", " 
					 + " 	servicemstr.service_standard_code as \"serviceCode\", " 
					 + " 	orderdetails.priority_id as \"priorityId\", " 
					 + " 	ordermaster.order_no as \"orderNo\", " 
					 + " 	to_char( ordermaster.order_date, 'dd/mm/yyyy' ) as \"orderDate\", " 
					 + " 	encountermstr.encounter_type_id as \"encounterTypeId\", " 
					 + " 	coalesce( encountermstr.is_critical, 'N' ) as \"isCritical\", " 
					 + " 	patientreg.is_vip as \"isVip\", " 
					 + " 	( " 
					 + " 	case " 
					 + " 		when patientreg.is_vip = 'Y' then coalesce( patientreg.alise_name, patientreg.first_name ) " 
					 + " 		else concat( coalesce(patientreg.first_name), ' ', coalesce(patientreg.middle_name), ' ', coalesce(patientreg.last_name)) " 
					 + " 	end ) as \"patientName\", " 
					 + " 	orderdetails.accession_number as \"accessionNumber\", " 
					 + " 	ordermaster.visit_type_id as \"visitTypeId\", " 
					 + " 	patientreg.identification_number as \"identificationNumber\", " 
					 + " 	vm.visit_type_name as \"visitName\", " 
					 + " 	pcm.patient_category as \"patientCategory\", " 
					 + " 	(case " 
					 + " 		when ordermaster.visit_type_id != 1 then concat(coalesce(ward.ward_name, ''), ', ', coalesce(room.room_desc, ''), ', ', coalesce(bed.bed_number, '')) " 
					 + " 		else '-' " 
					 + " 	end)as \"location\", " 
					 + " 	(case " 
					 + " 		when ordermaster.visit_type_id != 1 then admission.admission_number " 
					 + " 		else encountermstr.encounter_number " 
					 + " 	end) as \"visitNumber\", " 
					 + " 	prefxmstr.prefix_code as \"prefixDesc\", " 
					 + " 	patientreg.image_path as \"imagePath\" " 
					 + " from " 
					 + " 	public.t_order_details orderdetails " 
					 + " inner join public.t_order_master ordermaster on " 
					 + " 	orderdetails.order_id = ordermaster.order_id " 
					 + " inner join patient.t_patient_registration patientreg on " 
					 + " 	ordermaster.patient_id = patientreg.patient_id " 
					 + " inner join public.m_prefix_master prefixmstr on " 
					 + " 	patientreg.prefix_id = prefixmstr.prefix_id " 
					 + " inner join public.m_gender_master gendermstr on " 
					 + " 	patientreg.gender_id = gendermstr.gender_id " 
					 + " inner join patient.t_patient_details patdtl on " 
					 + " 	patdtl.patient_id = patientreg.patient_id " 
					 + " left outer join adt.t_admission admission on " 
					 + " 	ordermaster.admission_id = admission.admission_id " 
					 + " 	and admission.status = 'A' " 
					 + " left outer join adt.t_admission_details dtls on " 
					 + " 	dtls.admission_id = admission.admission_id " 
					 + " 	and dtls.status = 'A' " 
					 + " left join public.m_prefix_master prefxmstr on " 
					 + " 	prefxmstr.prefix_id = patientreg.prefix_id " 
					 + " inner join public.m_visit_type_master vm on " 
					 + " 	vm.visit_type_id = ordermaster.visit_type_id " 
					 + " left join public.m_patient_category_master pcm on " 
					 + " 	pcm.patient_category_id = patientreg.patient_category_id " 
					 + " left outer join opd.t_encounter_master encountermstr on " 
					 + " 	ordermaster.encounter_id = encountermstr.encounter_id " 
					 + " left join billing.t_billing_details billingdtls on " 
					 + " 	orderdetails.order_details_id = billingdtls.order_details_id " 
					 + " left join billing.m_billing_master billingmstr on " 
					 + " 	billingdtls.billing_master_id = billingmstr.billing_master_id " 
					 + " 	and billingmstr.bill_cancelled = 'N' " 
					 + " left outer join doctor.m_doctor_master doctormstr on " 
					 + " 	orderdetails.ord_doc_id = doctormstr.doctor_id " 
					 + " left outer join doctor.m_speciality_master specialitymstr on " 
					 + " 	orderdetails.ord_doc_spl_id = specialitymstr.speciality_id " 
					 + " inner join service.m_service_master servicemstr on " 
					 + " 	orderdetails.service_id = servicemstr.service_master_id " 
					 + " left outer join adt.m_bed_master bed on " 
					 + " 	bed.bed_id = dtls.bed_id " 
					 + " left outer join adt.m_ward_master ward on " 
					 + " 	ward.ward_id = bed.ward_id " 
					 + " left outer join adt.m_room_master room on " 
					 + " 	room.room_id = bed.room_id " 
					 + " inner join t_unit_application_billing_configuration config on " 
					 + " 	config.unit_id = orderdetails.org_unit_id " 
					 + " left join billing.t_credit_note_detail tcnd on " 
					 + " 	tcnd.order_detail_id = orderdetails.order_details_id " 
					 + " 	and tcnd.status = 'A' " 
					 + " 	and tcnd.cancel_or_discount = 1 " 
					 + " where " 
					 + " 	servicemstr.speciality_id not in (:deptList) " 
					 + " 	and orderdetails.service_rendered = 0 " 
					 + " 	and (orderdetails.status = 'A' and tcnd.credit_note_detail_id is null) " 
					 + " 	and orderdetails.is_package = false " 
					 + " 	and ordermaster.org_id =:orgId " 
					 + " 	and ordermaster.org_unit_id =:unitId "
					 + "    and servicemstr.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id =:unitId and is_rendering_speciality = 'Y' ) " 
					 + " 	and " 
					 + " 	(case " 
					 + " 		when ordermaster.visit_type_id != 1 then dtls.nursing_acceptance = 'Y' " 
					 + " 		else ( dtls.nursing_acceptance is null) " 
					 + " 	end) " 
					 + " 	and ((ordermaster.visit_type_id = 1 " 
					 + " 		and orderdetails.service_is_billed = 1 " 
					 + " 		and billingmstr.bill_status_id != 1 " 
					 + " 		and config.is_pre_or_post_billing = 'Y') " 
					 + " 	or (ordermaster.visit_type_id = 1 " 
					 + " 		and config.is_pre_or_post_billing = 'N') " 
					 + " 	or (ordermaster.visit_type_id != 1)) ");

			if (radiologyOrdersListDto.getFromDate() != null && radiologyOrdersListDto.getToDate() != null) {
				query.append(" AND to_char(ordermaster.order_date, 'YYYY-MM-DD HH:mm:ss') BETWEEN :fromDate AND :toDate ");
			}
			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId ");
			}
			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}
			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				query.append(" AND ordermaster.visit_type_id IN :visitTypeList ");
			}
			    StringBuilder sb1 = query;
				Integer count=0;
				Query createQ1 =sessionFactory.getCurrentSession().createSQLQuery(sb1.toString())
						.setParameter("unitId", radiologyOrdersListDto.getUnitId())
						.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
						.setParameterList("deptList", radiologyOrdersListDto.getDeptList());
			if (radiologyOrdersListDto.getFromDate() != null && radiologyOrdersListDto.getToDate() != null) {
				String fromDate = radiologyOrdersListDto.getFromDate() + " 00:00:00";
				String toDate = radiologyOrdersListDto.getToDate() + " 23:59:59";
				createQ1.setParameter("fromDate", fromDate);
				createQ1.setParameter("toDate", toDate);
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				createQ1.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				createQ1.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}

			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				createQ1.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			List<RadiologyOrdersListDto> listServiceMasterDto1 =  	createQ1.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
				
				 if(listServiceMasterDto1 != null && listServiceMasterDto1.size()>0) {
			    	  count = listServiceMasterDto1.size();
			      }

			Query createQ2 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", radiologyOrdersListDto.getUnitId())
					.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
					.setParameterList("deptList", radiologyOrdersListDto.getDeptList())
					.setFirstResult(radiologyOrdersListDto.getOffset() != null ? radiologyOrdersListDto.getOffset()  : 0)
					.setMaxResults(radiologyOrdersListDto.getNoOfRecordsPerPage() != null ? radiologyOrdersListDto.getNoOfRecordsPerPage()  : 10);
			if (radiologyOrdersListDto.getFromDate() != null && radiologyOrdersListDto.getToDate() != null) {
				String fromDate = radiologyOrdersListDto.getFromDate() + " 00:00:00";
				String toDate = radiologyOrdersListDto.getToDate() + " 23:59:59";
				createQ2.setParameter("fromDate", fromDate);
				createQ2.setParameter("toDate", toDate);
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				createQ2.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				createQ2.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}

			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				createQ2.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			List<RadiologyOrdersListDto> listServiceMasterDto = 		createQ2.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, count);
			
		}catch (Exception e) {
		logger.error("getOtherServicesForRendering failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateChildEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			if (encounterMasterDto.getAgainstEncId() != null && encounterMasterDto.getAgainstEncId() > 0 && encounterMasterDto.getDoctorId()!=null && encounterMasterDto.getDoctorId()>0) 
			{
				String updateQuery = "update opd.t_encounter_ehc_consultation_mppr set "
						+ "	child_encounter_id = :childEncounterId  "
						+ " where "
						+ "	service_id = :serviceId "
						+ "	and doctor_id = :doctorId "
						+ " and consultation_speciality_id=:specialityId "
						+ " and encounter_id =:parentEncounterId and  consultation_mppr_id =:consultationMpprId";
				
				sessionFactory.getCurrentSession().createSQLQuery(updateQuery)
				.setParameter("doctorId", encounterMasterDto.getDoctorId())
				.setParameter("specialityId", encounterMasterDto.getSpecialityId())
				.setParameter("childEncounterId", encounterMasterDto.getChildEncounterId())
				.setParameter("parentEncounterId", encounterMasterDto.getAgainstEncId())
				.setParameter("consultationMpprId", encounterMasterDto.getConsultationMpprId())
				.setParameter("serviceId", encounterMasterDto.getServiceId()).executeUpdate();
			}
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("updateChildEncounter failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public EncounterMasterDto getEncounterDetailsForUpdatePackage(Integer encounterId) {
		try {
			
			StringBuilder query = new StringBuilder("select " 
			 + " 	enc.organization_id as \"organizationId\", " 
			 + " 	enc.unit_id as \"unitId\", " 
			 + " 	enc.payment_entitlement_type_id as \"paymentEntitlementId\", " 
			 + " 	enc.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " 
			 + " 	enc.dept_id as \"deptId\", " 
			 + " 	spl.dept_wise_appointment as \"deptWiseAppointment\", " 
			 + " 	(case when spl.dept_wise_appointment='Y' then " 
			 + " 		0  " 
			 + " 	else " 
			 + " 	 	enc.doctor_id end) as \"doctorId\", "
			 + "	tpsd.payee_id as \"companyId\", "
			 + "	tpsd.insurance_id as \"assoCompanyId\" " 
			 + " from " 
			 + " 	opd.t_encounter_master enc " 
			 + " 	inner join t_unit_speciality_mapper spl on "
			 + " 		enc.dept_id = spl.speciality_id and spl.unit_id =enc.unit_id  "
			 + "	inner join opd.t_patient_sponsor_details tpsd on 	"
			 + "		tpsd.encounter_id = enc.encounter_id "
			 + "		and tpsd.priority_id =1 and tpsd.status='A' "
			 + " where " 
			 + " 	enc.encounter_id =:encounterId" );
			
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return  encounterMasterDto;
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getEncounterDetailsForUpdatePackage failed", e);
		}
		return null;
	}

	@Override
	public Response getPatientListForPhysiotherapyWorkbench(PhysiotherapyOrdersListDto physiotherapyOrdersListDto) {
		try {
			StringBuilder OPquery = new StringBuilder(" select " 
					 + " 	distinct (ordDet.order_details_id) as \"orderDetailsId\", " 
					 + " 	ordDet.ord_doc_id as \"ordDoctorId\", " 
					 + " 	ordDet.status as \"status\", " 
					 + " 	ordDet.service_rendered as \"serviceRendered\", " 
					 + " 	patReg.patient_id as \"patientId\", " 
					 + " 	patReg.uhid_number as \"uhidNumber\", " 
					 + " 	patReg.gender_id as \"genderId\", " 
					 + " 	genMas.gender_code as \"genderCode\", " 
					 + " 	prefMas.prefix_code as \"prefixCode\", " 
					 + " 	to_char(patReg.dob, 'DD-MM-YYYY HH:mm:ss') as \"dob\", " 
					 + " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, patReg.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, patReg.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, patReg.dob)), 'D ') as \"ageString\", " 
					 + " 	encMas.encounter_id as \"encounterId\", " 
					 + " 	encMas.doctor_id as \"doctorId\", " 
					 + " 	('Dr. ' || coalesce(docMas.first_name)|| ' ' || coalesce(docMas.middle_name)|| ' ' || coalesce(docMas.last_name)) as \"doctorName\", " 
					 + " 	servMas.speciality_id as \"specialityId\", " 
					 + " 	specMas.speciality_name as \"specialityName\", " 
					 + " 	servMas.service_standard_name as \"serviceName\", " 
					 + " 	servMas.service_master_id as \"serviceId\", " 
					 + " 	servMas.service_standard_code as \"serviceCode\", " 
					 + " 	ordDet.priority_id as \"priorityId\", " 
					 + " 	ordMas.order_no as \"orderNo\", " 
					 + " 	to_char(ordDet.order_date, 'dd/mm/yyyy') as \"orderDate\", " 
					 + " 	encMas.encounter_type_id as \"encounterTypeId\", " 
					 + " 	coalesce(encMas.is_critical, 'N') as \"isCritical\" , " 
					 + " 	patReg.is_vip as \"isVip\", " 
					 + " 	(case " 
					 + " 		when patReg.is_vip = 'Y' then coalesce(patReg.alise_name, patReg.first_name) " 
					 + " 		else concat(coalesce(patReg.first_name) , ' ', coalesce(patReg.middle_name) , ' ', coalesce(patReg.last_name)) " 
					 + " 	end) as \"patientName\", " 
					 + " 	tempTrans.template_transaction_id as \"templateTransactionId\", " 
					 + " 	tempTrans.template_object as \"templateObject\", " 
					 + " 	tempMas.template_format_id as \"templateFormatId\" , " 
					 + " 	'-' as \"bedNumber\" , " 
					 + " 	ordDet.accession_number as \"accessionNumber\" , " 
					 + " 	ordMas.visit_type_id as \"visitTypeId\", " 
					 + " 	patReg.identification_number as \"identificationNumber\", " 
					 + " 	vm.visit_type_name as \"visitName\", " 
					 + " 	pcm.patient_category as \"patientCategory\" , " 
					 + " 	encMas.encounter_number as \"visitNumber\", " 
					 + " 	cm.country_code as \"countryCode\","
					 + "    billMas.billing_master_id as \"billingMasterId\","
					 + " 	pm.priority_desc as \"priorityName\", " 
					 + " 	'-' as \"wardName\" " 
					 + " from " 
					 + " 	public.t_order_details ordDet " 
					 + " inner join public.t_order_master ordMas on " 
					 + " 	ordDet.order_id = ordMas.order_id " 
					 + " inner join patient.t_patient_registration patReg on " 
					 + " 	ordMas.patient_id = patReg.patient_id " 
					 + " inner join public.m_prefix_master prefMas on " 
					 + " 	patReg.prefix_id = prefMas.prefix_id " 
					 + " inner join public.m_gender_master genMas on " 
					 + " 	patReg.gender_id = genMas.gender_id " 
					 + " inner join patient.t_patient_details patdtl on " 
					 + " 	patdtl.patient_id = patReg.patient_id " 
					 + " inner join public.m_visit_type_master vm on " 
					 + " 	vm.visit_type_id = ordMas.visit_type_id " 
					 + " 	and vm.status = 'A' " 
					 + " left join public.m_patient_category_master pcm on " 
					 + " 	pcm.patient_category_id = patReg.patient_category_id " 
					 + " 	and pcm.status = 'A' " 
					 + " left join public.m_country_master cm on " 
					 + " 	cm.country_id = patdtl.country_id " 
					 + " inner join opd.t_encounter_master encMas on " 
					 + " 	ordMas.encounter_id = encMas.encounter_id " 
					 + " left outer join adt.t_admission adm on " 
					 + " 	ordMas.admission_id = adm.admission_id " 
					 + " left join billing.t_billing_details billDet on " 
					 + " 	ordDet.order_details_id = billDet.order_details_id " 
					 + " inner join billing.m_billing_master billMas on " 
					 + " 	ordDet.service_bill_id = billMas.billing_master_id " 
					 + " 	and billMas.bill_cancelled = 'N' " 
					 + " left outer join doctor.m_doctor_master docMas on " 
					 + " 	ordDet.ord_doc_id = docMas.doctor_id " 
					 + " left join doctor.m_speciality_master specMas on " 
					 + " 	ordDet.ord_doc_spl_id = specMas.speciality_id " 
					 + " left outer join public.t_template_transaction tempTrans on " 
					 + " 	ordDet.order_details_id = tempTrans.order_details_id " 
					 + " 	and (tempTrans.status = 'A') " 
					 + " left outer join public.t_template_master tempMas on " 
					 + " 	tempTrans.template_id = tempMas.template_id " 
					 + " 	and (tempMas.template_type_id = 13) " 
					 + " inner join opd.m_appointment_master appMstr on " 
					 + " 	appMstr.appointment_id = encMas.appointment_id " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	sm.slot_id = appMstr.slot_id " 
					 + " inner join service.m_service_master servMas on " 
					 + " 	ordDet.service_id = servMas.service_master_id " 
					 + " left join adt.m_priority_master pm on " 
					 + " 	pm.priority_id = ordDet.priority_id " 
					 + " left join t_unit_application_billing_configuration appConfig on " 
					 + " 	appConfig.unit_id = ordDet.org_unit_id " 
					 + " left join billing.t_credit_note_detail tcnd on " 
					 + " 	tcnd.order_detail_id = ordDet.order_details_id " 
					 + " 	and tcnd.status = 'A' " 
					 + " 	and tcnd.cancel_or_discount = 1 " 
					 + " where " 
					 + " 	servMas.speciality_id =:physiotherapySpecialityId " 
					 + " 	and ordDet.service_rendered = 0 " 
					 + " 	and ordMas.org_id =:orgId " 
					 + " 	and ordMas.org_unit_id =:unitId " 
					 + " 	and (ordDet.status = 'A' and tcnd.credit_note_detail_id is null) " 
					 + " 	and (appConfig.is_pre_or_post_billing = 'N' " 
					 + " 		or (appConfig.is_pre_or_post_billing = 'Y' " 
					 + " 			and billMas.bill_status_id <> 1)) " 
					 + " 	and ordMas.visit_type_id = 1 " 
					 + " 	and billMas.bill_status_id != 1 " 
					 + " 	and ordDet.is_order_visible_dept = 'A' ");
			
			StringBuilder IPQuery = new StringBuilder(" select " 
					+ " 	ordDet.order_details_id as \"orderDetailsId\", " 
					+ " 	ordDet.ord_doc_id as \"ordDoctorId\", " 
					+ " 	ordDet.status as \"status\", " 
					+ " 	ordDet.service_rendered as \"serviceRendered\", " 
					+ " 	patReg.patient_id as \"patientId\", " 
					+ " 	patReg.uhid_number as \"uhidNumber\", " 
					+ " 	patReg.gender_id as \"genderId\", " 
					+ " 	genMas.gender_code as \"genderCode\", " 
					+ " 	prefMas.prefix_code as \"prefixCode\", " 
					+ " 	to_char(patReg.dob, 'DD-MM-YYYY HH:mm:ss') as \"dob\", " 
					+ " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, patReg.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, patReg.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, patReg.dob)), 'D ') as \"ageString\", " 
					+ " 	adm.admission_id as \"encounterId\", " 
					+ " 	encMast.doctor_id as \"doctorId\", " 
					+ " 	('Dr. ' || coalesce(docMas.first_name)|| ' ' || coalesce(docMas.middle_name)|| ' ' || coalesce(docMas.last_name)) as \"doctorName\", " 
					+ " 	servMas.speciality_id as \"specialityId\", " 
					+ " 	specMast.speciality_name as \"specialityName\", " 
					+ " 	servMas.service_standard_name as \"serviceName\", " 
					+ " 	servMas.service_master_id as \"serviceId\", " 
					+ " 	servMas.service_standard_code as \"serviceCode\", " 
					+ " 	ordDet.priority_id as \"priorityId\", " 
					+ " 	ordMas.order_no as \"orderNo\", " 
					+ " 	to_char(ordDet.order_date, 'dd/mm/yyyy') as \"orderDate\", " 
					+ " 	encMast.encounter_type_id as \"encounterTypeId\", " 
					+ " 	coalesce(encMast.is_critical, 'N') as \"isCritical\" , " 
					+ " 	patReg.is_vip as \"isVip\", " 
					+ " 	(case " 
					+ " 		when patReg.is_vip = 'Y' then coalesce(patReg.alise_name, patReg.first_name) " 
					+ " 		else concat(coalesce(patReg.first_name) , ' ', coalesce(patReg.middle_name) , ' ', coalesce(patReg.last_name)) " 
					+ " 	end) as \"patientName\", " 
					+ " 	tempTrans.template_transaction_id as \"templateTransactionId\", " 
					+ " 	tempTrans.template_object as \"templateObject\", " 
					+ " 	tempMast.template_format_id as \"templateFormatId\" , " 
					+ " 	bedMstr.bed_number as \"bedNumber\" , " 
					+ " 	ordDet.accession_number as \"accessionNumber\" , " 
					+ " 	ordMas.visit_type_id as \"visitTypeId\" , " 
					+ " 	patReg.identification_number as \"identificationNumber\", " 
					+ " 	vm.visit_type_name as \"visitName\" , " 
					+ " 	pcm.patient_category as \"patientCategory\", " 
					+ " 	adm.admission_number as \"visitNumber\", " 
					+ " 	cm.country_code as \"countryCode\", " 
					+ "    billMast.billing_master_id as \"billingMasterId\","
					+ " 	pm.priority_desc as \"priorityName\", " 
					+ " 	wardMast.ward_name as \"wardName\" " 
					+ " from " 
					+ " 	public.t_order_details ordDet " 
					+ " inner join public.t_order_master ordMas on " 
					+ " 	ordDet.order_id = ordMas.order_id " 
					+ " inner join patient.t_patient_registration patReg on " 
					+ " 	ordMas.patient_id = patReg.patient_id " 
					+ " inner join public.m_prefix_master prefMas on " 
					+ " 	patReg.prefix_id = prefMas.prefix_id " 
					+ " inner join public.m_gender_master genMas on " 
					+ " 	patReg.gender_id = genMas.gender_id " 
					+ " inner join patient.t_patient_details patdtl on " 
					+ " 	patdtl.patient_id = patReg.patient_id " 
					+ " inner join public.m_visit_type_master vm on " 
					+ " 	vm.visit_type_id = ordMas.visit_type_id " 
					+ " 	and vm.status = 'A' " 
					+ " left join public.m_patient_category_master pcm on " 
					+ " 	pcm.patient_category_id = patReg.patient_category_id " 
					+ " 	and pcm.status = 'A' " 
					+ " left join public.m_country_master cm on " 
					+ " 	cm.country_id = patdtl.country_id " 
					+ " left outer join opd.t_encounter_master encMast on " 
					+ " 	ordMas.encounter_id = encMast.encounter_id " 
					+ " left outer join adt.t_admission adm on " 
					+ " 	ordMas.admission_id = adm.admission_id " 
					+ " left join adt.t_admission_details dtl on " 
					+ " 	dtl.admission_id = adm.admission_id " 
					+ " 	and adm.status = 'A' " 
					+ " 	and dtl.status = 'A' " 
					+ " left join adt.m_bed_master bedMstr on " 
					+ " 	dtl.bed_id = bedMstr.bed_id " 
					+ " left outer join billing.t_billing_details billDet on " 
					+ " 	ordDet.order_details_id = billDet.order_details_id " 
					+ " left outer join billing.m_billing_master billMast on " 
					+ " 	billDet.billing_master_id = billMast.billing_master_id " 
					+ " 	and billMast.bill_cancelled = 'N' " 
					+ " left outer join doctor.m_doctor_master docMas on " 
					+ " 	ordDet.ord_doc_id = docMas.doctor_id " 
					+ " left outer join doctor.m_speciality_master specMast on " 
					+ " 	ordDet.ord_doc_spl_id = specMast.speciality_id " 
					+ " left outer join public.t_template_transaction tempTrans on " 
					+ " 	ordDet.order_details_id = tempTrans.order_details_id " 
					+ " 	and (tempTrans.status = 'A') " 
					+ " left outer join public.t_template_master tempMast on " 
					+ " 	tempTrans.template_id = tempMast.template_id " 
					+ " 	and (tempMast.template_type_id = 13) " 
					+ " left join opd.m_appointment_master appMstr on " 
					+ " 	appMstr.order_details_id = ordDet.order_details_id " 
					+ " left join opd.m_slot_master sm on " 
					+ " 	sm.slot_id = appMstr.slot_id " 
					+ " inner join service.m_service_master servMas on " 
					+ " 	ordDet.service_id = servMas.service_master_id " 
					+ " left join adt.m_priority_master pm on " 
					+ " 	pm.priority_id = ordDet.priority_id " 
					+ " left join adt.m_ward_master wardMast on " 
					+ " 	wardMast.ward_id = dtl.ward_id " 
					+ " left join billing.t_credit_note_detail tcnd on " 
					+ " 	tcnd.order_detail_id = ordDet.order_details_id " 
					+ " 	and tcnd.status = 'A' " 
					+ " 	and tcnd.cancel_or_discount = 1 " 
					+ " where " 
					+ " 	servMas.speciality_id =:physiotherapySpecialityId " 
					+ " 	and ordDet.service_rendered = 0 " 
					+ " 	and ordMas.org_id =:orgId " 
					+ " 	and ordMas.org_unit_id =:unitId " 
					+ " 	and (ordDet.status = 'A' and tcnd.credit_note_detail_id is null) " 
					+ " 	and ordMas.visit_type_id in (2, 3, 4) " 
					+ " 	and ordDet.is_order_visible_dept = 'A' ");
			
			
			
			if (physiotherapyOrdersListDto.getFromDate() != null){
				IPQuery.append(" AND to_char(ordDet.order_date,'YYYY-MM-DD') between :fromDate AND :toDate");
				OPquery.append(" AND to_char(ordDet.order_date,'YYYY-MM-DD') between :fromDate AND :toDate");
				//OPPostBilling.append(" AND to_char(orderdetai0_.order_date,'YYYY-MM-DD') between '" + physiotherapyOrdersListDto.getFromDate().substring(0, 10)+"' and  '" + physiotherapyOrdersListDto.getToDate().substring(0, 10) + "'");
			}
				
			if(physiotherapyOrdersListDto.getPatientId() !=null && physiotherapyOrdersListDto.getPatientId() > 0){
				IPQuery.append(" and ordMas.patient_id=:patientId ");
				OPquery.append(" and ordMas.patient_id=:patientId ");
				//OPPostBilling.append(" and ordermaste1_.patient_id="+physiotherapyOrdersListDto.getPatientId());
			}
			
			if(physiotherapyOrdersListDto.getServiceId()!=null && physiotherapyOrdersListDto.getServiceId() > 0){
				IPQuery.append(" and ordDet.service_id=:serviceId ");
				OPquery.append(" and ordDet.service_id=:serviceId ");
				//OPPostBilling.append(" and orderdetai0_.service_id="+physiotherapyOrdersListDto.getServiceId());1
			}
			
			if(physiotherapyOrdersListDto.getVisitList()!=null && physiotherapyOrdersListDto.getVisitList().size() > 0){
				IPQuery.append(" and ordMas.visit_type_id IN :visitTypeList ");
				OPquery.append(" and ordMas.visit_type_id IN :visitTypeList ");
				//OPPostBilling.append(" and ordermaste1_.visit_type_id IN :visitTypeList ");
			}
			
			StringBuilder finalQuery = new StringBuilder();
			
			
			finalQuery = OPquery.append(" union ").append(IPQuery);
			
			finalQuery.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
			
			Query query2 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString());
			
			if(physiotherapyOrdersListDto.getVisitList()!=null && physiotherapyOrdersListDto.getVisitList().size()>0) {
				query2.setParameterList("visitTypeList", physiotherapyOrdersListDto.getVisitList());
			}
			if (physiotherapyOrdersListDto.getFromDate() != null && physiotherapyOrdersListDto.getToDate() != null) {
				String fromDate = physiotherapyOrdersListDto.getFromDate();
				String toDate = physiotherapyOrdersListDto.getToDate();
				query2.setParameter("fromDate", fromDate);
				query2.setParameter("toDate", toDate);

			}
			if (physiotherapyOrdersListDto.getPatientId() != null && physiotherapyOrdersListDto.getPatientId() > 0) {
				query2.setParameter("patientId", physiotherapyOrdersListDto.getPatientId());
				query2.setParameter("patientId", physiotherapyOrdersListDto.getPatientId());
			}

			if (physiotherapyOrdersListDto.getServiceId() != null && physiotherapyOrdersListDto.getServiceId() > 0) {
				query2.setParameter("serviceId", physiotherapyOrdersListDto.getServiceId());
				query2.setParameter("serviceId", physiotherapyOrdersListDto.getServiceId());
			}

			List<RadiologyOrdersListDto> listServiceMasterDto = query2
					.setParameter("unitId", physiotherapyOrdersListDto.getUnitId())
					.setParameter("orgId", physiotherapyOrdersListDto.getOrganizationId())
					.setParameter("physiotherapySpecialityId", physiotherapyOrdersListDto.getPhysiotherapySpecialityId())
					.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (Exception e) {
		logger.error("getPatientListForPhysiotherapyWorkbench failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);

	}

	@Override
	public Response getOrderDetailByAppointment(EncounterMasterDto encounterMasterDto2) {
		try {
			List<OrderDetailsByEncounterIdDto> list=(List<OrderDetailsByEncounterIdDto>)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RENDERED_SERVICES_BY_APPOINTMENT_ID")
					.setParameter("appointmentId", encounterMasterDto2.getAppointmentId())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();
			if(list!=null && list.size()>0)
			{
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, list.get(0));
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	 }catch (Exception e) {
	 logger.error("getOrderDetailByAppointment failed", e);
	 }
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);

	}

	@Override
	public Response getEncountersDetailsForCanelledBills(Integer encounterId) {
		try {
			String query = " SELECT "
					   + " distinct(encounter.encounterId) as encounterId, "
					   + " encounter.encounterNumber as encounterNumber, "
					   + " encounter.doctorId as doctorId, "
					   + " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
					   + " encounter.deptId as specialityId, "
					   + " encounter.subDeptId as subSpecialityId, "
					   + " speciality.specialityName as specialityName, "
					   + " subSpeciality.subSpecialityMasterName as subSpecialityName, "
					   + " encounter.modalityId as modalityId, "
					   + " modality.modalityDesc as modalityDesc,  "
					   + " appoint.ehcPackageId as ehcPackageId, "
					   + " appoint.appointmentTypeId as appointmentTypeId "
					   + " FROM EncounterMaster encounter "
					   + " LEFT JOIN encounter.appointmentMaster appoint "
					   + " LEFT JOIN encounter.doctorMaster doc "
					   + " LEFT JOIN encounter.subSpecialityMaster subSpeciality "
					   + " LEFT JOIN encounter.modalityMaster modality "
					   + " LEFT JOIN encounter.specialityMaster speciality "
					   + " WHERE encounter.encounterId=:encounterId "
					   + " ORDER BY encounterId desc ";
			
			List<EncounterMasterDto> appointmentStatusMasterDtoList = sessionFactory.getCurrentSession()
					.createQuery(query)
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, appointmentStatusMasterDtoList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getEncountersDetailsForCanelledBills failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getConsentParamDetails(List<String> paramNames) {
		try{
			
			String Query = "select "
					+ " code as \"code\", "
					+ " query as \"query\" "
				+ " from "
					+ " public.t_consent_values "
				+ " where "
					+ " code in (:codes) ";
					
			List<PatientConsentMapperDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(Query)
					.setParameterList("codes", paramNames)
					.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).list();
			
			/*StringBuilder query = new StringBuilder("select ");
			
			if(list.size()>0)
			{
				int i = 0;
				for(PatientConsentMapperDto dto : list)
				{
					query.append(dto.getQuery());
					i++;
					
					if(list.size()!=i)
						query.append(", ");
					else
						query.append("; ");
				}
			}
			
			System.out.println(query);*/
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public String getResult(String query) {
		try{
			
			String param = (String)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.uniqueResult();
			
			return param!=null ? param : " " ;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAdmissionsById(int admissionId) throws BillingException {
		try{
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT").setParameter("admissionId", admissionId)
										.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response checkFeedBackFormSubmitted(TemplateTransactionDto dto) throws ApplicationException {
		try {
			TemplateTransactionDto templateTrDto =null;
					if(dto.getEncounterId()!=null && dto.getEncounterId()>0)
					{
						templateTrDto = (TemplateTransactionDto)sessionFactory.getCurrentSession().createSQLQuery("select "
								+ "	 concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
								+ "	 msm.speciality_name  as \"specialityName\", "
								+ "	 msm.speciality_id  as \"specialityId\", "
								+ "	 enc.feedback_form_submitted  as \"feedbackFormSubmitted\", "
								+ "	 enc.appointment_id as \"appointmentId\", "
								+ "	 enc.patient_id as \"patientId\", "
								+ "	 enc.doctor_id as \"doctorId\", "
								+ "	 enc.organization_id as \"orgId\", "
								+ "	 enc.unit_id as \"unitId\", "
								+ "	 to_char(enc.encounter_date, 'dd/MM/yyyy') as \"appointmentDate\", "
								+ " 	 to_char(enc.encounter_date, 'HH:mm:ss') as \"timeSlot\" "
								+ " 	 from opd.t_encounter_master enc "
								+ "	 left join doctor.m_speciality_master msm  on enc.dept_id  = msm.speciality_id "
								+ "	 left join doctor.m_doctor_master doc on enc.doctor_id  = doc.doctor_id "
								+ "where "
								+ "	 encounter_id = :encounterId")
							.setParameter("encounterId", dto.getEncounterId() != null ? dto.getEncounterId() : 0)
							.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class))
						.uniqueResult();
					}
					else if(dto.getLabSampleDtlsId()!=null && dto.getLabSampleDtlsId()>0)
						{
							templateTrDto = (TemplateTransactionDto)sessionFactory.getCurrentSession().createSQLQuery("select "
									+ "	 concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
									+ "	 msm.speciality_name  as \"specialityName\", "
									+ "	 msm.speciality_id  as \"specialityId\", "
									+ "	 enc.feedback_form_submitted  as \"feedbackFormSubmitted\", "
									+ "	 enc.appointment_id as \"appointmentId\", "
									+ "	 enc.patient_id as \"patientId\", "
									+ "	 enc.doctor_id as \"doctorId\", "
									+ "	 enc.organization_id as \"orgId\", "
									+ "	 enc.unit_id as \"unitId\", "
									+ "	 to_char(enc.encounter_date, 'dd/MM/yyyy') as \"appointmentDate\", "
									+ " 	 to_char(enc.encounter_date, 'HH:mm:ss') as \"timeSlot\", "
									+ " 	 enc.encounter_id as \"encounterId\" "
									+ " 	 from lab.t_lab_sample_dtls tlsd "
									+ " 	 inner join public.t_order_master tom on tlsd.order_id  = tom.order_id "
									+ " 	 inner join opd.t_encounter_master enc on tom.encounter_id  = enc.encounter_id "
									+ "	 left join doctor.m_speciality_master msm  on enc.dept_id  = msm.speciality_id "
									+ "	 left join doctor.m_doctor_master doc on enc.doctor_id  = doc.doctor_id "
									+ "where "
									+ "	 tlsd.lab_sample_dtls_id = :labSampleDtlsId")
								.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId() != null ? dto.getLabSampleDtlsId() : 0)
								.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class))
							.uniqueResult();
						}
					return new Response(SUCCESS, null, null, null, templateTrDto);
				} catch (HibernateException he) {
				logger.error("checkFeedBackFormSubmitted failed", he);
				} catch (Exception e) {
				logger.error("checkFeedBackFormSubmitted failed", e);
				}
				return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBackFormSubmitted(TemplateTransactionDto dto) throws ApplicationException {
		try {
			
			if(dto.getEncounterId()!=null && dto.getEncounterId()>0)
			{
				sessionFactory.getCurrentSession().createQuery("update EncounterMaster set "
						+ "feedbackFormSubmitted=:feedbackFormSubmitted "
						+ "WHERE encounterId=:encounterId")
					.setBoolean("feedbackFormSubmitted", true)
					.setParameter("encounterId", dto.getEncounterId())
				.executeUpdate();
			}
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("updateBackFormSubmitted failed", he);
		} catch (Exception e) {
		logger.error("updateBackFormSubmitted failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateFollowUpDateOfPatient(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			String fromTimeString = "";
			if(encounterMasterDto.getSlotId()!=null && encounterMasterDto.getSlotId()>0){
				java.sql.Time fromTime = (java.sql.Time) sessionFactory.getCurrentSession().createSQLQuery(
					    " select from_time from opd.m_slot_master "
								+ " where slot_id =:slotId "
								+ " and  status ='A' limit 1")
						.setParameter("slotId", encounterMasterDto.getSlotId())
						.uniqueResult();

				fromTimeString = fromTime.toString();
			}
			
			if(encounterMasterDto.getPatientId()!=null && encounterMasterDto.getPatientId() > 0) 
			{
				String updatedDateTimeString = encounterMasterDto.getFollowUpDate();
				if(fromTimeString != null && !fromTimeString.isEmpty()) {

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

					LocalDateTime dateTime = LocalDateTime.parse(encounterMasterDto.getFollowUpDate(), formatter);

					LocalTime newTime = LocalTime.parse(fromTimeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
					LocalDateTime updatedDateTime = dateTime.withHour(newTime.getHour())
							.withMinute(newTime.getMinute())
							.withSecond(newTime.getSecond());

					updatedDateTimeString = updatedDateTime.format(formatter);
				}

				sessionFactory.getCurrentSession().createSQLQuery("update  opd.t_encounter_master  set follow_up_date =:followUpDate "
						+ " where patient_id=:patientId"
						+ " and  encounter_id =:encounterId")
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("followUpDate", GlobalCommonDateUtils.getDate(updatedDateTimeString, "yyyy-M-dd HH:mm:ss"))
				.executeUpdate();
			}
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("updateFollowUpDateOfPatient failed", he);
		} catch (Exception e) {
		logger.error("updateFollowUpDateOfPatient failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

    @Override
    public Response getSearchedListEncounterMasterForNursingAssToken(SlotMasterDto slotMasterDto)
            throws ApplicationException {
        try {
            Integer count = 0;
            StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST_NURSING_ASS_TOKEN").getQueryString().toString());

			if( slotMasterDto.getSpecialityIdList() != null){
				stringQuery.append(" AND enMst.dept_id IN (:deptList) "    );
			}

                stringQuery.append(" AND (am.appointment_type_id != 6 and enMst.against_enc_id IS NULL) "    );
                stringQuery.append(" AND (tod.service_rendered IS NOT NULL AND (am.appointment_type_id =1 OR am.appointment_type_id = 9 ) )"    );
                
                
                if (slotMasterDto.getFromDate() != null) {
                	stringQuery.append(" AND to_char(enMst.encounter_date,'yyyy-mm-dd') Between :fromDate AND :toDate");
                }
                    stringQuery.append(" order by enMst.encounter_date asc, enDet.is_priority = 'Y',enMst.is_critical = 'Y',EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) > 60 desc,enMst.dummy_token_number ");
            //StringBuilder finalQuery  = this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto);
            
            SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());    
           Query sql = query
                .setParameter("orgId", slotMasterDto.getOrganizationId())
                .setParameter("unitId", slotMasterDto.getUnitId());

			if( slotMasterDto.getSpecialityIdList() != null){

				sql.setParameterList("deptList", slotMasterDto.getSpecialityIdList());
			}
			if (slotMasterDto.getFromDate() != null) {
				sql.setParameter("fromDate", slotMasterDto.getFromDate().substring(0, 10));  // Assuming the date format is 'yyyy-MM-dd'
				sql.setParameter("toDate", slotMasterDto.getToDate().substring(0, 10));
			}
			List<SlotMasterDto> slotMasterDtosList = sql.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
            count=slotMasterDtosList.size();
             
            return new Response(SUCCESS, null, null, slotMasterDtosList, count);
            
        } catch (HibernateException he) {
        logger.error("getSearchedListEncounterMasterForNursingAssToken failed", he);
        } catch (Exception e) {
        logger.error("getSearchedListEncounterMasterForNursingAssToken failed", e);
        }
        return new Response(ERROR, null, null, null, null);
    }
    

    @Override
    public Response universalSearchOpPatientListByDocId(EncounterMasterDto encounterMasterDto)
            throws ApplicationException {
try {
            List<Integer> deptIds = new LinkedList();
            String query = " select " 
                     + "     spl.speciality_id as \"specialityId\" " 
                     + " from " 
                     + "     doctor.t_doctor_speciality_mapper dsm " 
                     + " inner join t_unit_speciality_mapper spl on "
                     + "     spl.speciality_id = dsm.speciality_id " 
                     + " where " 
                     + "      dsm.status = 'A' "
                     + "    and spl.unit_id =:unitId "
                     + "     and spl.dept_wise_appointment='Y' ";
            
            if(encounterMasterDto.getIsRMO()!=null) {
                query=query.concat(" and dsm.doctor_id in (:doctorList) ");
            }else {
                query=query.concat(" and dsm.doctor_id =:doctorId ");
            }
            
            List<DoctorSpecialityMapperDto> list=new ArrayList<DoctorSpecialityMapperDto>();
            
            if(encounterMasterDto.getIsRMO()!=null) {
                 list = sessionFactory.getCurrentSession().createSQLQuery(query)
                            .setParameterList("doctorList", encounterMasterDto.getDoctorList())
						 	.setParameter("unitId", encounterMasterDto.getUnitId())
                            .setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
            }else {
                 list = sessionFactory.getCurrentSession().createSQLQuery(query)
						 .setParameter("unitId", encounterMasterDto.getUnitId())
						 .setParameter("doctorId", encounterMasterDto.getDoctorId())
                            .setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
            }
            
            if(list.size()>0)
            {
                for(DoctorSpecialityMapperDto dto :list)
                {
                    deptIds.add(dto.getSpecialityId());
                }
            }
            
            StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST").getQueryString().toString());

            if(encounterMasterDto.getIsRMO()!=null){
                if(!list.isEmpty()) {
                stringQuery.append("AND (((enMst.dept_id IN(:deptIds) and enMst.preferred_doc_id is null and enMst.doctor_id is null ) "
                        + " OR enMst.preferred_doc_id in(:doctorList))  OR enMst.doctor_id IN (:doctorList)) ");
                }else {
                    stringQuery.append(" AND enMst.doctor_id IN (:doctorList) ");
                }
            }else if(list.size()>0){
                stringQuery.append("AND (((enMst.dept_id IN (:deptIds) and enMst.preferred_doc_id is null and enMst.doctor_id is null ) OR enMst.preferred_doc_id = :doctorId");
                stringQuery.append(" OR enMst.doctor_id=:doctorId) ");
            }else{
                stringQuery.append(" AND enMst.doctor_id=:doctorId ");
            }
            
            if(encounterMasterDto.getStatusId()!=null && encounterMasterDto.getStatusId()>0)
            {
                if(encounterMasterDto.getStatusId() == 1)
                    stringQuery.append(" AND enDet.consultation_start_time is null");
                if(encounterMasterDto.getStatusId() == 2)
                    stringQuery.append(" AND enDet.consultation_start_time is not null AND enDet.consultation_stop_time is null ");
                if(encounterMasterDto.getStatusId() == 3)
                    stringQuery.append(" AND enDet.consultation_start_time is not null AND enDet.consultation_stop_time is not null ");
            }
            
            if(encounterMasterDto.getSearchString()!=null && encounterMasterDto.getSearchString()!="") {
                stringQuery.append(" AND ( lower(enMst.encounter_number) like :searchString"
                        +" OR to_char(enMst.encounter_date, 'DD/MM/YYYY' ) like :searchString"
                        +" OR lower(patient.first_name) like :searchString"
                        +" OR lower((case     when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " + 
                        "                                 else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " + 
                        "                                  end )) like :searchString"
                        +" OR lower(patient.uhid_number) like :searchString"
                        +" OR lower(concat('Dr. ', coalesce(prefDoc.first_name), ' ', coalesce(prefDoc.middle_name), ' ', coalesce(prefDoc.last_name))) like :searchString"
                        +" OR lower(concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) like :searchString"
                        +" OR concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') like :searchString"
                        +" OR cast(enMst.unit_token_number AS Char ) =:searchString "
                        +" OR to_char(enDet.consultation_start_time, 'HH24:MI:SS') like :searchString"
                        +" OR lower(enMst.encounter_number) like :searchString )");
            }
            
            stringQuery.append(" AND (enDet.assignment_start_time is not null or (am.is_teleconsultation_apppointment = 1 " 
                     + "     and mbm.bill_status_id = 3 and enMst.status_id in (5,6,9))) ");
            stringQuery.append(" AND to_char(enMst.encounter_date,'yyyy-mm-dd') Between :fromDate and :toDate");
            //stringQuery.append((encounterMasterDto.getStatusId() > 0 ) ? " AND enMst.status_id =" + encounterMasterDto.getStatusId() : " ");
            //stringQuery.append(" order by enMst.encounter_date desc, enDet.is_priority = 'Y',enMst.is_critical = 'Y',EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob))>60 desc,\"specialityQueueNumber\" ");
			if(encounterMasterDto.getDoctorId() != null) {
			stringQuery.append(" AND enMst.doctor_id=:doctorId ");
				}

            stringQuery.append(" order by enMst.encounter_date desc, enDet.is_priority desc,enMst.is_critical desc,EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) > 60 desc,\"specialityQueueNumber\" ");
            Integer count=null;
            List<SlotMasterDto> slotMasterDtosList=new ArrayList<>();
            if(encounterMasterDto.getIsRMO()!=null){
                if(!list.isEmpty()) {
                slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
                        .setParameter("orgId", encounterMasterDto.getOrganizationId())
                        .setParameter("unitId", encounterMasterDto.getUnitId())
                        .setParameterList("deptIds", deptIds)
						.setParameter("fromDate",  encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate",  encounterMasterDto.getToDate().substring(0, 10))
						.setParameter("searchString",  "%" + encounterMasterDto.getSearchString().toLowerCase().trim() + "%")
                        .setParameterList("doctorList", encounterMasterDto.getDoctorList())
                        .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
                }else {
                    slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
                            .setParameter("orgId", encounterMasterDto.getOrganizationId())
                            .setParameter("unitId", encounterMasterDto.getUnitId())
                            .setParameterList("doctorList", encounterMasterDto.getDoctorList())
							.setParameter("fromDate",  encounterMasterDto.getFromDate().substring(0, 10))
							.setParameter("toDate",  encounterMasterDto.getToDate().substring(0, 10))
							.setParameter("searchString",  "%" + encounterMasterDto.getSearchString().toLowerCase().trim() + "%")
                            .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
                }
              count=slotMasterDtosList.size();
            }else if(list.size()>0){
                slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
                        .setParameter("orgId", encounterMasterDto.getOrganizationId())
                        .setParameter("unitId", encounterMasterDto.getUnitId())
						.setParameter("doctorId", encounterMasterDto.getDoctorId())
						.setParameter("fromDate",  encounterMasterDto.getFromDate().substring(0, 10))
						.setParameter("toDate",  encounterMasterDto.getToDate().substring(0, 10))
						.setParameter("searchString",  "%" + encounterMasterDto.getSearchString().toLowerCase().trim() + "%")
                        .setParameterList("deptIds", deptIds)
                        .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
                     count=slotMasterDtosList.size();
            }else{
            slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
                .setParameter("orgId", encounterMasterDto.getOrganizationId())
                .setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("fromDate",  encounterMasterDto.getFromDate().substring(0, 10))
					.setParameter("toDate",  encounterMasterDto.getToDate().substring(0, 10))
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("searchString",  "%" + encounterMasterDto.getSearchString().toLowerCase().trim() + "%")
                .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
             count=slotMasterDtosList.size();
            }

            return new Response(SUCCESS, null, null, slotMasterDtosList, count);
            }catch (Exception e) {
            logger.error("universalSearchOpPatientListByDocId failed", e);
            }
            return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
    }

    @Override
    public Response getUniversalSearchedListEncounterMaster(SlotMasterDto slotMasterDto) throws ApplicationException {
        try {
            StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST").getQueryString().toString());
            if(slotMasterDto.getOffset()!=null && slotMasterDto.getNoOfRecordsPerPage()!=null) 
            {
                stringQuery.append(" AND enMst.dept_id IN (:deptList) "    );
                stringQuery.append(" AND (am.appointment_type_id != 6 and enMst.against_enc_id IS NULL) "    );
                stringQuery.append(" AND (tod.service_rendered IS NOT NULL AND (am.appointment_type_id =1 OR am.appointment_type_id = 9 ) )"    );
            }
            
            if(slotMasterDto.getSearchString()!=null && !slotMasterDto.getSearchString().isEmpty()) {
                stringQuery.append(" AND ( lower(enMst.encounter_number) like :searchString "
                        +" OR to_char(enMst.encounter_date, 'DD/MM/YYYY' ) like :searchString "
                        +" OR lower(patient.first_name) like :searchString "
                        +" OR lower((case     when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " + 
                        "                                 else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " + 
                        "                                  end )) like :searchString "
                        +" OR lower(patient.uhid_number) like :searchString "
                        +" OR lower(concat('Dr. ', coalesce(prefDoc.first_name), ' ', coalesce(prefDoc.middle_name), ' ', coalesce(prefDoc.last_name))) like :searchString "
                        +" OR lower(concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) like :searchString "
                        +" OR concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') like :searchString "
                        +" OR CAST(enMst.unit_token_number AS Char)  like :searchString "
                        +" OR lower(enMst.status) like :searchString "
                        +" OR lower(speciaityMaster.speciality_name) like :searchString "
                        +" OR lower((case when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'HH24:MI:SS') " + 
                        "        else to_char(enMst.encounter_date, 'HH24:MI:SS') " + 
                        "        end)) like :searchString "
                        +" OR lower(enMst.encounter_number) like :searchString)");
            }
            StringBuilder finalQuery  = this.getFinalQueryAfterAppendConditions(stringQuery,slotMasterDto);

			SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString());

			if (slotMasterDto.getPatientId() != null && slotMasterDto.getPatientId() > 0) {
				query.setParameter("patientId", slotMasterDto.getPatientId());
			}

			if (slotMasterDto.getFromDate() != null && slotMasterDto.getToDate() != null) {
				query.setParameter("fromDate", slotMasterDto.getFromDate().substring(0, 10));
				query.setParameter("toDate", slotMasterDto.getToDate().substring(0, 10));
			}

			if (slotMasterDto.getSpecialityId() != null && slotMasterDto.getSpecialityId() > 0) {
				query.setParameter("specialityId", slotMasterDto.getSpecialityId());
			}

			if (slotMasterDto.getDoctorId() != null && slotMasterDto.getDoctorId() > 0) {
				query.setParameter("doctorId", slotMasterDto.getDoctorId());
			}

			if (slotMasterDto.getNursingStationId() != null && slotMasterDto.getNursingStationId() > 0) {
				query.setParameter("nursingStationId", slotMasterDto.getNursingStationId());
			}

			if (slotMasterDto.getVisitTypeId() != null && slotMasterDto.getVisitTypeId() > 0) {
				query.setParameter("clinicId", slotMasterDto.getClinicId());
			}

			if (slotMasterDto.getSearchString() != null && !slotMasterDto.getSearchString().isEmpty()) {
				query.setParameter("searchString", "%" + slotMasterDto.getSearchString().toLowerCase().trim() + "%");
			}


            List<SlotMasterDto> slotMasterDtosList = query
                .setParameter("orgId", slotMasterDto.getOrganizationId())
                .setParameter("unitId", slotMasterDto.getUnitId())
                .setParameterList("deptList", slotMasterDto.getSpecialityIdList())
                .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
            return new Response(SUCCESS, null, null, slotMasterDtosList, null);
            
        } catch (HibernateException he) {
        logger.error("getUniversalSearchedListEncounterMaster failed", he);
        }
        return new Response(ERROR, null, null, null, null);
    }

	@Override
	public Response updateEncounterDetailsPatientByEncounterId(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
	 try {
		if (encounterMasterDto.getListKinDetailsDto().size() > 0) 
		{
			Iterator<KinDetailsDto> itrKinDetailsDto = encounterMasterDto.getListKinDetailsDto().iterator();
			int index = 0;
			while (itrKinDetailsDto.hasNext()) 
			{
				KinDetailsDto kinDetailsDto = itrKinDetailsDto.next();
				if (kinDetailsDto.getKinDetailsId() == 0) 
				{
					kinDetailsDto.setPatientId(encounterMasterDto.getPatientId());
					if (kinDetailsDto.getKinName().equals(""))
						continue;
					Response res = iKinDetailsDao.saveKinDetails(kinDetailsDto);
					if (index == 0) 
					{
						if (encounterMasterDto.getKinDetailsId() == null) 
						{
							Integer kinDetailsId = (Integer) res.getObject();
							encounterMasterDto.setKinDetailsId(kinDetailsId);
						}
					}
				} else {
					kinDetailsDto.setPatientId(encounterMasterDto.getPatientId());
					iKinDetailsDao.updateKinDetails(kinDetailsDto);
					encounterMasterDto.setKinDetailsId(kinDetailsDto.getKinDetailsId());
				}
				index++;
			}
		}

			Session session = sessionFactory.getCurrentSession();
		 if (encounterMasterDto.getSourceId() != null && encounterMasterDto.getPatientClassId() !=null) {
			 session.createSQLQuery("update opd.t_encounter_master set "
					+ "source_id=:sourceId, "
					+ "patient_class_id=:patientClassId, "
							 + " remark=:remark  "
							 + "where encounter_id =:encounterId AND status ='A'")
		 			.setParameter("sourceId", encounterMasterDto.getSourceId())
					.setParameter("patientClassId", encounterMasterDto.getPatientClassId())
					 .setParameter("remark", encounterMasterDto.getRemark())
					 .setParameter("encounterId", encounterMasterDto.getEncounterId())
					 .executeUpdate();
		 }else{
			 session.createSQLQuery("update opd.t_encounter_master set "
							 + " remark=:remark  "
							 + "where encounter_id =:encounterId AND status ='A'")
					 .setParameter("remark", encounterMasterDto.getRemark())
					 .setParameter("encounterId", encounterMasterDto.getEncounterId())
					 .executeUpdate();
		 }
			Session session1 = sessionFactory.getCurrentSession();
			session1.createSQLQuery("update opd.m_appointment_master set " + "is_teleconsultation_apppointment=:isTeleconsultationAppointment "
					 + " where appointment_id =:appointmentId "
					 + "   AND status ='A'")
		 			.setParameter("isTeleconsultationAppointment", encounterMasterDto.getIsTeleconsultationAppointment())
		 			.setParameter("appointmentId", encounterMasterDto.getAppointmentId())
					.executeUpdate();

			if (encounterMasterDto.getReferralTypeId() != null) {
				// update status 'I' FOR PRE REF DETAIL
				if (encounterMasterDto.getReferalDetailsId() != null
						&& encounterMasterDto.getReferalDetailsId() != null) {
					Session session2 = sessionFactory.getCurrentSession();
					session2.createSQLQuery(" update public.t_referal_details set status ='I'"
							+ "  where referal_details_id=:referalDetailsId ")
							.setParameter("referalDetailsId", encounterMasterDto.getReferalDetailsId())
							.executeUpdate();
				}

				ReferalDetailsDto referalDetailsDto = new ReferalDetailsDto();
				referalDetailsDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				referalDetailsDto.setUnitId(encounterMasterDto.getUnitId());
				referalDetailsDto.setVisitTypeId(encounterMasterDto.getVisitTypeId());
				referalDetailsDto.setEncounterId(encounterMasterDto.getEncounterId());
				referalDetailsDto.setReferralId(encounterMasterDto.getReferralId());
				referalDetailsDto.setReferralTypeId(encounterMasterDto.getReferralTypeId());
				referalDetailsDto.setReferralRemark(encounterMasterDto.getReferralRemark());
				referalDetailsDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				referalDetailsDto.setCreatedDate(GlobalCommonDateUtils.getAvalDate(new Date(), "dd-MM-yyyy HH:mm:ss"));
				referalDetailsDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
				referalDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getAvalDate(new Date(), "dd-MM-yyyy HH:mm:ss"));
				referalDetailsDto.setStatus('A');
				iReferalDetailsService.saveReferalDetails(referalDetailsDto);
			}

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
		 throw  he;
		}

	}

	@Override
	public Response getWardAndFloorAndConsultingDoctorDetails(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			AdmissionDto adtDto = (AdmissionDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_FLOOR_AND_CONSULTING_DR_DETAILS_BY_LAB_SAMPLE_DETAILS_ID")
					.setParameter("orderDetailsId", dto.getOrderDetailsId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, adtDto);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getWardAndFloorAndConsultingDoctorDetails failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getPatientClassIdByEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			AdmissionDto adtDto = (AdmissionDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_CLASS_ID_BY_ENCOUNTER")
					.setParameter("encounterId", encounterMasterDto.getEncounterId() != null ? encounterMasterDto.getEncounterId() : 0 )
					.setParameter("admissionId", encounterMasterDto.getAdmissionId() != null ? encounterMasterDto.getAdmissionId() : 0)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, adtDto);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getPatientClassIdByEncounter failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getListOfEncountersByPatientIdForWalkinSale(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			
			String currentDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()),"yyyy-MM-dd");  
			
			String query = "select  " 
					+ "	distinct(encounter.encounter_id) as \"encounterId\",  "
					+ "	encounter.encounter_number as \"encounterNumber\",  "
					+ "	doc.doctor_id as \"doctorId\",  "
					+ "	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\",  "
					+ "	to_char(encounter.encounter_date, 'dd/mm/yyyy') as \"encounterDateString\",  "
					+ "	encounter.dept_id as \"specialityId\",  "
					+ "	encounter.sub_dept_id as \"subSpecialityId\",  "
					+ "	speciality.speciality_name as \"specialityName\",  "
					+ "	subSpeciality.sub_speciality_name as \"subSpecialityName\",  "
					+ "	encounter.modality_id as \"modalityId\",  "
					+ "	modality.modality_desc as \"modalityDesc\",  "
					+ "	appoint.ehc_package_id as \"ehcPackageId\",  "
					+ "	appoint.appointment_type_id as \"appointmentTypeId\",  "
					+ "	appoint.appointment_status_id as \"statusId\"  "
					+ "from  "
					+ "	opd.t_encounter_master encounter  "
					+ "left join opd.m_appointment_master appoint on encounter.appointment_id =appoint.appointment_id   "
					+ "left join doctor.m_doctor_master doc on doc.doctor_id = (case when encounter.doctor_id is not null then encounter.doctor_id else encounter.preferred_doc_id end) "
					+ "left join doctor.m_sub_speciality_master subSpeciality on subSpeciality.sub_speciality_id = encounter.sub_dept_id   "
					+ "left join public.m_modality_master modality on modality.modality_id = encounter.modality_id   "
					+ "left join doctor.m_speciality_master speciality on speciality.speciality_id = encounter.dept_id   "
					+ "where  " 
					+ "	encounter.organization_id =:orgId  " 
					+ "	and encounter.unit_id =:unitId  "
					+ "	and encounter.patient_id =:patientId  " 
					+ "	and (encounter.status_id in (5, 8, 6) "
					+ "		or (encounter.status_id = 1 and appoint.is_paid_appointment ='Y') "
					+ "		or (encounter.status_id = 9 and (encounter.created_date >= cast(:currentDate as date) - INTERVAL '30 days')))  "
					+ "	and encounter.against_enc_id is null  "
					+ " order by  "
					+ "	encounter.encounter_id desc  ";
			List<AppointmentStatusMasterDto> appointmentStatusMasterDtoList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("currentDate", currentDate)
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, appointmentStatusMasterDtoList, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getListOfEncountersByPatientIdForWalkinSale failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getFollowUpDateEncounterList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
try {
			
			String query = "select to_char(mst.follow_up_date, 'DD/MM/YYYY') as \"followUpDate\" , mst.encounter_id as \"encounterId\", mst.remark as \"remark\" from opd.t_encounter_master mst where "
					+ " mst.patient_id =:patientId "
					+ " and mst.unit_id =:unitId "
					+ " and mst.organization_id =:orgId";
			List<AppointmentStatusMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getFollowUpDateEncounterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateFollowUpDateEncounterRemark(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.t_encounter_master set remark=:remark "
			+ " where encounter_id=:encounterId ")
			.setParameter("remark", encounterMasterDto.getRemark())  // Bind the 'remark' parameter
			.setParameter("encounterId", encounterMasterDto.getEncounterId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateFollowUpDateEncounterRemark failed", he);
			} catch (Exception e) {
			logger.error("updateFollowUpDateEncounterRemark failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAdvisedBy(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			
			if(billingMasterDto.getAdvisedByName()!= null) {
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update opd.t_encounter_master set advised_by_name=:advisedByName "
				+ " where encounter_id=:encounterId ")
						.setParameter("advisedByName", billingMasterDto.getAdvisedByName())
						.setParameter("encounterId", billingMasterDto.getEncounterId())
				.executeUpdate();
			}
			
			if(billingMasterDto.getAdvisedById() != null) {
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update opd.t_encounter_master set advised_by_id=:advisedByName "
				+ " where encounter_id=:encounterId ")
						.setParameter("advisedByName", billingMasterDto.getAdvisedByName())
						.setParameter("encounterId", billingMasterDto.getEncounterId())
						.executeUpdate();
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateAdvisedBy failed", he);
			} catch (Exception e) {
			logger.error("updateAdvisedBy failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLastEncounteredListOfPatient(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			List<EncounterMasterDto> list2=new ArrayList<>();
			List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery("select " + 
						"	encounter.encounter_id as \"encounterId\", " + 
						"	encounter.dept_id as \"deptId\", " + 
						"	encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " + 
						"	to_char(encounter.encounter_date, 'HH24:MI:SS') as \"visitTime\", " + 
						"	to_char(encounter.encounter_date, 'yyyy-mm-dd') as \"encounterDateString\", " + 
						"	to_char(encounter.encounter_date, 'dd/mm/yyyy') as \"encounterDateString2\", " + 
						"	encounter.encounter_number as \"encounterNumber\", " + 
						"	encounter.doctor_id as \"doctorId\", " + 
						"	encounter.patient_class_id as \"patientClassId\", " + 
						"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
						"	encounter.dept_id as \"specialityId\", " + 
						"	speciality.speciality_name as \"specialityName\" " + 
						"from " + 
						"	opd.t_encounter_master encounter " + 
						"inner join doctor.m_doctor_master doc on " + 
						"	encounter.doctor_id = doc.doctor_id " + 
						"inner join doctor.m_speciality_master speciality on " + 
						"	encounter.dept_id = speciality.speciality_id " + 
						"where " + 
						"	encounter.organization_id =:organizationId  " + 
						"	and encounter.patient_id =:patientId  " + 
						"	and (encounter.status_id in (6 , 9)) " + 
						"order by " + 
						"	encounter.encounter_date desc limit 5 " 
						)
				.setParameter("organizationId", encounterMasterDto.getOrganizationId())
				.setParameter("patientId", encounterMasterDto.getPatientId())
				.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			
			for(EncounterMasterDto dto : list) 
			{
				
					List<ComplaintsDto> listComplaints=sessionFactory.getCurrentSession()
							.createSQLQuery("select " + 
									"	complaintAppointment.complaint_appo_id as \"complaintAppoId\", " +
									"	complaintAppointment.diagnosis_id as \"diagnosisId\", " + 
									"	complaintAppointment.encounter_id as \"encounterId\", " + 
									"	icpc1_.icpc_desc as \"diagnosisName\", " + 
									"	icpc1_.icpc_code as \"icpcCode\", " + 
									"	complaintAppointment.icpc_code_text as \"icpcCodeText\", " + 
									"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + 
									"from " + 
									"	patient.t_complaint_appo_mapper complaintAppointment " + 
									"inner join emr.m_icpc icpc1_ on " + 
									"	complaintAppointment.diagnosis_id = icpc1_.icpc_id " + 
									"left join emr.m_icpc_system icpcsystem2_ on " + 
									"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " + 
									"where " + 
									"	complaintAppointment.patient_id =:patientId " + 
									"	and complaintAppointment.status = 'A' " + 
									"	and complaintAppointment.encounter_id =:encounterId " + 
									"	and complaintAppointment.is_resolved = 'N' " +
//									 " AND to_char(complaintAppointment.cerated_date, 'YYYY-MM-DD')=:createdDate"+
									"	and complaintAppointment.organization_id =:organizationId " + 
									"	and(complaintAppointment.is_complaint_stop = 'N')")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
//						.setParameter("createdDate", dto.getEncounterDateString())
						.setResultTransformer(Transformers.aliasToBean(ComplaintsDto.class)).list();
			
					
					List<DiagnosisPatientAppointmentMapperDto> listDiagnosis=sessionFactory.getCurrentSession()
							.createQuery( "SELECT "
								 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
								 	+ " diagMapper.admissionId AS admissionId, "
								 	+ " diagMapper.isDeleted AS isDeleted, "
								 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
									+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
								 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
									+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
								 	+ " diagMapper.patientId AS patientId, "
								 	+ " diagMapper.diagnosisText AS diagnosisText, "
							 		+ " diagMapper.diagnosisId AS diagnosisId, "
							 		+ " diagMapper.type AS type, "
							 		+ " diagMapper.diagnosisType AS diagnosisType, "
							 		+ " (case " 
									+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
									+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
									+ " else 'Admin' end) as userName,"
							 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
							 		+ " diagMapper.created_date as created_date_new, "
							 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
							 		+ " diagMapper.remark AS remark, "
							 		+ " diagMaster.isNotifiable AS isNotifiable , "
							 		+ " diagMaster.isComorbidity AS isComorbidity,"
							 		+ " diagMaster.code AS code,"
							 		+ " diagMaster.diagnosisName AS diagnosisName "
							+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
							+ " left JOIN diagMapper.diagnosisMaster diagMaster "
							+ " LEFT JOIN diagMapper.userMaster um " 
							+ " LEFT JOIN um.employeeMastersList empmst " 
							+ " LEFT JOIN um.doctorMastersList  docmst "
							+ " WHERE "
							+ " diagMapper.patientId=:patientId "
							+ " and diagMapper.encounterId=:encounterId "
							+ " and diagMapper.status='A'"
							+ " AND diagMapper.organizationId=:organizationId"
//							+ " AND to_char(diagMapper.created_date, 'YYYY-MM-DD')=:createdDate"
							+ " AND diagMapper.isDiagnosisCancel='N'"
							+ " AND diagMapper.isDeleted='N'"
							+ " AND diagMapper.isResolved='N'"
							+ " order by diagMapper.patientDiagnosisId desc")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
//						.setParameter("createdDate", dto.getEncounterDateString())
						.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					
					List<InvestigationOrderMasterNewDto> listlabOrder=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" 1 as \"visitTypeId\"," + 
									//" '"+N+"' as \"nursingServiceOrderFlag\"," + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
//									 " AND to_char(ord_mst.created_date, 'YYYY-MM-DD')=:createdDate"
									 " and ord_mst.patient_id =:patientId and ord_mst.encounter_id =:encounterId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
//						.setParameter("createdDate", dto.getEncounterDateString())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
					
					List<CoversheetInstructionDto> listAdvice=sessionFactory.getCurrentSession()
							.createQuery("SELECT cover.instructionId as instructionId, "
									+ " cover.instructionDetailsId as instructionDetailsId, "
									+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
									+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
									+ " (case "
									+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
									+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
									+ " else 'Admin' end) as userName,"
									+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
									+ " FROM CoversheetInstruction cover "
									+ " LEFT JOIN cover.instructionMaster instr "
									+ " LEFT JOIN cover.userMaster userMaster "
									+ " LEFT JOIN userMaster.doctorMastersList doc "
									+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
									" where " + 
									"	cover.patientId =:patientId " + 
									"	and cover.status ='A'" +
									"	and cover.encounterId =:encounterId " + 
									"	and cover.organizationId =:organizationId " 
//									 " AND to_char(cover.createdDate, 'YYYY-MM-DD')=:createdDate"

									)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
//						.setParameter("createdDate", dto.getEncounterDateString())
						.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					
					List<MedicationDTO> listMedication=sessionFactory.getCurrentSession()
							.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", " + 
									" pres.id as \"prescriptionId\", " + 
									" (case when presDtl.item_id is not null then itm.name " + 
									"	else presDtl.item_name end) as \"itemName\", " + 
									" preFreDtl.dose as \"doseDesc\","+
									" su.unit as \"strengthDesc\", " + 
									" preFreDtl.frequency_id as \"frequencyId\", " + 
									" freq.description as \"frequencyDesc\", " + 
									" preFreDtl.route_id as \"roaId\", " + 
									" presDtl.order_qty as \"orderQty\", " +
									" presDtl.duration as \"duration\", " + 
									" presDtl.duration as \"durationCount\", " + 
									" presDtl.instruction_desc as \"instructionText\", " + 
									" presDtl.duration_unit as \"durationUnit\", " + 
									" preFreDtl.start_date as \"startDateNew\", " + 
									" preFreDtl.stop_date as \"endDateNew\", " + 
									" 1 as \"visitTypeId\", " + 
									" roa.description as \"roaDesc\", " + 
									" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
									" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," + 
									" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
									" presDtl.instruction_id as \"instructionId\" ," + 
									" instr.instruction_desc as \"instructionDesc\", " + 
									" presDtl.is_medication_stop as \"isMedicationStop\","+
									" (case when presDtl.item_id is not null then gen.name \r\n" + 
									"	else presDtl.generic_name end) as \"genericName\" " + 
									" FROM pharmacy.t_prescription pres " + 
									" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
									" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
									" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
									" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
									" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
									" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
									" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
									" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
									" where " + 
									"	pres.patient_id =:patientId " + 
									"	and pres.is_active =true " + 
									"	and presDtl.is_active =true " + 
									"	and pres.encounter_id =:encounterId "
									+ " and pres.organization_id =:organizationId"
									+ " AND presDtl.is_medication_stop='N' " 
									+ " and presDtl.status_id != 9 "
//									 " AND to_char(pres.added_date, 'YYYY-MM-DD')=:createdDate"
									)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
//						.setParameter("createdDate", dto.getEncounterDateString())
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
					
					dto.setComplaintlistData(listComplaints);
					dto.setDiagnosisListData(listDiagnosis);
					dto.setMedicationDataList(listMedication);
					dto.setLabOrderList(listlabOrder);
					dto.setAdviceDataList(listAdvice);
				
					
					
					
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
		throw he ;
	} catch (Exception e) {
		throw e ;
	}
//	return new Response(ERROR, null, null, null, null);
}

	@Override
	public Response updateLatestCount(SlotMasterDto dto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.t_encounter_master set latest_call = false where organization_id =:organizationId "
							+" and unit_id  =:unitId "
							+" and dept_id in(:specialityIdList) ")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameterList("specialityIdList", dto.getSpecialityIdList())
			.executeUpdate();
			
			Session session1=sessionFactory.getCurrentSession();
			session1.createSQLQuery(" update opd.t_encounter_master set latest_call = true where encounter_id =:encounterId")
					.setParameter("encounterId", dto.getEncounterId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateLatestCount failed", he);
			} catch (Exception e) {
			logger.error("updateLatestCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorLatestCount(SlotMasterDto dto) throws ApplicationException {
try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.t_encounter_master set doctor_latest_call = false where organization_id =:organizationId  and unit_id  =:unitId ")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.executeUpdate();
			
			Session session1=sessionFactory.getCurrentSession();
			session1.createSQLQuery(" update opd.t_encounter_master set doctor_latest_call = true where encounter_id =:encounterId")
					.setParameter("encounterId", dto.getEncounterId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateDoctorLatestCount failed", he);
			} catch (Exception e) {
			logger.error("updateDoctorLatestCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLastEncounterDataOfPatientForWebAPP(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			List<EncounterMasterDto> list2=new ArrayList<>();
			List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery("select " + 
						"	encounter.encounter_id as \"encounterId\", " + 
						"	encounter.dept_id as \"deptId\", " + 
						"	encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " + 
						"	to_char(encounter.encounter_date, 'HH24:MI:SS') as \"visitTime\", " + 
						"	to_char(encounter.encounter_date, 'yyyy-mm-dd') as \"encounterDateString\", " + 
						"	to_char(encounter.encounter_date, 'dd/mm/yyyy') as \"encounterDateString2\", " + 
						"	encounter.encounter_number as \"encounterNumber\", " + 
						"	encounter.doctor_id as \"doctorId\", " + 
						"	encounter.patient_class_id as \"patientClassId\", " + 
						"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
						"	encounter.dept_id as \"specialityId\", " + 
						"	speciality.speciality_name as \"specialityName\" " + 
						"from " + 
						"	opd.t_encounter_master encounter " + 
						"inner join doctor.m_doctor_master doc on " + 
						"	encounter.doctor_id = doc.doctor_id " + 
						"inner join doctor.m_speciality_master speciality on " + 
						"	encounter.dept_id = speciality.speciality_id " + 
						"where " + 
						"	encounter.organization_id =:organizationId  " + 
						"	and encounter.patient_id =:patientId  " + 
						"	and encounter.unit_id =:unitId  " + 
						"	and (encounter.status_id in (6 , 9, 5)) " +
						"   and encounter.encounter_id !=:encounterId " +
						" order by " + 
						"	encounter.encounter_date desc limit 1 " 
						)
				.setParameter("organizationId", encounterMasterDto.getOrganizationId())
				.setParameter("patientId", encounterMasterDto.getPatientId())
				.setParameter("unitId", encounterMasterDto.getUnitId())
				.setParameter("encounterId", encounterMasterDto.getEncounterId())
				.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			
			for(EncounterMasterDto dto : list) 
			{
				
					List<ComplaintsDto> listComplaints=sessionFactory.getCurrentSession()
							.createSQLQuery("select " + 
									"	complaintAppointment.complaint_appo_id as \"complaintAppoId\", " + 
									"	complaintAppointment.encounter_id as \"encounterId\", " + 
									"	complaintAppointment.diagnosis_id as \"diagnosisId\", " + 
									"	icpc1_.icpc_desc as \"diagnosisName\", " + 
									"	icpc1_.icpc_code as \"icpcCode\", " + 
									"	complaintAppointment.icpc_code_text as \"icpcCodeText\", " + 
									"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + 
									"from " + 
									"	patient.t_complaint_appo_mapper complaintAppointment " + 
									"inner join emr.m_icpc icpc1_ on " + 
									"	complaintAppointment.diagnosis_id = icpc1_.icpc_id " + 
									"left join emr.m_icpc_system icpcsystem2_ on " + 
									"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " + 
									"where " + 
									"	complaintAppointment.patient_id =:patientId " + 
									"	and complaintAppointment.status = 'A' " + 
									"	and complaintAppointment.encounter_id =:encounterId " + 
									"	and complaintAppointment.is_resolved = 'N' " +
									"	and complaintAppointment.organization_id =:organizationId " + 
									"	and complaintAppointment.unit_id =:unitId " + 
									"	and(complaintAppointment.is_complaint_stop = 'N')")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintsDto.class)).list();
			
					
					List<DiagnosisPatientAppointmentMapperDto> listDiagnosis=sessionFactory.getCurrentSession()
							.createQuery( "SELECT "
								 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
								 	+ " diagMapper.admissionId AS admissionId, "
								 	+ " diagMapper.isDeleted AS isDeleted, "
								 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
									+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
								 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
									+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
								 	+ " diagMapper.patientId AS patientId, "
								 	+ " diagMapper.diagnosisText AS diagnosisText, "
							 		+ " diagMapper.diagnosisId AS diagnosisId, "
							 		+ " diagMapper.type AS type, "
							 		+ " diagMapper.diagnosisType AS diagnosisType, "
							 		+ " (case " 
									+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
									+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
									+ " else 'Admin' end) as userName,"
							 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
							 		+ " diagMapper.created_date as created_date_new, "
							 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
							 		+ " diagMapper.remark AS remark, "
							 		+ " diagMaster.isNotifiable AS isNotifiable , "
							 		+ " diagMaster.isComorbidity AS isComorbidity,"
							 		+ " diagMaster.code AS code,"
							 		+ " diagMaster.diagnosisName AS diagnosisName "
							+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
							+ " left JOIN diagMapper.diagnosisMaster diagMaster "
							+ " LEFT JOIN diagMapper.userMaster um " 
							+ " LEFT JOIN um.employeeMastersList empmst " 
							+ " LEFT JOIN um.doctorMastersList  docmst "
							+ " WHERE "
							+ " diagMapper.patientId=:patientId "
							+ " and diagMapper.encounterId=:encounterId "
							+ " and diagMapper.status='A'"
							+ " AND diagMapper.organizationId=:organizationId"
							+ " AND diagMapper.isDiagnosisCancel='N'"
							+ " AND diagMapper.isDeleted='N'"
							+ " AND diagMapper.isResolved='N'"
							+ " AND diagMapper.unitId=:unitId "
							+ " order by diagMapper.patientDiagnosisId desc")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					
					List<InvestigationOrderMasterNewDto> listlabOrder=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
									 " and ord_mst.patient_id =:patientId and ord_mst.encounter_id =:encounterId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
					
					
					List<InvestigationOrderMasterNewDto> listlabOrderFreeText=sessionFactory.getCurrentSession()
							.createSQLQuery("select " + 
									"	tftos.patient_id as \"patientId\", " + 
									"	tftos.admission_id as \"admissionId\", " + 
				                    " tftos.free_text_ordered_services_id as \"freeTextOrderedServicesId\", " + 
									"	tftos.encounter_id as \"encounterId\", " + 
									"	tftos.order_details_id as \"orderDetailsId\", " + 
									"	CURRENT_TIMESTAMP as \"orderSchDateNew\", " + 
									"	tftos.service_text as \"serviceText\", " + 
									"	CURRENT_TIMESTAMP as \"createdDateNew\" " + 
									" from " + 
									"	public.t_free_text_ordered_services tftos " + 
									" left join public.t_order_details ord_dtls on " + 
									"	tftos.order_details_id = ord_dtls.order_details_id " + 
									"	where " + 
									"		tftos.organization_id =:organizationId " + 
									"		and tftos.status ='A' " + 
									"	    and tftos.encounter_id =:encounterId " + 
									"		and tftos.unit_id =:unitId "
									
									)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("unitId", encounterMasterDto.getUnitId())
							.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
					listlabOrder.addAll(listlabOrderFreeText);
					
					List<CoversheetInstructionDto> listAdvice=sessionFactory.getCurrentSession()
							.createQuery("SELECT cover.instructionId as instructionId, "
									+ " cover.instructionDetailsId as instructionDetailsId, "
									+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
									+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
									+ " (case "
									+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
									+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
									+ " else 'Admin' end) as userName,"
									+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
									+ " FROM CoversheetInstruction cover "
									+ " LEFT JOIN cover.instructionMaster instr "
									+ " LEFT JOIN cover.userMaster userMaster "
									+ " LEFT JOIN userMaster.doctorMastersList doc "
									+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
									" where " + 
									"	cover.patientId =:patientId " + 
									"	and cover.status ='A'" +
									"	and cover.encounterId =:encounterId " + 
									"	and cover.organizationId =:organizationId " +
									"	and cover.unitId =:unitId " 
//									 " AND to_char(cover.createdDate, 'YYYY-MM-DD')=:createdDate"

									)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					
					List<MedicationDTO> listMedication=sessionFactory.getCurrentSession()
							.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", "+
									" pres.id as \"prescriptionId\", " + 
									" presDtl.id as \"prescriptionDetailsid\", " + 
									" (case when presDtl.item_id is not null then itm.name " + 
									"	else presDtl.item_name end) as \"itemName\", " + 
									" preFreDtl.dose as \"doseDesc\", " + 
									" su.unit as \"strengthDesc\", " + 
									" preFreDtl.frequency_id as \"frequencyId\", " + 
									" freq.description as \"frequencyDesc\", " + 
									" preFreDtl.route_id as \"roaId\", " + 
									" presDtl.order_qty as \"orderQty\", " +
									" presDtl.duration as \"duration\", " + 
									" presDtl.instruction_desc as \"instructionText\", " + 
									" presDtl.duration as \"durationCount\", " + 
									" presDtl.duration_unit as \"durationUnit\", "+
									" 1 as \"visitTypeId\", " + 
									" roa.description as \"roaDesc\", " + 
									" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," + 
									" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
									"	preFreDtl.start_date as \"startDateNew\", " + 
									"	preFreDtl.stop_date as \"endDateNew\", " + 
									" presDtl.instruction_id as \"instructionId\" ," + 
									" instr.instruction_desc as \"instructionDesc\", " + 
									" presDtl.is_medication_stop as \"isMedicationStop\","+
									" (case when presDtl.item_id is not null then gen.name \r\n" + 
									"	else presDtl.generic_name end) as \"genericName\" " + 
									" FROM pharmacy.t_prescription pres " + 
									" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
									" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
									" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
									" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
									" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
									" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
									" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
									" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
									" where " + 
									"	pres.patient_id =:patientId " + 
									"	and pres.is_active =true " + 
									"	and presDtl.is_active =true " + 
									"	and pres.encounter_id =:encounterId "
									+ " and pres.organization_id =:organizationId"
									+ " and pres.unit_id =:unitId"
									+ " AND presDtl.is_medication_stop='N' " 
									+ " and presDtl.status_id != 9 "
//									 " AND to_char(pres.added_date, 'YYYY-MM-DD')=:createdDate"
									)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setParameter("unitId", encounterMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
					
					String query="	select " + 
							"	mppr.quick_notes_mapper_id as \"quickNotesMapperId\", " + 
							"	mppr.quick_notes_desc as \"quickNotesDesc\", " + 
							"	mppr.admission_id as \"admissionId\", " + 
							"	mppr.patient_id as \"patientId\", " + 
							"	mppr.encounter_id as \"encounterId\", " + 
							"	mppr.doctor_id as \"doctorId\", " + 
							"	mppr.status as \"status\" " + 
							"from " + 
							"	opd.t_quick_notes_mapper mppr " + 
							"where " + 
							"	mppr.patient_id =:patientId " + 
							"	and mppr.encounter_id =:encounterId " + 
							"	and mppr.organization_id =:organizationId " + 
							"	and mppr.unit_id =:unitId "
							+ " order by mppr.quick_notes_mapper_id desc";
					List<QuickNotesMapperDto> quickNotesMapperlist=sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
							.setParameter("patientId",  encounterMasterDto.getPatientId())
							.setParameter("encounterId",  dto.getEncounterId())
							.setParameter("unitId",  encounterMasterDto.getUnitId())					
							.setResultTransformer(Transformers.aliasToBean(QuickNotesMapperDto.class)).list();
					
					String query1="select reg.patient_remark  as \"patientRemark\" " + 
							"from patient.t_patient_registration reg     " + 
							"where reg.patient_id=:patientId " + 
							"and reg.organization_id =:organizationId " + 
							"and reg.unit_id =:unitId ";
					List<EncounterMasterDto> patientRemarklist=sessionFactory.getCurrentSession().createSQLQuery(query1)
							.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
							.setParameter("patientId",  encounterMasterDto.getPatientId())
							.setParameter("unitId",  encounterMasterDto.getUnitId())					
							.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
					
					
					String query2="select     " + 
							"	distinct appointmen0_.vital_id as \"vitalId\", " + 
							"	appointmen0_.appointment_vital_mapper_id as \"appointmentVitalMapperId\",     " + 
							"	listorgwis2_.vital_order_opd as \"vitalOrderOpd\",     " + 
							"	appointmen0_.admission_id as \"admissionId\",     " + 
							"	appointmen0_.patient_id as \"patientId\",     " + 
							"	appointmen0_.value as \"value\",     " + 
							"	appointmen0_.status as \"status\",     " + 
							"	appointmen0_.is_self_triage as \"isSelfTriage\",     " + 
							"	appointmen0_.created_by as \"createdBy\",     " + 
							"	appointmen0_.updated_by as \"updatedBy\",     " + 
							"	to_char(     " + 
							"		appointmen0_.created_date,     " + 
							"		'YYYY/MM/DD'     " + 
							"	) as \"creatDate\",     " + 
							"	to_char(     " + 
							"		appointmen0_.created_date,     " + 
							"		'DD/MM/YYYY'     " + 
							"	) as \"createdDate\",     " + 
							"	appointmen0_.created_date as \"createdDateNew\",     " + 
							"	to_char(     " + 
							"		appointmen0_.post_facto_date,     " + 
							"		'DD-MM-YYYY'     " + 
							"	) as \"postFactoDate\",     " + 
							"	to_char(     " + 
							"		appointmen0_.post_facto_time,     " + 
							"		'HH24:MI'     " + 
							"	) as \"postFactoTime\",     " + 
							"	appointmen0_.is_post_facto as \"isPostFacto\",     " + 
							"	appointmen0_.encounter_id as \"encounterId\",     " + 
							"	to_char(     " + 
							"		appointmen0_.created_date,     " + 
							"		'HH24:MI'     " + 
							"	) as \"createdTime\",     " + 
							"	appointmen0_.created_date as \"createdTimeNew\",     " + 
							"	to_char(     " + 
							"		appointmen0_.updated_date,     " + 
							"		'DD-MM-YYYY'     " + 
							"	) as \"updatedDate\",     " + 
							"	appointmen0_.updated_date as \"updatedDateNew\",     " + 
							"	appointmen0_.unit_id as \"unitId\",     " + 
							"	appointmen0_.organization_id as \"organizationId\",     " + 
							"	vitalmaste1_.vital_name as \"vitalName\",     " + 
							"	vitalmaste1_.unit as \"unitDesc\",     " + 
							"	vitalmaste1_.is_icu as \"isIcu\",     " + 
							"	appointmen0_.role_id as \"roleId\",     " + 
							"	appointmen0_.is_reviewed_flag as \"isReviewedFlag\",     " + 
							"	(     " + 
							"		coalesce(employeema4_.first_name)|| ' ' || coalesce(employeema4_.middle_name)|| ' ' || coalesce(employeema4_.last_name)     " + 
							"	) as \"employeeName\",     " + 
							"	(     " + 
							"		'Dr. ' || coalesce(doctormast5_.first_name)|| ' ' || coalesce(doctormast5_.middle_name)|| ' ' || coalesce(doctormast5_.last_name)     " + 
							"	) as \"doctorName\",     " + 
							"	appointmen0_.dialysis_event_id as \"dialysisEventId\",      "+
							"   appointmen0_.patient_dialysis_rec_id  as \"patientDialysisRecId\"  " + 
							"from     " + 
							"	patient.t_appointment_vital_mapper appointmen0_     " + 
							"inner join emr.m_vital_master vitalmaste1_ on     " + 
							"	appointmen0_.vital_id = vitalmaste1_.vital_id     " + 
							"inner join emr.t_org_wise_vital_mapper listorgwis2_ on     " + 
							"	vitalmaste1_.vital_id = listorgwis2_.vital_id     " + 
							"	and(     " + 
							"		listorgwis2_.organization_id =107     " + 
							"	)     " + 
							"left outer join public.m_user_master usermaster3_ on     " + 
							"	appointmen0_.created_by = usermaster3_.user_id     " + 
							"left outer join public.m_employee_master employeema4_ on     " + 
							"	usermaster3_.user_id = employeema4_.user_id     " + 
							"left outer join doctor.m_doctor_master doctormast5_ on     " + 
							"	usermaster3_.user_id = doctormast5_.user_id     " + 
							"where     " + 
							"	appointmen0_.organization_id =:organizationId     " + 
							"	and appointmen0_.unit_id =:unitId     " + 
							"	and appointmen0_.patient_id =:patientId     " + 
							"	and appointmen0_.status = 'A'     " + 
							"	and appointmen0_.value != ''     " + 
							"	and appointmen0_.is_entered_in_error = 'N'     " + 
							"	and appointmen0_.encounter_id =:encounterId     " ;
					List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto=sessionFactory.getCurrentSession().createSQLQuery(query2)
							.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
							.setParameter("patientId",  encounterMasterDto.getPatientId())
							.setParameter("encounterId",  dto.getEncounterId())
							.setParameter("unitId",  encounterMasterDto.getUnitId())					
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
					
				
					
					dto.setComplaintlistData(listComplaints);
					dto.setDiagnosisListData(listDiagnosis);
					dto.setMedicationDataList(listMedication);
					dto.setLabOrderList(listlabOrder);
					dto.setAdviceDataList(listAdvice);
					dto.setQuickNotesMapperlist(quickNotesMapperlist);
					dto.setPatientRemarklist(patientRemarklist);
					dto.setListAppointmentVitalMapperDto(listAppointmentVitalMapperDto);
				
					
					
					
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getLastEncounterDataOfPatientForWebAPP failed", he);
	} catch (Exception e) {
	logger.error("getLastEncounterDataOfPatientForWebAPP failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}

	@Override
	public Response getLastAdmisionDataOfPatientForWebAPP(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			List<EncounterMasterDto> list2=new ArrayList<>();
			List<EncounterMasterDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery( "select"
					       + "	adm.admission_id as \"admissionId\"," + 
					       "	adm.visit_type_id as \"visitTypeId\"," + 
					       "	vtm.visit_type_code as \"visitTypeName\"," + 
					       "	to_char(" + 
					       "		adtl.doa," + 
					       "		'DD/MM/YYYY'" + 
					       "	) as \"visitDate\"," + 
					       "		adtl.doa as \"visitDateNew\"," + 
					       "	adm.speciality_id as \"specialityId\"," + 
					       "	dept.speciality_name as \"specialityName\"," + 
					       "	(" + 
					       "		'Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)" + 
					       "	) as \"doctorName\" " + 
					       " from adt.t_admission adm " + 
					       " left join public.m_visit_type_master vtm on" + 
					       "	vtm.visit_type_id = adm.visit_type_id" + 
					       " inner join adt.t_admission_details adtl on  " + 
					       "	adtl.admission_id = adm.admission_id  " + 
					       " left join doctor.m_doctor_master doc on  " + 
					       "	doc.doctor_id=adm.doctor_id " + 
					       " left join doctor.m_speciality_master dept on  " + 
					       "	dept.speciality_id=adm.speciality_id	 " + 
					       " left join adt.m_discharge_summary	dissumm on  " + 
					       "    dissumm.admission_id=adm.admission_id and dissumm.status='A' " + 
					       " left join adt.t_discharge	discharge on  " + 
					       "    discharge.admission_id=adm.admission_id " + 
					       " where  adm.patient_id=:patientId "+
					       "  and adm.admission_id !=:admissionId " + 
					       "   and adtl.nursing_acceptance = 'Y'" + 
					       "   and adm.status = 'I'  " + 
					       "   and adm.patient_id = adm.patient_id " + 
					       "	and adtl.admission_details_id = ( " + 
					       "	select admission_details_id " + 
					       "	from adt.t_admission_details " + 
					       "	where admission_id = adm.admission_id " + 
					       "	order by admission_details_id desc limit 1) " 
						)
				.setParameter("patientId", encounterMasterDto.getPatientId())
				.setParameter("admissionId", encounterMasterDto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			
			for(EncounterMasterDto dto : list) 
			{
				
					List<ComplaintAppointmentDto> listComplaints=sessionFactory.getCurrentSession()
							.createSQLQuery("select   " + 
									"	complaintAdmission.complaint_admission_id as \"complaintAdmissionId\",   " + 
									"	complaintAdmission.admission_id as \"admissionId\",   " + 
									"	complaintAdmission.diagnosis_id as \"diagnosisId\",   " + 
									"	complaintAdmission.resolved_remark as \"resolvedRemark\",   " + 
									"	(   " + 
									"		select   " + 
									"			max( templateTransaction.m_icpc_id )   " + 
									"		from   " + 
									"			public.t_template_transaction templateTransaction   " + 
									"		where   " + 
									"			templateTransaction.m_icpc_id = complaintAdmission.diagnosis_id   " + 
									"			and complaintAdmission.admission_id = templateTransaction.admission_id   " + 
									"		group by   " + 
									"			templateTransaction.m_icpc_id   " + 
									"	) as \"mIcpcId1\",   " + 
									"	(   " + 
									"		select   " + 
									"			count( templateTransaction1.m_icpc_id )   " + 
									"		from   " + 
									"			public.t_template_transaction templateTransaction1   " + 
									"		where   " + 
									"			templateTransaction1.m_icpc_id = complaintAdmission.diagnosis_id   " + 
									"         and templateTransaction1.status = 'A'   " + 
									"			and complaintAdmission.admission_id = templateTransaction1.admission_id   " + 
									"		group by   " + 
									"			templateTransaction1.m_icpc_id   " + 
									"	) as \"complaintTempCountBook\",   " + 
									"	icpc1_.icpc_desc as \"diagnosisName\",   " + 
									"	icpc1_.icpc_code as \"icpcCode\",   " + 
									"	complaintAdmission.since as \"since\",   " + 
									"	complaintAdmission.duration as \"duration\",   " + 
									"	complaintAdmission.remark as \"remark\",   " + 
									"		to_char(   " + 
									"		complaintAdmission.created_date,   " + 
									"		'dd/mm/yyyy'   " + 
									"	) as \"createdDate\",   " + 
									"		to_char(   " + 
									"		complaintAdmission.resolved_date,   " + 
									"		'dd/mm/yyyy'   " + 
									"	) as \"resolvedDate\",   " + 
									"	complaintAdmission.status as \"status\",   " + 
									"	complaintAdmission.role_id as \"roleId\",   " + 
									"	complaintAdmission.icpc_code_text as \"icpcCodeText\",   " + 
									"	icpc1_.icpc_system_id as \"icpcSystemId\",   " + 
									"		complaintAdmission.is_reviewed_flag as \"isReviewedFlag\",   " + 
									"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\",   " + 
									"		(   " + 
									"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)   " + 
									"	) as \"employeeName\",   " + 
									"	(   " + 
									"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)   " + 
									"	) as \"doctorName\"   " + 
									" from   " + 
									"	adt.t_complaint_admission complaintAdmission   " + 
									" inner join emr.m_icpc icpc1_ on   " + 
									"	complaintAdmission.diagnosis_id = icpc1_.icpc_id   " + 
									" left  join emr.m_icpc_system icpcsystem2_ on   " + 
									"	complaintAdmission.icpc_system_id = icpcsystem2_.icpc_system_id   " + 
									" left  join public.m_user_master um on   " + 
									"	complaintAdmission.created_by = um.user_id   " + 
									" left  join public.m_employee_master employee on   " + 
									"	um.user_id = employee.user_id   " + 
									" left  join doctor.m_doctor_master doctor on   " + 
									"	um.user_id = doctor.user_id   " + 
									" where   " + 
									"	complaintAdmission.patient_id =:patientId   " + 
									"	and    " + 
									"	complaintAdmission.status = 'A'   " + 
									"	and complaintAdmission.is_complaint_stop = 'N'   " + 
									" and complaintAdmission.is_resolved = 'N'   " + 
									"	and complaintAdmission.organization_id =:organizationId  "+
									" and 	complaintAdmission.admission_id  =:admissionId  " + 
									"   " + 
									" union   " + 
									" select   " + 
									"	complaintAppointment.complaint_appo_id as \"complaintAppoId\",   " + 
									"	complaintAppointment.encounter_id as \"encounterId\",   " + 
									"	complaintAppointment.diagnosis_id as \"diagnosisId\",   " + 
									"	complaintAppointment.resolved_remark as \"resolvedRemark\",   " + 
									"	(   " + 
									"		select   " + 
									"			max( templateTransaction.m_icpc_id )   " + 
									"		from   " + 
									"			public.t_template_transaction templateTransaction   " + 
									"		where   " + 
									"			templateTransaction.m_icpc_id = complaintAppointment.diagnosis_id   " + 
									"			and complaintAppointment.encounter_id = templateTransaction.encounter_id   " + 
									"		group by   " + 
									"			templateTransaction.m_icpc_id   " + 
									"	) as \"mIcpcId1\",   " + 
									"	(   " + 
									"		select   " + 
									"			count( templateTransaction1.m_icpc_id )   " + 
									"		from   " + 
									"			public.t_template_transaction templateTransaction1   " + 
									"		where   " + 
									"			templateTransaction1.m_icpc_id = complaintAppointment.diagnosis_id   " + 
									"			and complaintAppointment.encounter_id = templateTransaction1.encounter_id    " + 
									"         and templateTransaction1.status = 'A'   " + 
									"		group by   " + 
									"			templateTransaction1.m_icpc_id   " + 
									"	) as \"complaintTempCountBook\",   " + 
									"	icpc1_.icpc_desc as \"diagnosisName\",   " + 
									"	icpc1_.icpc_code as \"icpcCode\",   " + 
									"	complaintAppointment.since as \"since\",   " + 
									"	complaintAppointment.duration as \"duration\",   " + 
									"	complaintAppointment.remark as \"remark\",   " + 
									"	to_char(   " + 
									"		complaintAppointment.cerated_date,   " + 
									"		'dd/mm/yyyy'   " + 
									"	) as \"createdDate\",   " + 
									"	to_char(   " + 
									"		complaintAppointment.resolved_date,   " + 
									"		'dd/mm/yyyy'   " + 
									"	) as \"resolvedDate\",   " + 
									"	complaintAppointment.status as \"status\",   " + 
									"	complaintAppointment.role_id as \"roleId\",   " + 
									"	complaintAppointment.icpc_code_text as \"icpcCodeText\",   " + 
									"	icpc1_.icpc_system_id as \"icpcSystemId\",   " + 
									"	complaintAppointment.is_reviewed_flag as \"isReviewedFlag\",   " + 
									"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\",   " + 
									"	(   " + 
									"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)   " + 
									"	) as \"employeeName\",   " + 
									"	(   " + 
									"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)   " + 
									"	) as \"doctorName\"   " + 
									" from   " + 
									"	patient.t_complaint_appo_mapper complaintAppointment   " + 
									" inner join emr.m_icpc icpc1_ on   " + 
									"	complaintAppointment.diagnosis_id = icpc1_.icpc_id   " + 
									" left  join emr.m_icpc_system icpcsystem2_ on   " + 
									"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id   " + 
									"	 left  join public.m_user_master um on   " + 
									"	complaintAppointment.created_by = um.user_id   " + 
									" left  join public.m_employee_master employee on   " + 
									"	um.user_id = employee.user_id   " + 
									" left  join doctor.m_doctor_master doctor on   " + 
									"	um.user_id = doctor.user_id   " + 
									" where   " + 
									"	complaintAppointment.patient_id =:patientId   " + 
									"	and    " + 
									"	complaintAppointment.status = 'A'   " + 
									"	and complaintAppointment.organization_id =:organizationId   " + 
									"	and(complaintAppointment.is_complaint_stop = 'N'   " + 
									" and complaintAppointment.is_resolved = 'N'   "+
									"	 and 	complaintAppointment.encounter_id =:admissionId  " + 
									"	)")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
			
					
					List<DiagnosisPatientAppointmentMapperDto> listDiagnosis=sessionFactory.getCurrentSession()
							.createQuery( "SELECT "
								 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
								 	+ " diagMapper.admissionId AS admissionId, "
								 	+ " diagMapper.isDeleted AS isDeleted, "
								 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
									+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
								 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
									+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
								 	+ " diagMapper.patientId AS patientId, "
								 	+ " diagMapper.diagnosisText AS diagnosisText, "
							 		+ " diagMapper.diagnosisId AS diagnosisId, "
							 		+ " diagMapper.type AS type, "
							 		+ " diagMapper.diagnosisType AS diagnosisType, "
							 		+ " (case " 
									+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
									+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
									+ " else 'Admin' end) as userName,"
							 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
							 		+ " diagMapper.created_date as created_date_new, "
							 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
							 		+ " diagMapper.remark AS remark, "
							 		+ " diagMaster.isNotifiable AS isNotifiable , "
							 		+ " diagMaster.isComorbidity AS isComorbidity,"
							 		+ " diagMaster.code AS code,"
							 		+ " diagMaster.diagnosisName AS diagnosisName "
							+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
							+ " left JOIN diagMapper.diagnosisMaster diagMaster "
							+ " LEFT JOIN diagMapper.userMaster um " 
							+ " LEFT JOIN um.employeeMastersList empmst " 
							+ " LEFT JOIN um.doctorMastersList  docmst "
							+ " WHERE "
							+ " diagMapper.patientId=:patientId "
							+ " and diagMapper.admissionId=:admissionId "
							+ " and diagMapper.status='A'"
							+ " AND diagMapper.organizationId=:organizationId"
							+ " AND diagMapper.isDiagnosisCancel='N'"
							+ " AND diagMapper.isDeleted='N'"
							+ " AND diagMapper.isResolved='N'"
							+ " order by diagMapper.patientDiagnosisId desc")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					
					List<InvestigationOrderMasterNewDto> listlabOrder=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
									 " and ord_mst.patient_id =:patientId and ord_mst.admission_id =:admissionId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
					
					List<CoversheetInstructionDto> listAdvice=sessionFactory.getCurrentSession()
							.createQuery("SELECT cover.instructionId as instructionId, "
									+ " cover.instructionDetailsId as instructionDetailsId, "
									+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
									+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
									+ " (case "
									+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
									+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
									+ " else 'Admin' end) as userName,"
									+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
									+ " FROM CoversheetInstruction cover "
									+ " LEFT JOIN cover.instructionMaster instr "
									+ " LEFT JOIN cover.userMaster userMaster "
									+ " LEFT JOIN userMaster.doctorMastersList doc "
									+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
									" where " + 
									"	cover.patientId =:patientId " + 
									"	and cover.status ='A'" +
									"	and cover.admissionId =:admissionId " + 
									"	and cover.organizationId =:organizationId " 
									)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					
					List<MedicationDTO> listMedication=sessionFactory.getCurrentSession()
							.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", " + 
									" pres.id as \"prescriptionId\", " + 
									" presDtl.id as \"prescriptionDetailsid\", " + 
									" itm.name as \"itemName\", " + 
									" preFreDtl.dose as \"doseDesc\", " + 
									" su.unit as \"strengthDesc\", " + 
									" preFreDtl.frequency_id as \"frequencyId\", " + 
									" freq.description as \"frequencyDesc\", " + 
									" preFreDtl.route_id as \"roaId\", " + 
									" presDtl.order_qty as \"orderQty\", " +
									" presDtl.duration as \"duration\", " + 
									" presDtl.duration_unit as \"durationUnit\", "+
									" presDtl.duration as \"durationCount\", " + 
									" 1 as \"visitTypeId\", " + 
									" roa.description as \"roaDesc\", " + 
									" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," + 
									" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
									"	preFreDtl.start_date as \"startDateNew\", " + 
									"	preFreDtl.stop_date as \"endDateNew\", " + 
									" presDtl.instruction_id as \"instructionId\" ," + 
									" instr.instruction_desc as \"instructionDesc\", " + 
									" presDtl.is_medication_stop as \"isMedicationStop\","+
									" gen.name as \"genericName\" " + 
									" FROM pharmacy.t_prescription pres " + 
									" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
									" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
									" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
									" INNER JOIN public.m_item itm on itm.id=presDtl.item_id " + 
									" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
									" INNER JOIN public.m_generic gen on itm.generic_id = gen.id " + 
									" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
									" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
									" where " + 
									"	pres.patient_id =:patientId " + 
									"	and pres.is_active =true " + 
									"	and presDtl.is_active =true " + 
									"	and pres.admission_id =:admissionId "
									+ " and pres.organization_id =:organizationId"
									+ " AND presDtl.is_medication_stop='N' " 
									+ " and presDtl.status_id != 9 "
									)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
					
					
					String query1="select reg.patient_remark  as \"patientRemark\" " + 
							"from patient.t_patient_registration reg     " + 
							"where reg.patient_id=:patientId " + 
							"and reg.organization_id =:organizationId " + 
							"and reg.unit_id =:unitId ";
					List<EncounterMasterDto> patientRemarklist=sessionFactory.getCurrentSession().createSQLQuery(query1)
							.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
							.setParameter("patientId",  encounterMasterDto.getPatientId())
							.setParameter("unitId",  encounterMasterDto.getUnitId())					
							.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
					
					
					String query2="select     " + 
							"	distinct appointmen0_.vital_id as \"vitalId\", " + 
							"	appointmen0_.appointment_vital_mapper_id as \"appointmentVitalMapperId\",     " + 
							"	listorgwis2_.vital_order_opd as \"vitalOrderOpd\",     " + 
							"	appointmen0_.admission_id as \"admissionId\",     " + 
							"	appointmen0_.patient_id as \"patientId\",     " + 
							"	appointmen0_.value as \"value\",     " + 
							"	appointmen0_.status as \"status\",     " + 
							"	appointmen0_.is_self_triage as \"isSelfTriage\",     " + 
							"	appointmen0_.created_by as \"createdBy\",     " + 
							"	appointmen0_.updated_by as \"updatedBy\",     " + 
							"	to_char(     " + 
							"		appointmen0_.created_date,     " + 
							"		'YYYY/MM/DD'     " + 
							"	) as \"creatDate\",     " + 
							"	to_char(     " + 
							"		appointmen0_.created_date,     " + 
							"		'DD/MM/YYYY'     " + 
							"	) as \"createdDate\",     " + 
							"	appointmen0_.created_date as \"createdDateNew\",     " + 
							"	to_char(     " + 
							"		appointmen0_.post_facto_date,     " + 
							"		'DD-MM-YYYY'     " + 
							"	) as \"postFactoDate\",     " + 
							"	to_char(     " + 
							"		appointmen0_.post_facto_time,     " + 
							"		'HH24:MI'     " + 
							"	) as \"postFactoTime\",     " + 
							"	appointmen0_.is_post_facto as \"isPostFacto\",     " + 
							"	appointmen0_.encounter_id as \"encounterId\",     " + 
							"	to_char(     " + 
							"		appointmen0_.created_date,     " + 
							"		'HH24:MI'     " + 
							"	) as \"createdTime\",     " + 
							"	appointmen0_.created_date as \"createdTimeNew\",     " + 
							"	to_char(     " + 
							"		appointmen0_.updated_date,     " + 
							"		'DD-MM-YYYY'     " + 
							"	) as \"updatedDate\",     " + 
							"	appointmen0_.updated_date as \"updatedDateNew\",     " + 
							"	appointmen0_.unit_id as \"unitId\",     " + 
							"	appointmen0_.organization_id as \"organizationId\",     " + 
							"	vitalmaste1_.vital_name as \"vitalName\",     " + 
							"	vitalmaste1_.unit as \"unitDesc\",     " + 
							"	vitalmaste1_.is_icu as \"isIcu\",     " + 
							"	appointmen0_.role_id as \"roleId\",     " + 
							"	appointmen0_.is_reviewed_flag as \"isReviewedFlag\",     " + 
							"	(     " + 
							"		coalesce(employeema4_.first_name)|| ' ' || coalesce(employeema4_.middle_name)|| ' ' || coalesce(employeema4_.last_name)     " + 
							"	) as \"employeeName\",     " + 
							"	(     " + 
							"		'Dr. ' || coalesce(doctormast5_.first_name)|| ' ' || coalesce(doctormast5_.middle_name)|| ' ' || coalesce(doctormast5_.last_name)     " + 
							"	) as \"doctorName\",     " + 
							"	appointmen0_.dialysis_event_id as \"dialysisEventId\",      "+
							"   appointmen0_.patient_dialysis_rec_id  as \"patientDialysisRecId\"  " + 
							"from     " + 
							"	patient.t_appointment_vital_mapper appointmen0_     " + 
							"inner join emr.m_vital_master vitalmaste1_ on     " + 
							"	appointmen0_.vital_id = vitalmaste1_.vital_id     " + 
							"inner join emr.t_org_wise_vital_mapper listorgwis2_ on     " + 
							"	vitalmaste1_.vital_id = listorgwis2_.vital_id     " + 
							"	and(     " + 
							"		listorgwis2_.organization_id =107     " + 
							"	)     " + 
							"left outer join public.m_user_master usermaster3_ on     " + 
							"	appointmen0_.created_by = usermaster3_.user_id     " + 
							"left outer join public.m_employee_master employeema4_ on     " + 
							"	usermaster3_.user_id = employeema4_.user_id     " + 
							"left outer join doctor.m_doctor_master doctormast5_ on     " + 
							"	usermaster3_.user_id = doctormast5_.user_id     " + 
							"where     " + 
							"	appointmen0_.organization_id =:organizationId     " + 
							"	and appointmen0_.patient_id =:patientId     " + 
							"	and appointmen0_.status = 'A'     " + 
							"	and appointmen0_.value != ''     " + 
							"	and appointmen0_.is_entered_in_error = 'N'     " + 
							"	and appointmen0_.admission_id =:admissionId     " ;
					List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto=sessionFactory.getCurrentSession().createSQLQuery(query2)
							.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
							.setParameter("patientId",  encounterMasterDto.getPatientId())
							.setParameter("admissionId",  dto.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
					
				
					
					dto.setComplaintlistDataLastVisit(listComplaints);
					dto.setDiagnosisListData(listDiagnosis);
					dto.setMedicationDataList(listMedication);
					dto.setLabOrderList(listlabOrder);
					dto.setAdviceDataList(listAdvice);
					dto.setPatientRemarklist(patientRemarklist);
					dto.setListAppointmentVitalMapperDto(listAppointmentVitalMapperDto);
				
					
					
					
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getLastAdmisionDataOfPatientForWebAPP failed", he);
	} catch (Exception e) {
	logger.error("getLastAdmisionDataOfPatientForWebAPP failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}

	@Override
	public Response getSubSpecialityById(Integer serviceId) throws ApplicationException {

		try {	
			
			String query = "select sub_speciality_id as \"subSpecialityId\" from service.m_service_master msm where service_master_id  =:serviceId limit 1  ";
			SubSpecialityMasterDto obj = (SubSpecialityMasterDto) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("serviceId", serviceId)
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Character getIsBedChargesService(ContractConsumptionDto consumptionDto) {
	if(consumptionDto != null && consumptionDto.getUnitId() != null && consumptionDto.getOrganizationId() != null && consumptionDto.getServiceId() != null ){
		Character gstFlag = (Character) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT is_bed_charges_service FROM service.t_unit_service_master_details "
						+ "WHERE unit_id = :unitId AND organization_id = :organizationId "
						+ "AND service_master_id = :serviceId AND status = 'A'")
				.setParameter("unitId", consumptionDto.getUnitId())
				.setParameter("organizationId", consumptionDto.getOrganizationId())
				.setParameter("serviceId", consumptionDto.getServiceId())
				.uniqueResult();

		return gstFlag;
	}else{
		 return null;
	}
		
	}

	@Override
	public Response getReferalDetailsByEncounterId(EncounterMasterDto encounterMasterDto) throws ApplicationException {

		try {	
			
			String query = "	select " + 
					"	enMst.encounter_id as \"encounterId\", " + 
					"	refrlTypeMst.referral_type_name as \"referralTypeName\", " + 
					"	reflMst.referral_name as \"referralName\", " + 
					"	reflMst.referral_id as \"referralId\", " + 
					"	refrDtls.referral_remark as \"referralRemark\", " + 
					"	temp.otherConsultants as \"otherConsultants\"  " + 
					"  from " + 
					"	opd.t_encounter_master enMst " + 
					"   left join adt.m_nursing_station_master mnsm on " + 
					"	mnsm.nursing_station_id = enMst.nursing_station_id " + 
					"   left join public.t_referal_details refrDtls on " + 
					"	(case " + 
					"		when enMst.against_enc_id is not null then refrDtls.encounter_id = enMst.against_enc_id " + 
					"		else refrDtls.encounter_id = enMst.encounter_id " + 
					"	end) " + 
					"	and refrDtls.status = 'A' " + 
					"   left join adt.m_referral_type_master refrlTypeMst on " + 
					"	refrlTypeMst.referral_type_id = refrDtls.referral_type_id " + 
					"   left join adt.m_referral_master reflMst on " + 
					"	reflMst.referral_id = refrDtls.referral_id " + 
					"	and reflMst.status = 'A' " + 
					"   left join ( " + 
					"	select " + 
					"		concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.last_name)) as otherConsultants, " + 
					"		mpr.encounter_id, " + 
					"		mpr.doctor_id " + 
					"	from " + 
					"		opd.t_encounter_doctor_consultants_mapper mpr " + 
					"	inner join doctor.m_doctor_master doc on " + 
					"		doc.doctor_id = mpr.doctor_id) temp on " + 
					"	temp.encounter_id = enMst.encounter_id " + 
					"	and temp.doctor_id != enMst.doctor_id " + 
					"   left join public.m_clinic_master clinMst on " + 
					"	clinMst.clinic_master_id = enMst.clinic_id " + 
					"   left join opd.t_nursing_station_clinic_mapper nurStatClinMapp on " + 
					"	nurStatClinMapp.clinic_id = clinMst.clinic_master_id " + 
					"   where " + 
					"	 enMst.encounter_id =:encounterId ";
			EncounterMasterDto list = (EncounterMasterDto) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, list);
		}catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
			throw e;
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response PatientDocStatusUpdate(PatientDocReqDto patientDocReqDto) throws ApplicationException {

		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.t_encounter_doc_details set  status='I' where encounter_doc_id  =:encounterDocId ")
					.setParameter("encounterDocId",patientDocReqDto.getEncounterDocId())
			.executeUpdate();
			
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("PatientDocStatusUpdate failed", he);
			} catch (Exception e) {
			logger.error("PatientDocStatusUpdate failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getPackageOrderCount(OrderDetailsMasterDto ordDtlsDto) throws ApplicationException {

		try {
			BigInteger gstFlag= (BigInteger) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from t_order_details_package_mapper todpm  "
					+ " inner join billing.t_package_consumption_master tpcm on tpcm.package_consumption_master_id  = todpm.package_consumption_master_id "
					+ "where order_details_id  =:orderDetailsId  and todpm.package_id is not null and tpcm.status = 'A' ")
					.setParameter("orderDetailsId",ordDtlsDto.getOrderDetailsId())
	                .uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, gstFlag);
			
			} catch (HibernateException he) {
			logger.error("getPackageOrderCount failed", he);
			} catch (Exception e) {
			logger.error("getPackageOrderCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);


	}

	public Response getOpPatientListByDocIdForVideo(AppointmentStatusSyncDto dto) throws ApplicationException {
		try {
            SlotMasterDto slotMasterDtosList1 = new SlotMasterDto();
            if(!dto.getCallToGetDocId()) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(date);
                StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SEARCH_ENCOUNTERED_LIST_FOR_VIDEO").getQueryString().toString());
                stringQuery.append(" AND (enDet.assignment_start_time is not null or (am.is_teleconsultation_apppointment = 1 "
                        + " 	and mbm.bill_status_id = 3 and enMst.status_id in (5,6,9))) ");
                stringQuery.append(" AND to_char(enMst.encounter_date,'yyyy-mm-dd') Between :formattedDate and  :formattedDate ");
                stringQuery.append(" AND patient.uuid=:uuid ");
                stringQuery.append(" order by Firstentmst.encounter_date  asc ");
                Integer count = null;
                slotMasterDtosList1 = (SlotMasterDto) sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
                        .setParameter("uuid", dto.getUuid())
                        .setParameter("formattedDate", formattedDate)
                        .setParameter("unitId", dto.getHisUnitId())
                        .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).uniqueResult();
            } else {
                slotMasterDtosList1 = (SlotMasterDto) sessionFactory
                        .getCurrentSession()
                        .createSQLQuery(
                                "SELECT mdm.doctor_id AS \"doctorId\" " +
                                        "FROM doctor.m_doctor_master mdm " +
                                        "WHERE mdm.global_doc_id = :globalDoctorId"
                        )
                        .setParameter("globalDoctorId", dto.getDoctorId())
                        .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
                        .uniqueResult();
            }
			return new Response(SUCCESS,SUCCESS_CODE , SUCCESS_MESSAGE,null, null,slotMasterDtosList1) ;
			}catch (Exception e) {
			logger.error("getOpPatientListByDocIdForVideo failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getLastEncounteredListOfPatientForAbdm(EncounterMasterDto encounterMasterDto) throws Exception {
	    try {
	        Set<String> encounterNumbers = new HashSet<>();
	        List<EncounterMasterDto> careContextListNew = encounterMasterDto.getCareContextList().stream()
	                .filter(dtow -> dtow.getEncounterNumber() != null && encounterNumbers.add(dtow.getEncounterNumber()))
	                .map(dtow -> {
	                    EncounterMasterDto dto = new EncounterMasterDto();
	                    dto.setEncounterNumber(dtow.getEncounterNumber());
	                    return dto;
	                }).collect(Collectors.toList());

	        List<EncounterMasterDto> resultList = new ArrayList<>();
	        		
	        for (EncounterMasterDto dto : careContextListNew) {
	            if (dto.getEncounterNumber() != null) {
	                resultList.addAll(fetchEncounterData(dto.getEncounterNumber(), encounterMasterDto));
	            }
	        }

	        for (EncounterMasterDto dto : resultList) {
	            processSignatureImage(dto);
	            processAdditionalData(dto, encounterMasterDto);
	        }

	        return new Response(SUCCESS, SUCCESS_CODE, null, resultList, null);
	    } catch (HibernateException he) {
	        throw he;
	    } catch (Exception e) {
	        throw e;
	    }
	}

	private List<EncounterMasterDto> fetchEncounterData(String encounterNumber, EncounterMasterDto encounterMasterDto) {
	    List<EncounterMasterDto> listEncounter = fetchEncounters(encounterNumber, encounterMasterDto.getOrganizationId());
	    List<EncounterMasterDto> listAdmission = fetchAdmissions(encounterNumber, encounterMasterDto.getOrganizationId());
	    listEncounter.addAll(listAdmission);
	    return listEncounter;
	}

	private List<EncounterMasterDto> fetchEncounters(String encounterNumber, int organizationId) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("select " + 
						"	encounter.encounter_id as \"encounterId\", " + 
						"	encounter.dept_id as \"deptId\", " + 
						"	encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " + 
						"	to_char(encounter.encounter_date, 'HH24:MI:SS') as \"visitTime\", " + 
						"	to_char(encounter.encounter_date, 'yyyy-mm-dd') as \"encounterDateString\", " + 
						"   to_char(encounter.encounter_date, 'DD/MM/YYYY HH24:MI:SS') as \"encounterDateString2\", " + 
						"   'OP' as \"visitTypeName\", " + 
						"	encounter.encounter_number as \"encounterNumber\", " + 
						"	encounter.doctor_id as \"doctorId\", " + 
						"   doc.signture_img as \"signtureImg\", " +
						"	encounter.patient_class_id as \"patientClassId\", " + 
						"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
						"	encounter.dept_id as \"specialityId\", " + 
						"	speciality.speciality_name as \"specialityName\" " + 
						"from " + 
						"	opd.t_encounter_master encounter " + 
						"inner join doctor.m_doctor_master doc on " + 
						"	encounter.doctor_id = doc.doctor_id " + 
						"inner join doctor.m_speciality_master speciality on " + 
						"	encounter.dept_id = speciality.speciality_id " + 
						"where " + 
						"	encounter.organization_id = :organizationId  " + 
						"	and encounter.encounter_number = :number  " + 
						 " limit 5")
	            .setParameter("organizationId", organizationId)
	            .setParameter("number", encounterNumber)
	            .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
	            .list();
	}

	private List<EncounterMasterDto> fetchAdmissions(String encounterNumber, int organizationId) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery( "select " +
					    "    admission.admission_id as \"admissionId\", " +
					    "   admission.speciality_id as \"deptId\", " +  // Placeholder for deptId
					    "   admission.visit_type_id as \"userDefinedVisitTypeId\", " + // Placeholder for userDefinedVisitTypeId
					    "   NULL as \"visitTime\", " + // Placeholder for visitTime
					    "   to_char(admission.created_date, 'yyyy-mm-dd') as \"encounterDateString\", " + // Placeholder for encounterDateString
					    "   to_char(admission.created_date, 'DD/MM/YYYY HH24:MI:SS') as \"encounterDateString2\", " +
					    "   admission.admission_number as \"encounterNumber\", " +
					    "   'IP' as \"visitTypeName\", " + 
					    "   admission.doctor_id as \"doctorId\", " +
					    "   doc.signture_img as \"signtureImg\", " +
					    "   NULL as \"patientClassId\", " +  // Placeholder for patientClassId
					    "	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " +
					    "   NULL as \"specialityId\", " + // Placeholder for specialityId
					    "   NULL as \"specialityName\" " + // Placeholder for specialityName
					    "from " +
					    "   adt.t_admission admission " +
					    "inner join doctor.m_doctor_master doc on " + 
						"	admission.doctor_id = doc.doctor_id " + 
					    "where " +
					    "   admission.organization_id = :organizationId " +
					    "   and admission.admission_number = :number " +
					     " limit 5")
	            .setParameter("organizationId", organizationId)
	            .setParameter("number", encounterNumber)
	            .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
	            .list();
	}

	private void processSignatureImage(EncounterMasterDto dto) throws IOException {
	    String urlPath = dto.getSigntureImg();
	    if (urlPath != null) {
	        URL url = new URL(urlPath);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.connect();

	        try (InputStream inputStream = connection.getInputStream();
	             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	            String base64Encoded = Base64.getEncoder().encodeToString(outputStream.toByteArray());
	            dto.setSigntureImg(base64Encoded);
	        }
	    }
	}

	private void processAdditionalData(EncounterMasterDto dto, EncounterMasterDto encounterMasterDto) {
	    if (dto.getEncounterId() != null) {
	        dto.setComplaintlistData(fetchComplaints(dto.getEncounterId(), encounterMasterDto));
	        dto.setMedicationDataList(fetchMedications(dto.getEncounterId(), encounterMasterDto));
	        dto.setLabOrderList(fetchLabOrders(dto.getEncounterId(), encounterMasterDto));
	        dto.setAdviceDataList(fetchAdviceData(dto.getEncounterId(), encounterMasterDto));
	        dto.setAllergyDataList(fetchAllergyData(dto.getEncounterId(), encounterMasterDto));
	        dto.setListPatientFamilyHistoryDto(fetchFamilyHistoryData(dto.getEncounterId(), encounterMasterDto));
	        dto.setVacineList(fetchVaccines(dto.getEncounterId(), encounterMasterDto));
	    } else if (dto.getAdmissionId() != null) {
	        dto.setComplaintlistData(fetchAdmissionComplaints(dto.getAdmissionId(), encounterMasterDto));
	        dto.setMedicationDataList(fetchAdmissionMedications(dto.getAdmissionId(), encounterMasterDto));
	        dto.setLabOrderList(fetchAdmissionLabOrders(dto.getAdmissionId(), encounterMasterDto));
	        dto.setAdviceDataList(fetchAdviceDataAdmission(dto.getAdmissionId(), encounterMasterDto));
	        dto.setAllergyDataList(fetchAllergyDataAdmission(dto.getAdmissionId(), encounterMasterDto));
	        dto.setListPatientFamilyHistoryDto(fetchFamilyHistoryDataAdmission(dto.getAdmissionId(), encounterMasterDto));
	        dto.setVacineList(fetchVaccinesByAdmission(dto.getAdmissionId(), encounterMasterDto));
	       
	    }
	    dto.setInvoiceList(fetcheInvoiceList(dto.getEncounterId(), dto.getAdmissionId()));
	    dto.setProcedureList(fetchProcedureData(dto.getEncounterId(), dto.getAdmissionId()));
	    dto.setListPatientMedicalHistoryDto(fetchMedicalHistory(dto.getEncounterId(), dto.getAdmissionId(), encounterMasterDto));
	    dto.setListAppointmentVitalMapperDto(fetchVitals(dto.getEncounterId(), dto.getAdmissionId(), encounterMasterDto));
	    dto.setObservationList(fetchObservations(dto.getEncounterId(), dto.getAdmissionId(), encounterMasterDto));
	    dto.setListPatientHabitDetailsDto(fetchPatientHabits(encounterMasterDto));
	}
	
	@Transactional
     private List<EncounterMasterDto> fetcheInvoiceList(Integer encounterId, Integer admissionId) {
    	 try {
    		 List<EncounterMasterDto> saleDatails = new ArrayList<>();
    		 ArrayList finallist = new ArrayList<>();
    		 List<EncounterMasterDto> finallistNew = new ArrayList<>();
    		 if(encounterId != null){
    			 saleDatails =  sessionFactory.getCurrentSession().createSQLQuery("select  id as \"saleId\" from pharmacy.t_sale where encounter_id =:encounterId "
    					 ).setParameter("encounterId", encounterId) 
    					 .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
    			
    		 }else{
    			 
    			 saleDatails =  sessionFactory.getCurrentSession().createSQLQuery("select  id as \"saleId\" from pharmacy.t_sale where admission_id=:admissionId  "
    					 ).setParameter("admissionId", admissionId) 
    					 .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
    			
    		 }
    		if(saleDatails != null ){
    			for(EncounterMasterDto billListResponse :saleDatails){
    				 String sql=" select * from abdm.get_pharmacy_invoice where Id=:id";
        			 Query queryNew = getSession().createSQLQuery(sql);
        			 queryNew.setParameter("id", billListResponse.getSaleId());
        			 finallist = (ArrayList) queryNew.list();
        			 EncounterMasterDto BillListResponse = new EncounterMasterDto();
        			 BillListResponse.setSaleId(billListResponse.getSaleId());
        			 BillListResponse.setItems(finallist);
        			 finallistNew.add(BillListResponse);
    			}
    			
    		}
    		 return finallistNew;
    		 
    	 } catch (HibernateException he) {
 	        throw he;
 	    } catch (Exception e) {
 	        throw e;
 	    }
	 }

/*	@org.springframework.transaction.annotation.Transactional
	private BillListResponse fetcheInvoiceList(Long id, Integer admissionId, EncounterMasterDto encounterMasterDto) {
		try {
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			Response response = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)response.getObject();
			
			
			
			BillListResponse billResp = getBillDetailsBPABDM(id,unitApplicationConfigurationDtos.getSelfPayeeId(),encounterMasterDto.getUnitId());
			
			if(billResp.getPatientId() != null)
			{
				BillListResponse creditDeu = getCreditDuaAmountABDM(id,unitApplicationConfigurationDtos.getSelfPayeeId());
				
				List<Long> saleIds = new ArrayList<>();
				saleIds.add(id);
				List<BillListResponse>  issueReturnList=isIssueReturnAbdm(saleIds);
				billResp.setIsIssueReturn(issueReturnList != null && issueReturnList.size()>0 && issueReturnList.get(0).getIsIssueReturn()? issueReturnList.get(0).getIsIssueReturn() : false);
				billResp.setCreditDueAmount(creditDeu.getCreditDueAmount());
				billResp.setPatientId(billResp.getPatientId());
				billResp.setUhid(billResp.getUhid());
				Timestamp now = new Timestamp((new DateTime()).getMillis());
				Boolean checkDoctor = false;
	            try {
	                String pat = billResp.getPatientDetails();
	            }
	            catch (Exception e) {
	                checkDoctor = true;
	            }
	            if(!checkDoctor) {
	            	billResp.setPatientDetails((billResp.getPatientDetails()!= null ? billResp.getPatientDetails():"") +(billResp.getPatientDob()!=null? Period.between(billResp.getPatientDob().toLocalDateTime().toLocalDate(), 
	    					now.toLocalDateTime().toLocalDate()).getYears():""));
	            }
				
				billResp.setGenderId(billResp.getGenderId());
				billResp.setImagePath(billResp.getImagePath());
			}
			else if(billResp.getTempPatId()!= null)
			{
				billResp.setPatientId(billResp.getTempPatId());
				billResp.setPatientDetails(billResp.getTempPatientDetails());
				billResp.setGenderId(billResp.getTempGenderId());
			}
			List<ItemDetailsResponse> itemResponse=getSaleDetailsBPABDM(billResp.getId());
			for (ItemDetailsResponse itemDetailsResponse : itemResponse) {
					Integer getOrgId=getOrgIdAbdm(itemDetailsResponse.getId());
					Integer leaseQuantity = Util.GetActualIssuedQuantity(itemDetailsResponse.getQuantityPrescribed()!=null ? itemDetailsResponse.getQuantityPrescribed():0, getUom(itemDetailsResponse.getItemId(),getOrgId),
							new UomType(itemDetailsResponse.getUomTypeId()));
					itemDetailsResponse.setQuantityPrescribed(leaseQuantity);
			}
			billResp.setItems(itemResponse);
			return billResp;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	@Transactional
	public List<PurchaseOrderUomMapper> getUom(Integer itemId,Integer orgId) {

		List<UomResponse> uomResponse = globalDao.getUnitOfMeasurementListByItem(itemId,orgId);
		List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
		for (UomResponse uom : uomResponse) {
			PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
					new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
					uom.getStoreUom());
			prUomResponse.add(prUom);
		}
		return prUomResponse;
	}
	
	@Transactional
	    public Integer getOrgIdAbdm(Long id) {
	        String sqlQuery1 = " select organization_id  from pharmacy.t_sale_detail where id="+id.intValue()+"";
	        Integer orgId=(Integer) getSession().createSQLQuery(sqlQuery1.toString()).uniqueResult();
	        return orgId;
	    }

	 
	

	@Transactional
	public List<ItemDetailsResponse> getSaleDetailsBPABDM(Long id) 
	{
		String hql = "select sd.id as id, item.id as itemId, item.name as itemName, item.code as code ,generic.name as genericName, generic.id as genericId, "
				+ "manufacturer.name as manufacturer, batch.id as batchId,  sd.consessionAmount as concession ,  "
				+ "batch.batchNo as batchCode, batch.expiryDate as expiryDate, uomUnit.id as uomId, uomUnit.units as uomUnit,uomType.id as uomTypeId, "
				+ "sd.quantity as quantity,"
				+ "prescription.orderQty as quantityPrescribed, sd.rate as rate, sd.totalAmount as totalAmount, sd.discountPercentage as discountPercentage , "
				+ "sd.discountAmount as discount, sd.taxAmount as taxAmount, "
				+ "sd.netAmount as netAmount, tax.name as taxName, sd.taxPercentage as taxPercent, sd.creditPayable as creditPayable ,sd.selfPayable as selfPayable,sd.packageConsumptionAmt as packageConsumptionAmt "
				+ " FROM SaleDetail as sd "
				+ " INNER JOIN sd.sale sale "
				+ " LEFT JOIN sd.batch as batch "
				+ " LEFT JOIN sd.item as item "
				+ " LEFT JOIN item.manufacturer as manufacturer "
				+ " LEFT JOIN item.generic as generic "
				+ " LEFT JOIN sd.uomUnit as uomUnit "
				+ " LEFT JOIN sd.uomType as uomType "
				+ " LEFT JOIN sd.prescriptionDetail as prescription "
				+ " LEFT JOIN sd .tax as tax "
				+ " where sale.id = :id order by sd.id desc ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemDetailsResponse.class));
		return query.list();
		
	}
	
	

	@Transactional
	public List<BillListResponse> isIssueReturnAbdm(List<Long> saleIds) {
		String sqlQuery1 = " select is_return as \"isIssueReturn\" , sale_id as \"id\" from pharmacy.t_issue where sale_id in("+saleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ";
		
		List<BillListResponse> pharmacyInvList =  getSession()
				.createSQLQuery(sqlQuery1)
				.addScalar("isIssueReturn")
				.addScalar("id", StandardBasicTypes.LONG)
				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
		return pharmacyInvList;
	}
	
	

	@Transactional
	public BillListResponse getCreditDuaAmountABDM(Long id, Integer selfPayeeId) {
		String queryString=
				" select sum(due_amount) as \"creditDueAmount\"   from pharmacy.t_pharmacy_bill_payee_details where t_sale_id =:id and payee_id not in(:selfPayeeId) and status  = 'A' ";
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("id", id);
		query.setParameter("selfPayeeId", selfPayeeId);
		query.setResultTransformer(Transformers.aliasToBean(BillListResponse.class));
		return (BillListResponse) query.uniqueResult();
		
	}

	
	@Transactional
	public BillListResponse getBillDetailsBPABDM(Long id, Integer selfPayeeId, Integer unitId) {


		String sql="select status_id from pharmacy.t_sale where unit_id="+unitId+" and id="+id+" ";
		Query query1 = getSession().createSQLQuery(sql);
		Integer statusId=(Integer) query1.uniqueResult();
		boolean isActiveStatus=true;
		boolean isDeletedStatus=false;
		if(statusId==9)
		{
			 isActiveStatus=false;
			 isDeletedStatus=true;
		}
			
		
		String hql = "SELECT DISTINCT sale.id as id, sale.addedDate as billDate, sale.billNo as billNo, discountType.id as discountTypeId,sale.isConsolidatedBill as isConsolidatedBill, "
				+ " discountCat.category as discountCategory, sale.taxAmount as taxAmount, sale.totalAmount as totalAmount, sale.concession as concession , "
				+ " sale.discount as discount, sale.totalDiscountAmount as totalDiscount,sale.admission.admissionId as admissionId , sale.encounter.encounterId as encounterId , "
				+ " sale.roundOff as roundOff, sale.netAmount as netAmount,sale.totalCreditPayable as totalCreditPayable,coalesce(sale.totalSelfPayable,0) as totalSelfPayable,sale.paidAmount as paidAmt, payee.dueAmount as selfDueAmount,"
				+ " coalesce((payee.dueAmount),0) as balanceAmt, sale.totalDiscountAmount as  totalDiscount , "
				+ " status.status as status, saleType.type as saleType, saleType.id as saleTypeId, "
				+ " patient.patientId as patientId, coalesce(patient.uhidNumber,tempPatient.tempUhid) as uhid, concat(prefix.prefixCode, '. ',patient.firstName,' ', "
				+ " patient.middleName, ' ',patient.lastName,' | ', gender.genderCode) as patientDetails, "
				+ " concat(tempPatientPrefix.prefixCode, '. ',tempPatient.patientName,' | ', "
				+ " tempPatient.age, ' | ', tempGender.genderCode) as tempPatientDetails, tempPatient.id as tempPatId, "
				+ " patient.dob as patientDob,"
				+ " case when (tempPatient.patientName is null) then concat(patient.firstName,' ',patient.lastName) else (tempPatient.patientName) end as patientName,"
				+ " case when (tempDoctor.doctorName is not null) then (tempDoctor.doctorName) "
				+ " when (doctor.doctorId is not null) "
				+ " then concat(doctor.firstName, ' ', doctor.middleName,' ', doctor.lastName) "
				+ " when (doctorSale.doctorId is not null) "
				+ " then concat(doctorSale.firstName, ' ', doctorSale.middleName,' ', doctorSale.lastName) "
				+ " when (doctorPatIndent.doctorId is not null) "
				+ " then concat(doctorPatIndent.firstName, ' ', doctorPatIndent.middleName,' ', doctorPatIndent.lastName) "
				+ "  end as doctorName, " + " gender.genderId As genderId, " + " patient.imagePath As imagePath, "
				+ " tempGender.genderId As tempGenderId, " + " patient.registrationTypeId as registrationTypeId , "
				+ " case when (doctor.id is null) then (tempDoctor.id) else (doctor.id) end as doctorId "
				
				+ " FROM Sale AS sale "
				+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false "
				+ "		LEFT JOIN sale.prescription as prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN sale.saleDetailsList as items WITH items.isActive ="+isActiveStatus+" AND items.isDeleted ="+isDeletedStatus+" AND items.unit.unitId = :unitId "
				+ "		LEFT JOIN items.batch as batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.unit.unitId = :unitId "
				+ "		LEFT JOIN batch.item as item WITH item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
				+ "		LEFT JOIN item.generic as generic WITH generic.isActive = true AND generic.isDeleted = false AND generic.unit.unitId = :unitId "
				+ "		LEFT JOIN item.manufacturer as manufacturer WITH manufacturer.isActive = true AND manufacturer.isDeleted = false "
				+ "		LEFT JOIN items.uomUnit as uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false AND uomUnit.unit.unitId = :unitId "
				+ "		LEFT JOIN items.tax as tax WITH tax.isActive = true AND tax.isDeleted = false AND tax.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.billPayeeList as payee WITH payee.status = 'A' AND payee.payeeId = :payeeId "
				+ "		LEFT JOIN sale.discountType as discountType WITH discountType.isActive = true AND discountType.isDeleted = false AND discountType.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A' "
				+ " 	LEFT JOIN patient.prefix AS prefix  " + " 	LEFT JOIN patient.gender AS gender "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false AND tempPatient.unit.unitId = :unitId "
				+ "		LEFT JOIN tempPatient.gender AS tempGender "
				+ " 	LEFT JOIN tempPatient.prefix AS tempPatientPrefix  "
				+ "		LEFT JOIN prescription.doctor as doctor WITH doctor.status = 'A' "
				+ "		LEFT JOIN sale.doctor as doctorSale WITH doctorSale.status = 'A' "
				+ "		LEFT JOIN sale.tempDoctor as tempDoctor WITH tempDoctor.isActive = true AND tempDoctor.isDeleted = false AND tempDoctor.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.discountCategory as discountCat With discountCat.isActive = true AND discountCat.isDeleted = false AND discountCat.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.issueList as issue WITH issue.isActive = true AND issue.isDeleted = false "
				+ "		LEFT JOIN issue.patientIndent as patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
				+ "		LEFT JOIN patientIndent.doctor as doctorPatIndent WITH doctorPatIndent.status = 'A' "
				+ " WHERE sale.id = :id  AND sale.unit.unitId = :unitId  "; //HIS-17458

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId);
		query.setParameter("payeeId", selfPayeeId);
		query.setResultTransformer(Transformers.aliasToBean(BillListResponse.class));
		return (BillListResponse) query.uniqueResult();
	
	}

	@org.springframework.transaction.annotation.Transactional
    private List<VaccineImmunizationMapperDto> fetchVaccinesByAdmission(Integer admissionId,
			EncounterMasterDto encounterMasterDto) {
    	try {
    		if (admissionId == null) return Collections.emptyList();
    	  return sessionFactory.getCurrentSession()
  	            .getNamedQuery("GET_VACCINE_IMMUNIZATION_MAPPER_ABDM_BYADDMISSION")
  	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
  	            .setParameter("patientId", encounterMasterDto.getPatientId())
  	           .setParameter("admissionId", admissionId)
  	            .setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class))
  	            .list();
    	}catch (Exception e) {
    	logger.error("fetchVaccinesByAdmission failed", e);
    	}
		return null;
	}

	@org.springframework.transaction.annotation.Transactional
	private List<VaccineImmunizationMapperDto> fetchVaccines(Integer encounterId,
			EncounterMasterDto encounterMasterDto) {
    	try {
    		if (encounterId == null) return Collections.emptyList();
    	  return sessionFactory.getCurrentSession()
  	            .getNamedQuery("GET_VACCINE_IMMUNIZATION_MAPPER_ABDM_BYENCOUNTER")
  	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
  	            .setParameter("patientId", encounterMasterDto.getPatientId())
  	             .setParameter("encounterId", encounterId)
  	            .setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class))
  	            .list();
    	}catch (Exception e) {
    	logger.error("fetchVaccines failed", e);
    	}
		return null;
	}

	@org.springframework.transaction.annotation.Transactional
	private List<PatientFamilyHistoryDto> fetchFamilyHistoryDataAdmission(Integer admissionId,
			EncounterMasterDto encounterMasterDto) {
	    if (admissionId == null) return Collections.emptyList();
	    return sessionFactory.getCurrentSession()
	            .createQuery(" SELECT 		patientFamilyHist.familyHistoryId as familyHistoryId, patientFamilyHist.status as status, "
	            		+ "        		patientFamilyHist.remark as remark, patientFamilyHist.isNoFamilyHistoryStatus as isNoFamilyHistoryStatus,"
	            		+ "				patientFamilyHist.relationId as relationId, patientFamilyHist.diagnosisId as diagnosisId, "
	            		+ "				patientFamilyHist.encounterId as encounterId, patientFamilyHist.covTemplateId as covTemplateId, "
	            		+ "				patientFamilyHist.roleId as roleId,"
	            		+ "				patientFamilyHist.isEnterInErrorStatus as isEnterInErrorStatus,"
	            		+ "  			patientFamilyHist.patientId as patientId, patientFamilyHist.organizationId as organizationId, patientFamilyHist.unitId as unitId,"
	            		+ "				patientFamilyHist.createdBy as createdBy, patientFamilyHist.updatedBy as updatedBy, patientFamilyHist.isReviewedFlag as isReviewedFlag, "
	            		+ "         (case "
	            		+ "         when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
	            		+ "         when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
	            		+ "         else 'Admin' end) as userName,"
	            		+ "				to_char(patientFamilyHist.createdDate, 'DD/MM/YYYY') as createdDate,patientFamilyHist.createdDate as createdDateNew, to_char(patientFamilyHist.updatedDate, 'DD/MM/YYYY') as updatedDate,"
	            		+ " 			relationMst.desc as desc, diagMst.code as diagnosisCode, diagMst.diagnosisName as diagnosisName "
	            		+ " FROM 		PatientFamilyHistory patientFamilyHist "
	            		+ " INNER JOIN 	patientFamilyHist.patientRegistration patientReg "
	            		+ " LEFT JOIN 	patientFamilyHist.relationMaster relationMst "
	            		+ "  LEFT JOIN	patientFamilyHist.diagnosisMaster diagMst "
	            		+ "  LEFT JOIN   patientFamilyHist.userMaster um "
	            		+ "  LEFT JOIN   um.employeeMastersList empmst "
	            		+ "  LEFT JOIN   um.doctorMastersList docmst "
	            		+ " WHERE  patientFamilyHist.patientId=:patientId "
	            		+ " AND  patientFamilyHist.status = 'A' "
//	            		+ " AND 		patientFamilyHist.unitId=:unitId "
	            		+ " AND patientFamilyHist.organizationId =:organizationId " 
	            		+"	and patientFamilyHist.admissionId =:admissionId " 
	            		+ " ORDER BY  patientFamilyHist.familyHistoryId asc")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("admissionId", admissionId)
	            .setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class))
	            .list();
	}

    @org.springframework.transaction.annotation.Transactional
	private List<PatientFamilyHistoryDto> fetchFamilyHistoryData(Integer encounterId,
			EncounterMasterDto encounterMasterDto) {
	    if (encounterId == null) return Collections.emptyList();
	    return sessionFactory.getCurrentSession()
	            .createQuery(" SELECT 		patientFamilyHist.familyHistoryId as familyHistoryId, patientFamilyHist.status as status, "
	            		+ "        		patientFamilyHist.remark as remark, patientFamilyHist.isNoFamilyHistoryStatus as isNoFamilyHistoryStatus,"
	            		+ "				patientFamilyHist.relationId as relationId, patientFamilyHist.diagnosisId as diagnosisId, "
	            		+ "				patientFamilyHist.encounterId as encounterId, patientFamilyHist.covTemplateId as covTemplateId, "
	            		+ "				patientFamilyHist.roleId as roleId,"
	            		+ "				patientFamilyHist.isEnterInErrorStatus as isEnterInErrorStatus,"
	            		+ "  			patientFamilyHist.patientId as patientId, patientFamilyHist.organizationId as organizationId, patientFamilyHist.unitId as unitId,"
	            		+ "				patientFamilyHist.createdBy as createdBy, patientFamilyHist.updatedBy as updatedBy, patientFamilyHist.isReviewedFlag as isReviewedFlag, "
	            		+ "         (case "
	            		+ "         when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
	            		+ "         when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
	            		+ "         else 'Admin' end) as userName,"
	            		+ "				to_char(patientFamilyHist.createdDate, 'DD/MM/YYYY') as createdDate,patientFamilyHist.createdDate as createdDateNew, to_char(patientFamilyHist.updatedDate, 'DD/MM/YYYY') as updatedDate,"
	            		+ " 			relationMst.desc as desc, diagMst.code as diagnosisCode, diagMst.diagnosisName as diagnosisName "
	            		+ " FROM 		PatientFamilyHistory patientFamilyHist "
	            		+ " INNER JOIN 	patientFamilyHist.patientRegistration patientReg "
	            		+ " LEFT JOIN 	patientFamilyHist.relationMaster relationMst "
	            		+ "  LEFT JOIN	patientFamilyHist.diagnosisMaster diagMst "
	            		+ "  LEFT JOIN   patientFamilyHist.userMaster um "
	            		+ "  LEFT JOIN   um.employeeMastersList empmst "
	            		+ "  LEFT JOIN   um.doctorMastersList docmst "
	            		+ " WHERE  patientFamilyHist.patientId=:patientId "
	            		+ " AND  patientFamilyHist.status = 'A' "
//	            		+ " AND 		patientFamilyHist.unitId=:unitId "
	            		+ " AND patientFamilyHist.organizationId =:organizationId " 
	            		+"	and patientFamilyHist.encounterId =:encounterId " 
	            		+ " ORDER BY  patientFamilyHist.familyHistoryId asc")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("encounterId", encounterId)
	            .setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class))
	            .list();
	}

	private List<CoversheetInstructionDto> fetchAdviceDataAdmission(Integer admisisonId,
			EncounterMasterDto encounterMasterDto) {
	    if (admisisonId == null) return Collections.emptyList();
	    return sessionFactory.getCurrentSession()
	            .createQuery("SELECT cover.instructionId as instructionId, "
						+ " cover.instructionDetailsId as instructionDetailsId, "
						+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
						+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
						+ " (case "
						+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
						+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
						+ " else 'Admin' end) as userName,"
						+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
						+ " FROM CoversheetInstruction cover "
						+ " LEFT JOIN cover.instructionMaster instr "
						+ " LEFT JOIN cover.userMaster userMaster "
						+ " LEFT JOIN userMaster.doctorMastersList doc "
						+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
						" where " + 
						"	cover.patientId =:patientId " + 
						"	and cover.status ='A'" +
						"	and cover.organizationId =:organizationId " +
						"	and cover.admissionId =:admisisonId ")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("admisisonId", admisisonId)
	            .setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class))
	            .list();
	}

	private List<ComplaintsDto> fetchComplaints(int encounterId, EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("select " + 
						"	complaintAppointment.complaint_appo_id as \"complaintAppoId\", " +
						"	complaintAppointment.diagnosis_id as \"diagnosisId\", " + 
						"	complaintAppointment.encounter_id as \"encounterId\", " + 
						"	icpc1_.icpc_desc as \"diagnosisName\", " + 
						"	icpc1_.icpc_code as \"icpcCode\", " + 
						"	complaintAppointment.icpc_code_text as \"icpcCodeText\", " + 
						"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + 
						"from " + 
						"	patient.t_complaint_appo_mapper complaintAppointment " + 
						"inner join emr.m_icpc icpc1_ on " + 
						"	complaintAppointment.diagnosis_id = icpc1_.icpc_id " + 
						"left join emr.m_icpc_system icpcsystem2_ on " + 
						"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " + 
						"where " + 
						"	complaintAppointment.patient_id =:patientId " + 
						"	and complaintAppointment.status = 'A' " + 
						"	and complaintAppointment.encounter_id =:encounterId " + 
						"	and complaintAppointment.is_resolved = 'N' " +
						"	and complaintAppointment.organization_id =:organizationId " + 
						"	and(complaintAppointment.is_complaint_stop = 'N')")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("encounterId", encounterId)
	            .setResultTransformer(Transformers.aliasToBean(ComplaintsDto.class))
	            .list();
	}

	private List<ComplaintsDto> fetchAdmissionComplaints(int admissionId, EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .createQuery(" SELECT "
					+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.remark as remark, icpcSystemMaster.icpcSystemDesc as icpcSystemDesc "
					+ " FROM ComplaintAdmission comApp "
					+ " INNER JOIN comApp.diagnosisMaster digMast "
					+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
	                + " LEFT JOIN comApp.userMaster um " 
				    + " LEFT JOIN um.employeeMastersList empmst " 
				    + " LEFT JOIN um.doctorMastersList  docmst "
					+ " WHERE comApp.admissionId =:admissionId AND comApp.status = 'A'"
					+ " AND comApp.isComplaintStop ='N'"
					+ " AND comApp.organizationId =:organizationId "
					+ " AND comApp.isResolved ='N' "
					+ " ORDER BY icpcSystemMaster.icpcSystemDesc asc")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("admissionId", admissionId)
	            .setResultTransformer(Transformers.aliasToBean(ComplaintsDto.class))
	            .list();
	}

	private List<MedicationDTO> fetchMedications(int encounterId, EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("SELECT presDtl.item_id as \"itemId\", " + 
						" pres.id as \"prescriptionId\", " + 
						" (case when presDtl.item_id is not null then itm.name " + 
						"	else presDtl.item_name end) as \"itemName\", " + 
						" preFreDtl.dose as \"doseDesc\","+
						" su.unit as \"strengthDesc\", " + 
						" preFreDtl.frequency_id as \"frequencyId\", " + 
						" freq.description as \"frequencyDesc\", " +
                         " freq.no_of_administration_time as \"noOfAdministrationTime\", " +
						" freq.code as \"frequencyCode\", " +
						" preFreDtl.route_id as \"roaId\", " + 
						" presDtl.order_qty as \"orderQty\", " +
						" presDtl.duration as \"duration\", " + 
						" presDtl.duration as \"durationCount\", " + 
						" presDtl.instruction_desc as \"instructionText\", " + 
						" presDtl.duration_unit as \"durationUnit\", " + 
						" preFreDtl.start_date as \"startDateNew\", " + 
						" preFreDtl.stop_date as \"endDateNew\", " + 
						" 1 as \"visitTypeId\", " + 
						" roa.description as \"roaDesc\", " + 
						" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
						" to_char(preFreDtl.start_date,'DD/MM/YYYY HH24:MI:SS') as \"startDate\"," + 
						" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
						" presDtl.instruction_id as \"instructionId\" ," + 
						" instr.instruction_desc as \"instructionDesc\", " + 
						" presDtl.is_medication_stop as \"isMedicationStop\","+
						" (case when presDtl.item_id is not null then gen.name \r\n" + 
						"	else presDtl.generic_name end) as \"genericName\" " + 
						" FROM pharmacy.t_prescription pres " + 
						" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
						" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
						" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
						" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
						" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
						" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
						" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
						" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
						" where " + 
						"	pres.patient_id =:patientId " + 
						"	and pres.is_active =true " + 
						"	and presDtl.is_active =true " + 
						"	and pres.encounter_id =:encounterId "
						+ " and pres.organization_id =:organizationId"
						+ " AND presDtl.is_medication_stop='N' " 
						+ " and presDtl.status_id != 9 ")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("encounterId", encounterId)
	            .setResultTransformer(Transformers.aliasToBean(MedicationDTO.class))
	            .list();
	}

	private List<MedicationDTO> fetchAdmissionMedications(int admissionId, EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("SELECT presDtl.item_id as \"itemId\", " + 
						" pres.id as \"prescriptionId\", " + 
						" (case when presDtl.item_id is not null then itm.name " + 
						"	else presDtl.item_name end) as \"itemName\", " + 
						" preFreDtl.dose as \"doseDesc\","+
						" su.unit as \"strengthDesc\", " + 
						" preFreDtl.frequency_id as \"frequencyId\", " + 
						" freq.description as \"frequencyDesc\", " +
                         " freq.no_of_administration_time as \"noOfAdministrationTime\", " +
						" freq.code as \"frequencyCode\", " +
						" preFreDtl.route_id as \"roaId\", " + 
						" presDtl.order_qty as \"orderQty\", " +
						" presDtl.duration as \"duration\", " + 
						" presDtl.duration as \"durationCount\", " + 
						" presDtl.instruction_desc as \"instructionText\", " + 
						" presDtl.duration_unit as \"durationUnit\", " + 
						" preFreDtl.start_date as \"startDateNew\", " + 
						" preFreDtl.stop_date as \"endDateNew\", " + 
						" 1 as \"visitTypeId\", " + 
						" roa.description as \"roaDesc\", " + 
						" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
						" to_char(preFreDtl.start_date,'YYYY-MM-DD') as \"startDate\"," + 
						" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
						" presDtl.instruction_id as \"instructionId\" ," + 
						" instr.instruction_desc as \"instructionDesc\", " + 
						" presDtl.is_medication_stop as \"isMedicationStop\","+
						" (case when presDtl.item_id is not null then gen.name \r\n" + 
						"	else presDtl.generic_name end) as \"genericName\" " + 
						" FROM pharmacy.t_prescription pres " + 
						" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
						" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
						" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
						" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
						" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
						" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
						" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
						" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
						" where " + 
						"	pres.patient_id =:patientId " + 
						"	and pres.is_active =true " + 
						"	and presDtl.is_active =true " + 
						"	and pres.admission_id =:admissionId "
						+ " and pres.organization_id =:organizationId"
						+ " AND presDtl.is_medication_stop='N' " 
						+ " and presDtl.status_id != 9 ")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("admissionId", admissionId)
	            .setResultTransformer(Transformers.aliasToBean(MedicationDTO.class))
	            .list();
	}

	private List<InvestigationOrderMasterNewDto> fetchLabOrders(int encounterId, EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("select "  + 
						" ord_mst.order_id as \"orderId\", " + 
						" 1 as \"visitTypeId\"," + 
						" service_mst.speciality_id as \"specialityId\"," + 
						" service_mst.sub_speciality_id as \"subDeptId\"," + 
						" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
						"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
						" ord_mst.admission_id as \"admissionId\", " + 
						" ord_mst.encounter_id as \"encounterId\", " + 
						" ord_mst.patient_id as \"patientId\", " + 
						" ord_dtls.order_details_id as \"orderDetailsId\", " + 
						" ord_dtls.service_id as \"serviceMasterId\", " + 
						" ord_dtls.order_set_id as \"orderSetId\", " + 
						" ord_dtls.priority_id as \"priorityId\", " + 
						" ord_dtls.ord_remarks as \"ordRemarks\", " + 
						" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
						" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
						" service_mst.service_standard_name as \"serviceStandardName\", " + 
						" service_mst.service_standard_code as \"serviceStandardCode\", " + 
						" speciality_mst.speciality_name as \"specialityName\", " + 
						" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
						" service_dtls.is_panel as \"isPanel\", " + 
						" lab_res.lab_test_res_id  as \"labTestResId\", " + 
						" cast( " + 
						" 	null as integer " + 
						" ) as \"panelId\", " + 
						" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
						" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
						" to_char( " + 
						" 	ord_dtls.created_date, " + 
						" 	'dd/MM/yyyy HH24:MI' " + 
						" ) as \"createdDate\", " + 
						" to_char( " + 
						" 	sample_dtls.sample_gen_datetime, " + 
						" 	'dd/MM/yyyy HH24:MI' " + 
						" ) as \"sampleGenDatetime\", " + 
						" 	ord_dtls.created_date as \"createdDateNew\", " + 
						" ord_dtls.service_rendered as \"serviceRendered\", " + 
						" ord_dtls.is_package_service as \"isPackageService\", " + 
						" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
						" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
						" ord_dtls.status as \"orderStatus\", " + 
						 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
						 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
						 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
						 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
						 + " else 'Super Admin' end) as \"collectedUserName\","
						 
						+" (case " + 
						" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
						" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
						" else 'Admin' end) as \"userName\"," + 
						" ord_dtls.package_id as \"packageId\" " + 
						" from 	public.t_order_master ord_mst " + 
						" 		inner join public.t_order_details ord_dtls on " + 
						" ord_mst.order_id = ord_dtls.order_id " + 
						" 		inner join service.t_unit_service_mapper service_mppr on " + 
						" service_mppr.service_id = ord_dtls.service_id " + 
						" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
						" 		inner join service.t_unit_service_master_details service_dtls on " + 
						" service_dtls.service_master_id = service_mppr.service_id " + 
						" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
						" 		inner join service.m_service_master service_mst on " + 
						" service_mst.service_master_id = ord_dtls.service_id " + 
						" 		left join doctor.m_speciality_master speciality_mst on " + 
						" speciality_mst.speciality_id = service_mst.speciality_id " + 
						" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
						" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
						" and sample_dtls.sample_status_id <> 4 " + 
						" 		left join lab.m_test_status_master test_status on " + 
						" test_status.test_status_id = sample_dtls.sample_status_id " + 
						"        	left join lab.m_test_status_master test_status2 on " + 
						" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
						" 		left join lab.t_lab_test_result lab_res on " + 
						" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
						" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
						" 		left outer join public.t_template_transaction templatetr12_ on " + 
						" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
						" and( " + 
						" 	templatetr12_.status = 'A' " + 
						" ) " + 
						" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
						" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
						" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
						 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
			    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
						" 		where " + 
						" service_mst.status = 'A' " + 
						" and ord_dtls.status = 'A' " + 
						" and ord_dtls.old_ord_dtl_id is null " + 
						" and service_dtls.is_package = 'I' " + 
						" and ord_dtls.nursing_service_order_flag = 'N' " + 
						" and service_dtls.is_panel = 'N' "+
						 " and ord_mst.patient_id =:patientId and ord_mst.encounter_id =:encounterId "  
						+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
						 + " where organization_id =:organizationId"
						+" and is_rendering_speciality = 'Y' ) ")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("encounterId", encounterId)
	            .setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class))
	            .list();
	}

	private List<InvestigationOrderMasterNewDto> fetchAdmissionLabOrders(int admissionId, EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("select "  + 
						" ord_mst.order_id as \"orderId\", " + 
						" 1 as \"visitTypeId\"," + 
						" service_mst.speciality_id as \"specialityId\"," + 
						" service_mst.sub_speciality_id as \"subDeptId\"," + 
						" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
						"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
						" ord_mst.admission_id as \"admissionId\", " + 
						" ord_mst.encounter_id as \"encounterId\", " + 
						" ord_mst.patient_id as \"patientId\", " + 
						" ord_dtls.order_details_id as \"orderDetailsId\", " + 
						" ord_dtls.service_id as \"serviceMasterId\", " + 
						" ord_dtls.order_set_id as \"orderSetId\", " + 
						" ord_dtls.priority_id as \"priorityId\", " + 
						" ord_dtls.ord_remarks as \"ordRemarks\", " + 
						" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
						" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
						" service_mst.service_standard_name as \"serviceStandardName\", " + 
						" service_mst.service_standard_code as \"serviceStandardCode\", " + 
						" speciality_mst.speciality_name as \"specialityName\", " + 
						" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
						" service_dtls.is_panel as \"isPanel\", " + 
						" lab_res.lab_test_res_id  as \"labTestResId\", " + 
						" cast( " + 
						" 	null as integer " + 
						" ) as \"panelId\", " + 
						" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
						" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
						" to_char( " + 
						" 	ord_dtls.created_date, " + 
						" 	'dd/MM/yyyy HH24:MI' " + 
						" ) as \"createdDate\", " + 
						" to_char( " + 
						" 	sample_dtls.sample_gen_datetime, " + 
						" 	'dd/MM/yyyy HH24:MI' " + 
						" ) as \"sampleGenDatetime\", " + 
						" 	ord_dtls.created_date as \"createdDateNew\", " + 
						" ord_dtls.service_rendered as \"serviceRendered\", " + 
						" ord_dtls.is_package_service as \"isPackageService\", " + 
						" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
						" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
						" ord_dtls.status as \"orderStatus\", " + 
						 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
						 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
						 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
						 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
						 + " else 'Super Admin' end) as \"collectedUserName\","
						 
						+" (case " + 
						" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
						" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
						" else 'Admin' end) as \"userName\"," + 
						" ord_dtls.package_id as \"packageId\" " + 
						" from 	public.t_order_master ord_mst " + 
						" 		inner join public.t_order_details ord_dtls on " + 
						" ord_mst.order_id = ord_dtls.order_id " + 
						" 		inner join service.t_unit_service_mapper service_mppr on " + 
						" service_mppr.service_id = ord_dtls.service_id " + 
						" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
						" 		inner join service.t_unit_service_master_details service_dtls on " + 
						" service_dtls.service_master_id = service_mppr.service_id " + 
						" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
						" 		inner join service.m_service_master service_mst on " + 
						" service_mst.service_master_id = ord_dtls.service_id " + 
						" 		left join doctor.m_speciality_master speciality_mst on " + 
						" speciality_mst.speciality_id = service_mst.speciality_id " + 
						" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
						" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
						" and sample_dtls.sample_status_id <> 4 " + 
						" 		left join lab.m_test_status_master test_status on " + 
						" test_status.test_status_id = sample_dtls.sample_status_id " + 
						"        	left join lab.m_test_status_master test_status2 on " + 
						" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
						" 		left join lab.t_lab_test_result lab_res on " + 
						" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
						" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
						" 		left outer join public.t_template_transaction templatetr12_ on " + 
						" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
						" and( " + 
						" 	templatetr12_.status = 'A' " + 
						" ) " + 
						" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
						" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
						" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
						 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
			    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
						" 		where " + 
						" service_mst.status = 'A' " + 
						" and ord_dtls.status = 'A' " + 
						" and ord_dtls.old_ord_dtl_id is null " + 
						" and service_dtls.is_package = 'I' " + 
						" and ord_dtls.nursing_service_order_flag = 'N' " + 
						" and service_dtls.is_panel = 'N' "+
						 " and ord_mst.patient_id =:patientId and ord_mst.admission_id =:admissionId "  
						+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
						 + " where organization_id =:organizationId"
						+" and is_rendering_speciality = 'Y' ) " )
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("admissionId", admissionId)
	            .setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class))
	            .list();
	}

	private List<CoversheetInstructionDto> fetchAdviceData(Integer encounterId, EncounterMasterDto encounterMasterDto) {
	    if (encounterId == null) return Collections.emptyList();
	    return sessionFactory.getCurrentSession()
	            .createQuery("SELECT cover.instructionId as instructionId, "
						+ " cover.instructionDetailsId as instructionDetailsId, "
						+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
						+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
						+ " (case "
						+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
						+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
						+ " else 'Admin' end) as userName,"
						+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
						+ " FROM CoversheetInstruction cover "
						+ " LEFT JOIN cover.instructionMaster instr "
						+ " LEFT JOIN cover.userMaster userMaster "
						+ " LEFT JOIN userMaster.doctorMastersList doc "
						+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
						" where " + 
						"	cover.patientId =:patientId " + 
						"	and cover.status ='A'" +
						"	and cover.organizationId =:organizationId " +
						"	and cover.encounterId =:encounterId ")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("encounterId", encounterId)
	            .setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class))
	            .list();
	}

	private List<AllergyPatientMapperDto> fetchAllergyData(Integer encounterId, EncounterMasterDto encounterMasterDto) {
	    if (encounterId == null) return Collections.emptyList();
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("SELECT DISTINCT allergypat0_.allergy_id AS \"allergyId\", "
				           + "allergypat0_.patient_id AS \"patientId\", "
				           + "allergypat0_.generic_id AS \"genericId\" , "
				           + "allergypat0_.allergy_type_id AS \"allergyTypeId\", "
				           + "allergypat0_.remark AS \"remark\", "
				           + "allergymas1_.allergy_name AS \"allergyName\", "
				           + "allergymas1_.allergy_desc AS \"allergyDesc\", "
				           + "allergytyp3_.allergy_type_desc AS \"allergyTypeDesc\", "
				           + "allergypat0_.is_no_known_allergies AS \"isNoKnownAllergies\", "
				           + "generic2_.name AS \"genericName\" "
				           + "FROM patient.t_allergy_patient_mapper allergypat0_ "
				           + "LEFT OUTER JOIN emr.m_allergy_master allergymas1_ ON allergypat0_.allergy_id = allergymas1_.allergy_id "
				           + "LEFT OUTER JOIN public.m_generic generic2_ ON allergypat0_.generic_id = generic2_.id "
				           + "LEFT OUTER JOIN emr.m_allergy_type_master allergytyp3_ ON allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id "
				           + "LEFT OUTER JOIN emr.m_severity_master severityma4_ ON allergypat0_.severity_id = severityma4_.severity_id "
				           + "WHERE allergypat0_.patient_id =:patientId "
				           + "AND allergypat0_.organization_id =:organizationId "
				           + "AND allergypat0_.encounter_id =:encounterId "
				           + "AND (allergypat0_.resolved_remark IS NULL) "
				           + "AND allergypat0_.status = 'A'")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setParameter("encounterId", encounterId)
	            .setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class))
	            .list();
	}
	
	private List<AllergyPatientMapperDto> fetchAllergyDataAdmission(Integer admissionId, EncounterMasterDto encounterMasterDto) {
	    if (admissionId == null) return Collections.emptyList();
	    return sessionFactory.getCurrentSession()
	            .createSQLQuery("SELECT DISTINCT allergypat0_.allergy_id AS \"allergyId\", "
				           + "allergypat0_.patient_id AS \"patientId\", "
				           + "allergypat0_.generic_id AS \"genericId\" , "
				           + "allergypat0_.allergy_type_id AS \"allergyTypeId\", "
				           + "allergypat0_.remark AS \"remark\", "
				           + "allergymas1_.allergy_name AS \"allergyName\", "
				           + "allergymas1_.allergy_desc AS \"allergyDesc\", "
				           + "allergytyp3_.allergy_type_desc AS \"allergyTypeDesc\", "
				           + "allergypat0_.is_no_known_allergies AS \"isNoKnownAllergies\", "
				           + "generic2_.name AS \"genericName\" "
				           + "FROM patient.t_allergy_patient_mapper allergypat0_ "
				           + "LEFT OUTER JOIN emr.m_allergy_master allergymas1_ ON allergypat0_.allergy_id = allergymas1_.allergy_id "
				           + "LEFT OUTER JOIN public.m_generic generic2_ ON allergypat0_.generic_id = generic2_.id "
				           + "LEFT OUTER JOIN emr.m_allergy_type_master allergytyp3_ ON allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id "
				           + "LEFT OUTER JOIN emr.m_severity_master severityma4_ ON allergypat0_.severity_id = severityma4_.severity_id "
				           + "LEFT OUTER JOIN opd.t_encounter_master encounterm5_ ON allergypat0_.patient_id = encounterm5_.patient_id "
				           + "WHERE allergypat0_.patient_id =:patientId "
				           + "AND allergypat0_.organization_id =:organizationId "
				           + "AND allergypat0_.admission_id =:admissionId "
				           + "AND (allergypat0_.resolved_remark IS NULL) "
				           + "AND allergypat0_.status = 'A'")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
				.setParameter("admissionId", admissionId)
	            .setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class))
	            .list();
	}

	private List<ProcedureOrderMasterDto> fetchProcedureData(Integer encounterId, Integer admissionId) {
	    Response res = encounterId != null 
	        ? getProcedureOrderListByAdmissionIdForAbdm(0, encounterId, 0)
	        : getProcedureOrderListByAdmissionIdForAbdm(admissionId, 0, 0);
	    return res.getListObject();
	}


	private List<PatientMedicalHistoryDto> fetchMedicalHistory(Integer encounterId, Integer admissionId, EncounterMasterDto encounterMasterDto) {
		if(encounterId != null) {
			
			String query = "select " +
				    "patientmed0_.patient_medical_history_id as \"patientMedicalHistoryId\", " +
				    "patientmed0_.encounter_id as \"encounterId\", " +
				    "patientmed0_.diagnosis_id as \"diagnosisId\", " +
				    "patientmed0_.patient_id as \"patientId\", " +
				    "patientmed0_.since as \"since\", " +
				    "patientmed0_.duration as \"duration\"," +
				    "patientmed0_.remark as \"remark\", " +
				    "patientmed0_.cov_template_id as \"covTemplateId\", " +
				    "patientmed0_.is_no_significant_status as \"isNoSignificantStatus\", " +
				    "patientmed0_.unit_id as \"unitId\", " +
				    "patientmed0_.organization_id as \"organizationId\", " +
				    "patientmed0_.status as \"status\", " +
				    "to_char(patientmed0_.created_date, 'DD/MM/YYYY') as \"created_date\", " +
				    "patientmed0_.created_date as \"created_date_new\", " +
				    "to_char(patientmed0_.updated_date, 'DD/MM/YYYY') as \"updated_date\", " +
				    "diagnosism1_.code as \"code\", " +
				    "diagnosism1_.diagnosis_name as \"diagnosisName\", " +
				    "case " +
				    "when employeema3_.employee_id is not null then (employeema3_.first_name || ' ' || employeema3_.last_name) " +
				    "when doctormast4_.doctor_id is not null then ('Dr. ' || doctormast4_.first_name || ' ' || doctormast4_.last_name) " +
				    "else 'Admin' " +
				    "end as \"userName\", " +
				    "patientmed0_.is_enter_in_error as \"isEnterInError\", " +
				    "patientmed0_.is_reviewed_flag as \"isReviewedFlag\", " +
				    "patientmed0_.role_id as \"roleId\" "+
				    "from patient.t_patient_medical_history patientmed0_ " +
				    "left outer join emr.m_diagnosis_master diagnosism1_ on patientmed0_.diagnosis_id = diagnosism1_.diagnosis_id " +
				    "left outer join public.m_user_master usermaster2_ on patientmed0_.created_by = usermaster2_.user_id " +
				    "left outer join public.m_employee_master employeema3_ on usermaster2_.user_id = employeema3_.user_id " +
				    "left outer join doctor.m_doctor_master doctormast4_ on usermaster2_.user_id = doctormast4_.user_id " +
				    "where patientmed0_.patient_id = :patientId " +
				    "and patientmed0_.encounter_id =:encounterId " +
				    "and patientmed0_.status = 'A' " +
				    "and patientmed0_.organization_id = :organizationId " +
				    "order by patientmed0_.patient_medical_history_id asc";


			return sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("organizationId", encounterMasterDto.getOrganizationId())
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			
	    }
		
		if(admissionId != null) {
			
			
			String query = "select " +
				    "patientmed0_.patient_medical_history_id as \"patientMedicalHistoryId\", " +
				    "patientmed0_.encounter_id as \"encounterId\", " +
				    "patientmed0_.diagnosis_id as \"diagnosisId\", " +
				    "patientmed0_.patient_id as \"patientId\", " +
				    "patientmed0_.since as \"since\", " +
				    "patientmed0_.duration as \"duration\"," +
				    "patientmed0_.remark as \"remark\", " +
				    "patientmed0_.cov_template_id as \"covTemplateId\", " +
				    "patientmed0_.is_no_significant_status as \"isNoSignificantStatus\", " +
				    "patientmed0_.unit_id as \"unitId\", " +
				    "patientmed0_.organization_id as \"organizationId\", " +
				    "patientmed0_.status as \"status\", " +
				    "to_char(patientmed0_.created_date, 'DD/MM/YYYY') as \"created_date\", " +
				    "patientmed0_.created_date as \"created_date_new\", " +
				    "to_char(patientmed0_.updated_date, 'DD/MM/YYYY') as \"updated_date\", " +
				    "diagnosism1_.code as \"code\", " +
				    "diagnosism1_.diagnosis_name as \"diagnosisName\", " +
				    "case " +
				    "when employeema3_.employee_id is not null then (employeema3_.first_name || ' ' || employeema3_.last_name) " +
				    "when doctormast4_.doctor_id is not null then ('Dr. ' || doctormast4_.first_name || ' ' || doctormast4_.last_name) " +
				    "else 'Admin' " +
				    "end as \"userName\", " +
				    "patientmed0_.is_enter_in_error as \"isEnterInError\", " +
				    "patientmed0_.is_reviewed_flag as \"isReviewedFlag\", " +
				    "patientmed0_.role_id as \"roleId\" "+
				    "from patient.t_patient_medical_history patientmed0_ " +
				    "left outer join emr.m_diagnosis_master diagnosism1_ on patientmed0_.diagnosis_id = diagnosism1_.diagnosis_id " +
				    "left outer join public.m_user_master usermaster2_ on patientmed0_.created_by = usermaster2_.user_id " +
				    "left outer join public.m_employee_master employeema3_ on usermaster2_.user_id = employeema3_.user_id " +
				    "left outer join doctor.m_doctor_master doctormast4_ on usermaster2_.user_id = doctormast4_.user_id " +
				    "where patientmed0_.patient_id = :patientId " +
				    "and patientmed0_.admission_id =:admissionId " +
				    "and patientmed0_.status = 'A' " +
				    "and patientmed0_.organization_id = :organizationId " +
				    "order by patientmed0_.patient_medical_history_id asc";




			 return sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("organizationId", encounterMasterDto.getOrganizationId())
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
		}
		return null;
	}

	
	

	/*@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getLastEncounteredListOfPatientForAbdm(EncounterMasterDto encounterMasterDto)
			throws Exception {
		try {
			List<EncounterMasterDto> list2=new ArrayList<>();
			List<EncounterMasterDto> listEncounter =null;
			List<EncounterMasterDto> listAdmission =null;
			 List<EncounterMasterDto> list3 =new ArrayList<>();
			 
			 
			 
				List<EncounterMasterDto> careContextListNew =new ArrayList<>();
				// Create a set to track encounter numbers that have been added
				Set<String> encounterNumbers = new HashSet<>();

				for(EncounterMasterDto dtow : encounterMasterDto.getCareContextList()) 
				{
				
				    EncounterMasterDto dto = new EncounterMasterDto();
				    if (!encounterNumbers.contains(dtow.getEncounterNumber())) {
				        encounterNumbers.add(dtow.getEncounterNumber());
				        dto.setEncounterNumber(dtow.getEncounterNumber());
				        careContextListNew.add(dto);
				    }
				}
				
			 String number = null;
			for(EncounterMasterDto dtow : careContextListNew) 
			{
				
				if(dtow.getEncounterNumber() != null) {
					 number = dtow.getEncounterNumber();
				
			
				
				
				listEncounter=sessionFactory.getCurrentSession()
						.createSQLQuery("select " + 
								"	encounter.encounter_id as \"encounterId\", " + 
								"	encounter.dept_id as \"deptId\", " + 
								"	encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " + 
								"	to_char(encounter.encounter_date, 'HH24:MI:SS') as \"visitTime\", " + 
								"	to_char(encounter.encounter_date, 'yyyy-mm-dd') as \"encounterDateString\", " + 
								"   to_char(encounter.encounter_date, 'DD/MM/YYYY HH24:MI:SS') as \"encounterDateString2\", " + 
								"   'OP' as \"visitTypeName\", " + 
								"	encounter.encounter_number as \"encounterNumber\", " + 
								"	encounter.doctor_id as \"doctorId\", " + 
								"   doc.signture_img as \"signtureImg\", " +
								"	encounter.patient_class_id as \"patientClassId\", " + 
								"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
								"	encounter.dept_id as \"specialityId\", " + 
								"	speciality.speciality_name as \"specialityName\" " + 
								"from " + 
								"	opd.t_encounter_master encounter " + 
								"inner join doctor.m_doctor_master doc on " + 
								"	encounter.doctor_id = doc.doctor_id " + 
								"inner join doctor.m_speciality_master speciality on " + 
								"	encounter.dept_id = speciality.speciality_id " + 
								"where " + 
								"	encounter.organization_id = :organizationId  " + 
								"	and encounter.encounter_number = :number  " + 
								 " limit 5"
								)
								 .setParameter("organizationId", encounterMasterDto.getOrganizationId())
								    .setParameter("number", number)
								    .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
								    .list();
						
								//"union all " + 
				listAdmission = sessionFactory.getCurrentSession()
						.createSQLQuery(  "select " +
							    "    admission.admission_id as \"admissionId\", " +
							    "   admission.speciality_id as \"deptId\", " +  // Placeholder for deptId
							    "   admission.visit_type_id as \"userDefinedVisitTypeId\", " + // Placeholder for userDefinedVisitTypeId
							    "   NULL as \"visitTime\", " + // Placeholder for visitTime
							    "   to_char(admission.created_date, 'yyyy-mm-dd') as \"encounterDateString\", " + // Placeholder for encounterDateString
							    "   to_char(admission.created_date, 'DD/MM/YYYY HH24:MI:SS') as \"encounterDateString2\", " +
							    "   admission.admission_number as \"encounterNumber\", " +
							    "   'IP' as \"visitTypeName\", " + 
							    "   admission.doctor_id as \"doctorId\", " +
							    "   doc.signture_img as \"signtureImg\", " +
							    "   NULL as \"patientClassId\", " +  // Placeholder for patientClassId
							    "	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " +
							    "   NULL as \"specialityId\", " + // Placeholder for specialityId
							    "   NULL as \"specialityName\" " + // Placeholder for specialityName
							    "from " +
							    "   adt.t_admission admission " +
							    "inner join doctor.m_doctor_master doc on " + 
								"	admission.doctor_id = doc.doctor_id " + 
							    "where " +
							    "   admission.organization_id = :organizationId " +
							    "   and admission.admission_number = :number " +
							     " limit 5"
								)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
					    .setParameter("number", number)
					    .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
					    .list();
				
				list3.addAll(listEncounter);
				 
				 list3.addAll(listAdmission);
		
				}
			}
			
			for(EncounterMasterDto dto : list3) 
			{
				
			    	
			        String urlPath = dto.getSigntureImg();
                        if(urlPath != null){
                        	 URL url = new URL(urlPath);
     			            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     			            connection.setRequestMethod("GET");
     			            connection.connect();
     			            
     			            int responseCode = connection.getResponseCode();

     			            InputStream inputStream = connection.getInputStream();
     			            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     			            byte[] buffer = new byte[4096];
     			            int bytesRead;
     			            while ((bytesRead = inputStream.read(buffer)) != -1) {
     			                outputStream.write(buffer, 0, bytesRead);
     			            }
     			            byte[] fileContent = outputStream.toByteArray();

     			            inputStream.close();
     			            outputStream.close();
     			            
     			            System.out.println("Encoding to Base64...");
     			            String base64Encoded = Base64.getEncoder().encodeToString(fileContent);
     			
     			            dto.setSigntureImg(base64Encoded);
                        }
			           
                        List<ComplaintsDto> listComplaints = null;
                        List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto =null;
                        if(dto.getEncounterId() != null){
                        	
					    listComplaints=sessionFactory.getCurrentSession()
								.createSQLQuery("select " + 
										"	complaintAppointment.complaint_appo_id as \"complaintAppoId\", " +
										"	complaintAppointment.diagnosis_id as \"diagnosisId\", " + 
										"	complaintAppointment.encounter_id as \"encounterId\", " + 
										"	icpc1_.icpc_desc as \"diagnosisName\", " + 
										"	icpc1_.icpc_code as \"icpcCode\", " + 
										"	complaintAppointment.icpc_code_text as \"icpcCodeText\", " + 
										"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + 
										"from " + 
										"	patient.t_complaint_appo_mapper complaintAppointment " + 
										"inner join emr.m_icpc icpc1_ on " + 
										"	complaintAppointment.diagnosis_id = icpc1_.icpc_id " + 
										"left join emr.m_icpc_system icpcsystem2_ on " + 
										"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " + 
										"where " + 
										"	complaintAppointment.patient_id =:patientId " + 
										"	and complaintAppointment.status = 'A' " + 
										"	and complaintAppointment.encounter_id =:encounterId " + 
										"	and complaintAppointment.is_resolved = 'N' " +
										"	and complaintAppointment.organization_id =:organizationId " + 
										"	and(complaintAppointment.is_complaint_stop = 'N')")
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(ComplaintsDto.class)).list();
                        }
					
                        if(dto.getAdmisisonId() != null){
							listComplaints = sessionFactory.getCurrentSession()
									.createQuery(" SELECT comApp.complaintAdmissionId as complaintAdmissionId, comApp.admissionId as admissionId, comApp.diagnosisId as diagnosisId,"
											+ " comApp.resolvedRemark as resolvedRemark, to_char(comApp.resolvedDate, 'DD/MM/YYYY') as resolvedDate,"
											+ "(select max(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId "
											+ " and comApp.admissionId=tempObj.admissionId group by tempObj.mIcpcId ) as templateIcpcId, "
											+ "(select count(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId and tempObj.status = 'A'"
											+ " and comApp.admissionId=tempObj.admissionId group by tempObj.mIcpcId ) as complaintTempCount, "
											+ " (case " 
											+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
											+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
											+ " else 'Admin' end) as userName,"
										+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.remark as remark, icpcSystemMaster.icpcSystemDesc as icpcSystemDesc, "
										+ " comApp.createdDate as createdDateNew, "
										+ " to_char(comApp.createdDate, 'dd/mm/yyyy') as createdDate, to_char(comApp.createdDate, 'DD/MM/YYYY HH24:MI') as createdDate2, comApp.status as status, comApp.since as since, comApp.duration as duration,comApp.roleId as roleId,"
										+ " digMast.icpcSystemId as icpcSystemId ,comApp.icpcCodeText as icpcCodeText, "
										+ " comApp.isReviewedFlag as isReviewedFlag "
										+ " FROM ComplaintAdmission comApp "
										+ " INNER JOIN comApp.diagnosisMaster digMast "
										+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
						                + " LEFT JOIN comApp.userMaster um " 
									    + " LEFT JOIN um.employeeMastersList empmst " 
									    + " LEFT JOIN um.doctorMastersList  docmst "
										+ " WHERE comApp.admissionId =:admissionId AND comApp.status = 'A'"
										+ " AND comApp.isComplaintStop ='N'"
										+ " AND comApp.organizationId =:organizationId "
										+ " AND comApp.isResolved ='N' "
										+ " ORDER BY icpcSystemMaster.icpcSystemDesc asc")
									.setParameter("organizationId", encounterMasterDto.getOrganizationId())
									.setParameter("patientId", encounterMasterDto.getPatientId())
									.setParameter("admissionId", dto.getAdmisisonId())
									.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					}
						
					
					List<DiagnosisPatientAppointmentMapperDto> listDiagnosis =null;
					
					if(dto.getEncounterId() != null){
						listDiagnosis=sessionFactory.getCurrentSession()
								.createQuery( "SELECT "
									 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
									 	+ " diagMapper.admissionId AS admissionId, "
									 	+ " diagMapper.isDeleted AS isDeleted, "
									 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
									 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
									 	+ " diagMapper.patientId AS patientId, "
									 	+ " diagMapper.diagnosisText AS diagnosisText, "
								 		+ " diagMapper.diagnosisId AS diagnosisId, "
								 		+ " diagMapper.type AS type, "
								 		+ " diagMapper.diagnosisType AS diagnosisType, "
								 		+ " (case " 
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
										+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
										+ " else 'Admin' end) as userName,"
								 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
								 		+ " diagMapper.created_date as created_date_new, "
								 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
								 		+ " diagMapper.remark AS remark, "
								 		+ " diagMaster.isNotifiable AS isNotifiable , "
								 		+ " diagMaster.isComorbidity AS isComorbidity,"
								 		+ " diagMaster.code AS code,"
								 		+ " diagMaster.diagnosisName AS diagnosisName "
								+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
								+ " left JOIN diagMapper.diagnosisMaster diagMaster "
								+ " LEFT JOIN diagMapper.userMaster um " 
								+ " LEFT JOIN um.employeeMastersList empmst " 
								+ " LEFT JOIN um.doctorMastersList  docmst "
								+ " WHERE "
								+ " diagMapper.patientId=:patientId "
								+ " and diagMapper.encounterId=:encounterId "
								+ " and diagMapper.status='A'"
								+ " AND diagMapper.organizationId=:organizationId"
								+ " AND diagMapper.isDiagnosisCancel='N'"
								+ " AND diagMapper.isDeleted='N'"
								+ " AND diagMapper.isResolved='N'"
								+ " order by diagMapper.patientDiagnosisId desc")
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					}
				
					if(encounterMasterDto.getAdmisisonId() != null){
						listDiagnosis=sessionFactory.getCurrentSession()
								.createQuery( "SELECT "
									 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
									 	+ " diagMapper.admissionId AS admissionId, "
									 	+ " diagMapper.isDeleted AS isDeleted, "
									 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
									 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
									 	+ " diagMapper.patientId AS patientId, "
									 	+ " diagMapper.diagnosisText AS diagnosisText, "
								 		+ " diagMapper.diagnosisId AS diagnosisId, "
								 		+ " diagMapper.type AS type, "
								 		+ " diagMapper.diagnosisType AS diagnosisType, "
								 		+ " (case " 
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
										+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
										+ " else 'Admin' end) as userName,"
								 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
								 		+ " diagMapper.created_date as created_date_new, "
								 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
								 		+ " diagMapper.remark AS remark, "
								 		+ " diagMaster.isNotifiable AS isNotifiable , "
								 		+ " diagMaster.isComorbidity AS isComorbidity,"
								 		+ " diagMaster.code AS code,"
								 		+ " diagMaster.diagnosisName AS diagnosisName "
								+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
								+ " left JOIN diagMapper.diagnosisMaster diagMaster "
								+ " LEFT JOIN diagMapper.userMaster um " 
								+ " LEFT JOIN um.employeeMastersList empmst " 
								+ " LEFT JOIN um.doctorMastersList  docmst "
								+ " WHERE "
								+ " diagMapper.patientId=:patientId "
								+ " and diagMapper.admissionId=:admissionId "
								+ " and diagMapper.status='A'"
								+ " AND diagMapper.organizationId=:organizationId"
								+ " AND diagMapper.isDiagnosisCancel='N'"
								+ " AND diagMapper.isDeleted='N'"
								+ " AND diagMapper.isResolved='N'"
								+ " order by diagMapper.patientDiagnosisId desc")
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getAdmisisonId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					}
					
					
					if(listDiagnosis != null  && listDiagnosis.size()>0) {
						dto.setDiagnosisListData(listDiagnosis);
						}
					
					List<InvestigationOrderMasterNewDto> listlabOrder=null;
					
					if(dto.getEncounterId()!= null) {
					
					listlabOrder=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" 1 as \"visitTypeId\"," + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
									 " and ord_mst.patient_id =:patientId and ord_mst.encounter_id =:encounterId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
			}
					
					List<InvestigationOrderMasterNewDto> listlabOrderAdmission= null;
					if(dto.getAdmissionId()!= null) {
					 listlabOrderAdmission=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" 1 as \"visitTypeId\"," + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
									 " and ord_mst.patient_id =:patientId and ord_mst.admission_id =:admissionId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
					}
					
					List<CoversheetInstructionDto> listAdvice = null;
					
					if(dto.getEncounterId() !=null){
						listAdvice=sessionFactory.getCurrentSession()
								.createQuery("SELECT cover.instructionId as instructionId, "
										+ " cover.instructionDetailsId as instructionDetailsId, "
										+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
										+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
										+ " (case "
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
										+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
										+ " else 'Admin' end) as userName,"
										+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
										+ " FROM CoversheetInstruction cover "
										+ " LEFT JOIN cover.instructionMaster instr "
										+ " LEFT JOIN cover.userMaster userMaster "
										+ " LEFT JOIN userMaster.doctorMastersList doc "
										+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
										" where " + 
										"	cover.patientId =:patientId " + 
										"	and cover.status ='A'" +
										"	and cover.organizationId =:organizationId " +
										"	and cover.encounterId =:encounterId " 
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					}
				
					if(dto.getAdmisisonId() !=null){
						listAdvice=sessionFactory.getCurrentSession()
								.createQuery("SELECT cover.instructionId as instructionId, "
										+ " cover.instructionDetailsId as instructionDetailsId, "
										+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
										+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
										+ " (case "
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
										+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
										+ " else 'Admin' end) as userName,"
										+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
										+ " FROM CoversheetInstruction cover "
										+ " LEFT JOIN cover.instructionMaster instr "
										+ " LEFT JOIN cover.userMaster userMaster "
										+ " LEFT JOIN userMaster.doctorMastersList doc "
										+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
										" where " + 
										"	cover.patientId =:patientId " + 
										"	and cover.status ='A'" +
										"	and cover.organizationId =:organizationId " +
										"	and cover.admissionId =:admissionId " 
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					}
					
					
					
					List<MedicationDTO> listMedication=null;
					if(dto.getEncounterId() != null){
						
						 listMedication=sessionFactory.getCurrentSession()
								.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", " + 
										" pres.id as \"prescriptionId\", " + 
										" (case when presDtl.item_id is not null then itm.name " + 
										"	else presDtl.item_name end) as \"itemName\", " + 
										" preFreDtl.dose as \"doseDesc\","+
										" su.unit as \"strengthDesc\", " + 
										" preFreDtl.frequency_id as \"frequencyId\", " + 
										" freq.description as \"frequencyDesc\", " +
	                                     " freq.no_of_administration_time as \"noOfAdministrationTime\", " +
										" freq.code as \"frequencyCode\", " +
										" preFreDtl.route_id as \"roaId\", " + 
										" presDtl.order_qty as \"orderQty\", " +
										" presDtl.duration as \"duration\", " + 
										" presDtl.duration as \"durationCount\", " + 
										" presDtl.instruction_desc as \"instructionText\", " + 
										" presDtl.duration_unit as \"durationUnit\", " + 
										" preFreDtl.start_date as \"startDateNew\", " + 
										" preFreDtl.stop_date as \"endDateNew\", " + 
										" 1 as \"visitTypeId\", " + 
										" roa.description as \"roaDesc\", " + 
										" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
										" to_char(preFreDtl.start_date,'DD/MM/YYYY HH24:MI:SS') as \"startDate\"," + 
										" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
										" presDtl.instruction_id as \"instructionId\" ," + 
										" instr.instruction_desc as \"instructionDesc\", " + 
										" presDtl.is_medication_stop as \"isMedicationStop\","+
										" (case when presDtl.item_id is not null then gen.name \r\n" + 
										"	else presDtl.generic_name end) as \"genericName\" " + 
										" FROM pharmacy.t_prescription pres " + 
										" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
										" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
										" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
										" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
										" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
										" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
										" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
										" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
										" where " + 
										"	pres.patient_id =:patientId " + 
										"	and pres.is_active =true " + 
										"	and presDtl.is_active =true " + 
										"	and pres.encounter_id =:encounterId "
										+ " and pres.organization_id =:organizationId"
										+ " AND presDtl.is_medication_stop='N' " 
										+ " and presDtl.status_id != 9 "
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
						
					}
					
					
		if(dto.getAdmisisonId() != null){
						
						 listMedication=sessionFactory.getCurrentSession()
								.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", " + 
										" pres.id as \"prescriptionId\", " + 
										" (case when presDtl.item_id is not null then itm.name " + 
										"	else presDtl.item_name end) as \"itemName\", " + 
										" preFreDtl.dose as \"doseDesc\","+
										" su.unit as \"strengthDesc\", " + 
										" preFreDtl.frequency_id as \"frequencyId\", " + 
										" freq.description as \"frequencyDesc\", " +
	                                     " freq.no_of_administration_time as \"noOfAdministrationTime\", " +
										" freq.code as \"frequencyCode\", " +
										" preFreDtl.route_id as \"roaId\", " + 
										" presDtl.order_qty as \"orderQty\", " +
										" presDtl.duration as \"duration\", " + 
										" presDtl.duration as \"durationCount\", " + 
										" presDtl.instruction_desc as \"instructionText\", " + 
										" presDtl.duration_unit as \"durationUnit\", " + 
										" preFreDtl.start_date as \"startDateNew\", " + 
										" preFreDtl.stop_date as \"endDateNew\", " + 
										" 1 as \"visitTypeId\", " + 
										" roa.description as \"roaDesc\", " + 
										" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
										" to_char(preFreDtl.start_date,'YYYY-MM-DD') as \"startDate\"," + 
										" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
										" presDtl.instruction_id as \"instructionId\" ," + 
										" instr.instruction_desc as \"instructionDesc\", " + 
										" presDtl.is_medication_stop as \"isMedicationStop\","+
										" (case when presDtl.item_id is not null then gen.name \r\n" + 
										"	else presDtl.generic_name end) as \"genericName\" " + 
										" FROM pharmacy.t_prescription pres " + 
										" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
										" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
										" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
										" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
										" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
										" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
										" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
										" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
										" where " + 
										"	pres.patient_id =:patientId " + 
										"	and pres.is_active =true " + 
										"	and presDtl.is_active =true " + 
										"	and pres.admission_id =:admissionId "
										+ " and pres.organization_id =:organizationId"
										+ " AND presDtl.is_medication_stop='N' " 
										+ " and presDtl.status_id != 9 "
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
						
					}
					
					
					
		List<AllergyPatientMapperDto> allergyPatientMapperDtoList= null;
		if(dto.getEncounterId() != null){
					String allergyQuery = "SELECT DISTINCT allergypat0_.allergy_id AS \"allergyId\", "
					           + "allergypat0_.patient_id AS \"patientId\", "
					           + "allergypat0_.generic_id AS \"genericId\" , "
					           + "allergypat0_.allergy_type_id AS \"allergyTypeId\", "
					           + "allergypat0_.remark AS \"remark\", "
					           + "allergymas1_.allergy_name AS \"allergyName\", "
					           + "allergymas1_.allergy_desc AS \"allergyDesc\", "
					           + "allergytyp3_.allergy_type_desc AS \"allergyTypeDesc\", "
					           + "allergypat0_.is_no_known_allergies AS \"isNoKnownAllergies\", "
					           + "generic2_.name AS \"genericName\" "
					           + "FROM patient.t_allergy_patient_mapper allergypat0_ "
					           + "LEFT OUTER JOIN emr.m_allergy_master allergymas1_ ON allergypat0_.allergy_id = allergymas1_.allergy_id "
					           + "LEFT OUTER JOIN public.m_generic generic2_ ON allergypat0_.generic_id = generic2_.id "
					           + "LEFT OUTER JOIN emr.m_allergy_type_master allergytyp3_ ON allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id "
					           + "LEFT OUTER JOIN emr.m_severity_master severityma4_ ON allergypat0_.severity_id = severityma4_.severity_id "
					           + "WHERE allergypat0_.patient_id =:patientId "
					           + "AND allergypat0_.organization_id =:organizationId "
					           + "AND allergypat0_.encounter_id =:encounterId "
					           + "AND (allergypat0_.resolved_remark IS NULL) "
					           + "AND allergypat0_.status = 'A'";

					
					 allergyPatientMapperDtoList=sessionFactory.getCurrentSession()
							.createSQLQuery(allergyQuery).setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
					
		}
		
		
		
		if(dto.getAdmisisonId() != null){

			String allergyQuery = "SELECT DISTINCT allergypat0_.allergy_id AS \"allergyId\", "
			           + "allergypat0_.patient_id AS \"patientId\", "
			           + "allergypat0_.generic_id AS \"genericId\" , "
			           + "allergypat0_.allergy_type_id AS \"allergyTypeId\", "
			           + "allergypat0_.remark AS \"remark\", "
			           + "allergymas1_.allergy_name AS \"allergyName\", "
			           + "allergymas1_.allergy_desc AS \"allergyDesc\", "
			           + "allergytyp3_.allergy_type_desc AS \"allergyTypeDesc\", "
			           + "allergypat0_.is_no_known_allergies AS \"isNoKnownAllergies\", "
			           + "generic2_.name AS \"genericName\" "
			           + "FROM patient.t_allergy_patient_mapper allergypat0_ "
			           + "LEFT OUTER JOIN emr.m_allergy_master allergymas1_ ON allergypat0_.allergy_id = allergymas1_.allergy_id "
			           + "LEFT OUTER JOIN public.m_generic generic2_ ON allergypat0_.generic_id = generic2_.id "
			           + "LEFT OUTER JOIN emr.m_allergy_type_master allergytyp3_ ON allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id "
			           + "LEFT OUTER JOIN emr.m_severity_master severityma4_ ON allergypat0_.severity_id = severityma4_.severity_id "
			           + "LEFT OUTER JOIN opd.t_encounter_master encounterm5_ ON allergypat0_.patient_id = encounterm5_.patient_id "
			           + "WHERE allergypat0_.patient_id =:patientId "
			           + "AND allergypat0_.organization_id =:organizationId "
			           + "AND allergypat0_.admission_id =:admissionId "
			           + "AND (allergypat0_.resolved_remark IS NULL) "
			           + "AND allergypat0_.status = 'A'";

			
		 allergyPatientMapperDtoList=sessionFactory.getCurrentSession()
					.createSQLQuery(allergyQuery).setParameter("organizationId", encounterMasterDto.getOrganizationId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("admissionId", dto.getAdmisisonId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			

		}
					
					
				List<ProcedureOrderMasterDto> procedureList= null;
			    if(dto.getEncounterId()!= null) {
				    Response res = getProcedureOrderListByAdmissionIdForAbdm(0,dto.getEncounterId() , 0);
				    procedureList=res.getListObject();
			    }
				
				if(dto.getAdmissionId()!= null) {
			    	Response resProcedureListAdmission=	getProcedureOrderListByAdmissionIdForAbdm(dto.getAdmissionId(), 0, 0);
				    procedureList=resProcedureListAdmission.getListObject();
				}
				
				List<PatientMedicalHistoryDto> listPatientMedicalHistoryDto =null;
				
				if(dto.getEncounterId()!= null) {
					
					String query = "select " +
						    "patientmed0_.patient_medical_history_id as \"patientMedicalHistoryId\", " +
						    "patientmed0_.encounter_id as \"encounterId\", " +
						    "patientmed0_.diagnosis_id as \"diagnosisId\", " +
						    "patientmed0_.patient_id as \"patientId\", " +
						    "patientmed0_.since as \"since\", " +
						    "patientmed0_.duration as \"duration\"," +
						    "patientmed0_.remark as \"remark\", " +
						    "patientmed0_.cov_template_id as \"covTemplateId\", " +
						    "patientmed0_.is_no_significant_status as \"isNoSignificantStatus\", " +
						    "patientmed0_.unit_id as \"unitId\", " +
						    "patientmed0_.organization_id as \"organizationId\", " +
						    "patientmed0_.status as \"status\", " +
						    "to_char(patientmed0_.created_date, 'DD/MM/YYYY') as \"created_date\", " +
						    "patientmed0_.created_date as \"created_date_new\", " +
						    "to_char(patientmed0_.updated_date, 'DD/MM/YYYY') as \"updated_date\", " +
						    "diagnosism1_.code as \"code\", " +
						    "diagnosism1_.diagnosis_name as \"diagnosisName\", " +
						    "case " +
						    "when employeema3_.employee_id is not null then (employeema3_.first_name || ' ' || employeema3_.last_name) " +
						    "when doctormast4_.doctor_id is not null then ('Dr. ' || doctormast4_.first_name || ' ' || doctormast4_.last_name) " +
						    "else 'Admin' " +
						    "end as \"userName\", " +
						    "patientmed0_.is_enter_in_error as \"isEnterInError\", " +
						    "patientmed0_.is_reviewed_flag as \"isReviewedFlag\", " +
						    "patientmed0_.role_id as \"roleId\" "+
						    "from patient.t_patient_medical_history patientmed0_ " +
						    "left outer join emr.m_diagnosis_master diagnosism1_ on patientmed0_.diagnosis_id = diagnosism1_.diagnosis_id " +
						    "left outer join public.m_user_master usermaster2_ on patientmed0_.created_by = usermaster2_.user_id " +
						    "left outer join public.m_employee_master employeema3_ on usermaster2_.user_id = employeema3_.user_id " +
						    "left outer join doctor.m_doctor_master doctormast4_ on usermaster2_.user_id = doctormast4_.user_id " +
						    "where patientmed0_.patient_id = :patientId " +
						    "and patientmed0_.encounter_id =:encounterId " +
						    "and patientmed0_.status = 'A' " +
						    "and patientmed0_.organization_id = :organizationId " +
						    "order by patientmed0_.patient_medical_history_id asc";



					 listPatientMedicalHistoryDto=sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
					
			    }
				
				if(dto.getAdmissionId()!= null) {
					
					
					String query = "select " +
						    "patientmed0_.patient_medical_history_id as \"patientMedicalHistoryId\", " +
						    "patientmed0_.encounter_id as \"encounterId\", " +
						    "patientmed0_.diagnosis_id as \"diagnosisId\", " +
						    "patientmed0_.patient_id as \"patientId\", " +
						    "patientmed0_.since as \"since\", " +
						    "patientmed0_.duration as \"duration\"," +
						    "patientmed0_.remark as \"remark\", " +
						    "patientmed0_.cov_template_id as \"covTemplateId\", " +
						    "patientmed0_.is_no_significant_status as \"isNoSignificantStatus\", " +
						    "patientmed0_.unit_id as \"unitId\", " +
						    "patientmed0_.organization_id as \"organizationId\", " +
						    "patientmed0_.status as \"status\", " +
						    "to_char(patientmed0_.created_date, 'DD/MM/YYYY') as \"created_date\", " +
						    "patientmed0_.created_date as \"created_date_new\", " +
						    "to_char(patientmed0_.updated_date, 'DD/MM/YYYY') as \"updated_date\", " +
						    "diagnosism1_.code as \"code\", " +
						    "diagnosism1_.diagnosis_name as \"diagnosisName\", " +
						    "case " +
						    "when employeema3_.employee_id is not null then (employeema3_.first_name || ' ' || employeema3_.last_name) " +
						    "when doctormast4_.doctor_id is not null then ('Dr. ' || doctormast4_.first_name || ' ' || doctormast4_.last_name) " +
						    "else 'Admin' " +
						    "end as \"userName\", " +
						    "patientmed0_.is_enter_in_error as \"isEnterInError\", " +
						    "patientmed0_.is_reviewed_flag as \"isReviewedFlag\", " +
						    "patientmed0_.role_id as \"roleId\" "+
						    "from patient.t_patient_medical_history patientmed0_ " +
						    "left outer join emr.m_diagnosis_master diagnosism1_ on patientmed0_.diagnosis_id = diagnosism1_.diagnosis_id " +
						    "left outer join public.m_user_master usermaster2_ on patientmed0_.created_by = usermaster2_.user_id " +
						    "left outer join public.m_employee_master employeema3_ on usermaster2_.user_id = employeema3_.user_id " +
						    "left outer join doctor.m_doctor_master doctormast4_ on usermaster2_.user_id = doctormast4_.user_id " +
						    "where patientmed0_.patient_id = :patientId " +
						    "and patientmed0_.admission_id =:admissionId " +
						    "and patientmed0_.status = 'A' " +
						    "and patientmed0_.organization_id = :organizationId " +
						    "order by patientmed0_.patient_medical_history_id asc";



					 listPatientMedicalHistoryDto=sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("admissionId", dto.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
				}
				
				
				
				
				//Immunization
				List<VaccineImmunizationMapperDto> vacineList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VACCINE_IMMUNIZATION_MAPPER_ABDM")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class)).list();
				
				

				if(dto.getEncounterId() != null){
					Session statlessSession = sessionFactory.getCurrentSession();
					StringBuilder vitalsQuery = new StringBuilder(statlessSession
							.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM").getQueryString());
					listAppointmentVitalMapperDto = statlessSession.createQuery(vitalsQuery.toString())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
				}
				if(dto.getAdmisisonId() != null){
					Session statlessSession = sessionFactory.getCurrentSession();
					StringBuilder vitalsQuery = new StringBuilder(statlessSession
							.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM_ADMISSION").getQueryString());
					listAppointmentVitalMapperDto = statlessSession.createQuery(vitalsQuery.toString())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
				}
				
				
				
				
				 String habitQuery=sessionFactory.getCurrentSession().getNamedQuery(
				 "GET_CURRENT_PATIENT_HABIT_DETAILS_LIST").getQueryString(); StringBuffer
				 subQuery=new StringBuffer(); if(encounterMasterDto.getEncounterId()!=null){
				 subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC "); }
				 if(encounterMasterDto.getAdmissionId()!=null){
				 subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC "); }
				
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;
			habitQuery=habitQuery.concat(subQuery.toString());
				listPatientHabitDetailsDto=sessionFactory.getCurrentSession().createQuery(habitQuery)
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
				
				
				//Diet Advice
				StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_OBSERVATION_LIST_ABDM").getQueryString());
				if(encounterMasterDto.getAdmissionId()!=null && encounterMasterDto.getAdmissionId()!=0){
					stringQuery.append(" AND observation.admissionId="+encounterMasterDto.getAdmissionId());
				}else if(encounterMasterDto.getEncounterId()!=null && encounterMasterDto.getEncounterId()!=0){
					stringQuery.append(" AND observation.encounterId="+encounterMasterDto.getEncounterId());
				}
				List<ObservationDto> observationList=sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())			
						.setResultTransformer(Transformers.aliasToBean(ObservationDto.class)).list();
				
				    dto.setComplaintlistData(listComplaints);
					dto.setMedicationDataList(listMedication);
					if(listlabOrder != null && listlabOrder.size() >0){
						dto.setLabOrderList(listlabOrder);
					}else{
						dto.setLabOrderList(listlabOrderAdmission);
					}
					
					dto.setAdviceDataList(listAdvice);
					dto.setAllergyDataList(allergyPatientMapperDtoList);
					dto.setComplaintlistData(listComplaints);
					dto.setAdviceDataList(listAdvice);
					dto.setProcedureList(procedureList);
					dto.setListPatientMedicalHistoryDto(listPatientMedicalHistoryDto);
					dto.setVacineList(vacineList);
					dto.setListAppointmentVitalMapperDto(listAppointmentVitalMapperDto);
					dto.setListPatientHabitDetailsDto(listPatientHabitDetailsDto);
					dto.setObservationList(observationList);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list3, null);
	} catch (HibernateException he) {
		throw he ;
	} catch (Exception e) {
		throw e ;
	}
}*/

	private Response getProcedureOrderListByAdmissionIdForAbdm(int i, Integer encounterId, int j) {
		try
		{
			///String query=sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_PROCEDURE_ORDER_BY_ADMISSION_ID").getQueryString();
			String q1 = " select spl.speciality_name as \"specialityName\" , " + 
					"	spl.speciality_id as \"specialityId\"," + 
					"	pom.procedure_order_id as \"procedureOrderId\"," + 
					"	pom.service_id as \"serviceId\"," + 
					"	pom.urgency as \"urgency\"," + 
					"	service_standard_name \"procedureName\"," + 
					"	to_char(pom.prefered_date_time, 'dd-MM-yyyy') as \"preferedDateTime\"," + 
					"	to_char(pom.prefered_time, 'hh24:mi') as \"preferedTime\"," + 
					"	prm.pro_req_id as \"proReqId\"," + 
					"	prm.procedure_status_id as \"procedureStatusId\"," + 
					"	mpos.status as \"procedureStatus\"," + 
					"	pom.request_reason as \"requestReason\"," + 
					"	to_char(pom.prefered_date_time, 'yyyy') as \"year\"," + 
					"	to_char(pom.prefered_date_time, 'month') as \"month\"," + 
					"	to_char(pom.prefered_date_time, 'dd/MM/yyyy') as \"orderDateTime\"," + 
					"	pom.created_by as \"createdBy\"," + 
					"	to_char(pom.created_date, 'dd-MM-yyyy HH:MI:SS')as \"createdDate\"," +
					"	to_char(pom.created_date, 'DD/MM/YYYY')as \"createdDate1\"," +
					"	to_char(pom.updated_date, 'dd-MM-yyyy HH:MI:SS')as \"updatedDate\"," + 
					"	pom.updated_by as \"updatedBy\"," + 
					"	pom.amount as \"amount\"," + 
					"	cast(pom.is_icu_required as char(1))  as \"isIcuRequired\"," + 
					"	cast(pom.is_primary_secondary as char(1)) as \"isPrimarySecondary\"," + 
					"	cast(pom.is_blood_component_required as char(1)) as \"isBloodComponentRequired\"," + 
					"	prm.org_id as \"orgId\"," + 
					"	cast(rtrim(pom.status) as varchar(10)) as \"status\"," + 
					"	prm.unit_id as \"unitId\"," + 
					"	cast(pom.is_pac_required as char(1)) as \"isPacRequired\"," + 
					" (case " + 
					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
					" else 'Admin' end) as \"userName\", "+
					"	dtls.procedure_type_id as \"procedureTypeId\","
					+" config.operation_theatre_type_id as \"operationTheatreTypeId\","
					+ " pom.procedure_sequence AS \"procedureSequence\"  " + 
					" from otims.t_procedure_req_mst prm " + 
					" inner join otims.t_procedure_order_master pom on " + 
					"	prm.pro_req_id = pom.pro_req_id" + 
                    " inner join otims.m_procedure_order_status mpos on " + 
                    " prm.procedure_status_id = mpos.id" + 
					" inner join service.m_service_master ser on " + 
					"	ser.service_master_id = pom.service_id " + 
					" inner join doctor.m_speciality_master spl on" + 
					"	spl.speciality_id = ser.speciality_id " + 
					" inner join service.t_unit_service_master_details dtls on" + 
					"	dtls.service_master_id = pom.service_id	" + 
					" left join public.m_user_master um on prm.created_by = um.user_id "+ 
					" left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
					" left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "+
					" inner join otims.t_procedure_configuration config on config.service_id = pom.service_id "+
					" where pom.status = 'A'  "
					+ " and dtls.procedure_type_id  is not null ";
			
			String q2 =" select distinct spl.speciality_name as \"specialityName\" ," + 
					"	spl.speciality_id as \"specialityId\"," + 
					"	0 as \"procedureOrderId\"," + 
					"	tod.service_id as \"serviceId\"," + 
					"	0 as \"urgency\"," + 
					"	service_standard_name \"procedureName\"," + 
					"	to_char(tod.order_date, 'dd-MM-yyyy') as \"preferedDateTime\"," + 
					"	to_char(tod.order_date, 'hh24:mi') as \"preferedTime\"," + 
					"	0 as \"proReqId\"," + 
					"	0 as \"procedureStatusId\"," + 
					"   '-' as \"procedureStatus\"," +
					"	'-' as \"requestReason\"," + 
					"	to_char(tod.order_date, 'yyyy') as \"year\"," + 
					"	to_char(tod.order_date, 'month') as \"month\"," + 
					"	to_char(tod.order_date, 'dd/MM/yyyy') as \"orderDateTime\"," + 
					"	tod.created_by as \"createdBy\"," + 
					"	to_char(tod.created_date, 'dd-MM-yyyy HH:MI:SS')as \"createdDate\"," +
					"	to_char(tod.created_date, 'DD/MM/YYYY')as \"createdDate1\"," +
					"	to_char(tod.updated_date, 'dd-MM-yyyy HH:MI:SS')as \"updatedDate\"," + 
					"	tod.updated_by as \"updatedBy\"," + 
					"	0 as \"amount\"," + 
					"	cast('N' as char(1)) as \"isIcuRequired\"," + 
					"	cast('N' as char(1)) as \"isPrimarySecondary\"," + 
					"	cast('N' as char(1)) as \"isBloodComponentRequired\"," + 
					"	tod.org_id as \"orgId\"," + 
					"	cast(rtrim(tod.status) as varchar(10)) as \"status\"," + 
					"	tod.org_unit_id as \"unitId\"," + 
					"	cast('N' as char(1)) as \"isPacRequired\"," + 
					" (case " + 
					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
					" else 'Admin' end) as \"userName\", "+
					"	dtls.procedure_type_id as \"procedureTypeId\", " +
					" config.operation_theatre_type_id as \"operationTheatreTypeId\","+
					" 0 AS \"procedureSequence\"  " + 
					" from  " + 
					"	t_order_details tod " + 
					" inner join t_order_master om on" + 
					"	om.order_id = tod.order_id " + 
					" inner join service.m_service_master ser on " + 
					"	ser.service_master_id = tod.service_id " + 
					" inner join doctor.m_speciality_master spl on " + 
					"	spl.speciality_id = ser.speciality_id "
					+ "	inner join public.t_unit_speciality_mapper unitspc on " + 
					"	unitspc.speciality_id = spl.speciality_id " + 
					" inner join service.t_unit_service_master_details dtls on " + 
					"	dtls.service_master_id = tod.service_id " + 
					" left join public.m_user_master um on tod.created_by = um.user_id "+ 
					" left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
					" left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "+
					" inner join otims.t_procedure_configuration config on config.service_id = tod.service_id "+
					" where " + 
					"	tod.status = 'A' "
					+ " and dtls.procedure_type_id  is not null " + 
					"	and dtls.procedure_type_id in(1,2) "
					+ " and unitspc.is_surgical_code = 'Y'  " + 
					"	and dtls.unit_id = tod.org_unit_id ";
			
			
			StringBuffer subQuery = new StringBuffer();
			StringBuffer subQuery1 = new StringBuffer();
			int count = 0;
			if(i!=0){
				subQuery.append(" and prm.admission_id="+i);
				subQuery1.append(" and om.admission_id="+i);
				count = 1 ; 
			}
			if(encounterId!=0){
				subQuery.append(" and prm.encounter_id="+encounterId);
				subQuery1.append(" and om.encounter_id="+encounterId);
				count = 1 ; 
			}
			if(j!=0){
				subQuery.append(" and prm.pro_req_id="+j);
			}
			
			q1 = q1.concat(subQuery.toString());
			q2 = q2.concat(subQuery1.toString());
			String query = "";
			if(count == 1) {
				query = q1+ " union "+q2;	 
			}else {
				query = q1;
			}
			List<ProcedureOrderMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			

		} catch (Exception e)
		{
		logger.error("getProcedureOrderListByAdmissionIdForAbdm failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}



			

	private List<VaccineImmunizationMapperDto> fetchVaccines(EncounterMasterDto encounterMasterDto) {
	    return sessionFactory.getCurrentSession()
	            .getNamedQuery("GET_VACCINE_IMMUNIZATION_MAPPER_ABDM")
	            .setParameter("organizationId", encounterMasterDto.getOrganizationId())
	            .setParameter("patientId", encounterMasterDto.getPatientId())
	            .setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class))
	            .list();
	}

	private List<AppointmentVitalMapperDto> fetchVitals(Integer encounterId, Integer admissionId, EncounterMasterDto encounterMasterDto) {
		
		if(encounterId  != null){
			Session statlessSession = sessionFactory.getCurrentSession();
			StringBuilder vitalsQuery = new StringBuilder(statlessSession
					.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM").getQueryString());
			return statlessSession.createQuery(vitalsQuery.toString())
					.setParameter("organizationId", encounterMasterDto.getOrganizationId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("encounterId",encounterId )
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
		}
		if(admissionId != null){
			Session statlessSession = sessionFactory.getCurrentSession();
			StringBuilder vitalsQuery = new StringBuilder(statlessSession
					.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM_ADMISSION").getQueryString());
			return statlessSession.createQuery(vitalsQuery.toString())
					.setParameter("organizationId", encounterMasterDto.getOrganizationId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
		}
		return null;
		
	    
	}

	private List<PatientHabitDetailsDto> fetchPatientHabits( EncounterMasterDto encounterMasterDto) {
		String habitQuery=sessionFactory.getCurrentSession().getNamedQuery(
				 "GET_CURRENT_PATIENT_HABIT_DETAILS_LIST").getQueryString(); StringBuffer
				 subQuery=new StringBuffer();
				 subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC "); 
				
			habitQuery=habitQuery.concat(subQuery.toString());
				return sessionFactory.getCurrentSession().createQuery(habitQuery)
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
	}

	private List<ObservationDto> fetchObservations(Integer encounterId, Integer admissionId, EncounterMasterDto encounterMasterDto) {//Diet Advice
		
		StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_OBSERVATION_LIST_ABDM").getQueryString());
	
		if(admissionId !=null && admissionId !=0){
			stringQuery.append(" AND observation.admissionId="+admissionId);
		}else if(encounterId!=null && encounterId!=0){
			stringQuery.append(" AND observation.encounterId="+encounterId);
		}
		
		return sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
				.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
				.setParameter("patientId", encounterMasterDto.getPatientId())			
				.setResultTransformer(Transformers.aliasToBean(ObservationDto.class)).list();
		}

	

	/*@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getLastEncounteredListOfPatientForAbdm(EncounterMasterDto encounterMasterDto)
			throws Exception {
		try {
			List<EncounterMasterDto> list2=new ArrayList<>();
			List<EncounterMasterDto> listEncounter =null;
			List<EncounterMasterDto> listAdmission =null;
			 List<EncounterMasterDto> list3 =new ArrayList<>();
			 
			 
			 
				List<EncounterMasterDto> careContextListNew =new ArrayList<>();
				// Create a set to track encounter numbers that have been added
				Set<String> encounterNumbers = new HashSet<>();

				for(EncounterMasterDto dtow : encounterMasterDto.getCareContextList()) 
				{
				
				    EncounterMasterDto dto = new EncounterMasterDto();
				    if (!encounterNumbers.contains(dtow.getEncounterNumber())) {
				        encounterNumbers.add(dtow.getEncounterNumber());
				        dto.setEncounterNumber(dtow.getEncounterNumber());
				        careContextListNew.add(dto);
				    }
				}
				
			 String number = null;
			for(EncounterMasterDto dtow : careContextListNew) 
			{
				
				if(dtow.getEncounterNumber() != null) {
					 number = dtow.getEncounterNumber();
				
			
				
				
				listEncounter=sessionFactory.getCurrentSession()
						.createSQLQuery("select " + 
								"	encounter.encounter_id as \"encounterId\", " + 
								"	encounter.dept_id as \"deptId\", " + 
								"	encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " + 
								"	to_char(encounter.encounter_date, 'HH24:MI:SS') as \"visitTime\", " + 
								"	to_char(encounter.encounter_date, 'yyyy-mm-dd') as \"encounterDateString\", " + 
								"   to_char(encounter.encounter_date, 'DD/MM/YYYY HH24:MI:SS') as \"encounterDateString2\", " + 
								"   'OP' as \"visitTypeName\", " + 
								"	encounter.encounter_number as \"encounterNumber\", " + 
								"	encounter.doctor_id as \"doctorId\", " + 
								"   doc.signture_img as \"signtureImg\", " +
								"	encounter.patient_class_id as \"patientClassId\", " + 
								"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
								"	encounter.dept_id as \"specialityId\", " + 
								"	speciality.speciality_name as \"specialityName\" " + 
								"from " + 
								"	opd.t_encounter_master encounter " + 
								"inner join doctor.m_doctor_master doc on " + 
								"	encounter.doctor_id = doc.doctor_id " + 
								"inner join doctor.m_speciality_master speciality on " + 
								"	encounter.dept_id = speciality.speciality_id " + 
								"where " + 
								"	encounter.organization_id = :organizationId  " + 
								"	and encounter.encounter_number = :number  " + 
								 " limit 5"
								)
								 .setParameter("organizationId", encounterMasterDto.getOrganizationId())
								    .setParameter("number", number)
								    .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
								    .list();
						
								//"union all " + 
				listAdmission = sessionFactory.getCurrentSession()
						.createSQLQuery(  "select " +
							    "    admission.admission_id as \"admissionId\", " +
							    "   admission.speciality_id as \"deptId\", " +  // Placeholder for deptId
							    "   admission.visit_type_id as \"userDefinedVisitTypeId\", " + // Placeholder for userDefinedVisitTypeId
							    "   NULL as \"visitTime\", " + // Placeholder for visitTime
							    "   to_char(admission.created_date, 'yyyy-mm-dd') as \"encounterDateString\", " + // Placeholder for encounterDateString
							    "   to_char(admission.created_date, 'DD/MM/YYYY HH24:MI:SS') as \"encounterDateString2\", " +
							    "   admission.admission_number as \"encounterNumber\", " +
							    "   'IP' as \"visitTypeName\", " + 
							    "   admission.doctor_id as \"doctorId\", " +
							    "   doc.signture_img as \"signtureImg\", " +
							    "   NULL as \"patientClassId\", " +  // Placeholder for patientClassId
							    "	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " +
							    "   NULL as \"specialityId\", " + // Placeholder for specialityId
							    "   NULL as \"specialityName\" " + // Placeholder for specialityName
							    "from " +
							    "   adt.t_admission admission " +
							    "inner join doctor.m_doctor_master doc on " + 
								"	admission.doctor_id = doc.doctor_id " + 
							    "where " +
							    "   admission.organization_id = :organizationId " +
							    "   and admission.admission_number = :number " +
							     " limit 5"
								)
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
					    .setParameter("number", number)
					    .setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
					    .list();
				
				list3.addAll(listEncounter);
				 
				 list3.addAll(listAdmission);
		
				}
			}
			
			for(EncounterMasterDto dto : list3) 
			{
				
			    	
			        String urlPath = dto.getSigntureImg();
                        if(urlPath != null){
                        	 URL url = new URL(urlPath);
     			            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     			            connection.setRequestMethod("GET");
     			            connection.connect();
     			            
     			            int responseCode = connection.getResponseCode();

     			            InputStream inputStream = connection.getInputStream();
     			            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     			            byte[] buffer = new byte[4096];
     			            int bytesRead;
     			            while ((bytesRead = inputStream.read(buffer)) != -1) {
     			                outputStream.write(buffer, 0, bytesRead);
     			            }
     			            byte[] fileContent = outputStream.toByteArray();

     			            inputStream.close();
     			            outputStream.close();
     			            
     			            System.out.println("Encoding to Base64...");
     			            String base64Encoded = Base64.getEncoder().encodeToString(fileContent);
     			
     			            dto.setSigntureImg(base64Encoded);
                        }
			           
                        List<ComplaintsDto> listComplaints = null;
                        List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto =null;
                        if(dto.getEncounterId() != null){
                        	
					    listComplaints=sessionFactory.getCurrentSession()
								.createSQLQuery("select " + 
										"	complaintAppointment.complaint_appo_id as \"complaintAppoId\", " +
										"	complaintAppointment.diagnosis_id as \"diagnosisId\", " + 
										"	complaintAppointment.encounter_id as \"encounterId\", " + 
										"	icpc1_.icpc_desc as \"diagnosisName\", " + 
										"	icpc1_.icpc_code as \"icpcCode\", " + 
										"	complaintAppointment.icpc_code_text as \"icpcCodeText\", " + 
										"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + 
										"from " + 
										"	patient.t_complaint_appo_mapper complaintAppointment " + 
										"inner join emr.m_icpc icpc1_ on " + 
										"	complaintAppointment.diagnosis_id = icpc1_.icpc_id " + 
										"left join emr.m_icpc_system icpcsystem2_ on " + 
										"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " + 
										"where " + 
										"	complaintAppointment.patient_id =:patientId " + 
										"	and complaintAppointment.status = 'A' " + 
										"	and complaintAppointment.encounter_id =:encounterId " + 
										"	and complaintAppointment.is_resolved = 'N' " +
										"	and complaintAppointment.organization_id =:organizationId " + 
										"	and(complaintAppointment.is_complaint_stop = 'N')")
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(ComplaintsDto.class)).list();
                        }
					
                        if(dto.getAdmisisonId() != null){
							listComplaints = sessionFactory.getCurrentSession()
									.createQuery(" SELECT comApp.complaintAdmissionId as complaintAdmissionId, comApp.admissionId as admissionId, comApp.diagnosisId as diagnosisId,"
											+ " comApp.resolvedRemark as resolvedRemark, to_char(comApp.resolvedDate, 'DD/MM/YYYY') as resolvedDate,"
											+ "(select max(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId "
											+ " and comApp.admissionId=tempObj.admissionId group by tempObj.mIcpcId ) as templateIcpcId, "
											+ "(select count(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId and tempObj.status = 'A'"
											+ " and comApp.admissionId=tempObj.admissionId group by tempObj.mIcpcId ) as complaintTempCount, "
											+ " (case " 
											+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
											+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
											+ " else 'Admin' end) as userName,"
										+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.remark as remark, icpcSystemMaster.icpcSystemDesc as icpcSystemDesc, "
										+ " comApp.createdDate as createdDateNew, "
										+ " to_char(comApp.createdDate, 'dd/mm/yyyy') as createdDate, to_char(comApp.createdDate, 'DD/MM/YYYY HH24:MI') as createdDate2, comApp.status as status, comApp.since as since, comApp.duration as duration,comApp.roleId as roleId,"
										+ " digMast.icpcSystemId as icpcSystemId ,comApp.icpcCodeText as icpcCodeText, "
										+ " comApp.isReviewedFlag as isReviewedFlag "
										+ " FROM ComplaintAdmission comApp "
										+ " INNER JOIN comApp.diagnosisMaster digMast "
										+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
						                + " LEFT JOIN comApp.userMaster um " 
									    + " LEFT JOIN um.employeeMastersList empmst " 
									    + " LEFT JOIN um.doctorMastersList  docmst "
										+ " WHERE comApp.admissionId =:admissionId AND comApp.status = 'A'"
										+ " AND comApp.isComplaintStop ='N'"
										+ " AND comApp.organizationId =:organizationId "
										+ " AND comApp.isResolved ='N' "
										+ " ORDER BY icpcSystemMaster.icpcSystemDesc asc")
									.setParameter("organizationId", encounterMasterDto.getOrganizationId())
									.setParameter("patientId", encounterMasterDto.getPatientId())
									.setParameter("admissionId", dto.getAdmisisonId())
									.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					}
						
					
					List<DiagnosisPatientAppointmentMapperDto> listDiagnosis =null;
					
					if(dto.getEncounterId() != null){
						listDiagnosis=sessionFactory.getCurrentSession()
								.createQuery( "SELECT "
									 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
									 	+ " diagMapper.admissionId AS admissionId, "
									 	+ " diagMapper.isDeleted AS isDeleted, "
									 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
									 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
									 	+ " diagMapper.patientId AS patientId, "
									 	+ " diagMapper.diagnosisText AS diagnosisText, "
								 		+ " diagMapper.diagnosisId AS diagnosisId, "
								 		+ " diagMapper.type AS type, "
								 		+ " diagMapper.diagnosisType AS diagnosisType, "
								 		+ " (case " 
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
										+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
										+ " else 'Admin' end) as userName,"
								 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
								 		+ " diagMapper.created_date as created_date_new, "
								 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
								 		+ " diagMapper.remark AS remark, "
								 		+ " diagMaster.isNotifiable AS isNotifiable , "
								 		+ " diagMaster.isComorbidity AS isComorbidity,"
								 		+ " diagMaster.code AS code,"
								 		+ " diagMaster.diagnosisName AS diagnosisName "
								+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
								+ " left JOIN diagMapper.diagnosisMaster diagMaster "
								+ " LEFT JOIN diagMapper.userMaster um " 
								+ " LEFT JOIN um.employeeMastersList empmst " 
								+ " LEFT JOIN um.doctorMastersList  docmst "
								+ " WHERE "
								+ " diagMapper.patientId=:patientId "
								+ " and diagMapper.encounterId=:encounterId "
								+ " and diagMapper.status='A'"
								+ " AND diagMapper.organizationId=:organizationId"
								+ " AND diagMapper.isDiagnosisCancel='N'"
								+ " AND diagMapper.isDeleted='N'"
								+ " AND diagMapper.isResolved='N'"
								+ " order by diagMapper.patientDiagnosisId desc")
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					}
				
					if(encounterMasterDto.getAdmisisonId() != null){
						listDiagnosis=sessionFactory.getCurrentSession()
								.createQuery( "SELECT "
									 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
									 	+ " diagMapper.admissionId AS admissionId, "
									 	+ " diagMapper.isDeleted AS isDeleted, "
									 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
									 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
									 	+ " diagMapper.patientId AS patientId, "
									 	+ " diagMapper.diagnosisText AS diagnosisText, "
								 		+ " diagMapper.diagnosisId AS diagnosisId, "
								 		+ " diagMapper.type AS type, "
								 		+ " diagMapper.diagnosisType AS diagnosisType, "
								 		+ " (case " 
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
										+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
										+ " else 'Admin' end) as userName,"
								 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
								 		+ " diagMapper.created_date as created_date_new, "
								 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
								 		+ " diagMapper.remark AS remark, "
								 		+ " diagMaster.isNotifiable AS isNotifiable , "
								 		+ " diagMaster.isComorbidity AS isComorbidity,"
								 		+ " diagMaster.code AS code,"
								 		+ " diagMaster.diagnosisName AS diagnosisName "
								+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
								+ " left JOIN diagMapper.diagnosisMaster diagMaster "
								+ " LEFT JOIN diagMapper.userMaster um " 
								+ " LEFT JOIN um.employeeMastersList empmst " 
								+ " LEFT JOIN um.doctorMastersList  docmst "
								+ " WHERE "
								+ " diagMapper.patientId=:patientId "
								+ " and diagMapper.admissionId=:admissionId "
								+ " and diagMapper.status='A'"
								+ " AND diagMapper.organizationId=:organizationId"
								+ " AND diagMapper.isDiagnosisCancel='N'"
								+ " AND diagMapper.isDeleted='N'"
								+ " AND diagMapper.isResolved='N'"
								+ " order by diagMapper.patientDiagnosisId desc")
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getAdmisisonId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
					}
					
					
					if(listDiagnosis != null  && listDiagnosis.size()>0) {
						dto.setDiagnosisListData(listDiagnosis);
						}
					
					List<InvestigationOrderMasterNewDto> listlabOrder=null;
					
					if(dto.getEncounterId()!= null) {
					
					listlabOrder=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" 1 as \"visitTypeId\"," + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
									 " and ord_mst.patient_id =:patientId and ord_mst.encounter_id =:encounterId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
			}
					
					List<InvestigationOrderMasterNewDto> listlabOrderAdmission= null;
					if(dto.getAdmissionId()!= null) {
					 listlabOrderAdmission=sessionFactory.getCurrentSession()
							.createSQLQuery("select "  + 
									" ord_mst.order_id as \"orderId\", " + 
									" 1 as \"visitTypeId\"," + 
									" service_mst.speciality_id as \"specialityId\"," + 
									" service_mst.sub_speciality_id as \"subDeptId\"," + 
									" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
									"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
									" ord_mst.admission_id as \"admissionId\", " + 
									" ord_mst.encounter_id as \"encounterId\", " + 
									" ord_mst.patient_id as \"patientId\", " + 
									" ord_dtls.order_details_id as \"orderDetailsId\", " + 
									" ord_dtls.service_id as \"serviceMasterId\", " + 
									" ord_dtls.order_set_id as \"orderSetId\", " + 
									" ord_dtls.priority_id as \"priorityId\", " + 
									" ord_dtls.ord_remarks as \"ordRemarks\", " + 
									" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
									" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
									" service_mst.service_standard_name as \"serviceStandardName\", " + 
									" service_mst.service_standard_code as \"serviceStandardCode\", " + 
									" speciality_mst.speciality_name as \"specialityName\", " + 
									" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
									" service_dtls.is_panel as \"isPanel\", " + 
									" lab_res.lab_test_res_id  as \"labTestResId\", " + 
									" cast( " + 
									" 	null as integer " + 
									" ) as \"panelId\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
									" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
									" to_char( " + 
									" 	ord_dtls.created_date, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"createdDate\", " + 
									" to_char( " + 
									" 	sample_dtls.sample_gen_datetime, " + 
									" 	'dd/MM/yyyy HH24:MI' " + 
									" ) as \"sampleGenDatetime\", " + 
									" 	ord_dtls.created_date as \"createdDateNew\", " + 
									" ord_dtls.service_rendered as \"serviceRendered\", " + 
									" ord_dtls.is_package_service as \"isPackageService\", " + 
									" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
									" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
									" ord_dtls.status as \"orderStatus\", " + 
									 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
									 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
									 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
									 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
									 + " else 'Super Admin' end) as \"collectedUserName\","
									 
									+" (case " + 
									" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
									" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
									" else 'Admin' end) as \"userName\"," + 
									" ord_dtls.package_id as \"packageId\" " + 
									" from 	public.t_order_master ord_mst " + 
									" 		inner join public.t_order_details ord_dtls on " + 
									" ord_mst.order_id = ord_dtls.order_id " + 
									" 		inner join service.t_unit_service_mapper service_mppr on " + 
									" service_mppr.service_id = ord_dtls.service_id " + 
									" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.t_unit_service_master_details service_dtls on " + 
									" service_dtls.service_master_id = service_mppr.service_id " + 
									" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
									" 		inner join service.m_service_master service_mst on " + 
									" service_mst.service_master_id = ord_dtls.service_id " + 
									" 		left join doctor.m_speciality_master speciality_mst on " + 
									" speciality_mst.speciality_id = service_mst.speciality_id " + 
									" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
									" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
									" and sample_dtls.sample_status_id <> 4 " + 
									" 		left join lab.m_test_status_master test_status on " + 
									" test_status.test_status_id = sample_dtls.sample_status_id " + 
									"        	left join lab.m_test_status_master test_status2 on " + 
									" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
									" 		left join lab.t_lab_test_result lab_res on " + 
									" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
									" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
									" 		left outer join public.t_template_transaction templatetr12_ on " + 
									" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
									" and( " + 
									" 	templatetr12_.status = 'A' " + 
									" ) " + 
									" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
									" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
									" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
									 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
						    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
									" 		where " + 
									" service_mst.status = 'A' " + 
									" and ord_dtls.status = 'A' " + 
									" and ord_dtls.old_ord_dtl_id is null " + 
									" and service_dtls.is_package = 'I' " + 
									" and ord_dtls.nursing_service_order_flag = 'N' " + 
									" and service_dtls.is_panel = 'N' "+
									 " and ord_mst.patient_id =:patientId and ord_mst.admission_id =:admissionId "  
									+ " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper "
									 + " where organization_id =:organizationId"
									+" and is_rendering_speciality = 'Y' ) " )
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
					}
					
					List<CoversheetInstructionDto> listAdvice = null;
					
					if(dto.getEncounterId() !=null){
						listAdvice=sessionFactory.getCurrentSession()
								.createQuery("SELECT cover.instructionId as instructionId, "
										+ " cover.instructionDetailsId as instructionDetailsId, "
										+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
										+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
										+ " (case "
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
										+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
										+ " else 'Admin' end) as userName,"
										+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
										+ " FROM CoversheetInstruction cover "
										+ " LEFT JOIN cover.instructionMaster instr "
										+ " LEFT JOIN cover.userMaster userMaster "
										+ " LEFT JOIN userMaster.doctorMastersList doc "
										+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
										" where " + 
										"	cover.patientId =:patientId " + 
										"	and cover.status ='A'" +
										"	and cover.organizationId =:organizationId " +
										"	and cover.encounterId =:encounterId " 
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					}
				
					if(dto.getAdmisisonId() !=null){
						listAdvice=sessionFactory.getCurrentSession()
								.createQuery("SELECT cover.instructionId as instructionId, "
										+ " cover.instructionDetailsId as instructionDetailsId, "
										+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
										+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
										+ " (case "
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
										+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
										+ " else 'Admin' end) as userName,"
										+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
										+ " FROM CoversheetInstruction cover "
										+ " LEFT JOIN cover.instructionMaster instr "
										+ " LEFT JOIN cover.userMaster userMaster "
										+ " LEFT JOIN userMaster.doctorMastersList doc "
										+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
										" where " + 
										"	cover.patientId =:patientId " + 
										"	and cover.status ='A'" +
										"	and cover.organizationId =:organizationId " +
										"	and cover.admissionId =:admissionId " 
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
					}
					
					
					
					List<MedicationDTO> listMedication=null;
					if(dto.getEncounterId() != null){
						
						 listMedication=sessionFactory.getCurrentSession()
								.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", " + 
										" pres.id as \"prescriptionId\", " + 
										" (case when presDtl.item_id is not null then itm.name " + 
										"	else presDtl.item_name end) as \"itemName\", " + 
										" preFreDtl.dose as \"doseDesc\","+
										" su.unit as \"strengthDesc\", " + 
										" preFreDtl.frequency_id as \"frequencyId\", " + 
										" freq.description as \"frequencyDesc\", " +
	                                     " freq.no_of_administration_time as \"noOfAdministrationTime\", " +
										" freq.code as \"frequencyCode\", " +
										" preFreDtl.route_id as \"roaId\", " + 
										" presDtl.order_qty as \"orderQty\", " +
										" presDtl.duration as \"duration\", " + 
										" presDtl.duration as \"durationCount\", " + 
										" presDtl.instruction_desc as \"instructionText\", " + 
										" presDtl.duration_unit as \"durationUnit\", " + 
										" preFreDtl.start_date as \"startDateNew\", " + 
										" preFreDtl.stop_date as \"endDateNew\", " + 
										" 1 as \"visitTypeId\", " + 
										" roa.description as \"roaDesc\", " + 
										" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
										" to_char(preFreDtl.start_date,'DD/MM/YYYY HH24:MI:SS') as \"startDate\"," + 
										" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
										" presDtl.instruction_id as \"instructionId\" ," + 
										" instr.instruction_desc as \"instructionDesc\", " + 
										" presDtl.is_medication_stop as \"isMedicationStop\","+
										" (case when presDtl.item_id is not null then gen.name \r\n" + 
										"	else presDtl.generic_name end) as \"genericName\" " + 
										" FROM pharmacy.t_prescription pres " + 
										" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
										" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
										" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
										" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
										" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
										" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
										" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
										" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
										" where " + 
										"	pres.patient_id =:patientId " + 
										"	and pres.is_active =true " + 
										"	and presDtl.is_active =true " + 
										"	and pres.encounter_id =:encounterId "
										+ " and pres.organization_id =:organizationId"
										+ " AND presDtl.is_medication_stop='N' " 
										+ " and presDtl.status_id != 9 "
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
						
					}
					
					
		if(dto.getAdmisisonId() != null){
						
						 listMedication=sessionFactory.getCurrentSession()
								.createSQLQuery( "SELECT presDtl.item_id as \"itemId\", " + 
										" pres.id as \"prescriptionId\", " + 
										" (case when presDtl.item_id is not null then itm.name " + 
										"	else presDtl.item_name end) as \"itemName\", " + 
										" preFreDtl.dose as \"doseDesc\","+
										" su.unit as \"strengthDesc\", " + 
										" preFreDtl.frequency_id as \"frequencyId\", " + 
										" freq.description as \"frequencyDesc\", " +
	                                     " freq.no_of_administration_time as \"noOfAdministrationTime\", " +
										" freq.code as \"frequencyCode\", " +
										" preFreDtl.route_id as \"roaId\", " + 
										" presDtl.order_qty as \"orderQty\", " +
										" presDtl.duration as \"duration\", " + 
										" presDtl.duration as \"durationCount\", " + 
										" presDtl.instruction_desc as \"instructionText\", " + 
										" presDtl.duration_unit as \"durationUnit\", " + 
										" preFreDtl.start_date as \"startDateNew\", " + 
										" preFreDtl.stop_date as \"endDateNew\", " + 
										" 1 as \"visitTypeId\", " + 
										" roa.description as \"roaDesc\", " + 
										" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," + 
										" to_char(preFreDtl.start_date,'YYYY-MM-DD') as \"startDate\"," + 
										" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
										" presDtl.instruction_id as \"instructionId\" ," + 
										" instr.instruction_desc as \"instructionDesc\", " + 
										" presDtl.is_medication_stop as \"isMedicationStop\","+
										" (case when presDtl.item_id is not null then gen.name \r\n" + 
										"	else presDtl.generic_name end) as \"genericName\" " + 
										" FROM pharmacy.t_prescription pres " + 
										" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
										" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
										" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
										" LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " + 
										" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
										" LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " + 
										" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
										" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "  + 
										" where " + 
										"	pres.patient_id =:patientId " + 
										"	and pres.is_active =true " + 
										"	and presDtl.is_active =true " + 
										"	and pres.admission_id =:admissionId "
										+ " and pres.organization_id =:organizationId"
										+ " AND presDtl.is_medication_stop='N' " 
										+ " and presDtl.status_id != 9 "
										)
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
						
					}
					
					
					
		List<AllergyPatientMapperDto> allergyPatientMapperDtoList= null;
		if(dto.getEncounterId() != null){
					String allergyQuery = "SELECT DISTINCT allergypat0_.allergy_id AS \"allergyId\", "
					           + "allergypat0_.patient_id AS \"patientId\", "
					           + "allergypat0_.generic_id AS \"genericId\" , "
					           + "allergypat0_.allergy_type_id AS \"allergyTypeId\", "
					           + "allergypat0_.remark AS \"remark\", "
					           + "allergymas1_.allergy_name AS \"allergyName\", "
					           + "allergymas1_.allergy_desc AS \"allergyDesc\", "
					           + "allergytyp3_.allergy_type_desc AS \"allergyTypeDesc\", "
					           + "allergypat0_.is_no_known_allergies AS \"isNoKnownAllergies\", "
					           + "generic2_.name AS \"genericName\" "
					           + "FROM patient.t_allergy_patient_mapper allergypat0_ "
					           + "LEFT OUTER JOIN emr.m_allergy_master allergymas1_ ON allergypat0_.allergy_id = allergymas1_.allergy_id "
					           + "LEFT OUTER JOIN public.m_generic generic2_ ON allergypat0_.generic_id = generic2_.id "
					           + "LEFT OUTER JOIN emr.m_allergy_type_master allergytyp3_ ON allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id "
					           + "LEFT OUTER JOIN emr.m_severity_master severityma4_ ON allergypat0_.severity_id = severityma4_.severity_id "
					           + "WHERE allergypat0_.patient_id =:patientId "
					           + "AND allergypat0_.organization_id =:organizationId "
					           + "AND allergypat0_.encounter_id =:encounterId "
					           + "AND (allergypat0_.resolved_remark IS NULL) "
					           + "AND allergypat0_.status = 'A'";

					
					 allergyPatientMapperDtoList=sessionFactory.getCurrentSession()
							.createSQLQuery(allergyQuery).setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
					
		}
		
		
		
		if(dto.getAdmisisonId() != null){

			String allergyQuery = "SELECT DISTINCT allergypat0_.allergy_id AS \"allergyId\", "
			           + "allergypat0_.patient_id AS \"patientId\", "
			           + "allergypat0_.generic_id AS \"genericId\" , "
			           + "allergypat0_.allergy_type_id AS \"allergyTypeId\", "
			           + "allergypat0_.remark AS \"remark\", "
			           + "allergymas1_.allergy_name AS \"allergyName\", "
			           + "allergymas1_.allergy_desc AS \"allergyDesc\", "
			           + "allergytyp3_.allergy_type_desc AS \"allergyTypeDesc\", "
			           + "allergypat0_.is_no_known_allergies AS \"isNoKnownAllergies\", "
			           + "generic2_.name AS \"genericName\" "
			           + "FROM patient.t_allergy_patient_mapper allergypat0_ "
			           + "LEFT OUTER JOIN emr.m_allergy_master allergymas1_ ON allergypat0_.allergy_id = allergymas1_.allergy_id "
			           + "LEFT OUTER JOIN public.m_generic generic2_ ON allergypat0_.generic_id = generic2_.id "
			           + "LEFT OUTER JOIN emr.m_allergy_type_master allergytyp3_ ON allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id "
			           + "LEFT OUTER JOIN emr.m_severity_master severityma4_ ON allergypat0_.severity_id = severityma4_.severity_id "
			           + "LEFT OUTER JOIN opd.t_encounter_master encounterm5_ ON allergypat0_.patient_id = encounterm5_.patient_id "
			           + "WHERE allergypat0_.patient_id =:patientId "
			           + "AND allergypat0_.organization_id =:organizationId "
			           + "AND allergypat0_.admission_id =:admissionId "
			           + "AND (allergypat0_.resolved_remark IS NULL) "
			           + "AND allergypat0_.status = 'A'";

			
		 allergyPatientMapperDtoList=sessionFactory.getCurrentSession()
					.createSQLQuery(allergyQuery).setParameter("organizationId", encounterMasterDto.getOrganizationId())
					.setParameter("patientId", encounterMasterDto.getPatientId())
					.setParameter("admissionId", dto.getAdmisisonId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			

		}
					
					
				List<ProcedureOrderMasterDto> procedureList= null;
			    if(dto.getEncounterId()!= null) {
				    Response res = getProcedureOrderListByAdmissionIdForAbdm(0,dto.getEncounterId() , 0);
				    procedureList=res.getListObject();
			    }
				
				if(dto.getAdmissionId()!= null) {
			    	Response resProcedureListAdmission=	getProcedureOrderListByAdmissionIdForAbdm(dto.getAdmissionId(), 0, 0);
				    procedureList=resProcedureListAdmission.getListObject();
				}
				
				List<PatientMedicalHistoryDto> listPatientMedicalHistoryDto =null;
				
				if(dto.getEncounterId()!= null) {
					
					String query = "select " +
						    "patientmed0_.patient_medical_history_id as \"patientMedicalHistoryId\", " +
						    "patientmed0_.encounter_id as \"encounterId\", " +
						    "patientmed0_.diagnosis_id as \"diagnosisId\", " +
						    "patientmed0_.patient_id as \"patientId\", " +
						    "patientmed0_.since as \"since\", " +
						    "patientmed0_.duration as \"duration\"," +
						    "patientmed0_.remark as \"remark\", " +
						    "patientmed0_.cov_template_id as \"covTemplateId\", " +
						    "patientmed0_.is_no_significant_status as \"isNoSignificantStatus\", " +
						    "patientmed0_.unit_id as \"unitId\", " +
						    "patientmed0_.organization_id as \"organizationId\", " +
						    "patientmed0_.status as \"status\", " +
						    "to_char(patientmed0_.created_date, 'DD/MM/YYYY') as \"created_date\", " +
						    "patientmed0_.created_date as \"created_date_new\", " +
						    "to_char(patientmed0_.updated_date, 'DD/MM/YYYY') as \"updated_date\", " +
						    "diagnosism1_.code as \"code\", " +
						    "diagnosism1_.diagnosis_name as \"diagnosisName\", " +
						    "case " +
						    "when employeema3_.employee_id is not null then (employeema3_.first_name || ' ' || employeema3_.last_name) " +
						    "when doctormast4_.doctor_id is not null then ('Dr. ' || doctormast4_.first_name || ' ' || doctormast4_.last_name) " +
						    "else 'Admin' " +
						    "end as \"userName\", " +
						    "patientmed0_.is_enter_in_error as \"isEnterInError\", " +
						    "patientmed0_.is_reviewed_flag as \"isReviewedFlag\", " +
						    "patientmed0_.role_id as \"roleId\" "+
						    "from patient.t_patient_medical_history patientmed0_ " +
						    "left outer join emr.m_diagnosis_master diagnosism1_ on patientmed0_.diagnosis_id = diagnosism1_.diagnosis_id " +
						    "left outer join public.m_user_master usermaster2_ on patientmed0_.created_by = usermaster2_.user_id " +
						    "left outer join public.m_employee_master employeema3_ on usermaster2_.user_id = employeema3_.user_id " +
						    "left outer join doctor.m_doctor_master doctormast4_ on usermaster2_.user_id = doctormast4_.user_id " +
						    "where patientmed0_.patient_id = :patientId " +
						    "and patientmed0_.encounter_id =:encounterId " +
						    "and patientmed0_.status = 'A' " +
						    "and patientmed0_.organization_id = :organizationId " +
						    "order by patientmed0_.patient_medical_history_id asc";



					 listPatientMedicalHistoryDto=sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
					
			    }
				
				if(dto.getAdmissionId()!= null) {
					
					
					String query = "select " +
						    "patientmed0_.patient_medical_history_id as \"patientMedicalHistoryId\", " +
						    "patientmed0_.encounter_id as \"encounterId\", " +
						    "patientmed0_.diagnosis_id as \"diagnosisId\", " +
						    "patientmed0_.patient_id as \"patientId\", " +
						    "patientmed0_.since as \"since\", " +
						    "patientmed0_.duration as \"duration\"," +
						    "patientmed0_.remark as \"remark\", " +
						    "patientmed0_.cov_template_id as \"covTemplateId\", " +
						    "patientmed0_.is_no_significant_status as \"isNoSignificantStatus\", " +
						    "patientmed0_.unit_id as \"unitId\", " +
						    "patientmed0_.organization_id as \"organizationId\", " +
						    "patientmed0_.status as \"status\", " +
						    "to_char(patientmed0_.created_date, 'DD/MM/YYYY') as \"created_date\", " +
						    "patientmed0_.created_date as \"created_date_new\", " +
						    "to_char(patientmed0_.updated_date, 'DD/MM/YYYY') as \"updated_date\", " +
						    "diagnosism1_.code as \"code\", " +
						    "diagnosism1_.diagnosis_name as \"diagnosisName\", " +
						    "case " +
						    "when employeema3_.employee_id is not null then (employeema3_.first_name || ' ' || employeema3_.last_name) " +
						    "when doctormast4_.doctor_id is not null then ('Dr. ' || doctormast4_.first_name || ' ' || doctormast4_.last_name) " +
						    "else 'Admin' " +
						    "end as \"userName\", " +
						    "patientmed0_.is_enter_in_error as \"isEnterInError\", " +
						    "patientmed0_.is_reviewed_flag as \"isReviewedFlag\", " +
						    "patientmed0_.role_id as \"roleId\" "+
						    "from patient.t_patient_medical_history patientmed0_ " +
						    "left outer join emr.m_diagnosis_master diagnosism1_ on patientmed0_.diagnosis_id = diagnosism1_.diagnosis_id " +
						    "left outer join public.m_user_master usermaster2_ on patientmed0_.created_by = usermaster2_.user_id " +
						    "left outer join public.m_employee_master employeema3_ on usermaster2_.user_id = employeema3_.user_id " +
						    "left outer join doctor.m_doctor_master doctormast4_ on usermaster2_.user_id = doctormast4_.user_id " +
						    "where patientmed0_.patient_id = :patientId " +
						    "and patientmed0_.admission_id =:admissionId " +
						    "and patientmed0_.status = 'A' " +
						    "and patientmed0_.organization_id = :organizationId " +
						    "order by patientmed0_.patient_medical_history_id asc";



					 listPatientMedicalHistoryDto=sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("admissionId", dto.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
				}
				
				
				
				
				//Immunization
				List<VaccineImmunizationMapperDto> vacineList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VACCINE_IMMUNIZATION_MAPPER_ABDM")
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class)).list();
				
				

				if(dto.getEncounterId() != null){
					Session statlessSession = sessionFactory.getCurrentSession();
					StringBuilder vitalsQuery = new StringBuilder(statlessSession
							.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM").getQueryString());
					listAppointmentVitalMapperDto = statlessSession.createQuery(vitalsQuery.toString())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("encounterId", dto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
				}
				if(dto.getAdmisisonId() != null){
					Session statlessSession = sessionFactory.getCurrentSession();
					StringBuilder vitalsQuery = new StringBuilder(statlessSession
							.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM_ADMISSION").getQueryString());
					listAppointmentVitalMapperDto = statlessSession.createQuery(vitalsQuery.toString())
							.setParameter("organizationId", encounterMasterDto.getOrganizationId())
							.setParameter("patientId", encounterMasterDto.getPatientId())
							.setParameter("admissionId", dto.getAdmisisonId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
				}
				
				
				
				
				 String habitQuery=sessionFactory.getCurrentSession().getNamedQuery(
				 "GET_CURRENT_PATIENT_HABIT_DETAILS_LIST").getQueryString(); StringBuffer
				 subQuery=new StringBuffer(); if(encounterMasterDto.getEncounterId()!=null){
				 subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC "); }
				 if(encounterMasterDto.getAdmissionId()!=null){
				 subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC "); }
				
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;
			habitQuery=habitQuery.concat(subQuery.toString());
				listPatientHabitDetailsDto=sessionFactory.getCurrentSession().createQuery(habitQuery)
						.setParameter("patientId", encounterMasterDto.getPatientId())
						.setParameter("organizationId", encounterMasterDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
				
				
				//Diet Advice
				StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_OBSERVATION_LIST_ABDM").getQueryString());
				if(encounterMasterDto.getAdmissionId()!=null && encounterMasterDto.getAdmissionId()!=0){
					stringQuery.append(" AND observation.admissionId="+encounterMasterDto.getAdmissionId());
				}else if(encounterMasterDto.getEncounterId()!=null && encounterMasterDto.getEncounterId()!=0){
					stringQuery.append(" AND observation.encounterId="+encounterMasterDto.getEncounterId());
				}
				List<ObservationDto> observationList=sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
						.setParameter("organizationId",  encounterMasterDto.getOrganizationId())
						.setParameter("patientId", encounterMasterDto.getPatientId())			
						.setResultTransformer(Transformers.aliasToBean(ObservationDto.class)).list();
				
				    dto.setComplaintlistData(listComplaints);
					dto.setMedicationDataList(listMedication);
					if(listlabOrder != null && listlabOrder.size() >0){
						dto.setLabOrderList(listlabOrder);
					}else{
						dto.setLabOrderList(listlabOrderAdmission);
					}
					
					dto.setAdviceDataList(listAdvice);
					dto.setAllergyDataList(allergyPatientMapperDtoList);
					dto.setComplaintlistData(listComplaints);
					dto.setAdviceDataList(listAdvice);
					dto.setProcedureList(procedureList);
					dto.setListPatientMedicalHistoryDto(listPatientMedicalHistoryDto);
					dto.setVacineList(vacineList);
					dto.setListAppointmentVitalMapperDto(listAppointmentVitalMapperDto);
					dto.setListPatientHabitDetailsDto(listPatientHabitDetailsDto);
					dto.setObservationList(observationList);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list3, null);
	} catch (HibernateException he) {
		throw he ;
	} catch (Exception e) {
		throw e ;
	}
}*/

	

	@Override
	public Response getPatientDocDetailsForNabh(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		 try {
			String query=  "select doc_type_mst.document_type_name as \"documentName\", "
						 + " doc_dtls.document_path as \"documentPath\", "
						 + " doc_dtls.file_name as \"fileName\", "
						 + " doc_dtls.remark as \"remark\", "
						 + " doc_dtls.document_type_id as \"documentTypeId\", "
						 + " u_app.bucket_flag as \"bucketFlag\", "
						 + " to_char(doc_dtls.created_date,'DD-MM-YYYY') as \"createdDate\", "
						 + " to_char(doc_dtls.created_date,'DD-MM-YYYY') as \"uploadDoccumentDate\", "
						 + " doc_dtls.encounter_doc_id as \"encounterDocId\" "
						 + " from opd.t_encounter_doc_details doc_dtls "
						 + " INNER join public.m_document_type_master doc_type_mst on "
						 + " doc_dtls.document_type_id = doc_type_mst.document_type_id  "
						 + " INNER join t_unit_application_billing_configuration u_app on "
						 + " u_app.unit_id = doc_dtls.unit_id  "
						 + " where doc_dtls.organisation_id  =:orgId "
						 + " and doc_dtls.unit_id =:unitId "
						 + " and doc_dtls.status = 'A' "
						 + " and doc_dtls.nabh_reporting_mapper_id =:nabhReportingMapperId ";
			List<PatientDocDto> listPatientDocDto = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", patientDocReqDto.getOrganisationId())
					.setParameter("unitId", patientDocReqDto.getUnitId())
					.setParameter("nabhReportingMapperId", patientDocReqDto.getNabhReportingMapperId()) 
					.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listPatientDocDto, null);
			 
		 }catch (HibernateException e){
		 logger.error("getPatientDocDetailsForNabh failed", e);
		 String errorMsg = EncounterMasterDaoImpl.class.getName() + ":" +e.getMessage();
		 return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		 }
	}


	@Override
	public Response saveDocumentAgainstEncounterForNbh(EncounterDocDetailsDto encounterDocDetailsDto)
			throws ApplicationException {
		try {
			EncounterDocDetails master=new EncounterDocDetails();
				master.setEncounterId(encounterDocDetailsDto.getEncounterId());
				master.setDocumentTypeId(encounterDocDetailsDto.getDocumentTypeId());
				master.setPatientId(encounterDocDetailsDto.getPatientId());
				master.setDocumentPath(encounterDocDetailsDto.getDocumentPath());
				master.setStatus('A');
				master.setOrganisationId(encounterDocDetailsDto.getOrganisationId());
				master.setUnitId(encounterDocDetailsDto.getUnitId());
				master.setCreatedBy(encounterDocDetailsDto.getCreatedBy());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(encounterDocDetailsDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
				master.setUpdatedBy(encounterDocDetailsDto.getUpdatedBy());
				master.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId()));
				master.setVisitTypeId(encounterDocDetailsDto.getVisitTypeId());
				master.setAdmissionId(encounterDocDetailsDto.getAdmissionId());
				master.setExpenseId(encounterDocDetailsDto.getExpenseId());
				master.setGrnId(encounterDocDetailsDto.getGrnId());
				master.setNabhReportingMapperId(encounterDocDetailsDto.getNabhReportingMapperId());
				master.setPreauthRequestId(encounterDocDetailsDto.getPreauthRequestId());
				master.setLabSampleDtlsId(encounterDocDetailsDto.getLabSampleDtlsId());
				master.setCompanyExpenseId(encounterDocDetailsDto.getCompanyExpenseId());
                master.setRemark(encounterDocDetailsDto.getRemark()!=null ? encounterDocDetailsDto.getRemark():null);
                master.setPoId(encounterDocDetailsDto.getPoId()!=null ? encounterDocDetailsDto.getPoId():null);
                master.setDeathPatientId(encounterDocDetailsDto.getDeathPatientId() != null ? encounterDocDetailsDto.getDeathPatientId():null);
                master.setOpbId(encounterDocDetailsDto.getOpbId());
                master.setVendorId(encounterDocDetailsDto.getVendorId());
				if(encounterDocDetailsDto.getUploadDoccumentDate() != null) {
					master.setUploadDoccumentDate(GlobalCommonDateUtils.getDate(encounterDocDetailsDto.getUploadDoccumentDate(), "dd-MM-yyyy HH:mm:ss"));
				}
				master.setFileName(encounterDocDetailsDto.getFileName());
		    Integer id = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);

		} catch (HibernateException e){
		logger.error("saveDocumentAgainstEncounterForNbh failed", e);
		 String errorMsg = EncounterMasterDaoImpl.class.getName() + ":" +e.getMessage();
		 return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response saveDocumentAgainstEncounterForIncident(EncounterDocDetailsDto encounterDocDetailsDto)
			throws ApplicationException {
		try {
			EncounterDocDetails master=new EncounterDocDetails();
			master.setEncounterId(encounterDocDetailsDto.getEncounterId());
			master.setDocumentTypeId(encounterDocDetailsDto.getDocumentTypeId());
			master.setPatientId(encounterDocDetailsDto.getPatientId());
			master.setDocumentPath(encounterDocDetailsDto.getDocumentPath());
			master.setStatus('A');
			master.setOrganisationId(encounterDocDetailsDto.getOrganisationId());
			master.setUnitId(encounterDocDetailsDto.getUnitId());
			master.setCreatedBy(encounterDocDetailsDto.getCreatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(encounterDocDetailsDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
			master.setUpdatedBy(encounterDocDetailsDto.getUpdatedBy());
			master.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId()));
			master.setVisitTypeId(encounterDocDetailsDto.getVisitTypeId());
			master.setAdmissionId(encounterDocDetailsDto.getAdmissionId());
			master.setExpenseId(encounterDocDetailsDto.getExpenseId());
			master.setGrnId(encounterDocDetailsDto.getGrnId());
			master.setIncidentReportingMapperId(encounterDocDetailsDto.getIncidentReportingMapperId());
			master.setPreauthRequestId(encounterDocDetailsDto.getPreauthRequestId());
			master.setLabSampleDtlsId(encounterDocDetailsDto.getLabSampleDtlsId());
			master.setCompanyExpenseId(encounterDocDetailsDto.getCompanyExpenseId());
			master.setRemark(encounterDocDetailsDto.getRemark()!=null ? encounterDocDetailsDto.getRemark():null);
			master.setPoId(encounterDocDetailsDto.getPoId()!=null ? encounterDocDetailsDto.getPoId():null);
			master.setDeathPatientId(encounterDocDetailsDto.getDeathPatientId() != null ? encounterDocDetailsDto.getDeathPatientId():null);
			master.setOpbId(encounterDocDetailsDto.getOpbId());
			master.setVendorId(encounterDocDetailsDto.getVendorId());
			if(encounterDocDetailsDto.getUploadDoccumentDate() != null) {
				master.setUploadDoccumentDate(GlobalCommonDateUtils.getDate(encounterDocDetailsDto.getUploadDoccumentDate(), "dd-MM-yyyy HH:mm:ss"));
			}
			master.setFileName(encounterDocDetailsDto.getFileName());
			Integer id = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);

		} catch (HibernateException e){
		logger.error("saveDocumentAgainstEncounterForIncident failed", e);
		String errorMsg = EncounterMasterDaoImpl.class.getName() + ":" +e.getMessage();
		return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response getPatientDocDetailsForIncident(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			String query=  "select doc_type_mst.document_type_name as \"documentName\", "
					+ " doc_dtls.document_path as \"documentPath\", "
					+ " doc_dtls.file_name as \"fileName\", "
					+ " doc_dtls.remark as \"remark\", "
					+ " doc_dtls.document_type_id as \"documentTypeId\", "
					+ " u_app.bucket_flag as \"bucketFlag\", "
					+ " to_char(doc_dtls.created_date,'DD-MM-YYYY') as \"createdDate\", "
					+ " to_char(doc_dtls.created_date,'DD-MM-YYYY') as \"uploadDoccumentDate\", "
					+ " doc_dtls.encounter_doc_id as \"encounterDocId\" "
					+ " from opd.t_encounter_doc_details doc_dtls "
					+ " INNER join public.m_document_type_master doc_type_mst on "
					+ " doc_dtls.document_type_id = doc_type_mst.document_type_id  "
					+ " INNER join t_unit_application_billing_configuration u_app on "
					+ " u_app.unit_id = doc_dtls.unit_id  "
					+ " where doc_dtls.organisation_id  =:orgId "
					+ " and doc_dtls.unit_id =:unitId "
					+ " and doc_dtls.status = 'A' "
					+ " and doc_dtls.incident_reporting_mapper_id =:incidentReportingMapperId ";
			List<PatientDocDto> listPatientDocDto = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", patientDocReqDto.getOrganisationId())
					.setParameter("unitId", patientDocReqDto.getUnitId())
					.setParameter("incidentReportingMapperId", patientDocReqDto.getIncidentReportingMapperId())
					.setResultTransformer(Transformers.aliasToBean(PatientDocDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listPatientDocDto, null);

		}catch (HibernateException e){
		logger.error("getPatientDocDetailsForIncident failed", e);
		String errorMsg = EncounterMasterDaoImpl.class.getName() + ":" +e.getMessage();
		return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response getServiceDetails(List<Integer> serviceIds, Integer unitId) throws ApplicationException {
		try {
			String query= "SELECT "  +
					"    msm.service_master_id AS \"serviceMasterId\","  +
					"    msm.speciality_id AS \"specialityId\","  +
					"    msm.sub_speciality_id AS \"subSpecialityId\","  +
					"    tusmd.is_outsource AS \"isOutsource\","  +
					"    tusmd.is_package AS \"isPackage\","  +
					"    tusmd.is_rate_editable AS \"isRateEditable\","  +
					"    tusmd.is_tax_applicable AS \"isTaxApplicable\","  +
					"    tusmd.is_ref_doctor_share AS \"isRefDoctorShare\","  +
					"    tusmd.is_discount AS \"isDiscount\","  +
					"    tusmd.is_procedure AS \"isProcedure\","  +
					"    tusmd.is_doc_req AS \"isDocReq\","  +
					"    tusmd.is_poc AS \"isPoc\","  +
					"    tusmd.is_periodicity AS \"isPeriodicity\","  +
					"    tusmd.is_auto_render AS \"isAutoRender\","  +
					"    tusmd.is_appointable AS \"isAppointable\","  +
					"    tusmd.is_review_applicable AS \"isReviewApplicable\","  +
					"    tusmd.is_doc_share_per_wise AS \"isDocSharePerWise\","  +
					"    tusmd.is_ref_share_per_wise AS \"isRefSharePerWise\","  +
					"    tuabc.self_payee_id AS \"selfPayeeId\","  +
					"    tuabc.default_self_tariff_id AS \"defaultSelfTariffId\","  +
					"    tuabc.opd_billing_cat_id AS \"opdBillingCatId\","  +
					"    tuabc.pharmacy_service_id AS \"pharmacyServiceId\","  +
					"    tuabc.default_patient_category_id AS \"defaultPatientCategoryId\" "  +
					"FROM service.m_service_master AS msm  "  +
					"INNER JOIN service.t_unit_service_master_details AS tusmd "  +
					"    ON msm.service_master_id = tusmd.service_master_id "  +
					"INNER JOIN public.t_unit_application_billing_configuration AS tuabc "  +
					"    ON tuabc.unit_id = tusmd.unit_id"  +
					" WHERE msm.service_master_id in(:servIds) "  +
					"    AND tusmd.unit_id = :unitId " ;
			List<ServiceMasterDto> listPatientDocDto = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameterList("servIds",serviceIds)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listPatientDocDto, null);

		}catch (HibernateException e){
		logger.error("getServiceDetails failed", e);
		String errorMsg = EncounterMasterDaoImpl.class.getName() + ":" +e.getMessage();
		return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}


	@Override
	public String getDocumentAnalyzeUrl(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {
			String query = "select ai_document_analyze_url from public.domain_config";
			String documentAnalyzeUrl = (String) sessionFactory.getCurrentSession()
					.createSQLQuery(query).uniqueResult();
			return documentAnalyzeUrl;

		}catch (HibernateException e){
		logger.error("getDocumentAnalyzeUrl failed", e);
		return null;
		}
	}

	@Override
	public Response saveAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto, String json) throws ApplicationException {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();

			sessionFactory.getCurrentSession().createSQLQuery("update opd.t_encounter_doc_details set "
							+ "document_analyzed_path =:documentAnalyzedPath, "
							+ "document_analyzed_date = now(), "
							+ "document_analyzed_json =:documentAnalyzedJson "
							+ "WHERE encounter_doc_id =:encounterDocId")
					.setParameter("documentAnalyzedPath", encounterDocDetailsDto.getVideo_url())
					.setParameter("documentAnalyzedJson", json)
					.setParameter("encounterDocId", Integer.parseInt(encounterDocDetailsDto.getReport_id()))
					.executeUpdate();
			return new Response(SUCCESS, null, "Document analyzed successfully...!!", null, null);

		} catch (HibernateException he) {
		logger.error("saveAnalyzedReport failed", he);
		} catch (Exception e) {
		logger.error("saveAnalyzedReport failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();

			EncounterDocDetailsDto encounterDoc = (EncounterDocDetailsDto) sessionFactory.getCurrentSession().createSQLQuery("select doc.encounter_doc_id as \"encounterDocId\", "
							+ " doc.document_analyzed_path as \"videolink\", "
//							+ "to_char(doc.document_analyzed_date, 'YYYY-MM-DD HH24:MI:SS') as \"analyzedDocDate\" "
							+ " doc.document_analyzed_date as \"analyzedDocDate\" "
							+ " from opd.t_encounter_doc_details doc  "
							+ " WHERE doc.encounter_doc_id =:encounterDocId "
							+ " order by doc.encounter_doc_id desc limit 1")
					.setParameter("encounterDocId", encounterDocDetailsDto.getEncounterDocId())
					.setResultTransformer(Transformers.aliasToBean(EncounterDocDetailsDto.class)).uniqueResult();

			if(encounterDoc.getVideolink() != null && !encounterDoc.getVideolink().isEmpty()) {
				if(encounterDoc.getAnalyzedDocDate() != null) {
					String dateStr = encounterDoc.getAnalyzedDocDate().toString();
					String trimmedDateStr = dateStr.split("\\.")[0];
					LocalDateTime fetchedDate = LocalDateTime.parse(trimmedDateStr, dtf);
					LocalDateTime sevenDaysAgo = now.minusDays(7);
					if (fetchedDate.isBefore(sevenDaysAgo)) {
						return new Response(SUCCESS, SUCCESS_CODE, "Analyzed document has expired...!!", null, null);
					} else {
						return new Response(SUCCESS, SUCCESS_CODE, "SUCCESS", null, encounterDoc);
					}
				}
			}
			else{
				return new Response(SUCCESS, SUCCESS_CODE, "Analyzed document not ready...!!", null, null);
			}

		} catch (HibernateException he) {
		logger.error("getAnalyzedReport failed", he);
		} catch (Exception e) {
		logger.error("getAnalyzedReport failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateAiSummaryAndQuestion(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, encounterMasterDto);
		}  catch (Exception e) {
		logger.error("updateAiSummaryAndQuestion failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public void updateEncounterStopTime(EncounterAssignmenMapperDto mpprDto) throws ApplicationException {
		try {
			Date stopTime = TimeZoneComponent.getDateByzone(mpprDto.getUnitId());
			sessionFactory.getCurrentSession().createSQLQuery("UPDATE opd.t_encounter_details SET "
							+ " assignment_stop_time =:stopTime "
							+ " WHERE encounter_id =:encounterId AND assignment_stop_time IS NULL")
					.setParameter("stopTime", stopTime)
					.setParameter("encounterId", mpprDto.getEncounterId())
					.executeUpdate();

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public Response getAllCoversheetDataByPatientId(Integer patientId) throws ApplicationException {
		try {
			String sql = "  SELECT tedd.document_path AS \"documentPath\", pr.latest_medical_summary_report AS \"latestMedicalSummaryReport\" FROM opd.t_encounter_doc_details tedd LEFT JOIN patient.t_patient_registration pr ON pr.patient_id = tedd.patient_id " +
					" WHERE tedd.patient_id = :patientId AND tedd.document_type_id <> 69 AND tedd.created_date > (SELECT updated_date FROM opd.t_encounter_doc_details WHERE patient_id = :patientId AND document_type_id = 69)  ";
			List<EncounterDocDetailsDto> docDeatils = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(EncounterDocDetailsDto.class)).list();

			if (docDeatils != null && docDeatils.size() > 0) {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, docDeatils, null);
			}
		} catch (HibernateException e) {
		logger.error("getAllCoversheetDataByPatientId failed", e);
		} catch (Exception e) {
		logger.error("getAllCoversheetDataByPatientId failed", e);
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, "No Data Found", null, null);
	}

	@Override
	public String getAiLetterCallbackUrl() throws ApplicationException {
		try {
			String sql = " select hislink  from m_my_life_connection_link limit  1";
			String hisLink = (String) sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult();

			if (hisLink != null) {
				return  hisLink;
			}
		} catch (HibernateException e) {
		logger.error("getAiLetterCallbackUrl failed", e);
		} catch (Exception e) {
		logger.error("getAiLetterCallbackUrl failed", e);
		}
		return null;
	}

	@Override
	public Integer getLatestSummaryDocumentPKId(Integer patientId, Integer documentId) throws ApplicationException {
		try {
			String sql = " select encounter_doc_id from opd.t_encounter_doc_details  where patient_id = :patientId and document_type_id = :documentId limit 1 ";
			Integer docId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sql)
			.setParameter("patientId", patientId)
		     .setParameter("documentId", documentId).
					uniqueResult();

			if (docId != null) {
				return  docId;
			}
		} catch (HibernateException e) {
		logger.error("getLatestSummaryDocumentPKId failed", e);
		} catch (Exception e) {
		logger.error("getLatestSummaryDocumentPKId failed", e);
		}
		return null;
	}

	@Override
	public Integer updateSummaryDocument(Integer pkdocId, String docPath) throws ApplicationException {
		try {
			String sql = " UPDATE opd.t_encounter_doc_details SET document_path= :docPath,updated_date=Now() WHERE encounter_doc_id= :pkdocId ";
			Integer docId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("docPath", docPath)
					.setParameter("pkdocId", pkdocId).
					executeUpdate();

			if (docId != null) {
				return  docId;
			}
		} catch (HibernateException e) {
		logger.error("updateSummaryDocument failed", e);
		} catch (Exception e) {
		logger.error("updateSummaryDocument failed", e);
		}
		return 0;
	}

	@Override
	public Response getAugmentDoctorList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			List<EncounterMasterDto> encounterMasterDtoList = new ArrayList<>();
			String query = """ 
					 SELECT
					 	appo.ai_appointment_id as "aiAppointmentId",
					 	appo.appointment_id as "appointmentId",
					 	appo.doctor_id as "augmentDoctorId"
					 	FROM opd.ai_appointment_queue appo
					 	INNER JOIN doctor.m_doctor_master mdm on
					 	appo.doctor_id = mdm.doctor_id
					 	and mdm.status ='A'
					 	WHERE appo.status = 'PENDING'
					 	AND to_char(appo.updated_date,'yyyy-mm-dd') Between :fromDate and :toDate
					 	AND mdm.organization_id=:orgId
					 """;

			encounterMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
//					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.setParameter("fromDate", encounterMasterDto.getFromDate().substring(0, 10))
					.setParameter("toDate", encounterMasterDto.getToDate().substring(0, 10) )
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).list();
			if(encounterMasterDtoList != null && !encounterMasterDtoList.isEmpty()){
				return new Response<>(SUCCESS, SUCCESS_CODE,null , encounterMasterDtoList, encounterMasterDtoList.get(0));
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, "No Data Found", null, null);
		} catch (Exception e) {
		logger.error("getAugmentDoctorList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAugmentDoctor(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			String speciality = """
				SELECT
					map.speciality_id as "specialityId",
					master.speciality_name as "specialityName"
				FROM doctor.t_doctor_speciality_mapper map
				INNER JOIN doctor.m_speciality_master master on
					master.speciality_id = map.speciality_id
				WHERE
					map.doctor_id = :doctorId
					AND map.status = 'A'
					AND master.status = 'A'
					ORDER BY map.doctor_speciality_id DESC LIMIT 1
				""";

			EncounterMasterDto encounterMasterDto1 = (EncounterMasterDto) sessionFactory.getCurrentSession().createSQLQuery(speciality)
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class))
					.uniqueResult();

			encounterMasterDto.setSpecialityId(encounterMasterDto1.getSpecialityId());
			encounterMasterDto.setSpecialityName(encounterMasterDto1.getSpecialityName());

			String encounterMaster = """
				UPDATE opd.t_encounter_master SET
					dept_id = :deptId,
					doctor_id = :doctorId,
					updated_date = Now(),
					updated_by = :doctorId
				WHERE
					appointment_id = :appointmentId
					AND encounter_id = :encounterId
					AND unit_id = :unitId
					AND organization_id = :orgId
				""";

			Integer encounterMasterId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(encounterMaster)
					.setParameter("deptId", encounterMasterDto.getSpecialityId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("appointmentId", encounterMasterDto.getAppointmentId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.executeUpdate();

			String encounterDetails = """
				UPDATE opd.t_encounter_details SET
					doctor_id = :doctorId
				WHERE
					encounter_id = :encounterId
					AND unit_id = :unitId
					AND organization_id = :orgId
				""";

			Integer encounterDetailsId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(encounterDetails)
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.executeUpdate();

			String orderMaster = """
				UPDATE t_order_master SET
					ord_doctor_id = :doctorId,
					ord_doc_spl_id = :specialityId,
					updated_by = :doctorId,
					updated_date = now()
				WHERE
					order_id = (
						SELECT master.order_id
							FROM t_order_master master
							WHERE
								master.encounter_id = :encounterId
								AND master.org_unit_id = :unitId
								AND master.org_id = :orgId
							ORDER BY master.order_id ASC LIMIT 1
						)
				""";

			Integer orderMasterId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(orderMaster)
					.setParameter("specialityId", encounterMasterDto.getSpecialityId())
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.executeUpdate();

			String orderDetails = """
					UPDATE t_order_details details
					SET
					    ord_doc_id = :doctorId,
					    ord_doc_spl_id = :specialityId,
					    updated_by = :doctorId,
					    updated_date = now()
					WHERE details.order_details_id = (
					    SELECT details2.order_details_id
							FROM t_order_details details2
							INNER JOIN t_order_master master
								ON details2.order_id = master.order_id
							WHERE
								master.encounter_id = :encounterId
								AND details2.org_unit_id = :unitId
								AND details2.org_id = :orgId
							ORDER BY details2.order_details_id ASC
							LIMIT 1
						)
				""";

			Integer orderDetailsId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(orderDetails)
					.setParameter("specialityId", encounterMasterDto.getSpecialityId())
					.setParameter("doctorId", encounterMasterDto.getDoctorId())
					.setParameter("encounterId", encounterMasterDto.getEncounterId())
					.setParameter("unitId", encounterMasterDto.getUnitId())
					.setParameter("orgId", encounterMasterDto.getOrganizationId())
					.executeUpdate();

			String aiAppointment = """
				UPDATE opd.ai_appointment_queue SET
					status = 'COMPLETED'
				WHERE
					ai_appointment_id = :aiAppointmentId
					AND appointment_id = :appointmentId
				""";

			Integer aiAppointmentId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(aiAppointment)
					.setParameter("appointmentId",encounterMasterDto.getAppointmentId(),IntegerType.INSTANCE)
					.setParameter("aiAppointmentId",encounterMasterDto.getAiAppointmentId(),IntegerType.INSTANCE)
					.executeUpdate();

			return new Response<>(SUCCESS, SUCCESS_CODE, "All Data Updated Successfully..!", null, encounterMasterDto);
		} catch (Exception e) {
			logger.error("Exection", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, 0);
	}
}
