import { useState } from "react";
import { governmentEmployeeSignup } from "../../controller/authController";

import '../../assets/css/signup.css';

const GovernmentEmployeeSignup = () => {
  const [id, setId] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [address, setAddress] = useState("");
  const [departmentId, setDepartmentId] = useState("");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    const userData = { memberId: id, username, email, password, phoneNumber, address, departmentId };
    governmentEmployeeSignup(userData);
    //redirect using useNavigate of react router
  };

  return (
    <div className="container">
        <h1>Signup</h1>
        <form onSubmit={handleSubmit}>
            <input type="text" id="id" name="id" placeholder="Contractor Id" value={username} onChange={(event) => setId(event.target.value)} required />
            <br />
            <input type="text" id="username" name="username" placeholder="username" value={username} onChange={(event) => setUsername(event.target.value)} required />
            <br />
            <input type="email" placeholder="Email" value={email} onChange={(event) => setEmail(event.target.value)} required />
            <br />
            <input type="password" placeholder="Password" value={password} onChange={(event) => setPassword(event.target.value)} required />
            <br />
            <input type="tel" placeholder="Phone number" id="phoneNumber" name="phoneNumber" value={phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)} />
            <br />
            <input type="tel" placeholder="Address" id="address" name="address" value={address} onChange={(event) => setAddress(event.target.value)} />
            <br />
            <input type="text" id="departmentId" name="departmentId" placeholder="Department Id" value={username} onChange={(event) => setDepartmentId(event.target.value)} required />
            <br />
            <input type="submit" value="Submit" />
        </form>
        <a href="/loginPage" className="nav-link">Go to Login</a>
        <a href="/verifyPage" className="nav-link">Verify Email</a>
    </div>
  );
};

export default GovernmentEmployeeSignup;
