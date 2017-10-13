<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>案件录入</title>
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
		<li class="active"><a href="${ctx}/oa/oaCase/form?id=${oaCase.id}"><shiro:hasPermission name="oa:oaCase:edit">办理${not empty oaCase.id?'修改':'申请'}流程</shiro:hasPermission><shiro:lacksPermission name="oa:oaCase:edit">查看</shiro:lacksPermission></a></li>
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
			<legend>案件调查</legend>
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
					<td class="tit">案情简述</td>
					<td colspan="5">${oaCase.caseDescription}</td>
				</tr>										
				<tr>
					<td class="tit">案件来源</td><td>${oaCase.caseSource}</td>
					<td class="tit">承办人</td><td>${oaCase.assigneeIds}</td>
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
				</tr>
				<!-- 时间进展 -->
				
				<tr><td class="tit" colspan=6><h4>案件调查</h4></td></tr>	
				<tr>
					<td class="tit">案件调查材料提报</td>
					<td colspan="3">
						<form:textarea path="caseAttachmentLinks" rows="4"/>
					</td>
				</tr>
				<tr>
					<td>调查材料维护--更新</td>
					<td>调查结束</td>
				</tr>
				<!-- 调查结束 -->		
			</table>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="oa:oaCase:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
				<c:if test="${not empty oaCase.id}">
					<input id="btnSubmit2" class="btn btn-inverse" type="submit" value="撤销申请" onclick="$('#flag').val('no')"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<c:if test="${not empty oaCase.id}">
			<act:histoicFlow procInsId="${oaCase.act.procInsId}" />
		</c:if>
	</form:form>
</body>
</html>
