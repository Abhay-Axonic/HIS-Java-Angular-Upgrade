(function() {
	"use strict";

	function itemCreationDetailsController($rootScope, CurrencyService, $scope,
			$http, $cookies, CONSTANTS, $state, GenericService, commonDetailService,
			rejectReasonService, StatusService, growl) {
		var self = this;

		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
			$rootScope.loginpage = true;

			self.model = {
				SelectedItems : []
			};

			StatusService.GetStatus().then(function(status) {
				self.model.statusList = status;
			});

			rejectReasonService.GetReason().then(function(reason) {
				self.model.rejectReasons = reason;
			});

			CurrencyService.GetCurrency().then(function(currency) {
				self.model.Currency = currency;
			});

			self.model.NavigateToBack = NavigateToBack;
			self.model.NavigateToItemMaster = NavigateToItemMaster;
			self.model.NavigateToAssetMaster = NavigateToAssetMaster;
			self.model.CreateItem = CreateItem;
			self.model.CreateAsset = CreateAsset;
			self.model.isDisable = isDisable;
			// self.model.SelectItem = SelectItem;
			GetPurchaseRequest(commonDetailService.getDataId());
		}

		var setStatus = function(x) {
			var status = self.model.statusList.find(function(obj) {
				return obj.status == x;
			});

			return status.id;
		}

		var GetPurchaseRequest = function(id) {
			//commonDetailService.setDataId(null);
			if(id != undefined  &&  id != null){
			var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_CREATION.DETAILS_API
					+ id;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.itemDetails = response.data;
						self.model.requestItems = JSON.parse(JSON
								.stringify(response.data.items));
						$rootScope.stopLoader();
					}, function(err) {
						$rootScope.stopLoader();
						growl.error(err.data.message);
					});
					}
		}

		var NavigateToBack = function() {
			$state.go('itemCreationPendingList');
		}

		var NavigateToItemMaster = function() {

			$state.go('itemMasterGlobal');
		}

		var NavigateToAssetMaster = function() {
			$state.go('fixedAssetMaster');
		}

		var ValidItem = function() {
			var isValid = true;
			if (!selectedItem()) {
				growl.error('Please select item');
				isValid = false;
			}

			return isValid;
		}

		var CreateItem = function() {
			if (ValidItem()) {
				commonDetailService.setDataId(selectedItem());
				NavigateToItemMaster();
			}
		}

		var CreateAsset = function() {
			if (ValidItem()) {
				commonDetailService.setDataId(selectedItem());
				commonDetailService.setManufacturer(
						self.model.itemDetails.items[0].manufacturer,
						self.model.itemDetails.items[0].manufacturerId,
						self.model.itemDetails.items[0].id);
				NavigateToAssetMaster();
			}
		}

		var selectedItem = function() {
			var returnItem = [];
			self.model.itemDetails.items
					.find(function(item) {
						if (item.isSelected) {
							returnItem = item;
							returnItem["assetType"] = self.model.itemDetails.assetType;
							returnItem["assetTypeId"] = self.model.itemDetails.assetTypeId;
							returnItem["manufacturer"] = item.manufacturer;
							returnItem["manufacturerId"] = item.manufacturerId;
						}
					});
			return returnItem;
		}

		var isDisable = function(purchase) {
			if (purchase.statusId !== 5) {
				return true;
			}
			return false;
		}

		$scope.updateSelection = function(position, purchase) {
			angular.forEach(purchase, function(item, index) {
				if (position != index){
					item.isSelected = false;
				}
				else if(item.isSelected == true){
					self.model.itemDetails.isSelectedItem = true;
				}	
				else{
					self.model.itemDetails.isSelectedItem = false;
				}
			});
			if(self.model.itemDetails.assetType == 'Consumable' && self.model.itemDetails.isSelectedItem == true){
				var sbmt = document.getElementById("consumable");
				sbmt.disabled = false;
			}
			else if(self.model.itemDetails.assetType == 'Fixed' && self.model.itemDetails.isSelectedItem == true){
				var sbmt1 = document.getElementById("asset");
				sbmt1.disabled = false;
			}
			else{
				var sbmt = document.getElementById("consumable");
				var sbmt1 = document.getElementById("asset");
				sbmt.disabled = true;
				sbmt1.disabled = true;
			}
		}

		$scope.itemDisabled = function(statusId) {

			if (statusId == 8) {

				return true;

			}

		}

		var isSelected = function(item) {
			if (true)
				return true;
			else
				return item.isSelected;
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"itemCreationDetails",
						{
							url : "/itemCreationDetails",
							templateUrl : 'views/procurement/purchase/item-creation-details.html',
							controller : "ItemCreationDetails.Controller",
							controllerAs : "vm",
							title: 'Item Creation Pending List'
						});
	}

	angular.module("myApp").config(config).controller(
			"ItemCreationDetails.Controller", itemCreationDetailsController);
})();