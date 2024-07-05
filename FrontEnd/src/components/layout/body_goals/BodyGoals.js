import "./BodyGoals.css"
import { useState, useEffect } from "react";
import { GoGoal } from "react-icons/go";
import { TbPigMoney } from "react-icons/tb";
import FinancialGoalFormBox from "../../items/goals/FinancialGoalFormBox";
import InvestmentGoalFormBox from "../../items/goals/InvestmentGoalFormBox";
import FinancialGoalTable from "../../items/goals/FinancialGoalTable";
import InvestmentGoalTable from "../../items/goals/InvestmentGoalTable";


function BodyGoals() {

    //URL Hosts
    const localhost = "http://localhost:8080/";
    const railwayhost = "https://essencial-backend.up.railway.app/";

    //UseState - Show FormBoxes
    const [showAddInvestmentGoalFormBox, setShowAddInvestmentGoalFormBox] = useState(false);
    const [showAddFinancialGoalFormBox, setShowAddFinancialGoalFormBox] = useState(false);
    const [showUpdateInvestmentGoalFormBox, setShowUpdateInvestmentGoalFormBox] = useState(false);
    const [showUpdateFinancialGoalFormBox, setShowUpdateFinancialGoalFormBox] = useState(false);

    //UseState - Objects
    const [financialGoal, setFinancialGoal] = useState([]);
    const [investmentGoal, setInvestmentGoal] = useState([]);

    //UseState - Financial Goal data Post and Update
    const [financialGoalName, setFinancialGoalName] = useState();
    const [financialGoalValue, setFinancialGoalValue] = useState();
    const [financialGoalDescription, setFinancialGoalDescription] = useState();

    //UseState - Investment Goal data Post
    const [investmentGoalValue, setInvestmentGoalValue] = useState();


    //UseEffect - Get FinancialGoal
    useEffect(() => {
        fetch(localhost + 'financialgoals', {
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
            .then((json) => setFinancialGoal(json))
            .catch((err) => {
                if (err.message === "Not Found") {
                    setFinancialGoal(null);
                }
            })
    }, [])


    //UseEffect - Get InvestmentGoal
    useEffect(() => {
        fetch(localhost + 'investmentgoals', {
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
            .then((json) => setInvestmentGoal(json))
            .catch((err) => {
                if (err.message === "Not Found") {
                    setInvestmentGoal(null);
                }
            })
    }, [])


    
    //Function - Request (Post and Update) Financial Goal
    const requestFinancialGoal = async (method, errorMessage) => {

        try {
            const response = await fetch(localhost + 'financialgoals', {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({
                    name: financialGoalName,
                    value: financialGoalValue,
                    description: financialGoalDescription
                })
            });

            if (!response.ok) {
                alert(errorMessage);
                throw new Error(errorMessage)
            }

            if (financialGoalName === undefined && financialGoalValue === undefined && financialGoalDescription === undefined) {
                alert("Todos os campos estão vazios.");
                throw new Error("Todos os campos estão vazios.");

            } else {
                window.location.reload();
            }

        } catch (error) {
            console.error(errorMessage, error);
        }
    };


    //Function - Request (Post and Update) Investment Goal
    const requestInvestmentGoal = async (method, errorMessage) => {

        try {
            const response = await fetch(localhost + 'investmentgoals', {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({
                    value: investmentGoalValue
                })
            });

            if (!response.ok) {
                alert(errorMessage);
                throw new Error(errorMessage)
            }

            window.location.reload();

        } catch (error) {
            console.error(errorMessage, error);
        }
    };

    //Function - Delete FinancialGoal and InvestmentGoal
    const deleteGoal = async (goal, confirmMessage, url, errorMessage, noGoalMessage) => {
        
        if (goal !== null) {
            var confirm = window.confirm(confirmMessage);

            if (confirm) {
                try {
                    const response = await fetch(url, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${localStorage.getItem("token")}`,
                        }
                    });

                    if (!response.ok) {
                        alert(errorMessage);
                        throw new Error(errorMessage)
                    }

                    window.location.reload();

                } catch (error) {
                    console.error(errorMessage, error);
                }
            }

        } else {
            alert(noGoalMessage)
        }

    };


    //Functions - Buttons Click
    function buttonAddInvestmentGoalClick() {
        setShowAddInvestmentGoalFormBox(!showAddInvestmentGoalFormBox);
        setShowUpdateInvestmentGoalFormBox(false);
    };

    function buttonUpdateInvestmentGoalClick() {
        setShowAddInvestmentGoalFormBox(false);
        setShowUpdateInvestmentGoalFormBox(!showUpdateInvestmentGoalFormBox);
    };

    function buttonAddFinancialGoalClick() {
        setShowAddFinancialGoalFormBox(!showAddFinancialGoalFormBox);
        setShowUpdateFinancialGoalFormBox(false);
    };

    function buttonUpdateFinancialGoalClick() {
        setShowAddFinancialGoalFormBox(false);
        setShowUpdateFinancialGoalFormBox(!showUpdateFinancialGoalFormBox);
    };

    return (
        //Financial Goal
        <div className="body_goals">
            <h2>METAS FINANCEIRAS</h2>

            <div className="body_financialgoal">
                <div className="financialgoal_addbox">
                    <div className="financialgoal_buttonbox" onClick={buttonAddFinancialGoalClick}>
                        <GoGoal />
                        <button>+ Meta Financeira</button>
                    </div>

                    {/*FormBoxes*/}
                    {showAddFinancialGoalFormBox &&
                        <FinancialGoalFormBox
                            title={"Adicionar uma Meta Financeira"}
                            financialGoalName={financialGoalName}
                            setFinancialGoalName={setFinancialGoalName}
                            financialGoalDescription={financialGoalDescription}
                            setFinancialGoalDescription={setFinancialGoalDescription}
                            financialGoalValue={financialGoalValue}
                            setFinancialGoalValue={setFinancialGoalValue}
                            request={() => requestFinancialGoal(
                                "POST",
                                "Erro ao cadastrar a Meta Financeira!"
                            )}
                        />
                    }

                    {showUpdateFinancialGoalFormBox &&
                        <FinancialGoalFormBox
                            title={"Atualizar a Meta Financeira"}
                            financialGoalName={financialGoalName}
                            setFinancialGoalName={setFinancialGoalName}
                            financialGoalDescription={financialGoalDescription}
                            setFinancialGoalDescription={setFinancialGoalDescription}
                            financialGoalValue={financialGoalValue}
                            setFinancialGoalValue={setFinancialGoalValue}
                            request={() => requestFinancialGoal(
                                "PUT",
                                "Erro ao atualizar a Meta Financeira!"
                            )}
                        />
                    }
                </div>

                {/*Table*/}
                <FinancialGoalTable
                    financialGoal={financialGoal}
                    buttonUpdateFinancialGoalClick={buttonUpdateFinancialGoalClick}
                    deleteFinancialGoal={() => deleteGoal(
                        financialGoal,
                        "Você realmente deseja deletar a Meta de Financeira?",
                        localhost + 'financialgoals',
                        "Erro ao deletar a Meta Financeira!",
                        "Não há nenhuma Meta Financeira cadastrada."
                    )}
                />
            </div>

            <div className="body_investmentgoal">
                <div className="investmentgoal_addbox">
                    <div className="investmentgoal_buttonbox" onClick={buttonAddInvestmentGoalClick}>
                        <TbPigMoney />
                        <button>+ Meta de Investimento</button>
                    </div>

                    {/*FormBoxes*/}
                    {showAddInvestmentGoalFormBox &&
                        <InvestmentGoalFormBox
                            title={"Adicionar uma Meta de Investimento"}
                            investmentGoalValue={investmentGoalValue}
                            setInvestmentGoalValue={setInvestmentGoalValue}
                            request={() => requestInvestmentGoal(
                                "POST",
                                "Erro ao cadastrar a Meta de Investimento!"
                            )}
                        />
                    }

                    {showUpdateInvestmentGoalFormBox &&
                        <InvestmentGoalFormBox
                            title={"Atualizar a Meta de Investimento"}
                            investmentGoalValue={investmentGoalValue}
                            setInvestmentGoalValue={setInvestmentGoalValue}
                            request={() => requestInvestmentGoal(
                                "PUT",
                                "Erro ao atualizar a Meta de Investimento!"
                            )}
                        />
                    }
                </div>

                {/*Table*/}
                <InvestmentGoalTable
                    investmentGoal={investmentGoal}
                    buttonUpdateInvestmentGoalClick={buttonUpdateInvestmentGoalClick}
                    deleteInvestmentGoal={() => deleteGoal(
                        investmentGoal,
                        "Você realmente deseja deletar a Meta de Mensal de Investimento?",
                        localhost + 'investmentgoals',
                        "Erro ao deletar a Meta de Investimento!",
                        "Não há nenhuma Meta Mensal de Investimento cadastrada."
                    )}
                />
            </div>
        </div >
    )
}

export default BodyGoals;