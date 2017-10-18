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
			<legend>承办机构结案审批：${oaCase.title}</legend>
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
					<td class="tit" rowspan="2">案情</td>
					<td colspan=5>${oaCase.normCaseDesc}</td>
				</tr>
				<tr>
					<td colspan=5>${oaCase.normAssigneePenalOpt}</td>
				</tr>
				<tr><td class="tit">案件文号</td><td class="tit" colspan="3">${oaCase.caseDocNo}</td></tr>
				<!-- 案件简报 -->
				<!-- 时间进展 -->
				<tr>
					<td class="tit">案件申报日期</td><td>${oaCase.caseRegStartDate}</td>
					<td class="tit">立案日期</td><td>${oaCase.caseRegEndDate}</td>				
					<td class="tit">调查完成日期</td><td>${oaCase.caseSurveyEndDate}</td>	
					<td class="tit">行政处罚开始日期</td><td>${oaCase.casePenalStartDate}</td>
					<td class="tit">行政处罚办结日期</td><td>${oaCase.casePenalEndDate}</td>	
				</tr>
				<!-- 时间进展 -->
				<tr>
					<td>承办人意见</td>
					<td colspan="5">${oaCase.assigneeCloseCaseOption}</td>	
				</tr>					
				<tr><td class="tit" colspan="5"><h4>填写</h4></td></tr>	
				<tr>
					<td>承办机构意见</td>
					<td colspan="5">
						<form:textarea path="institutionCloseCaseOption" class="required" rows="3" maxlength="300"/>
					</td>	
				</tr>	
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
			<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
		</div>
	</form:form>
</body>
</html>