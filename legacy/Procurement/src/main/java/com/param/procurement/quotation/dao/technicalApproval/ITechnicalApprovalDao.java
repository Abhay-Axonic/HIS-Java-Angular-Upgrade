package com.param.procurement.quotation.dao.technicalApproval;

import java.util.List;

import com.param.entity.model.procurement.QuotationFill;
import com.param.entity.model.procurement.QuotationFillDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.TechnicalApproval;
import com.param.entity.model.procurement.TechnicalApprovalDetail;
import com.param.entity.model.procurement.VendorNegotiation;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalRequestDto;

public interface ITechnicalApprovalDao {

	List<TechnicalApproval> searchTechnicalApprovalList(TechnicalApprovalRequestDto techApprovalRequestDto,
			Integer screenId);

	TechnicalApproval getTechnicalApproval(Integer id);

	List<TechnicalApprovalDetail> getTechnicalApprovalDetail(Integer id);

	TechnicalApproval updateTechnicalApproval(TechnicalApproval technicalApproval);

	TechnicalApproval saveTechnicalApproval(TechnicalApproval technicalApproval);

	QuotationRequest getQuotationRequest(Integer qrId);

	List<QuotationFill> getQuotationFill(Integer qrId);
	
	List<QuotationRequestDetail> getQuotationRequestDetail(Integer qrId);

	QuotationRequest updateQuotationRequest(QuotationRequest quotationRequest);

	QuotationFill getQuotationFillById(Integer qfId);

	VendorNegotiation saveVendorNegotiation(VendorNegotiation vendorNegotiation);

}
