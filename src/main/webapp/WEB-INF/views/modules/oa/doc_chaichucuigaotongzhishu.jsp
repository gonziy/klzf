<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拆除催告通知书</title>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js" ></script>
	<script type="text/javascript">
	function printpage(){
		window.print()
	}
	function cuigao(id){
		$.get("${ctxStatic}/../apiv1/oa/case/cuigao?id="+id,function(data){
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
	table td{ font-size:16px; line-height:1.5; padding:10px;}
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
      		<h3>拆除催告通知书</h3>
      	</p>
   </td>
  </tr>
      <tr>
    <td height="70" colspan="4" valign="center">
    	<p align="right">垦执催字[${fns:getCaseIdYear(oaCase.caseDocNo)}]第 ${fns:getCaseId(oaCase.caseDocNo)}号&nbsp;&nbsp; </p>
   </td>
  </tr>
  <tr>
  	<td>
	<u>${oaCase.caseParties}</u>:<br />
  	 &nbsp;&nbsp;&nbsp;&nbsp;你（单位）的<u>${oaCase.normCaseDescPart1}</u>行为，违反了<u>${oaCase.normCaseDescPart2}</u>之规定，依据<u>${oaCase.normCaseDescPart2}</u>之规定，
  	 本机关于<fmt:formatDate value="${oaCase.chufaDate}" pattern="yyyy年MM月dd日"/>
  	 向你（单位）下发了垦执拆字[${fns:getCaseIdYear(oaCase.caseDocNo)}]第 ${fns:getCaseId(oaCase.caseDocNo)}号《责令限期拆除决定书》，
  	 你（单位）在法定期限内对该行政决定未申请行政复议或者提起行政诉讼，也未履行该行政决定。现再次催告你（单位）于
  	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日前自行拆除。
  	 <br />
  	 &nbsp;&nbsp;&nbsp;&nbsp;如对本催告决定不服，你（单位）享有陈述和申辩的权利。如要求陈述和申辩，应在收到本催告书之日起三日内向
  	 本机关提出。逾期未提出的，视为放弃此权利。
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
  	<br />
  	【期限】责令限期拆除决定书下发之日起满六个月，制作并下发该文书。
  	 <br /><br />

  	</td>
  </tr>
</table>
<input type="button" value="打印" class="hide" onclick="printpage()" />
<input type="button" value="现已送达《拆除催告通知书》" class="hide" onclick="cuigao('${oaCase.id}')" />
</body>
</html>
