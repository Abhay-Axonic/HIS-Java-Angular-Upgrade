(function () {
    "use strict";
  
    function assetIndentDetailsController($scope, $rootScope,commonDetailService, $http, $cookies, CONSTANTS, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
         
  
         
      };
      self.model.accessoryModal= accessoryModal;
      fromAssetIndent();
  
    }


    function fromAssetIndent() {
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.ASSET.ASSET_INDENT.DETAIL_API + $scope.unitId+"/"+ $scope.organizationId +"/"+ id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
            function (response) {
                self.model.assetIndentDetailsData = response.data;
                self.model.assetIndentDetailsData.items.forEach(function (item) {
                    item.isDisabled = true;

                });
            });
    }

    var accessoryModal = function (asset) {
        $("#viewDetailsAccessoriesModal").modal()
        self.model.acessoryList = [];
        self.model.acessoryList = asset.accessories; 
      }
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsAssetIndent", {
        url: "/detailsAssetIndent",
        templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-indent.html',
        controller: "AssetIndentDetails.Controller",
        controllerAs: "vm",
        title: 'Asset Indent'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("AssetIndentDetails.Controller", assetIndentDetailsController);
  })();

			
			 