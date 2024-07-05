//import "./PeriodFormBox.css"
import { IoIosCheckmarkCircle } from "react-icons/io";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { registerLocale, setDefaultLocale } from 'react-datepicker';
import ptBR from 'date-fns/locale/pt-BR';

registerLocale('pt-BR', ptBR);
setDefaultLocale('pt-BR');


function PeriodFormBox({selectedInitialDate, setSelectedInitialDate, selectedFinalDate, setSelectedFinalDate, buttonClick}) {
    return (
        <div className="period_formbox">
            <div className="period_selectbox">
                <div className="periodbox">
                    <label>Data Inicial:</label>
                    <DatePicker
                        className="date_picker"
                        selected={selectedInitialDate}
                        onChange={date => setSelectedInitialDate(date)}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Selecione uma data"
                    />
                </div>
                <div className="periodbox">
                    <label>Data Final:</label>
                    <DatePicker
                        className="date_picker"
                        selected={selectedFinalDate}
                        onChange={date => setSelectedFinalDate(date)}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Selecione uma data"
                    />
                </div>
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

export default PeriodFormBox;