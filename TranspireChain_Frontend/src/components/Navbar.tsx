import { useDispatch } from "react-redux";
import { NavLink } from "react-router-dom";
import { logout } from "../controller/authController";

const Navbar = () => {
  const dispatch = useDispatch();
  return (
    <>
      <nav className="d-block navbar navbar-expand-lg bg-secondary bg-gradient">
        <div className="container-fluid">
          <NavLink className="navbar-brand text-white" to="/">TranspireChain</NavLink>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation" >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav">
              <li className="nav-item">
                <NavLink className="nav-link active text-white" aria-current="page" to="/">
                  Home
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link text-white" to="#">
                  Features
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link text-white" to="#">
                  Pricing
                </NavLink>
              </li>
              <li className="nav-item dropdown">
                <div className="nav-link dropdown-toggle text-white" data-bs-toggle="dropdown" aria-expanded="true" >
                  Signup
                </div>
                <ul className="dropdown-menu">
                  <li>
                    <NavLink className={(isActive) => `dropdown-item ${isActive ? 'active' : ''}`} to="/signup/viewerSigup">
                      Viewer Signup
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className={(isActive) => `dropdown-item ${isActive ? 'active' : ''}`} to="/signup/contractorSignup">
                      Contractor Signup
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className={(isActive) => `dropdown-item ${isActive ? 'active' : ''}`} to="/signup/governmentSignup">
                      Gov. Employee Signup
                    </NavLink>
                  </li>
                </ul>
              </li>
              <li className="nav-item">
                <NavLink className={(isActive) => `nav-link text-white ${isActive ? 'active' : ''}`} to="/login">
                  Login
                </NavLink>
              </li>
              <li className="nav-item">
                <button className="nav-link text-white" onClick={() => {logout(dispatch)}}>
                  Logout
                </button>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </>
  );
};
export default Navbar;
