package gov.kl.chengguan.modules.oa.entity;

import gov.kl.chengguan.common.persistence.ActEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OaDoc3Leaders extends ActEntity<OaDoc3Leaders> {
	
	
	private String id;
	private String name;
	private Date addtime;
	private String opinion;
	private String text;
	
	

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		String[] tmp = id.split(":");
		if(tmp.length==2){
			return tmp[1];
		}
		return "";
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getAddtime() {
		String[] tmp = text.split("#");
		if(tmp.length==3){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date;
			try {
				date = sdf.parse(tmp[0]);
			} catch (ParseException e) {
				
			}
			return null;
		}
		
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getOpinion() {
		String[] tmp = text.split("#");
		if(tmp.length==3){
			return tmp[2];
		}
		return "";
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

}
