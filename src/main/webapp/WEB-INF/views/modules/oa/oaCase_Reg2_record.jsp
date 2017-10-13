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
			<legend>案情提报</legend>
			<table class="table-form">
				<!-- 案件申报 ，此时可以再次设置和核准相应的信息-->
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
					<td class="tit">案件申报材料提报</td>
					<td colspan="4"><form:textarea path="caseAttachmentLinks" rows="4"/></td>
				</tr>
				<tr>
					<td class="tit">案件来源</td>
					<td colspan="3">
						<form:select path ="caseSource" class="required" style="width:120px">
							<form:option value="1">上级交办</form:option>
							<form:option value="2">有关部门移交</form:option>
							<form:option value="3">调查发现</form:option>
							<form:option value="4">新闻媒体曝光</form:option>	
							<form:option value="5">举报投诉</form:option>
							<form:option value="6">其他</form:option>																	
						</form:select>
					</td>
				</tr>
				<tr><td class="tit" colspan="5"><h4>填写</h4></td></tr>
				<tr>																			
					<td class="tit">承办人：</td>
					<td colspan="3">
						<form:input path="assigneeIds" class="required" maxlength="150"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案情</td>
					<td colspan="3">
						<form:select path ="normCaseDescPart1" class="required">
							<form:option value="1">行为1</form:option>
							<form:option value="2">行为2</form:option>
							<form:option value="3">行为3</form:option>																
						</form:select>
					</td>
					<td class="tit">违反了</td><td>
							<form:select path ="normCaseDescPart2" class="required">
							<form:option value="1">条例1</form:option>
							<form:option value="2">条例2</form:option>
							<form:option value="3">条例3</form:option>																
						</form:select>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交审核" onclick="$('#flag').val('yes')"/>&nbsp;		
			<input id="btnCancel" class="btn" type="button" value="取消" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
