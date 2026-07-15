/**
 * @author Ganesh Chaudhari
 */
'use strict';
angular
		.module('myApp')
		.controller(
				"phlebotomyController",
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
                        '$compile',
                        '$timeout',
                        'reportService',
                        '$http',
                        'GenericServiceParamHeader',
                        'BillingGenericService',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$interval,$window,$compile,$timeout,reportService,$http,GenericServiceParamHeader,BillingGenericService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 25;
							$scope.phlebotomyWorkList = [];
							$scope.phlebotomyWorkListDetails = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword = "";
							$scope.commonListCount;
							$scope.getUnitByOrganization();
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.isPreOrPostBilling   =cookieObjectAppConfig.isPreOrPostBilling;
							$scope.neuronPlus = cookieObjectAppConfig.neuronPlus;
							$scope.unitNameCookie = cookieObject.unitNme;
							if (cookieObjectAppConfigSecond != undefined) {
							$scope.mandatoryScan = cookieObjectAppConfigSecond.mandatoryScan;
							}
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.isPinApplicable = labConfig.isPinApplicable;
							$scope.isPrebarcodeGenerantion = labConfig.isPrebarcodeGenerantion;
							$scope.isPRNPrint=  labConfig.isPRNPrint;
							$scope.prnFormat = labConfig.prnFormat;
							$scope.prnUrl = labConfig.prnUrl;
							$scope.labSortingId =  labConfig.labSortingId;
							$scope.outsourceLabId = labConfig.outsourceLabId;
							$scope.manualSampleCollectionTime = labConfig.manualSampleCollectionTime;
							console.log("adfd",$scope.isPRNPrint+"="+$scope.prnFormat);
							$scope.visitTypeId=1;
							$scope.visityTypeList = [];
							$scope.selectedVisitType = [];
							$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
							if (cookieObjectAppConfigSecond != undefined) {
							$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
							}
							$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
							if($scope.isBillManipulationAllowed == true){
								$scope.filterDummyService = 1;
							}else{
								$scope.filterDummyService = 2;
							}
							/* code for setting Label */
							$(".selectedPageName").text("Lab Home Sample Collection");
							
							/**DatePicker Code  */
							
							$scope.toDate = new Date();
							$scope.fromDate = new Date();
						    $scope.completed='N';
							$scope.pending = 'N';
							$scope.outsourced = 'N';
							
							var date = new Date();
							
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
								minDate : date.setDate((new Date()).getDate() - 90),
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

							   // for Medication History
                            $scope.openDatePickerMedicationHistory = function(index) {

                                for (var i = $scope.phlebotomyWorkListDetails.length - 1; i >= 0; i--) {
                                    if (i === index) {

                                        $scope.datepickerMedicationHistory.opened[i] = true;
                                    } else {
                                        $scope.datepickerMedicationHistory.opened[i] = false;
                                    }
                                }
                            };

                            $scope.datepickerMedicationHistory = {
                                opened : [ false ],

                            };
                            $scope.dateOptionsMedicationHistory = {
                            		formatYear : 'yyyy',
    								showWeeks : false,
    							};
                            
                            
                        
                            $scope.initClockPicker = function() 
                            { 
                                $('.clockpicker input').clockpicker({
                                    autoclose: true
                                }).on('change', function(e) {
                                    $scope.$apply(function() {
                                        $scope.worklstDetail.manaulTime = e.target.value;
                                    });
                                })
                            }



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
								/*"sampleTestTime":"",
								"patientVisitAge":"",
								"genderId":""*/
									
							}

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
			                                    $scope.staffSpecialityList = response.data.listObject;
											    $scope.staffSubSpecialityList = response.data.object;
											}
		                                 });
							$scope.setNoOfRecords = function() {
								$scope.initPhlebotomyWorklist($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.phlebotomyWorkList = [];
							$scope.initPhlebotomyWorklist = function(orgId, orgUnitId, offset, noOfRecordsPerPage) {
//									$rootScope.startLoader();
									
									var data = {
											 "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :$scope.selectedPatient,
											  "visitTypes" :$scope.selectedVisitType,
											  "deptId": $scope.deptId,
											  "orderStatusId": $scope.orderStatusId,
											  "priorityId" : $scope.priorityId,
											  "isPreOrPostBilling": $scope.isPreOrPostBilling,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "offset": offset,
											  "recordPerPage" : noOfRecordsPerPage,
											  "isCountRequired" : true,
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
											  "userId" : $scope.userId,
									};
									var URI1 = BASE_URL + ROOT_URL + LIS_TRANSACTION + PHLEBOTOMY_WORK_LIST_SEARCH;
									console.log("URI1", URI1);
									GenericService.serviceAction("POST", URI1, data)
									.then(function(response){
										if(response.data.status=='success')
										{
											           $rootScope.startLoader();
											           $scope.phlebotomyWorkList = [];
														if($scope.isBillManipulationAllowed == true){
															$scope.phlebotomyWorkList = response.data.listObject;
														}else{
															
															angular.forEach(response.data.listObject, function(value, key) 
																	{
																			if(value.realServiceCount >0){
																				$scope.phlebotomyWorkList.push(value);
																			}
																	});
															
														}
														
														$scope.commonListCount = response.data.object;
														console.log("$scope.commonListCount",$scope.commonListCount);
														console.log("$scope.phlebotomyWorkList length",$scope.phlebotomyWorkList.length);
														$scope.setPage(1,false);
														$rootScope.startLoader();
										                  $timeout( function(){
										                    	  $rootScope.stopLoader();
										                    },1000);
//														$rootScope.stopLoader();
										}
													});
									//$rootScope.stopLoader();
							}
							 $rootScope.startLoader();
			                   /* $timeout( function(){
			                    	  $rootScope.stopLoader();
			                    },5000);*/
									
							$scope.getPhlebotomyWorklist = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									$scope.phlebotomyWorkList = [];
									var data = {
											 "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :$scope.selectedPatient,
											  "visitTypes" :$scope.selectedVisitType,
											  "deptId": $scope.deptId,
											  "orderStatusId": $scope.orderStatusId,
											  "priorityId" : $scope.priorityId,
											  "isPreOrPostBilling": $scope.isPreOrPostBilling,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "offset": offset,
											  "recordPerPage" : noOfRecordsPerPage,
											  "isCountRequired" : false,
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
											  "userId" : $scope.userId
									};
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION
											 + PHLEBOTOMY_WORK_LIST_SEARCH ;
										//	+ GET_PHLEBOTOMY_WORKLIST + S
										//	+ orgId + S + orgUnitId + S
										//	+ $scope.deptId+S
										//	+ offset + S + noOfRecordsPerPage+S+$scope.isPreOrPostBilling;

									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response)
													{
														$scope.phlebotomyWorkList = [];
														if($scope.isBillManipulationAllowed == true){
															$scope.phlebotomyWorkList = response.data.listObject;
														}else{
															
															angular.forEach(response.data.listObject, function(value, key) 
																	{
																			if(value.realServiceCount >0){
																				$scope.phlebotomyWorkList.push(value);
																			}
																	});
															
														}
														$rootScope.stopLoader();
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							$scope.labSampleDetails = {};
							
							 $scope.comorbidityFlag = false;
								$scope.getPatientDiagnosis = function(obj) {
									
									$scope.comorbidityFlag = false;
									var data = {};
									var headerObjDia = {
										'unit_id' : $scope.unitId,
										'organization_id' : $scope.organizationId,
										'patient_id' : $scope.patientIdN,
										'Content-Type' : 'application/json',
									};
									var URI = BASE_URL + ROOT_URL
											+ GETPATIENTDIAGNOSISBYPATIENTID;
									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE,
													data, headerObjDia)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.patientDiagnosisList = response.data.listObject;
														console.log( "patientDiagnosisList", $scope.patientDiagnosisList);
														 $scope.complaintDelete=true;
														if($scope.patientDiagnosisList.length > 0)
														{
														  $scope.complaintDelete=false;
														  $scope.comorbidityFlag = true;
														}
														
														
														if(obj.visitTypeId == 1){
															   $scope.finalString="";
					                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
					                                        	 if(index == 0){
					                                        		 if(value.diagnosisType == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 
					                                        	   var String = value.diagnosisName +" "+ext+" ";
					                                        	   $scope.finalString= $scope.finalString.concat(String);
					                                        	 }
					                                        	 else{
					                                        		 
					                                        		 if(value.diagnosisType == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 
					                                        	      var String = value.diagnosisName +" "+ext+" ";
						                                        	   $scope.finalString= $scope.finalString.concat(","+String);
					                                        	 }
					                                         });
														}else{
															 $scope.finalString="";
					                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
					                                        	 if(index == 0){
					                                        		 if(value.type == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 if(value.diagnosisType == 1){
						                                        			var ext2 = "(P)" ;
						                                        		 }else{
						                                        			 var ext2 = "(S)" ;
						                                        		 }
					                                        		 
					                                        	   var String = value.diagnosisName +""+ext+"" +ext2;
					                                        	   $scope.finalString = $scope.finalString.concat(String);
					                                        	 }
					                                        	 else{
					                                        		 if(value.type == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 if(value.diagnosisType == 1){
						                                        			var ext2 = "(P)" ;
						                                        		 }else{
						                                        			 var ext2 = "(s)" ;
						                                        		 }
					                                        		   var String = value.diagnosisName +""+ext+"" +ext2;
						                                        	   $scope.finalString = $scope.finalString.concat(" , "+String);
					                                        	 }
					                                         });
					                                      
														}
													    
				                                         
													});
								}
								
								
							$scope.getWorkListDetails = function(obj) 
							{
								var orgId=obj.orgId;
								var orgUnitId =obj.orgUnitId;
								var orderId=obj.orderId;
								$scope.admissionIdN=obj.admissionId;
								$scope.encounterIdN=obj.encounterId;
								$scope.patientIdN=obj.patientId;
								$rootScope.startLoader();
								try {

									$scope.labSampleDetails = $scope.phlebotomyWorkList
											.filter(function(labSampleDtls) {
												if (labSampleDtls.orderId == orderId) {
													$scope.labSampleMaster = labSampleDtls;
													return labSampleDtls;

												}
											});

									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION
											+ GET_PHLEBOTOMY_WORKLIST_DETAILS
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
															$scope.phlebotomyWorkListDetailstest = response.data.listObject;

															$scope.savePhlrbotomy=true;
                                                            angular.forEach($scope.phlebotomyWorkListDetailstest, function(value, key) {
                                                                                              if(value.sampleStatusId == undefined || value.sampleStatusId==null){
                                                                                                $scope.savePhlrbotomy=false;
                                                                                             }
                                                                                          });
															
															if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
																angular.forEach($scope.phlebotomyWorkListDetailstest, function(value, key) {
																	angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																		if(value.subDeptId == value1.subSpecialityMasterId){
																			value.performTestId  = value.testId;
																			value.tempOutSource = value.isOutsourced;
																			if(value.manualDate != null){
																				value.manualDate =  moment(value.manualDate, 'DD-MM-YYYY').toDate();
																			}
																			if(value.assignUnit == null){
																				value.assignUnit  = value.orgUnitId;
																			}
																			$scope.phlebotomyWorkListDetails.push(value);
																		}
																	
																	});		
																 });
															}else{
																angular.forEach($scope.phlebotomyWorkListDetailstest, function(value, key) {
																			value.performTestId  = value.testId;
																			value.tempOutSource = value.isOutsourced;
																			if(value.manualDate != null){
																				value.manualDate =  moment(value.manualDate, 'DD-MM-YYYY').toDate();
																			}
																			if(value.assignUnit == null){
																				value.assignUnit  = value.orgUnitId;
																			}
																			$scope.phlebotomyWorkListDetails.push(value);
																 });
															}
															
															$scope.phlebotomyWorkListDetailsForDummyServiceFilter = $scope.phlebotomyWorkListDetails;
															if($scope.isBillManipulationAllowed==false)
									                        {
																$scope.phlebotomyWorkListDetails = [];
									                            angular.forEach($scope.phlebotomyWorkListDetailsForDummyServiceFilter,function(obj,key)
									                             {
									                                if(obj.isDummyService==false)
									                                {
									                                	$scope.phlebotomyWorkListDetails.push(obj);
									                                }
									                             });
									                        }else{
									                        	$scope.phlebotomyWorkListDetailsForDummyServiceFilter = $scope.phlebotomyWorkListDetails;
																$scope.filterByDummyServices();
									                        }
															$scope.getPatientDiagnosis(obj);
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
							
                            $scope.filterByDummyServices= function()
                            {
                                $rootScope.startLoader();
                                $scope.phlebotomyWorkListDetails =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.phlebotomyWorkListDetails = angular.copy($scope.phlebotomyWorkListDetailsForDummyServiceFilter);
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.phlebotomyWorkListDetailsForDummyServiceFilter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.phlebotomyWorkListDetails.push(value);
                                    });
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                    angular.forEach($scope.phlebotomyWorkListDetailsForDummyServiceFilter, function (value, key) 
                                    {
                                        if(value.isDummyService==true)
                                        	$scope.phlebotomyWorkListDetails.push(value);
                                    });
                                }
                                $rootScope.stopLoaderPopup();
                                $rootScope.stopLoader();
                            }

							
							
							$scope.collectionObj = {};
							$scope.validateUhid = "";
							$scope.validateWorkListDetails = function(obj){
								 $scope.billingMasterId = obj.billingMasterId;
								 $scope.visitTypeId = obj.visitTypeId;
									$scope.checkFlags = 1;
									$scope.checkFlagBillSettle = false;
								if($scope.mandatoryScan != null && $scope.mandatoryScan =="Y" && obj.orderStatusId != 3){
									$scope.collectionObj = obj; 
									$scope.validateUhid = "";
									$('#validatePatient').modal('show');
								}else{
									$scope.getWorkListDetails(obj);
								}
								
							}
							
							$scope.closevalidatePop = function(){
								$('#validatePatient').modal('hide');
							}
							
							$scope.validateEquealPatient = function(event,barcode){
								 var enterCode = 13;
								 if (!barcode && event.which === enterCode) {
								        barcode = $scope.validateUhid;
								      }
									 if(!!barcode){
										 
										 if($scope.collectionObj.uhid.toLowerCase() == $scope.validateUhid.toLowerCase() ){
												$('#validatePatient').modal('hide');
												$scope.getWorkListDetails($scope.collectionObj);
											}else{
												growl.warning('Please enter valid patient number',
														{
															title : 'Warning!'
														});
											}
										 
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
									$scope.getPhlebotomyWorklist($scope.orgId,
											$scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initPhlebotomyWorklist($scope.orgId,
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
												$scope.phlebotomyWorkListDetails,
												function(labDetailsObj) {
													if(labDetailsObj.labSampleDtlsId==null)
														{
													      labDetailsObj.sampleStatusId = $scope.selectedAll;
														}
												});
							}

							
						

							$scope.patHistory = "";
														$scope.tempEncounterId = "";
														$scope.getPatHis = function(item){
															$scope.tempEncounterId = item.encounterId;
															try{
																$rootScope.startLoader();
																
																var data = {
																		"encounterId" : item.encounterId
																};
																var URI = BASE_URL + ROOT_URL
																+"/LIS/transaction/getPatHistoryByEncounterId";
														GenericService.serviceAction("POST", URI, data)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				$scope.patHistory = response.data.object.patHistory;
																				$rootScope.stopLoader();
																			}
																			$rootScope.stopLoader();
																		});
															}catch(e){
																console.log("Exception-",e);
																$rootScope.stopLoader();
															}
															
														}
														
														
														$scope.getData = function(){
															var data = {
																	"encounterId" : $scope.tempEncounterId,
																	"patHistory" : $scope.patHistory 
															};
														}
														
														
														$scope.updateHistory = function(){
															try{
																$rootScope.startLoader();
																
																var data = {
																		"encounterId" : $scope.tempEncounterId,
																		"patHistory" : $scope.patHistory 
																};
																var URI = BASE_URL + ROOT_URL
																+"/LIS/transaction/updatePatHistoryByEncounterId";
														GenericService.serviceAction("POST", URI, data)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				
																				growl.success("Patient History Updated Successfully",
																						{
																							title : 'Success!'
																						});
																				$rootScope.stopLoader();
																			}
																			$rootScope.stopLoader();
																		});
															}catch(e){
																console.log("Exception-",e);
																$rootScope.stopLoader();
															}
															
														}
							
							
							
							
							
							
							// ====================================================CODE
							// END FOR SAMPLE MASTER
							// LIST===========================================================
							
														 // Do Printing ... for Prn call
														$scope.printPRNPrint= function(prnObject){						
														
														
                                                                        var url = $scope.prnUrl;

                                                                        var xhr = new XMLHttpRequest();
                                                                        xhr.open("POST", url);

                                                                        xhr.setRequestHeader("Content-Type", "text/plain");

                                                                        xhr.onreadystatechange = function () {
                                                                           if (xhr.readyState === 4) {
                                                                              console.log(xhr.status);
                                                                              console.log(xhr.responseText);
                                                                           }};

                                                                        var data = prnObject;
                                                                        xhr.send(data);
														} 
														//Do printing... For JSPrinter Manager call
													    function print(o) {
													        if (jspmWSStatus()) {
													        	  var cpj = new JSPM.ClientPrintJob();
												                    //Set Printer type (Refer to the help, there many of them!)
												                    if ($('#useDefaultPrinter').prop('checked')) {
												                        cpj.clientPrinter = new JSPM.DefaultPrinter();
												                    } else {
												                    	 cpj.clientPrinter = new JSPM.InstalledPrinter($scope.prnUrl);
												                    }
												                    //Set printer commands...
												                    cpj.binaryPrinterCommands = $scope.finalPRN;
												                    //Send print job to printer!
												                    cpj.sendToClient();								        
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
														  $scope.pinUserId =  response.data.object;
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
							
							$scope.cancelBeforBillSettle = function()
							{
								$scope.checkFlagBillSettle = false;
								$('#checkBillSettlePopUp').modal('hide');
							}
							
							$scope.continueBeforBillSettle = function()
							{
								$scope.checkFlags = 2;
								$scope.checkFlagBillSettle = false;
								$('#checkBillSettlePopUp').modal('hide');
								$scope.checkConfiguration();
							}
						
							
							$scope.savePhlrbotomy = false;
							$scope.checkFlagBillSettle = false;
							$scope.checkFlags = 1;
							$scope.checkConfiguration = function()
							{
								var check = false;
								angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
									if (value.sampleStatusId == true) {
										if($scope.manualSampleCollectionTime == 'Y'){
											if(value.manualDate != undefined && value.manualDate != null && value.manualDate != ""){
												if(value.manaulTime == undefined || value.manaulTime == null || value.manaulTime == ""){
													check = true;
												}
											}
										}
										
									}
								});
								
								
								if(check == true){
									$rootScope.stopLoader();
									growl.warning('Please select manual time',
											{
												title : 'Warning!'
											});
									  return false;
								}
								$scope.checkBillList = [];
								if($scope.visitTypeId == 1){
									
								
									var data = {
										    "orgId":$scope.orgId,
										    "unitId":$scope.orgUnitId,
										    "billingMasterId":$scope.billingMasterId
										}
									var URI = BASE_URL + ROOT_URL + "/api/billing/getDataForCheckBillStatusByBillNumber";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											if($scope.isPreOrPostBilling == "N"){
												
												$scope.checkBillList=false;
												$scope.billStatusId = 3;
												$scope.paymentEntitlementId =1;
												
											}else{
												
												$scope.checkBillList=response.data.listObject;
												$scope.billStatusId = $scope.checkBillList[0].billStatusId;
												$scope.paymentEntitlementId = $scope.checkBillList[0].paymentEntitlementId;
											}
											
											
											if($scope.paymentEntitlementId != 2  && $scope.paymentEntitlementId != 3){
												
											if($scope.billStatusId == 3 && $scope.paymentEntitlementId == 1 && $scope.checkFlagBillSettle == false){
												$scope.savePhlrbotomy = true;
												var profileIds = [];
												var listSampleDtls = [];
										    	angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
															if (value.sampleStatusId == true) {
																var worklistDetails = angular
																		.copy(value);
																worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
																worklistDetails.patientId = $scope.labSampleMaster.patientId;
																worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
																worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
																worklistDetails.sampleCollectionBy = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
																worklistDetails.sampleGenBy  = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
																worklistDetails.createdBy  = $scope.userId;
																worklistDetails.performTestId  = value.performTestId;
																worklistDetails.assignUnit  = value.assignUnit;
																worklistDetails.srfId  = value.srfId;
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
										    	$scope.savePhlrbotomy = false;
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
											}else{
												
												
												if($scope.checkFlags == 1){
													$scope.checkFlagBillSettle = true;
													$('#checkBillSettlePopUp').modal('show');
												}else{
													$scope.savePhlrbotomy = true;
													var profileIds = [];
													var listSampleDtls = [];
													
											    	angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
																if (value.sampleStatusId == true) {
																	var worklistDetails = angular
																			.copy(value);
																	worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
																	worklistDetails.patientId = $scope.labSampleMaster.patientId;
																	worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
																	worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
																	worklistDetails.sampleCollectionBy = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
																	worklistDetails.sampleGenBy  = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
																	worklistDetails.createdBy  = $scope.userId;
																	worklistDetails.performTestId  = value.performTestId;
																	worklistDetails.assignUnit  = value.assignUnit;
																	worklistDetails.srfId  = value.srfId;
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
											    	$scope.savePhlrbotomy = false;
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
												
											}
										}else{

											$scope.savePhlrbotomy = true;
											var profileIds = [];
											var listSampleDtls = [];
											
									    	angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
														if (value.sampleStatusId == true) {
															var worklistDetails = angular
																	.copy(value);
															worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
															worklistDetails.patientId = $scope.labSampleMaster.patientId;
															worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
															worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
															worklistDetails.sampleCollectionBy = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
															worklistDetails.sampleGenBy  = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
															worklistDetails.createdBy  = $scope.userId;
															worklistDetails.performTestId  = value.performTestId;
															worklistDetails.assignUnit  = value.assignUnit;
															worklistDetails.srfId  = value.srfId;
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
									    	$scope.savePhlrbotomy = false;
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
										}
									});
									
									
								}else{
									$scope.savePhlrbotomy = true;
									var profileIds = [];
									var listSampleDtls = [];
							    	angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
												if (value.sampleStatusId == true) {
													var worklistDetails = angular
															.copy(value);
													worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
													worklistDetails.patientId = $scope.labSampleMaster.patientId;
													worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
													worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
													worklistDetails.sampleCollectionBy = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
													worklistDetails.sampleGenBy  = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
													worklistDetails.createdBy  = $scope.userId;
													worklistDetails.performTestId  = value.performTestId;
													worklistDetails.assignUnit  = value.assignUnit;
													worklistDetails.srfId  = value.srfId;
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
							    	$scope.savePhlrbotomy = false;
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
															$scope
																	.initPhlebotomyWorklist(
																			$scope.orgId,
																			$scope.orgUnitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															$scope.phlebotomyWorkListDetails=[];
														
															//if($scope.isPrebarcodeGenerantion == 'Y'){
															$scope.barcodeList = [];
															console.log('response.data.listObject',response.data.listObject);
															 $scope.finalPRN="";
																/*if(worklistDetails.profileId!=null)
																{
																	profileOrderDetailsIds.push(worklistDetails.orderDetailsId);
																}
																else if(profileOrderDetailsIds.length>0)
																 {*/
																 var count = 0;
																 $scope.sampleIds = [];
																
																 var labSampleIds = [];
															angular.forEach(response.data.listObject,
																	function(value, key) 
																	{
																		var barcode  = angular.copy($scope.barcode);
																		barcode.code =value.sampleId;
																		barcode.itemCode =value.patientId;
																		barcode.sampleName =value.sampleName;
																		barcode.uhid =value.uhid;
																		barcode.patientDetails =value.patientDetails;
																		barcode.generationDateTime =value.generationDateTime;
																		$scope.barcodeList.push(barcode);
																		$scope.sampleIds.push(value.sampleId);
																		count++;
																		if(response.data.listObject.length==count){
																			if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' && $scope.prnFormat!=null){
																			 var sortedList = new Set($scope.sampleIds);
																			 let array = [];
                                                                             sortedList.forEach(v => array.push(v));
																				var info={
																								patientId:$scope.patientIdN,
																								templateDesc:$scope.prnFormat,
																								admissionId:$scope.admissionIdN,
																						        encounterId:$scope.encounterIdN,
																						        sampleIds:array,
																						        labSampleDetailsId : value.labSampleDtlsId,
																						        
																							}
																				  $scope.printRadiologyReport(info,null);
																			}
																			 
																		}
																		
																		if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' && $scope.prnFormat!=null){
																		}else{
																			labSampleIds.push(value.labSampleDtlsId);
																		}
																		/*if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' &&  $scope.prnFormat!=null){
																			if($scope.finalPRN!=""){
																				 $scope.finalPRN= $scope.finalPRN +"\n"+$scope.prnFormat.replaceAll("@patientName",value.patientDetails);
																				 $scope.finalPRN= $scope.finalPRN.replaceAll("@hospitalName",$scope.unitNameCookie);
	                                                                             $scope.finalPRN= $scope.finalPRN.replaceAll("@barcodeValue",value.sampleId);
	                                                                              $scope.finalPRN= $scope.finalPRN.replaceAll("@date",value.generationDateTime);
	                                                                              $scope.finalPRN= $scope.finalPRN.replaceAll("@testName",value.sampleName);
	                                                                              var lastchar=value.sampleId.slice(-2);
	                                                                              var firstString=value.sampleId.substring(0, value.sampleId.length-2); 
	                                                                              $scope.finalPRN= $scope.finalPRN.replaceAll("@Exponantial","C"+firstString+"&E"+lastchar);
																			}else{
																				 $scope.finalPRN= $scope.prnFormat.replaceAll("@patientName",value.patientDetails);
																				$scope.finalPRN= $scope.finalPRN.replaceAll("@hospitalName",$scope.unitNameCookie);
                                                                             $scope.finalPRN= $scope.finalPRN.replaceAll("@barcodeValue",value.sampleId);
                                                                              $scope.finalPRN= $scope.finalPRN.replaceAll("@date",value.generationDateTime);
                                                                              $scope.finalPRN= $scope.finalPRN.replaceAll("@testName",value.sampleName);
                                                                              var lastchar=value.sampleId.slice(-2);
                                                                              var firstString=value.sampleId.substring(0, value.sampleId.length-2); 
                                                                              $scope.finalPRN= $scope.finalPRN.replaceAll("@Exponantial","C"+firstString+"&E"+lastchar);
																			}
																			
																		}*/
																	});
//														
															
															if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y'){
																
															}else{
																
																if(labSampleIds!=undefined && labSampleIds!=null && labSampleIds.length>0){
																	var data =  
																	{
																            "appName": "HIS",
																            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																            "reportDesignFilePath": "PathologybarcodePhebo.rpt",
																            "reportFormat": "PDF",
																            "exportFileName": "pathologyReport",
																            "reportParams": [
																              {
																	             "paramName": "lab_sample_Detils_id",
																	             "paramType": "String",
																	             "paramValue": labSampleIds.toString()
																              }
																            ]
																      };
																	  reportService.generate(REPORT_API_URI1,data);
																}
																
															}
															
															
															
													
															/* $scope.persons = $http({
														            url: $scope.prnUrl,//'https://webhook.site/04f40bf6-c987-4357-95b3-55c5093f1c98',
														            method: "POST",
														            data: $scope.finalPRN,
														            headers: {'Content-Type': 'text/plain'}
														        }).success(function (data, status, headers, config) {
														                $scope.data = data; // how do pass this to $scope.persons?
														            }).error(function (data, status, headers, config) {
														                $scope.status = status;
														            });*/
															/*if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' && $scope.prnFormat!=null){
																//JSPrinterManager call
//																print($scope.finalPRN);
																//For PRN Print manager
																	$scope.printPRNPrint($scope.finalPRN);
															}*/
															
															//console.log('$scope.barcodeList',JSON.stringify($scope.barcodeList));
															//$('#sampleBarCode').modal('show');
															
															//}
														} else if (response.data.status == 'error') 
														{	
															$rootScope.stopLoader();
															if(response.data.code==409)
																{
																	growl.warning(response.data.message,
																			{
																				title : 'Warning!'
																			});
																	
																	$scope.initPhlebotomyWorklist(
																			$scope.orgId,
																			$scope.orgUnitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
																	
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
								}else{
								
								angular.forEach($scope.phlebotomyWorkListDetails,function(value, key) {
													if (value.sampleStatusId == true) {
														worklistDetails = angular.copy(value);
														worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
														worklistDetails.patientId = $scope.labSampleMaster.patientId;
														worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
														worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
														worklistDetails.sampleCollectionBy = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
														worklistDetails.sampleGenBy  = ($scope.pinUserId!=undefined || $scope.pinUserId!=null) ? $scope.pinUserId :$scope.userId;
														worklistDetails.createdBy  = $scope.userId;
														worklistDetails.createdDate  = new Date().getTime();
														worklistDetails.updatedDate  = new Date().getTime();
														worklistDetails.updatedBy  = $scope.userId;
														worklistDetails.performTestId  = value.performTestId;
														worklistDetails.assignUnit  = value.assignUnit;
														worklistDetails.collectionTime = null;
														if($scope.manualSampleCollectionTime == 'Y'){
															if(worklistDetails.manualDate != undefined && worklistDetails.manualDate!= null && worklistDetails.manualDate!= ""
																&& worklistDetails.manaulTime != undefined && worklistDetails.manaulTime!= null && worklistDetails.manaulTime!= ""){
																
																worklistDetails.manualSampleCollectionDateTime = moment(worklistDetails.manualDate).format('DD-MM-YYYY')+" "+worklistDetails.manaulTime +":00";
															}
														}
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
								
							}
							
					
							/* $interval(refreshPage, 3000000);*/
							 function refreshPage()
							 {
								 $scope.initPhlebotomyWorklist($scope.orgId,
											$scope.orgUnitId, $scope.offset,
											$scope.noOfRecordsPerPage);
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
							    $scope.clinObj = {
										clinicalNote :"",
										showclinicalNote : ""
								}
								
								$scope.showClinicalNote = function(item) {
									
									$scope.clinObj.showclinicalNote =item.ordRemarks;
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
								
								
								
								$scope.testAndPaymentDetails = {};
								$scope.getTestDetails = function(orderId)
								{
									try 
									{
										var data = {
												"orgId":$scope.orgId,
												"orgUnitId":$scope.orgUnitId,
												"orderId":orderId,
												"userId":$scope.userId,
										}
										
										var URI = BASE_URL 
										        + ROOT_URL+"/LIS/transaction/PhlebotomyWorklistDetails"
										 GenericService.serviceAction("POST", URI,data).then(function(response) {
											 $scope.testAndPaymentDetails = {};
											 
											 if (response.data.status == 'success') {
												 if( response.data.object != null){
													 $scope.testAndPaymentDetails =  response.data.object;
													 $('#phlebotomyWorkList').modal('show');	
												 }
												 
											 }
											
										 });
									} catch (e) {
										console.log(e.message)
									}
								}
								
								
								
								$scope.checkPatient = function()
								{
									if($scope.patientId ==undefined)
									{
										$scope.selectedPatient = 0;
									}
								}

								 $scope.searchPhlebotomyWorkList = function()
									{
										try 
										{
											$rootScope.startLoader();
											$scope.phlebotomyWorkList = [];
											var searchObj = {
													
														  "orgId" : $scope.orgId,
														  "orgUnitId" :$scope.orgUnitId,
														  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
														  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
														  "patientId" :$scope.selectedPatient,
														  "visitTypes" :$scope.selectedVisitType,
														  "deptId": $scope.deptId,
														  "orderStatusId": $scope.orderStatusId,
														  "priorityId" : $scope.priorityId,
														  "isPreOrPostBilling": $scope.isPreOrPostBilling,
														  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
														  "offset": $scope.offset,
														  "recordPerPage" : $scope.noOfRecordsPerPage,
														  "isCountRequired" : true,
														  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
														  "userId" : $scope.userId
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
											console.log("phlebotomyWorkList",JSON.stringify(searchObj));
											var URI = BASE_URL 
											        + ROOT_URL
											        + LIS_TRANSACTION
													+ PHLEBOTOMY_WORK_LIST_SEARCH;
											console.log("URI", URI);
											
											GenericService.serviceAction("POST", URI, searchObj)
											.then(function(response){
																$rootScope.stopLoader();
																$scope.phlebotomyWorkList = [];
																if($scope.isBillManipulationAllowed == true){
																	$scope.phlebotomyWorkList = response.data.listObject;
																}else{
																	
																	angular.forEach(response.data.listObject, function(value, key) 
																			{
																					if(value.realServiceCount >0){
																						$scope.phlebotomyWorkList.push(value);
																					}
																			});
																	
																}
																$scope.commonListCount = response.data.object;
																$scope.setPage(1,false);
															});
											
										
										} catch (e) {
											console.log(e.message)
										}
									}	
								 
								  $scope.printRadiologyReport=function(info,printWithHeader){
							        	
							        	info.organizationId = $scope.organizationId;
							        	info.unitId =  $scope.unitId;
//							        	info.printWithHeader = printWithHeader;
							        	var URI = BASE_URL + ROOT_URL + "/LIS/transaction/printBarcodeLab";
							    		BillingGenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				   
							    				   if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' && $scope.prnFormat!=null && response.data.object.templateDesc!=null){
														//JSPrinterManager call
//														print($scope.finalPRN);
														//For PRN Print manager
															$scope.printPRNPrint(response.data.object.templateDesc);
													}														
							    					 					    				
										            
							    				   console.log("finalPRN",response.data.object.templateDesc);
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
									
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
										
										if(labSampleDtlsId==null)
										{
										$rootScope.stopLoader();
										growl.error('Please Collect Sample to Print Barcode.',
												{
													title : 'Error!'
												});
										  return false;
										}
										$scope.barcodeList = [];
										var newDate = {};
										
										var data =[];
										data.push(sampleId);
										$scope.sampleIdss = data;
										newDate.sampleIds = data;
										newDate.labSampleDetialsId = labSampleDtlsId;
										 var labSampleIds = [];
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
																	 $scope.finalPRN="";
																	angular.forEach(response.data.listObject,
																			function(value, key) 
																			{
																				var barcode  = angular.copy($scope.barcode);
																				barcode.code =value.sampleId;
																				barcode.itemCode =value.patientId;
																				barcode.uhid =value.uhid;
																				barcode.patientDetails =value.patientDetails;
																				barcode.sampleName =value.sampleName;
																				barcode.generationDateTime =value.generationDateTime;
																				$scope.barcodeList.push(barcode);
																				
																			/*	if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' && $scope.prnFormat!=null){
																					if($scope.finalPRN!=""){
																						 $scope.finalPRN= $scope.finalPRN +"\n"+$scope.prnFormat.replaceAll("@patientName",value.patientDetails);
																						 $scope.finalPRN= $scope.finalPRN.replaceAll("@hospitalName",$scope.unitNameCookie);
	                                                                                     $scope.finalPRN= $scope.finalPRN.replaceAll("@barcodeValue",value.sampleId);
	                                                                                      $scope.finalPRN= $scope.finalPRN.replaceAll("@date",value.generationDateTime);
	                                                                                      $scope.finalPRN= $scope.finalPRN.replaceAll("@testName",value.sampleName);
	                                                                                      var lastchar=value.sampleId.slice(-2);
	                                                                                      var firstString=value.sampleId.substring(0, value.sampleId.length-2); 
	                                                                                      $scope.finalPRN= $scope.finalPRN.replaceAll("@Exponantial","C"+firstString+"&E"+lastchar);
																					}else{
																						 $scope.finalPRN= $scope.prnFormat.replaceAll("@patientName",value.patientDetails);
																						$scope.finalPRN= $scope.finalPRN.replaceAll("@hospitalName",$scope.unitNameCookie);
                                                                                     $scope.finalPRN= $scope.finalPRN.replaceAll("@barcodeValue",value.sampleId);
                                                                                      $scope.finalPRN= $scope.finalPRN.replaceAll("@date",value.generationDateTime);
                                                                                      $scope.finalPRN= $scope.finalPRN.replaceAll("@testName",value.sampleName);
                                                                                      var lastchar=value.sampleId.slice(-2);
                                                                                      var firstString=value.sampleId.substring(0, value.sampleId.length-2); 
                                                                                      $scope.finalPRN= $scope.finalPRN.replaceAll("@Exponantial","C"+firstString+"&E"+lastchar);
																					}	 
																				}*/
																			});
																	if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint=='Y' && $scope.prnFormat!=null){
																		  var info={
																						patientId:$scope.patientIdN,
																						templateDesc:$scope.prnFormat,
																						admissionId:$scope.admissionIdN,
																				        encounterId:$scope.encounterIdN,
																				        sampleIds:$scope.sampleIdss,
																				        labSampleDetailsId : labSampleDtlsId,
																		           }
																		       $scope.printRadiologyReport(info,null);
																	       }else{
																	    	  
																	    	   labSampleIds.push(labSampleDtlsId);
																	    	   
																	    	   if(labSampleIds!=undefined && labSampleIds!=null && labSampleIds.length>0){
																					var data =  
																					{
																				            "appName": "HIS",
																				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																				            "reportDesignFilePath": "PathologybarcodePhebo.rpt",
																				            "reportFormat": "PDF",
																				            "exportFileName": "pathologyReport",
																				            "reportParams": [
																				              {
																					             "paramName": "lab_sample_Detils_id",
																					             "paramType": "String",
																					             "paramValue": labSampleIds.toString()
																				              }
																				            ]
																				      };
																					  reportService.generate(REPORT_API_URI1,data);
																				}
																	    	   
																	       }
																	
																	 
																//	$('#sampleBarCode').modal('show');
																	//$scope.printBarCode();
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
									$scope.priorityId = '';
									$scope.uhid = undefined;
									$scope.toDate = new Date();
									$scope.fromDate = new Date();
									$scope.initPhlebotomyWorklist($scope.orgId,$scope.orgUnitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
							
			                  
			                    $scope.getClassForColor = function(color){
			                    	
			                    	if(color.orderStatusId==2){
			                    		
			                    		if(color.priorityId==2){
			                    			$scope.col='td-bg-red-and-blue';
				                               return $scope.col;
				                    	}else{
				                    		$scope.col='td-bg-blue';
				                               return $scope.col;
				                    	}
			                    		
			                    		
			                               
			                           }else if (color.orderStatusId==1) 
			                           {
			                        	   
			                        	   if(color.priorityId==2){
			                        		   $scope.col='td-bg-red-and-yellow';
				                               return $scope.col;
					                    		
					                    	}else{
					                    		$scope.col='td-bg-yellow';
					                               return $scope.col;
					                    	}
			                        	   
			                                
			                           }else if(color.orderStatusId==3){
			                        	   
			                        	   if(color.priorityId==2){
			                        		   $scope.col='td-bg-red-and-green';
				                               return $scope.col;
					                    		
					                    	}else{
					                    		$scope.col='td-bg-green';
					                               return $scope.col;
					                    	}
			                        	   
			                               
			                           }
			                    	
			                    	
			                    	
			                    }
			                    
			                    $scope.getClassForColor1 = function(color){
			                    	if(color.priorityId==2){
		                        		   $scope.col='td-bg-red';
			                               return $scope.col;
				                    		
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
													$scope.changeAssignUnitObj = {
															testId : "",
															assignUnit : "",
															labSampleDtlsId : ""
													};
													if(collectedSample.sampleStatusId!=null &&(collectedSample.sampleStatusId == 2 || collectedSample.sampleStatusId == 12 
															|| collectedSample.sampleStatusId == 13 || collectedSample.sampleStatusId == 14)){
														 
														$scope.changeAssignUnitObj.testId = response.data.object.performTestId;
														$scope.changeAssignUnitObj.assignUnit = collectedSample.assignUnit
														$scope.changeAssignUnitObj.labSampleDtlsId = collectedSample.labSampleDtlsId;
														 $('#assignUnitConfirmation').modal('show');
													}
//													else if(collectedSample.sampleBarcode != null){
//														angular .forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
//															
//															if(value.orderDetailsId == collectedSample.orderDetailsId && value.testId==collectedSample.testId)
//															{
//																value.assignUnit = $scope.orgUnitId;;
//															}
//																		
//														 });
//														growl.warning("You can not change Assign Unit!!!",
//																{
//																	title : 'warning!'
//																});
//														return false;
//													}
												}else{
													
														angular .forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
														
														if(value.orderDetailsId == collectedSample.orderDetailsId && value.testId==collectedSample.testId)
														{
															value.assignUnit = $scope.orgUnitId;;
														}
																	
													 });
														
														growl.warning("Assign Unit Does Not Perform This Test !!!",
																{
																	title : 'warning!'
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
								 
								 $scope.updateAssignUnit = function(){
			                          $rootScope.startLoader();
			                          $scope.changeAssignUnitObj.updatedBy  = $scope.userId;
										var URI = BASE_URL + ROOT_URL + "/LIS/transaction/updateAssignUnitAndTestId";
										GenericService.serviceAction("POST",URI,$scope.changeAssignUnitObj).then(function(response){
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.status,
														{
															title : 'success!'
														});
												$scope.changeAssignUnitObj = {
														testId : "",
														assignUnit : "",
														labSampleDtlsId : ""
												};
												$scope.phlebotomyWorkListDetails[$scope.indexForOut].tempOutSource = $scope.isOutSource;
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
										$rootScope.stopLoader();
										$('#assignUnitConfirmation').modal('hide');
								 }
								 
								 $scope.clearAssignUnit = function(){
									 $scope.changeAssignUnitObj = {
												testId : "",
												assignUnit : "",
												labSampleDtlsId : ""
										};
								 }
								 
								 $scope.msg = "";
								 $scope.orderDetailsId = "";
								 $scope.isOutSource = "";
								 $scope.indexForOut= "";
								 $scope.openOutsourcePopup = function(obj,index){
									 $scope.indexForOut= index;
									 if(obj.sampleBarcode!=null && (obj.sampleStatusId == 2 || obj.sampleStatusId == 12 || obj.sampleStatusId == 13 || obj.sampleStatusId == 14 
											 || obj.sampleStatusId == 16 || obj.sampleStatusId == 19 )){
										 $scope.orderDetailsId = "";
										 $scope.isOutSource = "";
										 if(obj.isOutsourced == 'Y'){
											 $scope.msg = "Do You Want To Outsource this Service?"
										 }else  if(obj.isOutsourced != 'Y'){
											 $scope.msg = "Do You Want To Cancel Outsource Service?"
										 }
										 $scope.orderDetailsId = obj.orderDetailsId; 
										 $scope.isOutSource = obj.isOutsourced;
										 
										 $('#outSourceConfirmation').modal('show');
										
									 }else if(obj.sampleBarcode!=null && obj.labSampleDtlsId != null){
										 $rootScope.stopLoader();
											growl.error("You Can not Outsource, This Service is Already Processed in Report Entry",
													{
														title : 'Error!'
													});
											obj.isOutsourced = obj.tempOutSource;
											return false;
									 }
								 }
								 
								 $scope.updateOutSourceService = function(){
									 var data = {
											 orderDetailsId :  $scope.orderDetailsId,
											 isOutSource : $scope.isOutSource
									 	}
			                          $rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/LIS/transaction/updateLabSampleDetails/outsource";
										GenericService.serviceAction("POST",URI,data).then(function(response){
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.status,
														{
															title : 'success!'
														});
												$scope.phlebotomyWorkListDetails[$scope.indexForOut].tempOutSource = $scope.isOutSource;
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
											$rootScope.stopLoader();
											 $('#outSourceConfirmation').modal('hide');
										});
									 
								 }
			                    
								$scope.clearOutSourceService = function(){
									 $scope.msg = "";
									 $scope.orderDetailsId = "";
									 $scope.isOutSource = "";
									 $scope.phlebotomyWorkListDetails[$scope.indexForOut].isOutsourced = $scope.phlebotomyWorkListDetails[$scope.indexForOut].tempOutSource;
								 }
								
								$scope.printAllBarcode = function(){

									var barcodeList = [];
									var count = 0;
									
									angular.forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
									
									if(value.sampleBarcode  != null)
									{
										if(count == 0){
											barcodeList.push(value.orderDetailsId);
										}else{
											var barcode = ","+value.orderDetailsId;
											barcodeList.push(barcode);
										}
									}
												
								 });
									
									var data = {
											"appName": "HIS",
											"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											"reportDesignFilePath": "Pathologybarcode.rpt",
											"reportFormat": "PDF",
											"exportFileName": "Certificate",
											"reportParams": [
												{
													"paramName": "orderDetailsId",
													"paramType": "String",
													"paramValue": barcodeList.toString()

												}
												]
											};
											reportService.generate(REPORT_API_URI1,data);
									
								}
								
								
								

								 $scope.searchType = function(searchKeyword)
									{
									 
									 if(searchKeyword.length >= 3){
										try 
										{
											$rootScope.startLoader();
											$scope.phlebotomyWorkList = [];
											var searchObj = {
													
														  "orgId" : $scope.orgId,
														  "userId" : $scope.userId,
														  "orgUnitId" :$scope.orgUnitId,
														  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
														  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
														  "visitTypes" :$scope.selectedVisitType,
														  "deptId": $scope.deptId,
														  "orderStatusId": $scope.orderStatusId,
														  "priorityId" : $scope.priorityId,
														  "isPreOrPostBilling": $scope.isPreOrPostBilling,
														  "offset": $scope.offset,
														  "recordPerPage" : $scope.noOfRecordsPerPage,
														  "isCountRequired" : true,
														  "searchField":searchKeyword,
														  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
														  "deptId": $scope.deptId,
														  
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
											console.log("phlebotomyWorkList",JSON.stringify(searchObj));
											var URI = BASE_URL 
											        + ROOT_URL
											        + "/LIS/transaction/PhlebotomyWorklist/Type/Search";
											console.log("URI", URI);
											
											GenericService.serviceAction("POST", URI, searchObj)
											.then(function(response){
																$rootScope.stopLoader();
																$scope.phlebotomyWorkList = [];
																if (response.data.listObject.length > 0) {
																if($scope.isBillManipulationAllowed == true){
																	$scope.phlebotomyWorkList = response.data.listObject;
																}else{
																	
																	angular.forEach(response.data.listObject, function(value, key) 
																			{
																					if(value.realServiceCount >0){
																						$scope.phlebotomyWorkList.push(value);
																					}
																			});
																	
																}
																 } else {
												                        growl.error("No record found", {
												                            title: 'Error'
												                        });
												                    }
																$scope.commonListCount = response.data.object;
																$scope.setPage(1,false);
															});
											
										
										} catch (e) {
											console.log(e.message)
										}
										
									 }else  if(searchKeyword.length == 0){
										 $scope.searchPhlebotomyWorkList();
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

								 $scope.labSortingList = [];
									$scope.getLabSortingMasterList = function () {
										var URI = BASE_URL + ROOT_URL + "/LIS/transaction/labSortingList";
										$scope.labSortingList = [];
										GenericService.serviceAction("GET", URI).then(function (response) {
											if (response.data.status == "success") {
												if(response.data.listObject!=null && response.data.listObject!=undefined && response.data.listObject.length>0){
													angular.forEach(response.data.listObject,function(value,index){
														if(value.id != 3){ // not equal to sampleId
															$scope.labSortingList.push(value);
														}
													});
												}
											}
										});
									}
									
									$scope.getLabSortingMasterList();  
								
                                    $scope.printReport = function(orderId){
                                        var data =  
                                        {
                                                "appName": "HIS",
                                                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                                                "reportDesignFilePath": "Phlebotomyworklist.rpt",
                                                "reportFormat": "PDF",
                                                "exportFileName": "PhlebotomyWorklistprint",
                                                "reportParams": [
                                                  {
                                                     "paramName": "order_id",
                                                     "paramType": "Integer",
                                                     "paramValue": orderId
                                                  }
                                                ]
                                          };
                                          reportService.generate(REPORT_API_URI1,data);
                                    }
                                    
                                    $scope.companyName = "";
                                    $scope.getCompanyName = function(idx,workistObj){
                                    	$rootScope.startLoader();
                                    	if(workistObj.paymentEntitlementId!=null && workistObj.paymentEntitlementId!=1){
                                    		var data  = {
                                            		paymentEntitlementId : workistObj.paymentEntitlementId,
                                            		payeeId : workistObj.payeeId
                                            	}
                                            	var URI = BASE_URL + ROOT_URL + "/LIS/transaction/PhlebotomyWorklist/getCompanyOrInsuranceNameById";
                                            	  $scope.companyName = "";
        										GenericService.serviceAction("POST", URI,data).then(function (response) {
        											if (response.data.status == "success") {
        												$rootScope.stopLoader();
        												  $scope.companyName = response.data.object.companyDesc;
        											}
        										});
                                    	}else if(workistObj.paymentEntitlementId ==1 ){
                                    		 $scope.companyName = "Self";
                                    	}
                                    	$rootScope.stopLoader();
                                    }
                                    
                                    $scope.medallPrifixList =[];
                                	$scope.medallUnitObj ={};
                                	$scope.medallDoctorList =[];
                                	$scope.getMedalDdetails = function(){
                                		var data = {
                                				  "organizationId" : $scope.organizationId,
                                				  "unitId" : $scope.unitId,
                                		}
                                		var URI = BASE_URL + ROOT_URL + "/adt/getPrefixMasterAndMedallPrefixMaster";
                                		BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                			if (response.data.status == "success")
                                			{
                                				$scope.medallPrifixList =response.data.listObject;
                                				$scope.medallUnitObj =response.data.object;
                                				$scope.medallDoctorList =response.data.resObject;
                                				
                                			}
                                		});
                                	}
                                	$scope.getMedalDdetails();
                                    
                                    $scope.resendOrderToneuronPlus = function(info){	
        								var orgId=info.orgId;
        								var orgUnitId =info.orgUnitId;
        								var orderId=info.orderId;
        								$scope.admissionIdN=info.admissionId;
        								$scope.encounterIdN=info.encounterId;
        								$scope.patientIdN=info.patientId;
        								$rootScope.startLoader();
        									var data = "";
        									var URI = BASE_URL + ROOT_URL
        											+ LIS_TRANSACTION
        											+ GET_PHLEBOTOMY_WORKLIST_DETAILS
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
        														
        														if($scope.outsourceLabId ==1){
        															$scope.ordersIdListNeuron = [];
        			                								var saveNuroPlus = {};
        			                								saveNuroPlus.branchName = "NphMain";
        			                								saveNuroPlus.orderId = info.orderId.toString();
        			                								saveNuroPlus.IPOPNo = info.encounterNumber;
        			                								saveNuroPlus.UHID =  info.uhid;
        			                								saveNuroPlus.orderTs =info.orderDateString+"T"+info.orderDateTime;
        			                								if(info.visitTypeId == 2){
        																var tyy= "IPD";
        																}else if(info.visitTypeId == 3){
        																var tyy= "DC";
        																}else if(info.visitTypeId == 4){
        																var tyy= "ER";
        																}else{
        																	var tyy= "OP";
        																}
        																saveNuroPlus.ipoptype = tyy;
        			                								saveNuroPlus.salutation =  info.genderId.toString();;
        			                								saveNuroPlus.patientName =  info.firstName;
        			                								saveNuroPlus.gender = info.genderCode;
        			                							     var Birth = moment(info.birthDate,"DD/MM/YYYY").toDate();
        			                							     var Bx = moment(Birth).format('YYYY-MM-DD');
        			                								saveNuroPlus.birthDate = Bx+"T00:00:00";
        			                								saveNuroPlus.ward ="";
        			                								saveNuroPlus.patMobileNo = "";
        			                								saveNuroPlus.patEmailId = info.email;
        			                								saveNuroPlus.patAddress = info.address;
        			                								saveNuroPlus.patCity = info.cityName;
        			                								saveNuroPlus.refDrCode = info.doctorName;
        			                								saveNuroPlus.refDrName = info.doctorName;
        			                								saveNuroPlus.feesPaid = false;
        			                								saveNuroPlus.orderTests=[];
        			                								$scope.ordersIdListNeuron.push(saveNuroPlus);
		        			                				angular.forEach($scope.ordersIdListNeuron, function (value, key) {
		        			                					angular.forEach($scope.phlebotomyWorkListDetails, function (value1, key) {
		        			                						    		var newAer = {};
		        			                						    		newAer.testRecordId = value1.serviceId.toString();
		        			                						    		newAer.testName = value1.testDesc;
		        			                						    		newAer.testCode = value1.testCode;
		        			                						    		value.orderTests.push(newAer);
		        			                					});
		        			                				});
        			                					console.log("nuronlist",$scope.ordersIdListNeuron);
// return false;
        			                					angular.forEach($scope.ordersIdListNeuron, function (value, key) {
        			                						var data ={};
        			                						data=value;
        			                						var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
        			                						 var config ={
        			                								  "Access-Control-Allow-Origin":'*',
        			                								  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
        			                								  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
        			                								  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
        			                								  'accept' :'application/json, text/javascript, *',
        			                								  'content-type' :'application/json',
        			                								  'scheme' :'https',
        			                						};

        			                						$http.post(url, data, config).then(function (response) {

        			                							console.log(response);
        			                							if(response.data.statusCode==200){
        			                								var masse=response.data.message;
        			                								var arr = masse.split(' ');
        			                								var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
        			                								var data ={
        			                										"orgId" : $scope.orgId,
        																	 "orgUnitId" :$scope.orgUnitId,
        			                										"orderId":arr[3],
        			                    									"neuronPlusUrl":response.data.reportUrl,
        			                    									
        			                									}
        			                									GenericService.serviceAction("POST", URI, data).then(function (response) {
        			                										if (response.data.status == "success") 
        			                										{
        			                											$scope.searchPhlebotomyWorkList();
        			                							            }
        			                									});
        			                							}

        			                						}, function (response) {

        			                						});

        			                					});
        															}
        															if($scope.outsourceLabId == 2){	
        																
        																$scope.ordersIdListMedall= [];
        															angular.forEach($scope.phlebotomyWorkListDetails, function (value, key) {
//        																 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
//        																	{
        																	 if(info.genderId == 1){
        																		 info.genderId="0"
        																	 }else if(info.genderId == 2){
        																		 info.genderId="1"
        																	 }else{
        																		 info.genderId="2"
        																	 }
        																	 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");

        																	 var Birth = moment(info.birthDate,"DD/MM/YYYY").toDate();
        																     var Bx = moment(Birth).format('YYYY-MM-DD');
        																	 var medallObj =
        																	 {
        																		    "name": info.patientName !=null ? info.patientName :"",
        																		    "gender": info.genderId,
        																		    "mobile": info.mobileNo != null ? info.mobileNo :"",
        																		    "pincode":  info.zipCode != null ? info.zipCode :600020,
        																		    "address": info.address != null ? info.address:"",
        																		    "state": info.stateName!= null ? info.stateName:"",
        																		    "district": info.districtName != null ? info.districtName : "",
        																		    "passport": null,
        																		    "srfid": null,
        																		   // "referalid": null,
        																		    "relationid": "1",
        																		   // "salutationid": info.prefixId != null ? info.prefixId:"",
        																		    "nationality": "Indian",
        																		    "dob": Bx,
        																		    "centerid": centerid,
        																		    "appointmentid": null,
        																		    "externalid":  info.uhid !=null ?info.uhid :"",
        																		    "emailid": info.email != null ? info.email:"",
        																		    "orderid":value.orderId,
        																		    "sourceid":"10019471",
        																		    "hospitaltype":"IP",
        																		    "studyinfo": []
        																		}
        																	 
        																	 
        																	 
        																	 var prefixId = "2513";
        																	 
        																		angular.forEach($scope.medallPrifixList, function (val, inx) {
        																			
        																			if(info.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
        																				prefixId = val.medallSalutationIdPrefixId;
        																			}
        																			
        																		});
        																		
        																		medallObj.salutationid = prefixId.toString();
        																		
        																		if(info.doctorId != undefined && info.doctorId != null && info.doctorId != 0 ){
        																			
        																			var check = true;
        																			angular.forEach($scope.medallDoctorList, function (val, inx) {
        																				
        																				if(info.doctorId == val.doctorId && val.medallReferalId != null){
        																					medallObj.referalid = val.medallReferalId;
        																					check = false;
        																				}
        																				
        																			});
        																			
        																			if(check == true){
        																				medallObj.referalid = '5001195';
        																			}
        																		}else{
        																			medallObj.referalid = '5001195';
        																		}
        																	 	if($scope.ordersIdListMedall.length==0){
        																		 $scope.ordersIdListMedall.push(medallObj);
        																		}
//        															     }
        															});
        														
        															$scope.checkAutoRenderedServicesForMedall = [];
        															angular.forEach($scope.ordersIdListMedall, function (value, key) {
        															    angular.forEach($scope.phlebotomyWorkListDetails, function (value1, key1) {
        																    if(value.orderid == value1.orderId){
        																    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
        																		{
        																    		$scope.checkAutoRenderedServicesForMedall.push(value1);
        																		}
        																    }
        																});
        															});
        															
        															angular.forEach($scope.ordersIdListMedall, function (value, key) {
        															    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
        																    if(value.orderid == value1.orderId){
        																    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
        																		{
        																    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
        																	    			var newAer = {};
        																		    		newAer.billingamount="0";
        																		    		newAer.billingid="0";
        																		    		newAer.billingtype="";
        																		    		newAer.corpbillingid=(value1.medallServiceId).toString();
        																		    		newAer.discount="0";
        																		    		newAer.paidamount="0";
        																		    		newAer.topaid="0";
        																		    		newAer.extraamount="0";
        																		    		value.studyinfo.push(newAer);
        																	    		}
        																			}
        																    }
        																});
        															});
        															if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
        																if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
			        																angular.forEach($scope.ordersIdListMedall, function (value, key) {
			        																var data ={};
			        																data=value;
			        																var url ='https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';
			        																var config ={
			        																		  "Access-Control-Allow-Origin":'*',
			        																		  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
			        																		  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
			        																		  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
			        																		  'accept' :'application/json, text/javascript, *',
			        																		  'content-type' :'application/json',
			        																		  'scheme' :'https',
			        																		  headers: {
			        																		      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
			        																		    }
			        																};
			
			        																$http.post(url, data, config).then(function (response) {
			
			        																// This function handles success
			//        																	alert("success");
			        																	console.log(response);
			        																	if(response.data.status=="success"){
			        																		growl.success("success");
			        																		
			        																		var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
			        																		var data ={
			        																				encounterId:$scope.encounterDetails.encounterId,
			        																				patientId:$scope.encounterDetails.patientId,
			        																				requestedPayload:JSON.stringify($scope.ordersIdListMedall),
			        																				returnedResponse:JSON.stringify(response.data),
			        																				status:'A',
			        																				organizationId:$scope.organizationId,
			        																				unitId:$scope.unitId,
			        																				addedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			        																				addedBy:$scope.userId,
			        																				updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			        																				updatedBy:$scope.userId
			        																			}
			        																			GenericService.serviceAction("POST", URI, data).then(function (response) {
			        																				if (response.data.status == "success") 
			        																				{
			        																	            }
			        																			});
			        																	}else{
			        																		growl.warning(response.data.status);
			        																	}
			        																}, function (response) {
			
			        																});
			
			        															});
        																}else{
        																	growl.warning("Service is not mapped with Medall.");
        																	return false;
        																}
        															}else{
        																growl.warning("Service is not mapped with Medall.");
        																return false;
        															}
        														}
        													}
        												});
        										
                				             }
                                   
						} ]);
