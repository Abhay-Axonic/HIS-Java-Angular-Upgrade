
/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"detailsSerologyValidationController",
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
						'GenericServiceFileUpload',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams,GenericServiceFileUpload,reportService) {

							
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
							$scope.saveOrNextButton = false;
							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.subDeptId = labConfig.serologyId;
							$scope.consolidatedResultEntry =labConfig.consolidatedResultEntry;

							
							$('[data-toggle="deltatooltip"]').tooltip();
							
							  $scope.selectedTechniqueList = [];
							  
							  $scope.dropDownSetting = {
							    		scrollableHeight: '100px', 
							    		scrollable: true
							    }; 
							
							$scope.getSerologyFindings = function()
						       {
						        try 
						        {
						        	
						        	$scope.reportEntryDetailsList = [];
						        	
						        	var data = {};
							           
									angular.forEach($scope.labResultMasterList,function(value,key){
						        	     data[value.labSampleDtlsId]=value.testId; 
						        	     $scope.isOnHoldParameter=value.isOnHoldParameter;
											});
						        	$rootScope.startLoader();
						        	if($scope.isOnHoldParameter == true){
						        		var URI = BASE_URL 
						                 + ROOT_URL
						                 + GET_SEROLOGY_REPORT_VALIDATION_DETAILS;
						        	}else{
						        		 var URI = BASE_URL+ ROOT_URL+ GET_SEROLOGY_REPORT_ENTRY_DETAILS;
						        	}
						           
						            
						            GenericService.serviceAction("POST", URI, data).then(function(response) 
						            {
						            	if (response.data.status == 'success')
										{
						            	  $rootScope.stopLoader();
						            	  
						                  $scope.reportEntryDetailsList = []; 
						                
						                  angular.forEach($scope.labResultMasterList,function(value,key){
						                	  	 $scope.validateObj = {
						                	  			'validateList':[],
						                	  			'findingResultDesc':'',
						                	  			'testDesc':value.testDesc,
						                	  			'testCode':value.testCode,
						                	  			'sampleNo':value.sampleNo,
						                	  			'panelCode':value.panelCode,
						                	  			'isSelected':true,
						                	  			'testId' : value.testId,
						                	  			'labSampleDtlsId':value.labSampleDtlsId,
						                	  			'isOnHoldParameter':value.isOnHoldParameter,

						                	  	};
						                	  	 $scope.validateList = [];
							                	  angular.forEach(response.data.listObject,function(value1,index1){
							                		  
							                		  if(value1.labSampleDtlsId == value.labSampleDtlsId){
							                			  if($scope.isOnHoldParameter == true){
							                			  value1.findingResult =  value1.findingResult;
							                			  }else{
							                				  value1.findingResult =  value1.firstFinding;
							                			  }
									                	  value1.findingResultDesc = '';
									                	  $scope.validateObj.findingResultDesc ="";
									                	
									                	  if(value1.isAbnormal== null && value1.isAbnormal==undefined){
								                			  value1.isAbnormal = 'N';
								                		  }
									                	  $scope.validateList.push(value1);
							                		  }
							                		  $scope.validateObj.internalNotes=value1.internalNotes;
							                		  $scope.validateObj.isOnHoldParameter=value1.isOnHoldParameter;

								                  });
							                	  $scope.validateObj.validateList = $scope.validateList;
							                	  $scope.reportEntryDetailsList.push($scope.validateObj);
												});
						                  console.log(" $scope.reportEntryDetailsList",$scope.reportEntryDetailsList);
						                 
										}
						            	else if(response.data.status == 'error')
						            		{
						            		$rootScope.stopLoader();
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
							
							$scope.init = function(){	
								
								//$stateParams.resultMstObj.labSampleDtlsId
								$scope.findingResultDesc="";
								$scope.serologyResultMaster = {
									  "serologyTestResultId" :'',
									  "labSampleDtlsId" : "",
									  "resultEnterBy" : $scope.userId,
									  "resultEnterDate" : "",
									  "resultValidatedBy" : $scope.userId,
									  "resultValidatedDate" : "",
									  "reportReleaseBy" : "",
									  "reportReleaseDate" : "",
									  "createdDate" : "",
									  "createdBy" : $scope.userId,
									  "updatedDate" : "",
									  "updatedBy" : $scope.userId,
									  "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "serologyResultTestMapperDtoList" : []
									}
								}
							
							/**Redirect Page Code*/
							if(angular.equals({},$stateParams.resultMstObj))
 							{
								$state.go('reportValidation',{searchObj : $stateParams.searchObj});
							} else {
								$scope.init();
								$scope.labResultMasterList = $stateParams.resultMstList;
                                $scope.nextRecordList = $stateParams.nextRecordList;
								$scope.getSerologyFindings();
							}
							$scope.serologyTestResultMapperList = [];
							
							
							$scope.isRedirect = false;
							$scope.provisional = false;
							
							$scope.saveReportValidation= function(isRedirect,provisionalRelease,isOnHoldParameter) 
							{
									try 
									{
										$scope.provisional = provisionalRelease;
											$rootScope.startLoader();
											$scope.isRedirect = isRedirect;
											var saveflag = false;
											var checkIsOnHoldSave = true;
											$scope.serologyResultMasterList = [];
											angular.forEach( $scope.reportEntryDetailsList,function(value,key){
												$scope.finalMapprList = [];
												var labSampleDtlsId = '';
												angular.forEach(value.validateList,function(value1,index){
													var newArr={};
													if(value1.findingResult!=undefined && value1.findingResult!=null && value1.findingResult != ''){
														newArr.findingResult = value1.findingResult;
														newArr.findingResultDesc = value.findingResultDesc;
														newArr.serologyTestResultId = value1.serologyTestResultId;
														newArr.serologyResultTestMpprId = value1.serologyResultTestMpprId;
														newArr.isAbnormal = (value1.isAbnormal!=undefined && value1.isAbnormal!=null)? value1.isAbnormal: 'N';
														$scope.finalMapprList.push(newArr);
														labSampleDtlsId = value1.labSampleDtlsId;
														checkIsOnHoldSave = false;
													}else{
														
														saveflag = true;
													}
												});
												$scope.serologyResultMaster.serologyResultTestMapperDtoList = $scope.finalMapprList;
												$scope.serologyResultMaster.labSampleDtlsId  = labSampleDtlsId;
												$scope.serologyResultMaster.internalNotes = value.internalNotes;
												$scope.serologyResultMaster.isOnHoldParameter = isOnHoldParameter;
												$scope.serologyResultMaster.provisional = $scope.provisional;
												$scope.serologyResultMasterList.push($scope.serologyResultMaster);
												$scope.init();
											});
											
											if(checkIsOnHoldSave  && isOnHoldParameter == true){ 
												
												$rootScope.stopLoader();
												growl.warning("Please enter at least one result. ",
														{
															title : 'warning!'
														});
												$scope.saveOrNextButton = false;
												return false;
												
											}
											
											
											if(saveflag  && isOnHoldParameter == false){ 
												
												$rootScope.stopLoader();
												growl.warning("Please Fill All Mandatory Fields.",
														{
															title : 'warning!'
														});
												$scope.saveOrNextButton = false;
												return false;
												
											}else{
											var URI =BASE_URL + ROOT_URL
											+ SAVE_SEROLOGY_VALIDATION_ENTRY;
											console.log("URI",URI);
											console.log("$scope.serologyResultMasterList",$scope.serologyResultMasterList);
											GenericService.serviceAction("POST", URI, $scope.serologyResultMasterList).then(function(response) 
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
													
													
													
	                                                if($scope.isRedirect || ($scope.nextRecordList.length) == $scope.nextRecordIndex){
													
	                                                	if($scope.consolidatedResultEntry!='Y'){
	                                                		$state.go('reportValidation',{searchObj : $stateParams.searchObj});
	                                                	}else{
	                                                		$state.go('consolidatedReportValidation',{searchObj : $stateParams.searchObj});
	                                                	}
	                                                }else{
	                                                	$scope.init();
	                                                	$scope.serologyResultMasterList= [];
	                    								$scope.labResultMasterList = $scope.nextRecordList[$scope.nextRecordIndex].resultMstList;
	                    								$scope.getSerologyFindings();
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
										}
										
									} catch (e) {
										console.log("Exception",e.message);
									}
								
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
							$scope.goToBack = function(){
								 if($scope.consolidatedResultEntry!='Y'){
									   $state.go('reportValidation',{searchObj : $stateParams.searchObj});
								   }else if($scope.consolidatedResultEntry == 'Y'){
									   $state.go('consolidatedReportValidation',{searchObj:$stateParams.searchObj});
								   }
							}

							$scope.nextRecordIndex=0;
							 $scope.cnt = 0;
							   $scope.loadNextRecord = function(){
								   $scope.saveOrNextButton = true;
								   if($scope.cnt == 0){
									   $scope.nextRecordIndex=0;   
									   $scope.cnt = $scope.cnt+1;
								   }else{
									   $scope.nextRecordIndex = $scope.nextRecordIndex +1;
									   $scope.cnt = $scope.cnt+1;
								   }
								   $scope.saveReportValidation(false,false);
							   }
								$scope.showHelpValue = function(info,tIndex,cIndex)
								{
									
									try 
									{
										$rootScope.startLoader();
										$scope.tIndex = tIndex;
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
									$scope.reportEntryDetailsList[$scope.tIndex].validateList[$scope.cIndex].findingResult = helpValue;
									$scope.reportEntryDetailsList[$scope.tIndex].validateList[$scope.cIndex].isAbnormal = isAbnormal;
									
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
									var labSampleDtlsListTemp = groupBy($scope.reportEntryDetailsList, 'labSampleDtlsId');
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
								   
								 //for recollect
									
									  $scope.showRejecPopup = function(isAccRej,index)
		                              {
										//if(isAccRej=='R')
										//{
										  $scope.rejIndex = index;
										  $scope.sampleRejectReasonId = "";
											 $scope.sampleRejectReason = ""
										  $("#accptRejInPatient").modal('show');
									    //}
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
										
								   $scope.recollectSampleFromValidation= function() 
									{
									   

											try 
											{
												  $("#accptRejInPatient").modal('hide');
												  $('.modal-backdrop').remove()
													$(document.body).removeClass("modal-open");
													$rootScope.startLoader();
													$scope.serologyResultMasterList = [];
													angular.forEach( $scope.reportEntryDetailsList,function(value,key){
														$scope.finalMapprList = [];
														$scope.obj={};
														angular.forEach(value.validateList,function(value1,index){
															var newArr={};
																newArr.labSampleDtlsId = value1.labSampleDtlsId;
																newArr.serologyTestResultId = value1.serologyTestResultId;
																$scope.finalMapprList.push(newArr);
														});
														$scope.obj.orgUnitId  = $scope.orgUnitId;
														$scope.obj.orgId  = $scope.orgId;
														$scope.obj.serologyResultTestsList = $scope.finalMapprList;
														$scope.obj.updatedDate = moment(new Date).format("YYYY-MM-DD HH:mm:ss");
														$scope.obj.updatedBy = $scope.userId;
														$scope.obj.sampleRejectReason= $scope.sampleRejectReason;
														$scope.obj.sampleRejectReasonId= $scope.sampleRejectReasonId;

													});
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
			                                        
													var URI =BASE_URL + ROOT_URL+ "/LIS/Serology/recollectSampleForSerlogy";
													GenericService.serviceAction("POST", URI, $scope.obj).then(function(response) 
													{	
														if (response.data.status == 'success')
														{
															$rootScope.stopLoader();
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
			                    								if($scope.consolidatedResultEntry!='Y'){
			                                                		$state.go('reportValidation',{searchObj : $stateParams.searchObj});
			                                                	}else{
			                                                		$state.go('consolidatedReportValidation',{searchObj : $stateParams.searchObj});
			                                                	}
//			                                                }	
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
												console.log("Exception",e.message);
											}
										
									}
								   
								   
									
								   $scope.retestSampleFromValidation= function() 
									{
											try 
											{
													$rootScope.startLoader();
													$scope.serologyResultMasterList = [];
													angular.forEach( $scope.reportEntryDetailsList,function(value,key){
														$scope.finalMapprList = [];
														$scope.obj={};
														angular.forEach(value.validateList,function(value1,index){
															var newArr={};
																newArr.labSampleDtlsId = value1.labSampleDtlsId;
																newArr.serologyTestResultId = value1.serologyTestResultId;
																$scope.finalMapprList.push(newArr);
																
														});
														$scope.obj.orgUnitId  = $scope.orgUnitId;
														$scope.obj.orgId  = $scope.orgId;
														$scope.obj.serologyResultTestsList = $scope.finalMapprList;
														$scope.obj.updatedDate = moment(new Date).format("YYYY-MM-DD HH:mm:ss"),
														$scope.obj.updatedBy = $scope.userId,
														$scope.init();
													});
													
												
													var URI =BASE_URL + ROOT_URL
													+ "/LIS/Serology/retestSampleForSerlogy";
													GenericService.serviceAction("POST", URI, $scope.obj).then(function(response) 
													{	
														if (response.data.status == 'success')
														{
															$rootScope.stopLoader();
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
			                    								if($scope.consolidatedResultEntry!='Y'){
			                                                		$state.go('reportValidation',{searchObj : $stateParams.searchObj});
			                                                	}else{
			                                                		$state.go('consolidatedReportValidation',{searchObj : $stateParams.searchObj});
			                                                	}
//			                                                }	
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
												console.log("Exception",e.message);
											}
										
									}
						} ]);