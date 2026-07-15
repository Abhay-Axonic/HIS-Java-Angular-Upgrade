'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:transferRequestController
 * @description #transferRequestController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'transferRequestController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,PaginationService) {
							
							/*if($cookies.get('bedManagmentCookies') != null)
							{
							$scope.bedManagmentCookiesObj =JSON.parse($cookies.get('bedManagmentCookies'));
							}
							else
							{
								$scope.bedManagmentCookiesObj =null;
							}*/
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							
							
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
							$scope.skipTransferAcceptance =cookieObjectAppConfig. skipTransferAcceptance;
							$scope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping;
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
							
							
							/* init() function for form object create. */
							$scope.init = function() {
								
								
								$rootScope.startLoader();
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
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
								$rootScope.itemSelectedsAccept = {};
								
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

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEWARDLIST;
								GenericService
										.serviceAction("POST", URI,
												commonObj)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.wardActiveList=response.data.listObject;
														console.log("$scope.wardActiveList",$scope.wardActiveList);
													}
												});
								
                              
								
								
								$scope.commonObjInitiat = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardList :  $scope.wardList
									};
								console.log($scope.commonObjInitiat);
								
								$scope.commonObjTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 1,
										wardList :  $scope.wardList
									};
								
								if ($scope.isShowPatientByBed == 'Y') {
									if($scope.nurseBedList.length>0){
										$scope.commonObjInitiat.bedList=$scope.nurseBedList;
										$scope.commonObjTransfer.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") 
													{
														$scope.reasonList = response.data.listObject;
														$rootScope.stopLoader();
													}
													else if (response.data.status == "error") 
														{
													     $rootScope.stopLoader();
														}
												});
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDCATEGORYLISTBYUNIT;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.bedCategoryList = [];
													if (response.data.status == "success")
													{
														$scope.bedCategoryList = response.data.listObject;
														$rootScope.stopLoader();
													}
													else if (response.data.status == "error") 
													{
												     $rootScope.stopLoader();
													}
												});
								
								if($scope.bedManagmentCookiesObj != null)
								{
									$rootScope.startLoader();
									
									var commonObjInitiatForBM = {
											admissionId:$scope.bedManagmentCookiesObj.admissionId,
											organizationId:$scope.organizationId,
											unitId : $scope.unitId
											};
									var commonObjTransferBM  = {
											admissionId:$scope.bedManagmentCookiesObj.admissionId,
											organizationId:$scope.organizationId,
											unitId : $scope.unitId
									};
									
									console.log($scope.bedManagmentCookiesObj.admissionId);
									
									var URI = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUESTFORBM;
									//		+ GETTRANSFERPATIENTLIST;
									GenericService
											.serviceAction("POST", URI, commonObjInitiatForBM)
											.then(
													function(response) {
														
														 $scope.pagination.data=[];
														$scope.listOfAdmittedPatientInitiatList = [];
														$scope.admittedPatientInitiatList = [];

														if (response.data.status == "success")
														{
															$scope.admittedPatientInitiatList = response.data.listObject;
															angular
																	.forEach(
																			$scope.admittedPatientInitiatList,
																			function(
																					value,
																					index) {

																				var newArr = {};
																				newArr.patientFullName = value.pFirstName;
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
																				newArr.bedCategoryId = value.bedCategoryId;
																				newArr.bedCategoryDesc = value.bedCategoryDesc;
																				newArr.doctorId = value.doctorId;
																				newArr.admissionDetailsId = value.admissionDetailsId;
																				newArr.transferStatusDesc = value.transferStatusDesc;
																				newArr.wardId = value.wardId;
																				newArr.wardName = value.wardName;
																				newArr.transferStatusId = value.transferStatusId;
																				newArr.reasonDesc = value.reasonDesc;
																				newArr.transferTypeId = value.transferTypeId;
	                                                                            newArr.ageStr = value.ageStr;
	                                                                            newArr.prefixDesc = value.prefixDesc;
	                                                                            newArr.imagePath = value.imagePath;

																				$scope.listOfAdmittedPatientInitiatList
																						.push(newArr);

																			});
															
															
															 if($scope.listOfAdmittedPatientInitiatList.length > 0){            
										                         PaginationService.searchData($scope.listOfAdmittedPatientInitiatList, $scope.pagination);
										                       }
															 $rootScope.stopLoader();
														}
														else if (response.data.status == "error") 
														{
													       $rootScope.stopLoader();
														}
													});
									var URI = BASE_URL + ROOT_URL
									+ GETTRANSFERREQUESTLISTBYADMISSIONIDFORB2B;
								GenericService
										.serviceAction("POST", URI, commonObjTransferBM)
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
																			newArr.patientFullName = value.pFirstName;
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
																			newArr.bedCategoryId = value.bedCategoryId;
																			newArr.bedCategoryDesc = value.bedCategoryDesc;
																			newArr.doctorId = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc;
																			newArr.wardId = value.wardId;
																			newArr.wardName = value.wardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.toBedNumber = value.toBedNumber;
																			newArr.toWardName = value.toWardname;
																			newArr.rejectReasonDesc = value.rejectReasonDesc;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.adtReasonDesc = value.adtReasonDesc;
																			newArr.adtRejectReason =  value.adtRejectReason;
																			newArr.doctorReasonDesc =  value.doctorReasonDesc;
																			newArr.doctorRejectReason =  value.doctorRejectReason;
																			newArr.transferRequestId =  value.transferRequestId;
																			newArr.toBedId = value.toBedId;
																			newArr.toWardId = value.toWardId;
																			newArr.toFloorId = value.toFloorId;
																			newArr.toRoomId = value.toRoomId;
																			newArr.fromBedId = value.fromBedId;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.toBillingBedCategoryId = value.billingBedCategoryId;
																			newArr.transferTypeId = value.transferTypeId;
																			newArr.remark = value.note;
																			newArr.fromBillingBedCategoryId  = value.fromBillingBedCategoryId;
																			newArr.fromWardId = value.fromWardId;
																			newArr.fromRoomId = value.fromRoomId;
                                                                            newArr.ageStr = value.ageStr;
                                                                            newArr.prefixDesc = value.prefixDesc;
                                                                            newArr.imagePath = value.imagePath;
                                                                            newArr.doctorNote = value.doctorNote;
                                                                            newArr.fromFloorId = value.fromFloorId;
																			$scope.listOfAdmittedPatientList
																					.push(newArr);

																		});

														 $rootScope.stopLoader();
													}
													else if (response.data.status == "error") 
													{
												       $rootScope.stopLoader();
													}
												});
								
								}
								else
									{

							    $scope.initiateTransferRequestList = function(){

                                $rootScope.startLoader();
							    if($scope.nurseRole == 1 && $scope.wardList.length > 0){
                                var URI = BASE_URL+ROOT_URL+GETINITIATETRANSFERREQUEST;
                                //		+ GETTRANSFERPATIENTLIST;
                                GenericService
                                        .serviceAction("POST", URI, $scope.commonObjInitiat)
                                        .then(
                                                function(response) {
                                                    $scope.pagination.data=[];
                                                    $scope.listOfAdmittedPatientInitiatList = [];
                                                    $scope.admittedPatientInitiatList = [];

                                                    if (response.data.status == "success")
                                                    {
                                                        $scope.admittedPatientInitiatList = response.data.listObject;
                                                        angular
                                                                .forEach(
                                                                        $scope.admittedPatientInitiatList,
                                                                        function(
                                                                                value,
                                                                                index) {
                                                                            if(value.visitTypeId!=4){

                                                                            var newArr = {};
                                                                            newArr.patientFullName = value.pFirstName;
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
                                                                            newArr.bedCategoryId = value.bedCategoryId;
                                                                            newArr.bedCategoryDesc = value.bedCategoryDesc;
                                                                            newArr.doctorId = value.doctorId;
                                                                            newArr.admissionDetailsId = value.admissionDetailsId;
                                                                            newArr.transferStatusDesc = value.transferStatusDesc;
                                                                            newArr.wardId = value.wardId;
                                                                            newArr.wardName = value.wardName;
                                                                            newArr.transferStatusId = value.transferStatusId;
                                                                            newArr.reasonDesc = value.reasonDesc;
                                                                            newArr.ageStr = value.ageStr;
                                                                            newArr.prefixDesc = value.prefixDesc;
                                                                            newArr.imagePath = value.imagePath;

                                                                            $scope.listOfAdmittedPatientInitiatList
                                                                                    .push(newArr);
                                                                            }
                                                                        });

                                                           if($scope.listOfAdmittedPatientInitiatList.length > 0){
                                                             PaginationService.searchData($scope.listOfAdmittedPatientInitiatList, $scope.pagination);
                                                           }else{
                                                             $scope.pagination.data = "";
                                                           }
                                                         $rootScope.stopLoader();
                                                    }
                                                    else if (response.data.status == "error")
                                                    {
                                                       $rootScope.stopLoader();
                                                    }
                                                });

                                    }

							}
                            $scope.initiateTransferRequestList();
						       }

							}
							$scope.init();


							$scope.physicalTransferRequestList = function(){

                                $rootScope.startLoader();
							    if($scope.nurseRole == 1 && $scope.wardList.length > 0){

                                var URI = BASE_URL + ROOT_URL
                                    + GETTRANSFERPATIENTLIST;
                                GenericService
                                        .serviceAction("POST", URI, $scope.commonObjTransfer)
                                        .then(
                                                function(response) {
                                                    $scope.listOfAdmittedPatientList = [];
                                                    $scope.admittedPatientList = [];

                                                    if (response.data.status == "success")
                                                    {
                                                        $scope.admittedPatientList = response.data.listObject;
                                                        angular
                                                                .forEach(
                                                                        $scope.admittedPatientList,
                                                                        function(
                                                                                value,
                                                                                index) {

                                                                            var newArr = {};
                                                                            newArr.patientFullName = value.pFirstName;
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
                                                                            newArr.bedCategoryId = value.bedCategoryId;
                                                                            newArr.bedCategoryDesc = value.bedCategoryDesc;
                                                                            newArr.doctorId = value.doctorId;
                                                                            newArr.admissionDetailsId = value.admissionDetailsId;
                                                                            newArr.transferStatusDesc = value.transferStatusDesc;
                                                                            newArr.wardId = value.wardId;
                                                                            newArr.wardName = value.wardName;
                                                                            newArr.transferStatusId = value.transferStatusId;
                                                                            newArr.toBedCategoryDesc = value.toBedCategoryDesc;
                                                                            newArr.toBedNumber = value.toBedNumber;
                                                                            newArr.toWardName = value.toWardname;
                                                                            newArr.rejectReasonDesc = value.rejectReasonDesc;
                                                                            newArr.reasonDesc = value.reasonDesc;
                                                                            newArr.adtReasonDesc = value.adtReasonDesc;
                                                                            newArr.adtRejectReason =  value.adtRejectReason;
                                                                            newArr.doctorReasonDesc =  value.doctorReasonDesc;
                                                                            newArr.doctorRejectReason =  value.doctorRejectReason;
                                                                            newArr.transferRequestId =  value.transferRequestId;
                                                                            newArr.toBedId = value.toBedId;

                                                                            newArr.remark = value.note;

                                                                            newArr.fromBillingBedCategoryId  = value.fromBillingBedCategoryId;
                                                                            newArr.fromWardId = value.fromWardId;
                                                                            newArr.fromRoomId = value.fromRoomId;
                                                                            newArr.fromBedId = value.fromBedId;

                                                                            newArr.ageStr = value.ageStr;
                                                                            newArr.prefixDesc = value.prefixDesc;
                                                                            newArr.imagePath = value.imagePath;

                                                                            newArr.doctorNote = value.doctorNote;
                                                                            newArr.transferTypeId = value.transferTypeId;
                                                                            newArr.doctorNote = value.doctorNote;

                                                                            newArr.fromFloorId = value.fromFloorId;
                                                                            newArr.toWardId = value.toWardId;
                                                                            newArr.toFloorId = value.toFloorId;
                                                                            newArr.toRoomId = value.toRoomId;
                                                                            newArr.toBedCategoryId = value.toBedCategoryId;
                                                                            newArr.toBillingBedCategoryId = value.billingBedCategoryId;

                                                                            $scope.listOfAdmittedPatientList
                                                                                    .push(newArr);

                                                                        });
                                                             if($scope.listOfAdmittedPatientList.length > 0){
                                                                 PaginationService.searchData($scope.listOfAdmittedPatientList, $scope.pagination);
                                                               }else{
                                                                    $scope.pagination.data = "";
                                                                }

                                                         $rootScope.stopLoader();
                                                    }
                                                    else if (response.data.status == "error")
                                                    {
                                                       $rootScope.stopLoader();
                                                    }
                                                });
                                                console.log("listOfAdmittedPatientList======", $scope.listOfAdmittedPatientList);
                                }

							}

                            $scope.searchWardInitiateRequest = function(searchKeyword){

                                var commonObjInitiat = {
                                    unitId : $scope.unitId,
                                    organizationId : $scope.organizationId,
                                    wardList :  $scope.wardList,
                                    searchKeyword : searchKeyword
                                };

                                $rootScope.startLoader();
                                var URI = BASE_URL+ROOT_URL+GETINITIATETRANSFERREQUEST;
                                    //		+ GETTRANSFERPATIENTLIST;
                                    GenericService
                                            .serviceAction("POST", URI, commonObjInitiat)
                                            .then(
                                                    function(response) {
                                                        $scope.pagination.data=[];
                                                        $scope.listOfAdmittedPatientInitiatList = [];
                                                        $scope.admittedPatientInitiatList = [];

                                                        if (response.data.status == "success")
                                                        {
                                                            $scope.admittedPatientInitiatList = response.data.listObject;
                                                            angular
                                                                    .forEach(
                                                                            $scope.admittedPatientInitiatList,
                                                                            function(
                                                                                    value,
                                                                                    index) {
                                                                                if(value.visitTypeId!=4){

                                                                                var newArr = {};
                                                                                newArr.patientFullName = value.pFirstName;
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
                                                                                newArr.bedCategoryId = value.bedCategoryId;
                                                                                newArr.bedCategoryDesc = value.bedCategoryDesc;
                                                                                newArr.doctorId = value.doctorId;
                                                                                newArr.admissionDetailsId = value.admissionDetailsId;
                                                                                newArr.transferStatusDesc = value.transferStatusDesc;
                                                                                newArr.wardId = value.wardId;
                                                                                newArr.wardName = value.wardName;
                                                                                newArr.transferStatusId = value.transferStatusId;
                                                                                newArr.reasonDesc = value.reasonDesc;
                                                                                newArr.ageStr = value.ageStr;
                                                                                newArr.prefixDesc = value.prefixDesc;
                                                                                newArr.imagePath = value.imagePath;

                                                                                $scope.listOfAdmittedPatientInitiatList
                                                                                        .push(newArr);
                                                                                }
                                                                            });

                                                             if($scope.listOfAdmittedPatientInitiatList.length > 0){
                                                                 PaginationService.searchData($scope.listOfAdmittedPatientInitiatList, $scope.pagination);
                                                               }else{
                                                                    $scope.pagination.data = "";
                                                                  }
                                                             $rootScope.stopLoader();
                                                        }
                                                        else if (response.data.status == "error")
                                                        {
                                                           $rootScope.stopLoader();
                                                        }
                                                    });

                            }


                            $scope.searchWardPhysicalTransferRequest = function(searchKeyword){

                                var commonObjTransfer = {
                                    unitId : $scope.unitId,
                                    organizationId : $scope.organizationId,
                                    transferTypeId : 1,
                                    wardList :  $scope.wardList,
                                    searchKeyword: searchKeyword
                                };

                                $rootScope.startLoader();

                                var URI = BASE_URL + ROOT_URL
                                    + GETTRANSFERPATIENTLIST;
                                GenericService
                                        .serviceAction("POST", URI, commonObjTransfer)
                                        .then(
                                                function(response) {
                                                    $scope.listOfAdmittedPatientList = [];
                                                    $scope.admittedPatientList = [];

                                                    if (response.data.status == "success")
                                                    {
                                                        $scope.admittedPatientList = response.data.listObject;
                                                        angular
                                                                .forEach(
                                                                        $scope.admittedPatientList,
                                                                        function(
                                                                                value,
                                                                                index) {

                                                                            var newArr = {};
                                                                            newArr.patientFullName = value.pFirstName;
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
                                                                            newArr.bedCategoryId = value.bedCategoryId;
                                                                            newArr.bedCategoryDesc = value.bedCategoryDesc;
                                                                            newArr.doctorId = value.doctorId;
                                                                            newArr.admissionDetailsId = value.admissionDetailsId;
                                                                            newArr.transferStatusDesc = value.transferStatusDesc;
                                                                            newArr.wardId = value.wardId;
                                                                            newArr.wardName = value.wardName;
                                                                            newArr.transferStatusId = value.transferStatusId;
                                                                            newArr.toBedCategoryDesc = value.toBedCategoryDesc;
                                                                            newArr.toBedNumber = value.toBedNumber;
                                                                            newArr.toWardName = value.toWardname;
                                                                            newArr.rejectReasonDesc = value.rejectReasonDesc;
                                                                            newArr.reasonDesc = value.reasonDesc;
                                                                            newArr.adtReasonDesc = value.adtReasonDesc;
                                                                            newArr.adtRejectReason =  value.adtRejectReason;
                                                                            newArr.doctorReasonDesc =  value.doctorReasonDesc;
                                                                            newArr.doctorRejectReason =  value.doctorRejectReason;
                                                                            newArr.transferRequestId =  value.transferRequestId;
                                                                            newArr.toBedId = value.toBedId;

                                                                            newArr.remark = value.note;

                                                                            newArr.fromBillingBedCategoryId  = value.fromBillingBedCategoryId;
                                                                            newArr.fromWardId = value.fromWardId;
                                                                            newArr.fromRoomId = value.fromRoomId;
                                                                            newArr.fromBedId = value.fromBedId;

                                                                            newArr.ageStr = value.ageStr;
                                                                            newArr.prefixDesc = value.prefixDesc;
                                                                            newArr.imagePath = value.imagePath;

                                                                            newArr.doctorNote = value.doctorNote;
                                                                            newArr.transferTypeId = value.transferTypeId;
                                                                            newArr.doctorNote = value.doctorNote;

                                                                            newArr.fromFloorId = value.fromFloorId;
                                                                            newArr.toWardId = value.toWardId;
                                                                            newArr.toFloorId = value.toFloorId;
                                                                            newArr.toRoomId = value.toRoomId;
                                                                            newArr.toBedCategoryId = value.toBedCategoryId;
                                                                            newArr.toBillingBedCategoryId = value.billingBedCategoryId;

                                                                            $scope.listOfAdmittedPatientList
                                                                                    .push(newArr);

                                                                        });
                                                                if($scope.listOfAdmittedPatientList.length > 0){
                                                                 PaginationService.searchData($scope.listOfAdmittedPatientList, $scope.pagination);
                                                               }else{
                                                                 $scope.pagination.data = "";
                                                               }

                                                         $rootScope.stopLoader();
                                                    }
                                                    else if (response.data.status == "error")
                                                    {
                                                       $rootScope.stopLoader();
                                                    }
                                                });

                            }


							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.submitRequest();
							}

							$scope.selectPatient = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;

								}
							}

							$scope.submitRequest = function() {

								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								} else {
									
									if($scope.itemSelecteds.requiredBedCategory=="" || $scope.itemSelecteds.requiredBedCategory ==undefined || $scope.itemSelecteds.requiredBedCategory =="Select Bed" ){
										$rootScope.stopLoader();
										growl.warning('Please select Bed...!!',
												{
													title : 'warning!'
												});
										return false;
									} 
									
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');

										var data = {
											treatingDoctorId : $scope.itemSelecteds.doctorId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											admissionId : $scope.itemSelecteds.admissionId,
											transferId : $scope.itemSelecteds.transferId,
											fromWardId : $scope.itemSelecteds.wardId,
											reasonId : $scope.itemSelecteds.reasonId,
											note : $scope.itemSelecteds.remark,
											authorizedBy : $scope.userId,
											fromBedCategoryId : $scope.itemSelecteds.bedCategoryId,
											//transferType :
											createdBy : $scope.userId,
											createdDate : $scope.currentDate,
											updatedBy : $scope.userId,
											updatedDate : $scope.currentDate,
											transferTypeId : 1
										}

										console
												.log($scope.itemSelecteds.requiredBedCategory);
										var array = $scope.itemSelecteds.requiredBedCategory
												.split('_');
										//alert($scope.itemSelecteds.hierarchyId);
										//alert(array[1]);
										if (array[1] > $scope.itemSelecteds.hierarchyId) {
											console.log(array[0] + "__"
													+ array[1]);
											data.toBedCategoryId = array[0];
											data.transferStatusId = 3;
										} else {
											console.log(array[0] + "__"
													+ array[1]);
											data.toBedCategoryId = array[0];
											data.transferStatusId = 4;
										}
										console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ ADDTRANSFERREQUEST;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Transfer Request Sent Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.init();
																$scope.setFirstTabLevel(1);
																 $rootScope.stopLoader();
															} else if (response.data.status == "error")
															{
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
																 $rootScope.stopLoader();
															}

														});

									}
								}
							}

							$scope.finalTransfer = function() {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									transferdId : $scope.transferdId,
									tranferRequestId : $scope.transferReqestId,

								};

							}
							$scope.adtReason = '';
							$scope.adtRejectReason = '';
							$scope.doctorReason = '';
							$scope.doctorRejectReason = '';
							$scope.reasonPopupDoctor = function(resonDesc,rejectNote)
							{
								$scope.doctorRejectReason = "";
								$scope.doctorRejectionNote = "";
								$scope.doctorRejectReason = resonDesc;
								$scope.doctorRejectionNote = rejectNote;
								
							}
							$scope.reasonPopupADT = function(resonDesc,rejectNote)
							{
								$scope.ADTRejectReason = "";
								$scope.ADTRejectionNote = "";
								$scope.ADTRejectReason = resonDesc;
								$scope.ADTRejectionNote = rejectNote;
							}
							
							$scope.getAdmissionDataAccept = function(item)
							{
								//alert("sc");
								//console.log(item);
								$rootScope.itemSelectedsAccept = [];
								$rootScope.itemSelectedsAccept = item;
								console.log($rootScope.itemSelectedsAccept);
								
							}
							
							$scope.saveAcceptance = function()
							{
								//$rootScope.startLoader();
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								var tansferNote = $('#noteTransfer').val();
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										transferId:$scope.itemSelectedsAccept.transferId,
											updatedBy:$scope.userId,
											updatedDate:$scope.currentDate,
												transferRequestId:$scope.itemSelectedsAccept.transferRequestId,
													finalNote :tansferNote											
										
								};
								
								console.log(data);
								angular.element('#commonNote').modal('hide');
								//return false;
								var URI = BASE_URL + ROOT_URL + ACCEPTTRANSFERREQUESTONREQUEST;
								if($scope.skipTransferAcceptance!=undefined && $scope.skipTransferAcceptance!=null 
										&& $scope.skipTransferAcceptance == 'Y'){
									URI = BASE_URL + ROOT_URL + ACCEPTFINALTRANSFERREQUEST;
									 data = {
											admissionId: $scope.itemSelectedsAccept.admissionId,
											fromBedId: $scope.itemSelectedsAccept.fromBedId ,
											fromFloorId: $scope.itemSelectedsAccept.fromFloorId ,
											fromRoomId: $scope.itemSelectedsAccept.fromRoomId ,
											fromWardId: $scope.itemSelectedsAccept.fromWardId ,
											nursingStationId: $scope.itemSelectedsAccept.fromNursingStationId ,
											organizationId:  $scope.organizationId ,
											skipHousekeeping: $scope.skipHousekeeping,
											toBedCategoryId: $scope.itemSelectedsAccept.toBedCategoryId ,
											toBedId: $scope.itemSelectedsAccept.toBedId ,
											toBillingBedCategoryId: $scope.itemSelectedsAccept.toBillingBedCategoryId ,
											toFloorId: $scope.itemSelectedsAccept.toFloorId ,
											toRoomId: $scope.itemSelectedsAccept.toRoomId ,
											toWardId: $scope.itemSelectedsAccept.toWardId ,
											transferId: $scope.itemSelectedsAccept.transferId ,
											transferRequestId: $scope.itemSelectedsAccept.transferRequestId ,
											unitId: $scope.unitId,
											updatedBy:$scope.userId,
											updatedDate: $scope.currentDate,
											workstationNote: tansferNote
									};
		
								}
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") 
													{
														growl
																.success(
																		'Ward Transferred Successfully...!!',
																		{
																			title : 'Success!'
																		});
														//commonNote
														/*angular.element('#commonNote').modal('hide');*/
														$state.reload();
														$rootScope.stopLoader();
													} else if (response.data.status == "error"){
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$rootScope.stopLoader();
													}

												});
							}
							
							$scope.saveAcceptReject = function()
							{
								
								$rootScope.startLoader();
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										transferId:$scope.itemSelectedsAccept.transferId,
											updatedBy:$scope.userId,
											updatedDate:$scope.currentDate,
												transferRequestId:$scope.itemSelectedsAccept.transferRequestId,
												toBedId : $scope.itemSelectedsAccept.toBedId,
												adtRejectReason :$('#noteRejectDrpDN').val(),
												adtReasonId : $('#rejectReasonIdDrpDN').val(),
												admissionId:$scope.itemSelectedsAccept.admissionId,
												fromBedCategoryId:$scope.itemSelectedsAccept.bedCategoryId,
												fromBillingBedCategoryId:$scope.itemSelectedsAccept.fromBillingBedCategoryId,
												fromWardId:$scope.itemSelectedsAccept.fromWardId,
												fromRoomId:$scope.itemSelectedsAccept.fromRoomId,
												fromBedId:$scope.itemSelectedsAccept.fromBedId,
												
										
								};
								
								if(data.adtReasonId=="" || data.adtReasonId==undefined || data.adtReasonId==null){
									growl.warning('Please Select Reason..',
											{
												title : 'warning'
											});
									$rootScope.stopLoader();
									return false;
								}
								angular.element(
								'#accepatReject')
								.modal('hide');
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + REJECTTRANSFERREQUESTFORADT;
										//+ REJECTTRANSFERREQUESTONREQUEST;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Ward Transfer Request Aborted...!!',
																		{
																			title : 'Success!'
																		});
												
														$state.reload();
														$rootScope.stopLoader();
													} else if(response.data.status == "error"){
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$rootScope.stopLoader();
													}

												});
							}
							$scope.searchGroupList = function(keyword) 
							{

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ GETADMITTEDPATIENTBYKEWWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										// alert("ff");
										// console.log(response.data.listObject);
										return response.data.listObject;
									}
								});
								// return data="[{name:'test'}]";
							}
							
							
							/*First Level*/
					    	$scope.firstTabLevel = 1 ;
					    	
					  
							
							$scope.setFirstTabLevel= function(newTab)
							{
								$scope.firstTabLevel = newTab;

                                $scope.pagination.currentPage= 1;
                                $scope.pagination.totalPages= 0;
                                $scope.pagination.pageSize= '10';
                                $scope.pagination.search= '';
                                $scope.pagination.searchData= PaginationService.searchData;
                                $scope.pagination.setTotalPages= PaginationService.setTotalPages;
                                $scope.pagination.getPagesList= PaginationService.getPagesList;
                                $scope.pagination.setPage= PaginationService.setPage;
                                $scope.pagination.setFirstPage= PaginationService.setFirstPage;
                                $scope.pagination.setPreviousPage= PaginationService.setPreviousPage;
                                $scope.pagination.setNextPage= PaginationService.setNextPage;
                                $scope.pagination.setLastPage= PaginationService.setLastPage;
                                $scope.pagination.sortBy= PaginationService.sortBy;

								if(newTab == 1){

                                    $scope.initiateTransferRequestList();

								}else if(newTab == 2){

                                    $scope.physicalTransferRequestList();

								}
							};
							
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							
							$scope.selectPatientId = function(info, model,
									label) 
									{
								
								 $rootScope.startLoader();
								var commonObjInitiatForBM = {
										admissionId:info.admissionId,
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
										};
								var commonObjTransferBM  = {
										admissionId:info.admissionId,
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								};
								
								var URI = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUESTFORBM;
								//		+ GETTRANSFERPATIENTLIST;
								GenericService
										.serviceAction("POST", URI, commonObjInitiatForBM)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientInitiatList = [];
													$scope.admittedPatientInitiatList = [];

													if (response.data.status == "success") 
													{
														$scope.admittedPatientInitiatList = response.data.listObject;
														angular
																.forEach(
																		$scope.admittedPatientInitiatList,
																		function(
																				value,
																				index) {

																			var newArr = {};
																			newArr.patientFullName = value.pFirstName;
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
																			newArr.bedCategoryId = value.bedCategoryId;
																			newArr.bedCategoryDesc = value.bedCategoryDesc;
																			newArr.doctorId = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc;
																			newArr.wardId = value.wardId;
																			newArr.wardName = value.wardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.transferTypeId = value.transferTypeId;
                                                                            newArr.ageStr = value.ageStr;
                                                                            newArr.prefixDesc = value.prefixDesc;
                                                                            newArr.imagePath = value.imagePath;

																			$scope.listOfAdmittedPatientInitiatList
																					.push(newArr);

																		});
														
														 $rootScope.stopLoader();
													}
													else if(response.data.status == "error")
														{
														   $rootScope.stopLoader();
														}
													
												});
								var URI = BASE_URL + ROOT_URL
								+ GETTRANSFERREQUESTLISTBYADMISSIONIDFORB2B;
							GenericService
									.serviceAction("POST", URI, commonObjTransferBM)
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
																		newArr.patientFullName = value.pFirstName;
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
																		newArr.bedCategoryId = value.bedCategoryId;
																		newArr.bedCategoryDesc = value.bedCategoryDesc;
																		newArr.doctorId = value.doctorId;
																		newArr.admissionDetailsId = value.admissionDetailsId;
																		newArr.transferStatusDesc = value.transferStatusDesc;
																		newArr.wardId = value.wardId;
																		newArr.wardName = value.wardName;
																		newArr.transferStatusId = value.transferStatusId;
																		newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																		newArr.toBedNumber = value.toBedNumber;
																		newArr.toWardName = value.toWardname;
																		newArr.rejectReasonDesc = value.rejectReasonDesc;
																		newArr.reasonDesc = value.reasonDesc;
																		newArr.adtReasonDesc = value.adtReasonDesc;
																		newArr.adtRejectReason =  value.adtRejectReason;
																		newArr.doctorReasonDesc =  value.doctorReasonDesc;
																		newArr.doctorRejectReason =  value.doctorRejectReason;
																		newArr.transferRequestId =  value.transferRequestId;
																		newArr.toBedId = value.toBedId;
																		newArr.toWardId = value.toWardId;
																		newArr.toFloorId = value.toFloorId;
																		newArr.toRoomId = value.toRoomId;
																		newArr.fromBedId = value.fromBedId;
																		newArr.toBedCategoryId = value.toBedCategoryId;
																		newArr.toBillingBedCategoryId = value.billingBedCategoryId;
																		newArr.transferTypeId = value.transferTypeId;
																		newArr.remark = value.note;
                                                                        newArr.ageStr = value.ageStr;
                                                                        newArr.prefixDesc = value.prefixDesc;
                                                                        newArr.imagePath = value.imagePath;
                                                                        newArr.doctorNote = value.doctorNote;
                                                                        newArr.fromFloorId = value.fromFloorId;
																		$scope.listOfAdmittedPatientList
																				.push(newArr);

																	});
													 $rootScope.stopLoader();
												}
												else if(response.data.status == "error")
												{
												   $rootScope.stopLoader();
												}
											});

								
							}

						} ]);
