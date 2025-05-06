import axiosInstance, { setAuthToken } from "../utils/axiosConfig";
import { handleLogin, handleLogout } from "../redux/roleSlice";
import store, { AppDispatch } from "../redux/store";
import { showAlert } from "../utils/common";

const login = async (email:string, password:string, dispatch: AppDispatch) => {
    try {
      const response = await axiosInstance.post("/user/login", { email, password });

      if (response.status !== 200) {
        throw new Error("Login failed");
      }

      const { token, role } = response.data;

      setAuthToken(token);
      dispatch(handleLogin({ token, role }));
    } catch (error) {
      console.error("Error logging in:", error);
    }
};

const logout = async (dispatch: AppDispatch) => {
  try{
    const response = await axiosInstance.get("/user/logout");
    if(response.status !== 200){
      throw new Error("Logout failed");
    }
    setAuthToken(null);
    showAlert("Logout Successfull", store.dispatch);
    return dispatch(handleLogout());
  } catch (error) {
    console.error("Error logging out:", error);
  }
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
  const response = await axiosInstance.post("user/verify", userData);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

const resendEmail = async (email: string) => {
  const response = await axiosInstance.post("user/resend?email="+email);
  if(response.status !== 200){
    throw new Error("Signup failed with status " + response.status);
  }
}

export {login, logout, viewerSignup, contractorSignup, governmentEmployeeSignup, verifyEmail, resendEmail};