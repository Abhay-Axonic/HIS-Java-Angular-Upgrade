package com.param.billing.global.transaction.dto;

public class SmartCardMasterDto {

	
	 	private String globalId;
	    
	    private String memberNr;
	    
	    private Integer AdmitID;
	    
	    private Integer ProgressFlag;
	    
	    private Integer ExchangeType;
	    
	    private String RejectionReason;
	    
	    private Integer InOutType;
	    
	    private String LocationID;
	    
	    private String SmartDate;
	    
	    private String ExchangeDate;
	    
	    private String ResultDate;
	    
	    private byte[] SmartFile;
		
	    private byte[] ExchangeFile;
		
	    private byte[] ResultFile;
	    
	    private Double  Amount;
	    
	    private Integer Id;
	    
	    private String SmartFileString;
	    
	    
	    
	    

		public String getSmartFileString() {
			return SmartFileString;
		}

		public void setSmartFileString(String smartFileString) {
			SmartFileString = smartFileString;
		}

		public Integer getId() {
			return Id;
		}

		public void setId(Integer id) {
			Id = id;
		}

		public String getGlobalId() {
			return globalId;
		}

		public void setGlobalId(String globalId) {
			this.globalId = globalId;
		}

		public String getMemberNr() {
			return memberNr;
		}

		public void setMemberNr(String memberNr) {
			this.memberNr = memberNr;
		}

		public Integer getAdmitID() {
			return AdmitID;
		}

		public void setAdmitID(Integer admitID) {
			AdmitID = admitID;
		}

		public Integer getProgressFlag() {
			return ProgressFlag;
		}

		public void setProgressFlag(Integer progressFlag) {
			ProgressFlag = progressFlag;
		}

		public Integer getExchangeType() {
			return ExchangeType;
		}

		public void setExchangeType(Integer exchangeType) {
			ExchangeType = exchangeType;
		}

		public String getRejectionReason() {
			return RejectionReason;
		}

		public void setRejectionReason(String rejectionReason) {
			RejectionReason = rejectionReason;
		}

		public Integer getInOutType() {
			return InOutType;
		}

		public void setInOutType(Integer inOutType) {
			InOutType = inOutType;
		}

		public String getLocationID() {
			return LocationID;
		}

		public void setLocationID(String locationID) {
			LocationID = locationID;
		}

		public String getSmartDate() {
			return SmartDate;
		}

		public void setSmartDate(String smartDate) {
			SmartDate = smartDate;
		}

		public String getExchangeDate() {
			return ExchangeDate;
		}

		public void setExchangeDate(String exchangeDate) {
			ExchangeDate = exchangeDate;
		}

		public String getResultDate() {
			return ResultDate;
		}

		public void setResultDate(String resultDate) {
			ResultDate = resultDate;
		}

		public byte[] getSmartFile() {
			return SmartFile;
		}

		public void setSmartFile(byte[] smartFile) {
			SmartFile = smartFile;
		}

		public byte[] getExchangeFile() {
			return ExchangeFile;
		}

		public void setExchangeFile(byte[] exchangeFile) {
			ExchangeFile = exchangeFile;
		}

		public byte[] getResultFile() {
			return ResultFile;
		}

		public void setResultFile(byte[] resultFile) {
			ResultFile = resultFile;
		}

		public Double getAmount() {
			return Amount;
		}

		public void setAmount(Double amount) {
			Amount = amount;
		}

		@Override
		public String toString() {
			return "SmartCardMasterDto [globalId=" + globalId + ", memberNr=" + memberNr + ", AdmitID=" + AdmitID
					+ ", ProgressFlag=" + ProgressFlag + ", ExchangeType=" + ExchangeType + ", RejectionReason="
					+ RejectionReason + ", InOutType=" + InOutType + ", LocationID=" + LocationID + ", SmartDate="
					+ SmartDate + ", ExchangeDate=" + ExchangeDate + ", ResultDate=" + ResultDate + ", SmartFile="
					+ SmartFile + ", ExchangeFile=" + ExchangeFile + ", ResultFile=" + ResultFile + ", Amount=" + Amount
					+ "]";
		}

		
	   
	    
	    
}
