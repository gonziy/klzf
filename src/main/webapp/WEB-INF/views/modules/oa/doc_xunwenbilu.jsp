<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>询问笔录</title>
	<script type="text/javascript">
	function printpage()
	  {
	  window.print()
	  }
	</script>
	<style>
	table{ font-size:14px; width:600px;}
	table h3{ font-size:20px; line-height:1.5; text-align:center;}
	table td{ font-size:16px; line-height:1.5;padding:10px;}
	</style>
	<style media="print" type="text/css">
	.hide{ display:none;}
	</style>
</head>
<body>
<table border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td height="70" colspan="4" valign="center"><p align="center"><h3>东营市垦利区综合行政执法</h3>
      <h3>询问笔录</h3></p></td>
  </tr>
  <tr>
  	<td>
  	询问地点：<hr />
  	询问时间： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp; 时&nbsp;&nbsp;&nbsp;&nbsp;分
  	至&nbsp;&nbsp;时&nbsp;&nbsp;分
  	 <hr />
  	询问机关：垦利区综合行政执法局${oaCase.officeName}<hr />
  	询问人：${oaCase.assigneeNameAndId1}<hr />
  	记录人：${oaCase.assigneeNameAndId2}<hr />
  	被询问人：姓名：${oaCase.caseParties}性别：&nbsp;&nbsp;&nbsp;&nbsp;年龄：&nbsp;&nbsp;&nbsp;&nbsp;民族：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<hr />
  	工作单位：<hr />
  	住址：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系电话：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<hr />
  	问：我们是垦利区综合行政执法局${oaCase.officeName}的行政执法人员，这是我们的执法证，向您出示，请看一下。现依法对你进行询问，你有权进行陈述、申辩和申请回避，是否清楚？<hr />
  	<br /><hr />
  	<br /><hr />
  	<br /><hr />
  	<br /><hr />
  	被询问人签名：<hr />
  	询问人签名：<hr />  	
  	见证人签名：<br />
  	</td>
  </tr>
</table>
<input type="button" value="打印" class="hide" onclick="printpage()" />
</body>
</html>
