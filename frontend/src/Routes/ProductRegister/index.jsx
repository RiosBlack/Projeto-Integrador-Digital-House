import styles from './styles.module.css';
import ProductHeader from '../../components/ProductHeader';
import InputField from '../../components/InputField';
import InputFieldPrice from '../../components/InputFieldPrice';
import SelectInputCity from '../../components/SelectInputCity';
import SelectInputCategories from '../../components/SelectInputCategories';
import { useState, useContext, useEffect } from 'react';
import { MainContext } from '../../contexts/MainContext';
import { MuiFileInput } from 'mui-file-input';
import { AiFillCaretRight } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import ModalButton from '../../components/ModalButton';
import api from '../../services';
import Loading from "../../components/Loading";

const ProductRegister = () => {
    const { categories, displayErrorMessage, loading, setLoading } = useContext(MainContext);
    const [title, setTitle] = useState('');
    const [price, setPrice] = useState('');
    const [address, setAddress] = useState('');
    const [description, setDescription] = useState('');
    const [citySelect, setCitySelect] = useState([]);
    const [politic1, setPolitic1] = useState('');
    const [politic2, setPolitic2] = useState('');
    const [politic3, setPolitic3] = useState('');
    const [LatSelect, setLatSelect] = useState('');
    const [LonSelect, setLonSelect] = useState('');
    const [feature, setFeature] = useState([]);
    const [valueUpload, setValueUpload] = useState(null);
    const [categorySku, setCategorySku] = useState();
    const [errValidTitle, setErrValidTitle] = useState(false);
    const validTitleInput = () => {
        title.length <= 2 ? setErrValidTitle(true) : setErrValidTitle(false);
    };
    const [errValidPass, setErrValidPass] = useState(false);
    const validPassInput = () => {
        price.length <= 1 ? setErrValidPass(true) : setErrValidPass(false);
    };
    const [errValidAddre, setErrValidAddre] = useState(false);
    const validPassAddre = () => {
        address.length <= 5 ? setErrValidAddre(true) : setErrValidAddre(false);
    };
    const [errValidDesc, setErrValidDes] = useState(false);
    const validPassDesc = () => {
        description.length <= 5 ? setErrValidDes(true) : setErrValidDes(false);
    };
    const [errValidLatLon, setErrValidLatLon] = useState(false);
    const validLatLog = () => {
        LatSelect.length <= 4 || LonSelect.length <= 4
            ? setErrValidLatLon(true)
            : setErrValidLatLon(false);
    };
    const navigate = useNavigate();
    const headersS3 = {
        'Content-Type': 'multipart/form-data',
        'Access-Control-Allow-Origin': '*',
        Authorization: localStorage.getItem('@project_token'),
    };
    const headers = {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: localStorage.getItem('@project_token'),
    };
    var listUrl = [];

    const handleChange = newValue => {
        setValueUpload(newValue);
    };

    const token = localStorage.getItem('@project_token');
    const functionUser = localStorage.getItem('@project_user_function')

    useEffect(() => {
        console.log(functionUser);
        if (token === null || token === '' || functionUser !== 'Admin') {
            navigate('/');
        }
    }, []);

    const DataProduct = {
        productTitle: title,
        productDetails: description,
        productPrice: price,
        productAddress: address,
        productLatitude: LatSelect,
        productLongitude: LonSelect,
        images: listUrl,
        policies: [
            {
                policyHotel1: politic1,
                policyHotel2: politic2,
                policyHotel3: politic3,
            },
        ],
        category: categorySku,
        city: citySelect,
    };

    var features2 = [];
    var features3 = [];

    const handleOnChange = async e => {
        try {
            var categoryFilter = await categories.filter(
                category => category.kind == e.target.value
            );
            features2 = categoryFilter[0].features;
            features3 = features2.map(data => data.featureTitle);
            setFeature(features3);
            setCategorySku(categoryFilter[0]);
        } catch (e) {
            console.log(e.message);
        }
    };

    const submitProduct = async () => {
       
        await api
            .post('api/products', DataProduct, { headers })
            .then(async response => {
                if (response.status === 200 || response.status === 201) {
                    console.log('Produto cadastrado');
                    navigate(
                        '/admin/dashboard/new_product/ReservationProductRegisterSucced'
                    );
                }
            })
            .catch(error => {
                console.log(error);
                displayErrorMessage(
                    'Erro ao cadastrar o produto. Tente novamente mais tarde!',
                    'error'
                );
            });
        setLoading(false)
    };

    const handleSubmit = async e => {
        e.preventDefault();
        setLoading(true)
        const formData = new FormData();
        valueUpload.forEach(e => {
            formData.append('file', e);
        });

        await api
            .post('/api/s3/upload', formData, { headersS3 })
            .then(response => {
                if (response.status === 200 || response.status === 201) {
                    response.data.forEach(element => {
                        const obj = {
                            imageUrl: element,
                        };
                        listUrl.push(obj);
                    });
                }
            })
            .catch(error => {
                console.log(error);
                displayErrorMessage(
                    'Erro ao enviar a imagem. Tente novamente mais tarde!',
                    'error'
                );
            });

        submitProduct();
    };

    return (
        <>
        {
            loading 
            ?
                <Loading />
            :
            (
<div className={styles.productRegisterContainer}>
            <ProductHeader title={'Área do administrador'} />
            <div className={styles.productRegisterTitle}>
                <h2>Adicionar hotel</h2>
            </div>
            <div className={styles.productRegisterFormContainer}>
                <form className={styles.productRegisterForm}>
                    <div className={styles.inputsContainer}>
                        <InputField
                            label={'Título'}
                            onChange={e => setTitle(e.target.value)}
                            errMessage="O titulo deve conter no mínimo 3 caracteres"
                            error={errValidTitle}
                            validateInput={validTitleInput}
                        />
                    </div>
                    <div className={styles.inputsContainer}>
                        <div className={styles.inputData}>
                            <InputFieldPrice
                                label={'Preço'}
                                onChange={e => setPrice(e.target.value)}
                                errMessage="O titulo deve conter no mínimo 2 caracteres"
                                error={errValidPass}
                                validateInput={validPassInput}
                            />
                        </div>
                        <div className={styles.inputCity}>
                            <SelectInputCity
                                onChange={e => setCitySelect(e.target.value)}
                            />
                            <ModalButton />
                        </div>
                    </div>
                    <div className={styles.inputsContainer}>
                        <div className={styles.inputData}>
                            <InputField
                                label={'Endereço'}
                                onChange={e => setAddress(e.target.value)}
                                errMessage="O titulo deve conter no mínimo 6 caracteres"
                                error={errValidAddre}
                                validateInput={validPassAddre}
                            />
                        </div>
                        <div className={styles.inputData}>
                            <SelectInputCategories
                                onChange={e => handleOnChange(e)}
                            />
                        </div>
                    </div>
                    <div className={styles.inputsContainer}>
                        <InputField
                            label={'Descrição'}
                            multiline={true}
                            rows={5}
                            onChange={e => setDescription(e.target.value)}
                            errMessage="O titulo deve conter no mínimo 6 caracteres"
                            error={errValidDesc}
                            validateInput={validPassDesc}
                        />
                    </div>
                    <div>
                        <h3>Localização</h3>
                        <div className={styles.localizador}>
                            <div className={styles.div}>
                                <InputField
                                    label={'Latitude'}
                                    rows={5}
                                    onChange={e => setLatSelect(e.target.value)}
                                    errMessage="O titulo deve conter no mínimo 5 caracteres"
                                    error={errValidLatLon}
                                    validateInput={validLatLog}
                                />
                            </div>
                            <div className={styles.div}>
                                <InputField
                                    label={'Longitude'}
                                    rows={5}
                                    onChange={e => setLonSelect(e.target.value)}
                                    errMessage="O titulo deve conter no mínimo 5 caracteres"
                                    error={errValidLatLon}
                                    validateInput={validLatLog}
                                />
                            </div>
                        </div>
                    </div>
                    <div>
                        <h3>Características</h3>
                        <ul>
                            {feature.map((data, index) => (
                                <li key={index}>
                                    <AiFillCaretRight color="#1dbeb4" />
                                    {data}
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div>
                        <h3>Políticas</h3>
                        <div className={styles.inputData}>
                            <div className={styles.div}>
                                <InputField
                                    label={
                                        'Digite a primeira política do hotel'
                                    }
                                    onChange={e => setPolitic1(e.target.value)}
                                />
                            </div>
                            <div className={styles.div}>
                                <InputField
                                    label={'Digite a segunda política do hotel'}
                                    onChange={e => setPolitic2(e.target.value)}
                                />
                            </div>
                            <div className={styles.div}>
                                <InputField
                                    label={
                                        'Digite a terceira política do hotel'
                                    }
                                    onChange={e => setPolitic3(e.target.value)}
                                />
                            </div>
                        </div>
                    </div>
                    <div>
                        <h3>Carregar imagens</h3>
                        <div className={styles.insertImagesContainer}>
                            <MuiFileInput
                                multiple
                                value={valueUpload}
                                onChange={handleChange}
                                placeholder="Clique ou arraste os arquivos para upload."
                            />
                        </div>
                    </div>
                    <button onClick={handleSubmit}>Cadastrar Produto</button>
                </form>
            </div>
        </div>
            )
        }
        </>
            );
};

export default ProductRegister;
