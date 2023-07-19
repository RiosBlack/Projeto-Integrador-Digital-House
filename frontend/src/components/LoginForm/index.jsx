import styles from './styles.module.css';
import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AuthContext } from '../../contexts/AuthContext';
import InputField from '../../components/InputField';
import InputPasswordField from '../../components/InputPasswordField';
import { MainContext } from '../../contexts/MainContext';
import { FiAlertCircle } from 'react-icons/fi';
import api from '../../services';

const LoginForm = () => {
    const navigate = useNavigate();
    const {
        saveToken,
        displayUserInitials,
        email,
        errEmail,
        setErrEmail,
        password,
        errPassword,
        setErrPassword,
        clearForm,
        onChangeEmail,
        onChangePassword,
        setUserData,
    } = useContext(AuthContext);
    const { displayErrorMessage, bookingPath } = useContext(MainContext);
    const emailPattern = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
    const validateEmail = () => {
        email.match(emailPattern) ? setErrEmail(false) : setErrEmail(true);
    };
    const validatePassword = () => {
        password.length <= 5 ? setErrPassword(true) : setErrPassword(false);
    };

    const handleFormSubmit = async e => {
        e.preventDefault();
        if (!email) {
            setErrEmail(true);
            password.length < 6 ? setErrPassword(true) : null;
            return;
        }
        if (password.length < 6) {
            setErrPassword(true);
            return;
        }

        const userData = {
            email: email,
            password: password,
        };
        const headers = {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json',
            Authorization: localStorage.getItem('@project_token'),
        };

        await api
            .post('api/users/login', userData, { headers })
            .then(async response => {
                if (response.status === 200 || response.status === 201) {
                    const token = response.headers['authorization'];
                    const handledToken = token.replace('Bearer ', '');
                    saveToken(handledToken);
                    setUserData(response.data)
                    localStorage.setItem(
                        '@project_user_sku',
                        response.data.userSku
                    );
                    localStorage.setItem(
                        '@project_all_name',
                        `${response.data.name} ${response.data.surname}`
                    );
                    localStorage.setItem(
                        '@project_user_name',
                        response.data.name
                    );
                    localStorage.setItem(
                        '@project_user_surname',
                        response.data.surname
                    );
                    localStorage.setItem(
                        '@project_user_email',
                        response.data.email
                    );
                    localStorage.setItem(
                        '@project_user_function',
                        response.data.functionName
                    );
                    displayUserInitials();
                    bookingPath ? navigate(bookingPath) : navigate('/home');
                }
            })
            .catch(error => {
                console.log(error);
                displayErrorMessage(
                    'Email e/ou senha erradas. Tente novamente!',
                    'error'
                );
            });
        clearForm();
    };

    return (
        <form className={styles.containerLoginForm}>
            <ToastContainer />
            {bookingPath ? (
                <div className={styles.bookingLoginWarn}>
                    <FiAlertCircle size={30} color="red" />
                    <p>Para fazer uma reserva, você precisa estar logado!</p>
                </div>
            ) : (
                ''
            )}
            <h1>Iniciar sessão</h1>
            <div className={styles.inputField}>
                <InputField
                    label="E-mail"
                    onChange={onChangeEmail}
                    validateInput={validateEmail}
                    error={errEmail}
                    value={email}
                    errMessage="Insira um email válido"
                />
            </div>
            <InputPasswordField
                label="Senha"
                onChange={onChangePassword}
                validateInput={validatePassword}
                error={errPassword}
                value={password}
                errMessage="Senha deve conter ao menos 6 caracteres"
            />
            <button
                className={styles.submitLoginButton}
                onClick={handleFormSubmit}
                disabled={errEmail || errPassword ? true : false}
            >
                Entrar
            </button>
        </form>
    );
};

export default LoginForm;
