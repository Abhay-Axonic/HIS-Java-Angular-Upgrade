(function() {
	"use strict";
	var CACHE = {};

	function addNewCrashCartRequestController($scope, $state, $rootScope,
			$http, $filter, $cookies, CONSTANTS, StatusService, BillingGenericService, GenericService, growl,
			commonDetailService) {
		var self = this;
		var mainStoreList = [];
		var visitNumber = '';
		var cookieObjectAppConfig = {};
		[cookieObjectAppConfig] = $rootScope.checkCookies();

		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		$scope.nurseRole = cookieObjectAppConfig.nurseRole;	
		$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
		$scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
		var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
		$scope.nursingStationsWardsListId = nursingStationsWardsList;
		
		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
		function initializeController() {
			$rootScope.loginpage = true;
			self.model = {
				dateTime : '',
				dateTimeOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date()
				},
				storeList : [ {
					"store" : "All"
				} ],
				assetCategories : [ {
					"category" : "Select Asset Category"
				} ],
				productCategories : [ {
					"category" : "Select Product Category"
				} ],
				wardList : [ {
					"wardName" : "Select ward name"
				} ],
				storeList : [ {
					"store" : "All"
				} ],
				visitDetails : [ {
					"visitNo" : "Select Visit No"
				} ],
				visitTypeList : [ {
					"type" : "All"
				} ],
				SelectedItemsIds : [],
				selectAllRows : false,
				SelectedItemIds : [],
				ItemDetailList : [],
				// indentId: null,
				consignment : false,
				itemSearch : {
					itemCode : undefined,
					itemName : undefined,
					batchNo : undefined
				}
			};

			StatusService.GetStatus().then(function(status) {
				self.model.Status = status;
			})

			self.model.OpenDateTime = OpenDateTime;
			self.model.SearchpatientNames = SearchpatientNames;
			self.model.SearchUhid = SearchUhid;
			self.model.AddPatientDetails = AddPatientDetails;
			self.model.search = '';
			self.model.prevSearch = '';
			self.model.patientNames = [];
			self.model.GetItemList = GetItemList;
			self.model.GetAssetCategoryList = GetAssetCategoryList;
			self.model.GetProductCategoryList = GetProductCategoryList;
			self.model.SearchNewItem = SearchNewItem;
			self.model.NewItemList = [];
			self.model.SelectItem = SelectItem;
			self.model.SelectAllItems = SelectAllItems;
			self.model.AddItems = AddItems;
			self.model.RemoveRowData = RemoveRowData;
			self.model.Validate = Validate;
			self.model.SaveNewCrashCartRequest = SaveNewCrashCartRequest;
			self.model.NavigteToBillableConsumption = NavigteToBillableConsumption;
			// self.model.dateTimeCheck = dateTimeCheck;
			self.model.ChangeVisitNumber = ChangeVisitNumber;
			self.model.ChangeVisitType = ChangeVisitType;
			self.model.PatientNameKeyPress = PatientNameKeyPress;
			self.model.uHIDKeyPress = uHIDKeyPress;
			self.model.validateQty = validateQty;

			$scope.patientSearchObj.uhidNumber = '';
			$scope.patientSearchObj.patientName = '';
			$scope.patientSearchObj.genderId = '';
			$scope.birthDate = '';
			$scope.patientSearchObj.identificationTypeId = '';
			$scope.patientSearchObj.identificationNumber = '';

			// self.model.indentId = commonDetailService.getDataId();
			// GetPatientIndentDetails(commonDetailService.getDataId());

			PopulateDropdown();
			// GetAssetCategoryList();
		}

		var validateQty = function (item) {
			var isValid = true;
			if(item.quantity > item.stockAvailable){
				growl.error("Quantity is grater than available stock quantity");
				isValid = false;
				item.quantity = 0;
			}
			return isValid;
		}

		var OpenDateTime = function() {
			self.model.dateOpened = true;
			self.model.dateTimeOptions.minDate = self.model.dateTime;
		}

		// var dateTimeCheck = function(data) {
		// 	if (!data) {
		// 		growl.error('Enter Valid Date and Time');
		// 		return;
		// 	}
		// }

		var PopulateDropdown = function() {

			var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;

			self.model.visitType = self.model.visitTypeList[0];
			self.model.visitNumber = self.model.visitDetails[0];
			self.model.store = self.model.storeList[0];
			self.model.ward = self.model.wardList[0];

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.visitTypeList = self.model.visitTypeList
								.concat(response.data.visitType);
						self.model.storeList = self.model.storeList
								.concat(response.data.store);
						self.model.wardList = self.model.wardList
								.concat(response.data.ward);
						self.model.visitType = self.model.visitTypeList.find(function (obj) {
							return obj.id == 2;
						});
						$rootScope.stopLoader();
					});
		}

		// $scope.validatesearch = function () {
        //     var isValid = true;
        //     if(self.model.visitType.id == 0){
        //         growl.error("Please select visit type");
        //         isValid = false;
        //     }
        //     return isValid;
        // }

		var SearchpatientNames = function(search) {
			// if($scope.validatesearch()){
				if (search.length < 3)
				return;

				if ((!self.model.prevSearch || self.model.prevSearch != search)
						&& (search.length == 3) && (self.model.visitType.id)) {
					self.model.prevSearch = search;

					var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/"
							+ self.model.visitType.id;

					$rootScope.startLoader();
					return GenericService.serviceAction("GET", URI, {}).then(
							function(response) {
								$rootScope.stopLoader();
								if (!!response.data && response.data.length > 0) {
									self.model.patientNames = response.data;
									self.model.patientId = response.data.patientId;
									self.model.uhid = response.data.uhid;
									$rootScope.stopLoader();
									// console.log(self.model.patientNames.itemName);
									return $filter('filter')(
											self.model.patientNames, {
												$ : search
											});
								} else {
									$rootScope.stopLoader();
									return PatientNameNotFound(search);
								}
							}, function(err) {
								$rootScope.stopLoader();
								return PatientNameNotFound(search);
							});
				} else {
					if (!self.model.visitType.id) {
						growl
								.error('Please select visit type for fetching the patient name');
					}
					if (!!self.model.patientNames
							&& self.model.patientNames.length > 0
							&& self.model.patientNames[0].itemFound != undefined
							&& !self.model.patientNames[0].itemFound) {
						return PatientNameNotFound(search);
					} else {
						return $filter('filter')(self.model.patientNames, {
							$ : search
						});
					}
				}
			// }
		}

		var PatientNameNotFound = function(search) {
			var item = {
				"itemFound" : false,
				"details" : "Patient Not found with name " + search
			};
			self.model.patientNames = [ item ];
			return self.model.patientNames;
		}

		var SearchUhid = function(search) {

			if (search.length < 1)
				return;

			if ((!self.model.prevSearch || self.model.prevSearch != search)
					&& (search.length == 1)
					&& (self.model.patientNames.length > 0)) {
				self.model.prevSearch = search;

				var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search +'/' + $scope.organizationId;

				$rootScope.startLoader();
				return GenericService
						.serviceAction("GET", URI, {})
						.then(
								function(response) {
									$rootScope.stopLoader();
									if (!!response.data
											&& response.data.length > 0) {
										self.model.patientNamesForUHIID = response.data;
										self.model.patientId = response.data.patientId;
										// self.model.uhid = response.data.uhid;
										$rootScope.stopLoader();
										// console.log(self.model.patientNames.itemName);
										return $filter('filter')(
												self.model.patientNames, {
													$ : search
												});
									} else {
										$rootScope.stopLoader();
										return UHIDNotFound(search);
									}
								}, function(err) {
									$rootScope.stopLoader();
									return UHIDNotFound(search);
								});
			} else {
				if (self.model.patientNames.length == 0) {
					$rootScope.stopLoader();
					growl.error('Please select patient name for fetching UHID');
				}
				if (!!self.model.patientNames
						&& self.model.patientNames.length > 0
						&& self.model.patientNames[0].itemFound != undefined
						&& !self.model.patientNames[0].itemFound) {
					return UHIDNotFound(search);
				} else {
					return $filter('filter')(self.model.patientNames, {
						$ : search
					});
				}
			}
		}

		var UHIDNotFound = function(search) {
			var item = {
				"itemFound" : false,
				"details" : "UHID Not found with search " + search
			};
			self.model.patientNames = [ item ];
			return self.model.patientNames;
		}

		var ChangeVisitNumber = function(visitData) {
			self.model.encounterId = visitData.encounterId;
			self.model.admissionId = visitData.admissionId;
			self.model.doctorId = visitData.doctorId;
		}

		var AddPatientDetails = function(item, index) {
			var radio = document.getElementById('patientSelectRadioCommon_' + index);
			radio.checked = true;
			if (!!item.patientId) {
				var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_SEARCH_API +'/' + $scope.organizationId + '/'+$scope.unitId;

				var data = {
					"visitType" : !!self.model.visitType
							&& !!self.model.visitType.id ? self.model.visitType.id
							: 0,
					"patientId" : !!item.patientId ? item.patientId : undefined,
				// "uhid": !!item.uhid ? item.uhid : undefined,
				};

				$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {

											self.model.patientInfo = response.data,
											self.model.patientName = response.data.patientName,
											self.model.patientId = response.data.patientId,
											self.model.uhid = response.data.uhid,
											self.model.bedNo = response.data.bedNo,
											self.model.bedId = response.data.bedId
									// self.model.encounterId =
									// response.data.encounterId,
									// self.model.admissionId =
									// response.data.admissionId

									if (self.model.visitType.id == 1) {
//										self.model.visitDetails = self.model.visitDetails
//												.concat(response.data.visitDetails);
										var date = new Date();
										$scope.dt = moment(date).format('YYYY-MM-DD');
										angular.forEach(response.data.visitDetails,function(value,key){
			                                
											if(moment(value.encounterDate).format('YYYY-MM-DD') ==  $scope.dt) {
											self.model.visitDetails = self.model.visitDetails.concat(value);
											self.model.visitNumber = self.model.visitDetails[1];
											self.model.encounterId = value.encounterId;
											self.model.admissionId = value.admissionId;
											self.model.doctorId = value.doctorId;
											
											}
										});
									} else if (self.model.visitType.id != 1) {
										self.model.visitNumberText = response.data.visitDetails[0].visitNo;
												self.model.encounterId = response.data.visitDetails[0].encounterId,
												self.model.admissionId = response.data.visitDetails[0].admissionId,
												self.model.doctorId = response.data.visitDetails[0].doctorId
									}

									self.model.ward = self.model.wardList
											.find(function(ward) {
												return response.data.wardId == ward.id;
											});

									$rootScope.stopLoader();
								}, function(err) {
									// growl.error('Error while fetching the
									// patient details');
									growl.error(err.data.summary);

									$rootScope.stopLoader();
								});
			}
		}

		var ClearPatientInfo = function() {
			self.model.patientData = null;
			self.model.patientId = null;
			self.model.bedNo = null;
			self.model.bedId = null;
			self.model.ward = self.model.wardList[0];
			self.model.visitNumberText = null;
			self.model.encounterId = null;
			self.model.admissionId = null;
			self.model.doctorId = null;
			self.model.visitNumber = self.model.visitDetails.splice(1);
			self.model.visitNumber = self.model.visitDetails[0];
			self.model.patientInfo = null;
		}

		var ChangeVisitType = function() {
			self.model.patientName = null;
			self.model.uhid = null;
			self.model.patientNames = [];
			ClearPatientInfo();
		}

		var PatientNameKeyPress = function(event) {
			var search = $filter('filter')(self.model.patientNames, {
				$ : self.model.patientName
			});
			if (!self.model.patientName) {
				self.model.uhid = null;
				ClearPatientInfo();
			}
		}

		var uHIDKeyPress = function(event) {
			var search = $filter('filter')(self.model.patientNames, {
				$ : self.model.uhid
			});
			if (!self.model.uhid) {
				self.model.patientName = null;
				ClearPatientInfo();
			}
		}

		var ValidateToGetItems = function() {
			var isValid = true;

			if (!self.model.store.id) {
				self.model.isStoreValid = true;
				isValid = false;
				growl.error('Select store')
			}
			if (!self.model.patientId) {
				self.model.isStoreValid = true;
				isValid = false;
				growl.error('Select Patient')
			}

			if(self.model.visitType.id == 1){
				if(!self.model.visitNumber){
					isValid = false;
					growl.error('Select visit number')
				}
			}
			else{
				if(!self.model.visitNumberText){
					isValid = false;
					growl.error('Select visit number')
				}
			}
			

			return isValid;
		}

		var GetItemList = function() {
			if (ValidateToGetItems()) {
				GetAssetCategoryList();
				self.model.NewItemList = [];
				self.model.selectAllRows = false,
						self.model.itemSearch.itemCode = undefined;
				self.model.itemSearch.itemName = undefined;
				self.model.itemSearch.batchNo = undefined;
				self.model.action = 'Open';
				var popup = angular.element('#addNewItemWasteStore');
				popup.modal('show');
			}
		}

		var GetAssetCategoryList = function() {
			var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

			self.model.assetCategory = self.model.assetCategories[0];
			self.model.productCategory = self.model.productCategories[0];

			self.model.assetCategories.splice(1);
			self.model.productCategories.splice(1);

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.assetCategories = self.model.assetCategories
								.concat(response.data);
						$rootScope.stopLoader();
						self.model.search.itemName = '';
					}, function(err) {
						$rootScope.stopLoader();
					});
		}

		var GetProductCategoryList = function(id) {

			var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
			self.model.productCategory = self.model.productCategories[0];
			self.model.productCategories.splice(1);

			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								self.model.productCategories = self.model.productCategories
										.concat(response.data);
								$rootScope.stopLoader();
							}, function(err) {
								$rootScope.stopLoader();
							});
		}

		var SearchNewItem = function() {
			if(self.model.search.itemName){
				if(self.model.search.itemName.length >= 3){
					// var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
					var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

					var data = {
						"assetTypeId" : 1,
						"storeId" : self.model.store.id ? self.model.store.id
								: undefined,
						"assetCategoryId" : self.model.assetCategory.id ? self.model.assetCategory.id
								: undefined,
						"productCategoryId" : self.model.productCategory.id ? self.model.productCategory.id
								: undefined,
						"itemCode" : self.model.search.itemCode,
						"itemName" : self.model.search.itemName
					}
					self.model.NewItemList = [];
					$rootScope.startLoaderPopup();
					GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						$rootScope.stopLoaderPopup();
						self.model.NewItemList = response.data;
					}, function errorCallback(response) {
						$rootScope.stopLoaderPopup();
						growl.error('Error');
					});
				}
			}
			else if(self.model.search.itemCode){
				if(self.model.search.itemCode.length >= 3){
					var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

					var data = {
						"assetTypeId" : 1,
						"storeId" : self.model.store.id ? self.model.store.id
								: undefined,
						"assetCategoryId" : self.model.assetCategory.id ? self.model.assetCategory.id
								: undefined,
						"productCategoryId" : self.model.productCategory.id ? self.model.productCategory.id
								: undefined,
						"itemCode" : self.model.search.itemCode,
						"itemName" : self.model.search.itemName
					}
					self.model.NewItemList = [];
					$rootScope.startLoaderPopup();
					GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						$rootScope.stopLoaderPopup();
						self.model.NewItemList = response.data;
					}, function errorCallback(response) {
						$rootScope.stopLoaderPopup();
						growl.error('Error');
					});
				}
			}
			else{
				self.model.NewItemList = [];
			}
		}

		var SelectAllItems = function() {
			if (self.model.selectAllRows) {
				if (self.model.SelectedItemsIds) {
					self.model.data = self.model.NewItemList
							.map(function(item) {
								item.isSelected = true;
								self.model.SelectedItemsIds.push(item.itemId);
								return self.model.SelectedItemsIds;
							});
				} else {
					self.model.SelectedItemsIds = self.model.NewItemList
							.map(function(item) {
								item.isSelected = true;
								self.model.isItemChecked = true;
								return item.itemId;
							});
				}
			} else {
				self.model.SelectedItemsIds = [];
				self.model.NewItemList.forEach(function(item) {
					item.isSelected = false;
					self.model.isItemChecked = false;
				});
			}
		}

		var SelectItem = function(item) {
			var itemIds = self.model.SelectedItemsIds;

			if (item.isSelected) {
				itemIds.push(item.itemId);
			} else {
				var index = itemIds.indexOf(item.itemId);
				if (index > -1) {
					itemIds.splice(index, 1);
				}
			}

			if (self.model.NewItemList.every(isSelected)) {
				self.model.selectAllRows = true;
			} else {
				self.model.selectAllRows = false;
			}

			self.model.isItemChecked = false;
			self.model.NewItemList.forEach(function(item) {
				if (item.isSelected == true) {
					self.model.isItemChecked = true;
					return;
				}
			});
		}

		var isSelected = function(item) {
			return item.isSelected;
		}

		$scope.validateItem = function () {
			var isValid = true;
			if(self.model.SelectedItemsIds.length == 0){
				growl.error("Please select item");
				isValid = false;
			}
			return isValid;
		}

		var AddItems = function() {
			if($scope.validateItem())
			{
				// var URI = constants.apiurl + '/api/common/items/details';
				var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

				var data = {
					"itemIds" : self.model.SelectedItemsIds,
					"storeId" : self.model.store.id
				};
				$rootScope.startLoaderPopup();
				GenericService.serviceAction("POST", URI, data).then(
				function(response) {
					response.data.forEach(function(item) {

						var isFound = self.model.ItemDetailList
								.find(function(x) {
									return x.itemId == item.itemId;
								});
						if (!isFound) {
							self.model.ItemDetailList.push(item);
						}

					});
					// self.model.ItemDetailList =
					// self.model.ItemDetailList.concat(response.data);
					self.model.SelectedItemsIds = [];
					self.model.items = [];
					var selectedUom;
					angular.forEach(self.model.ItemDetailList, function(
							item) {
						if (!item.id) {
							selectedUom = item.uom.find(function(obj) {
								return obj.opUom;
							});

							item["uomType"] = selectedUom.uomDetails;
							item["uomUnit"] = selectedUom.uomUnit;
							item["uomTypeId"] = selectedUom.uomTypeId;
							item["uomUnitId"] = selectedUom.uomUnitId;
						}

						if(item.quantity == undefined || item.quantity == null){
							item.quantity = 0;
						}
						
						if(item.isThreeUOMApplicable == false){
                            if(item.uomTypeId ==1){
								item.receivedQtyCon=(item.quantity +"*"+ item.uom[0].conversion);
								item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
								item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                
                            }
                            else{
                                item.receivedQtyCon=(item.quantity);
								item.receivedQtyCalculation = (item.quantity);
								item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uomTypeId ==1){
								item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
								item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
								item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
								item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
								item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon=(item.quantity);
								item.receivedQtyCalculation = (item.quantity);
								item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
					});

					$rootScope.stopLoaderPopup();
					self.model.searchNewItemList = [];
					if (self.model.search.itemName != undefined
							&& self.model.search.itemCode != undefined) {
						self.model.search.itemName = '';
						self.model.search.itemCode = '';
					}
					var popup = angular.element('#addNewItemWasteStore');
					popup.modal('hide');

				}, function(err) {
					$rootScope.stopLoaderPopup();
					growl.error(err.data.message);
				});
			}
		}

		var RemoveRowData = function(index) {
			var ItemDetailList = self.model.ItemDetailList;
			self.model.ItemDetailList = ItemDetailList.slice(0, index).concat(
					ItemDetailList.slice(index + 1))
		}

		var Validate = function() {
			var isValid = true;
			$scope.saveBtnDisabled = true;
			self.model.isPatientNameValid = false;
			self.model.isVisitTypeValid = false;
			self.model.isVisitNoValid = false;
			self.model.isStoreValid = false;

			if (!self.model.patientName) {
				self.model.isPatientNameValid = true;
				isValid = false;
				growl.error('Enter patient name');
				$scope.saveBtnDisabled = false;
			}
			if (!self.model.visitType.id) {
				self.model.isVisitTypeValid = true;
				isValid = false;
				growl.error('Select visit type');
				$scope.saveBtnDisabled = false;
			}
			if (!self.model.store.id) {
				self.model.isStoreValid = true;
				isValid = false;
				growl.error('Select store');
				$scope.saveBtnDisabled = false;
			}
			if (self.model.visitType) {
				if (self.model.visitType.id == 1) {
					if (!self.model.visitNumber.visitNo) {
						self.model.isVisitNoValid = true;
						isValid = false;
						growl.error('Select visit no');
						$scope.saveBtnDisabled = false;
					}
				} else {
					if (!self.model.visitNumberText) {
						self.model.isVisitNoValid = true;
						isValid = false;
						growl.error('Select visit no');
						$scope.saveBtnDisabled = false;
					}
				}
			}


			 if(self.model.encounterId == undefined && self.model.admissionId == undefined){
        		isValid = false;
				growl.error("Select visit no");
				$scope.saveBtnDisabled = false;
			 }
			
			
			var ItemDetailList = self.model.ItemDetailList;
			if (!ItemDetailList || ItemDetailList.length <= 0) {
				isValid = false;
				growl.error("Please add some items in the bill");
				$scope.saveBtnDisabled = false;
			} else {
				ItemDetailList.forEach(function(item) {

					self.model.isRemarkValid = false;
					item['isQuantityValid'] = false;

					if (!item.quantity
							|| item.availableQuantity < item.quantity) {
						item['isQuantityValid'] = true;
						isValid = false;
						growl.error('Enter valid quantity');
						$scope.saveBtnDisabled = false;
					}

					if (item.quantity == 0) {
						item['isQuantityValid'] = true;
						isValid = false;
						growl.error('Enter quantity greater than zero');
						$scope.saveBtnDisabled = false;
					}
				});
			}

			if (!self.model.dateTime) {
				isValid = false;
				growl.error("Please enter codeblue date");
				$scope.saveBtnDisabled = false;
			}

			if(!$scope.codeblueTime){
				isValid = false;
				growl.error("Please enter codeblue time");
				$scope.saveBtnDisabled = false;
			}

			return isValid;
		}

		var SetStatus = function(x) {
			var status = self.model.Status.find(function(obj) {
				return obj.status == x;
			});
			return status.id;
		}

		$scope.saveBtnDisabled = false;
		var SaveNewCrashCartRequest = function() {
			$scope.saveBtnDisabled = true;
			var ItemDetailList = self.model.ItemDetailList;
			if (Validate()) {
				$scope.saveBtnDisabled = true;
				var URI = CONSTANTS.PHARMACY.NURSING.CRASH_CART.SAVE_API;

				var data = {

					"patientId" : !!self.model.patientId ? self.model.patientId
							: undefined,
					"doctorId" : !!self.model.doctorId && self.model.doctorId >0 ? self.model.doctorId
							: undefined,
					"visitType" : !!self.model.visitType
							&& !!self.model.visitType.id ? self.model.visitType.id
							: undefined,
					"admissionId" : !!self.model.admissionId ? self.model.admissionId
							: undefined,
					"encounterId" : !!self.model.encounterId ? self.model.encounterId
							: undefined,
					"storeId" : self.model.store.id ? self.model.store.id
							: undefined,
					"wardId" : !!self.model.ward.id ? self.model.ward.id
							: undefined,
					"bedId" : !!self.model.bedId ? self.model.bedId : undefined,
					"statusId" : SetStatus('New'),
					"remark" : self.model.remark,
					"codeBlueDateTime" : moment(self.model.dateTime).format(
							"YYYY-MM-DD"),
					"items" : [],
					"unitId":$scope.unitId,
		            "organizationId":$scope.organizationId,
		            "userId":$scope.userId
		            // "storeId":$scope.storeId

				}

				data.codeBlueDateTime = data.codeBlueDateTime + " "
						+ $scope.codeblueTime;

				angular.forEach(ItemDetailList, function(item, key) {
					var item = {
						"itemId" : item.itemId,
						"uomTypeId" : item.uomTypeId,
						"uomUnitId" : item.uomUnitId,
						"requiredQuantity" : parseInt(item.quantity),
						"statusId" : SetStatus('New')
					}
					data.items.push(item);
				});

				$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									NavigteToBillableConsumption();
									$rootScope.stopLoader();
									$scope.saveBtnDisabled = false;
									growl.success('New crash cart request Successfully saved');
								},
								function(err) {
									$rootScope.stopLoader();
									// growl.error('Error while saving new crash
									// cart request');
									if (err.data.summary == ' Please configure prefix number ') {
										$scope.saveBtnDisabled = false;
										growl.error(err.data.summary);
									} else {
										$scope.saveBtnDisabled = false;
										growl.error('Error while saving new crash cart request');
									}
								});
			}
		}

		var NavigteToBillableConsumption = function() {
			$state.go('crashCartRequest');
		}

		// function for initializa clockpicker
		$scope.initClockPicker = function() {

			$('.clockpicker').clockpicker();
		}

		$scope.isWardChanged = function()
		{      
			self.model.patientName = "";
			self.model.uhid = "";
			self.model.visitNumber = "";
			self.model.visitNumberText = "";
			$scope.searchPatientList = [];
			$scope.patientSearchObj.uhidNumber = '';
			$scope.patientSearchObj.patientName = '';
			$scope.patientSearchObj.genderId = '';
			$scope.birthDate = '';
			$scope.patientSearchObj.identificationTypeId = '';
			$scope.patientSearchObj.identificationNumber = '';

			//start for nursing
			var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
			GenericService.serviceAction("GET",URI,{}).then(function(response){
				if (response.data.status == "success") {
					$scope.departmentNursingStationMapperDtoslist = [];			  
					$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
					//alert($scope.roleName);
					if($scope.nurseRole == 1){
						if($scope.departmentNursingStationMapperDtoslist.length > 0)
						{
							if($scope.departmentNursingStationMapperDtoslist.length > 1){
								angular.element('#nursingStationModal').modal('show');
								//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
							}
							else
							{
								var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET",URI,{}).then(function(response){
									if (response.data.status == "success") {
										$scope.departmentNursingStationListC = {};
										// console.log("fff",response.data.object);
										$scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
										sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
										sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
										sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
										  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
										//$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
										sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
										$scope.wardListForGetBedList = response.data.object;
									}
								});
							}
						}
					}
				}
			});        
		}

		$scope.departmentNursing = {};
		$scope.selectsetNursingStation = function()
		{
			self.model.wardIdFromService = [];
		   
			if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
				growl.warning('Please select nursing station..!!');
				return false;
			}else{
			$scope.wardListForGetBedList=[];
			var data = JSON.parse($scope.departmentNursing);
			//console.log("$scope.departmentNursing",$scope.departmentNursing);
			//console.log("$scope.departmentNursing ID",data.nursingStationId);
			var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
				GenericService.serviceAction("GET",URI,{}).then(function(response){
					if (response.data.status == "success") {
						$scope.departmentNursingStationListC = {};
						//console.log('nursingStationsWardsList',response.data.object);
						$scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
						sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
						sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
						sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
						sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
						$scope.nursingStationId = data.nursingStationId;
						sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
						$scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
						$scope.wardListForGetBedList = response.data.object;
						self.model.ward.id = $scope.wardListForGetBedList;
						$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
						self.model.wardIdFromService = $scope.wardListForGetBedList;
						angular.element('#nursingStationModal').modal('hide');
					}
				});
			}
		}

		$scope.genderList = function () {
			var data = {};
			var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
			BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
				console.log(response);
				$scope.genderList = [];
				if (response.data.status == "success")
					$scope.genderList = response.data.listObject;
			});
		}
		$scope.genderList();

		$scope.identificationType = function () {
			var data = {};
			var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
			BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
				console.log(response);
				$scope.identificationList = [];
				if (response.data.status == "success")
					$scope.identificationList = response.data.listObject;
			});
		}
		$scope.identificationType();

		$scope.openDatePicker = function () {
			$scope.datepicker.opened = true;
		};

		$scope.birthDateOptions = {
			formatYear: 'yyyy',
			showWeeks: false,
			maxDate: new Date()
		};

		$scope.datepicker = {
			opened: false
		};

		$scope.searchPatient = function (patientSearchData) {
			self.model.patientName = "";
			self.model.uhid = "";
			self.model.visitNumber = "";
			self.model.visitNumberText = "";
			$scope.searchPatientList = [];
			if (!!$scope.birthDate) {
				var birthDate = moment($scope.birthDate).format('YYYY-MM-DD');
			} else {
				var birthDate = undefined;
			}
			if($scope.wardListForGetBedList == undefined){
				$scope.wardListForGetBedList = $scope.nursingStationsWardsListId;
			}
			var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteria/global';
			var data = {
				"uhidNumber": patientSearchData.uhidNumber,
				"patientName": patientSearchData.patientName,
				"genderId": patientSearchData.genderId,
				"mobileNumber": patientSearchData.mobileNumber,
				"birthDate": birthDate,
				"identificationNumber": patientSearchData.identificationNumber,
				"identificationTypeId": patientSearchData.identificationTypeId,
				"unitId": $scope.unitId,
				"organizationId": $scope.organizationId,
				"wardList": $scope.wardListForGetBedList,
				"visitTypeId": self.model.visitType.id
			}
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(
			function (response) {
				$scope.searchPatientList = response.data.listObject; 
				$rootScope.stopLoader();
			},
			function errorCallback(response) {
				$rootScope.stopLoader();
			});
		}

		$scope.calculateReceivedQty = function (item) {
			if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
				item.quantity = 0;
			}
			if(item.isThreeUOMApplicable == false){
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
					item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
					item.uomUnitData = item.uom[2].uomUnit;
				}
			}
			else{
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
					item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
					item.uomUnitData = item.uom[2].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
					item.uomUnitData = item.uom[2].uomUnit;
				}
			}
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"addNewCrashCartRequest",
						{
							url : "/addNewCrashCartRequest",
							templateUrl : 'views/PharmacyInventory/InPatient/add-new-crash-cart-request.html',
							controller : "AddNewCrashCartRequest.Controller",
							controllerAs : "vm",
							title : 'Crash Cart Request'
						});
	}

	angular.module("myApp").config(config).controller(
			"AddNewCrashCartRequest.Controller",
			addNewCrashCartRequestController);
})();