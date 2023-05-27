import axios from "axios";

export const getAllModes = async() => {
    const res = await axios.get("/modes");
    return res.data;
}