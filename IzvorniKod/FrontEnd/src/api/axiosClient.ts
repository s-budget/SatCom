import axios, { AxiosStatic } from "axios";
import { UserData } from "../models/userData";


/*
Axios is an HTTP client library based on promises. 
It makes sending asynchronous HTTP requests to REST endpoints
*/

if (window.location.hostname === "localhost") {
    var port = 8080;
    axios.defaults.baseURL = "http://" + window.location.hostname + ":" + port + "/api";
} else {
    axios.defaults.baseURL = "http://51.103.117.168:8080/api";
}


export const configureAxiosClient = (axios: AxiosStatic) => {
    axios.interceptors.request.use(config => {
        const userData = sessionStorage.getItem("user");
        if (userData) {
            const user = JSON.parse(userData) as UserData;
            config.headers!.Authorization = `Bearer ${user.token}`;
        }
        return config;
    }); 
    axios.interceptors.request.use(config => {
        config.baseURL = config.baseURL ?? `/api/`;
        return config;
    });
    axios.interceptors.request.use(config => {
        config.paramsSerializer = {
            indexes: null,
        };
        return config;
    });
    axios.interceptors.response.use(
        response => {
            return response;
        },
        error => {
            console.error(error);
            return Promise.reject(error);
        }
    );
};

declare module "axios" {
    export interface AxiosRequestConfig {
        apiVersion?: number;
    }
}
