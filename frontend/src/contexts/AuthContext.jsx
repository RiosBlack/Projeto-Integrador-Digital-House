import { createContext, useState } from "react";

export const AuthContext = createContext({})

const AuthProvider = ({children}) => {
    const [initials, setInitials] = useState('')
    const [email, setEmail] = useState('')
    const [errEmail, setErrEmail] = useState(false)
    const [password, setPassword] = useState('')
    const [errPassword, setErrPassword] = useState(false)
    const [name, setName] = useState('')
    const [errName, setErrName] = useState(false)
    const [surname, setSurname] = useState('')
    const [errSurname, setErrSurname] = useState(false)
    const [emailTwin, setEmailTwin] = useState('')
    const [errEmailTwin, setErrEmailTwin] = useState(false)
    const [userData, setUserData] = useState([])

    const saveToken = (token) => localStorage.setItem('@project_token', token)
    
    const displayUserInitials = () => {
        let initialsLetter = ''
        if(localStorage.getItem('@project_all_name')){
            const names = localStorage.getItem('@project_all_name').split(' ')
            names.forEach(name => {
                initialsLetter+=name.slice(0,1).toUpperCase()
            });
        }
        setInitials(initialsLetter)
    }

    const onChangeEmail = (e) => {
        setEmail(e.target.value)
        setErrEmail(false)
    }   
    
    const onChangePassword = (e) => {
        setPassword(e.target.value)
        setErrPassword(false)
    }  

    const onChangeName = (e) => {
        setName(e.target.value)
        setErrName(false)
    }  

    const onChangeSurname = (e) => {
        setSurname(e.target.value)
        setErrSurname(false)
    }  

    const onChangeEmailTwin = (e) => {
        setEmailTwin(e.target.value)
        setErrEmailTwin(false)
    } 

    const logout = () => {
        localStorage.clear()
        setInitials('')
    }

    const clearForm = () => {
        setEmail('') 
        setErrEmail(false)
        setPassword('')
        setErrPassword(false)
        setName('')
        setErrName(false)
        setEmailTwin('')
        setErrEmailTwin(false)
        setSurname('')  
        setErrSurname(false)  
    }

    return (
        <AuthContext.Provider value={{initials, saveToken, displayUserInitials, logout, 
            email, setEmail, onChangeEmail, errEmail, setErrEmail,
            emailTwin, setEmailTwin, onChangeEmailTwin, errEmailTwin, setErrEmailTwin, 
            password, setPassword, errPassword, setErrPassword, onChangePassword, 
            name, setName, onChangeName, errName, setErrName,
            surname, setSurname, onChangeSurname, errSurname, setErrSurname,
            clearForm, userData, setUserData}}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthProvider