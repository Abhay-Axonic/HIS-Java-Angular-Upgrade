(function () {
  "use strict";

  function uomController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, GenericService, commonDetailService, PaginationService, growl,PagerService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        ispQuantity: [],
        issQuantity: [],
        isdQuantity: [],
        itemList: [],
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        ItemToUpdate: {
          uomList: [{"id": null}]
        },
       /* pagination: {
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
      
  	/**Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.commonList = [];
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;

      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.SaveUOMData = SaveUOMData;
      self.model.GetListOfItems = GetListOfItems;
      self.model.ChangeUOMType = ChangeUOMType;
      self.model.UpdateItem = UpdateItem;

      self.model.uomTypes = [
        {
          "id": 3,
          "uomType": "Dispense"
        },
        {
            "id": 1,
            "uomType": "Purchase"
          },
          {
              "id": 2,
              "uomType": "Stock"
            },
      ]

      GetUOMList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
    }

    var GetUOMList = function (organizationId,offset,noOfRecordsPerPage) {
      if($scope.search == ""){
        $scope.search = undefined;
      }
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UOM_MASTER.SAVE_UOM+organizationId+ S + offset
		+ S + noOfRecordsPerPage + S + $scope.search;

      GetUOMUnitList();

      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	
        	$scope.commonListCount = response.data.object;
        	
        	
          response.data.listObject.forEach(function(item){
            var selectedipUom = item.uomList.find(function(uom){
              return uom.ipUom == true;
            });

            var selectedopUom = item.uomList.find(function(uom){
              return uom.opUom == true;
            });

            var selectedstoreUom = item.uomList.find(function(uom){
              return uom.storeUom == true;
            });

            item.ipuom = self.model.uomTypes[item.uomList.indexOf(selectedipUom)];
            item.opuom = self.model.uomTypes[item.uomList.indexOf(selectedopUom)];
            item.storeuom = self.model.uomTypes[item.uomList.indexOf(selectedstoreUom)];

            item.purchaseQuantity = item["uomList"][0].conversion;
            item.stockQuantity = item["uomList"][1].conversion;
            item.dispenseQuantity = item["uomList"][2].conversion;

            item.puom = self.model.uomUnits.find(function(units){
              return item["uomList"][0].uomUnitId == units.id;
            });

            item.suom = self.model.uomUnits.find(function(units){
              return item["uomList"][1].uomUnitId == units.id;
            });

            item.duom = self.model.uomUnits.find(function(units){
              return item["uomList"][2].uomUnitId == units.id;
            });

            if (item.uomList[0].id) {
              item["isDisabled"] = true;
            }
          });

          self.model.itemDetails = response.data.listObject;

        
          self.model.ItemToUpdate.id = null;
          $scope.setPage(1, false);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetListOfItems = function (){
      self.model.action = 'Open';
      var popup = angular.element('#addItemListPop');
      popup.modal('show');
      PopulateAssetCategoryDropdown();
      self.model.selectAllRows = false
      self.model.items = [];
      self.model.search.itemCode = undefined;
      self.model.search.itemName = undefined;
    }

    var ValidateUOM = function (){
      var isValidItem = true;

      for (var i = 0; i < self.model.itemDetails.length; i++) {
        if(!self.model.itemDetails[i].id && !parseInt(self.model.itemDetails[i].purchaseQuantity)){
          self.model.ispQuantity[i] = true;
          isValidItem = false;
        }

        if(!self.model.itemDetails[i].id  && !parseInt(self.model.itemDetails[i].stockQuantity)){
          self.model.issQuantity[i] = true;
          isValidItem = false;
        }

        if(!self.model.itemDetails[i].id  && !parseInt(self.model.itemDetails[i].dispenseQuantity)){
          self.model.isdQuantity[i] = true;
          isValidItem = false;
        }

        if ((!self.model.itemDetails[i].id)  &&( !parseInt(self.model.itemDetails[i].purchaseQuantity) || !parseInt(self.model.itemDetails[i].stockQuantity) || !parseInt(self.model.itemDetails[i].dispenseQuantity))) {
          growl.error('All fields are required');
          isValidItem = false;
        }
      }

      return isValidItem;
    }

    

	
    
    var GetUOMUnitList = function () {
      var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.uomUnits = response.data;
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    
    $scope.setNoOfRecords = function() {
    	
    	GetUOMList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
	};
    
	
	
	$scope.MasterList = function(orgId, offset, noOfRecordsPerPage) {
      if($scope.search == ""){
        $scope.search = undefined;
      }
		 var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UOM_MASTER.SAVE_UOM+orgId+ S + offset
			+ S + noOfRecordsPerPage + S + $scope.search;

	      GetUOMUnitList();

	      GenericService.serviceAction("GET", URI, {}).then(
	        function (response) {
	        	
	        	$scope.commonListCount = response.data.object;
	        	
	        	
	          response.data.listObject.forEach(function(item){
	            var selectedipUom = item.uomList.find(function(uom){
	              return uom.ipUom == true;
	            });

	            var selectedopUom = item.uomList.find(function(uom){
	              return uom.opUom == true;
	            });

	            var selectedstoreUom = item.uomList.find(function(uom){
	              return uom.storeUom == true;
	            });

	            item.ipuom = self.model.uomTypes[item.uomList.indexOf(selectedipUom)];
	            item.opuom = self.model.uomTypes[item.uomList.indexOf(selectedopUom)];
	            item.storeuom = self.model.uomTypes[item.uomList.indexOf(selectedstoreUom)];

	            item.purchaseQuantity = item["uomList"][0].conversion;
	            item.stockQuantity = item["uomList"][1].conversion;
	            item.dispenseQuantity = item["uomList"][2].conversion;

	            item.puom = self.model.uomUnits.find(function(units){
	              return item["uomList"][0].uomUnitId == units.id;
	            });

	            item.suom = self.model.uomUnits.find(function(units){
	              return item["uomList"][1].uomUnitId == units.id;
	            });

	            item.duom = self.model.uomUnits.find(function(units){
	              return item["uomList"][2].uomUnitId == units.id;
	            });

	            if (item.uomList[0].id) {
	              item["isDisabled"] = true;
	            }
	          });

	          self.model.itemDetails = response.data.listObject;

	        
	          self.model.ItemToUpdate.id = null;
	          $rootScope.stopLoader();
	        },
	        function (err) {
	          $rootScope.stopLoader();
	        });
		}
	
	
	
	
	$scope.pager = {};
	$scope.page;

	$scope.setPage = function(page, flag) {
		if (page < 1 || page > $scope.pager.totalPages)
		{
			return;
		}
		$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
		if (flag)
		{
			$scope.MasterList($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
		}
	}
    
    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

      self.model.assetCategory = self.model.assetCategories[0];
      self.model.productCategory = self.model.productCategories[0];

      self.model.assetCategories.splice(1);
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.assetCategories = self.model.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateProductCategory = function (assetCategory) {
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id + '/'+$scope.organizationId;
      
      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);
      
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.productCategories = self.model.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetItemList = function () {
      if(self.model.search.itemName){
        if(self.model.search.itemName.length >= 3){
              
          var URI = CONSTANTS.GLOBAL.ITEM_API_FOR_MASTER + "org/"+$scope.organizationId;

          var data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "itemName": self.model.search.itemName
          }

          self.model.items = [];
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.items = response.data;
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }
      }
      else{
        self.model.items = [];
      }
    }

    var AddItems = function () {
      if (self.model.SelectedItemsIds.length == 0) {
        growl.error('Select Items to Add');
        return false;
      } else {
        $rootScope.startLoaderPopup();
        self.model.SelectedItemsIds.forEach(function(item) {
          var isFound = self.model.itemDetails.find(function(x){
            return x.itemId == item.itemId;
          });

          if (!isFound) {
            item.dispenseQuantity = 1;
            self.model.itemDetails.push(item);
            item.uomList = [{
                "uomTypeId": 1,
                "uomUnitId": 1,
                "conversion": null,
                "ipUom": true,
                "opUom": true,
                "storeUom": true,
                "status": true
              }, {
                "uomTypeId": 2,
                "uomUnitId": 1,
                "conversion": null,
                "ipUom": false,
                "opUom": false,
                "storeUom": false,
                "status": true
              }, {
                "uomTypeId": 3,
                "uomUnitId": 1,
                "conversion": null,
                "ipUom": false,
                "opUom": false,
                "storeUom": false,
                "status": true
              }
            ]
            selectUOM(item);
          }
        });

       // PaginationService.searchData(self.model.itemDetails, self.model.pagination);
       // PaginationService.setLastPage(self.model.pagination);

        self.model.SelectedItemsIds = [];
        self.model.items = [];

        var popup = angular.element('#addItemListPop');
        popup.modal('hide');

        $rootScope.stopLoaderPopup();
      }
    }

    var selectUOM = function (item) {
     
      item.puom = self.model.uomUnits[0];
      
      item.uomList[0].uomUnitId = item.puom.id;
      
      item.suom = self.model.uomUnits[0];
      
      item.uomList[1].uomUnitId = item.suom.id;
    
      item.duom = self.model.uomUnits[0];
      
      item.uomList[2].uomUnitId = item.duom.id;
      
      item.ipuom = self.model.uomTypes[0];
      item.opuom = self.model.uomTypes[0];
      item.storeuom = self.model.uomTypes[0];
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.items.map(function (item) {
          item.isSelected = true;
          return item;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.items.forEach(function (item) {
          item.isSelected = false;
        });
      }
    }

    var SelectItem = function (item) {
      var itemIds = self.model.SelectedItemsIds;

      if (item.isSelected) {
        itemIds.push(item);
      } else {
        var index = itemIds.indexOf(item);
        if (index > -1) {
          itemIds.splice(index, 1);
        }
      }

      if (self.model.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var UpdateItem = function (item) {
    if(self.model.itemList!=undefined  && self.model.itemList!=null &&  self.model.itemList.length>0 ){
    	var alreadyexist=false;
    	self.model.itemList.forEach(function(ItemObject){
    		if(ItemObject.itemId==item.itemId){
    			alreadyexist=true;
    		}
         });
      if(!alreadyexist){
    	  self.model.itemList.push(item);
          self.model.itemDetails.forEach(function(uomItem){
            if (uomItem.uomList[0].id) {
              uomItem.isDisabled = true;
            }
          });

          self.model.itemList.forEach(function(data){
            data.isDisabled = false;
          });
          self.model.ItemToUpdate = item;
      }
      
    }else{
    	  self.model.itemList.push(item);
          self.model.itemDetails.forEach(function(uomItem){
            if (uomItem.uomList[0].id) {
              uomItem.isDisabled = true;
            }
          });

          self.model.itemList.forEach(function(data){
            data.isDisabled = false;
          });
          self.model.ItemToUpdate = item;
    }
 
    }

    var SaveUOMData = function () {
      if (ValidateUOM()) {
        if(!self.model.ItemToUpdate.uomList[0].id){
          CreateUOM();
        }
        else{
          UpdateUOM();
        }
      }
    }

    var ChangeUOMType = function (item, uomName) {
      if (uomName == 'ipuom') {
        if (item.ipuom.id == 1) {
          item.uomList[0].ipUom = true;
          item.uomList[1].ipUom = false;
          item.uomList[2].ipUom = false;
        } else if (item.ipuom.id == 2) {
          item.uomList[0].ipUom = false;
          item.uomList[1].ipUom = true;
          item.uomList[2].ipUom = false;
        } else if (item.ipuom.id == 3) {
          item.uomList[0].ipUom = false;
          item.uomList[1].ipUom = false;
          item.uomList[2].ipUom = true;
        }
      } else if (uomName == 'opuom') {
        if (item.opuom.id == 1) {
          item.uomList[0].opUom = true;
          item.uomList[1].opUom = false;
          item.uomList[2].opUom = false;
        } else if (item.opuom.id == 2) {
          item.uomList[0].opUom = false;
          item.uomList[1].opUom = true;
          item.uomList[2].opUom = false;
        } else if (item.opuom.id == 3) {
          item.uomList[0].opUom = false;
          item.uomList[1].opUom = false;
          item.uomList[2].opUom = true;
        }
      } else if (uomName == 'storeuom') {
        if (item.storeuom.id == 1) {
          item.uomList[0].storeUom = true;
          item.uomList[1].storeUom = false;
          item.uomList[2].storeUom = false;
        } else if (item.storeuom.id == 2) {
          item.uomList[0].storeUom = false;
          item.uomList[1].storeUom = true;
          item.uomList[2].storeUom = false;
        } else if (item.storeuom.id == 3) {
          item.uomList[0].storeUom = false;
          item.uomList[1].storeUom = false;
          item.uomList[2].storeUom = true;
        }
      }
      
      console.log(item.uomList);
    }

    
    var CreateUOM = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UOM_MASTER.SAVE_UOM+$scope.organizationId + '/'+$scope.userId;

      var data = [];

      angular.forEach(self.model.itemDetails, function (item) {
        if (!item.uomList[0].id) {
          var uomItem = {
            "itemId": item.itemId,
            "uomList": item.uomList,
            "organizationId": $scope.organizationId,
            "unitId": $scope.unitId,
            "userId":$scope.userId
          }
          angular.forEach(item.uomList, function(d){
            if(d.uomTypeId == 3){
              d.conversion = 1;
            }
          });
          data.push(uomItem);
        }
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
        	GetUOMList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
          growl.success(response.data.message);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateUOM = function () {
      self.model.itemList.forEach(function(item){
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UOM_MASTER.SAVE_UOM+ $scope.organizationId  + '/'+ item.itemId;

        var updatedUomList = [];
        item.uomList.forEach(function(uom){
          updatedUomList.push({
            "id": uom.id,
            "uomTypeId": uom.uomTypeId,
            "uomUnitId": uom.uomUnitId,
            "conversion": parseInt(uom.conversion),
            "ipUom": uom.ipUom,
            "opUom": uom.opUom,
            "storeUom": uom.storeUom,
            "status": true
          });
        });
        var data = {
          "uomList": updatedUomList
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.ItemToUpdate.uomList[0].id = null;
            GetUOMList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
            growl.success(response.data.message);
            self.model.itemList=[];
            $rootScope.stopLoader();
          },
          function (err) {
            growl.error(err.data.message);
            $rootScope.stopLoader();
          });
      });
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("unitofMeasurement", {
      url: "/unitofMeasurement",
      templateUrl: 'views/PharmacyInventory/unit-Measurement.html',
      controller: "UOMController.Controller",
      title: 'Clinical Unit of Measurement',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 466);
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
    .controller("UOMController.Controller", uomController);
})();