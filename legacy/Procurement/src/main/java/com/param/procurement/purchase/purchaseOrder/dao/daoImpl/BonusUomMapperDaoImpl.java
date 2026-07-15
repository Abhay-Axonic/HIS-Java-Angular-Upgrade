package com.param.procurement.purchase.purchaseOrder.dao.daoImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.purchaseOrder.dao.BonusUomMapperDao;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;

import com.param.procurement.purchase.purchaseOrder.model.BonusUomMapper;
import com.param.procurement.purchase.purchaseOrder.model.LocPurchaseOrderDetail;

@Repository()
public class BonusUomMapperDaoImpl extends AbstractDao<Integer, BonusUomMapper> implements BonusUomMapperDao{
	
	@Override
	public Integer saveBonusUom(BonusUomMapper bonusUomMapper) {
		try {
			return (Integer) getSession().save(bonusUomMapper);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Integer updateBonusUomMapper(PurchaseOrderMasterDto purchaseOrderDto) {
	//try
		/*{
			for(PurchaseOrderDetailDto purchaseOrderDeatilsDto : purchaseOrderDto.getItems())
			{
				for (PurchaseOrderUomMapperDto bonusUom : purchaseOrderDeatilsDto.getBonusUom())
				{
					BonusUomMapper bonusUomMapperModel = getByKey(bonusUom.getId());
										
					if(bonusUomMapperModel!=null)
					{
							if(bonusUom.getUomTypeId()!=null && bonusUom.getUomTypeId()>0)
							{
								bonusUomMapperModel.setUomTypeId(bonusUom.getUomTypeId());
							}
							if(bonusUom.getUomUnitId()!=null && bonusUom.getUomUnitId()>0)
							{
								bonusUomMapperModel.setUomUnitId(bonusUom.getUomUnitId());
							}
							if(bonusUom.getIpUom()!=null)
							{
								bonusUomMapperModel.setIpUom(bonusUom.getIpUom());
							}
							if(bonusUom.getOpUom()!=null)
							{
								bonusUomMapperModel.setOpUom(bonusUom.getOpUom());
							}
							if(bonusUom.getStoreUom()!=null)
							{
								bonusUomMapperModel.setStoreUom(bonusUom.getStoreUom());
							}
					}*/
			/*	}
			}
			return 1;
		}catch (HibernateException he) {
		   h
		   throw he;
		} 
		catch (Exception e) {
		  
		  throw e;
		}*/
		return 1;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> retrieveBonusUomDetails(Integer purchaseOrderDetailsId) {
	try {
			StringBuilder strQuery = new StringBuilder("SELECT boum.id, boum.uom_type_id, ut.\"type\", boum.uom_unit_id, uu.unit, boum.conversions, "
					+ " boum.ip_uom, boum.op_uom, \\\r\n" + " boum.store_uom\r\n" + 
					" FROM procurement.t_bonus_uom_mapper boum\r\n" + 
					" INNER JOIN public.m_uom_type ut ON ut.id = boum.uom_type_id\r\n" + 
					" INNER JOIN public.m_uom_unit uu ON uu.id = boum.uom_unit_id\r\n" + 
					" INNER JOIN procurement.t_purchase_order_detail pod ON pod.id = boum.purchase_order_detail_id \r\n" + 
					" INNER JOIN procurement.t_purchase_order po ON po.id = pod.purchase_order_id\r\n" + 
					" WHERE pod.id ="+purchaseOrderDetailsId);
					
		Query	query = getSessionFactory().getCurrentSession().createSQLQuery(strQuery.toString());
		return query.list();
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
}
