import { useContext } from "react";
import styles from "./styles.module.css";
import { IoArrowBackCircleOutline } from "react-icons/io5";
import { Link } from "react-router-dom";
import { MainContext } from "../../contexts/MainContext";

const ProductHeader = (props) => {
    const {category, title} = props
    const { setCheckIn, setCheckOut, setDates} = useContext(MainContext)
    const handleClick = () => {
        setCheckIn('')
        setCheckOut('')
        setDates([])
    }
    return (
        <div>
            <section className={styles.productDetailsHeader}>
                <div className={styles.productDetailsHeaderTitles}>
                    <h4>{category}</h4>
                    <h2>{title}</h2>
                </div>
                <div className={styles.returnIcon}>
                   <Link to='/home' onClick={handleClick}><IoArrowBackCircleOutline size={'3em'} color="#FFF"/></Link>
                </div>
            </section>
        </div>
    )    
}

export default ProductHeader

