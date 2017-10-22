<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>执法案件管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/oaCase/list/task">待办任务</a></li>
		<li class="active"><a href="${ctx}/oa/oaCase/">案件列表</a></li>
		<shiro:hasPermission name="oa:oaCase:edit"><li><a href="${ctx}/oa/oaCase/form">开始执法流程</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="oaCase" action="${ctx}/oa/oaCase/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>按承办人查询：</label>
		<sys:treeselect id="user" name="user.id" value="${oaCase.caseQueryAssignee.id}" labelName="user.name" labelValue="${oaCase.caseQueryAssignee.name}" 
			title="承办人" url="/sys/office/treeData?type=3" cssStyle="width:150px" allowClear="true" notAllowSelectParent="true"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	
	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr><th>当事人</th><th>法人</th><th>案件说明</th><th>申请时间</th><th>立案时间</th><th>承办人</th><th>调查结束时间</th><th>行政处罚决定</th><th>结案状态</th><th>结案时间</th><shiro:hasPermission name="oa:oaCase:edit"><th>操作</th></shiro:hasPermission></tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaCase">
			<tr>
				<td><a href="${ctx}/oa/oaCase/form?id=${oaCase.id}">${oaCase.caseParties}</a></td>
				<td>${oaCase.caseLegalAgent}</td>
				<td>${oaCase.getNormCaseDesc()}</td>
				<td><fmt:formatDate value="${oaCase.caseRegStartDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${oaCase.caseRegEndDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${oaCase.assigneeIds}</td>
				<td><fmt:formatDate value="${oaCase.caseSurveyEndDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${oaCase.getNormAssigneePenalOpt()}</td>	
				<td>结案状态</td>
				<td><fmt:formatDate value="${oaCase.caseSurveyEndDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>								
				<shiro:hasPermission name="oa:oaCase:edit"><td>
    				<a href="${ctx}/oa/oaCase/form?id=${oaCase.id}">查询</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
