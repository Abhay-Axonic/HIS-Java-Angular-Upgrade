package com.param.otims.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@NamedQuery(name="GET_ACTIVE_PROCEDURE_ORDER_STATUS",
			query=" SELECT procOrdStatus.id AS id , procOrdStatus.status AS status "
				+ " FROM ProcedureOrderStatus procOrdStatus ")
@Entity
@Table(name="m_procedure_order_status",schema="otims")
public class ProcedureOrderStatus {

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="status")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	
}
