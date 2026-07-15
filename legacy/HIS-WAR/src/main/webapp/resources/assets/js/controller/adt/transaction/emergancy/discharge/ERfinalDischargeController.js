'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:finalDischargeController
 * @description #finalDischargeController Controller of the myApp
 */

angular
		.module('myApp')
		.controller(
				'ERfinalDischargeController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state'
						,'PagerService',
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,PagerService,BillingGenericService,$window) {

							/**Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.admittedPatientCount;
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
								$scope.isEr=true;
								$scope.dropdown = 'Y';
								$rootScope.loginpage = true;
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
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								
								
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
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								
								
								
								if($scope.nurseRole == 1)
								{
									$scope.wardList = [];
									var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
									 if(nursingStationsWardsList == undefined){
										 growl.warning(
													'No ward mapped with selected nursing station !!',
													{
														title : 'warning!'
													});
									 }
									 else
									  {
									   console.log('nursingStationsWardsList',nursingStationsWardsList);
									   $scope.wardList = nursingStationsWardsList; 
									  }
									
									 $scope.nurseBedList = [];
										var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
										 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y' ){
											 growl.warning(
														'No bed mapped with nurse !!',
														{
															title : 'warning!'
														});
										 }
										 else
										  {
										   $scope.nurseBedList =mappedNurseBedList; 
										  console.log('Nurse Bed List',$scope.nurseBedList);
										  }
								}else{
									growl.warning(
											'Logged in user is not a nurse...!!',
											{
												title : 'warning!'
											});
									return false;
								}
								
								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
//									visitTypeId: 4,
									wardList : $scope.wardList
								};
			                  
								$scope.visitTypeId = 4;

				

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
																			newArr.tPatientId = value.tPatientId;
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
																			newArr.nursingStationId = value.nursingStationId;
																			newArr.floorId = value.floorId;
																			newArr.roomId = value.roomId;
																			newArr.bedCategoryId = value.bedCategoryId;
																			newArr.isIcu = value.isIcu;
																			newArr.visitTypeId=value.visitTypeId;
																			newArr.isFinancialClearance = value.isFinancialClearance;
																			newArr.billStatusCount = value.billStatusCount;
																			newArr.orderDetailsCount = value.orderDetailsCount;
																			newArr.prefixDesc = value.prefixDesc;
																			newArr.age = value.ageStr;
																			newArr.imagePath = value.imagePath;
																			if(value.erBedTypeAllocation == '1')
																			{
																			newArr.erBedTypeAllocationDesc = "ER";
																			}
																		if(value.erBedTypeAllocation == '2')
																		{
																		newArr.erBedTypeAllocationDesc = "Observation";
																		}
																		if(value.erBedTypeAllocation == '3')
																			{
																			newArr.erBedTypeAllocationDesc = "Resuctination";
																			}
																		  if(value.visitTypeId == 4){
																			   $scope.listOfAdmittedPatientList.push(newArr);
																			}


																		});

													}
												});

						

							}

							$scope.init();

							/*chetana*/
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										visitTypeId: 4,
										wardList : $scope.wardList,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										unitId:unitId
									};
				                  
//									$scope.visitTypeId = 4;

					

									var URI = BASE_URL + ROOT_URL
											+ "/adt/getPatientFinalDischargeListNewForListAndCount";
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.listOfAdmittedPatientList = [];
														$scope.admittedPatientList = [];

														if (response.data.status == "success") {
															$scope.admittedPatientList = response.data.listObject;
															$scope.admittedPatientCount = response.data.object;
															
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
																				newArr.dischargeStatusId = value.dischargeStatusId;
																				newArr.dischargeTypeId = value.dischargeTypeId;
																				newArr.floorName = value.floorName;
																				newArr.dischargeTypeName = value.dischargeTypeName;
																				newArr.dischargeDesc= value.dischargeDesc;
																				newArr.bedId = value.bedId;
																				newArr.wardId  = value.wardId;
																				newArr.nursingStationId = value.nursingStationId;
																				newArr.floorId = value.floorId;
																				newArr.roomId = value.roomId;
																				newArr.bedCategoryId = value.bedCategoryId;
																				newArr.isIcu = value.isIcu;
																				newArr.visitTypeId=value.visitTypeId;
																				newArr.isFinancialClearance = value.isFinancialClearance;
																				newArr.billStatusCount = value.billStatusCount;
																				newArr.orderDetailsCount = value.orderDetailsCount;
																				newArr.prefixDesc = value.prefixDesc;
																				newArr.age = value.ageStr;
																				newArr.imagePath = value.imagePath;
																				if(value.erBedTypeAllocation == '1')
																				{
																				newArr.erBedTypeAllocationDesc = "ER";
																				}
																			if(value.erBedTypeAllocation == '2')
																			{
																			newArr.erBedTypeAllocationDesc = "Observation";
																			}
																			if(value.erBedTypeAllocation == '3')
																				{
																				newArr.erBedTypeAllocationDesc = "Resuctination";
																				}
																			  if(value.visitTypeId == 4){
																				   $scope.listOfAdmittedPatientList.push(newArr);
//																				   $scope.admittedPatientCount=$scope.listOfAdmittedPatientList.length;
																				}


																			});

														}
														$rootScope.stopLoader();
														$scope.setPage(1,false);
													});
								
								
								
							}		
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										visitTypeId: 4,
										wardList : $scope.wardList,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
									};
				                  
//									$scope.visitTypeId = 4;

					

									var URI = BASE_URL + ROOT_URL
											+ "/adt/getPatientFinalDischargeListNewForListAndCount";
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.listOfAdmittedPatientList = [];
														$scope.admittedPatientList = [];

														if (response.data.status == "success") {
															$scope.admittedPatientList = response.data.listObject;
															$scope.admittedPatientCount=response.data.object;
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
																				newArr.dischargeStatusId = value.dischargeStatusId;
																				newArr.dischargeTypeId = value.dischargeTypeId;
																				newArr.floorName = value.floorName;
																				newArr.dischargeTypeName = value.dischargeTypeName;
																				newArr.dischargeDesc= value.dischargeDesc;
																				newArr.bedId = value.bedId;
																				newArr.wardId  = value.wardId;
																				newArr.nursingStationId = value.nursingStationId;
																				newArr.floorId = value.floorId;
																				newArr.roomId = value.roomId;
																				newArr.bedCategoryId = value.bedCategoryId;
																				newArr.isIcu = value.isIcu;
																				newArr.visitTypeId=value.visitTypeId;
																				newArr.isFinancialClearance = value.isFinancialClearance;
																				newArr.billStatusCount = value.billStatusCount;
																				newArr.orderDetailsCount = value.orderDetailsCount;
																				newArr.prefixDesc = value.prefixDesc;
																				newArr.age = value.ageStr;
																				newArr.imagePath = value.imagePath;
																				if(value.erBedTypeAllocation == '1')
																				{
																				newArr.erBedTypeAllocationDesc = "ER";
																				}
																			if(value.erBedTypeAllocation == '2')
																			{
																			newArr.erBedTypeAllocationDesc = "Observation";
																			}
																			if(value.erBedTypeAllocation == '3')
																				{
																				newArr.erBedTypeAllocationDesc = "Resuctination";
																				}
																			  if(value.visitTypeId == 4){
																				   $scope.listOfAdmittedPatientList.push(newArr);
//																				   $scope.admittedPatientCount=$scope.listOfAdmittedPatientList.length;
																				}


																			});
															$rootScope.stopLoader();

														}
													});
							}
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								$scope.pager.totalPages=undefined;
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.admittedPatientCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							
						$scope.vocateBedAgainstPatient = function(item)
						{
							console.log(item);
							$scope.currentDate = new Date();
							$scope.currentDate = moment($scope.currentDate)
									.format('DD-MM-YYYY HH:mm:ss');
							
							var data={
									organizationId :$scope.organizationId,
									unitId : $scope.unitId,
									dischargeId : item.dischargeId,
									admissionId :item.admissionId,
									bedId : item.bedId,
									updatedDate: $scope.currentDate,
									updatedBy : $scope.unitId,
									wardId  : item.wardId,
									nursingStationId : item.nursingStationId,
									floorId : item.floorId,
									roomId : item.roomId,
									bedCategoryId:item.bedCategoryId,
									isIcu : item.isIcu,
									patientId : item.patientId,
									visitTypeId : item.visitTypeId,
							
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
													growl
															.success(
																	'Patient Discharged Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope.init();
													$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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

