package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.Objects;
@DataType()
public final class Fund {
    @Property()
    private final String transactionId;
    @Property()
    private final String departmentId;
    @Property()
    private final String employeeId;
    @Property()
    private final String contractorId;
    @Property()
    private final String amount;
    @Property()
    private final String projectName;

    public Fund(@JsonProperty("transactionId") final String transactionId,
                @JsonProperty("departmentId") final String departmentId,
                @JsonProperty("employeeId") final String employeeId,
                @JsonProperty("contractorId") final String contractorId,
                @JsonProperty("amount") final String amount,
                @JsonProperty("projectName") final String projectName) {
        this.transactionId = transactionId;
        this.departmentId = departmentId;
        this.employeeId = employeeId;
        this.contractorId = contractorId;
        this.amount = amount;
        this.projectName = projectName;
    }

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        Fund other = (Fund) obj;
        return Objects.deepEquals(
                new String[]{getTransactionId(), getDepartmentId(), getEmployeeId(), getContractorId(), getAmount(), getProjectName()},
                new String[]{other.getTransactionId(), other.getDepartmentId(), other.getEmployeeId(), other.getContractorId(), other.getAmount(), other.getProjectName()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionId(), getDepartmentId(), getEmployeeId(), getContractorId(), getAmount(), getProjectName());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + "@"
                + Integer.toHexString(hashCode())
                + " ["
                + "transactionId="
                + transactionId
                + ", departmentId="
                + departmentId
                + ", employeeId="
                + employeeId
                + ", contractorId="
                + contractorId
                + ", amount="
                + amount
                + ", projectName="
                + projectName
                + "]";
    }
}
