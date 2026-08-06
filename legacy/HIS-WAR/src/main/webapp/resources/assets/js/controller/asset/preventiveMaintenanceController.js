angular
		.module('myApp')
		.controller(
				'preventiveMaintenanceController',
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
						'CONSTANTS',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,CONSTANTS) {
							
							$scope.init = function(){
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
								if(cookieObjectStore==undefined){
									$rootScope.storePopup();
									angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
								}
								$scope.toDate= moment().toDate();
								$scope.fromDate= moment().toDate();
								$scope.dateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate:new Date(),
									minDate : moment(new Date()).subtract(180, 'days').toDate()
								};

							    
							    $scope.toDateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate:new Date()
								};
								$scope.storeId = (cookieObjectStore != null && cookieObjectStore != undefined) ? cookieObjectStore.storeId : undefined; // QF-AR: guard the deref — the lone-if above only shows the store popup; the deref was unconditional and crashed every load when the store cookie is absent
								
								$scope.schedulerObj ={
										"itemNameId":"",
										"serialNo":"",
										"assetCategory":"",
										"itemCode":"",
										"itemName":"",
										"serialNo":"",
										"vendorName":"",
										"pmsFrequency":"",
										"pmsType":"",
										"startDate":"",
										"endDate":"",
										"createdBy":$scope.userId,
										"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"status":"A",
										"updatedBy":$scope.userId,
										"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"unitId":$scope.unitId ,
										"organizationId":$scope.organizationId
								}
							
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
							
								$scope.statusId =0;
								$scope.assetCategoriesList  = [];
								$scope.assetCategoryObj = {};
								 $scope.productCategoriesList = [];
								 $scope.productCategoryObj = {};
							     $scope.itemObj = {
											itemCode:"",
											itemName:""
									}
							     
							     $scope.searchNewItemList = [];
							     $scope.serialNumberList = [];
								
								$scope.assetList = [];
								/*var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
										};								
								var URI = BASE_URL + ROOT_URL+ "/api/assetManagement/maintenance/getActiveAssestList";
								GenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.assetList = response.data.listObject;
													console.log("--------------------------", 	response.data.listObject);
											}
										});*/
								
								 var URI =  BASE_URL+ROOT_URL+"/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {	
						                	//console.log(response.data);
						                    $scope.assetCategoriesList = response.data;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							$scope.init();
							
							$scope.getProductCategoryList = function () {
								var id = $scope.assetSearchObj.assetCategory;
					            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id +"/"+$scope.organizationId;
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
							
							$scope.getActiveAssetsByCriteria = function () {
								$scope.assetList = [];
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										assetCategoryId: $scope.assetSearchObj.assetCategory,
									    productCategoryId: $scope.assetSearchObj.productCategory,
									    assetCode:$scope.assetSearchObj.itemCode,
									    assetName:$scope.assetSearchObj.itemName
										};	
								/*var data = {
										 	"assetTypeId": 2,
									        "assetCategoryId": $scope.assetSearchObj.assetCategory,
									        "productCategoryId": $scope.assetSearchObj.productCategory,
									        "storeId": $scope.storeId
										}*/;
								
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getActiveAssestList";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									//console.log("----"+$scope.assetSearchObj.assetCategory+"---"+$scope.assetSearchObj.productCategory+"---",response.data.listObject);
									for(let i=0; i<response.data.listObject.length;i++){
										
							        	if(response.data.listObject[i].assetCategoryId===$scope.assetSearchObj.assetCategory) {
							        		/*console.log("-----"+id+"-----",response.data[i]);*/
							        		$scope.assetList.push(response.data.listObject[i]);	
							        	}
							          }
									//console.log("$scope.assetList",$scope.assetList);
								});
							}
							
							
							$scope.setDataToSchedule = function(info){
									if(info!=null){
										$state.go('assetScheduler',{assetInfoObj: info});
								}
							}
							
							$scope.getManufactureDetailsById = function(id){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											unitId:$scope.unitId,
											organizationId:$scope.organizationId,
									};
									$rootScope.startLoader();
									 var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API +$scope.organizationId;
									 GenericService.serviceAction("GET", URI, {}).then(
											 function (response) {
												 $scope.manufactureDataList = [];
												 $rootScope.stopLoader();
										          for(let i=0; i<response.data.length;i++){
										        	if(response.data[i].id===id) {
										        		/*console.log("-----"+id+"-----",response.data[i]);*/
										        		$scope.manufactureDataList.push(response.data[i]);									        		
										        	}
										          }
										        });
								}catch(e){
									console.log("Exception",e);
								}
							}
							
							$scope.getAccessoryDetailsById = function(info) {
								var data = {
									 	"assetTypeId": 2,
								        "assetCategoryId": info.assetCategoryId,
								        "productCategoryId": info.productCategoryId,
								        "storeId": $scope.storeId
									};
								var id=info.itemNameId;
								//console.log(info,data);
								 $scope.itemDataList = [];
								 var URI = BASE_URL + ROOT_URL + "/api/global/items/unit/"+$scope.unitId;
								 GenericService.serviceAction("POST", URI, data).then(function(response) {
									 //console.log(response.data);
									 for(let i=0; i<response.data.length;i++){
								        	if(response.data[i].itemId===id) {
								        		/*console.log("-----"+id+"-----",response.data[i]);*/
								        		$scope.itemDataList.push(response.data[i]);										        		
								        	}
								          }
								});
							   }
							
							$scope.getVendorMasterList = function(id) {
							      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API+ $scope.unitId + '/'+$scope.organizationId;
							      $rootScope.startLoader();
							      GenericService.serviceAction("GET", URI, {}).then(
							    		  function (response) {
												 $scope.vendorDataList = [];
												 $rootScope.stopLoader();
										          for(let i=0; i<response.data.length;i++){
										        	if(response.data[i].id===id) {
										        		/*console.log("-----"+id+"-----",response.data[i]);*/
										        		$scope.vendorDataList.push(response.data[i]);									        		
										        	}
										          }
										        });
							    };
							
								
//							    $scope.getInsuranceList = function(id) {
//							    	$rootScope.startLoader();
//									var data = "";
//									var offset = offset != null ? offset : 0;
//									var noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 100;
//									var URI = BASE_URL + ROOT_URL+"/api/unit/billing/getInsuranceMasterList";
//									var data = {
//										orgId : $scope.organizationId,
//										unitId:$scope.unitId,
//										offset : offset,
//										noOfRecordsPerPage : noOfRecordsPerPage
//									}
//									// For Get designation List Using Offset
//									GenericService.serviceAction("POST", URI, data).then(
//													function(response) {
//														$scope.insuranceDataList = [];
//														 $rootScope.stopLoader();
//												          for(let i=0; i<response.data.length;i++){
//												        	if(response.data[i].id===id) {
//												        		/*console.log("-----"+id+"-----",response.data[i]);*/
//												        		$scope.insuranceDataList.push(response.data[i]);								        		
//												        	}
//												          }
//													});
//								}
							
							    $scope.getAssetDataToView = function(data){
							    	//$rootScope.startLoader();
							    	var data = "";
									var offset = offset != null ? offset : 0;
									var noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 100;
									var URI = BASE_URL + ROOT_URL+"/api/assetManagement/maintenance/getAssetDataToView";
									
									// For Get Asset List to view
									GenericService.serviceAction("POST", URI, data).then(
													function(response) {
														$scope.insuranceDataList = [];
														 //$rootScope.stopLoader();
														if(info!=null){
															//$state.go('assetScheduler',{assetInfoObj: info});
														}
													});
							    }

							    $scope.setDataToView = function(info){
									if(info!=null){
										//console.log("setDataToView () Data == ",info);
										$state.go('viewAssetScheduler',{assetDataObj: info});
									}
								}

								
}]);
