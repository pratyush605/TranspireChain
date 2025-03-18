package org.blockchain.TranspireChain.Security.Service;

import org.blockchain.TranspireChain.Security.Model.Contractor;
import org.blockchain.TranspireChain.Security.Model.GovernmentEmployee;
import org.blockchain.TranspireChain.Security.Model.MyUserDetails;
import org.blockchain.TranspireChain.Security.Model.Viewer;
import org.blockchain.TranspireChain.Security.Repo.ContractorRepo;
import org.blockchain.TranspireChain.Security.Repo.GovernmentEmployeeRepo;
import org.blockchain.TranspireChain.Security.Repo.ViewerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    public ViewerRepo viewerRepo;

    @Autowired
    public ContractorRepo contractorRepo;

    @Autowired
    public GovernmentEmployeeRepo employeeRepo;

    private MyUserDetails createUserDetails(Long id, String username, String email, String password, String role) {
        List<String> hasAuthority;
        List<String> pagePermission;

        switch (role) {
            case "CONTRACTOR":
                hasAuthority = List.of("READ", "WRITE");
                pagePermission = List.of("CONTRACTOR_DASH");
                break;
            case "VIEWER":
                hasAuthority = List.of("READ");
                pagePermission = List.of("VIEWER_DASH");
                break;
            case "EMPLOYEE":
                hasAuthority = List.of("READ", "WRITE", "DELETE");
                pagePermission = List.of("EMPLOYEE_DASH", "STAFF");
                break;
            default:
                hasAuthority = List.of("READ");
                pagePermission = List.of("DEFAULT_DASH");
        }

        return new MyUserDetails(id, username, password, email, hasAuthority, pagePermission);
    }
    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Contractor contractor = contractorRepo.findByEmail(email);
            if (contractor != null) {
                return createUserDetails(contractor.getContractorId(), contractor.getUsername(), contractor.getEmail(), contractor.getPassword(), "CONTRACTOR");
            }

            Viewer viewer = viewerRepo.findByEmail(email);
            if (viewer != null) {
                return createUserDetails(viewer.getUserId(), viewer.getUsername(), viewer.getEmail(), viewer.getPassword(), "VIEWER");
            }

            GovernmentEmployee employee = employeeRepo.findByEmail(email);
            if (employee != null) {
                return createUserDetails(employee.getEmployeeId(), employee.getUsername(), employee.getEmail(), employee.getPassword(), "EMPLOYEE");
            }

            throw new UsernameNotFoundException("User not found with Email: " + email);
    }
}
