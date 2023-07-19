import styles from './styles.module.css';
import { AiFillCaretRight } from 'react-icons/ai';
import { MainContext } from '../../contexts/MainContext';
import { useContext } from 'react';

const ProductDetailsFeatures = props => {
    const { productsFetuaresDetails } = useContext(MainContext);

    return (
        <div className={styles.featuresContainer}>
            <h2>Características</h2>
            <hr />
            <ul>
                {productsFetuaresDetails.map(data => (
                    <li className={styles.feature} key={data.featureSku}>
                        <AiFillCaretRight color="#1dbeb4" />
                        <p>{data.featureTitle}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProductDetailsFeatures;
