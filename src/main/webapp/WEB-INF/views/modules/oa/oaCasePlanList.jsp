<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<html>
<head>
	<title>计划任务提醒</title>
	<style>
	body{ padding:5px;}
	table{ font-size:13px;}
	table thead th{text-align:center;}
	</style>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
	$(function(){
		$.get("${ctxStatic}/../apiv1/oa/case/planlist?user_id=${fns:getUser().id}",function(result){
			var jsondata = result.data;
			var list = jsondata.data;
			if(list.length>0){
				for(var i=0;i<list.length;i++){
					$("#contentTable tbody").append("<tr><td><a href=\"form?id="+list[i].id+"\">"+list[i].title+"</a></td><td>"+list[i].planRemark+"</td><td>"+list[i].expire+"</td><td><a href=\"form?id="+list[i].id+"\">查询</a>&nbsp;&nbsp;<a href=\"documents?id="+list[i].id+"\">流程预览</a></td></tr>")
				}
			}else{
				alert("暂无提醒");
			}
		})
	})
	</script>
</head>

<body>

	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>案件标题</th>
				<th>提醒事项</th>
				<th>剩余时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</body>
</html>
