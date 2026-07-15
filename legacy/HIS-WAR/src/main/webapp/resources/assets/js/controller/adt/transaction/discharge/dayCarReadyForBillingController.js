'use strict';


angular
		.module('myApp')
		.controller(
				'dayCarReadyForBillingController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state) {

							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.visitTypeIdflag ='3';
								$scope.dropdown = 'Y';
								
								var appConfigObject = $cookies.getObject('cookieObjectAppConfig');
								$scope.radioLogySpecialityId = appConfigObject.radiologySpecialityId;
								
								$scope.roleName = appConfigObject.roleName;
								$scope.nurseRole = appConfigObject.nurseRole;	 
								$scope.doctorRole = appConfigObject.doctorRole;
								$scope.isShowPatientByBed=appConfigObject.isShowPatientByBed;
								var labConfig  =    $scope.checkLabConfig();
								$scope.labSpecialityId = labConfig.labSpecialityId;
								
								
								$rootScope.loginpage = true;
								
								
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
									 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y'){
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
									visitTypeId : 3,
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
								
								//patient redirected from bed management
								if (sessionStorage.getItem('bedManagmentCookies') != undefined) {
									$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
									commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
									sessionStorage.removeItem('bedManagmentCookies');
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
																			
																			newArr.labCountGreen = value.labCountGreen;
																			newArr.labCountRed = value.labCountRed;
																			newArr.labCountYellow = value.labCountYellow;
																			newArr.radiologyCountGreen = value.radiologyCountGreen;
																			newArr.radiologyCountRed = value.radiologyCountRed;
																			newArr.radiologyCountYellow = value.radiologyCountYellow;
																			newArr.otherSerCountGreen = value.otherSerCountGreen;
																			newArr.otherSerCountRed = value.otherSerCountRed;
																			newArr.otherSerCountYellow = value.otherSerCountYellow;
																			newArr.salesRed = value.salesRed;
																			newArr.salesYellow = value.salesYellow;
																			newArr.salesGreen = value.salesGreen;
																			newArr.procedureRed = value.procedureRed;
																			newArr.procedureGreen = value.procedureGreen;
																			newArr.procedureYellow = value.procedureYellow;
																			newArr.pharmecyRed = value.pharmecyRed;
																			newArr.pharmecyGreen = value.pharmecyGreen;
																			newArr.pharmecyYellow = value.pharmecyYellow;
																			newArr.imagePath = value.imagePath;
																			newArr.prefixDesc = value.prefixDesc;
																			newArr.age = value.ageStr;
																			if(value.visitTypeId==3){
																			newArr.visitTypeId=value.visitTypeId;
																			$scope.listOfAdmittedPatientList.push(newArr);
																			}
																		});

													}
													$rootScope.stopLoader();
												});
							      }
							}
							
							$scope.finalDischarge = function(dischargeId,admissionId)
							{
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
									visitTypeId : 3
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
														growl.success(
																		'Submitted Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.init();
													} else {
														$rootScope.stopLoader();
														growl.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}

							$scope.init();

						

						} ]);
