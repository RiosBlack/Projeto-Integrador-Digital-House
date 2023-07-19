import { Link, useNavigate } from 'react-router-dom';
import { useState, useContext } from 'react';
import styles from './styles.module.css';
import MenuRoundedIcon from '@mui/icons-material/MenuRounded';
import {AuthContext} from "../../contexts/AuthContext";
import Avatar from "@mui/material/Avatar";
import Stack from "@mui/material/Stack";
import CloseIcon from '@mui/icons-material/Close';
import { motion } from 'framer-motion';

const HamburgerMenu = () => {
    const { initials, logout } = useContext(AuthContext)
    const navigate = useNavigate()
    const [state, setState] = useState(false)
    const variants = {
        open: { opacity: 1, x: 0 },
        closed: { opacity: 0, x: '-100%' },
    }

    return (
        <div className={styles.menuHamburguer}>
            <div onClick={() => setState(!state)}>
                <MenuRoundedIcon
                    className={styles.menuHamburguerIcon}
                    sx={{ fontSize: 40 }}
                />
            </div>
            <motion.nav
                className={styles.menuHamburguerTrasitionActive}
                animate={state ? 'open' : 'closed'}
                variants={variants}
            >
                <div className={styles.menuHamburguerTrasitionActiveMotion}>
                    <CloseIcon
                        className={styles.menuHamburguerIcon}
                        onClick={() => setState(!state)}
                        sx={{ fontSize: 40, color: 'white' }}
                    />
                    <div className={styles.menuHamburguerTrasitionActiveTitile}>
                        {initials ? (
                            <div>
                                <Stack
                                    direction="row"
                                    spacing={2}
                                    className={styles.menuHamburguerStack}
                                >
                                    <Avatar
                                        sx={{ bgcolor: '#fff'}}
                                        style={{
                                            color: '#1dbeb4',
                                        }}
                                    >
                                        {initials}
                                    </Avatar>
                                </Stack>
                            </div>
                        ) : (
                            <div>
                                <p>Menu</p>
                            </div>
                        )}
                    </div>
                </div>
                <ul className={styles.menuHamburguerUl}>
                    {initials ? (
                        <>
                            <li>
                                <Link
                                    onClick={() => (setState(!state), logout())}
                                    className={styles.menuHamburguerUlLI}
                                    to={'/'}
                                >
                                    Fazer Logoff
                                </Link>
                            </li>
                            <li>
                                <Link
                                    onClick={() => (setState(!state))}
                                    className={styles.menuHamburguerUlLI}
                                    to={'/my_bookings'}
                                >
                                    Minhas Reservas
                                </Link>
                            </li>
                            <li>
                                <Link
                                    onClick={() => (setState(!state), logout())}
                                    className={styles.menuHamburguerUlLI}
                                    to={'/admin/dashboard/new_product'}
                                >
                                    Criar Produto
                                </Link>
                            </li>
                        </>
                    ) : (
                        <>
                            <li>
                                <Link
                                    className={styles.menuHamburguerUlLI}
                                    to={'/'}
                                    onClick={() => setState(!state)}
                                >
                                    Home
                                </Link>
                            </li>
                            <li>
                                <Link
                                    className={styles.menuHamburguerUlLI}
                                    to={'/register'}
                                    onClick={() => setState(!state)}
                                >
                                    Criar conta
                                </Link>
                            </li>
                            <li>
                                <Link
                                    className={styles.menuHamburguerUlLI}
                                    to={'/login'}
                                    onClick={() => setState(!state)}
                                >
                                    Fazer login
                                </Link>
                            </li>
                        </>
                    )}
                </ul>
            </motion.nav>
        </div>
    );
};

export default HamburgerMenu;
