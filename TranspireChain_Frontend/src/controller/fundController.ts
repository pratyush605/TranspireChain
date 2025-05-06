import store from "../redux/store";
import axiosInstance from "../utils/axiosConfig";
import { showAlert } from "../utils/common";
import fundType from "../utils/types/fundType";

const addFund = async (fund: fundType) => {
    try{
        const response = await axiosInstance.post('/contract/addFund', fund);
        if(response.status === 403) {
            showAlert('Access Denied: Only Government Employees and Contractors are allowed', store.dispatch, 'danger');
            return;
        } else if (response.status !== 200) {
            throw new Error("Could not add Funds");
        }
        showAlert('Transaction saved successfully', store.dispatch);
    } catch (error) {
        console.error("Error getting data:", error);
    }
}

const updateFund = async (fund: fundType) => {
    try{
        const response = await axiosInstance.post('/contract/updateFund', fund);
        if(response.status === 403) {
            showAlert('Access Denied: Only Government Employees and Contractors are allowed', store.dispatch, 'danger');
            return;
        } else if (response.status !== 200) {
            throw new Error("Could not update Funds");
        }
        showAlert('Transaction saved successfully', store.dispatch);
    } catch (error) {
        console.error("Error getting data:", error);
    }
}

const readFund = async (transactionId: string) => {
    try{
        // response is the Object of Fund
        const url = '/contract/readFund?transactionId=' + transactionId;
        const response = await axiosInstance.get(url);
        if (response.status !== 200) {
            throw new Error("Could not get Funds");
        }
        return response.data;
    } catch (error) {
        console.error("Error getting data:", error);
    }
}

const getAllFunds = async (url='getAllFunds', searchQuery='') => {
    try{
        // response is the Array of Objects
        let path = '/contract/';
        if (url === 'getAllFunds') {
            path = path + url;
        } else if (url === 'getAllFundsByProjectName') {
            path = path + url + '?projectName=' + searchQuery;
        } else if (url === 'getAllFundsByDepartmentId') {
            path = path + url + '?departmentId=' + searchQuery;
        } else if (url === 'getAllFundsByEmployeeId') {
            path = path + url + '?employeeId=' + searchQuery;
        } else if (url === 'getAllFundsByContractorId') {
            path = path + url + '?contractorId=' + searchQuery;
        }
        const response = await axiosInstance.get(path);
        if (response.status !== 200) {
            throw new Error("Could not get Funds");
        }
        return response.data;
    } catch (error) {
        console.error("Error getting data:", error);
    }
}

export {addFund, updateFund, readFund, getAllFunds};