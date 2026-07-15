'use strict';



angular
		.module('myApp')
		.controller(
				'dayCareFinalDischargeController',
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
								$scope.isEr = false;
								$rootScope.loginpage = true;
								
								$scope.visitTypeIdflag ='3';
								$scope.dropdown = 'Y';
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
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								
								
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
								if ( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y') {
									growl.warning(
											'No bed mapped with nurse !!', {
												title : 'Success!'
											});
								} else {
									$scope.nurseBedList = mappedNurseBedList;
									console.log('Nurse Bed List',
											$scope.nurseBedList);
								}
								
								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
//									visitTypeId: 3,
									wardList : $scope.wardList
								};
			                     
								if ($scope.isShowPatientByBed == 'Y') {
									if($scope.nurseBedList.length>0){
									 commonObj.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];
									}
								}
								
								$scope.visitTypeId= 3;
								//patient redirected from bed management
								if (sessionStorage.getItem('bedManagmentCookies') != undefined) {
									$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
									commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
									sessionStorage.removeItem('bedManagmentCookies');
								}
								//end
								if($scope.nurseRole == 1 && $scope.wardList.length > 0){
								var URI = BASE_URL + ROOT_URL
										+ GETFINALDISCHARGEPATIENTLIST;
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
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.dischargeId = value.dischargeId;
																			newArr.futureDischargeDate = value.futureDischargeDate;
																			newArr.dischargeStatusId = value.dischargeStatusId;
																			newArr.dischargeTypeId = value.dischargeTypeId;
																			newArr.floorName = value.floorName;
																			newArr.dischargeTypeName = value.dischargeTypeName;
																			newArr.dischargeDesc= value.dischargeDesc;
																			newArr.bedId = value.bedId;
																			newArr.wardId  = value.wardId;
																			newArr.wardName  = value.wardName;
																			newArr.doctorId  = value.doctorId;
																			newArr.nursingStationId = value.nursingStationId;
																			newArr.floorId = value.floorId;
																			newArr.roomId = value.roomId;
																			newArr.bedCategoryId = value.bedCategoryId;
																			newArr.isIcu = value.isIcu;
																			newArr.billStatusCount = value.billStatusCount;
																			newArr.orderDetailsCount = value.orderDetailsCount;
																			newArr.isFinancialClearance = value.isFinancialClearance;
																			newArr.referalDocId = value.referalDocId;
																			newArr.age = value.ageStr;
																			newArr.prefixDesc = value.prefixDesc;
																			
																			if(value.visitTypeId==3){
																				newArr.visitTypeId=value.visitTypeId;
																				$scope.listOfAdmittedPatientList.push(newArr);
																				}

																		});

													}
												});
							}

						

							}

							$scope.init();

						$scope.vocateBedAgainstPatient = function(item)
						{
							$rootScope.startLoader();
							console.log(item);
							$scope.currentDate = new Date();
							$scope.currentDate = moment($scope.currentDate)
									.format('DD-MM-YYYY HH:mm:ss');
							
							var data={
									organizationId :$scope.organizationId,
									unitId : $scope.unitId,
									dischargeId : item.dischargeId,
									patientId : item.patientId,
									doctorId : item.doctorId,
									bedNumber : item.bedNumber,
									admissionId :item.admissionId,
									bedId : item.bedId,
									updatedDate: $scope.currentDate,
									updatedBy : $scope.unitId,
									wardId  : item.wardId,
									wardName  : item.wardName,
									nursingStationId : item.nursingStationId,
									floorId : item.floorId,
									roomId : item.roomId,
								    bedCategoryId :item.bedCategoryId,
								    isIcu : item.isIcu,
								    referalDocId : item.referalDocId
							}
							
							console.log(data);
							//return false; 
							var URI = BASE_URL + ROOT_URL
									+ PATIENTFINALDISCHARGE;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													growl
															.success(
																	'Patient Discharged Successfully...!!',
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

						} ]);

