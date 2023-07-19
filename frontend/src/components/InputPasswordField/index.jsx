import styles from "./styles.module.css";
import FormControl from '@mui/material/FormControl';
import FormHelperText from "@mui/material/FormHelperText";
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import { useContext, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import { MainContext } from "../../contexts/MainContext";

const InputPasswordField = (props) => {

    const { label, onChange, validateInput, error, value, errMessage } = props
    const { windowSize } = useContext(MainContext)
    const { password, setErrPassword } = useContext(AuthContext)
    const [showPassword, setShowPassword] = useState(false);
    const validatePassword = () => { password.length >= 6 ? setErrPassword(false) : setErrPassword(true)}

    const handleClickShowPassword = () => setShowPassword((show) => !show);

    const handleMouseDownPassword = (e) => {
      e.preventDefault();
    };

    return(
        <FormControl 
            sx={ windowSize[0] > 900 ? { margin:'3em 0em 1em'} : {margin:'1em 0 1em'}} 
            variant="outlined" 
            required 
            error={error}
            className={styles.formControlInputPasswordField}
        >
            <InputLabel htmlFor="outlined-adornment-password">{label}</InputLabel>
            <OutlinedInput
            sx={{backgroundColor:'white', boxShadow:'2px 2px 3px black'}}
            id="outlined-adornment-password"
            type={showPassword ? 'text' : 'password'}
            endAdornment={
                <InputAdornment position="end">
                <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowPassword}
                    onMouseDown={handleMouseDownPassword}
                    edge="end"
                >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                </IconButton>
                </InputAdornment>
            }
            onChange={onChange}
            onBlur={validateInput}
            value={value}
            label={label}
            />
            {error ? (<FormHelperText sx={{color:'red', margin:'0.2em 0 0'}}>{errMessage}</FormHelperText>) : null}
        </FormControl>
    )
}

export default InputPasswordField