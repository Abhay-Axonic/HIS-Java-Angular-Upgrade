angular.module("myApp").factory('itemFactory',['$rootScope','$http','growl','$cookies','GenericService',function($rootScope,$http,growl,$cookies,GenericService){
	
	var factory = {};
	$http.defaults.headers.common.Authorization = 'Bearer '+$cookies.getObject("cookieObject").authToken;
	factory.setOrgUnit = function(orgId,unitId,scope){
		scope.orgId = orgId;
		scope.unitId = unitId;
	}
	
	factory.initItemFactory = function(scope){
		var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/1/"+$cookies.getObject("cookieObject").unitId+"/"+$cookies.getObject("cookieObject").organizationId;
		var httpObject = {method : "GET",url : URI,data : null};
		GenericService.serviceAction(METHOD_GET, URI, null)
		.then(function(response){
			if(response.status == 200)
				scope.assetCategoryList = response.data;
		});
	}
	
	factory.getProductCatByAssestCat = function(assetCategoryId,scope){
		if(assetCategoryId != "" && typeof(assetCategoryId) != "undefined"){

			var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+assetCategoryId+"/"+$cookies.getObject("cookieObject").organizationId;
			GenericService.serviceAction(METHOD_GET, URI, null)
			.then(function(response){
				if(response.status == 200)
					scope.productCategoryList = response.data;
				else
					scope.productCategoryList = [];
				
				scope.itemObj.productCategoryId = "";
			});
		}
	}
	
	factory.itemSearch = function(itemObj,scope)
	{
		itemObj.assetTypeId = 1;
		var URI = BASE_URL + ROOT_URL + "/api/global/items";//BASE_URL + ROOT_URL + "/api/global/items";
		GenericService.serviceAction(METHOD_POST, URI, itemObj)
		.then(function(response){
			if(response.status == 200)
				scope.searchItemList = response.data;
		});
		itemObj = {};
	}
	
	factory.selectAllItems = function(flag,scope){
		angular.forEach(scope.searchItemList,function(value,key){
			value.isSelected = flag;
		});
	}
	
	factory.itemSearchForPackage = function(itemObj,scope){
//		itemObj.assetTypeId = 1;
		var Obj={
			productCategoryList:itemObj.productCategoryId!="" && itemObj.productCategoryId!=undefined && itemObj.productCategoryId!=null ? [parseInt(itemObj.productCategoryId)]:[],
			assetCategoryList:itemObj.assetCategoryId!="" && itemObj.assetCategoryId!=undefined && itemObj.assetCategoryId!=null ? [parseInt(itemObj.assetCategoryId)] : [],
			unitId:scope.unitId,
			itemName:itemObj.itemName,
			itemCode:itemObj.itemCode,
//			assetTypeId:1
		}
		scope.startLoaderPopup();
		var URI = BASE_URL+ ROOT_URL+ "/unit/CompanyContract/getItemListByAssetAndProductCategory";
		GenericService.serviceAction(METHOD_POST, URI, Obj)
		.then(function(response){
			    if(response.status == 200)
			    {
				  scope.searchItemList = response.data.listObject;
			      searchObj = {};
			      scope.stopLoaderPopup();
				}
			else if(response.status == 500)
				{
				  scope.stopLoaderPopup();
				  searchObj = {};
				}
		});
		
		
		itemObj = {};
	}
	return factory;
}]);	