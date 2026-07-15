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

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenance;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class CorrectiveMaintenanceDaoImpl extends GenericDao<CorrectiveMaintenance, Integer> implements ICorrectiveMaintenanceDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceDaoImpl.class);

	public CorrectiveMaintenanceDaoImpl() {
		super(CorrectiveMaintenance.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCorrectiveMaintenance(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			
			if(correctiveMaintenanceDto.getCorrectiveMaintenanceId() != null) {	
				   
				CorrectiveMaintenance mstr = findById(correctiveMaintenanceDto.getCorrectiveMaintenanceId());
				Instant downTime = null;
				if(correctiveMaintenanceDto.getIsDown() == true) {
					LocalTime time1 = LocalTime.parse(correctiveMaintenanceDto.getDownTime());
					LocalDate today = LocalDate.now();
					downTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					 mstr.setDownDateTime(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getDownDateTime(),"dd-M-yyyy HH:ss:mm"));
				}else {
					 mstr.setDownDateTime(null);
				}
				
				if(correctiveMaintenanceDto.getReferenceId()!=null && correctiveMaintenanceDto.getReferenceLogNumber()!=null) {
					mstr.setReferenceId(correctiveMaintenanceDto.getReferenceId());
					mstr.setReferenceLogNumber(correctiveMaintenanceDto.getReferenceLogNumber());
				}
				LocalTime time1 = LocalTime.parse(correctiveMaintenanceDto.getResolutionExpectedTime());
				LocalDate today = LocalDate.now();
				Instant resolutionExpectedTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				mstr.setIsDown(correctiveMaintenanceDto.getIsDown());
				mstr.setBatchId(correctiveMaintenanceDto.getBatchId());
				mstr.setRemark(correctiveMaintenanceDto.getRemark());
				mstr.setRequestDetails(correctiveMaintenanceDto.getRequestDetails());
				mstr.setResolutionExpectedDateTime(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getResolutionExpectedDateTime(),"dd-M-yyyy HH:ss:mm"));
				mstr.setSerialNumber(correctiveMaintenanceDto.getSerialNumber());
				mstr.setDownTime(downTime);
				mstr.setResolutionExpectedTime(resolutionExpectedTime);
				mstr.setUpdatedBy(correctiveMaintenanceDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
				sessionFactory.getCurrentSession().update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, mstr);
			}else {
					
					CorrectiveMaintenance mstr = new CorrectiveMaintenance();

				Instant downTime = null;
					if(correctiveMaintenanceDto.getIsDown() == true) {
						LocalTime time1 = LocalTime.parse(correctiveMaintenanceDto.getDownTime());
						LocalDate today = LocalDate.now();
						downTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

						 mstr.setDownDateTime(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getDownDateTime(),"dd-M-yyyy HH:ss:mm"));
					}else {
						 mstr.setDownDateTime(null);
					}
					if(correctiveMaintenanceDto.getReferenceId()!=null && correctiveMaintenanceDto.getReferenceLogNumber()!=null) {
						mstr.setReferenceId(correctiveMaintenanceDto.getReferenceId());
						mstr.setReferenceLogNumber(correctiveMaintenanceDto.getReferenceLogNumber());
					}

					LocalTime time1 = LocalTime.parse(correctiveMaintenanceDto.getResolutionExpectedTime());
					LocalDate today = LocalDate.now();
					Instant resolutionExpectedTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					mstr.setCallLogNumber(correctiveMaintenanceDto.getCallLogNumber());
					mstr.setCreatedBy(correctiveMaintenanceDto.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
					mstr.setIsDown(correctiveMaintenanceDto.getIsDown());
					mstr.setItemId(correctiveMaintenanceDto.getItemId());
					mstr.setMaintenanceStatusId(correctiveMaintenanceDto.getMaintenanceStatusId());
					mstr.setOrganizationId(correctiveMaintenanceDto.getOrganizationId());
					mstr.setRemark(correctiveMaintenanceDto.getRemark());
					mstr.setRequestCriticalityId(correctiveMaintenanceDto.getRequestCriticalityId());
					mstr.setRequestDateTime(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getRequestDateTime(),"dd-M-yyyy HH:ss:mm"));
					mstr.setRequestDetails(correctiveMaintenanceDto.getRequestDetails());
					mstr.setRequestTypeId(correctiveMaintenanceDto.getRequestTypeId());
					mstr.setResolutionExpectedDateTime(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getResolutionExpectedDateTime(),"dd-M-yyyy HH:ss:mm"));
					mstr.setSerialNumber(correctiveMaintenanceDto.getSerialNumber());
					mstr.setBatchId(correctiveMaintenanceDto.getBatchId());
					mstr.setStatus(correctiveMaintenanceDto.getStatus());
					mstr.setStoreId(correctiveMaintenanceDto.getStoreId());
					mstr.setDownTime(downTime);
					mstr.setResolutionExpectedTime(resolutionExpectedTime);
					mstr.setUnitId(correctiveMaintenanceDto.getUnitId());
					mstr.setUpdatedBy(correctiveMaintenanceDto.getUpdatedBy());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
					sessionFactory.getCurrentSession().save(mstr);
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}
			} catch (HibernateException he) {
				logger.error("Error while saving corrective maintenance", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("Error while saving corrective maintenance", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getCorrectiveMaintenanceListByMultipleCriteria(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORRECTIVE_MAINTENANCE_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			if(correctiveMaintenanceDto.getFromDate()!=null && correctiveMaintenanceDto.getToDate()!=null){
				subQuery.append(" AND to_char(mstr.requestDateTime, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			}
			
			if(correctiveMaintenanceDto.getMaintenanceStatusId()!=0){
				subQuery.append(" AND mstr.maintenanceStatusId =:maintenanceStatusId ");
			}
			
			if(correctiveMaintenanceDto.getStoreId()!=0 && correctiveMaintenanceDto.getStoreId()!=null){
				subQuery.append(" AND mstr.storeId = "+correctiveMaintenanceDto.getStoreId());
			}
		
			if(correctiveMaintenanceDto.getIsWorklist() == 1) {
				
				if(correctiveMaintenanceDto.getRequestCriticalityId()!=0){
					subQuery.append(" AND mstr.requestCriticalityId = :requestCriticalityId ");
				}
				
				subQuery.append(" AND mstr.maintenanceStatusId NOT IN (5,6) ");
				subQuery.append(" AND mstr.exportedToWorklist = false");
			}else {
				subQuery.append(" AND mstr.createdBy =:createdBy ");
			}
			subQuery.append(" order by mstr.correctiveMaintenanceId desc");
			query = query.concat(subQuery.toString());
			Query createQuery = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("unitId", correctiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceDto.getOrganizationId());

			if(correctiveMaintenanceDto.getFromDate()!=null && correctiveMaintenanceDto.getToDate()!=null){
				createQuery.setParameter("fromDate", correctiveMaintenanceDto.getFromDate());
				createQuery.setParameter("toDate", correctiveMaintenanceDto.getToDate());
			}

			if(correctiveMaintenanceDto.getMaintenanceStatusId()!=0){
				createQuery.setParameter("maintenanceStatusId", correctiveMaintenanceDto.getMaintenanceStatusId());
			}

			if(correctiveMaintenanceDto.getStoreId()!=0 && correctiveMaintenanceDto.getStoreId()!=null){
				createQuery.setParameter("storeId", correctiveMaintenanceDto.getStoreId());
			}

			if(correctiveMaintenanceDto.getIsWorklist() == 1) {

				if(correctiveMaintenanceDto.getRequestCriticalityId()!=0){
					createQuery.setParameter("requestCriticalityId", correctiveMaintenanceDto.getRequestCriticalityId());
				}

			}else {
				createQuery.setParameter("createdBy", correctiveMaintenanceDto.getCreatedBy());

			}

			List<CorrectiveMaintenanceDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceDto.class)).list();


			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("Error while getting corrective maintenance list by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getSerialNoByItemId(Integer itemId, Integer unitId, Integer organizationId)
			throws ApplicationException {
		try {
			List<CorrectiveMaintenanceDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("select batch_no as \"serialNumber\" , id as \"batchId\" from inventory.t_batch  where item_id =:itemId and unit_id=:unitId and  organization_id =:organizationId and is_deleted=false")
					.setParameter("unitId", unitId)
					.setParameter("organizationId", organizationId)
					.setParameter("itemId", itemId)
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("Error while getting serial number by item id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response ExportedToCorrectiveMaintenanceWorklist(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			
			CorrectiveMaintenance mstr = findById(correctiveMaintenanceDto.getCorrectiveMaintenanceId());
			mstr.setExportedToWorklist(true);
			mstr.setMaintenanceStatusId(correctiveMaintenanceDto.getMaintenanceStatusId());
			sessionFactory.getCurrentSession().update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
			logger.error("Error while exporting corrective maintenance to worklist", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response CorrectiveMaintenanceRequestCompleteByRequester(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
						LocalTime time1 = LocalTime.parse(correctiveMaintenanceDto.getCompletionTime());
						LocalDate today = LocalDate.now();
						Instant CompletionTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

						CorrectiveMaintenance mstr = findById(correctiveMaintenanceDto.getCorrectiveMaintenanceId());
						mstr.setCompletionDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getCompletionDate(),"dd-M-yyyy HH:ss:mm"));
						mstr.setCompletionTime(CompletionTime);
						mstr.setCompletionRemak(correctiveMaintenanceDto.getCompletionRemak());
						mstr.setIsComplete(correctiveMaintenanceDto.getIsComplete());
						mstr.setUpdatedBy(correctiveMaintenanceDto.getUpdatedBy());
						mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
						sessionFactory.getCurrentSession().update(mstr);
						
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}catch (Exception e) {
						logger.error("Error while completing corrective maintenance request by requester", e);
						throw new ServiceException(e.getMessage());
					}
	}

	@Override
	public Response updateCorrectiveMaintenancePendingToNewRequest(CorrectiveMaintenanceDto dto)
			throws ApplicationException {
	try {
			CorrectiveMaintenance mstr = findById(dto.getCorrectiveMaintenanceId());
			mstr.setMaintenanceStatusId(dto.getMaintenanceStatusId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			sessionFactory.getCurrentSession().update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (Exception e) {
			logger.error("Error while updating corrective maintenance pending to new request", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelCorrectiveMaintenanceRequest(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			CorrectiveMaintenance mstr = findById(correctiveMaintenanceDto.getCorrectiveMaintenanceId());
			mstr.setMaintenanceStatusId(correctiveMaintenanceDto.getMaintenanceStatusId()); //for cancel
			mstr.setUpdatedBy(correctiveMaintenanceDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			sessionFactory.getCurrentSession().update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, "cancelled successfully", null, null);
		}catch (Exception e) {
			logger.error("Error while cancelling corrective maintenance request", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkForUniqueItemRequest(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			CorrectiveMaintenanceDto dto = (CorrectiveMaintenanceDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_ITEM_REQUEST")
					.setParameter("unitId", correctiveMaintenanceDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceDto.getOrganizationId())
					.setParameter("itemId", correctiveMaintenanceDto.getItemId())
					.setBigInteger("batchId", correctiveMaintenanceDto.getBatchId())
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceDto.class)).uniqueResult();
			
			return new Response(ERROR, ERROR_CODE, ALREADY_EXIST, null, dto);
		}catch (Exception e) {
			logger.error("Error while checking for unique item request", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateReopen(Integer referenceId) throws ApplicationException {
		try {
			Integer count = sessionFactory.getCurrentSession().createSQLQuery("update assets.t_corrective_maintenance set re_open =true"
					+" where corrective_maintenance_id="+referenceId+" ")
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		}catch (Exception e) {
			logger.error("Error while updating reopen for corrective maintenance", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
