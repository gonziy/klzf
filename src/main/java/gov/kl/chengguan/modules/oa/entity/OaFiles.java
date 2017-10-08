package gov.kl.chengguan.modules.oa.entity;

import gov.kl.chengguan.common.persistence.DataEntity;

public class OaFiles extends DataEntity<OaFiles> {


	private static final long serialVersionUID = 1L;
	private String groupId; 
	private String fileName;
	private String filePath; 
	private String type;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
}
