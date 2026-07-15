package com.param.pharmacy.master.global.dao.tax;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.pharmacy.master.global.dto.tax.TaxResponse;

@Repository("TaxDao")
public class TaxDao extends AbstractDao<Integer, TaxResponse> implements ITaxDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Tax> getAll(Integer orgId) {
		String hql = "SELECT tax FROM Tax AS tax" + " where tax.isActive=true AND tax.isDeleted=false AND tax.organization.organizationId = :orgId "
				+ "ORDER BY tax.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("orgId", orgId!=null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public Tax getTax(Integer id) {
		String hql = "SELECT tax FROM Tax AS tax " + "WHERE tax.id = :id  AND tax.isDeleted = false ";
		// + "WHERE rack.id = :id AND rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Tax) query.uniqueResult();
	}

	@Override
	public Tax getById(Integer id) {

		String hql = "SELECT tax FROM Tax AS tax "
				+ "WHERE tax.id = :id  AND tax.isActive = true AND tax.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Tax) query.uniqueResult();
	}

	@Override
	public List<TaxDetail> getTaxDetail(Integer id) {
		String hql = "SELECT taxDetail FROM TaxDetail AS taxDetail "
				+ " WHERE taxDetail.tax.id = :id  AND taxDetail.isActive = true AND taxDetail.isDeleted = false "
				+ " ORDER BY taxDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public TaxComponent getTaxComponent(Integer id) {
		return (TaxComponent) getSession().get(TaxComponent.class, id);
	}

	@Override
	public Tax updateTax(Tax tax) {
		getSession().update(tax);
		return tax;
	}

	@Override
	public Integer update(Tax tax) {
		getSession().update(tax);
		return tax.getId();
	}

	@Override
	public Tax saveTax(Tax tax) {
		getSession().save(tax);
		return tax;
	}

	@Override
	public List<Tax> getTaxList(Integer orgId) {
		String hql = "SELECT tax FROM Tax AS tax \r\n"
				+ "WHERE tax.isActive = true AND tax.isDeleted = false AND tax.organization.organizationId =:organizationId \r\n"
				+ " ORDER BY tax.name ASC ";

		Query query = getSession().createQuery(hql);
		//query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("organizationId",orgId!=null ? orgId : sessionService.getOrganizationId()); // as discussed changed to org wise

		return query.list();

	}

	@Override
	public Long getTaxFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.purchaseTax.id = :id OR item.salesTax.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getTaxFromTaxComponent(Integer id) {
		String hql = " SELECT count(tax) FROM Tax AS tax "
				+ " INNER JOIN tax.taxDetailList AS taxDetail WITH taxDetail.tax.id=:id AND taxDetail.isActive = true AND taxDetail.isDeleted = false "
				+ " INNER JOIN taxDetail.taxComponent AS tax_component WITH tax_component.isActive = false AND tax_component.isDeleted = false "
				+ " WHERE taxDetail.isDeleted = false AND tax.organization.organizationId =:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId",sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

}
