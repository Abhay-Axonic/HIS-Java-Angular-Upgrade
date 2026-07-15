package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_AMBULANCE_CHARGES_LIST",
			query = "SELECT ambCharge.ambulance_charges_id as \"ambulanceChargesId\", pat.uhid_number as \"uhidNumber\",  " + 
					"  CONCAT(coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"patientName\",   " + 
					"  ambCharge.distance as \"distance\", ambCharge.ambulance_provider as \"ambulanceProvider\",  ambType.ambulance_type as \"ambulanceTypeName\",   " + 
					"  ambCharge.ambulance_type as \"ambulanceType\",ambCharge.lpo_to_location as \"lpoToLocation\", ambCharge.lpo_no as \"lpoNo\", ambCharge.lpo_date as \"lpoDate\",   " + 
					"  ambCharge.from_address as \"fromAddress\", ambCharge.to_address as \"toAddress\", ambCharge.lpo_from_location as \"lpoFromLocation\",    " +
					"  ambCharge.lpo_vender_name as \"lpoVenderName\", ambCharge.lpo_ambulance_provider as \"lpoAmbulanceProvider\", ambCharge.lpo_distance as \"lpoDistance\",  " + 
					"  ambCharge.lpo_vender_address as \"lpoVenderAddress\", ambCharge.lpo_amount as \"lpoAmount\", ambCharge.out_source_vender_name as \"outSourceVenderName\",   " + 
					"  ambCharge.vehicle_no as \"vehicleNo\" , ambCharge.patient_id as \"patientId\" , ambCharge.order_details_id as \"orderDetailsId\" , tom.order_no as \"orderNo\","
					+ " (tod.self_payable + tod.credit_payable) as \"totalAmount\" , tbd.billing_master_id as \"billingMasterId\" ,tod.status as \"status\" " + 
					" FROM adt.m_ambulance_charges ambCharge   " + 
					" INNER JOIN patient.t_patient_registration pat on ambCharge.patient_id = pat.patient_id   " + 
					" INNER JOIN adt.m_ambulance_tarrif ambType	on ambCharge.ambulance_type = ambType.ambulance_tarrif_master_id "
					+" inner join  t_order_details tod  on tod.order_details_id  = ambcharge.order_details_id " + 
					" inner join t_order_master tom on tom.order_id  = tod.order_id   "
					+ " left join billing.t_billing_details tbd  on tbd.order_details_id = tod.order_details_id " + 
					" WHERE ambCharge.organization_id=:organizationId  " + 
					" ORDER BY ambCharge.ambulance_charges_id DESC"),
	
	@NamedNativeQuery(name="GET_AMBULANCE_CHARGES_SINGLE_REC",
			query = "SELECT ambCharge.ambulance_charges_id as \"ambulanceChargesId\", ambCharge.distance as \"distance\", ambCharge.ambulance_provider as \"ambulanceProvider\",   " + 
					"  ambCharge.ambulance_type as \"ambulanceType\",ambCharge.lpo_to_location as \"lpoToLocation\", ambCharge.lpo_no as \"lpoNo\", ambCharge.lpo_date as \"lpoDate\",   " + 
					"  ambCharge.from_address as \"fromAddress\", ambCharge.to_address as \"toAddress\", ambCharge.lpo_from_location as \"lpoFromLocation\",    " +
					"  ambCharge.lpo_vender_name as \"lpoVenderName\", ambCharge.lpo_ambulance_provider as \"lpoAmbulanceProvider\", ambCharge.lpo_distance as \"lpoDistance\",  " + 
					"  ambCharge.lpo_vender_address as \"lpoVenderAddress\", ambCharge.lpo_amount as \"lpoAmount\", ambCharge.out_source_vender_name as \"outSourceVenderName\",   " + 
					"  ambCharge.vehicle_no as \"vehicleNo\" , ambCharge.patient_id as \"patientId\" " + 
					" FROM adt.m_ambulance_charges ambCharge   " + 
					" WHERE ambCharge.organization_id=:organizationId  " + 
					" AND ambCharge.ambulance_charges_id=:ambulanceChargesId" +
					" ORDER BY ambCharge.ambulance_charges_id DESC"),
	
})
	

@Entity
@Table(name = "m_ambulance_charges", schema = "adt")
@SequenceGenerator(name = "m_ambulance_charges_seq", sequenceName = "adt.m_ambulance_charges_seq", allocationSize = 1)
public class AmbulanceCharges {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ambulance_charges_seq")
	@Column(name = "ambulance_charges_id")
	private int ambulanceChargesId;

	@Column(name = "patient_id",updatable = false)
	private int patientId;
	
	@Column(name = "bill_no")
	private String billNo;
	
	@Column(name = "from_address")
	private String fromAddress;
	
	@Column(name = "to_address")
	private String toAddress;
	
	@Column(name = "ambulance_type")
	private int ambulanceType ;
	
	@Column(name = "distance")
	private String distance;
	
	@Column(name = "ambulance_provider")
	private String ambulanceProvider;
	
	@Column(name = "vehicle_no")
	private String vehicleNo;
	
	@Column(name = "out_source_vender_name")
	private String outSourceVenderName;
	
	@Column(name = "lpo_no")
	private String lpoNo;
	
	@Column(name = "lpo_date")
	private String lpoDate;
	
	@Column(name = "lpo_vender_name")
	private String lpoVenderName;
	
	@Column(name = "lpo_vender_address")
	private String lpoVenderAddress;
	
	@Column(name = "lpo_ambulance_provider")
	private String lpoAmbulanceProvider;
	
	@Column(name = "lpo_from_location")
	private String lpoFromLocation;
	
	@Column(name = "lpo_to_location")
	private String lpoToLocation;
	
	@Column(name = "lpo_distance")
	private String lpoDistance;
	
	@Column(name = "lpo_amount")
	private String lpoAmount;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name="organization_id")
	private int organizationId;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "ambulance_tarrif_master_id", insertable = false,
	 * updatable = false) private AmbulanceTarrif ambType;
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration pat;

	public int getAmbulanceChargesId() {
		return ambulanceChargesId;
	}

	public void setAmbulanceChargesId(int ambulanceChargesId) {
		this.ambulanceChargesId = ambulanceChargesId;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getAmbulanceType() {
		return ambulanceType;
	}

	public void setAmbulanceType(int ambulanceType) {
		this.ambulanceType = ambulanceType;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAmbulanceProvider() {
		return ambulanceProvider;
	}

	public void setAmbulanceProvider(String ambulanceProvider) {
		this.ambulanceProvider = ambulanceProvider;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getOutSourceVenderName() {
		return outSourceVenderName;
	}

	public void setOutSourceVenderName(String outSourceVenderName) {
		this.outSourceVenderName = outSourceVenderName;
	}

	public String getLpoNo() {
		return lpoNo;
	}

	public void setLpoNo(String lpoNo) {
		this.lpoNo = lpoNo;
	}

	public String getLpoDate() {
		return lpoDate;
	}

	public void setLpoDate(String lpoDate) {
		this.lpoDate = lpoDate;
	}

	public String getLpoVenderName() {
		return lpoVenderName;
	}

	public void setLpoVenderName(String lpoVenderName) {
		this.lpoVenderName = lpoVenderName;
	}

	public String getLpoVenderAddress() {
		return lpoVenderAddress;
	}

	public void setLpoVenderAddress(String lpoVenderAddress) {
		this.lpoVenderAddress = lpoVenderAddress;
	}

	public String getLpoAmbulanceProvider() {
		return lpoAmbulanceProvider;
	}

	public void setLpoAmbulanceProvider(String lpoAmbulanceProvider) {
		this.lpoAmbulanceProvider = lpoAmbulanceProvider;
	}

	public String getLpoFromLocation() {
		return lpoFromLocation;
	}

	public void setLpoFromLocation(String lpoFromLocation) {
		this.lpoFromLocation = lpoFromLocation;
	}

	public String getLpoToLocation() {
		return lpoToLocation;
	}

	public void setLpoToLocation(String lpoToLocation) {
		this.lpoToLocation = lpoToLocation;
	}

	public String getLpoDistance() {
		return lpoDistance;
	}

	public void setLpoDistance(String lpoDistance) {
		this.lpoDistance = lpoDistance;
	}

	public String getLpoAmount() {
		return lpoAmount;
	}

	public void setLpoAmount(String lpoAmount) {
		this.lpoAmount = lpoAmount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	
	
	
	
}
