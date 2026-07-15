
(function () {
    "use strict";

    function implantIndentController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService, screenDataService) {
        var self = this;
        var indentId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    searchDataByField:PaginationService.searchDataByField,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                  },
                fromDate: new Date(),
                toDate: new Date(),
                SetFirstTabLevel: 1,
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },

                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                selectAllRows: false,
                SelectedRequestIds: [],
                statusDataList: [{
                    "status": "All"
                }],
                priorityDataList: [{
                    "priority": "All"
                }],
                indentTypeDataList: [{
                    "type": "All"
                }],
                storeList: [{
                    "store": "All"
                }],
                TypeList: [{
                    "type": "All"
                }],

            };

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ValidateDate = ValidateDate;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetImplantIndent = GetImplantIndent;
            self.model.NavigateToAddNew = NavigateToAddNew;
            self.model.NavigateToEdit = NavigateToEdit;
            self.model.NavigateToDetails = NavigateToDetails;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectImplantIndent = SelectImplantIndent;
            self.model.ApproveIndent = ApproveIndent;
            self.model.isDisable = isDisable;
            self.model.ModalDismiss=ModalDismiss;

            PopulateDropdown();
        }

        cancelReasonService.GetReason().then(function (reason) {
            self.model.reasons = reason;
          });

          StatusService.GetStatus().then(function (status) {
            self.model.statuselect = status;
          });
          
        var PopulateDropdown = function () {

            var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.status = self.model.statusDataList[0];
            self.model.store = self.model.storeList[0];
            self.model.priority = self.model.priorityDataList[0];
            self.model.indentType = self.model.indentTypeDataList[0];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                    self.model.storeList = self.model.storeList.concat(response.data.toStore);
                    self.model.indentTypeDataList = self.model.indentTypeDataList.concat(response.data.indentType);
                    $rootScope.stopLoader();
                    GetImplantIndent();
                });
        }

        var GetImplantIndent = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": !!self.model.indentType && !!self.model.indentType.id ? self.model.indentType.id : undefined,
                    "implantIndentNo": !!self.model.indentNumber && !!self.model.indentNumber ? self.model.indentNumber : undefined,
                    "unitId":$scope.unitId,
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.pagination.totalPages=0;
                        self.model.implantIndentList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.implantIndentList, self.model.pagination);
                          }
                        self.model.SelectedRequestIds = [];
                        self.model.selectAllRows = false;
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    }
                );

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


        var ValidateAndUpdate = function (action, popupName, id) {
            indentId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            } else if (self.model.fromDate == self.model.toDate) {
                isValid = true;
            }

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var NavigateToAddNew = function () {
            $state.go('addNewImplantIndent');
        }

        var NavigateToDetails = function (indent) {
            commonDetailService.setDataId(indent.id);
            $state.go('detailsImplantIndent');
        }

        var NavigateToEdit = function (indent) {
            commonDetailService.setDataId(indent.id);
            $state.go('addNewImplantIndent');
        }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.implantIndentList.forEach(function (datum) {
                    if (!isDisable(datum)) {
                        datum.isSelected = true;
                        self.model.SelectedRequestIds.push(datum.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.implantIndentList.forEach(function (datum) {
                    datum.isSelected = false;
                });
            }
        }

        var isSelected = function (indent) {
            if (!isDisable(indent))
                return indent.isSelected;
            else
                return true;
        }

        var SelectImplantIndent = function (indent) {
            var indentImplantIds = self.model.SelectedRequestIds;

            if (indent.isSelected) {
                indentImplantIds.push(indent.id);
            } else {
                var index = indentImplantIds.indexOf(indent.id);
                if (index > -1) {
                    indentImplantIds.splice(index, 1);
                }
            }

            if (self.model.implantIndentList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
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

        var isDisable = function (datum) {
            if (datum.status != 'New') {
                return true;
            }
            return false;
        }

        var ApproveIndent = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.APPROVE_API;

                var data = {
                    "impIndId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "unitId":$scope.unitId,
                    "userId":$scope.userId,
                    "organizationId":$scope.organizationId,
                    "approvalStatusId": setStatus('Pending Approval')
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        GetImplantIndent();

                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.CANCEL_API + indentId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "unitId":$scope.unitId,
                        "statusId": setStatus('Cancelled')
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('cancelImplantIndent');
                            GetImplantIndent();
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

        initializeController();
    }



    function config($stateProvider) {
        $stateProvider.state("implantIndent", {
            url: '/implantIndent',
            templateUrl: 'views/master/TheatreManagement/implant-indent.html',
            controller: "ImplantIndent.Controller",
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 326);
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
        .controller("ImplantIndent.Controller", implantIndentController)


})();