package com.param.linenLaundry.linenReturnAcceptance.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.linenAndLaundry.LinenReturn;
import com.param.entity.model.linenAndLaundry.LinenReturnDetail;
import com.param.entity.model.linenAndLaundry.RejectedAcceptance;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;

@Repository
public class LinenReturnAcceptanceDao extends AbstractDao<Integer, LinenReturn> implements ILinenReturnAcceptanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LinenResponse> searchLinenReturnAcceptance(LinenRequest request, Integer screenId) {
	
		String hql = " SELECT linenReturn.id AS id, fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStoreName, "
				+ "     toStore.storeId AS toStoreId, toStore.storeName AS toStoreName, linenReturn.remark AS remark, "
				+ "     status.id AS statusId, status.status AS status, linenReturn.returnNo AS returnNo, "
				+ "		linenReturn.addedDate AS returnDate, user.userName AS returnBy, user.userId AS returnById "
				+ "		FROM LinenReturn AS linenReturn "
				+ "		INNER JOIN linenReturn.addedBy as user WITH user.status = 'A' "
				+ "		LEFT JOIN linenReturn.fromStore as fromStore "
				+ "		LEFT JOIN linenReturn.toStore as toStore WITH toStore.isActive = true AND toStore.isDeleted = false " 
				+ "		LEFT JOIN linenReturn.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		WHERE linenReturn.isActive = true AND linenReturn.isDeleted = false AND linenReturn.unit.unitId = :unitId "
				+ " 	AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
				+ "		AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "									WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 	AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
				+ " 	AND (:returnNo = '' OR lower(linenReturn.returnNo) LIKE :returnNo) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY linenReturn.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromStoreId",
				request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("returnNo", request.getReturnNo() != null ? request.getReturnNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("userId", sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(LinenResponse.class));
		return query.list();
	}

	@Override
	public LinenReturn saveLinenReturnAcceptance(LinenReturn linenReturn) {
		getSession().update(linenReturn);

		return linenReturn;
	}
	
	@Override
	public LinenResponse getLinenReturnAcceptance(Integer id)
	{

		String hql = " SELECT linenReturn.id AS id, fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStoreName, "
				+ "     toStore.storeId AS toStoreId, toStore.storeName AS toStoreName, "
				+ "     status.id AS statusId, status.status AS status, linenReturn.returnNo AS returnNo, linenReturn.addedDate AS returnDate "
				+ "		FROM LinenReturn AS linenReturn "
				+ "		LEFT JOIN linenReturn.fromStore as fromStore "
				+ "		LEFT JOIN linenReturn.toStore as toStore WITH toStore.isActive = true AND toStore.isDeleted = false " 
				+ "		LEFT JOIN linenReturn.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		WHERE linenReturn.id = :id AND linenReturn.isActive = true AND linenReturn.isDeleted = false AND linenReturn.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(LinenResponse.class));
		return (LinenResponse) query.uniqueResult();
	}
	
	@Override
	public LinenReturn getLinenReturnAcceptanceId(Integer id)
	{
		String hql = " SELECT linenReturn FROM LinenReturn AS linenReturn WHERE linenReturn.id = :id AND linenReturn.isActive = true "
				+ "    AND linenReturn.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (LinenReturn) query.uniqueResult();
	}
	
	@Override
	public LinenReturnDetail getLinenReturnDetail(Integer id)
	{
		String hql = " SELECT linenReturnDetail FROM LinenReturnDetail AS linenReturnDetail WHERE linenReturnDetail.id = :id AND linenReturnDetail.isActive = true "
				+ "    AND linenReturnDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (LinenReturnDetail) query.uniqueResult();
	}
	
	@Override
	public List<LinenReturnDetail> getLinenReturnAcceptanceDetail(Integer linenReturnId)
	{
		String hql = " SELECT linenReturnDetail FROM LinenReturnDetail AS linenReturnDetail \r\n"
				+ " INNER JOIN linenReturnDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "	INNER JOIN linenReturnDetail.linenReturn AS linenReturn WITH linenReturn.isActive = true AND linenReturn.isDeleted = false \r\n"
				+ "	WHERE linenReturn.id = :id AND linenReturnDetail.isActive = true AND linenReturnDetail.isDeleted = false \r\n"
				+ "	ORDER BY linenReturnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", linenReturnId);

		return query.list();
	}
	
	

	@Override
	public LinenReturn updateLinenReturnAcceptance(LinenReturn linenReturn) {
		getSession().update(linenReturn);

		return linenReturn;
	}
	
	@Override
	public RejectedAcceptance saveRejectAcceptance(RejectedAcceptance rejectAcceptance)
	{
		getSession().save(rejectAcceptance);

		return rejectAcceptance;
	}
}