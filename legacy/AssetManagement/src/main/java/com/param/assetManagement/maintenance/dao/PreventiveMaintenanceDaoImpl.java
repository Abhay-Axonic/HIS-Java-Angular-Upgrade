package com.param.assetManagement.maintenance.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.maintenance.dto.AssetScheduleChecklistDto;
import com.param.assetManagement.maintenance.dto.PMSDetailsDto;
import com.param.assetManagement.maintenance.dto.PreventiveMaintenanceDto;
import com.param.assetManagement.maintenance.dto.SpareConsumptionDto;
import com.param.assetManagement.maintenance.model.AssetSchedule;
import com.param.assetManagement.maintenance.model.AssetScheduleChecklist;
import com.param.assetManagement.maintenance.model.PMSDetails;
import com.param.assetManagement.maintenance.model.PreventiveMaintenance;
import com.param.assetManagement.maintenance.model.SpareConsumption;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class PreventiveMaintenanceDaoImpl extends GenericDao<PreventiveMaintenance, Integer> implements IPreventiveMaintenanceDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(PreventiveMaintenanceDaoImpl.class);

	public PreventiveMaintenanceDaoImpl() {
		super(PreventiveMaintenance.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public int saveAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			
			AssetSchedule mstr = new AssetSchedule(); 
			
			mstr.setItemNameId(preventiveMaintenanceDto.getItemNameId());
			mstr.setItemName(preventiveMaintenanceDto.getItemName());
			mstr.setItemCode(preventiveMaintenanceDto.getItemCode());
			mstr.setNextDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getNextDate(),"dd-MM-yyyy"));
			mstr.setSerialNo(preventiveMaintenanceDto.getSerialNo());
			mstr.setPmsType(preventiveMaintenanceDto.getPmsType());
			mstr.setVendorId(preventiveMaintenanceDto.getVendorId());
			mstr.setVendor(preventiveMaintenanceDto.getVendor());
			mstr.setPmsFrequency(preventiveMaintenanceDto.getPmsFrequency());
			mstr.setRemark(preventiveMaintenanceDto.getRemark());
			mstr.setStoreId(preventiveMaintenanceDto.getStoreId());
			mstr.setManufacturerId(preventiveMaintenanceDto.getManufacturerId());
			mstr.setProcessStatus(preventiveMaintenanceDto.getProcessStatus());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-MM-yyyy HH:ss:mm"));
			mstr.setCreatedBy(preventiveMaintenanceDto.getCreatedBy());
			mstr.setStatus('A');
			mstr.setUnitId(preventiveMaintenanceDto.getUnitId());
			mstr.setOrganizationId(preventiveMaintenanceDto.getOrganizationId());
			sessionFactory.getCurrentSession().save(mstr);
			
			return  mstr.getAssetScheduleId();

			} catch (HibernateException he) {
				logger.error("saveAssetSchedule failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveAssetSchedule failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	

	@Override
	public Response saveChecklists(AssetScheduleChecklistDto assetScheduleDto) throws ApplicationException {
		try {
				AssetScheduleChecklist mstr = new AssetScheduleChecklist(); 
				
				mstr.setAssetScheduleId(assetScheduleDto.getAssetScheduleId());
				mstr.setChecklistNameId(assetScheduleDto.getChecklistNameId());
				mstr.setChecklistName(assetScheduleDto.getChecklistName());
				mstr.setChecklistCode(assetScheduleDto.getChecklistCode());
				mstr.setStatus('A');
				mstr.setCreatedBy(assetScheduleDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(assetScheduleDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
				mstr.setUnitId(assetScheduleDto.getUnitId());
				mstr.setOrganizationId(assetScheduleDto.getOrganizationId());
				
				sessionFactory.getCurrentSession().save(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

			} catch (HibernateException he) {
				logger.error("saveChecklists failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveChecklists failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
			
			String hqlUpdate = "update AssetSchedule asset set asset.remark=:remark,asset.updatedBy=:updatedBy,asset.updatedDate=:updatedDate where asset.assetScheduleId=:assetScheduleId ";
			Query qry = sessionFactory.getCurrentSession().createQuery( hqlUpdate )
			        .setParameter( "remark", preventiveMaintenanceDto.getRemark() )
			        .setParameter("updatedBy", preventiveMaintenanceDto.getUpdatedBy())
			        .setParameter("updatedDate", GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"))
			        .setParameter("assetScheduleId", preventiveMaintenanceDto.getAssetScheduleId());
			qry.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("updateAssetSchedule failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateAssetSchedule failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response fetchAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
			List<PreventiveMaintenanceDto> assessList =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ASSET_SCHEDULED_LIST")
					.setParameter("itemNameId", preventiveMaintenanceDto.getItemNameId())
					.setParameter("serialNo", preventiveMaintenanceDto.getSerialNo())
					.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AssetSchedule.class)).list();
			return new Response(SUCCESS, null, null, assessList, null);
		}catch (HibernateException he) {
			logger.error("fetchAssetSchedule failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("fetchAssetSchedule failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response fetchCheckListByAssetScheduleId(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
			List<PreventiveMaintenanceDto> checkList =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ASSET_CHECKLIST_LIST_BY_ID")
					.setParameter("assetScheduleId", preventiveMaintenanceDto.getAssetScheduleId())
					.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AssetScheduleChecklist.class)).list();
			return new Response(SUCCESS, null, null, checkList, null);
		}catch (HibernateException he) {
			logger.error("fetchCheckListByAssetScheduleId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("fetchCheckListByAssetScheduleId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response addAndUpdateChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException{
		try {
			for(AssetScheduleChecklistDto dto : preventiveMaintenanceDto.getGetChecklistData()){
				AssetScheduleChecklist check = new AssetScheduleChecklist(); 
				
					if(dto.getAssetScheduleChecklistId()!=null && dto.getAssetScheduleChecklistId()!=0){
						check.setAssetScheduleChecklistId(dto.getAssetScheduleChecklistId());
						check.setUpdatedBy(preventiveMaintenanceDto.getCreatedBy());
						check.setUpdatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
					}
					check.setChecklistNameId(dto.getChecklistNameId());
					check.setChecklistName(dto.getChecklistName());
					check.setChecklistCode(dto.getChecklistCode());
					check.setAssetScheduleId(dto.getAssetScheduleId());	
					check.setStatus('A');
					check.setCreatedBy(preventiveMaintenanceDto.getCreatedBy());
					check.setCreatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
					check.setUnitId(preventiveMaintenanceDto.getUnitId());
					check.setOrganizationId(preventiveMaintenanceDto.getOrganizationId());
				sessionFactory.getCurrentSession().saveOrUpdate(check);
			}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("addAndUpdateChecklist failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("addAndUpdateChecklist failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response deleteChecklist(PreventiveMaintenanceDto assetScheduleDto) throws ApplicationException{
		try {
			String hqlUpdate = "update AssetScheduleChecklist asset set asset.status='I',asset.updatedBy=:updatedBy,asset.updatedDate=:updatedDate where asset.assetScheduleChecklistId=:assetScheduleChecklistId ";
			Query qry = sessionFactory.getCurrentSession().createQuery(hqlUpdate)
			        .setParameter("updatedBy", assetScheduleDto.getCreatedBy())
			        .setParameter("updatedDate", GlobalCommonDateUtils.getDate(assetScheduleDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"))
			        .setParameter("assetScheduleChecklistId", assetScheduleDto.getAssetScheduleChecklistId());
			qry.executeUpdate();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			} catch (HibernateException he) {
				logger.error("deleteChecklist failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("deleteChecklist failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getAssetDetailsByCriteria(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			List<PreventiveMaintenanceDto> list =new ArrayList<PreventiveMaintenanceDto>();
            StringBuilder query=new StringBuilder();
			Query createQuery;
			String sql="select  mas.asset_schedule_id as \"assetScheduleId\",pms.pms_details_id as \"pmsDetailsId\", mas.item_name_id as \"itemNameId\", mas.item_name as \"itemName\", mas.item_code as \"itemCode\"," + 
							"        mas.next_date as \"nextDate\", mas.serial_no as \"serialNo\", mas.pms_type as \"pmsType\", mas.vendor as \"vendor\", mas.vendor_id as \"vendorId\", mas.freq as \"pmsFrequency\"," + 
							"        mas.remark as \"remark\", mas.manufacturer_id as \"manufacturerId\", mas.store_id as \"storeId\", str.store_name as \"storeName\", mas.status as \"status\"," +
							" 		 mas.scheduled_time as \"scheduledTime\", mas.scheduled_date as \"scheduledDate\", mas.scheduled_remark as \"scheduledRemark\", mas.completion_time as \"completionTime\"," +
							"		 mas.completion_date as \"completionDate\", mas.completion_remark as \"completionRemark\", mas.process_status as \"processStatus\"," + 
							" 		 mas.assign_to as \"assignTo\", mas.assign_on as \"assignOn\", mas.assign_time as \"assignTime\", mas.assign_remark as \"assignRemark\"  from equipment.m_asset_schedule mas " + 
							"		 inner join public.m_store str on str.store_id = mas.store_id "+
							"		 left join equipment.m_pms_details pms on pms.asset_schedule_id = mas.asset_schedule_id "+							
							"		 where mas.status='A' and mas.unit_id=:unitId and mas.organization_id =:organizationId ";
					query.append(sql);
                    if(preventiveMaintenanceDto.getPmsType() != null && !preventiveMaintenanceDto.getPmsType().isEmpty())
                        query.append("AND lower(mas.pms_type) LIKE concat('%',LOWER(:pmsType),'%') ");
                    if(preventiveMaintenanceDto.getStoreId() != null && preventiveMaintenanceDto.getStoreId()>0)
                        query.append(" AND mas.store_id=:storeId ");
                    if(preventiveMaintenanceDto.getAssignTo() != null && preventiveMaintenanceDto.getAssignTo()>0)
                        query.append(" AND mas.assign_to=:assignTo ");
                    if(preventiveMaintenanceDto.getProcessStatus() != null && preventiveMaintenanceDto.getProcessStatus()>0)
                        query.append(" AND mas.process_status=:processStatus ");
                    if(preventiveMaintenanceDto.getFromDate() != null && preventiveMaintenanceDto.getToDate()!=null)
                    {
                    	 query.append(" AND mas.scheduled_date BETWEEN :fromDate AND :toDate ");
						createQuery = sessionFactory.getCurrentSession()
     							.createSQLQuery(query.toString())
     							.setParameter("fromDate",Util.getDate(preventiveMaintenanceDto.getFromDate()))
    							.setParameter("toDate", Util.getDate(preventiveMaintenanceDto.getToDate()))
     							.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
     							.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId());

						if(preventiveMaintenanceDto.getPmsType() != null && !preventiveMaintenanceDto.getPmsType().isEmpty())
						{createQuery.setParameter("pmsType",preventiveMaintenanceDto.getPmsType());}
						if(preventiveMaintenanceDto.getStoreId() != null && preventiveMaintenanceDto.getStoreId()>0)
						{  createQuery.setParameter("storeId",preventiveMaintenanceDto.getStoreId());}
						if(preventiveMaintenanceDto.getAssignTo() != null && preventiveMaintenanceDto.getAssignTo()>0)
						{	createQuery.setParameter("assignTo",preventiveMaintenanceDto.getAssignTo());}
						if(preventiveMaintenanceDto.getProcessStatus() != null && preventiveMaintenanceDto.getProcessStatus()>0)
						{createQuery.setParameter("processStatus",preventiveMaintenanceDto.getProcessStatus());}


						list =createQuery.setResultTransformer(Transformers.aliasToBean(PreventiveMaintenanceDto.class)).list();
                    }
                    else
                    {
                    	  createQuery = sessionFactory.getCurrentSession()
    							.createSQLQuery(query.toString())
    							.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
    							.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId());

						if(preventiveMaintenanceDto.getPmsType() != null && !preventiveMaintenanceDto.getPmsType().isEmpty())
						{createQuery.setParameter("pmsType",preventiveMaintenanceDto.getPmsType());}
						if(preventiveMaintenanceDto.getStoreId() != null && preventiveMaintenanceDto.getStoreId()>0)
						{  createQuery.setParameter("storeId",preventiveMaintenanceDto.getStoreId());}
						if(preventiveMaintenanceDto.getAssignTo() != null && preventiveMaintenanceDto.getAssignTo()>0)
						{	createQuery.setParameter("assignTo",preventiveMaintenanceDto.getAssignTo());}
						if(preventiveMaintenanceDto.getProcessStatus() != null && preventiveMaintenanceDto.getProcessStatus()>0)
						{createQuery.setParameter("processStatus",preventiveMaintenanceDto.getProcessStatus());}

						list =createQuery.setResultTransformer(Transformers.aliasToBean(PreventiveMaintenanceDto.class)).list();

                    }
			//, mas.store_id as \"storeId\"
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getAssetDetailsByCriteria failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveAssestList(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			StringBuilder query=new StringBuilder();
			String sql="select distinct ON(item.id)item.id as \"itemNameId\", assc.id as \"assetCategoryId\"," +
							"assc.category as \"assetCategory\",pc.id as \"productCategoryId\",pc.category as \"productCategory\"," +
							" item.code as \"itemCode\", item.name as \"itemName\", batch.batch_no as \"serialNo\",  batch.pms_frequency_id as \"pmsFrequencyId\"," +
							" batch.warranty_from as \"startDate\", batch.warranty_to as \"endDate\", vndr.manufacturer_id as \"manufacturerId\"," +
							" vndr.id as \"vendorId\",vndr.name as \"vendor\" from public.m_item item " + 
							"inner join inventory.t_batch batch on item.id = batch.item_id " + 
							"inner join public.m_product_category  pc on item.product_category_id = pc.id " + 
							"inner join public.m_asset_category assc on pc.asset_category_id = assc.id " + 
							"inner join public.m_asset_type asst on asst.id = assc.asset_type_id " + 
							"inner join public.m_vendor vndr on vndr.id = batch.vendor_id " + 
							"where item.is_active=true and item.unit_id=:unitId and item.organization_id =:organizationId ";
			                query.append(sql);
							if(preventiveMaintenanceDto.getAssetCode() != null && !preventiveMaintenanceDto.getAssetCode().isEmpty())
								query.append(" AND lower(item.code) LIKE concat('%',LOWER(:assetCode),'%') ");
							if(preventiveMaintenanceDto.getAssetName() != null && !preventiveMaintenanceDto.getAssetName().isEmpty())
								query.append(" AND lower(item.name) LIKE concat('%',LOWER(:assetName),'%') ");
							if(preventiveMaintenanceDto.getAssetCategoryId() != null)
								query.append(" AND assc.id=:assetCategoryId ");
							if(preventiveMaintenanceDto.getProductCategoryId() != null)
								query.append(" AND pc.id=:productCategoryId ");
						Query createQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
					.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId());

			if(preventiveMaintenanceDto.getAssetCode() != null && !preventiveMaintenanceDto.getAssetCode().isEmpty())
				createQuery.setParameter("assetCode", preventiveMaintenanceDto.getAssetCode().toLowerCase());
			if(preventiveMaintenanceDto.getAssetName() != null && !preventiveMaintenanceDto.getAssetName().isEmpty())
				createQuery.setParameter("assetName", preventiveMaintenanceDto.getAssetCode().toLowerCase());
			if(preventiveMaintenanceDto.getAssetCategoryId() != null)
				createQuery.setParameter("assetCategoryId", preventiveMaintenanceDto.getAssetCategoryId());
			if(preventiveMaintenanceDto.getProductCategoryId() != null)
				createQuery.setParameter("productCategoryId", preventiveMaintenanceDto.getProductCategoryId());

			List<PreventiveMaintenanceDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(PreventiveMaintenanceDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getActiveAssestList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getAssetDataToView(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
			List<PreventiveMaintenanceDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("select distinct ON(item.id)item.id as \"itemNameId\", assc.id as \"assetCategoryId\"," +
							"assc.category as \"assetCategory\",pc.id as \"productCategoryId\",pc.category as \"productCategory\"," +
							" assc.code as \"itemCode\", item.name as \"itemName\", batch.batch_no as \"serialNo\",  batch.pms_frequency_id as \"pmsFrequencyId\"," +
							" batch.warranty_from as \"startDate\", batch.warranty_to as \"endDate\", vndr.manufacturer_id as \"manufacturerId\"," +
							" vndr.id as \"vendorId\",vndr.name as \"vendor\" from public.m_item item \r\n" + 
							"inner join inventory.t_batch batch on item.id = batch.item_id\r\n" + 
							"inner join public.m_product_category  pc on item.product_category_id = pc.id\r\n" + 
							"inner join public.m_asset_category assc on pc.asset_category_id = assc.id \r\n" + 
							"inner join public.m_asset_type asst on asst.id = assc.asset_type_id\r\n" + 
							"inner join public.m_vendor vndr on vndr.id = batch.service_provider_id\r\n" + 
							"where item.is_active=true and item.unit_id=:unitId and item.organization_id =:organizationId")
					.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PreventiveMaintenanceDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("getAssetDataToView failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response saveChangedSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
		
			String hqlUpdate = "update AssetSchedule asset set asset.scheduledDate=:scheduledDate,asset.scheduledTime=:scheduledTime,asset.scheduledRemark=:scheduledRemark,asset.processStatus=:processStatus where asset.assetScheduleId=:assetScheduleId ";
			
			Query qry = sessionFactory.getCurrentSession().createQuery( hqlUpdate )
			.setParameter( "scheduledRemark", preventiveMaintenanceDto.getScheduledRemark())
			.setParameter("scheduledTime", preventiveMaintenanceDto.getScheduledTime())
			.setParameter("scheduledDate", GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getScheduledDate(),"dd-M-yyyy"))
			.setParameter("processStatus", 2)
			.setParameter("assetScheduleId", preventiveMaintenanceDto.getAssetScheduleId());
			qry.executeUpdate();
			
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("saveChangedSchedule failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveChangedSchedule failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response saveCompletionDetails(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
		
			String hqlUpdate = "update AssetSchedule asset set asset.completionDate=:completionDate,asset.completionTime=:completionTime,asset.completionRemark=:completionRemark where asset.assetScheduleId=:assetScheduleId ";
			
			Query qry = sessionFactory.getCurrentSession().createQuery( hqlUpdate )
			.setParameter( "completionRemark", preventiveMaintenanceDto.getCompletionRemark())
			.setParameter("completionTime", preventiveMaintenanceDto.getCompletionTime())
			.setParameter("completionDate", GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCompletionDate(),"dd-M-yyyy"))
			.setParameter("assetScheduleId", preventiveMaintenanceDto.getAssetScheduleId());
			qry.executeUpdate();
			
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("saveCompletionDetails failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveCompletionDetails failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response fetchScheduledAndCompletionData(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			List<PreventiveMaintenanceDto> assessList =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCHEDULE_AND_COMPLETION")
					.setParameter("assetScheduleId", preventiveMaintenanceDto.getAssetScheduleId())
					.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AssetSchedule.class)).list();
			return new Response(SUCCESS, null, null, assessList, null);
			}catch (HibernateException he) {
				logger.error("fetchScheduledAndCompletionData failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("fetchScheduledAndCompletionData failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response saveOrUpdatePMSDetails(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			// Save or Update Master
			int PmsDetailsId = 0;
			
			PMSDetails mstr;
			if(preventiveMaintenanceDto.getPmsDetailsId()!=null && preventiveMaintenanceDto.getPmsDetailsId()!=0){
				mstr = (PMSDetails) sessionFactory.getCurrentSession().get(PMSDetails.class, preventiveMaintenanceDto.getPmsDetailsId());
				mstr.setUpdatedBy(preventiveMaintenanceDto.getCreatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
				mstr.setPmsDetailsId(preventiveMaintenanceDto.getPmsDetailsId());
				//				PmsDetailsId = preventiveMaintenanceDto.getPmsDetailsId();
			}else {
				mstr = new PMSDetails(); 
				mstr.setCreatedBy(preventiveMaintenanceDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
			}
			
			mstr.setAssetScheduleId(preventiveMaintenanceDto.getAssetScheduleId());
			mstr.setPmsType(preventiveMaintenanceDto.getPmsType());
			mstr.setTicketNo(preventiveMaintenanceDto.getTicketNo());
			mstr.setPmsDetails(preventiveMaintenanceDto.getPmsDetails() != null ? preventiveMaintenanceDto.getPmsDetails() : "");
			mstr.setPmsRemarks(preventiveMaintenanceDto.getPmsRemarks() != null ? preventiveMaintenanceDto.getPmsRemarks() : "");
			
			if(preventiveMaintenanceDto.getWorkOrderStatus() != null && preventiveMaintenanceDto.getWorkOrderStatus() == 'C') {
				mstr.setCompletionDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCompletionDate(),"dd-M-yyyy"));
				mstr.setCompletionTime((preventiveMaintenanceDto.getCompletionTime()));
			}
			
			if(preventiveMaintenanceDto.getWorkOrderStatus() != null && preventiveMaintenanceDto.getWorkOrderStatus() == 'O') {
				mstr.setUnderObservationDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getUnderObservationDate(),"dd-M-yyyy"));
				mstr.setUnderObservationTime((preventiveMaintenanceDto.getUnderObservationTime()));
			}
			
			mstr.setWorkOrderStatus(preventiveMaintenanceDto.getWorkOrderStatus());
			mstr.setStatus('A');
			mstr.setUnitId(preventiveMaintenanceDto.getUnitId());
			mstr.setOrganizationId(preventiveMaintenanceDto.getOrganizationId());
			sessionFactory.getCurrentSession().saveOrUpdate(mstr);
			PmsDetailsId = mstr.getPmsDetailsId();
			mstr.setPmsDetailsId(PmsDetailsId);
				
			//save or update Spare (Slave)
			if(preventiveMaintenanceDto.getGetSpareConsumptionList()!=null && preventiveMaintenanceDto.getGetSpareConsumptionList().size()!=0) {
				for(SpareConsumptionDto spare : preventiveMaintenanceDto.getGetSpareConsumptionList()){
					try {
							SpareConsumption slave;
							if(spare.getSpareConsumptionId()!=0 ) {
								slave = (SpareConsumption) sessionFactory.getCurrentSession().get(SpareConsumption.class, spare.getSpareConsumptionId());
							}else {
								slave = new SpareConsumption(); 
								slave.setPmsDetailsId(PmsDetailsId);
								slave.setCreatedBy(preventiveMaintenanceDto.getCreatedBy());
								slave.setCreatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
							}
							//slave.setSpareConsumptionId(preventiveMaintenanceDto.getSpareConsumptionId());
							
							slave.setAssetId(spare.getAssetId());
							slave.setAssetQty(spare.getAssetQty());
							slave.setStoreId(spare.getStoreId());
							slave.setSerialNo(spare.getSerialNo());
							slave.setRemark(spare.getRemark());
							slave.setStatus('A');
							slave.setUnitId(preventiveMaintenanceDto.getUnitId());
							slave.setOrganizationId(preventiveMaintenanceDto.getOrganizationId());
							sessionFactory.getCurrentSession().saveOrUpdate(slave);

					}catch (Exception e) {
						logger.error("saveOrUpdatePMSDetails (spare consumption) failed", e);
						throw new ServiceException(e.getMessage());
					}
				}
			}
			
			//save or update Checklist (Slave)
			try {
				for(AssetScheduleChecklistDto dto : preventiveMaintenanceDto.getGetChecklistData()){
					AssetScheduleChecklist check ;
						if(dto.getAssetScheduleChecklistId()!=null && dto.getAssetScheduleChecklistId()!=0){
							check = (AssetScheduleChecklist) sessionFactory.getCurrentSession().get(AssetScheduleChecklist.class, dto.getAssetScheduleChecklistId());
							check.setUpdatedBy(preventiveMaintenanceDto.getCreatedBy());
							check.setUpdatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
						}else {
							check = new AssetScheduleChecklist();
							check.setCreatedBy(preventiveMaintenanceDto.getCreatedBy());
							check.setCreatedDate(GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
						}
						check.setChecklistNameId(dto.getChecklistNameId());
						check.setChecklistName(dto.getChecklistName());
						check.setChecklistCode(dto.getChecklistCode());
						check.setAssetScheduleId(dto.getAssetScheduleId());	
						check.setStatus('A');
						check.setUnitId(preventiveMaintenanceDto.getUnitId());
						check.setOrganizationId(preventiveMaintenanceDto.getOrganizationId());
					sessionFactory.getCurrentSession().saveOrUpdate(check);
				}
			}catch (Exception e) {
				logger.error("saveOrUpdatePMSDetails (checklist) failed", e);
				throw new ServiceException(e.getMessage());
			}
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
		} catch (HibernateException he) {
			logger.error("saveOrUpdatePMSDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveOrUpdatePMSDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveAssignDetails(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
			String hqlUpdate = "update AssetSchedule asset set asset.assignTo=:assignTo,asset.assignTime=:assignTime,asset.assignRemark=:assignRemark,asset.processStatus=:processStatus,asset.assignOn=:assignOn where asset.assetScheduleId=:assetScheduleId ";
			
			Query qry = sessionFactory.getCurrentSession().createQuery( hqlUpdate )
				.setParameter( "assignTo", preventiveMaintenanceDto.getAssignTo())
				.setParameter("assignTime", preventiveMaintenanceDto.getAssignTime())
				.setParameter("assignRemark", preventiveMaintenanceDto.getAssignRemark())
				.setParameter("processStatus", 3)
				.setParameter("assignOn", GlobalCommonDateUtils.getDate(preventiveMaintenanceDto.getAssignOn(),"dd-M-yyyy"))
				.setParameter("assetScheduleId", preventiveMaintenanceDto.getAssetScheduleId());
				qry.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("saveAssignDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveAssignDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveOrUpdateSpareConsumption(SpareConsumptionDto dto)throws ApplicationException{
		try {
				SpareConsumption mstr = new SpareConsumption(); 
				
				mstr.setSpareConsumptionId(dto.getSpareConsumptionId());
				mstr.setPmsDetailsId(dto.getPmsDetailsId());
				mstr.setAssetId(dto.getAssetId());
				mstr.setAssetQty(dto.getAssetQty());
				mstr.setStoreId(dto.getStoreId());
				mstr.setSerialNo(dto.getSerialNo());
				mstr.setRemark(dto.getRemark());
				mstr.setStatus('A');
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
				mstr.setUnitId(dto.getUnitId());
				mstr.setOrganizationId(dto.getOrganizationId());
				
				sessionFactory.getCurrentSession().save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			} catch (HibernateException he) {
				logger.error("saveOrUpdateSpareConsumption failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveOrUpdateSpareConsumption failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response fetchPMSByPMSId(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			PMSDetailsDto object =  (PMSDetailsDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PMS_DETAILS_BY_PMS_ID")
					.setParameter("pmsDetailsId", preventiveMaintenanceDto.getPmsDetailsId())
					.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PMSDetailsDto.class))
					.uniqueResult();
			if(object.getPmsDetailsId()!=null && object.getPmsDetailsId()!=0) {
				List<SpareConsumptionDto> list =  sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT spare.spare_consumption_id as \"spareConsumptionId\", spare.asset_id as \"assetId\", item.code as \"assetCode\", "
								+ "spare.serial_no as \"serialNo\", spare.asset_qty as \"assetQty\", spare.remark as \"remark\", item.name as \"assetName\", "
								+ "spare.store_id as \"storeId\" FROM equipment.m_spare_consumption spare left join public.m_item item on item.id = spare.asset_id "
								+ "WHERE spare.status='A' AND spare.pms_details_id=:pmsDetailsId AND spare.unit_id=:unitId AND spare.organization_id=:organizationId")
						.setParameter("pmsDetailsId", preventiveMaintenanceDto.getPmsDetailsId())
						.setParameter("unitId", preventiveMaintenanceDto.getUnitId())
						.setParameter("organizationId", preventiveMaintenanceDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(SpareConsumptionDto.class)).list();
				object.setGetSpareConsumptionList(list);
			}
			return new Response(SUCCESS, null, null, null, object);
		}catch (HibernateException he) {
			logger.error("fetchPMSByPMSId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("fetchPMSByPMSId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response deleteSpareById(PreventiveMaintenanceDto dto) throws ApplicationException{
		try {
			String hqlUpdate = "update SpareConsumption spare set spare.status='I',spare.updatedBy=:updatedBy,spare.updatedDate=:updatedDate where spare.spareConsumptionId=:spareConsumptionId ";
			Query qry = sessionFactory.getCurrentSession().createQuery(hqlUpdate)
			        .setParameter("updatedBy", dto.getCreatedBy())
			        .setParameter("updatedDate", GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"))
			        .setParameter("spareConsumptionId", dto.getSpareConsumptionId());
			qry.executeUpdate();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			} catch (HibernateException he) {
				logger.error("deleteSpareById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("deleteSpareById failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


}
