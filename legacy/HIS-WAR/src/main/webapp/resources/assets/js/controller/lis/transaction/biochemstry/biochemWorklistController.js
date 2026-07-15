
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"biochemistryWorklistController",
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
						'subDeptId',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,subDeptId) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.biochemWorklist = [];
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
							$scope.isAcceptanceInDepartment= labConfig.isAcceptanceInDepartment;
							$scope.subDeptId = subDeptId;
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							 $scope.saveBtn = false;
							
							$scope.visitTypeId = 1;
							$scope.selectedVisitTypeIds = [1,2];
							$scope.sampleStatusIds=[SAMPLE_ACCEPTED,RETESTING];
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							
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
								"deptId" : $scope.deptId,
								"subDeptId" : $scope.subDeptId,
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
								maxDate:new Date()
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

							/** Sample Status List  */
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
						
							
							$scope.showRejecPopup = function(index) {
								$scope.rejIndex = index;
								$("#pendingAcceptance").modal('show');
							}
							

							$scope.setNoOfRecords = function() {
								$scope.initBiochemWorkList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initBiochemWorkList = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									        "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"patientId" :  $scope.selectedPatient,
											"labSampleDtlsId" : $scope.labSampleDtlsId,
											"visitTypes" :$scope.selectedVisitType,
											"testTypes" : $scope.selectedtestList,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo,
											"subDepts" : $scope.selectedDepartMentList,
											"assignUnit" :$scope.orgUnitId 
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit,
											"deptId":$scope.deptId,
											"assignUnit" :$scope.orgUnitId ,
											"subDeptId":$scope.subDeptId,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ BIOCHEMISTRY_WORK_LIST_DEPT_SEARCH

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ BIO_CHEM_WORKLIST_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.biochemWorklist = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.biochemWorklistForFiltter = $scope.biochemWorklist;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initBiochemWorkList($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
									$scope.noOfRecordsPerPage);

				
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
								$scope.initBiochemWorkList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getBiochemWorkList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":deptId,
											"subDeptId":$scope.subDeptId,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"patientId" :  $scope.selectedPatient,
											"labSampleDtlsId" : $scope.labSampleDtlsId,
											"visitTypes" :$scope.selectedVisitType,
											"testTypes" : $scope.selectedtestList,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo,
											"subDepts" : $scope.selectedDepartMentList,
											"assignUnit" :$scope.orgUnitId 
										};
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ BIOCHEMISTRY_WORK_LIST_DEPT_SEARCH
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.biochemWorklist = response.data.listObject;
														$scope.biochemWorklistForFiltter = $scope.biochemWorklist;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							
							$scope.filterByDummyServices= function()
                            {
								$scope.biochemWorklist  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.biochemWorklist = angular.copy($scope.biochemWorklistForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.biochemWorklistForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.biochemWorklist.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.biochemWorklistForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.biochemWorklist.push(value);
                                              });
                                               
                                }
                            }
							
							
							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1 )
								{
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getBiochemWorkList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									 $scope.saveBtn = true;
								} else {
									$scope.selectedAll = false;
									 $scope.saveBtn = false;
								}
								angular
										.forEach(
												$scope.biochemWorklist,
												function(labDetailsObj) {
													labDetailsObj.sampleStatusId = $scope.selectedAll
												});
							 }

							
							
							
							
							
							
							$scope.checkSample =function()
							{
								var listLabSampleDtls = [];
								angular
								.forEach(
										$scope.biochemWorklist,
										function(value, key) 
										{
											if (value.sampleStatusId == true) 
											{
												var labSampleDtlsforAccept = angular
														.copy($scope.labSampleDtls);
												labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
												labSampleDtlsforAccept.sampleStatusId = SEND_TO_REPORT_ENTRY;
												labSampleDtlsforAccept.createdBy = $scope.userId;
												labSampleDtlsforAccept.updatedBy = $scope.userId;
												labSampleDtlsforAccept.createdDate = moment().toDate().getTime();
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
									$("#sendToResultEntry").modal('show');
								 }
							}
							
							
							
							$scope.sendToReportEntry = function() {
								
								   var found = $scope.biochemWorklist.find(ar=>ar.sampleStatusId == true && (ar.sampWrkStatusId==1||ar.sampWrkStatusId==''));
								   if(found!=undefined)
									   {
									   growl.warning("Please Change Sample Status Work in Progress.",
												{
													title : 'Warning!'
												});
									   return false;
									   }
								
								$scope.listLabSampleDtls = [];
								$rootScope.startLoader();
								angular
										.forEach(
												$scope.biochemWorklist,
												function(value, key) 
												{
													if (value.sampleStatusId == true) 
													{
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.sampleStatusId = SEND_TO_REPORT_ENTRY;
														labSampleDtlsforAccept.createdBy = $scope.userId;
														labSampleDtlsforAccept.updatedBy = $scope.userId;
														labSampleDtlsforAccept.createdDate = moment().toDate().getTime();
														$scope.listLabSampleDtls
																.push(labSampleDtlsforAccept);
													}
												});

								try {
									if($scope.listLabSampleDtls.length==0)
									{
										growl.warning("Please select sample..!!",
											{
												title : 'Error!'
											});
									   $rootScope.stopLoader();
									   return false;
									}
									$rootScope.startLoader();
									console.log("$scope.listLabSampleDtls",JSON.stringify($scope.listLabSampleDtls));
									var URI = BASE_URL + ROOT_URL
									        + LIS_BIOCHEMISTRY
											+ BIO_CHEM_SEND_TO_REPORT_ENTRY;
									console.log("SEND_TO_BIOCHEMISTY WORKLIST", URI);
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$scope.initBiochemWorkList($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.offset,
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
										+ S + orgId + S + orgUnitId ;
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
						$scope.getDepartMentList($scope.orgId,$scope.assignUnit);
						
						
						/*get Sample Number List*/
						$scope.getSampleNoList = function(searchKeyword)
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.assignUnit,
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
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.orgUnitId ,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment
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
									    	+ LIS_BIOCHEMISTRY
											+ BIOCHEMISTRY_WORK_LIST_DEPT_SEARCH
									console.log("URI", URI);
									
									var data1 = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
										};
									
								var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ BIO_CHEM_WORKLIST_COUNT;

									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI, URI2, METHOD_POST,
													METHOD_POST,searchObj,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.biochemWorklist = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.biochemWorklistForFiltter = $scope.biochemWorklist;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
													});
									
								} catch (e) {
									console.log(e.message)
								}
							}
						 
						 $scope.selectPatientForSave = function(){
		                	   
		                	   var checkedCount = 0;
		                       angular.forEach($scope.biochemWorklist, function(value, key) {
		                      
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