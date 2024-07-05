//import "./SelectCategoryFormBox.css"
import { IoIosCheckmarkCircle } from "react-icons/io";

function SelectCategoryFormBox({categoryValue, setCategoryValue, categoriesList, buttonClick}) {
    return (
        <div className="category_formbox">
            <div className="category_selectbox">
                <label>Escolha uma categoria:</label>
                <select
                    value={categoryValue}
                    onChange={(e) => setCategoryValue(e.target.value)}
                >
                    <option></option>
                    {categoriesList.map((obj) => (
                        <option>{obj}</option>
                    ))};
                </select>
            </div>
            <IoIosCheckmarkCircle
                className="transactions_button"
                onClick={() => {
                    buttonClick();
                }}
            />
        </div>
    )
}

export default SelectCategoryFormBox;