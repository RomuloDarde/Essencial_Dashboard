//import "./TransactionFormBox.css"
import { IoIosAddCircle } from "react-icons/io";


function TransactionFormBox({ title, categoriesList, request,
    type, setType, category, setCategory, value, setValue, description, setDescription}) {

    return (
        <div className="transactions_formbox">
            <div className="transactions_inputbox">
                <h4>{title}</h4>

                <div className="transactions_enumbox">
                    <div className="transactions_selectbox">
                        <label>Tipo</label>
                        <select
                            className="select_type"
                            value={type}
                            onChange={(e) => setType(e.target.value)}
                        >
                            <option></option>
                            <option>RECEITA</option>
                            <option>DESPESA</option>
                        </select>
                    </div>
                    <div className="transactions_selectbox">
                        <label>Categoria</label>
                        <select
                            className="select_category"
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                        >
                            <option></option>
                            {categoriesList.map((obj) => (
                                <option>{obj}</option>
                            ))};
                        </select>
                    </div>
                </div>

                <label>Descrição</label>
                <input
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
                <label>Valor</label>
                <input
                    type="number"
                    className="input_value"
                    value={value}
                    onChange={(e) => setValue(e.target.value)}
                />
            </div>

            <IoIosAddCircle
                className="transactions_button"
                onClick={request}
            />
        </div>
    )

}

export default TransactionFormBox;