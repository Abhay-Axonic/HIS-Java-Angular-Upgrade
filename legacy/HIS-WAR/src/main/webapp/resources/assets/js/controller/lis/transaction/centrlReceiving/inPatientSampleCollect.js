/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"inPatientController",
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
								GenericService, BillingGenericService , PagerService, promiseFactoryWithObject,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.inPatientSampleList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							 $scope.saveBtn = false;
							
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.isSendToCrApplicable = labConfig.isSendToCrApplicable;
							
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							$scope.visitTypeId = 2;
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
								"assignUnit":"",
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
								$scope.initSampleListInPatient($scope.orgId,
										$scope.orgUnitId, IP, $scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initSampleListInPatient = function(orgId,
									orgUnitId, visitTypeId, offset,
									noOfRecordsPerPage) {

								try {
									$rootScope.startLoader();
									var data = {
											 "orgId" : $scope.orgId,
											 "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId ,
											  "fromDt":moment($scope.fromDate).format('YYYY-MM-DD'),
							                  "toDt":moment($scope.toDate).format('YYYY-MM-DD'),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "visitTypeId" : $scope.visitTypeId,
											  "recordPerPage":$scope.noOfRecordsPerPage,
											  "isSendToCrApplicable" : $scope.isSendToCrApplicable,
											  "offset":$scope.offset,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo
									};
									var URI1 = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ OUT_PATIENT_LIST_SEARCH ;
									
									var data2 = {
											 "orgId" : $scope.orgId,
											 "orgUnitId" :$scope.assignUnit,
											 "assignUnit" :$scope.orgUnitId ,
											 "fromDt":moment($scope.fromDate).format('YYYY-MM-DD'),
							                 "toDt":moment($scope.toDate).format('YYYY-MM-DD'),
					                         "isSendToCrApplicable" : $scope.isSendToCrApplicable,
					                         "visitTypes" :$scope.selectedVisitType,
					                         "visitTypeId" : $scope.visitTypeId,
									};
									
									var URI2 = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ GET_TOTAL_RECORD_INPATIENT ; 

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject.setPromisesWithObject(URI1, URI2, "POST",
													"POST",data,data2)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.inPatientSampleList=[];
														$scope.inPatientSampleListTest=[];
														$rootScope.stopLoader();
														$scope.inPatientSampleListTest = response[0].listObject;
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular .forEach($scope.inPatientSampleListTest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.inPatientSampleList.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.inPatientSampleList = response[0].listObject;
														}
														
														$scope.inPatientSampleListForFiltter = $scope.inPatientSampleList;
														$scope.filterByDummyServices();
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}
							
							
							$scope.filterByDummyServices= function()
                            {
								$scope.inPatientSampleList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.inPatientSampleList = angular.copy($scope.inPatientSampleListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.inPatientSampleListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.inPatientSampleList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.inPatientSampleListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.inPatientSampleList.push(value);
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
								$scope.initSampleListInPatient($scope.orgId,
										$scope.orgUnitId, IP, $scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getRejectionList = function(orgId) {
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
							
							$scope.showRejecPopup = function(isAccRej,index)
                              {
								if(isAccRej=='R')
								{
								  $scope.rejIndex = index;
								  $scope.sampleRejectReasonId = "";
									 $scope.sampleRejectReason = ""
								  $("#accptRejInPatient").modal('show');
							    }
							}

							   $scope.saveRejectDetails = function(){
									
									if($scope.sampleRejectReasonId == ""){
										
										growl.warning("Please select reason..!!",
												{
													title : 'warning!'
												});
										$scope.inPatientSampleList[$scope.rejIndex].isAcceptOrReject = 'N';
										
									} else if($scope.sampleRejectReason == "")
									{
										growl.warning("Please Enter reason note..!!",
												{
													title : 'warning!'
												});
										$scope.inPatientSampleList[$scope.rejIndex].isAcceptOrReject = 'N';
										
									} else {
										
										$scope.inPatientSampleList[$scope.rejIndex].sampleRejectReasonId = $scope.sampleRejectReasonId;
										$scope.inPatientSampleList[$scope.rejIndex].sampleRejectReason = $scope.sampleRejectReason;
										
										 $scope.sampleRejectReasonId = "";
										 $scope.sampleRejectReason = "";  
									}
									$scope.selectPatientForSave();
								}
								
								$scope.cancelRejectDetails = function(){
									
									$scope.inPatientSampleList[$scope.rejIndex].isAcceptOrReject = 'N';
									$scope.selectPatientForSave();
								}
							
							$scope.getSampleListInPatient = function(orgId,
									orgUnitId, visitTypeId, offset,
									noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
									"orgId" : $scope.orgId,
									"orgUnitId" :$scope.assignUnit,
									"assignUnit" :$scope.orgUnitId ,
									"fromDt":moment($scope.fromDate).format('YYYY-MM-DD'),
					                "toDt":moment($scope.toDate).format('YYYY-MM-DD'),
									"patientId" :  $scope.selectedPatient,
									"labSampleDtlsId" : $scope.labSampleDtlsId,
									"visitTypes" :$scope.selectedVisitType,
									"testTypes" : $scope.selectedtestList,
									"deptId" : $scope.deptId,
									"subDepts" : $scope.selectedDepartMentList,
									"visitTypeId" : $scope.visitTypeId,
									"recordPerPage":noOfRecordsPerPage,
									"isSendToCrApplicable" : $scope.isSendToCrApplicable,
									"offset":offset,
									"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
									"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo
									};
									var URI = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ OUT_PATIENT_LIST_SEARCH ;

									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.inPatientSampleList = response.data.listObject;
														$scope.inPatientSampleListForFiltter = $scope.inPatientSampleList;
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
									$scope.getSampleListInPatient($scope.orgId,
											$scope.orgUnitId, IP,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initSampleListInPatient($scope.orgId,
									$scope.orgUnitId, IP, $scope.offset,
									$scope.noOfRecordsPerPage);
							
							$scope.checkSample =function()
							{
								var listLabSampleDtls = [];
								angular
										.forEach(
												$scope.inPatientSampleList,
												function(value, key) 
												{
													if (value.isAcceptOrReject == 'A') 
													{
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.sampleAcceptBy = $scope.userId;
														labSampleDtlsforAccept.currStatus = value.isAcceptOrReject;
														labSampleDtlsforAccept.samplePendingCount = value.samplePendingCount;
														labSampleDtlsforAccept.isCentrifugationReq = value.isCentrifugationReq;
														labSampleDtlsforAccept.updatedBy = $scope.userId;
														listLabSampleDtls.push(labSampleDtlsforAccept);
													}
													if (value.isAcceptOrReject == 'R') {
														var labSampleDtlsforReject = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforReject.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforReject.currStatus = value.isAcceptOrReject;
														labSampleDtlsforReject.sampleRejectReasonId = value.sampleRejectReasonId;
														labSampleDtlsforReject.sampleRejectReason = value.sampleRejectReason;
														labSampleDtlsforReject.sampleRejectBy = $scope.userId;
														labSampleDtlsforReject.sampleRejectBy = $scope.userId;
														labSampleDtlsforReject.updatedBy = $scope.userId;
														listLabSampleDtls.push(labSampleDtlsforReject);
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
									$("#inPatient").modal('show');
								 }
							}
							
							
							$scope.collectSampleForInPatient = function() {
								$scope.listLabSampleDtls = [];
								$rootScope.startLoader();
								angular
										.forEach(
												$scope.inPatientSampleList,
												function(value, key) {
													if (value.isAcceptOrReject == 'A') {
														var labSampleDtlsforAccept = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.sampleAcceptBy = $scope.userId;
														labSampleDtlsforAccept.currStatus = value.isAcceptOrReject;
														labSampleDtlsforAccept.samplePendingCount = value.samplePendingCount;
														labSampleDtlsforAccept.isCentrifugationReq = value.isCentrifugationReq;
														labSampleDtlsforAccept.updatedBy = $scope.userId;
														$scope.listLabSampleDtls
																.push(labSampleDtlsforAccept);
													}
													if (value.isAcceptOrReject == 'R') {
														var labSampleDtlsforReject = angular
																.copy($scope.labSampleDtls);
														labSampleDtlsforReject.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforReject.currStatus = value.isAcceptOrReject;
														labSampleDtlsforReject.sampleRejectReasonId = value.sampleRejectReasonId;
														labSampleDtlsforReject.sampleRejectReason = value.sampleRejectReason;
														labSampleDtlsforReject.sampleRejectBy = $scope.userId;
														labSampleDtlsforReject.updatedBy = $scope.userId;
														$scope.listLabSampleDtls.push(labSampleDtlsforReject);
													}
												});
								
								if($scope.listLabSampleDtls.length==0)
								{
									$rootScope.stopLoader();
									growl
									.error(
											"Please select sample.",
											{
												title : 'Error!'
											});
									return false;
								}

								var URI = BASE_URL + ROOT_URL
										+ LIS_CENTRAL_RECEIVING
										+ ACCPET_REJECT_SAMPLE_FOR_IN_OUT;

								try {
									console
											.log("$scope.listLabSampleDtls",
													JSON
															.stringify($scope.listLabSampleDtls));
									var URI = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ ACCPET_REJECT_SAMPLE_FOR_IN_OUT;

									console.log("ACCEPT_REJECT_SAMPLE", URI);
									GenericService
											.serviceAction("POST", URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$scope
																	.initSampleListInPatient(
																			$scope.orgId,
																			$scope.orgUnitId,
																			IP,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															growl
																	.success(
																			response.data.message,
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
												$scope.inPatientSampleList,
												function(labDetailsObj) {
													labDetailsObj.isAcceptOrReject = $scope.value;
												});
								 $scope.selectPatientForSave();
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
										"sampleStatusId":SAMPLE_SEND_TO_CR,
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
						} else {
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
										if(multiselectObj.label!='OP'){
											$scope.visityTypeList.push(multiselectObj);
										}
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
											  if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
												  angular.forEach($scope.staffSubSpecialityList, function(value, key) {
														var multiselectObj = angular.copy($scope.multiselectObj);
														multiselectObj.id = value.subSpecialityMasterId;
														multiselectObj.label = 	value.subSpecialityMasterName;
															$scope.departmentList.push(multiselectObj);
														});
											  }else{
												  angular.forEach(response.data.listObject, function(value, key) {
														var multiselectObj = angular.copy($scope.multiselectObj);
														multiselectObj.id = value.id;
														multiselectObj.label = 	value.name;
															$scope.departmentList.push(multiselectObj);
														});
							                      }
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
										"sampleStatusId":SAMPLE_SEND_TO_CR,
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
												  "assignUnit" :$scope.orgUnitId ,
												  "fromDt":moment($scope.fromDate).format('YYYY-MM-DD'),
								                  "toDt":moment($scope.toDate).format('YYYY-MM-DD'),
												  "patientId" :  $scope.selectedPatient,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "visitTypeId" : $scope.visitTypeId,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "offset":$scope.offset,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo,
												  "isSendToCrApplicable" : $scope.isSendToCrApplicable
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
									
									console.log("$scope.selectedtestList",$scope.selectedtestList);
									console.log("$scope.selectedDepartMentList",$scope.selectedDepartMentList);
									console.log("collectedSampleList",JSON.stringify(searchObj));
									var URI = BASE_URL 
									        + ROOT_URL
									        + LIS_CENTRAL_RECEIVING
											+ OUT_PATIENT_LIST_SEARCH
									console.log("URI", URI);
									
									var data2 = {
											 "orgId" : $scope.orgId,
											 "orgUnitId" :$scope.assignUnit,
											 "assignUnit" :$scope.orgUnitId ,
											 "fromDt":moment($scope.fromDate).format('YYYY-MM-DD'),
							                 "toDt":moment($scope.toDate).format('YYYY-MM-DD'),
					                         "isSendToCrApplicable" : $scope.isSendToCrApplicable,
					                         "visitTypes" :$scope.selectedVisitType
									};
									
									var URI2 = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ GET_TOTAL_RECORD_INPATIENT ; 

									console.log("URI2", URI2);
									promiseFactoryWithObject.setPromisesWithObject(URI, URI2, "POST",
													"POST",searchObj,data2)
											.then(
													function(response) {
														$scope.inPatientSampleList=[];
														$scope.inPatientSampleListTest=[];
														$rootScope.stopLoader();
														$scope.inPatientSampleListTest = response[0].listObject;
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular .forEach($scope.inPatientSampleListTest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.inPatientSampleList.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.inPatientSampleList = response[0].listObject;
														}
														$scope.inPatientSampleListForFiltter = $scope.inPatientSampleList;
														$scope.filterByDummyServices();
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
													});
									
								} catch (e) {
									console.log(e.message)
								}
							}

						 $scope.selectPatientForSave = function(){
		                	   
		                	   var checkedCount = 0;
		                       angular.forEach($scope.inPatientSampleList, function(value, key) {
		                      
		                           if(value.isAcceptOrReject != 'N'  && value.isAcceptOrReject != null ){
		                                checkedCount = checkedCount + 1;
		                           }
		                      });
		                       
		                       if(checkedCount > 0){
		                           $scope.saveBtn = true;
		                       }else{
		                           $scope.saveBtn = false;
		                       }
		                   }
						 
						 $scope.getClassForColor = function(info)
		                    {
		                        	   if(info.priorityId==2){
                              		return 'td-bg-red';
		                        	   }
		                    }	
						} ]);