/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"departmentStoreMapperController",
				[	    	'$scope',
							'$http',
							'$localStorage',
							'$sessionStorage',
							'$cookies',
							'$rootScope',
							'GenericService',
							'PagerService',
							'promiseFactory',
							'BillingGenericService',
							'GenericServiceFileUpload',
							'reportService',
							'PaginationService',
							'growl',
							'$state',
							'$stateParams',
							
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, PaginationService,growl, $state,
								$stateParams,$modalInstance) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Machine ";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/** * Check user is Loggedin */
							var cookieObject, cookieObjectAppConfig;
			       [cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
			              	var data = {};
						$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.deptObj = {
									"deptStoreMapperId" : '',
									"storeId" : '',
									"deptId" : '',
									"deptName" :'',
									"storeName":'',
									"id" : '',
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"status":'A'
								}
							};
							$scope.initCommon();

							$scope.initDeptStoreMapped = function() {
								$scope.deptStoreMapped = {
										"deptStoreMapperId" : $scope.deptObj.deptStoreMapperId,
										"storeId" : $scope.deptObj.storeId,
										"deptId" : $scope.deptObj.deptId,
										"deptName" :$scope.deptObj.deptName,
										"storeName":$scope.deptObj.storeName,
										"id" : $scope.deptObj.id,
										"status" : "A",
										"createdBy" : $scope.userId,
										"createdDate" : "",
										"updatedBy" : $scope.userId,
										"updatedDate" : "",
										"orgId" : $scope.orgId,
										"unitId" : $scope.unitId
								}
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
								

						/*	
							 START FORFOR SEROLOGY MASTER 

							$scope.setNoOfRecords = function() {
								$scope.initDeptStoreMappedList($scope.orgId,
										$scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initDeptStoreMappedList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI1 = BASE_URL + ROOT_URL + API
								        + "/globals/listDeptStoreMapper"
								        + S + orgId + S
										+ $scope.unitId + S + offset + S
										+ noOfRecordsPerPage;
								var URI2 = BASE_URL + ROOT_URL + API
								        + "/globals/getTotalDeptStoreMapperRecord"
								        + S + orgId + S
										+ $scope.unitId;
								GenericService.serviceAction("GET", URI1).then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.getDeptStoremappedList = response.data.listObject;
									}
								});
								GenericService.serviceAction("GET", URI2).then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.getDeptStoremappedList = response.data.listObject;
										$scope.setPage(1,false);
										console.log("commonList",$scope.getDeptStoremappedList);
									}
								});
							}
							

							$scope.getDeptStoreMappedList = function(orgId,
									offset, noOfRecordsPerPage) {
								 var data={
										 orgId :$scope.orgId,
										 unitId :$scope.unitId
								 };
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL
							        	+ "/globals/getDeptStoremappedList"
								        + S + orgId + S
										+ $scope.unitId + S + offset + S
										+ noOfRecordsPerPage;
								 var URI = BASE_URL + ROOT_URL + "/api/globals/getDeptStoremappedList";
							      GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.status == 200) {
													$rootScope.stopLoader();
													$scope.getDeptStoremappedList = response.data.listObject;
													console.log("getDeptList",$scope.getDeptStoremappedList);
												}
											});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage,
										$scope.unitId);
								if (flag) {
									    $scope.masterList($scope.orgId,
										$scope.pager.startIndex,
										$scope.pager.pageSize,
										$scope.unitId);
								}
							}

							$scope.initDeptStoreMappedList($scope.orgId,
									$scope.offset, $scope.noOfRecordsPerPage);
							// END FOR SEROLOGY MASTER
*/							
							$scope.getStoreList = [];

							 $scope.storeList = function() {
								 var data={};
								 var URI = BASE_URL + ROOT_URL + "/api/global/user/stores"+"/"+$scope.organizationId  + '/'+$scope.unitId+"/"+$scope.userId;
							      GenericService.serviceAction("GET", URI, data)
									.then(
											function(response) {

												if (response.status == 200) {
													$rootScope.stopLoader();
													$scope.getStoreList = response.data;
													
													
													 /* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
													$scope.getMultipleStoreList = [];
													angular.forEach($scope.getStoreList,function(value,index) {
										                var data = {};
										                data.id = value.id;
										                data.label = value.store;
										                $scope.getMultipleStoreList.push(data);
										          });
													 /* end code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
													
												}
											});

							    
							    }
							 $scope.storeList();

						
							 $scope.getDeptList = [];

							 $scope.deptList = function() {
								 var data={
										 orgId :$scope.orgId,
										 unitId :$scope.unitId
								 };
								 var URI = BASE_URL + ROOT_URL + "/api/globals/getDeptListById";
							      GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.status == 200) {
													$rootScope.stopLoader();
													$scope.getDeptList = response.data.listObject;
													
													 /* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
													 $scope.getMultipleDepartmentList=[];
													angular.forEach($scope.getDeptList,function(value,index) {
										                var data = {};
										                data.id = value.deptId;
										                data.label = value.deptName;
										                $scope.getMultipleDepartmentList.push(data);
										          });
													 /* end code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
													
													
												}
											});

							    
							    }
							 $scope.deptList();
								
							 
							 //save fuctionality
							 $scope.saveDeptStroreMapper = function() {
								 console.log("===="+JSON.stringify($scope.deptObj));
								 
								 if($scope.deptObj.id==''){
									 growl.error(
												"Store is required",
												{
													title : 'Error!'
												});
									 return;
								 }
								 if($scope.deptObj.deptId==''){
									 growl.error(
												"Department is required",
												{
													title : 'Error!'
												});
									 return;
								 }
								 
								 
							     $scope.$broadcast('show-errors-check-validity');
									console.log("Depat Master",JSON.stringify($scope.deptObj));
									try {
											$rootScope.startLoader();
											
											var URI = BASE_URL + ROOT_URL
											+ "/api/globals/saveDeptStoremapper";
											GenericService.serviceAction("POST", URI,
													$scope.deptObj)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success("Department Store Mapped Save Successfully..!",
																		{
																			title : 'Success!'
																		});
														$scope.$broadcast('show-errors-reset');
														$scope.deptStoremappedList();
														$scope.initCommon();
														$scope.initDeptStoreMapped(
																		$scope.orgId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
													} else if (response.data.status == 'error') {
														$rootScope.stopLoader();
														growl.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		});
														$scope.initCommon();
														$scope.initDeptStoreMapped(
																		$scope.orgId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
													}

												});
										} catch (e) {
											console.log("Exception", e.message);
										}

						      
								}
							
						
							 $scope.getDeptStoremappedList = [];

						 $scope.deptStoremappedList = function() {
							 $rootScope.startLoader();
								 var data={
										 orgId :$scope.orgId,
										 unitId :$scope.unitId
								 };
								 var URI = BASE_URL + ROOT_URL + "/api/globals/getDeptStoremappedList";
							      GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.status == 200) {
													$rootScope.stopLoader();
													
													$scope.pagination.data = [];
													$scope.getDeptStoremappedList = response.data.listObject;
													
													if($scope.getDeptStoremappedList.length > 0){            
								                         PaginationService.searchData($scope.getDeptStoremappedList, $scope.pagination);
								                       }
													
												}
												else
													{
													$rootScope.stopLoader();
													}
											});

							    
							    }
							 $scope.deptStoremappedList();
                       // update Status
							 $scope.updateStatus = function(deptStoreMapperId, status) {
									try {
										$rootScope.startLoader();
										$scope.initDeptStoreMapped();
										var URI = BASE_URL + ROOT_URL + API +
										        "/globals/activeInactiveDeptStoreMapper"
		                                        + S + $scope.orgId + S + $scope.unitId + S + deptStoreMapperId
												+ S + status;
										
					           	GenericService.serviceAction("GET", URI,
					           			$scope.deptObj)
								.then(
										function(response) {
											if (response.data.status == 'success') {
												$rootScope.stopLoader();
												growl.success(
																"Status Update Successfully..!",
																{
																	title : 'Success!'
																});
												$scope.$broadcast('show-errors-reset');
												$scope.initCommon();
											} else if (response.data.status == 'error') {
												$rootScope.stopLoader();
												growl.error(response.data.message,
																{
																	title : 'Error!'
																});
											}
										});
									} catch (e) {
										console.log("Exception", e.message);
									}

								}
						
							 
							 //edit function 
							 $scope.showUpdateBtn = function(deptStoreMapperId) {
									try {
										$rootScope.startLoader();
										$scope.saveBtnFlag = false;
										$scope.updateBtnFlag = true;
										var URI = BASE_URL + ROOT_URL + API + 
										"/globals/getDeptStoreMapperById"
									    + S+ $scope.orgId + S + $scope.unitId
												+ S + deptStoreMapperId;
										GenericService.serviceAction("GET", URI,$scope.common)
												.then(function(response) {
												if (response.data.status == 'success') {
													$rootScope.stopLoader();
													
							                        $scope.deptObj = response.data.object;
							                        
							                        /* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
							                        $scope.updateDepartmentStoreMapper=true;
							                        let storeIndex = $scope.getMultipleStoreList.findIndex(x => x.id === $scope.deptObj.id);//deptObj.id=storeId
							                        let deptIndex = $scope.getMultipleDepartmentList.findIndex(x => x.id === $scope.deptObj.deptId);
							                        $scope.selectedStore = [ $scope.getMultipleStoreList[storeIndex] ];
							                        $scope.selectedDepartment = [ $scope.getMultipleDepartmentList[deptIndex] ];
							                        /* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
											 
												} else if (response.data.status == 'error') {
													$rootScope
															.stopLoader();
													alert("Error In Fetching Data");
												}
											});
									} catch (e) {
										console.log("Exception", e.message);
									}

								}
							 
							 
							//Update fuctionality
							 $scope.updateDeptStroreMapper = function() {
								 $scope.deptObj.orgId=$scope.orgId;
								 $scope.deptObj.unitId=$scope.unitId;
							     $scope.$broadcast('show-errors-check-validity');
									console.log("Depat Master",JSON.stringify($scope.deptObj));
									try {
											$rootScope.startLoader();
											
											var URI = BASE_URL + ROOT_URL
											+ "/api/globals/updateDeptStoremapper";
											GenericService.serviceAction("POST", URI,
													$scope.deptObj)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success("Department Store Mapped Update successfully",
																		{
																			title : 'Success!'
																		});
														$scope.$broadcast('show-errors-reset');
														$scope.deptStoremappedList();
														$scope.initCommon();
														$scope.initDeptStoreMapped(
																		$scope.orgId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
													} else if (response.data.status == 'error') {
														$rootScope.stopLoader();
														growl.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		});
														$scope.initCommon();
														$scope.initDeptStoreMapped(
																		$scope.orgId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
													}

												});
										} catch (e) {
											console.log("Exception", e.message);
										}

						      
								}
							 
							 
							 
							 /* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
								$scope.getMultipleStoreList = [];								
								//$scope.storeIds=[];
								$scope.selectedStore=[];
								$scope.getMultipleDepartmentList=[];								
								//$scope.departmentIds=[];
								$scope.selectedDepartment=[];
								
								$scope.updateDepartmentStoreMapper=false;
								
							// name of setting  changed from example6settings
							 $scope.multiSelectSettings = {
								      enableSearch: true,
								      scrollableHeight: '150px',
								      scrollable: true
								    };
							 
							// name of setting  changed from yourEvents
							 $scope.multiSelectEvents = {
								      /*onItemSelect: function(selectedValue) {
								    	  $scope.GetDepartmentStoreMapperNew($scope.selectedStore, $scope.selectedDepartment);                     
								      },
								      onItemDeselect: function(selectedValue) {
								    	  $scope.GetDepartmentStoreMapperNew($scope.selectedStore, $scope.selectedDepartment);  
								      },
								      onDeselectAll:function(selectedValue) {
								    	  $scope.GetDepartmentStoreMapperNew($scope.selectedStore, $scope.selectedDepartment);  
								      },
								      onSelectAll:function(selectedValue) {
								    	  $scope.GetDepartmentStoreMapperNew($scope.selectedStore, $scope.selectedDepartment); 
								      },*/
								      onSelectionChanged :function(selectedValue) {
								    	  $scope.GetDepartmentStoreMapperNew($scope.selectedStore, $scope.selectedDepartment); 
								      }
								    };
							 
							 $scope.GetDepartmentStoreMapperNew = function(selectedStore , selectedDepartment) {								 
								 //console.log("storeIds : ",selectedStore);
								 //console.log("departmentIds : ",selectedDepartment);
								 let storeIds=selectedStore.map(x=>x.id);
								 let deptIds=selectedDepartment.map(x=>x.id);
								 
										 var data={
												 orgId :$scope.orgId,
												 unitId :$scope.unitId,
												 storeIds : storeIds,
												 deptIds : deptIds
										 };
										 
										 var URI = BASE_URL + ROOT_URL + "/api/globals/getDeptStoremappedListNew";
										 $rootScope.startLoader();
									      GenericService.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.status == 200) {
															$rootScope.stopLoader();
															$scope.getDeptStoremappedList = response.data.listObject;
														}else{
															$rootScope.stopLoader();
															}
														});
									      }
							 
							 $scope.SaveAndUpdateDeptStroreMapper = function() {
								 var URI = BASE_URL + ROOT_URL+ "/api/globals/SaveAndUpdateDeptStroreMapper";
								 
								 let storeIds=$scope.selectedStore.map(x=>x.id);
								 let deptIds=$scope.selectedDepartment.map(x=>x.id);
								 
								 if(storeIds.length == 0){
									 growl.error("Store is required", {title : 'Error!'});
									 return;
								 }
								 if(deptIds.length == 0){
									 growl.error("Department is required", {title : 'Error!'});
									 return;
								 }
								 
								 let data ={
										 deptStoreMapperId : $scope.deptObj.deptStoreMapperId,
										 orgId :$scope.orgId,
										 unitId :$scope.unitId,
										 storeIds : storeIds,
										 deptIds : deptIds,
										 status : "A",
										 createdBy : $scope.userId,
										 updatedBy : $scope.userId										 
								 }
								 console.log(data);
								 try{
									 GenericService.serviceAction("POST", URI, data)
									 .then(
											 function(response) {
												 if (response.data.status == 'success') {
													 $rootScope.stopLoader();
													 if(response.data.message != null && response.data.message != undefined){
														 growl.warning(response.data.message ,{title : 'Success!'});
														 //growl.info(response.data.message ,{title : 'Success!'});
													 }else{
														 growl.success("Department Store Mapped Save Successfully..!",{title : 'Success!'});
														 }
													 $scope.$broadcast('show-errors-reset');
													 $scope.cancelUpdate();
													 $scope.deptStoremappedList();
													 } else if (response.data.status == 'error') {
														 $rootScope.stopLoader();
														 growl.error(response.data.message,{ title : 'Error!'});
														 }
												 });
									 } catch (e) {
										 console.log("Exception", e.message);
										 }
								 }
							 
							 $scope.cancelUpdate = function(){
								 $scope.updateDepartmentStoreMapper=false;
								 $scope.selectedStore=[];
								 $scope.selectedDepartment=[];
								 $scope.initCommon();
								 $scope.initDeptStoreMapped($scope.orgId,$scope.offset,$scope.noOfRecordsPerPage);
							 }
							 /* end code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
							 
			
			} ]);