
String.prototype.startWith=function(str){     
  var reg=new RegExp("^"+str);     
  return reg.test(this);        
}  

String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}

Function.prototype.heredoc = function(){
	var doc = this.toString()
	var m=doc.match(/\/\*([\s\S]+?)\*\//)
	if(m.length>1){
		return m[1];
	}
}

function table_data(selector) {
	var data = []
	$('table tr', $(selector)).each(function() {
		var row = []
		$('td,th', $(this)).each(function() {
			row.push($.trim($(this).text()))
		})
		data.push(row)
	})
	return data
}


function obj2json(data) {
	return JSON.stringify(data)
}


function get_www_url(){
	
   var url= window.location.href
   if(url.startWith('http')){
	   return url;
   } else {
	   return __www_url;
   }
}



function replace_all_url(url){
	var surl= url.match(/(http(?:s)?:\/\/[^\/]+)\/?/)[1]
	var rurl=url.substring(0,url.lastIndexOf('/'))
	
	$('a').each(function(){
	   var href=''+ $(this).attr('href');
	   if(href.startWith('/')){
	      $(this).attr('href',surl+href)
	   } else if(href.startWith('http://')||href.startWith('https://')){
	
	   } else {
	     
	     $(this).attr('href',rurl+'/'+href)
	   }	
	})

}


function replace_all_img(url){
	var surl= url.match(/(http(?:s)?:\/\/[^\/]+)\/?/)[1]
	var rurl=url.substring(0,url.lastIndexOf('/'))
	
	$('img').each(function(){
	   var href=''+ $(this).attr('src');
	   if(href.startWith('/')){
	      $(this).attr('src',surl+href)
	   } else if(href.startWith('http://')||href.startWith('https://')){
	
	   } else {
	    
	     $(this).attr('src',rurl+'/'+href)
	   }	
	})

}

replace_all_url(get_www_url());
replace_all_img(get_www_url());

function href_data(selector) {

	var data = []
	$('a', $(selector)).each(function() {

		var href = $(this).attr('href');
		var title = $.trim($(this).text()) 
		data.push({
			href : href,
			title : title
		})
	})

	return data

}

function html_data(selector) {
	return $(selector).html()
}

function text_data(selector) {
	return $(selector).html()
}

function include_js(url) {
	$.getScript(url)
}

function __outerhtml(selector) {

	return $(selector).prop('outerHTML')

}

function out_html(selector) {
	return __outerhtml(selector)
}

function html_out(selector) {
	return __outerhtml(selector)
}

function json_encode(obj) {

	return obj2json(obj)

}

function json_decode(obj){
	return JSON.parse(obj)
	
}

function out(obj){
	
	
	return json_encode(obj)
	
}

function print(obj){
	
	return json_encode(obj)
	
}


function __http_request(method,url,data,callback){

       $.ajax({
                url : url,
                data:data,
                cache : false, 
                async : false,
                type : method,
                dataType : 'html', 
                success : function (result){
                    return callback(result)
                }
            });
}


function http_post(url,data,callback){
	if(typeof data=='function'){
		callback=data
		data={}
	}
	return __http_request('POST',url,data,callback)
}

function http_get(url,data,callback){ 
	if(typeof data=='function'){
		callback=data
		data={}
	}
	return __http_request('GET',url,data,callback);
}


















