import { useState } from "react";
import {login} from "../controller/authController";
import { useDispatch } from "react-redux";
import stylex from '@stylexjs/stylex';

const loginStyle = stylex.create({
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
    
const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const dispatch = useDispatch();
  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    login(email, password, dispatch);
    //redirect using useNvigate of react router
  };
    return (
        <div className={stylex(loginStyle.bg_img)}>
            <div className={stylex(loginStyle.content)}>
                <header className={stylex(loginStyle.header)}>Login Form</header>
                <form onSubmit={handleSubmit}>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="email" placeholder="Email" onChange={(event) => setEmail(event.target.value)} />
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="password" placeholder="Password" onChange={(event) => setPassword(event.target.value)} />
                    </div>
                    <button type="submit" className="mt-4 w-100 btn btn-danger">Submit</button>
                </form>
            </div>
        </div>
    );
}

export default Login;