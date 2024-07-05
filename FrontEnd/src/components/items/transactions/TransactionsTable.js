import { RxUpdate } from "react-icons/rx";
import { MdDeleteForever } from "react-icons/md";

function TransactionsTable({transactionsList, buttonUpdateTransactionClick, setUpdateId, deleteTransaction}) {
    return (
        <div className="transactions_table">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Tipo</th>
                        <th>Categoria</th>
                        <th>Descrição</th>
                        <th>Valor</th>
                        <th>Data</th>
                        <th>Atualizar</th>
                        <th>Deletar</th>
                    </tr>
                </thead>
                <tbody>
                    {transactionsList.map((obj, indice) => (
                        <tr key={indice}>
                            <td>{indice + 1}</td>
                            <td>{transactionsList !== null ? obj.type : ""}</td>
                            <td>{transactionsList !== null ? obj.category.replace(/_/g, ' ') : ""}</td>
                            <td>{transactionsList !== null ? obj.description : ""}</td>

                            <td
                                className="transactions_td_value"
                                style={{ color: obj.type === "DESPESA" ? "#A62D2D" : "#0aab2d" }}
                            >
                                {transactionsList !== null && obj.value !== undefined ?
                                    `R$ ${obj.value.toLocaleString('pt-BR', {
                                        minimumFractionDigits: 2,
                                        maximumFractionDigits: 2,
                                    })}` : " "}
                            </td>

                            <td>{transactionsList !== null ? obj.registrationDate : " "}</td>
                            <td
                                className="td_update"
                                onClick={() => {
                                    buttonUpdateTransactionClick();
                                    setUpdateId(obj.id)
                                }}
                            ><RxUpdate /></td>
                            <td
                                className="td_delete"
                                onClick={() => deleteTransaction(obj.id)}
                            ><MdDeleteForever /></td>
                        </tr>
                    ))
                    }
                </tbody>
            </table>
        </div>

    )
}

export default TransactionsTable;