<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<%@ include file="/WEB-INF/views/include/uploadify.jsp"%>

<html>
<head>
	<title>发文</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
			//
			//var url = "${ctx}/apiv1/user/info/list";
			var url = "http://localhost:8080/day5/apiv1/user/info/list";
			$.getJSON(url,function(json) {
			        $('#cc1').combobox({
			            data: json.data,
			            valueField: 'username',
						textField:'name',
						groupField:'officeName',
						multiple:true,
						panelHeight:'auto',						
						onLoadSuccess: function(){
						},
						onSelect: function() {
							var docApproverIDs = $('#cc2').val($(this).combobox('getValues'));
						}		
			        });
			});

	        $("#fileUploader").uploadify({  
	            'swf'           : '${ctxStatic}/uploadify/uploadify.swf?var='+(new Date()).getTime(),  
	            'uploader'      : '${ctx}/test/uploadFile.action',  //服务器端方法  
	            //'formData'    : {'someKey' : 'someValue', 'someOtherKey' : 1},//传输数据json格式  
	            'height'        : 30,  //按钮高度  
	            'width'         : 100,  //按钮宽度    
	            'fileObjName'   : 'uploadify',//默认 Filedata, $_FILES控件name名称  
	            'multi'         : true,  //设置是否允许一次选择多个文件，true为允许，false不允许  
	            'auto'          : false,  //是否自动上传  
	            'buttonText'    : '选择文件',//按钮显示文字  
	            //'buttonClass' : 'uuid', //按钮辅助class  
	            'buttonCursor'  : 'hand', //设置鼠标移到按钮上的开状，接受两个值'hand'和'arrow'(手形和箭头)  
	            'debug'         : false, //开启或关闭debug模式  
	            'cancelImg'     : '${ctx }/js/uploadify/uploadify-cancel.png', //这个没测试出来，默认是放在与uploadify同级的img文件夹下  
	            'fileTypeExts'  : '*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.pdf;*.jpg;*.jpeg;*.png;*.txt;*.zip;*.7z', //文件后缀限制 默认：'*.*'  
	            'fileSizeLimit' : '10MB',//接受一个单位（B,KB,MB,GB）。如果是数字则默认单位为KB。设置为0时表示不限制  
	            'fileTypeDesc'  : 'All Files',//可选文件的描述。这个值出现在文件浏览窗口中的文件类型下拉选项中。（chrome下不支持，会显示为'自定义文件',ie and firefox下可显示描述）  
	            'method'        : 'post', //提交上传文件的方法，接受post或get两个值，默认为post  
	            'progressData'  : 'percentage',//设置文件上传时显示数据，有‘percentage’ or ‘speed’两个参数(百分比和速度)  
	            'queueID'       : 'upload_file_queue',//设置上传队列DOM元素的ID，上传的项目会增加进这个ID的DOM中。设置为false时则会自动生成队列DOM和ID。默认为false  
	            'queueSizeLimit'  : 5,//一个队列上传文件数限制  
	            'simUploadLimit'  : 5, //一次同步上传的文件数目  
	            'removeCompleted' : true, //完成时是否清除队列 默认true    
	            'removeTimeout'   : 1,   //完成时清除队列显示秒数,默认3秒    
	            'requeueErrors'   : false, //设置上传过程中因为出错导致上传失败的文件是否重新加入队列中上传  
	            'successTimeout'  : 30,   //设置文件上传后等待服务器响应的秒数，超出这个时间，将会被认为上传成功，默认为30秒  
	            'uploadLimit'     : 99,  //允许上传的最多张数   
	            'onUploadSuccess' : function(file, data, response) { //上传成功  
	                var jdata = $.parseJSON(data);  
	                $("#fileName").append("<p><em name='fileName' onclick = 'downLoadFile(this)' style='color:#555555'>"+jdata.fileName+"</em><em style='color:red' onclick = 'deleteFile(this)'>删除</em><a name='filePath' style='display:none'>"+jdata.filePath+"</a></p>");  
	                console.log( 'id: ' + file.id  
	                    + ' - 索引: ' + file.index
	                    + ' - 文件名: ' + file.name
	                    + ' - 文件大小: ' + file.size
	                    + ' - 类型: ' + file.type
	                    + ' - 创建日期: ' + file.creationdate
	                    + ' - 修改日期: ' + file.modificationdate
	                    + ' - 文件状态: ' + file.filestatus
	                    + ' - 服务器端消息: ' + data
	                    + ' - 是否上传成功: ' + response);  
	             },  
	             'onFallback':function(){  
	                alert("您未安装FLASH控件，无法一次性上传多个文件！请安装FLASH控件后再试。");  
	             },  
	             onSelectError:function(file, errorCode, errorMsg){ //选择失败  
	                    switch(errorCode) {  
	                        case -100:    
	                            alert("上传的文件数量已经超出系统限制的"+$('#uploadFile').uploadify('settings','queueSizeLimit')+"个文件！");      
	                            break;    
	                        case -110:    
	                         alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#uploadFile').uploadify('settings','fileSizeLimit')+"大小！");  
	                            break;    
	                        case -120:    
	                            alert("文件 ["+file.name+"] 大小异常！");  
	                            break;    
	                        case -130:    
	                            alert("文件 ["+file.name+"] 类型不正确！");  
	                            break;  
	                    }  
	                },  
	               /* //上传汇总 
	              'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {   
	                  $('#progress').html(totalBytesUploaded + ' bytes uploaded of ' + totalBytesTotal + ' bytes.');   
	               }, 
	              'onUploadComplete' : function(file) { //上传完成   
	                   console.log('The file ' + file.name + ' finished processing.');   
	               },   
	               //修改formData数据 ,每个文件即将上传前触发 
	               'onUploadStart' : function(file) { 
	                   $("#uploadFile").uploadify("settings", "someOtherKey", 2); 
	               }, 
	               //删除时触发   
	               'onCancel' : function(file) { 
	                   alert('The file ' + file.name + '--' + file.size + ' was cancelled.');   
	               }, 
	               //清除队列   
	               'onClearQueue' : function(queueItemCount) { 
	                   alert(queueItemCount + ' file(s) were removed from the queue');   
	               }, 
	               //调用destroy是触发   
	               'onDestroy' : function() {   
	                   alert('我被销毁了');   
	               },   
	               //每次初始化一个队列是触发   
	               'onInit' : function(instance){   
	                   alert('The queue ID is ' + instance.settings.queueID);   
	               },  
	               //上传错误   
	               'onUploadError' : function(file, errorCode, errorMsg, errorString) { 
	                   alert('The file ' + file.name + ' could not be uploaded: ' + errorString);   
	               }
	               //*/  
	        }); 
	        
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/docRouting/">待办任务</a></li>
		<li><a href="${ctx}/oa/docRouting/list">任务管理</a></li>
		<!-- <shiro:hasPermission name="oa:leave:edit"> -->
		<li><a href="${ctx}/oa/docRouting/form">发文</a></li>
		<!--</shiro:hasPermission> -->
	</ul>
	
	<form:form id="inputForm" modelAttribute="doc" action="${ctx}/oa/docRouting/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<fieldset>
			<legend>审批申请</legend>
			<table class="table-form">
				<tr>
					<td class="tit">发文抬头：</td>
					<td><form:input path="docTitle" class="required" maxlength="100"/>	</td>
				</tr>
				<tr>
					<td class="tit">审阅人列表：</td>
					<td>
						<input id= "cc1" class="easyui-combobox" name="docApproverIDs" multiline="true" style="width:80%;height:50px;"/>
					</td>
				</tr>
				<tr>
					<td class="tit">截止时间：</td>
					<td>
						<input id="dueDate" name="dueDate" type="datetime" readonly="readonly" maxlength="20" class="Wdate required"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
				</tr>
				<tr>
					<!-- 需要上传多个文件 -->
					<td class="tit">上传公文及附件：</td>
					<td>
					   <div>       
					        <input type="file" name="fileUploader" id="fileUploader" />  
					        <div id=" upload_file_queue"></div>  
					        <div  id="fileName"></div>  
					        <div style="clear: both;margin-top: 20px;cursor: pointer;">  
					             <a onclick="javascript:$('#fileUploader').uploadify('upload','*')"> 开始上传 </a>
					             <a onclick="javascript:$('#fileUploader').uploadify('cancel','*')"> 取消上传 </a>  
					        </div>  
					    </div>  
						<input id = "docAttachmentLinks" name="docAttachmentLinks" type="text"  style="width:80%;">
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
