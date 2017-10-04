<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

<html>
<head>
	<title>公文流转</title>
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/docRouting/">待办任务</a></li>
		<li><a href="${ctx}/oa/docRouting/list">所有任务</a></li>
		<!-- <shiro:hasPermission name="oa:leave:edit"> -->
		<li><a href="${ctx}/oa/docRouting/form">公文传阅</a></li>
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
				<tr>
					<td class="tit">审阅人列表：</td>
					<td>
					<select class="easyui-combobox" 
						name="approvers[]" multiline="true" panelMaxHeight="200px" style="width:80%;height:80px;" data-options="
							url:'${ctx}/apiv1/user/info/list',
							method:'post',
							valueField:'data',
							textField:'username',
							groupField:'officeName',
							value:[],
							multiple:true,
							panelHeight:'auto',
							label: '选择审阅人：',
							labelPosition: 'top'
							">
							</select>
					</td>
				</tr>
				<tr>
					<td class="tit">截止时间：</td>
					<td>
						<input id="dueDate" name="dueDate" type="text" readonly="readonly" maxlength="20" class="Wdate required"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
				</tr>
				<tr>
					<!-- 需要上传多个文件 -->
					<td class="tit">上传公文及附件：</td>
					<td>
						<input class="easyui-filebox" label="上传发文:" labelPosition="top" data-options="prompt:'要上传的文件...'" style="width:80%;">
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
