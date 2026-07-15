
angular
		.module('myApp')
		.controller(
				"consolidatedReportValidationController",
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

							
							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "worklistForValidation";
							$scope.thirdLevel = false;
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
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							$scope.consolidatedResultEntry = labConfig.consolidatedResultEntry;
							$scope.labSortingId =  labConfig.labSortingId;
							$scope.searchKeyword ="";
							$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
							if (cookieObjectAppConfigSecond != undefined) {
							$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
							}
							$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
							if($scope.isBillManipulationAllowed == true){
								$scope.filterDummyService = 1;
							}else{
								$scope.filterDummyService = 2;
							}
							//Multiple Report Validation
							$scope.selectedValEntryList = [];
							
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
						            "subDeptId": "",
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
									"deltaDaysId":"",
									"infromationDtls": "",
									"informationType": "",
									"informedTo": "",
									"informedDatetime": "",
									"paramRecheckedBy": "",
									"parameterFormula": "",
									"createdBy": $scope.userId,
									"updatedBy": $scope.userId,
									"updatedDate": ""
							}
							
							$scope.multiselectObj = {
						    		"id":"",
						    		"label":""
						    }
							
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
																
																        
																	}
								                                 });
											
											});
								   } catch (e) {
									console.log(e.message)
								}
							}
							$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
							
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
									
									$scope.selectedValEntryList = [];
									$rootScope.startLoader();
									var data = {
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
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
											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
													"userId" : $scope.userId,
										};
									
									
									var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/consolidated/Validation/Search";
;								
									        GenericService.serviceAction(METHOD_POST, URI, data).then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryworklistForValidation = [];
														$scope.resultEntryworklistForValidationtest = [];
														$scope.resultEntryworklistForValidationtest  = response.data.listObject;
														
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular.forEach($scope.resultEntryworklistForValidationtest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.resultEntryworklistForValidation.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.resultEntryworklistForValidation  = response.data.listObject;
														}
														$scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
														$scope.filterByDummyServices();
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);
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
										$scope.selectedDepartMentList = $stateParams.searchObj.selectedDepartMentList;
										$scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
										$scope.selectedtestList = $stateParams.searchObj.selectedtestList;
										$scope.selectedSampleNo = $stateParams.searchObj.selectedSampleNo;
									}
								 $scope.initResultEntryWorklistForValidation($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
							 },1000);
							 
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
							    $scope.initResultEntryWorklistForValidation($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);

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
											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
				                            "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
				                            		"userId" : $scope.userId,
		
										};
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/Validation/Search";
									GenericService
											.serviceAction(METHOD_POST, URI, data).then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryworklistForValidation = [];
														$scope.resultEntryworklistForValidationtest = [];
														$scope.resultEntryworklistForValidationtest  = response.data.listObject;
														
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular.forEach($scope.resultEntryworklistForValidationtest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.resultEntryworklistForValidation.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.resultEntryworklistForValidation  = response.data.listObject;
														}
														$scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
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
							
							$scope.generateResultValidationUrl = function()
							{
								var subId = 0;
								if($scope.selectedValEntryList.length>0)
								{
								$rootScope.startLoader();
								var isSameDept = false;
								if($scope.selectedValEntryList.length>1){
									var cnt =0;
									angular.forEach($scope.selectedValEntryList, function(value, key){
										
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
								 subId = $scope.selectedValEntryList[0].subDeptId;
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
										angular.forEach($scope.resultEntryworklistForValidation,function(value,indx){
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
								
							if($scope.selectedValEntryList[0].subDeptId != labConfig.serologyId){

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
								angular.forEach($scope.selectedValEntryList, function(value, key) 
										{
									value.orgId = value.orgId;
									value.orgUnitId = value.orgUnitId;
									value.createdBy = $scope.userId;
									value.listLabSampleResultDetailsMaster = [];
									patientIds.push(value.patientId);	
									
									count++;
									if(new Set(patientIds).size==1 && $scope.selectedValEntryList.length==count)
									{
									 flag =true;
									 $timeout(function () {
										 $rootScope.stopLoader();
										 $state.go('detailsWorklistForValidation',
													{
											 		  subDeptId : $scope.selectedValEntryList[0].subDeptId,
											 		 resultMstList : $scope.selectedValEntryList,
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
							}else if($scope.selectedValEntryList[0].subDeptId == labConfig.serologyId){
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
								var count = 0;
								 var patientIds = [];
								 var flag = false;
								angular.forEach($scope.selectedValEntryList, function(value, key) 
										{
									value.orgId = value.orgId;
									value.orgUnitId = value.orgUnitId;
									value.createdBy = $scope.userId;
									value.listLabSampleResultDetailsMaster = [];
									patientIds.push(value.patientId);	
									
									count++;
									if(new Set(patientIds).size==1 && $scope.selectedValEntryList.length==count)
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
										$state.go('detailsReportValidation',
												{
												  resultMstList : $scope.selectedValEntryList,
												  searchObj : searchObj,
                                                  nextRecordList : $scope.finalSeroDeptRecords
												});
								
									 }, 2000);
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
         /*====================================SEARCH DETAILS=====================================*/
							
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
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
							
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate:new Date()
							};
						    
							$scope.visitTypeId = 1;
							$scope.selectedVisitTypeIds = [1,2];
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
									//	"subDeptId":$scope.subDeptId,
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
									//	"subDeptId":$scope.subDeptId
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
									console.log('$scope.testList',$scope.testList);
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
						 $scope.searchOutPatientList = function()
							{
								try 
								{
									$rootScope.startLoader();
									$scope.selectedValEntryList.splice(0, $scope.selectedValEntryList.length);
									$scope.resultEntryworklistForValidation = [];
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						                          "patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
											      "labSampleDtlsId" : $scope.labSampleDtlsId==undefined ? null:$scope.labSampleDtlsId,
											      "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList == undefined?null :$scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
												  "assignUnit" :$scope.orgUnitId ,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage,
						                            "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false ,   
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
													"userId" : $scope.userId,
		                             }
									
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
									
									var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/consolidated/Validation/Search";
									
									GenericService.serviceAction(METHOD_POST, URI, searchObj).then(
											function(response) {
											$rootScope.stopLoader();
											$scope.resultEntryworklistForValidation = [];
											$scope.resultEntryworklistForValidationtest = [];
											$scope.resultEntryworklistForValidationtest  = response.data.listObject;
											
											if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
												angular.forEach($scope.resultEntryworklistForValidationtest, function(value, key) {
													angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
														if(value.subDeptId == value1.subSpecialityMasterId){
															$scope.resultEntryworklistForValidation.push(value);
														}
													
													});		
												 });
											}else{
												$scope.resultEntryworklistForValidation  = response.data.listObject;
											}
											$scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
											$scope.filterByDummyServices();
											$scope.commonListCount = response.data.object;
											$scope.setPage(1,false);
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							
						 
						 $scope.typeSearchForConsolidatedValidation = function(searchKeyword)
							{
							 if(searchKeyword.length >= 3){
								try 
								{
									$rootScope.startLoader();
									$scope.selectedValEntryList.splice(0, $scope.selectedValEntryList.length);
									$scope.resultEntryworklistForValidation = [];
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						                          "patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
											      "labSampleDtlsId" : $scope.labSampleDtlsId==undefined ? null:$scope.labSampleDtlsId,
											      "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList == undefined?null :$scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
												  "assignUnit" :$scope.orgUnitId ,
												  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
												  "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
												  "offset":$scope.offset,
												  "userId":$scope.userId,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "searchField":searchKeyword,
												  "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null
		                             }
									
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
									
									var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/consolidated/Validation/type/Search";
									
									GenericService.serviceAction(METHOD_POST, URI, searchObj).then(
											function(response) {
											$rootScope.stopLoader();
											$scope.resultEntryworklistForValidation  = response.data.listObject;
											$scope.commonListCount = response.data.object;
											$scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
											$scope.filterByDummyServices();
											$scope.setPage(1,false);
									});
								} catch (e) {
									console.log(e.message)
								}
							 }else  if(searchKeyword.length == 0){
								 $scope.searchOutPatientList();
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
							
							
							$scope.nextSerologyRecords = [];
							$scope.nextOtherDeptRecords = [];
							$scope.selectedIndex = [];
							 $scope.selectRecord = function(obj,index){
								 if(obj.statusFlag == true){
									 $scope.selectedIndex.push(index);
									 $scope.selectedValEntryList.push(obj);
									 
								 }else{
									 var selectedResEntryListTemp = [];
									 angular.forEach($scope.selectedValEntryList, function(value, key) 
												{
										 			if(value.labSampleDtlsId != obj.labSampleDtlsId){
										 				 selectedResEntryListTemp.push(value);
										 			}
												});
									 $scope.selectedValEntryList = selectedResEntryListTemp;
									 var idx = $scope.selectedIndex.findIndex(item => item ==  index);
									 $scope.selectedIndex.splice(idx,1);
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