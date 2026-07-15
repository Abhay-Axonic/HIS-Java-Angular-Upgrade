package com.param.linenLaundry.sendToCondemnate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Store;
import com.param.global.dao.global.IGlobalDao;
import com.param.linenLaundry.sendToCondemnate.dto.SendCondemnateDropdownResponse;

@Service("SendCondemnateService")
public class SendCondemnateService implements ISendCondemnateService {

	private Integer screenId /*= Screen.SendCondemnate.getScreen()*/;

	@Autowired
	IGlobalDao GlobalDao;
	@Override
	public SendCondemnateDropdownResponse getDropdowns() {
	SendCondemnateDropdownResponse response = new SendCondemnateDropdownResponse();

	for (Store store : GlobalDao.getUserStoreList(null,null,null)) {
		response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
	}

	for (Store store : GlobalDao.getStoreList(null)) {
		response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
				store.getStoreType().getCode().equals("MS")));
	}

	for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
		response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
				screenStatus.getStatus().getStatus()));
	}

	for (IndentType indentType : GlobalDao.getIndentTypeList(null,null)) {
		response.addIndentType(
				new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
	}

	for (Priority priority : GlobalDao.getPriorityList()) {
		response.addPriority(
				new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
	}

	return response;
}
}
