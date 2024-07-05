import "./BodyAccount.css"
import { useState, useEffect } from "react";

function BodyAccount() {

    //URL Hosts
    const localhost = "http://localhost:8080/";
    const railwayhost = "https://essencial-backend.up.railway.app/";

    //UseState
    const [account, setAccount] = useState();
    const [financialGoalPercentage, setFinancialGoalPercentage] = useState();
    const [investmentGoalValue, setInvestmentGoalValue] = useState();


    //UseEffect
    //Total Balance
    useEffect(() => {
        fetch(localhost + 'accounts', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((resp) => resp.json())
            .then((json) => setAccount(json))
            .catch((err) => console.log(err))
    }, [])


    //Financial Goal Percentage
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
            .then((json) => json.percentage.toFixed(2))
            .then((finacialGoalPercentage) => setFinancialGoalPercentage(finacialGoalPercentage))
            .catch((err) => {
                if (err.message === "Not Found") {
                    setFinancialGoalPercentage(0);
                }
            })
    }, [])


    //Investment Goal Value
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
            .then((json) => {
                const formattedValue = json.value.toLocaleString('pt-BR', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                });
                return formattedValue;
            })
            .then((investmentGoalValue) => setInvestmentGoalValue(investmentGoalValue))
            .catch((err) => {
                if (err.message === "Not Found") {
                    setInvestmentGoalValue(0);
                }
            })
    }, [])


    return (
        <div className="body_account">
            <h2>STATUS DA CONTA</h2>

            <div className="account_formbox">
                <form >

                    {account !== undefined &&
                        <div className="account_inputbox">
                            <label>Saldo Total:</label>
                            <div className="input">
                                <label
                                    style={{ color: account.currentBalance <= 0 ? "#A62D2D" : "#2E8405" }}
                                >
                                    {`R$ ${account.currentBalance.toLocaleString('pt-BR', {
                                            minimumFractionDigits: 2,
                                            maximumFractionDigits: 2,
                                        })}` }
                                </label>
                            </div>
                        </div>
                    }

                    {account !== undefined &&
                        <div className="account_inputbox">
                            <label>Saldo do Mês:</label>
                            <div className="input">
                                <label
                                    style={{ color: account.monthBalance < 0 ? "#A62D2D" : "#2E8405"}}
                                > 
                                    { `R$ ${account.monthBalance.toLocaleString('pt-BR', {
                                            minimumFractionDigits: 2,
                                            maximumFractionDigits: 2,
                                        })}` }
                                </label>
                            </div>
                        </div>
                    }

                    <div className="account_inputbox">
                        <label>Meta Financeira:</label>
                        <div className="input">
                            <label style={{ color: financialGoalPercentage < 0 ? "#A62D2D" : "#2E8405" }}>
                                {financialGoalPercentage < 100 ? `${financialGoalPercentage}%` : "100%"}
                            </label>
                        </div>
                    </div>

                    <div className="account_inputbox">
                        <label>Meta de Investimento:</label>
                        <div className="input">
                            <label
                                style={{ color: "#2E8405" }}
                            >{`R$ ${investmentGoalValue}`}</label>
                        </div>
                    </div>

                </form>

                <img src="/images/logo_body.png" alt="Logomarca do Essencial." />
            </div>

        </div>
    )
}

export default BodyAccount;