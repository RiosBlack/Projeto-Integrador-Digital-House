import styles from './styles.module.css'
import { MainContext } from "../../contexts/MainContext";
import { FiAlertCircle } from "react-icons/fi";
import { useContext } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import api from '../../services';
import { AuthContext } from '../../contexts/AuthContext';
import Loading from "../../components/Loading";

const BookingDetails = () => {

    const navigate = useNavigate()
    const { checkIn, checkOut, userCity, setErrCity, hour, productDetails, clearBookingDetailsFields, setLoading } = useContext(MainContext)
    const { userData } = useContext(AuthContext)
    const { sku } = useParams()
    const images = productDetails.images.map(element => element.imageUrl)

    const productPostData = {
        bookingStartDate: checkIn.toString().split('/').reverse().join('-'),
        bookingEndDate: checkOut.toString().split('/').reverse().join('-'),
        bookingCheckInTime: '14:00',
        productDTO: productDetails, 
        userDTO: userData
    }

    const headers = {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: localStorage.getItem('@project_token'),
    };

    let diarias = 1
    if(checkIn!==null && checkIn!==null && checkOut!==null && checkOut!==''){
        diarias = parseInt(checkOut.slice(0,2)) - parseInt(checkIn.slice(0,2))
        isNaN(diarias) ? diarias =   1 : diarias
    }

    const handleClick = async () => {
        if(!userCity){ 
            setErrCity(true) 
            return
        }   
        if(!checkIn || !checkOut) {
            return
        }
        if(!hour) {
            return
        }       
        setErrCity(false)
        try {
            setLoading(true)
            await api.post('/api/bookings', productPostData, { headers })
            .then( async response => {
                console.log(response);
            })
        } catch (e) {
            console.log(e);
        } finally {
            setLoading(false)
        }
        navigate(`/product/${sku}/succeed`)
        clearBookingDetailsFields()
    }

    return(
<div className={styles.bookingDetailsContainer}>
            <h2>Detalhes da reserva</h2>
            <div className={styles.bookingDetailsImg}>
                <img src={images[0]} alt={productDetails.productTitle} />
            </div>
            <h3>{productDetails.category.kind}</h3>
            <h2>{productDetails.productTitle}</h2>
            <p>{productDetails.productAddress}</p>
            <hr className={styles.lineDivision}/>
            <div>
                <p>Check in * : {checkIn}</p>
            </div>
            <hr className={styles.lineDivision}/>
            <div>
                <p>Check out * : {checkOut}</p>
            </div>
            <hr className={styles.lineDivision}/>
            {
                !checkIn || !checkOut ?
                <>
                
                <div className={styles.insertDates}>
                    <FiAlertCircle size={30}  color="red"/>
                    <p>Para continuar, você precisa inserir uma data.</p>
                </div> 
                <hr className={styles.lineDivision}/>
                </>
                :
                null
            }
            <div>
                <p style={{fontSize: '20px', paddingBottom:'2em'}}>Valor: R$ {parseInt(productDetails.productPrice)*diarias},00</p>
            </div>
            <button className={styles.bookingDetailsButton} onClick={handleClick}>Confirmar reserva</button>
        </div>
    )
}

export default BookingDetails