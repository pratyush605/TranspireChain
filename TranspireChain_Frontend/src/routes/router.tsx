import Home from "../components/Home"
import { createBrowserRouter, Outlet } from "react-router-dom";
import ContractorSignup from "../components/signup/ContractorSignup";
import GovernmentEmployeeSignup from "../components/signup/GovernmentEmpolyeeSignup";
import ViewerSignup from "../components/signup/ViewerSignup";
import Login from "../components/Login";
import Navbar from "../components/Navbar";
import VerifyEmail from "../components/signup/VerifyEmail";
import AlertSlide from "../components/AlertSlide";
import GetAllFunds from "../components/transactions/GetAllFunds";
import AddFund from "../components/transactions/AddFund";

const RootLayout = () => {
  return (
    <div className="d-block app">
      <Navbar />
      <AlertSlide/>
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
        },
        {
          path: 'getAllFunds',
          element: <GetAllFunds url='getAllFunds' label=""/>
        },
        {
          path: 'getAllFundsByProjectName',
          element: <GetAllFunds url='getAllFundsByProjectName' label="Search by Project Name"/>
        },
        {
          path: 'getAllFundsByDepartmentId',
          element: <GetAllFunds url='getAllFundsByDepartmentId' label="Search by Department Id"/>
        },
        {
          path: 'getAllFundsByEmployeeId',
          element: <GetAllFunds url='getAllFundsByEmployeeId' label="Search by Employee Id"/>
        },
        {
          path: 'getAllFundsByContractorId',
          element: <GetAllFunds url='getAllFundsByContractorId' label="Search by Contractor Id"/>
        },
        {
          path: 'readFund',
          element: <GetAllFunds url='readFund' label="Search by Transaction Id"/>
        },
        {
          path: 'addFund',
          element: <AddFund action="addFund"/>
        },
        {
          path: 'updateFund',
          element: <AddFund action="updateFund"/>
        }
      ]
    }
  ]);

export default router;