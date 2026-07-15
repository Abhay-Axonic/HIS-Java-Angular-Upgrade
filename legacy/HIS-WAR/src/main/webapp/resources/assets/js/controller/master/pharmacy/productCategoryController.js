(function() {
  "use strict";

  function productCategoryController(
    $scope,
    $rootScope,
    $http,
    $filter,
    CONSTANTS,
    GenericService,
    PaginationService,
    growl,
    $cookies,
    BillingGenericService 
  ) {
    var self = this;
    // var apiUrl = constants.apiurl + constants.unitsApi;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
    	
    	var cookieObject = $cookies.getObject('cookieObject');
    	if(cookieObject == undefined){
    		$state.go('login');
    		return;
    	}
    	
    	$scope.unitId = cookieObject.unitId;
    	$scope.organizationId = cookieObject.organizationId;
    	$scope.userId = cookieObject.userId;
    	
      $rootScope.loginpage = true;
      self.model = {
        PopulateAssetTypeDropdown: [
          {
            type: "Select Asset Type"
          }
        ],
        assetCategoryDropdown: [
          {
            category: "Select Asset Category Type"
          }
        ],
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '10',
          search: '',
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

      //Methods
      self.model.GetProductCategory = GetProductCategory;
      self.model.ToggleStatus = ToggleStatus;
      self.model.SaveProductCategory = SaveProductCategory;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.ClearFields = ClearFields;
      self.model.SavePopup = SavePopup;
      self.model.clearSearch = clearSearch;
      self.model.generalLedgerId=0;
      self.model.generalLedgerName='';
      $scope.search = '';
      PopulateAssetTypeDropdown();
      self.model.searchProduct = searchProduct;
    }

    //Method to get the list of ProductCategory
    var GetProductCategoryList = function() {
      var URI = CONSTANTS.GLOBAL.MASTER_PRODUCT_CATEGORY_API +$scope.organizationId;
      self.model.generalLedgerName="";
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.productCategoryList = response.data;
          angular.forEach(self.model.productCategoryList, function(value1 , key) {
          angular.forEach($scope.ledgerList, function(value , key) {
  			if(value1.generalLedgerId==value.generalLedgerId)
  				{
  				value1.generalLedgerName=value.generalLedgerName;
  				value1.generalLedgerId=value.generalLedgerId;
  				}
  		});
        });
          if(!!response && !!response.data && response.data.length > 0){            
            searchProduct(self.model.productCategoryList, self.model.pagination);
          }

          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };
    
	$scope.searchServices = function(searchKeyword){
		$scope.servicesList=[];
		
		var serData = {
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				searchKeyword : searchKeyword
		}
		var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
		
		//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
		return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				$scope.servicesList = response.data.listObject;
			}
			return $scope.servicesList;
		});
	}
	
	  $scope.selectService = function($item, $model, $label)
	    {
		  self.model.serviceId = $item.serviceMasterId; 
	    }

    //Method to Populate Asset Type Dropdown
    var PopulateAssetTypeDropdown = function() {
      var productCategory = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId ;

      productCategory.assetType = productCategory.PopulateAssetTypeDropdown[0];
      self.model.assetCategory = self.model.assetCategoryDropdown[0];

      productCategory.PopulateAssetTypeDropdown.splice(1);
      productCategory.assetCategoryDropdown.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          productCategory.PopulateAssetTypeDropdown = productCategory.PopulateAssetTypeDropdown.concat(
            response.data
          );
          GetProductCategoryList();
          // PopulateAssetCategoryDropdown();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };
    var PopulateAssetCategoryDropdown = function(assetType, selectedValue) {
      var assettypeId = 0;
      if (!!assetType.assetTypeId) {
        assettypeId = assetType.assetTypeId;
      } else if (!!assetType.id) {
        assettypeId = assetType.id;
      }

      // if (assetType.assetTypeId == undefined) {
      //   assettypeId = 0;
      // }
      // else {
      //   assettypeId = assetType.assetTypeId;
      // }
      var assetCategory = self.model;

      assetCategory.assetCategory = self.model.assetCategoryDropdown[0];
      assetCategory.assetCategoryDropdown.splice(1);
      // self.model.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + assettypeId +"/"+$scope.unitId + "/"+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.assetCategoryDropdown = self.model.assetCategoryDropdown.concat(
            response.data
          );
          $rootScope.stopLoader();
          if (selectedValue) {
            var assetCategory = self.model.assetCategoryDropdown.find(function(
              type
            ) {
              return assetType.assetCategoryId == type.id;
            });
            self.model.assetCategory = assetCategory;
          }
        },
        function(err) {}
      );
    };

    //Method to populate ProductCategory for editing
    var GetProductCategory = function(productCategoryId) {
      self.model.activeProductCategory = productCategoryId;
      self.model.isAssetTypeIdValid = false;
      self.model.isAssetCategoryIdValid = false;
      self.model.isProductCategoryCodeValid = false;
      self.model.isProductCategoryNameValid = false;

      var URI =
        CONSTANTS.GLOBAL.MASTER_PRODUCT_CATEGORY_API +$scope.organizationId+ '/' + productCategoryId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        var assetType = self.model.PopulateAssetTypeDropdown.find(function(
          type
        ) {
          return response.data.assetTypeId == type.id;
        });
        self.model.assetType = assetType;
        PopulateAssetCategoryDropdown(response.data, true);
        self.model.id = response.data.id;
        self.model.productCategoryCode = response.data.code;
        self.model.productCategoryName = response.data.category;
        self.model.serviceId = response.data.serviceId;
        $scope.search = response.data.serviceName;
        angular.forEach($scope.ledgerList, function(value , key) {
			if(response.data.generalLedgerId==value.generalLedgerId)
				{
				self.model.generalLedgerName=value.generalLedgerName;
				self.model.generalLedgerId=value.generalLedgerId;
				}
		});
      });
      $rootScope.stopLoader();
    };

    //Method to activate or deactivate ProductCategory
    var ToggleStatus = function(productCategory) {
      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.PRODUCT_CATEGORY_MASTER
          .PRODUCT_CATEGORY_API + productCategory.id +"/"+$scope.organizationId;

          var data = {
            "status": productCategory.status
          };
          if (productCategory.status == false) {
            ClearFields();
          }
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              $rootScope.stopLoader();
              growl.success('Status changed successfully');
            }, function (err) {
              if(err.status == 406){
                growl.error(err.data.message);
                productCategory.status = true;
              }else{
             
              growl.error('Something Went Wrong');
            }
            });
            $rootScope.stopLoader();
    };

    //Method for popup
    var SavePopup = function() {
      var popup = angular.element("#confirmSave");
      popup.modal("show");
    };
    $scope.getLedgerList = function(keyword)
	{
		$scope.ledgerList=[];
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
       var data = {
               
           }
        GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       $scope.ledgerList=response.data.listObject;
                   }
               });
	}
	$scope.getLedgerList();
	$scope.ledgerNameSearch = function(keyword)
	{
		self.model.generalLedgerId=null;
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
       var data = {
               "generalLedgerName":keyword
           }
       return GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       return response.data.listObject;
                   }
               });
	}
	$scope.SelectLedgerName =function(ledger)
   {
		self.model.generalLedgerId=ledger.generalLedgerId;
   }
    //Method to add or update the ProductCategory
    var SaveProductCategory = function() {
      if (Validate()) {
        if (!self.model.id) {
          CreateProductCategory();
        } else {
          UpdateProductCategory();
        }
      }
    };

    var ValidateProductCategory = function() {
      var isValid = true;

      self.model.productCategoryList.forEach(function(element) {
        if (element.code == self.model.productCategoryCode) {
          growl.error("Duplicate Product Category code");
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCategoryCode)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        return;
      }
      return isValid;
    };

    var ValidateCodeForUpdate = function() {
      var isValid = true;

      self.model.productCategoryList.forEach(function(element) {
        if (element.id != self.model.activeProductCategory) {
          if (element.code == self.model.productCategoryCode) {
            growl.error("Duplicate Product Category code");
            isValid = false;
          }
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCategoryCode)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        return;
      }
      return isValid;
    };
    $scope.clearText = function()
    {
    	if(self.model.generalLedgerId==undefined || self.model.generalLedgerId==null || self.model.generalLedgerId==0)
    		{
    		self.model.generalLedgerName="";
    		}
    }
    //Method to add the ProductCategory
    var CreateProductCategory = function() {
      if (ValidateProductCategory()) {
        var URI =
          CONSTANTS.MASTER.GLOBAL.PHARAMACY.PRODUCT_CATEGORY_MASTER
            .PRODUCT_CATEGORY_API;
        var data = {
          assetCategoryId: self.model.assetCategory.id,
          code: self.model.productCategoryCode,
          serviceId: self.model.serviceId,
          category: self.model.productCategoryName,
          status: true,
          organizationId : $scope.organizationId,
          unitId : $scope.unitId,
          userId : $scope.userId,
          generalLedgerId:self.model.generalLedgerId
        };
        var assetType = self.model.PopulateAssetTypeDropdown.find(function(
          type
        ) {
          return type.id == self.model.assetType.id;
        });
        var assetCategoryData = self.model.assetCategoryDropdown.find(function(
          type
        ) {
          return type.id == self.model.assetCategory.id;
        });
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function(response) {
            /*var productCategory = {
              id: response.data.id,
              code: self.model.productCategoryCode,
              category: self.model.productCategoryName,
              assetType: assetType.type,
              assetCategory: assetCategoryData.category,
              status: true
            };
            self.model.productCategoryList.push(productCategory);
            PaginationService.searchData(self.model.productCategoryList, self.model.pagination);
            PaginationService.setLastPage(self.model.pagination);
 */
            ClearFields();

            $rootScope.stopLoader();
            growl.success(response.data.message);
            PopulateAssetTypeDropdown();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    };

    //Method to update the  ProductCategory
    var UpdateProductCategory = function() {
      if (ValidateCodeForUpdate()) {
        var URI =
          CONSTANTS.MASTER.GLOBAL.PHARAMACY.PRODUCT_CATEGORY_MASTER
            .PRODUCT_CATEGORY_API + $scope.organizationId + "/" + self.model.id;

        var data = {
          assetCategoryId: self.model.assetCategory.id,
          code: self.model.productCategoryCode,
          serviceId:self.model.serviceId,
          category: self.model.productCategoryName,
          status: true,
          generalLedgerId:self.model.generalLedgerId
        };

         $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(function(
          response
        ) {
          var assetType = self.model.PopulateAssetTypeDropdown.find(function(
            type
          ) {
            return type.id == self.model.assetType.id;
          });
          var assetCategoryData = self.model.assetCategoryDropdown.find(
            function(type) {
              return type.id == self.model.assetCategory.id;
            }
          );
          var productCategory = self.model.productCategoryList.find(function(
            type
          ) {
            return type.id == self.model.id;
          });

          productCategory.code = data.code;
          productCategory.category = data.category;
          productCategory.assetType = assetType.type;
          productCategory.assetCategory = assetCategoryData.category;

          ClearFields();
          $rootScope.stopLoader();
          GetProductCategoryList();
          growl.success(response.data.message);
        });
      }
    };

    //Method to clear the fileds
    var ClearFields = function() {
      self.model.assetType = self.model.PopulateAssetTypeDropdown[0];
      self.model.assetCategory = self.model.assetCategoryDropdown[0];
      self.model.assetCategoryDropdown.splice(1);
      self.model.productCategoryCode = "";
      self.model.productCategoryName = "";
      self.model.id = undefined;
      self.model.isAssetTypeIdValid = false;
      self.model.isAssetCategoryIdValid = false;
      self.model.isProductCategoryCodeValid = false;
      self.model.isProductCategoryNameValid = false;
      $scope.search = '';
    };

    var clearSearch = function() {
      self.model.pagination.search = "";
//      PaginationService.searchData(self.model.productCategoryList, self.model.pagination);
      searchProduct(self.model.productCategoryList, self.model.pagination);
    };

    // Methode to Search the product
    var searchProduct = function (productCategoryList, pagination) {
            var data = productCategoryList;
            var searchQuery = pagination.search.toLowerCase();

            if (searchQuery) {
                pagination.filteredData = $filter('filter')(data, function (item) {
                    return (
    					(item.assetType != undefined ? item.assetType.toLowerCase().includes(searchQuery) : "")||
                        (item.assetCategory != undefined ? item.assetCategory.toLowerCase().includes(searchQuery) : "") ||
    					(item.code != undefined ? item.code.toLowerCase().includes(searchQuery) : "") ||
    					(item.category != undefined ?  item.category.toLowerCase().includes(searchQuery) : "") ||
    					(item.generalLedgerName != undefined ? item.generalLedgerName.toLowerCase().includes(searchQuery) : "") ||
    					(item.serviceName != undefined ? item.serviceName.toLowerCase().includes(searchQuery) : "")
                    );
                });
            } else {
                pagination.filteredData = data;
            }

            setTotalPages(pagination);
        }


    var setTotalPages = function (pagination) {
            pagination.currentPage = 1;
            pagination.totalPages = Math.ceil(pagination.filteredData.length / parseInt(pagination.pageSize, 10));
           return setPagedData(pagination);
    }

    var setPagedData = function (pagination) {
          var limit = parseInt(pagination.pageSize, 10);
          var offset = (pagination.currentPage - 1) * limit;
          pagination.data = pagination.filteredData.slice(offset, offset + limit);
    }

    var Validate = function() {
      var isValid = true;

      if (!self.model.assetType.id) {
        isValid = false;
        self.model.isAssetTypeIdValid = true;
      }
      if (!self.model.assetCategory.id) {
        isValid = false;
        self.model.isAssetCategoryIdValid = true;
      }
      if (!self.model.productCategoryCode) {
        isValid = false;
        self.model.isProductCategoryCodeValid = true;
      }
      if (!self.model.productCategoryName) {
        isValid = false;
        self.model.isProductCategoryNameValid = true;
      }

      return isValid;
    };

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("pharmacyproductCategoryMaster", {
      url: "/pharmacyproductCategoryMaster",
      // data: { title: "About", activetab : "about" },
      templateUrl:"views/master/GlobalMaster/pharmacy-product-category-master.html",
      controller: "ProductCategory.Controller",
      title: 'Product Category Master',
  	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 458);
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
      // authenticate: false
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("ProductCategory.Controller", productCategoryController);
})();
