package com.param.pharmacy.op.dao.billList;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import com.param.billing.global.model.BankMaster;
import com.param.billing.global.model.CardTypeMaster;
import com.param.billing.global.model.PaymentDepositConsumption;
import com.param.billing.global.model.PaymentDepositConsumptionDetails;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.billing.dto.BillBadDebitListResponse;
import com.param.global.billing.dto.BillListResponse;
import com.param.global.billing.dto.ItemDetailsResponse;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.SalePayeeDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsResponseForDrugRecall;
import com.param.pharmacy.op.dto.billList.BillListRequest;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class BillListDao extends AbstractDao<Integer, Sale> implements IBillListDao ,ICommonConstants{

	private static final Logger logger = Logger.getLogger(BillListDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Sale> searchBills(BillListRequest billListRequest, Integer screenId) {

		String hql = "SELECT sale FROM Sale AS sale  "
				+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false   "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false  "
				+ "		INNER JOIN sale.addedBy as user WITH user.status = 'A'  "
				+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A'   "
				+ " 	LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false   "
				+ " 	LEFT JOIN tempPatient.prefix AS tempPatientPrefix WITH tempPatientPrefix.status = 'A' "
				+ "		LEFT JOIN sale.admission as admission WITH admission.status = 'A' AND admission.unit.unitId = :unitId  "
				+ "		LEFT JOIN sale.encounter as encounter WITH encounter.status = 'A' AND encounter.unitId = :unitId  "
				+ "WHERE sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId  "
				+ "		AND sale.addedDate BETWEEN :fromDate AND :toDate  "
				+ " 	AND (:billNo = '' OR lower(sale.billNo) LIKE :billNo)  "
				+ " 	AND (:saleTypeId = 0 OR saleType.id = :saleTypeId)  "
				+ " 	AND (:statusId = 0 OR status.id = :statusId)  "
				+ " 	AND (:visitNo = '' OR lower(admission.admissionNumber) like :visitNo OR lower(encounter.encounterNumber) like :visitNo)  "
				+ " 	AND (:uhid = '' OR lower(patient.uhidNumber) like :uhid)  "
				+ " 	AND ( :patientId = 0 OR patient.patientId = :patientId OR tempPatient.id = :patientId ) "
				+ "		AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " 	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName OR lower(patient.lastName) LIKE :patientName "
				+ "		OR lower(tempPatientPrefix.prefixCode) LIKE :patientName OR lower(tempPatient.patientName) LIKE :patientName OR lower(COALESCE(CONCAT(tempPatientPrefix.prefixCode,' ', tempPatient.patientName))) LIKE :patientName )) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus  "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false "
				+ "										AND screen.isActive = true  "
				+ "								INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "						  WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false)  "
				+ "ORDER BY sale.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(billListRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(billListRequest.getToDate()));
		query.setParameter("billNo",
				billListRequest.getBillNo() != null ? billListRequest.getBillNo().toLowerCase() + '%' : "");
		query.setParameter("saleTypeId",
				billListRequest.getSalesTypeId() != null ? billListRequest.getSalesTypeId() : 0);
		query.setParameter("statusId", billListRequest.getStatusId() != null ? billListRequest.getStatusId() : 0);
		query.setParameter("visitNo",
				billListRequest.getVisitNo() != null ? billListRequest.getVisitNo().toLowerCase() + '%' : "");
		query.setParameter("uhid",
				billListRequest.getUhid() != null ? billListRequest.getUhid().toLowerCase() + '%' : "");
		query.setParameter("patientId",billListRequest.getPatientId() != null ? billListRequest.getPatientId() : 0);
		query.setParameter("patientName", billListRequest.getPatientName() != null ? billListRequest.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}

	@Override
	public Sale getBillDetails(Long id) {
		
		String hql = "SELECT sale FROM Sale AS sale "
				+ "WHERE sale.id = :id AND sale.isActive = true AND sale.isDeleted = false  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
	//	query.setParameter("unitId", sessionService.getUnitId());

		return (Sale) query.uniqueResult();
	}
	
	@Override
	public Sale saveBill(Sale sale)
	{
		getSession().update(sale);

		return sale;	
	}

	@Override
	public PaymentMode getPaymentModeById(Integer paymentModeId) {
		String hql = "Select p from PaymentMode p where p.paymentModeId = :paymentModeId";

		Query query = getSession().createQuery(hql);
		query.setParameter("paymentModeId", paymentModeId);

		return (PaymentMode) query.uniqueResult();
	}

	@Override
	public BankMaster getBankMasterById(Integer bankId) {
		String hql = "Select p from BankMaster p where p.bankId = :bankId";

		Query query = getSession().createQuery(hql);
		query.setParameter("bankId", bankId);

		return (BankMaster) query.uniqueResult();
	}

	@Override
	public CardTypeMaster getCardTypeyId(Integer cardTypeId) {
		String hql = "Select p from CardTypeMaster p where p.cardTypeId = :cardTypeId";

		Query query = getSession().createQuery(hql);
		query.setParameter("cardTypeId", cardTypeId);

		return (CardTypeMaster) query.uniqueResult();
	}

	@Override
	public List<BillListResponse> searchBillsBP(BillListRequest billListRequest, Integer screenId) {
		String hql = "SELECT tempPatient.tempUhid as tempUhid ,  sale.paidAmount as paidAmt, sale.netAmount as netAmount, sale.id as id, sale.addedDate as billDate, "
				+ "sale.billNo as billNo, saleType.type as salesType, saleType.id as saleTypeId, sale.totalAmount as totalAmount, sale.outstandingAmount as balanceAmt,"
				+ "status.status as status,"
				+ "status.id as statusId, user.userName as orderBy,"
				+ "case when (sale.paidAmount > sale.netAmount) then 0.0 else 0.0 end as refundAmt, "
				+ "patient.countryCallingCode as countryCallingCode, patient.id as patientId, patient.uhidNumber as uhid, concat(prefix.prefixCode, ' ',patient.firstName,' ',"
				+ "patient.middleName, ' ',patient.lastName,' | ', gender.genderCode) as patientDetails, "
				
				+ "concat(tempPatientPrefix.prefixCode, '. ',tempPatient.patientName,' | ', " 
				+ "tempPatient.age, ' | ', tempGender.genderCode) as tempPatientDetails, tempPatient.id as tempPatId, "
				+ " (case when tempGender.genderId is null then gender.genderId else tempGender.genderId end) as genderId ,"
				+ " (case when tempGender.genderCode is null then gender.genderCode else tempGender.genderCode end) as genderCode ,"
				+ " patient.imagePath as imagePath ,"
				+ " (case when    concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ')  is null then "
				+ "  (cast(tempPatient.age as string)) else "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') end) as ageString ,"
				+ " (case when CONCAT(tempPatientPrefix.prefixCode, ' ',tempPatient.patientName) is null then"
				+ " CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ',patient.lastName)"
				+ " else CONCAT(tempPatientPrefix.prefixCode, ' ',tempPatient.patientName) end) as patientName ,"
				+ " CONCAT(prefix.prefixCode, ' ', patient.firstName) as tempPatientName , "
				+ " (case when CONCAT(tempDoctor.doctorName) is null then"
				+ " CONCAT('Dr.',doctor.firstName,' ',"
				+ " doctor.middleName, ' ',doctor.lastName)"
				+ " else CONCAT(tempDoctor.doctorName) end) as doctorName ,"
				
				+ " patient.mobileNumber as mobileNumber ,"
				+ " patient.dob as patientDob ,"
				+ " sale.totalDiscountAmount as totalDiscountAmount ,"
				+ " sale.concession as concession ,"
				+ " sale.totalCreditPayable as totalCreditPayable,"
				+ " sale.totalSelfPayable as totalSelfPayable ,"
				+ " sale.searchPatient as searchPatient, "
				+ " sale.cancelledReasonId as cancelledReasonId, "
				+ " sale.cancelledRemark as cancelledRemark "
				+ " FROM Sale AS sale  "
				+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false   "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN sale.addedBy as user  "
				+ "		LEFT JOIN sale.patient as patient   " //WITH patient.status = 'A'
				+ " 	LEFT JOIN patient.prefix AS prefix  "
				+ " 	LEFT JOIN patient.gender AS gender "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false   "
				+ " 	LEFT JOIN tempPatient.prefix AS tempPatientPrefix  "
				+ "		LEFT JOIN tempPatient.gender AS tempGender "
				+ "		LEFT JOIN sale.admission as admission WITH admission.status = 'A' AND admission.unit.unitId = :unitId  "
				+ "		LEFT JOIN sale.encounter as encounter WITH encounter.status = 'A' AND encounter.unitId = :unitId  "
				+ "     LEFT JOIN sale.doctor AS doctor WITH doctor.status = 'A'"
				
				+ "     LEFT JOIN sale.tempDoctor AS tempDoctor WITH tempDoctor.isActive=true AND tempDoctor.isDeleted = false "
				
				+ " WHERE sale.store.storeId=:storeId AND ( sale.unit.unitId = :unitId  AND saleType.id not in (0) )  " //HIS-11601 AND saleType.id not in (4,5,1) / HIS-16470
				+ "		AND sale.addedDate BETWEEN :fromDate AND :toDate  " //HIS-17458
				+ " 	AND (:billNo = '' OR lower(sale.billNo) LIKE :billNo) "
				+ " AND (COALESCE(sale.isVirtualStore, false) = false OR COALESCE(sale.isVirtualStore, false) IS NULL) "
				+ "  AND ((sale.visitType.visitTypeId !=1 and sale.isFromBillableConsumption = false  and sale.isFromCrashCart = false "
				+ " AND sale.isFromPendingIssue = false  and sale.isFromNarcotics = false and sale.isFromPatientIndent = false ) OR (sale.visitType.visitTypeId = 1 OR saleType.id = 3))"//HIS-18259 
				
				+ " 	AND  (:doctorName = '' OR  (lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName OR lower(doctor.lastName) LIKE :doctorName) OR lower(tempDoctor.doctorName) LIKE :doctorName) "
				
				+ " 	AND (:saleTypeId = 0 OR saleType.id = :saleTypeId)  "
				+ " 	AND (:statusId = 0 OR status.id = :statusId)  "
				+ " 	AND (:visitNo = '' OR lower(admission.admissionNumber) like :visitNo OR lower(encounter.encounterNumber) like :visitNo)  "
				+ " 	AND (:uhid = '' OR lower(patient.uhidNumber) like :uhid)  "
				+ " 	AND ( :patientId = 0 OR patient.patientId = :patientId OR tempPatient.id = :patientId ) "
				+ "		AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " 	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName OR lower(patient.lastName) LIKE :patientName "
				+ "		OR lower(tempPatientPrefix.prefixCode) LIKE :patientName OR lower(tempPatient.patientName) LIKE :patientName OR lower(COALESCE(CONCAT(tempPatientPrefix.prefixCode,' ', tempPatient.patientName))) LIKE :patientName )) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus  "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false "
				+ "										AND screen.isActive = true  "
				+ "								INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "						  WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false)  "
				+ "ORDER BY sale.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(billListRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(billListRequest.getToDate()));
		query.setParameter("billNo",
				billListRequest.getBillNo() != null ? '%'+billListRequest.getBillNo().toLowerCase() + '%' : "");
		
		query.setParameter("doctorName",
				billListRequest.getDoctorName() != null ? '%'+billListRequest.getDoctorName().toLowerCase() + '%' : "");
		
		query.setParameter("saleTypeId",
				billListRequest.getSalesTypeId() != null ? billListRequest.getSalesTypeId() : 0);
		query.setParameter("statusId", billListRequest.getStatusId() != null ? billListRequest.getStatusId() : 0);
		query.setParameter("visitNo",
				billListRequest.getVisitNo() != null ? '%'+billListRequest.getVisitNo().toLowerCase() + '%' : "");
		query.setParameter("uhid",
				billListRequest.getUhid() != null ? '%'+billListRequest.getUhid().toLowerCase() + '%' : "");
		query.setParameter("patientId",billListRequest.getPatientId() != null ? billListRequest.getPatientId() : 0);
		query.setParameter("patientName", billListRequest.getPatientName() != null ? '%'+billListRequest.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", billListRequest.getUnitId()!=null ? billListRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("storeId", billListRequest.getStoreId()!=null ? billListRequest.getStoreId() : sessionService.getStoreId());
		query.setResultTransformer(Transformers.aliasToBean(BillListResponse.class));
		return query.list();
	}

	@Override
	public List<PatientDetailsResponseForDrugRecall> searchBillsForDrugRecall(PatientDetailsRequestForDrugRecall request) {
		String hql =  " SELECT  "
					+ "  	saleType.type as visitType,patient.uhidNumber as uhid, "
					+ "   	concat(prefix.prefixCode, '. ',patient.firstName,' ',patient.middleName, ' ',patient.lastName,' | ', gender.genderCode) as patientDetail, "
					+ "  	batch.batchNo as batchNo,batch.expiryDate as expiryDate,wardMaster.wardName as wardName,encounter.encounterNumber as visitNo "
					+ " FROM Sale AS sale  "
					+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false AND saleType.unit.unitId = :unitId  "
					+ "		INNER JOIN sale.saleDetailsList as items WITH items.isActive = true AND items.isDeleted = false AND items.unit.unitId = :unitId "
					+ "		INNER JOIN items.batch as batch WITH batch.isActive = true AND batch.isDeleted = false  AND batch.unit.unitId = :unitId "
					+ "		INNER JOIN batch.item as item WITH item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
					+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A'  "
					+ " 	LEFT JOIN patient.prefix AS prefix  "
					+ " 	LEFT JOIN patient.gender AS gender "
					+ "		LEFT JOIN sale.admission as admission WITH admission.status = 'A' AND admission.unit.unitId = :unitId  "
					+ "     LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ "     LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ "		LEFT JOIN sale.encounter as encounter WITH encounter.status = 'A' AND encounter.unitId = :unitId  "
					+ " WHERE sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId  "
					+ "		AND item.id = :itemId  "
					+ " ORDER BY sale.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId",request.getItemId());
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PatientDetailsResponseForDrugRecall.class));
		return query.list();
	}
	
	@Override
	public BillListResponse getBillDetailsBP(Long id,Integer selfPayeeId, Integer unitId) {

		String sql="select status_id from pharmacy.t_sale where unit_id=:unitId and id=:id ";
		Query query1 = getSession().createSQLQuery(sql)
				.setParameter("id",id)
				.setParameter("unitId",unitId);
		Integer statusId=(Integer) query1.uniqueResult();
		boolean isActiveStatus=true;
		boolean isDeletedStatus=false;
		if(statusId==9)
		{
			 isActiveStatus=false;
			 isDeletedStatus=true;
		}
			
		
		String hql = "SELECT DISTINCT sale.id as id, sale.addedDate as billDate, sale.billNo as billNo, discountType.id as discountTypeId,sale.isConsolidatedBill as isConsolidatedBill, "
				+ " discountCat.category as discountCategory, sale.taxAmount as taxAmount, sale.totalAmount as totalAmount, sale.concession as concession , "
				+ " sale.discount as discount, sale.totalDiscountAmount as totalDiscount,sale.admission.admissionId as admissionId , sale.encounter.encounterId as encounterId , "
				+ " sale.roundOff as roundOff, sale.netAmount as netAmount,sale.totalCreditPayable as totalCreditPayable,coalesce(sale.totalSelfPayable,0) as totalSelfPayable,sale.paidAmount as paidAmt, payee.dueAmount as selfDueAmount,"
				+ " coalesce((payee.dueAmount),0) as balanceAmt, sale.totalDiscountAmount as  totalDiscount , "
				+ " status.status as status, saleType.type as saleType, saleType.id as saleTypeId, "
				+ " patient.patientId as patientId, coalesce(patient.uhidNumber,tempPatient.tempUhid) as uhid, concat(prefix.prefixCode, '. ',patient.firstName,' ', "
				+ " patient.middleName, ' ',patient.lastName,' | ', gender.genderCode) as patientDetails, "
				+ " concat(tempPatientPrefix.prefixCode, '. ',tempPatient.patientName,' | ', "
				+ " tempPatient.age, ' | ', tempGender.genderCode) as tempPatientDetails, tempPatient.id as tempPatId, "
				+ " patient.dob as patientDob,"
				+ " case when (tempPatient.patientName is null) then concat(patient.firstName,' ',patient.lastName) else (tempPatient.patientName) end as patientName,"
				+ " case when (tempDoctor.doctorName is not null) then (tempDoctor.doctorName) "
				+ " when (doctor.doctorId is not null) "
				+ " then concat(doctor.firstName, ' ', doctor.middleName,' ', doctor.lastName) "
				+ " when (doctorSale.doctorId is not null) "
				+ " then concat(doctorSale.firstName, ' ', doctorSale.middleName,' ', doctorSale.lastName) "
				+ " when (doctorPatIndent.doctorId is not null) "
				+ " then concat(doctorPatIndent.firstName, ' ', doctorPatIndent.middleName,' ', doctorPatIndent.lastName) "
				+ "  end as doctorName, " + " gender.genderId As genderId, " + " patient.imagePath As imagePath, "
				+ " tempGender.genderId As tempGenderId, " + " patient.registrationTypeId as registrationTypeId , "
				+ " case when (doctor.id is null) then (tempDoctor.id) else (doctor.id) end as doctorId "
				
				+ " FROM Sale AS sale "
				+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false "
				+ "		LEFT JOIN sale.prescription as prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN sale.saleDetailsList as items WITH items.isActive =:isActiveStatus AND items.isDeleted =:isDeletedStatus AND items.unit.unitId = :unitId "
				+ "		LEFT JOIN items.batch as batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.unit.unitId = :unitId "
				+ "		LEFT JOIN batch.item as item WITH item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
				+ "		LEFT JOIN item.generic as generic WITH generic.isActive = true AND generic.isDeleted = false AND generic.unit.unitId = :unitId "
				+ "		LEFT JOIN item.manufacturer as manufacturer WITH manufacturer.isActive = true AND manufacturer.isDeleted = false "
				+ "		LEFT JOIN items.uomUnit as uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false AND uomUnit.unit.unitId = :unitId "
				+ "		LEFT JOIN items.tax as tax WITH tax.isActive = true AND tax.isDeleted = false AND tax.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.billPayeeList as payee WITH payee.status = 'A' AND payee.payeeId = :payeeId "
				+ "		LEFT JOIN sale.discountType as discountType WITH discountType.isActive = true AND discountType.isDeleted = false AND discountType.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A' "
				+ " 	LEFT JOIN patient.prefix AS prefix  " + " 	LEFT JOIN patient.gender AS gender "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false AND tempPatient.unit.unitId = :unitId "
				+ "		LEFT JOIN tempPatient.gender AS tempGender "
				+ " 	LEFT JOIN tempPatient.prefix AS tempPatientPrefix  "
				+ "		LEFT JOIN prescription.doctor as doctor WITH doctor.status = 'A' "
				+ "		LEFT JOIN sale.doctor as doctorSale WITH doctorSale.status = 'A' "
				+ "		LEFT JOIN sale.tempDoctor as tempDoctor WITH tempDoctor.isActive = true AND tempDoctor.isDeleted = false AND tempDoctor.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.discountCategory as discountCat With discountCat.isActive = true AND discountCat.isDeleted = false AND discountCat.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.issueList as issue WITH issue.isActive = true AND issue.isDeleted = false "
				+ "		LEFT JOIN issue.patientIndent as patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
				+ "		LEFT JOIN patientIndent.doctor as doctorPatIndent WITH doctorPatIndent.status = 'A' "
				+ " WHERE sale.id = :id  AND sale.unit.unitId = :unitId  "; //HIS-17458

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setParameter("payeeId", selfPayeeId!=null ? selfPayeeId : sessionService.getSelfPayeeId());
		query.setParameter("isActiveStatus", isActiveStatus);
		query.setParameter("isDeletedStatus", isDeletedStatus);
		query.setResultTransformer(Transformers.aliasToBean(BillListResponse.class));
		return (BillListResponse) query.uniqueResult();
	}

	@Override
	public List<ItemDetailsResponse> getSaleDetailsBP(Long id) 
	{
		String hql = "select sd.id as id, item.id as itemId, item.name as itemName, item.code as code ,generic.name as genericName, generic.id as genericId, "
				+ "manufacturer.name as manufacturer, batch.id as batchId,  sd.consessionAmount as concession ,  "
				+ "batch.batchNo as batchCode, batch.expiryDate as expiryDate, uomUnit.id as uomId, uomUnit.units as uomUnit,uomType.id as uomTypeId, "
				+ "sd.quantity as quantity,"
				+ "prescription.orderQty as quantityPrescribed, sd.rate as rate, sd.totalAmount as totalAmount, sd.discountPercentage as discountPercentage , "
				+ "sd.discountAmount as discount, sd.taxAmount as taxAmount, "
				+ "sd.netAmount as netAmount, tax.name as taxName, sd.taxPercentage as taxPercent, sd.creditPayable as creditPayable ,sd.selfPayable as selfPayable,sd.packageConsumptionAmt as packageConsumptionAmt "
				+ " FROM SaleDetail as sd "
				+ " INNER JOIN sd.sale sale "
				+ " LEFT JOIN sd.batch as batch "
				+ " LEFT JOIN sd.item as item "
				+ " LEFT JOIN item.manufacturer as manufacturer "
				+ " LEFT JOIN item.generic as generic "
				+ " LEFT JOIN sd.uomUnit as uomUnit "
				+ " LEFT JOIN sd.uomType as uomType "
				+ " LEFT JOIN sd.prescriptionDetail as prescription "
				+ " LEFT JOIN sd .tax as tax "
				+ " where sale.id = :id order by sd.id desc ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemDetailsResponse.class));
		return query.list();
		
	}


	@Override
	public Response saveDepositeConsumption(BillSaveRequest request) {
		try{
			
			PaymentDepositConsumption paymentDepositConsumption = new PaymentDepositConsumption();
			
				paymentDepositConsumption.setOrganizationId(request.getOrgnizationId());
				paymentDepositConsumption.setUnitId(request.getUnitId());
				paymentDepositConsumption.setDepConsumptionDate(request.getCreatedDate() != null ?GlobalCommonDateUtils.getDate(request.getCreatedDate(),"dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(request.getUnitId()));
				paymentDepositConsumption.setCreatedDate(request.getCreatedDate() != null ?GlobalCommonDateUtils.getDate(request.getCreatedDate(),"dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(request.getUnitId()));
				paymentDepositConsumption.setUpdatedDate(request.getCreatedDate() != null ?GlobalCommonDateUtils.getDate(request.getCreatedDate(),"dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(request.getUnitId()));
				paymentDepositConsumption.setUpdatedBy(request.getCreatedBy());
				paymentDepositConsumption.setCreatedBy(request.getCreatedBy());
				paymentDepositConsumption.setBillId((request.getSaleId()!=null && request.getSaleId()>0)?null:request.getBillingMasterId().intValue());
				paymentDepositConsumption.setSaleId(request.getSaleId());
				paymentDepositConsumption.setBillPayeeDetailsId(request.getBillPayeeDetailsId() != null?request.getBillPayeeDetailsId().intValue():null);
				paymentDepositConsumption.setConsumedAmount(request.getDepositConsumption());
				paymentDepositConsumption.setDepositeConsumptionNumber(request.getDepositeConsumptionNumber());
				paymentDepositConsumption.setCounterId(null);
				paymentDepositConsumption.setStatus('A');
				
				getSession().save(paymentDepositConsumption);
			
			return new Response(SUCCESS,SUCCESS_CODE,COMMON_SUCCESS,null,paymentDepositConsumption.getDepConsumptionId());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response addDepositConsumptionDetails(PaymentDepositDetailsDto paymentDepositDetailsDto) {
		try{
			
			PaymentDepositConsumptionDetails paymentDepositConsumptionDetails = new PaymentDepositConsumptionDetails();
				paymentDepositConsumptionDetails.setOrganizationId(paymentDepositDetailsDto.getOrganizationId());
				paymentDepositConsumptionDetails.setUnitId(paymentDepositDetailsDto.getUnitId());
				paymentDepositConsumptionDetails.setCreatedDate(paymentDepositDetailsDto.getUpdatedDate()!=null?GlobalCommonDateUtils.getDate(paymentDepositDetailsDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"):null);
				paymentDepositConsumptionDetails.setUpdatedDate(paymentDepositDetailsDto.getUpdatedDate()!=null?GlobalCommonDateUtils.getDate(paymentDepositDetailsDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"):null);
				paymentDepositConsumptionDetails.setUpdatedBy(paymentDepositDetailsDto.getUpdatedBy());
				paymentDepositConsumptionDetails.setCreatedBy(paymentDepositDetailsDto.getUpdatedBy());
				paymentDepositConsumptionDetails.setConsumedAmount(paymentDepositDetailsDto.getDepositConsumed());
				paymentDepositConsumptionDetails.setDepositId(paymentDepositDetailsDto.getDepositId()!= null ?paymentDepositDetailsDto.getDepositId():null);
				//paymentDepositConsumptionDetails.setDepositDetailsId(paymentDepositDetailsDto.getDepositDetailsId());
				paymentDepositConsumptionDetails.setDepConsumptionId(paymentDepositDetailsDto.getDepConsumptionId());
				paymentDepositConsumptionDetails.setStatus('A');
				paymentDepositConsumptionDetails.setRemark(paymentDepositDetailsDto.getRemark());
				getSession().save(paymentDepositConsumptionDetails);
			
			return new Response(SUCCESS,SUCCESS_CODE,COMMON_SUCCESS,null,null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public List<BillBadDebitListResponse> searchBillsBadDebitlist(BillListRequest billListRequest, Integer screenId) {
			String queryStringpart1="select " + 
					"	sale.paid_amount as \"paidAmt\", " + 
					"	sale.net_amount as \"netAmount\", " + 
					"	cast(sale.id as int8) as  \"id\", " + 
					"	sale.added_date as \"billDate\", " + 
					"	sale.bill_no as \"billNo\", " + 
					"	saletype.type as \"salesType\", " + 
					"	sale.total_amount as \"totalAmount\", " + 
					"	sale.outstanding_amount as \"balanceAmt\", " + 
					"	statusMst.status as \"status\", " + 
					"	userMst.user_name as \"orderBy\", " + 
					"	case " + 
					"		when sale.paid_amount>sale.net_amount then 0.0 " + 
					"		else 0.0 " + 
					"	end as \"refundAmt\", " + 
					"	sale.patient_id as \"patientId\", " + 
					"	patientRegistration.uhid_number as \"uhid\", " + 
					"	(prefixMst.prefix_code || ' ' || patientRegistration.first_name || ' ' || patientRegistration.middle_name || ' ' || patientRegistration.last_name) as \"patientDetails\", " + 
					"	(tempprefixMst.prefix_code || ' ' || tempPatientMst.patient_name) as \"tempPatientDetails\", " + 
					"	tempPatientMst.id as \"tempPatId\", " + 
					"	patientRegistration.dob as \"patientDob\" " + 
					"   from " + 
					"	pharmacy.t_sale sale " + 
					"   inner join pharmacy.m_sale_type saletype on " + 
					"	sale.sale_type_id = saletype.id " + 
					"	and (saletype.is_active = true " + 
					"	and saletype.is_deleted = false )" + 
//					"	and saletype.unit_id =:unitId) " + 
					"   inner join public.m_status statusMst on " + 
					"	sale.status_id = statusMst.id " + 
					"	and (statusMst.is_active = true " + 
					"	and statusMst.is_deleted = false) " + 
					"   inner join public.m_user_master userMst on " + 
					"	sale.added_by = userMst.user_id " + 
					"	and (userMst.status = 'A' " + 
					"	and userMst.unit_id =:unitId) " + 
					"   left outer join patient.t_patient_registration patientRegistration on " + 
					"	sale.patient_id = patientRegistration.patient_id " + 
					"	and (patientRegistration.status = 'A' )" + 
//					"	and patientRegistration.unit_id =:unitId) " + 
					"   left outer join public.m_prefix_master prefixMst on " + 
					"	patientRegistration.prefix_id = prefixMst.prefix_id " + 
					"   left outer join public.m_gender_master gender6_ on " + 
					"	patientRegistration.gender_id = gender6_.gender_id " + 
					"   left outer join pharmacy.t_temp_patient_registration tempPatientMst on " + 
					"	sale.temp_patient_id = tempPatientMst.id " + 
					"	and (tempPatientMst.is_active = true " + 
					"	and tempPatientMst.is_deleted = false " + 
					"	and tempPatientMst.unit_id =:unitId) " + 
					"   left outer join public.m_prefix_master tempprefixMst on " + 
					"	tempPatientMst.prefix_id = tempprefixMst.prefix_id " + 
					"   left outer join public.m_gender_master gender9_ on " + 
					"	tempPatientMst.gender_id = gender9_.gender_id " + 
					"   left outer join adt.t_admission admissionMst on " + 
					"	sale.admission_id = admissionMst.admission_id " + 
					"	and (admissionMst.status = 'A' " + 
					"	and admissionMst.unit_id =:unitId) " + 
					"   left outer join opd.t_encounter_master encounterMst on " + 
					"	sale.encounter_id = encounterMst.encounter_id " + 
					"	and (encounterMst.status = 'A' " + 
					"	and encounterMst.unit_id =:unitId) " + 
					"   where " + 
					"	sale.is_active = true " + 
					"	and sale.is_deleted = false " + 
					"	and sale.unit_id =:unitId " + 
					"	and sale.added_date <= :fromDate " + 
					
					"	and (sale.status_id = 2 " + 
					"	or sale.status_id = 18) "; 
		            
			StringBuilder sb=new  StringBuilder(queryStringpart1);
			if(billListRequest.getBillNo() != null && billListRequest.getBillNo()!="") {
				sb.append( " and lower( sale.bill_no )  like :billNo " );
			}
			if(billListRequest.getSalesTypeId() != null ) {
				sb.append( " and sale.sale_type_id =:salesTypeId " );
			}
			if(billListRequest.getVisitNo()!= null && billListRequest.getVisitNo()!="" ) {
				sb.append( " and ( lower(admissionMst.admission_number) like :visitNo OR lower(encounterMst.encounter_number) like :visitNo ) " );
			}
			if(billListRequest.getPatientId() != null) {
						sb.append( " and tempPatientMst.id=:patientId or patientRegistration.patient_id =:patientId " );
			}
			if(billListRequest.getStatusId() != null) {
				sb.append(" AND sale.status_id = :statusId ");
	         }else {
	        	 sb.append( "	and (sale.status_id = 2 or sale.status_id = 19 or sale.status_id = 18) " );
	         }
			
			String queryStringpart2=
					"	and (statusMst.id in ( " + 
					"	select " + 
					"		statusMst.id " + 
					"	from " + 
					"		public.m_screen_status_mapper screenstatusMapper " + 
					"	inner join public.m_screen screenMst on " + 
					"		screenstatusMapper.screen_id = screenMst.id " + 
					"		and (screenMst.id =:screenId " + 
					"		and screenMst.is_deleted = false " + 
					"		and screenMst.is_active = true) " + 
					"	inner join public.m_status statusMst on " + 
					"		screenstatusMapper.status_id = statusMst.id " + 
					"		and (statusMst.is_deleted = false " + 
					"		and statusMst.is_active = true) " + 
					"	where " + 
					"		screenstatusMapper.is_active = true " + 
					"		and screenstatusMapper.is_deleted = false)) " + 
					"   order by " + 
					"	sale.id desc";
			
			
		String  queryString=sb.toString()+queryStringpart2;
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("fromDate", Util.getDate(billListRequest.getFromDate()));
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		if (billListRequest.getBillNo() != null && !billListRequest.getBillNo().isEmpty()) {
			query.setParameter("billNo", "%" + billListRequest.getBillNo().toLowerCase() + "%");
		}
		if (billListRequest.getSalesTypeId() != null) {
			query.setParameter("salesTypeId", billListRequest.getSalesTypeId());
		}
		if (billListRequest.getVisitNo() != null && !billListRequest.getVisitNo().isEmpty()) {
			query.setParameter("visitNo", "%" + billListRequest.getVisitNo().toLowerCase() + "%");
		}
		if (billListRequest.getPatientId() != null) {
			query.setParameter("patientId", billListRequest.getPatientId());
		}
		if (billListRequest.getStatusId() != null) {
			query.setParameter("statusId", billListRequest.getStatusId());
		}
		query.setResultTransformer(Transformers.aliasToBean(BillBadDebitListResponse.class));
		return query.list();
	}

	@Override
	public BillListResponse getbadDebtBillDetialsBP(Long id) {
		
		String hql = "SELECT DISTINCT sale.id as id, sale.addedDate as billDate, sale.billNo as billNo, discountType.id as discountTypeId,sale.isConsolidatedBill as isConsolidatedBill, "
				+ "discountCat.category as discountCategory, sale.taxAmount as taxAmount, sale.totalAmount as totalAmount, "
				+ "sale.discount as discount, sale.totalDiscountAmount as totalDiscount,"
				+ "sale.roundOff as roundOff, sale.netAmount as netAmount,sale.totalCreditPayable as totalCreditPayable,(coalesce(sale.totalSelfPayable,0)-coalesce(sale.paidAmount,0)) as totalSelfPayable,sale.paidAmount as paidAmt, "
				+ "sale.outstandingAmount as balanceAmt, "
				+ "gender.genderCode as  genderCode, " 
				+ "status.status as status, saleType.type as saleType, saleType.id as saleTypeId, "
				+ "patient.id as patientId, patient.uhidNumber as uhid, concat(prefix.prefixCode, '. ',patient.firstName,' ', "
				+ "patient.middleName, ' ',patient.lastName,' | ', gender.genderCode) as patientDetails, "
				+ "concat(tempPatientPrefix.prefixCode, '. ',tempPatient.patientName,' | ', "
				+ "tempPatient.age, ' | ', tempGender.genderCode) as tempPatientDetails, tempPatient.id as tempPatId, " 
				+ " patient.dob as patientDob, case when (doctor.id is null) then (tempDoctor.doctorName) else concat(doctor.firstName, ' ',"
				+ " doctor.middleName,' ', doctor.lastName) end as doctorName, "
				+ " patient.registrationTypeId as registrationTypeId , "
				+ "case when (doctor.id is null) then (tempDoctor.id) else (doctor.id) end as doctorId "
				+ " FROM Sale AS sale "
				+ "		INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND saleType.isDeleted = false "
				//+ " AND saleType.unit.unitId = :unitId "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		LEFT JOIN sale.saleDetailsList as items WITH items.isActive = true AND items.isDeleted = false AND items.unit.unitId = :unitId "
				+ "		LEFT JOIN items.batch as batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.unit.unitId = :unitId "
				+ "		LEFT JOIN batch.item as item WITH item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
				+ "		LEFT JOIN item.generic as generic WITH generic.isActive = true AND generic.isDeleted = false AND generic.unit.unitId = :unitId "
				+ "		LEFT JOIN item.manufacturer as manufacturer WITH manufacturer.isActive = true AND manufacturer.isDeleted = false "
				+ "		LEFT JOIN items.uomUnit as uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false AND uomUnit.unit.unitId = :unitId "
				+ "		LEFT JOIN items.tax as tax WITH tax.isActive = true AND tax.isDeleted = false AND tax.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.discountType as discountType WITH discountType.isActive = true AND discountType.isDeleted = false AND discountType.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A' "
				+ " 	LEFT JOIN patient.prefix AS prefix  "
				+ " 	LEFT JOIN patient.gender AS gender "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false AND tempPatient.unit.unitId = :unitId "
				+ "		LEFT JOIN tempPatient.gender AS tempGender "
				+ " 	LEFT JOIN tempPatient.prefix AS tempPatientPrefix  "
				+ "		LEFT JOIN sale.doctor as doctor WITH doctor.status = 'A'  "
				+ "		LEFT JOIN sale.tempDoctor as tempDoctor WITH tempDoctor.isActive = true AND tempDoctor.isDeleted = false AND tempDoctor.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.discountCategory as discountCat With discountCat.isActive = true AND discountCat.isDeleted = false AND discountCat.unit.unitId = :unitId "
				+ "WHERE sale.id = :id AND sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BillListResponse.class));
		return (BillListResponse) query.uniqueResult();
	}

	@Override
	public  List<SalePayeeDetailsDto> badDebtBillPayeeDetials(Long id) {
		String queryString=
				" select " + 
				"	payeedetails.payee_details_id as \"payeeDetailsId\" , " + 
				"	payeedetails.payee_id as \"payeeId\" , " + 
				"	payeedetails.t_sale_id as \"tSaleId\" , " + 
				"	payeedetails.bill_amount as \"billAmount\" , " + 
				"	payeedetails.due_amount as \"dueAmount\" , " + 
				"	payeedetails.cr_amount as \"crAmount\" , " + 
				"	companymst.company_desc as \"companyDesc\", " + 
				"	sales.patient_id as \"patientId\", " + 
				"	sales.encounter_id as \"encounterId\", " + 
				"	sales.sale_type_id as \"visitTypeId\" , " + 
				"	sales.admission_id as \"admissionId\" " + 
				"   from " + 
				"	pharmacy.t_pharmacy_bill_payee_details payeedetails " + 
				"   inner join public.t_company_master companymst on " + 
				"	payeedetails.payee_id = companymst.company_id " + 
				"   inner join pharmacy.t_sale sales on " + 
				"	payeedetails.t_sale_id = sales.id " + 
				"   where " + 
				"	payeedetails.t_sale_id =:id " + 
				"   order by " + 
				"	payeedetails.payee_details_id desc";
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(SalePayeeDetailsDto.class));
		return query.list();
	}

	@Override
	public BillListResponse getCreditDuaAmount(Long id, Integer selfPayeeId) {
		String queryString=
				" select sum(due_amount) as \"creditDueAmount\"   from pharmacy.t_pharmacy_bill_payee_details where t_sale_id =:id and payee_id not in(:selfPayeeId) and status  = 'A' ";
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("id", id);
		query.setParameter("selfPayeeId", selfPayeeId);
		query.setResultTransformer(Transformers.aliasToBean(BillListResponse.class));
		return (BillListResponse) query.uniqueResult();
		
	}

   
    @Override
    public String getIsInvoiceGeneratied(Long id, Integer selfPayeeId) {
    	if(selfPayeeId!=null)
    	{
    		 String queryString=
    	                " select cast(is_invoice_generated as varchar)  as\"invoiceGenerated\" from pharmacy.t_pharmacy_bill_payee_details where t_sale_id =:id and payee_id not in(:selfPayeeId) and status  = 'A' ";
    		 
    		 List<BillListResponse> list1  = getSession()
      		        .createSQLQuery(queryString)
      		        .setLong("id", id)
       				.setParameter("selfPayeeId", selfPayeeId)
       				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
    		 
    		 if(list1 != null && list1.size()>0) {
    			 
    			 boolean checkflag = false;
    			 
    			 for (BillListResponse billListResponse : list1) {
    				 
					if(billListResponse.getInvoiceGenerated() != null && billListResponse.getInvoiceGenerated().equals("Y")) {
						checkflag = true;
					}
					
    			 }
    			 
    			 if(checkflag == true) {
    				 return"Y"; 
    			 }else {
    				 return"N"; 
    			 }
    			
    		 }else {
    			 return"N"; 
    		 }
    	}
    	else
    	{
    		return  "N";
    	}
       
    }

	@Override
	public List<BillListResponse> getIpdOrOpdBillGeneratedStatus(List<Long> saleIds) {
		
		
		String sqlStr = "select (case when bill_id is null then false else true end) as \"ipdOpdBillStatus\" ,ts2.id as \"id\"  from pharmacy.t_sale ts2 "
				+ " where ts2.id in("+saleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") "+ 
				" union  " + 
				" select (case when (ts.bill_id is not null AND mbm.bill_cancelled='N') then true else false end)  as \"ipdOpdBillStatus\" ,ts.id as \"id\" from pharmacy.t_sale ts " + 
				" inner join  billing.m_billing_master mbm on mbm.billing_master_id=ts.bill_id "
				+ " where ts.id in("+saleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ";
		
		List<BillListResponse> pharmacyInvList =  getSession()
				.createSQLQuery(sqlStr)
				.addScalar("ipdOpdBillStatus")
				.addScalar("id", StandardBasicTypes.LONG)
				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
		return pharmacyInvList;
		
	}

	@Override
	public List<BillListResponse> pharmacyReceiptCancellationFlag(BillListRequest billListRequest) {
		String sql="select pharmacy_receipt_cancellation as \"pharmacyReceiptCancellation\" from public.t_user_unit_mapper where unit_id=:orgUnitId and organisation_id=:orgId and user_id=:userId and status='A' ";
		List<BillListResponse> list1  = getSession()
		          .createSQLQuery(sql)
				.setParameter("orgId", billListRequest.getOrganizationId())
				.setParameter("orgUnitId", billListRequest.getUnitId())
				.setParameter("userId", billListRequest.getUserId())
				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
		return list1;

	}

	@Override
	public List<BillListResponse> isPharmacyReturn(List<Long> saleIds) {
		String sqlQuery1 = " select sale_id as \"id\"  from pharmacy.t_return where sale_id in(:saleIds)";
		
		List<BillListResponse> pharmacyInvList =  getSession()
				.createSQLQuery(sqlQuery1)
				.addScalar("id", StandardBasicTypes.LONG)
				.setParameterList("saleIds", saleIds)
				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
		return pharmacyInvList;
		
	}

	@Override
	public List<BillListResponse> isIssueReturn(List<Long> saleIds) {
		String sqlQuery1 = " select is_return as \"isIssueReturn\" , sale_id as \"id\" from pharmacy.t_issue where sale_id in(:saleIds) ";
		
		List<BillListResponse> pharmacyInvList =  getSession()
				.createSQLQuery(sqlQuery1)
				.addScalar("isIssueReturn")
				.addScalar("id", StandardBasicTypes.LONG)
				.setParameterList("saleIds", saleIds)
				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
		return pharmacyInvList;
	}
    @Override
    public Integer getOrgId(Long id) {
        String sqlQuery1 = " select organization_id  from pharmacy.t_sale_detail where id=:id ";
        Integer orgId=(Integer) getSession().createSQLQuery(sqlQuery1.toString())
				.setParameter("id", id)
				.uniqueResult();
        return orgId;
    }

	@Override
	public PharamcyBillPayeeDetails checkSelfDueAmount(BillSaveRequest request) {
		String hql = "SELECT bill FROM PharamcyBillPayeeDetails AS bill "
				+ "WHERE bill.tSaleId = :id AND bill.status = 'A' and  bill.payeeId = :selfPayeeId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", request.getBillingMasterId().intValue());
		query.setParameter("selfPayeeId", request.getSelfPayeeId());

		return (PharamcyBillPayeeDetails) query.uniqueResult();
	}

	@Override
	public List<BillListResponse> isInvoiceGenerated(List<Long> saleIds,Integer unitId) {
		
		String query=" select self_payee_id from t_unit_application_billing_configuration tuabc where unit_id=:unitId and status='A' ";
		Integer selfPayeeId=(Integer) getSession().createSQLQuery(query.toString())
				.setParameter("unitId", unitId)
				.uniqueResult();
		
		List<BillListResponse> pharmacyInvList =  getSession()
				.createSQLQuery(
						" select t_sale_id as \"id\", is_invoice_generated as \"isInvoiceGenerated\"  "
						+ " from pharmacy.t_pharmacy_bill_payee_details where  "
						+ " status='A' and is_invoice_generated = 'Y' and payee_id!=:selfPayeeId and t_sale_id  in (:saleIds) "
						+ " group  by t_sale_id,is_invoice_generated  ")
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("isInvoiceGenerated")
				.setParameter("selfPayeeId", selfPayeeId)
				.setParameterList("saleIds", saleIds)
				.setResultTransformer(Transformers.aliasToBean(BillListResponse.class)).list();
		
		return pharmacyInvList;
		
	}
}
