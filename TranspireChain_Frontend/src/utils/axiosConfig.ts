import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://172.27.49.246:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json"
  },
});

export const setAuthToken = (token: string | null) => {
  if (token) {
    axiosInstance.defaults.headers["Authorization"] = `Bearer ${token}`;
  } else {
    delete axiosInstance.defaults.headers["Authorization"];
  }
};

export default axiosInstance;
