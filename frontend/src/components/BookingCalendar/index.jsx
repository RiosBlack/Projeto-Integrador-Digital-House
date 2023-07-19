import "rsuite/dist/rsuite.css";
import styles from "./styles.module.css";
import { useContext } from "react";
import { MainContext } from "../../contexts/MainContext";
import { Calendar, DateObject } from "react-multi-date-picker";
import pt_br from "react-date-object/locales/gregorian_pt_br";

const BookingCalendar = () => {

    const { windowSize, setCheckIn, setCheckOut, dates, setDates } = useContext(MainContext)

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
                    <h2>Selecione a data da reserva</h2>
                </div>
                <div className={styles.calendarStyle}>
                    <Calendar
                        locale={pt_br}
                        format="DD/MM/YYYY"
                        numberOfMonths={ windowSize[0] >= 700 ? 2 : 1}
                        className='teste'
                        range
                        rangeHover
                        onChange={handleDateChange}
                        style={{width:'100%'}}
                        minDate={new DateObject()}
                        value={dates}
                    />    
                </div>
            </div>
        </div>
    )
}

export default BookingCalendar