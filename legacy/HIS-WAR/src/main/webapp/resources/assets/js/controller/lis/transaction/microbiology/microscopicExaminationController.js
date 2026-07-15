
/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"microscopicExaminationController",
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
						$scope.microScopicExaminationList = [];
						$scope.totalNoOfRecords;
						$scope.searchKeyword;
						$scope.commonListCount;
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
					    $scope.subDeptId = labConfig.microbiologyId;
					    $scope.getUnitByOrganization();
						$scope.assignUnit = $scope.orgUnitId;
					    
					    $scope.isComplete = "";
					    
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
						
						$scope.setNoOfRecords = function() {
							$scope.initMicroScopicExaminationList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);
						};

						$scope.initMicroScopicExaminationList = function(orgId,
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
										"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						                "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
										"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
										"patientId" :  $scope.selectedPatient,
										"labSampleDtlsId" : $scope.labSampleDtlsId,
										"visitTypes" :$scope.selectedVisitType,
										"testTypes" : $scope.selectedtestList,
										"subDepts" : $scope.selectedDepartMentList,
										"assignUnit" :$scope.orgUnitId
									};
								var data1 = {
										"orgId":orgId,
										"orgUnitId":orgUnitId,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId,
										"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
									};
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 50;
								var URI1 = BASE_URL + ROOT_URL
										+ LIS_MICROBIOLOGY
										+ MICROSPIC_EXAMINATION_LIST_SEARCH
										//+ MICROSCOPIC_EXAMINATION_WORKLIST;

								var URI2 = BASE_URL
										+ ROOT_URL
										+ LIS_MICROBIOLOGY
										+ MICROSCOPIC_EXAMINATION_WORKLIST_COUNT;

								console.log("URI1", URI1);
								console.log("URI2", URI2);
								promiseFactoryWithObject
										.setPromisesWithObject(URI1, URI2, METHOD_POST,
												METHOD_POST,data,data1)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.microScopicExaminationList = response[0].listObject;
													$scope.commonListCount = response[1].object;
													$scope.microScopicExaminationListForFilter = $scope.microScopicExaminationList;
													$scope.filterByDummyServices();
													$scope.setPage(1,false);
												});
							} catch (e) {
								console.log(e.message);
							}
						}

						$scope.initMicroScopicExaminationList($scope.orgId,
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
							$scope.selectedSampleNo='';
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
						    $scope.initMicroScopicExaminationList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);

						};
						
						$scope.getMicroScopicExaminationList = function(orgId,
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
										 "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										 "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
				                          "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
										  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
										  "patientId" :  $scope.selectedPatient,
										  "labSampleDtlsId" : $scope.labSampleDtlsId,
										  "visitTypes" :$scope.selectedVisitType,
										  "testTypes" : $scope.selectedtestList,
										  "subDepts" : $scope.selectedDepartMentList,
										  "assignUnit" :$scope.orgUnitId
									};
								var URI = BASE_URL + ROOT_URL
							            	+ LIS_MICROBIOLOGY
							            	+ MICROSPIC_EXAMINATION_LIST_SEARCH
								console.log("URI", URI);

								GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.microScopicExaminationList = response.data.listObject;
													$scope.microScopicExaminationListForFilter = $scope.microScopicExaminationList;
													$scope.filterByDummyServices();
												});
							} catch (e) {
								console.log(e.message)
							}

						}
						
						$scope.filterByDummyServices= function()
                        {
							$scope.microScopicExaminationList  =[]
                            if($scope.filterDummyService==1)//all 
                            {
                            	$scope.microScopicExaminationList = angular.copy($scope.microScopicExaminationListForFilter);
                            	
                            }else if($scope.filterDummyService==2)//real
                            {
                                angular.forEach($scope.microScopicExaminationListForFilter, function (value, key) 
                                {
                                    if(value.isDummyService==false)
                                    	$scope.microScopicExaminationList.push(value);
                                });
                                 
                                
                            }else if($scope.filterDummyService==3 )//dummy
                            {
                            	  angular.forEach($scope.microScopicExaminationListForFilter, function (value, key) 
                                          {
                                              if(value.isDummyService==true)
                                            	  $scope.microScopicExaminationList.push(value);
                                          });
                                           
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
								$scope.getMicroScopicExaminationList(
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
						
						
			
						
						
						
						
						$scope.generateMicroDetailsUrl = function(microScopicExaminationObj)
						{
							if(microScopicExaminationObj!=null)
							  {
								microScopicExaObj = microScopicExaminationObj;
								microScopicExaObj.orgId = $scope.orgId;
								microScopicExaObj.orgUnitId = $scope.orgUnitId;
								microScopicExaObj.createdBy = $scope.userId;
								microScopicExaObj.listTMicroSampleMediaDto = [];
											$state.go('detailsMicroscopicExam',
												{
												   microExaObj : microScopicExaObj,
												});
							  }
						  else
						     {
							    growl.error("Something went Wrong.",{title : 'Error!'});
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
						$scope.sampleStatusIds=[32,33];
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
					                          "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDeptId" : $scope.subDeptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "assignUnit" :$scope.orgUnitId,
											  "offset":$scope.offset,
											  "recordPerPage":$scope.noOfRecordsPerPage
	                             }
								
								
								
								var isValidDate = $rootScope.isValidDate(searchObj.fromDate,searchObj.toDate);
								if(!isValidDate){
									$rootScope.stopLoader();
									growl.warning("From date can not be greater than to Date ",
											{
												title : 'warning!'
											});
									return false;
								}
								
								var data1 = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId,
										"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
									};
								
								var URI2 = BASE_URL
								+ ROOT_URL
								+ LIS_MICROBIOLOGY
								+ MICROSCOPIC_EXAMINATION_WORKLIST_COUNT;
								
								console.log("collectedSampleList",JSON.stringify(searchObj));
								var URI = BASE_URL 
								        + ROOT_URL
								    	+ LIS_MICROBIOLOGY
										+ MICROSPIC_EXAMINATION_LIST_SEARCH
								console.log("URI", URI);
								promiseFactoryWithObject
								.setPromisesWithObject(URI, URI2, METHOD_POST,
										METHOD_POST,searchObj,data1)
								.then(
												 function(response) {
													 $scope.sampleAccPendingList=[];
															$rootScope.stopLoader();
															$scope.microScopicExaminationList = response[0].listObject;
															$scope.commonListCount = response[1].object;
															$scope.microScopicExaminationListForFilter = $scope.microScopicExaminationList;
															$scope.filterByDummyServices();
															$scope.setPage(1,false);
													});
							} catch (e) {
								console.log(e.message)
							}
						}		
					
					} 
					]);