//import "./SelectMonthFormBox.css"
import { IoIosCheckmarkCircle } from "react-icons/io";

function SelectMonthFormBox({ title, monthValue, setMonthValue, monthsArray, buttonQueryClick }) {
    return (
        <div className="month_formbox">
            <div className="month_selectbox">
                <label>{title}</label>
                <select
                    value={monthValue}
                    onChange={(e) =>
                        setMonthValue(e.target.value)
                    }
                >
                    <option></option>
                    {monthsArray.map((obj) => (
                        <option>{obj}</option>
                    ))};
                </select>
            </div>
            <IoIosCheckmarkCircle
                className="transactions_button"
                onClick={() => {
                    buttonQueryClick();
                }}
            />
        </div>
    )
}

export default SelectMonthFormBox;