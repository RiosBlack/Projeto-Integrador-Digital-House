import styles from './styles.module.css'
import Header from '../../components/Header'
import Footer from '../../components/Footer'
import { MainContext } from "../../contexts/MainContext"
import { useContext } from "react";
const MainTemplate = ({children}) => {

    const { path } = useContext(MainContext)
    return(
        <div className={styles.templateContainer}>
            <Header />
            <main className={styles.templateMain} style={{background:`${path == '/login' || path == '/register' ? '#f6f6f6' : ''}`}}>
                {children}
            </main>    
            <Footer />        
        </div>)
}

export default MainTemplate