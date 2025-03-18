package org.blockchain.TranspireChain.Security.Repo;

import org.blockchain.TranspireChain.Security.Model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRepo extends JpaRepository<Contractor, Long> {
    Contractor findByEmail(String email);

    Contractor findByUsername(String username);
}
