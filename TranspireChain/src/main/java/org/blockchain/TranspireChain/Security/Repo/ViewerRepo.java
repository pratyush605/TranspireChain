package org.blockchain.TranspireChain.Security.Repo;

import org.blockchain.TranspireChain.Security.Model.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewerRepo extends JpaRepository<Viewer, Long> {
    Viewer findByEmail(String email);

    Viewer findByUsername(String username);
}
