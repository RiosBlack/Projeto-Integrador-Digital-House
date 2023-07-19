import ImageGallery from "react-image-gallery";
import "react-image-gallery/styles/css/image-gallery.css";
import styles from "./styles.module.css";
import { useContext, useEffect, useState } from "react";
import './MyCss.css'
import { MainContext } from "../../contexts/MainContext";

const ImageGallerySlider = (props) => {
    const { showThumbnails,showPlayButton,autoPlay,slideDuration,
            showNav,showFullscreenButton,slideInterval,showIndex,showBullets} = props

    const [imagesArray, setImagesArray] = useState([])
    const { productDetails } = useContext(MainContext)

    const transformImages = () => {
        let imagesObject = []
        productDetails.images.map((image) => {
            imagesObject.push({
                original: image.imageUrl,
                thumbnail: image.imageUrl
            })})
        setImagesArray(imagesObject)
    }

    useEffect(() => {
        transformImages()
    },[])

    return (
        <div className={`${styles.imageGalleryContainer} custom-image-gallery`}>
            <ImageGallery 
                items={imagesArray} 
                showThumbnails={showThumbnails}  
                showPlayButton={showPlayButton}
                autoPlay={autoPlay}
                slideDuration={slideDuration}
                showNav={showNav}
                showFullscreenButton={showFullscreenButton}
                slideInterval={slideInterval}
                showIndex={showIndex}                
                showBullets={showBullets}
            />
    </div>
    )
}

export default ImageGallerySlider