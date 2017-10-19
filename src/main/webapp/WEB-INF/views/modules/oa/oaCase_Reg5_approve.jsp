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
			<legend>主管领导审核：${oaCase.title}</legend>
			<table class="table-form">
				<!-- 案件申报 -->
				<tr><td class="tit" colspan=6><h4>立案审批</h4></td></tr>	
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
					<td class="tit">案件申报日期</td>
					<td class="tit"><fmt:formatDate value="${oaCase.caseRegStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>	
				</tr>					
				<tr>
					<td class="tit">初审结论</td>
					<td colspan="4">${oaCase.caseCheckResult}</td>
				</tr>
				<tr>
					<td class="tit">承办机构意见</td>
					<td colspan="4">${oaCase.institutionRegOption}</td>	
					<td>
						<c:choose>
						<c:when test="${oaCase.institutionRegApproval==true}"><h4>同意</h4></c:when>
						<c:otherwise><h4>不同意</h4></c:otherwise>
						</c:choose>
					</td>				
				</tr>
				<tr>
					<td class="tit">分管领导意见</td>
					<td colspan="4">${oaCase.deptLeaderRegOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.deptLeaderRegApproval==true}"><h4>同意</h4></c:when>
						<c:otherwise><h4>不同意</h4></c:otherwise>
						</c:choose>
					</td>				
				</tr>				
				<tr><td class="tit" colspan="6"><h4>填写</h4></td></tr>				
				<tr>
					<td class="tit">主管领导意见</td>
					<td colspan="4">
						<form:textarea path="mainLeaderRegOption" class="required" rows="5" maxlength="300"/>
					</td>					
				</tr>
				<!-- 立案结束 -->	
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="通过审核" onclick="$('#flag').val('yes')"/>&nbsp;		
			<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
		</div>
	</form:form>
</body>
</html>
