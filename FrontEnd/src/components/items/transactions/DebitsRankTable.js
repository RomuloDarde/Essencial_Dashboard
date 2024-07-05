//import "./DebitsRankTable.css"
import PieChart from "../PieChart";

function DebitsRankTable({ debitsRankList }) {
    return (
        <div className="debits_rank">
            <div className="debits_rank_table">
                <table>
                    <thead>
                        <tr>
                            <th>Categoria</th>
                            <th>Valor</th>
                        </tr>
                    </thead>
                    <tbody>
                        {debitsRankList.map((obj) => (
                            <tr>
                                <td>{debitsRankList !== null ? obj.category.replace(/_/g, ' ') : " "}</td>
                                <td className="transactions_td_value">
                                    {debitsRankList !== null && obj.amount !== undefined ?
                                        `R$ ${obj.amount.toLocaleString('pt-BR', {
                                            minimumFractionDigits: 2,
                                            maximumFractionDigits: 2,
                                        })}` : " "}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {debitsRankList !== null &&
                <PieChart className="piechart" data={debitsRankList} />
            }
        </div>

    )

}

export default DebitsRankTable;