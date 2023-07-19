import styles from "./styles.module.css";

const Description = (props) => {
    const { description } = props;
    return (
        <div className={styles.descriptionContainer}>
            <h2>Descrição</h2>
            <p> {description} </p>
        </div>
    )
}

export default Description