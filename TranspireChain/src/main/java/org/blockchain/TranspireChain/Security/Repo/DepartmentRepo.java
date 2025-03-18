package org.blockchain.TranspireChain.Security.Repo;

import org.blockchain.TranspireChain.Security.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    Department findByDepartmentId(Long departmentId);
}
