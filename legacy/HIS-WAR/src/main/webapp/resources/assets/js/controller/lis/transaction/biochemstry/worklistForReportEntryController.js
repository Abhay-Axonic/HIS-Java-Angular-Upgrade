
/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"worklistForReportEntryController",
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
						'subDeptId',
						'$timeout',
						'$stateParams',
						'reportService',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,subDeptId,$timeout,$stateParams,reportService) {

							
							
							$scope.isRecollected = false;
							$rootScope.loginpage = true;
							$scope.thirdLevel = false;
							
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultEntryWorklist  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword ="";
							$scope.commonListCount;
							$scope.labResultTestDtls;
							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
							$scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
							$scope.isAcceptanceInDepartment= labConfig.isAcceptanceInDepartment;
							$scope.isWorklistDepartment= labConfig.isWorklistDepartment;
							$scope.labSortingId =  labConfig.labSortingId;
							$scope.subDeptId = subDeptId;
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
							
							
							$scope.labSortingList = [];
							$scope.getLabSortingMasterList = function () {
								var URI = BASE_URL + ROOT_URL + "/LIS/transaction/labSortingList";
								$scope.labSortingList = [];
								GenericService.serviceAction("GET", URI).then(function (response) {
									if (response.data.status == "success") {
										$scope.labSortingList = response.data.listObject;
									}
								});
							}
							
							$scope.getLabSortingMasterList();
							
							//Multiple Report Entry
							$scope.selectedResEntryList = [];
							
							$scope.setNoOfRecords = function() {
								$scope.initResultEntryList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initResultEntryList = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									
									$scope.selectedResEntryList = [];
									$scope.resultEntryWorklist = [];
									$rootScope.startLoader();
									var data = {
											  "orgId":orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId ,
	                                          "offset":offset,
	                                          "recordPerPage":noOfRecordsPerPage,
	                                          "deptId":$scope.deptId,
	                                          "subDeptId":$scope.subDeptId,
	                                          "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
	                                          "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "subDepts" : $scope.selectedDepartMentList,
											  "isSendToCrApplicable":$scope.isSendToCrApplicable,
					                          "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
					                          "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
					                          "isWorklistDepartment":$scope.isWorklistDepartment,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null		  
	                             }
									
									var data1 = {
											"orgId":orgId,
											"orgUnitId":$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
					                        "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
					                        "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
					                        "isWorklistDepartment":$scope.isWorklistDepartment,
											"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									        "toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
									        + LIS_BIOCHEMISTRY
									        + "/resultEntry/Search"

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ WORK_LIST_FOR_DEPT_RESULT_ENTERY_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryWorklist  = response[0].listObject;
														angular.forEach($scope.resultEntryWorklist, function(value, key) 
																{
																	value.statusFlag = false;
															
																});
														$scope.commonListCount = response[1].object;
														$scope.resultEntryWorklistForFiltter  = $scope.resultEntryWorklist;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
													});
								} catch (e) {
									console.log(e.message);
								}
							}

								 if($stateParams.searchObj!=undefined && $stateParams.searchObj!=null && $stateParams.searchObj.assignUnit!=null){
									 $scope.assignUnit = $stateParams.searchObj.assignUnit;
										$scope.fromDate = $stateParams.searchObj.fromDate;
										$scope.toDate = $stateParams.searchObj.toDate;
										$scope.patientId = $stateParams.searchObj.patientId;
										$scope.selectedPatient = $stateParams.searchObj.selectedPatient;
										$scope.uhid = $stateParams.searchObj.uhid;
										$scope.selectedDepartMentList = $stateParams.searchObj.selectedDepartMentList;
										$scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
										$scope.selectedtestList = $stateParams.searchObj.selectedtestList;
										$scope.selectedSampleNo = $stateParams.searchObj.selectedSampleNo;
									}
							$scope.initResultEntryList($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
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
							    $scope.initResultEntryList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};
				

							$scope.getBiochemWorkList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:50;
									
									var data = {
                                            "orgId":orgId,
                                            "orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
                                            "offset":offset,
                                            "recordPerPage":noOfRecordsPerPage,
                                            "deptId":$scope.deptId,
                                            "subDeptId":$scope.subDeptId,
                                            "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
  											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"patientId" :  $scope.selectedPatient,
											"labSampleDtlsId" : $scope.labSampleDtlsId,
											"visitTypes" :$scope.selectedVisitType,
											"testTypes" : $scope.selectedtestList,
											"subDepts" : $scope.selectedDepartMentList,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
					                          "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
					                          "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
					                          "isWorklistDepartment":$scope.isWorklistDepartment,
											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null
										};	
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ "/resultEntry/Search"
										//	+ WORK_LIST_FOR_DEPT_RESULT_ENTERY;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryWorklist  = response.data.listObject;
														angular.forEach($scope.resultEntryWorklist, function(value, key) 
																{
																	value.statusFlag = false;
															
																});
														$scope.resultEntryWorklistForFiltter  = $scope.resultEntryWorklist;
														$scope.filterByDummyServices();
														
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							$scope.filterByDummyServices= function()
                            {
								$scope.resultEntryWorklist  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.resultEntryWorklist = angular.copy($scope.resultEntryWorklistForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.resultEntryWorklistForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.resultEntryWorklist.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.resultEntryWorklistForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.resultEntryWorklist.push(value);
                                              });
                                               
                                }
                            }
							
							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								$scope.pager.totalPages = undefined;
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getBiochemWorkList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							$scope.nextSerologyRecords = [];
							$scope.nextOtherDeptRecords = [];
							$scope.selectedIndex = [];
							 $scope.selectRecord = function(obj,index){
								 
								 if(obj.statusFlag == true){
									 $scope.selectedIndex.push(index);
									 $scope.selectedResEntryList.push(obj);
									 
								 }else{
									 var selectedResEntryListTemp = [];
									 angular.forEach($scope.selectedResEntryList, function(value, key) 
												{
										 			if(value.labSampleDtlsId != obj.labSampleDtlsId){
										 				 selectedResEntryListTemp.push(value);
										 			}
										 
												});
									 
									 $scope.selectedResEntryList = selectedResEntryListTemp;
									 var idx = $scope.selectedIndex.findIndex(item => item ==  index);
									 $scope.selectedIndex.splice(idx,1);
								 }
							 }
							
							$scope.generateReportEntryDtlsUrl = function()
							{
								if($scope.selectedResEntryList.length>0)
									{
									$rootScope.startLoader();
									
									 if($scope.selectedIndex.length>0){
											angular.forEach($scope.selectedIndex,function(val,index){
												angular.forEach($scope.resultEntryWorklist,function(value,indx){
													 if(val < indx && !value.statusFlag){
														// other dept
														 $scope.nextOtherDeptRecords.push(value);
													 }
												 });
											});
										}
									 
									 
									
									var count = 0;
									 var patientIds = [];
									 var flag = false;
									angular.forEach($scope.selectedResEntryList, function(value, key) 
											{
										value.orgId = value.orgId;
										value.orgUnitId = value.orgUnitId;
										value.createdBy = $scope.userId;
										value.listLabSampleResultDetailsMaster = [];
										patientIds.push(value.patientId);	
										
										count++;
										if(new Set(patientIds).size==1 && $scope.selectedResEntryList.length==count)
										{
										 flag =true;
										 
										 var searchObj = {};
										 searchObj.assignUnit = $scope.assignUnit;
										 searchObj.fromDate = $scope.fromDate;
										 searchObj.toDate = $scope.toDate;
										 searchObj.patientId = $scope.patientId;
										 searchObj.selectedPatient = $scope.selectedPatient;
										 searchObj.uhid = $scope.uhid;
										 searchObj.selectedDepartMentList = $scope.selectedDepartMentList;
										 searchObj.selectedVisitType = $scope.selectedVisitType;
										 searchObj.selectedtestList = $scope.selectedtestList;
										 searchObj.selectedSampleNo = $scope.selectedSampleNo;
										 
										 $scope.finalOtherDeptRecords = [];
			                                if($scope.nextOtherDeptRecords!=undefined || $scope.nextOtherDeptRecords!=null){
			                                    angular.forEach($scope.nextOtherDeptRecords,function(val,idx){
			                                            var newArr = {
			                                                    subDeptId : "",
			                                                    resultMstList : [],
			                                                    searchObj : {}
			                                            };
			                                                newArr.subDeptId  = val.subDeptId,
			                                                newArr.resultMstList.push(val) ,
			                                                newArr.searchObj = searchObj;
			                                                $scope.finalOtherDeptRecords.push(newArr);
			                                    });
			                                }
										 $timeout(function () {
											 $rootScope.stopLoader();
											 $state.go('deatilsReportEntryWorklist',
														{
														  subDeptId : subDeptId,
														  resultMstList : $scope.selectedResEntryList,
														  searchObj : searchObj,
														  nextRecordList : $scope.finalOtherDeptRecords
														});

											 }, 2000);
									
										 }
									});
									if(!flag)
									{
										  growl
											.warning("Please select Unique Patient tests.",
													{
														title : 'Warning!'
													});
										  $rootScope.stopLoader();
									}
			
									}
								else{
									growl
									.warning("Please Select Test.",
											{
												title : 'Warning!'
											});
								}
								
								
						
							}
/*====================================SEARCH DETAILS=====================================*/
							
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate:new Date()
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
							$scope.selectedVisitTypeIds = [1,2,3,4];
							$scope.sampleStatusIds=[SEND_TO_DEPT];
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
						 
						/* $scope.compareFn = function(obj1, obj2){
						        return obj1.labSampleDtlsId === obj2.labSampleDtlsId;
						    };*/
						 
						
						 $scope.searchOutPatientList = function()
							{
								try 
								{
									$scope.selectedResEntryList.splice(0, $scope.selectedResEntryList.length);
									$scope.resultEntryWorklist=[];
									$rootScope.startLoader();
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "assignUnit" :$scope.orgUnitId ,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "patientId" :  $scope.selectedPatient,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
						                          "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
						                          "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
						                          "isWorklistDepartment":$scope.isWorklistDepartment,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
												  "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false 	  
		                             };
									
									var fromDt = ($scope.fromDate==undefined)?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
									var toDt =  ($scope.toDate==undefined)?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
									
									var isValidDate = $rootScope.isValidDate(fromDt,toDt);
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
									var URI1 = BASE_URL 
									        + ROOT_URL
									    	+ LIS_BIOCHEMISTRY
											+ "/resultEntry/Search";
									
									var data1 = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isSendToCrApplicable":$scope.isSendToCrApplicable,
					                        "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
					                        "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
					                        "isWorklistDepartment":$scope.isWorklistDepartment,
											"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									        "toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
		                                    "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false    

									};
									
										var URI2 = BASE_URL
										+ ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ WORK_LIST_FOR_DEPT_RESULT_ENTERY_COUNT;

								console.log("URI1", URI1);
								console.log("URI2", URI2);
								promiseFactoryWithObject
										.setPromisesWithObject(URI1, URI2, METHOD_POST,
												METHOD_POST,searchObj,data1)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.resultEntryWorklist  = response[0].listObject;
													angular.forEach($scope.resultEntryWorklist, function(value, key) 
															{
																value.statusFlag = false;
														
															});
													$scope.commonListCount = response[1].object;
													$scope.resultEntryWorklistForFiltter  = $scope.resultEntryWorklist;
													$scope.filterByDummyServices();
													$scope.setPage(1,false);
												});
								} catch (e) {
									$rootScope.stopLoader();
									console.log(e.message)
								}
							}
						 
						 
						 $scope.typeSearchList = function(searchKeyword)
							{
							 
							    if (searchKeyword.length>=3){
							        
							    	try 
									{
							    		$scope.selectedResEntryList.splice(0, $scope.selectedResEntryList.length);
										$scope.resultEntryWorklist=[];
										$rootScope.startLoader();
										var data = {
												 "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "assignUnit" :$scope.orgUnitId ,
												  "deptId":$scope.deptId,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
						                          "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
						                          "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
						                          "isWorklistDepartment":$scope.isWorklistDepartment,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "searchField":searchKeyword,
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null

										}
										
										var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/resultEntry/Type/Search";
										GenericService.serviceAction(METHOD_POST, URI, data).then(
												function(response) {
													$rootScope.stopLoader();
													$scope.resultEntryWorklist  = response.data.listObject;
													$scope.commonListCount = response.data.object;
													$scope.resultEntryWorklistForFiltter  = $scope.resultEntryWorklist;
													$scope.filterByDummyServices();
													$scope.setPage(1,false);
												});
									} catch (e) {
										$rootScope.stopLoader();
										console.log(e.message)
									}
							    	
							    }else if(searchKeyword.length == 0){
							    	$scope.searchOutPatientList();
							    }

							 
							
							}
						 
						 $scope.print = function(labSampleDtlsId){
							 

								
								$scope.labSampleDtlsIdsForPrint = [];
						    	$scope.labSampleDtlsIdsForPrint.push(labSampleDtlsId);
						    	
						    	var data =  
								{
									"appName": "HIS",
									"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									"reportDesignFilePath": "pathologyReport_GroupPrint.rpt",
									"reportFormat": "PDF",
									"exportFileName": "pathologyReport",
									"reportParams": [
													{
											             "paramName": "lab_sample_dtls_id",
											             "paramType": "String",
											             "paramValue":  $scope.labSampleDtlsIdsForPrint.toString()
										              },
										              {
												             "paramName": "isOriginal",
												             "paramType": "Integer",
												             "paramValue": 1
											           },
											           {
												             "paramName": "isHeader",
												             "paramType": "Integer",
												             "paramValue": 0
											           }
										            ]
								};
								reportService.generate(REPORT_API_URI1,data);
							 
						 }
						 
							
						} ]);