import CategoryCard from "../CategoryCard";
import styles from "./styles.module.css";
import { useEffect, useContext } from "react";
import { MainContext } from "../../contexts/MainContext";

const Category = () => {

    const { categories, getCategories, getProductsList } = useContext(MainContext)

    useEffect(() => {
        getCategories()
    },[])

    const handleClick = async (categoria) => {
        getProductsList(categoria)
    }

    return(
        <div className={styles.categoriesContainer}>
            <h2 className={styles.categorySearchTitle}>Buscar por categoria</h2>
            <div className={styles.categoryCards}>
                {
                    categories.map((category) => (
                        <CategoryCard key={category.sku} title={category.kind} img={category.imageUrl} onClick={()=>handleClick(category.kind)}/>
                    ))
                }
            </div>
        </div>)
}

export default Category