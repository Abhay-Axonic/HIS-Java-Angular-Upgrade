package com.param.procurement.quotation.dao.quotationFill;

import java.util.List;

import com.param.entity.model.procurement.QuotationFill;
import com.param.entity.model.procurement.QuotationFillDetail;
import com.param.entity.model.procurement.QuotationRequestAccessoriesMapper;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.entity.model.procurement.TechnicalApproval;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillRequest;

public interface IQuotationFillDao {

	QuotationFill saveQuotationFill(QuotationFill quotationFill);

	QuotationFill updateQuotationFill(QuotationFill quotationFill);

	QuotationFill getQuotationFill(Integer id);

	List<QuotationFillDetail> getQuotationFillDetail(Integer id);

	List<QuotationVendor> getQuotationVendorSearchList(QuotationFillRequest quotationFillRequest,List<Integer> statusIds);

	QuotationVendor getQuotationVendor(Integer id);

	QuotationVendorDetail getQuotationVendorDetail(Integer itemId,Integer vqrId);

	List<QuotationFill> searchQuotationFillList(QuotationFillRequest quotationFillRequest, Integer screenId);

	List<QuotationRequestAccessoriesMapper> getQuotationRequestAccessoriesMapperList(Integer id);
	
	QuotationRequestAccessoriesMapper saveQuotationRequestAccessoriesMapper(QuotationRequestAccessoriesMapper quotationRequestAccessoriesMapper);

	List<QuotationRequestDetail> getQuotationRequestDetail(Integer qrId, List<Integer> qrDetailId);

	void updateQuotationReqDetail(QuotationRequestDetail r);

	Integer getMapperId(Integer id);

	void updateDetailscolumn(String details, Integer mapperId);


}
