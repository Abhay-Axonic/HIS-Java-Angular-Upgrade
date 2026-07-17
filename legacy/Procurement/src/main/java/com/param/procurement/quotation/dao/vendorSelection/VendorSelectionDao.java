package com.param.procurement.quotation.dao.vendorSelection;

import java.util.Arrays;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.VendorNegotiationDetail;
import com.param.entity.model.procurement.VendorNegotiationMapper;
import com.param.entity.model.procurement.VendorSelection;
import com.param.entity.model.procurement.VendorSelectionMapper;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.VendorNegotiationMapperDto;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionRequest;

@Repository
public class VendorSelectionDao extends AbstractDao<Integer, VendorSelection> implements IVendorSelectionDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<VendorSelection> searchVendorSelectionList(VendorSelectionRequest vendorSelectionRequest,
			Integer screenId) {
		String hql = "SELECT vendorSelection FROM VendorSelection AS vendorSelection "
				+ "	INNER JOIN vendorSelection.addedBy as user  "
				+ " INNER JOIN vendorSelection.quotationRequest as quotationRequest "
				+ "	LEFT JOIN quotationRequest.store as store " + "	LEFT JOIN vendorSelection.status as status "
				+ " LEFT JOIN quotationRequest.assetType as assetType "
				+ " LEFT JOIN quotationRequest.requestType as requestType "
				+ " WHERE vendorSelection.isActive = true AND vendorSelection.isDeleted = false AND vendorSelection.unit.unitId = :unitId "
				+ " 	AND vendorSelection.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY vendorSelection.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(vendorSelectionRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(vendorSelectionRequest.getToDate()));
		query.setParameter("storeId",
				vendorSelectionRequest.getStoreId() != null ? vendorSelectionRequest.getStoreId() : 0);
		query.setParameter("statusId",
				vendorSelectionRequest.getStatusId() != null ? vendorSelectionRequest.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", vendorSelectionRequest.getUnitId()!=null ? vendorSelectionRequest.getUnitId() : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public VendorSelection getVendorSelection(Integer id) {
		String hql = "SELECT vendorSelection FROM VendorSelection AS vendorSelection "
				+ "WHERE vendorSelection.id = :id  AND vendorSelection.isActive = true AND vendorSelection.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (VendorSelection) query.uniqueResult();
	}

	@Override
	public VendorSelection getVendorSelectionFornegotiation(Integer qrId) {
		String hql = "SELECT vendorSelection FROM VendorSelection AS vendorSelection "
				+ "WHERE vendorSelection.quotationRequest.id = :id  AND vendorSelection.isActive = true AND vendorSelection.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", qrId);

		return (VendorSelection) query.uniqueResult();
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
	public VendorSelection updateVendorSelection(VendorSelection vendorSelection) {
		getSession().saveOrUpdate(vendorSelection);
		return vendorSelection;
	}

	@Override
	public List<VendorSelectionMapper> getVendorSelectionMapperList(Integer id, List<Integer> ids) {

		/*
		 * String hql =
		 * "SELECT distinct vendorSelectionMapper FROM VendorSelectionMapper  AS vendorSelectionMapper inner join vendorSelectionMapper.vendorNegotiationMapper "
		 * +
		 * "WHERE vendorSelectionMapper.vendorSelectionDetail.id = :id  AND vendorSelectionMapper.isActive = true AND vendorSelectionMapper.isDeleted = false "
		 * ;
		 */
		/*
		 * String hql =
		 * "SELECT distinct vendorSelectionMapper  FROM VendorSelectionMapper  AS vendorSelectionMapper  "
		 * +
		 * "WHERE vendorSelectionMapper.vendorSelectionDetail.id = :id  AND vendorSelectionMapper.isActive = true "
		 * +
		 * " AND vendorSelectionMapper.isDeleted = false  and vendorSelectionMapper.vendorNegotiationMapper.id in( "
		 * +
		 * " select max(vm.vendorNegotiationMapper.id) from  VendorSelectionMapper as vm"
		 * +
		 * " where  vm.vendorSelectionDetail.id = :id  AND vm.isActive = true group by vm.vendor.id) "
		 * ;
		 */ // + ""
			// + " group by vendorSelectionMapper.vendorNegotiationMapper.id";

		String hql = "SELECT distinct vendorSelectionMapper  FROM VendorSelectionMapper  AS vendorSelectionMapper  "
				+ "WHERE vendorSelectionMapper.vendorSelectionDetail.id = :id  AND vendorSelectionMapper.isActive = true "
				+ " AND vendorSelectionMapper.isDeleted = false  and vendorSelectionMapper.id in( :ids ) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameterList("ids", ids);

		return query.list();

	}

	@Override
	public VendorNegotiationMapper getVendorNegotiationMapper(Integer id) {

		String hql = "SELECT vendorNegotiationMapper FROM VendorNegotiationMapper  AS vendorNegotiationMapper "
				+ "WHERE vendorNegotiationMapper.vendorNegociationDetail.id = :id  AND vendorNegotiationMapper.isActive = true AND vendorNegotiationMapper.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (VendorNegotiationMapper) query.uniqueResult();

	}

	@Override
	public VendorNegotiationDetail getVendorNegotiationDetail(Integer id) {

		String hql = "SELECT vendorNegotiationDetail FROM VendorNegotiationMapper  AS VendorNegotiationMapper inner join VendorNegotiationMapper.vendorNegotiationDetail as vendorNegotiationDetail "
				+ "WHERE VendorNegotiationMapper.id = :id  AND vendorNegotiationDetail.isActive = true AND vendorNegotiationDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		if (query.list().size() > 0) {
			return (VendorNegotiationDetail) query.list().getFirst();
		} else {
			return null;
		}

		// return (VendorNegotiationMapper) query.uniqueResult();

	}

	@Override
	public List<Integer> getMapperIds(Integer id) {

		String hql = "   select max( id ) from procurement.t_vendor_selection_mapper\r\n"
				+ " where vendor_selection_detail_id=" + id + " group by vendor_id  ";
		Query query = getSession().createSQLQuery(hql);
		return query.list();

		// return (VendorNegotiationMapper) query.uniqueResult();

	}

	@Override
	public VendorNegotiationMapperDto getVendorNegotiationMapperByIdNew(int id) {

		/*
		 * List<VendorNegotiationMapper> vm=null; String hql =
		 * "SELECT vendorNegotiationMapper FROM VendorNegotiationMapper  AS vendorNegotiationMapper "
		 * +
		 * "WHERE vendorNegotiationMapper.id = :id  AND vendorNegotiationMapper.isActive = true AND vendorNegotiationMapper.isDeleted = false "
		 * ; Query query = getSession().createQuery(hql); query.setParameter("id", id);
		 * 
		 * //return
		 * query.setMaxResults(1).setResultTransformer(Transformers.aliasToBean(
		 * VendorNegotiationMapper.class)).list().get(0);
		 * 
		 * if(query.list().size() > 0) { vm= query.list(); }
		 * 
		 * return vm.get(0);
		 */

		String sql = "  select vendornego0_.id as id , vendornego0_.vendor_negociation_detail_id as vendorNegociationDetailId , vendornego0_.cop as cop , "
				+ "  vendornego0_.vendor_warrenty_period as vendorWarrentyPeriod , vendornego0_.vendor_support_terms as vendorsupportTerms , vendornego0_.vendor_technical_specification "
				+ "  as vendorTechnicalSpecification , vendornego0_.vendor_expected_payment as vendorExpectedPayment , vendornego0_.remarks as remark  "
				+ "  from procurement.t_vendor_negociation_mapper vendornego0_ where vendornego0_.id=" + id
				+ "  and vendornego0_.is_active=true and vendornego0_.is_deleted=false ";
		Query query = getSession().createSQLQuery(sql);
		return (VendorNegotiationMapperDto) query.setMaxResults(1)
				.setResultTransformer(Transformers.aliasToBean(VendorNegotiationMapperDto.class)).uniqueResult();

	}

	@Override
	public Integer getVendorNegotiationMapperId(Integer id) {

		String hql = "   select vendor_negociation_mapper_id from procurement.t_vendor_selection_mapper\r\n"
				+ " where id=" + id;
		Query query = getSession().createSQLQuery(hql);
		return (Integer) query.uniqueResult();

		// return (VendorNegotiationMapper) query.uniqueResult();

	}

	@Override
	public VendorNegotiationDetail getVendorNegotiationDetailById(Integer vendornegociationdetailid) {

		String hql = "SELECT vendorNegotiationDetail FROM VendorNegotiationDetail  AS vendorNegotiationDetail  "
				+ "WHERE vendorNegotiationDetail.id = :id  AND vendorNegotiationDetail.isActive = true AND vendorNegotiationDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", vendornegociationdetailid);

		return (VendorNegotiationDetail) query.uniqueResult();

	}

	@Override
	public void updateVendorSelectionMapper(VendorSelectionMapper mapper) {

		getSession().update(mapper);

	}

}
