(function () {
    "use strict";

    function stockAdjustmentController($scope, $rootScope, $http, StatusService, cancelReasonService, $state, commonDetailService, $cookies, CONSTANTS, GenericService, growl,reportService,PaginationService) {
        var self = this;
        var uploadId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                statuses: [{
                    "status": "All Status"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                   maxDate: new Date(),
                   minDate : moment(new Date()).subtract(180, 'days').toDate()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                   // maxDate: new Date() 
                },
                fromDate: new Date(),
                toDate: new Date(),
                SelectedRequestIds: [],
                selectAllRows: false,
                
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                  }
            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });

            self.model.PopulateDropdown = PopulateDropdown;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.AddStockAdjustment = AddStockAdjustment;
            self.model.StockAdjustmentSearch = StockAdjustmentSearch;
            self.model.SelectUploadRequest = SelectUploadRequest;
            self.model.isDisable = isDisable;
            self.model.isSelected = isSelected;
            self.model.print = print;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.UploadStockDetails = UploadStockDetails;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.ApproveUploadStock = ApproveUploadStock;
            self.model.setStatus = setStatus;
            // self.model.EditUploadStock = EditUploadStock;

            PopulateDropdown();
            StockAdjustmentSearch();
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
            var upload = self.model;
            upload.status = upload.statuses[0];
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                upload.statuses = upload.statuses.concat(response.data.status);
                $rootScope.stopLoader();
                // GetStoreIndents();
            },
            function (err) {
                $rootScope.stopLoader();
            });
        }

        var StockAdjustmentSearch = function () {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "unitId": $scope.unitId
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoader();
                for (var i = 0; i < response.data.length; i++) {
                    response.data[i]["isChecked"] = false;
                    if(response.data[i].status=="Cancelled")
                    	{
                    	 response.data[i].approvalStatus="Cancelled";
                    	}
                }
                self.model.pagination.data=[];
                self.model.stockAdjustmentData = response.data;
                if(!!response && !!response.data && response.data.length > 0){            
                    PaginationService.searchData(self.model.stockAdjustmentData, self.model.pagination);
                }
            },
            function errorCallback(response) {
                $rootScope.stopLoader();
                growl.error('Error');
            });
        }

        var AddStockAdjustment = function () {
            commonDetailService.setDataId(null);
            $state.go('detailsStockAdjustment');
        }

        // var EditUploadStock = function (upload) {
        //     commonDetailService.setDataId({ "id": upload.id });
        //     $state.go('detailsStockAdjustment');
        // }

        var SelectUploadRequest = function (upload) {
            var uploadIds = self.model.SelectedRequestIds;
            $scope.uploadId = upload.id;
            if (upload.isSelected) {
                uploadIds.push(upload.id);
            } else {
                var index = uploadIds.indexOf(upload.id);
                if (index > -1) {
                    uploadIds.splice(index, 1);
                }
            }

            if (self.model.stockAdjustmentData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.stockAdjustmentData.forEach(function (upload) {
                if (upload.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        
        var isDisable = function (upload) {
            if (upload.status != 'New') {
                return true;
            }
            return false;
        }

        var isSelected = function (upload) {
            if (!isDisable(upload))
                return upload.isSelected;
            else
                return true;
        }

        var print = function (upload) {
		    //$scope.save();

		    var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                //"reportDesignFilePath": "get_uploadStock.rpt",
                "reportDesignFilePath": "get_Upload_stock_Approval.rpt",
                "reportFormat": "PDF",
                "exportFileName": "Certificate",
                "reportParams": [
                    {
                    "paramName": "id",
                    "paramType": "Integer",
                    "paramValue": upload.id

                    }
                ]
		    };
		    reportService.generate(REPORT_API_URI1,data);
        }

        var ValidateAndUpdate = function (action, popupName, id) {
           $scope.uploadId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var UploadStockDetails = function (upload,flag) {
            commonDetailService.setDataId({ "id": upload.id, "isDetail": true });
            commonDetailService.setDataFlag({"flag":flag,"isDetail":true});
            $state.go('detailsStockAdjustment');
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.stockAdjustmentData.forEach(function (upload) {
                    if (!isDisable(upload)) {
                        upload.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(upload.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.stockAdjustmentData.forEach(function (upload) {
                    upload.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        } 

        var ApproveUploadStock = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.APPROVE_API;
                var data = {
                    "uploadStockId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "userId": $scope.userId,
                    "unitId": $scope.unitId
                };
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        self.model.isItemChecked = false;
                        StockAdjustmentSearch();
                        self.model.selectAllRows = false;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                	var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.CANCEL_API + $scope.uploadId;
                   

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "approvalStatusId": setStatus('Cancelled'),
                        "userId": $scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            StockAdjustmentSearch();
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

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("stockAdjustment", {
            url: "/stockAdjustment",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/stock-adjustment.html',
            controller: "StockAdjustment.Controller",
            title: 'Stock Adjustment',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 1032);
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
        .controller("StockAdjustment.Controller", stockAdjustmentController);
})(); 