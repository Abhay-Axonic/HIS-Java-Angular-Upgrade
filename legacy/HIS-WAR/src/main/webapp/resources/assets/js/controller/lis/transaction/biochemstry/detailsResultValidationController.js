
/**
 * @author Ganesh Chadhari
 */

angular
		.module('myApp')
		.controller(
				"detailsResultValidationController",
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
						'GenericServiceFileUpload',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams,subDeptId,GenericServiceFileUpload,reportService) {

							
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
							/** * Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/** * Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = subDeptId;
							$scope.consolidatedResultEntry =labConfig.consolidatedResultEntry;

							$scope.listHelpValueMaster = [];
							
							
							$scope.setFirstTabLevel= function(newTab)
							{ 
								$scope.firstTabLevel = newTab;
								$scope.getParameterHistory();
							};
							
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								 
								return $scope.firstTabLevel === tabNum;
							};
							
							/* Second Level */
							$scope.secondTabLevel = 1 ;
							
							$scope.setSecondTabLevel = function(newTab)
							{
								$scope.secondTabLevel = newTab;
							};
							
							$scope.isSetSecondTabLevel = function(tabNum)
							{
								return $scope.secondTabLevel === tabNum;
							};
							
							
							/* Third Level */
							$scope.thirdTabLevel = 1 ;
							
							$scope.setThirdTabLevel = function(newTab)
							{
								$scope.thirdTabLevel = newTab;
							};
							
							$scope.isSetThirdTabLevel = function(tabNum)
							{
								return $scope.thirdTabLevel === tabNum;
							};
							
							
							
							/* FORTH Level */
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
							
							
							
							
							/*
							 * $scope.showHelpValue =
							 * function(testIndex,headerIndex,resultIndex,testType,HelpValueMaster) {
							 * 
							 * $scope.tIdx = testIndex; $scope.hIdx =
							 * headerIndex; $scope.rIdx = resultIndex;
							 * $scope.tType = testType;
							 * $scope.listHelpValueMaster = HelpValueMaster;
							 * console.log($scope.listHelpValueMaster);
							 * $('#helpValueSelect').modal('show'); }
							 */
							
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

							// ================getTest and paramter by patient
							
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
						                 $rootScope.stopLoader();
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
							            			  growl.warning("No history found for this patient..",
																{
																	title : 'Error!'
																});
							            			  $rootScope.stopLoader();
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
							
								// ================getTest and paramter by
								// patient
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
							
// $scope.sampleRecollectionFlag=true;
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
							
							$scope.getBiochemResultEntryDetails = function() 
							{
								
								
								var resultEntry = $scope.labResultMasterDto;
								try {
									$rootScope.startLoader();
									$scope.labResultDetailsMasterDtoDtlsList =[];
									 var dataList = [];
									angular.forEach($scope.labResultMasterList, function(value, key) {
									
									var resultEntry = value;
									// $scope.isRecollected =
									// resultEntry.sampleRecollectFlag=='Y'?true:false;
									// $scope.sampleRecollectAgainstId =
									// resultEntry.sampleRecollectAgainstId;
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
											"labResultId":resultEntry.labTestResId,
											"assingnedFrom":resultEntry.orgUnitId,
											"labSampleDtlsId":resultEntry.labSampleDtlsId
										};
									   dataList.push(data);
									});
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ GET_WORK_LIST_VALIDATION_ENTERY_DETAILS;
									GenericService
											.serviceAction(METHOD_POST, URI, dataList)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.testMasterResList =[];
														$scope.testDescData=[];
														$scope.testMasterResList = response.data.listObject;
														angular.forEach($scope.testMasterResList,function(value,key){
                                                            $scope.testDescData.push(value.testDesc)
                                                        });
														angular.forEach($scope.testMasterResList, function(value, key) {
															value.isSelected = true;
															var labResultMaster = $scope.labResultMasterList.find(obj => {return obj.labSampleDtlsId == value.labSampleDtlsId})
														    value.sampleRecollectAgainstId =labResultMaster.sampleRecollectAgainstId;
														    value.isRecollected =labResultMaster.sampleRecollectFlag=='Y'?true:false;
														    value.isRetestRecollected = (labResultMaster.sampleRecollectFlag=='Y'||labResultMaster.retestCount>0)?true:false;
														    angular.forEach($scope.testMasterResList, function(value, key) {
															    $scope.getPreviousDeltaReport(value.testId,value.testType,value.labSampleDtlsId,key);
														    });
														    if(value.labResultDetailsViewDto!=null){ 
														    	 if(value.labResultDetailsViewDto.isAbnormal == null || value.labResultDetailsViewDto.isAbnormal == undefined){
																    	value.labResultDetailsViewDto.isAbnormal = 'N';
																    }
														    	value.isAbnormal = value.labResultDetailsViewDto.isAbnormal;
														    	$scope.changeArrowPosition(key,0,0,value.testType,1,false);
														    }
														});
														
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
													
													});
								} catch (e) {
									$rootScope.stopLoader();
									console.log(e.message)
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
							
							/** Redirect Page Code */
							if($stateParams.resultMstList!=null  && $stateParams.resultMstList.length>0)
 							{
								$scope.labResultMasterList = $stateParams.resultMstList;
								 $scope.nextRecordList = $stateParams.nextRecordList;
								  $scope.getMachineList();
								$scope.getBiochemResultEntryDetails();
								
							} else {
								if($scope.consolidatedResultEntry =='Y'){
									   $state.go('consolidatedReportValidation',
												{
												  searchObj : $stateParams.searchObj
												});
								   }else if($scope.consolidatedResultEntry !='Y'){
										$state.go('worklistForValidation',{subDeptId : subDeptId, searchObj : $stateParams.searchObj});
								   }
							}
							
							
							$scope.labResultMasterDtoList = [];
							$scope.createLabResultObjectList = function(flag,sampleStatusId)
							{
								angular.forEach($scope.testMasterResList, function(value, key) {
									 
								$scope.labResultMasterDto = 
										 $scope.labResultMasterList.find(obj => {
											  return obj.labSampleDtlsId == value.labSampleDtlsId
											})
											
								 if(value.isSelected==true)
									{
									if(value.testType==0)
									{
										  $scope.listLabResultEntryDtls = [];
										  if(flag)
										  {
										    $scope.labResultMasterDto.labTestResId = null;
										  }  
										  $scope.labResultMasterDto.createdBy =$scope.userId;
										  $scope.labResultMasterDto.updatedBy =$scope.userId;
							    	      $scope.labResultMasterDto.orgId= $scope.orgId;
							    	      $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
							    	      if(sampleStatusId== 15){
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
							    	      value.labResultDetailsViewDto.orgId = $scope.orgId;
							    	      value.labResultDetailsViewDto.orgUnitId  =$scope.orgUnitId;
							    	      value.labResultDetailsViewDto.listHelpValueMaster = [];
							    	      value.labResultDetailsViewDto.listParamRefrengMaster = [];
							    	      if( value.labResultDetailsViewDto.isAbnormal == undefined ||  value.labResultDetailsViewDto.isAbnormal== null){
							    	      	  value.labResultDetailsViewDto.isAbnormal = "N";
							    	      }
							    	      if(flag)
										  {
							    	        value.labResultDetailsViewDto.labResDtlsId = null;
							    	        value.labResultDetailsViewDto.labResultId = null;
										  }
							    	      
							    		    // to append precision to int val
									      if(value.labResultDetailsViewDto!=undefined && value.labResultDetailsViewDto!=null &&  value.labResultDetailsViewDto.refrangeTypeId !=null && value.labResultDetailsViewDto.refrangeTypeId == 1){
									      		 if(! value.labResultDetailsViewDto.finalResult.isNaN &&  value.labResultDetailsViewDto.finalResult!=null &&  value.labResultDetailsViewDto.finalResult!=undefined && value.labResultDetailsViewDto.finalResult!="-"){
									      		     var arr =  value.labResultDetailsViewDto.finalResult.split('.');
									      		     if(arr[1]==undefined &&  value.labResultDetailsViewDto.paramPrecision > 0 ){
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
									      $scope.labResultMasterDto.resultLevel = RESULT_VALIDATION;
									      $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
									      $scope.labResultMasterDto.machineId = value.machineId ; 
									      $scope.labResultMasterDto.provisional = $scope.provisional;
									      $scope.labResultMasterDto.isOnHoldParameter = value.isOnHoldParameter;
									      $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
									}
									else if(value.testType==1)
									{
										$scope.listLabResultEntryDtls  = [];
										 if(flag)
										  {
										    $scope.labResultMasterDto.labTestResId = null;
										  }
										$scope.labResultMasterDto.createdBy=$scope.userId;
					    	            $scope.labResultMasterDto.orgId= $scope.orgId;
					    	            $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
					    	            if(sampleStatusId== 15){
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
					    	            $scope.labResultMasterDto.assingnedFrom =  $scope.labResultMasterDto.orgUnitId;
										angular.forEach(value.listMultyParamTestHeaderDto, function(headerParamObj, key) 
												{
											angular.forEach(headerParamObj.listLabResultDetailsViewDto, function(labResultDetails, key) 
													{
												      labResultDetails.orgId = $scope.orgId;
												      labResultDetails.orgUnitId =$scope.orgUnitId;
												      labResultDetails.listHelpValueMaster = [];
												      labResultDetails.listParamRefrengMaster = [];
												      if(flag)
													  {
												       labResultDetails.labResDtlsId = null;
												       labResultDetails.labResultId = null;
													  }
												      if(labResultDetails.isAbnormal== null || labResultDetails.isAbnormal == undefined){
												    	  labResultDetails.isAbnormal = 'N';
												      }
												      
												      
												      // to append precision
														// to int val
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
										  $scope.labResultMasterDto.resultLevel = RESULT_VALIDATION;
										  $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
										  $scope.labResultMasterDto.assingnedFrom =  $scope.labResultMasterDto.orgUnitId;
										  $scope.labResultMasterDto.machineId = value.machineId ; 
										  $scope.labResultMasterDto.provisional = $scope.provisional;
										  $scope.labResultMasterDto.isOnHoldParameter = value.isOnHoldParameter;
									      $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
									  }
									}
									});
								
							}
							
							
							
							
							$scope.countflag = false;
							$scope.headerName= "";
							$scope.mistachCountMSG = "";
							$scope.isRedirect = false;
							$scope.provisional = false;
							$scope.validateTotalCountSaveResultEntryDetails =  function(isRedirect,provisionalRelease,isOnHol){
								$scope.isOnHoldParameter = isOnHol;
								$scope.countflag = false;
								$scope.isRedirect = isRedirect;
								$scope.headerName= "";
								$scope.mistachCountMSG = "";
								$scope.provisional = provisionalRelease;
								var  totalValue = 0;
								var  headerTotalCount = 0;
								var loopFlag = false;
								angular.forEach($scope.testMasterResList, function(value, key) {
									 
								$scope.labResultMasterDto = 
										 $scope.labResultMasterList.find(obj => {
											  return obj.labSampleDtlsId == value.labSampleDtlsId
											})
								value.isOnHoldParameter = isOnHol;
								 if(value.isSelected==true)
									{
									 	if(value.testType==1)
									 	{
									 		$scope.listLabResultEntryDtls  = [];
										
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
														
														if( $scope.countflag == true && totalValue!=headerParamObj.totalCount){
															
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
										  
										  $scope.saveResultEntryDetails();
									  }
								
								
								
							}
							
							
							$scope.saveResultEntryDetails= function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								
								
									try 
									{
										
										$scope.checkValueFlag = false;
										$scope.checkValueFlagOnHold = false;
										
										angular.forEach($scope.testMasterResList, function(value, key) {
											 
											$scope.labResultMasterDto =  $scope.labResultMasterList.find(obj => {
														  return obj.labSampleDtlsId == value.labSampleDtlsId
														})
														
											 if(value.isSelected==true)
												{
													if(value.testType==0)
													{
														  
													      if(value.labResultDetailsViewDto!=undefined && value.labResultDetailsViewDto!=null ){
													      		 if( value.labResultDetailsViewDto.finalResult == undefined &&  value.labResultDetailsViewDto.finalResult == null && value.labResultDetailsViewDto.finalResult ==""){
													      			$scope.checkValueFlag = false;
													      		 }else{
													      			$scope.checkValueFlag = true;
													      		 }
													      }
											    	    
													}
													else if(value.testType==1)
													{
														
														
														if($scope.isOnHoldParameter == true){
														
														angular.forEach(value.listMultyParamTestHeaderDto, function(headerParamObj, key) 
																{
															angular.forEach(headerParamObj.listLabResultDetailsViewDto, function(labResultDetails, key) 
																	{
																      
																      	if(labResultDetails.finalResult!=undefined && labResultDetails.finalResult!=null && labResultDetails.finalResult!=""){
																      		$scope.checkValueFlag = true;		
																      	}
																      
																      
																  });
															});
														}
														
														if($scope.isOnHoldParameter == false){
															$scope.checkValueFlag = true;
															angular.forEach(value.listMultyParamTestHeaderDto, function(headerParamObj, key) 
																	{
																angular.forEach(headerParamObj.listLabResultDetailsViewDto, function(labResultDetails, key) 
																	{
																	      
																	      	if((labResultDetails.finalResult == undefined || labResultDetails.finalResult == null  || labResultDetails.finalResult =="") && (labResultDetails.finalResult != 0)){
																	      		$scope.checkValueFlagOnHold = true;	
																	      	}
																	      
																	});
															});
														}
													  }
												}
											});
										
										
										if($scope.checkValueFlag == false){
											
											growl.warning("Please enter atleast one test result.",
													{
														title : 'Warning!'
													});
											$scope.saveOrNextButton = false;
											return false;
										}
										
										if($scope.checkValueFlagOnHold == true){
											
											growl.warning("Please enter all test result.",
													{
														title : 'Warning!'
													});
											$scope.saveOrNextButton = false;
											return false;
										}
										
										$rootScope.startLoader();
										
										$scope.createLabResultObjectList(false,RESULT_VALIDATION_DONE);
										
										if($scope.labResultMasterDtoList.length == 0){
											$rootScope.stopLoader();
											growl.warning("Please Select Test",
														{
															title : 'warning'
														});
											$scope.saveOrNextButton = false;
														return false;
										}
										
										var URI =BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ WORK_VALIDATION_ENTERY_DETAILS;
										console.log("URI",URI);
										GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$scope.saveOrNextButton = false;
												$rootScope.stopLoader();
												
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												
												
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
													if($scope.consolidatedResultEntry =='Y'){
														   $state.go('consolidatedReportValidation',
																	{
																	  searchObj : $stateParams.searchObj
																	});
													   }else if($scope.consolidatedResultEntry !='Y'){
															$state.go('worklistForValidation',{subDeptId : subDeptId, searchObj : $stateParams.searchObj});
													   }
												}else{
													$scope.labResultMasterDtoList = [];
													$scope.labResultMasterList = $scope.nextRecordList[$scope.nextRecordIndex].resultMstList;
													 $scope.getMachineList();
													$scope.getBiochemResultEntryDetails();
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
							
							

							$scope.retestDisableBtn = false;
							$scope.reTestResultEntryDetails= function() 
							{
									
								
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.worklistForReportEntryForm.$valid)
								{
									try 
									{
										$rootScope.startLoader();
										$scope.retestDisableBtn = true;
										$scope.createLabResultObjectList(true,RETESTING);
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
										console.log("URI",URI);
										console.log("labResultMasterDtoList",JSON.stringify($scope.labResultMasterDtoList));
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
												
												if($scope.consolidatedResultEntry =='Y'){
													   $state.go('consolidatedReportValidation',
																{
																  searchObj : $stateParams.searchObj
																});
												   }else if($scope.consolidatedResultEntry !='Y'){
														$state.go('worklistForValidation',{subDeptId : subDeptId, searchObj : $stateParams.searchObj});
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
									}
									
							}
							
                            $scope.disableRecollectBtn = false;
							$scope.reCollectResultEntryDetails= function() 
							{
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
										$scope.createLabResultObjectList(true,SAMPLE_REJECTED);
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
												if($scope.consolidatedResultEntry =='Y'){
													   $state.go('consolidatedReportValidation',
																{
																  searchObj : $stateParams.searchObj
																});
												   }else if($scope.consolidatedResultEntry !='Y'){
														$state.go('worklistForValidation',{subDeptId : subDeptId, searchObj : $stateParams.searchObj});
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
									}
							}
							
							$scope.getResultEntryDataList = function(sampleRecollectAgainstId,parameterId) {
								try {
									
									$rootScope.startLoader();
									var data = {
											"orgId":$scope.orgId,
											"orgUnitId":$scope.orgUnitId,
											"parameterId":parameterId,
											"sampleRecollectAgainstId":sampleRecollectAgainstId,
											"firstLevelResult":"secondLevel"
										};
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ GET_WORK_RESULT_ENTERY_DETAILS;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
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
							
						/*
						 * $scope.previousResultFlag = false;
						 * $scope.getLabHistoryReport = function() {
						 * $("#resetDetails").modal('show'); }
						 */
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
									     paramRes.finalResult = previousObj.listParameterResultDto[index].result;
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
												 "resultLevel":RESULT_VALIDATION
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
							
							
							$scope.previousTestResultList = [];
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
								
								
								/*
								 * var promiseRetestRecollect =
								 * $scope.getRetestRecollectTests(testId,testType,labSampleDtlsId);
								 * promiseRetestRecollect.then(function(testObjList) {
								 * if(testObjList!=null) {
								 * $scope.previousResultFlag = true; } else{
								 * $scope.previousResultFlag = false; } });
								 */
								
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
										  "testId" : testId,
										  "testType" :  testType,
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
							
							
							
							$scope.getLabResultOBj = function(testIndex, parameterId)
							{
								 angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(paramInHeaderObj, key)
									    {
									        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(paramObj, key)
									          {
													   if(paramObj.parameterId == parameterId &&paramObj.refrangeTypeId==REFERENCE_VALUE)
													   {
														   
														   return paramObj;
													   }
													   else
													   {
														   return {};
													   }
										    });  
										});
								
							};
							
							 $scope.changeArrowPosition  = function(testIndex,headerIndex,resultIndex,testType,flag,isEditable,id2)
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
	                                    }

									    // Update the input field with the formatted value
									  
                            if(!isNaN(formattedValue) && formattedValue != undefined && formattedValue != null && formattedValue != ""){
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
									 if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.refrangeTypeId ==REFERENCE_VALUE){
										 $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.arrowClass  = '';
										 $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.isAbnormal  = 'N';
									 }
                                 	  
                                 }
								
								
								if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE)
									{
									if( $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax !=null &&  $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult > $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax) {
                                        $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-up color-red';
						                  $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
									}else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin !=null &&  $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult < $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin){
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
									   if(flag==1){
									   growl.warning("Please select the Result values from  Help values ",
												{
													title : 'Warning!'
												});
									   return false;
									   $rootScope.stopLoader();
									   }
									}
								else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==MULTIVALUED_RANGE)
									{
									 $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
									}
								
								
								   if($scope.testMasterResList[testIndex].previousTestResultList!=undefined && $scope.testMasterResList[testIndex].previousTestResultList.length>0)
									   {
									   
									   
									   var deltaChange = $scope.calculateDeltaPercentage($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.finalResult,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.refrangeTypeId,
											   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.deltaDaysId,
											   $scope.testMasterResList[testIndex].testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime
											   
											   
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
									 if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult > $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax) {
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
									   }
								   else{
								   }
								   if(flag==1){
									   growl.warning("Please select the Result values from  Help values ",
												{
													title : 'Warning!'
												});
									   return false;
									   $rootScope.stopLoader();
									  }
								}
							   else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null 
									   && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==MULTIVALUED_RANGE)
								{
								   
								   if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal == undefined || 
										    $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal == null){
									   			$scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
									   			$scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  ='N';
								   }			
								}
								if($scope.testMasterResList[testIndex].previousTestResultList!=undefined && $scope.testMasterResList[testIndex].previousTestResultList.length>0)
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
										    		  // labResObj.finalResult=null;
										    		   var formula  = labResObj.parameterFormula;
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
																	        	if(paramObj.parameterId == value.parameterId )
																					   {
																						   if(paramObj.finalResult==null||paramObj.finalResult==""||paramObj==undefined)
																						   {
																							   paramObj.finalResult=null;
																							   checkParameter = true;
																						   }
																						   else if((paramObj.refrangeTypeId == 1) && (paramObj.paramPrecision !== undefined && paramObj.paramPrecision !== '' && paramObj.paramPrecision !== null)){
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
														  if(result==0.00 || isNaN(result)){
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
														  }else{
															  labResObj.finalResult =result.toFixed(labResObj.paramPrecision);
														  }

														  if(labResObj.finalResult==Infinity){
                                                              labResObj.finalResult= String;
                                                              labResObj.finalResult = "";
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

									angular.forEach($scope.testMasterResList, function(testObj, key) 
											{
									angular.forEach(testObj.listMultyParamTestHeaderDto, function(headerObj, key) 
											{
											      angular.forEach(headerObj.listLabResultDetailsViewDto, function(labResObj, key) 
													{
											    	   if(labResObj.isFormula=='Y')
											    		   {
											    		  // labResObj.finalResult=null;
											    		   var formula  = labResObj.parameterFormula;
														   var simplified = math.simplify(math.parse(formula));
														   var formulaDetailsList = labResObj.listFormulaDetails;
														    var obj = {};
														    var testList = [];
														    var checkParameter = false;
															  angular.forEach(formulaDetailsList, function(value, key)
															   {
																  
																  angular.forEach($scope.testMasterResList, function(testparamInHeaderObj, key)
																		    {
																  angular.forEach(testparamInHeaderObj.listMultyParamTestHeaderDto, function(paramInHeaderObj, key)
																		    {
																		        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(paramObj, key)
																		          {
																						  
																		        	if(paramObj.parameterId == value.parameterId )
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
															  if(result==0.00 || isNaN(result)){
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
															  }else{
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
							/*
							 * growl.error("Something went wrong", { title :
							 * 'Error!' });
							 */
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
										console.log("URI", URI);

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
															
														});
									} catch (e) {
										console.log(e.message)
									}
									// }
								}	 
								
								$scope.dateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
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
								
								$scope.informDate = moment().toDate();
								$scope.initInformTo = function()
								{
									$scope.saveBtn = true;
									$scope.updateBtn = false;
									$scope.informToObje = {
										  "informToId":"",
										  "labSampleDtlsId" : '',
										  "informById":$scope.userId,
										  "informDate":"",
										  "note":"",
										  "unitId":$scope.unitId,
										  "organizationId":$scope.orgUnitId,
										  "createdBy":$scope.userId,
										  "status":'A',
										  "createdDate":"",
										  "updatedDate":"",
										  "updatedBy":$scope.userId,
										  "doctorName":"",
										  "informedBy":$scope.userId,
										}
								}
								$scope.initInformTo();
								
								
								$scope.getInformToList = function(labSampleDtlsId)
								{
									$scope.informToList = [];
									try {
									$rootScope.startLoader();
									var data = {
											  "labSampleDtlsId" : labSampleDtlsId,
											}
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ GET_INFORM_TO;
								    GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) 
													{
														if(response.data.status=="success")
															{
															 $scope.informToList = response.data.listObject;
															 $rootScope.stopLoader();
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
								
						       // informToCode
								$scope.getTreatingDoctorId = function(labSampleDtlsId)
								{
									try {
									$scope.initInformTo();
									$rootScope.startLoader();
									var data = {
											  "labSampleDtlsId" : labSampleDtlsId,
											}
									var URI = BASE_URL + ROOT_URL
											+ LIS_BIOCHEMISTRY
											+ LAB_GET_TREATING_DOCTOR;
								    GenericService
											.serviceAction(METHOD_POST, URI, data)
											.then(
													function(response) 
													{
														if(response.data.status=="success")
															{
															 $scope.infomrToDoctor = response.data.object;
															 $scope.informToObje.doctorId = $scope.infomrToDoctor.doctorId;
															 $scope.informToObje.doctorName = $scope.infomrToDoctor.doctorName;
															 if($scope.informToObje.doctorId>0)
																 {
																  $("#informedTo").modal('show');
																  $scope.labSampleDtlsId = labSampleDtlsId;
																  $scope.getInformToList(labSampleDtlsId);
																 }
															 $rootScope.stopLoader();
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
								
				$scope.editInformTo = function(informToO)
				{
					$scope.informToObje.informToId =informToO.informToId,
					$scope.informToObje.note=informToO.note,
					$scope.informDate = moment(informToO.informDate,'DD-MM-YYYY').toDate();
					$scope.informToObje.createdBy =$scope.userId,
					$scope.informToObje.updatedBy =$scope.userId,
					$scope.informToObje
					$scope.saveBtn = false;
					$scope.updateBtn = true;
				}
				
				$scope.updateInformTo =function()
				{
					$scope.saveInformTo();
				}
								
							// informToCode
							$scope.saveInformTo = function()
							{
								try {
//								$rootScope.startLoader();
								if ($scope.savingInProgress) {
						            return;
							        }
								 $scope.savingInProgress = true;
								
								if($scope.informDate==undefined||$scope.informDate==null||$scope.informDate=='')
									{ 
									  growl.warning("Inform date should not be blank..",
												{
													title : 'Error!'
												});
									  $scope.savingInProgress = false;
									  return false;
									}
								if($scope.informToObje.note==''||$scope.informToObje.note==null)
								{ 
								  growl.warning("Please enter note.",
											{
												title : 'Error!'
											});
								  $scope.savingInProgress = false;
								  return false;
								}
								
								$scope.informToObje.labSampleDtlsId =$scope.labSampleDtlsId;
								$scope.informToObje.informDate = moment($scope.informDate).format('DD-MM-YYYY HH:MM:SS');
								var URI = BASE_URL + ROOT_URL
										+ LIS_BIOCHEMISTRY
										+ SAVE_INFORM_TO;
								
							    GenericService
										.serviceAction(METHOD_POST, URI, $scope.informToObje)
										.then(
												function(response) 
												{
													if(response.data.status=="success")
														{
														  $scope.getInformToList($scope.labSampleDtlsId);
														  $rootScope.stopLoader();
														  growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
														  $scope.informToObje.note='';
														}
													else if(response.data.status=="error")
														{
														  $rootScope.stopLoader();
														  growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
														}
													$scope.saveBtn = true;
													$scope.updateBtn = false;
													$scope.savingInProgress = false;
												});
							   } catch (e) {
								console.log(e.message)
								$scope.savingInProgress = false;
							  }
							}	
							
							$scope.goToBack = function(){
								 if($scope.consolidatedResultEntry =='Y'){
									   $state.go('consolidatedReportValidation',
												{
												  searchObj : $stateParams.searchObj
												});
								   }else if($scope.consolidatedResultEntry !='Y'){
										$state.go('worklistForValidation',{subDeptId : subDeptId, searchObj : $stateParams.searchObj});
								   }
							}
							
							
							$scope.nextRecordIndex=0;
							$scope.cnt = 0;
							   $scope.loadNextRecord = function(){
								   $scope.saveOrNextButton = true;
								   if($scope.cnt  == 0){
									   $scope.nextRecordIndex=0;   
									   $scope.cnt  = $scope.cnt +1;
								   }else{
									   $scope.nextRecordIndex = $scope.nextRecordIndex +1;
									   $scope.cnt  = $scope.cnt +1;
								   }
								   $scope.validateTotalCountSaveResultEntryDetails(false,false,false)   ;
							   }
							   
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
																   }else{
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
																							$scope.changeArrowPosition(indx,index,index1,$scope.testMasterResList[indx].testType,1,null);
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
																 			   			$scope.changeArrowPosition(indx,index,index1,$scope.testMasterResList[indx].testType,1,null);
																 			   	   }
																 			   	 
																				});
																 		  
																 		  		
								                                            });
																   }
															   }
												   });
									   }

									  }
								   
								   // upload document
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
                                          $scope.saveBtnClickCount = 0;
                                          $scope.counterDocSave = 0;
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
                                        $scope.saveBtnClickCount = 0;
                                        $scope.counterDocSave = 0;
                                        $scope.uploadDocBtn = false;
                                    }


								   $scope.showRejecPopup = function(isAccRej,index)
		                              {
										// if(isAccRej=='R')
										// {
										  $scope.rejIndex = index;
										  $scope.sampleRejectReasonId = "";
											 $scope.sampleRejectReason = ""
										  $("#accptRejInPatient").modal('show');
									    // }
									}
								   $scope.selectPatientForSave = function(){
				                	   
				                	   var checkedCount = 0;
				                       angular.forEach($scope.inPatientSampleList, function(value, key) {
				                      
				                           if(value.isAcceptOrReject != 'N'  && value.isAcceptOrReject != null ){
				                                checkedCount = checkedCount + 1;
				                           }
				                      });
				                       
				                       if(checkedCount > 0){
				                           $scope.saveBtn = true;
				                       }else{
				                           $scope.saveBtn = false;
				                       }
				                   }
								   
								   /*
									 * $scope.saveRejectDetails = function(){
									 * 
									 * if($scope.sampleRejectReasonId == ""){
									 * 
									 * growl.warning("Please select reason..!!", {
									 * title : 'warning!' }); return false;
									 *  } else if($scope.sampleRejectReason ==
									 * "") { growl.warning("Please Enter reason
									 * note..!!", { title : 'warning!' });
									 * return false;
									 *  } else {
									 * 
									 * $scope.inPatientSampleList[$scope.rejIndex].sampleRejectReasonId =
									 * $scope.sampleRejectReasonId;
									 * $scope.inPatientSampleList[$scope.rejIndex].sampleRejectReason =
									 * $scope.sampleRejectReason;
									 * 
									 * $scope.sampleRejectReasonId = "";
									 * $scope.sampleRejectReason = "";
									 * 
									 * $scope.reCollectResultEntryDetails(); } }
									 */
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
								   	
								   	
								   	
							
						} ]);