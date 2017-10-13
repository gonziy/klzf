<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行政执法流程</title>
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
		<li class="active"><a href="#"><shiro:hasPermission name="oa:oaCase:edit">${oaCase.act.taskName}</shiro:hasPermission><shiro:lacksPermission name="oa:oaCase:edit">查看</shiro:lacksPermission></a></li>
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
			<legend>${oaCase.act.taskName}</legend>
						<table class="table-form">
				<!-- 案件申报 -->
				<tr>
					<td class="tit">当事人</td><td>${oaCase.caseParties}</td>			
					<td class="tit">法人</td><td>${oaCase.caseLegalAgent}</td>
				</tr>
				<tr>
					<td class="tit">联系方式</td><td>${oaCase.phoneNumber}</td>
					<td class="tit">地址</td><td>${oaCase.address}</td>
				</tr>
				<tr><td class="tit">案情简述</td><td colspan="3">${oaCase.caseDescription}</td></tr>
				<tr><td class="tit">案件申报材料提报</td><td colspan="3">${oaCase.caseAttachmentLinks}</td></tr>
				<tr><td class="tit">案件来源</td><td>${oaCase.caseSource}</td></tr>
				<tr>																			
					<td class="tit">承办人</td>
					<td colspan="2">${oaCase.assigneeIds}</td>
				</tr>
				<tr><td class="tit">案情</td><td>${oaCase.getNormCaseDesc()}</td></tr>
				<tr><td class="tit">承办机构立案意见</td><td colspan="5">${oaCase.institutionRegOption}</td></tr>
				<tr><td class="tit">分管领导立案意见</td><td colspan="5">${oaCase.deptLeaderRegOption}</td></tr>				
				<tr><td class="tit">主管领导立案意见</td><td colspan="5">${oaCase.mainLeaderRegOption}</td></tr>
				<!-- 立案结束 -->	
				<tr><td class="tit">案件调查材料提报</td><td colspan="3">${oaCase.caseAttachmentLinks}</td></tr>
				<tr><td class="tit">调查结束</td><td>${oaCase.caseSurveyEndDate}</td></tr>
				<!-- 调查结束 -->		
				<tr><td class="tit" colspan=3>案件情况</td><td>${oaCase.getNormAssigneePenalOpt()}</td></tr>
				<tr><td class="tit" colspan=3>承办人意见</td><td colspan="3">${oaCase.assigneePenalOption}</td></tr>				
				<tr><td class="tit">承办机构行政处罚意见</td><td colspan="5">${oaCase.institutionPenalOption}</td></tr>
				<tr><td class="tit">案件管理中心行政处罚意见</td><td colspan="5">${oaCase.caseMgtCenterPenalOption}</td></tr>
				<tr><td class="tit">分管领导行政处罚意见</td><td colspan="5">${oaCase.deptLeaderPenalOption}</td>	</tr>				
				<tr><td class="tit">主管领导行政处罚意见</td><td colspan="5">${oaCase.mainLeaderPenalOption}</td>	</tr>
				<!-- 行政处罚 -->
				<tr><td class="tit">承办人结案意见</td><td colspan="3">${oaCase.assigneeCloseCaseOption}</td></tr>				
				<tr><td class="tit">承办单位结案意见</td><td colspan="5">${oaCase.institutionCloseCaseOption}</td></tr>
				<tr><td class="tit">案件管理中心结案意见</td><td colspan="5">${oaCase.caseMgtCenterCloseCaseOption}</td></tr>		
				<tr><td class="tit">主管领导结案意见</td><td colspan="5">${oaCase.mainLeaderCloseCaseOption}</td></tr>
			</table>
		</fieldset>
		
		<div class="form-actions">
			<shiro:hasPermission name="oa:oaCase:edit">
				<c:if test="${oaCase.act.taskDefKey eq 'apply_end'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="兑 现" onclick="$('#flag').val('yes')"/>&nbsp;
				</c:if>
				<c:if test="${oaCase.act.taskDefKey ne 'apply_end'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
					<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<act:histoicFlow procInsId="${oaCase.act.procInsId}"/>
	</form:form>
</body>
</html>
