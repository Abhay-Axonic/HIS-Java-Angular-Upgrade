package com.param.global.service.kitItem;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.entity.model.master.Item;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.kit.items.IKitItemDao;
import com.param.global.dto.kit.Items.KitItemsDetailsListResponse;
import com.param.global.dto.kit.Items.KitItemsRequest;

@Service
public class KitItemService implements IKitItemService{
		
	@Autowired
	IKitItemDao kitItemDao;

	@Autowired
	IGlobalDao GlobalDao;
		@Override
		@Transactional
		public List<KitItemsDetailsListResponse> kitItemsDetails(KitItemsRequest kitItemsRequest) {

			List<Item> count = kitItemDao.productCategoryCount(kitItemsRequest.getItemIds());
			List<KitItemsDetailsListResponse> response = null;
			if (count.isEmpty())
			{
				response = kitItemDao.kitItemsDetails(kitItemsRequest.getItemIds(),kitItemsRequest.getStoreId());
			} 
			else if ((!count.isEmpty())) 
			{
				response = kitItemDao.kitItemsDetailsFixedAsset(kitItemsRequest.getItemIds(),kitItemsRequest.getStoreId());
			}
			for (KitItemsDetailsListResponse item : response) {

				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(kitItemsRequest.getStoreId(), item.getItemId());
				Long lastMonthSale = GlobalDao.getLastMonthSale(kitItemsRequest.getStoreId(), item.getItemId(),kitItemsRequest.getUnitId());
				Long currentMonthSale = GlobalDao.getCurrentMonthSale(kitItemsRequest.getStoreId(), item.getItemId(),kitItemsRequest.getUnitId());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(kitItemsRequest.getStoreId(), item.getItemId(),kitItemsRequest.getOrganizationId());
				Long maxOrderQuantity = maxStoreQuantity - (item.getStockAvailable() + stockInTransit);

				if (kitItemsRequest.getScreenId() != null
						&& (kitItemsRequest.getScreenId().equals(Screen.PurchaseRequest.getScreen())
								|| kitItemsRequest.getScreenId().equals(Screen.LocalPurchaseRequest.getScreen()))) {
					maxOrderQuantity = maxStoreQuantity - (item.getStockAvailable() + stockInTransit
							+ kitItemDao.getLeaseQunatity(kitItemsRequest.getStoreId(), item.getItemId(),kitItemsRequest.getUnitId()));
				}

				item.setStockInTransit(stockInTransit);
				item.setLastMonthSale(lastMonthSale);
				item.setCurrentMonthSale(currentMonthSale);
				item.setMaxOrderQuantity(maxOrderQuantity);
				item.setMaxStoreQuantity(maxStoreQuantity);
				item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(),kitItemsRequest.getOrganizationId()));
			}
			return response;
		}
}
