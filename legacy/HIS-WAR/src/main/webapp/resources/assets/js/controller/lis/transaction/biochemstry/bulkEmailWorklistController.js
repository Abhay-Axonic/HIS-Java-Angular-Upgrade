/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"bulkEmailWorklistController",
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
						'reportService',
						'$timeout',
						'GenericServiceFileUpload',
						'$stateParams',
						'BillingGenericService',
						function($scope, $rootScope, $state, $cookies, $filter,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,reportService,$timeout,GenericServiceFileUpload,$stateParams,BillingGenericService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "bulkEmailWorklist";
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.resultEntryworklistForValidation  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
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
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.getUnitByOrganization();
							$scope.assignUnit = $scope.orgUnitId;
							$scope.deptId = labConfig.labSpecialityId;
							$scope.labSortingId =  labConfig.labSortingId;
							$scope.subDeptId = 0;
							$scope.isMailSend = "";
							$scope.insuranceId= "";
							$scope.companyId="";
							$scope.paymentEntitlementId="";
							$scope.paymentEntitlementList = [];
							$scope.getPaymentEntitlementList = function() {
								var data = "";
								var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
								GenericService.serviceAction("GET", URI, data).then(function(response){
									$scope.paymentEntitlementList= response.data.listObject;
								});
						   }
							$scope.getPaymentEntitlementList();
							
							$scope.selectedCompanyList = [];
							$scope.selectedassociateCompanyMasterList = [];
							$scope.companyMasterList = [];
							  $scope.getCompanyMasterList = function(companyTypeId) {
									  var param = {organizationId:$scope.orgId,unitId:$scope.orgUnitId,companyTypeId:companyTypeId};
										$rootScope.startLoader();
									    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
									    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success"){
												 $scope.companyMasterList = [];
												 $scope.selectedCompanyList = [];
												 $scope.selectedassociateCompanyMasterList = [];
												 if(response.data.listObject!=null && response.data.listObject.length>0){
													 angular.forEach(response.data.listObject,function(value,index){
														 var newArr = {};
														 newArr.id = value.id;
														 newArr.label = value.name;
														 $scope.companyMasterList.push(newArr);
													 });
												 }
											}
											$rootScope.stopLoader();
										});
									  }
								
								$scope.associateCompanyMasterList = [];
								$scope.getAssociateCompanyMasterList = function() {
									var companyIds = [];
									if($scope.selectedCompanyList!=null && $scope.selectedCompanyList!=undefined && $scope.selectedCompanyList.length>0){
										angular.forEach($scope.selectedCompanyList,function(val,idx){
											companyIds.push(val.id);
										});
									}
									if(companyIds.length>0 && $scope.paymentEntitlementId == 2){
										 var param = {organizationId:$scope.orgId,unitId:$scope.orgUnitId,companyIds:companyIds};
											$rootScope.startLoader();
										    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMasterByMultipleCompanyIds"
										    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success"){
													 $scope.associateCompanyMasterList = [];
													 $scope.selectedassociateCompanyMasterList = [];
													if(response.data.listObject!=null && response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(val,idx){
															var newArr = {};
															 var newArr = {};
															 newArr.id = val.id;
															 newArr.label = val.name;
															 $scope.associateCompanyMasterList.push(newArr);
														});
													}
												}
												$rootScope.stopLoader();
											});
									}
									 
								}
							$scope.getReportHandOverList = function(orgId,orgUnitId,deptId,offset,noOfRecordsPerPage) {
								try {
									$rootScope.startLoader();
									var ids = [];
									if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 2){
										//insurance
										if($scope.selectedassociateCompanyMasterList!=null && $scope.selectedassociateCompanyMasterList!=undefined && $scope.selectedassociateCompanyMasterList.length>0){
											angular.forEach($scope.selectedassociateCompanyMasterList,function(val,idx){
												ids.push(val.id);
											});
										}
										
									}else if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 3){
										//company
										if($scope.selectedCompanyList!=null && $scope.selectedCompanyList!=undefined && $scope.selectedCompanyList.length>0){
											angular.forEach($scope.selectedCompanyList,function(val,idx){
												ids.push(val.id);
											});
										}
									}
									var data = {
											  "orgId" :orgId,
											  "orgUnitId" :$scope.assignUnit,
											  "assignUnit" :$scope.orgUnitId ,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "patientId" :  $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" : $scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "subDeptId" : $scope.subDeptId,
											  "subDepts" : $scope.selectedDepartMentList,
											  "offset":offset,
											  "recordPerPage":noOfRecordsPerPage,
											  "isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
											  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
											  "ids" : ids		  
	                             };
								
								var URI = BASE_URL  + ROOT_URL+ '/LIS/ReportHandOver/bulkEmailWorklistPatientSearch';
									GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
														$rootScope.stopLoader();
														$scope.resultReportHandOver  = response.data.listObject;
														$scope.resultReportHandOverForFiltter  = $scope.resultReportHandOver;
														$scope.filterByDummyServices();
									});
									
								} catch (e) {
									console.log(e.message)
								}

							}
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
								} else {
									$scope.selectedAll = false;
								}
								angular.forEach($scope.resultReportHandOver,
												function(resultEntry)
												{
									              resultEntry.printReport = $scope.selectedAll;
												});
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
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
							    $scope.isMailSend = "";
							    $scope.insuranceId= "";
								$scope.companyId="";
								$scope.paymentEntitlementId="";
								$scope.selectedCompanyList=[];
								$scope.selectedassociateCompanyMasterList=[];
							};
							
							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1 )
								{
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getReportHandOverList(
											$scope.orgId, $scope.orgUnitId,$scope.deptId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							
							$scope.filterByDummyServices= function()
                            {
								$scope.resultReportHandOver  =[]
                                if($scope.filterDummyService==1)//all 
                                {
                                	$scope.resultReportHandOver = angular.copy($scope.resultReportHandOverForFiltter);
                                	
                                }else if($scope.filterDummyService==2)//real
                                {
                                    angular.forEach($scope.resultReportHandOverForFiltter, function (value, key) 
                                    {
                                        if(value.isDummyService==false)
                                        	$scope.resultReportHandOver.push(value);
                                    });
                                     
                                    
                                }else if($scope.filterDummyService==3 )//dummy
                                {
                                	  angular.forEach($scope.resultReportHandOverForFiltter, function (value, key) 
                                              {
                                                  if(value.isDummyService==true)
                                                	  $scope.resultReportHandOver.push(value);
                                              });
                                               
                                }
                            }
							
	/*====================================SEARCH DETAILS=====================================*/
							
							$scope.sampleStatusIds=[REPORT_RELEASED];
							$scope.selectedVisitTypeIds = [1,2];
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
                          /**DatePicker Code  */
							
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
							
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
						
						 $scope.searchPatientList = function()
							{
								try {
									
									if($scope.paymentEntitlementId == undefined || $scope.paymentEntitlementId == null || $scope.paymentEntitlementId == ""){
										growl.warning('Please Select Payment Entitlement',{
													title : 'warning'
												});
										return false;
									}
									if($scope.paymentEntitlementId != undefined && $scope.paymentEntitlementId != null || $scope.paymentEntitlementId != ""){
										
										if( $scope.paymentEntitlementId == 2 && ($scope.selectedassociateCompanyMasterList == null || $scope.selectedassociateCompanyMasterList == undefined 
												|| $scope.selectedassociateCompanyMasterList.length == 0)){
											growl.warning('Please Select Insurance',{
												title : 'warning'
											});
											return false;
										}
										
										if( $scope.paymentEntitlementId == 3 && ($scope.selectedCompanyList == null || $scope.selectedCompanyList == undefined 
												|| $scope.selectedCompanyList.length == 0)){
											growl.warning('Please Select Company',{
												title : 'warning'
											});
											return false;
										}
									}
									
								var ids = [];
								if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 2){
									//insurance
									if($scope.selectedassociateCompanyMasterList!=null && $scope.selectedassociateCompanyMasterList!=undefined && $scope.selectedassociateCompanyMasterList.length>0){
										angular.forEach($scope.selectedassociateCompanyMasterList,function(val,idx){
											ids.push(val);
										});
									}
									
								}else if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 3){
									//company
									if($scope.selectedCompanyList!=null && $scope.selectedCompanyList!=undefined && $scope.selectedCompanyList.length>0){
										angular.forEach($scope.selectedCompanyList,function(val,idx){
											ids.push(val);
										});
									}
								}
									$rootScope.startLoader();
									var searchObj = {
												  "orgId" : $scope.orgId,
												  "orgUnitId" :$scope.assignUnit,
												  "assignUnit" :$scope.orgUnitId ,
												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
												  "patientId" :  $scope.selectedPatient,
												  "labSampleDtlsId" : $scope.labSampleDtlsId,
												  "visitTypes" :$scope.selectedVisitType,
												  "testTypes" : $scope.selectedtestList,
												  "deptId" : $scope.deptId,
												  "subDeptId" : $scope.subDeptId,
												  "subDepts" : $scope.selectedDepartMentList,
												  "offset":$scope.offset,
												  "recordPerPage":$scope.noOfRecordsPerPage,
												  "isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
												  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	,
												  "ids" : ids		,
												  "paymentEntitlementId":$scope.paymentEntitlementId
		                             };
									
									var URI = BASE_URL  + ROOT_URL+ '/LIS/ReportHandOver/bulkEmailWorklistPatientSearch'
									GenericService.serviceAction(METHOD_POST, URI, searchObj).then(function(response){
										$rootScope.stopLoader();
										$scope.biochemWorklist=[];
										$scope.resultReportHandOver=[];
										$scope.resultReportHandOver = response.data.listObject;
										$scope.resultReportHandOverForFiltter  = $scope.resultReportHandOver;
										$scope.filterByDummyServices();
										$scope.commonListCount = response.data.object;
										$scope.setPage(1,false);
									});		
							
								} catch (e) {
									console.log(e.message)
								}
							}

								$scope.getClassForColor = function(info)
								{
									if(info.mailSend){
										return 'td-bg-green';
									}/*else 
									{
										return 'td-bg-yellow';
									}*/
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
									
									$scope.setNoOfRecords = function() {
										$scope.searchPatientList();
									};
									
									$scope.payeeList = [];
									$scope.sendEmail= function()	{
										$rootScope.startLoader();
										$scope.payeeList = [];
										var groupBy = function(xs, key) {
											  return xs.reduce(function(rv, x) {
											    (rv[x[key]] = rv[x[key]] || []).push(x);
											    return rv;
											  }, {});
											};

										   var checkList = [];	
											angular
											.forEach(
													$scope.resultReportHandOver,
													function(resultEntryMaster,key) 
													{
														if(resultEntryMaster.printReport==true)
															{
															  checkList.push(resultEntryMaster);
															}
													});
											if(checkList.length==0)
												{
												growl
												.warning(
														"Please select patient to send Mail.",
														{
															title : 'Warning!'
														});
												$rootScope.stopLoader();
												 return false;
												}
		 									else {

											$scope.finalPatientList = [];
											$scope.payeeList = groupBy(checkList,
													'payeeId');
											if ($scope.payeeList != null) {
												angular.forEach($scope.payeeList,function(value, key) {
													var newArr = {};
													var pList = [];
													newArr.payeeId = key;
													if(value!=undefined && value!=null && value.length>0){
														angular.forEach(value,function(val,idx){
															if( pList.length==0){
																var newData = {};
																newData.labSampleDtlsList = [];
																newData.labSampleDtlsList.push(val.labSampleDtlsId);
																newData.patientDetails = val.patientDetails;
																newData.patientId = val.patientId;
																newData.uhid = val.uhid;
																newData.testDesc =val.testDesc;
																newData.orderDate = val.orderDateTime;
																pList.push(newData);
															}else{
																var found = pList.find(obj => {return obj.patientId == val.patientId});
																if(found!=undefined){
																	found.labSampleDtlsList.push(val.labSampleDtlsId);
																	found.testDesc =  found.testDesc +","+ val.testDesc ;
																}else{
																	var newData = {};
																	newData.labSampleDtlsList = [];
																	newData.labSampleDtlsList.push(val.labSampleDtlsId);
																	newData.patientDetails = val.patientDetails;
																	newData.patientId = val.patientId;
																	newData.uhid = val.uhid;
																	newData.testDesc =val.testDesc;
																	newData.orderDate = val.orderDateTime;
																	pList.push(newData);
																}
															}
														});
													}
													newArr.orgId  = $scope.orgId;
													newArr.unitId = $scope.orgUnitId ;	
													newArr.patientList = pList;
													newArr.paymentEntitlementId = $scope.paymentEntitlementId;
													$scope.finalPatientList.push(newArr);
												});
												
												if($scope.finalPatientList!=null && $scope.finalPatientList.length>0){
													var URI = BASE_URL  + ROOT_URL+ '/LIS/ReportHandOver/sendBulkEmailToCompanyOrInsurance';
													GenericService.serviceAction(METHOD_POST, URI, $scope.finalPatientList).then(function(response) {
														$rootScope.stopLoader();
														if (response.data.status == "success"){
															growl.success(response.data.message,
																	{
																		title : 'success!'
																	});
															$scope.selectedAll = false;
															$scope.searchPatientList();
														}else if(response.data.status == "error"){
															growl.warning(response.data.message,
																	{
																		title : 'warning!'
																	});
															$scope.selectedAll = false;
															$scope.searchPatientList();
														}
													});
												}
											}

										}
									}
								 
						}]);
