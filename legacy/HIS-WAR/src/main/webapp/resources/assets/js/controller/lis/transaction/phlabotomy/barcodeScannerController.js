
/**
 * @author 
 */

angular
		.module('myApp')
		.controller(
				"barcodeScannerController",
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
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$timeout,$stateParams) {

							
							
							$rootScope.loginpage = true;
							
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
							$scope.consolidatedResultEntry = labConfig.consolidatedResultEntry;
							$scope.labSortingId =  labConfig.labSortingId;
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.dateFormat = cookieObjectAppConfig.dateFormat;
							 $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							$scope.departmentList = [];
							$scope.getDepartMentList = function(orgId,orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_DEPARMENT_LIST
											+ S + orgId + S + orgUnitId;
									console.log("URI", URI);
									$scope.departmentList = [];
									GenericService.serviceAction("GET", URI, data)
											  .then(function(response) {
												  angular.forEach(response.data.listObject, function(value, key) {
														var multiselectObj = angular.copy($scope.multiselectObj);
														if($scope.consolidatedResultEntry=='Y'){
															if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id){
																multiselectObj.id = value.id;
																multiselectObj.label = 	value.name;
																$scope.departmentList.push(multiselectObj);
															}
														}else{
															if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id
																&& labConfig.biochemistryId!=value.id && labConfig.hematologyId!=value.id
																&& labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id){
																multiselectObj.id = value.id;
																multiselectObj.label = 	value.name;
																$scope.departmentList.push(multiselectObj);
															}
														}
														});
													});
								   } catch (e) {
									console.log(e.message)
								}
							}
							$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
							
							
														
							
														 
														
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
						//	$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							
						    $scope.dropDownSetting = {
						    		scrollableHeight: '100px', 
						    		scrollable: true
						    }; 
						    
						/** Service Autocomplete List * */
						    
						    
						/*get Visi tType List*/
						$scope.getVisitTypeList = function()
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"deptId":$scope.deptId,
										//"subDeptId":$scope.subDeptId,
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
										//"subDeptId":$scope.subDeptId
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
										//"subDeptId":$scope.subDeptId,
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
						 $scope.selectPatient = function($item, $model,$label)
							{
						      $scope.selectedPatient = $item.id;
						     // $scope.PatientName= $item.firstName +"  "+$item.lastName;
						      $scope.uhid=$item.uhid;
						      $scope.searchList();
					        }
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
											//"subDeptId":$scope.subDeptId,
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
							} else {
						        var filteredList = $filter('filter')($scope.patientListNew, { $: searchKeyword });
						        if (filteredList.length === 0) {
						            $scope.filteredPatientList = null;
						        } else {
						            $scope.filteredPatientList = filteredList;
						        }
						        return $scope.filteredPatientList;
						    }
						};
						 $scope.dataCheck=false;
						 $scope.searchList = function()
							{
								try 
								{
									$scope.selectedResEntryList = [];
									$scope.resultEntryWorklist = [];
									$rootScope.startLoader();
									var data = {
											  "orgId":$scope.orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId ,
	                                          "offset":$scope.offset,
	                                          "recordPerPage":$scope.noOfRecordsPerPage,
	                                          "deptId":$scope.deptId,
	                                          "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
	    									  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
					                          "patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId==undefined ? null:$scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList == undefined?null :$scope.selectedtestList,
											  "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
											  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
											  "isSendToCrApplicable":$scope.isSendToCrApplicable,
											  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
											  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
											  "departmentList":$scope.departmentList,
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null

									}
									
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/barcodeScannerList/Search";
									GenericService.serviceAction(METHOD_POST, URI, data).then(
											function(response) {
												$rootScope.stopLoader();
												$scope.resultEntryWorklist  = response.data.listObject;
												
												var cleaned = [];
												$scope.resultEntryWorklist.forEach(function(itm) {
													itm["oldSampleReception"]=itm.sampleReception;
													itm["oldSampleProcessing"]=itm.sampleProcessing;
													itm["oldSampleAnalysisAndDetection"]=itm.sampleAnalysisAndDetection;
													if(itm.sampleReception=="" || itm.sampleReception==null)
														{
														itm["sampleReceptionflag"]=false;
														}
													else
														{
														itm["sampleReceptionflag"]=true
														}
													if(itm.sampleProcessing=="" || itm.sampleProcessing==null)
													{
														itm["sampleProcessingflag"]=false;
													}
												else
													{
													itm["sampleProcessingflag"]=true;
													}
													if(itm.sampleAnalysisAndDetection=="" || itm.sampleAnalysisAndDetection==null)
													{
														itm["sampleAnalysisAndDetectionflag"]=false;
													}
												else
													{
													itm["sampleAnalysisAndDetectionflag"]=true;
													}
												var unique = true;
												cleaned.forEach(function(itm2) {
												if (_.isEqual(itm.labSampleDtlsId, itm2.labSampleDtlsId)) unique = false;
												});
												if (unique) cleaned.push(itm);
												});
												$scope.resultEntryWorklist= cleaned;
												
												$scope.commonListCount = response.data.object;
												$scope.setPage(1,false);
											});
								} catch (e) {
									$rootScope.stopLoader();
									console.log(e.message)
								}
							}
						 $scope.searchList();
						 $scope.dataArrayToSave=[];
						 $scope.getSampleReception=function(resultEntry)
						 {
							 if(resultEntry.sampleReceptionflag==false)
								 {
								 resultEntry.sampleReception=moment(new Date()).format($scope.dateFormat.toUpperCase()+" hh:mm:ss");
								 }else
									{
									 resultEntry.sampleReception=resultEntry.oldSampleReception
									} 
							 var data = {
									 "labSampleDtlsId":resultEntry.labSampleDtlsId,
									 "sampleReception":resultEntry.sampleReception!=undefined?resultEntry.sampleReception:'',
									 "sampleProcessing":resultEntry.sampleProcessing!=undefined?resultEntry.sampleProcessing:'',
									 "sampleAnalysisAndDetection":resultEntry.sampleAnalysisAndDetection!=undefined?resultEntry.sampleAnalysisAndDetection:''
							 }
							 if($scope.dataArrayToSave.length>0 && !resultEntry.oldSampleReception)
								 {
								 angular.forEach($scope.dataArrayToSave, function(value, key) {
									 if(value.labSampleDtlsId==resultEntry.labSampleDtlsId)
										 {
										 value.sampleReception=resultEntry.sampleReception!=undefined?resultEntry.sampleReception:'';
										 }
									 else
										 {
										 $scope.dataArrayToSave.push(data);
										 }
								 })
								 }else
									 {
									 if(!resultEntry.oldSampleReception)
										 {
										 $scope.dataArrayToSave.push(data);
										 }
									 }
							 
							 console.log("getSampleReception",$scope.dataArrayToSave);
						 }
						 $scope.getSampleProcessing=function(resultEntry)
						 {
								if(resultEntry.sampleProcessingflag==false)
									{
									 resultEntry.sampleProcessing=moment(new Date()).format($scope.dateFormat.toUpperCase()+" hh:mm:ss");
									}
								else
									{
									 resultEntry.sampleProcessing=resultEntry.oldSampleProcessing
									}
							 var data = {
									 "labSampleDtlsId":resultEntry.labSampleDtlsId,
									 "sampleReception":resultEntry.sampleReception!=undefined?resultEntry.sampleReception:'',
									 "sampleProcessing":resultEntry.sampleProcessing!=undefined?resultEntry.sampleProcessing:'',
									 "sampleAnalysisAndDetection":resultEntry.sampleAnalysisAndDetection!=undefined?resultEntry.sampleAnalysisAndDetection:''
							 }
							 if($scope.dataArrayToSave.length>0 && !resultEntry.oldSampleProcessing){
							 angular.forEach($scope.dataArrayToSave, function(value, key) {
								 if(value.labSampleDtlsId==resultEntry.labSampleDtlsId)
									 {
									 value.sampleProcessing=resultEntry.sampleProcessing!=undefined?resultEntry.sampleProcessing:'';
									 }
								 else
									 {
									 $scope.dataArrayToSave.push(data)
									 }
							 })
						 }else
						 {
							 if(!resultEntry.oldSampleProcessing)
							 {
							 $scope.dataArrayToSave.push(data);
							 }
							 }
							 console.log("getSampleProcessing",$scope.dataArrayToSave);
						 }
						 $scope.getSampleAnalysisAndDetection=function(resultEntry)
						 {
								if(resultEntry.sampleAnalysisAndDetectionflag==false)
									{
									resultEntry.sampleAnalysisAndDetection=moment(new Date()).format($scope.dateFormat.toUpperCase()+" hh:mm:ss");
									}
								else
									{
									resultEntry.sampleAnalysisAndDetection=resultEntry.oldSampleAnalysisAndDetection
									}
							 var data = {
									 "labSampleDtlsId":resultEntry.labSampleDtlsId,
									 "sampleReception":resultEntry.sampleReception!=undefined?resultEntry.sampleReception:'',
									 "sampleProcessing":resultEntry.sampleProcessing!=undefined?resultEntry.sampleProcessing:'',
									 "sampleAnalysisAndDetection":resultEntry.sampleAnalysisAndDetection!=undefined?resultEntry.sampleAnalysisAndDetection:''
							 }
							 if($scope.dataArrayToSave.length>0 && !resultEntry.oldSampleAnalysisAndDetection){
							 angular.forEach($scope.dataArrayToSave, function(value, key) {
								 if(value.labSampleDtlsId==resultEntry.labSampleDtlsId)
									 {
									 value.sampleAnalysisAndDetection=resultEntry.sampleAnalysisAndDetection!=undefined?resultEntry.sampleAnalysisAndDetection:'';
									 }
								 else
									 {
									 $scope.dataArrayToSave.push(data)
									 }
							 })
						 }
						 else
						 {
							 if(!resultEntry.oldSampleAnalysisAndDetection)
							 {
							 $scope.dataArrayToSave.push(data);
							 }
						 }
							 console.log("getSampleAnalysisAndDetection",$scope.dataArrayToSave);
						 }
						 
						 $scope.saveOrUpdate = function(sampleStatusId)
							{
								$rootScope.startLoader();
								try {
									console.log("$scope.listLabSampleDtls",$scope.dataArrayToSave);
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/barcodeScannerList/save";
									GenericService
											.serviceAction(METHOD_POST, URI,
													$scope.dataArrayToSave)
											.then(
													function(response) {
														if (response.data.status == 'success') 
														{ 
															$scope.searchList();
															$scope.flagCheck = true;
															$rootScope.stopLoader();
															$scope.init();
															$scope.initResultEntryList($scope.orgId,
																	$scope.orgUnitId,$scope.deptId,$scope.offset,
																	$scope.noOfRecordsPerPage);
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
						 
						 $scope.typeSearchList = function(searchKeyword)
							{
							 
							    if (searchKeyword.length>=3){
							        
							    	try 
									{
										$scope.selectedResEntryList = [];
										$scope.resultEntryWorklist = [];
										$rootScope.startLoader();
										var data = {
												  "orgId":$scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "assignUnit" :$scope.orgUnitId ,
		                                          "offset":$scope.offset,
		                                          "recordPerPage":$scope.noOfRecordsPerPage,
		                                          "deptId":$scope.deptId,
		                                          "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
		    									  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
												  "isSendToCrApplicable":$scope.isSendToCrApplicable,
												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
												  "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
												  "departmentList":$scope.departmentList,
												  "searchField":searchKeyword,
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null

										}
										
										var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/resultEntry/Type/Search";
										GenericService.serviceAction(METHOD_POST, URI, data).then(
												function(response) {
													$rootScope.stopLoader();
													$scope.resultEntryWorklist  = response.data.listObject;
													$scope.commonListCount = response.data.object;
													$scope.setPage(1,false);
												});
									} catch (e) {
										$rootScope.stopLoader();
										console.log(e.message)
									}
							    	
							    }else if(searchKeyword.length == 0){
							    	$scope.searchList();
							    }

							 
							
							}
						 
						 
						 $scope.getClassForColor = function(info)
		                    {

		                        	   if(info.priorityId==2){
                                 		return 'td-bg-red';
		                        	   }
		                    }
							
						} ]);