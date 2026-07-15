(function () {
    "use strict";

    function detailsPatientIssueAcceptanceController($scope, $rootScope,$timeout, $http, $cookies, CONSTANTS, GenericService,GenericServiceParamHeader, $filter, $state, growl, CurrencyService, detailService, rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;

        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isAcceptQuantityValid: [],
                isrejectReasonIdValid: [],
                SelectedRequestItems: [],
                comorbidityFlag :false,
                notifiableFlag : false,
                patientDiagnosisListIcon: [],
                selectedItem: null,
                rejectReasons: [{
                    "reason": "Select Reason"
                }],
            };

            // Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })


            self.model.GetMedicationIssueAcceptanceDetails = GetMedicationIssueAcceptanceDetails;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveIssueAcceptance = SaveIssueAcceptance;
            self.model.NevigateToPatientIssueAcceptance = NevigateToPatientIssueAcceptance;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;
            self.model.valueOnChange = valueOnChange;
            self.model.ValidateAcceptQuantity =ValidateAcceptQuantity;
            self.model.ApprovedQuantity =ApprovedQuantity;
            self.model.ClosePopup = ClosePopup;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
            self.model.SetFocus = SetFocus;
            self.model.BarcodeScan = BarcodeScan;
            self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
            GetMedicationIssueAcceptanceDetails(detailService.getId());
        }
        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = self.model.rejectReasons.concat(reason);
        });

        $scope.size=0;
        var GetMedicationIssueAcceptanceDetails = function (itemId) {

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.MEDICATION_DETAIL_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.medicationIssueAcceptanceDetailslist = response.data;
                    $rootScope.allergydata(response.data);
                    getAllPatientDiagnosisForIcon(response.data.patientId);
                    self.model.medicationIssueAcceptanceDetailslist.items.forEach(function (item) {
                        item.isDisabled = true;
                        item.acceptQuantity = item.acceptQuantity ;
                        if (!!item.acceptQuantity && !!item.quantityIssued) {
                            ChangeItemStatus(item);
                        }
                        if (!!item.rejectQuantity) {
                            item.isRejected = true;
                            item.rejectReasonId = 1;
                        }
                        
                        if(item.rejectQuantity  != undefined ){
                        	item.itemDisabled = true;
                        }else{
                        	item.itemDisabled = false;
                        }
                        $scope.calculateQty(item);
                    });
                    
                    
                    
                    
                    self.model.medicationIssueAcceptanceDetailslist.items.forEach(function (item) {
                       
                    	if(item.itemDisabled == false)
                    		{
                    		 $scope.size= $scope.size+1;
                    		}
                        
                    });
                    
                    
                    $rootScope.stopLoader();
                });
        }

        $scope.calculateQty = function (item){
            if(self.model.medicationIssueAcceptanceDetailslist.status == "Partially Completed" || self.model.medicationIssueAcceptanceDetailslist.status == "Completed" || self.model.medicationIssueAcceptanceDetailslist.status == "Closed"){
                if (!!item.uomResponse) {
                    var quantityIssued = item.quantityIssued;
                    var orderedQty=item.orderQty;
                    if (item.uom == "Purchase") {
                        item.quantityIssued = parseInt(quantityIssued / (item.uomResponse[0].conversion * item.uomResponse[1].conversion * item.uomResponse[2].conversion));
                        item.orderQty = math.ceil(orderedQty / (item.uomResponse[0].conversion * item.uomResponse[1].conversion * item.uomResponse[2].conversion));
                    } else if (item.uom == "Stock") {
                        item.quantityIssued = parseInt(quantityIssued / (item.uomResponse[1].conversion * item.uomResponse[2].conversion));
                        item.orderQty = math.ceil(orderedQty / (item.uomResponse[1].conversion * item.uomResponse[2].conversion));
                    } else if (item.uom == "Dispense") {
                        item.quantityIssued = parseInt(quantityIssued / (item.uomResponse[2].conversion));
                        item.orderQty = math.ceil(orderedQty / (item.uomResponse[2].conversion));
                    }
                } 
            }
        }
        
        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }
        var BarcodeScan = function (event, barcode) {
            var enterCode = 13;
            if (!barcode && event.which === enterCode) {
                barcode = self.model.barcode;
            }

            if (!!barcode) {
                var itemId = barcode.split(' ')[0];
                var batchId = barcode.split(' ')[1];

                if (!!itemId && !!batchId) {
                    // AddBatchToDetail(self.model.materialPickerDetail, itemId,
					// batchId);
                }

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
        }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.medicationIssueAcceptanceDetailslist.items.forEach(function (item) {
                	if(item.itemDisabled != true){
                		 item.acceptQuantity=item.quantityIssued
                		 item.rejectQuantity = 0;
                		 item.isSelected = true;
                         item.isDisabled = false;
                         self.model.SelectedRequestItems.push(item);
                	}
                   

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.medicationIssueAcceptanceDetailslist.items.forEach(function (item) {
                	  item.acceptQuantity=0;
                    item.isSelected = false;
                    item.isDisabled = true;
                    // item.acceptQuantity = item.acceptQuantity +
					// item.rejectQuantity;
                    item.rejectQuantity = 0;
                    item.isRejected = false;
                });
            }
            // console.log(self.model.SelectedRequestItems);
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectRequestItem = function (item) {
            var itemIds = self.model.SelectedRequestItems;

            if (item.isSelected) {
            	 item.acceptQuantity=item.quantityIssued
                itemIds.push(item);
                item.isDisabled = false;
            } else {
                var index = itemIds.indexOf(item);
                item.isDisabled = true;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
                item.acceptQuantity = item.acceptQuantity + item.rejectQuantity;
                item.rejectQuantity = 0;
                item.isRejected = false;
            }

            if (self.model.medicationIssueAcceptanceDetailslist.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function (itemList) {
            var isValid = true;

            // if (self.model.rejectionFlag == false) {
            // var isValid = false;
            // growl.error("Please save rejection reason");
            // }

            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
                $scope.saveBtn = true;
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isAcceptQuantityValid'] = false;
                    item['isrejectReasonIdValid'] = false;

                    if ((item.quantityIssued < (item.acceptQuantity))) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity less than quantity issued");
                        $scope.saveBtn = true;
                    }

                    if (!item.acceptQuantity && item.acceptQuantity < 0) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity");
                        $scope.saveBtn = true;
                    }

                    if (item.acceptQuantity < 0) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than or equal to zero");
                        $scope.saveBtn = true;
                    }

                    // if (!!item.rejectQuantity) {
                    // if (!item.rejectReasonId){
                    // item['isrejectReasonIdValid'] = true;
                    // isValid = false;
                    // growl.error("Please select reject checkbox");
                    // }
                    // }
                });

            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }

        var ClearFields = function (itemList) {
            itemList.Id = "1" + (new Date()).getTime();
            itemList.uhid = null;
            itemList.patientDetail = null;
            itemList.wardName = null;
            itemList.dob = null;
            itemList.pdd = null;
            itemList.doa = null;
            itemList.bedNo = null;
            itemList.doctorDetail = null;
            itemList.issueNo = null;
            itemList.issueDate = null;
            itemList.indentNo = null;
            itemList.store = null;
            itemList.items = [];
        }

        var RejectQty = function (item) {
            self.model.selectedItem = item;
            if (!!item.isRejected) {
                selectedItemId = item.id;
                self.model.qty = Number(item.quantityIssued) - Number(item.acceptQuantity);
                self.model.rejectReason = self.model.rejectReasons[0];
                self.model.rejectionFlag = false;

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
            }
            else {
                item.isRejected = true;
                item.rejectReasonId = 1;
                var popup = angular.element('#cancelStatus');
                popup.modal('show');
            }
        }

        function valueOnChange(item, isSelectAll) {
            if (isSelectAll)
                self.model.medicationIssueAcceptanceDetailslist.items.forEach(function (item) {
                    // if (self.model.selectAllRows) {
                    // item.acceptQuantity = item.quantityIssued;
                    // item.rejectQuantity = 0;
                    // item.isReject = false;
                    // } else
                    // item.acceptQuantity = 0;
                    if (self.model.selectAllRows) {
                        if(item.acceptedQuantity == 0)
                        {
                            item.acceptQuantity = item.issueQuantity;
                            item.rejectQuantity = 0;
                            item.isRejected = false;       
                        }
                    }
                    else if(item.acceptedQuantity == 0)
                    {
                        item.acceptQuantity = 0;      
                    }
                });
            else
                if (item.isSelected) {
                    item.acceptQuantity = item.issueQuantity;
                    item.rejectQuantity = 0;
                    item.isRejected = false;
                } else
                {
                    item.acceptQuantity = 0;
                    item.rejectQuantity = 0;
                    item.isRejected = false;
                }
        }

        var ValidateRejection = function () {
            var isValid = true;
            let item = self.model.selectedItem;
            if (!self.model.rejectReason.id) {
              isValid = false;
              growl.error('Please Select Reason to Reject');
              item.isRejected = false;
            }
            return isValid;
        }

        var SaveRejectPopup = function () {
            if (ValidateRejection()) {
                let item = self.model.selectedItem;
            
                if(Number(self.model.qty) > item.quantityIssued) {
                    growl.error("Please enter quantity less than or equal to Quantity Issued");
                    item.isRejected = false;
                    return false;
                } else if(!self.model.rejectReason) {
                    growl.error("Please select reject");
                    item.isRejected = false;
                    return false;
                }
                if(self.model.qty != 0){
                    item.isRejected = true;
                    self.model.rejectionFlag = false;
                    item.rejectQuantity = self.model.qty;
                    item.acceptQuantity = item.quantityIssued - self.model.qty;
                    item['rejectReasonId'] = self.model.rejectReason.id;
                    item['note'] = self.model.rejectNote;
                    var popup = angular.element('#qtyaccepatReject');
                    popup.modal('hide');
                    // });
                    self.model.rejectReason = undefined;
                    self.model.rejectNote = undefined;
                    return
                }
                else{
                    item.isRejected = false;
                }
                item.rejectQuantity = self.model.qty;
                item.acceptQuantity = item.quantityIssued - self.model.qty;
                item['rejectReasonId'] = self.model.rejectReason.id;
                item['note'] = self.model.rejectNote;
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('hide');
                self.model.rejectReason = undefined;
                self.model.rejectNote = undefined;
            }
        }

        var CancelRejection = function (itemList) {
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.quantityIssued;
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var ChangeItemStatus = function (item) {    
            if (!!item.rejectQuantity) {
                item.isRejected = true;
            }
            if (item.newQty > parseInt(item.quantity)) {
                item.statusId =  SetStatus('Partially Completed');
            } else if (item.newQty == parseInt(item.quantity)) {
                item.statusId = SetStatus('Closed');
            }
        }

        $scope.saveBtn = true;
        $scope.saveBtnDisabled=false;
        var SaveIssueAcceptance = function (itemList) {
            $scope.saveBtnDisabled=true;
            $scope.saveBtn = false;
            if (ValidateBill(itemList)) {
                $scope.saveBtn = false;
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.MEDICATION_SAVE_API;
                var data = {

                    "id": itemList.id,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                }

                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        var PatientData = {
                            "id": item.id,
                            "statusId": item.statusId,
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectQuantity": item.rejectQuantity,
                            "reasonId": item.rejectReasonId,
                            "note": item.note,
                            "batchMapperId": item.batchMapperId
                        }
                        data.items.push(PatientData);
                    }
                });
              /*
				 * data.items.forEach(function (key) { var itemStatusList = [];
				 * itemStatusList.push(key.statusId);
				 * 
				 * if (itemStatusList.indexOf(8) !== -1) { // closed status id
				 * data.statusId = 8; // closed stutus } else { data.statusId =
				 * 4; // Completed stutus } });
				 */
                
                
                if(self.model.medicationIssueAcceptanceDetailslist.status == "Completed")
                	{
                	
                	$scope.length=data.items.length;
                	if($scope.size==$scope.length)
                		{
                		 data.statusId = 8;    
                		}
                	else
                		{
                		 data.statusId = 4;    
                		}
                	
                	}
                else
                	{
                	 data.statusId = 18;    
                	}
                
                
                
                
                
                
                
               /*
				 * if(self.model.medicationIssueAcceptanceDetailslist.statusId ==
				 * 4){ data.statusId = 8; }else{ data.statusId = 4; }
				 */
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $scope.saveBtn = true;
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NevigateToPatientIssueAcceptance();
                        growl.success('Patient issue acceptance successfully saved');

                    },
                    function (err) {
                        $scope.saveBtn = true;
                        $rootScope.stopLoader();
                        growl.error('Error while saving patient issue acceptance');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var NevigateToPatientIssueAcceptance = function () {
            // $state.go('patientIssueAcceptance');
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('patientIssueAcceptance');
            }, 1000);
        }

        var ValidateAcceptQuantity = function (item) {
            var isValid = true;
            if (parseInt(item.acceptQuantity) > item.quantityIssued) {
                item.isAcceptQuantityValid = true;
                isValid = false;
                growl.error('Entered accept quantity is exceeding the quantity issued');
            } else {
                item.acceptQuantity = parseInt(item.acceptQuantity);
                isValid = true;
            }

            return isValid;
        }

        var ApprovedQuantity = function (item) {
            $timeout(function(){
                if (item.acceptQuantity > item.quantityIssued) {
                    item.rejectQuantity = 0;
                    self.model.rejectQty = undefined;
                    item.isRejected = false;
                    } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
                    item.rejectQuantity = item.quantityIssued;
                    self.model.rejectQty = item.rejectQuantity;
                    item.isRejected = true;
                    RejectQty(item)
                    } else if (item.acceptQuantity < item.quantityIssued) {
                    item.rejectQuantity = item.quantityIssued - item.acceptQuantity;
                    self.model.rejectQty = item.rejectQuantity;
                    item.isRejected = true;
                    RejectQty(item)
                    } else if (item.acceptQuantity == item.quantityIssued) {
                    item.rejectQuantity = item.quantityIssued - item.acceptQuantity;
                    self.model.rejectQty = item.rejectQuantity;
                    item.isRejected = false;
                }
            },1000);
        }

        var ClosePopup = function (item) {
            item.isRejected = false;
            item.rejectReasonId = null;
            self.model.rejectionFlag = false;
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.quantityIssued;
            var popup = angular.element('#qtyaccepatReject');
            popup.modal('hide');
        }

        // Method to get the list of Generic
        var GetGenericList = function () {
            var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                self.model.genericList = response.data;
                GetGenericMaster();
                $rootScope.stopLoader();
            },
            function errorCallback(response) {
                $rootScope.stopLoader();
            });
        }
        GetGenericList();
    
        var genericMasterList = [];
        var GetGenericMaster = function () {
            var arraylist = self.model.genericList.filter(x => x.status == true);
            for (let generic of arraylist){
            genericMasterList.push(generic);
            }       
            $rootScope.stopLoader();
        } 

        var GetStrengthUnitList = function () {

            var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                  self.model.strengthUnitList = response.data;
                  console.log("Strength Units : ",self.model.strengthUnitList);
                  $rootScope.stopLoader();
                },
                function errorCallback(response) {
                  console.log("Error");
                  console.log("Strength Units : ",self.model.strengthUnitList);
                  $rootScope.stopLoader();
                });
          }
          GetStrengthUnitList();

          var ViewCombinationDetails = function(item) {
            self.model.genericCombinationList = [];
            var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
      
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
                for(let combination of response.data) {
                  let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
                  combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
                  combination.code = item.code;
                  combination.genericName = item.name;
                }
                self.model.strengthUnitList.forEach(function (strength){
                    response.data.forEach(function (data){
                      if(data.strength == strength.id){
                        $scope.strength = strength.unit;
                        var obj = {
                          strength: "",
                          code:"",
                          genericId: "",
                          genericName: "",
                          id:"",
                          name:"",
                          strengthUnit:"",
                          subGenericId: "",
                        }
                        obj.strength = $scope.strength;
                        obj.code = data.code;
                        obj.genericId = data.genericId;
                        obj.genericName = data.genericName;
                        obj.id = data.id;
                        obj.name = data.name;
                        obj.strengthUnit = data.strengthUnit;
                        obj.subGenericId = data.subGenericId;
                        self.model.genericCombinationList.push(obj);
                      }
                    });
                  });
                var popup = angular.element("#combinationDetails");
                popup.modal("show");
                $rootScope.stopLoader();
              },
              function errorCallback(response) {
                $rootScope.stopLoader();
              });
          }
      
          var getAllPatientDiagnosisForIcon= function(patientId){
            self.model.patientDiagnosisListIcon =[];
            var cookieObject = $cookies.getObject('cookieObject');
             var headerObjDia = {
                         'unit_id' : cookieObject.unitId,
                        'organization_id' : cookieObject.organizationId,
                        'patient_id' : patientId,
                       'Content-Type':'application/json',
             };
             var data={};
             var URI = CONSTANTS.ROOT_URL
             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
             GenericServiceParamHeader
             .serviceAction(METHOD_GET, URI,
                data,
                NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
             .then(
                     function(response) {
                         console.log(response);
                         // $rootScope.startLoader();
                         if (response.data.status == "success")
                         self.model.patientDiagnosisListIcon = response.data.listObject; 
                         console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                             self.model.comorbidityFlag = false;
                             self.model.notifiableFlag = false;
                            
                                if(self.model.patientDiagnosisListIcon.length > 0)
                                    {
                                        self.model.complaintDelete=false;
                                        angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                            if(value.isNotifiable == 'Y')
                                            {
                                                self.model.notifiableFlag = true;
                                            }
                                            else if(value.isComorbidity == 'Y')
                                            {
                                                self.model.comorbidityFlag = true;
                                            }
                                        });
                                    }
                         
                     });
            }
          var getAllPatientDiagnosisForIcon= function(patientId){
              self.model.patientDiagnosisListIcon =[];
              var cookieObject = $cookies.getObject('cookieObject');
               var headerObjDia = {
                           'unit_id' : cookieObject.unitId,
                          'organization_id' : cookieObject.organizationId,
                          'patient_id' : patientId,
                         'Content-Type':'application/json',
               };
               var data={};
               var URI = CONSTANTS.ROOT_URL
               + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
               GenericServiceParamHeader
               .serviceAction(METHOD_GET, URI,
                  data,
                  NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
               .then(
                       function(response) {
                           console.log(response);
                           // $rootScope.startLoader();
                           if (response.data.status == "success")
                           self.model.patientDiagnosisListIcon = response.data.listObject; 
                           console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                               self.model.comorbidityFlag = false;
                               self.model.notifiableFlag = false;
                              
                                  if(self.model.patientDiagnosisListIcon.length > 0)
                                      {
                                          self.model.complaintDelete=false;
                                          angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                              if(value.isNotifiable == 'Y')
                                              {
                                                  self.model.notifiableFlag = true;
                                              }
                                              else if(value.isComorbidity == 'Y')
                                              {
                                                  self.model.comorbidityFlag = true;
                                              }
                                          });
                                      }
                           
                       });
              }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsPatientIssueAcceptance", {
            url: "/detailsPatientIssueAcceptance",
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-issue-acceptance.html',
            controller: "DetailsPatientIssueAcceptance.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientIssueAcceptance.Controller", detailsPatientIssueAcceptanceController)
})();