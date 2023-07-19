import styles from './styles.module.css';
import { Link, useNavigate } from 'react-router-dom';
import HamburgerMenu from '../HamburgerMenu';
import { useContext, useEffect } from 'react';
import { AuthContext } from '../../contexts/AuthContext';
import { MainContext } from '../../contexts/MainContext';
import Avatar from '@mui/material/Avatar';

const Header = () => {
    const navigate = useNavigate();
    const createAccount = { label: 'Criar conta', path: '/register' };
    const login = { label: 'Iniciar sessão', path: '/login' };
    const { initials, logout, displayUserInitials } = useContext(AuthContext);
    const { path } = useContext(MainContext);

    const buttons = [
        { label: 'Criar conta', path: '/register' },
        { label: 'Iniciar sessão', path: '/login' },
    ];

    const functionUser = localStorage.getItem('@project_user_function');

    useEffect(() => {
        displayUserInitials();
    }, []);

    return (
        <header className={styles.containerHeader}>
            <div className={styles.logo}>
                <Link to={'/'}>
                    <img src="../../images/logo-icon.png" alt="icon-car" />
                </Link>
                <div className={styles.nameDiv}>
                    <Link to={'/'} className={styles.slogan}>
                        <h1 className={styles.name}> Royal Reservations</h1>
                        Sinta-se bem, mesmo longe de casa
                    </Link>
                </div>
            </div>
            <div>
                <div>
                    <HamburgerMenu
                        createAccount={createAccount}
                        login={login}
                    />
                </div>
                <nav className={styles.nav}>
                    <ul>
                        {initials ? (
                            <>
                                <Avatar
                                    sx={{ bgcolor: '#1dbeb4' }}
                                    style={{ color: '#fff' }}
                                >
                                    {initials}
                                </Avatar>
                                <button
                                    className={styles.buttonHeader}
                                    onClick={() => (logout(), navigate('/'))}
                                >
                                    Fazer logoff
                                </button>
                                <button
                                    className={styles.buttonHeader}
                                    onClick={() => navigate('/my_bookings')}
                                >
                                    Minhas Reservas
                                </button>
                                {functionUser === 'Admin' ? (
                                    <button
                                        className={styles.buttonHeader}
                                        onClick={() =>
                                            navigate(
                                                '/admin/dashboard/new_product'
                                            )
                                        }
                                    >
                                        Criar Produto
                                    </button>
                                ) : (
                                    <></>
                                )}
                            </>
                        ) : (
                            <>
                                {buttons.map((button, index) => (
                                    <li
                                        key={index}
                                        className={`${
                                            button.path == path
                                                ? styles.buttonHeaderDisplay
                                                : styles.buttonHeader
                                        }`}
                                    >
                                        <button
                                            className={styles.buttonHeader}
                                            onClick={() =>
                                                navigate(button.path)
                                            }
                                        >
                                            {button.label}
                                        </button>
                                    </li>
                                ))}
                            </>
                        )}
                    </ul>
                </nav>
            </div>
        </header>
    );
};

export default Header;
