package com.param.pharmacy.master.unit.service.implantItemMaster;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.implantItemMaster.GetItemResponse;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterDropdown;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterRequest;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterResponse;

public interface IImplantItemMasterService {

	ImplantItemMasterDropdown getDropdowns(Integer unitId, Integer orgId);
	
	List<ImplantItemMasterResponse> getAll(Integer unitId);
	
	GetItemResponse getById(Integer id);
	
	CommonResponse implantItemMasterSave(ImplantItemMasterRequest implantItemMasterSave);

	GetItemResponse update(ImplantItemMasterRequest itemRequest) throws CustomeException;

	}
