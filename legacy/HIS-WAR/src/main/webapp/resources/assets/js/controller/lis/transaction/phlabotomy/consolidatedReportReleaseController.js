
angular
		.module('myApp')
		.controller(
				"consolidatedReportReleaseController",
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
						function($scope, $rootScope, $state, $cookies,  $filter,
								GenericService, BillingGenericService,PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$timeout,$stateParams,reportService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "consolidatedReportRelease";
							$scope.thirdLevel = true;
							$scope.isRecollected = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultEntryworklistForValidation  = [];
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
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							$scope.labSortingId =  labConfig.labSortingId;
							
							//Multiple Report Release
							$scope.selectedRepReleaseEntryList  =[];
							$scope.reportReleaseBtn = false;
							$scope.saveBtn = false;
							$scope.isDisableGroupRelease = true;
							$scope.consolidatedResultEntry = labConfig.consolidatedResultEntry;
							$scope.listHelpValueMaster = [];
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
						            "subDeptId": 0,
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
							
							 
						    $scope.multiselectObj = {
						    		"id":"",
						    		"label":""
						    }
							
							$scope.departmentList=[];
							$scope.getDepartMentList = function(orgId,orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_DEPARMENT_LIST
											+ S + orgId + S + orgUnitId;
									console.log("URI", URI);
									$scope.departmentList=[];
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
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 50;
									$scope.selectedRepReleaseEntryList = [];
									$rootScope.startLoader();
									var data = {
									        "deptId":$scope.deptId,
											"orgId":orgId,
											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
											"offset":offset,
											"recordPerPage":noOfRecordsPerPage,
											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							                "patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
											"visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
											"subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
											"priorityId": $scope.priorityId == undefined ? null :$scope.priorityId ,
											"reportType": $scope.reportType == undefined ? null: $scope.reportType ,
											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
											"offset":$scope.offset,
											"recordPerPage":$scope.noOfRecordsPerPage,
											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
											"userId" : $scope.userId,
										};
									
									var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/ConsolidatedReleaseWorkList/Search";

									 GenericService.serviceAction(METHOD_POST, URI, data).then(
												function(response) {
													$scope.resultEntryworklistForValidation = [];
													$scope.resultEntryworklistForValidationTest = [];
													$rootScope.stopLoader();
													$scope.resultEntryworklistForValidationTest  = response.data.listObject;
													
													if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
														angular .forEach($scope.resultEntryworklistForValidationTest, function(value, key) {
															angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																if(value.subDeptId == value1.subSpecialityMasterId){
																	$scope.resultEntryworklistForValidation.push(value);
																}
															
															});		
														 });
													}else{
														$scope.resultEntryworklistForValidation  = response.data.listObject;
													}
													
													angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
														value.selectOnlyOneVisitType = true;
													});
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
										$scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
										$scope.reportType = $stateParams.searchObj.reportType ;
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
								$scope.selectedtestList = [];
								$scope.selectedDepartMentList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
							    $scope.reportType = '';
							    $scope.initResultEntryWorklistForValidation($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);

							};

							$scope.getBiochemWorkList = function(orgId,orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
                                      var data = {
                                    		"deptId":$scope.deptId,
  											"orgId":orgId,
  											"orgUnitId" :$scope.assignUnit,
											"assignUnit" :$scope.orgUnitId ,
  											"offset":offset,
  											"recordPerPage":noOfRecordsPerPage,
  											"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
  											"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
  											"patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
  											"visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
  											"subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
  											"priorityId": $scope.priorityId == undefined ? null :$scope.priorityId ,
  											"reportType": $scope.reportType == undefined ? null: $scope.reportType ,
  											"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
  											"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
  											"labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
  				                            "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
  				                            "userId" : $scope.userId,

										};
                                      var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/ConsolidatedReleaseWorkList/Search";

									GenericService.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.resultEntryworklistForValidation = [];
														$scope.resultEntryworklistForValidationTest = [];
														$rootScope.stopLoader();
														$scope.resultEntryworklistForValidationTest  = response.data.listObject;
														
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular .forEach($scope.resultEntryworklistForValidationTest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subDeptId == value1.subSpecialityMasterId){
																		$scope.resultEntryworklistForValidation.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.resultEntryworklistForValidation  = response.data.listObject;
														}
														angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
															value.selectOnlyOneVisitType = true;
														});
														
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
							
							$scope.generateUrlForReportRelease = function()
							{
								var subId = 0;
								if($scope.selectedRepReleaseEntryList.length>0)
								{
								$rootScope.startLoader();
								var isSameDept = false;
								if($scope.selectedRepReleaseEntryList.length>1){
									var cnt =0;
									angular.forEach($scope.selectedRepReleaseEntryList, function(value, key){
										
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
								subId = $scope.selectedRepReleaseEntryList[0].subDeptId;
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
							searchObj.selectedVisitType = $scope.selectedVisitType;
							searchObj.reportType = $scope.reportType;
							
							if($scope.selectedRepReleaseEntryList[0].subDeptId != labConfig.serologyId){

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
								angular.forEach($scope.selectedRepReleaseEntryList, function(value, key) 
										{
									value.orgId = value.orgId;
									value.orgUnitId = value.orgUnitId;
									value.createdBy = $scope.userId;
									value.listLabSampleResultDetailsMaster = [];
									patientIds.push(value.patientId);	
									
									count++;
									if(new Set(patientIds).size==1 && $scope.selectedRepReleaseEntryList.length==count)
									{
									 flag =true;
									 $timeout(function () {

										 $rootScope.stopLoader();
										 $state.go('detailsWorklistForReportRelease',
													{
											 		  subDeptId : $scope.selectedRepReleaseEntryList[0].subDeptId,
													  resultMstList : $scope.selectedRepReleaseEntryList,
													  backFlag : 3 ,
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
							}else if($scope.selectedRepReleaseEntryList[0].subDeptId == labConfig.serologyId){
								
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
								angular.forEach($scope.selectedRepReleaseEntryList, function(value, key) 
										{
									value.orgId = value.orgId;
									value.orgUnitId = value.orgUnitId;
									value.createdBy = $scope.userId;
									value.listLabSampleResultDetailsMaster = [];
									patientIds.push(value.patientId);	
									
									count++;
									if(new Set(patientIds).size==1 && $scope.selectedRepReleaseEntryList.length==count)
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
										$state.go('detailsReportRelease',
												{
												  backFlag : 3 ,
												  resultMstList : $scope.selectedRepReleaseEntryList,
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
									$scope.getBiochemWorkList($scope.orgId, $scope.orgUnitId,$scope.deptId,$scope.pager.startIndex,$scope.pager.pageSize);
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
													  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"), "patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
							                          "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
													  "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
													  "priorityId": $scope.priorityId == undefined ? null :$scope.priorityId ,
													  "reportType": $scope.reportType == undefined ? null: $scope.reportType ,
													  "deptId": $scope.deptId,
													  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
													  "offset":$scope.offset,
													  "recordPerPage":$scope.noOfRecordsPerPage,
	  				                                  "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false ,   
													  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
													  "userId" : $scope.userId,
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
										var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/ConsolidatedReleaseWorkList/Search";
								
								GenericService.serviceAction(METHOD_POST, URI, searchObj).then(
										function(response) {
											$scope.resultEntryworklistForValidation = [];
											$scope.resultEntryworklistForValidationTest = [];
											$rootScope.stopLoader();
											$scope.resultEntryworklistForValidationTest  = response.data.listObject;
											
											if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
												angular .forEach($scope.resultEntryworklistForValidationTest, function(value, key) {
													angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
														if(value.subDeptId == value1.subSpecialityMasterId){
															$scope.resultEntryworklistForValidation.push(value);
														}
													
													});		
												 });
											}else{
												$scope.resultEntryworklistForValidation  = response.data.listObject;
											}
											
											angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
												value.selectOnlyOneVisitType = true;
											});
											$scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
											$scope.filterByDummyServices();
											$scope.commonListCount = response.data.object;
											$scope.setPage(1,false);
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
									obj.sampleStatusId = false;
									$scope.selectedIndex.push(index);
									 $scope.selectedRepReleaseEntryList.push(obj);
									 
								 }else{
									 var selectedRepReleaseEntryListTemp = [];
									 angular.forEach($scope.selectedRepReleaseEntryList, function(value, key) 
												{
										 			if(value.labSampleDtlsId != obj.labSampleDtlsId){
										 				value.sampleStatusId = false;
										 				selectedRepReleaseEntryListTemp.push(value);
										 			}
										 
												});
									 $scope.selectedRepReleaseEntryList = selectedRepReleaseEntryListTemp;
									 var idx = $scope.selectedIndex.findIndex(item => item ==  index);
									 $scope.selectedIndex.splice(idx,1);
								 }
								
								if($scope.selectedRepReleaseEntryList.length>0){
									$scope.saveBtn = true;
								}else{
									$scope.saveBtn = false;
								}
							}
						
							
							// for type search
							
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
													  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"), "patientId" :  $scope.selectedPatient == undefined ? null: $scope.selectedPatient,
							                          "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
													  "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
													  "priorityId": $scope.priorityId == undefined ? null :$scope.priorityId ,
													  "reportType": $scope.reportType == undefined ? null: $scope.reportType ,
													  "deptId": $scope.deptId,
													  "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
													  "offset":$scope.offset,
													   "userId":$scope.userId,
													  "recordPerPage":$scope.noOfRecordsPerPage,
													  "searchField":searchKeyword,
													  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null
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
										var URI = BASE_URL  + ROOT_URL+ "/LIS/BioChemistry/ConsolidatedReleaseWorkList/type/Search";
								
								GenericService.serviceAction(METHOD_POST, URI, searchObj).then(
										function(response) {
											$scope.resultEntryworklistForValidation = [];
											$rootScope.stopLoader();
											$scope.resultEntryworklistForValidation  = response.data.listObject;
											angular.forEach($scope.resultEntryworklistForValidation,function(value,key){
												value.selectOnlyOneVisitType = true;
											});
											$scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
											$scope.filterByDummyServices();
											$scope.commonListCount = response.data.object;
											$scope.setPage(1,false);
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