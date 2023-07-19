import { Link } from "react-router-dom";
import styles from './styles.module.css'
import LoginForm from '../../components/LoginForm'
import { useEffect, useContext } from "react";
import { MainContext } from "../../contexts/MainContext";
import { AuthContext } from "../../contexts/AuthContext";

const Login = () => {

    const { setPath } = useContext(MainContext)
    const { clearForm } = useContext(AuthContext)

    useEffect(()=> {
        setPath('/login')
        clearForm()
    },[])
    return (
        <div className={styles.containerLogin}>
            <LoginForm />
            <p>Ainda não tem conta? <Link to={'/register'} className={styles.linkRegister}>Registre-se</Link> </p>
        </div>
    )
}

export default Login