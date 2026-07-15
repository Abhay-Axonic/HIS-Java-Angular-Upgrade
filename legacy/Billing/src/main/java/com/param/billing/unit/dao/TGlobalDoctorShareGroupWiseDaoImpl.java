package com.param.billing.unit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.model.TGlobalDocShareGroupWise;
import com.param.billing.global.model.TIndividualDocShareServiceWise;
import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TGlobalDocShareGroupWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareGroupWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareServiceWiseDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.CommonDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TGlobalDoctorShareGroupWiseDaoImpl extends GenericDao<TGlobalDocShareGroupWise, Integer> implements IGlobalDoctorShareGroupWiseDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TGlobalDoctorShareGroupWiseDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	public TGlobalDoctorShareGroupWiseDaoImpl() {
		super(TGlobalDocShareGroupWise.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveGlobalDoctorShareGroupWiseDao(List<TGlobalDocShareGroupWiseDto> docShareGroupWiseDtosList)
			throws ApplicationException, SQLException {
			
		Connection connection = null;
		PreparedStatement statement = null;
		try{
				
				connection = driverManager.getConnection();
				connection.setAutoCommit(false);
		        int i = 0;
				 statement = connection.prepareStatement("INSERT INTO billing.t_global_doc_share_group_wise "
						+" (speciality_id, billing_bed_category_id, visit_type_id, patient_type_id, payment_entitlement_id, department_id, doc_share, status, created_by, created_date, updated_by, updated_date, org_id, org_unit_id, effective_date,  tariff_id) "
						+" VALUES(?, ?, ?, ?, ?, ?, ?, 'A', ?, now(), ?, now(), ?, ?, ?, ?);"); 
				
				for(TGlobalDocShareGroupWiseDto masterDto : docShareGroupWiseDtosList)
				{
			        	statement.setInt(1, masterDto.getSpecialityId());
			        	statement.setInt(2, masterDto.getBillingBedCategoryId());
			        	statement.setInt(3, masterDto.getVisitTypeId());
			        	statement.setInt(4, masterDto.getPatientTypeId());
			        	statement.setInt(5, masterDto.getPaymentEntitlementId());
			        	statement.setInt(6, masterDto.getDepartmentId());
			        	statement.setDouble(7, masterDto.getDocShare());
			        	statement.setInt(8,masterDto.getCreatedBy());
			        	statement.setInt(9,masterDto.getCreatedBy());
			        	statement.setInt(10,masterDto.getOrgId());
			        	statement.setInt(11,masterDto.getUnitId());
			        	statement.setDate(12, new java.sql.Date(masterDto.getEffectiveDate()));
			        	statement.setInt(13,masterDto.getTariffId());
			            statement.addBatch();
			            i++;

			            if (i % 100 == 0 || i == docShareGroupWiseDtosList.size()) {
			               int[] x = statement.executeBatch(); // Execute every 100 items.
			                connection.commit();  
			                //System.out.println(x);
			            }
					
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}catch(ConstraintViolationException e){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, null, null, null);
			}catch(Exception e){
				
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}finally{
			if (statement != null) {
				statement.close();
			}
				connection.setAutoCommit(true);
				connection.close();
			}
			
	}



	@Override
	public Response getDoctorGradeList(Integer orgId,Integer orgUnitId) throws ApplicationException
	{
		List<CommonDto>  listDoctorGrade  = null; 
		try
		{
			listDoctorGrade = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	docgrade.doctor_grade_id AS \"id\", "
							+"	docgrade.doc_grade_name AS \"name\" "
							+"FROM "
							+"	doctor.m_doctor_grade docgrade "
							+"WHERE "
							+"	docgrade.status = 'A' "
							+"	AND docgrade.org_id = :orgId "
							+"	AND docgrade.org_unit_id = :orgUnitId ")
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listDoctorGrade, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500, null, null);
		}
	}




	@Override
	public Response getGroupWiseDoctorShareByDepartmentId(DoctorShareReqDto doctorShareReqDto) {
		try {
			List<TGlobalDocShareGroupWiseDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GROUP_WISE_GLOBAL_DOCTOR_SHARE_BY_DEPARTMENT_ID_AND_GRADE_ID")
					.setParameter("departmentId", doctorShareReqDto.getDepartmentId())
					//.setInteger("gradeId", doctorShareReqDto.getGradeId())
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId",doctorShareReqDto.getUnitId())
	                .setResultTransformer(Transformers.aliasToBean(TGlobalDocShareGroupWiseDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}


	@Override
	public Response getGroupWiseDepartmentList(DoctorShareReqDto doctorShareReqDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder firstQuery=new StringBuilder();
			firstQuery.append("select bg.speciality_id AS \"specialityId\","
					+ " sm.speciality_name AS \"specialityName\" "
					//+ " bg.grade_id AS \"gradeId\","
					//+ " gm.doc_grade_name AS \"doctorGradeName\" "
					+ " from billing.t_global_doc_share_group_wise bg "
					+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bg.speciality_id "
					//+ " inner join doctor.m_doctor_grade gm on bg.grade_id=gm.doctor_grade_id "
					+ " where bg.status='A' and bg.org_id=:orgId and bg.org_unit_id=:orgUnitId ");
		    
			if(doctorShareReqDto.getDepartmentId()!=null) {
		    	 firstQuery.append(" AND bg.speciality_id =:departmentId ");
		     }
		     firstQuery.append(" union ");
		     firstQuery.append("select bs.speciality_id AS \"specialityId\","
				+ " sm.speciality_name AS \"specialityName\" "
				//+ "bs.grade_id AS \"gradeId\","
				//+ "gm.doc_grade_name AS \"doctorGradeName\" " 
				+ " from billing.t_global_doc_share_service_wise bs "
				+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bs.speciality_id "
				//+ " inner join doctor.m_doctor_grade gm on bs.grade_id=gm.doctor_grade_id "
				+ " where bs.status='A' and bs.org_id=:orgId and bs.org_unit_id=:orgUnitId ");
		     
		     if(doctorShareReqDto.getDepartmentId()!=null) {
		    	 firstQuery.append(" AND bs.speciality_id =:departmentId ");
		     }
		     
		     Query actualQuery = session.createSQLQuery(firstQuery.toString());
		     Query sqlQuery =actualQuery
		    		    .setParameter("orgId", doctorShareReqDto.getOrgId())
						.setParameter("orgUnitId", doctorShareReqDto.getUnitId());

			if(doctorShareReqDto.getDepartmentId()!=null)
				sqlQuery.setParameter("departmentId", doctorShareReqDto.getDepartmentId());

			List<TGlobalDocShareGroupWiseDto> list = sqlQuery
						.setFirstResult(doctorShareReqDto.getOffset() != null ? doctorShareReqDto.getOffset() : 0)
						.setMaxResults(doctorShareReqDto.getNoOfRecordsPerPage() != null ? doctorShareReqDto.getNoOfRecordsPerPage() : 10)
		                .setResultTransformer(Transformers.aliasToBean(TGlobalDocShareGroupWiseDto.class)).list();
		     
		     
		    Query sqlQueryCount =sessionFactory.getCurrentSession().createSQLQuery(firstQuery.toString())
		    		    .setParameter("orgId", doctorShareReqDto.getOrgId())
						.setParameter("orgUnitId", doctorShareReqDto.getUnitId());

			if(doctorShareReqDto.getDepartmentId()!=null)
				sqlQueryCount.setParameter("departmentId", doctorShareReqDto.getDepartmentId());

			List<TGlobalDocShareGroupWiseDto> listcount = sqlQueryCount
		                .setResultTransformer(Transformers.aliasToBean(TGlobalDocShareGroupWiseDto.class)).list();
		     
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, listcount.size());
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getGroupWiseDepartmentListSearch(DoctorShareReqDto doctorShareReqDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder firstQuery=new StringBuilder();
			firstQuery.append("select bg.speciality_id AS \"specialityId\","
					+ " sm.speciality_name AS \"specialityName\" "
					//+ " bg.grade_id AS \"gradeId\","
					//+ " gm.doc_grade_name AS \"doctorGradeName\" "
					+ " from billing.t_global_doc_share_group_wise bg "
					+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bg.speciality_id "
					//+ " inner join doctor.m_doctor_grade gm on bg.grade_id=gm.doctor_grade_id "
					+ " where bg.status='A' and bg.org_id=:orgId and bg.org_unit_id=:orgUnitId ");
		    
			if(doctorShareReqDto.getDepartmentId()!=null) {
		    	 firstQuery.append(" AND bg.speciality_id =:departmentId ");
		     }
			if(doctorShareReqDto.getSpecialitySerarchName()!=null && doctorShareReqDto.getSpecialitySerarchName()!="" ) {
		    	 firstQuery.append(" AND lower( sm.speciality_name )  like :specialitySearchName ");
		     }
		     firstQuery.append(" union ");
		     firstQuery.append("select bs.speciality_id AS \"specialityId\","
				+ " sm.speciality_name AS \"specialityName\" "
				//+ "bs.grade_id AS \"gradeId\","
				//+ "gm.doc_grade_name AS \"doctorGradeName\" " 
				+ " from billing.t_global_doc_share_service_wise bs "
				+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bs.speciality_id "
				//+ " inner join doctor.m_doctor_grade gm on bs.grade_id=gm.doctor_grade_id "
				+ " where bs.status='A' and bs.org_id=:orgId and bs.org_unit_id=:orgUnitId ");
		     
		     if(doctorShareReqDto.getDepartmentId()!=null) {
		    	 firstQuery.append(" AND bs.speciality_id =:departmentId ");
		     }
		     if(doctorShareReqDto.getSpecialitySerarchName()!=null && doctorShareReqDto.getSpecialitySerarchName()!="" ) {
		    	 firstQuery.append(" AND lower( sm.speciality_name )  like :specialitySearchName ");
		     }

		     Query sqlQuery = session.createSQLQuery(firstQuery.toString());

			if(doctorShareReqDto.getDepartmentId()!=null)
				sqlQuery.setParameter("departmentId", doctorShareReqDto.getDepartmentId());

			if(doctorShareReqDto.getSpecialitySerarchName()!=null && doctorShareReqDto.getSpecialitySerarchName()!="" )
				sqlQuery.setParameter("specialitySearchName", "%" + doctorShareReqDto.getSpecialitySerarchName().toLowerCase().trim() + "%");

		     List<TGlobalDocShareGroupWiseDto> list=sqlQuery
		    		    .setParameter("orgId", doctorShareReqDto.getOrgId())
						.setParameter("orgUnitId", doctorShareReqDto.getUnitId())
						.setFirstResult(doctorShareReqDto.getOffset() != null ? doctorShareReqDto.getOffset() : 0)
						.setMaxResults(doctorShareReqDto.getNoOfRecordsPerPage() != null ? doctorShareReqDto.getNoOfRecordsPerPage() : 10)
		                .setResultTransformer(Transformers.aliasToBean(TGlobalDocShareGroupWiseDto.class)).list();
		     
		     Query sqlQueryCount =sessionFactory.getCurrentSession().createSQLQuery(firstQuery.toString())
		    		    .setParameter("orgId", doctorShareReqDto.getOrgId())
						.setParameter("orgUnitId", doctorShareReqDto.getUnitId());

			if(doctorShareReqDto.getDepartmentId()!=null)
				sqlQueryCount.setParameter("departmentId", doctorShareReqDto.getDepartmentId());

			if(doctorShareReqDto.getSpecialitySerarchName()!=null && doctorShareReqDto.getSpecialitySerarchName()!="" )
				sqlQueryCount.setParameter("specialitySearchName", "%" + doctorShareReqDto.getSpecialitySerarchName().toLowerCase().trim() + "%");

			List<TGlobalDocShareGroupWiseDto> listcount = sqlQueryCount
		                .setResultTransformer(Transformers.aliasToBean(TGlobalDocShareGroupWiseDto.class)).list();
		     
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, listcount.size());
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response checkUniqueGlobalGroup(Set<Integer> deptIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> tariffIds, Set<Date> effectiveDates, Character inactive) {
		try{
			
			
			List<Integer> deptIdsL = new LinkedList<>();
			deptIdsL.addAll(deptIds);
			
			List<Integer> billingBedCategoryIdsL = new LinkedList<>();
			billingBedCategoryIdsL.addAll(billingBedCategoryIds);
			
			List<Integer> visitTypeIdsL = new LinkedList<>();
			visitTypeIdsL.addAll(visitTypeIds);
			
			List<Integer> patientTypeIdsL = new LinkedList<>();
			patientTypeIdsL.addAll(patientTypeIds);
			
			List<Integer> paymentEntitlementIdsL = new LinkedList<>();
			paymentEntitlementIdsL.addAll(paymentEntitlementIds);
			
			/*List<Integer> doctorIdsL = new LinkedList<>();
			doctorIdsL.addAll(doctorIds);*/

			
			List<Integer> tariffIdsL = new LinkedList<>();
			tariffIdsL.addAll(tariffIds);
			
			List<Date> effectiveDatesL = new LinkedList<>();
			effectiveDatesL.addAll(effectiveDates);
			
			String query = "select "
				+" global_group_wise_id as \"globalGroupWiseId\" "
				+" from "
				+" billing.t_global_doc_share_group_wise "
				+" where "
				+" department_id In (:departmentId) "
				+" and billing_bed_category_id in (:billingBedCategoryId) "
				+" and patient_type_id in (:patientTypeId) "
				+" and tariff_id in (:tariffIds) "
			    +" and payment_entitlement_id in (:paymentEntitlementIds) "
				+" and visit_type_id in (:visitTypeId) "
				+" and status = 'A' "
				+" and effective_date in (:effectiveDates) " ;
			
			List<TGlobalDocShareGroupWiseDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("billingBedCategoryId", billingBedCategoryIdsL)
					.setParameterList("patientTypeId", patientTypeIdsL)
					.setParameterList("paymentEntitlementIds", paymentEntitlementIdsL)
					.setParameterList("visitTypeId", visitTypeIdsL)
					.setParameterList("tariffIds", tariffIdsL)
					.setParameterList("effectiveDates", effectiveDatesL)
					.setParameterList("departmentId", deptIdsL)
					.setResultTransformer(Transformers.aliasToBean(TGlobalDocShareGroupWiseDto.class)).list();
			
			List<Integer> ids = new LinkedList<>(); 
			for(TGlobalDocShareGroupWiseDto dto : list)
			{
				ids.add(dto.getGlobalGroupWiseId());
			}
			if(ids.size()>0 && inactive.equals('Y')){
				sessionFactory.getCurrentSession().createSQLQuery("Update  billing.t_global_doc_share_group_wise set status='I' where global_group_wise_id in (:ids) ")
				.setParameterList("ids", ids).executeUpdate();
			}
			return new Response<>(SUCCESS, null, null, null, ids);
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, null, null, null, 0);
	}


	@Override
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentId(
			IndividualDoctorShareReqDto doctorShareReqDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			
			StringBuilder sqlQuery=new StringBuilder(" select " 
					 + " 	distinct(bg.referral_id) as \"referralId\", " 
					 + " 	bg.status as \"status\", " 
					 + " 	refType.referral_type_name as \"referralType\", " 
					 + " 	referralM.referral_name as \"referralName\" " 
					 + " from " 
					 + " 	billing.t_individual_doc_share_service_wise bg " 
					 + " inner join adt.m_referral_master referralM on " 
					 + " 	referralM.referral_id = bg.referral_id " 
					 + " inner join adt.m_referral_type_master refType on " 
					 + " 	refType.referral_type_id = referralM.referral_type_id " 
					 + " where " 
					 + " 	bg.org_id =:orgId " 
					 + " 	and bg.org_unit_id =:orgUnitId "
					 + "	and bg.status='A' or bg.status='I'");
			
			StringBuilder sqlQuery2=new StringBuilder(" select " 
					 + " 	distinct(bg.referral_id) as \"referralId\", " 
					 + " 	bg.status as \"status\", " 
					 + " 	refType.referral_type_name as \"referralType\", " 
					 + " 	referralM.referral_name as \"referralName\" " 
					 + " from " 
					 + " 	billing.t_individual_doc_share_group_wise bg " 
					 + " inner join adt.m_referral_master referralM on " 
					 + " 	referralM.referral_id = bg.referral_id " 
					 + " inner join adt.m_referral_type_master refType on " 
					 + " 	refType.referral_type_id = referralM.referral_type_id " 
					 + " where " 
					 + " 	bg.org_id =:orgId " 
					 + " 	and bg.org_unit_id =:orgUnitId "
					 + "	and bg.status='A' or bg.status='I'");
			
			if(doctorShareReqDto.getReferralId()!=null) {
				sqlQuery.append(" AND bg.referral_id =:referralId ");
				sqlQuery2.append(" AND bg.referral_id =:referralId ");
			}
			
			sqlQuery.append(" union ").append(sqlQuery2);
			
			Query actualQuery = session.createSQLQuery(sqlQuery.toString());

			if(doctorShareReqDto.getReferralId()!=null)
				actualQuery.setParameter("referralId", doctorShareReqDto.getReferralId());

			List<TIndividualDocShareGroupWiseDto> list=actualQuery
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId", doctorShareReqDto.getUnitId())
					.setFirstResult(doctorShareReqDto.getOffset() != null ? doctorShareReqDto.getOffset() : 0)
					.setMaxResults(doctorShareReqDto.getNoOfRecordsPerPage() != null ? doctorShareReqDto.getNoOfRecordsPerPage() : 10)
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();
			
			Query actualQueryCount =sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString())
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId", doctorShareReqDto.getUnitId());

			if(doctorShareReqDto.getReferralId()!=null)
				actualQueryCount.setParameter("referralId", doctorShareReqDto.getReferralId());

			List<TIndividualDocShareGroupWiseDto> listcount = actualQueryCount
					.setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, listcount.size());
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}
	
	@Override
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentIdSearch(
			IndividualDoctorShareReqDto doctorShareReqDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			
			StringBuilder sqlQuery=new StringBuilder(" select " 
					 + " 	distinct(bg.referral_id) as \"referralId\", " 
					 + " 	bg.status as \"status\", " 
					 + " 	refType.referral_type_name as \"referralType\", " 
					 + " 	referralM.referral_name as \"referralName\" " 
					 + " from " 
					 + " 	billing.t_individual_doc_share_service_wise bg " 
					 + " inner join adt.m_referral_master referralM on " 
					 + " 	referralM.referral_id = bg.referral_id " 
					 + " inner join adt.m_referral_type_master refType on " 
					 + " 	refType.referral_type_id = referralM.referral_type_id " 
					 + " where " 
					 + " 	bg.org_id =:orgId " 
					 + " 	and bg.org_unit_id =:orgUnitId "
					 + "	and bg.status='A' or bg.status='I'");
			
			StringBuilder sqlQuery2=new StringBuilder(" select " 
					 + " 	distinct(bg.referral_id) as \"referralId\", " 
					 + " 	bg.status as \"status\", " 
					 + " 	refType.referral_type_name as \"referralType\", " 
					 + " 	referralM.referral_name as \"referralName\" " 
					 + " from " 
					 + " 	billing.t_individual_doc_share_group_wise bg " 
					 + " inner join adt.m_referral_master referralM on " 
					 + " 	referralM.referral_id = bg.referral_id " 
					 + " inner join adt.m_referral_type_master refType on " 
					 + " 	refType.referral_type_id = referralM.referral_type_id " 
					 + " where " 
					 + " 	bg.org_id =:orgId " 
					 + " 	and bg.org_unit_id =:orgUnitId "
					 + "	and bg.status='A' or bg.status='I'");
			
			if(doctorShareReqDto.getReferralId()!=null) {
				sqlQuery.append(" AND bg.referral_id =:referralId ");
				sqlQuery2.append(" AND bg.referral_id =:referralId ");
			}
			
			if(doctorShareReqDto.getSpecialitySerarchName()!=null) {
				sqlQuery.append(" AND lower( referralM.referral_name ) like :specialitySearchName ");
				sqlQuery2.append(" AND lower( referralM.referral_name ) like :specialitySearchName ");
			}
			
			sqlQuery.append(" union ").append(sqlQuery2);
			
			Query actualQuery = session.createSQLQuery(sqlQuery.toString());

			if(doctorShareReqDto.getReferralId()!=null)
				actualQuery.setParameter("referralId", doctorShareReqDto.getReferralId());

			if(doctorShareReqDto.getSpecialitySerarchName()!=null && doctorShareReqDto.getSpecialitySerarchName()!="" )
				actualQuery.setParameter("specialitySearchName", "%" + doctorShareReqDto.getSpecialitySerarchName().toLowerCase().trim() + "%");

			List<TIndividualDocShareGroupWiseDto> list=actualQuery
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId", doctorShareReqDto.getUnitId())
					.setFirstResult(doctorShareReqDto.getOffset() != null ? doctorShareReqDto.getOffset() : 0)
					.setMaxResults(doctorShareReqDto.getNoOfRecordsPerPage() != null ? doctorShareReqDto.getNoOfRecordsPerPage() : 10)
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}


	@Override
	public Response getGroupWiseReferralShareByReferralId(IndividualDoctorShareReqDto individualDoctorShareReqDto) {
		try {
			List<TIndividualDocShareGroupWiseDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GROUP_WISE_DOCTOR_SHARE_BY_DEPARTMENT_ID_AND_REFERRAL_ID")
					.setParameter("referralId", individualDoctorShareReqDto.getReferralId())
					.setParameter("orgId", individualDoctorShareReqDto.getOrgId())
					.setParameter("orgUnitId",individualDoctorShareReqDto.getUnitId())
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}


	@Override
	public Response getServiceWiseReferralShareByReferralId(IndividualDoctorShareReqDto individualDoctorShareReqDto) {
		try {
			List<TIndividualDocShareServiceWiseDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_WISE_DOCTOR_SHARE_BY_REFERRAL_ID")
					.setParameter("referralId", individualDoctorShareReqDto.getReferralId())
					.setParameter("orgId", individualDoctorShareReqDto.getOrgId())
					.setParameter("orgUnitId",individualDoctorShareReqDto.getUnitId())
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareServiceWiseDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}

}
