
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"microbiologyWorklistController",
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
							$scope.microbiologyWorklist = [];
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
							$scope.isAcceptanceInDepartment = labConfig.isAcceptanceInDepartment;
						    $scope.subDeptId = labConfig.microbiologyId;
						    $scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
							$scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
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

							$scope.listTMicroSampleMediaDto = [];
							$scope.tMicroSampleMediaDto = {
									  "tSampleMediaId" :"",
									  "labSampleDtlsId" :"",
									  "orgId" :  $scope.orgId,
									  "orgUnitId" :  $scope.orgUnitId,
									  "mediaId" :  "",
									  "remark":"",
									  "isSelected" :  "",
									  "desc" :  "",
									  "createdBy" :  $scope.userId,
									  "createdDate" :  "",
									  "updatedBy" :  "",
									  "updatedDate" :  "",
									  "isDeleted" :  "N"
									}
							
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
								$scope.initMicrobiologyWorkList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initMicrobiologyWorkList = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"isSendToCrApplicable":$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"isSendToCrApplicable":$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_MICROBIOLOGY
											+ MICROBIOLOGY_WORKLIST;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_MICROBIOLOGY
											+ MICROBIOLOGY_WORKLIST_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.microbiologyWorklist = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.microbiologyWorklistForFiltter = $scope.microbiologyWorklist;
														$scope.filterByDummyServices();
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
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
								$scope.initMicrobiologyWorkList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.initMicrobiologyWorkList($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
									$scope.noOfRecordsPerPage);

				

							$scope.getMicrobiologyWorkList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"isSendToCrApplicable":$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									var URI = BASE_URL + ROOT_URL
									              + LIS_MICROBIOLOGY
									              + MICROBIOLOGY_WORKLIST;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.microbiologyWorklist = response.data.listObject;
														$scope.microbiologyWorklistForFiltter = $scope.microbiologyWorklist;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
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
									$scope.getMicrobiologyWorkList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							
							$scope.filterByDummyServices= function()
                            {
								
								$scope.microbiologyWorklist  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.microbiologyWorklist = angular.copy($scope.microbiologyWorklistForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.microbiologyWorklistForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.microbiologyWorklist.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.microbiologyWorklistForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.microbiologyWorklist.push(value);
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
								angular
										.forEach(
												$scope.microbiologyWorklist,
												function(labDetailsObj) {
													labDetailsObj.sampleStatusId = $scope.selectedAll
												});
							 }
							
							/** code for setting all checkboxes */
							$scope.checkAllMedia = function() {
								if ($scope.selectedAllMedia) {
									$scope.selectedAllMedia = true;
								} else {
									$scope.selectedAllMedia = false;
								}
								angular
										.forEach(
												$scope.listTMicroSampleMediaDto,
												function(mediaSampleObj) {
													mediaSampleObj.isSelected = $scope.selectedAllMedia;
												});
							}
							
							
							
							$scope.getMediaListBySampleId = function(labSampleDtlsId,sampleId)
							{
								try{
									$rootScope.startLoader();
									$scope.listTMicroSampleMediaDto = [];
									var data = "";
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ MEDIA_LIST_BY_SAMPLEID+S+sampleId+S+'orgId'+S+$scope.orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) 
													{
														if(response.data.status =="success")
															{
															$scope.mediaList = response.data.listObject;
															console.log("$scope.mediaList",$scope.mediaList);
															
															if($scope.mediaList!=null && $scope.mediaList.length>0)
																{
																angular.forEach($scope.mediaList, function(mediaObj, key) {
																	 var tMicroSampleMediaDto = angular.copy($scope.tMicroSampleMediaDto);
																	 tMicroSampleMediaDto.labSampleDtlsId = labSampleDtlsId;
																	 tMicroSampleMediaDto.mediaId = mediaObj.id;
																	 tMicroSampleMediaDto.desc = mediaObj.name;
																	 $scope.listTMicroSampleMediaDto.push(tMicroSampleMediaDto);
																	 $("#mediaDetailsAddModal").modal('show');
																	});
																 $rootScope.stopLoader();
																}
															else{
																growl.error("No media is Mapped with Sample.",
																		{
																			title : 'Error!'
																		});
																$rootScope.stopLoader();
															    }
															}
														else if(response.data.status =="error")
															{
															  $rootScope.stopLoader();
															}
														
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.saveIncubationMedia = function()
							{
								try{
									 $("#mediaDetailsAddModal").modal('hide');
								     $rootScope.startLoader();
									var data = "";
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ ADD_INCUBATION_MEDIA;
									
									var tempTMicroSamleMediaList = [];
									
									angular.forEach($scope.listTMicroSampleMediaDto, function(sampleMediaobj, key) {
                                                  if(sampleMediaobj.isSelected==true)
                                                	  {
                                                	   sampleMediaobj.createdDate = moment().toDate().getTime();
                                                	   tempTMicroSamleMediaList.push(sampleMediaobj); 
                                                	  }
										});
									console.log("URI", URI);
									console.log("tempTMicroSamleMediaList", JSON.stringify(tempTMicroSamleMediaList));
									if(tempTMicroSamleMediaList.length>0){
										
										GenericService
										.serviceAction(METHOD_POST, URI, tempTMicroSamleMediaList)
										.then(
												function(response) {
													$rootScope.stopLoader();
													if (response.data.status == 'success') {
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
														 $rootScope.stopLoader();
														$scope.$broadcast('show-errors-reset');
													} else if (response.data.status == 'error') {
														$rootScope.stopLoader();
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
														 $rootScope.stopLoader();
													}
													
												});
										
									}else{
										 $rootScope.stopLoader();
										growl.warning("Please select atleast one media",
												{
													title : 'warning!'
												});
									}
								
								} catch (e) {
									$rootScope.stopLoader();
									console.log(e.message)
								}
							}
							
							$scope.getIncubationMedia = function(labSampleDtlsId)
							{
								try{
									$scope.listTMicroSampleMediaDto = [];
									var data = "";
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ GET_INCUBATION_MEDIA+S+labSampleDtlsId+S+$scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.listTMicroSampleMediaDto = response.data.listObject;
														$("#mediaDetailsViewModal").modal('show');
														console.log("$scope.listTMicroSampleMediaDto",$scope.listTMicroSampleMediaDto);
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
							$scope.checkSample = function()
							{
								listLabSampleDtls = [];
						   		angular.forEach($scope.microbiologyWorklist,function(value, key) {
									if (value.sampleStatusId == true) 
									{
										var labSampleDtlsforAccept = angular.copy($scope.labSampleDtls);
										labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
										labSampleDtlsforAccept.sampleStatusId = SEND_TO_REPORT_ENTRY;
										labSampleDtlsforAccept.createdBy = $scope.userId;
										labSampleDtlsforAccept.createdDate = moment().toDate().getTime();
										listLabSampleDtls.push(labSampleDtlsforAccept);
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
								$scope.listLabSampleDtls = [];
								$rootScope.startLoader();
								
								
								 var found = $scope.microbiologyWorklist.find(ar=>ar.sampleStatusId == true && (ar.sampWrkStatusId==1||ar.sampWrkStatusId==''));
								   if(found!=undefined)
									   {
									   growl.warning("Please Change Sample Status Work in Progress.",
												{
													title : 'Warning!'
												});
										$rootScope.stopLoader();
									   return false;
									   }
								
								   		angular.forEach($scope.microbiologyWorklist,function(value, key) {
													if (value.sampleStatusId == true) 
													{
														var labSampleDtlsforAccept = angular.copy($scope.labSampleDtls);
														labSampleDtlsforAccept.labSampleDtlsId = value.labSampleDtlsId;
														labSampleDtlsforAccept.sampleStatusId = SEND_TO_REPORT_ENTRY;
														labSampleDtlsforAccept.createdBy = $scope.userId;
														labSampleDtlsforAccept.createdDate = moment().toDate().getTime();
														$scope.listLabSampleDtls.push(labSampleDtlsforAccept);
													}
												});

								try {
									console.log("$scope.listLabSampleDtls",JSON.stringify($scope.listLabSampleDtls));
									var URI = BASE_URL + ROOT_URL
								            + LIS_MICROBIOLOGY
											+ MICROBIOLOGY_SEND_TO_REPORT_ENTRY;
									console.log("SEND_TO_microbiology WORKLIST", URI);
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listLabSampleDtls)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$scope.initMicrobiologyWorkList($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');

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
							$scope.sampleStatusIds=[SAMPLE_ACCEPTED];
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
												  "sampleNo" :$scope.selectedSampleNo!=undefined ? $scope.selectedSampleNo : null,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,//MicroWorklist/Search
												  "assignUnit" :$scope.orgUnitId,
												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
												
		                             }
									
									var URI = BASE_URL 
									        + ROOT_URL
									    	+ LIS_MICROBIOLOGY
											+ SEARCH_MICROBIOLOGY_WORKLIST_SEARCH
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 searchObj).then(
													 function(response) {
														 $scope.sampleAccPendingList=[];
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																console.log("resultEntryWorklist",JSON.stringify(response.data.listObject));
																$scope.microbiologyWorklist =  response.data.listObject;
																$scope.microbiologyWorklistForFiltter = $scope.microbiologyWorklist;
																$scope.filterByDummyServices();
															} else if (response.data.status == 'error') {
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
																$scope.microbiologyWorklist=[];
															}
														});
								} catch (e) {
									console.log(e.message)
								}
							}	

						} ]);