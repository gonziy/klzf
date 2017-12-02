<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
	<%  
	Random r= new Random();
	String ranNum1= Integer.toString(r.nextInt()+100000); 
	%>  
	
<html>
<head>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<!--引入CSS-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
	<!--引入JS-->
	<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.js"></script>
		
	<title>上传公文</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		jQuery(function() {
			 var $ = jQuery,
	        $list = $('#fileList'),
	        // 优化retina, 在retina下这个值是2
	        ratio = window.devicePixelRatio || 1,
	
	        // 缩略图大小
	        thumbnailWidth = 100 * ratio,
	        thumbnailHeight = 100 * ratio,
	
	        // Web Uploader实例
	        uploader;
	
	    // 初始化Web Uploader
	    uploader = WebUploader.create({
	        // 自动上传。
	        auto: true,
			// swf文件路径
			swf: '${ctxStatic}/webuploader/Uploader.swf',
			// 文件接收服务端。
			server: '${ctx}/../apiv1/oa/files/webupload',
	        // 选择文件的按钮。可选。
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        pick: '#filePicker',
	    });
	
	    // 当有文件添加进来的时候
	    uploader.on( 'fileQueued', function( file ) {
	        var $li = $(
	                '<div id="' + file.id + '" class="file-item thumbnail">' +
	                    '<img>' +
	                    '<div class="info">' + file.name + '</div>' +
	                '</div>'
	                ),
	            $img = $li.find('img');
	
	        $list.append( $li );
	    });
	
	    // 文件上传过程中创建进度条实时显示。
	    uploader.on( 'uploadProgress', function( file, percentage ) {
	        var $li = $( '#'+file.id ),
	            $percent = $li.find('.progress span');
	
	        // 避免重复创建
	        if ( !$percent.length ) {
	            $percent = $('<p class="progress"><span></span></p>')
	                    .appendTo( $li )
	                    .find('span');
	        }
	
	        $percent.css( 'width', percentage * 100 + '%' );
	    });
	
	    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
	    uploader.on( 'uploadSuccess', function( file, response) {
	        $( '#'+file.id ).addClass('upload-state-done');
	        var path = response.data.path + ";";
	        $("#attachLinks").val($("#attachLinks").val() + path);
	    });
	
	    // 文件上传失败，现实上传出错。
	    uploader.on( 'uploadError', function( file ) {
	        var $li = $( '#'+file.id ),
	            $error = $li.find('div.error');
	
	        // 避免重复创建
	        if ( !$error.length ) {
	            $error = $('<div class="error"></div>').appendTo( $li );
	        }
	
	        $error.text('上传失败');
	    });
	
	    // 完成上传完了，成功或者失败，先删除进度条。
	    uploader.on( 'uploadComplete', function( file ) {
	        $( '#'+file.id ).find('.progress').remove();
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
						<div id="uploader">
						    <!--用来存放item-->
						    <div id="fileList" class="uploader-list"></div>
						    <div id="filePicker">选择文件</div>
						</div> 
					    <form:input path="attachLinks" class="required" style="width:80%; display:none;"/>	
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
