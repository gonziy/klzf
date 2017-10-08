package gov.kl.chengguan.modules.oa.entity;

import gov.kl.chengguan.common.persistence.DataEntity;

public class OaCaseFields extends DataEntity<OaCaseFields> {


	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	public String getValueFirst() {
		return valueFirst;
	}

	public void setValueFirst(String valueFirst) {
		this.valueFirst = valueFirst;
	}

	public String getValueSecond() {
		return valueSecond;
	}

	public void setValueSecond(String valueSecond) {
		this.valueSecond = valueSecond;
	}

	public String getValueThird() {
		return valueThird;
	}

	public void setValueThird(String valueThird) {
		this.valueThird = valueThird;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	private String no;

	private String valueFirst; //第1个关键字
	private String valueSecond; //第2个关键字
	private String valueThird; //第3个关键字
	
	private String intro;  //案情简介
	
	
	
}
