import styles from './styles.module.css'
import { useEffect, useState } from "react";
import api from "../../services";
import { MdLocationOn } from "react-icons/md";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const SearchBar = () => {
    const [startDate, setStartDate] = useState(new Date())
    const [endDate, setEndDate] = useState(new Date())
    const [cities, setCities] = useState([])

    useEffect(() => {
        getCities()
    },[])

    const getCities = async () => {
        try {
            const { data } = await api.get('/api/cities')
            setCities(data)
            console.log(data);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <div className={styles.searchBarContainer}>
            <h1 className={styles.title}>Buscar hotéis, casas e muito mais</h1>
            <form className={styles.searchForm}>
                <div className={styles.whereToGo}>
                    <MdLocationOn  className={styles.locationIcon} />                    
                    <select className={styles.whereToGoInput} name="whereToGo" id="">
                        <option value="Para onde vamos" defaultValue>Para onde vamos</option>
                        {
                            cities.map((city) => (
                                <option key={city.cityDenomination} value={city.cityDenomination}>{city.cityDenomination}</option>
                            ))
                        }
                    </select>
                </div>
                <div className={styles.inputCheckInOut}>
                    <div className={styles.checkIn}>
                    <DatePicker
                        dateFormat='dd/MM/yyyy'
                        selected={startDate} 
                        onChange={date => setStartDate(date)} 
                        selectsStart
                        startDate={startDate}
                        endDate={endDate}
                        className={`${styles.date} ${styles.dateLeft}`} 
                        minDate={new Date()}
                         />
                    </div>
                    <div className={styles.checkOut}>
                    <DatePicker 
                        dateFormat='dd/MM/yyyy'
                        selected={endDate} 
                        onChange={date => setEndDate(date)} 
                        selectsEnd
                        startDate={startDate}
                        endDate={endDate}
                        minDate={startDate}
                        className={`${styles.date} ${styles.dateRight}`} 
                        />
                    </div>
                </div>
                <button className={styles.searchButton} type='submit'>Buscar</button>  
            </form>
        </div>
    )
}

export default SearchBar