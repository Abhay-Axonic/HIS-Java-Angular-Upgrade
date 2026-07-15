(function () {
    "use strict";

    function detailsAcceptanceOfEmptyAmpoulesController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, GenericServiceParamHeader,$filter, $state, growl, CurrencyService, StatusService, commonDetailService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


            self.model = {
                SelectedRequestItems: []
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.GetAccepatnceOfEmptyAmpoulesDetails = GetAccepatnceOfEmptyAmpoulesDetails;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.SaveReturnEmptyAmpoules = SaveReturnEmptyAmpoules;
            self.model.ClearFields = ClearFields;
            self.model.NavigateToAcceptanceOfEmptyAmpoules = NavigateToAcceptanceOfEmptyAmpoules;
            self.model.checkQuantity = checkQuantity;
            self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
            GetAccepatnceOfEmptyAmpoulesDetails(commonDetailService.getDataId());
        }

        var GetAccepatnceOfEmptyAmpoulesDetails = function (itemId) {
            var URI = CONSTANTS.PHARMACY.NARCOTICS.ACCEPTANCE_EMPTY_AMPOULES.DETAIL_API  + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.emptyAmpoulesDetails = response.data;
                    self.model.statusId1=response.data.statusId;
                    $rootScope.allergydata(response.data);
                    getAllPatientDiagnosisForIcon(response.data.patientId);
                    $rootScope.stopLoader();
                });
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.emptyAmpoulesDetails.items.forEach(function (item) {
                    item.isSelected = true;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.emptyAmpoulesDetails.items.forEach(function (item) {
                    item.isSelected = false;
                });
            }
            // console.log(self.model.SelectedRequestItems);
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectRequestItem = function (item) {

            var itemIds = self.model.SelectedRequestItems;

            if (item.isSelected) {
                itemIds.push(item);
            } else {
                var index = itemIds.indexOf(item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.emptyAmpoulesDetails.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        //-----------//
        var checkQuantity= function(){


            var isValid = true;

//            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
//                isValid = false;
//                growl.error("No items in the list to save");
//            } else {
                self.model.SelectedRequestItems.forEach(function (billitem) {

                    billitem['isQuantityValid'] = false;
                    self.model.statusId=8;
//                    if (billitem.quantityIssued > billitem.returedQuantity) {
                    if (billitem.acceptedQuantity!=undefined && billitem.acceptedQuantity==billitem.returedQuantity) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('No enough quantity.')
                        
                    }
                    else if((parseInt(billitem.returedQuantity)-parseInt(billitem.acceptedQuantity))==0){
                        billitem['isQuantityValid'] = true;
                     isValid = false;
                     growl.error('No enough available returned quantity.') 
                    }
                    else if (billitem.acceptedQuantity!=undefined && ((parseInt(billitem.returedQuantity)-parseInt(billitem.acceptedQuantity))<(parseInt(billitem.returnQuantity)))) {

                        billitem['isQuantityValid'] = true;
                        billitem['availableQty'] = (parseInt(billitem.returedQuantity)-parseInt(billitem.acceptedQuantity));
                        isValid = false;
                        growl.warning('Entered value is greater than available returned quantity.')
                        
                    }
                    else if (billitem.returedQuantity < billitem.returnQuantity) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Please enter value equal to returned quantity.')
                        
                    }else if (billitem.returedQuantity < billitem.returnQuantity) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Please enter value equal to returned quantity.')
                    }
                });
//            }

            return isValid;
        
        };
        //-----------//
        
       /* var ValidateBill = function (bill) {

            var isValid = true;

            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("No items in the list to save");
            } else {
                self.model.SelectedRequestItems.forEach(function (billitem) {

                    billitem['isQuantityValid'] = false;

                    if ((!billitem.returnQuantity) || (billitem.returnQuantity > billitem.returedQuantity)) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Enter valid quantity')
                    }else if(self.model.statusId1==3){
                    	 billitem['isQuantityValid'] = true;
                         isValid = true;
                         self.model.statusId=3;
                    }
                });
            }

            return isValid;
        


            var isValid = true;

            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("No items in the list to save");
            } else {
            	$scope.totalIssuedQuantity=0;
				$scope.totalAcceptedQuantity=0;
				$scope.totalReturnQuantity=0;
//                self.model.SelectedRequestItems.forEach(function (billitem) {
            	bill.items.forEach(function (billitem) {
            		
            		var acceptedQuantityNew=billitem.acceptedQuantity!=undefined?billitem.acceptedQuantity:0;
					var issuedQtyNew=billitem.quantityIssued;
					$scope.totalAcceptedQuantity += parseInt(acceptedQuantityNew);
					$scope.totalIssuedQuantity += parseInt(issuedQtyNew);
					
                    billitem['isQuantityValid'] = false;
                    if(billitem.isSelected==true){
                    	var returnQtyNew=billitem.returnQuantity;
						$scope.totalReturnQuantity +=parseInt(returnQtyNew);
                    	
                    if ((!billitem.returnQuantity) || (billitem.returnQuantity > billitem.returedQuantity)) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Enter valid quantity')
                    }
                    else if(billitem.returnQuantity==billitem.quantityIssued){
                   	 billitem['isQuantityValid'] = true;
                        isValid = true;
                        self.model.statusId=8;
                   }
                    else if(billitem.acceptedQuantity==undefined && billitem.acceptedQuantity!=billitem.quantityIssued){
                    	 billitem['isQuantityValid'] = true;
                         isValid = true;
                         self.model.statusId=3;
                    }
                    else if(billitem.quantityIssued > (parseInt(billitem.acceptedQuantity)+parseInt(billitem.returnQuantity))){
                      	 billitem['isQuantityValid'] = true;
                           isValid = true;
                           self.model.statusId=3;
                      }
                    else if((parseInt(billitem.acceptedQuantity)+parseInt(billitem.returnQuantity))==billitem.quantityIssued){
                    	 billitem['isQuantityValid'] = true;
                         isValid = true;
                         self.model.statusId=8;
                    }
            	}
                });
            }
            if(($scope.totalIssuedQuantity == $scope.totalAcceptedQuantity)|| ($scope.totalIssuedQuantity ==$scope.totalReturnQuantity) ||
            		($scope.totalIssuedQuantity ==(parseInt($scope.totalReturnQuantity)+ parseInt($scope.totalAcceptedQuantity)))){
				self.model.statusIdNew=8;
            }else{
            	self.model.statusIdNew=3;
            }
            return isValid;
            }*/
        
        
        
        var ValidateBill = function (bill) {/*

            var isValid = true;

            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("No items in the list to save");
            } else {
                self.model.SelectedRequestItems.forEach(function (billitem) {

                    billitem['isQuantityValid'] = false;

                    if ((!billitem.returnQuantity) || (billitem.returnQuantity > billitem.returedQuantity)) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Enter valid quantity')
                    }else if(self.model.statusId1==3){
                    	 billitem['isQuantityValid'] = true;
                         isValid = true;
                         self.model.statusId=3;
                    }
                });
            }

            return isValid;
        */


            var isValid = true;

            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("No items in the list to save");
            } else {
            	$scope.totalReturnedNewQuantity=0;
				$scope.totalAcceptedQuantity=0;
				$scope.totalReturnQuantity=0;
//                self.model.SelectedRequestItems.forEach(function (billitem) {
            	bill.items.forEach(function (billitem) {
            		
            		var acceptedQuantityNew=billitem.acceptedQuantity!=undefined?billitem.acceptedQuantity:0;
					var issuedQtyNew=billitem.returedQuantity;
					$scope.totalAcceptedQuantity += parseInt(acceptedQuantityNew);
					$scope.totalReturnedNewQuantity += parseInt(issuedQtyNew);
					
                    billitem['isQuantityValid'] = false;
                    if(billitem.isSelected==true){
                    	var returnQtyNew=billitem.returnQuantity;
						$scope.totalReturnQuantity +=parseInt(returnQtyNew);
                    	
                    if ((!billitem.returnQuantity) || (billitem.returnQuantity > billitem.returedQuantity)) {

                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Enter valid quantity')
                    }
                    else if(billitem.returnQuantity==billitem.returedQuantity){
                   	 billitem['isQuantityValid'] = true;
                        isValid = true;
                        self.model.statusId=8;
                   }
                    else if(billitem.acceptedQuantity==undefined && billitem.acceptedQuantity!=billitem.returedQuantity){
                    	 billitem['isQuantityValid'] = true;
                         isValid = true;
                         self.model.statusId=3;
                    }
                    else if(billitem.returedQuantity > (parseInt(billitem.acceptedQuantity)+parseInt(billitem.returnQuantity))){
                      	 billitem['isQuantityValid'] = true;
                           isValid = true;
                           self.model.statusId=3;
                      }
                    else if((parseInt(billitem.acceptedQuantity)+parseInt(billitem.returnQuantity))==billitem.returedQuantity){
                    	 billitem['isQuantityValid'] = true;
                         isValid = true;
                         self.model.statusId=8;
                    }
            	}
                });
            }
            if(($scope.totalReturnedNewQuantity == $scope.totalAcceptedQuantity)|| ($scope.totalReturnedNewQuantity ==$scope.totalReturnQuantity) ||
            		($scope.totalReturnedNewQuantity ==(parseInt($scope.totalReturnQuantity)+ parseInt($scope.totalAcceptedQuantity)))){
				self.model.statusIdNew=8;
            }else{
            	self.model.statusIdNew=3;
            }
            return isValid;
            }
        
        
        
        var SaveReturnEmptyAmpoules = function (bill) {
        	checkQuantity();
            if (ValidateBill(bill)) {
                var URI = CONSTANTS.PHARMACY.NARCOTICS.ACCEPTANCE_EMPTY_AMPOULES.SAVE_API;

                var data = {
                    "id": !!bill.id ? bill.id : undefined,
                    "statusId" : self.model.statusIdNew,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId

                }
                angular.forEach(bill.items, function (item, key) {
                    if (item.isSelected) {
                        var returnItem = {
                            "id": item.id,
                            /*"returnQuantity": Number(item.returnQuantity),
                            "pendingQuantity": Number(item.pendingQuantity),*/
                            "quantityAccepted": item.acceptedQuantity==null || item.acceptedQuantity==undefined ? Number(item.returnQuantity):Number(item.returnQuantity)+Number(item.acceptedQuantity),
                            "quantityReturned": Number(item.pendingQuantity),
                           // "quantityRejected" :   Number(item.returedQuantity) -Number(item.returnQuantity),
                            "quantityRejected" :   Number(item.returedQuantity==undefined ? item.quantityIssued : item.returedQuantity) -Number(item.returnQuantity),
                            "statusId" : self.model.statusId,
                        }
                        data.items.push(returnItem);
                    }
                });

                console.log(data)

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        ClearFields(bill);
                        growl.success('Acceptance of empty ampoules Successfully saved');
                        $rootScope.stopLoader();
                        NavigateToAcceptanceOfEmptyAmpoules();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving acceptance of empty ampoules');
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

        var NavigateToAcceptanceOfEmptyAmpoules = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('acceptanceOfEmptyAmpoules');
            }, 1000);
        }

        var ClearFields = function (bill) {

            bill.Id = "1" + (new Date()).getTime();
            bill.uhid = null;
            bill.patientDetail = null;
            bill.dob = null;
            bill.pdd = null;
            bill.doa = null;
            bill.wardName = null;
            bill.bedNo = null;
            bill.prescriptionDate = null;
            bill.doctorDetail = null;
            bill.prescriptionId = null;

            bill.items = [];
        }

        


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsAcceptanceOfEmptyAmpoules", {
            url: "/detailsAcceptanceOfEmptyAmpoules",
            templateUrl: 'views/PharmacyInventory/NarcoticsStore/details-acceptance-of-empty-ampoules.html',
            controller: "DetailsAcceptanceOfEmptyAmpoules.Controller",
            controllerAs: "vm",
            title: 'Acceptance of Empty Ampoules'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsAcceptanceOfEmptyAmpoules.Controller", detailsAcceptanceOfEmptyAmpoulesController)
})();