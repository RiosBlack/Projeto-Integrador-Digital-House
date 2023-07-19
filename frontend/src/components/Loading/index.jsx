import * as React from 'react';
import styles from './styles.module.css'
import CircularProgress from '@mui/material/CircularProgress';

export default function Loading() {
  return (
    <div className={styles.loading}>
      <CircularProgress sx={{color: '#DAAA2C'}}/>
      <h1>Carregando...</h1>
    </div>
  );
}