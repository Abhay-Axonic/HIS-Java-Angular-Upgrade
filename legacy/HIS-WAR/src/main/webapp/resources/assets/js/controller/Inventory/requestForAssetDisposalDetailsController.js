(function () {
  "use strict";

  function requestForAssetDisposalDetailsController($scope, commonDetailService, $state, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {



      };
      self.model.fromRequestDisposal = fromRequestDisposal;
      self.model.SaveApproval = SaveApproval;
      self.model.NavigateToBack = NavigateToBack;
      fromRequestDisposal();

    }
    function fromRequestDisposal() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.SCRAP.REQUEST_FOR_ASSET_DISPOSAL.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId+ '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.requestDisposalDetailData = response.data;
        });
    }

    var SaveApproval = function () {

        var URI = CONSTANTS.INVENTORY.SCRAP.REQUEST_FOR_ASSET_DISPOSAL.SAVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": 4,
          "witnessId": parseInt(self.model.witnessId),
          "organizationId": $scope.organizationId,
          "unitId": $scope.unitId,
          "userId": $scope.userId
        }
        $rootScope.startLoader();
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


    var NavigateToBack = function () {
      $state.go('requestForAssetDisposalWorklist');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsRequestForAssestDisposalWorklist", {
      url: "/detailsRequestForAssestDisposalWorklist",
      templateUrl: 'views/PharmacyInventory/Store/details-request-for-assest-disposal-worklist.html',
      controller: "RequestForAssetDisposalDetails.Controller",
      controllerAs: "vm",
      title: 'Request For Asset Disposal Worklist'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("RequestForAssetDisposalDetails.Controller", requestForAssetDisposalDetailsController);
})(); 