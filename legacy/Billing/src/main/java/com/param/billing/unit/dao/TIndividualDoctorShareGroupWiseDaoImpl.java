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
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.model.TIndividualDocShareGroupWise;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TGlobalDocShareGroupWiseDto;
import com.param.billing.unit.dto.TIndividualDocShareGroupWiseDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TIndividualDoctorShareGroupWiseDaoImpl extends GenericDao<TIndividualDocShareGroupWise, Integer> implements IIndividualDoctorShareGroupWiseDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TIndividualDoctorShareGroupWiseDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	public TIndividualDoctorShareGroupWiseDaoImpl() {
		super(TIndividualDocShareGroupWise.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveIndividualDoctorShareGroupWise(List<TIndividualDocShareGroupWiseDto> individualDocShareGroupWiseDtosList)
			throws ApplicationException, SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try{
			
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
	        int i = 0;
			 statement = connection.prepareStatement("INSERT INTO billing.t_individual_doc_share_group_wise "
					+"(speciality_id, billing_bed_category_id, visit_type_id, patient_type_id, payment_entitlement_id, department_id, doc_share, created_by, created_date, updated_by, updated_date, org_id, org_unit_id, effective_date, doctor_id, tariff_id)"
					+" VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?, ?, ?, ?, ?);"); 
			
			for(TIndividualDocShareGroupWiseDto masterDto : individualDocShareGroupWiseDtosList)
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
		        	statement.setInt(13,masterDto.getDoctorId());
		        	statement.setInt(14,masterDto.getTariffId());
		            statement.addBatch();
		            i++;

		            if (i % 100 == 0 || i == individualDocShareGroupWiseDtosList.size()) {
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
	public Response getGroupWiseDoctorShareByDepartmentIdAndDoctorId(
			IndividualDoctorShareReqDto individualDoctorShareReqDto) {
		try {
			List<TIndividualDocShareGroupWiseDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GROUP_WISE_DOCTOR_SHARE_BY_DEPARTMENT_ID_AND_DOCTOR_ID")
					.setParameter("specialityId", individualDoctorShareReqDto.getSpecialityId())
					.setParameter("doctorId", individualDoctorShareReqDto.getDoctorId())
					.setParameter("orgId", individualDoctorShareReqDto.getOrgId())
					.setParameter("orgUnitId",individualDoctorShareReqDto.getUnitId())
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}


	@Override
	public Response getDoctorShareDepartmentMasterList(IndividualDoctorShareReqDto doctorShareReqDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			
			StringBuilder sqlQuery=new StringBuilder();
			sqlQuery.append("select bg.speciality_id AS \"specialityId\","
					+ " sm.speciality_name AS \"specialityName\",concat(dm.first_name,' ',dm.last_name) as \"doctorName\","
					+ " bg.doctor_id as \"doctorId\" "
					+ " from billing.t_individual_doc_share_group_wise bg "
					+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bg.speciality_id "
					+ " inner join doctor.m_doctor_master dm on dm.doctor_id=bg.doctor_id "
					+ " where bg.org_id=:orgId and bg.org_unit_id=:orgUnitId ");
			
			if(doctorShareReqDto.getSpecialityId()!=null) {
				sqlQuery.append(" AND bg.speciality_id =:specialityId ");
			}
			
			if(doctorShareReqDto.getDoctorId()!=null) {
				sqlQuery.append(" AND bg.doctor_id =:doctorId ");
			}

			sqlQuery.append(" union ");
			sqlQuery.append(" select bs.speciality_id AS \"specialityId\","
					+ " sm.speciality_name AS \"specialityName\",concat(dm.first_name,' ',dm.last_name) as \"doctorName\","
					+ " bs.doctor_id as \"doctorId\" " 
					+ " from billing.t_individual_doc_share_service_wise bs "
					+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bs.speciality_id "
					+ " inner join doctor.m_doctor_master dm on dm.doctor_id=bs.doctor_id "
					+ " where bs.org_id=:orgId and bs.org_unit_id=:orgUnitId ");
			
			if(doctorShareReqDto.getSpecialityId()!=null) {
				sqlQuery.append(" AND bs.speciality_id =:specialityId ");
			}
			
			if(doctorShareReqDto.getDoctorId()!=null) {
				sqlQuery.append(" AND bs.doctor_id =:doctorId ");
			}
			
			Query actualQuery = session.createSQLQuery(sqlQuery.toString());

			if(doctorShareReqDto.getSpecialityId()!=null)
				actualQuery.setParameter("specialityId", doctorShareReqDto.getSpecialityId());

			if(doctorShareReqDto.getDoctorId()!=null)
				actualQuery.setParameter("doctorId", doctorShareReqDto.getDoctorId());

			List<TIndividualDocShareGroupWiseDto> list=actualQuery
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId", doctorShareReqDto.getUnitId())
					.setFirstResult(doctorShareReqDto.getOffset() != null ? doctorShareReqDto.getOffset() : 0)
					.setMaxResults(doctorShareReqDto.getNoOfRecordsPerPage() != null ? doctorShareReqDto.getNoOfRecordsPerPage() : 10)
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();


			Query actualQueryCount =sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString())
	    		    .setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId", doctorShareReqDto.getUnitId());

			if(doctorShareReqDto.getSpecialityId()!=null)
				actualQueryCount.setParameter("specialityId", doctorShareReqDto.getSpecialityId());

			if(doctorShareReqDto.getDoctorId()!=null)
				actualQueryCount.setParameter("doctorId", doctorShareReqDto.getDoctorId());

			List<TIndividualDocShareGroupWiseDto> listcount = actualQueryCount
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();
			
					
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, listcount.size());
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}
	
	
	@Override
	public Response getDoctorShareDepartmentMasterListSearch(IndividualDoctorShareReqDto doctorShareReqDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			
			StringBuilder sqlQuery=new StringBuilder();
			sqlQuery.append("select bg.speciality_id AS \"specialityId\","
					+ " sm.speciality_name AS \"specialityName\",concat(dm.first_name,' ',dm.last_name) as \"doctorName\","
					+ " bg.doctor_id as \"doctorId\" "
					+ " from billing.t_individual_doc_share_group_wise bg "
					+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bg.speciality_id "
					+ " inner join doctor.m_doctor_master dm on dm.doctor_id=bg.doctor_id "
					+ " where bg.org_id=:orgId and bg.org_unit_id=:orgUnitId ");
			
			if(doctorShareReqDto.getSpecialityId()!=null) {
				sqlQuery.append(" AND bg.speciality_id =:specialityId ");
			}
			
			if(doctorShareReqDto.getDoctorId()!=null) {
				sqlQuery.append(" AND bg.doctor_id =:doctorId ");
			}
			if(doctorShareReqDto.getSpecialitySerarchName()!=null) {
				sqlQuery.append(" AND  ( lower (sm.speciality_name ) like :specialitySearchName OR lower (concat(dm.first_name,dm.last_name)) like :specialitySearchName  ) ");
			}
			sqlQuery.append(" union ");
			sqlQuery.append(" select bs.speciality_id AS \"specialityId\","
					+ " sm.speciality_name AS \"specialityName\",concat(dm.first_name,' ',dm.last_name) as \"doctorName\","
					+ " bs.doctor_id as \"doctorId\" " 
					+ " from billing.t_individual_doc_share_service_wise bs "
					+ " inner join doctor.m_speciality_master sm on sm.speciality_id=bs.speciality_id "
					+ " inner join doctor.m_doctor_master dm on dm.doctor_id=bs.doctor_id "
					+ " where bs.org_id=:orgId and bs.org_unit_id=:orgUnitId ");
			
			if(doctorShareReqDto.getSpecialityId()!=null) {
				sqlQuery.append(" AND bs.speciality_id =:specialityId ");
			}
			if(doctorShareReqDto.getDoctorId()!=null) {
				sqlQuery.append(" AND bs.doctor_id =:doctorId ");
			}
			if(doctorShareReqDto.getSpecialitySerarchName()!=null) {
				sqlQuery.append(" AND  ( lower (sm.speciality_name ) like :specialitySearchName OR lower (concat(dm.first_name,dm.last_name)) like :specialitySearchName  ) ");
			}

			Query actualQuery = session.createSQLQuery(sqlQuery.toString());

			if(doctorShareReqDto.getSpecialityId()!=null)
				actualQuery.setParameter("specialityId", doctorShareReqDto.getSpecialityId());

			if(doctorShareReqDto.getDoctorId()!=null)
				actualQuery.setParameter("doctorId", doctorShareReqDto.getDoctorId());

			if(doctorShareReqDto.getSpecialitySerarchName()!=null)
				actualQuery.setParameter("specialitySearchName", "%"+doctorShareReqDto.getSpecialitySerarchName().trim().toLowerCase()+"%");

			List<TIndividualDocShareGroupWiseDto> list=actualQuery
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId", doctorShareReqDto.getUnitId())
					.setFirstResult(doctorShareReqDto.getOffset() != null ? doctorShareReqDto.getOffset() : 0)
					.setMaxResults(doctorShareReqDto.getNoOfRecordsPerPage() != null ? doctorShareReqDto.getNoOfRecordsPerPage() : 10)
	                .setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}


	@Override
	public List<TIndividualDocShareGroupWiseDto> checkUniqueIndividualGroup(Set<Integer> deptIds, Set<Integer> billingBedCategoryIds, Set<Integer> visitTypeIds,
			Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds, Set<Integer> doctorIds,
			Set<Integer> tariffIds, Set<Date> effectiveDates) {
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

			List<Integer> docIds = new LinkedList<>();
			docIds.addAll(doctorIds);
			
			List<Integer> tariffIdsL = new LinkedList<>();
			tariffIdsL.addAll(tariffIds);
			
			List<Date> effectiveDatesL = new LinkedList<>();
			effectiveDatesL.addAll(effectiveDates);
			
			String query = "select "
				+" individual_group_wise_id as \"individualGroupWiseId\" "
				+" from "
				+" billing.t_individual_doc_share_group_wise "
				+" where "
				+" doctor_id in (:doctorId) "
				+" and department_id In (:departmentId) "
				//+" and service_id in (:serviceId) "
				+" and billing_bed_category_id in (:billingBedCategoryId) "
				+" and patient_type_id in (:patientTypeId) "
				+" and tariff_id in (:tariffIds) "
			    +" and payment_entitlement_id in (:paymentEntitlementIds) "
				+" and visit_type_id in (:visitTypeId) "
				+" and status = 'A' "
				+" and effective_date in (:effectiveDates) " ;
			
			List<TIndividualDocShareGroupWiseDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("doctorId", docIds)
					.setParameterList("billingBedCategoryId", billingBedCategoryIdsL)
					.setParameterList("patientTypeId", patientTypeIdsL)
					.setParameterList("paymentEntitlementIds", paymentEntitlementIdsL)
					.setParameterList("visitTypeId", visitTypeIdsL)
					.setParameterList("tariffIds", tariffIdsL)
					.setParameterList("effectiveDates", effectiveDatesL)
					.setParameterList("departmentId", deptIdsL)
					.setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();
			
			List<Integer> ids = new LinkedList<>(); 
			for(TIndividualDocShareGroupWiseDto dto : list)
			{
				ids.add(dto.getIndividualGroupWiseId());
			}
			if(ids.size()>0){
				sessionFactory.getCurrentSession().createSQLQuery("Update  billing.t_individual_doc_share_group_wise set status='I' where individual_group_wise_id in (:ids) ")
				.setParameterList("ids", ids).executeUpdate();
			}
			
			return list;
			
		}catch(Exception e){
			
		}
		return null;
	}


	@Override
	public List<TIndividualDocShareGroupWiseDto> checkUniqueIndividualGroupForReferral(Set<Integer> deptIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> doctorIds, Set<Integer> tariffIds, Set<Date> effectiveDates) {
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

			List<Integer> docIds = new LinkedList<>();
			docIds.addAll(doctorIds);
			
			List<Integer> tariffIdsL = new LinkedList<>();
			tariffIdsL.addAll(tariffIds);
			
			List<Date> effectiveDatesL = new LinkedList<>();
			effectiveDatesL.addAll(effectiveDates);
			
			String query = "select "
				+" individual_group_wise_id as \"individualGroupWiseId\" "
				+" from "
				+" billing.t_individual_doc_share_group_wise "
				+" where "
				+" referral_id in (:doctorId) "
				+" and department_id In (:departmentId) "
				//+" and service_id in (:serviceId) "
				+" and billing_bed_category_id in (:billingBedCategoryId) "
				+" and patient_type_id in (:patientTypeId) "
				+" and tariff_id in (:tariffIds) "
			    +" and payment_entitlement_id in (:paymentEntitlementIds) "
				+" and visit_type_id in (:visitTypeId) "
				+" and status = 'A' "
				+" and effective_date in (:effectiveDates) " ;
			
			List<TIndividualDocShareGroupWiseDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("doctorId", docIds)
					.setParameterList("billingBedCategoryId", billingBedCategoryIdsL)
					.setParameterList("patientTypeId", patientTypeIdsL)
					.setParameterList("paymentEntitlementIds", paymentEntitlementIdsL)
					.setParameterList("visitTypeId", visitTypeIdsL)
					.setParameterList("tariffIds", tariffIdsL)
					.setParameterList("effectiveDates", effectiveDatesL)
					.setParameterList("departmentId", deptIdsL)
					.setResultTransformer(Transformers.aliasToBean(TIndividualDocShareGroupWiseDto.class)).list();
			
			List<Integer> ids = new LinkedList<>(); 
			for(TIndividualDocShareGroupWiseDto dto : list)
			{
				ids.add(dto.getIndividualGroupWiseId());
			}
			if(ids.size()>0){
				sessionFactory.getCurrentSession().createSQLQuery("Update  billing.t_individual_doc_share_group_wise set status='I' where individual_group_wise_id in (:ids) ")
				.setParameterList("ids", ids).executeUpdate();
			}
			
			return list;
			
		}catch(Exception e){
			
		}
		return null;
		
	}


	@Override
	public Response saveIndividualDoctorShareGroupWiseForReferral(List<TIndividualDocShareGroupWiseDto> docShareGroupWiseDtosList) throws SQLException 
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try{
			
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
	        int i = 0;
			 statement = connection.prepareStatement("INSERT INTO billing.t_individual_doc_share_group_wise "
					+"(referral_id, billing_bed_category_id, visit_type_id, patient_type_id, payment_entitlement_id, department_id, doc_share, created_by, created_date, updated_by, updated_date, org_id, org_unit_id, effective_date,tariff_id)"
					+" VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?, ?, ?, ?);"); 
			
			for(TIndividualDocShareGroupWiseDto masterDto : docShareGroupWiseDtosList)
			{
		        	statement.setInt(1, masterDto.getDoctorId());
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


	
}
