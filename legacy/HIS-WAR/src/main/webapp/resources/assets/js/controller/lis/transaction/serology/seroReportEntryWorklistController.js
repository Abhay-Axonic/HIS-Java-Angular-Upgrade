
/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"seroReportEntryWorklistController",
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
						'$timeout',
						'$stateParams',
						'reportService',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$timeout,$stateParams,reportService) {

							
							$scope.isRecollected = false;
							$rootScope.loginpage = true;
							$scope.thirdLevel = false;
							
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultEntryWorklist  = [];
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
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = labConfig.serologyId;
							$scope.isAcceptanceInDepartment = labConfig.isAcceptanceInDepartment;
							$scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
							$scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
							$scope.labSortingId =  labConfig.labSortingId;
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
							//Multiple Test Selection
							$scope.selectedResEntryList =[];
							
							
							$scope.setNoOfRecords = function() {
								$scope.initResultEntryList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initResultEntryList = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									$scope.commonListCount =0;
									$scope.resultEntryWorklist = [];
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
											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											"patientId" :  $scope.selectedPatient,
											"labSampleDtlsId" : $scope.labSampleDtlsId,
											"visitTypes" :$scope.selectedVisitType,
											"testTypes" : $scope.selectedtestList,
											"deptId" : $scope.deptId,
											"subDeptId" : $scope.subDeptId,
											"subDepts" : $scope.selectedDepartMentList,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
  				                           "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false    

										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							                "toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
									        + GET_SEROLOGY_REPORT_ENTRY_SEARCH

									var URI2 = BASE_URL
											+ ROOT_URL
											+ GET_SEROLOGY_REPORT_ENTRY_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryWorklist  = response[0].listObject;
														$scope.resultEntryWorklistForFiltter  = $scope.resultEntryWorklist;
														$scope.filterByDummyServices();
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
							    $scope.initResultEntryList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getSerologyWorklist = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:50;
											
									$scope.resultEntryWorklist = [];
									var data = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									        "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									        "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null		  ,
			  				                 "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false    
		
										};	
									var URI = BASE_URL + ROOT_URL
									   + GET_SEROLOGY_REPORT_ENTRY_SEARCH
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryWorklist  = response.data.listObject;
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
							
							$scope.generateReportEntryDtlsUrl = function()
							{
								if($scope.selectedIndex.length>0){
									angular.forEach($scope.selectedIndex,function(val,index){
										angular.forEach($scope.resultEntryWorklist,function(value,indx){
											 if(val < indx && !value.statusFlag){
												// other dept
												 $scope.nextSerologyRecords.push(value);
											 }
										 });
									});
								}
							  if($scope.selectedResEntryList.length>0)
								{
								    var patientIds = [];
								    angular.forEach($scope.selectedResEntryList, function(value, key) {
									value.orgId = value.orgId;
									value.orgUnitId = value.orgUnitId;
									value.createdBy = $scope.userId;
									value.listLabSampleResultDetailsMaster = [];
									patientIds.push(value.patientId);			
								});
								if(new Set(patientIds).size==1)
								{
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
									 
									 $scope.finalSeroDeptRecords = [];
		                                if($scope.nextSerologyRecords!=undefined || $scope.nextSerologyRecords!=null){
		                                    angular.forEach($scope.nextSerologyRecords,function(val,idx){
		                                        var newArr = {
		                                                subDeptId : "",
		                                                resultMstList : [],
		                                                searchObj : {}
		                                        };
		                                                newArr.resultMstList.push(val) ,
		                                                newArr.searchObj = searchObj;
		                                                $scope.finalSeroDeptRecords.push(newArr);
		                                    });
		                                }
									
									 $timeout(function () {
										 $state.go('detailsSerologyReportEntry',
											{
										 		resultMstList : $scope.selectedResEntryList,
										 		searchObj : searchObj,
										 		nextRecordList : $scope.finalSeroDeptRecords
											});
										 
									 }, 2000);
								}
								else{
									  growl
										.warning("Please select Unique Patient tests.",
												{
													title : 'Warning!'
												});
								    }
								}
							else{ 
								growl
								.warning("Please Select Test.",
										{
											title : 'Warning'
										});
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
						 $scope.searchSerology = function()
							{
								try 
								{
									$rootScope.startLoader();
									$scope.resultEntryWorklist = [];
									$scope.offset = $scope.offset != null ? $scope.offset : 0;
									$scope.noOfRecordsPerPage = $scope.noOfRecordsPerPage != null ? $scope.noOfRecordsPerPage: 50;
									$scope.commonListCount =0;
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
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
												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,		  
							  				      "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false    

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
									
									console.log("$scope.selectedtestList",$scope.selectedtestList);
									console.log("$scope.selectedDepartMentList",$scope.selectedDepartMentList);
									console.log("collectedSampleList",JSON.stringify(searchObj));
									var URI = BASE_URL 
									        + ROOT_URL
									        + GET_SEROLOGY_REPORT_ENTRY_SEARCH
									console.log("URI", URI);
							
									var data1 = {
											"orgId":$scope.orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											 "isSendToCrApplicable":$scope.isSendToCrApplicable,
											 "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											 "fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							                 "toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												
										};
									
									var URI2 = BASE_URL
											+ ROOT_URL
											+ GET_SEROLOGY_REPORT_ENTRY_COUNT;

									console.log("URI", URI);
									console.log("URI2", URI2);
									promiseFactoryWithObject
											.setPromisesWithObject(URI, URI2, METHOD_POST,
													METHOD_POST,searchObj,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryWorklist  = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.resultEntryWorklistForFiltter  = $scope.resultEntryWorklist;
														$scope.filterByDummyServices();
														$scope.setPage(1,false);
													});

								} catch (e) {
									console.log(e.message)
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
									$scope.getSerologyWorklist($scope.orgId, $scope.orgUnitId,$scope.deptId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							$timeout( function(){
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
							},1000);
							
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
								
								$scope.nextSerologyRecords = [];
								$scope.nextOtherDeptRecords = [];
								$scope.selectedIndex = [];
								$scope.selectRecord = function(obj,index){
									 if(obj.statusFlag == true){
										 $scope.selectedIndex.push(index);
										 $scope.selectedResEntryList.push(obj);
										 $scope.saveBtn = true;
									 }else{
										 var selectedResEntryListTemp = [];
										 angular.forEach($scope.selectedResEntryList, function(value, key) 
													{
											 			if(value.labSampleDtlsId != obj.labSampleDtlsId){
											 				selectedResEntryListTemp.push(value);
											 			}
													});
										 $scope.saveBtn = false;
										 $scope.selectedResEntryList = selectedResEntryListTemp;
										 var idx = $scope.selectedIndex.findIndex(item => item ==  index);
										 $scope.selectedIndex.splice(idx,1);
									 }
								}
								
								 $scope.print = function(labSampleDtlsId){
										
										$scope.labSampleDtlsIdsForPrint = [];
								    	$scope.labSampleDtlsIdsForPrint.push(labSampleDtlsId);
								    	
								    	var data =  
										{
											"appName": "HIS",
											"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											"reportDesignFilePath": "SerologyReport_GroupPrint.rpt",
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
