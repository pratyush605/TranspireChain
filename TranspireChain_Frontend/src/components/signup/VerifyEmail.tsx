import { useState } from "react";
import {verifyEmail, resendEmail} from "../../controller/authController";
import { useNavigate } from "react-router-dom";
import stylex from "@stylexjs/stylex";
import { showAlert } from "../../utils/common";
import { useDispatch } from "react-redux";

const signupStyle = stylex.create({
    bg_img: {
        position: 'relative',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '43rem',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundImage: "url('https://wallpapercave.com/wp/wp6509773.jpg')",
    },
    content: {
        textAlign: 'center',
        padding: '60px 32px',
        height: '65%',
        width: '30%',
        backgroundColor: 'rgba(255,255,255,0.09)',
        boxShadow: '-1px 4px 28px 0px rgba(0,0,0,0.75)',
    },
    header: {
        color: 'black',
        fontSize: '33px',
        fontWeight: '600',
        margin: '0 0 35px 0',
        fontFamily: "'Montserrat',sans-serif",
    },
  });

const VerifyEmail = () => {
  const [action, setAction] = useState<string>('');
  const [email, setEmail] = useState("");
  const [verificationCode, setVerificationCode] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if(action === 'verify'){
        if (!email || !verificationCode) {
            showAlert('All fields are required', dispatch);
            return;
        }
        verifyEmail({email, verificationCode});
        navigate('/login');
    } else {
        if (!email) {
            showAlert('Kindly fill the email and then click resend!!', dispatch);
            return;
        }
        resendEmail(email);
    }
  };
    return (
        <div className={stylex(signupStyle.bg_img)}>
            <div className={stylex(signupStyle.content)}>
                <header className={stylex(signupStyle.header)}>Verify!!!</header>
                <form onSubmit={handleSubmit}>
                    <div className="border border-3 border-danger rounded form-group">
                        <input className="form-control" type="email" name="email" placeholder="Email" onChange={(e) => {
                            setEmail(e.target.value);
                        }}/>
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input type="text" className="form-control" name="verificationCode" placeholder="verificatoinCode" onChange={e => {
                            setVerificationCode(e.target.value);
                        }}/>
                    </div>
                    <button className="mt-4 w-100 btn btn-danger" type="submit" onClick={() => {setAction('verify')}}>verify</button>
                    <button className="mt-4 w-100 btn btn-danger" type="submit" onClick={() => {setAction('resend')}}>resend</button>
                </form>
            </div>
        </div>
    );
}

export default VerifyEmail;
