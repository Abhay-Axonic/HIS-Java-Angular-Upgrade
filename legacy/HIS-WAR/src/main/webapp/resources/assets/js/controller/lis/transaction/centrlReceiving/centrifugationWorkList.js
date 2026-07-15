/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"centrifugationWorklistController",
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
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.centrifugationWorklist = [];
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
							$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
							$scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							
							$scope.visitTypeId = 1;
							
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];

							
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
								"sampWrkStatusId":"1",
								"isOutsourced" : "",
								"outsourcedLabId" : "",
								"sampleSendtocrDatetime" : "",
								"sampleSendtocrBy" : "",
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
								"listTMicroSampleMediaDto":[]	
							}


							/**DatePicker Code  */
						
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
							
					
					    
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

							$scope.setNoOfRecords = function() {
								$scope.initCentrifugationWorkList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initCentrifugationWorkList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data1 = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "visitTypeId" : $scope.visitTypeId,
											  "assignUnit" :$scope.orgUnitId ,
											  "isSendToCrApplicable":$scope.isSendToCrApplicable,
											  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											  "offset":$scope.offset,
											  "recordPerPage":$scope.noOfRecordsPerPage,
									};
									var URI1 = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ CENTRIFUGATION_WORK_LIST_SEARCH;
											//+ CENTRIFUGATION_WORKLIST + S
											//+ orgId + S + orgUnitId + S +$scope.deptId+S
											//+ offset + S + noOfRecordsPerPage;

									var data2 = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
									}
									var URI2 = BASE_URL
											+ ROOT_URL
											+ "/LIS/centralReceiving/getTotalcentrifugationWorkListCount";


									console.log("URI1", URI1);
									console.log("URI2", URI2);
									
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, "POST",
													"POST",data1,data2)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.centrifugationWorklist = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initCentrifugationWorkList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);

							$scope.getSampleStatusList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ GET_SAMPLE_STAUS_LIST + S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response)
													{
														$scope.sampleStatusList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getSampleStatusList($scope.orgId);

							$scope.getCentrifugationWorkList = function(orgId,
									orgUnitId,offset,noOfRecordsPerPage) { 
								try {
									$rootScope.startLoader();
									var data1 = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "visitTypeId" : $scope.visitTypeId,
											  "assignUnit" :$scope.orgUnitId ,
											  "isSendToCrApplicable":$scope.isSendToCrApplicable,
											  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											  "offset":offset,
											  "recordPerPage":noOfRecordsPerPage,
									};
									
									var URI = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ CENTRIFUGATION_WORK_LIST_SEARCH;
											//+ CENTRIFUGATION_WORKLIST + S
											//+ orgId + S + orgUnitId +$scope.offset+ S
											//$scope.noOfRecordsPerPage;
									console.log("URI", URI);

									GenericService
											.serviceAction("POST", URI, data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.centrifugationWorklist = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getCentrifugationWorkList(
											$scope.orgId, $scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									$scope.sendVal = 'Y';
								} else {
									$scope.selectedAll = false;
									$scope.sendVal = 'N';
								}
								angular
										.forEach(
												$scope.centrifugationWorklist,
												function(labDetailsObj) {
													labDetailsObj.sendToDept =$scope.sendVal;
												});
							 }

							
							
							
							
							
							
							
							
							
							$scope.checkSample =function()
							{
								var listLabSampleDtls = [];
								angular
										.forEach(
												$scope.centrifugationWorklist,
												function(value, key) 
												{
													if (value.isSelected == true) 
													{
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.createdBy = $scope.userId;
														labSampleDtlsforAccept.updatedBy = $scope.userId;
														labSampleDtlsforAccept.sampleStatusId = SEND_TO_DEPT;
														labSampleDtlsforAccept.sampWrkStatusId = value.sampWrkStatusId
														listLabSampleDtls
																.push(labSampleDtlsforAccept);
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
									$("#sendToDept").modal('show');
								 }
							}
							
							
							
							$scope.sendToDepartment = function() 
							{
								$scope.listLabSampleDtls = [];
								angular
										.forEach(
												$scope.centrifugationWorklist,
												function(value, key) 
												{
													if (value.isSelected == true) 
													{
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.createdBy = $scope.userId;
														labSampleDtlsforAccept.updatedBy = $scope.userId;
														labSampleDtlsforAccept.sampleStatusId = SEND_TO_DEPT;
														labSampleDtlsforAccept.sampWrkStatusId = value.sampWrkStatusId
														$scope.listLabSampleDtls
																.push(labSampleDtlsforAccept);
													}
												});

								try {
									
									if($scope.listLabSampleDtls.length==0)
									{
										growl.warning("Please select sample..!!",
											{
												title : 'Warning!'
											});
									   $rootScope.stopLoader();
									   return false;
									}
									
									var found = $scope.centrifugationWorklist.find(ar=>ar.isSelected == true && (ar.sampWrkStatusId==1||ar.sampWrkStatusId==''));
									   if(found!=undefined)
										   {
										   growl.warning("Please Change Sample Status Work in Progress.",
													{
														title : 'Warning!'
													});
										   return false;
										   }
									$rootScope.startLoader();
									console.log("$scope.listLabSampleDtls",JSON.stringify($scope.listLabSampleDtls));
									var URI = BASE_URL + ROOT_URL
									        + LIS_CENTRAL_RECEIVING
											+ SEND_TO_DEPARMENT;
									console.log("SEND_TO_DEPARTMENT", URI);
									GenericService
											.serviceAction("POST", URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$scope.initCentrifugationWorkList($scope.orgId,
																	$scope.orgUnitId, $scope.offset,
																	$scope.noOfRecordsPerPage);;
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.saveBtn =false;
															$scope
																	.$broadcast('show-errors-reset');

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
										"sampleStatusId":OP_IP_ACCEPTED,
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
						
						
						/*get Sample Number List*/
						$scope.getSampleNoList = function(searchKeyword)
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"sampleStatusId":OP_IP_ACCEPTED,
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
						 $scope.searchOutPatientList = function()
							{
								try 
								{
									$rootScope.startLoader();
									
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "patientId" :  $scope.selectedPatient,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "visitTypeId" : $scope.visitTypeId,
												  "assignUnit" :$scope.orgUnitId ,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage,
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
									        + LIS_CENTRAL_RECEIVING
											+ CENTRIFUGATION_WORK_LIST_SEARCH;
									console.log("URI", URI);
									
									/*var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ GET_TOTAL_CENTRIFUGATIONWORKLIST_RECORD
											+ S + orgId + S + orgUnitId+ S +$scope.deptId
											+ S + $scope.fromDt + S + $scope.toDt;*/
									
									var data2 = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
									}
									var URI2 = BASE_URL
											+ ROOT_URL 
											+ "/LIS/centralReceiving/getTotalcentrifugationWorkListCount";

									console.log("URI2", URI2);
									
									promiseFactoryWithObject
											.setPromisesWithObject(URI, URI2, "POST",
													"POST",searchObj,data2)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.centrifugationWorklist = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
													});
									
									/* GenericService.serviceAction("POST", URI,
											 searchObj).then(
													 function(response) {
														 $scope.centrifugationWorklist=[];
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																$scope.centrifugationWorklist=  response.data.listObject;
																$rootScope.stopLoader();

															} else if (response.data.status == 'error') {
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
																$scope.centrifugationWorklist=[];
															}
														});*/
								} catch (e) {
									console.log(e.message)
								}
							}
						 
							$scope.clearFields = function()
							{
								$scope.patientId = undefined;
								$scope.selectedPatient = null;
								$scope.selectedVisitType = [];
								$scope.orderStatusId = '';
								$scope.priorityId = '';
								$scope.uhid = '';
								$scope.selectedSampleNo="";
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
								$scope.initCentrifugationWorkList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.selectedAll = false;
							 $scope.saveBtn = false;
							/* code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									 $scope.saveBtn = true;
								} else {
									$scope.selectedAll =false;
									 $scope.saveBtn = false;
								}
								angular.forEach($scope.centrifugationWorklist,function(detailsObj) {
									detailsObj.isSelected = $scope.selectedAll
												});
							 }
							
							//$scope.sampWrkStatusIdFlag = false;
							 $scope.selectPatientForSave = function(){
			                	  
			                	   var checkedCount = 0;
			                	   var checkAll = 0;
			                       angular.forEach($scope.centrifugationWorklist, function(value, key) {
			                      
			                    	  
			                           if(value.isSelected){
			                                checkedCount = checkedCount + 1;
			                           }
			                           
			                           if(!value.isSelected){
			                        	   checkAll = checkAll + 1;
			                           }
			                           
			                      });
			                       
			                      /* if($scope.sampWrkStatusIdFlag == true){
			                    		$scope.sampWrkStatusIdFlag = false;
			                    	   growl.warning('Please select sample status.',
												{
													title : 'Warning!'
												});
										  return false;

			                       }*/
			                       if(checkedCount > 0){
			                           $scope.saveBtn = true;
			                       }else{
			                           $scope.saveBtn = false;
			                       }
			                       
			                       if(checkAll > 0){
			                    	   $scope.selectedAll = false;
			                       }
			                       
			                   }

							 $scope.getClassForColor = function(info)
			                    {
			                        	   if(info.priorityId==2){
	                                 		return 'td-bg-red';
			                        	   }
			                    }	
						} ]);
