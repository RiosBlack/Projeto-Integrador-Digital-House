import { useContext } from 'react';
import { useNavigate } from "react-router-dom";
import { MainContext } from "../../contexts/MainContext";
import Succeed from "../../components/Succeed";

const ProductPostSucceed = () => {

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
            title={'Produto criado com sucesso'}
            handleClick={handleClick}
        />
    )
}

export default ProductPostSucceed