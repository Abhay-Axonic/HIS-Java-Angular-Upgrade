package com.param.procurement.quotation.dao.technicalApproval;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.QuotationFill;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.TechnicalApproval;
import com.param.entity.model.procurement.TechnicalApprovalDetail;
import com.param.entity.model.procurement.VendorNegotiation;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalRequestDto;

@Repository
public class TechnicalApprovalDao extends AbstractDao<Integer, TechnicalApproval> implements ITechnicalApprovalDao{
	@Autowired
	private SessionService sessionService;

	@Override
	public List<TechnicalApproval> searchTechnicalApprovalList(TechnicalApprovalRequestDto techApprovalRequestDto,Integer screenId) {
		String hql = "SELECT technicalApproval FROM TechnicalApproval AS technicalApproval "
				+ "	INNER JOIN technicalApproval.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN technicalApproval.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN technicalApproval.status as status "
				+ " LEFT JOIN technicalApproval.assetType as assetType "
				+ " LEFT JOIN technicalApproval.quotationRequest as quotationRequest "
				+ " WHERE technicalApproval.isActive = true AND technicalApproval.isDeleted = false AND technicalApproval.unit.unitId = :unitId "
				+ " 	AND technicalApproval.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + " 	AND (:qrNumber = '' OR lower(quotationRequest.qrNo) LIKE :qrNumber ) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY technicalApproval.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(techApprovalRequestDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(techApprovalRequestDto.getToDate()));
		query.setParameter("storeId",techApprovalRequestDto.getStoreId() != null ? techApprovalRequestDto.getStoreId() : 0);
		query.setParameter("statusId", techApprovalRequestDto.getStatusId() != null ? techApprovalRequestDto.getStatusId() : 0);
		query.setParameter("qrNumber",techApprovalRequestDto.getQrNumber() !=null ? '%'+techApprovalRequestDto.getQrNumber().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", techApprovalRequestDto.getUnitId()!=null ? techApprovalRequestDto.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public TechnicalApproval getTechnicalApproval(Integer id)
	{
		String hql = "SELECT technicalApproval FROM TechnicalApproval AS technicalApproval "
            + "WHERE technicalApproval.id = :id  AND technicalApproval.isActive = true AND technicalApproval.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (TechnicalApproval) query.uniqueResult();
	}
	
	@Override
	public List<TechnicalApprovalDetail> getTechnicalApprovalDetail(Integer id)
	{
		String hql = "SELECT quotationRequestDetail FROM TechnicalApprovalDetail AS quotationRequestDetail "
            + " WHERE quotationRequestDetail.quotationRequest.id = :id  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
            + " ORDER BY quotationRequestDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return  query.list();
	}
	
	@Override
	public TechnicalApproval saveTechnicalApproval(TechnicalApproval technicalApproval) {
		getSession().save(technicalApproval);
		return technicalApproval;
		
	}
	
	@Override
	public TechnicalApproval updateTechnicalApproval(TechnicalApproval technicalApproval) {
		getSession().update(technicalApproval);
		return technicalApproval;
	}
	
	@Override
	public QuotationRequest updateQuotationRequest(QuotationRequest quotationRequest) {
		getSession().update(quotationRequest);
		return quotationRequest;
	}

	@Override
	public QuotationRequest getQuotationRequest(Integer qrId)
	{
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
	            + "WHERE quotationRequest.id = :id  AND quotationRequest.isActive = true AND quotationRequest.isDeleted = false "
		        + " ORDER BY quotationRequest.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", qrId);
		return (QuotationRequest) query.uniqueResult();
	}
/*	@Override
	public List<QuotationFill> getQuotationFill(Integer qrId)
	{		String hql = "SELECT quotationFill FROM QuotationFill AS quotationFill "
				 + "WHERE quotationFill.quotationRequest.id = :id  AND quotationFill.isActive = true AND quotationFill.isDeleted = false "
		         + " ORDER BY quotationFill.id ";
		Query query = getSession().createQuery(hql);		query.setParameter("id", qrId);		return  query.list();	}*/
	
	@Override
	public List<QuotationFill> getQuotationFill(Integer qrId)
	{
		String hql = "SELECT quotationFill FROM QuotationFill AS quotationFill "
								+ "WHERE quotationFill.quotationRequest.id = :qrId  AND quotationFill.isActive = true AND quotationFill.isDeleted = false "
								+ " ORDER BY quotationFill.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("qrId", qrId);
		return  query.list();
	}
	

	@Override
	public List<QuotationRequestDetail> getQuotationRequestDetail(Integer qrId)
	{
		String hql = "SELECT quotationRequestDetail FROM QuotationRequestDetail AS quotationRequestDetail "
				//+ " INNER JOIN quotationFillDetail.quotationRequestDetail AS quotationRequestDetail WITH quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
					 + "WHERE  quotationRequestDetail.quotationRequest.id= :qrId AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false ";
		      
		Query query = getSession().createQuery(hql);
		query.setParameter("qrId", qrId);
		return  query.list();
		}
	/*@Override
	public QuotationFillDetail getQuotationFillDetail(Integer qrId)
	{
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
						+ "WHERE quotationRequest.id = :id  AND quotationRequest.isActive = true AND quotationRequest.isDeleted = false "
  						+ " group by quotationRequest.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", qrId);
		return  (QuotationFillDetail) query.uniqueResult();
	}
	*/
	@Override
	public QuotationFill getQuotationFillById(Integer qfId)
	{
		String hql = "SELECT quotationFill FROM QuotationFill AS quotationFill "
				 + "WHERE quotationFill.id = :id  AND quotationFill.isActive = true AND quotationFill.isDeleted = false "
		         + " ORDER BY quotationFill.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", qfId);
		return  (QuotationFill) query.uniqueResult();
	}
	
	@Override
	public VendorNegotiation saveVendorNegotiation(VendorNegotiation vendorNegotiation) {
		getSession().save(vendorNegotiation);
		return vendorNegotiation;
		
	}
	
}
