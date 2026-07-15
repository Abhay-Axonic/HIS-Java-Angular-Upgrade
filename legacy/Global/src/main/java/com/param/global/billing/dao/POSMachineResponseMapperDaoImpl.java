package com.param.global.billing.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.POSMachineResponseMapper;
import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class POSMachineResponseMapperDaoImpl extends GenericDao<POSMachineResponseMapper, Integer> implements IPOSMachineResponseMapperDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(POSMachineResponseMapperDaoImpl.class);

	public POSMachineResponseMapperDaoImpl() {
		super(POSMachineResponseMapper.class);
	}

	
	@Autowired
	private SessionFactory SessionFactory;

	@Override
	public Response savePOSMachineUniqueId(POSMachineResponseMapperDto dto) {
		try {
			POSMachineResponseMapper mst=new POSMachineResponseMapper();
			mst.setRequestedUniqueId(dto.getRequestedUniqueId());
			mst.setDeviceId(dto.getDeviceId());
			mst.setRequestedJson(dto.getRequestedJson());
			mst.setTransactionAmount(dto.getTransactionAmount());
			mst.setPatientId(dto.getPatientId());
			mst.setOrganizationId(dto.getOrganizationId());
			mst.setUnitId(dto.getUnitId());
			mst.setStatus('A');
			mst.setCreatedBy(dto.getCreatedBy());
			mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			mst.setUpdatedBy(dto.getUpdatedBy());
			mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 
			save(mst);
			dto.setRequestedUniqueId(mst.getRequestedUniqueId());
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, dto.getRequestedUniqueId());

		} catch (HibernateException he) {
			logger.error("savePOSMachineUniqueId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("savePOSMachineUniqueId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPOSMachineUniqueId(String invoice) {
	try {
			
			String query = "SELECT tt.requested_unique_id as \"requestedUniqueId\" "
						 + "FROM  billing.t_pos_machine_response_mapper tt "
						 + "INNER JOIN public.m_route_of_administration roa ON roa.id=fnr.roa_id "
						 + "WHERE tt.requested_unique_id like :invoice ";
			
			List<POSMachineResponseMapperDto> roaMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("invoice","%"+invoice+"%")
					.setResultTransformer(Transformers.aliasToBean(POSMachineResponseMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtosList, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPOSMachineUniqueId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePosMachineTransactionResponse(POSMachineResponseMapperDto dto1) {
		try {
			String updateQuery = "UPDATE billing.t_pos_machine_response_mapper SET generated_reponse = :generatedReponse "
					+ "WHERE requested_unique_id = :requestedUniqueId";

			String insertQuery = "INSERT INTO billing.t_pos_machine_response_mapper (requested_unique_id, generated_reponse) "
					+ "SELECT :requestedUniqueId, :generatedReponse "
					+ "WHERE NOT EXISTS (SELECT 1 FROM billing.t_pos_machine_response_mapper WHERE requested_unique_id = :requestedUniqueId)";

			Query updateSQLQuery = sessionFactory.getCurrentSession().createSQLQuery(updateQuery);
			updateSQLQuery.setParameter("generatedReponse", dto1.getGeneratedReponse());
			updateSQLQuery.setParameter("requestedUniqueId", dto1.getRequestedUniqueId());
			updateSQLQuery.executeUpdate();
			Query insertSQLQuery = sessionFactory.getCurrentSession().createSQLQuery(insertQuery);
			insertSQLQuery.setParameter("generatedReponse", dto1.getGeneratedReponse());
			insertSQLQuery.setParameter("requestedUniqueId", dto1.getRequestedUniqueId());
			insertSQLQuery.executeUpdate();
			return new Response(SUCCESS, null, null, null, dto1);

		} catch (HibernateException he) {
			logger.error("updatePosMachineTransactionResponse failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePosMachineTransactionResponse failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkGeneratedResponseAvailable(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto) {
		try {
			String query = "select mppr.pos_machine_response_id as \"posMachineResponseId\", " +
					"mppr.requested_unique_id as \"requestedUniqueId\", " +
					"mppr.generated_reponse as \"generatedReponse\", " +
					"mppr.device_id as \"deviceId\", " +
					"mppr.patient_id as \"patientId\" " +
					"from billing.t_pos_machine_response_mapper mppr " +
					"where mppr.patient_id = :patientId " +
					"and mppr.generated_reponse is not null " +
					"and mppr.transaction_amount = :transactionAmount " +
					"order by mppr.pos_machine_response_id desc";
			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("patientId", pOSMachineDeviceMasterDto.getPatientId());
			sqlQuery.setParameter("transactionAmount", pOSMachineDeviceMasterDto.getTransactionAmount());
			List<POSMachineResponseMapperDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(POSMachineResponseMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("checkGeneratedResponseAvailable failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePOSMachineResponseMapperFlag(POSMachineResponseMapperDto pOSMachineResponseMapperDto) {
		try {
			if(pOSMachineResponseMapperDto.getRequestedUniqueId() != null){
				String updateQuery = "UPDATE billing.t_pos_machine_response_mapper SET is_consumed = true " +
						"WHERE requested_unique_id = :requestedUniqueId";

				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(updateQuery);
				sqlQuery.setParameter("requestedUniqueId", pOSMachineResponseMapperDto.getRequestedUniqueId());
				sqlQuery.executeUpdate();
			}



			return new Response(SUCCESS, null, null, null, null);

			} catch (HibernateException he) {
				logger.error("updatePOSMachineResponseMapperFlag failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updatePOSMachineResponseMapperFlag failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getPOSMachineConfigDetails(POSMachineResponseMapperDto dto) {
		try {
			String query = "select " +
					"mpmdm.pos_machine_device_id as \"posMachineDeviceId\", " +
					"mpmdm.machine_config_details as \"machineConfigDetails\" " +
					"from billing.m_pos_machine_device_master mpmdm " +
					"where mpmdm.organization_id = :organizationId " +
					"and mpmdm.unit_id = :unitId " +
					"and mpmdm.device_name = :deviceId";
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("organizationId", dto.getOrganizationId());
			sqlQuery.setParameter("unitId", dto.getUnitId());
			sqlQuery.setParameter("deviceId", dto.getDeviceId());
			POSMachineResponseMapperDto obj = (POSMachineResponseMapperDto) sqlQuery
					.setResultTransformer(Transformers.aliasToBean(POSMachineResponseMapperDto.class))
					.uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPOSMachineConfigDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
