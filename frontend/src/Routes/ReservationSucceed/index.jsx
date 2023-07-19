import { useContext } from 'react';
import { useNavigate } from "react-router-dom";
import { MainContext } from "../../contexts/MainContext";
import Succeed from "../../components/Succeed";

const ReservationSucced = () => {

    const { setCheckIn, setCheckOut, setDates } = useContext(MainContext)
    const navigate = useNavigate()

    const handleClick = () => {
        setCheckIn('')
        setCheckOut('')
        setDates([])
        navigate('/')
    }

    return (
        <Succeed
            title={'Reserva efetuada com sucesso'} 
            handleClick={handleClick}
        />
    )
}

export default ReservationSucced