/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public final class FundTransferTest {

    private static final class MockKeyValue implements KeyValue {

        private final String key;
        private final String value;

        MockKeyValue(final String key, final String value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getStringValue() {
            return this.value;
        }

        @Override
        public byte[] getValue() {
            return this.value.getBytes();
        }

    }

    private static final class MockFundResultsIterator implements QueryResultsIterator<KeyValue> {

        private final List<KeyValue> fundList;

        MockFundResultsIterator() {
            super();

            fundList = new ArrayList<KeyValue>();

            fundList.add(new MockKeyValue("asset1",
                    "{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }"));
//            fundList.add(new MockKeyValue("asset2",
//                    "{ \"transactionId\": \"asset2\", \"departmentId\": \"red\", \"employeeId\": \"5\",\"contractorId\": \"Brad\", \"amount\": \"400\" }"));
//            fundList.add(new MockKeyValue("asset3",
//                    "{ \"transactionId\": \"asset3\", \"departmentId\": \"green\", \"employeeId\": \"10\",\"contractorId\": \"Jin Soo\", \"amount\": \"500\" }"));
//            fundList.add(new MockKeyValue("asset4",
//                    "{ \"transactionId\": \"asset4\", \"departmentId\": \"yellow\", \"employeeId\": \"10\",\"contractorId\": \"Max\", \"amount\": \"600\" }"));
//            fundList.add(new MockKeyValue("asset5",
//                    "{ \"transactionId\": \"asset5\", \"departmentId\": \"black\", \"employeeId\": \"15\",\"contractorId\": \"Adrian\", \"amount\": \"700\" }"));
//            fundList.add(new MockKeyValue("asset6",
//                    "{ \"transactionId\": \"asset6\", \"departmentId\": \"white\", \"employeeId\": \"15\",\"contractorId\": \"Michel\", \"amount\": \"800\" }"));
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return fundList.iterator();
        }

        @Override
        public void close() throws Exception {
            // do nothing
        }

    }

    @Test
    public void invokeUnknownTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyNoInteractions(ctx);
    }

    @Nested
    class InvokeReadFundTransaction {

        @Test
        public void whenFundExists() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1"))
                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }");

            Fund fund = contract.ReadFund(ctx, "asset1");

            assertThat(fund).isEqualTo(new Fund("asset1", "blue", "5", "Tomoko", "300", "hi"));
        }

        @Test
        public void whenFundDoesNotExist() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.ReadFund(ctx, "asset1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Fund asset1 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
        }
    }

    @Test
    void invokeInitLedgerTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.InitLedger(ctx, "asset1", "blue", "5", "Tomoko", "300", "hi");
//        contract.AddFund(ctx, "asset2", "red", "5", "Brad", "400");
//        contract.AddFund(ctx, "asset3", "green", "10", "Jin Soo", "500");
//        contract.AddFund(ctx, "asset4", "yellow", "10", "Max", "600");
//        contract.AddFund(ctx, "asset5", "black", "15", "Adrian", "700");

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("asset1", "{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}");
//        inOrder.verify(stub).putStringState("asset1", "{\"transactionId\":\"asset1\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"contractorId\":\"Tomoko\",\"amount\":\"300\"}");
//        inOrder.verify(stub).putStringState("asset2", "{\"transactionId\":\"asset2\",\"departmentId\":\"red\",\"employeeId\":\"5\",\"contractorId\":\"Brad\",\"amount\":\"400\"}");
//        inOrder.verify(stub).putStringState("asset3", "{\"transactionId\":\"asset3\",\"departmentId\":\"green\",\"employeeId\":\"10\",\"contractorId\":\"Jin Soo\",\"amount\":\"500\"}");
//        inOrder.verify(stub).putStringState("asset4", "{\"transactionId\":\"asset4\",\"departmentId\":\"yellow\",\"employeeId\":\"10\",\"contractorId\":\"Max\",\"amount\":\"600\"}");
//        inOrder.verify(stub).putStringState("asset5", "{\"transactionId\":\"asset5\",\"departmentId\":\"black\",\"employeeId\":\"15\",\"contractorId\":\"Adrian\",\"amount\":\"700\"}");
    }

    @Nested
    class InvokeAddFundTransaction {

        @Test
        public void whenFundExists() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1"))
                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }");

            Throwable thrown = catchThrowable(() -> {
                contract.AddFund(ctx, "asset1", "blue", "45", "Siobhán", "60", "hi");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Fund asset1 already exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_ALREADY_EXISTS".getBytes());
        }

        @Test
        public void whenFundDoesNotExist() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1")).thenReturn("");

            Fund fund = contract.AddFund(ctx, "asset1", "blue", "45", "Siobhán", "60", "hi");

            assertThat(fund).isEqualTo(new Fund("asset1", "blue", "45", "Siobhán", "60", "hi"));
        }
    }

    @Test
    void invokeGetAllFundsTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockFundResultsIterator());

        String funds = contract.GetAllFunds(ctx);

        assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        ,"
//                + "{\"transactionId\":\"asset2\",\"departmentId\":\"red\",\"employeeId\":\"5\",\"contractorId\":\"Brad\",\"amount\":\"400\"},"
//                + "{\"transactionId\":\"asset3\",\"departmentId\":\"green\",\"employeeId\":\"10\",\"contractorId\":\"Jin Soo\",\"amount\":\"500\"},"
//                + "{\"transactionId\":\"asset4\",\"departmentId\":\"yellow\",\"employeeId\":\"10\",\"contractorId\":\"Max\",\"amount\":\"600\"},"
//                + "{\"transactionId\":\"asset5\",\"departmentId\":\"black\",\"employeeId\":\"15\",\"contractorId\":\"Adrian\",\"amount\":\"700\"},"
//                + "{\"transactionId\":\"asset6\",\"departmentId\":\"white\",\"employeeId\":\"15\",\"contractorId\":\"Michel\",\"amount\":\"800\"}]");

    }

    @Test
    void invokeGetAllFundsByProjectNameTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockFundResultsIterator());

        String funds = contract.GetAllFundsByProjectName(ctx, "hi");

        assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        ,"
//                + "{\"transactionId\":\"asset2\",\"departmentId\":\"red\",\"employeeId\":\"5\",\"contractorId\":\"Brad\",\"amount\":\"400\"},"
//                + "{\"transactionId\":\"asset3\",\"departmentId\":\"green\",\"employeeId\":\"10\",\"contractorId\":\"Jin Soo\",\"amount\":\"500\"},"
//                + "{\"transactionId\":\"asset4\",\"departmentId\":\"yellow\",\"employeeId\":\"10\",\"contractorId\":\"Max\",\"amount\":\"600\"},"
//                + "{\"transactionId\":\"asset5\",\"departmentId\":\"black\",\"employeeId\":\"15\",\"contractorId\":\"Adrian\",\"amount\":\"700\"},"
//                + "{\"transactionId\":\"asset6\",\"departmentId\":\"white\",\"employeeId\":\"15\",\"contractorId\":\"Michel\",\"amount\":\"800\"}]");

    }

    @Test
    void invokeGetAllFundsByDepartmentIdTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockFundResultsIterator());

        String funds = contract.GetAllFundsByDepartmentId(ctx, "blue");

        assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        ,"
//                + "{\"transactionId\":\"asset2\",\"departmentId\":\"red\",\"employeeId\":\"5\",\"contractorId\":\"Brad\",\"amount\":\"400\"},"
//                + "{\"transactionId\":\"asset3\",\"departmentId\":\"green\",\"employeeId\":\"10\",\"contractorId\":\"Jin Soo\",\"amount\":\"500\"},"
//                + "{\"transactionId\":\"asset4\",\"departmentId\":\"yellow\",\"employeeId\":\"10\",\"contractorId\":\"Max\",\"amount\":\"600\"},"
//                + "{\"transactionId\":\"asset5\",\"departmentId\":\"black\",\"employeeId\":\"15\",\"contractorId\":\"Adrian\",\"amount\":\"700\"},"
//                + "{\"transactionId\":\"asset6\",\"departmentId\":\"white\",\"employeeId\":\"15\",\"contractorId\":\"Michel\",\"amount\":\"800\"}]");

    }

    @Test
    void invokeGetAllFundsByEmployeeIdTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockFundResultsIterator());

        String funds = contract.GetAllFundsByEmployeeId(ctx, "5");

        assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        ,"
//                + "{\"transactionId\":\"asset2\",\"departmentId\":\"red\",\"employeeId\":\"5\",\"contractorId\":\"Brad\",\"amount\":\"400\"},"
//                + "{\"transactionId\":\"asset3\",\"departmentId\":\"green\",\"employeeId\":\"10\",\"contractorId\":\"Jin Soo\",\"amount\":\"500\"},"
//                + "{\"transactionId\":\"asset4\",\"departmentId\":\"yellow\",\"employeeId\":\"10\",\"contractorId\":\"Max\",\"amount\":\"600\"},"
//                + "{\"transactionId\":\"asset5\",\"departmentId\":\"black\",\"employeeId\":\"15\",\"contractorId\":\"Adrian\",\"amount\":\"700\"},"
//                + "{\"transactionId\":\"asset6\",\"departmentId\":\"white\",\"employeeId\":\"15\",\"contractorId\":\"Michel\",\"amount\":\"800\"}]");

    }

    @Test
    void invokeGetAllFundsByContractorIdTransaction() {
        FundTransfer contract = new FundTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockFundResultsIterator());

        String funds = contract.GetAllFundsByContractorId(ctx, "Tomoko");

        assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        ,"
//                + "{\"transactionId\":\"asset2\",\"departmentId\":\"red\",\"employeeId\":\"5\",\"contractorId\":\"Brad\",\"amount\":\"400\"},"
//                + "{\"transactionId\":\"asset3\",\"departmentId\":\"green\",\"employeeId\":\"10\",\"contractorId\":\"Jin Soo\",\"amount\":\"500\"},"
//                + "{\"transactionId\":\"asset4\",\"departmentId\":\"yellow\",\"employeeId\":\"10\",\"contractorId\":\"Max\",\"amount\":\"600\"},"
//                + "{\"transactionId\":\"asset5\",\"departmentId\":\"black\",\"employeeId\":\"15\",\"contractorId\":\"Adrian\",\"amount\":\"700\"},"
//                + "{\"transactionId\":\"asset6\",\"departmentId\":\"white\",\"employeeId\":\"15\",\"contractorId\":\"Michel\",\"amount\":\"800\"}]");

    }

//    @Nested
//    class TransferFundTransaction {
//
//        @Test
//        public void whenFundExists() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1"))
//                    .thenReturn("{ \"assetID\": \"asset1\", \"color\": \"blue\", \"size\": 5, \"owner\": \"Tomoko\", \"appraisedValue\": 300 }");
//
//            String oldOwner = contract.TransferFund(ctx, "asset1", "Dr Evil");
//
//            assertThat(oldOwner).isEqualTo("Tomoko");
//        }
//
//        @Test
//        public void whenAssetDoesNotExist() {
//            AssetTransfer contract = new AssetTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.TransferAsset(ctx, "asset1", "Dr Evil");
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Asset asset1 does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ASSET_NOT_FOUND".getBytes());
//        }
//    }

    @Nested
    class UpdateFundTransaction {

        @Test
        public void whenFundExists() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1"))
                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"45\", \"contractorId\": \"Arturo\", \"amount\": \"60\", \"projectName\": \"hi\" }");

            Fund fund = contract.UpdateFund(ctx, "asset1", "pink", "45", "Arturo", "600", "hi");

            assertThat(fund).isEqualTo(new Fund("asset1", "pink", "45", "Arturo", "600", "hi"));
        }

        @Test
        public void whenFundDoesNotExist() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.UpdateFund(ctx, "asset1", "blue", "45", "Alex", "60", "hi");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Fund asset1 does not exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
        }
    }

    @Nested
    class DeleteFundTransaction {

        @Test
        public void whenFundDoesNotExist() {
            FundTransfer contract = new FundTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("asset1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.DeleteFund(ctx, "asset1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Fund asset1 does not exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
        }
    }

//    @Nested
//    class GetAllFundsByProjectNameTransaction {
//
//        @Test
//        public void whenFundExists() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1"))
//                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }");
//
//            String funds = contract.GetAllFundsByProjectName(ctx, "hi");
//
//            assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        }
//
//        @Test
//        public void whenFundDoesNotExist() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.GetAllFundsByProjectName(ctx, "hi");
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Fund, with %s Project Name, does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
//        }
//    }
//
//    @Nested
//    class GetAllFundsByDepartmentIdTransaction {
//
//        @Test
//        public void whenFundExists() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1"))
//                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }");
//
//            String funds = contract.GetAllFundsByDepartmentId(ctx, "blue");
//
//            assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        }
//
//        @Test
//        public void whenFundDoesNotExist() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.GetAllFundsByDepartmentId(ctx, "blue");
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Fund, with %s DepartmentId, does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
//        }
//    }
//
//    @Nested
//    class GetAllFundsByEmployeeIdTransaction {
//
//        @Test
//        public void whenFundExists() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1"))
//                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }");
//
//            String funds = contract.GetAllFundsByEmployeeId(ctx, "5");
//
//            assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        }
//
//        @Test
//        public void whenFundDoesNotExist() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.GetAllFundsByEmployeeId(ctx, "5");
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Fund, with %s EmployeeId, does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
//        }
//    }
//
//    @Nested
//    class GetAllFundsByContractorIdTransaction {
//
//        @Test
//        public void whenFundExists() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1"))
//                    .thenReturn("{ \"transactionId\": \"asset1\", \"departmentId\": \"blue\", \"employeeId\": \"5\", \"contractorId\": \"Tomoko\", \"amount\": \"300\", \"projectName\": \"hi\" }");
//
//            String funds = contract.GetAllFundsByContractorId(ctx, "Tomoko");
//
//            assertThat(funds).isEqualTo("[{\"amount\":\"300\",\"contractorId\":\"Tomoko\",\"departmentId\":\"blue\",\"employeeId\":\"5\",\"projectName\":\"hi\",\"transactionId\":\"asset1\"}]");
//        }
//
//        @Test
//        public void whenFundDoesNotExist() {
//            FundTransfer contract = new FundTransfer();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("asset1")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.GetAllFundsByContractorId(ctx, "Tomoko");
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Fund, with %s ContractorId, does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("FundTransfer_NOT_FOUND".getBytes());
//        }
//    }
}
