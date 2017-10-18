<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行政处罚审批</title>
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
			<legend>承办人行政处罚意见：${oaCase.title}</legend>
			<table class="table-form">
				<!-- 案件简报 -->
				<tr><td class="tit" colspan=6><h4>案情</h4></td></tr>	
				<tr>
					<td class="tit">当事人</td><td>${oaCase.caseParties}</td>
					<td class="tit">法人</td><td>${oaCase.caseLegalAgent}</td>
					<td class="tit">联系电话</td><td>${oaCase.phoneNumber}</td>
				</tr>
				<tr>
					<td class="tit" colspan="4">地址</td><td>${oaCase.address}</td>					
				</tr>							
				<tr>
					<td class="tit" colspan="2">案件来源</td><td>${oaCase.caseSource}</td>
					<td class="tit" colspan="2">承办人</td><td>${oaCase.assigneeIds}</td>
				</tr>
				<tr>
					<td class="tit">案情</td>
					<td colspan=5>${oaCase.getNormCaseDesc()}</td>
				</tr>
				<!-- 案件简报 -->
				<!-- 时间进展 -->
				<tr>
					<td class="tit">案件申报日期</td><td>${oaCase.caseRegStartDate}</td>
					<td class="tit">立案日期</td><td>${oaCase.caseRegEndDate}</td>				
					<td class="tit">调查完成日期</td><td>${oaCase.caseSurveyEndDate}</td>	
				</tr>
				<!-- 时间进展 -->
				
				<tr><td class="tit" colspan="5"><h4>填写</h4></td></tr>	
				<tr>
					<td class="tit">依据：</td>
					<td colspan="2">
						<form:select path ="normAssigneePenalOptPart1" class="required">
							<form:option value="1">法条1</form:option>
							<form:option value="2">法条2</form:option>
							<form:option value="3">法条3</form:option>																
						</form:select>
					</td>
					<td class="tit">给出处罚：</td>
					<td colspan="2">
							<form:select path ="normAssigneePenalOptPart2" class="required">
							<form:option value="1">处罚1</form:option>
							<form:option value="2">处罚2</form:option>
							<form:option value="3">处罚3</form:option>
							<form:option value="4">处罚4</form:option>																									
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="tit">承办人意见</td>
					<td colspan="5">
						<form:textarea path="assigneePenalOption" class="required" rows="3" maxlength="300"/>
					</td>	
				</tr>
				<tr>
					<td class="tit">案件文号</td>
					<td colspan="3"><form:input path="caseDocNo" class="required" maxlength="50"/></td>
				</tr>				
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>			
		</div>
	</form:form>
</body>
</html>