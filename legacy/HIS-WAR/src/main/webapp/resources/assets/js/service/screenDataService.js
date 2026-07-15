angular.module('myApp').service('screenDataService', ['$rootScope', function ($rootScope) {
    var setData = [];
    
    var setScreenData = function (data) {
        setData.push(data) ;
    };

    var getScreenData = function () {
        return setData;
    };

    return {
        setScreenData: setScreenData,
        getScreenData: getScreenData,
    };
}]);