<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>勘验检查笔录</title>
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
      <h3>勘验检查笔录</h3></p></td>
  </tr>
  <tr>
  	<td>
  	
  	起止时间： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp; 时&nbsp;&nbsp;&nbsp;&nbsp;分
  	至 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp; 时&nbsp;&nbsp;&nbsp;&nbsp;分
  	 <hr />
  	 询问地点：<hr />
  	 勘验检查人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;职务：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<hr />
  	记录人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;职务：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<hr />
  	被检查人：${oaCase.caseParties}&nbsp;&nbsp;&nbsp;&nbsp;地址：${oaCase.address}<hr />
  	法定代表人：${oaCase.caseLegalAgent}&nbsp;&nbsp;&nbsp;&nbsp;联系方式：${oaCase.phoneNumber}<hr />
  	勘查情况：
  	<br /><hr />
  	<br /><hr />
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
