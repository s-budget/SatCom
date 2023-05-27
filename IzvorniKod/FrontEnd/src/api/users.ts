import axios, { AxiosResponse } from "axios";
import { useSelector } from "react-redux";
import { UserData } from "../models/userData";
import { AppState } from "../store/configureStore";

export const login = async (username: string, password: string) => {
    const res = await axios.post("/auth/login", { username, password });
    const user = await handleResponse(res);
    if (user.token) {
        sessionStorage.setItem("user", JSON.stringify(user));
        // sessionStorage.setItem("loggedIn", JSON.stringify(true));
    }
    return user;
};

const handleResponse = async (res: AxiosResponse) => {
    const data = res.data;
    if (res.status !== 200) {
        if (res.status === 401) logout();
        if (res.status === 403) return Promise.reject("403");
        const error = (data && data.message) || res.statusText;
        return Promise.reject(error);
    }
    return data || res.status === 200;
};

export const logout = () => {
    sessionStorage.removeItem("user");
    // sessionStorage.setItem("loggedIn", JSON.stringify(false));
};

export const createUser = async (username: string, email: string, role: string, password: string) => {
    const res = await axios.post("/auth/signup", {username, email, role, password});
    return res.status;
}

export const updateUser = async (userId: number, username: string, password: string) => {
    let res: AxiosResponse<any>;
    if(username?.length > 0 && password?.length > 0) {
        res = await axios.put("/users/" + userId, {username, password});
    } else if(username.length > 0) {
        res = await axios.put("/users/" + userId, {username});
    } else {
        res = await axios.put("/users/" + userId, {password});
    }

    return res.status;

}

export const getAllUsers = async () => {
    const res = await axios.get("/users");
    return res.data;
}

export const getUserData =async (userId: number) => {
    const res = await axios.get("/users/" + userId);
    return res.data;
}

export const deleteUser = async (userId: number) => {
    const res = await axios.delete("/users/"+ userId);
    return res.data;
}