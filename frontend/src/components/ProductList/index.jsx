import styles from './styles.module.css'
import Card from "../Card";
import { useContext, useEffect } from "react";
import { MainContext } from '../../contexts/MainContext';

const ProductList = (props) => {
    
    const {title} = props
    const { productsList, getProductsList } = useContext(MainContext)

    useEffect(() => {
        getProductsList()
    },[])

    return( 
        <div className={styles.recomendationsContainer} onClick={()=>getProductsList()}>
            <h2 className={styles.recomendationsTitle} onClick={()=>getProductsList()}>{title}</h2>
            <div className={styles.recomendations}>
                {
                    productsList.map((hotel) => (
                         <li key={hotel.productSku}>
                           <Card  
                                product={hotel}
                             /> 
                         </li>
                    ))
                }
            </div>
            <div className={styles.space}></div>
        </div> 
    )
}

export default ProductList