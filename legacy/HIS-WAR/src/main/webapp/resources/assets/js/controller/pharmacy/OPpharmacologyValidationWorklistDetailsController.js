(function () {
    "use strict";

    function OPpharmacologyValidationWorklistDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, pharmacologyValidationService, StatusService) {
        var self = this;
        

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isSaveValid : false
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            $scope.saveFlag = true;
            self.model.pharmacologyValidationDetails = pharmacologyValidationDetails;
            self.model.IsValidateToggle = IsValidateToggle;
            self.model.Validation = Validation;
            self.model.SavePopup = SavePopup;
            self.model.SaveButtonValidation = SaveButtonValidation;
            self.model.SavePharmacologyValidationWorkList = SavePharmacologyValidationWorkList;
            self.model.NavigateToPharmacologicalValidation = NavigateToPharmacologicalValidation;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            pharmacologyValidationDetails(pharmacologyValidationService.getPharmacologyValidationId());
        }    

        var pharmacologyValidationDetails = function (itemId) {

            var URI = CONSTANTS.PHARMACY.OP.PHARMACOLOGICAL_VALIDATION.DETAIL_SEARCH_API + "/" + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.pharmacologyValidationDetailsList = response.data;

                    var intervention = [];
                    angular.forEach(self.model.pharmacologyValidationDetailsList.items, function (item, key) {
                        intervention.push(item.intervention);
                        if(item.status == 'Pending' || item.status == "Cancelled" ){
                            item.itemDisabled = true;
                        }else{
                            $scope.saveFlag = false;
                        }
                    });
                    if (intervention.indexOf(true) !== -1) {
                        self.model.isValidateValid = true;
                    } else {
                        self.model.isValidateValid = false;
                    }
                    $rootScope.stopLoader();
                });
        }

        var IsValidateToggle = function (bill) {
            var Interventions = [];
            angular.forEach(bill.items, function (item, key) {
                if (item.intervention) {
                    self.model.isValidateValid = true;
                    self.model.validate = false;
                } else {
                    self.model.isValidateValid = false;
                }
                Interventions.push(item.intervention);
            });

            var index = Interventions.indexOf(true);
            if (index !== -1) {
                //  if true 
                self.model.isValidateValid = true;
                self.model.validate = false;
            }

        }

        var Validation = function (bill) {
            var isValid = true;
            var isChecked = false;
           
            bill.items.forEach(function (item) {
                item['isinterventionRemarkValid'] = false;
                
                if (item.intervention && !isChecked){ 
                    isChecked = true;
                }

                if(item.intervention && (item.interventionRemark == undefined || item.interventionRemark == '')) { 
                    isValid = false;
                    item['isinterventionRemarkValid'] = true;
                    growl.error("Remark is required for intervention items");
                }

            });
            
            return isValid;
        }

        var ClearFields = function (itemList){
            itemList.Id = "1" + (new Date()).getTime();
            itemList.uhid = null;
            itemList.patientDetail = null;
            itemList.wardName = null;
            itemList.pdd = null;
            itemList.doa = null;
            itemList.bedNo = null;
            itemList.doctorDetail = null;
            self.model.pharmacologyValidationDetailsList = null;
            self.model.interventionRemark = null;
            self.model.validate = null;
            itemList.items = [];
        }

        var SetStatus = function (x){
            var status = self.model.Status.find(function(obj){
              return obj.status == x;
            });
            
            return status.id;
        }

        var SaveButtonValidation = function (bill) {
            var isValid = true;
                if(!!bill){
                bill.items.find(function (item) {
                    if (!!item.intervention || !!self.model.validate) {
                        isValid = false;
                    }
                });
            }
            return isValid;
        }

        var SavePharmacologyValidationWorkList = function (itemList) {
            
            if (Validation(itemList)) {
                var URI = CONSTANTS.PHARMACY.OP.PHARMACOLOGICAL_VALIDATION.SAVE_API;
                var statusId;
                var itemStatusId;

                if(self.model.validate){
                    statusId = SetStatus('Pending');
                    itemStatusId = SetStatus('Pending');
                }else{
                    statusId = SetStatus('Intervention');
                    itemStatusId = SetStatus('Intervention');
                }

                var data = {
                    "id": !!itemList.id ? itemList.id : undefined,
                    "validate": !!self.model.validate ?  self.model.validate : undefined,
                    "statusId": !!statusId ? statusId : undefined,
                    "admissionId": !!itemList.admissionId ? itemList.admissionId : undefined,
                    "doctorId" : !!itemList.doctorId ? itemList.doctorId : undefined,
                    "visitType" : !!itemList.visitType ? itemList.visitType : undefined,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                }

                angular.forEach(itemList.items, function (item, key) {
                   
                    if(item.statusId !=9){
	                	var item = {
	
	                        "id": item.id,
	                        "itemStatusId": !!itemStatusId ? itemStatusId : undefined,
	                        "intervention": !!item.intervention ? item.intervention : undefined,
	                        "remark": !!item.interventionRemark ? item.interventionRemark : undefined
	                    }
	                    data.items.push(item);
                    }
                });

            //   console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        ClearFields(itemList);
                        $rootScope.stopLoader();
                        self.model.NavigateToPharmacologicalValidation();
                        growl.success('Pharmacology validation worklist  successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving pharmacology validation worklist ');
                    });


            }
        }

        var NavigateToPharmacologicalValidation = function () {
            $state.go('OPpharmacologyValidationWorklist');
          }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

          //Method to get the list of Generic 
    var GetGenericList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.genericList = response.data;
            GetGenericMaster();
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }
      GetGenericList();
  
      var genericMasterList = [];
      var GetGenericMaster = function () {
        var arraylist = self.model.genericList.filter(x => x.status == true);
        for (let generic of arraylist){
          genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
      } 

      var GetStrengthUnitList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.strengthUnitList = response.data;
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            },
            function errorCallback(response) {
              console.log("Error");
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            });
      }
      GetStrengthUnitList();
  
      var ViewCombinationDetails = function(item) {
        self.model.genericCombinationList = [];
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            for(let combination of response.data) {
              let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
              combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
              combination.code = item.code;
              combination.genericName = item.name;
            }
            self.model.strengthUnitList.forEach(function (strength){
                response.data.forEach(function (data){
                  if(data.strength == strength.id){
                    $scope.strength = strength.unit;
                    var obj = {
                      strength: "",
                      code:"",
                      genericId: "",
                      genericName: "",
                      id:"",
                      name:"",
                      strengthUnit:"",
                      subGenericId: "",
                    }
                    obj.strength = $scope.strength;
                    obj.code = data.code;
                    obj.genericId = data.genericId;
                    obj.genericName = data.genericName;
                    obj.id = data.id;
                    obj.name = data.name;
                    obj.strengthUnit = data.strengthUnit;
                    obj.subGenericId = data.subGenericId;
                    self.model.genericCombinationList.push(obj);
                  }
                });
              });
            var popup = angular.element("#combinationDetails");
            popup.modal("show");
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("OPpharmacologyValidationWorklistDetails", {
            url: "/OPpharmacologyValidationWorklistDetails",
            templateUrl: 'views/PharmacyInventory/InPatient/op-pharmacology-validation-worklist-details.html',
            controller: "OPPharmacologyValidationWorklistDetails.Controller",
            controllerAs: "vm",
            title: 'Pharmacology Validation Worklist'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("OPPharmacologyValidationWorklistDetails.Controller", OPpharmacologyValidationWorklistDetailsController)
})();