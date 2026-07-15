
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"macroScopicExaminationController",
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

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.macroScopicExaminationList = [];
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
						    $scope.subDeptId = labConfig.microbiologyId;
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
						    
						    
						     /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
				        	
							
							
							
							/**DatePicker Code  */

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
							
							
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/

							$scope.setNoOfRecords = function() {
								$scope.initMacroScopicExaminationList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initMacroScopicExaminationList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL + ROOT_URL
											+ LIS_MICROBIOLOGY
											+ MACROSCOPIC_EXAMINATION_WORKLIST;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_MICROBIOLOGY
											+ MACROSCOPIC_EXAMINATION_WORKLIST_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.macroScopicExaminationList = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.macroScopicExaminationListForFiltter = $scope.macroScopicExaminationList;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initMacroScopicExaminationList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);
							
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
								$scope.selectedSampleNo = '';
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
							    $scope.initMacroScopicExaminationList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);

							};
							
							$scope.getMacroScopicExaminationList = function(orgId,
									orgUnitId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage
										};
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ MACROSCOPIC_EXAMINATION_WORKLIST;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.macroScopicExaminationList = response.data.listObject;
														$scope.macroScopicExaminationListForFiltter = $scope.macroScopicExaminationList;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.filterByDummyServices= function()
                            {
								$scope.macroScopicExaminationList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.macroScopicExaminationList = angular.copy($scope.macroScopicExaminationListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.macroScopicExaminationListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.macroScopicExaminationList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.macroScopicExaminationListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.macroScopicExaminationList.push(value);
                                              });
                                               
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
									$scope.initMacroScopicExaminationList(
											$scope.orgId, $scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
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
												$scope.macroScopicExaminationList,
												function(labDetailsObj) {
													labDetailsObj.isAcceptOrReject = $scope.selectedAll
												});
							 }
							
							
							
							$scope.macroscopicExaminationMasterDtoList = [];
							$scope.macroscopicExaminationMasterDto={
								  "tMacroExaId" : "",
								  "labSampleDtlsId" : "",
								  "macroExaResult" : "",
								  "isCompleted" : "N",
								  "orgId" : $scope.orgId,
								  "orgUnitId" : $scope.orgUnitId,
								  "createdBy" : $scope.userId,
								  "createdDate" : "",
								  "updatedBy" : "",
								  "updatedDate" : "",
								  "isDeleted" : "N"
								}
							
							$scope.saveExamination = function()
							{
								if($scope.macroscopicExaminationMasterDto.macroExaResult == "" || $scope.macroscopicExaminationMasterDto.macroExaResult == undefined){
									growl.warning("please enter Details",
											{
												title : 'warning!'
											});
									return false;
								}
								if($scope.macroscopicExaminationMasterDto.tMacroExaId==""||$scope.macroscopicExaminationMasterDto.tMacroExaId==null)
									{
									  $scope.saveMacroScopicExamination();
									}
								else if($scope.macroscopicExaminationMasterDto.tMacroExaId>0)
									{
									$scope.updateMacroscopicExamination();
									}
							}
							
							$scope.saveMacroScopicExamination= function() 
							{
									try 
									{
										$scope.macroscopicExaminationMasterDtoList = [];
										
										var URI =BASE_URL + ROOT_URL
										+ LIS_MICROBIOLOGY
										+ SAVE_MACROSCOPIC_EXAMINATION;
										if($scope.macroscopicExaminationMasterDto.macroExaResult == ""){
											growl.warning("please enter Details",
													{
														title : 'warning!'
													});
											return false;
										}else{
											$rootScope.startLoader();
											$scope.macroscopicExaminationMasterDto.createdDate = moment().toDate().getTime();
											$scope.macroscopicExaminationMasterDtoList.push($scope.macroscopicExaminationMasterDto);
											console.log("URI",URI);
											console.log("macroscopicExaminationMasterDtoList",JSON.stringify($scope.macroscopicExaminationMasterDtoList));
											GenericService.serviceAction(METHOD_POST, URI,$scope.macroscopicExaminationMasterDtoList).then(function(response) 
											{	
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$scope.initMacroScopicExaminationList($scope.orgId,
															$scope.orgUnitId, $scope.offset,
															$scope.noOfRecordsPerPage);
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
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
									
							}
							
							$scope.updateMacroscopicExamination= function() 
							{
								try 
								{
									$scope.macroscopicExaminationMasterDtoList = [];
									$rootScope.startLoader();
									var URI =BASE_URL + ROOT_URL
									+ LIS_MICROBIOLOGY
									+ UPDATE_MACROSCOPIC_EXAMINATION;
									$scope.macroscopicExaminationMasterDto.updatedDate = moment().toDate().getTime();
									$scope.macroscopicExaminationMasterDto.updatedBy = $scope.userId;
									$scope.macroscopicExaminationMasterDtoList.push($scope.macroscopicExaminationMasterDto);
									
									console.log("URI",URI);
									console.log("macroscopicExaminationMasterDtoList",JSON.stringify($scope.macroscopicExaminationMasterDtoList));
									GenericService.serviceAction("PUT", URI,$scope.macroscopicExaminationMasterDtoList).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.initMacroScopicExaminationList($scope.orgId,
													$scope.orgUnitId, $scope.offset,
													$scope.noOfRecordsPerPage);
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
								} catch (e) {
									$rootScope.stopLoader();
									console.log("Exception",e.message);
								}
							}
							
							$scope.getMacroscopicExamination = function(labSampleDtlsId,tMacroExaId)
							{
								try{
										if(tMacroExaId!=null&&tMacroExaId!=undefined)
										{
										var data = "";
										var URI = BASE_URL + ROOT_URL
									            	+ LIS_MICROBIOLOGY
									               	+ GET_MACROSCOPIC_EXAMINATION+S+tMacroExaId+S+$scope.orgId+S+$scope.orgUnitId;
										console.log("URI", URI);
										GenericService
												.serviceAction(METHOD_GET, URI, data)
												.then(
														function(response) {
															$rootScope.stopLoader();
															$scope.macroscopicExaminationMasterDto = response.data.object;
															$('#macroExampleDetailsModal').modal('show');
															console.log("$scope.macroscopicExaminationMasterDtoList",$scope.macroscopicExaminationMasterDtoList);
														});
										}
										
									else{
										$scope.macroscopicExaminationMasterDto.labSampleDtlsId = labSampleDtlsId;
										$scope.macroscopicExaminationMasterDto.macroExaResult = "";
										$scope.macroscopicExaminationMasterDto.isCompleted = "N";
										$("#macroExampleDetailsModal").modal('show');
									}
									
								} catch (e) {
									console.log(e.message)
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
							$scope.sampleStatusIds=[26,28,29];
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
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.orgUnitId ,
												  "sampleNo" : $scope.selectedSampleNo !=undefined ? $scope.selectedSampleNo : null
		                             }
									
									var URI = BASE_URL 
									        + ROOT_URL
									    	+ LIS_MICROBIOLOGY
											+ MACROSPIC_EXAMINATION_LIST_SEARCH
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 searchObj).then(
													 function(response) {
														 $scope.sampleAccPendingList=[];
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																$scope.macroScopicExaminationList =  response.data.listObject;
																$scope.macroScopicExaminationListForFiltter = $scope.macroScopicExaminationList;
																$scope.filterByDummyServices();
															} else if (response.data.status == 'error') {
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
																$scope.macroScopicExaminationList=[];
															}
														});
								} catch (e) {
									console.log(e.message)
								}
							}	
				
						} ]);