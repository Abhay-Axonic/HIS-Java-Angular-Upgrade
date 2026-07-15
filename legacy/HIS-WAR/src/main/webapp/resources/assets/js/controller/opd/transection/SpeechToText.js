// Speech Recognition
//var message;
var final_transcript = '';
var recognizing = false;
var ignore_onend;
var start_timestamp;
var focuselement;
var SpeechGrammarList = SpeechGrammarList || webkitSpeechGrammarList


function callvoice1(){
	//alert("dd"+document.getElementById("voicetext").value);
	
	var arr= document.getElementById("voicetext").value.trim().replace( /\n/g, " " ).split( " " );
	getSuccessOutput(document.getElementById("voicetext").value.trim());
	
	/*if(arr[0].indexOf("complain")!=-1){
		console.log("inside complain");
	
		if(arr[1].indexOf("chest")!=-1){
			console.log("inside chest");
			document.getElementById("icpcsystem").value=1;
			document.getElementById("icpc").value='Chest pain NOS|A11';
			document.getElementById("since").value=arr[2];
			document.getElementById("comp_day_id").value="Day";
			document.getElementById("remark").value="remark";
			document.getElementById("icpcId").value=11
			document.getElementById("icpcSystemId").value=1;
		}else if(arr[1].indexOf("fever")!=-1){
			console.log("inside fever");
			document.getElementById("icpcsystem").value=1;
			document.getElementById("icpc").value='Fever|A03';
			document.getElementById("since").value=arr[2];
			document.getElementById("comp_day_id").value="Day";
			document.getElementById("remark").value="remark";
			document.getElementById("icpcId").value=3
			document.getElementById("icpcSystemId").value=1;
		}
		else if(arr[1].indexOf("headache")!=-1){
			console.log("neurological");
			document.getElementById("icpcsystem").value=8;
			document.getElementById("icpc").value='Headache|N01';
			document.getElementById("since").value=arr[2];
			document.getElementById("comp_day_id").value="Day";
			document.getElementById("remark").value="remark";
			document.getElementById("icpcId").value=292
			document.getElementById("icpcSystemId").value=8;
		}
		document.getElementById("savecomplaint").click();
	
	
	
	}else{
	
	console.log("call consult");
	//if(arr[0].indexOf("consult")!=-1){
		console.log("inside consult0");
		document.getElementById("unit").value=17;
		document.getElementById("department").value=10;
		document.getElementById("doctor").value=2202;
		document.getElementById("priority").value='Normal';
		console.log("inside consult");
		document.getElementById("addConsultOrder").click();
	}*/
	
	
	
		
}

        $scope.generateSampleCollection = function(mapList)
            {

                if(mapList!=null)
                  {
                                $state.go('sampleCollection',
                                    {
                                     obj : mapList,
                                    });
                  }
            else
               {
                    growl.error("Something went Wrong.",{title : 'Error!'});
               }

            }

function getSuccessOutput(message) {

	$.ajax({
	type: 'GET',

	url: 'api/opd/callAI/'+message,

	contentType: 'text/plain',
	headers: {
	       
	        'Access-Control-Allow-Origin':'*',
	        'Access-Control-Allow-Methods': 'HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE',
	        'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
	        'Access-Control-Allow-Headers': 'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
	          
	         'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD'
	        
	    },
	xhrFields: {
	withCredentials: false
	},



	success: function(data) {
	//alert(data);
	//var res= data.responseText;
	document.getElementById("jsonmessage").value=data;
	document.getElementById("savecomplaint").click();

	},

	error: function(resp) {
	//alert('error--'+resp);
	//alert(new Date())
	//var res= '{"ICPC_system":"General and Unspecified","ICPC":"Fever","duration":"2","durationtype":"2 days","classificationtype":"Symptoms/Complaints","ICPC_code":"A03","ICPC_System_code":"A"}';
	//alert(res);
	var res="";
	alert("Voice Issue . Please use manual save");

	//document.getElementById("jsonmessage").value=res;
	//document.getElementById("savecomplaint").click();

	}


	});
	}
/*function getSuccessOutput(message) {


$.ajax({
	type: 'GET',
	url: 'https://www.neuronet.ai/raptr/getComplaints?usersays='+message,
	
	contentType: 'text/plain',
	headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Origin':'*',
        'Access-Control-Allow-Methods': 'HEAD, GET, POST, PUT, PATCH, DELETE',
        'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token'
        	
    },
	xhrFields: {
		withCredentials: false
	},

	

	success: function(data) {
		var res= data.responseText;
		document.getElementById("jsonmessage").value=res;
		document.getElementById("savecomplaint").click();
		
	},

	error: function() {
		
		var res="";
		alert("Voice Issue . Please use manual save");
		
		
	}
	
	
});
}*/

try{
var monitor = setInterval(function(){
	var frames = document.getElementsByTagName('iframe');
	for (var i = 0; i < frames.length; i++) {
	    if (!frames[i].id) {
	    	if(frames[i].contentDocument.children[0].children[1]!=undefined && frames[i].contentDocument.children[0].children[1].children[0]!=undefined){
	    	frames[i].contentDocument.children[0].children[1].children[0].addEventListener("mouseover",function(event){
	    		focuselement=event.target;
	    		writenotes='';
	    		final_transcript='';
	    		previousValue=event.target.innerText.trim();
	    		
	    	});
	    	frames[i].contentDocument.children[0].children[1].children[0].addEventListener("click",function(event){
	    		focuselement=event.target;
	    		writenotes='';
	    		final_transcript='';
	    		previousValue=event.target.innerText.trim();
	    	});
	    	}
	    }
	}
}, 100);
}catch(e){
	
}




$("#click").on('click', function() {
        var $target = $("#ts");
        var $clone = $target.clone().removeAttr('id');
        $clone.val($target.val()).css({
            overflow: "auto",
            position: 'absolute',
            'z-index': 999,
            left: $target.offset().left,
            top: $target.offset().top + $target.outerHeight(),
            width: $target.outerWidth()
        }).attr('size', $clone.find('option').length > 10 ? 10 : $clone.find('option').length).change(function() {
            $target.val($clone.val());
        }).on('click blur keypress',function(e) {
         if(e.type !== "keypress" || e.which === 13)
            $(this).remove();
        });
        $('secondtabdiv2').append($clone);
        $clone.focus();
    });


/* function readTextFile(file, callback) {
    var rawFile = new XMLHttpRequest();
    rawFile.overrideMimeType("application/json");
    rawFile.open("GET", file, true);
    rawFile.onreadystatechange = function() {
        if (rawFile.readyState === 4 && rawFile.status == "200") {
            callback(rawFile.responseText);
        }
    }
    rawFile.send(null); 
}
var jsonarr;
readTextFile("test.json", function(text){
    jsonarr = JSON.parse(text);
    
}); */

var jsonarr=  {
  
  "nameid": [
    
	{ "name":"voicetext","id":"voicetext" ,"tab":"secondtab2","type":"text" },
	{ "name":"save","id":"save" ,"tab":"secondtab2","type":"function" },
	{ "name":"sieve","id":"save" ,"tab":"secondtab2","type":"function" }
	/*{ "name":"medical","id":"medical" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"treatment","id":"treatment" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"family","id":"family" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"surgical","id":"surgical" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"medication history","id":"medication" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"medical","id":"medical" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"immunization","id":"immunization" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"immunisation","id":"immunization" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"travel","id":"travel" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"progress note","id":"progressnote" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"complaint","id":"complaints" ,"tab":"secondtab2","type":"anchor" },
	
	
	{ "name":"habit","id":"habits" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"habits","id":"habits" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"examination","id":"examination" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"observation","id":"observation" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"letter","id":"letter" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"allergy","id":"allergy" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"complaint","id":"complaint" ,"tab":"secondtab2","type":"anchor" },
	
	
	{ "name":"diagnosis","id":"diagnosis" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"old encounter","id":"oldencounter" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"old","id":"oldencounter" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"procedure order","id":"procedureorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"investigation","id":"investigation" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"advice medication","id":"advicemedication" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"advise medication","id":"advicemedication" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"advice","id":"advice" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"advise","id":"advice" ,"tab":"secondtab2","type":"anchor" },
	
	
	{ "name":"history medication","id":"historymedication" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"history medication","id":"historymedication" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"history","id":"history" ,"tab":"secondtab2","type":"anchor" },
	
	
	
	{ "name":"infusion","id":"infusion" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"instruction","id":"instruction" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"consult order","id":"consultorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"consult","id":"consultorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"order set","id":"orderset" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"package order","id":"packageorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"blood component","id":"bloodcomponent" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"diet order","id":"dietorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"nursing order","id":"nursingorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"note","id":"idNote1" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"special need","id":"idSpecialNeeds1" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"special","id":"idSpecialNeeds1" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"notification","id":"idNotification1" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"search","id":"searchdischarge" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"save","id":"save" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"safe","id":"save" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"say","id":"save" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"print","id":"print" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"ok","id":"ok" ,"tab":"secondtab2","type":"button" },
	{ "name":"cancel","id":"cancel" ,"tab":"secondtab2","type":"button" },
	{ "name":"discharge summary","id":"dischargeSummary" ,"tab":"","type":"redirect" },
	{ "name":"doctor desk","id":"doctorDesk" ,"tab":"","type":"redirect" },
	{ "name":"bed management","id":"bedManagement" ,"tab":"","type":"redirect" },
	{ "name":"history medication","id":"medication" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"investigation","id":"investigation" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"tab instruction","id":"tabinstruction" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"admission","id":"admission" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"procedure","id":"procedureorder" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"vaccination","id":"vaccination" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"growth chart","id":"growthchart" ,"tab":"secondtab2","type":"anchor" },
	
	
	
	{ "name":"case sheet","id":"casesheet" ,"tab":"secondtab2","type":"anchor" },
	
	{ "name":"encounter","id":"encounter" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"vitals","id":"vitals" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"vital","id":"vitals" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"scores","id":"scores" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"score","id":"scores" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"general examination","id":"generalexamination" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"physical examination","id":"physicalexamination" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"general","id":"generalexamination" ,"tab":"secondtab2","type":"anchor" },
	{ "name":"physical","id":"physicalexamination" ,"tab":"secondtab2","type":"anchor" }*/
	
	
	//------------------
    
  ]
 }

if (!('webkitSpeechRecognition' in window)) {
  //message.innerHTML = 'Web Speech API is not supported by this browser. Upgrade to <a href="//www.google.com/chrome">Chrome</a> version 25 or later.';
} else {
	var colors = [ 'aqua' , 'azure' , 'beige', 'bisque', 'black', 'blue', 'brown', 'chocolate', 'coral', 'crimson', 'cyan', 'fuchsia', 'ghostwhite', 'gold', 'goldenrod', 'gray', 'green', 'indigo', 'ivory', 'khaki', 'lavender', 'lime', 'linen', 'magenta', 'maroon', 'moccasin', 'navy', 'olive', 'orange', 'orchid', 'peru', 'pink', 'plum', 'purple', 'red', 'salmon', 'sienna', 'silver', 'snow', 'tan', 'teal', 'thistle', 'tomato', 'turquoise', 'violet', 'white', 'yellow'];
	var grammar = '#JSGF V1.0; grammar colors; public <color> = ' + colors.join(' | ') + ' ;'
	
  var recognition = new webkitSpeechRecognition();
  var speechRecognitionList = new SpeechGrammarList();
  speechRecognitionList.addFromString(grammar, 1);
  recognition.grammars = speechRecognitionList;
 
  recognition.lang = 'en-US';
  //recognition.interimResults = false;
  recognition.maxAlternatives = 1;
  recognition.continuous = true;
  recognition.interimResults = true;

  recognition.onstart = function() {
    recognizing = true;
   // message.innerHTML = 'Speak now.';
    //talk_button.innerHTML = 'Listen';
  };
var writenotes='voicetext';
var dropdownname='';
var jsonarrnumber='';
var dropdownval;
var type;
var tmpVar='';
var tmpId;
var tmpName;
var previousType;
var previousValue='';
  recognition.onresult = function(event) {
    var interim_transcript = '';
	var write=true;
	
	var final;
	var onlyfocus=false;
	
    for (var i = event.resultIndex; i < event.results.length; ++i) {
    	
      if (event.results[i].isFinal) {
    	  final=true;
		console.log(event.results[i][0].transcript);
		//document.getElementById("micadd").innerHTML=event.results[i][0].transcript;
		if(event.results[i][0].transcript.indexOf('stop recording')!=-1){
			recognition.stop();
			write=false;
		}
		if(event.results[i][0].transcript.indexOf('clear')!=-1){
			final_transcript=final_transcript.substring(0,final_transcript.lastIndexOf(" "));
		}else if(event.results[i][0].transcript.indexOf('reset')!=-1){
			final_transcript="";
			previousValue="";
		}
		else{
			final_transcript += event.results[i][0].transcript;
		}
		
		//readTextFile("resources/js/test.json", function(text){
		 //   var data = JSON.parse(text);
		  //  console.log(data);
		    
		    for (speech in jsonarr.nameid) {
		    
		    	onlyfocus=false;
		    	 // alert(event.results[i][0].transcript+jsonarr.nameid[j].name+jsonarr.nameid[j].id);
		    	  if(jsonarr.nameid[speech].name!="" && event.results[i][0].transcript.trim().toLowerCase().indexOf(jsonarr.nameid[speech].name.trim().toLowerCase())!=-1){
		    			//document.getElementById(tabactive).className='nav-link';
		    				//document.getElementById('navpills-3').className='nav-link  active show';
		    				//tabactive='navpills-3';
		    				gototab(document.getElementById(jsonarr.nameid[speech].tab),50,jsonarr.nameid[speech].id,jsonarr.nameid[speech].type);
		    				writenotes=jsonarr.nameid[speech].id;
							type=jsonarr.nameid[speech].type;
							
							//console.log("final type inside cond="+type+"id="+writenotes);
		    				final_transcript='';
		    				write=false;
		    				onlyfocus=true;
							if(type=="dropdown"){
							//$("#"+writenotes).attr('size', 5);
								dropdownname=writenotes;
								jsonarrnumber=speech;
								previousType=type;
    
							}else if(type=="checkbox"){
								dropdownname='';
								$("#"+writenotes).checkbox=true;
								
							}
							else if(type=="anchor" || type=="button" ){
								dropdownname='';
								document.getElementById(writenotes).click();
								
							}
							else if(type=="redirect"){
								dropdownname='';
								window.location.href="#/"+jsonarr.nameid[speech].id;
							}
							else if(type=="function"){
								callvoice1();
								
								break;
							}
							else{ // for number and input
								dropdownname='';
								previousValue=document.getElementById(jsonarr.nameid[speech].id).value;
							}
							break;
		    		} 
		    	  
		    	}
		    if(dropdownname!=''){
		    	dropdownval=jsonarr.nameid[jsonarrnumber].keyval[event.results[i][0].transcript.trim().toLowerCase()];
		    	
		    }
		//});
		
        
       // vc_search(final_transcript);
      } else {
    	  
        interim_transcript += event.results[i][0].transcript;
        final=false;
        console.log("interim trans"+interim_transcript);
        
        tmpId=writenotes;
        for (speech in jsonarr.nameid) {
		    
	    	onlyfocus=false;
	    	 // alert(event.results[i][0].transcript+jsonarr.nameid[j].name+jsonarr.nameid[j].id);
	    	  if(jsonarr.nameid[speech].name!="" && interim_transcript.trim().toLowerCase().indexOf(jsonarr.nameid[speech].name.trim().toLowerCase())!=-1){
	    		  onlyfocus=true;
	    		  type=jsonarr.nameid[speech].type;
					//console.log(" interim type else ="+type);
	    		  //console.log("interim trans focus true "+interim_transcript);
	    		  if(type=='input'){
	    		  previousValue=document.getElementById(jsonarr.nameid[speech].id).value;
	    		  }
	    		  else if(type=="dropdown"){
						
						previousType=type;

					}
	    		  break;
	    	  }
        } 
        
   //	document.getElementById(writenotes).value=interim_transcript;
        if(dropdownname!=''){
        	dropdownval=jsonarr.nameid[jsonarrnumber].keyval[event.results[i][0].transcript.trim().toLowerCase()]
        }
      }
    }
	if(writenotes='voicetext'){
		type="input";
	}
	if(write && writenotes!=''){
		if( type!=undefined && type!="text" && type!="anchor" && type!="button")
	document.getElementById(writenotes).focus();
	var tmp;
	if(final){
		tmp=final_transcript;
		console.log("final val"+tmp)
		tmpVar=tmp;
	}else{
		tmp=interim_transcript;
	}
	tmp=previousValue +final_transcript+interim_transcript;
	//console.log("With focus"+onlyfocus+tmp);
	if(!onlyfocus){
		//console.log("write with focus false"+writenotes+tmp);
		
	 paste(writenotes,tmp,type,dropdownval);
	}else{
		//document.getELementById(tmpId).value=document.getELementById(tmpId).value.
		//console.log(" write with focus true"+tmpId+tmpVar+previousType);	
		if(previousType!='dropdown')
		document.getElementById(tmpId).value=previousValue + tmpVar;
	}
	//document.getElementById(writenotes).innerHTML=final_transcript;
	//document.getElementById(writenotes).focus();
	}else{
		if(focuselement!=undefined){
			var tmp;
			if(final){
				tmp=final_transcript;
				//console.log("final val for text area"+tmp)
				tmpVar=tmp;
			}else{
				tmp=interim_transcript;
			}
			
			tmp=previousValue +final_transcript+interim_transcript;
			//if(focuselement.innerText!=undefined || focuselement.innerText!='')
			//focuselement.innerText=focuselement.innerText+" "+tmp;
			focuselement.innerText=tmp;
		}
	}
	
	
  };

  recognition.onend = function() {

    recognizing = false;
    startmic=false;
    if (ignore_onend) {
      return;
    }
    //speechMyText(final_transcript);
    if (!final_transcript) {
     // message.innerHTML = 'Click "Talk" and begin speaking.';
      //talk_button.innerHTML = 'Talk';
      return;
    }
  };

  recognition.onerror = function(event) {
    if (event.error == 'no-speech') {
      //message.innerHTML = 'No speech was detected.';
      ignore_onend = true;
    }
    if (event.error == 'audio-capture') {
      //message.innerHTML = 'No microphone was found. Ensure that a microphone is installed.';
      ignore_onend = true;
    }
    if (event.error == 'not-allowed') {
      if (event.timeStamp - start_timestamp < 100) {
        //message.innerHTML = 'Permission to use microphone is blocked. To change, go to chrome://settings/contentExceptions#media-stream';
      } else {
        //message.innerHTML = 'Permission to use microphone was denied.';
      }
      ignore_onend = true;
      startmic=false;
    }
  };

}


function isPhone(phone) {
    var regex = /(\W|^)[(]{0,1}\d{3}[)]{0,1}[\s-]{0,1}\d{3}[\s-]{0,1}\d{4}(\W|$)/;
    return regex.test(phone);
}

function paste(id,html,type,dropdownvalue){
if(type!=undefined && type !="text" && dropdownname==''){	
document.getElementById(id).focus();
//console.log("before writing"+html);
if(type=="number"){
	html=html.replace(/\D/g, '');
	/*if(!isPhone(html)){
		alert(id +"not valid");
		document.getElementById(id).value ="";
		return false;
	}*/
}
if(document.getElementById(id).value!=undefined){
	document.getElementById(id).value =   html;
}else{
	document.getElementById(id).value = html;
}


if(type=='input'){
var el = document.getElementById(id);
    var range = document.createRange();
    var sel = window.getSelection();
    if(el.childNodes.length>0)
    range.setStart(el.childNodes[0], document.getElementById(id).value.length);
    range.collapse(true);
    sel.removeAllRanges();
    sel.addRange(range);
    el.focus();
}
  

}else if(type=="text"){
	//document.getElementById(id).innerHTML = html;
	var iframe=document.getElementById(id).getElementsByTagName("iframe")[0];
	var cntnt = iframe.contentWindow.document;
	var parag = cntnt.getElementsByTagName("p")[0];
	parag.innerText=html;
}else{

	if(dropdownvalue!=undefined)
	document.getElementById(dropdownname).value=dropdownvalue;
}

}



function talkWithApp(event) {
  if (recognizing) {
    recognition.stop();
    startmic=false;
    document.getElementById("micadd").innerHTML='';
   // $('#micadd').remove(d1);
    added=false;
    $('#micadd').empty();
    //message.innerHTML = 'Click "Talk" and begin speaking.';
   // talk_button.innerHTML = 'Talk';
    return;
  }
  final_transcript = '';
 // recognition.lang = language.value;
  if(!startmic){
	  recognition.start();
	  startmic=true;
  }
  ignore_onend = false;
  //final_span.innerHTML = '';
  //interim_span.innerHTML = '';
  //message.innerHTML = 'Click the "Allow" button above to enable your microphone.';
  start_timestamp = event.timeStamp;
}

// Speech Synthesis
function speechMyText(textToSpeech) {
  var u = new SpeechSynthesisUtterance();
  u.text = textToSpeech;
  u.lang = language.value;
  u.rate = 1.0;
  u.onend = function(event) {}
  speechSynthesis.speak(u);
}
//////////////////////////////

$(document).ready(function() {
  
  // GC browser language
  //var userLang = navigator.language || navigator.userLanguage;
  //setTimeout(function(){ 
    var userLang = navigator.language || navigator.userLanguage;
    //$(document).find("#language").val('fr').change();
    //$("#language").val(userLang).change();
    //$("#language").val(userLang);
 // }, 600);
 
});



window.gototab = function(target,number,id,type) {

//document.getElementById(tabactive).className='nav-link';
//			document.getElementById(target).className='nav-link  active show';
//			tabactive=newtab;
if(type!="text" &&  type!='button' &&  type!='redirect' && type!='anchor' & type!='function'){
document.getElementById(id).focus();
}
else if(type=='redirect'){
	window.location.href="#/dischargeSummary";
}

	
}

var startmic=false;
var added=false;
document.body.addEventListener('keypress', function(e) {
    //console.log(e.keyCode);
    if(e.keyCode=="10"){
    	var d1=' <div class="modal-body">   <div class="delivery-popup clearfix">  <div class="delivery-container">      <input type="text" name="voicetext" id="voicetext" class="form-control"  placeholder="Voice to text">  <input type="hidden" id="jsonmessage"  value=""/>   </div>   </div>        </div>                <div class="modal-footer">     <a><input type="button" value="Cancel" id="cancel" onclick="callvoice2()"  class="btn btn-primary btn-save-box-shadow"></a>      <a><input type="button" value="Save" id="savecomplaint1" onclick="callvoice1()"  class="btn btn-primary btn-save-box-shadow"></a>                    </div>            </div>                </div>';
    	if(!added){
    	/*$('#micadd').append(d1);
    	added=true;*/
    	}
    	
    	talkWithApp(event)
    }
});




function callvoice2(){
	$('#micadd').empty();
}




function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  if (document.getElementById(elmnt.id )) {
    /* if present, the header is where you move the DIV from:*/
    document.getElementById(elmnt.id ).onmousedown = dragMouseDown;
  } else {
    /* otherwise, move the DIV from anywhere inside the DIV:*/
    elmnt.onmousedown = dragMouseDown;
  }
}

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    /* stop moving when mouse button is released:*/
    document.onmouseup = null;
    document.onmousemove = null;
  }
  
 
  $(function() {
		var container = document.createElement('div');
		
		container.id = 'micadd';
		container.className = 'fixed-action-btn horizontal';
		 container.style.position='fixed';
		container.style.right='23px';
		container.style.bottom='23px';
		container.style.paddingTop='19px';
		//container.style.marginBottom='543px';
		container.style.marginRight='434px';
		container.style.zIndex='998';
		container.style.border='none';
		container.style.borderRadius='30px';
		container.style.display='inline-block';
		//container.style.height='36px';
		container.style.lineHeight='36px';
		container.style.outline='0';
		container.style.padding='0 2rem';
		container.style.textTransform='uppercase';
		container.style.verticalAlign='middle';
	container.style.overflow='hidden';
		
		container.style.backgroundColor='#F44336 !important'; 
		
	//	
	if($("#micadd").length==0){
	$('body').append(container);
	
	}
	//alert("sd"+$('body').innerHTML);
//		document.body.insertBefore(container, document.body.firstChild);
		
		
	//
//		var html = '<div>';
	//
	//	
//		    //html+= '<a class="btn-floating btn-large red"><i class="fa fa-microphone" style="color:#28b2f7;    margin-top: 2px;  margin-right: 6px; font-size: 25px;width:5px; " onclick="talkWithApp(event)"></i></a>';
//		    html+= '<a style="width: 51.5px; height: 55.5px;     background-color: #F44336 !important;    box-shadow: 0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12);     border: none;     border-radius: 23px;    display: inline-block;     height: 48px;     line-height: 9px;     outline: 0;     padding: 0 2rem;     text-transform: uppercase;     vertical-align: middle;" ><i class="fa fa-microphone" style="color:#28b2f7;    margin-top: 10px;  margin-right: 6px; font-size: 25px;width:5px; " onclick="talkWithApp(event)"></i></a>';
//			
	//
//		html+= '</div>';
	//
	//$('#micadd').append(d1);
//		dragElement(document.getElementById("micadd"));
		});
		
