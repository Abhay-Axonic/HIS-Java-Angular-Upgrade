
/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"worklistForReportReleaseController",
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

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "worklistForReportRelease";
							$scope.thirdLevel = true;
							$scope.isRecollected = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultEntryworklistForValidation  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword="";
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
							//Multiple Report Release
							$scope.selectedRepReleaseEntryList  =[];
							$scope.reportReleaseBtn = false;
							$scope.saveBtn = false;
							$scope.isDisableGroupRelease = true;
							
							$scope.listHelpValueMaster = [];
							
							$scope.labResultDetailsMasterDtoDtlsList=[];
							
							$scope.listLabResultEntryDtls  = [];
							$scope.labResultMasterDto = {
									"labTestResId": "",
						            "orgId": $scope.orgId,
						            "orgUnitId":$scope.orgUnitId,
						            "testId": "",
						            "deptId": $scope.deptId,
						            "patientId": "",
						            "visitTypeId": "",
						            "visitAdmId": "",
						            "orderDetailsId": "",
						            "sampleNo": "",
						            "labSampleDtlsId": "",
						            "resultEnterDatetime": "",
						            "resultEnterBy": "",
						            "resultValidatedDatetime": "",
						            "resultValidatedBy": "",
						            "resultAuthorisedDatetime": "",
						            "resultAuthorisedBy": "",
						            "resultAuthorisedFlag": "",
						            "resultHandoverDatetime": "",
						            "resultHandoverBy": "",
						            "suggetionNotes": "",
						            "footsNotes": "",
						            "wardCode":"",
						            "bedNumber":"",
						            "sampleType":"",
						            "testCode":"",
						            "createdBy": $scope.userId,
						            "createdDate": "",
						            "updatedBy": $scope.userId,
						            "updatedDate": "",
						            "sampleBarcode": "",
						            "labSampleNo": "",
						            "subDeptId": $scope.subDeptId,
						            "visitType": "",
						            "uhid": "",
						            "patientDetails": "",
						            "doctorDetails": "",
						            "testDesc": "",
						            "priorityName": "",
						            "colorCode": "",
						            "panelCode":"",
						            "orderDateTime":"",
						            "sampleCollectionDatetime":"",
						            "panelId":"",
						            "genderId":"",
						            "resultLevel":"",
						            "sampleStatusId" :"",
						            "retestCount":"",
						            "listLabSampleResultDetailsMaster":$scope.labResultDetailsMasterDtoDtls
							}

						
							$scope.labResultDetailsMasterDtoDtls={
									"labResDtlsId": "",
									"labResultId": "",
									"parameterId": "",
									"headerId": "",
									"orgId":$scope.orgId,
									"orgUnitId": $scope.orgUnitId,
									"testId": "",
									"paramName": "",
									"paramUnit": "",
									"sampleNo": "",
									"finalResult": "",
									"firstLevelResult": "",
									"secondLevelResult": "",
									"thirdLevelResult": "",
									"resultTypeFlag": "",
									"parameterMin": "",
									"paramAbnrmlMin": "",
									"parameterMax": "",
									"paramAbnrmlMax": "",
									"paramPrintOrder": "",
									"refrangeTypeId":"",
									"textualRangeId":"",
									"multitextaulRange":"",
									"textualRangeName":"",
									"machineId": "",
									"machineResult": "",
									"remarks": "",
									"isDeltaFlag": "",
									"deltaPer":"",
									"deltaChange":"",
									"infromationDtls": "",
									"informationType": "",
									"informedTo": "",
									"informedDatetime": "",
									"paramRecheckedBy": "",
									"parameterFormula": "",
									"deltaPer":"",
									"createdBy": $scope.userId,
									"updatedBy": $scope.userId,
									"updatedDate": ""
							}
							
							$scope.showRejecPopup = function(index) {
								$scope.rejIndex = index;
								$("#pendingAcceptance").modal('show');
							}
							
							
							
							$scope.showHelpValue = function(HelpValueMaster)
							{
								$scope.listHelpValueMaster = HelpValueMaster;
								console.log($scope.listHelpValueMaster);
							}
							$scope.setNoOfRecords = function() {
								$scope.initResultEntryWorklistForValidation($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initResultEntryWorklistForValidation = function(orgId,
									orgUnitId,deptId, offset, noOfRecordsPerPage) {
								try {
									
									$scope.selectedRepReleaseEntryList = [];
									$rootScope.startLoader();
									var data = {
									        "deptId":$scope.deptId,
									        "subDeptId":$scope.subDeptId,
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"patientId" :$scope.selectedPatient,
											"visitTypes" :$scope.selectedVisitType,
											"priorityId": $scope.priorityId,
											"reportType": $scope.reportType,
											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											"offset":$scope.offset,
											"recordPerPage":$scope.noOfRecordsPerPage,
											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
											"isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false
										};
									var data1 = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"deptId":$scope.deptId,
										    "subDeptId":$scope.subDeptId,
										    "fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
										};
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ LAB_REPORT_RELEASE_SEARCH

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ WORK_LIST_FOR_REPORT_RELEASE_COUNT;

									console.log("URI1", URI1);
									console.log("URI2", URI2);
									console.log("data", JSON.stringify(data));
									
									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,data,data1)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryworklistForValidation  = response[0].listObject;
														angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
															value.selectOnlyOneVisitType = true;
														});
														$scope.commonListCount = response[1].object;
														$scope.setPage(1,false);
														$scope.isDisableGroupRelease = true;
														if($scope.resultEntryworklistForValidation!=null && $scope.resultEntryworklistForValidation .length>0){
															var found = $scope.resultEntryworklistForValidation.find(obj => {return obj.reportType==NORMAL_REPORT});
															if(found!=undefined){
																$scope.isDisableGroupRelease = false;
															}
															 
														}
														
														$scope.resultEntryworklistForValidationForFiltter  = $scope.resultEntryworklistForValidation;
														$scope.filterByDummyServices();
													});
								} catch (e) {
									console.log(e.message);
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
									$scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
									$scope.reportType = $stateParams.searchObj.reportType;
								}
							$scope.initResultEntryWorklistForValidation($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.offset,
									$scope.noOfRecordsPerPage);
							},1000);
				
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
							    $scope.reportType = '';
							    $scope.initResultEntryWorklistForValidation($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};

							$scope.getBiochemWorkList = function(orgId,
									orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
                                      var data = {
                                    		"deptId":$scope.deptId,
  									        "subDeptId":$scope.subDeptId,
  											"orgId":orgId,
  											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
  											"offset":offset,
  											"recordPerPage":noOfRecordsPerPage,
  											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
  											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
  											"patientId" :$scope.selectedPatient,
  											"visitTypes" :$scope.selectedVisitType,
  											"priorityId": $scope.priorityId,
  											"reportType": $scope.reportType,
  											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
  											"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
  											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
										};
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ LAB_REPORT_RELEASE_SEARCH
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryworklistForValidation  = response.data.listObject;
														angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
															value.selectOnlyOneVisitType = true;
														});
														
														$scope.resultEntryworklistForValidationForFiltter  = $scope.resultEntryworklistForValidation;
														$scope.filterByDummyServices();
														
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							
							$scope.filterByDummyServices= function()
                            {
								$scope.resultEntryworklistForValidation  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.resultEntryworklistForValidation = angular.copy($scope.resultEntryworklistForValidationForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.resultEntryworklistForValidationForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.resultEntryworklistForValidation.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.resultEntryworklistForValidationForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.resultEntryworklistForValidation.push(value);
                                              });
                                               
                                }
                            }
							
							
							
							$scope.generateUrlForReportRelease = function()
							{
							  if($scope.selectedRepReleaseEntryList.length>0)
								  {
								                $rootScope.startLoader();
								                var patientIds = [];
												var flag = false;
												var count = 0;
												
												if($scope.selectedIndex.length>0){
													angular.forEach($scope.selectedIndex,function(val,index){
														angular.forEach($scope.resultEntryworklistForValidation,function(value,indx){
															 if(val < indx && !value.statusFlag){
																// other dept
																 $scope.nextOtherDeptRecords.push(value);
															 }
														 });
													});
												}
												
												angular.forEach($scope.selectedRepReleaseEntryList, function(value, key) {
													value.orgId = value.orgId;
													value.orgUnitId = value.orgUnitId;
													value.createdBy = $scope.userId;
													value.listLabSampleResultDetailsMaster = [];
													patientIds.push(value.patientId);
													$scope.isOnHoldParameter = value.isOnHoldParameter;
													count++;
													if(new Set(patientIds).size==1 && $scope.selectedRepReleaseEntryList.length==count)
													{
													 flag =true;
													 var searchObj = {};
													 searchObj.assignUnit = $scope.assignUnit;
													 searchObj.fromDate = $scope.fromDate;
													 searchObj.toDate = $scope.toDate;
													 searchObj.patientId = $scope.patientId;
													 searchObj.selectedPatient = $scope.selectedPatient;
													 searchObj.uhid = $scope.uhid;
													 searchObj.selectedVisitType = $scope.selectedVisitType;
													 searchObj.reportType = $scope.reportType;
													 
													 $scope.finalOtherDeptRecords = [];
						                                if($scope.nextOtherDeptRecords!=undefined || $scope.nextOtherDeptRecords!=null){
						                                    angular.forEach($scope.nextOtherDeptRecords,function(val,idx){
						                                            var newArr = {
						                                                    subDeptId : "",
						                                                    resultMstList : [],
						                                                    searchObj : {}
						                                            };
					                                                newArr.subDeptId  = val.subDeptId,
					                                                newArr.isOnHoldParameter  = val.isOnHoldParameter,
						                                                newArr.resultMstList.push(val) ,
						                                                newArr.searchObj = searchObj;
						                                                $scope.finalOtherDeptRecords.push(newArr);
						                                    });
						                                }
													 $timeout(function () {
														 $rootScope.stopLoader();
														 $state.go('detailsWorklistForReportRelease',
																	{
																	  subDeptId : subDeptId,
																	  backFlag : 1,
																	  resultMstList : $scope.selectedRepReleaseEntryList,
																	  searchObj : searchObj,
																	  isOnHoldParameter :$scope.isOnHoldParameter,
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
							  else
							     {
								  growl
									.warning("Please Select Test.",
											{
												title : 'Warning!'
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
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getBiochemWorkList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							
							$scope.showPopupRelease = function()
							{
								$('#bulk_release').modal('show');
							}
							
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									$scope.flag = REPORT_RELEASED;
								} else {
									$scope.selectedAll = false;
									$scope.flag = 0;
								}
								angular
										.forEach(
												$scope.resultEntryworklistForValidation,
												function(labResObj) {
													if(labResObj.reportType==NORMAL_REPORT)
														{
														  labResObj.sampleStatusId = $scope.flag;
														}
												});
								$scope.selectGroupRelease();
							}
							
							/**Bulk Normal Report Release Report */
							$scope.reportReleased = REPORT_RELEASED;
							
							$scope.labResultMasterDtoList = [];
							$scope.createLabResultObjectList = function()
							{
								$scope.labResultMasterDtoList = [];
								angular.forEach($scope.resultEntryworklistForValidation, function(value, key) {
									
									if(value.reportType==NORMAL_REPORT && value.sampleStatusId == REPORT_RELEASED)
										{
											value.createdBy =$scope.userId;
											value.updatedBy =$scope.userId;
											value.orgId= $scope.orgId;
											value.orgUnitId=$scope.orgUnitId;
											value.resultLevel = RESULT_RELEASE;
											value.sampleStatusId = REPORT_RELEASED;
											value.assingnedFrom = value.assingnedFrom;
											$scope.labResultMasterDtoList.push(value);
										}
									});
							}
							
							
							$scope.saveBulkRelease= function() 
							{
									try 
									{
										$rootScope.startLoader();
										$scope.createLabResultObjectList();
										
										if($scope.labResultMasterDtoList.length>0)
											{  
											console.log("labResultMasterDtoList",JSON.stringify($scope.labResultMasterDtoList));
											var URI =BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ BULK_REPORT_RELEASE;
											console.log("URI",URI);
											GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
											{	
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$('#bulk_release').modal('hide');
													$scope.selectedAll = false;
													$scope.initResultEntryWorklistForValidation($scope.orgId,
															$scope.orgUnitId,$scope.deptId,$scope.offset,
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
										else{
											growl.warning("Please select Test for Group Release..!!",
													{
														title : 'Warning!'
													});
											$rootScope.stopLoader();
											return false;
										 }
									
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
									
							}
							
							
							/*====================================SEARCH DETAILS=====================================*/
						
							
	                        $scope.visityTypeList = [];
							$scope.selectedVisitType = [];
							
							
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
						    
						    $scope.completed='N';
							$scope.pending = 'N';
							$scope.outsourced = 'N';
							
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

							

						    $scope.dropDownSetting = {
						    		scrollableHeight: '100px', 
						    		scrollable: true
						    }; 
						    
						    $scope.multiselectObj = {
						    		"id":"",
						    		"label":""
						    }
						    
						    /** Service Autocomplete List * */
							$scope.getPatientSearch = function(searchKeyword)
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
											+ LIS_BIOCHEMISTRY
											+ SEARCH_LAB_PATIENT
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

							 $scope.searchLabReportRelease = function()
								{
									try 
									{
										$scope.selectedRepReleaseEntryList.splice(0, $scope.selectedRepReleaseEntryList.length);
										$scope.resultEntryworklistForValidation = [];
										$rootScope.startLoader();
										var searchObj = {
													  "orgId" : $scope.orgId,
													  "orgUnitId" :$scope.assignUnit,
													  "assignUnit" :$scope.orgUnitId ,
													  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
													  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
													  "patientId" :$scope.selectedPatient,
													  "visitTypes" :$scope.selectedVisitType,
													  "deptId": $scope.deptId,
													  "subDeptId": $scope.subDeptId,
													  "priorityId": $scope.priorityId,
													  "reportType": $scope.reportType,
													  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
													  "offset":$scope.offset,
													  "recordPerPage":$scope.noOfRecordsPerPage,
													  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
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
										
										
										console.log("phlebotomyWorkList",JSON.stringify(searchObj));
										var URI = BASE_URL 
										        + ROOT_URL
										        + LIS_BIOCHEMISTRY
												+ LAB_REPORT_RELEASE_SEARCH
										console.log("URI", URI);
										
										var data1 = {
												"orgId":$scope.orgId,
												"orgUnitId" :$scope.assignUnit,
												"assignUnit" :$scope.orgUnitId ,
												"deptId":$scope.deptId,
											    "subDeptId":$scope.subDeptId,
											    "fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												"isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false

										};
										
										var URI2 = BASE_URL
										+ ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ WORK_LIST_FOR_REPORT_RELEASE_COUNT;

								console.log("URI2", URI2);
								
								promiseFactoryWithObject
										.setPromisesWithObject(URI, URI2, METHOD_POST,
												METHOD_POST,searchObj,data1)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.resultEntryworklistForValidation  = response[0].listObject;
													angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
														value.selectOnlyOneVisitType = true;
													});
													$scope.commonListCount = response[1].object;
													$scope.setPage(1,false);
													$scope.isDisableGroupRelease = true;
													if($scope.resultEntryworklistForValidation!=null && $scope.resultEntryworklistForValidation .length>0){
														var found = $scope.resultEntryworklistForValidation.find(obj => {return obj.reportType==NORMAL_REPORT});
														if(found!=undefined){
															$scope.isDisableGroupRelease = false;
														}
														 
													}
													$scope.resultEntryworklistForValidationForFiltter  = $scope.resultEntryworklistForValidation;
													$scope.filterByDummyServices();
												});
										
									} catch (e) {
										console.log(e.message)
									}
								}	
							
							 $scope.tempRepReleaseEntryList=[];
							$scope.selectGroupRelease = function(){
								 $scope.saveBtn = false;
								 var groupRelease = false;
								 var counter=0 ;
								 var repTypecountr=0;
								 angular.forEach($scope.resultEntryworklistForValidation, function(value,index){
										if(value.sampleStatusId == $scope.reportReleased){
											groupRelease = true;
											counter ++;
										}
										if(value.reportType==1){
											repTypecountr++;
										}
								 });
								if(groupRelease){
									
									$scope.reportReleaseBtn = true;
								}else{
									$scope.reportReleaseBtn = false;
								}
								if(counter != repTypecountr){
									$scope.selectedAll =false;
								}else{
									$scope.selectedAll =true;
								}
							}
							
							
                            /*for check same patient having both visitType OP and IP HIS-17745*/
							
							$scope.checkAdmissionAndEncounterAvailableForPatient = function(info) {
								$scope.selectedVisitTypeId = info.visitType;
								$scope.selectedPatientId = info.patientId;
							    var countCheck = 0;
								angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
									if($scope.selectedPatientId == value.patientId){
											if($scope.selectedVisitTypeId != value.visitType){
												value.sampleStatusId = 0;
												value.statusFlag = false;
												value.selectOnlyOneVisitType = false;
											}
									}
								});
								
								angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
									if($scope.selectedPatientId == value.patientId){
									if($scope.selectedVisitTypeId == value.visitType){
										if(value.printReport == true){
											countCheck++;
										}else if(value.statusFlag == true){
											countCheck++;
										}
									}
									}
								});
								angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
									if($scope.selectedPatientId == value.patientId){
										if($scope.selectedVisitTypeId != value.visitType){
										if(countCheck == 0){
											value.selectOnlyOneVisitType = true;
										}
										}
									}
								});
								console.log("list",$scope.resultEntryworklistForValidation);
							}
							$scope.nextSerologyRecords = [];
							$scope.nextOtherDeptRecords = [];
							$scope.selectedIndex = [];
							$scope.selectReport = function(obj,index){
								$scope.reportReleaseBtn = false;
								 if(obj.statusFlag == true){
									 $scope.selectedIndex.push(index);
									 $scope.selectedRepReleaseEntryList.push(obj);
									 $scope.saveBtn = true;
									 
								 }else{
									 var selectedRepReleaseEntryListTemp = [];
									 angular.forEach($scope.selectedRepReleaseEntryList, function(value, key) 
												{
										 			if(value.labSampleDtlsId != obj.labSampleDtlsId){
										 				selectedRepReleaseEntryListTemp.push(value);
										 			}
										 
												});
									 $scope.saveBtn = false;
									 $scope.selectedRepReleaseEntryList = selectedRepReleaseEntryListTemp;
									 var idx = $scope.selectedIndex.findIndex(item => item ==  index);
									 $scope.selectedIndex.splice(idx,1);
								 }
							}
							
							
							 $scope.typeSearchLabReportRelease = function(searchKeyword)
								{
								 if(searchKeyword.length >= 3){
									try 
									{
										$scope.selectedRepReleaseEntryList.splice(0, $scope.selectedRepReleaseEntryList.length);
										$scope.resultEntryworklistForValidation = [];
										$rootScope.startLoader();
										var searchObj = {
													  "orgId" : $scope.orgId,
													  "orgUnitId" :$scope.assignUnit,
													  "assignUnit" :$scope.orgUnitId ,
													  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
													  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
													  "patientId" :$scope.selectedPatient,
													  "visitTypes" :$scope.selectedVisitType,
													  "deptId": $scope.deptId,
													  "subDeptId": $scope.subDeptId,
													  "priorityId": $scope.priorityId,
													  "reportType": $scope.reportType,
													  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
													  "offset":$scope.offset,
													  "recordPerPage":$scope.noOfRecordsPerPage,
													  "searchField":searchKeyword,
													  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
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
										
										
										console.log("phlebotomyWorkList",JSON.stringify(searchObj));
										var URI = BASE_URL 
										        + ROOT_URL
												+ "/LIS/BioChemistry/ReportReleaseWorkList/Type/Search"
										console.log("URI", URI);
										
								
										GenericService.serviceAction(METHOD_POST,URI,searchObj)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.resultEntryworklistForValidation  = response.data.listObject;
													angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
														value.selectOnlyOneVisitType = true;
													});
													$scope.isDisableGroupRelease = true;
													if($scope.resultEntryworklistForValidation!=null && $scope.resultEntryworklistForValidation .length>0){
														var found = $scope.resultEntryworklistForValidation.find(obj => {return obj.reportType==NORMAL_REPORT});
														if(found!=undefined){
															$scope.isDisableGroupRelease = false;
														}
													}
													$scope.resultEntryworklistForValidationForFiltter  = $scope.resultEntryworklistForValidation;
													$scope.filterByDummyServices();
												});
										
									} catch (e) {
										console.log(e.message)
									}
								 }else  if(searchKeyword.length == 0){
									 $scope.searchLabReportRelease();
								 }
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