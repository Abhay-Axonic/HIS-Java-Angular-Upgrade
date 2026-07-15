/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"multiParameterTestController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'$timeout',
					
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl, $timeout) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Department";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							
							$scope.addUpdateFlag = true;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.multiParamTestMasterListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.microbiologyId = labConfig.microbiologyId;
							$scope.isHistoCyto="N";
							
							/* code for setting Label */
							$(".selectedPageName").text("Multyparameter Test");

							$scope.multyParamTestList = [];
							$scope.departmentList;
							$scope.sampleList;
							$scope.unitList;
							$scope.containerList;
							$scope.reportList;
							$scope.techniqueList;
							$scope.prerequisitesList;
							$scope.timeList;
							$scope.noOfDayList;
							$scope.ageGroupList;
							$scope.genderList;
							$scope.headerList;
							$scope.parameterList;

							$scope.getDepartMentList = function(orgId,orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_DEPARMENT_LIST
											+ S + orgId + S + orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.departmentList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);

							$scope.getSampleList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_SAMPLETYPE_LIST
											+ S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.sampleList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getSampleList($scope.orgId);

							$scope.getUnitList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_UNIT_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.unitList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getUnitList($scope.orgId);

							$scope.getContainerList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_CONTAINER_LIST
											+ S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.containerList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getContainerList($scope.orgId);

							$scope.getReportList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_REPORTTYPE_LIST
											+ S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.reportList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getReportList($scope.orgId);

							$scope.getTechniqueList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_TECHNIQUE_LIST
											+ S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.techniqueList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getTechniqueList($scope.orgId);

							$scope.getPrerequisitesList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ GET_PREREQUISITES_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.prerequisitesList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getPrerequisitesList($scope.orgId);

							$scope.getTurnArroundTimeList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ GET_TRUNAROUND_TIME_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.timeList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getTurnArroundTimeList($scope.orgId);

							$scope.getnoOfDayList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_DAY_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.getnoOfDayList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getnoOfDayList($scope.orgId);

							$scope.getAgeGroupList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_AGE_GROUP_LIST
											+ S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.ageGroupList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getAgeGroupList($scope.orgId);

							$scope.getGenderList = function(orgId, orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_GENDER_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.genderList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getGenderList($scope.orgId);

						/*	$scope.getHeaderList = function(orgId, orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_HEADER_LIST + S
											+ orgId + S + orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.headerList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}*/
							//$scope.getHeaderList($scope.orgId,$scope.orgUnitId);
							
							
							$scope.getTypeHeadHeaderList = function(search) {
								try {
									if(search!="" && search!=undefined && search!=null && search.length>=3){
										var data = "";
										var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + "/getTypeHeadSearchHeaderList" + S
											+ $scope.orgId + S + $scope.orgUnitId + S + search;
										console.log("URI", URI);
										return GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														if(response.data.listObject!=null && response.data.listObject.length>0){
															return response.data.listObject;
														}else{
															var newArr = {};
															var list = [];
															newArr.name = "No Record Found"
																list.push(newArr);
															return list;
														}
													});
										}
									} catch (e) {
										console.log(e.message)
									}
							}

							/*$scope.getParameterList = function(orgId, orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_PARAMETER_LIST
											+ S + orgId + S + orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.parameterList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getParameterList($scope.orgId,$scope.orgUnitId);*/
							
							
							$scope.getTypeHeadParameterList = function(search) {
								try {
									if(search!="" && search!=undefined && search!=null && search.length>=3){
										var data = "";
										var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + "/getTypeHeadSearchParameterList" + S
											+ $scope.orgId + S + $scope.orgUnitId + S + search;
									
									console.log("URI", URI);
									return GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														if(response.data.listObject!=null && response.data.listObject.length>0){
															return response.data.listObject;
														}else{
															var newArr = {};
															var list = [];
															newArr.name = "No Record Found"
																list.push(newArr);
															return list;
														}
													});
									}
								} catch (e) {
									console.log(e.message)
								}
							}
							

							/** Service Autocomplete List * */
							$scope.getServiceList = function(searchKeyword) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
									        + "/billing"
											+ AUTO_COMPLETE_SERVICES_LAB + S
											+ $scope.orgId + S
											+ $scope.orgUnitId + S
											+ $scope.deptId + S
											+ searchKeyword;
								 	console.log("URI", URI);
								 	return GenericService.serviceAction("GET", URI,
											data).then(function(response) {
										return response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}

							$scope.selectServiceId = function($item, $model,
									$label) {
								$scope.multiParamTestMaster.serviceId = $item.id;
								try {
									var data = {
										serviceId : $item.id,
										orgId : $scope.orgId,
										orgUnitId : $scope.orgUnitId,
										testType : 1,
										testId : $scope.multiParamTestMaster.testId
									};
									var URI = BASE_URL + ROOT_URL
											+ LIS_UNIT + CHECK_SERVICE_AVAIABLE+"/"+$scope.addUpdateFlag;
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.serviceCount = response.data.object;
														$scope.multiParamTestMaster.isOutsourced = response.data.object1.isOutsource;
														if (response.data.object > 0) {
															$scope.isServiceExist = true;
															growl.error("Service is already Exists.",
																	{
																		title : 'Error!'
																	});
															$scope.multiParamTestMaster.testDesc = "";
															
														} else {
															$scope.isServiceExist = false;
														}
													});
								} catch (e) {
									console.log(e.message)
								}

							}

							/** =====================>Code for Push Pop Objects<=============== */

							$scope.parameterDetailList = [];
							$scope.getParameterDetailsListByHeaderList = function(headerId) {
								try {
									$rootScope.startLoader();
									var data = "";
									var URI = BASE_URL + ROOT_URL + "/LIS/global/getHeaderParameterList"
											+ S + $scope.orgId + S + $scope.unitId + S + headerId;
									 GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.parameterDetailList =  response.data.listObject;
														if($scope.parameterDetailList!=null && $scope.parameterDetailList.length>0){
														 var isPopupShow = true;
														 if($scope.listTestParamMpprMaster!=null && $scope.listTestParamMpprMaster.length>0){
															 angular.forEach($scope.parameterDetailList ,function(value,index){
																 angular.forEach($scope.listTestParamMpprMaster,function(val,idx){
																	 if(value.headerId == val.headerId && value.parameterId == val.parameterId){
																		 isPopupShow = false;
																	 }
																 });
															 });
														 }
														 if(isPopupShow){
															 angular.element('#headerModal').modal('show');
														 }
														}
														$rootScope.stopLoader();
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.headerObj = {id: "",name:""};
							$scope.parameterObj = {id: "",name:""};
						/*	$scope.selectHeader = function(headerId) {
								$scope.headerObj = $scope.headerList
										.filter(function(headerMaster) {
											if (headerMaster.id == headerId) {
												return headerMaster;
											}
										});
								 $scope.getParameterDetailsListByHeaderList(headerId);
							}*/
							
							$scope.selectSearchedHeader = function($item, $model,$label){
								if( $item.id!=null &&  $item.id!=undefined){
									$scope.headerObj.id=  $item.id;
									$scope.headerObj.name = $item.name;
									$scope.headerId = $item.id;
									$scope.getParameterDetailsListByHeaderList($item.id);
								}else{
									$scope.headerDesc = "";
								}	
							}

						/*	$scope.selectParameter = function(parameterId) {
								$scope.parameterObj = $scope.parameterList
										.filter(function(parameterMaster) {
											if (parameterMaster.id == parameterId) {
												return parameterMaster;
											}
										});
							}*/
							
							$scope.selectSearchedParameter = function($item, $model,$label) {
								if( $item.id!=null &&  $item.id!=undefined){
									$scope.parameterObj.id =  $item.id ;
									$scope.parameterObj.name = $item.name;
									$scope.parameterId = $item.id;
								}else{
									$scope.parameterDesc = "";
								}
							}

							$scope.testParamList = [];
							$scope.addParameterMapping = function() {
								
								if(isNaN($scope.paraSequnce)||Number($scope.paraSequnce)==0)
									{
									growl.error("Please Enter Valid Parameter Sequence",
											{
												title : 'Error!'
											});
									  return false;
									}
								if($scope.headerId==null||$scope.headerId==undefined)
								{
								growl.error("Please Select Header.",
										{
											title : 'Error!'
										});
								  return false;
								}
								
								if($scope.parameterId==null||$scope.parameterId==undefined)
								{
								growl.error("Please Select Parameter.",
										{
											title : 'Error!'
										});
								  return false;
								}

								var checkParaSeq = false;
								if( $scope.listTestParamMpprMaster  != null && $scope.listTestParamMpprMaster.length >0){
									
									angular.forEach($scope.listTestParamMpprMaster,function(value,index){
										
										if(value.paraSequence == Number($scope.paraSequnce)){
											checkParaSeq = true;
										}
										
									});
								}
								
								if(checkParaSeq)
								{
									growl.error("Please Enter Valid Parameter Sequence",
											{
												title : 'Error!'
											});
									  return false;
								}
								
								
								var isExists;
								function checkExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												return (val.parameterId === arr.parameterId);
														/*&& (val.headerId === arr.headerId);*/
											});
								}

								if ($scope.updateScope == false) {

									var testParamMpprMaster = angular
											.copy($scope.testParamMpprMaster);
									testParamMpprMaster.headerId = $scope.headerObj.id;
									testParamMpprMaster.headerDesc = $scope.headerObj.name;
									testParamMpprMaster.parameterId = $scope.parameterObj.id;
									testParamMpprMaster.parameterDesc = $scope.parameterObj.name;
									testParamMpprMaster.paraSequence = $scope.paraSequnce;
									isExists = checkExistsOrNot(
											$scope.listTestParamMpprMaster,
											testParamMpprMaster);
									if (!isExists) {
										$scope.listTestParamMpprMaster
												.push(testParamMpprMaster);
                                        $scope.headerDesc = "";
                                        $scope.headerId = null;
										$scope.parameterId = null;
                                        $scope.parameterDesc = "";
                                        $scope.paraSequnce= "";
                                    	$scope.headerObj = {id: "",name:""};
            							$scope.parameterObj = {id: "",name:""};

										
									} else {
										growl.error("Parameter is already Added To.",
												{
													title : 'Error!'
												});
									}

								} else if ($scope.updateScope == true) {
									testParamMpprMaster = angular
											.copy($scope.testParamMpprMaster);
									testParamMpprMaster.testId = $scope.multiParamTestMaster.listTestParamMppr.length == 0 ? $scope.multiParamTestMaster.testId : $scope.multiParamTestMaster.listTestParamMppr[0].testId;
									testParamMpprMaster.headerDesc = $scope.headerObj.name;
									testParamMpprMaster.parameterId = $scope.parameterObj.id;
									testParamMpprMaster.headerId = $scope.headerId;
									testParamMpprMaster.parameterDesc = $scope.parameterObj.name;
									testParamMpprMaster.paraSequence = $scope.paraSequnce;
									isExists = checkExistsOrNot(
											$scope.listTestParamMpprMaster,
											testParamMpprMaster);
									if (!isExists) {
										$scope.listTestParamMpprMaster
												.push(testParamMpprMaster);
										    $scope.headerDesc = "";
										    $scope.headerId = null;
											$scope.parameterId = null;
	                                        $scope.parameterDesc = "";
	                                        $scope.paraSequnce= "";
	                                    	$scope.headerObj = {id: "",name:""};
	            							$scope.parameterObj = {id: "",name:""};
									} else {
										growl.error("Parameter is already Added To.",
												{
													title : 'Error!'
												});
									}
								}

							};

							$scope.removeParameterMapping = function(
									deleteIndex) {
								if ($scope.updateScope == false) {
									$scope.listTestParamMpprMaster.splice(
											deleteIndex, 1);
								} else if ($scope.updateScope == true) {
									var testParamMpprMaster = angular
											.copy($scope.multiParamTestMaster.listTestParamMppr[deleteIndex]);
									testParamMpprMaster.parameterId = $scope.multiParamTestMaster.listTestParamMppr[deleteIndex].parameterId;
									testParamMpprMaster.testId = $scope.multiParamTestMaster.listTestParamMppr[0].testId;
									testParamMpprMaster.isDeleted = 'Y';
									$scope.testParamList
											.push(testParamMpprMaster);
									$scope.listTestParamMpprMaster.splice(
											deleteIndex, 1);
								}
							}

							$scope.showFootNote = function(testIndex) 
							{
								$('#footNoteSingle').modal('show');
								$scope.footNote = $scope.multiParamTestList[testIndex].footNote;

							}

							/**
							 * =================>OBJECT DELCLARATION
							 * <========================= *
							 */
							$scope.listParameterMaster = [];
							$scope.listParamRefrengMaster = [];
							$scope.listHelpValueMaster = [];
							$scope.listTestParamMpprMaster = [];

							$scope.paramRefrengMaster = {
								"refrengId" : "",
								"parameterId" : "",
								"genderId" : "",
								"minValue" : "",
								"maxValue" : "",
								"lessThan" : "",
								"moreThan" : "",
								"ageGroupId" : "",
								"remark" : "",
								"refrengStatus" : "A",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"isDeleted" : "N"
							}

							$scope.helpValueMaster = {
								"helpValueId" : "",
								"parameterId" : "",
								"helpValue" : "",
								"status" : "A",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"isDeleted" : "N"
							}

							$scope.testParamMpprMaster = {
								"testPerMpprId" : "",
								"testId" : "",
								"parameterId" : "",
								"parameterDesc" : "",
								"paraSequence" : "",
								"headerId" : "1",
								"headerDesc" : "",
								"testParaStatus" : "A",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"isDeleted" : "N"
							}

							$scope.parameterMaster = {
								"parameterId" : "",
								"parameterName" : "",
								"aliesName" : "",
								"parameterCode" : "",
								"unitId" : "",
								"deltaDaysId" : "",
								"deltaPer" : "",
								"testType" : 1,
								"isMultyparameter" : "N",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"status" : "A",
								"isFormula":"N",
								"listParamRefrengMaster" : $scope.listParamRefrengMaster,
								"listHelpValueMaster" : $scope.listHelpValueMaster
							}

							$scope.initMultyParamTest = function() {
								$scope.addUpdateFlag = true;
								$scope.cultureTestCheck = false;
								$scope.testParamList = [];
								$scope.listTestParamMpprMaster = [];
								// $scope.listTestParamMpprMaster.push(angular.copy($scope.testParamMpprMaster));
								$scope.multiParamTestMaster = {
									"testId" : "",
									"testCode" : "",
									"testDesc" : "",
									"testAlies" : "",
									"deptId" : "",
									"sampleId" : "",
									"noOfSampleReq" : "",
									"sampleVolume" : "",
									"containerId" : "",
									"reportTypeId" : "",
									"techniqueId" : "",
									"identificationNo" : "",
									"prerequisitsId" : "",
									"proRelease" : "",
									"testStatus" : "",
									"isOutsourced" : "",
									"footNote" : "",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : "",
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"orgUnitId" : $scope.orgUnitId,
									"normlTatTime" : "",
									"normlTatId" : "",
									"urgentTatTime" : "",
									"urgentTatId" : "",
									"serviceId" : "",
									"testType" : 1,
									"unitId" :"",
									"isHistoCyto":"N",
									"specimanId":"",
									"isGynac":'N',
									"isFnac":'N',
									"isCentrifugationRequried":"A",
									"isUniqueBarcode":"N",
									"listParameterMasterDto" : $scope.listParameterMaster,
									"listTestParamMppr" : $scope.listTestParamMpprMaster,
									"cultureTest":false,
								}
							}
							$scope.initMultyParamTest();
							// ====================================================CODE
							// START FOR SINGLE PARAM MASTER
							// LIST===========================================================

							$scope.setNoOfRecords = function() {
								$scope.initMultiParamTestMasterList(
										$scope.orgId, $scope.orgUnitId,
										$scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initMultiParamTestMasterList = function(
									orgId, orgUnitId, offset,
									noOfRecordsPerPage) {
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI1 = BASE_URL + ROOT_URL
										+ LIS_UNIT
										+ LIST_MULTIPARAM_TEST_MASTER + S
										+ orgId + S + $scope.isHistoCyto + S + orgUnitId + S + offset
										+ S + noOfRecordsPerPage;
								var URI2 = BASE_URL + ROOT_URL
										+ LIS_UNIT + GET_TOTAL_MULTITEST_RECORD
										+ S + orgId + S + $scope.isHistoCyto + S + orgUnitId;
								
								console.log("URI1",URI1);
								console.log("URI2",URI2);
								promiseFactory
										.setPromises(URI1, URI2, "GET", "GET")
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.multiParamTestList = response[0].listObject;
													$scope.multiParamTestMasterListCount = response[1].object;
													$scope.setPage(1, false);
												});
							}

							$scope.getMultiParamTestMasterList = function(
									orgId, orgUnitId, offset,
									noOfRecordsPerPage) {
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ LIS_UNIT
										+ LIST_MULTIPARAM_TEST_MASTER + S
										+ orgId + S + $scope.isHistoCyto + S + orgUnitId + S + offset
										+ S + noOfRecordsPerPage;
								console.log("URI", URI);
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.multiParamTestList = response.data.listObject;
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.multiParamTestMasterListCount,
										page, $scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getMultiParamTestMasterList(
											$scope.orgId, $scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initMultiParamTestMasterList($scope.orgId,
									$scope.orgUnitId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// ====================================================CODE
							// END FOR SINGLE PARAM
							// LIST===========================================================

							/** CRUD FOR SINGLE PARAM TEST MASTER * */
							// code for Activating Inactivating Sample
							$scope.activeInactiveMultiParamTest = function(index,
									orgId, orgUnitId, testId, testStatus,serviceId) {
								try {
									$rootScope.startLoader();
									$scope.initMultyParamTest();
									var URI = BASE_URL + ROOT_URL
											+ LIS_UNIT
											+ ACTIVE_INACTIVEMULTIPARAM_TEST
											+ S + orgId + S + orgUnitId + S
											+ testId + S + testStatus + S + serviceId;
									console.log("ACT_INACT_URI", URI);
									GenericService
											.serviceAction("GET", URI,
													$scope.multiParamTestMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success(response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															$scope.initMultyParamTest();
														} else if (response.data.status == 'error') {
															$rootScope.stopLoader();
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
															 
															if(response.data.object != null && response.data.object == 0){
																$scope.multiParamTestList[index].testStatus = 'I';
															}
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}

							}

							$scope.getMultiParamTestById = function(orgId,
									orgUnitId, testId) {
								try {
									$rootScope.startLoader();
									$scope.updateScope = true;
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									$scope.addUpdateFlag= false;
									var URI = BASE_URL + ROOT_URL
											+ LIS_UNIT
											+ GET_MULTYPARAMETER_TEST + S
											+ orgId + S + orgUnitId + S
											+ testId;
									console.log("EDIT", URI);
									GenericService
											.serviceAction("GET", URI,
													$scope.multiParamTestMaster)
											.then(
													function(response) {
														console.log("data",
																response.data);
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															$scope.multiParamTestMaster = response.data.object;
									                        $scope.unitIdData = $scope.unitList.find(function(obj){
									                             return obj.id == response.data.object.unitId;
										                    });
									                        if($scope.unitIdData == undefined || $scope.unitIdData == null || $scope.unitIdData == 0){
									                        	$scope.multiParamTestMaster.unitId = "";
									                        }
									                        else{
									                        	$scope.multiParamTestMaster.unitId = response.data.object.unitId+"";
									                        }
															$scope.listTestParamMpprMaster = $scope.multiParamTestMaster.listTestParamMppr;
															$scope.multiParamTestMaster.deptId = response.data.object.deptId
																	+ "";
															$scope.multiParamTestMaster.sampleId = response.data.object.sampleId
																	+ "";
															$scope.multiParamTestMaster.listParameterMasterDto[0].unitId = response.data.object.listParameterMasterDto[0].unitId
																	+ "";
															$scope.multiParamTestMaster.containerId = response.data.object.containerId
																	+ "";
															$scope.multiParamTestMaster.reportTypeId = response.data.object.reportTypeId
																	+ "";
															$scope.multiParamTestMaster.techniqueId = response.data.object.techniqueId
																	+ "";
															$scope.multiParamTestMaster.prerequisitsId = response.data.object.prerequisitsId
																	+ "";
															$scope.multiParamTestMaster.normlTatId = response.data.object.normlTatId
																	+ "";
															$scope.multiParamTestMaster.urgentTatId = response.data.object.urgentTatId
																	+ "";
															$scope.multiParamTestMaster.listParameterMasterDto[0].deltaDaysId = response.data.object.listParameterMasterDto[0].deltaDaysId
																	+ "";
															if($scope.microbiologyId != undefined && $scope.microbiologyId != null && $scope.microbiologyId == $scope.multiParamTestMaster.deptId){
																$scope.cultureTestCheck = true;
															}else{
																$scope.cultureTestCheck = false;
															}
															$scope.selectedVisitType=response.data.listObject;
															console.info("$scope.selectedVisitType"+$scope.selectedVisitType)
															console.log(
																			"$scope.multiParamTestMaster",
																			$scope.multiParamTestMaster);
														} else if (response.data.status == 'error') {
															$rootScope
																	.stopLoader();
															alert("Error In Fetching Data");
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}

							}

/*----------EquipmentsList Start------------*/

							$scope.checkMicroDept =function(){
								if($scope.microbiologyId != undefined && $scope.microbiologyId != null && $scope.microbiologyId == $scope.multiParamTestMaster.deptId){
									$scope.cultureTestCheck = true;
								}else{
									$scope.cultureTestCheck = false;
								}
							}
							
							$scope.selectedVisitType = [];
							$scope.equipmentIdList=[];
							$scope.getEquipmentsList = function() {
								var data = {};
								
								var URI = BASE_URL + ROOT_URL
								+ '	/api/opd/equipments/'+$scope.orgUnitId;
					
								GenericService
								.serviceAction(METHOD_GET, URI,
										{},
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.equipmentList = response.data.listObject;
											console.log("---------- EquipmentsList end-----------"+$scope.equipmentList)
											console.log("---------- EquipmentsList -----------"+JSON.stringify($scope.equipmentList))
											  angular.forEach($scope.equipmentList,function(value,index){
							            		  
							            		   
							            			   var equipment={};
							            			   equipment.id = value.id;
							            			   equipment.label = value.name;
														$scope.equipmentIdList.push(equipment);
							            		   
													});
										});
									}
					      $scope.getEquipmentsList();
					      /*---------- EquipmentsList end------------*/
					      
					      
						$scope.saveMultiParamTest = function() {
								
							if($scope.isSaved){
								return;
							}
								$scope.multiParamTestMaster.equipmentList =$scope.selectedVisitType;
								$scope.isSaved=true;
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.multiParamTestForm.$valid) {
									try {
										$rootScope.startLoader();
										$scope.multiParamTestMaster.listParameterMasterDto = [];
										$scope.listTestParamMpprMaster.sort((a, b) => parseInt(a.paraSequence) - parseInt(b.paraSequence));
										$scope.multiParamTestMaster.listTestParamMppr = $scope.listTestParamMpprMaster;
										console
												.log(
														"multiParamTestMaster",
														JSON
																.stringify($scope.multiParamTestMaster));
										var URI = BASE_URL + ROOT_URL
												+ LIS_UNIT
												+ ADD_MULTIPARAMETER_TEST;

										GenericService
												.serviceAction(
														"POST",
														URI,
														$scope.multiParamTestMaster)
												.then(
														function(response) {
															if (response.data.status == 'success') {
																$rootScope
																		.stopLoader();
																growl
																		.success(
																				response.data.message,
																				{
																					title : 'Success!'
																				});
																$scope.isSaved=false;
																$scope.$broadcast('show-errors-reset');
																$scope.initMultyParamTest();
																$scope.selectedVisitType =[];
																$scope.headerDesc = "";
																$scope.headerId = null;
																$scope.parameterId = null;
																$scope.parameterDesc = "";
																$scope.paraSequnce= "";
																$scope.headerObj = {id: "",name:""};
																$scope.parameterObj = {id: "",name:""};
																$scope.initMultiParamTestMasterList(
																				$scope.orgId,
																				$scope.orgUnitId,
																				$scope.offset,
																				$scope.noOfRecordsPerPage);
															} else if (response.data.status == 'error') {
																$rootScope.stopLoader();
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$scope.isSaved=false;
															}
														});

									} catch (e) {
										console.log("Exception", e.message);
										$scope.isSaved=false;
									}

								}
							}
							
							//-----///
							$scope.showFieldList = function(testId){
								console.log(testId)
								try{
									$scope.fieldMasterList=[];
									var data={};
									var URI = BASE_URL + ROOT_URL + "/LIS/unit/getEquipmentList" +"/"+ testId +"/"+ $scope.orgId +"/"+$scope.orgUnitId;
									
									GenericService.serviceAction("GET",URI,data).then(function(response){
										
										if(response.data.status == "success"){
				 							$scope.fieldMasterList= response.data.listObject;
				 							console.log($scope.fieldMasterList)
										}else if(response.data.status == 'error'){
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
									
								}catch(e){
									console.log("Exception-",e);
								}
							}
							///-----//
							$scope.updateMultiParamTest = function() {
								 $scope.multiParamTestMaster.equipmentList =$scope.selectedVisitType;
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.multiParamTestForm.$valid) {
									try {
										$rootScope.startLoader();
										$scope.listTestParamMpprMaster.sort((a, b) => parseInt(a.paraSequence) - parseInt(b.paraSequence));
										$scope.multiParamTestMaster.listParameterMasterDto = [];
										$scope.multiParamTestMaster.listTestParamMppr = [];
										$scope.multiParamTestMaster.listTestParamMpprList = $scope.listTestParamMpprMaster
										
										.concat($scope.testParamList);
										console	.log(
														"multiParamTestMaster",
														JSON
																.stringify($scope.multiParamTestMaster));
										var URI = BASE_URL + ROOT_URL
												+ LIS_UNIT
												+ UPDATE_MULTIPARAMETER_TEST;

										GenericService
												.serviceAction(
														"PUT",
														URI,
														$scope.multiParamTestMaster)
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
																$scope.saveBtnFlag = true;
																$scope.updateScope = false;
																$scope.updateBtnFlag = false;
																$scope.selectedVisitType =[];
																$scope.initMultyParamTest();
																$scope.headerDesc = "";
																$scope.headerId = null;
																$scope.parameterId = null;
																$scope.parameterDesc = "";
																$scope.paraSequnce= "";
																$scope.headerObj = {id: "",name:""};
																$scope.parameterObj = {id: "",name:""};
																$scope.initMultiParamTestMasterList(
																				$scope.orgId,
																				$scope.orgUnitId,
																				$scope.offset,
																				$scope.noOfRecordsPerPage);
															} else if (response.data.status == 'error') {
																$rootScope.stopLoader();
																growl.error(
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
							}
							
							$scope.getTestDetails = function(testId) {
								try 
								{
									$scope.testDetails = {};
									$rootScope.startLoader();
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_UNIT + SINGLE_PARAM_TEST_DETAILS
											+ S + $scope.orgId+S+$scope.orgUnitId+S+testId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) 
													{
														if(response.data.status=="success")
															{
															$scope.testDetails = response.data.listObject[0];
															$rootScope.stopLoader();
															$('#otherDetailsModalM').modal('show');
															}
														else if(response.data.status=="error")
															{
															  $rootScope.stopLoader();
															}
														
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.showParameters = function(testId) {
								try 
								{
									$scope.lstTestParameterMppr = [];
									$rootScope.startLoader();
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_UNIT + S+'parametersByTest'
											+ S + testId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) 
													{
														if(response.data.status=="success")
															{
															$scope.lstTestParameterMppr = response.data.listObject;
															$('#headerParameter').modal('show');
															$rootScope.stopLoader();
															}
														else if(response.data.status=="error")
															{
															  $rootScope.stopLoader();
															}
														
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.searchMultiParamTestMaster = function(testDesc){
								$rootScope.startLoader();
								$scope.multiParamTestList=[];
								$scope.loadingMsg="Loading...";
								if(testDesc!=''){
								var URI = BASE_URL + ROOT_URL +"/LIS/unit/multiParamTestMasterUniversalSearch"
								+ S + $scope.organizationId
								+ S + $scope.isHistoCyto
								+ S + $scope.unitId
								+ S + $scope.offset
								+ S + $scope.noOfRecordsPerPage 
								+ S + testDesc ;
								console.log("URI", URI);
								GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) 
										{
													$rootScope.stopLoader();
													$scope.multiParamTestList = response.data.listObject;
													if($scope.multiParamTestList.length==0)
														$scope.loadingMsg="No record found...";
												});
								}else{
									$scope.initMultiParamTestMasterList($scope.orgId,$scope.orgUnitId,$scope.offset,$scope.noOfRecordsPerPage);
								}
								$rootScope.stopLoader();
							}
							
							$scope.addMappedParameterList = function(){
								var paramIsExist = false;
								var isExists;
								if ($scope.updateScope == false) {
									var parameterList = "";
									var headerList = "";
									angular.forEach($scope.parameterDetailList,function(value,index){
										var testParamMpprMaster = angular.copy($scope.testParamMpprMaster);
										testParamMpprMaster.headerId = value.headerId;
										testParamMpprMaster.headerDesc = value.headerName;
										testParamMpprMaster.parameterId = value.parameterId;
										testParamMpprMaster.parameterDesc = value.parameterName;
										testParamMpprMaster.paraSequence = value.paraSequence;
										 isExists = $scope.listTestParamMpprMaster.find(obj => {return obj.parameterId == testParamMpprMaster.parameterId});
										if (isExists == undefined) {
											$scope.listTestParamMpprMaster.push(testParamMpprMaster);
											$scope.headerDesc = "";
											$scope.headerId = null;
											$scope.parameterId = null;
											$scope.parameterDesc = "";
											$scope.paraSequnce= "";
											$scope.headerObj = {id: "",name:""};
											$scope.parameterObj = {id: "",name:""};
										}else{
											paramIsExist = true;
											if(parameterList == ""){
												parameterList = isExists.parameterDesc;
											}else {
												parameterList = parameterList+","+isExists.parameterDesc;
											}
											if(headerList == ""){
												headerList = isExists.headerDesc;
											}else{
												headerList =  headerList+"," +isExists.headerDesc;
											}
										}
									});

									if(paramIsExist){
										var param = parameterList.split(",");
										if(param.length>1){
											growl.warning(parameterList +" Parameters are already Added in "+headerList +" Headers",
													{
														title : 'warning!'
													});
											return false;
										}else{
										growl.warning(parameterList +" Parameter is already Added in "+headerList+" Header",
												{
													title : 'warning!'
												});
										return false;
										}
									}
									
								} else if ($scope.updateScope == true) {
									var parameterList = "";
									var headerList = "";
									angular.forEach($scope.parameterDetailList,function(value,index){
										var testParamMpprMaster = angular.copy($scope.testParamMpprMaster);
										testParamMpprMaster.testId = $scope.multiParamTestMaster.listTestParamMppr.length == 0 ? $scope.multiParamTestMaster.testId : $scope.multiParamTestMaster.listTestParamMppr[0].testId;
										testParamMpprMaster.headerId = value.headerId;
										testParamMpprMaster.headerDesc = value.headerName;
										testParamMpprMaster.parameterId = value.parameterId;
										testParamMpprMaster.parameterDesc = value.parameterName;
										testParamMpprMaster.paraSequence = value.paraSequence;
										 isExists = $scope.listTestParamMpprMaster.find(obj => {return obj.parameterId == testParamMpprMaster.parameterId});
										 if (isExists == undefined) {
												$scope.listTestParamMpprMaster.push(testParamMpprMaster);
												$scope.headerDesc = "";
												$scope.headerId = null;
												$scope.parameterId = null;
												$scope.parameterDesc = "";
												$scope.paraSequnce= "";
												$scope.headerObj = {id: "",name:""};
												$scope.parameterObj = {id: "",name:""};
											}else{
												paramIsExist = true;
												if(parameterList == ""){
													parameterList = isExists.parameterDesc;
												}else {
													parameterList = parameterList+","+isExists.parameterDesc;
												}
												if(headerList == ""){
													headerList = isExists.headerDesc;
												}else{
													headerList =  headerList+"," +isExists.headerDesc;
												}
											}
									});
									if(paramIsExist){
										var param = parameterList.split(",");
										if(param.length>1){
											growl.warning(parameterList +" Parameters are already Added in "+headerList +" Headers",
													{
														title : 'warning!'
													});
											return false;
										}else{
										growl.warning(parameterList +" Parameter is already Added in "+headerList+" Header",
												{
													title : 'warning!'
												});
										return false;
										}
									}
								}

							}
							
							/**
							 * =============================>Errors Messages
							 * <=======================================*
							 */
							$scope.testNameReq = 'Test name is required.';
							$scope.testCodeReq = 'Test code is required.';
							$scope.testAliesReq = 'Alias name is required.';
							$scope.deptReq = 'Please select department.';
							$scope.sampleReq = 'Please select sample.';
							$scope.noOfSampleReq = 'Please enter no of sample.';
							$scope.sampleVolumeReq = 'Please enter sample volume.';
							$scope.sampleUnitReq = 'Please select sample unit.';
							$scope.containerReq = 'Please select container type.';
							$scope.reportTypeReq = 'Please select report type.';
							$scope.techniqueReq = 'Please select technique type.';
							$scope.identificationNoReq = 'Please enter identification number.';
							$scope.footNoteReq = 'Please enter foot note.';
							$scope.urgentTatReq = 'Please select urgent time.';
							$scope.urgentTatTimeReq = 'Please enter argent time.';
							$scope.normlTimeReq = 'Please select normal time.';
							$scope.normlTatTimeReq = 'Please enter normal time.';
							$scope.headerNoReq = "Please select header.";
							$scope.headerNoReq = "Please select header name.";
							$scope.parameterReq = "Please select parameter name."
							$scope.paraSequenceReq = "Please select parameter sequence."

						} ]);