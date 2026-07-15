(function () {
    "use strict";

    function crashCartAdministrationApprovalController($scope, $rootScope,$cookies, $http, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService) {
        var self = this;
      
        var cookieObject = $cookies.getObject('cookieObject');
       
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};


            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });
            
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            self.model.doctorName = cookieObject.firstName;
            self.model.doctorId = cookieObject.doctorId;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.GetAdministrationApproval = GetAdministrationApproval;
            self.model.Cancel = Cancel;
            self.model.CancelItem = CancelItem;
            self.model.isDisable = isDisable;
            self.model.NavigateToDetail = NavigateToDetail;

            GetAdministrationApproval();
        }

        var SearchdoctorNames = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.DOCTOR_SEARCH_API +$scope.organizationId +"/"+$scope.unitId + "/" + search;

                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        $rootScope.stopLoader();
                        if (!!response.data && response.data.length > 0) {
                            self.model.doctorNames = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.doctorNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return DoctorNotFound(search);

                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return DoctorNotFound(search);
                    });
            } else {
                if (!!self.model.doctorNames && self.model.doctorNames.length > 0 &&
                    self.model.doctorNames[0].itemFound != undefined && !self.model.doctorNames[0].itemFound) {
                    return DoctorNotFound(search);
                } else {
                    return $filter('filter')(self.model.doctorNames, {
                        $: search
                    });
                }
            }
        }

        var DoctorNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "DOCTOR Not found with name " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var GetAdministrationApproval = function () {
            
            var URI = CONSTANTS.PHARMACY.NURSING.ADMINISTRATION_APPROVAL.SEARCH_API;

            var data = {
                "doctorId":  !!self.model.doctorId ? self.model.doctorId : undefined
               // "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined
            };

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.administrationApprovalList = response.data;
                })
            $rootScope.stopLoader();
        }

        var Cancel = function (action, popupName, id) {
            itemId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var SetStatus = function (x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });
        }

        var CancelItem = function () {
            if (self.model.action == 'Cancel') {

                var URI = CONSTANTS.PHARMACY.NURSING.ADMINISTRATION_APPROVAL.CANCEL_API + itemId;

                var data = {
                    "cancelReasonId": self.model.cancelReason,
                    "cancelNote": self.model.cancelNote,
                    "statusId": SetStatus('Cancelled')
                };

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        GetCrashCartRequest();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            }
        }

        var isDisable = function (Item) {
            if (Item.status == 'Cancelled') {
                return true;
            }
            return false;
        }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('detailsAdministrationApproval');
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("administrationApproval", {
            url: "/administrationApproval",
            templateUrl: 'views/PharmacyInventory/InPatient/crash-cart-medication-administration-approval.html',
            controller: "CrashCartAdministrationApproval.Controller",
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 253);
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
        .controller("CrashCartAdministrationApproval.Controller", crashCartAdministrationApprovalController)
})();