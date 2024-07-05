import { RxUpdate } from "react-icons/rx";
import { MdDeleteForever } from "react-icons/md";

function InvestmentGoalTable({investmentGoal, buttonUpdateInvestmentGoalClick, deleteInvestmentGoal}) {
    return (
        <div className="investmentgoal_table">
            <table>
                <thead>
                    <tr>
                        <th>Valor</th>
                        <th>Status Atual</th>
                        <th>Atualizar</th>
                        <th>Deletar</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td className="investmentgoal_td_value">
                            {investmentGoal !== null && investmentGoal.value !== undefined ?
                                `R$ ${investmentGoal.value.toLocaleString('pt-BR', {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2,
                                })}` : " "
                            }
                        </td>
                        <td
                            className="investmentgoal_td_value"
                            style={{
                                color: investmentGoal !== null && investmentGoal.status === "Não atingida"
                                    ? "#A62D2D" : "#2E6239"
                            }}
                        >{investmentGoal !== null ? investmentGoal.status : " "}
                        </td>
                        <td
                            className="td_update"
                            onClick={buttonUpdateInvestmentGoalClick}
                        ><RxUpdate /></td>
                        <td
                            className="td_delete"
                            onClick={deleteInvestmentGoal}
                        ><MdDeleteForever /></td>
                    </tr>
                </tbody>
            </table>
        </div>

    )
}
export default InvestmentGoalTable;