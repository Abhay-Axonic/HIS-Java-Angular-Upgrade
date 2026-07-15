package com.param.procurement.purchase.purchaseOrder.dao.daoImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.entity.model.master.OtherCharge;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.purchaseOrder.dao.OtherChargesPOMapperDao;

import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.model.PurchaseOrderOtherChargesMapper;

@Repository()
public class OtherChargesPOMapperDaoImpl extends AbstractDao<Integer, PurchaseOrderOtherChargesMapper> implements OtherChargesPOMapperDao{
	
	@Override
	public Integer saveOtherCharges(PurchaseOrderOtherChargesMapper poOtherChrages) {
		try {
			return (Integer) getSession().save(poOtherChrages);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@Override
	public Integer updatePoOtherCharges(PurchaseOrderMasterDto purchaseOrderDto) {
	/*try
		{
			for(PurchaseOrderDetailDto purchaseOrderDeatilsDto : purchaseOrderDto.getItems())
			{
				for (PoOtherChargesMapperDto poOtherChargesDto : purchaseOrderDeatilsDto.getOtherCharges())
				{
					PurchaseOrderOtherChargesMapper poOtherChargesMapperModel = getByKey(poOtherChargesDto.getId());
										
					if(poOtherChargesMapperModel!=null)
					{
						if(poOtherChargesDto.getOtherChargesId()!=null && poOtherChargesDto.getOtherChargesId()>0)
						{
							OtherCharge otherCharges = new OtherCharge();
							otherCharges.setId(poOtherChargesDto.getOtherChargesId());
							poOtherChargesMapperModel.setOtherCharge(otherCharges);
						}
						if(poOtherChargesDto.getAmount()!=null)
						{
							poOtherChargesMapperModel.setAmount(poOtherChargesDto.getAmount());
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
	public List<Object[]> retrieveOtherChargesDetailsByPoId(Integer purchaseOrderId) {
	try {
			StringBuilder strQuery = new StringBuilder("SELECT ocpm.id, ocpm.other_charges_id, oc.description, ocpm.amount\r\n" + 
					"FROM procurement.t_other_charges_po_mapper ocpm\r\n" + 
					"INNER JOIN public.m_other_charges oc ON oc.id = ocpm.other_charges_id\r\n" + 
					"INNER JOIN procurement.t_purchase_order_detail pod ON pod.id = ocpm.purchase_order_detail_id\r\n" + 
					"INNER JOIN procurement.t_purchase_order po ON po.id = pod.purchase_order_id\r\n" + 
					"WHERE po.id ="+purchaseOrderId);
		Query	query = getSessionFactory().getCurrentSession().createSQLQuery(strQuery.toString());
		return query.list();
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

}
