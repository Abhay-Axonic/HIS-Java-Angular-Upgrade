package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PharmanDrugDto;
@Transactional
public interface IPharmanService {

	public Response getPharmanDrugList(PharmanDrugDto pharmanDrugDto);

	public Response getPharmanDrugFormByName(PharmanDrugDto pharmanDrugDto);

	public Response getPopUpData(PharmanDrugDto pharmanDrugDto);

	public Response getPopUpDataWithId(PharmanDrugDto pharmanDrugDto);

}
