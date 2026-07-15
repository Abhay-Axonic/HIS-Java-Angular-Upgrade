package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.otims.model.OtcCompletionInvMedication;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtcCompletionInvMedicationDaoImpl extends GenericDao<OtcCompletionInvMedication, Integer> implements ICommonConstants, IOtcCompletionInvMedicationDao{
	final static Logger logger = Logger.getLogger(OtcCompletionInvMedicationDaoImpl.class);


	public OtcCompletionInvMedicationDaoImpl() {
		super(OtcCompletionInvMedication.class);
	}

	@Override
	public Response saveInvMedications(OtcCompletionInvMedicationDto dto)
			throws ApplicationException {
		try{
			
			OtcCompletionInvMedication master = null;
			if(dto.getOtcCompletionInvMedicationId()!=null && dto.getOtcCompletionInvMedicationId()>0)
			{
				    master = (OtcCompletionInvMedication) sessionFactory.getCurrentSession().get(OtcCompletionInvMedication.class, dto.getOtcCompletionInvMedicationId());
					master.setProcedureRequestId(dto.getProcedureRequestId());
					master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					master.setBookedQty(dto.getBookedQty());
					master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					//otcCompletionInvMedication.setInventoryTypeId(otcCompletionInvMedicationDto.getInventoryTypeId());
					//otcCompletionInvMedication.setIsItemKit(otcCompletionInvMedicationDto.getIsItemKit());
					master.setItemId(dto.getItemId());
					master.setMrp(dto.getMrp()!=null?dto.getMrp():null);
					master.setOrgId(dto.getOrgId());
					master.setIssueDetailsBatchMapperId(dto.getIssueDetailsBatchMpprId()!=null?dto.getIssueDetailsBatchMpprId():null);
					master.setRemainingQty(dto.getRemainingQty());
					master.setStatus('A');
					master.setStoreId(dto.getStoreId());
					master.setTaxPercentage(dto.getTaxPercent());
					master.setTotalAmount(dto.getTotalAmount());
					master.setUnitId(dto.getUnitId());
					master.setBatchId(dto.getBatchId()!=null?dto.getBatchId().intValue():null);
					master.setUsedQty(dto.getUsedQty());
					master.setCreatedBy(dto.getCreatedBy());
					master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setUomTypeId(dto.getUomTypeId());
					master.setUomUnitId(dto.getUomUnitId());
					master.setRejectedQty(dto.getRejectedQty());
					master.setProductCategoryId(dto.getProductCategoryId());
					master.setTaxAmount(dto.getTaxAmount());
					master.setConsessionAmount(dto.getConsessionAmount());
					master.setConsessionPercentage(dto.getConsessionPercentage());
					master.setUnitConsessionAmount(dto.getUnitConsessionAmount());
					master.setNetAmount(dto.getNetAmount());
					master.setTaxId(dto.getTaxId());
					master.setAllocationId(dto.getAllocationId()!= null ?dto.getAllocationId():null);
					master.setFromOt(dto.getFromOT()!=null ? dto.getFromOT(): false);
					master = update(master);
			}
			else {
				master = new OtcCompletionInvMedication();
				master.setProcedureRequestId(dto.getProcedureRequestId());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setBookedQty(dto.getBookedQty());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				//otcCompletionInvMedication.setInventoryTypeId(otcCompletionInvMedicationDto.getInventoryTypeId());
				//otcCompletionInvMedication.setIsItemKit(otcCompletionInvMedicationDto.getIsItemKit());
				master.setItemId(dto.getItemId());
				master.setMrp(dto.getMrp()!=null?dto.getMrp():null);
				master.setOrgId(dto.getOrgId());
				master.setIssueDetailsBatchMapperId(dto.getIssueDetailsBatchMpprId()!=null?dto.getIssueDetailsBatchMpprId():null);
				master.setRemainingQty(dto.getRemainingQty());
				master.setStatus('A');
				master.setStoreId(dto.getStoreId());
				master.setTaxPercentage(dto.getTaxPercent());
				master.setTotalAmount(dto.getTotalAmount());
				master.setUnitId(dto.getUnitId());
				master.setBatchId(dto.getBatchId()!=null?dto.getBatchId().intValue():null);
				master.setUsedQty(dto.getUsedQty());
				master.setCreatedBy(dto.getCreatedBy());
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUomTypeId(dto.getUomTypeId());
				master.setUomUnitId(dto.getUomUnitId());
				master.setRejectedQty(dto.getRejectedQty());
				master.setProductCategoryId(dto.getProductCategoryId());
				master.setTaxAmount(dto.getTaxAmount());
				master.setConsessionAmount(dto.getConsessionAmount());
				master.setConsessionPercentage(dto.getConsessionPercentage());
				master.setUnitConsessionAmount(dto.getUnitConsessionAmount());
				master.setTaxId(dto.getTaxId());
				master.setNetAmount(dto.getNetAmount());
				master.setAllocationId(dto.getAllocationId()!= null ?dto.getAllocationId():null);
				master.setFromOt(dto.getFromOT()!=null ? dto.getFromOT(): false);
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response updateIssueStatus(OtcCompletionInvMedicationDto meddto) throws ApplicationException {
		try{
			Integer count =0;
			if(meddto.getIssueId() != null){
				 count =
						sessionFactory.getCurrentSession().createSQLQuery(
										"UPDATE pharmacy.t_issue "
												+" SET exported_to_otbilling=true "
												+ " WHERE id=:id ")
								.setParameter("id",meddto.getIssueId())
								.executeUpdate();
			}

		if(count>0) {
			return new Response<>("success", "200", "success", null,null);
		}
		else
		{
			return new Response<>("error", "500", "error", null,null);
		}
		}
		 catch(Exception e){
		 logger.error("updateIssueStatus failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		 }

	}

	@Override
	public Response getOtMedications(int proReqId) throws ApplicationException {
		try{
			List<OtcCompletionInvMedicationDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_COMPLETION_MEDICATION_LIST").setParameter("id", proReqId).setResultTransformer(Transformers.aliasToBean(OtcCompletionInvMedicationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
}
