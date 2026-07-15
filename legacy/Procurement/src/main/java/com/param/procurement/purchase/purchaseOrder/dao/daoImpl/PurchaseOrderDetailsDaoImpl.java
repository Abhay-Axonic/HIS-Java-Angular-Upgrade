package com.param.procurement.purchase.purchaseOrder.dao.daoImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.entity.model.master.Status;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.purchaseOrder.dao.PurchaseOrderDetailsDao;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;
import com.param.procurement.purchase.purchaseOrder.model.LocPurchaseOrderDetail;
import com.param.utility.TimeZoneComponent;

@Repository()
public class PurchaseOrderDetailsDaoImpl extends AbstractDao<Integer, LocPurchaseOrderDetail> implements PurchaseOrderDetailsDao{
	
	
	@Override
	public Integer savePurchaseOrderDetails(LocPurchaseOrderDetail locPurchaseOrderDetails) {
		try {
			return (Integer) getSession().save(locPurchaseOrderDetails);
		}catch (HibernateException he) {
             throw he;
		} 
		catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@Override
	public Integer updatePurchaseOrderDetails(PurchaseOrderMasterDto purchaseOrderDto) {
	try
		{
			for(PurchaseOrderDetailDto purchaseOrderDeatilsDto : purchaseOrderDto.getItems())
			{
				LocPurchaseOrderDetail locPurchaseOrderDetails = getByKey(purchaseOrderDeatilsDto.getId());
				if(locPurchaseOrderDetails!=null)
				{	
									
				if(purchaseOrderDeatilsDto.getPoQuantity()!=null)
				{
					locPurchaseOrderDetails.setPoQuantity(purchaseOrderDeatilsDto.getPoQuantity());
				}
				
				if(purchaseOrderDeatilsDto.getBonusQuantity()!=null)
				{
					locPurchaseOrderDetails.setBonusQuantity(purchaseOrderDeatilsDto.getBonusQuantity());
				}
				
				if(purchaseOrderDeatilsDto.getCop()!=null)
				{
					locPurchaseOrderDetails.setCop(purchaseOrderDeatilsDto.getCop());
				}
				if(purchaseOrderDeatilsDto.getDiscount()!=null)
				{
					locPurchaseOrderDetails.setDiscount(purchaseOrderDeatilsDto.getDiscount());
				}
					
				if(purchaseOrderDeatilsDto.getTaxId()!=null && purchaseOrderDeatilsDto.getTaxId()>0)
				{
					locPurchaseOrderDetails.setTaxId(purchaseOrderDeatilsDto.getTaxId());
				}
				if(purchaseOrderDeatilsDto.getTaxPercentage()!=null)
				{
					locPurchaseOrderDetails.setTaxPercentage(purchaseOrderDeatilsDto.getTaxPercentage());
				}
				if(purchaseOrderDeatilsDto.getTaxAmount()!=null )
				{
					locPurchaseOrderDetails.setTaxAmount(purchaseOrderDeatilsDto.getTaxAmount());
				}
				if(purchaseOrderDeatilsDto.getNetAmount()!=null)
				{
					locPurchaseOrderDetails.setNetAmount(purchaseOrderDeatilsDto.getNetAmount());
				}
				if(purchaseOrderDeatilsDto.getTotalDiscount()!=null)
				{
					locPurchaseOrderDetails.setTotalDiscount(purchaseOrderDeatilsDto.getTotalDiscount());;
				}
				/*if(purchaseOrderDeatilsDto.getTotalOtherCharges()!=null )
				{
					locPurchaseOrderDetails.setTotalOtherAmount(purchaseOrderDeatilsDto.getTotalOtherCharges());
				}*/
				
				if(purchaseOrderDeatilsDto.getManufactureId()!=null && purchaseOrderDeatilsDto.getManufactureId()>0)
				{
					locPurchaseOrderDetails.setManufactureId(purchaseOrderDeatilsDto.getManufactureId());
				}
					
				if(purchaseOrderDeatilsDto.getStatusId()!=null && purchaseOrderDeatilsDto.getStatusId()>0)
				{
					Status status = new Status();
					status.setId(purchaseOrderDeatilsDto.getStatusId());
					locPurchaseOrderDetails.setStatus(status);
				}
					
				if(purchaseOrderDeatilsDto.getSpecification()!=null)
				{
					locPurchaseOrderDetails.setSpecification(purchaseOrderDeatilsDto.getSpecification());
				}
				if(purchaseOrderDeatilsDto.getRemark()!=null)
				{
					locPurchaseOrderDetails.setRemark(purchaseOrderDeatilsDto.getRemark());
				}
					
				if(purchaseOrderDeatilsDto.getBonusUomTypeId()!=null && purchaseOrderDeatilsDto.getBonusUomTypeId()>0)
				{
					locPurchaseOrderDetails.setBonusUOMTypeId(purchaseOrderDeatilsDto.getBonusUomTypeId());
				}
				if(purchaseOrderDeatilsDto.getBonusUomUnitId()!=null && purchaseOrderDeatilsDto.getBonusUomUnitId()>0)
				{
					locPurchaseOrderDetails.setBonusUOMUnitId(purchaseOrderDeatilsDto.getBonusUomUnitId());
				}
				if(purchaseOrderDeatilsDto.getPoUomTypeId()!=null && purchaseOrderDeatilsDto.getPoUomTypeId()>0)
				{
					locPurchaseOrderDetails.setPoUOMTypeId(purchaseOrderDeatilsDto.getPoUomTypeId());
				}
				if(purchaseOrderDeatilsDto.getPoUomUnitId()!=null && purchaseOrderDeatilsDto.getPoUomUnitId()>0)
				{
					locPurchaseOrderDetails.setPoUOMUnitId(purchaseOrderDeatilsDto.getPoUomUnitId());
				}
				
				if(purchaseOrderDeatilsDto.getPoApprovedQuantity()!=null && purchaseOrderDeatilsDto.getPoApprovedQuantity()>0)
				{
					locPurchaseOrderDetails.setPoApprovedQuantity(purchaseOrderDeatilsDto.getPoApprovedQuantity());
				}
				if(purchaseOrderDeatilsDto.getBonusApprovedQuantity()!=null  && purchaseOrderDeatilsDto.getBonusApprovedQuantity()>0)
				{
					locPurchaseOrderDetails.setBonusApprovedQuantity(purchaseOrderDeatilsDto.getBonusApprovedQuantity());
				}
				if(purchaseOrderDeatilsDto.getRejectReasonId()!=null && purchaseOrderDeatilsDto.getRejectReasonId()>0)
				{
					locPurchaseOrderDetails.setRejectReasonId(purchaseOrderDeatilsDto.getRejectReasonId());
				}
				if(purchaseOrderDeatilsDto.getRejectedQuantity()!=null && purchaseOrderDeatilsDto.getRejectedQuantity()>0)
				{
					locPurchaseOrderDetails.setRejectedQuantity(purchaseOrderDeatilsDto.getRejectedQuantity());
				}
				if(purchaseOrderDeatilsDto.getRejectNote()!=null && purchaseOrderDeatilsDto.getRejectNote().length()==0)
				{
					locPurchaseOrderDetails.setRejectNote(purchaseOrderDeatilsDto.getRejectNote());
				}
				
					
				locPurchaseOrderDetails.setUpdatedBy(1L);					
				locPurchaseOrderDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(locPurchaseOrderDetails.getUnitId()));
				getSession().update(locPurchaseOrderDetails);	
				}
	      }
			return 1;
	}
	catch (HibernateException he) {
        throw he;
		} 
		catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Integer updatePurchaseOrderDetailStatus(PurchaseOrderStatusDto purchaseOrderStatusDto) {
		try {
			List<Integer> poIdList = purchaseOrderStatusDto.getPoIdList();
			for (Integer Id : poIdList) {
				String sql = null;
				if(purchaseOrderStatusDto.getStatusId()==5)    //new SimpleDateFormat("yyyy-M-dd").format((Date)
				{//approved StatusId 
					sql = "UPDATE procurement.t_purchase_order_detail" + " SET status_id=" + purchaseOrderStatusDto.getStatusId()+ ", po_approved_quantity=po_quantity " 
					+", bonus_approved_quantity = bonus_quantity, approved_by="+1+", approved_date='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()))+"', updated_by="+1+", updated_date='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()))
					+ "' WHERE purchase_order_id=" + Id;
				}
				if(purchaseOrderStatusDto.getStatusId()==9)
				{//reject statusId
					sql = "UPDATE procurement.t_purchase_order_detail" + " SET status_id=" + purchaseOrderStatusDto.getStatusId()+ ", po_approved_quantity=po_quantity " 
							+", bonus_approved_quantity = bonus_quantity, approved_by="+1+", approved_date="+TimeZoneComponent.getDateByzone(purchaseOrderStatusDto.getUnitId())+", updated_by="+1+", updated_date="+TimeZoneComponent.getDateByzone(purchaseOrderStatusDto.getUnitId())
							+ " WHERE purchase_order_id=" + Id;
				}
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			 query.executeUpdate();
		}
			return 1;
		}catch (HibernateException he) {
           throw he;
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> retrievePuchaseOrderDetails(Integer id) {
	try {
			StringBuilder strQuery = new StringBuilder("SELECT pod.id, pod.item_id, it.code, it.name as \"itemName\", pod.po_quantity, pod.po_uom_type_id, ut.type as \"poUomType\" ,\r\n" + 
					" pod.po_uom_unit_id, uu.unit as \"poUnit\", pod.bonus_quantity, pod.bonus_uom_type_id, but.type as \"bonusUomType\", pod.bonus_uom_unit_id, \r\n" + 
					" buu.unit as \"bonusUnit\", pod.cop, pod.mrp, pod.discount, pod.tax_id, tax.name as \"taxCode\", pod.tax_percentage, \r\n" + 
					" pod.total_amount, pod.total_discount, pod.tax_amount, pod.total_other_amount, pod.net_amount, pod.specification,\r\n" + 
					" pod.remark\r\n" + 
					" FROM procurement.t_purchase_order_detail pod\r\n" + 
					" INNER JOIN procurement.t_purchase_order po ON po.id = pod.purchase_order_id\r\n" + 
					" INNER JOIN public.m_item it ON it.id = pod.item_id\r\n" + 
					" INNER JOIN public.m_uom_type ut ON ut.id = pod.po_uom_type_id\r\n" + 
					" INNER JOIN public.m_uom_unit uu ON uu.id = pod.po_uom_unit_id\r\n" + 
					" INNER JOIN public.m_uom_type but ON but.id = pod.bonus_uom_type_id\r\n" + 
					" INNER JOIN public.m_uom_unit buu ON buu.id = pod.bonus_uom_unit_id\r\n" + 
					" INNER JOIN public.m_tax tax ON tax.id = pod.tax_id\r\n" + 
					" WHERE pod.purchase_order_id ="+id);					
		Query	query = getSessionFactory().getCurrentSession().createSQLQuery(strQuery.toString());
		return query.list();
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}

	}
	
	
	
	@Override
	public Integer updatePurchaseOrderDetailsApproval(PurchaseOrderDetailDto purchaseOrderDeatilsDto) {
	try
		{
			LocPurchaseOrderDetail locPurchaseOrderDetails = getByKey(purchaseOrderDeatilsDto.getId());
				if(locPurchaseOrderDetails!=null)
				{	
									
				if(purchaseOrderDeatilsDto.getStatusId()!=null && purchaseOrderDeatilsDto.getStatusId()>0)
				{
					Status status = new Status();
					status.setId(purchaseOrderDeatilsDto.getStatusId());
					locPurchaseOrderDetails.setStatus(status);
				}
					
								
				if(purchaseOrderDeatilsDto.getPoApprovedQuantity()!=null && purchaseOrderDeatilsDto.getPoApprovedQuantity()>0)
				{
					locPurchaseOrderDetails.setPoApprovedQuantity(purchaseOrderDeatilsDto.getPoApprovedQuantity());
				}
				if(purchaseOrderDeatilsDto.getBonusApprovedQuantity()!=null  && purchaseOrderDeatilsDto.getBonusApprovedQuantity()>0)
				{
					locPurchaseOrderDetails.setBonusApprovedQuantity(purchaseOrderDeatilsDto.getBonusApprovedQuantity());
				}
				if(purchaseOrderDeatilsDto.getRejectReasonId()!=null && purchaseOrderDeatilsDto.getRejectReasonId()>0)
				{
					locPurchaseOrderDetails.setRejectReasonId(purchaseOrderDeatilsDto.getRejectReasonId());
				}
				if(purchaseOrderDeatilsDto.getRejectedQuantity()!=null && purchaseOrderDeatilsDto.getRejectedQuantity()>0)
				{
					locPurchaseOrderDetails.setRejectedQuantity(purchaseOrderDeatilsDto.getRejectedQuantity());
				}
				if(purchaseOrderDeatilsDto.getRejectNote()!=null && purchaseOrderDeatilsDto.getRejectNote().length()==0)
				{
					locPurchaseOrderDetails.setRejectNote(purchaseOrderDeatilsDto.getRejectNote());
				}
				
					
				locPurchaseOrderDetails.setUpdatedBy(1L);					
				locPurchaseOrderDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(locPurchaseOrderDetails.getUnitId()));
				getSession().update(locPurchaseOrderDetails);	
				}
			return 1;
	}
	catch (HibernateException he) {
        throw he;
		} 
		catch (Exception e) {
			
			throw e;
		}
	}
}
