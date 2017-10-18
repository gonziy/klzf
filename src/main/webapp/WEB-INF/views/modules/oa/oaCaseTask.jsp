<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>待办任务</title>
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
		<li class="active"><a href="${ctx}/oa/oaCase/list/task">待办任务</a></li>
		<li><a href="${ctx}/oa/oaCase/">案件列表</a></li>
		<shiro:hasPermission name="oa:oaCase:edit"><li><a href="${ctx}/oa/oaCase/form">开始执法流程</a></li></shiro:hasPermission>
	</ul>

	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th>创建人</th>
				<th>当事人</th>
				<th>法人</th>
				<th>联系方式</th>
				<th>案情说明</th>
				<th>申请时间</th>			
				<th>立案时间</th>
				<th>承办人</th>
				<th>调查结束时间</th>
				<th>行政处罚决定</th>
				<th>结案时间</th>
				<th>流程状态</th>
				<th>操作</th>
			</tr>
		<tbody>
			<c:forEach items="${oaCases}" var="oaCase">
				<c:set var="task" value="${oaCase.task}" />
				<c:set var="pi" value="${oaCase.processInstance}" />
				<tr id="${oaCase.id }" tid="${oaCase.id}">
					<td>${oaCase.createBy.Name}</td>
					<td>${oaCase.caseParties}</td>
					<td>${oaCase.caseLegalAgent}</td>
					<td>${oaCase.phoneNumber}</td>
					<td>${oaCase.getNormCaseDesc()}</td>									
					<td><fmt:formatDate value="${doc.caseRegStartDate}" type="both"/></td>
					<td><fmt:formatDate value="${doc.caseRegEndDate}" type="both"/></td>
					<td>${oaCase.assigneeIds}</td>
					<td><fmt:formatDate value="${doc.caseSurveyEndDate}" type="both"/></td>
					<td>${oaCase.getNormAssigneePenalOpt()}</td>
					<td><fmt:formatDate value="${doc.caseCloseUpEndDate}" type="both"/></td>
					<td>${pi.suspended ? "已挂起" : "正常" }</td>
					<td>
						<a target="_blank" href="${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}">进度</a>
						<c:if test="${empty task.assignee}">
							<a class="claim" href="#" onclick="javescript:claim('${task.id}');">签收</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<%-- 此处用tkey记录当前节点的名称 --%>
							<a class="handle" href="#" data-tkey="${task.taskDefinitionKey}" data-tname="${task.name}"  data-id="${doc.id}"  data-tid="${task.id}">办理</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>