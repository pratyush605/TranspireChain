import { useState } from "react";
import { viewerSignup } from "../../controller/authController";

import '../../public/css/signup.css';

const ViewerSignup = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [address, setAddress] = useState("");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    const userData = { username, email, password, phoneNumber, address };
    viewerSignup(userData);
    //redirect using useNavigate of react router
  };

  return (
    <div className="container">
        <h1>Signup</h1>
        <form onSubmit={handleSubmit}>
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
            <input type="submit" value="Submit" />
        </form>
        <a href="/loginPage" className="nav-link">Go to Login</a>
        <a href="/verifyPage" className="nav-link">Verify Email</a>
    </div>
  );
};

export default ViewerSignup;
