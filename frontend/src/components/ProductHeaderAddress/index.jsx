import styles from './styles.module.css'
import { SiGooglemaps } from 'react-icons/si';

const ProductHeaderAddress = (props) => {
    const {address} = props
    return (
        <section className={styles.locationAndRate}>
            <div className={styles.location}>
                <p>
                    {' '}
                    <SiGooglemaps className={styles.locationIcon} />
                    Endereço: {address}{' '}
                </p>
            </div>
        </section>
    );
}

export default ProductHeaderAddress