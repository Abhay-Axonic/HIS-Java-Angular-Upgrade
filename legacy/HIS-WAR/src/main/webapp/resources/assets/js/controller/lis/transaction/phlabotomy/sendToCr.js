/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"sendToCrController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'$filter',
						'GenericService',
						'BillingGenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, BillingGenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.collectedSampleList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							if (cookieObjectAppConfigSecond != undefined) {
							$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
							$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
							}
							$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
							if($scope.isBillManipulationAllowed == true){
								$scope.filterDummyService = 1;
							}else{
								$scope.filterDummyService = 2;
							}
							$scope.visitTypeId = 1;
							
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							 $scope.saveBtn = false;

							/* code for setting Label */
							$(".selectedPageName").text("Send To CR");

							$scope.listLabSampleDtls = [];
							$scope.labSampleDtls = {
								"labSampleDtlsId" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"testId" : "",
								"deptId" : $scope.deptId,
								"subDeptId" : "",
								"deptName" : "",
								"labSampleId" : "",
								"sampleBarcode" : "",
								"sampleId" : "",
								"sampleNo" : "",
								"orderDetailsId" : "",
								"orderId" : "",
								"orderQty" : "",
								"serviceId" : "",
								"testCode" : "",
								"testDesc" : "",
								"sampleName" : "",
								"sampleVolume" : "",
								"unitName" : "",
								"profileId" : "",
								"panelCode":"",
								"packageId" : "",
								"isCentrifugationReq" : "",
								"isAlliquoteReq" : "",
								"sampleTypeId" : "",
								"containerId" : "",
								"containerName" : "",
								"sampleReqCount" : "",
								"samplePendingCount" : "",
								"sampleGenDatetime" : "",
								"sampleGenBy" : "",
								"currStatus" : "",
								"sampleStatusId" : "",
								"isOutsourced" : "",
								"outsourcedLabId" : "",
								"sampleSendtocrDatetime" : "",
								"sampleSendtocrBy" : $scope.userId,
								"sampleCollectionDatetime" : "",
								"sampleCollectionBy" : "",
								"sampleCentrifugationDatetime" : "",
								"sampleCentrifugationBy" : "",
								"sampleAcceptDatetime" : "",
								"sampleAcceptBy" : "",
								"sampleRejectDatetime" : "",
								"sampleRejectBy" : "",
								"sampleRejectReasonId" : "",
								"sampleRejectReason" : "",
								"sampleRecollectFlag" : "",
								"sampleRecollectAgainstId" : "",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"sampleTestTime":"",
								"patientVisitAge":"",
								"genderId":"",
								"visitType":"",
								"assignUnit":"",
								"performTestId" : "",
								"performedUnit":$scope.orgUnitId,
								"listTMicroSampleMediaDto":[]	
							}

							/**DatePicker Code  */
							
							$scope.toDate = new Date();
							$scope.fromDate = new Date();
						    $scope.completed='N';
							$scope.pending = 'N';
							$scope.outsourced = 'N';
							
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
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

							
							
							
							// ====================================================CODE
							// START FORFOR SAMPLE MASTER
							// LIST===========================================================
						    var specDocObj = {
					    			organizationId:$scope.organizationId,
					    			userId:$scope.userId
					        }
						    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
					        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
											if (response.data.status == "success"){
												$scope.staffSpecialityList = [];
												$scope.staffSubSpecialityList = [];
			                                    $scope.staffSpecialityList = response.data.listObject;
											    $scope.staffSubSpecialityList = response.data.object;
											}
		                                 });
					        
							$scope.setNoOfRecords = function() {
								$scope.initCollectedSampleList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initCollectedSampleList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								$rootScope.startLoader();

								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 50;
								var data = {
										 "orgId" : $scope.orgId,
										 "orgUnitId" :$scope.assignUnit,
										  "assignUnit" :$scope.orgUnitId ,
										  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
		                            	  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
										  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
										  "patientId" : $scope.selectedPatient,
										  "visitTypeId" :$scope.visitTypeId,
										  "labSampleDtlsId" : $scope.labSampleDtlsId,
										  "deptId" : $scope.deptId,
										  "subDepts" :$scope.selectedDepartMentList,
										  "offset" : offset,
										  "recordPerPage" : noOfRecordsPerPage,
										  "userId" : $scope.userId
								};
								
								var URI1 = BASE_URL + ROOT_URL
										+ LIS_TRANSACTION 
										+ SEND_TO_CR_SEARCH ;
						
								
								GenericService.serviceAction("POST", URI1, data).then(
										function(response) {
											$rootScope.stopLoader();
											$scope.collectedSampleList =[];
											 $scope.collectedSampleListTest=[];
												$scope.collectedSampleListTest = response.data.listObject;
												
												if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
													angular.forEach($scope.collectedSampleListTest, function(value, key) {
														angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
															if(value.subDeptId == value1.subSpecialityMasterId){
																$scope.collectedSampleList.push(value);
															}
														
														});		
													 });
												}else{
													$scope.collectedSampleList = response.data.listObject;
												}
												$scope.collectedSampleListForFiltter = $scope.collectedSampleList; 
												$scope.filterByDummyServices();
											$scope.commonListCount = response.data.object> 0 ? response.data.object : 0 ;
											$scope.setPage(1, false);
										});
								
							}
							
							$scope.clearFields = function()
							{
								$scope.patientId = undefined;
								$scope.selectedPatient = null;
								$scope.selectedVisitType = [];
								$scope.orderStatusId = '';
								$scope.priorityId = '';
								$scope.uhid = '';
								$scope.selectedSampleNo='';
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
								$scope.initCollectedSampleList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getCollectedSampleList = function(orgId,
									orgUnitId,offset,
									noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data =  {
									  "orgId" : $scope.orgId,
									  "orgUnitId" :$scope.assignUnit,
									  "assignUnit" :$scope.orgUnitId ,
									  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
	                            	  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
									  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
									  "patientId" : $scope.selectedPatient,
									  "visitTypeId" :$scope.visitTypeId,
									  "labSampleDtlsId" : $scope.labSampleDtlsId,
									  "deptId" : $scope.deptId,
									  "subDepts" :$scope.selectedDepartMentList,
									  "offset" : offset,
									  "recordPerPage" : noOfRecordsPerPage,
									  "userId" : $scope.userId
									};
									
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION
											+ SEND_TO_CR_SEARCH ;
									
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.collectedSampleList =[];
														 $scope.collectedSampleListTest=[];
															$scope.collectedSampleListTest = response.data.listObject;
															
															if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
																angular.forEach($scope.collectedSampleListTest, function(value, key) {
																	angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																		if(value.subDeptId == value1.subSpecialityMasterId){
																			$scope.collectedSampleList.push(value);
																		}
																	
																	});		
																 });
															}else{
																$scope.collectedSampleList = response.data.listObject;
															}
															
															$scope.collectedSampleListForFiltter = $scope.collectedSampleList; 
															$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 ) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getCollectedSampleList($scope.orgId,$scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							$scope.initCollectedSampleList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);

							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular
										.forEach(
												$scope.collectedSampleList,
												function(labDetailsObj) {
													labDetailsObj.sampleStatusId = $scope.selectedAll;
												});
								
								 $scope.selectPatientForSave();
							}

							// ====================================================CODE
							// END FOR SAMPLE MASTER
							// LIST===========================================================


							$scope.filterByDummyServices= function()
                            {
								$scope.collectedSampleList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.collectedSampleList = angular.copy($scope.collectedSampleListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.collectedSampleListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.collectedSampleList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.collectedSampleListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.collectedSampleList.push(value);
                                              });
                                               
                                }
                            }
							
							
							$scope.checkTest = function()
							{
								var listLabSampleDtls =[];
								angular
								.forEach(
										$scope.collectedSampleList,
										function(value, key) {
											if (value.sampleStatusId == true) {
												var labSampleDtls = angular
														.copy($scope.labSampleDtls);
												labSampleDtls.labSampleDtlsId = value.labSampleDtlsId;
												labSampleDtls.sampleStatusId = SAMPLE_SEND_TO_CR;
												labSampleDtls.sampleSendtocrBy = $scope.userId;
												labSampleDtls.createdBy = $scope.userId;
												labSampleDtls.updatedBy = $scope.userId;
												labSampleDtls.assignUnit = value.assignUnit;
												labSampleDtls.performTestId = value.performTestId;
												listLabSampleDtls
														.push(labSampleDtls);
											}
										});
								if(listLabSampleDtls.length==0)
								{
									growl.warning('Please select sample.',
											{
												title : 'Warning!'
											});
									  return false;
								}
								else{
									$("#sendToCr").modal('show');
								 }
							}
							
							
							// code for Activating Inactivating Sample
							$scope.saveDetailflag = false;
							$scope.saveDetails = function() {
								
								if($scope.saveDetailflag){
									return false;
								}
								$scope.saveDetailflag = true;
								angular
										.forEach(
												$scope.collectedSampleList,
												function(value, key) {
													if (value.sampleStatusId == true) {
														var labSampleDtls = angular
																.copy($scope.labSampleDtls);
														labSampleDtls.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtls.sampleStatusId = SAMPLE_SEND_TO_CR;
														labSampleDtls.sampleSendtocrBy = $scope.userId;
														labSampleDtls.createdBy = $scope.userId;
														labSampleDtls.updatedBy = $scope.userId;
														labSampleDtls.assignUnit = value.assignUnit;
														labSampleDtls.performTestId = value.performTestId;
														$scope.listLabSampleDtls.push(labSampleDtls);
													}
												});
								var URI = BASE_URL + ROOT_URL
										+ LIS_TRANSACTION + SEND_TO_CR;

								try {
									
									if($scope.listLabSampleDtls.length==0)
									{
										growl.error("Please select sample..!!",
											{
												title : 'Error!'
											});
										$scope.saveDetailflag = false;
									   $rootScope.stopLoader();
									   return false;
									}
									$rootScope.startLoader();
									GenericService
											.serviceAction("POST", URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															$scope.saveDetailflag = false;
															$scope.initCollectedSampleList(
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
															$scope.saveDetailflag = false;
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
									$scope.saveDetailflag = false;
								}
							}
							
							/*====================================SEARCH DETAILS=====================================*/
							
						    $scope.dropDownSetting = {
						    		scrollableHeight: '100px', 
						    		scrollable: true
						    }; 
						    
						    $scope.multiselectObj = {
						    		"id":"",
						    		"label":""
						    }
						    
						/** Service Autocomplete List * */
						$scope.getSendToCRList = function(searchKeyword)
						{
						    if(searchKeyword.length == 3)
							{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"visitTypeId":$scope.visitTypeId,
										"sampleStatusId":SAMPLE_COLLECTED,
										"searchKeyword":searchKeyword,
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
										+ LIS_TRANSACTION
										+ SEARCH_SEND_TO_CR_LIST_PATIENT;
								console.log("URI", URI);
								return GenericService.serviceAction("POST", URI,
										data).then(function(response) {
									console.log("response.data.listObject",response.data.listObject);
									$scope.patientListNew =response.data.listObject;
									return response.data.listObject;
								});
							} catch (e) {
								console.log(e.message)
							}
						}	else {
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
						      $scope.selectedPatient = $item.id;
						     // $scope.PatientName= $item.firstName +"  "+$item.lastName;
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
										"searchKeyword":"",
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
										"subDeptId":$scope.subDeptId,
										"searchKeyword":"",
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
						
						
						/*get Sample Number List*/
						$scope.getSampleNoList = function(searchKeyword)
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"sampleStatusId":SAMPLE_COLLECTED,
										"searchKeyword":searchKeyword,
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
								    	+ LIS_TRANSACTION
										+ SEARCH_GENRAL_LAB_SAMPLE_NO_LIST
								console.log("URI", URI);
								
									return GenericService.serviceAction("POST", URI,
											data).then(function(response) {
										console.log("response.data.listObject",response.data.listObject);
										return response.data.listObject;
									});
									
							} catch (e) {
								console.log(e.message)
							}
						}
						
						
						 $scope.selectSampleNo= function($item, $model,$label)
							{
						      $scope.selectedSampleNo = $item.sampleNo;
						      $scope.labSampleDtlsId = $item.labSampleDtlsId;
						    
					        }
						 
						 $scope.getTestList = function()
							{
								try 
								{
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"searchKeyword":"",
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
						 $scope.searchSendToCRList = function()
							{
								try 
								{
									var data = {
											 "orgId" : $scope.orgId,
											 "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId ,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
			                            	  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											  "patientId" : $scope.selectedPatient,
											  "visitTypeId" :$scope.visitTypeId,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "deptId" : $scope.deptId,
											  "subDepts" :$scope.selectedDepartMentList,
											  "offset" : $scope.offset,
											  "recordPerPage" : $scope.noOfRecordsPerPage,
											  "userId" : $scope.userId
									};
									
									var isValidDate = $rootScope.isValidDate(data.fromDt,data.toDt);
									if(!isValidDate){
										$rootScope.stopLoader();
										growl.warning("From date can not be greater than to Date ",
												{
													title : 'warning!'
												});
										return false;
									}
									
									var URI1 = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION 
											+ SEND_TO_CR_SEARCH ;
									
									GenericService.serviceAction("POST", URI1, data).then(
											function(response) {
												$rootScope.stopLoader();
												$scope.collectedSampleList =[];
												 $scope.collectedSampleListTest=[];
													$scope.collectedSampleListTest = response.data.listObject;
													if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
														angular.forEach($scope.collectedSampleListTest, function(value, key) {
															angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																if(value.subDeptId == value1.subSpecialityMasterId){
																	$scope.collectedSampleList.push(value);
																}
															
															});		
														 });
													}else{
														$scope.collectedSampleList = response.data.listObject;
													}
													$scope.collectedSampleListForFiltter = $scope.collectedSampleList; 
													$scope.filterByDummyServices();
												$scope.commonListCount =  response.data.object>0 ? response.data.object : 0 ;
												$scope.setPage(1, false);
											});
								} catch (e) {
									console.log(e.message)
								}
							}
						
						 
						 $scope.searchType = function(searchKeyword)
							{
							 
							 if(searchKeyword.length >= 3){

									try 
									{
										$rootScope.startLoader();
										var data = {
												 "orgId" : $scope.orgId,
												 "orgUnitId" :$scope.assignUnit,
												  "assignUnit" :$scope.orgUnitId ,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
				                            	  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
												  "patientId" : $scope.selectedPatient,
												  "visitTypeId" :$scope.visitTypeId,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "searchField":searchKeyword,
												  "deptId" : $scope.deptId,
												  "subDepts" :$scope.selectedDepartMentList,
												  "offset" : $scope.offset,
												  "recordPerPage" : $scope.noOfRecordsPerPage,
												  "userId" : $scope.userId
										};
										
										var isValidDate = $rootScope.isValidDate(data.fromDt,data.toDt);
										if(!isValidDate){
											$rootScope.stopLoader();
											growl.warning("From date can not be greater than to Date ",
													{
														title : 'warning!'
													});
											return false;
										}
										
										var URI1 = BASE_URL + ROOT_URL
												+ LIS_TRANSACTION 
												+ SEND_TO_CR_SEARCH ;
										
										GenericService.serviceAction("POST", URI1, data).then(
												function(response) {
													$rootScope.stopLoader();
													$scope.collectedSampleList =[];
													 $scope.collectedSampleListTest=[];
														$scope.collectedSampleListTest = response.data.listObject;
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular.forEach($scope.collectedSampleListTest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.collectedSampleList.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.collectedSampleList = response.data.listObject;
														}
														$scope.collectedSampleListForFiltter = $scope.collectedSampleList; 
														$scope.filterByDummyServices();
													$scope.commonListCount =  response.data.object>0 ? response.data.object : 0 ;
													$scope.setPage(1, false);
													$rootScope.stopLoader();
												});
									} catch (e) {
										console.log(e.message)
										$rootScope.stopLoader();
									}
								
							 }else  if(searchKeyword.length == 0){
								 $scope.searchSendToCRList();
							 }
							}
						 
						 $scope.selectPatientForSave = function(){
		                	   
		                	   var checkedCount = 0;
		                       angular.forEach($scope.collectedSampleList, function(value, key) {
		                      
		                           if(value.sampleStatusId == true){
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