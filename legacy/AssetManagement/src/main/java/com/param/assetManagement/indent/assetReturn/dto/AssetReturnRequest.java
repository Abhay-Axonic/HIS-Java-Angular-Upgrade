package com.param.assetManagement.indent.assetReturn.dto;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;

public class AssetReturnRequest extends AssetIndentRequest {

	protected String returnNo;

	protected List<Integer> returnId;

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public List<Integer> getReturnId() {
		return returnId;
	}

	public void setReturnId(List<Integer> returnId) {
		this.returnId = returnId;
	}

}
