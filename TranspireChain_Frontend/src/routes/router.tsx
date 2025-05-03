import Home from "../components/Home"
import { createBrowserRouter, Outlet } from "react-router-dom";
import ContractorSignup from "../components/signup/ContractorSignup";
import GovernmentEmployeeSignup from "../components/signup/GovernmentEmpolyeeSignup";
import ViewerSignup from "../components/signup/ViewerSignup";
import Login from "../components/Login";
import Navbar from "../components/Navbar";
import VerifyEmail from "../components/signup/VerifyEmail";

const RootLayout = () => {
  return (
    <div className="d-block app">
      <Navbar />
      <main className="d-block pages">
        <Outlet />
      </main>
    </div>
  );
};

const router = createBrowserRouter([
    {
      path: '/',
      element: <RootLayout/>,
      children: [
        { index: true, element: <Home/> },
        {
          path: 'signup',
          children: [
            {index: true},
            {
              path: 'contractorSignup',
              element: <ContractorSignup/>
            },
            {
              path: 'governmentSignup',
              element: <GovernmentEmployeeSignup/>
            },
            {
              path: 'viewerSigup',
              element: <ViewerSignup/>
            },
            {
              path: 'verify',
              element: <VerifyEmail/>
            }
          ]
        },
        {
          path: 'login',
          element: <Login/>
        }
      ]
    }
  ]);

export default router;