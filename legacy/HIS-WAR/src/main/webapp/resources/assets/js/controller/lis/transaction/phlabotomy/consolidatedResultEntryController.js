
/**
 * @author 
 */

angular
		.module('myApp')
		.controller(
				"consolidatedResultEntryController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'$filter',
						'GenericService',
						'BillingGenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'$timeout',
						'$stateParams',
						'reportService',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService,BillingGenericService, PagerService, promiseFactoryWithObject,
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
							 $scope.multiselectObj = {
							    		"id":"",
							    		"label":""
							    }
							$scope.departmentList = [];
							 
							 var specDocObj = {
						    			organizationId:$scope.organizationId,
						    			userId:$scope.userId
						        }
							    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
						        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
												if (response.data.status == "success"){
													$scope.staffSpecialityList = [];
													$scope.staffSubSpecialityList = [];
				                                    $scope.staffSpecialityList = response.data.listObject;
												    $scope.staffSubSpecialityList = response.data.object;
												}
			                                 });
							 
						        
						    	$scope.onLoadCall = function(){
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
										 $scope.initResultEntryList($scope.orgId,	$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
									 },1000);
								}
						    	
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
												  $scope.departmentListtest =  response.data.listObject
														  var specDocObj = {
													    			organizationId:$scope.organizationId,
													    			userId:$scope.userId
													        }
														    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
													        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(function(response) {
																			if (response.data.status == "success"){
																				$scope.staffSpecialityList = [];
											                                    $scope.staffSpecialityList = response.data.listObject;
																			    $scope.staffSubSpecialityList = response.data.object;
																		        if( $scope.staffSubSpecialityList != null &&  $scope.staffSubSpecialityList.length > 0){
																		            angular.forEach($scope.departmentListtest, function(value, key) {
																			            	angular.forEach($scope.staffSubSpecialityList, function(value1, key1) {
																			            		if(value.id == value1.subSpecialityMasterId){
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
																											&& labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id 
																											&& labConfig.cystopathologyId!=value.id){
																											multiselectObj.id = value.id;
																											multiselectObj.label = 	value.name;
																											$scope.departmentList.push(multiselectObj);
																										}
																									}
																			            		}
																			            	});
																					});
																		        }else{
																		            angular.forEach($scope.departmentListtest, function(value, key) {
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
																								&& labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id 
																								&& labConfig.cystopathologyId!=value.id){
																								multiselectObj.id = value.id;
																								multiselectObj.label = 	value.name;
																								$scope.departmentList.push(multiselectObj);
																							}
																						}
																						});
																		        }
																		        $scope.onLoadCall();
																		        
																			}
										                                 });
													
													});
								   } catch (e) {
									console.log(e.message)
								}
							}
							$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
							
							
							$scope.init = function(){
								
								$scope.listSensitivityTestResultMasterDto = [];
								$scope.listSensitivityTestResultDetailsMasterDto = [];
								$scope.listSensitivityTestResultSubDetailsMasterDto = [];
								$scope.labSampleDtlsId =0;
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
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 50;
									$rootScope.startLoader();
									var data = {
											  "orgId":orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId ,
	                                          "offset":offset,
	                                          "recordPerPage":noOfRecordsPerPage,
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
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
					                           "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
					                           "userId": $scope.userId

									}
									
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/resultEntry/Search";
									GenericService.serviceAction(METHOD_POST, URI, data).then(
											function(response) {
												$rootScope.stopLoader();
												$scope.resultEntryWorklist = [] ;
												$scope.resultEntryWorklisttest =[];
												$scope.resultEntryWorklisttest  = response.data.listObject;
												if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
													angular .forEach($scope.resultEntryWorklisttest, function(value, key) {
														angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
															if(value.subDeptId == value1.subSpecialityMasterId){
																$scope.resultEntryWorklist.push(value);
															}
														
														});		
													 });
												}else{
													$scope.resultEntryWorklist  = response.data.listObject;
												}
											
												$scope.commonListCount = response.data.object;
												$scope.resultEntryWorklistForFiltter = $scope.resultEntryWorklist;
												$scope.filterByDummyServices();
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
							    $scope.initResultEntryList($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
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
											 "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
					                          "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
					                        "userId": $scope.userId

										};	
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/resultEntry/Search";
									console.log("URI", URI);

									GenericService.serviceAction(METHOD_POST, URI, data).then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryWorklist = [];
														$scope.resultEntryWorklisttest =[];
														$scope.resultEntryWorklisttest  = response.data.listObject;
														
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular .forEach($scope.resultEntryWorklisttest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.resultEntryWorklist.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.resultEntryWorklist  = response.data.listObject;
														}
														angular.forEach($scope.resultEntryWorklist, function(value, key) 
																{
																	value.statusFlag = false;
															
																});
														$scope.resultEntryWorklistForFiltter = $scope.resultEntryWorklist;
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
									$scope.getBiochemWorkList($scope.orgId, $scope.orgUnitId,$scope.deptId,	$scope.pager.startIndex,	$scope.pager.pageSize);
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
								
								var subId = 0;
								if($scope.selectedResEntryList.length>0)
									{
									$rootScope.startLoader();
									var isSameDept = false;
									if($scope.selectedResEntryList.length>1){
									//	var subId = 0;
										var cnt =0;
										angular.forEach($scope.selectedResEntryList, function(value, key){
											
											if(cnt == 0){
												subId = value.subDeptId;
											}
											if(cnt>0){
												if(subId != value.subDeptId){
													isSameDept = true;
												}
											}
											cnt++;
										});
									}
									if(isSameDept){
										growl.warning("Please select Same Department Test",
												{
													title : 'Warning!'
												});
									  $rootScope.stopLoader();
									  return false;
									}
			
									 subId = $scope.selectedResEntryList[0].subDeptId;
									}
								else{
									growl
									.warning("Please Select Test.",
											{
												title : 'Warning!'
											});
								}
								
								if($scope.selectedIndex.length>0){
									angular.forEach($scope.selectedIndex,function(val,index){
										angular.forEach($scope.resultEntryWorklist,function(value,indx){
											 if(val < indx && labConfig.serologyId == value.subDeptId && value.subDeptId == subId && !value.statusFlag){
												 //serology
												 $scope.nextSerologyRecords.push(value);
											 }else if(val < indx && labConfig.serologyId != value.subDeptId && value.subDeptId == subId && !value.statusFlag){
												 // other dept
												 $scope.nextOtherDeptRecords.push(value);
											 }
										 });
									});
								}
								
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
								
								if($scope.selectedResEntryList[0].subDeptId != labConfig.serologyId ){

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
										 $timeout(function () {
											 $rootScope.stopLoader();
											 $state.go('deatilsReportEntryWorklist',
														{
												 		  subDeptId : $scope.selectedResEntryList[0].subDeptId,
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
								}else if($scope.selectedResEntryList[0].subDeptId == labConfig.serologyId){
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
//													$scope.selectedResEntryList.push(val);
										});
									}
									
									var count = 0;
									 var patientIds = [];
									 var flag = false;
									angular.forEach($scope.selectedResEntryList, function(value, key) {
										value.orgId = value.orgId;
										value.orgUnitId = value.orgUnitId;
										value.createdBy = $scope.userId;
										value.listLabSampleResultDetailsMaster = [];
										patientIds.push(value.patientId);	
										
										count++;
										if(new Set(patientIds).size==1 && $scope.selectedResEntryList.length==count)
										{
											flag =true;
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
										  return false;
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
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
						  				      "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
						  				      "userId": $scope.userId

									}
									
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/resultEntry/Search";
									GenericService.serviceAction(METHOD_POST, URI, data).then(
											function(response) {
												$rootScope.stopLoader();
												$scope.resultEntryWorklist = [];
												$scope.resultEntryWorklisttest =[];
												$scope.resultEntryWorklisttest  = response.data.listObject;
												
												if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
													angular .forEach($scope.resultEntryWorklisttest, function(value, key) {
														angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
															if(value.subDeptId == value1.subSpecialityMasterId){
																$scope.resultEntryWorklist.push(value);
															}
														
														});		
													 });
												}else{
													$scope.resultEntryWorklist  = response.data.listObject;
												}
												$scope.commonListCount = response.data.object;
												$scope.resultEntryWorklistForFiltter = $scope.resultEntryWorklist;
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
													$scope.resultEntryWorklistForFiltter = $scope.resultEntryWorklist;
													$scope.filterByDummyServices();
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
						 
						 $scope.print = function(labSampleDtlsId,key){
							 
								$scope.labSampleDtlsIdsForPrint = [];
						    	$scope.labSampleDtlsIdsForPrint.push(labSampleDtlsId);
						    	
						    	if(labConfig.serologyId==key)
								{
						    		
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
									
								}else{
									
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
							 
						 }
						 
						 
						 
						 
							
						} ]);