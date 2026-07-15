(function () {
  "use strict";

  function storeIndentingController($scope, $rootScope, $http, $cookies, CONSTANTS, StatusService, screenDataService, GenericService,PaginationService, cancelReasonService, commonDetailService, $state, $filter, growl, reportService) {
    var self = this;
    var itemId;
    var indentId;
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
        selectItemList: [],
        requestData: [],
        selectAllRows: false,
        SelectedRequestIds: [],

        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        fromDate: new Date(),
        toDate: new Date(),
        fromStores: [{
          "store": "All Stores"
        }],
        toStores: [{
          "store": "All Stores"
        }],
        statuses: [{
          "status": "All"
        }],
        indentTypes: [{
          "type": "All"
        }],
        searchStoreIndentData: undefined

      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuselect = status;
      });

      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.StoreIndentData = StoreIndentData;
      self.model.SelectStoreIndent = SelectStoreIndent;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.isSelected = isSelected;
      self.model.isDisable = isDisable;
      self.model.getClassForColor = getClassForColor;
      self.model.ApproveIndent = ApproveIndent;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.EditStoreIndentDetails = EditStoreIndentDetails;
      self.model.StoreIndentingDetails = StoreIndentingDetails;
      self.model.AddStoreIndentDetails = AddStoreIndentDetails;
      /**Chetana*/
      self.model.PrintStoreIndentReport=PrintStoreIndentReport;
      /***/
      PopulateDropdown();
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        StoreIndentData()
      }
      else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'Store Indent') {

            var requestData = dataobj.searchrequest;
            self.model.fromStore = self.model.fromStores.find(function (fromStore) {
              return $scope.storeId == fromStore.id;
            });
            self.model.toStore = self.model.toStores.find(function (toStore) {
              return requestData.toStore.id == toStore.id;
            });
            self.model.indentType = self.model.indentTypes.find(function (indentType) {
              return requestData.indentType.id == indentType.id;
            });
            self.model.status = self.model.statuses.find(function (status) {
              return requestData.status.id == status.id;
            });
            self.model.indentNumber = requestData.indentNo
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);

          }
        });
        StoreIndentData();
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
      indent.indentType = indent.indentTypes[0];
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
      $rootScope.startLoader();

      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.fromStores = indent.fromStores.concat(response.data.fromStore);
          indent.toStores = indent.toStores.concat(response.data.toStore);
          indent.statuses = indent.statuses.concat(response.data.status);
          indent.indentTypes = indent.indentTypes.concat(response.data.indentType);
         
          self.model.fromStore = indent.fromStores.find(function (fromStore) {
              return $scope.storeId == fromStore.id;
            });
          initialCall();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ValidateDate = function () {
      var isValid = true;
      var patt = new RegExp(/^([0-2][0-9]|(3)[0-1])(\/)(((0)[0-9])|((1)[0-2]))(\/)\d{4}$/i);

   
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please Enter Date');
        isValid = false;
      }
      else if(patt.test(self.model.fromDate) || patt.test(self.model.toDate)){
        growl.error('Please Enter Valid Date');

        isValid = false;
      }
      else if (self.model.fromDate > self.model.toDate) {
        growl.error('From Date should lesser than To Date');
        isValid = false;
      }
       
      return isValid;
    }

    var StoreIndentData = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.SEARCH_API;
        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
          "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
          "indentNo": self.model.indentNumber ? self.model.indentNumber : undefined,
          "statusId": self.model.status.id ? self.model.status.id : undefined,
          "indentTypeId": self.model.indentType ? self.model.indentType.id : undefined,
          "unitId":$scope.unitId,
          "userId":$scope.userId

        };
        $rootScope.startLoader()
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            for (var i = 0; i < response.data.length; i++) {
              response.data[i]["isChecked"] = false;
            }
            self.model.pagination.data=[];
            self.model.searchStoreIndentData = response.data;
            console.log(" self.model.searchStoreIndentData",  self.model.searchStoreIndentData);
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.searchStoreIndentData, self.model.pagination);
            }
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            $rootScope.stopLoader();
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
            "status": self.model.status,
            "indentType": self.model.indentType,
          },
          screenName: 'Store Indent',
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var SelectAllRequests = function () {
    	$scope.saveBtnDisabled=false;
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.searchStoreIndentData.forEach(function (indent) {
          if (!isDisable(indent)) {
            indent.isSelected = true;
            self.model.isItemChecked = true;
            self.model.SelectedRequestIds.push(indent.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.searchStoreIndentData.forEach(function (indent) {
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

    var SelectStoreIndent = function (indent) {
      var indentIds = self.model.SelectedRequestIds;
      $scope.saveBtnDisabled=false;
      if (indent.isSelected) {
        indentIds.push(indent.id);
      } else {
        var index = indentIds.indexOf(indent.id);
        if (index > -1) {
          indentIds.splice(index, 1);
        }
      }

      if (self.model.searchStoreIndentData.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
      self.model.isItemChecked = false;
      self.model.searchStoreIndentData.forEach(function (indent) {
        if (indent.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });
    }

    $scope.saveBtnDisabled=false;
    var ApproveIndent = function () {
        $scope.saveBtnDisabled=true;
      if (self.model.action == 'Update') {
        var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.APPROVE_API;

        var data = {
          "indentId": self.model.SelectedRequestIds,
          "statusId": setStatus('Pending'),
          "approvalStatusId": setStatus('Pending Approval'),
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationid,
          "userId" : $scope.userId
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.SelectedRequestIds = [];
            $rootScope.stopLoader();
            growl.success(response.data.message);
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            ModalDismiss('confirmLogout1');
            StoreIndentData();

          },
          function (err) {
            $rootScope.stopLoader();
            if(!!err && !!err.data && !!err.data.summary){
              growl.error(err.data.summary);
            } else{
              growl.error('Something Went Wrong');
            }
          });
      } else if (self.model.action == 'Cancel') {
        if (ValidateCancelation()) {
          var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.CANCEL_API + indentId;

          var data = {
            "cancelReasonId": self.model.cancelReason,
            "cancelNote": self.model.cancelNote,
            "statusId":8,
            "approvalStatusId": 9,
            "userId" : $scope.userId
          };

          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              $rootScope.stopLoader();
              growl.success(response.data.message);
              ModalDismiss('confirmCancel');
              StoreIndentData();
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
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
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


    var AddStoreIndentDetails = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewStoreIndenting');
    }

    var EditStoreIndentDetails = function (indent) {
      commonDetailService.setDataId(indent.id);
      $state.go('addNewStoreIndenting');
    }

    var StoreIndentingDetails = function (indent) {
      commonDetailService.setDataId(indent.id);
      $state.go('DetailsStoreIndenting');
    }
/***Chetana***/
    var PrintStoreIndentReport= function(indent){
    	 var data = {
    		        "appName": "HIS",
    		        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
    		        "reportDesignFilePath":  "get_Store_Indent.rpt",
    		        "reportFormat": "PDF",
    		        "exportFileName": "IndentReport",
    		        "reportParams": [
    		          {
    		            "paramName": "StoreIndent_id",
    		            "paramType": "Integer",
    		            "paramValue": indent.id
    		          }
    		        ]
    		      };
    		      reportService.generate(REPORT_API_URI1, data);
    }
    /******/
    initializeController();
  }
  
  /*----------------------------------------  for changing color of forced cancel list-----------------------------------------*/
  var getClassForColor = function(info)
  {
      	   if(info.forceClose == true){
       		return 'td-bg-red';
      	   }
  }
  

  function config($stateProvider) {
    $stateProvider.state("storeIndenting", {
      url: "/storeIndenting",
      templateUrl: 'views/PharmacyInventory/InPatient/store-indenting.html',
      controller: "StoreIndenting.Controller",
      title: 'Store Indent',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 124);
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
    .controller("StoreIndenting.Controller", storeIndentingController)
})();