package com.param.assetManagement.maintenance.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceScheduleDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceSparesComsumptionDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceSparesComsumption;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class CorrectiveMaintenanceSparesComsumptionDaoImpl  extends GenericDao< CorrectiveMaintenanceSparesComsumption, Integer> implements ICorrectiveMaintenanceSparesComsumptionDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceSparesComsumptionDaoImpl.class);

	public CorrectiveMaintenanceSparesComsumptionDaoImpl() {
		super( CorrectiveMaintenanceSparesComsumption.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCorrectiveMaintenanceSparesComsumption(CorrectiveMaintenanceSparesComsumptionDto dto)throws ApplicationException {
		try {
				CorrectiveMaintenanceSparesComsumption mstr = new CorrectiveMaintenanceSparesComsumption(); 
				mstr.setItemId(dto.getItemId());
				mstr.setQty(dto.getQty());
				mstr.setSparesComsumptionRemark(dto.getSparesComsumptionRemark());
				mstr.setCorrectiveMaintenanceScheduleId(dto.getCorrectiveMaintenanceScheduleId());
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(dto.getOrganizationId());
				mstr.setStatus(dto.getStatus());
				mstr.setUnitId(dto.getUnitId());
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
				
				if(dto.getCorrectiveMaintenanceSparesComsumptionId()!=null) {
					mstr.setCorrectiveMaintenanceSparesComsumptionId(dto.getCorrectiveMaintenanceSparesComsumptionId());
					sessionFactory.getCurrentSession().update(mstr);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
				}else {
					
				sessionFactory.getCurrentSession().save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
				}
	} catch (HibernateException he) {
		logger.error("Error while saving corrective maintenance spares consumption", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("Error while saving corrective maintenance spares consumption", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getCorrectiveMainSparesComsumptionByitemId(
			CorrectiveMaintenanceSparesComsumptionDto correctiveMaintenanceSparesComsumptionDto)
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
							+" and asst.id =2 and item.is_spares = true  and item.id!= :itemId and (item.code like :itemCode or item.name like :itemName) ")
					.setParameter("unitId", correctiveMaintenanceSparesComsumptionDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceSparesComsumptionDto.getOrganizationId())
					.setParameter("itemId", correctiveMaintenanceSparesComsumptionDto.getItemId())
					.setParameter("itemCode", correctiveMaintenanceSparesComsumptionDto.getItemCode()!=null ? "%"+correctiveMaintenanceSparesComsumptionDto.getItemCode().toLowerCase()+"%" : "")
					.setParameter("itemName", correctiveMaintenanceSparesComsumptionDto.getItemName()!=null ? "%"+correctiveMaintenanceSparesComsumptionDto.getItemName().toLowerCase()+"%" : "")
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceSparesComsumptionDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);

		}catch (Exception e) {
			logger.error("Error while getting corrective maintenance spares consumption by item id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getSparesComsumptionlistById(CorrectiveMaintenanceScheduleDto dto)throws ApplicationException {
		try {
			List<CorrectiveMaintenanceSparesComsumptionDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPARE_LIST_BY_SCHEDULE_ID")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("correctiveMaintenanceScheduleId", dto.getCorrectiveMaintenanceScheduleId())
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceSparesComsumptionDto.class)).list();
			
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);

		}catch (Exception e) {
			logger.error("Error while getting spares consumption list by schedule id", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
