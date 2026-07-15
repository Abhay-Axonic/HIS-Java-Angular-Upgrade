package com.param.assetManagement.maintenance.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceScheduleDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceSchedule;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class CorrectiveMaintenanceScheduleDaoImpl extends GenericDao<CorrectiveMaintenanceSchedule, Integer> implements ICorrectiveMaintenanceScheduleDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceScheduleDaoImpl.class);

	public CorrectiveMaintenanceScheduleDaoImpl() {
		super(CorrectiveMaintenanceSchedule.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCorrectiveMaintenanceSchedule(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto)
			throws ApplicationException {
		try {
					CorrectiveMaintenanceSchedule mstr = new CorrectiveMaintenanceSchedule(); 
					if(correctiveMaintenanceScheduleDto.getAssetUpTime()!=null) {
						LocalTime assetUpTime1 = LocalTime.parse(correctiveMaintenanceScheduleDto.getAssetUpTime());
						LocalDate today = LocalDate.now();
						Instant assetUpTime = assetUpTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

						mstr.setAssetUpTime(assetUpTime);
					}
					mstr.setCorrectiveMaintenanceWorklistId(correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceWorklistId());
					mstr.setCreatedBy(correctiveMaintenanceScheduleDto.getCreatedBy());
					mstr.setAssetUpDate(correctiveMaintenanceScheduleDto.getAssetUpDate()!=null?GlobalCommonDateUtils.getDate(correctiveMaintenanceScheduleDto.getAssetUpDate(),"dd-M-yyyy HH:ss:mm"):null);
					mstr.setCompletionDate(correctiveMaintenanceScheduleDto.getCompletionDate()!=null?GlobalCommonDateUtils.getDate(correctiveMaintenanceScheduleDto.getCompletionDate(),"dd-M-yyyy HH:ss:mm"):null);
					mstr.setFromDate(correctiveMaintenanceScheduleDto.getFromDate()!=null?GlobalCommonDateUtils.getDate(correctiveMaintenanceScheduleDto.getFromDate(),"dd-M-yyyy HH:ss:mm"):null);
					mstr.setIsCompleted(correctiveMaintenanceScheduleDto.getIsCompleted());
					mstr.setIsStart(correctiveMaintenanceScheduleDto.getIsStart());
					mstr.setIsUnderObservation(correctiveMaintenanceScheduleDto.getIsUnderObservation());
					mstr.setMaintenanceScheduleStatusId(correctiveMaintenanceScheduleDto.getMaintenanceScheduleStatusId());
					mstr.setRequestAllocationTypeId(correctiveMaintenanceScheduleDto.getRequestAllocationTypeId());
					mstr.setResolutionDetails(correctiveMaintenanceScheduleDto.getResolutionDetails());
					mstr.setTicketNo(correctiveMaintenanceScheduleDto.getTicketNo());
					mstr.setVendorId(correctiveMaintenanceScheduleDto.getVendorId()!=null ? correctiveMaintenanceScheduleDto.getVendorId(): null);
					mstr.setToDate(correctiveMaintenanceScheduleDto.getToDate()!=null?GlobalCommonDateUtils.getDate(correctiveMaintenanceScheduleDto.getToDate(),"dd-M-yyyy HH:ss:mm"):null);
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceScheduleDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(correctiveMaintenanceScheduleDto.getOrganizationId());
					mstr.setRemark(correctiveMaintenanceScheduleDto.getRemark());
					mstr.setStatus(correctiveMaintenanceScheduleDto.getStatus());
					mstr.setUnitId(correctiveMaintenanceScheduleDto.getUnitId());
					mstr.setUpdatedBy(correctiveMaintenanceScheduleDto.getUpdatedBy());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceScheduleDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
				
					if(correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceScheduleId()!=null) {
						mstr.setCorrectiveMaintenanceScheduleId(correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceScheduleId());
						sessionFactory.getCurrentSession().update(mstr);
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
					}else {
					sessionFactory.getCurrentSession().save(mstr);
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
					}
					
			
			} catch (HibernateException he) {
				logger.error("Error while saving corrective maintenance schedule", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("Error while saving corrective maintenance schedule", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getCorrectiveMaintenanceScheduleByMultipleCrit(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORRECTIVE_MAINTENANCE_SCHEDULING_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			if(correctiveMaintenanceScheduleDto.getFromDate()!=null && correctiveMaintenanceScheduleDto.getToDate()!=null){
				subQuery.append(" AND to_char(correctiveMaintenance.requestDateTime, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			}
			
			if(correctiveMaintenanceScheduleDto.getMaintenanceScheduleStatusId()!=0){
				subQuery.append(" AND mstr.maintenanceScheduleStatusId =:maintenanceScheduleStatusId ");
			}
			
			if(correctiveMaintenanceScheduleDto.getStoreId()!=0){
				subQuery.append(" AND correctiveMaintenance.storeId =:storeId ");
			}
			if(correctiveMaintenanceScheduleDto.getRequestCriticalityId()!=0){
					subQuery.append(" AND correctiveMaintenance.requestCriticalityId =:requestCriticalityId ");
			}
			
			//subQuery.append(" AND correctiveMaintenanceWorklist.exportedToScheduling = true");
			subQuery.append(" order by mstr.correctiveMaintenanceScheduleId desc");
			query = query.concat(subQuery.toString());
			Query createQuery = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("unitId", correctiveMaintenanceScheduleDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceScheduleDto.getOrganizationId());

			if(correctiveMaintenanceScheduleDto.getFromDate()!=null && correctiveMaintenanceScheduleDto.getToDate()!=null){
				createQuery.setParameter("fromDate", correctiveMaintenanceScheduleDto.getFromDate());
				createQuery.setParameter("toDate", correctiveMaintenanceScheduleDto.getToDate());
			}

			if(correctiveMaintenanceScheduleDto.getMaintenanceScheduleStatusId()!=0){
				createQuery.setParameter("maintenanceScheduleStatusId", correctiveMaintenanceScheduleDto.getMaintenanceScheduleStatusId());
			}

			if(correctiveMaintenanceScheduleDto.getStoreId()!=0){
				createQuery.setParameter("storeId", correctiveMaintenanceScheduleDto.getStoreId());

			}

			if(correctiveMaintenanceScheduleDto.getRequestCriticalityId()!=0){
				createQuery.setParameter("requestCriticalityId", correctiveMaintenanceScheduleDto.getRequestCriticalityId());

			}

			List<CorrectiveMaintenanceScheduleDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceScheduleDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("Error while fetching corrective maintenance schedule by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getvendorlistByBatchId(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto)
			throws ApplicationException {
		try {
			CorrectiveMaintenanceWorklistDto dto = (CorrectiveMaintenanceWorklistDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select vendor.id as \"vendorId\" , vendor.name  as \"vendorName\" from inventory.t_batch batch " + 
							"inner join public.m_vendor vendor on batch.service_provider_id = vendor.id where batch.id =:batchId and  batch.unit_id=:unitId and  batch.organization_id =:organizationId and batch.is_deleted=false")
					.setParameter("unitId", correctiveMaintenanceWorklistDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceWorklistDto.getOrganizationId())
					.setBigInteger("batchId", correctiveMaintenanceWorklistDto.getBatchId())
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceWorklistDto.class)).uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dto);

		}catch (Exception e) {
			logger.error("Error while fetching vendor list by batch id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getdetailsOfCM(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto)
			throws ApplicationException {
try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORRECTIVE_MAINTENANCE_SCHEDULING_LIST_BY_CMS_ID")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();

			subQuery.append(" order by mstr.correctiveMaintenanceScheduleId desc");
			query = query.concat(subQuery.toString());
			CorrectiveMaintenanceScheduleDto obj = (CorrectiveMaintenanceScheduleDto) sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("unitId", correctiveMaintenanceScheduleDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceScheduleDto.getOrganizationId())
					.setParameter("correctiveMaintenanceScheId", correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceScheduleId())
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceScheduleDto.class)).uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, obj);
		}catch (Exception e) {
			logger.error("Error while fetching details of corrective maintenance", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*@Override
	public Response searchFixedItemListByCodeOrName(CorrectiveMaintenanceSparesComsumptionDto dto)
			throws ApplicationException {
		try {
			List<CorrectiveMaintenanceSparesComsumptionDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("select distinct ON(item.id)item.id as \"itemId\", "
						//	+" assc.id as \"assetCategoryId\",assc.category as \"assetCategory\",pc.id as \"productCategoryId\",pc.category as \"productCategory\", "  
							+" item.code as \"itemCode\", item.name as \"itemName\" "
							+" from public.m_item item " 
							+" inner join public.m_product_category  pc on item.product_category_id = pc.id " 
							+" inner join public.m_asset_category assc on pc.asset_category_id = assc.id " 
							+" inner join public.m_asset_type asst on asst.id = assc.asset_type_id " 
							+" where item.is_active=true and item.is_deleted= false and item.unit_id=:unitId and item.organization_id =:organizationId" 
							+" and asst.id =2 and item.id!= :itemId and (item.code like :itemCode or item.name like :itemName) ")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("itemId", dto.getItemId())
					.setParameter("itemCode", dto.getItemCode()!=null ? "%"+dto.getItemCode().toLowerCase()+"%" : "")
					.setParameter("itemName", dto.getItemName()!=null ? "%"+dto.getItemName().toLowerCase()+"%" : "")
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceWorklistDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
			
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
}
