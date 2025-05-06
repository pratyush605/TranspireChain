import { useState } from "react"
import { getAllFunds, readFund } from "../../controller/fundController";
import fundType from "../../utils/types/fundType";

interface propsType {
    url: string,
    label: string
}

const GetAllFunds = (props: propsType) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [response, setResponse] = useState<fundType | Array<fundType>>();

    const handleSubmit = async () => {
        if (props.url === 'readFund') {
            setResponse(await readFund(searchQuery));
        } else if (props.url === 'getAllFunds') {
            setResponse(await getAllFunds());
        } else if (props.url === 'getAllFundsByProjectName') {
            setResponse(await getAllFunds(props.url, searchQuery));
        } else if (props.url === 'getAllFundsByDepartmentId') {
            setResponse(await getAllFunds(props.url, searchQuery));
        } else if (props.url === 'getAllFundsByEmployeeId') {
            setResponse(await getAllFunds(props.url, searchQuery));
        } else if (props.url === 'getAllFundsByContractorId') {
            setResponse(await getAllFunds(props.url, searchQuery));
        }
    }

    return (
        <>
            {props.label !== '' && (<form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="searchQuery">{props.label}</label>
                    <input className="form-control" name="searchQuery" id="searchQuery" value={searchQuery} onChange={(e) => {setSearchQuery(e.target.value)}}></input>
                </div>
                <button type="submit" className="mt-4 w-10 btn btn-danger">Submit</button>
            </form>)}
            {response && Array.isArray(response)}
        </>
    )
}

export default GetAllFunds