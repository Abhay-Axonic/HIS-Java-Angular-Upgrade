
/**
 * @author Ganesh
 */

angular
		.module('myApp')
		.controller(
				"specimenReceiptController",
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
							$scope.LISDynamicLabel = "Specimen Receipt";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 50;
							$scope.specimenReceiptList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.isPreOrPostBilling   =cookieObjectAppConfig.isPreOrPostBilling;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
						    $scope.subDeptId = labConfig.histopathologyId;
							
							
							$scope.selectedVisitTypeIds = [1,2];
							$scope.sampleStatusIds=[SEND_TO_DEPT];
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							$scope.specimanTypeList =[];
							
							$scope.selectedspecimanTypeList = [];
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];

							$scope.saveBtn = false;
							
							
		/**DatePicker Code  */
							
							$scope.fromDate = moment().startOf('day').toDate();
						    $scope.toDate = moment().startOf('day').toDate();
						    $scope.completed='N';
							$scope.pending = 'N';
							$scope.outsourced = 'N';
							
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

							
							

							$scope.listSpecimanReceiptDtls = [];
							$scope.labSpecimanReceiptDtls = {
								"deptName" : "",
								"specimanType" :"",
								"specimanId" : "",
								"visitType" : "",
								"orderId" : "",
								"orderDetailsId" : "",
								"packageId" : "",
								"deptId" : $scope.deptId,
								"subDeptId" :$scope.subDeptId,
								"testId" : "",
								"testDesc" : "",
								"sampleId" : "",
								"containerId" : "",
								"isOutsourced" : "",
								"genderId" : "",
								"orderStatusId" : "",
								"orgId" :  $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"visitTypeId" : "",
								"doctorId" : "",
								"patientId" : "",
								"visitAdmId" : "",
								"encounterId":"",
								"uhid" : "",
								"patientDetails" : "",
								"doctorDetails" : "",
								"priorityId" : "",
								"priorityName" : "",
								"colorCode" : "",
								"createdBy" :$scope.userId,
								"isAccpted" :"",
								"wardCode":"",
								"bedNumber":""
							}

							

							$scope.setNoOfRecords = function() {
								$scope.initSpecimenReceiptList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initSpecimenReceiptList = function(orgId,
									orgUnitId,deptId,subDeptId, offset, noOfRecordsPerPage)
								{
								try {
									$rootScope.startLoader();
									var searchObj = {
											
											  "orgId" : $scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "fromDate": $scope.fromDate.getTime() ,
			                            	  "toDate":  $scope.toDate.getTime() ,
											  "patientId" : $scope.selectedPatient,
											  "labSampleDtlsId" : $scope.labSampleDtlsId,
											  "visitTypes" :$scope.selectedVisitType,
											  "testTypes" :$scope.selectedtestList,
											  "deptId" : $scope.deptId,
											  "specimanTypes": $scope.selectedspecimanTypeList,
											  "subDepts" :$scope.selectedDepartMentList,
											  "offset":offset,
											  "recordPerPage":noOfRecordsPerPage,
											  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
											  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
													 
	                                  }
									var isValidDate = $rootScope.isValidDate(searchObj.fromDate,searchObj.toDate);
									if(!isValidDate){
										$rootScope.stopLoader();
										growl.warning("From date can not be greater than to Date ",
												{
													title : 'warning!'
												});
										return false;
									}
									
									var URI1 = BASE_URL 
									        + ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTOPATHOLOGY_SPECIMAN_RECEIPT_LIST+S+$scope.isPreOrPostBilling;

									var URI2 = BASE_URL
											+ ROOT_URL
											+ LIS_HISTOPATHOLOGY
											+ HISTOPATHOLOGY_SPECIMAN_RECEIPT_COUNT+S+$scope.isPreOrPostBilling;

									promiseFactoryWithObject
											.setPromisesWithObject(URI1, URI2, METHOD_POST,
													METHOD_POST,searchObj,searchObj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.specimenReceiptList = response[0].listObject;
														$scope.commonListCount = response[1].object;
														$scope.setPage(1, false);
													});
									
								} catch (e) {
									console.log(e.message);
								}
							}

							$scope.initSpecimenReceiptList($scope.orgId,
									$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
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
								$scope.selectedspecimanTypeList = [];
								$scope.fromDate = moment().toDate();
							    $scope.toDate = moment().toDate();
								$scope.initSpecimenReceiptList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
										$scope.noOfRecordsPerPage);

							};
           

							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								if (page < 1)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								if (flag) 
								{
									$scope.initSpecimenReceiptList($scope.orgId, $scope.orgUnitId,$scope.deptId, $scope.subDeptId,$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							 /** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									 $scope.saveBtn = true;
								} else {
									$scope.selectedAll = false;
									 $scope.saveBtn = false;
								}
								angular
										.forEach(
												$scope.specimenReceiptList,
												function(specimanObj) {
													specimanObj.isAccpted = $scope.selectedAll
												});
							 }

							 // code for Activating Inactivating Sample
							$scope.acceptSpecimen = function() 
							{
								
								$scope.listSpecimanReceiptDtls = [];
								$rootScope.startLoader();
								//var saveFlag = false;
								angular.forEach($scope.specimenReceiptList,function(value, key) 
								{
													if (value.isAccpted == true) 
													{
														
														value.createdBy = $scope.userId;
														value.createdDate = moment().toDate().getTime();
														value.orgId = $scope.orgId;
														value.orgUnitId = $scope.orgUnitId;
														value.deptId = $scope.deptId;
														value.updatedBy = $scope.userId;
														value.updatedDate = moment().toDate().getTime();
														$scope.listSpecimanReceiptDtls
																.push(value);
													}
													/*if(value.specimanName == null || value.specimanName == ""){
														saveFlag = true;
													}		*/		
									
								});
								
//                                if($scope.listSpecimanReceiptDtls.length>0){
//                                    var found = $scope.listSpecimanReceiptDtls.find(ar=>ar.isAccpted == true && (ar.specimanName==null||ar.specimanName==''));
//                                       if(found!=undefined)
//                                           {
//                                           growl.warning("please enter specimen name ",
//                                                    {
//                                                        title : 'Warning!'
//                                                    });
//                                            $rootScope.stopLoader();
//                                            return false;
//                                           }
//                                }

								try {
									//if(saveFlag == false){
										
										console.log("$scope.listSpecimanReceiptDtls",JSON.stringify($scope.listSpecimanReceiptDtls));
										var URI = BASE_URL 
												+ ROOT_URL
												+ LIS_HISTOPATHOLOGY
												+ HISTOPATHOLOGY_SPECIMEN_COLLECTION;
										console.log("ACCEPT SPECIMAN", URI);
										GenericService
												.serviceAction(METHOD_POST, URI,
														$scope.listSpecimanReceiptDtls)
												.then(
														function(response) {
															if (response.data.status == 'success') 
															{
																$scope.initSpecimenReceiptList($scope.orgId,
																		$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
																		$scope.noOfRecordsPerPage);
																
																
																growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
																$scope.saveBtn = false;
																$scope.$broadcast('show-errors-reset');

															} else if (response.data.status == 'error') 
															{
																$rootScope.stopLoader();
																growl.error(response.data.message,
																{
																	title : 'Error!'
																});
															}
														});
										
								/*	}else{
										growl.warning("please enter speciman name ",
												{
													title : 'Warning!'
												});
										$rootScope.stopLoader();
									}*/
								
								} catch (e) {
									console.log("Exception", e.message);
								}
							}
							
							
							
							
							


														$scope.patHistory = "";
														$scope.tempEncounterId = "";
														$scope.getPatHis = function(item){
															$scope.tempEncounterId = item.encounterId;
															try{
																$rootScope.startLoader();
																
																var data = {
																		"encounterId" : item.encounterId
																};
																var URI = BASE_URL + ROOT_URL
																+"/LIS/transaction/getPatHistoryByEncounterId";
														GenericService.serviceAction("POST", URI, data)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				$scope.patHistory = response.data.object.patHistory;
																				$rootScope.stopLoader();
																			}
																			$rootScope.stopLoader();
																		});
															}catch(e){
																console.log("Exception-",e);
																$rootScope.stopLoader();
															}
															
														}
														
														
														$scope.getData = function(){
															var data = {
																	"encounterId" : $scope.tempEncounterId,
																	"patHistory" : $scope.patHistory 
															};
														}
														
														
														$scope.updateHistory = function(){
															try{
																$rootScope.startLoader();
																
																var data = {
																		"encounterId" : $scope.tempEncounterId,
																		"patHistory" : $scope.patHistory 
																};
																var URI = BASE_URL + ROOT_URL
																+"/LIS/transaction/updatePatHistoryByEncounterId";
														GenericService.serviceAction("POST", URI, data)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				
																				growl.success("Patient History Updated Successfully",
																						{
																							title : 'Success!'
																						});
																				$rootScope.stopLoader();
																			}
																			$rootScope.stopLoader();
																		});
															}catch(e){
																console.log("Exception-",e);
																$rootScope.stopLoader();
															}
															
														}
							
							
							
	/*====================================SEARCH DETAILS=====================================*/
							
							$scope.departmentList= [];
							$scope.visityTypeList = [];
							$scope.testList = [];
							$scope.selectedVisitType = [];
							$scope.selectedtestList = [];
							$scope.selectedDepartMentList = [];
							$scope.selectedspecimanTypeList = [];
							
						    $scope.dropDownSetting = {
						    		scrollableHeight: '100px', 
						    		scrollable: true
						    }; 
						    
						    $scope.multiselectObj = {
						    		"id":"",
						    		"label":""
						    }
						    
						/** Service Autocomplete List * */
						$scope.patientSearch = function(searchKeyword)
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
						    $scope.patientBlank = function()
							{
						      if($scope.patientId==undefined)
						    	  {
						    	    $scope.selectedPatient =null;
						    	  }
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
						
						$scope.getSpecimanList = function()
						{
							try 
							{
								var data = {
										"orgId":$scope.orgId,
										"orgUnitId":$scope.orgUnitId,
										"deptId":$scope.deptId,
										"subDeptId":$scope.subDeptId,
										"searchKeyword":"",
								}
								
								var URI = BASE_URL 
								        + ROOT_URL
										+ LIS_HISTOPATHOLOGY
										+ HISTO_MICRO_SPECIMAN_LIST
								console.log("URI", URI);
								 GenericService.serviceAction("POST", URI,
										data).then(function(response) 
								{
									console.log("response.data.listObject",response.data.listObject);
								  $scope.specimanTypeList =  response.data.listObject;
								  console.log("specimanTypeList",JSON.stringify($scope.specimanTypeList));
								});
							} catch (e) {
								console.log(e.message)
							}
						}
						$scope.getSpecimanList();
						
						
						
						
                   $scope.searchSpecimanReciept = function()
                   {
                	   $scope.initSpecimenReceiptList($scope.orgId,
								$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
								$scope.noOfRecordsPerPage);
                   }
                   
                   $scope.selectPatientForSave = function(status){
                	   
                	   var checkedCount = 0;
                       angular.forEach($scope.specimenReceiptList, function(value, key) {
                      
                           if(value.isAccpted == true){
                                checkedCount = checkedCount + 1;
                           }
                      });
                       
                       if(checkedCount > 0){
                           $scope.saveBtn = true;
                       }else{
                           $scope.saveBtn = false;
                       }
                   }

						} ]);