(function () {
    "use strict";
    var CACHE = {};

    function addNewReturnBillableConsumptionController($scope, $state, $rootScope, $http, $filter, $cookies, CONSTANTS, StatusService, BillingGenericService, GenericService, growl, commonDetailService) {
        var self = this;
        var mainStoreList = [];
        var visitNumber = '';
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
            $scope.nurseRole = cookieObjectAppConfig.nurseRole;	
			$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
            $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
            var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
		    $scope.nursingStationsWardsListId = nursingStationsWardsList;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                fromDateOptions: {
                    showWeeks: false,
                },
                toDateOptions: {
                    showWeeks: false,
                },
                storeList: [{
                    "store": "All"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                wardList: [{
                    "wardName": "All"
                }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                visitTypeList: [{
                    "type": "Select Visit Type"
                }],
                SelectedItemsIds: [],
                selectAllRows: false,
                SelectedItemIds: [],
                ItemDetailList: [],
                // indentId: null,
                consignment: false,
                itemSearch: {
                    itemCode: undefined,
                    itemName: undefined,
                    batchNo: undefined
                }
            };

            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.GetItemList = GetItemList;
            self.model.SearchNewItem = SearchNewItem;
            self.model.NewItemList = [];
            // self.model.SelectItem = SelectItem;
            // self.model.SelectAllItems = SelectAllItems;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.RemoveRowData = RemoveRowData;
            self.model.Validate = Validate;
            self.model.EnableButton = EnableButton;
            self.model.SelectBill = SelectBill;
            self.model.SelectAllBill = SelectAllBill;
            self.model.SaveNewbillableConsumption = SaveNewbillableConsumption;
            self.model.NavigteToBillableConsumption = NavigteToBillableConsumption;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.PopupAction = PopupAction;

            $scope.patientSearchObj.uhidNumber = '';
			$scope.patientSearchObj.patientName = '';
			$scope.patientSearchObj.genderId = '';
			$scope.birthDate = '';
			$scope.patientSearchObj.identificationTypeId = '';
			$scope.patientSearchObj.identificationNumber = '';

            PopulateDropdown();
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

            var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeList[0];
            self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                self.model.storeList = self.model.storeList.concat(response.data.store);
                self.model.wardList = self.model.wardList.concat(response.data.ward);
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

        var SearchpatientNames = function (search) {
            // if($scope.validatesearch()){
                if (search.length < 3) return;

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3) && (self.model.visitType.id)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API +$scope.unitId + "/" + search  + "/" +self.model.visitType.id;

                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
                                $rootScope.stopLoader();
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
			
			 if(item.isFinalBill != null && item.isFinalBill == "Y"){
			    	radio.checked = false;
			    	
			    	 growl.warning("The final bill has been generated for this patient.");
			    	
			    	return false;
			    }
			
            if (!!item.patientId) {
                var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_SEARCH_API +'/' + $scope.organizationId + '/'+$scope.unitId;

                var data = {
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : 0,
                    "patientId": !!item.patientId ? item.patientId : undefined,
                    // "uhid": !!item.uhid ? item.uhid : undefined,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data)
                    .then(function (response) {

                        self.model.patientInfo = response.data,
                            self.model.patientName = response.data.patientName,
                            self.model.patientId = response.data.patientId,
                            self.model.uhid = response.data.uhid,
                            self.model.bedNo = response.data.bedNo,
                            self.model.store = response.data.store,
                            self.model.bedId = response.data.bedId
                        // self.model.encounterId = response.data.encounterId,
                        // self.model.admissionId = response.data.admissionId
                        if (!!response.data.visitDetails.length) {
                            if (self.model.visitType.id == 1) {
//                                self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
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
                        growl.error(err.data.summary);
                        $rootScope.stopLoader();
                    });
            }
            self.model.ItemDetailList = [];
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

        var ChangeVisitType = function () {
            self.model.patientName = null;
            self.model.uhid = null;
            self.model.patientNames = [];
            ClearPatientInfo();
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

        var ValidateToGetItems = function () {
            var isValid = true;

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
            if (!self.model.store.id) {
                self.model.isStoreValid = true;
                isValid = false;
                growl.error('Select store')
            }
            if (self.model.visitType) {
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
                        growl.error('Select visit no')
                    }
                }
            }
            return isValid;
        }

        var GetItemList = function () {
        	$scope.flag=true;
            if (ValidateToGetItems()) {
                self.model.NewItemList = [];
                self.model.selectAllRows = false,
                    self.model.itemSearch.itemCode = undefined;
                self.model.itemSearch.itemName = undefined;
                self.model.itemSearch.batchNo = undefined;
                self.model.consumptionNo = undefined;
                $scope.search = "";
                self.model.action = 'Open';
                var popup = angular.element('#getIssueModal');
                popup.modal('show');
            }
        }

        var SearchNewItem = function () {

            var URI = CONSTANTS.PHARMACY.NURSING.RETURN_BILLABLE_CONSUMPTION.SEARCH_NEW_API;

            if (self.model.visitType) {
                if (self.model.visitType.id == 1) {
                    visitNumber = self.model.visitNumber.visitNo;
                } else {
                    visitNumber = self.model.visitNumberText;
                }
            }
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
              //  "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                "consumptionNo": !!self.model.consumptionNo ? self.model.consumptionNo : undefined,
                "unitId":$scope.unitId
            }
            self.model.NewItemList = [];
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                 
                    self.model.NewItemList = response.data;
                    self.model.isChecked = false;
                    self.model.billId = null;
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectAllBill = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isChecked = true;
                    return item.id;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.NewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isChecked = false;
                });
            }
        }

        var SelectBill = function (item) {
            var ids = self.model.SelectedItemsIds;
            if (item.isSelected) {
                ids.push(item.id);
            } else {
                var index = ids.indexOf(item.id);
                if (index > -1) {
                    ids.splice(index, 1);
                }
            }

            if (self.model.NewItemList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }

            self.model.isChecked = false;
            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isChecked = true;
                    return;
                }
            });
        }
        $scope.flag=true;
        var getAllItemDetails = function () {
            var conversionPurchase;
            var conversionStock;
            var conversionDespense;
            if(self.model.SelectedItemsIds.length == 0){
                growl.error('Please select items to add');
            } else {
            	if($scope.flag==true)
            		{
            		$scope.flag=false;
                var URI = CONSTANTS.PHARMACY.NURSING.RETURN_BILLABLE_CONSUMPTION. ADD_MULTIPLE_ITEMS;
                var data = {
                    "ids": self.model.SelectedItemsIds
                }
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.billItemDetailList = response.data;
                    response.data.forEach(function (item1) {
                        var isFound = self.model.billItemDetailList.find(function (x) {
                            return x.id == item1.id;
                        });
                        if (isFound) {
                            isFound.items.forEach(function(billItems){
                                /*start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
                                billItems.availableReturnQuantity = billItems.consumedQuantity-billItems.totalConsumedQuantity-billItems.returnQuantity
                                /*end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
                                
                                if(billItems.availableReturnQuantity != 0){
                                    self.model.ItemDetailList.push(billItems);
                                }
                                angular.forEach(self.model.ItemDetailList, function (item){
                              		 item.uomDatalist = item.uomList.find(function (uom) {
                                           return item.uomTypeId == uom.uomTypeId;
                                         });     
                                         item.uom = item.uomDatalist.uomDetails;
                                         if(item.billableConsumptionId == undefined){
                                        	 item.billableConsumptionId= item1.id;
                                         }
                                        
                                        item.uomList.forEach(function (uom) {
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
                                        if(item.quantity == undefined || item.quantity == null || item.quanity == ""){
                                            item.quantity = 0;
                                        }
                                         
                                         if(item.isThreeUOMApplicable == false){
                                             if (item.uomTypeId == 1) {
                                                item.receivedQtyCon = (item.quantity +"*"+ conversionPurchase);
                                                item.receivedQtyCalculation = (item.quantity * conversionPurchase);
                                                item.uomUnitData = item.uomList[2].uomUnit;
                                            }else{
                                                item.receivedQtyCon = (item.quantity);
                                                item.receivedQtyCalculation = (item.quantity);
                                                item.uomUnitData = item.uomList[2].uomUnit;
                                             }
                                         }
                                         else{
                                             if (item.uomTypeId == 1) {
                                                 item.receivedQtyCon = (item.quantity +"*"+ conversionPurchase +"*"+ conversionStock);
                                                 item.receivedQtyCalculation = (item.quantity * conversionPurchase * conversionStock);
                                                 item.uomUnitData = item.uomList[2].uomUnit;
                                             }else if(item.uomTypeId == 2){
                                                 item.receivedQtyCon = (item.quantity +"*"+ conversionStock);
                                                 item.receivedQtyCalculation = (item.quantity * conversionStock);
                                                 item.uomUnitData = item.uomList[2].uomUnit;
                                             }else{
                                                 item.receivedQtyCon = (item.quantity);
                                                 item.receivedQtyCalculation = (item.quantity);
                                                 item.uomUnitData = item.uomList[2].uomUnit;
                                             }
                                         }
                              	});
                            });
                        }
                    });
                    $rootScope.stopLoader();
                    self.model.SelectedItemsIds = [];
                    // self.model.items = [];
                    var popup = angular.element('#getIssueModal');
                    popup.modal('hide');
                },
                function errorCallback(response) {
                	$scope.flag=true;
                    $rootScope.stopLoader();
                    self.model.SelectedItemsIds = [];
                    growl.error('Error');
                });
            }
        }
        }

        var PopupAction = function (action, modal) {
            var popup = angular.element('#' + modal);
            self.model.action = action;
            if (action == "Open") {
                popup.modal('show');
            } else {
                popup.modal('hide');
            }
        }

        var ConfirmRemove = function (index) {
            self.model.PopupAction('Open', 'confirmRemove');
            self.model.itemForRemove = index;
        }


        var RemoveRowData = function (index) {
            var ItemDetailList = self.model.ItemDetailList;
            self.model.ItemDetailList = ItemDetailList.slice(0, index).concat(ItemDetailList.slice(index + 1))
            self.model.PopupAction('Close', 'confirmRemove');
        }

        var ValidateQuantity = function (item) {
            var isValid = true;
            item['isQuantityValid'] = false;

            if (!item.quantity) {
                item['isQuantityValid'] = true;
                isValid = false;
                growl.error('Please enter quantity')
            }

            if ((parseInt(item.returnQuantity)) == item.consumedQuantity) {
                item['isQuantityValid'] = true;
                isValid = false;
                growl.error('All consumed quantity has been returned');
            }
//            else if (((parseInt(item.quantity)) + (parseInt(item.returnQuantity))) > item.consumedQuantity) {
//                item['isQuantityValid'] = true;
//                isValid = false;
//                growl.error('Entered quantity is exceeding the max available quantity');
//            }

            if (item.quantity <= 0) {
                item['isQuantityValid'] = true;
                isValid = false;
                growl.error('Enter quantity greater than zero')
            }
            
            /*start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
            let issuedQuantity = item.consumedQuantity;
            let totalConsumedQuantity=item.totalConsumedQuantity;
            let totalReturnQuantity=item.returnQuantity;
            let consumptionQuantity= parseInt(item.consumptionQuantity) ?  parseInt(item.consumptionQuantity):0 ;            
            let returningQuantity= parseInt(item.quantity) ?  parseInt(item.quantity):0 ;            
            //item.availableQuantity = item.consumedQuantity-item.totalConsumedQuantity-item.returnQuantity
            item.availableReturnQuantity =  issuedQuantity-totalConsumedQuantity-totalReturnQuantity-consumptionQuantity;
            
            let availableConsumptionQuantity= issuedQuantity-totalConsumedQuantity-totalReturnQuantity-returningQuantity;
            let availableQuantity= issuedQuantity-totalConsumedQuantity-totalReturnQuantity-consumptionQuantity-returningQuantity;            	
                                
            if (parseInt(item.quantity) > parseInt(item.availableReturnQuantity)) {
                item['isQuantityValid'] = true;
                isValid = false;
                growl.error('Entered Return quantity is exceeding the Available Return Quantity');
            }
                       
            if (parseInt(item.consumptionQuantity) < 0 || parseInt(item.consumptionQuantity) > availableConsumptionQuantity) {
            	item['isQuantityValid'] = true;
            	isValid = false;
            	growl.error('Please enter valid Consumption quantity');
            }
//            if(availableQuantity < 0) {
//            	item['isQuantityValid'] = true;
//            	isValid = false;
//            	growl.error('Please enter valid Quantities');
//            }
            /*end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
            
            return isValid;
        }

        var Validate = function () {
            var isValid = true;

            self.model.isPatientNameValid = false;
            self.model.isVisitTypeValid = false;
            self.model.isVisitNoValid = false;

            if (!self.model.patientName) {
                self.model.isPatientNameValid = true;
                isValid = false;
                growl.error('Enter patient name')
            }
            if (!self.model.store.id) {
                self.model.isStoreValid = true;
                isValid = false;
                growl.error('Select store')
            }
            if (!self.model.visitType.id) {
                self.model.isVisitTypeValid = true;
                isValid = false;
                growl.error('Select visit type')
            }
            if (self.model.visitType) {
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
                        growl.error('Select visit no')
                    }
                }
            }

            var ItemDetailList = self.model.ItemDetailList;
            if (!ItemDetailList || ItemDetailList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                ItemDetailList.forEach(function (item) {

                    self.model.isRemarkValid = false;
                    item['isQuantityValid'] = false;

                    if (!item.quantity) {
                        item['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Please enter quantity')
                    }
//                    if (((parseInt(item.quantity)) + (parseInt(item.returnQuantity))) > item.consumedQuantity) {
//                        item['isQuantityValid'] = true;
//                        isValid = false;
//                        growl.error('Entered quantity is exceeding the max available quantity');
//                    }
                    if (item.quantity == 0) {
                        item['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Enter quantity greater than zero')
                    }
                    if (!ValidateQuantity(item)) {
                        isValid = false;
                    }
                });
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }
        var EnableButton = function () {
            var isValid = true;

            self.model.isPatientNameValid = false;
            self.model.isVisitTypeValid = false;
            self.model.isVisitNoValid = false;

            if (!self.model.patientName) {
                self.model.isPatientNameValid = true;
                isValid = false;
                //growl.error('Enter patient name')
            }
            if (!self.model.store.id) {
                self.model.isStoreValid = true;
                isValid = false;
               // growl.error('Select store')
            }
            if (!self.model.visitType.id) {
                self.model.isVisitTypeValid = true;
                isValid = false;
                //growl.error('Select visit type')
            }
            if (self.model.visitType) {
                if (self.model.visitType.id == 1) {
                    if (!self.model.visitNumber.visitNo) {
                        self.model.isVisitNoValid = true;
                        isValid = false;
                        //growl.error('Select visit no')
                    }
                } else {
                    if (!self.model.visitNumberText) {
                        self.model.isVisitNoValid = true;
                        isValid = false;
                       // growl.error('Select visit no')
                    }
                }
            }
            if (isValid)
                document.getElementById("btnGetIssue").disabled = false;
            else
                document.getElementById("btnGetIssue").disabled = true;
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }
        $scope.saveBtnDisabled=false;
        var SaveNewbillableConsumption = function () {
        	$scope.saveBtnDisabled=true;
            var ItemDetailList = self.model.ItemDetailList;
            if (Validate()) {
                var URI = CONSTANTS.PHARMACY.NURSING.RETURN_BILLABLE_CONSUMPTION.SAVE_API;

                angular.forEach(ItemDetailList, function(billitem, key){
                    self.model.billableConsumptionId = billitem.id;
                })

                var data = {
                    "billableConsumptionId": !!self.model.billableConsumptionId ? self.model.billableConsumptionId : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "admissionId": !!self.model.admissionId ? self.model.admissionId : undefined,
                    "storeId":!!self.model.store ? self.model.store.id : undefined,
                    "encounterId": !!self.model.encounterId ? self.model.encounterId : undefined,
                    "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                    "bedId": !!self.model.bedId ? self.model.bedId : undefined,
                    "statusId": SetStatus('Completed'),
                    "approvedStatusId": SetStatus('Completed'),
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "cookieStoreId":$scope.storeId
                }

                angular.forEach(ItemDetailList, function (item, key) {
                    var item = {
                        "billbleConsumptionDetailId": item.id,
                        "batchId":item.batchId,
                        "statusId": SetStatus('Completed'),
                        "uomTypeId":item.uomTypeId,
                        "returnQuantity": parseInt(item.quantity),
                        "consumedQuantity" : parseInt(item.consumptionQuantity),
                        "billableConsumptionId":item.billableConsumptionId,
                    }
                    data.items.push(item);
                });

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        NavigteToBillableConsumption();
                        $rootScope.stopLoader();
                        growl.success('New return billable consumption successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving new billable consumption ');
                    });
            }
        }
        var NavigteToBillableConsumption = function () {
            $state.go('returnBillableConsumption');
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
			self.model.bedNo = "";
            self.model.store = "";
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
			self.model.ItemDetailList = [];
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
        	if(item.quantity==undefined || item.quantity==null)
        		{
        		item.quantity=0;
        		}
            if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.quantity +"*"+ item.uomList[0].conversion);
                    item.receivedQtyCalculation = (item.quantity * item.uomList[0].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                    item.receivedQtyCon = (item.quantity);
                    item.receivedQtyCalculation = (item.quantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
            else{
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.quantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.quantity * item.uomList[0].conversion * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    item.receivedQtyCon = (item.quantity +"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.quantity * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                    item.receivedQtyCon = (item.quantity);
                    item.receivedQtyCalculation = (item.quantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
        }

        initializeController();
    }


    function config($stateProvider) {
        $stateProvider.state("addNewReturnBillableConsumption", {
            url: "/addNewReturnBillableConsumption",
            templateUrl: 'views/PharmacyInventory/InPatient/add-new-return-billable-consumption.html',
            controller: "AddNewReturnBillableConsumption.Controller",
            controllerAs: "vm",
            title: 'Return Billable Consumption'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewReturnBillableConsumption.Controller", addNewReturnBillableConsumptionController);
})();