import styles from './styles.module.css'
import TextField from '@mui/material/TextField';
import FormHelperText from '@mui/material/FormHelperText';
import { useContext } from 'react';
import { MainContext } from '../../contexts/MainContext';

const InputField = (props) => {
  const { label, onChange, validateInput, error, errMessage, value, disabled, multiline, rows } = props
  const { path } = useContext(MainContext) 

    return (
        <div
            className={
                path == '/login' || path == '/register'
                    ? styles.inputFieldContainer
                    : styles.inputFieldBooking
            }
        >
            <div className={styles.inputFieldContainer}>
                <TextField
                    required
                    id="outlined-basic"
                    label={label}
                    onChange={onChange}
                    onBlur={validateInput}
                    error={error}
                    value={value}
                    className={styles.textFieldInput}
                    disabled={disabled}
                    multiline={multiline ? multiline : false}
                    rows={rows ? rows : ''}
                />
                {error ? (
                    <FormHelperText sx={{ color: 'red' }}>
                        {errMessage}
                    </FormHelperText>
                ) : null}
            </div>
        </div>
    );
}
export default InputField