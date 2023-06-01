package com.kgisl.dbEngine.model.dest;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="mttrxdrv")
public class Mttrxdrv {
	
	@EmbeddedId
	private MttrxdrvId mttrxdrvId;
	
	private int cndrvno;
	private String cndrvname ;
	private int cndrvexp;
	public MttrxdrvId getMttrxdrvId() {
		return mttrxdrvId;
	}
	public void setMttrxdrvId(MttrxdrvId mttrxdrvId) {
		this.mttrxdrvId = mttrxdrvId;
	}
	public int getCndrvno() {
		return cndrvno;
	}
	public void setCndrvno(int cndrvno) {
		this.cndrvno = cndrvno;
	}
	public String getCndrvname() {
		return cndrvname;
	}
	public void setCndrvname(String cndrvname) {
		this.cndrvname = cndrvname;
	}
	public int getCndrvexp() {
		return cndrvexp;
	}
	public void setCndrvexp(int cndrvexp) {
		this.cndrvexp = cndrvexp;
	}
	
	

}
