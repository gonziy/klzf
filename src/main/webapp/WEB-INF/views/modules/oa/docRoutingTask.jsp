<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<html>
<head>
	<title>公文流转</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".handle").click(function(){
				var obj = $(this);
				// 任务id
				var taskId = obj.data("tid");
				// 传递对象的id
				var docId = obj.data("id");
				// 标识任务的每一步进程
				var tkey=obj.data("tkey");

				// 审批文件
				if(tkey=="utDocApprove") {
					// json结果
					$.getJSON("${ctx}/oa/docRouting/detail/" + docId , function(data){
						 var html= Mustache.render($("#docApproveTemplate").html(),data);
						 top.$.jBox(html, { title: "流程["+obj.data("tname") + "]",buttons:{"确定":"yes","代办":"no"},submit: function (v, h, f) {
							 //同意
							 if(v=="yes") {
									complete(taskId, [{										
										key: 'bNeedCommission',	// 在流程bpmn文件中定义的处理节点
										value: false,		// 值
										type: 'B'			// 变量类型
									}]);
							//驳回
							 } 
							 else if (v=="no") {
										complete(taskId, [{
											key: 'bNeedCommission',
											value: true,
											type: 'B'
										}]);
							 }
						 }
						 });
					});
				}
				else if(tkey=="utDocAssigneeApprove")
				{
					$.getJSON("${ctx}/oa/docRouting/detail-with-vars/" + docId + "/" + taskId, function(data){
						 var html= Mustache.render($("#docAssigneeConfirmTemplate").html(),data);
						 top.$.jBox(html, { title: "流程["+obj.data("tname") + "]",buttons:{"确认":"yes","取消":"no"},submit: function (v, h, f) {
							 //同意
							 if(v=="yes") {
									complete(taskId, [{										
										key: 'bAssigneeSayOk',	// 在流程bpmn文件中定义的处理节点
										value: true,		// 值
										type: 'B'			// 变量类型
									}]);
							//驳回
							 } 
							 else if (v=="no") {
										complete(taskId, [{
											key: 'bAssigneeSayOk',
											value: false,
											type: 'B'
										}]);
							 }
						 }
						 });
					});
					
				}
			})
		});
		
		/**
		 * 完成任务
		 * @param {Object} taskId
		 */
		function complete(taskId, variables) {
			// 转换JSON为字符串
		    var keys = "", values = "", types = "";
			if (variables) {
				$.each(variables, function(idx) {
					if (keys != "") {
						keys += ",";
						values += ",";
						types += ",";
					}
					keys += this.key;
					values += this.value;
					types += this.type;
				});
			}
			// 发送任务完成请求
		    $.post('${ctx}/act/task/complete/', {
		    	taskId: taskId,
		        "vars.keys": keys,
		        "vars.values": values,
		        "vars.types": types
		    }, function(data) {
		        top.$.jBox.tip('任务完成');
		        //location = '${pageContext.request.contextPath}' + data;
		        location.reload();
		    });
		}

		/**
		 * 签收任务
		 * @param {Object} taskId
		 */
		function claim(taskId) {
			$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {
	        	top.$.jBox.tip('签收完成');
	            //location = '${pageContext.request.contextPath}' + data;
	        	location.reload();
		    });
		}
	</script>
	
	<script type="text/template" id="docApproveTemplate">
		<table class="table table-striped ">
		    <tr>
				<td>发文：</td>
				<td><font color="red">{{docTitle}}</font></td>			
			</tr>
			
			<tr>				
				<!-- 显示文件正文 -->
				<td>
				  <h2>文件正文</h2>
				</td>
			</tr>
			<tr>				
				<!-- 显示文件链接 -->
				<td>
				  <p>dockFileLinks</p>
				</td>
			</tr>

			<tr>
				<td><font color="red">截至时间：</font></td>
				<td>{{dueDate}}</td>
			</tr>			
			<tr>
				<!-- 需要对代办人填写情况进行检查 -->			
				<td>选择代办人：</td>
				<td>
					<p>select assignee</p>
				</td>
			</tr>
		</table>
	</script>
	<script type="text/template" id="docAssigneeConfirmTemplate">
		<table class="table table-striped ">
		    <tr>
				<td>发文：</td>
				<td><font color="red">{{docTitle}}</font></td>			
			</tr>
			
			<tr>				
				<!-- 显示文件正文 -->
				<td>
				  <h2>文件正文</h2>
				</td>
			</tr>
			<tr>				
				<!-- 显示文件链接 -->
				<td>
				  <p>dockFileLinks</p>
				</td>
			</tr>

			<tr>
				<td>截至时间：</td>
				<td><font color="red">{{dueDate}}</font></td>
			</tr>
		</table>
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/docRouting/list/task">待办任务</a></li>
		<li><a href="${ctx}/oa/docRouting/list">任务管理</a></li>
		<!--<shiro:hasPermission name="oa:leave:edit"> -->
			<li><a href="${ctx}/oa/docRouting/form">发文</a></li>
		<!--</shiro:hasPermission> -->
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th>发文名称</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>截至时间</th>
				<th>审批人列表</th>			
				<th>当前节点</th>
				<th>任务创建时间</th>
				<th>流程状态</th>
				<th>操作</th>
			</tr>
		<tbody>
			<c:forEach items="${docs}" var="doc">
				<c:set var="task" value="${doc.task}" />
				<c:set var="pi" value="${doc.processInstance}" />
				<tr id="${doc.id }" tid="${task.id}">
					<td>${doc.docTitle}</td>
					<td>${doc.createBy.name}</td>
					<td><fmt:formatDate value="${doc.createDate}" type="both"/></td>
					<td><fmt:formatDate value="${doc.dueDate}" type="both"/></td>
					<td>${task.name}</td>
					<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
					<td>${pi.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${doc.processDefinition.version}</b></td>
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