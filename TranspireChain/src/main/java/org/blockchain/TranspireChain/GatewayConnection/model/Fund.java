package org.blockchain.TranspireChain.GatewayConnection.model;

public class Fund {
    private String transactionId;
    private String departmentId;
    private String employeeId;
    private String contractorId;
    private String amount;
    private String projectName;

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getDepartmentId() {
        return this.departmentId;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getContractorId() {
        return this.contractorId;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setContractorId(String contractorId) {
        this.contractorId = contractorId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
