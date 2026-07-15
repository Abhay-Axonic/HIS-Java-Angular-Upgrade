package com.param.pharmacy.op.dao.salesReturn;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Return;
import com.param.entity.model.pharmacy.ReturnDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.op.dto.SalesWorkQty;
import com.param.pharmacy.op.dto.salesReturn.ItemDetails;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnDto;
import com.param.pharmacy.op.dto.salesReturn.ResponseAddBill;
import com.param.pharmacy.op.dto.salesReturn.ResponseReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.ResponseSalesReturnBillList;
import com.param.pharmacy.op.dto.salesReturn.ReturnDetails;
import com.param.pharmacy.op.dto.salesReturn.ReturnItem;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnBillListSerach;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnQtyDetail;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Repository("salesReturnDao")
public class SalesReturnDao extends AbstractDao<Integer, Return> implements ISalesReturnDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(SalesReturnDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Return> salesReturnListSearch(SalesReturnListSearch salesReturnList, Integer screenId) {
		try {
			String hql = " SELECT return FROM Return AS return "
					+ " INNER JOIN return.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId  "
					+ " LEFT JOIN sale.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN sale.tempPatient AS tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false "
					+ " LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
					+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
					+ " LEFT JOIN tempPatient.gender WITH gender.status = 'A' "
					+ " INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE return.isActive = true AND return.isDeleted = false "
					+ " AND return.addedDate BETWEEN  :fromDate AND :toDate "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR tempPatient.id = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName OR lower(patient.lastName) LIKE :patientName "
					+ "	OR lower(tempPatientPrefix.prefixCode) LIKE :patientName OR lower(tempPatient.patientName) LIKE :patientName OR lower(COALESCE(CONCAT(tempPatientPrefix.prefixCode,' ', tempPatient.patientName))) LIKE :patientName )) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) "
					+ " AND (:returnNo = '' OR lower(return.returnNo) LIKE :returnNo)" + " ORDER BY return.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(salesReturnList.getFromDate()));
			query.setParameter("toDate", Util.getDate(salesReturnList.getToDate()));
			query.setParameter("patientId",
					salesReturnList.getPatientId() != null ? salesReturnList.getPatientId() : 0);
			query.setParameter("patientName",
					salesReturnList.getPatientName() != null ? salesReturnList.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("uhid",
					salesReturnList.getUhid() != null ? salesReturnList.getUhid().toLowerCase() + '%' : "");
			query.setParameter("returnNo",
					salesReturnList.getReturnNo() != null ? salesReturnList.getReturnNo().toLowerCase() + '%' : "");
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Return salesReturnById(Integer id, Integer unitId) {

		try {
			String hql = " SELECT return FROM Return AS return "
					+ " WHERE ( return.id = :id ) AND return.isActive = true AND return.isDeleted = false "
					+ " AND ( return.unit.unitId = :unitId ) ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

			return (Return) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<ReturnDetail> salesReturnDetailsById(Integer id) {
		try {
			String hql = " SELECT returnDetail FROM ReturnDetail AS  returnDetail "
					+ " INNER JOIN returnDetail.returnId AS returnId WITH returnId.isActive = true AND returnId.isDeleted = false "
					+ " INNER JOIN returnId.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN returnId.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false "
					+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " INNER JOIN saleDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isReturn = false "
					+ " INNER JOIN saleDetail.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
					+ " LEFT JOIN sale.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN sale.tempPatient AS tempPatient WITH tempPatient.isActive = true OR tempPatient.isDeleted = false "
					+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
					+ " LEFT JOIN tempPatient.gender WITH gender.status = 'A' "
					+ " WHERE ( returnDetail.returnId.id = :id ) AND returnDetail.isActive = true AND returnDetail.isDeleted = false   ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<Sale> searchSalesReturnBills(SalesReturnBillListSerach salesReturnBillListSerach, Integer screenId) {
		Date current = TimeZoneComponent
				.getDateByzone(salesReturnBillListSerach.getUnitId() != null ? salesReturnBillListSerach.getUnitId()
						: sessionService.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT sale FROM Sale AS sale "
				+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false AND saleType.unit.unitId = :unitId "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN sale.addedBy as user WITH user.status = 'A' AND user.unitId = :unitId "
				+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A' AND patient.unitId = :unitId "
				+ " 	LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false AND tempPatient.unit.unitId = :unitId "
				+ " 	LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
				+ " 	WHERE sale.id IN ( SELECT sale1.id FROM Sale AS sale1 INNER JOIN sale1.saleDetailsList AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ "							INNER JOIN saleDetail.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isReturn = false AND batch.expiryDate > :greaterThanCurrentMonth AND "
				+ "							batch.isQuarantine = false AND batch.isPharmacy = false "
				+ "							WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false ) "
				+ "		AND sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId "
				+ "		AND sale.addedDate BETWEEN  :fromDate AND :toDate "
				+ " 	AND (:saleTypeId = 0 OR saleType.id = :saleTypeId) "
				+ " 	AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) "
				+ " 	AND ( :patientId = 0 OR patient.patientId = :patientId OR tempPatient.id = :patientId ) "
				+ "		AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " 	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName OR lower(patient.lastName) LIKE :patientName "
				+ "		OR lower(tempPatientPrefix.prefixCode) LIKE :patientName OR lower(tempPatient.patientName) LIKE :patientName OR lower(COALESCE(CONCAT(tempPatientPrefix.prefixCode,' ', tempPatient.patientName))) LIKE :patientName )) "
				+ "		ORDER BY sale.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(salesReturnBillListSerach.getFromDate()));
		query.setParameter("toDate", Util.getDate(salesReturnBillListSerach.getToDate()));
		query.setParameter("saleTypeId",
				salesReturnBillListSerach.getSaleTypeId() != null ? salesReturnBillListSerach.getSaleTypeId() : 0);
		query.setParameter("uhid",
				salesReturnBillListSerach.getUhid() != null ? salesReturnBillListSerach.getUhid().toLowerCase() + '%'
						: "");
		query.setParameter("patientName",
				salesReturnBillListSerach.getPatientName() != null
						? salesReturnBillListSerach.getPatientName().toLowerCase() + '%'
						: "");
		query.setParameter("patientId",
				salesReturnBillListSerach.getPatientId() != null ? salesReturnBillListSerach.getPatientId() : 0);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("unitId",
				salesReturnBillListSerach.getUnitId() != null ? salesReturnBillListSerach.getUnitId()
						: sessionService.getUnitId());

		return query.list();
	}

	@Override
	public Sale salesReturnGetBillById(Long id) {
		Date current = TimeZoneComponent.getDateByzone(sessionService.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = " SELECT sale FROM Sale AS sale "
				+ " INNER JOIN sale.saleDetailsList AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ " WHERE saleDetail.id IN ( SELECT saleDetail FROM SaleDetail AS saleDetail INNER JOIN saleDetail.batch AS batch WITH "
				+ " batch.isDeleted = false AND batch.isActive = true AND batch.isReturn = false AND batch.isQuarantine = false "
				+ " AND batch.expiryDate > :greaterThanCurrentMonth WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false ) "
				+ "	AND sale.id = :id AND sale.isActive = true AND sale.isDeleted = false "
				+ " AND saleDetail.isActive = true AND saleDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("greaterThanCurrentMonth", current);
		return (Sale) query.uniqueResult();
	}

	@Override
	public List<ReturnDetail> populateReturnDetail(Long saleDetailId) {
		String hql = " SELECT returnDetail FROM ReturnDetail AS returnDetail "
				+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ " WHERE returnDetail.isActive = true AND returnDetail.isDeleted = false AND saleDetail.id = :saleDetailId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("saleDetailId", saleDetailId);
		return query.list();
	}

	@Override
	public Long getReturnedQuantity(Long id) {
		try {
			String hql = "SELECT COALESCE(SUM(returnDetail.returnQty), 0) FROM ReturnDetail AS returnDetail "
					+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ "	INNER JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE returnDetail.isActive = true AND returnDetail.isDeleted = false  AND prescriptionDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return (query.uniqueResult() != null ? (long) query.uniqueResult() : 0L);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Return salesReturnDetailsSave(Return returnDetail) {
		getSession().save(returnDetail);
		return returnDetail;
	}

	@Override
	public SaleDetail getSaleDetailById(Long id, Integer unitId) {
		try {
			String hql = " SELECT saleDetail FROM SaleDetail AS saleDetail "
					+ " WHERE ( saleDetail.id = :id ) AND saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " AND ( saleDetail.unit.unitId = :unitId ) ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId !=  null ? unitId :sessionService.getUnitId());

			return (SaleDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<SalesWorkQty> getReturnedQuantityBP(List<Long> itemIds, Integer unitId) {
		String hql = "SELECT COALESCE(SUM(returnDetail.returnQty), 0) as qty, " + " prescriptionDetail.id as detailId "
				+ " FROM ReturnDetail AS returnDetail "
				+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ "	INNER JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " WHERE returnDetail.isActive = true AND returnDetail.isDeleted = false AND returnDetail.unit.unitId = :unitId AND (prescriptionDetail.id in (:id)) "
				+ " GROUP BY prescriptionDetail.id";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", itemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(SalesWorkQty.class));
		return query.list();
	}

	@Override
	public List<ResponseReturnListSearch> salesReturnListSearchBP(SalesReturnListSearch salesReturnList,
			Integer screenId) {
		try {
			String hql = " SELECT patient.id as patientId, tempPatient.id as tempPatId, patient.dob as patientDob, "
					+ " CONCAT(COALESCE(prefix.prefixCode, ' '), ' ', patient.firstName, ' ', COALESCE(patient.middleName, ' '), ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
					+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName, ' | ',tempPatient.age, ' | ',tempPatGender.genderCode) AS tempPatientDatail, "
					+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName) AS tempPatientDatails, "
					+ " return.id as id, return.addedDate as returnDate, return.returnNo as returnNo, patient.uhidNumber as uhid,"
					+ " sale.billNo as billNo, sale.addedDate as billDate, return.totalReturnAmt as returnAmount, returnStatus.id as statusId, "
					+ " gender.genderId as genderId, " + " patient.imagePath as imagePath, "
					+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''))AS patientName, "
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as ageString, "
					+ " gender.genderCode as genderCode, "
					+ " returnStatus.status as status, user.userName as createdBy, return.totalreturnPaidAmount as returnPaidAmount,sale.store.id as storeId , "
					+ "tempPatient.tempUhid as tempUhid "
					+ " FROM Return AS return "
					+ " INNER JOIN return.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId "
					+ " INNER JOIN return.status AS returnStatus "
					+ "	INNER JOIN return.addedBy as user WITH user.status = 'A'  "
					+ " LEFT JOIN sale.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN sale.tempPatient AS tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false "
					+ " LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
					+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
					+ " LEFT JOIN tempPatient.gender AS tempPatGender WITH tempPatGender.status = 'A' "
					+ " INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE (return.isActive = true AND return.isDeleted = false) "
					+ " AND (return.addedDate BETWEEN  :fromDate AND :toDate) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR tempPatient.id = :patientId ) "
					+ "	AND ( :patientName = '' or  lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName ) "
					// + " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName)
					// LIKE :patientName OR lower(patient.middleName) LIKE :patientName OR
					// lower(patient.lastName) LIKE :patientName "
					// + " OR lower(tempPatientPrefix.prefixCode) LIKE :patientName OR
					// lower(tempPatient.patientName) LIKE :patientName OR
					// lower(COALESCE(CONCAT(tempPatientPrefix.prefixCode,' ',
					// tempPatient.patientName))) LIKE :patientName )) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) AND sale.store.id=:storeId  ";
			// + " AND return.updatedDate BETWEEN :fromDate AND :toDate "

			StringBuffer sb = new StringBuffer(hql);

			sb.append((salesReturnList.getReturnNo() != null && !salesReturnList.getReturnNo().isEmpty())
					? " AND lower(return.returnNo) LIKE '%" + salesReturnList.getReturnNo().toLowerCase() + "%'"
					: " ");
			sb.append(" ORDER BY return.id DESC ");

			Query query = getSession().createQuery(sb.toString());
			query.setParameter("fromDate", Util.getDate(salesReturnList.getFromDate()));
			query.setParameter("toDate", Util.getDate(salesReturnList.getToDate()));
			query.setParameter("patientId",
					salesReturnList.getPatientId() != null ? salesReturnList.getPatientId() : 0);
			query.setParameter("patientName",
					salesReturnList.getPatientName() != null
							? '%' + salesReturnList.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("uhid",
					salesReturnList.getUhid() != null ? '%' + salesReturnList.getUhid().toLowerCase() + '%' : "");

			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("storeId", salesReturnList.getStoreId());
			query.setResultTransformer(Transformers.aliasToBean(ResponseReturnListSearch.class));
			List<ResponseReturnListSearch> list = query.list();
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
//	@Override
//	public List<ResponseReturnListSearch> salesReturnListSearchBP(SalesReturnListSearch salesReturnList,
//			Integer screenId) {
//		try 
//		{
//			String hql = " SELECT DISTINCT (returnId.id) as id, patient.id as patientId, tempPatient.id as tempPatId, patient.dob as patientDob, "
//					+ " CONCAT(COALESCE(prefix.prefixCode, ' '), ' ', patient.firstName, ' ', COALESCE(patient.middleName, ' '), ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
//					+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName, ' | ',tempPatient.age, ' | ',tempPatGender.genderCode) AS tempPatientDatail, "
//					+ " returnId.id as id, returnId.addedDate as returnDate, returnId.returnNo as returnNo, patient.uhidNumber as uhid,"
//					+ " sale.billNo as billNo, sale.addedDate as billDate, returnId.totalReturnAmt as returnAmount, returnStatus.id as statusId, "
//					+ " returnStatus.status as status ,"
//					+ " saleDetail.rate as rate,saleDetail.totalAmount as totalAmount,saleDetail.discountAmount as discountAmount,"
//					+ " saleDetail.netAmount as netAmount,returnDetail.returnQty as returnQuantity,returnDetail.returnTaxAmount as returnTaxAmount,user.userName as createdBy  "
//					+ " FROM ReturnDetail AS returnDetail "
//					+ " INNER JOIN returnDetail.returnId AS returnId WITH returnId.isActive = true AND returnId.isDeleted = false "
//					+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
//					+ " INNER JOIN returnId.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId  "
//					+ " INNER JOIN returnId.status AS returnStatus "
//					+ " LEFT JOIN sale.patient AS patient WITH patient.status = 'A' "
//					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
//					+ " LEFT JOIN sale.tempPatient AS tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false "
//					+ " LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
//					+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
//					+ " LEFT JOIN tempPatient.gender AS tempPatGender WITH tempPatGender.status = 'A' "
//					+ " INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
//					+ "	INNER JOIN sale.addedBy as user WITH user.status = 'A' AND user.unitId = :unitId  "
//					+ " WHERE returnId.isActive = true AND returnId.isDeleted = false "
//					+ " AND returnId.addedDate BETWEEN  :fromDate AND :toDate "
//					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR tempPatient.id = :patientId ) "
//					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
//					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName OR lower(patient.lastName) LIKE :patientName "
//					+ "	OR lower(tempPatientPrefix.prefixCode) LIKE :patientName OR lower(tempPatient.patientName) LIKE :patientName OR lower(COALESCE(CONCAT(tempPatientPrefix.prefixCode,' ', tempPatient.patientName))) LIKE :patientName )) "
//					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) "
//					+ " AND (:returnNo = '' OR lower(returnId.returnNo) LIKE :returnNo)" + " ORDER BY returnId.id DESC ";
//
//			Query query = getSession().createQuery(hql);
//			query.setParameter("fromDate", Util.getDate(salesReturnList.getFromDate()));
//			query.setParameter("toDate", Util.getDate(salesReturnList.getToDate()));
//			query.setParameter("patientId",
//					salesReturnList.getPatientId() != null ? salesReturnList.getPatientId() : 0);
//			query.setParameter("patientName",
//					salesReturnList.getPatientName() != null ? salesReturnList.getPatientName().toLowerCase() + '%'
//							: "");
//			query.setParameter("uhid",
//					salesReturnList.getUhid() != null ? salesReturnList.getUhid().toLowerCase() + '%' : "");
//			query.setParameter("returnNo",
//					salesReturnList.getReturnNo() != null ? salesReturnList.getReturnNo().toLowerCase() + '%' : "");
//			query.setParameter("unitId", sessionService.getUnitId());
//			query.setResultTransformer(Transformers.aliasToBean(ResponseReturnListSearch.class));
//			return query.list();
//		} catch (Exception e) {
//			
//		}
//
//		return null;
//	}

	@Override
	public ReturnDetails salesReturnByIdBP(Integer id, Integer unitId) {
		try {
			String hql = " SELECT patient.id as patientId, tempPatient.id as tempPatId, patient.dob as patientDob, "
					+ " CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
					+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName, ' | ',tempPatient.age, ' | ',tempPatGender.genderCode) AS tempPatientDatail, "
					+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName) AS tempPatientDatail1, "
					+ " return.id as id, patient.uhidNumber as uhid, return.addedDate as retunDate, return.returnNo as returnNo, "
					+ " store.storeId as storeId, store.storeName as storeName, returnStatus.id as statusId, "
					+ " gender.genderId As genderId , patient.imagePath as imagePath , tempPatGender.genderId As tempGenderId,"
					+ " returnStatus.status as status, return.totalReturnAmt as totalRefundAmount, return.totalreturnPaidAmount as totalreturnPaidAmount , tempPatient.tempUhid as tempUhid  "
					+ " FROM Return AS return "
					+ " INNER JOIN return.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId  "
					+ " INNER JOIN return.status AS returnStatus " + " INNER JOIN return.store store "
					+ " LEFT JOIN sale.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN sale.tempPatient AS tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false "
					+ " LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
					+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
					+ " LEFT JOIN tempPatient.gender AS tempPatGender WITH tempPatGender.status = 'A' "
					+ " WHERE ( return.id = :id ) AND return.isActive = true AND return.isDeleted = false "
					+ " AND ( return.unit.unitId = :unitId ) ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(ReturnDetails.class));
			return (ReturnDetails) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ReturnItem> salesReturnDetailsByIdBP(Integer id, Integer unitId) {

		try {
			String hql = " SELECT batch.id as batchId, batch.expiryDate as batchExpiryDate, returnDetail.id as returnDetailId, item.name as itemName, item.isThreeUOMApplicable as isThreeUOMApplicable, "
					+ " generic.name as genericName, batch.batchNo as batchNo, uomUnit.units as uomUnit, saleDetail.quantity as quantityIssued, "
					+ " saleDetail.rate as rate, saleDetail.totalAmount as totalAmount, saleDetail.discountAmount as discountAmount, "
					+ " saleDetail.netAmount as netAmount, returnDetail.returnQty as returnQuantity, returnDetail.returnAmt as refundAmount, "
					+ " returnDetail.remark as reason, returnDetail.returnTaxAmount as returnTaxAmount, generic.id as genericId ,returnDetail.saleId as saleId, sale.billNo as billNo  "
					+ " FROM ReturnDetail AS  returnDetail "
					+ " INNER JOIN returnDetail.returnId AS returnId WITH returnId.isActive = true AND returnId.isDeleted = false "
					+ " INNER JOIN returnId.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN returnId.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false "
					+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " LEFT JOIN saleDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isReturn = false "
					+ " LEFT JOIN saleDetail.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " INNER JOIN saleDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
					+ " LEFT JOIN sale.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN sale.tempPatient AS tempPatient WITH tempPatient.isActive = true OR tempPatient.isDeleted = false "
					+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
					+ " LEFT JOIN tempPatient.gender WITH gender.status = 'A' "
					+ " WHERE ( returnDetail.returnId.id = :id ) AND returnDetail.isActive = true AND returnDetail.isDeleted = false "
					+ " AND ( returnDetail.unit.unitId = :unitId ) ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(ReturnItem.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResponseSalesReturnBillList> searchSalesReturnBillsBP(
			SalesReturnBillListSerach salesReturnBillListSerach, Integer screenId, List<Integer> statusList) {
		Date current = TimeZoneComponent
				.getDateByzone(salesReturnBillListSerach.getUnitId() != null ? salesReturnBillListSerach.getUnitId()
						: sessionService.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		StringBuilder sb = new StringBuilder();

			sb.append("""
				select  distinct sale0_.id AS "saleId",
					sale0_.added_date AS "billDate",
					(case when mbm.bill_number is not null
					then mbm.bill_number else sale0_.bill_no end ) as "billNo",
					(case when patientreg4_.patient_id is not null then
						patientreg4_.uhid_number else temppatien6_.temp_uhid end ) as "uhid",
					coalesce(temppatien6_.age,0 ) as "age",
					sale0_.net_amount AS "billAmount",
					saletype1_.id AS "saleTypeId",
					admission8_.admission_id AS "admissionId",
					admission8_.created_date AS "admissionDate",
					admission8_.admission_number AS "admissionNumber",
					encounter9_.encounter_id AS "encounterId",
					encounter9_.created_date AS "encounterDate",
					encounter9_.encounter_number AS "encounterNumber",
					patientreg4_.dob AS "patientDob",
					(case when patientreg4_.patient_id is not null then (
					prefix5_.prefix_code || ' ' || patientreg4_.first_name || ' ' || coalesce(patientreg4_.middle_name,'' )  || ' ' || coalesce(patientreg4_.last_name ,'' )|| ' | ' || gender10_.gender_code)
						else (prefix7_.prefix_code || ' ' || temppatien6_.patient_name || ' | ' ||coalesce(temppatien6_.age,0 ) || ' | ' || gender11_.gender_code) end )as "patientDetail",
					patientreg4_.patient_id AS "patientId",
					 (select ti.issue_no from  pharmacy.t_issue ti where ti.sale_id  = sale0_.id limit 1) as "issueNo",
					temppatien6_.id AS "tempPatId"
					 from pharmacy.t_sale sale0_
					 INNER JOIN pharmacy.m_sale_type saletype1_ on sale0_.sale_type_id = saletype1_.id
					 AND (saletype1_.is_active = TRUE
					 AND saletype1_.is_deleted = FALSE)
					 INNER JOIN public.m_status status2_ on sale0_.status_id = status2_.id
					 AND (status2_.is_active = TRUE
					 AND status2_.is_deleted = FALSE
					 AND (status2_.id IN (:status)))
					 left outer join billing.m_billing_master mbm
					    on mbm.billing_master_id  = sale0_.bill_id and mbm.bill_cancelled  = 'N'
					 left outer join billing.t_bill_payee_details tbpd
					    on mbm.billing_master_id  = tbpd.bill_id
					 LEFT OUTER JOIN patient.t_patient_registration patientreg4_ on sale0_.patient_id = patientreg4_.patient_id
					 AND (patientreg4_.status = 'A'
					 )
					 LEFT OUTER JOIN public.m_prefix_master prefix5_ on patientreg4_.prefix_id = prefix5_.prefix_id
					 AND (prefix5_.status = 'A')
					 LEFT OUTER JOIN public.m_gender_master gender10_ on patientreg4_.gender_id = gender10_.gender_id
					 AND (gender10_.status = 'A')
					 LEFT OUTER JOIN pharmacy.t_temp_patient_registration temppatien6_ on sale0_.temp_patient_id = temppatien6_.id
					 AND (temppatien6_.is_active = TRUE
					 AND temppatien6_.is_deleted = FALSE
					 AND temppatien6_.unit_id =:unitId)
					 LEFT OUTER JOIN public.m_prefix_master prefix7_ on temppatien6_.prefix_id = prefix7_.prefix_id
					 AND (prefix7_.status = 'A')
					 LEFT OUTER JOIN public.m_gender_master gender11_ on temppatien6_.gender_id = gender11_.gender_id
					 AND (gender11_.status = 'A')
					 LEFT OUTER JOIN adt.t_admission admission8_ on sale0_.admission_id = admission8_.admission_id
					 LEFT OUTER JOIN opd.t_encounter_master encounter9_ on sale0_.encounter_id = encounter9_.encounter_id
					 LEFT OUTER JOIN
					 	(
					 	select sum(total_return_amt)as return_amt,sale_id from
					 	pharmacy.t_return where status_id not in(9) group by sale_id having sum(total_return_amt) > 0
					 	) temp
					 	on temp.sale_id = sale0_.id
					 where
					 sale0_.total_self_payable > coalesce(temp.return_amt,0)
					AND sale0_.is_active = TRUE AND sale0_.is_deleted = FALSE AND sale0_.unit_id =:unitId
					 and (sale0_.added_date between :fromDate and :toDate )
					 AND sale0_.is_from_billable_consumption = false
					 AND sale0_.store_id =:storeId
				"""
					);
		
		
		if (salesReturnBillListSerach.getSaleTypeId() != null && salesReturnBillListSerach.getSaleTypeId() != 0) {
			sb.append(" AND saletype1_.id = :saleTypeId ");
			sb.append(" AND (CASE saletype1_.id WHEN 1 THEN (sale0_.bill_id IS NOT NULL) ELSE saletype1_.id = :saleTypeId END) ");
			sb.append(" AND COALESCE(tbpd.due_amount, 0) = 0 ");
		}else {
			sb.append(" and (case saletype1_.id when 1 then (sale0_.bill_id is not null) else saletype1_.id in (2, 3, 4)end) and coalesce(tbpd.due_amount, 0) = 0" );

		}
		if (salesReturnBillListSerach.getUhid() != null) {
			sb.append(" AND LOWER(patientreg4_.uhid_number) LIKE CONCAT('%', LOWER(:uhid), '%') ");
		}
		if (salesReturnBillListSerach.getPatientName() != null) {
			sb.append(" AND (lower(patientreg4_.first_name) LIKE concat('%',LOWER(:patientName ),'%') OR "
		        	+ " lower(patientreg4_.middle_name) LIKE concat('%',LOWER(:patientName ),'%') OR "
		        	+ " lower(patientreg4_.last_name) LIKE concat('%',LOWER(:patientName ),'%') OR "
		        	+ "lower(temppatien6_.patient_name) LIKE concat('%',LOWER(:patientName ),'%') ) ");
		}

		sb.append(" ORDER BY  sale0_.id DESC");
		Query createdQuery = getSession().createSQLQuery(sb.toString());

		Query sql = createdQuery
				.setParameter("fromDate", Util.getDate(salesReturnBillListSerach.getFromDate()))
				.setParameter("toDate", Util.getDate(salesReturnBillListSerach.getToDate()))
				.setParameterList("status", statusList)
				.setParameter("storeId", salesReturnBillListSerach.getStoreId())
				.setParameter("unitId", salesReturnBillListSerach.getUnitId());
		if (salesReturnBillListSerach.getSaleTypeId() != null && salesReturnBillListSerach.getSaleTypeId() != 0) {
			sql.setParameter("saleTypeId", salesReturnBillListSerach.getSaleTypeId());
		}
		if (salesReturnBillListSerach.getUhid() != null) {
			sql.setParameter("uhid", salesReturnBillListSerach.getUhid().toLowerCase());
		}
		if (salesReturnBillListSerach.getPatientName() != null) {
			sql.setParameter("patientName", salesReturnBillListSerach.getPatientName().toLowerCase());
		}
		List<ResponseSalesReturnBillList> listLabSampleMasterDto = sql.setResultTransformer(Transformers.aliasToBean(ResponseSalesReturnBillList.class)).list();

		return listLabSampleMasterDto;
	}

	@Override
	public List<ResponseAddBill> salesReturnGetBillByIdBP(List<Long> id) {
		Date current = TimeZoneComponent.getDateByzone(sessionService.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = " SELECT DISTINCT (sale.id) as id,patient.dob as patientDob, patient.patientId as patientId, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
				+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName, ' | ',tempPatient.age, ' | ',tempPatGender.genderCode) AS tempPatientDatail, "
				+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName) AS tempPatientDatails, "
				+ "  sale.billNo as billNo, patient.uhidNumber as uhid, saleType.id as saleTypeId, saleType.type as saleType, sale.store.id as storeId,(payee.billAmount-payee.dueAmount) as selfPaidAmount "
				+ " FROM Sale AS sale " + "	INNER JOIN sale.saleType as saleType "
				+ " INNER JOIN sale.saleDetailsList AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ " LEFT JOIN sale.billPayeeList AS payee WITH payee.payeeId = :payeeId" // HIS-16194
				+ "	LEFT JOIN sale.patient as patient WITH patient.status = 'A' "
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ "	LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false "
				+ " LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
				+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
				+ " LEFT JOIN tempPatient.gender AS tempPatGender WITH tempPatGender.status = 'A' "
				+ " WHERE saleDetail.id IN ( SELECT coalesce(saleDetail.id,0) FROM SaleDetail AS saleDetail LEFT JOIN saleDetail.batch AS batch WITH "
				+ " batch.isDeleted = false AND batch.isActive = true AND batch.isReturn = false AND batch.isQuarantine = false AND (batch.expiryDate =null or batch.expiryDate > :greaterThanCurrentMonth) WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false ) "
				+ "	AND sale.id IN (:id) AND sale.isActive = true AND sale.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", id);
		query.setParameter("payeeId", sessionService.getSelfPayeeId());
		query.setParameter("greaterThanCurrentMonth", current);
		query.setResultTransformer(Transformers.aliasToBean(ResponseAddBill.class));
		return (List<ResponseAddBill>) query.list();
	}

//	@Override
//	public ResponseAddBill salesReturnGetBillByIdBP(Long id) {
//		Date current = TimeZoneComponent.getDateByzone(unitId);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(current);
//		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//		current = cal.getTime();
//		
//		String hql = " SELECT DISTINCT (sale.id) as id,patient.dob as patientDob, "
//				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
//				+ " CONCAT(tempPatientPrefix.prefixCode, ' ', tempPatient.patientName, ' | ',tempPatient.age, ' | ',tempPatGender.genderCode) AS tempPatientDatail, "
//				+ "  sale.billNo as billNo, patient.uhidNumber as uhid, saleType.id as saleTypeId, saleType.type as saleType "
//				+ " FROM Sale AS sale "
//				+ "	INNER JOIN sale.saleType as saleType "
//				+ " INNER JOIN sale.saleDetailsList AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
//				+ "	LEFT JOIN sale.patient as patient WITH patient.status = 'A' "
//				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
//				+ "	LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false "
//				+ " LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
//				+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' "
//				+ " LEFT JOIN tempPatient.gender AS tempPatGender WITH tempPatGender.status = 'A' "
//				+ " WHERE saleDetail.id IN ( SELECT saleDetail FROM SaleDetail AS saleDetail INNER JOIN saleDetail.batch AS batch WITH "
//				+ " batch.isDeleted = false AND batch.isActive = true AND batch.isReturn = false AND batch.isQuarantine = false AND batch.expiryDate > :greaterThanCurrentMonth WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false ) "
//				+ "	AND sale.id = :id AND sale.isActive = true AND sale.isDeleted = false ";
//		
//		Query query = getSession().createQuery(hql);
//		query.setParameter("id", id);
//		query.setParameter("greaterThanCurrentMonth", current);
//		query.setResultTransformer(Transformers.aliasToBean(ResponseAddBill.class));
//		return (ResponseAddBill) query.uniqueResult();
//	}

	@Override
	public List<ItemDetails> saleDetailsBySaleId(Long id) {
		String hql = "SELECT batch.id as batchId, sd.id as id, item.id as itemId, item.name as itemName, item.isThreeUOMApplicable as isThreeUOMApplicable, "
				+ " generic.name as genericName, batch.batchNo as batchNo, batch.expiryDate as batchExpiryDate, "
				+ " uomType.type as uomType, "
				+ " uomType.id as uomTypeId, sd.quantity as quantityIssued, sd.rate as rate, sd.totalAmount as totalAmount, "
				+ " sd.discountAmount as discount, sd.taxAmount as taxAmount, sd.unitDiscountAmount as unitDiscount, "
				+ " sd.netAmount as netAmount, sd.taxPercentage as taxPercentage " + " FROM SaleDetail AS sd "
				+ " INNER JOIN sd.sale as sale " + " LEFT JOIN sd.batch AS batch " + " LEFT JOIN sd.item AS item "
				+ " LEFT JOIN item.generic AS generic " + " LEFT JOIN sd.uomType AS uomType "
				+ " WHERE sale.id = :saleId";
		Query query = getSession().createQuery(hql);
		query.setParameter("saleId", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemDetails.class));
		return query.list();
	}

	@Override
	public List<SalesReturnQtyDetail> populateReturnDetailBP(List<Long> saleDetailsIds) {

		String hql = " SELECT coalesce(sum(returnDetail.returnQty),0) as totalReturnQuantity, saleDetail.id as saleDetailId "
				+ " FROM ReturnDetail AS returnDetail "
				+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ " WHERE returnDetail.isActive = true AND returnDetail.isDeleted = false AND saleDetail.id in (:saleDetailId) "
				+ " GROUP BY saleDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("saleDetailId", saleDetailsIds);
		query.setResultTransformer(Transformers.aliasToBean(SalesReturnQtyDetail.class));
		return query.list();

	}

	@Override
	public List<SaleDetail> getSaleDetailByIdBP(List<Long> saleDetailIds) {
		try {
			String hql = " SELECT saleDetail FROM SaleDetail AS saleDetail " + " LEFT JOIN FETCH saleDetail.tax as tax "
					+ " WHERE ( saleDetail.id in (:id) ) AND saleDetail.isActive = true AND saleDetail.isDeleted = false  ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", saleDetailIds);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String getMuiltipleBillNoByReturnId(Integer id) {
		try {
			String sql = "select string_agg(sale.bill_no, ',') over () as billNo  from pharmacy.t_return_detail as returnDetail "
					+ " left join pharmacy.t_sale as sale on returnDetail.sale_id = sale.id "
					+ " where returnDetail.return_id =:returnId group by returnDetail.sale_id, sale.bill_no limit 1 ";

			Query query = getSession().createSQLQuery(sql);
			query.setParameter("returnId", id);

			return (String) query.uniqueResult();

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public Return salesReturnUpdate(Return returObj) {
		getSession().update(returObj);
		return returObj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response getPatientNameSearchForSalesReturn(RequestSaleReturnDto requestSaleReturnDto) {
		try {
			String query = "select distinct(pat.patient_id) as \"patientId\"," + "	pat.uhid_number as \"uhid\" ,"
					+ "	pre.prefix_code as \"prefixCode\",  " + "	pat.first_name as \"firstName\" ,"
					+ "	pat.middle_name as \"middleName\", " + "	pat.last_name as \"lastName\", " +
//					"	pat.dob as \"dob\", " + 
					// " saleDtl.status_id as \"statusId\"," +
					"	gen.gender_code as \"genderCode\", "
					+ " concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name,' '),' ', coalesce(pat.middle_name,' '),' ',coalesce(pat.last_name)) as \"patientName\","
					+ " concat(coalesce(pat.uhid_number,''),'| ',coalesce(pre.prefix_code,''),'. ',coalesce(pat.first_name,' '),' ', "
					+ "	coalesce(pat.middle_name,' '),' ',coalesce(pat.last_name),'| ',trim(concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pat.dob)),'999'))),' |',"
					+ "	gen.gender_code,' |',pat.mobile_number) as \"details\","
					+ "	pat.mobile_number as \"mobileNumber\" " + "	from pharmacy.t_sale_detail saleDtl"
					+ "	inner join pharmacy.t_sale sale on sale.id= saleDtl.sale_id"
					+ "	inner join patient.t_patient_registration pat on pat.patient_id = sale.patient_id"
					+ "	inner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id "
					+ "	inner join public.m_gender_master gen on pat.gender_id = gen.gender_id" + "	 where "
					+ " saleDtl.status_id in (2,4,18) " + " and pat.status ='A' " + " and pat.unit_id =:unitId "
			        + " AND (LOWER(pat.first_name) LIKE CONCAT('%', LOWER(:firstName), '%'))"
					+ "%')" + " union select distinct(pat.patient_id) as \"patientId\","
					+ "	pat.uhid_number as \"uhid\" ," + "	pre.prefix_code as \"prefixCode\",  "
					+ "	pat.first_name as \"firstName\" ," + "	pat.middle_name as \"middleName\", "
					+ "	pat.last_name as \"lastName\", " +
//			"	pat.dob as \"dob\", " + 
					// " saleDtl.status_id as \"statusId\"," +
					"	gen.gender_code as \"genderCode\", "
					+ " concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name,' '),' ', coalesce(pat.middle_name,' '),' ',coalesce(pat.last_name)) as \"patientName\","
					+ " concat(coalesce(pat.uhid_number,''),'| ',coalesce(pre.prefix_code,''),'. ',coalesce(pat.first_name,' '),' ', "
					+ "	coalesce(pat.middle_name,' '),' ',coalesce(pat.last_name),'| ',trim(concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pat.dob)),'999'))),' |',"
					+ "	gen.gender_code,' |',pat.mobile_number) as \"details\","
					+ "	pat.mobile_number as \"mobileNumber\" " + "	from pharmacy.t_sale_detail saleDtl"
					+ "	inner join pharmacy.t_sale sale on sale.id= saleDtl.sale_id"
					+ "   inner join pharmacy.t_temp_patient_registration temppat on "
					+ "   sale.temp_patient_id = temppat.id 	 "
					+ "	inner join patient.t_patient_registration pat on pat.patient_id = temppat.patient_id"
					+ "	inner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id "
					+ "	inner join public.m_gender_master gen on pat.gender_id = gen.gender_id" + "	 where "
					+ " saleDtl.status_id in (2,4,18) " + " and pat.status ='A' " + " and pat.unit_id =:unitId "
					+ " AND (LOWER(pat.first_name) LIKE CONCAT('%', LOWER(:firstName), '%'))";
//					"	or lower(pat.last_name) LIKE '%"+requestSaleReturnDto.getLastName()+"%')";

			List<RequestSaleReturnDto> dtos = getSession().createSQLQuery(query)
					.setParameter("unitId", requestSaleReturnDto.getUnitId())
//					.setParameterList("statusIdsList", requestSaleReturnDto.getStatusIdsList())
					.setParameter("firstName", requestSaleReturnDto.getFirstName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(RequestSaleReturnDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, dtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	// start code for resolved HIS-9583 Return refund bills are showing in get bills
	// in case multiple bills are settled on sales return
	public List<Long> getSaleIdForNotReturned(List<Long> saleIds) {
		try {
			if (saleIds != null && saleIds.size() > 0) {
				String sql = "SELECT sale.id AS id FROM pharmacy.t_sale AS sale "
						+ " INNER JOIN pharmacy.t_sale_detail AS saleDetail ON saleDetail.sale_id = sale.id"
						+ " left outer join ( select sum(return_qty) as return_qty ," + 
						"		sale_detail_id from pharmacy.t_return_detail " + 
						"	group by sale_detail_id having sum(return_qty) > 0 ) temp on temp.sale_detail_id = saleDetail.id "
						+ " WHERE sale.id IN (:saleIds) "
						+ " AND saleDetail.quantity > COALESCE(temp.return_qty,0)  "
						+ " group by sale.id";
				Query query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG);
				query.setParameterList("saleIds", saleIds);
				return query.list();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}
	// end code for resolved HIS-9583 Return refund bills are showing in get bills
	// in case multiple bills are settled on sales return

	@Override
	public Integer getTaxId(Long id) {

		try {
			String sql = " select tax_id from pharmacy.t_sale_detail where id=:id ";

			Query query = getSession().createSQLQuery(sql);
			query.setParameter("id", id);
			// query.setParameter("unitId", sessionService.getUnitId());

			return (query.uniqueResult() != null ? (int) query.uniqueResult() : 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemDetails> patientIssueReturnList(List<Long> saleDetailsIds) {
		try {
			String query = " select coalesce(tidbm.return_accepted_quantity,0) as \"quantityReturned\", tid.sale_detail_id as \"saleDetailsId\" from pharmacy.t_issue_detail tid  " + 
					"inner join pharmacy.t_issue_detail_batch_mapper tidbm on tid.id  = tidbm.issue_detail_id where tid.sale_detail_id in(:saleDetailsIds)";
		
			List<ItemDetails> dtos = getSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ItemDetails.class))
					.setParameterList("saleDetailsIds", saleDetailsIds)
					.list();
			
			return dtos;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Double getSalesAmount(List<Long> saleIds, Integer selfPayeeId) {
		String query = "select coalesce(sum(tpbpd.bill_amount),0) from pharmacy.t_pharmacy_bill_payee_details tpbpd " +
				" where tpbpd.t_sale_id in (:saleIds) and payee_id=:selfPayeeId ";
		Double res=(Double) getSession().createSQLQuery(query).setParameterList("saleIds", saleIds)
				.setParameter("selfPayeeId", selfPayeeId)
				.uniqueResult();
		return res;
	}

	@Override
	public Double getReturnedAmount(List<Long> saleIds) {
		String query = " select coalesce(sum(return.total_return_amt),0) from pharmacy.t_return return  " + 
				" where return.sale_id in (:saleIds)";
		Double res=(Double) getSession().createSQLQuery(query).setParameterList("saleIds", saleIds).uniqueResult();
		return res;
	}

	@Override
	public String getPaymentRefundVoucherNoByReturnId(Integer id) {

		try {
			String sql = "select payment_refund_voucher_no  from billing.t_payment_refund_master tprm where sale_return_id =:returnId  ";

			Query query = getSession().createSQLQuery(sql);
			query.setParameter("returnId", id);

			return (String) query.uniqueResult();

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

}