package com.param.global.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.OperationsCompletionInventory;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.otims.dto.UomDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class OTCompletionDao extends AbstractDao<Integer, Item> implements IOTCompletionDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(OTCompletionDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;


	@Override
	public List<ItemsSearchResponse> itemsSearch(boolean isImplant, String search, Integer storeId , Integer unitId) {

		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		String hql=null;
		if(isImplant)
		{

		 hql = "SELECT item.id AS id, item.code || ' | ' || item.name AS details, item.isThreeUOMApplicable AS isThreeUOMApplicable, assetCategory.id as assetCategoryId ,productCategory.id as productCategoryId  FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		left JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0  and storeStock.availableQty is not null "
				+ "		Left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
			//	+ "		LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		Left JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true  "
				+ "		Left JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true  "
				+ "		LEFT JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				//+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		left JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId AND item.isImplant = :isImplant "
				+ "		AND (upper(item.code) like upper(:search) OR upper(item.name) like upper(:search)) "
				+ "		 AND item.id NOT IN (SELECT itemRestriction.item.id "
				+ "							FROM StoreItemRestriction AS itemRestriction "
				+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							WHERE itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
				+ "	     AND batch.expiryDate > :greaterThanCurrentMonth ";
		
		}
		else
		{
			 hql = "SELECT item.id AS id, item.code || ' | ' || item.name AS details, item.isThreeUOMApplicable AS isThreeUOMApplicable, assetCategory.id as assetCategoryId ,productCategory.id as productCategoryId FROM Batch AS batch "
					+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
					+ "		left JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0  "
					+ "		Left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ "		LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
					+ "		LEFT JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
					+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
					+ "		left JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId AND item.isImplant = :isImplant "
					+ "		AND (upper(item.code) like upper(:search) OR upper(item.name) like upper(:search)) "
					+ "		 AND item.id NOT IN (SELECT itemRestriction.item.id "
					+ "							FROM StoreItemRestriction AS itemRestriction "
					+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "							WHERE itemRestriction.unit.unitId = :unitId "
					+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
					+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
					+ "	     AND batch.expiryDate > :greaterThanCurrentMonth ";
		}

		StringBuffer sb = new StringBuffer(hql);
		sb.append(sessionService.getStoreId()!=null?" AND store.storeId = :storeId  AND  storeStock.store.storeId = :storeId ":" ");
		sb.append("		GROUP BY item.id ,assetCategory.id ,productCategory.id" + "     ORDER BY item.id");
		Query query = getSession().createQuery(sb.toString());
		if(sessionService.getStoreId()!=null)
		{
			query.setParameter("storeId", storeId!=null ? storeId : sessionService.getStoreId());
		}
		//query.setParameter("unitId",1);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		//query.setParameter("isConsumable", isConsumable);
		query.setParameter("isImplant", isImplant);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("search", search + '%');
		query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));

		return query.list();
	}

	@Override
	public OperationsCompletionInventory saveOTCompletions(OperationsCompletionInventory completionInventory) {
		getSession().save(completionInventory);
		return completionInventory;
	}

	@Override
	public Sale saveSale(Sale sale) {
		getSession().save(sale);
		return sale;
	}

	@Override
	public Issue saveIssue(Issue issue) {
		getSession().save(issue);
		return issue;
	}


	@Override
	public Response getImplantIssueAcceptanceData(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto) {
		
		try{
			
		
			List<OtcCompletionInvMedicationDto> issueAcceptanceList = sessionFactory.getCurrentSession().createSQLQuery(
					"select " + 
					"	pid.item_id as \"itemId\", " + 
					"	itm.name as \"itemName\", " + 
					"	itm.code as \"itemCode\", " + 
					"	itm.is_implant as \"isImplant\", " + 
					"	itm.is_consumable as \"isConsumable\", " + 
					"	umt.type as \"uomType\", " + 
					"	umt.id as \"uomTypeId\", " + 
					"	unt.code as \"uomUnit\", " + 
					"	unt.id as \"uomUnitId\", " + 
					"	issue.id as \"issueId\", " + 
					"	ptx.id as \"taxId\"," + 
					"	ptx.tax_percentage as \"taxPercent\", " + 
					"	st.store_name as \"storeName\", " + 
					"	idbm.amount as \"mrp\", " + 
					"   idbm.id as \"issueDetailsBatchMpprId\" ,"+
					"	idbm.accepted_quantity as \"bookedQty\", " + 
					"	idbm.issue_quantity as \"issuedQty\", " + 
					"	to_char(batch.expiry_date, " + 
					"	'YYYY-DD-MM') as \"expiryDate\"," + 
					"	batch.batch_no as \"batchNo\", " + 
					"	batch.batch_no || ' | ' || to_char(batch.expiry_date, " + 
					"	'MMM-yyyy') as \"batchDetails\", " + 
					"  (case when generic.product_category_id is null then itm.product_category_id else generic.product_category_id end )as \"productCategoryId\", " +
					"	cast(batch.id as int4) as \"tempBatchId\","
					+"proct.asset_category_id as \"assetCategoryId\"  " + 
					"from pharmacy.t_issue issue " + 
					"inner join pharmacy.t_issue_detail issue_detail on issue_detail.issue_id = issue.id " + 
					"inner join pharmacy.t_patient_indent_detail pid on issue_detail.patient_indent_detail_id = pid.id " + 
					"inner join pharmacy.t_patient_indent pi on pid.patient_indent_id=pi.id " + 
					"inner join public.m_item itm on pid.item_id = itm.id " + 
					"left join public.m_generic generic on itm.generic_id = generic.id " + 
					"left join public.m_uom_type umt on pid.uom_type_id = umt.id " + 
					"left join public.m_uom_unit unt on pid.uom_unit_id = unt.id " + 
					"inner join public.m_store st on pi.to_store_id = st.store_id " + 
					"inner join pharmacy.t_issue_detail_batch_mapper idbm on issue_detail.id = idbm.issue_detail_id " + 
					"inner join inventory.t_batch batch on idbm.batch_id = batch.id " + 
					"inner join patient.t_patient_registration pr on pi.patient_id = pr.patient_id " + 
					"inner join public.m_tax ptx on itm.p_tax_id = ptx.id "
					+ "inner join public.m_product_category proct on itm.product_category_id = proct.id " + 
					"where issue.status_id = 8 " + 
					"	and pr.uhid_number =:uhidNumber " +
					"	and itm.is_implant = true" + 
					"	and issue.exported_to_otbilling = false" ).setParameter("uhidNumber",otcCompletionInvImplantsDto.getUhidNumber()).setResultTransformer(Transformers.aliasToBean(OtcCompletionInvImplantsDto.class)).list();
		
		return new Response<>("success", "200", "success", issueAcceptanceList,null);
		}
		 catch(Exception e){
				logger.error("getImplantIssueAcceptanceData failed", e);
			}
		return new Response<>("error", null, null, null,null);
	}

	@Override
	public Response getIssueAcceptanceData(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto) {
		
		try{
			
		
			List<OtcCompletionInvMedicationDto> issueAcceptanceList = sessionFactory.getCurrentSession().createSQLQuery(
					"select pid.item_id as \"itemId\", "
					+"itm.name as \"itemName\", "
					+"itm.code as \"itemCode\", "
					+"itm.is_implant as \"isImplant\", "
					+"itm.is_consumable as \"isConsumable\", "
					+"umt.type as \"uomType\", "
					+"umt.id as \"uomTypeId\", "
					+"unt.code as \"uomUnit\", "
					+"unt.id as \"uomUnitId\", "
					+"issue.id as \"issueId\", "
					+"ptx.id as \"taxId\", "
					+"ptx.tax_percentage as \"taxPercent\", "
					+"st.store_name as \"storeName\", "
					+"idbm.amount as \"mrp\", "
					+"idbm.id as \"issueDetailsBatchMpprId\" ,"
					+"idbm.accepted_quantity as\"bookedQty\", "
					+"idbm.issue_quantity as\"issuedQty\", "
					+"to_char(batch.expiry_date,'YYYY-DD-MM') as \"expiryDate\", "
					+"generic.name as \"genericName\", "
					+"generic.product_category_id as \"productCategoryId\", "
					+"batch.batch_no as \"batchNo\", "
					+"batch.batch_no || ' | ' || to_char(batch.expiry_date, 'MMM-yyyy') as \"batchDetails\", "
					+"CAST(batch.id AS int4) as \"tempBatchId\","
					+"proct.asset_category_id as \"assetCategoryId\"  "
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
					+"inner join public.m_tax ptx on itm.p_tax_id = ptx.id "
					+ "inner join public.m_product_category proct on generic.product_category_id = proct.id "
					+"where issue.status_id=8 AND pr.uhid_number =:uhidNumber "
					+" AND itm.is_implant=false AND issue.exported_to_otbilling=false "
					).setParameter("uhidNumber",otcCompletionInvMedicationDto.getUhidNumber()).setResultTransformer(Transformers.aliasToBean(OtcCompletionInvMedicationDto.class)).list();
		
	//System.out.println("issueAcceptanceList"+issueAcceptanceList);
	////System.out.println("issueAcceptanceList"+query.list());
		return new Response<>("success", "200", "success", issueAcceptanceList,null);
		}
		 catch(Exception e){
				logger.error("getIssueAcceptanceData failed", e);
			}
		return new Response<>("error", null, null, null,null);
	}

	@Override
	public Response updateIssue(Sale sale, Integer issueId) {
		
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_ISSUE").setLong("saleId", sale.getId()).setParameter("issueId", issueId).executeUpdate();
			return new Response<>("success", "200", "success", null,result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBatchListByItemId(int storeId, Integer itemId, Integer orgId, Integer unitId)
			throws ArithmeticException {
		try
		{
			List<BatchDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_BATCH_DETAILS_BY_ITEM_ID")
					.setParameter("storeId", storeId)
					.setParameter("itemId", itemId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BatchDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error("getBatchListByItemId failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response getUOMListByBatchId(Integer batchId, Integer orgId, Integer unitId) throws ArithmeticException {
		try
		{
			List<UomDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_UOM_DETAILS_BY_BATCH_ID")
					.setParameter("batchId", batchId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UomDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error("getUOMListByBatchId failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

}
