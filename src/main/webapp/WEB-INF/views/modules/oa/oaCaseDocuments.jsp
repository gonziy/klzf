<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>案件管理</title>
	<meta name="decorator" content="default"/>
	<style>
	.docs li { width:140px; height:140px; display:block; float:left; margin:10px; border:1px solid #ccc; text-align:center; font-size:14px; color:#333; line-height:20px; padding:80px 10px 10px 10px;}
	.docs li a{width:140px; height:140px; display:block;}
	</style>
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/oaCase/">返回列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaCase/form/?procInsId=${oaCase.procInsId}">案件详情</a></li>
	</ul>
	<form:form class="form-horizontal">
		<sys:message content="${message}"/>
		<fieldset>
			<legend>文书打印</legend>
			<ul class="docs">
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">立案审批表</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">责令限期整改通知书</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">责令停止违法行为通知书</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">行政处罚审批表</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">陈述、申辩（听证）权利告知书  gaozhi</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">责令限期拆除决定书  chufa</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">行政处罚决定书     chufa</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">拆除催告通知书  cuigao</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">拆除公告通知书</a></li>
				<li><a href="${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}">结案登记表</a></li>
			</ul>
		</fieldset>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
