package com.param.adt.master.unit.dao;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.BedManagementDto;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.master.global.dto.BedCategoryAvailibilityDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.adt.master.global.model.BedMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import scala.collection.mutable.StringBuilder;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused"})
public class BedMasterDaoImpl extends GenericDao<BedMaster, Integer> implements IBedMasterDao,ICommonConstants{

	public BedMasterDaoImpl() {
		super(BedMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response checkBedAlreadyExistOrNot(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_BED_ALREADY_EXIST_OR_NOT")
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		}catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addBed(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			BedMaster bedMaster = mapper.map(bedMasterDto, BedMaster.class,"BedMasterDto_to_BedMaster");
			bedMaster.setCreatedDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() :0 ));
			bedMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() :0));
			bedMaster.setStatus('A');
			bedMaster=save(bedMaster);
			
			bedMasterDto.setBedId(bedMaster.getBedId());
			
			return new Response(SUCCESS, null, null, null, bedMasterDto);

		}catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveBedLog(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			BedLogStatus bedLogStatus = mapper.map(bedMasterDto, BedLogStatus.class,"BedMasterDto_to_BedLogStatus");
			bedLogStatus.setCreatedDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0));
			bedLogStatus.setUpdatedDate(TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId() : 0));
			sessionFactory.getCurrentSession().save(bedLogStatus);
			return new Response(SUCCESS, null, SUCCESS_MESSAGE, null, null);

		}catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Long getNextValBedMasterId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('adt.bed_master_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('adt.bed_master_seq'," + (id-1) + ")");
			query2.uniqueResult();

			return id;
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}
	
	@Override
	public Response getBedMasterList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BED_MASTER_LIST")
					.setParameter("orgId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset() : 0)
					.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedByNameNotId(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			
			List<BedMasterDto> areaMasterDtoList=null;
			if(bedMasterDto.getIsMovingBed()=='Y'){
				areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BED_MASTER_BY_NAME_NOT_ID_FOR_MOVING_BED")
						.setParameter("orgId", bedMasterDto.getOrganizationId())
						.setParameter("unitId", bedMasterDto.getUnitId())
						.setParameter("bedCode",bedMasterDto.getBedCode().toLowerCase())
						.setParameter("bedDesc",bedMasterDto.getBedDesc().toLowerCase())
						.setParameter("bedId", bedMasterDto.getBedId())
						.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			}else{	
			//areaMasterDtoList =
				StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_BED_MASTER_BY_NAME_NOT_ID").getQueryString());
				if(bedMasterDto.getBedCategoryId()!=null) {
					query.append(" AND bm.bedCategoryId=:bedCategoryId ");
				}
				if(bedMasterDto.getBillingBedCategoryId()!=null){
				query.append(" AND bm.billingBedCategoryId=:billingBedCategoryId ");
				}
				
				Query sql=sessionFactory.getCurrentSession().createQuery(query.toString())
					.setParameter("orgId",bedMasterDto.getOrganizationId() )
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setParameter("floorId", bedMasterDto.getFloorId())
					.setParameter("wardId", bedMasterDto.getWardId())
					.setParameter("roomId", bedMasterDto.getRoomId())
					.setParameter("isVirtual", bedMasterDto.getIsVirtual())
					.setParameter("isEarMarking", bedMasterDto.getIsEarMarking())
					.setParameter("bedCode",bedMasterDto.getBedCode())
					.setParameter("bedDesc",bedMasterDto.getBedDesc())
					.setParameter("bedId", bedMasterDto.getBedId());
				if(bedMasterDto.getBedCategoryId()!=null) {
					sql.setParameter("bedCategoryId",bedMasterDto.getBedCategoryId() );
				}
				if(bedMasterDto.getBillingBedCategoryId()!=null){
					sql.setParameter("billingBedCategoryId",bedMasterDto.getBillingBedCategoryId() );
				}
				areaMasterDtoList=	sql.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			}
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBedMaster(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			
			BedMaster bedMaster = findById(bedMasterDto.getBedId());
			if(bedMasterDto.getIsMovingBed()=='Y'){				
				bedMaster.setBedCode(bedMasterDto.getBedCode());
				bedMaster.setBedDesc(bedMasterDto.getBedDesc());
				bedMaster.setBedNumber(bedMasterDto.getBedNumber());
				bedMaster.setUpdatedBy(bedMasterDto.getUpdatedBy());
				bedMaster.setUpdatedDate(ADTCommonDateUtils.getDate(bedMasterDto.getUpdatedDate(), "yyyy-dd-mm HH:mm:ss"));
			}else{
				bedMaster.setFloorId(bedMasterDto.getFloorId());
				bedMaster.setWardId(bedMasterDto.getWardId());
				bedMaster.setRoomId(bedMasterDto.getRoomId());
				if(bedMasterDto.getBillingBedCategoryId()!=null)
				bedMaster.setBillingBedCategoryId(bedMasterDto.getBillingBedCategoryId());
				if(bedMasterDto.getBedCategoryId()!=null)
				bedMaster.setBedCategoryId(bedMasterDto.getBedCategoryId());
				bedMaster.setIsVirtual(bedMasterDto.getIsVirtual());
				bedMaster.setIsEarMarking(bedMasterDto.getIsEarMarking());
				bedMaster.setBedCode(bedMasterDto.getBedCode());
				bedMaster.setBedDesc(bedMasterDto.getBedDesc());
				bedMaster.setEarmarkingDesc(bedMasterDto.getEarmarkingDesc());
				bedMaster.setBedNumber(bedMasterDto.getBedNumber());
				bedMaster.setUpdatedBy(bedMasterDto.getUpdatedBy());
				if(bedMasterDto.getItemId()!=null)
				bedMaster.setItemId(bedMasterDto.getItemId());
				bedMaster.setUpdatedDate(ADTCommonDateUtils.getDate(bedMasterDto.getUpdatedDate(), "yyyy-dd-mm HH:mm:ss"));
			}
			update(bedMaster);	
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

		}catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getbedById(Integer bedId) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BED_BY_ID")
					.setParameter("bedId", bedId)
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatus(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update BedMaster set status=:status, "
					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ "WHERE bedId=:bedId ")
					.setParameter("bedId", bedMasterDto.getBedId())
					.setParameter("updatedBy",bedMasterDto.getUpdatedBy())
					.setParameter("status", bedMasterDto.getStatus())
					.setParameter("updatedDate",ADTCommonDateUtils.getDate(bedMasterDto.getUpdatedDate(), "yyyy-mm-dd HH:mm:ss"))
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response bedListCount(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(BedMaster.class);
			c.add(Restrictions.eq("organizationId", bedMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", bedMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedCategoryAvailabilityCount(int organizationId, int unitId) throws ApplicationException {
		try {
			List<BedCategoryAvailibilityDto> areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BED_CATEGORY_AVAILABILITY_COUNT")
					//.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BedCategoryAvailibilityDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedByNameAndDesc(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<BedMasterDto> areaMasterDtoList=null;
					if(bedMasterDto.getIsMovingBed()=='Y'){
						areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BED_BY_NAME_AND_DESC_FOR_MOVING_BED")
								.setParameter("orgId", bedMasterDto.getOrganizationId())
								.setParameter("unitId", bedMasterDto.getUnitId())
								.setParameter("bedCode",bedMasterDto.getBedCode().toLowerCase())
								.setParameter("bedDesc",bedMasterDto.getBedDesc().toLowerCase())
								.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
					}else{			
						StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_BED_BY_NAME_AND_DESC").getQueryString());
						if(bedMasterDto.getBedCategoryId()!=null) {
							query.append(" AND bm.bedCategoryId=:bedCategoryId ");
						}
						if(bedMasterDto.getBillingBedCategoryId()!=null) {
							query.append(" AND bm.billingBedCategoryId=:billingBedCategoryId ");
						}
						
						Query sql=sessionFactory.getCurrentSession().createQuery(query.toString())
							    .setParameter("orgId", bedMasterDto.getOrganizationId())
								.setParameter("unitId", bedMasterDto.getUnitId())
								.setParameter("floorId", bedMasterDto.getFloorId())
								.setParameter("wardId", bedMasterDto.getWardId())
								.setParameter("roomId", bedMasterDto.getRoomId())
								.setParameter("isVirtual", bedMasterDto.getIsVirtual())
								.setParameter("isEarMarking", bedMasterDto.getIsEarMarking())
								.setParameter("bedCode",bedMasterDto.getBedCode().toLowerCase())
								.setParameter("bedDesc",bedMasterDto.getBedDesc().toLowerCase());
						if(bedMasterDto.getBedCategoryId()!=null) {
							sql.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
						}
						if(bedMasterDto.getBillingBedCategoryId()!=null) {
							sql.setParameter("billingBedCategoryId", bedMasterDto.getBillingBedCategoryId());
						}
						areaMasterDtoList=	sql.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
						
					}
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		 return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response postBedMagementCount(Integer unitId) throws ApplicationException {
		try {
			boolean flag = (boolean) sessionFactory.getCurrentSession()
					.createSQLQuery("select adt.post_bed_management_couts(:unitId)")
					.setParameter("unitId", unitId)
			.uniqueResult();
			if(flag = true)	
				return new Response(SUCCESS, null, null, null, null);
			else
				return new Response(ERROR, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	
	private StringBuilder searchQuery(BedMasterDto bedMasterDto,StringBuilder query)
	{
		if(bedMasterDto.getFloorId()>0) {
			query.append(" AND bm.floor_id=:floorId ");
		}
		if(bedMasterDto.getWardId()>0) {
			query.append(" AND bm.ward_id=:wardId ");
		}
		/*if(bedMasterDto.getNursingStationId()>0)
			query.append(" AND nurWard.nursing_station_id="+bedMasterDto.getNursingStationId()+" ");
		*/
		if(bedMasterDto.getBedCategoryId()>0) {
			query.append(" AND bm.bed_category_id=:bedCategoryId ");
		}
		if(bedMasterDto.getPatientId()>0) {
			query.append(" AND admission.patient_id=:patientId ");
		}
		
		return query;
	}
	
	@Override
	public Response getDataForBedManagementData(BedMasterDto bedMasterDto,boolean search) throws ApplicationException {
		try {
			
			List<Integer> specialityIds = new LinkedList<>();
			if(bedMasterDto.getLabSpecialityId()!=null)
				specialityIds.add(bedMasterDto.getLabSpecialityId());
			if(bedMasterDto.getRadiologySpecialityId()!=null)
				specialityIds.add(bedMasterDto.getRadiologySpecialityId());
			if(bedMasterDto.getHemodialysisSpecialityId()!=null)
				specialityIds.add(bedMasterDto.getHemodialysisSpecialityId());
			
			List<BedManagementDto> bedManagementList = null;
			StringBuilder query = new StringBuilder(" select "
					 + " 	admission.admission_id as \"admissionId\", "
					 + " 	admission.patient_id as \"patientId\", "
					 + " 	admission.t_patient_id as \"tPatientId\", "
					 + " 	admission.ot_status as \"otStatus\", "
					 + " 	admission.is_final_bill as \"isFinalBill\", "
					 + " 	admission.admission_number as \"admissionNumber\", "
					 + " 	admission.visit_type_id as \"visitTypeId\", "
					 + " 	admission.doctor_id as \"doctorId\", "
					 + " 	admission.speciality_id as \"specialityId\", "
					 + " 	sp.speciality_name as \"specialityName\", "
					 + " 	pr.uhid_number as \"UHIDNumber\", "
					 + " 	pr.image_path as \"imagePath\", "
					 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), ' ', coalesce(pr.middle_name), ' ', coalesce(pr.last_name)) as \"pFirstName\", "
					 + " 	pr.gender_id as \"genderId\", "
					 + " 	gm.gender_code as \"genderCode\", "
					 + " 	to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
					 + "    concat( extract( year from AGE( current_timestamp, pr.dob )), 'Y ', extract( month from AGE( current_timestamp, pr.dob )), 'M ', extract( day from AGE( current_timestamp, pr.dob )), 'D ' ) as \"ageStr\","
					 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"dFirstName\", "
					 + " 	to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
					 + " 	to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
					 + " 	spd.payment_entitlement_id as \"paymentEntitlementId\","
					 + "(case when spd.payment_entitlement_id = 2 then ( select insurance.insurance_desc from "
					 + " adt.t_insurance insurance where insurance.insurance_id = spd.insurance_id ) else ( "
					 + " select tcm.company_desc from "
					 + " public.t_company_master tcm where tcm.company_id = spd.payee_id )end ) as \"associateCompanyDesc\", "
					 + " 	spd.contract_id as \"contractId\", "
					 + " 	spd.payee_id as \"payeeId\", "
					 + " 	pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
					 + " 	bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
					 + " 	bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
					 + " 	bedCatMst.bed_category_id as \"bedCategoryId\", "
					 + " 	bm.floor_id as \"floorId\", "
					 + " 	bm.bed_number as \"bedNumber\", "
					 + " 	bm.bed_id as \"bedId\", "
					 + " 	bm.ward_id as \"wardId\", "
					 + " 	bm.bed_status_id as \"bedStatusId\", "
					 + " 	ward.ward_name as \"wardName\", "
					 + "  	ward.is_labor_ward as \"isLaborWard\","
					 + "  	ward.is_hemodialysis_ward as \"ishemodialysisward\","
					 + " 	adl.is_short_leave as \"isShortLeave\", "
					 + " 	adl.room_id as \"roomId\", "
					 + "    'Completed' as \"housekeepingStatusDesc\", "
					 + " 	pr.patient_category_id as \"patientCategoryId\","
					 + "    (select transfer_request_id  from otims.t_transfer_request where admission_id = admission.admission_id  and ot_transfer_status = 6 and status = 'A'  limit 1 ) as \"otTransfer\", "  
					 + "    (select obgyn_speciality_id from t_unit_application_billing_configuration  where status='A' and unit_id=bm.unit_id and obgyn_speciality_id=admission.speciality_id ) as \"obgynSpecialityId\" , "
					 + "    (select hemodialysis_speciality_id from t_unit_application_billing_configuration  where status='A' and unit_id=bm.unit_id and hemodialysis_speciality_id=admission.speciality_id ) as \"hemodialysisSpecialityId\" ,"
					 + " 	dis.discharge_id as \"dischargeId\", dis.discharge_status_id as \"dischargeStatusId\",dis.is_death as \"isDeath\","
					 + " 	mews.mews_mapper_id as \"mewsMapperId\", "
					 + " 	mews.total_score as \"totalScore\", "
//					 + " 	( "
//					 + " 		select cast(count(orderDetails.order_details_id) as text ) as \"labOrder\" "
//					 + " 	from "
//					 + " 		public.t_order_details orderDetails "
//					 + " 	inner join public.t_order_master orderM on "
//					 + " 		orderM.order_id = orderDetails.order_id "
//					 + " 	inner join service.m_service_master service on "
//					 + " 		service.service_master_id = orderDetails.service_id"
//					 + "    inner join service.t_unit_service_master_details usmd on " 
//					 + "        usmd.service_master_id=service.service_master_id and usmd.status ='A' "
//					 + " 	where "
//					 + " 		orderM.admission_id = admission.admission_id "
//					 + " 		and orderDetails.service_rendered = 0 "
//					 + " 		and orderDetails.status = 'A' "
//					 + " 		and (orderDetails.order_details_id not in (select coalesce(am.order_details_id,0) from opd.m_appointment_master am where am.unit_id = orderM.org_unit_id and orderM.admission_id =  am.admission_id) "
//					 + "             or usmd.is_cyclic ='Y') "
//					 + " 		and service.speciality_id IN (:specialityIds)), "
						//					 code added by Rahul starts-
					// + " cast(coalesce (A.labOrder,0) as text ) as \"labOrder\" , "
					 //					 code added by Rahul ends-
					 + " 	( "
					 + " 		select cast(count(dietOrd.ipd_diet_order_id) as text ) as \"nbm\" "
					 + " 	from "
					 + " 		adt.t_ipd_diet_order dietOrd "
					 + " 	where "
					 + " 		dietOrd.admission_id = admission.admission_id "
					 + " 		and dietOrd.is_nbm = 'Y' ), "
					 + " 	( "
					 + " 		select cast(count(prm.pro_req_id) as text ) as \"procedureOrder\" "
					 + " 	from "
					 + " 		otims.t_procedure_req_mst prm "
					 + " 	where "
					 + " 		prm.admission_id = admission.admission_id "
					 + " 		and prm.procedure_status_id = 1 ), "
					 + " 	( "
					 + " 		select cast(count(alrgyMap.allergy_patient_mapp_id) as text ) as \"allergy\" "
					 + " 	from "
					 + " 		patient.t_allergy_patient_mapper alrgyMap "
					 + " 	where "
					 + " 		alrgyMap.patient_id = admission.patient_id"
					 + " and alrgyMap.is_no_known_allergies!='Y') "
//					 + "( "  
//					 + "	select "  
//					 + "		cast(count( pres.id ) as text) as \"drugAdministration\" "  
//					 + "	from"  
//					 + "		pharmacy.t_prescription pres"  
//					 + "	inner join pharmacy.t_prescription_detail pdl on"  
//					 + "		pres.id = pdl.prescription_id"  
//					 + "	inner join pharmacy.t_prescription_frequency_detail pfd on"  
//					 + "		pdl.id = pfd.prescription_detail_id"  
//					 + "	where" 
//					 + "		pres.admission_id = admission.admission_id"  
//					 + "		and pfd.stop_date >= :currentDate "  
//					 + " ) "
					  //					 code added by Rahul starts-
					// + " cast(coalesce (B.drugAdministration,0) as text) as \"drugAdministration\" "
					 //					 code added by Rahul ends-
					 + " from "
					 + " 	adt.m_bed_master bm "
					 + " left join adt.t_admission_details adl on "
					 + " 	bm.bed_id = adl.bed_id "
					 + " 	and adl.status = 'A' "
					 + " 	and adl.nursing_acceptance = 'Y' "
					 + " left join adt.t_admission admission on "
					 + " 	adl.admission_id = admission.admission_id "
					 + " 	and admission.status = 'A' "
					 + " 	and admission.organization_id = bm.organization_id "
					 + " 	and admission.unit_id = bm.unit_id "
					 + " 	and admission.visit_type_id IN (2,3) "
					 + " left join patient.t_patient_registration pr on "
					 + " 	admission.patient_id = pr.patient_id "
					 + " left join public.m_gender_master gm on "
					 + " 	pr.gender_id = gm.gender_id "
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pr.prefix_id "
					 + " left join doctor.m_doctor_master doc on "
					 + " 	admission.doctor_id = doc.doctor_id "
					 + " left join doctor.m_speciality_master sp on "
					 + " 	admission.speciality_id = sp.speciality_id "
					 + " inner join adt.m_ward_master ward on "
					 + " 	bm.ward_id = ward.ward_id "
					 + " left join opd.t_patient_sponsor_details spd on "
					 + " 	spd.admission_id = admission.admission_id "
					 + " 	and spd.priority_id = 1 "
					 + " 	and spd.status = 'A' "
					 + " left join adt.m_bed_category_master bedCatMst on "
					 + " 	bm.bed_category_id = bedCatMst.bed_category_id "
					 + " left join public.m_payment_entitlement_master pe on "
					 + " 	spd.payment_entitlement_id = pe.payment_entitlement_id "
					 + " left join adt.t_discharge dis on "
					 + " 	dis.admission_id = admission.admission_id "
					 + " 	and dis.status = 'A' "
					 + " left join adt.t_mews_mapper mews on "
					 + " 	mews.admission_id = admission.admission_id  "
					 + " 	and mews.status = 'A' "
//					 + " left outer join adt.t_housekeeping housekeepi on "
//					 + "	bm.bed_id = housekeepi.bed_id "
//					 + " 	and housekeepi.status = 'A' "
//					 + " left outer join adt.m_housekeeping_status_master housekeepistatus on " 
//					 + "	housekeepi.housekeeping_status_id = housekeepistatus.housekeeping_status_id"
//					 code added by Rahul starts-
					/* + " left join "
					 + " ( select count(orderDetails.order_details_id) as labOrder , "
					 + " ta.admission_id " 
					 + " from public.t_order_details orderDetails " + 
					 "		 " + 
					 "	inner join public.t_order_master orderM on " + 
					 "		orderM.order_id = orderDetails.order_id " + 
					 "	inner join service.m_service_master service on " + 
					 "		service.service_master_id = orderDetails.service_id " + 
					 "	inner join service.t_unit_service_master_details usmd on " + 
					 "		usmd.service_master_id = service.service_master_id " + 
					 "		and usmd.status = 'A' " + 
					 "	inner join adt.t_admission ta on " + 
					 "		ta.admission_id = orderM.admission_id " + 
					 "		and ta.status = 'A' " + 
					 "	inner join adt.t_admission_details tad on " + 
					 "		tad.admission_id = ta.admission_id " + 
					 "	inner join adt.m_bed_master bm on " + 
					 "		bm.bed_id = tad.bed_id " + 
					 "	where " + 
					 "		orderDetails.service_rendered = 0 " + 
					 "		and orderDetails.status ='A' " + 
					 "		and service.speciality_id in (:specialityIds) " + 
					 "			and bm.ward_id in (:wardList) " + 
					 "		group by " + 
					 "			ta.admission_id ) A on " + 
					 "	A.admission_id = admission.admission_id " + */
					/* " left join " + 
					 "		( " + 
					 "	select " + 
					 "		 count(pres.id) as drugAdministration , " + 
					 "		pres.admission_id " + 
					 "	from " + 
					 "		pharmacy.t_prescription pres " + 
					 "	inner join pharmacy.t_prescription_detail pdl on " + 
					 "		pres.id = pdl.prescription_id " + 
					 "	inner join pharmacy.t_prescription_frequency_detail pfd on " + 
					 "		pdl.id = pfd.prescription_detail_id " + 
					 "	where " + 
					 "		pfd.stop_date >= :currentDate " + 
					 "	group by " + 
					 "		pres.admission_id " + 
					 "	) B on " + 
					 "		B.admission_id = admission.admission_id	 " + */
					 +" "
//					 code added by Rahul ends-
					 
					/* + " left join adt.m_bed_management_counts bedCounts on "
					 + " 	bedCounts.admission_id = admission.admission_id "*/
					 + " where "
					 + " 	bm.status = 'A' "
					 + " 	and bm.organization_id = :organizationId "
					 + " 	and admission.visit_type_id IN (2,3) "
					 + " 	and bm.unit_id = :unitId "
					 + "	and ward.is_er = 'N' "
					);
				if(bedMasterDto.getDoctorId()!=null && bedMasterDto.getDoctorId()>0)
				{
					query.append( " and admission.doctor_id="+bedMasterDto.getDoctorId());
					 
					if(search==true)
						query = searchQuery(bedMasterDto,query);

					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							//.setParameterList("specialityIds", specialityIds)
							.setParameter("unitId", bedMasterDto.getUnitId());
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql.setParameter("patientId", bedMasterDto.getPatientId());
					}
							//.setDate("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0))
					bedManagementList=	sql.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();

					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				
				else if((bedMasterDto.getWardList()!=null && bedMasterDto.getWardList().size()>0) && bedMasterDto.getBedList()==null)
				{
					query.append(" and bm.ward_id IN (:wardList)");
					
					if(search==true)
						query = searchQuery(bedMasterDto,query);
					
					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql2 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameter("unitId", bedMasterDto.getUnitId())
//							.setParameterList("specialityIds", specialityIds)
							.setParameterList("wardList", bedMasterDto.getWardList());
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql2.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql2.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql2.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql2.setParameter("patientId", bedMasterDto.getPatientId());
					}
							//.setDate("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0))
					bedManagementList=	sql2.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				else if((bedMasterDto.getBedList()!=null && bedMasterDto.getBedList().size()>0) && bedMasterDto.getWardList()==null)
				{
					query.append(" and bm.bed_id IN (:bedList)");
					
					if(search==true)
						query = searchQuery(bedMasterDto,query);
					
					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql3 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameter("unitId", bedMasterDto.getUnitId())
//							.setParameterList("specialityIds", specialityIds)
							.setParameterList("bedList", bedMasterDto.getBedList());
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql3.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql3.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql3.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql3.setParameter("patientId", bedMasterDto.getPatientId());
					}
							//.setDate("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0))
					bedManagementList=	sql3.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				else{
					
					StringBuilder normalquery = new StringBuilder(" select "
							 + " 	admission.admission_id as \"admissionId\", "
							 + " 	admission.patient_id as \"patientId\", "
							 + " 	admission.t_patient_id as \"tPatientId\", "
							 + " 	admission.admission_number as \"admissionNumber\", "
							 + " 	admission.visit_type_id as \"visitTypeId\", "
							 + " 	admission.is_final_bill as \"isFinalBill\", "
							 + " 	admission.doctor_id as \"doctorId\", "
							 + " 	admission.speciality_id as \"specialityId\", "
							 + " 	sp.speciality_name as \"specialityName\", "
							 + " 	pr.uhid_number as \"UHIDNumber\", "
							 + "    pr.image_path as \"imagePath\", "
							 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), ' ', coalesce(pr.middle_name), ' ', coalesce(pr.last_name)) as \"pFirstName\", "
							 + "    concat( extract( year from AGE( current_timestamp, pr.dob )), 'Y ', extract( month from AGE( current_timestamp, pr.dob )), 'M ', extract( day from AGE( current_timestamp, pr.dob )), 'D ' ) as \"ageStr\","
							 + " 	pr.gender_id as \"genderId\", "
							 + " 	gm.gender_code as \"genderCode\", "
							 + " 	to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
							 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"dFirstName\", "
							 + " 	to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
							 + " 	to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
							 + " 	spd.payment_entitlement_id as \"paymentEntitlementId\", "
							 + "(case when spd.payment_entitlement_id = 2 then ( select insurance.insurance_desc from "
							 + " adt.t_insurance insurance where insurance.insurance_id = spd.insurance_id ) else ( "
							 + " select tcm.company_desc from "
							 + " public.t_company_master tcm where tcm.company_id = spd.payee_id )end ) as \"associateCompanyDesc\", "
							 + " 	dis.discharge_id as \"dischargeId\", dis.discharge_status_id as \"dischargeStatusId\",dis.is_death as \"isDeath\","
							 + " 	spd.contract_id as \"contractId\", "
							 + " 	spd.payee_id as \"payeeId\", "
							 + " 	pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
							 + " 	bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
							 + " 	bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
							 + " 	bedCatMst.bed_category_id as \"bedCategoryId\", "
							 + " 	bm.floor_id as \"floorId\", "
							 + " 	bm.bed_number as \"bedNumber\", "
							 + " 	bm.bed_id as \"bedId\", "
							 + " 	bm.ward_id as \"wardId\", "
							 + " 	bm.bed_status_id as \"bedStatusId\", "
							 + " 	ward.ward_name as \"wardName\", "
							 + "  	ward.is_labor_ward as \"isLaborWard\","
							 + "  	ward.is_hemodialysis_ward as \"ishemodialysisward\","
							 + " 	adl.is_short_leave as \"isShortLeave\", "
							 + " 	adl.room_id as \"roomId\", "
							 + "    housekeepistatus.housekeeping_status_desc as \"housekeepingStatusDesc\", "
							 + " (select transfer_request_id  from otims.t_transfer_request where admission_id = admission.admission_id  and ot_transfer_status = 6 and status = 'A'  limit 1 ) as \"otTransfer\", "  
							 + " 	pr.patient_category_id as \"patientCategoryId\" "
							 + " from "
							 + " 	adt.m_bed_master bm "
							 + " left join adt.t_admission_details adl on "
							 + " 	bm.bed_id = adl.bed_id "
							 + " 	and adl.status = 'A' "
							 + " 	and adl.nursing_acceptance = 'Y' "
							 + " left join adt.t_admission admission on "
							 + " 	adl.admission_id = admission.admission_id "
							 + " 	and admission.status = 'A' "
							 + " 	and admission.organization_id = bm.organization_id "
							 + " 	and admission.unit_id = bm.unit_id "
							 + " 	and admission.visit_type_id IN (2,3) "
							 + " left join patient.t_patient_registration pr on "
							 + " 	admission.patient_id = pr.patient_id "
							 + " left join public.m_gender_master gm on "
							 + " 	pr.gender_id = gm.gender_id "
							 + " left join m_prefix_master pre on "
							 + " 	pre.prefix_id = pr.prefix_id "
							 + " left join doctor.m_doctor_master doc on "
							 + " 	admission.doctor_id = doc.doctor_id "
							 + " left join doctor.m_speciality_master sp on "
							 + " 	admission.speciality_id = sp.speciality_id "
							 + " inner join adt.m_ward_master ward on "
							 + " 	bm.ward_id = ward.ward_id "
							 + " left join opd.t_patient_sponsor_details spd on "
							 + " 	spd.admission_id = admission.admission_id "
							 + " 	and spd.priority_id = 1 "
							 + " 	and spd.status = 'A' "
							 + " left join adt.m_bed_category_master bedCatMst on "
							 + " 	bm.bed_category_id = bedCatMst.bed_category_id "
							 + " left join public.m_payment_entitlement_master pe on "
							 + " 	spd.payment_entitlement_id = pe.payment_entitlement_id "
							 + " left join adt.t_discharge dis on "
							 + " 	dis.admission_id = admission.admission_id "
							 + " 	and dis.status = 'A' "
							 + " left join adt.t_mews_mapper mews on "
							 + " 	mews.admission_id = admission.admission_id  "
							 + " 	and mews.status = 'A' "
							 + " left join "
							 + " (SELECT DISTINCT ON (bed_id) * "
							 + " FROM adt.t_housekeeping "
							 + " WHERE status = 'A' ORDER BY bed_id, housekeeping_id DESC) "
							 + " housekeepi on bm.bed_id = housekeepi.bed_id "
//							 + " left outer join adt.t_housekeeping housekeepi on "
//							 + "	bm.bed_id = housekeepi.bed_id "
//							 + " 	and housekeepi.status = 'A' "
							 + " left outer join adt.m_housekeeping_status_master housekeepistatus on " 
							 + "	housekeepi.housekeeping_status_id = housekeepistatus.housekeeping_status_id "
							/* + " left join adt.m_bed_management_counts bedCounts on "
							 + " 	bedCounts.admission_id = admission.admission_id "*/
							 + " where "
							 + " 	bm.status = 'A' "
							 + " 	and bm.organization_id = :organizationId "
							 + " 	and bm.unit_id = :unitId "
							 + "	and ward.is_er = 'N' ");
					
					if(search==true)
						normalquery = searchQuery(bedMasterDto,normalquery);
					
					normalquery.append("	order by admission.admission_id,bm.bed_number ");
					Query sql4 = sessionFactory.getCurrentSession().createSQLQuery(normalquery.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameter("unitId", bedMasterDto.getUnitId());
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql4.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql4.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql4.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql4.setParameter("patientId", bedMasterDto.getPatientId());
					}
					bedManagementList=	sql4.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();

					List<BedManagementDto> filteredList =  Optional.ofNullable(bedManagementList)
							.orElse(Collections.emptyList()).stream()
							.filter(bed -> bed.getAdmissionId() != null
							 && bed.getPatientId() != null)
							.collect(Collectors.toList());

					return new Response(SUCCESS, null, null, filteredList, null);
				}
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
//		 return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getDataForBedManagementDataForER(BedMasterDto bedMasterDto,boolean search) throws ApplicationException {
		try {
			
			List<Integer> specialityIds = new LinkedList<>();
			if(bedMasterDto.getLabSpecialityId()!=null)
				specialityIds.add(bedMasterDto.getLabSpecialityId());
			if(bedMasterDto.getRadiologySpecialityId()!=null)
				specialityIds.add(bedMasterDto.getRadiologySpecialityId());
			if(bedMasterDto.getHemodialysisSpecialityId()!=null)
				specialityIds.add(bedMasterDto.getHemodialysisSpecialityId());
			
			List<BedManagementDto> bedManagementList = null;
			StringBuilder query = new StringBuilder(" select distinct "
					 + " 	admission.admission_id as \"admissionId\", "
					 + " 	admission.patient_id as \"patientId\", "
					 + " 	admission.t_patient_id as \"tPatientId\", "
					 + " 	admission.admission_number as \"admissionNumber\", "
					 + " 	admission.visit_type_id as \"visitTypeId\", "
					 + " 	admission.is_final_bill as \"isFinalBill\", "
					 + " 	admission.doctor_id as \"doctorId\", "
					 + " 	admission.speciality_id as \"specialityId\", "
					 + " 	sp.speciality_name as \"specialityName\", "
					 + " 	pr.uhid_number as \"UHIDNumber\", "
					 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), ' ', coalesce(pr.middle_name), ' ', coalesce(pr.last_name)) as \"pFirstName\", "
					 + "    concat( extract( year from AGE( current_timestamp, pr.dob )), 'Y ', extract( month from AGE( current_timestamp, pr.dob )), 'M ', extract( day from AGE( current_timestamp, pr.dob )), 'D ' ) as \"ageStr\","
					 + " 	pr.gender_id as \"genderId\", "
					 + " 	gm.gender_code as \"genderCode\", "
					 + " 	to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
					 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"dFirstName\", "
					 + " 	to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
					 + " 	to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
					 + " 	spd.payment_entitlement_id as \"paymentEntitlementId\", "
					 + "   (case when spd.payment_entitlement_id = 2 then ( select insurance.insurance_desc from "
					 + " adt.t_insurance insurance where insurance.insurance_id = spd.insurance_id ) else ( "
					 + " select tcm.company_desc from "
					 + " public.t_company_master tcm where tcm.company_id = spd.payee_id )end ) as \"associateCompanyDesc\", "
					 + " 	spd.contract_id as \"contractId\", "
					 + " 	spd.payee_id as \"payeeId\", "
					 + " 	pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
					 + " 	bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
					 + " 	bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
					 + " 	bedCatMst.bed_category_id as \"bedCategoryId\", "
					 + " 	bm.floor_id as \"floorId\", "
					 + " 	bm.bed_number as \"bedNumber\", "
					 + " 	bm.bed_id as \"bedId\", "
					 + " 	bm.ward_id as \"wardId\", "
					 + " 	bm.bed_status_id as \"bedStatusId\", "
					 + " 	ward.ward_name as \"wardName\", "
					 + "  	ward.is_labor_ward as \"isLaborWard\","
					 + "  	ward.is_hemodialysis_ward as \"ishemodialysisward\","
					 + " 	adl.is_short_leave as \"isShortLeave\", "
					 + " 	adl.room_id as \"roomId\", "
					 + " 	pr.patient_category_id as \"patientCategoryId\", "
					 + " (select transfer_request_id  from otims.t_transfer_request where admission_id = admission.admission_id  and ot_transfer_status = 6 and status = 'A'  limit 1 ) as \"otTransfer\", " 
					 + "    (select obgyn_speciality_id from t_unit_application_billing_configuration  where status='A' and unit_id=bm.unit_id and obgyn_speciality_id=admission.speciality_id ) as \"obgynSpecialityId\" , "
					 + "    (select hemodialysis_speciality_id from t_unit_application_billing_configuration  where status='A' and unit_id=bm.unit_id and hemodialysis_speciality_id=admission.speciality_id ) as \"hemodialysisSpecialityId\" ,"
					 + " 	dis.discharge_id as \"dischargeId\", dis.discharge_status_id as \"dischargeStatusId\",dis.is_death as \"isDeath\","
					 + " 	mews.mews_mapper_id as \"mewsMapperId\", "
					 + " 	mews.total_score as \"totalScore\", "
					 + "    housekeepistatus.housekeeping_status_desc as \"housekeepingStatusDesc\", "
//					 + " 	( "
//					 + " 		select cast(count(orderDetails.order_details_id) as text ) as \"labOrder\" "
//					 + " 	from "
//					 + " 		public.t_order_details orderDetails "
//					 + " 	inner join public.t_order_master orderM on "
//					 + " 		orderM.order_id = orderDetails.order_id "
//					 + " 	inner join service.m_service_master service on "
//					 + " 		service.service_master_id = orderDetails.service_id "
//					 + " 	where "
//					 + " 		orderM.admission_id = admission.admission_id "
//					 + " 		and orderDetails.service_rendered = 0 "
//					 + " 		and orderDetails.status = 'A' "
//					 + " 		and orderDetails.order_details_id not in (select coalesce(am.order_details_id,0) from opd.m_appointment_master am where am.unit_id = orderM.org_unit_id and orderM.admission_id =  am.admission_id) "
//					 + " 		and service.speciality_id IN (:specialityIds)), "
                     + " cast(coalesce (A.labOrder,0) as text ) as \"labOrder\" , "
                     + " cast(coalesce (C.pocOrders,0) as text ) as \"pocOrders\" , "
					 + " 	( "
					 + " 		select cast(count(dietOrd.ipd_diet_order_id) as text ) as \"nbm\" "
					 + " 	from "
					 + " 		adt.t_ipd_diet_order dietOrd "
					 + " 	where "
					 + " 		dietOrd.admission_id = admission.admission_id "
					 + " 		and dietOrd.is_nbm = 'Y' ), "
					 + " 	( "
					 + " 		select cast(count(prm.pro_req_id) as text ) as \"procedureOrder\" "
					 + " 	from "
					 + " 		otims.t_procedure_req_mst prm "
					 + " 	where "
					 + " 		prm.admission_id = admission.admission_id "
					 + " 		and prm.procedure_status_id = 1 ), "
					 + " 	( "
					 + " 		select cast(count(alrgyMap.allergy_patient_mapp_id) as text ) as \"allergy\" "
					 + " 	from "
					 + " 		patient.t_allergy_patient_mapper alrgyMap "
					 + " 	where "
					 + " 		alrgyMap.patient_id = admission.patient_id"
					 + " and alrgyMap.is_no_known_allergies!='Y'),"
//					 + "( "  
//					 + "	select "  
//					 + "		cast(count( pres.id ) as text) as \"drugAdministration\" "  
//					 + "	from"  
//					 + "		pharmacy.t_prescription pres"  
//					 + "	inner join pharmacy.t_prescription_detail pdl on"  
//					 + "		pres.id = pdl.prescription_id"  
//					 + "	inner join pharmacy.t_prescription_frequency_detail pfd on"  
//					 + "		pdl.id = pfd.prescription_detail_id"  
//					 + "	where" 
//					 + "		pres.admission_id = admission.admission_id"  
//					 + "		and pfd.stop_date >= :currentDate "
//					 + "        and pdl.is_medication_stop='N'"  
//					 + " ) "
					 + " adl.triage_category_id as \"triageCategoryId\" ,"
                     + " cast(coalesce (B.drugAdministration,0) as text) as \"drugAdministration\", "
					 + " roomMst.room_desc as \"roomDesc\" "
					 + " from "
					 + " 	adt.m_bed_master bm "
					 + " left join adt.t_admission_details adl on "
					 + " 	bm.bed_id = adl.bed_id "
					 + " 	and adl.status = 'A' "
					 + " 	and adl.nursing_acceptance = 'Y' "
					 + " left join adt.t_admission admission on "
					 + " 	adl.admission_id = admission.admission_id "
					 + " 	and admission.status = 'A' "
					 + " 	and admission.organization_id = bm.organization_id "
					 + " 	and admission.unit_id = bm.unit_id "
					 + " 	and admission.visit_type_id IN (4) "
					 + "	left join adt.m_room_master roomMst on " +
							"		roomMst.room_id = bm.room_id "
					 + " left join patient.t_patient_registration pr on "
					 + " 	admission.patient_id = pr.patient_id "
					 + " left join public.m_gender_master gm on "
					 + " 	pr.gender_id = gm.gender_id "
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pr.prefix_id "
					 + " left join doctor.m_doctor_master doc on "
					 + " 	admission.doctor_id = doc.doctor_id "
					 + " left join doctor.m_speciality_master sp on "
					 + " 	admission.speciality_id = sp.speciality_id "
					 + " inner join adt.m_ward_master ward on "
					 + " 	bm.ward_id = ward.ward_id "
					 + " left join opd.t_patient_sponsor_details spd on "
					 + " 	spd.admission_id = admission.admission_id "
					 + " 	and spd.priority_id = 1 "
					 + " 	and spd.status = 'A' "
					 + " left join adt.m_bed_category_master bedCatMst on "
					 + " 	bm.bed_category_id = bedCatMst.bed_category_id "
					 + " left join public.m_payment_entitlement_master pe on "
					 + " 	spd.payment_entitlement_id = pe.payment_entitlement_id "
					 + " left join adt.t_discharge dis on "
					 + " 	dis.admission_id = admission.admission_id "
					 + " 	and dis.status = 'A' "
					 + " left join adt.t_mews_mapper mews on "
					 + " 	mews.admission_id = admission.admission_id  "
					 + " 	and mews.status = 'A' "
					 + " left outer join adt.t_housekeeping housekeepi on "
					 + "	bm.bed_id = housekeepi.bed_id "
					 + " 	and housekeepi.status = 'A' "
					 + " left outer join adt.m_housekeeping_status_master housekeepistatus on " 
					 + "	housekeepi.housekeeping_status_id = housekeepistatus.housekeeping_status_id "
//					 code added by Rahul starts-
					 + " left join "
					 + " ( select count(orderDetails.order_details_id) as labOrder , "
					 + " ta.admission_id " 
					 + " from public.t_order_details orderDetails " + 
					 "		 " + 
					 "	inner join public.t_order_master orderM on " + 
					 "		orderM.order_id = orderDetails.order_id " + 
					 "	inner join service.m_service_master service on " + 
					 "		service.service_master_id = orderDetails.service_id " + 
					 "	inner join service.t_unit_service_master_details usmd on " + 
					 "		usmd.service_master_id = service.service_master_id " + 
					 "		and usmd.status = 'A' " + 
					 "	inner join adt.t_admission ta on " + 
					 "		ta.admission_id = orderM.admission_id " + 
					 "		and ta.status = 'A' " + 
					 "	inner join adt.t_admission_details tad on " + 
					 "		tad.admission_id = ta.admission_id " + 
					 "	inner join adt.m_bed_master bm on " + 
					 "		bm.bed_id = tad.bed_id " + 
					 "	where " + 
					 "		orderDetails.service_rendered = 0 " + 
					 "		and orderDetails.status ='A' " + 
					 "		and service.speciality_id in (:specialityIds) " + 
					 "			and bm.ward_id in (:wardList) " + 
					 "		group by " + 
					 "			ta.admission_id ) A on " + 
					 "	A.admission_id = admission.admission_id " + 
					 " left join " + 
					 "		( " + 
					 "	select " + 
					 "		 count(pres.id) as drugAdministration , " + 
					 "		pres.admission_id " + 
					 "	from " + 
					 "		pharmacy.t_prescription pres " + 
					 "	inner join pharmacy.t_prescription_detail pdl on " + 
					 "		pres.id = pdl.prescription_id " + 
					 "	inner join pharmacy.t_prescription_frequency_detail pfd on " + 
					 "		pdl.id = pfd.prescription_detail_id " + 
					 "	where " + 
					 "		pfd.stop_date >= :currentDate " +
					 "	group by " +
					 "		pres.admission_id " + 
					 "	) B on " + 
					 "		B.admission_id = admission.admission_id	 "
//					 code added by Rahul ends-
//					 code added by Avishkar for fetching isPOC services on map of ward bed ER
					+ " left join "
					+ " ( select count(unitSer.service_master_id) as pocOrders , "
					+ " adm.admission_id "
					+ " from public.t_order_details orderDetailsPoc " +
					"		 " +
					"	inner join public.t_order_master orderM on " +
					"		orderM.order_id = orderDetailsPoc.order_id " +
					"	inner join service.m_service_master serviceM on " +
					"		serviceM.service_master_id = orderDetailsPoc.service_id " +
					"	inner join service.t_unit_service_master_details unitSer on " +
					"		unitSer.service_master_id = serviceM.service_master_id " +
					"		and unitSer.status = 'A' " +
					"		and unitSer.is_poc = 'Y' " +
					"	inner join adt.t_admission adm on " +
					"		adm.admission_id = orderM.admission_id " +
					"		and adm.status = 'A' " +
					"	inner join adt.t_admission_details admDtl on " +
					"		admDtl.admission_id = adm.admission_id " +
					"	inner join adt.m_bed_master bedM on " +
					"		bedM.bed_id = admDtl.bed_id " +
					"	where " +
//					"		orderDetailsPoc.service_rendered = 0  and " +
					"		 orderDetailsPoc.status ='A' " +
					"		and serviceM.speciality_id in (:specialityIds) " +
					"			and bedM.ward_id in (:wardList) " +
					"		group by " +
					"			adm.admission_id ) C on " +
					"	C.admission_id = admission.admission_id "
//					==========isPOC code end==========
					 + " where "
					 + " 	bm.status = 'A' "
					 + " 	and bm.organization_id = :organizationId "
					 + " 	and bm.unit_id = :unitId "
					 + "	and ward.is_er = 'Y' "
					);
				if(bedMasterDto.getIsClinical()!=null)
				{
					if(search==true)
						query = searchQuery(bedMasterDto,query);
					
					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameterList("specialityIds", specialityIds)
							.setParameter("unitId", bedMasterDto.getUnitId())
							.setParameter("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0))
							.setParameterList("wardList", bedMasterDto.getWardList());
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql.setParameter("patientId", bedMasterDto.getPatientId());
					}
					bedManagementList=	sql.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					if(bedManagementList != null && !bedManagementList.isEmpty()) {
						List<BedManagementDto> filteredList = bedManagementList.stream()
								.filter(bed -> bed.getAdmissionId() != null)
								.collect(Collectors.toList());
						bedManagementList = filteredList;
					}

					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				if(bedMasterDto.getDoctorId()!=null && bedMasterDto.getDoctorId()>0)
				{
					query.append( " and admission.doctor_id="+bedMasterDto.getDoctorId());
					
					if(search==true)
						query = searchQuery(bedMasterDto,query);
					
					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql1 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameterList("specialityIds", specialityIds)
							.setParameter("unitId", bedMasterDto.getUnitId())
							.setParameter("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0));
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql1.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql1.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql1.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql1.setParameter("patientId", bedMasterDto.getPatientId());
					}
					bedManagementList = sql1.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					if(bedManagementList != null && !bedManagementList.isEmpty()) {
						List<BedManagementDto> filteredList = bedManagementList.stream()
								.filter(bed -> bed.getAdmissionId() != null)
								.collect(Collectors.toList());
						bedManagementList = filteredList;
					}
					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				else if((bedMasterDto.getWardList()!=null && bedMasterDto.getWardList().size()>0) && bedMasterDto.getBedList()==null)
				{
					query.append(" and bm.ward_id IN (:wardList)");
					
					if(search==true)
						query = searchQuery(bedMasterDto,query);
					
					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql2 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameter("unitId", bedMasterDto.getUnitId())
							.setParameterList("specialityIds", specialityIds)
							.setParameterList("wardList", bedMasterDto.getWardList())
							.setParameter("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0));
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql2.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql2.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql2.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql2.setParameter("patientId", bedMasterDto.getPatientId());
					}
					bedManagementList=	sql2.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					if(bedManagementList != null && !bedManagementList.isEmpty()) {
						List<BedManagementDto> filteredList = bedManagementList.stream()
								.filter(bed -> bed.getAdmissionId() != null)
								.collect(Collectors.toList());
						bedManagementList = filteredList;
					}
					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				else if((bedMasterDto.getBedList()!=null && bedMasterDto.getBedList().size()>0) && bedMasterDto.getWardList()==null)
				{
					query.append(" and bm.bed_id IN (:bedList)");
					
					if(search==true)
						query = searchQuery(bedMasterDto,query);
					
					query.append("	order by admission.admission_id,bm.bed_number ");
					Query sql3 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameter("unitId", bedMasterDto.getUnitId())
							.setParameterList("specialityIds", specialityIds)
							.setParameterList("bedList", bedMasterDto.getBedList())
							.setParameter("currentDate", TimeZoneComponent.getDateByzone(bedMasterDto.getUnitId() != null ? bedMasterDto.getUnitId():0));
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql3.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId()  != null && bedMasterDto.getWardId()>0) {
						sql3.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql3.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql3.setParameter("patientId", bedMasterDto.getPatientId());
					}
					bedManagementList=	sql3.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					if(bedManagementList != null && !bedManagementList.isEmpty()) {
						List<BedManagementDto> filteredList = bedManagementList.stream()
								.filter(bed -> bed.getAdmissionId() != null)
								.collect(Collectors.toList());
						bedManagementList = filteredList;
					}
					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
				else{
					
					StringBuilder normalquery = new StringBuilder(" select "
							 + " 	admission.admission_id as \"admissionId\", "
							 + " 	admission.patient_id as \"patientId\", "
							 + " 	admission.t_patient_id as \"tPatientId\", "
							 + " 	admission.admission_number as \"admissionNumber\", "
							 + " 	admission.visit_type_id as \"visitTypeId\", "
							 + " 	admission.doctor_id as \"doctorId\", "
							 + " 	admission.is_final_bill as \"isFinalBill\", "
							 + " 	admission.speciality_id as \"specialityId\", "
							 + " 	sp.speciality_name as \"specialityName\", "
							 + " 	pr.uhid_number as \"UHIDNumber\", "
							 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), ' ', coalesce(pr.middle_name), ' ', coalesce(pr.last_name)) as \"pFirstName\", "
							 + "    concat( extract( year from AGE( current_timestamp, pr.dob )), 'Y ', extract( month from AGE( current_timestamp, pr.dob )), 'M ', extract( day from AGE( current_timestamp, pr.dob )), 'D ' ) as \"ageStr\","
							 + " 	pr.gender_id as \"genderId\", "
							 + " 	gm.gender_code as \"genderCode\", "
							 + " 	to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
							 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"dFirstName\", "
							 + " 	to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
							 + " 	to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
							 + " 	spd.payment_entitlement_id as \"paymentEntitlementId\", "
							 + "   (case when spd.payment_entitlement_id = 2 then ( select insurance.insurance_desc from "
							 + " adt.t_insurance insurance where insurance.insurance_id = spd.insurance_id ) else ( "
							 + " select tcm.company_desc from "
							 + " public.t_company_master tcm where tcm.company_id = spd.payee_id )end ) as \"associateCompanyDesc\", "
							 + " 	spd.contract_id as \"contractId\", "
							 + " 	spd.payee_id as \"payeeId\", "
							 + " 	pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
							 + " 	bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
							 + " 	bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
							 + " 	bedCatMst.bed_category_id as \"bedCategoryId\", "
							 + "    housekeepistatus.housekeeping_status_desc as \"housekeepingStatusDesc\", "
							 + "(select transfer_request_id  from otims.t_transfer_request where admission_id = admission.admission_id  and ot_transfer_status = 6 and status = 'A'  limit 1 ) as \"otTransfer\", " 
							 + " 	dis.discharge_id as \"dischargeId\", dis.discharge_status_id as \"dischargeStatusId\",dis.is_death as \"isDeath\","
							 + " 	bm.floor_id as \"floorId\", "
							 + " 	bm.bed_number as \"bedNumber\", "
							 + " 	bm.bed_id as \"bedId\", "
							 + " 	bm.ward_id as \"wardId\", "
							 + " 	bm.bed_status_id as \"bedStatusId\", "
							 + " 	ward.ward_name as \"wardName\", "
							 + "  	ward.is_labor_ward as \"isLaborWard\","
							 + "  	ward.is_hemodialysis_ward as \"ishemodialysisward\","
							 + " 	adl.is_short_leave as \"isShortLeave\", "
							 + " 	adl.room_id as \"roomId\", "
							 + " adl.triage_category_id as \"triageCategoryId\" ,"
							 + " 	pr.patient_category_id as \"patientCategoryId\", "
							 + " roomMst.room_desc as \"roomDesc\" "
							 + " from "
							 + " 	adt.m_bed_master bm "
							 + " left join adt.t_admission_details adl on "
							 + " 	bm.bed_id = adl.bed_id "
							 + " 	and adl.status = 'A' "
							 + " 	and adl.nursing_acceptance = 'Y' "
							 + " left join adt.t_admission admission on "
							 + " 	adl.admission_id = admission.admission_id "
							 + " 	and admission.status = 'A' "
							 + " 	and admission.organization_id = bm.organization_id "
							 + " 	and admission.unit_id = bm.unit_id "
							 + " 	and admission.visit_type_id IN (4) "
							 + " left join patient.t_patient_registration pr on "
							 + " 	admission.patient_id = pr.patient_id "
							 + " left join public.m_gender_master gm on "
							 + " 	pr.gender_id = gm.gender_id "
							 + " left join m_prefix_master pre on "
							 + " 	pre.prefix_id = pr.prefix_id "
							 + " left join doctor.m_doctor_master doc on "
							 + " 	admission.doctor_id = doc.doctor_id "
							 + " left join doctor.m_speciality_master sp on "
							 + " 	admission.speciality_id = sp.speciality_id "
							 + " inner join adt.m_ward_master ward on "
							 + " 	bm.ward_id = ward.ward_id "
							 + " left join opd.t_patient_sponsor_details spd on "
							 + " 	spd.admission_id = admission.admission_id "
							 + " 	and spd.priority_id = 1 "
							 + " 	and spd.status = 'A' "
							 + " left outer join adt.t_housekeeping housekeepi on "
							 + "	bm.bed_id = housekeepi.bed_id "
							 + " 	and housekeepi.status = 'A' "
							 + " left outer join adt.m_housekeeping_status_master housekeepistatus on " 
							 + "	housekeepi.housekeeping_status_id = housekeepistatus.housekeeping_status_id "
							 + " left join adt.m_bed_category_master bedCatMst on "
							 + " 	bm.bed_category_id = bedCatMst.bed_category_id "
							 + " left join public.m_payment_entitlement_master pe on "
							 + " 	spd.payment_entitlement_id = pe.payment_entitlement_id "
							 + " left join adt.t_discharge dis on "
							 + " 	dis.admission_id = admission.admission_id "
							 + " 	and dis.status = 'A' "
							 + " left join adt.t_mews_mapper mews on "
							 + " 	mews.admission_id = admission.admission_id  "
							 + " 	and mews.status = 'A' "
							 + " left join adt.m_room_master roomMst on " +
							   "		roomMst.room_id = bm.room_id "
							/* + " left join adt.m_bed_management_counts bedCounts on "
							 + " 	bedCounts.admission_id = admission.admission_id "*/
							 + " where "
							 + " 	bm.status = 'A' "
							 + " 	and bm.organization_id = :organizationId "
							 + " 	and bm.unit_id = :unitId "
							 + "	and ward.is_er = 'Y' ");
					
					if(search==true)
						normalquery = searchQuery(bedMasterDto,normalquery);
					
						normalquery.append("	order by admission.admission_id,bm.bed_number ");
					Query sql = sessionFactory.getCurrentSession().createSQLQuery(normalquery.toString())
							.setParameter("organizationId", bedMasterDto.getOrganizationId())
							.setParameter("unitId", bedMasterDto.getUnitId());
					if(bedMasterDto.getFloorId() != null && bedMasterDto.getFloorId()>0){
						sql.setParameter("floorId", bedMasterDto.getFloorId());
					}
					if(bedMasterDto.getWardId() != null && bedMasterDto.getWardId()>0) {
						sql.setParameter("wardId", bedMasterDto.getWardId());
					}
					if(bedMasterDto.getBedCategoryId() != null && bedMasterDto.getBedCategoryId()>0) {
						sql.setParameter("bedCategoryId", bedMasterDto.getBedCategoryId());
					}
					if(bedMasterDto.getPatientId() != null && bedMasterDto.getPatientId()>0) {
						sql.setParameter("patientId", bedMasterDto.getPatientId());
					}
					bedManagementList= 	sql.setResultTransformer(Transformers.aliasToBean(BedManagementDto.class)).list();
					if(bedManagementList != null && !bedManagementList.isEmpty()) {
						List<BedManagementDto> filteredList = bedManagementList.stream()
								.filter(bed -> bed.getAdmissionId() != null)
								.collect(Collectors.toList());
						bedManagementList = filteredList;
					}
					return new Response(SUCCESS, null, null, bedManagementList, null);
				}
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
//		 return new Response(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Response getBedMovingBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_BED_MOVING_BED_LIST")
					.setParameter("orgId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset() : 0)
					.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response movingBedListCount(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(BedMaster.class);
			c.add(Restrictions.eq("organizationId", bedMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("isMovingBed", bedMasterDto.getIsMovingBed()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response activeMovingBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<BedMasterDto> bedMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("ACTIVE_MOVING_BED_LIST")
					.setParameter("orgId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDialysisChair(BedMasterDto bedMasterDto) {
		try {
			List<BedMasterDto> bedMstrDto = sessionFactory.getCurrentSession().createSQLQuery("select bed_id as\"bedId\",bed_number as\"bedNumber\",item_id as \"itemId\" "
					+ " from adt.m_bed_master "
					+ " where item_id !=0 "
					+ " and unit_id =:unitId "
					+ " and organization_id  =:orgId "
					+ " ORDER BY bed_number ASC ")
					.setParameter("orgId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMstrDto, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response bedMasterUniversalSearch(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			
			if(bedMasterDto.getIsMovingBed()!='Y') {
			List<BedMasterDto> bedMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_BED_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setParameter("bedDesc", "%"+bedMasterDto.getBedDesc().trim().toLowerCase()+"%")
					.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset() : 0)
					.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtoList, null);
			}else {
				List<BedMasterDto> movingBedMasterDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PAGINATED_MOVING_BED_MASTER_DETAILS_LIST_SEARCH")
						.setParameter("orgId", bedMasterDto.getOrganizationId())
						.setParameter("unitId", bedMasterDto.getUnitId())
						.setParameter("bedDesc", "%"+bedMasterDto.getBedDesc().trim().toLowerCase()+"%")
						.setFirstResult(bedMasterDto.getOffset() != null ? bedMasterDto.getOffset() : 0)
						.setMaxResults(bedMasterDto.getNoOfRecordsPerPage() != null ? bedMasterDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
				return new Response(SUCCESS, null, null, movingBedMasterDtoList, null);
			}
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedByStatusId(BedMasterDto bedMasterDto) throws ApplicationException {
	try {
			
			List<BedMasterDto> areaMasterDtoList=null;
			//areaMasterDtoList =
				StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_BED_BY_BED_STATUS_ID").getQueryString());
				areaMasterDtoList=sessionFactory.getCurrentSession().createQuery(query.toString())
					.setParameter("bedId", bedMasterDto.getBedId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Boolean checkBedNumberExistOrNot(BedMasterDto bedMasterDto) throws ApplicationException {
		try {

		StringBuilder querySb=new StringBuilder("select count(*)  from adt.m_bed_master as mbm where lower(bed_number) like :bedNumber and unit_id =:unitId ");

			// BR-102-followup: room_id is a raw native-SQL parameter with no entity-mapping
			// type info available to Hibernate. Moving Bed rows genuinely have no room
			// (roomId is always null for them, by design -- confirmed live, the frontend
			// never sends roomId for a Moving Bed payload), and binding a null Object with
			// no type hint on a native query made the PostgreSQL JDBC driver fall back to
			// binding it as bytea, throwing "operator does not exist: integer = bytea" and
			// aborting the whole transaction -- silently, since this method's own catch
			// blocks swallow it, masking the real Update that ran immediately afterward in
			// the same (now-aborted) transaction. Null-guarded the same way this method
			// already null-guards bedId just below, preserving the check's real intent:
			// duplicate bed-number detection scoped to "same room" for Normal Beds, or
			// "the roomless Moving Bed pool" for Moving Beds -- not skipping the check
			// entirely, just scoping it correctly instead of crashing.
			if(bedMasterDto.getRoomId()!=null) {
				querySb.append(" and room_id=:roomId ");
			} else {
				querySb.append(" and room_id is null ");
			}

			if(bedMasterDto.getBedId()!=null ) {
				querySb.append(" and bed_id!="+bedMasterDto.getBedId());
			}

			Query query = sessionFactory.getCurrentSession().createSQLQuery(querySb.toString())
			.setParameter("bedNumber", bedMasterDto.getBedDesc().trim().toLowerCase())
			.setParameter("unitId", bedMasterDto.getUnitId());
			if(bedMasterDto.getRoomId()!=null) {
				query.setParameter("roomId", bedMasterDto.getRoomId());
			}
			BigInteger cnt = (BigInteger) query.uniqueResult();

		   if(cnt.intValue()>0) {
			   return true;
		   }
		} catch (HibernateException he) {

		} catch (Exception e) {

		}
		return false;
	}

	@Override
	public Response getPaymentEntitlmentbyAdmissionId(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			
			String query="select "
+"(case "
+"when tpsd.payment_entitlement_id = 2 then ( "
+"select insurance.insurance_desc from "
+"adt.t_insurance insurance where insurance.insurance_id = tpsd.insurance_id ) "
+"else ( "
+"select "
+"tcm.company_desc "
+"from "
+"public.t_company_master tcm "
+"where "
+"tcm.company_id = tpsd.payee_id ) "
+"end ) as \"associateCompanyDesc\" "
+"from opd.t_patient_sponsor_details tpsd where status = 'A' and admission_id =:admissionId order by priority_id asc";
			List<BedMasterDto> bedMstrDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", bedMasterDto.getadmissionId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, bedMstrDto, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Integer getConfiguredBedLimit(BedMasterDto bedMasterDto) {
		String sqlQuery=" select allocated_beds from t_unit_application_billing_configuration where unit_id=:unitId and organization_id=:orgId ";
		Integer data = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.setParameter("orgId", bedMasterDto.getOrganizationId())
				.setParameter("unitId", bedMasterDto.getUnitId()).uniqueResult();
		return data;
	}

	@Override
	public Integer getBedCount(BedMasterDto bedMasterDto) {
		String sqlQuery=" select cast(count(*) as int) from adt.m_bed_master where unit_id=:unitId and organization_id=:orgId and status='A' ";
		Integer data = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.setParameter("orgId", bedMasterDto.getOrganizationId())
				.setParameter("unitId", bedMasterDto.getUnitId()).uniqueResult();
		return data;
		
	}

	@Override
	public Response getIcuMonitorData(BedMasterDto bedMasterDto) throws ApplicationException {
		try {

			String sqlQuery=" select " +
					" hl7_result_id as \"resultId\", " +
					" CAST(admission_id AS INT) AS \"admissionId\", " +
					" parameter_name as \"parameterName\", " +
					" parameter_value as \"parameterValue\", " +
					" parameter_unit as \"paramUnit\", " +
					" to_char(result_date_time, 'MM/dd/yyyy HH24:MI:SS') as \"resultDateTime\", " +
					" machine_name as \"sendingMachineName\" " +
					" from t_hl7_message_result where " +
					" admission_id=:admissionId " +
					" and unit_id=:unitId " +
					" order by hl7_result_id desc " +
					" limit 10";


			List<BedMasterDto> bedMstrDtoList = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
					.setParameter("admissionId", bedMasterDto.getadmissionId().toString())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();

			for(BedMasterDto bedMasterDto1:bedMstrDtoList) {
				if(bedMasterDto1.getParameterName() != null && !bedMasterDto1.getParameterName().isEmpty()){
					String newParameterName = bedMasterDto1.getParameterName();
					newParameterName = newParameterName.replace("VITAL ", "");
					bedMasterDto1.setParameterName(newParameterName);
				}
			}

			return new Response(SUCCESS, null, null, bedMstrDtoList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getInterventionDetails(List<Integer> patientIds, Integer unitId, Integer organizationId) throws ApplicationException {
		try {
			StringBuilder sb = new StringBuilder(" select clinical_intervention_procedure_id as \"clinicalInterventionProcedureId\", "
					+   " patient_id as \"patientId\", "
					+   " intervention_processed_doctor_id as \"interventionProcessedDoctorId\" "
					+   " from public.t_clinical_intervention_procedure "
					+   " where "
					+   "   unit_id =:unitId"
					+   "   and organization_id =:orgId"
					+	"	and is_clinical_intervention_required = 'Y' "
					+	"	and status = 'A'");

			if(patientIds != null && !patientIds.isEmpty()){
				sb.append(" and patient_id in (:patientIds) ");
			}

			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("unitId", unitId)
					.setParameter("orgId", organizationId);

			if(patientIds != null && !patientIds.isEmpty()){
				sqlQuery.setParameterList("patientIds", patientIds);
			}

			List<BedMasterDto> resList = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, resList, null);
		} catch (Exception e) {

		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
