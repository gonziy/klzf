<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>结案登记表</title>
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
      <h3>结案登记表</h3></p></td>
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
    <td width="120" height="150" valign="center"><p align="center">简要案情 <br>
     承办人签字 </p></td>
    <td colspan="3" valign="center">
    	<p>&nbsp;&nbsp;${oaCase.normCaseDescPart1}行为，违反了${oaCase.normCaseDescPart2}之规定，给予处罚${oaCase.normAssigneePenalOptPart2}</p>
    	    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.closeUpAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.caseCloseUpStartDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">承办机构 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.institutionCloseCaseOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.institutionCloseAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.institutionCloseDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">案件管理中心 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.caseMgtCenterCloseCaseOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.caseMgtCenterCloseAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.caseMgtCenterCloseDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">主要领导 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.mainLeaderCloseCaseOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.mainLeaderCloseAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.caseCloseUpEndDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
</table>
<input type="button" value="打印" class="hide" onclick="printpage()" />
</body>
</html>
