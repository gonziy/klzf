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
			<img src="${ctxStatic}/images/case_progress.png" alt="" width="1180" height="650" usemap="#Map"/>
			<map name="Map">
			  <area shape="rect" coords="61,432,185,480" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#责令限期改正通知书">
			  <area shape="rect" coords="60,491,186,534" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#责令停止违法行为通知书">
			  <area shape="rect" coords="299,312,420,354" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#询问笔录">
			  <area shape="rect" coords="297,366,422,409" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#勘验检查笔录">
			  <area shape="rect" coords="506,456,632,499" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#权利告知书">
			  <area shape="rect" coords="510,511,635,553" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#责令限期拆除决定书">
			  <area shape="rect" coords="510,565,632,608" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#行政处罚决定书">
			  <area shape="rect" coords="710,356,831,401" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#拆除催告通知书">
			  <area shape="rect" coords="705,450,834,493" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#拆除公告通知书">
			  <area shape="rect" coords="25,121,245,420" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#立案审批表">
			  <area shape="rect" coords="462,100,680,443" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#行政处罚表">
			  <area shape="rect" coords="909,123,1131,421" href="#${ctx}/oa/oaCase/doc_lianshenpibiao?id=${oaCase.id}#结案表">
			</map>
		</fieldset>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
