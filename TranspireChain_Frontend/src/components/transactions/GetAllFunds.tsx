import { useEffect, useState } from "react"
import { getAllFunds, readFund } from "../../controller/fundController";
import fundType from "../../utils/types/fundType";
import { showAlert } from "../../utils/common";
import store from "../../redux/store";

interface propsType {
    url: string,
    label: string
}

const GetAllFunds = (props: propsType) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [response, setResponse] = useState<fundType | Array<fundType>>();

    const setInitialResponse = async () => {
        if(props.url === 'getAllFunds') {
            const result: Array<fundType> = await getAllFunds();
            setResponse(result);
        }
    }

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try{
            if (props.url === 'readFund') {
                const result: fundType = await readFund(searchQuery);
                setResponse(result);
            } else {
                const result: Array<fundType> = await getAllFunds(props.url, searchQuery);
                setResponse(result);
            }
        } catch (error) {
            console.error("Error while getting transaction data", error);
            showAlert("Error while getting transaction data", store.dispatch);
        }
    }

    useEffect(() => {
        setInitialResponse();
    }, [])
    
    return (
        <>
            {props.url !== 'getAllFunds' && (<form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="searchQuery">{props.label}</label>
                    <input className="form-control" name="searchQuery" id="searchQuery" value={searchQuery} onChange={(e) => {setSearchQuery(e.target.value)}}></input>
                </div>
                <button type="submit" className="mt-4 w-10 btn btn-danger">Submit</button>
            </form>)}
            {response && (
                <table className="table table-bordered" id="permission">
                    <thead className="table-dark">
                        <tr>
                            <th>Transaction Id</th>
                            <th>Department Id</th>
                            <th>Employee Id</th>
                            <th>Contractor Id</th>
                            <th>Amount</th>
                            <th>Project Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Array.isArray(response) ? (
                            response.map((item, index) => (
                            <tr key={index}>
                                <td>{item.transactionId}</td>
                                <td>{item.departmentId}</td>
                                <td>{item.employeeId}</td>
                                <td>{item.contractorId}</td>
                                <td>{item.amount}</td>
                                <td>{item.projectName}</td>
                            </tr>
                            ))
                        ) : (
                            <tr>
                                <td>{response.transactionId}</td>
                                <td>{response.departmentId}</td>
                                <td>{response.employeeId}</td>
                                <td>{response.contractorId}</td>
                                <td>{response.amount}</td>
                                <td>{response.projectName}</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            )}
        </>
    )
}

export default GetAllFunds;