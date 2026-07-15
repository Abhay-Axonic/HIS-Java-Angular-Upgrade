package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureConfigurationInventoryItemDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfigurationInventoryItem;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcedureConfigurationInventoryItemDaoImpl extends GenericDao<ProcedureConfigurationInventoryItem, Integer> implements IProcedureConfigurationInventoryItemDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureConfigurationInventoryItemDaoImpl.class);

	public ProcedureConfigurationInventoryItemDaoImpl() {
		super(ProcedureConfigurationInventoryItem.class);
	}

	
	@Override
	public Response saveProcedureConfigInventoryItem(List<ProcedureConfigurationInventoryItemDto> inventoryItemList,
			int procedureConfigId) throws ApplicationException {
		try {
			
			for (ProcedureConfigurationInventoryItemDto dto : inventoryItemList) {
				ProcedureConfigurationInventoryItem mstr = new ProcedureConfigurationInventoryItem();
				mstr.setItemId(dto.getItemId());
				mstr.setQty(dto.getQty());
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(dto.getOrganizationId());
				mstr.setProcedureConfigId(procedureConfigId);
				mstr.setStatus(dto.getStatus());
				mstr.setUnitId(dto.getUnitId());
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				if(dto.getProcedureConfigInventoryItemId()!=null) {
					mstr.setProcedureConfigInventoryItemId(dto.getProcedureConfigInventoryItemId());
					update(mstr);
				}else {
				save(mstr);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}


	@Override
	public Response getInventoryItemsByProcedureConfigId(ProcedureConfigurationDto dto)
			throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INVENTORY_ITEMS_BY_PROCEDURE_CONFIG_ID")
					.getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getProcedureConfigId()!=null){
				subQuery.append(" AND mstr.procedureConfigId=:procedureConfigId ");
			}
			
			if(dto.getServiceId()!=null ){
				subQuery.append(" AND procedureConfiguration.serviceId=:serviceId ");
			}
			
			query = query.concat(subQuery.toString());
			Query createQuery =sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("organizationId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId());

			if(dto.getProcedureConfigId()!=null){
				createQuery.setParameter("procedureConfigId", dto.getProcedureConfigId());
			}
			if(dto.getServiceId()!=null ){
				createQuery.setParameter("serviceId", dto.getServiceId());
			}



			List<ProcedureConfigurationInventoryItemDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationInventoryItemDto.class)).list();
			for (ProcedureConfigurationInventoryItemDto procedureConfigurationInventoryItemDto : list) {
				
				procedureConfigurationInventoryItemDto.setIsItemOrKit('N');
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
