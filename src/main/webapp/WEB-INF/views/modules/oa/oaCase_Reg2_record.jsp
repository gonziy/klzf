<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>立案</title>
	<meta name="decorator" content="default"/>
	<!--引入CSS-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
	<!--引入JS-->
	<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.js"></script>
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
	        $("#caseDocuments").append(path);
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
	<script type="text/javascript">
	jQuery(function() {
	    var $ = jQuery,
	        $list2 = $('#fileList2'),
	        // 优化retina, 在retina下这个值是2
	        ratio = window.devicePixelRatio || 1,
	
	        // 缩略图大小
	        thumbnailWidth = 100 * ratio,
	        thumbnailHeight = 100 * ratio,
	
	        // Web Uploader实例
	        uploader2;
	
	    // 初始化Web Uploader
	    uploader2 = WebUploader.create({
	        // 自动上传。
	        auto: true,
			// swf文件路径
			swf: '${ctxStatic}/webuploader/Uploader.swf',
			// 文件接收服务端。
			server: '${ctx}/../apiv1/oa/files/webupload',
	        // 选择文件的按钮。可选。
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        pick: '#filePicker2',
	        // 只允许选择图片文件。
		    accept: {
		        title: 'Images',
		        extensions: 'gif,jpg,jpeg,bmp,png',
		        mimeTypes: 'image/*'
		    }
	    });
	
	
	    // 当有文件添加进来的时候
	    uploader2.on( 'fileQueued', function( file ) {
	        var $li = $(
	                '<div id="' + file.id + '" class="file-item thumbnail">' +
	                    '<img>' +
	                    '<div class="info">' + file.name + '</div>' +
	                '</div>'
	                ),
	            $img = $li.find('img');
	
	        $list2.append( $li );
	    });
	    // 文件上传过程中创建进度条实时显示。
	    uploader2.on( 'uploadProgress', function( file, percentage ) {
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
	    uploader2.on( 'uploadSuccess', function( file, response) {
	        $( '#'+file.id ).addClass('upload-state-done');
	        var path = response.data.path + ";";
	        $("#caseImages").append(path);
	    });
	
	    // 文件上传失败，现实上传出错。
	    uploader2.on( 'uploadError', function( file ) {
	        var $li = $( '#'+file.id ),
	            $error = $li.find('div.error');
	
	        // 避免重复创建
	        if ( !$error.length ) {
	            $error = $('<div class="error"></div>').appendTo( $li );
	        }
	
	        $error.text('上传失败');
	    });
	
	    // 完成上传完了，成功或者失败，先删除进度条。
	    uploader2.on( 'uploadComplete', function( file ) {
	        $( '#'+file.id ).find('.progress').remove();
	    });
	});
	</script>
	<script type="text/javascript">
	jQuery(function() {
	    var $ = jQuery,
	        $list3 = $('#fileList3'),
	        // 优化retina, 在retina下这个值是2
	        ratio = window.devicePixelRatio || 1,
	
	        // 缩略图大小
	        thumbnailWidth = 100 * ratio,
	        thumbnailHeight = 100 * ratio,
	
	        // Web Uploader实例
	        uploader3;
	
	    // 初始化Web Uploader
	    uploader3 = WebUploader.create({
	        // 自动上传。
	        auto: true,
			// swf文件路径
			swf: '${ctxStatic}/webuploader/Uploader.swf',
			// 文件接收服务端。
			server: '${ctx}/../apiv1/oa/files/webupload',
	        // 选择文件的按钮。可选。
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        pick: '#filePicker3',
	        threads:1,//上传并发数。允许同时最大上传进程数。
	    });
	
	
	    // 当有文件添加进来的时候
	    uploader3.on( 'fileQueued', function( file ) {
	        var $li = $(
	                '<div id="' + file.id + '" class="file-item thumbnail">' +
	                    '<img>' +
	                    '<div class="info">' + file.name + '</div>' +
	                '</div>'
	                ),
	            $img = $li.find('img');
	
	        $list3.append( $li );
	    });
	    // 文件上传过程中创建进度条实时显示。
	    uploader3.on( 'uploadProgress', function( file, percentage ) {
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
	    uploader3.on( 'uploadSuccess', function( file, response) {
	        $( '#'+file.id ).addClass('upload-state-done');
	        var path = response.data.path + ";";
	        $("#caseVideos").append(path);
	    });
	
	    // 文件上传失败，现实上传出错。
	    uploader3.on( 'uploadError', function( file ) {
	        var $li = $( '#'+file.id ),
	            $error = $li.find('div.error');
	
	        // 避免重复创建
	        if ( !$error.length ) {
	            $error = $('<div class="error"></div>').appendTo( $li );
	        }
	
	        $error.text('上传失败');
	    });
	
	    // 完成上传完了，成功或者失败，先删除进度条。
	    uploader3.on( 'uploadComplete', function( file ) {
	        $( '#'+file.id ).find('.progress').remove();
	    });
	});
	</script>
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
			
			var url = "${ctx}/../apiv1/user/info/list?office_id=${fns:getUser().office.id}";
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
						multiple : false
					});
				}
			});
			var url1 = "${ctx}/../apiv1/cms/article/list?categoryid=3";
			$.ajax({
				type : "get",
				url : url1,
				async : false,
				success : function(result) {
					var resviewersData = "[";
					for (var i = 0; i < result.data.length; i++) {
						resviewersData += "{\"id\":\"" + result.data[i].title + "\",\"text\":\"" + result.data[i].title + "\"},";
					}
					resviewersData = resviewersData.substring(0,resviewersData.length-1);
					resviewersData += "]";
					var resviewersJsonData = eval('(' + resviewersData+ ')');
					$("#normCaseDescPart1").select2({
						data : resviewersJsonData,
						multiple : false
					});
				}
			});
			var url2 = "${ctx}/../apiv1/cms/article/similar?title=" + $("#normCaseDescPart1").val();
				$.ajax({
					type : "get",
					url : url2,
					async : false,
					success : function(result) {
						var resviewersData = "[";
						for (var i = 0; i < result.data.length; i++) {
							resviewersData += "{\"id\":\"" + result.data[i].title + "\",\"text\":\"" + result.data[i].title + "\"},";
						}
						resviewersData = resviewersData.substring(0,resviewersData.length-1);
						resviewersData += "]";
						var resviewersJsonData = eval('(' + resviewersData+ ')');
						$("#normCaseDescPart2").select2({
							data : resviewersJsonData,
							multiple : false
						});
					}
				});
			$("#normCaseDescPart1").change(function(){
				$("#normCaseDescPart2").empty();
				var url3 = "${ctx}/../apiv1/cms/article/similar?title=" + $("#normCaseDescPart1").val();
				$.ajax({
					type : "get",
					url : url3,
					async : false,
					success : function(result) {
						var resviewersData = "[";
						for (var i = 0; i < result.data.length; i++) {
							resviewersData += "{\"id\":\"" + result.data[i].title + "\",\"text\":\"" + result.data[i].title + "\"},";
						}
						resviewersData = resviewersData.substring(0,resviewersData.length-1);
						resviewersData += "]";
						var resviewersJsonData = eval('(' + resviewersData+ ')');
						$("#normCaseDescPart2").select2({
							data : resviewersJsonData,
							multiple : false
						});
					}
				});
			})
			
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
					<td>文档资料上传</td>
					<td colspan="5">
							<div id="uploader">
							    <!--用来存放item-->
							    <div id="fileList" class="uploader-list"></div>
							    <div id="filePicker">选择文件</div>
							</div>
							<form:textarea path="caseDocuments" rows="3"/>

					</td>
				</tr>
				
				<tr>
					<td>照片图片上传</td>
					<td colspan="5">
							<div id="uploader2">
							    <!--用来存放item-->
							    <div id="fileList2" class="uploader-list"></div>
							    <div id="filePicker2">选择文件</div>
							</div>
							<form:textarea path="caseImages" rows="3"/>

					</td>
				</tr>
				<tr>
					<td>视频影像上传</td>
					<td colspan="5">
						<div id="uploader3">
						    <!--用来存放item-->
							<div id="fileList3" class="uploader-list"></div>
							<div id="filePicker3">选择文件</div>
						</div>
						<form:textarea path="caseVideos" rows="3"/>
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
					<td colspan="2">${fns:getUser().name},
						<form:select path="assigneeIds" class="form-control" multiple="multiple" style="width:300px; border:1 px solid #ccc;">
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="tit">案情</td>
					<td colspan="2">
						<form:select path ="normCaseDescPart1" class="required"></form:select>
					</td>
					<td class="tit">违反了</td>
					<td colspan="2">
						<form:select path ="normCaseDescPart2" class="required"></form:select>
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