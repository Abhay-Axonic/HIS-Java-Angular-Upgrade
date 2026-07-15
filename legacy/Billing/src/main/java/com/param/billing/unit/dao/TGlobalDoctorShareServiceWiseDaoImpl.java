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
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.model.TGlobalDocShareServiceWise;
import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.dto.TGlobalDocShareServiceWiseDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TGlobalDoctorShareServiceWiseDaoImpl extends GenericDao<TGlobalDocShareServiceWise, Integer> implements IGlobalDoctorShareServiceWiseDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TGlobalDoctorShareServiceWiseDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	public TGlobalDoctorShareServiceWiseDaoImpl() {
		super(TGlobalDocShareServiceWise.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveGlobalDoctorShareServiceWiseDao(List<TGlobalDocShareServiceWiseDto> docShareServiceWiseDtosList)
			throws ApplicationException, SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try{
				
				connection = driverManager.getConnection();
				connection.setAutoCommit(false);
		        int i = 0;
				 statement = connection.prepareStatement("INSERT INTO billing.t_global_doc_share_service_wise "
						+" (speciality_id, billing_bed_category_id, visit_type_id, patient_type_id, payment_entitlement_id, service_id, doc_share, status, created_by, created_date, updated_by, updated_date, org_id, org_unit_id, effective_date, tariff_id, amount) "
						+" VALUES(?, ?, ?, ?, ?, ?, ?,'A', ?, now(), ?, now(), ?, ?, ?, ?, ?);"); 
				
				for(TGlobalDocShareServiceWiseDto masterDto : docShareServiceWiseDtosList)
				{
			        	statement.setInt(1, masterDto.getSpecialityId());
			        	statement.setInt(2, masterDto.getBillingBedCategoryId());
			        	statement.setInt(3, masterDto.getVisitTypeId());
			        	statement.setInt(4, masterDto.getPatientTypeId());
			        	statement.setInt(5, masterDto.getPaymentEntitlementId());
			        	statement.setInt(6, masterDto.getServiceId());
			        	statement.setDouble(7, masterDto.getDocShare());
			        	statement.setInt(8,masterDto.getCreatedBy());
			        	statement.setInt(9,masterDto.getCreatedBy());
			        	statement.setInt(10,masterDto.getOrgId());
			        	statement.setInt(11,masterDto.getUnitId());
			        	statement.setDate(12, new java.sql.Date(masterDto.getEffectiveDate()));
			        	statement.setInt(13,masterDto.getTariffId());
			        	statement.setDouble(14,masterDto.getAmount());
			            statement.addBatch();
			            i++;

			            if (i % 100 == 0 || i == docShareServiceWiseDtosList.size()) {
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
	public Response getServiceDepartmentList(Integer orgId, Integer orgUnitId) {
		try{
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getServiceWiseDoctorShareByDepartmentId(DoctorShareReqDto doctorShareReqDto) {
		try {
			List<TGlobalDocShareServiceWiseDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_WISE_GLOBAL_DOCTOR_SHARE_BY_DEPARTMENT_ID_AND_GRADE_ID")
					.setParameter("departmentId", doctorShareReqDto.getDepartmentId())
					.setParameter("orgId", doctorShareReqDto.getOrgId())
					.setParameter("orgUnitId",doctorShareReqDto.getUnitId())
	                .setResultTransformer(Transformers.aliasToBean(TGlobalDocShareServiceWiseDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		 }catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		  }
	}


	@Override
	public void checkUniqueGlobalService(Set<Integer> serviceIds, Set<Integer> billingBedCategoryIds,
			Set<Integer> visitTypeIds, Set<Integer> patientTypeIds, Set<Integer> paymentEntitlementIds,
			Set<Integer> tariffIds, Set<Date> effectiveDates,Character inactive) {
		try{

			List<Integer> serviceIdsL = new LinkedList<>();
			serviceIdsL.addAll(serviceIds);
			
			List<Integer> billingBedCategoryIdsL = new LinkedList<>();
			billingBedCategoryIdsL.addAll(billingBedCategoryIds);
			
			List<Integer> visitTypeIdsL = new LinkedList<>();
			visitTypeIdsL.addAll(visitTypeIds);
			
			List<Integer> patientTypeIdsL = new LinkedList<>();
			patientTypeIdsL.addAll(patientTypeIds);
			
			List<Integer> paymentEntitlementIdsL = new LinkedList<>();
			paymentEntitlementIdsL.addAll(paymentEntitlementIds);
			
			List<Integer> tariffIdsL = new LinkedList<>();
			tariffIdsL.addAll(tariffIds);
			
			List<Date> effectiveDatesL = new LinkedList<>();
			effectiveDatesL.addAll(effectiveDates);
			
			String query = "select "
				+" global_share_wise_id as \"globalShareWise_id\" "
				+" from "
				+" billing.t_global_doc_share_service_wise  "
				+" where "
				+" service_id in (:serviceId) "
				+" and billing_bed_category_id in (:billingBedCategoryId) "
				+" and patient_type_id in (:patientTypeId) "
				+" and tariff_id in (:tariffIds) "
			    +" and payment_entitlement_id in (:paymentEntitlementIds) "
				+" and visit_type_id in (:visitTypeId) "
				+" and status = 'A' "
				+" and effective_date in (:effectiveDates) " ;
			
			List<TGlobalDocShareServiceWiseDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("billingBedCategoryId", billingBedCategoryIdsL)
					.setParameterList("patientTypeId", patientTypeIdsL)
					.setParameterList("paymentEntitlementIds", paymentEntitlementIdsL)
					.setParameterList("visitTypeId", visitTypeIdsL)
					.setParameterList("tariffIds", tariffIdsL)
					.setParameterList("effectiveDates", effectiveDatesL)
					.setParameterList("serviceId", serviceIdsL)
					.setResultTransformer(Transformers.aliasToBean(TGlobalDocShareServiceWiseDto.class)).list();
			
			List<Integer> ids = new LinkedList<>(); 
			for(TGlobalDocShareServiceWiseDto dto : list)
			{
				ids.add(dto.getGlobalShareWise_id());
			}
			if(ids.size()>0 && inactive.equals('Y')){
				sessionFactory.getCurrentSession().createSQLQuery("Update  billing.t_global_doc_share_service_wise set status='I' where global_share_wise_id in (:ids) ")
				.setParameterList("ids", ids).executeUpdate();
			}
		
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
		
	}


	@Override
	public Response<DoctorShareReqDto, Integer> updateStatusByReferralId(
			IndividualDoctorShareReqDto doctorShareReqDto1) {

        Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_individual_doc_share_group_wise set status =:status "
        		+ " where  referral_id =:referralId ")
				.setParameter("referralId", doctorShareReqDto1.getReferralId())
				.setParameter("status", doctorShareReqDto1.getStatus())
                .executeUpdate();
        
        Integer count1=sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_individual_doc_share_service_wise set status =:status "
        		+ " where  referral_id =:referralId ")
				.setParameter("referralId", doctorShareReqDto1.getReferralId())
				.setParameter("status", doctorShareReqDto1.getStatus())
                .executeUpdate();
        
		return new Response("success", "200", null, null,null);
	}





	
}
