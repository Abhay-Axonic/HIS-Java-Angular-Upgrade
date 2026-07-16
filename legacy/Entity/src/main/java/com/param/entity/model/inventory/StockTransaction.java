package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Screen;
import com.param.entity.model.master.Store;
import com.param.entity.model.pharmacy.BillableConsumptionDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "StockTransaction")
@Table(name = "t_stock_transaction", schema = "inventory")
public class StockTransaction extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	private Double cop;

	@Column(name = "current_bal")
	private Integer currentBal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_id")
	private Screen doc;

	@Column(name = "doc_type", length = 50)
	private String docType;

	@Column(name = "lending_rate")
	private Double lendingRate;

	private Integer quantity;

	private Double rate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	@Column(name = "is_transit") 
	protected Boolean isTransit = false;
	
	@Column(name = "transit_no", length = 50)
	private String transitNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details_id")
	private IssueDetail issue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billable_consumption_detail_id")
	private BillableConsumptionDetail billableConsumptionDetail;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "non_billable_consumption_detail_id")
	private NonBillableConsumptionDetail nonBillableConsumptionDetail;
	
	@Column(name = "transaction_details_id")
	 private Integer transactionDetialsId;
	
	
	@Column(name = "transaction_id")
	 private Integer transactionId;
	
	@Column(name = "in_qty")
	 private Integer inQty;
	
	@Column(name = "out_qty")
	 private Integer outQty;
	
	
	//@Column(name = "patient_id")
	//private Integer patientId;
//	public Integer getPatientId() {
//		return patientId;
//	}
//
//	public void setPatientId(Integer patientId) {
//		this.patientId = patientId;
//	}

	public Integer getTransactionDetialsId() {
		return transactionDetialsId;
	}

	public void setTransactionDetialsId(Integer transactionDetialsId) {
		this.transactionDetialsId = transactionDetialsId;
	}

	public NonBillableConsumptionDetail getNonBillableConsumptionDetail() {
		return nonBillableConsumptionDetail;
	}

	public void setNonBillableConsumptionDetail(NonBillableConsumptionDetail nonBillableConsumptionDetail) {
		this.nonBillableConsumptionDetail = nonBillableConsumptionDetail;
	}

	public BillableConsumptionDetail getBillableConsumptionDetail() {
		return billableConsumptionDetail;
	}

	public void setBillableConsumptionDetail(BillableConsumptionDetail billableConsumptionDetail) {
		this.billableConsumptionDetail = billableConsumptionDetail;
	}

	public IssueDetail getIssue() {
		return issue;
	}

	public void setIssue(IssueDetail issue) {
		this.issue = issue;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Boolean getIsTransit() {
		return isTransit;
	}

	public void setIsTransit(Boolean isTransit) {
		this.isTransit = isTransit;
	}

	public String getTransitNo() {
		return transitNo;
	}

	public void setTransitNo(String transitNo) {
		this.transitNo = transitNo;
	}

	public StockTransaction() {
	}
	
	public StockTransaction(Integer currentBal, Screen doc, String docType, Integer quantity, Store store, Integer transactionDetialsId) {
		super();
		this.currentBal = currentBal;
		this.doc = doc;
		this.docType = docType;
		this.quantity = quantity;
		this.store = store;
		this.transactionDetialsId=transactionDetialsId;
	}
	
	public StockTransaction(Integer currentBal, Screen doc, String docType,  Store store) {
		super();
		this.currentBal = currentBal;
		this.doc = doc;
		this.docType = docType;
		this.store = store;
	}

	public StockTransaction(Integer currentBal, Screen doc, String docType, Integer quantity, Store store, Batch batch,Integer transactionDetialsId) {
		this(currentBal, doc, docType, quantity, store , transactionDetialsId);
		this.batch = batch;
	}

	public StockTransaction(Integer currentBal, Screen doc, String docType, Integer quantity, Double rate, Store store,
			Batch batch, Integer transactionDetialsId) {
		this(currentBal, doc, docType, quantity, store, batch, transactionDetialsId);
		this.rate = rate;
	}

	public StockTransaction(Double cop, Integer currentBal, Screen doc, String docType, Double lendingRate,
			Integer quantity, Double rate, Store store, Batch batch, Integer transactionDetialsId) {
		this(currentBal, doc, docType, quantity, rate, store, batch, transactionDetialsId);
		this.cop = cop;
		this.lendingRate = lendingRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getCop() {
		return cop;
	}

	public void setCop(double cop) {
		this.cop = cop;
	}

	public Integer getCurrentBal() {
		return currentBal;
	}

	public void setCurrentBal(Integer currentBal) {
		this.currentBal = currentBal;
	}

	public Screen getDoc() {
		return doc;
	}

	public void setDoc(Screen doc) {
		this.doc = doc;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public double getLendingRate() {
		return lendingRate;
	}

	public void setLendingRate(double lendingRate) {
		this.lendingRate = lendingRate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getInQty() {
		return inQty;
	}

	public void setInQty(Integer inQty) {
		this.inQty = inQty;
	}

	public Integer getOutQty() {
		return outQty;
	}

	public void setOutQty(Integer outQty) {
		this.outQty = outQty;
	}

}