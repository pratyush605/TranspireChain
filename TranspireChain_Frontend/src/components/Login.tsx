import { useState } from "react";
import {login} from "../controller/authController";
import "../../public/css/login.css";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    login(email, password);
    //redirect using useNvigate of react router
  };
    return (
        <div className="bg-img">
            <div className="content">
                <header>Login Form</header>
                <form onSubmit={handleSubmit}>
                    <div className="field">
                        <span className="fa fa-user"></span>
                        <input type="email" placeholder="Email" onChange={(event) => setEmail(event.target.value)} />
                    </div>
                    <div className="field space">
                        <span className="fa fa-lock"></span>
                        <input type="password" placeholder="Password" onChange={(event) => setPassword(event.target.value)} />
                    </div>
                    <br/>
                    <div className="field">
                        <input type="submit" value="LOGIN"/>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Login;