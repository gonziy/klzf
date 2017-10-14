<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>案件管理</title>
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
			<legend>结案审批</legend>
			<table class="table-form">
				<!-- 案件简报 -->
				<tr>
					<td class="tit">当事人</td><td>${oaCase.caseParties}</td>
					<td class="tit">法人</td><td>${oaCase.caseLegalAgent}</td>
					<td class="tit">联系电话</td><td>${oaCase.phoneNumber}</td>
				</tr>
				<tr>
					<td class="tit">地址</td><td>${oaCase.address}</td>					
				</tr>							
				<tr>
					<td class="tit">案件来源</td><td>${oaCase.caseSource}</td>
					<td class="tit">承办人</td><td>${oaCase.assigneeIds}</td>
				</tr>
				<tr>
					<td class="tit">案情</td>
					<td colspan=5>${oaCase.getNormCaseDesc()}</td>
				</tr>
				<tr><td class="tit" colspan=6><h4>行政处罚</h4></td></tr>		
				<tr>
					<!-- 此处并不清楚最终的处罚意见会不会更新上 -->
					<td class="tit" rowspan=2>处罚意见</td>
					<td colspan=5>${oaCase.getNormAssigneePenalOpt()}</td>
				</tr>
				<!-- 案件简报 -->
				<!-- 时间进展 -->
				<tr>
					<td class="tit">案件申报日期</td><td>${oaCase.caseRegStartDate}</td>
					<td class="tit">立案日期</td><td>${oaCase.caseRegEndDate}</td>				
				</tr>
				<tr>
					<td class="tit">调查完成日期</td><td>${oaCase.caseSurveyEndDate}</td>	
				</tr>
				<tr>
					<td class="tit">处罚审批办理日期</td><td>${oaCase.casePenalStartDate}</td>
					<td class="tit">处罚审批办结日期</td><td>${oaCase.casePenalEndDate}</td>				
				</tr>
				<!-- 时间进展 -->
				
				<tr><td class="tit" colspan=6><h4>结案审批</h4></td></tr>				
				<tr>
					<td class="tit">承办人意见</td>
					<td colspan="3">
						<form:textarea path="assigneeCloseCaseOption" class="required" rows="5" maxlength="300"/>
					</td>	
				</tr>				
				<tr>
					<td class="tit">承办单位意见</td>
					<td colspan="5">
						<form:textarea path="institutionCloseCaseOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案件管理中心意见</td>
					<td colspan="5">
						<form:textarea path="caseMgtCenterCloseCaseOption" class="required" rows="5" maxlength="300"/>					
					</td>
				</tr>		
				<tr>
					<td class="tit">主管领导意见</td>
					<td colspan="5">
						<form:textarea path="mainLeaderCloseCaseOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>			
			</table>
		</fieldset>
		
		<act:histoicFlow procInsId="${oaCase.act.procInsId}" />
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
