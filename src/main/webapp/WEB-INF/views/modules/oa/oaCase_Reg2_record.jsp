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
					$("#assigneeIds").select2({
						data : resviewersJsonData,
						multiple : true
					});
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
			<legend>案情提报：${oaCase.title}</legend>
			<table class="table-form">
				<!-- 案件申报 ，此时可以再次设置和核准相应的信息-->
				<tr>
					<td class="tit">当事人</td>
					<td><form:input path="caseParties" class="required" maxlength="100"/></td>			
					<td class="tit">法人</td>
					<td><form:input path="caseLegalAgent" class="required" maxlength="50"/></td>
					<td class="tit">联系方式</td>
					<td><form:input path="phoneNumber" class="required" maxlength="50"/></td>
				</tr>
				<tr>
					<td class="tit">地址</td>
					<td colspan="5"><form:input path="address" class="required" maxlength="150"/></td>
				</tr>
				<tr>
					<td class="tit">案情简述</td>
					<td colspan="4"><form:textarea path="caseDescription" class="required" rows="5" maxlength="500"/></td>
				</tr>
				<tr>
					<td colspan="6">
					<table>
						<tr><th>文书材料上传</th><th>图片材料上传</th><th>视频材料上传</th></tr>
						<tr>
							<td><form:textarea path="caseDocuments" rows="3"/></td>
							<td><form:textarea path="caseImages" rows="3"/></td>
							<td><form:textarea path="caseVideos" rows="3"/></td>
						</tr>
					</table>
					</td>				
				</tr>	
				<tr>
					<td class="tit">案件来源</td>
					<td colspan="3">
						<form:select path ="caseSource" class="required" style="width:120px">
							<form:option value="上级交办">上级交办</form:option>
							<form:option value="有关部门移交">有关部门移交</form:option>
							<form:option value="调查发现">调查发现</form:option>
							<form:option value="新闻媒体曝光">新闻媒体曝光</form:option>	
							<form:option value="举报投诉">举报投诉</form:option>
							<form:option value="其他">其他</form:option>																	
						</form:select>
					</td>
				</tr>
				<tr><td class="tit" colspan="6"><h4>填写</h4></td></tr>
				<tr>																			
					<td class="tit">承办人：</td>
					<td colspan="2">
						<form:select path="assigneeIds" class="form-control" multiple="multiple" style="width:300px; border:1 px solid #ccc;">
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="tit">案情</td>
					<td colspan="2">
						<form:select path ="normCaseDescPart1" class="required">
							<form:option value="行为1">行为1</form:option>
							<form:option value="行为2">行为2</form:option>
							<form:option value="行为3">行为3</form:option>																
						</form:select>
					</td>
					<td class="tit">违反了</td>
					<td colspan="2">
						<form:select path ="normCaseDescPart2" class="required">
							<form:option value="法条1">条例1</form:option>
							<form:option value="法条2">条例2</form:option>
							<form:option value="法条3">条例3</form:option>																
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="tit">是否违法建设</td>
					<td><form:checkbox path="illegalConstructionFlag"/></td>  
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交审核" onclick="$('#flag').val('yes')"/>&nbsp;		
			<input id="btnCancel" class="btn" type="button" value="取消" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>