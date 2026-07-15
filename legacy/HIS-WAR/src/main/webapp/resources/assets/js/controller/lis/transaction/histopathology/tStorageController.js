
/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"tStorageController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Storage Controller";
							
							$scope.firstLevel = true;
							$scope.secondLevel = true;
							$scope.thirdLevel = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultEntryworklistForValidation  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.storeListCount;
							$scope.labResultTestDtls;
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = labConfig.histopathologyId;
							
							$scope.listHelpValueMaster = [];
							
							$scope.listTSlideStorageMasterDto=[];
							  /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
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
							
							
							/*Third Level*/
							$scope.thirdTabLevel = 1 ;
							
							$scope.setThirdTabLevel = function(newTab)
							{
								$scope.thirdTabLevel = newTab;
							};
							
							$scope.isSetThirdTabLevel = function(tabNum)
							{
								return $scope.thirdTabLevel === tabNum;
							};
							
							
							$scope.open = function() {
								$scope.popup.opened = true;
							};

							$scope.popup =
							{
								opened : false
							};
							
							$scope.popup1 =
							{
								opened : false
							};
							
							$scope.open1 = function() 
							{
								$scope.popup1.opened = true;
							};

							/*FORTH Level*/
							$scope.forthTabLevel = 1 ;
							
							$scope.setForthTabLevel = function(newTab)
							{
								$scope.forthTabLevel = newTab;
							};
							
							$scope.isSetForthTabLevel = function(tabNum)
							{
								return $scope.forthTabLevel === tabNum;
							};
							
							$scope.listLabResultMasterDto  = [];
							$scope.HistoParamDto = {
						            "orgId": $scope.orgId,
						            "orgUnitId":$scope.orgUnitId,
						            "subDeptId":$scope.subDeptId,
						            "deptId": $scope.deptId,
						            "offset":$scope.offset,
						            "recordPerPage": $scope.recordPerPage,
							}

							
							$scope.TSlideStorageMasterDto={
									
									  "tSlideStorageId":"",
									    "rackId":"",
									    "shelfId":"",
									    "tSlideId":"",
									    "remark":"",
									    "isSlideAccepted":"",
									    "orgId":$scope.orgId,
									    "orgUnitId":$scope.orgUnitId,
									    "createdBy":$scope.userId,
									    "createdDate":"",
									    "updatedBy":$scope.userId,
									    "updatedDate":"",	
										"isDeleted":"N"
										
							}
						
							$scope.setNoOfRecords = function() {
								$scope.initTStorage($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initTStorage = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage									 
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ SLIDE_STORAGE_LIST;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ SLIDE_STORAGE_LIST_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultTStorageList = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
														
														console.log("$scope.resultTStorageList", JSON
																.stringify($scope.resultTStorageList));	
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initTStorage($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
									$scope.noOfRecordsPerPage);
				

							$scope.getTStorageList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage
										};
									var URI = BASE_URL + ROOT_URL
														+ LIS_HISTOPATHOLOGY
														+ SLIDE_STORAGE_LIST;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultTStorageList  = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular.forEach($scope.resultTStorageList,
												function(resultEntry)
												{
									              resultEntry.printReport = $scope.selectedAll;
												});
							}
							
							/*
							 *  Get Rack Master List
							 */
							$scope.getRackList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
									        + LIS_COMMON
											+ GET_RACK_LIST
											+S
											+$scope.orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.getRackList  = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.getRackList();
							
							/*
							 *  Get Shelf List ByRank ID
							 */
							$scope.getShelfListByRankId = function(rackId) {
								try {
									if(rackId==undefined){
										$scope.shelfListByRankId =[];
									}else{
										var data = "";
										var URI = BASE_URL + ROOT_URL
										        + LIS_COMMON
												+ GET_SHELF_LIST_BY_RACK_ID
												+S+rackId+ S
												+ $scope.orgId;
										console.log("URI", URI);
										GenericService
												.serviceAction("GET", URI, data)
												.then(
														function(response) {
															$scope.shelfListByRankId  = response.data.listObject;
														});
									}
									
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.showDetails = function()
							{
								$('#storageDetails').modal('show');
							}
							
							
							/*
							 *  Get Shelf List ByRank ID
							 */
							$scope.getSlideStorageDtls = function() {
								try {
										var data = "";
										var URI = BASE_URL + ROOT_URL
										        + LIS_HISTOPATHOLOGY
												+ GET_SLIDES_STORAGE_DTLS
												+ S+$scope.rackId+ S
												+ $scope.shelfId + S
												+ $scope.orgId 
												+S+ $scope.orgUnitId;;
										console.log("URI", URI);
										GenericService
												.serviceAction("GET", URI, data)
												.then(
														function(response) {
															$scope.listSlideStorageDtls  = response.data.object;
															
															console.log("$scope.listSlideStorageDtls", JSON
																	.stringify($scope.listSlideStorageDtls));	
														});
									
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.saveDetails = function() {
								
								
								$rootScope.startLoader();
								angular
								.forEach(
										$scope.resultTStorageList,
										function(value,key) 
										{
											
											if(value.printReport==true)
												{
										var tSlideStorageMasterDto = angular
												.copy($scope.TSlideStorageMasterDto);
										tSlideStorageMasterDto.tSlideStorageId =value.tSlideStorageId;
										tSlideStorageMasterDto.rackId =$scope.rackId;
										tSlideStorageMasterDto.shelfId=$scope.shelfId;
										tSlideStorageMasterDto.tSlideId=value.tSlideId;
										tSlideStorageMasterDto.remark=$scope.remark;
										tSlideStorageMasterDto.isSlideAccepted="A";
										tSlideStorageMasterDto.createdBy = $scope.userId;
										tSlideStorageMasterDto.orgId=$scope.orgId;
										tSlideStorageMasterDto.orgUnitId=$scope.orgUnitId;
										tSlideStorageMasterDto.isDeleted="N";
										$scope.listTSlideStorageMasterDto
												.push(tSlideStorageMasterDto);
										console.log("$scope.listTSlideStorageMasterDto", JSON
												.stringify($scope.listTSlideStorageMasterDto));	
										     }else{
										     }
										});
							
								var URI = BASE_URL + ROOT_URL
								+ LIS_HISTOPATHOLOGY + ACCEPT_SLIDE_STORAGE;

								try {
									console.log("$scope.listTSlideStorageMasterDto", URI);
									GenericService
											.serviceAction("POST", URI,
													$scope.listTSlideStorageMasterDto)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															$('#storageDetails').modal('hide');
															$scope.initTStorage($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
															growl.success(response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');

														} else if (response.data.status == 'error') {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
							}
							
							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getTStorageList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							
							
							
							
							
						/*	==============================STORE DETAILS PAGE START===================================*/
							
							
							$scope.setNoOfRecordsStoreDetails = function() {
								$scope.initStoreDetails($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initStoreDetails = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage									 
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ STORAGED_SLIDE_LIST;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ STORAGED_SLIDE__LIST_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.storeDetailsList = response[0].listObject;
														$scope.storeListCount = response[1].object;
														$scope.setPage(1,false);
														
														console.log("$scope.storeDetailsList", JSON
																.stringify($scope.storeDetailsList));	
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initStoreDetails($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
									$scope.noOfRecordsPerPage);
				

							$scope.getStoreDetailsList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage
										};
									var URI = BASE_URL + ROOT_URL
														+ LIS_HISTOPATHOLOGY
														+ SLIDE_STORAGE_LIST;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.storeDetailsList  = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.rackSelect ='Please Select Rack';

							$scope.rackShelf ='Please Select Shelf';
							
							
						} ]);