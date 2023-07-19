import styles from "./styles.module.css";
import { useContext } from 'react';
import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Fade from '@mui/material/Fade';
import Typography from '@mui/material/Typography';
import ImageGallerySlider from "../ImageGallerySlider";
import { MainContext } from '../../contexts/MainContext';
import { AiOutlineClose } from "react-icons/ai";

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '70%',
  height: '70%',
  bgcolor: 'background.paper',
  borderRadius: '20px',
  boxShadow: 24,
  // p: 4,
};

const ProductDetailsModal = () => {
  const {open, setOpen} = useContext(MainContext)

  return (
    <div>
      <Modal
        aria-labelledby="transition-modal-title"
        aria-describedby="transition-modal-description"
        open={open}
        onClose={() => setOpen(false)}
        closeAfterTransition
        slots={{ backdrop: Backdrop }}
        slotProps={{
          backdrop: {
            timeout: 500,
          },
        }}
      >
        <Fade in={open}>
          <Box sx={style}>
            <AiOutlineClose onClick={() => setOpen(false)} className={styles.closeModal} size={30}/>
            <ImageGallerySlider 
              showThumbnails={true}  
              showPlayButton={false}
              autoPlay={false}
              slideDuration={400}
              showNav={true}
              showFullscreenButton={false}
              slideInterval={2000}
              showIndex={true}
              showBullets={true}
            />
          </Box>
        </Fade>
      </Modal>
    </div>
  );
}

export default ProductDetailsModal