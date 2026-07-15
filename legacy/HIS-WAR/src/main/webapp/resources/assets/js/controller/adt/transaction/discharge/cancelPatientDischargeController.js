// 'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:dueForDischargeController
 * @description #dueForDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'cancelPatientDischargeController',
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
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams, BillingGenericService, $window) {
							
							 $scope.dateOptions = {							 
								      formatYear: 'yyyy',
								      showWeeks: false,
								      minDate: new Date(),
								  };
								 $scope.openDatePicker = function() {
									// alert("in");
								      $scope.datepickerFutureDate.opened = true;
								    };
								    $scope.datepickerFutureDate = {
										      opened: false
										    };

							/* init() function for form object create. */
							$scope.init = function() {

								//$rootScope.dueForDischargeLable = "Due For Discharge";
								$scope.dueForDischargeFlage = false;
								$(".selectedPageName").text("Cancel Discharge");    
									    
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.cookieDoctorId = cookieObject.doctorId;
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								 
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								
								$scope.isRMO=cookieObject.isRMO;
								if($scope.isRMO==undefined){
									$scope.isRMO='N';
								}

								var rmoMappedWardList=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								
								$scope.wardList = [];
						/*		var nursingStationsWardsList = $cookies.getObject('nursingStationsWardsList');
								 if(nursingStationsWardsList == undefined){
									 growl.warning(
												'No ward mapped with selected nursing station !!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   console.log('nursingStationsWardsList',nursingStationsWardsList);
								   $scope.wardList = nursingStationsWardsList; 
								   console.log('nursingStationCookie',nursingStationCookie);
								   $scope.nursingStationDesc=nursingStationCookie;
								   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
									console.log("departmentNursingStationMapperDtoslist",$scope.departmentNursingStationMapperDtoslist);
								  }*/
								 

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

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
									pdd : $scope.cD
								};
								
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								var commonObjForDischargeList = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										dischargeDate : $scope.cD,
										wardList :$scope.wardList
									};
								
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

								
				
				
				if($scope.doctorRole == 1)
				{
			/*	var URI = BASE_URL + ROOT_URL
				+ GETDISCHARGEPATIENTLISTFORNURSING;
	    	GenericService
				.serviceAction("POST", URI, commonObjForDischargeList)
				.then(
						function(response) {
							$scope.listOfAdmittedPatientList = [];
							$scope.admittedPatientList = [];

							if (response.data.status == "success") {
								$scope.filterDate = new Date();
								$scope.filterDate = moment($scope.filterDate)
										.format('DD/MM/YYYY');
								$scope.admittedPatientList = response.data.listObject;
								angular
										.forEach(
												$scope.admittedPatientList,
												function(
														value,
														index) {

													var newArr = {};
													newArr.specialityName = value.specialityName;
													newArr.doctorFullName = value.dFirstName;
													newArr.patientFullName = value.pFirstName;
													newArr.reasonDesc = value.reasonDesc;
													newArr.dob = value.dob;
													newArr.datepicker = value.doa;
													newArr.datepickerPdd = value.pdd;
													newArr.genderCode = value.genderCode;
													newArr.uhidnumber = value.uhidnumber;
													newArr.patientId = value.patientId;
													newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
													newArr.bedNumber = value.bedNumber;
													newArr.admissionId = value.admissionId;
													newArr.prefixCode = value.prefixCode;
													newArr.identificationNo = value.identificationNo;
													newArr.dischargeId = value.dischargeId;
													newArr.futureDischargeDate = value.futureDischargeDate;
													newArr.dischargeStatusId = value.dischargeStatusId;
													newArr.dischargeTypeId = value.dischargeTypeId;
													newArr.dischargeTypeName = value.dischargeTypeName;
													newArr.dischargeDesc = value.dischargeDesc;
													newArr.doa = value.doa;
													newArr.pdd = value.pdd;
													newArr.age = value.ageString;
													newArr.wardName = value.wardName;
													newArr.dischargeSummaryId = value.dischargeSummaryId;
													newArr.genderId=value.genderId;
													newArr.admissionNumber = value.admissionNumber;
													$scope.listOfAdmittedPatientList
															.push(newArr);

												});

							}
						     });*/
					
					var URI = BASE_URL + ROOT_URL
					+ GETIPDPATIENTBYDOCTORIDCANCELDISCHARGE;
			     console.log("URI",URI);
			 	var commonObjDoc = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
						doctorId : $scope.cookieDoctorId
					};
			 	if($scope.isRMO=="Y"){
			 		commonObjDoc.rmoMappedWardList=rmoMappedWardList!=undefined?rmoMappedWardList:null;
					commonObjDoc.isRMO='Y';
			 	}
			     console.log("commonObj",JSON.stringify(commonObjDoc));
			     
					//patient redirected from bed management
					if (sessionStorage.getItem('bedManagmentCookies') != undefined) {
						$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
						commonObjDoc.admissionId=$scope.bedManagmentCookiesObj.admissionId;
						sessionStorage.removeItem('bedManagmentCookies');
					}
					//end
					
			     $rootScope.startLoader();
			GenericService
					.serviceAction("POST", URI, commonObjDoc)
					.then(
							function(response) 
							{
								 $rootScope.startLoader();
								$scope.listOfAdmittedPatientList = [];
								$scope.admittedPatientList = [];
								if (response.data.status == "success")
								{
									$scope.admittedPatientList = response.data.listObject;
									angular.forEach($scope.admittedPatientList, function(value , key) 
									{
	                                	
										if($scope.bedManagmentCookiesObj!=null)
											{
												var newArr = {};
													
												if($scope.bedManagmentCookiesObj.admissionId==value.admissionId)
												{
												newArr.admissionId = value.admissionId;
													newArr.patientId = value.patientId;
													if( value.visitTypeId == 3)
													{
														newArr.visitTypeId = value.visitTypeId;
													}
													newArr.doctorId = value.doctorId;
													newArr.specialityId = value.specialityId;
													newArr.specialityName = value.specialityName;
													newArr.genderCode = value.genderCode;
													newArr.wardId = value.wardId;
													newArr.floorId = value.floorId;
													newArr.bedId = value.bedId;
													newArr.doa = value.doa;
													newArr.datepickerPdd = value.pdd;
													newArr.bedCategoryId = value.bedCategoryId;
													newArr.billingBedCategoryId = value.billingBedCategoryId;
													newArr.paymentEntitlementId = value.paymentEntitlementId;
													newArr.admissionNumber = value.admissionNumber;
													newArr.isShortLeave = value.isShortLeave;
													newArr.patientCategoryId = value.patientCategoryId;
													newArr.bedNumber = value.bedNumber;
													newArr.dFirstName = value.dFirstName;
													newArr.patientFullName = value.pFirstName;
													newArr.dob = value.dob;
													newArr.prefixId = value.prefixId;
													newArr.dischargeId = value.dischargeId;
													newArr.bedCategoryDesc = value.bedCategoryDesc;
													newArr.genderId = value.genderId;
													newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
													newArr.wardName = value.wardName;
													newArr.labOrder = value.labOrder;
													newArr.payeeId = value.payeeId;
													newArr.uhidnumber = value.uhidnumber;
													newArr.isShortLeave = value.isShortLeave;
													newArr.billStatusId = value.billStatusId;
													newArr.ageStr = value.ageStr;										
													newArr.prefixDesc = value.prefixCode;
													newArr.imagePath = value.imagePath;
													newArr.visitTypeId = value.visitTypeId;
												$scope.listOfAdmittedPatientList.push(newArr);
												}
											}else{
												var newArr = {};
												newArr.admissionId = value.admissionId;
												newArr.patientId = value.patientId;
												if( value.visitTypeId == 3)
												{
													newArr.visitTypeId = value.visitTypeId;
												}
												newArr.doctorId = value.doctorId;
												newArr.specialityId = value.specialityId;
												newArr.specialityName = value.specialityName;
												newArr.genderCode = value.genderCode;
												newArr.wardId = value.wardId;
												newArr.floorId = value.floorId;
												newArr.bedId = value.bedId;
												newArr.doa = value.doa;
												newArr.datepickerPdd = value.pdd;
												newArr.bedCategoryId = value.bedCategoryId;
												newArr.billingBedCategoryId = value.billingBedCategoryId;
												newArr.paymentEntitlementId = value.paymentEntitlementId;
												newArr.admissionNumber = value.admissionNumber;
												newArr.isShortLeave = value.isShortLeave;
												newArr.patientCategoryId = value.patientCategoryId;
												newArr.bedNumber = value.bedNumber;
												newArr.dFirstName = value.dFirstName;
												newArr.patientFullName = value.pFirstName;
												newArr.dob = value.dob;
												newArr.prefixId = value.prefixId;
												newArr.dischargeId = value.dischargeId;
												newArr.bedCategoryDesc = value.bedCategoryDesc;
												newArr.genderId = value.genderId;
												newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
												newArr.wardName = value.wardName;
												newArr.labOrder = value.labOrder;
												newArr.payeeId = value.payeeId;
												newArr.uhidnumber = value.uhidnumber;
												newArr.isShortLeave = value.isShortLeave;
												newArr.billStatusId = value.billStatusId;
												newArr.ageStr = value.ageStr;										
												newArr.prefixDesc = value.prefixCode;
												newArr.imagePath = value.imagePath;
												newArr.visitTypeId = value.visitTypeId;
												$scope.listOfAdmittedPatientList.push(newArr);
											}
										
	                                });
									$rootScope.stopLoader();
									
									if($scope.listOfAdmittedPatientList.length==0)
									{
										growl
										.warning(
												'No record found..!!',
												{
													title : 'Warning!'
												});
									}
								}
								else if(response.data.status == "error")
									{
									  $rootScope.stopLoader();
									}
							});
						}
				

							}
							
							
						
							$scope.init();

							$scope.popupDischarge = function(item)
							{
								$scope.dischargeTypeId =0;
								$scope.itemForDischargePatient = item;
							}
							
							$scope.dueForDischarge = function(item)
							{
								$scope.note="";
								$scope.reasonId = 0;
								$scope.itemForDischargePatient = item;
								$scope.noOfHours = item.noOfHours;
							}

						
							
							$scope.updateExtendPdd = function()
							{
								if($scope.reasonId == 0 || $scope.reasonId == '')
								{
								growl
								.warning(
										'Please select reason..!!',
										{
											title : 'Warning!'
										});
								return false;
								
								}
							else if($scope.datepicker == undefined || $scope.datepicker == "")
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
								$('#popupDischargeCancel').modal('hide');
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									organizationId : $scope.organizationId,
									dischargeId : $scope.itemForDischargePatient.dischargeId,
									unitId : $scope.unitId,
									reasonId : $scope.reasonId,
									admissionId : $scope.itemForDischargePatient.admissionId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									pdd : moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
									noteCancel : $scope.note,
									dischargeStatusId :6
								};
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ CANCELDISCHARGEINBILL;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.pddDate=moment($scope.datepicker).format('DD-MM-YYYY')
														$rootScope.stopLoader();
														growl
																.success(
																		'Discharge request cancelled and Extended PDD is '+$scope.pddDate,
																		{
																			title : 'Success!'
																		});
														angular.element('#popupDischargeCancel').modal('hide');
														$scope.init();
														$scope.datepicker = undefined;
													} else if (response.data.status == "error")
													{
														$rootScope.stopLoader();
														growl
																.error(response.data.message,
																		{
																			title : 'Error!'
																		});
														$scope.init();
													}
												});
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
