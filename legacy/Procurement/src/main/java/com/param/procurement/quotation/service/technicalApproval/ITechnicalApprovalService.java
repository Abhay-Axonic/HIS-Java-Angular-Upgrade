package com.param.procurement.quotation.service.technicalApproval;

import com.param.procurement.quotation.dto.quotationRequest.QuotationReqDropDownDto;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalRequestDto;

public interface ITechnicalApprovalService {

	QuotationReqDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getTechnicalApprovalList(TechnicalApprovalRequestDto techApprovalRequestDto);

	Object saveTechnicalApproval(TechnicalApprovalRequestDto techApprovalRequestDto);

	Object getTechnicalApproval(Integer id);

	Object updateTechnicalApproval(TechnicalApprovalRequestDto techApprovalRequestDto);

	Object cancelTechnicalApproval(TechnicalApprovalRequestDto techApprovalRequestDto);

}
