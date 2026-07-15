'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:ERDueForDischargeController
 * @description #ERDueForDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'ERDueForDischargeController',
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
						'PagerService',
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,PagerService,BillingGenericService,$window) {
							
							 $scope.dateOptions = {							 
								      formatYear: 'yyyy',
								      showWeeks: false
								      
								  };
								 $scope.openDatePicker = function() {
									// alert("in");
								      $scope.datepicker.opened = true;
								    };
								    $scope.datepicker = {
										      opened: false
										    };

							/* init() function for form object create. */
							$scope.init = function() {

								$scope.dueForDischargeFlage = false;
								$(".selectedPageName").text("Due For Discharge");
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
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
								
								$scope.visitTypeId = 4;

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

								
/*
								var URI = BASE_URL + ROOT_URL
										+ GETDISCHARGEADMITTEDPATIENTLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
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
																			newArr.tPatientId = value.tPatientId;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.doctorId = value.doctorId;
																			newArr.bedId = value.bedId;
																			$scope.listOfAdmittedPatientList
																					.push(newArr);

																		});

													}
												});*/


							}
							

							$scope.init();
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initERAdmittedPatientList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initERAdmittedPatientList = function(orgId,
									offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETERADMITTEDPATIENTLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTERADMITTEDPATIENT;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
								}

								// For Get ErPatient List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													//$scope.listOfERAdmittedPatientList = [];
													//$scope.ERadmittedPatientList = [];
													
													$scope.listOfAdmittedPatientList = [];
													$scope.ERadmittedPatientList = [];
													

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.ERadmittedPatientList = response.data.listObject;
														angular
																.forEach(
																		$scope.ERadmittedPatientList,
																		function(
																				value,
																				index) {

																			var newArr = {};
																			newArr.doctorFullName = value.dFirstName;
																			newArr.patientFullName = value.patientName;
																			newArr.dob = value.dob;
																			newArr.datepicker = value.doa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.genderCode = value.genderName;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tpatientId = value.tPatientId;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.bedId  = value.bedId;
																			newArr.doctorId = value.doctorId;
																			newArr.noOfHours = value.noOfHours;
																			newArr.visitTypeId=4;
																			newArr.imagePath=value.imagePath;
																			newArr.ageStr=value.ageStr;
																			newArr.prefixDesc=value.prefixDesc;
																			newArr.userName = value.userName;
																			newArr.pddTime = value.pddTime;

																			var pddtimeData = value.pddTime;

																			if(pddtimeData != null && pddtimeData.split(' ').length > 1){
																				newArr.pddTime = pddtimeData.split(' ')[1];
																				newArr.datepickerPdd = pddtimeData.split(' ')[0];
																			}


																			$scope.listOfAdmittedPatientList
																					.push(newArr);
																			console.log("$scope.listOfAdmittedPatientList",$scope.listOfAdmittedPatientList);

																		});

													}
												});

								// For Count ErPatient List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);

													}
												});
							}

							$scope.getERAdmittedPatientList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETERADMITTEDPATIENTLIST;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													/*$scope.listOfERAdmittedPatientList = [];
													$scope.ERadmittedPatientList = [];*/
													$scope.listOfAdmittedPatientList = [];
													$scope.ERadmittedPatientList = [];
													
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.ERadmittedPatientList = response.data.listObject;
														angular
																.forEach(
																		$scope.ERadmittedPatientList,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.doctorFullName = value.dFirstName;
																			newArr.patientFullName = value.patientName;
																			newArr.dob = value.dob;
																			newArr.datepicker = value.doa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.genderCode = value.genderName;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tpatientId = value.tPatientId;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.bedId  = value.bedId;
																			newArr.doctorId = value.doctorId;
																			newArr.noOfHours = value.noOfHours;
																			newArr.visitTypeId=4;
																			newArr.imagePath=value.imagePath;
																			newArr.ageStr=value.ageStr;
																			newArr.prefixDesc=value.prefixDesc;
																			newArr.userName = value.userName;
																			newArr.pddTime = value.pddTime;
																			$scope.listOfAdmittedPatientList
																					.push(newArr);
																		});
													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getERAdmittedPatientList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initERAdmittedPatientList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End
							
						

							$scope.popupDischarge = function(item)
							{
								$scope.dischargeTypeId = 0;
								$scope.itemForDischargePatient = item;
								$scope.datepicker= new Date();
							}
							
							$scope.dueForDischarge = function(item)
							{
								$scope.note="";
								$scope.reasonId = 0;
								$scope.itemForDischargePatient = item;
								$scope.datepicker = new Date();
								
								 var arrayN = item.datepickerPdd.split(' ');
								 var array = arrayN[0].split('/');
								var newDateDoa =array[1]+'/'+array[0]+'/'+array[2];
								newDateDoa = newDateDoa+' '+arrayN[1];
								//alert(newDateDoa);
								var now = new Date(newDateDoa);
								//alert(now);
								//alert(item.noOfHours);
								now.setHours(now.getHours()+4);
								now.setMinutes(now.getMinutes()+0);
								//alert(now);
								$scope.noOfHours = moment(now).format('DD/MM/YYYY HH:mm:ss');
								$scope.noOfHoursFormat = moment(now).format('DD-MM-YYYY HH:mm:ss');
								
							}

							$scope.saveDischargeRequest = function() {
								
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									dischargeTypeId : $scope.dischargeTypeId,
									admissionId : $scope.itemForDischargePatient.admissionId,
									patientId : $scope.itemForDischargePatient.patientId,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									destinationId :$scope.itemForDischargePatient.bedId,
									dischargeDate : moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
									dischargeStatusId: 1,
									visitTypeId : 4
								};
								console.log(data);
								if($scope.dischargeTypeId == 0 ){
									growl.warning(" Please select discharge type. ",
											{
												warning : 'warning!'
											});
									$rootScope.stopLoaderPopup();
									return false;
								}
								$rootScope.startLoaderPopup();
								var URI = BASE_URL + ROOT_URL
										+ ADDDISCHARGEREQUEST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl.success('Discharge Request Raised Successfully...!!',
																		{
																			title : 'Success!'
																		});
														angular.element('#popupDischarge').modal('hide');
														$scope.initERAdmittedPatientList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);
														$rootScope.stopLoaderPopup();
														$scope.init();
													} else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$rootScope.stopLoaderPopup();
													}
												});
							}
							
							$scope.updateExtendPdd = function()
							{
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
									pdd : $scope.noOfHoursFormat,
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
														growl
																.success(
																		'Discharge Request Raised Successfully...!!',
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
												});
							}

							$scope.patientDocumnetInformation = function(info) {
								$scope.patientDocumentFlipBook ={};
								$scope.patientDocumentFlipBook =info;
								console.log($scope.patientDocumentFlipBook);
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
