package com.param.adt.mrd;



import com.param.global.model.MrdFileStatus;


import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({

        @NamedNativeQuery(name = "INACTIVE_FILE_ALLOCATION_STATUS_BY_ALLOCATION_ID",
                query = "update adt.t_mrd_file_management_status set status = 'I' where  allocation_id =:allocationId "),

        @NamedNativeQuery(name = "GET_FILE_STATUS_ID_AS_PARENT_ID",
        query = "select mrd_file_status_id from adt.t_mrd_file_management_status where  allocation_id =:allocationId and status = 'A'")

})
@Entity
@Table(name = "t_mrd_file_management_status", schema = "adt")
@SequenceGenerator(name = "mrd_file_status_seq", sequenceName = "adt.mrd_file_status_seq", allocationSize = 1)
public class MrdFileAllocationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mrd_file_status_seq")
    @Column(name = "mrd_file_status_id")
    private Integer mrdFileStatusId;

    @Column(name = "allocation_id")
    private Integer allocationId;

    @Column(name = "request_no")
    private String requestedNo;

    @Column(name = "issue_no")
    private String issuedNo;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "file_no")
    private String fileNo;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "status")
    private char status;
    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "requested_by")
    private Integer requestedBy;

    @Column(name = "accepted_by")
    private Integer acceptedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", insertable = false, nullable = false, updatable = false)
    private MrdFileStatus fileStatus;

    public Integer getMrdFileStatusId() {
        return mrdFileStatusId;
    }

    public void setMrdFileStatusId(Integer mrdFileStatusId) {
        this.mrdFileStatusId = mrdFileStatusId;
    }

    public Integer getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Integer allocationId) {
        this.allocationId = allocationId;
    }

    public String getRequestedNo() {
        return requestedNo;
    }

    public void setRequestedNo(String requestedNo) {
        this.requestedNo = requestedNo;
    }

    public String getIssuedNo() {
        return issuedNo;
    }

    public void setIssuedNo(String issuedNo) {
        this.issuedNo = issuedNo;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public MrdFileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(MrdFileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(Integer acceptedBy) {
        this.acceptedBy = acceptedBy;
    }
}
