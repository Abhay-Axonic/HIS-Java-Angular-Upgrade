(function () {
    "use strict";

    function PatientOrdersWorkbench($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,PagerService,BillingGenericService,GenericServiceFileUpload,GenericServiceParamHeader) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.labConfig = $scope.checkLabConfig();
            $scope.updateBtnFlag=true;

            /**Variables for pagination */
            $scope.offset = 0;
            $scope.noOfRecordsPerPage = 10;
            $scope.unitDeptMasterList = [];
            $scope.totalNoOfRecords;
            $scope.searchKeyword;
            $scope.commonListCount;
            $scope.pager = {};
            $scope.page;

            $scope.time = new Date();

            $scope.pathlogyTestDate = new Date();
            $scope.pathlogyTestDatePopup = {
                opened: false
            };

            $scope.openDateTh = function (order) {
                angular.forEach($scope.currentPathologyEncounterOrderLists, function (o) {
                    o.opened = false;
                });
                order.opened = true;
            };

            $scope.openDateImage = function (order) {
                angular.forEach($scope.currentImageTestEncounterOrderLists, function (o) {
                    o.opened = false;
                });
                order.opened = true;
            };
            $scope.openDateCross = function (details) {
                angular.forEach($scope.consultOrdersList, function (o) {
                    o.opened = false;
                });
                details.opened = true;
            };

            $scope.openDateMedication = function (details) {
                            angular.forEach($scope.detailedMedications, function (o) {
                                o.opened = false;
                            });
                            details.opened = true;
            };


            $scope.openDateBedSide = function (details) {
                            angular.forEach($scope.currentBedSideEncounterOrderLists, function (o) {
                                o.opened = false;
                            });
                            details.opened = true;
            };

            $scope.openDateAddmision = function (details) {
                            angular.forEach($scope.opdAdmissionDetails, function (o) {
                                o.opened = false;
                            });
                            details.opened = true;
            };

            $scope.dateOptions = {
                formatYear: 'yyyy',
                showWeeks: false,
                minDate: new Date()
            };

            $scope.pushWorkbenchObj = function (item, tabId) {
                  if (!item) return;
                  var recordId = null;
                  var workbenchAddedDate = null;
                  var workbenchEndDate = null;
                  if (tabId === 1 || tabId === 2 || tabId === 4) {
                      recordId = item.orderDetailsId;
                      workbenchAddedDate = item.workbenchAddedDate;
                  } else if (tabId === 6) {
                      recordId = item.consultDoctorId;
                      workbenchAddedDate = item.workbenchAddedDate;
                  } else if (tabId === 3) {
                      recordId = item.prescriptionId;
                      workbenchAddedDate = item.workbenchAddedDate;
                      workbenchEndDate = item.workbenchEndDate;
                  } else if (tabId === 7) {
                      recordId = item.admissionNoteId;
                      workbenchAddedDate = item.workbenchAddedDate;
                  }
                  $scope.saveObjList.push({
                      patientWorkbenchId : 0,
                      patientId          : $rootScope.patientDetails.patientId,
                      recordId           : recordId,
                      tabId              : tabId,
                      resultId           : item.resultId || 1,
                      workbenchAddedDate : workbenchAddedDate,
                      workbenchEndDate   : workbenchEndDate,
                      workbenchRemark    : item.workbenchRemark || '',
                      unitId             : $scope.unitId,
                      organizationId     : $scope.organizationId,
                      createdBy          : cookieObject.userId,
                      updatedBy          : cookieObject.userId
                  });
              };

            $scope.saveBox=function(tabId){
              $scope.saveObjList = [];
               switch (tabId) {
                   case 1: // Pathology
                       if ($scope.currentPathologyEncounterOrderLists && $scope.currentPathologyEncounterOrderLists.length > 0) {
                           angular.forEach($scope.currentPathologyEncounterOrderLists, function (item) {
                               $scope.pushWorkbenchObj(item, tabId);
                           });
                       }
                       break;

                   case 2: // Image Test / Radiology
                       if ($scope.currentImageTestEncounterOrderLists && $scope.currentImageTestEncounterOrderLists.length > 0) {
                           angular.forEach($scope.currentImageTestEncounterOrderLists, function (item) {
                               $scope.pushWorkbenchObj(item, tabId);
                           });
                       }
                       break;

                   case 3: //Medications
                       if ($scope.detailedMedications && $scope.detailedMedications.length > 0) {
                           angular.forEach($scope.detailedMedications, function (item) {
                               $scope.pushWorkbenchObj(item, tabId);
                           });
                       }
                       break;

                   case 4: // Bed Side Orders
                       if ($scope.currentBedSideEncounterOrderLists && $scope.currentBedSideEncounterOrderLists.length > 0) {
                           angular.forEach($scope.currentBedSideEncounterOrderLists, function (item) {
                               $scope.pushWorkbenchObj(item, tabId);
                           });
                       }
                       break;

                   case 6: // Cross Referrals
                       if ($scope.consultOrdersList && $scope.consultOrdersList.length > 0) {
                           angular.forEach($scope.consultOrdersList, function (item) {
                               $scope.pushWorkbenchObj(item, tabId);
                           });
                       }
                       break;

                   case 7: // Admission
                       if ($scope.opdAdmissionDetails && $scope.opdAdmissionDetails.length > 0) {
                           angular.forEach($scope.opdAdmissionDetails, function (item) {
                               $scope.pushWorkbenchObj(item, tabId);
                           });
                       }
                       break;

                   default:
                       console.warn("Unsupported tabId:", tabId);
               }
                var URI = BASE_URL + ROOT_URL+ "/adt/savePatientOrdersWorkbench";
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, $scope.saveObjList).then(function (response) {
                    if (response.data.status == 'success'){
                        growl.success('Saved successfully',
                            {
                                title : 'Success!'
                            });
                    }
                    else if(response.data.status =='error'){
                        growl.error('Something went wrong',
                            {
                                title : 'Error!'
                            });
                    }
                    $rootScope.stopLoader();
                });
            }


            $scope.getBox=function(){
                var getObj={
                    unitId:$scope.unitId,
                    organizationId :$scope.organizationId,
                    page :$scope.offset,
                    size:$scope.noOfRecordsPerPage
                }

                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI2, getObj).then(function (response) {
                    $scope.getMrd=[];
                    $scope.getMrd=response.data.listObject;
                    $scope.commonListCount = response.data.object;
                    $scope.setNoOfRecords();
                    $scope.setPage(1, false);
                    console.log(response);
                    $rootScope.stopLoader();
                });
            }
            $scope.getBox();


            $scope.setNoOfRecords = function() {
                $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
            };

            $scope.initBoxGroupMasterList = function(offset, noOfRecordsPerPage){
                var obj1 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId,
                    page        :offset != null ? offset : 0,
                    size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
                }

                var obj2 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId
                }
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                    $scope.getMrd=[];
                    $scope.getMrd=response.data.listObject;
                    $scope.unitDeptMasterList = response.data.listObject;
                    $scope.commonListCount = response.data.object;
                    $scope.setPage(1, false);
                    console.log(response);
                    $rootScope.stopLoader();
                });
            }


            $scope.masterList = function(offset, noOfRecordsPerPage) {
                var obj1 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId,
                    page        :offset != null ? offset : 0,
                    size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
                }

                var obj2 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId
                }
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                    $scope.getMrd=[];
                    $scope.getMrd=response.data.listObject;
                    $scope.unitDeptMasterList = response.data.listObject;
                    $scope.commonListCount = response.data.object;
                    //$scope.setPage(1, false);
                    console.log(response);
                    $rootScope.stopLoader();
                });
            }




            $scope.setPage = function(page, flag) {
                if (page < 1 || page > $scope.pager.totalPages)
                {
                    return;
                }
                $scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
                if (flag)
                {
                    $scope.masterList($scope.pager.startIndex, $scope.pager.pageSize);
                }
            }
            $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);

            $scope.viewList = [];
            $scope.viewBtn=function (data){
                if(data.status=='I'){
                    growl.error("Please update status then you can edit.",
                        {
                            title : 'Error!'
                        });
                    return false;
                }
                console.log(data)
                var URI =  BASE_URL+ROOT_URL+"/mrdbox/getMrdBoxById/"+data.boxId+"/"+$scope.organizationId+"/"+$scope.unitId;
                GenericService.serviceAction("GET", URI,{}).then(
                    function (response) {
                        $scope.viewList=response;
                        console.log($scope.viewList)
                        $scope.boxID=response.data.object.boxId;
                        $scope.boxCode=response.data.object.boxCode;
                        $scope.boxDescription=response.data.object.boxDescription;
                        $scope.saveBtnFlag=true;
                        $scope.updateBtnFlag=false;
                    });
            }

            $scope.showUpdateBtn=function(value){
                var objUpdate ={
                    boxId :$scope.boxID,
                    boxCode: $scope.boxCode,
                    boxDescription:$scope.boxDescription,
                    status:'A',
                    organizationId :$scope.organizationId,
                    unitId :$scope.unitId,
                    updatedBy :$scope.userId,
                    createdBy :$scope.userId,
                }
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/updateMrdBox";
                GenericService.serviceAction("PUT", URI2, objUpdate).then(function (response) {
                    if (response.data.status == 'success'){
                        growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                    }
                    $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                    $scope.clearFunction();
                    $scope.getBox();
                    $scope.saveBtnFlag=false;
                    $scope.updateBtnFlag=true
                    console.log(response);
                });

            }

            $scope.updateStatus = function(boxId,boxCode,boxDescription,id, status){
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/updateMrdBoxStatus";
                $scope.currentDate = new Date();
                var objStatus = {
                    boxId :boxId,
                    boxCode: boxCode,
                    boxDescription:boxDescription,
                    status:status,
                    organizationId :$scope.organizationId,
                    unitId :$scope.unitId,
                    updatedBy :$scope.userId,
                    createdBy :$scope.userId,
                }
                GenericService.serviceAction("PUT", URI2, objStatus).then(function (response) {
                    if (response.data.status == 'success'){
                        growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                    }
                    $scope.clearFunction();
                    $scope.getBox();
                    console.log(response);
                });
            }

            $scope.resultOptions = [
                { id: 1, label: 'Normal' },
                { id: 2, label: 'Abnormal' }
            ];

            $scope.searchPatient = function(){
                  var commonObj = {
                            unitId : $scope.unitId,
                            organizationId : $scope.organizationId,
                  };
                  $rootScope.startLoader();
                  var URI = BASE_URL + ROOT_URL + "/adt/getListOfERPatients";
                  GenericService.serviceAction("POST", URI, commonObj).then(
                            function(response) {
                                $scope.erAdmittedPatientList= [];
                                if (response.data.status == "success") {
                                    $rootScope.stopLoader();
                                    $scope.erAdmittedPatientList = response.data.listObject;
                                }
                            });
            }
            $scope.searchPatient();
            $scope.getInvetigation= function(patient){
                    $rootScope.patientDetails = patient;
                    $rootScope.patientDetails.patientName = patient.pFirstName;
                    $rootScope.patientDetails.dateOfAdmission = patient.doa;
                    $rootScope.patientDetails.patientDischargeDate =patient.pdd;
                    $rootScope.patientDetails.uhIdNumber = patient.uhidnumber;
                    $rootScope.patientDetails.age = patient.ageStr;
                    $rootScope.startLoader();
                    var data={};
                    var paramObj={};
                    var labConfig =  $scope.labConfig.labSpecialityId;
                    var headers = {
                            'patientId' : patient.patientId,
                            'flag' : false,
                            'Content-Type' : 'application/json',
                            'unitId':$scope.unitId
                        };
                    var URI = BASE_URL + ROOT_URL +"/api/opd/getInvestigationsListForCoversheet";
                    GenericServiceParamHeader.serviceAction("GET",URI,data,false, paramObj,headers).then(
                            function(response) {
                                if (response.data.status == "success") {
                                    $scope.mainCurrentEncounterOrderLists = response.data.listObject;
                                         $scope.currentPathologyEncounterOrderLists = [];
                                         $scope.currentImageTestEncounterOrderLists = [];
                                         $scope.currentBedSideEncounterOrderLists = [];
                                         angular.forEach($scope.mainCurrentEncounterOrderLists, function (value, index) {
                                             var createdDate1 = moment(value.createdDate, 'DD-MM-YYYY HH:mm')
                                                                     .format('DD-MMM-YYYY HH:mm');
                                             var orderSchDate1 = moment(value.orderSchDate, 'DD-MM-YYYY')
                                                                     .format('DD-MMM-YYYY');
                                             var sampleGenDatetime1 = moment(value.sampleGenDatetime, 'DD-MM-YYYY HH:mm')
                                                                     .format('DD-MMM-YYYY HH:mm');
                                             var ordRenderDatetime1 = moment(value.ordRenderDatetime)
                                                   .format('DD/MM/YYYY HH:mm');

                                             value.createdDate1 = (createdDate1 === 'Invalid date') ? '' : createdDate1;
                                             value.orderSchDate1 = (orderSchDate1 === 'Invalid date') ? '' : orderSchDate1;
                                             value.sampleGenDatetime1 = (sampleGenDatetime1 === 'Invalid date') ? '' : sampleGenDatetime1;
                                             value.ordRenderDatetime = (ordRenderDatetime1 === 'Invalid date') ? '' : ordRenderDatetime1;
                                             value.workbenchAddedDate = value.workbenchAddedDate == null ? new Date() : value.workbenchAddedDate;
                                             value.resultId = (value.resultId === 1 || value.resultId === 2) ? value.resultId : 1;
                                             value.opened = false;
                                             if(value.specialityId == labConfig && value.isPoc == 'N') {
                                                 $scope.currentPathologyEncounterOrderLists.push(value);
                                             }
                                             if(value.specialityId != labConfig &&value.isPoc == 'N') {
                                                 $scope.currentImageTestEncounterOrderLists.push(value);
                                             }
                                             if(value.isPoc != 'N'){
                                                 $scope.currentBedSideEncounterOrderLists.push(value);
                                             }
                                         });
                                         console.log("Pathology Orders:", $scope.currentPathologyEncounterOrderLists);
                                         console.log("Image Orders:", $scope.currentImageTestEncounterOrderLists);
                                    $rootScope.stopLoader();
                                }
                    });
            }

            $scope.getDetailedMedication = function() {
                     $scope.detailedMedications = [];
                     var data={};
                     var paramObj={};
                     var headerObj = {
                                unitId : $scope.unitId,
                                organizationId : $scope.organizationId
                     };
                     var paramObjForPer = {
                                patientId : $rootScope.patientDetails.patientId,
                                admissionId : $rootScope.patientDetails.admissionId,
                                doctorId : $rootScope.patientDetails.doctorId
                     };
                    var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsByDoctorId";
                    GenericServiceParamHeader.serviceAction("GET", URI, data,false,paramObjForPer,headerObj).then(
                            function(response) {
                                if (response.data.status == "success") {
                                    $scope.detailedMedications = response.data.listObject;
                                    angular.forEach($scope.detailedMedications,function(value,index) {
                                            var addedDate1 = moment(value.addedDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
                                            var startDate1 = moment(value.startDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
                                            var endDate1 = moment(value.endDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
                                            if(addedDate1=='Invalid date')
                                                {
                                                addedDate1="";
                                                }else
                                                    {
                                                    addedDate1=addedDate1;
                                                    }
                                            if(startDate1=='Invalid date')
                                            {
                                                startDate1="";
                                            }else
                                                {
                                                startDate1=startDate1;
                                                }
                                            if(endDate1=='Invalid date')
                                            {
                                                endDate1="";
                                            }else
                                                {
                                                endDate1=endDate1;
                                                }
                                            value["addedDate1"]=addedDate1;
                                            value["startDate1"]=startDate1;
                                            value["endDate1"]=endDate1;
                                            value.workbenchAddedDate = value.workbenchAddedDate == null ? new Date() : value.workbenchAddedDate;
                                            value.workbenchEndDate = value.workbenchEndDate == null ? new Date() : value.workbenchEndDate;
                                            });
                                }
                            });
            }

            $scope.getOPDAdmissionDetails = function()  {
                    $scope.opdAdmissionDetails = [];
                    var data={
                            organizationId : $scope.organizationId,
                            visitTypeId : 2,
                            admissionId : $rootScope.patientDetails.admissionId,
                            unitId : $scope.unitId,
                            patientId : $rootScope.patientDetails.patientId,
                    };
                    var URI = BASE_URL + ROOT_URL + "/adt/getDayCareAdmissionRequestDetails";
                    GenericServiceParamHeader.serviceAction("POST", URI, data)
                        .then(
                            function(response) {
                                if (response.data.status == "success") {
                                    $scope.opdAdmissionDetails = response.data.listObject;

                                    angular.forEach($scope.opdAdmissionDetails,function(value,index) {
                                          value.workbenchAddedDate = value.workbenchAddedDate == null ? new Date() : value.workbenchAddedDate;
                                    })
                                    console.log("admissionlist",$scope.opdAdmissionDetails);
                                }
                            });
                }

            $scope.consultOrderList = function()  {
                    var data = {
                            admissionId: $rootScope.patientDetails.admissionId,
                            unitId : $scope.unitId,
                            patientId : $rootScope.patientDetails.patientId,
                            organizationId : $scope.organizationId,
                        }
                    var URI = BASE_URL + ROOT_URL + "/api/opd/consultDoctorListByAdmission";
                	GenericServiceParamHeader.serviceAction("POST",URI,data,false)
                            .then(function(response) {
                                if(response.data.status == "success"){
                                    $scope.consultOrdersList = response.data.listObject;
                                    angular.forEach($scope.consultOrdersList, function (order) {
                                        order.opened = false;
                                        order.workbenchAddedDate = order.workbenchAddedDate == null ? new Date() : order.workbenchAddedDate;
                                    });
                                    console.log("consultOrdersListresulttt", $scope.consultOrdersList);
                                }
                            });
            }
        }


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("patientOrdersWorkbench", {
            url: "/patientOrdersWorkbench",
            title: 'ER Patient Orders Workbench',
            templateUrl: 'views/AdmissionDischarge/ER/patient-orders-workbench.html',
            controller: "PatientOrdersWorkbench.Controller",
            resolve : {
                data: function($q, $state, $timeout) {
                    var deferred = $q.defer();
                    $timeout(function() {
                        var arr = JSON.parse(localStorage.getItem('permissions'))
                            .modules.filter(function(item){
                                return (item.id == 1069);
                            });
                        if(arr.length <= 0){
                            $state.go("accessDenied")
                            deferred.reject();
                        } else {
                            deferred.resolve();
                        }
                    });

                    return deferred.promise;
                }
            },
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("PatientOrdersWorkbench.Controller", PatientOrdersWorkbench);
})();