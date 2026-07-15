(function () {
    "use strict";


    function scrapRequestApprovalController($scope, $rootScope, $state, commonDetailService,PaginationService,StatusService,cancelReasonService, screenDataService, $http, $cookies, CONSTANTS, GenericService, growl,reportService) {
        var self = this;
        var scrapId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
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
                stores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All"
                }],
                assetTypes: [{
                    "type": "All"
                }],
                scrapTypes: [
                    { "id": 2, "type": " Auction " },
                    {  "id": 1, "type": " Scrap " }
                   ],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : moment(new Date()).subtract(180, 'days').toDate()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()},            
                fromDate: new Date(),
                toDate: new Date(),
                scrapApprovalData: undefined
            };
            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.ScrapRequestApprovalSearch = ScrapRequestApprovalSearch;
            self.model.ScrapApprovalDetails = ScrapApprovalDetails;
            self.model.Print = Print;
            self.model.isDisable = isDisable;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ApproveScrapRequest = ApproveScrapRequest;
            self.model.setStatus = setStatus;

            PopulateDropdown();
        }
        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                ScrapRequestApprovalSearch()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Scrap Request Approval') {

                        var requestData = dataobj.searchrequest;

                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                        self.model.store = self.model.stores.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });

                    }
                });
                ScrapRequestApprovalSearch();
            }

        }


        var ScrapRequestApprovalSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST_APPROVAL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id,
                    "scrapType": self.model.scrapFlag == undefined ? undefined : self.model.scrapFlag.id,
                    "unitId": $scope.unitId
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.pagination.data=[];
                        self.model.scrapApprovalData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.scrapApprovalData, self.model.pagination);
                          }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "store": self.model.store,
                        "status": self.model.status

                    },
                    screenName: 'Scrap Request Approval',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        
        var Print = function (scrap){
       	 
       	 var data = {
					    "appName": "HIS",
					    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					    "reportDesignFilePath": "get_Scrap_Request.rpt",
					    "reportFormat": "PDF",
					    "exportFileName": "Certificate",
					    "reportParams": [
					    {
					    "paramName": "id",
					    "paramType": "Integer",
					    "paramValue": scrap.id

					    }
					    ]
					    };
					    reportService.generate(REPORT_API_URI1,data);
        }
        
        var PopulateDropdown = function () {
            var scrap = self.model;
            scrap.store = scrap.stores[0];
            scrap.status = scrap.statuses[0];
            scrap.assetType = scrap.assetTypes[0];
            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST_APPROVAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    scrap.stores = scrap.stores.concat(response.data.store);
                    scrap.statuses = scrap.statuses.concat(response.data.status);
                    scrap.assetTypes = scrap.assetTypes.concat(response.data.assetType);
                    initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var isDisable = function (scrap) {
        	if (scrap.status == 'Approved' || scrap.status == 'Completed' ||  scrap.status == 'Cancelled' || scrap.approvalStatus == 'Approved') {
                return true;
            }
            return false;
        }
        var ValidateAndUpdate = function (action, popupName, id) {
            self.model.cancelReason=  undefined;
            self.model.cancelNote = '';
            scrapId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }
        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        var ScrapApprovalDetails = function (scrap) {
            commonDetailService.setDataId(scrap.id);
            $state.go('detailsApprovalScrapRequest');
        }
        var ApproveScrapRequest = function () {
            if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST_APPROVAL.CANCEL_API + scrapId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId": $scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            var popup = angular.element('#confirmCancel');
                            popup.modal('hide');
                            ScrapRequestApprovalSearch();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.message);
                        });
                }
            }
        }

        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("approvalScrapRequest", {
            url: "/approvalScrapRequest",
            templateUrl: 'views/PharmacyInventory/Store/approved-scrap-request.html',
            controller: "ScrapRequestApproval.Controller",
            title: 'Scrap Request Approval',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 157);
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
        .controller("ScrapRequestApproval.Controller", scrapRequestApprovalController);
})();
