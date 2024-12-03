/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public final class FundTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            Fund fund = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");

            assertThat(fund).isEqualTo(fund);
        }

        @Test
        public void isSymmetric() {
            Fund fundA = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");
            Fund fundB = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");

            assertThat(fundA).isEqualTo(fundB);
            assertThat(fundB).isEqualTo(fundA);
        }

        @Test
        public void isTransitive() {
            Fund fundA = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");
            Fund fundB = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");
            Fund fundC = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");

            assertThat(fundA).isEqualTo(fundB);
            assertThat(fundB).isEqualTo(fundC);
            assertThat(fundA).isEqualTo(fundC);
        }

        @Test
        public void handlesInequality() {
            Fund fundA = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");
            Fund fundB = new Fund("asset2", "Red", "40", "Lady", "200", "hi");

            assertThat(fundA).isNotEqualTo(fundB);
        }

        @Test
        public void handlesOtherObjects() {
            Fund fundA = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");
            String fundB = "not a asset";

            assertThat(fundA).isNotEqualTo(fundB);
        }

        @Test
        public void handlesNull() {
            Fund fund = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");

            assertThat(fund).isNotEqualTo(null);
        }
    }

    @Test
    public void toStringIdentifiesAsset() {
        Fund fund = new Fund("asset1", "Blue", "20", "Guy", "100", "hi");

        assertThat(fund.toString()).isEqualTo("Fund@2c8270b7 [transactionId=asset1, departmentId=Blue, employeeId=20, contractorId=Guy, amount=100, projectName=hi]");
    }
}
