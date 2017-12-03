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
		<li class="active"><a href="${ctx}/oa/oaCase/">案件列表</a></li>
		<li><a href="${ctx}/act/task/form?procDefId=CaseProcess:1:e253a8a5f2fb4cf4937619da3e0c8541">新建任务</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="oaCase" action="${ctx}/oa/oaCase/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="caseQueryKeyword"  name="caseQueryKeyword"  type="text" maxlength="100" class="input-medium" style="width:130px;" placeholder="请输入关键字"
				value="${oaCase.caseQueryKeyword}" />&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	
	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>标题</th>
			<th>当事人</th>
			<th>法人</th>
			<th>地址</th>
			<th>联系方式</th>
			<th>违法行为</th>
			<th>案件说明</th>
			<th>立案时间</th>
			<th>承办人</th>
			<shiro:hasPermission name="oa:oaCase:edit"><th>操作</th></shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaCase">
			<tr>
				<td><a href="${ctx}/oa/oaCase/form?id=${oaCase.id}">${oaCase.title}</a></td>
				<td>${oaCase.caseParties}</td>
				<td>${oaCase.caseLegalAgent}</td>
				<td>${oaCase.address}</td>
				<td>${oaCase.phoneNumber}</td>
				<td>${oaCase.normCaseDescPart1}</td>
				<td>${oaCase.getNormCaseDesc()}</td>
				<td><fmt:formatDate value="${oaCase.caseRegStartDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${oaCase.assigneeNames}</td>
				<shiro:hasPermission name="oa:oaCase:edit"><td>
    				<a href="${ctx}/oa/oaCase/form?id=${oaCase.id}">查询</a>
    				<a href="${ctx}/oa/oaCase/documents?id=${oaCase.id}">流程预览</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
