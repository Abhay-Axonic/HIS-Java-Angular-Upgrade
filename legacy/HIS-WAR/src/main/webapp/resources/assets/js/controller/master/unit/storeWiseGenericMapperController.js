(function() {
	"use strict";

	function storeWiseGenericMapperController($scope, $rootScope, $http,
			$filter, $cookies, CONSTANTS, GenericService, PaginationService, growl) {
		var self = this;
		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
		function initializeController() {
			$rootScope.loginpage = true;
			self.model = {
				stores : [ {
					"store" : "Select Store"
				} ],
				assetCategoryList : [ {
					"category" : "Select Asset Category Type"
				} ],
				productCategoryList : [ {
					"category" : "Select Product Category"
				} ],
				vedList : [ {
					"name" : "Select VED"
				} ],
				genericSearch : '',
				prevSearch : '',
				mPrevSearch : '',
				pagination : {
					currentPage : 1,
					totalPages : 0,
					pageSize : '10',
					search : '',
					searchData : PaginationService.searchData,
					setTotalPages : PaginationService.setTotalPages,
					getPagesList : PaginationService.getPagesList,
					setPage : PaginationService.setPage,
					setFirstPage : PaginationService.setFirstPage,
					setPreviousPage : PaginationService.setPreviousPage,
					setNextPage : PaginationService.setNextPage,
					setLastPage : PaginationService.setLastPage,
					sortBy : PaginationService.sortBy
				},
				genericData : []
			};

			// Methods

			// self.model.PopulateGenericName = PopulateGenericName;
			self.model.ToggleStatus = ToggleStatus;
			self.model.SaveStoreGenericMapper = SaveStoreGenericMapper;
			self.model.GetGenericMapperList = GetGenericMapperList;
			self.model.PopulateStoreDropdown = PopulateStoreDropdown;
			self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
			self.model.PopulateProductCategoryDropdown = PopulateProductCategoryDropdown;
			self.model.PopulateVED = PopulateVED;
			self.model.ClearFields = ClearFields;
			self.model.ValidateGenericMaster = ValidateGenericMaster;
			self.model.GetStoreWiseData = GetStoreWiseData;
			self.model.SaveGeneric = SaveGeneric;
			self.model.SearchGeneric = SearchGeneric;
			self.model.AddGeneric = AddGeneric;
			self.model.PopulateDropdown = PopulateDropdown;
			self.model.clearSearch = clearSearch;
			PopulateDropdown();
		}

		// Method to get the list of Generic
		var GetGenericMapperList = function() {

			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_API + "unit/"+$scope.unitId;
			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.genericMapperList = response.data;
						if (!!response && !!response.data
								&& response.data.length > 0) {
							PaginationService.searchData(
									self.model.genericMapperList,
									self.model.pagination);
						}
						$rootScope.stopLoader();
					}, function errorCallback(response) {
						$rootScope.stopLoader();
					});
		}
		var PopulateDropdown = function() {
			var storeMapper = self.model;
			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_DROPDOWN_API + '/' + $scope.organizationId + '/'+$scope.unitId;

			storeMapper.store = storeMapper.stores[0];
			storeMapper.ved = storeMapper.vedList[0];
			storeMapper.assetCategory = self.model.assetCategoryList[0];
			storeMapper.productCategory = self.model.productCategoryList[0];

			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {

								self.model.stores = self.model.stores
										.concat(response.data.store);
								if(response.data.vedResponseLists !== undefined && response.data.vedResponseLists !== null){
                                        self.model.vedList = self.model.vedList
                                                .concat(response.data.vedResponseLists);
								}

								self.model.assetCategoryList = self.model.assetCategoryList
										.concat(response.data.assetCategories);
								GetGenericMapperList();
								$rootScope.stopLoader();
							});

		}

		var PopulateVED = function() {
			var storeMapper = self.model;
			storeMapper.ved = storeMapper.vedList[0];
			storeMapper.vedList.splice(1);

			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_DROPDOWN;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						storeMapper.vedList = storeMapper.vedList
								.concat(response.data);
						$rootScope.stopLoader();
						// GetAssetCategories();
					}, function(err) {

					});
		}

		// Method to get the list of ProductCategory
		var PopulateProductCategoryDropdown = function(assetCategory,
				selectedValue) {
			var storeMapper = self.model;
			var assetcategoryId = 0;
			if (!!assetCategory.assetCategoryId) {
				assetcategoryId = assetCategory.assetCategoryId
			} else if (!!assetCategory.id) {
				assetcategoryId = assetCategory.id
			}
			if (selectedValue) {
				assetcategoryId = assetCategory.assetCategoryId;
			}
			storeMapper.productCategory = storeMapper.productCategoryList[0];

			storeMapper.productCategoryList.splice(1);
			var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + assetcategoryId + '/'+$scope.organizationId;

			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								storeMapper.productCategoryList = storeMapper.productCategoryList
										.concat(response.data);
								$rootScope.stopLoader();
								// GetAssetCategories();

								if (selectedValue) {
									var productCategory = storeMapper.productCategoryList
											.find(function(productcategory) {
												return assetCategory.productCategoryId == productcategory.id;
											});
									self.model.productCategory = productCategory;
								}
							}, function(err) {

							});
		}

		var PopulateStoreDropdown = function() {
			var storeMapper = self.model;

			var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
			storeMapper.store = storeMapper.stores[0];
			self.model.productCategoryList[0];
			storeMapper.stores.splice(1);
			self.model.productCategoryList.splice(1);
			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						storeMapper.stores = storeMapper.stores
								.concat(response.data);
						$rootScope.stopLoader();
					}, function(err) {
						$rootScope.stopLoader();
					});

		}

		// Method to Populate Asset Category Dropdown
		var PopulateAssetCategoryDropdown = function() {
			var storeMapper = self.model;
			storeMapper.assetCategory = self.model.assetCategoryList[0];
			storeMapper.productCategory = self.model.productCategoryList[0];
			storeMapper.productCategoryList[0];
			storeMapper.assetCategoryList.splice(1);
			storeMapper.productCategoryList.splice(1);
			var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								storeMapper.assetCategoryList = storeMapper.assetCategoryList
										.concat(response.data);
								GetGenericMapperList();
								$rootScope.stopLoader();
							}, function(err) {
								$rootScope.stopLoader();
							});
		}

		// Method to populate ProductCategory for editing
		var GetStoreWiseData = function(storeData) {
			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_API
					+ storeData.id;

			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								var assetCategory = self.model.assetCategoryList
										.find(function(type) {
											return response.data.assetCategoryId == type.id;
										});
								self.model.assetCategory = assetCategory;
								PopulateProductCategoryDropdown(response.data,
										true);
								self.model.id = response.data.id;
								self.model.genericId = response.data.genericId;
								self.model.genericCode = response.data.genericCode;
								self.model.genericName = response.data.genericName;
								self.model.activeGenericId = response.data.id;
								var ved = self.model.vedList
										.find(function(type) {
											return response.data.vedId == type.id;
										});
								var store = self.model.stores.find(function(
										type) {
									return response.data.storeId == type.id;
								});
								self.model.ved = ved;
								self.model.store = store;
								$rootScope.stopLoader();
							});
		};

		var SearchGeneric = function(search) {
			if (search.length < 1)
				return;
			if ((!self.model.mPrevSearch || self.model.mPrevSearch != search)
					&& (search.length == 1)) {
				self.model.mPrevSearch = search;
				var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_SEARCH_API + $scope.unitId +'/' + $scope.organizationId+ '/'
						+ search;

				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {}).then(
						function(response) {
							if (!!response.data && response.data.length > 0) {
								self.model.genericData = response.data;
								$rootScope.stopLoader();
								return $filter('filter')(
										self.model.genericData, {
											$ : search
										});
							} else {
								$rootScope.stopLoader();
								return genericCodeNotFound(search);
							}
						}, function(err) {
							$rootScope.stopLoader();
							return genericCodeNotFound(search);
						});
			} else {
				if (!!self.model.genericData
						&& self.model.genericData.length > 0
						&& self.model.genericData[0].genericFound != undefined
						&& !self.model.genericData[0].genericFound) {
					return genericCodeNotFound(search);
				} else {
					return $filter('filter')(self.model.genericData, {
						$ : search
					});
				}
			}
		}

		var genericCodeNotFound = function(search) {
			var item = {
				"genericFound" : false,
				"genericCode" : "Generic Code Not Found with Name " + search,
				"search" : search
			};
			self.model.genericData = [ item ];
			return self.model.genericData;
		}

		var AddGeneric = function(value) {
			self.model.genericCode = value.genericCode;
			self.model.genericName = value.genericName;
			self.model.autoGenericId = value.id;
		}
		var ToggleStatus = function(generic) {
			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_API
					+ generic.id;

			var data = {
				"status" : generic.status,
				"unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
			};
			if (generic.status == false) {
				ClearFields();
			}
			$rootScope.startLoader();
			GenericService.serviceAction("PATCH", URI, data).then(
					function(response) {
						$rootScope.stopLoader();
						growl.success('Status changed successfully');
					}, function(err) {
						if (err.status == 406) {
							growl.error(err.data.message);
							generic.status = true;
						} else {

							growl.error('Something Went Wrong');
						}
					});
			$rootScope.stopLoader();
		}

		var ValidateCode = function() {
			var isValid = true;

			self.model.genericMapperList.forEach(function(element) {
				if (element.genericCode == self.model.genericCode) {
					growl.error('Duplicate Generic code');
					isValid = false;
				}
			});
			if (!/^[a-z0-9-]+$/i.test(self.model.genericCode)) {
				// -------------------------^^^^^^^^^^
				growl.error('Name can only be alpha numeric with hypen.');
				return;
			}
			return isValid;
		}

		var ValidateCodeForUpdate = function() {
			var isValid = true;

			self.model.genericMapperList.forEach(function(element) {
				if (element.id != self.model.activeGenericId) {
					if (element.genericCode == self.model.genericCode) {
						growl.error('Duplicate Generic code');
						isValid = false;
					}
				}
			});

			return isValid;
		}

		// Method to add the Generic
		var SaveStoreGenericMapper = function() {
			// if(ValidateCode()){
			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_API;

			var data = {
				"storeId" : self.model.store.id,
				"genericId" : self.model.autoGenericId,
				"status" : true,
				"vedId" : self.model.ved.id,
				"unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
			}

			var storeData = self.model.stores.find(function(type) {
				return type.id == self.model.store.id;
			});
			var vedData = self.model.vedList.find(function(type) {
				return type.id == self.model.ved.id;
			});
			var assetCategoryData = self.model.assetCategoryList.find(function(
					type) {
				return type.id == self.model.assetCategory.id;
			});
			var productCategoryData = self.model.productCategoryList
					.find(function(type) {
						return type.id == self.model.productCategory.id;
					});

			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						GetGenericMapperList();
						var genericData = {
							"id" : response.data.id,
							"storeName" : storeData.store,
							"assetCategory" : assetCategoryData.category,
							"productCategory" : productCategoryData.category,
							"genericCode" : self.model.genericCode,
							"genericName" : self.model.genericName,
							"vedName" : vedData.name,
							"status" : true
						};
						self.model.genericMapperList.push(genericData);
						ClearFields();

						$rootScope.stopLoader();
						growl.success(response.data.message);
					}, function errorCallback(response) {
						GetGenericMapperList();
						$rootScope.stopLoader();
						growl.error('Something went wrong');
					});
			// }
		};

		// Method to add or update the Generic
		var SaveGeneric = function() {
			if (ValidateGenericMaster()) {
				if (!self.model.id) {
					SaveStoreGenericMapper();
				} else {
					UpdateStoreGenericMapper();
				}
			}
		};
		// Method to update the Generic
		var UpdateStoreGenericMapper = function() {
			// if(ValidateCodeForUpdate()){
			var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_WISE_GENERIC_MAPPER.STORE_WISE_GENERIC_API
					+ self.model.id;

			var data = {
				"storeId" : self.model.store.id,
				"genericId" : self.model.autoGenericId,
				"status" : true,
				"vedId" : self.model.ved.id,
				"unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
			}
			// $rootScope.startLoader();
			GenericService
					.serviceAction("PATCH", URI, data)
					.then(
							function(response) {
								var storeWiseData = self.model.genericMapperList
										.find(function(type) {
											return type.id == self.model.id;
										});
								var storeData = self.model.stores
										.find(function(type) {
											return type.id == self.model.store.id;
										});
								var vedData = self.model.vedList.find(function(
										type) {
									return type.id == self.model.ved.id;
								});
								var assetCategoryData = self.model.assetCategoryList
										.find(function(type) {
											return type.id == self.model.assetCategory.id;
										});
								var productCategoryData = self.model.productCategoryList
										.find(function(type) {
											return type.id == self.model.productCategory.id;
										});

								storeWiseData.store = data.store;
								storeWiseData.genericCode = self.model.genericCode;
								storeWiseData.genericName = self.model.genericName;
								storeWiseData.productCategory = productCategoryData.category;
								storeWiseData.assetCategory = assetCategoryData.category;
								storeWiseData.ved = vedData.name;

								ClearFields();
								$rootScope.stopLoader();
								growl.success(response.data.message);
								self.model.activeProductCategoryId = undefined;
								// self.model.isEdit = false;
							});
			// }
		};

		// Method to clear the fileds
		function ClearFields() {
			self.model.pagination.search = '';
			self.model.id = undefined;
			self.model.assetCategory = self.model.assetCategoryList[0];
			self.model.productCategory = [ self.model.productCategoryList[0] ];
			self.model.productCategory = self.model.productCategoryList[0];
			self.model.ved = self.model.vedList[0];
			self.model.store = self.model.stores[0];
			self.model.genericCode = "";
			self.model.genericName = "";
			self.model.search = '';
			// self.model.assetCategoryList.splice(1);
			self.model.productCategoryList.splice(1);
			// self.model.vedList.splice(1);
		}

		var clearSearch = function() {
			self.model.pagination.search = "";
			PaginationService.searchData(self.model.genericMapperList,
					self.model.pagination);
		}

		var ValidateGenericMaster = function() {
			var isValid = true;

			if (!self.model.assetCategory.id) {
				isValid = false;
				self.model.isValidCode = true;
			}
			if (!self.model.productCategory.id) {
				isValid = false;
				self.model.isValidProductCat = true;
			}
			if (!self.model.genericCode) {
				isValid = false;
				self.model.isValidGenericCode = true;
			}
			if (!self.model.genericName) {
				isValid = false;
				self.model.isValidGenericName = true;
			}
			if(self.model.autoGenericId == undefined){
				isValid = false;
				growl.error("Please select generic name");
			}
			return isValid;
		}
		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"storewiseGenericMapper",
						{
							url : "/storewiseGenericMapper",
							templateUrl : 'views/master/UnitMaster/pharmacy/storewise-generic-mapper.html',
							controller : "StoreWiseGenericMapper.Controller",
							title : 'Storewise Generic Mapper Master',
							resolve : {

								data : function($q, $state, $timeout) {
									var deferred = $q.defer();

									$timeout(function() {
										var arr = JSON.parse(localStorage
												.getItem('permissions')).modules
												.filter(function(item) {
													return (item.id == 635);
												});
										if (arr.length <= 0) {
											$state.go("accessDenied")
											deferred.reject();
										} else {
											deferred.resolve();
										}
									});

									return deferred.promise;
								}
							},
							controllerAs : "vm"
						});
	}

	angular.module("myApp").config(config).controller(
			"StoreWiseGenericMapper.Controller",
			storeWiseGenericMapperController);
})();