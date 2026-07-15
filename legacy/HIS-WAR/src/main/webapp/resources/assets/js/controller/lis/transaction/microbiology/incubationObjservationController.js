
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"incubationObjservationController",
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
							$scope.incubationObservationList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.incubationPeriodId;
							$scope.incubationStartDate = moment().startOf('day').toDate();
							$scope.incubationStartTime;
							$scope.microscopicExamination;

							
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
							$scope.isWorklistDepartment = labConfig.isWorklistDepartment;
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
						    $scope.isComplete = "";
						    $scope.incuDetailsObj =
						    	{
						    		"sampleId":"",
						    		"tabLevel":"",
						    		"microbioResultEntryId":"",
						    		"isIncubationComplete":"",
						    		"isMacro_exaComplete":"",
						    		"isMicro_exaComplete":""
						    	}
						    
						    
						     /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
				        	/*First Level*/
					    	$scope.firstTabLevel = 1 ;
							
							$scope.setFirstTabLevel= function(newTab)
							{ 
								$scope.incuDetailsObj.tabLevel = newTab;
								$scope.getIncuationPopupDetails($scope.incuDetailsObj);
								$scope.firstTabLevel = newTab;
							};
							
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								 
								return $scope.firstTabLevel === tabNum;
							};
							
							/*Second Level*/
							$scope.secondTabLevel = 1 ;
							
							$scope.setSecondTabLevel = function(newTab)
							{
								$scope.secondTabLevel = newTab;
							};
							
							$scope.isSetSecondTabLevel = function(tabNum)
							{
								return $scope.secondTabLevel === tabNum;
							};
							
							
							/*Third Level*/
							$scope.thirdTabLevel = 1 ;
							
							$scope.setThirdTabLevel = function(newTab)
							{
								$scope.thirdTabLevel = newTab;
							};
							
							$scope.isSetThirdTabLevel = function(tabNum)
							{
								return $scope.thirdTabLevel === tabNum;
							};
							
							
							
							/*FORTH Level*/
							$scope.forthTabLevel = 1 ;
							
							$scope.setForthTabLevel = function(newTab)
							{
								$scope.forthTabLevel = newTab;
							};
							
							$scope.isSetForthTabLevel = function(tabNum)
							{
								return $scope.forthTabLevel === tabNum;
							};
							
							
							
							/**DatePicker Code  */

							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date()
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
								$scope.initIncucationObjservationList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initIncucationObjservationList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
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
											"isWorklistDepartment":$scope.isWorklistDepartment,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"isWorklistDepartment":$scope.isWorklistDepartment,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL + ROOT_URL
											+ LIS_MICROBIOLOGY
											+ INCUBATION_OBSERVATION_LIST;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_MICROBIOLOGY
											+ INCUBATION_OBSERVATION_LIST_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.incubationObservationList = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.incubationObservationListForFiltter = $scope.incubationObservationList;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initIncucationObjservationList($scope.orgId,
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
								$scope.initIncucationObjservationList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);

							};
							
							$scope.getIncucationObjservationList = function(orgId,
									orgUnitId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId":orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											"isWorklistDepartment":$scope.isWorklistDepartment,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
											"isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
										};
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ INCUBATION_OBSERVATION_LIST;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.incubationObservationList = response.data.listObject;
														$scope.incubationObservationListForFiltter = $scope.incubationObservationList;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.filterByDummyServices= function()
                            {
								$scope.incubationObservationList  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.incubationObservationList = angular.copy($scope.incubationObservationListForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.incubationObservationListForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.incubationObservationList.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.incubationObservationListForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.incubationObservationList.push(value);
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
									$scope.IncucationObjservationList(
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
												$scope.microbiologyAccPendingList,
												function(labDetailsObj) {
													labDetailsObj.isAcceptOrReject = $scope.selectedAll
												});
							 }
							
							
				
							
							
							
							
							$scope.generateIncubationDetailsUrl = function(incubationObservationObj)
							{
								if(incubationObservationObj!=null)
								  {
									incubationObservationObj = incubationObservationObj;
									incubationObservationObj.orgId = $scope.orgId;
									incubationObservationObj.orgUnitId = $scope.orgUnitId;
									incubationObservationObj.createdBy = $scope.userId;
									incubationObservationObj.listTMicroSampleMediaDto = [];
												$state.go('detailsIncubationMaster',
													{
													  incuObsrvationObj : incubationObservationObj,
													});
								  }
							  else
							     {
								    growl.error("Something went Wrong.",{title : 'Error!'});
							     }
							}
							
/*====================================SEARCH DETAILS=====================================*/
							

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
							$scope.sampleStatusIds=[26,27];
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
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.orgUnitId,
												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
												  "isWorklistDepartment":$scope.isWorklistDepartment,
												  "sampleNo" :$scope.selectedSampleNo!=undefined ? $scope.selectedSampleNo : null,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable
		                             }
									
									console.log("$scope.selectedtestList",$scope.selectedtestList);
									console.log("$scope.selectedDepartMentList",$scope.selectedDepartMentList);
									console.log("collectedSampleList",JSON.stringify(searchObj));
									var URI = BASE_URL 
									        + ROOT_URL
									    	+ LIS_MICROBIOLOGY
											+ MICROBIOLOGY_INCUBATION_LIST_SEARCH
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 searchObj).then(
													 function(response) {
														 $scope.sampleAccPendingList=[];
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																$scope.incubationObservationList =  response.data.listObject;
																$scope.incubationObservationListForFiltter = $scope.incubationObservationList;
																$scope.filterByDummyServices();
															} else if (response.data.status == 'error') {
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
																$scope.incubationObservationList=[];
															}
														});
								} catch (e) {
									console.log(e.message)
								}
							}	
							
							$scope.plzSelectIncubationPeriod = 'Please Select Incubation Period.';
							$scope.plzSelectIncubationStartDate= 'Please Select Incubation Start Date.';
							$scope.plzSelectIncubationStartTime= 'Please Select Incubation StartTime.';
							$scope.plzSelectIncubationName= 'Please Select Incubation Name.';
							$scope.plzSelectMediaName= 'Please Select Media Name.';
							$scope.microscopicExaminationDetials = 'Microscopic Examination Details Requried.';
							$scope.plzSelectStainingName= 'Please Select Staining Name.';
							$scope.plzSelectOrganismGroup= 'Please Select Organism Group.';
						} ]);