
/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"pendinglistForReportUploadController",
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
						'BillingGenericService',
						'GenericServiceFileUpload',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,BillingGenericService,GenericServiceFileUpload) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "pendinglistForSampleDispatchController";
							
							$scope.firstLevel = true;
							$scope.secondLevel = true;
							$scope.thirdLevel = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultOutSourcePendingListController  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.labResultTestDtls;
							
							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
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
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = 0;
							
							
							$scope.listHelpValueMaster = [];
							
							$scope.listOutSourceMasterDto  = [];
							

							$scope.visitTypeId = 1;
							$scope.selectedVisitTypeIds = [1,2];
							$scope.sampleStatusIds=[2];
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];



							/**DatePicker Code  */
						
						$scope.fromDate = moment().startOf('day').toDate();
					    $scope.toDate = moment().startOf('day').toDate();
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

							
							$scope.outSourceMasterDto = {
									"outSourcedId": "",
						            "orgId": $scope.orgId,
						            "orgUnitId":$scope.orgUnitId,
						            "testId": "",
						            "patientId": "",
						            "visitTypeId": "",
						            "orderId": "",
						            "sampleNo": "",
						            "labSampleDtlsId": "",
						            "createdBy": $scope.userId,
						            "createdDate": "",
						            "updatedBy": $scope.userId,
						            "updatedDate": "",
						            "labSampleNo": "",
						            "visitTypeCode": "",
						            "containerName": "",
						            "uhid": "",
						            "patientDetails": "",
						            "doctorDetails": "",
						            "testDesc": "",
						            "deptName": "",
						            "priorityName": "",
						            "colorCode": "",
						            "sampleStatusId": "",
						            "uploadDoc": "",
						            "notes": "",
						            "status": "",
						            "recordPerPage": "",
						            "accPayableMstId":"",
						            "sampleStatus": "",
						            "listOutSourceDetailMasterDto":[]
							}
							
							$scope.OutSourceDetailMasterDto = {
									"outSourceDetailId": "",
						            "orgId": $scope.orgId,
						            "orgUnitId":$scope.orgUnitId,
						            "resource": "",
						            "sampleSendThroughId": "",
						            "sampleSendThrough":"",
						            "courierNumber": "",
						            "remarks": "",
						            "expectedDate": "",
						            "expectedStringDate":"",
						            "outSourcedId": "",
						            "createdBy": $scope.userId,
						            "createdDate": "",
						            "updatedBy": $scope.userId,
						            "updatedDate": "",
						            "status": "",
						            
							}

							
						
							$scope.setNoOfRecords = function() {
								$scope.initOutSourcePendingList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initOutSourcePendingList = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit ,
											"assignUnit" :$scope.orgUnitId ,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"sampleStatusId":"2"
											
									 
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit ,
											"assignUnit" :$scope.orgUnitId ,
											"sampleStatusId":"2"
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_OUT_SOURCE_LAB
											+ LIST_PENDING_LIST_FOR_SAMPLE_DISPATCH;


									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_OUT_SOURCE_LAB
											+ LIST_PENDING_LIST_FOR_SAMPLE_DISPATCH_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultOutSourceMasterList = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.resultOutSourceMasterListForFiltter = $scope.resultOutSourceMasterList;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
														
													});
									
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initOutSourcePendingList($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
									$scope.noOfRecordsPerPage);
				

							
							$scope.clearFields = function()
							{
							
							    $scope.outSourceLabName = '';
								$scope.patientId = undefined;
								$scope.selectedPatient = null;
								$scope.selectedVisitType = [];
								$scope.orderStatusId = '';
								$scope.priorityId = '';
								$scope.uhid = '';
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
							    $scope.initOutSourcePendingList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};
							
							$scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") {
											$scope.documentTypeList = response.data.listObject;											 
										}
									});
							}
							$scope.getDocumentType();
							$scope.getOutSourcePendingList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit ,
											"assignUnit" :$scope.orgUnitId ,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"sampleStatusId":"2"
										};
									var URI = BASE_URL 
							        + ROOT_URL
									+ LIS_OUT_SOURCE_LAB
									+ LIST_PENDING_LIST_FOR_SAMPLE_DISPATCH;

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultOutSourceMasterList  = response.data.listObject;
														$scope.resultOutSourceMasterListForFiltter = $scope.resultOutSourceMasterList;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.filterByDummyServices= function()
                            {
								$scope.resultOutSourceMasterList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.resultOutSourceMasterList = angular.copy($scope.resultOutSourceMasterListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.resultOutSourceMasterListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.resultOutSourceMasterList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.resultOutSourceMasterListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.resultOutSourceMasterList.push(value);
                                              });
                                               
                                }
                            }
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular.forEach($scope.resultOutSourceMasterList,
												function(resultEntry)
												{
									              resultEntry.printReport = $scope.selectedAll;
												});
							}

							
							$scope.getOutSourceDetailsByOutSourceId = function(outSourcedId) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"outSourcedId":outSourcedId
										};
									var URI = BASE_URL 
							        + ROOT_URL
									+ LIS_OUT_SOURCE_LAB
									+ GET_OUT_SOURCE_DETAILS_BY_OUT_SOURCE_ID;

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultOutSourceDetails= response.data.object;
														console.log("$scope.resultOutSourceDetails", JSON
																.stringify($scope.resultOutSourceDetails));
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.checkSample = function()
							{
								listOutSourceMasterDto = [];
								angular
								.forEach(
										  $scope.resultOutSourceMasterList,
										function(value,key) 
										{
											if(value.printReport==true)
												{
												var outSourceMasterDto = angular
														.copy($scope.outSourceMasterDto);
												outSourceMasterDto.patientId =value.patientId;
												outSourceMasterDto.doctorId=value.doctorId;
												outSourceMasterDto.visitTypeId=value.visitTypeId;
												outSourceMasterDto.testId=value.testId;
												outSourceMasterDto.uploadDoc=value.uploadDoc;
												outSourceMasterDto.notes=value.notes;
												outSourceMasterDto.createdBy = $scope.userId;
												outSourceMasterDto.orgId=$scope.orgId;
												outSourceMasterDto.orgUnitId=$scope.orgUnitId;
												outSourceMasterDto.orderId =value.orderId;
												outSourceMasterDto.labSampleDtlsId=value.labSampleDtlsId;	
												outSourceMasterDto.status=ACCEPT;
												outSourceMasterDto.labSampleNo=value.labSampleNo;
												outSourceMasterDto.sampleStatus=3;
												listOutSourceMasterDto
														.push(outSourceMasterDto);
										     }
										});
								
								if(listOutSourceMasterDto.length==0)
								{
									growl.warning('Please select sample.',
											{
												title : 'Warning!'
											});
									  return false;
								}
								else{
									$("#outSourcUploadLab").modal('show');
								 }
							}
							
							$scope.saveDetails = function() {
						
								$rootScope.startLoader();
								angular
								.forEach(
										$scope.resultOutSourceMasterList,
										function(value,key) 
										{
											if(value.printReport==true)
												{
											
										var outSourceMasterDto = angular
												.copy($scope.outSourceMasterDto);
										outSourceMasterDto.patientId =value.patientId;
										outSourceMasterDto.doctorId=value.doctorId;
										outSourceMasterDto.visitTypeId=value.visitTypeId;
										outSourceMasterDto.outSourcedId = value.outSourcedId;
										outSourceMasterDto.testId=value.testId;
										outSourceMasterDto.uploadDoc=value.uploadDoc;
										outSourceMasterDto.notes=value.notes;
										outSourceMasterDto.createdBy = $scope.userId;
										outSourceMasterDto.orgId=$scope.orgId;
										outSourceMasterDto.orgUnitId=$scope.orgUnitId;
										outSourceMasterDto.orderId =value.orderId;
										outSourceMasterDto.labSampleDtlsId=value.labSampleDtlsId;	
										outSourceMasterDto.status=ACCEPT;
										outSourceMasterDto.labSampleNo=value.labSampleNo;
										outSourceMasterDto.sampleStatus=3;
										outSourceMasterDto.textData = value.textData;
										outSourceMasterDto.patientEmail = value.patientEmail;
										$scope.listOutSourceMasterDto.push(outSourceMasterDto);
										     }
										});
							
								
								var URI = BASE_URL + ROOT_URL
										+ LIS_OUT_SOURCE_LAB + ADD_OUT_SOURCE_MST_LAB;

								try {
									GenericService
											.serviceAction("POST", URI,
													$scope.listOutSourceMasterDto)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															$scope.initOutSourcePendingList($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.offset,
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
							
							
							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getOutSourcePendingList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
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
													        + LIS_OUT_SOURCE_LAB
															+ OUT_SOURCE_LIST_PATIENT_LIST_SEARCH;
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
											
											
											 $scope.searchOutPatientList = function()
												{
													try 
													{
														$scope.selectedAll = false;
														$rootScope.startLoader();
														var searchObj = {
																	  "orgId" : $scope.orgId,
																	  "orgUnitId":$scope.assignUnit ,
																	  "assignUnit" :$scope.orgUnitId ,
																	  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
																	  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
																	  "patientId" :  $scope.selectedPatient,
																	  "uhidNumber" : ($scope.uhid != undefined && $scope.uhid != null) ?$scope.uhid:"",
																	  "labSampleDtlsId" : $scope.labSampleDtlsId,
																	  "visitTypes" :$scope.selectedVisitType,
																	  "testTypes" : $scope.selectedtestList,
																	  "deptId" : $scope.deptId,
																	  "subDeptId" : $scope.subDeptId,
																	  "subDepts" : $scope.selectedDepartMentList,
																	  "sampleStatusId" :2,
																	  "outSourceLabName":$scope.outSourceLabName
																	 
																	
							                             }
														
														var URI = BASE_URL 
														        + ROOT_URL
														        + LIS_OUT_SOURCE_LAB
																+ '/OutSourceCommonList/Search'
														console.log("URI", URI);
														 GenericService.serviceAction("POST", URI,
																 searchObj).then(
																		 function(response) {
																			 $scope.resultOutSourceMasterList=[];
																				if (response.data.status == 'success') {
																					$rootScope.stopLoader();
																					
																					$scope.resultOutSourceMasterList =  response.data.listObject;
																					$scope.resultOutSourceMasterListForFiltter = $scope.resultOutSourceMasterList;
																					$scope.filterByDummyServices();
																				} else if (response.data.status == 'error') {
																					growl
																							.error(
																									response.data.message,
																									{
																										title : 'Error!'
																									});
																					$rootScope.stopLoader();
																					$scope.resultOutSourceMasterList=[];
																				}
																			});
													} catch (e) {
														console.log(e.message)
													}
												}
											 
											 /**==========================================UPLOAD IMAGES START===================================*/
											 $scope.getPatientDocumentList = function(labSampleDtlsId)
										        {
										         $rootScope.startLoader();
										         var data =
										           {
										           "unitId":$scope.unitId,
										           "organisationId":$scope.organizationId,
										           "labSampleDtlsId":labSampleDtlsId,
										          }
										         var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
										         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										           $scope.patientDocumentList = [];
										           $scope.patientCorporateDocumentList = [];
										           if (response.data.status == "success") 
										           {
										            $scope.patientDocumentList = response.data.listObject;				            
										        
										            $rootScope.stopLoader();
										           }
										           else if(response.data.status == "error")
										            {
										            $rootScope.stopLoader();
										            }
										          });
										        }
											
											 $scope.addImageDetails = function()
												{ 
													var encounterDetails = angular.copy($scope.encounterDetails);
													$scope.encounterDetailsList.push(encounterDetails);
												};
												
												 $scope.removeImageDetails = function(deleteIndex)
													{ 
													   $scope.encounterDetailsList.splice(deleteIndex, 1);
												    }	
												 
												 
												 $scope.uploadEncounterDoc = function(Obj)
													{
														$('#documentUploadModal123').modal('hide');
														 var fd = new FormData();
													        fd.append('encounterId', 0);
													        fd.append('visitTypeId', Obj.visitTypeId);
													        fd.append('admissionId', 0);
													        fd.append('patientId',0 );
													        fd.append('documentTypeId', Obj.documentTypeId);
													        fd.append('createdBy',Obj.createdBy );
													        fd.append('updatedBy', Obj.updatedBy);
													        fd.append('unitId', Obj.unitId);
													        fd.append('organisationId', Obj.organisationId);
													        fd.append('labSampleDtlsId', Obj.labSampleDtlsId);
													        fd.append('file',Obj.file );
													        fd.append('expenseId', 0);
													        fd.append('grnId',0);
															fd.append('poId',0);
													        fd.append('incidentReportingMapperId',0);
													        fd.append('companyExpenseId',0);
													        fd.append('createdDate',null);
															fd.append('fileEditedName',null);
															fd.append('opbId',0);
															fd.append('vendorId',0);
													        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
													        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
															{	
																return response;
															});
													}
													 $scope.uploadDoc = function()
													 
													    {
														 $rootScope.startLoader();
														  $scope.finalDocUploadList=[];
														  angular.forEach($scope.encounterDetailsList, function(value, key) {  
															  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
																	 growl.warning("Please select document type .",
																								{
																									title : 'warning!'
																								});
																					   $rootScope.stopLoader();
																	return false;
																}
																  if (angular.equals(value.file, {}) || value.file==undefined) {
																		growl.warning('Please select file.',
																						{
																							title : 'warning!'
																						});
																		 $rootScope.stopLoader();
																		return false;
																	} 
																 var ext = value.file.name.split(".").pop().toLowerCase();
																	if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
																		growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
																				{
																					title : 'warning!'
																				});
																		$rootScope.stopLoader();
																		return false;
																	}
																	$scope.finalDocUploadList.push(value);
														});
														  if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
														  $scope.count = 0;
														 angular.forEach($scope.finalDocUploadList, function(value, key) 
															{  
																var promise = $scope.uploadEncounterDoc(value);
																promise.then(function(response) 
																		{
																		  if(response.data.status == "success")
																			  {
																			    $scope.count++;
																			    if($scope.encounterDetailsList.length==$scope.count)
																				 {
																				   growl.success("Document Uploaded Successfully.",
																							{
																								title : 'Success!'
																							});
																				   $rootScope.stopLoader();
																				   $scope.encounterDetailsList = [];
																				   $scope.searchOutPatientList();
																				 }
																		      }
																		  else 
																			{
																				growl.error('Something went wrong.',
																						{
																							title : 'Error!'
																						});
																				 $rootScope.stopLoader();
																			}
																	    });
																	
															});
														  }
														
													    }
														
													 
													 
														$scope.openImage = function(index)
								                              { 
																$rootScope.startLoader();
																var imageUrl = $scope.patientDocumentList[index].documentPath;
																var href = $rootScope.currentHrefDoccument();
																$.fancybox.open({
																	width: 600, // or whatever
																	height: 320,
																	href:href+imageUrl,
																	type: 'iframe'
																});
								                         $rootScope.stopLoader();
								                        }
														$scope.openDocument = function(obj)
														{
															$('#file11').val(undefined);
															$scope.encounterDetails = {
																	  encounterDocId : "",
																	  encounterId : "",
																	  admissionId:"",
																	  visitTypeId:2,
																	  patientId : obj.patientId,
																	  documentPath : "",
																	  documentTypeId : "27",
																	  status : 'A',
																	  createdDate : "",
																	  createdBy : $scope.userId,
																	  updatedDate : "",
																	  updatedBy : $scope.userId,
																	  unitId : $scope.unitId,
																	  organisationId : $scope.organizationId,
																	  labSampleDtlsId : obj.labSampleDtlsId,
																	  file : {}
																	}
															$scope.encounterDetailsList =[];
															var encounterDetails = angular.copy($scope.encounterDetails);
//															encounterDetails.patientId = obj.patientId;
															$scope.encounterDetailsList.push(encounterDetails);
															$('#documentUploadModal123').modal('show');
															$scope.getPatientDocumentList(obj.labSampleDtlsId);
														}
														
														$scope.openDocumentList = function(obj)
														{
															
															$('#documentUploadModalList').modal('show');
															$scope.getPatientDocumentList(obj.labSampleDtlsId);
														}
											 // upload images end
														$scope.currentIdx = "";
														$scope.getCurrentIndex=function(index){
															$scope.currentIdx = index;
														}
														
														$scope.getClassForColor = function(info)
									                    {
									                        	   if(info.priorityId==2){
							                                 		return 'td-bg-red';
									                        	   }
									                    }
														
														//for report send
														 $scope.patientName = "";
							  							 $scope.mailSendFlagPrint = null;
							  							 $scope.patientVisit = null;
							  							 $scope.paymentEntil = null;
							  							 $scope.BlockReportCompany = null;
														$scope.checkPrint= function(mailSendFlag)
														
														{
															 $scope.mailSendFlagPrint = mailSendFlag;
															 $scope.sendMailButton = false;
															 $scope.checkReport = false;
															 $scope.patientName = "";
							    							 var countFlag = false;
															$scope.patientList = [];
															var groupBy = function(xs, key) {
																  return xs.reduce(function(rv, x) {
																    (rv[x[key]] = rv[x[key]] || []).push(x);
																    return rv;
																  }, {});
																};

															   var checkList = [];	
																angular
																.forEach(
																		$scope.resultOutSourceMasterList,
																		function(resultEntryMaster,key) 
																		{
																			if(resultEntryMaster.printReport==true)
																				{
																				  checkList.push(resultEntryMaster);
																				  if(resultEntryMaster.statusCount == 0){
																					$scope.checkReport = true;
																					  
																				 }
																				}
																			
																		});
																if($scope.checkReport == true){
																	growl
																	.warning(
																			"Report has not uploaded.",
																			{
																				title : 'Warning!'
																			});
																	 return false;	
																}
																if(checkList.length==0)
																	{
																	growl
																	.warning(
																			"Please select test to Print.",
																			{
																				title : 'Warning!'
																			});
																	 return false;
																	}
																else{
																	
																	
																$scope.patientList =groupBy(checkList, 'patientId');
																if($scope.patientList!=null)
																	{
																	angular
																	.forEach(
																			$scope.patientList,
																			function(value,key) 
																			{
																				var subDeptList =groupBy(value, 'subDeptId');
																				value.subDeptList = subDeptList;
																				angular.forEach(value.subDeptList,function(value1,key) 
																						{
																							var labSampleIds=[];
																							var labResIds=[];
																							angular.forEach(value1,function(v,key) 
																									{
																										if(v.labTestResId!=null)
																											{
																											  labResIds.push(v.labTestResId);
																											}
																										if(v.labSampleDtlsId!=null)
																											{
																											 labSampleIds.push(v.labSampleDtlsId);
																											}
																									});
																							value1.labSampleIds = labSampleIds;
																							value1.labResIds = labResIds;
																						});
																			});
																	  }
																
																	var  i = 0 ;
																	 angular.forEach($scope.patientList,function(resultEntryMaster,key) 
										  										{
																		
																		 var  inLoop = 0;
																		 var pName = ""; 
									    								 		angular.forEach(resultEntryMaster,function(val,kye) 
																					{ 
									    								 			 $scope.patientVisit = val.visitTypeId;
									    								 			 $scope.paymentEntil = val.paymentEntitlementId;
									    				  							 $scope.BlockReportCompany = val.blockReportPrint;
											  											if(val.billSettled == false)
											  												{
												  												countFlag = true;
												  												inLoop = 1;
												  												pName = val.patientDetails
											  												}
																					});
									    								 		
									    								 		if(inLoop == 1){
								  													
								  													if(i == 0){
									  													$scope.patientName = pName; 
									  													i = 1;
									  												}else{
									  													
									  													$scope.patientName = $scope.patientName+", "+pName; 
									  												}
								  												}
									    								 		
										  										});
									    							 if(mailSendFlag == 0){
									    								 
									    								 	if(countFlag == true){
										    								 
									    								 	if($scope.paymentEntil == 1){
									    								 		
									    								 		if($scope.blockReportPrint!= null && $scope.blockReportPrint != "" && $scope.blockReportPrint == 'Y' && $scope.patientVisit == 1 ){
									                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
									                                                     
									                                                	 $('#resourceNotAvailable').modal('show');

									                                                 }else{
									                                                 	growl .warning( "You do not have rights to print the report.",
																					            {
																						            title : 'Warning!'
																					               });
																			                 return false;
									                                                 }
									                                            }else{
									                                            	$('#printReport').modal('show');
									                                            }
									    								 		
									    								 	}else{
									    								 		
									    								 		if($scope.BlockReportCompany!= null && $scope.BlockReportCompany != "" && $scope.BlockReportCompany == 'Y' && $scope.patientVisit == 1 ){
									                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
									                                                     
									                                                	 $('#resourceNotAvailable').modal('show');

									                                                 }else{
									                                                 	growl .warning( "You do not have rights to print the report.",
																					            {
																						            title : 'Warning!'
																					               });
																			                 return false;
									                                                 }
									                                            }else{
									                                            	$('#printReport').modal('show');
									                                            }
									    								 		
									    								 	}	
										    								 
										    								 
										    							 }else{
										    								 $('#printReport').modal('show');
										    								 
										    							 }
									    							 }else{
									    								 
									    								 if(countFlag == true){
										    								 if($scope.paymentEntil == 1){
										    									 
										    									 if($scope.blockReportPrint!= null && $scope.blockReportPrint != "" && $scope.blockReportPrint == 'Y' && $scope.patientVisit == 1 ){
									                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
									                                                     
									                                                	 $('#blockReport').modal('show');

									                                                 }else{
									                                                 	growl .warning( "You do not have rights to send the report email.",
																					            {
																						            title : 'Warning!'
																					               });
																			                 return false;
									                                                 }
									                                            }else{
									                                            	$('#reportSendOnMail').modal('show');
									                                            }
										    									 
										    								 }else{
										    									 
										    									 if($scope.BlockReportCompany!= null && $scope.BlockReportCompany != "" && $scope.BlockReportCompany == 'Y' && $scope.patientVisit == 1 ){
									                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){
									                                                     
									                                                	 $('#blockReport').modal('show');

									                                                 }else{
									                                                 	growl .warning( "You do not have rights to send the report email.",
																					            {
																						            title : 'Warning!'
																					               });
																			                 return false;
									                                                 }
									                                            }else{
									                                            	$('#reportSendOnMail').modal('show');
									                                            }
										    								 }
										    								 
										    								 
										    							 }else{
										    							 	$('#reportSendOnMail').modal('show');
										    							 }
									    							 }
																	 
																   }
														}
														
														
														// send report on mail
														 $scope.sendReport = function(){
															 $scope.sendMailButton = true;
															 $scope.newList = [];
															 $rootScope.startLoader();
																angular.forEach($scope.patientList,function(patient,patientId) 
																		{
																				 var newData = {};
																				  var count = 0 ;
																				   $scope.labSampDetailList = [];
																	 			angular.forEach(patient,function(val,kye) 
																				{
																					$scope.labSampDetailList.push(val.labSampleDtlsId);
																					if(count == 0 ){
																						newData.testDesc =  val.testDesc; 
																					}else{
																						 newData.testDesc =  newData.testDesc +","+val.testDesc;
																					}
																					if(val.patientId!=null){
																						newData.patientId = val.patientId;
																					}
																		 			newData.orgId =  val.orgId;
																		 			newData.orgUnitId =  val.orgUnitId;
																		 			newData.doctorId =  val.doctorId;
																		 			newData.createdBy =  $scope.userId;
																		 			 if(val.paymentEntitlementId!=null && val.paymentEntitlementId == 2){
																            			  newData.insuranceId = val.payeeId;
																            			  newData.companyId = null;
																					     }else if(val.paymentEntitlementId!=null && val.paymentEntitlementId == 3){
																					    	 newData.companyId = val.payeeId;
																					    	 newData.insuranceId = null;
																					     }else{
																					    	 newData.companyId = null;
																					    	 newData.insuranceId = null;
																					    }
																		 			count++;
																				});

																  					newData.labSampleDtlsIds = $scope.labSampDetailList;
																  					newData.newWhatsAppNo = $scope.newWhatsAppNumber;
																  					newData.sendOnlyWhatsApp = $scope.sendOnlyWhatsApp;
													 			                    $scope.newList.push(newData);
																	 			
																		});		
															
																
																  console.log("$scope.newList",$scope.newList);
															 
															 var URI = BASE_URL + ROOT_URL + "/LIS/OutSource/sendOutsourceReportOnMail";
																GenericService.serviceAction("POST",URI, $scope.newList).then(function(response){
																	if (response.data.status == 'success')
																	{
																		 $scope.sendMailButton = false;
																		 $scope.sendOnlyWhatsApp = false;
																		if(response.data.message!=null){
																			growl.warning(response.data.message,
																					{
																						title : 'warning!'
																					});
																		}else{
																			growl.success(response.data.status,
																					{
																						title : 'Success!'
																					});
																		}
																		 
																	} 
																	 $scope.sendMailButton = false;
																	 $scope.sendOnlyWhatsApp = false;
																	$rootScope.stopLoader();
																	$scope.initOutSourcePendingList($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
														
																});
														 }
														 
														 $scope.remove = function(encounterDocId,ndex) {
																try {
																	$rootScope.startLoader();
																	var data = {
																			"encounterDocId":encounterDocId
																		};
																	var URI = BASE_URL + ROOT_URL+"/api/opd/patientDoc/status/update";

																	GenericService
																			.serviceAction(METHOD_POST, URI, data)
																			.then(
																					function(response) {
																						$rootScope.stopLoader();
																						if(response.data.status == "success"){
																							growl.success("Document removed successfully ",
																									{
																										title : 'Success!'
																									});
																						}
																						
																						$scope.patientDocumentList.splice(ndex,1);
																						
																						$scope.initOutSourcePendingList($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
																					});
																} catch (e) {
																	console.log(e.message)
																}

															}

                                           $scope.newWhatsAppNumber = '';
                                           $scope.whatsAppObj = {};
                                           $scope.sendOnlyWhatsApp = false;
                                           $scope.openPopupForWhatsApp = function(resultEntryMaster){
                                                $scope.checkReport = false;
                                                $scope.patientName = "";
                                                $scope.whatsAppObj = resultEntryMaster;
                                                var countFlag = false;
                                                $scope.patientList = [];
                                                var groupBy = function(xs, key) {
                                                    return xs.reduce(function(rv, x) {
                                                        (rv[x[key]] = rv[x[key]] || []).push(x);
                                                        return rv;
                                                    }, {});
                                                };


                                                var checkList = [];
//                                                angular.forEach($scope.resultOutSourceMasterList,function(resultEntryMaster,key)
//                                                {
                                                if($scope.whatsAppObj.printReport==true)
                                                {
                                                    checkList.push($scope.whatsAppObj);
                                                    if($scope.whatsAppObj.statusCount == 0){
                                                        $scope.checkReport = true;
                                                    }
                                                }

//                                                });
                                                if($scope.checkReport == true){
                                                    growl.warning("Report has not uploaded.",
                                                    {
                                                        title : 'Warning!'
                                                    });
                                                    return false;
                                                }
//                                                if(checkList.length==0)
//                                                {
//                                                    growl.warning("Please select test to Print.",
//                                                    {
//                                                        title : 'Warning!'
//                                                    });
//                                                    return false;
//                                                }
//                                                else if(checkList.length === 1){
                                                    $scope.patientList =groupBy(checkList, 'patientId');
                                                    if($scope.patientList!=null)
                                                    {
                                                        angular.forEach($scope.patientList,function(value,key)
                                                        {
                                                            var subDeptList =groupBy(value, 'subDeptId');
                                                            value.subDeptList = subDeptList;
                                                             $scope.newWhatsAppNumber = '+91'+ $scope.whatsAppObj.patientMobileNumber;
                                                            angular.forEach(value.subDeptList,function(value1,key)
                                                            {
                                                                var labSampleIds=[];
                                                                var labResIds=[];
                                                                angular.forEach(value1,function(v,key)
                                                                {
                                                                    if(v.labTestResId!=null)
                                                                    {
                                                                        labResIds.push(v.labTestResId);
                                                                    }
                                                                    if(v.labSampleDtlsId!=null)
                                                                    {
                                                                        labSampleIds.push(v.labSampleDtlsId);
                                                                    }
                                                                });

                                                                value1.labSampleIds = labSampleIds;
                                                                value1.labResIds = labResIds;
                                                            });
                                                        });
                                                    }

                                                    var  i = 0 ;
                                                    angular.forEach($scope.patientList,function(resultEntryMaster,key)
                                                    {

                                                        var  inLoop = 0;
                                                        var pName = "";
                                                        angular.forEach(resultEntryMaster,function(val,kye)
                                                        {
                                                            $scope.patientVisit = val.visitTypeId;
                                                            $scope.paymentEntil = val.paymentEntitlementId;
                                                            $scope.BlockReportCompany = val.blockReportPrint;
                                                            if(val.billSettled == false)
                                                            {
                                                                countFlag = true;
                                                                inLoop = 1;
                                                                pName = val.patientDetails
                                                            }
                                                        });

                                                        if(inLoop == 1){

                                                            if(i == 0){
                                                                $scope.patientName = pName;
                                                                i = 1;
                                                            }else{

                                                                $scope.patientName = $scope.patientName+", "+pName;
                                                            }
                                                        }
                                                    });

                                                     if(countFlag == true){
                                                         if($scope.paymentEntil == 1){

                                                             if($scope.blockReportPrint!= null && $scope.blockReportPrint != "" && $scope.blockReportPrint == 'Y' && $scope.patientVisit == 1 ){
                                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){

                                                                     $('#blockReport').modal('show');

                                                                 }else{
                                                                    growl .warning( "You do not have rights to send the report email.",
                                                                            {
                                                                                title : 'Warning!'
                                                                               });
                                                                         return false;
                                                                 }
                                                             }else{
//                                                                $scope.newWhatsAppNumber = '+91'+$scope.selectedPatient.mobileNo;
                                                                $('#manualWhatsAppConfirmation').modal('show');
                                                             }

                                                         }else{

                                                             if($scope.BlockReportCompany!= null && $scope.BlockReportCompany != "" && $scope.BlockReportCompany == 'Y' && $scope.patientVisit == 1 ){
                                                                 if($scope.enableBlockReport != null && $scope.enableBlockReport == true){

                                                                     $('#blockReport').modal('show');

                                                                 }else{
                                                                    growl .warning( "You do not have rights to send the report email.",
                                                                            {
                                                                                title : 'Warning!'
                                                                               });
                                                                         return false;
                                                                 }
                                                            }else{
//                                                                $scope.newWhatsAppNumber = '+91'+$scope.selectedPatient.mobileNo;
                                                                $('#manualWhatsAppConfirmation').modal('show');
                                                            }
                                                         }

                                                     }else{
//                                                        $scope.newWhatsAppNumber = '+91'+$scope.selectedPatient.mobileNo;
                                                        $('#manualWhatsAppConfirmation').modal('show');
                                                     }
//                                                }
//                                                else{
//                                                    growl .warning( "Please select only one test at a time",
//                                                    {
//                                                        title : 'Warning!'
//                                                    });
//                                                    return false;
//                                                }
//                                               if($scope.selectedPatient.patientId !== undefined && $scope.selectedPatient.patientId !== null){
//                                                   $scope.newWhatsAppNumber = '+91'+$scope.selectedPatient.mobileNo;
//                                                   $("#manualWhatsAppConfirmation").modal("show");
//                                               }
//                                               else{
//                                                   growl.warning("Please select patient!",
//                                                   {
//                                                       title : 'Warning!'
//                                                   });
//                                               }
                                           }

                                           $scope.sendWhatsAppManually = function(){

                                               if($scope.newWhatsAppNumber !== undefined && $scope.newWhatsAppNumber !== null && $scope.newWhatsAppNumber !== ""){

                                                   if ($scope.newWhatsAppNumber.includes("+")) {
                                                       $scope.newWhatsAppNumber =  $scope.newWhatsAppNumber.replace("+","");
                                                   }else{
                                                          $scope.newWhatsAppNumber =  $scope.newWhatsAppNumber;
                                                   }

                                                   if($scope.newWhatsAppNumber.length >=10 && $scope.newWhatsAppNumber.length <= 12){
                                                       if($scope.newWhatsAppNumber.length === 10){
                                                          $scope.newWhatsAppNumber = "91"+$scope.newWhatsAppNumber;
                                                       }
                                                       else if($scope.newWhatsAppNumber.length === 11){
                                                           growl.warning("Enter valid whatsapp number",
                                                           {
                                                               title : 'Warning!'
                                                           });
                                                       }
                                                        $scope.sendOnlyWhatsApp = true;
                                                       $scope.sendReport();
                                                   }
                                                   else{
                                                       growl.warning("Enter valid whatsapp number",
                                                       {
                                                           title : 'Warning!'
                                                       });
                                                   }
                                               }
                                               else{
                                                   growl.warning("Enter valid whatsapp number",
                                                   {
                                                       title : 'Warning!'
                                                   });
                                               }
                                           };
														 
														 
						} ]);