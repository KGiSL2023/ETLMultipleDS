package com.kgisl.dbEngine.model.dest;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mtallocn")
public class Mtallocn {

	@EmbeddedId
    private MtallocnId mtallocnId;
	
	public MtallocnId getMtallocnId() {
		return mtallocnId;
	}

	public void setMtallocnId(MtallocnId mtallocnId) {
		this.mtallocnId = mtallocnId;
	}

	@Column(name="CnStatus")
	private String cnStatus;
	
	@Column(name="Trxseqno")
	private int trxSeqno;
	
	@Column(name="CnInsertdate")
	private Date cnInsertdate;
	
	public String getCnStatus() {
		return cnStatus;
	}

	public void setCnStatus(String cnStatus) {
		this.cnStatus = cnStatus;
	}

	public int getTrxSeqno() {
		return trxSeqno;
	}

	public void setTrxSeqno(int trxSeqno) {
		this.trxSeqno = trxSeqno;
	}

	public Date getCnInsertdate() {
		return cnInsertdate;
	}

	public void setCnInsertdate(Date cnInsertdate) {
		this.cnInsertdate = cnInsertdate;
	}

	
}

