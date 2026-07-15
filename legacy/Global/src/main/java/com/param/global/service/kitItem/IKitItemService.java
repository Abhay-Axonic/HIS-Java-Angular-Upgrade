package com.param.global.service.kitItem;

import java.util.List;

import com.param.global.dto.kit.Items.KitItemsDetailsListResponse;
import com.param.global.dto.kit.Items.KitItemsRequest;

public interface IKitItemService {
	
	List<KitItemsDetailsListResponse> kitItemsDetails(KitItemsRequest kitItemsRequest);


}
