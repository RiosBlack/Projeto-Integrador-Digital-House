import styles from './styles.module.css';
import { FaRegMoneyBillAlt } from 'react-icons/fa';

const PriceDetails = props => {
    const { price } = props;
    return (
        <section className={styles.locationAndRate}>
            <div className={styles.location}>
                <p>
                    {' '}
                    <FaRegMoneyBillAlt className={styles.locationIcon} />
                    Preço reservando hoje: R$ {price} dia
                </p>
            </div>
        </section>
    );
};

export default PriceDetails;
