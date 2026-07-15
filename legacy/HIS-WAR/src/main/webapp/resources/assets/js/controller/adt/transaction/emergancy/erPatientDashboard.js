(function () {
    "use strict";
    /**
     * @Author By Akshay Narale
     * @name myApp.controller:ErPatientDashboard
     */
    function ErPatientDashboard($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,PagerService,BillingGenericService,GenericServiceFileUpload,GenericServiceParamHeader) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.updateBtnFlag=true;

            /**Variables for pagination */
            $scope.offset = 0;
            $scope.noOfRecordsPerPage = 10;
            $scope.unitDeptMasterList = [];
            $scope.totalNoOfRecords;
            $scope.searchKeyword;
            $scope.commonListCount;
            $scope.pager = {};
            $scope.page;


            $scope.openDatePicker = function() {
                $scope.datepicker.opened = true;
            };

            $scope.datepicker = {
                opened : false
            };


            $scope.dateOptions = {
                formatYear : 'yyyy',
                showWeeks : false,
                minDate: new Date()
            };
            $scope.openDatePickerPdd = function() {
                $scope.datepickerPdd.opened = true;
            };


            $scope.datepicker = new Date();
            $scope.datepickerPdd = new Date();

            $scope.clearFunction=function(){
                $scope.boxCode="";
                $scope.boxDescription="";
            }

            $scope.saveBox=function(){

                if($scope.boxCode==undefined || $scope.boxCode==null || $scope.boxCode=="" ){
                    growl.error("Please Enter Box Code",
                        {
                            title : 'Error!'
                        });
                    return false;
                }
                if($scope.boxDescription==undefined || $scope.boxDescription==null || $scope.boxDescription=="" ){
                    growl.error("Please Enter Box Description",
                        {
                            title : 'Error!'
                        });
                    return false;
                }
                var saveObj={
                    boxCode:$scope.boxCode,
                    boxDescription:$scope.boxDescription,
                    unitId:$scope.unitId,
                    organizationId :$scope.organizationId,
                    updatedBy :$scope.userId,
                    createdBy :$scope.userId,
                    status :'A'
                }

                var URI1 = BASE_URL + ROOT_URL +"/mrdbox/saveMrdBox";
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                        growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                    }
                    else if(response.data.status =='error'){
                        growl.error(response.data.message,
                            {
                                title : 'Error!'
                            });
                    }
                    $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                    console.log(response);
                    $scope.getBox();
                    initializeController();
                    $scope.clearFunction();
                    $rootScope.stopLoader();
                });
            }



              $scope.erPatientList=[];

              $scope.convertMinutesToHHmm = function(minutes) {
                if(minutes==0 || minutes==null || minutes==undefined){
                    return "NA";
                }
									const hrs = Math.floor(minutes / 60);
									const mins = minutes % 60;

									return String(hrs).padStart(2, '0') + ':' + String(mins).padStart(2, '0');
								}
           
            $scope.getERPatientDashBoardData=function(){
                 var fromDate = $scope.datepicker ? moment($scope.datepicker).format('DD-MM-YYYY') : null;
                 var toDate   = $scope.datepickerPdd ? moment($scope.datepickerPdd).format('DD-MM-YYYY') : null;
                 $scope.erPatientList=[];
                var objUpdate ={
                    organizationId :$scope.organizationId,
                    unitId :$scope.unitId,
                    fromDate :fromDate,
                    toDate:toDate,
                }
                var URI2 = BASE_URL + ROOT_URL + '/api/opd/getERPatientDashBoardData';
                GenericService.serviceAction("POST", URI2, objUpdate).then(function (response) {
                    if (response.data.status == 'success'){
                        $scope.erPatientList=response.data.listObject;

                       angular.forEach($scope.erPatientList, function (item) {
                       switch (true) {
                            case item.stemi === 'Y':
                                item.code = 'STEMI';
                                break;

                            case item.stroke === 'Y':
                                item.code = 'Stroke';
                                break;

                            case item.truma === 'Y':
                                item.code = 'Trauma';
                                break;

                            case item.sepsis === 'Y':
                                item.code = 'Sepsis';
                                break;

                            case item.shock === 'Y':
                                item.code = 'Shock';
                                break;

                            default:
                                item.code = null;
                        }

                        item.lengthOfStay = $scope.convertMinutesToHHmm(item.lengthOfStay);

                        console.log(item.code);
                        item.triageCategoryId = item.currentTriage !=null ? item.currentTriage : item.triageCategoryId;
                         if (item.arrivalTime) {
                           let str = item.arrivalTime.toString();
                           if (str.lastIndexOf(":") !== -1) {
                             item.arrivalTime = str.substring(
                               0,
                               str.lastIndexOf(":")
                             );
                           } else {
                             item.arrivalTime = str; 
                           }
                         }
                       });

                        growl.success(response.data.message,
                            {
                                title : 'Success!',
                            });
                    }else{
                        growl.error(response.data.message,
                            {
                                title : 'Error!',
                                message : 'Failed to load ER Patient Dashboard Data '
                            });
                    }
                   
                });

            }




            $scope.initABCDE = function() {

		$scope.abcde = {
		airway: {
			patent: false,
			talking: false,
			obstructed: false,
			snoring: false,
			cSpine: false,
			interventions: {
			suction: false,
			jawThrust: false,
			opaNpa: false,
			ett: false,
			surgical: false
			},
			status: null   // radio: Stable / Threatened / Compromised
		},
		breathing: {
			spontaneous: false,
			distress: false,
			symmetrical: false,
			unequal: false,
			cyanosis: false,
			rr: null,
			spo2: null,
			breathSound: null,    // radio: Equal / Decreased / Absent / Wheeze / Crepitation
			intervention: null,   // radio: O2_NC / O2_FM / etc.
			status: null          // radio: Adequate / Inadequate
		},
		circulation: {
			centralPulse: false,
			peripheralPulse: false,
			capillaryRefill: false,
			bleeding: false,
			coldClammy: false,
			bpSys: null,
			bpDia: null,
			hr: null,
			interventions: {
			directPressure: false,
			tourniquet: false,
			ivAccess: false,
			ivSize: null,       // text input for IV size
			ivFluids: false,
			bloodProducts: false
			},
			status: null          // radio: Stable / Shock
		},
		disability: {
			gcsTotal: null,
			gcsE: null,
			gcsV: null,
			gcsM: null,
			avpu: null,           // radio: Alert / Voice / Pain / Unresponsive
			pupils: null,         // radio: EqualReactive / UnequalNonReactive / SeizureActivity
			glucose: null,
			status: null          // radio: Normal / Altered
		},
		exposure: {
			fullyExposed: false,
			hiddenInjuries: false,
			tempChecked: false,
			temperature: null,
			interventions: {
			warmBlanket: false,
			fluidWarmer: false,
			rewarming: false
			},
			status: null         
		}
		};
	};

	$scope.initABCDE();



            $scope.viewABCDE = function (abcdeRow) {

    // Reset
    $scope.abcde = {
        airway: { interventions: {} },
        breathing: { interventions: {} },
        circulation: { interventions: {} },
        disability: {},
        exposure: { interventions: {} }
    };

    // Parse JSON string
    if (abcdeRow) {
        try {
            $scope.abcde = JSON.parse(abcdeRow);
        } catch (e) {
            console.error("Error parsing ABCDE JSON", e);
        }
    }

    // Show popup
    angular.element('#abcdePopup').modal('show');
};



            $scope.getBox=function(){
                     $scope.getERPatientDashBoardData();
                var getObj={
                    unitId:$scope.unitId,
                    organizationId :$scope.organizationId,
                    page :$scope.offset,
                    size:$scope.noOfRecordsPerPage
                }

                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI2, getObj).then(function (response) {
                    $scope.getMrd=[];
                    $scope.getMrd=response.data.listObject;
                    $scope.commonListCount = response.data.object;
                    $scope.setNoOfRecords();
                    $scope.setPage(1, false);
                    console.log(response);
                    $rootScope.stopLoader();
                });
            }
            $scope.getBox();


            $scope.setNoOfRecords = function() {
                $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
            };

            $scope.initBoxGroupMasterList = function(offset, noOfRecordsPerPage){
                var obj1 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId,
                    page        :offset != null ? offset : 0,
                    size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
                }

                var obj2 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId
                }
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                    $scope.getMrd=[];
                    $scope.getMrd=response.data.listObject;
                    $scope.unitDeptMasterList = response.data.listObject;
                    $scope.commonListCount = response.data.object;
                    $scope.setPage(1, false);
                    console.log(response);
                    $rootScope.stopLoader();
                });
            }


            $scope.masterList = function(offset, noOfRecordsPerPage) {
                var obj1 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId,
                    page        :offset != null ? offset : 0,
                    size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
                }

                var obj2 = {
                    organizationId :$scope.organizationId,
                    unitId        :$scope.unitId
                }
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                    $scope.getMrd=[];
                    $scope.getMrd=response.data.listObject;
                    $scope.unitDeptMasterList = response.data.listObject;
                    $scope.commonListCount = response.data.object;
                    //$scope.setPage(1, false);
                    console.log(response);
                    $rootScope.stopLoader();
                });
            }




            $scope.setPage = function(page, flag) {
                if (page < 1 || page > $scope.pager.totalPages)
                {
                    return;
                }
                $scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
                if (flag)
                {
                    $scope.masterList($scope.pager.startIndex, $scope.pager.pageSize);
                }
            }
            $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);

            $scope.viewList = [];
            $scope.viewBtn=function (data){
                if(data.status=='I'){
                    growl.error("Please update status then you can edit.",
                        {
                            title : 'Error!'
                        });
                    return false;
                }
                console.log(data)
                var URI =  BASE_URL+ROOT_URL+"/mrdbox/getMrdBoxById/"+data.boxId+"/"+$scope.organizationId+"/"+$scope.unitId;
                GenericService.serviceAction("GET", URI,{}).then(
                    function (response) {
                        $scope.viewList=response;
                        console.log($scope.viewList)
                        $scope.boxID=response.data.object.boxId;
                        $scope.boxCode=response.data.object.boxCode;
                        $scope.boxDescription=response.data.object.boxDescription;
                        $scope.saveBtnFlag=true;
                        $scope.updateBtnFlag=false;
                    });
            }



              $scope.set_color_er_list = function(triageCategoryId) {
			             							    if(triageCategoryId == 1){
			             							        return {	"height": "22px",
			             							        	"width": "22px",
			             							        	"margin-left": "9px",
			             							        	"margin-top":"6px",
																"display": "inline-block",
			             							        	"background-color": "red"};
			             							    }
			             							    else if(triageCategoryId  == 2 ){
			             							        return {	"height": "22px",
			             							        	"width": "22px",
																 "display": "inline-block",
			             							        	"margin-left": "9px",
			             							        "margin-top":"6px",
			             							        	"background-color": "red"};
			             							        
			             							    }
			             							    else if(triageCategoryId  == 3 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "Yellow"
			             							        };
			             							        
			             							    }
			             							    else if(triageCategoryId == 4 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "Aquamarine"};
			             							        
			             							    }
			             							    else if(triageCategoryId == 5 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "Aquamarine"};
			             							        
			             							    }else if(triageCategoryId == 6){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "grey"};
			             							        
			             							    }
													
			             							};


            $scope.refferalDoctorList = null;



            $scope.showRefferalDoctorData = function(reffData){
                if (reffData && reffData.trim() !== "") {
                  $scope.refferalDoctorList = reffData.split("#");
                   angular.element('#referralDoctorsPopup').modal('show');
                } else {
                  $scope.refferalDoctorList = [];
                }

            }






            
          
            $scope.showUpdateBtn=function(value){
                var objUpdate ={
                    boxId :$scope.boxID,
                    boxCode: $scope.boxCode,
                    boxDescription:$scope.boxDescription,
                    status:'A',
                    organizationId :$scope.organizationId,
                    unitId :$scope.unitId,
                    updatedBy :$scope.userId,
                    createdBy :$scope.userId,
                }
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/updateMrdBox";
                GenericService.serviceAction("PUT", URI2, objUpdate).then(function (response) {
                    if (response.data.status == 'success'){
                        growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                    }
                    $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                    $scope.clearFunction();
                    $scope.getBox();
                    $scope.saveBtnFlag=false;
                    $scope.updateBtnFlag=true
                    console.log(response);
                });

            }

            $scope.updateStatus = function(boxId,boxCode,boxDescription,id, status){
                var URI2 = BASE_URL + ROOT_URL +"/mrdbox/updateMrdBoxStatus";
                $scope.currentDate = new Date();
                var objStatus = {
                    boxId :boxId,
                    boxCode: boxCode,
                    boxDescription:boxDescription,
                    status:status,
                    organizationId :$scope.organizationId,
                    unitId :$scope.unitId,
                    updatedBy :$scope.userId,
                    createdBy :$scope.userId,
                }
                GenericService.serviceAction("PUT", URI2, objStatus).then(function (response) {
                    if (response.data.status == 'success'){
                        growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                    }
                    $scope.clearFunction();
                    $scope.getBox();
                    console.log(response);
                });
            }

            $scope.getActiveComplaints=function(erPatient){
                 if(erPatient.patientId==null || erPatient.patientId=='' || erPatient.patientId==undefined)
                {
                    growl.warning("Please select Patient..",{
                        title : ERROR_MSG

                    });
                    return false;
                }
                $scope.complaintsPatientList=[];
                var headerObj = {
                    organization_id : $scope.organizationId
                };

                var paramObj = {
                    patientId : erPatient.patientId,
                };
                var data = {};
                var URI = BASE_URL + ROOT_URL + "/api/opd/getComplaintAppointmentByPatientId";
                GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,paramObj, headerObj)
                    .then(
                        function(response) {
                            console.log(response);
                            if (response.data.status == "success")
                                $scope.complaintsPatientList = response.data.listObject;
                            console.log("complaintsPatientList", $scope.complaintsPatientList);
                    });
            }

            $scope.getComplaintTemplateByIcpcId=function(templateTypeId,obj){

                $rootScope.startLoader();
                var data = {
                    orgId : $scope.organizationId,
                    unitId : $scope.unitId,
                    templateTypeId : templateTypeId,
                    patientId : $scope.patientDetails.patientId,
                    mIcpcId :obj.diagnosisId
                }

                console.log('data', JSON.stringify(data));
                var URI = BASE_URL + ROOT_URL
                        + "/global/petient/template";
                BillingGenericService
                    .serviceAction(METHOD_POST, URI, data,
                        NOTIFICATION_MSG_STATUS_FALSE)
                        .then(
                            function(response) {

                                    if (response.data.status == 'success') {
                                        $scope.templateTransactionList = response.data.listObject;
                                        $('#viewtemplateModal').modal('show');
                                        $rootScope.stopLoader();
                                    } else if (response.data.status == 'error') {
                                        $rootScope.stopLoader();
                                    }
                        });

            }

            $scope.clearSearchFilter = function(){
                $scope.searchKeyword="";
            }

        }


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("ErPatientDashboard", {
            url: "/Er-PatientDashboard",
            title: 'ER Patients Dashboard',
            templateUrl: 'views/AdmissionDischarge/ER/erPatient-dashboard.html',
            controller: "ErPatientDashboard.Controller",
            resolve : {
/*                data: function($q, $state, $timeout) {
                    var deferred = $q.defer();
                    $timeout(function() {
                        var arr = JSON.parse(localStorage.getItem('permissions'))
                            .modules.filter(function(item){
                                return (item.id == 1069);
                            });
                        if(arr.length <= 0){
                            $state.go("accessDenied")
                            deferred.reject();
                        } else {
                            deferred.resolve();
                        }
                    });

                    return deferred.promise;
                }*/
            },
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("ErPatientDashboard.Controller", ErPatientDashboard);
})();