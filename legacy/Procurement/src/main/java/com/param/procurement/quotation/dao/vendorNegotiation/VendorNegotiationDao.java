package com.param.procurement.quotation.dao.vendorNegotiation;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.VendorNegotiation;
import com.param.entity.model.procurement.VendorNegotiationMapper;
import com.param.entity.model.procurement.VendorSelection;
import com.param.entity.model.procurement.VendorSelectionMapper;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.PatientSearchDetails;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.VendorNegotiationMapperDto;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationMapperResp;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationRequest;
import com.param.service.global.SessionService;

@Repository
public class VendorNegotiationDao extends AbstractDao<Integer, VendorNegotiation> implements IVendorNegotiationDao {

	final static Logger logger = Logger.getLogger(VendorNegotiationDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<VendorNegotiation> searchVendorNegotiationList(VendorNegotiationRequest vendorNegotiationRequest,
			Integer screenId) {
		String hql = "SELECT distinct vendorNegotiation FROM VendorNegotiation AS vendorNegotiation "
				+ "	INNER JOIN vendorNegotiation.addedBy as user  " + "	LEFT JOIN vendorNegotiation.store as store "
				+ "	LEFT JOIN vendorNegotiation.vendor as vendor  " + "	LEFT JOIN vendorNegotiation.status as status "
				+ " LEFT JOIN vendorNegotiation.assetType as assetType "
				// + " LEFT JOIN technicalApproval.quotationRequest as quotationRequest "
				+ " WHERE vendorNegotiation.isActive = true AND vendorNegotiation.isDeleted = false AND vendorNegotiation.unit.unitId = :unitId "
				+ " 	AND vendorNegotiation.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				// + " AND (:qrNumber = '' OR lower(quotationRequest.qrNo) LIKE :qrNumber ) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " 	AND (:vendorId = 0 OR vendorNegotiation.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(vendorNegotiation.vendor.name) LIKE :vendorName ) "
				+ " ORDER BY vendorNegotiation.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(vendorNegotiationRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(vendorNegotiationRequest.getToDate()));
		query.setParameter("storeId",
				vendorNegotiationRequest.getStoreId() != null ? vendorNegotiationRequest.getStoreId() : 0);
		query.setParameter("statusId",
				vendorNegotiationRequest.getStatusId() != null ? vendorNegotiationRequest.getStatusId() : 0);
		if (vendorNegotiationRequest.getVendorId() != null && vendorNegotiationRequest.getVendorId() > 0) {
			query.setParameter("vendorId",
					vendorNegotiationRequest.getVendorId() != null ? vendorNegotiationRequest.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (vendorNegotiationRequest.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					vendorNegotiationRequest.getVendorName() != null
							? vendorNegotiationRequest.getVendorName().toLowerCase() + '%'
							: "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		// query.setParameter("qrNumber",techApprovalRequestDto.getQrNumber() !=null ?
		// techApprovalRequestDto.getQrNumber().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",vendorNegotiationRequest.getUnitId()!=null ? vendorNegotiationRequest.getUnitId() : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public VendorNegotiation getVendorNegotiation(Integer id) {
		String hql = "SELECT vendorNegotiation FROM VendorNegotiation AS vendorNegotiation "
				+ "WHERE vendorNegotiation.id = :id  AND vendorNegotiation.isActive = true AND vendorNegotiation.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (VendorNegotiation) query.uniqueResult();
	}

	/*
	 * @Override public List<VendorNegotiationDetail>
	 * getVendorNegotiationDetail(Integer id) { String hql =
	 * "SELECT quotationRequestDetail FROM TechnicalApprovalDetail AS quotationRequestDetail "
	 * +
	 * " WHERE quotationRequestDetail.quotationRequest.id = :id  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
	 * + " ORDER BY quotationRequestDetail.id "; Query query =
	 * getSession().createQuery(hql); query.setParameter("id", id); return
	 * query.list(); }
	 */

	@Override
	public VendorNegotiation updateVendorNegotiation(VendorNegotiation vendorNegotiation) {
		getSession().saveOrUpdate(vendorNegotiation);
		return vendorNegotiation;
	}

	@Override
	public VendorSelection saveVendorSelection(VendorSelection vendorSelection) {
		getSession().save(vendorSelection);
		return vendorSelection;
	}

	@Override
	public VendorSelectionMapper saveVendorSelectionForNew(VendorSelectionMapper vendorSelectionMapper) {
		getSession().save(vendorSelectionMapper);
		return vendorSelectionMapper;
	}

	@Override
	public List<Integer> getMapperIds(Integer id) {

		String hql = "select max(id)  from procurement.t_vendor_negociation_mapper where vendor_negociation_detail_id ="
				+ id;
		Query query = getSession().createSQLQuery(hql);
		return query.list();

		// return (VendorNegotiationMapper) query.uniqueResult();

	}

	@Override
	public List<VendorNegotiationMapperDto> getVendorNegotiationMapperList(Integer id, List<Integer> ids) {

		try {
			
			Query query=null;
			if(ids.get(0) != null) {
				String sql = "select " + 
						"	distinct vendornego0_.id as \"id\", " + 
//						"	vendornego0_.added_by as \"addedBy\", " + 
						"	vendornego0_.added_date as \"addedDate\", " + 
						"	vendornego0_.is_active as \"isActive\", " + 
//						"	vendornego0_.is_deleted as \"isDeleted\", " + 
//						"	vendornego0_.updated_by as \"updatedBy\", " + 
						//"	vendornego0_.updated_date as \"updatedDate\", " + 
//						"	vendornego0_.organization_id as \"organizationId\", " + 
//						"	vendornego0_.unit_id as \"unitId\", " + 
						"	vendornego0_.accept as \"accept\", " + 
						"	vendornego0_.cop as \"cop\", " + 
						"	vendornego0_.on_hold as \"onHold\", " + 
						"	vendornego0_.reject as \"reject\", " + 
						"	vendornego0_.reject_note as \"rejectNote\", " + 
						"	vendornego0_.remarks as \"negotiationRemark\", " + 
						"	vendornego0_.status_id as \"statusId\", " + 
						"	vendornego0_.vendor_expected_payment as \"vendorExpectedPayment\", " + 
						"	vendornego0_.vendor_negociation_detail_id as \"vendorNegociationDetailId\", " + 
						"	vendornego0_.vendor_support_terms as \"vendorSupportTerms\", " + 
						"	vendornego0_.vendor_technical_specification as \"vendorTechnicalSpecification\", " + 
						"	vendornego0_.vendor_warrenty_period as \"vendorwarrentyPeriod\" " + 
						"from " + 
						"	procurement.t_vendor_negociation_mapper vendornego0_ " + 
						"inner join procurement.t_vendor_negociation_detail vendornego1_ on " + 
						"	vendornego0_.vendor_negociation_detail_id = vendornego1_.id " + 
						"where " + 
						"	vendornego1_.id =:id " + 
						"	and vendornego0_.is_active = true " + 
						"	and vendornego0_.is_deleted = false " + 
						"	and (vendornego0_.id in (:ids))";
				 query = getSession().createSQLQuery(sql).addScalar("addedDate").addScalar("accept").addScalar("cop").addScalar("onHold")
						.addScalar("reject").addScalar("rejectNote").addScalar("negotiationRemark")
						.addScalar("statusId").addScalar("vendorExpectedPayment").addScalar("vendorNegociationDetailId")
						.addScalar("vendorSupportTerms").addScalar("vendorTechnicalSpecification").addScalar("vendorwarrentyPeriod");
				query.setParameter("id", id);
				query.setParameterList("ids", ids);
				query.setResultTransformer(Transformers.aliasToBean(VendorNegotiationMapperDto.class));
			
			}else {
				String hql = "SELECT distinct vendorNegotiationMapper  FROM VendorNegotiationMapper  AS vendorNegotiationMapper  "
						+ "WHERE vendorNegotiationMapper.vendorNegociationDetail.id = :id  AND vendorNegotiationMapper.isActive = true "
						+ " AND vendorNegotiationMapper.isDeleted = false  and vendorNegotiationMapper.id in( :ids ) ";

				query = getSession().createQuery(hql);
				query.setParameter("id", id);
				query.setParameterList("ids", ids);

				return query.list();
			}
			/*String hql = "SELECT distinct vendorNegotiationMapper  FROM VendorNegotiationMapper  AS vendorNegotiationMapper  "
					+ "WHERE vendorNegotiationMapper.vendorNegociationDetail.id = :id  AND vendorNegotiationMapper.isActive = true "
					+ " AND vendorNegotiationMapper.isDeleted = false  and vendorNegotiationMapper.id in( :ids ) ";*/
		
			return query.list();

		} catch (Exception e) {
			logger.error("getVendorNegotiationMapperList failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	

}
