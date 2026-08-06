(function () {
    "use strict";

    function stopTransactionController($scope, $rootScope, $http, CONSTANTS, commonDetailService,PaginationService, StatusService, cancelReasonService, $state, GenericService, growl,reportService,$cookies,$filter) {
        var self = this;
        var stopTransactionId;
        var StockInTransit = 0;       
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
                stores: [{
                    "store": "All Stores"
                }],
                storesPopup: [{
                    "store": "Select Store"
                }],
                statuses: [{
                    "status": "All Status"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                   // maxDate: new Date() 
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
                startDateTime: new Date(),
                startDateTimeOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    minDate: new Date(),
                  //  maxDate: new Date() 
                },

                stopDateTime: new Date(),
               
                stopDateTimeOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                  //  maxDate: new Date()
                },
                startTime: $filter('date')( new Date(), 'HH:mm'),
                stopTime: $filter('date')( new Date(), 'HH:mm')
            };
         
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
	 	$scope.storeId = selectedTopStore != null ? selectedTopStore.storeId : undefined; // store null -> undefined (the calls gate on storeReady)       
	 
	 	 self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.OpenStopDateTime = OpenStopDateTime;
            self.model.OpenStartDateTime = OpenStartDateTime;
            self.model.dateTimeCheck = dateTimeCheck;
            self.model.StopTransactionSearch = StopTransactionSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectTransaction = SelectTransaction;
            self.model.SaveStopTransaction = SaveStopTransaction;
            self.model.openStopTransaction = openStopTransaction;
            self.model.StopTransactionDetails = StopTransactionDetails;
            self.model.ApproveStopTransaction = ApproveStopTransaction;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.isDisable = isDisable;
            self.model.openAddnew = openAddnew;
            self.model.print = print;
            self.model.sendAlert = sendAlert;
            PopulateDropdown();
            StopTransactionSearch();
            disabledPastTime();   
            self.model.CheckStopTransactionDetail = CheckStopTransactionDetail;
         }
        
        function disabledPastTime(){         
            var now = new Date();
            var hr = now.getHours();
            var hours = [];
            for(var i=1; i <= hr;i++){
            	hours.push(i);
           }     	
     
        	$('#startClockpicker').clockpicker({
        		afterShow: function() {  
        		    $(".clockpicker-hours").find(".clockpicker-tick").filter(function(index,element){
        		      /*var elementValue = parseInt($(element).text());
        		      hours.forEach(function(value){
          		       if(elementValue == value){
          		       	 $(element).addClass('disabled');
          		       }
          		      })*/
        		    	
        		    	/* start code for resolved HIS-10135 stop transction next date time */
        		    	if(self.model.startDateTime > new Date()){
            		    	 $(element).removeClass('disabled');
            		       }else{
            		    	   var elementValue = parseInt($(element).text());
                 		      hours.forEach(function(value){
                   		       if(elementValue == value){
                   		       	 $(element).addClass('disabled');
                   		       }
                   		      })
            		       }
        		    	/* end code for resolved HIS-10135 stop transction next date time */
        		    	
        		    });
        		  },	
        	}); 
        	
        	$('#endClockpicker').clockpicker({
        		afterShow: function() {  
        		    $(".clockpicker-hours").find(".clockpicker-tick").filter(function(index,element){        		    
        		    	 $(element).removeClass('disabled');
        		    });
        		  },	
        	}); 
        }        
     
        var openAddnew = function () {
            self.model.startDateTime = new Date();
            self.model.stopDateTime = new Date();
            $scope.startTime=$filter('date')( new Date(), 'HH:mm');
            $scope.stopTime= $filter('date')( new Date(), 'HH:mm');
            self.model.storePopup = self.model.storesPopup[0];            
            try{               
    		 	self.model.storePopup  =self.model.storesPopup.filter(x=>x.id==$scope.storeId)[0];
            }catch (e) {
                console.log("error!",e);          
            }            
            $("#addNewTransaction").modal()
        }
        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        
           var validateAddNew = function () {
            var isValid = true;
            var startTimePicker = $('#startTimePicker').val(); 
            var endTimePicker = $('#endTimePicker').val();
            
            var starDate = (self.model.startDateTime).toUTCString();
            var endDate = (self.model.stopDateTime).toUTCString();

            
            if (!self.model.storePopup.id) {
                growl.error('Please Select Store');
                isValid = false;
            }
            else if (!self.model.startDateTime || !self.model.stopDateTime) {
                growl.error('Enter Valid Start Date Time & Stop Date Time');
                isValid = false;
            }
            else if(startTimePicker =="" || endTimePicker ==""){ 
            	   growl.error('Enter Valid Start Time & Stop Time');
                   isValid = false;
            }  
            else if(starDate > endDate) {
            	growl.error('Enter Valid Start Date and Stop Date');
                isValid = false;
            }
            else if(startTimePicker){
                var checkTime = startTimePicker; 
                var timeParts = checkTime.split(":"); 
                var endTimeParts =  endTimePicker.split(":");
                
                var currentTime = new Date(); 
                var hours = currentTime.getHours();
                var minutes = currentTime.getMinutes();

                /*if((parseInt(hours) >= parseInt(timeParts[0]))  && (parseInt(minutes) > parseInt(timeParts[1]) )){
                	  growl.error('Enter Valid Start Time and End Time.');
                	  isValid = false;
                }*/                
                /* start code for resolved HIS-10135 stop transction next date time */
                if((parseInt(hours) >= parseInt(timeParts[0]))  && (parseInt(minutes) > parseInt(timeParts[1])) && currentTime > self.model.startDateTime){
                	growl.error('Enter Valid Start Time and End Time.');
                	isValid = false;
                }
                /* end code for resolved HIS-10135 stop transction next date time */
                else if(starDate === endDate && parseInt(parseInt(timeParts[0])) > parseInt(endTimeParts[0])){
                 	  growl.error('Enter Valid Start and End Time.');
                  	  isValid = false;
                 }
              }         
    
            return isValid;

        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenStartDateTime = function () {
            self.model.startDateOpened = true;
            self.model.stopDateTimeOptions.minDate = self.model.startDateTime;

        }

        var OpenStopDateTime = function () {
            self.model.stopDateOpened = true;
            self.model.stopDateTimeOptions.minDate = self.model.startDateTime;
        }

        var dateTimeCheck = function (data) {
            if (!data) {
                growl.error('Enter Valid Date and Time');
                return;
            }
        }

        var PopulateDropdown = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var stransaction = self.model;
            stransaction.store = stransaction.stores[0];
            stransaction.storePopup = stransaction.storesPopup[0];
            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    stransaction.stores = stransaction.stores.concat(response.data);
                    stransaction.storesPopup = stransaction.storesPopup.concat(response.data);
                    $rootScope.stopLoader();
                    try{                        
            		 	self.model.store  =self.model.stores.filter(x=>x.id==$scope.storeId)[0];            		 
                    }catch (e) {
                        console.log("error!",e);          
                    }  
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        
       var sendAlert = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
    	   var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.SEND_ALERT +  $scope.storeId + "/" + $scope.unitId;   
    	   $rootScope.startLoader();
    	   GenericService.serviceAction("PATCH", URI, "").then(
    		 function(response){ 
                 $rootScope.stopLoader();
    			 growl.success('Stop transaction alert send successfully');    			 
    		 },
    		 function errorCallback(response) {
                 $rootScope.stopLoader();
                 growl.error('Error');
             });    	   
        }
  
        var StopTransactionSearch = function (isSearch) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.SEARCH_API;         
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "organizationId": $scope.organizationId,
                "unitId": $scope.unitId              
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
                    self.model.stopTransactionData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.stopTransactionData, self.model.pagination);
                      }
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        

        
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.stopTransactionData.forEach(function (transaction) {
                    if (!isDisable(transaction)) {
                        transaction.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(transaction.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.stopTransactionData.forEach(function (transaction) {
                    transaction.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (transaction) {
            if (!isDisable(transaction))
                return transaction.isSelected;
            else
                return true;
        }

        var isDisable = function (transaction) {
            if (transaction.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectTransaction = function (transaction) {
            var transactionIds = self.model.SelectedRequestIds;

            if (transaction.isSelected) {
                transactionIds.push(transaction.id);
            } else {
                var index = transactionIds.indexOf(transaction.id);
                if (index > -1) {
                    transactionIds.splice(index, 1);
                }
            }

            if (self.model.stopTransactionData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.stopTransactionData.forEach(function (transaction) {
                if (transaction.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        
        var SaveStopTransaction  = function (){   
            if (validateAddNew()) {
            	CheckStopTransactionExist();            	
            	}
            }

// Comment Transit Condition        
//        var SaveStopTransaction  = function (){   
//        if (validateAddNew()) {      
//     	  var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.STOCK_TRANSIT_API + "/" + self.model.storePopup.id             
//           $rootScope.startLoader();
//           GenericService.serviceAction("POST", URI, "").then(
//               function (response) {
//                   var stock = response.data; 
//                    if(stock > 0 ){                 	 	                     
//                    	 growl.error("Some stock for this store are in Transit.Please released that stock first then process for Stop Transation.");
//                     } else{
//                        	CheckStopTransactionExist();                                     		                      
//                     }
//                    $rootScope.stopLoader();      
//               },
//               function (err) {
//                   $rootScope.stopLoader();
//                   growl.error(err.data.message);                 
//               });      
//        	}        
//      }
        
        var CheckStopTransactionExist = function(){       	
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        	var isExists = false;
           /*	var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.CHECK_STOP_TRANSCTION_BY_STORE + "/" + self.model.storePopup.id;
           	
            GenericService.serviceAction("GET", URI, "").then(
                    function (response) {                    	
               if(response.data.length > 0)
            	   growl.error("Stop Transaction already exists.");  
               else
            	   ActualStopTransactionSave();   
            });  */
        	
        	//start code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
        	var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.CHECK_STOP_TRANSCTION_BY_STORE;
        	var startTimePicker = $scope.startTime; 
        	var data ={
                "storeId": self.model.storePopup.id,
                "startDateTime": moment(self.model.startDateTime).format("YYYY-MM-DD " + startTimePicker + ":00"),
                "organizationId": $scope.organizationId,
                "unitId": $scope.unitId
            }
            GenericService.serviceAction("POST", URI, data).then(
                    function (response) {                    	
               if(response.data.length > 0){
            	   growl.error("Stop Transaction already exists.");
               } else {
            	   ActualStopTransactionSave();
               }
             //end code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
            });            
            return isExists;
        }
        
        
        var ActualStopTransactionSave  = function  (){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            $rootScope.startLoaderPopup();
        	var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.SAVE_API;                
            var startTimePicker = $scope.startTime; 
            var endTimePicker = $scope.stopTime;
          
            var data = {
                "storeId": self.model.storePopup.id,
                "startDateTime": moment(self.model.startDateTime).format("YYYY-MM-DD " + startTimePicker + ":00"),
                "stopDateTime": moment(self.model.stopDateTime).format("YYYY-MM-DD " + endTimePicker + ":00"),
                "statusId": 1,
                "organizationId": $scope.organizationId,
                "unitId": $scope.unitId,
                "userId": $scope.userId
            };
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                // ClearRequests();
                // NavigteToBack();
                growl.success(response.data.message);
                ModalDismiss('addNewTransaction');
                $rootScope.stopLoaderPopup();
                StopTransactionSearch();
            },
            function (err) {
                $rootScope.stopLoaderPopup();
                growl.error('Something Went Wrong');
            });            
        }        

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var openStopTransaction = function (transaction) {
            commonDetailService.setDataId({"id": transaction.id}); 
            $state.go('addNewStopTransaction');
        }

        var StopTransactionDetails = function (transaction) {
            commonDetailService.setDataId({ "id": transaction.id, "isDetail": true });
            $state.go('addNewStopTransaction');
        }
        
        var print = function (transaction){
         	 
         	 var data = {
  					    "appName": "HIS",
  					    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
  					    "reportDesignFilePath": "get_StopTransactionRpt.rpt",
  					    "reportFormat": "PDF",
  					    "exportFileName": "Certificate",
  					    "reportParams": [
  					    {
  					    "paramName": "id",
  					    "paramType": "Integer",
  					    "paramValue": transaction.id

  					    }
  					    ]
  					    };
  					    reportService.generate(REPORT_API_URI1,data);
          }


        var ApproveStopTransaction = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.APPROVE_API;

                var data = {
                    "stopTransactionId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "userId":$scope.userId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        self.model.isItemChecked  = false;
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        StopTransactionSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.CANCEL_API + stopTransactionId;

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
                            StopTransactionSearch();
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

        var ValidateAndUpdate = function (action, popupName, id) {
            stopTransactionId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        /* start code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
        var CheckStopTransactionDetail = function (){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        	
        	var data = {
                    "stopTransactionId": self.model.SelectedRequestIds
                };
        	
        	var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.CHECK_STOP_TRANSCTION_DETAIL_API;
        	
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    let stoptransactionIdForDetail = response.data;                    
                    let stoptransactionIdWithoutDetail = self.model.SelectedRequestIds.filter(stoptransactionId => !stoptransactionIdForDetail.includes(stoptransactionId));
                    
                    if (stoptransactionIdWithoutDetail.length > 0){
                    	angular.forEach (self.model.stopTransactionData,function(stopTransaction){                    		
                    		let warningFlag = stoptransactionIdWithoutDetail.includes(stopTransaction.id);
                    		if(warningFlag){
                    			growl.warning("Please add detail for " + stopTransaction.requestNo);
                    		}
                    	})
                    }else{                    	
                    	ApproveStopTransaction();
                    }
                    
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.msg);
                });
        }
        /* end code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("stopTransaction", {
            url: "/stopTransaction",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/stop-transaction.html',
            controller: "StopTransaction.Controller",
            title: 'Stop Transaction',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 146);
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
        .controller("StopTransaction.Controller", stopTransactionController);
})();
