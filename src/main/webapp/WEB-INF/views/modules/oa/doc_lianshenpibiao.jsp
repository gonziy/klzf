<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>立案审批表</title>
	<script type="text/javascript">
	function printpage()
	  {
	  window.print()
	  }
	</script>
	<style>
	table{ font-size:14px;}
	table h3{ font-size:20px; line-height:1.5; text-align:center;}
	</style>
	<style media="print" type="text/css">
	.hide{ display:none;}
	</style>
</head>
<body>
<table border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td height="70" colspan="4" valign="center"><p align="center"><h3>东营市垦利区综合行政执法</h3>
      <h3>立案审批表</h3></p></td>
  </tr>
  <tr>
    <td colspan="4" height="40" valign="center"><p align="right">垦执立字[ 2017 ] 第123456 号&nbsp;&nbsp; </p></td>
  </tr>
  <tr>
    <td width="120" height="40" valign="center"><p align="center">当事人 </p></td>
    <td width="200" valign="center"><p>&nbsp;&nbsp;${oaCase.caseParties}</p></td>
    <td width="120" valign="center"><p align="center">地址 </p></td>
    <td width="200" valign="center"><p>&nbsp;&nbsp;${oaCase.address}</p></td>
  </tr>
  <tr>
    <td width="120" height="40" valign="center"><p align="center">法定代表人 </p></td>
    <td width="200" valign="center"><p>&nbsp;&nbsp;${oaCase.caseLegalAgent}</p></td>
    <td width="120" valign="center"><p align="center">联系电话 </p></td>
    <td width="200" valign="center"><p>&nbsp;&nbsp;${oaCase.phoneNumber}</p></td>
  </tr>
  <tr>
    <td width="94" height="40" valign="center"><p align="center">案件来源 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.caseSource}</p></td>
  </tr>
  <tr>
    <td width="120" height="150" valign="center"><p align="center">案    情 <br>
      简    述 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.caseDescription}</p></td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">承办人 <br>
      意  见 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.caseCheckResult}</p></td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">承办机构 <br>
      意  见 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.institutionRegOption}</p></td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">分管领导 <br>
      意  见 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.deptLeaderRegOption}</p></td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">主要领导 <br>
      意  见 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.mainLeaderRegOption}</p></td>
  </tr>
</table>
<input type="button" value="打印" class="hide" onclick="printpage()" />
</body>
</html>
