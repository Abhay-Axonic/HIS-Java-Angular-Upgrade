package com.param.ER.dao;

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

import com.param.ER.dto.ERBedTypeAllocationDto;
import com.param.ER.model.ERBedTypeAllocation;
import com.param.adt.admission.model.Admission;
import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.Transfer;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.UnknownPatientRegistrationDto;
import com.param.global.org.common.dto.PriorityMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ERBedTypeAllocationDaoImpl extends GenericDao<ERBedTypeAllocation, Integer>
		implements IERBedTypeAllocationDao, ICommonConstants {

	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	public ERBedTypeAllocationDaoImpl() {
		super(ERBedTypeAllocation.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getUnknownPatientsList(UnknownPatientRegistrationDto unknownPatientRegistrationDto)
			throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			StringBuilder newQuery = getSearchQuery(unknownPatientRegistrationDto);
			Query actualQuery = session.createQuery(newQuery.toString());

			Query sql= actualQuery
					.setParameter("orgId", unknownPatientRegistrationDto.getOrganizationId())
					.setParameter("unitId", unknownPatientRegistrationDto.getUnitId());
			if (unknownPatientRegistrationDto.getErBedType() > 0
					&& unknownPatientRegistrationDto.getPatientName() == null) {
				sql.setParameter("erBedType", unknownPatientRegistrationDto.getErBedType());

			} else if (unknownPatientRegistrationDto.getErBedType() > 0
					&& unknownPatientRegistrationDto.getPatientName() != null) {
				sql.setParameter("erBedType", unknownPatientRegistrationDto.getErBedType());
				sql.setParameter("patientName", unknownPatientRegistrationDto.getPatientName());
			} else if (unknownPatientRegistrationDto.getErBedType() == 0
					&& unknownPatientRegistrationDto.getPatientName() != null) {
				sql.setParameter("status", 'B');
			}

			List<UnknownPatientRegistrationDto> unknownPatientRegistrationDtosList = sql.setResultTransformer(Transformers.aliasToBean(PriorityMasterDto.class)).list();
			if (unknownPatientRegistrationDtosList != null) {
				return new Response(SUCCESS, null, null, unknownPatientRegistrationDtosList, null);
			}
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getERBedTypeAllocationList(ERBedTypeAllocationDto erBedTypeAllocationDto) {
		try {

			List<ERBedTypeAllocationDto> erBedTypeAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ER_BED_TYPE_ALLOCATION_LIST")
					.setParameter("orgId", erBedTypeAllocationDto.getOrganizationId())
					.setParameter("unitId", erBedTypeAllocationDto.getUnitId())
					.setFirstResult(erBedTypeAllocationDto.getOffset() != null ? erBedTypeAllocationDto.getOffset()  : 0)
					.setMaxResults(erBedTypeAllocationDto.getNoOfRecordsPerPage() != null ? erBedTypeAllocationDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			if (erBedTypeAllocationDtosList != null) {
				return new Response(SUCCESS, null, null, erBedTypeAllocationDtosList, null);
			}
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCount(ERBedTypeAllocationDto erBedTypeAllocationDto) {
		try {
			List<ERBedTypeAllocationDto> eRBedTypeAllocationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ER_BED_TYPE_ALLOCATION_LIST")
					.setParameter("orgId", erBedTypeAllocationDto.getOrganizationId())
					.setParameter("unitId", erBedTypeAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			if (eRBedTypeAllocationDtosList != null) {
				Integer count = eRBedTypeAllocationDtosList.size();
				return new Response(SUCCESS, null, null, null, count);
			}
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getERBedList(ERBedTypeAllocationDto erBedTypeAllocationDto) throws ApplicationException {
		try {

			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_ER_BED_LIST")
					.setParameter("organizationId", erBedTypeAllocationDto.getOrganizationId())
					.setParameter("unitId", erBedTypeAllocationDto.getUnitId())
					.setParameter("erCategoryId",erBedTypeAllocationDto.getErCategoryId() )
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();

			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateERPatientAdmission(AdmissionDto admissionDto) throws ApplicationException {
		try {

			Admission admission = mapper.map(admissionDto, Admission.class, "AdmissionDto_to_Admission");
				admission.setIsFinancialCreance('N');
				admission.setStatus('A');
//				admission.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() :0));
				admission.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() :0));
			sessionFactory.getCurrentSession().update(admission);
			return new Response<AdmissionDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			return new Response<AdmissionDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<AdmissionDto, Integer>(ERROR, null, null, null, null);
		} 
	}

	@Override
	public Response updateERPatientDetails(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Session session = sessionFactory.openSession();

			AdmissionDetails admissionDetails = mapper.map(admissionDto, AdmissionDetails.class,
					"AdmissionDto_to_AdmissionDetails");
				admissionDetails.setStatus('A');
				admissionDetails.setIsShortLeave('N');
				admissionDetails.setIsDischarge('N');
				admissionDetails.setIsCancel('N');
				admissionDetails.setIsClose('N');
				admissionDetails.setIsHighRisk('N');
				admissionDetails.setIsCritical('N');
				admissionDetails.setIsMedicoLegal('N');
				admissionDetails.setIsTransferOfCare('N');
				admissionDetails.setNursingAcceptance('Y');
				admissionDetails.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() :0));
				admissionDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() :0));
			sessionFactory.getCurrentSession().update(admissionDetails);

			if (admissionDto.getBedId() != null)
			// <-----Changing status of bed to Booked (5 is the ID of 'booked'
			// status)
			{
				Query qry3 = session.createQuery(
						"update BedMaster bed " +
								"set bed.bedStatusId = :bedStatusId, " +
								"bed.updatedBy = :updatedBy, " +
								"bed.updatedDate = :updatedDate " +
								"where bed.bedId = :bedId"
				);
				qry3.setParameter("bedStatusId", admissionDto.getBedStatusId());
				qry3.setParameter("updatedBy", admissionDto.getUpdatedBy());
				qry3.setParameter(
						"updatedDate",
						ADTCommonDateUtils.getDate(
								TimeZoneComponent.getDateByzone(
										admissionDto.getUnitId() != null ? admissionDto.getUnitId() : 0
								),
								"dd-MM-yyyy HH:mm:ss"
						)
				);
				qry3.setParameter("bedId", admissionDto.getBedId());
				qry3.executeUpdate();
				// ----->

				// <-----Updating status of current bed to I in BedLogStatus
				Query qry4 = session.createQuery("update BedLogStatus bedStatus set "
						+" bedStatus.status=:status, "
						+ "  bedStatus.updatedBy=:updatedBy, "
						+"  bedStatus.updatedDate=:updatedDate "
						+ " where bedStatus.status='A' AND bedStatus.bedId=:bedId ");
				qry4.setParameter("status", 'I');
				qry4.setParameter("updatedBy", admissionDto.getUpdatedBy());
				qry4.setParameter("updatedDate", ADTCommonDateUtils.getDate(
						TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() : 0),
						"dd-MM-yyyy HH:mm:ss"));
				qry4.setParameter("bedId", admissionDto.getBedId());
				qry4.executeUpdate();
				// ----->

				// <-----Adding new entry in log as the status of bed is
				// changing
				BedLogStatus bedLogStatus = mapper.map(admissionDto, BedLogStatus.class,
						"AdmissionDto_to_BedLogStatus");
				bedLogStatus.setCreatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() :0));
				bedLogStatus.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId() :0));
				Transaction tr2 = session.beginTransaction();
				session.save(bedLogStatus);
				tr2.commit();
				session.close();
				// ----->
			}
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			 he.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}

		return new Response(ERROR, null, null, null, null);
	}

	private StringBuilder getSearchQuery(UnknownPatientRegistrationDto unknownPatientRegistrationDto) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.getNamedQuery("GET_UNKNOWN_PATIENT_LIST");
		StringBuilder qry = new StringBuilder(query.getQueryString());
		if (unknownPatientRegistrationDto.getErBedType() > 0
				&& unknownPatientRegistrationDto.getPatientName() == null) {
			qry.append("AND admd.er_bed_type_allocation=:erBedType ");
			return qry;

		} else if (unknownPatientRegistrationDto.getErBedType() > 0
				&& unknownPatientRegistrationDto.getPatientName() != null) {
			qry.append("AND admd.er_bed_type_allocation=:erBedType "
					+ " AND patient.patent_name=:patientName ");
			return qry;
		} else if (unknownPatientRegistrationDto.getErBedType() == 0
				&& unknownPatientRegistrationDto.getPatientName() != null) {
			qry.append("AND adm.status=:status ");
			return qry;

		} else {
			return qry;
		}
	}

	@Override
	public Response getActiveDoctorList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			List<DoctorMasterDto> doctorMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_LIST").setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();

			return new Response(SUCCESS, null, null, doctorMasterDtoList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveTransfer(TransferDto transferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Transfer transfer = mapper.map(transferDto, Transfer.class, "TransferDto_to_Transfer");
				transfer.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0));
				transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0));
			transfer.setStatus('A');
			transfer.setIsPrimary('Y');
			session.save(transfer);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			return new Response(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response(ERROR, null, null, null, null);
		} 
	}

	@Override
	public Response getDoctorListForOPDBilling(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			List<DoctorMasterDto> doctorMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_LIST_FOR_OPD_BILLING")
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();

			return new Response(SUCCESS, null, null, doctorMasterDtoList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorShareWiseDocList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			
			String query = "select " 
					+ "	distinct tidssw.doctor_id as \"doctorId\",  "
					+ "		concat('Dr. ',coalesce(mdm.first_name ,''),' ',coalesce(mdm.middle_name,''),' ',coalesce(mdm.last_name,'')) as \"doctorName\" "
					+ " from " 
					+ "	billing.t_individual_doc_share_service_wise tidssw "
					+ "	inner join doctor.m_doctor_master mdm on mdm.doctor_id = tidssw.doctor_id  " 
					+ " where "
					+ "	tidssw.service_id = :serviceId "
					+ "	and tidssw.billing_bed_category_id = :billingBedCategoryId "
					+ "	and tidssw.visit_type_id = :visitTypeId "
					+ " 	and tidssw.org_unit_id  = :unitId"

					+ "	and tidssw.patient_type_id =  :patientTypeId "
					+ "	and tidssw.payment_entitlement_id = :paymentEntitlementId " +
					" and mdm.status = 'A' "
					
					+ " union  " + "select "
					+ "	distinct tidsgw.doctor_id as \"doctorId\",  "
					+ "		concat('Dr. ',coalesce(mdm.first_name ,''),' ',coalesce(mdm.middle_name,''),' ',coalesce(mdm.last_name,'')) as \"doctorName\" "
					+ " from " + "	billing.t_individual_doc_share_group_wise tidsgw "
					+ "	inner join doctor.m_doctor_master mdm on mdm.doctor_id = tidsgw.doctor_id  " 
					+ " where "
					+ "	tidsgw.department_id = :serviceDepartmentId "
					+ "	and tidsgw.billing_bed_category_id = :billingBedCategoryId "
					+ "	and tidsgw.visit_type_id = :visitTypeId "
					+ " 	and tidsgw.org_unit_id  = :unitId"
					+ "	and tidsgw.patient_type_id =  :patientTypeId "
					+ "	and tidsgw.payment_entitlement_id = :paymentEntitlementId " +
					" and mdm.status = 'A' "
					
					+ " union " 
					+ " select "
					+ "	distinct tdsm.doctor_id as \"doctorId\",  "
					+ "		concat('Dr. ',coalesce(mdm.first_name ,''),' ',coalesce(mdm.middle_name,''),' ',coalesce(mdm.last_name,'')) as \"doctorName\" "
					+ " from " 
					+ "	doctor.t_doctor_speciality_mapper tdsm "
					+ " inner join doctor.m_doctor_master mdm on mdm.doctor_id = tdsm.doctor_id  " 
					+ " where "
					+ "	tdsm.speciality_id in ( " 
					+ "	select " 
					+ "		distinct tgdsgw.speciality_id " 
					+ "	from "
					+ "		billing.t_global_doc_share_group_wise tgdsgw " 
					+ "	where "
					+ "		tgdsgw.department_id = :serviceDepartmentId "
					+ "		and tgdsgw.billing_bed_category_id = :billingBedCategoryId "
					+ "		and tgdsgw.visit_type_id = :visitTypeId "
					+ " 	and tgdsgw.org_unit_id  = :unitId"
					+ "		and tgdsgw.patient_type_id =  :patientTypeId "
					+ "		and tgdsgw.payment_entitlement_id = :paymentEntitlementId) " 
					+ "	and tdsm.status = 'A'" +
					" and mdm.status = 'A'  "
					
					+ " union  " + "select " 
					+ "	distinct tdsm.doctor_id as \"doctorId\",  "
					+ "		concat('Dr. ',coalesce(mdm.first_name ,''),' ',coalesce(mdm.middle_name,''),' ',coalesce(mdm.last_name,'')) as \"doctorName\" "
					+ " from " 
					+ "	doctor.t_doctor_speciality_mapper tdsm "
					+ "		 inner join doctor.m_doctor_master mdm on mdm.doctor_id = tdsm.doctor_id  " + " " + "where "
					+ "	tdsm.speciality_id in ( " 
					+ "	select " 
					+ "		distinct tgdssw.speciality_id " 
					+ "	from "
					+ "		billing.t_global_doc_share_service_wise tgdssw " 
					+ "	where "
					+ "		tgdssw.service_id = :serviceId "
					+ "		and tgdssw.billing_bed_category_id = :billingBedCategoryId "
					+ "		and tgdssw.visit_type_id = :visitTypeId "
					+ " 	and tgdssw.org_unit_id  = :unitId"
					+ "		and tgdssw.patient_type_id = :patientTypeId "
					+ "		and tgdssw.payment_entitlement_id = :paymentEntitlementId) " 
					+ "	and tdsm.status = 'A'" +
					" and mdm.status = 'A'  ";
			List<DoctorMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("serviceId", doctorMasterDto.getServiceId())
					.setParameter("serviceDepartmentId", doctorMasterDto.getServiceDepartmentId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setParameter("billingBedCategoryId", doctorMasterDto.getBillingBedCategoryId())
					.setParameter("visitTypeId", doctorMasterDto.getVisitTypeId())
					.setParameter("patientTypeId", doctorMasterDto.getPatientTypeId())
					.setParameter("paymentEntitlementId", doctorMasterDto.getPaymentEntitlementId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

}
