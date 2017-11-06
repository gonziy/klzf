<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公文流转</title>
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
		<li><a href="${ctx}/oa/doc3Routing/">发文列表</a></li>
		<li class="active"><a href="${ctx}/oa/doc3Routing/form/?id=${oaDoc3.id}">发文详情</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaDoc3" action="${ctx}/oa/doc3Routing/saveDoc3" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>办公室主任发文：${oaDoc3.docTitle}</legend>
			<table class="table-form">
				<!-- 案件简报 -->
				<tr>
					<td class="tit">文件及附件链接</td><td colspan="2">${oaDoc3.attachLinks}</td>					
				</tr>
				<tr>
					<td class="tit">截止时间</td><td><fmt:formatDate value="${oaDoc3.dueDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
					<td class="tit">领导意见</td><td colspan="2">${oaDoc3.leaderOption}</td>				
				</tr>
				<tr>
					<td class="tit">传阅人列表</td><td colspan="2">${oaDoc3.reviewersIDs}</td>
				</tr>		
				<tr><td class="tit" colspan="3" style="color: red"><h4>填写</h4></td></tr>				
				<tr>
					<td class="tit">选择传阅人</td>
					<td colspan="2"><form:input path="reviewersIDs1" maxlength="200"/>${oaDoc3.reviewersIDs1}</td>
				</tr>		
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="发 文" onclick="$('#flag').val('yes')"/>&nbsp;
		</div>
	</form:form>
</body>
</html>
