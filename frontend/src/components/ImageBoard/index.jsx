import styles from "./styles.module.css";
import { useContext, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import ImageGallerySlider from "../ImageGallerySlider";
import ProductDetailsModal from "../ProductDetailsModal";
import { MainContext } from "../../contexts/MainContext";
import './MyCustom.css';

const ImageBoard = (props) => {
    const {images} = props
    const {open, setOpen, windowSize } = useContext(MainContext)
    const { sku } = useParams()

    useEffect(() => {
        
    })
    return (
        <div className={styles.imageBoardContainer}>
            {
                windowSize[0] >= 1024 ? (
                    <div>
                        <div className={styles.imageBoardContainerDesktop}>
                            <img src={images[0]} alt="" className={styles.img1} />
                            <div className={styles.img2}>
                                <img src={images[1]} alt=""  />
                            </div>
                            <div className={styles.img3}>
                                <img src={images[2]} alt=""  />
                            </div>
                            <div className={styles.img4}>
                                <img src={images[3]} alt=""  />
                            </div>
                            <div className={styles.img5}>
                                <img src={images[4]} alt=""  />
                            </div>
                        <Link onClick={() => setOpen(true)}><p className={styles.seeMore}>Ver mais</p></Link>
                        </div>
                        <ProductDetailsModal />
                    </div> 
                ) :  
                (
                    <div className="my-custom-gallery">
                        <ImageGallerySlider 
                                showThumbnails={false}  
                                showPlayButton={false}
                                autoPlay={true}
                                slideDuration={400}
                                showNav={false}
                                showFullscreenButton={false}
                                slideInterval={2000}
                                showIndex={true}
                                showBullets={false}
                                className=''
                        />
                    </div>

                )
            }   
        </div>
    )
}

export default ImageBoard