angular.module('myApp').service('CurrencyService',
  function ($rootScope, $q, GenericService, CONSTANTS,$cookies) {
    var currency;
    var GetCurrency = function () {
    	
    	var cookieObject = $cookies.getObject("cookieObject");
    	var unitId = null;
		if(cookieObject!=undefined){
			unitId = cookieObject.unitId;
		}
		
      var defer = $q.defer();

      if (!currency) {
        var SetCurrency = function (symbol) {
          currency = symbol;
        };

        var URI = CONSTANTS.GLOBAL.CURRENCY_API + '/' +unitId;
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            SetCurrency(response.data.symbol);
            defer.resolve(response.data.symbol);
          },
          function errorCallback(err) {
            defer.reject(err);
          });
      } else {
        defer.resolve(currency);
      }

      return defer.promise;
    }

    return {
      GetCurrency: GetCurrency
    };
  }
);