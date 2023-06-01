package com.kgisl.dbEngine.model.dest;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MttrxdrvId {
	
	@Column(name="CnCvnoteNo")
	private String	cnCvnoteNo;
	
	@Column(name="cnregno")
	private String	cnregno;
	
	@Column(name="cndrvic")
	private String	cndrvic;

	public String getCnCvnoteNo() {
		return cnCvnoteNo;
	}

	public void setCnCvnoteNo(String cnCvnoteNo) {
		this.cnCvnoteNo = cnCvnoteNo;
	}

	public String getCnregno() {
		return cnregno;
	}

	public void setCnregno(String cnregno) {
		this.cnregno = cnregno;
	}

	public String getCndrvic() {
		return cndrvic;
	}

	public void setCndrvic(String cndrvic) {
		this.cndrvic = cndrvic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnCvnoteNo == null) ? 0 : cnCvnoteNo.hashCode());
		result = prime * result + ((cndrvic == null) ? 0 : cndrvic.hashCode());
		result = prime * result + ((cnregno == null) ? 0 : cnregno.hashCode());
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
		MttrxdrvId other = (MttrxdrvId) obj;
		if (cnCvnoteNo == null) {
			if (other.cnCvnoteNo != null)
				return false;
		} else if (!cnCvnoteNo.equals(other.cnCvnoteNo))
			return false;
		if (cndrvic == null) {
			if (other.cndrvic != null)
				return false;
		} else if (!cndrvic.equals(other.cndrvic))
			return false;
		if (cnregno == null) {
			if (other.cnregno != null)
				return false;
		} else if (!cnregno.equals(other.cnregno))
			return false;
		return true;
	}
	

}
