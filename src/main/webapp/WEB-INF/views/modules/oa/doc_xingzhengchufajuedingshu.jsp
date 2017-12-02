<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行政处罚决定书</title>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js" ></script>
	<script type="text/javascript">
	function printpage(){
		window.print()
	}
	function chufa(id){
		$.get("${ctxStatic}/../apiv1/oa/case/chufa?id="+id,function(data){
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
      		<h3>行政处罚决定书</h3>
      	</p>
   </td>
  </tr>
    <tr>
    <td height="70" colspan="4" valign="center">
    	<p align="right">垦执处字[${fns:getCaseIdYear(oaCase.caseDocNo)}]第 ${fns:getCaseId(oaCase.caseDocNo)}号&nbsp;&nbsp; </p>
   </td>
  </tr>
  <tr>
  	<td>
  	当事人：<u>${oaCase.caseParties}</u>地址：<u>${oaCase.address}</u><br />
  	<b>违法事实、相关证据及具体法律规定：</b><br />
  	 &nbsp;&nbsp;&nbsp;&nbsp;经查实，你（单位）的<u>${oaCase.normCaseDescPart1}</u>。<br />
  	 <b>责令限期拆除的法律依据：</b><br />
  	 &nbsp;&nbsp;&nbsp;&nbsp;依据<u>${oaCase.normCaseDescPart2}</u>之规定。<br />
  	  <b>履行方式、期限和救济途径：</b><br />
  	&nbsp;&nbsp;&nbsp;&nbsp;对本处罚决定不服的，可以在收到本
  	决定书之日起60日内向东营市垦利区人民政府申请行政复议，或6个月内向东营市垦利区人民法院提起行政诉讼。
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
<input type="button" value="现已送达《行政处罚决定书》" class="hide" onclick="chufa('${oaCase.id}')" />
</body>
</html>
