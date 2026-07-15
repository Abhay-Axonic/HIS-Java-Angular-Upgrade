'use strict';

/**
 * @Author By Sidheshwar Lamture
 * @name myApp.controller: laborRoomTransferRequestAcceptanceController
 * @description # laborRoomTransferRequestAcceptanceController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'laborRoomTransferRequestAcceptanceController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'PagerService',
						'growl',
						'$state',
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, PagerService, growl,
								$state,BillingGenericService,$window) {

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

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.admittedPatient = {};
								$scope.itemSelecteds = {};
								
								$scope.beditemSelecteds = {};
								$scope.bedTr = {};
								$scope.rejectTransferPopUpFlag = false;
							    $scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});
						

								/*var URI = BASE_URL + ROOT_URL
										+ "/adt/getPendingTransferRequestListOfLaborBed";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.listOfTransferRequestAcceptanceList = [];
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
//																			newArr.patientFullName = value.prefixDesc+' '+value.patientName;
																			newArr.patientFullName = value.prefixDesc+' '+value.patientName;
																			newArr.dob = value.dob;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.transferId = value.transferId;
																			newArr.hierarchyId = value.hierarchyId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.bedCategoryId = value.fromBedCategoryId;
																			newArr.billingBedCategoryId = value.fromBillingBedCategoryId;
																			newArr.fromRoomId = value.fromRoomId;
																			newArr.bedCategoryDesc = value.fromBedCategoryDesc;
																			newArr.doctorId  = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc; 
																			newArr.wardId = value.wardId;
																			newArr.wardName = value.wardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.remark = value.note;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.doctorFullName = value.doctorName;
																			newArr.transferRequestId = value.transferRequestId;
																			newArr.doctorNote = value.doctorNote;
																			newArr.transferTypeId = value.transferTypeId;
																			newArr.toBedId = value.toBedId;
																			newArr.fromWardId = value.fromWardId;
																			newArr.fromBedId = value.fromBedId;
																			newArr.genderId = value.genderId;
																			newArr.createdDate=value.createdDate;
																			newArr.prefixCode=value.prefixDesc;
																			newArr.imagePath=value.imagePath;
																			newArr.ageStr=value.ageStr;
																			$scope.listOfTransferRequestAcceptanceList.push(newArr);

																		});

													}
												});*/

							}

							$scope.init();
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.nurseRole = cookieObjectAppConfig.nurseRole;	
        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));

        $scope.isWardChanged = function()
        {      
            //start for nursing
            var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
            GenericService.serviceAction("GET",URI,{}).then(function(response){
                if (response.data.status == "success") {
                    $scope.departmentNursingStationMapperDtoslist = [];			  
                    $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
                    //alert($scope.roleName);
                    if($scope.nurseRole == 1){
                        if($scope.departmentNursingStationMapperDtoslist.length > 0)
                        {
                            if($scope.departmentNursingStationMapperDtoslist.length > 1){
                                angular.element('#nursingStationModal').modal('show');
                                //$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
                            }
                            else
                            {
                                var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                                GenericService.serviceAction("GET",URI,{}).then(function(response){
                                    if (response.data.status == "success") {
                                        $scope.departmentNursingStationListC = {};
                                        // console.log("fff",response.data.object);
                                        $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
                                        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                                        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                                        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                                        sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
                                        //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
                                        sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
                                        $scope.wardListForGetBedList = response.data.object;
                                    }
                                });
                            }
                        }
                    }
                }
            });        
        }

        $scope.departmentNursing = {};
        $scope.selectsetNursingStation = function()
        {
            if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
                growl.warning('Please select nursing station..!!');
                return false;
            }else{
            $scope.wardListForGetBedList=[];
            var data = JSON.parse($scope.departmentNursing);
            //console.log("$scope.departmentNursing",$scope.departmentNursing);
            //console.log("$scope.departmentNursing ID",data.nursingStationId);
            var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                GenericService.serviceAction("GET",URI,{}).then(function(response){
                    if (response.data.status == "success") {
                        $scope.departmentNursingStationListC = {};
                        //console.log('nursingStationsWardsList',response.data.object);
                        $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
                        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                        sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
						sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
                        $scope.wardListForGetBedList = response.data.object;
                        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
                        $scope.getPendingTransferRequestListOfLaborBedList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
                        $scope.init();
						angular.element('#nursingStationModal').modal('hide');
                    }
                });
            }
        }		
							
							//For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.getPendingTransferRequestListOfLaborBedList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getPendingTransferRequestListOfLaborBedList = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										wardList: $scope.wardListForGetBedList
									};

								var URI = BASE_URL + ROOT_URL
										+ "/adt/getPendingTransferRequestListOfLaborBed";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.listOfTransferRequestAcceptanceList = [];
													$scope.admittedPatientList = [];
													
													if (response.data.status == "success") {
														$scope.admittedPatientList = response.data.listObject;
														angular.forEach( $scope.admittedPatientList, function( value, index) {

																			var newArr = {};
																			newArr.patientFullName = value.prefixDesc+' '+value.patientName;
																			newArr.dob = value.dob;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.transferId = value.transferId;
																			newArr.hierarchyId = value.hierarchyId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.bedCategoryId = value.fromBedCategoryId;
																			newArr.billingBedCategoryId = value.fromBillingBedCategoryId;
																			newArr.fromRoomId = value.fromRoomId;
																			newArr.bedCategoryDesc = value.fromBedCategoryDesc;
																			newArr.doctorId  = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc; 
																			newArr.wardId = value.wardId;
																			newArr.wardName = value.wardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.remark = value.note;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.doctorFullName = value.doctorName;
																			newArr.transferRequestId = value.transferRequestId;
																			newArr.doctorNote = value.doctorNote;
																			newArr.transferTypeId = value.transferTypeId;
																			newArr.toBedId = value.toBedId;
																			newArr.fromWardId = value.fromWardId;
																			newArr.fromBedId = value.fromBedId;
																			newArr.genderId = value.genderId;
																			newArr.createdDate=value.createdDate;
																			newArr.prefixCode=value.prefixDesc;
																			newArr.imagePath=value.imagePath;
																			newArr.ageStr=value.ageStr;
																			$scope.listOfTransferRequestAcceptanceList.push(newArr);

																		});
														
														$scope.commonListCount = response.data.object;
								            			$scope.pager = {};
								            			$scope.page;
								            			$scope.setPage(1,false);
								            			

													}
												});


							}

							$scope.getPendingTransferRequestListOfLaborBedListMaster = function(orgId, offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										wardList: $scope.wardListForGetBedList
									};

								var URI = BASE_URL + ROOT_URL
										+ "/adt/getPendingTransferRequestListOfLaborBed";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.listOfTransferRequestAcceptanceList = [];
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
																			newArr.patientFullName = value.prefixDesc+' '+value.patientName;
																			newArr.dob = value.dob;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.transferId = value.transferId;
																			newArr.hierarchyId = value.hierarchyId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.bedCategoryId = value.fromBedCategoryId;
																			newArr.billingBedCategoryId = value.fromBillingBedCategoryId;
																			newArr.fromRoomId = value.fromRoomId;
																			newArr.bedCategoryDesc = value.fromBedCategoryDesc;
																			newArr.doctorId  = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc; 
																			newArr.wardId = value.wardId;
																			newArr.wardName = value.wardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.remark = value.note;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.doctorFullName = value.doctorName;
																			newArr.transferRequestId = value.transferRequestId;
																			newArr.doctorNote = value.doctorNote;
																			newArr.transferTypeId = value.transferTypeId;
																			newArr.toBedId = value.toBedId;
																			newArr.fromWardId = value.fromWardId;
																			newArr.fromBedId = value.fromBedId;
																			newArr.genderId = value.genderId;
																			newArr.createdDate=value.createdDate;
																			newArr.prefixCode=value.prefixDesc;
																			newArr.imagePath=value.imagePath;
																			newArr.ageStr=value.ageStr;
																			$scope.listOfTransferRequestAcceptanceList.push(newArr);

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
								$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getPendingTransferRequestListOfLaborBedListMaster($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.getPendingTransferRequestListOfLaborBedList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End
							
							
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								var transferId = $scope.itemSelecteds.transferId;
								$scope.acceptRequest(transferId);
							}
							
							
							$scope.selectItemBed = function(item) {
								 angular.forEach($scope.listOfTransferRequestAcceptanceList,function(value,index){
									 if($scope.selectedPatientId === value.patientId){
										 value.bedAllocObj=item;
									 }
								 });
							}

							
							$scope.selectedBedObject = function() {
								 var list=$.grep($scope.listOfTransferRequestAcceptanceList, function (obj) {
								        return $scope.selectedPatientId === obj.patientId;
								    });
								 if(angular.equals({}, list[0].bedAllocObj)){
									 growl.warning(
												'Select bed !!',
												{
													title : 'Warning!'
												});
									return false;
								 }else{
									 growl.success(
												'Bed allocated successfully...!!',
												{
													title : 'Success!'
												});
										angular.element('#bedAllocationForTransfer').modal(
												'hide');
								 }
								
							}
							
							$scope.transferIdForCheck = '';
							$scope.toSelectedBedId = 0;
							$scope.selectedPatientId=0;
							$scope.bedAllocastionTransfer = function(obj)
							{
								$scope.selectedPatientId=obj.patientId;
								obj.bedAllocObj={};
								var data = {
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								}
								
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getActiveLaborBedList";
						GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											$scope.activeBedList = [];

											if (response.data.status == "success") {
												$scope.activeBedList = response.data.listObject;

											}
										});
								
								
							}
							
							function isEmpty(obj) {
								for(var key in obj) {
									if(obj.hasOwnProperty(key))
									return true;
									}
									return false;
								}

							$scope.selectPatient = function(item) {
								
								$scope.transferIdForCheck = item.transferId;
								$scope.rejectTransferPopUpFlag = true;
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;

								}
							}

							$scope.acceptRequest = function(transferId) {
								if($scope.transferIdForCheck == transferId)
									{
										var list=$.grep($scope.listOfTransferRequestAcceptanceList, function (obj) {
									        return transferId === obj.transferId;
									    });
										
										 if(angular.equals({}, list[0].bedAllocObj) || list[0].bedAllocObj==undefined){
											 growl.warning(
														'Please allocate bed...!!',
														{
															title : 'Warning!'
														});
											return false;
										 }else {
											 $scope.beditemSelecteds=list[0].bedAllocObj;
											if ($scope.popUpFlag) {
												$scope.showModal = !$scope.showModal;

											} else {
												$rootScope.startLoader();
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
												var data = {
														organizationId:$scope.organizationId,
														unitId : $scope.unitId,
														//toBedCategoryId :$scope.itemSelecteds.toBedCategoryId,
														toBedId:$scope.beditemSelecteds.bedId,
														toWardId : $scope.beditemSelecteds.wardId,
														toRoomId :  $scope.beditemSelecteds.roomId,
													//toBillingBedCategoryId: $scope.beditemSelecteds.billingBedCategoryId,
														updatedBy:$scope.userId,
														updatedDate : $scope.currentDate,
														transferId : $scope.itemSelecteds.transferId,
														transferRequestId : $scope.itemSelecteds.transferRequestId,
														
														admissionId : $scope.itemSelecteds.admissionId,
														toFloorId : $scope.beditemSelecteds.floorId,
														admissionDetailsId : $scope.itemSelecteds.admissionDetailsId,
														fromBedId : $scope.itemSelecteds.fromBedId,
														//toBedCategoryId: $scope.itemSelectedsAccept.toBedCategoryId,
														//toBillingBedCategoryId:$scope.itemSelectedsAccept.toBillingBedCategoryId,
														//nursingStationId:$scope.itemSelectedsAccept.fromNursingStationId, // it's for from nursingstation
														//fromFloorId : $scope.beditemSelecteds.fromFloorId,
														fromWardId : $scope.itemSelecteds.fromWardId,
														fromRoomId :$scope.itemSelecteds.fromRoomId,
												}
												console.log(data);
												var URI = BASE_URL + ROOT_URL
												+ "/adt/acceptFinalLaborRoomTransferRequest";
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl.success(
																				'Transfer Request accepted...!!',
																				{
																					title : 'Success!'
																				});
																$scope.init();
																$scope.getPendingTransferRequestListOfLaborBedList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
															} else {
																growl.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
															$rootScope.stopLoader();

														});
										
										
											}
										}
									}
								else
									{
									growl.error(
											'Please select patient for acceptance...!!',
											{
												title : 'Error!'
											});
									return false;
									}
								
							}
							
							$scope.rejectRequest = function(transferId)
							{
								if($scope.transferIdForCheck == transferId)
								{
									$scope.reasonId=0;
									$scope.rejectionNote="";
									$('#accepatReject').modal('show');
								}
							else
								{
								growl
								.error(
										'Please select patient...!!',
										{
											title : 'Error!'
										});
								return false;
								}
							}
					
							$scope.saveAcceptReject = function()
							{
								//alert($scope.rejectReasonId);
								if($scope.reasonId==0 || $scope.reasonId==""){
									 growl.warning(
												'Select reason...!!',
												{
													title : 'Warning!'
												});
									return false;
								}
								
                               if($scope.rejectionNote==undefined || $scope.rejectionNote==""){
                            	   growl.warning(
											'Rejection note required ...!!',
											{
												title : 'Warning!'
											});
                            	   return false;
								}
                               $('#closeRejectionModal').trigger('click');
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select patient...!!',
													{
														title : 'Warning!'
													});
									return false;
								}
								else if($('#rejectReasonIdDropDn').val() == '' || $('#rejectReasonIdDropDn').val() == null || $('#rejectReasonIdDropDn').val() == undefined){
									growl
									.warning('Please select reason.',
											{
												title : 'Warning!'
											});
										return false;
								}
								else
									{
									
									angular.element('#accepatReject').modal('hide');
									$rootScope.startLoader();
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									var data = {
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											updatedBy:$scope.userId,
											updatedDate : $scope.currentDate,
											transferId : $scope.itemSelecteds.transferId,
											transferRequestId : $scope.itemSelecteds.transferRequestId,
											adtReasonId : $('#rejectReasonIdDropDn').val(),
											adtRejectReason :  $('#noteReject').val(),
											admissionId:$scope.itemSelecteds.admissionId,
											fromBedCategoryId:$scope.itemSelecteds.bedCategoryId,
											fromBillingBedCategoryId:$scope.itemSelecteds.billingBedCategoryId,
											fromWardId:$scope.itemSelecteds.fromWardId,
											fromRoomId:$scope.itemSelecteds.fromRoomId,
											fromBedId:$scope.itemSelecteds.fromBedId,
											toBedId: $scope.itemSelecteds.toBedId
									
									
									}
									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL
									+ REJECTTRANSFERREQUESTFORADT;
							GenericService
									.serviceAction("POST", URI,
											data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl.success(
																	'Transfer Request rejected successfully...!!',
																	{
																		title : 'Success!'
																	});
													
													 angular.element('#accepatReject').modal('hide');
													$scope.init();
													$scope.getPendingTransferRequestListOfLaborBedList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
												} else {
													growl.error(
																	'Something went wrong...!!',
																	{
																		title : 'Error!'
																	});
												}
												$rootScope.stopLoader();

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
