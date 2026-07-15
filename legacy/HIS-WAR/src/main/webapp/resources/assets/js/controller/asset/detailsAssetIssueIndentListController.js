(function () {
  "use strict";

  function detailsAssetIssueIndentListController($scope, $rootScope, $http, commonDetailService, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
      };

      self.model.fromAssetIssueIndentList = fromAssetIssueIndentList;

      fromAssetIssueIndentList()
    }

    function fromAssetIssueIndentList() {
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.ASSET.ASEET_ISSUE.ISSUELIST_DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.assetIssueListDetail = response.data;
        });
    }

    initializeController();
  }
  
  function config($stateProvider) {
    $stateProvider.state("detailsAssetIssueIndentList", {
      url: '/detailsAssetIssueIndentList',
      templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-issue-indent-list.html',
      controller: "detailsAssetIssueIndentList.Controller",
      controllerAs: "vm",
      title:"Details Asset Issue"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("detailsAssetIssueIndentList.Controller", detailsAssetIssueIndentListController);
})();
