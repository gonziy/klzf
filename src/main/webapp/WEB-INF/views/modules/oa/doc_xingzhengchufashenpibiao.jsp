<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行政处罚表</title>
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
      <h3>行政处罚表</h3></p></td>
  </tr>
  <tr>
    <td colspan="4" height="40" valign="center"><p align="right">垦执审字[${fns:getCaseIdYear(oaCase.caseDocNo)}]第 ${fns:getCaseId(oaCase.caseDocNo)}号&nbsp;&nbsp; </p></td>
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
    <td width="120" height="150" valign="center"><p align="center">案    情 <br>
      简    述 </p></td>
    <td colspan="3" valign="center"><p>&nbsp;&nbsp;${oaCase.normCaseDescPart1}行为，违反了${oaCase.normCaseDescPart2}之规定</p></td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">承办人 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;建议依据${oaCase.normCaseDescPart2}给予处罚${oaCase.normAssigneePenalOptPart2}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.createBy.id)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.casePenalStartDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">承办机构 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.institutionPenalOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.institutionPenalAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.institutionPenalDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">案件管理中心 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.caseMgtCenterPenalOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.caseMgtCenterPenalAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.caseMgtCenterPenalDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">分管领导 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.deptLeaderPenalOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.deptLeaderPenalAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.deptLeaderPenalDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
  <tr>
    <td width="120" height="100" valign="center"><p align="center">主要领导 <br>
      意  见 </p></td>
    <td colspan="3" valign="center">
    <p>&nbsp;&nbsp;${oaCase.mainLeaderPenalOption}</p>
    <p style="text-align:right"><img src="${fns:getUserPhotoById(oaCase.mainLeaderPenalAssignee)}" alt="" />&nbsp;&nbsp;</p>
    <p style="text-align:right"><fmt:formatDate value="${oaCase.casePenalEndDate}" pattern="yyyy/MM/dd"/>&nbsp;&nbsp;</p>
    </td>
  </tr>
</table>
<input type="button" value="打印" class="hide" onclick="printpage()" />
</body>
</html>
