import { useContext, useEffect} from 'react';
import FormControl from '@mui/material/FormControl';
import { MainContext } from '../../contexts/MainContext';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import Box from '@mui/material/Box';
import styles from './styles.module.css';

const SelectInputCategories = props => {
    const { onChange } = props;
    const { categories } = useContext(MainContext);


    return (
        <Box
            component="form"
            noValidate
            autoComplete="off"
            className={styles.textFieldInput}
        >
            <FormControl fullWidth>
                <TextField
                    sx={{ width: '100%' }}
                    select
                    label="Categorias"
                    onChange={onChange}
                    className={styles.map}
                >
                    {categories.map(data => (
                        <MenuItem value={data.kind} key={data.sku}>
                            {data.kind}
                        </MenuItem>
                    ))}
                </TextField>
            </FormControl>
        </Box>
    );
};

export default SelectInputCategories;
