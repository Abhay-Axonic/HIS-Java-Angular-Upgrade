
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"restainRequestWorklistController",
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
							$scope.LISDynamicLabel = "Block Creation Controller";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.restainRequestWorkList = [];
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
						    $scope.subDeptId = labConfig.histopathologyId;
							
							/**DatePicker Code  */
							
							$scope.fromDate = moment().startOf('day').toDate();
						    $scope.toDate = moment().startOf('day').toDate();

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

							   
							 $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							    $scope.selectedItems = [];
							    $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    }; 
							
							$scope.tRestainRequestMaster =  {
									  "tRestainingReqId" : "",
									  "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "labSampleDtlsId" : "",
									  "tSubSpecimanId" : "",
								      "createdBy" : $scope.userId,
									  "createdDate" : "",
									  "updatedBy" : "",
									  "updatedDate" : "",
									  "isDeleted" : "N",
									  "histopathlogyNumber" : "",
									  "subSpcimanNo" : "",
									  "subSpecimanBarcode" : "",
									  "specimanId" : "",
									  "specimanName" : "",
									  "uhid" : "",
									  "genderName" : "",
									  "patientDetails" : "",
									  "doctorDetails" : "",
									  "dob" : "",
									  "age" : 0,
									  "visitType" : "",
									  "specimanType" : "",
									  "specimanTypeId" : "",
									  "wardCode":"",
									  "bedNumber":"",
									  "listTRestainingRequestDetailsMaster" : []
									}
						
							$scope.setNoOfRecords = function() {
								$scope.initRestainRequestWorkList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initRestainRequestWorkList = function(orgId,
									orgUnitId,deptId,subDeptId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									
									var searchObj = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDate": $scope.fromDate.getTime() ,
			                            	  "toDate":  $scope.toDate.getTime() ,
											  "patientId" : $scope.selectedPatient,
											  "labSampleId" : $scope.labSampleId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" :$scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDeptId" : $scope.subDeptId,
											  "specimanTypes": $scope.selectedspecimanTypeList,
											  "subDepts" :$scope.selectedDepartMentList,
											  "offset":offset,
											  "recordPerPage":noOfRecordsPerPage,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
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
									
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_RESTAIN_REQUEST_WORKLIST;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_RESTAIN_REQUEST_WORKLIST_COUNT;

									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,searchObj,searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.restainRequestWorkList = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.setPage(1, false);
													});
									
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initRestainRequestWorkList($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
									$scope.noOfRecordsPerPage);
							
           
							$scope.getRestainRequestWorkList = function(orgId,
									orgUnitId,deptId,subDeptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var searchObj = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDate": $scope.fromDate.getTime() ,
			                            	  "toDate":  $scope.toDate.getTime() ,
											  "patientId" : $scope.selectedPatient,
											  "labSampleId" : $scope.labSampleId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" :$scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDeptId" : $scope.subDeptId,
											  "specimanTypes": $scope.selectedspecimanTypeList,
											  "subDepts" :$scope.selectedDepartMentList,
											  "offset":offset,
											  "recordPerPage":noOfRecordsPerPage,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD")
	                                   }
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_RESTAIN_REQUEST_WORKLIST;
									console.log("URI", URI);
									
									GenericService
											.serviceAction(METHOD_POST, URI, searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.restainRequestWorkList = response.data.listObject;
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
						$scope.uhid = '';
						$scope.selectedtestList = [];
						$scope.selectedDepartMentList = [];
						$scope.selectedspecimanTypeList = [];
						$scope.fromDate = moment().toDate();
					    $scope.toDate = moment().toDate();
					    $scope.initRestainRequestWorkList($scope.orgId,
								$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
								$scope.noOfRecordsPerPage);
					};

							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								if (flag) 
								{
									$scope.getRestainRequestWorkList($scope.orgId, $scope.orgUnitId,$scope.deptId, $scope.subDeptId,$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							/** code for generating url * */
							$scope.generateRestainDetailsUrl = function(restainReqMst)
								{
								 $scope.tRestainRequestMaster = restainReqMst;
								 $scope.tRestainRequestMaster.createdBy = $scope.userId;
								 $scope.tRestainRequestMaster.orgId = $scope.orgId;
								 $scope.tRestainRequestMaster.orgUnitId = $scope.orgUnitId;
								 $scope.tRestainRequestMaster.listTRestainingRequestDetailsMaster = []; 
											$state.go('restainingRequestWorklistDetails',
												{
												  restainReqMstObj : $scope.tRestainRequestMaster,
												});
								}
							
							
							$scope.getBlockCreationDetails = function(grossObj) {
								try 
								{
									$rootScope.startLoader();
									$scope.histopathlogyNumber = grossObj.histopathlogyNumber;
									$scope.subSpecimanName = grossObj.subSpecimanName;
									$scope.tGrossMaster = grossObj;
									$scope.tGrossMaster.orgId = $scope.orgId;
									$scope.tGrossMaster.orgUnitId = $scope.orgUnitId;
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_GET_GROSS_DETAILS;
									console.log("URI", URI);
									
									GenericService
											.serviceAction(METHOD_POST, URI, $scope.tGrossMaster)
											.then(
													function(response)
													{
														if (response.data.status == 'success') 
														{
															$rootScope.stopLoader();
															$scope.listTGrossMaster = response.data.listObject;
															$("#details").modal('show');

														} else if (response.data.status == 'error') 
														{
															$rootScope.stopLoader();
															growl.error("Something went Wrong",	{title : 'Error!'});
														}
													
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							 // code for Activating Inactivating Sample
							$scope.saveBlockDetails = function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.blockCreationForm.$valid)
								{
								$rootScope.startLoader();
								try {
									console.log("$scope.listTGrossMaster",JSON.stringify($scope.listTGrossMaster));
									var URI = BASE_URL 
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_CREATE_BLICKS;
									console.log("CREATE GROSS", URI);
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.listTGrossMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{
															$("#details").modal('hide');
															$scope.initBlockCreationWorklist($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
															 growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.$broadcast('show-errors-reset');

														} else if (response.data.status == 'error') 
														{
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
						}
							$scope.blockCaptureNoteReq = "Capture note is required.";	
							
							
							
							
							
  //Search Filter
							
							$scope.selectedVisitTypeIds = [];
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							$scope.specimanTypeList =[];
							
							$scope.selectedspecimanTypeList = [];
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							
							
							$scope.getMicroPatientList = function(searchKeyword)
							{
								if(searchKeyword.length == 3)
								{
								try 
								{
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"deptId":$scope.deptId,
											"subDeptId":$scope.subDeptId,
											"searchKeyword":searchKeyword,
									}
									
									var URI = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTO_MICRO_PATIENT_SEARCH
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
										$scope.visityTypeList =  response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.getVisitTypeList();
							
							$scope.getSpecimanList = function()
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
											+ HISTO_MICRO_SPECIMAN_LIST
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											data).then(function(response) 
									{
									  $scope.specimanTypeList =  response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getSpecimanList();
							
							$scope.getTestList = function()
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
											+ HISTO_MICRO_TEST_LIST
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											data).then(function(response) {
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
							
							$scope.selectPatient = function($item, $model,$label)
							{
						      $scope.selectedPatient = $item.id;
					        }
					    $scope.patientBlank = function()
						{
					      if($scope.patientId==undefined)
					    	  {
					    	    $scope.selectedPatient =null;
					    	  }
				        }
						$scope.searchRestainList= function()
						{
							$scope.initRestainRequestWorkList($scope.orgId,	$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
									$scope.noOfRecordsPerPage);
						}

						} ]);