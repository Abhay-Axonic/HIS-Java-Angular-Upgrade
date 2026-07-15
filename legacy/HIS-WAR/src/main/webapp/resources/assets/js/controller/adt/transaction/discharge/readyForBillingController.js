'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:readyForBillingController
 * @description #readyForBillingController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'readyForBillingController',
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
								$state,$stateParams,BillingGenericService,$window) {

							/* init() function for form object create. */
							$scope.visitTypeIdflag='2';
							$scope.dropdown = 'N';
							$scope.init = function() {
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								
								var appConfigObject = $cookies.getObject('cookieObjectAppConfig');
								$scope.radioLogySpecialityId = appConfigObject.radiologySpecialityId;
								
								$scope.roleName = appConfigObject.roleName;
								$scope.nurseRole = appConfigObject.nurseRole;	 
								$scope.doctorRole = appConfigObject.doctorRole;
								$scope.isShowPatientByBed=appConfigObject.isShowPatientByBed;
								
								var labConfig  =    $scope.checkLabConfig();
								$scope.labSpecialityId = labConfig.labSpecialityId;
								
								
								$rootScope.loginpage = true;
								$scope.dischargeIdForSubmit="";
								$scope.admissionIdForSubmit="";
								$scope.visitTypeIdForSubmit="";
								
								$scope.wardList = [];
								var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
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
								   /*console.log('nursingStationCookie',nursingStationCookie);
								   $scope.nursingStationDesc=nursingStationCookie;
								   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
									console.log("departmentNursingStationMapperDtoslist",$scope.departmentNursingStationMapperDtoslist);*/
								  }
								
								 $scope.nurseBedList = [];
									var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
									 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y' ){
										 growl.warning(
													'No bed mapped with nurse !!',
													{
														title : 'Success!'
													});
									 }
									 else
									  {
									   $scope.nurseBedList =mappedNurseBedList; 
									  console.log('Nurse Bed List',$scope.nurseBedList);
									  }
								
								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									visitTypeId : $scope.visitTypeIdflag,
									wardList : $scope.wardList,
									labDeptId:$scope.labSpecialityId,
									radDeptId:$scope.radioLogySpecialityId
								};
								
								
								if ($scope.isShowPatientByBed == 'Y') {
									if($scope.nurseBedList.length>0){
									 commonObj.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								//patient redirected from bed management
								if ($scope.bedManagmentCookiesObj != null) {
									//$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
									commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
									//$cookies.remove('bedManagmentCookies');
									if($scope.bedManagmentCookiesObj.visitTypeId == 3)
										$scope.visitTypeIdflag = "3";
									else
										$scope.visitTypeIdflag = "2";
								}
								//end
								if($scope.nurseRole == 1 && $scope.wardList.length > 0){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ GETREADYFORBILLINGPATIENTLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
														$scope.admittedPatientList = response.data.listObject;
														angular
																.forEach(
																		$scope.admittedPatientList,
																		function(
																				value,
																				index) {

																			var newArr = {};
																			newArr.specialityName = value.specialityName;
																			newArr.dFirstName = value.dFirstName;
																			newArr.pFirstName = value.pFirstName;
																			newArr.reasonDesc = value.reasonDesc;
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
																			newArr.dischargeId = value.dischargeId;
																			newArr.futureDischargeDate = value.futureDischargeDate;
																			newArr.dischargeStatus = value.dischargeStatus;
																			newArr.dischargeTypeId = value.dischargeTypeId;
																			newArr.floorName = value.floorName;
																			//newArr.labCountGreen = value.labCountGreen;
																			newArr.labCountRed = value.labCountRed;
																			newArr.labCountYellow = value.labCountYellow;
																			//newArr.radiologyCountGreen = value.radiologyCountGreen;
																			newArr.radiologyCountRed = value.radiologyCountRed;
																			newArr.radiologyCountYellow = value.radiologyCountYellow;
																			//newArr.otherSerCountGreen = value.otherSerCountGreen;
																			newArr.otherSerCountRed = value.otherSerCountRed;
																			newArr.otherSerCountYellow = value.otherSerCountYellow;
																			newArr.salesRed = value.salesRed;
																			newArr.salesYellow = value.salesYellow;
																			//newArr.salesGreen = value.salesGreen;
																			newArr.procedureRed = value.procedureRed;
																			//newArr.procedureGreen = value.procedureGreen;
																			newArr.procedureYellow = value.procedureYellow;
																			newArr.pharmecyRed = value.pharmecyRed;
																			//newArr.pharmecyGreen = value.pharmecyGreen;
																			newArr.pharmecyYellow = value.pharmecyYellow;
																			newArr.dischargeMedicationRed = value.dischargeMedicationRed;
																			//newArr.dischargeMedicationGreen = value.dischargeMedicationGreen;
																			newArr.dischargeMedicationYellow = value.dischargeMedicationYellow;
																			newArr.dischargeSummaryGreen = (value.dischargeSummaryGreen!=undefined && value.dischargeSummaryGreen!=null && value.dischargeSummaryGreen> 0 ) ? 0 : 1;
																			newArr.imagePath = value.imagePath;
																			newArr.age = value.ageStr;
																			newArr.prefixDesc = value.prefixDesc;
																			newArr.visitTypeId=value.visitTypeId;
																			newArr.visitorPassRed = value.visitorPassRed
																			newArr.visitorPassYellow = value.visitorPassYellow
																			$scope.listOfAdmittedPatientList.push(newArr);
																			

																		});
														$rootScope.stopLoader();

													}else{
														$rootScope.stopLoader();
													}
												});
							      }
							}
							
							
							$scope.checkPendingStuff = function(info)
							{
								$scope.message = "";
								
								$scope.dischargeIdForSubmit=info.dischargeId;
								$scope.admissionIdForSubmit=info.admissionId;
								$scope.visitTypeIdForSubmit=info.visitTypeId;
								
								if(info.labCountRed >0)
								{
									$scope.message = "Pending service(s) detected for lab, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}	
								else if (info.radiologyCountRed > 0)
								{
									$scope.message = "Pending service(s) detected for radilology, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else if (info.procedureRed > 0)
								{
									$scope.message = "Pending procedure(s) detected, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else if (info.otherSerCountRed > 0) 
								{
									$scope.message = "Pending service(s) detected, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else if (info.salesRed > 0 )
								{
									$scope.message = "Pending pharmacy sales return detected, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else if (info.pharmecyRed > 0) 
								{
									$scope.message = "Pending pharmacy sale(s) detected, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else if (info.dischargeSummaryGreen > 0)
								{
									$scope.message = "The discharge summary is not yet generated, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else if (info.visitorPassRed > 0) 
								{
									$scope.message = "Pending visitor pass(es) detected, still do you want to continue ? ";
									angular.element('#confirmation').modal('show');
								}else{
									$scope.finalDischarge(info.dischargeId,info.admissionId,info.visitTypeId);
								}
								
							}
							
							$scope.finalDischarge = function(dischargeId,admissionId , visitTypeId)
							{
								
								angular.element('#confirmation').modal('hide');
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									dischargeStatusId : 3,
									dischargeId :dischargeId,
									admissionId :admissionId,
									visitTypeId : visitTypeId
								};
								


								if ($scope.radioLogySpecialityId != undefined) {
									data.radDeptId = $scope.radioLogySpecialityId;
								} else {
									data.radDeptId = null;
								}
								if ($scope.labSpecialityId != undefined) {
									data.labDeptId = $scope.labSpecialityId;
								} else {
									data.labDeptId = null;
								}
								
								
								
								console.log(data);
								// return false;
								var URI = BASE_URL + ROOT_URL
										+ FINALDISCHARGEFORPATIENT;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl
																.success(
																		'Submitted Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.init();
													} else {
														$rootScope.stopLoader();
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}

							$scope.init();

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
											//console.log($scope.patientDocumentListTest.length);
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
