import styles from './styles.module.css'

const CategoryCard = (props) => {
    const {img, title, onClick} = props

    return (
        <div className={styles.categoryCardContainer} onClick={onClick}>
            <img src={img} alt={title} className={styles.img}/>
            <h3 className={styles.h3}>{title}</h3>
        </div>
    )
}

export default CategoryCard