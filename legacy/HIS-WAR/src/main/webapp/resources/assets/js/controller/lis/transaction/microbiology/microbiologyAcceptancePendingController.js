
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"microbiologyAcceptancePendingController",
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
							$scope.microbiologyAccPendingList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							var ACCEPT = 'A';
							var REJECT = 'R';
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
						    $scope.subDeptId = labConfig.microbiologyId;
						    $scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
							$scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
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
							$scope.listLabSampleDtls = [];
							$scope.labSampleDtls = {
								"labSampleDtlsId" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"testId" : "",
								"deptId" : "",
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
								"sampWrkStatusId":"",
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

							
							

							$scope.getRejectionList = function(orgId)
							{
								try {
									var data = {
											organizationId :orgId
										};
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEREASONLIST;
									GenericService
									.serviceAction(METHOD_POST, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
													function(response) {
														$scope.rejectionList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							$scope.getRejectionList($scope.orgId);
							
							$scope.showRejecPopup = function(isAccRej,index) {
								if(isAccRej=='R')
								{
								 $scope.rejIndex = index;
								 $scope.sampleRejectReasonId = "";
								 $scope.sampleRejectReason = ""
								 $("#accepatReject").modal('show');
								}
							}
							
							
							  $scope.saveRejectDetails = function(){
									
									if($scope.sampleRejectReasonId == ""){
										
										growl.warning("Please select reason..!!",
												{
													title : 'warning!'
												});
										$scope.microbiologyAccPendingList[$scope.rejIndex].isAcceptOrReject = 'N';
										
									} else if($scope.sampleRejectReason == "")
									{
										growl.warning("Please Enter reason note..!!",
												{
													title : 'warning!'
												});
										$scope.microbiologyAccPendingList[$scope.rejIndex].isAcceptOrReject = 'N';
										
									} else {
										
										$scope.microbiologyAccPendingList[$scope.rejIndex].sampleRejectReasonId = $scope.sampleRejectReasonId;
										$scope.microbiologyAccPendingList[$scope.rejIndex].sampleRejectReason = $scope.sampleRejectReason;
										
										 $scope.sampleRejectReasonId = "";
										 $scope.sampleRejectReason = "";  
									}
									
								}
								
								$scope.cancelRejectDetails = function(){
									
									$scope.microbiologyAccPendingList[$scope.rejIndex].isAcceptOrReject = 'N';
								}

							
							

							$scope.setNoOfRecords = function() {
								$scope.initmicrobiologyAccPendingList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initmicrobiologyAccPendingList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL + ROOT_URL
											+ LIS_MICROBIOLOGY
											+ MICROBIOLOGY_ACCEPTANCE_PENDING;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_MICROBIOLOGY
											+ MICROBIOLOGY_ACCEPTANCE_PENDING_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.microbiologyAccPendingList = response[0].listObject;
														$scope.microbiologyAccPendingListForFiltter = $scope.microbiologyAccPendingList ;
														$scope.filterByDummyServices();
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initmicrobiologyAccPendingList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);

							$scope.filterByDummyServices= function()
                            {
								$scope.microbiologyAccPendingList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.microbiologyAccPendingList = angular.copy($scope.microbiologyAccPendingListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.microbiologyAccPendingListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.microbiologyAccPendingList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.microbiologyAccPendingListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.microbiologyAccPendingList.push(value);
                                              });
                                               
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
								$scope.selectedSampleNo='';
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
							    $scope.initmicrobiologyAccPendingList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getMicrobiologyAccPendingList = function(orgId,
									orgUnitId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ MICROBIOLOGY_ACCEPTANCE_PENDING;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.microbiologyAccPendingList = response.data.listObject;
														$scope.microbiologyAccPendingListForFiltter = $scope.microbiologyAccPendingList ;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getMicrobiologyAccPendingList(
											$scope.orgId, $scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									$scope.value = 'A';
								} else {
									$scope.selectedAll = false;
									$scope.value = 'N';
								}
								angular
										.forEach(
												$scope.microbiologyAccPendingList,
												function(labDetailsObj) {
													labDetailsObj.isAcceptOrReject = $scope.value
												});
							 }

							$scope.checkSample = function()
							{
								listLabSampleDtls = [];
								angular
										.forEach(
												$scope.microbiologyAccPendingList,
												function(value, key) 
												{
													if (value.isAcceptOrReject ==  'A') 
													{
													
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.createdBy = $scope.userId;
														labSampleDtlsforAccept.currStatus = value.isAcceptOrReject;
														listLabSampleDtls
																.push(labSampleDtlsforAccept);
													}
													if (value.isAcceptOrReject == 'R') 
													{
														
														var labSampleDtlsforReject = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforReject.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforReject.createdBy = $scope.userId;
														labSampleDtlsforReject.currStatus = value.isAcceptOrReject;
														labSampleDtlsforReject.sampleRejectReasonId = value.sampleRejectReasonId;
														labSampleDtlsforReject.sampleRejectReason = value.sampleRejectReason;
														labSampleDtlsforReject.sampleRejectBy =$scope.userId;
														listLabSampleDtls
																.push(labSampleDtlsforReject);
													}
												});
								
								if(listLabSampleDtls.length==0)
								{
									growl.warning('Please perform action on at least one sample.',
											{
												title : 'Warning!'
											});
									  return false;
								}
								else{
									$("#sendToMicroList").modal('show');
								 }
							}
							
							
							$scope.acceptRejectMicrobiologyWorklist = function() {
								$scope.listLabSampleDtls = [];
								$rootScope.startLoader();
								angular
										.forEach(
												$scope.microbiologyAccPendingList,
												function(value, key) 
												{
													if (value.isAcceptOrReject ==  'A') 
													{
													
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.createdBy = $scope.userId;
														labSampleDtlsforAccept.currStatus = value.isAcceptOrReject;
														$scope.listLabSampleDtls
																.push(labSampleDtlsforAccept);
													}
													if (value.isAcceptOrReject == 'R') 
													{
														
														var labSampleDtlsforReject = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforReject.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforReject.createdBy = $scope.userId;
														labSampleDtlsforReject.currStatus = value.isAcceptOrReject;
														labSampleDtlsforReject.sampleRejectReasonId = value.sampleRejectReasonId;
														labSampleDtlsforReject.sampleRejectReason = value.sampleRejectReason;
														labSampleDtlsforReject.sampleRejectBy =$scope.userId;
														$scope.listLabSampleDtls
																.push(labSampleDtlsforReject);
													}
												});

								try {
									var URI = BASE_URL + ROOT_URL
									        + LIS_MICROBIOLOGY
											+ MICROBIOLOGY_SMP_ACCEPT_REJECT;
									console.log("SEND_TO_Microbiology WORKLIST", URI);
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$scope.initmicrobiologyAccPendingList(
																			$scope.orgId,
																			$scope.orgUnitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
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
							
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
						    
							$scope.visitTypeId = 1;
							$scope.selectedVisitTypeIds = [1,2];
							$scope.sampleStatusIds=[SAMPLE_COLLECTED];
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
						$scope.getSendToCRList = function(searchKeyword)
						{
						   	if(searchKeyword.length == 3)
							{
							try 
							{
								var data = { 
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"visitTypes":$scope.selectedVisitTypeIds,
										"sampleStatusIds":$scope.sampleStatusIds,
										"searchKeyword":searchKeyword,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId,
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ BIOCHECMISTRY_PATIENT_LIST_SEARCH;
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
										"visitTypes":$scope.selectedVisitTypeIds,
										"sampleStatusIds":$scope.sampleStatusIds,
										"searchKeyword":searchKeyword,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId,
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
								    	+ LIS_BIOCHEMISTRY
										+ BIOCHECMISTRY_SAMPLE_NO_LIST_SEARCH
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
						 $scope.searchMicroBiology = function()
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
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.orgUnitId,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
												 
												
		                             }
									
									var isValidDate = $rootScope.isValidDate(searchObj.fromDate,searchObj.toDate);
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
									    	+ LIS_MICROBIOLOGY
											+ SEARCH_MICROBIOLOGY_DEPT_SEARCH
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 searchObj).then(
													 function(response) {
														 $scope.sampleAccPendingList=[];
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																$scope.microbiologyAccPendingList =  response.data.listObject;
																$scope.microbiologyAccPendingListForFiltter = $scope.microbiologyAccPendingList ;
																$scope.filterByDummyServices();
															} else if (response.data.status == 'error') {
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
																$scope.microbiologyAccPendingList=[];
															}
														});
								} catch (e) {
									console.log(e.message)
								}
							}	

						} ]);