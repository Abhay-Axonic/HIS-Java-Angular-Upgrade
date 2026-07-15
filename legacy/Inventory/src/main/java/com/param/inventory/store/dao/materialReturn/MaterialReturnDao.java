package com.param.inventory.store.dao.materialReturn;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import com.param.service.global.SessionService;

import ch.epfl.lamp.compiler.msil.util.Table.Constant;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;

import com.param.entity.model.inventory.StoreStock;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReturnIssue;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

@Repository("MaterialReturnDao")
public class MaterialReturnDao extends AbstractDao<Integer, Return> implements IMaterialReturnDao {

	private static final Logger logger = Logger.getLogger(MaterialReturnDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ReasonMaster> getReasonList() {
		String hql = "SELECT reasonMaster FROM ReasonMaster AS reasonMaster WHERE reasonMaster.status = 'A' AND reasonMaster.organizationId = :organizationId \r\n"
				+ "ORDER BY reasonMaster.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());

		return query.list();

	}

	@Override
	public List<MaterialResponse> searchMaterialReturnList(MaterialReurnRequest materialReurnRequest,
			Integer screenId) {

		String hql = "SELECT return.id AS id, return.addedDate AS returnDate, return.returnNo AS returnNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		return.nearByExpiry AS Type, return.isConsignment AS isConsignment, user.userName AS returnBy, approvalStatus.status AS approvalStatus, "
				+ "		return.remark AS remark, status.status AS status, return.nearByExpiry AS nearByExpiry "
				+ " FROM com.param.entity.model.inventory.Return AS return "
				+ "		INNER JOIN return.addedBy as user " + "		INNER JOIN return.fromStore AS fromStore "
				+ "		INNER JOIN return.toStore AS toStore " + "		INNER JOIN return.status AS status "
				+ "		INNER JOIN return.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE return.isActive = true AND return.isDeleted = false AND return.unit.unitId = :unitId "
				+ "	     AND date(return.addedDate) BETWEEN :fromDate AND :toDate "
				+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ "     AND (:toStoreId = 0 OR  toStore.storeId = :toStoreId ) "
				+ "		AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ "     AND (:returnNo = '' OR lower(return.returnNo) LIKE :returnNo) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) " + "ORDER BY return.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", materialReurnRequest.getFromDate());
		query.setParameter("toDate", materialReurnRequest.getToDate());
		query.setParameter("fromStoreId",
				materialReurnRequest.getFromStoreId() != null ? materialReurnRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				materialReurnRequest.getToStoreId() != null ? materialReurnRequest.getToStoreId() : 0);
		query.setParameter("statusId",
				materialReurnRequest.getStatusId() != null ? materialReurnRequest.getStatusId() : 0);
		query.setParameter("returnNo",
				materialReurnRequest.getReturnNo() != null ? '%'+materialReurnRequest.getReturnNo().toLowerCase() + '%'
						: "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", materialReurnRequest.getUnitId() != null ? materialReurnRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", materialReurnRequest.getUserId() != null ? materialReurnRequest.getUserId() : sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialResponse.class));

		return query.list();

	}

	@Override
	public Return saveMaterialReturn(Return returnData) {
		getSession().save(returnData);
		return returnData;
	}

	@Override
	public Return getMaterialReturn(Integer id) {
		String hql = "SELECT return FROM com.param.entity.model.inventory.Return AS return WHERE return.id = :id  AND return.isActive = true AND return.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Return) query.uniqueResult();
	}

	@Override
	public List<ReturnDetail> getMaterialReturndetail(Integer id) {
		String hql = "SELECT returnDetail FROM com.param.entity.model.inventory.ReturnDetail AS returnDetail \r\n"
				+ "	INNER JOIN returnDetail.mreturn AS return WITH return.isActive = true AND return.isDeleted = false \r\n"
				+ "WHERE return.id = :id AND returnDetail.isActive = true AND returnDetail.isDeleted = false \r\n"
				+ "ORDER BY return.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public Return updateMaterialReturn(Return returnData) {
		getSession().update(returnData);

		return returnData;
	}

	@Override
	public List<MaterialReturnIssue> getMaterialReturnIssue(PatientIssueReturnRequest patientIssueReturnRequest) {

	/*	String sql = " select issue.id as issueId, issue.issue_no as issueNo, issue.added_date as isueDate, details.id as issueDetailsId , details.item_id as itemid \r\n"
				+ "from inventory.t_material_issue_detail details join inventory.t_material_issue issue on details.material_issue_id=issue.id"
				+ " where issue.from_store_id=:from_store_id and issue.to_store_id=:to_store_id  and issue.added_date between :fromDate and  :toDate  and (:issueNumber = '' OR issue.issue_no LIKE :issueNumber) ";
*/
		String sql = " select issue.id as issueId, issue.issue_no as issueNo, issue.added_date as isueDate  "
				+ "from inventory.t_material_issue issue "
				+ " where issue.from_store_id=:from_store_id and issue.to_store_id=:to_store_id  and issue.added_date between :fromDate and  :toDate  and (:issueNumber = '' OR issue.issue_no LIKE :issueNumber) "
				/*start code for resolved HIS-10957 Item Details Not Getting Display on Material Return and Material Return Acceptance*/ 
				+ " and issue.status_id in (18,8)";
				/*end code for resolved HIS-10957 Item Details Not Getting Display on Material Return and Material Return Acceptance*/ 

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("from_store_id", patientIssueReturnRequest.getStoreId());
		query.setParameter("to_store_id", patientIssueReturnRequest.getToStoreId());
		query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
		query.setParameter("issueNumber",patientIssueReturnRequest.getIssueNo() != null ? patientIssueReturnRequest.getIssueNo().toUpperCase() + '%' : "");
		/*start code for resolved HIS-10957 Item Details Not Getting Display on Material Return and Material Return Acceptance*/ 
//		query.setParameter("statusId",com.param.common.constants.Status.Closed.getStatus());
		/*end code for resolved HIS-10957 Item Details Not Getting Display on Material Return and Material Return Acceptance*/ 
		query.setResultTransformer(Transformers.aliasToBean(MaterialReturnIssue.class));
		return query.list();

	}

	@Override
	public List<MaterialIssueDetail> getMaterialIssuedetail(Integer id) {
		String hql = "SELECT returnDetail FROM MaterialIssueDetail AS returnDetail \r\n"
				+ "	INNER JOIN returnDetail.materialIssue AS return WITH return.isActive = true AND return.isDeleted = false \r\n"
				+ "WHERE return.id = :id AND returnDetail.isActive = true AND returnDetail.isDeleted = false \r\n"
				+ "ORDER BY returnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<MaterialIssueBatchMapper> getMaterialIssueBatchMapper(Integer id, Integer id2) {

		String hql = "SELECT returnDetail FROM MaterialIssueBatchMapper AS returnDetail \r\n"
				+ "	INNER JOIN returnDetail.materialIssueDetail AS return WITH return.isActive = true AND return.isDeleted = false \r\n"
				+ "WHERE return.id = :id AND returnDetail.isActive = true AND returnDetail.isDeleted = false \r\n"
				+ "ORDER BY returnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();

	}

	@Override
	public List<MaterialReturnIssue> getMaterialReturnIssueData(Integer id) {

		String sql = " select issue.id as issueId, issue.issue_no as issueNo, issue.added_date as isueDate, details.id as issueDetailsId , details.item_id as itemid \r\n"
				+ "			from inventory.t_material_issue_detail details join inventory.t_material_issue issue on details.material_issue_id=issue.id\r\n"
				+ "			join inventory.t_return_detail returndetail on returndetail.material_issuedetail_id = details.id\r\n"
				+ "	where returndetail.id=:id";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		// query.setParameter("to_store_id", patientIssueReturnRequest.getToStoreId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialReturnIssue.class));
		return query.list();

	}

	@Override
	public BigInteger getReturnQty(Integer id) {
		/*
		 * String sql
		 * =" SELECT sum(rejDetail.accepted_quantity) FROM inventory.t_reject_detail rejDetail join  inventory.t_return_detail returndetail \r\n"
		 * +
		 * "	on rejDetail.return_detail_id=returndetail.id join inventory.t_material_issue_detail details on returndetail.material_issuedetail_id=details.id\r\n"
		 * +
		 * "	join inventory.t_material_issue issue on details.material_issue_id=issue.id\r\n"
		 * + "	where details.id=1 ";
		 */

		BigInteger t = BigInteger.valueOf(0);
		String sql = " SELECT sum(rejDetail.accepted_quantity) FROM inventory.t_reject_detail rejDetail join  inventory.t_return_detail returndetail  on rejDetail.return_detail_id=returndetail.id join inventory.t_material_issue_detail details on returndetail.material_issuedetail_id=details.id join inventory.t_material_issue issue on details.material_issue_id=issue.id where details.id=:id ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}

	}
	
	/* start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */
	
	public Integer getRejectedQty(Integer id) {
		if(id != null) {
		String sql = " select rejected_quantity from inventory.t_material_issue_batch_mapper where material_issue_detail_id =:id order by id limit 1";
		
//		List<Integer> statusIds = new ArrayList<Integer>();
//		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
//		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
//		statusIds.add(com.param.common.constants.Status.Rejected.getStatus());
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
//		query.setParameterList("statusId", statusIds);
			return (Integer) query.uniqueResult();
			}else {
				return Integer.valueOf(0);
			}
			}
	
	
	
	public BigInteger getTotalReturnedQty(Integer id,Integer batchId,Integer detailsId) {
		if(id != null) {
		String sql = " SELECT Coalesce(SUM(returnDetail.accepted_qty),0) FROM inventory.t_return_detail AS returnDetail "
				+ " where returnDetail.material_issuedetail_id=:id AND returnDetail.status_id in (:statusId) AND returnDetail.batch_Id=:batchId ";
		
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.Rejected.getStatus());
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setParameter("batchId", batchId);
//		query.setParameter("detailsId", detailsId);
		query.setParameterList("statusId", statusIds);
			return (BigInteger) query.uniqueResult();
			}else {
				return BigInteger.valueOf(0);
			}
			}
	public BigInteger getTotalComsumedQty(Integer id,Integer batchId) {
		if(id != null) {
		String sql = " SELECT Coalesce(SUM(returnDetail.comsumed_qty),0) FROM inventory.t_return_detail AS returnDetail "
				+ " where returnDetail.material_issuedetail_id=:id AND returnDetail.status_id in (:statusId) AND returnDetail.batch_Id=:batchId ";
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.Rejected.getStatus());
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setParameter("batchId", batchId);
		query.setParameterList("statusId", statusIds);
			return (BigInteger) query.uniqueResult();
	}else {
		return BigInteger.valueOf(0);
	}
			}
	/* end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */

	@Override
	public List<MaterialIssueBatchMapper> getMaterialIssueExpireBatchMapper(Integer id,
			PatientIssueReturnRequest patientIssueReturnRequest) {
		LocalDate today = LocalDate.now();
		LocalDate threeMonthsLater = today.plusMonths(3);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String todaysDate = today.format(formatter);
		String threeMonthsDate = threeMonthsLater.format(formatter);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedCurrentDate = null;
		Date parsedThreeMonthsDate = null;
        try {
			parsedCurrentDate = dateFormat.parse(todaysDate);
			parsedThreeMonthsDate = dateFormat.parse(threeMonthsDate);
        } catch (ParseException e) {
            logger.error("Error while parsing material issue expire batch mapper date range", e);
            throw new ServiceException(e.getMessage());
        }

		java.sql.Date CurrentDate= new java.sql.Date(parsedCurrentDate.getTime());
		java.sql.Date ThreeMonthDate = new java.sql.Date(parsedThreeMonthsDate.getTime());

		String hql = "SELECT returnDetail FROM MaterialIssueBatchMapper AS returnDetail \r\n"
				+ "	INNER JOIN returnDetail.materialIssueDetail AS return WITH return.isActive = true AND return.isDeleted = false \r\n"
				+ " inner join returnDetail.batch as batch  "
				+ "WHERE return.id = :id AND returnDetail.isActive = true AND returnDetail.isDeleted = false  and batch.expiryDate >= :fromDate AND batch.expiryDate <= :toDate "
				+ "ORDER BY returnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", CurrentDate);
		query.setParameter("toDate", ThreeMonthDate);
		query.setParameter("id", id);

		return query.list();

	
	}

//	SELECT coalesce(SUM(returnDetail.return_qty)-(SUM(returnDetail.rejected_qty)+SUM(returndetail.accepted_qty)), 0) FROM inventory.t_return_detail AS returnDetail "
//	+ " where returnDetail.material_issuedetail_id=:id AND returnDetail.status_id in (:statusId)
	
	
//	initial SELECT coalesce(SUM(returnDetail.return_qty), 0) FROM inventory.t_return_detail AS returnDetail "
//+ " where returnDetail.material_issuedetail_id=:id AND returnDetail.status_id in (:statusId)";
	
//	" inner join inventory.t_reject_detail as trd on " + 
//	" trd.return_detail_id = returnDetail.return_id "+
	@Override
	public BigInteger getTotalTransiQty(Integer id,Integer batchId) {

		if(id != null) {
		String sql = " SELECT coalesce(SUM(returnDetail.return_qty), 0) FROM inventory.t_return_detail AS returnDetail "
				+ " where returnDetail.material_issuedetail_id=:id AND returnDetail.status_id in (:statusId) AND returnDetail.batch_Id=:batchId ";


		List<Integer> statusIds = new ArrayList<Integer>();
//		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.Rejected.getStatus());
		
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setParameter("batchId", batchId);
		query.setParameterList("statusId", statusIds);
			return (BigInteger) query.uniqueResult();
			}else {
				return BigInteger.valueOf(0);
			}
			
	}

	
	@Override
	public Return getReturn(Integer id) {
		String hql = " SELECT return FROM com.param.entity.model.inventory.Return AS return "
				+ "	WHERE return.id = :id AND return.isActive = true AND return.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Return) query.uniqueResult();
	}
	
	@Override
	public Return updateReturn(Return returnData) {
		getSession().update(returnData);
		return returnData;
	}

	@Override
	public Reject save(Reject reject) {
		getSession().save(reject);
		return reject;	
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {
		getSession().save(storeStock);
		return storeStock;
	}

    @Override
    public Response getMaterialReturnDetails1(List<Integer> ids, Integer fromStoreId, Integer toStoreId) {
        String sql =" select count(trd.id) from inventory.t_return_detail trd " + 
        		" inner join inventory.t_return tr on tr.id = trd.return_id " + 
        		" where trd.material_issuedetail_id in (:ids) and tr.status_id =1 and tr.from_store_id =:fromStoreId and tr.to_store_id =:toStoreId";
        
        Query query = getSession().createSQLQuery(sql);
        query.setParameter("fromStoreId", fromStoreId);
        query.setParameter("toStoreId", toStoreId);
        query.setParameterList("ids", ids);
        BigInteger count=(BigInteger)query.uniqueResult();
        
        return new Response("success", "200", null, null, count);
    }

	@Override
	public Integer materialReturnStatus(Integer id) {
		CommonDto idStatus = (CommonDto) getSessionFactory().getCurrentSession()
                .createSQLQuery("    select status_id as \"id\" from inventory.t_return  where  is_active = true and is_deleted = false and id ="+id)
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).uniqueResult();
        return idStatus.getId();
	}

	@Override
	 public Integer getAcceptedQty(Integer materialIssueDetailId,Integer batchId,Integer id) {
		if(materialIssueDetailId != null) {
			 String sql = " select issue_qty from inventory.t_return_detail where material_issuedetail_id =:id and batch_id =:batchId and id=:detailId order by id limit 1";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter("id", materialIssueDetailId);
			query.setParameter("batchId", batchId);
			query.setParameter("detailId", id);
				return (Integer) query.uniqueResult();
				}else {
					return Integer.valueOf(0);
				}
	}

	@Override
	public List<MaterialIssueDetail> getMaterialIssuedetailAll(List<Integer> issueIds) {

		if (issueIds == null || issueIds.isEmpty()) {
			// Handle the case where issueIds is empty
			return Collections.emptyList();
		}

		String hql = "SELECT returnDetail FROM MaterialIssueDetail AS returnDetail "
				+ "	INNER JOIN returnDetail.materialIssue AS return WITH return.isActive = true AND return.isDeleted = false "
				+ "WHERE return.id in(:id) AND returnDetail.isActive = true AND returnDetail.isDeleted = false "
				+ "ORDER BY returnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", issueIds);

		return query.list();
	}


}
