import styles from './styles.module.css'
import { BsCheckCircle } from "react-icons/bs";
import Select from '@mui/joy/Select';
import Option from '@mui/joy/Option';
import KeyboardArrowDown from '@mui/icons-material/KeyboardArrowDown';
import { useContext, useState } from "react";
import { MainContext } from '../../contexts/MainContext';
import { FiAlertCircle } from "react-icons/fi";


const BookingDetailsArriveTime = () => {

    const { hour, setHour } = useContext(MainContext)
    const hours = ['00:00','01:00','02:00','03:00','04:00','05:00','06:00','07:00','08:00','09:00','10:00','11:00',
                    '12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00']


    return(
        <div className={styles.arriveTimeContainer}>
            <h2>Seu horário de chegada</h2>
            <div className={styles.arriveTimeSelectContainer}>
                <div className={styles.arriveTimeSelectText}>
                    <BsCheckCircle size={20} />
                    <h3>Seu quarto estara pronto para check in as 14h00</h3>
                </div>
                {
                    !hour ?
                <div className={styles.insertDates}>
                    <FiAlertCircle size={30}  color="red"/>
                    <p>Isira seu horário de chegada.</p>
                </div> : null
                }
                <div className={styles.arriveTimeInput}>
                    <h4>Informe seu horário previsto de chegada</h4>
                    <Select
                        placeholder="Selecione sua hora de chegada"
                        indicator={<KeyboardArrowDown />}
                        sx={{
                            width: 300,
                            margin:'0 0 0 1em',
                            display:'flex',
                        }}
                        onChange={(e, newValue) => setHour(newValue)}
                    >
                        {hours.map((hour,index) => (
                            <Option key={hour} value={hour} className={styles.selectOption} id={`hora${index}`}>{hour}</Option>
                        ))}
                    </Select>
                </div>
            </div>
        </div>
    )
}

export default BookingDetailsArriveTime