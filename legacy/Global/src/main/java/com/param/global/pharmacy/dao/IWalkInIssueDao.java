package com.param.global.pharmacy.dao;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Sale;

public interface IWalkInIssueDao {
	
    Issue saveIssue(Issue issue);

	Issue updateSaleforIssueId(Issue issue, Sale sale);

}
