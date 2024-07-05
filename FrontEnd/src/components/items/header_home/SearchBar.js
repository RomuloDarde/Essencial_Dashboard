import { useState } from "react";
import { IoMdSearch } from "react-icons/io";
import { Link } from 'react-router-dom';

const SearchBar = () => {
    const [searchTerm, setSearchTerm] = useState('');

    const links = [
        { to: '/home', label: 'Início' },
        { to: '/notifications', label: 'Notificações' },
        { to: '/account', label: 'Status da Conta' },
        { to: '/transactions', label: 'Transações' },
        { to: '/goals', label: 'Metas Financeiras' },
        { to: '/minimalism', label: 'Minimalismo Financeiro' },
        { to: '/profile', label: 'Dados Cadastrais' },
    ];

    const filteredLinks = links.filter(link =>
        link.label.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleChange = (event) => {
        setSearchTerm(event.target.value);
    };

    return (
        <div>
            <div className="search_bar_inputbox">
                <input
                    type="text"
                    placeholder="Pesquisar"
                    value={searchTerm}
                    onChange={handleChange}
                />
                <IoMdSearch />
            </div>

            {searchTerm !== "" &&
                <div className="search_term">
                    <ul>
                        {filteredLinks.map(link => (
                            <Link key={link.to} className="sidebar_link" to={link.to}>
                                <li>
                                    <button>{link.label}</button>
                                </li>
                            </Link>
                        ))}
                    </ul>
                </div>
            }
        </div>
    )
}
export default SearchBar;