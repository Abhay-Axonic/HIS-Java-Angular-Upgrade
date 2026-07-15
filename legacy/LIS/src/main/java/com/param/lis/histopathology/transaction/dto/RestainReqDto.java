package com.param.lis.histopathology.transaction.dto;

public class RestainReqDto 
{
	private Integer tRestainingDetailId;
	private Integer tRestainingSubDetailId;
	private Integer tSubSpecimanId;
	private Character isNew;
	private Integer orgId;
	private Integer orgUnitId;
	private Integer tBlockId;
    private Character isHistoCyto;
	
	public Integer gettRestainingDetailId() {
		return tRestainingDetailId;
	}

	public void settRestainingDetailId(Integer tRestainingDetailId) {
		this.tRestainingDetailId = tRestainingDetailId;
	}

	public Integer gettRestainingSubDetailId() {
		return tRestainingSubDetailId;
	}

	public void settRestainingSubDetailId(Integer tRestainingSubDetailId) {
		this.tRestainingSubDetailId = tRestainingSubDetailId;
	}

	public Character getIsNew() {
		return isNew;
	}

	public void setIsNew(Character isNew) {
		this.isNew = isNew;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Character getIsHistoCyto() {
		return isHistoCyto;
	}

	public void setIsHistoCyto(Character isHistoCyto) {
		this.isHistoCyto = isHistoCyto;
	}

	public Integer gettBlockId() {
		return tBlockId;
	}

	public void settBlockId(Integer tBlockId) {
		this.tBlockId = tBlockId;
	}

	public Integer gettSubSpecimanId() {
		return tSubSpecimanId;
	}

	public void settSubSpecimanId(Integer tSubSpecimanId) {
		this.tSubSpecimanId = tSubSpecimanId;
	}

	
}
