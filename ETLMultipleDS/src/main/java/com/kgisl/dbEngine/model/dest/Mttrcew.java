package com.kgisl.dbEngine.model.dest;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "mttrcew")
public class Mttrcew {

	@EmbeddedId
	private MttrcewId mttrcewId;
	
	private String cnformulatype;
	private double cnsuminsured;  
	
	public MttrcewId getMttrcewId() {
		return mttrcewId;
	}
	public void setMttrcewId(MttrcewId mttrcewId) {
		this.mttrcewId = mttrcewId;
	}
	public String getCnformulatype() {
		return cnformulatype;
	}
	public void setCnformulatype(String cnformulatype) {
		this.cnformulatype = cnformulatype;
	}
	public double getCnsuminsured() {
		return cnsuminsured;
	}
	public void setCnsuminsured(double cnsuminsured) {
		this.cnsuminsured = cnsuminsured;
	}
	public double getCnbenpremium() {
		return cnbenpremium;
	}
	public void setCnbenpremium(double cnbenpremium) {
		this.cnbenpremium = cnbenpremium;
	}
	public double getCnrate() {
		return cnrate;
	}
	public void setCnrate(double cnrate) {
		this.cnrate = cnrate;
	}
	private double cnbenpremium;
	private double cnrate;

}
