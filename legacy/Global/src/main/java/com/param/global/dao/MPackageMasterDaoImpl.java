package com.param.global.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.MPackageMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.PackageListReqDto;
import com.param.global.dto.PackageListResDto;
import com.param.global.dto.PackageSearchRequestDto;
import com.param.global.dto.ParentPackageMasterDto;
import com.param.global.dto.ServiceForPackageReqDto;
import com.param.global.dto.ServiceForPackageResDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TPackageConsumptionServiceDetailDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.opd.coversheet.model.EncounterEhcConsultationMaster;
import com.param.opd.coversheet.model.EncounterEhcConsultationMppr;
import com.param.opd.encounter.dto.EncounterEhcConsultationMasterDto;
import com.param.opd.encounter.dto.EncounterEhcConsultationMpprDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.model.MPackageMaster;
import com.param.service.global.model.PackageVersionMaster;
import com.param.service.global.model.ParentPackageMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused"})
public class MPackageMasterDaoImpl extends GenericDao<MPackageMaster, Integer>
		implements IMPackageMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(MPackageMasterDaoImpl.class);

	public MPackageMasterDaoImpl() {
		super(MPackageMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	ITPackageConsumptionCapDetailsDao iTPackageConsumptionCapDetailsDao;

	@Autowired
	private com.param.global.service.VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveParentPackageMaster(ParentPackageMasterDto dto) throws ServiceException {
		try {

			// BR-159: cross-tenant IDOR fix -- org/unit must come from the JWT-verified
			// identity, never from the client-supplied DTO. All 4 package-creation
			// endpoints (/api/packages/ehc, /allInclusive, /multiencounter, /eitheror)
			// funnel through this method and the old code wrote verifiedOrgId/
			// verifiedUnitId into every child record, which live-confirmed let an
			// org-1 session create an org-999 parent package row.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();

			Integer parentPackageId = dto.getParentPackageId();
			ParentPackageMaster parentPackageMaster = null;
			if (parentPackageId != null && parentPackageId > 0) {
				
				 Integer versionId = this.getPackageVersionByParentPacakgeId(parentPackageId);
				 
				PackageVersionMaster versionMaster = new PackageVersionMaster();
				versionMaster.setParentPackageId(parentPackageId);
				versionMaster.setVersionId(versionId);
				versionMaster.setCreatedBy(dto.getCreatedBy());
				versionMaster.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				versionMaster.setOrgId(verifiedOrgId);
				versionMaster.setStatus(ACTIVE);
				versionMaster.setUnitId(verifiedUnitId);
				versionMaster.setUpdatedBy(dto.getUpdatedBy());
				versionMaster.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				
				sessionFactory.getCurrentSession().save(versionMaster);
				
			    parentPackageMaster = new ParentPackageMaster();
				parentPackageMaster.setParentPackageId(parentPackageId);
				parentPackageMaster.setVersionId(versionId);
				parentPackageMaster.setCreatedBy(dto.getCreatedBy());
				parentPackageMaster.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				parentPackageMaster.setMaxAge(dto.getMaxAge());
				parentPackageMaster.setMinAge(dto.getMinAge());
				parentPackageMaster.setOrgId(verifiedOrgId);
				parentPackageMaster.setPackageTypeId(dto.getPackageTypeId());
				parentPackageMaster.setServiceId(dto.getServiceId());
				parentPackageMaster.setSexId(dto.getSexId());
				parentPackageMaster.setStatus(ACTIVE);
				parentPackageMaster.setUnitId(verifiedUnitId);
				parentPackageMaster.setUpdatedBy(dto.getUpdatedBy());
				parentPackageMaster.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				parentPackageMaster
						.setValidityFromDate(dto.getValidityFromDate() != null && !dto.getValidityFromDate().isEmpty()
								? GlobalCommonDateUtils.getDate(dto.getValidityFromDate(), "dd-M-yyyy")
								: null);
				parentPackageMaster.setValidityPeriodIndays(dto.getValidityPeriodIndays());
				parentPackageMaster
						.setValidityToDate(dto.getValidityToDate() != null && !dto.getValidityToDate().isEmpty()
								? GlobalCommonDateUtils.getDate(dto.getValidityToDate(), "dd-M-yyyy")
								: null);
				parentPackageMaster.setNoOfEncounters(dto.getNoOfEncounters());
				sessionFactory.getCurrentSession().update(parentPackageMaster);
				
				
			} else {
				Integer versionId = 1;
				
			    parentPackageMaster = new ParentPackageMaster();
				parentPackageMaster.setVersionId(versionId);
				parentPackageMaster.setCreatedBy(dto.getCreatedBy());
				parentPackageMaster.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				parentPackageMaster.setMaxAge(dto.getMaxAge());
				parentPackageMaster.setMinAge(dto.getMinAge());
				parentPackageMaster.setOrgId(verifiedOrgId);
				parentPackageMaster.setPackageTypeId(dto.getPackageTypeId());
				parentPackageMaster.setServiceId(dto.getServiceId());
				parentPackageMaster.setSexId(dto.getSexId());
				parentPackageMaster.setStatus(ACTIVE);
				parentPackageMaster.setUnitId(verifiedUnitId);
				parentPackageMaster.setUpdatedBy(dto.getUpdatedBy());
				parentPackageMaster.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				parentPackageMaster
						.setValidityFromDate(dto.getValidityFromDate() != null && !dto.getValidityFromDate().isEmpty()
								? GlobalCommonDateUtils.getDate(dto.getValidityFromDate(), "dd-M-yyyy")
								: null);
				parentPackageMaster.setValidityPeriodIndays(dto.getValidityPeriodIndays());
				parentPackageMaster
						.setValidityToDate(dto.getValidityToDate() != null && !dto.getValidityToDate().isEmpty()
								? GlobalCommonDateUtils.getDate(dto.getValidityToDate(), "dd-M-yyyy")
								: null);
				parentPackageMaster.setNoOfEncounters(dto.getNoOfEncounters());
				parentPackageId = (Integer) sessionFactory.getCurrentSession().save(parentPackageMaster);
				
				PackageVersionMaster versionMaster = new PackageVersionMaster();
				versionMaster.setParentPackageId(parentPackageId);
				versionMaster.setVersionId(1);
				versionMaster.setCreatedBy(dto.getCreatedBy());
				versionMaster.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				versionMaster.setOrgId(verifiedOrgId);
				versionMaster.setStatus(ACTIVE);
				versionMaster.setUnitId(verifiedUnitId);
				versionMaster.setUpdatedBy(dto.getUpdatedBy());
				versionMaster.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty()
						? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss")
						: TimeZoneComponent.getDateByzone(verifiedUnitId));
				
				 sessionFactory.getCurrentSession().save(versionMaster);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, parentPackageMaster);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePackageMaster(MPackageMasterDto masterDto) throws ServiceException {
		try {

			// update
			MPackageMaster mPackageMaster = new MPackageMaster();
			mPackageMaster.setParentPackageId(masterDto.getParentPackageId());
			mPackageMaster.setBillingBedCategoryId(masterDto.getBillingBedCategoryId());
			mPackageMaster.setCreatedBy(masterDto.getCreatedBy());
			mPackageMaster.setCreatedDate(masterDto.getCreatedDate() != null && !masterDto.getCreatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(masterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
					: TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
			mPackageMaster.setIsManualRoundingIsAllow(masterDto.getIsManualRoundingIsAllow());
			mPackageMaster.setManualRoundoffAmount(masterDto.getManualRoundoffAmount());
			mPackageMaster.setMarkupDownInPercentage(masterDto.getMarkupDownInPercentage());
			mPackageMaster.setMaxAge(masterDto.getMaxAge());
			mPackageMaster.setMinAge(masterDto.getMinAge());
			mPackageMaster.setOrgId(masterDto.getOrgId());
			mPackageMaster.setPackageCost(masterDto.getPackageCost());
			mPackageMaster.setPackagePrice(masterDto.getPackagePrice());
			mPackageMaster.setPackageTypeId(masterDto.getPackageTypeId());
			mPackageMaster.setPatientTypeId(masterDto.getPatientTypeId());
			mPackageMaster.setPaymentEntitlementTypeId(masterDto.getPaymentEntitlementTypeId());
			mPackageMaster.setServiceId(masterDto.getServiceId());
			mPackageMaster.setSexId(masterDto.getSexId());
			mPackageMaster.setStatus(ACTIVE);
			mPackageMaster.setUnitId(masterDto.getUnitId());
			mPackageMaster.setUpdatedBy(masterDto.getUpdatedBy());
			mPackageMaster.setUpdatedDate(masterDto.getUpdatedDate() != null && !masterDto.getUpdatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(masterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss")
					: TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
			mPackageMaster.setValidityFromDate(
					masterDto.getValidityFromDate() != null && !masterDto.getValidityFromDate().isEmpty()
							? GlobalCommonDateUtils.getDate(masterDto.getValidityFromDate(), "dd-M-yyyy")
							: null);
			mPackageMaster.setValidityPeriodIndays(masterDto.getValidityPeriodIndays());
			mPackageMaster
					.setValidityToDate(masterDto.getValidityToDate() != null && !masterDto.getValidityToDate().isEmpty()
							? GlobalCommonDateUtils.getDate(masterDto.getValidityToDate(), "dd-M-yyyy")
							: null);
			mPackageMaster.setVisitTypeId(masterDto.getVisitTypeId());
			mPackageMaster.setTariffId(masterDto.getTariffId());
			mPackageMaster.setIsMarkupDown(masterDto.getIsMarkupDown());
			mPackageMaster.setRoundOffPositNeg(masterDto.getRoundOffPositNeg());
			mPackageMaster.setNoOfEncounters(masterDto.getNoOfEncounters());
			mPackageMaster.setVersionId(masterDto.getVisitTypeId());
			mPackageMaster = save(mPackageMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mPackageMaster);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchServiceForPackage(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String orderDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(reqDto.getUnitId()));

			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("SEARCH_SERVICE_FOR_PACKAGES").getQueryString());

			if (reqDto.getTariffId() != null) {
				query.append(" AND srvtrf.tariff_id = :tariffId");
			}
			if (reqDto.getPaymentEntitlementId() != null) {
				query.append(" AND srvtrf.payment_entitlement_id = :paymentEntitlementId");
			}
			if (reqDto.getVisitTypeId() != null) {
				query.append(" AND srvtrf.visit_type_id = :visitTypeId");
			}
			if (reqDto.getBillingBedCategoryId() != null) {
				query.append(" AND srvtrf.billing_bed_category_id = :billingBedCategoryId");
			}
			if (reqDto.getPatientTypeId() != null) {
				query.append(" AND srvtrf.patient_type_id = :patientTypeId");
			}
			if (reqDto.getIsDoctorRequired() != null) {
				query.append(" AND usmd.is_doc_req = :isDoctorRequired");
			}
			if (reqDto.getIsRefDoctorShare() != null) {
				query.append(" AND usmd.is_ref_doctor_share = :isRefDoctorShare");
			}
			if (reqDto.getGroupId() != null) {
				query.append(" AND sp.speciality_id = :groupId");
			}
			if (reqDto.getSubGroupId() != null) {
				query.append(" AND subsp.sub_speciality_id = :subGroupId");
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				query.append(" AND lower(trim(serv.service_standard_code)) LIKE :serviceCode");
			}
			if (reqDto.getServiceDescription() != null && !reqDto.getServiceDescription().isEmpty()) {
				query.append(" AND lower(trim(serv.service_standard_name)) LIKE :serviceDescription");
			}
			query.append(" AND to_char(srvtrf.effective_date, 'yyyy-mm-dd') <= :orderDate");
			query.append(" AND srvtrf.status = 'A'");
			query.append(" AND (usmd.gender = :sexId OR usmd.gender = 0)");
			query.append(" ORDER BY serv.service_standard_code, serv.service_master_id, srvtrf.effective_date DESC");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("orderDate", orderDate)
					.setParameter("sexId", reqDto.getSexId()!= null ? reqDto.getSexId() : 0);

// Set parameters conditionally
			if (reqDto.getTariffId() != null) {
				sqlQuery.setParameter("tariffId", reqDto.getTariffId());
			}
			if (reqDto.getPaymentEntitlementId() != null) {
				sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());
			}
			if (reqDto.getVisitTypeId() != null) {
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());
			}
			if (reqDto.getBillingBedCategoryId() != null) {
				sqlQuery.setParameter("billingBedCategoryId", reqDto.getBillingBedCategoryId());
			}
			if (reqDto.getPatientTypeId() != null) {
				sqlQuery.setParameter("patientTypeId", reqDto.getPatientTypeId());
			}
			if (reqDto.getIsDoctorRequired() != null) {
				sqlQuery.setParameter("isDoctorRequired", reqDto.getIsDoctorRequired());
			}
			if (reqDto.getIsRefDoctorShare() != null) {
				sqlQuery.setParameter("isRefDoctorShare", reqDto.getIsRefDoctorShare());
			}
			if (reqDto.getGroupId() != null) {
				sqlQuery.setParameter("groupId", reqDto.getGroupId());
			}
			if (reqDto.getSubGroupId() != null) {
				sqlQuery.setParameter("subGroupId", reqDto.getSubGroupId());
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				sqlQuery.setParameter("serviceCode", "%" + reqDto.getServiceCode().trim().toLowerCase() + "%");
			}
			if (reqDto.getServiceDescription() != null && !reqDto.getServiceDescription().isEmpty()) {
				sqlQuery.setParameter("serviceDescription", "%" + reqDto.getServiceDescription().trim().toLowerCase() + "%");
			}
			List<ServiceForPackageResDto> listServiceForPackageResDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ServiceForPackageResDto.class))
					.list();


			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForPackageResDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchServicesForPackage(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("SEARCH_SERVICE_FOR_PACKAGES_ADD").getQueryString()
			);
			if (reqDto.getGroupId() != null) {
				query.append(" AND speciality.speciality_id = :groupId");
			}
			if (reqDto.getSubGroupId() != null) {
				query.append(" AND sub_speciality.sub_speciality_id = :subGroupId");
			}
			if (reqDto.getSexId() != null) {
				query.append(" AND (details.gender = :sexId OR details.gender = 0)");
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				query.append(" AND lower(trim(service.service_standard_code)) LIKE :serviceCode");
			}
			if (reqDto.getServiceDescription() != null && !reqDto.getServiceDescription().isEmpty()) {
				query.append(" AND lower(trim(service.service_standard_name)) LIKE :serviceDescription");
			}
			query.append(" ORDER BY service.service_standard_name");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId());

			if (reqDto.getGroupId() != null) {
				sqlQuery.setParameter("groupId", reqDto.getGroupId());
			}
			if (reqDto.getSubGroupId() != null) {
				sqlQuery.setParameter("subGroupId", reqDto.getSubGroupId());
			}
			if (reqDto.getSexId() != null) {
				sqlQuery.setParameter("sexId", reqDto.getSexId());
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				sqlQuery.setParameter("serviceCode", "%" + reqDto.getServiceCode().trim().toLowerCase() + "%");
			}
			if (reqDto.getServiceDescription() != null && !reqDto.getServiceDescription().isEmpty()) {
				sqlQuery.setParameter("serviceDescription", "%" + reqDto.getServiceDescription().trim().toLowerCase() + "%");
			}

			List<ServiceForPackageResDto> listServiceForPackageResDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ServiceForPackageResDto.class))
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForPackageResDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchServiceForOrderSet(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_ORDER_SET_SERVICES").getQueryString()
			);
			if (reqDto.getGroupId() != null) {
				query.append(" AND sp.speciality_id = :groupId");
			}
			if (reqDto.getSubGroupId() != null) {
				query.append(" AND subsp.sub_speciality_id = :subGroupId");
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				query.append(" AND lower(trim(serv.service_standard_code)) LIKE :serviceCode");
			}
			if (reqDto.getServiceDescription() != null && !reqDto.getServiceDescription().isEmpty()) {
				query.append(" AND lower(trim(serv.service_standard_name)) LIKE :serviceDescription");
			}
			if (reqDto.getSexId() != null) {
				query.append(" AND (usmd.gender = :sexId OR usmd.gender = 0)");
			}
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId());

			if (reqDto.getGroupId() != null) {
				sqlQuery.setParameter("groupId", reqDto.getGroupId());
			}
			if (reqDto.getSubGroupId() != null) {
				sqlQuery.setParameter("subGroupId", reqDto.getSubGroupId());
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				sqlQuery.setParameter("serviceCode", "%" + reqDto.getServiceCode().trim().toLowerCase() + "%");
			}
			if (reqDto.getServiceDescription() != null && !reqDto.getServiceDescription().isEmpty()) {
				sqlQuery.setParameter("serviceDescription", "%" + reqDto.getServiceDescription().trim().toLowerCase() + "%");
			}
			if (reqDto.getSexId() != null) {
				sqlQuery.setParameter("sexId", reqDto.getSexId());
			}
			List<ServiceForPackageResDto> listServiceForPackageResDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ServiceForPackageResDto.class))
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForPackageResDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEHCPackagesList(ServiceForPackageReqDto reqDto) throws ApplicationException {
		try {
			List<ServiceForPackageReqDto> slotMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EHC_PACKAGES_LIST").setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId()).setParameter("age", reqDto.getAge())
					.setParameter("sexId", reqDto.getSexId())
					.setResultTransformer(Transformers.aliasToBean(ServiceForPackageReqDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("Error while fetching EHC packages list", he);
		} catch (Exception e) {
			logger.error("Error while fetching EHC packages list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchServicesForBilling(ServiceSearchReqDto reqDto) throws ServiceException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_PACKAGE_SERVICES_FOR_BILLING").getQueryString());

			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				query.append(" AND (LOWER(TRIM(service.service_standard_code)) LIKE :serviceCode OR LOWER(TRIM(service.service_standard_name)) LIKE :serviceCode)");
			}
			if (reqDto.getServiceName() != null && !reqDto.getServiceName().isEmpty()) {
				query.append(" AND (LOWER(TRIM(service.service_standard_name)) LIKE :serviceName OR LOWER(TRIM(service.service_standard_code)) LIKE :serviceName)");
			}
			if (reqDto.getVisitTypeId() != null) {
				query.append(" AND pack.visit_type_id = :visitTypeId");
			}
			if (reqDto.getPaymentEntitlementId() != null) {
				query.append(" AND pack.payment_entitlement_type_id = :paymentEntitlementId");
			}
			if (reqDto.getPatientTypeId() != null) {
				query.append(" AND pack.patient_type_id = :patientTypeId");
			}
			if (reqDto.getPackageTypeId() != null && reqDto.getPackageTypeId() > 0) {
				query.append(" AND pack.package_type_id = :packageTypeId");
			}
			if (reqDto.getBedBillingCategoryId() != null) {
				query.append(" AND pack.billing_bed_category_id = :bedBillingCategoryId");
			}
			query.append(" AND parent.status = 'A'");
			query.append(" AND pack.status = 'A'");
			if (reqDto.getGenderId() != null) {
				query.append(" AND (pack.sex_id = :genderId OR pack.sex_id = 0)");
			}

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("orgId", reqDto.getOrganisationId());

			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				sqlQuery.setParameter("serviceCode", "%" + reqDto.getServiceCode().trim().toLowerCase() + "%");
			}
			if (reqDto.getServiceName() != null && !reqDto.getServiceName().isEmpty()) {
				sqlQuery.setParameter("serviceName", "%" + reqDto.getServiceName().trim().toLowerCase() + "%");
			}
			if (reqDto.getVisitTypeId() != null) {
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());
			}
			if (reqDto.getPaymentEntitlementId() != null) {
				sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());
			}
			if (reqDto.getPatientTypeId() != null) {
				sqlQuery.setParameter("patientTypeId", reqDto.getPatientTypeId());
			}
			if (reqDto.getPackageTypeId() != null && reqDto.getPackageTypeId() > 0) {
				sqlQuery.setParameter("packageTypeId", reqDto.getPackageTypeId());
			}
			if (reqDto.getBedBillingCategoryId() != null) {
				sqlQuery.setParameter("bedBillingCategoryId", reqDto.getBedBillingCategoryId());
			}
			if (reqDto.getGenderId() != null) {
				sqlQuery.setParameter("genderId", reqDto.getGenderId());
			}

			List<ServiceSearchResDto> listServiceSearchResDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class))
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceSearchResDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageById(int packageId) throws ServiceException {
		try {
			MPackageMasterDto mPackage = (MPackageMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_BY_ID").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(MPackageMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mPackage);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageListByFilter(PackageListReqDto reqDto) throws ServiceException {
		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_LIST_BY_TYPE").getQueryString());

			if (reqDto.getPackageCode() != null && !reqDto.getPackageCode().isEmpty()) {
				query.append(" AND TRIM(LOWER(srv.service_standard_code)) LIKE :packageCode");
			}
			if (reqDto.getPackageName() != null && !reqDto.getPackageName().isEmpty()) {
				query.append(" AND TRIM(LOWER(srv.service_standard_name)) LIKE :packageName");
			}
			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty()) {
				query.append(" AND (LOWER(ptype.package_type) LIKE :searchKeyword OR "
						+ "LOWER(srv.service_standard_code) LIKE :searchKeyword OR "
						+ "LOWER(srv.service_standard_name) LIKE :searchKeyword)");
			}

			StringBuilder queryCount = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_LIST_COUNT").getQueryString());

			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty()) {
				queryCount.append(" AND (LOWER(ptype.package_type) LIKE :searchKeyword OR "
						+ "LOWER(srv.service_standard_code) LIKE :searchKeyword OR "
						+ "LOWER(srv.service_standard_name) LIKE :searchKeyword)");
			}
			if (reqDto.getPackageCode() != null && !reqDto.getPackageCode().isEmpty()) {
				queryCount.append(" AND TRIM(LOWER(srv.service_standard_code)) LIKE :packageCode");
			}
			if (reqDto.getPackageName() != null && !reqDto.getPackageName().isEmpty()) {
				queryCount.append(" AND TRIM(LOWER(srv.service_standard_name)) LIKE :packageName");
			}

			Query countQuery = sessionFactory.getCurrentSession().createSQLQuery(queryCount.toString())
					.setParameter("packageTypeId", reqDto.getPackageTypeId())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId());

			if (reqDto.getPackageCode() != null && !reqDto.getPackageCode().isEmpty()) {
				countQuery.setParameter("packageCode", "%" + reqDto.getPackageCode().trim().toLowerCase() + "%");
			}
			if (reqDto.getPackageName() != null && !reqDto.getPackageName().isEmpty()) {
				countQuery.setParameter("packageName", "%" + reqDto.getPackageName().trim().toLowerCase() + "%");
			}
			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty()) {
				countQuery.setParameter("searchKeyword", "%" + reqDto.getSearchKeyword().toLowerCase() + "%");
			}

			BigInteger count = (BigInteger) countQuery.uniqueResult();

			Query listQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("packageTypeId", reqDto.getPackageTypeId())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
					.setMaxResults(reqDto.getNoOfRecordsPerPage() != null ? reqDto.getNoOfRecordsPerPage() : 10);

			if (reqDto.getPackageCode() != null && !reqDto.getPackageCode().isEmpty()) {
				listQuery.setParameter("packageCode", "%" + reqDto.getPackageCode().trim().toLowerCase() + "%");
			}
			if (reqDto.getPackageName() != null && !reqDto.getPackageName().isEmpty()) {
				listQuery.setParameter("packageName", "%" + reqDto.getPackageName().trim().toLowerCase() + "%");
			}
			if (reqDto.getSearchKeyword() != null && !reqDto.getSearchKeyword().isEmpty()) {
				listQuery.setParameter("searchKeyword", "%" + reqDto.getSearchKeyword().toLowerCase() + "%");
			}

			List<PackageListResDto> listPackageListResDto = listQuery
					.setResultTransformer(Transformers.aliasToBean(PackageListResDto.class))
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPackageListResDto, count);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPackageLog(Integer packageConsumptionServiceDetailId) throws ServiceException {
		try {
			List<TPackageConsumptionServiceDetailDto> mPackage = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_LOG")
					.setParameter("packageConsumptionServiceDetailId", packageConsumptionServiceDetailId)
					.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionServiceDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mPackage);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageListForIPDPackageOrder(Integer patientId,Integer admissionId, Integer orgId, Integer unitId,
			Integer visiitTypeId) throws ServiceException {
		try 
		{
			List<PackageConsumptionMasterResDto> list = null;
			if(admissionId!=null && admissionId>0) {
			Date date = TimeZoneComponent.getDateByzone(unitId);
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_LIST_FOR_IPD_PACKAGE_ORDER").getQueryString());
			query.append("	order by \"packageConsumptionMasterId\" ");
			 list = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("patientId", patientId)
					.setParameter("encounterId", admissionId)
					.setParameter("visitTypeId",visiitTypeId)
					.setParameter("organisationId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionMasterResDto.class))
					.list();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkServiceAssignedOrNot(Integer orgId, Integer unitId, Integer serviceId)
			throws ServiceException {
		try {
			BigInteger serviceCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_PACKAGE_SERVICE_EXISTS_OR_NOT").setParameter("orgId", orgId)
					.setParameter("unitId", unitId).setParameter("serviceId", serviceId).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, serviceCount);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response renderPackageService(OrderDetailsMaster dto,AdmissionsForBatchProcessDto vistDetailsObj ,Integer packageId)
			throws ApplicationException {
		try {
			
			OrderDetailsMaster master =null;
			if(vistDetailsObj.getAdmissionId()!=null && vistDetailsObj.getAdmissionId()>0 )
			{
				 master = (OrderDetailsMaster) sessionFactory.getCurrentSession().createSQLQuery("select "
						+ "	od.order_details_id as \"orderDetailsId\", cast(pm.package_price as numeric) as \"serviceAmt\" "
						+ " from " + "	public.t_order_details od "
						+ "inner join public.t_order_master om on om.order_id = od.order_id "
						+ "inner join service.m_package_master pm on od.service_id = pm.service_id "
						+ "inner join billing.t_package_consumption_master pcm on "
						+ "	pm.package_master_id = pcm.package_id " 
						+ " where " 
						+ "	pm.package_master_id = :packageId "
						+ "	and od.service_rendered = 0 " 
						+ "	and pcm.admission_id = :admissionId "
						+ "	and om.admission_id = :admissionId " + "	and od.is_package = true "
						+ "	and od.status = 'A' order by  od.order_details_id limit 1")
						.setParameter("packageId", packageId).setParameter("admissionId", vistDetailsObj.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsMaster.class)).uniqueResult();
			}
			else if(vistDetailsObj.getEncounterId()!=null && vistDetailsObj.getEncounterId()>0 )
			{
				 master = (OrderDetailsMaster) sessionFactory.getCurrentSession().createSQLQuery("select "
						+ "	od.order_details_id as \"orderDetailsId\", cast(pm.package_price as numeric) as \"serviceAmt\" "
						+ " from " + "	public.t_order_details od "
						+ " inner join public.t_order_master om on om.order_id = od.order_id "
						+ " inner join service.m_package_master pm on od.service_id = pm.service_id "
						+ " inner join billing.t_package_consumption_master pcm on "
						+ "	pm.package_master_id = pcm.package_id " 
						+ " where " 
						+ "	pm.package_master_id = :packageId "
						+ "	and od.service_rendered = 0 " 
						+ "	and pcm.encounter_id = :encounterId "
						+ "	and om.encounter_id = :encounterId " 
						+ "	and od.is_package = true "
						+ "	and od.status = 'A' order by  od.order_details_id limit 1")
						.setParameter("packageId", packageId).setParameter("encounterId", vistDetailsObj.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsMaster.class)).uniqueResult();
			}

			if (master != null && master.getOrderDetailsId() != 0) {
				OrderDetailsMaster mst = (OrderDetailsMaster) sessionFactory.getCurrentSession()
						.get(OrderDetailsMaster.class, master.getOrderDetailsId());
				mst.setServiceAmt(master.getServiceAmt());
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mst);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchPackageCommonFilter(PackageSearchRequestDto searchDto) throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("SEARCH_PACKAGE_COMMON_FILTER").getQueryString());
			if (searchDto.getPackageCode() != null && !searchDto.getPackageCode().isEmpty()) {
				query.append(" AND LOWER(srvismstr.service_standard_code) LIKE :packageCode");
			}
			if (searchDto.getPackageName() != null && !searchDto.getPackageName().isEmpty()) {
				query.append(" AND LOWER(srvismstr.service_standard_name) LIKE :packageName");
			}
			query.append(" ORDER BY parentpckgmstr.parent_package_id DESC");
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("unitId", searchDto.getUnitId())
					.setParameter("packageTypeId", searchDto.getPackageTypeId());

			if (searchDto.getPackageCode() != null && !searchDto.getPackageCode().isEmpty()) {
				sqlQuery.setParameter("packageCode", "%" + searchDto.getPackageCode().trim().toLowerCase() + "%");
			}
			if (searchDto.getPackageName() != null && !searchDto.getPackageName().isEmpty()) {
				sqlQuery.setParameter("packageName", "%" + searchDto.getPackageName().trim().toLowerCase() + "%");
			}
			List<MPackageMasterDto> list = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(MPackageMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while searching package by common filter", he);
		} catch (Exception e) {
			logger.error("Error while searching package by common filter", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);

	}

	@Override
	public Response getPackageServicesByPackageId(PackageSearchRequestDto searchDto) throws ApplicationException {
		try {

		} catch (HibernateException he) {
			logger.error("Error while fetching package services by package id", he);
		} catch (Exception e) {
			logger.error("Error while fetching package services by package id", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveEhcEncounterEhcConsultationMaster(EncounterEhcConsultationMasterDto dto) throws Exception {
		try {
			EncounterEhcConsultationMaster master = new EncounterEhcConsultationMaster();
			master.setEncounterId(dto.getEncounterId());
			master.setOrderDtailsId(dto.getOrderDtailsId());
			master.setPackageId(dto.getPackageId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setServiceId(dto.getServiceId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss").getTime()
					: TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss").getTime()
					: TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
			Integer ehcConsultationMasterId = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, ehcConsultationMasterId);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	public Response saveEhcEncounterEhcConsultation(EncounterEhcConsultationMpprDto dto) throws Exception {
		try {
			EncounterEhcConsultationMppr master = new EncounterEhcConsultationMppr();
			master.setEncounterId(dto.getEncounterId());
			master.setConsultationSpecialityId(dto.getSpecialityId());
			master.setOrderDtailsId(dto.getOrderDetailsId());
			master.setPackageId(dto.getPackageId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setConsultationMasterId(dto.getConsultationMasterId());
			master.setUnitId(dto.getUnitId());
			master.setDoctorId(dto.getDoctorId());
			master.setAppointmentStatusId(APPOINTMENTSTATUSID1);
			master.setServiceId(dto.getServiceId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss").getTime()
					: TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss").getTime()
					: TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
			Integer subEncounterId = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, subEncounterId);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEhcConsultationServices(EncounterMasterDto dto) throws Exception {
		try {
			List<EncounterEhcConsultationMpprDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EHC_CONSULTATION_BY_ENCOUNTER_ID")
					 .setParameter("encounterId", dto.getEncounterId())
					.setParameter("packageId", dto.getEhcPackageId())
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EncounterEhcConsultationMpprDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchServiceForPackageByMultipleCategory(ServiceForPackageReqDto reqDto, Set<Integer> serviceIds)
			throws ServiceException {
		try {
			if (serviceIds != null && serviceIds.size() > 0) {
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String orderDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(reqDto.getUnitId()));

				String Q = "select " + "	serv.service_master_id AS \"serviceMasterId\", "
						+ "	sp.speciality_id AS \"specialityId\", " + "	sp.speciality_name AS \"groupDesc\", "
						+ "	subsp.sub_speciality_id AS \"subSpecialityMasterId\", "
						+ "	subsp.sub_speciality_name AS \"subGroup\", "
						+ "	serv.service_standard_code AS \"serviceCode\", "
						+ "	serv.service_standard_name AS \"serviceDescription\" , "
						+ "	petm.payment_entitlement_desc  AS \"paymentEntitlement\", "
						+ "	pcm.patient_category AS \"patientType\", "
						+ "	vistmst.visit_type_code  AS \"visitTypeName\", "
						+ "	bbcm.billing_bed_desc  AS \"billingBedCategory\", "
						+ "	coalesce(srvtrf.rate, bprice.base_price) AS \"rate\", "
						+ "	To_char(srvtrf.effective_date, 'DD-MM-YYYY') AS \"effectiveDate\", "
						+ "	usmd.product_category_id AS \"productCategoryId\" " + " from "
						+ "	service.m_service_master serv "
						+ "	inner join service.m_service_base_price_master bprice on serv.service_master_id =  bprice.service_master_id "
						+ " inner join doctor.m_speciality_master sp on " + "	sp.speciality_id = serv.speciality_id "
						+ " inner join doctor.m_sub_speciality_master subsp on "
						+ "	subsp.sub_speciality_id = serv.sub_speciality_id "
						+ " inner join service.t_unit_service_master_details usmd on "
						+ "	usmd.service_master_id = serv.service_master_id "
						+ " left join service.m_unit_service_tariff_master srvtrf on "
						+ "	srvtrf.service_id = serv.service_master_id ";

				StringBuilder query = new StringBuilder(Q);
				StringBuilder maxEffectiveDate = new StringBuilder(
						sessionFactory.getCurrentSession().getNamedQuery("GET_MAX_TERRIF_DATE").getQueryString());
				if (reqDto.getTariffId() != null) {

					maxEffectiveDate.append(
							reqDto.getTariffId() != null ? " and srvtrf.tariff_id =" + reqDto.getTariffId() : " ");
					// maxEffectiveDate.append(reqDto.getPaymentEntitlementId() != null ? " and
					// srvtrf.payment_entitlement_id ="+reqDto.getPaymentEntitlementId() : " ");
					maxEffectiveDate.append(
							reqDto.getPaymentEntitlementId() != null ? " and srvtrf.payment_entitlement_id = 1" : " ");
					maxEffectiveDate.append(reqDto.getPatientTypeId() != null
							? " and srvtrf.patient_type_id =" + reqDto.getPatientTypeId()
							: " ");
					maxEffectiveDate.append(
							reqDto.getVisitTypeId() != null ? " and srvtrf.visit_type_id = " + reqDto.getVisitTypeId()
									: " ");
					maxEffectiveDate.append(reqDto.getBillingBedCategoryId() != null
							? " and srvtrf.billing_bed_category_id = " + reqDto.getBillingBedCategoryId()
							: " ");
				}

				query.append(reqDto.getTariffId() != null ? " and srvtrf.tariff_id =" + reqDto.getTariffId() : " ");
				query.append(reqDto.getPaymentEntitlementId() != null ? " and srvtrf.payment_entitlement_id =1" : "");
				query.append(
						reqDto.getPatientTypeId() != null ? " and srvtrf.patient_type_id =" + reqDto.getPatientTypeId()
								: " ");
				query.append(reqDto.getVisitTypeId() != null ? " and srvtrf.visit_type_id = " + reqDto.getVisitTypeId()
						: " ");
				query.append(reqDto.getBillingBedCategoryId() != null
						? " and srvtrf.billing_bed_category_id = " + reqDto.getBillingBedCategoryId()
						: " ");

				query.append(reqDto.getIsDoctorRequired() != null
						? " and usmd.is_doc_req ='" + reqDto.getIsDoctorRequired() + "'"
						: " ");
				query.append(reqDto.getGroupId() != null ? " and sp.speciality_id =" + reqDto.getGroupId() : " ");
				query.append(reqDto.getSubGroupId() != null ? " and subsp.sub_speciality_id =" + reqDto.getSubGroupId()
						: " ");

				maxEffectiveDate.append(reqDto.getIsDoctorRequired() != null
						? " and usmd.is_doc_req ='" + reqDto.getIsDoctorRequired() + "'"
						: " ");
				maxEffectiveDate
						.append(reqDto.getGroupId() != null ? " and sp.speciality_id =" + reqDto.getGroupId() : " ");
				maxEffectiveDate.append(
						reqDto.getSubGroupId() != null ? " and subsp.sub_speciality_id =" + reqDto.getSubGroupId()
								: " ");
				maxEffectiveDate.append(" and srvtrf.effective_date < '" + orderDate + "'");
				maxEffectiveDate.append(" and srvtrf.service_id in (:serviceIds)");

				query.append(" and srvtrf.effective_date = (" + maxEffectiveDate + ")");
				query.append(" and srvtrf.status = 'A'");

				query.append(reqDto.getTariffId() != null ? " and srvtrf.tariff_id =" + reqDto.getTariffId() : " ");
				query.append(" left join public.m_payment_entitlement_master petm on "
						+ "	petm.payment_entitlement_id = srvtrf.payment_entitlement_id ");
				query.append(reqDto.getPaymentEntitlementId() != null ? " and srvtrf.payment_entitlement_id =1" : "");

				query.append(" left join public.m_patient_category_master pcm on "
						+ "	pcm.patient_category_id = srvtrf.patient_type_id ");
				query.append(
						reqDto.getPatientTypeId() != null ? " and srvtrf.patient_type_id =" + reqDto.getPatientTypeId()
								: " ");

				query.append(" left join public.m_visit_type_master vistmst on "
						+ "	vistmst.visit_type_id = srvtrf.visit_type_id ");
				query.append(reqDto.getVisitTypeId() != null ? " and srvtrf.visit_type_id = " + reqDto.getVisitTypeId()
						: " ");

				query.append(" left join adt.m_billing_bed_category_master bbcm on "
						+ " bbcm.billing_bed_category_id = srvtrf.billing_bed_category_id");
				query.append(reqDto.getBillingBedCategoryId() != null
						? " and srvtrf.billing_bed_category_id = " + reqDto.getBillingBedCategoryId()
						: " ");

				query.append(" where   usmd.unit_id = bprice.unit_id and usmd.unit_id =:unitId");
				query.append(" and ( usmd.gender=" + reqDto.getSexId() + " OR usmd.gender=0)");
				query.append(" and serv.service_master_id in (:serviceIds) and usmd.status = 'A' ");
				query.append(" order by serv.service_standard_code, serv.service_master_id ");
				List<ServiceForPackageResDto> listServiceForPackageResDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString()).setParameter("orgId", reqDto.getOrgId())
						.setParameter("unitId", reqDto.getUnitId()).setParameterList("serviceIds", serviceIds)
						.setResultTransformer(Transformers.aliasToBean(ServiceForPackageResDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForPackageResDto, null);
			}
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response searchItemsForPackageByMultipleCategory(ServiceForPackageReqDto reqDto, Set<Integer> itemIds)
			throws ServiceException {
		try {
			if (itemIds != null && itemIds.size() > 0) {
				List<ServiceForPackageResDto> itemList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ITEM_BY_ITEM_IDS").setParameter("orgId", reqDto.getOrgId())
						.setParameter("unitId", reqDto.getUnitId()).setParameterList("itemIds", itemIds)
						.setResultTransformer(Transformers.aliasToBean(ServiceForPackageResDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, itemList, null);
			}
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getParentPacakgeById(Integer parentPackageId) throws ServiceException {
		try {
			ParentPackageMasterDto parentPackageDto = (ParentPackageMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PARENT_PACAKGE_BY_ID").setParameter("parentPackageId", parentPackageId)
					.setResultTransformer(Transformers.aliasToBean(ParentPackageMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, parentPackageDto);

		} catch (HibernateException he) {
			logger.error("Error while fetching parent package by id", he);
		} catch (Exception e) {
			logger.error("Error while fetching parent package by id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPacakgeByParentId(Integer parentPackageId) throws ServiceException {
		try {
			List<MPackageMasterDto> packageList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACAKGE_BY_PARENT_ID").setParameter("parentPackageId", parentPackageId)
					.setResultTransformer(Transformers.aliasToBean(MPackageMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, packageList, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response inactivePacakge(Integer parentPackageId, Integer packageTypeId,boolean flag,int fromMethod) throws ServiceException {
 	try {

 		// BR-160: cross-tenant IDOR fix (package status-toggle, write-by-ID
 		// variant) -- the UPDATEs below keyed ONLY by parent_package_id, so any
 		// authenticated session could deactivate ANY tenant's package + all its
 		// child rows (live-confirmed: org-1 session deactivated an org-999 victim
 		// package A->I via /api/packages/inActivePackage). Guard at entry: the
 		// parent package must belong to the JWT-verified org/unit, else reject.
 		Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
 		Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
 		Number ownerCount = (Number) sessionFactory.getCurrentSession().createSQLQuery(
 				"select count(*) from service.m_parent_package_master "
 				+ "where parent_package_id =:parentPackageId and org_id =:orgId and unit_id =:unitId")
 				.setParameter("parentPackageId", parentPackageId)
 				.setParameter("orgId", verifiedOrgId)
 				.setParameter("unitId", verifiedUnitId)
 				.uniqueResult();
 		if (ownerCount == null || ownerCount.longValue() == 0) {
 			return new Response(ERROR, ERROR_CODE, "Package not found for the verified tenant", null, null);
 		}

 		Character status=flag ? 'A' : 'I'; 
 		Integer inactiveCount = 0;
			if(fromMethod==1){
				
				 inactiveCount = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.m_package_master set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();
				
			}else{
				
				 inactiveCount = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.m_package_master set status = '"+status+"' where package_master_id  =  "
								+ " (select package_master_id from service.m_package_master where parent_package_id  = :parentPackageId order by 1 desc limit 1)  ")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();
				
			}
			
			

			switch (packageTypeId) {
			case 1:
				// EHC package
				Integer ehcCount = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_services_details set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();
				break;
			case 2:
				// Either or Package
				Integer service = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_services_details set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();

				Integer inclusionEx = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_eitheror_group_details set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();

				break;
			case 3: // package with cap

				Integer capService = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_services_details set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();

				Integer capInclusiveExlusive = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_inc_exc_details set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();

				Integer capCount = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_cap_details set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();

				Integer bedCategoryCount = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_bed_category_detail set status = '"+status+"' where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId).executeUpdate();

				Integer consumableDetails = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_category_wise_consumable_details set status =:status where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId)
						.setParameter("status", String.valueOf(status))
						.executeUpdate();

				break;
			case 4: // All exclusive package
				Integer allInclusive = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_inc_exc_details set status =:status where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId)
						.setParameter("status", String.valueOf(status))
						.executeUpdate();
				break;
			case 5: // multi encounter package

				Integer serviceme = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_services_details set status =:status where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId)
						.setParameter("status", String.valueOf(status))
						.executeUpdate();

				Integer inclusionExclusionMe = sessionFactory.getCurrentSession().createSQLQuery(
						"update service.t_package_inc_exc_details set status =:status where parent_package_id  =:parentPackageId")
						.setParameter("parentPackageId", parentPackageId)
						.setParameter("status", String.valueOf(status))
						.executeUpdate();
				break;
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, inactiveCount);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response packageSearchForBilling(ServiceSearchReqDto reqDto) throws ServiceException {
		try {



			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("SEARCH_PACKAGE_FOR_BILLING").getQueryString());

			Map<String, Object> parameters = new HashMap<>();

// Conditionally append query conditions and add parameters
			if (reqDto.getVisitTypeId() != null) {
				query.append(" and pack.visit_type_id = :visitTypeId");
				parameters.put("visitTypeId", reqDto.getVisitTypeId());
			}
			if (reqDto.getPaymentEntitlementId() != null) {
				query.append(" and pack.payment_entitlement_type_id = :paymentEntitlementId");
				parameters.put("paymentEntitlementId", reqDto.getPaymentEntitlementId());
			}
			if (reqDto.getPatientTypeId() != null && reqDto.getPatientTypeId() > 0) {
				query.append(" and pack.patient_type_id = :patientTypeId");
				parameters.put("patientTypeId", reqDto.getPatientTypeId());
			}
			if (reqDto.getBedBillingCategoryId() != null) {
				query.append(" and pack.billing_bed_category_id = :bedBillingCategoryId");
				parameters.put("bedBillingCategoryId", reqDto.getBedBillingCategoryId());
			}
			if (reqDto.getGenderId() != null) {
				query.append(" and (parentMstr.sex_id = :genderId or parentMstr.sex_id = 0)");
				parameters.put("genderId", reqDto.getGenderId());
			}
			if (reqDto.getPackageTypeId() != null && reqDto.getPackageTypeId().intValue() == 1) {
				query.append(" and parentMstr.package_type_id = 1");
			}
			if (reqDto.getAge() != null) {
				query.append(" and (parentMstr.min_age >= :age and parentMstr.max_age <= :age)");
				parameters.put("age", reqDto.getAge());
			}
			if (reqDto.getServiceName() != null && !reqDto.getServiceName().isEmpty()) {
				query.append(" and (lower(trim(service.service_standard_name)) like :serviceName " +
						"or lower(trim(service.service_standard_code)) like :serviceName)");
				parameters.put("serviceName", "%" + reqDto.getServiceName().toLowerCase().trim() + "%");
			}
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				query.append(" and (lower(trim(service.service_standard_name)) like :serviceCode " +
						"or lower(trim(service.service_standard_code)) like :serviceCode)");
				parameters.put("serviceCode", "%" + reqDto.getServiceCode().toLowerCase().trim() + "%");
			}
			query.append(" order by pack.package_master_id desc");
			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class));
			sqlQuery.setParameter("unitId", reqDto.getUnitId());
			sqlQuery.setParameter("orgId", reqDto.getOrganisationId());

			if (reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if (reqDto.getPaymentEntitlementId() != null)
				sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());

			if (reqDto.getPatientTypeId() != null && reqDto.getPatientTypeId() > 0)
				sqlQuery.setParameter("patientTypeId", reqDto.getPatientTypeId());

			if (reqDto.getBedBillingCategoryId() != null)
				sqlQuery.setParameter("bedBillingCategoryId", reqDto.getBedBillingCategoryId());

			if (reqDto.getGenderId() != null)
				sqlQuery.setParameter("genderId", reqDto.getGenderId());

			if (reqDto.getAge() != null)
				sqlQuery.setParameter("age", reqDto.getAge());

			if (reqDto.getServiceName() != null && !reqDto.getServiceName().isEmpty())
				sqlQuery.setParameter("serviceName", "%" + reqDto.getServiceName().toLowerCase().trim() + "%");

			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty())
				sqlQuery.setParameter("serviceCode", "%" + reqDto.getServiceCode().toLowerCase().trim() + "%");


			List<ServiceSearchResDto> listServiceSearchResDto = sqlQuery.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceSearchResDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response parentPackage(ServiceSearchReqDto reqDto) throws ServiceException {
		try {
			String baseQuery = "select " + "	service.service_master_id as \"serviceMasterId\", "
					+ "	service.service_standard_name as \"serviceStandardName\" , "
					+ "	service.service_standard_code as \"serviceStandardCode\", "
					+ "	parentMstr.parent_package_id as \"parentPackageId\" " + " from "
					+ "	service.m_parent_package_master parentMstr "
					+ "	inner join service.m_service_master service on service.service_master_id  = parentMstr.service_id "
					+ " where " + "	parentMstr.unit_id =:unitId " + "	and parentMstr.org_id =:orgId "
					+ "	and parentMstr.status = 'A' " + "	and parentMstr.package_type_id = 1";
			StringBuilder query = new StringBuilder(baseQuery);
			Map<String, Object> parameters = new HashMap<>();
			if (reqDto.getServiceCode() != null && !reqDto.getServiceCode().isEmpty()) {
				query.append("and lower(trim(service.service_standard_code)) like :serviceCode ");
				parameters.put("serviceCode", "%" + reqDto.getServiceCode().toLowerCase().trim() + "%");
			}
			if (reqDto.getServiceName() != null && !reqDto.getServiceName().isEmpty()) {
				query.append("or lower(trim(service.service_standard_name)) like :serviceName ");
				parameters.put("serviceName", "%" + reqDto.getServiceName().toLowerCase().trim() + "%");
			}
			Query hqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class));
			hqlQuery.setParameter("unitId", reqDto.getUnitId());
			hqlQuery.setParameter("orgId", reqDto.getOrganisationId());
			parameters.forEach(hqlQuery::setParameter);
			List<ServiceSearchResDto> listServiceSearchResDto = hqlQuery.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceSearchResDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageRate(ServiceForPackageReqDto reqDto) throws ServiceException 
	{
		try {

			// Base query with escaped double quotes
			String q = "select service.service_master_id as \"serviceMasterId\", " +
					"   service.service_standard_name as \"serviceStandardName\", " +
					"   service.service_standard_code as \"serviceStandardCode\", " +
					"   pkg.package_master_id as \"packageMasterId\", " +
					"   pkg.package_price as \"packagePrice\" " +
					"from " +
					"   service.m_package_master pkg " +
					"   inner join service.m_service_master service on service.service_master_id = pkg.service_id " +
					"where " +
					"   pkg.unit_id = :unitId " +
					"   and pkg.org_id = :orgId " +
					"   and pkg.status = 'A' ";

			StringBuilder query = new StringBuilder(q);

			Map<String, Object> parameters = new HashMap<>();
			if (reqDto.getServiceId() != null && reqDto.getServiceId() > 0) {
				query.append(" and service_id = :serviceId");
				parameters.put("serviceId", reqDto.getServiceId());
			}
			if (reqDto.getPaymentEntitlementId() != null && reqDto.getPaymentEntitlementId() > 0) {
				query.append(" and payment_entitlement_type_id = :paymentEntitlementId");
				parameters.put("paymentEntitlementId", reqDto.getPaymentEntitlementId());
			}
			if (reqDto.getPatientTypeId() != null && reqDto.getPatientTypeId() > 0) {
				query.append(" and patient_type_id = :patientTypeId");
				parameters.put("patientTypeId", reqDto.getPatientTypeId());
			}
			if (reqDto.getBillingBedCategoryId() != null && reqDto.getBillingBedCategoryId() > 0) {
				query.append(" and billing_bed_category_id = :billingBedCategoryId");
				parameters.put("billingBedCategoryId", reqDto.getBillingBedCategoryId());
			}
			if (reqDto.getVisitTypeId() != null && reqDto.getVisitTypeId() > 0) {
				query.append(" and visit_type_id = :visitTypeId");
				parameters.put("visitTypeId", reqDto.getVisitTypeId());
			}
			query.append(" limit 1");
			Query hqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class));
			hqlQuery.setParameter("unitId", reqDto.getUnitId());
			hqlQuery.setParameter("orgId", reqDto.getOrgId());
			parameters.forEach(hqlQuery::setParameter);
			List<ServiceSearchResDto> listServiceSearchResDto = hqlQuery.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceSearchResDto, null);
		} catch (HibernateException he) {
			logger.error("Error while fetching package rate", he);
		} catch (Exception e) {
			logger.error("Error while fetching package rate", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updatePackageService(SlotMasterDto slotMasterDto) throws ServiceException {
		try {
			// for updating package id for ehc packages start
			if (slotMasterDto.getEhcPackageId() != null && slotMasterDto.getEhcPackageId() > 0) {
				sessionFactory.getCurrentSession()
						.createQuery("update AppointmentMaster set " + " ehcPackageId=:ehcPackageId , serviceId=:serviceId"
								+ " WHERE appointmentId=:appointmentId ")
						.setParameter("ehcPackageId", slotMasterDto.getEhcPackageId())
						.setParameter("serviceId", slotMasterDto.getServiceId())
						.setParameter("appointmentId", slotMasterDto.getAppointmentId()).executeUpdate();
			}

			// for updating package id for ehc packages end
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			logger.error("Error while updating package service", he);
		} catch (Exception e) {
			logger.error("Error while updating package service", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Integer cancelPackageService(ContractConsumptionDto consumptionDto) throws ServiceException {
		try {
			
			Integer result  =1;
			if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty() && consumptionDto.getForceAddRemoved().intValue()==0)
			{
				for (OrderDetailsPackageMapperDto dto : consumptionDto.getOrderDetailsPackageMapperDtosList()) {
					if(dto.getIsPackageService()=='Y')
					{
						if(dto.getPackageConsumptionEODetailId()!=null && dto.getPackageConsumptionEODetailId()>0 && dto.getPackageQty()!=null && dto.getPackageQty()>0)
						{
							iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(dto.getPackageConsumptionEODetailId(), dto.getPackageQty(),false);
						}
						if(dto.getPacakgeConsumptionServiceDetailId()!=null && dto.getPacakgeConsumptionServiceDetailId()>0) 
						{
							if(dto.getPackageQty()!=null && dto.getPackageQty()>0)
							{
								iTPackageConsumptionCapDetailsDao.updatePackageQty(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageQty());
							}
							else if(dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
								{
									iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageAmount().doubleValue());
								}
						}
						if(dto.getPackageConsumptionCapDetailsId()!=null && dto.getPackageConsumptionCapDetailsId()>0 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0)
						{
							iTPackageConsumptionCapDetailsDao.updatePackageCap(dto.getPackageConsumptionCapDetailsId(), dto.getPackageAmount().doubleValue());
						}
					}
				}
			}
			return result;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer cancelPackageItem(CommonBillingItemsDto item,boolean isRecalcualtion, boolean pharmacyReturnServiceCal) throws ServiceException {
   try {			
			Integer result  =1;
			if(item.getSaleDetailPackageMapperDtoList()!=null && item.getSaleDetailPackageMapperDtoList().size()>0)
			{
				for (SaleDetailPackageMapperDto dto : item.getSaleDetailPackageMapperDtoList())
				{
					if(dto.getIsPackageItem()=='Y')
					{
						if(dto.getPackageConsumptionEODetailId()!=null && dto.getPackageConsumptionEODetailId()>0 && dto.getPackageQty()!=null && dto.getPackageQty()>0)
						{
							iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(dto.getPackageConsumptionEODetailId(), dto.getPackageQty(),isRecalcualtion);
						}
						if(dto.getPacakgeConsumptionServiceDetailId()!=null && dto.getPacakgeConsumptionServiceDetailId()>0) 
						{
							if(dto.getCapOrQty()==1 &&  dto.getPackageQty()!=null && dto.getPackageQty()>0)
							{
								iTPackageConsumptionCapDetailsDao.updatePackageCapItem(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageAmount(),isRecalcualtion);
							}
							else if(dto.getCapOrQty()==2 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
								{
									iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageQty(), dto.getPackageAmount(),isRecalcualtion);
								}
						}
						if(dto.getPackageConsumptionCapDetailsId()!=null && dto.getPackageConsumptionCapDetailsId()>0 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0)
						{
							if(dto.getCapOrQty()==1 &&  dto.getPackageQty()!=null && dto.getPackageQty()>0)
							{
								iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(dto.getPackageConsumptionCapDetailsId(), dto.getPackageAmount(),isRecalcualtion,pharmacyReturnServiceCal);
							}
							else if(dto.getCapOrQty()==2 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
								{
									iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(dto.getPackageConsumptionCapDetailsId(), dto.getPackageQty(), dto.getPackageAmount(),isRecalcualtion,pharmacyReturnServiceCal);
								}
						}
					}
				}
			}
			return result;

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Integer consumePackageItem(CommonBillingItemsDto item,boolean isRecalcualtion) throws ServiceException {
		 try {			
				Integer result  =1;
				int value = -1;
				if(item.getSaleDetailPackageMapperDtoList()!=null && item.getSaleDetailPackageMapperDtoList().size()>0)
				{
					for (SaleDetailPackageMapperDto dto : item.getSaleDetailPackageMapperDtoList())
					{
						if(dto.getIsPackageItem()=='Y')
						{
							if(dto.getPackageConsumptionEODetailId()!=null && dto.getPackageConsumptionEODetailId()>0 && dto.getPackageQty()!=null && dto.getPackageQty()>0)
							{
								iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(dto.getPackageConsumptionEODetailId(), dto.getPackageQty()*value,isRecalcualtion);
							}
							if(dto.getPacakgeConsumptionServiceDetailId()!=null && dto.getPacakgeConsumptionServiceDetailId()>0) 
							{
								if(dto.getCapOrQty()==1 &&  dto.getPackageQty()!=null && dto.getPackageQty()>0)
								{
									iTPackageConsumptionCapDetailsDao.updatePackageCapItem(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageAmount()*value,isRecalcualtion);
								}
								else if(dto.getCapOrQty()==2 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
									{
										iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageQty()*value, dto.getPackageAmount()*value,isRecalcualtion);
									}
							}
							if(dto.getPackageConsumptionCapDetailsId()!=null && dto.getPackageConsumptionCapDetailsId()>0 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0)
							{
								if(dto.getCapOrQty()==1 &&  dto.getPackageQty()!=null && dto.getPackageQty()>0)
								{
									iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(dto.getPackageConsumptionCapDetailsId(), dto.getPackageAmount()*value,isRecalcualtion,false);
								}
								else if(dto.getCapOrQty()==2 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
									{
										iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(dto.getPackageConsumptionCapDetailsId(), dto.getPackageQty()*value, dto.getPackageAmount()*value,isRecalcualtion,false);
									}
							}
						}
					}
				}
				return result;

			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				
				throw e;
			}
	}
	
	@Override
	public Integer addPackageItemForReturn(CommonBillingItemsDto item) throws ServiceException {
		 try {			
				if(item.getSaleDetailPackageMapperDtoList()!=null && item.getSaleDetailPackageMapperDtoList().size()>0)
				{
					for (SaleDetailPackageMapperDto dto : item.getSaleDetailPackageMapperDtoList())
					{
						if(dto.getIsPackageItem()=='Y')
						{
							if(dto.getPackageConsumptionEODetailId()!=null && dto.getPackageConsumptionEODetailId()>0 && dto.getPackageQty()!=null && dto.getPackageQty()>0)
							{
								iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(dto.getPackageConsumptionEODetailId(), dto.getPackageQty(),false);
							}
							if(dto.getPacakgeConsumptionServiceDetailId()!=null && dto.getPacakgeConsumptionServiceDetailId()>0) 
							{
								if(dto.getCapOrQty()==1 &&  dto.getPackageQty()!=null && dto.getPackageQty()>0)
								{
									iTPackageConsumptionCapDetailsDao.updatePackageCapItem(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageAmount(),false);
								}
								else if(dto.getCapOrQty()==2 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
									{
										iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(dto.getPacakgeConsumptionServiceDetailId(), dto.getPackageQty(), dto.getPackageAmount(),false);
									}
							}
							if(dto.getPackageConsumptionCapDetailsId()!=null && dto.getPackageConsumptionCapDetailsId()>0 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0)
							{
								if(dto.getCapOrQty()==1 &&  dto.getPackageQty()!=null && dto.getPackageQty()>0)
								{
									iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(dto.getPackageConsumptionCapDetailsId(), dto.getPackageAmount(),false,false);
								}
								else if(dto.getCapOrQty()==2 && dto.getPackageAmount()!=null && dto.getPackageAmount().doubleValue()>0) 
									{
										iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(dto.getPackageConsumptionCapDetailsId(), dto.getPackageQty(), dto.getPackageAmount(),false,false);
									}
							}
						}
					}
				}
				return 1;

			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				
				throw e;
			}
	}
	
	
	
	@Override
	public Response getPkgOrderDetailsIdByServiceId(ServiceMasterDto dto) throws ServiceException {
		try {
			 Integer orderDetailsId=null;
			if(dto.getEncounterId()!=null && dto.getEncounterId()>0)
			{
				String query = "select tod.order_details_id as \"orderDetailsId\" "
						+ "from opd.t_encounter_master as tem inner join public.t_order_master tom on tem.encounter_id=tom.encounter_id "
						+ "inner join public.t_order_details as tod on tom.order_id=tod.order_id "
						+ "inner join service.m_package_master as mpm on mpm.package_master_id=tod.package_id and mpm.package_type_id=1 "
						+ "where tod.service_id=:serviceId "
						+ "and tod.status='A' "
						+ "and tem.encounter_id=:encounterId "
						+ "and tod.service_rendered=0 "
						+ "and tod.org_unit_id=:unitId "
						+ "and tod.org_id=:orgId "
						+ "order by \"orderDetailsId\" asc  limit 1";
			
				  orderDetailsId = (Integer)sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("serviceId", dto.getServiceMasterId())
				.setParameter("encounterId", dto.getEncounterId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("orgId", dto.getOrgId()).uniqueResult();
			}
			if(orderDetailsId==null) {
				return new Response<>(SUCCESS, null, null, null, null);
			}else {
				return new Response<>(SUCCESS, orderDetailsId.toString(), null, null, null);
			}
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response renderPackageServiceFrPostBillig(OrderDetailsMaster dto,
			AdmissionsForBatchProcessDto vistDetailsObj, Integer packageId) throws ApplicationException {
		try {
			
			OrderDetailsMaster master =null;
			if(vistDetailsObj.getAdmissionId()!=null && vistDetailsObj.getAdmissionId()>0 )
			{
				 master = (OrderDetailsMaster) sessionFactory.getCurrentSession().createSQLQuery("select "
						+ "	od.order_details_id as \"orderDetailsId\", cast(pm.package_price as numeric) as \"serviceAmt\" "
						+ " from " + "	public.t_order_details od "
						+ "inner join public.t_order_master om on om.order_id = od.order_id "
						+ "inner join service.m_package_master pm on od.service_id = pm.service_id "
						+ "inner join billing.t_package_consumption_master pcm on "
						+ "	pm.package_master_id = pcm.package_id " 
						+ " where " 
						+ "	pm.package_master_id = :packageId "
						+ "	and od.service_rendered = 0 " 
						+ "	and pcm.admission_id = :admissionId "
						+ "	and om.admission_id = :admissionId " + "	and od.is_package = true "
						+ "	and od.status = 'A' order by  od.order_details_id limit 1")
						.setParameter("packageId", packageId).setParameter("admissionId", vistDetailsObj.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsMaster.class)).uniqueResult();
			}
			else if(vistDetailsObj.getEncounterId()!=null && vistDetailsObj.getEncounterId()>0 )
			{
				 master = (OrderDetailsMaster) sessionFactory.getCurrentSession().createSQLQuery("select "
						+ "	od.order_details_id as \"orderDetailsId\", cast(pm.package_price as numeric) as \"serviceAmt\" "
						+ " from " + "	public.t_order_details od "
						+ " inner join public.t_order_master om on om.order_id = od.order_id "
						+ " inner join service.m_package_master pm on od.service_id = pm.service_id "
						+ " inner join billing.t_package_consumption_master pcm on "
						+ "	pm.package_master_id = pcm.package_id " 
						+ " where " 
						+ "	pm.package_master_id = :packageId "
						+ "	and od.service_rendered = 0 " 
						+ "	and pcm.encounter_id = :encounterId "
						+ "	and om.encounter_id = :encounterId " 
						+ "	and od.is_package = true "
						+ "	and od.status = 'A' order by  od.order_details_id limit 1")
						.setParameter("packageId", packageId).setParameter("encounterId", vistDetailsObj.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsMaster.class)).uniqueResult();
			}

			if (master != null && master.getOrderDetailsId() != 0) {
				/*OrderDetailsMaster mst = (OrderDetailsMaster) sessionFactory.getCurrentSession()
						.get(OrderDetailsMaster.class, master.getOrderDetailsId());
				*/
				 sessionFactory.getCurrentSession().createSQLQuery("update "
							+ " public.t_order_details set service_rendered = 1 "
							+ " where order_details_id=:orderDetailsId " )
							.setParameter("orderDetailsId", master.getOrderDetailsId())
							.executeUpdate();
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response forceCancelPackage(PackageConsumptionMasterResDto reqDto) throws ServiceException {
		try {
			
			if(reqDto.getPackageConsumptionMasterId() !=null && reqDto.getPackageConsumptionMasterId()>0)
			{
				Date newDate = TimeZoneComponent.getDateByzone(reqDto.getUnitId());
				TPackageConsumptionMaster consumptionMaster = (TPackageConsumptionMaster) sessionFactory.getCurrentSession().get(TPackageConsumptionMaster.class, reqDto.getPackageConsumptionMasterId());
				if(newDate!=null && consumptionMaster.getValidityTo()!=null)
					{
						if(newDate.before(consumptionMaster.getValidityTo()))
						{
							consumptionMaster.setValidityTo(newDate);
						}
					}else if(consumptionMaster.getValidityFrom()==null && consumptionMaster.getValidityTo()==null){
						consumptionMaster.setValidityTo(newDate);
					}
					consumptionMaster.setIsForceClose(0);
				sessionFactory.getCurrentSession().update(consumptionMaster);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelEhcPackageServiceCR(OrderDetailsMasterDto dto) throws ApplicationException {
		try {
			// cancel services
			if(dto.getPackageId() !=null && dto.isPackage()){
				sessionFactory.getCurrentSession().createSQLQuery(" update public.t_order_details set status ='I' where package_id = :packageId and is_package = false;")
						.setParameter("packageId", dto.getPackageId())
						.executeUpdate();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
		
		
	}

	@Override
	public Response inactiveParentPackage(Integer parentPkgId, boolean flag) throws ServiceException {
		try {
			Character status=flag ? 'A' : 'I'; 
			// cancel services
			if(parentPkgId !=null && parentPkgId.intValue()>0){
				sessionFactory.getCurrentSession().
				createSQLQuery(" update service.m_parent_package_master set status =:status ,updated_date=:updatedDate where parent_package_id =:parentPkgId ")
						.setParameter("parentPkgId", parentPkgId)
						.setParameter("status",status)
						.setDate("updatedDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss"))
						.executeUpdate();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getPackageListForIPDPackageOrderForBookView(Integer patientId, String fromDate, String toDate) {
		try 
		{
			List<PackageConsumptionMasterResDto> list = null;
//			if(admissionId!=null && admissionId>0) {
			Date date = TimeZoneComponent.getDateByzone(0);
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_LIST_FOR_IPD_PACKAGEORDER_FOR_BOOKVIEW").getQueryString());
//			query.append("	and  '"+GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd")+"' >= to_char(pcm.validity_from,'YYYY-MM-DD')  ");
//			query.append("	and '"+GlobalCommonDateUtils.getStringDate(date,"yyyy-MM-dd")+"' <= to_char(pcm.validity_to,'YYYY-MM-DD') ");
			query.append("	order by \"packageConsumptionMasterId\" ");
			 list = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("patientId", patientId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate",toDate)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionMasterResDto.class))
					.list();
//			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer getPackageVersionByParentPacakgeId(Integer parentPackageId) throws ServiceException {
		try {
			Integer versionId = (Integer) sessionFactory.getCurrentSession().
				createSQLQuery("select " + 
						"	coalesce(version_id, 0)+ 1" + 
						" from " + 
						"	service.m_parent_package_master mppm " + 
						" where " + 
						"  parent_package_id = :parentPackageId")
						.setParameter("parentPackageId", parentPackageId)
						.uniqueResult();
			return versionId.intValue();
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto dto1) throws ServiceException {

		try {

			OrderDetailsPackageMapperDto dto = new OrderDetailsPackageMapperDto();
			
			if(dto1.getAdmissionId() != null) {
				String sqlQuery = "select " +
						"    todpm.package_id as \"id\", " +
						"    sum(todpm.package_amount) as \"packageAmount\" " +
						"from " +
						"    t_order_details_package_mapper todpm " +
						"inner join t_order_details tod on " +
						"    todpm.order_details_id = tod.order_details_id " +
						"inner join t_order_master tom on " +
						"    tom.order_id = tod.order_id " +
						"where " +
						"    tom.admission_id = :admissionId " +
						"    and tod.status = 'A' " +
						"    and tod.service_id = :pharmacyServiceId " +
						"    and todpm.package_id = :packageId " +
						"group by " +
						"    todpm.package_id";
				Query query = sessionFactory.getCurrentSession()
						.createSQLQuery(sqlQuery)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class));
				query.setParameter("admissionId", dto1.getAdmissionId());
				query.setParameter("pharmacyServiceId", dto1.getPharmacyServiceId());
				query.setParameter("packageId", dto1.getPackageId());
				 dto = (OrderDetailsPackageMapperDto) query.uniqueResult();

			}else {
				String sqlQuery = "select " +
						"    todpm.package_id as \"id\", " +
						"    sum(todpm.package_amount) as \"packageAmount\" " +
						"from " +
						"    t_order_details_package_mapper todpm " +
						"inner join t_order_details tod on " +
						"    todpm.order_details_id = tod.order_details_id " +
						"inner join t_order_master tom on " +
						"    tom.order_id = tod.order_id " +
						"where " +
						"    tom.encounter_id = :encounterId " +
						"    and tod.status = 'A' " +
						"    and tod.service_id = :pharmacyServiceId " +
						"    and todpm.package_id = :packageId " +
						"group by " +
						"    todpm.package_id";
				Query query = sessionFactory.getCurrentSession()
						.createSQLQuery(sqlQuery)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class));
				query.setParameter("encounterId", dto1.getEncounterId());
				query.setParameter("pharmacyServiceId", dto1.getPharmacyServiceId());
				query.setParameter("packageId", dto1.getPackageId());
				 dto = (OrderDetailsPackageMapperDto) query.uniqueResult();
			}

			return new Response(SUCCESS, null, null, null, dto);
		} catch (Exception e) {
			
			throw e;
		}
	
	}

	@Override
	public Response getAllInclusivePackageAmount(BillingMasterDto dto1) throws ServiceException {


		try {

			OrderDetailsPackageMapperDto dto = new OrderDetailsPackageMapperDto();
			
			if(dto1.getAdmissionId() != null) {
				String sqlQuery = "select " +
						"    tod.service_amt as \"serviceAmount\" " +
						"from " +
						"    t_order_details tod " +
						"inner join t_order_master tom on " +
						"    tom.order_id = tod.order_id " +
						"where " +
						"    tom.admission_id = :admissionId " +
						"    and tod.status = 'A' " +
						"    and tod.package_id = :packageId " +
						"limit 1";
				Query query = sessionFactory.getCurrentSession()
						.createSQLQuery(sqlQuery)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class));
				query.setParameter("admissionId", dto1.getAdmissionId());
				query.setParameter("packageId", dto1.getPackageId());
				 dto = (OrderDetailsPackageMapperDto) query.uniqueResult();

			}else {
				String sqlQuery = "select " +
						"    tod.service_amt as \"serviceAmount\" " +
						"from " +
						"    t_order_details tod " +
						"inner join t_order_master tom on " +
						"    tom.order_id = tod.order_id " +
						"where " +
						"    tom.encounter_id = :encounterId " +
						"    and tod.status = 'A' " +
						"    and tod.package_id = :packageId " +
						"limit 1";
				Query query = sessionFactory.getCurrentSession()
						.createSQLQuery(sqlQuery)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsPackageMapperDto.class));
				query.setParameter("encounterId", dto1.getEncounterId());
				query.setParameter("packageId", dto1.getPackageId());
				 dto = (OrderDetailsPackageMapperDto) query.uniqueResult();

			}

			return new Response(SUCCESS, null, null, null, dto);
		} catch (Exception e) {
			
			throw e;
		}
	
	
	}

	@Override
	public Response getPackageListOnCreditNote(Integer patientId, Integer admissionId, Integer orgId, Integer unitId,
			Integer visitTypeId) {

		try 
		{
			List<PackageConsumptionMasterResDto> list = null;
			if(admissionId!=null && admissionId>0) {
			Date date = TimeZoneComponent.getDateByzone(unitId);
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_LIST_FOR_CREDIT_NOTE").getQueryString());
			query.append("	order by \"packageConsumptionMasterId\" ");
			 list = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("patientId", patientId)
					.setParameter("encounterId", admissionId)
					.setParameter("visitTypeId",visitTypeId)
					.setParameter("organisationId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionMasterResDto.class))
					.list();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	
	}

	@Override
	public Response packageListForWeb(PackageListReqDto reqDto) throws ServiceException {

		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_LIST_FOR_WEB").getQueryString());

			Query listQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId());

			List<PackageListResDto> listPackageListResDto = listQuery
					.setResultTransformer(Transformers.aliasToBean(PackageListResDto.class))
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPackageListResDto, null);
		} catch (Exception e) {

			throw e;
		}
	}


}
