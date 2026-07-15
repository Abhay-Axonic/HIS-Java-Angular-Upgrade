package com.param.inventory.store.service.recallDrug;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.inventory.store.dao.recallDrug.IRecallDrugDao;
import com.param.inventory.store.dto.RecallDrug.RecallDrugRequest;

@Service("RecallDrugService")
public class RecallDrugService implements IRecallDrugService  {
	
	private Integer screenId = Screen.RecallDrug.getScreen();

	@Autowired
	IRecallDrugDao RecallDrugDao;

	@Override
	@Transactional
	public Object getRecallDrugList(RecallDrugRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		return RecallDrugDao.searchRecallDrugList(request, screenId);
	}
}
