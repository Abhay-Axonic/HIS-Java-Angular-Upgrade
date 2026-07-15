(function () {
  "use strict";
  var CACHE = {};

  function implantOpeningBalanceDetailsController($scope, $rootScope, growl, $http, CurrencyService, CONSTANTS, $state, GenericService, openingBalanceService,$cookies) {
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
  
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectOpeningBalance = SelectOpeningBalance;
      self.model.opbDetail = [];
      self.model.SaveApproval = SaveApproval;
      self.model.ValidateApproval = ValidateApproval;
      self.model.isSelected = isSelected;
      self.model.isDisable = isDisable;
      fromOpeningBalance();
    }
    function fromOpeningBalance() {

      var id = openingBalanceService.getOpeningBalanceId();
      var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.DETAILS_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
        
          self.model.opbDetail = response.data;
          self.model.opbDetail.items.forEach(function (item) {
            item.cop = item.cop.toFixed(2);
            item.mrp = item.mrp.toFixed(2);
            item.copAmount = item.copAmount.toFixed(2);
            item.taxAmount = item.taxAmount.toFixed(2);
            item.taxPercent = item.taxPercent.toFixed(2);
            item.markup = item.markupPercentage.toFixed(2);
         
          });

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



    var SaveApproval = function () {
      if (ValidateApproval()) {
        var id = openingBalanceService.getOpeningBalanceId();
        var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.APPROVE_API + id;
        var data = {
          "openingBalanceDetailId": self.model.SelectedRequestIds,
          "statusId": 8
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



    initializeController();
  }


  function config($stateProvider) {
    $stateProvider.state("DetailsImplantOpeningBalance", {
      url: "/DetailsImplantOpeningBalance",
      templateUrl: 'views/PharmacyInventory/Store/details-implant-opening-balance.html',
      controller: "implantOpeningBalanceDetails.Controller",
      controllerAs: "vm",
      title: 'Implant Opening Balance'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("implantOpeningBalanceDetails.Controller", implantOpeningBalanceDetailsController);
})();