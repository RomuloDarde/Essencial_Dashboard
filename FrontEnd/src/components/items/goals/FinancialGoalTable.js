import { RxUpdate } from "react-icons/rx";
import { MdDeleteForever } from "react-icons/md";

function FinancialGoalTable({financialGoal, buttonUpdateFinancialGoalClick, deleteFinancialGoal}) {
    return (
        <div className="financialgoal_table">
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Descrição</th>
                        <th>Valor</th>
                        <th>Percentual</th>
                        <th>Quanto falta</th>
                        <th>Atualizar</th>
                        <th>Deletar</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{financialGoal !== null ? financialGoal.name : " "}</td>
                        <td>{financialGoal !== null ? financialGoal.description : " "}</td>

                        <td
                            className="financialgoal_td_value"
                            style={{ color: "#2E6239" }}
                        >
                            {financialGoal !== null && financialGoal.value !== undefined ?
                                `R$ ${financialGoal.value.toLocaleString('pt-BR', {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2,
                                })}` : " "
                            }
                        </td>
                        <td
                            className=
                            "financialgoal_td_value"
                            style={{
                                color: financialGoal !== null && financialGoal.percentage < 0 ?
                                    "#A62D2D" : "#2E6239"
                            }}
                        >
                            {financialGoal !== null && financialGoal.percentage !== undefined ?
                                (financialGoal.percentage < 100 ? `${financialGoal.percentage.toFixed(0)}%` : "100%") : " "
                            }
                        </td>
                        <td className="financialgoal_td_value" >
                            {financialGoal !== null && financialGoal.valueToComplete !== undefined ?
                                (financialGoal.valueToComplete > 0 ? `R$ ${financialGoal.valueToComplete.toLocaleString('pt-BR', {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2,
                                })}` : "R$ 0,00") : " "
                            }
                        </td>
                        <td
                            className="td_update"
                            onClick={buttonUpdateFinancialGoalClick}
                        ><RxUpdate /></td>
                        <td
                            className="td_delete"
                            onClick={deleteFinancialGoal}
                        ><MdDeleteForever /></td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}
export default FinancialGoalTable;