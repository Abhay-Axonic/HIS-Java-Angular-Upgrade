(function() {
  "use strict";
  function substoremasterController($scope,$rootScope,$http,CONSTANTS,GenericService,PaginationService,$state,openingBalanceService,productService,growl,$cookies,PagerService) {
    var self = this;
    var uomUnitIdSet;
    var cookieObject = $cookies.getObject('cookieObject');
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        stores: [
          {
            store: "Select Store"
          }
        ],
        assetTypeList: [
          {
            type: "Select Asset Type"
          }
        ],
        assetCategories: [
          {
            category: "Select Asset Category"
          }
        ],
        productCategories: [
          {
            category: "Select Product Category"
          }
        ],
        isQtyValid: [],
        SelectedRequestIds: [],
        SelectedItemsIds: [],
        itemSelectionList: [],
        storeIndentData: [],
        selectAllRows: false,
        SelectedItemIds: [],
        requestData: [],
        openingBalanceData: [],
        selectAllRows: false,
        currentUomList: [],
        storeGenericList: [],
        storeNew: [],
        oldStore: [],
        dateOptions: {
          formatYear: "yyyy",
          showWeeks: false
        },
        dateOpen: [],
        storeFlag: false,
        isSellable: false,
        
      /*  pagination: {
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
        }*/
      };

      self.model.GetStoreList = GetStoreList;
      self.model.storeId = null;

      self.model.GetAssetCategoryList = GetAssetCategoryList;
      self.model.searchNewItem = searchNewItem;
      self.model.SelectItem = SelectItem;
      self.model.SelectAllItems = SelectAllItems;
      self.model.AddItems = AddItems;
      self.model.PopulateDropdown = PopulateDropdown;
      // // self.model.getproductCategory = getproductCategory;
      // self.model.getAllItemDetails = getAllItemDetails;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.selectedUOMList = [];
      self.model.selectedUOMUnit = [];
      self.model.selectedItemIndex = 0;
      self.model.ClearFields = ClearFields;
      self.model.isItemChecked = false;
      self.model.ItemDetailList = [];
      self.model.searchNewItemList = [];
      self.model.validateSave = validateSave;
      self.model.GetStoreGenericList = GetStoreGenericList;
      self.model.SelectItemDetails = SelectItemDetails;
      self.model.SaveStoreItem = SaveStoreItem;
      self.model.UpdateStoreItem = UpdateStoreItem;
      self.model.SaveSubItem = SaveSubItem;
      self.model.ValidateStoreMaster = ValidateStoreMaster;
      self.model.validPopup = validPopup;
      self.model.SelectAllMainItems = SelectAllMainItems;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.applyMinMaxQty = applyMinMaxQty;
      self.model.minQty = null;
      self.model.maxQty = null;
      
      $scope.selectedStoreList=[];
      
      buttondisabled();
      PopulateDropdown();
      // GetStoreGenericList();
      PopulateAssetTypeDropdown();
    }
      $scope.offset = 0;
	  $scope.noOfRecordsPerPage = 50;
	  $scope.commonListCount;
	  
    var buttondisabled = function() {
    	if($scope.selectedStoreList.length == 0){
        $('#buttondisabled').show();
        $('#buttonshow').hide();
    	}
    }
    
    

	$scope.setNoOfRecords = function() {
		GetStoreGenericList();
	};
    
    $scope.selectedStoreList = [];
    //Method to get the list of Generic
    var GetStoreGenericList = function(store ,offset ) {
    	if($scope.selectedStoreList.length != 0){
    		$('#buttonshow').show();
    		$('#buttondisabled').hide();
    		}
    	
    	var data = {};
    	
    	data.offset = offset != undefined  ? offset: $scope.offset;
    	data.noOfRecordsPerPage = $scope.noOfRecordsPerPage;
    	data.multipleStoreRequest = $scope.selectedStoreList;
    	
        var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_ITEM_MAPPER.MULTIPLE_STORE_ITEM_MAPPER_DROPDOWN_API;
        $rootScope.startLoader();
        GenericService.serviceAction("POST",URI,data).then(function(response){
        	 self.model.storeGenericList = [];
          if(!!response && !!response.data.listObject && response.data.listObject.length > 0){            
        	  self.model.storeGenericList = response.data.listObject;
        	  
        	  if(offset == undefined){
        		  $scope.commonListCount = response.data.object;
    			  $scope.setPage(1,false);
        	  }
        	  
        	  	
          }
          
        
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
        );
      };
      
      
      
      $scope.pager = {};
		$scope.page;
		$scope.setPage = function(page, flag)
		{
			if (page < 1 )
			{
				return;
			}
			$scope.pager = PagerService.GetPager(
					$scope.commonListCount, page,
					$scope.noOfRecordsPerPage);
			if (flag) {
				$scope.pager.startIndex;
				
				 GetStoreGenericList($scope.selectedStoreList,$scope.pager.startIndex);                   
			}
		}
		

    $scope.yourEvents = {
      onItemSelect: function(selectedValue) {
        GetStoreGenericList($scope.selectedStoreList);                     
      },
      onItemDeselect: function(selectedValue) {
        GetStoreGenericList($scope.selectedStoreList);  
      },
      onDeselectAll:function(selectedValue) {
        GetStoreGenericList($scope.selectedStoreList);  
      },
      onSelectAll:function(selectedValue) {
        GetStoreGenericList($scope.selectedStoreList);  
        $('#buttonshow').show();
    		$('#buttondisabled').hide();
      },
    };	

    $scope.storeMultiSelectList =[];
    var PopulateDropdown = function() {
      // var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
      var data = "";
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        if (response.status == 200) {
          $scope.storeList = response.data.deliveryStores;    
          angular.forEach(response.data.deliveryStores,
              function(value,index) {
                var data = {};
                data.id = value.id;
                data.label = value.store;
                $scope.storeMultiSelectList.push(data);
          });
        }
      });	
    };

    $scope.example6settings = {
      enableSearch: true,
      scrollableHeight: '150px', 
      scrollable: true
    };

    var PopulateAssetTypeDropdown = function() {
      var assetTypeDropdown = self.model;
      assetTypeDropdown.assetType = assetTypeDropdown.assetTypeList[0];
      self.model.assetCategory = self.model.assetCategories[0];
      self.model.productCategory = self.model.productCategories[0];

      assetTypeDropdown.assetTypeList.splice(1);
      self.model.assetCategories.splice(1);
      self.model.productCategories.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId ;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          assetTypeDropdown.assetTypeList = assetTypeDropdown.assetTypeList.concat(
            response.data
          );
          $rootScope.stopLoader();
          // PopulateAssetCategoryDropdown();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };
    var GetAssetCategoryList = function(assetType) {
      var assettypeId = 0;
      if (assetType.id == undefined) {
        assettypeId = 0;
      } else {
        assettypeId = assetType.id;
      }
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + assettypeId +"/"+$scope.unitId + "/"+$scope.organizationId;;

      self.model.assetCategory = self.model.assetCategories[0];
      self.model.productCategory = self.model.productCategories[0];

      self.model.assetCategories.splice(1);
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.assetCategories = self.model.assetCategories.concat(
            response.data
          );
          $rootScope.stopLoader();
        },
        function(err) {
          $rootScope.stopLoader();
        }
      );
    };

    var GetProductCategoryList = function(id) {
      var subStore = self.model;

      subStore.productCategory = subStore.productCategories[0];
      subStore.productCategories.splice(1);
      //TODO : Add asset category to the uri
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          subStore.productCategories = subStore.productCategories.concat(
            response.data
          );
          $rootScope.stopLoader();
        },
        function(err) {
          $rootScope.stopLoader();
        }
      );
    };

    function searchNewItem() {
      if (validPopup()) {
        if($scope.itemName){
          if($scope.itemName.length >= 3){
            
            var URI = CONSTANTS.GLOBAL.MULTI_ITEM_API_FOR_MASTER;
            const data = {
              assetTypeId: self.model.assetType.id,
              assetCategoryId: self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
              productCategoryId: self.model.productCategory.id ? self.model.productCategory.id : undefined,
              stores: $scope.selectedStoreList,
              itemName: $scope.itemName ? $scope.itemName : undefined,
              itemCode: $scope.itemCode ? $scope.itemCode : undefined,
              organizationId : $scope.organizationId
            };

            self.model.searchNewItemList = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function(response) {
              $rootScope.stopLoaderPopup();
              self.model.searchNewItemList = response.data;
              self.model.itemid = response.data.itemId;
            },
            function errorCallback(response) {
              $rootScope.stopLoaderPopup();
              growl.error(response.data.message);
            });
          }
        }
        else if($scope.itemCode){
          if($scope.itemCode.length >= 3){
            var URI = CONSTANTS.GLOBAL.MULTI_ITEM_API_FOR_MASTER;
            const data = {
              assetTypeId: self.model.assetType.id,
              assetCategoryId: self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
              productCategoryId: self.model.productCategory.id ? self.model.productCategory.id : undefined,
              stores: $scope.selectedStoreList,
              itemName: $scope.itemName ? $scope.itemName : undefined,
              itemCode: $scope.itemCode ? $scope.itemCode : undefined,
              organizationId : $scope.organizationId
            };

            self.model.searchNewItemList = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function(response) {
              $rootScope.stopLoaderPopup();
              self.model.searchNewItemList = response.data;
              self.model.itemid = response.data.itemId;
            },
            function errorCallback(response) {
              $rootScope.stopLoaderPopup();
              growl.error(response.data.message);
            });
          }
        }
        else{
          self.model.searchNewItemList = [];
        }
      }
    }

    var validPopup = function() {
      var isValid = true;
      if (!self.model.assetType.id) {
        growl.error("Select Asset Type");
        isValid = false;
      }
      return isValid;
    };

    var ValidateQty = function(subStore) {
      var isValid = true;

      if (!subStore || subStore.length <= 0) {
        isValid = false;
        growl.error("Add some items in the Sub Store Master");
      } else {
        subStore.forEach(function(subStoreitem) {
          if (
            !subStoreitem.minQty ||
            !subStoreitem.maxQty ||
            parseInt(subStoreitem.minQty) > parseInt(subStoreitem.maxQty)
          ) {
            subStoreitem.isQtyValid = true;
            growl.error("Min Quantity should be less than max quantity");
            isValid = false;
          }
          if (
            !(
              parseInt(subStoreitem.minQty) <= 10000000 &&
              parseInt(subStoreitem.minQty) > 0
            ) ||
            !(
              parseInt(subStoreitem.maxQty) <= 10000000 &&
              parseInt(subStoreitem.maxQty) >= 1
            )
          ) {
            self.model.isQtyValid = true;
            growl.error("Min & max Quantity should be between 1 to 10000000");
            isValid = false;
          }
        });
      }

      return isValid;
    };

    var validateSave = function() {
      var isValid = true;
      if (self.model.SelectedRequestIds < 1) {
        growl.error("Select Items");
        isValid = false;
      }
      return isValid;
    };

    function GetStoreList(storeSelect) {
      self.model.action = "Open";
      var popup = angular.element("#addNewStore");
      popup.modal("show");

      //  GetAssetCategoryList();
      self.model.SelectedItemsIds = [];
      self.model.itemSelectionList = [];
      self.model.SelectedRequestIds = [];
      self.model.searchNewItemList = [];
      self.model.selectAllRows = [];
      self.model.itemCode = null;
      self.model.itemName = null;
      self.model.isItemChecked = false;
      self.model.assetType = self.model.assetTypeList[0];
      self.model.assetCategory = [self.model.assetCategories[0]];
      self.model.assetCategory = self.model.assetCategories[0];
      self.model.productCategory = [self.model.productCategories[0]];
      self.model.productCategory = self.model.productCategories[0];
      $scope.search.itemName = '';
      $scope.search.itemCode = '';
      self.model.assetCategories.splice(1);
      self.model.productCategories.splice(1);
    }

    var AddItems = function() {
      self.model.itemSelectionList = self.model.SelectedItemsIds;
      $scope.itemMatchList = [];
      $scope.itemNotMatchList = [];
      self.model.itemSelectionList.forEach(function(x) {
          var flag = false;
      self.model.storeGenericList.forEach(function(item) {
          if (x.itemId == item.itemId) {
                      $scope.itemMatchList.push(item);
                      flag = true;
          }
                });
      if(!flag){
        $scope.itemNotMatchList.push(x);
      }
		  console.log("$scope.itemNotMatchList",$scope.itemNotMatchList);
      if(flag){
        $scope.selectedStoreList.forEach(function(store) {
          var flagI = true;
          $scope.itemMatchList.forEach(function(data) {
            if (data.storeId == store.id) {
              growl.error("Item is already mapped to " + data.storeName);
              flagI = false;
            } 
          });
          if (flagI == true) {
            $scope.addI(store);
          }
        });
      }
		});
		
		if($scope.itemNotMatchList.length > 0 ){
			$scope.itemNotMatchList1();
    }
    angular.forEach(self.model.storeGenericList, function(d){
      d.minQty = 1;
      d.maxQty = 10000000;
    });
   
    var popup = angular.element("#addNewStore");
    popup.modal("hide");  
    
  };

  $scope.addI = function (store){
    self.model.selectedStoreList=[];
    self.model.itemSelectionList.forEach(function (item){
      var obj = angular.copy(item);
      obj.storeId = store.id;
      $scope.storeMultiSelectList.forEach(function (x){
        if(x.id == store.id){
          $scope.storename = x.label;
        }
      });
      obj.storeName = $scope.storename;
      self.model.storeGenericList.push(obj);
      self.model.SelectedRequestIds.push(obj);
    });
  }  
    
  $scope.removeItemMapping=function(storeData)
  {
    
	  console.log(data);
      if(storeData.id){
        var URI =CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_ITEM_MAPPER.STORE_ITEM_REMOVE_API;

        var data = {
        		id: storeData.id,
        		storeId:storeData.storeId,
        		itemId:storeData.itemId,
        		userId:cookieObject.userId,
        	    fromDate: moment(new Date()).format('YYYY-MM-DD')

        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function(response) {
        	$rootScope.stopLoader();
          
            
            if(response.data.message == 'Stock is available')
            	{
            	 growl.error(response.data.message);
            	}
            else
            	{
            	  growl.success(response.data.message);
            	  GetStoreGenericList();
            	}
         
             
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error(response.data.message);
          }
        );
      }
      else{
        var newArray = [];
        self.model.storeGenericList.filteredData.map(function (obj) {
          if (obj !== storeData) {
            newArray.push(obj);
          }
        });
        self.model.storeGenericList.data = newArray;
      }
  }
  
  
  $scope.itemNotMatchList1 = function() {	
    $scope.itemNotMatchList.forEach(function (item){
      $scope.storeMultiSelectList.forEach(function (x){
        $scope.selectedStoreList.forEach(function (store){
          if(store.id == x.id){
            $scope.storename = x.label;
            $scope.storeId = x.id;
            var obj = angular.copy(item);
            obj.storeName = $scope.storename;
            obj.storeId = $scope.storeId;
            self.model.storeGenericList.push(obj);
            self.model.SelectedRequestIds.push(obj);
          } 
        });
      });   		 
      console.log("dataLoop",self.model.SelectedRequestIds)
    });
    

    var popup = angular.element("#addNewStore");
    popup.modal("hide");  
  }
  

    // Against Item Selection
    var isSelectedItem = function(item) {
      return item.isSelectedItem;
    }; // Against Item Selection
    var isSelected = function(item) {
      return item.isSelected;
    };

    var SelectAllItems = function() {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.searchNewItemList.map(function(
          item
        ) {
          item.isSelected = true;
          self.model.isItemChecked = true;
          return item;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.searchNewItemList.forEach(function(item) {
          item.isSelected = false;
          self.model.isItemChecked = false;
        });
      }
    };

    var SelectAllMainItems = function() {
      if (self.model.selectAllMainRows) {
        self.model.SelectedRequestIds = self.model.storeGenericList.map(
          function(item) {
            self.model.idList = item.id;
            item.isSelected = true;
            self.model.isItemChecked = true;
            return item;
          }
        );
      } else {
        self.model.SelectedRequestIds = [];
        self.model.storeGenericList.forEach(function(item) {
          item.isSelected = false;
          self.model.isItemChecked = false;
        });
      }
    };

    var SelectItemDetails = function(store) {
      self.model.id = store.id;
      var storeIds = self.model.SelectedRequestIds;

      if (store.isSelected) {
        storeIds.push(store);
      } else {
        var index = storeIds.indexOf(store);
        if (index > -1) {
          storeIds.splice(index, 1);
        }
      }

      if (self.model.storeGenericList.every(isSelected)) {
        self.model.selectAllMainRows = true;
      } else {
        self.model.selectAllMainRows = false;
      }
    };
    //self.model.SelectedRequestIds
    var SaveSubItem = function() {
      if (ValidateStoreMaster()) {
        if (self.model.id || self.model.idList) {
          UpdateStoreItem();
        } else {
          SaveStoreItem();
        }
      }
    };

    var ValidateStoreMaster = function() {
      var isValid = true;

      if ($scope.selectedStoreList.length == 0) {
        isValid = false;
        self.model.isStoreValid = true;
      }
      return isValid;
    };
    var SaveStoreItem = function() {
      if (ValidateQty(self.model.storeGenericList) && validateSave()) {
        var storeArray = [];
        angular.forEach(self.model.SelectedRequestIds, function(x) {
          storeArray.push({
            id: x.id,
            itemId: x.itemId,
            storeId: x.storeId,
            minQty: x.minQty,
            maxQty: x.maxQty,
            status: true
          });
        });
        var URI =
          CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_ITEM_MAPPER
            .STORE_ITEM_MAPPER_API;

        var data = {
          // stores: $scope.selectedStoreList,
          items: storeArray
        };

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, storeArray).then(
          function(response) {
            ClearFields();
            GetStoreGenericList(self.model.store);

            growl.success(response.data.message);
            $rootScope.stopLoader();
              self.model.SelectedRequestIds = [];
              angular.forEach(self.model.storeGenericList, function(item) {
              item.isSelected = false;
            });
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error(response.data.message);
          }
        );
      }
    };

    var UpdateStoreItem = function() {
      if (ValidateQty(self.model.storeGenericList) && validateSave()) {
        var URI =
          CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_ITEM_MAPPER.STORE_ITEM_MAPPER_API + "user/"+ $scope.userId;

        var data = [];

        angular.forEach(self.model.SelectedRequestIds, function(x) {
          if(x.isSellable == undefined){
            data.push({
              id: x.id,
              itemId: x.itemId,
              storeId: x.storeId,
              minQty: x.minQty,
              maxQty: x.maxQty,
              isSellable: false
            });
          }
          else{
            data.push({
              id: x.id,
              itemId: x.itemId,
              storeId: x.storeId,
              minQty: x.minQty,
              maxQty: x.maxQty,
              isSellable: x.isSellable
            });
          }
        });

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function(response) {
            ClearFields();
            GetStoreGenericList(self.model.store);
            $rootScope.stopLoader();
            growl.success(response.data.message);
            self.model.SelectedRequestIds = [];
            angular.forEach(self.model.storeGenericList, function(item) {
              item.isSelected = false;
            });
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    };

    var SelectItem = function(item) {
      var itemIds = self.model.SelectedItemsIds;

      if (item.isSelected) {
        itemIds.push(item);
      } else {
        var index = itemIds.indexOf(item);
        if (index > -1) {
          itemIds.splice(index, 1);
        }
      }

      if (self.model.searchNewItemList.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }

      self.model.isItemChecked = false;
      self.model.searchNewItemList.forEach(function(item) {
        if (item.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });
    };

    var OpenPopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
    }

    var ClosePopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('hide');
    }
    
    var applyMinMaxQty = function () {
    	angular.forEach(self.model.storeGenericList,
                function(store,index) {
    		if(store.isSelected)
    			{
	    			store.minQty = self.model.minQty;
	        		store.maxQty = self.model.maxQty;
    			}
            });
      }
    
    

    var SetOldValueStore = function(store){
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function(store){
     if (!!self.model.storeGenericList.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        OpenPopup('Open', 'confirmStore');
     }
     else{
       GetStoreGenericList(store);
     }
    }

    var ConfirmStorePopup = function(storeData){
      self.model.store = self.model.storeNew;
      ClosePopup('close', 'confirmStore');
      GetStoreGenericList(self.model.store);
      //self.model.storeGenericList = [];
    }

    var ClearFields = function(item) {
      self.model.store = self.model.stores[0];
      self.model.SelectedRequestIds.id = undefined;
      self.model.selectAllMainRows = "";
      self.model.selectAllRows = false;
    };

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("substoremaster", {
     url: "/substoremaster",
     templateUrl: "views/PharmacyInventory/StockAdjustment/sub-store-master.html",
     controller: "SubStoreMaster.Controller",
     title: 'Store Item Mapping Master',
  	 resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 632);
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
    .controller("SubStoreMaster.Controller", substoremasterController);
})();
