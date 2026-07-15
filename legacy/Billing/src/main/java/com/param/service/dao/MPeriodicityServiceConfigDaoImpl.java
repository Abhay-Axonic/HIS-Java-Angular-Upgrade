package com.param.service.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.dto.PeriodicityPatientSearchResDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.MPeriodicityServiceConfigDto;
import com.param.service.global.model.MPeriodicityServiceConfig;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MPeriodicityServiceConfigDaoImpl extends GenericDao<MPeriodicityServiceConfig, Integer> implements IMPeriodicityServiceConfigDao, ICommonConstants{

	public MPeriodicityServiceConfigDaoImpl() {
		super(MPeriodicityServiceConfig.class);
	}

	@Override
	public Response saveMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto) throws ServiceException {
		try{
			MPeriodicityServiceConfig config = new MPeriodicityServiceConfig();
			config.setCreatedBy(dto.getCreatedBy());
			config.setCreatedDate(dto.getCreatedDate() != null && dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config.setDuration(dto.getDuration());
			config.setEquipmentId(dto.getEquipmentId());
			config.setAssetCategoryId(dto.getAssetCategoryId());
			config.setProductCategoryId(dto.getProductCategoryId());
			config.setMeasurementUnitId(dto.getMeasurementUnitId());
			config.setOrganisationId(dto.getOrganisationId());
			config.setServiceId(dto.getServiceId());
			config.setStatus(ACTIVE);
			config.setUnitId(dto.getUnitId());
			config.setUpdatedBy(dto.getCreatedBy());
			config.setUpdatedDate(dto.getCreatedDate() != null && dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config.setVisitTypeId(dto.getVisitTypeId());
			if(dto.getPeriodicityServiceConfigId() != null && dto.getPeriodicityServiceConfigId() > 0)
			{
				config.setPeriodicityServiceConfigId(dto.getPeriodicityServiceConfigId());
			}
			config = save(config);
			return new Response<>(SUCCESS, SUCCESS_CODE, PERIODICITY_CREATED_SUCCESS_MSG, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getMPeriodicityList(int orgId, int unitId) throws ServiceException {
		try{
			List<MPeriodicityServiceConfigDto> listMPeriodicityServiceConfigDto = sessionFactory.getCurrentSession().getNamedQuery("GET_MPERIODICITY_CONFIG_LIST").setParameter("unitId", unitId)
						.setParameter("orgId", orgId).setResultTransformer(Transformers.aliasToBean(MPeriodicityServiceConfigDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listMPeriodicityServiceConfigDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateMPeriodicityStatusById(int id, char status) throws ServiceException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_MPERIODICITY_STATUS").setParameter("status", status)
						.setParameter("id", id).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto) throws ServiceException {
		try{
			MPeriodicityServiceConfig config = new MPeriodicityServiceConfig();
			config.setPeriodicityServiceConfigId(dto.getPeriodicityServiceConfigId());
			config.setCreatedBy(dto.getCreatedBy());
			config.setCreatedDate(dto.getCreatedDate() != null && dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config.setDuration(dto.getDuration());
			config.setEquipmentId(dto.getEquipmentId());
			config.setAssetCategoryId(dto.getAssetCategoryId());
			config.setProductCategoryId(dto.getProductCategoryId());
			config.setMeasurementUnitId(dto.getMeasurementUnitId());
			config.setOrganisationId(dto.getOrganisationId());
			config.setServiceId(dto.getServiceId());
			config.setVisitTypeId(dto.getVisitTypeId());
			config.setStatus(ACTIVE);
			config.setUnitId(dto.getUnitId());
			config.setUpdatedBy(dto.getUpdatedBy());
			config.setUpdatedDate(dto.getUpdatedDate() != null && dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config = update(config);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response searchPatientForPServiceOrder(int visitTypeId, int orgId, int unitId, String name)throws ServiceException {
		try{
			List<PeriodicityPatientSearchResDto> listPeriodicityPatientSearchResDto = sessionFactory.getCurrentSession().getNamedQuery("SEARCH_PATIENT_FOR_PERIODICITY_SERVICES").setParameter("visitTypeId", visitTypeId)
						.setParameter("orgId", orgId).setParameter("unitId", unitId).setParameter("name", "%"+name.toLowerCase().trim()+"%")
						.setResultTransformer(Transformers.aliasToBean(PeriodicityPatientSearchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPeriodicityPatientSearchResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response validatePServiceConfigAvail(MPeriodicityServiceConfigDto dto) throws ServiceException {
		try{
			String query = null;
			boolean flag2 = true;
			query = " select exists(select config.periodicity_service_config_id "
					+ "from service.m_periodicity_service_config config "
			 		+" where config.service_id =:serviceId "
			 		+ " and config.unit_id =:unitId "
			 		+ " and config.visit_type_id =:visitTypeId ) ";
			if(dto.getEquipmentId() != null && dto.getEquipmentId() >0)
			{
				String query2 = " select exists(select config.periodicity_service_config_id "
						+ "from service.m_periodicity_service_config config "
				 		+" where config.equipment_id =:equipmentId "
				 		+ " and config.unit_id =:unitId "
				 		+ " and config.visit_type_id =:visitTypeId ) ";
				flag2 = (boolean)sessionFactory.getCurrentSession().createSQLQuery(query2)
						.setParameter("unitId", dto.getUnitId())
						.setParameter("visitTypeId", dto.getVisitTypeId())
						.setParameter("equipmentId", dto.getEquipmentId())
						.uniqueResult();
			}
			boolean flag = (boolean)sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", dto.getUnitId())
						.setParameter("visitTypeId", dto.getVisitTypeId())
						.setParameter("serviceId", dto.getServiceId())
						.uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null,  flag==true && flag2==true);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public boolean checkExistsPServConfigUpdate(int serviceId, int equipmentId, int unitId, int configId,int visitTypeId)throws ServiceException {
		try{
			String query = null;
			boolean flag1 = true;
			query = " select exists(select config.periodicity_service_config_id "
					+ "from service.m_periodicity_service_config config "
			 		+" where config.service_id =:serviceId "
			 		+ " and config.unit_id =:unitId "
			 		+ " and config.periodicity_service_config_id <> :configId "
			 		+ " and config.visit_type_id =:visitTypeId ) ";
			if(equipmentId>0)
			{
				String query1 = " select exists(select config.periodicity_service_config_id "
						+ "from service.m_periodicity_service_config config "
				 		+" where config.equipment_id =:equipmentId "
				 		+ " and config.unit_id =:unitId "
				 		+ " and config.periodicity_service_config_id <> :configId "
				 		+ " and config.visit_type_id =:visitTypeId) ";
				flag1 = (boolean)sessionFactory.getCurrentSession().createSQLQuery(query1)
						.setParameter("unitId", unitId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("equipmentId", equipmentId)
						.setParameter("configId", configId)
						.uniqueResult();
			}
			boolean flag2 = (boolean)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("serviceId", serviceId)
					.setParameter("configId", configId)
						.uniqueResult();
			
			return flag1==true && flag2==true;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response validatePServiceConfigAvailUpdate(int serviceId, int organisationId, int unitId, int visitTypeId,
			int periodicityServiceConfigId) throws ServiceException {
		try{
			MPeriodicityServiceConfigDto dto = (MPeriodicityServiceConfigDto)sessionFactory.getCurrentSession().getNamedQuery("VALIDATE_PERIODICITY_SERVICE_CONFIG_AVAILABILITY_UPDATE").setParameter("serviceId", serviceId)
						.setParameter("orgId", organisationId)
						.setParameter("unitId", unitId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("peroidicityId", periodicityServiceConfigId)
						.setResultTransformer(Transformers.aliasToBean(MPeriodicityServiceConfigDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	

}
