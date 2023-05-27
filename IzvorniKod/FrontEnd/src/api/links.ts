import axios from "axios";
import { ILink } from "../models/link";

export const getAllLinks = async () => {
    const response = await axios.get("/links");
    return response.data;
} 

export const getCompatibileLinks = async (id: string) => {
    const response = await axios.get(`/satellites/${id}/links`);
    return response.data;
} 
export const createNewLink = async (link: ILink) => {
    await axios.post("/links", link);
};

export const getLinkById = async (id: number) => {
    const response = await axios.get(`/links/${id}`)
    return response.data as ILink; 
}

export const editLink = async (id: number, link: ILink) => {
    await axios.put(`/links/${id}`, {linkId: link.linkId, linkBaud: link.linkBaud, linkFreq: link.linkFreq, linkMode: link.linkMode});
};

export const deleteLink = async (id: number) => {
    await axios.delete(`/links/${id}`);
}

