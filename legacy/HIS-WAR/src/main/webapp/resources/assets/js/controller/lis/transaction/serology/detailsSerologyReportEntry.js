/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"detailsSerologyReportEntry",
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
						'$filter',
						'CONSTANTS',
						'GenericServiceFileUpload',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams,$filter,CONSTANTS,GenericServiceFileUpload,reportService) {

							
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
							
							$scope.patientDetails = {
									"patientId"	  : "",
									"patientName" : "",
									"genderCode"  : "",
									"age"         : "", 
									"uhIdNumber"  : "",
									"birthDate"	  : "",
									"encounterId" : "",
									"genderId":""
							}
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.storeId = cookieObject.storeId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = labConfig.serologyId;
							$scope.isValidationAndRelease = labConfig.isValidationAndRelease;
							$scope.skipValidation= labConfig.skipValidation;
							$scope.consolidatedResultEntry =labConfig.consolidatedResultEntry;
							$scope.saveOrNextButton = false;
							$scope.triggerReleaseMail = labConfig.triggerReleaseMail;
							$scope.sendMailOnOrderCompletion = labConfig.sendMailOnOrderCompletion;
							
							$('[data-toggle="deltatooltip"]').tooltip();
							
							  $scope.selectedTechniqueList = [];
							  $scope.techniqueList = [];
							  $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    }; 
							
							
							  $scope.serologyTestResultMapperList = [];
							  $scope.flagCheckOnHoldParam=false;
							  $scope.serologyResultMasterList=[];
							  $scope.serologyResultMaster = {
									  "serologyTestResultId" :'',
									  "labSampleDtlsId" : "",
									  "resultEnterBy" : $scope.userId,
									  "resultEnterDate" : "",
									  "resultValidatedBy" : "",
									  "resultValidatedDate" : "",
									  "reportReleaseBy" : "",
									  "reportReleaseDate" : "",
									  "createdDate" : "",
									  "createdBy" : $scope.userId,
									  "updatedDate" : "",
									  "updatedBy" : $scope.userId,
									  "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "serologyResultTestMapperDtoList" : $scope.serologyTestResultMapperList,
									  "isOnHoldParameter":false,
									}

							  
							  
								$scope.getTechniqueList = function() {
									try {
										var data = "";
										var URI = BASE_URL + ROOT_URL
												+ LIS_COMMON + GET_TECHNIQUE_LIST
												+ S + $scope.orgId;
										GenericService
												.serviceAction("GET", URI, data)
												.then(function(response) 
														{
																if (response.data.status == 'success')
																{
												                  $scope.techniqueList = response.data.listObject;
																}
												            	else if(response.data.status == 'error')
												            		{
												            		growl.error("No test Founds.",
																			{
																				title : 'Error!'
																			});
												            		}
														});
									} catch (e) {
										console.log(e.message)
									}
								}
								$scope.getTechniqueList();
								$scope.testParameterObj = [];
								 $scope.flagCheckOnHoldParam=false;

								$scope.getTestParameterList = function() {
									try {
                                        $rootScope.startLoader();
										var data = {};
										var data1 = {};
										
										angular.forEach($scope.serologyDtoList,function(value,key){
								        	     data[value.labSampleDtlsId]=value.testId; 
								        	     $scope.isOnHoldParameter=value.isOnHoldParameter;
												});
										if($scope.isOnHoldParameter == true || ($scope.amendSearchObj.amend != undefined &&  $scope.amendSearchObj.amend == true )){
											angular.forEach($scope.serologyDtoList,function(value,key){
												if(value.labSampleDtlsId != null){
													  data1.labSampleDtlsId=value.labSampleDtlsId;
										        	     $scope.isOnHoldParameter=value.isOnHoldParameter;	
												}
								        	   
												});
											var URI = BASE_URL+ ROOT_URL+ "/LIS/unit/getOnHoldReportEntryDetail";
											
											GenericService
											.serviceAction("POST", URI, data1)
											.then(function(response) 
													{
															if (response.data.status == 'success')
															{
																$scope.testParameterList  = [];
											                  $scope.testParameterList = response.data.listObject;
											                  
											                  
											                  angular.forEach($scope.testParameterList,function(value,key){
											                	 
											                	  var labResObj = $scope.serologyDtoList.find(obj => {
																	  return obj.labSampleDtlsId == value.labSampleDtlsId
																	})
																	
																	value.isSelected = true;
																	value.testDesc =labResObj.testDesc;
																	value.testCode =labResObj.testCode; 
																	value.sampleNo = labResObj.sampleNo;
																	value.panelCode = labResObj.panelCode;
																	 if($scope.isOnHoldParameter == true){
		                                                                    $scope.flagCheckOnHoldParam=true;
		                                                                }else{
		                                                                	$scope.flagCheckOnHoldParam=false;
		                                                                }
																		value.isOnHoldParameter = $scope.isOnHoldParameter;
																	value.orderNo = labResObj.orderNo;
											                	  angular.forEach(value.listTestParamMppr,function(value1,key){
											                		   value1.tempList = [];	 
												                	  var newArr = {
																			  "techniqueId" :value1.techniqueId!=null ? value1.techniqueId:"",
																			  "parameterId" :value1.parameterId !=null ? value1.parameterId :"",
																			  "findingResult" : value1.findingResult !=null ? value1.findingResult :"",
																			  "findingResultDesc" :value1.findingResult !=null ? value1.findingResultDesc:"",
																			  "firstFinding" : value1.firstFinding !=null ? value1.firstFinding :"",
																			  "firstFindingDesc" : value1.firstFindingDesc !=null ? value1.firstFindingDesc :"",
																			  "secondFinding" : value1.secondFinding !=null ? value1.secondFinding :"",
																			  "secondFindingDesc" :value1.secondFindingDesc !=null ? value1.secondFindingDesc:"",
																			  "thiredFinding" :value1.thiredFinding !=null ? value1.thiredFinding:"",
																			  "thirdFindingDesc" :value1.thirdFindingDesc !=null ? value1.thirdFindingDesc:"",
//																			  "parameterDesc" :value1.parameterName!=null ? value1.parameterName:"",

																			  "isAbnormal" : 'N',
																					
																	  };
												                	  if(value1.listHelpValueMaster!=null){
												                		  var findingResultObj = value1.listHelpValueMaster.find(obj => {return obj.isDefault});
												                		  if(findingResultObj!=undefined){
												                			  newArr.firstFinding = findingResultObj.helpValue;
												                			  newArr.isAbnormal = findingResultObj.isAbnormal;
												                		  }else{
												                		  	 newArr.isAbnormal = 'N';
												                		  }
												                	  }else{
												                		  newArr.isAbnormal = 'N';
												                	  }
												                	  value1.tempList.push(newArr);
												                	  
																	});
											                	  
											                	  if(labResObj.paymentEntitlementId!=null && labResObj.paymentEntitlementId == 2){
											            			  value.insuranceId = labResObj.payeeId;
											            			  value.companyId = null;
																     }else if(labResObj.paymentEntitlementId!=null && labResObj.paymentEntitlementId == 3){
																    	 value.companyId = labResObj.payeeId;
																    	 value.insuranceId = null;
																     }else{
																    	 value.companyId = null;
																    	 value.insuranceId = null;
																     }
											            		  
																});
											                 
                                                              $rootScope.stopLoader();
															}
											            	else if(response.data.status == 'error')
											            		{
											            		growl.error("No test Founds.",
																		{
																			title : 'Error!'
																		});
                                                                $rootScope.stopLoader();
											            		}
													});
										}else{
											var URI = BASE_URL + ROOT_URL
											+ "/LIS/unit/getParameterTestOnReportEntry";
											
											GenericService
											.serviceAction("POST", URI, data)
											.then(function(response) 
													{
															if (response.data.status == 'success')
															{
																$scope.testParameterList  = [];
											                  $scope.testParameterList = response.data.listObject;
											                  
											                  
											                  angular.forEach($scope.testParameterList,function(value,key){
											                	 
											                	  var labResObj = $scope.serologyDtoList.find(obj => {
																	  return obj.labSampleDtlsId == value.labSampleDtlsId
																	})
																	
																	value.isSelected = true;
																	value.testDesc =labResObj.testDesc;
																	value.testCode =labResObj.testCode; 
																	value.sampleNo = labResObj.sampleNo;
																	value.panelCode = labResObj.panelCode;
																	 if($scope.isOnHoldParameter == true){
		                                                                    $scope.flagCheckOnHoldParam=true;
		                                                                }else{
		                                                                	$scope.flagCheckOnHoldParam=false;
		                                                                }
																		value.isOnHoldParameter = $scope.isOnHoldParameter;
																	value.orderNo = labResObj.orderNo;
											                	  angular.forEach(value.listTestParamMppr,function(value1,key){
											                		   value1.tempList = [];	 
												                	  var newArr = {
																			  "techniqueId" :value1.techniqueId!=null ? value1.techniqueId:"",
																			  "parameterId" :value1.parameterId,
																			  "findingResult" : "",
																			  "findingResultDesc" : "",
																			  "firstFinding" : "",
																			  "firstFindingDesc" : "",
																			  "secondFinding" : "",
																			  "secondFindingDesc" : "",
																			  "thiredFinding" : "",
																			  "thirdFindingDesc" : "",  
																	  };
												                	  if(value1.listHelpValueMaster!=null){
												                		  var findingResultObj = value1.listHelpValueMaster.find(obj => {return obj.isDefault});
												                		  if(findingResultObj!=undefined){
												                			  newArr.firstFinding = findingResultObj.helpValue;
												                			  newArr.isAbnormal = findingResultObj.isAbnormal;
												                		  }else{
												                		  	 newArr.isAbnormal = 'N';
												                		  }
												                	  }else{
												                		  newArr.isAbnormal = 'N';
												                	  }
												                	  value1.tempList.push(newArr);
												                	  value1.desc = "";
																	});
											                	  
											                	  if(labResObj.paymentEntitlementId!=null && labResObj.paymentEntitlementId == 2){
											            			  value.insuranceId = labResObj.payeeId;
											            			  value.companyId = null;
																     }else if(labResObj.paymentEntitlementId!=null && labResObj.paymentEntitlementId == 3){
																    	 value.companyId = labResObj.payeeId;
																    	 value.insuranceId = null;
																     }else{
																    	 value.companyId = null;
																    	 value.insuranceId = null;
																     }
											            		  
																});
											                 
                                                              $rootScope.stopLoader();
															}
											            	else if(response.data.status == 'error')
											            		{
											            		growl.error("No test Founds.",
																		{
																			title : 'Error!'
																		});
                                                                $rootScope.stopLoader();
											            		}
													});
								} 
										}catch (e) {
											console.log(e.message)
										}
										
										
								}
								
								//get Consumption Item List
	                            $scope.itemList = [];
	                              $scope.getInventoryItemList = function(serviceMasterId){
	                                  $rootScope.startLoader();
	                                  var data = {
	                                          serviceIds : serviceMasterId,
	                                            organizationId : $scope.orgId,
	                                            unitId : $scope.orgUnitId
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
	                              
	                              
								
								/**Redirect Page Code*/
								if($stateParams.resultMstList!=null  && $stateParams.resultMstList.length>0)
	 							{
									$scope.serologyDtoList = $stateParams.resultMstList;
	                                $scope.nextRecordList = $stateParams.nextRecordList;
	                                $scope.amendSearchObj = $stateParams.searchObj;
									$scope.getTestParameterList();
								} else 
								{
									$state.go('reportEntry',{searchObj : $stateParams.searchObj});
								}
								
							
						
							$scope.addTechniq = function(testIndex,tech,info,parentIndex,index){
                                $rootScope.startLoader();
                                if($scope.testParameterList[testIndex].listTestParamMppr[parentIndex].tempList[index].techniqueId==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Select Technique",
                                            {
                                                title : 'warning!'
                                            });
                                }else if($scope.testParameterList[testIndex].listTestParamMppr[parentIndex].tempList[index].firstFinding==""){
                                    $rootScope.stopLoader();
                                    growl.warning("Please Enter Result",
                                            {
                                                title : 'warning!'
                                            });
                                }else{
                                   
								var newArr={ 
											"techniqueId" : info.techniqueId!=undefined ? info.techniqueId : "",
										  "parameterId" :info.parameterId,
										  "findingResult" : "",
										  "findingResultDesc" : "",
										  "firstFinding" : "",
										  "firstFindingDesc" : "",
										  "secondFinding" : "",
										  "secondFindingDesc" : "",
										  "thiredFinding" : "",
										  "thirdFindingDesc" : ""  };
								
								 $scope.testParameterList[testIndex].listTestParamMppr[parentIndex].tempList.push(newArr);
								 $rootScope.stopLoader();
                                }
							}
							
							$scope.removeTechniq = function(parentIndex,childIndex,index){
								if($scope.testParameterList[parentIndex].listTestParamMppr[childIndex].tempList.length>1){
									$scope.testParameterList[parentIndex].listTestParamMppr[childIndex].tempList.splice(index,1);
								}
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
                            $scope.provisional = false;
                            $scope.saveBtnDisabled = false;
                        //    $scope.isRedirect = false;
							$scope.saveDetailsSerologyReportEntry = function(isRedirect,provisionalRelease,isonH){
									try 
									{ 	var finalFlag = false;
										$scope.provisional = provisionalRelease;
										
										if((isonH != undefined && isonH == true)){
											$scope.isOnHoldParameter = true;
										}else{
											$scope.isOnHoldParameter = false;
										}
										
										$scope.serologyResultMasterList = [];
										angular.forEach($scope.testParameterList, function(value, key)
												{  
										            var flag=false;
													var flag1=false;
													var serologyTestResultMapperList = [];
													angular.forEach(value.listTestParamMppr, function(value1, key1)
													{
														angular.forEach(value1.tempList, function(value2, key2)
																{
																	if(value1.parameterId ==value2.parameterId ||  $scope.isOnHoldParameter == true){
																		value2.firstFindingDesc =value1.desc;
																		value2.serologyResultTestMpprId =value1.serologyResultTestMpprId;
																	}
																	if(value2.techniqueId!="" ||  $scope.isOnHoldParameter == true){
																		serologyTestResultMapperList.push(value2);
																	}
																});
													});
													var count=0;
													angular.forEach(value.listTestParamMppr, function(value3, key3){
																count=0;
																angular.forEach(value3.tempList, function(value4, index4)	{
																	
																	if(value4.techniqueId!="" && value4.firstFinding==""){
																		 flag=true;
																	}else if(value4.techniqueId=="" && value4.firstFinding=="" && count==0 && value3.parameterId ==value4.parameterId){
																		
																		flag1=true;
																	}
																	
																});
																count++;
															});
											 if(flag && $scope.isOnHoldParameter == false){
												
												 $rootScope.stopLoader();
													growl.warning("Please select Result",
															{
																title : 'warning!'
															});
													
													finalFlag = true;
													$scope.saveOrNextButton = false;
													$scope.saveBtnDisabled = false;
													return false;
											 }else if(flag1 && $scope.isOnHoldParameter == false){
												 $rootScope.stopLoader();
													growl.warning("Please select Techniques of all Parameters",
															{
																title : 'warning!'
															});
													finalFlag = true;
													$scope.saveOrNextButton = false;
													$scope.saveBtnDisabled = false;
													return false;
											 }
											 else{
										if(serologyTestResultMapperList.length>0 ||  $scope.isOnHoldParameter == true )
											{
											 $scope.serologyResultMaster.serologyResultTestMapperDtoList = serologyTestResultMapperList;
											 $scope.serologyResultMaster.labSampleDtlsId = value.labSampleDtlsId
											 $scope.serologyResultMaster.testDesc = value.testDesc;
											 $scope.serologyResultMaster.isOnHoldParameter=$scope.isOnHoldParameter;
											 $scope.serologyResultMaster.orderNo = value.orderNo;
											  if($scope.triggerReleaseMail == 'Y'){
												  $scope.serologyResultMaster.triggerReleaseMail = true;
											  }else{
												  $scope.serologyResultMaster.triggerReleaseMail = false;
											  }
											  if($scope.sendMailOnOrderCompletion == 'Y'){
	                                              $scope.serologyResultMaster.sendMailOnOrderCompletion = true;
	                                          }else{
	                                              $scope.serologyResultMaster.sendMailOnOrderCompletion = false;
	                                          }
											  $scope.serologyResultMaster.companyId = value.companyId;
											  $scope.serologyResultMaster.insuranceId = value.insuranceId;
											  $scope.serologyResultMaster.internalNotes = value.internalNotes;
											  $scope.serologyResultMaster.isReportRecall = false;
											 if($scope.amendSearchObj.amend != undefined &&  $scope.amendSearchObj.amend == true ){
												 $scope.serologyResultMaster.serologyTestResultId = null;
												 $scope.serologyResultMaster.amend = true;
											 }else{
												 $scope.serologyResultMaster.serologyTestResultId = value.serologyTestResultId;
												 $scope.serologyResultMaster.amend = false;
											 }
											 
											  $scope.serologyResultMaster.provisional = $scope.provisional;
											  
											  if($scope.isValidationAndRelease == 'Y' ){
												  $scope.serologyResultMaster.sampleStatusId = 8;
												}
											  
											 $scope.serologyResultMasterList.push($scope.serologyResultMaster);
											 $scope.serologyTestResultMapperList=[];
											  $scope.serologyResultMaster = {
													  "serologyTestResultId" :'',
													  "labSampleDtlsId" : "",
													  "resultEnterBy" : $scope.userId,
													  "resultEnterDate" : "",
													  "resultValidatedBy" : "",
													  "resultValidatedDate" : "",
													  "reportReleaseBy" : "",
													  "reportReleaseDate" : "",
													  "createdDate" : "",
													  "createdBy" : $scope.userId,
													  "updatedDate" : "",
													  "updatedBy" : $scope.userId,
													  "orgId" : $scope.orgId,
													  "orgUnitId" : $scope.orgUnitId,
													  "isOnHoldParameter":false,
													  "serologyResultTestMapperDtoList" : $scope.serologyTestResultMapperList
													}
											}
										else{
											$rootScope.stopLoader();
											growl.error("Please select Test.",
													{
														title : 'Error!'
													});
											$scope.saveOrNextButton = false;
											$scope.saveBtnDisabled = false;
											return false;
										}
									   }
									});
										
										if(finalFlag == true){
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
                                                    if($scope.cnt == 1){
   													 	$scope.nextRecordIndex=0;   
   													 	$scope.cnt = 0;
                                                    }else{
   													   	$scope.nextRecordIndex = $scope.nextRecordIndex -1;
   													   	$scope.cnt = $scope.cnt-1;
   													}
                                                    $scope.saveOrNextButton = false;
                                                    $scope.saveBtnDisabled = false;
                                                    return false;
                                             } 
                                            
                                            var isValidBatch = $scope.itemList.find(obj =>{return (obj.selectedUom == undefined || obj.selectedUom.uomTypeId == undefined )})
                                             if(isValidBatch!=undefined){
                                                 $rootScope.stopLoader();
                                                    growl.warning("Please Select UOM Type Of All Items",
                                                                {
                                                                    title : 'warning'
                                                                });
                                                    if($scope.cnt == 1){
   													 	$scope.nextRecordIndex=0;   
   													 	$scope.cnt = 0;
                                                    }else{
   													   	$scope.nextRecordIndex = $scope.nextRecordIndex -1;
   													   	$scope.cnt = $scope.cnt-1;
   													}
                                                    $scope.saveOrNextButton = false;
                                                    $scope.saveBtnDisabled = false;
                                                    return false;
                                             } 
                                            
                                            var isValidBatch = $scope.itemList.find(obj =>{return (obj.consumedQuantity == undefined || obj.consumedQuantity == 0 )})
                                             if(isValidBatch!=undefined){
                                                 $rootScope.stopLoader();
                                                    growl.warning("Please Enter Consumed Quantity",
                                                                {
                                                                    title : 'warning'
                                                                });
                                                    if($scope.cnt == 1){
   													 	$scope.nextRecordIndex=0;   
   													 	$scope.cnt = 0;
                                                    }else{
   													   	$scope.nextRecordIndex = $scope.nextRecordIndex -1;
   													   	$scope.cnt = $scope.cnt-1;
   													}
                                                    $scope.saveOrNextButton = false;
                                                    $scope.saveBtnDisabled = false;
                                                    return false;
                                             } 
                                        }
										
									    $rootScope.startLoader();
									    
									    $scope.checkValueFlag = false;
										
										if($scope.isOnHoldParameter == true && ($scope.serologyResultMasterList != null && $scope.serologyResultMasterList.length > 0)){
											angular.forEach($scope.serologyResultMasterList, function(value) {
												if(value.serologyResultTestMapperDtoList.length > 0){
													angular.forEach(value.serologyResultTestMapperDtoList, function(val1) {
														if(val1.firstFinding != ""){
															$scope.checkValueFlag = true;
														}
													});
												}
											});
										}else if($scope.isOnHoldParameter == false){
											$scope.checkValueFlag = true;
										}
										
										var URI = "";
										if($scope.checkValueFlag == true){
											
												 if($scope.flagCheckOnHoldParam == true){
													 
													 URI = BASE_URL + ROOT_URL+"/LIS/Serology/updateResultEntry";
												}else{
													
													if($scope.isValidationAndRelease == 'Y' ){
														 URI = BASE_URL + ROOT_URL+"/LIS/Serology/ResultEntryOrReleaseEntry";
													}else{
														 URI =BASE_URL + ROOT_URL
															+ SAVE_SEROLOGY_REPORT_ENTRY; 
													}
												}
										}else{
                                            	
											growl.warning("Please enter atleast one test result.",
													{
														title : 'Warning!'
													});
											$scope.saveBtnDisabled = false;
											$rootScope.stopLoader();
                                        }
										
										
										console.log("URI",URI);
										GenericService.serviceAction("POST", URI, $scope.serologyResultMasterList).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$scope.saveOrNextButton = false;
												$scope.saveBtnDisabled = false;
												$rootScope.stopLoader();
                                                if($scope.itemList!=null && $scope.itemList.length>0){
                                                    $scope.saveNewConsumption();
                                                }
                                                if(response.data.object!=null){
                                                	growl.success(response.data.message,
    														{
    															title : 'Success!'
    														});
                                                	
                                                	growl.warning(response.data.object,
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
													
													angular.forEach($scope.serologyResultMasterList,function(value,index) {
														
														$scope.labSampleDtlsIdsForPrint = [];
												    	$scope.labSampleDtlsIdsForPrint.push(value.labSampleDtlsId);
												    	
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
												    	
													});
													
												}
                                                
												
                                                if(isRedirect || ($scope.nextRecordList.length) == $scope.nextRecordIndex){
                                                	$scope.saveBtnDisabled = false;
                                                	
                                                	if($scope.amendSearchObj.amend != undefined &&  $scope.amendSearchObj.amend == true ){
                                                		$state.go('amendmentWorklist',{searchObj : $stateParams.searchObj});
                                                	}else{
                                                		
                                                		if($scope.consolidatedResultEntry !='Y'){
                                                    		$state.go('reportEntry',{searchObj : $stateParams.searchObj});
                                                    	}else{
                                                    		$state.go('consolidatedResultEntry',{searchObj : $stateParams.searchObj});
                                                    	}
                                                	}
                                                	
                                                }else{
                                                	$scope.serologyResultMasterList
                                                	$scope.serologyDtoList = $scope.nextRecordList[$scope.nextRecordIndex].resultMstList;
                                                	$scope.testParameterList = [];
                                                	$scope.getTestParameterList();
                                                }
											} else if(response.data.status == 'error')
											{
												$scope.saveOrNextButton = false;
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
												$scope.saveBtnDisabled = false;
											}
										  });
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
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
                      $scope.sampleRecollectionFlag=true;
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
                                        }else{
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
                                             "unitId" :    $scope.orgUnitId ,
                                            "organizationId" : $scope.orgId ,
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
                        
                        /*********************Add New Item END *********************/
                            $scope.serviceIds = []; 
							$scope.setFirstTabLevel= function(newTab)
							{ 
								$scope.firstTabLevel = newTab;
								if(newTab==2){
									if($scope.testParameterList.length>0 ){
										$scope.serviceIds = []; 
									angular.forEach($scope.testParameterList,function(value,index){
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
							
							   $scope.goToBack = function(){
									if($scope.amendSearchObj.amend != undefined &&  $scope.amendSearchObj.amend == true ){
                                		$state.go('amendmentWorklist',{searchObj : $stateParams.searchObj});
                                	}else{
                                		
                                		if($scope.consolidatedResultEntry !='Y'){
                                    		$state.go('reportEntry',{searchObj : $stateParams.searchObj});
                                    	}else{
                                    		$state.go('consolidatedResultEntry',{searchObj : $stateParams.searchObj});
                                    	}
                                	}
							   }
							   
							   $scope.nextRecordIndex=0;
							   $scope.cnt = 0;
							   
							   $scope.loadNextRecord = function(){
								   
								   $scope.saveOrNextButton = true;
							        $scope.saveBtnDisabled=true;
							        
								   	var resultValueFlag = true;
									var TechniquesFlag = true;
									var techniquesFlagArray = [];
									var flagArray = [];
								angular.forEach($scope.testParameterList, function(value, key){
									 
									   angular.forEach(value.listTestParamMppr, function(value3, key3){
										count=0;
										angular.forEach(value3.tempList, function(value4, index4){
																		
										 if(value4.techniqueId!="" && value4.firstFinding==""){
											 flagArray.push(resultValueFlag);
												
											}
										 else if(value4.techniqueId=="" && value4.firstFinding=="" && count==0 && value3.parameterId ==value4.parameterId)
											{
											  techniquesFlagArray.push(TechniquesFlag);
											}							
										});
											count++;
									 });
								 });
								 
								if (flagArray.length==0) {
								   
								   $scope.saveOrNextButton = true;
							        $scope.saveBtnDisabled=true;
							        
								   if($scope.cnt == 0){
									   $scope.nextRecordIndex=0;   
									   $scope.cnt = $scope.cnt+1;
								   }else{
									   $scope.nextRecordIndex = $scope.nextRecordIndex +1;
									   $scope.cnt = $scope.cnt+1;
								   }
								   
								   $scope.saveDetailsSerologyReportEntry(false,false);
								   
							   	}
								else if(techniquesFlagArray.length>0){
									
									growl.warning("Please select Techniques of all Parameters",
											{
												title : 'warning!'
											});
									$scope.saveOrNextButton = false;
									$scope.saveBtnDisabled=false;
									
									return false;
								}
								else if(flagArray.length>0){
									growl.warning("Please select Result",
											{
												title : 'warning!'
											});
									
									$scope.saveOrNextButton = false;
									$scope.saveBtnDisabled=false;
									
									return false;
								}  
							 }
							   
							   
							   

								$scope.showHelpValue = function(info,tIndex,pIndex,cIndex)
								{
									
									try 
									{
										$rootScope.startLoader();
										$scope.tIndex = tIndex;
										$scope.pIndex = pIndex;
										$scope.cIndex = cIndex;
										
										$scope.listHelpValueMaster = [];
										var URI = BASE_URL + ROOT_URL
										+ LIS_UNIT
										+ GET_HELP_VALUES +S + info.parameterId+S + $scope.orgId+S+$scope.orgUnitId;
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
													    		  return false;
													    	   }
													    	$rootScope.stopLoader();
													    }
													    else if(response.data.status=="error")
													    	{
													    	   $scope.listHelpValueMaster =[];
													    	   growl.warning("No help value found.",
																		{
																			title : 'Warning!'
																		});
												    		  
													    	   $rootScope.stopLoader();
													    	   return false;
													    	}
														
													});
											
										}
									catch (e) {
										console.log(e.message)
									}
								}
								
								
								$scope.selectHelpValue = function(helpValue,isAbnormal)
								{
									$scope.testParameterList[$scope.tIndex].listTestParamMppr[$scope.pIndex].tempList[$scope.cIndex].firstFinding = helpValue;
									$scope.testParameterList[$scope.tIndex].listTestParamMppr[$scope.pIndex].tempList[$scope.cIndex].isAbnormal = isAbnormal;
									
									$('#helpValueSelect').modal('hide'); 
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
									var labSampleDtlsListTemp = groupBy($scope.testParameterList, 'labSampleDtlsId');
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
								   
								   $scope.uploadLabReportDoc = function(fileOb){
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
												return response;
											});
								   }
								   
								   $scope.uploadDoc = function()
									 
								    {
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
										  growl.warning("Please select Test .",
													{
														title : 'warning!'
													});
										   $rootScope.stopLoader();
										   return false;
									  }
									  if(isFileEmpty){
										  growl.warning('Please select file.',
													{
														title : 'warning!'
													});
										  $rootScope.stopLoader();
										  return false;
									  }
									  
									  if(isFileFormatInvalid){
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
															growl.error('Something went wrong.',
																	{
																		title : 'Error!'
																	});
															 $rootScope.stopLoader();
														}
												    });
												
										});
									
								    }
								   
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
									
									//for recollect
									
									  $scope.showRejecPopup = function(isAccRej,index)
		                              {
										  $scope.rejIndex = index;
										  $scope.sampleRejectReasonId = "";
											 $scope.sampleRejectReason = ""
										  $("#seroReject").modal('show');
									}
									  
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
										
										$scope.recollectSerologySample=function(){



											try 
											{ 	var finalFlag = false;
											 $("#seroReject").modal('hide');
											 $('.modal-backdrop').remove()
												$(document.body).removeClass("modal-open");
												$scope.serologyResultMasterList = [];
												 $scope.serologyResultTestsList= [];
												
												
												angular.forEach($scope.testParameterList, function(value, key)
												{  
															var newArr={};
															newArr.labSampleDtlsId = value.labSampleDtlsId;
															newArr.serologyTestResultId = value.serologyTestResultId;
															 $scope.serologyResultTestsList.push(newArr);
													 
											});
												
												 $scope.obj = {
														  "updatedDate" : moment(new Date).format("YYYY-MM-DD HH:mm:ss"),
														  "updatedBy" : $scope.userId,
														  "orgId" : $scope.orgId,
														  "orgUnitId" : $scope.orgUnitId,
														  "sampleRejectReason": $scope.sampleRejectReason,
														  "sampleRejectReasonId": $scope.sampleRejectReasonId,
														  "serologyResultTestsList" : $scope.serologyResultTestsList
														}
											
		                                    	if($scope.sampleRejectReasonId == undefined || $scope.sampleRejectReasonId == null || $scope.sampleRejectReasonId == ""){
													$rootScope.stopLoader();
													growl.warning("Please select reason.",
															{
																title : 'warning!'
															});
													$scope.saveOrNextButton = false;
													return false;

												}
		                                        if($scope.sampleRejectReason == undefined | $scope.sampleRejectReason == null || $scope.sampleRejectReason == ""){
													$rootScope.stopLoader();
													growl.warning("Please select note.",
															{
																title : 'warning!'
															});
													$scope.saveOrNextButton = false;
													return false;

												}
												
											    $rootScope.startLoader();
												
												var URI =  URI = BASE_URL + ROOT_URL+"/LIS/Serology/recollectSampleForSerlogy";
												
												console.log("URI",URI);
												GenericService.serviceAction("POST", URI, $scope.obj).then(function(response) 
												{	
													if (response.data.status == 'success')
													{
														$rootScope.stopLoader();
		                                               
		                                                	growl.success(response.data.message,
		    														{
		    															title : 'Success!'
		    														});
		                                                	if($scope.amendSearchObj.amend != undefined &&  $scope.amendSearchObj.amend == true ){
		                                                		$state.go('amendmentWorklist',{searchObj : $stateParams.searchObj});
		                                                	}else{
		                                                		
		                                                		if($scope.consolidatedResultEntry !='Y'){
		                                                    		$state.go('reportEntry',{searchObj : $stateParams.searchObj});
		                                                    	}else{
		                                                    		$state.go('consolidatedResultEntry',{searchObj : $stateParams.searchObj});
		                                                    	}
		                                                	}
//		                                                }
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.error(response.data.status,
																{
																	title : 'Error!'
																});
														$scope.saveBtnDisabled = false;
													}
												  });
											} catch (e) {
												$rootScope.stopLoader();
												console.log("Exception",e.message);
											}
										}
						} ]);
