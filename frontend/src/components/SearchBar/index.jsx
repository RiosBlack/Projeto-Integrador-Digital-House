import styles from './styles.module.css';
import { useEffect, useState, useContext } from 'react';
import { MainContext } from '../../contexts/MainContext';
import api from '../../services';
import { MdLocationOn } from 'react-icons/md';
import DatePicker, { DateObject } from 'react-multi-date-picker';
import pt_br from 'react-date-object/locales/gregorian_pt_br';

const SearchBar = () => {
    const {
        checkIn,
        checkOut,
        setCheckIn,
        setCheckOut,
        cities,
        setCities,
        setProductsList,
        getProductsByCity,
        getProductsByDates,
        getProductsByDatesAndCity,
        dates,
        setDates,
    } = useContext(MainContext);
    const [city, setCity] = useState('');

    const handleDateChange = value => {
        if (value && value.length === 2) {
            setCheckIn(value[0]);
            setCheckOut(value[1]);
        } else {
            setCheckIn(null);
            setCheckOut(null);
        }
    };

    const handleClick = e => {
        e.preventDefault();
        if (
            checkIn != null &&
            checkIn != '' &&
            checkOut != null &&
            checkOut != '' &&
            city !== ''
        ) {
            console.log('to la');
            getProductsByDatesAndCity(
                checkIn.toString().split('/').reverse().join('-'),
                checkOut.toString().split('/').reverse().join('-'),
                city
            );
        } else if (
            checkIn != null &&
            checkIn != '' &&
            checkOut != null &&
            checkOut != ''
        ) {
            console.log('to aqui');
            getProductsByDates(
                checkIn.toString().split('/').reverse().join('-'),
                checkOut.toString().split('/').reverse().join('-')
            );
        } else if (city !== '') {
            console.log('to na data');
            getProductsByCity(city);
        }
        setCity('');
        setCheckIn(new Date());
        setCheckOut(new Date());
    };

    useEffect(() => {
        getCities();
    }, []);

    const getCities = async () => {
        try {
            const { data } = await api.get('/api/cities');
            setCities(data);
        } catch (e) {
            console.log(
                `Não foi possível completar a requisição para retornar as cidades: ${e}`
            );
        }
    };

    return (
        <div className={styles.searchBarContainer}>
            <h1 className={styles.title}>Buscar hotéis, casas e muito mais</h1>
            <form className={styles.searchForm}>
                <div className={styles.whereToGo}>
                    <MdLocationOn className={styles.locationIcon} />
                    <select
                        className={styles.whereToGoInput}
                        name="whereToGo"
                        id=""
                        onChange={e => setCity(e.target.value)}
                        value={city}
                    >
                        <option value="" defaultValue>
                            Para onde vamos
                        </option>
                        {cities.map(city => (
                            <option
                                key={city.citySku}
                                value={city.cityDenomination}
                            >
                                {city.cityDenomination}
                            </option>
                        ))}
                    </select>
                </div>
                <div className={styles.inputCheckInOut}>
                    <DatePicker
                        locale={pt_br}
                        format="DD/MM/YYYY"
                        dateSeparator=" - "
                        // value={values}
                        onChange={handleDateChange}
                        range
                        rangeHover
                        numberOfMonths={2}
                        placeholder="Check-in - Checkout"
                        style={{
                            width: '100%',
                            height: '2.9em',
                            border: 'none',
                            borderRadius: '5px',
                        }}
                        containerStyle={{ width: '100%' }}
                        hideOnScroll
                        minDate={new DateObject()}
                    />
                </div>
                <button className={styles.searchButton} onClick={handleClick}>
                    Buscar
                </button>
            </form>
        </div>
    );
};

export default SearchBar;
