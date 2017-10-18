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
	<form:form id="inputForm" modelAttribute="oaCase" action="${ctx}/oa/oaCase/saveCase" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>案件初审：${oaCase.title}</legend>
			<table class="table-form">
				<!-- 案件申报 -->
				<tr>
				<tr><td class="tit" colspan=6><h4>立案初审</h4></td></tr>	
				<tr>
					<td class="tit">当事人</td><td>${oaCase.caseParties}</td>
					<td class="tit">法人</td><td>${oaCase.caseLegalAgent}</td>
					<td class="tit">联系电话</td><td>${oaCase.phoneNumber}</td>
				</tr>
				<tr>
					<td class="tit">地址</td><td>${oaCase.address}</td>					
				</tr>
				<tr>
					<td class="tit">案情简述</td>
					<td colspan="5">${oaCase.caseDescription}</td>
				</tr>	
				<tr>
					<td colspan="6">
					<table>
						<tr><th>文书材料上传</th><th>图片材料上传</th><th>视频材料上传</th></tr>
						<tr>
							<td class="tit">${caseDocuments}</td>
							<td class="tit">${caseImages}</td>
							<td class="tit">${caseVideos}</td>
						</tr>
					</table>
					</td>				
				</tr>	
				<tr>
					<td class="tit">初审结论</td>
					<td colspan="4"><form:textarea path="caseCheckResult" rows="3" maxlength="500"/></td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="初审通过" onclick="$('#flag').val('yes')"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="拒绝" onclick="$('#flag').val('no')"/>
		</div>
	</form:form>
</body>
</html>
