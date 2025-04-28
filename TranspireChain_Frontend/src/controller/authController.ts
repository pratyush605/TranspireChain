import { useDispatch } from "react-redux";
import axiosInstance, { setAuthToken } from "../config/axiosConfig";
import { handleLogin, handleLogout } from "../redux/role/roleSlice";
import { AppDispatch } from "../redux/store";

const dispatch: AppDispatch = useDispatch();
const login = async (email:string, password:string) => {
    try {
      const response = await axiosInstance.post("/user/login", { email, password });

      if (response.status !== 200) {
        throw new Error("Login failed");
      }

      const { jwt, role } = response.data;

      setAuthToken(jwt);
      dispatch(handleLogin({ token: jwt, role }));
    } catch (error) {
      console.error("Error logging in:", error);
    }
};

const logout = async () => {
  const response = await axiosInstance.get("/user/logout");
  if(response.status !== 200){
    throw new Error("Logout failed");
  }
  setAuthToken(null);
  return dispatch(handleLogout());
}

const viewerSignup = async (userData: Object) => {
  const response = await axiosInstance.post("user/viewerSignup", userData);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

const contractorSignup = async (userData: Object) => {
  const response = await axiosInstance.post("user/contractorSignup", userData);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

const governmentEmployeeSignup = async (userData: Object) => {
  const response = await axiosInstance.post("user/employeeSignup", userData);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

const verifyEmail = async (userData: Object) => {
  const response = await axiosInstance.post("user/employeeSignup", userData);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

const resendEmail = async (email: string) => {
  const response = await axiosInstance.post("user/employeeSignup", email);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

export {login, logout, viewerSignup, contractorSignup, governmentEmployeeSignup, verifyEmail, resendEmail};