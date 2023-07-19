import styles from './styles.module.css';
import SearchBar from '../../components/SearchBar';
import Category from '../../components/Category';
import { useEffect, useContext } from 'react';
import { MainContext } from '../../contexts/MainContext';
import ProductList from '../../components/ProductList';
import { AuthContext } from '../../contexts/AuthContext';
import Loading from '../../components/Loading';

const Home = () => {
    const {
        setPath,
        setBookingPath,
        loading,
        setLoading,
        category,
        getMyReservations,
    } = useContext(MainContext);
    const { displayUserInitials } = useContext(AuthContext);

    useEffect(() => {
        getMyReservations(localStorage.getItem('@project_user_sku'));
        setLoading(true);
        setPath('/');
        setBookingPath('');
        displayUserInitials();
        setTimeout(() => {
            setLoading(false);
        }, 1000);
    }, []);

    return (
        <>
            {' '}
            {loading ? (
                <Loading />
            ) : (
                <div className={styles.homeContainer}>
                    <SearchBar />
                    <Category />
                    <ProductList
                        title={
                            category
                                ? `Acomodações incríveis | ${category}`
                                : 'Acomodações incríveis'
                        }
                    />
                </div>
            )}
        </>
    );
};

export default Home;
