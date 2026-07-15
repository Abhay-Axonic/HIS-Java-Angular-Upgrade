(function () {
  "use strict";

  function wasteAssetDisposalWorklistDetailsController($scope, $rootScope, commonDetailService, $state, $http, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {



      };
      self.model.fromWasteDisposal = fromWasteDisposal;
      self.model.SaveApproval = SaveApproval;
      self.model.NavigateToBack = NavigateToBack;
      self.model.ValidateApproval = ValidateApproval;
      fromWasteDisposal();

    }

    function fromWasteDisposal() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.SCRAP.WASTE_ASSET_DISPOSAL.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.wasteDisposalDetailData = response.data;
          
          angular.forEach(response.data.witnessDetails, function (value, key) { 

        	  if(value.id == response.data.witnessId)
        		  {
        		  self.model.wasteDisposalDetailData.witnessName=value.name;
        		  }
          
          }); 
          
          //self.model.witnessId = response.data.witnessId;
        });
    }
    var SaveApproval = function () {

      if (ValidateApproval()) {
      var URI = CONSTANTS.INVENTORY.SCRAP.WASTE_ASSET_DISPOSAL.SAVE_API + commonDetailService.getDataId();
      var data = {
        "statusId": 4,
        "witnessId": parseInt(self.model.witnessId),
        "organizationId": $scope.organizationId,
        "unitId": $scope.unitId,
        "userId": $scope.userId,
        "items": []
      }

      $rootScope.startLoader();
      angular.forEach(self.model.wasteDisposalDetailData.items, function (item) {
        var newItem = {
          "id": item.id,
          "disposeQuantity": parseInt(item.disposeQuantity),
          "statusId": 4
        }
        if (item.disposeQuantity == item.scrapQuantity) {
          newItem.statusId = 4
        }
        else {
          newItem.statusId = 18;
        }

        data.items.push(newItem);
      });

      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          // growl.success(response.data.message);
          growl.success("Consumable disposal worklist updated sucessfully");
          NavigateToBack();
        }, function (err) {
          $rootScope.stopLoader();
          growl.error('Somthing Went Wrong');
        });
      }   
    }
    var NavigateToBack = function () {
      $state.go('wasteAssetDisposalWorklist');
    }


    var ValidateApproval = function () {
      var isValid = true;
      angular.forEach(self.model.wasteDisposalDetailData.items, function (item) {
        if (!item.disposeQuantity || item.disposeQuantity > item.scrapQuantity) {
          growl.error('Please Enter Valid Dispose Quantity');
          isValid = false;
        }
        if(!self.model.witnessId){
          growl.error('Please Select Witness');
          isValid = false;
        }
      });


      return isValid;
    }


    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailswasteAssetDisposalWorklist", {
      url: "/detailswasteAssetDisposalWorklist",
      templateUrl: 'views/PharmacyInventory/Store/details-waste-disposal-worklist.html',
      controller: "WasteAssetDisposalWorklistDetails.Controller",
      controllerAs: "vm",
      title: 'Consumable Disposal Worklist'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("WasteAssetDisposalWorklistDetails.Controller", wasteAssetDisposalWorklistDetailsController);
})(); 