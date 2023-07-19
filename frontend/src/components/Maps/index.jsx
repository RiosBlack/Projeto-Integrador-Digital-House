import styles from './styles.module.css';
import { GoogleMap, useJsApiLoader, Marker } from '@react-google-maps/api';

export default function Maps(props) {
    const { latitude, longitude }= props

    const center = {
        lat: latitude,
        lng: longitude,
    };

    const marker = {
        lat: latitude,
        lng: longitude,
    };

    const containerStyle = {
        width: '100%',
        height: '350px',
    };

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: 'AIzaSyAMA0pgj4g5w3zL8suM34KGYdXMwxEIXj8',
    });

    return isLoaded ? (
        <div className={styles.maps}>
            <GoogleMap
                mapContainerStyle={containerStyle}
                center={center}
                zoom={17}
            >
                <Marker
                    className={styles.marker}
                    position={marker}
                />
            </GoogleMap>
        </div>
    ) : (
        <>
            <h1>Carregando mapa...</h1>
        </>
    );
}
