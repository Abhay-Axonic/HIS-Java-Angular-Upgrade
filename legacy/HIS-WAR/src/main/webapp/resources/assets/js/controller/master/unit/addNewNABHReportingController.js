angular
		.module('myApp')
		.controller(
				"addNewNABHReportingController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'$timeout',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'reportService',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'commonDetailService',
						'GenericServiceParamHeader',
						function($scope, $rootScope, $state, $cookies,
								GenericService, $timeout, PagerService, promiseFactory,
								$sessionStorage, growl,reportService, BillingGenericService,
								GenericServiceFileUpload, commonDetailService,
								GenericServiceParamHeader) {
							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Antibiotic Addition";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.popUpFlag = true;
							$scope.storeList = [];

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.usersList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							var cookieObject = $cookies
									.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}

							$scope.unitId = cookieObject.unitId;
							$scope.organisationId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.createdBy = cookieObject.userId;
							$scope.updatedBy = cookieObject.userId;
				            var cookieObjectAppConfig = {};
				            [cookieObjectAppConfig] = $rootScope.checkCookies();

				            var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
						    $scope.nursingStationsWardsListId = nursingStationsWardsList;

                            $scope.showPopover = false;

                                                    $scope.$on('$locationChangeStart', function(event) {
                                                      $scope.showPopover = false; // Close the popover when navigating to another page
                                                    });
							$scope.GetIncidentReportingData = function(id,dataFlag) {
								$scope.getEditedData = [];

								$scope.incidentReportingMapperId = id;
								if ($scope.incidentReportingMapperId == 0) {
									$scope.incidentReportingMapperId = null;
									$rootScope.stopLoader();
								}
								if ($scope.incidentReportingMapperId != null) {
									try {
										var URI = BASE_URL
												+ ROOT_URL
												+ "/api/unit/getNabhReportingMapperById";
										var obj = {
											"isDepartmentflag" : dataFlag,
											"nabhReportingMapperId" : id,
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId,
										}
										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, obj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console
																	.log(response);
															if (response.data.status == "success") {
																$scope.getEditedData = [];
																$scope.getEditedData = response.data.listObject;
																$scope
																		.getRolewiseUsers($scope.getEditedData[0].roleId);
																angular
																		.forEach(
																				$scope.getActiveNabhProcessList,
																				function(
																						value1,
																						key1) {
																					if ($scope.getEditedData[0].nabhProcessId == value1.nabhProcessId) {
																						$scope.getActiveRoleList = [];
																						$scope.getEditedData[0].nabhProcessDesc = value1.nabhProcessDesc;
																						angular
																								.forEach(
																										value1.incidentReportRoleMapperList,
																										function(
																												value,
																												index) {
																											var neeArr = {};
																											neeArr.id = value.roleId;
																											neeArr.label = value.roleName;
																											$scope.getActiveRoleList
																													.push(neeArr);

																										});
																						$scope.incidentReportTemplateMapperList = [];
																						$scope.incidentReportTemplateMapperList = angular
																								.copy(value1.incidentReportTemplateMapperList);
																					}

																				});
																// get template
																// mapped

																$scope.obj2 = {
																	'nabhProcessId' : $scope.getEditedData[0].nabhProcessId,
																	'nabhProcessDesc' : $scope.getEditedData[0].nabhProcessDesc,

																}
																$scope.nabhReportingMapperId = $scope.getEditedData[0].nabhReportingMapperId;
																$scope.nabhReportingDate = moment(
																		$scope.getEditedData[0].nabhReportingDate,
																		"DD-MM-YYYY")
																		.toDate();
																		$scope.nabhReportingDateTime = $scope.getEditedData[0].nabhReportingTime,
																		$scope.specialityId = $scope.getEditedData[0].specialityId;
																$scope.incidentCategoryId = $scope.getEditedData[0].incidentCategoryId;
																$scope.nabhProcessId = $scope.getEditedData[0].nabhProcessId;
																$scope.reportingTemplateId = $scope.getEditedData[0].reportingTemplateId;
																$scope.nabhTypeId = $scope.getEditedData[0].nabhTypeId;
																$scope.nabhRemark = $scope.getEditedData[0].nabhRemark;
																$scope.roleId = $scope.getEditedData[0].roleId;
																$scope.incidentUserId = $scope.getEditedData[0].userId;
																$scope.patientId = $scope.getEditedData[0].patientId;
																$scope.reportingTemplateObj = $scope.getEditedData[0].reportingTemplateObj;
																$scope.templateFormatId = $scope.getEditedData[0].templateFormatId;
																$scope.patientDetails.uhIdNumber = ($scope.getEditedData[0].uhidNumber);
																$scope.patientDetails.patientName = ($scope.getEditedData[0].patientName);
																$scope.conversationId = ($scope.getEditedData[0].conversationId);
																$scope.patientDetails.admissionId = ($scope.getEditedData[0].admissionId);
																$scope.patientDetails.encounterId = ($scope.getEditedData[0].encounterId);
																$scope.patientDetails.patientId = ($scope.getEditedData[0].patientId);
																$scope.patientDetails.visitTypeId = 1;
																$scope.patientDetails.doctorFullName = null;
																$scope.checkStatus($scope.incidentUserId);
																$rootScope.allergydata($scope.getEditedData[0]);

																if ($scope.templateFormatId == 1) {
																	document
																			.getElementById("htmlContent").innerHTML = $scope.reportingTemplateObj;
																} else if ($scope.templateFormatId == 2
																		|| $scope.templateFormatId == 3) {
																	   $scope.getReportedUserSavedData1[0].value = $scope.reportingTemplateObj;
																}
																$rootScope
																		.stopLoader();
															}
														});
									} catch (e) {
										console.log("Exception", e.message);
										$rootScope.stopLoader();
									}
								}

							}



							$scope.getTemplateConfig = function() {
								// $rootScope.startLoader();
								var obj = {};
								obj.unitId = $scope.unitId;
								var URI = BASE_URL + ROOT_URL
										+ "/api/global/templateConfig";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, obj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.templateConfigObject = {};
														$scope.templateConfigObject = response.data.object;
														// $rootScope.stopLoader();
													} else if (response.data.status == "error") {
														// $rootScope.stopLoader();
													}
												});
							}
							$scope.getTemplateConfig();

							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.incidentTypeId = 2;
								$scope.incidentReportingDate = new Date();
								$scope.incidentReportingDateTime = '',
										$scope.specialityId = '';
								$scope.incidentCategoryId = '';
								$scope.nabhProcessId = '';
								$scope.reportingTemplateId = '';
								$scope.incidentRemark = '';
								$scope.roleId = '';
								$scope.incidentUserId = $scope.userId;
								$scope.patientId = 0,
										$scope.incidentReportingMapperId = null;
								$scope.patientSearchObj = {
									"uhIdNumber" : "",
									"patientName" : "",
									"genderId" : "",
									"mobileNo" : "",
									"birthDate" : "",
									"identificationNo" : "",
									"patientType" : "1",
									"unitId" : $scope.unitId,
									"organizationId" : $scope.organizationId
								}

								$scope.getActiveIncidentReport = function(id) {
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/unit/getActiveNabhProcessMasterList";
									offset = 0;
									noOfRecordsPerPage = 10;
									var data1 = {
										organizationId : $scope.organisationId,
										unitId : $scope.unitId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
									};
									GenericService
											.serviceAction("POST", URI, data1)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$rootScope.stopLoader();
															$scope.getActiveNabhProcessList = [];
															angular.forEach(
																			response.data.listObject,
																			function(value,key) {
																				if (value.status == 'A') {
																					$scope.getActiveNabhProcessList.push(value);
																				}
																			});
															$scope.getActiveNabhProcessList.sort(function(a, b) {
												                return a.nabhProcessDesc.localeCompare(b.nabhProcessDesc);
												            });
															$scope.GetIncidentReportingData(commonDetailService.getDataId(),commonDetailService.getDataFlag());
														}
													});
								}
								$scope.getActiveIncidentReport();

							}
							$scope.initCommon();

							$scope.obj2 = {};

							/** ********from date***************** */

							$scope.openDatePickerDate = function() {
								$scope.datepickerDate.opened = true;
							};

							$scope.datepickerDate = {
								opened : false
							};

							$scope.dateOptionDate = {
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
							};

							// function for initializa clockpicker
							$scope.initClockPicker = function() {
                              $("#page-content-wrapper").scroll(function() {
                                    var inputField = $("input[id='time']");
                                    var topPos = inputField.offset().top;
                                    var leftPos = inputField.offset().left;
                                    var inputHeight = inputField.outerHeight();

                                    $(".popover").css("top", topPos + inputHeight + 5);
                                    $(".popover").css("left", leftPos);
                                });

                             $('.clockpicker').clockpicker();
                            };


							$scope.specialityMasterList = [];
							// ------------ Get Speciality Master List
							// ------------//
							$scope.getSpecialityMasterList = function() {
								try {
									var URI = BASE_URL + ROOT_URL
											+ GETACTIVESPECIALITY;
									var obj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													obj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if (response.data.status == "success") {
															$scope.specialityMasterList = response.data.listObject;
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
							}
							$scope.getSpecialityMasterList();

							// ------------ Get Active Category Maste List
							// ------------//
							$scope.getActiveCategoryMaster = function() {
								try {
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/global/unit/getActiveIncidentCategoryMaster/"
											+ $scope.organizationId + "/"
											+ $scope.unitId;
									var obj = {};
									BillingGenericService
											.serviceAction(METHOD_GET, URI,
													obj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if (response.data.status == "success") {
															$scope.incidentCategoryMasterList = [];
															$scope.incidentCategoryMasterList = response.data.listObject;
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
							}
							$scope.getActiveCategoryMaster();

							$scope.getUserList = function(id) {

								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/global/getActiveUserRoleMasterList";
								var data = {
									organisationId : $scope.organizationId,
									unitId : $scope.unitId
								}
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.getActiveUserList = [];
														angular
																.forEach(
																		response.data.listObject,
																		function(
																				value,
																				index) {
																			var data = {};
																			data.id = value.roleId;
																			data.label = value.roleName;
																			$scope.getActiveUserList
																					.push(data);
																		});
														console
																.log(
																		"getActiveRoleList",
																		$scope.getActiveRoleList);
													}
												});
							}

							$scope.getRoleMappedToIncidentReport = function(
									info) {
								$scope.getActiveRoleList = [];

								$scope.nabhProcessId = info.nabhProcessId;
								$scope.isCapaOff = info.isCapaOff;
								// get role mapped
								angular.forEach(
										info.incidentReportRoleMapperList,
										function(value, index) {
											var neeArr = {};
											neeArr.id = value.roleId;
											neeArr.label = value.roleName;
											$scope.getActiveRoleList
													.push(neeArr);

										});
								// get template mapped
								$scope.incidentReportTemplateMapperList = [];
								$scope.incidentReportTemplateMapperList = angular
										.copy(info.incidentReportTemplateMapperList);
							}

							// Role wise user
							$scope.getRolewiseUsers = function(id) {
								$scope.getRoleWiseUsersList = [];
								$scope.roleIdList = [];
								$scope.roleIdList.push(id);
								$scope.checkStatus($scope.userId);
                                $scope.incidentUserId=$scope.userId;
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/getRoleWiseUserList";
								offset = 0;
								noOfRecordsPerPage = 10;
								var data1 = {
									organizationId : $scope.organisationId,
									unitId : $scope.unitId,
									roleIdList : $scope.roleIdList

								};
								GenericService
										.serviceAction("POST", URI, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.getRoleWiseUsersList = response.data.listObject;
														console
																.log(
																		"$scope.getRoleWiseUsersList",
																		$scope.getRoleWiseUsersList);
														 $scope.incidentUserId = $scope.userId;
													}
												});
							}
							$scope.clearFun =function(){
								$scope.nabhReportingDate = "";
								$scope.nabhReportingDateTime = "";
								$scope.specialityId = "";
								$scope.nabhProcessId = "";
								$scope.roleId = "";
								$scope.incidentUserId = "";
								$scope.reportingTemplateId = "";
								$scope.incidentCategoryId ="";
								$scope.nabhProcessDesc ="";
								$scope.obj2 = "";
								$scope.patientDetails.patientName = "";
								$scope.patientDetails.uhIdNumber= "";
							}
 

							$scope.nabhTypeId = 2;
							$scope.set = function(tabValue) {
								$scope.nabhTypeId = tabValue;
								$scope.clearFun();
							}





							$scope.getPatientDetailsByIndex = function(index) {
								$scope.nabhReportingDate = "";
								$scope.nabhReportingDateTime = "";
								$scope.specialityId = "";
								$scope.nabhProcessId = "";
								$scope.roleId = "";
								$scope.incidentUserId = "";
								$scope.reportingTemplateId = "";
								$scope.patientDetails = {};
								$rootScope.patientLable = {};
								$rootScope.unknownPatient = {};

								if ($scope.patientList[index].isAbsconded == undefined
										|| $scope.patientList[index].isAbsconded == null
										|| $scope.patientList[index].isAbsconded == 'N') {
									$rootScope.patientLable.age = ($scope.patientList[index].age);
									$rootScope.patientLable.patientName = ($scope.patientList[index].patientName);
									$rootScope.patientLable.genderCode = ($scope.patientList[index].genderCode);
									$rootScope.patientLable.uhIdNumber = ($scope.patientList[index].uhIdNumber);
									$rootScope.patientLable.birthDate = ($scope.patientList[index].birthDate);
									$rootScope.unknownPatient.age = ($scope.patientList[index].age);
									$rootScope.unknownPatient.patientName = ($scope.patientList[index].patientName);
									$rootScope.unknownPatient.genderCode = ($scope.patientList[index].genderCode);
									$rootScope.unknownPatient.uhIdNumber = ($scope.patientList[index].uhIdNumber);
									$rootScope.unknownPatient.genderId = ($scope.patientList[index].genderId)
											.toString();
									$rootScope.unknownPatient.mobileNo = ($scope.patientList[index].mobileNo);
									$rootScope.unknownPatient.patientType = ($scope.patientList[index].patientType);
									$rootScope.unknownPatient.patientId = ($scope.patientList[index].patientId);
									$rootScope.unknownPatient.admissionId = ($scope.patientList[index].admissionId);
									$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
									$scope.patientId = ($scope.patientList[index].patientId);
									$scope.patientDetails.age = ($scope.patientList[index].age);
									$scope.patientDetails.patientId = ($scope.patientList[index].patientId);
									$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
									$scope.patientDetails.admissionId = ($scope.patientList[index].admissionId);
									$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
									$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
									$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
									$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
									$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
									$scope.patientDetails.admissionTime = ($scope.patientList[index].createdTime);
									$scope.patientDetails.doctorId = ($scope.patientList[index].doctorId);
									$scope.patientDetails.visitTypeId = 1;
									$scope.patientDetails.inputOutputchart = false;
									$scope.patientDetails.doctorFullName = ($scope.patientList[index].doctorName);
									$rootScope.allergydata($scope.patientList[index]);
								} else {
									$scope.abscondIndex = index;
									$scope.abscondPatientFlag = 'Y';
									angular.element('#abscondedPatientPopUp')
											.modal('show');
								}
							}

							$scope.cancelAbscondedPatient = function() {
								$rootScope.stopLoader();
								$scope.initPatientSearchObj();
								$scope.initAppointmentMasterObj();
								$scope.initKinDetails();
							}

							$scope.creatingEncounterForAbscondedPatient = function() {
								var index = $scope.abscondIndex;

								$rootScope.startLoader();
								$scope.initReferralObj();
								$scope.encounterDetailsObj = {};
								$scope.appointmentMasterObj.appointmentId = null;
								$scope.appointmentMasterObj.deptId = null;
								$scope.appointmentMasterObj.doctorId = null;
								$scope.appointmentMasterObj.patientClassId = '';
								$scope.slotId = null;
								$scope.appointmentMasterObj.encounterTime = "";
								$scope.appointmentMasterObj.systemGeneratedVisitTypeId = null;
								$scope.appointmentMasterObj.userDefinedVisitTypeId = null;
								$scope.patientDetails.age = ($scope.patientList[index].age);
								$scope.patientDetails.patientId = ($scope.patientList[index].patientId);
								$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
								$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
								$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
								$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
								$scope.patientDetails.genderId = ($scope.patientList[index].genderId);

								$scope.patientDetails.firstName = ($scope.patientList[index].firstName);
								$scope.patientDetails.mobileNumber = ($scope.patientList[index].mobileNo);
								$scope.patientDetails.registrationTypeId = ($scope.patientList[index].registrationTypeId);

								$scope.appointmentMasterObj.patientId = $scope.patientList[index].patientId;
								$scope.appointmentMasterObj.patientCategoryId = $scope.patientList[index].patientCategoryId;

								$scope.getKinDetailsByPatientId();
								$scope.kinDetailsList[0].patientId = $scope.appointmentMasterObj.patientId;

								$scope.registrationTypeId = $scope.patientList[index].registrationTypeId;
								// added for pop up to show or not
								$scope.documentUploadPopUp = true;

								var data = {
									orgId : $scope.organizationId,
									unitId : $scope.unitId,
									age : 32,
									sexId : $scope.patientDetails.genderId
								}
								if ($scope.patientList[index].appointmentId != null
										&& $scope.patientList[index].appointmentId > 0) {
									$scope.alreadyPatient = 1;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/searchInProcessAppointmentWeb/"
											+ $scope.patientList[index].appointmentId;
									BillingGenericService
											.serviceAction(METHOD_GET, URI, {},
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														$scope.alreadyAppointment = {};
														// $rootScope.startLoader();
														if (response.data.status == "success") {
															$scope.alreadyAppointment = response.data.listObject[0];
															$scope
																	.checkAppointment();
														}
													});
								}

								var URI = BASE_URL + ROOT_URL
										+ "/api/packages/ehcPackages";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.packageEHCList = [];
													if (response.data.status == "success") {
														$scope.packageEHCList = response.data.listObject;
													}

												});

								$rootScope.stopLoader();
							}

							// view template
							$scope.templateFormatId = 2;
							$scope.value = "";
							$scope.addTemplate = function(reportingTemplateId) {
								if (reportingTemplateId) {
									$scope.reportingTemplateId = reportingTemplateId;
									angular
											.forEach(
													$scope.incidentReportTemplateMapperList,
													function(value, key) {
														if (value.templateId == reportingTemplateId) {
															$scope.template = {};
															$scope.template = value;
														}
													});
									$scope.getReportedUserSavedData1[0].value = "";
									$scope.templateFormatId = $scope.template.templateFormatId;
									if ($scope.template.templateFormatId == 1) {

										$scope.obj = {};
										$scope.template.templateObject = JSON
												.parse($scope.template.templateObject);
										$scope.obj = $scope.template.templateObject.obj;
										var replacedHtml = $scope.template.templateObject.html
												.replace(/ng-model="/g,
														'ng-model="obj.');
										document.getElementById("htmlContent").innerHTML = replacedHtml;

										// document.getElementById("htmlContent").innerHTML
										// =
										// JSON.parse($scope.template.templateObject).html;
									} else if ($scope.template.templateFormatId == 2) {
										$scope.getReportedUserSavedData1[0].value = $scope.template.templateObject;
									} else if($scope.template.templateFormatId==3){
										   $rootScope.startLoaderPopup();
										    var templateTransaction = {};
											templateTransaction.templateId = $scope.template.templateId;
											templateTransaction.title = $scope.template.title;
											templateTransaction.specialityId = $scope.patientDetails.specialityId != null ? $scope.patientDetails.specialityId : $scope.specialityId;
											templateTransaction.subSpecialityId = $scope.patientDetails.subSpecialityId;
											templateTransaction.templateTypeId = $scope.template.templateTypeId;
											templateTransaction.encounterId=$scope.patientDetails.encounterId != null ?$scope.patientDetails.encounterId :0;
											templateTransaction.admissionId = $scope.patientDetails.admissionId != null ?$scope.patientDetails.admissionId  :0;
											templateTransaction.doctorId = $scope.patientDetails.doctorId;
											templateTransaction.patientId = $scope.patientDetails.patientId != null ? $scope.patientDetails.patientId :0;
											templateTransaction.diagnosisId = 0;
											templateTransaction.mIcpcSystemId = 0;
											templateTransaction.createdBy =  $scope.userId;
											templateTransaction.unitId = $scope.unitId;
											templateTransaction.updatedBy =  $scope.userId;
											templateTransaction.orgId = $scope.organizationId;
                                            templateTransaction.patientName = $scope.patientDetails.patientName !== undefined && $scope.patientDetails.patientName !== null ? $scope.patientDetails.patientName : "";
                                            templateTransaction.doctorName = $scope.patientDetails.doctorName !== undefined && $scope.patientDetails.doctorName !== null ? $scope.patientDetails.doctorName : "";
                                            templateTransaction.uhidNumber = $scope.patientDetails.uhidnumber !== undefined && $scope.patientDetails.uhidnumber !== null ? $scope.patientDetails.uhidnumber : $scope.patientDetails.uhidNumber;
                                            templateTransaction.dob = $scope.patientDetails.birthDate !== undefined && $scope.patientDetails.birthDate !== null ? $scope.patientDetails.birthDate : "";
                                            templateTransaction.ageString = $scope.patientDetails.age !== undefined && $scope.patientDetails.age !== null ? $scope.patientDetails.age : $scope.patientDetails.ageYear;
                                            templateTransaction.doa = $scope.patientDetails.createdDate !== undefined && $scope.patientDetails.createdDate !== null ? $scope.patientDetails.createdDate : "";
                                            templateTransaction.pdd = $scope.patientDetails.datepickerPdd !== undefined && $scope.patientDetails.datepickerPdd !== null ? $scope.patientDetails.datepickerPdd : "";
                                            templateTransaction.admissionNumber = $scope.patientDetails.admissionNumber !== undefined && $scope.patientDetails.admissionNumber !== null ? $scope.patientDetails.admissionNumber : "";
                                            templateTransaction.gender = $scope.patientDetails.genderCode !== undefined && $scope.patientDetails.genderCode !== null ? $scope.patientDetails.genderCode : "";
										
											
										  var URI = BASE_URL + ROOT_URL + NEW_TEMPLATE_SAVE;
											BillingGenericService.serviceAction(METHOD_POST, URI,templateTransaction,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
														if (response.data.status == "success"){
															window.open(response.data.object+"&formType=digital&patientName="+$scope.patientDetails.patientName+
		                                                          "&doctorName="+$scope.patientDetails.doctorName+
		                                                          "&uhid="+$scope.patientDetails.uhidnumber+
		                                                          "&dob="+$scope.patientDetails.birthDate+
		                                                          "&age="+$scope.patientDetails.age+
		                                                          "&gender="+$scope.patientDetails.genderCode+
		                                                          "&admissionNo="+$scope.patientDetails.admissionNumber+
		                                                          "&admissionDate="+$scope.patientDetails.createdDate+
		                                                          "&dischargeDate="+$scope.patientDetails.datepickerPdd,'_blank');
															//$scope.getTemplateListForList();
															 $rootScope.stopLoaderPopup();
															 
															$scope.conversationId = response.data.resObject.conversationId;
															console.log("conversationId",$scope.conversationId);
														}
														else if(response.data.status == "error")
															{
															  $('#addtemplateModal').modal('hide');
															  $rootScope.stopLoaderPopup();
															}
														
													});	
											 }
								}
							}
							
							
							$scope.getTemplateListForList = function(info) 
							{
								//$rootScope.startLoader();
						    	var data={
						    		  orgId : $scope.organizationId,
						    		  unitId : $scope.unitId,
						    		  doctorId : $scope.patientDetails.doctorId,
						    		  specialityId : $scope.patientDetails.specialityId,
						    		  templateTypeId : info.templateTypeId,
						    		  patientId : $scope.patientDetails.patientId,
						    		  visitTypeId: $scope.patientDetails.visitTypeId,
						    		  appointmentId:$scope.patientDetails.appointmentId,
						    		  templateId : patientDetails.templateId,
						    		  encounterId:$scope.patientDetails.encounterId != null ? $scope.patientDetails.encounterId:null,
						              admissionId:$scope.patientDetails.admissionId != null ? $scope.patientDetails.admissionId:null,
						    		}
						    	
						    	console.log('data',JSON.stringify(data));
								var URI = BASE_URL + ROOT_URL + "/global/petient/template/otims";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
										{
							
									   if(response.data.status=='success')
									      {
										   $scope.templateTransactionList =[];
										   	$scope.templateTransactionList = response.data.listObject;
										   	$rootScope.stopLoader();
									      }
									   else if(response.data.status=='error'){
										   $rootScope.stopLoader();
									   }
								});
							}
							
							$scope.filepreview = "";
							$scope.fileObj = {
								csvfile : '',             
							}
							// docuument Upload code
							$scope.files = [];
							$scope.getFileDetails = function(e) {
								$scope.$apply(function() {
									for (var i = 0; i < e.files.length; i++) {
										var isFileAvailable = false;
										console.log("File Name  " + e.files[i].name);
										for (var j = 0; j < $scope.files.length; j++) {
											if ($scope.files[j].name === e.files[i].name) {
												isFileAvailable = true;
												break;
											}
										}
										if (!isFileAvailable) {
											$scope.files.push(e.files[i]);
										} else {
													growl.warning("file is already available ::"
															+ e.files[i].name,{    
									                    title : "WARNING"
									                });
										}
									}
								});
							}

							
							
							
							$scope.uploadEncounterDoc = function(Obj) {
								var fd = new FormData();
								for (var i = 0; i < $scope.files.length; i++) {
									fd.append('files', $scope.files[i]);
								}	
								fd.append('encounterId', 0);
								fd.append('visitTypeId', 1);
								fd.append('admissionId', 0);
								fd.append('patientId', $scope.patientId);
								fd.append('documentTypeId', 103);
								fd.append('createdBy', $scope.userId);
								fd.append('updatedBy', $scope.userId);
								fd.append('unitId', $scope.unitId);
								fd.append('organisationId',
										$scope.organisationId);
								//fd.append('file', $scope.csvfile);
								fd.append('expenseId', 0);
								fd.append('grnId', 0);
								fd.append('poId', 0);
								fd.append('nabhReportingMapperId',
										Obj.nabhReportingMapperId);
								fd.append('companyExpenseId', 0);
								fd.append('createdDate', null);
								fd.append('opbId', 0);
								fd.append('vendorId', 0);
								var URI1 = BASE_URL + ROOT_URL
										+ "/api/opd/nabhPatientDoc";
								return GenericServiceFileUpload.serviceAction(
										"POST", URI1, fd, {
											'Content-Type' : undefined
										}).then(function(response) {
									return response;
								});
							}

							$scope.loadMoreData = function() {
								$scope.offset += 200;
								$scope.loadMoreFlag = true;
								$rootScope
										.searchPatient($scope.patientSearchObj);
							}
							$scope.offset = 0;
							$scope.loadMoreFlag = false;
							$scope.noOfRecordsPerPage = 200;
							$scope.patientList = [];
	                            $scope.searchPatient = function()
                                                            {
                                                                $rootScope.startLoader();
                                                                $scope.DischargeSummaryFinalList=[];
                                                                $scope.patientTemplateList=[];
                                                                if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
                                                                {
                                                                    growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
                                                                    $scope.patientList = [];
                                                                    $rootScope.stopLoader();
                                                                    return false;
                                                                }
                                                                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
                                                                var patSearchObj = angular.copy($scope.patientSearchObj);
                                                                if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
                                                                    var dt = new Date($scope.patientSearchObj.birthDate);
                                                                    patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
                                                                }
                                                                BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                                                    $scope.searchPatientList = [];
                                                                    if (response.data.status == "success")
                                                                        {
                                                                        $scope.patientList = response.data.listObject;
                                                                        patSearchObj = {};

                                                                        $scope.initPaymentDetailsObj();
                                                                        $scope.listPaymentDetails = [];
                                                                        $scope.patientDetailsList = [];
                                                                        $scope.patientDetails.visitTypeId=1;
                                                                        $scope.patientDetails = {};
                                                                        $rootScope.stopLoader();
                                                                        }
                                                                    else if(response.data.listObject==null){
                                                                        growl.warning(response.data.message,{
                                                                            title : ERROR_MSG
                                                                        });
                                                                        $scope.patientSearchObj.birthDate="";
                                                                        $scope.patientList=[];
                                                                        return false;
                                                                    }
                                                                    else{
                                                                        $rootScope.stopLoader();
                                                                    }
                                                                    $scope.patientSearchObj.birthDate="";
                                                                });
                                                            }
							
							
						 	$scope.getTemplateList = function(templateTypeId) 
					    	{

					       		$scope.templateTypeId = 27;
					    		$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : $scope.patientDetails.doctorId,
					        		  specialityId : $scope.patientDetails.specialityId,

					        		  templateTypeId : 27,
					        		  patientId : $scope.patientId,
					        		  visitTypeId: 2,
					        		  appointmentId:$scope.patientDetails.appointmentId,
					        		 encounterId:$scope.patientDetails.encounterId,
								     admissionId : $scope.patientDetails.admissionId,
								     conversationId : $scope.conversationId
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					    		var URI = BASE_URL + ROOT_URL + "/global/petient/template";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   	$scope.templateTransactionList = response.data.listObject;
					    				   	
					    				   	angular.forEach($scope.templateTransactionList ,function (value,key){
					    				   		value.printReport= false;	
					    				   	});
					    				   	$('#viewtemplateModal').modal('show');
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
					    	}
					       	
						 	
                           $scope.viewTemplate = function(template) {
                        	   $scope.template={};
								if(template.templateFormatId != 4 ){
								$scope.editorData=template.templateObject;
								$scope.template.templateFormatId=template.templateFormatId;
								$scope.saveTrasactionFlag = false;
								$scope.updateTrasactionFlag = false;
								$scope.obj = {};
								$('#viewtemplateModal').modal('hide');
								
								if(template.templateFormatId != null && (template.templateFormatId == 1 || template.templateFormatId == 2)){
									$('#addviewTemplateModal11').modal('show');
									document.getElementById("htmlContent1").innerHTML = JSON.parse(template.templateObject).html;
									$scope.obj = JSON.parse(template.templateObject).obj;
									var element = angular.element(document.getElementById("htmlContent1"));
									$compile(element)($scope);
								}else{
									$('#addviewTemplateModal').modal('show');
									var lines = template.templateObject.split('\n');
									   var formattedContent = lines.map(line => `<div class="styled-html">${line}</div>`).join('');
				                       document.getElementById("htmlContent3").innerHTML =formattedContent;
								}
								}else{
									$scope.template = {};
									$scope.template = template;
									$scope.templateTypeId = template.templateTypeId;
									$rootScope.startLoader();
									$scope.savedList=[];
									$scope.savedEditedList = [];
									var URI = BASE_URL + ROOT_URL+ "/api/global/unit/template/tabular/mapper/getPatientTabularTemplateDataMapperById/"
									+parseInt(template.tabularTemplateId) +"/"+$scope.organizationId+"/"+$scope.unitId +"/"+$stateParams.patientDetailsObj.patientId+"/"+template.templateTypeId;
									GenericService
									.serviceAction(METHOD_GET, URI)
									.then(
											function(response) {

												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													$scope.rowList = [];
													$scope.columnList = [];
													$scope.columnHeaderList = [];
													$scope.savedEditedList = [];
													$scope.savedEditedList = response.data.listObject;
													$scope.comObj = {};
													$scope.savedList = response.data.listObject;
													angular.forEach($scope.savedEditedList,function(value1,key1){
														value1.organizationId=$scope.organizationId;
														value1.unitId=$scope.unitId;
														value1.createdBy=$scope.userId;
														value1.updatedBy=$scope.userId;
														value1.status='A';
														value1.createdDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
														value1.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													});
													console.log("$scope.columnList",$scope.columnList);
													$scope.comObj.numberOfRow = response.data.listObject[0].numberOfRow;
													$scope.comObj.numberOfCoulmn = response.data.listObject[0].numberOfCoulmn;
													$scope.tableName = response.data.listObject[0].tableName;
													
														for(var i = 0; i < $scope.comObj.numberOfRow ;i++){
															var newArr = {};
															newArr.rowId = i;
															newArr.coulmnId = i;
															newArr.rowName = '';
															newArr.inputbox = false;
															newArr.tabularTemplateId = $scope.tabularTemplateId;
															$scope.rowList.push(newArr);
													     }
														for(var i = 0; i < $scope.comObj.numberOfCoulmn ;i++){
															var newArr = {};
															newArr.coulmnId = i;
															newArr.rowId = i;
															newArr.columnName = '';
															newArr.inputValue = '';
															newArr.inputbox = false;
															newArr.tabularTemplateId = $scope.tabularTemplateId;
															$scope.columnList.push(newArr);
															$scope.columnHeaderList.push(newArr);
													     }
														$scope.tableName = response.data.listObject[0].tableName;
														angular.forEach($scope.rowList,function(value,key){
															angular.forEach($scope.savedEditedList,function(value1,key1){
																if(value.rowId == value1.rowId){
																	value.rowName = value1.rowName;
																}
															});
														});
														angular.forEach($scope.columnList,function(value,key){
															angular.forEach($scope.savedEditedList,function(value1,key1){
																if(value.coulmnId == value1.coulmnId){
																	value.columnName = value1.columnName;
																}
															});
														});
														angular.forEach($scope.columnHeaderList,function(value,key){
															angular.forEach($scope.savedEditedList,function(value1,key1){
																if(value.coulmnId == value1.coulmnId){
																	value.columnName = value1.columnName;
																}
															});
														});
														
														console.log("$scope.rowList",$scope.rowList);
														console.log("$scope.columnList",$scope.columnList);
														
														$('#viewtemplateModal').modal('hide');
														$('#addviewTemplateModal11').modal('show');
														$scope.saveTrasactionFlag = true;
														$scope.updateTrasactionFlag = false;
												
														
							
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
												}
											});
								
							 }
							}
						 	
						 	 
						 	 
						 	$scope.printTemp=function(info){
                        		if(info.templateFormatId == 3){
                				if(info.isHtmlPrint == true){
                					$scope.printDataObj={
                                    		orgId : $scope.organizationId,
                					        	unitId :  $scope.unitId,
                					        	templateTransactionId:info.templateTransactionId,
                                    	};
                			        	
                			        	var URI = BASE_URL + ROOT_URL + "/global/printHTMLTemplate";
                			    		BillingGenericService.serviceAction(METHOD_POST, URI, $scope.printDataObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
                			    				{
                			    	
                			    			   if(response.data.status=='success')
                			    			      {
                			    				   console.log("gg",response.data.object);
                			    					 var headerSize = "";
                			    					 var letterImgAndHeader="";
                			    					 var patientDetails = "";
                			    					 var completeTemplateContent = "";
                			    						 headerSize = "105px;";
                			    						 patientDetails = response.data.object.headerPart;
                			    						 completeTemplateContent = patientDetails + response.data.object.templateHtml;
                			    						 $scope.el= angular.element(completeTemplateContent).find('input');
                			    						 console.log("inputttt", $scope.el);
                			    						 angular.forEach($scope.el ,function (t,key){
                			    							 var ind=completeTemplateContent.indexOf(t.outerHTML);
                			    							 if(t.type == "text"){
                				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.value+"</label>");
                			    							 }			    							 
                			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
                			    						 });
                			    						 
                			    						 $scope.e2= angular.element(completeTemplateContent).find('textarea');
                			    						 console.log("textareaaa", $scope.e2);
                			    						 angular.forEach($scope.e2 ,function (t,key){
                			    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
                			    							 if(t.type == "textarea"){
                				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.defaultValue+"</label>");
                			    							 }			    							 
                			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
                			    						 });
                			    						 
                			    						 $scope.e3= angular.element(completeTemplateContent).find('span');
                			    						 console.log("spannn", $scope.e3);
                			    						 angular.forEach($scope.e3 ,function (t,key){
                			    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
                			    							
                			    							 if(t.className == "ant-select-arrow"){
                				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");
                			    							 }
                			    							 if(t.className == "anticon anticon-close-circle"){
                				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");

                			    							 }
                			    							 if(t.className == "ant-select-clear"){
                				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");

                			    							 }
                			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
                			    						 });
                			    						 
                			    						 $scope.e4= angular.element(completeTemplateContent).find('div');
                			    						 console.log("divvvvv", $scope.e4);
                			    						 angular.forEach($scope.e4 ,function (t,key){
                			    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
                			    							 if(t.innerText == "Select Answer"){
                			    								 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, ""); 
                			    							 }
                			    							 if(t.className == "ant-select-selector"){
                				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.innerText+"</label>");
                			    							 }	
                			    							
                			    							 
                			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
                			    						 });
                			    						 
                			    						 
                			    					 var printWindow = window.open('', '', 'height=500,width=700');  
                                                        printWindow.document.write(completeTemplateContent);  
                                                        printWindow.document.close();  
                                                        $timeout( function(){
                                                             printWindow.print();  
                                                        },1000);			
                			    			      }
                			    			   else if(response.data.status=='error'){
                			    				   $rootScope.stopLoader();
                			    			   }
                			    		});
                				}
                        		}else if(info.templateFormatId == 4){
                					var data = {     
                							"appName": "HIS",     
                							"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",  
                							  "reportDesignFilePath": "CoversheetTemplate_Tabular.rpt",   
                							  "reportFormat": "PDF",   
                							  "exportFileName": "CoversheetTemplate_Tabular",   
                							  "reportParams": [     {    
                								"paramName": "patient_id",    
                								"paramType": "Integer",    
                								"paramValue": info.patientId    
                								 },    
                								 {  
                									  "paramName": "tabular_template_id", 
                									    "paramType": "Integer",    
                									"paramValue": info.tabularTemplateId    
                									 },   
                									  {    "paramName": "template_id",
                										     "paramType": "Integer",
                										     "paramValue": info.templateId  
                										   },  
                										   {   
                											"paramName": "op_ip_id",  
                											   "paramType": "Integer", 
                											    "paramValue": info.admissionId  
                											  } ]
									};
									reportService.generate(REPORT_API_URI1,data);
                				}else{
                					var data = {
    										"appName" : "HIS",
    										"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
    										"reportDesignFilePath" : "CoversheetTemplate.rpt",
    										"reportFormat" : "PDF",
    										"exportFileName" : "Template",
    										"reportParams" : [ {
    											"paramName" : "template_transaction_id",
    											"paramType" : "Integer",
    											"paramValue" : info.templateTransactionId
    										} ]
    									};
    									reportService.generate(REPORT_API_URI1,
    											data);
                				}
                	}
						 	 
							$scope.saveIncidentReporting = function() {
								try {
									$rootScope.startLoader();
									var date = new Date();
									$scope.dt = moment(date).format(
											'DD-MM-YYYY HH:mm:ss');
									if ($scope.nabhTypeId == 1) {
										if ($scope.patientId == ""
												|| $scope.patientId == undefined) {
											growl.error(
													"Please Select Patient ", {
														title : 'Warning!'
													});
											$rootScope.stopLoader();
											return false;
										}
									}

									if ($scope.nabhReportingDate == undefined) {
										growl.error("Please Select Date ", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}
									if ($scope.nabhReportingDateTime == undefined  
											|| $scope.nabhReportingDateTime == "") {
										growl.error("Please Enter Time", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}
									if ($scope.specialityId == undefined
                                    	|| $scope.specialityId == "") {
                                    	growl.error("Please Select Department", {
                                    	title : 'Warning!'
                                    	});
                                    	$rootScope.stopLoader();
                                    	return false;
                                    }
                                    if ($scope.incidentCategoryId == undefined
                                         || $scope.incidentCategoryId == "") {
                                           growl.error("Please Select Category", {
                                           title : 'Warning!'
                                           });
                                           $rootScope.stopLoader();
                                            return false;
                                    }
									if ($scope.roleId == undefined
											|| $scope.roleId == "") {
										growl.error("Please Select Role", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}

									if ($scope.incidentUserId == undefined
											|| $scope.incidentUserId == "") {
										growl.error("Please Select User", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}
									if ($scope.reportingTemplateId == undefined
											|| $scope.reportingTemplateId == "") {
										growl.error("Please Select Template", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}


									if($scope.templateFormatId == 1){
									  $scope.dataObj=document.getElementById("htmlContent").innerHTML;
										console.log("sdfsdfdsd",$scope.dataObj);
									}else{
										  $scope.dataObj=$scope.getReportedUserSavedData1[0].value;
											console.log("sdfsdfdsd",$scope.dataObj);
									}

									var data = {
										"nabhReportingMapperId" : $scope.nabhReportingMapperId,
										"nabhReportingDate" : moment(
												$scope.nabhReportingDate)
												.format('DD-MM-YYYY HH:mm:ss'),
										"nabhReportingTime" : $scope.nabhReportingDateTime,
										"specialityId" : $scope.specialityId,
										"nabhTypeId" : $scope.nabhTypeId,
										"incidentCategoryId" : $scope.incidentCategoryId,
										"nabhProcessId" : $scope.nabhProcessId,
										"reportingTemplateId" : $scope.reportingTemplateId,
										"reportingTemplateObj" : $scope.dataObj,
										"nabhRemark" : $scope.nabhRemark,
										"roleId" : $scope.roleId,
										"userId" : $scope.incidentUserId,
										"patientId" : $scope.patientId,
										"organizationId" : $scope.organisationId,
										"unitId" : $scope.unitId,
										"status" : 'A',
										"createdBy" : $scope.createdBy,
										"createdDate" : $scope.dt,
										"updatedBy" : $scope.updatedBy,
										"updatedDate" : $scope.dt,
										"conversationId" :$scope.conversationId,
										 "encounterId":$scope.patientDetails.encounterId,
									     "admissionId" : $scope.patientDetails.admissionId,
									}
									console.log("save data", JSON
											.stringify(data));
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/unit/saveNabhReportingMapper";
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == 'success') {

															growl
																	.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');

															var uploadData = {};
															uploadData.nabhReportingMapperId = response.data.object;
															if($scope.files.length!=0){
															   var promise = $scope.uploadEncounterDoc(uploadData);
																promise
																.then(function(
																		response) {
																	if (response.data.status == "success") {
																		$scope.count++;
																		if ($scope.finalDocUploadList.length == $scope.count) {

																			$rootScope
																					.stopLoader();
																			$scope.encounterDetailsContractList = [];
																			$scope.finalDocUploadList = [];
																			$scope.allDocumentFinalList = [];
																		}

																	} else {
																		$rootScope
																				.stopLoader();
																	}
																});
															}
															else{
																$rootScope.stopLoader();
															}
															$scope.saveBtnFlag = true;
															$scope.updateBtnFlag = false;
															$scope.nabhTypeId = 2;
															$scope.nabhReportingDate = new Date();
																	$scope.nabhReportingDateTime = '',
																	$scope.specialityId = '';
															$scope.incidentCategoryId = '';
															$scope.nabhReportId = '';
															$scope.reportingTemplateId = '';
															$scope.nabhRemark = '';
															$scope.roleId = '';
															$scope.incidentUserId = '';
																	$scope.patientId = null,
																	$scope.nabhReportingMapperId = null;
															$scope.obj2 = '';
															$scope.getReportedUserSavedData1[0].value = '';

															$state
																	.go('nabhReporting');
															$rootScope
																	.stopLoader();

														} else if (response.data.status == 'error') {
															$rootScope
																	.stopLoader();
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}

													});
								} catch (e) {
									console.log("Exception", e.message);
								}
							}
							$scope.getReportedUserSavedData1=[];
                            $scope.info={};
                            $scope.info.editFlag=true;
                            $scope.getReportedUserSavedData1.push($scope.info);
                            $scope.checkStatus=function(userId)
                            {
                                $scope.info={};
                                $scope.getReportedUserSavedData1=[];
                                if(userId == $scope.userId){
                                    $scope.info.editFlag=false;
                                }else{
                                    $scope.info.editFlag=true;
                                }
                                $scope.getReportedUserSavedData1.push($scope.info);
                            }
						 	$scope.printTemp=function(info){
				        		if(info.templateFormatId == 3){
								if(info.isHtmlPrint == true){
									$scope.printDataObj={
				                    		orgId : $scope.organizationId,
									        	unitId :  $scope.unitId,
									        	templateTransactionId:info.templateTransactionId,
				                    	};
							        	
							        	var URI = BASE_URL + ROOT_URL + "/global/printHTMLTemplate";
							    		BillingGenericService.serviceAction(METHOD_POST, URI, $scope.printDataObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				   console.log("gg",response.data.object);
							    					 var headerSize = "";
							    					 var letterImgAndHeader="";
							    					 var patientDetails = "";
							    					 var completeTemplateContent = "";
							    						 headerSize = "105px;";
							    						 patientDetails = response.data.object.headerPart;
							    						 completeTemplateContent = patientDetails + response.data.object.templateHtml;
							    						 $scope.el= angular.element(completeTemplateContent).find('input');
							    						 console.log("inputttt", $scope.el);
							    						 angular.forEach($scope.el ,function (t,key){
							    							 var ind=completeTemplateContent.indexOf(t.outerHTML);
							    							 if(t.type == "text"){
								    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.value+"</label>");
							    							 }			    							 
							    							 console.log("completeTemplateContenttttt", completeTemplateContent);
							    						 });
							    						 
							    						 $scope.e2= angular.element(completeTemplateContent).find('textarea');
							    						 console.log("textareaaa", $scope.e2);
							    						 angular.forEach($scope.e2 ,function (t,key){
							    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
							    							 if(t.type == "textarea"){
								    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.defaultValue+"</label>");
							    							 }			    							 
							    							 console.log("completeTemplateContenttttt", completeTemplateContent);
							    						 });
							    						 
							    						 $scope.e3= angular.element(completeTemplateContent).find('span');
							    						 console.log("spannn", $scope.e3);
							    						 angular.forEach($scope.e3 ,function (t,key){
							    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
							    							
							    							 if(t.className == "ant-select-arrow"){
								    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");
							    							 }
							    							 if(t.className == "anticon anticon-close-circle"){
								    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");

							    							 }
							    							 if(t.className == "ant-select-clear"){
								    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");

							    							 }
							    							 console.log("completeTemplateContenttttt", completeTemplateContent);
							    						 });
							    						 
							    						 $scope.e4= angular.element(completeTemplateContent).find('div');
							    						 console.log("divvvvv", $scope.e4);
							    						 angular.forEach($scope.e4 ,function (t,key){
							    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
							    							 if(t.innerText == "Select Answer"){
							    								 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, ""); 
							    							 }
							    							 if(t.className == "ant-select-selector"){
								    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.innerText+"</label>");
							    							 }	
							    							
							    							 
							    							 console.log("completeTemplateContenttttt", completeTemplateContent);
							    						 });
							    						 
							    						 
							    					 var printWindow = window.open('', '', 'height=500,width=700');  
				                                        printWindow.document.write(completeTemplateContent);  
				                                        printWindow.document.close();  
				                                        $timeout( function(){
				                                             printWindow.print();  
				                                        },1000);			
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
								}
				        		}else if(info.templateFormatId == 4){
									var data = {     
											"appName": "HIS",     
											"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",  
											  "reportDesignFilePath": "CoversheetTemplate_Tabular.rpt",   
											  "reportFormat": "PDF",   
											  "exportFileName": "CoversheetTemplate_Tabular",   
											  "reportParams": [     {    
												"paramName": "patient_id",    
												"paramType": "Integer",    
												"paramValue": info.patientId    
												 },    
												 {  
													  "paramName": "tabular_template_id", 
													    "paramType": "Integer",    
													"paramValue": info.tabularTemplateId    
													 },   
													  {    "paramName": "template_id",
														     "paramType": "Integer",
														     "paramValue": info.templateId  
														   },  
														   {   
															"paramName": "op_ip_id",  
															   "paramType": "Integer", 
															    "paramValue": info.admissionId  
															  } ]
									};
									reportService.generate(REPORT_API_URI1,data);
								}else{
									var data = {
											"appName" : "HIS",
											"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
											"reportDesignFilePath" : "CoversheetTemplate.rpt",
											"reportFormat" : "PDF",
											"exportFileName" : "Template",
											"reportParams" : [ {
												"paramName" : "template_transaction_id",
												"paramType" : "Integer",
												"paramValue" : info.templateTransactionId
											} ]
										};
										reportService.generate(REPORT_API_URI1,
												data);
								}
					}
						} ]);

