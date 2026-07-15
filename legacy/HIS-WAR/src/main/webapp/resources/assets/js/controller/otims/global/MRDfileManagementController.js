(function () {
    "use strict";

    function MRDfileManagementController($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,BillingGenericService, $stateParams) {
            var self = this;

            //Default Constructor
            var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
                $rootScope.loginpage = true;
            }

            $scope.commonObj = $stateParams.commonObj;
            $scope.patientList = [];
            $scope.patientDetails = {};

            $scope.firstTabLevel = 1 ;

            //Object For Patient Search
            $scope.patientSearchObj = {
                "uhidNumber" : "",
                "patientName" : "",
                "genderId" : "",
                "mobileNumber" : "",
                "dob" : "",
                "identificationNumber" : "",
                "identificationTypeId" : "",
                "unitId" : $scope.unitId,
                "organizationId" : $scope.organizationId,
                "registrationTypeIdList" : [1,2,3,4,6]
            }

            $scope.initPatientObj = function(){
                 $scope.patientObj = {
                        prefixId : '',
                        firstName : '',
                        middleName : '',
                        lastName : '',
                        genderId : '',
                        email : '',
                        mobileNumber : '',
                        dob : '',
                        createdDate : $scope.dt,
                        updatedDate : $scope.dt,
                        organizationId : $scope.organizationId,
                        unitId : $scope.unitId,
                        updatedBy : $scope.userId,
                        createdBy : $scope.userId,
                        status : 'A',
                        registrationTypeId:5, //for Pre-Registration Patient
                        patientId:0,
                        remark:'',
                        patientCategoryId:1
                 };
                $scope.newAge="";
                $scope.age = '';

                $scope.changeOfTime = "";
                $scope.changeOfDate = "";

            $scope.patientSearchObj = {
                    "uhIdNumber"      : "",
                    "patientName"     : "",
                    "genderId"        : "",
                    "mobileNo"        : "",
                    "birthDate"       : "",
                    "identificationNo":"",
                    "patientType"	  :"1",
                    "unitId"          :$scope.unitId,
                    "organizationId"  :$scope.organizationId
            }

               $scope.patientDetails = {
                       "patientId"	  : "",
                       "patientName" : "",
                       "genderCode"  : "",
                       "age"         : "",
                       "uhIdNumber"  : "",
                       "birthDate"	  : "",
                       "encounterId" : ""
               }


                $scope.patientDetails = {};
                $scope.patientDetails.isFromOtCompletion = true;

                $scope.staffObj = {};
                $scope.roleObj = {};
                $scope.staffList = [];
                $scope.newProcList = [];
                $scope.procedureOrderList = [];

                $scope.otList = [];
                $scope.otAddedList = [];
                $scope.otAddedStaffDetailsList = [];
                $scope.otStaffDetailsList = [];
                $scope.equipmentDetailList = [];
                $scope.equipmentBatchList = [];

                $scope.procedureOTScheduleList = [];
                $scope.procedureStaffScheduleList = [];
                $scope.procedureEquipmentScheduleList = [];
                $scope.patientList = [];

                $scope.requestedFilesList
                //$scope.staffList1 = [];
                $scope.datepicker="";

            }

            $scope.initPatientObj();
        $scope.activeRackList = [];
        $scope.activeBoxList = [];
        $scope.activeRows = [];

        $scope.patientSerachHideAndShow = true;

    	$scope.openDatePickerSearch = function() {
    		$scope.datepickerSearch.opened = true;
    	};

    	$scope.datepickerSearch = {
    		opened : false
    	};

        $scope.dateOptionsSearch = {
            formatYear : 'yyyy',
            showWeeks : false,
            maxDate : new Date()
        };


        $scope.clearFields = function(){
            $scope.patientSearchObj = {
                "uhIdNumber"      : "",
                "patientName"     : "",
                "genderId"        : "",
                "mobileNo"        : "",
                "birthDate"       : "",
                "identificationNo":"",
                "patientType"	  :"1",
                "unitId"          :$scope.unitId,
                "organizationId"  :$scope.organizationId
            }
            $scope.patientDetails = {
                "patientId"	  : "",
                "patientName" : "",
                "genderCode"  : "",
                "age"         : "",
                "uhIdNumber"  : "",
                "birthDate"	  : "",
                "encounterId" : "",
                "createdDate" :""
            }
            $scope.patientList = [];

            $scope.patientObj = {
                prefixId : '',
                firstName : '',
                middleName : '',
                lastName : '',
                genderId : '',
                email : '',
                mobileNumber : '',
                dob : '',
                createdDate : $scope.dt,
                updatedDate : $scope.dt,
                organizationId : $scope.organizationId,
                unitId : $scope.unitId,
                updatedBy : $scope.userId,
                createdBy : $scope.userId,
                status : 'A',
                registrationTypeId:1,
                patientId:0,
                remark:'',
                patientCategoryId:1
            };
            $scope.patientInfo = {};
            $scope.globalEncounterList = [];
            $scope.universalList = [];
            $scope.globalList = [];
            $scope.encounterList = [];
            $scope.initPatientObj();
        }


    $scope.loadMoreData = function()
    {
        $scope.offset+=200;
        $scope.loadMoreFlag=true;
        $rootScope.searchPatient($scope.patientSearchObj);
    }
    $scope.offset=0;
    $scope.loadMoreFlag=false;
    $scope.noOfRecordsPerPage=200;
    $scope.patientList = [];
    $rootScope.searchPatient = function(patientsearchObj){
        patientsearchObj.fromMrdScreen = true;
        if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#datepickerSearch').val() == '' && ( patientsearchObj.identificationTypeId == "" || patientsearchObj.identificationTypeId == undefined ) && $scope.patientSearchObj.identificationNo == '')
        {
            growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"

                });
                return false;
        }

        if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
        {
            growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
            $scope.patientList = [];
            return false;
        }
        $rootScope.startLoader();

        if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
            patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
        }
        if($scope.loadMoreFlag==true)
            {
            patientsearchObj.offset=$scope.offset;
            $scope.loadMoreFlag=false;
            }
        else
            {
            patientsearchObj.offset=0;
            $scope.loadMoreFlag=false;
            $scope.patientList = [];
            }
        patientsearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
        var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
        BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){

            if(response.data.listObject.length == 0){
                growl.warning("No Result Found!",{
                    title : ERROR_MSG
                });
            }
            else{
                $scope.patientList = [];
            angular.forEach(response.data.listObject,function(value,key)
                    {
                        if(value.registrationTypeId != 5 && value.registrationTypeId != 4)
                            {
                            $scope.patientList.push(value);
                            }

                    });
            }

        });
        console.log("patientList=",$scope.patientList)
    }

        $scope.isMrdUser = false;
        $scope.getMrdUser = function(){

            var URI = BASE_URL + ROOT_URL + "/adt/mrd/validateMrdUser/"+$scope.userId+"/"+$scope.unitId+"/"+$scope.organizationId;
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                 if(response.data.status == 'success'){
                    $scope.isMrdUser = response.data.object.isMrdUser;
                    $rootScope.stopLoader();
                 }
                 else{
                        console.log(response.data.message)
                        $rootScope.stopLoader();
                 }
            });
    //        http://localhost:8080/HIS-WAR/adt/mrd/validateMrdUser/231/186/107
        }
        $scope.getMrdUser();

        $scope.getActiveRacks = function(){
            var URI = BASE_URL + ROOT_URL + "/mrdrack/getActiveMrdRacks/"+$scope.organizationId+"/"+$scope.unitId;
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                 if(response.data.status == 'success'){
                    $scope.activeRackList = response.data.listObject;
                    $rootScope.stopLoader();
                 }
                 else{
                        console.log(response.data.message)
                        $rootScope.stopLoader();
                 }
            });
        }


        $scope.getActiveRacks();

        $scope.getActiveBoxes = function(){
            var URI = BASE_URL + ROOT_URL + "/mrdbox/getActiveMrdBoxes/"+$scope.organizationId+"/"+$scope.unitId;
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                 if(response.data.status == 'success'){
                    $scope.activeBoxList = response.data.listObject;
                    $rootScope.stopLoader();
                 }
                 else{
                        console.log(response.data.message)
                        $rootScope.stopLoader();
                 }
            });
        }
            $scope.getActiveBoxes();

        $scope.tabNo=1;
        $scope.getActiveRows = function(){
            var URI = BASE_URL + ROOT_URL + "/mrdrow/getActiveMrdRows/"+$scope.organizationId+"/"+$scope.unitId;
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                 if(response.data.status == 'success'){
                    $scope.activeRows = response.data.listObject;
                    $rootScope.stopLoader();
                 }
                 else{
                        console.log(response.data.message)
                        $rootScope.stopLoader();
                 }
            });
        }
                $scope.getActiveRows();

            $scope.isSetFirstTabLevel = function(tabNum){
                return $scope.firstTabLevel === tabNum;
            };

            $scope.setFirstTabLevel = function(newTab){
                $scope.firstTabLevel = newTab;
                $scope.clear();
            };


            $scope.getPatientDetailsByIndex = function(index) {

            $scope.setFirstTabLevel(1);
            $scope.tabNo = 1;
            $scope.clear();
            $scope.updatePatientInfo={};
            $scope.patientInfo = $scope.patientList[index];

            $scope.patientDetails.patientName = $scope.patientInfo.patientName;
            $scope.patientDetails.age = $scope.patientInfo.age;
            $scope.patientDetails.uhIdNumber = $scope.patientInfo.uhIdNumber
            $scope.patientDetails.createdDate = $scope.patientInfo.createdDate;
            $scope.patientDetails.doctorName = $scope.patientInfo.doctorName;
            $scope.patientDetails.genderCode = $scope.patientInfo.genderCode;
            $scope.patientDetails.patientProfile = $scope.patientInfo.patientProfile;

            $scope.patientSerachHideAndShow = false;
            $(".patienttSearchResult").toggle();

            $scope.visitTypeId = 1;
    //        $scope.setFirstTabLevel=1;
            $scope.getEncounters($scope.patientInfo);
            }

            $scope.changeVisitType = function(){
                 $scope.clear();
                 $scope.encounterList = [];
                 $scope.tabNo = 1;
                 $scope.setFirstTabLevel(1);

                 angular.forEach($scope.globalEncounterList, function(item){
                     if($scope.visitTypeId == item.visitTypeId)
                     {
                         $scope.encounterList.push(item)
                     }
                 });
                 $scope.filterList($scope.tabNo);
            }


            $scope.getEncounters = function(patientInfo){

            $scope.visitType = "";
            if($scope.visitTypeId == 1){
                 $scope.visitType = "OPD";
            }
            else if($scope.visitTypeId == 2){
                 $scope.visitType = "IPD";
            }
            else if($scope.visitTypeId == 4){
                 $scope.visitType = "ER";
            }
            else if($scope.visitTypeId == 3) {
                 $scope.visitType = "DC";
            }

                var URI = BASE_URL + ROOT_URL + "/adt/mrd/getEncounterNo/"+patientInfo.patientId+"/"+$scope.visitType;
                $scope.encounterList = [];
                GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                     if(response.data.status == 'success'){

                        angular.forEach(response.data.listObject, function(value){
                               value.visitId = value.encounterId!=undefined ? value.encounterId:value.admissionId;
                               value.visitNo = value.encounterNo!=undefined ? value.encounterNo:value.admissionNo;
                        });
    //                    $scope.encounterList = response.data.listObject;
                        $scope.globalEncounterList = response.data.listObject;
                        angular.forEach($scope.globalEncounterList, function(item){
                            if($scope.visitTypeId == item.visitTypeId)
                            {
                                $scope.encounterList.push(item)
                            }
                        });

                        $rootScope.stopLoader();
                        var status = 'All';
                        var tabNum = 1;
                        $scope.getActiveFilesForPatient(status);
                     }
                     else{
                            console.log(response.data.message)
                            $rootScope.stopLoader();
                     }
                });
            }

        $scope.rackId="";
        $scope.saveMrdBtnCount = 0;
        $scope.saveMrd = function(patientInfo){
            $scope.saveMrdBtnCount ++;

            if($scope.saveMrdBtnCount == 1){
                if(patientInfo == undefined || patientInfo == null || patientInfo == '' ){
                    $scope.saveMrdBtnCount = 0;
                    growl.error('Please Select Patient!');
                    return false;
                }

                if($scope.updatePatientInfo != undefined){
                    if($scope.updatePatientInfo.allocationId != undefined && $scope.updatePatientInfo.allocationId != ''){
                        $scope.updateMrdAllocation($scope.updatePatientInfo);
                    }
                    else{
                        $scope.saveFileAllocation(patientInfo);
                    }
                }
                else{
                    $scope.saveFileAllocation(patientInfo);
                }
            }
        }

        $scope.saveFileAllocation = function(patientInfo){

            angular.forEach($scope.encounterList, function(value){
                if(parseInt($scope.visitId) == value.encounterId){
                        $scope.encounterId = value.encounterId;
                }
                else if(parseInt($scope.visitId) == value.admissionId){
                        $scope.admissionId = value.admissionId;
                }
            });

            $scope.mrdObj={};
            $scope.mrdObj.patientId = patientInfo.patientId ;
            $scope.mrdObj.rackId = parseInt($scope.rackId);
            $scope.mrdObj.boxId = parseInt($scope.boxId);
            $scope.mrdObj.rowId = parseInt($scope.rowId);
            $scope.mrdObj.fileNo = $scope.fileNo;
            $scope.mrdObj.encounterId = $scope.encounterId != undefined ? $scope.encounterId :0;
            $scope.mrdObj.admissionId = $scope.admissionId != undefined ? $scope.admissionId : 0;
            $scope.mrdObj.organizationId = $scope.organizationId;
            $scope.mrdObj.unitId = $scope.unitId ;
            $scope.mrdObj.createdBy = $scope.userId;
            $scope.mrdObj.status = "Available";

            if($scope.visitId == undefined || $scope.visitId == ''|| isNaN($scope.visitId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Visit!');
                return false;
            }
            if($scope.mrdObj.fileNo == undefined || $scope.mrdObj.fileNo == ''){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Enter File Number!');
                return false;
            }
            if($scope.mrdObj.boxId == undefined || $scope.mrdObj.boxId == ''|| isNaN($scope.mrdObj.boxId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Box!');
                return false;
            }
            if($scope.mrdObj.rackId == undefined || $scope.mrdObj.rackId == '' || isNaN($scope.mrdObj.rackId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Rack!');
                return false;
            }
            if($scope.mrdObj.rowId == undefined || $scope.mrdObj.rowId == '' || isNaN($scope.mrdObj.rowId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Row!');
                return false;
            }
            if($scope.mrdObj.encounterId == 0 && $scope.mrdObj.admissionId == 0){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Visit!');
                return false;
            }

            $rootScope.startLoader();
            var URI = BASE_URL + ROOT_URL + "/adt/mrd/saveMrdAllocation";

            GenericService.serviceAction("POST", URI, $scope.mrdObj).then(
            function (response) {
                 if(response.data.status == 'success'){
                    $rootScope.stopLoader();
                    $scope.clear();
                    $scope.saveMrdBtnCount = 0;
                    growl.success('File allocation saved successfully!');
                     $scope.getActiveFilesForPatient('All');
                 }
                 else{
                    $scope.saveMrdBtnCount = 0;
                    $rootScope.stopLoader();
                    growl.error(response.data.message);
                 }
            });
        }

        $scope.activeFilesList = [];
        $scope.getActiveFilesForPatient = function(status){
            var URI = BASE_URL + ROOT_URL + "/adt/mrd/getMrdFiles/"+$scope.patientInfo.patientId+"/"+$scope.unitId+"/"+$scope.organizationId+"/"+status;
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                 if(response.data.status == 'success'){

                    $scope.universalList = response.data.listObject;

                    $scope.filterList($scope.tabNo);
                    $rootScope.stopLoader();
                 }
                 else{
                    $scope.saveMrdBtnCount = 0;
                    console.log(response.data.message)
                    $rootScope.stopLoader();
                 }
            });
        }

        $scope.updateFileAllocation = function(patientInfo){
            $scope.updatePatientInfo = {};
            $scope.updatePatientInfo = patientInfo;
            $scope.rackId = patientInfo.rackId.toString();
            $scope.boxId = patientInfo.boxId.toString();
            $scope.rowId = patientInfo.rowId.toString();
            $scope.fileNo = patientInfo.fileNo;
            if($scope.visitTypeId>1){
                $scope.visitId =patientInfo.admissionId.toString();
            }
            else{
                $scope.visitId = patientInfo.encounterId.toString();
            }
        }

        $scope.clear = function(){
            $scope.rackId = '';
            $scope.boxId = '';
            $scope.rowId = '';
            $scope.fileNo = '';
            $scope.visitId = '';
            $scope.encounterId = 0;
            $scope.admissionId = 0;
            $scope.saveMrdBtnCount = 0;
            $scope.selectRequests = false;
            $scope.saveBtnFlag = false;
            $scope.disableSaveRequestBtn = false;
            $scope.selectAllRequests();
            $scope.acceptRequestList= [];
        }

        $scope.updateMrdAllocation = function(patientInfo){

            if(patientInfo == undefined|| patientInfo == null || patientInfo == '' ){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Patient!');
                return false;
            }
            $scope.updatePatientInfo = {};
            angular.forEach($scope.encounterList, function(value){
                if(parseInt($scope.visitId) == value.encounterId){
                        $scope.encounterId = value.encounterId;
                }
                else if(parseInt($scope.visitId) == value.admissionId){
                        $scope.admissionId = value.admissionId;
                }
            });

            $scope.mrdObj={};
            $scope.mrdObj.allocationId = patientInfo.allocationId;
            $scope.mrdObj.allocationNo = patientInfo.allocationNumber;
            $scope.mrdObj.patientId = patientInfo.patientId ;
            $scope.mrdObj.rackId = parseInt($scope.rackId);
            $scope.mrdObj.boxId = parseInt($scope.boxId);
            $scope.mrdObj.rowId = parseInt($scope.rowId);
            $scope.mrdObj.fileNo = $scope.fileNo;
            $scope.mrdObj.encounterId = $scope.encounterId != undefined ? $scope.encounterId :0;
            $scope.mrdObj.admissionId = $scope.admissionId != undefined ? $scope.admissionId : 0;
            $scope.mrdObj.organizationId = $scope.organizationId;
            $scope.mrdObj.unitId = $scope.unitId ;
            $scope.mrdObj.createdBy = $scope.userId;
            $scope.mrdObj.status = "Available";

            if($scope.visitId == undefined || $scope.visitId == ''|| isNaN($scope.visitId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Visit!');
                return false;
            }
            if($scope.mrdObj.fileNo == undefined || $scope.mrdObj.fileNo == ''){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Enter File Number!');
                return false;
            }
            if($scope.mrdObj.boxId == undefined || $scope.mrdObj.boxId == ''|| isNaN($scope.mrdObj.boxId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Box!');
                return false;
            }
            if($scope.mrdObj.rackId == undefined || $scope.mrdObj.rackId == '' || isNaN($scope.mrdObj.rackId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Rack!');
                return false;
            }
            if($scope.mrdObj.rowId == undefined || $scope.mrdObj.rowId == '' || isNaN($scope.mrdObj.rowId)){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Row!');
                return false;
            }
            if($scope.mrdObj.encounterId == 0 && $scope.mrdObj.admissionId == 0){
                $scope.saveMrdBtnCount = 0;
                growl.error('Please Select Visit!');
                return false;
            }

            $rootScope.startLoader();
            var URI = BASE_URL + ROOT_URL + "/adt/mrd/updateMrdAllocation";
            GenericService.serviceAction("PUT", URI, $scope.mrdObj).then(
            function (response) {
                 if(response.data.status == 'success'){
                    $scope.clear();

                    $rootScope.stopLoader();
                    growl.success('File allocation updated Successfully!');
                    $scope.getActiveFilesForPatient('All');
                 }
                 else{
                    $scope.saveMrdBtnCount = 0;
                    $rootScope.stopLoader();
                    growl.error(response.data.message);
                 }
            });
        }

        $scope.ShowModalPopup = function(){
            $scope.clickCount =0;
            $scope.selectAllFilesFlag = false;
            var status = 'Available';
                $scope.getAvailableFiles();
                var popup = angular.element('#getFilesList');
                popup.modal('show');
        }

         $scope.getAvailableFiles = function(){
            $scope.activeFilesList = [];
            angular.forEach($scope.universalList, function(item){
                if(item.fileStatus=='Available' && item.status =='A'){
                    item.isSelected = false;
                $scope.activeFilesList.push(item);
                }
            });
         }

        $scope.fileRequestList = [];
        $scope.selectAllFilesFlag = false;

        $scope.selectAllFiles = function(){

              if ($scope.selectAllFilesFlag) {
                    $scope.fileRequestList = $scope.activeFilesList.map(function (item) {
                    item.isSelected = true;
                    return item;
                });
              } else {
                $scope.fileRequestList.splice(0);
                $scope.activeFilesList.forEach(function (item) {
                  item.isSelected = false;
                });
              }
        }

         var isSelected = function (item) {
              return item.isSelected;
            }

        $scope.saveBtnFlag = false;
        $scope.selectFile = function (item) {
              var files = $scope.fileRequestList;

              if (item.isSelected) {
                files.push(item);
              } else {
                var index = files.indexOf(item);
                if (index > -1) {
                  files.splice(index, 1);
                }
              }

              if ($scope.activeFilesList.every(isSelected)) {
                $scope.selectAllFilesFlag = true;
              } else {
                $scope.selectAllFilesFlag = false;
              }
        }

        $scope.acceptRequestList = [];
        $scope.selectRequests = false;

        $scope.selectAllRequests = function(){
        var requestedList = [];
            angular.forEach($scope.globalList, function(item){
               if(item.fileStatus == 'Requested' && item.status == 'A'){
                    requestedList.push(item);
                }
            });
            if ($scope.selectRequests) {
                $scope.acceptRequestList = requestedList.map(function (item) {
                    item.isSelected = true;
                    return item;
                });
              } else {
                $scope.acceptRequestList.splice(0);
                requestedList.forEach(function (item) {
                  item.isSelected = false;
                });
              }
        }

        $scope.selectRequest = function(file){

            var requestedList = [];
            angular.forEach($scope.globalList, function(item){
                if(item.fileStatus == 'Requested' && item.status == 'A'){
                    requestedList.push(item);
                }
            });

            var files = $scope.acceptRequestList;

            if (file.isSelected) {
                files.push(file);
            } else {
                var index = files.indexOf(file);
                if (index > -1) {
                files.splice(index, 1);
                }
            }

            if (requestedList.every(isSelected)) {
                $scope.selectRequests = true;
            } else {
                $scope.selectRequests = false;
            }
        }

        $scope.clickCount =0;
        $scope.disableSaveRequestBtn = false;
        $scope.SaveRequest = function(){
            $scope.clickCount ++;
            $scope.disableSaveRequestBtn = true;
            if($scope.clickCount==1){
                if($scope.fileRequestList.length==0){
                    $scope.clickCount =0;
                    $scope.disableSaveRequestBtn = false;
                    growl.error("Please Select at least one File..!");
                    return false;
                }

                $('#getFilesList').modal('hide');
                $scope.activeFilesList = [];
                  $scope.selectAllFilesFlag = false;
                  angular.forEach($scope.fileRequestList, function(item){
                    if(item.isSelected){
                        item.isSelected = false;
                        item.fileStatus = "Requested";
                    }
                  });
                $scope.updateMRDStatus($scope.fileRequestList);
            }

        }

        $scope.issueSaveBtnClickCount = 0;
        $scope.acceptRequest = function(){

            $scope.issueSaveBtnClickCount ++;
            $scope.saveBtnFlag = true;
            if($scope.issueSaveBtnClickCount == 1){
                if($scope.acceptRequestList.length==0){
                    $scope.issueSaveBtnClickCount = 0;
                    growl.error("Please Select at least one file..!");
                    $scope.saveBtnFlag = false;
                    return false;
                }

                angular.forEach($scope.acceptRequestList, function(item){
                        item.fileStatus = "Issued";
                });

                $scope.updateMRDStatus($scope.acceptRequestList);
            }

        }

        $scope.cancelFileRequest = function(fileInfo){
            var cancelList = [];
            fileInfo.fileStatus = "Available";
            cancelList.push(fileInfo);
            $scope.updateMRDStatus(cancelList);
        }


        $scope.updateMRDStatus = function(filesList){
        $scope.saveBtnFlag = true;
        var listData = [];
        var fileStatus = "";
        angular.forEach(filesList, function(item){
            var data = {};
            data.allocationId = item.allocationId;
            data.fileStatus = item.fileStatus;
            fileStatus = item.fileStatus;
            data.updatedBy = $scope.userId;
            data.requestedNo = item.requestedNo !=undefined ? item.requestedNo:null;
            data.issuedNo = item.issuedNo != undefined ? item.issuedNo:null;
            data.requestedById = item.requestedById != undefined ? item.requestedById:null;
            listData.push(data)
        });

            var URI = BASE_URL + ROOT_URL + "/adt/mrd/updateMrdAllocationStatus";
            GenericService.serviceAction("PUT", URI, listData).then(
            function (response) {
               if(response.data.status == 'success'){
                  $scope.acceptRequestList = [];
                  $scope.fileRequestList = [];
                  $scope.returnFilesList = [];
                  $scope.discardFilesList = [];
                  $scope.selectRequests = false;

                   $scope.getActiveFilesForPatient('All');
                   $scope.clear();
                   $scope.clickCount =0;
                   $scope.issueSaveBtnClickCount = 0;
                  $rootScope.stopLoader();
                  growl.success('File Status updated Successfully!');
               }
               else{
                   $scope.acceptRequestList = [];
                   $scope.fileRequestList = [];
                   $scope.returnFilesList = [];
                    $scope.selectRequests = false;
                   $scope.discardFilesList = [];
                    $scope.saveBtnFlag = false;
                    $scope.disableSaveRequestBtn = false;
                   $scope.clickCount =0;
                   $scope.issueSaveBtnClickCount = 0;
                  $rootScope.stopLoader();
                  growl.error(response.data.message);
              }
            });
        }

        $scope.returnFile = function (file){
              $scope.returnFilesList = [];
              file.fileStatus = 'Available';
              $scope.returnFilesList.push(file);
              $scope.updateMRDStatus($scope.returnFilesList);
        }

        $scope.discardFile = function(file){
            $scope.discardFilesList = [];
            file.fileStatus = 'Discarded';
            $scope.discardFilesList.push(file);
            $scope.updateMRDStatus($scope.discardFilesList);
        }

        $scope.getRowClass = function(info) {

            if(info.fileStatus === 'Discarded' && $scope.tabNo == 4){
                return{
                    'disabled': false,
                    'td-bg-green': true,
                }
            }
            return {

                'disabled': info.status !== 'A',
                'td-bg-black': info.fileStatus === 'Available',
                'td-bg-green': info.fileStatus === 'Discarded',
                'td-bg-red': info.fileStatus === 'Issued',
                'td-bg-blue': info.fileStatus === 'Requested'
            };
        };

        $scope.getClassForColor = function(info)
        {
           if(info.fileStatus == 'Available'){
                return 'td-bg-black';
           }
           if(info.fileStatus == 'Discarded'){
                return 'td-bg-green';
           }
           if(info.fileStatus == 'Issued'){
              return 'td-bg-red';
           }
           if(info.fileStatus == 'Requested'){
              return 'td-bg-blue';
           }
        }

        $scope.CancelItems = function(){
            $scope.clickCount =0;
            $scope.disableSaveRequestBtn = false;
        }


        $scope.filterList = function(tabNum){
            $scope.globalList = [];
            $scope.tabNo = tabNum;

            angular.forEach($scope.universalList, function(item){
                if($scope.tabNo == 1 && (item.status == 'A')){
                    $scope.globalList.push(item);
                }
                if($scope.tabNo == 2 && (item.fileStatus == 'Requested')){
                    $scope.globalList.push(item);
                }
                if($scope.tabNo == 3 && (item.fileStatus == 'Issued')){
                    $scope.globalList.push(item);
                }
                if($scope.tabNo == 4 && (item.fileStatus == 'Discarded')){
                    $scope.globalList.push(item);
                }
            });

        }


        $scope.showPatientSearch = function(){

            $scope.patientSerachHideAndShow = true;

        }
            $scope.visitTypeId = 1;
            initializeController();
    }


    function config($stateProvider) {
        $stateProvider.state("MRDfileManagement", {
            url: "/MRDfileManagement",
            templateUrl: 'views/MRD-file-management.html',
            title:'MRD File Management',
            controller: "MRDfileManagementController.Controller",
            	     resolve : {
                                data: function($q, $state, $timeout) {
                                    var deferred = $q.defer();

                                    $timeout(function() {
                                        var arr = JSON.parse(localStorage.getItem('permissions'))
                                        .modules.filter(function(item){
                                           return (item.id == 1035);
                                        });
                                        if(arr.length <= 0){
                                            $state.go("accessDenied")
                                            deferred.reject();
                                         } else {
                                            deferred.resolve();
                                         }
                                    });

                                    return deferred.promise;
                                  }
                             },

            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("MRDfileManagementController.Controller", MRDfileManagementController);
})();