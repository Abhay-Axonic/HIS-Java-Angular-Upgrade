/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"sampleRecollectionController",
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
						'$window',
						'$timeout',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$window,$timeout) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.rejectedSampleList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.isPaginationReq=true;
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.checkFlag = true;
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
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							
							//checkbox selection
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];

							/* code for setting Label */
							$(".selectedPageName").text("Sample Recollection");
							
							

							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();;

						    
						    $scope.completed='N';
							$scope.pending = 'N';
							$scope.outsourced = 'N';
							
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date()
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

							$scope.listLabSampleDtls = [];
							$scope.labSampleDtls = 
							{
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
								"samplePendingCount":"",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : $scope.userId,
								"updatedDate" : "",
								"sampleTestTime":"",
								"patientVisitAge":"",
								"genderId":"",
								"visitType":"",
								"listTMicroSampleMediaDto":[]	
							}


							$scope.setNoOfRecords = function() {
								$scope.initRejectedSampleList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initRejectedSampleList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								var data1 = {
										  "orgId" : $scope.orgId,
										  "orgUnitId" :$scope.orgUnitId,
										  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										  "patientId" :  $scope.tempSelectedPatientId,
										  "labSampleDtlsId" : '',
										  "visitTypes" :$scope.selectedVisitType,
										  "testTypes" : $scope.selectedtestList,
										  "deptId" : $scope.deptId,
										  "subDepts" : $scope.selectedDepartMentList,
										  "visitTypeId" : $scope.visitTypeId,
										  "assignUnit" :$scope.assignUnit,
										  "recordPerPage":noOfRecordsPerPage,
										  "offset":offset
										
								};
								var URI1 = BASE_URL + ROOT_URL
										+ LIS_CENTRAL_RECEIVING
										+ GET_REJECTED_SAMPLE_LIST+S+'Search';
								var data2 = {
										"orgId":orgId, 
										"deptId":$scope.deptId,
										"orgUnitId":orgUnitId,
										"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
								}
								var URI2 = BASE_URL + ROOT_URL
										+ LIS_CENTRAL_RECEIVING
										+ GET_TOTAL_RECORD_REJECTED_SAMPLE ;

								console.log("URI1", URI1);
								console.log("URI2", URI2);
								$scope.isPaginationReq=true;
								promiseFactoryWithObject
										.setPromisesWithObject(URI1, URI2, "POST", "POST",data1,data2)
										.then(
												function(response) 
												{
													$rootScope.stopLoader();
													$scope.rejectedSampleList = response[0].listObject;
													$scope.rejectedSampleListForFiltter = $scope.rejectedSampleList;
													$scope.filterByDummyServices();
													$scope.commonListCount = response[1].object;
													$scope.setPage(1, false);
												});
							}

							$scope.getRejectedSampleList = function(orgId,
									orgUnitId) {
								try {
									$rootScope.startLoader();
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_CENTRAL_RECEIVING
											+ GET_REJECTED_SAMPLE_LIST + S + orgId
											+ S + orgUnitId+S+$scope.deptId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.rejectedSampleList = response.data.listObject;
														$scope.rejectedSampleListForFiltter = $scope.rejectedSampleList;
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
									
									$scope.initRejectedSampleList($scope.orgId,
											$scope.orgUnitId, $scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							$scope.initRejectedSampleList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);

							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									$scope.checkFlag = false;
								} else {
									$scope.selectedAll = false;
									$scope.checkFlag = true;
								}
								angular.forEach($scope.rejectedSampleList,function(labDetailsObj) {
													labDetailsObj.sampleRecollectFlag = $scope.selectedAll;
												});
							}
							
							
							
							$scope.filterByDummyServices= function()
                            {
								$scope.rejectedSampleList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.rejectedSampleList = angular.copy($scope.rejectedSampleListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.rejectedSampleListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.rejectedSampleList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.rejectedSampleListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.rejectedSampleList.push(value);
                                              });
                                               
                                }
                            }
							
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
							          height: 60,
							          displayValue: false,
							          font: 'monospace',
							          textAlign: 'left',
							          textPosition: 'bottom',
							          fontSize: 12,
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

							$scope.labSampleMaster = [];
							$scope.initLabSampleMaster = function() {
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
									"createdBy" : "",
									"createdDate" : $scope.userId,
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
									"listLabSampleDetailsMaster" : []
								}
							}

							$scope.initLabSampleMaster();
							$scope.labSampleMasterList = [];
							// code for Activating Inactivating Sample
							
							
							$scope.saveSample = function()
							{
								var sampleList = [];
									angular
									.forEach(
											$scope.rejectedSampleList,
											function(value, key) {
												if (value.sampleRecollectFlag == true) 
												{
													sampleList.push(value);
												}
											});
									
									if(sampleList.length==0)
										{
										growl
										.warning(
												"Please select sample.",
												{
													title : 'Warning!'
												});
										  $rootScope.stopLoader();
										  return
										}
									else{
										$('#sampleRecollect').modal('show');
									  }		
							
							}
							
							
							$scope.recollectionBtn =false;
							$scope.sampleRecollect = function() 
							{
								var popup = angular.element('#sampleRecollect');
								popup.modal('hide');
								$scope.recollectionBtn =true;
								
								
								$rootScope.startLoader();
								
								$scope.labSampleMasterList = [];
								$scope.barcodeList = [];
								angular
										.forEach(
												$scope.rejectedSampleList,
												function(value, key) {
													  
													if (value.sampleRecollectFlag == true) 
													{
														var labSampleDtls = angular.copy($scope.labSampleDtls);
														var labSampleMaster = angular.copy($scope.labSampleMaster);
														labSampleMaster.patientDetails = value.patientDetails;
														labSampleMaster.uhid = value.uhid;
														labSampleMaster.createdBy = $scope.userId;
														labSampleMaster.updatedBy = $scope.userId;
													
														labSampleMaster.orderId = value.doctorId;
														labSampleMaster.visitTypeId = value.visitTypeId;
														labSampleMaster.admissionId = value.admissionId;
														labSampleMaster.encounterId= value.encounterId;
														labSampleMaster.patientId= value.patientId;
														labSampleMaster.doctorId= value.doctorId;
														
														labSampleDtls.visitType = value.visitType;
														labSampleDtls.deptName = value.deptName;
														labSampleDtls.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtls.sampleRecollectAgainstId = value.labSampleDtlsId;
														labSampleDtls.orderDetailsId = value.orderDetailsId;
														labSampleDtls.patientId = value.patientId;
														labSampleDtls.doctorId = value.doctorId;
														labSampleDtls.priorityId = value.priorityId;
														labSampleDtls.orderId = value.orderId;
														labSampleDtls.packageId = value.packageId;
														labSampleDtls.labSampleId = value.labSampleId;
														labSampleDtls.isOutsourced = value.isOutsourced;
														labSampleDtls.sampleReqCount = value.sampleReqCount;
														labSampleDtls.containerId = value.containerId;
														labSampleDtls.sampleTypeId = value.sampleTypeId;
														labSampleDtls.genderId = value.genderId;
														labSampleDtls.deptId = value.deptId;
														labSampleDtls.profileId = value.profileId;
														labSampleDtls.subDeptId = value.subDeptId;
														labSampleDtls.sampleStatusId = SAMPLE_RECOLLECTED;
														labSampleDtls.samplePendingCount = value.samplePendingCount;
														labSampleDtls.isAlliquoteReq = value.isAlliquoteReq;
														labSampleDtls.createdBy = $scope.userId;
														labSampleDtls.updatedBy = $scope.userId;
														labSampleDtls.sampleBarcode = value.sampleBarcode;
														labSampleDtls.assignUnit = value.performedUnit;
														labSampleDtls.performedUnit = $scope.orgUnitId;
														labSampleDtls.testId = (value.performTestId != undefined && value.performTestId !=null) ? value.performTestId : value.testId;
														labSampleDtls.performTestId = value.testId;
														//$scope.listLabSampleDtls.push(labSampleDtls);
														
														labSampleMaster.listLabSampleDetailsMaster = [labSampleDtls];
														$scope.labSampleMasterList.push(labSampleMaster);
													}
												});
								var URI = BASE_URL + ROOT_URL
										+ LIS_CENTRAL_RECEIVING + SAMPLE_RECOLLECTION;

								try {
									console.log("SAMPLE_RECOLLECTION", URI);
									GenericService
											.serviceAction("POST", URI,
													$scope.labSampleMasterList)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success(response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.recollectionBtn = false;
														$('#sampleRecollect').modal('hide');
															$scope.$broadcast('show-errors-reset');
															console.log('response.data.listObject',response.data.listObject)
															angular.forEach(response.data.listObject,
																	function(value, key) 
																	{
																		var barcode  = angular.copy($scope.barcode);
																		barcode.code =value.sampleId;
																		barcode.itemCode =value.patientId;
																		barcode.uhid =value.uhid;
																		barcode.patientDetails =value.patientDetails;
																		barcode.sampleName =value.sampleName ;
																		barcode.generationDateTime =value.generationDateTime;
																		$scope.barcodeList.push(barcode);
																	});
															$('#sampleBarCodeRecollect').modal('show');
															$scope.labSampleMasterList = [];
															$scope.initRejectedSampleList($scope.orgId,
																	$scope.orgUnitId, $scope.offset,
																	$scope.noOfRecordsPerPage);
															
															$rootScope.stopLoader();
															$scope.checkFlag = true;
														} else if (response.data.status == 'error') 
														{
															  
															growl
																	.error(
																			response.data.message, 
																			{
																				title : 'Error!'
																			});
															$scope.recollectionBtn = false;
															$rootScope.stopLoader();
														}
												
													});
								} catch (e) {
									console.log("Exception", e.message);
									 $scope.recollectionBtn = false;
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
								$scope.initRejectedSampleList($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
							}
							
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.fromDate.getTime();
								
								if(moment($scope.toDate).isBefore(moment($scope.fromDate)))
								{
									 $scope.toDate=new Date($scope.fromDate.getTime());
								}
							}
							
							$scope.selectedVisitTypeIds = [1,2,3,4];
							$scope.sampleStatusIds=[OP_IP_REJECTED,SAMPLE_REJECTED];
							
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
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ BIOCHECMISTRY_PATIENT_LIST_SEARCH;
									return GenericService.serviceAction("POST", URI,
											data).then(function(response) {
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
						
						 $scope.searchOutPatientList = function()
							{
								try 
								{
									$rootScope.startLoader();
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.orgUnitId,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "patientId" :  $scope.tempSelectedPatientId,
												  "labSampleDtlsId" : '',
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "visitTypeId" : $scope.visitTypeId,
												  "assignUnit" :$scope.assignUnit ,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "offset":$scope.offset
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
											+ GET_REJECTED_SAMPLE_LIST+S+'Search';
									var data2 = {
											"orgId":$scope.orgId, 
											"deptId":$scope.deptId,
											"orgUnitId":$scope.orgUnitId,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									}
									
									
									var URI2 = BASE_URL + ROOT_URL
									+ LIS_CENTRAL_RECEIVING
									+ GET_TOTAL_RECORD_REJECTED_SAMPLE ;

							console.log("URI2", URI2);
							$scope.isPaginationReq=true;
							promiseFactoryWithObject
									.setPromisesWithObject(URI, URI2, "POST", "POST",searchObj,data2)
									.then(
											function(response) 
											{
												$rootScope.stopLoader();
												$scope.rejectedSampleList=[];
												$scope.rejectedSampleList = response[0].listObject;
												$scope.commonListCount = response[1].object;
												$scope.rejectedSampleListForFiltter = $scope.rejectedSampleList;
												$scope.filterByDummyServices();
												$scope.setPage(1, false);
												
												$scope.tempSelectedPatientId=undefined;
											});
								} catch (e) {
									console.log(e.message)
								}
							}
						 
		
						 $scope.checkCheckedList = function(){
							 var checkedCount = 0;
							 		angular.forEach($scope.rejectedSampleList, function(value, key) {
									
							 			if(value.sampleRecollectFlag == true){
							 				 checkedCount = checkedCount + 1;
							 			}
									});
							 		if(checkedCount == $scope.rejectedSampleList.length){
							 			$scope.selectedAll = true;
							 		}else{
							 			$scope.selectedAll = false;
							 		}
							 		if(checkedCount > 0){
						 				$scope.checkFlag = false;
						 			}else{
						 				$scope.checkFlag = true;
						 			}
						 }
						 
						 
						} ]);