import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AuthProvider from './contexts/AuthContext';
import MainProvider from './contexts/MainContext';
import NewProductProvider from './contexts/NewProductContext';
import MainTemplate from './templates/main-template';
import Login from './Routes/Login';
import Home from './Routes/Home';
import Register from './Routes/Register';
import ProductDetails from './Routes/ProductDetails';
import ReservationSucceed from './Routes/ReservationSucceed';
import ProductPostSucceed from './Routes/ProductPostSucceed';
import ProductBooking from './Routes/ProductBooking';
import ProductRegister from './Routes/ProductRegister';
import Reservations from './Routes/Reservations';
import ProductRegisterSucceed from './Routes/ProductRegisterSucceed';
import VLibras from '@djpfs/react-vlibras';

const App = () => {
    return (
        <AuthProvider>
            <MainProvider>
                <VLibras />
                <NewProductProvider>
                    <BrowserRouter>
                        <MainTemplate>
                            <Routes>
                                <Route path="/" element={<Home />} />
                                <Route path="/home" element={<Home />} />
                                <Route path="/login" element={<Login />} />
                                <Route
                                    path="/register"
                                    element={<Register />}
                                />
                                <Route
                                    path="/product/:sku"
                                    element={<ProductDetails />}
                                />
                                <Route
                                    path="/product/:sku/booking"
                                    element={<ProductBooking />}
                                />
                                <Route
                                    path="/admin/dashboard/new_product"
                                    element={<ProductRegister />}
                                />
                                <Route
                                    path="/admin/dashboard/new_product/ReservationProductRegisterSucced"
                                    element={<ProductRegisterSucceed />}
                                />
                                <Route
                                    path="admin/dashboard/new_product/createSucceed"
                                    element={<ProductPostSucceed />}
                                />
                                <Route
                                    path="/my_bookings"
                                    element={<Reservations />}
                                />
                                <Route
                                    path="product/:sku/succeed"
                                    element={<ReservationSucceed />}
                                />
                            </Routes>
                        </MainTemplate>
                    </BrowserRouter>
                </NewProductProvider>
            </MainProvider>
        </AuthProvider>
    );
};

export default App;
