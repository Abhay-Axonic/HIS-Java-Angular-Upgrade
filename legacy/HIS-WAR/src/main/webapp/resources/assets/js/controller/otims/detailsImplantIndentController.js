(function () {
    "use strict";
  
    function detailsImplantIndentController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService,CurrencyService, commonDetailService, growl) {
      var self = this;
  
      var cookieObject = $cookies.getObject('cookieObject');
      if(cookieObject == undefined){ 
    	  $state.go('login');
    	  return;
    	 }
      $scope.unitId = cookieObject.unitId;
      $scope.organizationId = cookieObject.organizationId;
      $scope.userId = cookieObject.userId;
      $scope.storeId = cookieObject.storeId; 
      
      
      function initializeController() {
    	  
        $rootScope.loginpage = true;
  
        self.model = {};
   
        self.model.NavigateToBack = NavigateToBack;
        GetConsignOutwardRequests(commonDetailService.getDataId());
      }
  
      var GetConsignOutwardRequests = function (indentId) {
        commonDetailService.setDataId(null);
  
        var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.DETAILS_API +$scope.unitId+ "/"+$scope.storeId +"/" + indentId;       
       $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
          
            self.model.implantIndentDetails = response.data;
            
            angular.forEach(self.model.implantIndentDetails.items, function (item) {
      
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
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
  
      var NavigateToBack = function (){
        $state.go('implantIndent');
      }
  
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsImplantIndent", {
        url: "/detailsImplantIndent",
        templateUrl: 'views/master/TheatreManagement/details-implant-indent.html',
        controller: "DetailsImpalntIndent.Controller",
        controllerAs: "vm"
      });
     
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("DetailsImpalntIndent.Controller", detailsImplantIndentController);
  })();