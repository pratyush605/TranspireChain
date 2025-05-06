import { useState } from "react";
import { governmentEmployeeSignup } from "../../controller/authController";
import { NavLink, useNavigate } from "react-router-dom";
import stylex from "@stylexjs/stylex"

const signupStyle = stylex.create({
  bg_img: {
      position: 'relative',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100%',
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

const GovernmentEmployeeSignup = () => {
  const [id, setId] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [address, setAddress] = useState("");
  const [departmentId, setDepartmentId] = useState("");

  const navigate = useNavigate();

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    const userData = { memberId: id, username, email, password, phoneNumber, address, departmentId };
    governmentEmployeeSignup(userData);
    navigate('/signup/verify');
  };

  return (
    <div className={stylex(signupStyle.bg_img)}>
      <div className={stylex(signupStyle.content)}>
        <header className={stylex(signupStyle.header)}>Government Employee Signup</header>
        <form onSubmit={handleSubmit}>
          <div className="border border-3 border-danger rounded form-group">
            <input className="form-control" type="text" id="id" name="id" placeholder="Government Employee Id" value={id} onChange={(event) => setId(event.target.value)}/>
          </div>
          <div className="border border-3 border-danger rounded mt-4 form-group">
            <input className="form-control" type="text" id="username" name="username" placeholder="Username" value={username} onChange={(event) => setUsername(event.target.value)}/>
          </div>
          <div className="border border-3 border-danger rounded mt-4 form-group">
            <input className="form-control" type="email" id="email" name="email" placeholder="Email" value={email} onChange={(event) => setEmail(event.target.value)} required />
          </div>
          <div className="border border-3 border-danger rounded mt-4 form-group">
            <input className="form-control" type="password" id="password" name="password" placeholder="Password" value={password} onChange={(event) => setPassword(event.target.value)} required />
          </div>
          <div className="border border-3 border-danger rounded mt-4 form-group">
            <input className="form-control" type="tel" placeholder="Phone number" id="phoneNumber" name="phoneNumber" value={phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)} />
          </div>
          <div className="border border-3 border-danger rounded mt-4 form-group">
            <input className="form-control" type="text" placeholder="Address" id="address" name="address" value={address} onChange={(event) => setAddress(event.target.value)} />
          </div>
          <div className="border border-3 border-danger rounded mt-4 form-group">
            <input className="form-control" type="text" id="departmentId" name="departmentId" placeholder="Department Id" value={departmentId} onChange={(event) => setDepartmentId(event.target.value)} required />
          </div>
            <button type="submit" className="mt-4 w-100 btn btn-danger">Submit</button>
        </form>
        <NavLink to="/login" className="mt-2 w-100 btn btn-primary">Go to Login</NavLink>
        <NavLink to="/signup/verify" className="mt-2 w-100 btn btn-primary">Verify Email</NavLink>
      </div>
    </div>
  );
};

export default GovernmentEmployeeSignup;
