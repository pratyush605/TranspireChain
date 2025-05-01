import Home from "../components/Home"
import { createBrowserRouter, Outlet } from "react-router-dom";
import ContractorSignup from "../components/signup/ContractorSignup";
import GovernmentEmployeeSignup from "../components/signup/GovernmentEmpolyeeSignup";
import ViewerSignup from "../components/signup/ViewerSignup";
import Login from "../components/Login";
import { logout } from "../controller/authController";
import Navbar from "../components/Navbar";
import { useDispatch } from "react-redux";

const RootLayout = () => {
  return (
    <div className="app">
      <Navbar />
      <main className="pages">
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
            }
          ]
        },
        {
          path: 'login',
          element: <Login/>
        },
        {
          path: 'logout',
          loader: () => {
            const dispatch = useDispatch();
            logout(dispatch);
          }
        }
      ]
    }
  ]);

export default router;