(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function InPatientFeedback($scope, $rootScope, $http, GenericService, growl,organizationId,unitId,patientId,visitType,visitTypeId,admissionId) {
        var self = this;
        $scope.feedbackSelectedList = [];
        $scope.feedbackList = [];
        $scope.userId="";
        console.log("window.location.href-------",window.location.href);
        const url = window.location.href;
        const parts = url.split("/");
        $scope.admissionId=Number(parts[parts.length-1]);
        $scope.visitTypeId=Number(parts[parts.length-2]);
        $scope.visitType=parts[parts.length-3];
        $scope.patientId=Number(parts[parts.length-4]);
        $scope.organizationId=Number(parts[parts.length-5]);
        $scope.unitId=Number(parts[parts.length-6]);
        console.log($scope.visitTypeId,$scope.visitType,$scope.patientId,$scope.organizationId,$scope.unitId,$scope.admissionId);
        $scope.isAlreadyFilledUp = false;
        $scope.isSuggestionsFilled = "";

        $scope.selectStar = function(event, type){
//            function selectStar(event, type) {
            const stars = event.currentTarget.querySelectorAll("input[type='radio']");
            const clickedIndex = Array.from(stars).indexOf(event.target);
            for (let i = 0; i <= clickedIndex; i++) {
                stars[i].checked = true;
            }
        }

        function initializeController() {
            console.log("InPatientFeedback....😉");

            $scope.politeFriendlyHelpfulFun = function(value){
                var obj ={
                    key:0,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("politeFriendlyHelpfulFun=======",value);
            }
            $scope.admissionExplanationFun = function(value){
                var obj ={
                    key:1,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("admissionExplanationFun=======",value);
            }
            $scope.admissionReceivingFun = function(value){
                var obj ={
                    key:2,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("admissionReceivingFun=======",value);
            }
            $scope.attentivenessResponseFun = function(value){
                var obj ={
                    key:3,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("attentivenessResponseFun=======",value);
            }
            $scope.medicationTimelyFun = function(value){
                var obj ={
                    key:4,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("medicationTimelyFun=======",value);
            }
            $scope.communicationClarityFun = function(value){
                var obj ={
                    key:5,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("communicationClarityFun=======",value);
            }
            $scope.roomCleanlinessFun = function(value){
                var obj ={
                    key:6,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("roomCleanlinessFun=======",value);
            }
            $scope.servicePromptFun = function(value){
                var obj ={
                    key:7,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("servicePromptFun=======",value);
            }
            $scope.friendlyPoliteFun = function(value){
                var obj ={
                    key:8,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("friendlyPoliteFun=======",value);
            }
            $scope.conditionExplanationFun = function(value){
                var obj ={
                    key:9,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("conditionExplanationFun=======",value);
            }
            $scope.concernedCourteousFun = function(value){
                var obj ={
                    key:10,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("concernedCourteousFun=======",value);
            }
            $scope.approximateCounsellingFun = function(value){
                var obj ={
                    key:11,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("approximateCounsellingFun=======",value);
            }
            $scope.qualityFoodFun = function(value){
                var obj ={
                    key:12,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("qualityFoodFun=======",value);
            }
            $scope.serviceOnTimeFun = function(value){
                var obj ={
                    key:13,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("serviceOnTimeFun=======",value);
            }
            $scope.counsellingDietFun = function(value){
                var obj ={
                    key:14,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("counsellingDietFun=======",value);
            }
            $scope.instructionsClearFun = function(value){
                var obj ={
                    key:15,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("instructionsClearFun=======",value);
            }
            $scope.clarificationExplanationFun = function(value){
                var obj ={
                    key:16,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("clarificationExplanationFun=======",value);
            }
            $scope.billingCompletingFun = function(value){
                var obj ={
                    key:17,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("billingCompletingFun=======",value);
            }
            $scope.cleanlinessRatingFun = function(value){
                var obj ={
                    key:18,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("cleanlinessRatingFun=======",value);
            }
            $scope.experienceRatingFun = function(value){
                var obj ={
                    key:19,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("experienceRatingFun=======",value);
            }
            $scope.suggestionsFun = function(value){
                var obj ={
                    key:20,
                    value:value
                }
                $scope.updateOrPushObject(obj);
                console.log("suggestionsFun=======",value);
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


            $scope.submitForm = function(){
                $rootScope.startLoader();
                $scope.isSuggestionsFilled = $scope.feedbackSelectedList.some(item => item.key === 20);

                if($scope.isSuggestionsFilled=="" || $scope.isSuggestionsFilled == undefined || $scope.isSuggestionsFilled == null){
                    growl.warning("Please enter any suggestions...!",{title : "WARNING!"});
                    $rootScope.stopLoader();
                    return false;
                }

                var obj = {
                	admissionId:$scope.admissionId,
                	patientId:$scope.patientId,
                    visitType:$scope.visitType,
                    visitTypeId:$scope.visitTypeId,
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    feedbackSelectedList:$scope.feedbackSelectedList,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI = BASE_URL + ROOT_URL +"/api/inPatientFeedback/saveInPatientFeedback";

                console.log("obj========",obj);

                GenericService.serviceAction("POST", URI, obj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         $scope.getInPatientFeedback();
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


            $scope.getInPatientFeedback = function(){

                $rootScope.startLoader();
                var URI1 = BASE_URL + ROOT_URL +"/api/inPatientFeedback/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.patientId+"/"+$scope.visitType+"/"+$scope.visitTypeId+"/"+$scope.admissionId;

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.feedbackList = [];
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                        $scope.feedbackList = response.data.listObject;
                        $scope.admissionId = response.data.object;
                        if($scope.feedbackList.length>0){
                            $scope.isAlreadyFilledUp = true;
                            angular.forEach($scope.feedbackList, function(value,id) {
                                $scope.setInPatientFeedbackList(value,id);
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

            $scope.getInPatientFeedback();

            $scope.setInPatientFeedbackList = function(value,id){

                if(value.feedbackKey==0){
                    $scope.politeFriendlyHelpful = value.feedbackValue;
                }else if(value.feedbackKey==1){
                    $scope.admissionExplanation = Number(value.feedbackValue);
                }else if(value.feedbackKey==2){
                    $scope.admissionReceiving = Number(value.feedbackValue);
                }else if(value.feedbackKey==3){
                    $scope.attentivenessResponse = Number(value.feedbackValue);
                }else if(value.feedbackKey==4){
                    $scope.medicationTimely = Number(value.feedbackValue);
                }else if(value.feedbackKey==5){
                    $scope.communicationClarity = Number(value.feedbackValue);
                }else if(value.feedbackKey==6){
                    $scope.roomCleanliness = Number(value.feedbackValue);
                }else if(value.feedbackKey==7){
                    $scope.servicePrompt = Number(value.feedbackValue);
                }else if(value.feedbackKey==8){
                    $scope.friendlyPolite = Number(value.feedbackValue);
                }else if(value.feedbackKey==9){
                    $scope.conditionExplanation = Number(value.feedbackValue);
                }else if(value.feedbackKey==10){
                    $scope.concernedCourteous = Number(value.feedbackValue);
                }else if(value.feedbackKey==11){
                    $scope.approximateCounselling = Number(value.feedbackValue);
                }else if(value.feedbackKey==12){
                    $scope.qualityFood = Number(value.feedbackValue);
                }else if(value.feedbackKey==13){
                    $scope.serviceOnTime = value.feedbackValue;
                }else if(value.feedbackKey==13){
                    $scope.serviceOnTime = value.feedbackValue;
                }else if(value.feedbackKey==14){
                    $scope.counsellingDiet = value.feedbackValue;
                }else if(value.feedbackKey==15){
                    $scope.instructionsClear = value.feedbackValue;
                }else if(value.feedbackKey==16){
                    $scope.clarificationExplanation = value.feedbackValue;
                }else if(value.feedbackKey==17){
                    $scope.billingCompleting = value.feedbackValue;
                }else if(value.feedbackKey==18){
                    $scope.cleanlinessRating = value.feedbackValue;
                }else if(value.feedbackKey==19){
                    $scope.experienceRating = value.feedbackValue;
                }else if(value.feedbackKey==20){
                    $scope.suggestions = value.feedbackValue;
                }

            }


        }

        initializeController();

    }

    function config($stateProvider) {
        $stateProvider.state("inPatientFeedback", {
            url: "/api/inPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:admissionId",
            title: 'InPatient Feedback',
            templateUrl: 'views/AppointmentScheduling/inPatient-feedback.html',
            controller: "InPatientFeedback.Controller",
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
                        return $stateParams.cabinId;
                    },
                    visitType: function($stateParams, GenericService)
                    {
                        return $stateParams.cabinId;
                    },
                    visitTypeId: function($stateParams, GenericService)
                    {
                        return $stateParams.cabinId;
                    },
                    admissionId: function($stateParams, GenericService)
                    {
                        return $stateParams.admissionId;
                    }
             }
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("InPatientFeedback.Controller", InPatientFeedback);
})();