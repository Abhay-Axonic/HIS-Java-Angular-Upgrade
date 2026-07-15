/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"cytologyTestController",
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
							$scope.searchKeyword;
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
							
							$scope.isHistoCyto="C";

							/* code for setting Label */
							$(".selectedPageName").text(
									"Cytology Master");

							
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
								"searchKeyword" : "",
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
									"isHistoCyto":"C",
									"specimanId":"",
									"isGynac":'N',
									"searchKeyword" : "",
									"isFnac":'N',
									"isCentrifugationRequried":"A",
									"listParameterMasterDto" : $scope.listParameterMaster,
									"listTestParamMppr" : $scope.listTestParamMpprMaster
								}
							}
							$scope.initSingleParamTest();
							// ====================================================CODE START FOR SINGLE PARAM MASTER LIST===========================================================

							$scope.setNoOfRecords = function() {
								$scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.initSingleParamTestMasterList = function(orgId,orgUnitId, offset, noOfRecordsPerPage, searchKeyword)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var searchKey = (searchKeyword != undefined && searchKeyword != null && searchKeyword != "" ) ? searchKeyword : null;
								var URI1 = BASE_URL + ROOT_URL + LIS_UNIT + LIST_TEST_MASTER + S + orgId + S + $scope.isHistoCyto + S +orgUnitId+S+ offset
								+ S + noOfRecordsPerPage + S + searchKey;
								var URI2 = BASE_URL + ROOT_URL + LIS_UNIT + GET_TOTAL_TEST_RECORD + S + orgId+ S + $scope.isHistoCyto + S +orgUnitId;
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

							$scope.getSingleParamTestMasterList = function(orgId,orgUnitId, offset, noOfRecordsPerPage, searchKeyword)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var searchKey = (searchKeyword != undefined && searchKeyword != null && searchKeyword != "" ) ? searchKeyword : null;
								var URI = BASE_URL + ROOT_URL + LIS_UNIT + LIST_TEST_MASTER + S + orgId +  S + $scope.isHistoCyto + S +orgUnitId+S+ offset
								+ S + noOfRecordsPerPage + S + searchKey;
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
							$scope.activeInactiveSingleParamTest = function(list,testId,parameterId,testStatus,serviceId)
							{
								if(parameterId==undefined || parameterId==null || parameterId=="" ){
								parameterId=list.parameterId;
								 
							}
								try {
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + LIS_UNIT+ACTIVE_INACTIVE_TEST+S+$scope.orgId+S+$scope.orgUnitId+S+testId+S+parameterId+S+testStatus+S+serviceId;
									console.log("ACT_INACT_URI",URI);
									GenericService.serviceAction("GET", URI, $scope.singleParamTestMaster).then(function(response) 
									{
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success("Status Updated Successfully",
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
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
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
											$scope.singleParamTestMaster = response.data.object;
											$scope.singleParamTestMaster.deptId =  response.data.object.deptId+"";
											$scope.singleParamTestMaster.sampleId =  response.data.object.sampleId+"";
											$scope.singleParamTestMaster.containerId = response.data.object.containerId+"";
											$scope.singleParamTestMaster.reportTypeId =response.data.object.reportTypeId+"";
											$scope.singleParamTestMaster.specimanId = response.data.object.specimanId+"";
											$scope.singleParamTestMaster.normlTatId = response.data.object.normlTatId+"";
											$scope.singleParamTestMaster.urgentTatId = response.data.object.urgentTatId+"";
											$scope.selectedVisitType=response.data.listObject;
											console.log("$scope.singleParamTestMaster",$scope.singleParamTestMaster);
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
					      $scope.saveParamTSingle=true;
					      $scope.saveSingleParamTest = function() 
					      {
					          $scope.singleParamTestMaster.equipmentList =$scope.selectedVisitType;
					          $scope.$broadcast('show-errors-check-validity');


					                $rootScope.startLoader();
					                $scope.singleParamTestMaster.listParameterMasterDto = $scope.listParameterMaster;
					                $scope.singleParamTestMaster.listParameterMasterDto[0].isMultyparameter ='N';
					                $scope.singleParamTestMaster.listParameterMasterDto[0].status ='A';
					                $scope.singleParamTestMaster.listParameterMasterDto[0].parameterName = $scope.singleParamTestMaster.testDesc;
					                $scope.singleParamTestMaster.listParameterMasterDto[0].aliesName = $scope.singleParamTestMaster.testAlies;
					                $scope.singleParamTestMaster.listParameterMasterDto[0].parameterCode = $scope.singleParamTestMaster.testCode;
					                $scope.singleParamTestMaster.unitId = $scope.singleParamTestMaster.listParameterMasterDto[0].unitId;
					                console.log("singleParamTestMaster",JSON.stringify($scope.singleParamTestMaster));
					                var URI = BASE_URL + ROOT_URL + LIS_UNIT+ADD_SINGLE_PARAMETER_TEST;
					                GenericService.serviceAction("POST", URI, $scope.singleParamTestMaster).then(function(response) 
					                {  
					                   if (response.data.status == 'success')
					                   {
					                	  $scope.saveParamTSingle=false;
					                      $rootScope.stopLoader();
					                      growl.success(response.data.message,{title : 'Success!'});
					                      $scope.$broadcast('show-errors-reset');
					                      $scope.initSingleParamTest();
					                      $scope.selectedVisitType =[];
					                      $scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
					                   } else if(response.data.status == 'error')
					                   {
					                	   $scope.saveParamTSingle=false;
					                      $rootScope.stopLoader();
					                      growl.error(response.data.message,
					                            {
					                               title : 'Error!'
					                            });
					                   }
					                });

					             

					      }							//-----///
							$scope.showFieldList = function(testId){
								console.log($scope.orgUnitId)
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
							
							$scope.updateSingleParamTest = function() 
							{
								 $scope.singleParamTestMaster.equipmentList =$scope.selectedVisitType;
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.histopathTestForm.$valid)
								{
									try {
										$rootScope.startLoader();
										$scope.singleParamTestMaster.listParameterMasterDto[0].isMultyparameter ='N';
										$scope.singleParamTestMaster.listParameterMasterDto[0].status ='A';
										console.log("singleParamTestMaster",JSON.stringify($scope.singleParamTestMaster));
										var URI = BASE_URL + ROOT_URL + LIS_UNIT+UPDATE_SINGLEPARAMETER_TEST;
										GenericService.serviceAction("PUT", URI, $scope.singleParamTestMaster).then(function(response) 
										{
											if (response.data.status == 'success')
											{
												
												$scope.updateScope = false;
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												$scope.addUpdateFlag= true;
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
							 
					$scope.serachCytologyTestmaster = function(testDesc){
						
						$scope.singleParamTestList=[];
						$scope.loadingMsg="Loading...";
						if(testDesc!=''){
						var URI = BASE_URL + ROOT_URL +"/LIS/unit/cytologyTestMasterUniversalSearch"
						+ S + $scope.organizationId
						+ S + $scope.unitId
						+ S + $scope.offset
						+ S + $scope.noOfRecordsPerPage 
						+ S + testDesc 
						+ S + $scope.isHistoCyto ;
						console.log("URI", URI);
						GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) 
								{
											$rootScope.stopLoader();
											$scope.singleParamTestList = response.data.listObject;
											if($scope.singleParamTestList.length==0)
												$scope.loadingMsg="No record found...";
										});
						}else{
							$scope.initSingleParamTestMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
						}
					}
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
		$scope.footNoteReq = 'Please enter fooot note.';
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
		
		$scope.isGynacReq = 'Please Select Gynac.';
		$scope.isFnacReq = 'Please FNAC';
		
						} ]);