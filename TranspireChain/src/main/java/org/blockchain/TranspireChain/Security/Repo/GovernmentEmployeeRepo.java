package org.blockchain.TranspireChain.Security.Repo;

import org.blockchain.TranspireChain.Security.Model.GovernmentEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovernmentEmployeeRepo extends JpaRepository<GovernmentEmployee, Long> {
    GovernmentEmployee findByEmail(String email);

    GovernmentEmployee findByUsername(String username);
}
