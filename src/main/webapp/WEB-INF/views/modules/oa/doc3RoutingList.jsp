<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>发文管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
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
		<li><a href="${ctx}/oa/doc3Routing/list/task">待办任务</a></li>
		<li class="active"><a href="${ctx}/oa/doc3Routing/list">任务管理</a></li>
		<!--<shiro:hasPermission name="oa:doc3Routing:edit"> -->
		<li><a href="${ctx}/oa/doc3Routing/form">发文</a></li>
		<!--</shiro:hasPermission> -->
	</ul>
	<form:form id="searchForm" modelAttribute="doc" action="${ctx}/oa/doc3Routing/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>发文名称：&nbsp;</label>
			<input id="docTitle"  name="docTitle"  type="text" maxlength="100" class="input-medium" style="width:130px;" placeholder="请输入欲查询的发文名"
				value="${doc.docQueryTitle}" />
		</div>
		<div style="margin-top:8px;">
			<label>发文时间：</label>
			<input id="docPublishDateStart"  name="docPublishDateStart"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:130px;"
				value="<fmt:formatDate value="${doc.docQueryCreateDateStart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="docPublishDateEnd" name="docPublishDateEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:130px;"
				value="<fmt:formatDate value="${doc.docQueryCreateDateEnd}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>发文名称</th>
			<th>创建人</th>
			<th>创建时间</th>
			<th>截至时间</th>
			<th>审批领导</th>
			<th>传阅人列表</th>
			<th>当前环节</th>
			<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="doc">
			<c:set var="task" value="${doc.task }" />
			<c:set var="pi" value="${doc.processInstance }" />
			<c:set var="hpi" value="${doc.historicProcessInstance }" />
			<tr>
				<td>${doc.docTitle}</td>
				<td>${doc.createBy.name}</td>
				<td><fmt:formatDate value="${doc.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${doc.dueDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>	
				<td><fmt:formatDate value="${doc.leaderId}"/></td>	
				<td>${doc.reviewersIDs}</td>								
				<c:if test="${not empty task}">
					<td>${task.name}</td>
					<td><a target="_blank" href="${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}">进度</a></td>
				</c:if>
				<c:if test="${empty task}">
					<td>已结束</td>
					<td>&nbsp;</td>
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
