(function () {
  "use strict";
  var CACHE = {};

  function storeIndentApprovalController($scope, $state, $rootScope, $http, $cookies, CONSTANTS,PaginationService, screenDataService, cancelReasonService, GenericService, commonDetailService, $filter, growl, reportService) {
    var self = this;

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
        reasons:[{
          "reason":"Select Reason"
        }],
        storeIndentApprovalList : undefined
      };


      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = self.model.reasons.concat(reason);
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.SearchStoreIndent = searchStoreIndent;
      self.model.StoreIndentApproveDetails = storeIndentApproveDetails;
      self.model.CancelIndent = CancelIndent;
      self.model.PopulateDropdown = PopulateDropdown;
      self.model.isDisable = isDisable;
      self.model.isDisable1 = isDisable1;
      self.model.ModalDismiss = ModalDismiss;
      self.model.openCancelPopup = openCancelPopup;
      self.model.openForceCancelPopup = openForceCancelPopup;
      self.model.printStoreIndentApprovalReport = printStoreIndentApprovalReport;

      PopulateDropdown();
      getDropdownsList();
    }

    var PopulateDropdown = function () {
      var indent = self.model;
      indent.fromStore = indent.fromStores[0];
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
      $rootScope.startLoader();

      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.fromStores = indent.fromStores.concat(response.data.fromStore);
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

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var openCancelPopup = function (item) {
      self.model.reasonSelect = undefined;
      $("#CanellationModalForStoreIndent").modal()
      self.model.indentId = item.id;
    }
    var openForceCancelPopup = function (item) {
        self.model.reasonSelect = undefined;
        $("#CanellationModalForPartillyCompleted").modal()
        self.model.indentId = item.id;
      }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        searchStoreIndent()
      }
      else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'Store Indent Approval') {
            var requestData = dataobj.searchrequest;
            self.model.fromStore = self.model.fromStores.find(function (fromStore) {
              return requestData.fromStore.id == fromStore.id;
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
            self.model.indentNo = requestData.indentNo
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
          }
        });
        searchStoreIndent();
      }
  }

  var getDropdownsList = function () {
    var indents = self.model;
    // indents.fromStore = indents.fromStores[0];
    indents.toStore = indents.toStores[0];
    indents.status = indents.statuses[0];
    indents.indentType = indents.indentTypes[0];

    var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/' +$scope.storeId;

    $rootScope.startLoader();
    GenericService.serviceAction("GET", URI, {}).then(
      function (response) {
        // indents.fromStores = indents.fromStores.concat(response.data.fromStore);
        indents.toStores = indents.toStores.concat(response.data.toStore);
        indents.statuses = indents.statuses.concat(response.data.status);
        indents.indentTypes = indents.indentTypes.concat(response.data.indentType);

        $rootScope.stopLoader();
        initialCall();
      },
      function (err) {
        $rootScope.stopLoader();
      });
  }


  var searchStoreIndent = function () {
    if (ValidateDate()) {
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT_APPROVAL.SEARCH_API;

      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "fromStoreId": self.model.fromStore.id,
        "toStoreId": self.model.toStore.id,
        "indentNo": $scope.indentNo,
        "statusId": self.model.status.id,
        "indentTypeId": self.model.indentType.id,
        "unitId":$scope.unitId,
        "userId":$scope.userId
        

      };
      $rootScope.startLoader()
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
         // $rootScope.stopLoader();
          for (var i = 0; i < response.data.length; i++) {
            response.data[i]["isChecked"] = false;
          }
          self.model.pagination.data=[];
          self.model.storeIndentApprovalList = response.data;
         
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchDataByField(self.model.storeIndentApprovalList, self.model.pagination);
            
          }
          
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
          "indentNo": self.model.indentNo,
          "status": self.model.status,
          "indentType": self.model.indentType,

        },
        screenName: 'Store Indent Approval',
      }
     // screenDataService.setScreenData(screenData);
    }
  }

  var isDisable = function (Item) {
    if (Item.status == 'Cancelled' || Item.status == 'Closed' ) {
      return true;
    }
    if(Item.approvalStatusId == 5){
      return true;
    }
    return false;
  }
  
  var isDisable1 = function (Item) {
	    if (Item.forceClose==true || Item.statusId!=18) {
	      return true;
	    }
	    return false;
	  }
  
  
  /*-------------------------------- changing color of forced cancellation -------------------*/
  $scope.getClassForColor = function(info)
  {
      	   if(info.forceClose == true){
       		return 'td-bg-red';
      	   }
  }
 

  function CancelIndent() {
    if (ValidateRejection()) {
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT_APPROVAL.CANCEL_API + self.model.indentId;
      var data = {
        "cancelReasonId": self.model.reasonSelect,
        "cancelNote": self.model.reasonNote,
        "statusId": 9,
        "userId":$scope.userId
      };
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          self.model.reasonSelect = self.model.reasonNote = null;
          growl.success(response.data.message);
          searchStoreIndent();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });

      ModalDismiss('CanellationModalForStoreIndent');
    }
  }
 /*--------------- in case of partially completed status --> force cancel function --------------*/ 
   $scope.forceConcel = function () {
	    if (ValidateRejection()) {
	      var URI = BASE_URL + ROOT_URL + "/api/inventory/stores/indents/approval/updateStoreIndentForForceClose/" + self.model.indentId;
	      var data = {
	        "cancelReasonId": self.model.reasonSelect,
	        "cancelNote": self.model.reasonNote,
	        "statusId": 18,
	        "userId":$scope.userId
	      };
	      GenericService.serviceAction("POST", URI, data).then(
	        function (response) {
	          self.model.reasonSelect = self.model.reasonNote = null;
	         
	          if(response.status==200){
	        	  growl.success("Store Indent Approval Force Cancelled Successfully!");
	          }
	          searchStoreIndent();
	        },
	        function errorCallback(response) {
	          $rootScope.stopLoader();
	          growl.error('Error');
	          
	        });

	      ModalDismiss('CanellationModalForPartillyCompleted');
	    }
	  }

  var ValidateRejection = function () {
    var isValid = true;
    if (!self.model.reasonSelect) {
      isValid = false;
      growl.error('Please Select Reason to Cancel');
    }

    return isValid;
  }

  function storeIndentApproveDetails(id) {
    commonDetailService.setDataId(id);
    $state.go('detailsStoreIndentApproval');
  }

  var ModalDismiss = function (modal) {
    var popup = angular.element('#' + modal);
    popup.modal('hide');
  }

  
  /***Chetana**/
  var printStoreIndentApprovalReport = function (item) {
      var data = {
          "appName": "HIS",
          "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
          "reportDesignFilePath": "get_Store_Indent.rpt",
          "reportFormat": "PDF",
          "exportFileName": "StoreIndentApproval",
          "reportParams": [
              {
                  "paramName": "StoreIndent_id",
                  "paramType": "Integer",
                  "paramValue": item.id
              }
          ]
      };
      reportService.generate(REPORT_API_URI1, data);
  }
 /*****/ 
  
  initializeController();
}

  function config($stateProvider) {
  $stateProvider.state("storeIndentApproval", {
    url: "/storeIndentApproval",
    templateUrl: 'views/PharmacyInventory/InPatient/store-indent-approval.html',
    controller: "StoreIndentApproval.Controller",
    title: 'Store Indent Approval',
	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 125);
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
  .controller("StoreIndentApproval.Controller", storeIndentApprovalController)
}) ();