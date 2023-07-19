import styles from './styles.module.css'
import { MainContext } from "../../contexts/MainContext";
import { useContext, useEffect } from "react";

import InputField from "../../components/InputField";
import { useParams } from 'react-router-dom';
import { AuthContext } from '../../contexts/AuthContext';

const BookingDetailsUserForm = () => {
        const { sku } = useParams()
        const { setPath, userCity, onChangeCity, errCity, setErrCity } = useContext(MainContext)
        const validateCity = () => {userCity <= 1 ? setErrCity(true) : setErrCity(false)}

        useEffect(() => {
                setPath(`product/${sku}/booking`)     
        },[])

        return(
                <div className={styles.bookingDetailsUserFormContainer}>
                        <div className={styles.bookingFormInput}>
                                <InputField
                                        label='Nome'
                                        value={localStorage.getItem('@project_user_name')}
                                        disabled={true}
                                        className={styles.inputform}
                                />
                        </div>
                        <div className={styles.bookingFormInput}>
                                <InputField
                                        label='Sobrenome'
                                        value={localStorage.getItem('@project_user_surname')}
                                        disabled={true}
                                />    
                        </div>
                        <div className={styles.bookingFormInput}>  
                                <InputField
                                        label='Email'
                                        value={localStorage.getItem('@project_user_email')}
                                        disabled={true}
                                />
                        </div>
                        <div className={styles.bookingFormInput}>
                                <InputField
                                        label='Cidade de origem'
                                        onChange={onChangeCity}
                                        validateInput={validateCity}
                                        value={userCity}
                                        error={errCity}
                                        errMessage='Isira sua cidade'
                                />
                        </div>
                </div>
        )

}

export default BookingDetailsUserForm