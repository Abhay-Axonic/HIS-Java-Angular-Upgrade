(function () {
    "use strict";

    function detailsAuctionVendorApprovalController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService) {
        var self = this;
        var selectedItemId;

        //Default Constructor
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

            self.model = {
                vendorList: [],
                date: new Date(),
                toDate: new Date(),
                dateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false
                },
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });

            self.model.OpenDate = OpenDate;
            self.model.GetAuctionVendorApprovalDetails = GetAuctionVendorApprovalDetails;
            self.model.OpenPopup = OpenPopup;
            self.model.GetPaymentTerm = GetPaymentTerm;
            self.model.QtyValidation = QtyValidation;
            self.model.SaveAuctionVendorApproval = SaveAuctionVendorApproval;
            GetAuctionVendorApprovalDetails(commonDetailService.getDataId());
        }

        var OpenDate = function () {
            self.model.dateOpened = true;
        }

        var GetAuctionVendorApprovalDetails = function (itemId) {
            self.model.itemId = itemId;
            var URI = CONSTANTS.ASSET.AUCTION_VENDOR_APPROVAL.DETAILS_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.auctionVendorApprovalDetailsList = response.data;
                    self.model.itemInfo = response.data.items;
                    // self.model.itemInfo.forEach(function (dataList) {
                    //     dataList.vendorDetails.forEach(function (vendor) {
                    //     vendor.totalVendorQuantity = 0;
                    // });
                    // });
                    $rootScope.stopLoader();
                });
        }

        var QtyValidation = function (vendorDetails) {
            self.model.totalVendorQuantity = 0;
            vendorDetails.forEach(function (vendor) {
                self.model.totalVendorQuantity += parseFloat(vendor.qty);
                vendor.totalVendorQuantity = self.model.totalVendorQuantity;
                console.log(vendor.totalVendorQuantity);
                vendor.isQuantityValid = false;
            });
        }

        var OpenPopup = function (modal, item) {
            var popup = angular.element('#' + modal);
            popup.modal('show');
        }

        var GetPaymentTerm = function (vendor) {
            self.model.paymentTerms = vendor.paymentTerms
        }

        var ValidateVendorDetails = function () {
            var isValid = true;

            var itemInfo = self.model.itemInfo;
            if (!itemInfo || itemInfo.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            } else {


                self.model.itemInfo.forEach(function (dataList) {
                    dataList.vendorDetails.forEach(function (vendor) {
                        if (vendor.totalVendorQuantity > vendor.auctionQuantity) {
                            vendor['isQuantityValid'] = true;
                            isValid = false;
                            growl.error("Please add quantity less than auction quantity");
                        }
                        if (!vendor.qty) {
                            vendor['isQuantityValid'] = true;
                            isValid = false;
                        }
                        if (vendor.isAccepted == undefined) {
                            vendor['isAcceptedValid'] = true;
                            isValid = false;
                            growl.error("Please select radio button for accept or reject");
                        }
                        if (vendor.isAccepted == false) {
                            if (!vendor.rejectNote) {
                                vendor['isRejectNoteValid'] = true;
                                isValid = false;
                            }
                        }
                    });
                });

            }
            return isValid;
        }

        var SaveAuctionVendorApproval = function () {
            if (ValidateVendorDetails()) {
                var URI = CONSTANTS.ASSET.AUCTION_VENDOR_APPROVAL.SAVE_API + self.model.itemId;

                var data = {
                		"unitId":$scope.unitId,
                		"organizationId":$scope.organizationId,
                		"userId":$scope.userId,
                		"storeId":$scope.storeId,
                        "items": []
                }

                self.model.itemInfo.forEach(function (dataList) {

                    var item = {
                        "id": dataList.id,
                        "vendorDetails": []
                    }
                    dataList.vendorDetails.forEach(function (vendor) {

                        var vendorDetail = {
                            "id": vendor.id,
                            "isAccepted": vendor.isAccepted,
                            "rejectNote": vendor.rejectNote,
                            "approvedAuctionQuantity": vendor.qty,
                        }
                        item.vendorDetails.push(vendorDetail);
                    });
                    data.items.push(item);
                });



                console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        NavigateAuctionVendorApproval();
                        growl.success('Auction vendor approval Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving auction vendor approval ');
                    });
            }
        }

        var NavigateAuctionVendorApproval = function () {
            $state.go('auctionVendorApproval');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsAuctionVendorApproval", {
            url: "/detailsAuctionVendorApproval",
            templateUrl: 'views/Asset Management/details-auction-vendor-approval.html',
            controller: "DetailsAuctionVendorApproval.Controller",
            controllerAs: "vm",
            title: 'Auction Vendor Approval'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsAuctionVendorApproval.Controller", detailsAuctionVendorApprovalController)
})();