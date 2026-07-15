(function () {
    "use strict";

    function addNewConsignmentPatientImplantReturn($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, detailService, StatusService) {
        var self = this;
        var newIssueIds = [];

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){
        	$state.go('login');
        	return;
        	
        }
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        
        function initializeController() {
            $rootScope.loginpage = true;

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
                requestType : 1

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
            self.model.ConfirmRemove=ConfirmRemove;
            self.model.PopupAction=PopupAction;
 
            self.model.SaveIssueReturn = SaveIssueReturn;
            self.model.SearchItemNames = SearchItemNames;
            self.model.AddItemDetails = AddItemDetails;
            self.model.ClosePopup = ClosePopup;
            self.model.GetItems = GetItems;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.OpenRequestPopUp = OpenRequestPopUp;
            self.model.ConfirmRequestType = ConfirmRequestType;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateReturnReason = ValidateReturnReason;

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

          var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.storeList = self.model.storeList.concat(response.data.store);

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

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + self.model.visitType.id;

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
                            self.model.uhid = response.data.uhid;
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

        var AddPatientDetails = function (item) {

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
            // self.model.prRequestType = requestType;
            // if (!!self.model.issueDetailList.items.length) {
            //     var popup = angular.element('#confirmationPopUp');
            //     popup.modal('show');
            // } else {
            //   self.model.requestType = requestType;
            // }
        }
      
        var ConfirmRequestType = function (rt) {
            // self.model.prRequestType = rt;
            // self.model.issueDetailList.items = [];
            
            // var popup = angular.element('#confirmationPopUp');
            // popup.modal('hide');
            // console.log(self.model.requestType);
        }

        var OpenRequestPopUp = function (requestType) {
            if (Validate()) {
                ClearPopupSearchFields();
                if (requestType == 1) {
                    self.model.action = 'Open';
                    self.model.items = [];
                    self.model.medicationIssueItemlist = [];
                    self.model.SelectedIssueIds = [];
                    var popup = angular.element('#medicationGetIssueModal');
                    popup.modal('show');
                    // PopulateAssetCategoryDropdown();
                } else if (requestType == 2) {
                    self.model.action = 'Open';
                    self.model.medicationItemlist = [];
                    self.model.SelectedItemIds = [];
                    var popup = angular.element('#medicationGetItemModal');
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
                if (self.model.visitNumber == self.model.visitDetails[0] || !self.model.visitNumber) {
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
            if (!self.model.store.id) {
                self.model.isStoreValid = true;
                isValid = false;
                growl.error("Please select store");
            }
            return isValid;
        }

        var GetIssueItems = function () {
            if (validateStore()) {
                var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.AGAINST_ISSUE_SEARCH_API;
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
                    "unitId":$scope.unitId,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    
                        self.model.medicationIssueItemlist = response.data;
                        self.model.SelectedIssueIds = [];
                        self.model.selectAllRows=false;

                    })
                $rootScope.stopLoader();
            }
        }

        var SelectAllIssues = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedIssueIds = self.model.medicationIssueItemlist.map(function (issue) {
                    issue.isSelected = true;
                    return issue.id;
                });
            } else {
                self.model.SelectedIssueIds = [];
                self.model.medicationIssueItemlist.forEach(function (issue) {
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

            if (self.model.medicationIssueItemlist.every(isSelected)) {
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
              var URI= CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.AGAINST_ITEMS_SEARCH_API;
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
                    "unitId":$scope.unitId,
                    

                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                     /*    response.data=[{
                            "itemId":1,
                            "issueNo":"ISS-564"
                        }]; */
                        self.model.medicationItemlist = response.data;
                        self.model.SelectedItemIds = [];
                    })
                $rootScope.stopLoader();
            }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemIds = self.model.medicationItemlist.map(function (item) {
                    item.isSelected = true;
                    return item.itemId;
                });
                self.model.SelectedNewIssueIds = self.model.medicationItemlist.map(function (item) {
                    item.isSelected = true;
                    return item.id;

                });
            } else {
                self.model.SelectedItemIds = [];
                self.model.SelectedNewIssueIds = [];
                self.model.medicationItemlist.forEach(function (item) {
                    item.isSelected = false;
                });
            }
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectItem = function (item) {
            var itemIds = self.model.SelectedItemIds;
            var newIssueIds = self.model.SelectedNewIssueIds;

            if (item.isSelected) {
                // itemIds.push(item.id);
                itemIds.push(item.itemId);
                newIssueIds.push(item.id);
            } else {
                var index = itemIds.indexOf(
                    item.itemId
                );
                newIssueIds.indexOf(
                    item.id
                );
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.medicationItemlist.every(isSelected)) {
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

        var AddItems = function () {
            if (ValidatePopupItems()) {
                var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.ADD_NEW_API;
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
                        "unitId":$scope.unitId
                    }
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                 
                        self.model.returnReasonList = self.model.returnReasonList.concat(response.data.returnReason);
                        self.model.issueDetailList = response.data;
                        self.model.issueDetailList.items.forEach(function (item) {

                            var isFound = self.model.issueDetailList.items.find(function (x) {
                                return x.itemId == item.itemId;
                            });
                            if (!isFound) {
                                self.model.issueDetailList.items.push(item);
                            }
                        });

                        self.model.SelectedIssueIds = [];
                        self.model.SelectedItemIds = [];
                        self.model.medicationIssueItemlist = [];
                        self.model.SelectedNewIssueIds = []

                        if (self.model.requestType == 1) {
                            ClosePopup('close', 'medicationGetIssueModal');
                        } else if (self.model.requestType == 2) {
                            ClosePopup('close', 'medicationGetItemModal');
                        }

                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
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

        var ConfirmRemove = function (item) {
            self.model.PopupAction('Open', 'confirmRemove');
            self.model.itemForRemove = item;
        }

        var RemoveRowData = function (item) {
            var newArray = [];

            self.model.issueDetailList.items.map(function (obj) {
                if (obj.id !== item.id) {
                    newArray.push(obj);
                }
            });
            self.model.issueDetailList.items = newArray;
            self.model.PopupAction('Close', 'confirmRemove');
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
             var URI= CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.SAVE_API;
                var data = {
                    "patientId": itemList.patientId,
                    "visitType": itemList.visitType,
                    "encounterId": itemList.encounterId ? itemList.encounterId : undefined,
                    "admissionId": itemList.admissionId ? itemList.admissionId : undefined,
                    "wardId": itemList.wardId,
                    "bedId": itemList.bedId,
                    "storeId": itemList.storeId,
                    "statusId": SetStatus('Pending'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "cookieStoreId":$scope.storeId,
                    "items": []
                }
                angular.forEach(itemList.items, function (item, key) {

                    var item = {

                        "batchMapperId": item.batchMapperId,
                        "returnQuantity": item.returnQuantity,
                        "returnReasonId": item.reason,
                    }
                    data.items.push(item);

                });
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        NevigatetoPatientIssueReturn();
                        growl.success('Implant Return  Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving implant Return');
                    });
            }
        }

        var ValidateQuantity = function (item){
            var isValid = true;
            item['isReturnQuantityValid'] = false;

            if ((item.quantityIssued < (item.returnQuantity)) || !item.returnQuantity) {
                item['isReturnQuantityValid'] = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max returned quantity');
            }

            if (!item.returnQuantity || item.returnQuantity==0) {
                item['isReturnQuantityValid'] = true;
                isValid = false;
                growl.error("Please enter quantity greater than zero");
            }

            return isValid;
        }

        var ValidateReturnReason = function (item){
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

                    if ((item.quantityIssued < (item.returnQuantity)) || !item.returnQuantity) {
                        item['isReturnQuantityValid'] = true;
                        isValid = false;
                        growl.error('Entered quantity is exceeding the max returned quantity');
                    }

                    if (!item.returnQuantity || item.returnQuantity==0) {
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
            $state.go('implantReturn');
        }

     
        // var SavePopup = function () {
        //     var popup = angular.element('#confirmSave');
        //     popup.modal('show');
        // }

        // var NevigatetoPatientIssueReturn = function () {
        //     document.getElementById('popupClose').click();
        //     setTimeout(function () {
        //         $state.go('patientIssueReturn');
        //     }, 1000);
        // }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewConsignmentPatientImplantReturn", {
            url: '/addNewConsignmentPatientImplantReturn',
            templateUrl: 'views/master/TheatreManagement/add-new-consignment-patient-implant-return.html',
            controller: "AddNewConsignmentPatientImplantReturn.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewConsignmentPatientImplantReturn.Controller", addNewConsignmentPatientImplantReturn)
})();