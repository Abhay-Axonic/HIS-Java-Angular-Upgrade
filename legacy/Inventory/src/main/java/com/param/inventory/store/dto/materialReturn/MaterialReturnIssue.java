package com.param.inventory.store.dto.materialReturn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.op.dto.salesReturn.ItemDetails;

@JsonInclude(Include.NON_NULL)
public class MaterialReturnIssue {
	private Integer issueid;
	private String issueno;
	private Date isuedate;
	private Integer issuedetailsid;
	private Integer itemid;
	private List<ItemDetails> itemDetails;
	protected List<ItemRespone> items;


	public void addItems(ItemRespone item) {
		if (this.items == null)
			this.items = new ArrayList<ItemRespone>();

		this.items.add(item);
	}

	public List<ItemRespone> getItems() {
		return items;
	}

	public void setItems(List<ItemRespone> items) {
		this.items = items;
	}

	public List<ItemDetails> getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(List<ItemDetails> itemDetails) {
		this.itemDetails = itemDetails;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getIssueid() {
		return issueid;
	}

	public void setIssueid(Integer issueid) {
		this.issueid = issueid;
	}

	public String getIssueno() {
		return issueno;
	}

	public void setIssueno(String issueno) {
		this.issueno = issueno;
	}

	public Date getIsuedate() {
		return isuedate;
	}

	public void setIsuedate(Date isuedate) {
		this.isuedate = isuedate;
	}

	public Integer getIssuedetailsid() {
		return issuedetailsid;
	}

	public void setIssuedetailsid(Integer issuedetailsid) {
		this.issuedetailsid = issuedetailsid;
	}
}
