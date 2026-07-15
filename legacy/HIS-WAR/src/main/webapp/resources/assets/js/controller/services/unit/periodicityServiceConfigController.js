(function(){
	"use strict";
	
	function periodicityConfigController($scope, $rootScope, BillingGenericService, growl, $cookies,GenericService, $state,$http,PaginationService){
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			self.model = {
					pagination: {
	                    currentPage: 1,
	                    totalPages: 0,
	                    pageSize: '10',
	                    search: '',
	                    searchData: PaginationService.searchData,
	                    setTotalPages: PaginationService.setTotalPages,
	                    getPagesList: PaginationService.getPagesList,
	                    setPage: PaginationService.setPage,
	                    setFirstPage: PaginationService.setFirstPage,
	                    setPreviousPage: PaginationService.setPreviousPage,
	                    setNextPage: PaginationService.setNextPage,
	                    setLastPage: PaginationService.setLastPage,
	                    sortBy: PaginationService.sortBy
	                  }

			};
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			self.model.unitId = cookieObject.unitId;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.searchPServices = searchPServices;
			self.model.selectPService = selectPService;
			self.model.periodicity = {serviceId:"",measurementUnitId:"",duration:"",unitId:self.model.unitId,organisationId:self.model.organizationId,department:"",subDepartment:"",createdBy:self.model.userId,periodicityServiceConfigId:null,visitTypeId:'',assetCategoryId:'',productCategoryId:'',equipmentId:null};
			self.model.savePeriodicityConfig = savePeriodicityConfig;
			self.model.getPeriodicityList = getPeriodicityList;
			self.model.updatePeriodicityStatus = updatePeriodicityStatus;
			self.model.selectPerConfig = selectPerConfig;
			self.model.updatePerConfig = updatePerConfig;
			self.model.clearObject = clearObject;
			units();
			getPeriodicityList();
			$scope.productCategoryEquipmentList = [];
			$scope.serviceBaseAmt = 0;
			$scope.finalAmt = 0;
			
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+cookieObject.unitId+"/"+cookieObject.organizationId;
			GenericService.serviceAction(METHOD_GET, URI, null)
			.then(function(response){
				$rootScope.stopLoader();
				if(response.status == 200)
					
					$scope.assetCategoryListEquipment = response.data;
			});
			
			/*var URI = BASE_URL + ROOT_URL
			+ '	/api/opd/equipmentsChargable/'+self.model.unitId;

			BillingGenericService
					.serviceAction(METHOD_GET, URI,
							{},
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.equpmentList = response.data.listObject;
							});*/
	
		}
		
		$scope.getItemList = function(prodCatId){
			var assetsCatIds = [];
			var prodCatIds = [];
			prodCatIds.push(prodCatId);
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL
		+ '	/api/opd/equipmentsChargable/'+self.model.unitId+'/'+prodCatId;

		BillingGenericService
				.serviceAction(METHOD_GET, URI,
						{},
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.equpmentList = response.data.listObject;
						});
		}
		
		
		var searchPServices = function(serviceName)
		{
			var data={
					"visitTypeId" : 0,
					"unitIdId" : self.model.unitId,
					"serviceName" : serviceName
			};
			var URI = BASE_URL + ROOT_URL + "/api/global/search/services/periodicity";
			return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					self.model.pServices = response.data.listObject;
				}
				return self.model.pServices;
			});
		}
		
		$scope.getClearData = function(){
			self.model.periodicity.measurementUnitId = '';
			self.model.periodicity.duration = '';
			$scope.finalAmt = '';
		}
		
		var selectPService = function(item, model, label){
			self.model.periodicity.serviceId = item.serviceId;
			self.model.periodicity.department = item.department;
			self.model.periodicity.subDepartment = item.subDepartment;
			$scope.serviceBaseAmt = item.basePrice;
			
			$scope.getClearData();
		}
		
		var units = function(){
			var URI = BASE_URL + ROOT_URL + "/LIS/common/getTrunAroundTimeList/"+self.model.organizationId;
			return BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					 self.model.units = response.data.listObject;
				}
			});
			
			
			
		}
		
		// function for popup ok button call
		$scope.PopupOkBtn = function() {
			$scope.popUpFlag = false;
			savePeriodicityConfig();
		}
		
		$scope.getProductCatByAssestCatForEquipment = function(assetCategoryId)
		{
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+assetCategoryId + "/" + self.model.organizationId;
			GenericService.serviceAction(METHOD_GET, URI, null)
			.then(function(response){
				$rootScope.stopLoader();
				if(response.status == 200)
					$scope.productCategoryEquipmentList = response.data;
				else
					$scope.productCategoryEquipmentList = [];
				
				//scope.itemObj.productCategoryId = "";
			});
		}
		
		$scope.calculatePricePerUnit = function(item)
		{
			if(item.duration == 0 || item.duration == '' || item.duration == undefined){
				 $scope.finalAmt = '';
			}
			if(item.measurementUnitId > 0 && item.duration > 0)
				{
					/*1. Min
					2. Hours
					3. Day*/
				if(item.measurementUnitId == 1)
					{
						var perMin = parseFloat($scope.serviceBaseAmt)/parseFloat(item.duration);
						
					  $scope.finalAmt = parseFloat(perMin).toFixed(2)+"/min";
					}
				else if (item.measurementUnitId == 3)
					{
						var perMin = (24 * item.duration)*60;
					 // $scope.finalAmt = parseFloat($scope.serviceBaseAmt).toFixed(2)+"/unit";
						 var fin =  parseFloat($scope.serviceBaseAmt)/parseFloat(perMin);
						  $scope.finalAmt = parseFloat(fin).toFixed(2)+"/min";
					}
				
					
				}
		}
		
		$scope.calculatePricePerUnitDuration = function(item)
		{
			
			if(item.measurementUnitId > 0 && item.duration > 0)
				{
					/*1. Min
					2. Hours
					3. Day*/
				if(item.measurementUnitId == 1)
					{
						var perMin = parseFloat($scope.serviceBaseAmt)/parseFloat(item.duration);
						
					  $scope.finalAmt = parseFloat(perMin).toFixed(2)+"/min";
					}
				else if (item.measurementUnitId == 3)
					{
						var perMin = (24 * item.duration)*60;
					 // $scope.finalAmt = parseFloat($scope.serviceBaseAmt).toFixed(2)+"/unit";
						 var fin =  parseFloat($scope.serviceBaseAmt)/parseFloat(perMin);
						  $scope.finalAmt = parseFloat(fin).toFixed(2)+"/min";
					}
				
					
				}
		}
		
		
		
		var savePeriodicityConfig = function(){
			//delete department and sub deparment to avoid bad request
			$scope.$broadcast('show-errors-check-validity');
			self.model.periodicity.multidelete("department","subDepartment");
			self.model.periodicity.createdBy = self.model.userId;
			if ($scope.periodicityConfigForm.$valid) 
			{
				
				if($scope.configureEquipment=='Y')
					{
						if(self.model.periodicity.assetCategoryId==undefined || self.model.periodicity.assetCategoryId==null)
							{
								growl.warning("Please select asset category...!!",{
									title : 'Warning!'
								});
								return false;
							}else if(self.model.periodicity.productCategoryId==undefined || self.model.periodicity.productCategoryId==null){
								growl.warning("Please select product category...!!",{
									title : 'Warning!'
								});
								return false;
							}else if(self.model.periodicity.equipmentId==undefined || self.model.periodicity.equipmentId==null){
								growl.warning("Please select an equipment...!!",{
									title : 'Warning!'
								});
								return false;
							}
					}else{
						self.model.periodicity.assetCategoryId = '';
						self.model.periodicity.productCategoryId = '';
						self.model.periodicity.equipmentId = '';
					}
				if(self.model.periodicity.duration == 0){
					growl.warning("Duration should be greater than Zero(0)..!!",{
						title : 'Warning!'
					});
					return false;
				}
				
				if ($scope.popUpFlag) 
				{
					$scope.showModal = !$scope.showModal;

				} else {
					console.log(self.model.periodicity);
					$rootScope.startLoader();
					//return false;
					var URI = BASE_URL + ROOT_URL + "/api/periodicity/config";
					BillingGenericService.serviceAction(METHOD_POST,URI, self.model.periodicity, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
						if(response.data.status == "success"){
							init();
							getPeriodicityList();
							$rootScope.stopLoader();
						}else if(response.data.code==409)
						{
							growl.error(response.data.message,{
								title : ERROR_MSG
							});
							$rootScope.stopLoader();
						}
						
					});
				}
			}
			
			
		}
		
		var getPeriodicityList = function(){
			var URI = BASE_URL + ROOT_URL + "/api/periodicity/configs/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					
					 self.model.pagination.data = [];
					 self.model.periodicityConfigList = [];
					self.model.periodicityConfigList = response.data.listObject;
					 if(response.data.listObject.length > 0){            
                         PaginationService.searchData(self.model.periodicityConfigList, self.model.pagination);
                       }
                	 
				}
			});
		}
		var updatePeriodicityStatus = function(id,status){
			var URI = BASE_URL + ROOT_URL + "/api/periodicity/config/status/"+id+"/"+status;
			$rootScope.startLoader();
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success"){
					getPeriodicityList();
				}
				$rootScope.stopLoader();
			});
		}
		
		var selectPerConfig = function(config)
		{
			$scope.saveBtnFlag = false;
			$scope.updateBtnFlag = true;
			self.model.serviceSearchText = config.serviceName;
			if(config.assetCategoryId > 0){
			$scope.getProductCatByAssestCatForEquipment(config.assetCategoryId);
			}
			if(config.productCategoryId > 0){
			    $scope.getItemList(config.productCategoryId);
			}
			$scope.serviceBaseAmt = config.basePrice;
			self.model.periodicity = angular.copy(config);
			$scope.calculatePricePerUnit(self.model.periodicity);
			if(self.model.periodicity.equipmentId>0)
				$scope.configureEquipment='Y';
			else
				$scope.configureEquipment='N';
			//self.model.periodicity.measurementUnitId = config.measurementUnitId.toString();
		}
		
		var clearObject  = function()
		{
			self.model.periodicity = {serviceId:"",measurementUnitId:"",duration:"",unitId:self.model.unitId,organisationId:self.model.organizationId,department:"",subDepartment:"",createdBy:self.model.userId,periodicityServiceConfigId:null,visitTypeId:'',assetCategoryId:'',productCategoryId:''};
			$scope.finalAmt = 0;
		}
		
		var updatePerConfig = function()
		{
			
			$scope.$broadcast('show-errors-check-validity');
			
			if($scope.configureEquipment=='N')
			{
				self.model.periodicity.assetCategoryId = '';
				self.model.periodicity.productCategoryId = '';
				self.model.periodicity.equipmentId = '';
			}
			
			self.model.periodicity.multidelete("department","subDepartment");
			self.model.periodicity.createdBy = self.model.userId;
			if ($scope.periodicityConfigForm.$valid) {
				$rootScope.startLoader();
				
				var URI = BASE_URL + ROOT_URL + "/api/periodicity/config";
				BillingGenericService.serviceAction(METHOD_PUT,URI, self.model.periodicity, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						init();
						getPeriodicityList();
						growl.success("Updated Successfully...!!",{
							title : SUCCESS_MSG
						});
						$rootScope.stopLoader();
						
					}
					else if(response.data.code==409)
						{
							growl.error(response.data.message,{
								title : ERROR_MSG
							});
							$rootScope.stopLoader();
						}
					
				});
			}
			
		}
		
		init();
	}
	
	 function config($stateProvider) {
		    $stateProvider.state("periodicityServiceConfig", {
		    	url: '/periodicityServiceConfig',
				templateUrl: 'views/master/UnitMaster/billingMaster/periodicity-Service-Config.html',
				controller: "PeriodicityConfig.Controller",
				title:"Periodicity Service Configuration",
				controllerAs: "vm"
		    });
		  }

		  angular
		    .module("myApp")
		    .config(config)
		    .controller("PeriodicityConfig.Controller", periodicityConfigController);
})();

//function to delete multiple keys from object
if (!('multidelete' in Object.prototype)) {
    Object.defineProperty(Object.prototype, 'multidelete', {
        value: function () {
            for (var i = 0; i < arguments.length; i++) {
                delete this[arguments[i]];
            }
        }
    });
}