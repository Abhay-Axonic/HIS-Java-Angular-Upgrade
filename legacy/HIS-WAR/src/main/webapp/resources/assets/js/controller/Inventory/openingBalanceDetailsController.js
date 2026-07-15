(function () {
  "use strict";
  var CACHE = {};

  function openingBalanceDetailsController($scope, $rootScope, growl, $http, CurrencyService,BillingGenericService, $cookies, CONSTANTS, $state, GenericService, openingBalanceService) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        selectAllRows: false,
        SelectedItems: [],
        opbDetail: [],
        SelectedRequestIds: []
      };
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      self.model.fillItemList = fillItemList;
      self.model.uomInfoOnClick = uomInfoOnClick;
      self.model.uomModalOnClick = uomModalOnClick;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectOpeningBalance = SelectOpeningBalance;
      self.model.opbDetail = [];
      self.model.SaveApproval = SaveApproval;
      self.model.ValidateApproval = ValidateApproval;
      self.model.isSelected = isSelected;
      self.model.isDisable = isDisable;
      self.model.viewStorageLocation = viewStorageLocation;
      fromOpeningBalance();
    }
    function fromOpeningBalance() {

      var id = openingBalanceService.getOpeningBalanceId();
      $scope.opbIdForDoccumentUpload = id;
      var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          // self.model.storeData = openingBalanceService.passOpeningBalanceId(id);
          // self.model.detailsStoreIndenting = response.data.items;
          self.model.fillItemList(response.data.items);
          self.model.opbDetail = response.data;
            self.model.opbDetail.items.forEach(function (item) {
        	  angular.forEach(self.model.opbDetail.items, function(item){
                  item.uom.find(function(d){
                      if(d.uomType == item.uomType){
                          item.uomType = d.uomDetails;
                      }
                  });
                  if(item.isThreeUOMApplicable == false){
                      if(item.uomTypeId ==1){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                      else if(item.uomTypeId == 2){
                          
                      }
                      else{
                        item.receivedQtyCon = (item.quantity);
                        item.receivedQtyCalculation = (item.quantity);
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                  }
                  else{
                      if(item.uomTypeId ==1){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                      else if(item.uomTypeId == 2){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                      else{
                        item.receivedQtyCon = (item.quantity);
                        item.receivedQtyCalculation = (item.quantity);
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                  }
              });
            item.cop = item.cop.toFixed(2);
            item.mrp = item.mrp.toFixed(2);
            item.copAmount = item.copAmount.toFixed(2);
            item.taxAmount = item.taxAmount.toFixed(2);
            item.taxPercent = item.taxPercent.toFixed(2);
            item.markup = item.markup.toFixed(2);
            if (item.mrpUomTypeId == 1) {
              item.mrpUomType = "Purchase"
            }
            else if (item.mrpUomTypeId == 2) {
              item.mrpUomType = "Stock"
            }
            else if (item.mrpUomTypeId == 3) {
              item.mrpUomType = "Dispense"
            }
          });
          $rootScope.stopLoader();
        });
    }

    var uomInfoOnClick = function (uomList, index, data) {
      self.model.selectedUOMList = [];
      self.model.selectedUOMUnit = [];
      self.model.selectedItemIndex = index;
      if (uomList)
        // uomList.forEach(function (item) {
        //   item['selectedUOM'] = item.uomUnit;
        //   item['selectedUOM'] = item.uomUnit;
        //   self.model.selectedUOMList.push(item);
        //   self.model.selectedUOMUnit.push({ 'uomType': item.uomUnit });
        // });
        uomList.forEach(function (item, k) {
          if(data.isThreeUOMApplicable == false){
            if(item.uomTypeId != 2){
              self.model.selectedUOMList.push(item);
            }
          }
          else{
            self.model.selectedUOMList.push(item);
          }
          
          self.model.selectedUOMUnit.push({ 'uomType': item.uomUnit, 'uomTypeId': item.uomTypeId });
        });
    }

    var uomModalOnClick = function () {
      self.model.opbDetail[self.model.selectedItemIndex].defaultUOM = [];
      if (self.model.selectedUOMList) {
        self.model.selectedUOMList.forEach(function (item) {
          self.model.opbDetail[self.model.selectedItemIndex].defaultUOM.push({
            uomTypeId: item.uomTypeId,
            uomUnitId: item.uomUnitId,
            conversion: item.conversion
          });
        });
      }
    }

    var fillItemList = function (list) {
      list.forEach(function (item) {
        var defaultUOM = [];
        for (let uom of item.uom) {
          defaultUOM.push({
            uomTypeId: uom.uomTypeId,
            uomUnitId: uom.uomUnitId,
            conversion: uom.conversion
          });
        }

        item['defaultUOM'] = defaultUOM;
        self.model.opbDetail.push(item);
      });
    }

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedRequestIds || self.model.SelectedRequestIds.length == 0) {
        growl.error('Select Items to Approve');
        isValid = false;
      }
      return isValid;
    }

    var viewStorageLocation = function (item) {
      self.model.storageDetailsList = item.storageDetails;
    }

    var SaveApproval = function () {
      if (ValidateApproval()) {
        var id = openingBalanceService.getOpeningBalanceId();
        var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.APPROVE_API + id;
        var data = {
          "openingBalanceDetailId": self.model.SelectedRequestIds,
          "statusId": 8,
          "userId": $scope.userId,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId
        }

        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success(response.data.message);
            NavigateToBack();
          }, function (err) {
            $rootScope.stopLoader();
            growl.error('Somthing Went Wrong');
          });

      }
    }

    var NavigateToBack = function () {
      $state.go('openingBalance');
    }

    var SelectAllRequests = function () {

      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.opbDetail.items.forEach(function (opb) {
          if (!isDisable(opb)) {
            opb.isSelected = true;
            self.model.SelectedRequestIds.push(opb.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.opbDetail.items.forEach(function (opb) {
          opb.isSelected = false;
        });
      }
    }

    var isSelected = function (opb) {
      if (!isDisable(opb))
        return opb.isSelected;
      else
        return true;
    }
    var isDisable = function (opb) {
      if (opb.statusId == 7 || opb.statusId == 5 || opb.statusId == 9 || opb.statusId != 1) {
        return true;
      }
      return false;
    }


    var SelectOpeningBalance = function (opb) {

      var opbIds = self.model.SelectedRequestIds;

      if (opb.isSelected) {
        opbIds.push(opb.id);
      } else {
        var index = opbIds.indexOf(opb.id);
        if (index > -1) {
          opbIds.splice(index, 1);
        }
      }

      if (self.model.opbDetail.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    $scope.getPatientDocumentList = function()
    {
      $rootScope.startLoader();
      var data =
      {
        "patientId":0,
        "unitId":$scope.unitId,
        "organisationId":$scope.organizationId,
        "userId":undefined,
        "visitTypeId":undefined,
        "admissionId":undefined,
        "opbId":$scope.opbIdForDoccumentUpload,
      }
      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.patientDocumentList = [];
        if (response.data.status == "success") 
        {
       	  $scope.patientDocumentList = response.data.listObject; 
       	  $('#uploadDetails').modal('show');
       	  angular.forEach($scope.patientDocumentList,function(value, key) {
       		  if(value.bucketFlag == 1){
       		  var filepath = value.documentPath
            var arr = filepath.split("/");
            var length=arr.length;
            var fileName1=arr[length-1];
            var arr1 = fileName1.split("_");
            var length1=arr1.length;
            var fileName=arr1[length1-1];
            value.fileName=fileName;
       	  }else{
       		value.fileName=value.fileName;
       	  }
       	  });
          $rootScope.stopLoader();
        }
        else if(response.data.status == "error")
        {
       	  $rootScope.stopLoader();
        }
      });
    }

    initializeController();
  }


  function config($stateProvider) {
    $stateProvider.state("DetailsOpeningBalance", {
      url: "/DetailsOpeningBalance",
      templateUrl: 'views/PharmacyInventory/Store/details-opening-balance.html',
      controller: "OpeningBalanceDetails.Controller",
      controllerAs: "vm",
      title: 'Opening Balance'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("OpeningBalanceDetails.Controller", openingBalanceDetailsController);
})();