/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"deatilsReportEntryWorklistController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'$stateParams',
						'subDeptId',
						'$filter',
						'CONSTANTS',
						'GenericServiceFileUpload',
						'$timeout',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams,subDeptId,$filter,CONSTANTS,GenericServiceFileUpload,$timeout,reportService) {

							
							$scope.isRecollected = false;
							$rootScope.loginpage = true;
							$scope.thirdLevel = false;
								
								$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.resultEntryWorklist  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.labResultTestDtls;
							$scope.override = false;
							$scope.saveOrNextButton = false;
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.storeId = cookieObject.storeId
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = subDeptId;
							$scope.isValidationAndRelease = labConfig.isValidationAndRelease;
							$scope.skipValidation= labConfig.skipValidation;
							$scope.consolidatedResultEntry =labConfig.consolidatedResultEntry;
							$scope.reportReleasePin = labConfig.reportReleasePin;
							$scope.triggerReleaseMail = labConfig.triggerReleaseMail;
							$scope.sendMailOnOrderCompletion = labConfig.sendMailOnOrderCompletion;
							$scope.listHelpValueMaster = [];
							
							$('[data-toggle="deltatooltip"]').tooltip();
							
							$scope.itemList = [];
							  $scope.getInventoryItemList = function(serviceMasterIds){
								  $rootScope.startLoader();
								  var data = {
										  serviceIds : serviceMasterIds,
											organizationId : $scope.orgId,
											unitId : $scope.orgUnitId,
											storeId : $scope.storeId
										};
										var URI = BASE_URL + ROOT_URL + "/billing/resultEntry/getServiceItemMapperDetailsByServiceId";
								  GenericService.serviceAction("POST", URI, data).then(function(response) {
												$scope.itemList = [];
												 $rootScope.stopLoader();
												if (response.data.status == "success") {
                                                    if(response.data.listObject != null && response.data.listObject.length>0){
                                                        angular.forEach(response.data.listObject,function(value1,index1) {
                                                              
                                                        	  
															if(value1.batchReq == true){
																 $scope.itemList.push(value1);
																 value1.batchReq = value1.batchReq;
																 value1.consumedQuantity = value1.assignQty;
																 value1.newAssignQty = value1.assignQty;
															}else{
																value1.batchReq = value1.batchReq;
																value1.selectedBatch = {};
																value1.selectedBatch.batchId = value1.batchList[0].batchId;
																value1.selectedUom = {};
																value1.selectedUom.uomTypeId = value1.batchList[0].uom[2].uomTypeId;
																value1.selectedUom.uomUnitId = value1.batchList[0].uom[2].uomUnitId;
																value1.availableQuantity = value1.batchList[0].availableStockQuantity;
																value1.consumedQuantity = value1.assignQty;
																value1.newAssignQty = value1.assignQty;
																$scope.itemList.push(value1);
															}
                                                        });
                                                    }
												}
											});
							  }
							
							  //get StoreList 
						      $scope.storeList = [];
						      $scope.storeId = cookieObject.storeId;
						      $scope.getStoreList = function(){
									 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.orgId+"/"+$scope.orgUnitId;
							            $rootScope.startLoader();
							            GenericService.serviceAction("GET", URI, {}).then(
							                function (response) {
							                    $scope.storeList = response.data.listObject;
							                    $rootScope.stopLoader();
							                	console.log("$scope.store",$scope.store);
												console.log("$scope.storeList",$scope.storeList);
							                });
								}
							  
							$scope.serviceIds = []; 
							$scope.setFirstTabLevel= function(newTab)
							{ 
								$scope.firstTabLevel = newTab;
								$scope.getParameterHistory();
								if(newTab==2){
									if($scope.testMasterResList.length>0){
										$scope.serviceIds = []; 
									angular.forEach($scope.testMasterResList,function(value,index){
										$scope.serviceIds.push(value.serviceId);
									});
									if($scope.itemList ==null || $scope.itemList.length == 0){
										$scope.getInventoryItemList($scope.serviceIds);
										$scope.getStoreList();
									}
								}
								}
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
						            "internalNotes": "",
						            "srfId": "",
						            "icmrId": "",
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
						            "subDeptId": $scope.subDeptId,
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
                                    "isOnHoldParameter":false,
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
									"refrangeTypeId":"",
									"textualRangeId":"",
									"multitextaulRange":"",
									"textualRangeName":"",
									"paramPrintOrder": "",
									"machineId": "",
									"machineResult": "",
									"remarks": "",
									"isDeltaFlag": "",
									"infromationDtls": "",
									"informationType": "",
									"informedTo": "",
									"informedDatetime": "",
									"paramRecheckedBy": "",
									"parameterFormula": "",
									"isFormula":"",
									"paramPrecision":0,
									"deltaPer" :"",
									"deltaChange":"",
									"deltaDaysId":"",
									"createdBy": $scope.userId,
									"updatedBy": $scope.userId,
									"updatedDate": ""
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
							
							
							$scope.setNoOfRecords = function() {
								$scope.initResultEntryList($scope.orgId,
										$scope.orgUnitId,$scope.deptId,$scope.offset,
										$scope.noOfRecordsPerPage);
							};
							
							
							
							//================getTest and paramter by patient
							
							 $scope.patientParameter =[];
							$scope.getPriviousTestParameter = function(testId)
						       {
						        try 
						        {
						         var data = {
						           "patientId":$scope.patientId,
						           "testId":testId,
						         }
						         var URI = BASE_URL 
						                 + ROOT_URL
						                 + LIS_BIOCHEMISTRY
						                 + PATIENT_TEST_PARAMETER;
						            console.log("URI", URI);
						            GenericService.serviceAction("POST", URI, data).then(function(response) 
						            {
						            	if(response.data.status=="success")
					            		{
						                 $scope.patientParameter =  response.data.listObject;
					            		}
						            	else if(response.data.status=="error")
					            		{
					            		
					            		}
						            });
						        } catch (e) {
						         console.log(e.message)
						        }
						       }
							
								$scope.getPriviousTest = function()
							       {
							        try 
							        {
							        	$rootScope.startLoader();
							         var data = {
							           "patientId":$scope.patientId,
							         }
							         var URI = BASE_URL 
							                 + ROOT_URL
							                 + LIS_BIOCHEMISTRY
							                 + GET_PATIENT_PREVIOUS_TEST
							            GenericService.serviceAction("POST", URI, data).then(function(response) 
							            {
							            	if(response.data.status=="success")
							            		{
							            		  $scope.testListPatient =  response.data.listObject;
							            		  if($scope.testListPatient!=null && $scope.testListPatient.length>0)
							            			  {
							            			    $("#historyDetails").modal('show');
							            				$rootScope.stopLoader();
							            			  }
							            		  else{
							            			  $rootScope.stopLoader();
							            			  growl.warning("No history found for this patient..",
																{
																	title : 'Error!'
																});
							            		     }
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
							
								//================getTest and paramter by patient
							
//								$scope.sampleRecollectionFlag=true;
								 $scope.getSampleRecollectionFlag=function()
								 {
									 var data = {
											  "orgId":$scope.orgId,
											  "orgUnitId" :$scope.orgUnitId,
											  "userId" : $scope.userId

									}
									
									var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/resultEntry/sampleRecollection";
									GenericService.serviceAction(METHOD_POST, URI, data).then(
											function(response) {
												$rootScope.stopLoader();
													$scope.sampleRecollectionFlag=response.data.listObject[0].sampleRecollection;
											});
								 }
								 $scope.getSampleRecollectionFlag();
							
							$scope.getTestList = function()
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
						                 + HISTO_MICRO_TEST_LIST
						            console.log("URI", URI);
						            GenericService.serviceAction("POST", URI, data).then(function(response) 
						            {
						               console.log("response.data.listObject",response.data.listObject);
						               $scope.testList =  response.data.listObject;
						         });
						        } catch (e) {
						         console.log(e.message)
						        }
						       }
						       $scope.getTestList();
						       
						      
						       $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    };  
						
						       $scope.parameterList = [];
						       $scope.selectedParameters = [];
						       $scope.multiselectObj ={
						    		   "id":"",
						    		   "label":"",
						       }
								$scope.getParameterList = function(testId)
							       {
							        try 
							        {
							        $scope.parameterList=[];
							         var data = { "orgId":$scope.orgId,
							           "orgUnitId":$scope.orgUnitId,
							           "deptId":$scope.deptId,
							           "subDeptId":$scope.subDeptId,
							           "searchKeyword":"",
							         }
							         var URI = BASE_URL 
							                 + ROOT_URL
							                 + LIS_COMMON
							                 + TEST_PARAMETERS+S+testId+S+$scope.orgId+S+$scope.orgUnitId;
							            console.log("URI", URI);
							            GenericService.serviceAction("GET", URI, data).then(function(response) 
							            {
							            	angular.forEach(response.data.listObject, function(parameterObj, key)
													{
												       var multiselectObj = angular.copy($scope.multiselectObj); 
												       multiselectObj.id = parameterObj.parameterId;
												       multiselectObj.label = parameterObj.parameterName;
												       $scope.parameterList.push(multiselectObj);
													});
							         });
							        } catch (e) {
							         console.log(e.message)
							        }
							       }
							
							
						   
						      
						       //Get Multiple Test
                               $scope.flagCheckOnHoldParam=false;

							$scope.getBiochemResultEntryDetails = function() 
							{
								
								try {
									$rootScope.startLoader();
									$scope.labResultDetailsMasterDtoDtlsList =[];
									var dataList = [];
									angular.forEach($scope.labResultMasterList, function(value, key) {
									$scope.isOnHoldParameter=value.isOnHoldParameter;
									var resultEntry = value;
									$scope.testType= resultEntry.testType;
									$scope.patientId  = resultEntry.patientId;
									$scope.labSampleDtlsId  =resultEntry.labSampleDtlsId;
									
									var data = {
											"orgId":resultEntry.orgId,
											"orgUnitId":$scope.orgUnitId,
											"testId":resultEntry.testId,
											"testType":resultEntry.testType,
											"deptId":resultEntry.deptId,
											"subDeptId":resultEntry.subDeptId,
											"patientAgeDays":resultEntry.patientAgeDays,
											"labSampleNo":resultEntry.sampleNo,
											"genderId":resultEntry.genderId,
											"panelId":resultEntry.panelId,
											"assingnedFrom":resultEntry.orgUnitId,
											"labSampleDtlsId":resultEntry.labSampleDtlsId
											
										};
                                    if($scope.isOnHoldParameter == true || resultEntry.labTestResId != null ){
										data.labResultId = resultEntry.labTestResId;
										$scope.labTestResId = resultEntry.labTestResId;
									}else{
										data.labResultId = null;
									}
									 dataList.push(data);
									});
									
                                    if($scope.isOnHoldParameter == true || $scope.labTestResId != null ){
										var URI = BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ GET_WORK_LIST_VALIDATION_ENTERY_DETAILS;
									}else{
										var URI = BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ GET_WORK_LIST_DEPT_RESULT_ENTERY_DETAILS;
									}
									
									console.log("URI", URI);

									GenericService
									.serviceAction(METHOD_POST, URI, dataList)
											.then(
													function(response) {
														$rootScope.stopLoader();
														if (response.data.status == 'success')
														{
															$rootScope.stopLoader();
															$scope.testMasterResList = [];
															$scope.testMasterResList = response.data.listObject;

															$scope.testDescData=[];
                                                            angular.forEach($scope.testMasterResList,function(value,key){
                                                                $scope.testDescData.push(value.testDesc)
                                                            });

															angular.forEach($scope.testMasterResList, function(value, key) 
															{
																value.isSelected = true;
																var labResultMaster = $scope.labResultMasterList.find(obj => {return obj.labSampleDtlsId == value.labSampleDtlsId})
															    value.sampleRecollectAgainstId =labResultMaster.sampleRecollectAgainstId;
															    value.isRecollected =labResultMaster.sampleRecollectFlag=='Y'?true:false;
															    value.isRetestRecollected = (labResultMaster.sampleRecollectFlag=='Y'||labResultMaster.retestCount>0)?true:false;
                                                                if($scope.isOnHoldParameter == true){
                                                                    $scope.flagCheckOnHoldParam=true;
                                                                }else{
                                                                	$scope.flagCheckOnHoldParam=false;
                                                                }
															    if(labResultMaster.paymentEntitlementId!=null && labResultMaster.paymentEntitlementId == 2){
															    	value.insuranceId = labResultMaster.payeeId;
															    	value.companyId = null;
															     }else if(labResultMaster.paymentEntitlementId!=null && labResultMaster.paymentEntitlementId == 3){
															    	 value.companyId = labResultMaster.payeeId;
															    	 value.insuranceId = null;
															     }else{
															    	 value.companyId = null;
															    	 value.insuranceId = null;
															     }
															    angular.forEach($scope.testMasterResList, function(value, key) {
																    $scope.getPreviousDeltaReport(value.testId,value.testType,value.labSampleDtlsId,key);
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
															    });
															});
															
															
															$scope.getMachineResult(1);
															 // sneha code 
															angular.forEach($scope.testMasterResList, function(value, testIndex) 
																	{
															  angular.forEach(value.listMultyParamTestHeaderDto, function(paramInHeaderObj, headerIndex)
																	    {
																	        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(labResultDetails, resultIndex)
																	          {
																	        	labResultDetails.tabIndex = 'finalResult_' + testIndex + '_' + headerIndex + '_' + resultIndex;
																	          });
																	    });
															 
																	 });
															 
															/*	  $timeout(function() {
																  //Get the element
																  var element = document.getElementById(id);
																  console.log("element"+element);
																  // Check if the element is not disabled before setting focus
																  if (element) {
																      element.focus();
																  }
																  
															  }, 6000);*/
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
							
							/**Redirect Page Code*/
							if($stateParams.resultMstList!=null  && $stateParams.resultMstList.length>0)
 							{
								$scope.labResultMasterList = $stateParams.resultMstList;
								$scope.nextRecordList = $stateParams.nextRecordList;
								$scope.searchObjfromAmend = $stateParams.searchObj
								$scope.getBiochemResultEntryDetails();								
							} else {
								
								if( $scope.searchObjfromAmend == undefined || $scope.searchObjfromAmend.amend == undefined){
									
									if($scope.consolidatedResultEntry!='Y'){
										$state.go('worklistForReportEntry',{subDeptId : subDeptId,searchObj : $stateParams.searchObj});
									}else{
										
                                        if($stateParams.searchObj.selectedVisitType !== undefined && $stateParams.searchObj.selectedVisitType!== null && $stateParams.searchObj.selectedVisitType === 4){
                                            $stateParams.searchObj.selectedVisitType = [];
                                            $stateParams.searchObj.selectedPatient = null;
                                            $stateParams.searchObj.patientId = undefined;
                                            $stateParams.searchObj.uhid = '';
                                        }
										 $state.go('consolidatedResultEntry',
													{
													  searchObj : $stateParams.searchObj
													});
									
									}
								
								}else{
									$state.go('amendmentWorklist',
											{
											  searchObj : $stateParams.searchObj
											});
								}
								//$state.go('worklistForReportEntry',{subDeptId : subDeptId});
							}
							
							
							
							$scope.labResultMasterDtoList = [];
							
							$scope.createLabResultObjectList = function(sampleStatusId)
							{
								
								$scope.labResultMasterDtoList = [];
								angular.forEach($scope.testMasterResList, function(value, key) {
									
									if(value.isSelected==true)
										{
										 $scope.labResultMasterDto = 
										 $scope.labResultMasterList.find(obj => {
											  return obj.labSampleDtlsId == value.labSampleDtlsId
											})
										
										if(value.testType==0)
										{
											  $scope.listLabResultEntryDtls = [];
											  $scope.labResultMasterDto.createdBy =$scope.userId;
											  $scope.labResultMasterDto.updatedBy =$scope.userId;
								    	      $scope.labResultMasterDto.orgId= $scope.orgId;
								    	      $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
								    	      $scope.labResultMasterDto.testDesc = value.testDesc;
								    	      if($scope.isValidationAndRelease == 'Y' || sampleStatusId== 15){
								    	    	  $scope.labResultMasterDto.assignUnit=value.assignUnit;
									    	      $scope.labResultMasterDto.performTestId=value.performTestId;
									    	      $scope.labResultMasterDto.testId=value.testId;
								    	      }
								    	      if(sampleStatusId== 15){
								    	    	  $scope.labResultMasterDto.sampleRejectReason = $scope.sampleRejectReason;
								    	    	  $scope.labResultMasterDto.sampleRejectReasonId =  $scope.sampleRejectReasonId;
								    	      }
								    	      $scope.labResultMasterDto.footsNotes=value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;
                                              $scope.labResultMasterDto.isOnHoldParameter = ($scope.isOnHoldParameter !=undefined && $scope.isOnHoldParameter != null && $scope.isOnHoldParameter == false) ? false : value.isOnHoldParameter;
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
										      		 if( value.labResultDetailsViewDto.finalResult!=undefined && value.labResultDetailsViewDto.finalResult!=null &&  value.labResultDetailsViewDto.finalResult!="-"){
										      		     var arr =  value.labResultDetailsViewDto.finalResult.split('.');
										      		     if(arr[1]==undefined &&  value.labResultDetailsViewDto.paramPrecision > 0){
										      		     	 value.labResultDetailsViewDto.finalResult =  parseInt(value.labResultDetailsViewDto.finalResult).toFixed( value.labResultDetailsViewDto.paramPrecision); 
										      		     }	
										      		 }
										      }
								    	      
								    	      $scope.listLabResultEntryDtls.push(value.labResultDetailsViewDto);
										      $scope.labResultMasterDto.footsNotes = value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;

										      $scope.labResultMasterDto.listLabSampleResultDetailsMaster = $scope.listLabResultEntryDtls;
										      $scope.labResultMasterDto.resultLevel = RESULT_ENTRY;
										      $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
										      $scope.labResultMasterDto.remark = (value.remarks == undefined || value.remarks == null )? " " : value.remarks ;
										      if($scope.triggerReleaseMail == 'Y'){
												  $scope.labResultMasterDto.triggerReleaseMail = true;
											  }else{
												  $scope.labResultMasterDto.triggerReleaseMail = false;
											  }
										      if($scope.sendMailOnOrderCompletion == 'Y'){
												  $scope.labResultMasterDto.sendMailOnOrderCompletion = true;
											  }else{
												  $scope.labResultMasterDto.sendMailOnOrderCompletion = false;
											  }
										      $scope.labResultMasterDto.insuranceId =  value.insuranceId;
										      $scope.labResultMasterDto.companyId =  value.companyId;
										      $scope.labResultMasterDto.machineId = value.machineId;
										      if($scope.searchObjfromAmend.amend == undefined ){
										    	  $scope.labResultMasterDto.labTestResId = $scope.labTestResId;
										      }else{
										    	  $scope.labResultMasterDto.labTestResId = null;
										      }
										      $scope.labResultMasterDto.amend =  $scope.searchObjfromAmend.amend != undefined ? $scope.searchObjfromAmend.amend : false;
                                              $scope.labResultMasterDto.labSampleDtlsId = value.labSampleDtlsId;
                                              $scope.labResultMasterDto.provisional = $scope.provisional;
										      $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
										}
										else if(value.testType==1)
										{
											$scope.listLabResultEntryDtls  = [];
											$scope.labResultMasterDto.createdBy=$scope.userId;
											$scope.labResultMasterDto.updatedBy =$scope.userId;
						    	            $scope.labResultMasterDto.orgId= $scope.orgId;
						    	            $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
						    	            if($scope.searchObjfromAmend.amend == undefined){
										    	  $scope.labResultMasterDto.labTestResId =$scope.labTestResId;
										      }else{
										    	  $scope.labResultMasterDto.labTestResId = null;
										      }
						    	            $scope.labResultMasterDto.testDesc = value.testDesc;
						    	            if($scope.isValidationAndRelease == 'Y' || sampleStatusId== 15){
								    	    	  $scope.labResultMasterDto.assignUnit=value.assignUnit;
									    	      $scope.labResultMasterDto.performTestId=value.performTestId;
									    	      $scope.labResultMasterDto.testId=value.testId;
								    	      }
						    	            if(sampleStatusId== 15){
								    	    	  $scope.labResultMasterDto.sampleRejectReason = $scope.sampleRejectReason;
								    	    	  $scope.labResultMasterDto.sampleRejectReasonId =  $scope.sampleRejectReasonId;
								    	      }
						    	            $scope.labResultMasterDto.footsNotes=value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;
								    	      $scope.labResultMasterDto.isReportRecall=false;
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
													      
													      //to append precision to int val 
												      		 if(labResultDetails.finalResult!=undefined && labResultDetails.finalResult!=null   && labResultDetails.finalResult!='-'  &&  labResultDetails.refrangeTypeId !=null && labResultDetails.refrangeTypeId== 1){
												      			if(labResultDetails.finalResult==0)
											      				 {
											      				labResultDetails.finalResult="0";
											      				 }
												      		     /*var arr =  labResultDetails.finalResult.split('.');
												      		     if(arr[1]==undefined &&  labResultDetails.paramPrecision > 0 ){
												      		    	labResultDetails.finalResult =  parseInt(labResultDetails.finalResult).toFixed( labResultDetails.paramPrecision); 
												      		     }else  if(arr[1]!=undefined &&  labResultDetails.paramPrecision > 0 ){
												      		    	labResultDetails.finalResult = parseFloat(labResultDetails.finalResult).toFixed(labResultDetails.paramPrecision);
												      		     }	*/
												      		 }
													      $scope.listLabResultEntryDtls.push(labResultDetails);
													  });
												});
											  $scope.labResultMasterDto.listLabSampleResultDetailsMaster = $scope.listLabResultEntryDtls;
											  $scope.labResultMasterDto.footsNotes = value.footsNotes;
								    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
								    	      $scope.labResultMasterDto.srfId=value.srfId;
								    	      $scope.labResultMasterDto.icmrId=value.icmrId;
                                              $scope.labResultMasterDto.isOnHoldParameter=value.isOnHoldParameter;

											  $scope.labResultMasterDto.resultLevel = RESULT_ENTRY;
											  if($scope.triggerReleaseMail == 'Y'){
												  $scope.labResultMasterDto.triggerReleaseMail = true;
											  }else{
												  $scope.labResultMasterDto.triggerReleaseMail = false;
											  }
											  if($scope.sendMailOnOrderCompletion == 'Y'){
												  $scope.labResultMasterDto.sendMailOnOrderCompletion = true;
											  }else{
												  $scope.labResultMasterDto.sendMailOnOrderCompletion = false;
											  }
											  $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
											  $scope.labResultMasterDto.remark = (value.remarks == undefined || value.remarks == null )? "" :value.remarks ;
											  $scope.labResultMasterDto.insuranceId =  value.insuranceId;
										      $scope.labResultMasterDto.companyId =  value.companyId;
										      $scope.labResultMasterDto.machineId = value.machineId;
										      $scope.labResultMasterDto.provisional = $scope.provisional;
										      $scope.labResultMasterDto.amend =  $scope.searchObjfromAmend.amend != undefined ? $scope.searchObjfromAmend.amend : false;
											  $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
										 }
										}
									});
							}
							
							
							  //save Billable Consumption
							$scope.remark = "";
						      $scope.saveNewConsumption = function () {
						             
						                 var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.SAVE_API;
						                 var data = {
						                     "storeId": cookieObject.storeId,
						                     "statusId": 1,
						                     "approvalStatusId": 1,
						                     "remark": $scope.remark ,
						                     "requestFrom":"Lab",
						                     "batch": [],
						                     "unitId":$scope.orgUnitId,
						                     "organizationId":$scope.orgId,
						                     "userId":$scope.userId,
						                     "storeId":$scope.storeId
						                 };

						                 angular.forEach($scope.itemList, function (item, key) {
						                    
						                     var storeItem = {
						                             "batchId": item.selectedBatch.batchId,
						                             "uomTypeId": item.selectedUom.uomTypeId,
						                             "uomUnitId": item.selectedUom.uomUnitId,
						                             "consumedQuantity": parseInt(item.consumedQuantity),
						                             "statusId": 1
						                         }
						                     data.batch.push(storeItem);

						                 });
						                 $rootScope.startLoader();
						                 GenericService.serviceAction("POST", URI, data).then(
						                     function (response) {
						                         $rootScope.stopLoader();
						                         $scope.itemList = [];
						                     },
						                     function (err) {
						                         $rootScope.stopLoader();
						                       
						                     });
						             }
							
						     
							
						      
							$scope.saveReportEntry= function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.worklistForReportEntryForm.$valid || $scope.isOnHoldParameter == true)
								{
									try 
									{
										$rootScope.startLoader();
										//$scope.labResultMasterDto.sampleStatusId = RESULT_ENTRY_DONE;
										
										 if($scope.isValidationAndRelease == 'Y'){
											 $scope.createLabResultObjectList(8);
										 }else{
											 $scope.createLabResultObjectList(RESULT_ENTRY_DONE);
										 }
										
										
                                        if($scope.labResultMasterDtoList.length == 0){
                                            $rootScope.stopLoader();
                                            growl.warning("Please Select Test",
                                                        {
                                                            title : 'warning'
                                                        });
                                            $scope.saveOrNextButton = false;
                                                        return false;
                                        }
                                        
                                        //check Billable consumption is valid or not
                                        if($scope.itemList!=null && $scope.itemList.length>0){
                                        	
                                        	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedBatch == undefined || obj.selectedBatch.batchId == undefined )})
											 if(isValidBatch!=undefined){
												 $rootScope.stopLoader();
		                                            growl.warning("Please Select Batch Of All Items",
		                                                        {
		                                                            title : 'warning'
		                                                        });
		                                            $scope.saveOrNextButton = false;
		                                                        return false;
											 } 
                                        	
                                        	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedUom == undefined || obj.selectedUom.uomTypeId == undefined )})
											 if(isValidBatch!=undefined){
												 $rootScope.stopLoader();
		                                            growl.warning("Please Select UOM Type Of All Items",
		                                                        {
		                                                            title : 'warning'
		                                                        });
		                                            $scope.saveOrNextButton = false;
		                                                        return false;
											 } 
                                        	
                                        	var isValidBatch = $scope.itemList.find(obj =>{return (obj.consumedQuantity == undefined || obj.consumedQuantity == 0 )})
											 if(isValidBatch!=undefined){
												 $rootScope.stopLoader();
		                                            growl.warning("Please Enter Consumed Quantity",
		                                                        {
		                                                            title : 'warning'
		                                                        });
		                                            $scope.saveOrNextButton = false;
		                                                        return false;
											 } 
										}
                                        
										var URI = "";
										
										$scope.checkValueFlag = false;
										
										if($scope.isOnHoldParameter == true && ($scope.labResultMasterDtoList != null && $scope.labResultMasterDtoList.length > 0)){
											angular.forEach($scope.labResultMasterDtoList, function(value) {
												if(value.listLabSampleResultDetailsMaster.length > 0){
													angular.forEach(value.listLabSampleResultDetailsMaster, function(val1) {
														if(val1.finalResult != null){
															$scope.checkValueFlag = true;
														}
													});
												}
											});
										}else if($scope.isOnHoldParameter == false){
											$scope.checkValueFlag = true;
										}
										
										if($scope.checkValueFlag == true){
											if($scope.flagCheckOnHoldParam == true){
	                                            URI = BASE_URL + ROOT_URL+"/LIS/BioChemistry/ResultEntry/updateEntryDetails";
	                                        }else{
	                                            if($scope.isValidationAndRelease == 'Y'){
	                                                
	                                                URI = BASE_URL + ROOT_URL+"/LIS/BioChemistry/releaseEntry/EntryDetails";
	                                             
	                                        }else{
	                                             URI =BASE_URL + ROOT_URL
	                                                + LIS_BIOCHEMISTRY
	                                                + WORK_RESULT_ENTERY_DETAILS; 
	                                        }
										} 
										}else{
                                            	
											growl.warning("Please enter atleast one test result.",
													{
														title : 'Warning!'
													});
											$scope.saveOrNextButton = false;
											$rootScope.stopLoader();
											  return false;
                                        }

										
										console.log("URI",URI);
										GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$scope.saveOrNextButton = false;
												$rootScope.stopLoader();
												
												if($scope.itemList!=null && $scope.itemList.length>0){
													$scope.saveNewConsumption();
												}if(response.data.object!=null){
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													
													growl.warning(response.data.object.rptResponse,
															{
																title : 'warning!'
															});
												}else{
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
												}
												
												
												if($scope.provisional ==true){
													
													angular.forEach($scope.labResultMasterDtoList,function(value,index) {
														
														$scope.labSampleDtlsIdsForPrint = [];
												    	$scope.labSampleDtlsIdsForPrint.push(value.labSampleDtlsId);
												    	
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
												    	
													});
													
												}
												
												
												if($scope.isRedirect || ($scope.nextRecordList.length) == $scope.nextRecordIndex){
													
													if( $scope.searchObjfromAmend.amend == undefined){
														
														if($scope.consolidatedResultEntry!='Y'){
															$state.go('worklistForReportEntry',{subDeptId : subDeptId,searchObj : $stateParams.searchObj});
														}else{
															
                                                            if($stateParams.searchObj.selectedVisitType !== undefined && $stateParams.searchObj.selectedVisitType!== null && $stateParams.searchObj.selectedVisitType === 4){
                                                                $stateParams.searchObj.selectedVisitType = [];
                                                                $stateParams.searchObj.selectedPatient = null;
                                                                $stateParams.searchObj.patientId = undefined;
                                                                $stateParams.searchObj.uhid = '';
                                                            }
															 $state.go('consolidatedResultEntry',
																		{
																		  searchObj : $stateParams.searchObj
																		});
														
														}
													
													}else{
														$state.go('amendmentWorklist',
																{
																  searchObj : $stateParams.searchObj
																});
													}
												}else{
													$scope.labResultMasterList = [];
													$scope.labResultMasterDtoList = [];
													$scope.labResultMasterList = $scope.nextRecordList[$scope.nextRecordIndex].resultMstList;
													$scope.testMasterResList = [];
													$scope.getBiochemResultEntryDetails();		
													 $scope.getTestList();
												}
												
											} else if(response.data.status == 'error')
											{
												$scope.saveOrNextButton = false;
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
									
								}
								else
								{
									growl.error("Please Fill All Mandatory Fields.",
											{
												title : 'Error!'
											});
									$scope.saveOrNextButton = false;
								}
							}

							$scope.validateQty = function() {
								var isValid = true;
								angular.forEach($scope.itemList, function(value) {
									if(value.availableQuantity < value.consumedQuantity){
										growl.warning("Not Enough Quantity",
										{
											title : 'warning!'
										});
										
										if($scope.cnt  == 1){
											$scope.nextRecordIndex = 0;
										   	$scope.cnt  = 0;
										}else{
											$scope.nextRecordIndex = $scope.nextRecordIndex -1;
										   	$scope.cnt  = $scope.cnt -1;
										}
										$scope.saveOrNextButton = false;
						    		  	isValid = false;
//						    		  	$scope.itemList[index].consumedQuantity = 0;
									}
								});
								return isValid;
							}
							
							$scope.countflag = false;
							$scope.headerName= "";
							$scope.mistachCountMSG = "";
							$scope.isRedirect = false;
							$scope.provisional = false;
							
							$scope.saveResultEntryDetails = function(isRedirect,provisionalRelease,isOnHol){
								if($scope.validateQty()){
									$scope.isRedirect = isRedirect;
									$scope.isOnHoldParameter = isOnHol;
									$scope.countflag = false;
									$scope.headerName= "";
									$scope.provisional = provisionalRelease;
									$scope.mistachCountMSG = "";
									var  totalValue = 0;
									var   headerTotalCount = 0;
									var loopFlag = false;
									angular.forEach($scope.testMasterResList, function(value, key) {
										
										if(value.isSelected==true)
											{
											 $scope.labResultMasterDto = 
											 $scope.labResultMasterList.find(obj => {
												  return obj.labSampleDtlsId == value.labSampleDtlsId
												})
												
												value.isOnHoldParameter = isOnHol;
											 
											if(value.testType==1)
											{
												
												angular.forEach(value.listMultyParamTestHeaderDto, function(headerParamObj, key) 
														{
													
														if(!loopFlag){
														
															
															  totalValue = 0;
																angular.forEach(headerParamObj.listLabResultDetailsViewDto, function(labResultDetails, key) 
																		{
																			if(headerParamObj.isParameterCalculation == true && labResultDetails.refrangeTypeId == 1){
																				$scope.countflag = true;
																				$scope.headerName= headerParamObj.headerDesc;
																				totalValue = totalValue + parseFloat(labResultDetails.finalResult);
																			}
																			
																	    
																	  });
																
																if( $scope.countflag == true && totalValue!=headerParamObj.totalCount && $scope.flagCheckOnHoldParam == false){
																	
																	 headerTotalCount = headerParamObj.totalCount;
																	 if(totalValue>headerParamObj.totalCount){
																		 
																		 $scope.mistachCountMSG = $scope.headerName + " header's parameter total calculation greater than  " +headerParamObj.totalCount;
																		 
																	 }else{
																		 
																		 $scope.mistachCountMSG = $scope.headerName + " header's parameter total calculation less than  " +headerParamObj.totalCount; 
																		 
																	 }
																	 
																	 loopFlag = true;
																		// return false;
																 }else{
																	 $scope.countflag = false;
																 }
														}
													
													});
											 }
											
											}
										});
								 
								 
									 if( $scope.countflag == true){
										// $("#headerParamCalculationModal").modal('show');
											growl.warning($scope.mistachCountMSG,
													{
														title : 'warning!'
													});
												$scope.saveOrNextButton = false;
											    $rootScope.stopLoader();
											    return false;
										  }else{
											  
											  if($scope.isValidationAndRelease == 'Y'){
													
													 if($scope.reportReleasePin == "Y")
														{
														     $('#labPinModal').modal('show');
														     
														}else{
															
													 		$scope.saveReportEntry();
													 	}
													 
												}else{
													$scope.saveReportEntry();
												}
										  }
								} 	
							}
							 
							 $scope.confirmationSave = function(){
								  if($scope.isValidationAndRelease == 'Y'){
										
										 if($scope.reportReleasePin == "Y")
											{
											     $('#labPinModal').modal('show');
											     
											}else{
												
										 		$scope.saveReportEntry();
										 	}
										 
									}else{
										$scope.saveReportEntry();
									}
							 }
							 
								$scope.disablePinBtn = false;
								$scope.checkLabPin = function()
								{
									$scope.$broadcast('show-errors-check-validity');
									if (this.labPinForm.$valid)
									{
										$scope.disablePinBtn = true;
									 var URI = BASE_URL + ROOT_URL + "/lab/pinuser/"+$scope.orgId+"/"+$scope.orgUnitId+"/"+$scope.labPin;
									 GenericService
											.serviceAction(METHOD_GET, URI, {})
											.then(
													function(response) 
													{
														console.log(response);
														  $scope.disablePinBtn = false;
														if (response.data.status == "success")
															{
															  $scope.userId =  response.data.object;
															  $('#labPinModal').modal('hide');
															  $scope.saveReportEntry();
															  $('.modal-backdrop').remove()
							                                    $(document.body).removeClass("modal-open");
															  $scope.$broadcast('show-errors-reset');
															}
														else if(response.data.status == "error")
															{
															growl
															.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
															$('.modal-backdrop').remove()
						                                    $(document.body).removeClass("modal-open");
															   $rootScope.stopLoader();
															}
													});
									}
									else{
										 $('#labPinModal').modal('show');
										 $('.modal-backdrop').remove()
		                                    $(document.body).removeClass("modal-open");
									}
									$('.modal-backdrop').remove()
                                    $(document.body).removeClass("modal-open");
								}
							 
							 

							$scope.retestDisableBtn = false;
							$scope.reTestResultEntryDetails= function() 
							{
									try 
									{	
										
										$scope.$broadcast('show-errors-check-validity');
										if ($scope.worklistForReportEntryForm.$valid || $scope.isOnHoldParameter == true)
									{
										$rootScope.startLoader();
										$scope.retestDisableBtn = true;
										$scope.createLabResultObjectList(RETESTING);
										 if($scope.labResultMasterDtoList.length == 0){
	                                            $rootScope.stopLoader();
	                                            growl.warning("Please Select Test",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                            $scope.retestDisableBtn = false;
	                                                        return false;
	                                        }
										var URI =BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ RETEST_WORK_RESULT_ENTERY_DETAILS;
										GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												$scope.retestDisableBtn = false;
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												
												
												if( $scope.searchObjfromAmend.amend == undefined){
													
													if($scope.consolidatedResultEntry!='Y'){
														$state.go('worklistForReportEntry',{subDeptId : subDeptId,searchObj : $stateParams.searchObj});
													}else{
														
                                                        if($stateParams.searchObj.selectedVisitType !== undefined && $stateParams.searchObj.selectedVisitType!== null && $stateParams.searchObj.selectedVisitType === 4){
                                                            $stateParams.searchObj.selectedVisitType = [];
                                                            $stateParams.searchObj.selectedPatient = null;
                                                            $stateParams.searchObj.patientId = undefined;
                                                            $stateParams.searchObj.uhid = '';
                                                        }
														 $state.go('consolidatedResultEntry',
																	{
																	  searchObj : $stateParams.searchObj
																	});
													
													}
												
												}else{
													$state.go('amendmentWorklist',
															{
															  searchObj : $stateParams.searchObj
															});
												}
												

											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												$scope.retestDisableBtn = false;
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
									}
										else
										{
											growl.error("Please Fill All Mandatory Fields.",
													{
														title : 'Error!'
													});
										}
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
									
							}
							
							$scope.disableRecollectBtn = false;
							$scope.reCollectResultEntryDetails= function() 
							{
							    $scope.reCollectBtnCount = 0;
								if($scope.sampleRejectReasonId == ""){
									
									growl.warning("Please select reason..!!",
											{
												title : 'warning!'
											});
									return false;
								}
								else if($scope.sampleRejectReason == "")
								{
									growl.warning("Please Enter reason note..!!",
											{
												title : 'warning!'
											});
									return false;
									
								}
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.worklistForReportEntryForm.$valid)
									{
									try 
									{
										
										
										$rootScope.startLoader();
										$scope.disableRecollectBtn = true;
										$scope.createLabResultObjectList(SAMPLE_REJECTED);
										 if($scope.labResultMasterDtoList.length == 0){
	                                            $rootScope.stopLoader();
	                                            growl.warning("Please Select Test",
	                                                        {
	                                                            title : 'warning'
	                                                        });
	                                            $scope.disableRecollectBtn = false;
	                                                        return false;
	                                        }
										var URI =BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ RECOLLECT_WORK_RESULT_ENTERY_DETAILS;
										console.log("URI",URI);
										console.log("labResultMasterDtoList",JSON.stringify($scope.labResultMasterDtoList));
										GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												$scope.disableRecollectBtn = false;
												 $('#accptRejInPatient').modal('hide');
												 $('.modal-backdrop').remove();
													$('body').removeClass("modal-open");
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												
												if( $scope.searchObjfromAmend.amend == undefined){
													
													if($scope.consolidatedResultEntry!='Y'){
														$state.go('worklistForReportEntry',{subDeptId : subDeptId,searchObj : $stateParams.searchObj});
													}else{
														
                                                         if($stateParams.searchObj.selectedVisitType !== undefined && $stateParams.searchObj.selectedVisitType!== null && $stateParams.searchObj.selectedVisitType === 4){
                                                            $stateParams.searchObj.selectedVisitType = [];
                                                            $stateParams.searchObj.selectedPatient = null;
                                                            $stateParams.searchObj.patientId = undefined;
                                                            $stateParams.searchObj.uhid = '';
                                                         }
														 $state.go('consolidatedResultEntry',
																	{
																	  searchObj : $stateParams.searchObj
																	});
													
													}
												
												}else{
													$state.go('amendmentWorklist',
															{
															  searchObj : $stateParams.searchObj
															});
												}
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												$scope.disableRecollectBtn = false;
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										  });
										}
										 catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
							}
									else
									{
										growl.error("Please Fill All Mandatory Fields.",
												{
													title : 'Error!'
												});
									}
							}

							$scope.cancelRejectDetails = function(){
							    $scope.reCollectBtnCount = 0;
							}
							
							$scope.getResultEntryDataList = function(sampleRecollectAgainstId,parameterId) {
								try {
								
									$rootScope.startLoader();
									var data = {
													"orgId":$scope.orgId,
													"orgUnitId":$scope.orgUnitId,
													"parameterId":parameterId,
													"sampleRecollectAgainstId":sampleRecollectAgainstId,
													"firstLevelResult":"firstLevel"
										      };
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ GET_WORK_RESULT_ENTERY_DETAILS;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) 
													{
														if(response.data.status=="success")
															{
															  $rootScope.stopLoader();
															  $scope.resultEntryData  = response.data.listObject;
															  $('#previousResult').modal('show');
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
							
						/*	$scope.previousResultFlag = false;
							$scope.getLabHistoryReport = function()
							{
								$("#resetDetails").modal('show');
							}*/
							
							
							$scope.selectLabResult = function(index)
							{
								var paramResObj = {
										"parameterId":"",
										"finalResult":"",
								}
								var resultArray = [];
								
								angular.forEach($scope.previousTest.listParameterHistoryDto, function(previousObj, key) 
								    {
									     var paramRes = angular.copy(paramResObj);
									     paramRes.parameterId = previousObj.parameterId;
									     paramRes.finalResult = previousObj.listParameterResultDto==null?0:previousObj.listParameterResultDto[index].result;
									     console.log("previousObj",previousObj.listParameterResultDto);
									     resultArray.push(paramRes);
									});
								
								$scope.selectResultValue = function(parameterId) {
									$scope.ResultValue = resultArray
											.filter(function(paramObj) {
												if (paramObj.parameterId == parameterId) {
													return paramObj;
												}
											});
								}
									angular.forEach($scope.testMasterResList, function(testObj, key)
										{
										      if(testObj.testType==0)
										    	  {
										    	  $scope.selectResultValue(testObj.labResultDetailsViewDto.parameterId);
										    	  testObj.labResultDetailsViewDto.finalResult = $scope.ResultValue[0].finalResult;
										    	  }
										      else if(testObj.testType==1)
										    	  {
										    	  angular.forEach(testObj.listMultyParamTestHeaderDto, function(headerObj, key)
															{
														       angular.forEach(headerObj.listLabResultDetailsViewDto, function(resultObj, key)
																{
														    	   $scope.selectResultValue(resultObj.parameterId);
														    	   resultObj.finalResult = $scope.ResultValue[0].finalResult;
																});
															});
										    	  }
										        
										});
									$("#resetDetails").modal('hide');
									
							}
							
							$scope.getRetestRecollectTests = function(testId,testType,labSampleDtlsId)
							{
								  try {
									   $scope.previousTest = [];
										$rootScope.startLoader();
										var data = {
												 "orgId":$scope.orgId,
												 "orgUnitId":$scope.orgUnitId,
												 "deptId": $scope.deptId,
												 "testId":testId,
												 "testType":testType,
												 "labResultId":"",
												 "subDeptId":$scope.subDeptId,
												 "labSampleDtlsId":labSampleDtlsId,
												 "patientId":$scope.labResultMasterList[0].patientId,
												 "resultLevel":RESULT_ENTRY
												}
										
										var URI = BASE_URL + ROOT_URL
												+ LIS_BIOCHEMISTRY
												+ LAB_PREVIOUS_RESULT_BY_LEVEL;
										
										 GenericService
												.serviceAction(METHOD_POST, URI, data)
												.then(
														function(response) 
														{
															if(response.data.status=="success")
																{
																  $rootScope.stopLoader();
																  $scope.previousTest  = response.data.object;
																  $('#resetDetails').modal('show');
																}
															else if(response.data.status=="error"){
																$rootScope.stopLoader();
															  }
														});
									} catch (e) {
										console.log(e.message)
									}
							}
							
							
							
							$scope.getPreviousDeltaReport = function(testId,testType,labSampleDtlsId,key)
							{
								var promise =   $scope.getPreviousResultId(testId,testType,labSampleDtlsId);
								promise.then(function(resultId) {
									  if(resultId>0)
										  {
										  try {
											  var previousTestResultList = [];
												$rootScope.startLoader();
												var data = {
														 "orgId":$scope.orgId,
														 "orgUnitId":$scope.orgUnitId,
														 "deptId": $scope.deptId,
														 "testId":testId,
														 "testType":testType,
														 "labResultId":$scope.resultEntryId,
														 "subDeptId":$scope.subDeptId,
														 "labSampleDtlsId":""
														}
												
												var URI = BASE_URL + ROOT_URL
														+ LIS_BIOCHEMISTRY
														+ PRIVOUSRESULTBYSAMPLE_DETAILS;

												GenericService
														.serviceAction(METHOD_POST, URI, data)
														.then(
																function(response) 
																{
																	if(response.data.status=="sucess")
																		{
																		 $rootScope.stopLoader();
																		 previousTestResultList  = response.data.listObject!=null?response.data.listObject:[];	
																		 $scope.testMasterResList[key].previousTestResultList =previousTestResultList;
																		}
																	else{
																		$scope.testMasterResList[key].previousTestResultList =[];
																		   $rootScope.stopLoader();
																	    }
																});
											} catch (e) {
												console.log(e.message)
											}
										  }
									  else{
											$scope.testMasterResList[key].previousTestResultList =[];
										  }
								    });
								
								
						/*		  var promiseRetestRecollect =   
								  promiseRetestRecollect.then(function(testObjList) 
										  {
										  if(testObjList!=null)
											  {
											  $scope.previousResultFlag = true;
											  }
										  else{
											  $scope.previousResultFlag = false;
										  }
									    });*/
								
							}
							
							
							
							$scope.getPreviousResultId = function(testId,testType,labSampleDtlsId)
							{
								try {
								$rootScope.startLoader();
								var data = {
										  "labSampleDtlsId" : "",
										  "orgId" :$scope.orgId,
										  "orgUnitId" :$scope.orgUnitId,
										  "offset" : "",
										  "deptId" : $scope.deptId,
										  "recordPerPage" : "",
										  "testId" :  testId,
										  "testType" : testType,
										  "patientAgeDays" : "",
										  "panelId" : "",
										  "genderId" : "",
										  "labResultId" : "",
										  "headerId" : "",
										  "subDeptId" : $scope.subDeptId,
										  "labSampleNo" : "",
										  "patientId" : $scope.labResultMasterList[0].patientId
										}
								var URI = BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ GET_RESULT_ID_BY_TEST;
								console.log("URI", URI);
								console.log("data",JSON.stringify(data));
							  return GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													return $scope.resultEntryId  = response.data.object;
												});
							} catch (e) {
								console.log(e.message)
							}
							}
							
							
							 $scope.changeArrowPosition  = function(testIndex,headerIndex,resultIndex,testType,isEditable,id2)
							 {
								 
								 
								/* var inputField = document.getElementById(id2);

								// Add an event listener for input changes
								    // Get the input value
								    var inputValue = event.target.value;

								    // Convert the input value to a number with three decimal places
								    var formattedValue = 0;
								    
								    if(testType==0 && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==1)
                                    {
								    	formattedValue = parseFloat(inputValue).toFixed($scope.testMasterResList[testIndex].labResultDetailsViewDto.paramPrecision);
                                    }
                                    if(testType==1 && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==1 ){
                                    	formattedValue = parseFloat(inputValue).toFixed($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].paramPrecision);
                                    }*/
								    
								    	

								    // Update the input field with the formatted value
								  
                              /*  if(!isNaN(formattedValue) && formattedValue != undefined && formattedValue != null && formattedValue != ""){
                                    event.target.value = formattedValue;
                                    if(testType==0)
                                    {
                                        $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult = event.target.value;
                                    }
                                    if(testType==1){
                                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult = event.target.value;
                                    }
                                }*/
								
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
								
								
								   if($scope.testMasterResList[testIndex].previousTestResultList.length>0)
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
								if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId==REFERENCE_VALUE){
									
									if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult==undefined || 
											$scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult==null || 
											$scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult== "" ){
										
									}else{
										
										var factor = Math.pow(10, $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].paramPrecision);
										var finalResult = parseFloat($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult);
										var rounded = Math.round(finalResult * factor) / factor;
		                                $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult = rounded.toFixed($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].paramPrecision);
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
																								   }else  if(paramObj.refrangeTypeId==REFERENCE_VALUE){
																									   var factor = Math.pow(10, paramObj.paramPrecision);
		                                                                                                var rounded = Math.round(parseFloat(paramObj.finalResult) * factor) / factor;
		                                                                                                paramObj.finalResult = rounded.toFixed(paramObj.paramPrecision);
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

																  if(labResObj.finalResult==Infinity){
                                                                    labResObj.finalResult= String;
                                                                    labResObj.finalResult = "";
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
								 
								 
								/* var id = null;
								 var resultIndexNew =parseInt(resultIndex)+1; 
								 
								 
									  angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(paramInHeaderObj, headerIndexnew)
											    {
											        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(labResultDetails, resultIndexNewINNN)
											          {
											        	var resultIndexNew =parseInt(resultIndex)+1; 
											        	if(resultIndexNew == resultIndexNewINNN ){
											        	if(($scope.override == true ? false : (labResultDetails.isFormula == 'Y'?(labResultDetails.isEditable == 'N'?true:false):false) )){
											        		var resultIndexNews =parseInt(resultIndexNew)+1; 
											        		 id= 'finalResult_' + testIndex + '_' + headerIndex + '_' + resultIndexNews;
															 var element = document.getElementById(id);
															  console.log("element"+element);
															  // Check if the element is not disabled before setting focus
															  if (element) {
															      element.focus();
															  }
											        	}else{
															 id= 'finalResult_' + testIndex + '_' + headerIndex + '_' + resultIndexNew;
															 var element = document.getElementById(id);
															  console.log("element"+element);
															  // Check if the element is not disabled before setting focus
															  if (element) {
															      element.focus();
															  }
											        	}
											        	}	
											          });
											    });*/
									 
									 
								
								}
							
							  
							
							 $scope.resultReqMsg = "Please enter result."
								 
					$scope.overrideParameter = function(){
							$scope.override = true;
					}
					
							 
					$scope.applyFormula = function(){
						$scope.override = false;
						angular.forEach($scope.testMasterResList, function(testObj, testIndex) 
								{
							
							angular.forEach(testObj.listMultyParamTestHeaderDto, function(headerObj, key) 
									{
									      angular.forEach(headerObj.listLabResultDetailsViewDto, function(labResObj, key) 
											{
									    	   if(labResObj.isFormula=='Y')
									    		   {
									    		  // labResObj.finalResult=null;
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
														  
														  angular.forEach($scope.testMasterResList, function(test, testIndex) 
																	{
														  
														  angular.forEach(test.listMultyParamTestHeaderDto, function(paramInHeaderObj, key)
																    {
																        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(paramObj, key)
																          {
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
													  if(labResObj.finalResult==Infinity || labResObj.finalResult==0){
                                                        labResObj.finalResult= String;
                                                        labResObj.finalResult = "";
                                                     }
													  
									    		   }
										  
										    });
									});
							
								});

						
						
						
					}
					$scope.calculateDeltaPercentage = function(currentResult,previousResult,refrangeTypeId,deltaDaysId,resultValidateDateTime)
					{
						 var deltelPerncetage = "";
						 dt1 = new Date(resultValidateDateTime);
						 dt2 = new Date();
						 $scope.deltaDays =  Math.floor((Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()) - Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate()) ) /(1000 * 60 * 60 * 24));
						 if((currentResult!=null&&currentResult!="")&&(previousResult!=null&&previousResult!=""))
							{
							if(refrangeTypeId==REFERENCE_VALUE)
								{
								  if(deltaDaysId!=null&&deltaDaysId>0 && $scope.deltaDays<=deltaDaysId)
								  {
											
												   if(parseFloat(currentResult) > parseFloat(previousResult))
												   {
												     var increase  = currentResult- previousResult;
												     deltelPerncetage = (parseFloat(increase)/parseFloat(previousResult)) * 100;
												   }
												   else if(parseFloat(previousResult) > parseFloat(currentResult)){
													   var decrease  = previousResult - currentResult ;
													   deltelPerncetage = (parseFloat(decrease)/parseFloat(previousResult)) * 100;
												   }
												   var fixedValue = (parseFloat(deltelPerncetage)).toFixed(2);
												   return fixedValue;
								  }
								  else{
									  return "0";
								  }
								}
							else if(refrangeTypeId==TEXTUAL_RANGE)
							{
								if(deltaDaysId!=null&&deltaDaysId>0 && $scope.deltaDays<=deltaDaysId)
									{
									  var textualCurrentResult = currentResult.trim().toLowerCase();
									  var textualPreviousResult = previousResult.trim().toLowerCase();
										 if(textualCurrentResult===textualPreviousResult)
										 {
										    return 0;
										 }
									 else
									  {
										 return -1
									  }
									} 
								 else{
									  return "0";
								  }
							}
							else if(refrangeTypeId==MULTIVALUED_RANGE)
							{
								if(deltaDaysId!=null&&deltaDaysId>0 && $scope.deltaDays<=deltaDaysId)
								{
									 var mTextualCurrentResult = currentResult.trim().toLowerCase();
									 var mTextualPreviousResult = previousResult.trim().toLowerCase();
									 if(mTextualCurrentResult===mTextualPreviousResult)
										 {
										    return 0;
										 }
									 else{
										 return -1
									 }
									 
								}
								 else{
									  return "0";
								  }
							}
						}
						 else{
							/* growl.error("Something went wrong",
										{
											title : 'Error!'
										});*/
						 }
					}
							 
							 
						 	 
							 
							 
							 $scope.labels = [];
							 $scope.data = [];
							 $scope.paramHistoryList =[];
							 
							  $scope.onClick = function (points, evt) {
							    console.log(points, evt);
							  };
							  $scope.datasetOverride = [{
							        yAxisID: 'y-axis-1'
							    }];
							    $scope.options = {
							        scales: {
							            yAxes: [{
							                id: 'y-axis-1',
							                type: 'linear',
							                display: true,
							                position: 'left'
							            }]
							        },legend: { display: true }
							    };
							 
							    
							    $scope.selectedParameterList=[];
								$scope.getParameterHistory = function() 
								{  
									 $scope.series = [];
									 $scope.labels = [];
									 $scope.data = [];
									 $scope.paramHistoryList =[];
									try {
										
										$rootScope.startLoader();
										var data = 
										         { 
										          "orgUnitId":$scope.orgUnitId,
												   "orgId":$scope.orgId,
												   "patientId":$scope.patientId,
												   "testId":$scope.testId,
												   "limit":$scope.limit,
												   "parameterIds":$scope.selectedParameterList
												 }
										var URI = BASE_URL + ROOT_URL
												+ LIS_BIOCHEMISTRY
												+ MICROBIOLOGY_PARAMETER_HISTORY;
										console.log("PARAMETER HISTORY URI", URI);
										console.log("PARAMETER HISTORY DATA", JSON.stringify(data));
										
										
										
										
										GenericService
												.serviceAction(METHOD_POST, URI, data)
												.then(
														function(response) {
															$rootScope.stopLoader();
															$scope.paramHistoryList  = response.data.listObject;
															angular.forEach($scope.paramHistoryList, function(value, key)
																	{
																     if(value.refrangeTypeId==REFERENCE_VALUE)
																    	 {
																    	   $scope.series.push(value.parameterName);
																    	   var graphResult = [];
																			angular.forEach(value.listParameterResultDto, function(paramResObj, key)
																					{
																				       if(!isNaN(parseFloat(paramResObj.result)))
																				    	   {
																				    	     graphResult.push(parseFloat(paramResObj.result));
																				    	     
																				    	   }
																					});
																			 
																			$scope.data.push(graphResult);
																    	 }
																	});
															angular.forEach($scope.paramHistoryList[0].listParameterResultDto, function(dateObj, key)
																	{
																        $scope.labels.push(moment(dateObj.resultDateTime).format('DD/MM/YYYY'));
																	});
															
															console.log(JSON.stringify($scope.labels));
															console.log(JSON.stringify($scope.data));
															console.log(JSON.stringify($scope.series));
														});
									} catch (e) {
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
																									    		            if(resultObj.paramPrecision != undefined && resultObj.paramPrecision != null && resultObj.paramPrecision != 0 && resultObj.refrangeTypeId == 1 ){
                                                                                                                                var numberValue = parseFloat(value.paramResult);
                                                                                                                                var formattedResult = numberValue.toFixed(resultObj.paramPrecision);
                                                                                                                                resultObj.finalResult = formattedResult;
                                                                                                                            }
                                                                                                                            else{
                                                                                                                                resultObj.finalResult = value.paramResult;
                                                                                                                            }
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
								
								$scope.ConvertQtyOrdered = function (item, uom, uomList) {
						            var newUOM = [];
						            uom.find(function (value) {
						                if (!!value.uomTypeId) {
						                    newUOM.push(value);
						                }
						            });
						            var assignQty = item.assignQty;
						           // var consumedQuantity= item.consumedQuantity!=undefined ?item.consumedQuantity : item.assignQty;
						            var uomDetails = uomList.uomDetails.split('-')[0];
						            if (uomDetails == "P") {
						                item.newAssignQty = Math.ceil(assignQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
						               // item.consumedQuantity = Math.ceil(consumedQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
						                item.availableQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
						            } else if (uomDetails == "S") {
						                item.newAssignQty = Math.ceil(assignQty / (newUOM[1].conversion * newUOM[2].conversion));
						               // item.consumedQuantity = Math.ceil(consumedQuantity / ( newUOM[1].conversion * newUOM[2].conversion));
						                item.availableQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
						            } else if (uomDetails == "D") {
						                item.newAssignQty = Math.ceil(assignQty / (newUOM[2].conversion));
						               // item.consumedQuantity = Math.ceil(consumedQuantity / ( newUOM[2].conversion));
						                item.availableQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
						            }
						            
						            item.consumedQuantity = item.newAssignQty;
						        }

								$scope.batchExpiryMsgShow = function(item){
									var date = moment(new Date()).format("MM");
									var year = moment(new Date()).format("YYYY");
									var dateCal = parseInt(date) + 1;
									var expDate = moment(item.selectedBatch.expiry).format("MM");
									var expYear = moment(item.selectedBatch.expiry).format("YYYY");
									if((dateCal == expDate || date == expDate) && year == expYear){
										growl.warning("Batch No. " + item.selectedBatch.batchNo + " is near expiry");
									}  
								}
								
						        $scope.batchChange = function (index, batch) {
						            var item = null;
						            $scope.itemList[index].uom = batch.uom;
						            item =  $scope.itemList[index];
						            if (!!batch.batchId) {
						                //Select Default UOM
						                item.selectedUom = $filter('filter')(item.uom, {
						                    opUom: true
						                }, true)[0];

						                //"Select UOM" option from UOM dropdown
						                if (!item.selectedUom) {
						                    item.selectedUom = item.uom[0];
						                }
						               
						                item.availableStockQuantity = item.selectedBatch.availableStockQuantity;
						                item.expiry = item.selectedBatch.expiry;
						                //Convert Ordered Quantity
						                $scope.ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);

						            } else{
						                item.selectedUom = item.uom[0];
						                item.consumedQuantity = null;
						            }
						        }
						        
						       $scope.uomChange = function (index, uom ) {
						            var item = null;
						                item = $scope.itemList[index];
						            if (!!uom) {
						                //Convert Ordered Quantity
						            	$scope.ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
						            } 
						        }
						       
						       var ValidateUniqueBatchAndUom = function (item) {
				
						            var isValid = true;
						                item['isParentBatchValid'] = false;
						                item['isParentUOMValid'] = false;
						                var items = $scope.itemList.filter(function (detail) {
						                		
						                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
						                    && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
						                    == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
						                });
						                if (items.length > 1) {
						                    isValid = false;
						                    item['isParentBatchValid'] = true;
						                    item['isParentUOMValid'] = true;
						                    growl.error('There should not be same batch and same UOM for same item');
						                }
						            return isValid;
						        }
						       
						      $scope.validateBatch = function (item) {
						            var isValid = true;
						            if (!item.selectedBatch.batchId) {
						                item['isBatchValid'] = true;
						                growl.error("Please Select Batch");
						                isValid = false;
						            } else {
						                ValidateUniqueBatchAndUom(item)
						            }
						            return isValid;

						        }

						       $scope.validateUOM = function (item) {
						            var isValid = true;
						            if (!item.selectedUom) {
						                item['isUOMValid'] = true;
						                growl.error("Please Select UOM");
						                isValid = false;
						            } else {
						                ValidateUniqueBatchAndUom(item)
						            }
						            return isValid;
						        }
						       
						      $scope.removeItem = function (index) {
						    	  $scope.itemList.splice(index, 1);
						        }
						      
						      $scope.validateQuantity = function(index){
						    	  
						    	  if( $scope.itemList[index].availableQuantity< $scope.itemList[index].consumedQuantity){
						    		  growl.warning("Not Enough Quantity",
												{
													title : 'warning!'
												});
						    		  $scope.itemList[index].consumedQuantity = 0;
						    		  return false;
						    	   }
						      }
						      
						      /******* Add New Item START ******************/
						      
						      $scope.assetCategoryObj = {
										id:"",
										category:""
								}
								
								$scope.productCategoryObj = {
										id:"",
										category:""
								}
								
								$scope.assetCategories = [];
								$scope.getAssetCategoryList = function () {
									var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/1/"+$scope.orgUnitId+"/"+$scope.orgId; //1 for consumable asset type
									GenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.assetCategories = response.data;
										console.log("$scope.assetCategories",$scope.assetCategories);
										
									});
								   }
								
								$scope.getAssetCategoryList();
								
								
								$scope.productCategories = {};
								$scope.getProductCategoryList = function (id) {
									var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+id+"/"+$scope.orgId;
									GenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.productCategories = response.data;
										console.log("$scope.productCategories",$scope.productCategories);
										
									});
								}
								
								$scope.itemObj = {
										itemCode:"",
										itemName:""
								}
								$scope.searchNewItemList = [];
								$scope.searchNewItem = function () {
									var data = {
											 	"assetTypeId": 1,
										        "assetCategoryId": $scope.assetCategoryObj.id,
										        "productCategoryId": $scope.productCategoryObj.id,
										        "itemCode": $scope.itemObj.itemCode,
										        "itemName": $scope.itemObj.itemName
											};
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/global/items/getItemList/onTemplet";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$rootScope.stopLoaderPopup();
										$scope.searchNewItemList = response.data;
										console.log("$scope.searchNewItemList",$scope.searchNewItemList);
									});
							}
								
								$scope.isItemChecked=true;
									$scope.isItemExist=false;
									$scope.itemAdded = false;
									$scope.addItems = function(){
										$rootScope.startLoaderPopup();
										if($scope.searchNewItemList!=null  && $scope.searchNewItemList!=undefined && $scope.searchNewItemList.length>0){
										 var found = $scope.searchNewItemList.find(ar=>ar.isSelected);
	                                       if(found==undefined)
	                                           {
	                                    	   growl.warning("Please Select Item ",
														{
															title : 'warning!'
														});
	                                    	   $rootScope.stopLoaderPopup();
											   return false;
	                                           }
										}else{
											 growl.warning("Please Select Item ",
														{
															title : 'warning!'
														});
	                                    	   $rootScope.stopLoaderPopup();
											   return false;
										}
										
										$scope.isItemExist=false;
										$scope.isItemCheck=false;
										$scope.itemAdded = false;
										$scope.itemIds = [];
									    angular.forEach($scope.searchNewItemList,function(value,index) {
											if(value.isSelected){

												if($scope.itemList.length>0){
													angular.forEach($scope.itemList,function(value1,index1) {
														if(value1.itemId == value.itemId ){
															$scope.isItemCheck = true;
														}
													});

												}
												}
											});
										if($scope.isItemCheck){
										growl.warning("Selected Item Already Exist",
												{
													title : 'warning!'
												});
										$rootScope.stopLoaderPopup();
										return false;
									}
										
										angular.forEach($scope.searchNewItemList,function(value,index) {
											if(value.isSelected){
												
												if($scope.itemList.length>0){
													$scope.isItemExist=false;
													angular.forEach($scope.itemList,function(value1,index1) {
														if(value1.itemId == value.itemId ){
															$scope.isItemExist = true;
														}
													});
													
													if (!$scope.isItemExist){
														var newArr = {};
														newArr.itemId = value.itemId;
														newArr.selectedBatch = undefined;
														newArr.batchList = [];
														newArr.selectedUom = undefined;
														newArr.itemName = value.itemName;
														newArr.itemCode = value.itemCode;
														newArr.availableQuantity = 0;
														newArr.assignQty = 0;
														newArr.newAssignQty = 0;
														newArr.consumedQuantity = 0;
														$scope.itemList.push(newArr);
														
														var arr = {};
														arr.itemId =  value.itemId;
														$scope.itemIds.push(arr);
													}
												} else{
                                                    var newArr = {};
                                                    newArr.itemId = value.itemId;
                                                    newArr.selectedBatch = undefined;
                                                    newArr.batchList = [];
                                                    newArr.selectedUom = undefined;
                                                    newArr.itemName = value.itemName;
                                                    newArr.itemCode = value.itemCode;
                                                    newArr.availableQuantity = 0;
                                                    newArr.assignQty = 0;
                                                    newArr.newAssignQty = 0;
                                                    newArr.consumedQuantity = 0;
                                                    $scope.itemList.push(newArr);
                                                    
                                                    var arr = {};
                                                    arr.itemId =  value.itemId;
                                                    $scope.itemIds.push(arr);
												}
											}
										});
										
										if($scope.itemIds.length>0){
											var data = {
												 	"itemIds": $scope.itemIds,
												 	"unitId" :	$scope.orgUnitId ,
													"organizationId" : $scope.orgId,
													"storeId" : $scope.storeId
												};
											$scope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/billing/resultEntry/getBatchDetailsByItemList";
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											$scope.stopLoader();
											if(response.data.listObject!=null && response.data.listObject.length>0){
												angular.forEach(response.data.listObject,function(value,index) {
													angular.forEach($scope.itemList,function(value1,index1) {
														if(value1.itemId == value.itemId ){
															if(value.batchReq == true){
																value1.batchList = value.batchList;	
																value1.batchReq = value.batchReq;
															}else{
																value1.batchReq = value.batchReq;
																value1.selectedBatch = {};
																value1.selectedBatch.batchId = value.batchList[0].batchId;
																value1.selectedUom = {};
																value1.selectedUom.uomTypeId = value.batchList[0].uom[2].uomTypeId;
																value1.selectedUom.uomUnitId = value.batchList[0].uom[2].uomUnitId;
																value1.availableQuantity = value.batchList[0].availableStockQuantity;
															}
														}
													});
												});
											}
											$rootScope.stopLoaderPopup();
											 var popup = angular.element('#addNewItem');
										      popup.modal('hide');
										});
										}
										
										$scope.searchNewItemList = [];
										$scope.assetCategoryObj="";
										$scope.productCategoryObj= "";
										$scope.selectAllRows = false;
										$scope.stopLoader();
									}
									
									$scope.removeItems = function(indx){
										
										if($scope.selectedItemList[indx].procedureConfigInventoryItemId!=null){
											$scope.selectedItemList[indx].status="I";
											}else{
											$scope.selectedItemList.splice(indx,1);
										}
									}
									
									$scope.selectAllRows = false;
								   $scope.selectAllItems = function () {
								        if ($scope.selectAllRows) {
								        	  angular.forEach($scope.searchNewItemList,function(value,index) {
								        		  value.isSelected = true;
								            });
								      }else{
								    	  angular.forEach($scope.searchNewItemList,function(value,index) {
							        		  value.isSelected = false;
							            });
								      }
								   }
								   $scope.closeItemPopup = function(){
									   $scope.selectAllRows = false;
									   var popup = angular.element('#addNewItem');
									      popup.modal('hide');
								   }
								   $scope.checkSelectedAll = function(){
									   var count = 0;
									   angular.forEach($scope.searchNewItemList,function(value,index) {
							        		  if(value.isSelected){
							        			  count = count +1;
							        		  }
							            });
									   if(count == $scope.searchNewItemList.length){
										   $scope.selectAllRows = true;
									   }else{
										   $scope.selectAllRows = false;
									   }
								   }
								/*********************Add New Item END *********************/

								   $scope.goToBack = function(){
									   
									   if( $scope.searchObjfromAmend.amend == undefined){
											
											if($scope.consolidatedResultEntry!='Y'){
												$state.go('worklistForReportEntry',{subDeptId : subDeptId,searchObj : $stateParams.searchObj});
											}else{
												
												if($stateParams.searchObj.selectedVisitType !== undefined && $stateParams.searchObj.selectedVisitType!== null && $stateParams.searchObj.selectedVisitType === 4){
												    $stateParams.searchObj.selectedVisitType = [];
												    $stateParams.searchObj.selectedPatient = null;
												    $stateParams.searchObj.patientId = undefined;
												    $stateParams.searchObj.uhid = '';
												}
												 $state.go('consolidatedResultEntry',
															{
															  searchObj : $stateParams.searchObj
															});
											
											}
										
										}else{
											$state.go('amendmentWorklist',
													{
													  searchObj : $stateParams.searchObj
													});
										}
								   }
								   
								   $scope.nextRecordIndex=0;
								   $scope.cnt  = 0;
								   
								   $scope.loadNextRecord = function(){
								   if ($scope.worklistForReportEntryForm.$valid)
								   { 
									 //check Billable consumption is valid or not
                                       if($scope.itemList!=null && $scope.itemList.length>0){
                                       	
                                       	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedBatch == undefined || obj.selectedBatch.batchId == undefined )})
											 if(isValidBatch!=undefined){
												 
		                                            growl.warning("Please Select Batch Of All Items",
		                                                        {
		                                                            title : 'warning'
		                                                        });
		                                            $scope.saveOrNextButton = false;
		                                                        return false;
											 } 
                                       	
                                       	var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedUom == undefined || obj.selectedUom.uomTypeId == undefined )})
											 if(isValidBatch!=undefined){
												 
		                                            growl.warning("Please Select UOM Type Of All Items",
		                                                        {
		                                                            title : 'warning'
		                                                        });
		                                            $scope.saveOrNextButton = false;
		                                                        return false;
											 } 
                                       	
                                       	var isValidBatch = $scope.itemList.find(obj =>{return (obj.consumedQuantity == undefined || obj.consumedQuantity == 0 )})
											 if(isValidBatch!=undefined){
												
		                                            growl.warning("Please Enter Consumed Quantity",
		                                                        {
		                                                            title : 'warning'
		                                                        });
		                                            $scope.saveOrNextButton = false;
		                                                        return false;
											 } 
										}
  									   $scope.saveOrNextButton = true;
									   if($scope.cnt  == 0){
										   $scope.nextRecordIndex=0;   
										   $scope.cnt  = $scope.cnt +1;
									   	}else{
										   	$scope.nextRecordIndex = $scope.nextRecordIndex +1;
										   	$scope.cnt  = $scope.cnt +1;
									   	}
									   	$scope.saveResultEntryDetails(false,false,false)   ;
									   
								   	} else{
								   			growl.error("Please Fill All Mandatory Fields.",
												{
													title : 'Error!'
												});
											$scope.saveOrNextButton = false; 
								   		}
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
													   "genderId" : $scope.labResultMasterList[0].genderId,
													   "patientAgeDays" :  $scope.labResultMasterList[0].patientAgeDays,
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
									   
									   //upload document
									   $scope.initLabReportDocObj = function(){
										   $scope.labReportDocObj = {
												   labReportDocDtlsId : "",
												   labSampleDtlsId : "",
												   testId:"",
												   filePath : "",
												   status : 'A',
												   createdDate : "",
												   createdBy : $scope.userId,
												   updatedDate : "",
												   updatedBy : $scope.userId,
												   unitId : $scope.unitId,
												   organizationId : $scope.orgId,
												   labSampleDtlsId : "",
												   file : {},
												   testObj : {}  
											}
									   }
									   $scope.initLabReportDocObj();
									   
									   $scope.labReportDocumentDtlsList = [];
									   
									   $scope.labReportDocumentDtlsSavedList = [];
									   $scope.labSampleDtlsIds = [];
									   $scope.getLabReportDocumentList = function () {
											$scope.testIds = [];
											var groupBy = function(xs, key) {
												  return xs.reduce(function(rv, x) {
												    (rv[x[key]] = rv[x[key]] || []).push(x);
												    return rv;
												  }, {});
												};
										var labSampleDtlsListTemp = groupBy($scope.testMasterResList, 'labSampleDtlsId');
										angular.forEach(labSampleDtlsListTemp,function(value,key){
											$scope.labSampleDtlsIds.push(parseInt(key));
										});
											var data = {
													 	"orgId": $scope.orgId,
												        "orgUnitId": $scope.unitId,
												        "labSampleDtlsIds": $scope.labSampleDtlsIds,
													};
											$rootScope.startLoaderPopup();
											var URI = BASE_URL + ROOT_URL + "/LIS/transaction/getLabReportDocumentDetailsByTest";
											GenericService.serviceAction("POST", URI, data).then(function(response) {
												$rootScope.stopLoaderPopup();
												 $scope.labReportDocumentDtlsSavedList = [];
												$scope.labReportDocumentDtlsSavedList = response.data.listObject;
											});
									}
									   
									   $scope.getUploadedDocuments = function(){
										   $scope.labReportDocumentDtlsList = [];
										   $scope.initLabReportDocObj();
										   $scope.getLabReportDocumentList();
										   if(document.getElementById('file11')!=null){
											   	document.getElementById('file11').value = ""; 
											}
										   $scope.labReportDocumentDtlsList.push($scope.labReportDocObj);
										   $scope.counterDocSave = 0;
                                           $scope.saveBtnClickCount = 0;
                                           $scope.uploadDocBtn = false;
										   $('#uploadDocumentModal').modal('show');
									   }
								   
									   $scope.addImageDetails = function(index)
										{ 
										   if($scope.labReportDocumentDtlsList[index].testObj == undefined || $scope.labReportDocumentDtlsList[index].testObj == null
												   || $scope.labReportDocumentDtlsList[index].testObj.testId == undefined || $scope.labReportDocumentDtlsList[index].testObj.testId ==null ){
											   growl.warning("Please Select Test",
														{
															title : 'warning!'
														});
												$rootScope.stopLoaderPopup();
												return false;
										   }
										   
										   if($scope.labReportDocumentDtlsList[index].file == undefined || $scope.labReportDocumentDtlsList[index].file == null
												   || angular.equals({}, $scope.labReportDocumentDtlsList[index].file)){
											   growl.warning("Please Select File",
														{
															title : 'warning!'
														});
												$rootScope.stopLoaderPopup();
												return false;
										   }
										   $scope.labReportDocumentDtlsList[index].testId = $scope.labReportDocumentDtlsList[index].testObj.testId;
										   $scope.labReportDocumentDtlsList[index].labSampleDtlsId = $scope.labReportDocumentDtlsList[index].testObj.labSampleDtlsId;
										   $scope.initLabReportDocObj();
											$scope.labReportDocumentDtlsList.push($scope.labReportDocObj);
										}
									   
									   $scope.removeImageDetails = function(deleteIndex)
										{ 
										   $scope.labReportDocumentDtlsList.splice(deleteIndex, 1);
									    }	

									   $scope.counterDocSave = 0;
									   $scope.uploadLabReportDoc = function(fileOb){
                                            if($scope.counterDocSave < $scope.labReportDocumentDtlsList.length){
                                                  $('#uploadDocumentModal').modal('hide');
                                                   var fd = new FormData();
                                                      fd.append('testId', fileOb.testId);
                                                      fd.append('createdBy',fileOb.createdBy );
                                                      fd.append('updatedBy', fileOb.updatedBy);
                                                      fd.append('unitId', fileOb.unitId);
                                                      fd.append('organizationId', fileOb.organizationId);
                                                      fd.append('labSampleDtlsId', fileOb.labSampleDtlsId);
                                                      fd.append('file',fileOb.file );
                                                      var URI1 = BASE_URL + ROOT_URL + "/LIS/transaction/uploadLabReportDocument";
                                                      return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                                      {
                                                          $scope.counterDocSave ++;
                                                          return response;
                                                      });
                                            }


									   }


									   $scope.delayedUploadDoc = _.debounce(function() {
                                           $scope.uploadDoc();
                                           resetButtonClickState();
                                       }, 300);

                                       function resetButtonClickState() {
                                           $scope.saveBtnClickCount = 0;
                                           $scope.counterDocSave = 0;
                                           $scope.uploadDocBtn = false;
                                       }

                                        $scope.saveBtnClickCount = 0;
									   $scope.uploadDocBtn = false;
									   $scope.uploadDoc = function()
									    {
									        $scope.saveBtnClickCount ++;
                                            $scope.uploadDocBtn = true;

									        if($scope.saveBtnClickCount > 1 && $scope.saveBtnClickCount != 0){
									            $scope.saveBtnClickCount = 0;
									             $scope.uploadDocBtn = false;
									            return false;
									        }


										   var isTestNameEmpty = false;
										   var isFileFormatInvalid = false;
										   var isFileEmpty = false;
										 $rootScope.startLoader();
										  angular.forEach($scope.labReportDocumentDtlsList, function(value, key) {  
											  if(value.testObj== null || value.testObj == undefined || value.testObj.testId==undefined || value.testObj.testId=="" || value.testObj.testId==null){
												  isTestNameEmpty = true;
												}
												  if (angular.equals(value.file, {}) || value.file==undefined) {
													  isFileEmpty = true;
													} 
												  if(value.file.name!=undefined){
													  var ext = value.file.name.split(".").pop().toLowerCase();
														if($.inArray(ext, ["png","jpg",'gif','jpeg']) == -1) {
															isFileFormatInvalid =true;
														}
												  }
										});
										  
										  
										  if(isTestNameEmpty){
										        $scope.uploadDocBtn = false;
										        $scope.saveBtnClickCount = 0;
											  growl.warning("Please select Test .",
														{
															title : 'warning!'
														});
											   $rootScope.stopLoader();
											   return false;
										  }
										  if(isFileEmpty){
										        $scope.uploadDocBtn = false;
										        $scope.saveBtnClickCount = 0;
											  growl.warning('Please select file.',
														{
															title : 'warning!'
														});
											  $rootScope.stopLoader();
											  return false;
										  }
										  
										  if(isFileFormatInvalid){
										        $scope.uploadDocBtn = false;
										        $scope.saveBtnClickCount = 0;
											  growl.warning('You can only upload *.png, *.jpg, *.gif ,*.jpeg file',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
										  }
										  $scope.count = 0;
										 angular.forEach($scope.labReportDocumentDtlsList, function(value, key) 
											{  
											 value.labSampleDtlsId = value.testObj.labSampleDtlsId;
												value.testId  = value.testObj.testId;
												var promise = $scope.uploadLabReportDoc(value);
												promise.then(function(response) 
														{
														  if(response.data.status == "success")
															  {
															    $scope.uploadDocBtn = false;
															    $scope.saveBtnClickCount = 0;
															    $scope.count++;
															    if($scope.labReportDocumentDtlsList.length==$scope.count)
																 {
																   growl.success("Document Uploaded Successfully.",
																			{
																				title : 'Success!'
																			});
																   $rootScope.stopLoader();
																   if(document.getElementById('file11')!=null){
																	   	document.getElementById('file11').value = ""; 
																	 }
																   $scope.labReportDocumentDtlsList = [];
																   $scope.initLabReportDocObj();
																 }
														      }
														  else 
															{
															    $scope.uploadDocBtn = false;
															    $scope.saveBtnClickCount = 0;
																growl.error('Something went wrong.',
																		{
																			title : 'Error!'
																		});
																 $rootScope.stopLoader();
															}
													    });
													
											});
										
									    }

									    $scope.closeUploadDocPopup = function(){
									        $scope.counterDocSave = 0;
									        $scope.saveBtnClickCount = 0;
                                           $scope.uploadDocBtn = false;

									    }

									   $scope.reCollectBtnCount = 0;
									   $scope.showRejecPopup = function(isAccRej,index)
			                              {
                                            if($scope.reCollectBtnCount == 0){
                                                $scope.reCollectBtnCount = 1;
											//if(isAccRej=='R')
											//{
											  $scope.rejIndex = index;
											  $scope.sampleRejectReasonId = "";
												 $scope.sampleRejectReason = ""
											  $("#accptRejInPatient").modal('show');
										    //}
										    }
										}
									   
									  /* $scope.saveRejectDetails = function(){
											
											if($scope.sampleRejectReasonId == ""){
												
												growl.warning("Please select reason..!!",
														{
															title : 'warning!'
														});
												return false;
												
											} else if($scope.sampleRejectReason == "")
											{
												growl.warning("Please Enter reason note..!!",
														{
															title : 'warning!'
														});
												return false;
												
											} else {
												
												$scope.reCollectResultEntryDetails();
												return false;
											}
										}*/
									   $scope.getRejectionList = function(orgId) {
											try {
												var data = {
														organizationId :orgId
													};
												var URI = BASE_URL + ROOT_URL
												+ GETACTIVEREASONLIST;
												GenericService
												.serviceAction(METHOD_POST, URI,
														data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
																function(response) {
																	$scope.rejectionList = response.data.listObject;
																});
											} catch (e) {
												console.log(e.message)
											}

										}
										$scope.getRejectionList($scope.orgId);
										
										$scope.checkFlagOnHoldTest=false;
										
										$scope.checkFlagOnHold=function(info){
											if(info.isOnHoldParameter == true){
												$scope.checkFlagOnHoldTest=true;
												$scope.isOnHoldParameter=true;
											}else{
												$scope.checkFlagOnHoldTest=false;
												$scope.isOnHoldParameter=false;

											}
										}
										
										
									  
						} ]);
