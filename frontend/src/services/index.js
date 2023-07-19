import axios from 'axios';

const api = axios.create({
    baseURL: 'http://ec2-13-59-209-162.us-east-2.compute.amazonaws.com/',
    //baseURL: 'http://localhost:8080/',
});

export default api;
