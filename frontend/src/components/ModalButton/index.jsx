import { useState, useContext } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import InputField from '../../components/InputField';
import api from '../../services';
import { MainContext } from '../../contexts/MainContext';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


export default function ModalButton() {
    const [open, setOpen] = useState(false);
    const [city, setCity] = useState('')
    const [nations, setNations] = useState('')
    const { displayErrorMessage, getCities } = useContext(MainContext);
    const headers = {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: localStorage.getItem('@project_token'),
    };

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        p: 4,
        marginRight: 2,
    };

    const button = {
        position: 'relative',
        zIndex: 0,
        color: '#DAAA2C',
        backgroundColor: '#012B28',
    };

    const theme = createTheme({
        palette: {
            primary: {
                main: '#012B28',
            },
        },
    });

    const cityData = {
        cityDenomination: city,
        cityCountry: nations
    }

    const notify = () => {
        toast.success('Cidade cadastrada com sucesso !', {
            position: toast.POSITION.TOP_CENTER,
        });
    }

    const handleSubmit = async () => {
        await api.post('api/cities', cityData ,{ headers })
        .then( async response => {
        if(city === null || city === ''){
            return alert('O campo cidade não pode ser vazio.')
        }
        if (nations === null || nations === '') {
            return alert('O campo cidade não pode ser vazio.');
        }
        if(response.status === 200 || response.status === 201){
            setOpen(!open)
            getCities()
            notify()
            console.log('cidade cadastrada');
        }})
        .catch(error => {
        console.log(error)
        displayErrorMessage('Erro ao cadastrar nova cidade. Tente novamente!', 'error')
        })
    };

    return (
        <ThemeProvider theme={theme}>
            <ToastContainer />
            <Fab
                onClick={() => setOpen(!open)}
                size="small"
                color="primary"
                aria-label="add"
                sx={button}
            >
                <AddIcon />
            </Fab>
            <Modal
                open={open}
                onClose={() => setOpen(!open)}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography
                        id="modal-modal-title"
                        variant="h6"
                        component="h2"
                    >
                        <h3>Adicionar Cidade:</h3>
                    </Typography>
                    <div>
                        <InputField
                            label={'Cidade'}
                            onChange={e => setCity(e.target.value)}
                        />
                        <InputField
                            label={'Pais'}
                            onChange={e => setNations(e.target.value)}
                        />
                    </div>
                    <button onClick={handleSubmit}>Cadastrar</button>
                </Box>
            </Modal>
        </ThemeProvider>
    );
}
