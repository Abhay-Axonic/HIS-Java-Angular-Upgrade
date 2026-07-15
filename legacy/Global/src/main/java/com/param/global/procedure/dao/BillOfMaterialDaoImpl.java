package com.param.global.procedure.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.VisitTypeMasterDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.dto.BillOfMaterialDto;
import com.param.otims.model.BillOfMaterial;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class BillOfMaterialDaoImpl extends GenericDao<BillOfMaterial, Integer> implements IBillOfMaterialDao , ICommonConstants{

	final static Logger logger = Logger.getLogger(BillOfMaterialDaoImpl.class);

	public BillOfMaterialDaoImpl() {
		super(BillOfMaterial.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBillOfMaterial(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try {
			
			BillOfMaterial mstr = new BillOfMaterial();
			mstr.setAmount(billOfMaterialDto.getAmount());
			mstr.setBillingBedCatId(billOfMaterialDto.getBillingBedCatId());
			mstr.setProcedureId(billOfMaterialDto.getProcedureId());
			mstr.setIsGrade(billOfMaterialDto.getIsGrade());
			mstr.setSurgeryGradeId(billOfMaterialDto.getSurgeryGradeId());
			mstr.setServiceId(billOfMaterialDto.getServiceId());
			mstr.setOrganizationId(billOfMaterialDto.getOrganizationId());
			mstr.setUnitId(billOfMaterialDto.getUnitId());
			mstr.setCreatedBy(billOfMaterialDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(billOfMaterialDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setUpdatedBy(billOfMaterialDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(billOfMaterialDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setStatus('A');
			mstr.setTariffId(billOfMaterialDto.getTariffId());
			mstr.setPatientCategoryId(billOfMaterialDto.getPatientCategoryId());
			mstr.setVisitTypeId(billOfMaterialDto.getVisitTypeId());
			mstr.setPaymentEntitlementId(billOfMaterialDto.getPaymentEntitlementId());
			if(billOfMaterialDto.getBillOfMaterialId()!= null) {
				mstr.setBillOfMaterialId(billOfMaterialDto.getBillOfMaterialId());
				mstr = update(mstr);
			}else {
				mstr=save(mstr);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
		} catch (HibernateException he) {
			logger.error("saveBillOfMaterial failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBillOfMaterial failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillOfMaterialServices(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try {
			List<BillOfMaterialDto> list = sessionFactory.getCurrentSession().createSQLQuery(
					" SELECT "
							+ " sm.service_standard_name as \"serviceName\", "
							+ " smd.is_ref_doctor_share as \"isRefDoctorShare\", "
							+ " sm.service_master_id as \"serviceId\" "
							+ " FROM service.m_service_master sm "
							+ " inner join  service.t_unit_service_master_details smd on sm.service_master_id = smd.service_master_id "
							+ " WHERE  smd.bill_of_material = 'Y'"
							+ " and smd.unit_id =:unitId  "
							+ " and smd.organization_id =:organizationId "+
							" union " + 
							" SELECT sm.service_standard_name as \"serviceName\", " + 
							" smd.is_ref_doctor_share as \"isRefDoctorShare\", " + 
							" sm.service_master_id as \"serviceId\" " + 
							" FROM service.m_service_master sm " + 
							" inner join  service.t_unit_service_master_details smd on sm.service_master_id = smd.service_master_id " + 
							" inner join public.t_unit_application_billing_configuration uabc on uabc.ot_rent_service_id = sm.service_master_id " + 
							" WHERE  uabc.unit_id =:unitId  and uabc.organization_id =:organizationId  "+
							" ORDER BY \"serviceName\" ASC ")
					.setParameter("unitId",billOfMaterialDto.getUnitId())
					.setParameter("organizationId",billOfMaterialDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getBillOfMaterialServices failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getIsProcedure(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try {
			
			List<BillOfMaterialDto> list = sessionFactory.getCurrentSession().createSQLQuery(
					" SELECT "
						+ " sm.service_standard_name as \"serviceName\","
						+ " sm.service_master_id as \"serviceId\" "
						+ " FROM service.m_service_master sm "
						+ " inner join  service.t_unit_service_master_details smd on sm.service_master_id = smd.service_master_id "
						+ " WHERE  smd.is_procedure = 'Y'"
						+ " and smd.unit_id =:unitId  "
						+ " and lower(sm.service_standard_name) like :procedure "
						+ " and smd.organization_id ="+billOfMaterialDto.getOrganizationId())
					.setParameter("unitId",billOfMaterialDto.getUnitId())
					.setParameter("procedure","%"+billOfMaterialDto.getProcedure().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class)).list();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch (HibernateException he) {
		logger.error("getIsProcedure failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getIsProcedure failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getBillOfMaterialListByProcedureId(BillOfMaterialDto billOfMaterialDto)
			throws ApplicationException {
		try {
			String baseQuery = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_OF_MATERIAL_LIST")
					.getQueryString();
			StringBuilder queryBuilder = new StringBuilder(baseQuery);

			if (billOfMaterialDto.getBillingBedCatId() != null) {
				queryBuilder.append(" AND mstr.billingBedCatId = :billingBedCatId");
			}
			if (billOfMaterialDto.getProcedureId() != null && !billOfMaterialDto.getIsGrade()) {
				queryBuilder.append(" AND mstr.procedureId = :procedureId");
				queryBuilder.append(" AND mstr.isGrade = false");
			}
			if (billOfMaterialDto.getSurgeryGradeId() != null && billOfMaterialDto.getSurgeryGradeId() != 0 && billOfMaterialDto.getIsGrade()) {
				queryBuilder.append(" AND mstr.surgeryGradeId = :surgeryGradeId");
				queryBuilder.append(" AND mstr.isGrade = true");
			}
			if (billOfMaterialDto.getTariffId() != null && billOfMaterialDto.getTariffId() != 0) {
				queryBuilder.append(" AND mstr.tariffId = :tariffId");
			}
			if (billOfMaterialDto.getVisitTypeId() != null && billOfMaterialDto.getVisitTypeId() != 0) {
				queryBuilder.append(" AND mstr.visitTypeId = :visitTypeId");
			}
			if (billOfMaterialDto.getPaymentEntitlementId() != null && billOfMaterialDto.getPaymentEntitlementId() != 0) {
				queryBuilder.append(" AND mstr.paymentEntitlementId = :paymentEntitlementId");
			}
			if (billOfMaterialDto.getPatientCategoryId() != null && billOfMaterialDto.getPatientCategoryId() != 0) {
				queryBuilder.append(" AND mstr.patientCategoryId = :patientCategoryId");
			}

			Query query = sessionFactory.getCurrentSession()
					.createQuery(queryBuilder.toString())
					.setParameter("organizationId", billOfMaterialDto.getOrganizationId())
					.setParameter("unitId", billOfMaterialDto.getUnitId());
			if (billOfMaterialDto.getBillingBedCatId() != null) {
				query.setParameter("billingBedCatId", billOfMaterialDto.getBillingBedCatId());
			}
			if (billOfMaterialDto.getProcedureId() != null && !billOfMaterialDto.getIsGrade()) {
				query.setParameter("procedureId", billOfMaterialDto.getProcedureId());
			}
			if (billOfMaterialDto.getSurgeryGradeId() != null && billOfMaterialDto.getSurgeryGradeId() != 0 && billOfMaterialDto.getIsGrade()) {
				query.setParameter("surgeryGradeId", billOfMaterialDto.getSurgeryGradeId());
			}
			if (billOfMaterialDto.getTariffId() != null && billOfMaterialDto.getTariffId() != 0) {
				query.setParameter("tariffId", billOfMaterialDto.getTariffId());
			}
			if (billOfMaterialDto.getVisitTypeId() != null && billOfMaterialDto.getVisitTypeId() != 0) {
				query.setParameter("visitTypeId", billOfMaterialDto.getVisitTypeId());
			}
			if (billOfMaterialDto.getPaymentEntitlementId() != null && billOfMaterialDto.getPaymentEntitlementId() != 0) {
				query.setParameter("paymentEntitlementId", billOfMaterialDto.getPaymentEntitlementId());
			}
			if (billOfMaterialDto.getPatientCategoryId() != null && billOfMaterialDto.getPatientCategoryId() != 0) {
				query.setParameter("patientCategoryId", billOfMaterialDto.getPatientCategoryId());
			}
			List<BillOfMaterialDto> list = query.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch (HibernateException he) {
		logger.error("getBillOfMaterialListByProcedureId failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getBillOfMaterialListByProcedureId failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getVisitTypeByBillingBedCat(Integer billingBedCatId, Integer unitId) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(
					" select distinct visit_type_id as \"visitTypeId\" from adt.t_billing_bed_visit_type_mapper "
					+ " where billing_bed_category_id="+billingBedCatId+" and status='A' ")
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getVisitTypeByBillingBedCat failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillOfMaterialServiceAmount(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try {
			
			Integer amount = 0;
			String query=" 	select distinct bom.bill_of_material_id as \"billOfMaterialId\" ," + 
					"					bom.procedure_id as \"procedureId\", " + 
					"					bom.service_id as \"serviceId\"," + 
					"					bom.billing_bed_cat_id as \"billingBedCatId\"," + 
					"					bom.amount as \"amount\"," + 
					"				 	bom.status as \"status\", " + 
					"				 	bom.organization_id as \"organizationId\"," + 
					"					unitsermaster.is_ref_doctor_share as \"isRefDoctorShare\"," + 
					"					bom.unit_id as \"unitId\"," + 
					"					bom.created_by as \"createdBy\"," + 
					"					to_char(bom.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\"," + 
					"					to_char(bom.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\"," + 
					"					bom.updated_by as \"updatedBy\" ," + 
					"					sermas.service_standard_name as \"serviceName\" " + 
					"	from otims.t_bill_of_material  bom " + 
					"	 inner  join service.m_service_master sermas on bom.service_id = sermas.service_master_id " + 
					"	 inner join service.t_unit_service_master_details unitsermaster  on sermas.service_master_id = unitsermaster.service_master_id "
					+ " where "
					+"  bom.amount !=:amount  and bom.billing_bed_cat_id =:billingBedCatId  ";
			StringBuffer s1 = new StringBuffer(query);
			if(billOfMaterialDto.getProcedureId()!= null && billOfMaterialDto.getSurgeryGradeId() == null) {
			s1.append(" And bom.procedure_id =:procedureId ");
			}else {
			s1.append(" and bom.contract_grade_master_id =:surgeryGradeId ");
			}
			Query createQuery =   sessionFactory.getCurrentSession().createSQLQuery(s1.toString())
					.setParameter("amount",amount)
					.setParameter("billingBedCatId",billOfMaterialDto.getBillingBedCatId());

			if(billOfMaterialDto.getProcedureId()!= null && billOfMaterialDto.getSurgeryGradeId() == null) {
				createQuery.setParameter("procedureId",billOfMaterialDto.getProcedureId());
			}else {
				createQuery.setParameter("surgeryGradeId",billOfMaterialDto.getSurgeryGradeId());
			}

			List<BillOfMaterialDto> list =	createQuery.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getBillOfMaterialServiceAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateReservationAgainstAdmissionEncounter(OTReservationDto oTReservationDto)
			throws ApplicationException {
		try{	
			Integer count = null;
			if(oTReservationDto.getAdmissionId()!=null && oTReservationDto.getPatientId()!=null) {
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"UPDATE otims.t_ot_reservation " +
								"SET reservation_status_id = :reservationStatusId, " +
								"    admission_id = :admissionId, " +
								"    visit_type_id = :visitTypeId " +
								"WHERE ot_reservation_id = :otReservationId");

				query.setParameter("reservationStatusId", 3); // Assuming reservation status is constant
				query.setParameter("admissionId", oTReservationDto.getAdmissionId());
				query.setParameter("visitTypeId", oTReservationDto.getVisitTypeId());
				query.setParameter("otReservationId", oTReservationDto.getOtReservationId());

				 count = query.executeUpdate();

				if(count>0) {
					Query query1 = sessionFactory.getCurrentSession().createSQLQuery(
							"UPDATE otims.t_procedure_req_mst SET admission_id = :admissionId, visit_type_id = :visitTypeId WHERE pro_req_id = :proReqId");

					query1.setParameter("admissionId", oTReservationDto.getAdmissionId());
					query1.setParameter("visitTypeId", oTReservationDto.getVisitTypeId());
					query1.setParameter("proReqId", oTReservationDto.getProReqId());
					 count = query1.executeUpdate();
				}
			}else if(oTReservationDto.getEncounterId()!=null && oTReservationDto.getPatientId()!=null){
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"UPDATE otims.t_ot_reservation " +
								"SET reservation_status_id = :reservationStatusId, " +
								"    encounter_id = :encounterId, " +
								"    visit_type_id = :visitTypeId " +
								"WHERE ot_reservation_id = :otReservationId"
				);

// Set parameters
				query.setParameter("reservationStatusId", 3); // Hardcoded value
				query.setParameter("encounterId", oTReservationDto.getEncounterId());
				query.setParameter("visitTypeId", oTReservationDto.getVisitTypeId());
				query.setParameter("otReservationId", oTReservationDto.getOtReservationId());
				 count = query.executeUpdate();
				if(count>0) {
					Query query1 = sessionFactory.getCurrentSession().createSQLQuery(
							"UPDATE otims.t_procedure_req_mst " +
									"SET encounter_id = :encounterId, " +
									"    visit_type_id = :visitTypeId " +
									"WHERE pro_req_id = :proReqId"
					);
					query1.setParameter("encounterId", oTReservationDto.getEncounterId());
					query1.setParameter("visitTypeId", oTReservationDto.getVisitTypeId());
					query1.setParameter("proReqId", oTReservationDto.getProReqId());
					 count = query1.executeUpdate();
				}
			}
				
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (HibernateException he) {
			logger.error("updateReservationAgainstAdmissionEncounter failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateReservationAgainstAdmissionEncounter failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response reAssignOtReservationProcedureAdmissionEncounter(OTReservationDto oTReservationDto)
			throws ApplicationException {
		try{	
		
			Integer count = null;
			if(oTReservationDto.getAdmissionId()!=null){
				
				OTReservationDto obj = (OTReservationDto) sessionFactory.getCurrentSession().createSQLQuery(
						" select   ot_reservation_id as \"otReservationId\","
						+ " pro_req_id as \"proReqId\" "
						+ " from otims.t_ot_reservation  otre " + 
						"	inner join otims.t_procedure_req_mst proReq on otre.pro_req_id = proReq.pro_req_id " + 
						"	where otre.reservation_status_id = 3 and otre.admission_id =:admissionId "
						+ " and proReq.procedure_status_id in(3)  ")
						.setParameter("admissionId",oTReservationDto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).uniqueResult();
				
				 if(obj.getOtReservationId()!= null) {
					 
					 count = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_ot_reservation set reservation_status_id = 1 "
							 	+ " where ot_reservation_id=:otReservationId ")
							    .setParameter("otReservationId",obj.getOtReservationId())
								.executeUpdate();
					 
					 count = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_req_mst set admission_id = null "
							 	+ " where pro_req_id=:proReqId ")
							    .setParameter("proReqId",obj.getProReqId())
								.executeUpdate();
					}
			}else if(oTReservationDto.getEncounterId()!=null){
				
				OTReservationDto obj = (OTReservationDto) sessionFactory.getCurrentSession().createSQLQuery(
						" select   ot_reservation_id as \"otReservationId\", "
						+ " pro_req_id as \"proReqId\" "
						+ " from otims.t_ot_reservation  otre " + 
						"	inner join otims.t_procedure_req_mst proReq on otre.pro_req_id = proReq.pro_req_id " + 
						"	where otre.reservation_status_id = 3 and otre.encounter_id =:encounterId "
						+ " and proReq.procedure_status_id in(3)  ")
						.setParameter("encounterId",oTReservationDto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).uniqueResult();
				
				 if(obj.getOtReservationId()!= null) {
					 
					 count = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_ot_reservation set reservation_status_id = 1 "
							 	+ " where ot_reservation_id=:otReservationId")
							    .setParameter("otReservationId",obj.getOtReservationId())
								.executeUpdate();
					 
					 count = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_req_mst set encounter_id = null "
							 	+ " where pro_req_id=:proReqId ")
							    .setParameter("proReqId",obj.getProReqId())
								.executeUpdate();
					}
			}
				
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (HibernateException he) {
			logger.error("reAssignOtReservationProcedureAdmissionEncounter failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("reAssignOtReservationProcedureAdmissionEncounter failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response convertOtdepositeToActiveDeposite(OTReservationDto oTReservationDto) throws ApplicationException {
		try{
			Integer count = null;
			if(oTReservationDto.getPatientId()!=null) {
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"UPDATE billing.m_deposit_master " +
								"SET deposit_type_id = :depositTypeId, " +
								"    admission_id = :admissionId, " +
								"    encounter_id = :encounterId, " +
								"    visit_type_id = :visitTypeId " +
								"WHERE ot_reservation_id = :otReservationId"
				);
				query.setParameter("depositTypeId", 1); // Hardcoded value
				query.setParameter("admissionId", oTReservationDto.getAdmissionId());
				query.setParameter("encounterId", oTReservationDto.getEncounterId());
				query.setParameter("visitTypeId", oTReservationDto.getVisitTypeId());
				query.setParameter("otReservationId", oTReservationDto.getOtReservationId());
				 count = query.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (HibernateException he) {
			logger.error("convertOtdepositeToActiveDeposite failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("convertOtdepositeToActiveDeposite failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCongServicesByProReqId(OTReservationDto oTReservationDto) throws ApplicationException {
		try {
			List<BillOfMaterialDto> list = sessionFactory.getCurrentSession().createSQLQuery(
					"select serMsrt.service_master_id as \"serviceId\"," + 
					"serMsrt.speciality_id as \"specialityId\","
					+ " prore.unit_id as \"unitId\", "
					+ "prore.org_id as \"organizationId\" , "
					+ "prore.created_by as \"createdBy\"," + 
					"serMsrt.sub_speciality_id as \"subSpecialityId\" " + 
					"from otims.t_procedure_config_service  proSer " + 
					"inner join otims.t_procedure_configuration procofig on proSer.procedure_config_id = procofig.procedure_config_id  " + 
					"inner  join otims.t_procedure_req_mst prore  on procofig.service_id = prore.primary_procedure_id  " + 
					"inner join service.m_service_master  serMsrt on proSer.service_id = serMsrt.service_master_id " + 
					"where prore.pro_req_id =:proReqId ")
					.setParameter("proReqId",oTReservationDto.getProReqId())
					.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getCongServicesByProReqId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAdmissionsById(Integer admissionId) throws ApplicationException {
		try{
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT_FOR_BILLOF_MATERIAL").setParameter("admissionId", admissionId)
										.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBillOfMaterialListByProcedureList(BillOfMaterialDto billOfMaterialDto)
			throws ApplicationException {
		try {
			String baseQuery = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_OF_MATERIAL_PROCEDURE_LIST")
					.getQueryString();
			StringBuilder subQuery = new StringBuilder();
			Map<String, Object> parameters = new HashMap<>();

			if (billOfMaterialDto.getBillingBedCatId() != null) {
				subQuery.append(" AND billofmate0_.billing_bed_cat_id = :billingBedCatId");
				parameters.put("billingBedCatId", billOfMaterialDto.getBillingBedCatId());
			}

			subQuery.append(" AND billofmate0_.is_grade = :isGrade");
			parameters.put("isGrade", billOfMaterialDto.getIsGrade());

			if (billOfMaterialDto.getProcedureId() != null && !billOfMaterialDto.getIsGrade()) {
				subQuery.append(" AND billofmate0_.procedure_id = :procedureId");
				parameters.put("procedureId", billOfMaterialDto.getProcedureId());
			}

			if (billOfMaterialDto.getSurgeryGradeId() != null && billOfMaterialDto.getSurgeryGradeId() != 0 && billOfMaterialDto.getIsGrade()) {
				subQuery.append(" AND billofmate0_.surgery_grade_id = :surgeryGradeId");
				parameters.put("surgeryGradeId", billOfMaterialDto.getSurgeryGradeId());
			}

			if (billOfMaterialDto.getTariffId() != null && billOfMaterialDto.getTariffId() != 0) {
				subQuery.append(" AND billofmate0_.tariff_id = :tariffId");
				parameters.put("tariffId", billOfMaterialDto.getTariffId());
			}

			if (billOfMaterialDto.getVisitTypeId() != null && billOfMaterialDto.getVisitTypeId() != 0) {
				subQuery.append(" AND billofmate0_.visit_type_id = :visitTypeId");
				parameters.put("visitTypeId", billOfMaterialDto.getVisitTypeId());
			}

			if (billOfMaterialDto.getPaymentEntitlementId() != null && billOfMaterialDto.getPaymentEntitlementId() != 0) {
				subQuery.append(" AND billofmate0_.payment_entitlement_id = :paymentEntitlementId");
				parameters.put("paymentEntitlementId", billOfMaterialDto.getPaymentEntitlementId());
			}

			if (billOfMaterialDto.getPatientCategoryId() != null && billOfMaterialDto.getPatientCategoryId() != 0) {
				subQuery.append(" AND billofmate0_.patient_category_id = :patientCategoryId");
				parameters.put("patientCategoryId", billOfMaterialDto.getPatientCategoryId());
			}
			String finalQuery = baseQuery + subQuery.toString();
			Query query = sessionFactory.getCurrentSession()
					.createSQLQuery(finalQuery)
					.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class));
			query.setParameter("organizationId", billOfMaterialDto.getOrganizationId());
			query.setParameter("unitId", billOfMaterialDto.getUnitId());
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			List<BillOfMaterialDto> list = query.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch (HibernateException he) {
		logger.error("getBillOfMaterialListByProcedureList failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getBillOfMaterialListByProcedureList failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateStatusProcedure(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try{	
			
			if(billOfMaterialDto.getProcedureId() != null) {

				Integer count = null;
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"UPDATE otims.t_bill_of_material " +
								"SET status = :status " +
								"WHERE procedure_id = :procedureId " +
								"  AND tariff_id = :tariffId " +
								"  AND visit_type_id = :visitTypeId " +
								"  AND payment_entitlement_id = :paymentEntitlementId " +
								"  AND patient_category_id = :patientCategoryId"
				);
				query.setParameter("status", billOfMaterialDto.getStatus());
				query.setParameter("procedureId", billOfMaterialDto.getProcedureId());
				query.setParameter("tariffId", billOfMaterialDto.getTariffId());
				query.setParameter("visitTypeId", billOfMaterialDto.getVisitTypeId());
				query.setParameter("paymentEntitlementId", billOfMaterialDto.getPaymentEntitlementId());
				query.setParameter("patientCategoryId", billOfMaterialDto.getPatientCategoryId());
				count = query.executeUpdate();

			}
			     
				
			if(billOfMaterialDto.getSurgeryGradeId() != null) {
				Integer count = null;
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"UPDATE otims.t_bill_of_material " +
								"SET status = :status " +
								"WHERE surgery_grade_id = :surgeryGradeId " +
								"  AND tariff_id = :tariffId " +
								"  AND visit_type_id = :visitTypeId " +
								"  AND payment_entitlement_id = :paymentEntitlementId " +
								"  AND patient_category_id = :patientCategoryId"
				);
				query.setParameter("status", billOfMaterialDto.getStatus());
				query.setParameter("surgeryGradeId", billOfMaterialDto.getSurgeryGradeId());
				query.setParameter("tariffId", billOfMaterialDto.getTariffId());
				query.setParameter("visitTypeId", billOfMaterialDto.getVisitTypeId());
				query.setParameter("paymentEntitlementId", billOfMaterialDto.getPaymentEntitlementId());
				query.setParameter("patientCategoryId", billOfMaterialDto.getPatientCategoryId());
				count = query.executeUpdate();

			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (HibernateException he) {
			logger.error("updateStatusProcedure failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStatusProcedure failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillOfMaterialListByProcedureIdAll(BillOfMaterialDto billOfMaterialDto)
			throws ApplicationException {
		try {
			// Get the base query from the named query
			String baseQuery = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILL_OF_MATERIAL_LIST_ALL")
					.getQueryString();
			StringBuilder subQuery = new StringBuilder();
			Map<String, Object> parameters = new HashMap<>();

			if (billOfMaterialDto.getBillingBedCatId() != null) {
				subQuery.append(" AND mstr.billingBedCatId = :billingBedCatId");
				parameters.put("billingBedCatId", billOfMaterialDto.getBillingBedCatId());
			}

			if (billOfMaterialDto.getProcedureId() != null && !billOfMaterialDto.getIsGrade()) {
				subQuery.append(" AND mstr.procedureId = :procedureId");
				subQuery.append(" AND mstr.isGrade = false");
				parameters.put("procedureId", billOfMaterialDto.getProcedureId());
			}

			if (billOfMaterialDto.getSurgeryGradeId() != null && billOfMaterialDto.getSurgeryGradeId() != 0 && billOfMaterialDto.getIsGrade()) {
				subQuery.append(" AND mstr.surgeryGradeId = :surgeryGradeId");
				subQuery.append(" AND mstr.isGrade = true");
				parameters.put("surgeryGradeId", billOfMaterialDto.getSurgeryGradeId());
			}

			if (billOfMaterialDto.getTariffId() != null && billOfMaterialDto.getTariffId() != 0) {
				subQuery.append(" AND mstr.tariffId = :tariffId");
				parameters.put("tariffId", billOfMaterialDto.getTariffId());
			}

			if (billOfMaterialDto.getVisitTypeId() != null && billOfMaterialDto.getVisitTypeId() != 0) {
				subQuery.append(" AND mstr.visitTypeId = :visitTypeId");
				parameters.put("visitTypeId", billOfMaterialDto.getVisitTypeId());
			}

			if (billOfMaterialDto.getPaymentEntitlementId() != null && billOfMaterialDto.getPaymentEntitlementId() != 0) {
				subQuery.append(" AND mstr.paymentEntitlementId = :paymentEntitlementId");
				parameters.put("paymentEntitlementId", billOfMaterialDto.getPaymentEntitlementId());
			}

			if (billOfMaterialDto.getPatientCategoryId() != null && billOfMaterialDto.getPatientCategoryId() != 0) {
				subQuery.append(" AND mstr.patientCategoryId = :patientCategoryId");
				parameters.put("patientCategoryId", billOfMaterialDto.getPatientCategoryId());
			}
			String finalQuery = baseQuery + subQuery.toString();
			Query query = sessionFactory.getCurrentSession()
					.createQuery(finalQuery)
					.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class));
			query.setParameter("organizationId", billOfMaterialDto.getOrganizationId());
			query.setParameter("unitId", billOfMaterialDto.getUnitId());
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			List<BillOfMaterialDto> list = query.list();


			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch (HibernateException he) {
		logger.error("getBillOfMaterialListByProcedureIdAll failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getBillOfMaterialListByProcedureIdAll failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

}
