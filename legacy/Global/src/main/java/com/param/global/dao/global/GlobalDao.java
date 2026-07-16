package com.param.global.dao.global;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.param.billing.global.transaction.config.model.RegistrationFieldConfiguration;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.pharmacy.dto.*;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.FloorMasters;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.WorkingCondition;
import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Company;
import com.param.entity.model.master.Currency;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.FixedAssetType;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.KitType;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.Order;
import com.param.entity.model.master.OtherCharge;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.entity.model.master.PrefixConfiguration;
import com.param.entity.model.master.PrefixRunningNumber;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.ScrapReason;
import com.param.entity.model.master.Screen;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemMapper;
import com.param.entity.model.master.StoreType;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.Unit;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Ved;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.AssignPickerDetailAddedItems;
import com.param.entity.model.pharmacy.BatchAllocation;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleType;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseOrderStaged;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.SaleDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.GRNQuantity;
import com.param.global.dto.GoodReceiptNoteDetailDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.global.AgainstGrnDetailResponse;
import com.param.global.dto.global.AgainstGrnRequest;
import com.param.global.dto.global.AgainstGrnResponse;
import com.param.global.dto.global.AgainstPOSearchResponse;
import com.param.global.dto.global.AgainstPurchaseOrder;
import com.param.global.dto.global.AgainstPurchaseRequest;
import com.param.global.dto.global.AgainstWORequest;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.DateRequest;
import com.param.global.dto.global.DoctorSpecialityResp;
import com.param.global.dto.global.GRNAgainstApprovedPOQuantity;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.POOtherCharges;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.global.PatientDetailRequest;
import com.param.global.dto.global.PatientSearchDetails;
import com.param.global.dto.global.PoAccessoriesMapperGlobal;
import com.param.global.dto.global.Prefix;
import com.param.global.dto.global.PurchaseOrderDetailResponse;
import com.param.global.dto.global.PurchaseOrderStagedDcDate;
import com.param.global.dto.global.PurchaseRequestDetailResponse;
import com.param.global.dto.global.QuotationRequestDetailResponse;
import com.param.global.dto.global.Rack;
import com.param.global.dto.global.SalesType;
import com.param.global.dto.global.Shelf;
import com.param.global.dto.global.TariffResponse;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.common.dao.PrefixConfigDto;
import com.param.global.otims.dto.BatchDto;
import com.param.global.utility.Util;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GlobalDao extends AbstractDao<Integer, Integer> implements IGlobalDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(GlobalDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	SessionService sessionService;

	@Autowired
	HikariDataSource driverManager;

	@Override
	public Integer getBatchQuantity(Long batchId, Integer unitId) {

		String hql = "SELECT batch.quantity FROM Batch AS batch "
				+ "WHERE batch.id = :batchId AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getStoreStockByBatch(Integer storeId, Long batchId) {
		try {
			String hql = "SELECT SUM(storeStock.availableQty) FROM StoreStock AS storeStock "
					+ "		INNER JOIN storeStock.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
					+ "		INNER JOIN storeStock.batch AS batch WITH batch.id = :batchId AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true "
					+ "WHERE storeStock.isDeleted = false AND storeStock.isActive = true  and storeStock.availableQty is not null and storeStock.totalQty is not null and  storeStock.totalQty > 0 group by storeStock.availableQty order by storeStock.availableQty desc ";

			Query query = getSession().createQuery(hql);
			query.setParameter("storeId", storeId);
			query.setParameter("batchId", batchId);

			Long avaialableStoreStock1 = (Long) query.uniqueResult();
			avaialableStoreStock1 = avaialableStoreStock1 == null ? avaialableStoreStock1.valueOf(0)
					: avaialableStoreStock1;
			Integer avaialableStoreStock = avaialableStoreStock1.intValue();
			avaialableStoreStock = avaialableStoreStock == null ? 0 : avaialableStoreStock;

			return avaialableStoreStock;
		} catch (Exception e) {
			logger.error("Error while fetching store stock by batch", e);
			return null;
		}
	}

	/** Added by Ganesh */
	@Override
	public Integer getStoreStockByBatch(Integer storeId, Long batchId, Integer itemId) {
		try {
			String SQL = "select cast(sum(coalesce(storestock0_.available_qty, 0)) as bigint) as availableqty from "
					+ " inventory.t_store_stock storestock0_ "
					+ " inner join inventory.t_batch batch2_ on storestock0_.batch_id = batch2_.id "
					+ " and (batch2_.is_quarantine = false "
					+ " and batch2_.is_deleted = false and batch2_.is_active = true) "
					+ " where storestock0_.store_id = :storeId "
					+ " and storestock0_.is_deleted = false "
					+ " and storestock0_.available_qty is not null "
					+ " and storestock0_.is_active = true "
					+ " and storestock0_.available_qty >= 0 ";
			StringBuilder sb = new StringBuilder(SQL);
			if (batchId != null && batchId > 0) {
				sb.append(" AND batch2_.id = :batchId ");
			} else {
				sb.append(" AND batch2_.item_id = :itemId ");
			}
			sb.append(" limit 1 ");
			Query query = getSession().createSQLQuery(sb.toString());
			query.setParameter("storeId", storeId);
			if (batchId != null && batchId > 0) {
				query.setParameter("batchId", batchId);
			} else {
				query.setParameter("itemId", itemId);
			}
			BigInteger availableStoreStock = (BigInteger) query.uniqueResult();
			if (availableStoreStock == null) {
				availableStoreStock = BigInteger.valueOf(0);
			}
			return availableStoreStock.intValue();
		} catch (Exception e) {
			logger.error("Error while fetching store stock by batch and item", e);
			return null;
		}
	}

	@Override
	public Long getStoreStockByBatchList(Integer storeId, List<Long> batchId) {

		String hql = "SELECT SUM(storeStock.availableQty) as availableQty FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.id IN ( :batchId) AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true "
				+ "WHERE storeStock.isDeleted = false AND storeStock.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("batchId", batchId);

		Long avaialableStoreStock = (Long) query.uniqueResult();

		avaialableStoreStock = avaialableStoreStock == null ? 0 : avaialableStoreStock;

		return avaialableStoreStock;
	}

	@Override
	public Integer getTransitQuantityByBatch(Integer storeId, Long batchId) {

		String hql = "SELECT SUM(storeStock.tQty) FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.id = :batchId AND batch.isDeleted = false AND batch.isActive = true "
				+ "WHERE storeStock.isDeleted = false AND storeStock.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);

		Long tQty = (Long) query.uniqueResult();

		tQty = tQty == null ? 0 : tQty;

		return tQty.intValue();
	}

	@Override
	public Integer getStoreStockByBatchPharmacy(Integer storeId, Long batchId) {
		String hql = "SELECT storeStock.availableQty FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.id = :batchId AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isPharmacy = false "
				+ "WHERE storeStock.isDeleted = false AND storeStock.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);

		Integer avaialableStoreStock = (Integer) query.uniqueResult();

		avaialableStoreStock = avaialableStoreStock == null ? 0 : avaialableStoreStock;

		return avaialableStoreStock;
	}

	@Override
	public Batch getBatch(Integer storeId, Long batchId, Integer unitId) {

		String hql = "SELECT batch FROM Batch AS batch " + "		 JOIN FETCH batch.storeStockList AS storeStock "
				+ "		 JOIN FETCH storeStock.store AS store "
				+ "WHERE batch.id = :batchId AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("batchId", batchId);
		return (Batch) query.uniqueResult();
	}

	public List<Batch> getBatchList(Integer storeId, Long batchId, Integer itemId, Integer unitId) {

		String hql = "SELECT batch FROM Batch AS batch "
				+ "LEFT JOIN FETCH batch.storeStockList AS storeStock "
				+ "LEFT JOIN FETCH storeStock.store AS store "
				+ "WHERE batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true "
				+ "AND batch.unit.unitId = :unitId "
				+ "AND storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "AND storeStock.unit.unitId = :unitId "
				+ "AND store.isDeleted = false AND store.isActive = true "
				+ "AND store.storeId = :storeId AND store.unit.unitId = :unitId ";

		StringBuilder sb = new StringBuilder(hql);

// Conditionally append additional criteria based on batchId and itemId
		if (batchId != null && batchId > 0) {
			sb.append(" AND batch.id = :batchId ");
		} else {
			sb.append(" AND batch.isBatchRequired = false AND batch.item.id = :itemId ");
		}

		sb.append(" ORDER BY batch.id ASC ");

		Query query = getSession().createQuery(sb.toString());
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

// Set the batchId and itemId parameters conditionally
		if (batchId != null && batchId > 0) {
			query.setParameter("batchId", batchId);
		} else {
			query.setParameter("itemId", itemId);
		}

// Execute the query and return the result
		return (List<Batch>) query.list();

	}

	@Override
	public Batch getBatchPharmacy(Integer storeId, Long batchId, Integer unitId) {
		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "		INNER JOIN FETCH storeStock.store AS store "
				+ "WHERE batch.id = :batchId AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
				+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("batchId", batchId);

		return (Batch) query.uniqueResult();
	}

	@Override
	public Admission getAdmission(Integer admissionId) {
		if (admissionId == null)
			return null;

		String hql = "SELECT admission FROM Admission AS admission WHERE admission.admissionId = :admissionId AND admission.status = 'A' AND admission.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("admissionId", admissionId);
		// query.setParameter("unitId", sessionService.getUnitId());

		return (Admission) query.uniqueResult();
	}

	@Override
	public DiscountCategory getDiscountCategory(Integer discountCategoryId, Integer unitId) {
		String hql = "SELECT discountCategory FROM DiscountCategory AS discountCategory "
				+ "WHERE discountCategory.id = :discountCategoryId AND discountCategory.isDeleted = false AND discountCategory.isActive = true AND discountCategory.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("discountCategoryId", discountCategoryId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (DiscountCategory) query.uniqueResult();
	}

	@Override
	public DiscountType getDiscountType(Integer discountTypeId, Integer unitId) {
		String hql = "SELECT discountType FROM DiscountType AS discountType "
				+ "WHERE discountType.id = :discountTypeId AND discountType.isDeleted = false AND discountType.isActive = true AND discountType.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("discountTypeId", discountTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (DiscountType) query.uniqueResult();
	}

	/*
	 * @Override public Doctor getDoctor(Integer doctorId) { if (doctorId == null)
	 * return null;
	 * 
	 * String hql =
	 * "SELECT doctor FROM Doctor AS doctor WHERE doctor.doctorId = :doctorId AND doctor.status = 'A' AND doctor.unit.unitId = :unitId "
	 * ;
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("doctorId",
	 * doctorId); query.setParameter("unitId", sessionService.getUnitId());
	 * 
	 * return (Doctor) query.uniqueResult(); }
	 */

	@Override
	public Order getOrder(Integer orderId) {
		if (orderId == null)
			return null;

		String hql = "SELECT order FROM Order AS order WHERE order.orderId = :orderId AND order.orderStatus = 'A' ";

		Query query = getSession().createQuery(hql);
		query.setParameter("orderId", orderId);
		// query.setParameter("unitId", sessionService.getUnitId());

		return (Order) query.uniqueResult();
	}

	@Override
	public PatientRegistration getPatient(Integer patientId) {
		if (patientId == null)
			return null;

		String hql = "SELECT patient FROM PatientRegistrations AS patient WHERE patient.patientId = :patientId AND patient.status = 'A' ";

		Query query = getSession().createQuery(hql);
		query.setParameter("patientId", patientId);

		return (PatientRegistration) query.uniqueResult();
	}

	@Override
	public Admission admissionPatient(PatientDetailRequest patientDetailsSearch, Integer unitId) {
		try {
			String hql = " SELECT admission FROM Admissions AS admission "
					+ " INNER JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " INNER JOIN admission.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN admission.doctor AS doctor WITH doctor.status = 'A' "
					+ " INNER JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.nursingAcceptance='Y' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " WHERE admission.status = 'A' AND admissionDetail.isDischarge != 'D' AND admission.unit.unitId = :unitId "
					+ " AND ( patient.id = :patientId ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) ";
			Query query = getSession().createQuery(hql);
			query.setParameter("visitType",
					patientDetailsSearch.getVisitType() != null ? patientDetailsSearch.getVisitType() : 0);
			query.setParameter("patientId", patientDetailsSearch.getPatientId());
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

			return (Admission) query.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while fetching admitted patient", e);
			return null;
		}
	}

	@Override
	public User userSearch(Integer id) {
		try {
			String hql = " SELECT user FROM User AS user WHERE user.status = 'A' AND user.userId = :id ";
			Query query = getSession().createQuery(hql);

			query.setParameter("id", id);

			return (User) query.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while fetching user by id", e);
			return null;
		}
	}

	@Override
	public List<Encounter> encounterPatient(PatientDetailRequest patientDetailsSearch, Integer unitId) {
		try {
			/*
			 * String hql = " SELECT encounter FROM Encounters AS encounter " +
			 * " INNER JOIN encounter.patient AS patient WITH patient.status = 'A' " +
			 * " INNER JOIN encounter.status1 AS status  " +
			 * " LEFT JOIN encounter.visitType AS visitType WITH visitType.status = 'A' " +
			 * " LEFT JOIN encounter.doctor AS doctor WITH doctor.status = 'A' " +
			 * " WHERE  encounter.unitId = :unitId " +
			 * " AND patient.id = :patientId  and status.id (5,6,8) ";
			 */
			// + " AND ( visitType.visitTypeId = :visitType OR :visitType = null ) "; ststus
			// id in (5,6,8)

			String hql = " SELECT encounter FROM Encounters AS encounter "
					+ " INNER JOIN encounter.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN encounter.doctor AS doctor WITH doctor.status = 'A' "
					+ " WHERE encounter.status = 'A' AND encounter.unitId = :unitId "
					+ " AND patient.id = :patientId  AND  encounter.statusId in (5,6,8) ";

			Query query = getSession().createQuery(hql);
			// query.setParameter("visitType", patientDetailsSearch.getVisitType() != null ?
			// patientDetailsSearch.getVisitType() : 0);
			query.setParameter("patientId", patientDetailsSearch.getPatientId());
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching encounter patient list", e);
			return null;
		}

	}

	@Override
	public Status getStatus(Integer statusId) {
		if (statusId == null)
			return null;

		String hql = "SELECT status FROM Status AS status WHERE status.id = :statusId AND status.isDeleted = false AND status.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", statusId);

		return (Status) query.uniqueResult();
	}

	@Override
	public Store getStore(Integer storeId) {
		if (storeId == null)
			return null;

		String hql = "SELECT store FROM Store AS store WHERE store.storeId = :storeId AND store.isDeleted = false AND store.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		// query.setParameter("unitId", sessionService.getUnitId());

		return (Store) query.uniqueResult();
	}

	@Override
	public VisitType getVisitType(Integer visitTypeId) {
		if (visitTypeId == null)
			return null;
		else if (visitTypeId == 0)
			return null;

		String hql = "SELECT visitType FROM VisitType AS visitType WHERE visitType.visitTypeId = :visitTypeId AND visitType.status = 'A'  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("visitTypeId", visitTypeId);

		return (VisitType) query.uniqueResult();
	}

	@Override
	public Screen getScreen(Integer screenId) {
		String hql = "SELECT screen FROM Screen AS screen WHERE screen.id = :screenId AND screen.isDeleted = false AND screen.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("screenId", screenId);

		return (Screen) query.uniqueResult();
	}

	@Override
	public SaleDetail getSaleDetailById(Long id) {
		String hql = "SELECT saleDetail FROM SaleDetail AS saleDetail "
				+ "	LEFT JOIN saleDetail.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isReturn = false "
				+ "	WHERE saleDetail.id = :id AND saleDetail.isDeleted = false AND saleDetail.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (SaleDetail) query.uniqueResult();
	}

	@Override
	public List<ScreenStatus> getScreenStatus(Integer screenId) {
		try {
			String hql = "SELECT screenStatus FROM ScreenStatus AS screenStatus "
					+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false AND screen.isActive = true "
					+ "		INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
					+ "WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false "
					+ "ORDER BY status.status ASC";

			Query query = getSession().createQuery(hql);
			query.setParameter("screenId", screenId);

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching screen status", e);
			return null;
		}
	}

	@Override
	public List<SaleType> getSalesTypeList() {
		String hql = "SELECT saleType FROM SaleType AS saleType WHERE saleType.isActive = true AND saleType.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("unitId", sessionService.getUnitId());
		// Shifted to global level
		return query.list();
	}

	@Override
	public List<Prefix> getPrefixList(Integer orgId) {
		try {
			String hql = "SELECT prefix.prefixId as id, prefix.prefixDesc as prefix " + "FROM Prefix AS prefix "
					+ "WHERE prefix.status = 'A' AND prefix.organization.organizationId = :organizationId "
					+ " ORDER BY prefix.prefixDesc ASC ";
			Query query = getSession().createQuery(hql);
			query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
			query.setResultTransformer(Transformers.aliasToBean(Prefix.class));
			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching prefix list", e);
			return null;
		}
	}

	@Override
	public List<com.param.global.dto.global.Gender> getGenderList() {
		String hql = "SELECT gender.genderId as id, gender.genderName as gender" + " FROM Gender AS gender "
				+ "WHERE gender.status = 'A'  ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("organizationId", sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.Gender.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.DiscountCategory> getDiscountCategoryListBP(Integer orgId, Integer unitId) {

		String hql = "SELECT discountCategory.id as id, discountCategory.category as discountCategory "
				+ " FROM DiscountCategory AS discountCategory "
				+ "WHERE discountCategory.isDeleted = false AND discountCategory.isActive = true "
				+ " AND discountCategory.organization.organizationId=:organizationId AND discountCategory.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.DiscountCategory.class));
		return query.list();
	}

	@Override
	public List<DiscountCategory> getDiscountCategoryList(Integer orgId, Integer unitId) {

		String hql = "SELECT discountCategory " + " FROM DiscountCategory AS discountCategory "
				+ "WHERE discountCategory.isDeleted = false AND discountCategory.isActive = true AND "
				+ " discountCategory.organization.organizationId=:organizationId AND discountCategory.unit.unitId = :unitId "
				+ " ORDER BY category ASC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());
		// query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<Store> getUserStoreList(Integer unitId, Integer orgId, Integer userId) {

		String hql = """
				SELECT store.storeId as storeId,store.storeName as storeName,store.storeType as storeType
				FROM UserStoreMapperNew as storeMapper\s
				INNER JOIN storeMapper.storeMaster as store
				WHERE storeMapper.userId=:userId AND storeMapper.isDeleted = false AND storeMapper.unitId = :unitId \
				 AND storeMapper.organizationId=:organizationId \
				 ORDER BY store.storeName ASC""";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId != null ? userId : sessionService.getUserId());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		List<Store> stores = query.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(Store.class)).list();
		return stores;
	}

	@Override
	public List<Store> getUserActiveStoreList(Integer storeId, Integer unitId, Integer orgId) {
		String hql = """
				SELECT store.storeId as storeId,store.storeName as storeName,store.storeType as storeType
				FROM UserStoreMapperNew as storeMapper\s
				INNER JOIN storeMapper.storeMaster as store
				WHERE storeMapper.isDeleted = false AND storeMapper.isActive = true AND storeMapper.unitId = :unitId AND store.storeId = :storeId\
				 AND storeMapper.organizationId=:organizationId\s\
				""";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		List<Store> stores = query.setParameter("storeId", storeId)
				.setResultTransformer(Transformers.aliasToBean(Store.class)).list();
		return stores;
	}

	/*
	 * @Override public List<Store> getUserStoreList(Integer id) {
	 * 
	 * List<Store> stores =
	 * sessionFactory.getCurrentSession().getNamedQuery("GET_STORE_LIST_BY_ID")
	 * .setParameter("userId",
	 * id).setResultTransformer(Transformers.aliasToBean(Store.class)).list();
	 * return stores;
	 * 
	 * }
	 */

	@Override
	public List<Store> getLinenUserStoreList(Integer unitId, Integer userId) {
		String hql = "SELECT store FROM Store AS store "
				+ "		INNER JOIN store.userStoreMappersList as userStore WITH userStore.userId = :userId AND userStore.isActive = true "
				+ "					AND userStore.isDeleted = false AND userStore.unit.unitId = :unitId "
				+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
				+ " WHERE store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId AND storeType.id = 5 "
				+ " ORDER BY store.storeId";

		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId != null ? userId : sessionService.getUnitId());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUserId());

		return query.list();
	}

	@Override
	public List<StoreType> getStoreTypeList(Integer orgId) {
		// String hql = "SELECT storeType FROM StoreType AS storeType WHERE
		// storeType.isDeleted = false AND storeType.isActive = true AND
		// storeType.unit.unitId = :unitId "
		// + "ORDER BY storeType.id";
		String hql = "SELECT storeType FROM StoreType AS storeType WHERE storeType.isDeleted = false AND storeType.isActive = true AND storeType.organization.organizationId = :organizationId "
				+ "ORDER BY storeType.type ASC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		// query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<Store> getStoreList(Integer unitId) {
		String hql = "SELECT store FROM Store AS store WHERE store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "ORDER BY store.storeName ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<Store> getLinenStoreList(Integer unitId) {
		String hql = "SELECT store FROM Store AS store "
				+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
				+ " WHERE store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId AND storeType.id = 5 "
				+ " ORDER BY store.storeId";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<IndentType> getIndentTypeList(Integer orgId, Integer unitId) {
		String hql = "SELECT indentType FROM IndentType AS indentType "
				+ "WHERE indentType.isDeleted = false AND indentType.isActive = true " + "ORDER BY indentType.type ASC ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("unitId", unitId!=null ? unitId :
		// sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<Priority> getPriorityList() {
		// change above code for resolved HIS-10745 Store Indent : Click on Add New
		// Loader Keeps Rotating in Case of Login with Super Admin
		// as per discussion with Sharadbhai and kaustubhbhai the priority can move on
		// application level.
		String hql = "SELECT priority FROM Priority AS priority WHERE priority.status = 'A' "
				+ " ORDER BY priority.priorityDesc ASC ";
		Query query = getSession().createQuery(hql);

		return query.list();
	}

	/*
	 * @Override public List<Batch> getBatchDetails(Integer id) {
	 * 
	 * return getBatchDetails(id, sessionService.getStoreId()); }
	 * 
	 * @Override public List<Batch> getBatchDetails(Integer id, Integer storeId) {
	 * Date current = new Date(); Calendar cal = Calendar.getInstance();
	 * cal.setTime(current); cal.set(Calendar.DAY_OF_MONTH,
	 * cal.getActualMaximum(Calendar.DAY_OF_MONTH)); current = cal.getTime(); String
	 * hql = "SELECT batch FROM Batch AS batch " +
	 * "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
	 * + "		INNER JOIN FETCH batch.storeStockList AS storeStock " +
	 * "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId"
	 * +
	 * "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
	 * + " ORDER BY batch.expiryDate ASC";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("id", id);
	 * query.setParameter("unitId", sessionService.getUnitId());
	 * query.setParameter("storeId", storeId);
	 * query.setParameter("greaterThanCurrentMonth", current); return query.list();
	 * }
	 */

	@Override
	public List<Batch> getBatchDetails(Integer id, Integer unitId, Integer storeId, BatchProperty batchProperty) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		// System.out.println("Store Id : " + storeId);
		// System.out.println("Session Store Id : " + sessionService.getStoreId());

		// System.out.println("Unit Id : " + unitId);
		// System.out.println("Session Unit Id : " + sessionService.getUnitId());

		String hql = "SELECT batch FROM Batch AS batch " + "		INNER JOIN batch.item AS item "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND  storeStock.unit.unitId = :unitId  AND storeStock.availableQty > 0 "
				+ " AND (  batch.expiryDate >= :greaterThanCurrentMonth OR  batch.expiryDate is null ) and storeStock.availableQty is not null and storeStock.totalQty is not null "
				+ " AND item.isDeleted = false AND item.isActive = true AND batch.unit.unitId = :unitId "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		// query.setParameter("isPharmacy",batchProperty.getIsPharmacy() == null ||
		// batchProperty.getIsPharmacy() ? 0 : 1);
		// query.setParameter("isIndent", batchProperty.getIsIndent() == null ||
		// batchProperty.getIsIndent() ? 0 : 1);
		// query.setParameter("isReturn", batchProperty.getIsReturn() == null ||
		// batchProperty.getIsReturn() ? 0 : 1);
		return query.list();
	}

	@Override
	public List<Batch> getBatchNotApplicableDetails(Integer id, Integer unitId, Integer storeId,
			BatchProperty batchProperty) {
		List<Batch> batchList = null;
		String sql = "select " + "	sum(batch0_.current_quantity) as \"currentQuantity\", "
				+ "	batch0_.is_batch_required as \"isBatchRequired\", "
				+ "	batch0_.is_consignment as \"isConsignment\", " + "	batch0_.is_indent as \"isIndent\", "
				+ "	batch0_.is_opb as \"isOpb\", " + "	batch0_.is_pharmacy as \"isPharmacy\", "
				+ "	batch0_.is_quarantine as \"isQuarantine\", " + "	sum(batch0_.quantity) as \"quantity\", "
				+ "	avg(coalesce(batch0_.rate,0)+(coalesce(batch0_.rate,0)*coalesce(batch0_.markup))) as \"rate\", "
				+ "	avg(coalesce(batch0_.selling_price,0)+(coalesce(batch0_.selling_price,0)*coalesce(batch0_.markup))) as \"sellingPrice\", "
				+ "	sum(coalesce(batch0_.rejected_quantity,0)) as \"rejectedQuantity\", "
				+ "	sum(coalesce(batch0_.returned_quantity,0)) as \"returnedQuantity\", "
				+ "	batch0_.tax_id as \"taxId\" " + "	--coalesce(batch0_.tax_amount,0) as tax_amo30_357_0_, "
				+ "	--batch0_.uom_unit_id as uom_uni43_357_0_ " + "from " + "	inventory.t_batch batch0_ "
				+ "inner join public.m_item item1_ on " + "	batch0_.item_id = item1_.id "
				+ "inner join inventory.t_store_stock storestock2_ on " + "	batch0_.id = storestock2_.batch_id "
				+ "where " + "	item1_.id =:id " + "	and batch0_.is_deleted = false "
				+ "	and batch0_.is_active = true " + "	and batch0_.is_quarantine = false "
				+ "	and batch0_.current_quantity>0 " + "	and batch0_.unit_id =:unitId "
				+ "	and ((:isPharmacy = 0 or batch0_.is_pharmacy = false) or "
				+ "	(:isIndent=0 or batch0_.is_indent = false) " + "	or (:isReturn or batch0_.is_return = false)) "
				+ "	and storestock2_.store_id =:storeId " + "	and storestock2_.available_qty>0 "
				+ "	and item1_.is_deleted = false " + "	and item1_.is_active = true "
				+ "	and item1_.unit_id =:unitId " + "	and batch0_.is_batch_required  = false " + "group by "
				+ "	batch0_.is_batch_required, " + "	batch0_.is_consignment, " + "	batch0_.is_indent , "
				+ "	batch0_.is_opb , " + "	batch0_.is_pharmacy , " + "	batch0_.is_quarantine , "
				+ "	batch0_.is_return , " + "	batch0_.item_id , " + "	batch0_.tax_id , " + "	batch0_.tax_percentage";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("isPharmacy",
				batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
		query.setParameter("isIndent", batchProperty.getIsIndent() == null || batchProperty.getIsIndent() ? 0 : 1);
		query.setParameter("isReturn", batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
		batchList = query.list();

		for (Batch batch : batchList) {
			List<StoreStock> storeStockList = null;
			String batchSql = "select " + "	sum(storestock2_.available_qty) as \"availableQty\", "
					+ "	storestock2_.is_consignment as \"isConsignment\", " + "	storestock2_.store_id as \"storeId\", "
					+ "	sum(storestock2_.t_qty) as \"tQty\", " + "	sum(storestock2_.total_qty) as \"totalQty\" "
					+ "from " + "	inventory.t_batch batch0_ " + "inner join public.m_item item1_ on "
					+ "	batch0_.item_id = item1_.id " + "inner join inventory.t_store_stock storestock2_ on "
					+ "	batch0_.id = storestock2_.batch_id " + "where " + "	item1_.id =:id "
					+ "	and batch0_.is_deleted = false " + "	and batch0_.is_active = true "
					+ "	and batch0_.is_quarantine = false " + "	and batch0_.current_quantity>0 "
					+ "	and batch0_.unit_id =:unitId " + "	and ((:isPharmacy = 0 "
					+ "	or batch0_.is_pharmacy = false) " + "	or ( :isIndent = 0 "
					+ "	or batch0_.is_indent = false) " + "	or (:isReturn " + "	or batch0_.is_return = false)) "
					+ "	and storestock2_.store_id =:storeId " + "	and storestock2_.available_qty>0 "
					+ "	and item1_.is_deleted = false " + "	and item1_.is_active = true "
					+ "	and item1_.unit_id =:unitId " + "	and batch0_.is_batch_required = false " + "group by "
					+ "	storestock2_.is_consignment, " + "	storestock2_.store_id";

			Query stockQuery = getSession().createSQLQuery(batchSql);
			stockQuery.setParameter("id", id);
			stockQuery.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			stockQuery.setParameter("storeId", storeId);
			stockQuery.setParameter("isPharmacy",
					batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
			stockQuery.setParameter("isIndent",
					batchProperty.getIsIndent() == null || batchProperty.getIsIndent() ? 0 : 1);
			stockQuery.setParameter("isReturn",
					batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
			storeStockList = stockQuery.list();
			batch.setStoreStockList(storeStockList);

			List<BatchUomMapper> uomList = null;
			String sqlUom = "select " + "	distinct buommppr.conversions as \"conversions\", "
					+ "	buommppr.ip_uom as \"ipUom\", " + "	buommppr.op_uom as \"opUom\", "
					+ "	buommppr.store_uom as \"storeUom\", " + "	utype.\"type\" as \"uomType\", "
					+ "	uunit.unit as \"uomUnit\", " + "	buommppr.uom_type_id as \"uomTypeId\", "
					+ "	buommppr.uom_unit_id as \"uomUnitId\", " + "	utype.code || uunit.unit \"uomDetails\" "
					+ "from " + "	inventory.t_batch batch0_ " + "inner join public.m_item item1_ on "
					+ "	batch0_.item_id = item1_.id " + "inner join inventory.t_batch_uom_mapper buommppr on "
					+ "	batch0_.id = buommppr.batch_id " + "inner join public.m_uom_type utype on "
					+ "	buommppr.uom_type_id = utype.id " + "inner join public.m_uom_unit uunit on "
					+ "	buommppr.uom_unit_id = uunit.id " + "inner join inventory.t_store_stock storestock2_ on "
					+ "	batch0_.id = storestock2_.batch_id " + "where " + "	item1_.id =:id "
					+ "	and batch0_.is_deleted = false " + "	and batch0_.is_active = true "
					+ "	and batch0_.is_quarantine = false " + "	and batch0_.current_quantity>0 "
					+ "	and batch0_.unit_id =:unitId " + "	and ((:isPharmacy = 0 "
					+ "	or batch0_.is_pharmacy = false) " + "	or ( :isIndent = 0 "
					+ "	or batch0_.is_indent = false) " + "	or (:isReturn " + "	or batch0_.is_return = false)) "
					+ "	and storestock2_.store_id =:storeId " + "	and storestock2_.available_qty>0 "
					+ "	and item1_.is_deleted = false " + "	and item1_.is_active = true "
					+ "	and item1_.unit_id =:unitId " + "	and batch0_.is_batch_required = false";

			Query uomQuery = getSession().createSQLQuery(sqlUom);
			uomQuery.setParameter("id", id);
			uomQuery.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			uomQuery.setParameter("storeId", storeId);
			uomQuery.setParameter("isPharmacy",
					batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
			uomQuery.setParameter("isIndent",
					batchProperty.getIsIndent() == null || batchProperty.getIsIndent() ? 0 : 1);
			uomQuery.setParameter("isReturn",
					batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
			uomList = uomQuery.list();
			batch.setBatchUomList(uomList);
		}
		return batchList;
	}

	@Override
	public List<Batch> getBatchDetailsForAsset(Integer id, Integer storeId, BatchProperty batchProperty,
			Integer unitId) {
		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 and storeStock.availableQty is not null and storeStock.totalQty is not null  "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);

		// query.setParameter("isPharmacy", batchProperty.getIsPharmacy() == null ||
		// batchProperty.getIsPharmacy() ? 0 : 1);
		// query.setParameter("isIndent", batchProperty.getIsIndent() == null ||
		// batchProperty.getIsIndent() ? 0 : 1);
		// query.setParameter("isReturn", batchProperty.getIsReturn() == null ||
		// batchProperty.getIsReturn() ? 0 : 1);
		return query.list();
	}

	/* Consignmeentoutward UomList Return Purpose Write This Method */
	@Override
	public List<Batch> getBatchDetails(Integer id, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate >= :greaterThanCurrentMonth "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);

		return query.list();
	}

	/*
	 * @Override public List<Batch> getBatchDetailsPharmacy(Integer id,Integer
	 * unitId) { return getBatchDetailsPharmacy(id,
	 * sessionService.getStoreId(),unitId); }
	 */

	@Override
	public List<Batch> getBatchDetailsPharmacyConsignment(Integer id, Integer storeId, Integer unitId) {
		return getBatchDetailsPharmacy(id, storeId, unitId);
	}
	@Override
	public List<Batch> getBatchDetailsPharmacyConsignment1(Integer id, Integer storeId, Integer unitId) {
		return getBatchDetailsPharmacy1(id, storeId, unitId);
	}
	private List<Batch> getBatchDetailsPharmacy1(Integer id, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId  AND storeStock.availableQty is not null and storeStock.totalQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null ) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);

		return query.list();

	}

	@Override
	public List<Batch> getBatchListPharmacy(Integer itemId, Long batchId, Integer storeId, Integer unitId) {
		try {
			Date current = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			current = cal.getTime();

			String hql = "SELECT batch FROM Batch AS batch "
					+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
					+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
					+ "WHERE item.id = :itemId AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
					+ "		AND batch.id NOT IN (:batchId) AND storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
					+ " ORDER BY batch.expiryDate ASC";

			Query query = getSession().createQuery(hql);
			query.setParameter("itemId", itemId);
			query.setParameter("batchId", batchId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", storeId);
			query.setParameter("greaterThanCurrentMonth", current);

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching batch list for pharmacy", e);
			return null;
		}
	}

	@Override
	public List<Batch> getBatchDetailsPharmacy(Integer id, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty >0 AND storeStock.availableQty is not null and storeStock.totalQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null ) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		return query.list();
	}

	@Override
	public List<Batch> getBatchDetailsPharmacyForBarcodeScan(Integer id, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE batch.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty is not null and storeStock.totalQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null ) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id.longValue());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		return query.list();
	}

	@Override
	public Long getStoreStockByItem(Integer storeId, Integer itemId, Integer unitId) {
        Date date = TimeZoneComponent.getDateByzone(unitId);
        String sql = "SELECT  coalesce(stock.totalQty, 0) AS stockAvailable "
                + " FROM public.m_item item "
                + "    LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
                + "                FROM public.m_item item "
                + " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
                + " and (to_char(batch.expiry_date,'YYYY-MM-DD') >=:expiryDate or batch.expiry_date is null )"
                + "                    INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
                + "                                and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
                + " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
                + "                GROUP BY item.id) stock ON stock.itemId = item.id "
                + " WHERE item.id =:itemId AND item.is_deleted=false AND item.is_active=true "
                + "    ORDER BY item.id ";
        Query query = getSession().createSQLQuery(sql).addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", (unitId != null && unitId > 0) ? unitId : sessionService.getUnitId());
		query.setParameter("expiryDate",GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public Long getStoreStockByItem(Integer storeId, Integer itemId, Long batchId, Integer unitId) {

		String hql = "SELECT COALESCE(SUM(storeStock.availableQty), 0) FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId  AND storeStock.availableQty > 0 "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true  "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE store.storeId = :storeId AND item.id = :itemId "
				+ "		AND batch.isQuarantine = false AND batch.id =:batchId  AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("batchId", batchId);

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public Long getstockInTransitByItem(Integer storeId, Integer itemId, Integer unitId) {

		String hql = "SELECT COALESCE(SUM(storeStock.tQty), 0) FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE store.storeId = :storeId AND item.id = :itemId "
				+ "		AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}

	@Override
	public Long getStoreStockByItemPharmacy(Integer storeId, Integer itemId, Integer unitId) {

		/*
		 * String hql =
		 * "SELECT COALESCE(SUM(storeStock.availableQty), 0) FROM Batch AS batch " +
		 * "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
		 * 
		 * String hql = "SELECT COALESCE(SUM(storeStock.availableQty), 0) " +
		 * " FROM Batch AS batch " +
		 * "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId AND storeStock.availableQty > 0 "
		 * +
		 * "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
		 * +
		 * "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
		 * + " WHERE store.storeId = :storeId AND item.id = :itemId " +
		 * "		AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
		 * ;
		 */

		Date date = TimeZoneComponent.getDateByzone(unitId);
		String sql = "SELECT  coalesce(stock.totalQty, 0) AS stockAvailable "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
				+ " and (to_char(batch.expiry_date,'YYYY-MM-DD') >=:expiryDate or batch.expiry_date is null )"
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
				+ " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE item.id =:id AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";
		Query query = getSession().createSQLQuery(sql)
				.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameter("id", itemId);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setParameter("expiryDate",GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		Long availableStock = (Long) query.uniqueResult();
		return availableStock.longValue();
	}

	@Override
	public StoreStock getStoreStockByBatchAndStore(Long batchId, Integer storeId, Integer unitId) {
		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "		WHERE store.storeId = :storeId AND batch.id = :batchId  "
				+ " 	AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId and  storeStock.availableQty is not null and  storeStock.availableQty > 0 ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		List<StoreStock> data = query.list();
		if (data.size() > 0) {
			return (StoreStock) data.getFirst();
		}
		return null;

	}

	@Override
	public StoreStock getStoreStockByBatchAndStoreItemId(Long batchId, Integer storeId, Integer unitId,
			Integer itemId) {
		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "		WHERE store.storeId = :storeId AND batch.id = :batchId  and  batch.item.id = :itemId "
				+ " 	AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId  and storeStock.totalQty is not null and  storeStock.availableQty is not null  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);
		query.setParameter("itemId", itemId);

		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		List<StoreStock> data = query.list();
		if (data.size() > 0) {
			return (StoreStock) data.getFirst();
		}
		return null;

	}

	@Override
	public List<StoreStock> getStoreStockByBatchAndStoreByItem(Long batchId, Integer storeId, Integer itemId,
			Integer unitId) {
		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ " INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false "
				+ "     AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ " INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "     AND store.unit.unitId = :unitId "
				+ " WHERE store.storeId = :storeId "
				+ "   AND storeStock.isActive = true "
				+ "   AND storeStock.isDeleted = false "
				+ "   AND storeStock.unit.unitId = :unitId ";

		if (batchId != null && batchId > 0) {
			hql += " AND batch.id = :batchId ";
		} else {
			hql += " AND batch.isBatchRequired = false AND batch.item.id = :itemId ";
		}

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		if (batchId != null && batchId > 0) {
			query.setParameter("batchId", batchId);
		} else {
			query.setParameter("itemId", itemId);
		}

// Execute the query and return the result
		return query.list();



	}

	@Override
	public StoreStock getStoreStockByBatchAndStorePharmacy(Long batchId, Integer storeId, Integer unitId) {
		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "		WHERE store.storeId = :storeId AND batch.id = :batchId  "
				+ " 	AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (StoreStock) query.uniqueResult();
	}

	@Override
	public Integer getStoreMaxQuantityByItem(Integer storeId, Integer itemId, Integer orgId) {
		String hql = "SELECT COALESCE(storeItem.maxQty, 0) FROM StoreItemMapper AS storeItem "
				+ "		INNER JOIN storeItem.store AS store WITH store.storeId = :storeId AND store.isActive = true  AND store.isDeleted =false "
				+ "		INNER JOIN storeItem.item AS item WITH item.id = :itemId AND item.isActive = true  AND item.isDeleted =false "
				+ " WHERE storeItem.organization.organizationId =:organizationId "
				+ " 	AND storeItem.isActive = true  AND storeItem.isDeleted =false order by 1 desc";

		Query query = getSession().createQuery(hql);
		query.setMaxResults(1);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		Integer maxStoreQuantity = (Integer) query.uniqueResult();

		return maxStoreQuantity != null ? maxStoreQuantity : 0;
	}

	@Override
	public Integer getStoreMaxQuantityByItemFromAssetAcc(Integer storeId, Integer itemId, Integer orgId) {
		String hql = "SELECT COALESCE(storeItem.maxQty, 0) FROM StoreItemMapper AS storeItem "
				+ "		INNER JOIN storeItem.store AS store WITH store.storeId = :storeId "
				+ "		INNER JOIN storeItem.item AS item WITH item.id = :itemId "
				+ " WHERE storeItem.organization.organizationId =:organizationId "
				+ " group by storeItem.item.id,storeItem.maxQty";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		Integer maxStoreQuantity = (Integer) query.uniqueResult();

		return maxStoreQuantity != null ? maxStoreQuantity : 0;
	}

	@Override
	public Long getStoreStockInTransitByItem(Integer storeId, Integer itemId) {
		/*
		 * String hql = "SELECT COALESCE(SUM(storeIndentDetail.inTransitQuantity), 0) "
		 * + "FROM StoreIndentDetail AS storeIndentDetail " +
		 * "		INNER JOIN storeIndentDetail.storeIndent AS storeIndent " +
		 * "		INNER JOIN storeIndentDetail.item AS item " +
		 * "WHERE item.id = :itemId AND storeIndent.fromStore.storeId = :storeId " +
		 * "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false "
		 * ;
		 */
		Date date = new Date();
		String hql = "SELECT COALESCE(SUM(stock.tQty), 0) " + "FROM StoreStock AS stock "
				+ "		INNER JOIN stock.batch AS batch ON (to_char(batch.expiryDate,'YYYY-MM-DD') >=:expiryDate "
						+ " or batch.expiryDate is null )"
				+ " WHERE stock.store.storeId = :storeId AND batch.item.id = :itemId "
				+ "		AND stock.isActive = true AND stock.isDeleted = false AND stock.tQty > 0";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("expiryDate", GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

		// return (Long) query.uniqueResult();
	}

	/*
	 * @Override public Boolean setStoreStockInTransitByItem(Integer storeId,
	 * Integer itemId,Integer qty) { String hql =
	 * "UPDATE  StoreIndentDetail set inTransitQuantity=inTransitQuantity+:qty" +
	 * "WHERE item.id = :itemId AND storeIndent = :storeId " +
	 * "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false "
	 * ;
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("storeId",
	 * storeId); query.setParameter("itemId", itemId);
	 * 
	 * return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	 * 
	 * // return (Long) query.uniqueResult(); }
	 */
	@Override
	public Long getLastMonthSale(Integer storeId, Integer itemId, Integer unitId) {

		String hql = "SELECT COALESCE(SUM(lastMonthSale.saleQuantity), 0) FROM LastMonthSale AS lastMonthSale "
				+ "		INNER JOIN lastMonthSale.item AS item WITH item.id = :itemId AND item.isDeleted = false AND item.isActive = true  "
				+ "		INNER JOIN lastMonthSale.store AS store WITH store.storeId = :storeId AND store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE lastMonthSale.isDeleted = false AND lastMonthSale.isActive = true AND lastMonthSale.unit.unitId = :unitId  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (Long) query.uniqueResult();
	}

	@Override
	public Long getCurrentMonthSale(Integer storeId, Integer itemId, Integer unitId) {
		String hql = "SELECT COALESCE(SUM(saleDetail.leaseQuantity), 0) FROM SaleDetail AS saleDetail "
				+ "		INNER JOIN saleDetail.sale AS sale WITH sale.isDeleted = false AND sale.isActive = true AND sale.unit.unitId = :unitId "
				+ "		INNER JOIN sale.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN saleDetail.batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.id = :itemId "
				+ "WHERE saleDetail.isDeleted = false AND saleDetail.isActive = true AND saleDetail.unit.unitId = :unitId "
				+ "		AND date_trunc('month', saleDetail.addedDate) = date_trunc('month', current_date) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (Long) query.uniqueResult();
	}

	public List<AssetType> getAssetTypeList(Integer unitId, Integer orgId) {

		String hql = "SELECT assetType FROM AssetType AS assetType "
				+ "WHERE assetType.isActive = true AND assetType.isDeleted = false  " + "   ORDER BY assetType.type ";

		Query query = getSession().createQuery(hql);

		return query.list();
	}

	@Override
	public List<KitType> getKitTypeList() {
		String hql = "SELECT kType FROM KitType AS kType " + "WHERE kType.isActive = true AND kType.isDeleted = false  "
		// + "AND kType.unit.unitId = :unitId "
				+ "ORDER BY kType.description";

		Query query = getSession().createQuery(hql);
		// query.setParameter("unitId", sessionService.getUnitId());

		return query.list();

	}

	@Override
	public List<AssetCategory> getAssetCategoryList(Integer assetTypeId, Integer unitId, Integer orgId) {
		String hql = "SELECT assetCategory FROM AssetCategory AS assetCategory "
				+ "	INNER JOIN assetCategory.assetType as assetType WITH assetType.id = :assetTypeId AND assetType.isDeleted = false AND assetType.isActive = true "
				+ "WHERE assetCategory.isActive = true AND assetCategory.isDeleted = false AND assetCategory.organization.organizationId = :organizationId "
				+ "ORDER BY assetCategory.category ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", assetTypeId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<ProductCategory> getProductCategoryList(Integer assetCategoryId, Integer orgId) {
		String hql = "SELECT productCategory FROM ProductCategory AS productCategory "
				+ "	INNER JOIN productCategory.assetCategory as assetCategory WITH assetCategory.id = :assetCategoryId AND assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "WHERE productCategory.isActive = true AND productCategory.isDeleted = false AND productCategory.organization.organizationId = :organizationId "
				+ "ORDER BY productCategory.category ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetCategoryId", assetCategoryId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<UomResponse> getUnitOfMeasurementListByItem(Integer itemId, Integer orgId) {

		String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
				+ "		uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom "
				+ "	FROM UnitOfMeasurement AS uom "
				+ "		INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
				+ "	WHERE uom.item.id = :itemId AND uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId=:organizationId "// HIS-16396
																																						// HIS-12019
																																						// AND
				// uom.item.organization.organizationId=:organizationId"
				+ "	ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		// HIS-12019
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public Integer getPurchaseConversionByItem(Integer itemId, Integer orgId) {

		String hql = "  SELECT uom.conversions AS conversion" + "	    FROM UnitOfMeasurement AS uom "
				+ "	    WHERE uom.item.id = :itemId AND uom.uomType.id=1 AND uom.isDeleted = false AND uom.isActive = true  AND uom.organizationId=:organizationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		// query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		// System.out.println("Item Id in Method getPurchaseConversionByItem : " +
		// itemId);
		// System.out.println("Organization Id in Method getPurchaseConversionByItem : "
		// + sessionService.getOrganizationId());
		Integer purchaseConversion = (Integer) query.uniqueResult();

		return purchaseConversion != null ? purchaseConversion : 1;
	}

	@Override
	public Integer getStockConversionByItem(Integer itemId, Integer orgId) {

		String hql = "  SELECT uom.conversions AS conversion" + "	    FROM UnitOfMeasurement AS uom "
				+ "	    WHERE uom.item.id = :itemId AND uom.uomType.id=2 AND uom.isDeleted = false AND uom.isActive = true  AND uom.item.organization.organizationId=:organizationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setMaxResults(1);
		// query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		// System.out.println("Item Id in Method getStockConversionByItem : " + itemId);
		System.out
				.println("Organization Id in Method getStockConversionByItem : " + sessionService.getOrganizationId());
		Integer purchaseConversion = (Integer) query.uniqueResult();

		return purchaseConversion != null ? purchaseConversion : 1;
	}

	@Override
	public Integer getDispenseConversionByItem(Integer itemId, Integer orgId) {

		String hql = "  SELECT uom.conversions AS conversion" + "	    FROM UnitOfMeasurement AS uom "
				+ "	    WHERE uom.item.id = :itemId AND uom.uomType.id=3 AND uom.isDeleted = false AND uom.isActive = true  AND uom.item.organization.organizationId=:organizationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		// query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		// System.out.println("Item Id in Method getDispenseConversionByItem : " +
		// itemId);
		// System.out.println("Organization Id in Method getDispenseConversionByItem : "
		// + sessionService.getOrganizationId());
		Integer purchaseConversion = (Integer) query.uniqueResult();

		return purchaseConversion != null ? purchaseConversion : 1;
	}

	@Override
	public List<Status> getStatusList() {
		String hql = " SELECT status FROM Status AS status "
				+ " WHERE status.isActive = true AND status.isDeleted = false "
				+ " ORDER BY status.status ASC ";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<VisitType> getVisitTypeList() {
		/*
		 * String hql = "SELECT visitType FROM VisitType AS visitType " +
		 * "WHERE visitType.status = 'A' AND visitType.organization.organizationId = :organizationId "
		 * ;
		 */

		String hql = "SELECT visitType FROM VisitType AS visitType " + "WHERE visitType.status = 'A' "
				+ " ORDER BY visitType.visitTypeName ASC ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("organizationId", sessionService.getOrganizationId());

		return query.list();
	}
	

	@Override
	public List<PMSFrequency> getPMSFrequency(Integer orgId) {
		String hql = "SELECT PMSFrequency FROM PMSFrequency AS PMSFrequency "
				+ "WHERE PMSFrequency.isActive = true AND PMSFrequency.isDeleted = false "
				+ " ORDER BY PMSFrequency.description ASC ";
		// + "AND PMSFrequency.organization.organizationId = :organizationId ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("organizationId", orgId!=null ? orgId :
		// sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<WorkingCondition> getWorkingConditions() {
		String hql = "SELECT workingCondition FROM WorkingCondition AS workingCondition "
				+ "WHERE workingCondition.isActive = true AND workingCondition.isDeleted = false ";

		Query query = getSession().createQuery(hql);

		return query.list();
	}

	@Override
	public List<SaleType> getSaleTypeList() {
		String hql = "SELECT saleType FROM SaleType AS saleType "
				+ "WHERE saleType.isActive = true AND saleType.isDeleted = false ";
		// + " AND saleType.organization.organizationId = :organizationId ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("organizationId", sessionService.getOrganizationId());
		// Shifted to global level
		return query.list();
	}

	@Override
	public Currency getCurrency(Integer unitId) {
		String hql = "SELECT currency FROM Currency AS currency WHERE currency.unitId = :unitId AND  currency.status = 'A'";

		Query query = getSession().createQuery(hql).setMaxResults(1);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (Currency) query.uniqueResult();
	}

	@Override
	public List<RejectReason> getRejectReasonList(Integer unitId) {
		String hql = "SELECT rejectReason FROM RejectReason AS rejectReason "
				+ "WHERE rejectReason.isActive = true AND rejectReason.isDeleted = false AND rejectReason.unit.unitId = :unitId";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<CancelReason> getCancelReasonList() {
		String hql = "SELECT cancelReason FROM CancelReason AS cancelReason "
				+ "WHERE cancelReason.isActive = true AND cancelReason.isDeleted = false ";
//		AND cancelReason.unit.unitId = :unitId
		Query query = getSession().createQuery(hql);
//		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<StoreIndent> getStoreIndentList(Integer fromStoreId, Integer toStoreId, Integer unitId) {
		String hql = "SELECT storeIndent FROM StoreIndent AS storeIndent "
				+ "		LEFT JOIN storeIndent.fromStore as fromStore "
				+ "		LEFT JOIN storeIndent.toStore as toStore  "
				+ "WHERE storeIndent.isActive = true AND storeIndent.isDeleted = false AND storeIndent.unit.unitId = :unitId "
				+ "		AND fromStore.storeId = :fromStoreId AND toStore.storeId = :toStoreId "
				+ "		AND (storeIndent.approvalStatus.id = 5 OR storeIndent.approvalStatus.id = 6) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromStoreId", fromStoreId);
		query.setParameter("toStoreId", toStoreId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	public List<PharmacologicalClassification> getPharmacologicalClassificationList(Integer unitId) {
		String hql = "SELECT pharmacologicalClassification FROM PharmacologicalClassification AS pharmacologicalClassification "
				+ "WHERE pharmacologicalClassification.isActive = true AND pharmacologicalClassification.isDeleted = false AND pharmacologicalClassification.unit.unitId = :unitId";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<StoreIndentDetail> getStoreIndentDetailsList(Integer storeIndentId) {

		String hql = "SELECT storeIndentDetail FROM StoreIndentDetail AS storeIndentDetail "
				+ "WHERE storeIndentDetail.storeIndent.id = :storeIndentId "
				+ "		AND (storeIndentDetail.status.id = 5 OR storeIndentDetail.status.id = 6 ) "
				+ "		AND ( storeIndentDetail.purchaseRequestDetail is null OR storeIndentDetail.purchaseRequestDetail is not null ) "
				+ "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeIndentId", storeIndentId);
		return query.list();

	}

	@Override
	public List<StoreIndentDetail> getStoreIndentDetails(List<Integer> storeIndentDetailIds) {
		String hql = "SELECT storeIndentDetail FROM StoreIndentDetail AS storeIndentDetail "
				+ "WHERE storeIndentDetail.id in :storeIndentDetailIds "
				+ "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);

		query.setParameterList("storeIndentDetailIds", storeIndentDetailIds);
		return query.list();
	}

	public java.util.List<Manufacturer> getManufacturerList(Integer orgId) {
		String hql = "SELECT manufacturer FROM Manufacturer AS manufacturer "
				+ "WHERE manufacturer.isActive = true AND manufacturer.isDeleted = false AND manufacturer.organization.organizationId = :organizationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<StrengthUnit> getStrengthUnitList(Integer orgId) {
		String hql = "SELECT strengthUnit FROM StrengthUnit AS strengthUnit "
				+ "WHERE strengthUnit.isActive = true AND strengthUnit.isDeleted = false AND strengthUnit.organization.organizationId = :organizationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<StorageUnit> getStorageUnitList(Integer orgId) {
		String hql = "SELECT storageUnit FROM StorageUnit AS storageUnit "
				+ "WHERE storageUnit.isActive = true AND storageUnit.isDeleted = false AND storageUnit.organization.organizationId = :organizationId"
				+ " ORDER BY storageUnit.unitName ASC ";
//     globals/items/formulation/type
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<FormulationType> getFormulationTypeList(Integer orgId) {
		String hql = "SELECT formulationType FROM FormulationType AS formulationType "
				// + "INNER JOIN formulationType.productCategory as productCategory WITH
				// productCategory.id = :productCategoryId AND productCategory.isDeleted = false
				// AND productCategory.isActive = true AND productCategory.unit.unitId = :unitId
				// "
				+ "WHERE formulationType.isActive = true AND formulationType.isDeleted = false  AND formulationType.organization.organizationId = :organizationId "
				+ " ORDER BY formulationType.type ASC ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("productCategoryId", productCategoryId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<Manufacturer> getManufacturersList(Integer orgId) {
		String hql = "SELECT manufacturer FROM Manufacturer AS manufacturer "
				+ "WHERE manufacturer.isActive = true AND manufacturer.isDeleted = false  AND manufacturer.organization.organizationId = :organizationId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();

	}
//global/uom
	@Override
	public List<UomUnit> getUomUnitsList(Integer orgId) {
		String hql = "SELECT uomUnit FROM UomUnit AS uomUnit "
				+ "WHERE uomUnit.isActive = true AND uomUnit.isDeleted = false  AND uomUnit.organization.organizationId = :organizationId "
				+ " ORDER BY units ASC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<Generic> getGenericList(Integer productCategoryId, Integer orgId, String searchGenericName) {
		String hql = "SELECT generic FROM Generic AS generic "
				+ "INNER JOIN generic.productCategory as productCategory WITH  productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "WHERE generic.productCategory.id = :productCategoryId AND generic.isActive = true "
				+ " AND generic.isDeleted = false  AND generic.organization.organizationId = :organizationId"
				+ " AND LOWER(generic.name) like :searchGenericName "
				+ " ORDER BY generic.name";

		Query query = getSession().createQuery(hql);
		query.setParameter("productCategoryId", productCategoryId);
		query.setParameter("searchGenericName", "%"+searchGenericName.toLowerCase()+"%");
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<Tax> getTaxList(Integer orgId) {
		String hql = "SELECT tax FROM Tax AS tax "
				+ "WHERE tax.isActive = true AND tax.isDeleted = false AND tax.organization.organizationId = :organizationId"
				+ " ORDER BY tax.code ASC ";
	
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<TaxDropDown> getTaxListForWorkOrder(Integer orgId) {
		String hql = "SELECT tax.id AS taxId,tax.name AS taxName,tax.taxPercentage AS taxPercent FROM Tax AS tax "
				+ "WHERE tax.isActive = true AND tax.isDeleted = false AND tax.organization.organizationId = :organizationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(TaxDropDown.class));

		return query.list();
	}

	@Override
	public List<PatientRegistration> patientSearchByUhid(String uhid, Integer orgId) {

		String hql = "SELECT patient FROM PatientRegistrations AS patient " + "INNER JOIN patient.prefix AS prefix  "
				+ "WHERE patient.status = 'A' AND patient.organization.organizationId = :orgId  AND patient.registrationTypeId = 1 "
				+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("uhid", uhid != null ? '%'+ uhid.toLowerCase() + '%' : "");
		query.setParameter("orgId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<PatientSearchDetails> salesReturnPatientSearchByUhid(String uhid, Integer orgId) {

		String sql = """
				 select distinct pat.uhid_number as uhidNumber ,
				pre.prefix_code as prefixCode,  pat.first_name as firstName ,pat.middle_name as middleName,\s
				pat.last_name as lastName, pat.dob as dob,
				gen.gender_code as genderCode, pat.mobile_number as mobileNumber, pat.patient_id as patientId
				from opd.t_encounter_master enc
				inner join patient.t_patient_registration pat on pat.patient_id = enc.patient_id
				inner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id\s
				inner join public.m_gender_master gen on pat.gender_id = gen.gender_id
				 where enc.status_id = 5 and pat.status ='A' and pat.organization_id =:orgId \
				\t AND ((:uhid = '' OR lower(pat.uhid_number) LIKE :uhid ))\s\
				""";

		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("uhid", uhid != null ? uhid.toLowerCase() + '%' : "");
		query.setParameter("orgId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();
	}

	@Override
	public List<DoctorSpecialityMapperDto> doctorSearchByName(Integer orgId, Integer unitId, String name) {

		String query = "SELECT doc.doctor_id AS \"doctorId\", "
				+ "       CONCAT('Dr. ', COALESCE(doc.first_name), ' ', COALESCE(doc.middle_name), ' ', COALESCE(doc.last_name)) AS \"doctorName\" "
				+ "FROM doctor.m_doctor_master doc "
				+ "INNER JOIN t_user_unit_mapper uum ON uum.user_id = doc.user_id "
				+ "WHERE doc.organization_id = :orgId "
				+ "AND uum.unit_id = :unitId "
				+ "AND doc.status = 'A' "
				+ "AND uum.status = 'A' "
				+ "AND (LOWER(COALESCE(doc.first_name, '')) LIKE CONCAT(LOWER(:name), '%') "
				+ "OR LOWER(COALESCE(doc.last_name, '')) LIKE CONCAT(LOWER(:name), '%') "
				+ "OR LOWER(CONCAT(COALESCE(doc.first_name), ' ', COALESCE(doc.last_name, ''))) LIKE CONCAT('%', LOWER(:name), '%'))";

		Query queryObj = sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("orgId", orgId != null ? orgId : sessionService.getOrganizationId())
				.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId())
				.setParameter("name", name != null ? name : "");
		List<DoctorSpecialityMapperDto> list = queryObj
				.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class))
				.list();
		return list;
	}

	@Override
	public List<WardMasters> getWardList(Integer unitId) {
		String hql = "SELECT wardMaster FROM WardMasters AS wardMaster " + " WHERE wardMaster.unitId = :unitId"
				+ " AND wardMaster.status = 'A' "
				+ " ORDER BY wardName ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<FloorMasters> getFloorList(Integer unitId) {
		String hql = "SELECT floorMaster FROM FloorMasters AS floorMaster " + " WHERE floorMaster.unitId = :unitId"
				+ " AND floorMaster.status = 'A' "
				+ " ORDER BY floorName ASC";

		Query query = getSession().createQuery(hql);
		// query.setParameter("organizationId", sessionService.getOrganizationId());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();
	}

	/*
	 * @Override public List<DoctorSpecialityMapper> doctorSearchByIdValue(Integer
	 * id) {
	 * 
	 * String hql =
	 * "SELECT doctorSpeciality FROM DoctorSpeciality AS doctorSpeciality " +
	 * " INNER JOIN doctorSpeciality.doctor AS doctor WITH doctor.status = 'A' " +
	 * " INNER JOIN doctorSpeciality.speciality AS speciality WITH speciality.status = 'A' "
	 * +
	 * " WHERE  doctorSpeciality.status = 'A' AND doctorSpeciality.unitId = :unitId "
	 * + " AND (:id = doctor.id)";
	 * 
	 * if (id != null && id > 0) { Query query = getSession().createQuery(hql);
	 * query.setParameter("id", id); query.setParameter("unitId",
	 * sessionService.getUnitId()); return query.list(); } return null; }
	 */

	@Override
	public AdmissionDetail getPatientWardBedDetails(Integer admissionId) throws ApplicationException {
		try {
			String hql = "SELECT admissionDetail FROM AdmissionDetail AS admissionDetail "
					+ " INNER JOIN admissionDetail.admission AS admission WITH admission.status = 'A' "
					+ " WHERE admissionDetail.status = 'A' AND (:admissionId = admission.admissionId)";

			Query query = getSession().createQuery(hql);
			query.setParameter("admissionId", admissionId);
			// query.setParameter("untiId", sessionService.getUnitId());

			return (AdmissionDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while fetching patient ward bed details", e);
			return null;
		}
	}

	@Override
	public List<UomResponse> getBatchUomList(Long batchId) {
		String hql = "SELECT  uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, batchUomMapper.conversions AS conversion, "
				+ "CONCAT(uomType.code, ' - ', uomUnit.units) as uomDetails, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom  "
				+ "FROM BatchUomMapper AS batchUomMapper " + "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		INNER JOIN batchUomMapper.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN batchUomMapper.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.id = :batchId ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("batchId", batchId);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<UomResponse> getBatchUomListPharmacy(Long batchId) {
		String hql = "SELECT  uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, batchUomMapper.conversions AS conversion, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom  "
				+ "FROM BatchUomMapper AS batchUomMapper " + "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		INNER JOIN batchUomMapper.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN batchUomMapper.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isPharmacy = false "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.id = :batchId ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("batchId", batchId);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<Vendor> getVendorList(String name, Integer unitId) {

		String hql = "SELECT vendor FROM Vendor as vendor "
				+ " WHERE (lower(vendor.name) LIKE :name OR lower(vendor.code) LIKE :name "
				+ " AND vendor.isActive = true AND vendor.isDeleted = false) and (vendor.unit.unitId = :unitId )";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();

	}

	@Override
	public Integer getVendorIdByBatchNo(String batchNo) {

		return null;
	}

	@Override
	public String getVendorNameByVendorId(Integer vendorId) {
		return null;
	}

	@Override
	public List<Vendor> getServiceProviderList(String name, Integer orgId) {
		String hql = "SELECT vendor FROM Vendor as vendor "
				+ " WHERE (lower(vendor.name) LIKE :name OR lower(vendor.code) LIKE :name) "
				+ " AND   vendor.isActive = true AND vendor.isDeleted = false AND vendor.isServiceProvider = true   "
				+ "  AND  vendor.organization.organizationId =:organizationId  ";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<Company> getInsuranceCompanyList(String name, Integer unitId) {
		String hql = "SELECT company FROM Company as company "
				+ "WHERE lower(company.companyDesc) LIKE :name "
				+ "AND company.status = 'A' "
				+ "AND company.unitId = :unitId";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + "%" : "");
		query.setParameter("unitId", unitId);
		return query.list();
	}

	@Override
	public List<Manufacturer> getManufacturerList(String name, Integer orgId) {

		String hql = "SELECT manufacturer FROM Manufacturer as manufacturer "
				+ " WHERE ( lower(manufacturer.name) LIKE :name OR lower(manufacturer.code) LIKE :name )"
				+ " AND ( manufacturer.isActive = true AND manufacturer.isDeleted = false AND  manufacturer.organization.organizationId =:organizationId ) ";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		// query.setParameter("unitId", sessionService.getUnitId());
		return query.list();

	}

	@Override
	public ItemsDetailsResponse itemsDetail(int id) {
		/*
		 * String hql =
		 * "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, generic.name AS genericName, "
		 * +
		 * "		manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, salesTax.id AS taxId, salesTax.name AS taxName, "
		 * +
		 * "		salesTax.taxPercentage AS taxPercent, item.isOtc AS isOtc, formulationType.type AS formulationType, "
		 * +
		 * "		concat(item.strength, ' ', strengthUnit.units) as strength, (case when item.productCategory.id is null then generic.productCategory.id else item.productCategory.id end) as productCategoryId   "
		 * + " FROM Item AS item " +
		 * "		left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
		 * +
		 * "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
		 * +
		 * "		INNER JOIN item.salesTax AS salesTax WITH salesTax.isDeleted = false AND salesTax.isActive = true "
		 * +
		 * " 	LEFT JOIN item.formulationType AS formulationType WITH formulationType.isDeleted = false AND formulationType.isActive = true "
		 * +
		 * "		LEFT JOIN item.strengthUnit as strengthUnit WITH strengthUnit.isActive = true AND strengthUnit.isDeleted = false "
		 * + "WHERE item.id = :id AND item.isDeleted = false AND item.isActive = true ";
		 */

		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, generic.name AS genericName, "
				+ "		manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, salesTax.id AS taxId, salesTax.name AS taxName, "
				+ "		salesTax.taxPercentage AS taxPercent, item.isOtc AS isOtc, formulationType.type AS formulationType, item.isLasa as isLasa, "
				+ "		concat(item.strength, ' ', strengthUnit.units) as strength, (case when item.productCategory.id is null then generic.productCategory.id else item.productCategory.id end) as productCategoryId   "
				+ " FROM Item AS item "
				+ "		left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.salesTax AS salesTax WITH salesTax.isDeleted = false AND salesTax.isActive = true "
				+ " 	LEFT JOIN item.formulationType AS formulationType WITH formulationType.isDeleted = false AND formulationType.isActive = true "
				+ "		LEFT JOIN item.strengthUnit as strengthUnit WITH strengthUnit.isActive = true AND strengthUnit.isDeleted = false "
				+ "WHERE item.id = :id AND item.isDeleted = false AND item.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsResponse.class));

		return (ItemsDetailsResponse) query.uniqueResult();
	}

	@Override
	public ItemsDetailsResponse itemsDetailTariff(int id) {
		try {
			String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, item.isThreeUOMApplicable AS isThreeUOMApplicable, item.isLasa as isLasa, generic.id AS genericId, generic.name AS genericName, "
					+ "		manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, salesTax.id AS taxId, salesTax.name AS taxName, "
					+ "		salesTax.taxPercentage AS taxPercent, item.isOtc AS isOtc, formulationType.type AS formulationType, formulationType.id AS formulationTypeId, "
					+ "		concat(item.strength, ' ', strengthUnit.units) as strength, coalesce(generic.productCategory.id,item.productCategory.id) as productCategoryId, storageUnit.unitName as storageUnitName,item.isNarcotic as isNarcotic,item.isOtc as isOTC  "
					+ " FROM Item AS item "
					+ "		left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
					+ "		INNER JOIN item.salesTax AS salesTax WITH salesTax.isDeleted = false AND salesTax.isActive = true "
					+ " 	LEFT JOIN item.formulationType AS formulationType WITH formulationType.isDeleted = false AND formulationType.isActive = true "
					+ "		LEFT JOIN item.strengthUnit as strengthUnit WITH strengthUnit.isActive = true AND strengthUnit.isDeleted = false "
					+ "     LEFT JOIN item.storageUnit as storageUnit WITH storageUnit.isActive = true AND storageUnit.isDeleted = false "
					+ "WHERE item.id = :id AND item.isDeleted = false AND item.isActive = true ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsResponse.class));

			return (ItemsDetailsResponse) query.uniqueResult();
		}
		catch (Exception e){
			logger.error("Error while fetching item details tariff", e);
			return null;
		}
	}

	@Override
	public User getUserById(Integer id) {
		String hql = "SELECT user FROM User AS user WHERE user.status = 'A' AND user.id = :id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (User) query.uniqueResult();
	}

	@Override
	public List<UserMasterDto> getUserByName(String name, Integer unitId, Integer orgId) {
		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_USERS_LIST").getQueryString());
			query.append(" and (lower(coalesce(employee.first_name, '')) like concat( lower(:name), '%') "
					+ "or lower(coalesce(employee.last_name, '')) like concat( lower(:name), '%')) "
					+ "or lower(concat(coalesce(employee.first_name), ' ', coalesce(employee.last_name, ''))) like concat('%', lower(:name), '%'))");
			query.append(" ORDER BY name ASC ");
			Query hqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			hqlQuery.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
			hqlQuery.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			hqlQuery.setParameter("name", name != null ? name.toLowerCase() : "");
			List<UserMasterDto> userMasterDtosList = hqlQuery.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
			return userMasterDtosList;
		} catch (Exception e) {
			logger.error("Error while fetching user by name", e);
			return null;
		}
	}

	@Override
	public List<User> getUser(Integer orgId, Integer unitId) {
		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_USERS_LIST").getQueryString());

			List<User> userMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId())
					.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(User.class)).list();

			return userMasterDtosList;
		} catch (Exception e) {
			logger.error("Error while fetching users for organization/unit", e);
			return null;
		}
	}

	@Override
	public Item getItemById(Integer id) {
		// String hql = "SELECT item FROM Item AS item WHERE item.isActive = true AND
		// item.isDeleted = false AND item.id = :id AND item.unit.unitId = :unitId ";
		String hql = "SELECT item FROM Item AS item WHERE item.isActive = true AND item.isDeleted = false AND item.id = :id";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		// query.setParameter("unitId", sessionService.getUnitId());

		return (Item) query.uniqueResult();
	}

	public List<PurchaseRequest> searchLocalPuchaseRequest(AgainstPurchaseRequest purchaseRequestResponse,
			List<Integer> statusIds) {
		String hql = "SELECT purchaseRequestMaster FROM PurchaseRequest AS purchaseRequestMaster "
				+ " 	INNER JOIN purchaseRequestMaster.addedBy as user  "
				+ " 	LEFT JOIN purchaseRequestMaster.store as store  "
				+ " 	LEFT JOIN purchaseRequestMaster.assetType as assetType "
				+ " 	INNER JOIN purchaseRequestMaster.approvalStatus as approvalStatus "
				+ " WHERE purchaseRequestMaster.isActive = true AND purchaseRequestMaster.isDeleted = false AND purchaseRequestMaster.unit.unitId = :unitId"
				+ "		AND purchaseRequestMaster.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:storeId = 0 OR store.id = :storeId) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:purchaseNo = '' OR lower(purchaseRequestMaster.purchaseNo) LIKE :purchaseNo) "
				+ "     AND (purchaseRequestMaster.approvalStatus.id IN (:statusIds)) "
				+ " AND purchaseRequestMaster.id IN (SELECT detail.purchaseRequest.id FROM PurchaseRequestDetail AS detail WHERE detail.status.id IN (:statusIds) "
				+ "											)"
				+ " ORDER BY purchaseRequestMaster.id DESC ";

		// + " AND quotationRequest.id NOT IN (SELECT qv.quotationRequest.id FROM
		// QuotationVendor AS qv "
		// + " WHERE qv.vendor.id = :vendorId)"

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(purchaseRequestResponse.getFromDate()));
		query.setParameter("toDate", Util.getDate(purchaseRequestResponse.getToDate()));
		query.setParameter("storeId",
				purchaseRequestResponse.getStoreId() != null ? purchaseRequestResponse.getStoreId() : 0);
		query.setParameter("assetTypeId",
				purchaseRequestResponse.getAssetTypeId() != null ? purchaseRequestResponse.getAssetTypeId() : 0);
		query.setParameter("purchaseNo",
				purchaseRequestResponse.getPrNo() != null ? '%' + purchaseRequestResponse.getPrNo().toLowerCase() + '%'
						: "");
		query.setParameterList("statusIds", statusIds);
		// query.setParameter("detailStatusId", 5);
		query.setParameter("unitId", purchaseRequestResponse.getUnitId() != null ? purchaseRequestResponse.getUnitId()
				: sessionService.getUnitId());
		return query.list();
	}

	public List<QuotationRequest> searchLocalQuotationRequest(AgainstPurchaseRequest quotationFillRequest,
			List<Integer> statusIds) {

		String hql = "SELECT quotationRequest FROM QuotationVendor AS quotationVendor "
				+ "	INNER JOIN quotationVendor.addedBy as user  " + "	LEFT JOIN quotationVendor.store as store  "
				+ " INNER JOIN quotationVendor.status AS status "
				+ " INNER JOIN quotationVendor.quotationRequest AS quotationRequest "
				+ " INNER JOIN quotationVendor.vendor AS vendor "
				+ " INNER JOIN quotationRequest.assetType AS assetType "
				+ " INNER JOIN quotationVendor.approvalStatus as approvalStatus "
				+ " WHERE quotationVendor.isActive = true AND quotationVendor.isDeleted = false AND quotationVendor.unit.unitId = :unitId "
				+ " 	AND quotationVendor.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "   	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ " 	AND (:vqNumber = '' OR lower(quotationVendor.vqNumber) LIKE :vqNumber) "
				+ " 	AND (:qrNo = '' OR lower(quotationRequest.qrNo) LIKE :qrNo) "
				+ "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId)"
				+ "     AND (approvalStatus.id IN (:approvalStatusId)) " + " ORDER BY quotationVendor.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(quotationFillRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(quotationFillRequest.getToDate()));
		query.setParameter("storeId",
				quotationFillRequest.getStoreId() != null ? quotationFillRequest.getStoreId() : 0);
		query.setParameter("vendorId",
				quotationFillRequest.getVendorId() != null ? quotationFillRequest.getVendorId() : 0);
		query.setParameter("vqNumber",
				quotationFillRequest.getVqrNumber() != null
						? '%' + quotationFillRequest.getVqrNumber().toLowerCase() + '%'
						: "");
		query.setParameter("assetTypeId",
				quotationFillRequest.getAssetTypeId() != null ? quotationFillRequest.getAssetTypeId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", quotationFillRequest.getUnitId() != null ? quotationFillRequest.getUnitId()
				: sessionService.getUnitId());
		query.setParameter("qrNo",
				quotationFillRequest.getQrNo() != null ? '%' + quotationFillRequest.getQrNo().toLowerCase() + '%' : "");
		return query.list();

	}

	public List<WorkOrderCreation> searchLocalWOQuotationRequest(AgainstWORequest againstWORequest,
			List<Integer> statusIds, Integer unitId, Integer orgId) {

		String hql = "SELECT workOrderCreationMaster FROM WorkOrderCreation AS workOrderCreationMaster "
				+ " 	INNER JOIN workOrderCreationMaster.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN workOrderCreationMaster.store as store WITH store.unit.unitId = :unitId "
				+ " 	INNER JOIN workOrderCreationMaster.approvalStatus as approvalStatus "
				+ " WHERE workOrderCreationMaster.isActive = true AND workOrderCreationMaster.isDeleted = false AND workOrderCreationMaster.unit.unitId = :unitId"
				+ "		AND workOrderCreationMaster.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:storeId = 0 OR store.id = :storeId) "
				+ " 	AND (:workOrderNo = '' OR lower(workOrderCreationMaster.workOrderNo) LIKE :workOrderNo) "
				+ "     AND (workOrderCreationMaster.approvalStatus.id IN (:statusIds))"
				+ " AND workOrderCreationMaster.id IN (SELECT detail.workOrderCreation.id FROM WorkOrderCreationDetail AS detail WHERE detail.status.id IN (:statusIds)"
				+ "											and detail.workOrderDetail IS NULL)"
				// + " AND workOrderCreationMaster.id IN (SELECT detail.workOrderCreation.id
				// FROM WorkOrderCreationDetail AS detail WHERE detail.status.id =
				// :detailStatusId)"
				+ " ORDER BY workOrderCreationMaster.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(againstWORequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(againstWORequest.getToDate()));
		query.setParameter("storeId", againstWORequest.getStoreId() != null ? againstWORequest.getStoreId() : 0);
		query.setParameter("workOrderNo",
				againstWORequest.getWrNo() != null ? againstWORequest.getWrNo().toLowerCase() + '%' : "");
		query.setParameterList("statusIds", statusIds);
		// query.setParameter("detailStatusId", 5);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public PurchaseRequest getPurchaseRequestList(Long id) {
		String hql = "SELECT purchaseRequest FROM PurchaseRequest AS purchaseRequest "
				+ "WHERE purchaseRequest.id = :id  AND purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (PurchaseRequest) query.uniqueResult();
	}

	@Override
	public QuotationRequest getQuotationRequestList(Integer id) {
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
				+ "WHERE quotationRequest.id = :id  AND quotationRequest.isActive = true AND quotationRequest.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (QuotationRequest) query.uniqueResult();
	}

	@Override
	public List<PurchaseRequestDetailResponse> getItemsDetailsAgainstPr(Long prId, Integer Id) {
		String hql = " SELECT purchaseRequestDetail.id AS id,purchaseRequestDetail.approvedQuantity AS prQuantity,store.storeId AS storeId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, generic.name AS genericName, "
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, purchaseTax.id AS taxId, purchaseTax.name AS taxName, "
				+ " purchaseTax.taxPercentage AS taxPercent, uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit , purchaseRequestDetail.pendingQuantity   as pendingQuantity,item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail "
				+ "  INNER JOIN purchaseRequestDetail.purchaseRequest  AS purchaseRequest"
				+ "  INNER JOIN purchaseRequest.store AS store " + "  INNER JOIN purchaseRequestDetail.item  AS item"
				+ "  INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "  LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "  LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
				+ "  LEFT JOIN purchaseRequestDetail.uomType AS uomType WITH uomType.isDeleted = false AND uomType.isActive = true "
				+ "  LEFT JOIN purchaseRequestDetail.uomUnit AS uomUnit WITH uomUnit.isDeleted = false AND uomUnit.isActive = true "
				+ "  WHERE purchaseRequestDetail.id = :Id AND purchaseRequestDetail.purchaseRequest.id = :prId AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
				+ "  ORDER BY purchaseRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("Id", Id);
		query.setParameter("prId", prId);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseRequestDetailResponse.class));
		return query.list();

	}

	@Override
	public List<QuotationRequestDetailResponse> getItemsDetailsAgainstQr(Integer qrId, Integer Id) {
		String hql = " SELECT quotationRequestDetail.id AS id,quotationRequestDetail.quantity AS qrQuantity,quotationRequestDetail.pendingQuantity AS pendingPOqty,store.storeId AS storeId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, generic.name AS genericName, "
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, purchaseTax.id AS taxId, purchaseTax.name AS taxName, "
				+ " purchaseTax.taxPercentage AS taxPercent, uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit "
				+ " FROM QuotationRequestDetail AS quotationRequestDetail "
				+ "  INNER JOIN quotationRequestDetail.quotationRequest  AS quotationRequest"
				+ "  INNER JOIN quotationRequest.store AS store " + "  INNER JOIN quotationRequestDetail.item  AS item"
				+ "  INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "  LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "  LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
				+ "  LEFT JOIN quotationRequestDetail.uomType AS uomType WITH uomType.isDeleted = false AND uomType.isActive = true "
				+ "  LEFT JOIN quotationRequestDetail.uomUnit AS uomUnit WITH uomUnit.isDeleted = false AND uomUnit.isActive = true "
				+ "  WHERE quotationRequestDetail.id = :Id AND quotationRequestDetail.quotationRequest.id = :qrId AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
				+ "  ORDER BY quotationRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("Id", Id);
		query.setParameter("qrId", qrId);

		query.setResultTransformer(Transformers.aliasToBean(QuotationRequestDetailResponse.class));
		return query.list();

	}

	@Override
	public List<PurchaseRequestDetailResponse> getFixedItemsDetailsAgainstPr(Long Id, Integer itemId) {
		String hql = " SELECT purchaseRequestDetail.id AS id,purchaseRequestDetail.approvedQuantity AS prQuantity,store.storeId AS storeId, item.id AS itemId, item.code AS itemCode, item.name AS itemName,"
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName,purchaseTax.id AS taxId, purchaseTax.name AS taxName, "
				+ " purchaseTax.taxPercentage AS taxPercent , purchaseRequestDetail.pendingQuantity   as pendingQuantity "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail "
				+ "  INNER JOIN purchaseRequestDetail.item  AS item"
				+ "  INNER JOIN purchaseRequestDetail.purchaseRequest  AS purchaseRequest"
				+ "  INNER JOIN purchaseRequest.store AS store "
				+ "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "  LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
				+ "  WHERE purchaseRequestDetail.id = :itemId AND purchaseRequestDetail.purchaseRequest.id = :Id AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
				+ "  ORDER BY purchaseRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("Id", Id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseRequestDetailResponse.class));
		return query.list();

	}

	@Override
	public List<QuotationRequestDetailResponse> getFixedItemsDetailsAgainstQr(Integer Id, Integer itemId) {
		String hql = " SELECT quotationRequestDetail.id AS id,quotationRequestDetail.quantity AS qrQuantity,store.storeId AS storeId, item.id AS itemId, item.code AS itemCode, item.name AS itemName,"
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName,purchaseTax.id AS taxId, purchaseTax.name AS taxName, "
				+ " purchaseTax.taxPercentage AS taxPercent "
				+ " FROM QuotationRequestDetail AS quotationRequestDetail "
				+ "  INNER JOIN quotationRequestDetail.item  AS item"
				+ "  INNER JOIN quotationRequestDetail.quotationRequest  AS quotationRequest"
				+ "  INNER JOIN quotationRequest.store AS store "
				+ "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "  LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
				+ "  WHERE quotationRequestDetail.id = :itemId AND quotationRequestDetail.quotationRequest.id = :Id AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
				+ "  ORDER BY quotationRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("Id", Id);

		query.setResultTransformer(Transformers.aliasToBean(QuotationRequestDetailResponse.class));
		return query.list();

	}

	@Override
	public List<OtherCharge> getOtherCharges() {
		String hql = "SELECT otherCharge FROM OtherCharge AS otherCharge WHERE otherCharge.isActive = true AND otherCharge.isDeleted = false";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<PaymentMode> getPaymentModeList() {
		String hql = "SELECT paymentMode FROM PaymentMode AS paymentMode " + "WHERE paymentMode.status = 'A' ";
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public Batch getBatchByBatchUom(Long id) {
		String hql = " SELECT batch FROM Batch as batch " + "	INNER JOIN batch.batchUomList AS batchUom "
				+ " WHERE batch.id = :id  AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false "
				+ "	AND batchUom.isDeleted = false AND batchUom.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Batch) query.uniqueResult();
	}

	@Override
	public Batch getUomByBatchUomMapper(Long id) {
		String hql = " SELECT batch FROM Batch as batch " + "	INNER JOIN batch.batchUomList AS batchUom "
				+ " WHERE batch.id = :id  AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false "
				+ "	AND batchUom.isDeleted = false AND batchUom.isActive = true AND batchUom.storeUom = true";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Batch) query.uniqueResult();
	}

	@Override
	public Batch getBatch(Long id) {

		if (id != null && id > 0) {
			String hql = "SELECT batch FROM Batch as batch  WHERE batch.id = :id  AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (Batch) query.uniqueResult();
		}
		return null;
	}

	// added by minesh
	/*
	 * This Method is use for to get converted batch from given batchId
	 * 
	 * @id : batchId
	 * 
	 * @result : return single converted batch
	 */
	@Override
	public Batch getConvertedBatch(Long id) {
		Batch batch = getBatch(id);

		/*
		 * String sql =
		 * "select * from inventory.t_batch as batch Where batch.batch_no = :batchNo AND batch.expiry_date = :expiryDate "
		 * +
		 * " AND batch.cop = :cop AND batch.selling_price = :sellingPrice AND batch.item_id = :item "
		 * + " AND batch.id != :id " +
		 * " AND batch.is_active = true AND batch.is_deleted = false AND batch.is_quarantine = false"
		 * ;
		 * 
		 * Query query = getSession().createSQLQuery(sql); query.setParameter("batchNo",
		 * batch.getBatchNo()); query.setParameter("expiryDate", batch.getExpiryDate());
		 * query.setParameter("sellingPrice", batch.getSellingPrice());
		 * query.setParameter("cop", batch.getCop()); query.setParameter("item",
		 * batch.getItem()); query.setParameter("id", batch.getId());
		 * query.setResultTransformer(Transformers.aliasToBean(Batch.class)); Batch
		 * convertedBatch = (Batch) query.setMaxResults(1).uniqueResult();
		 */
		String hql = "SELECT batch FROM Batch as batch  WHERE batch.batchNo = :batchNo AND batch.expiryDate = :expiryDate AND batch.sellingPrice=:sellingPrice "
				+ " AND batch.cop =:cop AND batch.item=:item " + " AND batch.id != :id "
				+ "AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false ";
		Query queryHql = getSession().createQuery(hql);
		queryHql.setParameter("batchNo", batch.getBatchNo());
		queryHql.setParameter("expiryDate", batch.getExpiryDate());
		queryHql.setParameter("sellingPrice", batch.getSellingPrice());
		queryHql.setParameter("cop", batch.getCop());
		queryHql.setParameter("item", batch.getItem());
		queryHql.setParameter("id", batch.getId());
		Batch convertedBatchHql = (Batch) queryHql.setMaxResults(1).uniqueResult();

		return convertedBatchHql;
	}

	public Batch getExistedConvertedBatch(Long id) {
		Batch batch = getBatch(id);
		String hql = "SELECT batch FROM Batch as batch  WHERE batch.batchNo = :batchNo AND batch.expiryDate = :expiryDate AND batch.sellingPrice=:sellingPrice "
				+ " AND batch.cop =:cop AND batch.item=:item "
				+ " AND batch.id != :id AND batch.isConsignment = false AND batch.currentQuantity > 0 "
				+ " AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false ORDER BY batch.addedDate DESC ";
		Query queryHql = getSession().createQuery(hql);
		queryHql.setParameter("batchNo", batch.getBatchNo());
		queryHql.setParameter("expiryDate", batch.getExpiryDate());
		queryHql.setParameter("sellingPrice", batch.getSellingPrice());
		queryHql.setParameter("cop", batch.getCop());
		queryHql.setParameter("item", batch.getItem());
		queryHql.setParameter("id", batch.getId());
		Batch convertedBatchHql = (Batch) queryHql.setMaxResults(1).uniqueResult();

		return convertedBatchHql;
	}

	// created for calculate conversion status for consignment convert
	public List<Batch> getBatchListForConversion(String lotNo, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ " INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ " INNER JOIN batch.storeStockList AS storeStock "
				+ " WHERE  batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId AND batch.lotNo = :lotNo"
				+ " AND storeStock.store.storeId = :storeId AND batch.expiryDate >= :greaterThanCurrentMonth "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("lotNo", lotNo);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);

		return query.list();
	}

	public List<Batch> getBatchListForConversion(int grnId, Integer unitId) {

		try {
			String hql = "SELECT batch FROM Batch AS batch "
					+ " INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
					// + " INNER JOIN batch.storeStockList AS storeStock "
					+ " INNER JOIN batch.goodReceiptNoteDetail AS goodReceiptNoteDetail "
					+ " WHERE  batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
					// + " AND storeStock.store.storeId = :storeId "
					+ " AND goodReceiptNoteDetail.goodReceiptNote.id = :grnId" + " ORDER BY batch.expiryDate ASC";
			Query query = getSession().createQuery(hql);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			// query.setParameter("storeId", sessionService.getStoreId());
			query.setParameter("grnId", grnId);
			return query.list();

		} catch (Exception e) {
			logger.error("Error while fetching batch list for conversion", e);
			return null;
		}
	}

	// use for material issue.
	public List<Batch> getSameBatchListFromStoreSQL(String batchNum, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			String sql = "select  batch.id as id,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment " + "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id AND (item1.is_deleted=false AND item1.is_active=true AND item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where batch.is_deleted=false "
					+ "AND batch.is_active=true AND batch.unit_id=:unitId AND batch.batch_no= :batchNum "
					+ "AND (batch.is_pharmacy=false or batch.is_indent=false) AND batch.is_return=false AND batch.is_quarantine=false "
					+ "AND storestock2.store_id= :storeId AND storestock2.available_qty > 0 "
					+ "AND batch.expiry_date > :greaterThanCurrentMonth order by batch.current_quantity ASC ";

			SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG)
					.addScalar("batchNo", StandardBasicTypes.STRING).addScalar("lotNo", StandardBasicTypes.STRING)
					.addScalar("quantity", StandardBasicTypes.INTEGER)
					.addScalar("currentQuantity", StandardBasicTypes.INTEGER)
					.addScalar("expiryDate", StandardBasicTypes.DATE).addScalar("cop", StandardBasicTypes.DOUBLE)
					.addScalar("rate", StandardBasicTypes.DOUBLE).addScalar("sellingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("lendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("actualLendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("yearOfManufacture", StandardBasicTypes.INTEGER)
					.addScalar("rejectedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("returnedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN);
			query.setParameter("batchNum", batchNum);
			query.setParameter("unitId", unitId != null ? sessionService.getUnitId() : unitId);
			query.setParameter("storeId", storeId);
			query.setParameter("greaterThanCurrentMonth", current);
			query.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return query.list();

		} catch (Exception e) {
			logger.error("Error while fetching same batch list from store", e);
			return null;
		}
	}

	// use for consumption
	public List<Batch> getSameBatchList(Integer itemId, String batchNum, String lotNum, Integer storeId,
			Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			String condition = "";
			if (lotNum != null) {
				condition = " AND batch.lot_no= :lotNo ";
			}
			String sql = "select  batch.id as id,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment " + "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id AND (item1.is_deleted=false AND item1.is_active=true AND item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id AND batch.is_deleted=false "
					+ "AND batch.is_active=true AND batch.unit_id=:unitId "
					+ " AND batch.item_id= :id AND batch.batch_no= :batchNum " + condition
					+ " AND (batch.is_pharmacy=false or batch.is_indent=false) AND batch.is_return=false AND batch.is_quarantine=false "
					+ "AND storestock2.store_id= :storeId AND storestock2.available_qty > 0 "
					+ "AND batch.expiry_date >= :greaterThanCurrentMonth order by batch.current_quantity ASC";

			SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG)
					.addScalar("batchNo", StandardBasicTypes.STRING).addScalar("lotNo", StandardBasicTypes.STRING)
					.addScalar("quantity", StandardBasicTypes.INTEGER)
					.addScalar("currentQuantity", StandardBasicTypes.INTEGER)
					.addScalar("expiryDate", StandardBasicTypes.DATE).addScalar("cop", StandardBasicTypes.DOUBLE)
					.addScalar("rate", StandardBasicTypes.DOUBLE).addScalar("sellingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("lendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("actualLendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("yearOfManufacture", StandardBasicTypes.INTEGER)
					.addScalar("rejectedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("returnedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN);
			query.setParameter("id", itemId);
			query.setParameter("batchNum", batchNum);
			if (lotNum != null) {
				query.setParameter("lotNo", lotNum);
			}
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", storeId != null ? storeId : sessionService.getStoreId());
			query.setParameter("greaterThanCurrentMonth", current);
			query.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return query.list();

		} catch (Exception e) {
			logger.error("Error while fetching same batch list", e);
			return null;
		}
	}

	// use for consumption
	public List<Batch> getSameBatchListHQL(Integer itemId, String batchNum, String lotNum, Integer unitId,
			Integer storeId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			String hql = "SELECT batch.id as id,batch.item as item , batch.batchNo as batchNo ,batch.lotNo as lotNo ,batch.quantity as quantity, "
					+ " batch.currentQuantity as currentQuantity, batch.expiryDate as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ " batch.sellingPrice as sellingPrice, batch.lendingPrice as lendingPrice, batch.actualLendingPrice as actualLendingPrice, "
					+ " batch.yearOfManufacture as yearOfManufacture, batch.rejectedQuantity as rejectedQuantity, batch.returnedQuantity as returnedQuantity, "
					+ " batch.isConsignment as isConsignment " + " FROM Batch AS batch "
					+ " INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
					+ " INNER JOIN batch.storeStockList AS storeStock "
					+ " WHERE  batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId AND "
					+ "batch.lotNo = :lotNo AND batch.batchNo= :batchNum AND "
					+ "item.id= :id AND storeStock.store.storeId = :storeId AND batch.expiryDate >= :greaterThanCurrentMonth order by batch.currentQuantity ASC";
			Query queryHql = getSession().createQuery(hql);
			queryHql.setParameter("id", itemId);
			queryHql.setParameter("batchNum", batchNum);
			queryHql.setParameter("lotNo", lotNum);
			queryHql.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			queryHql.setParameter("storeId", storeId != null ? storeId : sessionService.getStoreId());
			queryHql.setParameter("greaterThanCurrentMonth", current);
			queryHql.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return queryHql.list();

		} catch (Exception e) {
			logger.error("Error while fetching same batch list via HQL", e);
			return null;
		}
	}

	public List<StoreStock> getStoreStockListByBatchAndStore(List<Long> batchIds, Integer storeId, Integer unitd) {
		try {
			String hql = " SELECT storeStock FROM StoreStock AS storeStock "
					+ " INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
					+ " INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
					+ " WHERE store.storeId = :storeId AND batch.id in (:batchIds) "
					+ " AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId AND storeStock.availableQty >= 0 "
					+ " ORDER BY batch.id , storeStock.availableQty desc ";

			Query query = getSession().createQuery(hql);
			query.setParameter("storeId", storeId);
			query.setParameterList("batchIds", batchIds);
			query.setParameter("unitId", unitd != null ? unitd : sessionService.getUnitId());
			return query.list();

		} catch (Exception e) {
			logger.error("Error while fetching store stock list by batch and store", e);
			return null;
		}
	}

	public List<StoreStock> getStoreStockListByBatchIdAndStoreId(Long batchId, Integer storeId, Integer unitId) {
		try {
			String sql = "";
			Batch batch = (batchId != null && batchId > 0) ? getBatch(batchId) : null;
			if (batch != null && batch.getLotNo() != null && !batch.getLotNo().isEmpty()
					&& batch.getBatchNo() == null) {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON " + "  " + " batch1.item_id=batch2.item_id "
						+ " AND batch1.lot_no=batch2.lot_no "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			}

			else if (batch != null && batch.getBatchNo() != null && !batch.getBatchNo().isEmpty()
					&& batch.getLotNo() != null && !batch.getLotNo().isEmpty()) {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON " + " batch1.batch_no = batch2.batch_no AND "
						+ " batch1.item_id=batch2.item_id " + " AND batch1.lot_no=batch2.lot_no "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			}

			else if (batch != null && batch.getBatchNo() != null && !batch.getBatchNo().isEmpty()
					&& batch.getLotNo() == null) {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON " + " batch1.batch_no = batch2.batch_no AND "
						+ " batch1.item_id=batch2.item_id " + "   "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			} else {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON " + "   " + " batch1.item_id=batch2.item_id "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			}

			Query query = getSession().createSQLQuery(sql).addScalar("batchId", StandardBasicTypes.LONG);
			query.setParameter("batchId", batchId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			// remove batch number logic and add batch id logic
			List<Long> batchIds1 = new ArrayList<Long>();
			batchIds1.add(batchId);
			return getStoreStockListByBatchAndStore(batchIds1, storeId, unitId);

		} catch (Exception e) {
			logger.error("Error while fetching store stock list by batch id and store id", e);
			return null;
		}
	}

	public List<StoreStock> getStoreStockListByBatchIdAndStoreId(Long batchId, Integer storeId, String lotNo,
			Integer unitId) {
		try {
			String sql = "";

			if (lotNo != null && !lotNo.isEmpty()) {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON " + " batch1.batch_no = batch2.batch_no AND "
						+ " batch1.item_id=batch2.item_id " + " AND batch1.lot_no=batch2.lot_no "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			} else {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON " + " batch1.batch_no = batch2.batch_no AND "
						+ " batch1.item_id=batch2.item_id " + " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			}

			Query query = getSession().createSQLQuery(sql).addScalar("batchId", StandardBasicTypes.LONG);
			query.setParameter("batchId", batchId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			List<Long> batchIds = query.list();
			return getStoreStockListByBatchAndStore(batchIds, storeId, unitId);

		} catch (Exception e) {
			logger.error("Error while fetching store stock list by batch id, store id and lot no", e);
			return null;
		}
	}

	// use for walkin sale realloaction batch
	public List<Long> getConsignmentBatchIds(Long batchId, Integer storeId, Integer unitId) {
		try {
			Batch batch = getBatch(batchId);
			String batchNoCondition = " ";
			String lotCondition = " ";
			if (batch.getBatchNo() != null) {
				batchNoCondition = " AND batch.batch_no = :batchNo ";
			}
			if (batch.getLotNo() != null) {
				lotCondition = " AND batch.lot_no = :lotNo ";
			}
			String sql = "SELECT batch.id as batchId FROM inventory.t_batch AS batch "
					+ "LEFT JOIN inventory.t_store_stock as storStock on batch.id = storStock.batch_id "
					+ "WHERE batch.item_id = :itemId "
					+ "AND batch.unit_id = :unitId "
					+ batchNoCondition
					+ lotCondition
					+ "AND storStock.store_id = :storeId "
					+ "ORDER BY batch.id";

			Query query = getSession().createSQLQuery(sql)
					.addScalar("batchId", StandardBasicTypes.LONG);
			query.setParameter("itemId", batch.getItem().getId());
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			if (batch.getBatchNo() != null) {
				query.setParameter("batchNo", batch.getBatchNo());
			}
			if (batch.getLotNo() != null) {
				query.setParameter("lotNo", batch.getLotNo());
			}
			query.setParameter("storeId", storeId);
			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching consignment batch ids", e);
			return null;
		}
	}

	public List<Long> getConsignmentBatchIds(Integer itemId, String batchNo, String lotNo, Integer storeId,
			Integer unitId) {
		try {
			String batchNoCondition = " ";
			String lotCondition = " ";
			if (batchNo != null) {
				batchNoCondition = " AND batch.batch_no = :batchNo ";
			}
			if (lotNo != null) {
				lotCondition = " AND batch.lot_no = :lotNo ";
			}
			String sql = "SELECT batch.id as batchId FROM inventory.t_batch AS batch "
					+ "LEFT JOIN inventory.t_store_stock as storStock on batch.id = storStock.batch_id "
					+ "WHERE batch.item_id = :itemId "
					+ "AND batch.unit_id = :unitId "
					+ batchNoCondition
					+ lotCondition
					+ "AND storStock.store_id = :storeId "
					+ "ORDER BY batch.id";
			Query query = getSession().createSQLQuery(sql)
					.addScalar("batchId", StandardBasicTypes.LONG);
			query.setParameter("itemId", itemId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			if (batchNo != null) {
				query.setParameter("batchNo", batchNo);
			}
			if (lotNo != null) {
				query.setParameter("lotNo", lotNo);
			}
			query.setParameter("storeId", storeId);
			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching consignment batch ids by item, batch no and lot no", e);
			return null;
		}
	}

	public GoodReceiptNote getGoodReceiptNote(String batchNum, String lotNum) {
		String hql = "SELECT goodReceiptNote FROM GoodReceiptNote AS goodReceiptNote "
				+ "	INNER JOIN goodReceiptNote.goodReceiptNoteDetailsList AS goodReceiptNoteDetails "
				+ " WHERE goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
				+ " AND goodReceiptNoteDetails.batchNo = :batchNum AND goodReceiptNoteDetails.lotNumber = :lotNum";

		Query query = getSession().createQuery(hql);
		query.setParameter("batchNum", batchNum);
		query.setParameter("lotNum", lotNum);
		return (GoodReceiptNote) query.setMaxResults(1).uniqueResult();
	}

	public BigInteger consignmentBatchCount(Integer itemId, String batchNo, String lotNo, String expiryDate,
			Integer unitId) {

		try {
			String lotCondition = " ";
			String expiryDateCondition = " ";
			if (lotNo != null) {
				lotCondition = " AND batch.lot_no = :lotNo ";
			}
			if (expiryDate != null) {
				expiryDateCondition = " AND batch.expiry_date = :expiryDate ";
			}
			String sql = "SELECT count(*) as count FROM inventory.t_batch as batch WHERE "
					+ "batch.is_consignment = true "
					+ lotCondition
					+ expiryDateCondition
					+ "AND batch.item_id = :itemId "
					+ "AND batch.unit_id = :unitId ";

			StringBuffer sb = new StringBuffer(sql);
			if (batchNo != null && !batchNo.isEmpty()) {
				sb.append(" AND batch.batch_no = :batchNo");
			} else {
				sb.append(" AND batch.is_batch_required = false");
			}
			Query query = getSession().createSQLQuery(sb.toString());
			query.setParameter("itemId", itemId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			if (lotNo != null) {
				query.setParameter("lotNo", lotNo);
			}
			if (expiryDate != null) {
				query.setParameter("expiryDate", expiryDate);
			}
			if (batchNo != null && !batchNo.isEmpty()) {
				query.setParameter("batchNo", batchNo);
			}
			return (BigInteger) query.uniqueResult();
		} catch (Exception ex) {
			logger.error("Error while fetching consignment batch count", ex);
			return null;
		}
	}

	public Long consignmentBatchId(Integer itemId, String batchNo, String lotNo) {
		try {
			String batchNoCondition = " ";
			String lotCondition = " ";
			if (batchNo != null) {
				batchNoCondition = " AND batch.batch_no = :batchNo ";
			}
			if (lotNo != null) {
				lotCondition = " AND batch.lot_no = :lotNo ";
			}
			String sql = "SELECT batch.id as batchId FROM inventory.t_batch AS batch "
					+ "WHERE batch.is_consignment = true "
					+ "AND batch.item_id = :itemId "
					+ batchNoCondition
					+ lotCondition
					+ "AND batch.unit_id = :unitId";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter("itemId", itemId);
			query.setParameter("unitId", sessionService.getUnitId());
			if (batchNo != null) {
				query.setParameter("batchNo", batchNo);
			}
			if (lotNo != null) {
				query.setParameter("lotNo", lotNo);
			}
			return (Long) query.setMaxResults(1).uniqueResult();
		} catch (Exception ex) {
			logger.error("Error while fetching consignment batch id", ex);
			return null;
		}
	}

	// get batchId of same batchNumber and same Experidate.
	public List<Long> getConsignmentBatchListIdWithExpDate(Long batchId, Integer storeId) {
		try {
			String sql = "";
			if (storeId != null) {
				sql = "SELECT batch1.id AS batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON batch1.batch_no = batch2.batch_no AND batch1.expiry_date=batch2.expiry_date "
						+ " LEFT JOIN inventory.t_store_stock AS storestock ON storestock.batch_id = batch1.id "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId AND storestock.store_id=:storeId ";
			} else {
				sql = "SELECT batch1.id as batchId FROM inventory.t_batch AS batch1 "
						+ " LEFT JOIN inventory.t_batch AS batch2 ON batch1.batch_no = batch2.batch_no AND batch1.expiry_date=batch2.expiry_date "
						+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
			}

			Query query = getSession().createSQLQuery(sql).addScalar("batchId", StandardBasicTypes.LONG);
			query.setParameter("batchId", batchId);
			query.setParameter("unitId", sessionService.getUnitId());
			if (storeId != null) {
				query.setParameter("storeId", storeId);
			}
			return query.list();

		} catch (Exception e) {
			logger.error("Error while fetching consignment batch list id with expiry date", e);
			return null;
		}
	}

	public List<StoreStock> getStoreStockListByBatchIdAndStoreIdWithExpireDate(Long batchId, Integer storeId,
			Integer unitId) {
		try {
			String sql = "SELECT batch1.id AS batchId FROM inventory.t_batch AS batch1 "
					+ " LEFT JOIN inventory.t_batch AS batch2 ON batch1.batch_no = batch2.batch_no AND batch1.expiry_date=batch2.expiry_date "
					+ " LEFT JOIN inventory.t_store_stock AS storestock ON storestock.batch_id = batch1.id "
					+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId AND storestock.store_id=:storeId ";

			Query query = getSession().createSQLQuery(sql).addScalar("batchId", StandardBasicTypes.LONG);
			query.setParameter("batchId", batchId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", storeId);
			List<Long> batchIds = query.list();
			return getStoreStockListByBatchAndStore(batchIds, storeId, unitId);

		} catch (Exception e) {
			logger.error("Error while fetching store stock list by batch id and store id with expiry date", e);
			return null;
		}
	}

	/*
	 * public GoodReceiptNote getGoodReceiptNote(String batchNum, String lotNum) {
	 * String hql =
	 * "SELECT goodReceiptNote FROM GoodReceiptNote AS goodReceiptNote " +
	 * "	INNER JOIN goodReceiptNote.goodReceiptNoteDetailsList AS goodReceiptNoteDetails "
	 * +
	 * " WHERE goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
	 * +
	 * " AND goodReceiptNoteDetails.batchNo = :batchNum AND goodReceiptNoteDetails.lotNumber = :lotNum"
	 * ;
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("batchNum",
	 * batchNum); query.setParameter("lotNum", lotNum); return (GoodReceiptNote)
	 * query.setMaxResults(1).uniqueResult(); }
	 */

	@Override
	public Batch getBatchPharmacy(Long id) {
		String hql = "SELECT batch FROM Batch as batch  WHERE batch.id = :id  AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false AND batch.isPharmacy = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Batch) query.uniqueResult();
	}

	@Override
	public List<PurchaseOrder> searchPurchaseOrder(AgainstPurchaseOrder againstPurchaseOrder, List<Integer> statusIds) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder"

				+ " 	INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType WITH assetType.unit.unitId = :unitId "
				+ "		LEFT JOIN purchaseOrder.status as status "
				+ " 	INNER JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType "
				+ " 	LEFT JOIN purchaseOrder.requestType as requestType "
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
				+ " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:vendorId = 0 OR purchaseOrder.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName ) "
				+ "     AND (approvalStatus.id IN (:approvalStatusId))"
				// + " AND purchaseOrderDetail.status.id = :detailStatusId "
				+ " AND purchaseOrder.id IN (SELECT detail.purchaseOrder.id FROM PurchaseOrderDetail AS detail WHERE detail.status.id = :detailStatusId)"
				+ " ORDER BY purchaseOrder.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(againstPurchaseOrder.getFromDate()));
		query.setParameter("toDate", Util.getDate(againstPurchaseOrder.getToDate()));
		query.setParameter("storeId",
				againstPurchaseOrder.getStoreId() != null ? againstPurchaseOrder.getStoreId() : 0);
		query.setParameter("assetTypeId",
				againstPurchaseOrder.getAssetTypeId() != null ? againstPurchaseOrder.getAssetTypeId() : 0);

		if (againstPurchaseOrder.getVendorId() != null && againstPurchaseOrder.getVendorId() > 0) {
			query.setParameter("vendorId",
					againstPurchaseOrder.getVendorId() != null ? againstPurchaseOrder.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (againstPurchaseOrder.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					againstPurchaseOrder.getVendorName() != null
							? againstPurchaseOrder.getVendorName().toLowerCase() + '%'
							: "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("detailStatusId", 5);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}

	@Override
	public PurchaseOrder getPurchaseOrderList(Integer id) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder "
				+ "WHERE purchaseOrder.id = :id  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (PurchaseOrder) query.uniqueResult();
	}

	@Override
	public List<PurchaseOrderStaged> getPurchaseOrderStaged(Integer id) {
		String hql = "SELECT purchaseOrderStaged FROM PurchaseOrderStaged AS purchaseOrderStaged "
				+ "WHERE purchaseOrderStaged.purchaseOrderDetail.id = :id  AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.PurchaseOrderStaged> getPurchaseOrderStagedList(Integer id) {
		/*
		 * String hql =
		 * " SELECT poStaged.id AS id, poStaged.approvedQunatity AS poApprovedQuantity, "
		 * +
		 * " 		poStaged.stagedDate AS stagedDate, grnStaged.receivedQuantity AS receivedQuantity, "
		 * + "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity " +
		 * " FROM PurchaseOrderDetail AS purchaseOrderDetail " +
		 * "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
		 * +
		 * "		LEFT OUTER JOIN poStaged.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
		 * +
		 * " WHERE purchaseOrderDetail.id = :id AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
		 * + " ORDER BY poStaged.id ";
		 */

		String hql = " SELECT poStaged.id AS id, poStaged.approvedQunatity AS poApprovedQuantity, "
				+ " 		poStaged.stagedDate AS stagedDate " + " FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
				+ " WHERE purchaseOrderDetail.id = :id AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " ORDER BY poStaged.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.PurchaseOrderStaged.class));

		return query.list();

	}

	@Override
	public List<PurchaseOrderOtherChargesMapper> getPurchaseOrderOtherChargesMapper(Integer id) {
		String hql = "SELECT poOtherCharges FROM PurchaseOrderOtherChargesMapper AS poOtherCharges "
				+ "WHERE poOtherCharges.purchaseOrderDetail.id = :id  AND poOtherCharges.isActive = true AND poOtherCharges.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<PurchaseOrderDetailResponse> getItemsDetailsAgainstPO(Integer Id, Integer itemId) {
		String hql = " SELECT purchaseOrderDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ " purchaseOrderDetail.poApprovedQuantity AS poQuantity,purchaseOrderDetail.bonusApprovedQuantity AS bonusQuantity,"
				+ " purchaseOrderDetail.poLeaseQuantity AS poLeaseQuantity,purchaseOrderDetail.bonusLeaseQuantity AS bonusLeaseQuantity,"
				+ " poUomType.id AS poUomTypeId, poUomType.type AS poUomType, poUomUnit.id AS poUomUnitId, poUomUnit.units AS poUomUnit,"
				+ " bonusUomType.id AS bonusUomTypeId, bonusUomType.type AS bonusUomType, bonusUomUnit.id AS bonusUomUnitId, bonusUomUnit.units AS bonusUomUnit,"
				+ " purchaseOrderDetail.cop AS cop,purchaseOrderDetail.mrp AS mrp, purchaseOrderDetail.discount AS discount, "
				+ " purchaseOrderDetail.totalDiscount AS discountAmount,purchaseOrderDetail.totalOtherAmount AS totalOtherAmount, purchaseOrderDetail.taxPercentage AS taxPercentage, "
				+ " generic.id AS genericId, generic.name AS genericName, "
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, tax.id AS taxId, tax.name AS taxName, "
				+ " tax.taxPercentage AS taxPercent, purchaseOrderDetail.specification AS specification"
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail " + "  INNER JOIN purchaseOrderDetail.item  AS item"
				+ "  INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "  INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
				+ "  INNER JOIN purchaseOrderDetail.tax AS tax WITH tax.isDeleted = false AND tax.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.poUomType as poUomType WITH poUomType.isDeleted = false AND poUomType.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.poUomUnit as poUomUnit WITH poUomUnit.isDeleted = false AND poUomUnit.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.bonusUomType as bonusUomType WITH bonusUomType.isDeleted = false AND bonusUomType.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.bonusUomUnit as bonusUomUnit WITH bonusUomUnit.isDeleted = false AND bonusUomUnit.isActive = true "
				+ "  WHERE purchaseOrderDetail.id = :itemId AND purchaseOrderDetail.purchaseOrder.id = :Id "
				+ "	AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "  ORDER BY purchaseOrderDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("Id", Id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailResponse.class));
		return query.list();

	}

	@Override
	public List<PurchaseOrderDetailResponse> getFixedItemsDetailsAgainstPO(Integer Id, Integer itemId) {
		String hql = " SELECT purchaseOrderDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ " purchaseOrderDetail.poApprovedQuantity AS poQuantity,purchaseOrderDetail.bonusApprovedQuantity AS bonusQuantity,"
				+ " purchaseOrderDetail.poLeaseQuantity AS poLeaseQuantity,purchaseOrderDetail.bonusLeaseQuantity AS bonusLeaseQuantity,"
				+ " purchaseOrderDetail.cop AS cop,purchaseOrderDetail.mrp AS mrp, purchaseOrderDetail.discount AS discount, "
				+ " purchaseOrderDetail.totalDiscount AS discountAmount,purchaseOrderDetail.totalOtherAmount AS totalOtherAmount, purchaseOrderDetail.taxPercentage AS taxPercentage, "
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, "
				// + ", tax.id AS taxId, tax.name AS taxName, "
				// + " tax.taxPercentage AS taxPercent, "
				+ " purchaseOrderDetail.specification AS specification"
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail " + "  INNER JOIN purchaseOrderDetail.item  AS item"
				+ "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				// + " INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted =
				// false AND purchaseTax.isActive = true "
				// + " INNER JOIN purchaseOrderDetail.tax AS tax WITH tax.isDeleted = false AND
				// tax.isActive = true "
				+ "  WHERE purchaseOrderDetail.id = :itemId AND purchaseOrderDetail.purchaseOrder.id = :Id AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "  ORDER BY purchaseOrderDetail.id ";

		/*
		 * String hql =
		 * " SELECT purchaseRequestDetail.id AS id,purchaseRequestDetail.approvedQuantity AS prQuantity,store.storeId AS storeId, item.id AS itemId, item.code AS itemCode, item.name AS itemName,"
		 * +
		 * "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName,purchaseTax.id AS taxId, purchaseTax.name AS taxName, "
		 * + " purchaseTax.taxPercentage AS taxPercent " +
		 * " FROM PurchaseRequestDetail AS purchaseRequestDetail " +
		 * "  INNER JOIN purchaseRequestDetail.item  AS item" +
		 * "  INNER JOIN purchaseRequestDetail.purchaseRequest  AS purchaseRequest" +
		 * "  INNER JOIN purchaseRequest.store AS store " +
		 * "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
		 * +
		 * "  LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
		 * +
		 * "  WHERE purchaseRequestDetail.id = :itemId AND purchaseRequestDetail.purchaseRequest.id = :Id AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
		 * + "  ORDER BY purchaseRequestDetail.id ";
		 */

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("Id", Id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailResponse.class));
		return query.list();

	}

	@Override
	public StoreStock updateStoreStockLeaseQuantity(StoreStock storeStock) {
		getSession().update(storeStock);
		return storeStock;

		/*
		 * String hql =
		 * " UPDATE StoreStock AS storeStock SET storeStock.availableQty = :availableQty WHERE storeStock.store.storeId = :store AND storeStock.batch.id = :batch"
		 * ; Query query = getSession().createQuery(hql);
		 * query.setParameter("availableQty", storeStock.getAvailableQty());
		 * query.setParameter("store", storeStock.getStore().getStoreId());
		 * query.setParameter("batch", storeStock.getBatch().getId()); return
		 * storeStock;
		 */

	}

	@Override
	public StoreStock saveStoreStockLeaseQuantity(StoreStock storeStock) {
		getSession().saveOrUpdate(storeStock);
		return storeStock;
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}

	@Override
	public Batch updateBatchLeaseQuantity(Batch batch) {
		getSession().update(batch);
		return batch;
	}

	@Override
	public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
		getSession().save(stockTransaction);
		return stockTransaction;
	}

	@Override
	public List<IssueDetailBatchMapper> getIssueDetailBatchMapper(Integer id) {
		String hql = " SELECT issueDetailBatchMapper FROM IssueDetailBatchMapper AS issueDetailBatchMapper "
				+ "	INNER JOIN issueDetailBatchMapper.issueDetail AS issueDetail "
				+ " WHERE issueDetail.id = :id AND issueDetail.isActive = true AND issueDetail.isDeleted = false "
				+ " ORDER BY issueDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public IssueDetailBatchMapper getIssueDetailBatchMapperById(Integer id) {

		String hql = " SELECT issueDetailBatchMapper FROM IssueDetailBatchMapper AS issueDetailBatchMapper "
				+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false "
				+ " WHERE issueDetailBatchMapper.id = :id AND issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (IssueDetailBatchMapper) query.uniqueResult();

	}

	@Override
	public IssueDetailBatchMapper updateIssueDetailBatchMapper(IssueDetailBatchMapper issueDetailBatchMapper) {
		getSession().update(issueDetailBatchMapper);
		return issueDetailBatchMapper;
	}

	@Override
	public List<IssueDetail> getIssueDeatilByIssueId(Integer issueDetailId) {
		try {
			String hql = "  SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true  "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false " + " AND issue.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueDetailId);

			return query.list();
		} catch (Exception e) {
		logger.error("getIssueDeatilByIssueId failed", e);
		return null;
		}
	}

	@Override
	public List<FixedAssetType> getFixedAssetTypes() {
		String hql = "SELECT fixedAssetType FROM FixedAssetType AS fixedAssetType "
				+ "WHERE fixedAssetType.isActive = true AND fixedAssetType.isDeleted = false  AND fixedAssetType.unit.unitId = :unitId "
				+ "ORDER BY fixedAssetType.type";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<ReturnReason> getReturnReason(Integer unitId) {
		String hql = "SELECT returnReason FROM ReturnReason AS returnReason WHERE returnReason.isDeleted = false AND returnReason.isActive = true "
				+ " AND returnReason.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<Rack> getRacks(Integer storeId, Integer orgId) {
		String hql = "SELECT rack.id AS id, rack.name AS rack FROM Rack AS rack "
				+ "		INNER JOIN rack.store AS store WITH store.storeId = :storeId AND store.isActive = true AND store.isDeleted = false "
				+ " WHERE rack.isActive = true AND rack.isDeleted = false "
				+ " AND rack.organization.organizationId =:organizationId " + "	ORDER BY rack.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.Rack.class));

		return query.list();
	}

	@Override
	public List<com.param.entity.model.master.Rack> getRacksForOpeningBalance(Integer storeId, Integer orgId) {
		String hql = "SELECT rack FROM Rack AS rack "
				+ "		INNER JOIN rack.store AS store WITH store.storeId = :storeId AND store.isActive = true AND store.isDeleted = false "
				+ " WHERE rack.isActive = true AND rack.isDeleted = false "
				+ " AND rack.organization.organizationId =:organizationId " + "	ORDER BY rack.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<Shelf> getShelves(Integer rackId) {
		String hql = "SELECT shelf.id AS id, shelf.name AS shelf FROM Shelf AS shelf "
				+ "		INNER JOIN shelf.rack AS rack WITH rack.id = :rackId AND rack.isActive = true AND rack.isDeleted = false "
				+ " WHERE shelf.isActive = true AND shelf.isDeleted = false " + "	ORDER BY shelf.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("rackId", rackId);
		query.setResultTransformer(Transformers.aliasToBean(Shelf.class));

		return query.list();
	}

	@Override
	public List<AgainstGrnResponse> getGrn(AgainstGrnRequest request) {
		String hql = "SELECT grn.id AS id, grn.grnNumber AS grnNo, grn.addedDate AS grnDate, vendor.id AS vendorId, vendor.name AS vendor, "
				+ "		store.storeId AS storeId, store.storeName AS store " + "	FROM GoodReceiptNote AS grn "
				+ "		INNER JOIN grn.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN grn.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN grn.status AS status WITH status.id = :statusId AND status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN grn.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.unit.unitId = :unitId "
				+ "     AND grn.isConsignment=false "
				+ "		AND grn.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "	    AND (:grnNumber = '' OR lower(grn.grnNumber) LIKE :grnNumber) "
				+ "     AND (:assetId = 0 OR assetType.id=:assetId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) " + "	ORDER BY grn.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
		query.setParameter("statusId", com.param.common.constants.Status.Closed.getStatus());
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("grnNumber", request.getGrnNo() != null ? '%' + request.getGrnNo().toLowerCase() + '%' : "");
		query.setParameter("assetId", request.getAssetTypeID() != null ? request.getAssetTypeID() : 0);
		query.setResultTransformer(Transformers.aliasToBean(AgainstGrnResponse.class));

		return query.list();
	}

	// For Barcode get Grn Approval Qty
	@Override
	public List<AgainstGrnDetailResponse> getBarcodeGrnDetails(List<Integer> grnId, Integer assetTypeId) {
		String hql = "SELECT grn.id AS grnId, grnDetail.id AS grnDetailId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ "		grn.organization.organizationName as hospitalName, batch.lotNo AS lotNo, batch.cop AS cop, vendor.id AS vendorId, vendor.name AS vendor, cast(physicalInspectionDetail.approvedQuantity as long) AS availableQuantity, "
				+ "		grnStore.storeId AS storeId, grnStore.storeName AS store, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, "
				+ "		uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, grnDetail.mrp AS mrp, batch.prePrintedBarcode AS prePrintedBarcode, "
				+ "		(COALESCE(SUM(staged.receivedQuantity), 0) - COALESCE(grnDetail.grnApprovedQuantity, 0)) AS pendingQuantity, batch.printedCount as printedCount "
				+ "	FROM GoodReceiptNote AS grn "
				+ "		LEFT JOIN grn.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		LEFT JOIN grn.store AS grnStore WITH grnStore.isActive = true AND grnStore.isDeleted = false "
				+ "		LEFT JOIN grn.goodReceiptNoteDetailsList AS grnDetail WITH grnDetail.isActive = true AND grnDetail.isDeleted = false "
				+ "		INNER JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail WITH physicalInspectionDetail.isActive = true AND physicalInspectionDetail.isDeleted = false "

				+ "		LEFT JOIN grnDetail.grnStagedQuantityList AS staged WITH staged.isActive = true AND staged.isDeleted = false "
				+ "		LEFT JOIN grnDetail.status AS status WITH status.id = :statusId AND status.isActive = true AND status.isDeleted = false "
				+ "		LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false "
				+ "		LEFT JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "	    LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "	    LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "	    LEFT JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "

				+ "		LEFT JOIN grnDetail.grnUomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		LEFT JOIN grnDetail.grnUomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
				+ " WHERE grn.isActive = true AND grn.isDeleted = false " + "AND grn.id IN (:id)   "
				// + "AND (:assetId = 0 OR assetType.id=:assetId) " + " AND store.storeId =
				// grnStore.storeId "
				+ "AND  store.storeId = grnStore.storeId "

				+ "	GROUP BY grn.id, grnDetail.id, item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, batch.cop, vendor.id, vendor.name, grn.organization.organizationName, batch.printedCount, "
				+ "		grnStore.storeId, grnStore.storeName, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, uomType.id, uomType.type, uomUnit.id, uomUnit.units, grnDetail.mrp , batch.prePrintedBarcode, grnDetail.grnApprovedQuantity,physicalInspectionDetail.approvedQuantity  "
				+ "	ORDER BY grn.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", grnId);
		// query.setParameter("assetId", assetTypeId != null ? assetTypeId : 0);
		query.setParameter("statusId", com.param.common.constants.Status.Closed.getStatus());
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		query.setResultTransformer(Transformers.aliasToBean(AgainstGrnDetailResponse.class));

		return query.list();
	}

	@Override
	public List<AgainstGrnDetailResponse> getGrnDetails(List<Integer> grnId, Integer assetTypeId) {
		String hql = null;

		if (assetTypeId != null && assetTypeId.equals(1)) {

			hql = "SELECT grn.id AS grnId, grnDetail.id AS grnDetailId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
					+ "		batch.lotNo AS lotNo, grnDetail.cop AS cop, vendor.id AS vendorId, vendor.name AS vendor, cast(grnDetail.rejectedQuantity as long) AS availableQuantity, "
					+ "		grnStore.storeId AS storeId, grnStore.storeName AS store, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, "
					+ "		uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, grnDetail.mrp AS mrp, "
					+ "		(COALESCE(grnDetail.grnApprovedQuantity, 0) - COALESCE(SUM(staged.approvedQuantity), 0)) AS pendingQuantity ,cast(COALESCE(grnDetail.grnApprovedQuantity, 0)+COALESCE(grnDetail.rejectedQuantity,0) as long) AS totalGRNQuantity, cast(physicalInspectionDetail.rejectQuantity as long) AS phyRejectQuantity,"
					+ "grnDetail.discount AS discount,grnDetail.totalAmount AS totalAmount,grnDetail.totalDiscount AS totalDiscount, item.isThreeUOMApplicable as isThreeUOMApplicable  "
					+ "	FROM GoodReceiptNote AS grn "
					+ "		LEFT JOIN grn.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ "		LEFT JOIN grn.store AS grnStore WITH grnStore.isActive = true AND grnStore.isDeleted = false "
					+ "		LEFT JOIN grn.goodReceiptNoteDetailsList AS grnDetail WITH grnDetail.isActive = true AND grnDetail.isDeleted = false "
					// + " LEFT JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail
					// WITH physicalInspectionDetail.isActive = true AND
					// physicalInspectionDetail.isDeleted = false "

					// start code for resolved HIS-10445 Returnable Gatepass - Rejected against GRN
					// - Not able to fetch Get GRN details after selection
					+ "		INNER JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail WITH physicalInspectionDetail.isActive = true AND physicalInspectionDetail.isDeleted = false "

					+ "		LEFT JOIN grnDetail.grnStagedQuantityList AS staged WITH staged.isActive = true AND staged.isDeleted = false "
					+ "		LEFT JOIN grnDetail.status AS status WITH status.id = :statusId AND status.isActive = true AND status.isDeleted = false "
					// + " LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND
					// batch.isDeleted = false AND batch.isQuarantine = false "
					+ "		LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false  "
					+ "		LEFT JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
					+ "		LEFT JOIN grnDetail.tax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
					+ "		LEFT JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ "	    LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
					+ "	    LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
					+ "	    LEFT JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
					+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
					+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
					+ "		INNER JOIN grnDetail.grnUomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ "		INNER JOIN grnDetail.grnUomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
					+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.id IN (:id)  "
					+ "		AND (:assetId = 0 OR grn.assetType.id=:assetId) "
					+ "	 GROUP BY grn.id, grnDetail.id, item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, grnDetail.cop, vendor.id, vendor.name, "
					+ "		grnStore.storeId, grnStore.storeName, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, uomType.id, "
					+ "		uomType.type, uomUnit.id, uomUnit.units, grnDetail.mrp ,grnDetail.grnApprovedQuantity,grnDetail.rejectedQuantity , physicalInspectionDetail.rejectQuantity,grnDetail.grnApprovedQuantity  "
					+ "	ORDER BY grn.id ";

		} else {

			hql = "SELECT grn.id AS grnId, grnDetail.id AS grnDetailId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
					+ "		batch.lotNo AS lotNo, batch.cop AS cop, vendor.id AS vendorId, vendor.name AS vendor, cast(grnDetail.rejectedQuantity as long) AS availableQuantity, "
					+ "		grnStore.storeId AS storeId, grnStore.storeName AS store, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, "
					+ "		uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, grnDetail.mrp AS mrp, "
					+ "		(COALESCE(grnDetail.grnApprovedQuantity, 0) - COALESCE(SUM(staged.approvedQuantity), 0)) AS pendingQuantity,cast(COALESCE(grnDetail.grnApprovedQuantity, 0) as long) AS totalGRNQuantity  , cast(physicalInspectionDetail.rejectQuantity as long) AS phyRejectQuantity,"
					+ " grnDetail.discount AS discount,grnDetail.totalAmount AS totalAmount,grnDetail.totalDiscount AS totalDiscount, item.isThreeUOMApplicable as isThreeUOMApplicable  "
					+ "	FROM GoodReceiptNote AS grn "
					+ "		LEFT JOIN grn.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ "		LEFT JOIN grn.store AS grnStore WITH grnStore.isActive = true AND grnStore.isDeleted = false "
					+ "		LEFT JOIN grn.goodReceiptNoteDetailsList AS grnDetail WITH grnDetail.isActive = true AND grnDetail.isDeleted = false "
					// + " LEFT JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail
					// WITH physicalInspectionDetail.isActive = true AND
					// physicalInspectionDetail.isDeleted = false "

					// start code for resolved HIS-10445 Returnable Gatepass - Rejected against GRN
					// - Not able to fetch Get GRN details after selection
					+ "		INNER JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail WITH physicalInspectionDetail.isActive = true AND physicalInspectionDetail.isDeleted = false "

					+ "		LEFT JOIN grnDetail.grnStagedQuantityList AS staged WITH staged.isActive = true AND staged.isDeleted = false "
					+ "		LEFT JOIN grnDetail.status AS status WITH status.id = :statusId AND status.isActive = true AND status.isDeleted = false "
					// + " LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND
					// batch.isDeleted = false AND batch.isQuarantine = false "
					+ "		LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false  "
					+ "		LEFT JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
					+ "		LEFT JOIN grnDetail.tax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
					+ "	    LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
					+ "	    LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
					+ "	    LEFT JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
					+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
					+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
					+ "		LEFT JOIN grnDetail.grnUomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ "		LEFT JOIN grnDetail.grnUomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
					+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.id IN (:id) "
					+ "		AND (:assetId = 0 OR grn.assetType.id=:assetId) "
					+ "	GROUP BY grn.id, grnDetail.id, item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, batch.cop, vendor.id, vendor.name, "
					+ "		grnStore.storeId, grnStore.storeName, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, uomType.id, "
					+ "		uomType.type, uomUnit.id, uomUnit.units, grnDetail.mrp ,grnDetail.grnApprovedQuantity,grnDetail.rejectedQuantity  , physicalInspectionDetail.rejectQuantity,grnDetail.grnApprovedQuantity  "
					+ "	ORDER BY grn.id ";

		}
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", grnId);
		query.setParameter("assetId", assetTypeId != null ? assetTypeId : 0);
		query.setParameter("statusId", com.param.common.constants.Status.Closed.getStatus());
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		query.setResultTransformer(Transformers.aliasToBean(AgainstGrnDetailResponse.class));

		return query.list();
	}

	@Override
	public List<ReturnableGatepassDetail> getReturnableGatepassDetails(Integer grnDetailId) {
		String hql = "SELECT returnableGatepassDetail " + "	FROM ReturnableGatepassDetail AS returnableGatepassDetail "
				+ "		INNER JOIN returnableGatepassDetail.goodReceiptNoteDetail AS grnDetails WITH grnDetails.isActive = true AND grnDetails.isDeleted = false "
				+ " WHERE returnableGatepassDetail.isActive = true AND returnableGatepassDetail.isDeleted = false "
				+ "		AND grnDetails.id = :grnDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("grnDetailId", grnDetailId);

		return query.list();
	}

	@Override
	public List<ScrapReason> getScrapReasonList() {
		String hql = "SELECT scrapReason FROM ScrapReason AS scrapReason "
				+ "WHERE scrapReason.isActive = true AND scrapReason.isDeleted = false AND scrapReason.organization.organizationId =:organizationId";

		Query query = getSession().createQuery(hql);
		// query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("organizationId", sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<ReasonMaster> getReasonList() {
		String hql = "SELECT reasonMaster FROM ReasonMaster AS reasonMaster " + "WHERE reasonMaster.status = 'A' ";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<ContractType> getContractType() {
		String hql = "SELECT contractType FROM ContractType AS contractType "
				+ "WHERE contractType.isActive = true AND contractType.isDeleted = false "
				+ "ORDER BY contractType.id ";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<RequestType> getRequestType() {
		String hql = "SELECT requestType FROM RequestType AS requestType "
				+ "WHERE requestType.isActive = true AND requestType.isDeleted = false " + "ORDER BY requestType.id ";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<TaxComponent> getTaxComponentList() {
		String hql = "SELECT taxComponent FROM TaxComponent AS taxComponent "
				+ " WHERE taxComponent.organization.organizationId =:organizationId AND "
				+ " taxComponent.isActive = true AND taxComponent.isDeleted = false "
				+ " ORDER BY taxComponent.component";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<RouteOfAdministration> getFormulationTypeAndROADropdown() {
		String hql = "SELECT ROA FROM RouteOfAdministration AS ROA "
				+ "WHERE ROA.isActive = true AND ROA.isDeleted = false "
				+ " AND ROA.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<AssetCategory> getAssetCategory(Integer orgId) {
		String hql = "SELECT assetCategory FROM AssetCategory AS assetCategory "
				+ "WHERE assetCategory.isActive = true AND assetCategory.isDeleted = false AND assetCategory.organization.organizationId =:organizationId "
				+ "ORDER BY assetCategory.category ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<Ved> getVedList() {
		String hql = "SELECT ved FROM Ved AS ved " + "WHERE ved.isActive = true AND ved.isDeleted = false"
				+ " ORDER BY ved.name ASC ";

		Query query = getSession().createQuery(hql);
		return query.list();

	}

	@Override
	public Long getPurchaseRequestLeaseQunatity(Integer storeId, Integer itemId, Integer unitId) {

		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId "
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id = :itemId AND purchaseRequestDetail.goodReceiptNoteDetail is null  "
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public Long getQuotationRequestLeaseQunatity(Integer storeId, Integer itemId, Integer unitId) {

		String hql = "SELECT SUM (COALESCE ((quotationRequestDetail.leaseQuantity),0) )"
				+ " FROM QuotationRequestDetail AS quotationRequestDetail "
				+ "INNER JOIN quotationRequestDetail.quotationRequest AS quotationRequest WITH quotationRequest.isDeleted = false AND quotationRequest.isActive = true AND quotationRequest.unit.unitId = :unitId "
				+ "WHERE quotationRequest.store.storeId = :storeId AND quotationRequestDetail.item.id = :itemId "
				+ " AND quotationRequestDetail.isDeleted = false AND quotationRequestDetail.isActive = true AND quotationRequestDetail.unit.unitId = :unitId "
				+ " AND quotationRequestDetail.status.id != :cancelStatusId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public List<Screen> getScreens() {
		String hql = "SELECT screen FROM Screen AS screen WHERE screen.isDeleted = false AND screen.isActive = true AND screen.isPrefixRequired = true "
				+ "ORDER BY screen.name ASC ";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<PurchaseOrderDetail> getPurchaseOrderHistory(Integer itemId, Integer unitId) {
		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ "INNER JOIN purchaseOrderDetail.item AS item "
				+ " WHERE purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false AND "
				+ "purchaseOrderDetail.unit.unitId = :unitId  AND purchaseOrderDetail.item.id = :itemId "
				+ " ORDER BY purchaseOrderDetail.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public PrefixConfiguration getPrefixConfiguration(Integer screenId) {

		String hql = "SELECT prefixConfiguration FROM PrefixConfiguration AS prefixConfiguration WHERE prefixConfiguration.screen.id = :screenId AND prefixConfiguration.isActive = true AND prefixConfiguration.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("screenId", screenId);

		return (PrefixConfiguration) query.uniqueResult();
	}

	@Override
	public PrefixConfigDto getPrefixConfiguration(Integer storeId, Integer unitId, Integer screenId) {
		String hql = "SELECT prefixConfiguration.id as id, prefixConfiguration.runningNumber as runningNumber, "
				+ " prefixConfiguration.resetId as resetId, prefixConfiguration.length as length, "
				+ " prefixConfiguration.formatId as formatId, prefixConfiguration.generatedCode as generatedCode, "
				+ " unit.unitCode as unitCode, prefixConfiguration.organization.organizationId as organizationId "
				+ " FROM PrefixConfiguration AS prefixConfiguration "
				+ " INNER JOIN prefixConfiguration.screen AS screen WITH screen.isActive = true and screen.isGlobal = false and screen.isDeleted = false and screen.id = :screenId"
				+ " INNER JOIN prefixConfiguration.unit AS unit WITH unit.id = :unitId"
				+ " WHERE prefixConfiguration.isActive = true AND prefixConfiguration.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", unitId);

		query.setResultTransformer(Transformers.aliasToBean(PrefixConfigDto.class));

		return (PrefixConfigDto) query.uniqueResult();
	}

	@Override
	public Unit getUnit(Integer unitId) {
		if (unitId == null)
			return null;

		String hql = "SELECT unit FROM Unit AS unit WHERE unit.unitId = :unitId AND unit.status = 'A' ";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Unit) query.uniqueResult();
	}

	@Override
	public String getPrefixRunningNumber(Integer unitId, int screenId, Integer storeId) {

		SQLQuery query = (SQLQuery) getSession()
				.createSQLQuery("SELECT*FROM t_generate_prefix_number(:storeId , :unitId, :screenId)");

		query.setParameter("unitId", unitId);
		query.setParameter("screenId", screenId);
		query.setParameter("storeId", storeId);
		// System.out.println(query);
		String prefixNo = (String) query.uniqueResult();
		// System.out.println("Prefix No : " + prefixNo);
		return prefixNo;
	}

	@Override
	public PrefixRunningNumber updatePrefixRunningNumbar(PrefixRunningNumber prefixRunningNumber) {
		getSession().update(prefixRunningNumber);
		return prefixRunningNumber;
	}

	@Override
	public List<TaxDetail> getTaxDetail(Integer taxId) {

		String hql = "SELECT taxDetail FROM TaxDetail AS taxDetail "
				+ "WHERE taxDetail.tax.id = :taxId  AND taxDetail.isActive = true AND taxDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("taxId", taxId);
		return query.list();

	}

	@Override
	public int getAccessTypeId(int id) {

		String hql = "SELECT assetType.id from item as item INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "	    INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "	    INNER JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "

				+ " WHERE item.id=(:id)  ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (Integer) query.uniqueResult();

	}

	@Override
	public ApplicationConfiguration getApplicationConfigurationByUnitId(Integer unitId) {
		String hql = " SELECT applicationConfiguration from ApplicationConfiguration as applicationConfiguration "
				+ "WHERE applicationConfiguration.unit.unitId = :unitId AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId);
		return (ApplicationConfiguration) query.uniqueResult();
	}

	@Override
	public Batch getBatchFromSaleDetail(Long id) {
		String hql = "SELECT batch FROM Batch as batch  WHERE batch.id = :id  AND batch.isActive = true AND batch.isDeleted = false AND batch.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());
		return (Batch) query.uniqueResult();
	}

	@Override
	public List<BatchListResponse> smartIssue(List<Integer> itemId, Integer storeId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT item.id AS itemId, batch.id AS batchId, batch.expiryDate AS expiry, cast(stock.availableQty as long) AS availableQuantity "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.id in (:itemId) AND item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS stock WITH stock.store.storeId = :storeId AND stock.availableQty > 0 "
				+ "				AND stock.isActive = true AND stock.isDeleted = false "
				+ "	WHERE batch.expiryDate > :expiryDate " + "	ORDER BY item.id, batch.expiryDate, stock.availableQty";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemId);
		query.setParameter("expiryDate", current);

		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	// For Kit Master Implementation
	@Override
	public Long getStoreStockInTransitByItem(Integer itemId) {
		String hql = "SELECT COALESCE(SUM(storeIndentDetail.inTransitQuantity), 0) "
				+ "FROM StoreIndentDetail AS storeIndentDetail " + "		INNER JOIN storeIndentDetail.item AS item "
				+ "WHERE item.id = :itemId "
				+ "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
		// return (Long) query.uniqueResult();
	}

	@Override
	public Long getLastMonthSale(Integer itemId) {

		String hql = "SELECT COALESCE(SUM(lastMonthSale.saleQuantity), 0) FROM LastMonthSale AS lastMonthSale "
				+ "		INNER JOIN lastMonthSale.item AS item WITH item.id = :itemId AND item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "WHERE lastMonthSale.isDeleted = false AND lastMonthSale.isActive = true AND lastMonthSale.unit.unitId = :unitId  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Long) query.uniqueResult();
	}

	@Override
	public Long getCurrentMonthSale(Integer itemId) {
		String hql = "SELECT COALESCE(SUM(saleDetail.leaseQuantity), 0) FROM SaleDetail AS saleDetail "
				+ "		INNER JOIN saleDetail.sale AS sale WITH sale.isDeleted = false AND sale.isActive = true AND sale.unit.unitId = :unitId "
				+ "		INNER JOIN saleDetail.batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.id = :itemId "
				+ "WHERE saleDetail.isDeleted = false AND saleDetail.isActive = true AND saleDetail.unit.unitId = :unitId "
				+ "		AND date_trunc('month', saleDetail.addedDate) = date_trunc('month', current_date) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Long) query.uniqueResult();
	}

	@Override
	public List<com.param.global.dto.global.AssetType> getAssetTypeListPerformance() {

		String hql = "SELECT assetType.id AS id, assetType.type AS type FROM AssetType AS assetType "
				+ "WHERE assetType.isActive = true AND assetType.isDeleted = false   " + "ORDER BY assetType.type";

		Query query = getSession().createQuery(hql);
		// query.setParameter("organizationId", sessionService.getOrganizationId());

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.AssetType.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.AssetType> getAssetTypeListPT() {

		 String hql = "select assetType.id as id, assetType.type as type" + " FROM AssetType AS assetType "
				+ "	WHERE assetType.isActive = true AND assetType.isDeleted = false ORDER BY assetType.type";

		Query query = getSession().createQuery(hql);
		List<com.param.global.dto.global.AssetType> list = query
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.AssetType.class)).list();

		return list;
	}

	@Override
	public List<com.param.global.dto.generic.PharmacologicalClassification> getPharmacologicalClassificationListPT(
			Integer orgId ,String searchClassificationName) {
		List<com.param.global.dto.generic.PharmacologicalClassification> list=null;
		String hql = "select pharmacologicalClassification.id as id, pharmacologicalClassification.classification as classification "
				+ " from PharmacologicalClassification pharmacologicalClassification "
				+ " WHERE pharmacologicalClassification.organization.organizationId=:organizationId AND pharmacologicalClassification.isActive = true"
				+ " AND pharmacologicalClassification.isDeleted =false";

		StringBuilder stringBuilderQuery = new StringBuilder(hql);
		if(searchClassificationName!=null)
		{
			stringBuilderQuery.append(" AND LOWER(pharmacologicalClassification.classification) LIKE :searchClassificationName");
			list = getSession().createQuery(stringBuilderQuery.toString())
					.setParameter("organizationId", orgId)
					.setParameter("searchClassificationName", "%"+searchClassificationName.toLowerCase()+"%")
					.setResultTransformer(
							Transformers.aliasToBean(com.param.global.dto.generic.PharmacologicalClassification.class))
					.list();
		}
		else
		{
		 list = getSession().createQuery(stringBuilderQuery.toString())
				.setParameter("organizationId", orgId)
				.setResultTransformer(
						Transformers.aliasToBean(com.param.global.dto.generic.PharmacologicalClassification.class))
				.list();
		}
		return list;
	}

	@Override
	public List<com.param.global.dto.generic.Manufacturer> getManufacturerListPT(Integer orgId,String searchManufactureName) {
		List<com.param.global.dto.generic.Manufacturer> list =null;
		String hql = "select manufacturer.id as id, manufacturer.name as name from Manufacturer manufacturer"
				+ " WHERE manufacturer.organization.organizationId=:organizationId AND manufacturer.isActive = true"
				+ " AND manufacturer.isDeleted =false";
		StringBuilder stringBuilderQuery = new StringBuilder(hql);
		if(searchManufactureName!=null)
		{
			stringBuilderQuery.append(" AND LOWER(manufacturer.name) LIKE :searchManufactureName order by manufacturer.name ");
			 list = getSession().createQuery(stringBuilderQuery.toString())
						.setParameter("organizationId", orgId)
						.setParameter("searchManufactureName", "%"+searchManufactureName.toLowerCase()+"%")
						.setResultTransformer(
								Transformers.aliasToBean(com.param.global.dto.generic.Manufacturer.class))
						.list();
		}
		else
		{
			stringBuilderQuery.append(" order by manufacturer.name ");
			Query query = getSession().createQuery(stringBuilderQuery.toString());
			query.setParameter("organizationId", sessionService.getOrganizationId());
			list= query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.generic.Manufacturer.class)).list();
		}
		return list;
	}

	@Override
	public List<com.param.global.dto.generic.StrengthUnit> getStrengthUnitListPT(Integer orgId) {
		String hql = "select strengthUnit.id as id, strengthUnit.units as unit from StrengthUnit strengthUnit"
				+ " WHERE strengthUnit.organization.organizationId =:organizationId AND strengthUnit.isActive=true "
				+ " AND strengthUnit.isDeleted =false";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		// List<com.param.global.dto.generic.StrengthUnit> list =
		// getSession().createQuery(hql)
		List<com.param.global.dto.generic.StrengthUnit> list = query
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.generic.StrengthUnit.class)).list();

		return list;
	}

	@Override
	public List<com.param.global.dto.generic.StorageUnit> getStorageUnitListPT(Integer orgId) {
		String hql = "select storageUnit.id as id, storageUnit.unitName as unit from StorageUnit storageUnit"
				+ " WHERE storageUnit.organization.organizationId =:organizationId AND storageUnit.isActive = true "
				+ " AND storageUnit.isDeleted =false"
				+ " ORDER BY storageUnit.unitName ASC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		List<com.param.global.dto.generic.StorageUnit> list = query
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.generic.StorageUnit.class)).list();

		return list;
	}

	@Override
	public List<com.param.global.dto.generic.Tax> getTaxListPT() {
		String hql = "select tax.id as id, tax.name as type from Tax tax "
				+ " WHERE tax.organization.organizationId = :organizationId AND tax.isActive = true "
				+ " AND tax.isDeleted =false"
				+ " ORDER BY tax.name ASC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		List<com.param.global.dto.generic.Tax> list = query
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.generic.Tax.class)).list();

		return list;
	}

	@Override
	public List<com.param.global.dto.global.FormulationType> getFormulationTypeListPT(Integer orgId) {
		String hql = "select formulationType.id as id, formulationType.type as type from FormulationType formulationType "
				+ " WHERE formulationType.organization.organizationId = :organizationId AND formulationType.isDeleted = false AND formulationType.isActive = true "
				+ " ORDER BY formulationType.type ASC ";

		// + " WHERE formulationType.organization.organizationId = :organizationId AND
		// formulationType.isDeleted = false AND formulationType.isActive = true ";

		List<com.param.global.dto.global.FormulationType> list = getSession().createQuery(hql)
				.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.FormulationType.class))
				.list();

		return list;
	}

	@Override
	public List<com.param.global.dto.global.Status> getScreenStatusBetterPerformance(Integer screenId) {
		String hql = "SELECT status.id as id, status.status as status FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false AND screen.isActive = true "
				+ "		INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "  WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false "
				+ " ORDER BY status.status ASC ";

		List<com.param.global.dto.global.Status> list = getSession().createQuery(hql).setParameter("screenId", screenId)
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.Status.class)).list();
		return list;
	}

	@Override
	public List<com.param.global.dto.global.VisitType> getVisitTypeListBetterPerformance() {
		String hql = "SELECT visitType.visitTypeId as id, visitType.visitTypeName as type FROM VisitType AS visitType "
				+ "WHERE visitType.status = 'A' "
				+ " ORDER BY visitType.visitTypeName ASC ";

		List<com.param.global.dto.global.VisitType> list = getSession().createQuery(hql)
				.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.VisitType.class)).list();
		return list;
	}

	@Override
	public PurchaseOrderDetail getPurchaseOrderDetailsById(Integer id) {
		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " WHERE purchaseOrderDetail.id = :id AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (PurchaseOrderDetail) query.uniqueResult();
	}

	public List<UomResponse> getConsignedInwordBatchUomList(Long batchId) {
		String hql = "SELECT  uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, batchUomMapper.conversions AS conversion, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom  "
				+ "FROM BatchUomMapper AS batchUomMapper " + "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		LEFT JOIN batchUomMapper.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN batchUomMapper.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.id = :batchId ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("batchId", batchId);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	public Long getConsignemntOutwordBatchWiseQty(Long batchId) {
		String hql = " SELECT SUM(consignmentOutwardDetail.returnQunatity) FROM ConsignmentOutwardDetail AS consignmentOutwardDetail "
				+ " WHERE consignmentOutwardDetail.batch.id = :batchId  "
				+ "AND consignmentOutwardDetail.isActive = true AND consignmentOutwardDetail.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("batchId", batchId);

		return (Long) (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}

	@Override
	public List<PurchaseOrderStagedDcDate> getPurchaseOrderStagedListDcDate(Integer id) {
		/*
		 * String hql =
		 * " SELECT poStaged.id AS id, poStaged.approvedQunatity AS poApprovedQuantity, "
		 * +
		 * " 		poStaged.stagedDate AS stagedDate, grnStaged.receivedQuantity AS receivedQuantity, "
		 * + "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity " +
		 * " FROM PurchaseOrderDetail AS purchaseOrderDetail " +
		 * "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
		 * +
		 * "		LEFT OUTER JOIN poStaged.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
		 * +
		 * " WHERE purchaseOrderDetail.id = :id AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
		 * + " ORDER BY poStaged.id ";
		 */

		String hql = "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS stagedReceivedQuantity, grnStaged.stagedDcDate AS stagedDcDate, "
				+ " COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity  "
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ "		INNER JOIN purchaseOrderDetail.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
				+ " WHERE purchaseOrderDetail.id = :id AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(
				Transformers.aliasToBean(com.param.global.dto.global.PurchaseOrderStagedDcDate.class));

		return query.list();
	}

	@Override
	public List<PurchaseOrderDetail> getPurchaseOrdersByRequestDetailId(Integer id) {
		String hql = "SELECT purchaseOrderDetail.poQuantity as poQuantity, purchaseOrderDetail.poApprovedQuantity as poApprovedQuantity FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ "INNER JOIN purchaseOrderDetail.putchRequestDetail as purchaseRequestDetails WITH purchaseRequestDetails.isActive = true AND purchaseRequestDetails.isDeleted = false AND purchaseRequestDetails.id = :id "
				+ " WHERE purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetail.class));

		return query.list();
	}

	/*
	 * @Override public List<PurchaseOrderDetail>
	 * getPurchaseOrdersByQuotationRequestDetailId(Integer id) { String hql =
	 * "SELECT purchaseOrderDetail.poQuantity as poQuantity, purchaseOrderDetail.poApprovedQuantity as poApprovedQuantity FROM PurchaseOrderDetail AS purchaseOrderDetail "
	 * +
	 * "INNER JOIN purchaseOrderDetail.quotationRequestDetail as quotationRequestDetails WITH quotationRequestDetails.isActive = true AND quotationRequestDetails.isDeleted = false AND quotationRequestDetails.id = :id "
	 * +
	 * " WHERE purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false"
	 * ; Query query = getSession().createQuery(hql); query.setParameter("id", id);
	 * 
	 * query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetail.class
	 * ));
	 * 
	 * return query.list(); }
	 */

	@Override
	public Batch getBatchForStockConsumption(Integer storeId, Long batchId, Integer unitId) {
		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "		INNER JOIN FETCH storeStock.store AS store "
				+ "WHERE batch.id = :batchId AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId);
		query.setParameter("batchId", batchId);

		return (Batch) query.uniqueResult();
	}

	@Override
	public List<Batch> getBatchDetailsCSSD(Integer id, Integer unitId, Integer storeId, BatchProperty batchProperty) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		// int unId = unitId==0?sessionService.getUnitId():unitId;
		query.setParameter("id", id);
		query.setParameter("unitId", unitId);
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("isPharmacy",
				batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
		query.setParameter("isIndent", batchProperty.getIsIndent() == null || batchProperty.getIsIndent() ? 0 : 1);
		query.setParameter("isReturn", batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
		return query.list();
	}

	@Override
	public List<UomResponse> getUnitOfMeasurementListByItemBP(List<Integer> consumabelItemIds, Integer orgId) {
		String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
				+ "		uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom, uom.item.id AS itemId "
				+ "	FROM UnitOfMeasurement AS uom "
				+ "		INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
				+ "	WHERE uom.item.id in (:itemId) AND uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId=:organizationId "
				+ "	ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemId", consumabelItemIds);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<OtherChargesResponse> getOtherChargesBP(Integer unitId) {
		String hql = "SELECT otherCharge.id as id, otherCharge.description as otherCharges FROM OtherCharge AS otherCharge WHERE otherCharge.isActive = true "
				+ "AND otherCharge.isDeleted = false AND otherCharge.unit.unitId=:unitId";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId()); // HIS-10871

		query.setResultTransformer(Transformers.aliasToBean(OtherChargesResponse.class));

		return query.list();
	}

	@Override
	public List<TaxDropDown> getTaxListBP(Integer orgId) {
		String hql = "SELECT tax.id as taxId, tax.name as taxName, tax.taxPercentage as taxPercent FROM Tax AS tax "
				+ "WHERE tax.isActive = true AND tax.isDeleted = false AND tax.organization.organizationId = :organizationId"
				+ " ORDER BY tax.name ASC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		query.setResultTransformer(Transformers.aliasToBean(TaxDropDown.class));

		return query.list();
	}

	@Override
	public List<UomResponse> getUnitOfMeasurementListByItemBettoerPerformance(List<Integer> itemIds, Integer orgId) {
		String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
				+ "		uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom, uom.item.id as itemId "
				+ "	FROM UnitOfMeasurement AS uom "
				+ "		INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
				+ "	WHERE uom.item.id in (:itemIds) AND uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId =:organizationId "
				+ "	ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemIds", itemIds);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<Quantities> getStoreStockByItemBP(Integer storeId, List<Integer> itemIds, Integer unitId) {
		/*
		 * String hql =
		 * "SELECT item.id as itemId, COALESCE(SUM(storeStock.availableQty), 0) as quantity FROM Batch AS batch "
		 * +
		 * "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
		 * +
		 * "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
		 * +
		 * "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
		 * +
		 * "WHERE store.storeId = :storeId AND item.id in (:itemId)  AND (batch.expiryDate >=:expiryDate OR batch.expiryDate IS NULL ) "
		 * +
		 * "		AND batch.isQuarantine = false and storeStock.totalQty is not null and storeStock.availableQty is not null AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId group by item.id "
		 * ;
		 */
		// as discussed with divyesh
        Date date = TimeZoneComponent.getDateByzone(unitId);
		String hql = "SELECT item.id as itemId, COALESCE(SUM(storeStock.availableQty), 0) as quantity FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE store.storeId = :storeId AND item.id in (:itemId)   "
				+ " and (to_char(batch.expiryDate,'YYYY-MM-DD') >=:expiryDate or batch.expiryDate is null ) "
				+ "	and storeStock.totalQty is not null and storeStock.availableQty is not null AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId group by item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemIds);
		query.setParameter("expiryDate", GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@Override
	public List<Quantities> getStoreStockInTransitByItemBP(Integer storeId, List<Integer> itemIds, Integer unitId) {
        Date date = TimeZoneComponent.getDateByzone(unitId);
		String hql = "SELECT batch.item.id as itemId, COALESCE(SUM(stock.tQty), 0) as quantity "
				+ "FROM StoreStock AS stock " + "		INNER JOIN stock.batch AS batch "
				+ " WITH (to_char(batch.expiryDate,'YYYY-MM-DD') >=:expiryDate or batch.expiryDate is null ) "
				+ "WHERE batch.item.id in (:itemId) AND stock.store.storeId  = :storeId and stock.unit.unitId =:unitId"
				+ "		AND stock.isActive = true AND stock.isDeleted = false AND stock.tQty > 0 group by batch.item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemIds);
		query.setParameter("expiryDate", GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));

		return query.list();
	}

	@Override
	public List<Quantities> getLastMonthSaleBP(Integer storeId, List<Integer> itemIds, Integer unitId) {
		String hql = "SELECT item.id as itemId, COALESCE(SUM(lastMonthSale.saleQuantity), 0) as quantity FROM LastMonthSale AS lastMonthSale "
				+ "		INNER JOIN lastMonthSale.item AS item WITH item.id in (:itemId) AND item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN lastMonthSale.store AS store WITH store.storeId = :storeId AND store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE lastMonthSale.isDeleted = false AND lastMonthSale.isActive = true AND lastMonthSale.unit.unitId = :unitId group by item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@Override
	public List<Quantities> getStoreMaxQuantityByItemBP(Integer storeId, List<Integer> itemIds, Integer orgId) {
		String hql = "SELECT item.id as itemId, COALESCE(storeItem.maxQty, 0) as intQuantity FROM StoreItemMapper AS storeItem "
				+ "		INNER JOIN storeItem.store AS store WITH store.storeId = :storeId "
				+ "		INNER JOIN storeItem.item AS item WITH item.id in (:itemId) "
				+ " WHERE storeItem.organization.organizationId =:organizationId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemIds);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());

		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@Override
	public List<Quantities> getCurrentMonthSaleBP(Integer storeId, List<Integer> itemIds, Integer unitId) {
		/*String hql = "SELECT item.id as itemId, COALESCE(SUM(saleDetail.leaseQuantity), 0) as quantity FROM SaleDetail AS saleDetail "
				+ "		INNER JOIN saleDetail.sale AS sale WITH sale.isDeleted = false AND sale.isActive = true AND sale.unit.unitId = :unitId "
				+ "		INNER JOIN sale.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN saleDetail.batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.id in (:itemId) "
				+ "WHERE saleDetail.isDeleted = false AND saleDetail.isActive = true AND saleDetail.unit.unitId = :unitId "
				+ "		AND date_trunc('month', saleDetail.addedDate) = date_trunc('month', current_date) group by item.id ";

		Query query = getSession().createQuery(hql);*///HIS-32075 Query Optimization
		
        String sql ="SELECT " + 
                "    item4_.id AS \"itemId\", "+
                " CAST (COALESCE(SUM(saledetail0_.lease_quantity), 0) AS bigint ) as \"quantity\" " +
                "FROM " + 
                "    pharmacy.t_sale_detail saledetail0_ " + 
                "INNER JOIN pharmacy.t_sale sale1_ ON " + 
                "    saledetail0_.sale_id = sale1_.id " + 
                "    AND sale1_.is_deleted = false " + 
                "    AND sale1_.is_active = true " + 
                "    AND sale1_.unit_id = :unitId " + 
                "INNER JOIN public.m_store store2_ ON " + 
                "    sale1_.store_id = store2_.store_id " + 
                "    AND store2_.store_id = :storeId " + 
                "    AND store2_.is_deleted = false " + 
                "    AND store2_.is_active = true " + 
                "INNER JOIN inventory.t_batch batch3_ ON " + 
                "    saledetail0_.batch_id = batch3_.id " + 
                "INNER JOIN public.m_item item4_ ON " + 
                "    batch3_.item_id = item4_.id " + 
                "    AND item4_.id IN (:itemId) " + 
                "WHERE " + 
                "    saledetail0_.is_deleted = false " + 
                "    AND saledetail0_.is_active = true " + 
                "    AND saledetail0_.unit_id = :unitId " + 
                "    AND saledetail0_.added_date >= date_trunc('month', current_date)\r\n" + 
                "    AND saledetail0_.added_date < date_trunc('month', current_date) + INTERVAL '1 month' " + 
                " GROUP BY " + 
                "    item4_.id";
        Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("quantity", StandardBasicTypes.LONG);

		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.Store> getUserStoreListBP(Integer userId, Integer unitId) {
		String hql = " SELECT store.storeId as id, store.storeName as store FROM Store AS store "
				+ "		INNER JOIN store.userStoreMappersList as userStore WITH userStore.isActive = true "
				+ "					AND userStore.isDeleted = false "
				+ " WHERE store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId AND userStore.userId = :userId"
				+ " ORDER BY store.storeName";

		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId != null ? userId : sessionService.getUserId());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.Store.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.Store> getStoreListBP(Integer unitId) {
		String hql = " SELECT store.storeId as id, store.storeName as store FROM Store AS store "
				+ " WHERE store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId"
				+ " ORDER BY store.storeName";

		Query query = getSession().createQuery(hql);
		// query.setParameter("userId", sessionService.getUserId());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.Store.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.Status> getScreenStatusBP(Integer screenId) {
		String hql = "SELECT status.id as id, status.status as status FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false AND screen.isActive = true "
				+ "		INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false "
				+ "ORDER BY status.status ASC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("screenId", screenId);
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.Status.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.AssetType> getAssetTypeListBP() {
		String hql = "SELECT assetType.id as id, assetType.type as type FROM AssetType AS assetType "
				// + "WHERE assetType.isActive = true AND assetType.isDeleted = false AND
				// assetType.unit.unitId = :unitId "
				+ "WHERE assetType.isActive = true AND assetType.isDeleted = false   " + "ORDER BY assetType.type";

		Query query = getSession().createQuery(hql);
		// query.setParameter("unitId", sessionService.getUnitId());
		// query.setParameter("organizationId", sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.AssetType.class));
		return query.list();
	}

	@Override
	public List<AgainstPOSearchResponse> searchPurchaseOrderBP(AgainstPurchaseOrder againstPurchaseOrder,
			List<Integer> statusIds) {

		String hql = "SELECT purchaseOrder.id as id, vendor.name as vendorName, vendor.id as vendorId , "
				+ "to_char(purchaseOrder.dueDate,'YYYY-MM-dd') as dueDate,"
				+ "store.storeId as storeId, store.storeName as storeName, purchaseOrder.poNumber as poNo,"
				+ "purchaseOrder.addedDate as poDate" + " FROM PurchaseOrder AS purchaseOrder"
				// + " INNER JOIN purchaseOrder.purchaseOrderDetailList AS purchaseOrderDetail
				// WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted =
				// false "
				+ " 	INNER JOIN purchaseOrder.addedBy as user " + " 	LEFT JOIN purchaseOrder.store as store  "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType "
				+ "		LEFT JOIN purchaseOrder.status as status "
				+ " 	INNER JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType "
				+ " 	LEFT JOIN purchaseOrder.requestType as requestType "
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
				+ " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate  and purchaseOrder.isForcefuly is null  "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:vendorId = 0 OR purchaseOrder.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName ) "
				+ " 	AND (:poNo = '' OR lower(purchaseOrder.poNumber) LIKE :poNo ) "
				+ "     AND (approvalStatus.id IN (:approvalStatusId))"
				// + " AND purchaseOrderDetail.status.id = :detailStatusId "
				+ " AND purchaseOrder.id IN (SELECT detail.purchaseOrder.id FROM PurchaseOrderDetail AS detail WHERE detail.status.id = :detailStatusId) "
				+ " AND purchaseOrder.isConsignment = false AND purchaseOrder.poFinalApprovalRequired=true " + " ORDER BY purchaseOrder.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(againstPurchaseOrder.getFromDate()));
		query.setParameter("toDate", Util.getDate(againstPurchaseOrder.getToDate()));
		query.setParameter("storeId",
				againstPurchaseOrder.getStoreId() != null ? againstPurchaseOrder.getStoreId() : 0);
		query.setParameter("assetTypeId",
				againstPurchaseOrder.getAssetTypeId() != null ? againstPurchaseOrder.getAssetTypeId() : 0);
		query.setParameter("poNo",
				againstPurchaseOrder.getPoNumber() != null
						? "%" + againstPurchaseOrder.getPoNumber().toLowerCase() + "%"
						: "");

		if (againstPurchaseOrder.getVendorId() != null && againstPurchaseOrder.getVendorId() > 0) {
			query.setParameter("vendorId",
					againstPurchaseOrder.getVendorId() != null ? againstPurchaseOrder.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (againstPurchaseOrder.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					againstPurchaseOrder.getVendorName() != null
							? againstPurchaseOrder.getVendorName().toLowerCase() + '%'
							: "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("detailStatusId", 5);
		query.setParameter("unitId", againstPurchaseOrder.getUnitId() != null ? againstPurchaseOrder.getUnitId()
				: sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(AgainstPOSearchResponse.class));
		return query.list();
	}

	@Override
	public List<PurchaseOrderDetailResponse> getPoDetailsResponse(List<Integer> poIds) {

		String sql = "select pod.id as id, po.id as purchaseOrderId, item.id as itemId, item.code as itemCode, item.\"name\" as itemName,"
				+ "		coalesce(pod.po_approved_quantity,0) as poQuantity, coalesce(pod.bonus_approved_quantity, 0) as bonusQuantity, "
				+ "		pod.cop as cop, pod.mrp as mrp, coalesce(grnDetails.qty,0) as grnApprovedAndReceivedSum "
				+ "			from procurement.t_purchase_order_detail pod"
				+ "				inner join procurement.t_purchase_order po on po.id = pod.purchase_order_id and po.id in (:poIds) "
				+ "				inner join public.m_item item on item.id = pod.item_id"
				+ "				left join(select pod.id as podId, sum(case "
				+ "									when "
				+ "										grnd.grn_approved_quantity isnull"
				+ "									then"
				+ "										coalesce(grnd.grn_received_quantity,0)"
				+ "									else	"
				+ "										grnd.grn_approved_quantity"
				+ "									end) as qty  "
				+ "					from procurement.t_good_receipt_note_detail grnd"
				+ "					inner join procurement.t_purchase_order_detail pod on pod.id = grnd.po_detail_id where grnd.status_id !=:grnDetailsStatusId  "
				+ "					group by pod.id) as grnDetails on grnDetails.podId = pod.id"
				+ "				where pod.status_id = :detailStatusId and pod.is_active = true and pod.is_deleted = false order by pod.id";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemId").addScalar("itemCode")
				.addScalar("itemName").addScalar("poQuantity").addScalar("bonusQuantity").addScalar("cop")
				.addScalar("mrp").addScalar("purchaseOrderId")
				.addScalar("grnApprovedAndReceivedSum", StandardBasicTypes.INTEGER);

		query.setParameterList("poIds", poIds);
		query.setParameter("detailStatusId", 5);
		query.setParameter("grnDetailsStatusId", com.param.common.constants.Status.Closed.getStatus());
		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailResponse.class));
		return query.list();
	}

	@Override
	public List<AgainstPOSearchResponse> getPurchaseOrderListBP(List<Integer> poIds) {
		String hql = "SELECT purchaseOrder.id as poId, purchaseOrder.id as id, purchaseOrder.poNumber as poNo,"
				+ "to_char(purchaseOrder.dueDate,'YYYY-MM-dd') as dueDate,"
				+ "vendor.id as vendorId, vendor.name as vendorName, assetType.id as assetTypeId, store.storeId as storeId "
				+ "  , purchaseOrder.addedDate as poDate , purchaseOrder.discount as discount , purchaseOrder.totalDiscount as totalDiscount"
				+ " , discountType.id as  discountTypeId " + " FROM PurchaseOrder AS purchaseOrder "
				+ " INNER JOIN purchaseOrder.vendor as vendor " + " INNER JOIN purchaseOrder.assetType as assetType "
				+ " INNER JOIN purchaseOrder.store as store "
				+ " LEFT JOIN purchaseOrder.discountType as discountType  "
				+ "WHERE purchaseOrder.id in (:id)  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", poIds);
		query.setResultTransformer(Transformers.aliasToBean(AgainstPOSearchResponse.class));
		return query.list();
	}

	@Override
	public List<PurchaseOrderDetailResponse> getItemsDetailsAgainstPOBP(List<Integer> consumablePoIds,
			List<Integer> consumableItemIds) {
		String hql = " SELECT purchaseOrderDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ " purchaseOrderDetail.poApprovedQuantity AS poQuantity,purchaseOrderDetail.bonusApprovedQuantity AS bonusQuantity,"
				+ " purchaseOrderDetail.poLeaseQuantity AS poLeaseQuantity,purchaseOrderDetail.bonusLeaseQuantity AS bonusLeaseQuantity,"
				+ " poUomType.id AS poUomTypeId, poUomType.type AS poUomType, poUomUnit.id AS poUomUnitId, poUomUnit.units AS poUomUnit,"
				+ " bonusUomType.id AS bonusUomTypeId, bonusUomType.type AS bonusUomType, bonusUomUnit.id AS bonusUomUnitId, bonusUomUnit.units AS bonusUomUnit,"
				+ " purchaseOrderDetail.cop AS cop,purchaseOrderDetail.mrp AS mrp, purchaseOrderDetail.discount AS discount, "
				+ " purchaseOrderDetail.totalDiscount AS discountAmount,purchaseOrderDetail.totalOtherAmount AS totalOtherAmount, purchaseOrderDetail.taxPercentage AS taxPercentage, "
				+ " generic.id AS genericId, generic.name AS genericName, "
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, tax.id AS taxId, tax.name AS taxName, "
				+ " tax.taxPercentage AS taxPercent, purchaseOrderDetail.specification AS specification, "
				+ " purchaseOrderDetail.purchaseOrder.id as poId, purchaseOrderDetail.id as poDetailId  , purchaseOrderDetail.pendingQuantity as pendingQuantity "
				+ "  , purchaseOrderDetail.remark as remark , item.isBatchRequired as  isBatchRequired, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail " + "  INNER JOIN purchaseOrderDetail.item  AS item"
				+ "  INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "  INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted = false AND purchaseTax.isActive = true "
				+ "  INNER JOIN purchaseOrderDetail.tax AS tax WITH tax.isDeleted = false AND tax.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.poUomType as poUomType WITH poUomType.isDeleted = false AND poUomType.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.poUomUnit as poUomUnit WITH poUomUnit.isDeleted = false AND poUomUnit.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.bonusUomType as bonusUomType WITH bonusUomType.isDeleted = false AND bonusUomType.isActive = true "
				+ "	 LEFT OUTER JOIN purchaseOrderDetail.bonusUomUnit as bonusUomUnit WITH bonusUomUnit.isDeleted = false AND bonusUomUnit.isActive = true "
				+ "  WHERE purchaseOrderDetail.item.id in (:itemId) AND purchaseOrderDetail.purchaseOrder.id in (:Id) "
				+ "	AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "  ORDER BY purchaseOrderDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemId", consumableItemIds);
		query.setParameterList("Id", consumablePoIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailResponse.class));
		return query.list();
	}

	@Override
	public Collection<? extends PurchaseOrderDetailResponse> getFixedItemsDetailsAgainstPOBP(List<Integer> fixedPoIds,
			List<Integer> fixedleItemIds) {
		String hql = " SELECT purchaseOrderDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ " purchaseOrderDetail.poApprovedQuantity AS poQuantity,purchaseOrderDetail.bonusApprovedQuantity AS bonusQuantity,"
				+ " purchaseOrderDetail.poLeaseQuantity AS poLeaseQuantity,purchaseOrderDetail.bonusLeaseQuantity AS bonusLeaseQuantity,"
				+ " purchaseOrderDetail.cop AS cop,purchaseOrderDetail.mrp AS mrp, purchaseOrderDetail.discount AS discount, "
				+ " purchaseOrderDetail.totalDiscount AS discountAmount,purchaseOrderDetail.totalOtherAmount AS totalOtherAmount, purchaseOrderDetail.taxPercentage AS taxPercentage, "
				+ "	manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, "
				// + ", tax.id AS taxId, tax.name AS taxName, "
				// + " tax.taxPercentage AS taxPercent, "
				+ " purchaseOrderDetail.specification AS specification, "
				+ " purchaseOrderDetail.purchaseOrder.id as poId, purchaseOrderDetail.id as poDetailId , purchaseOrderDetail.pendingQuantity as pendingQuantity "
				// added code for HIS-9505 GRN form asset against PO issues - Approve quantity
				// is showing 0
				+ " ,purchaseOrderDetail.remark as remark "
				+ " , tax.id AS taxId, tax.name AS taxName, serviceProvider.id as serviceProviderId, serviceProvider.name as serviceProvider, "
				+ " purchaseOrderDetail.warrantyPeriod  as warrantyPeriod, purchaseOrderDetail.supportTerms as supportTerms, "
				+ " purchaseOrderDetail.technicalSpecification as technicalSpecification, purchaseOrderDetail.expectedPayment as expectedPayment, "
				+ " item.isThreeUOMApplicable as isThreeUOMApplicable"
				// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
				// showing 0
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail " + "  INNER JOIN purchaseOrderDetail.item  AS item"
				+ "  INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				// + " INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isDeleted =
				// false AND purchaseTax.isActive = true "
				// + " INNER JOIN purchaseOrderDetail.tax AS tax WITH tax.isDeleted = false AND
				// tax.isActive = true "

				// added code for HIS-9505 GRN form asset against PO issues - Approve quantity
				// is showing 0
				+ " INNER JOIN purchaseOrderDetail.tax AS tax "
				+ " INNER JOIN purchaseOrderDetail.serviceProvider AS serviceProvider "
				// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
				// showing 0

				+ "  WHERE purchaseOrderDetail.item.id in (:itemId) AND purchaseOrderDetail.purchaseOrder.id in (:Id) AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "  ORDER BY purchaseOrderDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemId", fixedleItemIds);
		query.setParameterList("Id", fixedPoIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailResponse.class));
		return query.list();
	}

	@Override
	public List<com.param.global.dto.global.PurchaseOrderStaged> getPurchaseOrderStagedListBP(
			List<Integer> poDetailIds) {
		String hql = " SELECT poStaged.id AS id, poStaged.approvedQunatity AS poApprovedQuantity, "
				+ " 		poStaged.stagedDate AS stagedDate, purchaseOrderDetail.id as poDetailId "
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
				+ " WHERE purchaseOrderDetail.id in (:id) AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " ORDER BY poStaged.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", poDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.global.PurchaseOrderStaged.class));

		return query.list();
	}

	@Override
	public List<PurchaseOrderStagedDcDate> getPurchaseOrderStagedListDcDateBP(
			List<Integer> poDetailIds) {/*
										 * String hql =
										 * "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS stagedReceivedQuantity, grnStaged.stagedDcDate AS stagedDcDate, "
										 * +
										 * " COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity, purchaseOrderDetail.id as poDetailId  "
										 * + " FROM PurchaseOrderDetail AS purchaseOrderDetail " +
										 * "		INNER JOIN purchaseOrderDetail.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
										 * +
										 * " WHERE purchaseOrderDetail.id in (:id) AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
										 * ;
										 * 
										 * Query query = getSession().createQuery(hql); query.setParameterList("id",
										 * poDetailIds);
										 * query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.
										 * global.PurchaseOrderStagedDcDate.class));
										 * 
										 * return query.list();
										 */

		String hql = "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS stagedReceivedQuantity, grnStaged.stagedDcDate AS stagedDcDate, "
				+ " COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity, purchaseOrderDetail.id as poDetailId  "
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ "	 INNER JOIN purchaseOrderDetail.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
				+ "   INNER JOIN  grnStaged.goodReceiptNoteDetail as grnDetails "
				+ "   INNER JOIN  grnDetails.goodReceiptNote as grn "
				+ " WHERE purchaseOrderDetail.id in (:id) AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " and grn.approvalStatus.id !=:approvalStatusId";
		// + " and grn.approvalStatus.id !=:approvalStatusId and grnDetails.status.id
		// !=:grnDetailsStatusId";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", poDetailIds);
		query.setParameter("approvalStatusId", com.param.common.constants.Status.Cancelled.getStatus());
		// query.setParameter("grnDetailsStatusId",
		// com.param.common.constants.Status.Closed.getStatus());
		query.setResultTransformer(
				Transformers.aliasToBean(com.param.global.dto.global.PurchaseOrderStagedDcDate.class));

		return query.list();

	}

	@Override
	public List<POOtherCharges> getPurchaseOrderOtherChargesMapperBP(Integer poId) {
		String hql = "SELECT poOtherCharges.amount as amount, otherCharge.id as otherChragesId, "
				+ "otherCharge.description as otherCharges," + " poOtherCharges.purchaseOrder.id as poId "
				+ " ,otherCharge.id as otherChargesId, poOtherCharges.id as id " // added
				+ "FROM PurchaseOrderOtherChargesMapper AS poOtherCharges "
				+ " INNER JOIN poOtherCharges.otherCharge as otherCharge "
				+ "WHERE poOtherCharges.purchaseOrder.id =:id  AND poOtherCharges.isActive = true AND poOtherCharges.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", poId);
		query.setResultTransformer(Transformers.aliasToBean(POOtherCharges.class));
		return query.list();
	}

	@Override
	public List<POOtherCharges> getAppliedOtherChargesForPO(List<Integer> poDetailIds) {
		String hql = "SELECT purchaseOrderDetail.id AS poDetailId  "
				+ "FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail  "
				+ "INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail as purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true "
				+ "WHERE purchaseOrderDetail.id in (:id)  AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", poDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(POOtherCharges.class));
		return query.list();

	}

	@Override
	public List<GRNAgainstApprovedPOQuantity> getStoreStockByItemBP(List<Integer> storeIds,
			List<Integer> poDetailsItemIds, Integer unitId) {
		String hql = "SELECT store.storeId as storeId, item.id as itemId, COALESCE(SUM(storeStock.availableQty), 0) as quantity FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE store.storeId in (:storeId) AND item.id in (:itemId) "
				+ "		AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ " group by store.storeId, item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("storeId", storeIds);
		query.setParameterList("itemId", poDetailsItemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(GRNAgainstApprovedPOQuantity.class));
		return query.list();
	}

	@Override
	public List<GRNAgainstApprovedPOQuantity> getStoreStockInTransitByItemBP(List<Integer> storeIds,
			List<Integer> poDetailsItemIds) {
		String hql = "SELECT storeIndent.fromStore.storeId as storeId, item.id as itemId, COALESCE(SUM(storeIndentDetail.inTransitQuantity), 0) as quantity "
				+ "FROM StoreIndentDetail AS storeIndentDetail "
				+ "		INNER JOIN storeIndentDetail.storeIndent AS storeIndent "
				+ "		INNER JOIN storeIndentDetail.item AS item "
				+ "WHERE item.id in (:itemId) AND storeIndent.fromStore.storeId in (:storeId) "
				+ "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false "
				+ " group by item.id, storeIndent.fromStore.storeId ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("storeId", storeIds);
		query.setParameterList("itemId", poDetailsItemIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNAgainstApprovedPOQuantity.class));
		return query.list();
	}

	@Override
	public List<GRNAgainstApprovedPOQuantity> getStoreMaxQuantityByItemBP(List<Integer> storeIds,
			List<Integer> poDetailsItemIds) {
		String hql = "SELECT store.storeId as storeId, item.id as itemId, COALESCE(storeItem.maxQty, 0) as intQuantity FROM StoreItemMapper AS storeItem "
				+ "		INNER JOIN storeItem.store AS store WITH store.storeId in (:storeId) "
				+ "		INNER JOIN storeItem.item AS item WITH item.id in (:itemId)";

		Query query = getSession().createQuery(hql);
		query.setParameterList("storeId", storeIds);
		query.setParameterList("itemId", poDetailsItemIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNAgainstApprovedPOQuantity.class));
		return query.list();
	}

	@Override
	public List<GRNAgainstApprovedPOQuantity> getPurchaseRequestLeaseQunatity(List<Integer> storeIds,
			List<Integer> poDetailsItemIds, Integer unitId) {
		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) as quantity, "
				+ "purchaseRequest.store.storeId as storeId, purchaseRequestDetail.item.id as itemId "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId "
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId in (:storeId) AND purchaseRequestDetail.item.id in (:itemId) AND purchaseRequestDetail.goodReceiptNoteDetail is null  "
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId group by purchaseRequest.store.storeId, purchaseRequestDetail.item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameterList("storeId", storeIds);
		query.setParameterList("itemId", poDetailsItemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(GRNAgainstApprovedPOQuantity.class));
		return query.list();
	}

	@Override
	public List<GRNQuantity> getGrnReceivedAndApprovedQtySum(
			List<Integer> poDetailIds) {/*
										 * String sql = "select pod.id as poDetailId, sum(case " +
										 * "								when " +
										 * "									grnd.grn_approved_quantity isnull " +
										 * "								then " +
										 * "									coalesce(grnd.grn_received_quantity,0) "
										 * + "								else	" +
										 * "									grnd.grn_approved_quantity " +
										 * "								end) as receivedAndApprovedQtySum  " +
										 * "				from procurement.t_good_receipt_note_detail grnd " +
										 * "				inner join procurement.t_purchase_order_detail pod on pod.id = grnd.po_detail_id and pod.id in (:poIds)  "
										 * + "				group by pod.id";
										 * 
										 * Query query =
										 * getSession().createSQLQuery(sql).addScalar("poDetailId").addScalar(
										 * "receivedAndApprovedQtySum", StandardBasicTypes.INTEGER);
										 * 
										 * query.setParameterList("poIds", poDetailIds); //
										 * query.setParameter("detailStatusId", 5);
										 * query.setResultTransformer(Transformers.aliasToBean(GRNQuantity.class));
										 * return query.list();
										 */

		String sql = "select pod.id as poDetailId, sum(case " + "								when "
				+ "									grnd.grn_approved_quantity isnull "
				+ "								then "
				+ "									coalesce(grnd.grn_received_quantity,0) "
				+ "								else	"
				+ "									grnd.grn_approved_quantity "
				+ "								end) as receivedAndApprovedQtySum  "
				+ "				from procurement.t_good_receipt_note_detail grnd "
				+ "				inner join procurement.t_purchase_order_detail pod on pod.id = grnd.po_detail_id and pod.id in (:poIds)  "
				+ " WHERE grnd.status_id !=:grnDetailsStatusId " + "				group by pod.id";

		Query query = getSession().createSQLQuery(sql).addScalar("poDetailId").addScalar("receivedAndApprovedQtySum",
				StandardBasicTypes.INTEGER);

		query.setParameterList("poIds", poDetailIds);
		query.setParameter("grnDetailsStatusId", com.param.common.constants.Status.Closed.getStatus());

		// query.setParameter("detailStatusId", 5);
		query.setResultTransformer(Transformers.aliasToBean(GRNQuantity.class));
		return query.list();

	}

	@Override
	public List<SalesType> getSalesTypeListBP() {

		String hql = "SELECT saleType.id as id, saleType.type as type " + " FROM SaleType AS saleType "
				+ "WHERE saleType.isActive = true AND saleType.isDeleted = false "
				+ " ORDER BY saleType.type ASC ";
		// + "AND saleType.unit.unitId = :unitId ";
		// Shifted to global level
		Query query = getSession().createQuery(hql);
		// query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(SalesType.class));
		return query.list();
	}

	/*
	 * @Override public List<DoctorSpecialityResp>
	 * doctorSearchByIdValueBP(List<Integer> doctorIds) {
	 * 
	 * String hql =
	 * "SELECT speciality.specialityName as speciality, doctor.doctorId as doctorId "
	 * + " FROM DoctorSpeciality AS doctorSpeciality " +
	 * " INNER JOIN doctorSpeciality.doctor AS doctor WITH doctor.status = 'A' " +
	 * " INNER JOIN doctorSpeciality.speciality AS speciality WITH speciality.status = 'A' "
	 * +
	 * " WHERE  doctorSpeciality.status = 'A' AND doctorSpeciality.unitId = :unitId "
	 * + " AND (doctor.id in (:id))";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameterList("id",
	 * doctorIds); query.setParameter("unitId", sessionService.getUnitId());
	 * query.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityResp.
	 * class)); return query.list(); }
	 */

	@Override
	public List<BatchResponse> getBatchDetailsPharmacyBP(List<Integer> itemIds, Integer unitId, Integer storeId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch.id as batchId, batch.batchNo as batchNo, batch.expiryDate as expiry, batch.sellingPrice as sellingPrice,"
				+ " storeStock.availableQty as availableStockQuantity, batch.markup as markup, batch.rate as rate, item.id as itemId "
				+ " ,batch.isConsignment as isConsignment , batch.actualLendingPrice as actualLendingPrice  " // added
																												// by
																												// minesh
				+ " FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true  "
				+ "		INNER JOIN batch.storeStockList AS storeStock "
				// + "WHERE item.id in (:id) AND batch.isDeleted = false AND batch.isActive =
				// true AND batch.isQuarantine = false AND batch.isPharmacy = false AND
				// batch.unit.unitId = :unitId "
				+ "WHERE item.id in (:id) AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND  storeStock.availableQty is not null and storeStock.totalQty is not null AND (batch.expiryDate >= :greaterThanCurrentMonth OR  batch.expiryDate is null) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", itemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId != null ? storeId : sessionService.getStoreId());
		query.setParameter("greaterThanCurrentMonth", current);
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getBatchUomListPharmacyBP(List<Long> batchIds) {

		String hql = "SELECT  uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "CONCAT(uomType.code, ' - ', uomUnit.units) as uomDetails, "
				+ "batchUomMapper.conversions AS conversion, batch.id as batchId, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom  "
				+ "FROM BatchUomMapper AS batchUomMapper " + "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		INNER JOIN batchUomMapper.uomType AS uomType "
				+ "		INNER JOIN batchUomMapper.batch AS batch "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.id in (:batchId) ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("batchId", batchIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getBatchUomListPharmacyByItemId(Integer itemId) {

		String hql = "SELECT  distinct uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "CONCAT(uomType.code, ' - ', uomUnit.units) as uomDetails, "
				+ "batchUomMapper.conversions AS conversion, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom  "
				+ "FROM BatchUomMapper AS batchUomMapper " + "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		INNER JOIN batchUomMapper.uomType AS uomType "
				+ "		INNER JOIN batchUomMapper.batch AS batch "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.item.id = :itemId and batch.isBatchRequired = false ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}
	/*
	 * @Override public List<DoctorSpecialityResp> doctorSearchByIdValueBP(Integer
	 * doctorId) { String hql =
	 * "SELECT speciality.specialityName as speciality, doctor.doctorId as doctorId "
	 * + " FROM DoctorSpeciality AS doctorSpeciality " +
	 * " left JOIN doctorSpeciality.doctor AS doctor WITH doctor.status = 'A' " +
	 * " left JOIN doctorSpeciality.speciality AS speciality WITH speciality.status = 'A' "
	 * +
	 * " WHERE  doctorSpeciality.status = 'A' AND doctorSpeciality.unitId = :unitId "
	 * + " AND (doctor.id = :id)";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("id",
	 * doctorId); query.setParameter("unitId", sessionService.getUnitId());
	 * query.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityResp.
	 * class)); return query.list(); }
	 */

	@Override
	public List<TaxDetail> getTaxDetailBP(List<Integer> taxIds) {
		String hql = "SELECT taxDetail FROM TaxDetail AS taxDetail "
				+ " INNER JOIN FETCH taxDetail.tax AS tax WITH tax.id in(:taxId) "
				+ " WHERE taxDetail.isActive = true AND taxDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("taxId", taxIds);
		return query.list();
	}

	@Override
	public List<BatchResponse> getBatchForAssignPicker(List<Integer> assignPickerDetailIds) {
		String hql = "SELECT batch.id as batchId, batch.batchNo as batchNo, batch.expiryDate as expiry,"
				+ " batch.sellingPrice as sellingPrice,"
				+ "storeStock.availableQty as availableStockQuantity, batch.markup as markup, "
				+ "batch.rate as rate, assignPickerDetail.id as assignPickerId "
				+ " FROM AssignPickerDetail AS assignPickerDetail "
				+ "		INNER JOIN assignPickerDetail.batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock " + "WHERE "
				+ " assignPickerDetail.id in (:assignPickerDetailsId) " + " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameterList("assignPickerDetailsId", assignPickerDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));
		return query.list();
	}

	@Override
	public List<Quantities> getStoreStockByItemPharmacyBP(Integer storeId, List<Integer> itemIds, Integer unitId) {
		String hql = "SELECT COALESCE(SUM(storeStock.availableQty), 0) as quantity, item.id as itemId"
				+ " FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "WHERE store.storeId = :storeId AND item.id in (:itemId) "
				+ "		AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
				+ " GROUP BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", itemIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@Override
	public List<BatchResponse> getBatchListPharmacyBP(List<Integer> itemIds, List<Long> batchIds, Integer storeId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch.id as batchId, batch.batchNo as batchNo, batch.expiryDate as expiry, batch.sellingPrice as sellingPrice,"
				+ "storeStock.availableQty as availableStockQuantity, batch.markup as markup, batch.rate as rate, item.id as itemId "
				+ " FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN batch.storeStockList AS storeStock "
				+ "WHERE item.id in (:itemId) AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
				+ "		AND batch.id NOT IN (:batchId) AND storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemId", itemIds);
		query.setParameterList("batchId", batchIds);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));
		return query.list();
	}

	@Override
	public PharamcyBillPayeeDetails savePharamcyBillPayedetails(PharamcyBillPayeeDetails pharamcyBillPayeeDetails) {
		getSession().save(pharamcyBillPayeeDetails);
		return pharamcyBillPayeeDetails;
	}

	@Override
	public Integer updateDueAmountByBillBilPayeeId(Integer saleId, Integer payeeId, Double amount) {

		try {
			Integer result = getSession()
					.createSQLQuery(" update " + " pharmacy.t_pharmacy_bill_payee_details " + " set "
                            + " due_amount = (case when due_amount > :amount then coalesce(due_amount) - :amount else 0 end) " + " where " + " t_sale_id = :saleId "
							+ " and payee_id = :payeeId")
					.setParameter("saleId", saleId).setParameter("payeeId", payeeId).setParameter("amount", amount)
					.executeUpdate();

			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<PharmacyTariff> getTariff(List<Integer> prodCatIds, int entitlementId) {
		if(prodCatIds != null && !prodCatIds.isEmpty()) {
			String hql = "SELECT tarrif "
					+ "	from PharmacyTariff AS tarrif WHERE tarrif.productCategoryId in (:productCategoryId) "
					+ "	AND tarrif.paymentEntitlementId = :entitlementId and tarrif.status = 'A' ";
			Query query = getSession().createQuery(hql);
			query.setParameterList("productCategoryId", prodCatIds);
			query.setParameter("entitlementId", entitlementId);
			return query.list();
		}
		return null;
	}

	public Integer updateSaleMasterIsInvoiceCreationBySaleId(Integer saleId, Integer payeeId) throws BillingException {

		try {
			Integer result = getSession()
					.createSQLQuery(
							" update " + " pharmacy.t_pharmacy_bill_payee_details " + " set is_invoice_generated='Y' "
									+ " where " + " t_sale_id = :saleId " + " and payee_id = :payeeId")
					.setParameter("saleId", saleId).setParameter("payeeId", payeeId).executeUpdate();

			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<SaleDetailsDto> getBillingDetailsBySaleId(Integer saleId, Integer payeeId) throws BillingException {
		try {
			List<SaleDetailsDto> list = getSession().getNamedQuery("GET_SALE_DETAILS_BY_SALE_ID")
					.setParameter("saleId", saleId).setParameter("payeeId", payeeId)
					.setResultTransformer(Transformers.aliasToBean(SaleDetailsDto.class)).list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Integer updatePharamcyDetailsPaidAmount(Integer saleId, Double amount, Integer status, Integer entitlementId, Double disallowdAmount)
			throws BillingException {
		try {
			if(entitlementId==1) {
			Integer result = (Integer) getSession()
					.createSQLQuery("update" + "	pharmacy.t_sale" + " set"
							+ "	paid_amount = coalesce(paid_amount, 0)+:amount,"
							+ "	outstanding_amount = :outstandingAmount, "
							+ " status_id = :status " + " where id = :saleId")
					.setParameter("status", status).setParameter("saleId", saleId).setParameter("amount", amount).setParameter("outstandingAmount", disallowdAmount)
					.executeUpdate();

			return result;
			}
			else
			{
				Integer result = (Integer) getSession()
						.createSQLQuery("update" + "	pharmacy.t_sale" + " set"
								+ "	paid_amount = coalesce(paid_amount, 0)+:amount,"
								+ "	outstanding_amount = :outstandingAmount, "
								+ " status_id = :status " + " where id = :saleId")
						.setParameter("status", status).setParameter("saleId", saleId).setParameter("amount", amount).setParameter("outstandingAmount", disallowdAmount)
						.executeUpdate();

				return result;
			}
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Sale getSaleForBackOffice(Long id) {

		String hql = "SELECT sale FROM Sale AS sale "
				// + " INNER JOIN sale.saleType as saleType WITH saleType.isActive = true AND
				// saleType.isDeleted = false AND saleType.unit.unitId = :unitId "
				+ "		INNER JOIN sale.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN sale.saleDetailsList as items WITH items.isActive = true AND items.isDeleted = false AND items.unit.unitId = :unitId "
				+ "		INNER JOIN items.batch as batch WITH batch.isActive = true AND batch.isDeleted = false  AND batch.unit.unitId = :unitId " // AND
																																					// batch.isPharmacy
																																					// =
																																					// false
				+ "		INNER JOIN batch.item as item WITH item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
				+ "		INNER JOIN item.generic as generic WITH generic.isActive = true AND generic.isDeleted = false AND generic.unit.unitId = :unitId "
				+ "		INNER JOIN item.manufacturer as manufacturer WITH manufacturer.isActive = true AND manufacturer.isDeleted = false AND manufacturer.unit.unitId = :unitId "
				+ "		INNER JOIN items.uomUnit as uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false AND uomUnit.unit.unitId = :unitId "
				+ "		INNER JOIN items.tax as tax WITH tax.isActive = true AND tax.isDeleted = false AND tax.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.discountType as discountType WITH discountType.isActive = true AND discountType.isDeleted = false AND discountType.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.patient as patient WITH patient.status = 'A' "
				+ "		LEFT JOIN sale.tempPatient as tempPatient WITH tempPatient.isActive = true AND tempPatient.isDeleted = false AND tempPatient.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.doctor as doctor WITH doctor.status = 'A' AND doctor.unit.unitId = :unitId "
				+ "		LEFT JOIN sale.tempDoctor as tempDoctor WITH tempDoctor.isActive = true AND tempDoctor.isDeleted = false AND tempDoctor.unit.unitId = :unitId "
				+ "WHERE sale.id = :id AND sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Sale) query.uniqueResult();
	}

	@Override
	public Integer updatePharamcyBillStataus(Integer saleId, Integer status) throws BillingException {
		try {
			Integer result = getSession()
					.createSQLQuery("update pharmacy.t_sale set status_id = :status where id= :saleId")
					.setParameter("saleId", saleId).setParameter("status", status).executeUpdate();
			return result;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getDetailsBySaleDetailsId(OrderDetailsMasterDto orderDto) {
		try {
			List<OrderDetailsMasterDto> listObject = getSession().createSQLQuery(
					"select cast(sdtls.id as Integer) as \"saleDetailId\" ,sdtls.payee_id as \"payeeId\",sdtls.rate as \"rate\","
							+ "  sdtls.tax_id as \"taxId\", cast(sdtls.tax_percentage as NUMERIC)  as \"taxPer\", cast(sdtls.tax_amount as DECIMAL) as \"taxAmount\","
							+ "  cast( sdtls.credit_payable as DECIMAL) as \"creditPayable\" , cast( sdtls.self_payable as DECIMAL) as \"selfPayable\",case when itm.product_category_id is not null then  itm.product_category_id else genric.product_category_id end as \"productCategoryId\","
							+ " sale.admission_id as  \"admissionId\", sale.encounter_id as \"encounterId\", sale.patient_id as \"patientId\" "
							+ "  from pharmacy.t_sale sale "
							+ "  inner join pharmacy.t_sale_detail sdtls on sale.id=sdtls.sale_id "
							+ "  inner join inventory.t_batch btc on btc.id=sdtls.batch_id "
							+ "  left join public.m_item itm on itm.id=btc.item_id "
							+ "  left join public.m_generic genric on genric.id=itm.generic_id "
							+ "  where sdtls.id=:saleDetailsId  " + "  and sale.unit_id=:unitId "
							+ "  and sale.organization_id =:orgId")

					.setParameter("unitId", orderDto.getOrgUnitId()).setParameter("orgId", orderDto.getOrgId())
					.setParameter("saleDetailsId", orderDto.getSaleDetailId())
					.setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
			return new Response("SUCCESS", "SUCCESS CODE", "", listObject, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getSaleDetailsPayeeMapperBySaleDtlsId(SaleDetailPayeeMapperDto dto) throws Exception {
		try {
			List<SaleDetailPayeeMapperDto> listObject = getSession()
					.createSQLQuery(" select  mappr.item_id as \"itemId\",mappr.payee_amount as \"payeeAmount\","
							+ "  mappr.contract_id as \"contractId\",mappr.payee_id as \"payeeId\" ,cast(mappr.t_sale_details_id as Integer) as \"tSaleDetailsId\" "
							+ "  from  pharmacy.t_sale_detail_payee_mapper mappr"
							+ " where mappr.t_sale_details_id=:saleDetailsId  " + "  and mappr.unit_id=:unitId"
							+ "  and mappr.organization_id =:orgId " + "  and mappr.status='A'"
							+ " and mappr.item_id=:itemId" + " and mappr.payee_id !=" + sessionService.getSelfPayeeId())

					.setParameter("unitId", dto.getUnitId()).setParameter("orgId", dto.getOrganizationId())
					.setParameter("saleDetailsId", dto.gettSaleDetailsId()).setParameter("itemId", dto.getItemId())
					.setResultTransformer(Transformers.aliasToBean(SaleDetailPayeeMapperDto.class)).list();
			return new Response("SUCCESS", "SUCCESS CODE", "", listObject, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getCapDetails(ContractConsumptionDto dto) throws Exception {
		try {
			ContractConsumptionDto resDto = null;
			if (dto.getAdmissionId() != null) {
				resDto = (ContractConsumptionDto) sessionFactory.getCurrentSession().createSQLQuery(
						"select capmppr.balance_amount as \"balanceAmount\" , capmppr.amount as \"balanceCapAmount\""
								+ " 	from" + " 		service.t_vist_contract_cap_mapper capmppr" + " 	where"
								+ " 		capmppr.product_category_id =:prodCatId"
								+ " 		and capmppr.contract_id =:contractId		"
								+ " 		and capmppr.status = 'A'"
								// +" and capmppr.patient_id =:patientId"
								+ "     and capmppr.admission_id=:admissionId")
						.setParameter("prodCatId", dto.getProductCategoryId())
						.setParameter("contractId", dto.getContractId())
						/// .setParameter("patientId", dto.getPatientId())
						.setParameter("admissionId", dto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class)).uniqueResult();
			} else if (dto.getEncounterId() != null) {
				resDto = (ContractConsumptionDto) sessionFactory.getCurrentSession().createSQLQuery(
						"select capmppr.balance_amount as \"balanceAmount\" , capmppr.amount as \"balanceCapAmount\""
								+ " 	from" + " 		service.t_vist_contract_cap_mapper capmppr" + " 	where"
								+ " 		capmppr.product_category_id =:prodCatId"
								+ " 		and capmppr.contract_id =:contractId		"
								+ " 		and capmppr.status = 'A'"
//											+" 		and capmppr.patient_id =:patientId"
								+ "     and capmppr.encounter_id=:encounterId")
						.setParameter("prodCatId", dto.getProductCategoryId())
						.setParameter("contractId", dto.getContractId())
						// .setParameter("patientId", dto.getPatientId())
						.setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class)).uniqueResult();
			} else {
				resDto = (ContractConsumptionDto) sessionFactory.getCurrentSession().createSQLQuery(
						"select capmppr.balance_amount as \"balanceAmount\" , capmppr.amount as \"balanceCapAmount\""
								+ " 	from" + " 		service.t_vist_contract_cap_mapper capmppr" + " 	where"
								+ " 		capmppr.product_category_id =:prodCatId"
								+ " 		and capmppr.contract_id =:contractId		"
								+ " 		and capmppr.status = 'A'" + " 		and capmppr.patient_id =:patientId"
//											+ "     and capmppr.encounter_id=:encounterId"
								+ "")
						.setParameter("prodCatId", dto.getProductCategoryId())
						.setParameter("contractId", dto.getContractId()).setParameter("patientId", dto.getPatientId())
						// .setParameter("encounterId", dto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class)).uniqueResult();
			}
			return new Response("SUCCESS", "SUCCESS CODE", "", null, resDto);

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<Rack> getRackList(List<Long> batchIds, Integer storeId) {
		String hql = "" + " SELECT batch.id AS batchId, rack.name AS rack " + "	FROM ShelfItem AS shelfItem "
				+ "		INNER JOIN shelfItem.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ "		INNER JOIN shelfItem.store AS store WITH store.storeId = :storeId AND store.isActive = true AND store.isDeleted = false "
				+ " 	INNER JOIN shelfItem.rack AS rack WITH rack.isActive = true AND rack.isDeleted = false "
				+ " WHERE shelfItem.isActive = true and shelfItem.isDeleted = false AND batch.id IN (:batchId)";

		Query query = getSession().createQuery(hql);
		query.setParameterList("batchId", batchIds);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(Rack.class));

		return query.list();
	}

	public TempPatientRegistration getPatientById(Integer id) {
		try {
			TempPatientRegistration tempPatientRegistration = (TempPatientRegistration) getSession()
					.get(TempPatientRegistration.class, id);
			return tempPatientRegistration;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<UomResponse> getBatchUomListBP(List<Long> batchIds) {
		String hql = "SELECT  uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, batchUomMapper.conversions AS conversion, "
				+ "CONCAT(uomType.code, ' - ', uomUnit.units) as uomDetails, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom,  "
				+ " batchUomMapper.batch.id as batchId " + "FROM BatchUomMapper AS batchUomMapper "
				+ "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		INNER JOIN batchUomMapper.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN batchUomMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.id in (:batchId) ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("batchId", batchIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<User> getPickerUsers(Integer unitId) {
		/*String hql = "SELECT user FROM User AS user where user.userId in "
				+ " (select mapper.userId FROM UserRoleMapper mapper where mapper.roleId in "
				+ " (select role.roleId FROM UserRoleMaster role where role.roleTypeId = :roleTypeId)) "
				+ " and user.status = 'A' AND user.unitId = :unitId order by user.userName asc";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("roleTypeId", 5);*/
		
		
		List<User> listObject = getSession()
				.createSQLQuery("  select distinct mum.user_id as \"userId\", " + 
						"(case when mem.user_id is not null then  mem.first_name else mdm.first_name end ) as \"userName\" " + 
						"from m_user_master mum  " + 
						"inner join t_user_role_mapper turm  on turm.user_id  = mum.user_id  " + 
						"left join m_employee_master mem  on mem.user_id  = mum.user_id  " + 
						"left join doctor.m_doctor_master mdm  on mdm.user_id  = mum.user_id  " + 
						"where mum.unit_id  = :unitId and turm.role_id  in(select role_id from m_role_master mrm  where role_type_id  = 5 and  unit_id  = :unitId )" )

				.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(User.class)).list();
		

		return listObject;
	}

	@Override
	public SaleDtoChangeOfPayee getSale(Long id, Integer paymentEntitlementId, Integer patientClassId,Boolean flag) {

		String saleQuery = " with sale as ( " 
				 + " select " 
				 + " 	cast(sale.id as integer), " 
				 + " 	sale.organization_id, " 
				 + " 	sale.added_date, " 
				 + " 	sale.unit_id, " 
				 + " 	sale.bill_no, " 
				 + " 	sale.store_id, " 
				 + " 	sale.counter_id, " 
				 + " 	sale.patient_id, " 
				 + " 	sale.sale_type_id, " 
				 + " 	sale.visit_type_id, " 
				 + " 	sale.admission_id, " 
				 + " 	sale.doctor_id, " 
				 + " 	sale.status_id, " 
				 + " 	sale.discount_type_id, " 
				 + " 	sale.discount_category_id, " 
				 + " 	sale.encounter_id, " 
				 + " 	sale.received_by, " 
				 + " 	sale.received_on, " 
				 + " 	sale.is_handover, " 
				 + " 	sale.handover_by, " 
				 + " 	sale.prescription_id, " 
				 + " 	sale.carrier_id, " 
				 + " 	sale.running_number, " 
				 + " 	sale.smart_card_submited, " 
				 + " 	sale.claim_send, " 
				 + " 	sale.is_consolidated_bill, " 
				 + " 	(case when sd.item_net_amount is null or sd.item_net_amount = 0 THEN "
                 + " (case when sale.cop is not null and sale.cop = true then   "
				 + " (coalesce (case when tariff.id is not null "
				 + " and tariff.mark_up_down = 1 then coalesce(round(cast(batch.actual_lending_price as numeric), 2), 0) + (coalesce(round(cast(batch.actual_lending_price as numeric), 2), 0) * (coalesce(tariff.mark_up_down_per, 0) * 0.01))"
				 + " when tariff.id is not null "
				 + " and tariff.mark_up_down = 0 then coalesce(round(cast(batch.actual_lending_price as numeric), 2), 0) - (coalesce(round(cast(batch.actual_lending_price as numeric), 2), 0) * (coalesce(tariff.mark_up_down_per, 0) * 0.01))"
				 + " when tariff.id is null then coalesce(round(cast(batch.actual_lending_price as numeric), 2), 0) end, 0)* coalesce(uommpper.conversions, 1))"
				 + " else   "
				 + " (coalesce (case when tariff.id is not null "
				 + " and tariff.mark_up_down = 1 then coalesce(round(cast(batch.rate as numeric), 2), 0) + (coalesce(round(cast(batch.rate as numeric), 2), 0) * (coalesce(tariff.mark_up_down_per, 0) * 0.01)) "
				 + " when tariff.id is not null and tariff.mark_up_down = 0 then coalesce(round(cast(batch.rate as numeric), 2), 0) - (coalesce(round(cast(batch.rate as numeric), 2), 0) * (coalesce(tariff.mark_up_down_per, 0) * 0.01))"
				 + " when tariff.id is null then coalesce(round(cast(batch.rate as numeric), 2), 0) "
				 + " end ,0)* coalesce(uommpper.conversions, 1))  end ) else (coalesce(round(cast(sd.item_net_amount as numeric), 2), 0))end ) as rate, "
				 + " 	sd.quantity, " 
				 + " 	sd.tax_percentage, " 
				 + " 	config.tax_inclusive " 
				 + " from " 
				 + " 	pharmacy.t_sale_detail sd " 
				 + " inner join pharmacy.t_sale sale on " 
				 + " 	sale.id = sd.sale_id " 
				 + " inner join inventory.t_batch batch on " 
				 + " 	sd.batch_id = batch.id " 
				 + " left join inventory.t_batch_uom_mapper uommpper on " 
				 + " 	sd.uom_unit_id = uommpper.uom_unit_id " 
				 + " 	and sd.uom_type_id = uommpper.uom_type_id " 
				 + " 	and sd.batch_id = uommpper.batch_id " 
				 + " left join pharmacy.t_pharmacy_tariff tariff on " 
				 + " 	tariff.product_category_id = sd.product_category_id " 
				 + " 	and tariff.unit_id = sd.unit_id " 
				 + " 	and tariff.payment_entitlement_id =:paymentEntitlementId " 
				 + " 	and tariff.status = 'A' " 
				 + " inner join public.m_application_configuration config on " 
				 + " 	config.unit_id = sd.unit_id " 
				 + " where " 
				 + " 	sale.id =:saleId " 
				 + " 	and sale.status_id <> 9 ) " 
				 + " select " 
				 + " 	s.id as \"id\", " 
				 + " 	s.organization_id as \"organizationId\", " 
				 + " 	s.unit_id as \"unitId\", " 
				 + " 	s.bill_no as \"billNo\", " 
				 + " 	s.store_id as \"storeId\", " 
				 + " 	s.counter_id as \"counterId\", " 
				 + " 	s.patient_id as \"patientId\", " 
				 + " 	s.sale_type_id as \"saleTypeId\", " 
				 + " 	s.visit_type_id as \"visitTypeId\", " 
				 + " 	s.admission_id as \"admissionId\", " 
				 + " 	s.doctor_id as \"doctorId\", " 
				 + " 	s.status_id as \"statusId\", " 
				 + " 	s.discount_type_id as \"discountTypeId\", " 
				 + " 	s.discount_category_id as \"discountCategoryId\", " 
				 + " 	s.encounter_id as \"encounterId\", " 
				 + " 	s.smart_card_submited as \"smartCard\", " 
				 + " 	s.received_by as \"receivedBy\", " 
				 + " 	to_char(s.received_on, 'YYYY-MM-DD HH24:MI:SS') as \"receivedOn\", " 
				 + " 	to_char(s.added_date, 'YYYY-MM-DD HH24:MI:SS') as \"addedDate\", " 
				 + " 	to_char(s.added_date, 'YYYY-MM-DD HH24:MI:SS') as \"orderDate\", " 
				 + " 	s.is_handover as \"isHandover\", " 
				 + " 	s.handover_by as \"handoverBy\", " 
				 + " 	s.prescription_id as \"prescriptionId\", " 
				 + " 	s.carrier_id as \"carrierId\", " 
				 + " 	s.running_number as \"runningNumber\", " 
				 + " 	s.smart_card_submited as \"smartCardSubmited\", " 
				 + " 	s.claim_send as \"claimSend\", " 
				 + " 	s.tax_inclusive as \"taxInclusive\", " 
				 + " 	s.is_consolidated_bill as \"isConsolidatedBill\", " 
				 + " 	sum(s.rate * s.quantity) as \"totalAmount\", " 
				 + " 	sum(((s.rate * s.quantity)) *(s.tax_percentage * 0.01)) as \"taxAmount\", " 
				 + " 	sum(coalesce(case when s.tax_inclusive = true then (s.rate * s.quantity) end, case when s.tax_inclusive = false then ((s.rate * s.quantity))+((s.rate * s.quantity)) *(s.tax_percentage * 0.01) end, 0)) as \"netAmount\", " 
				 + " 	sum(coalesce(case when s.tax_inclusive = true then (s.rate * s.quantity) end, case when s.tax_inclusive = false then ((s.rate * s.quantity))+((s.rate * s.quantity)) *(s.tax_percentage * 0.01) end, 0)) as \"totalSelfPayable\" " 
				 + " from " 
				 + " 	sale s " 
				 + " group by " 
				 + " 	s.id, " 
				 + " 	s.organization_id , " 
				 + " 	s.unit_id , " 
				 + " 	s.bill_no , " 
				 + " 	s.store_id , " 
				 + " 	s.counter_id , " 
				 + " 	s.patient_id , " 
				 + " 	s.sale_type_id , " 
				 + " 	s.visit_type_id , " 
				 + " 	s.admission_id , " 
				 + " 	s.doctor_id , " 
				 + " 	s.status_id , " 
				 + " 	s.discount_type_id, " 
				 + " 	s.discount_category_id , " 
				 + " 	s.encounter_id , " 
				 + " 	s.smart_card_submited, " 
				 + " 	s.encounter_id , " 
				 + " 	s.received_by , " 
				 + " 	s.received_on , " 
				 + " 	s.is_handover, " 
				 + " 	s.handover_by , " 
				 + " 	s.prescription_id , " 
				 + " 	s.carrier_id , " 
				 + " 	s.running_number , " 
				 + " 	s.smart_card_submited, " 
				 + " 	s.claim_send , " 
				 + " 	s.is_consolidated_bill, " 
				 + " 	s.added_date, " 
				 + " 	s.tax_inclusive " ;
		Query query1 = getSession().createSQLQuery(saleQuery);
		query1.setParameter("saleId", id);
		query1.setParameter("paymentEntitlementId", paymentEntitlementId);
		List<SaleDtoChangeOfPayee> sale = query1
				.setResultTransformer(Transformers.aliasToBean(SaleDtoChangeOfPayee.class)).list();

		SaleDtoChangeOfPayee saleDto = sale.getFirst();
		
		if(flag == true){
			String saleDetailQuery = " with sale as ( " 
					 + " select " 
					 + " 	cast(sd.id as integer), " 
					 + " 	sd.organization_id, " 
					 + " 	sd.unit_id, " 
					 + " 	sd.prescription_detail_id, " 
					 + " 	sd.order_detail_id, " 
					 + " 	cast(sd.sale_id as integer), " 
					 + " 	cast(sd.batch_id as integer), " 
					 + " 	cast(batch.item_id as integer), " 
					 + " 	sd.consumed_item_id, " 
					 + " 	cast(sd.uom_unit_id as integer), "
                     + "    (case when sd.item_net_amount is null or sd.item_net_amount = 0 THEN "
					 + " 	(case when sale.cop is not null and sale.cop = true then (coalesce (CASE WHEN tariff.id IS NOT null AND tariff.mark_up_down = 1  " 
					 + " 		THEN COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) + (COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS NOT null AND tariff.mark_up_down = 0  " 
					 + " 		THEN COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) - (COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS null " 
					 + " 		THEN COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) " 
					 + " 	END,0)* COALESCE(uommpper.conversions,1)) else "
					 + " 	 (coalesce (CASE WHEN tariff.id IS NOT null AND tariff.mark_up_down = 1  " 
					 + " 		THEN COALESCE(round(cast(batch.rate as numeric),2),0) + (COALESCE(round(cast(batch.rate as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS NOT null AND tariff.mark_up_down = 0  " 
					 + " 		THEN COALESCE(round(cast(batch.rate as numeric),2),0) - (COALESCE(round(cast(batch.rate as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS null " 
					 + " 		THEN COALESCE(round(cast(batch.rate as numeric),2),0) " 
					 + " 	END,0)* COALESCE(uommpper.conversions,1)) end ) else (coalesce(round(cast(sd.item_net_amount as numeric), 2), 0))end ) "
					 + " as rate, "
	   				 + " 	sd.quantity, " 
					 + " 	sd.lease_quantity, " 
					 + " 	cast(sd.tax_id as integer), " 
					 + " 	sd.tax_percentage, " 
					 + " 	sd.disallowed_amount, " 
					 + " 	sd.unit_discount_amount, " 
					 + " 	sd.discount_amount, " 
					 + " 	sd.is_active, " 
					 + " 	sd.updated_by, " 
					 + " 	sd.updated_date, " 
					 + " 	sd.added_by, " 
					 + " 	sd.added_date, " 
					 + " 	sd.is_deleted, " 
					 + " 	sd.uom_type_id, " 
					 + " 	sd.status_id, " 
					 + " 	sd.product_category_id, " 
					 + " 	sd.consumed_pcategroy_id, " 
					 + " 	product.asset_category_id, " 
					 + " 	config.tax_inclusive, " 
					 + " 	ubconfig.self_payee_id, " 
					 + " 	sd.discount_percentage, " 
					 + " 	sd.old_discount_percentage " 
					 + " from " 
					 + " 	pharmacy.t_sale_detail sd " 
					 + " inner join pharmacy.t_sale sale on " 
					 + " 	sale.id = sd.sale_id " 
					 + " inner join inventory.t_batch batch on " 
					 + " 	sd.batch_id = batch.id " 
					 + " left join inventory.t_batch_uom_mapper uommpper on " 
					 + " 	sd.uom_unit_id = uommpper.uom_unit_id " 
					 + " 	and sd.uom_type_id = uommpper.uom_type_id " 
					 + " 	and sd.batch_id = uommpper.batch_id " 
					 + " left join pharmacy.t_pharmacy_tariff tariff on " 
					 + " 	tariff.product_category_id = sd.product_category_id " 
					 + " 	and tariff.unit_id = sd.unit_id " 
					 + " 	and tariff.payment_entitlement_id =:paymentEntitlementId " 
					 + " 	and tariff.status = 'A' " 
					 + " inner join public.m_application_configuration config on " 
					 + " 	config.unit_id = sd.unit_id " 
					 + " 	and config.is_active = true "
					 + " 	and config.is_deleted = false "
					 + " inner join public.t_unit_application_billing_configuration ubconfig on "
					 + " 	ubconfig.unit_id = sd.unit_id " 
					 + " 	and ubconfig.status = 'A' " 
					 + " inner join public.m_product_category product on " 
					 + " 	sd.product_category_id = product.id " 
					 + " where " 
					 + " 	sale.id = :saleId " 
					 + " 	and sale.status_id <> 9 ) " 
					 + " select " 
					 + " 	s.id as \"id\", " 
					 + " 	s.organization_id as \"organizationId\", " 
					 + " 	s.unit_id as \"unitId\", " 
					 + " 	s.item_id as \"itemId\", " 
					 + " 	s.consumed_item_id as \"consumedItemId\", " 
					 + " 	s.prescription_detail_id as \"prescriptionDetailId\", " 
					 + " 	s.order_detail_id as \"orderDetailId\", " 
					 + " 	cast(s.sale_id as integer) as \"saleId\", " 
					 + " 	s.batch_id as \"tempBatchId\", " 
					 + " 	s.uom_unit_id as \"uomUnitId\", " 
					 + " 	s.rate as \"rate\", " 
					 + " 	s.quantity as \"quantity\", " 
					 + " 	s.lease_quantity as \"leaseQuantity\", " 
					 + " 	s.tax_id as \"taxId\", " 
					 + " 	s.tax_percentage as \"taxPercentage\", " 
					 + " 	s.disallowed_amount as \"disallowedAmount\", " 
					 + " 	s.unit_discount_amount as \"unitDiscountAmount\", " 
					 + " 	s.discount_percentage as \"discountPercentage\", " 
					 + " 	s.old_discount_percentage as \"oldDiscountPercentage\", " 
					 + " 	s.discount_amount as \"discountAmount\", " 
					 + " 	s.is_active as \"isActive\", " 
					 + " 	s.updated_by as \"updatedBy\", " 
					 + " 	s.added_by as \"addedBy\", " 
					 + " 	s.uom_type_id as \"uomTypeId\", " 
					 + " 	s.status_id as \"statusId\", " 
					 + " 	s.product_category_id as \"productCategoryId\", " 
					 + " 	s.consumed_pcategroy_id as \"consumedPcategroyId\", " 
					 + " 	s.asset_category_id as \"assetCategoryId\", " 
					 + " 	s.self_payee_id as \"payeeId\", " 
					 + " 	s.rate * s.quantity as \"totalAmount\", " 
					 + " 	cast(0 as double precision) as \"creditPayable\", " 
					 + " 	(((s.rate * s.quantity)) *(s.tax_percentage*0.01)) as \"taxAmount\", " 
					 + " 	coalesce(case when s.tax_inclusive = true then (s.rate * s.quantity) end, case when s.tax_inclusive = false then ((s.rate * s.quantity))+((s.rate * s.quantity)) *(s.tax_percentage*0.01) end, 0) as \"netAmount\", " 
					 + " 	coalesce(case when s.tax_inclusive = true then (s.rate * s.quantity) end, case when s.tax_inclusive = false then ((s.rate * s.quantity)) *(s.tax_percentage*0.01) end, 0) as \"selfPayable\" " 
					 + " from " 
					 + " 	sale s " ;
			Query query = getSession().createSQLQuery(saleDetailQuery);
			query.setParameter("saleId", id);
			query.setParameter("paymentEntitlementId", paymentEntitlementId);
			List<SaleDetailsChangeOfPayee> saledDetails = (List<SaleDetailsChangeOfPayee>) query
					.setResultTransformer(Transformers.aliasToBean(SaleDetailsChangeOfPayee.class)).list();

			saleDto.setSaledDetails(saledDetails);
		}else{
			
			String saleDetailQuery = " with sale as ( " 
					 + " select " 
					 + " 	cast(sd.id as integer), " 
					 + " 	sd.organization_id, " 
					 + " 	sd.unit_id, " 
					 + " 	sd.prescription_detail_id, " 
					 + " 	sd.order_detail_id, " 
					 + " 	cast(sd.sale_id as integer), " 
					 + " 	cast(sd.batch_id as integer), " 
					 + " 	cast(batch.item_id as integer), " 
					 + " 	sd.consumed_item_id, " 
					 + " 	cast(sd.uom_unit_id as integer), "
                     + "    (case when sd.item_net_amount is null or sd.item_net_amount = 0 THEN "
					 + " 	(case when sale.cop is not null and sale.cop = true then (coalesce (CASE WHEN tariff.id IS NOT null AND tariff.mark_up_down = 1  " 
					 + " 		THEN COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) + (COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS NOT null AND tariff.mark_up_down = 0  " 
					 + " 		THEN COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) - (COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS null " 
					 + " 		THEN COALESCE(round(cast(batch.actual_lending_price as numeric),2),0) " 
					 + " 	END )) else "
					 + " 	 (coalesce (CASE WHEN tariff.id IS NOT null AND tariff.mark_up_down = 1  " 
					 + " 		THEN COALESCE(round(cast(batch.rate as numeric),2),0) + (COALESCE(round(cast(batch.rate as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS NOT null AND tariff.mark_up_down = 0  " 
					 + " 		THEN COALESCE(round(cast(batch.rate as numeric),2),0) - (COALESCE(round(cast(batch.rate as numeric),2),0) * (coalesce(tariff.mark_up_down_per,0) * 0.01)) " 
					 + " 	WHEN tariff.id IS null " 
					 + " 		THEN COALESCE(round(cast(batch.rate as numeric),2),0) " 
					 + " 	END )) end ) else (coalesce(round(cast(sd.item_net_amount as numeric), 2), 0))end ) "
					 + " as rate, "
	   				 + " 	sd.quantity, " 
					 + " 	sd.lease_quantity, " 
					 + " 	cast(sd.tax_id as integer), " 
					 + " 	sd.tax_percentage, " 
					 + " 	sd.disallowed_amount, " 
					 + " 	sd.unit_discount_amount, " 
					 + " 	sd.discount_amount, " 
					 + " 	sd.is_active, " 
					 + " 	sd.updated_by, " 
					 + " 	sd.updated_date, " 
					 + " 	sd.added_by, " 
					 + " 	sd.added_date, " 
					 + " 	sd.is_deleted, " 
					 + " 	sd.uom_type_id, " 
					 + " 	sd.status_id, " 
					 + " 	sd.product_category_id, " 
					 + " 	sd.consumed_pcategroy_id, " 
					 + " 	product.asset_category_id, " 
					 + " 	config.tax_inclusive, " 
					 + " 	ubconfig.self_payee_id, " 
					 + " 	sd.discount_percentage, " 
					 + " 	sd.old_discount_percentage " 
					 + " from " 
					 + " 	pharmacy.t_sale_detail sd " 
					 + " inner join pharmacy.t_sale sale on " 
					 + " 	sale.id = sd.sale_id " 
					 + " inner join inventory.t_batch batch on " 
					 + " 	sd.batch_id = batch.id " 
					 + " left join inventory.t_batch_uom_mapper uommpper on " 
					 + " 	sd.uom_unit_id = uommpper.uom_unit_id " 
					 + " 	and sd.uom_type_id = uommpper.uom_type_id " 
					 + " 	and sd.batch_id = uommpper.batch_id " 
					 + " left join pharmacy.t_pharmacy_tariff tariff on " 
					 + " 	tariff.product_category_id = sd.product_category_id " 
					 + " 	and tariff.unit_id = sd.unit_id " 
					 + " 	and tariff.payment_entitlement_id =:paymentEntitlementId " 
					 + " 	and tariff.status = 'A' " 
					 + " inner join public.m_application_configuration config on " 
					 + " 	config.unit_id = sd.unit_id " 
					 + " inner join public.t_unit_application_billing_configuration ubconfig on " 
					 + " 	ubconfig.unit_id = sd.unit_id " 
					 + " 	and ubconfig.status = 'A' " 
					 + " inner join public.m_product_category product on " 
					 + " 	sd.product_category_id = product.id " 
					 + " where " 
					 + " 	sale.id = :saleId " 
					 + " 	and sale.status_id <> 9 ) " 
					 + " select " 
					 + " 	s.id as \"id\", " 
					 + " 	s.organization_id as \"organizationId\", " 
					 + " 	s.unit_id as \"unitId\", " 
					 + " 	s.item_id as \"itemId\", " 
					 + " 	s.consumed_item_id as \"consumedItemId\", " 
					 + " 	s.prescription_detail_id as \"prescriptionDetailId\", " 
					 + " 	s.order_detail_id as \"orderDetailId\", " 
					 + " 	cast(s.sale_id as integer) as \"saleId\", " 
					 + " 	s.batch_id as \"tempBatchId\", " 
					 + " 	s.uom_unit_id as \"uomUnitId\", " 
					 + " 	s.rate as \"rate\", " 
					 + " 	s.quantity as \"quantity\", " 
					 + " 	s.lease_quantity as \"leaseQuantity\", " 
					 + " 	s.tax_id as \"taxId\", " 
					 + " 	s.tax_percentage as \"taxPercentage\", " 
					 + " 	s.disallowed_amount as \"disallowedAmount\", " 
					 + " 	s.unit_discount_amount as \"unitDiscountAmount\", " 
					 + " 	s.discount_percentage as \"discountPercentage\", " 
					 + " 	s.old_discount_percentage as \"oldDiscountPercentage\", " 
					 + " 	s.discount_amount as \"discountAmount\", " 
					 + " 	s.is_active as \"isActive\", " 
					 + " 	s.updated_by as \"updatedBy\", " 
					 + " 	s.added_by as \"addedBy\", " 
					 + " 	s.uom_type_id as \"uomTypeId\", " 
					 + " 	s.status_id as \"statusId\", " 
					 + " 	s.product_category_id as \"productCategoryId\", " 
					 + " 	s.consumed_pcategroy_id as \"consumedPcategroyId\", " 
					 + " 	s.asset_category_id as \"assetCategoryId\", " 
					 + " 	s.self_payee_id as \"payeeId\", " 
					 + " 	s.rate * s.quantity as \"totalAmount\", " 
					 + " 	cast(0 as double precision) as \"creditPayable\", " 
					 + " 	(((s.rate * s.quantity)) *(s.tax_percentage*0.01)) as \"taxAmount\", " 
					 + " 	coalesce(case when s.tax_inclusive = true then (s.rate * s.quantity) end, case when s.tax_inclusive = false then ((s.rate * s.quantity))+((s.rate * s.quantity)) *(s.tax_percentage*0.01) end, 0) as \"netAmount\", " 
					 + " 	coalesce(case when s.tax_inclusive = true then (s.rate * s.quantity) end, case when s.tax_inclusive = false then ((s.rate * s.quantity)) *(s.tax_percentage*0.01) end, 0) as \"selfPayable\" " 
					 + " from " 
					 + " 	sale s " ;
			Query query = getSession().createSQLQuery(saleDetailQuery);
			query.setParameter("saleId", id);
			query.setParameter("paymentEntitlementId", paymentEntitlementId);
			List<SaleDetailsChangeOfPayee> saledDetails = (List<SaleDetailsChangeOfPayee>) query
					.setResultTransformer(Transformers.aliasToBean(SaleDetailsChangeOfPayee.class)).list();

			saleDto.setSaledDetails(saledDetails);
			
		}
		
		
		return saleDto;

	}

	@Override
	public Sale updateSaleBatchUpdate(Sale sale) {
		Connection connection = null;
		List<SaleDetailPayeeMapper> finalPayeeListForSave = new LinkedList<SaleDetailPayeeMapper>();
		List<SaleDetailPackageMapper> finalPackageMprListForSave = new LinkedList<SaleDetailPackageMapper>();
		PreparedStatement saleDetailStatement = null;
		PreparedStatement saleStatement = null;
		try {
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);

			// Sale Statement - PreparedStatement with parameters
			String saleUpdateSql = "UPDATE pharmacy.t_sale SET "
					+ "status_id = ?, total_amount = ?, discount_type_id = ?, discount_category_id = ?, "
					+ "discount = ?, total_discount_amount = ?, net_amount = ?, tax_amount = ?, "
					+ "outstanding_amount = ?, paid_amount = ?, round_off = ?, updated_by = ?, updated_date = ?, "
					+ "total_self_payable = ?, total_credit_payable = ?, bill_id = ?, concession = ? "
					+ "WHERE id = ?";
			 saleStatement = connection.prepareStatement(saleUpdateSql);
			saleStatement.setInt(1, sale.getStatus().getId());
			saleStatement.setBigDecimal(2, BigDecimal.valueOf(sale.getTotalAmount()));
			saleStatement.setInt(3, sale.getDiscountType().getId());
			saleStatement.setInt(4, sale.getDiscountCategory().getId());
			saleStatement.setBigDecimal(5, BigDecimal.valueOf(sale.getDiscount()));
			saleStatement.setBigDecimal(6, BigDecimal.valueOf(sale.getTotalDiscountAmount()));
			saleStatement.setBigDecimal(7, BigDecimal.valueOf(sale.getNetAmount()));
			saleStatement.setBigDecimal(8, BigDecimal.valueOf(sale.getTaxAmount()));
			saleStatement.setBigDecimal(9, BigDecimal.valueOf(sale.getOutstandingAmount()));
			saleStatement.setBigDecimal(10, BigDecimal.valueOf(sale.getPaidAmount()));
			saleStatement.setBigDecimal(11, BigDecimal.valueOf(sale.getRoundOff()));
			saleStatement.setInt(12, sale.getUpdatedByUser());
			saleStatement.setString(13, GlobalCommonDateUtils.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			saleStatement.setBigDecimal(14, BigDecimal.valueOf(sale.getTotalSelfPayable()));
			saleStatement.setBigDecimal(15, BigDecimal.valueOf(sale.getTotalCreditPayable()));
			saleStatement.setInt(16, sale.getBillId());
			saleStatement.setBigDecimal(17, BigDecimal.valueOf(sale.getConcession()));
			saleStatement.setInt(18, Math.toIntExact(sale.getId()));

			Statement statement = connection.createStatement();
			int i = 0;

			for (SaleDetail detail : sale.getSaleDetailsList()) {
				// Sale Detail Statement - PreparedStatement with parameters
				String saleDetailUpdateSql = "UPDATE pharmacy.t_sale_detail SET "
						+ "order_detail_id = ?, sale_id = ?, batch_id = ?, uom_unit_id = ?, rate = ?, quantity = ?, "
						+ "lease_quantity = ?, tax_id = ?, tax_percentage = ?, tax_amount = ?, total_amount = ?, "
						+ "discount_amount = ?, unit_discount_amount = ?, net_amount = ?, updated_by = ?, updated_date = ?, "
						+ "uom_type_id = ?, status_id = ?, credit_payable = ?, self_payable = ?, payee_id = ?, "
						+ "product_category_id = ?, package_consumption_amt = ?, consession_percentage = ?, consession_amount = ?, "
						+ "unit_consession_amount = ?, consumed_item_id = ?, consumed_pcategroy_id = ?, discount_percentage = ? "
						+ "WHERE id = ?";
				 saleDetailStatement = connection.prepareStatement(saleDetailUpdateSql);
				saleDetailStatement.setLong(1, detail.getOrderDetailId());
				saleDetailStatement.setLong(2, detail.getSale().getId());
				saleDetailStatement.setLong(3, detail.getBatch().getId());
				saleDetailStatement.setInt(4, detail.getUomUnit().getId());
				saleDetailStatement.setBigDecimal(5, BigDecimal.valueOf(detail.getRate()));
				saleDetailStatement.setBigDecimal(6, BigDecimal.valueOf(detail.getQuantity()));
				saleDetailStatement.setBigDecimal(7, BigDecimal.valueOf(detail.getLeaseQuantity()));
				saleDetailStatement.setInt(8, detail.getTax().getId());
				saleDetailStatement.setBigDecimal(9, BigDecimal.valueOf(detail.getTaxPercentage()));
				saleDetailStatement.setBigDecimal(10, BigDecimal.valueOf(detail.getTaxAmount()));
				saleDetailStatement.setBigDecimal(11, BigDecimal.valueOf(detail.getTotalAmount()));
				saleDetailStatement.setBigDecimal(12, BigDecimal.valueOf(detail.getDiscountAmount()));
				saleDetailStatement.setBigDecimal(13, BigDecimal.valueOf(detail.getUnitDiscountAmount()));
				saleDetailStatement.setBigDecimal(14, BigDecimal.valueOf(detail.getNetAmount()));
				saleDetailStatement.setInt(15, sale.getUpdatedByUser());
				saleDetailStatement.setString(16, GlobalCommonDateUtils.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				saleDetailStatement.setInt(17, detail.getUomType().getId());
				saleDetailStatement.setInt(18, detail.getStatus().getId());
				saleDetailStatement.setBigDecimal(19, BigDecimal.valueOf(detail.getCreditPayable()));
				saleDetailStatement.setBigDecimal(20, BigDecimal.valueOf(detail.getSelfPayable()));
				saleDetailStatement.setInt(21, detail.getPayeeId());
				saleDetailStatement.setInt(22, detail.getProductCategoryId());
				saleDetailStatement.setBigDecimal(23, BigDecimal.valueOf(detail.getPackageConsumptionAmt()));
				saleDetailStatement.setBigDecimal(24, BigDecimal.valueOf(detail.getConsessionPercentage()));
				saleDetailStatement.setBigDecimal(25, BigDecimal.valueOf(detail.getConsessionAmount()));
				saleDetailStatement.setBigDecimal(26, BigDecimal.valueOf(detail.getUnitConsessionAmount()));
				saleDetailStatement.setInt(27, detail.getConsumedItemId());
				saleDetailStatement.setInt(28, detail.getConsumedPcategroyId());
				saleDetailStatement.setBigDecimal(29, BigDecimal.valueOf(detail.getDiscountPercentage()));
				saleDetailStatement.setInt(30, Math.toIntExact(detail.getId()));

				statement.addBatch(saleDetailStatement.toString());
				for (SaleDetailPayeeMapper payee : detail.getSaleDetailPayeeMapperList()) {
					finalPayeeListForSave.add(payee);
				}
				for (SaleDetailPackageMapper packageMpr : detail.getSaleDetailPackageMapperList()) {
					finalPackageMprListForSave.add(packageMpr);
				}
				i++;
			}
			if (i == sale.getSaleDetailsList().size()) {
				try {
					saleStatement.executeUpdate();  // Execute sale update
					statement.executeBatch();       // Execute sale detail batch update
					connection.commit();            // Commit the transaction
				} catch (BatchUpdateException e) {
					throw e.getNextException();
				}
			}

		} catch (Exception e) {
		logger.error("updateSaleBatchUpdate failed", e);
		} finally {
			try {
				if (saleDetailStatement != null) {
					saleDetailStatement.close();
				}
				if (saleStatement != null) {
					saleStatement.close();
				}
				connection.setAutoCommit(true);
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (Exception e) {
			logger.error("updateSaleBatchUpdate failed", e);
			}
		}
		executeBatchForMappers(finalPayeeListForSave, finalPackageMprListForSave);
		return sale;
	}

	@Override
	public Sale updateSale(Sale sale) {
		sessionFactory.getCurrentSession().update(sale);
		return sale;

	}

	public List<PMSFrequency> getFrequencyList(List<PMSFrequency> frequency) throws Exception {
		String hql = "SELECT PMSFrequency FROM PMSFrequency AS PMSFrequency "
				+ "WHERE PMSFrequency.isActive = true AND PMSFrequency.isDeleted = false AND PMSFrequency.organization.organizationId = :organizationId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public Long getStoreStockByItemForAssignPicker(Integer storeId, Integer itemId) {

		String hql = "SELECT COALESCE(SUM(storeStock.availableQty), 0) FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH  batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true"
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "WHERE store.storeId = :storeId AND item.id = :itemId AND"
				+ "	storeStock.isDeleted = false AND storeStock.isActive = true	 AND storeStock.availableQty > 0 ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public List<Vendor> getAllVendorList(Integer unitId,String vendorName) {

		if(vendorName!=null && vendorName!="" && !vendorName.equalsIgnoreCase("noSelectedVendorFound"))
		{
			String hql = "SELECT vendor FROM Vendor as vendor "
					+ " WHERE  vendor.isActive = true AND vendor.isDeleted = false and vendor.unit.unitId=:unitId "
					+ " and (lower(vendor.name) LIKE :vendorName OR lower(vendor.code) LIKE :vendorName)";
			Query query = getSession().createQuery(hql);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("vendorName", vendorName.toLowerCase()+'%');
			return query.list();
		}
		else
		{
		String hql = "SELECT vendor FROM Vendor as vendor "
				+ " WHERE  vendor.isActive = true AND vendor.isDeleted = false and vendor.unit.unitId=:unitId";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return query.list();
		}

	}

	@Override
	public String doctorSpeciality(Integer organizationId, Integer unitId) {
		String sql = "select spec.speciality_name as speciality_name "
				+ " from doctor.m_speciality_master spec where speciality_id = "
				+ " (select er_speciality_id from t_unit_application_billing_configuration "
				+ " where organization_id = :organizationId and unit_id = :unitId)";

		Query query = getSession().createSQLQuery(sql).addScalar("speciality_name");
		query.setParameter("organizationId", organizationId);
		query.setParameter("unitId", unitId);

		return (query.uniqueResult() != null ? (String) query.uniqueResult() : null);
	}

	public List<ReasonMaster> getreasonDropdownList(Integer orgId) {

		String hql = "SELECT reasonMaster FROM ReasonMaster AS reasonMaster " + "WHERE reasonMaster.status = 'A' "
				+ "and reasonTypeId =:reasonTypeId " + "and organizationId=:orgId";

		Query query = getSession().createQuery(hql);
		query.setParameter("reasonTypeId", 14);
		query.setParameter("orgId", orgId);
		return query.list();

	}

	@Override
	public List<PatientSearchDetails> getOPPatients(String name, Integer unitId) {

		/*
		 * String hql = "SELECT patient FROM EncounterMaster AS encounterMaster " +
		 * " INNER JOIN encounterMaster.patientRegistration AS patient " +
		 * " WHERE encounterMaster.statusId = '5' AND patient.status = 'A' AND patient.unitId = :unitId  "
		 * +
		 * " AND ((:name = '' OR lower(patient.firstName) LIKE :name) OR (:name = '' OR lower(patient.middleName) LIKE :name) "
		 * + " OR (:name = '' OR lower(patient.lastName) LIKE :name)) ";
		 */
		String sql = " select distinct pat.uhid_number as uhidNumber ,\n"
				+ "pre.prefix_code as prefixCode,  pat.first_name as firstName ,pat.middle_name as middleName, \n"
				+ "pat.last_name as lastName, pat.dob as dob,\n"
				+ "gen.gender_code as genderCode, pat.mobile_number as mobileNumber, pat.patient_id as patientId\n"
				+ "from opd.t_encounter_master enc\n"
				+ "inner join patient.t_patient_registration pat on pat.patient_id = enc.patient_id\n"
				+ "inner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id \n"
				+ "inner join public.m_gender_master gen on pat.gender_id = gen.gender_id\n"
				+ " where enc.status_id in (8,4,2,18,9,5,6) and pat.status ='A' and pat.unit_id =:unitId " +
				// " and enc.visit_type_id =:visitType \n" +
				"\t and (:name= ''\n" + "\tor lower(pat.first_name) like :name\n" + "\tor :name= ''\n"
				+ "\tor lower(pat.middle_name) like :name\n" + "\tor :name= ''\n"
				+ "\tor lower(pat.last_name) like :name)";

		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		// query.setParameter("visitType", visitType);
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();
	}

	@Override
	public List<PatientSearchDetails> getIPDCERPatients(String name, Integer visitType, Integer unitId) {

		String sql = "SELECT DISTINCT pat.uhid_number   AS uhidNumber, "
				+ "                pre.prefix_code   AS prefixCode, "
				+ "                pat.first_name    AS firstName, "
				+ "                pat.middle_name   AS middleName, "
				+ "                pat.last_name     AS lastName, " + "                pat.dob           AS dob, "
				+ "                gen.gender_code   AS genderCode, "
				+ "                pat.mobile_number AS mobileNumber, "
				+ "                pat.patient_id    AS patientId " + "	FROM   adt.t_admission adm "
				+ "       INNER JOIN patient.t_patient_registration pat "
				+ "               ON pat.patient_id = adm.patient_id " + "       LEFT JOIN adt.t_discharge dis "
				+ "              ON adm.admission_id = dis.admission_id "
				+ "       INNER JOIN PUBLIC.m_prefix_master pre " + "               ON pre.prefix_id = pat.prefix_id "
				+ "       INNER JOIN PUBLIC.m_gender_master gen " + "               ON pat.gender_id = gen.gender_id "
				+ "WHERE  (dis.discharge_status_id <> '5' or dis.discharge_status_id is null) "
				+ "       AND pat.status = 'A' " + "       AND adm.visit_type_id = :visitType "
				+ "       AND adm.unit_id =:unitId " + "       AND ( Lower(pat.first_name) LIKE :name "
				+ "              OR Lower(pat.middle_name) LIKE :name "
				+ "              OR Lower(pat.last_name) LIKE :name )";

		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("visitType", visitType);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();
	}

	@Override
	public Prescription getPrescription(Integer prescriptionId) {
		String hql = "select p from Prescription p "
				+ " INNER JOIN p.prescriptionDetailsList prescriptionDetails WITH prescriptionDetails.isDeleted = false AND prescriptionDetails.isActive = true "
				+ " WHERE p.isDeleted = false and p.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());
		return (Prescription) query.uniqueResult();
	}

	@Override
	public List<IssueDetail> getIssueDetailsByPickerDetail(Integer assignPickerDetailId) {
		String hql = " SELECT issueDetail from IssueDetail issueDetail "
				+ " INNER JOIN issueDetail.assignPickerDetail pickerDetail WITH pickerDetail.isDeleted = false "
				+ " and pickerDetail.isActive = true " + " WHERE pickerDetail.id = :assignPickerDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assignPickerDetailId", assignPickerDetailId);

		return query.list();
	}

	@Override
	public BigInteger getTotalIssuedQty(Integer patientId, Integer id) {
		String sql = " select sum(issue_detail.issue_quantity) as totalissuedqty from pharmacy.t_prescription_detail pre_detail JOIN pharmacy.t_prescription pres ON pre_detail.prescription_id=pres.id  join  pharmacy.t_issue_detail issue_detail on pre_detail.id=issue_detail.prescription_detail_id where pres.patient_id="
				+ patientId + " and pre_detail.item_id=" + id;
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}

	/*
	 * @Override public BigInteger getConsumedQty(Integer patientId, Integer id ,
	 * Long batchId) { String s =
	 * " select count(drug_detail.id) from pharmacy.t_drug_administration_detail drug_detail  join pharmacy.t_drug_administration drug on drug_detail.drug_administration_id= drug.id join pharmacy.t_prescription_detail predetails on drug_detail.prescription_detail_id=predetails.id where drug.patient_id = "
	 * + patientId + " and predetails.item_id= " + id; if (batchId.equals(0)) {
	 * 
	 * } else { s = s + " AND t_drug_administration_detail.batch_id=" + batchId;
	 * 
	 * }
	 * 
	 * Query query = getSession().createSQLQuery(s); return (BigInteger)
	 * query.uniqueResult();
	 * 
	 * }
	 */

	@Override
	public Integer getReturnQty(Integer admissionId, Long prescriptionDetailId) {

		// String sql = " select issue_quantity as returnqty from
		// pharmacy.t_issue_detail issuedetail join pharmacy.t_issue issue on
		// issuedetail.issue_id=issue.id where
		// issuedetail.prescription_detail_id=:prescriptionDetailId and
		// issue.admission_id=:admissionId ";

		String sql = "  select rd.return_qty from " + " pharmacy.t_patient_issue_return_detail rd "
				+ " join pharmacy.t_issue_detail_batch_mapper bm on rd.issue_batch_mapper_id=bm.id "
				+ " join pharmacy.t_issue_detail id on  bm.issue_detail_id=id.id "
				+ " join pharmacy.t_prescription_detail pd  on id.prescription_detail_id=pd.id "
				+ " join  pharmacy.t_patient_issue_return r on rd.patient_issue_return_id=r.id "
				+ " where r.admission_id=:admissionId and pd.id=:prescriptionDetailId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("prescriptionDetailId", prescriptionDetailId);
		query.setParameter("admissionId", admissionId);
		Integer returnQty = 0;
		if (query.list().size() > 0) {
			if (query.list().getFirst() != null) {
				returnQty = (Integer) query.list().getFirst();
			}

		}

		return returnQty;
	}

	@Override
	public List<PatientSearchDetails> billListPatientSearch(String name, Integer unitId) {

		String sql = " select distinct pat.uhid_number as uhidNumber ,\n"
				+ " pre.prefix_code as prefixCode,  pat.first_name as firstName ,pat.middle_name as middleName,\n"
				+ " pat.last_name as lastName, pat.dob as dob,\n"
				+ " gen.gender_code as genderCode, pat.mobile_number as mobileNumber, sale0_.patient_id as patientId\n"
				+ " from pharmacy.t_sale sale0_ \n"
				+ "\tinner join patient.t_patient_registration pat on sale0_.patient_id = pat.patient_id \n"
				+ "\tinner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id\n"
				+ "\tinner join public.m_gender_master gen on pat.gender_id = gen.gender_id\n" + "\t where \n"
				+ "\t    sale0_.is_active = true and pat.status ='A' and sale0_.is_deleted = false\n"
				+ "\tand sale0_.unit_id =:unitId and sale0_.status_id = "
				+ com.param.common.constants.Status.Completed.getStatus() + ""
				+ " and sale0_.sale_type_id in (2,3) and pat.status ='A' " +
				// " and enc.visit_type_id =:visitType \n" +
				"\t and (:name= ''\n" + "\tor lower(pat.first_name) like :name\n" + "\tor :name= ''\n"
				+ "\tor lower(pat.middle_name) like :name\n" + "\tor :name= ''\n"
				+ "\tor lower(pat.last_name) like :name)";

		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		// query.setParameter("visitType", visitType);
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();
	}

	@Override
	public List<PatientSearchDetails> getIPDCERPatientsIssueReturn(String name, Integer visitType, Integer unitId) {

		String sql = "select " + "	distinct pat.uhid_number as uhidNumber , " + "	pre.prefix_code as prefixCode, "
				+ "	pat.first_name as firstName , " + "	pat.middle_name as middleName, "
				+ "	pat.last_name as lastName, " + "	pat.dob as dob, " + "	gen.gender_code as genderCode, "
				+ "	pat.mobile_number as mobileNumber, " + "	pat.patient_id as patientId " + "from "
				+ "	adt.t_admission adm " + "inner join patient.t_patient_registration pat on "
				+ "	pat.patient_id = adm.patient_id " + "inner join public.m_prefix_master pre on "
				+ "	pre.prefix_id = pat.prefix_id " + "inner join public.m_gender_master gen on "
				+ "	pat.gender_id = gen.gender_id " + "left join adt.t_discharge dis on "
				+ "	adm.admission_id = dis.admission_id " + "	and dis.discharge_status_id not in (2,5) " + "where "
				+ "	pat.status = 'A' " + "	and adm.visit_type_id =:visitType " + "	and pat.unit_id =:unitId "
				+ "	and (lower(pat.first_name) like :name or lower(pat.middle_name) like :name 	or lower(pat.last_name) like :name)";
		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("visitType", visitType);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();
	}

	@Override
	public List<PatientSearchDetails> getIPDCERPatientsPatientIndent(String name, Integer visitType, Integer unitId) {

		String sql = """
				 select distinct pat.uhid_number as uhidNumber ,
				pre.prefix_code as prefixCode,  pat.first_name as firstName ,pat.middle_name as middleName,\s
				pat.last_name as lastName, pat.dob as dob,
				gen.gender_code as genderCode, pat.mobile_number as mobileNumber, pat.patient_id as patientId
				from adt.t_admission adm
				inner join patient.t_patient_registration pat on pat.patient_id = adm.patient_id
				left join adt.t_discharge dis on adm.admission_id=dis.admission_id
				inner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id\s
				inner join public.m_gender_master gen on pat.gender_id = gen.gender_id
				where (dis.discharge_status_id not in(\
				\t\t1,\
				\t\t2,\
				\t\t3,\
				\t\t5\
				\t) or dis.discharge_id is null) and pat.status = 'A'
				 and adm.visit_type_id =:visitType and adm.unit_id =:unitId \
				\tand (:name= ''
				\tor lower(pat.first_name) like :name
				\tor :name= ''
				\tor lower(pat.middle_name) like :name
				\tor :name= ''
				\tor lower(pat.last_name) like :name)""";

		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("visitType", visitType);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();
	}

	@Override
	public Batch getBatchByBatchLotNo(String lotNo) {
		String hql = " SELECT batch FROM Batch as batch  "
				+ " WHERE  batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false "
				+ "	AND batch.lotNo = :lotNo ";

		Query query = getSession().createQuery(hql);
		query.setParameter("lotNo", lotNo);

		return (Batch) query.uniqueResult();
	}

	@Override
	public List<BatchResponse> getBatchListByItemId(Long itemId, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			String sql = "select  batch.id as batchId,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.expiry_date as expiry, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice,  batch.markup as markup, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment,storestock2.available_qty AS availableStockQuantity,batch.tax_id AS taxId,batch.tax_percentage as taxPercentage "
					+ "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id AND (item1.is_deleted=false AND item1.is_active=true) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id "
					+ " where item1.id= :id AND batch.is_deleted=false "
					+ "AND batch.is_active=true AND batch.unit_id=:unitId AND batch.item_id= :id  "
					+ "AND (batch.is_pharmacy=false or batch.is_indent=false) AND batch.is_return=false AND batch.is_quarantine=false "
					+ "AND storestock2.store_id= :storeId   and storestock2.available_qty is not null and storestock2.total_qty is not null "
					+ "AND (batch.expiry_date >= :greaterThanCurrentMonth or batch.expiry_date is null) order by batch.expiry_date  asc ";

			SQLQuery query = getSession().createSQLQuery(sql).addScalar("batchId", StandardBasicTypes.LONG)
					.addScalar("batchNo", StandardBasicTypes.STRING).addScalar("lotNo", StandardBasicTypes.STRING)
					.addScalar("quantity", StandardBasicTypes.INTEGER)
					.addScalar("currentQuantity", StandardBasicTypes.INTEGER)
					.addScalar("expiryDate", StandardBasicTypes.DATE).addScalar("expiry", StandardBasicTypes.DATE)
					.addScalar("cop", StandardBasicTypes.DOUBLE).addScalar("rate", StandardBasicTypes.DOUBLE)
					.addScalar("sellingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("lendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("actualLendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("yearOfManufacture", StandardBasicTypes.INTEGER)
					.addScalar("rejectedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("returnedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN)
					.addScalar("availableStockQuantity", StandardBasicTypes.INTEGER)
					.addScalar("taxId", StandardBasicTypes.INTEGER)
					.addScalar("taxPercentage", StandardBasicTypes.DOUBLE);
			query.setParameter("id", itemId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", storeId != null ? storeId : sessionService.getStoreId());
			query.setParameter("greaterThanCurrentMonth", current);
			query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));
			return query.list();
		} catch (Exception e) {
			
			throw new RuntimeException("Error while fetching List by item id");
		}

	}

	@Override
	public List<Batch> getBatchListByItemIdForBillableConsumption(Long itemId, Integer storeId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			String sql = "select  batch.id as id,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment " + "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id AND (item1.is_deleted=false AND item1.is_active=true AND item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id AND batch.is_deleted=false "
					+ "AND batch.is_active=true AND batch.unit_id=:unitId AND batch.item_id= :id  "
					+ "  AND batch.is_quarantine=false "
					+ "AND storestock2.store_id= :storeId AND storestock2.available_qty > 0 "
					+ "AND (batch.expiry_date > :greaterThanCurrentMonth or batch.expiry_date is null )  order by batch.current_quantity ASC";

			SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG)
					.addScalar("batchNo", StandardBasicTypes.STRING).addScalar("lotNo", StandardBasicTypes.STRING)
					.addScalar("quantity", StandardBasicTypes.INTEGER)
					.addScalar("currentQuantity", StandardBasicTypes.INTEGER)
					.addScalar("expiryDate", StandardBasicTypes.DATE).addScalar("cop", StandardBasicTypes.DOUBLE)
					.addScalar("rate", StandardBasicTypes.DOUBLE).addScalar("sellingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("lendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("actualLendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("yearOfManufacture", StandardBasicTypes.INTEGER)
					.addScalar("rejectedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("returnedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN);
			query.setParameter("id", itemId);
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("storeId", storeId);
			query.setParameter("greaterThanCurrentMonth", current);
			query.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return query.list();
		} catch (Exception e) {
			
			throw new RuntimeException("Error while fetching List by item id");
		}

	}

	@Override
	public List<Batch> getConsignmentBatchDetails(String batchNo, long batchId, Integer storeId,
			BatchProperty batchProperty) {

		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		// System.out.println("Store Id : " + storeId);
		// System.out.println("Session Store Id : " + sessionService.getStoreId());

		// //System.out.println("Unit Id : " + unitId);
		// System.out.println("Session Unit Id : " + sessionService.getUnitId());

		String hql = "SELECT batch  FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.currentQuantity > 0 AND batch.unit.unitId = :unitId "
				+ " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		// int unId = unitId == 0 ? sessionService.getUnitId() : unitId;

		// query.setParameter("id", id);
		// query.setParameter("unitId", unId);
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("isPharmacy",
				batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
		query.setParameter("isIndent", batchProperty.getIsIndent() == null || batchProperty.getIsIndent() ? 0 : 1);
		query.setParameter("isReturn", batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
		return query.list();

	}

	public Long getMaxOrderQuantity(Integer maxStoreQuantity, Long storeStock, Long stockInTransit,
			Long approvedPRAndPOQuantity, Long approvedPOQuantity) {
		Long maxOrderQty = maxStoreQuantity
				- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
		if (maxOrderQty < 0)
			return 0L;
		return maxOrderQty;
	}

	@Override
	public PrefixRunningNumber getPrefixLatestNumber(Integer storeId, Integer unitId, Integer screenId) {

		List<PrefixRunningNumber> list = null;
		String hql = "SELECT prefixRunningNumber FROM PrefixRunningNumber AS prefixRunningNumber "
				+ " INNER JOIN prefixRunningNumber.screen AS screen WITH screen.isActive = true and screen.isGlobal = false and screen.isDeleted = false and screen.id = :screenId"
				+ " INNER JOIN prefixRunningNumber.unit AS unit WITH unit.id = :unitId "
				+ " INNER JOIN FETCH prefixRunningNumber.store AS store "
				+ " WHERE (store.storeId = :storeId OR :storeId = 0)"
				+ " AND store.isActive = true AND store.isDeleted = false "
				+ " AND prefixRunningNumber.isActive = true " + " AND prefixRunningNumber.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", unitId);
		query.setParameter("storeId", storeId != null ? storeId : 0);
		list = query.list();
		return list != null ? (PrefixRunningNumber) list.getFirst() : null;
	}

	@Override
	public String getDefaultPrefix(Integer storeId, Integer unitId, Integer screenId) {
		String sql = "select prefix.default_prefix as defaultPrefix " + " from public.m_screen_prefix prefix "
				+ " where prefix.screen_id = :screenId and prefix.unit_id = :unitId ";

		Query query = getSession().createSQLQuery(sql).addScalar("defaultPrefix");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", unitId);

		return (query.uniqueResult() != null ? (String) query.uniqueResult() : null);
	}

	@Override
	public PatientConsession getPatientConsessionAdmission(Integer itemId, Integer admissionId,
			Integer patientCategoryId, Integer assetCategoryId, Integer prodCatId) {
		String sql = " select " + " pc1.percentage as \"patientClassPercentage\", "
				+ " pc1.markup_status as \"markupStatus\", " + " 1 as \"priorityId\", "
				+ " pc1.patient_type_group_id as \"id\" " + "from " + " adt.t_admission admission "
				+ "inner join m_item item on " + " item.id =:itemId "
				+ "left join billing.m_patient_type_group_wise_details pc1 on " + " pc1.item_id = item.id "
				+ " and pc1.patient_type_id = admission.patient_class_id " + "where "
				+ " admission.admission_id = :admissionId " + " and pc1.percentage is not null "
				+ " and pc1.visit_type_id = admission.visit_type_id " + " and pc1.payment_entitlement_id = ( "
				+ " select " + " payment_entitlement_id " + " from " + " opd.t_patient_sponsor_details tpsd "
				+ " where " + " admission_id =:admissionId " + " and status = 'A' " + " and priority_id = 1 "
				+ " limit 1) " + " and pc1.patient_category_id =:patientCategoryId " + " and pc1.status = 'A' "
				+ " union " + "select " + " pc1.percentage as \"patientClassPercentage\", "
				+ " pc1.markup_status as \"markupStatus\", " + " 2 as \"priorityId\", "
				+ " pc1.patient_type_group_id as \"id\" " + "from " + " adt.t_admission admission "
				+ "inner join m_product_category pcat on " + " pcat.id =:productCategoryId "
				+ "left join billing.m_patient_type_group_wise_details pc1 on " + " pc1.product_category_id = pcat.id "
				+ " and pc1.patient_type_id = admission.patient_class_id " + "where "
				+ " admission.admission_id = :admissionId " + " and pc1.item_id is null "
				+ " and pc1.percentage is not null " + " and pc1.visit_type_id = admission.visit_type_id "
				+ " and pc1.payment_entitlement_id =( " + " select " + " payment_entitlement_id " + " from "
				+ " opd.t_patient_sponsor_details tpsd " + " where " + " admission_id =:admissionId "
				+ " and status = 'A' " + " and priority_id = 1 " + " limit 1) "
				+ " and pc1.patient_category_id =:patientCategoryId " + " and pc1.status = 'A' " + " union " + "select "
				+ " pc3.percentage as \"patientClassPercentage\", " + " pc3.markup_status as \"markupStatus\", "
				+ " 3 as \"priorityId\", " + " pc3.patient_type_group_id as \"id\" " + "from "
				+ " adt.t_admission admission " + "inner join m_asset_category asset on "
				+ " asset.id =:assetCategoryId " + "left join billing.m_patient_type_group_wise_details pc3 on "
				+ " pc3.asset_category_id = asset.id " + " and pc3.patient_type_id = admission.patient_class_id "
				+ "where " + " admission.admission_id = :admissionId " + " and pc3.sub_speciality_id is null "
				+ " and pc3.item_id is null " + " and pc3.product_category_id is not null "
				+ " and pc3.payment_entitlement_id =( " + " select " + " payment_entitlement_id " + " from "
				+ " opd.t_patient_sponsor_details tpsd " + " where " + " admission_id =:admissionId "
				+ " and status = 'A' " + " and priority_id = 1 " + " limit 1) "
				+ " and pc3.patient_category_id =:patientCategoryId "
				+ " and pc3.visit_type_id = admission.visit_type_id " + " and pc3.status = 'A' " + " union " + "select "
				+ " 0 as \"patientClassPercentage\", " + " false as \"markupStatus\", " + " 4 as \"priorityId\" , "
				+ " 0 as \"id\" " + " order by " + " \"priorityId\", " + " \"id\" desc " + "limit 1";

		PatientConsession patientConsession = (PatientConsession) getSession().createSQLQuery(sql)
				.setParameter("itemId", itemId).setParameter("admissionId", admissionId)
				.setParameter("patientCategoryId", patientCategoryId).setParameter("assetCategoryId", assetCategoryId)
				.setParameter("productCategoryId", prodCatId)
				.setResultTransformer(Transformers.aliasToBean(PatientConsession.class)).uniqueResult();
		return (patientConsession != null ? patientConsession : null);
	}

	@Override
	public PatientConsession getPatientConsessionEncounter(Integer itemId, Integer encounterId,
			Integer patientCategoryId, Integer assetCategoryId, Integer prodCatId, Integer patientClassId,
			Integer entitlementId) {
		PatientConsession patientConsession = null;
		if (encounterId == null && patientClassId != null) {
			String sql = " select " + " pc1.percentage as \"patientClassPercentage\", "
					+ " pc1.markup_status as \"markupStatus\", " + " 1 as \"priorityId\", "
					+ " pc1.patient_type_group_id as \"id\" " + " from "
					+ " billing.m_patient_type_group_wise_details pc1 " + " where " + " pc1.percentage is not null "
					+ " and pc1.visit_type_id = 1 " + " and pc1.payment_entitlement_id =:paymentEntitlementId "
					+ " and pc1.status = 'A' " + " and pc1.patient_type_id = :patientClassId "
					+ " and pc1.item_id =:itemId " + " and pc1.patient_category_id =:patientCategoryId " + " union "
					+ " select " + " pc1.percentage as \"patientClassPercentage\", "
					+ " pc1.markup_status as \"markupStatus\", " + " 2 as \"priorityId\", "
					+ " pc1.patient_type_group_id as \"id\" " + " from "
					+ " billing.m_patient_type_group_wise_details pc1 " + " where " + " pc1.item_id is null "
					+ " and pc1.percentage is not null " + " and pc1.visit_type_id = 1 "
					+ " and pc1.payment_entitlement_id =:paymentEntitlementId " + " and pc1.status = 'A' "
					+ " and pc1.product_category_id = :productCategoryId "
					+ " and pc1.patient_type_id = :patientClassId " + " union " + " select "
					+ " pc3.percentage as \"patientClassPercentage\", " + " pc3.markup_status as \"markupStatus\", "
					+ " 3 as \"priorityId\", " + " pc3.patient_type_group_id as \"id\" " + " from "
					+ " billing.m_patient_type_group_wise_details pc3 " + " where " + " pc3.sub_speciality_id is null "
					+ " and pc3.item_id is null " + " and pc3.product_category_id is not null "
					+ " and pc3.payment_entitlement_id =:paymentEntitlementId "
					+ " and pc3.patient_category_id =:patientCategoryId " + " and pc3.visit_type_id = 1 "
					+ " and pc3.status = 'A' " + " and pc3.asset_category_id = :assetCategoryId "
					+ " and pc3.patient_type_id = :patientClassId " + " union " + " select "
					+ " 0 as \"patientClassPercentage\", " + " false as \"markupStatus\", " + " 4 as \"priorityId\" , "
					+ " 0 as \"id\" " + " order by " + " \"priorityId\", " + " \"id\" desc " + " limit 1 ";

			patientConsession = (PatientConsession) getSession().createSQLQuery(sql).setParameter("itemId", itemId)
					.setParameter("paymentEntitlementId", entitlementId).setParameter("patientClassId", patientClassId)
					.setParameter("patientCategoryId", patientCategoryId).setParameter("productCategoryId", prodCatId)
					.setParameter("assetCategoryId", assetCategoryId)
					.setResultTransformer(Transformers.aliasToBean(PatientConsession.class)).uniqueResult();
		} else {
			String sql = "select " + "pc1.percentage as \"patientClassPercentage\", "
					+ "pc1.markup_status as \"markupStatus\", " + "1 as \"priorityId\", "
					+ "pc1.patient_type_group_id as \"id\" " + "from " + "opd.t_encounter_master tem "
					+ "inner join m_item item " + "on item.id =:itemId "
					+ "left join billing.m_patient_type_group_wise_details pc1 on " + "pc1.item_id = item.id "
					+ "and pc1.patient_type_id = tem.patient_class_id " + "where " + "tem.encounter_id = :encounterId "
					+ "and pc1.percentage is not null " + "and pc1.visit_type_id = 1 "
					+ "and pc1.payment_entitlement_id = (select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where encounter_id=:encounterId and status='A' and priority_id=1 limit 1) "
					+ "and pc1.patient_category_id =:patientCategoryId " + "and pc1.status='A' " + " union " + "select "
					+ "pc1.percentage as \"patientClassPercentage\", " + "pc1.markup_status as \"markupStatus\", "
					+ "2 as \"priorityId\", " + "pc1.patient_type_group_id as \"id\" " + "from "
					+ "opd.t_encounter_master tem " + "inner join m_product_category pcat "
					+ "on pcat.id =:productCategoryId " + "left join billing.m_patient_type_group_wise_details pc1 on "
					+ "pc1.product_category_id = pcat.id " + "and pc1.patient_type_id = tem.patient_class_id "
					+ "where " + "tem.encounter_id = :encounterId " + "and pc1.item_id is null "
					+ "and pc1.percentage is not null " + "and pc1.visit_type_id =1 "
					+ "and pc1.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where encounter_id=:encounterId and status='A' and priority_id=1 limit 1) "
					+ "and pc1.patient_category_id =:patientCategoryId " + "and pc1.status='A' " + " union " + "select "
					+ "pc3.percentage as \"patientClassPercentage\", " + "pc3.markup_status as \"markupStatus\", "
					+ "3 as \"priorityId\", " + "pc3.patient_type_group_id as \"id\" " + "from "
					+ "opd.t_encounter_master tem " + "inner join m_asset_category asset on "
					+ "asset.id =:assetCategoryId " + "left join billing.m_patient_type_group_wise_details pc3 on "
					+ "pc3.asset_category_id = asset.id " + "and pc3.patient_type_id = tem.patient_class_id " + "where "
					+ "tem.encounter_id = :encounterId " + "and pc3.sub_speciality_id is null "
					+ "and pc3.item_id is null " + "and pc3.product_category_id is not null "
					+ "and pc3.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where encounter_id=:encounterId and status='A' and priority_id=1 limit 1) "
					+ "and pc3.patient_category_id =:patientCategoryId " + "and pc3.visit_type_id = 1 "
					+ "and pc3.status='A' " + " union " + "select " + "0 as \"patientClassPercentage\", "
					+ "false as \"markupStatus\", " + " 4 as \"priorityId\" , " + "0 as \"id\" " + " order by "
					+ "\"priorityId\",\"id\" desc " + "limit 1";

			patientConsession = (PatientConsession) getSession().createSQLQuery(sql).setParameter("itemId", itemId)
					.setParameter("encounterId", encounterId).setParameter("patientCategoryId", patientCategoryId != null ? patientCategoryId : 1)
					.setParameter("assetCategoryId", assetCategoryId).setParameter("productCategoryId", prodCatId)
					.setResultTransformer(Transformers.aliasToBean(PatientConsession.class)).uniqueResult();
		}

		return (patientConsession != null ? patientConsession : null);

	}

	public BigInteger getConsignmentBatchWiseQty(String batchNo, String lotNo, Boolean isConsignment) {
		String s = " select SUM(S.available_qty) from inventory.t_batch b inner join "
				+ "inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment =:isConsignment  and B.batch_no =:batchNo and b.lot_no =:lotNo ";
		Query query = getSession().createSQLQuery(s);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNo", lotNo);
		query.setParameter("isConsignment", isConsignment);
		return (BigInteger) query.uniqueResult();
	}

	// added by minesh as per discusses with Divyeshsir
	public BigInteger getConsignmentBatchWiseStoreQty(String batchNo, String lotNo, Boolean isConsignment,
			int storeId) {
		String s = " select SUM(S.available_qty) from inventory.t_batch b inner join "
				+ "inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment =:isConsignment  and B.batch_no =:batchNo and b.lot_no =:lotNo "
				+ " AND S.store_id =:storeId ";
		Query query = getSession().createSQLQuery(s);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNo", lotNo);
		query.setParameter("isConsignment", isConsignment);
		query.setParameter("storeId", storeId);
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public Integer getPatientCategoryId(Integer encounterId) {
		String hql = "SELECT patientEncounter.patientCategoryId as patientCategoryId "
				+ "	FROM Encounters AS encounter"
				+ " INNER JOIN encounter.patient as patientEncounter WITH patientEncounter.status = 'A' "
				+ "	WHERE encounter.id = :encounterId " + "	AND encounter.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("encounterId", encounterId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Integer) query.uniqueResult();
	}

	@Override
	public List<TariffResponse> getItemProdCategories(List<Integer> itemIds) {
		String hql = "SELECT prodCat.id, item.id " + " FROM Item AS item "
				+ " INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ " INNER JOIN generic.productCategory AS prodCat "
				+ " WHERE item.isDeleted = false and item.isActive = true AND item.id in(:itemIds)"
				+ " AND item.unit.unitId = :unitId";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemIds", itemIds);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}

	@Override
	public Integer getPatientCategoryIdAdmission(Integer admissionId) {
		String hql = "SELECT patientAdmission.patientCategoryId as patientCategoryId "
				+ "	FROM Admission AS admission "
				+ " INNER JOIN admission.patientRegistration as patientAdmission WITH patientAdmission.status = 'A' "
				+ "	WHERE admission.id = :admissionId and admission.status='A' ";

		Query query = getSession().createQuery(hql);
		query.setParameter("admissionId", admissionId);
		// query.setParameter("unitId", sessionService.getUnitId());
		return (Integer) query.uniqueResult();
	}

	@Override
	public BigInteger getClubBatchWiseAvailableStock(String batchNo, String lotNo, Boolean isConsignment,
			Integer itemId, Integer storeId, Integer unitId) {
		BigInteger t = BigInteger.valueOf(0);
		String sql = "SELECT SUM(S.available_qty) FROM inventory.t_batch b " +
				"INNER JOIN inventory.t_store_stock S ON b.id = S.batch_id " +
				"WHERE b.batch_no = :batchNo AND b.lot_no = :lotno " +
				"AND b.item_id = :itemId AND S.store_id = :storeId " +
				"AND b.unit_id = :unitId AND S.available_qty IS NOT NULL " +
				"AND S.total_qty IS NOT NULL AND S.unit_id = :unitId";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotno", lotNo);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId); // Set unitId as a parameter to avoid concatenation
		BigInteger result = (BigInteger) query.uniqueResult();
		if (result != null) {
			return result;
		} else {
			return t;
		}
	}

	@Override
	public Long save(PrefixRunningNumber prefixRunningNumber) {
		getSession().save(prefixRunningNumber);
		return prefixRunningNumber.getId();
	}

	/*
	 * @Override public PrescriptionDetail savePrescriptionDetail(PrescriptionDetail
	 * prescriptionDetail) { getSession().save(prescriptionDetail); return
	 * prescriptionDetail.getId(); }
	 */

	@Override
	public List<SaleDetailPayeeMapperDto> getSalePayeeMpperListbyDetailsId(Integer saleDetailsId, Integer saleId) {
		try {
			StringBuilder query = new StringBuilder("SELECT "
					+ "  CAST(mapper.t_sale_details_id AS int) AS \"tSaleDetailsId\", "
					+ "  mapper.item_id AS \"itemId\", "
					+ "  mapper.contract_id AS \"contractId\", "
					+ "  mapper.payee_id AS \"payeeId\", "
					+ "  mapper.payee_amount AS \"payeeAmount\", "
					+ "  mapper.split_bill_amount AS \"spiltBillAmt\", "
					+ "  com.company_desc AS \"payeeDesc\", "
					+ "  tsd.product_category_id AS \"productCategoryId\" "
					+ "FROM "
					+ "  pharmacy.t_sale_detail tsd "
					+ "INNER JOIN pharmacy.t_sale_detail_payee_mapper mapper ON "
					+ "  mapper.t_sale_details_id = tsd.id "
					+ "LEFT JOIN t_company_master com ON "
					+ "  com.company_id = mapper.payee_id "
					+ "LEFT JOIN service.m_company_contract_master contract ON "
					+ "  contract.contract_id = mapper.contract_id "
					+ "WHERE "
					+ "  mapper.status = 'A' ");

			if (saleId != null && saleId > 0) {
				query.append(" AND tsd.sale_id = :saleId");
			}

			if (saleDetailsId != null && saleDetailsId > 0) {
				query.append(" AND tsd.id = :saleDetailsId");
			}

			query.append(" ORDER BY mapper.payee_id ASC");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			if (saleId != null && saleId > 0) {
				sqlQuery.setParameter("saleId", saleId);
			}

			if (saleDetailsId != null && saleDetailsId > 0) {
				sqlQuery.setParameter("saleDetailsId", saleDetailsId);
			}

			List<SaleDetailPayeeMapperDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(SaleDetailPayeeMapperDto.class)).list();
			return list;

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public BigInteger getClubBatchWiseAvailableStockLotNo(String lotNo, Boolean isConsignment, Integer itemId,
			Integer storeId) {

		// and b.item_id=995 and s.store_id=3
		BigInteger t = BigInteger.valueOf(0);
		// String sql = " select SUM(S.available_qty) from inventory.t_batch b inner
		// join inventory.t_store_stock S on B.id=S.batch_id where
		// B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no
		// =:lotno";
		String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where   b.lot_no =:lotno"
				+ " and b.item_id =:itemId and s.store_id =:storeId";

		Query query = getSession().createSQLQuery(sql);
		// query.setParameter("batchNo", batchNo);
		query.setParameter("lotno", lotNo);
		// query.setParameter("isconsignment", isconsignment);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);

		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}

	}

	public List<SaleDetailPackageMapperDto> getSalePackageMpperListbyDetailsId(Integer saleDetailsId,Integer saleId) {
		try {
			StringBuilder query = new StringBuilder("SELECT "
					+ "  CAST(mppr.t_sale_details_id AS int) AS \"saleDetailsId\", "
					+ "  mppr.item_id AS \"itemId\", "
					+ "  mppr.package_id AS \"packageId\", "
					+ "  mppr.payee_id AS \"payeeId\", "
					+ "  CAST(mppr.rate AS double precision) AS \"rate\", "
					+ "  CAST(mppr.package_amount AS double precision) AS \"packageAmount\", "
					+ "  mppr.is_package_item AS \"isPackageItem\", "
					+ "  item.name AS \"itemName\", "
					+ "  item.code AS \"itemCode\", "
					+ "  mppr.pacakge_consumption_service_detail_id AS \"pacakgeConsumptionServiceDetailId\", "
					+ "  mppr.package_consumption_master_id AS \"packageConsumptionMasterId\", "
					+ "  mppr.package_consumption_cap_details_id AS \"packageConsumptionCapDetailsId\", "
					+ "  mppr.package_qty AS \"packageQty\", "
					+ "  mppr.package_consumtion_eo_detail_id AS \"packageConsumptionEODetailId\", "
					+ "  mppr.cap_or_qty AS \"capOrQty\" "
					+ "FROM pharmacy.t_sale_detail tsd "
					+ "INNER JOIN public.t_sale_detail_package_mapper mppr ON mppr.t_sale_details_id = tsd.id "
					+ "LEFT JOIN service.m_package_master package_master ON mppr.package_id = package_master.package_master_id "
					+ "LEFT JOIN public.m_item item ON mppr.item_id = item.id "
					+ "WHERE mppr.status = 'A' ");

			if (saleDetailsId != null && saleDetailsId > 0) {
				query.append(" AND mppr.t_sale_details_id = :saleDetailsId");
			}
			if (saleId != null && saleId > 0) {
				query.append(" AND tsd.sale_id = :saleId");
			}
			query.append(" ORDER BY mppr.id");
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			if (saleDetailsId != null && saleDetailsId > 0) {
				sqlQuery.setParameter("saleDetailsId", saleDetailsId);
			}
			if (saleId != null && saleId > 0) {
				sqlQuery.setParameter("saleId", saleId);
			}
			List<SaleDetailPackageMapperDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(SaleDetailPackageMapperDto.class)).list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Sale getSaleById(long saleId) {
		return (Sale) getSession().get(Sale.class, saleId);
	}

	@Override
	public SaleDetail getSaleDetailsById(long saleDetailsId) {
		return (SaleDetail) getSession().get(SaleDetail.class, saleDetailsId);
	}

	@Override
	public PrescriptionDetail savePrescriptionDetailForSubstitute(PrescriptionDetail prescriptionDetail) {
		getSession().save(prescriptionDetail);
		return prescriptionDetail;
	}

	@Override
	public PrescriptionFrequencyDetail savePrescriptionFrequencyDetail(PrescriptionFrequencyDetail pfd) {
		getSession().save(pfd);
		return pfd;
	}

	@Override
	public AssignPickerDetail saveAssignPickerDetailItem(AssignPickerDetail assignPickerDetail) {
		getSession().saveOrUpdate(assignPickerDetail);
		return assignPickerDetail;
	}

	@Override
	public AssignPickerDetailAddedItems saveAssignPickerDetailAddedItem(AssignPickerDetailAddedItems addedItems) {
		getSession().save(addedItems);
		return addedItems;
	}

	@Override
	public List<SaleDetailPackageMapperDto> getSalePackageMpperListbyDetailsIdReturn(Integer saleDetailsId,
			BigDecimal netAmount) {
		try {

			String query = "select " + "	cast(mppr.t_sale_details_id as int) as \"saleDetailsId\", "
					+ "	mppr.item_id as \"itemId\", " + "	mppr.package_id as \"packageId\", "
					+ "	mppr.payee_id as \"payeeId\", " + "	cast(mppr.rate as double precision) as \"rate\", "
					+ "	cast(mppr.package_amount as double precision) \"packageAmount\", "
					+ "	mppr.is_package_item as \"isPackageItem\", " + "	item.\"name\" as \"itemName\", "
					+ "	item.code as \"itemCode\", "
					+ "	mppr.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
					+ "	mppr.package_consumption_master_id as \"packageConsumptionMasterId\", "
					+ "	mppr.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\", "
					+ "	mppr.package_qty as \"packageQty\", "
					+ "	mppr.package_consumtion_eo_detail_id as \"packageConsumptionEODetailId\", "
					+ "	mppr.cap_or_qty as \"capOrQty\" " + " from " + "	public.t_sale_detail_package_mapper mppr "
					+ " left join service.m_package_master package_master on "
					+ "	mppr.package_id = package_master.package_master_id " + " left join public.m_item item on "
					+ "	mppr.item_id = item.id " + " where " + " is_package_item ='Y' and mppr.status = 'A' "
					+ "	and mppr.t_sale_details_id= " + saleDetailsId + " order by " + "	mppr.id asc  ";

			List<SaleDetailPackageMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(SaleDetailPackageMapperDto.class)).list();

			List<SaleDetailPackageMapperDto> packageList = new ArrayList<SaleDetailPackageMapperDto>();
			for (SaleDetailPackageMapperDto pkgMpper : list) 
			{
				if (netAmount.doubleValue() > 0) 
				{
					packageList.add(pkgMpper);
					netAmount = BigDecimal.valueOf(GlobalCommonDateUtils
							.getAmountCap(netAmount.doubleValue() - pkgMpper.getPackageAmount().doubleValue()));
				}
			}
			return packageList;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public List<SaleDetailPayeeMapperDto> getSalePayeeMpperListbyDetailsIdReturn(Integer saleDetailsId) {
		try {

			String query = " select " 
					 + " 	cast(mapper.id as int) as id, " 
					 + " 	cast(mapper.t_sale_details_id as int) as \"tSaleDetailsId\", " 
					 + " 	mapper.item_id as \"itemId\", " 
					 + " 	mapper.contract_id as \"contractId\", " 
					 + " 	mapper.payee_id as \"payeeId\", " 
					 + " 	mapper.associate_company_id as \"associateCompanyId\", " 
					 + " 	mapper.payee_amount as \"payeeAmount\", " 
					 + " 	mapper.split_bill_amount as \"spiltBillAmt\", " 
					 + " 	com.company_desc as \"payeeDesc\" ," 
					 + "	tsb.product_category_id as \"productCategoryId\" " 
					 + " from " 
					 + " 	pharmacy.t_sale_detail_payee_mapper mapper "
					 + " inner join  pharmacy.t_sale_detail tsb on tsb.id = mapper.t_sale_details_id " 
					 + " left join t_company_master com on " 
					 + " 	com.company_id = mapper.payee_id " 
					 + " left join service.m_company_contract_master contract on " 
					 + " 	contract.contract_id = mapper.contract_id	 " 
					 + " where " 
					 + " 	mapper.status = 'A' " 
					 + " 	and mapper.t_sale_details_id =:saleDetailsId "
					 + " order by " 
					 + " 	mapper.id asc  ";

			Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			createQuery.setParameter("saleDetailsId",saleDetailsId);
			List<SaleDetailPayeeMapperDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(SaleDetailPayeeMapperDto.class)).list();
			return list;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<SaleDetailPayeeMapperDto> getSalePayeeMpperListbyDetailsIdReturn(Integer saleDetailsId,
			BigDecimal netAmount, Integer returnQty) {
		try {

			List<SaleDetailPayeeMapperDto> payeeList = new ArrayList<SaleDetailPayeeMapperDto>();
			String checkConract = " select " 
					+ "		coalesce(contract.co_share_percentage,0) as \"selfCoPay\", "
					+ "		mapper.payee_id as \"payeeId\", "
					+ "		mapper.associate_company_id as \"associateCompanyId\", "
					+ "		sum(mapper.payee_amount) as \"payeeAmount\", "
					+ "		(sum(mapper.payee_amount)/sum(nullif(mapper.quantity,0))) as \"unitPayeeRate\", "
					+ "	tsb.product_category_id as \"productCategoryId\" " 
					+ " from pharmacy.t_sale_detail_payee_mapper mapper " 
					 + " inner join  pharmacy.t_sale_detail tsb on tsb.id = mapper.t_sale_details_id " 
					+ "	left join t_company_master com on	"
					+ "		com.company_id = mapper.payee_id "
					+ " left join service.m_company_contract_master contract on "
					+ "		contract.contract_id = mapper.contract_id "
					+ " where mapper.status = 'A' "
					+ "		and mapper.t_sale_details_id =:saleDetailsId  "
					+ "	group by mapper.payee_id,mapper.associate_company_id, contract.co_share_percentage,tsb.product_category_id";

			String query = "select " 
					+ "     cast(mapper.id as int) as id, "
					+ " 	cast(mapper.t_sale_details_id as int) as \"tSaleDetailsId\", "
					+ " 	mapper.item_id as \"itemId\", " 
					+ " 	mapper.contract_id as \"contractId\", "
					+ " 	mapper.payee_id as \"payeeId\", "
					+ "		mapper.associate_company_id as \"associateCompanyId\", "
					+ " 	mapper.payee_amount as \"payeeAmount\", "
					+ "		mapper.split_bill_amount as \"spiltBillAmt\", " 
					+ " 	com.company_desc as \"payeeDesc\" ,"
					+ "	tsb.product_category_id as \"productCategoryId\" " 
					+ " from pharmacy.t_sale_detail_payee_mapper mapper " 
					 + " inner join  pharmacy.t_sale_detail tsb on tsb.id = mapper.t_sale_details_id " 
					+ " left join t_company_master com on "
					+ " 	com.company_id = mapper.payee_id "
					+ " left join service.m_company_contract_master contract "
					+ " 	on contract.contract_id=mapper.contract_id " 
					+ " where mapper.status = 'A' "
					+ " 	and mapper.t_sale_details_id =:saleDetailsId   order by "
					+ " 	mapper.contract_id,mapper.payee_id,mapper.associate_company_id asc ";

			Query creatQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("saleDetailsId",saleDetailsId);
			List<SaleDetailPayeeMapperDto> list = creatQuery.setResultTransformer(Transformers.aliasToBean(SaleDetailPayeeMapperDto.class)).list();

			Query creatQueSry = sessionFactory.getCurrentSession()
					.createSQLQuery(checkConract)	.setParameter("saleDetailsId",saleDetailsId);
			List<SaleDetailPayeeMapperDto> contractCheck =	creatQueSry.setResultTransformer(Transformers.aliasToBean(SaleDetailPayeeMapperDto.class)).list();

			if (contractCheck != null && contractCheck.size() > 0 && returnQty != null && returnQty > 0) {
				for (SaleDetailPayeeMapperDto payeeContract : contractCheck) {
					BigDecimal payeeAmount = payeeContract.getUnitPayeeRate().multiply(BigDecimal.valueOf(returnQty));
					for (SaleDetailPayeeMapperDto payee : list) {
						if (payeeContract.getPayeeId().equals(payee.getPayeeId())) {
							if (payeeAmount.doubleValue() > 0) {
								payeeList.add(payee);
								netAmount = BigDecimal.valueOf(GlobalCommonDateUtils
										.getAmountCap(netAmount.doubleValue() - payee.getPayeeAmount().doubleValue()));
								payeeAmount = BigDecimal.valueOf(GlobalCommonDateUtils.getAmountCap(
										payeeAmount.doubleValue() - payee.getPayeeAmount().doubleValue()));
							}
						}

					}
				}
			} else {
				for (SaleDetailPayeeMapperDto payee : list) {
					if (netAmount.doubleValue() > 0) {
						payeeList.add(payee);
						netAmount = BigDecimal.valueOf(GlobalCommonDateUtils
								.getAmountCap(netAmount.doubleValue() - payee.getPayeeAmount().doubleValue()));
					}
				}
			}
			return payeeList;
		} catch (Exception e) {
			
			throw e;
		}
	}

//=======
	public IssueDetailsDto getIssueDtlsByPrescriptionId(Integer id) {
		try {
			String sql = "select  " + " issueDtl.id as \"issueDetailId\","
					+ " issueDtl.prescription_detail_id as \"prescriptionDetailId\","
					+ " issueDtl.patient_indent_detail_id as \"patientIndentDetailId\","
					+ " issueDtl.status_id as \"statusId\"," + " issueDtl.issue_id as \"issueId\" ,"
					+ " issueDtl.assign_picker_detail_id as \"assignPickerDetailId\","
					+ " issueDtl.implant_indent_detail_id as \"implantIndentDetailId\","
					+ " issueDtl.issue_quantity as \"issueQuantity\"," + " issueDtl.batch_id as \"batchId\","
					+ " issueDtl.uom_type_id as \"uomTypeId\"," + " issueDtl.sale_detail_id as \"saleDetailId\","
					+ " issueDtl.uom_unit_id as \"uomUnitId\", "
					+ "  issueDtl.is_from_assign_picker as \"isFromAssignPicker\" ,"
					+ " issueDtl.parent_issue_detail_id as \"parentIssueDetailId\" ,"
					+ " issueDtl.net_amount as \"netAmount\" ," + " issueDtl.credit_payable as \"creditPayable\" ,"
					+ " issueDtl.self_payable as \"selfPayable\" ,"
					+ " issueDtl.discount_amount as \"discountAmount\" ," + " issueDtl.rate as \"rate\" ,"
					+ " issueDtl.tax_amount as \"taxAmount\" ," + " issueDtl.tax_percentage as \"taxPercentage\" ,"
					+ " issueDtl.total_amount as \"totalAmount\" ,"
					+ " issueDtl.unit_discount_amount as \"unitDiscountAmount\" ,"
					+ " issueDtl.package_consumption_amt as \"packageConsumptionAmt\" ,"
					+ " issueDtl.consession_amount as \"consessionAmount\" ,"
					+ " issueDtl.unit_consession_amount as \"unitConsessionAmount\" ,"
					+ " issueDtl.consession_percentage  as \"consessionPercentage\"  "
					+ " from pharmacy.t_issue_detail issueDtl " + " where issueDtl.id=:id ";

			Query query = getSession().createSQLQuery(sql).addScalar("issueDetailId").addScalar("prescriptionDetailId")
					.addScalar("patientIndentDetailId").addScalar("statusId").addScalar("assignPickerDetailId")
					.addScalar("implantIndentDetailId").addScalar("issueQuantity").addScalar("batchId")
					.addScalar("uomTypeId").addScalar("uomUnitId").addScalar("saleDetailId").addScalar("issueId")
					.addScalar("isFromAssignPicker", new TrueFalseType()).addScalar("parentIssueDetailId")
					.addScalar("netAmount").addScalar("creditPayable").addScalar("selfPayable")
					.addScalar("discountAmount").addScalar("rate").addScalar("taxAmount").addScalar("taxPercentage")
					.addScalar("totalAmount").addScalar("unitDiscountAmount").addScalar("packageConsumptionAmt")
					.addScalar("consessionAmount").addScalar("unitConsessionAmount").addScalar("consessionPercentage");
			query.setParameter("id", id);
//			query.setParameter("prescriptionDetailId", prescriptionDetailId);
			query.setResultTransformer(Transformers.aliasToBean(IssueDetailsDto.class));
			return (IssueDetailsDto) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getIssueDtlsByPrescriptionId failed", e);
		return null;
		}

	}

	@Override
	public IssueDetail saveIssueDetailForSubstitute(IssueDetail issueDtlNew) {
		getSession().save(issueDtlNew);
		return issueDtlNew;
	}

	@Override
	public IssueDetailsDto getIssueDetailsById(Integer id, Integer issueDetail) {
		try {
			String sql = "select  " + " issueDtl.id as \"issueDetailId\","
					+ " issueDtl.prescription_detail_id as \"prescriptionDetailId\","
					+ " presDtl.prescription_id as \"prescriptionId\"," + " presDtl.item_id as \"itemId\""
					+ " from pharmacy.t_issue_detail issueDtl "
					+ " inner join pharmacy.t_prescription_detail presDtl on presDtl.id=issueDtl.prescription_detail_id"
					+ " where presDtl.prescription_id=:id" + " AND issueDtl.id=:issueDetail";

			Query query = getSession().createSQLQuery(sql).addScalar("issueDetailId").addScalar("prescriptionDetailId")
					.addScalar("prescriptionId").addScalar("itemId");
			query.setParameter("id", id);
			query.setParameter("issueDetail", issueDetail);

			query.setResultTransformer(Transformers.aliasToBean(IssueDetailsDto.class));
			return (IssueDetailsDto) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getIssueDetailsById failed", e);
		return null;
		}

//>>>>>>> Stashed changes
	}

	@Override
	public List<SubstituteItemDto> getListOfSubstitutes(Integer assignPickerId, Integer assignPickerDetailId) {
		try {
			String sql = "	select		distinct(addItems.assign_picker_id) as \"assignPickerId\","
					+ "		presDtl.id as \"prescriptionDetailid\","
					+ "		presDtl.item_id as \"itemId\",		itm.name as \"itemName\","
					+ "		itm.code as \"itemCode\",		generic.id as \"genericId\","
					+ "		generic.name as \"genericName\",		freq.description as \"frequency\","
					+ "		presFreqDtl.dose as \"dose\",	"
					+ "		sts.status as \"status\",		presDtl.is_intervention as \"interventionFlag\","
					+ "		presDtl.order_qty as \"quantityOrdered\","
					+ "		addItems.issue_quantity as \"issuedQuantity\","
					+ "		addItems.uom_type_id as \"uomTypeId\",	" + "		addItems.uom_unit_id as \"uomUnitId\","
					+ "		addItems.batch_id as \"batchid\","
					+ "     presDtl.parent_substitute_id as \"parentSubstituteId\" "
					+ "    from  pharmacy.t_assign_picker_detail_added_items addItems "
					+ "	inner join pharmacy.t_assign_picker assign on		addItems.assign_picker_id = assign.id "
					+ "	inner join pharmacy.t_prescription_detail presDtl on "
					+ "		presDtl.id = addItems.prescription_detail_id "
					+ "	inner join pharmacy.t_prescription_frequency_detail presFreqDtl on "
					+ "		presDtl.id = presFreqDtl.prescription_detail_id	inner join public.m_item itm on "
					+ "		itm.id = presDtl.item_id	inner join public.m_generic generic on "
					+ "		itm.generic_id = generic.id	inner join public.m_frequency freq on "
					+ "		freq.id = presFreqDtl.frequency_id	inner join public.m_status sts on "
					+ "		sts.id = addItems.status_id	where		addItems.assign_picker_id =:id and addItems.id =:assignPickerDetailsId"
					+ "		and presDtl.parent_substitute_id is not null " + " ";

			Query query = getSession().createSQLQuery(sql).addScalar("assignPickerId").addScalar("prescriptionDetailid")
					.addScalar("itemId").addScalar("itemName").addScalar("itemCode").addScalar("genericId")
					.addScalar("genericName").addScalar("frequency").addScalar("dose")
//				.addScalar("startDate").addScalar("stopDate")
					.addScalar("status").addScalar("interventionFlag").addScalar("quantityOrdered")
					.addScalar("issuedQuantity").addScalar("uomTypeId").addScalar("uomUnitId").addScalar("batchid")
					.addScalar("parentSubstituteId");
			query.setParameter("id", assignPickerId);
			query.setParameter("assignPickerDetailsId", assignPickerDetailId);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));
			return query.list();
		} catch (Exception e) {
		logger.error("getListOfSubstitutes failed", e);
		return null;
		}

	}

	@Override
	public List<SubstituteItemDto> getListOfPendingIssuesSubstitutes(Integer id) {
		try {
			String sql = "select " + "	issueDtl.id as \"issueDtlId\"," + "	itm.name as \"itemName\" ,"
					+ "	itm.code as \"itemCode\"," + "	generic.id as \"genericId\","
					+ " itm.is_three_uom_applicable as \"isThreeUOMApplicable\", "
                    + " itm.storage_unit_id as \"storageUnitId\","
                    + " itm.is_lasa as \"isLasa\","
                    + " storageUnit.name as \"storageUnitName\", "
					+ "	generic.name as \"genericName\"," + "	freq.description as \"frequency\","
					+ "	presFreqDtl.dose as \"dose\"," + "	presFreqDtl.start_date as \"startDate\","
					+ "	presFreqDtl.stop_date as \"stopDate\"," + "	issueDtl.status_id as \"statusId\","
					+ "	assignDtl.picked_quantity as \"pickedQuantity\","
					+ "	sts.status as \"status\", manufac.name as \"manufacturer\", "
					+ "	presDtl.id as \"prescriptionDetailid\"," + "	presDtl.item_id as \"itemId\","
					+ "	presDtl.parent_substitute_id as \"parentSubstituteId\","
					+ "	issueDtl.parent_issue_detail_id  as \"parentIssueDetailsId\","
					+ " generic.product_category_id as \"productCategoryId\", "
					+ " prod.asset_category_id as \"assetCategoryId\", "
					+ " issueDtl.consession_percentage as \"consessionPercentage\", "
					+ " issueDtl.consession_amount as \"consessionAmount\", "
					+ "  issueDtl.is_from_assign_picker as \"isFromAssignPicker\","
					+ "	presDtl.is_intervention as \"interventionFlag\", itm.manufacturer_id as \"manufactureId\","
					+ "	presDtl.order_qty as \"quantityOrdered\"," + "	assadd.issue_quantity as \"quantityIssued\","
					+ "	issueDtl.issue_quantity as \"issuedQuantity\", issueDtl.uom_type_id as \"uomTypeId\" , issueDtl.uom_unit_id as \"uomUnitId\" ,"
					+ " issueDtl.batch_id as \"batchid\", itm.product_category_id as \"productCategoryId\","
					+ "	issueDtl.is_narcotic_substitute as \"isNarcoticSubstitute\", tx.name as \"tax\","
					// + " tx.tax_percentage as \"taxPercentage\", "
					+ " issueDtl.net_amount as \"netAmount\", saledtl.credit_payable as \"creditPayable\", issueDtl.self_payable as \"selfPayable\","
					+ "  issueDtl.discount_amount as \"discountAmount\", issueDtl.rate as \"rate\", issueDtl.tax_amount as \"taxAmount\", "
					+ "  tx.tax_percentage as \"taxPercentage\", issueDtl.total_amount as \"totalAmount\","
					+ "	itm.s_tax_id as \"taxId\" , itm.s_tax_id as \"taxId\" ,assignDtl.picked_quantity as \"pickedQuantity\""
					+ "	from pharmacy.t_issue_detail issueDtl "
					+ "	inner join pharmacy.t_assign_picker_detail assignDtl on assignDtl.id=issueDtl.assign_picker_detail_id"
					+ "	inner join pharmacy.t_prescription_detail presDtl on presDtl.id=issueDtl.prescription_detail_id"
					+ "	inner join pharmacy.t_prescription_frequency_detail presFreqDtl on presDtl.id=presFreqDtl.prescription_detail_id"
					+ "	left join pharmacy.t_issue_detail_batch_mapper bthmppr on issueDtl.id=bthmppr.issue_detail_id"
					+ " left join pharmacy.t_assign_picker_detail_added_items  assadd on assadd.issue_detail_id = issueDtl.id"
					+ "	left join pharmacy.t_sale_detail saledtl on saledtl.id=issueDtl.sale_detail_id "
					+ "	inner join public.m_item itm on itm.id=presDtl.item_id "
					+ " inner join public.m_tax tx on itm.s_tax_id=tx.id"
					+ " left join public.m_manufacturer manufac on itm.manufacturer_id=manufac.id"
					+ " left join public.m_storage_unit storageUnit on storageUnit.id =  itm.storage_unit_id "
					+ "	inner join public.m_generic generic on itm.generic_id=generic.id"
					+ "	inner join public.m_product_category prod on prod.id=generic.product_category_id"
					+ "	inner join public.m_frequency freq on freq.id=presFreqDtl.frequency_id"
					+ "	inner join public.m_status sts on sts.id=issueDtl.status_id"
					+ " where issueDtl.issue_id=:id  and issueDtl.is_substitute=true ";

			Query query = getSession().createSQLQuery(sql).addScalar("issueDtlId").addScalar("itemName")
					.addScalar("pickedQuantity").addScalar("productCategoryId").addScalar("itemCode")
					.addScalar("assetCategoryId").addScalar("genericId").addScalar("isThreeUOMApplicable")
					.addScalar("storageUnitId").addScalar("storageUnitName").addScalar("genericName").addScalar("frequency")
					.addScalar("tax").addScalar("manufactureId").addScalar("dose").addScalar("startDate")
					.addScalar("consessionPercentage").addScalar("consessionAmount").addScalar("stopDate")
					.addScalar("interventionFlag").addScalar("taxPercentage").addScalar("issuedQuantity")
					.addScalar("statusId").addScalar("status").addScalar("manufacturer").addScalar("parentSubstituteId")
					.addScalar("parentIssueDetailsId").addScalar("prescriptionDetailid").addScalar("quantityOrdered")
					.addScalar("quantityIssued").addScalar("taxId").addScalar("batchid").addScalar("uomTypeId")
					.addScalar("uomUnitId").addScalar("itemId").addScalar("netAmount").addScalar("creditPayable")
					.addScalar("selfPayable").addScalar("discountAmount").addScalar("rate").addScalar("taxAmount")
					.addScalar("totalAmount");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));
			return query.list();
		} catch (Exception e) {
		logger.error("getListOfPendingIssuesSubstitutes failed", e);
		return null;
		}

	}

	@Override
	public List<PurchaseOrderDetail> getPurchaseOrdersWithCanelByRequestDetailId(Integer id) {

		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Rejected.getStatus());
		statusIds.add(com.param.common.constants.Status.Cancelled.getStatus());

		String hql = "SELECT purchaseOrderDetail.poQuantity as poQuantity, purchaseOrderDetail.poApprovedQuantity as poApprovedQuantity "
				+ "FROM PurchaseOrderDetail AS purchaseOrderDetail INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder "
				+ "INNER JOIN purchaseOrderDetail.putchRequestDetail as purchaseRequestDetails WITH purchaseRequestDetails.isActive = true AND purchaseRequestDetails.isDeleted = false AND purchaseRequestDetails.id = :id "
				+ " WHERE purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false and purchaseOrder.approvalStatus.id not in (:statusId)";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameterList("statusId", statusIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetail.class));

		return query.list();

	}

	@Override
	public Long getCancelQuotationRequestLeaseQunatity(Integer storeId, Integer id, Integer unitId) {

		String hql = "SELECT SUM (COALESCE ((quotationRequestDetail.leaseQuantity),0) )"
				+ " FROM QuotationRequestDetail AS quotationRequestDetail "
				+ "INNER JOIN quotationRequestDetail.quotationRequest AS quotationRequest WITH quotationRequest.isDeleted = false AND quotationRequest.isActive = true AND quotationRequest.unit.unitId = :unitId "
				+ "WHERE quotationRequest.store.storeId = :storeId AND quotationRequestDetail.item.id = :itemId "
				+ " AND quotationRequestDetail.isDeleted = false AND quotationRequestDetail.isActive = true AND quotationRequestDetail.unit.unitId = :unitId ";
		// + " AND quotationRequestDetail.status.id != :cancelStatusId ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public List<SubstituteItemDto> getListOfSubstitutesWithoutAssignPicker(Integer id) {
		try {

			String sql = "select " + "	issueDtl.id as \"issueDtlId\","
			+ "	itm.name as \"itemName\" ,"
					+ "	itm.code as \"itemCode\"," 
					 + " itm.is_three_uom_applicable as \"isThreeUOMApplicable\", "
					 + " itm.is_lasa as \"isLasa\", "
			+ "	generic.id as \"genericId\","
					+ "	generic.name as \"genericName\"," 
			+ "	freq.description as \"frequency\","
					+ "	presFreqDtl.dose as \"dose\"," 
			+ "	presFreqDtl.start_date as \"startDate\","
					+ "	presFreqDtl.stop_date as \"stopDate\"," 
			+ "	issueDtl.status_id as \"statusId\","
					// + " assignDtl.picked_quantity as \"pickedQuantity\"," + " sts.status as
					// \"status\", "
					+ " manufac.name as \"manufacturer\", " 
					+ "	presDtl.id as \"prescriptionDetailid\","
					+ "	presDtl.item_id as \"itemId\"," 
					+ "	presDtl.parent_substitute_id as \"parentSubstituteId\","
					+ "	issueDtl.parent_issue_detail_id  as \"parentIssueDetailsId\","
					+ "  issueDtl.is_from_assign_picker as \"isFromAssignPicker\","
					+ "	presDtl.is_intervention as \"interventionFlag\","
					+ " itm.manufacturer_id as \"manufactureId\","
					+ " itm.storage_unit_id as \"storageUnitId\","
					+ " storageUnit.name as \"storageUnitName\", " 
					+ "	presDtl.order_qty as \"quantityOrdered\"," 
					+ "	issueDtl.issue_quantity as \"quantityIssued\","
					+ "	issueDtl.issue_quantity as \"issuedQuantity\", "
					+ "issueDtl.uom_type_id as \"uomTypeId\" ,"
					+ " issueDtl.uom_unit_id as \"uomUnitId\" ,"
					+ " issueDtl.batch_id as \"batchid\", " 
					+ " generic.product_category_id as \"productCategoryId\", "
					+ " prod.asset_category_id as \"assetCategoryId\", "
//					+ "saledtl.consession_percentage as \"consessionPercentage\","
					+ " issueDtl.consession_percentage as \"consessionPercentage\", "
					+ " issueDtl.consession_amount as \"consessionAmount\", "
					+ "	issueDtl.is_narcotic_substitute as \"isNarcoticSubstitute\","
					+ " tx.name as \"tax\","
					// + " tx.tax_percentage as \"taxPercentage\", "
					+ " saledtl.net_amount as \"netAmount\", "
					+ " saledtl.credit_payable as \"creditPayable\", "
					+ " saledtl.self_payable as \"selfPayable\","
					+ "  issueDtl.discount_amount as \"discountAmount\", "
					+ " issueDtl.rate as \"rate\", "
					+ " saledtl.tax_amount as \"taxAmount\", "
					+ "  saledtl.tax_percentage as \"taxPercentage\", "
					+ " saledtl.total_amount as \"totalAmount\","
					+ "	saledtl.tax_id as \"taxId\", "
					+ "	saledtl.package_consumption_amt as \"packageConsumptionAmt\" " 
					+ "	from pharmacy.t_issue_detail issueDtl "
					// + " inner join pharmacy.t_assign_picker_detail assignDtl on
					// assignDtl.id=issueDtl.assign_picker_detail_id"
					+ "	inner join pharmacy.t_prescription_detail presDtl on presDtl.id=issueDtl.prescription_detail_id"
					+ "	inner join pharmacy.t_prescription_frequency_detail presFreqDtl on presDtl.id=presFreqDtl.prescription_detail_id"
					+ "	left join pharmacy.t_issue_detail_batch_mapper bthmppr on issueDtl.id=bthmppr.issue_detail_id"
					+ "	left join pharmacy.t_sale_detail saledtl on saledtl.id=issueDtl.sale_detail_id "
					+ "	inner join public.m_item itm on itm.id=presDtl.item_id "
					+ " inner join public.m_tax tx on itm.s_tax_id=tx.id"
					+ " left join public.m_manufacturer manufac on itm.manufacturer_id=manufac.id"
					+ " left join public.m_storage_unit storageUnit on storageUnit.id =  itm.storage_unit_id "
					+ "	inner join public.m_generic generic on itm.generic_id=generic.id"
					+ "	inner join public.m_product_category prod on prod.id=generic.product_category_id"
					+ "	inner join public.m_frequency freq on freq.id=presFreqDtl.frequency_id"
					// + " inner join public.m_status sts on sts.id=assignDtl.status_id"
					+ " where issueDtl.issue_id=:id  and issueDtl.is_substitute=true ";

			Query query = getSession().createSQLQuery(sql).addScalar("issueDtlId").addScalar("itemName")
					.addScalar("productCategoryId").addScalar("itemCode").addScalar("isThreeUOMApplicable").addScalar("genericId")
					.addScalar("assetCategoryId").addScalar("genericName").addScalar("frequency").addScalar("tax")
					.addScalar("manufactureId").addScalar("storageUnitId").addScalar("storageUnitName")
					.addScalar("dose").addScalar("startDate").addScalar("stopDate")
					.addScalar("interventionFlag").addScalar("taxPercentage").addScalar("consessionPercentage")
					.addScalar("consessionAmount").addScalar("issuedQuantity").addScalar("statusId")
					.addScalar("manufacturer").addScalar("parentSubstituteId").addScalar("prescriptionDetailid")
					.addScalar("quantityOrdered").addScalar("quantityIssued").addScalar("taxId").addScalar("batchid")
					.addScalar("uomTypeId").addScalar("uomUnitId").addScalar("itemId").addScalar("parentIssueDetailsId")
					.addScalar("netAmount").addScalar("creditPayable").addScalar("selfPayable").addScalar("packageConsumptionAmt")
					.addScalar("discountAmount").addScalar("rate").addScalar("taxAmount").addScalar("totalAmount");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));
			return query.list();
		} catch (Exception e) {
		logger.error("getListOfSubstitutesWithoutAssignPicker failed", e);
		return null;
		}

	}

	@Override
	public List<SubstituteItemDto> getListOfNarcoticIssuesSubstitutes(Integer id) {
		try {
			String sql = "select " + "	issueDtl.id as \"issueDtlId\"," + "	itm.name as \"itemName\" ,"
					+ "	itm.code as \"itemCode\"," + "	generic.id as \"genericId\","
					+ "	generic.name as \"genericName\"," + "	freq.description as \"frequency\","
					+ "	presFreqDtl.dose as \"dose\"," + "	presFreqDtl.start_date as \"startDate\","
					+ "	presFreqDtl.stop_date as \"stopDate\"," + "	issueDtl.status_id as \"statusId\","
					+ "	assignDtl.picked_quantity as \"pickedQuantity\","
					+ "	sts.status as \"status\", manufac.name as \"manufacturer\", "
					+ "	presDtl.id as \"prescriptionDetailid\","
					+ "	issueDtl.parent_issue_detail_id  as \"parentIssueDetailsId\","
					+ "	presDtl.item_id as \"itemId\"," + "	presDtl.parent_substitute_id as \"parentSubstituteId\","
					+ "	presDtl.is_intervention as \"interventionFlag\", itm.manufacturer_id as \"manufactureId\","
					+ "	presDtl.order_qty as \"quantityOrdered\"," + "	assadd.issue_quantity as \"quantityIssued\","
					+ "	issueDtl.issue_quantity as \"issuedQuantity\", issueDtl.uom_type_id as \"uomTypeId\" , issueDtl.uom_unit_id as \"uomUnitId\" ,"
					+ " issueDtl.batch_id as \"batchid\","
					+ " generic.product_category_id as \"productCategoryId\", "
					+ " prod.asset_category_id as \"assetCategoryId\", "
					+ " issueDtl.consession_percentage as \"consessionPercentage\", "
					+ " issueDtl.consession_amount as \"consessionAmount\", "
					+ "	issueDtl.is_narcotic_substitute as \"isNarcoticSubstitute\", tx.name as \"tax\", "
					+ " issueDtl.net_amount as \"netAmount\", issueDtl.credit_payable as \"creditPayable\", issueDtl.self_payable as \"selfPayable\","
					+ "  issueDtl.discount_amount as \"discountAmount\", issueDtl.rate as \"rate\", issueDtl.tax_amount as \"taxAmount\", "
					+ " tx.tax_percentage as \"taxPercentage\", issueDtl.total_amount as \"totalAmount\","
					+ "	itm.s_tax_id as \"taxId\",assignDtl.picked_quantity as \"pickedQuantity\""
					+ "	from pharmacy.t_issue_detail issueDtl "
					+ "	left join pharmacy.t_assign_picker_detail assignDtl on assignDtl.id=issueDtl.assign_picker_detail_id"
					+ "	inner join pharmacy.t_prescription_detail presDtl on presDtl.id=issueDtl.prescription_detail_id"
					+ "	inner join pharmacy.t_prescription_frequency_detail presFreqDtl on presDtl.id=presFreqDtl.prescription_detail_id"
					+ "	left join pharmacy.t_issue_detail_batch_mapper bthmppr on issueDtl.id=bthmppr.issue_detail_id"
					+ " left join pharmacy.t_assign_picker_detail_added_items  assadd on assadd.issue_detail_id = issueDtl.id"
					+ "	left join pharmacy.t_sale_detail saledtl on saledtl.id=issueDtl.sale_detail_id "
					+ "	inner join public.m_item itm on itm.id=presDtl.item_id "
					+ " inner join public.m_tax tx on itm.s_tax_id=tx.id"
					+ " left join public.m_manufacturer manufac on itm.manufacturer_id=manufac.id"
					+ "	inner join public.m_generic generic on itm.generic_id=generic.id"
					+ "	inner join public.m_product_category prod on prod.id=generic.product_category_id"
					+ "	inner join public.m_frequency freq on freq.id=presFreqDtl.frequency_id"
					+ "	inner join public.m_status sts on sts.id=issueDtl.status_id"
					+ " where issueDtl.issue_id=:id  and issueDtl.is_substitute=true ";

			Query query = getSession().createSQLQuery(sql).addScalar("issueDtlId").addScalar("itemName")
					.addScalar("pickedQuantity").addScalar("productCategoryId").addScalar("itemCode")
					.addScalar("assetCategoryId").addScalar("genericId").addScalar("genericName").addScalar("frequency")
					.addScalar("tax").addScalar("manufactureId").addScalar("dose").addScalar("startDate")
					.addScalar("stopDate").addScalar("interventionFlag").addScalar("taxPercentage")
					.addScalar("issuedQuantity").addScalar("statusId").addScalar("status").addScalar("manufacturer")
					.addScalar("parentSubstituteId").addScalar("parentIssueDetailsId").addScalar("prescriptionDetailid")
					.addScalar("quantityOrdered").addScalar("quantityIssued").addScalar("taxId").addScalar("batchid")
					.addScalar("uomTypeId").addScalar("uomUnitId").addScalar("itemId").addScalar("netAmount")
					.addScalar("creditPayable").addScalar("selfPayable").addScalar("discountAmount").addScalar("rate")
					.addScalar("taxAmount").addScalar("consessionPercentage").addScalar("consessionAmount")
					.addScalar("totalAmount");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));
			return query.list();
		} catch (Exception e) {
		logger.error("getListOfNarcoticIssuesSubstitutes failed", e);
		return null;
		}
	}

	@Override
	public List<SubstituteItemDto> getListOfNarcoticIssuesWithoutPickerSubstitutes(Integer id) {
		try {

			String sql = "select " + "    issueDtl.id as \"issueDtlId\"," + "    itm.name as \"itemName\" ,"
					+ "    itm.code as \"itemCode\"," + "    generic.id as \"genericId\","
					+ "    generic.name as \"genericName\"," + "    freq.description as \"frequency\","
					+ "    presFreqDtl.dose as \"dose\"," + "    presFreqDtl.start_date as \"startDate\","
					+ "    presFreqDtl.stop_date as \"stopDate\"," + "    issueDtl.status_id as \"statusId\","
					+ "    assignDtl.picked_quantity as \"pickedQuantity\","
					+ "    sts.status as \"status\", manufac.name as \"manufacturer\", "
					+ "    presDtl.id as \"prescriptionDetailid\"," + "    presDtl.item_id as \"itemId\","
					+ "    presDtl.parent_substitute_id as \"parentSubstituteId\","
					+ "    issueDtl.parent_issue_detail_id  as \"parentIssueDetailsId\","
					+ "  issueDtl.is_from_assign_picker as \"isFromAssignPicker\","
					+ "    presDtl.is_intervention as \"interventionFlag\", itm.manufacturer_id as \"manufactureId\","
					+ "    presDtl.order_qty as \"quantityOrdered\","
					+ "    issueDtl.issue_quantity as \"quantityIssued\", saledtl.rate as \"rate\","
					+ "    issueDtl.issue_quantity as \"issuedQuantity\", issueDtl.uom_type_id as \"uomTypeId\" , issueDtl.uom_unit_id as \"uomUnitId\" ,"
					+ " issueDtl.batch_id as \"batchid\", "
//					+ " itm.product_category_id as \"productCategoryId\","
					+ " generic.product_category_id as \"productCategoryId\", "
					+ " prod.asset_category_id as \"assetCategoryId\", "
					+ " saledtl.consession_percentage as \"consessionPercentage\","
					+ "    issueDtl.is_narcotic_substitute as \"isNarcoticSubstitute\", tx.name as \"tax\", "
					// + " tx.tax_percentage as \"taxPercentage\", "
					+ " saledtl.net_amount as \"netAmount\", saledtl.credit_payable as \"creditPayable\", saledtl.self_payable as \"selfPayable\","
					+ "  issueDtl.discount_amount as \"discountAmount\",  saledtl.tax_amount as \"taxAmount\", "
					+ "  tx.tax_percentage as \"taxPercentage\", saledtl.total_amount as \"totalAmount\","
					+ "    itm.s_tax_id as \"taxId\",assignDtl.picked_quantity as \"pickedQuantity\" "
					+ "    from pharmacy.t_issue_detail issueDtl "
					+ "    left join pharmacy.t_assign_picker_detail assignDtl on assignDtl.id=issueDtl.assign_picker_detail_id"
					+ "    inner join pharmacy.t_prescription_detail presDtl on presDtl.id=issueDtl.prescription_detail_id"
					+ "    inner join pharmacy.t_prescription_frequency_detail presFreqDtl on presDtl.id=presFreqDtl.prescription_detail_id"
					+ "    left join pharmacy.t_issue_detail_batch_mapper bthmppr on issueDtl.id=bthmppr.issue_detail_id"
					+ "    left join pharmacy.t_sale_detail saledtl on saledtl.id=issueDtl.sale_detail_id "
					+ "    inner join public.m_item itm on itm.id=presDtl.item_id "
					+ " inner join public.m_tax tx on itm.s_tax_id=tx.id"
					+ " left join public.m_manufacturer manufac on itm.manufacturer_id=manufac.id"
					+ "    inner join public.m_generic generic on itm.generic_id=generic.id"
					+ "    inner join public.m_product_category prod on prod.id=generic.product_category_id"
					+ "    inner join public.m_frequency freq on freq.id=presFreqDtl.frequency_id"
					+ "    left join public.m_status sts on sts.id=assignDtl.status_id"
					+ " where issueDtl.issue_id=:id  and issueDtl.is_substitute=true ";

			Query query = getSession().createSQLQuery(sql).addScalar("issueDtlId").addScalar("itemName")
					.addScalar("pickedQuantity").addScalar("productCategoryId").addScalar("itemCode")
					.addScalar("assetCategoryId").addScalar("genericId").addScalar("genericName").addScalar("frequency")
					.addScalar("tax").addScalar("manufactureId").addScalar("dose").addScalar("startDate")
					.addScalar("stopDate").addScalar("interventionFlag").addScalar("taxPercentage")
					.addScalar("consessionPercentage").addScalar("issuedQuantity").addScalar("statusId")
					.addScalar("status").addScalar("manufacturer").addScalar("parentSubstituteId")
					.addScalar("parentIssueDetailsId").addScalar("prescriptionDetailid").addScalar("quantityOrdered")
					.addScalar("quantityIssued").addScalar("taxId").addScalar("batchid").addScalar("uomTypeId")
					.addScalar("uomUnitId").addScalar("itemId").addScalar("netAmount").addScalar("creditPayable")
					.addScalar("selfPayable").addScalar("discountAmount").addScalar("rate").addScalar("taxAmount")
					.addScalar("totalAmount");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));
			return query.list();
		} catch (Exception e) {
		logger.error("getListOfNarcoticIssuesWithoutPickerSubstitutes failed", e);
		return null;
		}

	}

	@Override
	public PrescriptionDetailsForOPSalesWorklistDto getPresDtlsByPresIdForWalkinSales(Integer prescriptionId,
			Integer itemId) {
		try {
			String sql = "select CAST(presDtl.id as int) as \"prescriptionDetailsid\","
					+ " presDtl.organization_id  as \"organizationId\"," + " presDtl.unit_id  as \"unitId\","
					+ " presDtl.prescription_id  as \"prescriptionId\"," + " presDtl.item_id as \"itemId\","
					+ " presDtl.store_id as \"storeId\"," + " presDtl.uom_unit_id as \"uomUnitId\","
					+ " presDtl.order_qty as \"orderQty\"," + " presDtl.strength as \"strength\","
					+ " presDtl.prescription_date as \"prescriptionDate\","
					+ " presDtl.authorised_by  as \"authorisedBy\"," + " presDtl.authorised_date as \"authorisedDate\","
					+ " presDtl.is_pre_stop as \"isPreStop\"," + " presDtl.is_intervention as \"isIntervention\","
					+ " presDtl.updated_by as \"updatedBy\","
					+ " to_char(presDtl.updated_date,'YYYY-MM-DD HH:mm:ss') as \"updatedDate\","
					+ " presDtl.added_by as \"addedBy\"," + " presDtl.added_date as \"addedDate\","
					+ " presDtl.is_deleted as \"isDeleted\"," + " presDtl.is_active as \"isActive\","
					+ " presDtl.status_id as \"statusId\"," + " presDtl.remark as \"remark\","
					+ " presDtl.prnflag as \"prnflag\"," + " presDtl.statflag as \"statflag\","
					+ " presDtl.infusionflag as \"infusionflag\","
					+ " to_char(presDtl.pre_stop_start_date,'YYYY-MM-DD HH:mm:ss') as \"preStopStartDate\","
					+ " to_char(presDtl.pre_stop_end_date,'YYYY-MM-DD HH:mm:ss') as \"preStopEndDate\","
					+ " presDtl.instruction_id as \"instructionId\","
					+ " 	presDtl.instruction_desc as \"instructionText\","
					+ " presDtl.is_non_formulary as \"isNonFormulary\"," + " presDtl.generic_name as \"genericName\","
					+ " presDtl.item_name as \"itemName\"," + " presDtl.justification_stop as \"justificationStop\","
					+ " presDtl.is_medication_stop as \"isMedicationStop\","
					+ " presDtl.parent_substitute_id as \"parentSubstituteId\"," + " preFreDtl.dose as \"dose\","
					+ " preFreDtl.dose_unit_id as \"doseUnitId\"," + " preFreDtl.frequency_id as \"frequencyId\","
					+ " preFreDtl.route_id as \"routeId\"," + " preFreDtl.start_date as \"startDate\","
					+ " preFreDtl.stop_date as \"stopDate\"," + " preFreDtl.instructions as \"instructions\","
					+ " itm.is_narcotic as \"isNarcotic\" " + " from pharmacy.t_prescription_detail presDtl"
					+ " INNER JOIN pharmacy.t_prescription pres on pres.id=presDtl.prescription_id"
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id "
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " where presdtl.prescription_id=:id " + " and presdtl.item_id=:itemId ";

			Query query = getSession().createSQLQuery(sql).addScalar("prescriptionDetailsid")
					.addScalar("organizationId").addScalar("unitId").addScalar("itemId").addScalar("storeId")
					.addScalar("uomUnitId").addScalar("orderQty").addScalar("strength").addScalar("prescriptionId")
					.addScalar("prescriptionDate").addScalar("authorisedBy").addScalar("authorisedDate")
					.addScalar("instructionText").addScalar("isDeleted", new TrueFalseType())
					.addScalar("isActive", new TrueFalseType()).addScalar("statusId").addScalar("remark")
					.addScalar("prnflag", new TrueFalseType()).addScalar("statflag", new TrueFalseType())
					.addScalar("infusionflag", new TrueFalseType()).addScalar("instructionId")
					.addScalar("isNonFormulary").addScalar("genericName").addScalar("itemName")
					.addScalar("justificationStop").addScalar("isMedicationStop").addScalar("parentSubstituteId")
					.addScalar("dose").addScalar("doseUnitId").addScalar("frequencyId").addScalar("routeId")
					.addScalar("startDate").addScalar("stopDate").addScalar("instructions")
					.addScalar("isNarcotic", new TrueFalseType());
			query.setParameter("id", prescriptionId);
			query.setParameter("itemId", itemId);
			query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsForOPSalesWorklistDto.class));
			return (PrescriptionDetailsForOPSalesWorklistDto) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getPresDtlsByPresIdForWalkinSales failed", e);
		return null;
		}
	}

	@Override
	public List<SubstituteItemDto> getListOfSubstitutesForOpSales(Long prescriptionId) {
		try {
			/*
			 * String sql = " select" + "	presDtl.id as \"prescriptionDetailid\"," +
			 * "	presDtl.parent_substitute_id as \"parentSubstituteId\"," +
			 * "	itm.id as \"itemId\"," + "	itm.code as \"itemCode\"," +
			 * "	itm.name as \"itemName\"," + "	generic.id as \"genericId\"," +
			 * "	generic.name as \"genericName\"," +
			 * "	manufacture.id as \"manufactureId\"," +
			 * "	manufacture.name as \"manufacturerName\"," +
			 * "	presDtl.order_qty as \"qtyPrescribed\"," + "	itm.is_otc as \"otc\","
			 * + "	tax.id as \"taxId\"," + "	tax.name as \"tax\"," +
			 * "	tax.tax_percentage as \"taxPercentage\"," +
			 * "	status.id as \"statusId\"," + "	status.status as \"status\"," +
			 * "	instruction.instruction_desc as \"instructionDesc\"," +
			 * "	itm.id as \"productCategoryId\", " // +
			 * " saledtl.consession_percentage as \"consessionPercentage\"," +
			 * " saledtl.consession_percentage as \"consessionPercentage\", " +
			 * " saledtl.consession_amount as \"consessionAmount\", " +
			 * "	saledtl.net_amount as \"netAmount\"," +
			 * "	saledtl.credit_payable as \"creditPayable\", " +
			 * "	saledtl.self_payable as \"selfPayable\"," +
			 * "	saledtl.discount_amount as \"discountAmount\", " +
			 * "	saledtl.rate as \"rate\", " + "	saledtl.tax_amount as \"taxAmount\", " +
			 * "	saledtl.tax_percentage as \"taxPercentage\", " +
			 * "	saledtl.quantity as \"newDispQty\"," +
			 * " saledtl.batch_id as \"batchID\"," +
			 * "	saledtl.uom_type_id as \"uomTypeId\", saledtl.uom_unit_id as \"uomUnitId\" ,"
			 * + "	saledtl.total_amount as \"totalAmount\"," + "	coalesce(" +
			 * "		assetcateg7_.id," + "		assetcateg8_.id" +
			 * "	) as \"assetCategoryId\"" + " from" +
			 * "	pharmacy.t_prescription_detail presDtl" +
			 * " inner join public.m_item itm on" + "	presDtl.item_id = itm.id" +
			 * " inner join public.m_generic generic on" + "	itm.generic_id = generic.id"
			 * + " left outer join public.m_product_category productcat6_ on" +
			 * "	generic.product_category_id = productcat6_.id" +
			 * " left outer join public.m_asset_category assetcateg8_ on" +
			 * " 	productcat6_.asset_category_id = assetcateg8_.id" +
			 * " left join public.m_manufacturer manufacture on" +
			 * "	itm.manufacturer_id = manufacture.id" + " left join public.m_tax tax on"
			 * + "	itm.s_tax_id = tax.id" +
			 * " left outer join public.m_product_category productcat5_ on" +
			 * "	itm.product_category_id = productcat5_.id" +
			 * " left outer join public.m_asset_category assetcateg7_ on" +
			 * "	productcat5_.asset_category_id = assetcateg7_.id" +
			 * " left join public.m_status status on" + "	presDtl.status_id = status.id" +
			 * " left outer join public.m_instruction instruction on" +
			 * "	presDtl.instruction_id = instruction.instruction_id" +
			 * " left join pharmacy.t_prescription prescription on" +
			 * "	presDtl.prescription_id = prescription.id" + "	and(" +
			 * "		prescription.is_active = true" +
			 * "		and prescription.is_deleted = false" + "	) " +
			 * " left join pharmacy.t_sale_detail saledtl on saledtl.prescription_detail_id=presDtl.id "
			 * + " where" + "	prescription.id=:id" + "	and presDtl.is_active = true" +
			 * "	and presDtl.is_deleted = false" +
			 * "	and presDtl.parent_substitute_id is not null" + " order by" +
			 * "	presDtl.id ";
			 */
			String sql = "		  " + " select  " + "	distinct(presDtl.id) as \"prescriptionDetailid\",  "
					+ "	presDtl.parent_substitute_id as \"parentSubstituteId\",  " + "	itm.id as \"itemId\",  "
					+ "	itm.code as \"itemCode\",  " + "	itm.name as \"itemName\",  "
					+ " itm.is_three_uom_applicable as \"isThreeUOMApplicable\", "
                    + " itm.storage_unit_id as \"storageUnitId\","
                    + " itm.is_lasa as \"isLasa\","
                    + " storageUnit.name as \"storageUnitName\", "
					+ "	generic.id as \"genericId\",  " + "	generic.name as \"genericName\",  "
					+ "	manufacture.id as \"manufactureId\",  " + "	manufacture.name as \"manufacturerName\",  "
					+ "	presDtl.order_qty as \"qtyPrescribed\",  " + "	itm.is_otc as \"otc\",  "
					+ "	tax.id as \"taxId\",  " + "	tax.name as \"tax\",  "
					+ "	tax.tax_percentage as \"taxPercentage\",  " + "	status.id as \"statusId\",  "
					+ "	status.status as \"status\",  " + "	instruction.instruction_desc as \"instructionDesc\",  "
					+ "	itm.id as \"productCategoryId\",  " + "	pc1.percentage as \"consessionPercentage\",  "
					+ "	saledtl.consession_amount as \"consessionAmount\",  "
					+ "	saledtl.net_amount as \"netAmount\",  " + "	saledtl.credit_payable as \"creditPayable\",  "
					+ "	saledtl.self_payable as \"selfPayable\",  "

					+ "   f.description as \"frequencyDesc\", " + "   fd.start_date as \"startDate\", "
					+ "   fd.stop_date as \"stopDate\", "

					+ "	saledtl.discount_amount as \"discountAmount\", saledtl.package_consumption_amt as \"packageConsumptionAmt\",  " + "	saledtl.rate as \"rate\",  "
					+ "	saledtl.tax_amount as \"taxAmount\",  " + "	saledtl.tax_percentage as \"taxPercentage\",  "
					+ "	saledtl.quantity as \"newDispQty\",  " + "	saledtl.batch_id as \"batchID\",  "
					+ "	saledtl.uom_type_id as \"uomTypeId\",  " + "	saledtl.uom_unit_id as \"uomUnitId\",  "
					+ "	saledtl.total_amount as \"totalAmount\",  " + "	coalesce(  " + "		assetcateg7_.id,  "
					+ "		assetcateg8_.id  " + "	) as \"assetCategoryId\" , saledtl.at_cop as \"atCop\"  " + " from  "
					+ "	pharmacy.t_prescription_detail presDtl  " + " inner join public.m_item itm on  "
					+ "	presDtl.item_id = itm.id  " + " inner join public.m_generic generic on  "
					+ "	itm.generic_id = generic.id  " + " left outer join public.m_product_category productcat6_ on  "
					+ "	generic.product_category_id = productcat6_.id  "
					+ " left outer join public.m_asset_category assetcateg8_ on  "
					+ "	productcat6_.asset_category_id = assetcateg8_.id  "
					+ " left join public.m_manufacturer manufacture on  " + "	itm.manufacturer_id = manufacture.id  "
					+ " left join public.m_tax tax on  " + "	itm.s_tax_id = tax.id  "
					+ " left outer join public.m_product_category productcat5_ on  "
					+ "	itm.product_category_id = productcat5_.id  "
					+ " left outer join public.m_asset_category assetcateg7_ on  "
					+ "	productcat5_.asset_category_id = assetcateg7_.id  " + " left join public.m_status status on  "
					+ "	presDtl.status_id = status.id  " + " left outer join public.m_instruction instruction on  "
					+ "	presDtl.instruction_id = instruction.instruction_id  "
					+ " left join pharmacy.t_prescription prescription on  "
					+ "	presDtl.prescription_id = prescription.id  " + "	and(  "
					+ "		prescription.is_active = true  " + "		and prescription.is_deleted = false  " + "	)  "
					+ " left join pharmacy.t_sale_detail saledtl on  "
					+ "	saledtl.prescription_detail_id = presDtl.id  " + "	  "
					+ " inner join opd.t_encounter_master em on  " + "	em.encounter_id = prescription.encounter_id  "
					+ " left join billing.m_patient_type_group_wise_details pc1 on  "
					+ "	pc1.item_id = presDtl.item_id  " + "	and pc1.patient_type_id = em.patient_class_id  "
					+ "    left join pharmacy.t_prescription_frequency_detail fd on fd.prescription_detail_id = presDtl.id  "
                    + "    left join public.m_storage_unit storageUnit on storageUnit.id =  itm.storage_unit_id "
					+ "   left join public.m_frequency f on f.id =  fd.frequency_id "

					+ " where  " + "	prescription.id =:id  " + "	and presDtl.is_active = true  "
					+ "	and presDtl.is_deleted = false  " + "	and presDtl.parent_substitute_id is not null and saledtl.is_active=true  "
					+ " order by  " + "	presDtl.id";

			Query query = getSession().createSQLQuery(sql).addScalar("prescriptionDetailid")
					.addScalar("parentSubstituteId").addScalar("itemId").addScalar("itemName").addScalar("itemCode").addScalar("isThreeUOMApplicable")
                    .addScalar("storageUnitId").addScalar("storageUnitName")
					.addScalar("genericId").addScalar("genericName").addScalar("manufactureId").addScalar("packageConsumptionAmt")
					.addScalar("manufacturerName").addScalar("qtyPrescribed").addScalar("otc").addScalar("taxId")
					.addScalar("tax").addScalar("taxPercentage").addScalar("productCategoryId").addScalar("netAmount")
					.addScalar("selfPayable").addScalar("consessionPercentage").addScalar("consessionAmount")
					.addScalar("creditPayable").addScalar("newDispQty").addScalar("batchID").addScalar("uomTypeId")
					.addScalar("uomUnitId").addScalar("discountAmount").addScalar("rate").addScalar("taxAmount")
					.addScalar("statusId").addScalar("status").addScalar("instructionDesc").addScalar("totalAmount")
					.addScalar("assetCategoryId");
			query.setParameter("id", prescriptionId);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteItemDto.class));
			return query.list();
		} catch (Exception e) {
		logger.error("getListOfSubstitutesForOpSales failed", e);
		return null;
		}
	}

	@Override
	public Response getPatientNameSearchByName(PatientDetailRequest patientDetailsSearch) {
		try {

			String query = "select " + " distinct(pat.patient_id) as \"patientId\","
					+ " pat.uhid_number as \"uhidNumber\" ,"
					+ " concat( coalesce( pre.prefix_code, '' ), ' ', coalesce( pat.first_name, ' ' ), ' ', coalesce( pat.middle_name, ' ' ), ' ', coalesce( pat.last_name )) as \"patientName\","
					+ " concat( coalesce( pat.uhid_number, '' ), '| ', coalesce( pre.prefix_code, '' ), '. ', coalesce( pat.first_name, ' ' ), ' ', coalesce( pat.middle_name, ' ' ), ' ', coalesce( pat.last_name ), '| ', trim( concat( to_char( extract( year from AGE( current_timestamp, pat.dob )), '999' ))), ' |', gen.gender_code, ' |', pat.mobile_number ) as \"details\""
					+ " from patient.t_patient_registration pat"
					+ " inner join opd.t_encounter_master enc on enc.patient_id = pat.patient_id and enc.status='A'"
					+ " inner join adt.t_admission adm on adm.patient_id = pat.patient_id and adm.status='A'"
					+ " inner join public.m_prefix_master pre on pre.prefix_id = pat.prefix_id "
					+ " inner join public.m_gender_master gen on pat.gender_id = gen.gender_id"
					+ " where pat.status ='A' " + "	 and lower(pat.first_name) like '%"
					+ patientDetailsSearch.getFirstName() + "%'";
			StringBuffer s1 = new StringBuffer(query);
			/*
			 * if(patientDetailsSearch.getEncounterId()!=null) {
			 * s1.append(" and pres.encounter_id = "+patientDetailsSearch.getEncounterId());
			 * }else {
			 * s1.append(" and pres.admission_id = "+patientDetailsSearch.getAdmissionId());
			 * }
			 */
			List<PatientDetailRequest> list = sessionFactory.getCurrentSession().createSQLQuery(s1.toString())
//					.setParameter("unitId",sessionService.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientDetailRequest.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPatientNameSearchByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	// start code for HIS-9505 GRN form asset against PO issues - Approve quantity
	// is showing 0
	@Override
	public List<PoAccessoriesMapperGlobal> getPoAccessoriesMapperBP(List<Integer> Ids) {
		String hql = "SELECT poAccessoriesMapper. id as id, "
				+ "item.id as itemId, item.name as itemName, item.code as itemCode, poAccessoriesMapper.purchaseOrderDetail.id as poDetailId "
				+ " FROM PoAccessoriesMapper AS poAccessoriesMapper " + " LEFT JOIN poAccessoriesMapper.item as item "
				+ " WHERE poAccessoriesMapper.purchaseOrderDetail.id in (:id)  AND poAccessoriesMapper.isActive = true AND poAccessoriesMapper.isDeleted = false "
				+ " ORDER BY poAccessoriesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", Ids);
		query.setResultTransformer(Transformers.aliasToBean(PoAccessoriesMapperGlobal.class));
		return query.list();
	}
	// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
	// showing 0

	@Override
	public Integer getVendorCop(Integer vendorId, Integer id) {

		try {
			String sql = " select qfdetails.vendor_cop   from  procurement.t_quotation_fill_detail qfdetails  join procurement.t_quotation_fill qf  on qfdetails.quotation_fill_id=qf.id where qfdetails.qr_detail_id ="
					+ id + " AND qf.vendor_id =" + vendorId;

			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			// query.setParameter("id", id);
			Double q = (Double) query.uniqueResult();
			return q.intValue();
		} catch (Exception e) {
		logger.error("getVendorCop failed", e);
		return null;
		}

	}

	@Override
	public List<QuotationVendor> getQuotationVendorSearchList(AgainstPurchaseRequest quotationFillRequest,
			List<Integer> statusIds, Integer unitId) {

		String hql = "SELECT quotationVendor FROM QuotationVendor AS quotationVendor "
				+ "	INNER JOIN quotationVendor.addedBy as user  " + "	LEFT JOIN quotationVendor.store as store  "
				+ " INNER JOIN quotationVendor.status AS status "
				+ " INNER JOIN quotationVendor.quotationRequest AS quotationRequest "
				+ " INNER JOIN quotationVendor.vendor AS vendor "
				+ " INNER JOIN quotationRequest.assetType AS assetType "
				+ " INNER JOIN quotationVendor.approvalStatus as approvalStatus "
				+ " WHERE quotationVendor.isActive = true AND quotationVendor.isDeleted = false AND quotationVendor.unit.unitId = :unitId "
				+ " 	AND quotationVendor.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "   	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ " 	AND (:vqNumber = '' OR lower(quotationVendor.vqNumber) LIKE :vqNumber) "
				+ "      AND quotationVendor.id NOT IN (SELECT qf.quotationVendor.id FROM QuotationFill AS qf "
				+ "			      					    WHERE qf.vendor.id = :vendorId AND qf.approvalStatus.id=5)"
				+ "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId)"
				+ "     AND (approvalStatus.id IN (:approvalStatusId)) " + " ORDER BY quotationVendor.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(quotationFillRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(quotationFillRequest.getToDate()));
		query.setParameter("storeId",
				quotationFillRequest.getStoreId() != null ? quotationFillRequest.getStoreId() : 0);
		query.setParameter("vendorId",
				quotationFillRequest.getVendorId() != null ? quotationFillRequest.getVendorId() : 0);
		query.setParameter("vqNumber",
				quotationFillRequest.getVqrNumber() != null ? quotationFillRequest.getVqrNumber().toLowerCase() + '%'
						: "");
		query.setParameter("assetTypeId",
				quotationFillRequest.getAssetTypeId() != null ? quotationFillRequest.getAssetTypeId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();

	}

	@Override
	public Long getLastMonthSaleNew(Integer storeId, Integer itemId, Integer unitId) {

		/*
		 * String hql =
		 * "SELECT COALESCE(SUM(saleDetail.leaseQuantity), 0) FROM SaleDetail AS saleDetail "
		 * +
		 * "		INNER JOIN saleDetail.sale AS sale WITH sale.isDeleted = false AND sale.isActive = true AND sale.unit.unitId = :unitId "
		 * +
		 * "		INNER JOIN sale.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
		 * + "		INNER JOIN saleDetail.batch AS batch " +
		 * "		INNER JOIN batch.item AS item WITH item.id = :itemId " +
		 * "WHERE saleDetail.isDeleted = false AND saleDetail.isActive = true AND saleDetail.unit.unitId = :unitId "
		 * +
		 * "		AND date_trunc('month', saleDetail.addedDate) = date_trunc('month', current_date ) "
		 * ;
		 */

		String sql = " select coalesce(sum(saledetail0_.lease_quantity), 0)  "
				+ "from pharmacy.t_sale_detail saledetail0_ inner join pharmacy.t_sale sale1_ on saledetail0_.sale_id=sale1_.id  "
				+ "and (sale1_.is_deleted=false and sale1_.is_active=true and sale1_.unit_id=:unitId)  "
				+ "inner join public.m_store store2_ on sale1_.store_id=store2_.store_id and (store2_.store_id=:storeId  "
				+ "and store2_.is_deleted=false and store2_.is_active=true) inner join inventory.t_batch batch3_ on saledetail0_.batch_id=batch3_.id "
				+ "inner join public.m_item item4_ on batch3_.item_id=item4_.id and item4_.id =:itemId  "
				+ "where saledetail0_.is_deleted=false and saledetail0_.is_active=true and saledetail0_.unit_id=:unitId "
				+ "and date_trunc('month', saledetail0_.added_date)=date_trunc('month', current_date - interval '1' month)  ";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		BigInteger data = (BigInteger) query.uniqueResult();
		return data.longValue();

	}

	@Override
	public List<StoreIndent> getStoreIndentListWithDate(Integer fromStoreId, Integer toStoreId,
			DateRequest dateRequest) {

		String hql = "SELECT storeIndent FROM StoreIndent AS storeIndent "
				+ "		LEFT JOIN storeIndent.fromStore as fromStore "
				+ "		LEFT JOIN storeIndent.toStore as toStore  "
				+ "WHERE  storeIndent.addedDate BETWEEN :fromDate AND :toDate  AND  storeIndent.isActive = true AND storeIndent.isDeleted = false AND storeIndent.unit.unitId = :unitId "
				+ "		AND fromStore.storeId = :fromStoreId AND toStore.storeId = :toStoreId "
				+ "		AND (storeIndent.approvalStatus.id = 5 OR storeIndent.approvalStatus.id = 6) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromStoreId", fromStoreId);
		query.setParameter("toStoreId", toStoreId);
		query.setParameter("unitId",
				dateRequest.getUnitId() != null ? dateRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("fromDate", Util.getDate(dateRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(dateRequest.getToDate()));
		return query.list();

	}

	@Override
	public Admission admissionPatientForNursing(PatientDetailRequest patientDetailsSearch, Integer unitId) {

		try {
			String hql = " SELECT admission FROM Admissions AS admission "
					+ " INNER JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " INNER JOIN admission.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN admission.doctor AS doctor WITH doctor.status = 'A' "
					+ " INNER JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " WHERE admission.status = 'A' AND admissionDetail.isDischarge != 'D' AND admissionDetail.nursingAcceptance = 'Y'  AND admission.unit.unitId = :unitId "
					+ " AND ( patient.id = :patientId ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) ";
			Query query = getSession().createQuery(hql);
			query.setParameter("visitType",
					patientDetailsSearch.getVisitType() != null ? patientDetailsSearch.getVisitType() : 0);
			query.setParameter("patientId", patientDetailsSearch.getPatientId());
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

			return (Admission) query.uniqueResult();
		} catch (Exception e) {
		logger.error("admissionPatientForNursing failed", e);
		return null;
		}

	}

	@Override
	public Doctor GetDoctorById(Integer doctorId) {

		String hql = "SELECT doctor  FROM Doctor AS doctor  where doctor.status = 'A'   AND doctor.doctorId = :id ";

		Query query = getSession().createQuery(hql);

		query.setParameter("id", doctorId);

		return (Doctor) query.uniqueResult();

	}

	@Override
	public DoctorSpecialityResp getDoctorSpecialityResp(Integer doctorId, Integer unitId) {
		String hql = "SELECT speciality.specialityName as speciality, doctor.doctorId as doctorId "
				+ " FROM DoctorSpeciality AS doctorSpeciality "
				+ " INNER JOIN doctorSpeciality.doctor AS doctor WITH doctor.status = 'A' "
				+ " INNER JOIN doctorSpeciality.speciality AS speciality WITH speciality.status = 'A' "
				+ " WHERE  doctorSpeciality.status = 'A' AND doctorSpeciality.unitId = :unitId "
				+ " AND (doctor.id =:id )";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", doctorId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityResp.class));
		if (query.list().isEmpty()) {
			return null;
		} else {
			return (DoctorSpecialityResp) query.list().getFirst();
		}

	}

	/*
	 * start code for resolved HIS-10253 Store Stock Dashboard - Billable
	 * Consumption -
	 */
	@Override
	public BatchResponse1 getUpdatedBatchDetails(int storeId, BigInteger batchId, Integer unitId) {

		String hql1 = "  select details.new_mrp as mrp , details.new_batch_no as batchno  , details.new_batch_expiry as expiry "
				+ " from  inventory.t_batch_exp_detail details "
				+ " join inventory.t_batch_exp batchexp on details.batch_expiry_id = batchexp.id "
				+ " where batchexp.store_id =:storeId and details.batch_id =:batchId and batchexp.unit_id =:unitId ";

		Query query = getSession().createSQLQuery(hql1);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId.intValue());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse1.class));

		if (query.list().size() > 0) {
			return (BatchResponse1) query.list().getFirst();
		} else {
			return null;
		}
	}
	/*
	 * end code for resolved HIS-10253 Store Stock Dashboard - Billable Consumption
	 * -
	 */

	@Override
	public List<QuotationRequestDetail> getQuotationRequestDetailList(Integer qrId, Integer vendorId) {
		/*
		 * String hqlNew
		 * =" select quotationRequestDetail from  QuotationFillDetail as quotationFillDetail  join quotationRequestDetail  "
		 * + " inner join  quotationRequestDetail.quotationRequest as quotationRequest "
		 * +
		 * " WHERE quotationRequestDetail.quotationRequest.id = :qrId AND quotationFillDetail.quotationFill.vendor.id = : vendorId  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
		 * ;
		 * 
		 * Query query = getSession().createQuery(hqlNew); query.setParameter("qrId",
		 * qrId); query.setParameter("vendorId", vendorId); return query.list();
		 */

		String hql = "SELECT qrDetail FROM QuotationVendorDetail AS quotationVendorDetail "
				+ "INNER JOIN quotationVendorDetail.qrDetail AS qrDetail INNER JOIN qrDetail.quotationRequest AS quotationRequest "
				+ " WHERE  quotationRequest.id =: qrId"
				+ " AND quotationVendorDetail.isActive = true AND quotationVendorDetail.isDeleted = false AND quotationVendorDetail.quotationVendor.vendor.id =:vendorId "
				+ " ORDER BY quotationVendorDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("qrId", qrId);
		query.setParameter("vendorId", vendorId);
		return query.list();

	}

	@Override
	public List<Integer> getQuotationRequestDetailIds(Integer id, Integer vendorId) {
		String sql = " select reqDetail.id from procurement.t_quotation_fill_detail as  filldetail  join procurement.t_quotation_fill as qfill "
				+ "  on filldetail.quotation_fill_id = qfill.id "
				+ "  join procurement.t_quotation_request_detail reqDetail "
				+ "  on filldetail.qr_detail_id = reqDetail.id "
				+ "  join procurement.t_quotation_request as qrequest on reqDetail.qr_id=qrequest.id "
				+ "  where qfill.vendor_id=" + vendorId + " and qrequest.id=" + id
				+ " and qrequest.is_active=true and qrequest.is_deleted=false ";
		Query query = getSession().createSQLQuery(sql);
		return query.list();
	}

	@Override
	public List<QuotationRequestDetail> getQuotationRequestDetailListByIds(List<Integer> quotationRequestDetailIds) {

		String hql = "SELECT quotationRequestDetail FROM QuotationRequestDetail AS quotationRequestDetail "
				+ " WHERE  quotationRequestDetail.id  in (:ids)"
				+ " AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("ids", quotationRequestDetailIds);
		return query.list();

	}

	@Override
	public Prescription getPrescriptionData(Integer prescriptionId) {
		try {

			String hql = "  SELECT prescription FROM Prescription AS prescription "
					+ " WHERE prescription.isActive = true AND prescription.isDeleted = false "
					+ " AND prescription.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", prescriptionId.longValue());

			return (Prescription) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getPrescriptionData failed", e);
		return null;
		}

	}

	@Override
	public PrescriptionDetail getListPrescriptionDetails(Integer prescriptionDetailId) {
		try {

			String hql = "  SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " WHERE prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " AND prescriptionDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", prescriptionDetailId.longValue());

			return (PrescriptionDetail) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getListPrescriptionDetails failed", e);
		return null;
		}

	}

	@Override
	public List<UserMasterDto> inWardBySearchUser(Integer organizationId, Integer unitId, String name) {
		try {
			List<UserMasterDto> userMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USERS_LIST_WITH_USERNAME").setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("userName", name != null ? name.toLowerCase().trim() + '%' : "")
					.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();

			return userMasterDtosList;
		} catch (Exception e) {
		logger.error("inWardBySearchUser failed", e);
		return null;
		}
	}

	@Override
	public BigInteger getClubBatchWiseAvailableStockWithoutLot(String batchNo, Boolean isConsignment, Integer itemId,
			Integer storeId, Integer unitId) {

		// and b.item_id=995 and s.store_id=3
		BigInteger t = BigInteger.valueOf(0);
		// String sql = " select SUM(S.available_qty) from inventory.t_batch b inner
		// join inventory.t_store_stock S on B.id=S.batch_id where
		// B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no
		// =:lotno";
		String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where  B.batch_no=:batchNo "
				+ " and b.item_id =:itemId and s.store_id =:storeId  and "
				+ " S.available_qty is not null  and S.total_qty is not null and S.unit_id=:unitId "
				+ " and b.unit_id=:unitId ";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("unitId", unitId);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);

		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}

	}

	@Override
	public List<ReasonMaster> getCancelReasonDropdownList(int orgId) {

		String hql = "SELECT reasonMaster FROM ReasonMaster AS reasonMaster " + "WHERE reasonMaster.status = 'A' "
				+ "and reasonTypeId =:reasonTypeId " + "and organizationId=:orgId";

		Query query = getSession().createQuery(hql);
		query.setParameter("reasonTypeId", 15);
		query.setParameter("orgId", orgId);
		return query.list();

	}

	public Response updatePharamcyBillPayees(List<PharamcyBillPayeeDetails> listToBeUpdated) {
		try {
			for (PharamcyBillPayeeDetails billPayeeDetails : listToBeUpdated) {
				sessionFactory.getCurrentSession().update(billPayeeDetails);
			}
			return null;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}

	}

	@Override
	public StoreItemMapper getItemMinMaxData(int itemId, int unitId, int storeId) {
		String hql = "SELECT storeItemMapper FROM StoreItemMapper AS storeItemMapper "
				+ "WHERE storeItemMapper.item.id = :id AND storeItemMapper.unit.unitId =:unitId "
				+ "AND storeItemMapper.store.storeId =:storeId AND storeItemMapper.isActive = true AND storeItemMapper.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", itemId);
		query.setParameter("unitId", unitId);
		query.setParameter("storeId", storeId);

		return (StoreItemMapper) query.uniqueResult();
	}

	@Override
	public ReceiverResponse getReceiverDetails(Long id, Integer unitId) {

		String hql = " select mh.receiver_name as  receivername, mh.receiver_contact_details contactdetail , r.relation_id relationid , r.relation_name as relationname , mh.prescription_id  as prescriptionid , mh.id as id "
				+ " from pharmacy.t_medication_handover mh  "
				+ " join  pharmacy.t_prescription p on mh.prescription_id=p.id "
				+ " join public.m_relation_master r on mh.relationship_id=r.relation_id "
				+ " where mh.prescription_id =:id and mh.unit_id =:unitId " + " order by mh.id desc " + " limit 1 ";
		Query query = getSession().createSQLQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId);
		query.setResultTransformer(Transformers.aliasToBean(ReceiverResponse.class));
		return (ReceiverResponse) query.uniqueResult();

	}

	@Override
	public List<AssetIndent> getAssetIndentListWithDate(Integer fromStoreId, Integer toStoreId,
			DateRequest dateRequest) {

		String hql = "SELECT storeIndent FROM AssetIndent AS storeIndent "
				+ "		LEFT JOIN storeIndent.fromStore as fromStore "
				+ "		LEFT JOIN storeIndent.toStore as toStore  "
				+ "WHERE  storeIndent.addedDate BETWEEN :fromDate AND :toDate  AND  storeIndent.isActive = true AND storeIndent.isDeleted = false AND storeIndent.unit.unitId = :unitId "
				+ "		AND fromStore.storeId = :fromStoreId AND toStore.storeId = :toStoreId "
				+ "		AND (storeIndent.approvalStatus.id = 5 OR storeIndent.approvalStatus.id = 6) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromStoreId", fromStoreId);
		query.setParameter("toStoreId", toStoreId);
		query.setParameter("unitId",
				dateRequest.getUnitId() != null ? dateRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("fromDate", Util.getDate(dateRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(dateRequest.getToDate()));
		return query.list();

	}

	@Override
	public List<AssetIndentDetail> getAssetIndentDetailsList(Integer id) {

		String hql = "SELECT storeIndentDetail FROM AssetIndentDetail AS storeIndentDetail "
				+ "WHERE storeIndentDetail.indent.id = :indentId "
				+ "		AND (storeIndentDetail.status.id = 5 OR storeIndentDetail.status.id = 6 ) "
				+ "		AND ( storeIndentDetail.purchaseRequestDetail is null OR storeIndentDetail.purchaseRequestDetail is not null ) "
				+ "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("indentId", id);
		return query.list();

	}

	@Override
	public AssetIndentDetail getAssetIndentDetail(Integer id) {

		String hql = "SELECT storeIndentDetail FROM AssetIndentDetail AS storeIndentDetail "
				+ "WHERE storeIndentDetail.id = :indentId "
				+ "		AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("indentId", id);
		return (AssetIndentDetail) query.uniqueResult();

	}

	@Override
	public List<ReasonMaster> getreasonDropdownListByTypeId(Integer orgId, Integer reasonTypeId) {

		String hql = "SELECT reasonMaster FROM ReasonMaster AS reasonMaster " + "WHERE reasonMaster.status = 'A' "
				+ "and reasonTypeId =:reasonTypeId " + "and organizationId=:orgId";

		Query query = getSession().createQuery(hql);
		query.setParameter("reasonTypeId", reasonTypeId);
		query.setParameter("orgId", orgId != null && orgId != 0 ? orgId : sessionService.getOrganizationId());
		return query.list();

	}

	@Override
	public Response getBatchDetailsByPresDetailsId(Integer id) {

		try {
			String query = "select" + "					issueDtl.batch_id as \"batchId\","
					+ "					bth.batch_no as \"batchNo\" " + "				from  "
					+ "					pharmacy.t_issue_detail issueDtl  "
					+ "				inner join inventory.t_batch bth on  "
					+ "					bth.id = issueDtl.batch_id  " + "				where  "
					+ "					issueDtl.prescription_detail_id =:prescriptionDetailsId";

			List<BatchDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("prescriptionDetailsId", id)
					.setResultTransformer(Transformers.aliasToBean(BatchDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getBatchDetailsByPresDetailsId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public List<IssueDetail> getIssueDetail(Integer id) {

		try {
			String hql = "  SELECT issueDetail FROM IssueDetail AS issueDetail "
					// + " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper
					// WITH issueDetailBatchMapper.isActive = true AND
					// issueDetailBatchMapper.isDeleted = false "
					// + " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isQuarantine
					// = false AND batch.isDeleted = false AND batch.isActive = true "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false " + " AND issue.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return query.list();
		} catch (Exception e) {
		logger.error("getIssueDetail failed", e);
		return null;
		}

	}

	@Override
	public BigInteger getConsumedQty(Integer patientId, Integer id, Long batchId, Integer issueDetailId) {
		String s = "SELECT COUNT(drug_detail.id) " +
				"FROM pharmacy.t_drug_administration_detail drug_detail " +
				"JOIN pharmacy.t_drug_administration drug ON drug_detail.drug_administration_id = drug.id " +
				"JOIN pharmacy.t_prescription_detail predetails ON drug_detail.prescription_detail_id = predetails.id " +
				"JOIN pharmacy.t_issue_detail iss ON iss.prescription_detail_id = predetails.id " +
				"WHERE drug.patient_id = :patientId AND predetails.item_id = :id ";

		if (batchId != null && batchId != 0L && issueDetailId != null && issueDetailId != 0) {
			s += "AND (drug_detail.batch_id = :batchId OR drug_detail.batch_id IS NULL) " +
					"AND iss.id = :issueDetailId";
		}

		Query query = getSession().createSQLQuery(s);
		query.setParameter("patientId", patientId);
		query.setParameter("id", id);

		if (batchId != null && batchId != 0L && issueDetailId != null && issueDetailId != 0) {
			query.setParameter("batchId", batchId);
			query.setParameter("issueDetailId", issueDetailId);
		}

		return (BigInteger) query.uniqueResult();

	}

	public GoodReceiptNoteDetailDto getLastPouomandMrp(Integer organizationId, Integer unitId, Integer itemId) {

		try {
			String query = "select " + "	pod.po_uom_type_id as \"lastGrnUomTypeId\" ,pod.mrp as \"lastMrp\",pod.cop as \"lastCOP\" "
					+ "from " + "	procurement.t_purchase_order po inner join "
					+ "	procurement.t_purchase_order_detail pod on " + "	po.id = pod.purchase_order_id " + "where "
					+ "	po.unit_id =:unitId " + "	and po.organization_id = :organizationId "
					+ "	and pod.item_id =:itemId " + " order by " + "	po.id desc " + "limit 1";

			GoodReceiptNoteDetailDto dto = (GoodReceiptNoteDetailDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("organizationId", organizationId).setParameter("unitId", unitId)
					.setParameter("itemId", itemId)
					.setResultTransformer(Transformers.aliasToBean(GoodReceiptNoteDetailDto.class)).uniqueResult();
			return dto;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getLastPouomandMrp failed", e);
		return null;
		}
	}

	@Override
	public GoodReceiptNoteDetailDto getLastGrnuomandMrp(Integer organizationId, Integer unitId, Integer itemId) {
		try {
			String query = "select " + "	grnd.grn_uom_type_id as \"lastGrnUomTypeId\" ,grnd.mrp as \"lastMrp\",grnd.cop as \"lastCOP\" "
					+ "from " + "	procurement.t_good_receipt_note grn inner join "
					+ "	procurement.t_good_receipt_note_detail grnd on " + "	grn.id = grnd.grn_id " + "where "
					+ "	grn.unit_id =:unitId " + "	and grn.organization_id = :organizationId "
					+ "	and grnd.item_id =:itemId " + " order by " + "	grnd.id desc " + "limit 1";

			GoodReceiptNoteDetailDto dto = (GoodReceiptNoteDetailDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("organizationId", organizationId).setParameter("unitId", unitId)
					.setParameter("itemId", itemId)
					.setResultTransformer(Transformers.aliasToBean(GoodReceiptNoteDetailDto.class)).uniqueResult();
			return dto;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getLastGrnuomandMrp failed", e);
		return null;
		}
	}

	@Override
	public String getEntitlementDesc(Integer admissionId) {
		String sql = "SELECT " +
				"   (CASE " +
				"       WHEN payment_entitlement_id = 1 THEN 'Self' " +
				"       WHEN payment_entitlement_id = 2 THEN 'Insurance' " +
				"       WHEN payment_entitlement_id = 3 THEN 'Corporate' " +
				"   END) " +
				"FROM opd.t_patient_sponsor_details tpsd " +
				"WHERE admission_id = :admissionId " +
				"AND priority_id = 1 " +
				"AND tpsd.status = 'A'";
		Query q = sessionFactory.getCurrentSession().createSQLQuery(sql);
		q.setParameter("admissionId", admissionId); // Bind the admissionId parameter
		List resultList = q.list();
		return !resultList.isEmpty() ? (String) resultList.getFirst() : "";
	}

	@Override
	public PurchaseOrderDetailResponse getPOReqStore(Integer id) {
		try {
			String query = " select \r\n"
					+ "	store4_.store_id as fromstoreid , store4_.store_name as fromstorename\r\n" + "from\r\n"
					+ "	procurement.t_purchase_order purchaseor0_ \r\n"
					+ "left outer join public.m_store store4_ on\r\n"
					+ "	purchaseor0_.requesting_store_id = store4_.store_id\r\n" + "where purchaseor0_.id=:id "
					+ "	 \r\n" + "	 ";

			PurchaseOrderDetailResponse dto = (PurchaseOrderDetailResponse) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("id",id)
					.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailResponse.class)).uniqueResult();
			return dto;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPOReqStore failed", e);
		return null;
		}
	}

	@Override
	public Batch getBatchForNa(String batchNo, Integer itemId, Integer storeId, Double cop, Double mrp, Integer unitId,
			Date expiryDate) {
		if(batchNo.toUpperCase().equals("NA") && expiryDate==null)
		{
			String hql = "SELECT batch FROM Batch AS batch  join batch.item as item  "
					+ "		LEFT JOIN FETCH batch.storeStockList AS storeStock "
					+ "		LEFT JOIN FETCH storeStock.store AS store "
					+ "WHERE item.id=:itemId AND   upper(batch.batchNo) = :batchNo AND batch.expiryDate IS NULL  and batch.cop=:cop AND batch.rate=:rate   AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
					+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
					+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId  order by batch.id ";
			Query query = getSession().createQuery(hql);
			query.setParameter("storeId", storeId);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("batchNo", batchNo.toUpperCase());
			query.setParameter("cop", cop);
			query.setParameter("rate", mrp);
			query.setParameter("itemId", itemId);
			query.setMaxResults(1);
			return (Batch) query.uniqueResult();
		}
		else
		{
		String hql = "SELECT batch FROM Batch AS batch  join batch.item as item  "
				+ "		LEFT JOIN FETCH batch.storeStockList AS storeStock "
				+ "		LEFT JOIN FETCH storeStock.store AS store "
				+ "WHERE item.id=:itemId AND   upper(batch.batchNo) = :batchNo AND batch.expiryDate=:expiryDate  and batch.cop=:cop AND batch.rate=:rate   AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId  order by batch.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("batchNo", batchNo.toUpperCase());
		query.setParameter("expiryDate", expiryDate);
		query.setParameter("cop", cop);
		query.setParameter("rate", mrp);
		query.setParameter("itemId", itemId);
		query.setMaxResults(1);
		return (Batch) query.uniqueResult();
		}

	}

	@Override
	public List<StoreStock> getStoreStockByBatchAndStoreList(Long batchId, Integer requestStoreId, Integer unitId) {

		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "		WHERE store.storeId = :storeId AND batch.id = :batchId  "
				+ " 	AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId and storeStock.totalQty is not null and  storeStock.availableQty is not null   order by storeStock.availableQty desc ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", requestStoreId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		List<StoreStock> data = query.list();
		return data;

	}

	@Override
	public Batch getBatchForCopAvg(String batchNo, Integer itemId, Integer storeId, Integer unitId, Date expiryDate) {

		String hql = "SELECT batch FROM Batch AS batch  join batch.item as item  "
				+ "		LEFT JOIN FETCH batch.storeStockList AS storeStock "
				+ "		LEFT JOIN FETCH storeStock.store AS store "
				+ "WHERE item.id=:itemId AND  batch.batchNo = :batchNo AND batch.expiryDate=:expiryDate  and batch.cop=:cop AND batch.rate=:rate AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("batchNo", batchNo);
		query.setParameter("expiryDate", expiryDate);
		// query.setParameter("cop", cop);
		// query.setParameter("rate", mrp);
		query.setParameter("itemId", itemId);
		query.setMaxResults(1);
		return (Batch) query.uniqueResult();

	}

	@Override
	public StoreStock getStoreStockByBatchAndStore1(Long batchId, Integer storeId, Integer unitId) {

		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "		WHERE store.storeId = :storeId AND batch.id = :batchId  "
				+ " 	AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId and  storeStock.availableQty is not null  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		List<StoreStock> data = query.list();
		if (data.size() > 0) {
			return (StoreStock) data.getFirst();
		}
		return null;

	}

	@Override
	public void updateStoreStockTransitQty(StoreStock storeStockList) {
		getSession().createSQLQuery("UPDATE inventory.t_store_stock SET t_qty = :qty WHERE id = :id")
				.setParameter("qty", storeStockList.gettQty())
				.setParameter("id", storeStockList.getId())
				.executeUpdate();
	}

	@Override
	public Integer getBatchId(Integer id) {
		Query q = getSession().createSQLQuery(
						"SELECT tgrnd.batch_id FROM procurement.t_good_receipt_note_detail tgrnd WHERE tgrnd.id = :id"
				)
				.setParameter("id", id);
		return (Integer) q.uniqueResult();
	}

	@Override
	public Batch getBatchById(long id) {

		String hql = "SELECT batch FROM Batch as batch  WHERE batch.id = :id  order by batch.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		List<Batch> batchList = query.list();
		return batchList.getFirst();

	}

	@Override
	public List<AgainstGrnDetailResponse> getApprovedGrnDetails(List<Integer> grnId, Integer assetTypeId) {

		String hql = null;
		
		if (assetTypeId != null && assetTypeId.equals(1)) {
			
			hql = "SELECT grn.id AS grnId, grnDetail.id AS grnDetailId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
					+ "		batch.lotNo AS lotNo, grnDetail.cop AS cop, vendor.id AS vendorId, vendor.name AS vendor, cast(grnDetail.rejectedQuantity as long) AS grnRejectQuantity, "
					+ "		grnStore.storeId AS storeId, grnStore.storeName AS store, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, "
					+ "		uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, grnDetail.mrp AS mrp, "
                    + "     cast(COALESCE(grnDetail.grnApprovedQuantity, 0)+COALESCE(grnDetail.rejectedQuantity,0) as long) AS totalGRNQuantity ,cast(COALESCE(storeStock.availableQty, 0) as long) AS availableQuantity, cast(physicalInspectionDetail.rejectQuantity as long) AS phyRejectQuantity,"
                    + "grnDetail.discount AS discount,grnDetail.totalAmount AS totalAmount,grnDetail.totalDiscount AS totalDiscount, (case WHEN item.isThreeUOMApplicable is false then false WHEN  item.isThreeUOMApplicable is true then true ELSE  true end)  as isThreeUOMApplicable, "
                    + " COALESCE(grnDetail.bonusApprovedQuantity, 0) as bonusApprovedQuantity,bonusUomType.id as bonusUomTypeId,COALESCE(grnDetail.grnApprovedQuantity, 0) as approvedQuantity "
					+ "	FROM GoodReceiptNote AS grn "
					+ "		LEFT JOIN grn.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ "		LEFT JOIN grn.store AS grnStore WITH grnStore.isActive = true AND grnStore.isDeleted = false "
					+ "		LEFT JOIN grn.goodReceiptNoteDetailsList AS grnDetail WITH grnDetail.isActive = true AND grnDetail.isDeleted = false "
					// + " LEFT JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail
					// WITH physicalInspectionDetail.isActive = true AND
					// physicalInspectionDetail.isDeleted = false "

					// start code for resolved HIS-10445 Returnable Gatepass - Rejected against GRN
					// - Not able to fetch Get GRN details after selection
					+ "		INNER JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail WITH physicalInspectionDetail.isActive = true AND physicalInspectionDetail.isDeleted = false "

					+ "		LEFT JOIN grnDetail.grnStagedQuantityList AS staged WITH staged.isActive = true AND staged.isDeleted = false "
					+ "		LEFT JOIN grnDetail.status AS status WITH status.id = :statusId AND status.isActive = true AND status.isDeleted = false "
					// + " LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND
					// batch.isDeleted = false AND batch.isQuarantine = false "
					+ "		LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false  "
					+ "		LEFT JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
					+ "		LEFT JOIN grnDetail.tax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
					+ "		LEFT JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ "	    LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
					+ "	    LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
					+ "	    LEFT JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
					+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
					+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
					+ "		INNER JOIN grnDetail.grnUomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ "		INNER JOIN grnDetail.grnUomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
					+ "		LEFT JOIN grnDetail.bonusUomType AS bonusUomType WITH bonusUomType.isActive = true AND bonusUomType.isDeleted = false "
					+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.id IN (:id)  "
					+ "		AND (:assetId = 0 OR grn.assetType.id=:assetId) AND physicalInspectionDetail.approvedQuantity>0"
					+ "	GROUP BY grn.id, grnDetail.id, item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, grnDetail.cop, vendor.id, vendor.name, "
					+ "		grnStore.storeId, grnStore.storeName, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, uomType.id, "
					+ "		uomType.type, uomUnit.id, uomUnit.units, grnDetail.mrp ,grnDetail.grnApprovedQuantity,grnDetail.rejectedQuantity , physicalInspectionDetail.rejectQuantity ,storeStock.availableQty, storeStock.totalQty,grnDetail.grnApprovedQuantity,bonusUomType.id  "
					+ "	ORDER BY grn.id ";

		} else {

			hql = "SELECT grn.id AS grnId, grnDetail.id AS grnDetailId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
					+ "		batch.lotNo AS lotNo, batch.cop AS cop, vendor.id AS vendorId, vendor.name AS vendor, cast(grnDetail.rejectedQuantity as long) AS grnRejectQuantity , "
					+ "		grnStore.storeId AS storeId, grnStore.storeName AS store, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, "
					+ "		uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, grnDetail.mrp AS mrp, "
                    + "     cast(COALESCE(grnDetail.grnApprovedQuantity, 0) as long) AS totalGRNQuantity  ,cast(COALESCE(storeStock.availableQty, 0) as long) AS availableQuantity, cast(physicalInspectionDetail.rejectQuantity as long) AS phyRejectQuantity,"
                    + "grnDetail.discount AS discount,grnDetail.totalAmount AS totalAmount,grnDetail.totalDiscount AS totalDiscount, (case WHEN item.isThreeUOMApplicable is false then false WHEN  item.isThreeUOMApplicable is true then true ELSE  true end)  as isThreeUOMApplicable, "
                    + " COALESCE(grnDetail.bonusApprovedQuantity, 0) as bonusApprovedQuantity,bonusUomType.id as bonusUomTypeId "
                    + "	FROM GoodReceiptNote AS grn "
					+ "		LEFT JOIN grn.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ "		LEFT JOIN grn.store AS grnStore WITH grnStore.isActive = true AND grnStore.isDeleted = false "
					+ "		LEFT JOIN grn.goodReceiptNoteDetailsList AS grnDetail WITH grnDetail.isActive = true AND grnDetail.isDeleted = false "
					// + " LEFT JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail
					// WITH physicalInspectionDetail.isActive = true AND
					// physicalInspectionDetail.isDeleted = false "

					// start code for resolved HIS-10445 Returnable Gatepass - Rejected against GRN
					// - Not able to fetch Get GRN details after selection
					+ "		INNER JOIN grnDetail.physicalInspectionDetail AS physicalInspectionDetail WITH physicalInspectionDetail.isActive = true AND physicalInspectionDetail.isDeleted = false "

					+ "		LEFT JOIN grnDetail.grnStagedQuantityList AS staged WITH staged.isActive = true AND staged.isDeleted = false "
					+ "		LEFT JOIN grnDetail.status AS status WITH status.id = :statusId AND status.isActive = true AND status.isDeleted = false "
					// + " LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND
					// batch.isDeleted = false AND batch.isQuarantine = false "
					+ "		LEFT JOIN grnDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false  "
					+ "		LEFT JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
					+ "		LEFT JOIN grnDetail.tax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
					+ "	    LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
					+ "	    LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
					+ "	    LEFT JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
					+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
					+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
					+ "		LEFT JOIN grnDetail.grnUomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ "		LEFT JOIN grnDetail.grnUomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
					+ "		LEFT JOIN grnDetail.bonusUomType AS bonusUomType WITH bonusUomType.isActive = true AND bonusUomType.isDeleted = false "
					+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.id IN (:id) "
					+ "		AND (:assetId = 0 OR grn.assetType.id=:assetId) AND physicalInspectionDetail.approvedQuantity>0 "
					+ "	GROUP BY grn.id, grnDetail.id, item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, batch.cop, vendor.id, vendor.name, "
					+ "		grnStore.storeId, grnStore.storeName, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, uomType.id, "
					+ "		uomType.type, uomUnit.id, uomUnit.units, grnDetail.mrp ,grnDetail.grnApprovedQuantity,grnDetail.rejectedQuantity  , physicalInspectionDetail.rejectQuantity ,storeStock.availableQty, storeStock.totalQty,grnDetail.grnApprovedQuantity,bonusUomType.id "
					+ "	ORDER BY grn.id ";

		}
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", grnId);
		query.setParameter("assetId", assetTypeId != null ? assetTypeId : 0);
		query.setParameter("statusId", com.param.common.constants.Status.Closed.getStatus());
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		query.setResultTransformer(Transformers.aliasToBean(AgainstGrnDetailResponse.class));

		return query.list();

	}

	@Override
	public Integer executeBatchForMappers(List<SaleDetailPayeeMapper> payeeList,
			List<SaleDetailPackageMapper> packageMprList) {
		Connection connection = null;
		PreparedStatement statementPackage = null;
		PreparedStatement statement = null;
		try {
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);

			 statement = connection.prepareStatement("INSERT INTO pharmacy.t_sale_detail_payee_mapper "
					+ "(t_sale_details_id, item_id, contract_id, payee_id, payee_amount, unit_id, organization_id, status, quantity, split_bill_amount, associate_company_id)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, 'A', ?, ?, ?);");
			int i = 0;
			for (SaleDetailPayeeMapper dto : payeeList) 
			{
				statement.setInt(1, dto.getSaleDetail().getId().intValue());
				statement.setInt(2, dto.getItemId());

				if (dto.getContractId() != null)
					statement.setInt(3, dto.getContractId());
				else
					statement.setNull(3, Types.NULL);

				if (dto.getPayeeId() != null)
					statement.setInt(4, dto.getPayeeId());
				else
					statement.setNull(4, Types.NULL);

				statement.setDouble(5, dto.getPayeeAmount().doubleValue());
				statement.setInt(6, dto.getUnitId());
				statement.setInt(7, dto.getOrganizationId());
				statement.setDouble(8, dto.getQuantity());
				statement.setDouble(9, dto.getPayeeAmount().doubleValue());

				if (dto.getAssociateCompanyId() != null)
					statement.setInt(10, dto.getAssociateCompanyId());
				else
					statement.setNull(10, Types.NULL);
				statement.addBatch();
				i++;
				if (i % 500 == 0 || i == payeeList.size()) 
				{
					statement.executeBatch(); // Execute every 20 items.
					connection.commit();
				}
			}

			statementPackage = connection
					.prepareStatement("INSERT INTO public.t_sale_detail_package_mapper "
							+ "(t_sale_details_id, item_id, package_id, payee_id, rate, is_package_item, package_amount, unit_id, organization_id, status, package_consumption_cap_details_id, package_consumption_master_id, package_consumtion_eo_detail_id, package_qty, cap_or_qty) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,'A',?,?,?,?,?);");
			int j = 0;
			for (SaleDetailPackageMapper dto : packageMprList) 
			{
				statementPackage.setInt(1, dto.getSaleDetail().getId().intValue());
				statementPackage.setInt(2, dto.getItemId());

				if (dto.getPackageId() != null)
					statementPackage.setInt(3, dto.getPackageId());
				else
					statementPackage.setNull(3, Types.NULL);

				if (dto.getPayeeId() != null)
					statementPackage.setInt(4, dto.getPayeeId());
				else
					statementPackage.setNull(4, Types.NULL);

				statementPackage.setDouble(5, dto.getRate().doubleValue());
				statementPackage.setObject(6, dto.getIsPackageItem());
				statementPackage.setDouble(7, dto.getPackageAmount().doubleValue());
				statementPackage.setInt(8, dto.getUnitId());
				statementPackage.setInt(9, dto.getOrganizationId());

				if (dto.getPackageConsumptionCapDetailsId() != null)
					statementPackage.setInt(10, dto.getPackageConsumptionCapDetailsId());
				else
					statementPackage.setNull(10, Types.NULL);

				if (dto.getPackageConsumptionMasterId() != null)
					statementPackage.setInt(11, dto.getPackageConsumptionMasterId());
				else
					statementPackage.setNull(11, Types.NULL);

				if (dto.getPackageConsumptionEODetailId() != null)
					statementPackage.setInt(12, dto.getPackageConsumptionEODetailId());
				else
					statementPackage.setNull(12, Types.NULL);

				if (dto.getPackageQty() != null)
					statementPackage.setInt(13, dto.getPackageQty());
				else
					statementPackage.setNull(13, Types.NULL);

				statementPackage.setInt(14, dto.getCapOrQty());
				statementPackage.addBatch();
				j++;
				if (j % 500 == 0 || j == packageMprList.size()) 
				{
					statementPackage.executeBatch();
					connection.commit();
				}
			}
			return 0;
		} catch (Exception e) {
		logger.error("executeBatchForMappers failed", e);
		return 1;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (statementPackage != null) {
					statementPackage.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (Exception e) {
			logger.error("executeBatchForMappers failed", e);
			}
		}
	}

	@Override
	public BatchAllocation getBatchAllocation(Integer batchAllocationId) {

		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation \r\n "
				+ " WHERE batchAllocation.id = :allocationId  ";
		Query query = getSession().createQuery(hql);
		query.setParameter("allocationId", batchAllocationId);
		// query.setParameter("unitId", sessionService.getUnitId());

		return (BatchAllocation) query.uniqueResult();
	}

	@Override
	public List<Batch> getBatchDetailsForPatientIssueAcceptanceNew(Integer id, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty >= 0  AND storeStock.availableQty is not null and storeStock.totalQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null ) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);

		return query.list();
	}

	@Override
	public Boolean getVendorSelectionMapper(Integer id, Integer vendorId) {
		String sql = " select is_accept from  procurement.t_vendor_selection_mapper m join  procurement.t_vendor_selection_detail  d \r\n"
				+ "on m.vendor_selection_detail_id =d.id \r\n" + "where d.qr_detail_id =:id and m.vendor_id =:vendorId limit 1";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id",id);
		query.setParameter("vendorId",vendorId);
		return (Boolean) query.uniqueResult();
	 

	}

	@Override
	public Long getStoreIndentQuantityByItem(Integer storeId, Integer id, Integer unitId) {
		 
		String hql = "SELECT COALESCE(SUM(stock.authorizedQuantity), 0) " + "FROM StoreIndentDetail AS stock "
				+ "		INNER JOIN stock.storeIndent AS batch "
				+ "WHERE batch.fromStore.storeId = :storeId AND stock.item.id = :itemId "
				+ "		AND stock.isActive = true AND stock.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", id);

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

		// return (Long) query.uniqueResult();
	}
	
	
//	
	@Override
	public AdmissionDto admissionPatient1(Integer admissionId) {
		try {
					
			String query = " select " + 
					" bedmaster5_.bed_id as bed ,bedmaster5_.bed_number as bednumber1,wardmaster6_.ward_id as ward ,wardmaster6_.ward_name as ward1" + 
					" from " + 
					" adt.t_admission as admission0_ " + 
					" inner join adt.t_admission_details as admissiond4_ on " + 
					" admission0_.admission_id = admissiond4_.admission_id " + 
					" and (admissiond4_.status = 'A' " + 
					" and admissiond4_.nursing_acceptance = 'Y') " + 
					" left outer join adt.m_bed_master as bedmaster5_ on " + 
					" admissiond4_.bed_id = bedmaster5_.bed_id " + 
					" and (bedmaster5_.status = 'A') " + 
					" left outer join adt.m_ward_master as wardmaster6_ on " + 
					" admissiond4_.ward_id = wardmaster6_.ward_id " + 
					" and (wardmaster6_.status = 'A') " + 
					" where " + 
					" admission0_.status = 'A' and " + 
					" admission0_.admission_id =:admissionId  ";

			AdmissionDto dto = (AdmissionDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("admissionId",admissionId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).uniqueResult();
			return dto;

			
			
		} catch (Exception e) {
		logger.error("admissionPatient1 failed", e);
		return null;
		}
	}

	
	

	@Override
	public List<PatientSearchDetails> getIPDCERPatientsAll(String name, Integer unitId) {
		String sql = "SELECT DISTINCT pat.uhid_number   AS uhidNumber, "
				+ "                pre.prefix_code   AS prefixCode, "
				+ "                pat.first_name    AS firstName, "
				+ "                pat.middle_name   AS middleName, "
				+ "                pat.last_name     AS lastName, " + "                pat.dob           AS dob, "
				+ "                gen.gender_code   AS genderCode, "
				+ "                pat.mobile_number AS mobileNumber, "
				+ "                pat.patient_id    AS patientId " + "	FROM   adt.t_admission adm "
				+ "       INNER JOIN patient.t_patient_registration pat "
				+ "               ON pat.patient_id = adm.patient_id " + "       LEFT JOIN adt.t_discharge dis "
				+ "              ON adm.admission_id = dis.admission_id "
				+ "       INNER JOIN PUBLIC.m_prefix_master pre " + "               ON pre.prefix_id = pat.prefix_id "
				+ "       INNER JOIN PUBLIC.m_gender_master gen " + "               ON pat.gender_id = gen.gender_id "
				+ "WHERE  (dis.discharge_status_id <> '5' or dis.discharge_status_id is null) "
				+ "       AND pat.status = 'A' " + "      "
				+ "       AND adm.unit_id =:unitId " + "       AND ( Lower(pat.first_name) LIKE :name "
				+ "              OR Lower(pat.middle_name) LIKE :name "
				+ "              OR Lower(pat.last_name) LIKE :name )";

		Query query = getSession().createSQLQuery(sql).addScalar("uhidNumber").addScalar("prefixCode")
				.addScalar("firstName").addScalar("middleName").addScalar("lastName").addScalar("dob")
				.addScalar("genderCode").addScalar("mobileNumber").addScalar("patientId", StandardBasicTypes.INTEGER);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PatientSearchDetails.class));
		return query.list();

	}
	
	@Override
	public Double patientclasspercentage(Integer itemId, Integer organizationId, Integer unitId,Integer entitlementId) {
		Double count = (Double) sessionFactory.getCurrentSession().createSQLQuery(
						"SELECT percentage " +
								"FROM billing.m_patient_type_group_wise_details " +
								"WHERE item_id = :itemId " +
								"AND payment_entitlement_id = :entitlementId " +
								"AND organization_id = :organizationId " +
								"AND unit_id = :unitId " +
								"ORDER BY created_date, updated_date ASC " +
								"LIMIT 1"
				)
				.setParameter("itemId", itemId != null ? itemId : 0)
				.setParameter("entitlementId", entitlementId != null ? entitlementId : 0)
				.setParameter("organizationId", organizationId != null ? organizationId : 0)
				.setParameter("unitId", unitId != null ? unitId : 0)
				.uniqueResult();

		return count;

	}

	@Override
	public String getUserNameByUserId(Integer addedByUser, Integer unitId) {

		String sql = "SELECT CONCAT(COALESCE(mem.first_name, ' '), ' ', COALESCE(mem.middle_name, ' '), ' ', COALESCE(mem.last_name)) AS \"userName\" " +
				"FROM m_employee_master mem " +
				"WHERE mem.user_id = :addedByUser AND mem.unit_id = :unitId " +
				"UNION ALL " +
				"SELECT CONCAT(COALESCE(mdm.first_name, ' '), ' ', COALESCE(mdm.middle_name, ' '), ' ', COALESCE(mdm.last_name)) AS \"userName\" " +
				"FROM doctor.m_doctor_master mdm " +
				"WHERE mdm.user_id = :addedByUser AND mdm.unit_id = :unitId";

		String userName = (String) sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("addedByUser", addedByUser)
				.setParameter("unitId", unitId)
				.uniqueResult();

		return userName;

	}

	@Override
	public BillableConsumptionSave getBillableConsumId(Integer integer) {
		try {
			
			String query = " select tbcd.billable_consumption_id  as  \"billableConsumptionId\" from pharmacy.t_billable_consumption_detail tbcd where is_active = true and id =:integer  ";
			BillableConsumptionSave dto = (BillableConsumptionSave) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("integer",integer)
					.setResultTransformer(Transformers.aliasToBean(BillableConsumptionSave.class)).uniqueResult();
			return dto;
		} catch (Exception e) {
		logger.error("getBillableConsumId failed", e);
		return null;
		}
	}

	@Override
	public Integer getTotalQuantity(Integer itemId, Integer issueId, Integer storeId, Integer unitId, Long batchId) {
		String sql = "SELECT CAST(SUM(tmid.quantity) AS INT) " +
				"FROM inventory.t_stock_transaction tmid " +
				"WHERE tmid.unit_id = :unitId " +
				"AND tmid.item_id = :itemId " +
				"AND tmid.batch_id = :batchId " +
				"AND tmid.transaction_details_id = :issueId";
		Query query = getSession().createSQLQuery(sql)
				.setParameter("unitId", unitId)
				.setParameter("itemId", itemId)
				.setParameter("batchId", batchId)
				.setParameter("issueId", issueId);
		return (Integer) query.uniqueResult();
	}

	@Override
	public List<ReturnableGatepassDetail> getAvailQty(Integer grnDetailId) {
		String hql = "SELECT returnableGatepassDetail " + "	FROM ReturnableGatepassDetail AS returnableGatepassDetail "
				+ "		INNER JOIN returnableGatepassDetail.goodReceiptNoteDetail AS grnDetails WITH grnDetails.isActive = true AND grnDetails.isDeleted = false "
				+ " INNER JOIN returnableGatepassDetail.returnableGatepass as returnableGatepass with returnableGatepass.isActive = true AND returnableGatepass.isDeleted = false"
				+ " WHERE returnableGatepassDetail.isActive = true AND returnableGatepassDetail.isDeleted = false and returnableGatepass.requestType=15 "
				+ "		AND grnDetails.id = :grnDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("grnDetailId", grnDetailId);

		return query.list();
	}

	@Override
	public Double getNetAmount(List<Integer> poIds) {
		 String sql ="select tpod.net_amount from procurement.t_purchase_order tpod"
	                + " where tpod.id in (:poIds) order by 1 desc";
	        Query query = getSession().createSQLQuery(sql);
	        query.setParameterList("poIds", poIds);
	        query.setMaxResults(1);
	        return (Double) query.uniqueResult();
	}

	@Override
	public Double getRountOffAmount(List<Integer> poIds) {
		 String sql ="select tpod.round_off_amount from procurement.t_purchase_order tpod"
	                + " where tpod.id in (:poIds) order by 1 desc";
	        Query query = getSession().createSQLQuery(sql);
	        query.setParameterList("poIds", poIds);
	        query.setMaxResults(1);
	        return (Double) query.uniqueResult();
	}

	@Override
	public Double getActualRateFromOpeningBalance(Long batchid, Integer unitId) {
		String sql = "select tobd.rate from inventory.t_batch tobd  where tobd.id =:batchId and tobd.unit_id =:unitId ";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchId", batchid.intValue());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return (Double) query.uniqueResult();
	}

	@Override
	public boolean getSubItemAtCOPFlag(BigInteger prescriptionDetailid,Integer unitId) {
		String sql = "select tsd.at_cop from pharmacy.t_sale_detail tsd  where tsd.prescription_detail_id =:prescriptionDetailId and tsd.unit_id =:unitId ";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("prescriptionDetailId", prescriptionDetailid);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return (boolean) query.uniqueResult();
	}

	@Override
	public String getEntitlementContractName(Integer admissionId) {
		String sql = " select "
				+ " cmm_.contract_name as \"contractName\" " + " from "
				+ "	opd.t_patient_sponsor_details tpsd  left join service.m_company_contract_master cmm_ on " + 
				"	tpsd.contract_id = cmm_.contract_id " + " where " + "	admission_id=:admissionId "
				+ "	and priority_id = 1 and tpsd.status = 'A' ";
		Query q = sessionFactory.getCurrentSession().createSQLQuery(sql);
		q.setParameter("admissionId",admissionId);
		return !q.list().isEmpty() ? (String) q.list().getFirst() : "";
	}

	@Override
	public Integer getBonusBatchId(Integer id) {
        String sql = "select  cast(batch.batch_id as int) as id "
                + "from procurement.t_grn_bonus_batch_expiry batch where grn_bonus_batch_expiry_id=:id ";

        SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.INTEGER);
        query.setParameter("id", id);
        return (Integer) query.uniqueResult();
	}

	@Override
	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiryByGrnDetailId(Integer grnDetailId) {
		String hql = "SELECT grnBonusBatchExpiry FROM GrnBonusBatchExpiry AS grnBonusBatchExpiry where grnDetailId=:grnDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("grnDetailId", grnDetailId);
		return query.list();
	}

	@Override
	public Integer getSumOfReturnedQty(Long batchId) {
		
		String sql = "select  cast(coalesce(sum(batch.returned_quantity),0) as int) as id "
                + "from inventory.t_returnable_bonus_batch_expiry batch where batch.batch_id=:id group by batch.batch_id ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", batchId);
        return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getBonusTypeId(Integer id) {
		String sql = "select (Case when detail.bonus_uom_type_id is not null then detail.bonus_uom_type_id else 1 end) as id "
                + "from procurement.t_good_receipt_note_detail detail where detail.id=:id ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        Integer i =(Integer) query.uniqueResult();
        System.out.println("my-Id iss:" +i);
        return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getBonusApprovedQty(Integer id) {
		String sql = "select  detail.bonus_approved_quantity "
                + "from procurement.t_good_receipt_note_detail detail where detail.id=:id ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        return (Integer) query.uniqueResult();
	}

	@Override
	public List<returnableBonusBatchExpiry> getReturnableBonusBatchExpiryByGrnDetailId(Integer id) {
		String hql = "SELECT returnableBonusBatchExpiry FROM returnableBonusBatchExpiry AS returnableBonusBatchExpiry where returnableGatepassDetailId=:grnDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("grnDetailId", id);
		return query.list();
	}

	@Override
	public Integer getIsFinalDischarge(Integer admissionId) {
		String sql = "select  cast(is_interim_or_final_bill as int) "
                + "from billing.m_billing_master where admission_id=:id and status='A' order by 1 desc";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", admissionId);
        query.setMaxResults(1);
        return (Integer) query.uniqueResult();
	}

	@Override
	public List<Batch> getKitItemsAndBatchOnWalkInSales(List<Integer> itemIds, Integer unitId,
			BatchProperty batchProperty, Integer storeId, Boolean fromWalkInPrescription) {

		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		String hql = "SELECT batch FROM Batch AS batch " + "		INNER JOIN batch.item AS item "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id in(:id) AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND  storeStock.unit.unitId = :unitId  "
				+ " AND (  batch.expiryDate >= :greaterThanCurrentMonth OR  batch.expiryDate is null ) "
				+ " AND item.isDeleted = false AND item.isActive = true AND batch.unit.unitId = :unitId ";

		if(fromWalkInPrescription == null || !fromWalkInPrescription){
			hql	= hql + " AND storeStock.availableQty > 0 "
					+ " and storeStock.availableQty is not null and storeStock.totalQty is not null ";
		}

		hql = hql +  " ORDER BY batch.expiryDate ASC";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", itemIds);
		query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		return query.list();
	
	}

	@Override
	public ItemsDetailsResponse itemsDetailTariffForKit(Integer id, Integer storeId) {

		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, item.isThreeUOMApplicable AS isThreeUOMApplicable, generic.id AS genericId, generic.name AS genericName, "
				+ "		manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, salesTax.id AS taxId, salesTax.name AS taxName, item.isLasa as isLasa, "
				+ "		salesTax.taxPercentage AS taxPercent, item.isOtc AS isOtc, formulationType.type AS formulationType, formulationType.id AS formulationTypeId,"
				+ " storeItem.minQty AS minQty ,  storeItem.maxQty AS maxQty , "
				+ "		concat(item.strength, ' ', strengthUnit.units) as strength, coalesce(generic.productCategory.id,item.productCategory.id) as productCategoryId, storageUnit.unitName as storageUnitName  "
				+ " FROM Item AS item "
				+ "		left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.salesTax AS salesTax WITH salesTax.isDeleted = false AND salesTax.isActive = true "
				+ " 	LEFT JOIN item.formulationType AS formulationType WITH formulationType.isDeleted = false AND formulationType.isActive = true "
				+ "		LEFT JOIN item.strengthUnit as strengthUnit WITH strengthUnit.isActive = true AND strengthUnit.isDeleted = false "
				+ "     LEFT JOIN item.storageUnit as storageUnit WITH storageUnit.isActive = true AND storageUnit.isDeleted = false "
				+ "     left join item.storeItemList  as storeItem with storeItem.store.id = :storeId "
				+ " WHERE item.id = :id AND item.isDeleted = false AND item.isActive = true and storeItem.isActive = true";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsResponse.class));

//		return (ItemsDetailsResponse) query.uniqueResult();
		ItemsDetailsResponse response = (ItemsDetailsResponse) query.uniqueResult();
		return response;
	}

	@Override
	public List<UomResponse> getKitItemsUomList(List<Integer> itemIds, Integer unitId) {

		try {
			
			StringBuffer sb = new StringBuffer("select tbum.batch_id as \"intbatchId\",tbum.conversions as \"conversion\", "
					+ " tbum.ip_uom as \"ipUom\","
					+ " tbum.op_uom as \"opUom\","
					+ " tbum.store_uom as \"storeUom\","
					+ " tbum.uom_type_id as \"uomTypeId\",tbum.uom_unit_id as \"uomUnitId\","
					+ " mut.type as \"uomType\" ,concat(mut.code,' - ',muu.unit ) as \"uomDetails\",muu.unit as \"uomUnit\" "
					+ " from inventory.t_batch_uom_mapper tbum "
					+ " inner join inventory.t_batch tb  on tb.id = tbum .batch_id  "
					+ " inner join public.m_uom_type mut on mut.id = tbum.uom_type_id " 
					+ " inner join public.m_uom_unit muu  on muu.id  = tbum .uom_unit_id "
					+ " where tb.item_id in(:itemIds) and tbum.is_deleted =false and tb.unit_id = :unitId and tb.current_quantity >0");
			List<UomResponse> list  = (List<UomResponse>)sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("unitId", unitId)
					.setParameterList("itemIds",itemIds)
					.setResultTransformer(Transformers.aliasToBean(UomResponse.class)).list();
			
			return list;
			
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public Integer getIsPendingStatus(Integer grnDetailId) {
		String hql = " select (case "
				+ " when trgd.status_id = 10 or trgd.status_id = 1 then 1 "
				+ " else 0 "
				+ " end ) "
				+ " from inventory.t_returnable_gatepass_detail trgd "
				+ " where trgd.grn_detail_id  = :grnDetailId ORDER BY 1 DESC limit 1 ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("grnDetailId", grnDetailId);
		
		

		return (Integer) query.uniqueResult();
	
	}

	@Override
	public String pharmacyPRNFormat(Integer unitId) {
		String sql = "SELECT mac.pharmacy_prn_format " +
				"FROM public.m_application_configuration mac " +
				"WHERE mac.unit_id = :unitId";
		String res = (String) getSession().createSQLQuery(sql)
				.setParameter("unitId", unitId)
				.uniqueResult();
		return res;

	}

	@Override
	public boolean isPRNRequired(Integer unitId) {
		String sql = "SELECT mac.is_prn_required " +
				"FROM public.m_application_configuration mac " +
				"WHERE mac.unit_id = :unitId";
		Boolean res = (Boolean) getSession().createSQLQuery(sql)
				.setParameter("unitId", unitId)
				.uniqueResult();
		return res;

	}
	public Integer getReturnableStatus(Long batchId) {
		String hql = " select cast(count(*) as int) from inventory.t_returnable_gatepass_detail detail"
				+ " inner join inventory.t_returnable_gatepass trg on trg.id=detail.returnable_gatepass_id where trg.approval_status_id not in (5,7) and detail.batch_id=:batchId  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("batchId", batchId);
		return (Integer) query.uniqueResult();
	}

	@Override
	public List<AgainstGrnDetailResponse> getBonusBatchDetails(int id) {
		try{
		String sql = " select "
				+ " (case when tgbbe.bonus_quantity >0 then tgbbe.batch_no end) as \"batchNo\", "
				+ " tgbbe.grn_bonus_batch_expiry_id  as \"bonusBatchExpiryId\", "
				+ " tgrn.id as \"grnId\", tgrnd.id as \"grnDetailId\", mi.id as \"itemId\", mi.code as \"itemCode\", mi.name  as \"itemName\", "
				+ " batch.id   as \"batchIdForBarcode\", "
				+ " tgbbe.expiry_date as \"expiry\", tgrnd.lot_number as \"lotNo\", "
				+ " tgrnd.cop as \"cop\", mv.id as \"vendorId\", mv.name as \"vendor\", "
				+ " mom.organization_name as \"hospitalName\", mstore.store_id as \"storeId\", mstore.store_name as \"store\", "
				+ " mtax.id as \"taxId\", mtax.name as \"taxName\", mtax.tax_percentage as \"taxPercentage\", uomtype.id as \"uomTypeId\", "
				+ " uomtype.type as \"uomType\", uomunit.id as \"uomUnitId\", uomunit.unit as \"uomUnit\", tgrnd.mrp as \"mrp\", "
				+ " tgbbe.bonus_quantity as \"pendingQuan\", tgbbe.bonus_quantity as \"availQuan\", batch.printed_count as \"printedCount\" "
				+ " from "
				+ " procurement.t_grn_bonus_batch_expiry tgbbe "
				+ " left join procurement.t_good_receipt_note_detail tgrnd on tgbbe.grn_detail_id = tgrnd.id "
				+ " left join public.m_item mi on mi.id = tgrnd.item_id "
				+ " left join procurement.t_good_receipt_note tgrn on tgrn.id = tgrnd.grn_id "
				+ " left join public.m_vendor mv on mv.id = tgrn.vendor_id "
				+ " left join public.m_store mstore on mstore.store_id = tgrn.store_id "
				+ " left join public.m_tax mtax on mtax.id = mi.p_tax_id "
				+ " left join public.m_uom_type uomtype on tgrnd.grn_uom_type_id = uomtype.id "
				+ " left join inventory.t_batch batch on batch.id = tgrnd.batch_id "
				+ " left outer join public.m_uom_unit uomunit on tgrnd.grn_uom_unit_id = uomunit.id "
				+ " inner join procurement.t_physical_inspection_detail physicalin on tgrnd.id = physicalin.grn_detail_id "
				+ " left join public.m_organization_master mom on mom.organization_id = tgrnd.organization_id "
				+ " where"
				+ " tgrnd.grn_id =:id ";

		Query query = getSession().createSQLQuery(sql);
			query.setParameter("id",id);
		query.setResultTransformer(Transformers.aliasToBean(AgainstGrnDetailResponse.class)).list();
		
		return query.list();
		}
		catch(Exception e){
			throw e;
		}
	}
    @Override
    public Integer getMaterialAcceptedQty(Integer issueDetailId) {
		String query = "SELECT CAST(COALESCE(SUM(issue.accepted_quantity), 0) AS INT) " +
				"FROM inventory.t_material_issue_batch_mapper issue " +
				"WHERE issue.material_issue_detail_id = :issueDetailId";
		Integer result = (Integer) getSession().createSQLQuery(query)
				.setParameter("issueDetailId", issueDetailId)
				.uniqueResult();
		return result;
	}
    
    @Override
    public Integer getMaterialReturnedQty(Integer issueDetailId) {
        String query = " select cast(coalesce((sum(CASE WHEN return.return_qty IS NOT NULL  THEN return.return_qty ELSE 0 END )+sum(CASE WHEN return.accepted_qty IS NOT NULL  THEN return.accepted_qty ELSE 0 END )+sum(CASE WHEN return.comsumed_qty IS NOT NULL  THEN return.comsumed_qty ELSE 0 END )),0) as int) from inventory.t_return_detail return  " + 
                " where return.material_issuedetail_id=:issueDetailId ";
        return (Integer) getSession().createSQLQuery(query).setParameter("issueDetailId",issueDetailId).uniqueResult();
    }

	@Override
	public String getColorCode(Integer genericId,Integer orgId,Integer unitId) {
		String query = "SELECT DISTINCT msm.color_code " +
				"FROM pharmacy.m_schedule_master msm " +
				"INNER JOIN pharmacy.m_generic_schedule_master mgsm ON msm.schedule_id = mgsm.schedule_id " +
				"WHERE mgsm.generic_id = :genericId " +
				"AND msm.org_id = :orgId " +
				"AND msm.unit_id = :unitId " +
				"AND msm.schedule_status = 'A' " +
				"AND msm.color_code IS NOT NULL";
		String result = (String) getSession().createSQLQuery(query)
				.setParameter("genericId", genericId)
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setMaxResults(1)
				.uniqueResult();
		return result;
	}

	@Override
	public List<ItemsDetailsListResponse> getGenericColorMapping(int unitId, int orgId) {
		String sql=" select msm.color_code as \"colorCode\", mg.name as \"genericName\" from pharmacy.m_schedule_master msm " + 
				" inner join pharmacy.m_generic_schedule_master mgsm on msm.schedule_id = mgsm.schedule_id " + 
				" inner join m_generic mg on mgsm.generic_id =mg.id " + 
				" where msm.org_id=:orgId  and msm.unit_id=:unitId ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("orgId",orgId);
		query.setParameter("unitId",unitId);
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsResponse.class));

		return (List<ItemsDetailsListResponse>) query.list();
	}

	@Override
	public List<Long> getPrescriptionDetails(List<Long> ids) {
		String hql = " select  prescriptionDetailsList.prescription.id from PrescriptionDetail as prescriptionDetailsList  "
					+ "	left JOIN prescriptionDetailsList.item as item with item.isNarcotic = false where prescriptionDetailsList.statFlag= false and prescriptionDetailsList.prescription.id in(:preId)";

		Query query = getSession().createQuery(hql);
		query.setParameterList("preId", ids);
		return query.list();
	}

	@Override
	public List<PatientSponsorDetailsDto> getAdmiassionDetails(List<Integer> admissionIds) {

		String sql="  select (case when tpsd.payment_entitlement_id = 1 then 'Self' when tpsd.payment_entitlement_id = 2 then 'Insurance' when tpsd.payment_entitlement_id = 3 then 'Corporate'"
				+  "end) \"paymentEntitlementDesc\",  cmm_.contract_name as \"companyDesc\" , mbm.bed_number as \"bedName\",  mwm.ward_name as \"wardName\", ta.admission_id  as \"admissionId\" "
				+ " from adt.t_admission ta  "
				+ " inner join adt.t_admission_details tad  on tad.admission_id  = ta.admission_id "
				+ " inner join opd.t_patient_sponsor_details tpsd on tpsd .admission_id = ta.admission_id   "
				+ " left join adt.m_bed_master mbm on mbm.bed_id =tad.bed_id "
				+ " left join adt.m_ward_master mwm  on mwm.ward_id  = tad.ward_id  "
				+ " left join service.m_company_contract_master cmm_ on tpsd.contract_id = cmm_.contract_id "
				+ " where ta.admission_id in(:admissionIds) and tpsd.priority_id = 1 and tpsd.status = 'A' ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameterList("admissionIds", admissionIds);
		query.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class));

		return (List<PatientSponsorDetailsDto>) query.list();
	
	}

	@Override
	public List<UomResponse> getBatchUomListAll(List<Long> batchIds) {
		String hql = "SELECT  uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, batchUomMapper.conversions AS conversion, "
				+ "CONCAT(uomType.code, ' - ', uomUnit.units) as uomDetails, "
				+ "batchUomMapper.ipUom AS ipUom, batchUomMapper.opUom AS opUom, batchUomMapper.storeUom AS storeUom ,batch.id As batchId  "
				+ "FROM BatchUomMapper AS batchUomMapper " + "		INNER JOIN batchUomMapper.uomUnit AS uomUnit "
				+ "		INNER JOIN batchUomMapper.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN batchUomMapper.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true "
				+ " WHERE batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false AND batch.id in(:batchId) ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("batchId", batchIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<UomResponse> getReturnableStatus(List<Long> batchIds) {
		String hql = " select cast(count(*) as int) as \"count\" ,detail.batch_id as \"integerBatchId\" from inventory.t_returnable_gatepass_detail detail"
				+ " inner join inventory.t_returnable_gatepass trg on trg.id=detail.returnable_gatepass_id where trg.approval_status_id not in (5,7)"
				+ " and detail.batch_id in (:batchId) group by detail.batch_id  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameterList("batchId", batchIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public boolean isManualBarcodeRequired(Integer unitId) {
		String query = "SELECT mac.is_manual_barcode_required " +
				"FROM public.m_application_configuration mac " +
				"WHERE mac.unit_id = :unitId";
		Boolean res = (Boolean) getSession().createSQLQuery(query)
				.setParameter("unitId", unitId)
				.uniqueResult();
		return res;
	}

	@Override
	public List<UomResponse> getUnitOfMeasurementList(Integer orgId) {
        String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
                + "        uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
                + "        uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom,uom.item.id AS ItemId "
                + "    FROM UnitOfMeasurement AS uom "
                + "        INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
                + "        INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
                + "    WHERE uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId=:organizationId "// HIS-16396
                + "    ORDER BY uomType.id ";

        Query query = getSession().createQuery(hql);
        query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
        // HIS-12019
        query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

        return query.list();
	}

	@Override
	public List<UomResponse> getStoreStockAll(Integer storeId, Integer unitId) {
		Date date = TimeZoneComponent.getDateByzone(unitId);
        String sql = "SELECT  coalesce(stock.totalQty, 0) AS \"stockAvailable\",item.id AS \"itemId\" "
                + " FROM public.m_item item "
                + "    LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
                + "                FROM public.m_item item "
                + " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
                + " and (to_char(batch.expiry_date,'YYYY-MM-DD') >=:expiryDate or batch.expiry_date is null )"
                + "                    INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
                + "                                and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
                + " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
                + "                GROUP BY item.id) stock ON stock.itemId = item.id "
                + " WHERE item.is_deleted=false AND item.is_active=true "
                + "    ORDER BY item.id ";
        Query query = getSession().createSQLQuery(sql).addScalar("stockAvailable", StandardBasicTypes.LONG)
        		.addScalar("itemId", StandardBasicTypes.INTEGER);
		query.setParameter("storeId", storeId);
		query.setParameter("expiryDate",GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		query.setParameter("unitId", (unitId != null && unitId > 0) ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getstockInTransitAll(Integer storeId, Integer unitId) {
		Date date=new Date();
		String hql = "SELECT COALESCE(SUM(stock.tQty), 0) AS stockInTransitQty, batch.item.id AS itemId, stock.store.storeId as storeId, stock.unitId as unitId FROM StoreStock AS stock "
				+ "		INNER JOIN stock.batch AS batch ON (to_char(batch.expiryDate,'YYYY-MM-DD') >=:expiryDate "
						+ " or batch.expiryDate is null )"
				+ " WHERE stock.store.storeId = :storeId  AND stock.unitId = :unitId"
				+ "		AND stock.isActive = true AND stock.isDeleted = false AND stock.tQty > 0 group by batch.item.id, stock.store.storeId,stock.unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("expiryDate",GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getLastMonthSaleNewAll(Integer storeId, Integer unitId) {
		String sql = " select coalesce(sum(saledetail0_.lease_quantity), 0) AS \"lastMonthSale\",item4_.id AS \"itemId\"  "
				+ "from pharmacy.t_sale_detail saledetail0_ inner join pharmacy.t_sale sale1_ on saledetail0_.sale_id=sale1_.id  "
				+ "and (sale1_.is_deleted=false and sale1_.is_active=true and sale1_.unit_id=:unitId)  "
				+ "inner join public.m_store store2_ on sale1_.store_id=store2_.store_id and (store2_.store_id=:storeId  "
				+ "and store2_.is_deleted=false and store2_.is_active=true) inner join inventory.t_batch batch3_ on saledetail0_.batch_id=batch3_.id "
				+ "inner join public.m_item item4_ on batch3_.item_id=item4_.id  "
				+ "where saledetail0_.is_deleted=false and saledetail0_.is_active=true and saledetail0_.unit_id=:unitId "
				+ "and date_trunc('month', saledetail0_.added_date)=date_trunc('month', current_date - interval '1' month) group by item4_.id  ";

		Query query = getSession().createSQLQuery(sql).addScalar("lastMonthSale", StandardBasicTypes.LONG);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getCurrentMonthSaleAll(Integer storeId, Integer unitId) {
		String hql = "SELECT COALESCE(SUM(saleDetail.leaseQuantity), 0) AS currentMonthSale,item.id AS itemId FROM SaleDetail AS saleDetail "
				+ "		INNER JOIN saleDetail.sale AS sale WITH sale.isDeleted = false AND sale.isActive = true AND sale.unit.unitId = :unitId "
				+ "		INNER JOIN sale.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN saleDetail.batch AS batch "
				+ "		INNER JOIN batch.item AS item "
				+ "WHERE saleDetail.isDeleted = false AND saleDetail.isActive = true AND saleDetail.unit.unitId = :unitId "
				+ "		AND date_trunc('month', saleDetail.addedDate) = date_trunc('month', current_date) group by item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getStoreMaxQuantityAll(Integer storeId, Integer orgId) {
		String hql = "SELECT COALESCE(storeItem.maxQty, 0) AS maxStoreQty,item.id AS itemId, store.storeId as storeId, storeItem.organization.organizationId as orgId FROM StoreItemMapper AS storeItem "
				+ "		INNER JOIN storeItem.store AS store WITH store.storeId = :storeId AND store.isActive = true  AND store.isDeleted =false "
				+ "		INNER JOIN storeItem.item AS item WITH item.isActive = true  AND item.isDeleted =false "
				+ " WHERE storeItem.organization.organizationId =:organizationId "
				+ " 	AND storeItem.isActive = true  AND storeItem.isDeleted =false order by 1 desc";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("organizationId", orgId != null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}
    @Override
    public List<UomResponse> getAllUomListAll(Integer organizationId) {
        String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
                + "        uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
                + "        uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom,uom.item.id AS itemId "
                + "    FROM UnitOfMeasurement AS uom "
                + "        INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
                + "        INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
                + "    WHERE uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId=:organizationId "
                + "    ORDER BY uomType.id ";

        Query query = getSession().createQuery(hql);
        query.setParameter("organizationId", organizationId != null ? organizationId : sessionService.getOrganizationId());
        query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
        return query.list();
    }

	@Override
	public List<Batch> getBatchDetailsAll(Integer unitId, Integer toStoreId,List<Integer> itemIds) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		String hql = "SELECT batch FROM Batch AS batch " + "		INNER JOIN batch.item AS item "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND  storeStock.unit.unitId = :unitId  AND storeStock.availableQty > 0 "
				+ " AND (  batch.expiryDate >= :greaterThanCurrentMonth OR  batch.expiryDate is null ) and storeStock.availableQty is not null and storeStock.totalQty is not null "
				+ " AND item.isDeleted = false AND item.isActive = true AND batch.unit.unitId = :unitId AND item.id in(:itemIds)"
				+ " ORDER BY batch.expiryDate ASC";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", toStoreId);
		query.setParameterList("itemIds",itemIds);
		query.setParameter("greaterThanCurrentMonth", current);
		return query.list();
	}
	@Override
	public List<Batch> getBatchAll(Integer storeId, Integer unitId) {
		String hql = "SELECT batch FROM Batch AS batch " + "		 JOIN FETCH batch.storeStockList AS storeStock "
				+ "		 JOIN FETCH storeStock.store AS store "
				+ "WHERE batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		AND storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId "
				+ "		AND store.isDeleted = false AND store.isActive = true AND store.storeId = :storeId AND store.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<Batch> getBatchDetailsPharmacyConsignmentAll(Integer storeId, Integer unitId, List<Integer> subDetailsIds) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND item.id IN (:subDetailsIds) AND storeStock.availableQty  is not null AND storeStock.availableQty>0 and storeStock.totalQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null ) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameterList("subDetailsIds",subDetailsIds);
		return query.list();
	}

	@Override
	public List<UomResponse> getAllUomListAllByUnit(Integer unitId) {
		String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
				+ "		uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom,uom.item.id AS itemId "
				+ "	FROM UnitOfMeasurement AS uom "
				+ "		INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
				+ "	WHERE uom.isDeleted = false AND uom.isActive = true AND uom.unit.unitId=:unitId "
				+ "	ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<UomResponse> getStoreStockByItemPharmacyAll(List<Integer> subDetailsIds,Integer storeId, Integer unitId) {
		Date date = TimeZoneComponent.getDateByzone(unitId);
		String sql = "SELECT coalesce(stock.totalQty, 0) AS stockAvailable, item.id AS itemId " +
				"FROM public.m_item item " +
				"LEFT JOIN (SELECT item.id AS itemId, SUM(stock.available_qty) AS totalQty " +
				"           FROM public.m_item item " +
				"           INNER JOIN inventory.t_batch batch ON batch.item_id = item.id " +
				"           AND batch.is_deleted = false AND batch.is_active = true AND batch.is_quarantine = false " +
				"           AND (to_char(batch.expiry_date, 'YYYY-MM-DD') >= :expiryDate OR batch.expiry_date IS NULL) " +
				"           INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id " +
				"           AND batch.unit_id = :unitId AND stock.store_id = :storeId " +
				"           AND stock.is_deleted = false AND stock.is_active = true AND stock.available_qty > 0 " +
				"           AND stock.available_qty IS NOT NULL AND stock.total_qty IS NOT NULL AND stock.unit_id = :unitId " +
				"           GROUP BY item.id) stock ON stock.itemId = item.id " +
				"WHERE item.id IN (:id) AND item.is_deleted = false AND item.is_active = true " +
				"ORDER BY item.id";

		Query query = getSession().createSQLQuery(sql)
				.addScalar("stockAvailable", StandardBasicTypes.LONG)
				.addScalar("itemId", StandardBasicTypes.INTEGER);

		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("expiryDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"));
		query.setParameterList("id", subDetailsIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();

	}

	@Override
	public List<Batch> getBatchAll(List<Long> batchIds) {
		if (batchIds != null && batchIds.size() > 0) {
			String hql = "SELECT batch FROM Batch as batch  WHERE batch.id in(:id)  AND batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false ";
			Query query = getSession().createQuery(hql);
			query.setParameterList("id", batchIds);
			return query.list();
		}
		return null;
	}

	@Override
	public List<StoreStock> getStoreStockByBatchAndStoreAll(List<Long> batchIds, Integer storeId, Integer unitId) {
		String hql = " SELECT storeStock FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ "		WHERE store.storeId = :storeId AND batch.id in(:batchId)  "
				+ " 	AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.unit.unitId = :unitId and  storeStock.availableQty is not null  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameterList("batchId", batchIds);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			return  query.list();
	}

	@Override
	public List<PrescriptionDetailsForOPSalesWorklistDto> getPresDtlsByPresIdForWalkinSales(Integer prescriptionId,
			List<Integer> parentItemIds) {
		try {
            if(prescriptionId!=null && parentItemIds!=null && parentItemIds.size()>0)
            {
			String sql = "select CAST(presDtl.id as int) as \"prescriptionDetailsid\","
					+ " presDtl.organization_id  as \"organizationId\"," + " presDtl.unit_id  as \"unitId\","
					+ " presDtl.prescription_id  as \"prescriptionId\"," + " presDtl.item_id as \"itemId\","
					+ " presDtl.store_id as \"storeId\"," + " presDtl.uom_unit_id as \"uomUnitId\","
					+ " presDtl.order_qty as \"orderQty\"," + " presDtl.strength as \"strength\","
					+ " presDtl.prescription_date as \"prescriptionDate\","
					+ " presDtl.authorised_by  as \"authorisedBy\"," + " presDtl.authorised_date as \"authorisedDate\","
					+ " presDtl.is_pre_stop as \"isPreStop\"," + " presDtl.is_intervention as \"isIntervention\","
					+ " presDtl.updated_by as \"updatedBy\","
					+ " to_char(presDtl.updated_date,'YYYY-MM-DD HH:mm:ss') as \"updatedDate\","
					+ " presDtl.added_by as \"addedBy\"," + " presDtl.added_date as \"addedDate\","
					+ " presDtl.is_deleted as \"isDeleted\"," + " presDtl.is_active as \"isActive\","
					+ " presDtl.status_id as \"statusId\"," + " presDtl.remark as \"remark\","
					+ " presDtl.prnflag as \"prnflag\"," + " presDtl.statflag as \"statflag\","
					+ " presDtl.infusionflag as \"infusionflag\","
					+ " to_char(presDtl.pre_stop_start_date,'YYYY-MM-DD HH:mm:ss') as \"preStopStartDate\","
					+ " to_char(presDtl.pre_stop_end_date,'YYYY-MM-DD HH:mm:ss') as \"preStopEndDate\","
					+ " presDtl.instruction_id as \"instructionId\","
					+ " 	presDtl.instruction_desc as \"instructionText\","
					+ " presDtl.is_non_formulary as \"isNonFormulary\"," + " presDtl.generic_name as \"genericName\","
					+ " presDtl.item_name as \"itemName\"," + " presDtl.justification_stop as \"justificationStop\","
					+ " presDtl.is_medication_stop as \"isMedicationStop\","
					+ " presDtl.parent_substitute_id as \"parentSubstituteId\"," + " preFreDtl.dose as \"dose\","
					+ " preFreDtl.dose_unit_id as \"doseUnitId\"," + " preFreDtl.frequency_id as \"frequencyId\","
					+ " preFreDtl.route_id as \"routeId\"," + " preFreDtl.start_date as \"startDate\","
					+ " preFreDtl.stop_date as \"stopDate\"," + " preFreDtl.instructions as \"instructions\","
					+ " itm.is_narcotic as \"isNarcotic\" " + " from pharmacy.t_prescription_detail presDtl"
					+ " INNER JOIN pharmacy.t_prescription pres on pres.id=presDtl.prescription_id"
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id "
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " where presdtl.prescription_id=:id " + " and presdtl.item_id in(:itemId) ";

			Query query = getSession().createSQLQuery(sql).addScalar("prescriptionDetailsid")
					.addScalar("organizationId").addScalar("unitId").addScalar("itemId").addScalar("storeId")
					.addScalar("uomUnitId").addScalar("orderQty").addScalar("strength").addScalar("prescriptionId")
					.addScalar("prescriptionDate").addScalar("authorisedBy").addScalar("authorisedDate")
					.addScalar("instructionText").addScalar("isDeleted", new TrueFalseType())
					.addScalar("isActive", new TrueFalseType()).addScalar("statusId").addScalar("remark")
					.addScalar("prnflag", new TrueFalseType()).addScalar("statflag", new TrueFalseType())
					.addScalar("infusionflag", new TrueFalseType()).addScalar("instructionId")
					.addScalar("isNonFormulary").addScalar("genericName").addScalar("itemName")
					.addScalar("justificationStop").addScalar("isMedicationStop").addScalar("parentSubstituteId")
					.addScalar("dose").addScalar("doseUnitId").addScalar("frequencyId").addScalar("routeId")
					.addScalar("startDate").addScalar("stopDate").addScalar("instructions")
					.addScalar("isNarcotic", new TrueFalseType());
			query.setParameter("id", prescriptionId);
			query.setParameterList("itemId", parentItemIds);
			query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsForOPSalesWorklistDto.class));
			return query.list();
            }
		} catch (Exception e) {
		logger.error("getPresDtlsByPresIdForWalkinSales failed", e);
		}
		return null;
	}

	@Override
	public List<Batch> getBatchDetailsPharmacyConsignmentAll(Integer itemId, Integer storeId, Integer unitId) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty is not null and storeStock.totalQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null ) "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", itemId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		return query.list();
	}

	@Override
	public List<Batch> getBatchDetailsWalkInPrescription(Integer id, Integer unitId, Integer storeId, BatchProperty batchProperty) {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		String hql = "SELECT batch FROM Batch AS batch " + "		INNER JOIN batch.item AS item "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND  storeStock.unit.unitId = :unitId "
				+ " AND (  batch.expiryDate >= :greaterThanCurrentMonth OR  batch.expiryDate is null ) and storeStock.availableQty is not null and storeStock.totalQty is not null "
				+ " AND item.isDeleted = false AND item.isActive = true AND batch.unit.unitId = :unitId "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		query.setParameter("greaterThanCurrentMonth", current);
		return query.list();
	}

	@Override
	public Integer getIsFinalBill(Integer EncounterId, Integer addmissionId) {
		String SQL = "";
		if (EncounterId != null) {
			SQL = "SELECT is_interim_or_final_bill " +
					"FROM billing.m_billing_master " +
					"WHERE encounter_id = :encounterId " +
					"AND status = 'A' " +
					"ORDER BY billing_master_id DESC " +
					"LIMIT 1";
		} else if (addmissionId != null) {
			SQL = "SELECT is_interim_or_final_bill " +
					"FROM billing.m_billing_master " +
					"WHERE admission_id = :admissionId " +
					"AND status = 'A' " +
					"ORDER BY billing_master_id DESC " +
					"LIMIT 1";
		}
		Query query = getSession().createSQLQuery(SQL);
		if (EncounterId != null) {
			query.setParameter("encounterId", EncounterId);
		} else if (addmissionId != null) {
			query.setParameter("admissionId", addmissionId);
		}
		return (Integer) query.uniqueResult();
	}

	@Override
	public List<UomResponse> getUnitOfMeasurementListByItemIds(List<Integer> itemsIds, Integer organizationId) {
		String hql = "SELECT uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		uomType.code || ' - ' || uomUnit.units AS uomDetails, uom.conversions AS conversion, "
				+ "		uom.ipUom AS ipUom, uom.opUom AS opUom, uom.storeUom AS storeUom , uom.item.id AS itemId "
				+ "	FROM UnitOfMeasurement AS uom "
				+ "		INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive=true AND uomUnit.isDeleted = false "
				+ "	WHERE uom.item.id in(:itemIds) AND uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId=:organizationId "// HIS-16396
				+ "	ORDER BY uomType.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("itemIds", itemsIds);
		query.setParameter("organizationId", organizationId != null ? organizationId : sessionService.getOrganizationId());
		// HIS-12019
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<Batch> getBatchDetailsOnReturnAcceptance(Integer id, Integer storeId, Integer unitId) {

		String hql = "SELECT batch FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "
				+ "WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId "
				+ " ORDER BY batch.expiryDate ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("storeId", storeId);
		return query.list();
	
	}

	@Override
	public String getcarrier(Integer id) {
		String SQL=" select  " + 
				" (case when mem.user_id is not null then  mem.first_name else mdm.first_name end ) " + 
				" from  pharmacy.t_issue ti    " + 
				" left join m_employee_master mem  on mem.user_id  = ti.carrier_id  " + 
				" left join doctor.m_doctor_master mdm  on mdm.user_id  = ti.carrier_id  " + 
				" where ti.id  =:id  ";
        Query query = getSession().createSQLQuery(SQL);
		query.setParameter("id",id);
        return  (String) query.uniqueResult();
	}

	@Override
	public RegistrationFieldConfiguration getRegistrationFieldConfigurationByUnitId(Integer id) {
		try {
			String query = " select "
					+ "	regconf.mobile_digit as \"mobileDigit\" from "
					+ "	public.t_unit_application_registration_configuration regconf "
					+ "	where regconf.unit_id =:unitId order by unit_appl_registration_config_id desc limit 1";

			RegistrationFieldConfiguration dto = (RegistrationFieldConfiguration) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId",id)
					.setResultTransformer(Transformers.aliasToBean(RegistrationFieldConfiguration.class)).uniqueResult();
			return dto;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getRegistrationFieldConfigurationByUnitId failed", e);
		return null;
		}
	}

    @Override
    public List<CommonBillingItemsDto> getGrnInfoByBatchId(List<Long> batchIds) {
        String query = "SELECT grn.id as \"grnId\", " +
                "   grn.grn_number as \"grnNumber\", " +
                "   batch.id as \"batchId\" " +
                "   FROM inventory.t_batch batch " +
                "   Inner join procurement.t_good_receipt_note_detail grnDtl on " +
                "       grnDtl.batch_id = batch.id " +
                "   Inner join procurement.t_good_receipt_note grn on " +
                "       grn.id = grnDtl.grn_id " +
                "   WHERE batch.id in (:batchIds) ";
        List<CommonBillingItemsDto> grnInfoList = getSession().createSQLQuery(query)
                .addScalar("grnId", StandardBasicTypes.INTEGER)
                .addScalar("grnNumber", StandardBasicTypes.STRING)
                .addScalar("batchId", StandardBasicTypes.LONG)
                .setParameterList("batchIds", batchIds)
                .setResultTransformer(Transformers.aliasToBean(CommonBillingItemsDto.class)).list();
        return grnInfoList;
    }

//    @Override
//    public Integer updateBatchCurrentQuantityWithStoreStockQuantity(Batch batch) {
//
//        try {
//            Session session = getSession();
//            Integer result = session.createSQLQuery(" update inventory.t_batch batch set "
//                            + " current_quantity = ( SELECT COALESCE(SUM(stock.available_qty), 0) "
//                            + " FROM inventory.t_store_stock stock "
//                            + " WHERE stock.batch_id = batch.id "
//                            + " AND stock.unit_id = batch.unit_id "
//                            + " AND stock.organization_id = batch.organization_id) "
//                            + " where  batch.id = :batchId "
//                            + " and batch.unit_id = :unitId"
//                            + " and batch.organization_id = :orgId")
//                    .setParameter("batchId", batch.getId())
//                    .setParameter("unitId", batch.getUnitId())
//                    .setParameter("orgId", batch.getOrganizationId())
//                    .executeUpdate();
//
//            session.flush();
//            session.refresh(batch);
//            return result;
//        } catch (Exception e) {
//
//        }
//        return 0;
//    }

    @Override
    public Response updateBatchQuantityForBillable(List<BatchListResponse> batchList) {
        try {
            StringBuilder caseBuilder = new StringBuilder("UPDATE inventory.t_batch SET current_quantity = CASE id ");

            List<Long> batchIds = new ArrayList<>();

            for (BatchListResponse batch : batchList) {
                if((batch.getBatchId() != null && batch.getBatchId() >0) && batch.getAvailableQuantity() != null) {
                    caseBuilder.append(" WHEN ")
                            .append(batch.getBatchId())
                            .append(" THEN ")
                            .append(batch.getAvailableQuantity());
                    batchIds.add(batch.getBatchId());
                }
            }

            caseBuilder.append(" END WHERE id IN (:ids) ")
                    .append(" AND unit_id = :unitId ")
                    .append(" AND organization_id = :orgId ");

            Query query = getSession().createSQLQuery(caseBuilder.toString())
                    .setParameterList("ids", batchIds)
                    .setParameter("unitId", batchList.getFirst().getUnitId())
                    .setParameter("orgId", batchList.getFirst().getOrganizationId());

            query.executeUpdate();
            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        } catch (Exception e) {
        logger.error("updateBatchQuantityForBillable failed", e);
        return new Response(ERROR, ERROR_CODE, ERROR, null, null);
        }
    }
}
