(function () {
    "use strict";

    function detailsFixedAssetMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService) {
        var self = this;
        var selectedItemId;
       
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            
            self.model.GetDetailsFixedAssetMaster = GetDetailsFixedAssetMaster;
            self.model.OpenPopup = OpenPopup;
            self.model.ViewAccessoryDetails = ViewAccessoryDetails;
            self.model.ViewSparesDetails = ViewSparesDetails;
            self.model.ViewInsuranceDetails = ViewInsuranceDetails;
            GetDetailsFixedAssetMaster(commonDetailService.getDataId());
        }

        var GetDetailsFixedAssetMaster = function (itemId) {

            var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.detailsFixedAssetMasterList = response.data;

                });
            $rootScope.stopLoader();
        }

        var OpenPopup = function (modal, item) {
            var popup = angular.element('#' + modal);
            popup.modal('show');
        }

        var ViewAccessoryDetails = function (item) {
          //  self.model.detailsFixedAssetMasterList.items.find(function (x) {
             //   if (x.itemId == item.itemId) {
                    self.model.itemForAccessoryDetail = item;
             //   }
            //});
        }

        var ViewSparesDetails = function (item){
            //self.model.detailsFixedAssetMasterList.items.find(function (x) {
              //  if (x.itemId == item.itemId) {
                    self.model.itemForSpareDetail = item;
               // }
           // });
        }

        var ViewInsuranceDetails = function (item) {

            //self.model.detailsFixedAssetMasterList.items.find(function (x) {
              //  if (x.itemId == item.itemId) {
                    self.model.insuranceCompanyId = item.insuranceCompanyId;
                    self.model.insuranceCompany = item.insuranceCompany;
                    self.model.policyNumber = item.policyNumber;
                    self.model.policyFromDate = item.policyFromDate;
                    self.model.policyToDate = item.policyToDate;
                    self.model.amountInsured = item.amountInsured;
                    self.model.remark = item.remark;
               // }
           // });

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsFixedAssetMaster", {
            url: "/detailsFixedAssetMaster",
            templateUrl:'views/master/UnitMaster/assetManagement/details-fixed-asset-master.html',
            controller: "DetailsFixedAssetMaster.Controller",
            controllerAs: "vm",
            title: 'Add Fixed Asset Opening Balance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsFixedAssetMaster.Controller", detailsFixedAssetMasterController)
})();
