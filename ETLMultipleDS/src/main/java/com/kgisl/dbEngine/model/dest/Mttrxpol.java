package com.kgisl.dbEngine.model.dest;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "mttrxpol")
public class Mttrxpol {

	@EmbeddedId
	private MttrxpolId mttrxpolId;
	
	private String cnicno;
	private String cnregno;
	private String cncovercode;
	private String cnvehtype;
	private String cnsuminsured;
	private String cntaxcode;
	private String cninvoiceno;
	private double cncommiamt;
	private double cnservicetaxamt;
	private double cndiscountamt;
	private double cngrossprem;
	private double cnnetprem;
	public MttrxpolId getMttrxpolId() {
		return mttrxpolId;
	}
	public void setMttrxpolId(MttrxpolId mttrxpolId) {
		this.mttrxpolId = mttrxpolId;
	}
	public String getCnicno() {
		return cnicno;
	}
	public void setCnicno(String cnicno) {
		this.cnicno = cnicno;
	}
	public String getCnregno() {
		return cnregno;
	}
	public void setCnregno(String cnregno) {
		this.cnregno = cnregno;
	}
	public String getCncovercode() {
		return cncovercode;
	}
	public void setCncovercode(String cncovercode) {
		this.cncovercode = cncovercode;
	}
	public String getCnvehtype() {
		return cnvehtype;
	}
	public void setCnvehtype(String cnvehtype) {
		this.cnvehtype = cnvehtype;
	}
	public String getCnsuminsured() {
		return cnsuminsured;
	}
	public void setCnsuminsured(String cnsuminsured) {
		this.cnsuminsured = cnsuminsured;
	}
	public String getCntaxcode() {
		return cntaxcode;
	}
	public void setCntaxcode(String cntaxcode) {
		this.cntaxcode = cntaxcode;
	}
	public String getCninvoiceno() {
		return cninvoiceno;
	}
	public void setCninvoiceno(String cninvoiceno) {
		this.cninvoiceno = cninvoiceno;
	}
	public double getCncommiamt() {
		return cncommiamt;
	}
	public void setCncommiamt(double cncommiamt) {
		this.cncommiamt = cncommiamt;
	}
	public double getCnservicetaxamt() {
		return cnservicetaxamt;
	}
	public void setCnservicetaxamt(double cnservicetaxamt) {
		this.cnservicetaxamt = cnservicetaxamt;
	}
	public double getCndiscountamt() {
		return cndiscountamt;
	}
	public void setCndiscountamt(double cndiscountamt) {
		this.cndiscountamt = cndiscountamt;
	}
	public double getCngrossprem() {
		return cngrossprem;
	}
	public void setCngrossprem(double cngrossprem) {
		this.cngrossprem = cngrossprem;
	}
	public double getCnnetprem() {
		return cnnetprem;
	}
	public void setCnnetprem(double cnnetprem) {
		this.cnnetprem = cnnetprem;
	}

	
}
