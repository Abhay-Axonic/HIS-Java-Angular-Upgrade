(function () {
    "use strict";
  
    function linenIndentDetailController($scope, $rootScope, $http, $cookies, CONSTANTS,commonDetailService, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
         
  
         
      };
      fromLinenIndent()
  
    }
    function fromLinenIndent() {
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.DETAIL_API + id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
          function (response) {
            self.model.linenIndentDetail = response.data;
          });
      } 
  

    
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("linenIndentDetails", {
        url: "/linenIndentDetails",
        templateUrl: 'views/Linen/linen-indent-details.html',
        controller: "LinenIndentDetail.Controller",
        controllerAs: "vm"
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("LinenIndentDetail.Controller", linenIndentDetailController);
  })();

