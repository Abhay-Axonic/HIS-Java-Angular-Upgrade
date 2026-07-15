package com.param.CSSD.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSD.dto.CssdOpeningBalanceRequest;
import com.param.CSSD.dto.CssdOpeningStockDto;
import com.param.CSSD.model.CssdOpeningBalanceDetailStock;
import com.param.CSSD.model.CssdOpeningBalanceStock;
import com.param.CSSD.model.CssdStockTransactionLog;
import com.param.entity.model.inventory.Batch;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CSSDOpeningStockDaoImpl extends GenericDao<CssdOpeningBalanceStock, Integer>
		implements ICSSDOpeningStockDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(CSSDOpeningStockDaoImpl.class);


	public CSSDOpeningStockDaoImpl() {
		super(CssdOpeningBalanceStock.class);
// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public Response saveOpeningStock(CssdOpeningStockDto cssdOpeningStockDto1) {
		try {
			List<CssdOpeningStockDto> kitInstrumentList = new ArrayList<CssdOpeningStockDto>();
			kitInstrumentList = cssdOpeningStockDto1.getKitInstrumentList();
			// for(CssdOpeningStockDto cssdOpeningStockDto:kitInstrumentList) {
			CssdOpeningBalanceStock cssdOpeningBalanceStock = new CssdOpeningBalanceStock();
			cssdOpeningBalanceStock.setOrganizationId(cssdOpeningStockDto1.getOrganizationId());
			cssdOpeningBalanceStock.setUnitId(cssdOpeningStockDto1.getUnitId());
			cssdOpeningBalanceStock.setStoreId(cssdOpeningStockDto1.getStoreId());
			// cssdOpeningBalanceStock.setKitId(cssdOpeningStockDto.getKitId());
			cssdOpeningBalanceStock.setStatusId(1);
			// cssdOpeningBalanceStock.setIsKit(cssdOpeningStockDto.getIsKit());
			cssdOpeningBalanceStock.setIsActive(true);
			cssdOpeningBalanceStock.setUpdatedBy(cssdOpeningStockDto1.getUpdated_by());
			cssdOpeningBalanceStock.setUpdatedDate(TimeZoneComponent.getDateByzone(cssdOpeningStockDto1.getUnitId()));
			cssdOpeningBalanceStock.setAddedBy(cssdOpeningStockDto1.getCreated_by());
			cssdOpeningBalanceStock.setAddedDate(TimeZoneComponent.getDateByzone(cssdOpeningStockDto1.getUnitId()));
			cssdOpeningBalanceStock.setApprovedBy(cssdOpeningStockDto1.getCreated_by());
			cssdOpeningBalanceStock.setApproveDate(TimeZoneComponent.getDateByzone(cssdOpeningStockDto1.getUnitId()));
			// cssdOpeningBalanceStock.setKitQuantity(cssdOpeningStockDto.getKitQty());
			cssdOpeningBalanceStock.setIsDeleted(false);
			sessionFactory.getCurrentSession().save(cssdOpeningBalanceStock);

			// save opening balance details

			for (CssdOpeningStockDto instrumentDto : kitInstrumentList) {
				CssdOpeningBalanceDetailStock balanceDetailStock = new CssdOpeningBalanceDetailStock();
				balanceDetailStock.setOrganizationId(cssdOpeningStockDto1.getOrganizationId());
				balanceDetailStock.setUnitId(cssdOpeningStockDto1.getUnitId());
				balanceDetailStock.setOpeningBalanceId(cssdOpeningBalanceStock.getId());
				balanceDetailStock.setInstrumentId(instrumentDto.getInstrumentId());
				balanceDetailStock.setInstrumentQuantity(instrumentDto.getQuantity());
				balanceDetailStock.setBatchNo("OPB" + cssdOpeningBalanceStock.getId());
				balanceDetailStock.setIsSterilize(instrumentDto.getIsSterilize());
				if (instrumentDto.getIsSterilize().equals(true)) {
					balanceDetailStock.setSterilizationExpiryDate(
							GlobalCommonDateUtils.getDate(instrumentDto.getSterilizedExpiryDate(), "dd-M-yyyy HH:mm:ss"));
				}
				balanceDetailStock.setStatusId(1);
				balanceDetailStock.setIsActive(true);
				balanceDetailStock.setUpdatedBy(cssdOpeningStockDto1.getUpdated_by());
				balanceDetailStock.setUpdatedDate(TimeZoneComponent.getDateByzone(cssdOpeningStockDto1.getUnitId()));
				balanceDetailStock.setAddedBy(cssdOpeningStockDto1.getCreated_by());
				balanceDetailStock.setAddedDate(TimeZoneComponent.getDateByzone(cssdOpeningStockDto1.getUnitId()));
				balanceDetailStock.setApprovedBy(cssdOpeningStockDto1.getCreated_by());
				balanceDetailStock.setApproveDate(TimeZoneComponent.getDateByzone(cssdOpeningStockDto1.getUnitId()));
				balanceDetailStock.setIsDeleted(false);
				balanceDetailStock.setIsKit(instrumentDto.getIsKit());
				if (instrumentDto.getIsKit().equals('Y')) {
					balanceDetailStock.setKitId(instrumentDto.getKitId());
					balanceDetailStock.setKitQuantity(instrumentDto.getKitQty());
				}
				sessionFactory.getCurrentSession().save(balanceDetailStock);
			}

			// }
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getCssdOpeningBalanceList(CssdOpeningBalanceRequest request) {
		try {
			request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		/*	String hql = "SELECT openingBalanceCSSD.id as cssdOpeningBalanceId,openingBalanceCSSD.statusId as statusId,store.storeName as storeName,"
					+ " to_char(openingBalanceCSSD.addedDate,'dd-MM-yyyy') as  created_date,"
					+ " user.userName as requestedBy,statusObj.status as status FROM CssdOpeningBalanceStock AS openingBalanceCSSD "
					+ "	LEFT JOIN openingBalanceCSSD.store as store "
					+ " LEFT JOIN openingBalanceCSSD.userMaster as user "
					+ " LEFT JOIN openingBalanceCSSD.status as statusObj "
					+ "WHERE openingBalanceCSSD.isActive = true AND openingBalanceCSSD.isDeleted = false AND openingBalanceCSSD.unitId = :unitId "
					+ "		AND to_char(openingBalanceCSSD.addedDate,'yyyy-MM-dd') BETWEEN '"+ request.getFromDate()+"'"+" AND '"+request.getToDate()+"'"
					+ " 	AND (:storeId = 0 OR store.storeId = :storeId) "
					+ "		AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
					+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
					+ "										AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
					+ "ORDER BY openingBalanceCSSD.id DESC";*/
			String sql="select" + 
					"	cssdopening.id as \"cssdOpeningBalanceId\"," + 
					"	cssdopening.status_id as \"statusId\"," + 
					"	store1_.store_name as \"storeName\"," + 
					"	to_char(cssdopening.added_date, 'dd-MM-yyyy') as \"created_date\"," + 
					"	usermaster2_.user_name as \"requestedBy\"," + 
					"	status3_.status as \"status\" " + 
					" from" + 
					"	cssd.t_cssd_opening_balance cssdopening" + 
					" left outer join public.m_store store1_ on" + 
					"	cssdopening.store_id = store1_.store_id" + 
					" left outer join public.m_user_master usermaster2_ on" + 
					"	cssdopening.added_by = usermaster2_.user_id" + 
					" left outer join public.m_status status3_ on" + 
					"	cssdopening.status_id = status3_.id" + 
					" where" + 
					"	cssdopening.is_active = true" + 
					"	and cssdopening.is_deleted = false" + 
					"	and cssdopening.unit_id =:unitId" + 
					"	and (to_char(cssdopening.added_date, 'yyyy-MM-dd') between :fromDate and :toDate)" + 
					"	and (:storeId= 0" + 
					"	or store1_.store_id =:storeId)" + 
					"	and (store1_.store_id in (" + 
					"	select" + 
					"		userstoreMppr.store_id" + 
					"	from" + 
					"		public.m_user_store_mapper userstoreMppr" + 
					"	cross join public.m_store store" + 
					"	where" + 
					"		userstoreMppr.store_id = store.store_id" + 
					"		and userstoreMppr.user_id =:userId" + 
					"		and userstoreMppr.is_active = true" + 
					"		and userstoreMppr.is_deleted = false" + 
					"		and userstoreMppr.unit_id =:unitId" + 
					"		and store.is_deleted = false" + 
					"		and store.is_active = true))" + 
					" order by" + 
					"	cssdopening.id desc";
			List<CssdOpeningStockDto> openingBalanceList = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(request.getFromDate(), "yyyy-MM-dd"))
					.setParameter("toDate", GlobalCommonDateUtils.getStringDate(request.getToDate(), "yyyy-MM-dd"))
					// .setParameter("to", to)
					.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0)
					.setParameter("unitId", sessionService.getUnitId()).setParameter("userId", sessionService.getUserId())
					.setResultTransformer(Transformers.aliasToBean(CssdOpeningStockDto.class)).list();
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, openingBalanceList, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}

	@Override
	public CssdOpeningBalanceStock getCssdOpeningBalanceById(Integer opbId) {
		String hql = "SELECT cssdOpeningBalance FROM CssdOpeningBalanceStock AS cssdOpeningBalance "
				+ " WHERE cssdOpeningBalance.id = :id AND cssdOpeningBalance.isActive = true AND cssdOpeningBalance.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", opbId);
		return (CssdOpeningBalanceStock) query.uniqueResult();
	}

	@Override
	public CssdOpeningBalanceStock updateCssdOpeningBalance(CssdOpeningBalanceStock openingBalance) {
		getSession().update(openingBalance);
		return openingBalance;
	}

	@Override
	public Response getCssdOpeningBalanceImplantByOpeningBalanceId(CssdOpeningBalanceRequest request) {
		try {

			List<CssdOpeningStockDto> finalKitInsturmentList = new ArrayList<CssdOpeningStockDto>();

			List<CssdOpeningStockDto> kitList = sessionFactory.getCurrentSession()
					.createSQLQuery("select distinct opbd.kit_id as \"kitId\", "
							+ "opb.id as \"cssdOpeningBalanceId\",opbd.is_kit as \"isKit\",km.code as \"code\",km.name as \"name\",opbd.kit_quantity as \"kitQty\","
							+ " st.store_id as \"storeId\",st.store_name as \"storeName\",opbd.is_sterilize as \"isSterilize\","
							+ "to_char(opbd.sterilization_expiry_date,'dd/MM/yyyy') as \"sterilizedExpiryDate\" "
							+ "from cssd.t_cssd_opening_balance opb "
							+ "inner join cssd.t_cssd_opening_balance_detail opbd on opb.id=opbd.opening_balance_id "
							+ "left join cssd.m_kit_master km on km.kit_id=opbd.kit_id "
							+ "left join public.m_item item on item.id=opbd.item_id "
							+ "inner join m_store st on st.store_id=opb.store_id " + " where opb.id=:openingBalanceId ")
					.setParameter("openingBalanceId", request.getId())
					.setResultTransformer(Transformers.aliasToBean(CssdOpeningStockDto.class)).list();

			List<CssdOpeningStockDto> instrumentList = sessionFactory.getCurrentSession()
					.createSQLQuery("select  opbd.kit_id as \"kitId\","
							+ "opb.id as \"cssdOpeningBalanceId\",opbd.is_kit as \"isKit\",opbd.kit_quantity as \"kitQty\",opbd.item_quantity as \"quantity\", "
							+ "km.code as \"code\",km.name as \"name\",opbd.item_id as \"instrumentId\", item.name as \"instrumentName\",item.code as \"instrumentCode\","
							+ " st.store_id as \"storeId\",st.store_name as \"storeName\",opbd.is_sterilize as \"isSterilize\","
							+ "to_char(opbd.sterilization_expiry_date,'dd/MM/yyyy') as \"sterilizedExpiryDate\" "
							+ " from cssd.t_cssd_opening_balance opb "
							+ " inner join cssd.t_cssd_opening_balance_detail opbd on opb.id=opbd.opening_balance_id "
							+ " left join cssd.m_kit_master km on km.kit_id=opbd.kit_id "
							+ " left join public.m_item item on item.id=opbd.item_id "
							+ " inner join m_store st on st.store_id=opb.store_id "
							+ " where opb.id=:openingBalanceId ")
					.setParameter("openingBalanceId", request.getId())
					.setResultTransformer(Transformers.aliasToBean(CssdOpeningStockDto.class)).list();

			if (kitList.get(0).getIsKit().equals('Y')) {
				for (CssdOpeningStockDto dto1 : kitList) {
					List<CssdOpeningStockDto> kitInstumentList = new ArrayList<CssdOpeningStockDto>();
					for (CssdOpeningStockDto dto2 : instrumentList) {
						if (dto2.getKitId().equals(dto1.getKitId())) {
							kitInstumentList.add(dto2);
						}
					}
					dto1.setKitInstrumentList(kitInstumentList);
					finalKitInsturmentList.add(dto1);
				}

			} else {

				for (CssdOpeningStockDto dto1 : instrumentList) {
					List<CssdOpeningStockDto> kitInstumentList1 = new ArrayList<CssdOpeningStockDto>();
					CssdOpeningStockDto dto2 = new CssdOpeningStockDto();
					dto2.setInstrumentId(dto1.getInstrumentId());
					dto2.setInstrumentCode(dto1.getInstrumentCode());
					dto2.setInstrumentName(dto1.getInstrumentName());
					dto2.setQuantity(dto1.getQuantity());
					kitInstumentList1.add(dto2);
					dto1.setCode(dto1.getInstrumentCode());
					dto1.setName(dto1.getInstrumentName());
					dto1.setKitQty(dto1.getQuantity());
					dto1.setKitInstrumentList(kitInstumentList1);
					finalKitInsturmentList.add(dto1);
				}

			}

			return new Response<>(SUCCESS, null, null, finalKitInsturmentList, null);
		} catch (HibernateException he) {
			logger.error("getCssdOpeningBalanceImplantByOpeningBalanceId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCssdOpeningBalanceImplantByOpeningBalanceId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCssdOpeningBalanceStock(CssdOpeningStockDto cssdOpeningStockDto1) {
		try {
			List<CssdOpeningStockDto> kitInstrumentList = new ArrayList<CssdOpeningStockDto>();
			kitInstrumentList = cssdOpeningStockDto1.getKitInstrumentList();
			for(CssdOpeningStockDto cssdOpeningStockDto:kitInstrumentList) {
			if(cssdOpeningStockDto.getIsKit().equals('Y')) {
				StringBuilder query = new StringBuilder("Update CssdOpeningBalanceDetailStock det "
						+ " set det.kitQuantity=:kitQty,"
						+ "det.updatedBy=:updatedBy,det.instrumentQuantity=:itemQty,det.updatedDate='"+GlobalCommonDateUtils.getDate(cssdOpeningStockDto1.getUpdated_date(), "dd-M-yyyy HH:mm:ss")+"'");
				if(cssdOpeningStockDto.getIsSterilize().equals(true)) {
					query.append(",det.isSterilize=true,det.sterilizationExpiryDate=:sterilizationExpiryDate ");
				}else {
					query.append(",det.isSterilize=false ");
				}
				query.append(" where det.openingBalanceId=:openingBalanceId and det.instrumentId=:itemId ");

				Query hqlQuery = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("kitQty", cssdOpeningStockDto.getKitQty())
				.setParameter("updatedBy", cssdOpeningStockDto1.getUpdated_by())
				.setParameter("itemQty", cssdOpeningStockDto.getQuantity())
				//.setParameter("updatedDate", TimeZoneComponent.getDateByzone(unitId))
				.setParameter("openingBalanceId", cssdOpeningStockDto1.getCssdOpeningBalanceId())
				.setParameter("itemId", cssdOpeningStockDto.getInstrumentId());


				if(cssdOpeningStockDto.getIsSterilize().equals(true)) {
					hqlQuery.setParameter("sterilizationExpiryDate", GlobalCommonDateUtils.getDate(cssdOpeningStockDto.getSterilizedExpiryDate(), "dd-M-yyyy"));
				}

				hqlQuery.executeUpdate();
			}else {
				StringBuilder query = new StringBuilder("Update CssdOpeningBalanceDetailStock det set "
						+ "det.updatedBy=:updatedBy ,det.instrumentQuantity=:itemQty,det.updatedDate='"+GlobalCommonDateUtils.getDate(cssdOpeningStockDto1.getUpdated_date(), "dd-M-yyyy HH:mm:ss")+"'");
				if(cssdOpeningStockDto.getIsSterilize().equals(true)) {
					query.append(",det.isSterilize=true,det.sterilizationExpiryDate=:sterilizationExpiryDate ");
				}else {
					query.append(",det.isSterilize=false ");
				}
				query.append(" where det.openingBalanceId=:openingBalanceId and det.instrumentId=:itemId ");

				Query hqlQuery =sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("updatedBy", cssdOpeningStockDto1.getUpdated_by())
				.setParameter("itemQty", cssdOpeningStockDto.getQuantity())
				//.setParameter("updatedDate", TimeZoneComponent.getDateByzone(unitId))
				.setParameter("openingBalanceId", cssdOpeningStockDto1.getCssdOpeningBalanceId())
				.setParameter("itemId", cssdOpeningStockDto.getInstrumentId());

				if(cssdOpeningStockDto.getIsSterilize().equals(true)) {
					hqlQuery.setParameter("sterilizationExpiryDate", GlobalCommonDateUtils.getDate(cssdOpeningStockDto.getSterilizedExpiryDate(), "dd-M-yyyy"));
				}


				hqlQuery.executeUpdate();
			}
			
			}
			return new Response<>(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error("updateCssdOpeningBalanceStock failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCssdOpeningBalanceStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveStockTransactionLog(CssdOpeningBalanceDetailStock details,Integer storeId) {
		try {
			CssdStockTransactionLog log=(CssdStockTransactionLog)sessionFactory.getCurrentSession()
					.createQuery("SELECT log FROM CssdStockTransactionLog log where log.storeId=:storeId and log.itemId=:itemId "
							+ " and log.organizationId=:orgId and log.unitId=:unitId ")
					.setParameter("storeId", storeId)
					.setParameter("itemId",details.getItem().getId())
					.setParameter("orgId", details.getOrganizationId())
					.setParameter("unitId", details.getUnitId())
					.uniqueResult();
			//if not exit then save
			if(log==null) {
				//save
				CssdStockTransactionLog cssdStockTransactionLog=new CssdStockTransactionLog();
				cssdStockTransactionLog.setStoreId(storeId);
				cssdStockTransactionLog.setItemId(details.getItem().getId());
				cssdStockTransactionLog.setSterilizedQuantity(details.getInstrumentQuantity());
				cssdStockTransactionLog.setInTransitQuantity(0);
				cssdStockTransactionLog.setNotReturnedQuantity(0);
				cssdStockTransactionLog.setOrganizationId(details.getOrganizationId());
				cssdStockTransactionLog.setUnitId(details.getUnitId());
				cssdStockTransactionLog.setCreatedBy(details.getAddedBy());
				cssdStockTransactionLog.setCreatedDate(TimeZoneComponent.getDateByzone(details.getUnitId()));
				cssdStockTransactionLog.setUpdatedBy(details.getUpdatedBy());
				cssdStockTransactionLog.setUpdatedDate(details.getUpdatedDate());
				cssdStockTransactionLog.setStatus('A');
				sessionFactory.getCurrentSession().save(cssdStockTransactionLog);
			}else {
				//for update 
				sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
						+ " set log.sterilizedQuantity=log.sterilizedQuantity+(:sterQty),"
						+ " log.updatedBy=:updatedBy,log.updatedDate=:updatedDate"
						+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
				.setParameter("sterQty", details.getInstrumentQuantity())
				.setParameter("updatedBy", details.getUpdatedBy())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(details.getUnitId()))
				.setParameter("storeId", storeId)
				.setParameter("itemId", details.getItem().getId())
				.setParameter("orgId", details.getOrganizationId())
				.setParameter("unitId", details.getUnitId())
				.executeUpdate();
			}
			return new Response<>(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error("saveStockTransactionLog failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveStockTransactionLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
