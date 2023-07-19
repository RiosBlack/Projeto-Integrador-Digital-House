import styles from "./styles.module.css";
import { MainContext } from '../../contexts/MainContext';
import { useContext } from 'react';

const ProductDetailsPolicies = () => {
    const { productsPoliticsDetails } = useContext(MainContext);

    return (
        <div className={styles.politiciesContainer}>
            <h2 className={styles.policiesTitle}>O que você precisa saber</h2>
            <hr />
            <div className={styles.descriptionPolicies}>
                <div className={styles.policy}>
                    <h3>Regras do Hotel</h3>
                    <ul className={styles.rules}>
                        {productsPoliticsDetails.map(data => (
                            <div key={data.policySku}>
                                <li className={styles.politics}>
                                    {data.policyHotel1}
                                </li>
                                <li className={styles.politics}>
                                    {data.policyHotel2}
                                </li>
                                <li>{data.policyHotel3}</li>
                            </div>
                        ))}
                    </ul>
                </div>
                <div className={styles.policy}>
                    <h3>Saúde e segurança</h3>
                    <ul className={styles.safetyAndHealth}>
                        <li>
                            <p>
                                Diretrizes de distanciamento social e outras
                                regulamentções relacionadas ao coronavírus se
                                aplicam
                            </p>
                        </li>
                        <li>
                            <p>Detector de fumaça</p>
                        </li>
                        <li>
                            <p>Cofre de segurança</p>
                        </li>
                    </ul>
                </div>
                <div className={styles.policy}>
                    <h3>Cancelamento</h3>
                    <ul className={styles.cancelAndRefund}>
                        <li>
                            <p>
                                Adicione as datas de cancelamento para obter
                                detalhes de cancelamento para esta estadia
                            </p>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default  ProductDetailsPolicies