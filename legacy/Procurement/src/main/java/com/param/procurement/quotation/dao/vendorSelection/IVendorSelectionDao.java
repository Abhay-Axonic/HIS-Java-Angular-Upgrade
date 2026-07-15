package com.param.procurement.quotation.dao.vendorSelection;

import java.util.List;

import com.param.entity.model.procurement.VendorNegotiationDetail;
import com.param.entity.model.procurement.VendorNegotiationMapper;
import com.param.entity.model.procurement.VendorSelection;
import com.param.entity.model.procurement.VendorSelectionMapper;
import com.param.procurement.quotation.dto.VendorNegotiationMapperDto;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionRequest;

public interface IVendorSelectionDao {

	List<VendorSelection> searchVendorSelectionList(VendorSelectionRequest vendorSelectionRequest, Integer screenId);

	VendorSelection getVendorSelection(Integer id);

	VendorSelection updateVendorSelection(VendorSelection vendorSelection);

	VendorSelection getVendorSelectionFornegotiation(Integer qrId);

	List<VendorSelectionMapper> getVendorSelectionMapperList(Integer id, List<Integer> ids);

	VendorNegotiationMapper getVendorNegotiationMapper(Integer id);

	VendorNegotiationDetail getVendorNegotiationDetail(Integer id);

	List<Integer> getMapperIds(Integer id);

	VendorNegotiationMapperDto getVendorNegotiationMapperByIdNew(int i);

	Integer getVendorNegotiationMapperId(Integer id);

	VendorNegotiationDetail getVendorNegotiationDetailById(Integer vendornegociationdetailid);

	void updateVendorSelectionMapper(VendorSelectionMapper mapper);

}
