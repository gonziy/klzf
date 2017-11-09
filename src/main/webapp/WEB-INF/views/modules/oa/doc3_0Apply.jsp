<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
	<%  
	Random r= new Random();
	String ranNum1= Integer.toString(r.nextInt()+100000); 
	%>  
	
<html>
<head>
	<link href="${ctxStatic }/uploadifive/uploadify.css" rel="stylesheet" type="text/css" > 

	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic }/uploadifive/jquery-uploadifive.min.js" type="text/javascript"></script>
	
	<title>上传公文</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//
			$('#doc_upload').uploadifive({  
				'auto'             : true,  
				'multi'            : true,  
				'buttonText'       : '选择文案',  
				'removeCompleted'  : true,  
			    //'fileType'         : 'document',  
				'fileSizeLimit'    : '10240KB',  
				'formData'         : {  
									   'timestamp' : '<%= ranNum1 %>',  
									   'token'     : '<%= ranNum1 %>'
									 },  
				'queueID'          : 'upload_doc_queue',  
				'uploadScript'     : '${ctx}/sys/utils/uploadifive.action'
				//'onUploadComplete' : function(file, data) {var obj=JSON.parse(data); $('.docFiles').val(obj.filename);}  
			});  	
			
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('提交中，请稍等...');
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
			
			$.ajax({
				type : "get",
				url : url,
				async : false,
				success : function(result) {
					var resviewersData = "[";
					for (var i = 0; i < result.data.length; i++) {
						resviewersData += "{\"id\":\"" + result.data[i].username + "\",\"text\":\"" + result.data[i].name + "\"},";
					}
					resviewersData = resviewersData.substring(0,resviewersData.length-1);
					resviewersData += "]";
					var resviewersJsonData = eval('(' + resviewersData+ ')');
					$("#reviewers").select2({
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
		<li><a href="${ctx}/oa/doc3Routing/list">发文列表</a></li>
		<li class="active"><a href="${ctx}/oa/doc3Routing/form/?id=${oaDoc3.id}">发文详情</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaDoc3" action="${ctx}/oa/doc3Routing/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<fieldset>
			<legend>公文提报</legend>
			<table class="table-form">
				<tr>
					<td class="tit">发文抬头：</td>
					<td><form:input path="docTitle" class="required" maxlength="100"/>	</td>
				</tr>
				<!-- 自动选择办公室领导审阅 -->
				<tr>
					<!-- 需要上传多个文件 -->
					<td class="tit">上传公文及附件：</td>
					<td>
					   <div> 
							<input type="file" name="doc_upload" id="doc_upload" />  
					        <div id="upload_doc_queue"></div>
					        <div style="clear: both;margin-top: 20px;cursor: pointer;"> 
						        <a onclick="javascript:$('#doc_upload').uploadifive('upload')"> 上传 </a>
					            <a onclick="javascript:$('#doc_upload').uploadifive('stop')"> 取消上传 </a> 
					        </div>
					    </div>  
					    <form:input path="attachLinks" class="required" style="width:80%;"/>	
					</td>
				</tr>									
			</table>
		</fieldset>
	
		<div class="form-actions">
			<!--<shiro:hasPermission name="oa:leave:edit"> -->
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交"/>&nbsp;
			<!--</shiro:hasPermission>-->
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
