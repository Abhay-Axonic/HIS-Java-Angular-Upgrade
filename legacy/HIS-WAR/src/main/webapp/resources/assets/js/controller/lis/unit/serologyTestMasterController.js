/**
 * @author Ganesh Chaudhari 
 */

angular
		.module('myApp')
		.controller(
				"serologyTestMasterController",
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
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							
							$scope.deltaNo = true;
							$scope.deltaText = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.searchKeyword=null;
							$scope.singleParamTestMasterListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							
							$scope.isHistoCyto="S";

							/* code for setting Label */
							$(".selectedPageName").text(
									"Serology Test Master");

							
							$scope.singleParamTestList = [];
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

							$scope.getDepartMentList = function(orgId,orgUnitId) 
							{
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_DEPARMENT_LIST
											+ S + orgId + S + orgUnitId;
									console.log("URI", URI);
									GenericService.serviceAction("GET", URI, data)
											  .then(function(response) {
														$scope.departmentList = response.data.listObject;
													});
								   } catch (e) {
									console.log(e.message)
								}
							}
							$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
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

							$scope.getSpecimanTypeList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_SPECIMAN_TYPE_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.specimenTypeList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getSpecimanTypeList($scope.orgId);
							
							$scope.getContainerList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_CONTAINER_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService.serviceAction("GET", URI, data)
											.then(function(response) {
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
											.then(function(response) {
														$scope.reportList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getReportList($scope.orgId);

							

							

							$scope.getTurnArroundTimeList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_TRUNAROUND_TIME_LIST
											+ S + orgId;
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

							

							

					/** =====================>Code for Push Pop Objects<===============*/
							
					
							
					$scope.showFootNote = function(testIndex)
					{
						$('#footNoteSingle').modal('show');
						$scope.footNote  = $scope.singleParamTestList[testIndex].footNote;
					}
					$scope.showPanicValue = function(testIndex)
					{
						
						$scope.paramRefRengeList = $scope.singleParamTestList[testIndex].listParameterMasterDto[0].listParamRefrengMaster;
						$('#panicValueSingle').modal('show');
						$scope.testIndex  = testIndex;
					}
					
					$scope.showHelpValue = function(testIndex)
					{
						$scope.helpValueList = $scope.singleParamTestList[testIndex].listParameterMasterDto[0].listHelpValueMaster;
						$('#helpValueSingle').modal('show');
						$scope.testIndex  = testIndex;
					}
							
					
					/** Service Autocomplete List * */
					$scope.getServiceList = function(searchKeyword) {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
									+ "/billing"
									+ AUTO_COMPLETE_SERVICES_LAB + S
									+ $scope.orgId + S
									+ $scope.orgUnitId +S
									+ $scope.deptId +S
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
						$scope.singleParamTestMaster.serviceId = $item.id;
						try {
							var data = {
								serviceId : $item.id,
								orgId : $scope.orgId,
								orgUnitId : $scope.orgUnitId,
								testType : 0,
								testId : $scope.singleParamTestMaster.testId
							};
							var URI = BASE_URL + ROOT_URL
									+ LIS_UNIT + CHECK_SERVICE_AVAIABLE+"/"+$scope.addUpdateFlag;
							console.log("URI", URI);
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.serviceCount = response.data.object;
												$scope.singleParamTestMaster.isOutsourced = response.data.object1.isOutsource;
												if (response.data.object > 0) {
													$scope.isServiceExist = true;
													growl.error("Service is already Exists.",
															{
																title : 'Error!'
															});
													$scope.singleParamTestList.testDesc = "";
												} else {
													$scope.isServiceExist = false;
												}
											});
						} catch (e) {
							console.log(e.message)
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
														console.log('$scope.testDetails',JSON.stringify($scope.testDetails));
														$rootScope.stopLoader();
														$('#histohistoOtherDetailsModal').modal('show');
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
					
						/**=================>OBJECT DELCLARATION <========================= **/	

					       $scope.listParameterMaster =[];

							$scope.testParamMpprMaster = {
								"testPerMpprId" : "",
								"testId" : "",
								"parameterId" : "",
								"paraSequence" : "",
								"headerId" : "1",
								"testParaStatus" : "A",
								"createdBy" : "",
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
								'refrangeTypeId':'',
								"aliesName" : "",
								"parameterCode" : "",
								"unitId" : "",
								"deltaDaysId" : "",
								"deltaPer" : "",
								"testType" : 0,
								"isMultyparameter" : "N",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"status" : "A",
								"listParamRefrengMaster" : [],
								"listHelpValueMaster" :[]
							}

							$scope.initSingleParamTest = function() 
							{
								$scope.addUpdateFlag = true;
								$scope.listParameterMaster =[];
								$scope.testParamList = [];
								$scope.listParameterMaster.push(angular.copy($scope.parameterMaster));
								$scope.listTestParamMpprMaster =[];
								$scope.singleParamTestMaster = {
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
									"testType" : 0,
									"isHistoCyto":"S",
									"specimanId":"",
									"isGynac":'N',
									"isFnac":'N',
									"isCentrifugationRequried":"A",
									"isUniqueBarcode":"N",
									"listParameterMasterDto" : $scope.listParameterMaster,
									"listTestParamMppr" : $scope.listTestParamMpprMaster
								}
							}
							$scope.initSingleParamTest();
							// ====================================================CODE START FOR SINGLE PARAM MASTER LIST===========================================================

							$scope.setNoOfRecords = function() {
								$scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.initSingleParamTestMasterList = function(orgId,orgUnitId, offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var searchKey = ($scope.searchKeyword != undefined && $scope.searchKeyword != null && $scope.searchKeyword != "" ) ? $scope.searchKeyword : null;
								var URI1 = BASE_URL + ROOT_URL + LIS_UNIT + LIST_TEST_MASTER + S + orgId + S + $scope.isHistoCyto + S +orgUnitId+S+ offset
										+ S + noOfRecordsPerPage  + S + searchKey;
								var URI2 = BASE_URL + ROOT_URL + LIS_UNIT + "/getTotalSerologyTestRecord" + S + orgId+ S + $scope.isHistoCyto + S +orgUnitId + S + searchKey;
								console.log("URI1", URI1);
								console.log("URI2", URI2);
								promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
								{
									$rootScope.stopLoader();
									$scope.singleParamTestList = response[0].listObject;
									$scope.singleParamTestMasterListCount = response[1].object;
									$scope.setPage(1, false);
								});
							}

							$scope.getSingleParamTestMasterList = function(orgId,orgUnitId, offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI = BASE_URL + ROOT_URL + LIS_UNIT + LIST_TEST_MASTER + S + orgId +  S + $scope.isHistoCyto + S +orgUnitId+S+ offset
								+ S + noOfRecordsPerPage + S + null;
								console.log("URI", URI);
								GenericService.serviceAction("GET", URI, data).then(function(response) 
								{
									$rootScope.stopLoader();
									$scope.singleParamTestList = response.data.listObject;
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {

							    if($scope.pager.totalPages==0){
                                    $scope.pager.totalPages=undefined;
                                }
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.singleParamTestMasterListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);

							// ====================================================CODE END FOR SINGLE PARAM LIST===========================================================
							
							
							
							/** CRUD FOR SINGLE PARAM TEST MASTER  **/
							//code for Activating Inactivating Sample
							$scope.activeInactiveSingleParamTest = function(index,testId,parameterId,testStatus,testType,serviceId)
							{
								try {
									$rootScope.startLoader();
									var URI = null;
									if(testType==0)
										{
										 URI = BASE_URL + ROOT_URL + LIS_UNIT+ACTIVE_INACTIVE_TEST+S+$scope.orgId+S+$scope.orgUnitId+S+testId+S+parameterId+S+testStatus+S+serviceId;
										}
									else if(testType==1)
									{
										 URI = BASE_URL + ROOT_URL+ LIS_UNIT+ ACTIVE_INACTIVEMULTIPARAM_TEST+ S + $scope.orgId + S + $scope.orgUnitId + S+ testId + S + testStatus + S + serviceId;
									}
									console.log("ACT_INACT_URI",URI);
									GenericService.serviceAction("GET", URI, $scope.singleParamTestMaster).then(function(response) 
									{
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success("Status Updated successfully",
													{
														title : 'Success!'
													});
											$scope.$broadcast('show-errors-reset');
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											if(response.data.object != null && response.data.object == 0){
												$scope.singleParamTestList[index].status = 'I';
											}
											
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							
							$scope.showParameters = function(testId) {
								try 
								{
									$scope.lstTestParameterMppr = [];
									$scope.listTestParamMpprMaster = [];
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

							
							$scope.getSingleParamTestById = function(testId)
							{
								try {
									$rootScope.startLoader();
									$scope.updateScope = true;
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									$scope.addUpdateFlag= false;
									var URI = BASE_URL + ROOT_URL + LIS_UNIT+GET_SINGLE_PARAMETER_TEST+S+$scope.orgId+S+$scope.orgUnitId+S+testId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET", URI, $scope.singleParamTestMaster).then(function(response) 
									{
										console.log("data",response.data);
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											if(response.data.object.testStatus == "I"){
												growl.warning("This entry is deactivated please active first then update.",
														{
															title : 'warning!'
														});
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												$scope.updateScope = false;
												return false;
											}else{
											$scope.singleParamTestMaster = response.data.object;
											$scope.singleParamTestMaster.deptId =  response.data.object.deptId+"";
											$scope.singleParamTestMaster.sampleId =  response.data.object.sampleId+"";
											$scope.listTestParamMpprMaster = $scope.singleParamTestMaster.listTestParamMppr;
											$scope.singleParamTestMaster.containerId = response.data.object.containerId+"";
											$scope.singleParamTestMaster.reportTypeId =response.data.object.reportTypeId+"";
											$scope.singleParamTestMaster.specimanId = response.data.object.specimanId+"";
											$scope.singleParamTestMaster.normlTatId = response.data.object.normlTatId+"";
											$scope.singleParamTestMaster.urgentTatId = response.data.object.urgentTatId+"";
											$scope.singleParamTestMaster.testType = response.data.object.testType;
											$scope.singleParamTestMaster.techniqueId =(response.data.object.techniqueId!=undefined || response.data.object.techniqueId !=null ) ? response.data.object.techniqueId:"";
											$scope.selectedVisitType=response.data.listObject;
											console.log("$scope.singleParamTestMaster",$scope.singleParamTestMaster);
											}
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error("Error In Fetching Data",{title : 'Error!'});
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							/*----------EquipmentsList Start------------*/

							
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
							
							$scope.saveSingleParamTest = function() 
							{
							    $scope.singleParamTestMaster.equipmentList =$scope.selectedVisitType;
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.histopathTestForm.$valid)
								{
									try 
									{
										$rootScope.startLoader();
										$scope.singleParamTestMaster.listParameterMasterDto = $scope.listParameterMaster;
										$scope.singleParamTestMaster.listParameterMasterDto[0].isMultyparameter ='N';
										$scope.singleParamTestMaster.listParameterMasterDto[0].status ='A';
										$scope.singleParamTestMaster.listParameterMasterDto[0].parameterName = $scope.singleParamTestMaster.testDesc;
										$scope.singleParamTestMaster.listParameterMasterDto[0].aliesName = $scope.singleParamTestMaster.testAlies;
										$scope.singleParamTestMaster.listParameterMasterDto[0].parameterCode = $scope.singleParamTestMaster.testCode;
										$scope.singleParamTestMaster.unitId = $scope.singleParamTestMaster.listParameterMasterDto[0].unitId;
										$scope.singleParamTestMaster.listTestParamMppr = $scope.listTestParamMpprMaster;
										$scope.singleParamTestMaster.listTestParamMppr.sort((a, b) => parseInt(a.paraSequence) - parseInt(b.paraSequence));
										console.log("singleParamTestMaster",JSON.stringify($scope.singleParamTestMaster));
										var URI = BASE_URL + ROOT_URL + LIS_UNIT+ADD_SINGLE_PARAMETER_TEST;
										GenericService.serviceAction("POST", URI, $scope.singleParamTestMaster).then(function(response) 
												{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.message,{title : 'Success!'});
												$scope.$broadcast('show-errors-reset');
												$scope.initSingleParamTest();
												$scope.selectedVisitType =[];
												$scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
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
										console.log("Exception",e.message);
									}
									
								}
							}
							
							//-----///
							$scope.showFieldList = function(testId){
								console.log($scope.orgUnitId)
								try{
									$scope.fieldMasterList=[];
									$scope.saveBtnFlag = false;
									$scope.updateScope = true;
									$scope.updateBtnFlag = true;
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
							$scope.updateSingleParamTest = function() 
							{
								$scope.singleParamTestMaster.equipmentList =$scope.selectedVisitType;
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.histopathTestForm.$valid)
								{
									try {
										
										if($scope.singleParamTestMaster.testType == 0 && $scope.listTestParamMpprMaster.length>0){
											growl.warning("Please remove Multiparameter list",
													{
														title : 'Warning!'
													});
											$scope.singleParamTestMaster.testType = 1;
										}else{
										$rootScope.startLoader();
										$scope.singleParamTestMaster.listParameterMasterDto[0].isMultyparameter ='N';
										$scope.singleParamTestMaster.listParameterMasterDto[0].status ='A';
										$scope.singleParamTestMaster.unitId = $scope.singleParamTestMaster.listParameterMasterDto[0].unitId;
										$scope.singleParamTestMaster.listTestParamMppr = $scope.listTestParamMpprMaster.concat($scope.testParamList);
										$scope.singleParamTestMaster.listTestParamMppr.sort((a, b) => parseInt(a.paraSequence) - parseInt(b.paraSequence));
										console.log("singleParamTestMaster",JSON.stringify($scope.singleParamTestMaster));
										var URI = BASE_URL + ROOT_URL + LIS_UNIT+UPDATE_SINGLEPARAMETER_TEST;
										GenericService.serviceAction("PUT", URI, $scope.singleParamTestMaster).then(function(response) 
										{
											if (response.data.status == 'success')
											{
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												$scope.updateScope = false;
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												$scope.initSingleParamTest();
												$scope.selectedVisitType =[];
												$scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
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
									} catch (e) 
									{
										console.log("Exception",e.message);
									}
									
								}
							}
							 $scope.checkDelta=function(value)
								{
								    if(value==1){
										$scope.deltaNo = true;
							            $scope.deltaText = false;
								    }else{

										$scope.deltaNo = false;
							            $scope.deltaText = true;
								    }
								}

								$scope.searchTestMaster = function(searchKeyword){

                                    $scope.loadingMsg="Loading...";
                                    if(searchKeyword.length>2 ){

                                        $scope.singleParamTestList=[];
                                        $rootScope.startLoader();
                                        var URI1 = BASE_URL + ROOT_URL + LIS_UNIT + LIST_TEST_MASTER
                                         + S + $scope.orgId
                                         + S + $scope.isHistoCyto
                                         + S + $scope.orgUnitId
                                         + S + $scope.offset
                                         + S + $scope.noOfRecordsPerPage
                                         + S + searchKeyword;

                                         var URI2 = BASE_URL + ROOT_URL + LIS_UNIT + "/getTotalSerologyTestRecord"
                                         + S + $scope.orgId
                                         + S + $scope.isHistoCyto
                                         + S + $scope.orgUnitId
                                         + S + searchKeyword;

                                        console.log("URI", URI1);
                                        console.log("URI", URI2);

                                        promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response)
                                        {
                                            $rootScope.stopLoader();
                                            $scope.singleParamTestList = response[0].listObject;
                                            $scope.singleParamTestMasterListCount = response[1].object;
                                            $scope.setPage(1, false);
                                        });
                                    }
                                    if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                                        $scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
                                    }
                                }
							 
	/******************************* Get Header List START ********************************************/
							
							 /*$scope.getHeaderList = function(orgId, orgUnitId) {
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
															console.log("$scope.headerList",$scope.headerList);
														});
									} catch (e) {
										console.log(e.message)
									}
								}
								$scope.getHeaderList($scope.orgId,$scope.orgUnitId);	
								
								$scope.getParameterList = function(orgId, orgUnitId) {
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
															console.log("$scope.parameterList",$scope.parameterList);
														});
									} catch (e) {
										console.log(e.message)
									}
								}
								$scope.getParameterList($scope.orgId,$scope.orgUnitId);*/
								
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
								/** =====================>Code for Push Pop Objects<=============== */

							 	$scope.headerObj = {id: "",name:""};
								$scope.parameterObj = {id: "",name:""};
								
								$scope.selectSearchedHeader = function($item, $model,$label){
									if( $item.id!=null &&  $item.id!=undefined){
										$scope.headerObj.id=  $item.id;
										$scope.headerObj.name = $item.name;
										$scope.obj.headerId= $item.id;
									}else{
										$scope.obj.headerDesc = "";
									}
								}
								
								$scope.selectSearchedParameter = function($item, $model,$label) {
									if( $item.id!=null &&  $item.id!=undefined){
										$scope.parameterObj.id =  $item.id ;
										$scope.parameterObj.name = $item.name;
										$scope.obj.parameterId = $item.id;
									}else{
										$scope.obj.parameterDesc = "";
									}
								}
								
								/*$scope.selectHeader = function(headerId) {
									$scope.headerObj = $scope.headerList
											.filter(function(headerMaster) {
												if (headerMaster.id == headerId) {
													return headerMaster;
												}
											});
								}

								$scope.selectParameter = function(parameterId) {
									$scope.parameterObj = $scope.parameterList
											.filter(function(parameterMaster) {
												if (parameterMaster.id == parameterId) {
													return parameterMaster;
												}
											});
								}*/

								$scope.testParamList = [];
							//	$scope.paraSequnce=0;
							//	$scope.headerId=0;
							//	$scope.parameterId=0;
								$scope.obj = {
										paraSequnce:'',
										headerId:'',
										parameterId:'',
										parameterDesc : "",
										headerDesc : ""
								};
								$scope.addParameterMapping = function() {
									
									if(isNaN($scope.obj.paraSequnce)||Number($scope.obj.paraSequnce)==0)
										{
										growl.error("Please Enter Valid Parameter Sequence",
												{
													title : 'Error!'
												});
										  return false;
										}
									if($scope.obj.headerId==null||$scope.obj.headerId==undefined)
									{
									growl.error("Please Select Header.",
											{
												title : 'Error!'
											});
									  return false;
									}
									
									if($scope.obj.parameterId==null||$scope.obj.parameterId==undefined)
									{
									growl.error("Please Select Parameter.",
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
										testParamMpprMaster.paraSequence = $scope.obj.paraSequnce;
										isExists = checkExistsOrNot(
												$scope.listTestParamMpprMaster,
												testParamMpprMaster);
										if (!isExists) {
											$scope.listTestParamMpprMaster.push(testParamMpprMaster);
											$scope.obj = {};
										} else {
											growl.error("Parameter is already Added To.",
													{
														title : 'Error!'
													});
										}

									} else if ($scope.updateScope == true) {
										testParamMpprMaster = angular
												.copy($scope.testParamMpprMaster);
										if($scope.singleParamTestMaster.listTestParamMppr.length>0){
										testParamMpprMaster.testId = $scope.singleParamTestMaster.listTestParamMppr[0].testId;
										}else{
											testParamMpprMaster.testId = $scope.singleParamTestMaster.testId;
										}
										testParamMpprMaster.headerId = $scope.headerObj.id;
										testParamMpprMaster.headerDesc = $scope.headerObj.name;
										testParamMpprMaster.parameterId = $scope.parameterObj.id;
										testParamMpprMaster.parameterDesc = $scope.parameterObj.name;
										testParamMpprMaster.paraSequence = $scope.obj.paraSequnce;
										isExists = checkExistsOrNot(
												$scope.listTestParamMpprMaster,testParamMpprMaster);
										if (!isExists) {
											$scope.listTestParamMpprMaster.push(testParamMpprMaster);
											$scope.obj={};
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
												.copy($scope.singleParamTestMaster.listTestParamMppr[deleteIndex]);
										testParamMpprMaster.parameterId = $scope.singleParamTestMaster.listTestParamMppr[deleteIndex].parameterId;
										testParamMpprMaster.testId = $scope.singleParamTestMaster.listTestParamMppr[0].testId;
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
								
								$scope.getTechniqueList = function(orgId) {
									try {
										var data = "";
										var URI = BASE_URL + ROOT_URL
												+ LIS_COMMON + GET_TECHNIQUE_LIST
												+ S + orgId;
										GenericService
												.serviceAction("GET", URI, data)
												.then(function(response) {
													$scope.techniqueList = response.data.listObject;
														});
									} catch (e) {
										console.log(e.message)
									}
								}
								$scope.getTechniqueList($scope.orgId);
								

	/******************************* Get Header List END ********************************************/	
								
								
								
	/**=============================>Errors Messages  <=======================================**/
		$scope.speciamnReq = 'Speciman Type is required.';
		$scope.testNameReq = 'Test name is required.';
		$scope.testCodeReq = 'Test code is required.';
		$scope.testAliesReq = 'Alies name is required.';
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
		
		$scope.minValReq = 'Required Min Val.';
		$scope.maxValReq = 'Required Max Val.';
		$scope.lessValReq = 'Required Less Val.';
		$scope.moreValReq = 'Required More Val.';
		$scope.helpValueReq = 'Required Help Val.';
		$scope.remarkValReq = 'Required Remark Val.';
		
		$scope.dayReq = 'Please Select Day.';
		$scope.deltaPer = 'Required delta no in %.';
		
		$scope.plzSelectUnit = 'Please Select Unit.';
		$scope.plzSelectAgeGroup = 'Please Select AgeGroup.';
		$scope.plzSelectSex='Please Select Sex.';
		
		$scope.headerNoReq = "Please select header.";
		$scope.headerNoReq = "Please select header name.";
		$scope.parameterReq = "Please select parameter name."
		$scope.paraSequenceReq = "Please select parameter sequence."
						} ]);