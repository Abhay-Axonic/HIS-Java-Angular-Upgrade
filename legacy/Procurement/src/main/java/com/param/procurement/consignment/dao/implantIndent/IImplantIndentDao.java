package com.param.procurement.consignment.dao.implantIndent;

import java.util.List;

import com.param.entity.model.master.UnitOfMeasurement;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.entity.model.procurement.ImplantIndentDetail;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;

public interface IImplantIndentDao {

	List<ImplantIndent> searchImplantIndent(ImplantIndentRequest request, Integer screenId);

	ImplantIndent saveImplantIndent(ImplantIndent implantIndent);

	ImplantIndent implantIndentById(Integer id, Integer unitId);

	List<ImplantIndentDetail> implantIndentDetailById(Integer id);

	ImplantIndent updateImplantIndent(ImplantIndent implantIndent);

	Issue pendingIssuesSave(Issue issue);

	List<UnitOfMeasurement> getAllByItemId(Integer id);
	
}
