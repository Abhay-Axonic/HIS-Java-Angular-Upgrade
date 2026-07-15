(function () {
  "use strict";

  function detailsAssetIssueListController($scope, $rootScope, $http, commonDetailService, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {



      };


      self.model.fromAssetIssueList = fromAssetIssueList;
      self.model.openViewModal = openViewModal;

      fromAssetIssueList()
    }

    function fromAssetIssueList() {

      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.ASSET.ASEET_ISSUE.ISSUELIST_DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.issueListDetail = response.data;
        });
    }


    var openViewModal = function (item) {
      self.model.accessoryList = [];
      self.model.accessoryList = item.asseccories;
      $("#view_accessories_modal").modal()
    }

    initializeController();
  }
  function config($stateProvider) {
    $stateProvider.state("detailsAssetIssueList", {
      url: '/detailsAssetIssueList',
      templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-issue-list.html',
      controller: "DetailsAssetIssueList.Controller",
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsAssetIssueList.Controller", detailsAssetIssueListController);
})();
