(function () {
    "use strict";
  
    function linenIssueAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS,commonDetailService, $state, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        var date = new Date();
        self.model = {
   
            requestData: [],
            fromDateOptions: {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date(),
                minDate : date.setDate((new Date()).getDate() - 90),
            },
            toDateOptions: {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date()
            },
            fromDate: new Date(),
            toDate: new Date(),
            fromStores: [{
                "store": "All Stores"
            }],
            toStores: [{
                "store": "All Stores"
            }],
            statuses: [{
                "status": "All"
            }],
            indentTypes: [{
                "type": "All"
            }],

        };

        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.MaterialIssueListData = MaterialIssueListData;
        self.model.LinenIssueAcceptanceDetails = LinenIssueAcceptanceDetails;
        getDropdownsList();
        MaterialIssueListData();
  
    }
    var getDropdownsList = function () {
        var materialissue = self.model;
        materialissue.fromStore = materialissue.fromStores[0];
        materialissue.toStore = materialissue.toStores[0];
        materialissue.status = materialissue.statuses[0];
        materialissue.indentType = materialissue.indentTypes[0];

        var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE_ACCEPTANCE.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.userId; 
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                materialissue.fromStores = materialissue.fromStores.concat(response.data.fromStore);
                materialissue.toStores = materialissue.toStores.concat(response.data.toStore);
                materialissue.statuses = materialissue.statuses.concat(response.data.status);
                materialissue.indentTypes = materialissue.indentTypes.concat(response.data.indentType);
                $rootScope.stopLoader();
            },
            function (err) {
                $rootScope.stopLoader();
            });
    }
    var OpenFromDate = function () {
        self.model.fromDateOpened = true;
        self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
        self.model.toDateOpened = true;
        self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var MaterialIssueListData = function () { 
    	
        var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE_ACCEPTANCE.SEARCH_API; 
        var data = {  
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
          "toStoreId": self.model.toStore.id ? self.model.toStore.id:undefined,
          "indentNo": self.model.indentNo? self.model.indentNo:undefined,
          "statusId": self.model.status.id? self.model.status.id:undefined, 
          "indentTypeId": self.model.indentType.id ? self.model.indentType.id:undefined, 
		  "unitId":$scope.unitId,
		  "userId":$scope.userId,
        };
        $rootScope.startLoader()
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            for (var i = 0; i < response.data.length; i++) {
              response.data[i]["isChecked"] = false;
            }
            self.model.searchMaterialIssueListData = response.data;
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Error' );
          });
      }

      var LinenIssueAcceptanceDetails = function (acceptance) {
        commonDetailService.setDataId(acceptance.id);
        $state.go('linenIssueAcceptanceDetails');
    }

      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("linenIssueacceptance", {
        url: "/linenIssueacceptance",
        templateUrl: 'views/Linen/linen-issue-acceptance.html',
        controller: "LinenIssueAcceptance.Controller",
        title: 'Linen Issuance Acceptance',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 96);
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
      .controller("LinenIssueAcceptance.Controller", linenIssueAcceptanceController);
  })();

