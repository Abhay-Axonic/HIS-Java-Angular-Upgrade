(function () {
    "use strict";

    function linenIndentController($scope, $rootScope, $http, $cookies, CONSTANTS, StatusService, screenDataService, GenericService, cancelReasonService, commonDetailService, $state, $filter, growl) {
        var self = this;
        var indentId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            self.model = {
                selectItemList: [],
                requestData: [],
                selectAllRows: false,
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
                SelectedRequestIds: [],
                fromStores: [{
                    "store": "All Stores"
                }],
                toStores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All"
                }]

            };

            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.LinenIndentData = LinenIndentData;
            self.model.SelectLinenIndent = SelectLinenIndent;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ApproveIndent = ApproveIndent;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.EditLinenIndentDetails = EditLinenIndentDetails;
            self.model.LinenIndentingDetails = LinenIndentingDetails;
            self.model.AddLinenIndentDetails = AddLinenIndentDetails;

            PopulateDropdown();
            LinenIndentData();
        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                LinenIndentData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Store Indent')
                        var requestData = dataobj.searchrequest;
                    self.model.fromStoreId = self.model.fromStores.find(function (fromStore) {
                        return requestData.fromStore.id == fromStore.id;
                    });
                    self.model.toStoreId = self.model.toStores.find(function (toStore) {
                        return requestData.toStore.id == toStore.id;
                    });
                    self.model.indentNumber = requestData.indentNo
                    self.model.fromDate = new Date(requestData.fromDate);
                    self.model.toDate = new Date(requestData.toDate);
                });

                LinenIndentData();
            }

        }
        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var PopulateDropdown = function () {
            var indent = self.model;
            indent.fromStore = indent.fromStores[0];
            indent.toStore = indent.toStores[0];
            indent.status = indent.statuses[0];
            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId+ '/'+$scope.userId;
            $rootScope.startLoader();

            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    indent.fromStores = indent.fromStores.concat(response.data.fromStore);
                    indent.toStores = indent.toStores.concat(response.data.toStore);
                    indent.statuses = indent.statuses.concat(response.data.status);
                    initialCall();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ValidateDate = function () {
            var isValid = true;

            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var LinenIndentData = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                    "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                    "indentNo": self.model.indentNumber ? self.model.indentNumber : undefined,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": self.model.indentType ? self.model.indentType.id : undefined,
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
                        self.model.searchLinenIndentData = response.data;
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });

                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "fromStore": self.model.fromStore,
                        "toStore": self.model.toStore,
                        "indentNo": self.model.indentNumber,
                        "statusId": self.model.status,
                        "indentTypeId": self.model.indentType,
                    },
                    screenName: 'Store Indent',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.searchLinenIndentData.forEach(function (indent) {
                    if (!isDisable(indent)) {
                        indent.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(indent.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.searchLinenIndentData.forEach(function (indent) {
                    indent.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (indent) {
            if (!isDisable(indent))
                return indent.isSelected;
            else
                return true;
        }

        var isDisable = function (indent) {
            if (indent.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectLinenIndent = function (indent) {
            var indentIds = self.model.SelectedRequestIds;

            if (indent.isSelected) {
                indentIds.push(indent.id);
            } else {
                var index = indentIds.indexOf(indent.id);
                if (index > -1) {
                    indentIds.splice(index, 1);
                }
            }

            if (self.model.searchLinenIndentData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.searchLinenIndentData.forEach(function (indent) {
                if (indent.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ApproveIndent = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.APPROVE_API;

                var data = {
                    "indentId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "userId":$scope.userId,
                    "approvalStatusId": setStatus('Pending Approval')
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        LinenIndentData();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.CANCEL_API + indentId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId":$scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            LinenIndentData();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }
            }

        }
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            indentId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }


        var AddLinenIndentDetails = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewLinenIndent');
        }

        var EditLinenIndentDetails = function (indent) {
            commonDetailService.setDataId(indent.id);
            $state.go('addNewLinenIndent');
        }

        var LinenIndentingDetails = function (indent) {
            commonDetailService.setDataId(indent.id);
            $state.go('linenIndentDetails');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("linenIndent", {
            url: "/linenIndent",
            templateUrl: 'views/Linen/linen-indent.html',
            controller: "LinenIndent.Controller",
            title: 'Linen Indent',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 94);
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
        .controller("LinenIndent.Controller", linenIndentController);
})();

