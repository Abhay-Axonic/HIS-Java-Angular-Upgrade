'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:patientListController
 * @description #patientListController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'patientListController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'PagerService',
						'$stateParams','reportService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, GenericServiceFileUpload,growl, $state,PagerService,
								$stateParams, reportService, $window) {

							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.patientList = [];
								$scope.noRecordMsg = true;
								
								$scope.patientObj = {

									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									genderId : '',
									countryCallingCode : '',
									email : '',
									nationalityId : '',
									raceId : '',
									maritalStatusId : '',
									mobileNumber : '',
									dob : '',
									identificationTypeId : '',
									identificationNumber : '',
									isVip : '',
									address : '',
									countryId : '',
									stateId : '',
									districtId : '',
									cityId : '',
									areaId : '',
									zipCode : '',
									phoneCode : '',
									phoneNumber : '',
									companyName : '',
									companyAddress : '',
									companyCountryId : '',
									companyStateId : '',
									companyDistrictId : '',
									companyCityId : '',
									companyAreaId : '',
									companyZipCode : '',
									companyMobileNumber : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:'1'
								};

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

								var data = {};

								var URI = BASE_URL + ROOT_URL
										+ GETHEALTHCARD;								
								
								
							}
							
							$scope.init();
							
							//for datepicker Search
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate: new Date()
							};

							$scope.openDatePickerSearch = function() {
						          $scope.datepickerSearchS.opened = true;
									
							};

							$scope.datepickerSearchS = {
								opened : false
							};
							
                           $scope.patientSearchObj= {};
							
							/*$scope.searchPatient = function()
							{
								if($scope.patientSearchObj.uhidNumber == undefined && $scope.patientSearchObj.patientName == undefined && $scope.patientSearchObj.uhid == undefined && $scope.patientSearchObj.mobileNumber == undefined &&  ($scope.datepickerSearch==null || $scope.datepickerSearch==""))
				           			{
											growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
											});
											return false;
					           		}
									
									else
										{
										
										    $scope.noRecordMsg = false;
											var ConDate = $('#datepickerSearch').val();
											if(ConDate != '')
											$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
											$scope.patientSearchObj.organizationId = $scope.organizationId;
											$scope.patientSearchObj.unitId = $scope.unitId;
											$scope.patientSearchObj.registrationTypeId =0;
											$scope.patientSearchObj.isFromPatientList='Y';
											$rootScope.startLoader();
											console.log("$scope.patientSearchObj",$scope.patientSearchObj);
													var URI = BASE_URL + ROOT_URL
															+ GETTEMPORYPATIENTFORPREREGISTRATION;

													BillingGenericService
															.serviceAction(METHOD_POST,
																	URI, $scope.patientSearchObj,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		$scope.patientList = [];
																		if (response.data.status == "success")
																			$rootScope.stopLoader();
																			$scope.patientList = response.data.listObject;
																			console.log($scope.patientList)
																			$scope.datepickerSearch="";
																			$scope.patientSearchObj.dob ="";
																	});
										}
							}
				
							*/
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.searchPatient($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.searchPatient = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
							$scope.datepickerSearch = new Date();
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								
								
									
									    $scope.noRecordMsg = false;
										var ConDate = $('#datepickerSearch').val();
										if(ConDate != '')
										$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
										$scope.patientSearchObj.organizationId = $scope.organizationId;
										$scope.patientSearchObj.unitId = $scope.unitId;
										$scope.patientSearchObj.registrationTypeId =0;
										$scope.patientSearchObj.isFromPatientList='Y';
										$scope.patientSearchObj.offset = offset;
										$scope.patientSearchObj.noOfRecordsPerPage = noOfRecordsPerPage;
										$rootScope.startLoader();
										console.log("$scope.patientSearchObj",$scope.patientSearchObj);
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATIONPATIENTLIST;

												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	$scope.patientList = [];
																	if (response.data.status == "success")
																		$rootScope.stopLoader();
																		$scope.patientList = response.data.listObject;
																		console.log($scope.patientList)
																		$scope.datepickerSearch="";
																		$scope.patientSearchObj.dob ="";
																		$scope.commonListCount = response.data.object;
																		$scope.setPage(1,false);
																});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						
									
									    $scope.noRecordMsg = false;
										var ConDate = $('#datepickerSearch').val();
										if(ConDate != '')
										$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
										$scope.patientSearchObj.organizationId = $scope.organizationId;
										$scope.patientSearchObj.unitId = $scope.unitId;
										$scope.patientSearchObj.registrationTypeId =0;
										$scope.patientSearchObj.isFromPatientList='Y';
										$scope.patientSearchObj.offset = offset;
										$scope.patientSearchObj.noOfRecordsPerPage = noOfRecordsPerPage;
										$rootScope.startLoader();
										console.log("$scope.patientSearchObj",$scope.patientSearchObj);
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATIONPATIENTLIST;

												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	$scope.patientList = [];
																	if (response.data.status == "success")
																		$rootScope.stopLoader();
																		$scope.patientList = response.data.listObject;
																		console.log($scope.patientList)
																		$scope.datepickerSearch="";
																		$scope.patientSearchObj.dob ="";
																});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.searchPatient($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							
							
							$scope.checkDate= function()
							{
								if($scope.datepickerSearch == undefined || $scope.datepickerSearch == null || $scope.datepickerSearch == ''){
									 $scope.age = "";
									   growl.warning("Date is invalid",{
											title : "warning"
										});
										$scope.datepickerSearch = new Date();
									
									   return false;
								}
							
							}

							
							


							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.savePatientRegistration();
							}
							
							
							$scope.getHealthCard = function(item)
							{
								var data =  {
             				            "appName": "HIS",
             				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
             				            "reportDesignFilePath": "Healthcard.rpt",
             				            "reportFormat": "PDF",
             				            "exportFileName": "HealthCard",
             				            "reportParams": [
             				              {
             				            	 "paramName": "patient_id",
							                  "paramType": "Integer",
							                  "paramValue":  item.patientId
							                
             				              }
             				            ]
             				 };
             					reportService.generate(REPORT_API_URI1,data);
								
								
							}
							
							 $scope.myLifeBaseUrl = "";
							  $scope.getMyLifeBaseUrl = function(){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/gtMyLifeBaseUrl";
									BillingGenericService.serviceAction(METHOD_POST, URI, "").then(function(response) {
										$scope.myLifeBaseUrl = "";
											if (response.data.status == "success") {
												$scope.myLifeBaseUrl = response.data.object.myLifeLink;
											}
										});
								}
								$scope.getMyLifeBaseUrl();
							
							$scope.sendMyLifeMail = function(patientObj)
							{
								if(patientObj.uuid==null || $scope.myLifeBaseUrl == "" || $scope.myLifeBaseUrl == null)
									{
										growl.warning("Patient is not registered in My-Life",{title : "Warning!"
									});
									return false;
									}
								else{
									$rootScope.startLoader();
									var data ={
											"patientId":patientObj.patientId,
											"uuid":patientObj.uuid
										};
									
									var URL = $scope.myLifeBaseUrl + "/sync/patients/sendmylifeapkmail";
									BillingGenericService.serviceAction(METHOD_POST, URL , data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") 
										{
											growl.success("Mail send to user successfully.",{title : "Success!"
											});
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											growl.warning("Patient is not registered in My-Life",{title : "Warning!"
											});
											$rootScope.stopLoader();
											}
									});
								}
							}
							
							
							$scope.updateStatus = function(id, status) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								//type = (type == "A") ? "A" : "I";

								var data = {
									patientId : id,
									status : status,
									updatedBy : $scope.userId,
								}
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ "/api/global/status";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
														
														//$scope.searchPatient();
														
													} else {
														
														if(response.data.code==409)
															{
																
															 angular.forEach($scope.patientList, function(value, key) {  
																 if(value.patientId == id)
																 {
																 	value.status='A';
																 }
																 
																 $scope.message = response.data.message;
																 angular.element('#cannotDeactive').modal('show');
																
																 });
																
																
															}else{
																growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
															}
													}
													$rootScope.stopLoader();
												});
							}
							
							
							$scope.getPatientDocumentList = function(obj)
						     {
						      
						      $rootScope.startLoader();
						      var data =
						        {
						        "patientId":obj.patientId,
						        "unitId":$scope.unitId,
						        "organisationId":$scope.organizationId,
						        "userId":undefined,
						        "visitTypeId":undefined,
						        "admissionId":undefined,
						       }
						      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
						      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						        $scope.patientDocumentList = [];
						        if (response.data.status == "success") 
						        {
						        	$scope.patientDocumentList = response.data.listObject;  
						           
						            $rootScope.stopLoader();
						        }
						        else if(response.data.status == "error")
						         {
						        	$rootScope.stopLoader();
						         }
						       });
						     }
							/***********************Sneha Dalave******************/
							/******************Doccument Upload********************/
							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
								  encounterDocId : "",
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
								  file : {},
                                  remark : ""
								}
							$scope.showDocUPloadPoup   = function(encounterObj)
							{
								 $('#fileId').val(undefined);
								$scope.encounterDetailsList=[];
								$scope.encounterDetails.patientId = encounterObj.patientId;	
								if($scope.encounterDetailsList.length>0)
								{
								  $('#uploadDetails').modal('show');
								}
							else{
								 $('#uploadDetails').modal('show');
								var encounterDetails = angular.copy($scope.encounterDetails);
								encounterDetails.patientId = encounterObj.patientId;
								$scope.encounterDetailsList.push(encounterDetails);
							}
								$scope.getPatientDocumentList(encounterObj);
								
									
							}
							$scope.addImageDetails = function()
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								$scope.encounterDetailsList.push(encounterDetails);
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }	
						  
						  $scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") {
											$scope.documentTypeList = response.data.listObject;
										}
									});
							}
							$scope.getDocumentType();
							
							 $scope.uploadPatientImage = function()
							    {
								  $scope.finalDocUploadList=[];
								  var count=0;
								 angular.forEach($scope.encounterDetailsList, function(value, key) {  
									  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
											 growl.warning("Please select document type .",
																		{
																			title : 'warning!'
																		});
															   $rootScope.stopLoader();
											return false;
										}
									  if (angular.equals(value.file, {}) || value.file==undefined) {
											growl.warning('Please select file.',
															{
																title : 'warning!'
															});
											 $rootScope.stopLoader();
											return false;
										}
//										 var ext = value.file.name.split(".").pop().toLowerCase();
//											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
//												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
//														{
//															title : 'warning!'
//														});
//												$rootScope.stopLoader();
//												return false;
//											}
											$scope.finalDocUploadList.push(value);
											 console.log("final list",$scope.finalDoc);
								});
								 
								 if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
									   $('#uploadDetails').modal('hide');
								
								 $rootScope.startLoader();
								  $scope.count = 0;
								 angular.forEach($scope.encounterDetailsList, function(value, key) 
									{
										var promise = $scope.uploadEncounterDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.encounterDetailsList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $scope.finalDocUploadList=[];
														   $scope.encounterDetailsList = [];
														   $rootScope.stopLoader();
														 }
												      }
												  else 
													{
														growl.error('Something went wrong.',
																{
																	title : 'Error!'
																});
													}
											    });
											
									});
								 }
								
							    }
								$scope.uploadEncounterDoc = function(Obj)
								{
									 var fd = new FormData();
								        fd.append('encounterId', 0);
								        fd.append('visitTypeId', 1);
								        fd.append('admissionId', 0);
								        fd.append('patientId',Obj.patientId );
								        fd.append('documentTypeId', Obj.documentTypeId);
								        fd.append('createdBy',Obj.createdBy );
								        fd.append('updatedBy', Obj.updatedBy);
								        fd.append('unitId', Obj.unitId);
								        fd.append('organisationId', Obj.organisationId);
								        fd.append('expenseId', 0);
								        fd.append('file',Obj.file );
								        fd.append('expenseId', 0);
								        fd.append('grnId',0);
										fd.append('poId',0);
								        fd.append('incidentReportingMapperId',0);
								        fd.append('companyExpenseId',0);
                                        fd.append('remark',Obj.remark );
                                        fd.append('createdDate',null);
										fd.append('fileEditedName',null);
										fd.append('opbId',0);
										fd.append('vendorId',0);
										fd.append('vendorId',0);
								        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
								        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
										{	
											return response;
										});
								}
						
								
								/***Chetana**/
							    $scope.printPatientLedger = function(info){
							    var data =  {
             				            "appName": "HIS",
             				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
             				            "reportDesignFilePath": "get_pl_ledger.rpt",
             				            "reportFormat": "PDF",
             				            "exportFileName": "Certificate",
             				            "reportParams": [
             				              {
             				            	 "paramName": "Patient_id",
							                  "paramType": "Integer",
							                  "paramValue":  info.patientId
							                
             				              }
             				            ]
             				 };
             					reportService.generate(REPORT_API_URI1,data);
						}
							   /*****/
							    
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

                        $scope.removeDocument = function(Obj, index)
                        {
                            try {
                                $rootScope.startLoader();
                                var data = {
                                        "encounterDocId":Obj.encounterDocId
                                    };
                                var URI = BASE_URL + ROOT_URL+"/api/opd/patientDoc/status/update";

                                GenericService
                                        .serviceAction(METHOD_POST, URI, data)
                                        .then(
                                                function(response) {
                                                    $rootScope.stopLoader();
                                                    if(response.data.status == "success"){
                                                        growl.success("Document removed successfully ",
                                                                {
                                                                    title : 'Success!'
                                                                });
                                                        $scope.patientDocumentList.splice(index,1);
                                                    }
                                                });
                            } catch (e) {
                                console.log(e.message)
                            }
                        }


						
						} ]);