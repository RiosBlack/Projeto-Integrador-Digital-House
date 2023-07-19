import styles from "./styles.module.css";
import './custom-styles.css'
import { useContext } from "react";
import { MainContext } from "../../contexts/MainContext";
import { Calendar, DateObject } from "react-multi-date-picker";
import pt_br from 'react-date-object/locales/gregorian_pt_br';
import { useNavigate, useParams } from "react-router-dom";
import 'react-multi-date-picker/styles/colors/teal.css';

const BookingStart = () => {

    const navigate = useNavigate()
    const { checkIn, setCheckIn, checkOut, setCheckOut, dates, setDates, windowSize } = useContext(MainContext)
    const { sku } = useParams()
    
    const handleSubmit = () => {
        navigate(`/product/${sku}/booking`)
    }

    const handleDateChange = (value) => {
        setDates(value)
        if (value && value.length === 1){
            setCheckIn(value[0].format())
            setCheckOut('')
        } else if (value && value.length === 2) {
          setCheckIn(value[0].format());
          setCheckOut(value[1].format());
        } else {
          setCheckIn(null);
          setCheckOut(null);
        }
      };

    return (
        <div className={styles.bookingStartContainer}>
            <div className={styles.calendarContainer}>      
                <div className={styles.calendarTitle}>
                    <h2>Datas disponíveis</h2>
                </div>
                <div className={styles.calendarStyle}>
                    <Calendar
                        locale={pt_br}
                        format="DD/MM/YYYY"
                        numberOfMonths={ windowSize[0] >=700 ? 2 : 1}
                        className='teste teal'
                        range
                        rangeHover
                        onChange={handleDateChange}
                        style={{width:'100%'}}
                        minDate={new DateObject()}
                        value={dates}
                        color={'black'}
                    />    
                </div>
            </div>
            <div className={styles.bookingForm}>
                <form className={styles.box}>
                    <p style={{marginBottom:'1em'}}>Adicione as datas de sua viagem para obter preços exatos</p>
                    <div style={{display:'flex', justifyContent:'space-between', marginBottom:'1em'}}>Check in: <p>{checkIn}</p></div>
                    <div style={{display:'flex', justifyContent:'space-between', marginBottom:'1em'}}>Check out: <p>{checkOut}</p></div>
                    <button  onClick={handleSubmit}>Iniciar reserva</button>
                </form>
            </div>
        </div>
    )
}

export default BookingStart