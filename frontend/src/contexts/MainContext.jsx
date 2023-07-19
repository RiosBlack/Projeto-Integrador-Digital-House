import { createContext, useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import api from '../services';

export const MainContext = createContext({});

const MainProvider = ({ children }) => {
    const [path, setPath] = useState('/');
    const [bookingPath, setBookingPath] = useState('');
    const [open, setOpen] = useState(false);
    const [windowSize, setWindowSize] = useState([
        window.innerWidth,
        window.innerHeight,
    ]);
    const [checkIn, setCheckIn] = useState([]);
    const [checkOut, setCheckOut] = useState([]);
    const [cities, setCities] = useState([]);
    const [userCity, setUserCity] = useState('');
    const [errCity, setErrCity] = useState(false);
    const [dates, setDates] = useState([]);
    const [hour, setHour] = useState('');
    const [productsList, setProductsList] = useState([]);
    const [productImages, setProductImages] = useState([]);
    const [products, setProducts] = useState([]);
    const [productDetails, setProductDetails] = useState([]);
    const [productsFetuaresDetails, setProductsFeaturesDetails] = useState([]);
    const [productsPoliticsDetails, setProductsPoliticsDetails] = useState([]);
    const [category, setCategory] = useState('');
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [lat, setLat] = useState('')
    const [lon, setLon] = useState('')
    const [myReservations, setMyReservations] = useState([])

    useEffect(() => {
        const handleWindowResize = () => {
            setWindowSize([window.innerWidth, window.innerHeight]);
        };
        window.addEventListener('resize', handleWindowResize);
        return () => {
            window.removeEventListener('resize', handleWindowResize);
        };
    }, []);

    const displayErrorMessage = (message, type) => {
        type === 'error' 
        ?
        toast.error(message, {
          position: "top-center",
          autoClose: 4000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "colored",
        })
        :
        toast.success(message, {
            position: "top-center",
            autoClose: 4000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: 'colored',
        });
    };

    const onChangeCity = e => {
        setUserCity(e.target.value);
        setErrCity(false);
    };

    const getProductsList = async (categoria = null) => {
        try {
            if (!categoria) {
                const { data } = await api.get('/api/products');
                setProducts(data);
                setProductsList(data);
                setCategory('');
            } else {
                setProductsList(
                    products.filter(
                        product => product.category.kind === categoria
                    )
                );
                setCategory(categoria);
            }
        } catch (e) {
            console.log(e);
        }
    };

    const getProductsByCity = async (city) => {
        try {
            const { data } = await api.get(`/api/products/cities/${city}`);
            setProductsList(data);
            setCategory('');
        } catch (e) {
            console.log(e);
        }
    }

    const getProductsByDates = async (checkIn, checkOut) => {
        try {
            const { data } = await api.get(`/api/products/dates/${checkIn}/${checkOut}`);
            setProductsList(data);
            setCategory('');
        } catch (e) {
            console.log(e);
        }
    }

    const getProductsByDatesAndCity = async (checkIn, checkOut, city) => {
        try {
            const { data } = await api.get(`/api/products/city/${city}/${checkIn}/${checkOut}`);
            setProductsList(data);
            setCategory('');
        } catch (e) {
            console.log(e);
        }
    }

    const getProductDetails = async sku => {
        try {
            const { data } = await api.get(`/api/products?productSku=${sku}`);
            setProductDetails(data);
        } catch (e) {
            console.log(`Não foi possível completar a requisição: ${e}`);
        }
    };

    const getCategories = async () => {
        try {
            const { data } = await api.get('/api/categories');
            setCategories(data);
        } catch (e) {
            console.log(e);
        }
    };

    const getCities = async () => {
        try {
            const { data } = await api.get('/api/cities');
            setCities(data);
        } catch (e) {
            console.log(e);
        }
    };

    const clearBookingDetailsFields = () => {
        setCheckIn('');
        setCheckOut('');
        setHour('');
        setUserCity('');
    };

    const getProductImages = async sku => {
        const { data } = await api.get(`/api/products?productSku=${sku}`);
        const images = await data.images.map(image => image.imageUrl);
        setProductImages(images);
        setProductsFeaturesDetails(data.category.features);
        setProductsPoliticsDetails(data.policies);
    };

    const getMyReservations = async (userSku) => {
        try {
            if(userSku){
            const { data } = await api.get(`api/bookings?userSku=${userSku}`)
            setMyReservations(data)}
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <MainContext.Provider
            value={{
                path,
                setPath,
                open,
                setOpen,
                windowSize,
                displayErrorMessage,
                checkIn,
                setCheckIn,
                checkOut,
                setCheckOut,
                bookingPath,
                setBookingPath,
                dates,
                setDates,
                userCity,
                setUserCity,
                errCity,
                setErrCity,
                onChangeCity,
                hour,
                setHour,
                cities,
                setCities,
                productsList,
                setProductsList,
                getProductsList,
                categories,
                setCategories,
                getCategories,
                productDetails,
                setProductDetails,
                getProductDetails,
                clearBookingDetailsFields,
                category,
                setCategory,
                productImages,
                setProductImages,
                getProductImages,
                loading,
                setLoading,
                productsFetuaresDetails,
                productsPoliticsDetails,
                getProductsByCity,
                getProductsByDates,
                getProductsByDatesAndCity,
                lon,
                setLon,
                lat,
                setLat,
                myReservations,
                setMyReservations,
                getMyReservations,
                getCities
            }}
        >
            {children}
        </MainContext.Provider>
    );
};

export default MainProvider;