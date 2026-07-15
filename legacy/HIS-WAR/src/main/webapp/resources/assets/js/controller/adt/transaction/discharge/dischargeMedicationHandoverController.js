'use strict';

/**
 * @Author By  Nikita Chintawar
 * @name myApp.controller:dischargeMedicationHandoverController
 * @description # dischargeMedicationHandoverController Controller of the myApp
 */
angular.module('myApp')
		.controller(
				'dischargeMedicationHandoverController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'GenericServiceParamHeader',
						'growl',
						'$state',
						'BillingGenericService',
						'reportService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, GenericServiceParamHeader,growl,
								$state,BillingGenericService,reportService, CONSTANTS) {

							$scope.init = function() {
                                var cookieObject = $cookies.getObject('cookieObject');
                                var bedManagementcookieObject = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
                                if(cookieObject == undefined){
                                    $state.go('login');
                                    return;
                                }
                                if($state.params.patientDetailsObj.admissionId)
                                {
                                    $scope.admissionId = $state.params.patientDetailsObj.admissionId;
                                }
                                else if (bedManagementcookieObject != undefined){
                                    $scope.admissionId = bedManagementcookieObject.admissionId;
                                }
                                $scope.unitId = cookieObject.unitId;
                                $scope.organizationId = cookieObject.organizationId;
                                $scope.userId = cookieObject.userId;
                                $scope.visitTypeId = 1;
                                $scope.doctorIdForBedList  = cookieObject.doctorId;
                                $rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
                            }

                            $scope.init();

                            $scope.getFloorList = function () {
                                var URI = BASE_URL + ROOT_URL + GETACTIVEFLOORLIST;
                                var commonObj = {
								    unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
                                GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
                                    $scope.activeFloorList = [];

                                    if (response.data.status == "success") {
                                        $scope.activeFloorList = response.data.listObject;

                                    }
                                });
                            }

                            $scope.getFloorList();

                            $scope.getAdmittedPatientByIds = function(commonType) {
								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								switch (commonType) {
								    case 'floor': 
									    commonData.floorId = $scope.floorId;
									    var URI = BASE_URL + ROOT_URL + GETWARDLISTBYFLOORIDORGUNITID;
									    GenericService.serviceAction("POST", URI,commonData).then(function(response) {
                                            $scope.wardList = [];
                                                if (response.data.status == "success") {
                                                    $scope.wardList = response.data.listObject;

                                                }
                                            });
									    break;
								    default:
								}
							}

                            $scope.getPatientList = function(searchKeyword)
                            {
                                try 
                                {
                                    var data = {
                                        "orgId":$scope.organizationId,
                                        "orgUnitId":$scope.unitId,
                                        "visitTypeId":$scope.visitTypeId,
                                        "searchKeyword":searchKeyword,
                                    }
										
                                    var URI = BASE_URL + ROOT_URL + LIS_TRANSACTION + SEARCH_PHLEBOTOMY_WORK_LIST_PATIENT;
                                    return GenericService.serviceAction("POST", URI,data).then(function(response) {
                                        return response.data.listObject;
                                    });
                                } catch (e) {
                                    console.log(e.message)
                                }
                            }

                            $scope.selectPatient = function($item, $model,$label)
                            {
                                $scope.selectedPatient = $item.id;
							    $scope.uhid=$item.uhid;
                            }

                            $scope.checkPatient = function()
                            {
                                if($scope.patientId ==undefined)
                                {
                                    $scope.selectedPatient = 0;
                                }
                            }

                            $scope.getActiveDoctorList = function () {
                                var URI = BASE_URL + ROOT_URL + "/api/global/doctor/active";
                                var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
						        GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
                                $scope.allDoctorsList = [];
                                    if (response.data.status == "success") {
                                        $scope.allDoctorsList = response.data.listObject;
                                    }
                                });
                            }
                            $scope.getActiveDoctorList();

                            $scope.searchDisMedicationHandover = function () {
                                var URI = BASE_URL + ROOT_URL + "/api/pharmacy/ip/pending/issues/acceptance/medication/medicationhandoversearch/" + $scope.admissionId;
                                var obj = {
                                    floorId: !!$scope.floorId && !!$scope.floorId ? $scope.floorId : undefined,
                                    wardId: !!$scope.wardId && !!$scope.wardId ? $scope.wardId : undefined,
                                    nursingStationId: !!$scope.nursingStationId && !!$scope.nursingStationId ? $scope.nursingStationId : undefined,
                                    patientId: !!$scope.patientId && !!$scope.patientId ? $scope.patientId : undefined,
                                    uhid: !!$scope.uhid && !!$scope.uhid ? $scope.uhid : undefined,
                                    doctorId: !!$scope.doctorId && !!$scope.doctorId ? $scope.doctorId : undefined,
                                    status: !!$scope.status && !!$scope.status ? $scope.status : undefined,
                                    visitType: $state.params.patientDetailsObj.visitTypeId
                                }
                                GenericService.serviceAction("POST", URI, obj).then(function(response) {
                                $scope.searchDisMedicationHandoverData = [];
                                    if (response.status == 200) {
                                        $scope.searchDisMedicationHandoverData = response.data;
                                    }
                                });
                            }
                            $scope.searchDisMedicationHandover();

                            $scope.getDisMedicationHandover = function (info) {
                                var data = {};
                                $scope.Handoverstatus = info.status;
                                var URI = BASE_URL + ROOT_URL + "/api/pharmacy/ip/pending/issues/acceptance/medication/medicationhandoverdetail/"+ $scope.unitId +"/" + $scope.admissionId;
                                GenericService.serviceAction("GET", URI, data).then(function(response) {
                                $scope.getDisMedicationHandoverData = [];
                                    if (response.status == 200) {
                                        $scope.getDisMedicationHandoverData = response.data;
                                        $scope.getDisMedicationHandoverData.items.forEach(function(info){
                                            info.handoverQty = info.acceptedQty;
                                        })
                                        $scope.receiverName = response.data.receiverName;
                                        $scope.receiverContact = response.data.receiverContactDetails;
                                        $scope.relation = $scope.relationList.find(function (key) {
                                            return key.desc == response.data.relationship;
                                        });
                                    }
                                });
                            }

                            $scope.relationList = function () {
                                var URI = BASE_URL + ROOT_URL + GETACTIVERELATIONLIST;
                                var data = {};
								GenericService.serviceAction("GET", URI, data).then(function(response) {
                                    if (response.data.status == "success") {
                                        $scope.relationList = response.data.listObject;
                                    }
                                });
                            }
                            $scope.relationList();

                            $scope.validateQty = function(info){
                                if(info.handoverQty > info.acceptedQty){
                                    growl.warning("Handover quantity is greater than received quantity");
                                    info.handoverQty = "";
                                }
                            }

                            $scope.closePopup = function () {
                                $('#medicationDetailsModal').modal('hide');
                            }

                            $scope.validate = function () {
                                var isValidItem = true;
                                if($scope.receiverName == undefined){
                                    $scope.isvalidateError = true;
                                    growl.error('Please enter receiver name');
                                    isValidItem = false;
                                }
                                if($scope.receiverContact == undefined){
                                    $scope.isvalidateError = true;
                                    growl.error('Please enter receiver contact details');
                                    isValidItem = false;
                                }
                                if($scope.relation == undefined){
                                    $scope.isvalidateError = true;
                                    growl.error('Please select relationship');
                                    isValidItem = false;
                                }
                                $scope.getDisMedicationHandoverData.items.forEach(function(data){
                                    if(data.handoverQty == ''){
                                        $scope.getDisMedicationHandoverData['isvalidateError'] = true;
                                        growl.error('Please enter handover quantity');
                                        isValidItem = false;
                                    }
                                })
                                return isValidItem;
                            }

                            $scope.saveMedicationHandover = function (){
                                if($scope.validate()){
                                    var medication = $scope.getDisMedicationHandoverData;
                                    var URI = BASE_URL + ROOT_URL + "/api/pharmacy/ip/pending/issues/acceptance/medication/medicationhandoversave";
                                    var data = {
                                        "prescriptionId": medication.id,
                                        "receiverName": $scope.receiverName,
                                        "receiverContactDetails": $scope.receiverContact,
                                        "relationshipId": $scope.relation.id,
                                        "visitTypeId": $state.params.patientDetailsObj.visitTypeId,
                                        "prescriptionDetailsList": []
                                    }
                                    angular.forEach(medication.items, function(item){
                                        var medicationItem = {
                                            "prescriptionDetailId": item.id,
                                            "itemId": item.itemId,
                                            "handOverQty": item.handoverQty
                                        }
                                        data.prescriptionDetailsList.push(medicationItem);
                                    });
                                    $rootScope.startLoader();
                                    GenericService.serviceAction("POST", URI, data).then(function (response) {
                                        growl.success(response.data.message);
                                        $('#medicationDetailsModal').modal('hide');
                                       
                                        $scope.getDisMedicationHandoverData = [];
                                        $scope.searchDisMedicationHandover();
                                        $rootScope.stopLoader();
                                    },
                                    function (err) {
                                        $rootScope.stopLoader();
                                    });
                                }
                            }

						} ]);
