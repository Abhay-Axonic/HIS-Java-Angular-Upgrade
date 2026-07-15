package com.param.procurement.quotation.dao.vendorNegotiation;

import java.util.List;

import com.param.entity.model.procurement.VendorNegotiation;
import com.param.entity.model.procurement.VendorNegotiationMapper;
import com.param.entity.model.procurement.VendorSelection;
import com.param.entity.model.procurement.VendorSelectionMapper;
import com.param.procurement.quotation.dto.VendorNegotiationMapperDto;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationMapperResp;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationRequest;

public interface IVendorNegotiationDao {

	List<VendorNegotiation> searchVendorNegotiationList(VendorNegotiationRequest vendorNegotiationRequest,
			Integer screenId);

	VendorNegotiation getVendorNegotiation(Integer id);

	VendorNegotiation updateVendorNegotiation(VendorNegotiation vendorNegotiation);

	VendorSelection saveVendorSelection(VendorSelection vendorSelection);
	
	VendorSelectionMapper saveVendorSelectionForNew(VendorSelectionMapper vendorSelectionMapper);

	List<Integer> getMapperIds(Integer id);

	List<VendorNegotiationMapperDto> getVendorNegotiationMapperList(Integer id, List<Integer> ids);


}
