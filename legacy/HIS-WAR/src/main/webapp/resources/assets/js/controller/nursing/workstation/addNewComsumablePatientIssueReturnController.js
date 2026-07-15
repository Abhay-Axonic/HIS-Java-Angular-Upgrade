(function () {
    "use strict";

    function addNewComsumablePatientIssueReturnController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, BillingGenericService, $filter, $state, growl, CurrencyService, detailService, StatusService) {
        var self = this;
        var newIssueIds = [];

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
                SetFirstTabLevel: 1,
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                returnReasonList: [{
                    "reason": "Select Return Reason"
                }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                storeList: [{
                    "store": "All"
                }],
                storeList: [{
                    "store": "Select Store"
                }],
                visitTypeList: [{
                    "type": "Select Visit Type"
                }],

                SelectedIssueIds: [],
                selectAllRows: false,
                issueDetailList: [],
                SelectedItemIds: [],
                SelectedNewIssueIds: [],
                requestType : 1,
                selectedBatchIds : [],

            };


            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.ClearSearchFields = ClearSearchFields;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.ChangeRequestType = ChangeRequestType;
            self.model.GetIssueItems = GetIssueItems;
            self.model.SelectIssue = SelectIssue;
            self.model.SelectAllIssues = SelectAllIssues;
            self.model.AddItems = AddItems;
            self.model.RemoveRowData = RemoveRowData;
            self.model.SaveIssueReturn = SaveIssueReturn;
            self.model.SearchItemNames = SearchItemNames;
            self.model.AddItemDetails = AddItemDetails;
            self.model.GetItems = GetItems;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateReturnReason = ValidateReturnReason;

            var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
            self.model.issueDetailList.store = cookieObjectStore.storeName;

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

            var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_DROPDOWN_API+ '/' + $scope.unitId;

            self.model.visitNumber = self.model.visitDetails[0];
            self.model.visitType = self.model.visitTypeList[0];
            self.model.store = self.model.storeList[0];
            $scope.visitList = [];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    angular.forEach(response.data.visitType, function(visit){
                        if(visit.type != 'Out Patient'){
                            $scope.visitList.push(visit);
                        }
                    });
                    // self.model.visitTypeList = self.model.visitTypeList.concat($scope.visitList);
                    self.model.visitTypeList = $scope.visitList;
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.visitType = self.model.visitTypeList.find(function (obj) {
                        return obj.id == 2;
                    });

                    $rootScope.stopLoader();
                });
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

                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" +self.model.visitType.id;

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

        var ClearSearchFields = function () {
            self.model.patientName = null,
                self.model.patientId = null,
                self.model.uhid = null,
                self.model.bedNo = null,
                self.model.bedId = null,
                self.model.encounterId = null,
                self.model.admissionId = null,
                self.model.visitNumber = self.model.visitDetails[0];
            self.model.visitNumberText = null;
        }

        var SearchUhid = function (search) {

            if (search.length < 3) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
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
        	self.model.issueDetailList.items=[];
			$scope.dummyList.length=0;
            var radio = document.getElementById('patientSelectRadioCommon_' + index);
			radio.checked = true;

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
                            self.model.bedId = response.data.bedId
                        // self.model.encounterId = response.data.encounterId,
                        // self.model.admissionId = response.data.admissionId
                        if (!!response.data.visitDetails.length) {
                            if (self.model.visitType.id == 1) {
                                self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
                            } else if (self.model.visitType.id != 1) {
                                self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                                self.model.encounterId = response.data.visitDetails[0].encounterId,
                                    self.model.admissionId = response.data.visitDetails[0].admissionId,
                                    self.model.doctorId = response.data.visitDetails[0].doctorId
                            }
                        }
                        Validate();
                        $rootScope.stopLoader();
                    }, function (err) {
                        growl.error(err.data.summary);
                        $rootScope.stopLoader();
                    });
            }
        }

        var ClearPatientInfo = function (){
            self.model.patientData = null;
            self.model.patientId = null;
            self.model.visitNumberText = null;
            self.model.encounterId = null;
            self.model.admissionId = null;
            self.model.doctorId = null;
            self.model.visitNumber = self.model.visitDetails.splice(1);
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.patientInfo = null;
        }

        var ChangeVisitType = function(){
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

        var ChangeRequestType = function (requestType) {
        	self.model.issueDetailList.items=[];
			$scope.dummyList.length=0;
            if (Validate()) {
                ClearPopupSearchFields();
                if (requestType == 1) {
                    self.model.action = 'Open';
                    self.model.items = [];
                    self.model.SelectedIssueIds = [];
                    self.model.consumableIssueItemlist = [];
                    var popup = angular.element('#consumableGetIssueModal');
                    popup.modal('show');
                    // PopulateAssetCategoryDropdown();
                } else if (requestType == 2) {
                    self.model.action = 'Open';
                    self.model.consumableItemlist = [];
                    self.model.SelectedItemIds = [];
                    var popup = angular.element('#consumableGetItemModal');
                    popup.modal('show');
                    // PopulateIndentDropdown();
                }
            }
        }

        var ClearPopupSearchFields = function () {

            self.model.fromDate = new Date();
            self.model.toDate = new Date();
            self.model.itemName = null;
            self.model.itemCode = null;
            self.model.batchNo = null;
            self.model.store = self.model.store = self.model.storeList[0];
            self.model.issueNo = null;
            self.model.search = null;
            self.model.selectAllRows = false;
        }

        var Validate = function () {
            var isValid = true;
            self.model.isrequestTypeValid = false;
            self.model.isVisitTypeValid = false;
            self.model.isPatientNameValid = false;
            self.model.isVisitNoValid = false;

            if (!self.model.requestType) {
                isValid = false;
                self.model.isrequestTypeValid = true;
            }
            if (!self.model.visitType.id) {
                isValid = false;
                self.model.isVisitTypeValid = true;
            }

            if (!self.model.patientName) {
                isValid = false;
                self.model.isPatientNameValid = true;
            }
            if (self.model.visitType.id == 1) {
                if (self.model.visitNumber == self.model.visitDetails[0] || !self.model.visitNumber.visitNo) {
                    isValid = false;
                    self.model.isVisitNoValid = true;
                }
            }
            if (self.model.visitType.id != 1) {
                if (!self.model.visitNumberText) {
                    isValid = false;
                    self.model.isVisitNoValid = true;
                }
            }
            if (!self.model.visitType.id || !self.model.patientName) {
                isValid = false;
                growl.error('Please Select Required Field');
            }

            return isValid;
        }

        var validateStore = function () {
            var isValid = true;
            // if (!self.model.store.id) {
            //     self.model.isStoreValid = true;
            //     isValid = false;
            //     growl.error("Please select store");
            // }
            return isValid;
        }

        var GetIssueItems = function () {
            if (validateStore()) {
                var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.CONSUMABLE_ISSUE_SEARCH_API;
                var visitNumber = '';
                if (self.model.visitType.id == 1) {
                    visitNumber = self.model.visitNumber.visitNo;
                } else {
                    visitNumber = self.model.visitNumberText;
                }

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "issueNo": !!self.model.issueNo ? self.model.issueNo : undefined,
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.consumableIssueItemlist = response.data;
                        self.model.SelectedIssueIds = [];
                    })
                $rootScope.stopLoader();
            }
        }

        var SelectAllIssues = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedIssueIds = self.model.consumableIssueItemlist.map(function (issue) {
                    issue.isSelected = true;
                    return issue.id;
                });
            } else {
                self.model.SelectedIssueIds = [];
                self.model.consumableIssueItemlist.forEach(function (issue) {
                    issue.isSelected = false;
                });
            }
        }

        var isSelected = function (issue) {
            return issue.isSelected;
        }

        var SelectIssue = function (issue) {
            var issueIds = self.model.SelectedIssueIds;

            if (issue.isSelected) {
                issueIds.push(issue.id);
            } else {
                var index = issueIds.indexOf(issue.id);
                if (index > -1) {
                    issueIds.splice(index, 1);
                }
            }

            if (self.model.consumableIssueItemlist.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var SearchItemNames = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;

                var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.ITEM_AUTOPOPULATE_API + search;

                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        $rootScope.stopLoader();
                        if (!!response.data && response.data.length > 0) {
                            self.model.itemNames = response.data;
                            self.model.itemId = response.data.itemId;
                            self.model.itemCode = response.data.itemCode;
                            self.model.batchNo = response.data.batchNo;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.itemNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return ItemNameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return ItemNameNotFound(search);
                    });
            } else {
                if (!!self.model.itemNames && self.model.itemNames.length > 0 &&
                    self.model.itemNames[0].itemFound != undefined && !self.model.itemNames[0].itemFound) {
                    return ItemNameNotFound(search);
                } else {
                    return $filter('filter')(self.model.itemNames, {
                        $: search
                    });
                }
            }
        }

        var ItemNameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "itemName": "Item Not found with name " + search
            };
            self.model.itemNames = [item];
            return self.model.itemNames;
        }

        var AddItemDetails = function () {
            self.model.itemNames = self.model.itemNames;
            self.model.itemId = self.model.itemId;
            self.model.itemCode = self.model.itemCode;
            self.model.batchNo = self.model.batchNo;
        }

        var GetItems = function () {
            if (validateStore()) {
                var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.CONSUMABLE_ITEM_SEARCH_API;
                var visitNumber = '';
                if (self.model.visitType.id == 1) {
                    visitNumber = self.model.visitNumber.visitNo;
                } else {
                    visitNumber = self.model.visitNumberText;
                }

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "issueNo": !!self.model.issueNo ? self.model.issueNo : undefined,
                    "itemName": !!self.model.itemName ? self.model.itemName : undefined,
                    "itemCode": !!self.model.itemCode ? self.model.itemCode : undefined,
                    "batchNo": !!self.model.batchNo ? self.model.batchNo : undefined,
                    "unitId":$scope.unitId    		

                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.consumableItemlist = response.data;
                        self.model.SelectedItemIds = [];
                    })
                $rootScope.stopLoader();
            }
        }

        var SelectAllItems = function () {
           
            if (self.model.selectAllRows) {
                self.model.SelectedItemIds = self.model.consumableItemlist.map(function (item) {
                    item.isSelected = true;
                    return item.itemId;
                });
                self.model.SelectedNewIssueIds = self.model.consumableItemlist.map(function (item) {
                    item.isSelected = true;
                    return item.id;

                });
                self.model.selectedBatchIds=self.model.consumableItemlist
                .map(function(item) {
                    item.isSelected = true;
                    return item.batchMapperId;

                });
            } else {
                self.model.SelectedItemIds = [];
                self.model.SelectedNewIssueIds = [];
                self.model.selectedBatchIds=[];
                self.model.consumableItemlist.forEach(function (item) {
                    item.isSelected = false;
                });
            }
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectItem = function (item) {
            var itemIds = self.model.SelectedItemIds;
            newIssueIds = self.model.SelectedNewIssueIds;

            if (item.isSelected) {
                itemIds.push(item.itemId);
                newIssueIds.push(item.id);
                self.model.selectedBatchIds.push(item.batchMapperId);
            } else {
                var index = itemIds.indexOf(
                    item.itemId
                );
                newIssueIds.indexOf(
                    item.id
                );
                if (index > -1) {
                    itemIds.splice(index, 1);
                    self.model.selectedBatchIds.splice(index, 1);
                }
            }

            if (self.model.consumableItemlist.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var ValidatePopupItems = function () {
            var isValid = true;

            if (self.model.requestType == 1) {
                if (self.model.SelectedIssueIds.length <= 0) {
                    isValid = false;
                    growl.error('Please select issues');
                }
            } else if (self.model.requestType == 2) {
                if (self.model.SelectedItemIds.length <= 0) {
                    isValid = false;
                    growl.error('Please select items');
                }
            }

            return isValid;
        }

        $scope.dummyList = [];
        var AddItems = function () {
            if (ValidatePopupItems()) {
                var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_ADD_ITEMS_API;
                self.model.reason = self.model.returnReasonList[0];

                if (self.model.requestType == 1) {
                    var data = {
                        "issueIds": self.model.SelectedIssueIds ? self.model.SelectedIssueIds : undefined,
                        "unitId":$scope.unitId
                    }
                } else if (self.model.requestType == 2) {
                    var data = {
                        "issueIds": self.model.SelectedNewIssueIds ? self.model.SelectedNewIssueIds : undefined,
                        "itemIds": self.model.SelectedItemIds ? self.model.SelectedItemIds : undefined,
                        "unitId":$scope.unitId,
                        "screenId": 1,
                        "batchIds":self.model.selectedBatchIds
                    }
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.returnReasonList = self.model.returnReasonList.concat(response.data.returnReason);
                        self.model.issueDetailList = response.data;
                        self.model.issueDetailList.items.forEach(function (item) {
                        	 item.uomDatalist = item.uomList.find(function (uom) {
                                 return item.uom == uom.uomType;
                               });     
                               item.uom = item.uomDatalist.uomDetails;
                               if(item.isThreeUOMApplicable == false){
                                   item.uomListdata = item.uomList;
                                   item.uomList = [];
                                   angular.forEach(item.uomListdata, function(data){
                                     if(data.uomType != 'Stock'){
                                       item.uomList.push(data);
                                     }
                                   });
                                 } 
                               CalculateStock(item, item.uomList, item.uomDatalist);
                          
                            if(item.patientReturnedQuantity == undefined){
                                item.patientReturnedQuantity = 0;
                            }
                            item.acceptQuantity = item.returnQuantity;
                            item.returnQuantity = item.acceptQuantity - (item.patientReturnedQuantity>0 ? item.patientReturnedQuantity:item.patientIssueReturnQuantity);
                            // var isFound = self.model.issueDetailList.items.find(function (x) {
                            //     return x.itemId == item.itemId;
                            // });
                            // if (!isFound) {
                            $scope.dummyList.push(item);
                            // }
                            var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
                            self.model.issueDetailList.store = cookieObjectStore.storeName;
                            self.model.issueDetailList.storeId = cookieObjectStore.storeId;
                        });

                        $scope.list = $scope.dummyList;
                        var cleaned = [];
                        $scope.list.forEach(function(itm) {
                        var unique = true;
                        cleaned.forEach(function(itm2) {
                            if (_.isEqual(itm.batchId, itm2.batchId) && _.isEqual(itm.batchMapperId, itm2.batchMapperId)) unique = false;
                        });
                        if (unique) cleaned.push(itm);
                        });
                        $scope.list = cleaned;

                      /*  if($scope.dummyList.length != $scope.list.length){
                            growl.error("Already Exits..!");
                        }*/
                        self.model.issueDetailList.items = $scope.list;
                        var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
                        self.model.issueDetailList.store = cookieObjectStore.storeName;
                        self.model.issueDetailList.storeId = cookieObjectStore.storeId;

                        self.model.SelectedIssueIds = [];
                        self.model.SelectedItemIds = [];
                        self.model.consumableIssueItemlist = [];
                        self.model.SelectedNewIssueIds = []

                        if (self.model.requestType == 1) {
                            ClosePopup ('close', 'consumableGetIssueModal');
                        } else if (self.model.requestType == 2) {
                            ClosePopup ('close', 'consumableGetItemModal');
                        }
                        
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
            }
        }
    	var CalculateStock = function (item,uomList, uomNew) {
		      if (!!uomNew.uomTypeId) {
		    	  uomNew.uomDetailsData = uomNew.uomDetails.split(' ')[0];
		        if(item.isThreeUOMApplicable == false){
		          if (uomNew.uomDetailsData == "P") {
		            item.receivedQtyCon=(item.returnQuantity +"*"+ item.uomListdata[0].conversion);
                    item.receivedQtyCalculation=(item.returnQuantity * item.uomListdata[0].conversion);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
		          } else if (uomNew.uomDetailsData == "S") {
		          } else if (uomNew.uomDetailsData == "D") {
		            item.receivedQtyCon=(item.returnQuantity);
                    item.receivedQtyCalculation=(item.returnQuantity);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
		          }
		        }
		        else{
		          if (uomNew.uomDetailsData == "P") {
		            item.receivedQtyCon=(item.returnQuantity +"*"+ uomList[0].conversion+"*"+ uomList[1].conversion);
                    item.receivedQtyCalculation=(item.returnQuantity * uomList[0].conversion * uomList[1].conversion);
                    item.uomUnitData = uomList[2].uomUnit;
		          } else if (uomNew.uomDetailsData == "S") {
		            item.receivedQtyCon=(item.returnQuantity +"*"+ uomList[1].conversion);
                    item.receivedQtyCalculation=(item.returnQuantity * uomList[1].conversion);
                    item.uomUnitData = uomList[2].uomUnit;
		          } else if (uomNew.uomDetailsData == "D") {
		            item.receivedQtyCon=(item.returnQuantity);
                    item.receivedQtyCalculation=(item.returnQuantity);
                    item.uomUnitData = uomList[2].uomUnit;
		          }
		        }
		      } 
		    }
        var RemoveRowData = function (item, index) {
          /*  var newArray = [];

            self.model.issueDetailList.items.map(function (obj) {
                if (obj.id !== item.id) {
                    newArray.push(obj);
                }
            });
            self.model.issueDetailList.items = newArray;*/
        	
        	   self.model.issueDetailList.items.splice(index, 1);
        }

        var ClosePopup = function (action, modal) {
            self.model.action = action;
            var popup = angular.element('#' + modal);
            popup.modal('hide');
        }

        var SetStatus = function (x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var SaveIssueReturn = function (itemList) {
            if (ValidateIssueList(itemList)) {

               // var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.CONSUMABLE_SAVE_API;
            	var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_SAVE_API;
                var data = {
                    "patientId": itemList.patientId,
                    "visitType": itemList.visitType,
                    "encounterId": itemList.encounterId ? itemList.encounterId : undefined,
                    "admissionId": itemList.admissionId ? itemList.admissionId : undefined,
                    "wardId": itemList.wardId,
                    "bedId": itemList.bedId,
                    "storeId": itemList.storeId,
                    "statusId": SetStatus('Pending'),
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "toStoreId":itemList.toStoreId
                }
                angular.forEach(itemList.items, function (item, key) {

                    var item = {

                        "batchMapperId": item.batchMapperId,
                        "returnQuantity": item.returnQuantity,
                        "returnReasonId": item.reason,
                        "id":item.id
                    }
                    data.items.push(item);

                });
                console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        NevigatetoPatientIssueReturn();
                        growl.success('Patient issue return Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving patient issue return');
                    });
            }
        }

        var ValidateQuantity = function (item) {
            var isValid = true;
            item['isReturnQuantityValid'] = false;

            if (!item.returnQuantity) {
                item['isReturnQuantityValid'] = true;
                isValid = false;
                growl.error('Please enter quantity')
            }
            
            if (  (parseInt(item.returnQuantity)) > (item.quantityIssued-(item.patientIssueReturnQuantity==undefined ? 0 : item.patientIssueReturnQuantity))) {
                item['isReturnQuantityValid'] = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max issued quantity');
                item.returnQuantity=0;
            }
          //  if (item.quantityIssued < (parseInt(item.returnQuantity)+item.patientReturnedQuantity)) {
            if (  (parseInt(item.returnQuantity)) > (item.quantityIssued-(item.patientReturnedQuantity==undefined ? 0 : item.patientReturnedQuantity))) {
                item['isReturnQuantityValid'] = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max issued quantity');
                item.returnQuantity=0;
            }

            if (item.returnQuantity == 0) {
                item['isReturnQuantityValid'] = true;
                isValid = false;
                growl.error('Enter quantity greater than zero')
            }
            return isValid;
        }

        var ValidateReturnReason = function (item) {
            var isValid = true;
            item['isReturnReasonValid'] = false;

            if (!item.reason) {
                item['isReturnReasonValid'] = true;
                isValid = false;
                growl.error("Please select reason");
            }
            return isValid;
        }

        var ValidateIssueList = function () {
            var isValid = true;

            if (!self.model.issueDetailList.items || self.model.issueDetailList.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                self.model.issueDetailList.items.forEach(function (item) {
                    item['isReturnQuantityValid'] = false;
                    item['isReturnReasonValid'] = false;

                    if (item.quantityIssued < (item.returnQuantity)) {
                        item['isReturnQuantityValid'] = true;
                        isValid = false;
                        growl.error("Entered quantity is exceeding the max issued quantity");
                    }

                    if (!item.returnQuantity) {
                        item['isReturnQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity");
                    }

                    if (item.returnQuantity == 0) {
                        item['isReturnQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than zero");
                    }

                    if (!item.reason) {
                        item['isReturnReasonValid'] = true;
                        isValid = false;
                        growl.error("Please select reason");
                    }

                    if(!ValidateQuantity(item)){
                        isValid = false;
                    }

                    if(!ValidateReturnReason(item)){
                        isValid = false;
                    }
                });

            }
            return isValid;
        }

        var NevigatetoPatientIssueReturn = function () {
            $state.go('patientIssueReturn');
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
                        sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
                        $scope.nursingStationId = data.nursingStationId;
                        $scope.wardListForGetBedList = response.data.object;
                        // self.model.ward.id = $scope.wardListForGetBedList;
                        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
                        $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
                        self.model.wardIdFromService = $scope.wardListForGetBedList;
                        angular.element('#nursingStationModal').modal('hide');
                    }
                });
            }
        }

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
            if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
				item.returnQuantity = 0;
			}
			if(item.isThreeUOMApplicable == false){
				if(item.uomDatalist.uomTypeId == 1){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomListdata[0].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uomListdata[0].conversion);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
				}
				else if(item.uomDatalist.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.returnQuantity);
                    item.receivedQtyCalculation = (item.returnQuantity);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
				}
			}
			else{
				if(item.uomDatalist.uomTypeId == 1){
					item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
				}
				else if(item.uomDatalist.uomTypeId == 2){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.returnQuantity);
                    item.receivedQtyCalculation = (item.returnQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
				}
			}
		}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewComsumablePatientIssueReturn", {
            url: '/addNewComsumablePatientIssueReturn',
            templateUrl: 'views/PharmacyInventory/InPatient/add-new-Comsumable-patient-issue-return.html',
            controller: "addNewComsumablePatientIssueReturn.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Return'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("addNewComsumablePatientIssueReturn.Controller", addNewComsumablePatientIssueReturnController)
})();