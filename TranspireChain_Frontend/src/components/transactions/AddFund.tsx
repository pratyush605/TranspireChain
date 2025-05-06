import { useState } from "react";
import stylex from "@stylexjs/stylex"
import { addFund, updateFund } from "../../controller/fundController";
import { showAlert } from "../../utils/common";
import store, { RootState } from "../../redux/store";
import { useSelector } from "react-redux";

const addFundStyle = stylex.create({
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

interface propsType {
    action: string
}

const AddFund = (props: propsType) => {
    const [transactionId, setTransactionId] = useState("");
    const [departmentId, setDepartmentId] = useState("");
    const [employeeId, setEmployeeId] = useState("");
    const [contractorId, setContractorId] = useState("");
    const [amount, setAmount] = useState("");
    const [projectName, setProjectName] = useState("");

    const role = useSelector((state: RootState) => {return state.auth.role});

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try{
            if ((role === 'governmentEmployee' || role === 'contractor') && props.action === 'addFund') {
                await addFund({transactionId, departmentId, employeeId, contractorId, amount, projectName});
                showAlert("Fund added successfully", store.dispatch);
            } else if ((role === 'governmentEmployee' || role === 'contractor') && props.action === 'updateFund') {
                await updateFund({transactionId, departmentId, employeeId, contractorId, amount, projectName});
                showAlert("Fund Updated successfully", store.dispatch);
            } else if (role !== 'governmentEmployee' && role !== 'contractor') {
                showAlert("Access Denied!!! Not authorised to add or update funds", store.dispatch);
            }
        } catch (error) {
            showAlert("Error while adding fund", store.dispatch, 'danger');
            console.error("Error while adding fund", error);
        }
    };

    return (
        <div className={stylex(addFundStyle.bg_img)}>
            <div className={stylex(addFundStyle.content)}>
                <header className={stylex(addFundStyle.header)}>Add Transaction</header>
                <form onSubmit={handleSubmit}>
                    <div className="border border-3 border-danger rounded form-group">
                        <input className="form-control" type="text" id="transactionId" name="transactionId" placeholder="Transaction Id" value={transactionId} onChange={(event) => setTransactionId(event.target.value)}/>
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="text" id="departmentId" name="departmentId" placeholder="Department Id" value={departmentId} onChange={(event) => setDepartmentId(event.target.value)}/>
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="text" id="employeeId" name="employeeId" placeholder="Employee Id" value={employeeId} onChange={(event) => setEmployeeId(event.target.value)} required />
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="text" id="contractorId" name="contractorId" placeholder="Contractor Id" value={contractorId} onChange={(event) => setContractorId(event.target.value)} required />
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="number" placeholder="Amount" id="amount" name="amount" value={amount} onChange={(event) => setAmount(event.target.value)} />
                    </div>
                    <div className="border border-3 border-danger rounded mt-4 form-group">
                        <input className="form-control" type="text" placeholder="Project Name" id="projectName" name="projectName" value={projectName} onChange={(event) => setProjectName(event.target.value)} />
                    </div>
                    <button type="submit" className="mt-4 w-100 btn btn-danger">Submit</button>
                </form>
            </div>
        </div>
    );
}

export default AddFund;