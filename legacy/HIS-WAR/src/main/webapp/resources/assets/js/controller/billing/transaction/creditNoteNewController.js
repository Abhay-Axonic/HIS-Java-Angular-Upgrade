angular.module("myApp").controller("creditNoteNewController", ['$scope', '$rootScope', 'BillingGenericService', '$cookies', '$state', 'growl', 'reportService','$timeout', 'GenericService',
    function($scope, $rootScope, BillingGenericService, $cookies, $state, growl, reportService,$timeout, GenericService) {
        try {

            $rootScope.loginpage = true;


            $scope.dateOptions = {
                formatYear: 'yyyy',
                showWeeks: false
            };

            $scope.dateOptionsSearch = {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date()
            };
            $scope.openDatePicker = function() {
                $scope.datepicker.opened = true;
            };

            $scope.datepicker = {
                opened: false
            };

            $scope.openDatePickerPatiSearch = function() {
                $scope.datepicker1.opened = true;
            };

            $scope.datepicker1 = {
                opened: false
            };


            $scope.searchBillDetails = "";
            $scope.searchOrderDetails = "";
            $scope.openFromDate = function() {
                $scope.fromDatePicker.opened = true;
            };

            $scope.fromDatePicker = {
                opened: false
            };

            $scope.openToDate = function() {
                $scope.toDatePicker.opened = true;
            };

            $scope.toDatePicker = {
                opened: false
            };


            var cookieObject = $cookies.getObject('cookieObject');
            if (cookieObject == undefined) {
                $state.go('login');
                return;
            }
            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
            var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
            if (cookieObjectAppConfig == undefined || cookieObjectAppConfigSecond == undefined) {
                $state.go('applicationConfiguration');
                return;
            } else {
                console.log('cookieObjectAppConfig', cookieObjectAppConfig);

            }
            $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
            $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
            $scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
            $scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
            $scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
            $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
            $scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
            $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
            $scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.userId = cookieObject.userId;
            $scope.userName=cookieObject.userName;
            $scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
            $scope.discountRadio = 1;
            $scope.filterDummyService=1
            $scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
        	$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
        	$scope.isBillManipulationAllowed = ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
            /**================================COUNTER CODE START======================================*/
            $scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
            $scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
            if ($scope.isCounterApplicable == 'Y' && $scope.isUserCounterApplicable == 'Y') {
                var flag = $scope.getCounterDetails();
                if (flag) {
                    $rootScope.counterId = $rootScope.counterMstObj.counterId != "" ? $rootScope.counterMstObj.counterId : "";
                    $rootScope.counterDesc = $rootScope.counterMstObj.counterDesc != "" ? $rootScope.counterMstObj.counterDesc : "";
                } else {
                    $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
                }
            } else {
                $rootScope.counterId = 0;
            }
            /**================================COUNTER CODE END======================================*/

            $scope.billPayeeDetailsId = null;
            $scope.creditNoteApproval = false;
            $scope.creditNoteType = null;
            $scope.discountType = null;
            $scope.fromDiscountAmount = null;
            $scope.toDiscountAmount = null;
            $scope.getCreditNoteRightByUser = function() {
                $rootScope.startLoader();
                var URI = BASE_URL + ROOT_URL + "/billing/creditNote/rights/orgId/" + $scope.organizationId + "/unitId/" + $scope.unitId + "/user/" + $scope.userId;
                console.log('URI', URI);
                return BillingGenericService.serviceAction(METHOD_GET, URI, "",
                        NOTIFICATION_MSG_STATUS_FALSE)
                    .then(function(response) {
                        if (response.data.status == "success") {
                            var obj = response.data.object;
                            if (obj != null) {
                                $scope.creditNoteMstObj.isApproved = 'Y';
                                $scope.creditNoteMstObj.isCreditNoteApproval = true;
                                $scope.creditNoteApproval = true;
                                $scope.fromCreditNoteAmount = obj.fromCreditNoteAmount;
                                $scope.toCreditNoteAmount = obj.toCreditNoteAmount;
                            }
                            else{
                               $scope.creditNoteMstObj.isApproved = 'N';
                               $scope.creditNoteMstObj.isCreditNoteApproval = false;
                               $scope.creditNoteApproval = false;
                               $scope.fromCreditNoteAmount = 0;
                               $scope.toCreditNoteAmount = 0;
                            }
                        } else if (response.data.status == "error") {
                            $scope.creditNoteApproval = false;
                        }
                    });
            }
            $scope.getListOfUser = function() {
                var URI = BASE_URL + ROOT_URL +
                    "/billing/users/creditNote/rights/orgId/" + $scope.organizationId + "/unitId/" + $scope.unitId + "/amt/" + $scope.finalTotalCrAmount;
                BillingGenericService.serviceAction(METHOD_GET, URI, "",
                        NOTIFICATION_MSG_STATUS_FALSE)
                    .then(function(response) {
                        if (response.data.status == "success") {
                            $scope.userList = response.data.listObject;
                            if($scope.userList!=null && $scope.userList.length>0)
                            {
                               $('#creditNoteRequest').modal('show');
                            }
                            else{
                              growl.warning("No user found for approval of credit note.", {
                                                                 title: WARNING_MSG
                                                           });
                            }
                        } else {
                            $scope.userList = null;
                            growl.warning("No user found for approval of credit note.", {
                                   title: WARNING_MSG
                             });
                        }
                    });
            }

            $scope.getCreditNoteRightByUser();


            var getAmount = function(num) {
                return (isNaN(Number(num)) || num == null) ? 0 : (Number(parseFloat(num).toFixed(2)));
                //return isNaN(Number(num))?0:(Math.floor(num*100)/100);
            }

            $scope.getDiscountPerFromDiscount = function(billObj) {
                if (getAmount(billObj.previousDiscount) > 0) {
                    return getAmount((billObj.previousDiscount * 100) / billObj.grossAmount);
                } else {
                    return 0;
                }
            }

            var commonObj = {
                unitId: $scope.unitId,
                organizationId: $scope.organizationId
            };



            var URI = BASE_URL + ROOT_URL +
                GETACTIVEREASONLIST;

            BillingGenericService
                .serviceAction(METHOD_POST, URI,
                    commonObj,
                    NOTIFICATION_MSG_STATUS_FALSE)
                .then(
                    function(response) {
                        console.log(response);
                        // $rootScope.startLoader();
                        if (response.data.status == "success")
                            $scope.reasonList = response.data.listObject;
                    });

            /**Bill Wise==================================================*/
            $scope.initCreditNoteWiseDiscount = function() {
                $scope.creditNoteWiseDiscout = {
                    "discountGivenBy": "",
                    "discountCategoryId": "",
                    "discountAmountCreditNoteWise": 0.00,
                    "discountPercentageCreditNoteWise": 0.00
                }
            }
            $scope.initCreditNoteWiseDiscount();

            $scope.initCreditNotMastObj = function() {

                $scope.totalAmountSummary = 0;
                $scope.totalConcessionSummary = 0;
                $scope.totalPreviousDiscount = 0;
                $scope.netPayableSummary = 0;
                $scope.totalDiscountSummary = 0;
                $scope.totalGstTaxSummary = 0;
                $scope.totaCrAmount = 0;
                $scope.totaCrAmount = 0;
                $scope.totalRefundAmount = 0;
                $scope.totalBalanceAmount = 0;
                $scope.finalTotalCrAmount = 0.00;
                $scope.initCreditNoteWiseDiscount();
                $scope.signRoundOff = 0;
                $scope.payeePaidAmount =0;
                $scope.totalRoundOff =0;
                $scope.selfBillPayeeDetails = null;
                $scope.billWiseDiscountObj = {
                    "discountGivenBy": "",
                    "discountCategoryId": "",
                    "discountAmountBillWise": "",
                    "discountPercentageBillWise": ""
                }


                $scope.creditNoteMstObj = angular.copy({
                        creditNoteId: "",
                        creditNoteDate: "",
                        billId: "",
                        billPayeeId: "",
                        billPayeeDetailsId: "",
                        selfPayeeId : $scope.selfPayeeId,
                        crNoteAgainst: "",
                        creditNoteNo: "",
                        crTotalAmount: 0.00,
                        //crAmount : 0.00,
                        crTaxAmount: 0.00,
                        crNetAmount: 0.00,
                        crOutAdjustmentAmount: 0.00,
                        crDeuAmount: 0.00,
                        crReasonId: "",
                        ceRemark: "",
                        creditNoteRemark: "",
                        unitId: $scope.unitId,
                        orgnisationId: $scope.organizationId,
                        status: 'A',
                        createdDate: "",
                        createdBy: "",
                        updatedDate: "",
                        updatedBy: $scope.userId,
                        cancelOrDiscount: '',
                        requestSentTo: '',
                        isCreditNoteApproval: false,
                        isApproved: 'N',
                        patientId: '',
                        counterId: $rootScope.counterId,
                        discountRoundoff: 0,
                        signRoundOff: 0,
                        reqDiscId: '',
                        associateCompanyId: '',
                        listCreditNoteDetails: []
                    }

                );

                /*var URI = BASE_URL + ROOT_URL +
                    "/billing/getUsersDiscRights/orgId/" + $scope.organizationId + "/unitId/" + $scope.unitId + "/userId/" + $scope.userId;
                BillingGenericService.serviceAction(METHOD_GET, URI, "",
                        NOTIFICATION_MSG_STATUS_FALSE)
                    .then(function(response) {
                        console.log(response);
                        // $rootScope.startLoader();
                        if (response.data.status == "success") {
                            $scope.userDiscountObject = {};
                            $scope.userDiscountObject = response.data.object;
                        }
                    });*/
            }


            $scope.initCreditNoteDetailsObj = function() {
                $scope.creditNoteDetailsObj = angular.copy({
                    creditNoteDetailId: "",
                    creditNoteId: "",
                    crNoteDate: "",
                    orderDtlIdOld: "",
                    orderDetailId: "",
                    orderDetailIdNew: "",
                    crDiscountAmount: 0.00,
                    crServiceRateAmount: 0.00,
                    crAdditionalDiscount: 0.00,
                    crTaxAmount: 0.00,
                    //crAmount :0.00,
                    crNetAmount: 0.00,
                    billingDetailsId: '',
                    organisationId: $scope.organizationId,
                    unitId: $scope.unitId,
                    createdDate: "",
                    createdBy: $scope.userId,
                    updatedBy: $scope.userId,
                    updatedDate: "",
                    orderId: "",
                    orderDetailsId: "",
                    cancelOrDiscount: "",
                    listCreditNoteDiscountDetailsDto: []
                })
            }

            $scope.creditNoteDiscoutDetailsObj = {
                creditNoteDiscountDtlId: '',
                creditNoteDetailId: '',
                billingDetailsId: '',
                discountCategoryId: '',
                discountCategory: '',
                discountPercentage: 0.0,
                discountAmount: 0.0,
                discountGivenBy: '',
                unitId: $scope.unitId,
                orgnisationId: $scope.organizationId,
                createdDate: '',
                createdBy: $scope.userId,
                updatedDate: '',
                updatedBy: $scope.userId,
            }



            $scope.creditNoteDiscountDetails = {
                "creditNoteDiscountDtlId": '',
                "creditNoteDetailId": '',
                "billingDetailsId": "",
                "discountCategoryId": '',
                "discountCategory": '',
                "discountPercentage": '',
                "discountAmount": '',
                "discountGivenBy": $scope.userId,
                "unitId": $scope.unitId,
                "orgnisationId": $scope.organizationId,
                "createdDate": '',
                "createdBy": $scope.userId,
                "updatedDate": '',
                "updatedBy": $scope.userId
            }




            $scope.patientSearchObj = {
                "uhIdNumber": "",
                "patientName": "",
                "genderId": "",
                "mobileNo": "",
                "birthDate": "",
                "identificationTypeId": "",
                "identificationNo": "",
                "patientType": "1",
                "unitId": $scope.unitId,
                "organizationId": $scope.organizationId
            }


            //For Get Active Gender List
            $scope.getSexMasterList = function() {
                var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
                BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    $scope.sexMasterList = response.data.listObject;
                });
            }

            $scope.getIdentificationTypeList = function() {
                var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
                BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success")
                        $scope.identificationTypeList = response.data.listObject;
                });
            }

            $scope.getBankList = function() {
                var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
                var param = {
                    orgnisationId: $scope.organizationId,
                    unitId: $scope.unitId
                };
                BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.bankList = response.data.listObject;
                    }
                });
            }

            $scope.getCardTypesList = function() {
                var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
                var param = {
                    orgnisationId: $scope.organizationId,
                    unitId: $scope.unitId
                };
                BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.cardTypeList = response.data.listObject;
                    }
                });
            }

            $scope.getDiscountCategoryList = function() {
                var URI = BASE_URL + ROOT_URL + "/api/billing/discountCategories/orgId/" + $scope.organizationId + "/unitId/" + $scope.unitId;
                BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    $scope.discountCategoryList = response.data.listObject;
                });
            }


            $scope.getSpecialityList = function() {
                var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
                BillingGenericService.serviceAction(METHOD_POST, URI, commonObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success")
                        $scope.specialityMasterList = response.data.listObject;
                });



            }

            $scope.groupWiseServicesSelectedList = {
                services: []
            }
            $scope.groupWiseCheckAll = function(chkFlg) {
                $scope.disPerPopupForGroup = '';
                $scope.disAmtPopupForGroup = '';
                $scope.disCategoryIdForGroup = '';
                if (chkFlg == 'Y')
                    $scope.groupWiseServicesSelectedList.services = $scope.groupWiseDiscountServicesList.map(function(item) {
                        return item.serviceMasterId;
                    });
            };


            $scope.serachGroupWiseServicesClear = function() {
                $scope.serviceSpecialityIdForGroup = '';
                $scope.serviceSubSpecialityIdForGroup = '';
                $scope.chkFlgGroup = 'N';
                $scope.groupWiseServicesSelectedList.services = [];
                $scope.groupWiseDiscountServicesList = [];

                $scope.disCategoryIdForGroup = '';
                $scope.disPerPopupForGroup = 0;
                $scope.disAmtPopupForGroup = 0;
                //$scope.groupWiseDiscountServicesList  = $scope.autoRenderedServicesFinalList;
                //$scope.groupWiseDiscountServicesList = angular.copy($scope.autoRenderedServicesFinalList);
            }


            $scope.getDisPerPopupForGroup = function(disPer) {
                var len = $scope.groupWiseServicesSelectedList.services.length;
                if (len == 0) {
                    growl.warning("Please select at least one service!", {
                        title: WARNING_MSG
                    });
                    $scope.disPerPopupForGroup = '';
                    return false;
                } else {
                    var totalAmtPopupAllServices = 0;
                    angular.forEach($scope.groupWiseDiscountServicesList, function(value, key) {
                        if ($scope.groupWiseServicesSelectedList.services.some(serviceMasterId => serviceMasterId == value.serviceMasterId && value.isDiscountApplicable == 'Y')) {

                            totalAmtPopupAllServices = Number(totalAmtPopupAllServices) + Number(value.grossAmount);
                        }

                    });
                }

                var calDicAmt = (totalAmtPopupAllServices * disPer / 100).toFixed(2);
                $scope.disAmtPopupForGroup = calDicAmt;
            }

            $scope.getDisAmtPopupForGroup = function(disAmt) {
                var len = $scope.groupWiseServicesSelectedList.services.length;
                if (len == 0) {
                    growl.warning("Please select at least one service!", {
                        title: WARNING_MSG
                    });
                    $scope.disAmtPopupForGroup = '';
                    return false;
                } else {

                    var totalAmtPopupAllServices = 0;
                    angular.forEach($scope.groupWiseDiscountServicesList, function(value, key) {
                        if ($scope.groupWiseServicesSelectedList.services.some(serviceMasterId => serviceMasterId == value.serviceMasterId)) {
                            totalAmtPopupAllServices = Number(totalAmtPopupAllServices) + Number(value.grossAmount);
                        }

                    });

                    var disPer = ((parseFloat(disAmt) / totalAmtPopupAllServices) * 100).toFixed(2);
                    $scope.disPerPopupForGroup = disPer;
                }


            }

            $scope.serachGroupWiseServices = function() {

                $scope.dummyList = angular.copy($scope.billDetailsListClone);
                if ($scope.serviceSpecialityIdForGroup > 0 && $scope.serviceSubSpecialityIdForGroup > 0) {
                    $scope.groupWiseDiscountServicesList = [];
                    //alert("Both Grater");
                    var i = 0;
                    angular.forEach($scope.dummyList, function(value, key) {
                        if (value.specialityId == $scope.serviceSpecialityIdForGroup && value.subSpecialityMasterId == $scope.serviceSubSpecialityIdForGroup) {
                            $scope.groupWiseDiscountServicesList.push(value);
                        }

                        i++;
                    });



                } else if ($scope.serviceSpecialityIdForGroup > 0) {
                    $scope.groupWiseDiscountServicesList = [];
                    //alert("Both Grater");
                    var i = 0;
                    angular.forEach($scope.dummyList, function(value, key) {
                        if (value.specialityId == $scope.serviceSpecialityIdForGroup) {
                            $scope.groupWiseDiscountServicesList.push(value);
                        }

                        i++;
                    });
                }
            }

            $scope.getSubSpecialityListById = function(spId) {

                var specialityIDS = [];
                specialityIDS.push(spId);

                var data = {
                    organizationId: $scope.organizationId,
                    unitId: $scope.unitId,
                    specialityIdList: specialityIDS
                }
                console.log(data);
                var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/" + $scope.organizationId + "/unitId/" + $scope.unitId + "/specialityId/" + spId;
                BillingGenericService
                    .serviceAction("GET", URI,
                        "",
                        NOTIFICATION_MSG_STATUS_FALSE)
                    .then(
                        function(response) {
                            console.log(response);
                            // $rootScope.startLoader();
                            if (response.data.status == "success") {
                                $scope.subSpecialityMaterList = response.data.listObject;
                            }

                        });
            }

            $scope.clearDiscount = function() {
                $scope.disPerPopupForGroup = '';
                $scope.disAmtPopupForGroup = '';
                $scope.disCategoryIdForGroup = '';
            }

            $scope.addDiscountInDiscountListForGroup = function() {
                if ($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '') {
                    growl.warning("All fields are required.", {
                        title: WARNING_MSG
                    });
                    return false;
                } else if ($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '') {
                    growl.warning("All fields are required.", {
                        title: WARNING_MSG
                    });
                    return false;
                } else if ($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '') {
                    growl.warning("All fields are required.", {
                        title: WARNING_MSG
                    });
                    return false;
                } else {
                    var len = $scope.groupWiseServicesSelectedList.services.length;
                    if (len == 0) {
                        growl.warning("Please select at least one service!", {
                            title: WARNING_MSG
                        });
                        return false;
                    } else {

                        var discountPer = $scope.disPerPopupForGroup;
                        var discount = $scope.disAmtPopupForGroup;


                        var devide = len == 0 ? 1 : len;
                        angular.forEach($scope.groupWiseDiscountServicesList, function(value, key) {

                            if (value.isDiscountApplicable == 'Y' && parseFloat(value.refundAmount) == 0) {
                                value.discountDetailsList = [];
                                value.discountPercentage = (discountPercentageF == "" || discountPercentageF == undefined) ? 0.00 : discountPercentageF;
                                value.discount = (discountF == "" || discountF == undefined) ? 0.00 : discountF;

                                if (discountPer > 0) {
                                    var discountF = getAmount((getAmount(value.grossAmount) * (discountPer / 100)));
                                    var discountPercentageF = discountPer;

                                    value.discountPercentage = discountPercentageF;
                                    value.discount = discountF;

                                }
                                var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
                                creditNoteDiscountDetails.discountCategoryId = $scope.disCategoryIdForGroup;
                                creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
                                creditNoteDiscountDetails.discountAmount = value.discount;
                                //creditNoteDiscountDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy ;
                                value.discountDetailsList.push(creditNoteDiscountDetails);
                            }
                        });


                    }

                }


            }


            $scope.init = function() {
                $scope.billMstId = null;
                $scope.discountCategoryList = [];
                $scope.listPaymentDetails = [];
                $scope.getDiscountCategoryList();
                $scope.identificationTypeList = [];
                $scope.sexMasterList = [];
                $scope.getSexMasterList();
                $scope.getIdentificationTypeList();
                $scope.patientDetailsList = [];
                $scope.patientDetails = {};
                $scope.billing = true;
                $scope.getBankList();
                $scope.getCardTypesList();
                $scope.visitNoList = [];
                $scope.initCreditNotMastObj();
                $scope.initCreditNoteDetailsObj();
            }

            $scope.init();


            $scope.loadMoreData = function()
            {
                $scope.offset+=200;
                $scope.loadMoreFlag=true;
                $scope.searchPatient($scope.patientSearchObj);
            }
            $scope.offset=0;
            $scope.loadMoreFlag=false;
            $scope.noOfRecordsPerPage=200;
            $scope.patientList = [];
            $scope.searchPatient = function(patientSearchObj) {
                $scope.patientSearchObj.unitId = cookieObject.unitId;
                $scope.patientSearchObj.organizationId = cookieObject.organizationId;
                if (patientSearchObj.patientName != "" && patientSearchObj.patientName.length < 3) {
                    growl.warning("Please enter atleast 3 characters..!", {
                        title: "WARNING!"
                    });
                    $scope.patientList = [];
                    return false;
                }
                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
                if($scope.loadMoreFlag==true)
                {
                	$scope.patientSearchObj.offset=$scope.offset;
                $scope.loadMoreFlag=false;
                }
            else
                {
            	$scope.patientSearchObj.offset=0;
                $scope.loadMoreFlag=false;
                $scope.patientList = [];
                }
                $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
                var patSearchObj = angular.copy($scope.patientSearchObj);
                if ($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined") {
                    var dt = new Date($scope.patientSearchObj.birthDate);
                    //			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
                    patSearchObj.birthDate = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
                }
                $rootScope.startLoader();
                BillingGenericService.serviceAction(METHOD_POST, URI, patSearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    $scope.searchPatientList = [];
                    console.log("response", response);
                    if (response.data.status == "success") {
                        angular.forEach(response.data.listObject,function(value,index)
        						{
        					$scope.patientList.push(value);
        						})
                        patSearchObj = {};
                    }
                    if (response.data.listObject == null) {
                        growl.warning(response.data.message, {
                            title: ERROR_MSG
                        });
                        $scope.patientSearchObj.birthDate = "";
                        $scope.patientList = [];
                        return false;
                    }
                    $scope.patientSearchObj.birthDate = "";
                    $rootScope.stopLoader();
                });
                $scope.billList = [];
                $scope.payeeList = [];
                $scope.billDetailsList = [];
                $scope.billDetailsListClone = [];
                $scope.patientDetails = {};
                $scope.requestDiscountFlag = false;
                $scope.requestDiscountDiv = false;
            }
            
            $scope.admissionDetailsNew = {};
            $scope.getAdmittedPatientData = function(info){
            	$scope.admissionDetailsNew = {};
				var URI1 = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+info.admissionId;
				BillingGenericService.serviceAction(METHOD_GET,URI1, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.admissionDetailsNew = response.data.object;
						if($scope.admissionDetailsNew.payeeId != $scope.selfPayeeId)
						{
							$scope.contractId = $scope.admissionDetailsNew.contractId;
						}
					else
						{
							$scope.contractId = 0;
						}
						
						var URI2 = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+$scope.admissionDetailsNew.admissionId+"/encounter/0";
						BillingGenericService.serviceAction(METHOD_GET,URI2, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								 $scope.admissionDetailsNew.patientSponsorDetailsDtosList = [];
								 $scope.admissionDetailsNew.patientSponsorDetailsDtosList = response.data.listObject;
							}
						});
						
					}
						
				});
			
            	
            }
            $scope.getPatientDetailsById = function(patientId) {

                $rootScope.startLoader();
                $scope.patientDetailsList = [];
                $scope.patientDetails = {};
                $rootScope.startLoader();

                $scope.billDetailsListClone = [];
                $scope.billList = [];
                $scope.initCreditNotMastObj();
                $scope.initCreditNoteDetailsObj();
                $scope.billMstId = null;

                $scope.requestStatusObject = null;
                $scope.requestApporivedDiscountDiv = false;
                $scope.discountDisabledFlagForRequest = false;
                $scope.requestAppUser = 0;
                $scope.approvedAmtApp = 0;
                $scope.requestAmtApp = 0;
                $scope.statusApp = "";
                $scope.discountRadio = 2;
                $scope.requestStatusObject = null;
                var selectedPatientData  = $scope.patientList.find(ar=>ar.patientId == patientId);
				 

                var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/" + patientId;
                BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.patientDetailsList = response.data.listObject;
                        $scope.patientDetails = response.data.listObject[0];
                        var index = $scope.patientList.findIndex(x => x.patientId == patientId);
                        $scope.patientDetails.age = ($scope.patientList[index].age);
                        $scope.patientDetails.patientName = ($scope.patientList[index].patientName);
                        $scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
                        $scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
                        $scope.patientDetails.patientId = ($scope.patientList[index].patientId);
                        $scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
                        $scope.creditNoteMstObj.patientId = patientId;
                        if ($scope.requestStatusObject == null) {
                            $scope.requestDiscountFlag = false;
                            $scope.requestDiscountDiv = false;
                        }
                        $scope.getBillListByPatientId(patientId);
                        
                        if(selectedPatientData != undefined && selectedPatientData.admissionId != null ){
                        	$scope.getAdmittedPatientData(selectedPatientData);
                        } 
                        $rootScope.stopLoader();
                    } else if (response.data.status == "error") {
                        $rootScope.stopLoader();
                    }
                });
            }



            $scope.getBillListByPatientId = function(patientId) {
                $scope.billList = [];
                $rootScope.startLoader();
                var URI = BASE_URL + ROOT_URL + "/api/billing/creditNote/bills/" + $scope.organizationId + "/" + $scope.unitId + "/" + patientId;
                BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.billList = response.data.listObject;
                        if ($scope.billList != null && $scope.billList.length > 0)
                            $(".anchor-tag").parents().next(".old-encounter-details").toggle();
                        $rootScope.stopLoader();
                    } else if (response.data.status == "error") {
                        $rootScope.stopLoader();
                    }

                });
            }
            $scope.encounterDetails = {};
            $scope.getEncounterAndAdmissionDetails = function(visitTypeId,admissionId) {
                $rootScope.startLoader();
                $scope.encounterDetails = {};
                var newArr = {};
		        
		        if(visitTypeId == 1){
		        	newArr.encounterId = admissionId;
		        	$scope.encounterIdNew = admissionId;
		        }else{
		        	newArr.admissionId = admissionId;
		        	$scope.admissionIdNew = admissionId;
		        }
                
                var URI = BASE_URL + ROOT_URL + "/api/billing/getEncounterAndAdmissionDetails";
                BillingGenericService.serviceAction("POST", URI, newArr, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.encounterDetails = response.data.object;
                        $rootScope.stopLoader();
                    } else if (response.data.status == "error") {
                        $rootScope.stopLoader();
                    }

                });
            }
            
            
            $scope.forceCancelPackage=function(obj){
            	var packageConsumptionMasterId = null;
            	angular.forEach($scope.listPackageOrder,function(item,key){
            		if(obj.packageId == item.packageId){
            			packageConsumptionMasterId = item.packageConsumptionMasterId;
            		}
            	});
        		var data={
        				packageConsumptionMasterId:packageConsumptionMasterId,
        				toDate : moment().format('YYYY-MM-DD HH:mm:ss'),
        				unitId : $scope.unitId
        		}
        		$rootScope.startLoader();
        		var URI = BASE_URL + ROOT_URL + "/api/packages/forceCancelPackage";
        		BillingGenericService.serviceAction("POST", URI, data).then(function(response){
        			if(response.data.status == "success")
        				{
        					growl.success("Package closed",{
        						title : WARNING_MSG
        					});
        					$rootScope.stopLoader();
        				}else{
        					$rootScope.stopLoader();
        				}
        				
        		});
        	}
            
            $scope.getListPackageOrder = function(index) {

                var URI = BASE_URL+ ROOT_URL
                        + "/api/packages/getPackageListOnCreditNote/"+$scope.patientDetails.patientId+"/"+ $scope.admissionId
                        + "/" + $scope.organizationId + "/"+ $scope.unitId+"/"+$scope.visitTypeId;
                    $rootScope.startLoader();
                GenericService.serviceAction("GET", URI).then(function(response) {
                                    $scope.listPackageOrder = [];
                                    
                                    if(response.data.listObject !=null && response.data.listObject.length >0){
                                    	
                                    	  angular.forEach(response.data.listObject,function(item,key){
                                          	if(item.isForceClose != null && item.isForceClose == 1){
                                          		  $scope.listPackageOrder.push(item);
                                          	}
                                          });
                                    	  
                                    }
                                    
                                    if($scope.listPackageOrder.length > 0){
                                        $scope.getPackages(index);
                                    }else{
                                    	$scope.calculateTotalAmount($scope.billDetailsListClone,index);
                                    }
                                
                                   
                                });
            }
            

            $scope.getPackages = function(index){
                
                angular.forEach($scope.listPackageOrder,function(item,key){
                    var data={};
                    data[item.packageConsumptionMasterId]=item.packageId;   
                    var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package";
                    BillingGenericService.serviceAction("POST", URI, data).then(function(response){
                        if(response.data.status == "success")
                            {
                               $scope.packageTypeId = response.data.listObject[0].mPackageMasterDto.packageTypeId;
                               $scope.listPackage = [];
                               $scope.listPackage = response.data.listObject[0];
                               var paymentEntitlementIdPackage = 1;
                                 angular.forEach($scope.encounterDetails.patientSponsorDetailsDtosList, function(value, key) {
                                     if(value.priorityId == 1 ){
                                         paymentEntitlementIdPackage = value.paymentEntitlementId;
                                         
                                     }
                                 });
                                 $scope.cunsumedServiceList = [];
                                var listObj = []; 
                               angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(rate,key){
                                   if($scope.visitTypeId == 1){
                                       if(rate.consumedQty > 0){
                                          var obj = {
                                                "encounterId" :$scope.encounterId,
                                                "contractId" :null,
                                                "serviceId" :rate.serviceMasterId,
                                                "currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
                                                "organizationId":$scope.organizationId,
                                                "unitId":$scope.unitId,
                                                "visitTypeId":1,
                                                "billingBedCategoryId":$scope.opdBillingCatId,
                                                "patientCategoryId": $scope.visitTypeId  == 1 ? $scope.encounterDetails.patientSponsorDetailsDtosList[0].patientTypeId :$scope.encounterDetails.patientCategoryId,
                                                "defaultSelfTariffId":$scope.defaultSelfTariffId,
                                                "selfPayeeId" :$scope.selfPayeeId,
                                                "patientClassId":null,
                                                "quantity":rate.consumedQty,
                                                "patientId":$scope.patientDetails.patientId,
                                                "tariffId" :$scope.defaultSelfTariffId,
                                                "paymentEntitlementId":paymentEntitlementIdPackage,
                                                "taxPercentage":null,
                                                "keyIndex":key
                                            };
                                          listObj.push(obj);
                        
                                     }
                                      
                                   }else{
                                       if(rate.consumedQty > 0){
                                             var obj = {
                                                     "admissionId" :$scope.admissionId,
                                                   "encounterId" :$scope.admissionId,
                                                     "contractId" :$scope.contractId,
                                                   "serviceId" :rate.serviceMasterId,
                                                   "currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
                                                   "organizationId":$scope.organizationId,
                                                   "unitId":$scope.unitId,
                                                   "visitTypeId":$scope.visitTypeId,
                                                   "billingBedCategoryId":$scope.admissionDetailsNew.bedBillingCategoryId,
                                                   "patientCategoryId": $scope.visitTypeId  == 1 ? $scope.admissionDetailsNew.patientSponsorDetailsDtosList[0].patientTypeId :$scope.encounterDetails.patientCategoryId,
                                                   "defaultSelfTariffId":$scope.defaultSelfTariffId,
                                                   "selfPayeeId" :$scope.selfPayeeId,
                                                   "patientClassId":$scope.admissionDetailsNew.patientCategoryId,
                                                   "quantity":rate.consumedQty,
                                                   "patientId":$scope.patientDetails.patientId,
                                                   "tariffId" :$scope.defaultSelfTariffId,
                                                   "paymentEntitlementId":$scope.admissionDetailsNew.patientSponsorDetailsDtosList[0].paymentEntitlementId,
                                                   "taxPercentage":null,
                                                   "keyIndex":key
                                               };
                                             listObj.push(obj);
                                          }
                                      }
                                   
                                   
                               });// end package service for loop
                              
                               if(listObj != null && listObj.length>0){
                            	   
                            	   var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobalList";
                                   BillingGenericService.serviceAction(METHOD_POST,URI, listObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                                       if(response.data.status=="success")
                                       {
                                           
                                           if(response.data.listObject != null && response.data.listObject.length>0)
                                           {
                                        	   
                                        	   angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(rate,key){
                                        	   
	                                            	   angular.forEach(response.data.listObject, function(value, inx) {
	                                                       
	                                            		   if(value.doesNotExist==null && key==value.keyIndex)
	                                                       {
	                                                           rate.basePrice=value.serviceRate * rate.consumedQty;
	                                                           rate.oldBasePrice =value.serviceRate * rate.consumedQty;
	                                                           rate.dummyServiceAmt=value.serviceRate * rate.consumedQty;
	                                                           rate.tariffId=value.tariffId;
	                                                           rate.coPayPer=value.coShare;
	                                                           rate.coShare =value.coShare;
	                                                           rate.selfPayable = value.selfPayable;
	                                                           rate.creditPayable = value.creaditPayable;
	                                                           rate.netPay = value.netAmount;
	                                                           $scope.cunsumedServiceList.push(rate);
	                                                       }
	                                            		   
	                                                   });
                                        	   });
                                              
                                           }
                                           
                                           $scope.calculateTotalAmount($scope.billDetailsListClone,index);
                                        }
                                    }); // end API service rate calling function 
                            	   
                               }else{
                            	   $scope.calculateTotalAmount($scope.billDetailsListClone,index);
                               }// end List length check 
                               
                            }
                            
                    });// end API package details calling function 
                    
                });// end package for loop
            }
            

            $scope.billStatusId  = 1;
            $scope.getBillDetailsByBillId = function(billObj) {
                $scope.billMstId = billObj.billingMasterId;
                $scope.visitTypeId = billObj.visitTypeId;
                $scope.admissionId = (billObj.admissionId == null) ? billObj.encounterId : billObj.admissionId;
                $scope.getEncounterAndAdmissionDetails($scope.visitTypeId,$scope.admissionId );
                $scope.cunsumedServiceList =[];
                $scope.paidAmountBill = 0;
                $scope.balanceAmountBill = 0;
                $scope.paidAmountBill = billObj.amountPaid;
                $scope.balanceAmountBill = billObj.outstanding;
                $scope.billDetailsList = [];
                $scope.billDetailsListClone = [];
                $scope.payeeList = [];
                $rootScope.startLoader();
                $scope.cancelDiscount();
                var URI1 = BASE_URL + ROOT_URL + "/api/billing/cr/payee/bill/" + billObj.billingMasterId + "/" + billObj.visitTypeId + "/" + $scope.admissionId;
                BillingGenericService.serviceAction(METHOD_GET, URI1, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.visitTypeId = billObj.visitTypeId;
                        $scope.payeeList = response.data.listObject;
                        $scope.totalRoundOff = response.data.resObject;
                        if ($scope.payeeList != null && $scope.payeeList.length > 0) {
                            angular
                                .forEach(
                                    $scope.payeeList,
                                    function(value, key) {
                                        if ($scope.selfPayeeId == value.payeeId) 
                                        {
                                        	$scope.selfBillPayeeDetails = $scope.payeeList[key];
                                        	$scope.creditNoteMstObj.billPayeeId = $scope.selfPayeeId;
                                        	$scope.billPayeeDetailsId = value.billPayeeDetailsId;
                                            $scope.payeePaidAmount = value.paidAmount-(value.disallowedAmount+value.otherDeduction)-value.crDeuAmount;
                                        }
                                       
                                    });
                        }
                        
                        angular.forEach(response.data.object,function(obj,key)
            					 {
            						if($scope.pharmacyServiceId == obj.serviceMasterId)
            						{
            							obj.isDiscountApplicable = 'N';
            							obj.discountDisabledFlag = 'Y';
            							
            						}
            						 $scope.billStatusId  = obj.billStatusId;
            					 }); 
                       
                        
                        if($scope.isBillManipulationAllowed==false)
        				{
        					$scope.billDetailsList = [];
        					angular.forEach(response.data.object,function(obj,key)
        					 {
        						if(obj.isDummyService==false)
        						{
        							$scope.billDetailsList.push(obj);
        						}
        					 });
        				}else{
        					$scope.billDetailsList = angular.copy(response.data.object);
        				}
                        $scope.billDetailsListForDymmyServiceFilter = angular.copy($scope.billDetailsList);
                        $scope.billDetailsListClone = angular.copy($scope.billDetailsList);
                        $scope.getBillPayeeDetailsObj();
                        $scope.cunsumedServiceList = [];
                       /* angular.forEach($scope.billDetailsListClone,function(v,k){
                        	if(v.isPackageService != 'Y' && 
                        			v.refundAmount==0 && 
                        			v.serviceRendered!=1  && 
                        			(v.package==false || ((v.package==true && v.packageTypeId==5)) &&(v.netAmt==v.selfPayable||v.netAmt==v.creditPayable))
                        			){
                        		  $scope.getListPackageOrderNew();
                        	}
                        }); */
                      
                       /*$scope.calculateTotalAmount($scope.billDetailsListClone);*/
                        console.log($scope.billDetailsListClone);

                        $scope.requestStatusObject = {};
                        var data1 = {};
                        if (billObj.visitTypeId == 1) {
                            $scope.encounterId = billObj.encounterId;
                            data1 = {
                                encounterId: $scope.encounterId,
                                requestType: 6,
                                billId:billObj.billingMasterId
                            }
                        } else if (billObj.visitTypeId == 2 || billObj.visitTypeId == 3 || billObj.visitTypeId == 4) {
                            $scope.admissionId = billObj.admissionId;
                            data1 = {
                                admissionId: $scope.admissionId,
                                requestType: 6,
                                billId:billObj.billingMasterId
                            }
                        }
                         $scope.getCreditNoteRightByUser();
                        

                        $rootScope.stopLoader();
                    } else if (response.data.status == "error") {
                        growl.warning(response.data.message, {
                            title: WARNING_MSG
                        });
                        $rootScope.stopLoader();
                    }

                });


                /*		var URI = BASE_URL + ROOT_URL + "/api/billing/billDetails/"+$scope.organizationId+"/"+$scope.unitId+ "/"+billMasterId;
                		console.log("URI",URI);
                		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
                		{
                			if (response.data.status == "success"){*/

                /*	}
                	$rootScope.stopLoader();
                });*/
            }


            $scope.getDiscountListForInfo = function(billingDetailsObj) {
                $scope.creditNoteDiscountDetailsList = [];
                $rootScope.startLoader();
                $scope.serciveNameView = billingDetailsObj.serviceStandardName;
                $scope.totalAmtView = billingDetailsObj.netAmt;
                $scope.totalDiscountAmtView = 0;

                var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/discount/details/" + billingDetailsObj.billingDetailsId;
                BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.creditNoteDiscountDetailsList = response.data.listObject;
                        if ($scope.creditNoteDiscountDetailsList != null && $scope.creditNoteDiscountDetailsList.length > 0) {
                            angular.forEach(creditNoteDiscountDetailsList, function(value, key) {
                                $scope.totalDiscountAmtView = $scope.totalDiscountAmtView + value.discountAmount;
                            });

                        }
                        $rootScope.stopLoader();
                    } else if (response.data.status == "error") {
                        $rootScope.stopLoader();
                    }

                });
            }

            $scope.cancelDiscount = function() {
                $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;

                $scope.billDetailsListClone = angular.copy($scope.billDetailsList);

                if ($scope.requestStatusObject == null) {
                    $scope.requestDiscountFlag = false;
                    $scope.requestDiscountDiv = false;
                    angular.forEach($scope.billDetailsListClone, function(cloneObj, key) {
                        cloneObj.discountDisabledFlag = 'N';
                    });
                }
                $scope.calculatePayerRefundableAmount();
                //$scope.calculateTotalAmount($scope.billDetailsListClone);
                
                angular.forEach($scope.billDetailsListClone, function(value, key) {
               	 $scope.calculateShareAmount(value,key);
               });
            }


            
            
            $scope.calculateConsumedAmountInMultivisitPackage = function(){
            	var obj = {
            			"admissionId" :$scope.admissionId,
    					"encounterId" :$scope.encounterDetails.encounterId,
    					"contractId" :null,
    					"serviceId" :$item.serviceMasterId,
    					"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
    					"organizationId":$scope.organizationId,
    					"unitId":$scope.unitId,
    					"visitTypeId":1,
    					"billingBedCategoryId":$scope.opdBillingCatId,
    					"patientCategoryId":$scope.patientDetails.patientTypeId,
    					"defaultSelfTariffId":$scope.defaultSelfTariffId,
    				    "selfPayeeId" :$scope.selfPayeeId,
    				    "patientClassId":$scope.encounterDetails.patientClassId,
    				    "quantity":1,
    				    "patientId":$scope.patientDetails.patientId,
    				    "tariffId" :$scope.defaultSelfTariffId,
    				    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
    				    "taxPercentage":$item.taxPercentage,
    				};
    			
    			// console.log('Base prise',obj)
    			$rootScope.startLoaderPopup();
    			var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
    			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    				if(response.data.status=="success")
    				{
    					
    					if(response.data.object != "undefined" && response.data.object != null)
    					{
    						if(response.data.object.doesNotExist==null)
							{
    							$scope.forServicePayeeId = response.data.object.payeeId;
        						$item.basePrice=response.data.object.serviceRate;
        						$item.oldBasePrice =response.data.object.serviceRate;
        						$item.dummyServiceAmt=response.data.object.serviceRate;
        						$item.coPayPer=response.data.object.coShare;
        						$item.quantity=1;
        						$item.coShare = response.data.object.coShare;
        						$item.selfPayable = response.data.object.selfPayable;
        						$item.creditPayable = response.data.object.creaditPayable;
        						$item.netPay = response.data.object.netAmount;
        						$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
        						$item.contractId = response.data.object.contractId;
        						$item.patientClassPercentage = response.data.object.patientClassPercentage;
        						$item.markupStatus = response.data.object.markupStatus;
        						$item.minRateEditable = response.data.object.minRateEditable;
        						$item.maxRateEditable = response.data.object.maxRateEditable;
        						$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
        						$item.isRateEditable=response.data.object.isRateEditable;
        						$item.isQuantityEditable=response.data.object.isQuantityEditable;
        						$item.tariffId=response.data.object.tariffId;
        						$item.ordTotalAmount = response.data.object.ordTotalAmount;
        						$item.concession = response.data.object.concession;
        						$item.isDiscountApplicable = response.data.object.isDiscountApplicable;
        						$item.patientClassPercentage = response.data.object.patientClassPercentage;
        						$item.taxAmt = response.data.object.taxAmt;
        						$item.taxPercentage = response.data.object.taxPercentage;
        						$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
        						$item.bedCategoryId=response.data.object.billingBedCategoryId;
        						$item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
        						$item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
        						$item.freeTextOrderedServicesId = $scope.freeTextOrderedServicesId;
        						$item.freeTextserviceName = $scope.freeTextserviceName;
        						$scope.servicePopupCalculate($item);
        						
        						angular.forEach($scope.freeTextServiceList, function (value, key) {
        							if(freeTextOrderedServicesId==value.freeTextOrderedServicesId){
        									value.status = 'I';
        								}
        							});
        						$scope.freeTextserviceName = "";
        						$scope.freeTextOrderedServicesId= null;
        						$rootScope.stopLoaderPopup();
        						$scope.serviceCheckFlag = false;
        						$scope.serviceCheckFlagForParameter=false;
							}else{
								$rootScope.stopLoaderPopup();
    							growl.warning("Service is not mapped with the tariff..!!",{
    								title : WARNING_MSG
    							});
    							$scope.serviceCheckFlag = false;
    							$scope.serviceCheckFlagForParameter=false;
    							return false;
							}
    						
    					}
    					else
    						{
    							$rootScope.stopLoaderPopup();
    							growl.warning("Please add base price of this service and then add..!!",{
    								title : ERROR_MSG
    							});
    							$scope.serviceCheckFlag = false;
    							$scope.serviceCheckFlagForParameter=false;
    							return false;
    						}
    					$scope.isFromAddServiceToTempServiceList=false;
    					$scope.serviceCheckFlag=false;
    					$scope.serviceCheckFlagForParameter=false;
    					// concession
    					// console.log("FINAL ITEM",$item);
    				 }
    			 });
            }
            $scope.calculateTotalAmount = function(inputList,index) {
                $scope.totalAmountSummary = 0.00;
                $scope.totalAmountSummaryDis = 0.00;
                $scope.totalConcessionSummary = 0.00;
                $scope.netPayableSummary = 0.00;
                $scope.totalDiscountSummary = 0.00;
                $scope.totalGstTaxSummary = 0.00;
                $scope.totaCrAmount = 0.00;
                $scope.totaCrTaxAmount = 0.00;
                $scope.totalRefundAmount = 0.00;
                $scope.totalBalanceAmount = 0.00;
                $scope.finalTotalCrAmount = 0.00;
                $scope.totalCrAmountR = 0.00;
                $scope.discountRoundOff = 0.00;
                $scope.totalPreviousDiscount = 0.00;
                $scope.totalremainingRefundAmt = 0.00;
                $scope.totaCrAmountGrid = 0.00;

                angular.forEach(inputList, function(value, key) {

                	if($scope.pharmacyServiceId != value.serviceMasterId)
					{
                		 value.isDiscountApplicable = 'Y';
                         value.discountDisabledFlag = 'N';
					}
                	
                   
                    if (value.serviceMasterId != $scope.surchargeServiceId) {
                      
                        value.discountDetailsList = (value.discountDetailsList == null) ? [] : value.discountDetailsList;
                        value.discountPercentage = (value.discountPercentage == null || value.discountPercentage == "" || isNaN(value.discountPercentage)) ? parseFloat(0).toFixed(2) : Number(parseFloat(value.discountPercentage));
                        value.discount = (value.discount == null || value.discount == "" || isNaN(value.discount)) ? parseFloat(0) : getAmount(value.discount);

                        if (value.discountPercentage == 0.00 && value.discount > 0.00) {
                            value.discountPercentage = (value.discount * 100) / (value.remainingRefundAmt);
                        } else if (value.discount == 0.00 && value.discountPercentage > 0.00) {
                            value.discount = getAmount(value.remainingRefundAmt) * (value.discountPercentage / 100);

                        } else if (value.discount == 0.00 && value.discountPercentage == 0) {
                            value.discount = getAmount(value.remainingRefundAmt) * (value.discountPercentage / 100);
                        }
                        //value.discountPercentage =  getAmount(value.discountPercentage);
                        value.discount = getAmount(value.discount);


                        if (value.discountPercentage > 100) {
                            growl.warning("Percentage Should be less than 100%.", {
                                title: WARNING_MSG
                            });
                            value.discountPercentage = 0.00;
                            value.discount = 0.00;
                            $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                            $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                        }

                        if (value.isSelected) {
                            //value.remainingRefundAmt = getAmount(value.netAmt);
                            value.cancelOrDiscount = 1;
                            value.discountPercentage = 0;
                            value.discount = 0;
                            $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                           // $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                            value.discountCategoryId = '';
                            value.refundAmount = getAmount(value.refundAmount);
                            if (value.isSelected == true) {

                                var txtAmt = 0;
                                if (parseFloat(value.taxPercentage) > 0) {
                                    txtAmt = (parseFloat(value.remainingRefundAmt) * parseFloat(value.taxPercentage)) / (100 + parseFloat(value.taxPercentage));
                                    var netA = parseFloat(value.remainingRefundAmt) - txtAmt;

                                    value.crTaxAmount = getAmount(txtAmt);
                                    value.crAmount = getAmount(netA);
                                    value.crNetAmount = getAmount(netA);
                                    value.crAmount = value.crNetAmount;

                                } else {
                                    value.crAmount = getAmount(value.remainingRefundAmt);
                                    value.balanceAmount = 0;
                                    value.crTaxAmount = (value.taxAmount);
                                    value.crNetAmount = getAmount(value.remainingRefundAmt);
                                }

                            } else {
                                //value.balanceAmount=getAmount(value.netAmt-value.refundAmount);
                                value.crAmount = 0;
                                value.crNetAmount = 0;
                                value.crTaxAmount = 0;
                            }
                        } else if (value.isDiscountApplicable == 'Y' && parseFloat(value.remainingRefundAmt) != 0) {

                            var txtAmt = 0;
                            /*&& value.discount==0.00*/
                            if (parseFloat(value.discountPercentage) > 0) {

                                if (parseFloat(value.taxPercentage) > 0) {

                                    txtAmt = (parseFloat(value.remainingRefundAmt) * parseFloat(value.taxPercentage)) / (100 + parseFloat(value.taxPercentage));

                                    var netA = parseFloat(value.remainingRefundAmt) - txtAmt;

                                    value.discount = (getAmount(value.remainingRefundAmt)) * (value.discountPercentage / 100);
                                    value.discountPercentage = getAmount(value.discountPercentage);
                                    value.crTaxAmount = (getAmount(txtAmt)) * (value.discountPercentage / 100);

                                    value.crNetAmount = getAmount(getAmount(value.discount) - getAmount(value.crTaxAmount));

                                    //value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);

                                    value.crAmount = value.crNetAmount;
                                } else {
                                    value.crTaxAmount = 0;
                                    value.taxPercentage = 0;
                                    txtAmt = 0;
                                    value.crTaxAmount = 0;

                                    var netA = parseFloat(value.remainingRefundAmt) - txtAmt;

                                    value.discount = (getAmount(value.remainingRefundAmt)) * (value.discountPercentage / 100);
                                    value.discountPercentage = getAmount(value.discountPercentage);
                                    value.crNetAmount = getAmount(getAmount(value.discount) - getAmount(value.crTaxAmount));

                                    //value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);

                                    value.crAmount = value.crNetAmount;

                                }
                                /*parseFloat(value.discountPercentage) == 0 &&*/
                            } else if (parseFloat(value.discount) > 0) {

                                if (parseFloat(value.taxPercentage) > 0) {

                                    txtAmt = (parseFloat(value.remainingRefundAmt) * parseFloat(value.taxPercentage)) / (100 + parseFloat(value.taxPercentage));

                                    var netA = parseFloat(value.remainingRefundAmt) - txtAmt;

                                    value.discountPercentage = parseFloat((parseFloat(value.discount) / parseFloat(value.remainingRefundAmt)) * 100);

                                    value.crTaxAmount = (getAmount(txtAmt)) * (parseFloat(value.discountPercentage) / 100);

                                    value.crNetAmount = getAmount(parseFloat(value.discount) - parseFloat(value.crTaxAmount));

                                    // value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);
                                    value.crAmount = value.crNetAmount;
                                } else {
                                    value.crTaxAmount = 0;
                                    //value.crAmount =0;
                                    value.taxPercentage = 0;
                                    txtAmt = 0;
                                    value.crTaxAmount = 0;

                                    var netA = parseFloat(value.remainingRefundAmt) - txtAmt;

                                    value.discountPercentage = parseFloat((parseFloat(value.discount) / parseFloat(value.remainingRefundAmt)) * 100);

                                    value.crNetAmount = getAmount(getAmount(value.discount) - getAmount(value.crTaxAmount));

                                    // value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);
                                    value.crAmount = value.crNetAmount;

                                }
                            }

                        }
                        if (isNaN(getAmount(value.crAmount))) {
                            value.crAmount = 0;
                        } else {
                            value.crAmount = value.crAmount;

                        }
                        if (isNaN(getAmount(value.crNetAmount))) {
                            value.crNetAmount = 0;
                        } else if (value.discount == 0.00 && value.discountPercentage == 0 && !value.isSelected) {

                            value.crNetAmount = 0;
                            value.crTaxAmount = 0;
                            value.crAmount = 0;

                        } else {
                            value.crNetAmount = value.crNetAmount;
                        }


                        value.grossAmount = getAmount(value.grossAmount);
                        value.concession = getAmount(value.concession);
                        value.previousDiscount = getAmount(value.previousDiscount);
                        value.netAmt = getAmount(value.netAmt);
                        value.discount = getAmount(value.discount);
                        value.taxAmount = getAmount(value.taxAmount);
                        value.discountPercentage = getAmount(value.discountPercentage);
                        value.crNetAmount = getAmount(value.crNetAmount);
                        value.crAmount = getAmount(value.crAmount);
                        value.balanceAmount = getAmount(value.balanceAmount);
                        value.crTaxAmount = getAmount(value.crTaxAmount);
                        value.refundAmount = getAmount(value.refundAmount);



                        $scope.totalAmountSummary = parseFloat($scope.totalAmountSummary) + getAmount(value.grossAmount);
                        if (value.isDiscountApplicable == 'Y' && parseFloat(value.remainingRefundAmt) != 0) {
                            $scope.totalAmountSummaryDis = parseFloat($scope.totalAmountSummaryDis) + getAmount(value.remainingRefundAmt);
                        }
                        $scope.totalConcessionSummary = parseFloat($scope.totalConcessionSummary) + getAmount(value.concession);
                        $scope.totalPreviousDiscount = parseFloat($scope.totalPreviousDiscount) + getAmount(value.billDiscount);

                        $scope.netPayableSummary = parseFloat($scope.netPayableSummary) + getAmount(value.netAmt);
                        $scope.totalDiscountSummary = (parseFloat($scope.totalDiscountSummary) + getAmount(value.discount)).toFixed(2);
                        $scope.totalGstTaxSummary = parseFloat($scope.totalGstTaxSummary) + getAmount(value.taxAmount);
                        $scope.totaCrAmount = getAmount($scope.totaCrAmount) + getAmount(value.crAmount) + getAmount(value.crTaxAmount);
                        $scope.totaCrAmountGrid = getAmount($scope.totaCrAmountGrid) + getAmount(value.crAmount);
                        $scope.totaCrTaxAmount = getAmount($scope.totaCrTaxAmount) + getAmount(value.crTaxAmount);

                        $scope.totalCrAmountR = getAmount($scope.totalCrAmountR) + getAmount(value.crNetAmount) + getAmount(value.crTaxAmount);
                        $scope.totalRefundAmount = getAmount($scope.totalRefundAmount) + getAmount(value.refundAmount);
                        $scope.totalBalanceAmount = getAmount($scope.totalBalanceAmount) + getAmount(value.balanceAmount);

                        $scope.totalremainingRefundAmt = getAmount($scope.totalremainingRefundAmt) + getAmount(value.remainingRefundAmt);
                        console.log($scope.totalCrAmountR, '', getAmount(value.crNetAmount));
                    }
                  
                });

                $scope.totalCrAmountR = parseFloat($scope.totalCrAmountR).toFixed(2);
                $scope.totalRefundAmount = parseFloat($scope.totalRefundAmount).toFixed(2);
                $scope.totalBalanceAmount = parseFloat($scope.totalBalanceAmount).toFixed(2);
                $scope.totaCrAmount = parseFloat($scope.totaCrAmount).toFixed(2);
                $scope.signRoundOff = "";

                $scope.isBillWiseDiscountApplied = true;

                if (getAmount($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise) > 0 && $scope.discountRoundOff == 0 && $scope.isBillWiseDiscountApplied) {
                    $scope.discountRoundOff = parseFloat(getAmount($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise) - getAmount($scope.totalDiscountSummary)).toFixed(2);
                }

                if (Math.sign(getAmount($scope.discountRoundOff)) == 0 || Math.sign(getAmount($scope.discountRoundOff)) == 1) {
                    $scope.signRoundOff = "+(" + getAmount($scope.discountRoundOff).toFixed(2) + ")";
                    $scope.creditNoteMstObj.signRoundOff = 1;
                } else {
                    var tempDisocuntRoundOff = getAmount($scope.discountRoundOff);
                    $scope.signRoundOff = "-(" + Math.abs(tempDisocuntRoundOff).toFixed(2) + ")";
                    $scope.creditNoteMstObj.signRoundOff = 2;
                }


                if($scope.discountRoundOff < 0 && $scope.finalTotalCrAmount <  $scope.totalCrAmountR){
                	$scope.finalTotalCrAmount = getAmount($scope.totalCrAmountR) + getAmount($scope.discountRoundOff);
                }else{
                	$scope.finalTotalCrAmount = getAmount($scope.totalCrAmountR) + getAmount($scope.discountRoundOff);
                }
                
                $scope.finalTotalCrAmount = parseFloat($scope.finalTotalCrAmount).toFixed(2);
               
              
                
              
            	
                    if($scope.cunsumedServiceList != undefined && $scope.cunsumedServiceList != null && $scope.cunsumedServiceList.length > 0 ){
                    	 $rootScope.startLoader();
                    	var consumedServiceRateFromPakage = 0;
                    	 angular.forEach($scope.cunsumedServiceList, function(value, key) {
                    		 consumedServiceRateFromPakage =consumedServiceRateFromPakage + value.basePrice;
                    	 });
                    	 
                    	var count =0;
                    	 angular.forEach(inputList, function(value, key) {
                         	if(value.isSelected != null && value.isSelected ==  true && index!=undefined && index == key){
         	                	if(value.package==true && value.packageTypeId==5){
         	                		  var checkPackageAllServicesConsumeOrNot = true;
         	                		  if(value.packageId == $scope.listPackage.mPackageMasterDto.packageMasterId){
                                          angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,key){
                      	                        	if(val.balancePackageQuantity > 0 && val.serviceId != null){
                      	                        		checkPackageAllServicesConsumeOrNot = false;
                      	                        	}
                      	                        });       
                                     }else{
                                             checkPackageAllServicesConsumeOrNot = false;    
                                     }
         	                		
         	                		 if(checkPackageAllServicesConsumeOrNot == true){
         	                			count = 1;
         	                			
         	                			consumedServiceRateFromPakage = value.remainingRefundAmt;
         	                			
         	                			value.remainingRefundAmt = value.remainingRefundAmt - consumedServiceRateFromPakage;
                                        value.crAmount = value.crAmount - consumedServiceRateFromPakage;
                                        value.crNetAmount = value.crNetAmount - consumedServiceRateFromPakage;
                                        $scope.totaCrAmount = $scope.totaCrAmount - consumedServiceRateFromPakage;
                                        $scope.totaCrAmountGrid = $scope.totaCrAmountGrid - consumedServiceRateFromPakage;
                                        $scope.totalremainingRefundAmt = $scope.totalremainingRefundAmt - consumedServiceRateFromPakage;
                                        $scope.totalCrAmountR = $scope.totalCrAmountR - consumedServiceRateFromPakage;
                                        $scope.finalTotalCrAmount = $scope.finalTotalCrAmount - consumedServiceRateFromPakage;
         	                			
         	                         }
         	                		
         	                		if($scope.balanceAmountBill == 0 && checkPackageAllServicesConsumeOrNot ==false){
         	                			
         	                			if(consumedServiceRateFromPakage > value.remainingRefundAmt){
         	                				consumedServiceRateFromPakage = value.remainingRefundAmt;
         	                			}
         	                			
         	                			
                                     value.remainingRefundAmt = value.remainingRefundAmt - consumedServiceRateFromPakage;
                                     value.crAmount = value.crAmount - consumedServiceRateFromPakage;
                                     value.crNetAmount = value.crNetAmount - consumedServiceRateFromPakage;
                                     $scope.totaCrAmount = $scope.totaCrAmount - consumedServiceRateFromPakage;
                                     $scope.totaCrAmountGrid = $scope.totaCrAmountGrid - consumedServiceRateFromPakage;
                                     $scope.totalremainingRefundAmt = $scope.totalremainingRefundAmt - consumedServiceRateFromPakage; 
                                     $scope.totalCrAmountR = $scope.totalCrAmountR - consumedServiceRateFromPakage;
                                     $scope.finalTotalCrAmount = $scope.finalTotalCrAmount - consumedServiceRateFromPakage;

         	                		}
         	                		else if( $scope.balanceAmountBill > 0 && checkPackageAllServicesConsumeOrNot ==false) {
         	                			
         	                			var diff = 0 ;
         	                            if($scope.paidAmountBill > $scope.balanceAmountBill){
         	                            	
         	                            	 diff = $scope.paidAmountBill - $scope.balanceAmountBill;
         	                            	 
         	                            	if(diff > value.remainingRefundAmt){
             	                				consumedServiceRateFromPakage = diff;
             	                			}
         	                            	 
         	                            	 value.remainingRefundAmt = diff - consumedServiceRateFromPakage;
         	                            	 value.crAmount = $scope.paidAmountBill - consumedServiceRateFromPakage;
         	                            	 value.crNetAmount = $scope.paidAmountBill  - consumedServiceRateFromPakage;
         	                            	 $scope.totaCrAmount = $scope.paidAmountBill - consumedServiceRateFromPakage;
         	                            	 $scope.totaCrAmountGrid = $scope.totaCrAmount;
         			                		 $scope.totalremainingRefundAmt = $scope.paidAmountBill - consumedServiceRateFromPakage;
         			                		 $scope.totalCrAmountR = $scope.paidAmountBill - consumedServiceRateFromPakage;
         			                		 $scope.finalTotalCrAmount = $scope.paidAmountBill - consumedServiceRateFromPakage;
         			                		 
         	                            }else if($scope.paidAmountBill == 0 &&  $scope.balanceAmountBill == 0 ){
											
        	                            	 value.crAmount = 	value.remainingRefundAmt- consumedServiceRateFromPakage;
        	                            	 value.crNetAmount = value.remainingRefundAmt- consumedServiceRateFromPakage;
        	                            	 $scope.totaCrAmount = value.remainingRefundAmt- consumedServiceRateFromPakage;
        	                            	 $scope.totaCrAmountGrid = value.remainingRefundAmt- consumedServiceRateFromPakage;
        			                		 $scope.totalremainingRefundAmt = value.remainingRefundAmt- consumedServiceRateFromPakage;
        			                		 $scope.totalCrAmountR = value.remainingRefundAmt- consumedServiceRateFromPakage;
        			                		 $scope.finalTotalCrAmount = value.remainingRefundAmt- consumedServiceRateFromPakage;
											value.remainingRefundAmt = 	value.remainingRefundAmt- consumedServiceRateFromPakage;
										}
         	                			
         	                			
         	                		}else{
         	                			value.isSelected = false;
         	                		}
         	                	}
                         	}else 	if(value.isSelected != null && index!=undefined && index == key) {

         	                	if(value.package==true && value.packageTypeId==5){
         	                		if($scope.balanceAmountBill == 0){
         	                		
	         	                		var remainAmount = 0;
	         	                		if(value.remainingRefundAmt == 0){
	         	                			 angular.forEach(value.payeeRefundMppr,function(val1,key1){
	               	                        	if(val1.payeeId  == $scope.creditNoteMstObj.billPayeeId){
	               	                        		remainAmount = val1.payeeRefundAmount;
	               	                        	}
	               	                        });     
	         	                			 $scope.totalremainingRefundAmt = getAmount($scope.totalremainingRefundAmt) + getAmount(remainAmount); 
	         	                			$scope.totalAmountSummaryDis = getAmount($scope.totalAmountSummaryDis) + getAmount(remainAmount);
	         	                			 value.remainingRefundAmt = remainAmount;
	         	                		}else{
	         	                			  $scope.totalremainingRefundAmt = getAmount($scope.totalremainingRefundAmt) + getAmount(consumedServiceRateFromPakage);
	         	                			 $scope.totalAmountSummaryDis = getAmount($scope.totalAmountSummaryDis) + getAmount(consumedServiceRateFromPakage);
	         	                			 value.remainingRefundAmt = value.remainingRefundAmt + consumedServiceRateFromPakage;
	         	                		}
                                    
         	                		}
         	                		else if( $scope.balanceAmountBill > 0){
         	                			var diff = 0 ;
         	                            if($scope.paidAmountBill > $scope.balanceAmountBill){
         	                            	 diff = $scope.paidAmountBill + $scope.balanceAmountBill;
         	                            	
         	                            	var remainAmount = 0;
                 	                		if(value.remainingRefundAmt == 0){
                 	                			 angular.forEach(value.payeeRefundMppr,function(val1,key1){
                       	                        	if(val1.payeeId  == $scope.creditNoteMstObj.billPayeeId){
                       	                        		remainAmount = val1.payeeRefundAmount;
                       	                        	}
                       	                        });     
                 	                			 $scope.totalremainingRefundAmt = getAmount($scope.totalremainingRefundAmt) + getAmount(remainAmount);
                 	                			$scope.totalAmountSummaryDis = getAmount($scope.totalAmountSummaryDis) + getAmount(remainAmount);
                 	                			 value.remainingRefundAmt = remainAmount;
                 	                		}else{
                 	                			  $scope.totalremainingRefundAmt = getAmount($scope.totalremainingRefundAmt) + getAmount(consumedServiceRateFromPakage);
                 	                			 $scope.totalAmountSummaryDis = getAmount($scope.totalAmountSummaryDis) + getAmount(consumedServiceRateFromPakage);
                 	                			 value.remainingRefundAmt = diff + consumedServiceRateFromPakage;
                 	                		}
         	                            	 
         	                           }else if($scope.paidAmountBill == 0 &&  $scope.balanceAmountBill == 0 ){
         	                        	   
       	                            	  $scope.totalremainingRefundAmt = getAmount(value.remainingRefundAmt) + getAmount(consumedServiceRateFromPakage);
           	                			 $scope.totalAmountSummaryDis = getAmount(value.remainingRefundAmt) + getAmount(consumedServiceRateFromPakage);
           	                			 value.remainingRefundAmt = value.remainingRefundAmt + consumedServiceRateFromPakage;
      	                            }
         	                		}
         	                	}
                         	
                         	}
                         });
                    	 
                    	  if(count == 1){
                         	 growl.warning("In this package all services are consumed", {
                                  title: WARNING_MSG
                              });
                         	$rootScope.stopLoader();
                         	 return  false ;
                         }
                        
                    	 
                    	 $rootScope.stopLoader();
                    }
                    $rootScope.stopLoader();
                
            }


            $scope.calculateTotalAmountAgainstService = function(index) {
            	
            	$rootScope.startLoader();
            	var checkPackageExitsOrNot = false;
            	angular.forEach($scope.billDetailsListClone,function(v,k){
                	if(v.isPackageService != 'Y' && 
                			v.refundAmount==0 && 
                			v.isSelected !=null &&
                			((v.serviceRendered!=1 && v.package==false) || (v.package==true && v.packageTypeId==5))  && 
                			k == index && 
                			(v.package==false || ((v.package==true && v.packageTypeId==5)) &&(v.netAmt==v.selfPayable||v.netAmt==v.creditPayable))
                			 && v.isSelected ==true){
                		
                			checkPackageExitsOrNot = true;
                		  $scope.getListPackageOrder(index);
                		  
                	}
                });
                var check = false;
                angular.forEach($scope.billDetailsListClone, function(value, key) {
                    
                    if(value.discountPercentage != undefined && value.discountPercentage != null && value.discountPercentage > 0){
                        check = true;
                    }
                    
                });
                if (check == true) {
                	
                	 angular.forEach($scope.billDetailsListClone, function(value, key) {
                         
                		 value.isSelected =false;
                         
                     });
                	
                    growl.warning("Can not select service because discount is applied on credit note ", {
                        title: WARNING_MSG
                    });
                    $rootScope.stopLoader();
                    return false;
                    
                }else{
                	
                		if(checkPackageExitsOrNot == false){
                			$rootScope.startLoader();
                			$scope.calculateTotalAmount($scope.billDetailsListClone,index);
                           
                		}
                		
                	
                }
            	
               
            };




            $scope.creditNoteWiseDiscountApply = function() {

                if ($scope.billMstId == null || $scope.billMstId == '') {
                    growl.warning("Please select bill.", {
                        title: WARNING_MSG
                    });
                    $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                    $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                    return false;
                }
                if ($scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise == '' && $scope.isBillWiseDiscountApplied) {
                    growl.warning("Please apply discount first.", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                if (parseFloat($scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise) > 100) {
                    growl.warning("Percentage Should be less than 100%.", {
                        title: WARNING_MSG
                    });
                    $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                    $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                    return false;
                }
                if ($scope.creditNoteMstObj.billPayeeId == null || $scope.creditNoteMstObj.billPayeeId == "") {
                    growl.warning("Please select payer.", {
                        title: WARNING_MSG
                    });
                    return false;
                } else if ($scope.creditNoteWiseDiscout.discountCategoryId == '') {
                    growl.warning("Discount Category is required.", {
                        title: WARNING_MSG
                    });
                    return false;
                } else {

                    var discountApplicableCount = $scope.billDetailsListClone.filter(obj => obj.isDiscountApplicable == 'Y');
                    discountApplicableCount = discountApplicableCount == 0 ? 0 : discountApplicableCount.length;
                    if (discountApplicableCount == 0) {
                        growl.warning("No service is applicable for discount...!!", {
                            title: WARNING_MSG
                        });
                        return;
                    }
                    
                    var payeeAmount = parseFloat(0).toFixed(2);
                    angular.forEach($scope.billDetailsListClone, function(value, key) {
                    	
                    	if(value.isDiscountApplicable == 'Y'){
                    		
                    		 angular.forEach(value.payeeRefundMppr, function(value1, key1) {
                             	
                             	if(value1.payeeId == $scope.creditNoteMstObj.billPayeeId ){
                             		payeeAmount = parseFloat(parseFloat(payeeAmount) + parseFloat(value1.payeeRefundAmount)).toFixed(2);
                             	}
                             	
                             });
                    		
                    	}
                    	
                    });
                    
                    if (parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise) > parseFloat(payeeAmount)) {
                        growl.warning("Discount should not be more than payer amount " + payeeAmount, {
                            title: WARNING_MSG
                        });
                        return;
                    }

                    var check = false;
                    angular.forEach($scope.billDetailsListClone, function(value, key) {
                    	
                    	if(value.isSelected != null && value.isSelected == true){
                    		check = true;
                    	}
                    	
                    });
                    if (check == true) {
                        growl.warning("Can not apply discount for selected servicewise credit note ", {
                            title: WARNING_MSG
                        });
                        $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                        $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                        return false;
                    }
                    
                    var totalRemainingAmount = 0;
                    angular.forEach($scope.billDetailsListClone, function(value, key) {
                        if (value.isDiscountApplicable == 'Y' && parseFloat(value.remainingRefundAmt) != 0) {
                            totalRemainingAmount = totalRemainingAmount + parseFloat(value.remainingRefundAmt);
                        }
                    })

                    var userRigth = false;
                    
                        $scope.billWiseDiscountObj.requestAmountBillWise = 0;
                        $scope.billWiseDiscountObj.requestUserId = "";
                        $scope.requestDiscountDiv = false;
                        $scope.requestDiscountFlag = false;
                        var discountPer = $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise;
                        var discount = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
                        var remainingRefundAmount= parseFloat(0);
                        angular.forEach($scope.billDetailsListClone, function(value, key) {
                        	
                        	if(value.isSelected != null && value.isSelected == true){
                        		remainingRefundAmount = remainingRefundAmount + parseFloat(value.remainingRefundAmt);
                        	}
                        	
                        });
                        
                        var remainingRefundAmountsub = parseFloat($scope.totalAmountSummaryDis) - remainingRefundAmount;
                        
                        if (remainingRefundAmountsub > 0 && discount > 0) {
                            var discountPercentageF = Number(parseFloat(((discount * 100) / (remainingRefundAmountsub))));
                        }
                       
                            $scope.isBillWiseDiscountApplied = true;
                            angular.forEach($scope.billDetailsListClone, function(value, key) {
                                if (value.isDiscountApplicable == 'Y' && parseFloat(value.remainingRefundAmt) != 0) {
                                    value.discountCategoryId = $scope.creditNoteWiseDiscout.discountCategoryId;
                                    value.discountDetailsList = [];
                                    value.discountPercentage = (discountPercentageF == "" || discountPercentageF == undefined) ? 0.00 : discountPercentageF;
                                    value.discount = 0;
                                }
                            });
                        }
                        $scope.calculateTotalAmount($scope.billDetailsListClone);
                        
                        angular.forEach($scope.billDetailsListClone, function(value, key) {
                        	
                        	if(value.isDiscountApplicable == 'Y' && (value.isSelected == null || value.isSelected == false)){
                        		 $scope.calculateShareAmount(value,key);
                        	}
                        	
                        });
                        
                       
                    }


            
            $scope.calculateShareAmount = function(item,$index){
            	
            	var paymentEntitlementId = 1;
            	 angular.forEach($scope.encounterDetails.patientSponsorDetailsDtosList, function(value, key) {
            		 if(value.priorityId == 1 ){
            			 paymentEntitlementId = value.paymentEntitlementId;
            			 
            		 }
            	 });
            	
            	
		        var data = {
		            "visitTypeId" :  $scope.visitTypeId,
		            "billingBedCategoryId" : $scope.visitTypeId  == 1 ? $scope.opdBillingCatId : $scope.encounterDetails.bedBillingCategoryId,
		            "patientTypeId" : $scope.visitTypeId  == 1 ? $scope.encounterDetails.patientSponsorDetailsDtosList[0].patientTypeId :$scope.encounterDetails.patientCategoryId,
		            "paymentEntitlementTypeId" : paymentEntitlementId,//$scope.visitTypeId  == 1 ? $scope.encounterDetails.patientSponsorDetailsDtosList[0].paymentEntitlementId : $scope.encounterDetails.paymentEntitlementId,
		            "referralId" : $scope.encounterDetails.referralId,
		            listOrderDetailsMasterDto : []
		        };
		        var newArr = {
		                ordDiscount:item.crAmount,
		                ordConcession : item.concession,
		                ordDocId : item.doctorId,
		                ordDocSplId : item.doctorSpecialityId,
		                orderDetailsId : item.orderDetailsId,
		                orgId : $scope.organizationId,
		                orgUnitId : $scope.unitId,
		                serviceId : item.serviceMasterId,
		                departmentId : item.specialityId,
		                ordTotalAmt : ((parseFloat(item.selfPayable) + parseFloat(item.creditPayable)) - parseFloat(item.refundAmount))  - ((parseFloat(item.crAmount) + parseFloat(item.taxAmount))),
		                tariffId : item.tariffId,
		                isDocReq : item.isDoctorRequired,
		                isRefDoctorShare:item.isRefDoctorShare ,
		                discountOwnership: parseInt(item.discountOwnership),
		                orderDate : item.orderDate,
		                
		        };
		        
		        if($scope.visitTypeId == 1){
		        	newArr.admissionId = $scope.admissionId;
		        }else{
		        	newArr.encounterId = $scope.admissionId;
		        }
		        
		        var orderDetailsList = [];
		        orderDetailsList.push(newArr);
		        data.listOrderDetailsMasterDto = orderDetailsList;
		        
		        $rootScope.startLoader();
		        var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare/forCreditNote";
		        
		        BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		            if(response.data.status == "success"){
		                $scope.billDetailsListClone[$index].docSharePer = (response.data.object.listOrderDetailsMasterDto[0].docSharePer !=null &&  response.data.object.listOrderDetailsMasterDto[0].docSharePer > 0) ?response.data.object.listOrderDetailsMasterDto[0].docSharePer: 0;
		                $scope.billDetailsListClone[$index].hospitalShare = (response.data.object.listOrderDetailsMasterDto[0].hospitalShare  !=null && response.data.object.listOrderDetailsMasterDto[0].hospitalShare >0) ?response.data.object.listOrderDetailsMasterDto[0].hospitalShare:0;
		                $scope.billDetailsListClone[$index].refDocSharePer = (response.data.object.listOrderDetailsMasterDto[0].refDocSharePer  !=null && response.data.object.listOrderDetailsMasterDto[0].refDocSharePer >0 )? response.data.object.listOrderDetailsMasterDto[0].refDocSharePer : 0;
		                $scope.billDetailsListClone[$index].discountOwnership = (response.data.object.listOrderDetailsMasterDto[0].discountOwnership>0 && ($scope.billDetailsListClone[$index].concession != 0 || $scope.billDetailsListClone[$index].discount != 0)) > 0 ? response.data.object.listOrderDetailsMasterDto[0].discountOwnership : $scope.defaultDiscountOwnership;
		                //$rootScope.stopLoader();
		                
		                if(response.data.resObject == true){
		                	  growl.warning("Doctor share has already been processed", {
		                          title: WARNING_MSG
		                      });
		                }
		            }else{
		                $rootScope.stopLoader();
		            }
		        });
		    }
            

            $scope.saveDiscountRequest = function() {


                if ($scope.billMstId == null || $scope.billMstId == '') {
                    growl.warning("Please select bill.", {
                        title: WARNING_MSG
                    });
                    $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                    $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                    return false;
                }

                if ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '') {
                    growl.warning("Please select a user...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                var netAmt = $scope.billDetailsListClone.reduce(function(prev, cur) {
                    return prev + cur.netAmt;
                }, 0);

                var date = moment().format("DD-MM-YYYY HH:mm:ss");
                $rootScope.startLoader();
                var requestDistCountList = $scope.billDetailsListClone;
                var requestDiscountObject = {};
                var requestDiscountDetailsList = [];

                requestDiscountObject.organizationId = $scope.organizationId;
                requestDiscountObject.unitId = $scope.unitId;
                requestDiscountObject.visitTypeId = $scope.visitTypeId;
                requestDiscountObject.encounterId = $scope.encounterId;
                requestDiscountObject.admissionId = $scope.admissionId;
                requestDiscountObject.payeeId = $scope.creditNoteMstObj.billPayeeId;
                requestDiscountObject.patientId = $scope.creditNoteMstObj.patientId;
                requestDiscountObject.netAmount = netAmt;
                requestDiscountObject.reqDisAmt = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
                requestDiscountObject.approvedDiscAmt = 0;
                requestDiscountObject.requestSentBy = $scope.userId;
                requestDiscountObject.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
                requestDiscountObject.createdBy = $scope.userId;
                requestDiscountObject.updatedBy = $scope.userId;
                requestDiscountObject.reqStatus = "1";
                requestDiscountObject.discType = $scope.creditNoteWiseDiscout.discountCategoryId;
                requestDiscountObject.requestType = "6";
                requestDiscountObject.billId =$scope.billMstId;
                requestDiscountObject.createdDate = date;
                requestDiscountObject.requestDate = date;

                var discountPer = $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise;
                var discount = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;

                var result = $scope.billDetailsListClone.filter(obj => obj.isDiscountApplicable == 'Y');
                var devide = result == 0 ? 1 : result.length;
                if ($scope.totalAmountSummaryDis > 0 && discount > 0) {
                    var discountPercentageF = Number(parseFloat(((discount * 100) / ($scope.totalAmountSummaryDis))).toFixed(2));
                }

                angular.forEach(requestDistCountList, function(value, key) {
                    if (value.isDiscountApplicable == 'Y' && value.discount > 0) {
                        value.discountDetailsList = [];
                        if ($scope.isBillWiseDiscountApplied) {
                            value.discountPercentage = (discountPercentageF == "" || discountPercentageF == undefined) ? 0.00 : discountPercentageF;
                            value.discount = getAmount((Number(value.remainingRefundAmt) * Number(value.discountPercentage)) / 100);
                        }
                        var cNDetails = angular.copy($scope.creditNoteDiscountDetails);
                        cNDetails.discountCategoryId = $scope.creditNoteWiseDiscout.discountCategoryId;
                        cNDetails.discountPercentage = value.discountPercentage;
                        cNDetails.discountAmount = value.discount;
                        cNDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy;

                        var detailsObj = {};
                        detailsObj.organizationId = $scope.organizationId;
                        detailsObj.unitId = $scope.unitId;
                        detailsObj.orderId = value.orderId;
                        detailsObj.orderDetailsId = value.orderDetailsId;
                        detailsObj.serviceId = value.serviceMasterId;
                        detailsObj.netAmount = value.netAmt;
                        detailsObj.reqDisAmt = value.discount;
                        detailsObj.approvedDiscAmt = 0;
                        detailsObj.createdBy = $scope.userId;
                        detailsObj.createdDate = date;
                        requestDiscountDetailsList.push(detailsObj);
                    }
                });

                requestDiscountObject.requestDiscountDetailsDtosList = requestDiscountDetailsList;

                var URI = BASE_URL + ROOT_URL + "/api/billing/requestDiscount";
                BillingGenericService.serviceAction(METHOD_POST, URI, requestDiscountObject,
                        NOTIFICATION_MSG_STATUS_FALSE)
                    .then(function(response) {
                        console.log(response);
                        if (response.data.status == "success") {
                            $rootScope.stopLoader();
                            growl.success(
                                "Discount request sent sucessfully...!!", {
                                    title: 'Success!'
                                });
                            $scope.requestDiscountFlag = true;
                            $scope.discountDisabledFlagForRequest = true;
                            $scope.requestDiscountFlag = false;
                            $scope.requestDiscountDiv = false;
                            $scope.totalCrAmountR = 0;
                            $scope.finalTotalCrAmount = 0;
                            $scope.init();
                            $scope.billDetailsListClone = [];
                            $scope.patientDetails = {};
                            $scope.billDetailsList = [];
                            $scope.billList = [];
                            $scope.initCreditNotMastObj();
                            $scope.initCreditNoteDetailsObj();
                        } else if (response.data.status == "error") {
                            $rootScope.stopLoader();
                        }

                    });
            }



            /** Group Wise Discount start======================================*/

            $scope.specialityMasterListForGroup = [];
            //Check Product Wise Category
            var isGroupExists;

            function checkGroupExistsOrNot(arr, val) {
                return arr
                    .some(function(arr) {
                        return (val.specialityId === arr.specialityId);
                    });
            }


            $scope.groupWiseDiscountPopup = function() {

                //		if($scope.againstServiceOrDeposit == 2)
                //			{
                $scope.groupWiseServicesSelectedList.services = [];
                $scope.serachGroupWiseServicesClear();
                $('#groupWiseDiscountModalCN').modal('show');
                $scope.totalGroupWiseAmount = 0;
                $scope.totalGroupWiseDiscount = 0;
                $scope.chkFlgGroup = 'N';

                console.log($scope.billDetailsListClone);
                angular.forEach($scope.billDetailsListClone, function(value, key) {

                    var temp = {
                        specialityId: value.specialityId,
                        specialityName: value.specialityName
                    };
                    if ($scope.specialityMasterListForGroup.length > 0) {
                        isGroupExists = checkGroupExistsOrNot($scope.specialityMasterListForGroup, temp)
                        if (!isGroupExists) {
                            $scope.specialityMasterListForGroup.push(temp);
                        }
                    } else {
                        $scope.specialityMasterListForGroup.push(temp);
                    }

                });
                //			}
                //		else{
                //			growl.warning("Please select against discount.",{
                //				title : WARNING_MSG
                //			});
                //		}

            }



            $scope.checkAllService = function() {
                if ($scope.selectedAllService) {
                    $scope.selectedAllService = true;
                } else {
                    $scope.selectedAllService = false;
                }
                angular
                    .forEach(
                        $scope.billDetailsListClone,
                        function(billDetailsObj) {
                            billDetailsObj.isSelected = $scope.selectedAll;
                        });
            }

            $scope.checkAllServiceMain = function() {
                if ($scope.selectedAllService) {
                    $scope.selectedAllService = true;
                } else {
                    $scope.selectedAllService = false;
                }
                angular
                    .forEach(
                        $scope.billDetailsListClone,
                        function(billDetailsObj) {
                            if (billDetailsObj.isPackageService != 'Y' && billDetailsObj.refundAmount == 0 && billDetailsObj.serviceRendered == 0) {
                                billDetailsObj.isSelected = $scope.selectedAllService;
                            }

                        });
                $scope.calculateTotalAmountAgainstService();
            }



            $scope.checkAll = function() {
                if ($scope.selectedAll) {
                    $scope.selectedAll = true;
                } else {
                    $scope.selectedAll = false;
                }
                angular
                    .forEach(
                        $scope.groupWiseDiscountServicesList,
                        function(billDetailsObj) {
                            billDetailsObj.isSelected = $scope.selectedAll;
                        });
            }

            $scope.calculatGroupWisePer = function() {
                var discountPer = 0.00;
                var discount = $scope.serviceWiseDiscountObj.serviceDiscountAmount;
                $scope.serviceWiseDiscountObj.serviceDiscountPer = 0.00;

                $scope.totalGroupWiseAmount = 0;
                $scope.totalGroupWiseDiscount = 0;

                angular.forEach($scope.groupWiseDiscountServicesList, function(value, key) {
                    $scope.totalGroupWiseAmount = getAmount($scope.totalGroupWiseAmount) + getAmount(value.balanceAmount);
                });
                if (discountPer == 0.00 && discount > 0.00) {
                    discountPer = (discount * 100) / ($scope.totalGroupWiseAmount);
                    $scope.serviceWiseDiscountObj.serviceDiscountPer = getAmount(discountPer);
                }


            }

            $scope.calucateGroupWiseAmount = function() {
                var discountPer = $scope.serviceWiseDiscountObj.serviceDiscountPer;
                $scope.serviceWiseDiscountObj.serviceDiscountAmount = 0.00;


                var discount = 0.00;
                $scope.totalGroupWiseAmount = 0;
                $scope.totalGroupWiseDiscount = 0;

                angular.forEach($scope.groupWiseDiscountServicesList, function(value, key) {
                    $scope.totalGroupWiseAmount = getAmount($scope.totalGroupWiseAmount) + getAmount(value.balanceAmount);
                });
                if (discount == 0.00 && discountPer > 0.00) {
                    discount = ($scope.totalGroupWiseAmount) * (discountPer / 100);
                    $scope.serviceWiseDiscountObj.serviceDiscountAmount = getAmount(discount);
                }
            }




            $scope.saveGroupDiscountToFinalList = function() {
                angular.forEach($scope.groupWiseDiscountServicesList, function(groupObj, key) {
                    angular.forEach($scope.billDetailsListClone, function(cloneObj, key) {
                        if (cloneObj.serviceMasterId == groupObj.serviceMasterId) {
                            cloneObj.discountPercentage = groupObj.discountPercentage;
                            cloneObj.discount = getAmount(groupObj.discount);
                            cloneObj.discountDetailsList = groupObj.discountDetailsList;
                        }
                    });
                });
                $scope.calculateTotalAmount($scope.billDetailsListClone);
            }

            /** Group Wise Discount end======================================*/


            /**bill Wise End ==================================================*/

            /**Service Wise==================================================*/

            $scope.initServiceDiscountObj = function() {
                $scope.serviceWiseDiscountObj = {
                    "serviceDiscountPer": "",
                    "serviceDiscountAmount": "",
                    "serviceDiscoutCategoryId": "",
                    "serviceDiscountGiveyBy": ""
                }
            }
            $scope.initServiceDiscountObj();




            $scope.serviceObj = {};
            $scope.serviceViseDiscountPopup = function(index, serviceObj) {
                $scope.serviceObj = serviceObj;
                $scope.serviceIndex = index;
                $scope.discountDetailsList = [];
                $scope.serciveNamePop = $scope.serviceObj.serviceStandardName;
                $scope.totalAmtPopup = $scope.serviceObj.grossAmount;
                //$scope.concession = $scope.serviceObj.concession==null?0.00:$scope.serviceObj.concession;
                if (serviceObj.discountDetailsList.length > 0) {
                    $scope.discountDetailsList = serviceObj.discountDetailsList;

                    $scope.totalDiscountAmtPopup = getAmount(serviceObj.discount);
                    $scope.totalDiscountPer = getAmount(serviceObj.discountPercentage);
                    //check
                    $scope.grossAmount = getAmount(serviceObj.grossAmount);
                } else {
                    $scope.totalDiscountAmtPopup = parseFloat(0).toFixed(2);
                    $scope.totalDiscountPer = parseFloat(0).toFixed(2);
                    $scope.grossAmount = $scope.serviceObj.grossAmount;
                }



            }


            $scope.calculatServiceWisePer = function() {
                var discountPer = 0.00;
                var discount = $scope.serviceWiseDiscountObj.serviceDiscountAmount;
                $scope.serviceWiseDiscountObj.serviceDiscountPer = 0.00;
                if (discountPer == 0.00 && discount > 0.00) {
                    /*discountPer = (($scope.netAmt-$scope.concession)*100)/(discount);*/
                    discountPer = (($scope.grossAmount) * 100) / (discount);
                    $scope.serviceWiseDiscountObj.serviceDiscountPer = getAmount(discountPer);
                }
            }

            $scope.calucateServiceWiseAmount = function() {
                if (parseFloat($scope.serviceWiseDiscountObj.serviceDiscountPer) > 100) {

                    growl.warning("Percentage cannot be more than 100%.", {
                        title: WARNING_MSG
                    });
                    $scope.serviceWiseDiscountObj.serviceDiscountPer = 0;
                    $scope.serviceWiseDiscountObj.serviceDiscountAmount = 0;
                    return false;
                } else {
                    var discountPer = $scope.serviceWiseDiscountObj.serviceDiscountPer;
                    var discount = 0.00;
                    $scope.serviceWiseDiscountObj.serviceDiscountAmount = 0.00;
                    if (discount == 0.00 && discountPer > 0.00) {
                        discount = ($scope.grossAmount) * (discountPer / 100);
                        $scope.serviceWiseDiscountObj.serviceDiscountAmount = getAmount(discount);
                    }
                }
            }


            $scope.addServiceViseDiscont = function() {
                var discountPer = $scope.serviceWiseDiscountObj.serviceDiscountPer;
                var discount = $scope.serviceWiseDiscountObj.serviceDiscountAmount;

                $scope.discountCategoryList.filter

                $scope.headerObj = $scope.discountCategoryList
                    .filter(function(discoutObj) {
                        if (discoutObj.id == $scope.serviceWiseDiscountObj.serviceDiscoutCategoryId) {
                            return discoutObj;
                        }
                    });

                var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
                creditNoteDiscountDetails.discountCategoryId = $scope.serviceWiseDiscountObj.serviceDiscoutCategoryId;
                creditNoteDiscountDetails.discountPercentage = discountPer;
                creditNoteDiscountDetails.discountAmount = discount;
                creditNoteDiscountDetails.discountGivenBy = $scope.serviceWiseDiscountObj.serviceDiscountGiveyBy;
                creditNoteDiscountDetails.discountCategory = $scope.headerObj[0].type;
                $scope.discountDetailsList.push(creditNoteDiscountDetails);

                $scope.totalDiscountAmtPopup = getAmount($scope.totalDiscountAmtPopup) + getAmount(discount);
                $scope.totalDiscountPer = getAmount($scope.totalDiscountPer) + getAmount(discountPer);

                $scope.billDetailsListClone[$scope.serviceIndex].discountDisabledFlag = 'Y';
                $scope.initServiceDiscountObj();
            }
            $scope.saveServiceWiseDiscount = function() {
                $scope.billDetailsListClone[$scope.serviceIndex].discountDetailsList = [];
                $scope.billDetailsListClone[$scope.serviceIndex].discountPercentage = 0.00;
                $scope.billDetailsListClone[$scope.serviceIndex].discount = 0.00;

                $scope.billDetailsListClone[$scope.serviceIndex].discountDetailsList = $scope.discountDetailsList;
                $scope.billDetailsListClone[$scope.serviceIndex].discountPercentage = getAmount($scope.totalDiscountPer);
                $scope.billDetailsListClone[$scope.serviceIndex].discount = getAmount($scope.totalDiscountAmtPopup);
                $scope.calculateTotalAmount($scope.billDetailsListClone);
            }
            $scope.getRemoveDiscount = function(Index) {
                $scope.discountDetailsList.splice(Index, 1);
                angular.forEach($scope.discountDetailsList, function(value, key) {
                    $scope.totalDiscountAmtPopup = 0.00;
                    $scope.totalDiscountPer = 0.00;
                    $scope.totalDiscountAmtPopup = getAmount($scope.totalDiscountAmtPopup) + getAmount(value.discountAmount);
                    $scope.totalDiscountPer = getAmount($scope.totalDiscountPer) + getAmount(value.discountPercentage);
                });
                if ($scope.discountDetailsList.length == 0) {
                    $scope.billDetailsListClone[$scope.serviceIndex].discountDisabledFlag = 'N';
                }

            }


            /**==================================================*/


            $scope.selectedIndex = 0;
            $scope.getDiscountPerCal = function(itemObj, idx) {
                $scope.selectedIndex = idx;
                $scope.isBillWiseDiscountApplied = false;
                $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountCategoryId == '';
                if ($scope.requestStatusObject == null) {
                    $scope.requestDiscountFlag = false;
                    $scope.requestDiscountDiv = false;
                } else {
                    $scope.getCancelDiscountReqForDiscount();
                }


                $scope.billDetailsListClone[idx].discountPercentage = 0.00;

                $scope.calculateTotalAmount($scope.billDetailsListClone);
                $scope.calculateShareAmount($scope.billDetailsListClone[idx],idx);
            }

            $scope.selectedIndex = 0;
            $scope.getDiscountCal = function(itemObj, idx) {
                $scope.selectedIndex = idx;
                $scope.isBillWiseDiscountApplied = false;
                $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountCategoryId == '';
                if ($scope.requestStatusObject == null) {
                    $scope.requestDiscountFlag = false;
                    $scope.requestDiscountDiv = false;
                } else {
                    $scope.getCancelDiscountReqForDiscount();
                }

                $scope.billDetailsListClone[idx].discount = 0.00;
                // var discount = (getAmount(itemObj.grossAmount))*(itemObj.discountPercentage/100);
                // $scope.billDetailsListClone[idx].discount = getAmount(discount);
                $scope.calculateTotalAmount($scope.billDetailsListClone);
                
                $scope.calculateShareAmount($scope.billDetailsListClone[idx],idx);
                
            }


            $scope.getCancelDiscountReqForDiscount = function() {


                $rootScope.startLoaderPopup();
                var data = {
                    reqDiscId: $scope.requestStatusObject.reqDiscId,
                }

                var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";

                GenericService
                    .serviceAction("POST", URI, data)
                    .then(function(response) {
                        $rootScope.stopLoaderPopup();
                        if (response.data.status == "success") {

                            $scope.requestApporivedDiscountDiv = false;
                            $scope.discountDisabledFlagForRequest = false;
                            $scope.requestAppUser = 0;
                            $scope.approvedAmtApp = 0;
                            $scope.requestAmtApp = 0;
                            $scope.statusApp = "";
                            $scope.discountRadio = 2;
                            $scope.requestStatusObject = null;
                            $scope.getCreditNoteRightByUser();
                        }
                    });

            }

            $scope.getFormattedDate = function(date, isTime) {
                var tempDate = new Date(date);
                var date = tempDate.getDate().toString().length == 1 ? "0" + tempDate.getDate().toString() : tempDate.getDate().toString();
                var month = (tempDate.getMonth() + 1).toString().length == 1 ? "0" + (tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
                var year = tempDate.getFullYear();
                if (isTime)
                    return date + "-" + month + "-" + year + " " + tempDate.getHours() + ":" + tempDate.getMinutes() + ":" + getSeconds();
                else
                    return date + "-" + month + "-" + year + " 00:00:00";
            }

            $scope.getCancelDiscountReq = function() {
                $rootScope.startLoaderPopup();
                var data = {
                    reqDiscId: $scope.requestStatusObject.reqDiscId,
                }

                var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";

                BillingGenericService
                    .serviceAction("POST", URI, data)
                    .then(function(response) {
                        $rootScope.stopLoaderPopup();
                        if (response.data.status == "success") {

                            growl.success("Cancelled !!!!", {
                                title: 'success'
                            });
                            $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                            $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                            $scope.requestDiscountFlag = false;
                            $scope.requestDiscountDiv = false;
                            $scope.calculateTotalAmount($scope.billDetailsListClone);
                            $scope.requestApporivedDiscountDiv = false;
                            $scope.discountDisabledFlagForRequest = false;
                            $scope.requestAppUser = 0;
                            $scope.approvedAmtApp = 0;
                            $scope.requestAmtApp = 0;
                            $scope.statusApp = "";
                            $scope.discountRadio = 2;
                            $scope.requestStatusObject = null;
                            $scope.cancelDiscount();
                            angular.element('#discountCancellationModalReq').modal('hide');
                            $scope.getCreditNoteRightByUser();
                        }
                    });

            }

            $scope.showCreditAmount = function() {

                var totalRemainingAmount = $scope.billDetailsListClone[$scope.selectedIndex].remainingRefundAmt;
                var DisPer = $scope.billDetailsListClone[$scope.selectedIndex].discountPercentage;
                var userRigth = false;
                var checkPer = false;
                var serviceCancelCount = 0;
                var checkPackageExitsOrNot = false;
                angular.forEach($scope.billDetailsListClone, function(value, key) {
                    if (value.discountPercentage != null && value.discountPercentage != 0 || value.isSelected ) {
                        checkPer = true;
                    }
                    if(value.isSelected){
                     serviceCancelCount++;
                    }
                    if(value.isPackageService != 'Y' && 
                    		value.refundAmount==0 && 
                    		value.serviceRendered!=1  && 
                			(value.package==false || ((value.package==true && value.packageTypeId==5)) )
                			 && value.isSelected ==true){
                    				checkPackageExitsOrNot = true;
                				}
                    
                });

                if (checkPer == false) {
                    growl.warning("Discount should be greater than zero..!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                 if ($scope.userDiscountObject != null ||serviceCancelCount>0) {
                    userRigth = true;
                }



                if ($scope.patientDetails.patientId == "" || $scope.patientDetails.patientId == null) {
                    growl.warning("Please select patient...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                if ($scope.billMstId == "" || $scope.billMstId == null) {
                    growl.warning("Please select bill...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                if ($scope.creditNoteMstObj.billPayeeId == "" || $scope.creditNoteMstObj.billPayeeId == null) {
                    growl.warning("Please select payer...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                if ($scope.requestDiscountDiv) {
                    growl.warning("Please send discount request...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                if (($scope.totaCrAmount == 0 || $scope.totaCrAmount == null || $scope.totaCrAmount == undefined) && checkPackageExitsOrNot == false) {
                    growl.warning("Credit note amount should be greater than zero...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                }
                if ($scope.creditNoteMstObj.crReasonId == '' || $scope.creditNoteMstObj.crReasonId == null) {
                    growl.warning("Please select credit note reason...!!", {
                        title: WARNING_MSG
                    });
                    return false;
                } else {


                        if ($scope.creditNoteApproval && $scope.finalTotalCrAmount>=$scope.fromCreditNoteAmount && $scope.finalTotalCrAmount<=$scope.toCreditNoteAmount) {
                            $scope.creditNoteMstObj.isApproved = 'Y';
                            $scope.creditNoteMstObj.isCreditNoteApproval = true;
                            $('#creditNoteSave').modal('show');
                        } else {

                            $scope.getListOfUser();
                            $scope.creditNoteMstObj.isApproved = 'N';
                            $scope.creditNoteMstObj.isCreditNoteApproval = false;
                            userRigth = false;
                        }


                    if (userRigth == true && $scope.finalTotalCrAmount > 0) {

                        $scope.creditNoteMstObj.isApproved = 'Y';
                        $scope.creditNoteMstObj.isCreditNoteApproval = true;
                        $('#creditNoteSave').modal('show');
                    }

                }
            }

            $scope.saveBtnDis = true;
            $scope.saveCreditNote = function() {
                $scope.saveBtnDis = false;
                $('#creditNoteSave').modal('hide');
                $('#creditNoteRequest').modal('hide');
                if ($scope.creditNoteMstObj.isApproved == 'N' && $scope.creditNoteMstObj.requestSentTo == '') {
                    growl.warning("Please select user.", {
                        title: WARNING_MSG
                    });
                    $scope.saveBtnDis = true;
                    $scope.checkDoubSave = false
                    return false;
                } else {
                    $scope.saveBtnDis = false;
                    $scope.saveCreditNoteFinal();
                }
            }



            $scope.setTabValue = function(againstServiceOrDeposit) {
                $scope.isBillWiseDiscountApplied = false;
                $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountCategoryId == '';
                if ($scope.requestStatusObject == null) {
                    $scope.requestDiscountFlag = false;
                    $scope.requestDiscountDiv = false;
                }


                $scope.againstServiceOrDeposit = againstServiceOrDeposit;
                $scope.calculateTotalAmount($scope.billDetailsListClone);
            }


            $scope.calculatePayerRefundableAmount = function() {

                $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = 0;
                $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
                angular.forEach($scope.billDetailsListClone, function(billDetails, key) {
                    billDetails.remainingRefundAmt = 0;
                    billDetails.discountPercentage = 0;
                    billDetails.discount = 0;
                    angular.forEach(billDetails.payeeRefundMppr, function(payee, key) {
                        if (payee.payeeId == $scope.creditNoteMstObj.billPayeeId)
                            {
                                if(payee.associateCompanyId == $scope.creditNoteMstObj.associateCompanyId){
                                     billDetails.remainingRefundAmt = billDetails.remainingRefundAmt + payee.payeeRefundAmount;
                                }
                            }
                    });
                });

               if ($scope.payeeList != null && $scope.payeeList.length > 0) {
                            angular.forEach($scope.payeeList,function(value, key) {
                                if ($scope.selfPayeeId == value.payeeId) 
                                {
                                	$scope.payeePaidAmount = value.paidAmount-(value.disallowedAmount+value.otherDeduction)-value.crDeuAmount;
                                }
                            });
                        }
                $scope.calculateTotalAmount($scope.billDetailsListClone);
            }

            $scope.getBillPayeeDetailsObj = function() {
                
            	  angular.forEach($scope.billDetailsListClone,function(value, key) {
            		  value.isSelected = false;
                  });
            	
            	
            	var selectedBillPayeeObj = $.grep($scope.payeeList, function (billPayeeList) {
                    return billPayeeList.billPayeeDetailsId == $scope.billPayeeDetailsId;
                });
            	$scope.creditNoteMstObj.billPayeeDetailsId = $scope.billPayeeDetailsId;
            	$scope.creditNoteMstObj.billPayeeId = selectedBillPayeeObj[0].payeeId;
            	$scope.creditNoteMstObj.associateCompanyId = selectedBillPayeeObj[0].associateCompanyId;
            	$scope.calculatePayerRefundableAmount();
            }
            
            $scope.deactivateFinacialRequest = function(){
				var data = {
						billId :$scope.billMstId,
				}
				
				var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequestFinancialRequest";
				
				GenericService
				.serviceAction("POST", URI, data)
				.then(function(response) {
							$rootScope.stopLoader();
							if (response.data.status == "success") {
							}
						});
            }

            $scope.checkDoubSave = false;
            $scope.saveCreditNoteFinal = function() {
                
                if( $scope.checkDoubSave == true){
                	return false;
                }
                $scope.checkDoubSave = true;
                $scope.saveBtnDis = false;
                
                $rootScope.startLoader();
                var crTotalAmount = 0.00;
                var crTaxAmount = 0.00;
                var crNetAmount = 0.00;
                var crDeuAmount = 0.00;
                var crOutAdjustmentAmount = 0.00;
                var selfPayable = 0.00;
                var creditPayable = 0.00;
                var packageConsumptionAmt = 0.00;
                var signRoundOff = 0;
                var creditNoteDetailsList = [];
                var flag = false;
                $scope.orderObj = {};
                angular
                    .forEach(
                        $scope.billDetailsListClone,
                        function(value, key) {

                            if (value.discount != 0 && value.discountPercentage > 0 && value.isDiscountApplicable == 'Y') {
                                var creditNoteDetailsObj = angular.copy($scope.creditNoteDetailsObj);
                                creditNoteDetailsObj.orderDetailId = value.orderDetailId;
                                creditNoteDetailsObj.cancelOrDiscount = 2;
                                creditNoteDetailsObj.crTaxAmount = value.crTaxAmount;
                                creditNoteDetailsObj.crNetAmount = value.crNetAmount;
                                creditNoteDetailsObj.crDiscountAmount = value.previousDiscount;
                                creditNoteDetailsObj.crAdditionalDiscount = value.discount;
                                creditNoteDetailsObj.crServiceRateAmount = value.grossAmount;
                                creditNoteDetailsObj.orderId = value.orderId
                                creditNoteDetailsObj.orderDetailsId = value.orderDetailsId;
                                
                                creditNoteDetailsObj.hospitalShare = value.hospitalShare;
                                creditNoteDetailsObj.discountOwnership = value.discountOwnership;
                                creditNoteDetailsObj.refDocSharePer = value.refDocSharePer;
                                creditNoteDetailsObj.docSharePer = value.docSharePer;
                                
                                creditNoteDetailsObj.billingDetailsId = value.billingDetailsId;
                                crTotalAmount = getAmount(crTotalAmount) + getAmount(value.crAmount);
                                crTaxAmount = getAmount(crTaxAmount) + getAmount(value.crTaxAmount);
                                crNetAmount = getAmount(crNetAmount) + (getAmount(value.crNetAmount) + getAmount(value.crTaxAmount));
                                if($scope.creditNoteMstObj.billPayeeId != $scope.selfPayeeId ){
                                        value.selfPayable = 0;
                                        value.creditPayable = getAmount(value.creditPayable) - (getAmount(value.crNetAmount) + getAmount(value.crTaxAmount));
                                        crDeuAmount = getAmount(crDeuAmount) + 0;
                                }
                                else{
                                	crDeuAmount = getAmount(crDeuAmount) + (getAmount(value.crNetAmount) + getAmount(value.crTaxAmount));
                                }

                                 selfPayable = getAmount(selfPayable) + getAmount(value.selfPayable);
                                 creditPayable = getAmount(creditPayable) + getAmount(value.creditPayable);
                                 packageConsumptionAmt = getAmount(packageConsumptionAmt) + getAmount(value.packageConsumptionAmt);

                                if (value.discountDetailsList.length > 0) {
                                    creditNoteDetailsObj.listCreditNoteDiscountDetailsDto = value.discountDetailsList;
                                    flag = true;
                                } else {
                                    var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
                                    creditNoteDiscountDetails.discountCategoryId = value.discountCategoryId;
                                    creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
                                    creditNoteDiscountDetails.discountAmount = value.discount;
                                    creditNoteDiscountDetails.discountGivenBy = $scope.userId;
                                    creditNoteDiscountDetails.discountCategory = "";
                                    creditNoteDetailsObj.listCreditNoteDiscountDetailsDto.push(creditNoteDiscountDetails);
                                    flag = true;
                                }
                                creditNoteDetailsList.push(creditNoteDetailsObj);
                            } else if (value.isSelected == true) {
                                var creditNoteDetailsObj = angular.copy($scope.creditNoteDetailsObj);
                                creditNoteDetailsObj.orderDetailId = value.orderDetailId;
                                creditNoteDetailsObj.cancelOrDiscount = 1;
                                creditNoteDetailsObj.crTaxAmount = value.taxAmount;
                                creditNoteDetailsObj.crNetAmount = value.netAmt;
                                creditNoteDetailsObj.crDiscountAmount = value.previousDiscount;
                                creditNoteDetailsObj.crAdditionalDiscount = 0.00;
                                creditNoteDetailsObj.crServiceRateAmount = value.grossAmount;
                                crTotalAmount = getAmount(crTotalAmount) + getAmount(value.grossAmount);
                                crTaxAmount = getAmount(crTaxAmount) + getAmount(value.taxAmount);
                                crNetAmount = getAmount(crNetAmount) + (getAmount(value.crNetAmount) + getAmount(value.crTaxAmount));
                                if($scope.creditNoteMstObj.billPayeeId != $scope.selfPayeeId ){
                                      value.selfPayable = 0;
                                      value.creditPayable = getAmount(value.creditPayable) - (getAmount(value.crNetAmount) + getAmount(value.crTaxAmount));
                                      crDeuAmount = getAmount(crDeuAmount) + 0;
                                }else{
                                   crDeuAmount = getAmount(crDeuAmount) + (getAmount(value.crNetAmount) + getAmount(value.crTaxAmount));
                                }
                                selfPayable = getAmount(selfPayable) + getAmount(value.selfPayable);
                                creditPayable = getAmount(creditPayable) + getAmount(value.creditPayable);
                                packageConsumptionAmt = getAmount(packageConsumptionAmt) + getAmount(value.packageConsumptionAmt);
                                creditNoteDetailsObj.orderId = value.orderId
                                creditNoteDetailsObj.orderDetailsId = value.orderDetailsId;
                                creditNoteDetailsObj.billingDetailsId = value.billingDetailsId;
                                creditNoteDetailsObj.selfPayable = value.selfPayable;
                                creditNoteDetailsObj.creditPayable = value.creditPayable;
                                creditNoteDetailsObj.packageConsumptionAmt = value.packageConsumptionAmt;
                                creditNoteDetailsObj.isPackage = value.package;
                                creditNoteDetailsObj.packageId = value.packageId;
                                creditNoteDetailsObj.packageTypeId = value.packageTypeId;
                                creditNoteDetailsList.push(creditNoteDetailsObj);

                                if (flag == false) {
                                    $scope.orderObj.orderId = value.orderId;
                                    $scope.orderObj.patientId = $scope.creditNoteMstObj.patientId;
                                    $scope.orderObj.createdBy = $scope.userId;
                                    $scope.orderObj.orgId = creditNoteDetailsObj.organisationId;
                                    $scope.orderObj.orgUnitId = creditNoteDetailsObj.unitId;
                                }
                                flag = true;
                            }
                        });



                if (flag) {
                    var creditNoteMstObj = angular.copy($scope.creditNoteMstObj);
                    creditNoteMstObj.billId = $scope.billDetailsListClone[0].billingMasterId;
                    creditNoteMstObj.counterId = $rootScope.counterId;
                    creditNoteMstObj.crTaxAmount = getAmount(crTaxAmount);
                    creditNoteMstObj.crTotalAmount = getAmount(crTotalAmount);
                    creditNoteMstObj.crNetAmount = $scope.finalTotalCrAmount ;
                    creditNoteMstObj.crOutAdjustmentAmount = crOutAdjustmentAmount;
                    creditNoteMstObj.crDeuAmount = getAmount(crDeuAmount)==0 ? 0 : getAmount(crDeuAmount) + getAmount($scope.discountRoundOff);
                    var actualBillAfterDiscount =0.00
                    if($scope.creditNoteMstObj.billPayeeId == $scope.selfPayeeId)
                     {
                        if($scope.payeePaidAmount > 0)
                           {
                              if(creditNoteMstObj.crDeuAmount > $scope.payeePaidAmount)
                               {
                                  creditNoteMstObj.crDeuAmount = $scope.payeePaidAmount;
                               }
                               else if($scope.payeePaidAmount >= creditNoteMstObj.crDeuAmount)
                               {
                                    creditNoteMstObj.crDeuAmount = creditNoteMstObj.crDeuAmount;
                               }
                               else{
                                  creditNoteMstObj.crDeuAmount = 0;
                               }
                          }
                          else{
                               creditNoteMstObj.crDeuAmount = 0;
                          }
                     }else{
                           creditNoteMstObj.crDeuAmount = 0;
                     }
                    creditNoteMstObj.discountRoundoff = $scope.discountRoundOff;
                    creditNoteMstObj.status = 'A';
                    creditNoteMstObj.cancelOrDiscount = 2;
                    creditNoteMstObj.listCreditNoteDetails = creditNoteDetailsList;
                    creditNoteMstObj.billPayeeId = $scope.creditNoteMstObj.billPayeeId;
                    creditNoteMstObj.selfPayeeId = $scope.selfPayeeIdCookies;
                    creditNoteMstObj.billPayeeDetailsId = $scope.creditNoteMstObj.billPayeeDetailsId;
                    creditNoteMstObj.associateCompanyId = $scope.creditNoteMstObj.associateCompanyId;
                    creditNoteMstObj.selfPayable = getAmount(actualBillAfterDiscount)==0 ? 0 : getAmount(actualBillAfterDiscount) + getAmount($scope.actualBillAfterDiscount);
                    creditNoteMstObj.creditPayable = getAmount(creditPayable);
                    creditNoteMstObj.packageConsumptionAmt = getAmount(packageConsumptionAmt);
                    creditNoteMstObj.encounterId = $scope.encounterIdNew;
                    creditNoteMstObj.admissionId = $scope.admissionIdNew;
                    //creditNoteMstObj.createdBy = creditNoteMstObj.requestSentTo;
                    creditNoteMstObj.createdBy = $scope.userId;
                    creditNoteMstObj.visitTypeId = $scope.visitTypeId;
                    console.log("creditNoteMstObj", JSON.stringify(creditNoteMstObj));
                    var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote";
                    BillingGenericService.serviceAction(METHOD_POST, URI, creditNoteMstObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
                        if (response.data.status == "success") {
                            $rootScope.stopLoader();
                            $scope.saveBtnDis = true;
                            $scope.checkDoubSave = false;
                            if($scope.creditNoteMstObj.isApproved == 'Y' ||  $scope.billStatusId == 1 ){
                            	
                            	 angular.forEach($scope.billDetailsListClone,function(value, key) {
                            		 if(value.isSelected != null && value.isSelected == true && value.packageId != undefined && value.packageId!= null){
                            			 $scope.forceCancelPackage(value);
                                 	}
                            	 });
                            	
                            }
                            $scope.patientDetailsList = [];
                            $scope.patientDetails = {};
                            $scope.billDetailsListClone = [];
                            $scope.billList = [];
                            $scope.initCreditNotMastObj();
                            $scope.initCreditNoteDetailsObj();
                            if(parseFloat($scope.totalremainingRefundAmt)== parseFloat($scope.totalCrAmountR)){
                            	$scope.deactivateFinacialRequest();
                            }
                            $scope.requestDiscountDiv = false;
                            //ForReport Print
                            var data = {
                                "appName": "HIS",
                                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                                "reportDesignFilePath": "OPD_CreditNote.rpt",
                                "reportFormat": "PDF",
                                "exportFileName": "CreditNote",
                                "reportParams": [{
                                    "paramName": "@credit_note_id",
                                    "paramType": "Integer",
                                    "paramValue": response.data.object
                                }]
                            };

                            $state.go('refundAgainstCR');
                            reportService.generate(REPORT_API_URI1, data);
                           
                            $rootScope.stopLoader();
                        } else if (response.data.status == "error") {
                            $scope.saveBtnDis = true;
                            $scope.checkDoubSave = false
                            $rootScope.stopLoader();
                        }

                    });
                } else {
                    growl.warning("Please select a service...!!", {
                        title: WARNING_MSG
                    });
                    $rootScope.stopLoader();
                    $scope.saveBtnDis = true;
                    $scope.checkDoubSave = false
                    return;
                }


            }


            //totalAmountSummary


            $scope.funDiscountPercentageBillWise = function() {
                //alert($scope.billWiseDiscountObj.discountPercentageBillWise);
                /*if($scope.autoRenderedServicesFinalList.length > 0){
                	var totalDisAmount =(((parseFloat($scope.totalAmountSummary) - parseFloat($scope.totalConcessionSummary))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
                	if(totalDisAmount > 0){
                	$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
                	$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
                	}
                }
                totalAmountSummary*/


                var devide = ($scope.billDetailsListClone != null && $scope.billDetailsListClone.length > 0) ? $scope.billDetailsListClone.length : 1
                var totalDisAmount = ((getAmount($scope.totalAmountSummaryDis) * (getAmount($scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise) / 100)));
                 $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = getAmount(totalDisAmount);
                if(parseFloat($scope.netPayableSummary)<=(parseFloat(totalDisAmount)+parseFloat($scope.totalRefundAmount)+parseFloat($scope.totaCrTaxAmount)))
                  {
                		 $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = getAmount(totalDisAmount)-getAmount($scope.totalRoundOff);
                  }


            }

            $scope.funDiscountAmountBillWise = function() {
                //alert($scope.billWiseDiscountObj.discountAmountBillWise);
                /*if($scope.autoRenderedServicesFinalList.length > 0){
                	var totalDisPercentage =((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/(parseFloat($scope.totalAmountSummary)-parseFloat($scope.totalConcessionSummary)))*100).toFixed(2);
                	if(totalDisPercentage > 0){
                	$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
                	$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
                	}
                }
                */
                var devide = ($scope.billDetailsListClone != null && $scope.billDetailsListClone.length > 0) ? $scope.billDetailsListClone.length : 1
                if(parseFloat($scope.netPayableSummary)<=(parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)+parseFloat($scope.totalRefundAmount)+parseFloat($scope.totaCrTaxAmount)))
                    {
                      $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = getAmount($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)-getAmount($scope.totalRoundOff);
                   }
                var totalDisPercentage = parseFloat($scope.totalAmountSummaryDis) > 0 ? ((Number(parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)) / (parseFloat($scope.totalAmountSummaryDis))) * 100) : 0;
                totalDisPercentage = Number(parseFloat(totalDisPercentage).toFixed(2));
                $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = getAmount(totalDisPercentage);

            }




        } catch (e) {
            console.log(e.message);
        }

        $scope.filterByDummyServices= function()
		 {
			 $rootScope.startLoader();
			 $scope.billDetailsListClone =[]
			 if($scope.filterDummyService==1)//all 
			 {
				 $scope.billDetailsListClone = angular.copy($scope.billDetailsListForDymmyServiceFilter);
			 }else if($scope.filterDummyService==2)//real
			 {
				 angular.forEach($scope.billDetailsListForDymmyServiceFilter, function (value, key) 
				 {
					 if(value.isDummyService==false)
						 $scope.billDetailsListClone.push(value);
				 });
			 }else if($scope.filterDummyService==3 )//dummy
			 {
				 angular.forEach($scope.billDetailsListForDymmyServiceFilter, function (value, key) 
				 {
					 if(value.isDummyService==true)
						 $scope.billDetailsListClone.push(value);
				 });
			 }
			 
			 $scope.calculateTotalAmount($scope.billDetailsListClone);
			 $rootScope.stopLoaderPopup();
			 $rootScope.stopLoader();
		 }
        
        //Thumb search

        $scope.thumbSearch = function() {
            try {
                $rootScope.startLoader();
                var quality = 60; //(1 to 100) (recommanded minimum 55)
                var timeout = 10; // seconds (minimum=10(recommanded), maximum=60, unlimited=0 )
                var res = CaptureFinger(quality, timeout);
                if (res.httpStaus) {
                    if (res.data.ErrorCode == "0") {
                        $scope.thumbImage = res.data.BitmapData;
                        var URI = BASE_URL + ROOT_URL + "/api/global/searchByFingerPrint";

                        var param = {
                            organizationId: $scope.organizationId,
                            unitId: $scope.unitId,
                            thumbImage: res.data.BitmapData
                        };
                    }
                    BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                        $scope.searchPatientList = [];
                        if (response.data.status == "success") {
                            $scope.patientList = response.data.listObject;
                            patSearchObj = {};
                        } else if (response.data.status == "error") {
                            $rootScope.stopLoader();
                        }

                    });

                } else {
                    alert(res.err);
                    $rootScope.stopLoader();
                }
            } catch (e) {
                alert(e);
                $rootScope.stopLoader();
            }
        }

    }
]);