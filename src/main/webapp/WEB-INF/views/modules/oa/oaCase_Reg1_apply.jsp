<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>立案</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
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
		<li><a href="${ctx}/oa/oaCase/">案件列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaCase/form/?procInsId=${oaCase.procInsId}">案件详情</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaCase" action="${ctx}/oa/oaCase/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>案件申报</legend>
			<table class="table-form">
				<!-- 案件申报 -->
				<tr>
					<td class="tit">当事人</td>
					<td><form:input path="caseParties" class="required" maxlength="100"/></td>			
					<td class="tit">法人</td>
					<td><form:input path="caseLegalAgent" class="required" maxlength="50"/></td>
					<td class="tit">联系方式</td>
					<td><form:input path="phoneNumber" class="required" maxlength="50"/></td>
				</tr>
				<tr>
					<td class="tit">地址</td>
					<td colspan="4"><form:input path="address" class="required" maxlength="150"/></td>
				</tr>
				<tr>
					<td class="tit">案情简述</td>
					<td colspan="4"><form:textarea path="caseDescription" class="required" rows="5" maxlength="500"/></td>
				</tr>
				<tr>
					<td class="tit">案件申报材料</td>
					<td colspan="4"><form:textarea path="caseAttachmentLinks" rows="4"/></td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
