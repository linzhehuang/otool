// ajax请求
function ajax() {
	var ajaxXHR = createXMLHttpRequest();
	var ajaxData = {
		requestType: arguments[0].requestType || "POST",
		contentType: arguments[0].contentType || "application/json",
		url: arguments[0].url || "",
		async: arguments[0].async || true,
		data: arguments[0].data || "{}",
		success: arguments[0].success || function() {},
		error: arguments[0].error || function() {}
	};
	// 发送请求
	with(ajaxXHR) {
		onreadystatechange = function() {
			if(readyState == 4) {
				if(status == 200) {
					ajaxData.success(ajaxXHR);
				} else {
					ajaxData.error(ajaxXHR);
				}
			}
		};
		open(ajaxData.requestType, ajaxData.url, ajaxData.async);
		setRequestHeader("Content-Type", ajaxData.contentType);
		send(formatData(ajaxData.data));
	}
	// XMLHttpRequest对象
	function createXMLHttpRequest() {
		if(window.ActiveXObject) return new ActiveXObject("Microsoft.XMLHTTP");
		else if(window.XMLHttpRequest) return new XMLHttpRequest();
	}
	// 格式化参数
	function formatData(data) {
		if(ajaxData.contentType == "application/json") return JSON.stringify(data);
		else {
			return data;
		}
	}
}
// JSON字符串转对象
function convertJSONString(jsonString) {
	return eval('(' + jsonString + ')');
}
// 根据id名称获取元素
function $(idName) {
	return document.getElementById(idName);
}
// 创建新标签
function $$(tagName) {
	return document.createElement(tagName);
}
