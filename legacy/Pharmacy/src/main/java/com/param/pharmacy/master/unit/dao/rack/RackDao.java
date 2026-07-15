package com.param.pharmacy.master.unit.dao.rack;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Store;
import com.param.service.global.SessionService;

@Repository("RackDao")
public class RackDao extends AbstractDao<Integer, Rack> implements IRackDao {
	
    @Autowired
    SessionService sessionService;

	@Override
	public List<Rack> getAll(Integer unitId, Integer orgId) {
        String hql = "SELECT  rack FROM Rack AS rack" + " where rack.isActive=true AND rack.isDeleted=false AND rack.organization.organizationId = :organizationId" +
				" AND rack.unit.unitId = :unitId "
				+ "ORDER BY rack.id DESC";

		Query query = getSession().createQuery(hql);
        query.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());
        query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public Rack getRack(Integer id) {
		String hql = "SELECT rack FROM Rack AS rack "
				+ "WHERE rack.id = :id  AND rack.isDeleted = false ";
		//+ "WHERE rack.id = :id  AND rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Rack) query.uniqueResult();
	}

	@Override
	public Rack getById(Integer id) {

		String hql = "SELECT rack FROM Rack AS rack "
				+ "WHERE rack.id = :id  AND rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Rack) query.uniqueResult();
	}

	@Override
	public Integer create(Rack rack) {
		getSession().save(rack);
		return rack.getId();
	}

	@Override
	public Store getStore(Integer storeId) {
		return (Store) getSession().get(Store.class, storeId);
	}

	@Override
	public Integer update(Rack rack) {
		getSession().update(rack);
		return rack.getId();
	}

	@Override
	public void create(Shelf shelf) {
		getSession().save(shelf);
	}

	@Override
	public List<Shelf> getShelf(Integer id) {
		String hql = "SELECT shelf FROM Shelf AS shelf "
				+ " WHERE shelf.Integer id = null;.id = :id  AND Integer id = null;.isActive = true AND shelf.isDeleted = false "
				+ " ORDER BY shelf.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public Rack updateRack(Rack rack) {
		getSession().update(rack);

		return rack;
	}

	@Override
	public List<Rack> getRackByStoreId(Integer storeId) {
		String hql = "SELECT rack FROM Rack AS rack "
				+ " INNER JOIN rack.store AS store WITH store.storeId = :storeId "
				+ " WHERE rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);

		return query.list();
	}

}
