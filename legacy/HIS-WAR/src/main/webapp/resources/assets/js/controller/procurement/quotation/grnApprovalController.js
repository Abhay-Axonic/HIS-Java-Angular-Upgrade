(function () {
  "use strict";

  function GRNApprovalController($scope, $rootScope, $http, $filter, $state, screenDataService, $cookies, CONSTANTS,reportService , GenericService,PaginationService, commonDetailService, StatusService, rejectReasonService, growl, $timeout) {
    var self = this;

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
          minDate : moment(new Date()).subtract(360, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        selectAllRows: false,
        SelectedOrderIds: [],
        selectedItems: [],
        storeList: [{
          "store": "Select Store"
        }],
        fromStoreList: [{
          "store": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }],
        assetTypes: [{
          "type": "Select GRN Type"
        }],
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });
      $scope.saveButton = false;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetGRNList = GetGRNList;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.SelectAllGRN = SelectAllGRN;
      self.model.SelectGRN = SelectGRN;
      self.model.isDisable = isDisable;
      self.model.OpenPopup = OpenPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.RejectGRN = RejectGRN;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.AcceptReject = AcceptReject;
      self.model.rePrintBillSlip = rePrintBillSlip;
      self.model.selectedGRNId = 0;
      self.model.ModalDismissOnClose = ModalDismissOnClose;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.NavigateToEdit = NavigateToEdit;
      
      PopulateDropdown();
      GetGRNList();
    }

    function SearchVendorNames(search) {
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

    function nameNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor Not found with name " + search,
        "search": search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName(vendor) {
      if (!vendor.id) {
        self.model.vendorName = vendor.search;
        self.model.vendorId = undefined;
      } else {
        self.model.vendorName = vendor.vendorName;
        self.model.vendorId = vendor.id;
      }
    }

    var setStatus = function (data) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == data;
      });

      return status.id;
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        GetGRNList()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'grnApproval') {
            var requestData = dataobj.searchrequest;

            self.model.status = self.model.statuses.find(function (status) {
              return requestData.status.id == status.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
              return requestData.store.id == store.id;
            });
            self.model.fromstore = self.model.fromStoreList.find(function (store) {
              return requestData.fromstore.id == store.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
            self.model.grnNumber = requestData.grnNumber;
            self.model.grnType = self.model.assetTypes.find(function (type) {
              return requestData.grnType.id == type.id;
            });
          }
        });

        GetGRNList();
      }
    }

    var PopulateDropdown = function () {
      self.model.store = self.model.storeList[0];
      self.model.fromstore = self.model.fromStoreList[0];
      self.model.status = self.model.statuses[0];
      self.model.grnType = self.model.assetTypes[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.deliveryStores);
          self.model.fromStoreList = self.model.fromStoreList.concat(response.data.requestingStores);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);

          $rootScope.stopLoader();
          initialCall();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be less than to date');
        isValid = false;
      } else if (self.model.fromDate == self.model.toDate) {
        isValid = true;
      }

      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }
    var NavigateToEdit = function (grn) {
        commonDetailService.setDataId(grn.id);
        commonDetailService.setIsFromApprovalEdit(true);
        $state.go('addItemGRN');
      }
    var GetGRNList = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "requestingStoreId": !!self.model.fromStore && !!self.model.fromStore.id ? self.model.fromStore.id : undefined,
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "assetTypeId": self.model.grnType ? self.model.grnType.id : undefined,
          "grnNumber": self.model.grnNumber ? self.model.grnNumber : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId": $scope.unitId,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "vendorId": self.model.vendorId ? self.model.vendorId : undefined
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.grnList = response.data;
            
            self.model.grnList.forEach(function (grn) {
            	if(grn.approvalStatus=="Approved"){
                	grn.flagForBarcodePrint = false;
                }
            	else{
            		grn.flagForBarcodePrint = true;
            	}
            });
            
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.grnList, self.model.pagination);
            }
            self.model.SelectedOrderIds = [];
            self.model.selectAllRows = false;
            angular.forEach(self.model.grnList, function (item) {
              item["disableFlag"] = true;
              item['rejectReasonId'] = 1;
              item['rejectNote'] = '';
              if (item.approvalStatus == 'Reject') {
                item.isReject = true;
              }
            });
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });

        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "status": self.model.status,
            "store": self.model.store,
            "grnType": self.model.grnType,
            "grnNumber": self.model.grnNumber
          },
          screenName: 'grnApproval'
        }
        screenDataService.setScreenData(screenData);
      }
    
    }

    var NavigateToDetails = function (grn) {
      commonDetailService.setDataId(grn.id);
      $state.go('detailsGRNApproval');
    }

    var SelectGRN = function (grn) {
      var grnIds = self.model.SelectedOrderIds;
      if (grn.isSelected) {
        grn.disableFlag = false;
        grnIds.push(grn.id);
        self.model.selectedItems.push(grn);
      } else {
        grn.isReject = false;
        grn.disableFlag = true;
        var index = grnIds.indexOf(grn.id);

        if (index > -1) {
          grnIds.splice(index, 1);
        }
      }

      if (self.model.grnList.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var SelectAllGRN = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedOrderIds = [];
        self.model.grnList.forEach(function (grn) {
          if (!isDisable(grn)) {
            grn.isSelected = true;
            grn.disableFlag = false;
            self.model.SelectedOrderIds.push(grn.id);
          }
        });
      } else {
        self.model.SelectedOrderIds = [];
        self.model.grnList.forEach(function (grn) {
          grn.isSelected = false;
          grn.disableFlag = true;
          grn.isReject = false;
        });
      }
    }

    var isDisable = function (grn) {
      if (grn.status == 'Closed' || grn.status == 'Approved' || grn.approvalStatus == 'Approved' || grn.approvalStatus == 'Partially Approved') {
        return true;
      }
      return false;
    }

    var isSelected = function (grn) {
      if (!isDisable(grn))
        return grn.isSelected;
      else
        return true;
    }

    var OpenPopup = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.id = data.id;
      popup.modal('show');
    }
    
   $scope.OpenBarcodePopup = function (popupName, data) {
        var popup = angular.element('#' + popupName);
        $scope.grn = data;
        popup.modal('show');
      }

    var RejectGRN = function (action, popupName, data) {
      self.model.selectedGRNId = data.poId;
      self.model.itemTrueFalse = data;
      if (data.isReject) {
        self.model.action = action;
        var popup = angular.element('#' + popupName);
        popup.modal('show');
      }
    }

    var AcceptReject = function () {
      if (ValidetReason()) {
        self.model.grnList.forEach(function (grn) {
          if (grn.poId == self.model.selectedGRNId) {
            grn.rejectReasonId = self.model.cancelReason;
            grn.rejectNote = self.model.rejectNote;
          }
        });
        ModalDismiss("RejectGRN");
      }
    }

    var ValidateApproval = function () {
      var isValid = true;
      if (!self.model.SelectedOrderIds.length) {
        growl.error('Please select GRN to approve');
        isValid = false;
      }

      return isValid;
    }

    var SaveAcceptReject = function () {
    	 $scope.saveButton = true;
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.APPROVE_API;

        var data = [];
        self.model.grnList.forEach(function (grn) {
          if (grn.isSelected) {
            if (grn.isReject) {
              data.push({
                "grnId": grn.id,
                "statusId": setStatus('Closed'),
                "approvalStatusId": setStatus('Rejected'),
                "rejectReasonId": grn.rejectReasonId,
                "rejectNote": grn.rejectNote,
                "userId" :cookieObject.userId,
                "organizationId" :cookieObject.organizationId,
                "unitId" :cookieObject.unitId
              });
            }
            else {
              data.push({
                "grnId": grn.id,
                "statusId": setStatus('Pending'),
                "approvalStatusId": setStatus('Approved'),
                "rejectReasonId": self.model.reasons[0].id,
                "rejectNote": null,
                "userId" :cookieObject.userId,
                "organizationId" :cookieObject.organizationId,
                "unitId" :cookieObject.unitId
              });
            }
          }
        });
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.SelectedOrderIds = [];
            ModalDismiss('confirmSave');
            $rootScope.stopLoader();
            GetGRNList();
            self.model.selectAllRows = false;
            $scope.saveButton = false;
            growl.success(response.data.message);
          },
          function (err) {
            ModalDismiss('confirmSave');
            $rootScope.stopLoader();
            $scope.saveButton = false;
            growl.error(err.data.message);
          }
        );
      }
    }

    var ValidateStatus = function () {
      var isValid = true;
      if (!self.model.SelectedOrderIds.length) {
        isValid = false;
      }
      return isValid;
    }

    var ValidetReason = function () {
      var isValid = true;

      if (!self.model.cancelReason) {
        self.model.isReasonValid = true;
        isValid = false;
      }

      return isValid;
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }
    var ModalDismissOnClose = function(popupName){
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      var test = self.model.itemTrueFalse;
      self.model.itemTrueFalse.isReject= false;
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }

    var rePrintBillSlip = function (grn) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_GRN_listview.rpt",
        "reportFormat": "PDF",
        "exportFileName": "Grn",
        "reportParams": [
          {
            "paramName": "Id",
            "paramType": "Integer",
            "paramValue": grn.id
          },
          {
            "paramName": "Storeid",
            "paramType": "Integer",
            "paramValue": grn.storeId
          }
        ]
      };
      reportService.generate(REPORT_API_URI1, data);
    }
    
    /*-------Batchwise barcode printing start -----------*/
    
    $scope.GRNItemsForBarcode = [];
    $scope.printCounts = 0;
    $scope.printCount = 1;
    $scope.bonusBatchFlag = false;
    $scope.GetGRNListForBarcode = function () {
    	if($scope.grn.id != undefined && $scope.grn.id>0){
    		var grnId = $scope.grn.id;
            var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' +  grnId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
              	$rootScope.stopLoader();
                $scope.itemsList = response.data;
                $scope.getItemsForBarcode($scope.itemsList);
                $rootScope.stopLoader();
              },
              function (err) {
                $rootScope.stopLoader();
                growl.error(err.data.message);
              });
    	}
    	else{
    		growl.error("Grn id not found!");
    	}
    	
      }
    
    $scope.getItemsForBarcode = function(newItem){
  	  
  	  $scope.printCounts = $scope.printCount;
  	  $scope.PRNRequired = newItem.isPRNRequired;
        $scope.pharmacyPRNFormat = newItem.pharmacyPRNFormat;
  	  
        $scope.itemsList.items.forEach(function (item) {
  	        if ($scope.itemsList.approvalStatus == "Approved" && item.grnReceivedQuantity > item.rejectedQuantity) {
  	        	
  	        	$scope.GRNItemsForBarcode.push(item);
  	        }
  	        
  	    });
  	  $scope.openMultiplePopUp();
    }
    
    
    $scope.openMultiplePopUp = function(){
  	  if($scope.GRNItemsForBarcode.length>0){
  		  $scope.GRNItemsForBarcode.forEach(function (item) {

  		  if($scope.PRNRequired==true && $scope.pharmacyPRNFormat!=null && $scope.pharmacyPRNFormat!="")
      		{
  			  if($scope.printCounts == undefined || $scope.printCounts == 0){
  				  item.prints = 1;
  			  }
  			  else{
  				  
  				  item.prints = $scope.printCounts;
  			  }
  			  
  			$scope.bonusBatchFlag = false;
  			if(item.grnBonusBatchExpiry.length !=0 && item.grnBonusBatchExpiry != undefined){
			item.grnBonusBatchExpiry.forEach(function (bonus){
				
				if(parseInt(bonus.batchNo) != parseInt(item.batchNo)){
				    item.grnId = item.id;
				    item.bonusBatchFlag = true;
			    }
			});
  			}
  			  
      		$scope.isPRNPrint=$scope.PRNRequired;
      		$scope.prnFormat=$scope.pharmacyPRNFormat;

  					item.templateDesc=$scope.pharmacyPRNFormat;
  					item.prnCount=item.prints;

      		}
      	
  	  });
  	    $scope.printRadiologyReport($scope.GRNItemsForBarcode,null);
  		  $scope.GRNItemsForBarcode = [];
  		  
    	}
    }
    
    $scope.printRadiologyReport=function(barcodePrintingList,printWithHeader){
    	$scope.listForBarcodePrint = [];
    	angular.forEach(barcodePrintingList, function(info){
    	var data = {};
           if(info.bonusBatchFlag){
                info.grnId = info.grnId != undefined ? info.grnId : 0 ;
                info.bonusBatchFlag = true;
           }else{
                info.bonusBatchFlag = false;
                info.barcodeScreen = info.barcodeScreen != undefined ? info.barcodeScreen:false;
                info.grnId = info.grnId != undefined ? info.grnId : 0 ;
           }
           info.organizationId = $scope.organizationId;
           info.unitId =  $scope.unitId;

           data.batchId = info.batchId;
           data.bonusBatchFlag = info.bonusBatchFlag;
           data.grnId = info.grnId;
           data.organizationId = info.organizationId;
           data.prnCount = info.prnCount;
           data.templateDesc = info.templateDesc;
           data.unitId = info.unitId;
           $scope.listForBarcodePrint.push(data);
        });

//    	info.printWithHeader = printWithHeader;
    	var URI = BASE_URL + ROOT_URL + "/LIS/transaction/barcodePrinting";
    	GenericService.serviceAction(METHOD_POST, URI, $scope.listForBarcodePrint,NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{
	
			   if(response.data.status=='success')
			      {
				    $scope.listForBarcodePrint=[];
				    $scope.GRNItemsForBarcode = [];
				   if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint==true && $scope.prnFormat!=null){
						//JSPrinterManager call
//						print($scope.finalPRN);
						//For PRN Print manager
							$scope.printPRNPrint(response.data.listObject);
					}														

			      }
			   else if(response.data.status=='error'){
				   $rootScope.stopLoader();
			   }
		});
	
    }
    
    var labConfig  =    $scope.checkLabConfig();
    $scope.prnUrl = labConfig.prnUrl;
    $scope.printPRNPrint= function(list){
		var listForUpdateCount = [];

        angular.forEach(list, function (item, index){
           if(item.templateDesc != ""){
               var newItem={};
               newItem.batchId = item.batchId;
               newItem.printedCount = parseInt(item.prnCount);
               listForUpdateCount.push(newItem);
           }
        });
		
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API + "/updatePrintedCount";
        GenericService.serviceAction(METHOD_POST, URI, listForUpdateCount).then(function(response) {

            if(response.data.status = "success"){

                angular.forEach(list, function(item){
                    console.log(item.templateDesc);
                    $timeout(function () {
                        var url = $scope.prnUrl;

                        var xhr = new XMLHttpRequest();
                        xhr.open("POST", url);

                        xhr.setRequestHeader("Content-Type", "text/plain");

                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === 4) {
                                console.log(xhr.status);
                                console.log(xhr.responseText);
                            }
                        };

                        var data = item.templateDesc;
                        xhr.send(data);

                    }, 3000);
                })
            }
        });
    }


    /*-------Batchwise barcode printing end-----------*/
    

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("GRNApproval", {
      url: "/GRNApproval",
      templateUrl: 'views/procurement/quotation/GRN-approval.html',
      controller: "GRNApproval.Controller",
      title: 'Good Receipt Note Approval',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 182);
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
    .controller("GRNApproval.Controller", GRNApprovalController);
})();