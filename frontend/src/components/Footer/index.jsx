import styles from './styles.module.css'

const Footer = () => {
    return (
            <footer className={styles.containerFooter}>
                <div className={styles.logo}>
                    <p>©2023 Royal Reservations</p>
                </div>
                <ul className={styles.listFooter}>
                    <li>
                        <a href="https://pt-br.facebook.com/"><img src="../../images/facebook.png" alt="facebook-logo" /></a>
                    </li>
                    <li>
                        <a href="https://linkedin.com/"><img src="../../images/linkedin.png" alt="linkedin-logo" /></a>
                    </li>
                    <li>
                        <a href="https://twitter.com/"><img src="../../images/twitter.png" alt="twitter-logo" /></a>
                    </li>
                    <li>
                        <a href="https://www.instagram.com/"><img src="../../images/instagram.png" alt="instagram-logo" /></a>
                    </li>
                </ul>
            </footer>
    )
}

export default Footer