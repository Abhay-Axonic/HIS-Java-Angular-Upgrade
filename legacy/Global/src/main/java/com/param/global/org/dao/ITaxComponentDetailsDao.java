package com.param.global.org.dao;

import java.util.List;

import com.param.global.dto.TaxComponentDetailsDto;



public interface ITaxComponentDetailsDao {

	void saveTaxComponentDetails(TaxComponentDetailsDto dto)throws Exception;

	List<TaxComponentDetailsDto> getTaxComponentDetailsById(Integer id, Integer fromGrnOrreturn);

	void updateTaxComponentDetails(TaxComponentDetailsDto taxComponentDetailsDto);

}
