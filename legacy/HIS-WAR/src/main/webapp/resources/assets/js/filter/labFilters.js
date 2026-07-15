angular
    .module("myApp")
    .filter('getReportType', function () {
        return function (value) 
        {
        	if(value == 1)
    			return "Normal";
    		else if (value == 2)
    			return "Abnormal";
    		else if (value == 3)
    			return "Critical";
        };
    });

angular
.module("myApp")
.filter('vitalSort', function () {
    return function (list,obj) 
    {
    	console.log('list',list);
    	console.log('obj',obj);
    	angular.forEach(list, function(value, key) 
    			{
    		   if(value.vitalTime==obj.time && value.createdDate==obj.date && value.vitalId ==obj.vitalId)
    			   {
                      return obj;    			   
    			   }
    		   else {
    			   return {};  
    		   }
    		});
    	
    };
});


angular
.module("myApp").filter('setDecimal', function ($filter) {
    return function (input,obj) 
    {
       if (isNaN(input)) return "";
        if(obj.resultTypeFlag==1)
        {
          if(obj.paramPrecision==1)
         {
             return Math.round(input * 10) / 10;
         }
          else{
            //  var factor = "1" + Array(+(obj.flowtingPt > 0 && obj.flowtingPt + 1)).join("0");
            //    return Math.round(input * factor) / factor;
             var res = Number.parseFloat(input).toFixed(obj.paramPrecision);
             if (isNaN(res)) return "";
             else return res;
          }
        }
    };
});


angular
.module("myApp").filter('setDec', function ($filter) {
    return function (input, places) 
    {
        if (isNaN(input)||input==null)
        	{
        	  return parseFloat(0).toFixed(2);
        	};
      var res = Number.parseFloat(input).toFixed(places);
             if (isNaN(res)) return parseFloat(0).toFixed(2);
             else return res;
    };
});