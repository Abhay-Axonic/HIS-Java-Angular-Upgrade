package com.param.pharmacy.op.service.salesReturn;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.billing.global.transaction.model.PaymentRefundMaster;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.pharmacy.Return;
import com.param.entity.model.pharmacy.ReturnDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleReturnDetailTaxComponenet;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.op.dao.salesReturn.ISalesReturnDao;
import com.param.pharmacy.op.dto.salesReturn.ItemDetails;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnDto;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnSave;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnSaveItem;
import com.param.pharmacy.op.dto.salesReturn.ResponseAddBill;
import com.param.pharmacy.op.dto.salesReturn.ResponseReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.ResponseSalesReturnBillList;
import com.param.pharmacy.op.dto.salesReturn.ReturnDetails;
import com.param.pharmacy.op.dto.salesReturn.ReturnItem;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnBillListSerach;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnQtyDetail;
import com.param.service.global.SessionService;

@Service("salesReturnListService")

@Transactional
public class SalesReturnService implements ISalesReturnService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ISalesReturnDao salesReturnDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(SalesReturnService.class);

	private Integer screenId = Screen.SaleReturn.getScreen();

	@Override
	public List<ResponseReturnListSearch> salesReturnListSearch(SalesReturnListSearch salesReturnList) {

		List<ResponseReturnListSearch> responseReturnListSearchList = new ArrayList<ResponseReturnListSearch>();
		salesReturnList.setToDate((new DateTime(salesReturnList.getToDate())).plusDays(1).toDate());

		for (Return returnItem : salesReturnDao.salesReturnListSearch(salesReturnList, screenId)) {
			if (returnItem.getSale().getPatient() != null) {

				Integer age = Util
						.getAgeByBirthDate(returnItem.getSale().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = returnItem.getSale().getPatient().getPrefix().getPrefixCode() + " "
						+ returnItem.getSale().getPatient().getFirstName() + " "
						+ returnItem.getSale().getPatient().getMiddleName() + " "
						+ returnItem.getSale().getPatient().getLastName() + " | " + age + " | "
						+ returnItem.getSale().getPatient().getGender().getGenderCode();
				ResponseReturnListSearch responseReturnListSearch = new ResponseReturnListSearch(returnItem.getId(),
						returnItem.getAddedDate(), returnItem.getReturnNo(),
						returnItem.getSale().getPatient().getUhidNumber(), patientDetail,
						returnItem.getSale().getBillNo(), returnItem.getSale().getAddedDate(),
						returnItem.getTotalReturnAmt(), returnItem.getStatus().getId(),
						returnItem.getStatus().getStatus());
				responseReturnListSearchList.add(responseReturnListSearch);
			} else if (returnItem.getSale().getTempPatient() != null) {
				String patientDetail = returnItem.getSale().getTempPatient().getPrefix().getPrefixCode() + " "
						+ returnItem.getSale().getTempPatient().getPatientName() + " | "
						+ returnItem.getSale().getTempPatient().getAge() + " | "
						+ returnItem.getSale().getTempPatient().getGender().getGenderCode();
				ResponseReturnListSearch responseReturnListSearch = new ResponseReturnListSearch(returnItem.getId(),
						returnItem.getAddedDate(), returnItem.getReturnNo(), null, patientDetail,
						returnItem.getSale().getBillNo(), returnItem.getSale().getAddedDate(),
						returnItem.getTotalReturnAmt(), returnItem.getStatus().getId(),
						returnItem.getStatus().getStatus());
				responseReturnListSearchList.add(responseReturnListSearch);
			}
		}
		return responseReturnListSearchList;

	}

	@Override
	public List<ResponseReturnListSearch> salesReturnListSearchBP(SalesReturnListSearch salesReturnList) {
		salesReturnList.setToDate((new DateTime(salesReturnList.getToDate())).plusDays(1).toDate());
		List<ResponseReturnListSearch> responseReturnListSearchs = salesReturnDao
				.salesReturnListSearchBP(salesReturnList, screenId);
		for (ResponseReturnListSearch responseReturnListSearch : responseReturnListSearchs) {
			String billNo = salesReturnDao.getMuiltipleBillNoByReturnId(responseReturnListSearch.getId());
			String paymentRefundVoucherNo = salesReturnDao.getPaymentRefundVoucherNoByReturnId(responseReturnListSearch.getId());
			responseReturnListSearch.setPaymentRefundVoucherNo(paymentRefundVoucherNo!= null ? paymentRefundVoucherNo : "");
			if (responseReturnListSearch.getPatientId() != null) {
				
				
				Integer age = 0;
				if(responseReturnListSearch.getPatientDob() != null) {
					age = Util.getAgeByBirthDate(responseReturnListSearch.getPatientDob().toLocalDateTime().toLocalDate());
				}
				
				responseReturnListSearch.setPatientDetail(responseReturnListSearch.getPatientDetail() != null ?  responseReturnListSearch.getPatientDetail()  : responseReturnListSearch.getPatientName()+ " | " + age);
				responseReturnListSearch.setTempPatId(null);
				responseReturnListSearch.setTempPatientDatail(null);
				if (billNo != null)
					responseReturnListSearch.setBillNo(billNo);
			} else if (responseReturnListSearch.getTempPatId() != null) {
				responseReturnListSearch.setPatientDetail(responseReturnListSearch.getTempPatientDatail());
				responseReturnListSearch.setPatientDob(null);
				responseReturnListSearch.setPatientId(null);
				if (billNo != null)
					responseReturnListSearch.setBillNo(billNo);
			}
		}
		return responseReturnListSearchs;
	}

	/**
	 * Sales Return Details Starts from Here
	 */

	@Override
	@Transactional
	public ReturnDetails salesReturnDetailsById(Integer id) {
		ReturnDetails returnDetails = new ReturnDetails();
		List<ReturnItem> returnItemList = new ArrayList<ReturnItem>();
		try {
			Return returnObject = salesReturnDao.salesReturnById(id, null);
			if (returnObject.getSale().getPatient() != null) {
				Integer age = Util.getAgeByBirthDate(
						returnObject.getSale().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = returnObject.getSale().getPatient().getPrefix().getPrefixCode() + " "
						+ returnObject.getSale().getPatient().getFirstName() + " "
						+ returnObject.getSale().getPatient().getMiddleName() + " "
						+ returnObject.getSale().getPatient().getLastName() + " | " + age + " | "
						+ returnObject.getSale().getPatient().getGender().getGenderCode();

				returnDetails = new ReturnDetails(returnObject.getId(),
						returnObject.getSale().getPatient().getUhidNumber(), patientDetail,
						returnObject.getSale().getPatient().getDob(), returnObject.getAddedDate(),
						returnObject.getReturnNo(), returnObject.getStore().getStoreId(),
						returnObject.getStore().getStoreName(), returnObject.getTotalReturnAmt(), returnItemList,
						returnObject.getStatus().getId(), returnObject.getStatus().getStatus());

			} else if (returnObject.getSale().getTempPatient() != null) {
				String patientDetail = returnObject.getSale().getTempPatient().getPrefix().getPrefixCode() + " "
						+ returnObject.getSale().getTempPatient().getPatientName() + " | "
						+ returnObject.getSale().getTempPatient().getAge() + " | "
						+ returnObject.getSale().getTempPatient().getGender().getGenderCode();

				returnDetails = new ReturnDetails(returnObject.getId(), patientDetail, returnObject.getAddedDate(),
						returnObject.getReturnNo(), returnObject.getStore().getStoreId(),
						returnObject.getStore().getStoreName(), returnObject.getTotalReturnAmt(), returnItemList,
						returnObject.getStatus().getId(), returnObject.getStatus().getStatus());
			}
			for (ReturnDetail returnResponse : salesReturnDao.salesReturnDetailsById(id)) {
				SimpleDateFormat month_date = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String actualDate = returnResponse.getSaleDetail().getBatch().getExpiryDate().toString();
				Date date = null;
				date = sdf.parse(actualDate);
				String month_name = month_date.format(date);

				ReturnItem returnItem = new ReturnItem(returnResponse.getId(),
						returnResponse.getSaleDetail().getBatch().getItem().getName(),
						returnResponse.getSaleDetail().getBatch().getItem().getGeneric().getName(),
						returnResponse.getSaleDetail().getBatch().getBatchNo(), month_name,
						returnResponse.getSaleDetail().getUomUnit().getUnits(),
						returnResponse.getSaleDetail().getQuantity(), returnResponse.getSaleDetail().getRate(),
						returnResponse.getSaleDetail().getTotalAmount(),
						returnResponse.getSaleDetail().getDiscountAmount(),
						returnResponse.getSaleDetail().getNetAmount(), returnResponse.getReturnQty(),
						returnResponse.getReturnAmt(), returnResponse.getRemark(), returnResponse.getReturnTaxAmount());
				returnItemList.add(returnItem);
			}
			return returnDetails;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public ReturnDetails salesReturnDetailsByIdBP(Integer id, Integer unitId) {
		ReturnDetails returnDetails = new ReturnDetails();
		List<ReturnItem> returnItemList = new ArrayList<ReturnItem>();
		try {
			returnDetails = salesReturnDao.salesReturnByIdBP(id, unitId);
			if (returnDetails.getPatientId() != null) {
				if(returnDetails.getPatientDob() != null ){
					Integer age = Util.getAgeByBirthDate(returnDetails.getPatientDob().toLocalDateTime().toLocalDate());
					returnDetails.setPatientDetail(returnDetails.getPatientDetail() + " | " + age);
				}else{
					returnDetails.setPatientDetail(returnDetails.getPatientDetail());
				}
				
				returnDetails.setTempPatId(null);
				returnDetails.setTempPatientDatail(null);

				if (returnDetails.getTotalreturnPaidAmount() == null)
					returnDetails.setTotalreturnPaidAmount(0D);
			} else if (returnDetails.getTempPatientDatail() != null) {
				returnDetails.setPatientDetail(returnDetails.getTempPatientDatail());
				returnDetails.setPatientDob(null);
				returnDetails.setPatientId(null);
				if (returnDetails.getTotalreturnPaidAmount() == null)
					returnDetails.setTotalreturnPaidAmount(0D);
			}

			returnItemList = salesReturnDao.salesReturnDetailsByIdBP(id, unitId);
			for (ReturnItem returnItem : returnItemList) {
				SimpleDateFormat month_date = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String actualDate = returnItem.getBatchExpiryDate() != null ? returnItem.getBatchExpiryDate().toString()
						: null;
				Date date = null;

				String month_name = null;
				if (actualDate != null) {
					date = sdf.parse(actualDate);
					month_name = month_date.format(date);
				}
				List<Long> batchIds = new ArrayList<>();
				batchIds.add(returnItem.getBatchId());
				List<UomResponse> uomsAll = new ArrayList<UomResponse>();
				if (!batchIds.isEmpty())
					uomsAll = GlobalDao.getBatchUomListPharmacyBP(batchIds);

				returnItem.setUom(uomsAll);
				returnItem.setExpiryDate(month_name);
				returnItem.setSaleId(returnItem.getSaleId());
				returnItem.setBillNo(returnItem.getBillNo());
			}
			returnDetails.setItem(returnItemList);
			return returnDetails;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Sales Return Bill list search starts from here
	 */
	@Transactional
	public List<ResponseSalesReturnBillList> salesReturnBillListSerach(
			SalesReturnBillListSerach salesReturnBillListSerach) {

		List<ResponseSalesReturnBillList> responseSalesReturnBillList = new ArrayList<ResponseSalesReturnBillList>();
		ResponseSalesReturnBillList responseSalesReturnBill = new ResponseSalesReturnBillList();
		salesReturnBillListSerach.setToDate((new DateTime(salesReturnBillListSerach.getToDate())).plusDays(1).toDate());
		List<Sale> sales = salesReturnDao.searchSalesReturnBills(salesReturnBillListSerach, screenId);
		for (Sale sale : salesReturnDao.searchSalesReturnBills(salesReturnBillListSerach, screenId)) {

			if (sale.getPatient() != null) {
				Integer age = Util.getAgeByBirthDate(sale.getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = sale.getPatient().getPrefix().getPrefixCode() + " "
						+ sale.getPatient().getFirstName() + " " + sale.getPatient().getMiddleName() + " "
						+ sale.getPatient().getLastName() + " | " + age + sale.getPatient().getGender().getGenderCode();
				if (sale.getAdmission() != null) {
					responseSalesReturnBill = new ResponseSalesReturnBillList(sale.getId(), sale.getAddedDate(),
							sale.getBillNo(), sale.getPatient().getUhidNumber(), patientDetail,
							sale.getAdmission().getCreatedDate(), sale.getAdmission().getAdmissionNumber(),
							sale.getNetAmount());
					responseSalesReturnBillList.add(responseSalesReturnBill);
				} else if (sale.getEncounter() != null) {
					responseSalesReturnBill = new ResponseSalesReturnBillList(sale.getId(), sale.getAddedDate(),
							sale.getBillNo(), sale.getPatient().getUhidNumber(), patientDetail,
							sale.getEncounter().getCreatedDate(), sale.getEncounter().getEncounterNumber(),
							sale.getNetAmount());
					responseSalesReturnBillList.add(responseSalesReturnBill);
				} else {
					responseSalesReturnBill = new ResponseSalesReturnBillList(sale.getId(), sale.getAddedDate(),
							sale.getBillNo(), sale.getPatient().getUhidNumber(), patientDetail, null, null,
							sale.getNetAmount());
					responseSalesReturnBillList.add(responseSalesReturnBill);
				}
			} else if (sale.getTempPatient() != null) {
				String patientDetail = sale.getTempPatient().getPrefix().getPrefixCode() + " "
						+ sale.getTempPatient().getPatientName() + " | " + sale.getTempPatient().getAge() + " | "
						+ sale.getTempPatient().getGender().getGenderCode();
				responseSalesReturnBill = new ResponseSalesReturnBillList(sale.getId(), sale.getAddedDate(),
						sale.getBillNo(), patientDetail, sale.getNetAmount());
				responseSalesReturnBillList.add(responseSalesReturnBill);
			}

		}
		return responseSalesReturnBillList;
	}

	@Override
	@Transactional
	public List<ResponseSalesReturnBillList> salesReturnBillListSerachBP(
			SalesReturnBillListSerach salesReturnBillListSerach) {
		List<ResponseSalesReturnBillList> responseSalesReturnBillList = new ArrayList<ResponseSalesReturnBillList>();
		salesReturnBillListSerach.setToDate((new DateTime(salesReturnBillListSerach.getToDate())).plusDays(1).toDate());

		List<Integer> statusesList = new ArrayList<Integer>();
		statusesList.add(com.param.common.constants.Status.Completed.getStatus());
		statusesList.add(com.param.common.constants.Status.PartiallyCompleted.getStatus());

		responseSalesReturnBillList = salesReturnDao.searchSalesReturnBillsBP(salesReturnBillListSerach, screenId,
				statusesList);
		for (ResponseSalesReturnBillList r : responseSalesReturnBillList) {
			Long id = r.getSaleId().longValue();
			r.setId(id);
		}
		
		for (ResponseSalesReturnBillList r : responseSalesReturnBillList) {
			if (r.getPatientId() != null) {
				
				Integer age = 0;
				
				if(r.getPatientDob() != null ) {
					age = Util.getAgeByBirthDate(r.getPatientDob().toLocalDateTime().toLocalDate());
				}
				r.setPatientDetail(r.getPatientDetail() + " | " + age);

			} else if (r.getTempPatId() != null) {
				r.setPatientDetail(r.getPatientDetail() + " | " + r.getAge());
			}
			if (r.getAdmissionId() != null) {
				r.setVisitDate(r.getAdmissionDate());
				r.setVisitNo(r.getAdmissionNumber());
			} else if (r.getEncounterId() != null) {
				r.setVisitDate(r.getEncounterDate());
				r.setVisitNo(r.getEncounterNumber());
			}

		}
		List<Long> saleIds = responseSalesReturnBillList.stream().map(x -> x.getId()).collect(Collectors.toList());
		List<Long> saleIdForNotSattled = salesReturnDao.getSaleIdForNotReturned(saleIds);
		List<ResponseSalesReturnBillList> responseSalesReturnBillListNew1 = responseSalesReturnBillList.stream()
				.filter(x -> saleIdForNotSattled.contains(x.getId())).collect(Collectors.toList());
		if (responseSalesReturnBillListNew1.size() > 0) {
			return responseSalesReturnBillListNew1;
		}
		return responseSalesReturnBillList;
	}

	@Override
	@Transactional
	public ResponseAddBill salesReturnGetBillById(Long id) {
		try {
			Sale saleDetail = salesReturnDao.salesReturnGetBillById(id);
			if (saleDetail != null) {
				ResponseAddBill responseAddBill = new ResponseAddBill();
				List<ItemDetails> itemList = new ArrayList<ItemDetails>();
				if (saleDetail.getPatient() != null) {
					Integer age = Util
							.getAgeByBirthDate(saleDetail.getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = saleDetail.getPatient().getPrefix().getPrefixCode() + " "
							+ saleDetail.getPatient().getFirstName() + " " + saleDetail.getPatient().getMiddleName()
							+ " " + saleDetail.getPatient().getLastName() + " | " + age + " | "
							+ saleDetail.getPatient().getGender().getGenderCode();

					responseAddBill = new ResponseAddBill(saleDetail.getId(), saleDetail.getBillNo(),
							saleDetail.getPatient().getUhidNumber(), patientDetail, saleDetail.getSaleType().getId(),
							saleDetail.getSaleType().getType(), itemList);

				} else if (saleDetail.getTempPatient() != null) {
					String patientDetail = saleDetail.getTempPatient().getPrefix().getPrefixCode() + " "
							+ saleDetail.getTempPatient().getPatientName() + " | "
							+ saleDetail.getTempPatient().getAge() + " | "
							+ saleDetail.getTempPatient().getGender().getGenderCode();
					responseAddBill = new ResponseAddBill(saleDetail.getId(), saleDetail.getBillNo(), patientDetail,
							saleDetail.getSaleType().getId(), saleDetail.getSaleType().getType(), itemList);

				}
				for (SaleDetail saleDetails : saleDetail.getSaleDetailsList()) {
					Item item = saleDetails.getBatch().getItem();
					SimpleDateFormat month_date = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String actualDate = saleDetails.getBatch().getExpiryDate().toString();
					Date date = null;
					date = sdf.parse(actualDate);
					String month_name = month_date.format(date);
					Integer totalReturnQuantity = 0;
					for (ReturnDetail returnDetail : salesReturnDao.populateReturnDetail(saleDetails.getId())) {
						if (returnDetail != null) {
							if (returnDetail.getIsDeleted() == false && returnDetail.getIsActive() == true) {
								totalReturnQuantity = totalReturnQuantity + returnDetail.getReturnQty();
							}
						}
					}
					ItemDetails itemObject = new ItemDetails(saleDetails.getId(), item.getId(), item.getName(),
							item.getGeneric().getName(), saleDetails.getBatch().getBatchNo(), month_name,
							saleDetails.getUomType().getType(), saleDetails.getUomType().getId(),
							saleDetails.getQuantity(), saleDetails.getRate(), saleDetails.getTotalAmount(),
							saleDetails.getDiscountAmount(), saleDetails.getTaxAmount(),
							saleDetails.getUnitDiscountAmount(), saleDetails.getNetAmount(), totalReturnQuantity,
							Util.PopulateUom(saleDetails.getBatch()), saleDetails.getTaxPercentage());
					itemList.add(itemObject);
				}
				return responseAddBill;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public List<ResponseAddBill> salesReturnGetBillByIdBP(List<Long> id) {
		try {

			List<ResponseAddBill> responseAddBillList = salesReturnDao.salesReturnGetBillByIdBP(id);

			for (ResponseAddBill responseAddBill : responseAddBillList) {
				if (responseAddBill.getPatientId() != null && responseAddBill.getPatientDob() != null) {
					Integer age = Util
							.getAgeByBirthDate(responseAddBill.getPatientDob().toLocalDateTime().toLocalDate());
					responseAddBill.setPatientDetail(responseAddBill.getPatientDetail() + " | " + age);
				} else {
					responseAddBill.setPatientDetail(responseAddBill.getPatientDetail());
				}

				List<ItemDetails> saleDetailsResp = salesReturnDao.saleDetailsBySaleId(responseAddBill.getId());
				List<Long> saleDetailsIds = saleDetailsResp.stream().map(x -> x.getId()).collect(Collectors.toList());
				List<Integer> itemIds = saleDetailsResp.stream().map(x -> x.getItemId()).collect(Collectors.toList());
				List<Long> batchIds = saleDetailsResp.stream().filter(x -> x.getBatchId() != null)
						.map(x -> x.getBatchId()).collect(Collectors.toList());
				List<SalesReturnQtyDetail> salesReturnQtyDetailsAll = salesReturnDao
						.populateReturnDetailBP(saleDetailsIds);
				List<ItemDetails> patientIssueReturnList = salesReturnDao.patientIssueReturnList(saleDetailsIds);
				
				List<UomResponse> uomsAll = new ArrayList<UomResponse>();
				if (!batchIds.isEmpty())
					uomsAll = GlobalDao.getBatchUomListPharmacyBP(batchIds);

				for (ItemDetails saleDetail : saleDetailsResp) {
					SimpleDateFormat month_date = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String actualDate = saleDetail.getBatchExpiryDate() != null
							? saleDetail.getBatchExpiryDate().toString()
							: null;
					Date date = null;
					String month_name = null;
					if (actualDate != null) {
						date = sdf.parse(actualDate);
						month_name = month_date.format(date);
					}
					
					Integer totalReturnQuantity = 0;
					List<SalesReturnQtyDetail> salesReturnQtyDetailSpecific = salesReturnQtyDetailsAll.stream()
							.filter(x -> x.getSaleDetailId().equals(saleDetail.getId())).collect(Collectors.toList());

					if (salesReturnQtyDetailSpecific != null && !salesReturnQtyDetailSpecific.isEmpty())
						totalReturnQuantity = salesReturnQtyDetailSpecific.getFirst().getTotalReturnQuantity().intValue();
					
					
					if (patientIssueReturnList != null && patientIssueReturnList.size() > 0) {
						
						List<ItemDetails> se = patientIssueReturnList.stream()
								.filter(x -> x.getSaleDetailsId().equals(saleDetail.getId().intValue())).collect(Collectors.toList());
						
						if (se != null && !se.isEmpty())
							totalReturnQuantity = totalReturnQuantity + se.getFirst().getQuantityReturned();
					}
					
					
					if (saleDetail.getBatchId() != null && saleDetail.getBatchId() > 0) {
						saleDetail.setUom(uomsAll.stream().filter(x -> x.getBatchId().equals(saleDetail.getBatchId()))
								.collect(Collectors.toList()));
					} else {
						saleDetail.setUom(GlobalDao.getBatchUomListPharmacyByItemId(saleDetail.getItemId()));
					}

					saleDetail.setExpiryDate(month_name);
					saleDetail.setQuantityReturned(totalReturnQuantity);
				}
				// responseAddBill.setItem(saleDetailsResp);
				// start code for resolved HIS-9583 Return refund bills are showing in get bills
				// in case multiple bills are settled on sales return
				List<ItemDetails> saleDetailsRespNew = saleDetailsResp.stream()
						.filter(x -> x.getQuantityIssued() > x.getQuantityReturned()).collect(Collectors.toList());
				responseAddBill.setItem(saleDetailsRespNew);
				// end code for resolved HIS-9583 Return refund bills are showing in get bills
				// in case multiple bills are settled on sales return

			}
			return responseAddBillList;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

//	@Override
//	@Transactional
//	public ResponseAddBill salesReturnGetBillByIdBP(Long id) {
//		try{
//			ResponseAddBill responseAddBill = salesReturnDao.salesReturnGetBillByIdBP(id);
//			if(responseAddBill.getPatientId() != null && responseAddBill.getPatientDob() != null) {
//				Integer age = Util
//						.getAgeByBirthDate(responseAddBill.getPatientDob().toLocalDateTime().toLocalDate());
//				responseAddBill.setPatientDetail(responseAddBill.getPatientDetail() + " | " + age);
//			}
//			
//			List<ItemDetails> saleDetailsResp = salesReturnDao.saleDetailsBySaleId(responseAddBill.getId());
//			List<Long> saleDetailsIds = saleDetailsResp.stream().map(x -> x.getId()).collect(Collectors.toList());
//			List<Long> batchIds = saleDetailsResp.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
//			List<SalesReturnQtyDetail> salesReturnQtyDetailsAll = salesReturnDao.populateReturnDetailBP(saleDetailsIds);
//			List<UomResponse> uomsAll = new ArrayList<UomResponse>();
//			if(!batchIds.isEmpty())
//				uomsAll = GlobalDao.getBatchUomListPharmacyBP(batchIds);
//			
//			for(ItemDetails saleDetail : saleDetailsResp){
//				SimpleDateFormat month_date = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String actualDate = saleDetail.getBatchExpiryDate().toString();
//				Date date = null;
//				date = sdf.parse(actualDate);
//				String month_name = month_date.format(date);
//				
//				Integer totalReturnQuantity = 0;
//				List<SalesReturnQtyDetail> salesReturnQtyDetailSpecific = salesReturnQtyDetailsAll.stream()
//						.filter(x -> x.getSaleDetailId().equals(saleDetail.getId())).collect(Collectors.toList());
//				if(salesReturnQtyDetailSpecific != null && !salesReturnQtyDetailSpecific.isEmpty())
//					totalReturnQuantity = salesReturnQtyDetailSpecific.get(0).getTotalReturnQuantity().intValue();
//				
//				saleDetail.setUom(uomsAll.stream().filter(x -> x.getBatchId().equals(saleDetail.getBatchId()))
//						.collect(Collectors.toList()));
//				saleDetail.setExpiryDate(month_name);
//				saleDetail.setQuantityReturned(totalReturnQuantity);
//			}
//			responseAddBill.setItem(saleDetailsResp);
//			return responseAddBill;
//		}catch (Exception e) {
//			
//		}
//		return null;
//	}
//	

	@Override
	@Transactional
	public CommonResponse saveSalesReturn(RequestSaleReturnSave request) {
		try {

			return salesReturnSave(request);
//			if(request.ValidateSave())
//			{ 
//				 return salesReturnSave(request);
//			}
//			else{
//				throw new IllegalArgumentException(request.getValidationMessage());
//			}
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private CommonResponse salesReturnSave(RequestSaleReturnSave requestSaleReturnSave) {

		/*
		 * Integer storeId = requestSaleReturnSave.getStoreId(); Store storeNew =
		 * GlobalDao.getStore(storeId); Unit unit =
		 * GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String retNumber = GlobalService.getBillNoByGeneratedCode(requestSaleReturnSave.getStoreId(),
				requestSaleReturnSave.getUnitId(), screenId);

		Return returnObject = new Return(requestSaleReturnSave.getTotalRefundAmount(),
				new Sale(requestSaleReturnSave.getSaleId()), new Store(requestSaleReturnSave.getStoreId()), retNumber);
		returnObject.setStatus(new Status(requestSaleReturnSave.getStatusId()));
		/// returnObject.setTotalreturnPaidAmount(requestSaleReturnSave.getTotalRefundAmount());
		// sessionService.setEntityDefaults(returnObject);
		returnObject.setCreateDefaults(
				requestSaleReturnSave.getOrganizationId() != null ? requestSaleReturnSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				requestSaleReturnSave.getUnitId() != null ? requestSaleReturnSave.getUnitId()
						: sessionService.getUnitId(),
				requestSaleReturnSave.getUserId() != null ? requestSaleReturnSave.getUserId()
						: sessionService.getUserId());

		ReturnDetail returnDetail = null;

		for (RequestSaleReturnSave requestSaleReturnSaveObj : requestSaleReturnSave.getReuquestSaleReturnList()) {
			for (RequestSaleReturnSaveItem item : requestSaleReturnSaveObj.getItem()) {
				ReturnDetail returnDetail2 = new ReturnDetail(new SaleDetail(item.getSaleDetailId()),
						(item.getReturnQuantity() != null) ? item.getReturnQuantity() : null,
						item.getLeaseReturnQuantity(), item.getRefundAmount(),
						(item.getReason() != null) ? item.getReason() : null, item.getReturnTaxAmount());

				// sessionService.setEntityDefaults(returnDetail2);
				returnDetail2.setCreateDefaults(
						requestSaleReturnSave.getOrganizationId() != null ? requestSaleReturnSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						requestSaleReturnSave.getUnitId() != null ? requestSaleReturnSave.getUnitId()
								: sessionService.getUnitId(),
						requestSaleReturnSave.getUserId() != null ? requestSaleReturnSave.getUserId()
								: sessionService.getUserId());

				returnDetail2.setSaleId(item.getSaleId());
				returnDetail = returnObject.addReturnDetailList(returnDetail2);

				SaleDetail saleDetail = salesReturnDao.getSaleDetailById(item.getSaleDetailId(),requestSaleReturnSave.getUnitId());

				Integer taxId = salesReturnDao.getTaxId(saleDetail.getId());
				// check saleDetail tax id
/*				if (taxId > 0) {
					for (TaxDetail taxDetail : GlobalDao.getTaxDetail(saleDetail.getTax().getId())) {

						Integer taxCompId = taxDetail.getTaxComponent().getId();
						Double taxPercentage = taxDetail.getTaxPercentage();
						Double amt = item.getReturnTaxAmount();

						Double calTaxAmt = (amt * taxPercentage) / 100;

						SaleReturnDetailTaxComponenet saleReturnDetailTaxComponenet = new SaleReturnDetailTaxComponenet(
								calTaxAmt, new ReturnDetail(returnObject.getReturnDetailList().get(0).getId()),
								taxPercentage, new TaxComponent(taxCompId));
						// sessionService.setEntityDefaults(saleReturnDetailTaxComponenet);
						saleReturnDetailTaxComponenet.setCreateDefaults(
								requestSaleReturnSave.getOrganizationId() != null
										? requestSaleReturnSave.getOrganizationId()
										: sessionService.getOrganizationId(),
								requestSaleReturnSave.getUnitId() != null ? requestSaleReturnSave.getUnitId()
										: sessionService.getUnitId(),
								requestSaleReturnSave.getUserId() != null ? requestSaleReturnSave.getUserId()
										: sessionService.getUserId());

						returnDetail.addSaleDetailTaxComponenet(saleReturnDetailTaxComponenet);
					}
				}*/
				
		for (TaxDetail taxDetail : GlobalDao.getTaxDetail(saleDetail.getTax().getId())) {

					Integer taxCompId = taxDetail.getTaxComponent().getId();
					Double taxPercentage = taxDetail.getTaxPercentage();
					Double disAmt = item.getDiscount() != null ? item.getDiscount() : 0;
					Double amt = item.getRefundAmount();
                    Double calTaxAmt = (amt * taxPercentage)/(100 + item.getTaxPercentage());

					SaleReturnDetailTaxComponenet saleReturnDetailTaxComponenet = new SaleReturnDetailTaxComponenet(
							calTaxAmt, new ReturnDetail(returnObject.getReturnDetailList().getFirst().getId()),
							taxPercentage, new TaxComponent(taxCompId));
					// sessionService.setEntityDefaults(saleReturnDetailTaxComponenet);
					saleReturnDetailTaxComponenet.setCreateDefaults(
							requestSaleReturnSave.getOrganizationId() != null
									? requestSaleReturnSave.getOrganizationId()
									: sessionService.getOrganizationId(),
							requestSaleReturnSave.getUnitId() != null ? requestSaleReturnSave.getUnitId()
									: sessionService.getUnitId(),
							requestSaleReturnSave.getUserId() != null ? requestSaleReturnSave.getUserId()
									: sessionService.getUserId());

					returnDetail.addSaleDetailTaxComponenet(saleReturnDetailTaxComponenet);
				}



			}
		}

		// sessionService.setEntityDefaults(returnDetail);
		returnDetail.setCreateDefaults(
				requestSaleReturnSave.getOrganizationId() != null ? requestSaleReturnSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				requestSaleReturnSave.getUnitId() != null ? requestSaleReturnSave.getUnitId()
						: sessionService.getUnitId(),
				requestSaleReturnSave.getUserId() != null ? requestSaleReturnSave.getUserId()
						: sessionService.getUserId());

		salesReturnDao.salesReturnDetailsSave(returnObject);
		//updateStoreStockTransitQuantity(requestSaleReturnSave, retNumber); 
		CommonResponse response = new CommonResponse();
		response.setId(returnObject.getId().longValue());
		response.setMessage("Return Details saved successfully");

		return response;

	}

//	private CommonResponse salesReturnSave(RequestSaleReturnSave requestSaleReturnSave) {
//		
//		  Integer storeId = requestSaleReturnSave.getStoreId();
//			Store storeNew = GlobalDao.getStore(storeId);
//		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
//
//		String retNumber = GlobalService.getBillNoByGeneratedCode(storeNew, unit, screenId);
//
//		Return returnObject = new Return(requestSaleReturnSave.getTotalRefundAmount(),
//				new Sale(requestSaleReturnSave.getSaleId()), new Store(requestSaleReturnSave.getStoreId()), retNumber);
//				returnObject.setStatus(new Status(requestSaleReturnSave.getStatusId()));
//				sessionService.setEntityDefaults(returnObject);	
//
//		ReturnDetail returnDetail = null;
//		
//		for (RequestSaleReturnSaveItem item : requestSaleReturnSave.getItem())
//		{
//			ReturnDetail returnDetail2=new ReturnDetail(new SaleDetail(item.getSaleDetailId()),
//					(item.getReturnQuantity() != null) ? item.getReturnQuantity() : null, item.getLeaseReturnQuantity(),
//					item.getRefundAmount(), (item.getReason() != null) ? item.getReason() : null,
//					item.getReturnTaxAmount());
//			sessionService.setEntityDefaults(returnDetail2);	
//			returnDetail = returnObject.addReturnDetailList(returnDetail2);
//
//			SaleDetail saleDetail = salesReturnDao.getSaleDetailById(item.getSaleDetailId());
//
//			for (TaxDetail taxDetail : GlobalDao.getTaxDetail(saleDetail.getTax().getId())) {
//
//				Integer taxCompId = taxDetail.getTaxComponent().getId();
//				Double taxPercentage = taxDetail.getTaxPercentage();
//				Double amt = item.getRefundAmount();
//
//				Double calTaxAmt = (amt * taxPercentage) / 100;
//
//				SaleReturnDetailTaxComponenet saleReturnDetailTaxComponenet=	new SaleReturnDetailTaxComponenet(calTaxAmt,
//						new ReturnDetail(returnObject.getReturnDetailList().get(0).getId()), taxPercentage,
//						new TaxComponent(taxCompId));
//				sessionService.setEntityDefaults(saleReturnDetailTaxComponenet);	
//				returnDetail.addSaleDetailTaxComponenet(saleReturnDetailTaxComponenet);
//			}
//
//		}
//		sessionService.setEntityDefaults(returnDetail);	
//		salesReturnDao.salesReturnDetailsSave(returnObject);
//		updateStoreStockAndBatch(requestSaleReturnSave);
//		CommonResponse response = new CommonResponse();
//		response.setId(returnObject.getId().longValue());
//		response.setMessage("Return Details saved successfully");
//
//		return response;
//
//	}

	/*
	 * private CommonResponse salesReturnSaveBP(RequestSaleReturnSave
	 * requestSaleReturnSave) {
	 * 
	 * Integer storeId = requestSaleReturnSave.getStoreId(); Store storeNew =
	 * GlobalDao.getStore(storeId); Unit unit =
	 * GlobalDao.getUnit(sessionService.getUnitId());
	 * 
	 * String retNumber =
	 * GlobalService.getBillNoByGeneratedCode(requestSaleReturnSave.getStoreId(),
	 * sessionService.getUnitId(), screenId);
	 * 
	 * Return returnObject = new
	 * Return(requestSaleReturnSave.getTotalRefundAmount(), new
	 * Sale(requestSaleReturnSave.getSaleId()), new
	 * Store(requestSaleReturnSave.getStoreId()), retNumber);
	 * returnObject.setStatus(new Status(requestSaleReturnSave.getStatusId()));
	 * sessionService.setEntityDefaults(returnObject);
	 * 
	 * 
	 * ReturnDetail returnDetail = null;
	 * 
	 * List<Long> saleDetailIds = requestSaleReturnSave.getItem().stream().map(x ->
	 * x.getSaleDetailId()).collect(Collectors.toList()); List<SaleDetail>
	 * saleDetails = salesReturnDao.getSaleDetailByIdBP(saleDetailIds);
	 * List<TaxDetail> taxDetails =
	 * GlobalDao.getTaxDetailBP(saleDetails.stream().map(x ->
	 * x.getTax().getId()).collect(Collectors.toList()));
	 * 
	 * for (RequestSaleReturnSaveItem item : requestSaleReturnSave.getItem()) {
	 * ReturnDetail returnDetail2=new ReturnDetail(new
	 * SaleDetail(item.getSaleDetailId()), (item.getReturnQuantity() != null) ?
	 * item.getReturnQuantity() : null, item.getLeaseReturnQuantity(),
	 * item.getRefundAmount(), (item.getReason() != null) ? item.getReason() : null,
	 * item.getReturnTaxAmount()); sessionService.setEntityDefaults(returnDetail2);
	 * returnDetail = returnObject.addReturnDetailList(returnDetail2);
	 * 
	 * List<SaleDetail> saleDetailSpecific = saleDetails.stream() .filter(x ->
	 * x.getId().equals(item.getSaleDetailId())).collect(Collectors.toList());
	 * if(saleDetailSpecific != null && !saleDetailSpecific.isEmpty()) { SaleDetail
	 * saleDetail = saleDetailSpecific.get(0); for (TaxDetail taxDetail :
	 * taxDetails.stream().filter(x ->
	 * x.getTax().equals(saleDetail.getTax())).collect(Collectors.toList())) {
	 * 
	 * Integer taxCompId = taxDetail.getTaxComponent().getId(); Double taxPercentage
	 * = taxDetail.getTaxPercentage(); Double amt = item.getRefundAmount();
	 * 
	 * Double calTaxAmt = (amt * taxPercentage) / 100;
	 * 
	 * SaleReturnDetailTaxComponenet saleReturnDetailTaxComponenet= new
	 * SaleReturnDetailTaxComponenet(calTaxAmt, new
	 * ReturnDetail(returnObject.getReturnDetailList().get(0).getId()),
	 * taxPercentage, new TaxComponent(taxCompId));
	 * sessionService.setEntityDefaults(saleReturnDetailTaxComponenet);
	 * returnDetail.addSaleDetailTaxComponenet(saleReturnDetailTaxComponenet); } } }
	 * sessionService.setEntityDefaults(returnDetail);
	 * salesReturnDao.salesReturnDetailsSave(returnObject);
	 * updateStoreStockAndBatchBP(requestSaleReturnSave, saleDetails);
	 * CommonResponse response = new CommonResponse();
	 * response.setId(returnObject.getId().longValue());
	 * response.setMessage("Return Details saved successfully");
	 * 
	 * return response;
	 * 
	 * }
	 */

	/*
	 * private void updateStoreStockAndBatchBP(RequestSaleReturnSave
	 * requestSaleReturnSave, List<SaleDetail> saleDetails) {
	 * 
	 * Store store = new Store(requestSaleReturnSave.getStoreId()); for
	 * (RequestSaleReturnSaveItem item : requestSaleReturnSave.getItem()) { if
	 * (item.getLeaseReturnQuantity() > 0) { List<SaleDetail> saleDetailSpecific =
	 * saleDetails.stream().filter(x ->
	 * x.getId().equals(item.getSaleDetailId())).collect(Collectors.toList());
	 * if(saleDetailSpecific == null || saleDetailSpecific.isEmpty()) { throw new
	 * IllegalArgumentException("Stock Adjustment is Under Process"); } SaleDetail
	 * saleDetail = saleDetailSpecific.get(0);
	 * 
	 * Batch batch = new Batch(saleDetail.getBatch().getId());
	 * sessionService.setEntityDefaults(batch); Integer quantity =
	 * saleDetail.getBatch().getCurrentQuantity(); Integer leaseQuantity =
	 * Util.GetLeaseQuantity(item.getLeaseReturnQuantity(),saleDetail.getBatch().
	 * getBatchUomList(), saleDetail.getUomType()); quantity = quantity +
	 * leaseQuantity; StoreStock storeStockDetail = null;
	 * 
	 * StoreStock storeStockList =
	 * GlobalDao.getStoreStockByBatchAndStore(saleDetail.getBatch().getId(),
	 * requestSaleReturnSave.getStoreId()); if (storeStockList == null) { StoreStock
	 * storeStock = new StoreStock(leaseQuantity, store, batch);
	 * sessionService.setEntityDefaults(storeStock); storeStockDetail =
	 * GlobalDao.saveStoreStockLeaseQuantity(storeStock); } else {
	 * storeStockList.setAvailableQty(quantity); storeStockDetail =
	 * GlobalDao.updateStoreStockLeaseQuantity(storeStockList); } // Batch
	 * batchObject = GlobalDao.getBatch(requestSaleReturnSave.getStoreId(), //
	 * saleDetail.getBatch().getId()); Batch batchObject =
	 * GlobalDao.getBatchFromSaleDetail(saleDetail.getBatch().getId());
	 * batchObject.setCurrentQuantity(quantity);
	 * GlobalDao.updateBatchLeaseQuantity(batchObject);
	 * 
	 * com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
	 * 
	 * StockTransaction stockTransaction = new
	 * StockTransaction(storeStockDetail.getAvailableQty(), new
	 * com.param.entity.model.master.Screen(screen.getId()), screen.getCode(),
	 * leaseQuantity, storeStockDetail.getBatch().getRate(), store, batch);
	 * 
	 * sessionService.setEntityDefaults(stockTransaction);
	 * GlobalDao.saveStockTransaction(stockTransaction); } } }
	 */

	private void updateStoreStockTransitQuantity(RequestSaleReturnSave requestSaleReturnSave, String retNumber) {
		Store store = new Store(requestSaleReturnSave.getStoreId());

		for (RequestSaleReturnSave requestSaleReturnSaveObj : requestSaleReturnSave.getReuquestSaleReturnList()) {
			for (RequestSaleReturnSaveItem item : requestSaleReturnSaveObj.getItem()) {
				if (item.getLeaseReturnQuantity() > 0) {
					SaleDetail saleDetail = GlobalDao.getSaleDetailById(item.getSaleDetailId());
					if (saleDetail == null) {
						throw new IllegalArgumentException("Stock Adjustment is Under Process");
					}

					StoreStock storeStockDetail = null;

					List<StoreStock> storeStockList = GlobalDao.getStoreStockByBatchAndStoreByItem(
							saleDetail.getBatch() != null ? saleDetail.getBatch().getId() : null,
							requestSaleReturnSave.getStoreId(), saleDetail.getItemId(),
							requestSaleReturnSave.getUnitId());

					com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
					/** Batch Required Change Ganesh */
					Integer returnQuantity = item.getLeaseReturnQuantity();
					for (StoreStock storeStock : storeStockList) {
						if (storeStock != null && returnQuantity > 0) {
							Batch batch = storeStock.getBatch();
							// comment to resolve HIS-9434 Unable to save the sales return request for
							// OP(User was altered from 5 to 277)
							// sessionService.setEntityDefaults(batch);
							// end resolve HIS-9434 Unable to save the sales return request for OP(User was
							// altered from 5 to 277)

							Integer quntity;
							if (storeStock.getTotalQty() >= returnQuantity) {
								quntity = storeStock.gettQty() + returnQuantity;
								returnQuantity = 0;
							} else {
								returnQuantity = returnQuantity - storeStock.gettQty();
								quntity = storeStock.gettQty() + returnQuantity;
							}

							// Integer availQuantity = storeStockList.getAvailableQty() -
							// item.getReturnQuantity();
							storeStock.settQty(quntity);
							// storeStockList.setAvailableQty(availQuantity);
							storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStock);
							StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
									new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(),
									storeStock.gettQty(), storeStockDetail.getBatch().getRate(), store, batch,
									saleDetail.getId().intValue());
							// sessionService.setEntityDefaults(stockTransaction);
							stockTransaction.setInQty(item.getLeaseReturnQuantity());
							stockTransaction.setTransactionDetialsId(item.getSaleDetailId().intValue());
							stockTransaction.setTransactionId(item.getSaleId().intValue());
							stockTransaction.setIsTransit(true);
							stockTransaction.setTransitNo(retNumber);
							stockTransaction.setItem(storeStockDetail.getBatch().getItem());
							stockTransaction.setCreateDefaults(
									requestSaleReturnSave.getOrganizationId() != null
											? requestSaleReturnSave.getOrganizationId()
											: sessionService.getOrganizationId(),
									requestSaleReturnSave.getUnitId() != null ? requestSaleReturnSave.getUnitId()
											: sessionService.getUnitId(),
									requestSaleReturnSave.getUserId() != null ? requestSaleReturnSave.getUserId()
											: sessionService.getUserId());

							GlobalDao.saveStockTransaction(stockTransaction);

						}
					}
				}
			}
		}
	}

//	private void updateStoreStockAndBatch(RequestSaleReturnSave requestSaleReturnSave) {
//		Store store = new Store(requestSaleReturnSave.getStoreId());
//		
//	 for(RequestSaleReturnSave requestSaleReturnSaveObj : requestSaleReturnSave.getReuquestSaleReturnList()){
//		for (RequestSaleReturnSaveItem item : requestSaleReturnSaveObj.getItem()) {
//			if (item.getLeaseReturnQuantity() > 0) {
//				SaleDetail saleDetail = GlobalDao.getSaleDetailById(item.getSaleDetailId());
//				if(saleDetail== null) {
//					throw new IllegalArgumentException("Stock Adjustment is Under Process");
//				}
//				Batch batch = new Batch(saleDetail.getBatch().getId());
//				sessionService.setEntityDefaults(batch);	
//				Integer quantity = saleDetail.getBatch().getCurrentQuantity();
//				Integer leaseQuantity = Util.GetLeaseQuantity(item.getLeaseReturnQuantity(),
//						saleDetail.getBatch().getBatchUomList(), saleDetail.getUomType());
//
//				quantity = quantity + leaseQuantity;
//				StoreStock storeStockDetail = null;
//			
//				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(saleDetail.getBatch().getId(),
//						requestSaleReturnSave.getStoreId());
//				if (storeStockList == null)
//				{
//					StoreStock storeStock = new StoreStock(leaseQuantity, store, batch);
//					sessionService.setEntityDefaults(storeStock);
//					storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
//				} else 
//				{
//					storeStockList.setAvailableQty(quantity);
//					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
//				}
//				// Batch batchObject = GlobalDao.getBatch(requestSaleReturnSave.getStoreId(),
//				// saleDetail.getBatch().getId());
//				Batch batchObject = GlobalDao.getBatchFromSaleDetail(saleDetail.getBatch().getId());
//				batchObject.setCurrentQuantity(quantity);
//				GlobalDao.updateBatchLeaseQuantity(batchObject);
//
//				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
//
//				StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
//						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
//						storeStockDetail.getBatch().getRate(), store, batch);
//				
//				sessionService.setEntityDefaults(stockTransaction);
//				GlobalDao.saveStockTransaction(stockTransaction);
//			}
//		}
//	 }
//	}

//	private void updateStoreStockAndBatch(RequestSaleReturnSave requestSaleReturnSave) {
//		Store store = new Store(requestSaleReturnSave.getStoreId());
//		for (RequestSaleReturnSaveItem item : requestSaleReturnSave.getItem()) {
//			if (item.getLeaseReturnQuantity() > 0) {
//				SaleDetail saleDetail = GlobalDao.getSaleDetailById(item.getSaleDetailId());
//				if(saleDetail== null) {
//					throw new IllegalArgumentException("Stock Adjustment is Under Process");
//				}
//				Batch batch = new Batch(saleDetail.getBatch().getId());
//				sessionService.setEntityDefaults(batch);	
//				Integer quantity = saleDetail.getBatch().getCurrentQuantity();
//				Integer leaseQuantity = Util.GetLeaseQuantity(item.getLeaseReturnQuantity(),
//						saleDetail.getBatch().getBatchUomList(), saleDetail.getUomType());
//
//				quantity = quantity + leaseQuantity;
//				StoreStock storeStockDetail = null;
//			
//				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(saleDetail.getBatch().getId(),
//						requestSaleReturnSave.getStoreId());
//				if (storeStockList == null)
//				{
//					StoreStock storeStock = new StoreStock(leaseQuantity, store, batch);
//					sessionService.setEntityDefaults(storeStock);
//					storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
//				} else 
//				{
//					storeStockList.setAvailableQty(quantity);
//					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
//				}
//				// Batch batchObject = GlobalDao.getBatch(requestSaleReturnSave.getStoreId(),
//				// saleDetail.getBatch().getId());
//				Batch batchObject = GlobalDao.getBatchFromSaleDetail(saleDetail.getBatch().getId());
//				batchObject.setCurrentQuantity(quantity);
//				GlobalDao.updateBatchLeaseQuantity(batchObject);
//
//				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
//
//				StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
//						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
//						storeStockDetail.getBatch().getRate(), store, batch);
//				
//				sessionService.setEntityDefaults(stockTransaction);
//				GlobalDao.saveStockTransaction(stockTransaction);
//			}
//		}
//	}

	@Override
	@Transactional
	public CommonResponse cancelsalesReturnById(Integer id, Integer StoreId, Integer unitId) {

		CommonResponse response = new CommonResponse();
		try {
			Return returnObject = salesReturnDao.salesReturnById(id, unitId);
			/*
			 * if (!revertAllocatedQuantity(returnObject, StoreId, unitId)) { throw new
			 * CustomException("Error while reverting the quantity"); }
			 */

			List<ReturnDetail> returnList = returnObject.getReturnDetailList();
			returnList.forEach(item -> item.setReturnQty(0));

			returnObject.setTotalReturnAmt(0.0);
			returnObject.setReturnDetailList(returnList);

			returnObject.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
			salesReturnDao.salesReturnUpdate(returnObject);

			response.setId(returnObject.getId().longValue());
			response.setMessage("Return cancel successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	private boolean revertAllocatedQuantity(Return returnObject, Integer storeId, Integer unitId) {
		if (returnObject != null && returnObject.getReturnDetailList() != null
				&& !returnObject.getReturnDetailList().isEmpty()) {
			List<ReturnDetail> returnDetails = returnObject.getReturnDetailList();

			for (ReturnDetail returnDetail : returnDetails) {

				Long batchId = returnDetail.getSaleDetail().getBatch() != null
						? returnDetail.getSaleDetail().getBatch().getId()
						: null;
				Store store = new Store(storeId);

				List<Batch> batchList = GlobalDao.getBatchList(storeId, batchId,
						returnDetail.getSaleDetail().getItemId(), unitId);
				Integer quantity = returnDetail.getSaleDetail().getLeaseQuantity();
				if(quantity == null) {
					quantity = returnDetail.getLeaseQty();
				}
				// returnDetail.getReturnQty() code to check
				for (Batch batch : batchList) {
					if (quantity > 0) {
						StoreStock storeStockDetail = null;
						Integer transitQuantity = 0;
						StoreStock storeStock = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), storeId, unitId);
						if (storeStock.gettQty() >= quantity) {
							transitQuantity = storeStock.gettQty() - quantity;
							quantity = transitQuantity;
						} else {
							quantity = quantity - storeStock.gettQty();
							transitQuantity = 0;
						}

						// Integer AvialQuantity = storeStock.getAvailableQty() + (2 *
						// returnDetail.getReturnQty());
						storeStock.settQty(transitQuantity);
						// storeStock.setAvailableQty(AvialQuantity);

						storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStock);
						com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
						StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
								new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(),
								transitQuantity, storeStockDetail.getBatch().getRate(), store, batch,
								returnDetail.getId());

						// sessionService.setEntityDefaults(stockTransaction);
						stockTransaction.setCreateDefaults(
								returnObject.getOrganizationId() != null ? returnObject.getOrganizationId()
										: sessionService.getOrganizationId(),
								returnObject.getUnitId() != null ? returnObject.getUnitId()
										: sessionService.getUnitId(),
								returnObject.getAddedByUser() != null ? returnObject.getAddedByUser()
										: sessionService.getUserId());

						GlobalDao.saveStockTransaction(stockTransaction);
					}

				}
			}
			return true;
		}
		return false;
	}

//	private boolean revertAllocatedQuantity(Return returnObject, Integer storeId) {
//		if (returnObject != null && returnObject.getReturnDetailList() != null && !returnObject.getReturnDetailList().isEmpty()) {
//			List<ReturnDetail> returnDetails = returnObject.getReturnDetailList();
//			
//			for (ReturnDetail returnDetail : returnDetails) {
//				Long batchId = returnDetail.getSaleDetail().getBatch().getId();
//				
//				Store store = new Store(storeId);				
//				
//				Integer quantity = returnDetail.getReturnQty();
//				
//				Batch batchObject = GlobalDao.getBatchFromSaleDetail(batchId);
//				Integer balanceQuantity = batchObject.getCurrentQuantity() - quantity;
//				batchObject.setCurrentQuantity(balanceQuantity);
//				GlobalDao.updateBatchLeaseQuantity(batchObject);			
//			
//				
//				StoreStock storeStockDetail = null;
//				StoreStock storeStock = GlobalDao.getStoreStockByBatchAndStore(batchId,
//						storeId);				
//				Integer balanceStoreQuantity = storeStock.getAvailableQty() - quantity;
//				storeStock.setAvailableQty(balanceStoreQuantity);
//				storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStock);
//
//				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);	
//			
//				StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
//						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), balanceStoreQuantity,
//						storeStockDetail.getBatch().getRate(), store, batchObject);					
//			
//				sessionService.setEntityDefaults(stockTransaction);
//				GlobalDao.saveStockTransaction(stockTransaction);
//			
//			}
//			return true;
//		}
//		return false;
//	}
//	
	@Override
	@Transactional
	public CommonResponse updateSalesReturnById(RequestSaleReturnSave request, Integer id, Integer unitId) throws Exception{
		CommonResponse response = new CommonResponse();
		try {
			Return returnObject = salesReturnDao.salesReturnById(id, unitId);
			returnObject.setStatus(new Status(request.getStatusId()));
			returnObject.setReceivedFrom(request.getReceivedFrom());
			Double TotalreturnPaidAmount = 0D;
			if (returnObject.getTotalreturnPaidAmount() != null)
				TotalreturnPaidAmount = returnObject.getTotalreturnPaidAmount() + request.getTotalreturnPaidAmount();
			else
				TotalreturnPaidAmount = request.getTotalreturnPaidAmount();

			Long getUpdatedEpochTime = getEpochTimeUsingPatientId(request.getPatientId());
			Long currentEpochTime = System.currentTimeMillis();

			if(TotalreturnPaidAmount > returnObject.getTotalReturnAmt() ||
					(getUpdatedEpochTime<currentEpochTime && getUpdatedEpochTime>(currentEpochTime-6000))) {
				response.setId((long) -1);
				response.setMessage("TotalPaidAmount is greater than TotalDueAmount");
				return response;
			}else {
				returnObject.setTotalreturnPaidAmount(TotalreturnPaidAmount);
			}
			if (returnObject.getStatus().getId() != 18)
				updateStoreStockandBatch(returnObject, request.getStoreId(), unitId,id);

			salesReturnDao.salesReturnUpdate(returnObject);
			response.setId(returnObject.getId().longValue());
			response.setMessage("Return updated successfully");

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		return response;
	}

	private Long getEpochTimeUsingPatientId(Integer patientId) {

		Long epochVal = 0L;

		try {
			String Query = "select to_char(prm.updated_date,'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\" from Billing.t_payment_refund_master prm " +
					"where prm.patient_id = :patientId " +
					" order by prm.payment_refund_id desc OFFSET 1 LIMIT 1 ";

			String formattedDate = (String) sessionFactory.getCurrentSession().createSQLQuery(Query)
					.setParameter("patientId", patientId)
					.uniqueResult();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));

			Date updatedDateByQuery = dateFormat.parse(formattedDate);
			if (updatedDateByQuery != null) {
				epochVal = updatedDateByQuery.getTime(); // This will return Epoch time in milliseconds
				return epochVal;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return epochVal;
	}



	private void updateStoreStockandBatch(Return returnObject, Integer storeId, Integer unitId, Integer returnId) {
		try {
			if (returnObject != null && returnObject.getReturnDetailList() != null
					&& !returnObject.getReturnDetailList().isEmpty()) {
				List<ReturnDetail> returnDetails = returnObject.getReturnDetailList();

				for (ReturnDetail returnDetail : returnDetails) {

					Long batchId = returnDetail.getSaleDetail().getBatch() != null
							? returnDetail.getSaleDetail().getBatch().getId()
							: null;
					Store store = new Store(storeId);
					List<Batch> batchList = GlobalDao.getBatchList(storeId, batchId,
							returnDetail.getSaleDetail().getItemId(), unitId);
					com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
					Integer returnQuantity = returnDetail.getLeaseQty();
					for (Batch batchObject : batchList) {

						if (returnQuantity > 0) {
							Integer balanceQuantity;
							Integer actualReturnQty;
							if (batchObject.getQuantity() >= returnQuantity) {
								balanceQuantity = batchObject.getCurrentQuantity() + returnQuantity;
								actualReturnQty = returnQuantity;
								returnQuantity = 0;
							} else {
								balanceQuantity = 0;
								returnQuantity = returnQuantity - returnDetail.getReturnQty();
								actualReturnQty = returnQuantity;
							}
							batchObject.setCurrentQuantity(balanceQuantity);
							GlobalDao.updateBatchLeaseQuantity(batchObject);
							StoreStock storeStockDetail = null;
							StoreStock storeStock = GlobalDao.getStoreStockByBatchAndStore1(batchObject.getId(),
									storeId, unitId);

							Integer transitQuantity = storeStock.gettQty() - actualReturnQty;
							Integer AvialQuantity = storeStock.getAvailableQty() + actualReturnQty;
							// Integer AvialQuantity = storeStock.getAvailableQty() + (2 *
							// returnDetail.getReturnQty());
							storeStock.settQty(transitQuantity >0 ? transitQuantity : 0);
							storeStock.setAvailableQty(AvialQuantity);

							storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStock);
							StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
									new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(),
									transitQuantity, storeStockDetail.getBatch().getRate(), store, batchObject,
									returnDetail.getId());
							stockTransaction.setTransactionId(returnId);
							stockTransaction.setInQty(actualReturnQty);
							// sessionService.setEntityDefaults(stockTransaction);
							stockTransaction.setCreateDefaults(
									returnObject.getOrganizationId() != null ? returnObject.getOrganizationId()
											: sessionService.getOrganizationId(),
									returnObject.getUnitId() != null ? returnObject.getUnitId()
											: sessionService.getUnitId(),
									returnObject.getAddedByUser() != null ? returnObject.getAddedByUser()
											: sessionService.getUserId());

							GlobalDao.saveStockTransaction(stockTransaction);

						}

					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Response getPatientNameSearchForSalesReturn(RequestSaleReturnDto requestSaleReturnDto) {
		try {

			return salesReturnDao.getPatientNameSearchForSalesReturn(requestSaleReturnDto);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	@Transactional
	public Double getsalesAmount(List<Long> saleIds, Integer selfPayeeId) {
		return salesReturnDao.getSalesAmount(saleIds, selfPayeeId);
	}

	@Override
	@Transactional
	public Double getReturnedAmount(List<Long> saleIds) {
		return salesReturnDao.getReturnedAmount(saleIds);
	}

}
