angular
		.module('myApp')
		.controller(
				"OutPatientController",
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
						'$filter',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, BillingGenericService ,PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$filter) {

							$rootScope.loginpage = true;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.sampleListForOutpatient = [];
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
							$scope.assignUnit = $scope.orgUnitId;
							$scope.isSendToCrApplicable = labConfig.isSendToCrApplicable;
							$scope.getUnitByOrganization();
							$scope.visitTypeId = 1;
							
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							$scope.saveBtn = false;
							

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
								$scope.initOutInPatientSampleList($scope.orgId,
										$scope.orgUnitId, OP, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initOutInPatientSampleList = function(orgId,
									orgUnitId, visitTypeId, offset,
									noOfRecordsPerPage) {

								try {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 50;
									var data1 = 
									{
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "patientId" :  $scope.selectedPatient,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.orgUnitId,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "recordPerPage":noOfRecordsPerPage,
												  "offset":offset,
												  "visitTypeId" : $scope.visitTypeId,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo
		                             };
									
									var URI = BASE_URL 
									        + ROOT_URL
									        + LIS_CENTRAL_RECEIVING
											+ OUT_PATIENT_LIST_SEARCH
									console.log("URI", URI);
								
									var data2 = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "isSendToCrApplicable":$scope.isSendToCrApplicable,
											  "visitTypeId" : $scope.visitTypeId
									};
									
									var URI2 = BASE_URL + ROOT_URL
									+ LIS_CENTRAL_RECEIVING
									+ GET_TOTAL_RECORD_OUTPATIENT 

							console.log("URI2", URI2);
							promiseFactoryWithObject.setPromisesWithObject(URI, URI2, "POST","POST",data1,data2)
									.then(
											function(response) 
											{
												$rootScope.stopLoader();
												$scope.sampleListForOutpatient=[];
												$scope.sampleListForOutpatientTest=[];
												$rootScope.stopLoader();
												$scope.sampleListForOutpatientTest = response[0].listObject;
												 
												if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
													angular .forEach($scope.sampleListForOutpatientTest, function(value, key) {
														angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
															if(value.subDeptId == value1.subSpecialityMasterId){
																$scope.sampleListForOutpatient.push(value);
															}
														
														});		
													 });
												}else{
													$scope.sampleListForOutpatient = response[0].listObject;
												}
												$scope.commonListCount = response[1].object;
												$scope.setPage(1,false);
											});
									
								} catch (e) {
									console.log(e.message);
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
							    $scope.initOutInPatientSampleList($scope.orgId,
										$scope.orgUnitId, OP, $scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getRejectionList = function(orgId) {
								try {

									var data = {
											organizationId :orgId
										};
									/*var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ GET_REJECTED_REASON_LIST + S
											+ orgId;*/
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
									 $("#accptRej").modal('show');
									}
							}

                             $scope.saveRejectDetails = function(){
								
								if($scope.sampleRejectReasonId == ""){
									
									growl.warning("Please select reason..!!",
											{
												title : 'warning!'
											});
									$scope.sampleListForOutpatient[$scope.rejIndex].isAcceptOrReject = 'N';
									
								} else if($scope.sampleRejectReason == "")
								{
									growl.warning("Please Enter reason note..!!",
											{
												title : 'warning!'
											});
									$scope.sampleListForOutpatient[$scope.rejIndex].isAcceptOrReject = 'N';
									
								} else {
									
									$scope.sampleListForOutpatient[$scope.rejIndex].sampleRejectReasonId = $scope.sampleRejectReasonId;
									$scope.sampleListForOutpatient[$scope.rejIndex].sampleRejectReason = $scope.sampleRejectReason;
									
									 $scope.sampleRejectReasonId = "";
									 $scope.sampleRejectReason = "";  
								}
								$scope.selectPatientForSave();
							}
							
							$scope.cancelRejectDetails = function(){
								
								$scope.sampleListForOutpatient[$scope.rejIndex].isAcceptOrReject = 'N';
								$scope.selectPatientForSave();
							}

							

							$scope.getOutInPatientSampleList = function(orgId,
									orgUnitId, visitTypeId, offset,
									noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "assignUnit" :$scope.orgUnitId,
											  "isSendToCrApplicable":$scope.isSendToCrApplicable,
											  "recordPerPage":noOfRecordsPerPage,
											  "offset":offset,
											  "visitTypeId" : $scope.visitTypeId,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo
									};
									var URI = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ OUT_PATIENT_LIST_SEARCH;

									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.sampleListForOutpatient = response.data.listObject;
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
									$scope.getOutInPatientSampleList(
											$scope.orgId, $scope.orgUnitId, OP,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initOutInPatientSampleList($scope.orgId,
									$scope.orgUnitId, OP, $scope.offset,
									$scope.noOfRecordsPerPage);
							
							
							$scope.checkSample =function()
							{
								var listLabSampleDtls = [];
								angular
										.forEach(
												$scope.sampleListForOutpatient,
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
									$("#outPatient").modal('show');
								 }
							}
							

							$scope.collectSampleForOutPatient = function() {
								$scope.listLabSampleDtls = [];
								$rootScope.startLoader();
								angular
										.forEach(
												$scope.sampleListForOutpatient,
												function(value, key) {
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
														$scope.listLabSampleDtls.push(labSampleDtlsforAccept);
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
									GenericService
											.serviceAction("POST", URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$scope.initOutInPatientSampleList(
																			$scope.orgId,
																			$scope.orgUnitId,
																			OP,
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
									$scope.sampleListForOutpatient = $filter('filter')($scope.sampleListForOutpatient, {
			                                $: $scope.searchKeyword
			                            });
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									$scope.value = 'A';
								} else {
									$scope.selectedAll = false;
									$scope.value = 'N';
								}
								angular
										.forEach(
												$scope.sampleListForOutpatient,
												function(labDetailsObj) {
													labDetailsObj.isAcceptOrReject = $scope.value;
												});
								 $scope.selectPatientForSave();
							}
							
							$scope.searchResult = function(){
								if($scope.searchKeyword ==""){
									$scope.selectedAll =false;
									$scope.searchOutPatientList();
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
											}	 else {
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
														var searchObj = 
														{
															"orgId" : $scope.orgId,
															"orgUnitId" :$scope.assignUnit,
															"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
															"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
															"patientId" :  $scope.selectedPatient,
															"labSampleDtlsId" : $scope.labSampleDtlsId,
															"testTypes" : $scope.selectedtestList,
															"deptId" : $scope.deptId,
															"subDepts" : $scope.selectedDepartMentList,
															"assignUnit" :$scope.orgUnitId,
															"isSendToCrApplicable":$scope.isSendToCrApplicable,
															"recordPerPage":$scope.noOfRecordsPerPage,
															"offset":$scope.offset,
															"visitTypeId" : $scope.visitTypeId,
															"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
															"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo == null)?"":$scope.selectedSampleNo
							                             };
														
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
																+ OUT_PATIENT_LIST_SEARCH;
														console.log("URI", URI);
														
														var data2 = {
                                                            "orgId" : $scope.orgId,
                                                            "orgUnitId" :$scope.assignUnit,
                                                            "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
                                                            "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
                                                            "isSendToCrApplicable":$scope.isSendToCrApplicable,
                                                            "visitTypeId" : $scope.visitTypeId,
                                                            "assignUnit" :$scope.orgUnitId
														};
														
														var URI2 = BASE_URL + ROOT_URL
														+ LIS_CENTRAL_RECEIVING
														+ GET_TOTAL_RECORD_OUTPATIENT ;

												console.log("URI2", URI2);
												promiseFactoryWithObject.setPromisesWithObject(URI, URI2, "POST","POST",searchObj,data2)
														.then(
																function(response) 
																{
																	$scope.sampleListForOutpatient=[];
																	$scope.sampleListForOutpatientTest=[];
																	$rootScope.stopLoader();
																	$scope.sampleListForOutpatientTest = response[0].listObject;
																	 
																	if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
																		angular .forEach($scope.sampleListForOutpatientTest, function(value, key) {
																			angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																				if(value.subDeptId == value1.subSpecialityMasterId){
																					$scope.sampleListForOutpatient.push(value);
																				}
																			
																			});		
																		 });
																	}else{
																		$scope.sampleListForOutpatient = response[0].listObject;
																	}
								
																	$scope.commonListCount = response[1].object;
																	$scope.setPage(1,false);
																	$scope.setNoOfRecords();
																});
														/* GenericService.serviceAction("POST", URI,
																 searchObj).then(
																		 function(response) {
																			 $scope.sampleListForOutpatient=[];
																				if (response.data.status == 'success') {
																					$rootScope.stopLoader();
																					
																					growl.success(response.data.message,
																									{
																										title : 'Success!'
																									});
																					
																					console.log("collectedSampleList",JSON.stringify(response.data.listObject));
																					$scope.sampleListForOutpatient =  response.data.listObject;
					
																				} else if (response.data.status == 'error') {
																					growl
																							.error(
																									response.data.message,
																									{
																										title : 'Error!'
																									});
																					$scope.sampleListForOutpatient=[];
																				}
																			});*/
													} catch (e) {
														console.log(e.message)
													}

												}
											 
											 $scope.selectPatientForSave = function(){
							                	   
							                	   var checkedCount = 0;
							                       angular.forEach($scope.sampleListForOutpatient, function(value, key) {
							                      
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