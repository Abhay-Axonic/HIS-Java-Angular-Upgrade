(function () {
  "use strict";

  function detailsWorkOrderCreationController($scope, $rootScope, $filter, $http, $state, $cookies, CONSTANTS, GenericService, commonDetailService, rejectReasonService, StatusService, CurrencyService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        taxListSelection: [{
          "taxName": "Select tax"
        }],
        batchListSelection: [{
          "batchNo": "Select Batch"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        taxCodeValid: [],
        purchaseId: null,
        vendorSearch: '',
        vendorNames: [],
        search: {
          itemName: undefined,
          itemCode: undefined
        },
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.isItemChecked = false;
      self.model.GetWorkOrders = GetWorkOrders;
      self.model.PopupAction = PopupAction;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ViewTerms = ViewTerms;
      self.model.changeDiscountAmnt = changeDiscountAmnt;
      self.model.getDiscountAmnt = getDiscountAmnt;
      self.model.getDiscountPercentage = getDiscountPercentage;
      GetWorkOrders(commonDetailService.getDataId());
      //PopulateDropdown();
    }

    var changeDiscountAmnt = function (item) {
      if (item.woAmount == "") {
        item.discountAmount = 0;
      }
    }
    var getDiscountAmnt = function (item) {
      var ans = (item.discount * item.woAmount) / 100;
      item.discountAmount = ans;
      return item.discountAmount;
    }
    var getDiscountPercentage = function (item) {
      var per = (item.discountAmount * 100) / item.woAmount;
      item.discount = per;
      return per;
    }
    var setStatus = function (data) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == data;
      });

      return status.id;
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    function nameNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    var OpenPopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
    }

    var ClosePopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('hide');
    }
    /* 
        var PopulateDropdown = function () {
          try {
            $rootScope.startLoader();
            self.model.store = "store 1";
            self.model.contractType ="code 1";
            self.model.againstType = "Direct";
          
            var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
    
            GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
                
    
                var purchaseId = commonDetailService.getDataId();
                $rootScope.stopLoader();
                if (!!purchaseId) {
                  commonDetailService.setDataId(null);
                  GetWorkOrders(purchaseId);
                }
              },
              function (err) {
                $rootScope.stopLoader();
              });
          } catch (error) {
            alert(error);
          }
        } */

    var ViewTerms = function (viewModal, item) {
      self.model.itemTerms = item;
      switch (viewModal) {
        case "inclusionDetails":
          self.model.itemDetails.items.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.inclusionDetails = x.inclusionDetails;
            }
          });
          break;
        case "exclusionDetails":
          self.model.itemDetails.items.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.exclusionDetails = x.exclusionDetails;
            }
          });
          break;
        case "paymentTerms":
          self.model.itemDetails.items.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.paymentTerms = x.paymentTerm;
            }
          });
          break;
      }
    }
    var GetWorkOrders = function (purchaseId) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.DETAILS_API + $scope.unitId+ '/'+$scope.organizationId  + '/' + purchaseId;
      $rootScope.startLoader();



      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {

          self.model.itemDetails = response.data;
          angular.forEach(response.data.items, function (item) {
          });

          $rootScope.stopLoader();
        },
        function (err) {

          growl.error(err.data.message);
          $rootScope.stopLoader();
        });

    }

    var PopupAction = function (action, modal) {
      var popup = angular.element('#' + modal);
      self.model.action = action;
      if (action == "Open") {
        popup.modal('show');
      } else {
        popup.modal('hide');
      }
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsWorkorderCreation", {
      url: "/detailsWorkorderCreation",
      templateUrl: 'views/procurement/workOrder/details-workorder-creation.html',
      controller: "DetailsWorkorderCreation.Controller",
      controllerAs: "vm",
      title: 'Work Order Creation'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsWorkorderCreation.Controller", detailsWorkOrderCreationController);
})();