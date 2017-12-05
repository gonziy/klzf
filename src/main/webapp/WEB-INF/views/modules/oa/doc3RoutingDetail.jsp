<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>公文流转</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td class="tit">标题</td>
				<td>${oaDoc3.docTitle}</td>
			</tr>
			<tr>
				<td class="tit">发文时间</td>
				<td><fmt:formatDate value="${oaDoc3.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
			<tr>
				<td class="tit">文件及附件链接</td>
				<td>${oaDoc3.attachLinksLinks}</td>
			</tr>
			<tr>
				<td class="tit">办公文员建议</td>
				<td>${oaDoc3.applyerOption}</td>
			</tr>
			<tr>
				<td class="tit">办公室主任建议</td>
				<td>${oaDoc3.officeHeaderOption}</td>
			</tr>
			<tr>
				<td class="tit">领导审阅意见</td>
				<td>${oaDoc3.leaderOptions}</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
