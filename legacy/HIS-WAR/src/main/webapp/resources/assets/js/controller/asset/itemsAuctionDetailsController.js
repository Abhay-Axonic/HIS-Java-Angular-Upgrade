(function () {
    "use strict";

    function itemsAuctionDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, StatusService, commonDetailService) {
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
                dateOpened: []
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.currency = currency;
            });

            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            });

            self.model.OpenDate = OpenDate;
            self.model.GetItemsAuctionDetails = GetItemsAuctionDetails;
            self.model.OpenPopup = OpenPopup;
            self.model.viewAssetDetails = viewAssetDetails;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.AddVendorName = AddVendorName;
            self.model.AddVendorDetails = AddVendorDetails;
            self.model.SaveItemsForAuction = SaveItemsForAuction;
            self.model.SaveVendorDetails = SaveVendorDetails;
            self.model.ViewVendorDetails = ViewVendorDetails;
            // self.model.AddVendor = AddVendor;
            self.model.RemoveVendor = RemoveVendor;
            GetItemsAuctionDetails(commonDetailService.getDataId());

        }

        var OpenDate = function (idx) {
            self.model.dateOpened[idx] = true;
        }




        var GetItemsAuctionDetails = function (itemId) {
            self.model.itemId = itemId;
            var URI = CONSTANTS.ASSET.ITEMS_FOR_AUCTION.DETAILS_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.itemsAuctionDetailsList = response.data;
                    $rootScope.stopLoader();
                });

        }

        var OpenPopup = function (modal, item) {
            var popup = angular.element('#' + modal);
            popup.modal('show'); 
        }

        var viewAssetDetails = function (item) {
            self.model.itemsAuctionDetailsList.items.find(function (x) {
                if (x.id == item.id) {
                    self.model.itemForAssetDetail = x.accessories;
                }
            });
        }

        var AddVendorDetails = function (item) {
            self.model.currentItem = item;
            self.model.vendorList = item.vendorDetails

        }

        var SearchVendorNames = function (search) {
            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.vendorNames = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.vendorNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return nameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return nameNotFound(search);
                    });
            } else {
                if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
                    return nameNotFound(search);
                } else {
                    return $filter('filter')(self.model.vendorNames, {
                        $: search
                    });
                }
            }
        }

        var nameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
        }

        var AddVendorName = function (vendor, item) {

            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
            var isFound = self.model.vendorList.find(function (x) {
                growl.error('Vendor Is Already Present in the List');
                return x.vendorId == vendor.id;
            });
            if (!isFound) {
                var data = {
                    "vendorId": self.model.vendorId,
                    "vendorName": self.model.vendorName,
                    "vendorAmount": null,
                    "paymentTerms": null,
                    "referenceNumber": null,
                    "referenceDate": null
                }
                self.model.vendorList.push(data);
            }

            // console.log(self.model.vendorList);
            self.model.vendor = null;
        }

        // var AddVendor = function (index, vendorList) {
        //     vendorList.splice(index + 1, 0, CopyVendor(index, vendorList));
        // }

        // var CopyVendor = function (index, vendorList) {
        //     var item = vendorList[index];
        //     return {
        //         "vendorId": item.vendorId,
        //         "vendorName": item.vendorName,
        //         "vendorAmount": item.vendorAmount,
        //         "paymentTerms": item.paymentTerms,
        //         "referenceNumber": item.referenceNumber,
        //         "referenceDate": item.referenceDate
        //     }
        // }

        var RemoveVendor = function (index, vendorList) {
            vendorList.splice(index, 1);
        }

        var SaveVendorDetails = function () {
            if (ValidateVendorDetails()) {
                self.model.itemsAuctionDetailsList.items.forEach(function (obj) {
                    if (obj.id == self.model.currentItem.id) {

                        obj.vendorDetails = self.model.vendorList;
                    }
                });
                // self.model.vendorList = [];
                // console.log(self.model.itemsAuctionDetailsList);

                var popup = angular.element('#AccessoriesDetailsQEModal');
                popup.modal('hide');
            }
        }

        var ValidateVendorDetails = function () {
            var isValid = true;

            var vendorList = self.model.vendorList;
            if (!vendorList || vendorList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            } else {
                vendorList.forEach(function (item) {

                    if (!item.vendorAmount || !item.referenceNumber || !item.referenceDate || !item.paymentTerms) {
                        isValid = false;
                        growl.error('Please fill all the fields');
                    }

                    if (!item.vendorAmount) {
                        item['isVendorAmountValid'] = true;
                        isValid = false;
                    }

                    if (!item.referenceNumber) {
                        item['isReferenceNumberValid'] = true;
                        isValid = false;
                    }

                    if (!item.referenceDate) {
                        item['isReferenceDateValid'] = true;
                        isValid = false;
                    }

                    if (!item.paymentTerms) {
                        item['isPaymentTermsValid'] = true;
                        isValid = false;
                    }

                });
            }
            return isValid;
        }

        var ViewVendorDetails = function (item) {
            self.model.itemsAuctionDetailsList.items.find(function (x) {
                if (x.id == item.id) {
                    self.model.itemForVendorDetail = item.vendorDetails;
                }
            });
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var SaveItemsForAuction = function () {
            // if (true) {
            var URI = CONSTANTS.ASSET.ITEMS_FOR_AUCTION.SAVE_API;

            var data = {
                "id": self.model.itemId,
                "statusId": SetStatus('Pending'),
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId,
                "items": []
            }

            self.model.itemsAuctionDetailsList.items.forEach(function (item) {
                var vendorDetails = [];
                item.vendorDetails.forEach(function (data) {
                    vendorDetails.push({
                        "vendorId": data.vendorId,
                        "vendorName": data.vendorName,
                        "vendorAmount": parseFloat(data.vendorAmount),
                        "paymentTerms": data.paymentTerms,
                        "referenceNumber": data.referenceNumber,
                        "referenceDate": data.referenceDate,
                        "referenceDate": moment(data.referenceDate).format('YYYY-MM-DD')
                    });

                })

                var item = {
                    "id": item.id,
                    "statusId": SetStatus('Pending'),
                    "vendorDetails": vendorDetails
                }
                data.items.push(item);
            });
            // console.log(data);
            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    NavigateToItemsForAuctionDetails();
                    growl.success('Items for auction successfully saved');
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Error while saving items for auction ');
                });
            // }
        }

        var NavigateToItemsForAuctionDetails = function () {

            $state.go('itemsForAuction');

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("itemsAuctionDetails", {
            url: "/itemsAuctionDetails",
            templateUrl: 'views/Asset Management/items-for-auction-details.html',
            controller: "ItemsAuctionDetails.Controller",
            controllerAs: "vm",
            title: 'Items For Auction'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("ItemsAuctionDetails.Controller", itemsAuctionDetailsController)
})();