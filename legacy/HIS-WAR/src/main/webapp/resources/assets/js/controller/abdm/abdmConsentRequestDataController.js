(function () {
    "use strict";

    function abdmConsentRequestDataController($scope, $interval ,$rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, BillingGenericService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService, reportService ,$timeout) {
        var self = this;
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            self.model = {};
        }


		$rootScope.loginpage = true;
		$scope.saveBtnFlag = true;
		$scope.updateBtnFlag = false;
		
        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');

     			
     			$scope.getHiuDecryptedData = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/truncateFHIRDetails";
    				var data={
  						  "consentRequestIdHiu" :  $rootScope.consentRequestIdHiu,
  					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    		             	$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
     			
     			
     			$scope.goBack = function () {
     				$scope.getHiuDecryptedData();
     				$state.go('abdmConsentRequest');
     			}
     			$scope.openPdfInNewWindow = function (base64String,docType,docName) {
     			    // Convert base64 string to a Blob
     				
     				if(docName==='base64'){
     			    var binary = atob(base64String.replace(/\s/g, ''));
     			    var len = binary.length;
     			    var buffer = new ArrayBuffer(len);
     			    var view = new Uint8Array(buffer);
     			    
     			    for (var i = 0; i < len; i++) {
     			        view[i] = binary.charCodeAt(i);
     			    }

     			    // Create a Blob from the binary data
     			    var blob = new Blob([view], { type: docType});

     			    // Create a URL for the Blob
     			    var blobUrl = URL.createObjectURL(blob);

     			    // Open the Blob URL in a new window
     			    window.open(blobUrl);
     			    
     			}else if(docName==='url'){
     				
     				 window.open(base64String);
     			}
     			};

    }
    


    function config($stateProvider) {
    $stateProvider.state("abdmConsentRequestData", {
      url: "/abdmConsentRequestData",
      templateUrl: 'views/Abdm/consent-data.html',
      controller: "AbdmConsentRequestData.Controller",
      title: 'ABDM Consent Request Data',
      controllerAs: "vm",
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AbdmConsentRequestData.Controller", abdmConsentRequestDataController);
})();
