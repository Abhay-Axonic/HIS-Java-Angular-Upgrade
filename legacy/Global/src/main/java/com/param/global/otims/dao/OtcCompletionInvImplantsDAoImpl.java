package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.otims.model.OtcCompletionInvImplants;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtcCompletionInvImplantsDAoImpl extends GenericDao<OtcCompletionInvImplants, Integer> implements ICommonConstants, IOtcCompletionInvImplantsDao{
	final static Logger logger = Logger.getLogger(OtcCompletionInvImplantsDAoImpl.class);


	public OtcCompletionInvImplantsDAoImpl() {
		super(OtcCompletionInvImplants.class);
	}

	@Override
	public Response saveInvImplants(OtcCompletionInvImplantsDto dto)
			throws ApplicationException {
		try{
			
			OtcCompletionInvImplants master = null;
			if(dto.getOtcCompletionInvImplantsId()!=null && dto.getOtcCompletionInvImplantsId()>0) {
				master = (OtcCompletionInvImplants) sessionFactory.getCurrentSession().get(OtcCompletionInvImplants.class, dto.getOtcCompletionInvImplantsId());
				master.setProcedureRequestId(dto.getProcedureRequestId());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setBookedQty(dto.getBookedQty());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setItemId(dto.getItemId());
				master.setMrp(dto.getMrp());
				master.setOrgId(dto.getOrgId());
				master.setRemainingQty(dto.getRemainingQty());
				master.setStatus('A');
				master.setIssueDetailsBatchMapperId(dto.getIssueDetailsBatchMpprId()!=null?dto.getIssueDetailsBatchMpprId():null);
				master.setStoreId(dto.getStoreId());
				master.setTaxPercentage(dto.getTaxPercentage());
				master.setTotalAmount(dto.getTotalAmount());
				master.setUnitId(dto.getUnitId());
				master.setBatchId(dto.getBatchId().intValue());
				master.setUsedQty(dto.getUsedQty());
				master.setCreatedBy(dto.getCreatedBy());
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUomTypeId(dto.getUomTypeId() == null?null:dto.getUomTypeId());
				master.setUomUnitId(dto.getUomUnitId() == null?null:dto.getUomUnitId());
				master.setRejectedQty(dto.getRejectedQty());
				master.setProductCategoryId(dto.getProductCategoryId());
				master.setTaxAmount(dto.getTaxAmount());
				master.setConsessionAmount(dto.getConsessionAmount());
				master.setConsessionPercentage(dto.getConsessionPercentage());
				master.setUnitConsessionAmount(dto.getUnitConsessionAmount());
				master.setNetAmount(dto.getNetAmount());
				master.setTaxId(dto.getTaxId());
				master.setFromOt(dto.getFromOT() !=null ? dto.getFromOT():false);
				master.setAllocationId(dto.getAllocationId()!= null ?dto.getAllocationId():null);
				master = update(master);
			}
			else {
			    master = new OtcCompletionInvImplants();
				master.setProcedureRequestId(dto.getProcedureRequestId());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setBookedQty(dto.getBookedQty());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setItemId(dto.getItemId());
				master.setMrp(dto.getMrp());
				master.setOrgId(dto.getOrgId());
				master.setRemainingQty(dto.getRemainingQty());
				master.setStatus('A');
				master.setIssueDetailsBatchMapperId(dto.getIssueDetailsBatchMpprId()!=null?dto.getIssueDetailsBatchMpprId():null);
				master.setStoreId(dto.getStoreId());
				master.setTaxPercentage(dto.getTaxPercentage());
				master.setTotalAmount(dto.getTotalAmount());
				master.setUnitId(dto.getUnitId());
				master.setBatchId(dto.getBatchId().intValue());
				master.setUsedQty(dto.getUsedQty());
				master.setCreatedBy(dto.getCreatedBy());
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUomTypeId(dto.getUomTypeId() == null?null:dto.getUomTypeId());
				master.setUomUnitId(dto.getUomUnitId() == null?null:dto.getUomUnitId());
				master.setRejectedQty(dto.getRejectedQty());
				master.setProductCategoryId(dto.getProductCategoryId());
				master.setTaxAmount(dto.getTaxAmount());
				master.setConsessionAmount(dto.getConsessionAmount());
				master.setConsessionPercentage(dto.getConsessionPercentage());
				master.setUnitConsessionAmount(dto.getUnitConsessionAmount());
				master.setNetAmount(dto.getNetAmount());
				master.setTaxId(dto.getTaxId());
				master.setFromOt(dto.getFromOT() !=null ? dto.getFromOT():false);
				master.setAllocationId(dto.getAllocationId()!= null ?dto.getAllocationId():null);
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
	public Response getImplantIssueAcceptanceData(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto)
			throws ApplicationException {
try{
					List<OtcCompletionInvImplantsDto> issueAcceptanceList = sessionFactory.getCurrentSession().createSQLQuery(
					"select pid.item_id as \"itemId\", "
					+"itm.name as \"itemName\", "
					+"itm.code as \"itemCode\", "
					+"umt.type as \"uomType\", "
					+"umt.id as \"uomTypeId\", "
					+"unt.code as \"uomUnit\", "
					+"unt.id as \"uomUnitId\", "
					//+"stx.id as \"salesTaxId\", "
					//+"stx.tax_percentage as \"salesTaxPercentage\", "
					+"ptx.id as \"purchaseTaxId\", "
					+"ptx.tax_percentage as \"purchaseTaxPercentage\", "
					+"st.store_name as \"storeName\", "
					+"idbm.amount as \"mrp\", "
					+"idbm.accepted_quantity as\"bookedQty\", "
					+"idbm.issue_quantity as\"issuedQty\", "
					+"to_char(batch.expiry_date,'YYYY-DD-MM') as \"expiryDate\", "
					+"generic.name as \"genericName\", "
					+"batch.batch_no as \"batchNo\", "
					+"batch.batch_no || ' | ' || to_char(batch.expiry_date, 'MMM-yyyy') as \"batchDetails\", "
					+"CAST(batch.id AS int4) as \"batchId\" "
					+"from pharmacy.t_issue issue "
					+"inner join pharmacy.t_issue_detail issue_detail on issue_detail.issue_id =issue.id "
					+"inner join pharmacy.t_patient_indent_detail pid on issue_detail.patient_indent_detail_id = pid.id "
					+"inner join pharmacy.t_patient_indent pi on pid.patient_indent_id=pi.id "
					+"inner join public.m_item itm on pid.item_id = itm.id "
					+"inner join public.m_uom_type umt on pid.uom_type_id = umt.id "
					+"inner join public.m_uom_unit unt on pid.uom_unit_id = unt.id "
					+"inner join public.m_store st on pi.store_id = st.store_id "
					+"inner join public.m_generic generic on itm.generic_id = generic.id "
					+"inner join pharmacy.t_issue_detail_batch_mapper idbm on issue_detail.id = idbm.issue_detail_id "
					+"inner join inventory.t_batch batch on idbm.batch_id = batch.id "
					+"inner join patient.t_patient_registration pr on pi.patient_id = pr.patient_id "
					//+"inner join public.m_tax stx on itm.s_tax_id = stx.id "
					+"inner join public.m_tax ptx on itm.p_tax_id = ptx.id "
					+"where issue.status_id=4 AND pr.uhid_number =:uhidNumber "
					+" AND itm.is_implant=true "
					).setParameter("uhidNumber",otcCompletionInvImplantsDto.getUhidNumber()).setResultTransformer(Transformers.aliasToBean(OtcCompletionInvMedicationDto.class)).list();
		
	//System.out.println("issueAcceptanceList"+issueAcceptanceList);
	////System.out.println("issueAcceptanceList"+query.list());
		return new Response<>("success", "200", "success", issueAcceptanceList,null);
		}
		 catch(Exception e){
		 logger.error("getImplantIssueAcceptanceData failed", e);
		 }
		return new Response<>("error", null, null, null,null);
	}
	
	
	@Override
	public Response updateIssueStatus(OtcCompletionInvImplantsDto meddto) throws ApplicationException {
		try{
			Integer count = 
					sessionFactory.getCurrentSession().createSQLQuery(
					"UPDATE pharmacy.t_issue "
					+" SET exported_to_otbilling=true "
					+ " WHERE id=:id ")
							.setParameter("id",meddto.getIssueId())
					.executeUpdate();
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
	public Response getInvImplants(int proReqId) throws ApplicationException {
		try{
			List<OtcCompletionInvImplantsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_COMPLETION_IMPLANT_LIST").setInteger("id", proReqId).setResultTransformer(Transformers.aliasToBean(OtcCompletionInvImplantsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}
}
