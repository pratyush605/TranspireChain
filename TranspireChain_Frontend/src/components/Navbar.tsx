import { NavLink } from "react-router-dom";

const Navbar = () => {
  return (
    <>
      <nav className="navbar navbar-expand-lg bg-secondary bg-gradient">
        <div className="container-fluid">
          <a className="navbar-brand text-white" href="#">TranspireChain</a>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation" >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav">
              <li className="nav-item">
                <a className="nav-link active text-white" aria-current="page" href="#">
                  Home
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="#">
                  Features
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="#">
                  Pricing
                </a>
              </li>
              <li className="nav-item dropdown">
                <a className="nav-link dropdown-toggle text-white" role="button" data-bs-toggle="dropdown" aria-expanded="false" >
                  Signup
                </a>
                <ul className="dropdown-menu">
                  <li>
                    <NavLink className="dropdown-item" to="/signup/viewerSigup">
                      Viewer Signup
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className="dropdown-item" to="/signup/contractorSignup">
                      Contractor Signup
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className="dropdown-item" to="/signup/governmentSignup">
                      Gov. Employee Signup
                    </NavLink>
                  </li>
                </ul>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="#">
                  Login
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="#">
                  Logout
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </>
  );
};
export default Navbar;
