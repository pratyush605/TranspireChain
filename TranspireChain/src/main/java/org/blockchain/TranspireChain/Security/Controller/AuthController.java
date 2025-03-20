package org.blockchain.TranspireChain.Security.Controller;

import org.blockchain.TranspireChain.Security.DTO.LoginDTO;
import org.blockchain.TranspireChain.Security.DTO.RegisterMemberDTO;
import org.blockchain.TranspireChain.Security.DTO.RegisterViewerDTO;
import org.blockchain.TranspireChain.Security.DTO.VerifyUserDTO;
import org.blockchain.TranspireChain.Security.Model.Contractor;
import org.blockchain.TranspireChain.Security.Model.Department;
import org.blockchain.TranspireChain.Security.Model.GovernmentEmployee;
import org.blockchain.TranspireChain.Security.Model.Viewer;
import org.blockchain.TranspireChain.Security.Responses.LoginResponse;
import org.blockchain.TranspireChain.Security.Service.EmailAuthService;
import org.blockchain.TranspireChain.Security.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailAuthService authService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/viewerSignup")
    public ResponseEntity<Viewer> registerViewer(@RequestBody RegisterViewerDTO viewerDTO) {
        Viewer viewer = authService.viewerSignUp(viewerDTO);
        return ResponseEntity.ok(viewer);
    }

    @PostMapping("/contractorSignup")
    public ResponseEntity<Contractor> registerContractor(@RequestBody RegisterMemberDTO contractorDTO){
        Contractor contractor = authService.contractorSignUp(contractorDTO);
        return ResponseEntity.ok(contractor);
    }

    @PostMapping("/employeeSignup")
    public ResponseEntity<GovernmentEmployee> registerEmployee(@RequestBody RegisterMemberDTO employeeDTO){
        GovernmentEmployee employee = authService.employeeSignUp(employeeDTO);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        String jwt = null;
        Object user = authService.login(loginDTO);
        String role;
        if(user != null){
            if(user instanceof Viewer viewer && viewer.isEnabled()){
                jwt = jwtService.createToken(
                        viewer.getUserId(),
                        viewer.getUsername(),
                        viewer.getEmail(),
                        viewer.getPhoneNumber(),
                        viewer.getAddress()
                );
                role = "viewer";
            }
            else if(user instanceof Contractor contractor && contractor.isEnabled()){
                Department department = contractor.getDepartment();
                jwt = jwtService.createToken(
                        contractor.getContractorId(),
                        contractor.getUsername(),
                        contractor.getEmail(),
                        contractor.getPhoneNumber(),
                        contractor.getAddress(),
                        department.getDepartmentId(),
                        department.getDepartment()
                );
                role = "contractor";
            }
            else if (user instanceof GovernmentEmployee employee && employee.isEnabled()){
                Department department = employee.getDepartment();
                jwt = jwtService.createToken(
                        employee.getEmployeeId(),
                        employee.getUsername(),
                        employee.getEmail(),
                        employee.getPhoneNumber(),
                        employee.getAddress(),
                        department.getDepartmentId(),
                        department.getDepartment()
                );
                role = "governmentEmployee";
            } else {
                return ResponseEntity.badRequest().body("Email Not Verified!!! Please verify the email.");
            }
            redisTemplate.opsForValue().set(jwt, role, 30, TimeUnit.MINUTES);
        }
        LoginResponse loginResponse = new LoginResponse(jwt, 1000*60*30);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO userDTO){
        try {
            authService.verifyUser(userDTO);
            return ResponseEntity.ok("Account verified successfully.");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent.");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
