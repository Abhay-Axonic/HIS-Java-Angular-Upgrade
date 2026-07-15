(function () {
	"use strict";
	var CACHE = {};

	function consumableIndentController($scope, $state, $rootScope, $http, $filter, CONSTANTS, StatusService, BillingGenericService, GenericService,PaginationService,  growl, commonDetailService,$cookies) {
		var self = this;
		var mainStoreList = [];
		var cookieObjectAppConfig = {};
		[cookieObjectAppConfig] = $rootScope.checkCookies();

		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		$scope.nurseRole = cookieObjectAppConfig.nurseRole;	
		$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
		$scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
		var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
		$scope.nursingStationsWardsListId = nursingStationsWardsList;

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
				storeList: [{
					"store": "All"
				}],
				fromstoreList: [{
                    "store": "All"
                  }],
				assetCategories: [{
					"category": "Select Asset Category"
				}],
				productCategories: [{
					"category": "Select Product Category"
				}],
				visitDetails: [{
					"visitNo": "Select Visit No"
				}],
				wardList: [{
					"wardName": "Select Ward"
				}],
				visitTypeList: [{
					"type": "Select VisitType"
				}],
				SelectedItemsIds: [],
				selectAllRows: false,
				// SelectedItemIds: [],
				ItemDetailList: [],
				indentId: null,
				consignment: false,
				searchItem: {
					itemName: undefined,
					itemCode: undefined
				},
			};

			StatusService.GetStatus().then(function (status) {
				self.model.Status = status;
			})

			self.model.SearchpatientNames = SearchpatientNames;
			self.model.SearchUhid = SearchUhid;
			self.model.AddPatientDetails = AddPatientDetails;
			self.model.search = '';
			self.model.prevSearch = '';
			self.model.patientNames = [];
			self.model.GetItemList = GetItemList;
			self.model.getPreviousPrescription = getPreviousPrescription;
			self.model.getPreviousIndent = getPreviousIndent;
			self.model.GetAssetCategoryList = GetAssetCategoryList;
			self.model.GetProductCategoryList = GetProductCategoryList;
			self.model.SearchNewItem = SearchNewItem;
			self.model.searchNewItemList = [];
			self.model.SelectItem = SelectItem;
			self.model.SelectAllItems = SelectAllItems;
			self.model.AddItems = AddItems;
			self.model.RemoveRowData = RemoveRowData;
			self.model.Validate = Validate;
			self.model.SaveConsumableIndent = SaveConsumableIndent;
			self.model.UpdatePatientIndent = UpdatePatientIndent;
			self.model.ChangeVisitNumber = ChangeVisitNumber;
			self.model.ChangeVisitType = ChangeVisitType;
			self.model.PatientNameKeyPress = PatientNameKeyPress;
			self.model.uHIDKeyPress = uHIDKeyPress;
			self.model.ValidateQuantity = ValidateQuantity;
			self.model.ConfirmRemove = ConfirmRemove;
			self.model.SetOldValueVisitType = SetOldValueVisitType;
			self.model.ConfirmVisitTypePopup = ConfirmVisitTypePopup;
			self.model.NavigteToPatientIndent = NavigteToPatientIndent;
			self.model.remark = "";
			self.model.Expand = Expand;
			// self.model.indentId = commonDetailService.getDataId();
			// GetPatientIndentDetails(commonDetailService.getDataId());

			$scope.patientSearchObj.uhidNumber = '';
			$scope.patientSearchObj.patientName = '';
			$scope.patientSearchObj.genderId = '';
			$scope.birthDate = '';
			$scope.patientSearchObj.identificationTypeId = '';
			$scope.patientSearchObj.identificationNumber = '';
			
			PopulateDropdown();
			GetAssetCategoryList();
		}

		var Expand = function (item) {
            item.Expanded = !item.Expanded;
        }

		var GetPatientIndentDetails = function (itemId) {

			var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.DETAIL_API + $scope.unitId + '/' + itemId;
			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					self.model.indentId = response.data.id;
					self.model.ItemDetailList = response.data.items;
					self.model.visitType = self.model.visitTypeList.find(function(type){
						return type.id == response.data.visitTypeId;
					});
					/*self.model.store = self.model.storeList.find(function(store){
						return store.id == response.data.storeId;
					});
					self.model.fromstore = self.model.fromstoreList.find(function(store){
						return store.id == response.data.issuingStoreId;
					});*/
					
					/*start code for resolved HIS-10694 After Click on Edit then  Requesting store is getting disappear and Issuing store getting change*/
					self.model.fromstore = self.model.fromstoreList.find(function(store){
						return store.id == response.data.storeId;
					});	
					self.model.store = self.model.storeList.find(function(store){
							return store.id == response.data.issuingStoreId;
						});						
					/*end code for resolved HIS-10694 After Click on Edit then  Requesting store is getting disappear and Issuing store getting change*/
					
					self.model.ward = self.model.wardList.find(function(ward){
						return ward.id == response.data.wardId;
					});
					
					
					self.model.consignment = response.data.consignment ;
					self.model.visitNumberText = response.data.visitNo ;
					self.model.patientName = response.data.patientName ;
					self.model.patientId = response.data.patientId ;
					self.model.uhid = response.data.uhid ;
					self.model.bedNo = response.data.bedNo ;
					self.model.bedId = response.data.bedId ;
					self.model.remark = response.data.remark ;
					
					self.model.itemDetails = [];
					self.model.ItemDetailList = response.data.items;
					self.model.ItemDetailList.forEach(function(item){
						 
						 item.selectedUom = item.uom.find(function (uom){
							 return item.uomType == uom.uomType;
						 });
						 item.uomType = item.selectedUom.uomDetails;
						 
						 if(item.isThreeUOMApplicable == false){
				              item.uomListdata = item.uom;
				              item.uom = [];
				              angular.forEach(item.uomListdata, function(data){
				                if(data.uomType != 'Stock'){
				                  item.uom.push(data);
				                }
				              });
				            }
						 	var conversionPurchase;
				            var conversionStock;
				            var conversionDespense;
						 var uomdata = item.selectedUom.uomDetails.split(' - ')[0];
						 if(item.isThreeUOMApplicable == false){
				                item.uomListdata.forEach(function (uom) {
				                  if (uom.uomTypeId == 1) {
				                    conversionPurchase = uom.conversion;
				                  }
				                  else if (uom.uomTypeId == 2) {
				                    conversionStock = uom.conversion
				                  }
				                  else {
				                    conversionDespense = uom.conversion
				                  }
				                });
				  
				                if (uomdata == 'P') {
				                  item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
				                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
				                  item.receivedQtyCon = (item.quantity +"*"+ conversionPurchase);
								  item.receivedQtyCalculation = (item.quantity * conversionPurchase);
								  item.uomUnitData = item.uomListdata[2].uomUnit;
				                }
				                else if (uomdata == 'S') {
				                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
				                  item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
				                }
				                else {
				                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
				                  item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
				                  item.receivedQtyCon = (item.quantity);
								  item.receivedQtyCalculation = (item.quantity);
								  item.uomUnitData = item.uomListdata[2].uomUnit;
				                }
				              }
				              else{
				                item.uom.forEach(function (uom) {
				                  if (uom.uomTypeId == 1) {
				                    conversionPurchase = uom.conversion;
				                  }
				                  else if (uom.uomTypeId == 2) {
				                    conversionStock = uom.conversion
				                  }
				                  else {
				                    conversionDespense = uom.conversion
				                  }
				                });
				  
				                if (uomdata == 'P') {
				                  item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
				                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
				                  item.receivedQtyCon = (item.quantity +"*"+ conversionPurchase +"*"+ conversionStock);
								  item.receivedQtyCalculation = (item.quantity * conversionPurchase * conversionStock);
								  item.uomUnitData = item.uom[2].uomUnit;
				                }
				                else if (uomdata == 'S') {
				                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
				                  item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
				                  item.receivedQtyCon = (item.quantity +"*"+ conversionStock);
								  item.receivedQtyCalculation = (item.quantity * conversionStock);
								  item.uomUnitData = item.uom[2].uomUnit;
				                }
				                else {
				                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
				                  item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
				                  item.receivedQtyCon = (item.quantity);
								  item.receivedQtyCalculation = (item.quantity);
								  item.uomUnitData = item.uom[2].uomUnit;
				                }
				              }
					});
					
					$rootScope.stopLoader();
				});

		}

		var PopulateDropdown = function () {

			var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

			self.model.visitType = self.model.visitTypeList[0];
			self.model.visitNumber = self.model.visitDetails[0];
			//self.model.store = self.model.storeList[0];
			self.model.ward = self.model.wardList[0];
			self.model.store = self.model.storeList[0];
            self.model.fromstore = self.model.fromstoreList[0];

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
					self.model.storeList = self.model.storeList.concat(response.data.toStore);
                    self.model.fromstoreList = self.model.fromstoreList.concat(response.data.fromStore);
					self.model.wardList = self.model.wardList.concat(response.data.ward);
					self.model.visitType = self.model.visitTypeList.find(function (obj) {
						return obj.id == 2;
					});
					var indentId = commonDetailService.getDataId();
					if (!!indentId) {
						commonDetailService.setDataId(null);
						GetPatientIndentDetails(indentId);
					}
					for(var i=0;i<self.model.fromstoreList.length;i++)
					{
						if(self.model.fromstoreList[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
						{
							self.model.fromstore = self.model.fromstoreList[i];
						}
					}
					console.log(self.model.store.id, self.model.fromstore.id)
					$rootScope.stopLoader();
				},
				function (err) {
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

		  $scope.kitMasterList = [];
		  $scope.kitId = null;
		  $scope.getKitMasterList = function () {
	           var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.GET_ALL + $scope.organizationId + "/" +$scope.unitId ;
	            $rootScope.startLoader();
	            GenericService.serviceAction("GET", URI, {}).then(
	                function (response) {
	                	
	                $scope.kitMasterList =[];
	               	  angular.forEach(response.data, function (x) {
	               		  if(x.status == true){
	               			  $scope.kitMasterList.push(x);
	               		  }
	               	  });
	                    
	                    $rootScope.stopLoader();
	                },
	                function (err) {
	                    $rootScope.stopLoader();
	                });
	        }
		  $scope.getKitMasterList();
		

		 $scope.kitMasterItemsList = [];
		 $scope.kitItemDetails =  function(){$scope.kitMasterItemsList = [];

         var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/" + $scope.kitId + "/" + self.model.fromstore.id + "/" +$scope.unitId ;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
            	 
            	  $scope.kitMasterItemsList = response.data.listObject;
            	  angular.element('#itemDetailsModalIndent').modal('show');
                  $rootScope.stopLoader();
              },
              function (err) {
                  $rootScope.stopLoader();
              });
          }
		 
		var SearchpatientNames = function (search) {
			// if($scope.validatesearch()){
				if (search.length < 3) return;

				if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)&& (self.model.visitType.id)) {
					self.model.prevSearch = search;

					var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API_PATIENT_INDENT + $scope.unitId +  "/" + search  + "/" +self.model.visitType.id; 

					$rootScope.startLoader();
					return GenericService.serviceAction("GET", URI, {})
						.then(function (response) {
							$rootScope.stopLoader();
							if (!!response.data && response.data.length > 0) {
								self.model.patientNames = response.data;
								self.model.patientId = response.data.patientId;
								self.model.uhid = response.data.uhid;
								$rootScope.stopLoader();
								//console.log(self.model.patientNames.itemName);
								return $filter('filter')(self.model.patientNames, {
									$: search
								});
							} else {
								$rootScope.stopLoader();
								return PatientNameNotFound(search);
							}
						}, function (err) {
							$rootScope.stopLoader();
							return PatientNameNotFound(search);
						});
				} else {
					if(!self.model.visitType.id){
						growl.error('Please select visit type for fetching the patient name');
					}
					if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
						self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
						return PatientNameNotFound(search);
					} else {
						return $filter('filter')(self.model.patientNames, {
							$: search
						});
					}
				}
			// }
		}

		var PatientNameNotFound = function (search) {
			var item = {
				"itemFound": false,
				"details": "Patient Not found with name " + search
			};
			self.model.patientNames = [item];
			return self.model.patientNames;
		}

		var SearchUhid = function (search) {

			if (search.length < 1) return;

			if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
				self.model.prevSearch = search;

				var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search +'/' + $scope.organizationId;

				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {})
					.then(function (response) {
						$rootScope.stopLoader();
						if (!!response.data && response.data.length > 0) {
							self.model.patientNames = response.data;
							self.model.patientId = response.data.patientId;
							// self.model.uhid = response.data.uhid;
							$rootScope.stopLoader();
							//console.log(self.model.patientNames.itemName);
							return $filter('filter')(self.model.patientNames, {
								$: search
							});
						} else {
							$rootScope.stopLoader();
							return UHIDNotFound(search);
						}
					}, function (err) {
						$rootScope.stopLoader();
						return UHIDNotFound(search);
					});
			} else {
				if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
					self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
					return UHIDNotFound(search);
				} else {
					return $filter('filter')(self.model.patientNames, {
						$: search
					});
				}
			}
		}

		var UHIDNotFound = function (search) {
			var item = {
				"itemFound": false,
				"details": "UHID Not found with search " + search
			};
			self.model.patientNames = [item];
			return self.model.patientNames;
		}

		var ChangeVisitNumber = function (visitData) {
			self.model.encounterId = visitData.encounterId;
			self.model.admissionId = visitData.admissionId;
			self.model.doctorId = visitData.doctorId;
		}

		var AddPatientDetails = function (item, index) {
			var radio = document.getElementById('patientSelectRadioCommon_' + index);
			radio.checked = true;
			if (!!item.patientId) {
				var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_NURSING_SEARCH_API;

				var data = {
					"visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : 0,
					"patientId": !!item.patientId ? item.patientId : undefined,
					"unitId": $scope.unitId
				};

				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data)
					.then(function (response) {
						$scope.isFinalBill=response.data.isFinalBill;
						if($scope.isFinalBill=='Y')
						{
						 growl.warning("The final bill has been generated for the selected patient; therefore, creating a patient indent is not possible.");
						 $rootScope.stopLoader();
						 return false;
						}
						self.model.patientInfo = response.data,
							self.model.patientName = response.data.patientName,
							self.model.patientId = response.data.patientId,
							self.model.uhid = response.data.uhid,
							self.model.bedNo = response.data.bedNo,
							self.model.bedId = response.data.bedId
						// self.model.encounterId = response.data.encounterId,
						// self.model.admissionId = response.data.admissionId
						if (!!response.data.visitDetails.length) {
							if (self.model.visitType.id == 1) {
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
								})
									
							} else if (self.model.visitType.id != 1) {
								self.model.visitNumberText = response.data.visitDetails[0].visitNo;
								self.model.encounterId = response.data.visitDetails[0].encounterId,
									self.model.admissionId = response.data.visitDetails[0].admissionId,
									self.model.doctorId = response.data.visitDetails[0].doctorId
							}
						}

						self.model.ward = self.model.wardList.find(function (ward) {
							return response.data.wardId == ward.id;
						});


						$rootScope.stopLoader();
					}, function (err) {
						let error = err.data.summary ? err.data.summary : err.data.message
						growl.error(error);
						$rootScope.stopLoader();
					});
			}
		}

		var ClearPatientInfo = function () {
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
 
		var SetOldValueVisitType = function (visitType) {
			self.model.oldVisitType = visitType;
			self.model.visitType = self.model.oldVisitType;
		  }
	  
		  var ChangeVisitType = function (visitType) {
			if (!!self.model.ItemDetailList.length) {
			  self.model.visitType = self.model.oldVisitType;
			  self.model.visitTypeNew = JSON.parse(JSON.stringify(visitType));
			 
			  $("#storeChange").modal()
			}
			self.model.patientNames = [];
		  }
	  
		  var ConfirmVisitTypePopup = function () {
			self.model.visitType = self.model.visitTypeNew;
			self.model.ItemDetailList = [];
			self.model.patientName = null;
			self.model.uhid = null;
			ClearPatientInfo();
			ModalDismiss('storeChange');
		  }
	  
	  
		var PatientNameKeyPress = function (event) {
			var search = $filter('filter')(self.model.patientNames, {
				$: self.model.patientName
			});
			if (!self.model.patientName) {
				self.model.uhid = null;
				ClearPatientInfo();
			}
		}

		var uHIDKeyPress = function (event) {
			var search = $filter('filter')(self.model.patientNames, {
				$: self.model.uhid
			});
			if (!self.model.uhid) {
				self.model.patientName = null;
				ClearPatientInfo();
			}
		}

		var GetItemList = function () {
			if (self.model.fromstore.id == self.model.store.id) {
        		growl.error("Please Select Different Stores");
      		}
      		else {
				self.model.searchNewItemList = [];
				self.model.pagination.data=[];
				self.model.pagination.totalPages=0;
				self.model.selectAllRows = false;
				self.model.search = '';
				self.model.SelectedItemsIds = [];
				self.model.action = 'Open';
				var popup = angular.element('#addNewItemWasteStore');
				popup.modal('show');
			}
		}

		var getPreviousPrescription = function() {
			if (self.model.fromstore.id == self.model.store.id) {
        		growl.error("Please Select Different Stores");
      		}
      		else {
				self.model.action = 'Open';
				var popup = angular.element('#previousPrescription');
				popup.modal('show');
				var URI = CONSTANTS.PHARMACY.IP.PENDING_ISSUES.Previous_Prescription;
				var data = {
					"visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
					"patientId": !!self.model.patientId ? self.model.patientId : undefined,
					"patientName": self.model.patientName,
					"uhid": self.model.uhid,
					"unitId":$scope.unitId
				}
				self.model.previousPrescriptionList = [];				
				$rootScope.startLoaderPopup();
				GenericService.serviceAction("POST", URI, data).then(
				function (response) {
					$rootScope.stopLoaderPopup();
					self.model.previousPrescriptionList = response.data;
				},
				function errorCallback(response) {
					$rootScope.stopLoaderPopup();
					growl.error('Error');
				});
			}
		}

		var getPreviousIndent = function () {
			if (self.model.fromstore.id == self.model.store.id) {
        		growl.error("Please Select Different Stores");
      		}
      		else {
				self.model.action = 'Open';
				var popup = angular.element('#previousIndent');
				popup.modal('show');
				var visitNumber = '';
				if (self.model.visitType) {
					if (self.model.visitType.id == 1) {
						visitNumber = self.model.visitNumber.visitNo;
					} else {
						visitNumber = self.model.visitNumberText;
					}
				}
				var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.Previous_Indent;
				var data = {
					"visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
					"patientId": !!self.model.patientId ? self.model.patientId : undefined,
					"patientName": self.model.patientName,
					"uhid": self.model.uhid,
					"ward": !!self.model.ward.id ? self.model.ward.id : undefined,
					"visitNo": visitNumber,
					"unitId":$scope.unitId
				}
				self.model.previousIndentList = [];				
				$rootScope.startLoaderPopup();
				GenericService.serviceAction("POST", URI, data).then(
				function (response) {
					$rootScope.stopLoaderPopup();
					self.model.previousIndentList = response.data;
				},
				function errorCallback(response) {
					$rootScope.stopLoaderPopup();
					growl.error('Error');
				});
			}
		}

		var GetAssetCategoryList = function () {
			var indents = self.model;
			var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

			indents.assetCategory = indents.assetCategories[0];
			indents.productCategory = indents.productCategories[0];

			indents.assetCategories.splice(1);
			indents.productCategories.splice(1);

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					indents.assetCategories = indents.assetCategories.concat(response.data);
					$rootScope.stopLoader();
				},
				function (err) {
					$rootScope.stopLoader();
				});
		}

		var GetProductCategoryList = function (id) {
			var indents = self.model;

			var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					indents.productCategories = indents.productCategories.concat(response.data);
					$rootScope.stopLoader();
				},
				function (err) {
					$rootScope.stopLoader();
				});
		}

		// $scope.validateItemName = function () {
		// 	var isValid = true;
		// 	if(!self.model.search.itemName && self.model.search.itemName.length == 3){
		// 		growl.error("Please enter 3 latters of Item Name..!");
		// 		isValid = false;
		// 	}
		// 	return isValid;
		// }

		var SearchNewItem = function () {
			if(self.model.search.itemName){
				if(self.model.search.itemName.length >= 3){
					self.model.selectAllRows = false;
					self.model.isItemChecked = false;
					var URI = CONSTANTS.GLOBAL.ITEM_PATIENT_INDENT_API_NEW;

					const data = {
						"assetTypeId": 1,
						"storeId": self.model.store.id ? self.model.store.id : undefined,
						"assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
						"productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
						"isConsignment": self.model.consignment,
						"itemCode": self.model.search.itemCode,
						"itemName": self.model.search.itemName,
						"unitId":$scope.unitId

						//"isConsumable":  true
					}
					self.model.searchNewItemList = [];
					$rootScope.startLoaderPopup();
					GenericService.serviceAction("POST", URI, data).then(
					function (response) {
						$rootScope.stopLoaderPopup();
						self.model.pagination.data=[];
						self.model.pagination.totalPages=0;
						self.model.searchNewItemList = response.data;
						if(!!response && !!response.data && response.data.length > 0){            
							PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
						}
					},
					function errorCallback(response) {
						$rootScope.stopLoaderPopup();
						growl.error('Error');
					});
				}
			}
			else if(self.model.search.itemCode){
				if(self.model.search.itemCode.length >= 3){
					self.model.selectAllRows = false;
					self.model.isItemChecked = false;
					var URI = CONSTANTS.GLOBAL.ITEM_PATIENT_INDENT_API_NEW;

					const data = {
						"assetTypeId": 1,
						"storeId": self.model.store.id ? self.model.store.id : undefined,
						"assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
						"productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
						"isConsignment": self.model.consignment,
						"itemCode": self.model.search.itemCode,
						"itemName": self.model.search.itemName,
						"unitId":$scope.unitId

						//"isConsumable":  true
					}
					self.model.searchNewItemList = [];
					$rootScope.startLoaderPopup();
					GenericService.serviceAction("POST", URI, data).then(
					function (response) {
						$rootScope.stopLoaderPopup();
						self.model.pagination.data=[];
						self.model.pagination.totalPages=0;
						self.model.searchNewItemList = response.data;
						if(!!response && !!response.data && response.data.length > 0){            
							PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
						}
					},
					function errorCallback(response) {
						$rootScope.stopLoaderPopup();
						growl.error('Error');
					});
				}
			}
			else{
				self.model.searchNewItemList = [];
				self.model.pagination.data=[];
			}
		}

		var SelectAllItems = function () {
			if (self.model.selectAllRows) {
				if(self.model.SelectedItemsIds){
					self.model.data = self.model.searchNewItemList.map(function (item) {
						item.isSelected = true;
						self.model.isItemChecked = true;
						self.model.SelectedItemsIds.push(item.itemId);
						return self.model.SelectedItemsIds;
					});
				}
				else{
					self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
						item.isSelected = true;
						self.model.isItemChecked = true;
						return item.itemId;
					});
				}
			} else {
				self.model.SelectedItemsIds = [];
				self.model.searchNewItemList.forEach(function (item) {
					item.isSelected = false;
					self.model.isItemChecked = false;
				});
			}
		}

		var isSelected = function (item) {
			return item.isSelected;
		}

		var SelectItem = function (item) {
			var itemIds = self.model.SelectedItemsIds;

			if (item.isSelected) {
				itemIds.push(item.itemId);
			} else {
				var index = itemIds.indexOf(item.itemId);
				if (index > -1) {
					itemIds.splice(index, 1);
				}
			}

			if (self.model.searchNewItemList.every(isSelected)) {
				self.model.selectAllRows = true;
			} else {
				self.model.selectAllRows = false;
			}
			self.model.searchNewItemList.forEach(function (item) {
				if (item.isSelected == true) {
				  self.model.isItemChecked = true;
				  return;
				}
			  });
		}

		var AddItems = function (flag) {
			var conversionPurchase;
		      var conversionStock;
		      var conversionDespense;
			
			if( flag == true &&  !self.model.SelectedItemsIds.length){
				growl.error('Please select items');
				return false;
			} 
			
			var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API_COSUMABLE_INDENT;

			var data = {
				//"itemIds": self.model.SelectedItemsIds,
				"storeId": self.model.fromstore.id,
				"unitId":$scope.unitId,
				"organizationId":$scope.organizationId
			};
			
			if( flag == true){
				data.itemIds = self.model.SelectedItemsIds;
			}else{
				if (self.model.fromstore.id == self.model.store.id) {
	        		growl.error("Please Select Different Stores");
	        		return false;
	      		}
				if( flag == false &&  $scope.kitId == null){
					growl.error('Please select Kit');
					return false;
				} 
				
				var found = $scope.kitMasterList.find(element => element.id == $scope.kitId);
				var itemIds = [];
				
				angular.forEach(found.items,function(value,key){
					if(value.status == true){
						itemIds.push(value.itemId);
					}
				})
				
				data.itemIds = itemIds;
			}
			
			$rootScope.startLoaderPopup();
			GenericService.serviceAction("POST", URI, data).then(
				function (response) {
					self.model.SelectedItemsIds = [];
					self.model.items = [];
					$scope.kitId = null;
					var selectedUom;
					response.data.forEach(function (item) {

						var isFound = self.model.ItemDetailList.find(function (x) {
							return x.itemId == item.itemId;
						});
						
						if (found && found.items != undefined) {
							var foundItem = found.items.find(element => element.itemId == item.itemId);
							if (foundItem) {
								item.quanity = foundItem.quantity;
								item.quantity = foundItem.quantity;
							}
						}
						
						if (!isFound) {
							if (!item.id) {
								selectedUom = item.uom.find(function (obj) {
									return obj.opUom;
								});
								if (selectedUom) {
									item["uomType"] = selectedUom.uomType;
									item["uomUnit"] = selectedUom.uomUnit;
									item["uomTypeId"] = selectedUom.uomTypeId;
									item["uomUnitId"] = selectedUom.uomUnitId;
								}
								item.uomDatalist = item.uom.find(function (uom) {
					                return item.uomTypeId == uom.uomTypeId;
					              });     
					              item.uomType = item.uomDatalist.uomDetails;
					              
					              if(item.isThreeUOMApplicable == false){
					                  item.uomListdata = item.uom;
					                  item.uom = [];
					                  angular.forEach(item.uomListdata, function(data){
					                    if(data.uomType != 'Stock'){
					                      item.uom.push(data);
					                    }
					                  });
					                } 
							}
							
							$scope.convertQtyOrdered(item.uom,item)
							
							self.model.ItemDetailList.push(item);
							if(item.quanity == undefined || item.quanity == null || item.quanity == ""){
								item.quanity = 0;
							}
							 if(item.isThreeUOMApplicable == false){
					                item.uomListdata.forEach(function (uom) {
					                  if (uom.uomTypeId == 1) {
					                    conversionPurchase = uom.conversion;
					                  }
					                  else if (uom.uomTypeId == 2) {
					                    conversionStock = uom.conversion
					                  }
					                  else {
					                    conversionDespense = uom.conversion
					                  }
					                });
					  
					                if (item.uomTypeId == 1) {
					                  item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
					                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
					                  item.receivedQtyCon = (item.quanity +"*"+ conversionPurchase);
									  item.receivedQtyCalculation = (item.quanity * conversionPurchase);
									  item.uomUnitData = item.uomListdata[2].uomUnit;
					                }
					                else if (item.uomTypeId == 2) {
					                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
					                  item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
					                }
					                else {
					                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
					                  item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
					                  item.receivedQtyCon = (item.quanity);
									  item.receivedQtyCalculation = (item.quanity);
									  item.uomUnitData = item.uomListdata[2].uomUnit;
					                }
					              }
					              else{
					                item.uom.forEach(function (uom) {
					                  if (uom.uomTypeId == 1) {
					                    conversionPurchase = uom.conversion;
					                  }
					                  else if (uom.uomTypeId == 2) {
					                    conversionStock = uom.conversion
					                  }
					                  else {
					                    conversionDespense = uom.conversion
					                  }
					                });
					  
					                if (item.uomTypeId == 1) {
					                  item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
					                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
					                  item.receivedQtyCon = (item.quanity +"*"+ conversionPurchase +"*"+ conversionStock);
									  item.receivedQtyCalculation = (item.quanity * conversionPurchase * conversionStock);
									  item.uomUnitData = item.uom[2].uomUnit;
					                }
					                else if (item.uomTypeId == 2) {
					                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
					                  item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
					                  item.receivedQtyCon = (item.quanity +"*"+ conversionStock);
									  item.receivedQtyCalculation = (item.quanity * conversionStock);
									  item.uomUnitData = item.uom[2].uomUnit;
					                }
					                else {
					                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
					                  item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
					                  item.receivedQtyCon = (item.quanity);
									  item.receivedQtyCalculation = (item.quanity);
									  item.uomUnitData = item.uom[2].uomUnit;
					                }
					              }
						}

					});

					$rootScope.stopLoaderPopup();
					self.model.searchNewItemList = [];
					var popup = angular.element('#addNewItemWasteStore');
					popup.modal('hide');  
				},
				function (err) {
					$rootScope.stopLoaderPopup();
					growl.error(err.data.message);
				});
		}

        $scope.convertQtyOrdered = function (uom,item) {
            var newUOM = [];
            if(item.isThreeUOMApplicable==false)
        	{
        	item.uomListdata.find(function (value) {
                if (!!value.uomTypeId) {
                    newUOM.push(value);
                }
            });
        	}
        else
        	{
        	uom.find(function (value) {
                if (!!value.uomTypeId) {
                    newUOM.push(value);
                }
            });
        	}
           var quanity=item.stockAvailable;
           if(item.isThreeUOMApplicable==true){
                  if (item.uomTypeId == 1) {
                      item.newStock = Math.floor(item.stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                      if(item.newStock==0 && quanity >0)
                         {
                          item.newStock=1;
                         }
                      item.receivedQtyCon=(item.quantity +"*"+ newUOM[0].conversion +"*"+ newUOM[1].conversion);
					  item.receivedQtyCalculation=(item.quantity * newUOM[0].conversion * newUOM[1].conversion);
					  item.uomUnitData = newUOM[2].uomUnit;
                  } else if (item.uomTypeId == 2) {
                      item.newStock = Math.floor(item.stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
                      if(item.newStock==0 && quanity >0)
                         {
                          item.newStock=1;
                         }
                      item.receivedQtyCon= (item.quantity +"*"+ newUOM[1].conversion);
					  item.receivedQtyCalculation= (item.quantity * newUOM[1].conversion);
					  item.uomUnitData = newUOM[2].uomUnit;
                  } else if (item.uomTypeId == 3) {
                      item.newStock = Math.floor(item.stockAvailable / (newUOM[2].conversion));
                      item.receivedQtyCon= (item.quantity);
					  item.receivedQtyCalculation= (item.quantity);
					  item.uomUnitData = newUOM[2].uomUnit;
                  }
           }
           else
        	   {
        	   if (item.uomTypeId == 1) {
                   item.newStock = Math.floor(item.stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                   if(item.newStock==0 && quanity >0)
                      {
                       item.newStock=1;
                      }
                   item.receivedQtyCon=(item.quantity +"*"+ newUOM[0].conversion +"*"+ newUOM[1].conversion);
				   item.receivedQtyCalculation=(item.quantity * newUOM[0].conversion * newUOM[1].conversion);
				   item.uomUnitData = newUOM[2].uomUnit;
               } else if (item.uomTypeId == 2) {
                   item.newStock = Math.floor(item.stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
                   if(item.newStock==0 && quanity >0)
                      {
                       item.newStock=1;
                      }
                   item.receivedQtyCon= (item.quantity +"*"+ newUOM[1].conversion);
				   item.receivedQtyCalculation= (item.quantity * newUOM[1].conversion);
				   item.uomUnitData = newUOM[2].uomUnit;
               } else if (item.uomTypeId == 3) {
                   item.newStock = Math.floor(item.stockAvailable / (newUOM[2].conversion));
                   item.receivedQtyCon= (item.quantity);
					item.receivedQtyCalculation= (item.quantity);
					item.uomUnitData = newUOM[2].uomUnit;
               }
        	   }
                  item.stockAvailable=item.newStock;
           
       };
		
		var RemoveRowData = function (item) {
			var newArray = [];
	  
			self.model.ItemDetailList.map(function (obj) {
			  if (obj !== item) {
				newArray.push(obj);
			  }
			});
			self.model.ItemDetailList = newArray;
			var popup = angular.element('#itemRemovePopup');
			popup.modal('hide');
		  }
		  
		var ValidateQuantity = function (item) {
			var isValid = true;
			item['isQuantityValid'] = false;

			if (!item.quantity) {
				item['isQuantityValid'] = true;
				isValid = false;
				growl.error('Please enter quantity')
			}

			if (item.quantity == 0) {
				item['isQuantityValid'] = true;
				isValid = false;
				growl.error('Enter quantity greater than zero')
			}
			return isValid;
		}

		var Validate = function () {
			var isValid = true;

			self.model.isPatientNameValid = false;
			self.model.isVisitTypeValid = false;
			self.model.isVisitNoValid = false;

			angular.forEach(self.model.ItemDetailList, function(val){
				if(!val.quantity){
					val['isQuantityValid'] = true;
					isValid = false;
					growl.error('Please enter quantity')
				}
			});

			if (!self.model.patientName) {
				self.model.isPatientNameValid = true;
				isValid = false;
				growl.error('Enter patient name')
			}
			if (!self.model.visitType.id) {
				self.model.isVisitTypeValid = true;
				isValid = false;
				growl.error('Select visit type')
			}
			if (self.model.visitType.id) {
				if (self.model.visitType.id == 1) {
					if (!self.model.visitNumber.visitNo) {
						self.model.isVisitNoValid = true;
						isValid = false;
						growl.error('Select visit no')
					}
				} else {
					if (!self.model.visitNumberText) {
						self.model.isVisitNoValid = true;
						isValid = false;
						growl.error('Enter visit no')
					}
				}
			}

			if (self.model.fromstore.id == self.model.store.id) {
				growl.warning("Please Select Different Stores");
				isValid = false;
			}

			var ItemDetailList = self.model.ItemDetailList;
			if (!ItemDetailList || ItemDetailList.length <= 0) {
				isValid = false;
				growl.error("Please add some items in the bill");
			}
			return isValid;
		}
		var ConfirmRemove = function (item) {
			var popup = angular.element('#itemRemovePopup');
			popup.modal('show');
			self.model.itemForRemove = item;
		  }
	  
		var SetStatus = function (x) {
			var status = self.model.Status.find(function (obj) {
				return obj.status == x;
			});
			return status.id;
		}

		var SaveNewPatientIndentRequest = function () {
			var ItemDetailList = self.model.ItemDetailList;
			// if (Validate()) {
			var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.SAVE_API;
			var visitNumber = '';
			if (self.model.visitType) {
				if (self.model.visitType.id == 1) {
					visitNumber = self.model.visitNumber.visitNo;
				} else {
					visitNumber = self.model.visitNumberText;
				}
			}
			var data = {

				"patientId": !!self.model.patientId ? self.model.patientId : undefined,
				"visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
				"doctorId": !!self.model.doctorId ? self.model.doctorId : undefined,
				"admissionId": !!self.model.admissionId ? self.model.admissionId : undefined,
				"encounterId": !!self.model.encounterId ? self.model.encounterId : undefined,
				"ward": !!self.model.ward.id ? self.model.ward.id : undefined,
				"bed": !!self.model.bedId ? self.model.bedId : undefined,
				// "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
				"toStoreId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
				"storeId": !!self.model.fromstore && !!self.model.fromstore.id ? self.model.fromstore.id : undefined,
				"consignment": !!self.model.consignment ? self.model.consignment : undefined,
				"remark": self.model.remark,
				"statusId": SetStatus('New'),
				"approvalStatusId": SetStatus('New'),
				"items": [],
				"unitId":$scope.unitId,
	            "organizationId":$scope.organizationId,
	            "userId":$scope.userId
	            // "cookieStoreId":$scope.storeId
			}

			angular.forEach(ItemDetailList, function (item, key) {
				var item = {
					"itemId": item.itemId,
					"uomTypeId": item.uomTypeId,
					"uomUnitId": item.uomUnitId,
					"indentQuantity": item.quantity,
					"statusId": SetStatus('New')
				}
				 var poUom = [];
		          angular.forEach(item.poUom, function (puom) {
		            if (!!puom.uomTypeId) {
		              poUom.push({
		                "conversion": puom.conversion,
		                "id": puom.id,
		                "ipUom": puom.ipUom,
		                "opUom": puom.opUom,
		                "storeUom": puom.storeUom,
		                "uomTypeId": puom.uomTypeId,
		                "uomUnitId": puom.uomUnitId.uomUnitId
		              });
		            }
		          });
				data.items.push(item);
			});

			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(
				function (response) {
					NavigteToPatientIndent();
					$rootScope.stopLoader();
					growl.success('Consumable indent successfully saved');
				},
				function (err) {
					$rootScope.stopLoader();
					growl.error('Error while saving consumable indent ');
				});
			// }
		}

		var UpdatePatientIndent = function () {

			var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.UPDATE_API + self.model.indentId;
			var data = {
				"consignment": self.model.consignment,
				"remark": self.model.remark,
				"items": [],
				"statusId": SetStatus('New'),
				"approvalStatusId": SetStatus('New'),
				"unitId":$scope.unitId,
	            "organizationId":$scope.organizationId,
	            "userId":$scope.userId,
	            "storeId":$scope.storeId
			};

			angular.forEach(self.model.ItemDetailList, function (item, key) {
				var indentItem = {
					"id": item.id,
					"itemId": item.itemId,
					"uomTypeId": item.uomTypeId,
					"uomUnitId": item.uomUnitId,
					"indentQuantity": parseInt(item.quantity),
					"statusId": SetStatus('New')
				}

				data.items.push(indentItem);
			});

			$rootScope.startLoader();
			GenericService.serviceAction("PATCH", URI, data).then(
				function (response) {
					NavigteToPatientIndent();
					$rootScope.stopLoader();
					growl.success('Consumable indent successfully updated');
				},
				function (err) {
					$rootScope.stopLoader();
					growl.error('Error while updating consumable indent ');
				});
		}

		var SaveConsumableIndent = function () {
			if($scope.isFinalBill=='Y')
			{
			 growl.warning("The final bill has been generated for the selected patient; therefore, creating a patient indent is not possible.");
			 $rootScope.stopLoader();
			 return false;
			}
			if (Validate()) {
				if (!self.model.indentId) {
					SaveNewPatientIndentRequest();
				} else {
					UpdatePatientIndent();
				}
			}
		}

		var NavigteToPatientIndent = function () {
			var flag =true;
			$state.go('patientIndent',
			{
			  obj : flag,
			});
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

			$scope.calculateReceivedQty = function(item){
				if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
					item.quantity = 0;
				}
				var uomType = item.uomType.split(" - ")[0];
				if(item.isThreeUOMApplicable == false){
					if(uomType == 'P'){
						item.receivedQtyCon = (item.quantity +"*"+ item.uomListdata[0].conversion);
						item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
						item.uomUnitData = item.uomListdata[2].uomUnit;
					}
					else if(uomType == 'S'){
						
					}
					else{
						item.receivedQtyCon = (item.quantity);
						item.receivedQtyCalculation = (item.quantity);
						item.uomUnitData = item.uomListdata[2].uomUnit;
					}
				}
				else{
					if(uomType == 'P'){
						item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
						item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
						item.uomUnitData = item.uom[2].uomUnit;
					}
					else if(uomType == 'S'){
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
		$stateProvider.state("consumableIndent", {
			url: "/consumableIndent",
			templateUrl: 'views/PharmacyInventory/InPatient/consuable-indent-add-new.html',
			controller: "ConsumableIndent.Controller",
			controllerAs: "vm",
			title: 'Consumable Indent'
		});
	}

	angular
		.module("myApp")
		.config(config)
		.controller("ConsumableIndent.Controller", consumableIndentController);
})();