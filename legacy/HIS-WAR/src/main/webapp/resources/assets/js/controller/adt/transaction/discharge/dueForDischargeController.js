 'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:dueForDischargeController
 * @description #dueForDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'dueForDischargeController',
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
						'PaginationService',
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,PaginationService,BillingGenericService,$window) {
							
							 $scope.dateOptions = {							 
								      formatYear: 'yyyy',
								      showWeeks: false,
								      minDate:new Date(),
								      maxDate: new Date()
								  };
							 
							 $scope.dateOptionsPdd = {							 
								      formatYear: 'yyyy',
								      showWeeks: false,
								      minDate:new Date()
								  };
							 
								 $scope.openDatePicker = function() {
									
								      $scope.datepicker.opened = true;
								     
								    };
								    $scope.datepicker = {
										      opened: false
										    };
								    
								    $scope.openDatePickerPdd = function() {
										// alert("in");
									      $scope.datepickerPdd.opened = true;
									    };
									    $scope.datepickerPdd = {
											      opened: false
											    };

									    $scope.pagination = {
						                    currentPage: 1,
						                    totalPages: 0,
						                    pageSize: '10',
						                    search: '',
						                    searchData: PaginationService.searchData,
						                    setTotalPages: PaginationService.setTotalPages,
						                    getPagesList: PaginationService.getPagesList,
						                    setPage: PaginationService.setPage,
						                    setFirstPage: PaginationService.setFirstPage,
						                    setPreviousPage: PaginationService.setPreviousPage,
						                    setNextPage: PaginationService.setNextPage,
						                    setLastPage: PaginationService.setLastPage,
						                    sortBy: PaginationService.sortBy
						                  }
									    
							/* init() function for form object create. */
							$scope.init = function() {
								
								$scope.datepicker = new Date();
								//$rootScope.dueForDischargeLable = "Due For Discharge";
								$scope.dueForDischargeFlage = false;
								$(".selectedPageName").text("Due for Discharge");    
									    
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.doctorId = cookieObject.doctorId;
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.btnDisableEnable = false;

								$scope.admittedPatient = {};
								$scope.itemSelecteds = {};

								var data = {
										organizationId : $scope.organizationId
								};
								
								$scope.cD = new Date();
								$scope.cD = moment($scope.cD)
										.format('DD-MM-YYYY');
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									pdd : $scope.cD + ' 24:00:00',
									doctorId : $scope.doctorId
								};
								//patient redirected from bed management

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								if ($scope.bedManagmentCookiesObj != null) 
								{
									//$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
									commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
									//$cookies.remove('bedManagmentCookies');
								}
								//end
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDISCHARGETYPELIST;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.dischargeTypeList = [];

											if (response.data.status == "success") {
												$scope.dischargeTypeList = response.data.listObject;

											}
										});
						
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEREASONLIST;
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									$scope.reasonList = [];

									if (response.data.status == "success") {
										$scope.reasonList = response.data.listObject;

									}
								});

								

								var URI = BASE_URL + ROOT_URL
										+ GETDISCHARGEADMITTEDPATIENTLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													
													$scope.pagination.data=[];
													
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
														$scope.filterDate = new Date();
														$scope.filterDate = moment($scope.filterDate)
																.format('DD/MM/YYYY');
														$scope.admittedPatientList = response.data.listObject;
														angular.forEach($scope.admittedPatientList, function( value, index) {
																			var newArr = {};
																			newArr.specialityName = value.specialityName;
																			newArr.doctorFullName = value.dFirstName;
																			newArr.patientFullName = value.pFirstName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.userName = value.userName;
																			newArr.pddTime = value.pddTime;
																			newArr.dob = value.dob;
																			newArr.datepicker = value.doa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.doctorId = value.doctorId;
																			newArr.bedId = value.bedId;
																			newArr.nursingStationDesc = value.nursingStationDesc;
																			newArr.wardName = value.wardName;
																			newArr.visitTypeId=value.visitTypeId;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.ageStr = value.ageStr;
																			newArr.imagePath = value.imagePath;
																			newArr.prefixDesc = value.prefixDesc;
																			newArr.isVip = value.isVip;
																			newArr.isCriticalOrder = value.isCriticalOrder;
																			$scope.listOfAdmittedPatientList
																					.push(newArr);

																		});
														
														 if($scope.listOfAdmittedPatientList.length > 0){            
									                         PaginationService.searchData($scope.listOfAdmittedPatientList, $scope.pagination);
									                       }
									                	 

													}
												});


							}
							
							
						
							$scope.init();

							$scope.popupDischarge = function(item)
							{
								$scope.dischargeTypeId =0;
								$scope.itemForDischargePatient = item;
								
								var data = {
										admissionId: item.admissionId,
								};
								
								var URI = BASE_URL+ ROOT_URL+ "/api/opd/getPatientReturnDetails";
								GenericService.serviceAction("POST", URI,data).then(
										function(response) {

											if (response.data.status == "success") 
											{
												if(response.data.listObject == null || response.data.listObject.length == 0){
													angular.element('#popupDischarge').modal('show');
												}else{
													growl
													.warning(
															'Return Medication not accepted',
															{
																title : 'Warning!'
															});
												}
												
												$rootScope.stopLoader();
												
											} else if(response.data.status == "error")
											{
												
												if(response.data.message!=null){
													growl.error(response.data.message,{title : 'Error!'});
												}else{
												 growl.error('Something went wrongs...!!',{title : 'Error!'});
												}
//												
											}
										});
						
								
							}
							
							$scope.dueForDischarge = function(item)
							{
								$scope.datepickerPdd = new Date();
								$scope.note="";
								$scope.reasonId = 0;
								$scope.itemForDischargePatient = item;
								$scope.noOfHours = item.noOfHours;
								
								var data = {
										admissionId: item.admissionId,
								};
								
								var URI = BASE_URL+ ROOT_URL+ "/api/opd/getPatientReturnDetails";
								GenericService.serviceAction("POST", URI,data).then(
										function(response) {

											if (response.data.status == "success") 
											{
												if(response.data.listObject == null || response.data.listObject.length == 0){
													angular.element('#dueForDischarge').modal('show');
												}else{
													growl
													.warning(
															'Return Medication not accepted',
															{
																title : 'Warning!'
															});
												}
												
												$rootScope.stopLoader();
												
											} else if(response.data.status == "error")
											{
												
												if(response.data.message!=null){
													growl.error(response.data.message,{title : 'Error!'});
												}else{
												 growl.error('Something went wrongs...!!',{title : 'Error!'});
												}
//												
											}
										});
						
								
								
							}
							
							   $scope.updatePatientDetailsInSQL = function(){
	                              	 $rootScope.startLoader();
	                              	  var data ={}; 
	                              	   data.uhidnumber = $scope.itemForDischargePatient.uhidnumber;
	                              	   data.unitId =$scope.unitId;
	                              	   data.organizationId =$scope.organizationId;
	                              	   data.updatedBy = $scope.userId;
	                              	   data.createdBy = $scope.userId;
	                              	
	                              	   
	                                   var URI = BASE_URL + ROOT_URL+"/api/global/updatePatientAsDeath";
	                                   BillingGenericService
	                                           .serviceAction(METHOD_POST,URI, data)
	                                           .then(
	                                                   function(response) {
	                                                       if (response.data.status == "success"){
	                                                      	 growl.success("Updated successfully",
	           														{
	           															title : 'Success!'
	           														});
	                                                           $rootScope.stopLoader();
	                                                       }else{
	                                                           $rootScope.stopLoader();
	                                                       }
	                                                   });
	                                   }
							   
							$scope.saveDischargeRequest = function() {
								
								if($scope.dischargeTypeId == 0 || $scope.dischargeTypeId == '')
									{
									growl
									.warning(
											'Please select discharge reason..!!',
											{
												title : 'Warning!'
											});
									return false;
									
									}
								else if($scope.dischargeTypeId != 3 && $scope.datepicker.opened == false)
									{
										growl
										.warning(
												'Please select future date..!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								else{
								$rootScope.startLoaderPopup();
								$scope.btnDisableEnable = true;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									dischargeTypeId : $scope.dischargeTypeId,
									admissionId : $scope.itemForDischargePatient.admissionId,
									doctorId :  $scope.itemForDischargePatient.doctorId,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									destinationId :$scope.itemForDischargePatient.bedId,
									dischargeDate : moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
									dischargeStatusId: 1,
									visitTypeId : $scope.itemForDischargePatient.visitTypeId,
									patientId:$scope.itemForDischargePatient.patientId
								};
								console.log(data);
								//$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + ADDDISCHARGEREQUEST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$rootScope.stopLoaderPopup();
														growl
																.success(
																		'Discharge Request Raised Successfully...!!',
																		{
																			title : 'Success!'
																		});
														
														angular.element('#popupDischarge').modal('hide');
														if($scope.dischargeTypeId == "3"){
															$scope.updatePatientDetailsInSQL();
														}
														$scope.init();
													} else {
														$rootScope.stopLoaderPopup();
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							}
							
							
							$scope.checkDatePdd = function(){
								var new_date = new Date();
								 if(!angular.isDate($scope.datepicker)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 $scope.datepicker = new Date();
										return false;
								 }else if(new_date > $scope.datepicker)
									 {
									 growl
										.warning(
												"PDD should be equal or greater then today's date",
												{
													title : 'Warning!'
												});
									 $scope.datepicker = new Date();
										return false;
									 }
							}
							
							$scope.updateExtendPdd = function()
							{
								
								/*if($scope.reasonId == 0 || $scope.reasonId == '')
								{
								growl
								.warning(
										'Please select reason..!!',
										{
											title : 'Warning!'
										});
								return false;
								
								}
							else*/ if($scope.datepickerPdd == undefined || $scope.datepickerPdd == null || $scope.datepickerPdd == '')
								{
									growl
									.warning(
											'Please select future date..!!',
											{
												title : 'Warning!'
											});
									return false;
								}
							else{
								$rootScope.startLoaderPopup();
								$scope.btnDisableEnable = true;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									reasonId : $scope.reasonId,
									admissionId : $scope.itemForDischargePatient.admissionId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									pdd : moment($scope.datepickerPdd).format('DD-MM-YYYY HH:mm:ss'),
									noteCancel : $scope.note
								};
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ UPDATEDISCHARGEPDD;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$rootScope.stopLoaderPopup();
														growl
																.success(
																		'Discharge Request Raised Successfully...!!',
																		{
																			title : 'Success!'
																		});
														angular.element('#dueForDischarge').modal('hide');
														$scope.init();
														
													} else {
														$rootScope.stopLoaderPopup();
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							  }
							}

						
							$scope.getClassForColor = function(info)
							{

								   if(info.isCriticalOrder==1){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
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
