package com.param.procurement.consignment.dto.implantReturnAcceptance;


import java.util.List;

public class ImplantReturnAcceptanceAddItem {
	
private List<Integer> itemIds;
	
	private List<Integer> issueIds;
	
	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public List<Integer> getIssueIds() {
		return issueIds;
	}

	public void setIssueIds(List<Integer> issueIds) {
		this.issueIds = issueIds;
	}


}
