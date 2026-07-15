package com.param.assetManagement.maintenance.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.assetManagement.maintenance.dto.InsuranceDto;
import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceWorklist;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class CorrectiveMaintenanceWorklistDaoImpl extends GenericDao<CorrectiveMaintenanceWorklist, Integer> implements ICorrectiveMaintenanceWorklistDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceWorklistDaoImpl.class);

	public CorrectiveMaintenanceWorklistDaoImpl() {
		super(CorrectiveMaintenanceWorklist.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	
	@Override
	public Response saveCorrectiveMaintenanceWorklist(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto)
			throws ApplicationException {
		try {
			LocalTime time1 = LocalTime.parse(correctiveMaintenanceWorklistDto.getScheduleTime());
			LocalDate today = LocalDate.now();
			Instant scheduleTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			CorrectiveMaintenanceWorklist mstr = new CorrectiveMaintenanceWorklist();
			mstr.setCorrectiveMaintenanceId(correctiveMaintenanceWorklistDto.getCorrectiveMaintenanceId());
			mstr.setAssignTo(correctiveMaintenanceWorklistDto.getAssignTo());
			mstr.setMaintenanceWorklistStatusId(correctiveMaintenanceWorklistDto.getMaintenanceWorklistStatusId());
			mstr.setScheduleTime(scheduleTime);
			mstr.setCreatedBy(correctiveMaintenanceWorklistDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceWorklistDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
			mstr.setScheduleDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceWorklistDto.getScheduleDate(),"dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(correctiveMaintenanceWorklistDto.getOrganizationId());
			mstr.setAllocationRemark(correctiveMaintenanceWorklistDto.getAllocationRemark());
			mstr.setStatus(correctiveMaintenanceWorklistDto.getStatus());
			mstr.setUnitId(correctiveMaintenanceWorklistDto.getUnitId());
			mstr.setUpdatedBy(correctiveMaintenanceWorklistDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceWorklistDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			if(correctiveMaintenanceWorklistDto.getCorrectiveMaintenanceWorklistId() == null) {
				
				sessionFactory.getCurrentSession().save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
				
			}else {
				
				mstr.setCorrectiveMaintenanceWorklistId(correctiveMaintenanceWorklistDto.getCorrectiveMaintenanceWorklistId());
				sessionFactory.getCurrentSession().update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}

			} catch (HibernateException he) {
				logger.error("Error saving corrective maintenance worklist", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("Error saving corrective maintenance worklist", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response getCorrectiveMaintenanceWorklistListByMultipleCriteria(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORRECTIVE_MAINTENANCE_WORKLIST_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			if(correctiveMaintenanceWorklistDto.getFromDate()!=null && correctiveMaintenanceWorklistDto.getToDate()!=null){
				subQuery.append(" AND to_char(mstr.createdDate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			}
			
			if(correctiveMaintenanceWorklistDto.getMaintenanceWorklistStatusId()!=0){
				subQuery.append(" AND correctiveMaintenance.maintenanceStatusId =:maintenanceScheduleStatusId ");
			}
			
            if(correctiveMaintenanceWorklistDto.getMaintenanceWorklistStatusId() == 0){
                subQuery.append(" AND correctiveMaintenance.maintenanceStatusId in (2,3,4,7) ");
            }

			
			if(correctiveMaintenanceWorklistDto.getRequestCriticalityId()!=0){
				subQuery.append(" AND correctiveMaintenance.requestCriticalityId =:requestCriticalityId ");
			}
			
			if(correctiveMaintenanceWorklistDto.getIsWorklist() == 3) {
				if(correctiveMaintenanceWorklistDto.getStoreId()!=0) {
				subQuery.append(" AND correctiveMaintenance.storeId =:storeId ");
				}
				subQuery.append(" AND mstr.assignTo =:assignTo ");
				subQuery.append(" AND mstr.exportedToScheduling = false");
			}
			
			subQuery.append(" order by mstr.correctiveMaintenanceWorklistId desc");
			query = query.concat(subQuery.toString());
			Query createQuery = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("unitId", correctiveMaintenanceWorklistDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceWorklistDto.getOrganizationId());


			if(correctiveMaintenanceWorklistDto.getFromDate()!=null && correctiveMaintenanceWorklistDto.getToDate()!=null){
				createQuery.setParameter("fromDate", correctiveMaintenanceWorklistDto.getFromDate());
				createQuery.setParameter("toDate", correctiveMaintenanceWorklistDto.getToDate());
			}

			if(correctiveMaintenanceWorklistDto.getMaintenanceWorklistStatusId()!=0){
				createQuery.setParameter("maintenanceScheduleStatusId", correctiveMaintenanceWorklistDto.getMaintenanceWorklistStatusId());
			}


			if(correctiveMaintenanceWorklistDto.getRequestCriticalityId()!=0){
				createQuery.setParameter("requestCriticalityId ",correctiveMaintenanceWorklistDto.getRequestCriticalityId());
			}


			if(correctiveMaintenanceWorklistDto.getIsWorklist() == 3) {
				if(correctiveMaintenanceWorklistDto.getStoreId()!=0) {
					createQuery.setParameter("storeId ",correctiveMaintenanceWorklistDto.getStoreId());
				}

				createQuery.setParameter("assignTo ",correctiveMaintenanceWorklistDto.getAssignTo());
			}

			List<CorrectiveMaintenanceWorklistDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceWorklistDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("Error fetching corrective maintenance worklist list by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response cancelCorrectiveMaintenanceRequestFromWorklist(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			Date updatedDt =GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm");
			Integer count = sessionFactory.getCurrentSession().createSQLQuery("update assets.t_corrective_maintenance_worklist set maintenance_worklist_status_id ='"+correctiveMaintenanceDto.getMaintenanceStatusId()+
					"' , updated_by='"+correctiveMaintenanceDto.getUpdatedBy()+
					"' , updated_date='"+updatedDt+"' where corrective_maintenance_id='"+correctiveMaintenanceDto.getCorrectiveMaintenanceId()+"'")
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		}catch (Exception e) {
			logger.error("Error cancelling corrective maintenance request from worklist", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateExportedToScheduling(CorrectiveMaintenanceWorklistDto dto) throws ApplicationException {
	try {
			
		CorrectiveMaintenanceWorklist mstr = findById(dto.getCorrectiveMaintenanceWorklistId());
		mstr.setExportedToScheduling(true);
		mstr.setMaintenanceWorklistStatusId(dto.getMaintenanceWorklistStatusId());
		mstr.setUpdatedBy(dto.getUpdatedBy());
		mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
		sessionFactory.getCurrentSession().update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
			logger.error("Error updating exported to scheduling flag", e);
			throw new ServiceException(e.getMessage());
		}
	}

    @Override
    public Response getManufacturerDetailsByItemId(CorrectiveMaintenanceDto correctiveMaintenanceDto)
            throws ApplicationException {
        try{
            List<VendorDto> list = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_MANUFACTURER_DETAILS_BY_ITEM_ID")
                    .setParameter("itemId", correctiveMaintenanceDto.getItemId())
                   // .setParameter("organizationId", correctiveMaintenanceDto.getOrganizationId())
                  //  .setParameter("unitId", correctiveMaintenanceDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(VendorDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        }catch(Exception e){
            
            throw e;
        }
    }


    @Override
    public Response getVendorDetailsByItemId(CorrectiveMaintenanceDto correctiveMaintenanceDto)
            throws ApplicationException {
        try{
            List<VendorDto> list = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_VENDOR_LIST_BY_ITEM_ID")
                    .setParameter("itemId", correctiveMaintenanceDto.getItemId())
                    .setBigInteger("batchId", correctiveMaintenanceDto.getBatchId())
                   // .setParameter("unitId", correctiveMaintenanceDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(VendorDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        }catch(Exception e){
            
            throw e;
        }
    }


    @Override
    public Response getAccessoryDetailsByItemId(CorrectiveMaintenanceDto correctiveMaintenanceDto)
            throws ApplicationException {
        try{
            List<CorrectiveMaintenanceDto> list = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_ACCESSORY_DETAILS_BY_ITEM_ID")
                    .setParameter("itemId", correctiveMaintenanceDto.getItemId())
                    .setBigInteger("batchId", correctiveMaintenanceDto.getBatchId())
                   // .setParameter("organizationId", correctiveMaintenanceDto.getOrganizationId())
                  //  .setParameter("unitId", correctiveMaintenanceDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        }catch(Exception e){
            
            throw e;
        }
    }


	@Override
	public Response getInsuranceDetailsByItemId(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		 try{
	            List<InsuranceDto> list = sessionFactory.getCurrentSession()
	                    .getNamedQuery("GET_INSURANCE_DETAILS_BY_ITEM_ID")
	                    .setParameter("itemId", correctiveMaintenanceDto.getItemId())
	                    .setBigInteger("batchId", correctiveMaintenanceDto.getBatchId())
	                    .setResultTransformer(Transformers.aliasToBean(InsuranceDto.class)).list();
	            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	        }catch(Exception e){
	            
	            throw e;
	        }
	}

}
