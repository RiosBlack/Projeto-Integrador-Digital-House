import styles from './styles.module.css'
import { ToastContainer } from "react-toastify";
import InputField from "../../components/InputField";
import InputPasswordField from "../../components/InputPasswordField";
import { AuthContext } from "../../contexts/AuthContext";
import { useContext } from "react";
import api from '../../services';
import { MainContext } from '../../contexts/MainContext';
import { useNavigate } from 'react-router-dom';

const RegisterForm = () => {

    const { name, onChangeName, errName, setErrName, 
            surname, onChangeSurname, errSurname, setErrSurname, 
            email, onChangeEmail, errEmail, setErrEmail,
            emailTwin, onChangeEmailTwin, errEmailTwin, setErrEmailTwin, 
            password, onChangePassword, errPassword, setErrPassword } = useContext(AuthContext)
    const { displayErrorMessage } = useContext(MainContext)
    const validateName = () => { name <= 0 ? setErrName(true) : setErrName(false) }
    const validateSurname = () => { surname <= 0 ? setErrSurname(true) : setErrSurname(false) }
    const emailPattern = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/
    const validateEmail = () => { email.match(emailPattern) ? setErrEmail(false) : setErrEmail(true) }
    const validateEmailTwin = () => {emailTwin===email ? setErrEmailTwin(false) : setErrEmailTwin(true)}
    const validatePassword = () => {password.length <=5 ? setErrPassword (true) : setErrPassword(false)}
    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        const headers = {
            'Content-Type': 'application/json',
          }
        e.preventDefault()
        if(!name || !surname || !email || !emailTwin || !password){
            if(!name){ setErrName(true) }
            if(!surname){ setErrSurname(true) }
            if (!email) { setErrEmail(true) }
            if (!emailTwin) { setErrEmailTwin(true) }
            if(!password) { setErrPassword(true) }
            return
        }
        await api.post('/api/users', {
            name: name,
            surname: surname,
            email: email,
            password: password,
        }, { headers }
        )
        .then( response => {
            if(response.status === 200 || response.status === 201){
            console.log(response.data)
            displayErrorMessage('Conta criada com sucesso!', 'success')
            setTimeout(() => {
                navigate('/home')    
            }, 3000);
            }
        })
        .catch(error => console.log(error))
        //incluir post para criação do usuário
    }
    
    return (
        
        <form className={styles.containerRegisterForm}>
            <ToastContainer />
            <h1>Criar conta</h1>
            <div className={styles.completeNameContainer}>
                <InputField
                    label='Nome'
                    onChange={onChangeName}
                    validateInput={validateName}
                    error={errName}
                    value={name}
                    errMessage='Nome deve conter mais de 1 caracter'
                />
                <InputField
                    label='Sobrenome'
                    onChange={onChangeSurname}
                    validateInput={validateSurname}
                    error={errSurname}
                    value={surname}
                    errMessage='sobrenome errado'
                />
            </div>
            <InputField 
                label='E-mail'
                onChange={onChangeEmail}
                validateInput={validateEmail}
                error={errEmail}
                value={email}
                errMessage='Insira um email válido'
            />
            <InputField 
                label='Repetir e-mail'
                onChange={onChangeEmailTwin}
                validateInput={validateEmailTwin}
                error={errEmailTwin}
                value={emailTwin}
                errMessage='E-mails não correspondem'
            />
            <InputPasswordField 
                label='Senha'
                onChange={onChangePassword}
                validateInput={validatePassword}    
                error={errPassword}
                value={password}
                errMessage='Senha deve conter ao menos 6 caracteres'
            />
            <button 
                className={styles.submitRegisterButton} 
                type="submit"  
                onClick={handleSubmit}
                disabled={errEmail || errPassword || errEmail || errEmailTwin || errSurname ? true : false}
            >
                Criar conta
            </button>
        </form>
    )
}

export default RegisterForm
