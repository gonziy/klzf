<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>案件管理</title>
	<meta name="decorator" content="default"/>
	<style>
	.listimg{ width:150px; height:120px; margin-right:10px;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			var imgs = "${oaCase.caseImages}";
			var arrayimgs = imgs.split(";");
			for(var i=0; i<arrayimgs.length;i++){
				if(arrayimgs[i].length>0){
					$("#fileimgs").append("<img class=\"listimg\" src=\""+arrayimgs[i]+"\" \/>");
				}
			}
			
			var vdos = "${oaCase.caseVideos}";
			var arrayvdos = vdos.split(";");
			for(var i=0; i<arrayvdos.length;i++){
				if(arrayvdos[i].length>0){
					$("#filevdos").append("<img class=\"listimg\" src=\""+arrayvdos[i]+"\" \/>");
				}
			}
			
			var docs = "${oaCase.caseDocuments}";
			var arraydocs = docs.split(";");
			for(var i=0; i<arraydocs.length;i++){
				if(arraydocs[i].length>0){
					$("#filedocs").append("<img class=\"listimg\" src=\""+arraydocs[i]+"\" \/>");
				}
			}
			
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
			<legend>案件详情</legend>
			<table class="table-form">
				<!-- 申报 -->
				<tr><td class="tit" colspan=6><h4>案件申报</h4></td></tr>	
				<tr>
					<td class="tit">当事人</td><td>${oaCase.caseParties}</td>
					<td class="tit">法人</td><td>${oaCase.caseLegalAgent}</td>
					<td class="tit">联系电话</td><td>${oaCase.phoneNumber}</td>
				</tr>
				<tr>
					<td class="tit">地址</td>
					<td colspan="5">${oaCase.address}</td>					
				</tr>
				<tr>
					<td class="tit">案情简述</td>
					<td colspan="5">${oaCase.caseDescription}</td>
				</tr>								
				<tr>
					<td class="tit" colspan= 6><h4>立案审批</h4></td>					
				</tr>				
				<tr>
					<td class="tit">案件来源</td><td colspan="2">${oaCase.caseSource}</td>
					<td class="tit">承办人</td><td colspan="2">${oaCase.assigneeNames}</td>
				</tr>
				<tr>
					<td class="tit">案情</td>
					<td colspan=5>${oaCase.normCaseDesc}</td>
				</tr>						
				<tr>
					<td class="tit">承办机构意见</td>
					<td colspan="4">${oaCase.institutionRegOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.institutionRegApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit">分管领导意见</td>
					<td colspan="4">${oaCase.deptLeaderRegOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.deptLeaderRegApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>					
				</tr>
				<tr>
					<td class="tit">主管领导意见</td>
					<td colspan="4">${oaCase.mainLeaderRegOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.mainLeaderRegApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>	
				</tr>
				
				<!-- 调查 -->
				<tr><td class="tit" colspan=6><h4>案件调查</h4></td></tr>	
				<tr>
					<td class="tit">案件材料</td>
					<td colspan=5></td>
				</tr>
				
				<!-- 处罚 -->	
				<tr><td class="tit" colspan= 6><h4>行政处罚审批</h4></td></tr>	
				<tr>
					<td class="tit">承办人意见</td>
					<td colspan=5>${oaCase.normAssigneePenalOpt}</td>
				</tr>
				<tr>
					<td class="tit">承办机构意见</td>
					<td colspan="4">${oaCase.institutionPenalOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.institutionPenalApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit">案件管理中心意见</td>
					<td colspan="4">${oaCase.caseMgtCenterPenalOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.caseMgtCenterPenalApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit">分管领导意见</td>
					<td colspan="4">${oaCase.deptLeaderPenalOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.deptLeaderPenalApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>					
				</tr>
				<tr>
					<td class="tit">主管领导意见</td>
					<td colspan="4">${oaCase.mainLeaderPenalOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.mainLeaderPenalApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>	
				</tr>

				<!-- 结案 -->
				<tr><td class="tit" colspan= 6><h4>结案审批</h4></td></tr>					
				<tr>
					<td class="tit">承办人意见</td>
					<td colspan=5>${oaCase.assigneeCloseCaseOption}</td>
				</tr>
				<tr>
					<td class="tit">承办机构意见</td>
					<td colspan="4">${oaCase.institutionCloseCaseOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.institutionCloseCaseApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit">案件管理中心意见</td>
					<td colspan="4">${oaCase.caseMgtCenterCloseCaseOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.caseMgtCenterCloseCaseApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit">主管领导意见</td>
					<td colspan="4">${oaCase.mainLeaderCloseCaseOption}</td>
					<td>
						<c:choose>
						<c:when test="${oaCase.mainLeaderCloseCaseApproval==true}"><b>已同意</b></c:when>
						<c:otherwise><b style="color:red; display:none;">已驳回</b></c:otherwise>
						</c:choose>
					</td>	
				</tr>				
				<tr>
					<td class="tit">立案结束日期</td><td><fmt:formatDate value="${oaCase.caseRegEndDate}" pattern="yyyy年MM月dd日"/></td>
					<td class="tit">处罚结束日期</td><td><fmt:formatDate value="${oaCase.casePenalEndDate}" pattern="yyyy年MM月dd日"/></td>
					<td class="tit">结案日期</td><td><fmt:formatDate value="${oaCase.caseCloseUpEndDate}" pattern="yyyy年MM月dd日"/></td>					
				</tr>
				<tr>
					<td class="tit">文档</td><td colspan="5"><div id="filedocs"></div></td>				
				</tr>
				<tr>
					<td class="tit">照片</td><td colspan="5"><div id="fileimgs"></div></td>				
				</tr>
				<tr>
					<td class="tit">视频</td><td colspan="5"><div id="filevdos"></div></td>				
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
