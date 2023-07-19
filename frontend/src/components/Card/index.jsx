import { useContext } from 'react';
import styles from './styles.module.css'
import { useNavigate } from "react-router-dom";
import { MainContext } from '../../contexts/MainContext';
import api from "../../services";

const Card = (props) => {
    const { product } = props
    const { productTitle, productDetails , productAddress, images, category, productSku, productLatitude, productLongitude } = product
    const { getProductImages, setLat, setLon } = useContext(MainContext)
    const image = images.map(teste=>teste.imageUrl)
    const navigate = useNavigate()

    const handleClick = () => {
        setLat(productLatitude)
        setLon(productLongitude)
        getProductImages(productSku)
        navigate(`/product/${productSku}`)
    }

    return (
        <div className={styles.cardContainer}>
            <div className={styles.imageContainer}>
                <img src={image[0]} alt={productTitle} className={styles.img}/>
            </div>
            <div className={styles.content}>
                <h4 className={styles.cardCategory}>{category.kind}</h4>
                <h2 className={styles.recomendationsCardTitle}>{productTitle}</h2>
                <h4 className={styles.cardLocation}>{productAddress}</h4>
                <p className={styles.p} >{productDetails}</p>
                <button className={styles.button} onClick={handleClick}>Detalhes</button>
            </div>
        </div>
    )
}

export default Card