'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:shortLeaveStatusController
 * @description #shortLeaveStatusController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'shortLeaveStatusController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'$stateParams',
						'reportService','BillingGenericService','GenericServiceFileUpload','PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,reportService,BillingGenericService,GenericServiceFileUpload,PagerService) {

							
							/* init() function for form object create. */
							$scope.init = function() {

								/*	if ($cookies.get('bedManagmentCookies') != null) {
										$scope.bedManagmentCookiesObj = JSON
												.parse($cookies
														.get('bedManagmentCookies'));
										console.log($scope.bedManagmentCookiesObj);
										$scope.patientFullName = $scope.bedManagmentCookiesObj.patientFullName;
										$scope.uhidNum = $scope.bedManagmentCookiesObj.uhidNum;
										$scope.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
										$scope.doctorFullName = $scope.bedManagmentCookiesObj.doctorFullName;
										$scope.wardName = $scope.bedManagmentCookiesObj.wardName;
										$scope.admissionId  =  $scope.bedManagmentCookiesObj.admissionId;
									  } else {
										$scope.bedManagmentCookiesObj = null;
									}*/

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$scope.shortLeaveRequest = {}
								$scope.itemSelecteds = {};
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
							/*	$scope.consentFormFlag  = false;
								$scope.uploadDocumentFlag = false;
								$scope.handOverMedicationFlag   = false;
								$scope.releasepatientFlag = false;*/
					

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								/**Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.commonList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeywordPat;
								$scope.commonListCount;
								$scope.pager = {};
								$scope.page;

								
								var dataObjForReason = {
										organizationId : $scope.organizationId,
								};
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEREASONLIST;
						GenericService
								.serviceAction("POST", URI, dataObjForReason)
								.then(
										function(response) {
											$scope.reasonList = [];

											if (response.data.status == "success") {
												$scope.reasonList = response.data.listObject;

											}
										});
							
						
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
						

							}

							$scope.init();

							
							$scope.initShortLeaveRequestList = function(orgId, offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
							
								$scope.shortLeaveRequestList = [];
								
								var shortLeaveStatusList = [];
								shortLeaveStatusList.push(1);
								shortLeaveStatusList.push(2);
								shortLeaveStatusList.push(3);
								shortLeaveStatusList.push(4);
								shortLeaveStatusList.push(5);
								shortLeaveStatusList.push(6);
								shortLeaveStatusList.push(7);
								shortLeaveStatusList.push(8);
								shortLeaveStatusList.push(9);
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var data = {
										organizationId : orgId,
										unitId : $scope.unitId,
										shortLeaveStatusIdList :shortLeaveStatusList,
										noOfRecordsPerPage : noOfRecordsPerPage,
										offset :offset
									};
								
								var URI = BASE_URL + ROOT_URL
										+ GETSHORTLEAVEREQUESTLIST;
								$scope.shortLeaveRequestList = [];
							
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														
														if($scope.bedManagmentCookiesObj!=null)
														{
															angular	.forEach(response.data.listObject,function(value,index)
																	{
																		if($scope.bedManagmentCookiesObj.admissionId == value.admissionId)
																			{
																				$scope.shortLeaveRequestList.push(value);
																			}
																	});
														}else{
															$scope.shortLeaveRequestList = response.data.listObject;
														}
														
														if($scope.shortLeaveRequestList.length==0)
														{
															growl.warning(
																	'No record found...!!',
																	{
																		title : 'Warning!'
																	});
														}
														
														$scope.commonListCount = response.data.object;
														$scope.setPage(1, false);
													}
													$rootScope.stopLoader();
												});
							}
							
							$scope.initShortLeaveRequestList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage);
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveShortLeaveRequest();
							}
							
							$scope.selectItem = function(item) {
								
								angular.element( document.querySelector( '#consentForm' ) ).addClass('disabled');
				            	angular.element( document.querySelector( '#uploadDocument' ) ).addClass('disabled');
				            	angular.element( document.querySelector( '#handoverMedication' ) ).addClass('disabled');
				            	angular.element( document.querySelector( '#releasePatient' ) ).addClass('disabled');
				            	angular.element( document.querySelector( '#rejectPatient' ) ).addClass('disabled');

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.shortLeaveRequest) {
									$scope.itemSelecteds = item;
									
									/*$scope.consentFormFlag  = false;
									$scope.uploadDocumentFlag = false;
									$scope.handOverMedicationFlag   = false;
									$scope.releasepatientFlag = false;*/
									if(item.shortLeaveStatusId != 1 )
										{
										 angular.element( document.querySelector( '#consentForm' ) ).removeClass('disabled');
						            	 angular.element( document.querySelector( '#uploadDocument' ) ).removeClass('disabled');
										}
									//if(item.shortLeaveStatusId == 3 && item.isHandoverMedication == 'Y')
									if(item.shortLeaveStatusId == 3)
										{
										angular.element( document.querySelector( '#releasePatient' ) ).removeClass('disabled');
										angular.element( document.querySelector( '#rejectPatient' ) ).removeClass('disabled');
										}
									if(item.isHandoverMedication == 'Y')
									{
									angular.element( document.querySelector( '#handoverMedication' ) ).removeClass('disabled');
									}
									
									
								}
							}
							$scope.commonFuntion = function() {
								console.log($scope.itemSelecteds);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								}
								else{
								
								if($scope.itemSelecteds.patientId > 0)
								{
								var age = $rootScope
								.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName =$scope.itemSelecteds.patientName
								+ " "
								+age
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
							else
								{
								$scope.patientFullName = $scope.itemSelecteds.patientName
								+ " "
								+ $scope.itemSelecteds.dob
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
								$scope.uhidNum = $scope.itemSelecteds.uhidNumber;
								$scope.doctorFullName =$scope.itemSelecteds.doctorName;
								$scope.bedNumber = $scope.itemSelecteds.bedNumber;
								$scope.dateOfAdmission =$scope.itemSelecteds.doa;
								$scope.patientDischargeDate = $scope.itemSelecteds.pdd;
								$scope.identificationNo = $scope.itemSelecteds.identificationNo;
								$scope.admissionId = $scope.itemSelecteds.admissionId;

							}
							}
							
							$scope.releasePatient = function()
							{
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								}
								else{
									
									var data = {
											admissionId : $scope.itemSelecteds.admissionId,
											shortLeaveId : $scope.itemSelecteds.shortLeaveId,
											updatedBy : $scope.userId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											updatedDate : $scope.currentDate,

										}
										console.log(data);
										//return false;
									$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL
												+ RELEASEPATIENTFORSHOTLEAVE;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Patient Released Successfully...!!',
																				{
																					title : 'Success!'
																				});
																
																$scope.init();
															} else {
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
															angular.element( document.querySelector( '#consentForm' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#uploadDocument' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#handoverMedication' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#releasePatient' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#rejectPatient' ) ).addClass('disabled');
											            	$rootScope.stopLoader();
														});
								
								}
								
							}
							
							
							//consent form
							$scope.selectedTemplates = [];
							$scope.getConsentTemplate = function(typeId) {
								$scope.selectedTemplates = [];
									if($scope.itemSelecteds.patientId > 0)
										{
										var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
										$scope.patientFullName = $scope.itemSelecteds.patientFullName
										+ " "
										+age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
										}
									else
										{
										$scope.patientFullName = $scope.itemSelecteds.patientFullName
										+ " "
										+ $scope.itemSelecteds.dob
										+ "/"
										+ $scope.itemSelecteds.genderCode;
										}
										
									
									$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
									$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
									$scope.bedNumber = $scope.itemSelecteds.bedNumber;
									$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
									$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
									//$scope.getPassIssuedList($scope.itemSelecteds.admissionId);
									
								
						    	
									if(typeId==9){
										$("#checkBx").prop("checked", false);
										$scope.templateObj = { "consentTransactionId":"",
											"consentMasterId":"",
											"admissionId":$scope.itemSelecteds.admissionId,
											"encounterId":"",
											"templateDesc":"",
											"status":"A",
											"createdBy":$scope.userId,
											"createdDate":"",
											"updatedBy":$scope.userId,
											"updatedDate":"",
											"unitId":$scope.unitId,
											"organizationId":$scope.organizationId,
											"consentTypeId":"1"}
										
										
						    		  var data={
						    				  organizationId : $scope.organizationId,
						    				  consentTypeId : 1,
							        		  admissionId:$scope.itemSelecteds.admissionId
							        		}
							        	
							        	$rootScope.startLoader();
						        	    var URI = BASE_URL + ROOT_URL + "/adt/consentTemplate";
						        	    GenericService.serviceAction(METHOD_POST,URI,data).then(function(response) {
							    			if (response.data.status == "success")
							    			{
							    				
							    			 	$scope.templateList = response.data.listObject;
							    			 	$('#concentTemplate').modal('show');
												$rootScope.stopLoader();
											}
							    			else if(response.data.status == "error")
							    				{
							    				$rootScope.stopLoader();
							    				}
											
							    		});
									}
							}

							//view consent template
							$scope.viewTemplate  = function(templateObj,idx)
					    	{
								var selectedList=[];
								angular.forEach($scope.selectedTemplates,function(value,index){
									if(value.consentTransactionId!=null)
									selectedList.push(value.consentTransactionId);
									else
										selectedList.push(value.consentMasterId);
								});
//								var index;
								/*if(templateObj.consentTransactionId!=null)
									index=selectedList.indexOf(templateObj.consentTransactionId);
								else
									index=selectedList.indexOf(templateObj.consentMasterId);
								if(index==-1){
									growl.error(
											'Please Select Template.',
											{
												title : 'Error!'
											});
									return false;
								}*/
								if($scope.selectedTemplates.length>0 && $scope.selectedTemplates[idx].templateDesc!=undefined)
									{
										$scope.value = templateObj.templateDesc;
										$scope.templateIndex = idx;
										$('#addviewTemplateModalAdmission').modal('show');
									} 
								else{
									growl.warning(
											'Please select template..!!',
											{
												title : 'Warning!'
											});
								}
								
					    	}
							
							//save template
							$scope.saveTemplate = function()
							{
								 $scope.selectedTemplates[$scope.templateIndex].templateDesc = $scope.value;
								 $('#addviewTemplateModalAdmission').modal('hide');
									growl
									.success(
											'Consent Saved Successfully...!!',
											{
												title : 'Success!'
											});
							};
							
							//print consent form template
							$scope.printTemplate = function(){
								if($scope.selectedTemplates.length==0)
									{
									growl
									.warning(
											'Please select template..!!',
											{
												title : 'Warning!'
											});
									}
								else{
									
									var templateSaveList = [];
									angular.forEach($scope.selectedTemplates, function(value, key)
											{
										            var obj = angular.copy($scope.templateObj);
										            obj.consentTransactionId = value.consentTransactionId;
										            obj.consentMasterId =value.consentMasterId; 
										            obj.templateDesc =value.templateDesc;
										            templateSaveList.push(obj);
										   });
							        	
							         $rootScope.startLoader();
						    		 $('#concentTemplate').modal('hide');
						    		 $('#addviewTemplateModalAdmission').modal('hide');
						        	    var URI = BASE_URL + ROOT_URL + "/adt/patientConsent";
						        	    GenericService.serviceAction(METHOD_POST,URI,templateSaveList).then(function(response) {
							    			if (response.data.status == "success")
							    			{
												$rootScope.stopLoader();
												angular.forEach($scope.selectedTemplates, function(value, key)
														{
													
													 var data =  
													  {
												            "appName": "HIS",
												            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
												            "reportDesignFilePath": "getConsent.rpt",
												            "reportFormat": "PDF",
												            "exportFileName": "consentRpt",
												            "reportParams": [
												              {
													             "paramName": "admission_id",
													             "paramType": "Integer",
													             "paramValue": $scope.itemSelecteds.admissionId
												              },
												              {
														             "paramName": "consent_master_id",
														             "paramType": "Integer",
														             "paramValue": value.consentMasterId
													           },
													           {
														             "paramName": "consent_type_id",
														             "paramType": "Integer",
														             "paramValue": 1//admission consent
													           }
												            ]
												         };
													 reportService.generate(REPORT_API_URI1,data);
													
													   });
											}
							    			else if(response.data.status == "error")
							    				{
							    				$rootScope.stopLoader();
							    				}
							    		});
								}
							}
							
							//document show and save
							$scope.openDocument = function()
							{
								
								$scope.encounterDetails = {
										  encounterDocId : "",
										  encounterId : "",
										  admissionId:"",
										  visitTypeId:2,
										  patientId : "",
										  documentPath : "",
										  documentTypeId : "",
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  organisationId : $scope.organizationId,
										  file : {}
										}
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
									$scope.encounterDetailsList =[];
								}
								else
								{
									$scope.encounterDetailsList =[];
									if($scope.itemSelecteds.patientId > 0)
									{
									var age = $rootScope
									.getAge($scope.itemSelecteds.dob);
									$scope.patientFullName = $scope.itemSelecteds.patientName
									+ " "
									+age
									+ "/"
									+ $scope.itemSelecteds.genderCode;
									$scope.genderCode = $scope.itemSelecteds.genderCode;
									}
								else
									{
									$scope.patientFullName = $scope.itemSelecteds.patientName
									+ " "
									+ $scope.itemSelecteds.dob
									+ "/"
									+ $scope.itemSelecteds.genderCode;
									$scope.genderCode = $scope.itemSelecteds.genderCode;
									}
									
								
								$scope.uhidNum = $scope.itemSelecteds.uhidNumber;
								$scope.doctorFullName =$scope.itemSelecteds.doctorName;
								$scope.bedNumber = $scope.itemSelecteds.bedNumber;
								$scope.dateOfAdmission = $scope.itemSelecteds.doa;
								$scope.patientDischargeDate = $scope.itemSelecteds.pdd;
									
									$scope.encounterDetailsList =[];
									var encounterDetails = angular.copy($scope.encounterDetails);
									encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
									encounterDetails.patientId = $scope.itemSelecteds.patientId;	
									$scope.encounterDetailsList.push(encounterDetails);
									$('#commonUploadDocuments').modal('show');
									$scope.getPatientDocumentList();
								}
							}
							
							
							$scope.addImageDetails = function()
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
								encounterDetails.patientId = $scope.itemSelecteds.patientId;
								$scope.encounterDetailsList.push(encounterDetails);
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }	
							//upload patient document
							$scope.uploadEncounterDoc = function(Obj)
							{
								$('#commonUploadDocuments').modal('hide');
								 var fd = new FormData();
							        fd.append('encounterId', 0);
							        fd.append('visitTypeId', Obj.visitTypeId);
							        fd.append('admissionId', Obj.admissionId);
							        fd.append('patientId',Obj.patientId );
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('organisationId', Obj.organisationId);
							        fd.append('file',Obj.file );
							        fd.append('expenseId', 0);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('incidentReportingMapperId',0);
							        fd.append('companyExpenseId',0);
							        fd.append('createdDate',null);
									fd.append('fileEditedName',null);
									fd.append('opbId',0);
									fd.append('vendorId',0);
							        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										return response;
									});
							}
							
							$scope.uploadDoc = function(){

								 $rootScope.startLoader();
								 $scope.finalDocUploadList=[];
								  angular.forEach($scope.encounterDetailsList, function(value, key) {  
									  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
											 growl.warning("Please select document type .",
																		{
																			title : 'warning!'
																		});
															   $rootScope.stopLoader();
											return false;
										}
//									  if (value.file == undefined || value.file == "" || value.file == null ) {
									  if (angular.equals(value.file, {}) || value.file==undefined) {
												growl.warning('Please select file.',
																{
																	title : 'warning!'
																});
												 $rootScope.stopLoader();
												return false;
											} 
										 var ext = value.file.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
											}
											$scope.finalDocUploadList.push(value);
								});
								  if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
									  
								
								  $scope.count = 0;
								 angular.forEach($scope.finalDocUploadList, function(value, key) 
									{  
									 if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
										$rootScope.stopLoader();
										return false;
									}
									 if (value.file.size == undefined || value.file.size == "" || value.file.size == 0 ) {
										
											 $rootScope.stopLoader();
											return false;
										} 
									 var ext = value.file.name.split(".").pop().toLowerCase();
										if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
										
											return false;
										}
										 
										var promise = $scope.uploadEncounterDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.finalDocUploadList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $rootScope.stopLoader();
														   $scope.encounterDetailsList = [];
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
							}
							
							
							 $scope.getPatientDocumentList = function()
						        {
						         $rootScope.startLoader();
						         var data =
						           {
							           "patientId":$scope.itemSelecteds.patientId,
							           "unitId":$scope.unitId,
							           "organisationId":$scope.organizationId,
							           "userId":$scope.userId,
							           "visitTypeId":$scope.itemSelecteds.visitTypeId,
							           "admissionId":$scope.itemSelecteds.admissionId
						          }
						         var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
						         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						           $scope.patientDocumentList = [];
						           $scope.patientDocumentListTemp = [];
						           $scope.patientCorporateDocumentList = [];
						           $scope.patientInsuranceDocumentList = [];
						           if (response.data.status == "success") 
						           {
						            $scope.patientDocumentListTemp = response.data.listObject;
						            
						            angular.forEach($scope.patientDocumentListTemp, function(value, key) 
											{  
						            			if(value.documentTypeId==19){				            				
						            			
							            			var data={
							            					documentTypeId:value.documentTypeId,
							            					documentName:value.documentName,
							            					documentPath:value.documentPath,
							            					createdDate:value.createdDate
							            			}
							            			$scope.patientCorporateDocumentList.push(data);
						            			}
						            			if(value.documentTypeId==30){				            				
							            			
							            			var data={
							            					documentTypeId:value.documentTypeId,
							            					documentName:value.documentName,
							            					documentPath:value.documentPath,
							            					createdDate:value.createdDate
							            			}
							            			$scope.patientInsuranceDocumentList.push(data);
						            			}
						            			if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
							            			
							            			var data={
							            					documentTypeId:value.documentTypeId,
							            					documentName:value.documentName,
							            					documentPath:value.documentPath,
							            					createdDate:value.createdDate
							            			}
							            			$scope.patientDocumentList.push(data);
						            			}
											});
						            $rootScope.stopLoader();
						           }
						           else if(response.data.status == "error")
						            {
						            $rootScope.stopLoader();
						            }
						          });
						        }
							 
							 //Document Type list
								$scope.getDocumentType = function(){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.documentTypeList = [];
											if (response.data.status == "success") {
												$scope.documentTypeList = response.data.listObject;
												 angular.forEach($scope.documentTypeList, function(value, key) 
															{  
													 			$scope.corporateDocTypeId=value.documentTypeId;
																	
															});
											}
										});
								}
								$scope.getDocumentType();
								
								$scope.openImage = function(index)
	                              { 
									$rootScope.startLoader();
									var imageUrl = $scope.patientDocumentList[index].documentPath;
									var href = $rootScope.currentHrefDoccument();
									$.fancybox.open({
										width: 600, // or whatever
										height: 320,
										href:href+imageUrl,
										type: 'iframe'
									});
	                         $rootScope.stopLoader();
	                        }
								
						//Short LeaverRejection By nurse
								$scope.saveAcceptReject = function()
								{
									if($('#rejectReasonIdDrpDN').val()== ""){
										 growl.warning(
													'Please select reject reason...!!',
													{
														title : 'Warning!'
													});
										 angular.element('#accepatReject').modal('show');
										return false;	
									}else{
								    	angular.element('#accepatReject').modal('hide');
								    	$rootScope.startLoader();
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');
									
										var data = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												shortLeaveId:$scope.itemSelecteds.shortLeaveId,
												updatedBy:$scope.userId,
												updatedDate:$scope.currentDate,
												nurseNote :$('#noteRejectDrpDN').val(),
												rejectionReasonId : $('#rejectReasonIdDrpDN').val(),
										};
										
										console.log(data);
									  //return false;
										var URI = BASE_URL + ROOT_URL
												+ REJECTSHOTLEAVEBYNURSE;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Request rejected...!!',
																				{
																					title : 'Success!'
																				});
																//accepatReject
																$scope.init();
															} else {
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
															angular.element( document.querySelector( '#consentForm' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#uploadDocument' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#handoverMedication' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#releasePatient' ) ).addClass('disabled');
											            	angular.element( document.querySelector( '#rejectPatient' ) ).addClass('disabled');
															$rootScope.stopLoader();
														});
								    }
									
								}			
								
								$scope.setNoOfRecords = function() {
									$scope.initShortLeaveRequestList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage);
								};
								
								$scope.getShortLeaveRequestList = function(orgId, offset, noOfRecordsPerPage) {
									$rootScope.startLoader();
									
									var shortLeaveStatusList = [];
									shortLeaveStatusList.push(1);
									shortLeaveStatusList.push(2);
									shortLeaveStatusList.push(3);
									shortLeaveStatusList.push(4);
									shortLeaveStatusList.push(5);
									shortLeaveStatusList.push(6);
									shortLeaveStatusList.push(7);
									shortLeaveStatusList.push(8);
									shortLeaveStatusList.push(9);
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									
									var data = {
											organizationId : orgId,
											unitId : $scope.unitId,
											shortLeaveStatusIdList :shortLeaveStatusList,
											noOfRecordsPerPage : noOfRecordsPerPage,
											offset :offset
										};
									
									var URI = BASE_URL + ROOT_URL
											+ GETSHORTLEAVEREQUESTLIST;
									$scope.shortLeaveRequestList = [];
									
									var URI = BASE_URL + ROOT_URL + GETSHORTLEAVEREQUESTLIST;
											
									console.log("URI", URI);
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$rootScope.stopLoader();
										$scope.shortLeaveRequestList = response.data.listObject;
									});
								}

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages)
									{
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
									if (flag)
									{
										$scope.getShortLeaveRequestList($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
									}
								}
								
								$scope.acceptRejectionNoteObj={};
								$scope.showAcceptRejectionData=function(obj){
									$scope.acceptRejectionNoteObj=obj;
								}
						} ]);
