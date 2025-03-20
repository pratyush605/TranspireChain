package org.blockchain.TranspireChain.GatewayConnection.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
<<<<<<< HEAD
=======
import jakarta.servlet.http.HttpServletRequest;
>>>>>>> 845aa6c (Initial commit)
import org.blockchain.TranspireChain.GatewayConnection.Service.ContractConnection;
import org.blockchain.TranspireChain.GatewayConnection.Service.GatewayConnection;
import org.blockchain.TranspireChain.GatewayConnection.model.Fund;
import org.hyperledger.fabric.client.*;
<<<<<<< HEAD
=======
import org.springframework.http.ResponseEntity;
>>>>>>> 845aa6c (Initial commit)
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

@RestController
@RequestMapping("/contract")
public class ContractController {
    public final GatewayConnection gatewayConnection;

    public final ContractConnection contractConnection;

    public final Contract contract;

    private final Gson gson;

    private final Type fundListType;

    public ContractController(GatewayConnection gatewayConnection, ContractConnection contractConnection) throws CertificateException, IOException, InvalidKeyException {

        this.gatewayConnection = gatewayConnection;

        this.contractConnection = contractConnection;

        Gateway gateway = gatewayConnection.getGateway();

        this.contract = gatewayConnection.getContract(gateway);

        this.gson = new GsonBuilder().setPrettyPrinting().create();

        this.fundListType = new TypeToken<ArrayList<Fund>>(){}.getType();

    }

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    @PostMapping("/init")
<<<<<<< HEAD
    public String initLeger(@RequestBody Fund fund) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        return contractConnection.initLedger(
=======
    public ResponseEntity<?> initLeger(@RequestBody Fund fund, HttpServletRequest request) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        String role = (String) request.getAttribute("userRole");
        if (!"governmentEmployee".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only Government Employees allowed");
        }
        return ResponseEntity.ok(contractConnection.initLedger(
>>>>>>> 845aa6c (Initial commit)
                contract,
                fund.getTransactionId(),
                fund.getDepartmentId(),
                fund.getEmployeeId(),
                fund.getContractorId(),
                fund.getAmount(),
<<<<<<< HEAD
                fund.getProjectName());
    }

    @PostMapping("/addFund")
    public Fund addFund(@RequestBody Fund fund) throws EndorseException, CommitException, SubmitException, CommitStatusException {
=======
                fund.getProjectName()
        ));
    }

    @PostMapping("/addFund")
    public ResponseEntity<?> addFund(@RequestBody Fund fund, HttpServletRequest request) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        String role = (String) request.getAttribute("userRole");
        if(!"contractor".equals(role) && !"governmentEmployee".equals(role)){
            return ResponseEntity.status(403).body("Access Denied: Only Government Employees allowed");
        }
>>>>>>> 845aa6c (Initial commit)
        byte[] json = contractConnection.addFund(
                contract,
                fund.getTransactionId(),
                fund.getDepartmentId(),
                fund.getEmployeeId(),
                fund.getContractorId(),
                fund.getAmount(),
<<<<<<< HEAD
                fund.getProjectName());

        return gson.fromJson(new String(json, StandardCharsets.UTF_8), Fund.class);
=======
                fund.getProjectName()
        );

        return ResponseEntity.ok(
                gson.fromJson(new String(json, StandardCharsets.UTF_8), Fund.class) // Return type is Fund.
        );
>>>>>>> 845aa6c (Initial commit)
    }

    @GetMapping("/readFund")
    public Fund readFund(@RequestParam("transactionId") String transactionId) throws GatewayException {
        byte[] json = contractConnection.readFundById(contract, transactionId);
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), Fund.class);
    }

    @PostMapping("updateFund")
<<<<<<< HEAD
    public Fund updateFund(@RequestBody Fund fund) throws EndorseException, CommitException, SubmitException, CommitStatusException {
=======
    public ResponseEntity<?> updateFund(@RequestBody Fund fund, HttpServletRequest request) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        String role = (String) request.getAttribute("userRole");
        if(!"contractor".equals(role) && !"governmentEmployee".equals(role)){
            return ResponseEntity.status(403).body("Access Denied: Only Government Employees allowed");
        }
>>>>>>> 845aa6c (Initial commit)
        byte[] json = contractConnection.updateFund(
                contract,
                fund.getTransactionId(),
                fund.getDepartmentId(),
                fund.getEmployeeId(),
                fund.getContractorId(),
                fund.getAmount(),
<<<<<<< HEAD
                fund.getProjectName());
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), Fund.class);
    }

    @PostMapping("/deleteFund")
    public String deleteFund(@RequestParam("transactionId") String transactionId) throws GatewayException {
        return contractConnection.deleteFundById(contract, transactionId);
=======
                fund.getProjectName()
        );
        return ResponseEntity.ok(
                gson.fromJson(new String(json, StandardCharsets.UTF_8), Fund.class) // Return type is Fund.
        );
    }

    @PostMapping("/deleteFund")
    public ResponseEntity<?> deleteFund(@RequestParam("transactionId") String transactionId, HttpServletRequest request) throws GatewayException {
        String role = (String) request.getAttribute("userRole");
        if (!"governmentEmployee".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only Government Employees allowed");
        }
        return ResponseEntity.ok(
                contractConnection.deleteFundById(contract, transactionId)
        );
>>>>>>> 845aa6c (Initial commit)
    }

    @GetMapping("/getAllFunds")
    public ArrayList<Fund> getAllFunds() throws GatewayException {
        byte[] json = contractConnection.getAllFunds(contract);
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), fundListType);
    }

    @GetMapping("/getAllFundsByProjectName")
    public ArrayList<Fund> getAllFundsByProjectName(@RequestParam("projectName") String projectName) throws GatewayException {
        byte[] json = contractConnection.getAllFundsByProjectName(contract, projectName);
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), fundListType);
    }

    @GetMapping("/getAllFundsByDepartmentId")
    public ArrayList<Fund> getAllFundsByDepartmentId(@RequestParam("departmentId") String departmentId) throws GatewayException {
        byte[] json = contractConnection.getAllFundsByDepartmentId(contract, departmentId);
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), fundListType);
    }

    @GetMapping("/getAllFundsByEmployeeId")
    public ArrayList<Fund> getAllFundsByEmployeeId(@RequestParam("employeeId") String employeeId) throws GatewayException {
        byte[] json = contractConnection.getAllFundsByEmployeeId(contract, employeeId);
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), fundListType);
    }

    @GetMapping("/getAllFundsByContractorId")
    public ArrayList<Fund> getAllFundsByContractorId(@RequestParam("contractorId") String contractorId) throws GatewayException {
        byte[] json = contractConnection.getAllFundsByContractorId(contract, contractorId);
        return gson.fromJson(new String(json, StandardCharsets.UTF_8), fundListType);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 845aa6c (Initial commit)
