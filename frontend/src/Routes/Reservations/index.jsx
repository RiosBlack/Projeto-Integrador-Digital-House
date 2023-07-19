import styles from './styles.module.css'
import ProductHeader from "../../components/ProductHeader";
import { useContext, useEffect, useState } from 'react';
import { FiAlertTriangle } from "react-icons/fi";
import { IoArrowBackCircleOutline } from "react-icons/io5";
import { useNavigate } from 'react-router-dom';
import { MainContext } from '../../contexts/MainContext';


const Reservations = () => {

    const navigate = useNavigate()
    const { myReservations, getMyReservations } = useContext(MainContext)
    const [images, setImages] = useState([])

    const handleClick = () => {
        navigate('/')
    }

    const token = localStorage.getItem('@project_token');

    useEffect(() => {
        scrollTo(0, 0);
        getMyReservations(localStorage.getItem('@project_user_sku'))
        if (token === null || token === '') {
            navigate('/');
        }
    },[])

    return(
        <div className={styles.reservationsContainer}>
            <ProductHeader 
                title='Minhas reservas'    
            />
            {
                myReservations.length !== 0
                ? (
                    <div className={styles.reservationContainer}>
                        {
                            myReservations.map((reservation) => (
                                <div key={reservation.bookingSku} className={styles.reservationItem} >
                                    <div>
                                        <img src={reservation.productDTO.images[0].imageUrl} alt={reservation.productDTO.productTitle} />
                                    </div>
                                    <div className={styles.reservationItemDetails}>
                                        <h3>{reservation.productDTO.productTitle}</h3>
                                        <p>Check-in: {reservation.bookingStartDate}</p>
                                        <p>Check-out: {reservation.bookingEndDate}</p>
                                    </div>
                                </div>
                            ))
                        }
                    </div>
                )
                : (
                    <div className={styles.noReservationsContainer}>
                        <FiAlertTriangle className={styles.alertIcon}/>
                        <h3>Não há reservas feitas</h3>    
                        <IoArrowBackCircleOutline className={styles.backHomeIcon} onClick={handleClick}/>
                    </div>
                    
                )
            }
        </div>
    )
}

export default Reservations