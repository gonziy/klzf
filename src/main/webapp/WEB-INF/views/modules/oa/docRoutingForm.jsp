<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

<html>
<head>
	<title>发文</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('提交中，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			//
			//var url = "${ctx}/apiv1/user/info/list";
 			var url = "${ctx}/../apiv1/user/info/list";
/*			$.getJSON(url,function(json) {
			        $('#cc1').combobox({
			            data: json.data,
			            valueField: 'username',
						textField:'name',
						groupField:'officeName',
						multiple:true,
						panelHeight:'auto',						
						onLoadSuccess: function(){
						},
						onSelect: function() {
							var docApproverIDs = $('#cc2').val($(this).combobox('getValues'));
						}		
			        });
			}); */
			
			
			$.ajax({
				type : "get",
				url : url,
				async : false,
				success : function(result) {
					var resviewersData = "[";
					for (var i = 0; i < result.data.length; i++) {
						resviewersData += "{\"id\":\"" + result.data[i].username + "\",\"text\":\"" + result.data[i].name + "\"},";
					}
					resviewersData = resviewersData.substring(0,resviewersData.length-1);
					resviewersData += "]";
					var resviewersJsonData = eval('(' + resviewersData+ ')');
					$("#reviewers").select2({
						data : resviewersJsonData,
						multiple : true
					});
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/docRouting/">待办任务</a></li>
		<li><a href="${ctx}/oa/docRouting/list">任务管理</a></li>
		<!-- <shiro:hasPermission name="oa:leave:edit"> -->
		<li><a href="${ctx}/oa/docRouting/form">发文</a></li>
		<!--</shiro:hasPermission> -->
	</ul>
	
	<form:form id="inputForm" modelAttribute="doc" action="${ctx}/oa/docRouting/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<fieldset>
			<legend>审批申请</legend>
			<table class="table-form">
				<tr>
					<td class="tit">发文抬头：</td>
					<td><form:input path="docTitle" class="required" maxlength="100"/>	</td>
				</tr>
				<!-- <tr>
					<td class="tit">审阅人列表：</td>
					<td>
						<input id= "cc1" class="easyui-combobox" name="docApproverIDs" style="width:80%;height:50px;" />
					</td>
				</tr> -->
				<tr>
					<td class="tit">选择审阅人：</td>
					<td>
						<select id="reviewers" name="docApproverIDs" class="form-control" multiple="multiple" style="width:300px; border:1 px solid #ccc;"></select>
					</td>
				</tr>
				<tr>
					<td class="tit">截止时间：</td>
					<td>
						<input id="dueDate" name="dueDate" type="datetime" readonly="readonly" maxlength="20" class="Wdate required"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
				</tr>
				<tr>
					<!-- 需要上传多个文件 -->
					<td class="tit">上传公文及附件：</td>
					<td>
					   <div>
						   
					        <input type="file" name="fileUploader" id="fileUploader" />  
					        <div id=" upload_file_queue"></div>  
					        <div  id="fileName"></div>  
					        <div style="clear: both;margin-top: 20px;cursor: pointer;">  
					             <a onclick="javascript:$('#fileUploader').uploadify('upload','*')"> 开始上传 </a>
					             <a onclick="javascript:$('#fileUploader').uploadify('cancel','*')"> 取消上传 </a>  
					        </div>  
					    </div>  
						<input id = "docAttachmentLinks" name="docAttachmentLinks" type="text"  style="width:80%;">
					</td>
				</tr>									
			</table>
		</fieldset>
	
		<div class="form-actions">
			<!--<shiro:hasPermission name="oa:leave:edit"> -->
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交"/>&nbsp;
			<!--</shiro:hasPermission>-->
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
