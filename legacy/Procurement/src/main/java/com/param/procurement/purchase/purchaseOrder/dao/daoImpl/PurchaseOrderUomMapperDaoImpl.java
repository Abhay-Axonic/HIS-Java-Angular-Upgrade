package com.param.procurement.purchase.purchaseOrder.dao.daoImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.purchaseOrder.dao.PurchaseOrderUomMapperDao;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;

import com.param.procurement.purchase.purchaseOrder.model.PurchaseOrderUomMapper;

@Repository()
public class PurchaseOrderUomMapperDaoImpl extends AbstractDao<Integer, PurchaseOrderUomMapper> implements PurchaseOrderUomMapperDao{
	
	@Override
	public Integer savePurchaseOrderUom(PurchaseOrderUomMapper poUomMapper) {
		try {
			return (Integer) getSession().save(poUomMapper);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Integer updatePurchaseOrderUomMapper(PurchaseOrderMasterDto purchaseOrderDto) {
	/*try
		{
			for(PurchaseOrderDetailDto purchaseOrderDeatilsDto : purchaseOrderDto.getItems())
			{
				for (PurchaseOrderUomMapperDto purchaseOrderUom : purchaseOrderDeatilsDto.getPoUom())
				{
					PurchaseOrderUomMapper purchaseOrderUomMapperModel = getByKey(purchaseOrderUom.getId());
										
					if(purchaseOrderUomMapperModel!=null)
					{
							if(purchaseOrderUom.getUomTypeId()!=null && purchaseOrderUom.getUomTypeId()>0)
							{
								purchaseOrderUomMapperModel.setUomTypeId(purchaseOrderUom.getUomTypeId());
							}
							if(purchaseOrderUom.getUomUnitId()!=null && purchaseOrderUom.getUomUnitId()>0)
							{
								purchaseOrderUomMapperModel.setUomUnitId(purchaseOrderUom.getUomUnitId());
							}
							if(purchaseOrderUom.getIpUom()!=null)
							{
								purchaseOrderUomMapperModel.setIpUom(purchaseOrderUom.getIpUom());
							}
							if(purchaseOrderUom.getOpUom()!=null)
							{
								purchaseOrderUomMapperModel.setOpUom(purchaseOrderUom.getOpUom());
							}
							if(purchaseOrderUom.getStoreUom()!=null)
							{
								purchaseOrderUomMapperModel.setStoreUom(purchaseOrderUom.getStoreUom());
							}
					}
				}
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
	public List<Object[]> retrievePuchaseOrderUomDetails(Integer purchaseOrderId) {
	try {
			StringBuilder strQuery = new StringBuilder("SELECT poum.id, poum.uom_type_id, ut.\"type\", poum.uom_unit_id, uu.unit, poum.conversions, poum.ip_uom, poum.op_uom, poum.store_uom\r\n" + 
					"FROM procurement.t_purchase_order_uom_mapper poum\r\n" + 
					"INNER JOIN public.m_uom_type ut ON ut.id = poum.uom_type_id\r\n" + 
					"INNER JOIN public.m_uom_unit uu ON uu.id = poum.uom_unit_id\r\n" + 
					"INNER JOIN procurement.t_purchase_order_detail pod ON pod.id = poum.purchase_order_detail_id \r\n" + 
					"INNER JOIN procurement.t_purchase_order po ON po.id = pod.purchase_order_id\r\n" + 
					"WHERE pod.id ="+purchaseOrderId);					
		Query	query = getSessionFactory().getCurrentSession().createSQLQuery(strQuery.toString());
		return query.list();
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

}
