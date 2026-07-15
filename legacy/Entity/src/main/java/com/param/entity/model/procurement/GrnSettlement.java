package com.param.entity.model.procurement;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "GrnSettlement")
@Table(name = "t_grn_settlement_receipt", schema = "procurement")
public class GrnSettlement extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "amount_data")
	private Double amountData;
	@Column(name = "receipt_number")
	private String receiptNumber;
	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getAmountReceivedFrom() {
		return amountReceivedFrom;
	}

	public void setAmountReceivedFrom(String amountReceivedFrom) {
		this.amountReceivedFrom = amountReceivedFrom;
	}


	@Column(name = "amount_received_from")
	private String amountReceivedFrom;
	@Column(name = "identificaiton_number")
	private  String identificaitonNumber;
	public String getIdentificaitonNumber() {
		return identificaitonNumber;
	}

	public void setIdentificaitonNumber(String identificaitonNumber) {
		this.identificaitonNumber = identificaitonNumber;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grnSettlement", cascade = CascadeType.ALL)
	private List<GrnSettlementDetail> grnSettlementDetailsList;

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmountData() {
		return amountData;
	}

	public void setAmountData(Double amountData) {
		this.amountData = amountData;
	}

	public List<GrnSettlementDetail> getGrnSettlementDetailsList() {
		return grnSettlementDetailsList;
	}

	public void setGrnSettlementDetailsList(List<GrnSettlementDetail> grnSettlementDetailsList) {
		this.grnSettlementDetailsList = grnSettlementDetailsList;
	}

	public GrnSettlementDetail addGrnSettlementDetailList(GrnSettlementDetail grnSettlementDetail) {
		if (getGrnSettlementDetailsList() == null)
			this.grnSettlementDetailsList = new ArrayList<GrnSettlementDetail>();

		getGrnSettlementDetailsList().add(grnSettlementDetail);
		grnSettlementDetail.setGrnSettlement(this);

		return grnSettlementDetail;
	}
}
