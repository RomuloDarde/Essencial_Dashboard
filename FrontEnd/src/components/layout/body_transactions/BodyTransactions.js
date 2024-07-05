import "./BodyTransactions.css"
import { useState, useEffect } from "react";
import { RiMoneyDollarCircleFill } from "react-icons/ri";

import TransactionsTable from "../../items/transactions/TransactionsTable";
import DebitsRankTable from "../../items/transactions/DebitsRankTable";
import SelectMonthFormBox from "../../items/transactions/SelectMonthFormBox";
import TransactionFormBox from "../../items/transactions/TransactionFormBox";
import PeriodFormBox from "../../items/transactions/PeriodFormBox";
import SelectCategoryFormBox from "../../items/transactions/SelectCategoryFormBox";


function BodyTransactions() {

    //URL Hosts
    const localhost = "http://localhost:8080/";
    const railwayhost = "https://essencial-backend.up.railway.app/";

    //Const - Month Array
    const currentMonth = new Date().getMonth() + 1; // +1 porque os meses começam em 0
    const monthsArray = Array.from({ length: currentMonth }, (_, index) => index + 1);

    //UseState - Show FormBoxes
    const [showAddTransactionFormBox, setShowAddTransactionFormBox] = useState(false);
    const [showUpdateTransactionFormBox, setShowUpdateTransactionFormBox] = useState(false);

    const [showMonthTransactionsListFormBox, setShowMonthTransactionsListFormBox] = useState(false);
    const [showPeriodFormBox, setShowPeriodFormBox] = useState(false);
    const [showCategoryFormBox, setShowCategoryFormBox] = useState(false);
    const [showMonthDebitsRankFormBox, setShowMonthDebitsRankFormBox] = useState(false);

    //UseState - Show Tables
    const [showTransactionsCurrentMonthTable, setShowTransactionsCurrentMonthTable] = useState(true);
    const [showDebitsRankTable, setShowDebitsRankTable] = useState(false);
    const [showTransactionsByMonthTable, setShowTransactionsByMonthTable] = useState(false);
    const [showTransactionsByPeriodTable, setShowTransactionsByPeriodTable] = useState(false);
    const [showTransactionsByCategoryTable, setShowTransactionsByCategoryTable] = useState(false);
    const [showDebitsRankByMonthTable, setShowDebitsRankByMonthTable] = useState(false);

    //UseState - Objects
    const [transactionsCurrentMonth, setTransactionsCurrentMonth] = useState([]);
    const [debitsRankByCategory, setDebitsRankByCategory] = useState([]);
    const [transactionsByMonth, setTransactionsByMonth] = useState([]);
    const [transactionsByPeriod, setTransactionsByPeriod] = useState([]);
    const [transactionsByCategory, setTransactionsByCategory] = useState([]);
    const [debitsRankByCategoryByMonth, setDebitsRankByCategoryByMonth] = useState([]);
    const [categoriesList, setCategoriesList] = useState([])

    //UseState - Transaction data Post and Update
    const [type, setType] = useState();
    const [category, setCategory] = useState();
    const [value, setValue] = useState();
    const [description, setDescription] = useState();
    const [updateId, setUpdateId] = useState();

    //UseState - Data Queries
    const [monthValue, setMonthValue] = useState();
    const [selectedInitialDate, setSelectedInitialDate] = useState(null);
    const [selectedFinalDate, setSelectedFinalDate] = useState(null);
    const [categoryValue, setCategoryValue] = useState();


    //Functions - Buttons Set Visible
    //FORMBOXES
    function buttonAddTransactionClick() {
        setShowAddTransactionFormBox(!showAddTransactionFormBox);
        setShowUpdateTransactionFormBox(false);
        setShowMonthTransactionsListFormBox(false);
        setShowPeriodFormBox(false);
        setShowCategoryFormBox(false)
        setShowMonthDebitsRankFormBox(false);
    };

    function buttonUpdateTransactionClick() {
        setShowAddTransactionFormBox(false);
        setShowUpdateTransactionFormBox(!showUpdateTransactionFormBox);
        setShowMonthTransactionsListFormBox(false);
        setShowPeriodFormBox(false);
        setShowCategoryFormBox(false);
        setShowMonthDebitsRankFormBox(false);
    };

    function buttonSelectMonthTransactionsListClick() {
        setShowAddTransactionFormBox(false);
        setShowUpdateTransactionFormBox(false);
        setShowMonthTransactionsListFormBox(!showMonthTransactionsListFormBox);
        setShowPeriodFormBox(false);
        setShowCategoryFormBox(false);
        setShowMonthDebitsRankFormBox(false);
    };

    function buttonSelectPeriodClick() {
        setShowAddTransactionFormBox(false);
        setShowUpdateTransactionFormBox(false);
        setShowMonthTransactionsListFormBox(false);
        setShowPeriodFormBox(!showPeriodFormBox);
        setShowCategoryFormBox(false);
        setShowMonthDebitsRankFormBox(false);
    };

    function buttonSelectCategoryClick() {
        setShowAddTransactionFormBox(false);
        setShowUpdateTransactionFormBox(false);
        setShowMonthTransactionsListFormBox(false);
        setShowPeriodFormBox(false);
        setShowCategoryFormBox(!showCategoryFormBox);
        setShowMonthDebitsRankFormBox(false);
    };
    function buttonSelectMonthDebitsRankClick() {
        setShowAddTransactionFormBox(false);
        setShowUpdateTransactionFormBox(false);
        setShowMonthTransactionsListFormBox(false);
        setShowPeriodFormBox(false);
        setShowCategoryFormBox(false);
        setShowMonthDebitsRankFormBox(!showMonthDebitsRankFormBox);
    };


    //TABLES
    function buttonTransactionsCurrentMonthListClick() {
        setShowDebitsRankTable(false);
        setShowTransactionsCurrentMonthTable(true);
        setShowTransactionsByMonthTable(false);
        setShowTransactionsByPeriodTable(false);
        setShowTransactionsByCategoryTable(false);
        setShowDebitsRankByMonthTable(false);
    };

    function buttonDebitsRankClick() {
        setShowDebitsRankTable(true);
        setShowTransactionsCurrentMonthTable(false);
        setShowTransactionsByMonthTable(false);
        setShowTransactionsByPeriodTable(false);
        setShowTransactionsByCategoryTable(false);
        setShowDebitsRankByMonthTable(false);
        getGenericList(
            localhost + 'transactions/debits/categoryrank',
            "Erro ao realizar a consulta do ranking de débitos.",
            setDebitsRankByCategory
        );
    };

    function buttonMonthTransactionsQueryClick() {
        setShowDebitsRankTable(false);
        setShowTransactionsCurrentMonthTable(false);
        setShowTransactionsByMonthTable(true);
        setShowTransactionsByPeriodTable(false);
        setShowTransactionsByCategoryTable(false);
        setShowDebitsRankByMonthTable(false);
        getGenericList(
            localhost + 'transactions/bymonth/?month=' + monthValue,
            "Erro ao realizar a consulta por mês.",
            setTransactionsByMonth
        );
    };

    function buttonPeriodQueryClick() {
        setShowDebitsRankTable(false);
        setShowTransactionsCurrentMonthTable(false);
        setShowTransactionsByMonthTable(false);
        setShowTransactionsByPeriodTable(true);
        setShowTransactionsByCategoryTable(false);
        setShowDebitsRankByMonthTable(false);
        if (selectedInitialDate !== null && selectedFinalDate !== null) {
            getGenericList(
                localhost + 'transactions/byperiod/?initialdate='
                + selectedInitialDate.toLocaleDateString()
                + '&finaldate=' + selectedFinalDate.toLocaleDateString(),
                "Erro ao realizar a consulta por período.",
                setTransactionsByPeriod
            );
        }
        
    };

    function buttonCategoryQueryClick() {
        setShowDebitsRankTable(false);
        setShowTransactionsCurrentMonthTable(false);
        setShowTransactionsByMonthTable(false);
        setShowTransactionsByPeriodTable(false);
        setShowTransactionsByCategoryTable(true);
        setShowDebitsRankByMonthTable(false);
        getGenericList(
            localhost + 'transactions/debits/?category=' + categoryValue,
            "Erro ao realizar a consulta por categoria.",
            setTransactionsByCategory
        );
    };

    function buttonDebitsRankByMonthQueryClick() {
        setShowDebitsRankTable(false);
        setShowTransactionsCurrentMonthTable(false);
        setShowTransactionsByMonthTable(false);
        setShowTransactionsByPeriodTable(false);
        setShowTransactionsByCategoryTable(false);
        setShowDebitsRankByMonthTable(true);
        getGenericList(
            localhost + 'transactions/debits/categoryrankbymonth/?month=' + monthValue,
            "Erro ao realizar a consulta do ranking de débitos por mês.",
            setDebitsRankByCategoryByMonth
        );
    };


    //HTTP Requests - Fecth
    //UseEffect - Get CategoriesList
    useEffect(() => {
        fetch(localhost + 'transactions/categories', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((resp) => {
                if (!resp.ok) {
                    throw new Error("Not Found");
                }
                return resp.json()
            })
            .then((json) => setCategoriesList(json))
            .catch((err) => {
                if (err.message === "Not Found") {
                    setCategoriesList(null);
                }
            })
    }, [])


    //UseEffect - Get Transactions at Current Month
    useEffect(() => {
        fetch(localhost + 'transactions/atcurrentmonth', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((resp) => {
                if (!resp.ok) {
                    throw new Error("Not Found");
                }
                return resp.json()
            })
            .then((json) => setTransactionsCurrentMonth(json))
            .catch((err) => {
                if (err.message === "Not Found") {
                    setTransactionsCurrentMonth(null);
                }
            })
    }, [])


    //Function - Get (Generic)
    const getGenericList = async (url, errorMessage, setData) => {
        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            })
            if (!response.ok) {
                alert(errorMessage)
                throw new Error(errorMessage);
            }
            const data = await response.json();
            setData(data);

        } catch (error) {
            console.error(errorMessage, error);
        }
    }


    //Function - Request (Post and Put) Transaction
    const requestTransaction = async (url, method, errorMessage) => {

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({
                    type,
                    category,
                    value,
                    description
                })
            });

            if (!response.ok) {
                alert(errorMessage + " Revise os campos!");
                throw new Error(errorMessage)
            }

            if (type === undefined && category === undefined && value === undefined && description === undefined) {
                alert("Todos os campos estão vazios.");
                throw new Error("Todos os campos estão vazios.");

            } else {
                window.location.reload();
            }

        } catch (error) {
            console.error(errorMessage, error);
        }
    };


    // Function - Delete Transaction
    const deleteTransaction = async (id) => {
        var confirm = window.confirm("Você realmente deseja deletar a Transação?");

        if (confirm) {
            try {
                const response = await fetch(localhost + 'transactions/' + id, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });

                if (!response.ok) {
                    //alert("Erro ao deletar a transação.");
                    throw new Error("Erro ao deletar a transação.")
                }

                window.location.reload();

            } catch (error) {
                console.error('Erro ao deletar a transação:', error);
            }
        }
    };


    return (
        <div className="body_transactions">
            <h2>TRANSAÇÕES</h2>

            <div className="transactions_addbox">
                <div className="transactions_buttonbox" onClick={buttonAddTransactionClick}>
                    <RiMoneyDollarCircleFill />
                    <button>+ Transação</button>
                </div>

                {/*Form Boxes*/}
                {showAddTransactionFormBox &&
                    <div>
                        <TransactionFormBox
                            title={"Adicionar uma Transação"}
                            categoriesList={categoriesList}
                            request={() => requestTransaction(
                                localhost + 'transactions',
                                "POST",
                                "Erro ao cadastrar a transação."
                            )}
                            type={type}
                            setType={setType}
                            category={category}
                            setCategory={setCategory}
                            value={value}
                            setValue={setValue}
                            description={description}
                            setDescription={setDescription}
                        />
                    </div>
                }

                {showUpdateTransactionFormBox &&
                    <div>
                        <TransactionFormBox
                            title={"Atualizar a Transação"}
                            categoriesList={categoriesList}
                            request={() => requestTransaction(
                                localhost +'transactions/' + updateId,
                                "PUT",
                                "Erro ao atualizar a transação."
                            )}
                            type={type}
                            setType={setType}
                            category={category}
                            setCategory={setCategory}
                            value={value}
                            setValue={setValue}
                            description={description}
                            setDescription={setDescription}
                        />
                    </div>
                }

                {showMonthTransactionsListFormBox &&
                    <SelectMonthFormBox
                        title={"Escolha um mês - Transações:"}
                        monthValue={monthValue}
                        setMonthValue={setMonthValue}
                        monthsArray={monthsArray}
                        buttonQueryClick={buttonMonthTransactionsQueryClick}
                    />
                }

                {showMonthDebitsRankFormBox &&
                    <SelectMonthFormBox
                        title={"Escolha um mês - Ranking de Débitos:"}
                        monthValue={monthValue}
                        setMonthValue={setMonthValue}
                        monthsArray={monthsArray}
                        buttonQueryClick={buttonDebitsRankByMonthQueryClick}
                    />
                }

                {showPeriodFormBox &&
                    <PeriodFormBox
                        selectedInitialDate={selectedInitialDate}
                        setSelectedInitialDate={setSelectedInitialDate}
                        selectedFinalDate={selectedFinalDate}
                        setSelectedFinalDate={setSelectedFinalDate}
                        buttonClick={buttonPeriodQueryClick}
                    />
                }

                {showCategoryFormBox &&
                    <SelectCategoryFormBox
                        categoryValue={categoryValue}
                        setCategoryValue={setCategoryValue}
                        categoriesList={categoriesList}
                        buttonClick={buttonCategoryQueryClick}
                    />
                }
            </div>


            {/*Queries*/}
            <div className="transactions_queries">
                <table>
                    <tbody>
                        <tr>
                            <td><button onClick={buttonTransactionsCurrentMonthListClick}>Transações do Mês</button></td>
                            <td><button onClick={buttonSelectMonthTransactionsListClick}>Transações por Mês</button></td>
                            <td><button onClick={buttonSelectPeriodClick}>Transações por Período</button></td>
                        </tr>
                        <tr>
                            <td><button onClick={buttonSelectCategoryClick}>Despesas por Categoria</button></td>
                            <td><button onClick={buttonDebitsRankClick} >Ranking de Despesas por Categoria</button></td>
                            <td><button onClick={buttonSelectMonthDebitsRankClick} >Ranking de Despesas por Categoria por Mês</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>


            {/*Tables*/}
            {showTransactionsCurrentMonthTable && transactionsCurrentMonth !== null &&
                <div>
                    <TransactionsTable
                        transactionsList={transactionsCurrentMonth}
                        buttonUpdateTransactionClick={buttonUpdateTransactionClick}
                        setUpdateId={setUpdateId}
                        deleteTransaction={deleteTransaction}
                    />
                </div>
            }

            {showTransactionsByMonthTable && transactionsByMonth !== null &&
                <div>
                    <TransactionsTable
                        transactionsList={transactionsByMonth}
                        buttonUpdateTransactionClick={buttonUpdateTransactionClick}
                        setUpdateId={setUpdateId}
                        deleteTransaction={deleteTransaction}
                    />
                </div>
            }

            {showTransactionsByPeriodTable && transactionsByPeriod !== null &&
                <div>
                    <TransactionsTable
                        transactionsList={transactionsByPeriod}
                        buttonUpdateTransactionClick={buttonUpdateTransactionClick}
                        setUpdateId={setUpdateId}
                        deleteTransaction={deleteTransaction}
                    />
                </div>
            }

            {showTransactionsByCategoryTable && transactionsByCategory !== null &&
                <div>
                    <TransactionsTable
                        transactionsList={transactionsByCategory}
                        buttonUpdateTransactionClick={buttonUpdateTransactionClick}
                        setUpdateId={setUpdateId}
                        deleteTransaction={deleteTransaction}
                    />
                </div>
            }

            {showDebitsRankTable && debitsRankByCategory !== null &&
                <div>
                    <DebitsRankTable
                        debitsRankList={debitsRankByCategory}
                    />
                </div>
            }

            {showDebitsRankByMonthTable && debitsRankByCategoryByMonth !== null &&
                <div>
                    <DebitsRankTable
                        debitsRankList={debitsRankByCategoryByMonth}
                    />
                </div>
            }
        </div>
    )

}

export default BodyTransactions;