angular
		.module('myApp')
		.controller(
				'pmScheduleController',
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
						'PaginationService',
						'CONSTANTS',
						'BillingGenericService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,PaginationService,CONSTANTS,BillingGenericService) {
							$scope.init = function(){
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
							/*	var cookieObjectStore = $cookies.getObject('selectedTopStore');
								if(cookieObjectStore==undefined){
									$rootScope.storePopup();
									angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
								}*/
								
								 var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
									if(cookieObjectAppConfig == undefined){
										$state.go('applicationConfiguration');
										return;
									}
									$scope.assetMaintenanceSpecialityId = cookieObjectAppConfig.assetMaintenanceSpecialityId;
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.AssetDetailsByScheduleCriteria = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								
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

								$scope.toDate= moment().toDate();
								$scope.fromDate= moment().toDate();
							   
								//$scope.storeId = cookieObjectStore.storeId;
							  	$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.scheduledDate = new Date();
								$scope.scheduledTime = "";
								$scope.scheduledRemark = ""; 
								$scope.completionDate = new Date();
								$scope.completionTime = "";
								$scope.completionRemark = "";
								$scope.pmScheduleObj = {
										"fromDate":new Date(),
										"toDate":new Date(),
										"storeId":0,
										"pmsType":"",
										"processStatus":"",
										"unitId":$scope.unitId,
										"organizationId":$scope.organizationId
								}
								
//								$scope.dateOptions = {
//									    'year-format': "'yyyy'",
//									    'starting-day': 1};
								
								  $scope.initClockPicker = function() {
									$('.clockpicker').clockpicker();
								}
							    
										$scope.openDatePickerF = function() {
											$scope.datepickerF.opened = true;
										};
										$scope.openDatePickerT = function() {
											$scope.datepickerT.toOpened = true;
										};
										$scope.openDatePickerS = function() {
											$scope.datepickerS.opened = true;
										};
										
										$scope.datepickerF = {
												opened : false
											};
										$scope.datepickerT = {
												toOpened : false
											};
										$scope.datepickerS = {
												opened : false
											};
										
										
										  $scope.pagination = {
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
										  
								 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
								 //http://localhost:8081/HIS-WAR/api/global/getDetailedStoreList/73/1/1
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $scope.storeList = response.data.listObject;
						                    $rootScope.stopLoader();
											//console.log("$scope.storeList",$scope.storeList);
						                });	
						            
						            $scope.usersList = [];

						    		try{
						    			$rootScope.startLoader();
						    			var data = {
						    				organizationId:$scope.organizationId,
						    				unitId:$scope.unitId,
						    				specialityId : $scope.assetMaintenanceSpecialityId 
						    			};
						    		var URI = BASE_URL + ROOT_URL+ "/api/global/getEmployeeListBySpecialityAndSubSpeciality";
						    		GenericService.serviceAction("POST", URI, data)
						    		.then(
						    			function(response) {
						    				$scope.usersList = [];
						    				if (response.data.status == "success") {
						    					$rootScope.stopLoader();
						    					$scope.usersList = response.data.listObject;
						    				}
						    			});
						    			}catch(e){
						    				$rootScope.stopLoader();
						    				console.log("Exception-",e);
						    			}
						    		
								
							}
							$scope.init();
							$scope.getAssetDetailsByCriteria = function () {
								$scope.AssetDetailsByScheduleCriteria.length = 0;
								var fDate = moment($scope.pmScheduleObj.fromDate).format('DD-MM-YYYY');
								var tDate = moment($scope.pmScheduleObj.toDate).format('DD-MM-YYYY');
								var store = $scope.pmScheduleObj.storeId;
								var pmstype= $scope.pmScheduleObj.pmsType;
								var proStatus= $scope.pmScheduleObj.processStatus;
								//console.log("^^^"+fDate+"^^^"+tDate+"^^^"+store+"^^^"+pmstype+"^^^"+proStatus);
					            var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getAssetDetailsByCriteria";
					            $rootScope.startLoader();
					            GenericService.serviceAction("POST", URI, $scope.pmScheduleObj).then(
					                function (responsse) {
					                	
					                	$scope.pagination.data = [];
					                	
					                	for(let i=0;i<responsse.data.listObject.length;i++){
					                		var newDate = moment(responsse.data.listObject[i].nextDate).format('DD-MM-YYYY');
					                		var pmsType =  responsse.data.listObject[i].pmsType;
					                		var storeId = responsse.data.listObject[i].storeId;
					                		var processStatus = responsse.data.listObject[i].processStatus;
					                		//console.log("^^^"+newDate+"^^^"+pmsType+"^^^"+storeId+"^^^"+processStatus);
					                		if(fDate >= newDate || tDate <= newDate || store === storeId || pmstype === pmsType || proStatus === processStatus){
					                			$scope.AssetDetailsByScheduleCriteria.push(responsse.data.listObject[i]);
					                		}
					                	}
					                	
					                	if($scope.AssetDetailsByScheduleCriteria.length > 0){            
				                            PaginationService.searchData($scope.AssetDetailsByScheduleCriteria , $scope.pagination);
				                          }
					                	
					                    $rootScope.stopLoader();
					                },
					                function (err) {
					                    $rootScope.stopLoader();
					                });
					        }
					        $scope.getAssetDetailsByCriteria();

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
								console.log(info,data);
								 $scope.itemDataList = [];
								 var URI = BASE_URL + ROOT_URL + "/api/global/items/unit/"+$scope.unitId;
								 GenericService.serviceAction("POST", URI, data).then(function(response) {
									 console.log(response.data);
									 for(let i=0; i<response.data.length;i++){
								        	if(response.data[i].itemId===id) {
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
							
							    $scope.getCheckListByAssetScheduleId= function(assetScheduleId){
									if(assetScheduleId != 0){
										var obj = {
												assetScheduleId:assetScheduleId,
												unitId:$scope.unitId ,
										        organizationId:$scope.organizationId
										        };
										console.log("object  ",obj);
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId";
										GenericService.serviceAction("POST", URI, obj).then(
											function(response) {
												$rootScope.stopLoader();
												$scope.addChecklist1 = [];
												$scope.addChecklist1 = response.data.listObject;
											});
									}
								}
							    
							    /*$scope.setScheduleData = function(assetScheduleId){
							    	$scope.assetScheduleId = assetScheduleId;
							    	var obj = {
											assetScheduleId:assetScheduleId,
											unitId:$scope.unitId ,
									        organizationId:$scope.organizationId
									        };
							    	var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchScheduledAndCompletionData";
									GenericService.serviceAction("POST", URI, obj).then(
										function(response) {
											//console.log(response.data.listObject);
											$rootScope.stopLoader();
											$scope.scheduledDate = new Date(response.data.listObject[0].scheduledDate);
											$scope.scheduledTime = response.data.listObject[0].scheduledTime;
											$scope.scheduledRemark = response.data.listObject[0].scheduledRemark;
											
											$scope.completionDate = new Date(response.data.listObject[0].completionDate);
											$scope.completionTime = response.data.listObject[0].completionTime;
											$scope.completionRemark = response.data.listObject[0].completionRemark;
										});
							    	
							    	
							    }*/
								$scope.setId = function(data){
									$scope.assetScheduleId = data.assetScheduleId;
									//console.log($scope.assetScheduleId);
									var obj = {
											assetScheduleId:$scope.assetScheduleId,
											unitId:$scope.unitId ,
									        organizationId:$scope.organizationId
									        };
							    	var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchScheduledCompletionAssignedData";
									GenericService.serviceAction("POST", URI, obj).then(
										function(response) {
											//console.log(response.data.listObject);
											$rootScope.stopLoader();
											$scope.scheduledDate = new Date(response.data.listObject[0].scheduledDate);
											$scope.scheduledTime = response.data.listObject[0].scheduledTime;
											$scope.scheduledRemark = response.data.listObject[0].scheduledRemark;
											
											$scope.completionDate = new Date(response.data.listObject[0].completionDate);
											$scope.completionTime = response.data.listObject[0].completionTime;
											$scope.completionRemark = response.data.listObject[0].completionRemark;
											
											$scope.assignTo = response.data.listObject[0].assignTo;
											$scope.assignOn = new Date();
											$scope.assignTime = moment(new Date()).format(' HH:mm'); ;
											$scope.assignRemark = response.data.listObject[0].assignRemark;
										});
								}
							    $scope.saveAssignDetails = function(){
							    	
							    	if($scope.assignTo == undefined || $scope.assignTo == null || $scope.assignTo == ""){
							    		growl.warning(" Please select assign to..",
												{
													title : 'Error!'
												});
									    
										return false;
							    	}
							    	
							    	
							    	if($scope.assignOn == undefined || $scope.assignOn == null || $scope.assignOn == ""){
							    		growl.warning(" Please select assign date..",
												{
													title : 'Error!'
												});
									    
										return false;
							    	}
							    	
							    	var obj = {
							    			assetScheduleId: $scope.assetScheduleId,
							    			assignTo: $scope.assignTo,
							    			assignOn: $scope.assignOn,
							    			assignTime: $scope.assignTime,
							    			assignRemark: $scope.assignRemark
									        };
							    	
							    	$rootScope.startLoader();
							    	//console.log('------------'+$scope.assetScheduleId,$scope.scheduledDate,$scope.scheduledTime,$scope.scheduledRemark);
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveAssignDetails";
									GenericService.serviceAction("POST", URI, obj).then(
											function(response) {
												$rootScope.stopLoader();
												if (response.data.status == "success") {
													growl.success('Schedule Assigned Successfully...!!',
																	{title : 'Success!'});
													$scope.getAssetDetailsByCriteria();
												} else {
													growl.error('Something went wrong...!!',
																	{title : 'Error!'});
												}
											});
									
								
							    }

							    $scope.getAssignNameById = function (id) {
								    var assignee = "";
								    if(id!=null && id!=0){
								    	angular.forEach($scope.usersList,function(value,key) {
								    if(value.userId == id)
								    	{assignee = value.name ;}
								    });
								    }return assignee;
							    }
							    
							    
								
}]);
