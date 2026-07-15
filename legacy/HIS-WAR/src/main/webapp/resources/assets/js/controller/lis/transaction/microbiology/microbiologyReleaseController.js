'use strict';

/**
 * @Author By  Shital/Nitin
 * @name myApp.controller:microbiologyReleaseController
 * @description #microbiologyReleaseController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'microbiologyReleaseController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$filter',
						'$interval',
						'GenericServiceParamHeader',
						'$compile',
						'reportService',
						'growl',
						'PagerService',
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout, $filter,
								$interval, GenericServiceParamHeader,$compile,reportService,growl,PagerService,$window) {

							
							 /*** Check user is Loggedin */
								$rootScope.loginpage = true;
								
								var cookieObject,cookieObjectAppConfig;
								[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
								$scope.orgUnitId = cookieObject.unitId;
								$scope.orgId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.isRisPatientDetailsOnFirstPage = cookieObjectAppConfig.isRisPatientDetailsOnFirstPage;
								/*** Lab Configuration */
								var labConfig  =    $scope.checkLabConfig();
								$scope.deptId = labConfig.labSpecialityId;
								$scope.isAcceptanceInDepartment = labConfig.isAcceptanceInDepartment;
							    $scope.subDeptId = labConfig.microbiologyId;
							    $scope.getUnitByOrganization();
								$scope.assignUnit = $scope.orgUnitId;

								$scope.selectedAll = false;
								$scope.isReportRecall = 'N';
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
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 50;
								$scope.microbiologyWorklist = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								
								
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
    					        
								
								$scope.getRecollFlag = function(){
									try{
										var URI = BASE_URL + ROOT_URL +"/api/global/getUserById/"+$scope.userId+"/"+$scope.orgId+"/"+$scope.orgUnitId;
										console.log("EDIT",URI);
										GenericService.serviceAction("GET",URI).then(function(response){
											if(response.data.status == "success"){
												$scope.isrecoll= response.data.listObject[0].isRecoll;
												if($scope.isrecoll == true){
													$scope.isReportRecall = 'Y';
												}else{
													$scope.isReportRecall = 'N';
												}
											}
										});
										
									}catch(e){
										console.log("Exception-",e);
									}
								}
								
								$scope.getRecollFlag();
								
								$scope.reCall =  function(){
									
									 var obj = {}; 
	    							 var  i = 0 ;
	    							 angular.forEach($scope.microbiologyWorklist,function(resultEntryMaster,key) 
		  										{
	    								 			//if(i>=1){
	    								 				
	    								 				if(resultEntryMaster.printReport==true )
		  												{
		  													obj = resultEntryMaster;
		  													i++;
		  												}
	    								 			//}
		  											
		  										});
	    							 
	    							
									if(i == 0){
										growl.warning("Please Select Record",
												{
													title : 'warning!'
												});
										return false;
									}else if(i>1){
										growl.warning("Please Select Only One Record",
												{
													title : 'warning!'
												});
										return false;
									}else{

										cookieObjectAppConfigSecond.microBiologyReleaseObj = {};
										cookieObjectAppConfigSecond.microBiologyReleaseObj.fromDate = $scope.fromDate;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.toDate = $scope.toDate;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.patientName = $scope.patientName;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.patientId = $scope.patientId;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.selectedPatient = $scope.selectedPatient;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.uhid = $scope.uhid;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.selectedVisitType = $scope.selectedVisitType;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.selectedtestList = $scope.selectedtestList;
										cookieObjectAppConfigSecond.microBiologyReleaseObj.selectedSampleNo = $scope.selectedSampleNo;
										localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));

										 obj.reportType=2;
		    							 obj.isTempFinalRelease='Y';
		    							 obj.isResultEntry='N';
		    							 obj.isRecall = 'Y';
		 					       	 	$state.go("microbiologyReportEntryDetails",{
		 					       		microbiologyReportObject :obj
		 								}); 
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
							    							$scope.selectedVisitTypeIds = [1,2,3,4];
							    							$scope.sampleStatusIds=[SAMPLE_ACCEPTED];
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

							if(cookieObjectAppConfigSecond.microBiologyReleaseObj!=undefined && cookieObjectAppConfigSecond.microBiologyReleaseObj!=null){
								var searchObj = {};
								searchObj= cookieObjectAppConfigSecond.microBiologyReleaseObj;
								if(searchObj.fromDate!=undefined){
									$scope.fromDate = new Date(searchObj.fromDate);
								}

								if(searchObj.toDate!=undefined){
									$scope.toDate = new Date(searchObj.toDate);
								}

								if(searchObj.patientName!=undefined){
									$scope.patientName = searchObj.patientName;
								}

								if(searchObj.patientId!=undefined){
									$scope.patientId = searchObj.patientId;
								}

								if(searchObj.selectedPatient!=undefined){
									$scope.selectedPatient = searchObj.selectedPatient;
								}

								if(searchObj.uhid!=undefined){
									$scope.uhid = searchObj.uhid;
								}

								if(searchObj.selectedVisitType!=undefined){
									$scope.selectedVisitType = searchObj.selectedVisitType;
								}

								if(searchObj.selectedtestList!=undefined){
									$scope.selectedtestList = searchObj.selectedtestList;
								}

								if(searchObj.selectedSampleNo!=undefined){
									$scope.selectedSampleNo = searchObj.selectedSampleNo;
								}
								cookieObjectAppConfigSecond.microBiologyReleaseObj = {};
								localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));
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
							    						
							    						
							    						 $scope.selectSampleNo= function($item, $model,$label)
							    							{
							    						      $scope.selectedSampleNo = $item.sampleNo;
							    						      $scope.labSampleDtlsId = $item.labSampleDtlsId;
							    						    
							    					        }
							    						 $scope.setNoOfRecords = function() {
								 								$scope.searchMicroBiology($scope.orgId,
																		$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
								 							};
								 							
							    						 $scope.searchMicroBiology = function(orgId,
							 									orgUnitId,deptId,offset, noOfRecordsPerPage)
							    							{
							    								try 
							    								{
							    									$rootScope.startLoader();
							    									offset = offset != null ? offset : 0;
							    									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
							    											: 50;
							    									var searchObj = {
							    												  "orgId" : $scope.orgId,
							    												  "orgUnitId" :$scope.assignUnit,
							    												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											    								  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							    												  "patientId" :  $scope.selectedPatient,
							    												  "labSampleDtlsId" : $scope.labSampleDtlsId,
							    												  "visitTypes" :$scope.selectedVisitType,
							    												  "testTypes" : $scope.selectedtestList,
							    												  "deptId" : $scope.deptId,
							    												  "subDeptId" : $scope.subDeptId,
							    												  "subDepts" : $scope.selectedDepartMentList,
							    												  "assignUnit" :$scope.orgUnitId,
							    												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
							    												  "reportType" :4,
							    												  "offset" : $scope.offset,
							    												  "recordPerPage" : $scope.noOfRecordsPerPage
							    												
							    		                             }
							    									
							    									var URI = BASE_URL 
							    									        + ROOT_URL
							    									        + "/LIS/Microbiology/MicroTempletWorkbeanch/Search";
										    								console.log("URI", URI);
							    									console.log("URI", URI);
							    									 GenericService.serviceAction("POST", URI,
							    											 searchObj).then(
							    													 function(response) {
							    														 $scope.microbiologyWorklist=[];
							    															if (response.data.status == 'success') {
							    																$rootScope.stopLoader();
							    																console.log("resultEntryWorklist",JSON.stringify(response.data.listObject));
							    																//$scope.microbiologyWorklist =  response.data.listObject;
							    																
							    																
							    																if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
							    																		angular.forEach(response.data.listObject, function(value, key) {
								    																		angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
								    																			if(value.subDeptId == value1.subSpecialityMasterId){
								    																				value.printReport=false;
									    																			value.selectOnlyOneVisitType = true;
									    																			$scope.microbiologyWorklist.push(value);
								    																			}
								    																		
								    																		});		
								    																	 }); 
							    																 }else{
							    																		angular.forEach(response.data.listObject,function(value,key) 
									    																		{
									    																			value.printReport=false;
									    																			value.selectOnlyOneVisitType = true;
									    																			$scope.microbiologyWorklist.push(value);
									    																		});
							    																 }
							    																 
							    																$scope.microbiologyWorklistForFiltter = $scope.microbiologyWorklist;	
							    																$scope.filterByDummyServices();
							    																$scope.commonListCount = response.data.object> 0 ? response.data.object : 0 ;
							    																$scope.setPage(1, false);
							    															} else if (response.data.status == 'error') {
							    																growl
							    																		.error(
							    																				response.data.message,
							    																				{
							    																					title : 'Error!'
							    																				});
							    																$rootScope.stopLoader();
							    																$scope.microbiologyWorklist=[];
							    															}
							    														});
							    								} catch (e) {
							    									console.log(e.message)
							    								}
							    							}			
							    						 
							    						
							    						 $scope.searchMicroBiology($scope.orgId,
								 									$scope.orgUnitId,$scope.deptId,$scope.offset,
								 									$scope.noOfRecordsPerPage);
							    						 
							    						 
							    						 $scope.filterByDummyServices= function()
							                                {
							    								$scope.microbiologyWorklist  =[]
							                                    if($scope.filterDummyService==1)//all 
							                                    {
							                                    	$scope.microbiologyWorklist = angular.copy($scope.microbiologyWorklistForFiltter);
							                                    	
							                                    }else if($scope.filterDummyService==2)//real
							                                    {
							                                        angular.forEach($scope.microbiologyWorklistForFiltter, function (value, key) 
							                                        {
							                                            if(value.isDummyService==false)
							                                            	$scope.microbiologyWorklist.push(value);
							                                        });
							                                         
							                                        
							                                    }else if($scope.filterDummyService==3 )//dummy
							                                    {
							                                    	  angular.forEach($scope.microbiologyWorklistForFiltter, function (value, key) 
							                                                  {
							                                                      if(value.isDummyService==true)
							                                                    	  $scope.microbiologyWorklist.push(value);
							                                                  });
							                                                   
							                                    }
							                                }
											    		 /* clear fields*/
							    						 $scope.clearFields = function()
															{
															
															    
																$scope.patientId = undefined;
																$scope.selectedPatient = null;
																$scope.selectedVisitType = [];
																$scope.orderStatusId = '';
																$scope.priorityId = '';
																$scope.uhid = '';
																$scope.selectedSampleNo = '';
																$scope.selectedtestList = [];
																$scope.selectedDepartMentList = [];
																$scope.fromDate = moment().toDate();
															    $scope.toDate = moment().toDate();
															    $scope.searchMicroBiology($scope.orgId,
																		$scope.orgUnitId,$scope.deptId,$scope.offset,
																		$scope.noOfRecordsPerPage);
															};
							    						 
							    						 $scope.checkAll = function()
							 					        {
							 					        	if($scope.selectedAll)
							 					        		{
							 					        		var idex = 0;
							 					        		angular.forEach($scope.microbiologyWorklist,function(resultEntryMaster,key) 
							 											{
							 					        			$scope.microbiologyWorklist[idex].printReport=true;
							 					        			idex ++;
							 											});
							 					        		}else{
							 					        			var idex = 0;
							 						        		angular.forEach($scope.microbiologyWorklist,function(resultEntryMaster,key) 
							 												{
							 						        			$scope.microbiologyWorklist[idex].printReport=false;
							 						        			idex ++;
							 												});
							 						        		}
							 					        }
							    					
							    						 /*for check same patient having both visitType OP and IP HIS-17745*/
							 							
							 							$scope.checkAdmissionAndEncounterAvailableForPatient = function(info) {
							 								$scope.selectedVisitTypeId = info.visitType;
							 								$scope.selectedPatientId = info.patientId;
							 							    var countCheck = 0;
							 								angular.forEach($scope.microbiologyWorklist,function(value,key){
							 									if($scope.selectedPatientId == value.patientId){
							 											if($scope.selectedVisitTypeId != value.visitType){
							 												value.printReport = false;
							 												value.selectOnlyOneVisitType = false;
							 											}
							 									}
							 								});
							 								
							 								angular.forEach($scope.microbiologyWorklist,function(value,key){
							 									if($scope.selectedPatientId == value.patientId){
							 									if($scope.selectedVisitTypeId == value.visitType){
							 										if(value.printReport == true){
							 											countCheck++;
							 										}
							 									}
							 									}
							 								});
							 								angular.forEach($scope.microbiologyWorklist,function(value,key){
							 									if($scope.selectedPatientId == value.patientId){
							 										if($scope.selectedVisitTypeId != value.visitType){
							 										if(countCheck == 0){
							 											value.selectOnlyOneVisitType = true;
							 										}
							 										}
							 									}
							 								});
							 								console.log("list",$scope.resultReportHandOver);
							 							}
							 							
							    						  $scope.printReports= function(printFlag,printWithHeader) 
							  							{
							  					        	$scope.selectedList=[];					        	
							  					        	angular.forEach($scope.microbiologyWorklist,function(resultEntryMaster,key) 
							  										{
							  											if(resultEntryMaster.printReport==true)
							  												{
							  												 $scope.selectedList.push(resultEntryMaster.labSampleDtlsId);
							  												}
							  										});
							  					        	if($scope.selectedList.length==0){
							  					        		growl.warning('Please select service...!!',
							  											{
							  												title : 'Warning!'
							  											});
							  					        		return false;
							  					        	}
							  								
							  								try 
							  								{
							  									$rootScope.startLoader();
							  										angular.forEach($scope.microbiologyWorklist,function(resultEntryMaster,index) 
							  												{
							  													if(resultEntryMaster.printReport==true)
							  														{
							  														var data = {
													    									 labSampleDtlsId : resultEntryMaster.labSampleDtlsId,
													    									 orderStatusId : 41,
													    									 microbiologyTemplateId : resultEntryMaster.microbiologyTemplateId,
													    									 orgUnitId : $scope.unitId,
													    									 reportReleaseDate:resultEntryMaster.reportReleaseDate
													    									 };							  														 
							  														var URI = BASE_URL + ROOT_URL
																					+ "/LIS/Microbiology/updateLabDetailsStatus";
							  											        	    BillingGenericService.serviceAction("POST",URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							  												    			if (response.data.status == "success")
							  												    			{												    			
							  																	
							  												    				var newObj = {};
																					        	  newObj.organizationId = $scope.organizationId;
																								  newObj.unitId =  $scope.unitId;
																								  newObj.patientName = resultEntryMaster.patientName;
																								  newObj.orderDate = resultEntryMaster.orderDate;
																								  newObj.orderDtTime = resultEntryMaster.orderDtTime;
																								  newObj.prefixCode = resultEntryMaster.prefixCode;
																								  newObj.referralName = resultEntryMaster.referralName;
																								  newObj.email = resultEntryMaster.email;
																								  newObj.ageString = resultEntryMaster.ageString;
																								  newObj.genderCode = resultEntryMaster.genderCode;
																								  newObj.templateContent = resultEntryMaster.templateObject; 
																								  newObj.uhidNumber = resultEntryMaster.uhid;
																								  newObj.genderId = resultEntryMaster.genderId;
																								  newObj.labSampleNo = resultEntryMaster.labSampleNo;
																								  newObj.orderNo = resultEntryMaster.orderNo;
																								  newObj.visitNo = resultEntryMaster.visitNo;
																								  newObj.printWithHeader = printWithHeader;
																								  newObj.companyDesc = resultEntryMaster.companyDesc;
																								  newObj.comAddress = resultEntryMaster.comAddress;
																								  newObj.paymentEntitlementId = resultEntryMaster.paymentEntitlementId;
																								  newObj.isProvisionalRelease = 'N';
																								  newObj.isFinalRelease = 'Y';
																								  newObj.registerDate = resultEntryMaster.registerDate;
																								  newObj.unitName = cookieObject.unitNme;
																								  newObj.reportEntryDate = resultEntryMaster.reportEntryDate;
																								  newObj.reportValidationDate = resultEntryMaster.reportValidationDate;
																								  newObj.reportReleaseDate = resultEntryMaster.reportReleaseDate;
																								  newObj.sampleType = resultEntryMaster.sampleType;
																								  newObj.isReportRecall = resultEntryMaster.isReportRecall;
																								  newObj.labSampleDetailsId = resultEntryMaster.labSampleDtlsId;
																								  newObj.orderDetailsId = resultEntryMaster.orderDetailsId;
                                                                                                  newObj.validatedBy = resultEntryMaster.validatedBy;
                                                                                                  newObj.reportEntryBy = resultEntryMaster.reportEntryBy;
                                                                                                  newObj.tariffId = resultEntryMaster.tariffId;
                                                                                                  newObj.fromLisTemplate = true;
																								  
																								  if(resultEntryMaster.isHtmlPrint==true){
																								  var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTestLab";
																						    		BillingGenericService.serviceAction(METHOD_POST, URI, newObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
																						    				{
																						    	
																						    			   if(response.data.status=='success')
																						    			      {
																						    				   console.log("gg",response.data.object);
																						    				   $rootScope.stopLoader();
																						    				   
																						    				   var headerSize = "";
																						    					 var letterImgAndHeader="";
																						    					 var patientDetails = "";
																						    					 var completeTemplateContent = "";
																						    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
																						    						 //print patient details on all pages
																						    						 headerSize = "220px;";
																						    						 letterImgAndHeader = response.data.object.latterHeadImg+response.data.object.header;
																						    						 completeTemplateContent = response.data.object.templateContent+response.data.object.footer;
																						    					 }else{
																						    						 //print patient details on first page
																						    						 headerSize = "105px;";
																						    						 letterImgAndHeader = response.data.object.latterHeadImg;
																						    						 if(printWithHeader == 0){
																						    							 patientDetails = response.data.object.header;
																						    						 }else{
																						    							 patientDetails = response.data.object.labHeaderWithoutImg;
																						    						 }
																						    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
																						    					 }
																						    				   
																						    				   
																						    				   var printWindow = window.open('', '', 'height=500,width=700');  
																	                                           printWindow.document.write('<html><head></head>');  
																	                                           printWindow.document.write(' <style> '
																	                                                   +'.page-header, .page-header-space {height: '+headerSize+'} '
																	                                                   +' .page-footer, .page-footer-space { height: 105px;} '
																	                                                   + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
																	                                                   + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
																	                                                   + ' .page {page-break-after: auto;} '
																	                                                   + 'p { margin:0px 0px 0px 0px; }'
																	                                                   + '  @page { margin: 20mm}'
																	                                                   + ' </style>' );
																	                                           printWindow.document.write('<body>');
																	                                           if(printWithHeader == 0){
																	                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
																	                                                 printWindow.document.write(letterImgAndHeader);  
																	                                                 printWindow.document.write('</div>');
																	                                                 
																	                                                 printWindow.document.write('<div class="page-footer" style="text-align: center">');
																			                                         printWindow.document.write(response.data.object.footerImg);  
																			                                         printWindow.document.write('</div>');
																	                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
																	                                        	   //skip this part during first page bcoz we append patient details allready
																	                                                printWindow.document.write('<div class="page-header" style="text-align: center">');
																	                                                printWindow.document.write(response.data.object.labHeaderWithoutImg);  
																	                                                printWindow.document.write('</div>');
																	                                            }
																	                                           
																	                                           printWindow.document.write('<table> <thead> <tr><td><div class="page-header-space"></div> </td></tr> </thead>');
																	                                           printWindow.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
																	                                           printWindow.document.write(completeTemplateContent);  
																	                                           printWindow.document.write('</div> </td></tr></tbody>');
																	                                           printWindow.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');
																	                                           printWindow.document.write('</body>');
																	                                           printWindow.document.write('</html>');
																	                                           printWindow.document.close();  
																	                                           $timeout( function(){
																	                                                printWindow.print();  
																	                                           },2000);
																						    			      }
																						    			   else if(response.data.status=='error'){
																						    				   $rootScope.stopLoader();
																						    			   }
																						    		});
																								  }
																								  else
																									  {
																									  var data = {
																												"appName": "HIS",
																												"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																												"reportDesignFilePath": "Pathologytemplatereport.rpt",
																												"reportFormat": "PDF",
																												"exportFileName": "Pathologytemplatereport",
																												"reportParams": [
																												{
																												"paramName": "lab_sample_dtls_id",
																												"paramType": "Integer",
																												"paramValue": resultEntryMaster.labSampleDtlsId
																												},{
																												"paramName": "isOriginal",
																												"paramType": "Integer",
																												"paramValue": 1
																												},{
																												"paramName": "isHeader",
																												"paramType": "Integer",
																												"paramValue": printWithHeader
																												}
																												]
																												};
																												reportService.generate(REPORT_API_URI1,data);

																									  }
																						    		
																						    		
																								/*var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/lab/printReport";
																								$("#LabPrint").remove();
																							     var form = null;
																							     form = document.createElement("form");
																							     form.setAttribute("id", "LabPrint");
																							     form.setAttribute("name", "Lab Report");
																							     form.setAttribute("method", "post");
																							     form.setAttribute("action", URI);
																							     form.setAttribute("target", "_blank");
																							     var hiddenField1 = document.createElement("input"); 
																							     hiddenField1.setAttribute("type", "hidden");
																							     hiddenField1.setAttribute("name", "reportParam");
																							     hiddenField1.setAttribute("value", JSON.stringify(newObj));
																							     form.appendChild(hiddenField1);
																							     
																							     document.body.appendChild(form);
																							     $("form[name='Lab Report']").submit();*/
							  																}
							  												    		});
							  														
							  														}
							  												});
							  										$scope.searchMicroBiology($scope.offset,$scope.noOfRecordsPerPage);
							  										
							  									} catch (e) {
							  										$rootScope.stopLoader();
							  										console.log("Exception",e.message);
							  									}
							  								}
							    						 
							    							 $scope.patientName = "";
								    						 $scope.printReportsTemp = function() {
								    							 $scope.patientName = "";
								    							 var countFlag = false;
								    							 angular.forEach($scope.microbiologyWorklist,function(resultEntryMaster,key) 
									  										{
								    								 			var  i = 0 ;
									  											if(resultEntryMaster.printReport==true && resultEntryMaster.billSettled == false)
									  												{
									  												countFlag = true;
									  												if(i == 0){
									  													$scope.patientName = resultEntryMaster.patientName; 
									  													i = 1;
									  												}else{
									  													
									  													$scope.patientName = $scope.patientName+" ,"+resultEntryMaster.patientName; 
									  												}
									  												
									  												}
									  										});
								    							 
								    							 if(countFlag == true){
								    								 $("#resourceNotAvailable").modal("show");
								    								 
								    							 }else{
								    								 $('#printReport').modal('show');
								    								 
								    							 }
								    						 }
							    						  $scope.finalPrint = function(){
							    							  $('#printReport').modal('show');
							    						  }
							    						  
							    						  $scope.getMicroReportReleaseList = function(offset,noOfRecordsPerPage) {
																try {
																	$rootScope.startLoader();
																	var searchObj = {
																			"orgId" : $scope.orgId,
						    												  "orgUnitId" :$scope.assignUnit,
						    												  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
										    								  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						    												  "patientId" :  $scope.selectedPatient,
						    												  "labSampleDtlsId" : $scope.labSampleDtlsId,
						    												  "visitTypes" :$scope.selectedVisitType,
						    												  "testTypes" : $scope.selectedtestList,
						    												  "deptId" : $scope.deptId,
						    												  "subDeptId" : $scope.subDeptId,
						    												  "subDepts" : $scope.selectedDepartMentList,
						    												  "assignUnit" :$scope.orgUnitId,
						    												  "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
						    												  "reportType" :4,
						    												  "offset" : offset,
						    												  "recordPerPage" : noOfRecordsPerPage
						    		                             }
						    									
						    									var URI = BASE_URL 
						    									        + ROOT_URL
						    									        + "/LIS/Microbiology/MicroTempletWorkbeanch/Search";
									    								console.log("URI", URI);
						    									console.log("URI", URI);
						    									 GenericService.serviceAction("POST", URI,
						    											 searchObj).then(
						    													 function(response) {
						    														 $scope.sampleAccPendingList=[];
						    															if (response.data.status == 'success') {
						    																$rootScope.stopLoader();
						    																
						    																if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
						    																		angular.forEach(response.data.listObject, function(value, key) {
							    																		angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
							    																			if(value.subDeptId == value1.subSpecialityMasterId){
							    																				value.printReport=false;
								    																			value.selectOnlyOneVisitType = true;
								    																			$scope.microbiologyWorklist.push(value);
							    																			}
							    																		
							    																		});		
							    																	 }); 
						    																 }else{
						    																		angular.forEach(response.data.listObject,function(value,key) 
								    																		{
								    																			value.printReport=false;
								    																			value.selectOnlyOneVisitType = true;
								    																			$scope.microbiologyWorklist.push(value);
								    																		});
						    																 }
						    																$scope.microbiologyWorklistForFiltter = $scope.microbiologyWorklist;	
						    																$scope.filterByDummyServices();
						    															}
																					});
																} catch (e) {
																	console.log(e.message)
																}
															}
								 							
								 							$scope.pager = {};
															$scope.page;

															$scope.setPage = function(page, flag) {
																if (page < 1 ) {
																	return;
																}
																$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
																if (flag) {
																	$scope.getMicroReportReleaseList($scope.pager.startIndex,$scope.pager.pageSize);
																}
															}
															
															 $scope.getClassForColor = function(info)
											                    {
											                        	   if(info.priorityId==2){
									                                 		return 'td-bg-red';
											                        	   }
											                    }
															 
																$scope.patientDocumnetInformation = function(info) {
																	$scope.patientDocumentFlipBook ={};
																	$scope.patientDocumentFlipBook =info;
																	
																	
																	
																		var data =
																		{
																		"encounterId":$scope.patientDocumentFlipBook.encounterId,
																		"patientId":$scope.patientDocumentFlipBook.patientId,
																		"unitId":$scope.unitId,
																		"organisationId":$scope.organizationId,
																		"userId":$scope.userId
																		}
																		
																		
																	var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
																	BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																			$scope.patientDocumentListTest = [];
																			if (response.data.status == "success") 
																			{
																				$scope.patientDocumentListTest = response.data.listObject;
																				if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
																					$('#patientDocumentOption').modal('show');
																				}
																				$rootScope.stopLoader();
																			}
																			else if(response.data.status == "error")
																				{
																				$rootScope.stopLoader();
																				}
																		});
															
														          };
														          
														          $scope.convertDateToTimestamp = function(dateString) {
														                let parts = dateString.split("-");
														                let day = parseInt(parts[0], 10);
														                let month = parseInt(parts[1], 10) - 1; 
														                let year = parseInt(parts[2], 10);
														                let date = new Date(year, month, day);
														                let timestampInMilliseconds = date.getTime();
														                let timestampInSeconds = timestampInMilliseconds / 1000;
														                return timestampInSeconds;
														            };
														            
															     		$scope.generateFlipbookData = function() {
															                  let flipbookDatalist = [];

																     			var filteredlist=[];
																     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
																     		    	
																     		    	if(filterObj.documentPath != null){
																     				var flipbookDataobj={};
																     				 let createdDate = filterObj.createdDate;
																     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
																     				flipbookDataobj.timestamp=timestamp;
																     				flipbookDataobj.url=filterObj.documentPath;
																     				flipbookDataobj.tags=[];
																     				flipbookDataobj.tags.push("Document");
																     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
																     				flipbookDatalist.push(flipbookDataobj);
																     		    	}
																     				
																     			});

															                let data = {
															                    flipbookData: flipbookDatalist,
															                    logo: $scope.firstImgaa,
															                    privacy: "public"
															                };

															                let baseUrlLocation = $window.location.href;
															                let temp = baseUrlLocation.split('/');
															                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

															                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
															                
															        		try {
															        			var param={};
															        				$.ajax({
															        			        url : generateFlipbookurl,
															        					type : "POST",
															        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
															        					contentType : "application/json",
															        					dataType : "json",
															        					data : JSON.stringify(data),
															        			        //headers:getHeaders(),
															        					success : function(response){
															        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
															        							  window.open(''+response.flipbookUrl, '_blank');
															        						}else{
															        							$.notify("Error occurred","error");
															        						}
															        			    	}
															        			});
															        	     } catch (e) {
															        	 		console.log(e );
															        		}
															            };
															 
						} ]);