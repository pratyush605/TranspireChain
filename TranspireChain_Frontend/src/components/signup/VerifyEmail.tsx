import { useState } from "react";
import {verifyEmail, resendEmail} from "../../controller/authController";
import "../../../public/css/verifyEmail.css";

const VerifyEmail = () => {
    const [action, setAction] = useState<string>('');
  const [email, setEmail] = useState("");
  const [verificationCode, setVerificationCode] = useState("");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if(action === 'verify'){
        verifyEmail({email, verificationCode});
        //redirect using useNvigate of react router
    } else {
        resendEmail(email);
    }
  };
    return (
        <div className="container">
            <h2>Verify!!</h2>
            <form onSubmit={handleSubmit}>
                <input type="email" name="email" placeholder="Email" onChange={(e) => {
                    setEmail(e.target.value);
                }} required/>
                <input name="verificationCode" placeholder="verificatoinCode" onChange={e => {
                    setVerificationCode(e.target.value);
                }}/>
                <button type="submit" onClick={() => {setAction('verify')}}>verify</button>
                <button type="submit" onClick={() => {setAction('resend')}}>resend</button>
            </form>
        </div>
    );
}

export default VerifyEmail;
