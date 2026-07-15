(function () {
    "use strict";

    function materialIssueController($scope, $cookies , $rootScope, $state) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                activeTab : 1
            };

            self.model.NavigateToPendingIssue = NavigateToPendingIssue;
            self.model.NavigateToIssueList = NavigateToIssueList;
            self.model.SetActiveTab = SetActiveTab;
        }

        var SetActiveTab = function (tab) {
            self.model.activeTab = tab;
        }

        var NavigateToPendingIssue = function () {
            $state.go('materialIssue.pendingIssue');
        }

        var NavigateToIssueList = function () {
            $state.go('materialIssue.issueList');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("materialIssue", {
            url: "/materialIssue",
            templateUrl: 'views/PharmacyInventory/InPatient/material-issue.html',
            controller: "MaterialIssue.Controller",
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 127 || item.id == 128);
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
        .controller("MaterialIssue.Controller", materialIssueController)
})();