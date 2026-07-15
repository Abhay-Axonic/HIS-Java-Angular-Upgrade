package com.param.pharmacy.master.global.service.tax;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.tax.GetTaxResponse;
import com.param.pharmacy.master.global.dto.tax.TaxDetailsResponse;
import com.param.pharmacy.master.global.dto.tax.TaxRequest;
import com.param.pharmacy.master.global.dto.tax.TaxResponseList;

public interface ITaxService {

	List<GetTaxResponse> getAll(Integer orgId);

	CommonResponse saveTax(TaxRequest request);

	List<TaxResponseList> getDropdownList(Integer orgId);

	TaxDetailsResponse getById(Integer id);

	CommonResponse updateTax(TaxRequest request) throws CustomeException;

}
