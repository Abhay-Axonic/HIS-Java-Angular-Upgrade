angular
		.module('myApp')
		.controller(
				'pmRequestController',
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
								var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor pilot: null-safe parse + popup trigger, one owner

								
								$rootScope.loginpage = true;
								$scope.LISDynamicLabel = "Machine ";
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.AssetDetailsByCriteria = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								
								$scope.storeId = cookieObjectStore != null ? cookieObjectStore.storeId : undefined; // store-accessor pilot: null-safe (the accessor above owns the parse + popup trigger)
							  	$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.scheduledDate = new Date();
								$scope.scheduledTime = "";
								$scope.scheduledRemark = ""; 
								$scope.completionDate = new Date();
								$scope.completionTime = "";
								$scope.completionRemark = "";
								$scope.pmRequestObj = {
										"month":"",
										"year":"",
										"storeId":0,
										"itemName":"",
										"processStatus":"",
										"unitId":$scope.unitId,
										"organizationId":$scope.organizationId
								}
								var currentMonth = new Date().getMonth() + 1;
								var currentYear1 = new Date().getFullYear();
								$scope.pmRequestObj.month = currentMonth.toString();
								$scope.pmRequestObj.year = currentYear1.toString();
								$scope.dateOptionsT = {
									    'year-format': "'yyyy'",
									    'starting-day': 1};
								
										$scope.openDatePickerT = function() {
											$scope.datepickerT.opened = true;
										};
										$scope.datepickerT = {
												opened : false
											};
										
								 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
								 //http://localhost:8081/HIS-WAR/api/global/getDetailedStoreList/73/1/1
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $scope.storeList = response.data.listObject;
						                    $rootScope.stopLoader();
											//console.log("$scope.storeList",$scope.storeList);
						                });	
						            
						            
						         // Start get 50 Previous Year with Current Year
						            
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
						            
						         // and month

						         var year = new Date().getFullYear();
						         var range = [];

						         range.push(year);

						         for (var i = 1; i < 50; i++) {
						         range.push(year - i);
						         }

						         $scope.years = range;

						         $scope.months = [];
						         $scope.months.push({
						         value : 01,
						         text : 'January'
						         });
						         $scope.months.push({
						         value : 02,
						         text : 'February'
						         });
						         $scope.months.push({
						         value : 03,
						         text : 'March'
						         });
						         $scope.months.push({
						         value : 04,
						         text : 'April'
						         });
						         $scope.months.push({
						         value : 05,
						         text : 'May'
						         });
						         $scope.months.push({
						         value : 06,
						         text : 'June'
						         });
						         $scope.months.push({
						         value : 07,
						         text : 'July'
						         });
						         $scope.months.push({
						         value : 08,
						         text : 'August'
						         });
						         $scope.months.push({
						         value : 09,
						         text : 'September'
						         });
						         $scope.months.push({
						         value : 10,
						         text : 'October'
						         });
						         $scope.months.push({
						         value : 11,
						         text : 'November'
						         });
						         $scope.months.push({
						         value : 12,
						         text : 'December'
						         });
								
							}
							$scope.init();
							
							$scope.getAssetDetailsByCriteria = function () {
								if(cookieObjectStore == null){ return; } // store-accessor extension: no store-scoped call before the store pick (QF-AS)
								$scope.AssetDetailsByCriteria.length = 0;
								var month = $scope.pmRequestObj.month;
								var year = $scope.pmRequestObj.year;
								var store = $scope.pmRequestObj.storeId;
								var itemName= $scope.pmRequestObj.itemName;
								var proStatus= $scope.pmRequestObj.processStatus;
								
					            var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getAssetDetailsByCriteria";
					            $rootScope.startLoader();
					            GenericService.serviceAction("POST", URI, $scope.pmRequestObj).then(
					                function (response) {
					                	
					                	
					                	$scope.pagination.data = [];
					                	for(let i=0;i<response.data.listObject.length;i++){
					                		var newDate = moment(response.data.listObject[i].nextDate).format('DD-MM-YYYY');
					                		var yr = (newDate.split('-')[2]);
					                		var mnth = (newDate.split('-')[1]);
					                		var assetName =  response.data.listObject[i].itemName;
					                		var storeId = response.data.listObject[i].storeId;
					                		var processStatus = response.data.listObject[i].processStatus;
					                		if(month === mnth || year === yr || store === storeId || itemName === assetName || proStatus === processStatus){
					                			$scope.AssetDetailsByCriteria.push(response.data.listObject[i]);
					                		}
					                	}
					                	
					                	 if($scope.AssetDetailsByCriteria.length > 0){            
					                            PaginationService.searchData($scope.AssetDetailsByCriteria , $scope.pagination);
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
								if(cookieObjectStore == null){ return; } // store-accessor extension: no store-scoped call before the store pick (QF-AS)
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
								        		/*console.log("-----"+id+"-----",response.data[i]);*/
								        		$scope.itemDataList.push(response.data[i]);										        		
								        	}
								          }
								});
							   }
							
							$scope.getVendorMasterList = function(id) {
							      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API + $scope.unitId + '/'+$scope.organizationId;
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
							    
							    $scope.setScheduleData = function(assetScheduleId){
							    	$scope.assetScheduleId = assetScheduleId;
							    	var obj = {
											assetScheduleId:assetScheduleId,
											unitId:$scope.unitId ,
									        organizationId:$scope.organizationId
									        };
							    	var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchScheduledCompletionAssignedData";
									GenericService.serviceAction("POST", URI, obj).then(
										function(response) {
											console.log(response.data.listObject);
											$rootScope.stopLoader();
											$scope.scheduledDate = response.data.listObject[0].scheduledDate!=null ? new Date(response.data.listObject[0].scheduledDate) : "";
											$scope.scheduledTime = response.data.listObject[0].scheduledTime!=null ? new Date(response.data.listObject[0].scheduledTime) : "";
											$scope.scheduledRemark = response.data.listObject[0].scheduledRemark;
											
											$scope.completionDate = response.data.listObject[0].completionDate!=null ? new Date(response.data.listObject[0].completionDate) : "";
											$scope.completionTime = response.data.listObject[0].completionTime !=null ? new Date(response.data.listObject[0].completionTime) : "";
											$scope.completionRemark = response.data.listObject[0].completionRemark;
										});
							    	
							    	
							    }
							    $scope.saveChangedSchedule = function(){
							    	if($scope.scheduledDate=="" || $scope.scheduledTime=="")
							    		{
							    		growl.error("Please Select Scheduled Date and Time");
							    		return false;
							    		}
							    	var obj = {
											assetScheduleId: $scope.assetScheduleId,
											scheduledDate:  $scope.scheduledDate,
											scheduledTime: moment($scope.scheduledTime).format('hh:mm:ss a'),
											scheduledRemark: $scope.scheduledRemark
									        };
							    	$rootScope.startLoader();
							    	console.log('------------'+$scope.assetScheduleId,$scope.scheduledDate,$scope.scheduledTime,$scope.scheduledRemark);
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveChangedSchedule";
									GenericService.serviceAction("POST", URI, obj).then(
											function(response) {
												$rootScope.stopLoader();
												if (response.data.status == "success") {
													growl.success('Asset Schedule Saved Successfully...!!',
																	{title : 'Success!'});
												} else {
													growl.error('Something went wrong...!!',
																	{title : 'Error!'});
												}
											});
									
								
							    }
							    
							    $scope.saveCompletionDetails = function(){
							    	if($scope.completionDate=="" || $scope.completionTime=="")
						    		{
						    		growl.error("Please Select Completion Date and Time");
						    		return false;
						    		}
							    	var obj = {
											assetScheduleId: $scope.assetScheduleId,
											completionDate: $scope.completionDate,
											completionTime: $scope.completionTime,
											completionRemark: $scope.completionRemark
									        };
							    	$rootScope.startLoader();
							    	console.log('------------'+$scope.assetScheduleId,$scope.completionDate,$scope.completionTime,$scope.completionRemark);
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCompletionDetails";
									GenericService.serviceAction("POST", URI, obj).then(
											function(response) {
												$rootScope.stopLoader();
												if (response.data.status == "success") {
													growl.success('Completeion Details Saved Successfully...!!',
																	{title : 'Success!'});
												} else {
													growl.error('Something went wrong...!!',
																	{title : 'Error!'});
												}
											});
									
								
							    }

							    $scope.initYearList = function() {
                                    var currentYear = new Date().getFullYear();
                                    var startYear = currentYear - 5;
                                    var endYear = currentYear + 5;
                                    $scope.yearsList = [];

                                    for (let year = startYear; year <= endYear; year++) {
                                        $scope.yearsList.push(year);
                                    }
                                };

                                // Call the function to initialize the year list
                                $scope.initYearList();

								
}]);
