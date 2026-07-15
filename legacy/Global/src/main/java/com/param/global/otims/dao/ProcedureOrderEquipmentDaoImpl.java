package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderEquipmentDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.otims.model.ProcedureOrderEquipment;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class ProcedureOrderEquipmentDaoImpl extends GenericDao<ProcedureOrderEquipment, Integer>implements IProcedureOrderEquipmentDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureOrderEquipmentDaoImpl.class);

	
	public ProcedureOrderEquipmentDaoImpl() {
		super(ProcedureOrderEquipment.class);
	}
	
	@Override
	public Response saveProcedureOrderEquipment(List<ProcedureOrderEquipmentDto> procedureOrderEquipmentList,Integer proReqId,
			Integer procedureOrderId) throws ApplicationException {
		try{
			
			for (ProcedureOrderEquipmentDto dto : procedureOrderEquipmentList) {
				ProcedureOrderEquipment mstr = new ProcedureOrderEquipment();
				mstr.setProcedureOrderId(procedureOrderId);
				mstr.setProcedureRequestId(proReqId);
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setItemId(dto.getItemId());
				mstr.setQty(dto.getQty());
				mstr.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				mstr.setOrganizationId(dto.getOrganizationId());
				mstr.setUnitId(dto.getUnitId());
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				if(dto.getProcedureOrderEquipmentId()!=null) {
					mstr.setProcedureOrderEquipmentId(dto.getProcedureOrderEquipmentId());
					mstr.setStatus(dto.getStatus());
					sessionFactory.getCurrentSession().update(mstr);
				}else {
					mstr.setStatus('A');
					sessionFactory.getCurrentSession().save(mstr);
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getEquipmentDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException {
		try{
			List<ProcedureOrderEquipmentDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ORDER_EQUIPMENT_BY_PROCEDURE_ORDER_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("procedureOrderId", dto.getProcedureOrderId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderEquipmentDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response cancelProcedureOrderEquipment(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_equipment set status = 'I' "
					+ " where procedure_request_id =:proReqId  ")
					.setParameter("proReqId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("cancelProcedureOrderEquipment failed", he);
		} catch (Exception e) {
		logger.error("cancelProcedureOrderEquipment failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getserviceIdByAssetCat(Integer equipmentId, Integer orgId, Integer unitId,Integer visitTypeId,Integer serviceId)
			throws ApplicationException {
		try{
			if(equipmentId!=null && equipmentId>0){

				String sql = "SELECT service_id AS \"serviceId\", measurement_unit_id AS \"measurementUnitId\", duration AS \"duration\" "
						+ "FROM service.m_periodicity_service_config "
						+ "WHERE equipment_id = :equipmentId "
						+ "AND unit_id = :unitId "
						+ "AND organisation_id = :orgId "
						+ "AND visit_type_id = :visitTypeId "
						+ "AND status = 'A' "
						+ "LIMIT 1";

				ProcedureOrderMasterDto obj = (ProcedureOrderMasterDto) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("equipmentId", equipmentId)
						.setParameter("unitId", unitId)
						.setParameter("orgId", orgId)
						.setParameter("visitTypeId", visitTypeId)
						.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class))
						.uniqueResult();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);	
			}else{
				String sql = "SELECT service_id AS \"serviceId\", measurement_unit_id AS \"measurementUnitId\", duration AS \"duration\" "
						+ "FROM service.m_periodicity_service_config "
						+ "WHERE unit_id = :unitId "
						+ "AND organisation_id = :orgId "
						+ "AND visit_type_id = :visitTypeId "
						+ "AND service_id = :serviceId "
						+ "AND status = 'A' "
						+ "LIMIT 1";

				ProcedureOrderMasterDto obj = (ProcedureOrderMasterDto) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("unitId", unitId)
						.setParameter("orgId", orgId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("serviceId", serviceId)
						.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class))
						.uniqueResult();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
			}
			
			
			
		}catch(Exception e){
			
			throw e;
		}
	}

	
}
