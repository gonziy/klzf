<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>陈述、申辩（听证）权利告知书</title>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js" ></script>
	<script type="text/javascript">
	function printpage(){
		window.print()
	}
	function gaozhi(id){
		$.get("${ctxStatic}/../apiv1/oa/case/gaozhi?id="+id,function(data){
			if(data.result==1){
				alert("修改成功");
			}else{
				alert("修改失败,请检查是否已经告知被处罚人");
			}
		})
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
    <td height="70" colspan="4" valign="center">
    	<p align="center">
    		<h3>东营市垦利区综合行政执法</h3>
      		<h3>陈述、申辩（听证）权利告知书</h3>
      	</p>
   </td>
  </tr>
    <tr>
    <td height="70" colspan="4" valign="center">
    	<p align="right">垦执告字[${fns:getCaseIdYear(oaCase.caseDocNo)}]第 ${fns:getCaseId(oaCase.caseDocNo)}号&nbsp;&nbsp; </p>
   </td>
  </tr>
  <tr>
  	<td>
  	<u>${oaCase.caseParties}</u>:<br />
  	 &nbsp;&nbsp;&nbsp;&nbsp;你（单位）的<u>${oaCase.normCaseDescPart1}</u>行为违反了<u>${oaCase.normCaseDescPart2}</u>之规定，依据<u>${oaCase.normCaseDescPart2}</u>的规定拟对你（单位）
  	 作出<u>${oaCase.normAssigneePenalOptPart2}</u>。<br />
  	&nbsp;&nbsp;&nbsp;&nbsp;根据《中华人民共和国行政处罚法》第三十二条规定，你享有以下第<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>项权利。<br />
  	&nbsp;&nbsp;&nbsp;&nbsp;1.自收到本告知书三日内，可就拟给予的行政处罚进行陈述和申辩，逾期视为放弃此权利。<br />
  	&nbsp;&nbsp;&nbsp;&nbsp;2.自收到本告知书三日内，可就拟给予的行政处罚要求听证，逾期视为放弃此权利。<br />
	<br />
  	本机关地址：<u>${oaCase.officeAddress}</u><br />
  	联 系 电 话：<u>${oaCase.officeTel}</u><br />
  	执法人员：<br /><u>${oaCase.assigneeNamesAndId}</u>
  	  	<br /><br />
  	<br /><br />
  	<p style="text-align:right;">
  		单位印章&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br /><br />
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	<br /><br />
  	</p>
  	</td>
  </tr>
</table>
<input type="button" value="打印" class="hide" onclick="printpage()" />
<input type="button" value="现已送达《权利告知书》" class="hide" onclick="gaozhi('${oaCase.id}')" />
</body>
</html>
