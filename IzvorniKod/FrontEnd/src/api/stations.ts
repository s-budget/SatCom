import axios from "axios";

export const getLinkStations = async(id: number) => {
    const res =await axios.get(`links/${id}/stations`);
    return res.data;
}

export const getAllStations =async () => {
    const res = await axios.get('stations');
    return res.data;
}

export const getIsPresent = async(id: number) => {
    const res =await axios.get(`stations/${id}/isPresent`);
    return res.data;
}