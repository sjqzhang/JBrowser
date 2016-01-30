function table_data(selector) {
	var data = []
	$('table tr', $(selector)).each(function() {
		var row = []
		$('td,th', $(this)).each(function() {
			row.push($(this).text())
		})
		data.push(row)
	})
	return data
}


function obj2json(data) {
	return JSON.stringify(data)
}

function href_data(selector) {

	var data = []
	$('a', $(selector)).each(function() {

		var href = $(this).attr('href');
		var title = $(this).text()
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


















