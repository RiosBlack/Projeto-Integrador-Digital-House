import { HiBadgeCheck } from "react-icons/hi";
import styles from "./styles.module.css";

const Succeed = (props) => {
    const {title, handleClick} = props
    return (
        <div className={styles.succeedContainer}>
            <HiBadgeCheck size={100} className={styles.confirmationIcon}/>
            <h2>Tudo certo!</h2>
            <h3>{title}</h3>
            <button onClick={handleClick} className={styles.succeedButton}>Ok</button>
        </div>
    )
}

export default Succeed