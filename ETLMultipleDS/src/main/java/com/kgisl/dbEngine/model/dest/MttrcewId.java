package com.kgisl.dbEngine.model.dest;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MttrcewId implements Serializable{
	
	@Column(name="CnCvnoteNo")
	private String cnCvnoteNo;
	
	@Column(name="cnregno")
	private String cnRegNo;
	
	@Column(name="cncewcode")
	private String cnCewCode;

	public String getCnCvnoteNo() {
		return cnCvnoteNo;
	}

	public void setCnCvnoteNo(String cnCvnoteNo) {
		this.cnCvnoteNo = cnCvnoteNo;
	}

	public String getCnRegNo() {
		return cnRegNo;
	}

	public void setCnRegNo(String cnRegNo) {
		this.cnRegNo = cnRegNo;
	}

	public String getCnCewCode() {
		return cnCewCode;
	}

	public void setCnCewCode(String cnCewCode) {
		this.cnCewCode = cnCewCode;
	}
	
	

}
