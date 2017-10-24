<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
	<%  
	Random r= new Random();
	String ranNum1= Integer.toString(r.nextInt()+100000); 
	String ranNum2= Integer.toString(r.nextInt()+200000); 
	String ranNum3= Integer.toString(r.nextInt()+300000); 
	%>  
	
<html>
<head>
	<link href="${ctxStatic }/uploadifive/uploadify.css" rel="stylesheet" type="text/css" > 

	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic }/uploadifive/jquery-uploadifive.min.js" type="text/javascript"></script>

	<title>立案</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">	
		$(function() { 
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
				'uploadScript'     : '${ctx}/sys/utils/uploadifive.action',  
				'onUploadComplete' : function(file, data) {var obj=JSON.parse(data); $('.docFiles').val(obj.filename);}  
			});  
			
			$('#img_upload').uploadifive({  
				'auto'             : true,  
				'multi'            : true,  
				'buttonText'       :'选择图片',  
				'removeCompleted'  : true,  
			    'fileType'         : 'image',  
				'fileSizeLimit'    : '10240KB',  
				'formData'         : {  
									   'timestamp' : '<%= ranNum2 %>',  
									   'token'     : '<%= ranNum2 %>'
									 },  
				'queueID'          : 'upload_img_queue',  
				'uploadScript'     : '${ctx}/sys/utils/uploadifive.action', 
				'onUploadComplete' : function(file, data) {var obj=JSON.parse(data); $('.imgFiles').val(obj.filename);}  
			}); 
			
			$('#video_upload').uploadifive({  
				'auto'             : true,  
				'multi'            : true,  
				'buttonText'       : '选择视频',  
			    'fileType'         : 'video',  			
				'removeCompleted'  : true,  
				'fileSizeLimit'    : '10240KB',  
				'formData'         : {  
									   'timestamp' : '<%= ranNum3 %>',  
									   'token'     : '<%= ranNum3 %>'
									 },  
				'queueID'          : 'upload_video_queue',  
				'uploadScript'     : '${ctx}/sys/utils/uploadifive.action',  
				'onUploadComplete' : function(file, data) {var obj=JSON.parse(data); $('.videoFiles').val(obj.filename);}  
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
		<li><a href="${ctx}/oa/oaCase/">案件列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaCase/form/?procInsId=${oaCase.procInsId}">案件详情</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaCase" action="${ctx}/oa/oaCase/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>案件申报</legend>
			<table class="table-form">
				<!-- 案件申报 -->
				<tr>
					<td class="tit">案件名称</td>
					<td><form:input path="title" class="required" maxlength="100"/></td>			
					<td class="tit">当事人</td>
					<td><form:input path="caseParties" class="required" maxlength="100"/></td>			
					<td class="tit">法人</td>
					<td><form:input path="caseLegalAgent" class="required" maxlength="50"/></td>
				</tr>
				<tr>
					<td class="tit">联系方式</td>
					<td><form:input path="phoneNumber" class="required" maxlength="50"/></td>
					<td class="tit">地址</td>
					<td colspan="2"><form:input path="address" class="required" maxlength="150"/></td>
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
							<td>	    
								<input type="file" name="doc_upload" id="doc_upload" />  
						        <div id="upload_doc_queue"></div>
						        <div style="clear: both;margin-top: 20px;cursor: pointer;"> 
							        <a onclick="javascript:$('#doc_upload').uploadifive('upload')"> 上传 </a>
						            <a onclick="javascript:$('#doc_upload').uploadifive('stop')"> 取消上传 </a> 
						        </div>  
        					</td>
        					<td>	    
								<input type="file" name="img_upload" id="img_upload" />  
						        <div id="upload_img_queue"></div>
						        <div style="clear: both;margin-top: 20px;cursor: pointer;"> 
							        <a onclick="javascript:$('#img_upload').uploadifive('upload')"> 上传 </a>
						            <a onclick="javascript:$('#img_upload').uploadifive('stop')"> 取消上传 </a> 
						        </div>  
        					</td>
        					<td>	    
								<input type="file" name="video_upload" id="video_upload" />  
						        <div id="upload_video_queue"></div>
						        <div style="clear: both;margin-top: 20px;cursor: pointer;"> 
							        <a onclick="javascript:$('#video_upload').uploadifive('upload')"> 上传 </a>
						            <a onclick="javascript:$('#video_upload').uploadifive('stop')"> 取消上传 </a> 
						        </div>  
        					</td>
						</tr>
						<tr>
							<td><form:textarea path="caseDocuments" id="docFiles" rows="3"/></td>
							<td><form:textarea path="caseImages" id="imgFiles" rows="3"/></td>
							<td><form:textarea path="caseVideos" id="videoFiles" rows="3"/></td>
						</tr>
					</table>
					</td>				
				</tr>						
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
