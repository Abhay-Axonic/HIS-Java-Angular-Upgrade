/**
 * Santosh
 * 04-06-2018
 */

angular.module("myApp").controller("AddNewAllInclusivePackageController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','GenericService',
  function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,GenericService){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if(cookieObjectAppConfig == undefined){
			$state.go('login');
			return;
		}
		
		
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$scope.userId = cookieObject.userId;
		
		$scope.init = function(){
			$scope.searchServiceList = [];
			$scope.selectedServiceList = [];
			$scope.initPackageObj();
			$scope.serviceWiseSummary = 0;
			$scope.grandTotal = 0;
			$scope.packageService = "";
			$scope.itemObj = {};
			$scope.searchServObj = {};
			servicesFactory.initServiceSearch($scope);
			itemFactory.initItemFactory($scope);
			$scope.itemWiseExclusionList = [];
		}
		
		$scope.initPackageObj = function(){
			$scope.packageObj = {packageTypeId:"4",sexId:"",minAge:"",maxAge:"",tariffId:$scope.defaultSelfTariffId,paymentEntitlementTypeId:"",patientTypeId:"",serviceId:"",markupDownInPercentage:"",isMarkupDown:"1",roundOffPositNeg:"1"}
		}
		
		$scope.init();
		
		//service factory methods 
		$scope.searchServices = function(searchObj){servicesFactory.searchServices(searchObj,$scope)}
		$scope.getSubSpecialityBySpecialityId = function(specialityId){servicesFactory.getSubSpecialityBySpecialityId(specialityId,$scope)}
		$scope.selectAllServices = function(flag){servicesFactory.selectAllServices(flag,$scope)}
		$scope.setOrgUnit = function(orgId,unitId,scope){servicesFactory.setOrgUnit(orgId,unitId,$scope)}
		
		//item factory methods
		$scope.getProductCatByAssestCat = function(assetCategoryId,scope){itemFactory.getProductCatByAssestCat(assetCategoryId,$scope)}
		$scope.itemSearch = function(itemObj,scope){itemFactory.itemSearch(itemObj,$scope)}
		$scope.selectAllItems = function(flag,scope){itemFactory.selectAllItems(flag,$scope)}
		
		$scope.getPackageTypeList = function(orgId,unitId) {
				var data = "";
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.packageTypeList = response.data.listObject;
				});
		}
		
		$scope.getTariffList = function(orgId,unitId) {
				/*var data = "";
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getTariff/"+orgId+"/"+unitId;
				BillingGenericService.serviceAction("GET", URI, data).then(function(response){
					$scope.tariffList= response.data.listObject;
				});*/
			$scope.tariffList = 1;
		}
		
		$scope.getPaymentEntitlementList = function() {
				var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
					$scope.paymentEntitlementList= response.data.listObject;
				});
		}
		
		$scope.getPatientTypeList = function() {
				var URI = BASE_URL + ROOT_URL + "/global/patientcategory/"+$scope.orgId;
				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
					$scope.patientTypeList= response.data.listObject;
				});
		}
		
		 $scope.getBedBillingCategoryList = function() {
			    var URI = BASE_URL + ROOT_URL + "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId;
			    BillingGenericService.serviceAction("GET", URI, null).then(function(response){
			     $scope.bedBillingCategoryList= response.data.listObject;
			    });
			  }

		//$scope.getBedBillingCategoryList();
		$scope.getPackageTypeList($scope.orgId,$scope.unitId);
		$scope.getTariffList($scope.orgId,$scope.unitId);
		$scope.getPatientTypeList();
		$scope.getPaymentEntitlementList();
	    
		/** Service Autocomplete List * */
		$scope.getPackageServiceList = function(searchKeyword)
		{
				var data = "";
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/autoCompleteServices/"+$scope.orgId+"/"+$scope.unitId+"/"+searchKeyword;
				return BillingGenericService.serviceAction("GET", URI,data).then(function(response) {
					return response.data.listObject;
				});
		}	
	    
		$scope.selectPackageService = function($item, $model,$label)
		{
	      $scope.packageObj.serviceId = $item.id;
        }
		
		$scope.addServices = function(){
			//$scope.selectedServiceList = [];
			angular.forEach($scope.searchServiceList,function(value,key){
				if(value.isSelected){
					value.qty = 0;
					value.total = 0;
					$scope.selectedServiceList.push(value);
				}
			});
		}
		
		$scope.removeSelectedService = function(index){
			$scope.selectedServiceList.splice(index,1);
		
		}
		
		$scope.addItems = function(){
			angular.forEach($scope.searchItemList,function(value,key){
				if(value.isSelected){
					value.qty = 1;
					$scope.itemWiseDetailsList.push(value);
				}
			});
		}
		
		$scope.removeItem = function(index){
			$scope.itemWiseDetailsList.splice(index,1);
		}
		
	}catch(e){
		console.log(e.message);
	}
	
	$scope.savePackage = function(){
		
		$scope.$broadcast('show-errors-check-validity');
		if ($scope.allInclusivePkg.$valid)
		{
		var listTPackageIncExcServiceDetailsDto = [];
		angular.forEach($scope.selectedServiceList,function(value,index){
			var temp = {
					serviceId : value.serviceMasterId,
					isInclusiveService : 2,
					isServiceItem : 1,
					orgId:1,
					orgUnitId:1,
					createdBy : $scope.userId,
					updatedBy : $scope.userId
				}
			listTPackageIncExcServiceDetailsDto.push(temp);
		});
		
		angular.forEach($scope.itemWiseExclusionList,function(value,index){
			var temp = {
					itemId : value.itemId,
					isServiceItem : 2,
					isInclusiveService : 2,
					orgId:1,
					orgUnitId:1,
					createdBy : $scope.userId,
					updatedBy : $scope.userId
				}
			listTPackageIncExcServiceDetailsDto.push(temp);
		});
		
		$scope.packageObj.orgId = $scope.orgId;
		$scope.packageObj.unitId = $scope.unitId;
		$scope.packageObj.createdBy = $scope.userId;
		$scope.packageObj.updatedBy = $scope.userId;
		$scope.packageObj.listTPackageIncExcServiceDetailsDto = listTPackageIncExcServiceDetailsDto;
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/allInclusive";
		BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
			if (response.data.status == "success")
				$scope.init();
			$scope.$broadcast('show-errors-reset');
			$rootScope.stopLoader();
		});
		}
	}
	
	/*First Level*/
	$scope.firstTabLevel = 1 ;
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
	};
	
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	
	/*Second Level*/
	$scope.secondTabLevel = 1 ;
	
	$scope.setSecondTabLevel = function(newTab)
	{
		$scope.secondTabLevel = newTab;
	};
	
	$scope.isSetSecondTabLevel = function(tabNum)
	{
		return $scope.secondTabLevel === tabNum;
	};
	
	$scope.addItemWiseExclusion = function(){
		$scope.itemWiseExclusionList = [];
		angular.forEach($scope.searchItemList,function(value,index){
			if(value.isSelected){
				$scope.itemWiseExclusionList.push(value);
			}
		});
	}
	
	$scope.removeItemWiseExclusion = function(index){
		$scope.itemWiseExclusionList.splice(index,1);
	}
	
	$scope.getBillingBedCategory = function(visitId)
	{
			var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitId+"/"+$scope.orgId+"/"+$scope.unitId;
		GenericService.serviceAction("GET", URI, "")
				.then(function(response) {
					    $scope.bedBillingCategoryList = [];
							if (response.data.status == "success") {
								
								$scope.bedBillingCategoryList = response.data.listObject;
								/*angular.forEach(response.data.listObject,
												function(value,index) {
													var data = {};
													data.id = value.billingBedCategoryId;
													data.label = value.billingBedDesc;
													$scope.billingCategoryAdd.push(data);
												});*/
								//$scope.activeBillingBedCategory();
								//console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
							}
						});
		
		}

		if($stateParams.newAllExclusiveObj!=undefined && $stateParams.newAllExclusiveObj!=null && $stateParams.newAllExclusiveObj.parentPackageId>0){
            		          $scope.packDetailsObj = {};
                              $scope.packDetailsObj.parentPackageId = $stateParams.newAllExclusiveObj.parentPackageId;
                              $scope.parentPackageId = $stateParams.newAllExclusiveObj.parentPackageId;
                              $scope.packDetailsObj.packageTypeId = 1;
                              //$scope.getSelectedPackageServices();
                			}
	
}]);