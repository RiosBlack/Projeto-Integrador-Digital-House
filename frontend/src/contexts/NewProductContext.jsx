import { createContext, useState } from "react";

export const NewProductContext = createContext({})

const NewProductProvider = ({children}) => {
    const [title, setTitle] = useState('')
    const [category, setCategory] = useState('')
    const [address, setAddress] = useState('')
    const [city, setCity] = useState('')
    const [details, setDetails] = useState('')
    const [images, setImages] = useState([])

    return(
        <NewProductContext.Provider value={{ title, setTitle, category, setCategory, address, setAddress,
        city, setCity, details, setDetails, images, setImages}}>
            {children}
        </NewProductContext.Provider>
    )

}

export default NewProductProvider