package com.param.global.dto.sync;

public class RadiologySyncDto {
	private Integer hospitalId;
	private Integer unitId;
	private String documentPath;
	private String serviceName;
	private String uuid;
//	private String pdfGenerationUrl;

	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/*public String getPdfGenerationUrl() {
		return pdfGenerationUrl;
	}
	public void setPdfGenerationUrl(String pdfGenerationUrl) {
		this.pdfGenerationUrl = pdfGenerationUrl;
	}
	*/
	
}
