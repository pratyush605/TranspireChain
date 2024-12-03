package org.hyperledger.fabric.samples.assettransfer;

import java.util.ArrayList;
import java.util.List;


import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
//import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(
        name = "basic",
        info = @Info(
                title = "transpire-chain-smart-contract",
                description = "Chaincode of fund transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
//                contact = @Contact(
//                        email = "a.transfer@example.com",
//                        name = "Adrian Transfer",
//                        url = "https://hyperledger.example.com")
        )
)
@Default
public final class FundTransfer implements ContractInterface {
    private final Genson genson = new Genson();
    private enum FundTransferErrors {
        FundTransfer_NOT_FOUND,
        FundTransfer_ALREADY_EXISTS
    }
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context context, final String transactionId, final String departmentId, final String employeeId, final String receiverDepartmentId, final String amount, final String projectName) {
        ChaincodeStub stub = context.getStub();
//        Fund fund = new Fund(transactionId, departmentId, employeeId, receiverDepartmentId, amount);
//        String fundState = genson.serialize(fund);
//        stub.putStringState(transactionId, fundState);
        Fund fund = AddFund(context, transactionId, departmentId, employeeId, receiverDepartmentId, amount, projectName);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Fund AddFund(final Context context, final String transactionId, final String departmentId, final String employeeId, final String contractorId, final String amount, final String projectName) {
        ChaincodeStub stub = context.getStub();
        String fundState = stub.getStringState(transactionId);

        if (fundState != null && !fundState.isEmpty()) {
            String errorMessage = String.format("Fund %s already exists", transactionId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FundTransferErrors.FundTransfer_ALREADY_EXISTS.toString());
        }
        Fund fund = new Fund(transactionId, departmentId, employeeId, contractorId, amount, projectName);
        fundState = genson.serialize(fund);
        stub.putStringState(transactionId, fundState);
        return fund;
    }
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Fund ReadFund(final Context context, final String transactionId) {
        ChaincodeStub stub = context.getStub();
        String fundState = stub.getStringState(transactionId);
        if (fundState == null || fundState.isEmpty()) {
            String errorMessage = String.format("Fund %s does not exist", transactionId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FundTransferErrors.FundTransfer_NOT_FOUND.toString());
//            TranspireChainSmartContract.FundTransfer.
        }
        Fund fund = genson.deserialize(fundState, Fund.class);
        return fund;
    }
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Fund UpdateFund(final Context context, final String transactionId, final String departmentId, final String employeeId, final String contractorId, final String amount, final String projectName) {
        ChaincodeStub stub = context.getStub();
        String fundState = stub.getStringState(transactionId);
        if (fundState == null || fundState.isEmpty()) {
            String errorMessage = String.format("Fund %s does not exists", transactionId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FundTransferErrors.FundTransfer_NOT_FOUND.toString());
        }
        Fund newFund = new Fund(transactionId, departmentId, employeeId, contractorId, amount, projectName);
        fundState = genson.serialize(newFund);
        stub.putStringState(transactionId, fundState);
        return newFund;
    }
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteFund(final Context context, final String transactionId) {
        ChaincodeStub stub = context.getStub();
        String fundState = stub.getStringState(transactionId);
        if (fundState == null || fundState.isEmpty()) {
            String errorMessage = String.format("Fund %s does not exists", transactionId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FundTransferErrors.FundTransfer_NOT_FOUND.toString());
        }
        stub.delState(transactionId);
    }
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllFunds(final Context context) {
        ChaincodeStub stub = context.getStub();
        List<Fund> queryResults = new ArrayList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Fund fund = genson.deserialize(result.getStringValue(), Fund.class);
            queryResults.add(fund);
        }
        final String response = genson.serialize(queryResults);
        System.out.println(response);
        return response;
    }
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllFundsByProjectName(final Context context, final String projectName) {
        ChaincodeStub stub = context.getStub();
        List<Fund> queryResults = new ArrayList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Fund fund = genson.deserialize(result.getStringValue(), Fund.class);
            if (fund.getProjectName().equals(projectName)) {
                queryResults.add(fund);
            }
        }
        final String response = genson.serialize(queryResults);
        System.out.println(response);
        return response;
    }
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllFundsByDepartmentId(final Context context, final String departmentId) {
        ChaincodeStub stub = context.getStub();
        List<Fund> queryResults = new ArrayList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Fund fund = genson.deserialize(result.getStringValue(), Fund.class);
            if (fund.getDepartmentId().equals(departmentId)) {
                queryResults.add(fund);
            }
        }
        final String response = genson.serialize(queryResults);
        System.out.println(response);
        return response;
    }
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllFundsByEmployeeId(final Context context, final String employeeId) {
        ChaincodeStub stub = context.getStub();
        List<Fund> queryResults = new ArrayList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Fund fund = genson.deserialize(result.getStringValue(), Fund.class);
            if (fund.getEmployeeId().equals(employeeId)) {
                queryResults.add(fund);
            }
        }
        final String response = genson.serialize(queryResults);
        System.out.println(response);
        return response;
    }
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllFundsByContractorId(final Context context, final String contractorId) {
        ChaincodeStub stub = context.getStub();
        List<Fund> queryResults = new ArrayList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Fund fund = genson.deserialize(result.getStringValue(), Fund.class);
            if (fund.getContractorId().equals(contractorId)) {
                queryResults.add(fund);
            }
        }
        final String response = genson.serialize(queryResults);
        System.out.println(response);
        return response;
    }
}
