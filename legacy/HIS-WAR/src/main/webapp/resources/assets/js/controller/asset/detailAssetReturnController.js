(function () {
    "use strict";
  
    function detailAssetReturnController($scope, $rootScope, $http, $cookies, CONSTANTS,commonDetailService, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
         
  
         
      };
  
      self.model.fromAssetReturn =fromAssetReturn;
      self.model.setaccessory = setaccessory;
      fromAssetReturn()
    }

    function fromAssetReturn() {  
        
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.ASSET.ASSET_RETURN.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
            function (response) { 
                self.model.assetReturnDetail = response.data; 
            });
    }



    var setaccessory = function(item){
      self.model.accessoryList  = [];
self.model.accessoryList = item.asseccories;
$('#viewDetailsAccessoriesModal').modal();
    }
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsAssetReturn", {
        url: '/detailsAssetReturn',
        templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-return.html',
        controller: "DetailAssetReturn.Controller",
        controllerAs: "vm",
        title: 'Asset Return'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("DetailAssetReturn.Controller", detailAssetReturnController);
  })();
   