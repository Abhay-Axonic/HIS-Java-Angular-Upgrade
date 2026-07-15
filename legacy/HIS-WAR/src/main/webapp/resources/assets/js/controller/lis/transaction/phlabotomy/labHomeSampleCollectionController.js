/**
 * @author 
 */
'use strict';
angular
		.module('myApp')
		.controller(
				"labHomeSampleCollectionController",
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
						'$interval',
						'$window',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$interval,$window) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.phlebotomyWorkList = [];
							$scope.phlebotomyWorkListDetails = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword = '';
							$scope.commonListCount;
							$scope.getUnitByOrganization();
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.isPreOrPostBilling   =cookieObjectAppConfig.isPreOrPostBilling;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.isPinApplicable = labConfig.isPinApplicable;
							$scope.isPrebarcodeGenerantion = labConfig.isPrebarcodeGenerantion;
							
							$scope.visitTypeId=1;
							$scope.visityTypeList = [];
							
							$scope.selectedVisitType = [];
							

							/* code for setting Label */
							$(".selectedPageName").text("Lab Home Sample Collection");
							
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

							

							$scope.initLabSampleMaster = function() {
								$scope.listSampleDtls = [];
								$scope.labSampleMaster = {
									"labSampleId" : "",
									"orgId" : $scope.orgId,
									"orgUnitId" : $scope.orgUnitId,
									"sampleGenDatetime" : "",
									"orderId" : "",
									"visitTypeId" : "",
									"admissionId" : "",
									"encounterId" : "",
									"patientId" : "",
									"doctorId" : "",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : "",
									"updatedDate" : "",
									"uhid" : "",
									"patientDetails" : "",
									"doctorDetails" : "",
									"priorityId" : "",
									"priorityName" : "",
									"colorCode" : "",
									"isOrderComplete" : "",
									"genderId" :"",
									"visitType":"",
									"listLabSampleDetailsMaster" : $scope.listSampleDtls
								}
							}

						
							
							$scope.initLabSampleMaster();
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
								"sampleGenBy" : $scope.userId,
								"currStatus" : "",
								"sampleStatusId" : "",
								"isOutsourced" : "",
								"outsourcedLabId" : "",
								"sampleSendtocrDatetime" : "",
								"sampleSendtocrBy" : "",
								"sampleCollectionDatetime" : "",
								"sampleCollectionBy" : $scope.userId,
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
								"assignUnit":$scope.orgUnitId,
								"listTMicroSampleMediaDto":[]	
								
									
							}

							// ====================================================CODE
							// START FORFOR SAMPLE MASTER
							// LIST===========================================================

							$scope.setNoOfRecords = function() {
								$scope.initHomeSampleCollectionlist($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
							};

							$scope.initHomeSampleCollectionlist = function(orgId,orgUnitId, offset, noOfRecordsPerPage) {

								try {
									$rootScope.startLoader();
									$scope.homeSampleCollectionList = [];
									var data = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :$scope.selectedPatient,
											  "visitTypes" :$scope.selectedVisitType,
											  "deptId": $scope.deptId,
											  "orderStatusId": $scope.orderStatusId,
											  "isPreOrPostBilling": $scope.isPreOrPostBilling,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "offset": offset,
											  "recordPerPage" : noOfRecordsPerPage,
											  "userId" : $scope.userId
									};
									var URI1 = BASE_URL + ROOT_URL + "/LIS/transaction/labSampleHomeCollection/Search";
									GenericService.serviceAction("POST", URI1, data).then(function(response){
														$rootScope.stopLoader();
														$scope.homeSampleCollectionList = response.data.listObject;
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.getHomeSampleCollectionlist = function(orgId,	orgUnitId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									$scope.homeSampleCollectionList = [];
									var data = {
											 "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :$scope.selectedPatient,
											  "visitTypes" :$scope.selectedVisitType,
											  "deptId": $scope.deptId,
											  "orderStatusId": $scope.orderStatusId,
											  "isPreOrPostBilling": $scope.isPreOrPostBilling,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "offset": offset,
											  "recordPerPage" : noOfRecordsPerPage,
											  "userId":$scope.userId
											   
									};
									var URI = BASE_URL + ROOT_URL + "/LIS/transaction/labSampleHomeCollection/Search" ; 
									GenericService.serviceAction("POST", URI, data).then(
													function(response)
													{
														$scope.homeSampleCollectionList = response.data.listObject;
														$rootScope.stopLoader();
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.labSampleDetails = {};
							$scope.billStatusId = 1;
							$scope.getWorkListDetails = function(orgId,orgUnitId, orderId,billStatusId) {
								$rootScope.startLoader();
								try {
									if(billStatusId == null || billStatusId == 1){
										$scope.billStatusId = 1;
									}else if(billStatusId == 2 || billStatusId == 3){
										$scope.billStatusId = 2;
									}
									
									$scope.labSampleDetails = $scope.homeSampleCollectionList
											.filter(function(labSampleDtls) {
												if (labSampleDtls.orderId == orderId) {
													$scope.labSampleMaster = labSampleDtls;
													return labSampleDtls;

												}
											});
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION
											+ "/getHomeColletionTestList"
											+ S + orgId + S + orgUnitId + S
											+ orderId + S + $scope.deptId +S+$scope.isPreOrPostBilling;;
									console.log("GET_PHLEBOTOMY_WORKLIST_DETAILS", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) 
													{
														if(response.data.status=='success')
															{
															
															$rootScope.stopLoader();
															$scope.phlebotomyWorkListDetails=[];
															$scope.phlebotomyWorkListDetails = response.data.listObject;
															
															angular .forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
																		
																value.performTestId  = value.testId;
																if(value.assignUnit == null){
																	value.assignUnit  = value.orgUnitId;
																}
																			
															 });
															
															$('#phlebotomyWorkListDetails').modal('show');
															}
														else if(response.data.status=='error')
															{
															  $rootScope.stopLoader();
															}
														
														
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
									$scope.getHomeSampleCollectionlist($scope.orgId,$scope.orgUnitId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}

							$scope.initHomeSampleCollectionlist($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);

							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular
										.forEach(
												$scope.phlebotomyWorkListDetails,
												function(labDetailsObj) {
													if(labDetailsObj.labSampleDtlsId==null)
														{
													      labDetailsObj.sampleStatusId = $scope.selectedAll;
														}
												});
							}

							// ====================================================CODE
							// END FOR SAMPLE MASTER
							// LIST===========================================================
							
							
							/**Code for printing Barcode*/
							$scope.printBarCode = function()
							{
								var printContents = $window.document.getElementById('barcodePrintingId').innerHTML;
						        var popupWin = $window.open('', '_blank', 'width=300,height=300');
						        popupWin.document.open();
						        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
						        popupWin.document.close();
							}
							
							$scope.options =  {
							          width: 2,
							          height: 40,
							          displayValue: false,
							          font: 'monospace',
							          textAlign: 'left',
							          textPosition: 'bottom',
							          fontSize: 15,
							          backgroundColor: '#ffffff',
							          lineColor: '#000000',
							          margin: 0
							        };
								
							$scope.barcode = {
							        type: "code128",
							        code: '',
							        itemCode: '',
							        batchCode: "123456",
							        uhid : '',
							        patientDetails:'',
							        generationDateTime:''
							      }
							 
							$scope.barcodeList = [];

							$scope.disablePinBtn = false;
							$scope.checkLabPin = function()
							{
								$scope.$broadcast('show-errors-check-validity');
								if (this.labPinForm.$valid)
								{
									$scope.disablePinBtn = true;
								 var URI = BASE_URL + ROOT_URL + "/lab/pinuser/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.labPin;
								 GenericService
										.serviceAction(METHOD_GET, URI, {})
										.then(
												function(response) 
												{
													console.log(response);
													  $scope.disablePinBtn = false;
													if (response.data.status == "success")
														{
														  $scope.userId =  response.data.object;
														  $('#labPinModal').modal('hide');
														  $scope.collectAndSaveSample();
														  $scope.$broadcast('show-errors-reset');
														}
													else if(response.data.status == "error")
														{
														growl
														.error(
																response.data.message,
																{
																	title : 'Error!'
																});
														   $rootScope.stopLoader();
														}
												});
								}
								else{
									 $('#labPinModal').modal('show');
								}
							}
							
							$scope.checkConfiguration = function()
							{
								var profileIds = [];
								var listSampleDtls = [];
						    	angular
								.forEach(
										$scope.phlebotomyWorkListDetails,
										function(value, key) {
											if (value.sampleStatusId == true) {
												var worklistDetails = angular
														.copy(value);
												worklistDetails.sampleStatusId = $scope.billStatusId;
												worklistDetails.patientId = $scope.labSampleMaster.patientId;
												worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
												worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
												worklistDetails.sampleCollectionBy = $scope.userId;
												worklistDetails.sampleGenBy  = $scope.userId;
												worklistDetails.createdBy  = $scope.userId;
												worklistDetails.performTestId  = value.performTestId;
												worklistDetails.assignUnit  = value.assignUnit;
												worklistDetails.collectionTime = null;
												worklistDetails.listTMicroSampleMediaDto=[];	
												
												if(worklistDetails.profileId!=null)
												{
												   profileIds.push(worklistDetails.profileId);
												}
												 listSampleDtls
														.push(worklistDetails);
											}
										});
						
						      if(listSampleDtls.length==0)
							   {
								$rootScope.stopLoader();
								growl.warning('Please select sample.',
										{
											title : 'Warning!'
										});
								  return false;
								}
							   else{
									if($scope.isPinApplicable)
									{
									     $('#labPinModal').modal('show');
									}
								else
								{
									$scope.collectAndSaveSample();
								}
						      }
							}
							
							 $scope.validateBarcode = function(info,index){
								 
								 var test = info.sampleBarcode.replace(/[^0-9]/g, '');

								 if(test!=info.sampleBarcode){
								 			growl.warning("Please enter only numeric value",
												{
													title : 'warning!!!'
												});
								 			var idx = $scope.phlebotomyWorkListDetails.findIndex(item => item.orderDetailsId ==  info.orderDetailsId);
								 			$scope.phlebotomyWorkListDetails[idx].sampleBarcode = "";
											 return false;
								 } 
								 
									var URI = BASE_URL 
							        + ROOT_URL
							        + "/LIS/transaction/validateBarcode";
										var searchObj = {
												  "sampleBarcode" : info.sampleBarcode,
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.orgUnitId
											}; 
										GenericService.serviceAction("POST", URI, searchObj).then(function(response){
															if(response.data.listObject != null && response.data.listObject.length >0){
																growl.warning("This barcode already exist in the system",
																		{
																			title : 'warning!!!'
																		});
															}
															
														});
										
									
							 }
							
							//Save Sample
							
							$scope.saveSample = function()
							{
								console.log("$scope.labSampleMaster", JSON
										.stringify($scope.labSampleMaster));
								var URI = BASE_URL + ROOT_URL
										+ LIS_TRANSACTION + COLLECT_SAMPLE;

								try {
									console.log("SAVE_SAMPLE_URI", URI);
									GenericService
											.serviceAction("POST", URI,
													$scope.labSampleMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															
															$scope.initHomeSampleCollectionlist($scope.orgId,$scope.orgUnitId,$scope.offset,$scope.noOfRecordsPerPage);
															$scope.phlebotomyWorkListDetails=[];
														
															if($scope.isPrebarcodeGenerantion == 'Y'){
															$scope.barcodeList = [];
															console.log('response.data.listObject',response.data.listObject);
															angular.forEach(response.data.listObject,
																	function(value, key) 
																	{
																		var barcode  = angular.copy($scope.barcode);
																		barcode.code =value.sampleId;
																		barcode.itemCode =value.patientId;
																		barcode.uhid =value.uhid;
																		barcode.patientDetails =value.patientDetails;
																		barcode.generationDateTime =value.generationDateTime;
																		$scope.barcodeList.push(barcode);
																	});
															console.log('$scope.barcodeList',JSON.stringify($scope.barcodeList));
															$('#sampleBarCode').modal('show');
															}
														} else if (response.data.status == 'error') 
														{	
															$rootScope.stopLoader();
															if(response.data.code==409)
																{
																	growl.warning(response.data.message,
																			{
																				title : 'Warning!'
																			});
																	
																	$scope.initHomeSampleCollectionlist($scope.orgId,$scope.orgUnitId,	$scope.offset,	$scope.noOfRecordsPerPage);
																	
																}else{
																	growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
																}
															
															
															
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
							}
							
							// code for Activating Inactivating Sample
							$scope.collectAndSaveSample = function()
							{
								$rootScope.startLoader();
								$scope.listSampleDtls = [];
								var profileOrderDetailsIds = [];
								var worklistDetails;
								$scope.saveFlag = true;
								
								$scope.barcodeValid = false;
								angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
									if (value.sampleStatusId == true && $scope.isPrebarcodeGenerantion == 'N') {
										if(value.sampleBarcode == undefined || value.sampleBarcode == null || value.sampleBarcode == ""){
											$scope.barcodeValid = true;
										}
										
									}
								});
								if($scope.barcodeValid)
								{
									$rootScope.stopLoader();
									growl.warning('Please enter barcode...',
											{
												title : 'warning!!!'
											});
									  return false;
								}
								
								
								
								angular.forEach(
												$scope.phlebotomyWorkListDetails,
												function(value, key) {
													if (value.sampleStatusId == true) {
														worklistDetails = angular
																.copy(value);
														worklistDetails.sampleStatusId = $scope.billStatusId;
														worklistDetails.patientId = $scope.labSampleMaster.patientId;
														worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
														worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
														worklistDetails.sampleCollectionBy = $scope.userId;
														worklistDetails.sampleGenBy  = $scope.userId;
														worklistDetails.createdBy  = $scope.userId;
														worklistDetails.createdDate  = new Date().getTime();
														worklistDetails.updatedDate  = new Date().getTime();
														worklistDetails.updatedBy  = $scope.userId;
														worklistDetails.performTestId  = value.performTestId;
														worklistDetails.assignUnit  = value.assignUnit;
														worklistDetails.collectionTime = null;
														worklistDetails.listTMicroSampleMediaDto=[];	
														
														if(worklistDetails.profileId!=null)
														{
															profileOrderDetailsIds.push(worklistDetails.orderDetailsId);
														}
														$scope.listSampleDtls
																.push(worklistDetails);
													}
												});
								$scope.labSampleMaster.collectionTime = null;
								$scope.labSampleMaster.createdBy = $scope.userId;
								$scope.labSampleMaster.listLabSampleDetailsMaster = $scope.listSampleDtls;
								
								if($scope.listSampleDtls.length==0)
									{
									$rootScope.stopLoader();
									growl.error('Please select sample.',
											{
												title : 'Error!'
											});
									  return false;
									}
								else if(profileOrderDetailsIds.length>0)
								 {
								 var count = 0;
								 var sortedList = new Set(profileOrderDetailsIds);
								 angular.forEach(sortedList,
											function(orderDetailsId, key)
											{
									          count++;
										      var found = $scope.phlebotomyWorkListDetails.find(ar=>(ar.orderDetailsId==orderDetailsId && (ar.sampleStatusId==false||ar.sampleStatusId==null) && ar.sampleBarcode==null));
										      if(found)
										    	 {
										    		growl.error("Please select all panel services.",
															{
																title : 'Error!'
															});
										    		$rootScope.stopLoader();
										    		$scope.saveFlag = false;
										    	    return false;
										    	 }
										      else if(count==sortedList.size && $scope.saveFlag)
										    	  {
										    	    $scope.saveSample();
										    	  }
										       
									        });
								 }
							 else{
								   $scope.saveSample();
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
								$scope.getPhlebotomyPatientList = function(searchKeyword)
								{
							    	if(searchKeyword.length == 3)
									{
									try 
									{
										var data = {
												"orgId":$scope.orgId,
												"orgUnitId":$scope.orgUnitId,
												"visitTypeId":$scope.visitTypeId,
												"searchKeyword":searchKeyword,
										}
										
										var URI = BASE_URL 
										        + ROOT_URL
												+ LIS_TRANSACTION
												+ SEARCH_PHLEBOTOMY_WORK_LIST_PATIENT
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
								}else {
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
								
								
								$scope.checkPatient = function()
								{
									if($scope.patientId ==undefined)
									{
										$scope.selectedPatient = 0;
									}
								}

								 $scope.searchHomeSampleCollectionList = function()
									{
										try 
										{
											$rootScope.startLoader();
											$scope.homeSampleCollectionList = [];
											var searchObj = {
													
														  "orgId" : $scope.orgId,
														  "orgUnitId" :$scope.orgUnitId,
														  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
														  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
														  "patientId" :$scope.selectedPatient!=undefined ? $scope.selectedPatient : null,
														  "visitTypes" :$scope.selectedVisitType,
														  "deptId": $scope.deptId,
														  "orderStatusId": $scope.orderStatusId,
														  "isPreOrPostBilling": $scope.isPreOrPostBilling,
														  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
														  "offset": $scope.offset,
														  "recordPerPage" : $scope.noOfRecordsPerPage,
														  "userId":$scope.userId
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
											var URI = BASE_URL  + ROOT_URL +"/LIS/transaction/labSampleHomeCollection/Search";
											
											GenericService.serviceAction("POST", URI, searchObj)
											.then(function(response){
																$rootScope.stopLoader();
																$scope.homeSampleCollectionList = response.data.listObject;
																$scope.commonListCount = response.data.object;
																$scope.setPage(1,false);
															});
										
										} catch (e) {
											console.log(e.message)
										}
									}	
								$scope.reprintBarCode = function(sampleId,labSampleDtlsId)
								{
									try 
									{
										
										if(sampleId==null)
										{
										$rootScope.stopLoader();
										growl.error('Please Collect Sample to Generate Barcode.',
												{
													title : 'Error!'
												});
										  return false;
										}
										$scope.barcodeList = [];
										var newDate = {};
										
										var data =[];
										data.push(sampleId);
										newDate.sampleIds = data;
										newDate.labSampleDetialsId = labSampleDtlsId;
										$rootScope.stopLoader();
										var URI = BASE_URL 
										        + ROOT_URL
										        + LIS_TRANSACTION
												+ '/reprint/Barcode'
										console.log("URI", URI);
										 GenericService.serviceAction("POST", URI,
												 newDate).then(
														 function(response) {
																if (response.data.status == 'success') {
																	$rootScope.stopLoader();
																	angular.forEach(response.data.listObject,
																			function(value, key) 
																			{
																				var barcode  = angular.copy($scope.barcode);
																				barcode.code =value.sampleId;
																				barcode.itemCode =value.patientId;
																				barcode.uhid =value.uhid;
																				barcode.patientDetails =value.patientDetails;
																				barcode.generationDateTime =value.generationDateTime;
																				$scope.barcodeList.push(barcode);
																			});
																	$('#sampleBarCode').modal('show');
																} else if (response.data.status == 'error') {
																	growl
																			.error(
																					response.data.message,
																					{
																						title : 'Error!'
																					});
																	$rootScope.stopLoader();
																}
															});
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
									$scope.uhid = undefined;
									$scope.toDate = new Date();
									$scope.fromDate = new Date();
									$scope.initHomeSampleCollectionlist($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
							
			                    $scope.getClassForColor = function(info)
			                    {

			                           if(info.orderStatusId==2){
			                               return 'td-bg-blue';
			                           }else if (info.orderStatusId==1) 
			                           {
			                                return 'td-bg-yellow';
			                           }else if(info.orderStatusId==3){
			                               return 'td-bg-green';
			                           }
			                    }
			                    
			                    

								 $scope.getPerformTestId = function(collectedSample){
									 

			                        var newarr = {};
			                        newarr.serviceId = collectedSample.serviceId;
			                        newarr.assignUnit = collectedSample.assignUnit;
			                        
			                          $rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/LIS/transaction/getPerformTestId";
										GenericService.serviceAction("POST",URI,newarr).then(function(response){
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												
												if(response.data.object!= null){
													angular .forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
														
														if(value.orderDetailsId == collectedSample.orderDetailsId && value.testId==collectedSample.testId)
														{
															value.testId = response.data.object.performTestId;
														}
																	
													 });
												}else{
													
														angular .forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
														
														if(value.orderDetailsId == collectedSample.orderDetailsId && value.testId==collectedSample.testId)
														{
															value.assignUnit = $scope.orgUnitId;;
														}
																	
													 });
														
														growl.error("Assign Unit Does Not Perform This Test !!!",
																{
																	title : 'Error!'
																});
													
												}
											
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
			                    

						} ]);