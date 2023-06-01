package com.kgisl.dbEngine.model.dest;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MtallocnId implements Serializable{
	
	@Column(name="CnCvnoteNo")
	private String cnCvnoteNo;
	
	@Column(name="CnAgentCode")
	private String cnAgentCode;
	public String getCnCvnoteNo() {
		return cnCvnoteNo;
	}

	public void setCnCvnoteNo(String cnCvnoteNo) {
		this.cnCvnoteNo = cnCvnoteNo;
	}

	public String getCnAgentCode() {
		return cnAgentCode;
	}

	public void setCnAgentCode(String cnAgentCode) {
		this.cnAgentCode = cnAgentCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnAgentCode == null) ? 0 : cnAgentCode.hashCode());
		result = prime * result + ((cnCvnoteNo == null) ? 0 : cnCvnoteNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MtallocnId other = (MtallocnId) obj;
		if (cnAgentCode == null) {
			if (other.cnAgentCode != null)
				return false;
		} else if (!cnAgentCode.equals(other.cnAgentCode))
			return false;
		if (cnCvnoteNo == null) {
			if (other.cnCvnoteNo != null)
				return false;
		} else if (!cnCvnoteNo.equals(other.cnCvnoteNo))
			return false;
		return true;
	}

	
}
