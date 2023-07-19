import { useContext, useEffect } from 'react';
import { MainContext } from '../../contexts/MainContext';

import styles from './styles.module.css';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';

const SelectInputCity = props => {
    const { onChange } = props;

    const { cities, getCities } = useContext(MainContext);

    useEffect(() => {
        getCities();
    }, []);

    return (
        <Box
            component="form"
            noValidate
            autoComplete="off"
            className={styles.textFieldInput}
        >
            <TextField
                sx={{ width: '100%' }}
                select
                label="Cidades"
                onChange={onChange}
                controlled
                className={styles.teste}
            >
                {cities.map(data => (
                    <MenuItem value={data} key={data.citySku}>
                        {data.cityDenomination}
                    </MenuItem>
                ))}
            </TextField>
        </Box>
    );
};

export default SelectInputCity;
