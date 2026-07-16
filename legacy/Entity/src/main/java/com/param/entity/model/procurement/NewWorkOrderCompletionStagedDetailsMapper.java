package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "NewWorkOrderCompletionStagedDetailsMapper")
@Table(name = "t_new_work_order_completion_staged_details", schema = "procurement")
public class NewWorkOrderCompletionStagedDetailsMapper extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_order_completion_staged_detail_id")
    private Integer workOrderCompletionStagedDetailId;
    
    
    @Column(name = "staged_workorder_id")
    private Integer stagedWorkorderId;

    @Column(name = "workorder_request_details_id")
    private Integer workorderRequestDetailsId;
    
    @Column(name = "working_stage_name")
    private String stageName;
    
    @Column(name = "percentage_amount")
    private Double percentage;

    @Column(name = "workorder_request_id")
    private Integer workorderRequestId;
    
    @Column(name = "percentage_amount_paid")
    private Double percentageAmountPaid;
    
    @Column(name = "percentage_amount_pay")
    private Double percentageAmountPay;
    
    @Column(name = "work_order_completion_id")
    private Integer workOrderCompletionId;
    
    
	public Integer getStagedWorkorderId() {
		return stagedWorkorderId;
	}

	public void setStagedWorkorderId(Integer stagedWorkorderId) {
		this.stagedWorkorderId = stagedWorkorderId;
	}

	public Integer getWorkorderRequestDetailsId() {
		return workorderRequestDetailsId;
	}

	public void setWorkorderRequestDetailsId(Integer workorderRequestDetailsId) {
		this.workorderRequestDetailsId = workorderRequestDetailsId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public Double getPercentageAmountPaid() {
		return percentageAmountPaid;
	}

	public void setPercentageAmountPaid(Double percentageAmountPaid) {
		this.percentageAmountPaid = percentageAmountPaid;
	}

	public Double getPercentageAmountPay() {
		return percentageAmountPay;
	}

	public void setPercentageAmountPay(Double percentageAmountPay) {
		this.percentageAmountPay = percentageAmountPay;
	}

	public Integer getWorkOrderCompletionStagedDetailId() {
		return workOrderCompletionStagedDetailId;
	}

	public void setWorkOrderCompletionStagedDetailId(Integer workOrderCompletionStagedDetailId) {
		this.workOrderCompletionStagedDetailId = workOrderCompletionStagedDetailId;
	}

	public Integer getWorkOrderCompletionId() {
		return workOrderCompletionId;
	}

	public void setWorkOrderCompletionId(Integer workOrderCompletionId) {
		this.workOrderCompletionId = workOrderCompletionId;
	}
}
