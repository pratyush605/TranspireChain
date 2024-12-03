package org.blockchain.TranspireChain.GatewayConnection.Service;


import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;


@Service
public class ContractConnection {

    public String initLedger(final Contract contract, final String transactionId, final String departmentId, final String employeeId, final String receiverDepartmentId, final String amount, final String projectName) throws EndorseException, SubmitException, CommitStatusException, CommitException {

        contract.submitTransaction("InitLedger", transactionId, departmentId, employeeId, receiverDepartmentId, amount, projectName);

        return "*** Transaction committed successfully";
    }

    public byte[] getAllFunds(final Contract contract) throws GatewayException {

        return contract.evaluateTransaction("GetAllFunds");
    }

    public byte[] getAllFundsByProjectName(final Contract contract, String projectName) throws GatewayException {

        return contract.evaluateTransaction("GetAllFundsByProjectName", projectName);

    }


    public byte[] getAllFundsByDepartmentId(final Contract contract, String departmentId) throws GatewayException {

        return contract.evaluateTransaction("GetAllFundsByDepartmentId", departmentId);

    }

    public byte[] getAllFundsByEmployeeId(final Contract contract, String employeeId) throws GatewayException {

        return contract.evaluateTransaction("GetAllFundsByEmployeeId", employeeId);

    }


    public byte[] getAllFundsByContractorId(final Contract contract, String contractorId) throws GatewayException {

        return contract.evaluateTransaction("GetAllFundsByContractorId", contractorId);

    }


    public byte[] addFund(final Contract contract, final String transactionId, final String departmentId, final String employeeId, final String contractorId, final String amount, final String projectName) throws EndorseException, SubmitException, CommitStatusException, CommitException {

        System.out.println("*** Transaction committed successfully");

        return  contract.submitTransaction("AddFund", transactionId, departmentId, employeeId, contractorId, amount, projectName);

    }

    public byte[] readFundById(final Contract contract, final String transactionId) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: ReadAsset, function returns asset attributes");

        return contract.evaluateTransaction("ReadFund", transactionId);

    }

    public byte[] updateFund(final Contract contract, final String transactionId, final String departmentId, final String employeeId, final String contractorId, final String amount, final String projectName) throws EndorseException, SubmitException, CommitStatusException, CommitException {

        System.out.println("*** Transaction committed successfully");
        return contract.submitTransaction("UpdateFund", transactionId, departmentId, employeeId, contractorId, amount, projectName);

    }

    public String deleteFundById(final Contract contract, final String transactionId) throws GatewayException {

        contract.evaluateTransaction("DeleteFund", transactionId);

        return "Deletion is successful";

    }

    public void updateNonExistentAsset(final Contract contract) {
        try {
            System.out.println("\n--> Submit Transaction: UpdateAsset asset70, asset70 does not exist and should return an error");

            contract.submitTransaction("UpdateFund", "asset70", "blue", "5", "Tomoko", "300", "hi");

            System.out.println("******** FAILED to return an error");
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            System.out.println("*** Successfully caught the error: ");
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());

            var details = e.getDetails();
            if (!details.isEmpty()) {
                System.out.println("Error Details:");
                for (var detail : details) {
                    System.out.println("- address: " + detail.getAddress() + ", mspId: " + detail.getMspId()
                            + ", message: " + detail.getMessage());
                }
            }
        } catch (CommitException e) {
            System.out.println("*** Successfully caught the error: " + e);
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());
            System.out.println("Status code: " + e.getCode());
        }
    }
}
