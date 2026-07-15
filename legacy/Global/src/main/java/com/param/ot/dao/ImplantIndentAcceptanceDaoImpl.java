package com.param.ot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.service.global.SessionService;

@Repository
public class ImplantIndentAcceptanceDaoImpl extends AbstractDao<Integer, PatientIssueReturn>
implements IImplantIndentAcceptanceOTDao  {

	@Autowired
	private SessionService sessionService;
	
	@Override
	public PatientIssueReturn savePatientIssueReturn(PatientIssueReturn issueReturn) {
		getSession().save(issueReturn);
		return issueReturn;
	}

}
