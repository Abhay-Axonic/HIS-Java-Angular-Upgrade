/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"sensitivityTestingController",
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
						    $scope.pathologiestId = null;
						    $scope.getUserListByRoleType($scope.orgUnitId,$scope.orgId);
						    $scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
						    $scope.organismGroupId = "";
						    $scope.organismId = "";
						    
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
						    $scope.triggerReleaseMail = labConfig.triggerReleaseMail;
						    
						     /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
				        	/*First Level*/
					    	$scope.firstTabLevel = 1 ;
							
							$scope.setFirstTabLevel= function(newTab)
							{
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
							
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
							
							
							$scope.init = function(){
								
								$scope.listSensitivityTestResultMasterDto = [];
								$scope.listSensitivityTestResultDetailsMasterDto = [];
								$scope.listSensitivityTestResultSubDetailsMasterDto = [];
								$scope.labSampleDtlsId =0;
								$scope.removeSesitivityResultSubDetailsId;
								$scope.SensitivityTestResultMasterDto = 
								{
										  "sensitivityResultId" : "",
										  "orgId" : $scope.orgId ,
										  "orgUnitId" : $scope.orgUnitId,
										  "createdBy" : $scope.userId,
										  "createdDate" : "",
										  "updatedBy" : "",
										  "updatedDate" : "",
										  "isDeleted" : 'N',
										  "checkTest" : "Y",
										  "labSampleDtlsId" : "",
										  "listSensitivityTestResultDetailsMaster" : $scope.listSensitivityTestResultDetailsMasterDto
								}
								
								$scope.SensitivityTestResultDetailsMasterDto = 
								{
										  "sesitivityResultDetailsId" : "",
										  "sensitivityResultId":"",
										  "orgId" : $scope.orgId ,
										  "orgUnitId" : $scope.orgUnitId,
										  "organismGroupId":"",
										  "organismGroupName":"",
										  "organismId" : "",
										  "organismName":"",
										  "selectedAll":"",
										  "createdBy" : $scope.userId,
										  "createdDate" : "",
										  "updatedBy" : "",
										  "updatedDate" : "",
										  "isDeleted" : "N",
										  "listSensitivityTestResultSubDetailsMaster" : $scope.listSensitivityTestResultSubDetailsMasterDto
								}
								
								$scope.flagCheck=true;
								$scope.SensitivityTestResultSubDetailsMasterDto = 
									{
										  "sesitivityResultSubDetailsId" : "",
										  "orgId" : $scope.orgId,
										  "orgUnitId" : $scope.orgUnitId,
										  "antibioticId" : "",
										  "antibioticName" : "",
										  "isChecked":"",
										  "sensitivityId":"",
										  "microLabPriorityId" : "",
										  "mic":"",
										  "remark":"",
										  "createdDate":"",
										  "createdBy" : $scope.userId,
										  "updatedBy" : "",
										  "updatedDate" : "",
										  "isDeleted" : "N",
										  "sesitivityResultDetailsId":""
									}
								

							}
							$scope.init();
							

							$scope.setNoOfRecords = function() {
								$scope.initIncucationObjservationList($scope.orgId,
										$scope.orgUnitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initIncucationObjservationList = function(orgId,
									orgUnitId, offset, noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									
									if($scope.selectedSampleNo == null || $scope.selectedSampleNo == ""){
										$scope.labSampleId = null;
										$scope.labSampleDtlsId=0;
									}
									
									var searchObj = {
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format('YYYY-MM-DD'):moment($scope.fromDate).format('YYYY-MM-DD'),
					                          "toDt":  $scope.toDate==undefined?moment(new Date()).format('YYYY-MM-DD'):moment($scope.toDate).format('YYYY-MM-DD'),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "labSampleId" : $scope.labSampleId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDeptId" : $scope.subDeptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "assignUnit" :$scope.orgUnitId ,
											  "recordPerPage":$scope.noOfRecordsPerPage,
											  "offset":  $scope.offset
	                             }
								
								console.log("collectedSampleList",JSON.stringify(searchObj));
								var URI = BASE_URL 
								        + ROOT_URL
								    	+ LIS_MICROBIOLOGY
										+ SENSITIVITY_TESTING_LIST_SEARCH
								console.log("URI", URI);
								 GenericService.serviceAction("POST", URI,
										 searchObj).then(
												 function(response) {
													 $scope.sampleAccPendingList=[];
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															$scope.incubationObservationList =  response.data.listObject;
															$scope.commonListCount = response.data.object> 0 ? response.data.object : 0 ;
															$scope.incubationObservationListForFiltter = $scope.incubationObservationList;
															$scope.filterByDummyServices();
															$scope.setPage(1,false);
														} 
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
											"fromDate": $scope.fromDate==undefined?moment(new Date()).format('YYYY-MM-DD'):moment($scope.fromDate).format('YYYY-MM-DD'),
							                "toDate":  $scope.toDate==undefined?moment(new Date()).format('YYYY-MM-DD'):moment($scope.toDate).format('YYYY-MM-DD'),
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage
										};
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ SENSITIVITY_TESTING_LIST;
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
							
							
							$scope.getAntibioticAutocompleteList = function(searchKeyword) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
					            	+ LIS_COMMON
					               	+ GET_ANTIBIOTICS_AUTOCOMPLETE+S+$scope.orgId+S+'antibiotic'+S+searchKeyword;
								 	console.log("URI", URI);
								 	return GenericService.serviceAction("GET", URI,
											data).then(function(response) {
										return response.data.listObject;
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.selectAnitbiotic = function($item, $model,
									$label) 
									{
								   $scope.antibioticId = $item.id;
								   $scope.antibioticName = $item.name;
								
								}
							
							$scope.addAntibiotic = function(index)
							{
								var isExists;
								function checkExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												return (arr.antibioticId == val);
											});
								}
								
								isExists = checkExistsOrNot(
										$scope.listSensitivityTestResultDetailsMasterDto[index].listSensitivityTestResultSubDetailsMaster,$scope.antibioticId);	
							
								if(isExists)
									{
									
									growl.warning("Antibiotics is already exists.",
											{
												title : 'warning!'
											});
									// alert("Antibiotics is already exists.");
									 $scope.antibiotics = undefined;
									}
								else
								{
									if( $scope.antibioticId == undefined ||  $scope.antibioticId == null ||  $scope.antibioticId== ""){
										growl.warning("Please Select Antibiotic",
											{
												title : 'warning!'
											});
											return false;
									}
									var SensitivityTestResultSubDetailsMasterDto = angular.copy($scope.SensitivityTestResultSubDetailsMasterDto);
								     SensitivityTestResultSubDetailsMasterDto.antibioticId = $scope.antibioticId;
								     SensitivityTestResultSubDetailsMasterDto.antibioticName = $scope.antibioticName;
								     $scope.listSensitivityTestResultDetailsMasterDto[index].listSensitivityTestResultSubDetailsMaster
											.push(SensitivityTestResultSubDetailsMasterDto);
								}
								$scope.antibiotics = undefined;
								this.antibiotics = undefined;
							}
							
							$scope.removeAntibiotics = function(parentIndex,childIndex)
							{
								$scope.listSensitivityTestResultDetailsMasterDto[parentIndex].listSensitivityTestResultSubDetailsMaster.splice(childIndex, 1);
							}
							
						
							
						
							
							$scope.checkAll = function(parentIndex)
							{
								if ($scope.listSensitivityTestResultDetailsMasterDto[parentIndex].selectedAll) 
								{
									$scope.listSensitivityTestResultDetailsMasterDto[parentIndex].selectedAll = true;
								} else {
									$scope.listSensitivityTestResultDetailsMasterDto[parentIndex].selectedAll = false;
								}
								angular
										.forEach(
												$scope.listSensitivityTestResultDetailsMasterDto[parentIndex].listSensitivityTestResultSubDetailsMaster,
												function(antibiocsObj) {
													antibiocsObj.isChecked = $scope.listSensitivityTestResultDetailsMasterDto[parentIndex].selectedAll;
													console.log("antibiocsObj.isChecked",antibiocsObj.isChecked)
												});
							
							}
							/** Micro_Lab priority List*/
							$scope.getMicroLabPriorityMaster = function()
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ GET_MICRO_LAB_PRIORITY_LIST+S+$scope.orgId;
									console.log("URI", URI);

									 GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) 
													{
														$scope.microLabPriorityList = response.data.listObject;
														console.log("$scope.microLabPriorityList",$scope.anitibioticList);
														 response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.getMicroLabPriorityMaster();
							
							/** Micro_organism Group List*/
							$scope.getAntibioticsByOrganism = function(orgId,organismId)
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ GET_ANTIBIOTICS_BY_ORGAINSM+S+orgId+S+'organismId'+S+organismId;
									console.log("URI", URI);

									return GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) 
													{
														$scope.anitibioticList = response.data.listObject;
														console.log("$scope.anitibioticList",$scope.anitibioticList);
														return response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
							/** Micro_organism Group List*/
							$scope.getMicroOrganismGroupList = function(orgId)
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ MICRO_ORGANISM_GROUP_LIST_BY_ORG_ID+S+$scope.orgId;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) {
														$scope.microOrganismGroupList = response.data.listObject;
														console.log("$scope.microOrganismGroupList",$scope.microOrganismGroupList);
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getMicroOrganismGroupList($scope.orgId);
							
							
							$scope.getMicroOrganismList = function(organismGroupId)
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ MICRO_GET_ORGANISM_BY_GROUP+S+organismGroupId+S+'orgId'+S+$scope.orgId;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) 
													{
														$scope.organismList = response.data.listObject;
														console.log("$scope.organismList",$scope.organismList);
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
					      
					    
					      
					      /**Function to get MicroOrganism Group idD*/
					      $scope.getMicroOrganismGroupId = function(organismGroupId)
					      {
					    	  $scope.organismGroupId = organismGroupId;
					      };
					      
				
					     
					      
					     $scope.addMicroOrganismGroup = function()
					      {
					    	 
					    	 var isExists;
					    	 var data = {
										"organismGroupId":"",
										"organismId":""
									};
					    	 
					    	 
					    	 data.organismGroupId = $scope.organismGroupId;
					    	 data.organismId = $scope.organismId;
					    	 
								function checkExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												return (val.organismGroupId == arr.organismGroupId)
														&& (val.organismId == arr.organismId);
											});
								}
					    	
							
								isExists = checkExistsOrNot(
										$scope.listSensitivityTestResultDetailsMasterDto,
										data);
								
								if(isExists)
									{
										growl.warning("Organism Group And Organism Already Exists",
											{
												title : 'warning!'
											});
									
								       // alert("Organism Group And Organism Already Exists");	
									}
								else{
									
									 var SensitivityTestResultDetailsMasterDto = angular.copy($scope.SensitivityTestResultDetailsMasterDto);
							    	 $scope.miroOrgGroupList = $scope.microOrganismGroupList
										.filter(function(microOrgGroupObj) {
											if (microOrgGroupObj.id ==  $scope.organismGroupId) 
											{
												SensitivityTestResultDetailsMasterDto.organismGroupName = microOrgGroupObj.name;
												SensitivityTestResultDetailsMasterDto.organismGroupId = microOrgGroupObj.id;
											}
										});
							    	 $scope.orgList = $scope.organismList
										.filter(function(microOrgObj) {
											if (microOrgObj.id ==  $scope.organismId) 
											{
												SensitivityTestResultDetailsMasterDto.organismName = microOrgObj.name;
												SensitivityTestResultDetailsMasterDto.organismId = microOrgObj.id;
											}
										});
							    	 
							    	 var promise = $scope.getAntibioticsByOrganism($scope.orgId,$scope.organismId);
							    	 promise.then(function(commonList)
												{
													if (commonList != null)
														{
														$scope.listSensitivityTestResultSubDetailsMasterDto =[];
														angular
														.forEach(
																commonList,
																function(value, key) 
																{
																	 var SensitivityTestResultSubDetailsMasterDto = angular
																				.copy($scope.SensitivityTestResultSubDetailsMasterDto);
																	 SensitivityTestResultSubDetailsMasterDto.antibioticId = value.id;
																	 SensitivityTestResultSubDetailsMasterDto.antibioticName =value.name;
																	 $scope.listSensitivityTestResultSubDetailsMasterDto.push(SensitivityTestResultSubDetailsMasterDto);
																});
															
														}
													
													 SensitivityTestResultDetailsMasterDto.listSensitivityTestResultSubDetailsMaster = $scope.listSensitivityTestResultSubDetailsMasterDto;
													 $scope.listSensitivityTestResultDetailsMasterDto.push(SensitivityTestResultDetailsMasterDto);
												}, function(commonList)
												{
													console.log("ERROR FETCHING SUPER Antibiotics", commonList);
												});
							    	 
							    	 
							    	 console.log("$scope.listSensitivityTestResultDetailsMasterDto",$scope.listSensitivityTestResultDetailsMasterDto);

									
								    }
								 $scope.organismId = "";
						    	 $scope.organismGroupId = "";
					    	 
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
									$scope.getIncucationObjservationList($scope.orgId, $scope.orgUnitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								//	$scope.IncucationObjservationList(cope.orgId, $scope.orgUnitId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}


							$scope.getSensitivityCheckTestResultDetails = function(labSampleDtlsId) {
								try {
									
									$rootScope.startLoader();
									 $scope.listSensitivityTestResultDetailsMasterDto = [];
									 $scope.labSampleDtlsId = labSampleDtlsId;
									var data = {};
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ GET_SENSITIVITY_CHECK_TEST_DETAILS+S+labSampleDtlsId+S+$scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response)
													{
														if(response.data.status=='success')
															{
															  $rootScope.stopLoader();
															  
															  if(response.data.object==null){
															  	 	$scope.flagCheck = false;
															  	 	
															  	 	$scope.SensitivityTestResultMasterDto = 
																	{
																			  "sensitivityResultId" : "",
																			  "orgId" : $scope.orgId ,
																			  "orgUnitId" : $scope.orgUnitId,
																			  "createdBy" : $scope.userId,
																			  "createdDate" : "",
																			  "updatedBy" : "",
																			  "updatedDate" : "",
																			  "isDeleted" : 'N',
																			  "checkTest" : "Y",
																			  "labSampleDtlsId" : "",
																			  "orderDetailsId":"",
																			  "listSensitivityTestResultDetailsMaster" : $scope.listSensitivityTestResultDetailsMasterDto
																	}
																	
																	$scope.SensitivityTestResultDetailsMasterDto = 
																	{
																			  "sesitivityResultDetailsId" : "",
																			  "sensitivityResultId":"",
																			  "orgId" : $scope.orgId ,
																			  "orgUnitId" : $scope.orgUnitId,
																			  "organismGroupId":"",
																			  "organismGroupName":"",
																			  "organismId" : "",
																			  "organismName":"",
																			  "selectedAll":"",
																			  "createdBy" : $scope.userId,
																			  "createdDate" : "",
																			  "updatedBy" : "",
																			  "updatedDate" : "",
																			  "isDeleted" : "N",
																			  "listSensitivityTestResultSubDetailsMaster" : $scope.listSensitivityTestResultSubDetailsMasterDto
																	}
															  }else{
																  $scope.flagCheck = true;
															  $scope.SensitivityTestResultMasterDto = response.data.object;
															  $scope.listSensitivityTestResultDetailsMasterDto = response.data.object.listSensitivityTestResultDetailsMaster;
															  angular.forEach($scope.listSensitivityTestResultDetailsMasterDto, function(value, key) {
                                                                   value.isChecked = true
                                                              });
                                                              $scope.getAntibioticListOnload();
															  
															  }
															}
														else if (response.data.status=='error')
															{
															  $rootScope.stopLoader();
															}
													
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							
							
							$scope.getSenisitivityDetails = function(sensitivityObj)
							{
								$scope.listSensitivityTestResultSubDetailsMaster = [];
								//$scope.incubationObservationObjList=[];
								 $('#details').modal('show');
								 $scope.SensitivityTestResultMasterDto.labSampleDtlsId = sensitivityObj.labSampleDtlsId;
								 $scope.orderDetailsId = sensitivityObj.orderDetailsId;
								 $scope.labSampleDtlsId = sensitivityObj.labSampleDtlsId;
								 $scope.incubationObservationObj = $scope.incubationObservationList
									.filter(function(incuObservationObj) {
										if (incuObservationObj.labSampleDtlsId == sensitivityObj.labSampleDtlsId) {
											$scope.incubationObservationObjList = incuObservationObj;
											return incuObservationObj;
										}
									});
								 
								 if(sensitivityObj.labSampleDtlsId!=null)
								 {
								   $scope.getSensitivityCheckTestResultDetails(sensitivityObj.labSampleDtlsId);
								 }
							}
							$scope.patientDetailsDto = {};
							$scope.testObj = {};
							$scope.getSensitivityCheckTestDetails = function(sensitivityObj)
							{
								$scope.testObj = {};
								$scope.testObj = sensitivityObj;
								$scope.listSensitivityTestResultDetailsMasterDto  = [];
								//$scope.incubationObservationObjList=[];
								 $('#sensitivityTesting').modal('show');
								 $scope.patientDetailsDto = {};
								 $scope.SensitivityTestResultMasterDto.labSampleDtlsId = sensitivityObj.labSampleDtlsId;
								 $scope.patientDetailsDto.testDesc = sensitivityObj.testDesc;
								 $scope.patientDetailsDto.patientId = sensitivityObj.patientId;
								 $scope.patientDetailsDto.doctorId = sensitivityObj.doctorId;
								 $scope.orderDetailsId = sensitivityObj.orderDetailsId;
								 $scope.labSampleDtlsId = sensitivityObj.labSampleDtlsId;
								 $scope.incubationObservationObj = $scope.incubationObservationList
									.filter(function(incuObservationObj) {
										if (incuObservationObj.labSampleDtlsId == sensitivityObj.labSampleDtlsId) {
											$scope.incubationObservationObjList = incuObservationObj;
											return incuObservationObj;
										}
									});
								 
								 if(sensitivityObj.labSampleDtlsId!=null)
									 {
									   $scope.getSensitivityCheckTestResultDetails(sensitivityObj.labSampleDtlsId);
									   $scope.getBiochemResultEntryDetails();
									 }
							}
							
							
							$scope.showHelpValue = function(testIndex,headerIndex,resultIndex,testType,parameterId)
							{
								
								try 
								{
									$rootScope.startLoader();
									$scope.tIdx = testIndex;
									$scope.hIdx = headerIndex;
									$scope.rIdx = resultIndex;
									$scope.tType = testType;
									$scope.listHelpValueMaster = [];
									var URI = BASE_URL + ROOT_URL
									+ LIS_UNIT
									+ GET_HELP_VALUES +S + parameterId+S+S + $scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
										GenericService
										.serviceAction("GET", URI, {})
										.then(
												function(response) 
												{
												    if(response.data.status=="success")
												    {
												    	$scope.listHelpValueMaster = response.data.listObject;
												    	if($scope.listHelpValueMaster!=null)
												    		{
										                    	$('#helpValueSelect').modal('show'); 
												    		}
												    	else{
												    		  growl.warning("No help value found.",
																		{
																			title : 'Warning!'
																		});
												    	   }
												    	$rootScope.stopLoader();
												    }
												    else if(response.data.status=="error")
												    	{
												    	   $scope.listHelpValueMaster =[];
												    	   $rootScope.stopLoader();
												    	}
													
												});
										
									}
								catch (e) {
									console.log(e.message)
								}
							}
							
							
							$scope.selectHelpValue = function(helpValue,isAbnormal)
							{
								if($scope.tType==0)
									{
									$scope.testMasterResList[$scope.tIdx].labResultDetailsViewDto.finalResult=helpValue; 
									$scope.testMasterResList[$scope.tIdx].labResultDetailsViewDto.isAbnormal=isAbnormal; 
									}
								else if($scope.tType==1)
									{
									$scope.testMasterResList[$scope.tIdx].listMultyParamTestHeaderDto[$scope.hIdx].listLabResultDetailsViewDto[$scope.rIdx].finalResult =helpValue;
									$scope.testMasterResList[$scope.tIdx].listMultyParamTestHeaderDto[$scope.hIdx].listLabResultDetailsViewDto[$scope.rIdx].isAbnormal =isAbnormal;
									}
								
								$('#helpValueSelect').modal('hide'); 
							}
							
							 $scope.machineList =[];
							   $scope.getMachineList = function() {
								   var data = {
								   "organisationId" : $scope.orgId,
								   "unitId" : $scope.orgUnitId,
								   }
								   var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ GETMACHINELIST;
								   GenericService.serviceAction("POST", URI, data).then(
										   function(response) {
											   $scope.machineList = [];
											   if (response.data.status == "success") {
												   $scope.machineList = response.data.listObject;
											   }
								   });

								    }
								   $scope.getMachineList();
								   
								   
								   $scope.selectMachine = function(indx,testRestObj) {
									   
									   if(testRestObj.machineId != null && testRestObj.machineId != ""){
										   
										   var data = {
												   "testId" : testRestObj.testId,
												   "machineId" : testRestObj.machineId,
												   "genderId" : $scope.testObj.genderId,
												   "patientAgeDays" :  $scope.testObj.patientAgeDays,
												   }
												   var URI = BASE_URL + ROOT_URL + "/LIS/BioChemistry/getParameterValueByMachineId";
												   GenericService.serviceAction("POST", URI, data).then(
														   function(response) {
															   if (response.data.status == "success") {
																   
																   if($scope.testMasterResList[indx].testType==0){
																	   	if($scope.testMasterResList[indx].labResultDetailsViewDto!=null){

																	   		if(response.data.listObject != null && response.data.listObject.length){
																	   			if(response.data.listObject[0].parameterId == $scope.testMasterResList[indx].labResultDetailsViewDto.parameterId){
																	   				if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 1 ){
																									
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMin = response.data.listObject[0].parameterMin;
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMax = response.data.listObject[0].parameterMax;
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMin = response.data.listObject[0].paramAbnrmlMin;
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMax = response.data.listObject[0].paramAbnrmlMax;
																								}else if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 2){
																									
																									$scope.testMasterResList[indx].labResultDetailsViewDto.textualRangeName = response.data.listObject[0].textualRangeName;
																								}else{
																									
																									$scope.testMasterResList[indx].labResultDetailsViewDto.multitextaulRange = response.data.listObject[0].multitextaulRange
																								}
																	   				
																	   							if(response.data.listObject[0].paramUnit!=null){
																		   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramUnit = response.data.listObject[0].paramUnit;
																	   							}else{
																		   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramUnit = response.data.listObject[0].defaultParamUnit;
																	   							}
					
																								$scope.changeArrowPosition(indx,0,0,$scope.testMasterResList[indx].testType);

																	   			}
																	   		}else{
																	   			if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 1 ){
																							
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMin = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParameterMin;
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMax = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParameterMax;
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMin = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParamAbnrmlMin;
																	   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMax = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParamAbnrmlMax; 
																							}else if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 2){
																								
																								$scope.testMasterResList[indx].labResultDetailsViewDto.textualRangeName = $scope.testMasterResList[indx].labResultDetailsViewDto.originalTextualRangeName;
	                                                                                            
																							}else{
																								
																								$scope.testMasterResList[indx].labResultDetailsViewDto.multitextaulRange = $scope.testMasterResList[indx].labResultDetailsViewDto.originalMultitextaulRange
																							}
																	   			
																	   						$scope.testMasterResList[indx].labResultDetailsViewDto.paramUnit = $scope.testMasterResList[indx].labResultDetailsViewDto.defaultParamUnit;
																	 			   			$scope.changeArrowPosition(indx,0,0,$scope.testMasterResList[indx].testType);
																	   		}
																	   	}
																	   }else {
																		   angular.forEach($scope.testMasterResList[indx].listMultyParamTestHeaderDto,function(value,index) {
									        		                           
																		 		  angular.forEach(value.listLabResultDetailsViewDto,function(value1,index1) {
																						var count = 0;
																		 			  angular.forEach(response.data.listObject,function(value2,index2) {
																							
																								if(value1.parameterId == value2.parameterId){
																									count = 1;
																									if(value1.refrangeTypeId == 1 ){
																										
			                                                                                           value1.parameterMin = value2.parameterMin;
			                                                                                           value1.parameterMax = value2.parameterMax;
			                                                                                           
			                                                                                           value1.paramAbnrmlMin = value2.paramAbnrmlMin;
			                                                                                           value1.paramAbnrmlMax = value2.paramAbnrmlMax;
																									}else if(value1.refrangeTypeId == 2){
																										
			                                                                                            value1.textualRangeName = value2.textualRangeName;
			                                                                                            
																									}else{
																										
			                                                                                            value1.multitextaulRange = value2.multitextaulRange
																									}
																									
																									if(value2.paramUnit!=null){
																										value1.paramUnit = value2.paramUnit;
																			   						}else{
																			   							value1.paramUnit = value2.defaultParamUnit;
																			   						}
																									$scope.changeArrowPosition(indx,index,index1,$scope.testMasterResList[indx].testType);
																								}
																							});
																		 			  		
																		 			   	   if(count == 0){
																		 			   		   
																		 			   		if(value1.refrangeTypeId == 1 ){
																								
		                                                                                           value1.parameterMin = value1.originalParameterMin;
		                                                                                           value1.parameterMax = value1.originalParameterMax;
		                                                                                           
		                                                                                           value1.paramAbnrmlMin = value1.originalParamAbnrmlMin;
		                                                                                           value1.paramAbnrmlMax = value1.originalParamAbnrmlMax;
																								}else if(value1.refrangeTypeId == 2){
																									
		                                                                                            value1.textualRangeName = value1.originalTextualRangeName;
		                                                                                            
																								}else{
																									
		                                                                                            value1.multitextaulRange = value1.originalMultitextaulRange
																								}
																		 			   			value1.paramUnit = value1.defaultParamUnit;
																		 			   			$scope.changeArrowPosition(indx,index,index1,$scope.testMasterResList[indx].testType);
																		 			   	   }
																		 			   	 
																						});
																		 		  
																		 		  		
										                                            });
																	   }
																 	
															   }
												   });
									   }

									  }
							
							
							$scope.getBiochemResultEntryDetails = function() 
							{
								
								try {
									$scope.testMasterResList = [];
										$rootScope.startLoader();
										$scope.labResultDetailsMasterDtoDtlsList =[];
										var dataList = [];
										
										var resultEntry = $scope.testObj;
										$scope.testType= resultEntry.testType;
										$scope.patientId  = resultEntry.patientId;
										$scope.labSampleDtlsId =resultEntry.labSampleDtlsId;
										
										var data = {
												"orgId": $scope.orgId,
												"orgUnitId":$scope.orgUnitId,
												"testId":resultEntry.testId,
												"testType":resultEntry.testType,
												"deptId":resultEntry.deptId,
												"subDeptId":resultEntry.subDeptId,
												"patientAgeDays":resultEntry.patientAgeDays,
												"labSampleNo":resultEntry.sampleNo,
												"genderId":resultEntry.genderId,
												"assingnedFrom":$scope.orgUnitId,
												"labSampleDtlsId":resultEntry.labSampleDtlsId
												
											};
											if(resultEntry.labTestResId != undefined && resultEntry.labTestResId != null){
												data.labResultId = resultEntry.labTestResId;
												$scope.labTestResId = resultEntry.labTestResId;
											}else{
												$scope.labTestResId = null;
												data.labResultId = null;
											}
											dataList.push(data);
								
											if( $scope.labTestResId != undefined && $scope.labTestResId != null){
												var URI = BASE_URL + ROOT_URL
												+ LIS_BIOCHEMISTRY
												+ GET_WORK_LIST_VALIDATION_ENTERY_DETAILS;
											}else{
												var URI = BASE_URL + ROOT_URL
												+ LIS_BIOCHEMISTRY
												+ GET_WORK_LIST_DEPT_RESULT_ENTERY_DETAILS;
											}
									

									GenericService .serviceAction(METHOD_POST, URI, dataList)
											.then(
													function(response) {
														$rootScope.stopLoader();
														if (response.data.status == 'success')
														{
															$rootScope.stopLoader();
															$scope.testMasterResList = [];
															$scope.testMasterResList = response.data.listObject;
															angular.forEach($scope.testMasterResList, function(value, key) 
															{
																value.isSelected = true;
															    value.isRecollected =false;
															    value.isRetestRecollected = false;
                                                              
															    angular.forEach($scope.testMasterResList, function(value, key) {
																   // $scope.getPreviousDeltaReport(value.testId,value.testType,value.labSampleDtlsId,key);
																    if(value.labResultDetailsViewDto!=undefined && value.labResultDetailsViewDto!=null){
																    		if(value.labResultDetailsViewDto.listHelpValueMaster.length>0){
																    			angular.forEach(value.labResultDetailsViewDto.listHelpValueMaster,function(val2,indx2){
																    				
																    				if(val2.isDefault){
																    					value.labResultDetailsViewDto.finalResult = val2.helpValue;
																    					value.labResultDetailsViewDto.isAbnormal = val2.isAbnormal;
																    				}else{
																    					value.labResultDetailsViewDto.isAbnormal = val2.isAbnormal;
																    				}
																    			});
																    		}
																    }else if(value.listMultyParamTestHeaderDto!=undefined && value.listMultyParamTestHeaderDto!=null && value.listMultyParamTestHeaderDto.length>0){
														    			angular.forEach(value.listMultyParamTestHeaderDto,function(val2,indx2){
														    				if(val2.listLabResultDetailsViewDto!=undefined && val2.listLabResultDetailsViewDto!=null && val2.listLabResultDetailsViewDto.length>0){
														    					angular.forEach(val2.listLabResultDetailsViewDto,function(val3,indx3){
														    						if(val3.listHelpValueMaster!=undefined && val3.listHelpValueMaster!=null && val3.listHelpValueMaster.length>0){
														    						angular.forEach(val3.listHelpValueMaster,function(val4,indx4){
														    						if(val4.isDefault){
														    						    val3.finalResult = val4.helpValue;
														    						    val3.isAbnormal = val4.isAbnormal;
														    						}
														    					});
														    						}else{
														    							if(val3.firstLevelResult == null){
														    								 val3.finalResult = val3.firstLevelResult;
														    							}else if($scope.isOnHoldParameter == true) {
														    								 val3.finalResult = val3.firstLevelResult;
														    							}
														    						}
														    					});
														    				}

														    			});
														    }
																    
																    $scope.changeArrowPosition(key,0,0,$scope.testMasterResList[key].testType);
															    });
															});
															
															$scope.getMachineResult(1);
															
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
									console.log(e.message)
								}

							}
							
							
							$scope.getMachineResult = function(onload) 
							{  
								try {
									
									 var mapObj = {};
									angular.forEach($scope.testMasterResList, function(value,key)
											{
										      mapObj[value.labSampleNo] = value.testId;
											});
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ GET_LAB_MACHINE_RESULT;
									GenericService
											.serviceAction(METHOD_POST, URI,mapObj)
											.then(
													function(response) {
														if(response.data.status == 'success')
															{
															   if(response.data.listObject!=null)
																   {
																       var machineResList = response.data.listObject;
																       
																       if(machineResList.length == 0 && (onload == undefined || onload == null)){
																    	   growl.warning("Data not available..",
																					{
																						title : 'warning!'
																					});
																		   $rootScope.stopLoader();
																		   return false;
																       }
																       
																        function compareResult(parameterId,labSampleDtlsId)
																       {
																    	   var obj = machineResList.find(function(value, index) {
																    		   if(value.parameterId==parameterId && value.labSampleDtlsId==labSampleDtlsId)
																    		      {
																    		        return value.paramResult;
																    		      }
																    		 });
																       }
																       angular.forEach($scope.testMasterResList, function(testObj, key)
																				{
																    	              var res = "";
																				      if(testObj.testType==0)
																				    	  {
																				    	  var obj = machineResList.find(function(value, index) {
																				    		   if(value.parameterId==testObj.labResultDetailsViewDto.parameterId &&  value.labSampleDtlsId==testObj.labSampleDtlsId)
																				    		      {
																				    			   testObj.labResultDetailsViewDto.finalResult= value.paramResult;
																				    			   $scope.changeArrowPosition(key,0,0,testObj.testType);
																				    		      }
																				    		 });
																				    	  }
																				       else if(testObj.testType==1)
																				    	  {
																				    	  angular.forEach(testObj.listMultyParamTestHeaderDto, function(headerObj, headerkey)
																									{
																								       angular.forEach(headerObj.listLabResultDetailsViewDto, function(resultObj, resultKey)
																										{
																								    	   var obj = machineResList.find(function(value, index) {
																								    		   if(value.parameterId==resultObj.parameterId &&  value.labSampleDtlsId==testObj.labSampleDtlsId)
																								    		      {
																								    		       if(resultObj.refrangeTypeId !=undefined && resultObj.refrangeTypeId == 1 && resultObj.paramPrecision == 0){
																								    		    	     var toStringResult = Math.round(value.paramResult)
																								    		             resultObj.finalResult = toStringResult.toString();
																								    		       }else{
																								    		       	    resultObj.finalResult =value.paramResult;
																								    		       }
																								    			   $scope.changeArrowPosition(key,headerkey,resultKey,testObj.testType);
																								    		      }
																								    		 });
																										});
																									});
																				    	  }
																				        
																				});
																       $rootScope.stopLoader();
																   }
															   else{
																   growl.error("No Result Found..",
																			{
																				title : 'Error!'
																			});
																   $rootScope.stopLoader();
															      }
															   
															}
														else if(response.data.status == 'error')
															{
															  $rootScope.stopLoader();
															}
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
							
							$scope.changeArrowPosition  = function(testIndex,headerIndex,resultIndex,testType,isEditable)
							 {
								if(testType==0)
									{
								
									if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == undefined || $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == null || $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == "") 
									{
									 
									 if($scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE){
										 $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.arrowClass  = '';
										 $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.isAbnormal  = 'N';
									 }
                                	  
                                }
								
								
								if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE)
									{
									
									 if( $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult > $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax) {
                                      
                                       $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-up color-red';
                                         $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
                                        
                                   }else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult < $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin){
                                      
                                       $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-down color-red';
                                         $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
                                        
                                   }else{
                                      
                                       $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
                                         $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N';
                                   }
                                  
						
									}
								else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==TEXTUAL_RANGE)
									{
									   var finalResult = $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.trim().toLowerCase();
									   var textualRangeName = $scope.testMasterResList[testIndex].labResultDetailsViewDto.textualRangeName.trim().toLowerCase();
									   console.log("finalResult.localeCompare(textualRangeName)",finalResult.localeCompare(textualRangeName));
									   if(finalResult===textualRangeName)
									   {
										   $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
										   $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N'; 
									   }
									   else
									   {
									   }
									   var isDefaultHelpSelected = false;
									   if($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster!=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster.length>0){
										   angular.forEach($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster,function(val,idx){
											  if(val.isDefault){
												  if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult == val.helpValue){
													  isDefaultHelpSelected = true;
												  }
											  } 
										   });
									   }
									
									   $rootScope.stopLoader();
									}
								else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==MULTIVALUED_RANGE)
									{
									$scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
									$scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N';
									
									
									  var isDefaultHelpSelected = false;
									   if($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster!=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster.length>0){
										   angular.forEach($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster,function(val,idx){
											  if(val.isDefault){
												  if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult == val.helpValue){
													  isDefaultHelpSelected = true;
												  }
											  } 
										   });
									   }
								
									}
								
								
								   if($scope.testMasterResList[testIndex].previousTestResultList != undefined && $scope.testMasterResList[testIndex].previousTestResultList.length>0)
									   {
									   
									   
									   var deltaChange = $scope.calculateDeltaPercentage($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.finalResult,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.refrangeTypeId,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.deltaDaysId,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime
											   
											   
									   );
									   if((deltaChange>$scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaPer)||(deltaChange==-1))
										   {
										   $scope.testMasterResList[testIndex].labResultDetailsViewDto.isDeltaFlag = 'Y';
										   if($scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE)
											   {
											    $scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaChange = deltaChange;
											   }
										   
										   }
									   else{
										   $scope.testMasterResList[testIndex].labResultDetailsViewDto.isDeltaFlag = 'N';
										   $scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaChange = 0;
									   }
									     
									   }
								
								}
							else if(testType==1)
								{ 
								
								 if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == undefined || $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == null || $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == "") 
                                {
									 
									 if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==REFERENCE_VALUE){
										 $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
	                                       $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
									 }
                                	  
                                }
								
								
								if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==REFERENCE_VALUE)
								{	

                                   if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult > $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax) {
                                      
                                       $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = 'fa fa-arrow-up color-red';
                                         $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'Y';
                                        
                                   }else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMin !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult < $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMin){
                                      
                                       $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = 'fa fa-arrow-down color-red';
                                         $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'Y';
                                        
                                   }else{
                                      
                                       $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                                         $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
                                        
                                   }
									
								}
								else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null 
										&& $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==TEXTUAL_RANGE)
								{
									 var finalResult = $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult.trim().toLowerCase();
									 var textualRangeName = $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].textualRangeName.trim().toLowerCase();
								   if(finalResult===textualRangeName)
									   {
									   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
									   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
									   }
								   else{
								   }
								   var isDefaultSelected =false;
								   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster!=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster.length>0){
									   angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster,function(value,idx){
										   if(value.isDefault){
											   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == value.helpValue){
												   isDefaultSelected = true;
											   }
										   }
									   });
								   }
								
								   $rootScope.stopLoader();
								}
							   else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null 
									   && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==MULTIVALUED_RANGE)
								{
								
								   
								   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
								   var isDefaultSelected =false;
								   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster!=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster.length>0){
									   angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster,function(value,idx){
										   if(value.isDefault){
											   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == value.helpValue){
												   isDefaultSelected = true;
											   }
										   }
									   });
								   }
								
								}
								if($scope.testMasterResList[testIndex].previousTestResultList!=null && $scope.testMasterResList[testIndex].previousTestResultList.length>0)
								   {
									
								   var deltaChange =  $scope.calculateDeltaPercentage(
										   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult,
										   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult,
										   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId,
										   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].deltaDaysId,
										   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime);
								   
								   if((deltaChange> $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].deltaPer)||(deltaChange==-1))
									   {
									     $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isDeltaFlag = 'Y';
									   }
								   else
								   {
									   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isDeltaFlag = 'N';
								    }
								   }
								
								if( $scope.override == false){
									if(isEditable == null || isEditable == 'N'){
										
										angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(headerObj, key) 
												{
												      angular.forEach(headerObj.listLabResultDetailsViewDto, function(labResObj, key) 
														{
												    	   if(labResObj.isFormula=='Y')
												    		   {
												    		   var formula  = labResObj.parameterFormula;
												    		   console.log('labResObj',labResObj);
												    		   console.log('formula',formula);
															   var simplified = math.simplify(math.parse(formula));
															   var formulaDetailsList = labResObj.listFormulaDetails;
															    var obj = {};
															    var testList = [];
															    var checkParameter = false;
																  angular.forEach(formulaDetailsList, function(value, key)
																   {
																	  
																	  angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(paramInHeaderObj, key)
																			    {
																			        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(paramObj, key)
																			          {
																							//   if(paramObj.parameterId == value.parameterId &&paramObj.refrangeTypeId==REFERENCE_VALUE)
																			        	if(paramObj.parameterId == value.parameterId)
																							   {
																								   if(paramObj.finalResult==null||paramObj.finalResult==""||paramObj==undefined)
																								   {
																									   paramObj.finalResult=null;
																									   checkParameter = true;
																								   }
																								   obj[value.paramVariable.toString()] = paramObj.finalResult==null?0.00:parseFloat(paramObj.finalResult);
																							   }
																				    });  
																				});
																	  
																   });
																  var result = simplified.eval(obj);
																  if(result == 0.00 || isNaN(result)){
																	  if(labResObj.finalResult == undefined ||labResObj.finalResult == null || labResObj.finalResult == ""){
																		 if(checkParameter == true){
																			 labResObj.finalResult==null;
																		 }else{
																			 labResObj.finalResult=0.00;
																		 }
																		 
																	  }
																	  else
																		  {
																		  labResObj.finalResult=0.00;
																		  }
																	 
																  } else {
																		  labResObj.finalResult =result.toFixed(labResObj.paramPrecision);
																	  
																  }
																  
																	 if(labResObj.finalResult != undefined && labResObj.finalResult != null && labResObj.finalResult != "" ){
																		 	
																			 if(labResObj.parameterMax != null && labResObj.finalResult > labResObj.parameterMax) {
		                                                                          labResObj.arrowClass  = 'fa fa-arrow-up color-red';
																                  labResObj.isAbnormal  = 'Y';
																			}else if(labResObj.parameterMin != null && labResObj.finalResult < labResObj.parameterMin){
		                                                                          labResObj.arrowClass  = 'fa fa-arrow-down color-red';
																                  labResObj.isAbnormal  = 'Y';
																			}else{
		                                                                          labResObj.arrowClass  = '';
																                  labResObj.isAbnormal  = 'N';
																			}
																		 }
																  
												    		   }
													  
													    });
												});
										}
									}
								
								 };
								}
							
							
							$scope.labResultMasterDtoList = [];
							
							$scope.createLabResultObjectList = function(sampleStatusId)
							{
								angular.forEach($scope.testMasterResList, function(value, key) {
									
									if(value.isSelected==true)
										{
										 $scope.labResultMasterDto = {};
										
										
										if(value.testType==0)
										{
											$scope.labResultMasterDto.deptId =$scope.testObj.deptId;
											$scope.labResultMasterDto.genderId=$scope.testObj.genderId;
											$scope.labResultMasterDto.orderDetailsId=$scope.testObj.orderDetailsId;
											$scope.labResultMasterDto.patientId=$scope.testObj.patientId;
											$scope.labResultMasterDto.priorityId=1;
											$scope.labResultMasterDto.subDeptId=$scope.testObj.subDeptId;
											$scope.labResultMasterDto.testId=$scope.testObj.testId;
											$scope.labResultMasterDto.visitTypeId=$scope.testObj.visitTypeId;
											$scope.labResultMasterDto.patientAgeDays = $scope.testObj.patientAgeDays; 
											$scope.labResultMasterDto.paymentEntitlementId = $scope.testObj.paymentEntitlementId;
											
											  $scope.listLabResultEntryDtls = [];
											  $scope.labResultMasterDto.createdBy =$scope.userId;
											  $scope.labResultMasterDto.updatedBy =$scope.userId;
								    	      $scope.labResultMasterDto.orgId= $scope.orgId;
								    	      $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
								    	      $scope.labResultMasterDto.testDesc = value.testDesc;
								    	      $scope.labResultMasterDto.footsNotes=value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;
								    	      $scope.labResultMasterDto.isReportRecall=false;
								    	      value.labResultDetailsViewDto.orgId = $scope.orgId;
								    	      value.labResultDetailsViewDto.orgUnitId  =$scope.orgUnitId;
								    	      value.labResultDetailsViewDto.listHelpValueMaster = [];
								    	      value.labResultDetailsViewDto.listParamRefrengMaster = [];
								    	      if( value.labResultDetailsViewDto.isAbnormal == undefined ||  value.labResultDetailsViewDto.isAbnormal== null){
								    	      	  value.labResultDetailsViewDto.isAbnormal = "N";
								    	      }
								    	    //to append precision to int val 
										      if(value.labResultDetailsViewDto!=undefined && value.labResultDetailsViewDto!=null  &&  value.labResultDetailsViewDto.refrangeTypeId !=null && value.labResultDetailsViewDto.refrangeTypeId == 1){
										      		 if(! value.labResultDetailsViewDto.finalResult.isNaN &&  value.labResultDetailsViewDto.finalResult!=null && value.labResultDetailsViewDto.finalResult!=undefined && value.labResultDetailsViewDto.finalResult!="-"){
										      		     var arr =  value.labResultDetailsViewDto.finalResult.split('.');
										      		     if(arr[1]==undefined &&  value.labResultDetailsViewDto.paramPrecision > 0){
										      		     	 value.labResultDetailsViewDto.finalResult =  parseInt(value.labResultDetailsViewDto.finalResult).toFixed( value.labResultDetailsViewDto.paramPrecision); 
										      		     }	
										      		 }
										      }
								    	      
								    	      $scope.listLabResultEntryDtls.push(value.labResultDetailsViewDto);
										      $scope.labResultMasterDto.footsNotes = value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
										      $scope.labResultMasterDto.listLabSampleResultDetailsMaster = $scope.listLabResultEntryDtls;
										      $scope.labResultMasterDto.resultLevel = RESULT_ENTRY;
										      $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
										      $scope.labResultMasterDto.remark = (value.remarks == undefined || value.remarks == null )? " " : value.remarks ;
										  
										      $scope.labResultMasterDto.insuranceId =  value.insuranceId;
										      $scope.labResultMasterDto.companyId =  value.companyId;
										      $scope.labResultMasterDto.machineId = value.machineId;
										      if(  $scope.labTestResId != undefined && $scope.labTestResId != null){
										    	  $scope.labResultMasterDto.labTestResId = $scope.labTestResId;
										      }else{
										    	  $scope.labResultMasterDto.labTestResId = null;
										      }
                                             
                                              $scope.labResultMasterDto.labSampleDtlsId = value.labSampleDtlsId;
										      $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
										}
										else if(value.testType==1)
										{
											$scope.listLabResultEntryDtls  = [];
											$scope.labResultMasterDto.deptId =$scope.testObj.deptId;
											$scope.labResultMasterDto.genderId=$scope.testObj.genderId;
											$scope.labResultMasterDto.orderDetailsId=$scope.testObj.orderDetailsId;
											$scope.labResultMasterDto.patientId=$scope.testObj.patientId;
											$scope.labResultMasterDto.priorityId=1;
											$scope.labResultMasterDto.subDeptId=$scope.testObj.subDeptId;
											$scope.labResultMasterDto.testId=$scope.testObj.testId;
											$scope.labResultMasterDto.visitTypeId=$scope.testObj.visitTypeId;
											$scope.labResultMasterDto.patientAgeDays = $scope.testObj.patientAgeDays; 
											$scope.labResultMasterDto.paymentEntitlementId = $scope.testObj.paymentEntitlementId;
											$scope.labResultMasterDto.createdBy=$scope.userId;
											$scope.labResultMasterDto.updatedBy =$scope.userId;
						    	            $scope.labResultMasterDto.orgId= $scope.orgId;
						    	            $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
						    	            if(  $scope.labTestResId != undefined && $scope.labTestResId != null){
										    	  $scope.labResultMasterDto.labTestResId = $scope.labTestResId;
										      }else{
										    	  $scope.labResultMasterDto.labTestResId = null;
										      }
						    	            $scope.labResultMasterDto.testDesc = value.testDesc;
						    	            
						    	            $scope.labResultMasterDto.footsNotes=value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;
											angular.forEach(value.listMultyParamTestHeaderDto, function(headerParamObj, key) 
													{
												
												angular.forEach(headerParamObj.listLabResultDetailsViewDto, function(labResultDetails, key) 
														{
													
													      labResultDetails.orgId = $scope.orgId;
													      labResultDetails.orgUnitId =$scope.orgUnitId;
													      labResultDetails.listHelpValueMaster = [];
													      labResultDetails.listParamRefrengMaster = [];
													      if(labResultDetails.isAbnormal== null || labResultDetails.isAbnormal == undefined){
													    	  labResultDetails.isAbnormal = 'N';
													      }
													      
												      		 if(labResultDetails.finalResult!=undefined && labResultDetails.finalResult!=null 
												      				 && ! labResultDetails.finalResult.isNaN && labResultDetails.finalResult!='-'  
												      					 &&  labResultDetails.refrangeTypeId !=null && labResultDetails.refrangeTypeId== 1){
												      			if(labResultDetails.finalResult==0)
											      				 {
											      				labResultDetails.finalResult="0";
											      				 }
												      		     var arr =  labResultDetails.finalResult.split('.');
												      		     if(arr[1]==undefined &&  labResultDetails.paramPrecision > 0 ){
												      		    	labResultDetails.finalResult =  parseInt(labResultDetails.finalResult).toFixed( labResultDetails.paramPrecision); 
												      		     }else  if(arr[1]!=undefined &&  labResultDetails.paramPrecision > 0 ){
												      		    	labResultDetails.finalResult = parseFloat(labResultDetails.finalResult).toFixed(labResultDetails.paramPrecision);
												      		     }	
												      		 }
													      $scope.listLabResultEntryDtls.push(labResultDetails);
													  });
												});
											  $scope.labResultMasterDto.listLabSampleResultDetailsMaster = $scope.listLabResultEntryDtls;
											  $scope.labResultMasterDto.footsNotes = value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;
											  $scope.labResultMasterDto.resultLevel = RESULT_ENTRY;
											  $scope.labResultMasterDto.labSampleDtlsId = value.labSampleDtlsId;
											  $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
											  $scope.labResultMasterDto.remark = (value.remarks == undefined || value.remarks == null )? "" :value.remarks ;
											  $scope.labResultMasterDto.insuranceId =  value.insuranceId;
										      $scope.labResultMasterDto.companyId =  value.companyId;
										      $scope.labResultMasterDto.machineId = value.machineId;
											  $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
										 }
										}
									});
							}
							
							
							$scope.saveReportEntry= function(sampleStatusId) 
							{
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.worklistForReportEntryForm.$valid)
								{
									try 
									{
										$rootScope.startLoader();
										$scope.createLabResultObjectList(sampleStatusId);

                                     
										var URI = "";
										
											if( $scope.labTestResId != undefined &&  $scope.labTestResId != null){
												
	                                            URI = BASE_URL + ROOT_URL+"/LIS/BioChemistry/ResultEntry/updateEntryDetails";
	                                            
	                                        }else{
	                                             URI =BASE_URL + ROOT_URL
	                                                + LIS_BIOCHEMISTRY
	                                                + WORK_RESULT_ENTERY_DETAILS; 
	                                        }
										
										GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												
													$scope.labResultMasterList = [];
													$scope.labResultMasterDtoList = [];
													$scope.testMasterResList = [];
													$scope.initIncucationObjservationList($scope.orgId,
															$scope.orgUnitId, $scope.offset,
															$scope.noOfRecordsPerPage);
											} else if(response.data.status == 'error')
											{
												$scope.initIncucationObjservationList($scope.orgId,
														$scope.orgUnitId, $scope.offset,
														$scope.noOfRecordsPerPage);
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
										
									} catch (e) {
										$scope.initIncucationObjservationList($scope.orgId,
												$scope.orgUnitId, $scope.offset,
												$scope.noOfRecordsPerPage);
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
									
								}
								else
								{
									$scope.initIncucationObjservationList($scope.orgId,
											$scope.orgUnitId, $scope.offset,
											$scope.noOfRecordsPerPage);
									
									/*growl.error("Please Fill All Mandatory Fields.",
											{
												title : 'Error!'
											});*/
								}
							}
							
							
							
							$scope.saveResult = function(sampleStatusId)
							{
								$scope.SensitivityTestResultMasterDto.createdDate = moment().toDate().getTime();
								$scope.SensitivityTestResultMasterDto.reportReleaseBy = ($scope.pathologiestId!=null &&  $scope.pathologiestId>0)?$scope.pathologiestId:$scope.userId;
								$scope.SensitivityTestResultMasterDto.labSampleDtlsId = $scope.labSampleDtlsId;
								$scope.SensitivityTestResultMasterDto.orderDetailsId = $scope.orderDetailsId;
								$scope.SensitivityTestResultMasterDto.doctorId = $scope.patientDetailsDto.doctorId;
								$scope.SensitivityTestResultMasterDto.patientId = $scope.patientDetailsDto.patientId;
								$scope.SensitivityTestResultMasterDto.testDesc = $scope.patientDetailsDto.testDesc;
								 if($scope.triggerReleaseMail == 'Y'){
									  $scope.SensitivityTestResultMasterDto.triggerReleaseMail = true;
								  }else{
									  $scope.SensitivityTestResultMasterDto.triggerReleaseMail = false;
								  }
								 
							    $scope.SensitivityTestResultMasterDto.listSensitivityTestResultDetailsMaster = $scope.listSensitivityTestResultDetailsMasterDto;
							    
							    $('#details').modal('hide');
								$rootScope.startLoader();
								try {
									console.log("$scope.listLabSampleDtls",JSON.stringify($scope.SensitivityTestResultMasterDto));
									var URI = BASE_URL + ROOT_URL
									        + LIS_MICROBIOLOGY
											+ SAVE_SENSITIVTY_DETAILS + S + sampleStatusId;
									console.log("SEND_TO_Microbiology WORKLIST", URI);
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.SensitivityTestResultMasterDto)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{ 
															$scope.flagCheck = true;
															$rootScope.stopLoader();
															
															$scope.saveReportEntry(sampleStatusId);
															$scope.patientId = undefined;
															$scope.init();
															
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
														
														} else if (response.data.status == 'error') {
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
							$scope.saveSensitivityResult = function(labSampleStausId,flag) 
							{
								var check =false;;
								angular.forEach($scope.listSensitivityTestResultDetailsMasterDto, function(value, key) {
									angular.forEach(value.listSensitivityTestResultSubDetailsMaster, function(value1, key) 
											{
										
											check =true;
										});
									});
								
								
								if(labSampleStausId == 8 && check == false){
									
									growl.warning("Please add organism ",{
										title : ERROR_MSG
									});
									
									return false;
								}
								
								
								if(flag)
									{
									var isExists =false;;
									var list = $scope.listSensitivityTestResultDetailsMasterDto
									angular.forEach($scope.listSensitivityTestResultDetailsMasterDto, function(value, key) {
										angular.forEach(value.listSensitivityTestResultSubDetailsMaster, function(value1, key) 
												{
											if( value1.isChecked == true)
												{
												  isExists =true;
												}
											});
										});
									
									if(!isExists)
										{
										growl.error("Please select Antibiotics.",
												{
													title : 'Error!'
												});
										return false;
										}
									else{
										$scope.saveResult(labSampleStausId);
									  }
									}
							
								else
								{
									$scope.saveResult(labSampleStausId);
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
							$scope.sampleStatusIds=[18,34,35];
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
						
 
						$scope.confirmationPopUp= function(parentInx,info,index){
							$scope.parentIndex = parentInx;
							$scope.childIndex = index;
							if(info.listSensitivityTestResultSubDetailsMaster[index].sesitivityResultSubDetailsId!=""){
							$scope.removeSesitivityResultSubDetailsId = info.listSensitivityTestResultSubDetailsMaster[index].sesitivityResultSubDetailsId;
							angular.element('#confirmationPopUp').modal('show');
							}
							else{
								angular.element('#confirmationPopUp').modal('show');
							    }
						}
						
						$scope.removeAntibiotic= function(){
							
							if($scope.removeSesitivityResultSubDetailsId!=""  && $scope.removeSesitivityResultSubDetailsId != undefined){
							$rootScope.startLoader();
							var URI =  BASE_URL + ROOT_URL +"/LIS/Microbiology/sensitivityTesting/removeSensitivityTestResultSubDetails" + S +$scope.removeSesitivityResultSubDetailsId+ S +$scope.orgId+ S +$scope.orgUnitId;
							GenericService.serviceAction(METHOD_DELETE , URI , null).then(function(response) {
								if (response.data.status == "success"){
									$scope.removeSesitivityResultSubDetailsId="";
									$rootScope.stopLoader();
									$scope.listSensitivityTestResultDetailsMasterDto[$scope.parentIndex].listSensitivityTestResultSubDetailsMaster.splice($scope.childIndex , 1);
									if($scope.listSensitivityTestResultDetailsMasterDto[$scope.parentIndex].listSensitivityTestResultSubDetailsMaster.length == 0){
										var sesitivityResultDetailsId =	$scope.listSensitivityTestResultDetailsMasterDto[$scope.parentIndex].sesitivityResultDetailsId
										var sensitivityResultId = $scope.listSensitivityTestResultDetailsMasterDto[$scope.parentIndex].sensitivityResultId
										$scope.removeOrganismGroup(sesitivityResultDetailsId , sensitivityResultId);
										$scope.listSensitivityTestResultDetailsMasterDto.splice($scope.parentIndex , 1);
									}
									else {
										growl.success(
												 response.data.message,
												 {
													 title: 'Success!'
												 }
												)	
									}
								}
								else {
									growl.error(
										 response.data.message,
										 {
											 title: 'Error!'
										 }
										)
								}
								$rootScope.stopLoader();
							});
						   }	
							
							else{
								$scope.listSensitivityTestResultDetailsMasterDto[$scope.parentIndex].listSensitivityTestResultSubDetailsMaster.splice($scope.childIndex, 1);

							      if($scope.listSensitivityTestResultDetailsMasterDto[$scope.parentIndex].listSensitivityTestResultSubDetailsMaster.length == 0){
							    	  $scope.listSensitivityTestResultDetailsMasterDto.splice($scope.parentIndex , 1);
									}
							    }

						}
						
						
						$scope.removeOrganismGroup= function(sesitivityResultDetailsId , sensitivityResultId){
							var URI =  BASE_URL + ROOT_URL +"/LIS/Microbiology/sensitivityTesting/removeSensitivityTestResultDetails" + S +sesitivityResultDetailsId+ S +sensitivityResultId + S +$scope.orgId+ S +$scope.orgUnitId;
							GenericService.serviceAction(METHOD_DELETE , URI , null).then(function(response) {
								if (response.data.status == "success"){

									growl.success(
											 response.data.message,
											 {
												 title: 'Success!'
											 }
											)
								}
							});
						}
					
 			
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
						
						$scope.labSampleId = null;
						$scope.selectedSampleNo = null;
						 $scope.selectSampleNo= function($item, $model,$label)
							{
	
							 
						      $scope.labSampleId=$item.labSampleId;
						      $scope.selectedSampleNo = $item.sampleNo;
						      
						      $scope.labSampleDtlsId = $item.labSampleDtlsId;
						      
						    
					        }
						 $scope.searchMicroBiology = function()
							{
								try 
								{
									$rootScope.startLoader();
									if($scope.selectedSampleNo == null || $scope.selectedSampleNo == ""){
										$scope.labSampleId = null;
										$scope.labSampleDtlsId = 0;
									}
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format('YYYY-MM-DD'):moment($scope.fromDate).format('YYYY-MM-DD'),
								                  "toDt":  $scope.toDate==undefined?moment(new Date()).format('YYYY-MM-DD'):moment($scope.toDate).format('YYYY-MM-DD'),
												  "patientId" :  $scope.selectedPatient,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "labSampleId" : $scope.labSampleId,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" :$scope.selectedSampleNo!=undefined ? $scope.selectedSampleNo : null,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "assignUnit" :$scope.orgUnitId ,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "offset":  $scope.offset
		                             }
									
									console.log("collectedSampleList",JSON.stringify(searchObj));
									var URI = BASE_URL 
									        + ROOT_URL
									    	+ LIS_MICROBIOLOGY
											+ SENSITIVITY_TESTING_LIST_SEARCH
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 searchObj).then(
													 function(response) {
														 $scope.sampleAccPendingList=[];
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																$scope.incubationObservationList =  response.data.listObject;
																$scope.commonListCount = response.data.object> 0 ? response.data.object : 0 ;
																$scope.incubationObservationListForFiltter = $scope.incubationObservationList;
																$scope.filterByDummyServices();
																$scope.patientId = undefined;
																$scope.setPage(1,false);
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
						 
						 $scope.closePopup = function(){
							 $scope.patientId = undefined;
							 $('#sensitivityTesting').modal('hide'); 
						 }

						 $scope.getAntibioticListOnload = function(){
						    angular.forEach($scope.listSensitivityTestResultDetailsMasterDto , function(organism){
                                if(organism.listSensitivityTestResultSubDetailsMaster === undefined || organism.listSensitivityTestResultSubDetailsMaster === null || organism.listSensitivityTestResultSubDetailsMaster.length == 0){
                                    var data = "";
                                    try{
                                        var URI = BASE_URL + ROOT_URL
                                                    + LIS_COMMON
                                                    + GET_ANTIBIOTICS_BY_ORGAINSM+S+$scope.orgId+S+'organismId'+S+organism.organismId;
                                        console.log("URI", URI);

                                        return GenericService
                                        .serviceAction(METHOD_GET, URI, data)
                                        .then(
                                            function(response)
                                            {
                                                var commonList = response.data.listObject;
                                                if (commonList != null)
                                                {
                                                $scope.listSensitivityTestResultSubDetailsMasterDto =[];
                                                angular.forEach(commonList, function(value, key)
                                                {
                                                     var SensitivityTestResultSubDetailsMasterDto = angular.copy($scope.SensitivityTestResultSubDetailsMasterDto);
                                                     SensitivityTestResultSubDetailsMasterDto.antibioticId = value.id;
                                                     SensitivityTestResultSubDetailsMasterDto.antibioticName =value.name;
                                                     $scope.listSensitivityTestResultSubDetailsMasterDto.push(SensitivityTestResultSubDetailsMasterDto);
                                                });
                                                organism.listSensitivityTestResultSubDetailsMaster = $scope.listSensitivityTestResultSubDetailsMasterDto;

                                                }
                                            });
                                    } catch (e) {
                                        console.log(e.message)
                                    }
                                }
						    });
						 }
						 
						 
						 

						} ]);