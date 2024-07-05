import { IoIosAddCircle } from "react-icons/io";

function InvestmentGoalFormBox({title, investmentGoalValue, setInvestmentGoalValue, request}) {
    return (
        <div className="investmentgoal_formbox">
            <div className="investmentgoal_inputbox">
                <h4>{title}</h4>
                <label>Valor</label>
                <input
                    type="number"
                    value={investmentGoalValue}
                    onChange={(e) => setInvestmentGoalValue(e.target.value)}
                />
            </div>

            <IoIosAddCircle
                className="investmentgoal_button_add"
                onClick={request}
            />
        </div>
    )
}
export default InvestmentGoalFormBox;