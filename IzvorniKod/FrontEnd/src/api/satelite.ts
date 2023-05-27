import axios from "axios";
import { ISatelite } from "../models/satelite";

export const getAllSatelites = async () => {
    const response = await axios.get("/satellites");
    return response.data;
} 

export const createNewSatelite = async (satellite: ISatelite) => {
    await axios.post("/satellites", satellite);
};

export const getSateliteById = async (id: number) => {
    const response = await axios.get(`/satellites/${id}`)
    return response.data as ISatelite; 
}

export const editSatelite = async (id: number, satellite: ISatelite) => {
    await axios.put(`/satellites/${id}`, { satName: satellite.satName});
};

export const deleteSatelite = async (id: number) => {
    await axios.delete(`/satellites/${id}`);
}

export const getSatelliteTransmitters =async (id: number) => {
    const response = await axios.get(`/satellites/${id}/transmitters`);
    return response.data;
}
