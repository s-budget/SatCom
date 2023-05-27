import axios from "axios";
import { ITransmitter } from "../models/transmitter";

export const getAllTransmitters = async () => {
    const response = await axios.get("/transmitters");
    return response.data;
}

export const getTransmForSatellite = async (id: number) => {
    const response = await axios.get(`/transmitters/forSat/${id}`);
    return response.data;
}

export const createNewTransmitter = async (transmitter: ITransmitter, id: number)=> {
    await axios.post("/transmitters", { ...transmitter, owningSatellite: { satId: id }});
};

export const getTransmitterById = async (id: number) => {
    const response = await axios.get(`/transmitters/${id}`)
    return response.data as ITransmitter; 
}

export const editTransmitter = async (id: number, transmitter: ITransmitter) => {
     
    await axios.put(`/transmitters/${id}`, 
    {transmName: transmitter.transmName, transmMode: transmitter.transmMode,
     transmBaud: transmitter.transmBaud, transmFreq: transmitter.transmFreq});
};

export const deleteTransmitter = async (id: number) => {
    await axios.delete(`/transmitters/${id}`);
}

