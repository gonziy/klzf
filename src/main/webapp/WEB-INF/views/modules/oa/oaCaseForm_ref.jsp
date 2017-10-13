<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>案件录入</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/oaCase/">案件列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaCase/form?id=${oaCase.id}"><shiro:hasPermission name="oa:oaCase:edit">办理${not empty oaCase.id?'修改':'申请'}流程</shiro:hasPermission><shiro:lacksPermission name="oa:oaCase:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaCase" action="${ctx}/oa/oaCase/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>执法案件</legend>
			<table class="table-form">
				<!-- 案件申报 -->
				<tr>
					<td class="tit">当事人</td><td>
						<form:input path="caseParties" class="required" htmlEscape="false" maxlength="100"/>
					</td>			
					<td class="tit">法人</td><td>
						<form:input path="caseLegalAgent" class="required" htmlEscape="false" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="tit">联系方式</td><td>
						<form:input path="phoneNumber" class="required" htmlEscape="false" maxlength="50"/>
					</td>
					<td class="tit">地址</td><td>
						<form:input path="address" class="required" htmlEscape="false" maxlength="150"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案情简述</td>
					<td colspan="3">
						<form:textarea path="caseDescription" class="required" rows="5" maxlength="500"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案件申报材料提报</td>
					<td colspan="3">
						<form:textarea path="caseAttachmentLinks" rows="4"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案件来源</td><td>
						<form:select path ="caseSource" class="required">
							<form:option value="1">上级交办</form:option>
							<form:option value="2">有关部门移交</form:option>
							<form:option value="3">调查发现</form:option>
							<form:option value="4">新闻媒体曝光</form:option>	
							<form:option value="5">举报投诉</form:option>
							<form:option value="6">其他</form:option>																	
						</form:select>
					</td>
				</tr>
				<tr>																			
					<td class="tit">承办人</td>
					<td colspan="2">
						<form:input path="assigneeIds" class="required" htmlEscape="false" maxlength="150"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案情</td><td>
						<form:select path ="normCaseDescPart1" class="required">
							<form:option value="1">行为1</form:option>
							<form:option value="2">行为2</form:option>
							<form:option value="3">行为3</form:option>																
						</form:select>
					</td>
					<td class="tit">违反了</td><td>
							<form:select path ="normCaseDescPart2" class="required">
							<form:option value="1">条例1</form:option>
							<form:option value="2">条例2</form:option>
							<form:option value="3">条例3</form:option>																
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="tit">承办机构立案意见</td>
					<td colspan="5">
						<form:textarea path="institutionRegOption" class="required" rows="5" maxlength="300"/>
					</td>					
				</tr>
				<tr>
					<td class="tit">分管领导立案意见</td>
					<td colspan="5">
						<form:textarea path="deptLeaderRegOption" class="required" rows="5" maxlength="300"/>
					</td>					
				</tr>				
				<tr>
					<td class="tit">主管领导立案意见</td>
					<td colspan="5">
						<form:textarea path="mainLeaderRegOption" class="required" rows="5" maxlength="300"/>
					</td>					
				</tr>
				<!-- 立案结束 -->	
				<tr>
					<td class="tit">案件调查材料提报</td>
					<td colspan="3">
						<form:textarea path="caseAttachmentLinks" rows="4"/>
					</td>
				</tr>
				<tr>
					<td class="tit">调查结束</td><td>
						<input id="caseSurveyEndDate" name="caseSurveyEndDate" 
						type="text" readonly="readonly" maxlength="20" class="Wdate required"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
				</tr>
				<!-- 调查结束 -->		
				<tr>
					<td class="tit" colspan=3>承办人意见</td>				
				</tr>
				<tr>
					<td class="tit">依据：</td><td>
						<form:select path ="normAssigneePenalOptPart1" class="required">
							<form:option value="1">法条1</form:option>
							<form:option value="2">法条2</form:option>
							<form:option value="3">法条3</form:option>																
						</form:select>
					</td>
					<td class="tit">给出处罚：</td><td>
							<form:select path ="normAssigneePenalOptPart2" class="required">
							<form:option value="1">处罚1</form:option>
							<form:option value="2">处罚2</form:option>
							<form:option value="3">处罚3</form:option>
							<form:option value="4">处罚4</form:option>																									
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<form:textarea path="assigneePenalOption" class="required" rows="5" maxlength="300"/>
					</td>	
				</tr>				
				<tr>
					<td class="tit">承办机构行政处罚意见</td>
					<td colspan="5">
						<form:textarea path="institutionPenalOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案件管理中心行政处罚意见</td>
					<td colspan="5">
						<form:textarea path="caseMgtCenterPenalOption" class="required" rows="5" maxlength="300"/>					
					</td>
				</tr>
				<tr>
					<td class="tit">分管领导行政处罚意见</td>
					<td colspan="5">
						<form:textarea path="deptLeaderPenalOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>				
				<tr>
					<td class="tit">主管领导行政处罚意见</td>
					<td colspan="5">
						<form:textarea path="mainLeaderPenalOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>
				<!-- 行政处罚 -->
				<tr>
					<td class="tit">承办人结案意见</td>
					<td colspan="3">
						<form:textarea path="assigneeCloseCaseOption" class="required" rows="5" maxlength="300"/>
					</td>	
				</tr>				
				<tr>
					<td class="tit">承办单位结案意见</td>
					<td colspan="5">
						<form:textarea path="institutionCloseCaseOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>
				<tr>
					<td class="tit">案件管理中心结案意见</td>
					<td colspan="5">
						<form:textarea path="caseMgtCenterCloseCaseOption" class="required" rows="5" maxlength="300"/>					
					</td>
				</tr>		
				<tr>
					<td class="tit">主管领导结案意见</td>
					<td colspan="5">
						<form:textarea path="mainLeaderCloseCaseOption" class="required" rows="5" maxlength="300"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="oa:oaCase:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
				<c:if test="${not empty oaCase.id}">
					<input id="btnSubmit2" class="btn btn-inverse" type="submit" value="撤销申请" onclick="$('#flag').val('no')"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<c:if test="${not empty oaCase.id}">
			<act:histoicFlow procInsId="${oaCase.act.procInsId}" />
		</c:if>
	</form:form>
</body>
</html>
