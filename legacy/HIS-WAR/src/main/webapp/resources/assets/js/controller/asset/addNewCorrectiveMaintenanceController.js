angular
		.module('myApp')
		.controller(
				'addNewCorrectiveMaintenanceController',
				[
						'$scope',
						'$rootScope',
						'$state',	
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'$stateParams',
						'$timeout',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,$timeout) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.storeId = 0;
							var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
							if(cookieObjectStore!=undefined){
								$scope.storeId = cookieObjectStore.storeId;
							}
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
			
							
							$scope.toDate =undefined;
							$scope.fromDate = undefined;
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
							
							 $scope.fromdateOptions ={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : moment(new Date()).subtract(2, 'days').toDate(),
										maxDate : new Date(),
							 };
								 
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};

							 $scope.todateOptions ={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date(),
							 };
							
							 
							// function for initializa clockpicker
								$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
								}
						
								$scope.getStoreList = function(){
									
									 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
							            $rootScope.startLoader();
							            GenericService.serviceAction("GET", URI, {}).then(
							                function (response) {
							                    $scope.storeList = response.data.listObject;
							                    $rootScope.stopLoader();
							                	console.log("$scope.store",$scope.store);
												console.log("$scope.storeList",$scope.storeList);
							                });
								}
								
								
								
							 $scope.getAssetCategoryList = function () {
						         
						            var URI =  BASE_URL+ROOT_URL+"/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId;

						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $scope.assetCategoriesList = response.data;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
						        }
							
							$scope.init = function(){
									$scope.corectiveMaintenanceObj = {
											"correctiveMaintenanceId":null,
											 "itemId":"",
											 "storeId":$scope.storeId,
											 "requestTypeId":"",
											 "requestCriticalityId":"",
											 "serialNumber":"",
											 "isDown":false,
											 "requestDateTime":$scope.dt,
											 "downDateTime":"",
											 "downTime":"",
											 "batchId":"",
											 "resolutionExpectedDateTime":"",
											 "resolutionExpectedTime":"",
											 "requestDetails":"",
											 "maintenanceStatusId":5,
											 "remark":"",
											 "createdBy":$scope.userId,
											"createdDate":$scope.dt,
											 "status":"A",
											 "updatedBy":$scope.userId,
											 "updatedDate":$scope.dt,
											 "unitId":$scope.unitId,
											 "organizationId":$scope.organizationId,
											 "assetName":"",
											 "assetCode":"",
											 "assetcategory": ""
									}
									
									$scope.serialNoObj = {
											batchId:"",
											serialNumber:""
									};
									
									$scope.assetCategoriesList  = [];
									$scope.assetCategoryObj = {};
									 $scope.productCategoriesList = [];
									 $scope.productCategoryObj = {};
									 $scope.getAssetCategoryList();
								     $scope.itemObj = {
												itemCode:"",
												itemName:""
										}
								     $scope.getStoreList();
								 	 $scope.storeList = [];
								     $scope.searchNewItemList = [];
								     $scope.serialNumberList = [];
							}
							
							$scope.init();
							
							
						     $scope. getProductCategoryList = function (id) {
						          
						            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id + "/"+$scope.organizationId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	$scope.productCategoriesList = response.data;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
						        }
							
						
								$scope.searchNewItem = function () {
									var data = {
											 	"assetTypeId": 2,
										        "assetCategoryId": $scope.assetCategoryObj.id,
										        "productCategoryId": $scope.productCategoryObj.id,
										        "storeId": $scope.corectiveMaintenanceObj.storeId
											};
									
									var URI = BASE_URL + ROOT_URL + "/api/global/items/unit/"+$scope.unitId;
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$scope.searchNewItemList = response.data;
										angular.forEach($scope.searchNewItemList,function(value, index){
											
											value.isSelected = false;
										})
										console.log("$scope.searchNewItemList",$scope.searchNewItemList);
									});
							}
							
								$scope.isSelected=false;
							$scope.selectItem = function(status){
								
								$scope.isSelected=status;
							}
							
							
							$scope.getSerialNumbers = function(itemId){
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getSerialNoByItemId/"+itemId+"/"+$scope.unitId+"/"+$scope.organizationId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	$scope.serialNumberList = response.data.listObject;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
								
								
							}
							
							$scope.addItems = function(){
								
								angular.forEach($scope.searchNewItemList,function(value, index){
									
									if(value.isSelected == true){
										
												$scope.corectiveMaintenanceObj.itemId = value.itemId;
												$scope.corectiveMaintenanceObj.assetName = value.itemName;
												$scope.corectiveMaintenanceObj.assetCode = value.itemCode;
												$scope.corectiveMaintenanceObj.assetcategory= value.assetCategory;
												$scope.getSerialNumbers(value.itemId);
												$scope.searchNewItemList = [];
												$scope.corectiveMaintenanceObj.serialNumber="";
									}
									
								})
								
							}
							
						$scope.comformCorectiveMaintenance = function(){
							
							$("#correctiveMainteanence").modal("show");
							
						} 
							
						$scope.getcriticality = function(){
							
							if($scope.corectiveMaintenanceObj.requestCriticalityId == 1){
								$scope.corectiveMaintenanceObj.isDown = false;
							}else if($scope.corectiveMaintenanceObj.requestCriticalityId == 2){
								$scope.corectiveMaintenanceObj.isDown = true;
							}
							
						} 
						
						$scope.clearList = function(){
							$scope.assetCategoryObj = {}; 
							$scope.productCategoryObj = {};
							$scope.itemObj = {
										itemCode:"",
										itemName:""
							}
							$scope.searchNewItemList = [];
						}
						
						$scope.getIsDownStatus = function(){
							
							if($scope.corectiveMaintenanceObj.isDown){
								$scope.corectiveMaintenanceObj.requestCriticalityId = 2;
							}else{
								$scope.corectiveMaintenanceObj.requestCriticalityId =1;
							}
							
						} 
						$scope.saveCorectiveMaintenance=function(){
							
						
							$scope.corectiveMaintenanceObj.resolutionExpectedDateTime = moment($scope.toDate).format('DD-MM-YYYY HH:mm:ss');
							$scope.corectiveMaintenanceObj.serialNumber = $scope.serialNoObj.serialNumber;
							$scope.corectiveMaintenanceObj.batchId =  $scope.serialNoObj.batchId;
							if($scope.corectiveMaintenanceObj.itemId == ""){
								
								growl.warning("Please Select Asset",
										{
											title : 'warning!'
										});
								
							}else if($scope.corectiveMaintenanceObj.requestTypeId == ""){
								
								growl.warning("Please select request Type",
										{
											title : 'warning!'
										});
								
							}else if($scope.corectiveMaintenanceObj.requestCriticalityId == ""){
								
								growl.warning("Please select request Criticality",
										{
											title : 'warning!'
										});
								
							}else if($scope.toDate == undefined || $scope.toDate == ""){
								
								growl.warning("Please select resolution Expected Date",
										{
											title : 'warning!'
										});
								
							}else  if($scope.corectiveMaintenanceObj.resolutionExpectedTime == undefined || $scope.corectiveMaintenanceObj.resolutionExpectedTime == ""){
									
									growl.warning("Please select resolution Expected Time",
											{
												title : 'warning!'
											});
									
							}else if($scope.corectiveMaintenanceObj.serialNumber == ""){
								
								growl.warning("Please select serial Number",
										{
											title : 'warning!'
										});
								
							}else if($scope.corectiveMaintenanceObj.requestDetails == ""){
								
								growl.warning("Please select request Details",
										{
											title : 'warning!'
										});
								
							}else{
							
							//var isEmpty = !(Object.values($scope.corectiveMaintenanceObj).every(x =>(x != null &&  x != '')));
								$scope.corectiveMaintenanceObj.downDateTime = moment($scope.fromDate).format('DD-MM-YYYY HH:mm:ss');
								if($scope.corectiveMaintenanceObj.isDown){
									
									if($scope.corectiveMaintenanceObj.downTime == ""){
										
										growl.warning("Please select  down time",
												{
													title : 'warning!'
												});
										return false;
									} else if($scope.fromDate == undefined){
										
										growl.warning("Please select  down date",
												{
													title : 'warning!'
												});
										return false;
									} else if($scope.corectiveMaintenanceObj.downDateTime == $scope.corectiveMaintenanceObj.resolutionExpectedDateTime){
										
										if($scope.corectiveMaintenanceObj.downTime >= $scope.corectiveMaintenanceObj.resolutionExpectedTime){
											
											growl.warning("Resolution Expected Time should be greater than down time",
													{
														title : 'warning!'
													});
											return false;
										}
									}
									
								}
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCorrectiveMaintenance";
								
									GenericService.serviceAction("POST",URI,$scope.corectiveMaintenanceObj).then(function(response){
										
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.init();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											
											 $timeout(function () { 
												 $state.go('correctiveMaintenance');
											 }, 3000);
											
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
									
									 }
							}
}]);
