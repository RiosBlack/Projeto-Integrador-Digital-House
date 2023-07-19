import styles from './styles.module.css'
import ProductHeader from "../../components/ProductHeader";
import BookingDetails from "../../components/BookingDetails";
import BookingDetailsUserForm from "../../components/BookingDetailsUserForm";
import BookingDetailsArriveTime from "../../components/BookingDetailsArriveTime";
import BookingCalendar from "../../components/BookingCalendar";
import ProductDetailsPolicies from "../../components/ProductPolicies";
import { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { MainContext } from '../../contexts/MainContext';
import Loading from '../../components/Loading';

const ProductBooking = () => {

    const { setBookingPath, productDetails, loading } = useContext(MainContext)
    const navigate = useNavigate()

    const checkLogin = () => {
        if(!localStorage.getItem('@project_user_sku')){
            setBookingPath('/product/:sku/booking')
            navigate('/login')
        } else {
            setBookingPath('')
        }
    }

    useEffect(() => {
        checkLogin()
        scrollTo(0,0)
    },[])

    return(
        <>
        { loading 
            ? 
                <Loading />
            :
            (
                <div className={styles.space}>
                    <div className={styles.productBookingContainer}>
                        <ProductHeader 
                            title={productDetails.productTitle}
                        />
                        <h1>Complete seus dados</h1>
                        <div className={styles.productBookingDetailsContainer}>
                            <div className={styles.productBookingData}>
                                <BookingDetailsUserForm />
                                <BookingCalendar />
                                <BookingDetailsArriveTime />
                            </div>
                            <div className={styles.productBookingDetails}>
                                <BookingDetails />
                            </div>
                        </div>
                    </div>
                    <ProductDetailsPolicies />
                </div>
            )
        }
        
        </>
    )
}

export default ProductBooking