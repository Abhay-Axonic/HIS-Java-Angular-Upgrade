package com.param.service.dto;

import java.util.List;

import com.param.billing.global.transaction.config.dto.ChargeConversionDetailsDto;
import com.param.billing.global.transaction.config.dto.ChargeConversionMasterDto;


public class ChargeConversionReqDto {

	private Integer orgId;
	private Integer unitId;
	ChargeConversionMasterDto chargeConversionMasterDto;
	private List<ChargeConversionMasterDto> listChargeConversionMasterDto;
	private List<ChargeConversionDetailsDto> listChargeConversionDetailsDto;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public List<ChargeConversionDetailsDto> getListChargeConversionDetailsDto() {
		return listChargeConversionDetailsDto;
	}

	public void setListChargeConversionDetailsDto(List<ChargeConversionDetailsDto> listChargeConversionDetailsDto) {
		this.listChargeConversionDetailsDto = listChargeConversionDetailsDto;
	}

	public List<ChargeConversionMasterDto> getListChargeConversionMasterDto() {
		return listChargeConversionMasterDto;
	}

	public void setListChargeConversionMasterDto(List<ChargeConversionMasterDto> listChargeConversionMasterDto) {
		this.listChargeConversionMasterDto = listChargeConversionMasterDto;
	}

	public ChargeConversionMasterDto getChargeConversionMasterDto() {
		return chargeConversionMasterDto;
	}

	public void setChargeConversionMasterDto(ChargeConversionMasterDto chargeConversionMasterDto) {
		this.chargeConversionMasterDto = chargeConversionMasterDto;
	}
	
}
