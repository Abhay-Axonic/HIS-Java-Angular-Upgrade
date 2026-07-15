package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.PharmanDrugDto;

public interface IPharmanDao{
	
	public Response getPharmanDrugList(PharmanDrugDto pharmanDrugDto);

	public Response getPharmanDrugFormByName(PharmanDrugDto pharmanDrugDto);

	public Response getPopUpData(PharmanDrugDto pharmanDrugDto);

	public Response getPopUpDataWithId(PharmanDrugDto pharmanDrugDto);
	
}
