(function () {
  "use strict";

  function detailsauctionRequistionController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl, commonDetailService) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {



      };
      self.model.GetDetailAuctionRequisitionData = GetDetailAuctionRequisitionData;
      self.model.viewAssetDetails = viewAssetDetails;
      GetDetailAuctionRequisitionData(commonDetailService.getDataId());
    }
    var GetDetailAuctionRequisitionData = function (auctionRequisitionId) {
      self.model.auctionRequisitionId = auctionRequisitionId;
      var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.DETAILS_API + $scope.unitId+ '/'+$scope.organizationId + '/' + $scope.userId + '/' + auctionRequisitionId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.auctionRequisitionId = response.data.id;
          self.model.detailsAuctionRequistionList = response.data;

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    var viewAssetDetails = function (item) {
      $("#viewAsset").modal()
      self.model.accessoriesDetails = [];
      self.model.accessoriesDetails = item.accessoriesDetails;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsauctionRequistion", {
      url: "/detailsauctionRequistion",
      templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/details-auction-requistion.html',
      controller: "DetailsAuctionRequistion.Controller",
      controllerAs: "vm",
      title: 'Auction Requisition'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsAuctionRequistion.Controller", detailsauctionRequistionController);
})();
