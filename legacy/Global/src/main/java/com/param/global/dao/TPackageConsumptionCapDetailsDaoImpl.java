package com.param.global.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.packages.model.TPackageConsumptionCapDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TPackageConsumptionCapDetailsDaoImpl extends GenericDao<TPackageConsumptionCapDetails, Integer> implements ITPackageConsumptionCapDetailsDao, ICommonConstants{

	public TPackageConsumptionCapDetailsDaoImpl() {
		super(TPackageConsumptionCapDetails.class);
	}

	@Override
	public Response saveTPackageConsumptionCapDetails(TPackageConsumptionCapDetailsDto dto) throws BillingException {
		try{
			TPackageConsumptionCapDetails details = new TPackageConsumptionCapDetails();
			details.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
			details.setDepartmentId(dto.getDepartmentId());
			details.setSubDepartmentId(dto.getSubDepartmentId());
			details.setIsServiceItem(dto.getIsServiceItem());
			details.setProductCategoryId(dto.getProductCategoryId());
			details.setCapAmount(dto.getCapAmount());
			details.setAvailableCap(dto.getCapAmount());
			details.setOrganisationId(dto.getOrganisationId());
			details.setUnitId(dto.getUnitId());
			details.setCreatedBy(dto.getCreatedBy());
			details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setUpdatedBy(dto.getUpdatedBy());
			details.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setStatus(dto.getStatus());
			details.setQuantity(dto.getQuantity());
			details.setPackageQty(dto.getPackageQty());
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateTPackageConsumptCapAmtByDeptSubDept(Integer packageId, Integer patientId,Integer encounterId, Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount, Integer admissionId, Integer visitTypeId) throws BillingException {
		try{
			if(subDepartmentId!=null){
				Integer updateCount = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_SUBDEPT_CAP_BALANCE")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
						.setParameter("departmentId", departmentId)
						.setParameter("subDepartmentId", subDepartmentId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("serviceAmount", serviceAmount)
						.executeUpdate();
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
			}else{
				Integer updateCount = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_DEPT_CAP_BALANCE")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
						.setParameter("departmentId", departmentId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("serviceAmount", serviceAmount)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
			}
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getTPackageConsumptionCapDtlsByPackageEncId(int packageId, Integer encounterId, Integer admissionId, Integer visitTypeId)throws BillingException {
		try{
			List<TPackageConsumptionCapDetailsDto> listTPackageConsumptionCapDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_CONS_CAP_DTLS_BY_PACKAGE_ENCOUNTER_ID")
											.setParameter("packageId", packageId).setParameter("encounterId", encounterId != null ? encounterId : admissionId)
											.setParameter("visitTypeId", visitTypeId)
											.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionCapDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageConsumptionCapDetailsDto, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	public Response updatePackageCap(Integer packageCapDetailsId, Double capAmount) throws ServiceException 
	{
		try{
			
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
		 session.createSQLQuery(
					" update "+ 
					"	billing.t_package_consumption_cap_details set available_cap = case when cap_amount is not null and (coalesce(available_cap, 0)+:capAmount) > cap_amount then cap_amount else coalesce(available_cap, 0)+:capAmount end  " + 
					" where " +
					"	package_consumption_cap_details_id = :packageCapDetailsId")
					.setParameter("packageCapDetailsId",packageCapDetailsId)
					.setDouble("capAmount", Math.round(GlobalCommonDateUtils.getAmountCap(capAmount) * 100.0)/100.0).executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePackageServiceCap(Integer pacakgeConsumptionServiceDetailId, Double capAmount)
			throws ServiceException {
		try{
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createSQLQuery("update "  
					+"	billing.t_package_consumption_service_detail set available_cap = (case when service_cap is not null and ((coalesce(available_cap, 0))+:capAmount)>service_cap then service_cap else (coalesce(available_cap, 0))+:capAmount end)  " 
					+ " where pacakge_consumption_service_detail_id = :pacakgeConsumptionServiceDetailId")
					.setParameter("pacakgeConsumptionServiceDetailId",pacakgeConsumptionServiceDetailId)
					.setDouble("capAmount", Math.round(GlobalCommonDateUtils.getAmountCap(capAmount) * 100.0)/100.0).executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePackageQty(Integer pacakgeConsumptionServiceDetailId, Integer qty) throws ServiceException {
		try{
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createSQLQuery("update " + 
					"	billing.t_package_consumption_service_detail set balance_package_quantity = case when package_quantity is not null and ((coalesce(balance_package_quantity, 0))+:qty) > package_quantity then package_quantity else ((coalesce(balance_package_quantity, 0))+:qty) end  " 
					+ " where pacakge_consumption_service_detail_id = :pacakgeConsumptionServiceDetailId")
					.setParameter("pacakgeConsumptionServiceDetailId",pacakgeConsumptionServiceDetailId)
					.setParameter("qty", qty).executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateEitherOrQuantity(Integer packageConsumptionEODetailId, Integer qty,boolean isRecalculation) throws ServiceException {
		try{
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			
			session.createSQLQuery("update " + 
					"	billing.t_package_consumption_eo_details set balance_quantity = case "
					+ (isRecalculation ? " when total_quantity is not null and (coalesce(balance_quantity, 0))=total_quantity then total_quantity ":"")
					+ "		when total_quantity is not null and ((coalesce(balance_quantity, 0))+:qty) > total_quantity then total_quantity "
					+ "		else ((coalesce(balance_quantity, 0))+:qty) end " + 
					" where pacakge_consumtion_eo_detail_id  =:packageConsumptionEODetailId")
					.setParameter("packageConsumptionEODetailId",packageConsumptionEODetailId)
					.setParameter("qty", qty).executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response updatePackageCapItem(Integer pacakgeConsumptionServiceDetailId, Double capAmount,boolean isRecalculation)
			throws ServiceException 
	{
		double amount = GlobalCommonDateUtils.getAmountCap(capAmount);
		try{
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createSQLQuery("update "  
					+"	billing.t_package_consumption_service_detail "
					+"  set available_cap = (case "
					+ (isRecalculation ?"		when service_cap is not null and (coalesce(available_cap, 0))=service_cap then service_cap " : "")
					+"		when service_cap is not null "
					+"			and (coalesce(available_cap, 0))+:capAmount > service_cap then service_cap "
					+"		else (coalesce(available_cap, 0))+:capAmount "
					+"	end) " 
					+ " where pacakge_consumption_service_detail_id = :pacakgeConsumptionServiceDetailId")
					.setParameter("pacakgeConsumptionServiceDetailId",pacakgeConsumptionServiceDetailId)
					.setDouble("capAmount", amount).executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePackageQuantityCapItem(Integer pacakgeConsumptionServiceDetailId, Integer qty, Double capAmount,boolean isRecalculation)
			throws ServiceException {
		try{
			double amount = GlobalCommonDateUtils.getAmountCap(capAmount);
			
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createSQLQuery(" update " 
					 + " 	billing.t_package_consumption_service_detail " 
					 + " set " 
					 + " 	balance_package_quantity = " 
					 + " 	(case " 
					 + (isRecalculation ? " when package_quantity is not null and (coalesce(balance_package_quantity, 0))=package_quantity then package_quantity " : "")
					 + "		when package_quantity is not null and (coalesce(balance_package_quantity, 0))+:qty > package_quantity then package_quantity " 
					 + " 		else (coalesce(balance_package_quantity, 0))+:qty " 
					 + " 	end) , " 
					 + " 	available_cap = " 
					 + " 	(case " 
					 + (isRecalculation ? "	when service_cap is not null and (coalesce(available_cap, 0))=service_cap then service_cap ":"")
					 + "		when service_cap is not null and (coalesce(available_cap, 0))+:capAmount > service_cap then service_cap " 
					 + " 		else (coalesce(available_cap, 0))+:capAmount " 
					 + " 	end) " 
					 + " where " 
					 + " 	pacakge_consumption_service_detail_id = :pacakgeConsumptionServiceDetailId ")
					.setParameter("pacakgeConsumptionServiceDetailId",pacakgeConsumptionServiceDetailId)
					.setParameter("qty", qty)
					.setDouble("capAmount", amount)
					.executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePackageCapProductCategory(Integer packageCapDetailsId, Double capAmount,boolean isRecalculation, boolean pharmacyReturnServiceCal)
			throws ServiceException {
   try{
		double amount = GlobalCommonDateUtils.getAmountCap(capAmount);	
		
		if(pharmacyReturnServiceCal == true) {
//			 Session session = sessionFactory.openSession();
//				Transaction t = session.beginTransaction();
				sessionFactory.getCurrentSession().createSQLQuery(
							  " update " 
							+ "	billing.t_package_consumption_cap_details " 
							+ "set " 
							+ "	available_cap = (COALESCE(available_cap, 0) + :capAmount )" 
							+ "where " 
							+ "	package_consumption_cap_details_id = :packageCapDetailsId ")
							.setParameter("packageCapDetailsId",packageCapDetailsId)
							.setDouble("capAmount", amount).executeUpdate();
//					t.commit();
//					session.close();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}else {
			
//			 Session session = sessionFactory.openSession();
//				Transaction t = session.beginTransaction();
				sessionFactory.getCurrentSession().createSQLQuery(
							  " update " 
							+ "	billing.t_package_consumption_cap_details " 
							+ "set " 
							+ "	available_cap = " 
							+ "	(case " 
							+   (isRecalculation ? "	when (coalesce(available_cap, 0))=cap_amount then cap_amount " : "")
							+ "		when (coalesce(available_cap, 0))+:capAmount > cap_amount then cap_amount " 
							+ "		else (COALESCE(available_cap, 0) + :capAmount) " 
							+ "	end) " 
							+ "where " 
							+ "	package_consumption_cap_details_id = :packageCapDetailsId ")
							.setParameter("packageCapDetailsId",packageCapDetailsId)
							.setDouble("capAmount", amount).executeUpdate();
//					t.commit();
//					session.close();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}
	   
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePackageQuantityCapProductCategory(Integer packageCapDetailsId, Integer qty, Double capAmount, boolean isRecalculation, boolean pharmacyReturnServiceCal)
			throws ServiceException {
		try{
			double amount = GlobalCommonDateUtils.getAmountCap(capAmount);	
			
			if(pharmacyReturnServiceCal == true) {
				Session session = sessionFactory.openSession();
				Transaction t = session.beginTransaction();
			    session.createSQLQuery(" update " 
			    		 + " 	billing.t_package_consumption_cap_details " 
			    		 + " set " 
			    		 + " 	available_cap = (coalesce(available_cap, 0))+:capAmount , " 
			    		 + " 	quantity = (coalesce(quantity, 0))+:qty  " 
			    		 + " where " 
			    		 + " 	package_consumption_cap_details_id = :packageCapDetailsId  ")
						.setParameter("packageCapDetailsId",packageCapDetailsId)
						.setParameter("qty", qty)
						.setDouble("capAmount", amount)
						.executeUpdate();
				t.commit();
				session.close();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}else {
				
				Session session = sessionFactory.openSession();
				Transaction t = session.beginTransaction();
			    session.createSQLQuery(" update " 
			    		 + " 	billing.t_package_consumption_cap_details " 
			    		 + " set " 
			    		 + " 	available_cap = " 
			    		 + " 	(case " 
			    		 + (isRecalculation ? " when (coalesce(available_cap, 0))=cap_amount then cap_amount " : "")
			    		 + "		when (coalesce(available_cap, 0))+:capAmount > cap_amount then cap_amount " 
			    		 + " 		else (coalesce(available_cap, 0))+:capAmount " 
			    		 + " 	end), " 
			    		 + " 	quantity =  " 
			    		 + " 	(case " 
			    		 + " 		when (coalesce(quantity, 0))=package_qty then package_qty "
			    		 + "		when (coalesce(quantity, 0))+:qty > package_qty then package_qty " 
			    		 + " 		else (coalesce(quantity, 0))+:qty " 
			    		 + " 	end) " 
			    		 + " where " 
			    		 + " 	package_consumption_cap_details_id = :packageCapDetailsId  ")
						.setParameter("packageCapDetailsId",packageCapDetailsId)
						.setParameter("qty", qty)
						.setDouble("capAmount", amount)
						.executeUpdate();
				t.commit();
				session.close();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateEitherCapItem(Integer packageConsumptionEODetailId, Double capAmount)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateEitherQuantityCapItem(Integer packageConsumptionEODetailId, Integer qty, Double capAmount)
			throws ServiceException 
	{
		try{
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createSQLQuery("update " + 
					"	billing.t_package_consumption_eo_details set balance_quantity = (coalesce(balance_quantity,0))+:qty ,group_price = (coalesce(group_price,0))+:capAmount" + 
					"  where pacakge_consumtion_eo_detail_id  =:packageConsumptionEODetailId")
					.setParameter("packageConsumptionEODetailId",packageConsumptionEODetailId)
					.setParameter("qty", qty)
					.setDouble("capAmount", Math.round(GlobalCommonDateUtils.getAmountCap(capAmount) * 100.0)/100.0)
					.executeUpdate();
			t.commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	

}
