String.prototype.startWith = function(str) {
	var reg = new RegExp("^" + str);
	return reg.test(this);
}

String.prototype.endWith = function(str) {
	var reg = new RegExp(str + "$");
	return reg.test(this);
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

function trim(str) {
	return str.trim();
}

String.prototype.trim_script = function() {
	return this.replace(/<script[^>]*?>[\s\S]*?<\/script>/g, "");
}

function trim_script(str) {
	return str.trim_script();

}

String.prototype.trim_style = function() {
	return this.replace(/<style[^>]*?>[\s\S]*?<\/style>/g, "");
}

function trim_style(str) {
	return str.trim_style();

}

Function.prototype.heredoc = function() {
	var doc = this.toString()
	var m = doc.match(/\/\*([\s\S]+?)\*\//)
	if (m.length > 1) {
		return m[1];
	}
}


function html_encode(str)   
{   
  var s = "";   
  if (str.length == 0) return "";   
  s = str.replace(/&/g, "&amp;");   
  s = s.replace(/</g, "&lt;");   
  s = s.replace(/>/g, "&gt;");   
  s = s.replace(/ /g, "&nbsp;");   
  s = s.replace(/\'/g, "&#39;");   
  s = s.replace(/\"/g, "&quot;");   
  s = s.replace(/\n/g, "<br>");   
  return s;   
}   
 
function html_decode(str)    
{   
  var s = "";   
  if (str.length == 0) return "";   
  s = str.replace(/&amp;/g, "&");   
  s = s.replace(/&lt;/g, "<");   
  s = s.replace(/&gt;/g, ">");   
  s = s.replace(/&nbsp;/g, " ");   
  s = s.replace(/&#39;/g, "\'");   
  s = s.replace(/&quot;/g, "\"");   
  s = s.replace(/<br>/g, "\n");   
  return s;   
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

function get_www_url() {

	var url = window.location.href
	if (url.startWith('http')) {
		return url;
	} else {
		return __www_url;
	}
}

function replace_all_url(url) {
	var surl = url.match(/(http(?:s)?:\/\/[^\/]+)\/?/)[1]
	var rurl = url.substring(0, url.lastIndexOf('/'))

	$('a').each(function() {
		var href = '' + $(this).attr('href');
		if (href.startWith('/')) {
			$(this).attr('href', surl + href)
		} else if (href.startWith('http://') || href.startWith('https://')) {

		} else {

			$(this).attr('href', rurl + '/' + href)
		}
	})

}

function replace_all_img(url) {
	var surl = url.match(/(http(?:s)?:\/\/[^\/]+)\/?/)[1]
	var rurl = url.substring(0, url.lastIndexOf('/'))

	$('img').each(function() {
		var href = '' + $(this).attr('src');
		if (href.startWith('/')) {
			$(this).attr('src', surl + href)
		} else if (href.startWith('http://') || href.startWith('https://')) {

		} else {

			$(this).attr('src', rurl + '/' + href)
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

function json_decode(obj) {
	return JSON.parse(obj)

}

function out(obj) {

	return json_encode(obj)

}

function print(obj) {

	return json_encode(obj)

}

function __http_request(method, url, data, callback) {

	$.ajax({
		url : url,
		data : data,
		cache : false,
		async : false,
		type : method,
		dataType : 'html',
		success : function(result) {
			return callback(result)
		}
	});
}

function http_post(url, data, callback) {
	if (typeof data == 'function') {
		callback = data
		data = {}
	}
	return __http_request('POST', url, data, callback)
}

function http_get(url, data, callback) {
	if (typeof data == 'function') {
		callback = data
		data = {}
	}
	return __http_request('GET', url, data, callback);
}

function TextExtract() {

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

}

TextExtract.prototype = {

	textLines : [],
	blksLen : [],
	blkSize : 6, 
	preProcess : function(text) {
	    //console.log(text) 
		text = text.replace(/<p[^>]*?>/g, "<p>PPPP");
//		text = text.replace(/<\/p>/g, "</p>");
		text = text.replace(/<br>|<\/br>/g, "brbrbr\n");
		
		text = text.replace(/<!--[\s\S]*?-->/g, "");
		text = text.replace(/<style[^>]*?>[\s\S]*?<\/style>/g, "");
		text = text.replace(/<script[^>]*?>[\s\S]*?<\/script>/g, "");
		text = text.replace(/<[^>]*?>/g, "");
		return text;
	},
	getTextLines : function(text) { 
		var lines = text.split(/\n+/);
		for ( var i = 0; i < lines.length; i++) {
			this.textLines.push(lines[i].trim()) 
		}
	},
	calBlocksLen : function() {
		textLineNum = this.textLines.length;

		// calculate the first block's length 
		blkLen = 0;
		for (i = 0; i < this.blkSize; i++) {
			blkLen += this.textLines[i].length;
		}
		this.blksLen[this.blksLen.length] = blkLen;

		// calculate the other block's length using Dynamic Programming method
		for (i = 1; i < (textLineNum - this.blkSize); i++) {
			blkLen = this.blksLen[i - 1]
					+ (this.textLines[i - 1 + this.blkSize].length)
					- (this.textLines[i - 1].length);
			this.blksLen[this.blksLen.length] = blkLen;
		}
	},
	getPlainText : function(text) {

		//var text=doc.heredoc();

		//debugger;

		var preProcText = this.preProcess(text);
		this.getTextLines(preProcText);
		this.calBlocksLen();

		var start = -1;
		var end = -1;
		var i = 0;
		var maxTextLen = 0;

		blkNum = (this.blksLen.length);
		while (i < blkNum) {
			while ((i < blkNum) && (this.blksLen[i] == 0))
				i++;
			if (i >= blkNum)
				break;
			var tmp = i;

			var curTextLen = 0;
			var portion = '';
			while ((i < blkNum) && (this.blksLen[i] != 0)) {
				if (this.textLines[i] != '') {
					portion = portion + this.textLines[i];
					portion = portion + "\n"; 
					curTextLen += (this.textLines[i].length);
				}
				i++;
			}
			if (curTextLen > maxTextLen) {
				this.text = portion;
				maxTextLen = curTextLen;
				start = tmp;
				end = i - 1;
			} 
		}
		
		console.log(this.text) 

		return  html_decode(this.text.replace(/^PPPP/img, "\n\t").replace("brbrbr", "\n"));     
  
	}

}

function get_main_text(text) {

	var ext = new TextExtract()
	 
	if(typeof(text)=='undefined'){
		var text=$('html').prop('outerHTML')
		 
	}

	return ext.getPlainText(text)  

}

function get_main_html(text) {

	var ext = new TextExtract()
	 
	if(typeof(text)=='undefined'){
		var text=$('html').prop('outerHTML')
		
	}

	var lines=ext.getPlainText(text).split(/\n/)
	
	var selector=''
	for(var i=0;i<lines.length;i++){
		if($(":contains("+lines[i].replace('"','\"') +")").parent().is('div')){
			console.log(lines[i])
		    if($(":contains("+lines[i].replace('"','\"') +")").parent().attr('id')==undefined){ 
		    	selector='.'+$(":contains("+lines[i].replace('"','\"') +")").parent().attr('class'); 
		    } else {
		    	selector='#'+$(":contains("+lines[i].replace('"','\"') +")").parent().attr('id'); 
		    }
			console.log(selector)
			
			if(selector!='.undefined'&&selector!='#undefined'){
				break;
			}
		}
	}
	
	return selector;


}

