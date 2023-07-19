import styles from './styles.module.css'
import RegisterForm from "../../components/RegisterForm";
import { Link } from "react-router-dom";
import { MainContext } from "../../contexts/MainContext";
import { AuthContext } from "../../contexts/AuthContext";
import { useContext, useEffect } from "react";

 const Register = () => {

    const {setPath, setBookingPath } = useContext(MainContext)
    const {clearForm} = useContext(AuthContext)

    useEffect(()=>{
        setPath('/register')
        setBookingPath('')
        clearForm()
    },[])

    return(
        <div className={styles.containerRegister}>
            <RegisterForm />
            <p className={styles.registerP}>Possui uma conta? <Link to={'/login'} className={styles.linkLogin}>Faça login</Link> </p>
        </div>
    )
 }

 export default Register