<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公文流转</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var url = "${ctx}/../apiv1/user/info/list";
			$.ajax({
				type : "get",
				url : url,
				async : false,
				success : function(result) {
					var resviewersData = "[";
					for (var i = 0; i < result.data.length; i++) {
						resviewersData += "{\"id\":\"" + result.data[i].id + "\",\"text\":\"" + result.data[i].name + "\"},";
					}
					resviewersData = resviewersData.substring(0,resviewersData.length-1);
					resviewersData += "]";
					var resviewersJsonData = eval('(' + resviewersData+ ')');
					$("#leaderId").select2({
						data : resviewersJsonData,
						multiple : false,
						placeholder: "请选择一个用户", //默认提示语  
			            allowClear: true  
					});
				}
			});
			
			
			
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
		<li><a href="${ctx}/oa/doc3Routing/">发文列表</a></li>
		<li class="active"><a href="${ctx}/oa/doc3Routing/form/?id=${oaDoc3.id}">发文详情</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaDoc3" action="${ctx}/oa/doc3Routing/saveDoc3" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>办公室主任审核：${oaDoc3.docTitle}</legend>
			<table class="table-form">
				<!-- 案件简报 -->
				<tr><td class="tit">文件及附件链接</td><td><a href="${oaDoc3.attachLinks}">${oaDoc3.attachLinks}</a></td></tr>					
				<tr>
					<td colspan="3">
						<table style=" width:100%;">
							<tr><td class="tit" colspan="3"><h4>通过</h4></td><td class="redtit" colspan="3"><h4 style="color:#fff">不通过</h4></td></tr>
							<tr>
								<td class="tit">选择一个审阅领导</td>
								<td colspan="2">
								<form:select path="leaderId" class="form-control" multiple="multiple" style="width:300px; border:1 px solid #ccc;"></form:select>
								<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交" onclick="$('#flag').val('yes')"/>
								
								</td>
								<td class="tit">输入修回意见</td>
								<td colspan="2"><form:input path="officeHeaderOption" maxlength="150"/>
								<input id="btnSubmit" class="btn btn-inverse" type="submit" value="修 回" onclick="$('#flag').val('no')"/>
								</td>
									
							</tr>
							<tr style="display:none">
								<td class="tit">选择截止时间</td>
								<td colspan="2">
									<input id="dueDate" name="dueDate" type="datetime" readonly="readonly" maxlength="20" 
									class="Wdate required" value="2099-12-31 23:59:59" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
								</td>
							</tr>
						</table>
					</td>	
			</table>
		</fieldset>
		<div class="form-actions">
			&nbsp;
			&nbsp;
		</div>
	</form:form>
</body>
</html>
