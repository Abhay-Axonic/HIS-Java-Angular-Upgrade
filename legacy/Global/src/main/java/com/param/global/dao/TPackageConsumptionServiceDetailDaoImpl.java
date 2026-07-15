package com.param.global.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.packages.model.TPackageConsumptionServiceDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageConsumptionServiceDetailDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TPackageConsumptionServiceDetailDaoImpl extends GenericDao<TPackageConsumptionServiceDetail, Integer> implements ITPackageConsumptionServiceDetailDao, ICommonConstants{

	public TPackageConsumptionServiceDetailDaoImpl() {
		super(TPackageConsumptionServiceDetail.class);
	}

	@Override
	public Response saveTPackageConsumptionServiceDetail(TPackageConsumptionServiceDetailDto dto) throws Exception {
		try{
			TPackageConsumptionServiceDetail details = new TPackageConsumptionServiceDetail();
			details.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
			details.setServiceId(dto.getServiceId());
			details.setPackageQuantity(dto.getPackageQuantity());
			details.setBalancePackageQuantity(dto.getBalancePackageQuantity());
			details.setApportionedPrice(dto.getApportionedPrice());
			details.setIsServiceItem(dto.getIsServiceItem());
			details.setItemId(dto.getItemId());
			details.setPackageEoDetailId(dto.getPackageEoDetailId());
			details.setOrganisationId(dto.getOrganisationId());
			details.setUnitId(dto.getUnitId());
			details.setCreatedBy(dto.getCreatedBy());
			details.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setUpdatedBy(dto.getUpdatedBy());
			details.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setStatus(ACTIVE);
			details.setIsInclusiveExclusive(dto.getIsInclusiveExclusive());
			details.setIsCap(dto.getIsCap());
			details.setServiceCap(dto.getServiceCap());
			details.setAvailableCap(dto.getServiceCap());
			details.setIsGroupwiseExlusion(dto.getIsGroupwiseExlusion());
			details.setDeptId(dto.getDeptId());
			details.setSubDeptId(dto.getSubDeptId());
			details.setProductCategoryId(dto.getProductCategoryId());
			save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServiceByPackageConsumServiceId(int packageConsumptionId, int serviceId)throws ServiceException {
		try{
			List<TPackageConsumptionServiceDetailDto> listTPackageConsumptionServiceDetailDto = sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICE_DETAILS_BY_PACKCONSUMP_SERVICE_ID").setParameter("packageConsumptionId", packageConsumptionId).setParameter("serviceId", serviceId).setResultTransformer(Transformers.aliasToBean(TPackageConsumptionServiceDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageConsumptionServiceDetailDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getPackageServiceByPackageConsumItemId(int packageConsumptionId, int itemId)
			throws ServiceException {
		try{
			List<TPackageConsumptionServiceDetailDto> listTPackageConsumptionServiceDetailDto = sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICE_DETAILS_BY_PACKCONSUMP_ITEM_ID").setParameter("packageConsumptionId", packageConsumptionId).setParameter("itemId", itemId).setResultTransformer(Transformers.aliasToBean(TPackageConsumptionServiceDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageConsumptionServiceDetailDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePackageConsumptionServiceQty(Integer packageId, Integer patientId,
			Integer encounterId, Integer serviceId,Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			Integer updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_SERVICE_QTY")
					.setParameter("packageId", packageId)
					.setParameter("patientId", patientId)
					.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
					.setParameter("serviceId", serviceId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("qty", -1)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updatePackageConsumptionEOBalanceQty(Integer packageId, Integer patientId,
			Integer encounterId, Integer serviceId,Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			Integer updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_PACKAGE_CONUMPTION_EO_BALANCE_QTY")
					.setParameter("packageId", packageId)
					.setParameter("patientId", patientId)
					.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
					.setParameter("serviceId", serviceId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("qty", -1)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updatePackageConsumptionCapBalanceQty(Integer packageId, Integer patientId,
			Integer encounterId, Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount,Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			if(subDepartmentId!=null){
				Integer updateCount = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_SUBDEPT_CAP_BALANCE")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
						.setParameter("departmentId", departmentId)
						.setParameter("subDepartmentId", subDepartmentId)
						.setBigDecimal("serviceAmount", serviceAmount.negate())
						.setParameter("visitTypeId", visitTypeId)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
			}else{
				Integer updateCount = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_DEPT_CAP_BALANCE")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
						.setParameter("departmentId", departmentId)
						.setBigDecimal("serviceAmount", serviceAmount.negate())
						.setParameter("visitTypeId", visitTypeId)
						.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
					
			}
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePackageConsumptSevcQtyOnSrvCancl(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId, Integer admissionId, Integer visitTypeId,Integer createdBy)throws ServiceException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_SERVICE_QTY").setParameter("packageId", packageId)
							.setParameter("patientId", patientId)
							.setParameter("encounterId", encounterId != null ? encounterId : admissionId)
							.setParameter("serviceId", serviceId)
							.setParameter("visitTypeId", visitTypeId)
							.setParameter("qty", 1).executeUpdate();
			if(result>0)
			{
				Integer packageConsumptionCount = (Integer)sessionFactory.getCurrentSession()
						.getNamedQuery("PACKAGE_CONSUMPTION_CANCEL_LOG")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("serviceId", serviceId)
						.setParameter("visitId", encounterId != null ? encounterId : admissionId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("createdBy", createdBy)
						.setTimestamp("createdDate", TimeZoneComponent.getDateByzone(0))
						.setParameter("isOrderedOrCancellted", 0)
						.uniqueResult();
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServicesByEncounterPackageId(int packageId, Integer encounterId,Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			List<TPackageConsumptionServiceDetailDto> listTPackageConsumptionServiceDetailDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_SERVICES_BY_PACKAGE_ENCOUNTER_ID")
										.setParameter("encounterId", encounterId != null ? encounterId : admissionId).setParameter("packageId", packageId)
										.setParameter("visitTypeId", visitTypeId)
										.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionServiceDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageConsumptionServiceDetailDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePackageConsumptionServiceQtyMultiEnc(Integer packageId, Integer patientId, Integer serviceId,	Integer admissionId, Integer visitTypeId,Integer createdBy) throws ServiceException {
		try{
			
			Integer updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_QTY_MULTI_ENC")
					.setParameter("packageId", packageId)
					.setParameter("patientId", patientId)
					.setParameter("serviceId", serviceId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("qty", -1)
					.executeUpdate();
			
			if(updateCount>0)
			{
				Integer packageConsumptionCount = (Integer)sessionFactory.getCurrentSession()
						.getNamedQuery("PACKAGE_CONSUMPTION_LOG")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("serviceId", serviceId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("createdBy", createdBy)
						.setTimestamp("createdDate", TimeZoneComponent.getDateByzone(0))
						.setParameter("isOrderedOrCancellted", 1)
						.uniqueResult();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServicesByEncounterPackageIdWithMultyEnc(int patientId, int packageId,
			Integer encounterId, Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			List<TPackageConsumptionServiceDetailDto> listTPackageConsumptionServiceDetailDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACAKAGE_CONSUMPTION_SERVICE_DETAILS_BY_PACKAGEID_PATIENTID_WITH_MULTY_ENC")
										.setParameter("encounterId", encounterId != null ? encounterId : admissionId).setParameter("packageId", packageId)
										.setParameter("visitTypeId", visitTypeId)
										.setParameter("patientId", patientId)
										.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionServiceDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageConsumptionServiceDetailDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePackageConsumptSevcQtyOnSrvCanclME(Integer packageId, Integer patientId, Integer encounterId,
			Integer serviceId, Integer admissionId, Integer visitTypeId, Integer createdBy) throws ServiceException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_SERVICE_QTY_ME").setParameter("packageId", packageId)
							.setParameter("patientId", patientId)
							.setParameter("serviceId", serviceId)
							.setParameter("visitTypeId", visitTypeId)
							.setParameter("packageId", packageId)
							.setParameter("qty", 1).executeUpdate();
			if(result>0)
			{
				Integer packageConsumptionCount = (Integer)sessionFactory.getCurrentSession()
						.getNamedQuery("PACKAGE_CONSUMPTION_CANCEL_ME_LOG")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("serviceId", serviceId)
						.setParameter("visitId", encounterId != null ? encounterId : admissionId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("createdBy", createdBy)
						.setTimestamp("createdDate", TimeZoneComponent.getDateByzone(0))
						.setParameter("isOrderedOrCancellted", 0)
						.uniqueResult();
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updatePackageConsumptQtyITemCancelME(Integer packageId, Integer patientId, Integer encounterId,
			Integer itemId, Integer admissionId, Integer visitTypeId, Integer qty,Integer createdBy) throws ServiceException 
	{
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PACKAGE_CONSUMPTION_ITEM_QTY_ME").setParameter("packageId", packageId)
							.setParameter("patientId", patientId)
							.setParameter("itemId", itemId)
							.setParameter("visitTypeId", visitTypeId)
							.setParameter("packageId", packageId)
							.setParameter("qty", qty).executeUpdate();
			if(result>0)
			{
				Integer packageConsumptionCount = (Integer)sessionFactory.getCurrentSession()
						.getNamedQuery("PACKAGE_CONSUMPTION_CANCEL_ITEM_ME_LOG")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("itemId", itemId)
						.setParameter("visitId", encounterId != null ? encounterId : admissionId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("createdBy", createdBy)
						.setTimestamp("createdDate", TimeZoneComponent.getDateByzone(0))
						.setParameter("isOrderedOrCancellted", 0)
						.uniqueResult();
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePackageConsumptionItemeQtyMultiEnc(Integer packageId, Integer patientId, Integer itemId,Integer qty,
			Integer visitId, Integer visitTypeId, Integer createdBy) throws ServiceException {
try{
			
			Integer updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_PACKAGE_ITEM_CONSUMPTION_QTY_MULTI_ENC")
					.setParameter("packageId", packageId)
					.setParameter("patientId", patientId)
					.setParameter("itemId", itemId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("qty", -qty)
					.executeUpdate();
			
			if(updateCount>0)
			{
				Integer packageConsumptionCount = (Integer)sessionFactory.getCurrentSession()
						.getNamedQuery("PACKAGE_CONSUMPTION_ITEMS_LOG")
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("itemId", itemId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("createdBy", createdBy)
						.setTimestamp("createdDate", TimeZoneComponent.getDateByzone(0))
						.setParameter("isOrderedOrCancellted", 1)
						.uniqueResult();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}




	
}
