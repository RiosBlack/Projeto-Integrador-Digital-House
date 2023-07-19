import { useNavigate } from 'react-router-dom';
import Succeed from '../../components/Succeed';

const ProductRegisterSucceed = () => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/');
    };

    return (
        <Succeed
            title={'Produto cadastrado com sucesso'}
            handleClick={handleClick}
        />
    );
};

export default ProductRegisterSucceed;
