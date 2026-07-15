(function () {
    "use strict";

    function pendingHandoverWorklistDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,GenericServiceParamHeader, $filter, $state, growl, CurrencyService, detailService, StatusService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                receiverDisabled : false,
                comorbidityFlag :false,
                notifiableFlag : false,
            };
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            
            self.model = {
                carrierList: [{
                    "carrierName": "Select carrier"
                }],
            };

            //Methods
            // self.model.GetPendingHandoverWorklistDetails = GetPendingHandoverWorklistDetails;
            self.model.SavePopup = SavePopup;
            self.model.SavePendingHandoverWorkList = SavePendingHandoverWorkList;
            self.model.NavigateToPendingHandoverWorkList = NavigateToPendingHandoverWorkList;
            self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
            self.model.RemovePopup = RemovePopup;

            GetPendingHandoverWorklistDetails(detailService.getId());
        }

        var GetPendingHandoverWorklistDetails = function (itemId) {

            var URI = CONSTANTS.PHARMACY.IP.PENDING_HANDOVER.DETAIL_API + $scope.unitId + '/'+ itemId;

            $rootScope.startLoader();
            self.model.carrier = self.model.carrierList[0];
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	$rootScope.allergydata(response.data);
                    getAllPatientDiagnosisForIcon(response.data.patientId);
                    self.model.carrierList = self.model.carrierList.concat(response.data.carrier);
                    self.model.pendingHandoverWorklistDetailsList = response.data;
                    self.model.receiverName = self.model.pendingHandoverWorklistDetailsList.receiverName
                    if(!!self.model.pendingHandoverWorklistDetailsList.receiverName){
                        self.model.receiverDisabled = true;
                    }
                    $rootScope.stopLoader();

                });
        }


        var Validation = function (itemList) {
            var isValid = true;

            if (!itemList.items || itemList.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            }
            else {
                if(self.model.carrier.id == undefined || self.model.carrier.id == ''){
                    isValid = false;
                    self.model.isReceiverValid = true;
//                    if(data.receiverName==null){
                    	growl.warning(" Please enter receiver name. ",
    							{
    								title : 'warning!'
    							});
//                    	$("#confirmSave").hide();
//                    }
                }
            }
            return isValid;
        }

        var ClearFields = function (itemList) {

            itemList.uhid = null;
            itemList.doa = null;
            itemList.patientDetail = null;
            itemList.doctorDetail = null;
            itemList.pdd = null;
            itemList.dob = null;
            itemList.wardName = null;
            itemList.bedNo = null;
            self.model.receiverName = null;
            itemList.items = [];
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var SavePendingHandoverWorkList = function (itemList) {


            if (Validation(itemList)) {
                var URI = CONSTANTS.PHARMACY.IP.PENDING_HANDOVER.SAVE_API;

                var data = {
                    "id": !!itemList.id ? itemList.id : undefined,
                    "statusId": SetStatus('Completed'),
                    "receiverName": self.model.carrier.carrierName,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId,
                    "carrierId":self.model.carrier.id
                    // "items": []
                }

                // angular.forEach(itemList.items, function (item, key) {
                //     var item = {

                //         "saleDetailId": item.id,
                //         "receiverName": self.model.receiver,
                //     }
                //     data.items.push(item);
                // });

                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                      
                        $rootScope.stopLoader();
                        NavigateToPendingHandoverWorkList();
                        growl.success('Pending Handover WorkList successfully saved');
                        ClearFields(itemList);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving pending handover worklist ');
                    });


            }
        }

        
        var getAllPatientDiagnosisForIcon= function(patientId){
            self.model.patientDiagnosisListIcon =[];
            var cookieObject = $cookies.getObject('cookieObject');
             var headerObjDia = {
                         'unit_id' : cookieObject.unitId,
                        'organization_id' : cookieObject.organizationId,
                        'patient_id' : patientId,
                       'Content-Type':'application/json',
             };
             var data={};
             var URI = CONSTANTS.ROOT_URL
             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
             GenericServiceParamHeader
             .serviceAction(METHOD_GET, URI,
                data,
                NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
             .then(
                     function(response) {
                         console.log(response);
                         // $rootScope.startLoader();
                         if (response.data.status == "success")
                         self.model.patientDiagnosisListIcon = response.data.listObject; 
                         console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                             self.model.comorbidityFlag = false;
                             self.model.notifiableFlag = false;
                            
                                if(self.model.patientDiagnosisListIcon.length > 0)
                                    {
                                        self.model.complaintDelete=false;
                                        angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                            if(value.isNotifiable == 'Y')
                                            {
                                                self.model.notifiableFlag = true;
                                            }
                                            else if(value.isComorbidity == 'Y')
                                            {
                                                self.model.comorbidityFlag = true;
                                            }
                                        });
                                    }
                         
                     });
            }
        
        var NavigateToPendingHandoverWorkList = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('pendingHandoverWorkList');
            }, 1000);
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var RemovePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('hide');
        }

        initializeController();
    }


    function config($stateProvider) {
        $stateProvider.state("pendingHandoverWorkListDetails", {
            url: "/pendingHandoverWorkListDetails",
            templateUrl: 'views/PharmacyInventory/Store/pending-handover-worklist-details.html',
            controller: "PendingHandoverWorklistDetails.Controller",
            controllerAs: "vm",
            title: 'Pending Handover Worklist'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("PendingHandoverWorklistDetails.Controller", pendingHandoverWorklistDetailsController)
})();
