(function () {
  "use strict";

  function productRestrictionController($scope, $rootScope, $state, $cookies) {
      var self = this;

      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
          $rootScope.loginpage = true;

          self.model = {
            activeTab: 1,
          };
          self.model.NavigateToProductCategoryMapping = NavigateToProductCategoryMapping;
          self.model.NavigateToItemRestriction = NavigateToItemRestriction;
          self.model.SetActiveTab = SetActiveTab;
      }

      var NavigateToProductCategoryMapping = function () {
          $state.go('productRestriction.productCategoryMapping');
      }

      var NavigateToItemRestriction = function () {
          $state.go('productRestriction.itemRestriction');
      }

      var SetActiveTab = function (tab) {
          if(tab == 1){
            NavigateToProductCategoryMapping();
          }
          else{
            NavigateToItemRestriction();
          }
          self.model.activeTab = tab;
      }
      initializeController();
  }

  function config($stateProvider) {
      $stateProvider.state("productRestriction", {
          url: "/productRestriction",
          templateUrl: 'views/PharmacyInventory/StockAdjustment/product-restriction.html',
          controller: "ProductRestriction.Controller",
      	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 633);
   				 	});
   					if(arr.length <= 0){
   						$state.go("accessDenied")
   						deferred.reject();
   					 } else {
   						deferred.resolve();
   					 }
   		        });
   		
   		        return deferred.promise;
   		      }
   		 },
          controllerAs: "vm"
      });
  }

  angular
      .module("myApp")
      .config(config)
      .controller("ProductRestriction.Controller", productRestrictionController)
})();