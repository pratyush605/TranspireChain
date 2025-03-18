package org.blockchain.TranspireChain.Security.Service;

import jakarta.mail.MessagingException;
import org.blockchain.TranspireChain.Security.DTO.LoginDTO;
import org.blockchain.TranspireChain.Security.DTO.RegisterMemberDTO;
import org.blockchain.TranspireChain.Security.DTO.RegisterViewerDTO;
import org.blockchain.TranspireChain.Security.DTO.VerifyUserDTO;
import org.blockchain.TranspireChain.Security.Model.Contractor;
import org.blockchain.TranspireChain.Security.Model.Department;
import org.blockchain.TranspireChain.Security.Model.GovernmentEmployee;
import org.blockchain.TranspireChain.Security.Model.Viewer;
import org.blockchain.TranspireChain.Security.Repo.ContractorRepo;
import org.blockchain.TranspireChain.Security.Repo.DepartmentRepo;
import org.blockchain.TranspireChain.Security.Repo.GovernmentEmployeeRepo;
import org.blockchain.TranspireChain.Security.Repo.ViewerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class EmailAuthService {
    @Autowired
    public EmailService emailService;

    @Autowired
    public ViewerRepo viewerRepo;
    @Autowired
    public ContractorRepo contractorRepo;

    @Autowired
    public GovernmentEmployeeRepo employeeRepo;

    @Autowired
    public DepartmentRepo departmentRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    public Viewer viewerSignUp(RegisterViewerDTO viewerDTO){
        Viewer viewer = new Viewer(
                viewerDTO.getUsername(),
                viewerDTO.getEmail(),
                viewerDTO.getPhoneNumber(),
                viewerDTO.getAddress(),
                passwordEncoder.encode(viewerDTO.getPassword())
        );
        viewer.setVerificationCode(generateVerificationCode());
        viewer.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        viewer.setEnabled(false);
        sendVerificationEmail(viewer.getEmail(), viewer.getVerificationCode());
        return viewerRepo.save(viewer);
    }

    public Contractor contractorSignUp (RegisterMemberDTO contractorDTO) {
        Department department = departmentRepo.findByDepartmentId(contractorDTO.getDepartmentId());
        Contractor contractor = new Contractor(
                contractorDTO.getMemberId(),
                contractorDTO.getUsername(),
                contractorDTO.getEmail(),
                contractorDTO.getPhoneNumber(),
                contractorDTO.getAddress(),
                passwordEncoder.encode(contractorDTO.getPassword()),
                department
        );
        contractor.setVerificationCode(generateVerificationCode());
        contractor.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        contractor.setEnabled(false);
        sendVerificationEmail(contractor.getEmail(), contractor.getVerificationCode());
        return contractorRepo.save(contractor);
    }
    public GovernmentEmployee employeeSignUp (RegisterMemberDTO employeeDTO) {
        Department department = departmentRepo.findByDepartmentId(employeeDTO.getDepartmentId());
        GovernmentEmployee employee = new GovernmentEmployee(
                employeeDTO.getMemberId(),
                employeeDTO.getUsername(),
                employeeDTO.getEmail(),
                employeeDTO.getPhoneNumber(),
                employeeDTO.getAddress(),
                department,
                passwordEncoder.encode(employeeDTO.getPassword())
        );
        employee.setVerificationCode(generateVerificationCode());
        employee.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        employee.setEnabled(false);
        sendVerificationEmail(employee.getEmail(), employee.getVerificationCode());
        return employeeRepo.save(employee);
    }

    public Object login(LoginDTO loginDTO) {
        List<Class<?>> userTypes = List.of(Viewer.class, Contractor.class, GovernmentEmployee.class);

        for (Class<?> userType : userTypes) {
            Object user = findUser(loginDTO.getEmail(), userType);
            if (user != null) {
                return authenticateUser(user, loginDTO.getEmail(), loginDTO.getPassword(), userType);
            }
        }

        throw new UsernameNotFoundException("User not found with email: " + loginDTO.getEmail());
    }

    private <T> T findUser(String email, Class<T> type) {
        if (type == Viewer.class) {
            Viewer emailViewer = viewerRepo.findByEmail(email);
            Viewer usernameViewer = viewerRepo.findByEmail(email);
            Viewer viewer = emailViewer != null ? emailViewer: usernameViewer;
            return type.cast(viewer);
        } else if (type == Contractor.class) {
            Contractor emailContractor = contractorRepo.findByEmail(email);
            Contractor usernameContractor = contractorRepo.findByUsername(email);
            Contractor contractor = emailContractor!=null ? emailContractor: usernameContractor;
            return type.cast(contractor);
        } else if (type == GovernmentEmployee.class) {
            GovernmentEmployee emailEmployee = employeeRepo.findByEmail(email);
            GovernmentEmployee usernameEmployee = employeeRepo.findByUsername(email);
            GovernmentEmployee employee = emailEmployee!=null? emailEmployee: usernameEmployee;
            return type.cast(employee);
        }
        return null;
    }

    private <T> T authenticateUser(Object user, String email, String password, Class<T> type) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (authentication.isAuthenticated()) {
            return type.cast(user);
        }
        throw new RuntimeException("Invalid credentials!");
    }

    public void verifyUser(VerifyUserDTO userDTO) {
        List<Class<?>> userTypes = List.of(Viewer.class, Contractor.class, GovernmentEmployee.class);

        for(Class<?> userType: userTypes){
            Object user = findUser(userDTO.getEmail(), userType);
            if(user != null){
                if(user instanceof Viewer viewer){
                    if(viewer.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                        throw new RuntimeException("Verification Code is expired");
                    }
                    if(viewer.getVerificationCode().equals(userDTO.getVerificationCode())){
                        viewer.setEnabled(true);
                        viewer.setVerificationCode(null);
                        viewer.setVerificationCodeExpiresAt(null);
                        viewerRepo.save(viewer);
                    }
                }
                else if(user instanceof Contractor contractor){
                    if(contractor.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                        throw new RuntimeException("Verification Code is expired");
                    }
                    if(contractor.getVerificationCode().equals(userDTO.getVerificationCode())){
                        contractor.setEnabled(true);
                        contractor.setVerificationCode(null);
                        contractor.setVerificationCodeExpiresAt(null);
                        contractorRepo.save(contractor);
                    }
                }
                else if(user instanceof GovernmentEmployee employee) {
                    if(employee.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                        throw new RuntimeException("Verification Code is expired");
                    }
                    if(employee.getVerificationCode().equals(userDTO.getVerificationCode())){
                        employee.setEnabled(true);
                        employee.setVerificationCode(null);
                        employee.setVerificationCodeExpiresAt(null);
                        employeeRepo.save(employee);
                    }
                }
                else {
                    throw new RuntimeException("Invalid Verification Code");
                }
            }
        }
    }

    public void resendVerificationCode(String email){
        Viewer viewer = viewerRepo.findByEmail(email);
        if(viewer != null){
            if(viewer.isEnabled()){
                throw new RuntimeException("Account is already verified");
            }
            viewer.setVerificationCode(generateVerificationCode());
            viewer.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(viewer.getEmail(), viewer.getVerificationCode());
            viewerRepo.save(viewer);
        }
        else {
            Contractor contractor = contractorRepo.findByEmail(email);
            if (contractor != null) {
                if (contractor.isEnabled()) {
                    throw new RuntimeException("Account is already verified");
                }
                contractor.setVerificationCode(generateVerificationCode());
                contractor.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
                sendVerificationEmail(contractor.getEmail(), contractor.getVerificationCode());
                contractorRepo.save(contractor);
            }
            else {
                GovernmentEmployee employee = employeeRepo.findByEmail(email);
                if (employee != null) {
                    if (employee.isEnabled()) {
                        throw new RuntimeException("Account is already verified");
                    }
                    employee.setVerificationCode(generateVerificationCode());
                    employee.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
                    sendVerificationEmail(employee.getEmail(), employee.getVerificationCode());
                    employeeRepo.save(employee);
                } else {
                    throw new RuntimeException("User not found");
                }
            }
        }
    }

    public void sendVerificationEmail(String email, String verificationCode){
        String subject = "Account verification";
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(email, subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
