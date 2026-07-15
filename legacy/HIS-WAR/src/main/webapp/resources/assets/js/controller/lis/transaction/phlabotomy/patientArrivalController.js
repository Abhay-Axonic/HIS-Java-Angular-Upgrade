/**
 * @author Sruaj Kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"patientArrivalController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'$filter',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies,$filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Patient Arrival";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.patientArrivalList;
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							$scope.currentStatus="R";
							$scope.isPaginationReq=true;
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							
							$scope.sendToDept=12;
							$scope.statusId=1;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
						
							$scope.listLabPatientArrival = [];
							$scope.labPatientArrivalDetails={
									"patientArrivalMasterId": {
										"statusId": "",
										"orderId": "",
										"labSmpDtlsId": "",
										"labSmpMstId": "",
										"patientId": ""
								
									},
									"testId":"",
									"pendingCount": "",
									"orgId": $scope.orgId,
									"orgUnitId":$scope.orgUnitId,
									"deptId": "",
									"doctorId": "",
									"collectionTime":"",
									"createdBy": $scope.userId,
									"createdDate": "",
									"orderDetailsId":""
								
								
								}
							
							/* code for setting Label */
							$(".selectedPageName").text("Patient Arrival List");

							
							// ====================================================CODE
							      // Patient Arrival 
							// LIST===========================================================
							
							
							$scope.setNoOfRecords = function() {
							
								$scope.initPatientArrivalList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.initPatientArrivalList = function(orgId,orgUnitId,offset, noOfRecordsPerPage)
							{
								var data1 = {
										  "orgId" : $scope.orgId,
										  "orgUnitId" :$scope.orgUnitId,
										  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
				                          "patientId" :  $scope.tempSelectedPatientId!=undefined ? $scope.tempSelectedPatientId : null,
										  "visitTypes" :$scope.selectedVisitType,
										  "testTypes" : $scope.selectedtestList,
										  "deptId" : $scope.deptId,
										  "subDepts" : $scope.selectedDepartMentList,
										  "assignUnit" :$scope.assignUnit ,
										  "sampleStatusId":$scope.statusId,
										  "offset":offset,
										  "recordPerPage":noOfRecordsPerPage 
								};
								 $scope.isPaginationReq=true;
								$rootScope.startLoader();
								
								var URI1 = BASE_URL + ROOT_URL +  "/LIS/transaction/patientArrival/Search";
								console.log("URI1",URI1);
								GenericService.serviceAction("POST", URI1, data1).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												$scope.patientArrivalList = response.data.listObject;
												$scope.commonListCount = response.data.object;
												console.log("$scope.patientArrivalList  IN ",$scope.patientArrivalList );
												console.log("$scope.commonListCount IN",$scope.commonListCount);
												$scope.setPage(1, false);
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

					$scope.initPatientArrivalList($scope.orgId, $scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
							
							
							$scope.getPatientArrivalList = function(orgId,orgUnitId, offset, noOfRecordsPerPage)
							{
									try {
										
										var data = {
												 "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.orgUnitId,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "patientId" :  $scope.tempSelectedPatientId!=undefined ? $scope.tempSelectedPatientId : null,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.assignUnit ,
												  "sampleStatusId":$scope.statusId,
												  "offset":offset,
												  "recordPerPage":noOfRecordsPerPage 	
										}
										$rootScope.startLoader();
										console.log("PatientArrival",JSON.stringify($scope.data));
										var URI = BASE_URL 
								        + ROOT_URL
								    	+ "/LIS/transaction/patientArrival/Search";
										GenericService.serviceAction("POST", URI, data).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												$scope.patientArrivalList = response.data.listObject;
												console.log("$scope.patientArrivalList get",$scope.patientArrivalList);
												$rootScope.stopLoader();
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
											
										});
									} catch (e) {
										console.log("Exception",e.message);
									}
									
							}
								
								
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular
										.forEach(
												$scope.patientArrivalList,
												function(labDetailsObj) {
													labDetailsObj.isArrived = $scope.selectedAll;
												});
							}

							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getPatientArrivalList($scope.orgId,$scope.orgUnitId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							
							$scope.checkSample = function()
							{
								var listLabPatientArrival =[];
								angular
								.forEach(
										$scope.patientArrivalList,
										function(value, key) {
											if (value.isArrived == true) 
											{
												var labPatientArrivalDetails = angular
														.copy($scope.labPatientArrivalDetails);
												labPatientArrivalDetails.patientArrivalMasterId.statusId =IS_ARRIVED;
												labPatientArrivalDetails.patientArrivalMasterId.orderId =value.orderId;
												labPatientArrivalDetails.patientArrivalMasterId.labSmpDtlsId=value.labSampleDtlsId;
												labPatientArrivalDetails.patientArrivalMasterId.labSmpMstId=value.labSampleId;
												labPatientArrivalDetails.patientArrivalMasterId.patientId=value.patientId;
												labPatientArrivalDetails.pendingCount =value.samplePendingCount;
												labPatientArrivalDetails.deptId =value.deptId;
												labPatientArrivalDetails.orderDetailsId=value.orderDetailsId;
												labPatientArrivalDetails.testId=value.testId;
												labPatientArrivalDetails.doctorId =value.doctorId;
												labPatientArrivalDetails.createdBy = $scope.userId;
												listLabPatientArrival
														.push(labPatientArrivalDetails);
											}
										});
								if(listLabPatientArrival.length==0)
								{
									growl.warning('Please select patient.',
											{
												title : 'Warning!'
											});
									  return false;
								}
								else{
									$("#patientArrival").modal('show');
								 }
							}
							
							
							
							// code for Activating Patient Arrival
							$scope.saveDetails = function() {
							
								$rootScope.startLoader();
								angular
										.forEach(
												$scope.patientArrivalList,
												function(value, key) {
													if (value.isArrived == true) 
													{
														var labPatientArrivalDetails = angular
																.copy($scope.labPatientArrivalDetails);
														labPatientArrivalDetails.patientArrivalMasterId.statusId =IS_ARRIVED;
														labPatientArrivalDetails.patientArrivalMasterId.orderId =value.orderId;
														labPatientArrivalDetails.patientArrivalMasterId.labSmpDtlsId=value.labSampleDtlsId;
														labPatientArrivalDetails.patientArrivalMasterId.labSmpMstId=value.labSampleId;
														labPatientArrivalDetails.patientArrivalMasterId.patientId=value.patientId;
														labPatientArrivalDetails.pendingCount =value.samplePendingCount;
														labPatientArrivalDetails.deptId =value.deptId;
														labPatientArrivalDetails.orderDetailsId=value.orderDetailsId;
														labPatientArrivalDetails.testId=value.testId;
														labPatientArrivalDetails.doctorId =value.doctorId;
														labPatientArrivalDetails.createdBy = $scope.userId;
														$scope.listLabPatientArrival
																.push(labPatientArrivalDetails);
														console.log("$scope.listLabPatientArrival", JSON
																.stringify($scope.listLabPatientArrival));
													}
												});
								
								console.log("$scope.listLabPatientArrival", JSON
										.stringify($scope.listLabPatientArrival));
								var URI = BASE_URL + ROOT_URL
										+ LIS_TRANSACTION + ADD_PATIENT_ARRIVAL_DETAILS;

								try {
									console.log("PatientArrival", URI);
									GenericService
											.serviceAction("POST", URI,
													$scope.listLabPatientArrival)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															 $scope.saveBtn = false;
															$scope.initPatientArrivalList(
																			$scope.orgId,
																			$scope.orgUnitId,
																			$scope.offset,
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
/*====================================SEARCH DETAILS=====================================*/
							
							
							$scope.clearFields=function(){
								 $scope.selectedPatient=undefined;
								 $scope.patientId=undefined;
								 $scope.uhid=undefined;
								 $scope.fromDate = moment().toDate();
								 $scope.toDate = moment().toDate();
								    
								 $scope.selectedVisitType=[];
								 $scope.selectedtestList=[];
								 $scope.selectedDepartMentList=[];
								 $scope.isPaginationReq=true;
								 $scope.tempSelectedPatientId=undefined;
								 $scope.initPatientArrivalList($scope.orgId, $scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
							}
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
							};

							
							$scope.todateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
								minDate : new Date(),
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
							
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.fromDate.getTime();
								
								if(moment($scope.toDate).isBefore(moment($scope.fromDate)))
								{
									 $scope.toDate=new Date($scope.fromDate.getTime());
								}
							}
							
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
							
						
							$scope.selectedVisitTypeIds = [];
							$scope.sampleStatusIds=[SEND_TO_DEPT];
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							
						    $scope.dropDownSetting = {
						    		scrollableHeight: '100px', 
						    		scrollable: true
						    }; 
						    
						    $scope.multiselectObj = {
						    		"id":"",
						    		"label":""
						    }
						    
						/** Service Autocomplete List * */
						$scope.getPatientArrivalPatientSearch = function(searchKeyword)
						{
						    if(searchKeyword.length == 3)
							{
							try 
							{
								var searchObj = {
										  "orgId" : $scope.orgId,
										  "orgUnitId" :$scope.assignUnit,
										  "searchKeyword":searchKeyword,
										  "sampleStatusId":$scope.statusId
                                 }								
								
								var URI = BASE_URL 
						        + ROOT_URL
								+ LIS_BIOCHEMISTRY
								+ BIOCHECMISTRY_PATIENT_LIST_SEARCH 
								console.log("URI", URI);
								return GenericService.serviceAction("POST", URI,
										searchObj).then(function(response) {
									console.log("response.data.listObject",response.data.listObject);
									$scope.patientListNew =response.data.listObject;
									return response.data.listObject;
								});								
							} catch (e) {
								console.log(e.message)
							}
							}
						    else {
						        var filteredList = $filter('filter')($scope.patientListNew, { $: searchKeyword });
						        if (filteredList.length === 0) {
						            $scope.filteredPatientList = null;
						        } else {
						            $scope.filteredPatientList = filteredList;
						        }
						        return $scope.filteredPatientList;
						    }
						};	
						    $scope.selectPatient = function($item, $model,$label)
							{
						      $scope.selectedPatient = $item.id ;
						      $scope.tempSelectedPatientId=$item.id ;
						      $scope.uhid=$item.uhid;
					        }
						    
						/*get Visi tType List*/
						$scope.getVisitTypeList = function()
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId,
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
										+ LIS_HISTOPATHOLOGY
										+ HISTO_MICRO_VISIT_LIST
								console.log("URI", URI);
								 GenericService.serviceAction("POST", URI,
										data).then(function(response) {
									console.log("response.data.listObject",response.data.listObject);
									angular.forEach(response.data.listObject, function(value, key) {
										var multiselectObj = angular.copy($scope.multiselectObj);
										multiselectObj.id = value.id;
										multiselectObj.label = 	value.label;
											$scope.visityTypeList.push(multiselectObj);
										});								
									
								});
							} catch (e) {
								console.log(e.message)
							}
						}
						
						$scope.getVisitTypeList();
						
						
						$scope.getTestList = function()
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
										+ LIS_HISTOPATHOLOGY
										+ HISTO_MICRO_TEST_LIST
								console.log("URI", URI);
								 GenericService.serviceAction("POST", URI,
										data).then(function(response) {
									console.log("response.data.listObject",response.data.listObject);
									$scope.testList =  response.data.listObject;
								});
							} catch (e) {
								console.log(e.message)
							}
						}
						$scope.getTestList();
						
						$scope.getDepartMentList = function(orgId,orgUnitId) {
							try {
								var data = "";
								var URI = BASE_URL + ROOT_URL
										+ LIS_COMMON + GET_DEPARMENT_LIST
										+ S + orgId + S + orgUnitId;
								console.log("URI", URI);
								GenericService.serviceAction("GET", URI, data)
										  .then(function(response) {
											  angular.forEach(response.data.listObject, function(value, key) {
													var multiselectObj = angular.copy($scope.multiselectObj);
													multiselectObj.id = value.id;
													multiselectObj.label = 	value.name;
														$scope.departmentList.push(multiselectObj);
													});
												});
							   } catch (e) {
								console.log(e.message)
							}
						}
						$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
						
						
						
						 $scope.searchOutPatientArrivalList = function()
							{
								try 
								{						
									
									$rootScope.startLoader();
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.orgUnitId,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						                          "patientId" :  $scope.tempSelectedPatientId!=undefined ? $scope.tempSelectedPatientId : null,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.assignUnit ,
												  "sampleStatusId":$scope.statusId,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage 
		                             }
									
									var isValidDate = $rootScope.isValidDate(searchObj.fromDt,searchObj.toDt);
									if(!isValidDate){
										$rootScope.stopLoader();
										growl.warning("From date can not be greater than to Date ",
												{
													title : 'warning!'
												});
										return false;
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
									    	+ "/LIS/transaction/patientArrival/Search";
									console.log("URI", URI);
									$rootScope.startLoader();
									GenericService.serviceAction("POST", URI, searchObj).then(function(response) 
											{	
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													$scope.patientArrivalList = [];
													$scope.patientArrivalList = response.data.listObject;
													$scope.commonListCount = response.data.object;
													$scope.setPage(1, false);
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
												}
												
											});
								} catch (e) {
									console.log(e.message)
								}
							}
						 $scope.saveBtn = false;
						 $scope.selectPatientArrivalData = function(status,index){
							   var checkedCount = 0;
		                       angular.forEach($scope.patientArrivalList, function(value, key) {
		                      
		                           if(value.isArrived == true){
		                                checkedCount = checkedCount + 1;
		                           }
		                      });
		                       
		                       if(checkedCount > 0){
		                           $scope.saveBtn = true;
		                       }else{
		                           $scope.saveBtn = false;
		                       } 
						 }
						
						} ]);