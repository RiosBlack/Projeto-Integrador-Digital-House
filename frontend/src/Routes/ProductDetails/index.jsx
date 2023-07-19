import styles from './styles.module.css';
import ProductHeader from '../../components/ProductHeader';
import ProductHeaderAddress from '../../components/ProductHeaderAddress';
import Description from '../../components/Description';
import ImageBoard from '../../components/ImageBoard';
import Maps from '../../components/Maps';
import ProductDetailsFeatures from '../../components/ProductDetailsFeatures';
import ProductDetailsPolicies from '../../components/ProductPolicies';
import BookingStart from '../../components/BookingStart';
import { useEffect, useContext, useState } from 'react';
import { AuthContext } from '../../contexts/AuthContext';
import { useParams } from 'react-router-dom';
import { MainContext } from '../../contexts/MainContext';
import api from '../../services';
import PriceDetails from '../../components/PriceDetails';
import Loading from '../../components/Loading';

const ProductDetails = () => {
    const { displayUserInitials } = useContext(AuthContext);
    const [loading, setLoading] = useState(true)
    const {
        productDetails,
        setProductDetails,
        productImages,
        getProductImages,
        lat,
        lon,
    } = useContext(MainContext);

    const iconSize = '1.5em';
    const { sku } = useParams()

    const getProductDetails = async sku => {
        try {
            const { data } = await api.get(`/api/products?productSku=${sku}`);
            await setProductDetails(data);
            console.log(productDetails);
        } catch (e) {
            console.log(e);
        }
    };

    useEffect(() => {
        getProductImages(sku);
        getProductDetails(sku);
        displayUserInitials();
        scrollTo(0, 0);
        setTimeout(() => {
            setLoading(false);
        }, 1000);
    }, []);

    return (
        <>
            {loading ? (
                <Loading />
            ) : (
                <div className={styles.productDetailsContainer}>
                    <ProductHeader title={productDetails.productTitle} />
                    {productDetails.length !== 0 ? (
                        <ImageBoard images={productImages} />
                    ) : null}
                    <PriceDetails price={productDetails.productPrice} />
                    <Description description={productDetails.productDetails} />
                    <ProductHeaderAddress
                        address={productDetails.productAddress}
                    />
                    <Maps
                        latitude={
                            /^-/.test(lat)
                                ? parseFloat(lat.replace('-', '')) * -1
                                : parseFloat(lat)
                        }
                        longitude={
                            /^-/.test(lon)
                                ? parseFloat(lon.replace('-', '')) * -1
                                : parseFloat(lon)
                        }
                    />
                    <ProductDetailsFeatures />
                    <ProductDetailsPolicies />
                    <BookingStart />
                    <div className={styles.space}></div>
                </div>
            )}
        </>
    );
};

export default ProductDetails;
