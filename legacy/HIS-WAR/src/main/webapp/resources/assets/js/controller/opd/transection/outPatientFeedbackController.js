(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function OutPatientFeedback($scope, $rootScope, $http, GenericService, growl,organizationId,unitId,patientId,visitType,visitTypeId,encounterId) {
        var self = this;
        $scope.feedbackSelectedList = [];
        $scope.feedbackList = [];
        $scope.userId="";
        console.log("window.location.href-------",window.location.href);
        const url = window.location.href;
        const parts = url.split("/");
        $scope.encounterId=Number(parts[parts.length-1]);
        $scope.visitTypeId=parts[parts.length-2];
        $scope.visitType=parts[parts.length-3];
        $scope.patientId=Number(parts[parts.length-4]);
        $scope.organizationId=Number(parts[parts.length-5]);
        $scope.unitId=Number(parts[parts.length-6]);
        console.log($scope.visitTypeId,$scope.visitType,$scope.patientId,$scope.organizationId,$scope.unitId,$scope.encounterId);
        $scope.isAlreadyFilledUp = false;
        $scope.isSectionOfHospitalFilled = "";
        $scope.isSuggestionsFilled="";

        $scope.selectStar = function(event, type){
//            function selectStar(event, type) {
            const stars = event.currentTarget.querySelectorAll("input[type='radio']");
            const clickedIndex = Array.from(stars).indexOf(event.target);
            for (let i = 0; i <= clickedIndex; i++) {
                stars[i].checked = true;
            }
        }

        function initializeController() {
            console.log("OutPatientFeedback....😉");

            $scope.sectionOfHospitalFun = function(value){
                var obj ={
                    key:0,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("easeOfGettingFun=======",value);
            }
            $scope.easeOfGettingFun = function(value){
                var obj ={
                    key:1,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("easeOfGettingFun=======",value);
            }
            $scope.informationRegardingFun = function(value){
                var obj ={
                    key:2,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("informationRegardingFun=======",value);
            }
            $scope.warmthCourtesyFun = function(value){
                var obj ={
                    key:3,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("warmthCourtesyFun=======",value);
            }
            $scope.comfortWaitingFun = function(value){
                var obj ={
                    key:4,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("comfortWaitingFun=======",value);
            }
            $scope.timeTakenFun = function(value){
                var obj ={
                    key:5,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("timeTakenFun=======",value);
            }
            $scope.positionInformationFun = function(value){
                var obj ={
                    key:6,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("positionInformationFun=======",value);
            }
            $scope.consultantDoctorFun = function(value){
                var obj ={
                    key:7,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("consultantDoctorFun=======",value);
            }
            $scope.pharmacyModelFun = function(value){
                var obj ={
                    key:8,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("pharmacyModelFun=======",value);
            }
            $scope.pathologyModelFun = function(value){
                var obj ={
                    key:9,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("pathologyModelFun=======",value);
            }
            $scope.radiologyModelFun = function(value){
                var obj ={
                    key:10,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("radiologyModelFun=======",value);
            }
            $scope.cleanlinessRatingFun = function(value){
                var obj ={
                    key:11,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("cleanlinessRatingFun=======",value);
            }
            $scope.experienceRatingFun = function(value){
                var obj ={
                    key:12,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("cleanlinessRatingFun=======",value);
            }
            $scope.suggestionsFun = function(value){
                var obj ={
                    key:13,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("easeOfGettingFun=======",value);
            }

            $scope.updateOrPushObject = function(obj){
                var foundIndex = -1;

                for (var i = 0; i < $scope.feedbackSelectedList.length; i++) {
                    if ($scope.feedbackSelectedList[i].key === obj.key) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.feedbackSelectedList[foundIndex] = obj;
                } else {
                    // Push the new object to the array
                    $scope.feedbackSelectedList.push(obj);
                }
                console.log("$scope.feedbackSelectedList=======",$scope.feedbackSelectedList);

            }

//            $scope.getOutPatientFeedback();

            $scope.submitForm = function(){
                $rootScope.startLoader();
                $scope.isSectionOfHospitalFilled = $scope.feedbackSelectedList.some(item => item.key === 0);
                $scope.isSuggestionsFilled = $scope.feedbackSelectedList.some(item => item.key === 13);

                if($scope.isSectionOfHospitalFilled=="" || $scope.isSectionOfHospitalFilled == undefined || $scope.isSectionOfHospitalFilled == null){
                    growl.warning("Please enter which section did you visited...!",{title : "WARNING!"});
                    $rootScope.stopLoader();
                    return false;
                }
                if($scope.isSuggestionsFilled=="" || $scope.isSuggestionsFilled == undefined || $scope.isSuggestionsFilled == null){
                    growl.warning("Please enter any suggestions...!",{title : "WARNING!"});
                    $rootScope.stopLoader();
                    return false;
                }

                var obj = {
                	encounterId	: $scope.encounterId,
                    patientId:$scope.patientId,
                    visitType:$scope.visitType,
                    visitTypeId:$scope.visitTypeId,
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    feedbackSelectedList:$scope.feedbackSelectedList,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI = BASE_URL + ROOT_URL +"/api/outPatientFeedback/saveOutPatientFeedback";

                console.log("obj========",obj);

                GenericService.serviceAction("POST", URI, obj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         $scope.getOutPatientFeedback();
                         growl.success(response.data.message,
                         {
                            title : 'Success!'
                         });
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });

            }

            $scope.getOutPatientFeedback = function(){

                $rootScope.startLoader();
                var URI1 = BASE_URL + ROOT_URL +"/api/outPatientFeedback/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.patientId+"/"+$scope.visitType+"/"+$scope.visitTypeId+"/"+$scope.encounterId;

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.feedbackList = [];
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                        $scope.feedbackList = response.data.listObject;
                        $scope.encounterId = response.data.object;
                        if($scope.feedbackList.length>0){
                            $scope.isAlreadyFilledUp = true;
                            angular.forEach($scope.feedbackList, function(value,id) {
                                $scope.setOutPatientFeedbackList(value,id);
                            });
                        }

                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.getOutPatientFeedback();

            $scope.setOutPatientFeedbackList = function(value,id){

                if(value.feedbackKey==0){
                    $scope.sectionOfHospital = value.feedbackValue;
                }else if(value.feedbackKey==1){
                    $scope.easeOfGetting = Number(value.feedbackValue);
                }else if(value.feedbackKey==2){
                    $scope.informationRegarding = Number(value.feedbackValue);
                }else if(value.feedbackKey==3){
                    $scope.warmthCourtesy = Number(value.feedbackValue);
                }else if(value.feedbackKey==4){
                    $scope.comfortWaiting = Number(value.feedbackValue);
                }else if(value.feedbackKey==5){
                    $scope.timeTaken = Number(value.feedbackValue);
                }else if(value.feedbackKey==6){
                    $scope.positionInformation = Number(value.feedbackValue);
                }else if(value.feedbackKey==7){
                    $scope.consultantDoctor = Number(value.feedbackValue);
                }else if(value.feedbackKey==8){
                    $scope.pharmacyModel = Number(value.feedbackValue);
                }else if(value.feedbackKey==9){
                    $scope.pathologyModel = Number(value.feedbackValue);
                }else if(value.feedbackKey==10){
                    $scope.radiologyModel = Number(value.feedbackValue);
                }else if(value.feedbackKey==11){
                    $scope.cleanlinessRating = Number(value.feedbackValue);
                }else if(value.feedbackKey==12){
                    $scope.experienceRating = Number(value.feedbackValue);
                }else if(value.feedbackKey==13){
                    $scope.suggestions = value.feedbackValue;
                }

            }

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("outPatientFeedback", {
            url: '/api/outPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:encounterId',
            title: 'OutPatient Feedback',
            templateUrl: 'views/AppointmentScheduling/outPatient-feedback.html',
            controller: "OutPatientFeedback.Controller",
            controllerAs: "vm",
            resolve : {
                 organizationId: function($stateParams, GenericService)
                    {
                        return $stateParams.organizationId;
                    },
                    unitId: function($stateParams, GenericService)
                    {
                        return $stateParams.unitId;
                    },
                    patientId: function($stateParams, GenericService)
                    {
                        return $stateParams.patientId;
                    },
                    visitType: function($stateParams, GenericService)
                    {
                        return $stateParams.cabinId;
                    },
                    visitTypeId: function($stateParams, GenericService)
                    {
                        return $stateParams.cabinId;
                    },
                    encounterId: function($stateParams, GenericService)
                    {
                        return $stateParams.cabinId;
                    }
             }
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("OutPatientFeedback.Controller", OutPatientFeedback);
})();