// 交互数据
var queryStatement = {
  "userName" : "",
  "columnNameList" : [  /* 表格列名列表 */ ],
  "whereClause" : { /* where子句 */
    "conjunctionList" : [],
    "conditionSet" : []
  }
};
var columnSetCache = null;
// 获取select中选中选项的数据
function getOptionData() {
  var el = $(arguments[0]);
  var index = el.selectedIndex;
  // 若为第一个选项（提示文字），数据应为null，
  var text = (index)?el.options[index].text:null;
  return {
    "index": index,
    "text": text
  };
}
// 渲染select中的option
function renderOptionList(selectId, optionList, tips) {
  // 清空之前选项
  $(selectId).innerHTML = "";
  var fristOption = $$("option");
  with(fristOption) {
    setAttribute("value", 0);
    innerHTML = tips || "请选择";
  }
  $(selectId).appendChild(fristOption);
  for(var i in optionList) {
    var option = $$("option");
    with(option) {
      setAttribute("value",i+1);
      innerHTML = optionList[i];
    }
    $(selectId).appendChild(option);
  }
}
// 检查数据合法性
function checkoutData() {
  for(var i in arguments) {
    var value = arguments[i];
    var type = (typeof value);
    switch(type) {
      case "string":
      if(value == "") return false;
      break;
      case "object":
      // 下标是0，为选中提示文字
      if(value.index == 0) return false;
      break;
    }
  }
  return true;
}
// 设置控件是否可用
function setWidgetDisabled(idName, disabled) {
  if(disabled) {
    $(idName).setAttribute("disabled", "disabled");
  } else {
    $(idName).removeAttribute("disabled");
  }
}
// 创建控件
function createWidget(widgetName) {
  // TODO 完成该函数
  // var widgetData = {
  //   "idName" :
  //   "eventSet" :
  //   "attributeSet"
  // }
}
// 生成不重复的表格名列表
function generateTableNameList(columnNameList) {
  var tableNameList = new Array();
  for(var i in columnNameList) {
    var tableName = columnNameList[i].split(".")[0];
    if(!isExistTableName(tableName)) {
      tableNameList.push(tableName);
    }
  }
  return tableNameList;
  //
  function isExistTableName(tableName) {
    for(var i in tableNameList) {
      if(tableNameList[i] == tableName) return true;
    }
    return false;
  }
}

// 获取用户名
(function() {
  ajax({
    url: "./GetUserNameList",
    data: {},
    success: function(xhr) {
      var response = convertJSONString(xhr.responseText);
      renderOptionList("userNameSelect", response.userNameList, "用户名");
    }
  });
})();
// 修改表名列表的数据
function changeTableName() {
  var optionData = getOptionData("userNameSelect");
  // 第一个选项为提示符，直接返回
  if(!optionData.index) return;
  var userName = optionData.text;
  ajax({
    url: "./GetTableNameList",
    data: {
      "userName": userName
    },
    success: function(xhr) {
      var response = convertJSONString(xhr.responseText);
      renderOptionList("tableNameSelect", response.tableNameList, "表格名");
      renderOptionList("columnNameSelect", [], "列名");  // 清空列名
    }
  });
}
// 修改列名列表的数据
function changeColumnName() {
  var optionData = getOptionData("tableNameSelect");
  // 第一个选项为提示符，直接返回
  if(!optionData.index) return;
  var tableName = optionData.text;
  ajax({
    url: "./GetColumnNameList",
    data: {
      "tableName": tableName
    },
    success: function(xhr) {
      var response = convertJSONString(xhr.responseText);
      renderOptionList("columnNameSelect", response.columnNameList, "列名");
    }
  });
}
// 插入数据到tableSet中
function insertIntoTableSet(columnName) {
  // var tableSet = queryStatement.tableSet;
  // for(var i in tableSet) {
  //   if(tableSet[i].tableName == tableName) {
  //     // 发现重复表名
  //     tableSet[i].columnNameList.push(columnName);
  //     return;
  //   }
  // }
  // tableSet.push({
  //   "tableName" : tableName,
  //   "columnNameList" : [columnName]
  // });
  var columnNameList = queryStatement.columnNameList;
  columnNameList.push(columnName);
}
// 从tableSet中移除数据
function removeFromTableSet(columnName) {
  // var tableSet = queryStatement.tableSet;
  // for(var i in tableSet) {
  //   if(tableSet[i].tableName == tableName) {
  //     var columnNameList = tableSet[i].columnNameList;
  //     for(var j in columnNameList) {
  //       if(columnNameList[j] == columnName) {
  //         // 删除该列名
  //         columnNameList.splice(j, 1);
  //         // 若该列名列表为空，则从表格集合中删除该表格
  //         if(!columnNameList.length) tableSet.splice(i, 1);
  //         return;
  //       }
  //     }
  //   }
  // }
  var columnNameList = queryStatement.columnNameList;
  for(var i in columnNameList) {
    if(columnNameList[i] == columnName) {
      columnNameList.splice(i,1);
      // 直接返回防止重复删除
      return;
    }
  }
}
// 添加表格的列名
function addColumnName() {
  // 获取数据
  var tableName = getOptionData("tableNameSelect").text;
  var columnName = getOptionData("columnNameSelect").text;
  // 未选中数据，直接返回
  // TODO 增加人性化的提示
  if(tableName == null || columnName == null) return;
  // 插入tableSet中
  insertIntoTableSet((tableName + "." + columnName));
  // 若为columnNameList不为空，则禁止切换用户
  if(queryStatement.columnNameList.length) {
    $("userNameSelect").setAttribute("disabled", "disabled");
    renderAllColumnNameSelect();
  }
  // 更改视图
  var item = $$("p"), dataNode = $$("span"), removeBtn = $$("input");
  with(removeBtn) {
    setAttribute("type", "button");
    setAttribute("onclick", "removeColumnName(this);");
    setAttribute("value", "移除");
  }
  with(dataNode) {
    innerHTML = tableName + "." + columnName;
    setAttribute("title", innerHTML);
  }
  with(item) {
    appendChild(dataNode);
    appendChild(removeBtn);
  }
  $("selectedColumnName").appendChild(item);
}
// 删除表格的列名
function removeColumnName() {
  var parentNode = arguments[0].parentNode;
  var dataNode = parentNode.firstElementChild;
  // 获取数据
  var columnName = dataNode.innerHTML;
  // 从tableSet中删除
  removeFromTableSet(columnName);
  // 若为columnNameList为空，则可切换用户
  if(!queryStatement.columnNameList.length) {
    $("userNameSelect").removeAttribute("disabled");
    renderAllColumnNameSelect();
  }
  // 从视图中删除
  parentNode.parentNode.removeChild(parentNode);
}
// 渲染条件视图
(function(){
  renderOptionList(
    "conditionTypeSelect",
    ["数据比较","确定范围","字符匹配","空值比较","多表连接"],
    "条件类型"
  );
  renderOptionList(
    "notSelect",
    ["是","否"],
    "是否取NOT"
  );
  renderOptionList(
    "conjunctionSelect",
    ["AND","OR"],
    "连接词"
  );
})();
// 渲染条件视图的列名下拉框
function renderAllColumnNameSelect() {
  var columnNameList = queryStatement.columnNameList;
  var tableNameList = generateTableNameList(columnNameList);
  // 获取所有被选中表格的列名列表
  ajax({
    url: "./GetAllColumnNameList",
    data: {
      "tableNameList" : tableNameList
    },
    success: function(xhr) {
      var response = convertJSONString(xhr.responseText);
      // 渲染select
      renderOptionList("allColumnNameSelect", response.allColumnNameList, "列名");
    }
  });
}
// 改变参数列表的内容
function changeArgListBox() {
  var conditionType = getOptionData("conditionTypeSelect").index;
  if(!conditionType) return;
  // 清空之前输入框
  $("argListBox").innerHTML = "";
  switch(conditionType) {
  case 1:
    renderArgInput("firstArg", "比较符");
    renderArgInput("secondArg", "比较值");
    break;
  case 2:
    renderArgInput("firstArg", "下限值");
    renderArgInput("secondArg", "上限值");
    break;
  case 3:
    renderArgInput("firstArg", "匹配值");
    break;
  case 5:
    var columnNameList = queryStatement.columnNameList;
    if(!columnNameList.length) return;
    var tableNameList = generateTableNameList(columnNameList);
    ajax({
      url: "./GetAllColumnNameList",
      data: {
        "tableNameList" : tableNameList
      },
      success: function(xhr) {
        var response = convertJSONString(xhr.responseText);
        // 渲染select
        renderArgSelect("firstArg", response.allColumnNameList, "列名");
      }
    });
    break;
  }
  //
  function renderArgInput(idName,tips) {
    var argInput = $$("input");
    with(argInput) {
      setAttribute("id", idName);
      setAttribute("type", "text");
      setAttribute("placeholder", tips);
    }
    $("argListBox").appendChild(argInput);
  }
  function renderArgSelect(idName,arr,tips) {
    var argSelect = $$("select");
    with(argSelect) {
      setAttribute("id", idName);
    }
    $("argListBox").appendChild(argSelect);
    renderOptionList(idName, arr, tips);
  }
}
// 添加条件
function addCondition() {
  // 获取数据
  var conditionType = getOptionData("conditionTypeSelect").index;
  var not = getOptionData("notSelect").index;
  var columnName = getOptionData("allColumnNameSelect").text;
  var argList = new Array();
  // 禁止空值
  if(!conditionType || !not || (columnName == null)) return;
  switch(conditionType) {
  case 1:
  case 2:
    var firstValue = $("firstArg").value;
    var secondValue = $("secondArg").value;
    if(firstValue == "" || secondValue == "") return;
    argList.push(firstValue);
    argList.push(secondValue);
    break;
  case 3:
    var firstValue = $("firstArg").value;
    if(firstValue == "") return;
    argList.push(firstValue);
    break;
  case 5:
    var firstValue = getOptionData("firstArg").text;
    if(firstValue == null) return;
    argList.push(firstValue);
  }
  var condition = {
    "conditionType" : conditionType,
    "not" : (not == 1) ? true : false,
    "columnName" : columnName,
    "argList" : argList
  };
  // 添加到conditionSet中
  var conditionSet = queryStatement.whereClause.conditionSet;
  conditionSet.push(condition);
  // 渲染界面
  var item = $$("p"), dataNode = $$("span"), removeBtn = $$("input");
  with(removeBtn) {
    setAttribute("type", "button");
    setAttribute("onclick", "removeCondition(this);");
    setAttribute("value", "移除");
  }
  with(dataNode) {
    setAttribute("index", (conditionSet.length - 1));
    innerHTML = "|" + getOptionData("conditionTypeSelect").text + "|";
    innerHTML += getOptionData("notSelect").text + "|";
    innerHTML += getOptionData("allColumnNameSelect").text + "|";
    var tmpList = conditionSet[conditionSet.length-1].argList;
    for(var i in tmpList) {
      innerHTML += tmpList[i] + "|"
    }
  }
  with(item) {
    appendChild(dataNode);
    appendChild(removeBtn);
  }
  $("conditionList").appendChild(item);
}
// 删除条件
function removeCondition() {
  var thisNode = arguments[0];
  var parentNode = thisNode.parentNode;
  var dataNode = parentNode.firstElementChild;
  // 获取数据
  var index = parseInt(dataNode.getAttribute("index"));
  // 从conitionSet中删除数据
  var conditionSet = queryStatement.whereClause.conditionSet;
  conditionSet.splice(index, 1);
  // 更改视图
  var next = parentNode;
  while(next=next.nextElementSibling) {
    var nextDataNode = next.firstElementChild;
    var oldIndex = parseInt(nextDataNode.getAttribute("index"));
    nextDataNode.setAttribute("index", oldIndex-1);
  }
  parentNode.parentNode.removeChild(parentNode);
}
// 添加连接词
function addConjunction() {
  console.log("mark");
  // 获取数据
  var conjunctionData = getOptionData("conjunctionSelect");
  var conjunctionText = conjunctionData.text;
  // 数据是否合法
  if(!checkoutData(conjunctionData)) {
    // TODO 增加提示
    return;
  }
  // 更改数据模型
  var conjunctionList = queryStatement.whereClause.conjunctionList;
  conjunctionList.push(conjunctionText);
  // 更改视图
  var dataNode = $$("span"), removeBtn = $$("input"), item = $$("p");
  with(dataNode) {
    setAttribute("index", (conjunctionList.length - 1));
    innerHTML = conjunctionText;
  }
  with(removeBtn) {
    setAttribute("type", "button");
    setAttribute("onclick", "removeConjunction(this);");
    setAttribute("value", "移除");
  }
  with(item) {
    appendChild(dataNode);
    appendChild(removeBtn);
  }
  $("conjunctionList").appendChild(item);
}
// 删除连接词
function removeConjunction() {
  var thisNode = arguments[0];
  var parentNode = thisNode.parentNode;
  var dataNode = parentNode.firstElementChild;
  // 获取数据
  var index = parseInt(dataNode.getAttribute("index"));
  // 更改数据模型
  var conjunctionList = queryStatement.whereClause.conjunctionList;
  conjunctionList.splice(index, 1);
  // 更改视图
  var next = parentNode;
  while(next=next.nextElementSibling) {
    var nextDataNode = next.firstElementChild;
    var oldIndex = parseInt(nextDataNode.getAttribute("index"));
    nextDataNode.setAttribute("index", oldIndex-1);
  }
  parentNode.parentNode.removeChild(parentNode);
}
// 查询表格
function query() {
  // 补充数据
  queryStatement.userName = getOptionData("userNameSelect").text;
  queryStatement.tableNameList = generateTableNameList(queryStatement.columnNameList);
  // 发送请求
  ajax({
    url: "./GetQueryResult",
    data: queryStatement,
    success: function(xhr) {
      var response = convertJSONString(xhr.responseText);
      renderTable(response.columnSet);
    }
  });
}
// 渲染表格
function renderTable(columnSet) {
  if(columnSet.length == 0) return;
  $("tableContent").innerHTML = "";
  // 渲染表头
  var tableHeaderNode = $$("p");
  for(var i in columnSet) {
    var columnNameNode = $$("span");
    with(columnNameNode) {
      setAttribute("class", "tableHeader");
      innerHTML = columnSet[i].columnName;
    }
    tableHeaderNode.appendChild(columnNameNode);
  }
  $("tableContent").appendChild(tableHeaderNode);
  // 渲染表格的值
  var len = columnSet[0].columnValueList.length;
  for(var i = 0;i < len;i++) {
    var line = $$("p");
    for(var j = 0;j < columnSet.length;j++) {
      var columnValue = columnSet[j].columnValueList[i];
      var cell = $$("span");
      with(cell) {
        setAttribute("class", "tableCell");
        innerHTML = columnValue;
      }
      line.appendChild(cell);
    }
    $("tableContent").appendChild(line);
  }
}
// 单表查询
function tableQuery() {
	// 获取数据
	var userNameData = getOptionData("userNameSelect");
	var tableNameData = getOptionData("tableNameSelect");
	// 检查数据合法性
	if(!checkoutData(userNameData)) {
		return;
	}
	// 
	var tableName = userNameData.text + "." + tableNameData.text;
	ajax({
	    url: "./GetTableInformation",
	    data: {
	    	"tableName" : tableName
	    },
	    success: function(xhr) {
	      var response = convertJSONString(xhr.responseText);
	      columnSetCache = response.columnSet;
	      renderMyTable(response.columnSet);
	    }
	});
}
// 渲染表格
function renderMyTable(columnSet) {
	  if(columnSet.length == 0) return;
	  $("tableContent").innerHTML = "";
	  // 渲染表头
	  var tableHeaderNode = $$("p");
	  for(var i in columnSet) {
	    var columnNameNode = $$("span");
	    with(columnNameNode) {
	      setAttribute("class", "tableHeader");
	      innerHTML = columnSet[i].columnName;
	    }
	    tableHeaderNode.appendChild(columnNameNode);
	  }
	  $("tableContent").appendChild(tableHeaderNode);
	  // 渲染表格的值
	  var len = columnSet[0].columnValueList.length;
	  for(var i = 0;i < len;i++) {
	    var line = $$("p");
	    for(var j = 0;j < columnSet.length;j++) {
	      var columnValue = columnSet[j].columnValueList[i];
	      var cell = $$("span");
	      with(cell) {
	    	// i-行 j-列
	    	setAttribute("i-value", i);
	    	setAttribute("j-value", j);
	        setAttribute("class", "tableCell");
	        innerHTML = columnValue;
	        setAttribute("ondblclick", "enableEdit(this);");
	        setAttribute("onblur", "changeValue(this);");
	      }
	      line.appendChild(cell);
	    }
	    $("tableContent").appendChild(line);
	  }
}
// 
function enableEdit() {
	arguments[0].setAttribute("contenteditable", "true");
	arguments[0].style.background = "none";
}
// 
function changeValue() {
	var thisNode = arguments[0];
	var parentNode = thisNode.parentNode;
	var topNode = parentNode.parentNode;
	var headerNode = topNode.firstChild;
	var childNodes;
	var data = {};
	// 更改视图
	thisNode.removeAttribute("contenteditable");
	thisNode.style.backgroundColor = "#ecf0f6";
	// 获取表名
	var userNameData = getOptionData("userNameSelect");
	var tableNameData = getOptionData("tableNameSelect");
	// 检查数据合法性
	if(!checkoutData(userNameData)) {
		return;
	}
	// 
	var tableName = userNameData.text + "." + tableNameData.text;
	data.tableName = tableName;
	// 获取定位数据
	var iValue = thisNode.getAttribute("i-value");  // 行
	var jValue = thisNode.getAttribute("j-value");  // 列
	var columnSet = new Array();
	for(var i in columnSetCache) {
		columnSet.push({
			"columnName" : columnSetCache[i].columnName,
			"columnValue" : columnSetCache[i].columnValueList[iValue]
		});
	}
	data.columnSet = columnSet;
	// 获取修改数据
	data.data = {
			"columnName" : columnSetCache[jValue].columnName,
			"columnValue" : thisNode.innerText
	}
	// 
	console.log(data);
	ajax({
	    url: "./TableRefactor",
	    data: data,
	    success: function(xhr) {
	      var response = convertJSONString(xhr.responseText);
	    }
	});
	//
	tableQuery();
}

//