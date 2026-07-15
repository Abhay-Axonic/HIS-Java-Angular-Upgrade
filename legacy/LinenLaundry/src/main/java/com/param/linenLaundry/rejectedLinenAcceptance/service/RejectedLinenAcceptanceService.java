package com.param.linenLaundry.rejectedLinenAcceptance.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Store;

import com.param.global.dao.global.IGlobalDao;
import com.param.linenLaundry.rejectedLinenAcceptance.dao.IRejectedLinenAcceptanceDao;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.LinensDropdownResponse;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectedLinenRequest;

@Service("RejectedLinenAcceptanceService")
public class RejectedLinenAcceptanceService implements IRejectedLinenAcceptanceService {
	
	private Integer screenId = Screen.LinenRejectAcceptance.getScreen();
	
	private com.param.entity.model.master.Screen screen;
	
	@Autowired
	IRejectedLinenAcceptanceDao RejectedLinenAcceptanceDao;
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Transactional
	@Override
	public LinensDropdownResponse getDropdowns() {
		// TODO Auto-generated method stub
		LinensDropdownResponse response = new LinensDropdownResponse();
		for (Store store : GlobalDao.getUserStoreList(null,null,null)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(null)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
//					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	public Object getRejectLinenAcceptanceList(RejectedLinenRequest request) {
		// TODO Auto-generated method stub
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		return RejectedLinenAcceptanceDao.searchRejectLinen(request, screenId);
	}
/*
	@Override
	@Transactional
	public Object getRejectMaterialAcceptanceDetails(Integer id) {

		Reject reject = RejectedMaterialAcceptanceDao.getReject(id);

		RejectLinenResponse response = null;

		if (reject.getMaterialIssue() != null) {
			response = new RejectLinenResponse(reject.getId(), reject.getRejectedDate(), reject.getReturnNo(),
					reject.getFromStore().getStoreId(), reject.getFromStore().getStoreName(),
					reject.getToStore().getStoreId(), reject.getToStore().getStoreName(),
					reject.getRejectedBy().getUserName(),
					(reject.getMaterialIssue() != null
							? reject.getLinenIssue().getStoreIndent()
							: null),
					(reject.getLinenIssue() != null ? reject.getRemark(): null),
					reject.getStatus().getStatus(), reject.getStatus().getId(), reject.getLinenIssue().getId());
		}

		if (reject.getmReturn() != null) {
			response = new RejectLinenResponse(reject.getId(), reject.getRejectedDate(), reject.getReturnNo(),
					reject.getFromStore().getStoreId(), reject.getFromStore().getStoreName(),
					reject.getToStore().getStoreId(), reject.getToStore().getStoreName(),
					reject.getRejectedBy().getUserName(),
					(reject.getLinenIssue() != null
							? reject.getLinenIssue().getStoreIndent()
							: null),
					reject.getStatus().getId(), 
					reject.getmReturn() != null ? reject.getmReturn().getId() : null,
					(reject.getmReturn()!= null? reject.getRemark(): null),
					reject.getStatus().getStatus());
		}

	}*/
}
