import { IoIosAddCircle } from "react-icons/io";

function FinancialGoalFormBox({title, financialGoalName, setFinancialGoalName, financialGoalDescription, 
    setFinancialGoalDescription, financialGoalValue, setFinancialGoalValue, request }) {
    return (
        <div className="financialgoal_formbox">
            <div className="financialgoal_inputbox">
                <h4>{title}</h4>
                <label>Nome</label>
                <input
                    type="text"
                    value={financialGoalName}
                    onChange={(e) => setFinancialGoalName(e.target.value)}
                    onFocus={(e) => { if (e.target.value === "") setFinancialGoalName(undefined); }}
                    onBlur={(e) => { if (e.target.value === "") setFinancialGoalName(undefined); }}
                />
                <label>Descrição</label>
                <input
                    type="text"
                    value={financialGoalDescription}
                    onChange={(e) => setFinancialGoalDescription(e.target.value)}
                    onFocus={(e) => { if (e.target.value === "") setFinancialGoalDescription(undefined); }}
                    onBlur={(e) => { if (e.target.value === "") setFinancialGoalDescription(undefined); }}
                />
                <label>Valor</label>
                <input
                    type="number"
                    className="input_value"
                    value={financialGoalValue}
                    onChange={(e) => setFinancialGoalValue(e.target.value)}
                    onFocus={(e) => { if (e.target.value === "") setFinancialGoalValue(undefined); }}
                    onBlur={(e) => { if (e.target.value === "") setFinancialGoalValue(undefined); }}
                />
            </div>

            <IoIosAddCircle
                className="financialgoal_button_add"
                onClick={request}
            />
        </div>
    )
}

export default FinancialGoalFormBox;