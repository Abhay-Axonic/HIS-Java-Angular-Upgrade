package com.param.pharmacy.master.unit.dao.Kit;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Kit;
import com.param.entity.model.master.KitType;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.MedicationDetailsDto;
import com.param.global.otims.dto.UomDto;
import com.param.pharmacy.master.unit.dto.kit.KitMasterResponseList;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Repository("KitTypeMasterDao")
public class KitTypeMasterDao extends AbstractDao<Integer, Kit>  implements IKitTypeMasterDao, ICommonConstants{
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(KitTypeMasterDao.class);

	@Override
	public KitType kitTypeMasterSave(KitType KitTypeMaster) {
		getSession().save(KitTypeMaster);
		return KitTypeMaster;
		
	}
	
	@Override
	public List<KitTypeMasterResponse> getAllKitType()
	{
		String hql="SELECT kitType.id AS id,kitType.code AS code,kitType.description AS description,kitType.isActive AS status FROM KitType AS kitType"
				+ " where  kitType.isDeleted=false " 
				+ "ORDER BY kitType.id ASC";
				
		Query query = getSession().createQuery(hql);
		
		
		query.setResultTransformer(Transformers.aliasToBean(KitTypeMasterResponse.class));
		return  (List<KitTypeMasterResponse>)query.list();
	}
	
	@Override
	public KitType getById(Integer id)
	{
			
		Criteria criteria = getSession().createCriteria(KitType.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		return (KitType) criteria.uniqueResult();
	}
	
	@Override
	public KitType getKitType(Integer id)
	{
		Criteria criteria = getSession().createCriteria(KitType.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		return (KitType) criteria.uniqueResult();
	}
	
	@Override
	public Integer update(KitType kitType) {
		getSession().update(kitType);
		return kitType.getId();
	}
	
	@Override
	public Kit saveKitMaster(Kit kit) {
		getSession().save(kit);
		return kit;
	}
	
	@Override
	public List<Kit> getAll(Integer orgId , Integer unitId) {
		
		String hql = "SELECT kit FROM Kit AS kit " + "  where  kit.isDeleted=false AND kit.unit.unitId = :unitId AND kit.organization.organizationId = :orgId"
				+ " ORDER BY description ASC";
				
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId);
		query.setParameter("orgId", orgId);
		return query.list();
	}
	
	@Override
	public Kit getByKitMasterId(Integer id) {
		String hql = "SELECT kit FROM Kit AS kit " + "WHERE kit.id = :id  AND kit.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Kit) query.uniqueResult();
	}
	@Override
	public Kit getKitMasterById(Integer id,Integer unitId) {
		
			String hql = " SELECT kit FROM Kit AS kit"
					+ " WHERE  kit.isDeleted = false AND kit.id = :id"
					+ " AND kit.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (Kit) query.uniqueResult();
		
	}
	
	
	@Override
	public Kit updateKitDetailMaster(Kit kit) {
		getSession().update(kit);
		return kit;
	}
	
	/*
	Override
	public List<Item> getItemSearch(String name) {

		String hql = "SELECT item FROM Item as item "
				+ " WHERE (lower(item.name) LIKE :name OR ( lower(item.code) LIKE :name)) "
				+ " AND item.isActive = true AND item.isDeleted = false AND item.isImplant = true ";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		// query.setParameter("unitId", sessionService.getUnitId());
		return query.list();

	}
	}
	 */
	
	@Override
	public  List<Kit> getKitMasterByCodeAndDecriptionSearch(String name) {

		String hql = "SELECT kit FROM Kit as kit "
				+ " WHERE(lower (kit.code) LIKE :name OR ( lower(kit.description) LIKE :name)) "

				+ " AND kit.isActive=true AND kit.isDeleted = false";
				Query query = getSession().createQuery(hql);
			//query.setParameter("code", code);
		//query.setParameter("description", description);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		//query.setParameter("description", description != null ? description.toLowerCase() + '%' : "");
		
		return  query.list();

		//return (Kit) query.uniqueResult();

	}

	@Override
	public List<Kit> getMedicationKitByCodeAndDecriptionSearch(String name) {
		String hql = "SELECT kit FROM Kit as kit "
				+ " WHERE(lower (kit.code) LIKE :name OR ( lower(kit.description) LIKE :name)) "
				+ " AND kit.isActive=true AND kit.isDeleted = false"
				+ " AND kit.kitType = 1";
				Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		
		return  query.list();
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public Response getKitDetailsByIdAndType(Integer storeId, Integer kitId, Integer kitTypeId,
			Integer orgId, Integer unitId) throws ApplicationException{
		try
		{
			List<MedicationDetailsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_KIT_DETAILS_BY_ID_AND_TYPE")
				//	.setParameter("storeId", storeId)
					.setParameter("kitTypeId", kitTypeId)
					.setParameter("kitId", kitId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MedicationDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response getBatchListByItemId(Integer storeId, Integer itemId, Integer orgId, Integer unitId)
			throws ApplicationException {
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
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getUOMListByBatchId( Integer batchId, Integer orgId, Integer unitId)
			throws ApplicationException {
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
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Object updateStatus(KitTypeMasterRequest request) {
	
		Integer count=sessionFactory.getCurrentSession().createSQLQuery(" UPDATE m_kit set is_active =:status where id =:id ")
				.setParameter("status", request.getStatus())
				.setParameter("id", request.getId())
				.executeUpdate();

		return count;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getKitDetails(Integer kitId, Integer storeId, Integer unitId) {
		
		try{
			Date date = TimeZoneComponent.getDateByzone(unitId);
			List<KitMasterResponseList> issueAcceptanceList = sessionFactory.getCurrentSession().createSQLQuery(
					" select  "
					+ "mi.name as \"itemName\", "
					+ "mi.code \"itemCode\", "
					+ "(case " +
						" when mi.is_lasa = true then true else false end ) as \"isLasa\" , "
					+ "mut.type  \"uomType\" ,"
					+ "muu.unit  \"uomUnit\","
					+ "mkd.quantity  \"quantity\","
					+ "mani.name  \"manufacturerName\","
					+ "mg.name  \"genericName\","
					+ "(select sum(tss.available_qty) from inventory.t_store_stock tss "
					+ "inner join inventory.t_batch tb on tb.id = tss.batch_id "
					+ "where tb.item_id = mi.id and (to_char(tb.expiry_date,'YYYY-MM-DD') >=:createdDate or mi.is_batch_required is   false) and tss.store_id =:storeId )  \"availableQty\" "
					+ "from m_kit_detail mkd "
					+ "inner join m_item mi on mi.id = mkd.item_id "
					+ "inner join m_uom_type mut on mut.id = mkd.uom_type_id "
					+ "inner join public.m_uom_unit muu on muu.id = mkd.uom_unit_id "
					+ "inner join m_manufacturer mani on mani.id = mi.manufacturer_id "
					+ "left join m_generic mg on mg.id = mi.generic_id "
					+ "where mkd.is_active = true and mkd.is_deleted = false and mi.is_active  = true and mi.is_deleted = false and kit_id ="+kitId
					)
					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("storeId", storeId)
					.setResultTransformer(Transformers.aliasToBean(KitMasterResponseList.class)).list();
		
		return new Response<>("success", "200", "success", issueAcceptanceList,null);
		}
		 catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
			}
	}

}
