(function () {
    "use strict";

    function uploadStockApprovalController($scope, $rootScope, $http, StatusService, cancelReasonService, commonDetailService, $state,  CONSTANTS, GenericService, growl,$cookies,$filter,reportService,PaginationService) {

        var self = this;
        var uploadId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                stores: [{
                    "store": "All Stores"
                }],
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
                    //maxDate: new Date()
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
            
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
	 	$scope.storeId = selectedTopStore != null ? selectedTopStore.storeId : undefined; 
	 	
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.UploadStockApprovalSearch = UploadStockApprovalSearch;
            self.model.StockApprovalDetails = StockApprovalDetails;
            self.model.ApproveUploadStock = ApproveUploadStock;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.isDisable = isDisable;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectUploadStock = SelectUploadStock;
            self.model.printUploadStockApprovalReport = printUploadStockApprovalReport;
            PopulateDropdown();
            UploadStockApprovalSearch();
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var upload = self.model;
            upload.store = upload.stores[0];
            upload.status = upload.statuses[0];
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;     
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    upload.stores = upload.stores.concat(response.data.store);
                    upload.statuses = upload.statuses.concat(response.data.status);
                    try{                        
            		 	self.model.store  =self.model.stores.filter(x=>x.id==$scope.storeId)[0];            		 
                    }catch (e) {
                        console.log("error!",e);          
                    }  
                    $rootScope.stopLoader();
                    
                    // GetStoreIndents();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }


        var UploadStockApprovalSearch = function (isSearch) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id
                };
                try{
                	if(self.model.store.id  == undefined && isSearch == undefined)
                		data.storeId = $scope.storeId;
                	else if(self.model.store.id  == undefined && isSearch != undefined){
               	 			data.storeId= "";
                	}else{
                        data.storeId = self.model.store.id;            		
                	}}catch(e){} 
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        
                        self.model.pagination.data=[];
                        
                        self.model.uploadStockApprovalData = response.data;
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.uploadStockApprovalData, self.model.pagination);
                          }
                        
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
            }
        }

        var StockApprovalDetails = function (upload) {
            commonDetailService.setDataId(upload.id);
            $state.go('detailsUploadStockApproval');
        }
        var ApproveUploadStock = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.APPROVE_API;

                var data = {
                    "uploadStockId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Approved'),
                    "userId": $scope.userId ,
                    "organizationId":$scope.organizationId,
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        self.model.isItemChecked = false;
                        self.model.selectAllRows = false;
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        UploadStockApprovalSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.CANCEL_API + uploadId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId" : $scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            UploadStockApprovalSearch();
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

        var ValidateAndUpdate = function (action, popupName, id) {
            uploadId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var isDisable = function (upload) {
            if (upload.status != 'Pending' || upload.approvalStatus != 'Pending Approval') {
                return true;
            }
            return false;
        }



        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.uploadStockApprovalData.forEach(function (upload) {
                    if (!isDisable(upload)) {
                        upload.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(upload.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.uploadStockApprovalData.forEach(function (upload) {
                    upload.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (upload) {
            if (!isDisable(upload))
                return upload.isSelected;
            else
                return true;
        }

        var SelectUploadStock = function (upload) {
            var indentIds = self.model.SelectedRequestIds;

            if (upload.isSelected) {
                indentIds.push(upload.id);
            } else {
                var index = indentIds.indexOf(upload.id);
                if (index > -1) {
                    indentIds.splice(index, 1);
                }
            }

            if (self.model.uploadStockApprovalData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.uploadStockApprovalData.forEach(function (upload) {
                if (upload.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        /***Chetana**/
        var printUploadStockApprovalReport = function (upload) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Upload_stock_Approval.rpt",
                "reportFormat": "PDF",
                "exportFileName": "UploadStockApproval",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": upload.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("uploadStockApproval", {
            url: "/uploadStockApproval",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/upload-stock-approval.html',
            controller: "UploadStockApproval.Controller",
            title: 'Upload Stock Approval',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 148);
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
        .controller("UploadStockApproval.Controller", uploadStockApprovalController);
})();
