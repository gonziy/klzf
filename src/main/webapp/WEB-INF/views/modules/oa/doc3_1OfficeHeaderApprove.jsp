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
						multiple : true,
						placeholder: "请选择领导", //默认提示语  
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
				<!-- 发文简报 -->
				<tr><td class="tit">标题</td><td>${oaDoc3.docTitle}</td></tr>	
				<tr><td class="tit">文件及附件链接</td><td>${oaDoc3.attachLinksLinks}</td></tr>	
				<tr><td class="tit">发文建议</td><td>${oaDoc3.applyerOption}</td></tr>						
				<tr>
					<td colspan="2">
						<table style="width:100%">
							<tr><td class="tit" colspan="3"><h4>审批</h4></td></tr>
							<tr>
								<td class="tit">选择审阅领导</td>
								<td colspan="2">
								<form:select path="leaderId" class="form-control" multiple="multiple" style="width:300px; border:1 px solid #ccc;"></form:select>
								</td>
							</tr>
							<tr>
								<td class="tit">发文意见</td>
								<td colspan="2"><form:input path="officeHeaderOption" maxlength="150"/></td>	
							</tr>
							<tr style="display:none">
								<td class="tit">选择截止时间</td>
								<td colspan="2">
									<input id="dueDate" name="dueDate" type="datetime" readonly="readonly" maxlength="20"  value="2099-12-31 23:59:59"
									class="Wdate required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
								</td>
							</tr>
						</table>
					</td>	
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交审阅" onclick="$('#flag').val('yes')"/>&nbsp;
			<input id="btnSubmit" class="btn btn-inverse" type="submit" value="修 回" onclick="$('#flag').val('no')"/>&nbsp;
		</div>
	</form:form>
</body>
</html>
